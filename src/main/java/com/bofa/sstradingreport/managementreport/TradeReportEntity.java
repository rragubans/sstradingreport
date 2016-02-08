package com.bofa.sstradingreport.managementreport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.Collectors.reducing;

@Entity
@Table(name = "bikes")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TradeReportEntity implements Serializable {

    private static final long serialVersionUID = 1249824815158908981L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true, length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(name = "trade_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull private Calendar tradeDate;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @JsonIgnore
    private Calendar createdAt;

    protected TradeReportEntity() {
    }

    public TradeReportEntity(String name, final LocalDate tradeDate) {
        this.name = name;
        this.tradeDate = GregorianCalendar.from(tradeDate.atStartOfDay(ZoneId.systemDefault()));
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Calendar.getInstance();
        }
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public Calendar getCreatedAt() {
        return this.createdAt;
    }


    public Calendar getTradeDate() {
        return tradeDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TradeReportEntity other = (TradeReportEntity) obj;
        return Objects.equals(this.name, other.name);
    }

    public static int comparePeriodsByValue(Map.Entry<LocalDate, Integer> period1, Map.Entry<LocalDate, Integer> period2) {
        return Integer.compare(period1.getValue(), period2.getValue());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTradeDate(Calendar tradeDate) {
        this.tradeDate = tradeDate;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}
