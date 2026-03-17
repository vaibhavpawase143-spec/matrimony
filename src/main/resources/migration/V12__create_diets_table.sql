CREATE TABLE diets (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(100),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_diet_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
