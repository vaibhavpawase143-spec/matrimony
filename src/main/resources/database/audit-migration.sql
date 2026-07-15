-- ============================================================================
-- AUDIT COLUMNS MIGRATION
-- Production-Level Audit Trail Implementation
-- ============================================================================
-- This migration adds comprehensive audit fields to all core entities:
-- - created_by: User ID who created the record
-- - created_at: Timestamp when record was created (may already exist)
-- - updated_by: User ID who last updated the record
-- - updated_at: Timestamp of last update (may already exist)
-- - is_deleted: Soft delete flag
-- - deleted_at: Timestamp when soft deleted
-- - deleted_by: User ID who deleted the record
-- - deletion_reason: Reason for deletion (optional)
-- - version: Optimistic locking version
-- ============================================================================

-- ALTER TABLE users
-- Add audit columns if they don't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE users ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion (soft delete)';
ALTER TABLE users ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

-- Add indexes for audit fields
CREATE INDEX IF NOT EXISTS idx_users_created_by ON users(created_by);
CREATE INDEX IF NOT EXISTS idx_users_updated_by ON users(updated_by);
CREATE INDEX IF NOT EXISTS idx_users_deleted_by ON users(deleted_by);
CREATE INDEX IF NOT EXISTS idx_users_is_deleted ON users(is_deleted);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_users_updated_at ON users(updated_at);

-- ============================================================================
-- PROFILES TABLE
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_profiles_created_by ON profiles(created_by);
CREATE INDEX IF NOT EXISTS idx_profiles_updated_by ON profiles(updated_by);
CREATE INDEX IF NOT EXISTS idx_profiles_deleted_by ON profiles(deleted_by);
CREATE INDEX IF NOT EXISTS idx_profiles_is_deleted ON profiles(is_deleted);
CREATE INDEX IF NOT EXISTS idx_profiles_created_at ON profiles(created_at);

-- ============================================================================
-- PARTNER PREFERENCES TABLE
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE partner_preferences ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_partner_prefs_created_by ON partner_preferences(created_by);
CREATE INDEX IF NOT EXISTS idx_partner_prefs_updated_by ON partner_preferences(updated_by);
CREATE INDEX IF NOT EXISTS idx_partner_prefs_is_deleted ON partner_preferences(is_deleted);

-- ============================================================================
-- INTERESTS TABLE
ALTER TABLE interests ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE interests ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_interests_created_by ON interests(created_by);
CREATE INDEX IF NOT EXISTS idx_interests_updated_by ON interests(updated_by);
CREATE INDEX IF NOT EXISTS idx_interests_is_deleted ON interests(is_deleted);

-- ============================================================================
-- SHORTLISTS TABLE
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE shortlists ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_shortlists_created_by ON shortlists(created_by);
CREATE INDEX IF NOT EXISTS idx_shortlists_updated_by ON shortlists(updated_by);
CREATE INDEX IF NOT EXISTS idx_shortlists_is_deleted ON shortlists(is_deleted);

-- ============================================================================
-- MESSAGES TABLE
ALTER TABLE messages ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE messages ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE messages ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE messages ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE messages ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_messages_created_by ON messages(created_by);
CREATE INDEX IF NOT EXISTS idx_messages_is_deleted ON messages(is_deleted);

-- ============================================================================
-- PAYMENTS TABLE
ALTER TABLE payments ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_payments_created_by ON payments(created_by);
CREATE INDEX IF NOT EXISTS idx_payments_updated_by ON payments(updated_by);
CREATE INDEX IF NOT EXISTS idx_payments_is_deleted ON payments(is_deleted);

-- ============================================================================
-- USER SUBSCRIPTIONS TABLE
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE user_subscriptions ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_user_subs_created_by ON user_subscriptions(created_by);
CREATE INDEX IF NOT EXISTS idx_user_subs_updated_by ON user_subscriptions(updated_by);
CREATE INDEX IF NOT EXISTS idx_user_subs_is_deleted ON user_subscriptions(is_deleted);

-- ============================================================================
-- USER PHOTOS TABLE
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE user_photos ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_user_photos_created_by ON user_photos(created_by);
CREATE INDEX IF NOT EXISTS idx_user_photos_is_deleted ON user_photos(is_deleted);

-- ============================================================================
-- SUPPORT TICKETS TABLE
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE support_tickets ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_support_tickets_created_by ON support_tickets(created_by);
CREATE INDEX IF NOT EXISTS idx_support_tickets_updated_by ON support_tickets(updated_by);

-- ============================================================================
-- REPORTS TABLE
ALTER TABLE reports ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE reports ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_reports_created_by ON reports(created_by);
CREATE INDEX IF NOT EXISTS idx_reports_updated_by ON reports(updated_by);
CREATE INDEX IF NOT EXISTS idx_reports_is_deleted ON reports(is_deleted);

-- ============================================================================
-- AUDIT LOG TABLE
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_audit_log_created_by ON audit_log(created_by);
CREATE INDEX IF NOT EXISTS idx_audit_log_is_deleted ON audit_log(is_deleted);

-- ============================================================================
-- CONVERSATIONS TABLE
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS deletion_reason VARCHAR(500) COMMENT 'Reason for deletion';
ALTER TABLE conversations ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_conversations_created_by ON conversations(created_by);
CREATE INDEX IF NOT EXISTS idx_conversations_is_deleted ON conversations(is_deleted);

-- ============================================================================
-- SWIPES TABLE
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS updated_at DATETIME;
ALTER TABLE swipes ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_swipes_created_by ON swipes(created_by);

-- ============================================================================
-- ADMIN AUDIT LOG TABLE
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE admin_audit_log ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_admin_audit_log_created_by ON admin_audit_log(created_by);

-- ============================================================================
-- PROFILE VISITOR TABLE
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS created_by BIGINT COMMENT 'User ID who created this record';
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS updated_by BIGINT COMMENT 'User ID who last updated this record';
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false COMMENT 'Soft delete flag';
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS deleted_at DATETIME COMMENT 'Timestamp when record was soft deleted';
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS deleted_by BIGINT COMMENT 'User ID who deleted this record';
ALTER TABLE profile_visitors ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version';

CREATE INDEX IF NOT EXISTS idx_profile_visitors_created_by ON profile_visitors(created_by);

-- ============================================================================
-- END OF MIGRATION
-- ============================================================================
-- Summary:
-- - Added complete audit trail fields to all core entities
-- - Added indexes on frequently queried audit columns
-- - Default values set appropriately (version=0, is_deleted=false)
-- - All columns are nullable to support existing data
-- - Soft delete flags are present for safe data removal
-- ============================================================================

