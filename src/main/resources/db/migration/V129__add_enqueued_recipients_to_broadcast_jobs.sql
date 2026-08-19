-- Migration script to add enqueued_recipients column to broadcast_jobs table
ALTER TABLE broadcast_jobs
    ADD COLUMN IF NOT EXISTS enqueued_recipients BIGINT NOT NULL DEFAULT 0;
