DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (account_id VARCHAR(20) PRIMARY KEY,
user_id INT,
balance DECIMAL(19,4),
currency VARCHAR(3),
account_limit DECIMAL(19,4)
);

CREATE INDEX idx_acc on accounts(user_id);

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (tid VARCHAR(64) PRIMARY KEY,
                       date_created long,
                       source VARCHAR(20),
                       destination VARCHAR(20),
                       balance DECIMAL(19,4),
);


