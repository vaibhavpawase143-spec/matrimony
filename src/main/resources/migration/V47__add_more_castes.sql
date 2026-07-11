-- Add more comprehensive castes data

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
-- Hindu Castes
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Brahmin', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kshatriya', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Vaishya', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Shudra', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kayastha', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Rajput', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Maratha', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Patel', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Gupta', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Agarwal', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Jat', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Yadav', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kurmi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Lodhi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Thakur', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Reddy', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Nair', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Pillai', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Gounder', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Vanniyar', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Thevar', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kamma', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kapu', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Velama', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Raju', TRUE, CURRENT_TIMESTAMP, NULL),

-- Muslim Castes
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Sunni', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Shia', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Bohra', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Memon', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Pathan', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Ansari', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Qureshi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Siddiqui', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Farooqi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Shaikh', TRUE, CURRENT_TIMESTAMP, NULL),

-- Christian Castes
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Catholic', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Protestant', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Orthodox', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Anglican', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Methodist', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Baptist', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Pentecostal', TRUE, CURRENT_TIMESTAMP, NULL),

-- Sikh Castes
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Jat', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Khatri', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Arora', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Saini', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Labana', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Sikh'), 'Kamboj', TRUE, CURRENT_TIMESTAMP, NULL),

-- Buddhist Castes
(NULL, (SELECT id FROM religions WHERE name='Buddhist'), 'Mahar', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Buddhist'), 'Neo Buddhist', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Buddhist'), 'Ambedkarite', TRUE, CURRENT_TIMESTAMP, NULL),

-- Jain Castes
(NULL, (SELECT id FROM religions WHERE name='Jain'), 'Shwetambar', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Jain'), 'Digambar', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Jain'), 'Sthanakvasi', TRUE, CURRENT_TIMESTAMP, NULL),

-- General
(NULL, (SELECT id FROM religions WHERE name='Other'), 'Other', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Other'), 'No Caste', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Other'), 'Prefer Not to Say', TRUE, CURRENT_TIMESTAMP, NULL)
ON CONFLICT (name, religion_id) DO NOTHING;
