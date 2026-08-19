-- Migration script to add aggregate_processed tracking columns to broadcast_recipient_status table
ALTER TABLE broadcast_recipient_status
    ADD COLUMN IF NOT EXISTS aggregate_processed BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS aggregate_processed_at TIMESTAMP;

-- Create performance indexes for atomic status updates and status queries
CREATE INDEX IF NOT EXISTS idx_brs_job_aggregate ON broadcast_recipient_status(broadcast_job_id, aggregate_processed);
CREATE INDEX IF NOT EXISTS idx_brs_job_app_status ON broadcast_recipient_status(broadcast_job_id, app_notification_status);
CREATE INDEX IF NOT EXISTS idx_brs_job_email_status ON broadcast_recipient_status(broadcast_job_id, email_status);
