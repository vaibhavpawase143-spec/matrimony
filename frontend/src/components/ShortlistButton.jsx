import { useState } from 'react';
import { Star } from 'lucide-react';
import useShortlist from '@/hooks/useShortlist';
import toast from 'react-hot-toast';

const ShortlistButton = ({ profileId, size = 'md', showLabel = true }) => {
  const { isShortlisted, add, remove, loading } = useShortlist();
  const [localLoading, setLocalLoading] = useState(false);

  const shortlisted = isShortlisted(profileId);

  const handleClick = async (e) => {
    e.stopPropagation();
    if (localLoading || loading) return;
    setLocalLoading(true);
    try {
      if (shortlisted) {
        await remove(profileId);
        toast.success('Removed from shortlists');
      } else {
        await add(profileId);
        toast.success('Added to shortlists');
      }
    } catch (err) {
      console.error('Shortlist error', err);
      toast.error(err.message || 'Action failed');
    } finally {
      setLocalLoading(false);
    }
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
    <button
      onClick={handleClick}
className="

w-12

h-12

rounded-xl

bg-gradient-to-br

from-purple-500

to-indigo-600

shadow-md

hover:scale-110

transition-all

duration-300

inline-flex

items-center

justify-center

text-white

"
      title={

      shortlisted

      ?

      "Shortlisted"

      :

      "Shortlist"

      }
    >
      <Star className={`${iconSizes[size]} ${shortlisted ? 'fill-current' : ''}`} />
      {showLabel && <span>{shortlisted ? 'Shortlisted' : 'Shortlist'}</span>}
    </button>
  );
};

export default ShortlistButton;

