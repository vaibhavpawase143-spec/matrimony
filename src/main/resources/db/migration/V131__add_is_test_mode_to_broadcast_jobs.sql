-- Migration V131: Add is_test_mode column to broadcast_jobs table
ALTER TABLE broadcast_jobs ADD COLUMN IF NOT EXISTS is_test_mode BOOLEAN DEFAULT FALSE;
