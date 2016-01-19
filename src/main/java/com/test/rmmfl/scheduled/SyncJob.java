package com.test.rmmfl.scheduled;


import com.test.rmmfl.dao.ReportDao;
import com.test.rmmfl.dao.SyncDao;
import com.test.rmmfl.entity.ReportRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SyncJob {

    @Autowired
    SyncDao syncDao;
    @Autowired
    ReportDao reportDao;

    @Scheduled(fixedRateString = "${gdo.report.sync.interval}", initialDelay = 30000)
    public void syncReports() {
        final Optional<Date> date = reportDao.getLastSyncDate();
        log.info("Fetching reports from '{}'", date);
        final List<ReportRow> reports = syncDao.getReports(date);
        log.info("Got '{}' report rows", reports.size());
        reportDao.saveReportRows(reports);
    }

}
