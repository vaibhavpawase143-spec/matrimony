import { useParams, Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import ShortlistButton from "@/components/ShortlistButton";
import { motion, AnimatePresence } from "framer-motion";

import {
  Heart,
  MapPin,
  GraduationCap,
  Briefcase,
  Calendar,
  ArrowLeft,
  Star,
  MessageSquare,
  User,
  Users,
  Phone,
  Coffee,
  Plus,
  Minus,
  Lock,
  Crown,
  ShieldCheck,
  Building
} from "lucide-react";
import { photoAPI } from "@/services/api";

import { useLanguage } from "@/context/LanguageContext";
import {
  profileAPI,
  interestAPI,
  profileVisitorAPI,
  blockAPI,
  subscriptionAPI
} from "@/services/api";
import toast from "react-hot-toast";
import profile1 from "@/assets/profile1.jpg";
import { resolveImageUrl } from "@/utils/urlSecurity";

const InfoRow = ({ label, value }) => {
  const displayValue =
    value !== null && value !== undefined && value !== ""
      ? String(value)
      : "Not specified";

  return (
    <div className="flex justify-between items-center py-3 border-b border-border/60 last:border-0 gap-4 transition-colors">
      <span className="text-sm font-medium text-muted-foreground shrink-0">
        {label}
      </span>
      <span className="text-sm font-semibold text-foreground text-right break-words">
        {displayValue}
      </span>
    </div>
  );
};

const AccordionSection = ({
  title,
  icon: Icon,
  isOpen,
  onToggle,
  children
}) => {
  return (
    <div className="bg-card rounded-2xl border border-border shadow-sm overflow-hidden transition-all duration-200 hover:shadow-md">
      <button
        type="button"
        onClick={onToggle}
        className="w-full flex items-center justify-between p-5 md:p-6 text-left focus:outline-none select-none hover:bg-muted/30 transition-colors group cursor-pointer"
        aria-expanded={isOpen}
      >
        <div className="flex items-center gap-3">
          {Icon && (
            <div className="h-9 w-9 rounded-xl bg-pink-500/10 text-pink-600 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
              <Icon className="h-5 w-5" />
            </div>
          )}
          <h2 className="text-lg md:text-xl font-bold font-display text-foreground tracking-tight">
            {title}
          </h2>
        </div>

        <div className="h-8 w-8 rounded-full bg-muted/60 group-hover:bg-pink-500/15 text-muted-foreground group-hover:text-pink-600 flex items-center justify-center transition-colors shrink-0">
          {isOpen ? (
            <Minus className="h-5 w-5 stroke-[2.5]" />
          ) : (
            <Plus className="h-5 w-5 stroke-[2.5]" />
          )}
        </div>
      </button>

      <AnimatePresence initial={false}>
        {isOpen && (
          <motion.div
            key="content"
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: "auto", opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ duration: 0.3, ease: [0.04, 0.62, 0.23, 0.98] }}
          >
            <div className="px-5 pb-5 md:px-6 md:pb-6 pt-1 border-t border-border/60">
              {children}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

const ProfileDetails = () => {
  const navigate = useNavigate();
  const { t } = useLanguage();
  const [currentPhotoIndex, setCurrentPhotoIndex] = useState(0);

  // Accordion state with mandated default values:
  // Personal Details: OPEN, Education & Career: OPEN, others: COLLAPSED
  const [openSections, setOpenSections] = useState({
    personal: true,
    education: true,
    family: false,
    location: false,
    contact: false,
    lifestyle: false,
  });

  const toggleSection = (sectionKey) => {
    setOpenSections((prev) => ({
      ...prev,
      [sectionKey]: !prev[sectionKey],
    }));
  };

  const calculateAge = (dob) => {
    if (!dob) return "-";
    const birth = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const month = today.getMonth() - birth.getMonth();

    if (month < 0 || (month === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    return age;
  };

  const [galleryPhotos, setGalleryPhotos] = useState([]);
  const [showGallery, setShowGallery] = useState(false);
  const { id } = useParams();

  const [profile, setProfile] = useState(null);
  const [profileNotFound, setProfileNotFound] = useState(false);
  const [blockedProfile, setBlockedProfile] = useState(false);

  const [interestSent, setInterestSent] = useState(false);
  const [canViewContact, setCanViewContact] = useState(false);
  const [isPremiumUser, setIsPremiumUser] = useState(false);
  const [showUpgradePopup, setShowUpgradePopup] = useState(false);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const data = await profileAPI.getProfileById(id);

        if (!data) {
          setProfileNotFound(true);
          return;
        }

        console.log("========== VIEW PROFILE ==========");
        console.log("PROFILE ID =", id);
        console.log("PROFILE RESPONSE =", data);
        console.log("==================================");

        setProfile(data);

        const targetUserId = data.userId || data.id;

        // Perform secondary enrichment tasks safely without blocking profile display
        try {
          const currentUserStr = sessionStorage.getItem("user") || localStorage.getItem("user") || "{}";
          const currentUser = JSON.parse(currentUserStr);
          const currentUserId = Number(
            currentUser?.userId || currentUser?.id || currentUser?.profile?.userId
          );

          // 1. My profile check for premium
          try {
            const myProfile = await profileAPI.getProfile();
            setIsPremiumUser(Boolean(myProfile?.isPremium));
          } catch (e) {
            console.warn("My profile load error in ProfileDetails:", e);
          }

          // 2. Blocked users check
          if (currentUserId && targetUserId) {
            try {
              const blockedUsers = await blockAPI.getMyBlockedUsers(currentUserId);
              const blockedList = Array.isArray(blockedUsers) ? blockedUsers : [];
              const blockedIds = blockedList.map((u) => u.blockedId);
              if (blockedIds.includes(targetUserId)) {
                setBlockedProfile(true);
                return;
              }
            } catch (e) {
              console.warn("Blocked check error:", e);
            }
          }

          // 3. User gallery photos
          if (targetUserId) {
            try {
              const galleryResponse = await photoAPI.getUserPhotos(targetUserId);
              setGalleryPhotos(galleryResponse?.photos || []);
            } catch (e) {
              console.warn("Photos load error:", e);
            }
          }

          // 4. Record profile visit
          if (currentUserId && targetUserId && currentUserId !== targetUserId) {
            try {
              await profileVisitorAPI.saveVisit(targetUserId);
              window.dispatchEvent(new Event("dashboardUpdated"));
            } catch (e) {
              console.warn("Visitor record error:", e);
            }
          }

          // 5. Sent interests status
          if (currentUserId && targetUserId) {
            try {
              const sentInterests = await interestAPI.getSentInterests(currentUserId);
              const sentList = Array.isArray(sentInterests) ? sentInterests : [];
              const acceptedInterest = sentList.find(
                (item) =>
                  Number(item.receiverId) === Number(targetUserId) &&
                  item.status === "ACCEPTED"
              );
              setCanViewContact(!!acceptedInterest);
              const alreadySent = sentList.some(
                (item) => Number(item.receiverId) === Number(targetUserId)
              );
              setInterestSent(alreadySent);
            } catch (e) {
              console.warn("Sent interests check error:", e);
            }
          }
        } catch (innerErr) {
          console.warn("Secondary data load failed:", innerErr);
        }
      } catch (err) {
        console.error("Profile load error:", err);
        setProfileNotFound(true);
      }
    };

    if (id) {
      loadProfile();
    }
  }, [id]);

  const handleMessageClick = async () => {
    try {
      const subscription = await subscriptionAPI.getMySubscription();
      if (subscription?.isActive) {
        navigate(`/messages?receiverId=${profile.userId}`);
      } else {
        setShowUpgradePopup(true);
      }
    } catch {
      setShowUpgradePopup(true);
    }
  };

  const handleSendInterest = async () => {
    try {
      const currentUser = JSON.parse(sessionStorage.getItem("user") || localStorage.getItem("user") || "{}");

      if (!profile) {
        toast.error("User not found");
        return;
      }

      const senderId = Number(
        currentUser?.userId || currentUser?.id || currentUser?.profile?.userId
      );

      if (!senderId) {
        toast.error("Current user ID not found");
        return;
      }

      if (interestSent) {
        toast("Interest already sent ❤️");
        return;
      }

      const receiverId = Number(profile.userId);

      if (senderId === receiverId) {
        toast.error("You cannot send interest to yourself");
        return;
      }

      await interestAPI.sendInterest(senderId, receiverId);
      setInterestSent(true);
      toast.success("Interest Sent Successfully ❤️");
    } catch (err) {
      console.log(err);

      if (err?.message?.includes("Daily limit reached")) {
        toast.error(
          "Daily limit reached.\nUpgrade to Premium for unlimited interests."
        );
        return;
      }

      toast.error(err?.message || "Failed");
    }
  };

  if (blockedProfile) {
    return (
      <div className="min-h-screen flex items-center justify-center text-2xl font-bold">
        🚫 This profile is blocked
      </div>
    );
  }

  if (profileNotFound) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-3xl font-bold mb-3">Profile Not Found</h1>
          <p className="text-gray-500 mb-5">
            This profile is unavailable or has been removed.
          </p>
          <button
            onClick={() => navigate("/home")}
            className="bg-pink-600 text-white px-5 py-2 rounded-lg"
          >
            Go Home
          </button>
        </div>
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="min-h-screen flex items-center justify-center text-muted-foreground">
        Loading Profile...
      </div>
    );
  }

  return (
    <>
      <div className="min-h-screen bg-muted/30 pb-12">
        <div className="container mx-auto px-4 py-8 max-w-6xl">
          <Link
            to="/search"
            className="inline-flex items-center gap-2 mb-6 text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
          >
            <ArrowLeft size={16} />
            Back to Search
          </Link>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* LEFT COLUMN: Profile Card & Actions */}
            <div>
              <div className="sticky top-20 bg-card rounded-2xl overflow-hidden border border-border shadow-sm">
                <div className="relative">
                  {profile.isPremium && (
                    <div className="absolute top-4 left-4 z-10 bg-gradient-to-r from-amber-400 to-yellow-500 text-white px-3 py-1 rounded-full text-xs font-bold shadow-lg flex items-center gap-1.5">
                      <Crown size={14} /> PREMIUM
                    </div>
                  )}

                  <img
                    src={resolveImageUrl(profile.imageUrl || profile.profilePhotoUrl, profile1)}
                    alt={`${profile.firstName || "User"} ${profile.lastName || ""}`}
                    className="w-full aspect-[3/4] object-cover"
                    onError={(e) => {
                      e.target.src = profile1;
                    }}
                  />
                </div>

                <div className="p-5 space-y-3">
                  <button
                    onClick={handleSendInterest}
                    disabled={interestSent}
                    className={`w-full text-white font-semibold rounded-xl py-3 flex items-center justify-center gap-2 shadow-md transition-all ${
                      interestSent
                        ? "bg-emerald-600 hover:bg-emerald-700"
                        : "bg-gradient-to-r from-pink-600 to-rose-600 hover:from-pink-700 hover:to-rose-700 active:scale-98"
                    }`}
                  >
                    <Heart size={18} className={interestSent ? "fill-white" : ""} />
                    {interestSent ? "Interest Sent" : "Send Interest"}
                  </button>

                  <button
                    onClick={handleMessageClick}
                    className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold rounded-xl py-3 flex items-center justify-center gap-2 shadow-md transition-all active:scale-98"
                  >
                    <MessageSquare size={18} />
                    Message
                  </button>

                  <button
                    onClick={() => {
                      if (!isPremiumUser) {
                        toast.error(
                          "Upgrade to Premium to view this user's photo gallery."
                        );
                        return;
                      }

                      if (!galleryPhotos || galleryPhotos.length === 0) {
                        toast("This user has not uploaded any gallery photos.");
                        return;
                      }

                      setCurrentPhotoIndex(0);
                      setShowGallery(true);
                    }}
                    className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl py-3 flex items-center justify-center gap-2 shadow-sm transition-all active:scale-98"
                  >
                    View Photo Gallery
                  </button>

                  <ShortlistButton
                    profileId={profile.id}
                    size="lg"
                    showLabel={true}
                  />
                </div>
              </div>
            </div>

            {/* RIGHT COLUMN: Profile Overview & Accordion Sections */}
            <div className="lg:col-span-2 space-y-6">
              {/* Profile Header Box */}
              <div className="bg-card rounded-2xl p-6 border border-border shadow-sm space-y-4">
                <div className="flex flex-wrap items-center justify-between gap-3">
                  <h1 className="text-2xl md:text-3xl font-bold font-display text-foreground flex items-center gap-2.5">
                    {profile.firstName} {profile.lastName}
                    {profile.isPremium && (
                      <span className="bg-amber-100 dark:bg-amber-950/50 text-amber-700 dark:text-amber-300 text-xs px-2.5 py-1 rounded-full font-semibold inline-flex items-center gap-1">
                        <Crown size={12} /> PREMIUM
                      </span>
                    )}
                  </h1>

                  {profile.verified && (
                    <div className="inline-flex items-center gap-1.5 bg-emerald-100 dark:bg-emerald-950/50 text-emerald-700 dark:text-emerald-300 px-3 py-1 rounded-full text-xs font-semibold">
                      <ShieldCheck size={14} /> Verified Profile
                    </div>
                  )}
                </div>

                <div className="flex flex-wrap gap-4 text-sm text-muted-foreground pt-1">
                  <span className="flex items-center gap-1.5">
                    <Calendar size={15} className="text-pink-600" />
                    {calculateAge(profile.dateOfBirth)} yrs
                  </span>

                  <span className="flex items-center gap-1.5">
                    <MapPin size={15} className="text-pink-600" />
                    {profile.cityName || "-"}
                  </span>

                  <span className="flex items-center gap-1.5">
                    <GraduationCap size={15} className="text-pink-600" />
                    {profile.educationLevelName || "-"}
                  </span>

                  <span className="flex items-center gap-1.5">
                    <Briefcase size={15} className="text-pink-600" />
                    {profile.occupationName || "-"}
                  </span>
                </div>

                {profile.aboutMe && (
                  <p className="text-sm leading-relaxed text-muted-foreground pt-3 border-t border-border/60">
                    {profile.aboutMe}
                  </p>
                )}
              </div>

              {/* ACCORDION INFORMATION SECTIONS */}
              <div className="space-y-4">
                {/* 1. PERSONAL DETAILS (Default: OPEN) */}
                <AccordionSection
                  title="Personal Details"
                  icon={User}
                  isOpen={openSections.personal}
                  onToggle={() => toggleSection("personal")}
                >
                  <InfoRow label="Gender" value={profile.genderName} />
                  <InfoRow label="Religion" value={profile.religionName} />
                  <InfoRow label="Caste" value={profile.casteName} />
                  <InfoRow label="Sub Caste" value={profile.subCasteName} />
                  <InfoRow label="Mother Tongue" value={profile.motherTongueName} />
                  <InfoRow label="Marital Status" value={profile.maritalStatusName} />
                  <InfoRow label="Height" value={profile.heightValue} />
                  <InfoRow label="Weight" value={profile.weightValue} />
                  <InfoRow label="Blood Group" value={profile.bloodGroupName} />
                  <InfoRow label="Manglik" value={profile.manglikStatusName} />
                  <InfoRow label="Disability" value={profile.disabilityStatusName} />
                </AccordionSection>

                {/* 2. EDUCATION & CAREER (Default: OPEN) */}
                <AccordionSection
                  title="Education & Career"
                  icon={GraduationCap}
                  isOpen={openSections.education}
                  onToggle={() => toggleSection("education")}
                >
                  <InfoRow label="Qualification" value={profile.qualificationName} />
                  <InfoRow label="Field Of Study" value={profile.fieldOfStudyName} />
                  <InfoRow label="Education" value={profile.educationLevelName} />
                  <InfoRow label="Occupation" value={profile.occupationName} />
                  <InfoRow label="Employment" value={profile.employedStatusName} />
                  <InfoRow label="Income" value={profile.incomeValue} />
                  <InfoRow label="Company" value={profile.companyName} />
                </AccordionSection>

                {/* 3. FAMILY DETAILS (Default: COLLAPSED) */}
                <AccordionSection
                  title="Family Details"
                  icon={Users}
                  isOpen={openSections.family}
                  onToggle={() => toggleSection("family")}
                >
                  <InfoRow label="Father's Name" value={profile.fatherName} />
                  <InfoRow label="Father's Occupation" value={profile.fatherOccupation} />
                  <InfoRow label="Mother's Name" value={profile.motherName} />
                  <InfoRow label="Mother's Occupation" value={profile.motherOccupation} />
                  <InfoRow label="Number of Siblings" value={profile.siblingsCount} />
                  <InfoRow label="Number of Brothers" value={profile.brothersCount} />
                  <InfoRow label="Number of Sisters" value={profile.sistersCount} />
                  <InfoRow label="Family Type" value={profile.familyTypeName} />
                  <InfoRow label="Family Status" value={profile.familyStatusName} />
                  <InfoRow label="Family Value" value={profile.familyValueName} />
                  <InfoRow label="Aunt Details" value={profile.aunt} />
                  <InfoRow label="Nanihal Details" value={profile.nanihalDetails} />
                  <InfoRow label="Best Friend" value={profile.bestFriend} />
                  <InfoRow label="Number of Uncles" value={profile.unclesCount} />
                  {profile.uncle1Name && <InfoRow label="Uncle 1 Name" value={profile.uncle1Name} />}
                  {profile.uncle2Name && <InfoRow label="Uncle 2 Name" value={profile.uncle2Name} />}
                  {profile.uncle3Name && <InfoRow label="Uncle 3 Name" value={profile.uncle3Name} />}
                  {profile.uncle4Name && <InfoRow label="Uncle 4 Name" value={profile.uncle4Name} />}
                </AccordionSection>

                {/* 4. LOCATION (Default: COLLAPSED) */}
                <AccordionSection
                  title="Location"
                  icon={MapPin}
                  isOpen={openSections.location}
                  onToggle={() => toggleSection("location")}
                >
                  <InfoRow label="Country" value={profile.countryName} />
                  <InfoRow label="State" value={profile.stateName} />
                  <InfoRow label="City" value={profile.cityName} />
                  <InfoRow label="Address" value={profile.address} />
                </AccordionSection>

                {/* 5. CONTACT DETAILS (Default: COLLAPSED) */}
                <AccordionSection
                  title="Contact Details"
                  icon={Phone}
                  isOpen={openSections.contact}
                  onToggle={() => toggleSection("contact")}
                >
                  <InfoRow
                    label="Email"
                    value={
                      !canViewContact
                        ? "🔒 Send Interest & Get Accepted"
                        : isPremiumUser
                        ? profile.email
                        : "👑 Upgrade to Premium to view Email"
                    }
                  />
                  <InfoRow
                    label="Phone"
                    value={
                      !canViewContact
                        ? "🔒 Send Interest & Get Accepted"
                        : isPremiumUser
                        ? profile.phone
                        : "👑 Upgrade to Premium to view Phone"
                    }
                  />
                </AccordionSection>

                {/* 6. LIFESTYLE (Default: COLLAPSED) */}
                <AccordionSection
                  title="Lifestyle"
                  icon={Coffee}
                  isOpen={openSections.lifestyle}
                  onToggle={() => toggleSection("lifestyle")}
                >
                  <InfoRow label="Diet" value={profile.dietValue} />
                  <InfoRow label="Smoking" value={profile.smokingValue} />
                  <InfoRow label="Drinking" value={profile.drinkingValue} />
                </AccordionSection>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* PHOTO GALLERY MODAL */}
      {showGallery && (
        <div className="fixed inset-0 bg-black/80 z-50 flex items-center justify-center p-4">
          <div className="bg-card rounded-2xl max-w-5xl w-full p-6 max-h-[90vh] overflow-auto shadow-2xl border border-border">
            <div className="flex justify-between items-center mb-4 pb-3 border-b border-border">
              <h2 className="text-xl font-bold">Photo Gallery</h2>
              <button
                onClick={() => setShowGallery(false)}
                className="text-muted-foreground hover:text-foreground text-xl p-1"
              >
                ✕
              </button>
            </div>

            <div className="flex items-center justify-center gap-4">
              {galleryPhotos.length > 1 && (
                <>
                  <button
                    onClick={() =>
                      setCurrentPhotoIndex((prev) =>
                        prev === 0 ? galleryPhotos.length - 1 : prev - 1
                      )
                    }
                    className="bg-muted hover:bg-muted/80 px-4 py-2 rounded-xl text-sm font-semibold transition"
                  >
                    ◀ Prev
                  </button>

                  <button
                    onClick={() =>
                      setCurrentPhotoIndex((prev) =>
                        prev === galleryPhotos.length - 1 ? 0 : prev + 1
                      )
                    }
                    className="bg-muted hover:bg-muted/80 px-4 py-2 rounded-xl text-sm font-semibold transition"
                  >
                    Next ▶
                  </button>
                </>
              )}

              {galleryPhotos.length > 0 && (
                <img
                  src={
                    resolveImageUrl(
                      galleryPhotos[currentPhotoIndex]?.photoUrl ||
                      galleryPhotos[currentPhotoIndex]?.imageUrl ||
                      galleryPhotos[currentPhotoIndex]?.url,
                      profile1
                    )
                  }
                  alt=""
                  className="max-h-[70vh] max-w-[70vw] object-contain rounded-xl"
                  onError={(e) => {
                    e.target.src = profile1;
                  }}
                />
              )}
            </div>
          </div>
        </div>
      )}

      {/* UPGRADE POPUP MODAL */}
      {showUpgradePopup && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">
          <div className="bg-card rounded-3xl p-8 w-[420px] text-center border border-border shadow-2xl">
            <div className="text-6xl mb-4">👑</div>
            <h2 className="text-2xl font-bold mb-3">Premium Required</h2>
            <p className="text-muted-foreground text-sm mb-6">
              Chat is available only for Premium members.
            </p>

            <div className="flex justify-center gap-3">
              <button
                onClick={() => {
                  setShowUpgradePopup(false);
                  navigate("/home");
                }}
                className="px-5 py-2.5 rounded-xl bg-muted text-muted-foreground hover:bg-muted/80 font-medium transition"
              >
                Cancel
              </button>

              <button
                onClick={() => {
                  setShowUpgradePopup(false);
                  navigate("/upgrade");
                }}
                className="px-5 py-2.5 rounded-xl bg-gradient-to-r from-pink-600 to-rose-600 text-white font-semibold shadow-md transition"
              >
                Upgrade Premium
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ProfileDetails;