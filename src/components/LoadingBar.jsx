import { useEffect, useState } from 'react';
import { Progress } from '@/components/ui/progress';
import { useLoading } from '@/hooks/useLoading';

const LoadingBar = () => {
  const { isLoading, loadingMessage } = useLoading();
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    if (isLoading) {
      setProgress(0);
      const interval = setInterval(() => {
        setProgress(prev => {
          if (prev >= 90) return prev;
          return prev + Math.random() * 10;
        });
      }, 200);

      return () => clearInterval(interval);
    } else {
      setProgress(100);
      const timeout = setTimeout(() => setProgress(0), 300);
      return () => clearTimeout(timeout);
    }
  }, [isLoading]);

  if (!isLoading && progress === 0) return null;

  return (
    <div className="fixed top-0 left-0 right-0 z-50">
      <Progress
        value={progress}
        className="h-1 bg-transparent"
      />
      {loadingMessage && (
        <div className="bg-primary text-primary-foreground text-xs px-4 py-1 text-center">
          {loadingMessage}
        </div>
      )}
    </div>
  );
};

export default LoadingBar;