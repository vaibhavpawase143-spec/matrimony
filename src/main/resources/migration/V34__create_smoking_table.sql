CREATE TABLE smoking (
                         id BIGSERIAL PRIMARY KEY,
                         value VARCHAR(100),
                         admin_id BIGINT,
                         status BOOLEAN DEFAULT TRUE,
                         CONSTRAINT fk_smoking_admin
                             FOREIGN KEY (admin_id)
                                 REFERENCES admins(id),
                         CONSTRAINT uq_smoking_admin_value
                             UNIQUE (admin_id, value)
);