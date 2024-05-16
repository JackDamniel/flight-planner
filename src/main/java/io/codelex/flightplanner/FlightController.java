package io.codelex.flightplanner;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}


