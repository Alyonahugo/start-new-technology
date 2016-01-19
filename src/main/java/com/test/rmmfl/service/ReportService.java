package com.test.rmmfl.service;


import com.test.rmmfl.entity.ReportRow;
import com.test.rmmfl.helper.ReportPeriod;
import com.test.rmmfl.helper.Tuple;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    Tuple<ReportRow, ReportRow> getReport(final ReportPeriod period, final String market);
}
