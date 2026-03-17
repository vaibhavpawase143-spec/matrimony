CREATE TABLE shortlists (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT,
profile_id BIGINT,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_shortlist_user
    FOREIGN KEY (user_id)
    REFERENCES users(id),

CONSTRAINT fk_shortlist_profile
    FOREIGN KEY (profile_id)
    REFERENCES profiles(id)
```

);
