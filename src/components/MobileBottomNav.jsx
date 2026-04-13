import { useLocation, useNavigate } from 'react-router-dom';
import { Home, Search, Heart, MessageSquare, User } from 'lucide-react';

const MobileBottomNav = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const navItems = [
    { path: '/home', label: 'Home', icon: Home },
    { path: '/search', label: 'Search', icon: Search },
    { path: '/matches', label: 'Matches', icon: Heart },
    { path: '/messages', label: 'Chat', icon: MessageSquare },
    { path: '/account', label: 'Profile', icon: User },
  ];

  const isActive = (path) => location.pathname === path;

  return (
    <nav className="md:hidden fixed bottom-0 left-0 right-0 bg-background dark:bg-background border-t border-border z-40">
      <div className="flex justify-around items-center h-16">
        {navItems.map(item => {
          const Icon = item.icon;
          const active = isActive(item.path);

          return (
            <button
              key={item.path}
              onClick={() => navigate(item.path)}
              className={`flex flex-col items-center justify-center flex-1 h-full transition-colors ${
                active
                  ? 'text-primary border-t-2 border-primary'
                  : 'text-muted-foreground hover:text-foreground'
              }`}
              title={item.label}
            >
              <Icon className="h-6 w-6" />
              <span className="text-xs mt-1 truncate">{item.label}</span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};

export default MobileBottomNav;