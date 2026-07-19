-- =====================================================
-- GATHBANDHAN MATRIMONY
-- CASTE MASTER DATA
-- PART 1 - HINDU CASTES (A–D)
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Agamudayar'),
        ('Agarwal'),
        ('Ahir'),
        ('Arora'),
        ('Arya Vysya'),
        ('Audichya Brahmin'),
        ('Bairagi'),
        ('Baniya'),
        ('Barai'),
        ('Baria'),
        ('Bari'),
        ('Beldar'),
        ('Besta'),
        ('Bhambi'),
        ('Bhandari'),
        ('Bhar'),
        ('Bharwad'),
        ('Bhatt'),
        ('Bhavsar'),
        ('Bhil'),
        ('Bhoi'),
        ('Bhumihar'),
        ('Billava'),
        ('Bishnoi'),
        ('Brahmin'),
        ('Bunt'),
        ('Chambhar'),
        ('Chandravanshi'),
        ('Charan'),
        ('Chaudhary'),
        ('Chaurasia'),
        ('Chhetri'),
        ('Chhipa'),
        ('Darji'),
        ('Deshastha Brahmin'),
        ('Devadiga'),
        ('Devanga'),
        ('Dewangan'),
        ('Dhakad'),
        ('Dhangar'),
        ('Dhimar'),
        ('Dhobi'),
        ('Dhor'),
        ('Dubey')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Hindu')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM castes WHERE religion_id = (
