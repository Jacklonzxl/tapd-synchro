package com.ext.tapd.tapd.pojo;

import org.springframework.context.annotation.ComponentScan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lx
 */
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
    //覆盖率
    private String coverage;
    //执行率
    @Column(name = "implement_rate")
    private String implementRate;
    //通过率
    @Column(name = "pass_rate")
    private String passRate;
    //项目名称
    private String workspace_name;

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

    public String getWorkspace_name() {
        return workspace_name;
    }

    public void setWorkspace_name(String workspace_name) {
        this.workspace_name = workspace_name;
    }
}
