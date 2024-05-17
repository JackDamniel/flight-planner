package io.codelex.flightplanner;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/testing-api/clear")
    public ResponseEntity<String> clearFlights() {
        flightService.clearFlights();
        return ResponseEntity.ok("Flights cleared successfully");
    }

    @PutMapping("/admin-api/flights")

    public ResponseEntity<Flight> addFlight(@Valid @RequestBody AddFlightRequest request) {
            Flight createdFlight = flightService.addFlight(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }
    @DeleteMapping("/admin-api/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/admin-api/flights/{id}")
    public ResponseEntity<Flight> fetchFlight(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id);
        return ResponseEntity.ok(flight);
    }
    @GetMapping("/api/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }
    @PostMapping("/api/flights/search")
    public ResponseEntity<PageResult<Flight>> searchFlights(SearchFlightsRequest request) {
        List<Flight> flights = flightService.searchFlights(request);
        PageResult<Flight> pageResult = new PageResult<>(0, flights.size(), flights);
        return ResponseEntity.ok(pageResult);
    }
    @GetMapping("/api/flights/{id}")
    public Flight findFlightById(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id);
        return flight;
    }
}


