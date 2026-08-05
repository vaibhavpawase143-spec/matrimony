import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { AuthProvider } from "@/hooks/useAuth";
import { LoadingProvider } from "@/hooks/useLoading";
import { ToastProvider } from "@/components/Toast";
import { LanguageProvider } from "@/context/LanguageContext";
import { ThemeProvider } from "@/context/ThemeContext";
import AdminRoute from "@/routes/AdminRoute";
import LoadingSpinner from "./components/LoadingSpinner";
import MobileBottomNav from "@/components/MobileBottomNav";
import AuthenticatedLayout from "@/components/AuthenticatedLayout";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";
import AnalyticsTracker from "@/components/AnalyticsTracker";
import { Suspense, lazy, useEffect } from "react";

// ==========================================
// ✅ CODE SPLITTING: LAZY LOAD ALL PAGES
// ==========================================

// PUBLIC PAGES
const Index = lazy(() => import("./pages/Index"));
const Login = lazy(() => import("./pages/Login"));
const Register = lazy(() => import("./pages/Register"));
const VerifyEmail = lazy(() => import("./pages/VerifyEmail"));
const RequestVerification = lazy(() => import("./pages/RequestVerification"));
const ForgotPassword = lazy(() => import("./pages/ForgotPassword"));
const ResetPassword = lazy(() => import("./pages/ResetPassword"));

const About = lazy(() => import("./pages/About"));
const Contact = lazy(() => import("./pages/Contact"));
const PrivacyPolicy = lazy(() => import("./pages/PrivacyPolicy"));
const TermsConditions = lazy(() => import("./pages/TermsConditions"));
const FAQ = lazy(() => import("./pages/FAQ"));
const HelpSupport = lazy(() => import("./pages/HelpSupport"));
const RefundPolicy = lazy(() => import("./pages/RefundPolicy"));
const EmailVerified = lazy(() => import("./pages/EmailVerified"));

const CookiePolicy = lazy(() => import("./pages/CookiePolicy"));
const SafetyTips = lazy(() => import("./pages/SafetyTips"));
const HowItWorks = lazy(() => import("./pages/HowItWorks"));
const MembershipPlans = lazy(() => import("./pages/MembershipPlans"));
const CommunityGuidelines = lazy(() => import("./pages/CommunityGuidelines"));

const NotFound = lazy(() => import("./pages/NotFound"));

// USER PAGES
const Home = lazy(() => import("./pages/Home"));
const CreateProfile = lazy(() => import("./pages/CreateProfile"));
const Search = lazy(() => import("./pages/Search"));
const ProfileDetails = lazy(() => import("./pages/ProfileDetails"));
const Matches = lazy(() => import("./pages/Matches"));
const MatchDetails = lazy(() => import("./pages/MatchDetails"));
const Kundli = lazy(() => import("./pages/Kundli"));

const Messages = lazy(() => import("./pages/Messages"));
const ChatPage = lazy(() => import("./pages/ChatPage"));
const NotificationDetails = lazy(() => import("./pages/NotificationDetails"));

const SentInterests = lazy(() => import("./pages/SentInterests"));
const ReceivedInterests = lazy(() => import("./pages/ReceivedInterests"));
const Likes = lazy(() => import("./pages/Likes"));
const ProfileVisitors = lazy(() => import("./pages/ProfileVisitors"));
const MyShortlists = lazy(() => import("./pages/MyShortlists"));

const SettingsPage = lazy(() => import("./pages/SettingsPage"));
const Account = lazy(() => import("./pages/Account"));
const UpgradePremium = lazy(() => import("./pages/UpgradePremium"));
const SubscriptionHistory = lazy(() => import("./pages/SubscriptionHistory"));

// SUPPORT
const SupportTickets = lazy(() => import("./pages/SupportTickets"));
const SupportTicketDetails = lazy(() => import("./pages/SupportTicketDetails"));

// ADMIN
const AdminDashboard = lazy(() => import("./pages/admin/AdminDashboard"));
const AdminSupportTickets = lazy(() => import("./pages/admin/AdminSupportTickets"));
const AdminSupportTicketDetails = lazy(() => import("./pages/admin/AdminSupportTicketDetails"));
const UsersPage = lazy(() => import("./pages/admin/UsersPage"));
const PaymentsPage = lazy(() => import("./pages/admin/PaymentsPage"));
const VerificationPage = lazy(() => import("./pages/admin/VerificationPage"));

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,
      gcTime: 10 * 60 * 1000,
      retry: 1,
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: 0,
    },
  },
});

