CREATE TABLE family_values (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

name VARCHAR(100),

admin_id BIGINT,

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_family_values_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
