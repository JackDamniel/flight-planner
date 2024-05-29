package io.codelex.flightplanner;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
public class FlightController {

    private final FlightInMemoryService flightInMemoryService;

    @Autowired
    public FlightController(FlightInMemoryService flightInMemoryService) {
        this.flightInMemoryService = flightInMemoryService;
    }

    @PostMapping("/testing-api/clear")
    public String clearFlights() {
        flightInMemoryService.clearFlights();
        return "Flights cleared successfully";
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin-api/flights")
    public Flight addFlight(@Valid @RequestBody AddFlightRequest request) {
        Flight createdFlight = flightInMemoryService.addFlight(request);
        return createdFlight;
    }
    @DeleteMapping("/admin-api/flights/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightInMemoryService.deleteFlight(id);
    }

    @GetMapping("/admin-api/flights/{id}")
    public Flight fetchFlight(@PathVariable Long id) {
        Flight flight = flightInMemoryService.findFlightById(id);
        return flight;
    }
    @GetMapping("/api/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightInMemoryService.searchAirports(search);
    }
    @PostMapping("/api/flights/search")
    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest request) {
        List<Flight> flights = flightInMemoryService.searchFlights(request);
        PageResult<Flight> pageResult = new PageResult<>(0, flights.size(), flights);
        return pageResult;
    }

    @GetMapping("/api/flights/{id}")
    public Flight findFlightById(@PathVariable Long id) {
        Flight flight = flightInMemoryService.findFlightById(id);
        return flight;
    }
}


