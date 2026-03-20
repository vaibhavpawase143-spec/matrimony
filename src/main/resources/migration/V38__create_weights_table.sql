CREATE TABLE weights (
                         id BIGSERIAL PRIMARY KEY,
                         admin_id BIGINT,
                         value VARCHAR(50) NOT NULL,
                         status BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NULL,
                         CONSTRAINT fk_weight_admin
                             FOREIGN KEY (admin_id)
                                 REFERENCES admins(id),
                         CONSTRAINT uq_admin_value
                             UNIQUE (admin_id, value)
);

-- Index
CREATE INDEX idx_weight_value ON weights(value);