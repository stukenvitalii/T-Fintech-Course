CREATE TABLE places (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(100),
    name VARCHAR(255)
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    is_free BOOLEAN NOT NULL,
    date DATE,
    place BIGSERIAL REFERENCES places(id)
);