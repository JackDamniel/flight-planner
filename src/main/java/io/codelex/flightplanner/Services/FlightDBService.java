package io.codelex.flightplanner.Services;

import io.codelex.flightplanner.*;
import io.codelex.flightplanner.Repository.AirportDBRepository;
import io.codelex.flightplanner.Repository.FlightDBRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void clearFlights() {
        this.flightDBRepository.deleteAll();
    }

    @Override
    public void deleteFlight(Long flightId) {
        this.flightDBRepository.deleteAll();

    }

    @Override
    public Flight addFlight(AddFlightRequest request) {
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
        flight.setDepartureTime(request.parseDepartureTime().format(formatter));
        flight.setArrivalTime(request.parseArrivalTime().format(formatter));

        Flight savedFlight = flightDBRepository.save(flight);
        return savedFlight;
    }
    public boolean flightExists(AddFlightRequest request) {
        Airport fromAirport = findOrCreateAirport(request.getFrom());
        Airport toAirport = findOrCreateAirport(request.getTo());

        LocalDateTime departureTime = request.parseDepartureTime();
        LocalDateTime arrivalTime = request.parseArrivalTime();

        Optional<Flight> existingFlight = flightDBRepository.findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
                fromAirport, toAirport, request.getCarrier(),
                departureTime.format(formatter), arrivalTime.format(formatter));

        return existingFlight.isPresent();
    }

    @Override
    public Flight findFlightById(Long id) {
        return flightDBRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Airport> searchAirports(String search) {
        String lowerCasePhrase = search.toLowerCase().trim();
        return airportRepository.findByCountryContainingIgnoreCaseOrCityContainingIgnoreCaseOrAirportContainingIgnoreCase(lowerCasePhrase, lowerCasePhrase, lowerCasePhrase);
    }

    @Override
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        return null;
    }

    private Airport findOrCreateAirport(Airport airport) {
        return airportRepository.findByCountryAndCityAndAirport(airport.getCountry(), airport.getCity(), airport.getAirport())
                .orElseGet(() -> airportRepository.save(airport));
    }
}

