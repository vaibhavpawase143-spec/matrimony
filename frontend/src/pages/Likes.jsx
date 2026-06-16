import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { swipeAPI } from "@/services/swipeAPI";
import { profileAPI } from "@/services/api";

const Likes = () => {

  const navigate = useNavigate();

  const [likes, setLikes] = useState([]);
  const [profiles, setProfiles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLikes();
  }, []);

  const calculateAge = (dob) => {

    if (!dob) return "-";

    const birthDate = new Date(dob);
    const today = new Date();

    let age =
      today.getFullYear() -
      birthDate.getFullYear();

    const monthDiff =
      today.getMonth() -
      birthDate.getMonth();

    if (
      monthDiff < 0 ||
      (
        monthDiff === 0 &&
        today.getDate() < birthDate.getDate()
      )
    ) {
      age--;
    }

    return age;
  };

  const loadLikes = async () => {

    try {

      const likesData =
        await swipeAPI.getReceivedLikes();

      setLikes(likesData);

      const profilePromises =
        likesData.map((like) =>
          profileAPI.getProfileByUserId(
            like.fromUserId
          )
        );

      const profilesData =
        await Promise.all(profilePromises);

      setProfiles(profilesData);

    } catch (err) {

      console.log(err);

    } finally {

      setLoading(false);

    }
  };

  return (

    <div className="min-h-screen bg-muted/30">



      <div className="container mx-auto px-4 py-8">

        <h1 className="text-3xl font-bold mb-6">
          ❤️ Likes Received
        </h1>

        {loading ? (

          <p>Loading...</p>

        ) : profiles.length === 0 ? (

          <p>No likes received yet.</p>

        ) : (

          <div className="grid grid-cols-1 gap-4">

            {profiles.map((profile) => (

              <div
                key={profile.id}
                onClick={() =>
                  navigate(`/profile/${profile.id}`)
                }
                className="
                bg-white
                rounded-xl
                shadow-lg
                p-4
                flex
                gap-4
                cursor-pointer
                hover:shadow-xl
                hover:scale-[1.02]
                transition-all
                duration-300
                "
              >

                <img
                 src={profile.imageUrl}
                  alt={`${profile.firstName} ${profile.lastName}`}
                  className="
                  w-20
                  h-20
                  rounded-full
                  object-cover
                  "
                />

                <div className="flex flex-col justify-center">

                  <h3 className="font-bold text-lg">
                    {profile.firstName}{" "}
                    {profile.lastName}
                  </h3>

                  <p className="text-gray-500">
                    {calculateAge(profile.dateOfBirth)} Years •{" "}
                    {profile.cityName || "City"}
                  </p>

                  <p className="text-pink-500 text-sm mt-1">
                    ❤️ Liked your profile
                  </p>

                </div>

              </div>

            ))}

          </div>

        )}

      </div>

    </div>

  );
};

export default Likes;