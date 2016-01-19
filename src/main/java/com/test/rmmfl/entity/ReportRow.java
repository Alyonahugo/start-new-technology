package com.test.rmmfl.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Date;

@Data
@Builder
public class ReportRow {
    BigInteger impressions;
    BigInteger clicks;
    BigInteger actions;
    String market;
    Date day;
}
