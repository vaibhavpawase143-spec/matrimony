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
    'SUPER_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'SUPER_ADMIN'
);

INSERT INTO roles (
    name,
    is_active,
    created_at
)
SELECT
    'ADMIN',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'ADMIN'
);

INSERT INTO roles (
    name,
    is_active,
    created_at
)
SELECT
    'USER',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE name = 'USER'
);