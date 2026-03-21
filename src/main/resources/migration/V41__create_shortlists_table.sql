CREATE TABLE shortlists (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT,
                            profile_id BIGINT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_shortlist_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id),
                            CONSTRAINT fk_shortlist_profile
                                FOREIGN KEY (profile_id)
                                    REFERENCES profiles(id),
                            CONSTRAINT uq_shortlist_user_profile
                                UNIQUE (user_id, profile_id)
);