import { Progress } from '@/components/ui/progress';
import { CheckCircle2, AlertCircle } from 'lucide-react';

const ProfileCompletionBar = ({ completionPercentage = 0, message = '', showMessage = true, compact = false }) => {
  const getColor = () => {
    if (completionPercentage === 100) return 'bg-green-500';
    if (completionPercentage >= 75) return 'bg-blue-500';
    if (completionPercentage >= 50) return 'bg-amber-500';
    return 'bg-orange-500';
  };

  if (compact) {
    return (
      <div className="flex items-center gap-2">
        <div className="flex-1">
          <Progress
            value={completionPercentage}
            className="h-2"
          />
        </div>
        <span className="text-sm font-semibold text-muted-foreground whitespace-nowrap">
          {completionPercentage}%
        </span>
      </div>
    );
  }

  return (
    <div className="bg-gradient-to-r from-primary/5 to-primary/10 dark:from-primary/10 dark:to-primary/5 border border-primary/20 rounded-lg p-4 space-y-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          {completionPercentage === 100 ? (
            <CheckCircle2 className="h-5 w-5 text-green-500 flex-shrink-0" />
          ) : (
            <AlertCircle className="h-5 w-5 text-amber-500 flex-shrink-0" />
          )}
          <h3 className="font-semibold text-foreground">Profile Completion</h3>
        </div>
        <span className="text-sm font-bold text-primary">{completionPercentage}%</span>
      </div>

      <Progress
        value={completionPercentage}
        className="h-3"
      />

      {showMessage && message && (
        <p className="text-sm text-muted-foreground">{message}</p>
      )}
    </div>
  );
};

export default ProfileCompletionBar;