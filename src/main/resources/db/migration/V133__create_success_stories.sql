-- Create success_stories table
CREATE TABLE success_stories (
    id BIGSERIAL PRIMARY KEY,
    partner_one_name VARCHAR(150) NOT NULL,
    partner_two_name VARCHAR(150) NOT NULL,
    partner_one_image_url TEXT,
    partner_two_image_url TEXT,
    couple_image_url TEXT,
    short_story VARCHAR(1000) NOT NULL,
    full_story TEXT,
    wedding_date DATE,
    location VARCHAR(255),
    consent_given BOOLEAN NOT NULL DEFAULT FALSE,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Performance & Listing Indexes
CREATE INDEX idx_success_stories_is_published ON success_stories(is_published);
CREATE INDEX idx_success_stories_display_order ON success_stories(display_order);
CREATE INDEX idx_success_stories_wedding_date ON success_stories(wedding_date);
CREATE INDEX idx_success_stories_created_at ON success_stories(created_at);
CREATE INDEX idx_success_stories_pub_order ON success_stories(is_published, display_order);
CREATE INDEX idx_success_stories_pub_consent ON success_stories(is_published, consent_given);
