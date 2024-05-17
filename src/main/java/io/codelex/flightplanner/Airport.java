package io.codelex.flightplanner;

import java.util.Objects;

public class Airport {
    private String country;
    private String city;
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }
    // Inside the Airport class
    public boolean isEgualAirport(Airport other) {
        if (other == null) {
            return false;
        }
        String thisCountry = this.country.trim().toLowerCase();
        String thisCity = this.city.trim().toLowerCase();
        String thisAirportCode = this.airport.trim().toUpperCase();

        String otherCountry = other.country.trim().toLowerCase();
        String otherCity = other.city.trim().toLowerCase();
        String otherAirportCode = other.airport.trim().toUpperCase();
        
        return thisCountry.equals(otherCountry) &&
                thisCity.equals(otherCity) &&
                thisAirportCode.equals(otherAirportCode);
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport airport1)) return false;
        return Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city) && Objects.equals(airport, airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}
