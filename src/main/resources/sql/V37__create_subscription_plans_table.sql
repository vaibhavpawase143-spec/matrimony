CREATE TABLE subscription_plans (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

name VARCHAR(120),

price DOUBLE,

duration_days INT,

description TEXT,

active BOOLEAN DEFAULT TRUE,

admin_id BIGINT,

CONSTRAINT fk_subscription_plan_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);
