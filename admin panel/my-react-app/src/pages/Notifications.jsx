import { useEffect, useState } from "react";
import { FaPaperPlane, FaSearch, FaBullhorn, FaEye, FaTimes, FaCheckCircle, FaExclamationTriangle, FaTimesCircle, FaSpinner, FaPlay, FaBan } from "react-icons/fa";
import { toast } from "sonner";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

import {
  sendNotification,
  broadcastNotification,
  getNotificationHistory,
  getActiveBroadcastJob,
  getBroadcastJobById,
  getBroadcastHistory,
  getBroadcastRecipients,
  resumeBroadcastJob,
  cancelBroadcastJob,
} from "../services/notificationService";

export default function Notifications() {
  // =========================================================
  // FORM STATES
  // =========================================================
  const [title, setTitle] = useState("");
  const [message, setMessage] = useState("");
  const [type, setType] = useState("SYSTEM");
  const [broadcast, setBroadcast] = useState(true);
  const [receiverIds, setReceiverIds] = useState("");
  const [sending, setSending] = useState(false);
  const [resuming, setResuming] = useState(false);
  const [cancelling, setCancelling] = useState(false);

  // =========================================================
  // BROADCAST LIFECYCLE & HISTORY STATES
  // =========================================================
  const [trackedJobId, setTrackedJobId] = useState(() => {
    const savedId = sessionStorage.getItem("admin_tracked_broadcast_job_id");
    return savedId ? Number(savedId) : null;
  });
  const [activeBroadcast, setActiveBroadcast] = useState(null);
  const [broadcastHistory, setBroadcastHistory] = useState([]);
  const [broadcastLoading, setBroadcastLoading] = useState(false);

  // =========================================================
  // RECIPIENT DELIVERY DETAILS MODAL STATES
  // =========================================================
  const [selectedBroadcast, setSelectedBroadcast] = useState(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [recipients, setRecipients] = useState([]);
  const [recipientsLoading, setRecipientsLoading] = useState(false);
  const [recipientSearch, setRecipientSearch] = useState("");
  const [appStatusFilter, setAppStatusFilter] = useState("");
  const [emailStatusFilter, setEmailStatusFilter] = useState("");
  const [recipientPage, setRecipientPage] = useState(0);
  const [recipientTotalPages, setRecipientTotalPages] = useState(0);
  const [recipientTotalElements, setRecipientTotalElements] = useState(0);

  // =========================================================
  // NORMAL NOTIFICATION HISTORY
  // =========================================================
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // =========================================================
  // LOAD DATA & WEBSOCKET SETUP
  // =========================================================
  const loadActiveBroadcast = async (specificJobId = trackedJobId) => {
    try {
      let res = null;
      if (specificJobId) {
        res = await getBroadcastJobById(specificJobId);
      }
      if (!res?.data) {
        res = await getActiveBroadcastJob();
      }
      if (res?.data) {
        setActiveBroadcast(res.data);
        if (res.data.id) {
          setTrackedJobId(res.data.id);
          sessionStorage.setItem("admin_tracked_broadcast_job_id", res.data.id.toString());
        }
      }
    } catch (err) {
      console.error("Failed to fetch active broadcast job", err);
    }
  };

  const loadBroadcastHistoryList = async () => {
    try {
      setBroadcastLoading(true);
      const res = await getBroadcastHistory(0, 10);
      setBroadcastHistory(res?.data?.content || []);
    } catch (err) {
      console.error("Failed to fetch broadcast history", err);
    } finally {
      setBroadcastLoading(false);
    }
  };

  const loadHistory = async (currentPage = page, currentKeyword = search) => {
    try {
      setLoading(true);
      const response = await getNotificationHistory(currentKeyword, currentPage, size);
      const pageData = response.data;
      setNotifications(pageData.content || []);
      setTotalPages(pageData.totalPages || 0);
      setTotalElements(pageData.totalElements || 0);
    } catch (error) {
      console.error(error);
      toast.error(error?.response?.data?.message || "Failed to load notification history.");
    } finally {
      setLoading(false);
    }
  };

  // STOMP WebSocket for Real-time Broadcast Lifecycle Updates
  useEffect(() => {
    loadActiveBroadcast();
    loadBroadcastHistoryList();

    let stompClient = null;
    try {
      const wsUrl = window.location.port === "5173" || window.location.port === "3000"
        ? "http://localhost:9090/ws-raw"
        : "/ws-raw";
      const socket = new SockJS(wsUrl);
      stompClient = Stomp.over(socket);
      stompClient.debug = () => { };

      stompClient.connect({}, () => {
        stompClient.subscribe("/topic/admin/broadcast-progress", (eventMessage) => {
          if (eventMessage.body) {
            const data = JSON.parse(eventMessage.body);
            const currentTrackedId = sessionStorage.getItem("admin_tracked_broadcast_job_id");
            
            if (!currentTrackedId || Number(currentTrackedId) === data.id || Number(currentTrackedId) === data.broadcastJobId) {
              setActiveBroadcast((prev) => {
                if (!prev) return data;
                if (prev.id === data.id || prev.id === data.broadcastJobId) {
                  const isPrevTerminal = prev.status === "COMPLETED" || prev.status === "COMPLETED_WITH_FAILURES" || prev.status === "FAILED" || prev.status === "CANCELLED";
                  if (isPrevTerminal && data.status === "IN_PROGRESS") {
                    return prev;
                  }
                  const prevProcessed = prev.processedRecipients || 0;
                  const newProcessed = data.processedRecipients || 0;
                  if (newProcessed < prevProcessed && data.status === "IN_PROGRESS") {
                    return {
                      ...data,
                      processedRecipients: prevProcessed,
                      progressPercentage: prev.progressPercentage || 0,
                    };
                  }
                }
                return data;
              });

              if (data.status === "COMPLETED" || data.status === "COMPLETED_WITH_FAILURES" || data.status === "FAILED" || data.status === "CANCELLED" || data.status === "INTERRUPTED") {
                loadBroadcastHistoryList();
              }
            }
          }
        });
      });
    } catch (e) {
      console.warn("STOMP connection failed:", e);
    }

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, []);

  // Polling fallback while broadcast exists (2-second interval)
  useEffect(() => {
    const isCurrentlyActive = activeBroadcast && (activeBroadcast.status === "IN_PROGRESS" || activeBroadcast.status === "PENDING");
    if (!isCurrentlyActive || !activeBroadcast?.id) return;

    const targetJobId = activeBroadcast.id;
    const intervalId = setInterval(async () => {
      try {
        const res = await getBroadcastJobById(targetJobId);
        if (res?.data) {
          setActiveBroadcast((prev) => {
            if (!prev) return res.data;
            if (prev.id === res.data.id) {
              const isPrevTerminal = prev.status === "COMPLETED" || prev.status === "COMPLETED_WITH_FAILURES" || prev.status === "FAILED" || prev.status === "CANCELLED";
              if (isPrevTerminal && res.data.status === "IN_PROGRESS") {
                return prev;
              }
              const prevProcessed = prev.processedRecipients || 0;
              const newProcessed = res.data.processedRecipients || 0;
              if (newProcessed < prevProcessed && res.data.status === "IN_PROGRESS") {
                return {
                  ...res.data,
                  processedRecipients: prevProcessed,
                  progressPercentage: prev.progressPercentage || 0,
                };
              }
            }
            return res.data;
          });

          if (res.data.status === "COMPLETED" || res.data.status === "COMPLETED_WITH_FAILURES" || res.data.status === "FAILED" || res.data.status === "CANCELLED" || res.data.status === "INTERRUPTED") {
            loadBroadcastHistoryList();
          }
        }
      } catch (err) {
        console.error("Error polling active broadcast job", err);
      }
    }, 2000);

    return () => clearInterval(intervalId);
  }, [activeBroadcast?.status, activeBroadcast?.id]);

  // =========================================================
  // RESUME / CANCEL HANDLERS FOR INTERRUPTED BROADCASTS
  // =========================================================
  const handleResumeBroadcast = async () => {
    if (!activeBroadcast?.id) return;
    setResuming(true);
    try {
      const res = await resumeBroadcastJob(activeBroadcast.id);
      toast.success("Broadcast job resumed successfully.");
      if (res?.data) {
        setActiveBroadcast(res.data);
      } else {
        loadActiveBroadcast(activeBroadcast.id);
      }
      loadBroadcastHistoryList();
    } catch (err) {
      console.error(err);
      toast.error(err?.response?.data?.message || "Failed to resume broadcast job.");
    } finally {
      setResuming(false);
    }
  };

  const handleCancelBroadcast = async () => {
    if (!activeBroadcast?.id) return;
    setCancelling(true);
    try {
      const res = await cancelBroadcastJob(activeBroadcast.id);
      toast.success("Broadcast job cancelled successfully.");
      if (res?.data) {
        setActiveBroadcast(res.data);
      } else {
        loadActiveBroadcast(activeBroadcast.id);
      }
      loadBroadcastHistoryList();
    } catch (err) {
      console.error(err);
      toast.error(err?.response?.data?.message || "Failed to cancel broadcast job.");
    } finally {
      setCancelling(false);
    }
  };

  // =========================================================
  // FETCH RECIPIENT DELIVERY STATUS DETAILS
  // =========================================================
  const fetchRecipientDetails = async (jobId, currentPage = recipientPage, searchKey = recipientSearch, appF = appStatusFilter, emailF = emailStatusFilter) => {
    if (!jobId) return;
    try {
      setRecipientsLoading(true);
      const res = await getBroadcastRecipients(jobId, searchKey, appF, emailF, currentPage, 50);
      const pageData = res?.data;
      setRecipients(pageData?.content || []);
      setRecipientTotalPages(pageData?.totalPages || 0);
      setRecipientTotalElements(pageData?.totalElements || 0);
    } catch (err) {
      console.error("Failed to load recipient details", err);
      toast.error("Failed to load recipient delivery details.");
    } finally {
      setRecipientsLoading(false);
    }
  };

  const handleOpenDetails = (job) => {
    setSelectedBroadcast(job);
    setRecipientSearch("");
    setAppStatusFilter("");
    setEmailStatusFilter("");
    setRecipientPage(0);
    setShowDetailsModal(true);
    fetchRecipientDetails(job.id, 0, "", "", "");
  };

  useEffect(() => {
    if (showDetailsModal && selectedBroadcast) {
      const timer = setTimeout(() => {
        fetchRecipientDetails(selectedBroadcast.id, recipientPage, recipientSearch, appStatusFilter, emailStatusFilter);
      }, 300);
      return () => clearTimeout(timer);
    }
  }, [recipientPage, recipientSearch, appStatusFilter, emailStatusFilter, showDetailsModal]);

  // =========================================================
  // SEND / BROADCAST HANDLER
  // =========================================================
  const handleSend = async () => {
    if (!title.trim()) {
      toast.error("Title is required.");
      return;
    }
    if (!message.trim()) {
      toast.error("Message is required.");
      return;
    }

    setSending(true);

    try {
      if (broadcast) {
        const response = await broadcastNotification({ title, message, type });
        toast.success("Broadcast notification accepted for processing...");
        setTitle("");
        setMessage("");
        const jobData = response?.data;
        if (jobData && jobData.id) {
          setTrackedJobId(jobData.id);
          sessionStorage.setItem("admin_tracked_broadcast_job_id", jobData.id.toString());
          setActiveBroadcast(jobData);
        } else {
          loadActiveBroadcast();
        }
        loadBroadcastHistoryList();
      } else {
        const ids = receiverIds
          .split(",")
          .map((id) => Number(id.trim()))
          .filter((id) => !Number.isNaN(id));

        if (ids.length === 0) {
          toast.error("Enter at least one receiver ID.");
          setSending(false);
          return;
        }

        await sendNotification({ receiverIds: ids, title, message, type });
        toast.success("Notification sent successfully.");
        setTitle("");
        setMessage("");
        setReceiverIds("");
        loadHistory();
      }
    } catch (error) {
      console.error(error);
      toast.error(error?.response?.data?.message || "Failed to send notification.");
    } finally {
      setSending(false);
    }
  };

  useEffect(() => {
    loadHistory();
  }, [page]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setPage(0);
      loadHistory(0, search);
    }, 500);
    return () => clearTimeout(timer);
  }, [search]);

  const isBroadcastInProgress = activeBroadcast && (activeBroadcast.status === "IN_PROGRESS" || activeBroadcast.status === "PENDING" || activeBroadcast.status === "INTERRUPTED");

  // =========================================================
  // RENDER HELPERS
  // =========================================================
  const renderStatusBadge = (status) => {
    switch (status) {
      case "IN_PROGRESS":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs font-semibold">
            <FaSpinner className="animate-spin" /> IN PROGRESS
          </span>
        );
      case "INTERRUPTED":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-amber-100 text-amber-800 rounded-full text-xs font-semibold">
            <FaExclamationTriangle className="text-amber-600" /> INTERRUPTED
          </span>
        );
      case "CANCELLED":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-gray-200 text-gray-800 rounded-full text-xs font-semibold">
            <FaTimesCircle className="text-gray-500" /> CANCELLED
          </span>
        );
      case "COMPLETED":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-green-100 text-green-800 rounded-full text-xs font-semibold">
            <FaCheckCircle /> SUCCESS
          </span>
        );
      case "COMPLETED_WITH_FAILURES":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-orange-100 text-orange-800 rounded-full text-xs font-semibold">
            <FaExclamationTriangle /> COMPLETED WITH FAILURES
          </span>
        );
      case "FAILED":
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-red-100 text-red-800 rounded-full text-xs font-semibold">
            <FaTimesCircle /> FAILED
          </span>
        );
      default:
        return <span className="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-xs font-semibold">{status}</span>;
    }
  };

  return (
    <div className="p-6 space-y-6">
      {/* ================= HEADER ================= */}
      <div>
        <h1 className="text-3xl font-bold text-gray-800">Notifications</h1>
        <p className="text-gray-500">Send notifications to selected users or broadcast to all users in real-time.</p>
      </div>

      {/* ================= INTERRUPTED BROADCAST BANNER ================= */}
      {activeBroadcast && activeBroadcast.status === "INTERRUPTED" && (
        <div className="bg-gradient-to-r from-amber-950 to-amber-900 text-white rounded-2xl shadow-xl p-6 border border-amber-600/50 space-y-4">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xs font-bold uppercase tracking-wider text-amber-400">Broadcast Interrupted</span>
                {activeBroadcast.isTestMode && (
                  <span className="bg-amber-500/20 text-amber-300 border border-amber-500/40 text-[10px] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">
                    TEST MODE
                  </span>
                )}
              </div>
              <h3 className="text-xl font-bold mt-0.5">{activeBroadcast.title}</h3>
              <p className="text-sm text-amber-200 mt-1 flex items-center gap-2">
                <FaExclamationTriangle className="text-amber-400 text-base" />
                Broadcast stopped because the server was restarted.
              </p>
            </div>
            <div className="flex items-center gap-3">
              {renderStatusBadge(activeBroadcast.status)}
              <button
                onClick={handleResumeBroadcast}
                disabled={resuming || cancelling}
                className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold rounded-xl text-sm transition-all shadow-md flex items-center gap-2 disabled:opacity-50"
              >
                {resuming ? <FaSpinner className="animate-spin" /> : <FaPlay className="text-xs" />}
                Resume
              </button>
              <button
                onClick={handleCancelBroadcast}
                disabled={resuming || cancelling}
                className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white font-semibold rounded-xl text-sm transition-all shadow-md flex items-center gap-2 disabled:opacity-50"
              >
                {cancelling ? <FaSpinner className="animate-spin" /> : <FaBan className="text-xs" />}
                Cancel
              </button>
            </div>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-5 gap-3 pt-2 border-t border-amber-800/60 text-center">
            <div className="bg-amber-900/40 p-3 rounded-xl border border-amber-700/50">
              <span className="text-xs text-amber-300 block">Total Recipients</span>
              <span className="text-lg font-bold text-white">{(activeBroadcast.totalRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-amber-900/40 p-3 rounded-xl border border-amber-700/50">
              <span className="text-xs text-amber-300 block">Enqueued</span>
              <span className="text-lg font-bold text-amber-200">{(activeBroadcast.enqueuedRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-amber-900/40 p-3 rounded-xl border border-amber-700/50">
              <span className="text-xs text-amber-300 block">Processed</span>
              <span className="text-lg font-bold text-white">{(activeBroadcast.processedRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-emerald-900/40 p-3 rounded-xl border border-emerald-700/50">
              <span className="text-xs text-emerald-300 block">Successful</span>
              <span className="text-lg font-bold text-emerald-400">{(activeBroadcast.successfulRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-rose-900/40 p-3 rounded-xl border border-rose-700/50">
              <span className="text-xs text-rose-300 block">Failed</span>
              <span className="text-lg font-bold text-rose-400">{(activeBroadcast.failedRecipients || 0).toLocaleString()}</span>
            </div>
          </div>
        </div>
      )}

      {/* ================= ACTIVE BROADCAST LIFECYCLE TRACKER (IN_PROGRESS / FINISHED) ================= */}
      {activeBroadcast && activeBroadcast.status !== "INTERRUPTED" && (
        <div className="bg-gradient-to-r from-slate-900 to-indigo-950 text-white rounded-2xl shadow-xl p-6 border border-indigo-800 space-y-4">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xs font-bold uppercase tracking-wider text-indigo-400">Notification Broadcast</span>
                {activeBroadcast.isTestMode && (
                  <span className="bg-amber-500/20 text-amber-300 border border-amber-500/40 text-[10px] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">
                    TEST MODE ({activeBroadcast.testRecipientLimit || 100} RECIPIENTS LIMIT)
                  </span>
                )}
              </div>
              <h3 className="text-xl font-bold mt-0.5">{activeBroadcast.title}</h3>
              <p className="text-sm text-gray-300">{activeBroadcast.message}</p>
            </div>
            <div>{renderStatusBadge(activeBroadcast.status)}</div>
          </div>

          {/* Progress Bar */}
          <div className="space-y-2">
            <div className="flex justify-between text-xs font-semibold text-gray-300">
              <span>{activeBroadcast.status === "IN_PROGRESS" ? "Enqueueing & delivering notifications across queue workers..." : "Broadcast Execution Summary"}</span>
              <span>{activeBroadcast.progressPercentage}%</span>
            </div>
            <div className="w-full bg-gray-800 rounded-full h-3 overflow-hidden border border-indigo-900">
              <div
                className="bg-gradient-to-r from-violet-500 to-indigo-400 h-3 rounded-full transition-all duration-500"
                style={{ width: `${Math.min(100, activeBroadcast.progressPercentage || 0)}%` }}
              />
            </div>
          </div>

          {/* Metrics Grid */}
          <div className="grid grid-cols-2 md:grid-cols-6 gap-3 pt-2 border-t border-indigo-900 text-center">
            <div className="bg-indigo-900/40 p-3 rounded-xl border border-indigo-800/50">
              <span className="text-xs text-indigo-300 block">Total Recipients</span>
              <span className="text-lg font-bold text-white">{(activeBroadcast.totalRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-blue-900/40 p-3 rounded-xl border border-blue-800/50">
              <span className="text-xs text-blue-300 block">Enqueued</span>
              <span className="text-lg font-bold text-blue-300">{(activeBroadcast.enqueuedRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-indigo-900/40 p-3 rounded-xl border border-indigo-800/50">
              <span className="text-xs text-indigo-300 block">Processed</span>
              <span className="text-lg font-bold text-white">{(activeBroadcast.processedRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-emerald-900/40 p-3 rounded-xl border border-emerald-800/50">
              <span className="text-xs text-emerald-300 block">Successful</span>
              <span className="text-lg font-bold text-emerald-400">{(activeBroadcast.successfulRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-rose-900/40 p-3 rounded-xl border border-rose-800/50">
              <span className="text-xs text-rose-300 block">Failed</span>
              <span className="text-lg font-bold text-rose-400">{(activeBroadcast.failedRecipients || 0).toLocaleString()}</span>
            </div>
            <div className="bg-purple-900/40 p-3 rounded-xl border border-purple-800/50">
              <span className="text-xs text-purple-300 block">Throughput / ETA</span>
              <span className="text-sm font-bold text-purple-300 block mt-0.5">
                {(activeBroadcast.currentThroughput || 0).toLocaleString()} /sec
              </span>
              <span className="text-[10px] text-gray-300 block">
                {activeBroadcast.status === "IN_PROGRESS"
                  ? `ETA: ${activeBroadcast.estimatedRemainingSeconds ? `${Math.floor(activeBroadcast.estimatedRemainingSeconds / 60)}m ${activeBroadcast.estimatedRemainingSeconds % 60}s` : 'Calculating...'}`
                  : 'Finished'}
              </span>
            </div>
          </div>
        </div>
      )}




      {/* ================= SEND NOTIFICATION FORM ================= */}
      <div className="bg-white rounded-2xl shadow-md border border-purple-100 p-6">
        <h2 className="text-xl font-semibold mb-6">Send Notification</h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {/* Title */}
          <div>
            <label className="block text-sm font-medium mb-2">Title</label>
            <input
              type="text"
              className="w-full border rounded-lg px-4 py-3 focus:ring-2 focus:ring-violet-500 outline-none"
              placeholder="Notification title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          {/* Type */}
          <div>
            <label className="block text-sm font-medium mb-2">Notification Type</label>
            <select
              value={type}
              onChange={(e) => setType(e.target.value)}
              className="w-full border rounded-lg px-4 py-3 focus:ring-2 focus:ring-violet-500 outline-none"
            >
              <option value="SYSTEM">System</option>
              <option value="ANNOUNCEMENT">Announcement</option>
              <option value="MAINTENANCE">Maintenance</option>
              <option value="WARNING">Warning</option>
              <option value="SUBSCRIPTION">Subscription</option>
            </select>
          </div>
        </div>

        {/* Message */}
        <div className="mt-5">
          <label className="block text-sm font-medium mb-2">Message</label>
          <textarea
            rows={4}
            className="w-full border rounded-lg px-4 py-3 resize-none focus:ring-2 focus:ring-violet-500 outline-none"
            placeholder="Write your notification..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
        </div>

        {/* Broadcast Checkbox */}
        <div className="mt-6 flex items-center gap-3">
          <input
            type="checkbox"
            id="broadcastCheck"
            checked={broadcast}
            onChange={(e) => setBroadcast(e.target.checked)}
            className="w-4 h-4 text-violet-600 rounded"
          />
          <label htmlFor="broadcastCheck" className="text-sm font-medium cursor-pointer">
            Broadcast to all active users
          </label>
        </div>

        {/* Selected Users Receiver IDs */}
        {!broadcast && (
          <div className="mt-5">
            <label className="block text-sm font-medium mb-2">Receiver IDs</label>
            <input
              type="text"
              placeholder="Example: 1,5,10"
              value={receiverIds}
              onChange={(e) => setReceiverIds(e.target.value)}
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>
        )}

        {/* Action Button */}
        <div className="mt-6">
          <button
            onClick={handleSend}
            disabled={sending || (broadcast && isBroadcastInProgress)}
            className="bg-violet-600 hover:bg-violet-700 disabled:bg-gray-400 text-white px-6 py-3 rounded-lg flex items-center gap-2 transition font-medium cursor-pointer disabled:cursor-not-allowed"
          >
            {sending ? <FaSpinner className="animate-spin" /> : broadcast ? <FaBullhorn /> : <FaPaperPlane />}
            {isBroadcastInProgress
              ? "Broadcast in progress..."
              : broadcast
                ? "Broadcast Notification"
                : "Send Notification"}
          </button>
        </div>
      </div>

      {/* ================= BROADCAST HISTORY & RECIPIENT TRACKING ================= */}
      <div className="bg-white rounded-2xl shadow-md border border-purple-100 overflow-hidden">
        <div className="p-5 border-b border-purple-100">
          <h2 className="text-xl font-semibold text-gray-800">Broadcast History</h2>
          <p className="text-xs text-gray-500">Track lifecycle execution and view detailed recipient-level delivery status.</p>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white text-sm">
              <tr>
                <th className="px-6 py-4">Broadcast</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Total</th>
                <th className="px-6 py-4">Success</th>
                <th className="px-6 py-4">Failed</th>
                <th className="px-6 py-4">Date</th>
                <th className="px-6 py-4 text-center">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 text-sm">
              {broadcastLoading ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-gray-500">Loading broadcast history...</td>
                </tr>
              ) : broadcastHistory.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-gray-500">No broadcast history records found.</td>
                </tr>
              ) : (
                broadcastHistory.map((job) => (
                  <tr key={job.id} className="hover:bg-purple-50 transition">
                    <td className="px-6 py-4">
                      <div className="font-semibold text-gray-800">{job.title}</div>
                      <div className="text-xs text-gray-500 line-clamp-1">{job.message}</div>
                    </td>
                    <td className="px-6 py-4">{renderStatusBadge(job.status)}</td>
                    <td className="px-6 py-4 font-semibold">{(job.totalRecipients || 0).toLocaleString()}</td>
                    <td className="px-6 py-4 text-emerald-600 font-semibold">{(job.successfulRecipients || 0).toLocaleString()}</td>
                    <td className="px-6 py-4 text-rose-600 font-semibold">{(job.failedRecipients || 0).toLocaleString()}</td>
                    <td className="px-6 py-4 text-xs text-gray-500">
                      {job.createdAt ? new Date(job.createdAt).toLocaleString() : "-"}
                    </td>
                    <td className="px-6 py-4 text-center">
                      <button
                        onClick={() => handleOpenDetails(job)}
                        className="inline-flex items-center gap-1.5 bg-violet-100 hover:bg-violet-200 text-violet-700 px-3 py-1.5 rounded-lg text-xs font-semibold transition"
                      >
                        <FaEye /> View Details
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* ================= NORMAL NOTIFICATION HISTORY ================= */}
      <div className="bg-white rounded-2xl shadow-md border border-purple-100">
        <div className="p-5 flex flex-col md:flex-row justify-between items-center gap-4 border-b border-purple-100">
          <div>
            <h2 className="text-xl font-semibold">Notification History</h2>
            <p className="text-xs text-gray-500">Individual notification dispatch log.</p>
          </div>

          <div className="flex items-center gap-2 border rounded-lg px-3 py-2 w-full md:w-80 bg-gray-50">
            <FaSearch className="text-gray-400" />
            <input
              type="text"
              placeholder="Search notifications..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-transparent outline-none text-sm"
            />
          </div>
        </div>

        <table className="w-full text-left">
          <thead className="bg-gray-100 text-gray-700 text-sm">
            <tr>
              <th className="px-6 py-4">Title</th>
              <th className="px-6 py-4">Message</th>
              <th className="px-6 py-4">Type</th>
              <th className="px-6 py-4">Receiver</th>
              <th className="px-6 py-4">Date</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 text-sm">
            {loading ? (
              <tr>
                <td colSpan="5" className="text-center py-12">Loading...</td>
              </tr>
            ) : notifications.length === 0 ? (
              <tr>
                <td colSpan="5" className="text-center py-12 text-gray-500">No notifications found.</td>
              </tr>
            ) : (
              notifications.map((item) => (
                <tr key={item.id} className="hover:bg-purple-50">
                  <td className="px-6 py-4 font-medium">{item.title}</td>
                  <td className="px-6 py-4 text-gray-600">{item.message}</td>
                  <td className="px-6 py-4">
                    <span className="bg-violet-100 text-violet-700 px-3 py-1 rounded-full text-xs font-semibold">
                      {item.type}
                    </span>
                  </td>
                  <td className="px-6 py-4">{item.receiverId}</td>
                  <td className="px-6 py-4 text-xs text-gray-500">{new Date(item.createdAt).toLocaleString()}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        {/* Footer Pagination */}
        <div className="p-4 flex flex-col md:flex-row justify-between items-center gap-4 border-t border-gray-100">
          <p className="text-sm text-gray-600">
            Total Notifications : <span className="font-semibold">{totalElements}</span>
          </p>
          <div className="flex items-center gap-3">
            <button
              disabled={page === 0}
              onClick={() => setPage((prev) => prev - 1)}
              className="px-4 py-2 text-sm rounded-lg bg-gray-100 hover:bg-gray-200 disabled:opacity-50 font-medium"
            >
              Previous
            </button>
            <div className="px-4 py-2 text-sm bg-violet-600 text-white rounded-lg font-medium">
              Page {page + 1} of {Math.max(totalPages, 1)}
            </div>
            <button
              disabled={page + 1 >= totalPages}
              onClick={() => setPage((prev) => prev + 1)}
              className="px-4 py-2 text-sm rounded-lg bg-gray-100 hover:bg-gray-200 disabled:opacity-50 font-medium"
            >
              Next
            </button>
          </div>
        </div>
      </div>

      {/* ================= RECIPIENT DELIVERY DETAILS MODAL ================= */}
      {showDetailsModal && selectedBroadcast && (
        <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl shadow-2xl border border-purple-100 w-full max-w-5xl max-h-[90vh] flex flex-col overflow-hidden animate-in fade-in zoom-in-95 duration-200">

            {/* Modal Header */}
            <div className="p-6 bg-gradient-to-r from-violet-800 to-indigo-900 text-white flex justify-between items-start">
              <div>
                <span className="text-xs uppercase font-bold text-violet-300 tracking-wider">Broadcast Details #{selectedBroadcast.id}</span>
                <h2 className="text-2xl font-bold mt-1">{selectedBroadcast.title}</h2>
                <p className="text-sm text-violet-100 mt-1 max-w-2xl">{selectedBroadcast.message}</p>
              </div>
              <button
                onClick={() => setShowDetailsModal(false)}
                className="text-violet-200 hover:text-white bg-white/10 p-2 rounded-full transition"
              >
                <FaTimes size={18} />
              </button>
            </div>

            {/* Modal Summary Grid */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 p-5 bg-purple-50/50 border-b border-purple-100 text-sm">
              <div>
                <span className="text-xs text-gray-500 block font-medium">Status</span>
                <div className="mt-1">{renderStatusBadge(selectedBroadcast.status)}</div>
              </div>
              <div>
                <span className="text-xs text-gray-500 block font-medium">Total Recipients</span>
                <span className="font-bold text-gray-800 text-base">{(selectedBroadcast.totalRecipients || 0).toLocaleString()}</span>
              </div>
              <div>
                <span className="text-xs text-gray-500 block font-medium">Successful</span>
                <span className="font-bold text-emerald-600 text-base">{(selectedBroadcast.successfulRecipients || 0).toLocaleString()}</span>
              </div>
              <div>
                <span className="text-xs text-gray-500 block font-medium">Failed</span>
                <span className="font-bold text-rose-600 text-base">{(selectedBroadcast.failedRecipients || 0).toLocaleString()}</span>
              </div>
            </div>

            {/* Recipient Search & Filters */}
            <div className="p-5 border-b border-purple-100 flex flex-col md:flex-row justify-between items-center gap-4 bg-gray-50">
              <div className="flex items-center gap-2 border rounded-lg px-3 py-2 bg-white w-full md:w-72">
                <FaSearch className="text-gray-400" />
                <input
                  type="text"
                  placeholder="Search User ID or Email..."
                  value={recipientSearch}
                  onChange={(e) => {
                    setRecipientSearch(e.target.value);
                    setRecipientPage(0);
                  }}
                  className="w-full outline-none text-sm"
                />
              </div>

              <div className="flex items-center gap-3 w-full md:w-auto">
                <div>
                  <label className="text-xs font-semibold text-gray-500 block mb-1">App Status</label>
                  <select
                    value={appStatusFilter}
                    onChange={(e) => {
                      setAppStatusFilter(e.target.value);
                      setRecipientPage(0);
                    }}
                    className="border rounded-lg px-3 py-1.5 text-sm bg-white outline-none"
                  >
                    <option value="">ALL</option>
                    <option value="QUEUED">QUEUED</option>
                    <option value="SENT">SENT</option>
                    <option value="FAILED">FAILED</option>
                  </select>
                </div>

                <div>
                  <label className="text-xs font-semibold text-gray-500 block mb-1">Email Status</label>
                  <select
                    value={emailStatusFilter}
                    onChange={(e) => {
                      setEmailStatusFilter(e.target.value);
                      setRecipientPage(0);
                    }}
                    className="border rounded-lg px-3 py-1.5 text-sm bg-white outline-none"
                  >
                    <option value="">ALL</option>
                    <option value="QUEUED">QUEUED</option>
                    <option value="PROVIDER_ACCEPTED">ACCEPTED</option>
                    <option value="DELIVERED">DELIVERED</option>
                    <option value="FAILED">FAILED</option>
                  </select>
                </div>
              </div>
            </div>

            {/* Recipient Table */}
            <div className="flex-1 overflow-y-auto p-5">
              <table className="w-full text-left border-collapse text-sm">
                <thead className="bg-gray-100 text-gray-700 sticky top-0">
                  <tr>
                    <th className="px-4 py-3">User ID</th>
                    <th className="px-4 py-3">Email</th>
                    <th className="px-4 py-3">App Notification</th>
                    <th className="px-4 py-3">Email Delivery</th>
                    <th className="px-4 py-3">Attempts</th>
                    <th className="px-4 py-3">Error / Status Notes</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {recipientsLoading ? (
                    <tr>
                      <td colSpan="6" className="text-center py-12 text-gray-500">Loading recipient delivery details...</td>
                    </tr>
                  ) : recipients.length === 0 ? (
                    <tr>
                      <td colSpan="6" className="text-center py-12 text-gray-500">No recipient status records found.</td>
                    </tr>
                  ) : (
                    recipients.map((r) => (
                      <tr key={r.id} className="hover:bg-purple-50">
                        <td className="px-4 py-3 font-semibold text-gray-800">#{r.userId}</td>
                        <td className="px-4 py-3 text-gray-600">{r.userEmail || "-"}</td>
                        <td className="px-4 py-3">
                          <span
                            className={`px-2.5 py-1 rounded-full text-xs font-semibold ${r.appNotificationStatus === "SENT"
                                ? "bg-emerald-100 text-emerald-800"
                                : r.appNotificationStatus === "FAILED"
                                  ? "bg-rose-100 text-rose-800"
                                  : "bg-yellow-100 text-yellow-800"
                              }`}
                          >
                            {r.appNotificationStatus}
                          </span>
                        </td>
                        <td className="px-4 py-3">
                          <span
                            className={`px-2.5 py-1 rounded-full text-xs font-semibold ${r.emailStatus === "PROVIDER_ACCEPTED" || r.emailStatus === "DELIVERED"
                                ? "bg-emerald-100 text-emerald-800"
                                : r.emailStatus === "FAILED"
                                  ? "bg-rose-100 text-rose-800"
                                  : "bg-yellow-100 text-yellow-800"
                              }`}
                          >
                            {r.emailStatus}
                          </span>
                        </td>
                        <td className="px-4 py-3 font-medium">{r.emailAttemptCount || 0}</td>
                        <td className="px-4 py-3 text-xs text-rose-600 max-w-xs truncate">
                          {r.emailError || r.lastError || "-"}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Modal Pagination Footer */}
            <div className="p-4 bg-gray-50 border-t border-purple-100 flex justify-between items-center text-sm">
              <span className="text-gray-600">Total Recipients: <span className="font-semibold">{recipientTotalElements}</span></span>
              <div className="flex items-center gap-3">
                <button
                  disabled={recipientPage === 0}
                  onClick={() => setRecipientPage((prev) => Math.max(0, prev - 1))}
                  className="px-3 py-1.5 rounded-lg bg-white border hover:bg-gray-100 disabled:opacity-50 font-medium"
                >
                  Previous
                </button>
                <span className="font-medium text-gray-700">Page {recipientPage + 1} of {Math.max(1, recipientTotalPages)}</span>
                <button
                  disabled={recipientPage + 1 >= recipientTotalPages}
                  onClick={() => setRecipientPage((prev) => prev + 1)}
                  className="px-3 py-1.5 rounded-lg bg-white border hover:bg-gray-100 disabled:opacity-50 font-medium"
                >
                  Next
                </button>
              </div>
            </div>

          </div>
        </div>
      )}
    </div>
  );
}