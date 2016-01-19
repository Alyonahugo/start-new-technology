package com.test.rmmfl.service.impl;


import com.test.rmmfl.dao.LocationDao;
import com.test.rmmfl.dao.ReportDao;
import com.test.rmmfl.entity.Location;
import com.test.rmmfl.helper.Tuple;
import com.test.rmmfl.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationDao locationDao;
    @Autowired
    ReportDao reportDao;

    @Override
    public Page<Location> getLocations(final Pageable pageable, final Optional<String> countryIso) {

        final Tuple<Long, List<Location>> tuple = countryIso
                .map(iso -> fetchAllForCountry(iso, pageable.getPageSize(), pageable.getOffset()))
                .orElse(fetchAll(pageable.getPageSize(), pageable.getOffset()));

        return new PageImpl<>(tuple.getT2(), pageable, tuple.getT1());
    }

    @Override
    public List<String> getCountries() {
        return reportDao.getAvailableCountries();
    }

    private Tuple<Long, List<Location>> fetchAll(final int size, final int offset) {
        final long count = locationDao.count();
        final List<Location> locations;
        if (count < 1) {
            locations = Collections.emptyList();
        } else {
            locations = locationDao.getLocations(size, offset);
        }
        return new Tuple<>(count, locations);
    }

    private Tuple<Long, List<Location>> fetchAllForCountry(final String iso, final int size, final int offset) {
        final long count = locationDao.countForCountry(iso);
        final List<Location> locations;
        if (count < 1) {
            locations = Collections.emptyList();
        } else {
            locations = locationDao.getCountryLocations(iso, size, offset);
        }
        return new Tuple<>(count, locations);
    }
}
