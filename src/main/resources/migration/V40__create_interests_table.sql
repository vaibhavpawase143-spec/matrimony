CREATE TABLE interests (
                           id BIGSERIAL PRIMARY KEY,
                           sender_id BIGINT,
                           receiver_id BIGINT,
                           status VARCHAR(20),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_interest_sender
                               FOREIGN KEY (sender_id)
                                   REFERENCES users(id),
                           CONSTRAINT fk_interest_receiver
                               FOREIGN KEY (receiver_id)
                                   REFERENCES users(id),
                           CONSTRAINT uq_interest_sender_receiver
                               UNIQUE (sender_id, receiver_id)
);