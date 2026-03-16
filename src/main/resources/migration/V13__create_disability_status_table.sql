CREATE TABLE disability_status (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

value VARCHAR(100),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_disability_status_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
