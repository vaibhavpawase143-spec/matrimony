import React, { useState, useRef, useEffect, useId } from "react";
import { createPortal } from "react-dom";
import { Search, ChevronDown, Check, X, Loader2 } from "lucide-react";

/**
 * Helper to extract unique string/numeric ID value from option
 */
const getOptionValue = (opt) => {
  if (opt === null || opt === undefined) return "";
  if (typeof opt === "object") {
    const val =
      opt?.id ??
      opt?.cityId ??
      opt?.stateId ??
      opt?.countryId ??
      opt?.casteId ??
      opt?.subCasteId ??
      opt?.heightId ??
      opt?.bloodGroupId ??
      opt?.value ??
      opt?.name ??
      String(opt);
    return String(val);
  }
  return String(opt);
};

/**
 * Helper to extract human-readable display label from option
 */
const getOptionLabel = (opt) => {
  if (opt === null || opt === undefined) return "";
  if (typeof opt === "object") {
    const label =
      opt?.subCasteName ||
      opt?.subCaste ||
      opt?.name ||
      opt?.value ||
      opt?.label ||
      opt?.height ||
      opt?.bloodGroup ||
      opt?.bloodGroupName ||
      opt?.groupName ||
      opt?.disabilityName ||
      opt?.disabilityStatus ||
      opt?.familyTypeName ||
      opt?.familyStatusName ||
      opt?.familyValueName ||
      opt?.qualificationName ||
      opt?.fieldOfStudyName ||
      opt?.employedStatusName ||
      opt?.cityName ||
      opt?.stateName ||
      opt?.countryName ||
      opt?.casteName ||
      opt?.smokingType ||
      opt?.smokingStatus ||
      opt?.smokingPreference ||
      opt?.drinkingType ||
      opt?.dietType ||
      opt?.dietName ||
      opt?.weight ||
      opt?.range ||
      opt?.type ||
      opt?.status ||
      (opt?.id !== undefined && opt?.id !== null ? String(opt.id) : "");

    return String(label || "");
  }
  return String(opt);
};

/**
 * Helper to deduplicate options by unique ID or normalized display label
 */
const deduplicateOptions = (opts) => {
  if (!Array.isArray(opts)) return [];
  const seen = new Set();
  return opts.filter((opt) => {
    const val = getOptionValue(opt);
    const label = getOptionLabel(opt)?.trim().toLowerCase();
    const key = val ? `id:${val}` : label ? `label:${label}` : null;
    if (!key || seen.has(key)) {
      return false;
    }
    seen.add(key);
    return true;
  });
};

