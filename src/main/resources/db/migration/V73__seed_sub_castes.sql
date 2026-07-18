-- =====================================================
-- GATHBANDHAN MATRIMONY
-- SUB CASTE MASTER DATA
-- PART 1 - BRAHMIN
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Chitpavan'),
        ('Deshastha'),
        ('Karhade'),
        ('Saraswat'),
        ('Konkanastha'),
        ('Kanyakubj'),
        ('Maithil'),
        ('Gaur'),
        ('Audichya'),
        ('Namboodiri'),
        ('Iyer'),
        ('Iyengar'),
        ('Havyaka'),
        ('Smartha'),
        ('Madhwa'),
        ('Vaidiki'),
        ('Rigvedi'),
        ('Yajurvedi'),
        ('Dravida'),
        ('Sanadhya'),
        ('Pushkarna'),
        ('Sakaldwipi'),
        ('Tyagi Brahmin'),
        ('Bhatt'),
        ('Joshi'),
        ('Pandit'),
        ('Purohit'),
        ('Dixit'),
        ('Shukla'),
        ('Tiwari'),
        ('Tripathi'),
        ('Chaturvedi'),
        ('Dwivedi'),
        ('Upadhyay'),
        ('Pathak'),
        ('Vyas'),
        ('Acharya'),
        ('Sharma')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Brahmin')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT * FROM sub_castes
-- WHERE caste_id = (SELECT id FROM castes WHERE name='Brahmin');

-- =====================================================
-- GATHBANDHAN MATRIMONY
-- SUB CASTE MASTER DATA
-- PART 1 - BRAHMIN
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Chitpavan'),
        ('Deshastha'),
        ('Karhade'),
        ('Saraswat'),
        ('Konkanastha'),
        ('Kanyakubj'),
        ('Maithil'),
        ('Gaur'),
        ('Audichya'),
        ('Namboodiri'),
        ('Iyer'),
        ('Iyengar'),
        ('Havyaka'),
        ('Smartha'),
        ('Madhwa'),
        ('Vaidiki'),
        ('Rigvedi'),
        ('Yajurvedi'),
        ('Dravida'),
        ('Sanadhya'),
        ('Pushkarna'),
        ('Sakaldwipi'),
        ('Tyagi Brahmin'),
        ('Bhatt'),
        ('Joshi'),
        ('Pandit'),
        ('Purohit'),
        ('Dixit'),
        ('Shukla'),
        ('Tiwari'),
        ('Tripathi'),
        ('Chaturvedi'),
        ('Dwivedi'),
        ('Upadhyay'),
        ('Pathak'),
        ('Vyas'),
        ('Acharya'),
        ('Sharma')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Brahmin')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT * FROM sub_castes
-- WHERE caste_id = (SELECT id FROM castes WHERE name='Brahmin');

