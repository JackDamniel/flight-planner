package io.codelex.flightplanner;
import io.codelex.flightplanner.Services.FlightPlannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
public class FlightController {

    @Value("dd.mm.yyyy")
    private String dateFormat;

    private final FlightPlannerService flightPlannerService;

    @Autowired
    public FlightController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/testing-api/clear")
    public String clearFlights() {
        flightPlannerService.clearFlights();
        return "Flights cleared successfully";
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin-api/flights")
    public Flight addFlight(@Valid @RequestBody AddFlightRequest request) {
        Flight createdFlight = flightPlannerService.addFlight(request);
        return createdFlight;
    }
    @DeleteMapping("/admin-api/flights/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightPlannerService.deleteFlight(id);
    }

    @GetMapping("/admin-api/flights/{id}")
    public Flight fetchFlight(@PathVariable Long id) {
        Flight flight = flightPlannerService.findFlightById(id);
        return flight;
    }
    @GetMapping("/api/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightPlannerService.searchAirports(search);
    }
    @PostMapping("/api/flights/search")
    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest request) {
        List<Flight> flights = flightPlannerService.searchFlights(request);
        PageResult<Flight> pageResult = new PageResult<>(0, flights.size(), flights);
        return pageResult;
    }

    @GetMapping("/api/flights/{id}")
    public Flight findFlightById(@PathVariable Long id) {
        Flight flight = flightPlannerService.findFlightById(id);
        return flight;
    }
}


