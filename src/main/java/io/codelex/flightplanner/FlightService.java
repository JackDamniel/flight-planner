package io.codelex.flightplanner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }
    public void clearFlights() {
        flightRepository.deleteAll();
    }
    public Flight addFlight(AddFlightRequest request) {
        if (flightRepository.existsDuplicateFlight(request)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        validateFlightRequest(request);

        Flight newFlight = new Flight(
                null,
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDepartureTime(),
                request.getArrivalTime()
        );
        return flightRepository.addFlight(newFlight);
    }
    private void validateFlightRequest(AddFlightRequest request) {
        if (request.getFrom() == null || request.getTo() == null ||
                request.getCarrier() == null || request.getDepartureTime() == null ||
                request.getArrivalTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (request.getCarrier().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime departureTime = LocalDateTime.parse(request.getDepartureTime(), formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(request.getArrivalTime(), formatter);


        if (departureTime.isAfter(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        validateAirport(request.getFrom(), "From");
        validateAirport(request.getTo(), "To");
    }

    private void validateAirport(Airport airport, String fieldName) {
        if (airport == null ||
                airport.getCountry() == null || airport.getCountry().isEmpty() ||
                airport.getCity() == null || airport.getCity().isEmpty() ||
                airport.getAirport() == null || airport.getAirport().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName);
        }
    }
}