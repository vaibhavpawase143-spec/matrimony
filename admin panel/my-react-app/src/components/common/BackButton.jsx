import { useNavigate } from "react-router-dom";
import { FaArrowLeft } from "react-icons/fa";

export default function BackButton({ label = "Back", to }) {
  const navigate = useNavigate();

  const handleClick = () => {
    if (to) {
      navigate(to);
    } else {
      navigate(-1);
    }
  };

  return (
    <button
      type="button"
      onClick={handleClick}
      className="inline-flex items-center gap-2 px-3.5 py-2 text-sm font-medium text-slate-700 bg-white hover:bg-slate-100/80 border border-slate-200 rounded-xl shadow-xs transition-all duration-200 hover:shadow-sm hover:border-slate-300 hover:-translate-x-0.5 active:translate-x-0 cursor-pointer mb-6"
    >
      <FaArrowLeft className="text-slate-500 group-hover:text-indigo-600 transition-colors duration-200 text-xs" />
      <span>{label}</span>
    </button>
  );
}
