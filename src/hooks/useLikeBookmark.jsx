import { useState, useEffect } from 'react';

export const useLikeBookmark = () => {
  const [likes, setLikes] = useState(() => {
    const saved = localStorage.getItem('profileLikes');
    return saved ? JSON.parse(saved) : [];
  });

  const [bookmarks, setBookmarks] = useState(() => {
    const saved = localStorage.getItem('profileBookmarks');
    return saved ? JSON.parse(saved) : [];
  });

  useEffect(() => {
    localStorage.setItem('profileLikes', JSON.stringify(likes));
  }, [likes]);

  useEffect(() => {
    localStorage.setItem('profileBookmarks', JSON.stringify(bookmarks));
  }, [bookmarks]);

  const isLiked = (profileId) => likes.includes(profileId);
  const isBookmarked = (profileId) => bookmarks.includes(profileId);

  const toggleLike = (profileId) => {
    setLikes(prev =>
      prev.includes(profileId)
        ? prev.filter(id => id !== profileId)
        : [...prev, profileId]
    );
  };

  const toggleBookmark = (profileId) => {
    setBookmarks(prev =>
      prev.includes(profileId)
        ? prev.filter(id => id !== profileId)
        : [...prev, profileId]
    );
  };

  return {
    likes,
    bookmarks,
    isLiked,
    isBookmarked,
    toggleLike,
    toggleBookmark,
    likeCount: likes.length,
    bookmarkCount: bookmarks.length,
  };
};