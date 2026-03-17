CREATE TABLE payments (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT,

amount DOUBLE,

payment_method VARCHAR(100),

transaction_id VARCHAR(150),

payment_status VARCHAR(50),

CONSTRAINT fk_payment_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
```

);