-- =====================================================
-- PART 3 - RAJPUT
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Sisodia'),
        ('Rathore'),
        ('Chauhan'),
        ('Parmar'),
        ('Solanki'),
        ('Tomar'),
        ('Kachwaha'),
        ('Bhati'),
        ('Bundela'),
        ('Chandel'),
        ('Gaharwar'),
        ('Jadeja'),
        ('Panwar'),
        ('Sengar'),
        ('Bais'),
        ('Baghel'),
        ('Nikumbh'),
        ('Gaur Rajput'),
        ('Jhala'),
        ('Tanwar')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Rajput')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 4 - YADAV
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Ahir'),
        ('Ghosi'),
        ('Nandvanshi'),
        ('Krishnavanshi'),
        ('Yaduvanshi'),
        ('Goala'),
        ('Konar'),
        ('Idaiyar'),
        ('Golla'),
        ('Govala'),
        ('Gwala'),
        ('Yadava'),
        ('Dudekula Yadav'),
        ('Nanda Gopa'),
        ('Abhira')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Yadav')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 5 - JAT
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Ahlawat'),
        ('Balyan'),
        ('Beniwal'),
        ('Dahiya'),
        ('Deswal'),
        ('Godara'),
        ('Jakhar'),
        ('Malik'),
        ('Punia'),
        ('Saharan'),
        ('Sangwan'),
        ('Sheoran'),
        ('Sihag'),
        ('Tomar Jat'),
        ('Rana Jat'),
        ('Rathi'),
        ('Nehra'),
        ('Kadian'),
        ('Gathwala'),
        ('Jat Sikh')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Jat')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 6 - PATEL / PATIDAR
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Kadva Patel'),
        ('Leuva Patel'),
        ('Kadva Patidar'),
        ('Leuva Patidar'),
        ('Charotar Patel'),
        ('Anjana Patel'),
        ('Desai Patel'),
        ('Patel'),
        ('Patidar'),
        ('Matiya Patel'),
        ('Chaudhary Patel'),
        ('Kanbi Patel'),
        ('Saurashtra Patel'),
        ('North Gujarat Patel'),
        ('South Gujarat Patel')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) IN (LOWER('Patel'), LOWER('Patidar'))
    ORDER BY name
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 7 - KUNBI
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Leva Kunbi'),
        ('Tirole Kunbi'),
        ('Dhonoje Kunbi'),
        ('Jhade Kunbi'),
        ('Khaire Kunbi'),
        ('Mana Kunbi'),
        ('Maratha Kunbi'),
        ('Charodi Kunbi'),
        ('Nagpuri Kunbi'),
        ('Desh Kunbi'),
        ('Konkan Kunbi'),
        ('Kunbi Patil'),
        ('Kunbi Maratha'),
        ('Halba Kunbi'),
        ('Zade Kunbi')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Kunbi')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 8 - KOLI
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Mahadev Koli'),
        ('Malhar Koli'),
        ('Dongar Koli'),
        ('Suryavanshi Koli'),
        ('Son Koli'),
        ('Mangela Koli'),
        ('Talpada Koli'),
        ('Chunvalia Koli'),
        ('Tokre Koli'),
        ('Dhor Koli'),
        ('Koli Patel'),
        ('Koli Mahadev'),
        ('Koli Thakor'),
        ('Kharwa Koli'),
        ('Koli Fisherman')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Koli')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 9 - LINGAYAT
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Banajiga'),
        ('Panchamasali'),
        ('Ganiga'),
        ('Jangama'),
        ('Nonaba'),
        ('Reddy Lingayat'),
        ('Sadara'),
        ('Sadar Lingayat'),
        ('Veerashaiva'),
        ('Aradhya'),
        ('Hatgar'),
        ('Gowda Lingayat'),
        ('Lingayat Banajiga'),
        ('Lingayat Panchamasali'),
        ('Lingayat Ganiga')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Lingayat')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 10 - REDDY
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Panta Reddy'),
        ('Kapu Reddy'),
        ('Motati Reddy'),
        ('Pakanati Reddy'),
        ('Desuri Reddy'),
        ('Kondaveeti Reddy'),
        ('Pedakanti Reddy'),
        ('Oruganti Reddy'),
        ('Renati Reddy'),
        ('Bagata Reddy'),
        ('Kamma Reddy'),
        ('Yellapu Reddy'),
        ('Perika Reddy'),
        ('Reddy Dora'),
        ('Telangana Reddy')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Reddy')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
-- =====================================================
-- PART 11 - KAMMA
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    c.id,
    sc.sub_caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Chowdary'),
        ('Illuvellani'),
        ('Kona Kamma'),
        ('Pedda Kamma'),
        ('Chinna Kamma'),
        ('Bangaru Kamma'),
        ('Gampa Kamma'),
        ('Godachatu Kamma'),
        ('Kavali Kamma'),
        ('Gandikota Kamma'),
        ('Nandendla Kamma'),
        ('Krishna Kamma'),
        ('Guntur Kamma'),
        ('Prakasam Kamma'),
        ('Rayalaseema Kamma')
) AS sc(sub_caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM castes
    WHERE LOWER(name) = LOWER('Kamma')
    LIMIT 1
) c

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes s
    WHERE s.caste_id = c.id
      AND LOWER(s.name) = LOWER(sc.sub_caste_name)
);
