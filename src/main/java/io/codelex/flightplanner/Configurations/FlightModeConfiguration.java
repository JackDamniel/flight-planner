package io.codelex.flightplanner.Configurations;

import io.codelex.flightplanner.Services.FlightValidator;
import io.codelex.flightplanner.Repository.AirportDBRepository;
import io.codelex.flightplanner.Repository.FlightDBRepository;
import io.codelex.flightplanner.Repository.FlightInMemoryRepository;
import io.codelex.flightplanner.Services.FlightDBService;
import io.codelex.flightplanner.Services.FlightInMemoryService;
import io.codelex.flightplanner.Services.FlightPlannerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightModeConfiguration {

    @Value("${myapp.flightPlanner-storage-mode}")
    private String storageMode;

    @Bean

    public FlightPlannerService createPersonServiceBean(FlightInMemoryRepository flightInMemoryRepository,
                                                        FlightDBRepository flightDBRepository, AirportDBRepository airportDBRepository, FlightValidator flightValidator) {
        if ("database".equals(storageMode)) {
            return new FlightDBService(flightDBRepository, airportDBRepository, flightValidator);
        } else {
            return new FlightInMemoryService(flightInMemoryRepository);
        }
    }
}