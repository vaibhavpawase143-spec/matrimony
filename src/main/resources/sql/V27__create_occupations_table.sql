CREATE TABLE occupations (
                             id BIGSERIAL PRIMARY KEY,
                             admin_id BIGINT,
                             name VARCHAR(150),
                             status BOOLEAN DEFAULT TRUE,
                             CONSTRAINT fk_occupation_admin
                                 FOREIGN KEY (admin_id)
                                     REFERENCES admins(id),
                             CONSTRAINT uq_occupation_admin_name
                                 UNIQUE (admin_id, name)
);