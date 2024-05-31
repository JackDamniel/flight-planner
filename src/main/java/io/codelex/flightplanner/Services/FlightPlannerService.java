package io.codelex.flightplanner.Services;

import io.codelex.flightplanner.AddFlightRequest;
import io.codelex.flightplanner.Airport;
import io.codelex.flightplanner.Flight;
import io.codelex.flightplanner.SearchFlightsRequest;

import java.util.List;

public interface FlightPlannerService {
    public void clearFlights();

    public void deleteFlight(Long flightId);

    public Flight addFlight(AddFlightRequest request);

    public Flight findFlightById(Long id);

    public List<Airport> searchAirports(String search);

    public List<Flight> searchFlights(SearchFlightsRequest request);
}
