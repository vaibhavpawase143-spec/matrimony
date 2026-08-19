-- Add publish_version column to success_stories for publish notification idempotency tracking
ALTER TABLE success_stories ADD COLUMN publish_version INTEGER NOT NULL DEFAULT 0;
