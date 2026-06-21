import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { formatDistanceToNow } from "date-fns";
import { profileVisitorAPI } from "@/services/api";
import toast from "react-hot-toast";

const ProfileVisitors = () => {

  const [visitors, setVisitors] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {

    loadVisitors();

  }, []);

  const loadVisitors = async () => {

    try {

      const data =
        await profileVisitorAPI.getMyVisitors();

      setVisitors(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (err) {

      console.log(err);

      toast.error(
        "Failed to load visitors"
      );

    } finally {

      setLoading(false);

    }

  };

  return (

    <div className="min-h-screen bg-gray-50">



      <div className="container mx-auto p-6">

        <div className="flex items-center justify-between mb-6">

          <h1 className="text-3xl font-bold">
            👀 Profile Visitors
          </h1>

<div
  className="
  bg-gradient-to-r
  from-pink-500
  to-rose-500
  text-white
  px-6
  py-4
  rounded-2xl
  shadow-lg
  "
>
  <p className="text-sm">
    Profile Visitors
  </p>

  <h2 className="text-3xl font-bold">
    {visitors.length}
  </h2>
</div>

        </div>

        {loading && (

          <div className="text-center py-10">
            Loading visitors...
          </div>

        )}

{!loading &&
  visitors.length === 0 && (

  <div
    className="
    bg-white
    rounded-2xl
    shadow-sm
    p-12
    text-center
    "
  >
    <div className="text-6xl mb-4">
      👀
    </div>

    <h3 className="text-xl font-semibold mb-2">
      No Visitors Yet
    </h3>

    <p className="text-gray-500">
      Complete your profile and start exploring
      matches to get noticed.
    </p>
  </div>

)}

        <div className="space-y-4">

          {visitors.map((visitor) => (

            <div
              key={visitor.userId}
              className="
              bg-white
              rounded-2xl
              shadow-sm
              border
              p-5
             flex
             flex-col
             md:flex-row
             md:items-center
             justify-between
             gap-4
              hover:shadow-md
              hover:-translate-y-1
              transition-all
              duration-300
              "
            >

              <div className="flex items-center gap-4">

                <img
                  src={
                    visitor.imageUrl ||
                    "/default-profile.png"
                  }
                  alt="Visitor"
                className="
                w-16
                h-16
                rounded-full
                object-cover
                border-2
                border-pink-200
                "
                />

                <div>

                  <h3 className="font-semibold text-lg">
                    {visitor.fullName}
                  </h3>


                  <p className="text-sm text-gray-400 mt-1">
                    Viewed{" "}
                    {
                      visitor.viewedAt
                        ? formatDistanceToNow(
                            new Date(visitor.viewedAt),
                            { addSuffix: true }
                          )
                        : "-"
                    }
                  </p>

                </div>

              </div>

              <Link
                to={`/profile/${visitor.profileId}`}
               className="
               w-full
               md:w-auto
               text-center
               bg-pink-600
               text-white
               px-5
               py-2.5
               rounded-xl
               hover:bg-pink-700
               transition
               "
              >
                View Profile
              </Link>

            </div>

          ))}

        </div>

      </div>

    </div>

  );

};

export default ProfileVisitors;