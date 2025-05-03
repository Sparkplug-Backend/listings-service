CREATE TABLE listing (
    id                   BIGSERIAL PRIMARY KEY,
    car_configuration_id BIGINT NOT NULL,
    creator_id           BIGINT NOT NULL,
    price                DECIMAL(19,2)  NOT NULL,
    mileage              INTEGER NOT NULL,
    description          TEXT,
    created_at           TIMESTAMP NOT NULL
);

CREATE TABLE listing_image (
    id         BIGSERIAL PRIMARY KEY,
    listing_id BIGINT NOT NULL REFERENCES listing(id),
    url        VARCHAR(255) NOT NULL
);