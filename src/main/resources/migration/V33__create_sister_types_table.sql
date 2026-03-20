CREATE TABLE sister_types (
                              id BIGSERIAL PRIMARY KEY,
                              admin_id BIGINT,
                              value VARCHAR(100) NOT NULL,
                              status BOOLEAN NOT NULL DEFAULT TRUE,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NULL,
                              CONSTRAINT fk_sister_type_admin
                                  FOREIGN KEY (admin_id)
                                      REFERENCES admins(id),
                              CONSTRAINT uq_sister_type_admin_value
                                  UNIQUE (admin_id, value)
);

-- Index on value
CREATE INDEX idx_sister_type_value ON sister_types(value);