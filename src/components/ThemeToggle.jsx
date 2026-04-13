import { Moon, Sun } from 'lucide-react';
import { useDarkMode } from '@/hooks/useDarkMode';

const ThemeToggle = ({ showLabel = false }) => {
  const { isDarkMode, toggleDarkMode } = useDarkMode();

  return (
    <button
      onClick={toggleDarkMode}
      className="p-2 rounded-lg hover:bg-muted dark:hover:bg-muted/50 transition-colors"
      title={isDarkMode ? 'Light mode' : 'Dark mode'}
      aria-label="Toggle dark mode"
    >
      {isDarkMode ? (
        <div className="flex items-center gap-2">
          <Sun className="h-5 w-5 text-amber-500" />
          {showLabel && <span className="text-sm">Light</span>}
        </div>
      ) : (
        <div className="flex items-center gap-2">
          <Moon className="h-5 w-5 text-slate-700" />
          {showLabel && <span className="text-sm">Dark</span>}
        </div>
      )}
    </button>
  );
};

export default ThemeToggle;