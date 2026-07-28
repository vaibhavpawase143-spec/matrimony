-- Drop old notification type constraint
ALTER TABLE notifications
DROP CONSTRAINT IF EXISTS chk_notification_type;

-- Recreate constraint with all supported notification types
ALTER TABLE notifications
ADD CONSTRAINT chk_notification_type
CHECK (
    type IN (
        'REQUEST',
        'VIEW',
        'MESSAGE',
        'SHORTLIST',
        'ACCEPT',
        'REJECT',
        'LIKE',
        'MATCH',

        'ANNOUNCEMENT',
        'SYSTEM',
        'MAINTENANCE',
        'SUBSCRIPTION',
        'WARNING',

        'REPORT',
        'SUPPORT',
        'NEW_USER',
        'ADMIN'
    )
);