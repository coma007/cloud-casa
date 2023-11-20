package com.casa.app.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    CityRepository cityRepository;
    @Autowired
    CountryRepository countryRepository;

    public City getByName(CityDTO cityDto) {
        City city = cityRepository.getCityByName(cityDto.getName().toUpperCase());
        if (city == null) {
            city = new City();
            city.setName(cityDto.getName().toUpperCase());
            city.setCountry(getByName(cityDto.getCountry()));
            cityRepository.save(city);
        }
        return city;
    }

    public Country getByName(String name) {
        Country country = countryRepository.getCountryByName(name.toUpperCase());
        if (country == null) {
            country = new Country();
            country.setName(name.toUpperCase());
            countryRepository.save(country);
        }
        return country;
    }

    public List<CityDTO> getAllCities(String country) {
        return cityRepository.getCityByCountryName(country.toUpperCase()).stream().map(CityDTO::new).collect(Collectors.toList());
    }

    public List<String> getAllCountries() {
        return countryRepository.findAll().stream().map(Country::getName).collect(Collectors.toList());
    }
}
