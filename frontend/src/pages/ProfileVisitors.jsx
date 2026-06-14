import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "@/components/Navbar";
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

      <Navbar />

      <div className="container mx-auto p-6">

        <div className="flex items-center justify-between mb-6">

          <h1 className="text-3xl font-bold">
            👀 Profile Visitors
          </h1>

          <div
            className="
            bg-pink-100
            text-pink-700
            px-4
            py-2
            rounded-lg
            font-semibold
            "
          >
            Total Visitors : {visitors.length}
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
            rounded-xl
            shadow-sm
            p-8
            text-center
            "
          >
            No visitors yet 👀
          </div>

        )}

        <div className="space-y-4">

          {visitors.map((visitor) => (

            <div
              key={visitor.userId}
              className="
              bg-white
              rounded-xl
              shadow-sm
              border
              p-5
              flex
              items-center
              justify-between
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
                  w-14
                  h-14
                  rounded-full
                  object-cover
                  border
                  "
                />

                <div>

                  <h3 className="font-semibold text-lg">
                    {visitor.fullName}
                  </h3>

                  <p className="text-gray-500">
                    {visitor.email}
                  </p>

                  <p className="text-sm text-gray-400 mt-1">
                    Viewed At :{" "}
                    {
                      visitor.viewedAt
                        ? new Date(
                            visitor.viewedAt
                          ).toLocaleString()
                        : "-"
                    }
                  </p>

                </div>

              </div>

              <Link
                to={`/profile/${visitor.profileId}`}
                className="
                bg-pink-600
                text-white
                px-4
                py-2
                rounded-lg
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