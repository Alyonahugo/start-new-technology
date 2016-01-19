package com.test.rmmfl.service.impl;


import com.test.rmmfl.dao.ReportDao;
import com.test.rmmfl.entity.ReportRow;
import com.test.rmmfl.helper.ReportPeriod;
import com.test.rmmfl.helper.Tuple;
import com.test.rmmfl.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportDao reportDao;

    @Override
    public Tuple<ReportRow, ReportRow> getReport(final ReportPeriod period, final String market) {

        final Date[] dates = getDatesForPeriod(period);

        final Tuple<ReportRow, ReportRow> response = new Tuple<>();
        response.setT1(reportDao.getReportForPeriod(dates[0], dates[1], market));

        // `all` period can't contain previous period
        if (period != ReportPeriod.all) {
            response.setT2(reportDao.getReportForPeriod(null, null, market));
        }

        return response;
    }

    private Date[] getDatesForPeriod(final ReportPeriod period) {

        final LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate from;
        LocalDate prevTo = null;
        LocalDate prevFrom = null;

        // `from` and `to` are inclusive in query
        switch (period) {
            case all:
                from = yesterday.minusYears(10);
                break;
            case month:
                from = yesterday.minusMonths(1).plusDays(1);
                prevTo = yesterday.minusMonths(1);
                prevFrom = prevTo.minusMonths(1).plusDays(1);
                break;
            case week:
                from = yesterday.minusWeeks(1).plusDays(1);
                prevTo = yesterday.minusWeeks(1);
                prevFrom = prevTo.minusWeeks(1).plusDays(1);
                break;
            case day:
                from = yesterday;
                prevTo = yesterday.minusDays(1);
                prevFrom = prevTo;
                break;
            case hour:
            default:
                throw new UnsupportedOperationException("Unsupported period");
        }

        return new Date[]{toSqlDate(from), toSqlDate(yesterday), toSqlDate(prevFrom), toSqlDate(prevTo)};
    }

    private Date toSqlDate(final LocalDate date) {
        return Optional.ofNullable(date)
                .map(d -> new Date(d.atStartOfDay().toEpochSecond(ZoneOffset.UTC))).orElse(null);
    }
}
