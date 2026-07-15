CREATE TABLE sub_castes (
                            id BIGSERIAL PRIMARY KEY,
                            admin_id BIGINT,
                            name VARCHAR(120),
                            caste_id BIGINT,
                            isActive BOOLEAN DEFAULT TRUE,
                            created_at TIMESTAMP NULL,
                            updated_at TIMESTAMP NULL,
                            CONSTRAINT fk_subcaste_admin
                                FOREIGN KEY (admin_id)
                                    REFERENCES admins(id),
                            CONSTRAINT fk_subcaste_caste
                                FOREIGN KEY (caste_id)
                                    REFERENCES castes(id),
                            CONSTRAINT uq_subcaste_caste_name
                                UNIQUE (caste_id, name)
);