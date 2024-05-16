package io.codelex.flightplanner;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FlightRepository {

    private List<Flight> flights = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);

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
    public Flight addFlight(Flight flight) {
        flight.setId(generateFlightId());
        flights.add(flight);
        return flight;
    }
    private Long generateFlightId() {
        return idGenerator.getAndIncrement();
    }
}

