-- ===========================================================
-- Create Permissions Table
-- ===========================================================

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) NOT NULL,

    description VARCHAR(255),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMP,
    deleted_by BIGINT,

    CONSTRAINT uk_permission_name UNIQUE (name),
    CONSTRAINT uk_permission_code UNIQUE (code)
);

-- ===========================================================
-- Indexes
-- ===========================================================

CREATE INDEX idx_permission_code
    ON permissions(code);

CREATE INDEX idx_permission_active
    ON permissions(is_active);

CREATE INDEX idx_permission_deleted
    ON permissions(deleted_at);

-- ===========================================================
-- Create Role Permissions Mapping Table
-- ===========================================================

CREATE TABLE role_permissions (

    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY(role_id, permission_id),

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY(permission_id)
        REFERENCES permissions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_role_permissions_role
    ON role_permissions(role_id);

CREATE INDEX idx_role_permissions_permission
    ON role_permissions(permission_id);

-- ===========================================================
-- Insert Default Permissions
-- ===========================================================

INSERT INTO permissions(name, code, description) VALUES
('View Users','USER_VIEW','View users'),
('Create Users','USER_CREATE','Create users'),
('Edit Users','USER_EDIT','Edit users'),
('Delete Users','USER_DELETE','Delete users'),
('Block Users','USER_BLOCK','Block or unblock users'),
('Verify Users','USER_VERIFY','Verify email or phone'),

('View Admins','ADMIN_VIEW','View admins'),
('Create Admin','ADMIN_CREATE','Create admin'),
('Edit Admin','ADMIN_EDIT','Edit admin'),
('Delete Admin','ADMIN_DELETE','Delete admin'),
('Change Admin Role','ADMIN_ROLE_CHANGE','Change admin role'),

('Manage CMS','CMS_MANAGE','Manage CMS pages'),
('Manage FAQ','FAQ_MANAGE','Manage FAQs'),

('View Reports','REPORT_VIEW','View reports'),
('Export Reports','REPORT_EXPORT','Export reports'),

('View Subscription','SUBSCRIPTION_VIEW','View subscriptions'),
('Manage Subscription','SUBSCRIPTION_MANAGE','Manage subscriptions'),

('View Support','SUPPORT_VIEW','View support tickets'),
('Reply Support','SUPPORT_REPLY','Reply support tickets'),
('Close Support','SUPPORT_CLOSE','Close support tickets'),

('View Audit Log','AUDIT_VIEW','View audit logs');

-- ===========================================================
-- SUPER_ADMIN gets ALL permissions
-- ===========================================================

INSERT INTO role_permissions(role_id, permission_id)
SELECT
    r.id,
    p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'SUPER_ADMIN';

-- ===========================================================
-- ADMIN gets limited permissions
-- ===========================================================

INSERT INTO role_permissions(role_id, permission_id)
SELECT
    r.id,
    p.id
FROM roles r
JOIN permissions p
ON p.code IN (

    'USER_VIEW',
    'USER_EDIT',
    'USER_BLOCK',
    'USER_VERIFY',

    'REPORT_VIEW',
    'REPORT_EXPORT',

    'SUBSCRIPTION_VIEW',

    'SUPPORT_VIEW',
    'SUPPORT_REPLY',

    'FAQ_MANAGE',

    'AUDIT_VIEW'
)
WHERE r.name = 'ADMIN';