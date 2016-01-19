package com.test.rmmfl.helper;

import com.test.gdo.entity.ReportRow;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.math.BigInteger.valueOf;

public final class ReportRowHelper {

    public static ReportRow extractFromResultSet(final ResultSet rs) throws SQLException {
        return ReportRow.builder()
                .actions(valueOf(rs.getLong("actions")))
                .impressions(valueOf(rs.getLong("impressions")))
                .clicks(valueOf(rs.getLong("clicks")))
                .market(rs.getString("market"))
                .day(rs.getDate("day"))
                .build();
    }
}
