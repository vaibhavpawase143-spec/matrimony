CREATE TABLE complexions (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

value VARCHAR(100),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_complexion_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
