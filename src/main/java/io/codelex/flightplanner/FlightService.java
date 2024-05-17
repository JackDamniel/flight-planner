package io.codelex.flightplanner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public boolean deleteFlight(Long flightId) {
        if (!flightRepository.deleteFlightById(flightId)) {}
        return true;
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

        if (departureTime.isAfter(arrivalTime) || departureTime.isEqual(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        validateAirport(request.getFrom(), "From");
        validateAirport(request.getTo(), "To");

        if (request.getFrom().isEgualAirport(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateAirport(Airport airport, String fieldName) {
        if (airport == null || airport.getCountry() == null || airport.getCountry().isEmpty() ||
                airport.getCity() == null || airport.getCity().isEmpty() ||
                airport.getAirport() == null || airport.getAirport().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName);
        }
    }
    public Flight findFlightById(Long id) {
        Flight flight = flightRepository.findFlightById(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }
    public List<Airport> searchAirports(String search) {
        return flightRepository.searchAirports(search);
    }
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);   //Making the 1st 05 test fail = sending back data
        return flightRepository.searchFlights(request);
    }

    private void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (request.getFrom().getAirport() == null || request.getTo().getAirport() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}