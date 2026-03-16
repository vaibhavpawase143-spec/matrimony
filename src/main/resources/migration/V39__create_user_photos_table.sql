CREATE TABLE user_photos (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT,

photo_type VARCHAR(50),

photo_url VARCHAR(255),

CONSTRAINT fk_user_photo_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
```

);
