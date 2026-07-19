-- =====================================================
-- GATHBANDHAN MATRIMONY
-- ROLE MASTER DATA
-- =====================================================

-- -----------------------------------------------------
-- DEFAULT SYSTEM ROLES
-- -----------------------------------------------------

INSERT INTO roles (
    name,
    is_active,
    created_at
)
SELECT
    'ROLE_SUPER_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'ROLE_SUPER_ADMIN'
);

INSERT INTO roles (
    name,
    is_active,
    created_at
)
SELECT
    'ROLE_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'ROLE_ADMIN'
);

INSERT INTO roles (
    name,
    is_active,
    created_at
)
SELECT
    'ROLE_USER',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'ROLE_USER'
);