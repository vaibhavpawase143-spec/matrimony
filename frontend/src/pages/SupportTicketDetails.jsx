import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";
import { supportAPI } from "@/services/api";

const SupportTicketDetails = () => {

    const { ticketNumber  } = useParams();

    const navigate = useNavigate();

    const [ticket, setTicket] = useState(null);

    const [loading, setLoading] = useState(true);
const [error, setError] = useState("");
   useEffect(() => {
       loadTicket();
   }, [ticketNumber]);

    const loadTicket = async () => {
      try {
        setLoading(true);
        setError("");

        const data = await supportAPI.getTicket(ticketNumber);

        console.log("TICKET DATA =", data);
        setTicket(data);
      } catch (error) {
        console.error("Failed to load ticket:", error);

        setTicket(null);
        setError(
          error?.message ||
          "Ticket not found or you do not have permission to view it."
        );
      } finally {
        setLoading(false);
      }
    };
    const closeTicket = async () => {

        try {

            await supportAPI.closeTicket(ticketNumber);

            toast.success(
                "Ticket closed successfully."
            );

            loadTicket();

        } catch (error) {

            console.log(error);

            toast.error(
                "Unable to close ticket"
            );

        }

    };

    const badgeColor = (status) => {

        switch (status) {

            case "OPEN":
                return "bg-green-100 text-green-700";

            case "IN_PROGRESS":
                return "bg-yellow-100 text-yellow-700";

            case "RESOLVED":
                return "bg-blue-100 text-blue-700";

            case "CLOSED":
                return "bg-red-100 text-red-700";

            default:
                return "bg-gray-100 text-gray-700";

        }

    };
console.log("Current Ticket =", ticket);
    if (loading) {

        return (

            <div className="min-h-screen flex items-center justify-center">

                Loading...

            </div>

        );

    }
if (error || !ticket) {
  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-6">
      <div className="bg-white border rounded-2xl shadow p-8 max-w-md w-full text-center">
        <h1 className="text-xl font-bold text-gray-900">
          Ticket unavailable
        </h1>

        <p className="text-gray-500 mt-3">
          {error || "This support ticket could not be found."}
        </p>

        <button
          onClick={() => navigate("/support/tickets")}
          className="mt-6 bg-pink-600 hover:bg-pink-700 text-white px-5 py-2 rounded-xl"
        >
          Back to My Tickets
        </button>
      </div>
    </div>
  );
}
return (

    <div className="min-h-screen bg-gray-50">

        <div className="max-w-5xl mx-auto p-6">

            {/* Header */}

            <div className="flex items-center justify-between mb-8">

                <div>

                    <button
                        onClick={() => navigate(-1)}
                        className="text-pink-600 hover:underline mb-3"
                    >
                        ← Back
                    </button>

                    <h1 className="text-3xl font-bold">
                        🎫 Ticket Details
                    </h1>

                    <p className="text-gray-500 mt-2">
                        {ticket.ticketNumber}
                    </p>

                </div>

                <span
                    className={`
                    px-5
                    py-2
                    rounded-full
                    font-semibold
                    ${badgeColor(ticket.status)}
                    `}
                >
                    {ticket.status}
                </span>

            </div>

            {/* Main Card */}

            <div className="bg-white rounded-2xl shadow border p-8">

                {/* Information */}

                <div className="grid md:grid-cols-3 gap-6">

                    <div>

                        <p className="text-gray-400 text-sm">
                            Category
                        </p>

                        <p className="font-bold text-lg">
                            {ticket.category}
                        </p>

                    </div>

                    <div>

                        <p className="text-gray-400 text-sm">
                            Priority
                        </p>

                        <p className="font-bold text-lg">
                            {ticket.priority}
                        </p>

                    </div>

                    <div>

                        <p className="text-gray-400 text-sm">
                            Created
                        </p>

                        <p className="font-bold text-lg">

                            {
                                new Date(ticket.createdAt)
                                    .toLocaleDateString(
                                        "en-IN",
                                        {
                                            day: "2-digit",
                                            month: "short",
                                            year: "numeric"
                                        }
                                    )
                            }

                        </p>

                    </div>

                </div>

                {/* Subject */}

                <div className="mt-10">

                    <h2 className="text-lg font-semibold mb-2">
                        Subject
                    </h2>

                    <div className="bg-gray-50 rounded-xl p-4">

                        {ticket.subject}

                    </div>

                </div>

                {/* Message */}

                <div className="mt-8">

                    <h2 className="text-lg font-semibold mb-2">
                        Message
                    </h2>

                    <div className="bg-gray-50 rounded-xl p-4 whitespace-pre-wrap">

                        {ticket.message}

                    </div>

                </div>

                {/* Admin Reply */}

                <div className="mt-8">

                    <h2 className="text-lg font-semibold mb-2">
                        Admin Reply
                    </h2>

                    <div className="bg-blue-50 border border-blue-200 rounded-xl p-4">

                        {

                            ticket.adminReply

                                ? ticket.adminReply

                                : "Waiting for admin response."

                        }

                    </div>

                </div>

                {/* Close Button */}

                {

   !["CLOSED", "RESOLVED"].includes(ticket.status) && (

                        <div className="mt-10 flex justify-end">

                            <button

                                onClick={closeTicket}

                                className="
                                bg-red-600
                                hover:bg-red-700
                                text-white
                                px-6
                                py-3
                                rounded-xl
                                transition
                                "

                            >

                                Close Ticket

                            </button>

                        </div>

                    )

                }

            </div>

        </div>

    </div>

);
};

export default SupportTicketDetails;