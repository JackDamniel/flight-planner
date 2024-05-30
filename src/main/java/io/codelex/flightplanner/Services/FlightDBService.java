package io.codelex.flightplanner.Services;

import io.codelex.flightplanner.*;
import io.codelex.flightplanner.Repository.AirportDBRepository;
import io.codelex.flightplanner.Repository.FlightDBRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightDBService implements FlightPlannerService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final FlightDBRepository flightDBRepository;
    private final AirportDBRepository airportRepository;
    protected final FlightValidator flightValidator;

    public FlightDBService(FlightDBRepository flightDBRepository, AirportDBRepository airportRepository, FlightValidator flightValidator) {
        this.flightDBRepository = flightDBRepository;
        this.airportRepository = airportRepository;
        this.flightValidator = flightValidator;
    }

    @Override
    public synchronized void clearFlights() {
        this.flightDBRepository.deleteAll();
    }

    @Override
    public synchronized void deleteFlight(Long flightId) {
        this.flightDBRepository.deleteAll();

    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        flightValidator.validateFlightRequest(request);

        Airport fromAirport = findOrCreateAirport(request.getFrom());
        Airport toAirport = findOrCreateAirport(request.getTo());

        if (flightExists(request)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Flight flight = new Flight();
        flight.setFrom(fromAirport);
        flight.setTo(toAirport);
        flight.setCarrier(request.getCarrier());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());

        return flightDBRepository.save(flight);
    }
    public boolean flightExists(AddFlightRequest request) {
        Airport fromAirport = findOrCreateAirport(request.getFrom());
        Airport toAirport = findOrCreateAirport(request.getTo());

        LocalDateTime departureTime = request.getDepartureTime();
        LocalDateTime arrivalTime = request.getArrivalTime();

        Optional<Flight> existingFlight = flightDBRepository.findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
                fromAirport, toAirport, request.getCarrier(), departureTime, arrivalTime);

        return existingFlight.isPresent();
    }

    @Override
    public synchronized Flight findFlightById(Long id) {
        return flightDBRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public synchronized List<Airport> searchAirports(String search) {
        String lowerCasePhrase = search.toLowerCase().trim();
        return airportRepository.findByCountryContainingIgnoreCaseOrCityContainingIgnoreCaseOrAirportContainingIgnoreCase(
                        lowerCasePhrase, lowerCasePhrase, lowerCasePhrase)
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Flight> searchFlights(SearchFlightsRequest request) {
        flightValidator.validateSearchRequest(request);

        LocalDateTime departureTime = request.getDepartureDate();

        List<Flight> flights = flightDBRepository.findByFrom_CountryAndTo_CountryAndDepartureTimeAfter(
                request.getFrom(), request.getTo(), departureTime);

        if (flights.isEmpty()) {
            return Collections.emptyList();
        } else {
            return flights;
        }
    }

    private synchronized Airport findOrCreateAirport(Airport airport) {
        return airportRepository.findByCountryAndCityAndAirport(airport.getCountry(), airport.getCity(), airport.getAirport())
                .orElseGet(() -> airportRepository.save(airport));
    }
}

