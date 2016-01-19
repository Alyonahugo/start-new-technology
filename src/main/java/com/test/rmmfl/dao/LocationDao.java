package com.test.rmmfl.dao;

import com.test.rmmfl.annotation.ToolsJdbcTemplate;
import com.test.rmmfl.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LocationDao {

    private static final String S_LOCATIONS = "SELECT * FROM location ORDER BY country, city LIMIT ? OFFSET ?";
    private static final String S_LOCATIONS_F_COUNTRY = "SELECT * FROM location WHERE iso = ? ORDER BY country, city " +
            "LIMIT ? OFFSET ?";
    private static final String S_COUNTRIES = "SELECT DISTINCT iso, country FROM location ORDER BY country";

    @Autowired
    @ToolsJdbcTemplate
    JdbcTemplate jdbcTemplate;

    public List<Location> getLocations(final int size, final int offset) {
        return jdbcTemplate.query(S_LOCATIONS, new Object[]{size, offset}, (rs, rowNum) -> {
            return extract(rs);
        });
    }

    public List<Location> getCountryLocations(final String iso, final int size, final int offset) {
        return jdbcTemplate.query(S_LOCATIONS_F_COUNTRY, new Object[]{iso, size, offset}, (rs, rowNum) -> {
            return extract(rs);
        });
    }

    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM location", Long.class);
    }

    public long countForCountry(final String iso) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM location WHERE iso = ?",
                new Object[]{iso}, Long.class);
    }

    private Location extract(final ResultSet rs) throws SQLException {
        return Location.builder()
                .id(rs.getLong("id"))
                .isoCode(rs.getString("iso"))
                .country(rs.getString("country"))
                .website(rs.getString("website"))
                .city(rs.getString("city"))
                .address1(rs.getString("address1"))
                .address2(rs.getString("address2"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .build();
    }
}
