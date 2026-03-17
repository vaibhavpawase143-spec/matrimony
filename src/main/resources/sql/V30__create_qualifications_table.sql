CREATE TABLE qualifications (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(150),

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_qualification_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
