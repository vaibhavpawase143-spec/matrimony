-- =====================================================
-- V59__create_system_configurations.sql
-- System Configurations Table
-- =====================================================

CREATE TABLE system_configurations (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- CONFIGURATION
    -- =====================================================
    config_key VARCHAR(255) NOT NULL,

    config_value TEXT NOT NULL,

    category VARCHAR(100),

    description TEXT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    is_encrypted BOOLEAN NOT NULL DEFAULT FALSE,

    config_type VARCHAR(50),

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_system_configuration_key
        UNIQUE (config_key),

    CONSTRAINT chk_system_configuration_type
        CHECK (
            config_type IS NULL OR
            config_type IN (
                'STRING',
                'INTEGER',
                'BOOLEAN',
                'JSON'
            )
        ),

    CONSTRAINT chk_system_configuration_category
        CHECK (
            category IS NULL OR
            category IN (
                'EMAIL',
                'PAYMENT',
                'SYSTEM',
                'USER',
                'NOTIFICATION',
                'SECURITY'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_system_configuration_key
    ON system_configurations(config_key);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_system_configuration_category
    ON system_configurations(category);

CREATE INDEX idx_system_configuration_active
    ON system_configurations(is_active);

CREATE INDEX idx_system_configuration_encrypted
    ON system_configurations(is_encrypted);

CREATE INDEX idx_system_configuration_type
    ON system_configurations(config_type);

CREATE INDEX idx_system_configuration_created
    ON system_configurations(created_at);

CREATE INDEX idx_system_configuration_updated
    ON system_configurations(updated_at);

CREATE INDEX idx_system_configuration_category_active
    ON system_configurations(category, is_active);

CREATE INDEX idx_system_configuration_type_active
    ON system_configurations(config_type, is_active);