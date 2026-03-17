CREATE TABLE user_photos (
                             id BIGSERIAL PRIMARY KEY,
                             user_id BIGINT,
                             photo_type VARCHAR(50),
                             photo_url VARCHAR(255),
                             CONSTRAINT fk_user_photo_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users(id),
                             CONSTRAINT uq_user_photo_type
                                 UNIQUE (user_id, photo_type)
);