CREATE TABLE incomes (
                         id BIGSERIAL PRIMARY KEY,
                         admin_id BIGINT,
                         "range" VARCHAR(100),
                         CONSTRAINT fk_income_admin
                             FOREIGN KEY (admin_id)
                                 REFERENCES admins(id),
                         CONSTRAINT uq_income_admin_range
                             UNIQUE (admin_id, "range")
);