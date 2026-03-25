import profile1 from "@/assets/profile1.jpg";
import profile2 from "@/assets/profile2.jpg";
import profile3 from "@/assets/profile3.jpg";
import profile4 from "@/assets/profile4.jpg";

const profiles = [
  { name: "Priya", age: 25, city: "Pune", image: profile1 },
  { name: "Sneha", age: 24, city: "Mumbai", image: profile2 },
  { name: "Aarushi", age: 26, city: "Delhi", image: profile3 },
  { name: "Neha", age: 23, city: "Bangalore", image: profile4 },
];

const FeaturedMembers = () => {
  return (
    <section className="bg-muted/50 py-12">
      <div className="container mx-auto px-4">
        <h2 className="text-3xl font-display font-bold text-foreground mb-8">
          Featured Members
        </h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 md:gap-6">
          {profiles.map((profile, i) => (
            <ProfileCard key={i} {...profile} />
          ))}
        </div>
      </div>
    </section>
  );
};

const ProfileCard = ({ name, age, city, image }) => (
  <div className="bg-card rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-all duration-150 hover:-translate-y-1 group border border-border">
    <div className="aspect-[3/4] overflow-hidden">
      <img
        src={image}
        alt={name}
        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
      />
    </div>
    <div className="p-3">
      <p className="font-body font-semibold text-foreground text-sm">
        <span className="text-primary font-bold">{name}</span>, {age}, {city}
      </p>
    </div>
  </div>
);

export default FeaturedMembers;
