package com.test.rmmfl.web.dto;


import com.test.rmmfl.helper.ReportPeriod;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class TickDto {

    BigInteger impressions;
    BigInteger clicks;
    BigInteger actions;
    String market;
    ReportPeriod period;
    PriorPeriodData priorPeriodData;

    @Data
    @Builder
    public static class PriorPeriodData {
        BigInteger impressions;
        BigInteger clicks;
        BigInteger actions;
    }

}