const App = () => {
  // Uncomment if you need online ping
  /*
  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) return;

    const pingServer = async () => {
      try {
        await fetch("/api/chat/ping", {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      } catch (err) {
        console.log("PING ERROR", err);
      }
    };

    pingServer();

    const interval = setInterval(pingServer, 10000);

    return () => clearInterval(interval);
  }, []);
  */

  return (
    <QueryClientProvider client={queryClient}>
      <LanguageProvider>
        <ThemeProvider>
          <LoadingProvider>
            <ToastProvider>
              <TooltipProvider>
                <LoadingSpinner />
                <Toaster />
                <Sonner />
                <AuthProvider>
                  <BrowserRouter>
                    <AnalyticsTracker />
                    <MobileBottomNav />

                    <Suspense fallback={<LoadingSpinner />}>
                      <Routes>
                          {/* ==========================================
                              PUBLIC ROUTES
                          ========================================== */}

                          <Route path="/" element={<Index />} />
                          <Route path="/login" element={<Login />} />
                          <Route path="/register" element={<Register />} />
                          <Route path="/verify-email" element={<VerifyEmail />} />
                          <Route path="/request-verification" element={<RequestVerification />} />
                          <Route path="/forgot-password" element={<ForgotPassword />} />
                          <Route path="/reset-password" element={<ResetPassword />} />

                          <Route path="/about" element={<About />} />
                          <Route path="/contact" element={<Contact />} />
                          <Route path="/privacy-policy" element={<PrivacyPolicy />} />
                          <Route path="/terms" element={<TermsConditions />} />
                          <Route path="/faq" element={<FAQ />} />
                          <Route path="/help" element={<HelpSupport />} />
                          <Route path="/refund-policy" element={<RefundPolicy />} />

                          <Route path="/cookie-policy" element={<CookiePolicy />} />
                          <Route path="/safety-tips" element={<SafetyTips />} />
                          <Route path="/how-it-works" element={<HowItWorks />} />
                          <Route path="/membership-plans" element={<MembershipPlans />} />
                          <Route path="/community-guidelines" element={<CommunityGuidelines />} />

                          <Route path="/email-verified" element={<EmailVerified />} />

                          {/* ==========================================
                              PROTECTED USER ROUTES
                          ========================================== */}

                          <Route
                            path="/home"
                            element={
                              <AuthenticatedLayout>
                                <Home />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/profie/create"
                            element={
                              <AuthenticatedLayout>
                                <CreateProfile />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/search"
                            element={
                              <AuthenticatedLayout>
                                <Search />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/profile/:id"
                            element={
                              <AuthenticatedLayout>
                                <ProfileDetails />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/kundli"
                            element={
                              <AuthenticatedLayout>
                                <Kundli />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/matches"
                            element={
                              <AuthenticatedLayout>
                                <Matches />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/match-details/:partnerId"
                            element={
                              <AuthenticatedLayout>
                                <MatchDetails />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/messages"
                            element={
                              <AuthenticatedLayout>
                                <Messages />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/chat/:conversationId/:receiverId"
                            element={<ChatPage />}
                          />

                          <Route
                            path="/notifications/:id"
                            element={
                              <AuthenticatedLayout>
                                <NotificationDetails />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/sent-interests"
                            element={
                              <AuthenticatedLayout>
                                <SentInterests />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/received-interests"
                            element={
                              <AuthenticatedLayout>
                                <ReceivedInterests />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/likes"
                            element={
                              <AuthenticatedLayout>
                                <Likes />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/profile-visitors"
                            element={
                              <AuthenticatedLayout>
                                <ProfileVisitors />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/shortlists"
                            element={
                              <AuthenticatedLayout>
                                <MyShortlists />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/settings"
                            element={
                              <AuthenticatedLayout>
                                <SettingsPage />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/account"
                            element={
                              <AuthenticatedLayout>
                                <Account />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/upgrade"
                            element={
                              <AuthenticatedLayout>
                                <UpgradePremium />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/subscription-history"
                            element={
                              <AuthenticatedLayout>
                                <SubscriptionHistory />
                              </AuthenticatedLayout>
                            }
                          />
                          {/* ==========================================
                              SUPPORT ROUTES
                          ========================================== */}

                          <Route
                            path="/support/tickets"
                            element={
                              <AuthenticatedLayout>
                                <SupportTickets />
                              </AuthenticatedLayout>
                            }
                          />

                          <Route
                            path="/support/tickets/:ticketNumber"
                            element={
                              <AuthenticatedLayout>
                                <SupportTicketDetails />
                              </AuthenticatedLayout>
                            }
                          />

                          {/* ==========================================
                              ADMIN ROUTES
                          ========================================== */}

                          <Route
                            path="/admin"
                            element={
                              <AdminRoute>
                                <AdminDashboard />
                              </AdminRoute>
                            }
                          />

                          <Route
                            path="/admin/support"
                            element={
                              <AdminRoute>
                                <AdminSupportTickets />
                              </AdminRoute>
                            }
                          />

                          <Route
                            path="/admin/support/:ticketNumber"
                            element={
                              <AdminRoute>
                                <AdminSupportTicketDetails />
                              </AdminRoute>
                            }
                          />

                          <Route
                            path="/admin/users"
                            element={
                              <AdminRoute>
                                <UsersPage />
                              </AdminRoute>
                            }
                          />

                          <Route
                            path="/admin/payments"
                            element={
                              <AdminRoute>
                                <PaymentsPage />
                              </AdminRoute>
                            }
                          />

                          <Route
                            path="/admin/verification"
                            element={
                              <AdminRoute>
                                <VerificationPage />
                              </AdminRoute>
                            }
                          />

                          {/* ==========================================
                              NOT FOUND
                          ========================================== */}

                          <Route path="*" element={<NotFound />} />

                          </Routes>
                          </Suspense>
                          </BrowserRouter>
                          </AuthProvider>
                          </TooltipProvider>
                          </ToastProvider>
                          </LoadingProvider>
                          </ThemeProvider>
                          </LanguageProvider>
                          </QueryClientProvider>
                          );
                          };

                          export default App;