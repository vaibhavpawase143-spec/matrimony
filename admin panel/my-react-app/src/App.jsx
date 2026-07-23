import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Users from "./pages/Users";
import UserDetails from "./pages/UserDetails";
import Payments from "./pages/Payments";
import Subscriptions from "./pages/Subscriptions";
import SubscriptionDetails from "./pages/SubscriptionDetails";
import Notifications from "./pages/Notifications";
import SupportTicketDetails from "./pages/SupportTicketDetails";
import ReportedProfiles from "./pages/ReportedProfiles";
import AdminProfile from "./pages/AdminProfile";
import SupportTickets from "./pages/SupportTickets";
import CMSPages from "./pages/CMSPages";
import FAQ from "./pages/FAQ";
import MasterData from "./pages/MasterData";
import AuditLogs from "./pages/AuditLogs";

import AdminLayout from "./components/AdminLayout";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login */}
        <Route path="/" element={<Login />} />

        {/* Admin Panel */}
        <Route element={<AdminLayout />}>

          {/* Profile */}
          <Route
            path="/profile"
            element={
              <ProtectedRoute module="profile">
                <AdminProfile />
              </ProtectedRoute>
            }
          />

          {/* Dashboard */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute module="dashboard">
                <Dashboard />
              </ProtectedRoute>
            }
          />

          {/* Users */}
          <Route
            path="/users"
            element={
              <ProtectedRoute module="users">
                <Users />
              </ProtectedRoute>
            }
          />

          <Route
            path="/users/:id"
            element={
              <ProtectedRoute module="users">
                <UserDetails />
              </ProtectedRoute>
            }
          />

          {/* Payments */}
          <Route
            path="/payments"
            element={
              <ProtectedRoute module="payments">
                <Payments />
              </ProtectedRoute>
            }
          />

          {/* Subscriptions */}
          <Route
            path="/subscriptions"
            element={
              <ProtectedRoute module="subscriptions">
                <Subscriptions />
              </ProtectedRoute>
            }
          />

          <Route
            path="/subscriptions/:id"
            element={
              <ProtectedRoute module="subscriptions">
                <SubscriptionDetails />
              </ProtectedRoute>
            }
          />

          {/* Notifications */}
          <Route
            path="/notifications"
            element={
              <ProtectedRoute module="notifications">
                <Notifications />
              </ProtectedRoute>
            }
          />

          {/* Reported Profiles */}
          <Route
            path="/reported-profiles"
            element={
              <ProtectedRoute module="reportedProfiles">
                <ReportedProfiles />
              </ProtectedRoute>
            }
          />

          {/* Support Tickets */}
          <Route
            path="/support-tickets"
            element={
              <ProtectedRoute module="supportTickets">
                <SupportTickets />
              </ProtectedRoute>
            }
          />

          {/* FAQ */}
          <Route
            path="/faqs"
            element={
              <ProtectedRoute module="faqs">
                <FAQ />
              </ProtectedRoute>
            }
          />

          {/* CMS */}
          <Route
            path="/cms-pages"
            element={
              <ProtectedRoute module="cmsPages">
                <CMSPages />
              </ProtectedRoute>
            }
          />

          {/* Master Data */}
          <Route
            path="/master-data"
            element={
              <ProtectedRoute module="masterData">
                <MasterData />
              </ProtectedRoute>
            }
          />

          {/* Audit Logs */}
          <Route
            path="/audit-logs"
            element={
              <ProtectedRoute module="auditLogs">
                <AuditLogs />
              </ProtectedRoute>
            }
          />
        </Route>

        {/* Support Ticket Details */}
        <Route
          path="/support-tickets/:ticketNumber"
          element={
            <ProtectedRoute module="supportTickets">
              <SupportTicketDetails />
            </ProtectedRoute>
          }
        />

        {/* Invalid URL */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;