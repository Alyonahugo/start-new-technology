package com.test.rmmfl.dao;


import com.test.rmmfl.annotation.ReportingJdbcTemplate;
import com.test.rmmfl.entity.ReportRow;
import com.test.rmmfl.helper.ReportRowHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Component
public class SyncDao {

    private static final String QUERY = "SELECT " +
            "SUM(impressions) impressions" +
            "FROM reporting_db r " +
            "%s " +
            "GROUP BY r.day";
    private static final String CONDITION = "WHERE r.day > ? ";

    @Autowired
    @ReportingJdbcTemplate
    JdbcTemplate jdbcTemplate;


    public List<ReportRow> getReports(final Optional<Date> from) {

        final String query;
        final Object[] args;

        if (from.isPresent()) {
            query = String.format(QUERY, CONDITION);
            args = new Object[]{from.get()};
        } else {
            query = String.format(QUERY, "");
            args = new Object[0];
        }

        return jdbcTemplate.query(query, args, (rs, rowNum) -> ReportRowHelper.extractFromResultSet(rs));
    }

}
