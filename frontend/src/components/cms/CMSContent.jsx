import DOMPurify from "dompurify";

// Strict DOMPurify configuration for CMS HTML rendering
const sanitizeCMSHtml = (content) => {
  if (!content || typeof content !== "string") return "";

  // Hook to ensure external links have target="_blank" and rel="noopener noreferrer"
  DOMPurify.addHook("afterSanitizeAttributes", (node) => {
    if (node.tagName === "A" && node.hasAttribute("href")) {
      const href = node.getAttribute("href");
      if (href.startsWith("http://") || href.startsWith("https://")) {
        node.setAttribute("target", "_blank");
        node.setAttribute("rel", "noopener noreferrer");
      }
    }
  });

  const sanitized = DOMPurify.sanitize(content, {
    ALLOWED_TAGS: [
      "h1", "h2", "h3", "h4", "h5", "h6",
      "p", "br", "hr", "strong", "b", "em", "i", "u", "s", "strike",
      "ul", "ol", "li", "blockquote", "pre", "code",
      "table", "thead", "tbody", "tfoot", "tr", "th", "td",
      "a", "img", "span", "div", "section", "article"
    ],
    ALLOWED_ATTR: [
      "href", "src", "alt", "title", "class", "className",
      "target", "rel", "width", "height", "align"
    ],
    FORBID_TAGS: ["script", "iframe", "object", "embed", "form", "input", "button", "svg", "math"],
    FORBID_ATTR: ["style", "onerror", "onload", "onclick", "onmouseover", "onfocus", "onblur"],
    ALLOWED_URI_REGEXP: /^(?:(?:(?:f|ht)tps?|mailto|tel|callto|cid|xmpp):|[^a-z]|[a-z+.\-]+(?:[^a-z+.\-:]|$))/i,
    ALLOW_DATA_ATTR: false,
  });

  DOMPurify.removeHook("afterSanitizeAttributes");
  return sanitized;
};

const CMSContent = ({ html }) => {
  return (
    <div className="container mx-auto px-4 py-12">
      <div
        className="cms-content max-w-5xl mx-auto bg-white rounded-2xl border border-border shadow-sm p-8 md:p-10"
        dangerouslySetInnerHTML={{
          __html: sanitizeCMSHtml(html),
        }}
      />
    </div>
  );
};

export default CMSContent;