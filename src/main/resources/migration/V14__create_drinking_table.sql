CREATE TABLE drinking (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

value VARCHAR(100),

admin_id BIGINT,

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_drinking_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
