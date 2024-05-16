package io.codelex.flightplanner;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddFlightRequest {
    @Valid
    @NotNull
    private Airport from;
    @Valid
    @NotNull
    private Airport to;
    @Valid
    @NotNull
    private String carrier;
    @Valid
    @NotNull
    private String departureTime;
    @Valid
    @NotNull
    private String arrivalTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AddFlightRequest(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime parseDepartureTime() {
        return LocalDateTime.parse(departureTime, formatter);
    }

    public LocalDateTime parseArrivalTime() {
        return LocalDateTime.parse(arrivalTime, formatter);
    }

    public String formatDepartureTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public String formatArrivalTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
