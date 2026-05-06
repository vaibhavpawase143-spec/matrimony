import { Heart, Send, MessageSquare, Bookmark, Eye, Users } from 'lucide-react';

const DashboardStats = ({ stats = {} }) => {
  const defaultStats = {
    totalMatches: 24,
    interestsSent: 5,
    interestsReceived: 8,
    bookmarkedProfiles: 12,
    profileViews: 42,
    messages: 6,
    ...stats,
  };

  const statItems = [
    {
      label: 'Total Matches',
      value: defaultStats.totalMatches,
      icon: Users,
      color: 'bg-blue-100 dark:bg-blue-900/30',
      textColor: 'text-blue-600 dark:text-blue-400',
    },
    {
      label: 'Interests Sent',
      value: defaultStats.interestsSent,
      icon: Send,
      color: 'bg-purple-100 dark:bg-purple-900/30',
      textColor: 'text-purple-600 dark:text-purple-400',
    },
    {
      label: 'Interests Received',
      value: defaultStats.interestsReceived,
      icon: Heart,
      color: 'bg-red-100 dark:bg-red-900/30',
      textColor: 'text-red-600 dark:text-red-400',
    },
    {
      label: 'Bookmarked',
      value: defaultStats.bookmarkedProfiles,
      icon: Bookmark,
      color: 'bg-amber-100 dark:bg-amber-900/30',
      textColor: 'text-amber-600 dark:text-amber-400',
    },
    {
      label: 'Profile Views',
      value: defaultStats.profileViews,
      icon: Eye,
      color: 'bg-green-100 dark:bg-green-900/30',
      textColor: 'text-green-600 dark:text-green-400',
    },
    {
      label: 'Messages',
      value: defaultStats.messages,
      icon: MessageSquare,
      color: 'bg-cyan-100 dark:bg-cyan-900/30',
      textColor: 'text-cyan-600 dark:text-cyan-400',
    },
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      {statItems.map((stat, index) => {
        const Icon = stat.icon;
        return (
          <div
            key={index}
            className={`${stat.color} rounded-lg p-4 border border-transparent hover:border-primary/20 transition-all duration-200`}
          >
            <div className="flex items-center justify-between mb-2">
              <p className="text-sm font-medium text-muted-foreground">{stat.label}</p>
              <Icon className={`h-5 w-5 ${stat.textColor}`} />
            </div>
            <p className={`text-3xl font-bold ${stat.textColor}`}>{stat.value}</p>
          </div>
        );
      })}
    </div>
  );
};

export default DashboardStats;