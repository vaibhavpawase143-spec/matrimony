import { useNavigate } from "react-router-dom";

const PremiumUpgradeModal = ({
    open,
    onClose,
    feature = "Premium Feature"
}) => {

    const navigate = useNavigate();

    if (!open) return null;

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">

            <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

                <div className="text-6xl mb-4">
                    👑
                </div>

                <h2 className="text-2xl font-bold mb-3">
                    Premium Required
                </h2>

                <p className="text-gray-600 mb-6">
                    {feature} is available only for Premium members.
                    <br />
                    Upgrade your plan to continue.
                </p>

                <div className="flex justify-center gap-3">

                    <button
                        onClick={onClose}
                        className="px-5 py-2 rounded-xl bg-gray-200"
                    >
                        Close
                    </button>

                    <button
                        onClick={() => {
                            onClose();
                            navigate("/upgrade");
                        }}
                        className="px-5 py-2 rounded-xl bg-pink-600 text-white"
                    >
                        Upgrade Premium
                    </button>

                </div>

            </div>

        </div>
    );
};

export default PremiumUpgradeModal;