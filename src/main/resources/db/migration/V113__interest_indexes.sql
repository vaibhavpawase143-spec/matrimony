CREATE INDEX IF NOT EXISTS idx_interest_status
ON interests(status);

CREATE INDEX IF NOT EXISTS idx_interest_active_status
ON interests(is_active, status);