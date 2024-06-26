package io.codelex.flightplanner.Services;

import io.codelex.flightplanner.AddFlightRequest;
import io.codelex.flightplanner.Airport;
import io.codelex.flightplanner.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FlightValidator {

    protected void validateFlightRequest(AddFlightRequest request) {
        if (request.getFrom() == null || request.getTo() == null ||
                request.getCarrier() == null || request.getDepartureTime() == null ||
                request.getArrivalTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (request.getCarrier().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime departureTime = request.getDepartureTime();
        LocalDateTime arrivalTime = request.getArrivalTime();

        if (!arrivalTime.isAfter(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        validateAirport(request.getFrom(), "From");
        validateAirport(request.getTo(), "To");

        if (request.getFrom().getCountry().equalsIgnoreCase(request.getTo().getCountry()) &&
                request.getFrom().getCity().equalsIgnoreCase(request.getTo().getCity()) &&
                request.getFrom().getAirport().trim().equalsIgnoreCase(request.getTo().getAirport().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    protected void validateAirport(Airport airport, String fieldName) {
        if (airport.getCountry() == null || airport.getCountry().isEmpty() ||
                airport.getCity() == null || airport.getCity().isEmpty() ||
                airport.getAirport() == null || airport.getAirport().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName);
        }
    }

    protected void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getFrom().isEmpty() ||
                request.getTo() == null || request.getTo().isEmpty() ||
                request.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (request.getFrom().equalsIgnoreCase(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
