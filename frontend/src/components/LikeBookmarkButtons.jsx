import { Heart, Bookmark } from 'lucide-react';
import { useState } from 'react';

const LikeBookmarkButtons = ({ profileId, isLiked = false, isBookmarked = false, onLike, onBookmark, size = 'md', showLabels = true }) => {
  const [likeAnimating, setLikeAnimating] = useState(false);
  const [bookmarkAnimating, setBookmarkAnimating] = useState(false);

  const handleLike = () => {
    setLikeAnimating(true);
    onLike && onLike(profileId);
    setTimeout(() => setLikeAnimating(false), 600);
  };

  const handleBookmark = () => {
    setBookmarkAnimating(true);
    onBookmark && onBookmark(profileId);
    setTimeout(() => setBookmarkAnimating(false), 600);
  };

  const sizeClasses = {
    sm: 'px-2 py-1 text-xs gap-1',
    md: 'px-3 py-2 text-sm gap-2',
    lg: 'px-4 py-2.5 text-base gap-2',
  };

  const iconSizes = {
    sm: 'h-4 w-4',
    md: 'h-5 w-5',
    lg: 'h-6 w-6',
  };

  return (
    <div className="flex gap-2 items-center">
      {/* Like Button */}
      <button
        onClick={handleLike}
        className={`${sizeClasses[size]} inline-flex items-center justify-center rounded-lg font-medium transition-all duration-200 ${
          isLiked
            ? 'bg-red-100 dark:bg-red-900/30 text-red-600 dark:text-red-400 hover:bg-red-200 dark:hover:bg-red-900/50'
            : 'bg-muted dark:bg-muted/50 text-muted-foreground hover:bg-primary/10 hover:text-primary'
        } ${likeAnimating ? 'scale-110' : 'scale-100'}`}
        title={isLiked ? 'Unlike' : 'Like'}
      >
        <Heart
          className={`${iconSizes[size]} ${isLiked ? 'fill-current' : ''}`}
        />
        {showLabels && <span>{isLiked ? 'Liked' : 'Like'}</span>}
      </button>

      {/* Bookmark Button */}
      <button
        onClick={handleBookmark}
        className={`${sizeClasses[size]} inline-flex items-center justify-center rounded-lg font-medium transition-all duration-200 ${
          isBookmarked
            ? 'bg-amber-100 dark:bg-amber-900/30 text-amber-600 dark:text-amber-400 hover:bg-amber-200 dark:hover:bg-amber-900/50'
            : 'bg-muted dark:bg-muted/50 text-muted-foreground hover:bg-primary/10 hover:text-primary'
        } ${bookmarkAnimating ? 'scale-110' : 'scale-100'}`}
        title={isBookmarked ? 'Remove bookmark' : 'Bookmark'}
      >
        <Bookmark
          className={`${iconSizes[size]} ${isBookmarked ? 'fill-current' : ''}`}
        />
        {showLabels && <span>{isBookmarked ? 'Saved' : 'Save'}</span>}
      </button>
    </div>
  );
};

export default LikeBookmarkButtons;