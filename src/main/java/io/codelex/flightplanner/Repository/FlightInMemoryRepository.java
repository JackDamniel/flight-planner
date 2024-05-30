package io.codelex.flightplanner.Repository;
import io.codelex.flightplanner.AddFlightRequest;
import io.codelex.flightplanner.Airport;
import io.codelex.flightplanner.Flight;
import io.codelex.flightplanner.SearchFlightsRequest;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FlightInMemoryRepository {

    private List<Flight> flights = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void saveFlight(Flight flight) {
        flight.setId(generateFlightId());
        flights.add(flight);
    }

    public List<Flight> findAllFlights() {
        return flights;
    }

    public synchronized boolean existsDuplicateFlight(AddFlightRequest request) {
        return flights.stream().anyMatch(flight ->
                flight.getFrom().equals(request.getFrom()) &&
                        flight.getTo().equals(request.getTo()) &&
                        flight.getDepartureTime().equals(request.getDepartureTime()) &&
                        flight.getArrivalTime().equals(request.getArrivalTime()) &&
                        flight.getCarrier().equals(request.getCarrier())
        );
    }
    public synchronized void deleteAll() {
        flights.clear();
    }

    public synchronized boolean deleteFlightById(Long id) {
        return flights.removeIf(flight -> flight.getId().equals(id));
    }
    public synchronized Flight addFlight(Flight flight) {
        flight.setId(generateFlightId());
        flights.add(flight);
        return flight;
    }
    private synchronized Long generateFlightId() {
        return idGenerator.getAndIncrement();
    }
    public Flight findFlightById(Long id) {
        for (Flight flight : flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }
    public List<Airport> searchAirports(String search) {
        String lowerCasePhrase = search.toLowerCase().trim();
        return flights.stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .distinct()
                .filter(Objects::nonNull)
                .filter(airport ->
                        airport.getCountry().toLowerCase().contains(lowerCasePhrase)
                                || airport.getCity().toLowerCase().contains(lowerCasePhrase)
                                || airport.getAirport().toLowerCase().contains(lowerCasePhrase))
                .collect(Collectors.toList());
    }
    public synchronized List<Flight> searchFlights(SearchFlightsRequest request) {
        return flights.stream()
                .filter(flight -> isEqualAirport(flight.getFrom(), request.getFrom()) &&
                        isEqualAirport(flight.getTo(), request.getTo()) &&
                        isSameDate(flight.getDepartureTime(), request.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private boolean isEqualAirport(Airport flightAirport, String requestAirport) {
        if (flightAirport == null || requestAirport == null) {
            return false;
        }
        String lowerCaseRequestAirport = requestAirport.trim().toLowerCase();
        return flightAirport.getCountry().trim().toLowerCase().contains(lowerCaseRequestAirport) ||
                flightAirport.getCity().trim().toLowerCase().contains(lowerCaseRequestAirport) ||
                flightAirport.getAirport().trim().toUpperCase().contains(lowerCaseRequestAirport.toUpperCase());
    }

    private boolean isSameDate(String flightDepartureTime, String requestDepartureDate) {
        LocalDateTime flightDateTime = LocalDateTime.parse(flightDepartureTime, formatter);
        LocalDate requestDate = LocalDate.parse(requestDepartureDate);
        return flightDateTime.toLocalDate().isEqual(requestDate);
    }
}

