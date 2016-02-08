package com.bofa.sstradingreport.managementreport;

import java.util.Calendar;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeReportRepository extends JpaRepository<TradeReportEntity, Integer> {
    List<TradeReportEntity> findAll();
    
    TradeReportEntity findByName(final String name);
 
}
