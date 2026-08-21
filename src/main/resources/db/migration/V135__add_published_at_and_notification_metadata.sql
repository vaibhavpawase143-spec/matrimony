-- Add published_at column to success_stories for publication recency tracking
ALTER TABLE success_stories ADD COLUMN published_at TIMESTAMP;

-- Add reference_id and event_type columns to notifications
ALTER TABLE notifications ADD COLUMN reference_id BIGINT;
ALTER TABLE notifications ADD COLUMN event_type VARCHAR(50);

-- Add reference_id and event_type columns to admin_notifications
ALTER TABLE admin_notifications ADD COLUMN reference_id BIGINT;
ALTER TABLE admin_notifications ADD COLUMN event_type VARCHAR(50);