--     SELECT id FROM religions WHERE name = 'Hindu'
-- );
-- =====================================================
-- PART 2 - HINDU CASTES (E–M)
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Ediga'),
        ('Gabit'),
        ('Gadaria'),
        ('Gandla'),
        ('Ganiga'),
        ('Gaud'),
        ('Gavali'),
        ('Gawli'),
        ('Gond'),
        ('Goswami'),
        ('Goud'),
        ('Gowda'),
        ('Gujar'),
        ('Gurav'),
        ('Halba'),
        ('Hatkar'),
        ('Helava'),
        ('Jadhav'),
        ('Jangam'),
        ('Jat'),
        ('Jatav'),
        ('Kadava Patidar'),
        ('Kahar'),
        ('Kaikadi'),
        ('Kakkalan'),
        ('Kalar'),
        ('Kalwar'),
        ('Kamboj'),
        ('Kamma'),
        ('Kanyakubj Brahmin'),
        ('Kapu'),
        ('Karhade Brahmin'),
        ('Karmakar'),
        ('Kasar'),
        ('Kashyap'),
        ('Khatik'),
        ('Khati'),
        ('Khatri'),
        ('Koli'),
        ('Konkani Brahmin'),
        ('Kori'),
        ('Koshti'),
        ('Kshatriya'),
        ('Kudmi'),
        ('Kulalar'),
        ('Kulmi'),
        ('Kumbhar'),
        ('Kunbi'),
        ('Kurmi'),
        ('Kuruba'),
        ('Kuruhina Shetty'),
        ('Kushwaha'),
        ('Leva Patil'),
        ('Leuva Patel'),
        ('Lingayat'),
        ('Lodh'),
        ('Lohana'),
        ('Lohar'),
        ('Madiga'),
        ('Mahajan'),
        ('Mahar'),
        ('Maheshwari'),
        ('Mali'),
        ('Mallah'),
        ('Marar'),
        ('Maratha'),
        ('Mathur'),
        ('Maurya'),
        ('Meena'),
        ('Mochi'),
        ('Mudaliar'),
        ('Mudiraj'),
        ('Mukkuvar')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Hindu')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 3 - HINDU CASTES (N–Z)
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Nadar'),
        ('Nai'),
        ('Naicker'),
        ('Naidu'),
        ('Nair'),
        ('Namdev'),
        ('Napit'),
        ('Nayak'),
        ('Oswal'),
        ('Padmashali'),
        ('Pallan'),
        ('Panchal'),
        ('Pandaram'),
        ('Pandit'),
        ('Patel'),
        ('Patidar'),
        ('Patil'),
        ('Perika'),
        ('Pillai'),
        ('Prajapati'),
        ('Rajaka'),
        ('Rajbhar'),
        ('Rajput'),
        ('Ramoshi'),
        ('Rastogi'),
        ('Reddy'),
        ('Sahu'),
        ('Saini'),
        ('Sakaldwipi Brahmin'),
        ('Saliya'),
        ('Saraswat Brahmin'),
        ('Saryuparin Brahmin'),
        ('Scheduled Caste'),
        ('Scheduled Tribe'),
        ('Sen'),
        ('Shimpi'),
        ('Sonar'),
        ('Soni'),
        ('Sourashtra'),
        ('Sutar'),
        ('Swarnakar'),
        ('Tambat'),
        ('Tanti'),
        ('Telaga'),
        ('Teli'),
        ('Thakur'),
        ('Thevar'),
        ('Togata'),
        ('Tyagi'),
        ('Udayar'),
        ('Uppara'),
        ('Vaddera'),
        ('Vaish'),
        ('Vaishnav'),
        ('Vaishya'),
        ('Valmiki'),
        ('Vaniya'),
        ('Vannar'),
        ('Vanniyar'),
        ('Velama'),
        ('Viswakarma'),
        ('Vokkaliga'),
        ('Vysya'),
        ('Yadav'),
        ('Yogi')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Hindu')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 4 - MUSLIM COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Ansari'),
        ('Arain'),
        ('Attar'),
        ('Bagban'),
        ('Baig'),
        ('Banjara Muslim'),
        ('Barelvi'),
        ('Bohra'),
        ('Chhipi'),
        ('Dudekula'),
        ('Fakir'),
        ('Hanafi'),
        ('Hashmi'),
        ('Khoja'),
        ('Malik'),
        ('Mansoori'),
        ('Mappila'),
        ('Memon'),
        ('Mir'),
        ('Mughal'),
        ('Mulla'),
        ('Musalman'),
        ('Nadaf'),
        ('Pathan'),
        ('Pinjara'),
        ('Qureshi'),
        ('Rayeen'),
        ('Salmani'),
        ('Sayyid'),
        ('Shaikh'),
        ('Shia'),
        ('Siddiqui'),
        ('Sunni'),
        ('Syed'),
        ('Tamboli'),
        ('Turk')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Muslim')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 5 - CHRISTIAN COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Anglo Indian'),
        ('Born Again Christian'),
        ('Catholic'),
        ('Church of North India'),
        ('Church of South India'),
        ('Evangelical'),
        ('Jacobite'),
        ('Knanaya Catholic'),
        ('Knanaya Jacobite'),
        ('Latin Catholic'),
        ('Malankara Catholic'),
        ('Malankara Orthodox'),
        ('Mangalorean Catholic'),
        ('Marthoma'),
        ('Orthodox'),
        ('Pentecostal'),
        ('Protestant'),
        ('Roman Catholic'),
        ('Seventh-day Adventist'),
        ('Syrian Catholic'),
        ('Syrian Christian'),
        ('Syrian Orthodox')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Christian')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 6 - SIKH COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Ahluwalia'),
        ('Arora Sikh'),
        ('Bhatra'),
        ('Ghumar'),
        ('Jat Sikh'),
        ('Kamboj Sikh'),
        ('Khatri Sikh'),
        ('Labana'),
        ('Lubana'),
        ('Mazhabi Sikh'),
        ('Nanakpanthi'),
        ('Ramdasia'),
        ('Rajput Sikh'),
        ('Saini Sikh'),
        ('Tonk Kshatriya'),
        ('Clean Shaven'),
        ('Amritdhari'),
        ('Kesadhari'),
        ('Rai Sikh'),
        ('Sikh')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Sikh')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 7 - JAIN COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Agarwal Jain'),
        ('Bagherwal'),
        ('Chaturtha'),
        ('Digambar'),
        ('Humad'),
        ('Jain'),
        ('Khandelwal Jain'),
        ('Oswal'),
        ('Parwar'),
        ('Porwal'),
        ('Shwetambar'),
        ('Shrimal'),
        ('Sthanakvasi'),
        ('Terapanthi')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Jain')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 8 - BUDDHIST COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Buddhist'),
        ('Neo Buddhist'),
        ('Navayana'),
        ('Theravada'),
        ('Mahayana'),
        ('Vajrayana'),
        ('Ambedkarite Buddhist'),
        ('Bauddha'),
        ('Bhikkhu'),
        ('Sakya'),
        ('Ladakhi Buddhist'),
        ('Tibetan Buddhist')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Buddhist')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);
-- =====================================================
-- PART 9 - PARSI, JEWISH, TRIBAL & OTHER COMMUNITIES
-- =====================================================

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Irani'),
        ('Parsi'),
        ('Zoroastrian')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Parsi')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Bene Israel'),
        ('Cochin Jewish'),
        ('Baghdadi Jewish'),
        ('Jewish')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Jewish')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Bhil'),
        ('Gond'),
        ('Munda'),
        ('Oraon'),
        ('Santhal'),
        ('Khasi'),
        ('Naga'),
        ('Tribal')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Tribal')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    r.id,
    c.caste_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Inter Religion'),
        ('No Caste'),
        ('Prefer Not To Say'),
        ('Other')
) AS c(caste_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

CROSS JOIN (
    SELECT id
    FROM religions
    WHERE LOWER(name) = LOWER('Other')
    LIMIT 1
) r

WHERE NOT EXISTS (
    SELECT 1
    FROM castes ct
    WHERE ct.admin_id = a.id
      AND ct.religion_id = r.id
      AND LOWER(ct.name) = LOWER(c.caste_name)
);