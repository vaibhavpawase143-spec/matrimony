ALTER TABLE notifications
ADD COLUMN subscription_id BIGINT;

CREATE INDEX idx_notification_subscription
ON notifications(subscription_id);