const SearchableSelect = ({
  value = "",
  onChange,
  options = [],
  placeholder = "Select Option",
  loading = false,
  loadingText = "Loading options...",
  emptyText = "No options found",
  disabled = false,
  className = "",
  name = "",
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [coords, setCoords] = useState({
    top: 0,
    left: 0,
    width: 0,
    placement: "bottom",
    maxHeight: 240,
  });

  const selectId = useId();
  const triggerRef = useRef(null);
  const menuRef = useRef(null);
  const searchInputRef = useRef(null);

  const stringValue = value !== null && value !== undefined ? String(value) : "";

  // Update floating menu overlay position relative to trigger button
  const updatePosition = () => {
    if (!triggerRef.current) return;
    const rect = triggerRef.current.getBoundingClientRect();

    const menuHeightEstimate = 280;
    const spaceBelow = window.innerHeight - rect.bottom;
    const spaceAbove = rect.top;

    let placement = "bottom";
    let top = rect.bottom + window.scrollY + 6;
    let computedMaxHeight = Math.min(240, Math.max(120, spaceBelow - 60));

    // Smart Viewport Flipping: Open upward if space below is limited and space above is greater
    if (spaceBelow < 260 && spaceAbove > spaceBelow) {
      placement = "top";
      computedMaxHeight = Math.min(240, Math.max(120, spaceAbove - 60));
      top = rect.top + window.scrollY - Math.min(menuHeightEstimate, spaceAbove - 10) - 6;
    }

    // Align left and width cleanly within viewport
    let left = rect.left + window.scrollX;
    let width = rect.width;

    const viewportWidth = window.innerWidth;
    if (left + width > viewportWidth - 12) {
      left = Math.max(12, viewportWidth - width - 12);
    }
    if (width > viewportWidth - 24) {
      width = viewportWidth - 24;
      left = 12;
    }

    setCoords({
      top,
      left,
      width,
      placement,
      maxHeight: computedMaxHeight,
    });
  };

  // Synchronize position and listen for scroll, resize, outside clicks, and single-instance events
  useEffect(() => {
    if (!isOpen) return;

    updatePosition();

    const handleScrollOrResize = () => {
      updatePosition();
    };

    const handleClickOutside = (event) => {
      if (
        triggerRef.current &&
        !triggerRef.current.contains(event.target) &&
        menuRef.current &&
        !menuRef.current.contains(event.target)
      ) {
        setIsOpen(false);
        setSearchTerm("");
      }
    };

    const handleOtherSelectOpen = (e) => {
      if (e.detail?.id !== selectId) {
        setIsOpen(false);
        setSearchTerm("");
      }
    };

    window.addEventListener("scroll", handleScrollOrResize, true);
    window.addEventListener("resize", handleScrollOrResize);
    document.addEventListener("mousedown", handleClickOutside);
    window.addEventListener("close-searchable-selects", handleOtherSelectOpen);

    return () => {
      window.removeEventListener("scroll", handleScrollOrResize, true);
      window.removeEventListener("resize", handleScrollOrResize);
      document.removeEventListener("mousedown", handleClickOutside);
      window.removeEventListener("close-searchable-selects", handleOtherSelectOpen);
    };
  }, [isOpen, selectId]);

  // Auto focus search input on open
  useEffect(() => {
    if (isOpen) {
      setTimeout(() => {
        searchInputRef.current?.focus();
      }, 50);
    } else {
      setSearchTerm("");
    }
  }, [isOpen]);

  // Key navigation
  const handleKeyDown = (e) => {
    if (e.key === "Escape") {
      setIsOpen(false);
      setSearchTerm("");
    }
  };

  const toggleDropdown = () => {
    if (disabled || loading) return;
    if (!isOpen) {
      window.dispatchEvent(
        new CustomEvent("close-searchable-selects", { detail: { id: selectId } })
      );
      setIsOpen(true);
    } else {
      setIsOpen(false);
      setSearchTerm("");
    }
  };

  // Extract deduplicated & normalized list
  const uniqueRawOptions = deduplicateOptions(options || []);
  const normalizedOptions = uniqueRawOptions.map((opt) => ({
    raw: opt,
    value: getOptionValue(opt),
    label: getOptionLabel(opt),
  }));

  // Find currently selected label
  const selectedOption = normalizedOptions.find((opt) => opt.value === stringValue);
  const displayLabel = selectedOption
    ? selectedOption.label
    : stringValue
    ? `Selected (${stringValue})`
    : "";

  // Filter options based on search query
  const filteredOptions = normalizedOptions.filter((opt) =>
    opt.label.toLowerCase().includes(searchTerm.trim().toLowerCase())
  );

  const handleSelect = (optionValue) => {
    onChange(optionValue);
    setIsOpen(false);
    setSearchTerm("");
  };

  const handleClear = (e) => {
    e.stopPropagation();
    onChange("");
    setSearchTerm("");
  };

  return (
    <div className={`relative w-full text-left ${className}`} onKeyDown={handleKeyDown}>
      {/* Select Trigger */}
      <button
        ref={triggerRef}
        type="button"
        disabled={disabled || loading}
        onClick={toggleDropdown}
        className={`w-full bg-background border border-border rounded-lg px-3.5 py-2.5 text-sm text-foreground flex items-center justify-between gap-2 cursor-pointer transition-all duration-150 outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary ${
          isOpen ? "ring-2 ring-primary/20 border-primary shadow-sm" : "hover:border-primary/50"
        } ${disabled || loading ? "opacity-60 cursor-not-allowed bg-muted" : ""}`}
      >
        <span
          className={`truncate flex-1 text-left ${
            displayLabel ? "text-foreground font-medium" : "text-muted-foreground"
          }`}
        >
          {loading && normalizedOptions.length === 0
            ? loadingText
            : displayLabel || placeholder}
        </span>

        <div className="flex items-center gap-1 shrink-0 text-muted-foreground">
          {loading ? (
            <Loader2 className="h-4 w-4 animate-spin text-primary" />
          ) : (
            <>
              {stringValue && !disabled && (
                <span
                  role="button"
                  tabIndex={0}
                  onClick={handleClear}
                  className="p-0.5 rounded-full hover:bg-muted text-muted-foreground hover:text-foreground transition-colors"
                  title="Clear selection"
                >
                  <X className="h-3.5 w-3.5" />
                </span>
              )}
              <ChevronDown
                className={`h-4 w-4 transition-transform duration-200 ${
                  isOpen ? "rotate-180 text-primary" : ""
                }`}
              />
            </>
          )}
        </div>
      </button>

      {/* Floating Dropdown Menu rendered via Portal to document.body */}
      {isOpen &&
        createPortal(
          <div
            ref={menuRef}
            style={{
              position: "absolute",
              top: `${coords.top}px`,
              left: `${coords.left}px`,
              width: `${coords.width}px`,
              zIndex: 99999,
            }}
            className="bg-popover border border-border rounded-xl shadow-xl overflow-hidden animate-in fade-in-50 zoom-in-95 pointer-events-auto"
            onMouseDown={(e) => e.stopPropagation()}
          >
            {/* Search Header */}
            <div className="p-2 border-b border-border bg-muted/30 sticky top-0 z-10">
              <div className="relative flex items-center">
                <Search className="absolute left-3 h-4 w-4 text-muted-foreground pointer-events-none" />
                <input
                  ref={searchInputRef}
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Search..."
                  className="w-full bg-background border border-border rounded-lg pl-9 pr-8 py-1.5 text-xs text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary"
                />
                {searchTerm && (
                  <button
                    type="button"
                    onClick={() => setSearchTerm("")}
                    className="absolute right-2.5 text-muted-foreground hover:text-foreground"
                  >
                    <X className="h-3.5 w-3.5" />
                  </button>
                )}
              </div>
            </div>

            {/* Options List */}
            <div
              className="overflow-y-auto p-1 space-y-0.5 text-sm"
              style={{ maxHeight: `${coords.maxHeight}px` }}
            >
              {filteredOptions.length === 0 ? (
                <div className="px-3 py-6 text-center text-xs text-muted-foreground">
                  {emptyText}
                </div>
              ) : (
                filteredOptions.map((opt) => {
                  const isSelected = opt.value === stringValue;
                  return (
                    <button
                      key={`${opt.value}-${opt.label}`}
                      type="button"
                      onClick={() => handleSelect(opt.value)}
                      className={`w-full flex items-center justify-between px-3 py-2 rounded-lg text-xs md:text-sm text-left transition-colors cursor-pointer ${
                        isSelected
                          ? "bg-primary/10 text-primary font-semibold"
                          : "text-foreground hover:bg-muted/70"
                      }`}
                    >
                      <span className="truncate">{opt.label}</span>
                      {isSelected && <Check className="h-4 w-4 shrink-0 text-primary ml-2" />}
                    </button>
                  );
                })
              )}
            </div>
          </div>,
          document.body
        )}
    </div>
  );
};

export default SearchableSelect;

