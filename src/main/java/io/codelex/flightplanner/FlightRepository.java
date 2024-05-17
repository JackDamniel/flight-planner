package io.codelex.flightplanner;
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
public class FlightRepository {

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

    public boolean existsDuplicateFlight(AddFlightRequest request) {
        for (Flight flight : flights) {
            if (flight.getFrom().equals(request.getFrom())
                    && flight.getTo().equals(request.getTo())
                    && flight.getDepartureTime().equals(request.getDepartureTime())
                    && flight.getArrivalTime().equals(request.getArrivalTime())
                    && flight.getCarrier().equals(request.getCarrier())) {
                return true;
            }
        }
        return false;
    }
    public void deleteAll() {
        flights.clear();
    }

    public boolean deleteFlightById(Long id) {
        return flights.removeIf(flight -> flight.getId().equals(id));
    }
    public Flight addFlight(Flight flight) {
        flight.setId(generateFlightId());
        flights.add(flight);
        return flight;
    }
    private Long generateFlightId() {
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
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        return flights.stream()
                .filter(flight -> flight.getFrom().isEgualAirport(request.getFrom())
                        && flight.getTo().isEgualAirport(request.getTo())
                        && isSameDate(flight.getDepartureTime(), request.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private boolean isSameDate(String flightDepartureTime, String requestDepartureDate) {
        LocalDateTime flightDateTime = LocalDateTime.parse(flightDepartureTime, formatter);
        LocalDate requestDate = LocalDate.parse(requestDepartureDate);
        return flightDateTime.toLocalDate().isEqual(requestDate);
    }
}

