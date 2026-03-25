const StatsBar = () => {
  const stats = [
    { value: "+20,000", label: "Success Stories" },
    { value: "50,000+", sublabel: "Trusted by", label: "Members" },
    { value: "500+", sublabel: "Matches Across", label: "Cities" },
  ];

  return (
    <section className="bg-gold py-5">
      <div className="container mx-auto px-4 flex flex-wrap justify-center md:justify-around items-center gap-6 md:gap-0">
        {stats.map((stat, i) => (
          <div key={i} className="flex items-center gap-3">
            <div className="text-center">
              {stat.sublabel && (
                <p className="text-primary-foreground/80 text-xs font-medium">{stat.sublabel}</p>
              )}
              <p className="text-3xl md:text-4xl font-display font-bold text-primary-foreground">{stat.value}</p>
              <p className="text-primary-foreground/90 text-sm font-medium">{stat.label}</p>
            </div>
            {i < stats.length - 1 && (
              <div className="hidden md:block h-12 w-px bg-primary-foreground/30 ml-8" />
            )}
          </div>
        ))}
      </div>
    </section>
  );
};

export default StatsBar;
