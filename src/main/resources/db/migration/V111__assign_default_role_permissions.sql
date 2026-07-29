-- ===========================================================
-- ROLE_SUPER_ADMIN gets ALL permissions
-- ===========================================================

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ROLE_SUPER_ADMIN'
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);

-- ===========================================================
-- ROLE_ADMIN gets limited permissions
-- ===========================================================

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
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
WHERE r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);