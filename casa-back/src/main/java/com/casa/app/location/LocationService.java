package com.casa.app.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    CityRepository cityRepository;

    public City getByName(String name) {
        return cityRepository.getCityByName(name);
    }
}
