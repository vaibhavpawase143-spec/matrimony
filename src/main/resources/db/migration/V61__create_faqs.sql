-- =====================================================
-- V60__create_faqs.sql
-- FAQs Table
-- =====================================================

CREATE TABLE faqs (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- FAQ
    -- =====================================================
    question VARCHAR(500) NOT NULL,

    answer TEXT NOT NULL,

    display_order INTEGER NOT NULL,

    published BOOLEAN NOT NULL DEFAULT TRUE,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_by BIGINT,

    updated_by BIGINT,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_faq_created_by
        FOREIGN KEY (created_by)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_faq_updated_by
        FOREIGN KEY (updated_by)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_faq_display_order
        CHECK (display_order >= 0)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_faq_created_by
    ON faqs(created_by);

CREATE INDEX idx_faq_updated_by
    ON faqs(updated_by);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_faq_display_order
    ON faqs(display_order);

CREATE INDEX idx_faq_published
    ON faqs(published);

CREATE INDEX idx_faq_active
    ON faqs(is_active);

CREATE INDEX idx_faq_created_at
    ON faqs(created_at);

CREATE INDEX idx_faq_updated_at
    ON faqs(updated_at);

CREATE INDEX idx_faq_published_active
    ON faqs(published, is_active);

CREATE INDEX idx_faq_display_published
    ON faqs(display_order, published);

CREATE INDEX idx_faq_display_active
    ON faqs(display_order, is_active);