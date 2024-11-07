-- V3__create_tokens_table.sql
CREATE TABLE tokens (
                        id SERIAL PRIMARY KEY,
                        token VARCHAR(255) NOT NULL,
                        expiry_date TIMESTAMP NOT NULL,
                        active BOOLEAN NOT NULL,
                        user_id INT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);