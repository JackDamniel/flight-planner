--liquibase formatted sql

--changeset linards:1
CREATE TABLE AIRPORT
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    airport VARCHAR(255) NOT NULL
);

--changeset linards:2
CREATE TABLE FLIGHT
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_airport_id BIGINT,
    to_airport_id BIGINT,
    carrier VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP,
    arrival_time TIMESTAMP,
    CONSTRAINT fk_from_airport FOREIGN KEY (from_airport_id) REFERENCES AIRPORT(id),
    CONSTRAINT fk_to_airport FOREIGN KEY (to_airport_id) REFERENCES AIRPORT(id)
);
