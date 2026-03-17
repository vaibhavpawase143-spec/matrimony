CREATE TABLE heights (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

height VARCHAR(20),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_height_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
