package com.test.rmmfl.dao;


import com.test.rmmfl.annotation.ToolsJdbcTemplate;
import com.test.rmmfl.annotation.ToolsTransactionManager;
import com.test.rmmfl.entity.ReportRow;
import com.test.rmmfl.helper.ReportRowHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ReportDao {

    private static final String INSERT_QUERY =
            "INSERT INTO daily_report_row (impressions, clicks, actions, market, day) VALUES (?, ?, ?, ?, ?)";
    private static final String S_REPORT_QUERY =
            "SELECT SUM(r.impressions), SUM(r.clicks), SUM(r.actions) FROM daily_report_row r " +
                    "WHERE r.market = ? AND r.day BETWEEN ? AND ?";

    @Autowired
    @ToolsJdbcTemplate
    JdbcTemplate jdbcTemplate;
    @Autowired
    @ToolsTransactionManager
    PlatformTransactionManager transactionManager;

    public ReportRow getReportForPeriod(final Date from, final Date to, final String market) {
        return jdbcTemplate.queryForObject(S_REPORT_QUERY, new Object[]{market, from, to},
                (resultSet, i) -> ReportRowHelper.extractFromResultSet(resultSet));
    }

    public List<String> getAvailableCountries() {
        return jdbcTemplate.queryForList("SELECT DISTINCT d.market FROM daily_report_row d", String.class);
    }

    public Optional<Date> getLastSyncDate() {
        final List<Date> dates = jdbcTemplate.queryForList(
                "SELECT d.day FROM daily_report_row d ORDER BY d.day DESC LIMIT 1", Date.class);
        return dates.isEmpty() ? Optional.<Date>empty() : Optional.of(dates.get(0));
    }

    public void saveReportRows(final List<ReportRow> rows) {

        if (rows.isEmpty()) {
            log.debug("Noting to save");
            return;
        }

        final TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("Saving report rows");
        try {

            jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final ReportRow row = rows.get(i);
                    ps.setObject(1, row.getImpressions());
                    ps.setObject(2, row.getClicks());
                    ps.setObject(3, row.getActions());
                    ps.setString(4, row.getMarket());
                    ps.setDate(5, row.getDay());
                }

                @Override
                public int getBatchSize() {
                    return rows.size();
                }
            });
            transactionManager.commit(transaction);
            log.info("Reports are saved");

        } catch (DataAccessException ex) {
            // reports will be synced with next run
            log.error("Can't save report data: ", ex);
            transactionManager.rollback(transaction);
        }

    }

}
