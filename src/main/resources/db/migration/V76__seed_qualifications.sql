-- =====================================================
-- GATHBANDHAN MATRIMONY
-- QUALIFICATION MASTER DATA
-- =====================================================

INSERT INTO qualifications (
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
    q.qualification_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES

    -- School Education
    ('SSC'),
    ('HSC'),

    -- Diploma & ITI
    ('ITI'),
    ('Diploma'),
    ('Polytechnic'),

    -- Undergraduate
    ('BA'),
    ('B.Com'),
    ('B.Sc'),
    ('BCA'),
    ('BBA'),
    ('B.Tech'),
    ('BE'),
    ('B.Arch'),
    ('B.Des'),
    ('B.Pharm'),
    ('B.Ed'),
    ('B.Lib'),
    ('BSW'),
    ('BHM'),
    ('BMS'),
    ('BFA'),
    ('BPT'),
    ('BDS'),
    ('BHMS'),
    ('BAMS'),
    ('BUMS'),
    ('BNYS'),
    ('MBBS'),
    ('LLB'),
    ('B.V.Sc'),
    ('B.Sc Nursing'),
    ('B.Plan'),

    -- Postgraduate
    ('MA'),
    ('M.Com'),
    ('M.Sc'),
    ('MCA'),
    ('MBA'),
    ('ME'),
    ('M.Tech'),
    ('M.Pharm'),
    ('M.Ed'),
    ('M.Lib'),
    ('MSW'),
    ('MHM'),
    ('MFA'),
    ('MPT'),
    ('MD'),
    ('MS'),
    ('MDS'),
    ('LLM'),
    ('M.Arch'),
    ('M.Des'),
    ('M.Plan'),
    ('M.Sc Nursing'),

    -- Doctorate
    ('M.Phil'),
    ('Ph.D'),
    ('D.Litt'),

    -- Professional Qualifications
    ('CA'),
    ('CS'),
    ('CMA'),
    ('CFA'),
    ('CPA'),
    ('ICWA'),

    -- Government & Competitive
    ('IAS'),
    ('IPS'),
    ('IFS'),
    ('IRS'),

    -- Technical Certifications
    ('AWS Certified'),
    ('Microsoft Certified'),
    ('Oracle Certified'),
    ('Cisco Certified'),
    ('Google Certified'),
    ('Red Hat Certified'),

    -- Vocational
    ('Hotel Management'),
    ('Fashion Designing'),
    ('Interior Designing'),
    ('Animation'),
    ('Graphic Designing'),
    ('Journalism'),
    ('Mass Communication'),
    ('Fine Arts'),
    ('Performing Arts'),
    ('Photography'),
    ('Film Making'),

    -- Agriculture
    ('Agriculture'),
    ('Forestry'),
    ('Horticulture'),
    ('Dairy Technology'),

    -- Aviation & Marine
    ('Commercial Pilot'),
    ('Cabin Crew'),
    ('Marine Engineering'),
    ('Nautical Science'),

    -- Medical Allied
    ('Physiotherapy'),
    ('Occupational Therapy'),
    ('Pharmacy'),
    ('Lab Technician'),
    ('Radiology'),
    ('Dialysis Technology'),

    -- Others
    ('Other')

) AS q(qualification_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username='superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM qualifications qq
    WHERE qq.admin_id = a.id
      AND LOWER(qq.name)=LOWER(q.qualification_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM qualifications;
-- SELECT * FROM qualifications ORDER BY name;