import { Search as SearchIcon, ChevronDown, SlidersHorizontal } from "lucide-react";
import { Link } from "react-router-dom";
import Navbar from "@/components/Navbar";
import profile1 from "@/assets/profile1.jpg";
import profile2 from "@/assets/profile2.jpg";
import profile3 from "@/assets/profile3.jpg";
import profile4 from "@/assets/profile4.jpg";

const results = [
  { id: 1, name: "Priya Sharma", age: 25, city: "Pune", religion: "Hindu", education: "MBA", profession: "Software Engineer", image: profile1 },
  { id: 2, name: "Sneha Patel", age: 24, city: "Mumbai", religion: "Hindu", education: "B.Tech", profession: "Designer", image: profile2 },
  { id: 3, name: "Aarushi Gupta", age: 26, city: "Delhi", religion: "Hindu", education: "M.Tech", profession: "Doctor", image: profile3 },
  { id: 4, name: "Neha Verma", age: 23, city: "Bangalore", religion: "Hindu", education: "BCA", profession: "Teacher", image: profile4 },
  { id: 5, name: "Riya Singh", age: 27, city: "Jaipur", religion: "Hindu", education: "MBA", profession: "CA", image: profile1 },
  { id: 6, name: "Kavya Joshi", age: 25, city: "Ahmedabad", religion: "Hindu", education: "B.Com", profession: "Manager", image: profile2 },
];

const SelectField = ({ label, options }) => (
  <div>
    <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
    <div className="relative">
      <select className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-2.5 pr-10 text-sm text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors">
        {options.map((o) => (
          <option key={o}>{o}</option>
        ))}
      </select>
      <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
    </div>
  </div>
);

const SearchPage = () => {
  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      {/* Header */}
      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Advanced Search</h1>
        <p className="text-primary-foreground/70 text-sm">Find your perfect match with detailed filters</p>
      </div>

      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Filters sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-card rounded-xl border border-border p-5 sticky top-20">
              <div className="flex items-center gap-2 mb-5">
                <SlidersHorizontal className="h-4 w-4 text-primary" />
                <h2 className="text-sm font-semibold text-foreground">Search Filters</h2>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Age Range</label>
                  <div className="grid grid-cols-2 gap-2">
                    <input type="number" placeholder="Min" defaultValue={21} className="w-full bg-background border border-border rounded-lg px-3 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" />
                    <input type="number" placeholder="Max" defaultValue={30} className="w-full bg-background border border-border rounded-lg px-3 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" />
                  </div>
                </div>

                <SelectField label="Religion" options={["All Religions", "Hindu", "Muslim", "Christian", "Sikh", "Buddhist", "Jain"]} />
                <SelectField label="Caste" options={["All Castes", "Brahmin", "Kshatriya", "Vaishya", "General"]} />
                <SelectField label="Location" options={["All India", "Delhi", "Mumbai", "Pune", "Bangalore", "Jaipur", "Ahmedabad"]} />
                <SelectField label="Education" options={["All Education", "B.Tech", "MBA", "M.Tech", "MBBS", "BCA", "B.Com", "Postgraduate"]} />
                <SelectField label="Profession" options={["All Professions", "Engineer", "Doctor", "Teacher", "Designer", "CA", "Manager"]} />

                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Marital Status</label>
                  <div className="relative">
                    <select className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-2.5 pr-10 text-sm text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary">
                      <option>Never Married</option>
                      <option>Divorced</option>
                      <option>Widowed</option>
                    </select>
                    <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
                  </div>
                </div>

                <button className="w-full flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-2.5 rounded-lg text-sm transition-colors">
                  <SearchIcon className="h-4 w-4" />
                  Search
                </button>

                <button className="w-full text-xs text-muted-foreground hover:text-foreground transition-colors py-1">
                  Reset Filters
                </button>
              </div>
            </div>
          </div>

          {/* Results */}
          <div className="lg:col-span-3">
            <div className="flex items-center justify-between mb-5">
              <p className="text-sm text-muted-foreground"><span className="font-semibold text-foreground">{results.length}</span> profiles found</p>
              <div className="relative">
                <select className="appearance-none bg-card border border-border rounded-lg px-4 py-2 pr-10 text-xs text-muted-foreground font-body focus:outline-none">
                  <option>Sort: Relevance</option>
                  <option>Newest First</option>
                  <option>Age: Low to High</option>
                  <option>Age: High to Low</option>
                </select>
                <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3 w-3 text-muted-foreground pointer-events-none" />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
              {results.map((p) => (
                <Link to={`/profile/${p.id}`} key={p.id} className="bg-card rounded-xl overflow-hidden border border-border shadow-sm hover:shadow-md transition-shadow group">
                  <div className="aspect-[3/4] overflow-hidden">
                    <img src={p.image} alt={p.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
                  </div>
                  <div className="p-4">
                    <h3 className="text-sm font-semibold text-foreground">{p.name}, <span className="text-primary">{p.age}</span></h3>
                    <p className="text-xs text-muted-foreground mt-0.5">{p.profession} · {p.city}</p>
                    <p className="text-xs text-muted-foreground">{p.education} · {p.religion}</p>
                    <div className="flex gap-2 mt-3">
                      <button className="flex-1 bg-accent/10 text-accent text-xs font-semibold py-1.5 rounded-lg hover:bg-accent/20 transition-colors">View Profile</button>
                      <button className="flex-1 bg-orange-cta/10 text-orange-cta text-xs font-semibold py-1.5 rounded-lg hover:bg-orange-cta/20 transition-colors">Send Interest</button>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchPage;
