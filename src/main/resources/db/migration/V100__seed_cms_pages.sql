-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DEFAULT CMS PAGES
-- =====================================================

INSERT INTO cms_pages (
    page_key,
    title,
    content,
    published,
    is_active,
    created_by,
    updated_by,
    created_at,
    updated_at
)
SELECT
    p.page_key,
    p.title,
    p.content,
    TRUE,
    TRUE,
    a.id,
    a.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
    (
        'ABOUT_US',
        'About Us',
        '<h2>About Gathbandhan Matrimony</h2><p>Welcome to Gathbandhan Matrimony. Our mission is to help individuals and families find meaningful life partners through a secure, trusted, and user-friendly platform.</p>'
    ),
    (
        'PRIVACY_POLICY',
        'Privacy Policy',
        '<h2>Privacy Policy</h2><p>We respect your privacy. Your personal information is securely stored and used only to provide matrimonial services in accordance with our privacy practices.</p>'
    ),
    (
        'TERMS_AND_CONDITIONS',
        'Terms & Conditions',
        '<h2>Terms & Conditions</h2><p>By using Gathbandhan Matrimony, you agree to comply with our terms of service, community guidelines, and applicable laws.</p>'
    ),
    (
        'CONTACT_US',
        'Contact Us',
        '<h2>Contact Us</h2><p>If you need assistance, please contact our support team through the Help & Support section or email us at support@gathbandhan.com.</p>'
    ),
    (
        'REFUND_POLICY',
        'Refund Policy',
        '<h2>Refund Policy</h2><p>Refunds are processed according to the subscription plan and applicable refund policy. Please contact support for assistance.</p>'
    ),
    (
        'COOKIE_POLICY',
        'Cookie Policy',
        '<h2>Cookie Policy</h2><p>We use cookies to improve your browsing experience, remember preferences, and enhance website performance.</p>'
    )
) AS p(page_key, title, content)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM cms_pages cp
    WHERE LOWER(cp.page_key) = LOWER(p.page_key)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT page_key, title, published
-- FROM cms_pages
-- ORDER BY page_key;