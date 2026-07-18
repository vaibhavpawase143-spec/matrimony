-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DEFAULT FAQ DATA
-- =====================================================

INSERT INTO faqs (
    question,
    answer,
    display_order,
    published,
    is_active,
    created_by,
    updated_by,
    created_at,
    updated_at
)
SELECT
    f.question,
    f.answer,
    f.display_order,
    TRUE,
    TRUE,
    a.id,
    a.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
    (
        'How do I create my profile?',
        'Register using your email or mobile number, verify your account, and complete your profile by adding your personal, educational, family, and lifestyle details.',
        1
    ),
    (
        'How can I search for matches?',
        'Use the search and filter options to find suitable profiles based on age, religion, caste, education, occupation, location, and other preferences.',
        2
    ),
    (
        'How do I send an interest?',
        'Open a profile and click the "Send Interest" button. The other member can accept or reject your request.',
        3
    ),
    (
        'Can I upload multiple photos?',
        'Yes. You can upload multiple profile photos and choose one as your primary profile picture.',
        4
    ),
    (
        'How do Premium memberships work?',
        'Premium members receive additional features such as unlimited interests, enhanced profile visibility, and other premium benefits based on the selected plan.',
        5
    ),
    (
        'How can I contact support?',
        'Go to the Help & Support section and create a support ticket. Our team will respond as soon as possible.',
        6
    )
) AS f(question, answer, display_order)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM faqs faq
    WHERE LOWER(faq.question) = LOWER(f.question)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT * FROM faqs ORDER BY display_order;