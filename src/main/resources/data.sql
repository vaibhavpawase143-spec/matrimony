-- =====================================================
-- SYSTEM DATA
-- =====================================================
-- This section contains mandatory system records required
-- for application startup, authentication, and authorization.
--
-- NOTE:
-- 1. Execute this section before inserting master data.
-- 2. Records are idempotent and safe to re-run.
-- 3. Update the default Super Admin credentials before
--    deploying to production.
-- =====================================================


-- =====================================================
-- DEFAULT ROLES
-- =====================================================
-- These roles are required by Spring Security and should
-- never be removed from the application.
-- =====================================================



-- =====================================================
-- DEFAULT SUPER ADMIN
-- =====================================================
-- Default administrator account created during the initial
-- database setup.
--
-- IMPORTANT:
-- • Replace the password hash with a real BCrypt password.
-- • Update the email and phone number for production.
-- • This account is created only if it does not already exist.
-- =====================================================

-- =====================================================
-- LOCATION MASTER
-- =====================================================

-- -----------------------------------------------------
-- COUNTRIES
-- Master list of supported countries.
--
-- NOTES:
-- • Countries are system-managed master records.
-- • Created by the default Super Admin.
-- • Safe to execute multiple times.
-- -----------------------------------------------------

INSERT INTO countries (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    c.country_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('India')
) AS c(country_name)
CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a
WHERE NOT EXISTS (
    SELECT 1
    FROM countries ct
    WHERE LOWER(ct.name) = LOWER(c.country_name)
);

-- =====================================================
-- STATES
-- =====================================================
-- Master list of Indian States.
--
-- NOTES:
-- • States are system-managed master records.
-- • Created by the default Super Admin.
-- • Linked to Country: India.
-- • Safe to execute multiple times.
-- =====================================================

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

-- =====================================================
-- CITIES - MAHARASHTRA
-- =====================================================

INSERT INTO cities (
    admin_id,
    state_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    s.id,
    c.city_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Ahmednagar'),
        ('Akola'),
        ('Amravati'),
        ('Aurangabad'),
        ('Beed'),
        ('Bhandara'),
        ('Buldhana'),
        ('Chandrapur'),
        ('Dhule'),
        ('Gadchiroli'),
        ('Gondia'),
        ('Hingoli'),
        ('Jalgaon'),
        ('Jalna'),
        ('Kolhapur'),
        ('Latur'),
        ('Mumbai'),
        ('Mumbai Suburban'),
        ('Nagpur'),
        ('Nanded'),
        ('Nandurbar'),
        ('Nashik'),
        ('Osmanabad'),
        ('Palghar'),
        ('Parbhani'),
        ('Pune'),
        ('Raigad'),
        ('Ratnagiri'),
        ('Sangli'),
        ('Satara'),
        ('Sindhudurg'),
        ('Solapur'),
        ('Thane'),
        ('Wardha'),
        ('Washim'),
        ('Yavatmal')
) AS c(city_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM states
    WHERE LOWER(name)=LOWER('Maharashtra')
    LIMIT 1
) s

WHERE NOT EXISTS (
    SELECT 1
    FROM cities ct
    WHERE ct.state_id = s.id
      AND LOWER(ct.name)=LOWER(c.city_name)
);

-- =====================================================
-- CITIES - GUJARAT
-- =====================================================

INSERT INTO cities (
    admin_id,
    state_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    s.id,
    c.city_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Ahmedabad'),
        ('Amreli'),
        ('Anand'),
        ('Aravalli'),
        ('Banaskantha'),
        ('Bharuch'),
        ('Bhavnagar'),
        ('Botad'),
        ('Chhota Udaipur'),
        ('Dahod'),
        ('Dang'),
        ('Devbhumi Dwarka'),
        ('Gandhinagar'),
        ('Gir Somnath'),
        ('Jamnagar'),
        ('Junagadh'),
        ('Kheda'),
        ('Kutch'),
        ('Mahisagar'),
        ('Mehsana'),
        ('Morbi'),
        ('Narmada'),
        ('Navsari'),
        ('Panchmahal'),
        ('Patan'),
        ('Porbandar'),
        ('Rajkot'),
        ('Sabarkantha'),
        ('Surat'),
        ('Surendranagar'),
        ('Tapi'),
        ('Vadodara'),
        ('Valsad')
) AS c(city_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM states
    WHERE LOWER(name)=LOWER('Gujarat')
    LIMIT 1
) s

WHERE NOT EXISTS (
    SELECT 1
    FROM cities ct
    WHERE ct.state_id = s.id
      AND LOWER(ct.name)=LOWER(c.city_name)
);

-- =====================================================
-- RELIGIONS
-- =====================================================
-- Master list of religions.
--
-- NOTES:
-- • System-managed master records.
-- • Created by the default Super Admin.
-- • Safe to execute multiple times.
-- =====================================================

INSERT INTO religions (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    r.religion_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Hindu'),
        ('Muslim'),
        ('Christian'),
        ('Sikh'),
        ('Jain'),
        ('Buddhist'),
        ('Parsi'),
        ('Jewish'),
        ('Bahá''í'),
        ('Tribal'),
        ('Other')
) AS r(religion_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM religions rel
    WHERE rel.admin_id = a.id
      AND LOWER(rel.name) = LOWER(r.religion_name)
);