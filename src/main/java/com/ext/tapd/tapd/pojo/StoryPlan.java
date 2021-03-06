package com.ext.tapd.tapd.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "t_story_plan")
public class StoryPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "iteration_name")
    private String iterationName;
    @Column(name = "story_type")
    private String storyType;
    @Column(name = "task_type")
    private String taskType;
    private String total_finish;
    private String development = "9999";
    private Integer story_num;
    private BigInteger task_num;
    private String requirements_review = "9999";
    private String technical_architecture_design = "9999";
    private String technical_scheme_design = "9999";
    private String interface_design = "9999";
    private String api_design = "9999";
    private String ui_design = "9999";
    private String view_design = "9999";
    private String other = "9999";
    private String empty = "9999";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevelopment() {
        return development;
    }

    public void setDevelopment(String development) {
        this.development = development;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTotal_finish() {
        return total_finish;
    }

    public void setTotal_finish(String total_finish) {
        this.total_finish = total_finish;
    }

    public Integer getStory_num() {
        return story_num;
    }

    public void setStory_num(Integer story_num) {
        this.story_num = story_num;
    }

    public BigInteger getTask_num() {
        return task_num;
    }

    public void setTask_num(BigInteger task_num) {
        this.task_num = task_num;
    }

    public String getRequirements_review() {
        return requirements_review;
    }

    public void setRequirements_review(String requirements_review) {
        this.requirements_review = requirements_review;
    }

    public String getTechnical_architecture_design() {
        return technical_architecture_design;
    }

    public void setTechnical_architecture_design(String technical_architecture_design) {
        this.technical_architecture_design = technical_architecture_design;
    }

    public String getTechnical_scheme_design() {
        return technical_scheme_design;
    }

    public void setTechnical_scheme_design(String technical_scheme_design) {
        this.technical_scheme_design = technical_scheme_design;
    }

    public String getInterface_design() {
        return interface_design;
    }

    public void setInterface_design(String interface_design) {
        this.interface_design = interface_design;
    }

    public String getApi_design() {
        return api_design;
    }

    public void setApi_design(String api_design) {
        this.api_design = api_design;
    }

    public String getUi_design() {
        return ui_design;
    }

    public void setUi_design(String ui_design) {
        this.ui_design = ui_design;
    }

    public String getView_design() {
        return view_design;
    }

    public void setView_design(String view_design) {
        this.view_design = view_design;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }
}
