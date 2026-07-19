-- =====================================================
-- GATHBANDHAN MATRIMONY
-- STATE MASTER DATA
-- =====================================================

-- -----------------------------------------------------
-- DEFAULT STATES & UNION TERRITORIES - INDIA
-- -----------------------------------------------------

INSERT INTO states (
    admin_id,
    country_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    c.id,
    s.state_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Andhra Pradesh'),
        ('Arunachal Pradesh'),
        ('Assam'),
        ('Bihar'),
        ('Chhattisgarh'),
        ('Goa'),
        ('Gujarat'),
        ('Haryana'),
        ('Himachal Pradesh'),
        ('Jharkhand'),
        ('Karnataka'),
        ('Kerala'),
        ('Madhya Pradesh'),
        ('Maharashtra'),
        ('Manipur'),
        ('Meghalaya'),
        ('Mizoram'),
        ('Nagaland'),
        ('Odisha'),
        ('Punjab'),
        ('Rajasthan'),
        ('Sikkim'),
        ('Tamil Nadu'),
        ('Telangana'),
        ('Tripura'),
        ('Uttar Pradesh'),
        ('Uttarakhand'),
        ('West Bengal'),
        ('Andaman and Nicobar Islands'),
        ('Chandigarh'),
        ('Dadra and Nagar Haveli and Daman and Diu'),
        ('Delhi'),
        ('Jammu and Kashmir'),
        ('Ladakh'),
        ('Lakshadweep'),
        ('Puducherry')
) AS s(state_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM countries
    WHERE LOWER(name) = LOWER('India')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM states st
    WHERE st.country_id = c.id
      AND LOWER(st.name) = LOWER(s.state_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM states;