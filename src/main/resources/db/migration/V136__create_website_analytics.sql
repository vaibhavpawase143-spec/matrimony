CREATE TABLE IF NOT EXISTS website_analytics (
                                                 id BIGINT PRIMARY KEY,
                                                 profile_hits BIGINT NOT NULL DEFAULT 0,
                                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO website_analytics (
    id,
    profile_hits,
    updated_at
)
VALUES (
           1,
           0,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;