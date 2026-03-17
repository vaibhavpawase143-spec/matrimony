CREATE TABLE roles (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(100),

CONSTRAINT fk_role_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
