CREATE TABLE marital_isActive (
                                id BIGSERIAL PRIMARY KEY,
                                admin_id BIGINT,
                                name VARCHAR(100),
                                isActive BOOLEAN DEFAULT TRUE,
                                CONSTRAINT fk_marital_isActive_admin
                                    FOREIGN KEY (admin_id)
                                        REFERENCES admins(id),
                                CONSTRAINT uq_marital_isActive_admin_name
                                    UNIQUE (admin_id, name)
);