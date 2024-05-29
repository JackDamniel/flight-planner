--liquibase formatted sql

--changeset linards:1

CREATE TABLE FLIGHT (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    From_Airport   VARCHAR(255) NOT NULL,
    To_Airport     VARCHAR(255) NOT NULL,
    Carrier        VARCHAR(255) NOT NULL,
    Departure_Time TIMESTAMP,
    Arrival_Time   TIMESTAMP
);
