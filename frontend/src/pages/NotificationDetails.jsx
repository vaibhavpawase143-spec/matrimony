import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { toast } from "sonner";

import { notificationAPI } from "@/services/api";

export default function NotificationDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [notification, setNotification] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadNotification();
  }, [id]);

  const loadNotification = async () => {
    try {
      setLoading(true);

      const data = await notificationAPI.getById(id);

      setNotification(data);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load notification");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="text-center py-20 text-gray-500">
          Loading notification...
        </div>
      </div>
    );
  }

  if (!notification) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 mb-6 text-pink-600 hover:text-pink-700"
        >
        <ArrowLeft className="h-5 w-5" />
          Back
        </button>

        <div className="bg-white rounded-xl shadow p-10 text-center">
          Notification not found.
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto p-6">

      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-2 mb-6 text-pink-600 hover:text-pink-700"
      >
       <ArrowLeft className="h-5 w-5" />
        Back
      </button>

      <div className="bg-white rounded-xl shadow-lg border">

        <div className="border-b px-6 py-5">
          <span className="inline-block px-3 py-1 rounded-full bg-pink-100 text-pink-700 text-sm font-semibold">
            {notification.type}
          </span>

          <h1 className="text-2xl font-bold mt-3">
            {notification.title}
          </h1>

          <p className="text-gray-500 mt-2">
            {new Date(notification.createdAt).toLocaleString()}
          </p>
        </div>

        <div className="p-6">

          <p className="text-gray-700 leading-8 whitespace-pre-wrap">
            {notification.message}
          </p>

        </div>

      </div>

    </div>
  );
}