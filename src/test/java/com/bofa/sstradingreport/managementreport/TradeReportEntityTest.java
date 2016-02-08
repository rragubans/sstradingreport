package com.bofa.sstradingreport.managementreport;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class TradeReportEntityTest {
    @Rule
    public final ExpectedException expectedException = none();

    private final TradeReportEntity defaultTestBike;

    public TradeReportEntityTest() {
        this.defaultTestBike = new TradeReportEntity();
    }


    @Test
    public void beanShouldWorkAsExpected() {
        final LocalDate now = LocalDate.now();
        TradeReportEntity bike = new TradeReportEntity("poef", now.withDayOfMonth(1));
        bike.prePersist();

        TradeReportEntity same = new TradeReportEntity("poef", now.withDayOfMonth(1));
        same.prePersist();

        TradeReportEntity other = new TradeReportEntity("other", now.withDayOfMonth(1));
        other.prePersist();

    }

}
