CREATE TABLE incomes (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

range VARCHAR(100),

CONSTRAINT fk_income_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
