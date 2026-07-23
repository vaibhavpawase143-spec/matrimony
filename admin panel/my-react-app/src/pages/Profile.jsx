import { useState } from "react";
import { FaUserCircle, FaCamera } from "react-icons/fa";

export default function Profile() {
  const [profile, setProfile] = useState({
    name: "Super Admin",
    email: "admin@gathbandhan.com",
    phone: "9876543210",
  });

  const handleChange = (e) => {
    setProfile({
      ...profile,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert("Profile Updated Successfully");
  };

  return (
    <div className="p-6">

      <div className="mb-6">
        <h1 className="text-3xl font-bold">My Profile</h1>
        <p className="text-gray-500">
          Update your administrator profile.
        </p>
      </div>

      <div className="bg-white rounded-2xl shadow-lg p-8 max-w-4xl mx-auto">

        <div className="flex flex-col items-center mb-8">

          <div className="relative">

            <FaUserCircle className="text-9xl text-pink-500" />

            <button className="absolute bottom-0 right-0 bg-pink-600 text-white p-2 rounded-full">
              <FaCamera />
            </button>

          </div>

          <h2 className="text-2xl font-semibold mt-4">
            Super Admin
          </h2>

          <p className="text-gray-500">
            admin@gathbandhan.com
          </p>

        </div>

        <form
          onSubmit={handleSubmit}
          className="grid md:grid-cols-2 gap-6"
        >

          <div>
            <label className="font-semibold">
              Full Name
            </label>

            <input
              type="text"
              name="name"
              value={profile.name}
              onChange={handleChange}
              className="w-full mt-2 border rounded-lg p-3"
            />
          </div>

          <div>
            <label className="font-semibold">
              Email
            </label>

            <input
              type="email"
              name="email"
              value={profile.email}
              onChange={handleChange}
              className="w-full mt-2 border rounded-lg p-3"
            />
          </div>

          <div>
            <label className="font-semibold">
              Phone
            </label>

            <input
              type="text"
              name="phone"
              value={profile.phone}
              onChange={handleChange}
              className="w-full mt-2 border rounded-lg p-3"
            />
          </div>

          <div className="flex items-end">

            <button
              type="submit"
              className="bg-pink-600 hover:bg-pink-700 text-white px-6 py-3 rounded-lg w-full"
            >
              Update Profile
            </button>

          </div>

        </form>

      </div>

    </div>
  );
}