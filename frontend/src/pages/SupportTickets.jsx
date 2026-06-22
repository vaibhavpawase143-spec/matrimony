import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { supportAPI } from "@/services/api";
import toast from "react-hot-toast";

const SupportTickets = () => {

    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadTickets();

    }, []);

    const loadTickets = async () => {

        try {

            const data =
                await supportAPI.getMyTickets();

            setTickets(
                Array.isArray(data)
                    ? data
                    : []
            );

        } catch (error) {

            console.log(error);

            toast.error(
                "Failed to load support tickets"
            );

        } finally {

            setLoading(false);

        }

    };

    const getStatusColor = (status) => {

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

    return (

        <div className="min-h-screen bg-gray-50">

            <div className="max-w-6xl mx-auto p-6">

                <div className="flex justify-between items-center mb-8">

                    <div>

                        <h1 className="text-3xl font-bold">
                            🎫 My Support Tickets
                        </h1>

                        <p className="text-gray-500 mt-2">
                            View and manage your support requests.
                        </p>

                    </div>

                    <Link
                        to="/help"
                        className="
                        bg-pink-600
                        hover:bg-pink-700
                        text-white
                        px-5
                        py-3
                        rounded-xl
                        transition
                        "
                    >
                        + New Ticket
                    </Link>

                </div>

                {loading && (

                    <div className="text-center py-20">

                        Loading...

                    </div>

                )}

                {!loading && tickets.length === 0 && (

                    <div
                        className="
                        bg-white
                        rounded-2xl
                        shadow
                        p-16
                        text-center
                        "
                    >

                        <div className="text-6xl mb-4">
                            🎫
                        </div>

                        <h2 className="text-2xl font-semibold">
                            No Support Tickets
                        </h2>

                        <p className="text-gray-500 mt-3">
                            You haven't created any support tickets yet.
                        </p>

                        <Link
                            to="/help"
                            className="
                            inline-block
                            mt-6
                            bg-pink-600
                            hover:bg-pink-700
                            text-white
                            px-6
                            py-3
                            rounded-xl
                            "
                        >
                            Create Ticket
                        </Link>

                    </div>

                )}

                <div className="space-y-5">

                    {tickets.map(ticket => (

                        <div
                            key={ticket.id}
                            className="
                            bg-white
                            rounded-2xl
                            shadow
                            border
                            p-6
                            hover:shadow-lg
                            transition
                            "
                        >

                            <div className="flex justify-between items-start">

                                <div>

                                    <h2 className="text-xl font-bold">

                                        {ticket.ticketNumber}

                                    </h2>

                                    <p className="text-gray-500 mt-1">

                                        {ticket.subject}

                                    </p>

                                </div>

                                <span
                                    className={`
                                    px-4
                                    py-2
                                    rounded-full
                                    text-sm
                                    font-semibold
                                    ${getStatusColor(ticket.status)}
                                    `}
                                >

                                    {ticket.status}

                                </span>

                            </div>

                            <div className="grid md:grid-cols-3 gap-6 mt-6">

                                <div>

                                    <p className="text-gray-400 text-sm">
                                        Category
                                    </p>

                                    <p className="font-semibold">
                                        {ticket.category}
                                    </p>

                                </div>

                                <div>

                                    <p className="text-gray-400 text-sm">
                                        Priority
                                    </p>

                                    <p className="font-semibold">
                                        {ticket.priority}
                                    </p>

                                </div>

                                <div>

                                    <p className="text-gray-400 text-sm">
                                        Created
                                    </p>

                                    <p className="font-semibold">

                                        {
                                         new Date(ticket.createdAt).toLocaleDateString(
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

                            <div className="mt-6 flex justify-end">

                                <Link
                                    to={`/support/tickets/${ticket.ticketNumber}`}
                                    className="
                                    bg-pink-600
                                    hover:bg-pink-700
                                    text-white
                                    px-5
                                    py-2
                                    rounded-lg
                                    "
                                >
                                    View Details
                                </Link>

                            </div>

                        </div>

                    ))}

                </div>

            </div>

        </div>

    );

};

export default SupportTickets;