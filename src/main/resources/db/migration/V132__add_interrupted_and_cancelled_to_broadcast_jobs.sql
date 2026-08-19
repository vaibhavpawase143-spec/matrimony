-- Migration script to expand broadcast_jobs status column and add index if needed
ALTER TABLE broadcast_jobs ALTER COLUMN status TYPE VARCHAR(50);
