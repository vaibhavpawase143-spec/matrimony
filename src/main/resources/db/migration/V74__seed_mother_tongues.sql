-- =====================================================
-- GATHBANDHAN MATRIMONY
-- MOTHER TONGUE MASTER DATA
-- =====================================================

INSERT INTO mother_tongues (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    mt.language_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Assamese'),
        ('Awadhi'),
        ('Bengali'),
        ('Bhojpuri'),
        ('Bodo'),
        ('Chhattisgarhi'),
        ('Dogri'),
        ('English'),
        ('Garhwali'),
        ('Gujarati'),
        ('Haryanvi'),
        ('Hindi'),
        ('Kannada'),
        ('Kashmiri'),
        ('Konkani'),
        ('Kumaoni'),
        ('Maithili'),
        ('Malayalam'),
        ('Marathi'),
        ('Marwari'),
        ('Mizo'),
        ('Nepali'),
        ('Odia'),
        ('Punjabi'),
        ('Rajasthani'),
        ('Sanskrit'),
        ('Santhali'),
        ('Sindhi'),
        ('Tamil'),
        ('Telugu'),
        ('Tulu'),
        ('Urdu'),
        ('Other')
) AS mt(language_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM mother_tongues m
    WHERE m.admin_id = a.id
      AND LOWER(m.name) = LOWER(mt.language_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM mother_tongues;
-- SELECT * FROM mother_tongues ORDER BY name;