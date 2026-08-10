CREATE INDEX IF NOT EXISTS idx_admin_role_id
ON admins(role_id);

CREATE INDEX IF NOT EXISTS idx_admin_is_active
ON admins(is_active);

CREATE INDEX IF NOT EXISTS idx_admin_created_at
ON admins(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_admin_phone
ON admins(phone);