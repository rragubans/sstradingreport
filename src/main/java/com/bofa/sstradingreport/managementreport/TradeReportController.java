package com.bofa.sstradingreport.managementreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
class TradeReportController {

    private final TradeReportRepository tradeReportRepository;

    @Autowired
    public TradeReportController(final TradeReportRepository tradeReportRepository) {
        this.tradeReportRepository = tradeReportRepository;
    }

    @RequestMapping(value = "/tradereport", method = GET)
    public List<TradeReportEntity> getTrades(final @RequestParam(required = false, defaultValue = "false") boolean all) {

        List<TradeReportEntity> list = new ArrayList<>();
        for (int i=0; i < 10; i++) {
            TradeReportEntity reportEntity = new TradeReportEntity();
            reportEntity.setId(i);
            reportEntity.setName("A" + i);
            reportEntity.setTradeDate(Calendar.getInstance());
            list.add(reportEntity);
        }
        return list;
        //return tradeReportRepository.findAll();
    }
}
