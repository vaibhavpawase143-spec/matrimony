-- =====================================================
-- GATHBANDHAN MATRIMONY
-- CITY MASTER DATA
-- =====================================================

-- -----------------------------------------------------
-- DEFAULT CITIES
-- -----------------------------------------------------

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
        -- Maharashtra
        ('Maharashtra', 'Mumbai'),
        ('Maharashtra', 'Pune'),
        ('Maharashtra', 'Nagpur'),
        ('Maharashtra', 'Nashik'),
        ('Maharashtra', 'Aurangabad'),
        ('Maharashtra', 'Thane'),
        ('Maharashtra', 'Kolhapur'),
        ('Maharashtra', 'Solapur'),
        ('Maharashtra', 'Satara'),
        ('Maharashtra', 'Sangli'),
        ('Maharashtra', 'Ratnagiri'),
        ('Maharashtra', 'Sindhudurg'),
        ('Maharashtra', 'Ahmednagar'),
        ('Maharashtra', 'Jalgaon'),
        ('Maharashtra', 'Dhule'),
        ('Maharashtra', 'Nandurbar'),
        ('Maharashtra', 'Beed'),
        ('Maharashtra', 'Latur'),
        ('Maharashtra', 'Osmanabad'),
        ('Maharashtra', 'Parbhani'),
        ('Maharashtra', 'Hingoli'),
        ('Maharashtra', 'Nanded'),
        ('Maharashtra', 'Akola'),
        ('Maharashtra', 'Amravati'),
        ('Maharashtra', 'Buldhana'),
        ('Maharashtra', 'Washim'),
        ('Maharashtra', 'Yavatmal'),
        ('Maharashtra', 'Wardha'),
        ('Maharashtra', 'Chandrapur'),
        ('Maharashtra', 'Gadchiroli'),
        ('Maharashtra', 'Raigad'),
        ('Maharashtra', 'Palghar'),

        -- Gujarat
        ('Gujarat', 'Ahmedabad'),
        ('Gujarat', 'Surat'),
        ('Gujarat', 'Vadodara'),
        ('Gujarat', 'Rajkot'),
        ('Gujarat', 'Bhavnagar'),
        ('Gujarat', 'Jamnagar'),
        ('Gujarat', 'Junagadh'),
        ('Gujarat', 'Gandhinagar'),
        ('Gujarat', 'Anand'),
        ('Gujarat', 'Navsari'),
        ('Gujarat', 'Valsad'),
        ('Gujarat', 'Mehsana'),
        ('Gujarat', 'Patan'),
        ('Gujarat', 'Banaskantha'),
        ('Gujarat', 'Sabarkantha'),
        ('Gujarat', 'Kutch'),
        ('Gujarat', 'Porbandar'),
        ('Gujarat', 'Amreli'),
        ('Gujarat', 'Surendranagar'),
        ('Gujarat', 'Botad'),
        ('Gujarat', 'Morbi'),
        ('Gujarat', 'Dahod'),
        ('Gujarat', 'Panchmahal'),
        ('Gujarat', 'Bharuch'),
        ('Gujarat', 'Narmada'),
        ('Gujarat', 'Tapi'),
        ('Gujarat', 'Dang')
) AS c(state_name, city_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

JOIN states s
ON LOWER(s.name) = LOWER(c.state_name)

WHERE NOT EXISTS (
    SELECT 1
    FROM cities ct
    WHERE ct.state_id = s.id
      AND LOWER(ct.name) = LOWER(c.city_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM cities;