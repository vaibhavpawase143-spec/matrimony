-- Migration script to add detailed progress tracking columns to broadcast_jobs table
ALTER TABLE broadcast_jobs 
    ALTER COLUMN status TYPE VARCHAR(50);

ALTER TABLE broadcast_jobs
    ADD COLUMN IF NOT EXISTS successful_recipients BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS failed_recipients BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS started_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS completed_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS last_error TEXT;
