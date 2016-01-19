package com.test.rmmfl.web;


import com.test.rmmfl.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/locations", produces = "application/json;charset=utf-8")
public class LocationController {

    @Autowired
    LocationService locationService;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<Page<Locatlion>> fetchLocations(
            final @RequestParam(name = "country", required = false) Optional<String> countryIso,
            final Pageable pageable) {

        return ResponseEntity.ok(locationService.getLocations(pageable, countryIso));
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public HttpEntity<List<String>> fetchCountries() {
        return ResponseEntity.ok(locationService.getCountries());
    }
}
