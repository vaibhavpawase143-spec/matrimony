CREATE TABLE mother_tongues (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(120),

status BOOLEAN DEFAULT TRUE,

created_at TIMESTAMP NULL,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_mother_tongue_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
