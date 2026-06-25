import {
  useState,
  useEffect,
  useCallback
} from "react";

import {
  swipeAPI
} from "@/services/swipeAPI";

const useLikes = () => {
  const [likedIds, setLikedIds] = useState(
    () => new Set()
  );

  const [loading, setLoading] = useState(true);

  const loadMyLikes = useCallback(async () => {
    try {
      const likes = await swipeAPI.getMyLikes();

      const ids = new Set(
        (Array.isArray(likes) ? likes : [])
          .map((like) => Number(like.toUserId))
          .filter(Boolean)
      );

      setLikedIds(ids);

      console.log(
        "MY LIKED PROFILE IDS:",
        [...ids]
      );
    } catch (error) {
      console.error(
        "Failed to load my likes:",
        error
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadMyLikes();
  }, [loadMyLikes]);

  const isLiked = useCallback(
    (id) => likedIds.has(Number(id)),
    [likedIds]
  );

  const toggleLike = useCallback(
    async (id) => {
      const userId = Number(id);

      if (!userId) {
        throw new Error("Invalid user ID for like");
      }

      if (likedIds.has(userId)) {
        await swipeAPI.remove(userId);

        setLikedIds((previous) => {
          const next = new Set(previous);
          next.delete(userId);
          return next;
        });

        window.dispatchEvent(
          new Event("like:updated")
        );

        window.dispatchEvent(
          new Event("dashboardUpdated")
        );

        return { liked: false };
      }

      await swipeAPI.like(userId);

      setLikedIds((previous) => {
        const next = new Set(previous);
        next.add(userId);
        return next;
      });

      window.dispatchEvent(
        new Event("like:updated")
      );

      window.dispatchEvent(
        new Event("dashboardUpdated")
      );

      return { liked: true };
    },
    [likedIds]
  );

  return {
    isLiked,
    toggleLike,
    loading,
    refreshLikes: loadMyLikes
  };
};

export default useLikes;