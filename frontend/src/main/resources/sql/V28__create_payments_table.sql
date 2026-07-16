CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT,
                          amount DOUBLE PRECISION,
                          payment_method VARCHAR(100),
                          transaction_id VARCHAR(150),
                          payment_isActive VARCHAR(50),
                          CONSTRAINT fk_payment_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id),
                          CONSTRAINT uq_payments_transaction_id
                              UNIQUE (transaction_id)
);