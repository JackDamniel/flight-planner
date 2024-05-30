package io.codelex.flightplanner.Services;
import io.codelex.flightplanner.*;
import io.codelex.flightplanner.Repository.FlightInMemoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


public class FlightInMemoryService implements FlightPlannerService {
    private final FlightInMemoryRepository flightInMemoryRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightInMemoryService(FlightInMemoryRepository flightInMemoryRepository) {
        this.flightInMemoryRepository = flightInMemoryRepository;
    }
    @Override
    public synchronized void clearFlights() {
        flightInMemoryRepository.deleteAll();
    }
    @Override
    public synchronized void deleteFlight(Long flightId) {
        flightInMemoryRepository.deleteFlightById(flightId);
    }
    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        if (flightInMemoryRepository.existsDuplicateFlight(request)) {
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
        return flightInMemoryRepository.addFlight(newFlight);
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

        LocalDateTime departureTime = request.getDepartureTime();
        LocalDateTime arrivalTime = request.getArrivalTime();

        if (departureTime.isAfter(arrivalTime) || departureTime.isEqual(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        validateAirport(request.getFrom(), "From");
        validateAirport(request.getTo(), "To");

        if (request.getFrom().equals(request.getTo())) {
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
    @Override
    public Flight findFlightById(Long id) {
        Flight flight = flightInMemoryRepository.findFlightById(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }
    @Override
    public List<Airport> searchAirports(String search) {
        return flightInMemoryRepository.searchAirports(search);
    }
    @Override
    public synchronized List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);
        if (request.getFrom().equals(request.getTo())) {
            return Collections.emptyList();
        }

        List<Flight> flights = flightInMemoryRepository.searchFlights(request);

        if (flights.isEmpty()) {
            return Collections.emptyList();
        }
        return flights;
    }

    private void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getFrom().isEmpty() ||
                request.getTo() == null || request.getTo().isEmpty() ||
                request.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (request.getFrom().equals(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}