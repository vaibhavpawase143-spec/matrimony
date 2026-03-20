CREATE TABLE qualifications (
                                id BIGSERIAL PRIMARY KEY,
                                admin_id BIGINT,
                                name VARCHAR(150),
                                isActive BOOLEAN DEFAULT TRUE,
                                CONSTRAINT fk_qualification_admin
                                    FOREIGN KEY (admin_id)
                                        REFERENCES admins(id),
                                CONSTRAINT uq_qualification_admin_name
                                    UNIQUE (admin_id, name)
);