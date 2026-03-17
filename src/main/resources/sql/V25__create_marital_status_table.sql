CREATE TABLE marital_status (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(100),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_marital_status_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
