package com.ext.tapd.tapd.pojo;

import org.springframework.context.annotation.ComponentScan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_test_statistics")
public class TestStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "plan_date")
    private Date planDate;

    private String coverage;

    @Column(name = "implement_rate")
    private String implementRate;

    @Column(name = "pass_rate")
    private String passRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getImplementRate() {
        return implementRate;
    }

    public void setImplementRate(String implementRate) {
        this.implementRate = implementRate;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }
}