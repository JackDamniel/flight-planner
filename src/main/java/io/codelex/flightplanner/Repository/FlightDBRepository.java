package io.codelex.flightplanner.Repository;

import io.codelex.flightplanner.Airport;
import io.codelex.flightplanner.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightDBRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
            Airport from, Airport to, String carrier, String departureTime, String arrivalTime);
}
