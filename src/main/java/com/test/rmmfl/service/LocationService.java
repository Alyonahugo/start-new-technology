package com.test.rmmfl.service;

import com.test.gdo.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LocationService {
    Page<Location> getLocations(final Pageable pageable, final Optional<String> countryIso);

    List<String> getCountries();
}
