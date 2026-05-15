import { Loader2 } from "lucide-react";
import { useLoading } from "../hooks/useLoading";

const LoadingSpinner = () => {
  const { isLoading, loadingMessage } = useLoading();

  if (!isLoading) return null;

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-black/40 backdrop-blur-sm">
      <div className="flex flex-col items-center gap-3 rounded-2xl bg-white px-8 py-6 shadow-xl">
        <Loader2 className="h-10 w-10 animate-spin text-pink-600" />
        <p className="text-sm font-medium text-gray-700">
          {loadingMessage || "Loading..."}
        </p>
      </div>
    </div>
  );
};

export default LoadingSpinner;