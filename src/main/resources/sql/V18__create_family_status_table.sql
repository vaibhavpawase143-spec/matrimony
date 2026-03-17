CREATE TABLE family_status (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

name VARCHAR(100),

admin_id BIGINT,

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_family_status_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
