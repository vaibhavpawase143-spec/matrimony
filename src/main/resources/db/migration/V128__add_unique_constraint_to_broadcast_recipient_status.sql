-- Migration V128: Clean duplicate broadcast recipient status records and add unique constraint

-- Step 1: Consolidate best status into one primary row per (broadcast_job_id, user_id)
WITH merged_status AS (
    SELECT broadcast_job_id, user_id,
           MAX(id) AS target_id,
           COALESCE(MAX(CASE WHEN app_notification_status = 'SENT' THEN 'SENT' WHEN app_notification_status = 'FAILED' THEN 'FAILED' END), 'QUEUED') AS best_app_status,
           COALESCE(MAX(CASE WHEN email_status = 'DELIVERED' THEN 'DELIVERED' WHEN email_status = 'PROVIDER_ACCEPTED' THEN 'PROVIDER_ACCEPTED' WHEN email_status = 'FAILED' THEN 'FAILED' END), 'QUEUED') AS best_email_status
    FROM broadcast_recipient_status
    GROUP BY broadcast_job_id, user_id
    HAVING COUNT(*) > 1
)
UPDATE broadcast_recipient_status b
SET app_notification_status = m.best_app_status,
    email_status = m.best_email_status,
    updated_at = CURRENT_TIMESTAMP
FROM merged_status m
WHERE b.id = m.target_id;

-- Step 2: Delete redundant duplicate rows keeping only the highest id (target_id)
WITH ranked_rows AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY broadcast_job_id, user_id 
               ORDER BY id DESC
           ) AS rn
    FROM broadcast_recipient_status
)
DELETE FROM broadcast_recipient_status
WHERE id IN (
    SELECT id FROM ranked_rows WHERE rn > 1
);

-- Step 3: Drop existing non-unique index if it exists
DROP INDEX IF EXISTS idx_brs_job_user;

-- Step 4: Create UNIQUE index to enforce exactly one recipient status row per broadcast job and user
CREATE UNIQUE INDEX IF NOT EXISTS uk_broadcast_recipient_job_user ON broadcast_recipient_status(broadcast_job_id, user_id);
