import { useState } from "react";
import { ChevronDown, Search } from "lucide-react";
import MatrimonySelect from "./MatrimonySelect";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";
import { useLoading } from "@/hooks/useLoading";
import { useLanguage } from "@/context/LanguageContext";

const SearchForm = () => {
  const { t } = useLanguage();
  const { getOptions, addCustomOption } = useMatrimonyOptions();
  const { startLoading, stopLoading } = useLoading();
  const [searchData, setSearchData] = useState({
    lookingFor: "",
    age: "",
    religion: "",
    caste: "",
    city: ""
  });

  const handleSearch = async () => {
    startLoading('Searching...');
    // Simulate search
    setTimeout(() => {
      stopLoading();
    }, 2000);
  };

  const handleFieldChange = (field, value) => {
    setSearchData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <section className="bg-background py-10" >
      
      <div className="container mx-auto px-4">
        <div className="bg-card rounded-2xl shadow-xl p-8 max-w-3xl ml-auto -mt-20 relative z-20 border border-border">
         
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">
            {t.searchForm?.heading || "Search Your Partner"}
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
            <SelectField 
              label={t.searchForm?.lookingFor || "I'm Looking For..."} 
              options={["Male", "Female", "Other"]}
              value={searchData.lookingFor}
              onChange={(value) => handleFieldChange('lookingFor', value)}
            />
            <SelectField 
              label={t.searchForm?.age || "Age"} 
              options={["18-25", "26-35", "36-45", "46+"]}
              value={searchData.age}
              onChange={(value) => handleFieldChange('age', value)}
            />
            <MatrimonySelect
              options={getOptions('religion')}
              value={searchData.religion}
              onChange={(value) => handleFieldChange('religion', value)}
              placeholder={t.searchForm?.religion || "Religion"}
              fieldType="religion"
              onAddCustom={addCustomOption}
            />
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <MatrimonySelect
              options={getOptions('caste')}
              value={searchData.caste}
              onChange={(value) => handleFieldChange('caste', value)}
              placeholder={t.searchForm?.caste || "Caste"}
              fieldType="caste"
              onAddCustom={addCustomOption}
            />
            <MatrimonySelect
              options={getOptions('city')}
              value={searchData.city}
              onChange={(value) => handleFieldChange('city', value)}
              placeholder={t.searchForm?.city || "City"}
              fieldType="city"
              onAddCustom={addCustomOption}
            />
            <button 
              onClick={handleSearch}
              className="flex items-center justify-center gap-2 bg-orange-cta hover:bg-orange-cta/90 text-primary-foreground font-semibold py-3 px-6 rounded-lg transition-all duration-150 shadow-md hover:shadow-lg"
            >
              <Search className="h-4 w-4" />
              {t.searchForm?.searchButton || "Search"}
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

const SelectField = ({ label, options, value, onChange }) => (
  <div className="relative">
    <select 
      value={value}
      onChange={(e) => onChange(e.target.value)}
      className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-3 pr-10 text-sm text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
    >
      <option value="">{label}</option>
      {options?.map(opt => (
        <option key={opt} value={opt}>{opt}</option>
      ))}
    </select>
    <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground pointer-events-none" />
  </div>
);

export default SearchForm;
