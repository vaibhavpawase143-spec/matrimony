import { useLocation, useNavigate } from 'react-router-dom';
import { Home, Search, Heart, MessageSquare, User } from 'lucide-react';
import { useState } from "react";
import { subscriptionAPI } from "@/services/api";
const MobileBottomNav = () => {
  const navigate = useNavigate();
  const location = useLocation();
const [showUpgradePopup, setShowUpgradePopup] = useState(false);
  const navItems = [
    { path: '/home', label: 'Home', icon: Home },
    { path: '/search', label: 'Search', icon: Search },
    { path: '/matches', label: 'Matches', icon: Heart },
    { path: '/messages', label: 'Chat', icon: MessageSquare },
    { path: '/account', label: 'Profile', icon: User },
  ];

  const isActive = (path) => location.pathname === path;

  return (
  <>
      <nav className="md:hidden fixed bottom-0 left-0 right-0 bg-background dark:bg-background border-t border-border z-40">

          <div className="flex justify-around items-center h-16">

              {navItems.map(item => {

                  const Icon = item.icon;
                  const active = isActive(item.path);

                  return (

                      <button
                          key={item.path}
                          onClick={async () => {

                              if (item.path === "/messages") {

                                  const subscription =
                                      await subscriptionAPI.getMySubscription();

                                  if (subscription?.isActive) {

                                      navigate("/messages");

                                  } else {

                                      setShowUpgradePopup(true);

                                  }

                                  return;
                              }

                              navigate(item.path);

                          }}
                          className={`flex flex-col items-center justify-center flex-1 h-full transition-colors ${
                              active
                                  ? "text-primary border-t-2 border-primary"
                                  : "text-muted-foreground hover:text-foreground"
                          }`}
                          title={item.label}
                      >

                          <Icon className="h-6 w-6" />

                          <span className="text-xs mt-1 truncate">
                              {item.label}
                          </span>

                      </button>

                  );

              })}

          </div>

      </nav>

      {showUpgradePopup && (

          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">

              <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

                  <div className="text-6xl mb-4">
                      👑
                  </div>

                  <h2 className="text-2xl font-bold mb-3">
                      Premium Required
                  </h2>

                  <p className="text-gray-600 mb-6">
                      Chat is available only for Premium members.
                  </p>

                  <div className="flex justify-center gap-3">

                      <button
                          onClick={() => setShowUpgradePopup(false)}
                          className="px-5 py-2 rounded-xl bg-gray-200"
                      >
                          Home
                      </button>

                      <button
                          onClick={() => {
                              setShowUpgradePopup(false);
                              navigate("/upgrade");
                          }}
                          className="px-5 py-2 rounded-xl bg-pink-600 text-white"
                      >
                          Upgrade Premium
                      </button>

                  </div>

              </div>

          </div>

            )}

        </>
      );

      };

      export default MobileBottomNav;