-- =====================================================
-- V61__create_cms_pages.sql
-- CMS Pages Table
-- =====================================================

CREATE TABLE cms_pages (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- PAGE DETAILS
    -- =====================================================
    page_key VARCHAR(100) NOT NULL,

    title VARCHAR(255) NOT NULL,

    content TEXT NOT NULL,

    published BOOLEAN NOT NULL DEFAULT FALSE,

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

    CONSTRAINT uk_cms_page_key
        UNIQUE (page_key),

    CONSTRAINT fk_cms_created_by
        FOREIGN KEY (created_by)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_cms_updated_by
        FOREIGN KEY (updated_by)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- =====================================================
-- ENTITY INDEXES
-- (Matches @Table(indexes = ...)
-- =====================================================

CREATE INDEX idx_cms_page_key
    ON cms_pages(page_key);

CREATE INDEX idx_cms_active
    ON cms_pages(is_active);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_cms_published
    ON cms_pages(published);

CREATE INDEX idx_cms_created_by
    ON cms_pages(created_by);

CREATE INDEX idx_cms_updated_by
    ON cms_pages(updated_by);

CREATE INDEX idx_cms_created_at
    ON cms_pages(created_at);

CREATE INDEX idx_cms_updated_at
    ON cms_pages(updated_at);

CREATE INDEX idx_cms_published_active
    ON cms_pages(published, is_active);

CREATE INDEX idx_cms_page_key_active
    ON cms_pages(page_key, is_active);

CREATE INDEX idx_cms_page_key_published
    ON cms_pages(page_key, published);