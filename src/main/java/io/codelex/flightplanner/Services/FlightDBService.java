package io.codelex.flightplanner.Services;

import io.codelex.flightplanner.*;
import io.codelex.flightplanner.Repository.AirportDBRepository;
import io.codelex.flightplanner.Repository.FlightDBRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightDBService implements FlightPlannerService {

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
        flightDBRepository.deleteAll();
    }

    @Override
    public void deleteFlight(Long flightId) {
        flightDBRepository.deleteById(flightId);
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        flightValidator.validateFlightRequest(request);

        if (flightExists(request)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Airport fromAirport = findOrCreateAirport(request.getFrom());
        Airport toAirport = findOrCreateAirport(request.getTo());

        Flight flight = new Flight();
        flight.setFrom(fromAirport);
        flight.setTo(toAirport);
        flight.setCarrier(request.getCarrier());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());

        return flightDBRepository.save(flight);
    }

    private boolean flightExists(AddFlightRequest request) {
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

        LocalDate departureTime = request.getDepartureDate();
        LocalDateTime startOfDay = departureTime.atStartOfDay();
        LocalDateTime endOfDay = departureTime.plusDays(1).atStartOfDay();

        List<Flight> flights = flightDBRepository.findByFrom_CountryAndTo_CountryAndDepartureTimeBetween(
                request.getFrom(), request.getTo(), startOfDay, endOfDay);

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

