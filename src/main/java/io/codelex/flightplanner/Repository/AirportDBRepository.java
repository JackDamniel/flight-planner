package io.codelex.flightplanner.Repository;

import io.codelex.flightplanner.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportDBRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCountryAndCityAndAirport(String country, String city, String airport);

    List<Airport> findByCountryContainingIgnoreCaseOrCityContainingIgnoreCaseOrAirportContainingIgnoreCase(String country, String city, String airport);
}

