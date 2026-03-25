import Navbar from "@/components/Navbar";
import HeroSection from "@/components/HeroSection";
import StatsBar from "@/components/StatsBar";
import SearchForm from "@/components/SearchForm";
import FeaturesSection from "@/components/FeaturesSection";
import FeaturedMembers from "@/components/FeaturedMembers";
import SuccessStories from "@/components/SuccessStories";
import VideoCarousel from "@/components/VideoCarousel";
import Footer from "@/components/Footer";

const Index = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <HeroSection />
      <div className="relative">
        <StatsBar />
        <SearchForm />
      </div>
      <FeaturesSection />
      <SuccessStories />
      <FeaturedMembers />
      <VideoCarousel />
      <Footer />
    </div>
  );
};

export default Index;
