CREATE TABLE mother_tongues (
                                id BIGSERIAL PRIMARY KEY,
                                admin_id BIGINT,
                                name VARCHAR(120),
                                isActive BOOLEAN DEFAULT TRUE,
                                created_at TIMESTAMP NULL,
                                updated_at TIMESTAMP NULL,
                                CONSTRAINT fk_mother_tongue_admin
                                    FOREIGN KEY (admin_id)
                                        REFERENCES admins(id),
                                CONSTRAINT uq_mother_tongue_admin_name
                                    UNIQUE (admin_id, name)
);