import { useState } from "react";

export default function Settings() {
  const [settings, setSettings] = useState({
    darkMode: false,
    notifications: true,
    maintenance: false,
    appName: "Gathbandhan",
    version: "1.0.0",
  });

  const handleChange = (e) => {
    const { name, type, checked, value } = e.target;

    setSettings({
      ...settings,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSave = () => {
    alert("Settings Saved Successfully");
  };

  return (
    <div className="p-6">

      <div className="mb-6">
        <h1 className="text-3xl font-bold">Settings</h1>
        <p className="text-gray-500">
          Manage application settings.
        </p>
      </div>

      <div className="bg-white rounded-2xl shadow-lg p-8 space-y-8">

        {/* Application */}

        <div>

          <h2 className="text-xl font-semibold mb-4">
            Application
          </h2>

          <div className="grid md:grid-cols-2 gap-6">

            <div>

              <label className="font-semibold">
                Application Name
              </label>

              <input
                type="text"
                name="appName"
                value={settings.appName}
                onChange={handleChange}
                className="w-full mt-2 border rounded-lg p-3"
              />

            </div>

            <div>

              <label className="font-semibold">
                Version
              </label>

              <input
                type="text"
                name="version"
                value={settings.version}
                onChange={handleChange}
                className="w-full mt-2 border rounded-lg p-3"
              />

            </div>

          </div>

        </div>

        {/* Switches */}

        <div>

          <h2 className="text-xl font-semibold mb-4">
            Preferences
          </h2>

          <div className="space-y-4">

            <label className="flex justify-between items-center">

              <span>Enable Dark Mode</span>

              <input
                type="checkbox"
                name="darkMode"
                checked={settings.darkMode}
                onChange={handleChange}
              />

            </label>

            <label className="flex justify-between items-center">

              <span>Email Notifications</span>

              <input
                type="checkbox"
                name="notifications"
                checked={settings.notifications}
                onChange={handleChange}
              />

            </label>

            <label className="flex justify-between items-center">

              <span>Maintenance Mode</span>

              <input
                type="checkbox"
                name="maintenance"
                checked={settings.maintenance}
                onChange={handleChange}
              />

            </label>

          </div>

        </div>

        {/* Button */}

        <button
          onClick={handleSave}
          className="bg-pink-600 hover:bg-pink-700 text-white px-8 py-3 rounded-lg"
        >
          Save Settings
        </button>

      </div>

    </div>
  );
}