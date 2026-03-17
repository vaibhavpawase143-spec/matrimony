CREATE TABLE manglik_status (
                                id BIGSERIAL PRIMARY KEY,
                                admin_id BIGINT,
                                name VARCHAR(100),
                                CONSTRAINT fk_manglik_status_admin
                                    FOREIGN KEY (admin_id)
                                        REFERENCES admins(id),
                                CONSTRAINT uq_manglik_status_admin_name
                                    UNIQUE (admin_id, name)
);