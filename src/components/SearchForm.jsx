import { ChevronDown, Search } from "lucide-react";

const SearchForm = () => {
  return (
    <section className="bg-background py-10" >
      
      <div className="container mx-auto px-4">
        <div className="bg-card rounded-2xl shadow-xl p-8 max-w-3xl ml-auto -mt-20 relative z-20 border border-border">
         
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">
            Search Your Partner
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
            <SelectField label="I'm Looking For..." />
            <SelectField label="Age" />
            <SelectField label="Religion" />
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <SelectField label="Religion" />
            <SelectField label="City" />
            <button className="flex items-center justify-center gap-2 bg-orange-cta hover:bg-orange-cta/90 text-primary-foreground font-semibold py-3 px-6 rounded-lg transition-all duration-150 shadow-md hover:shadow-lg">
              <Search className="h-4 w-4" />
              Search
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

const SelectField = ({ label }) => (
  <div className="relative">
    <select className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-3 pr-10 text-sm text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors">
      <option>{label}</option>
    </select>
    <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground pointer-events-none" />
  </div>
);

export default SearchForm;
