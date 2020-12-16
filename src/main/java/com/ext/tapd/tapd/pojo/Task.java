package com.ext.tapd.tapd.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_tasks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    //标题
    @Column(name = "name")
    private String name;

    //详细描述
    @Column(name = "description")
    private String description;
    //项目ID
    private Long workspace_id;
    //项目名称
    private String workspace_name;
    //创建人
    private String creator;
    //创建时间
    private Date created;
    //最后修改时间
    private Date modified;
    //状态
    private String status;
    //当前处理人
    private String owner;
    //抄送人
    private String cc;
    //预计开始
    private Date begin;
    //预计结束
    private Date due;
    //需求ID
    private Long story_id;
    //需求
    private String story_name;
    //迭代ID
    private Long iteration_id;
    //迭代
    private String iteration_name;
    //优先级
    private String priority;
    //进度
    private String progress;
    //完成时间
    private Date completed;
    //完成工时
    private String effort_completed;
    private String effort_total;
    //超出工时
    private float exceed;
    //剩余工时
    private float remain;
    //预估工时
    private String effort;
    private String custom_field_one;
    private String custom_field_two;
    private String custom_field_three;
    private String custom_field_four;
    private String custom_field_five;
    private String custom_field_six;
    private String custom_field_seven;
    private String custom_field_eight;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkspace_id() {
        return workspace_id;
    }

    public void setWorkspace_id(Long workspace_id) {
        this.workspace_id = workspace_id;
    }

    public String getWorkspace_name() {
        return workspace_name;
    }

    public void setWorkspace_name(String workspace_name) {
        this.workspace_name = workspace_name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Long getStory_id() {
        return story_id;
    }

    public void setStory_id(Long story_id) {
        this.story_id = story_id;
    }

    public String getStory_name() {
        return story_name;
    }

    public void setStory_name(String story_name) {
        this.story_name = story_name;
    }

    public String getIteration_name() {
        return iteration_name;
    }

    public void setIteration_name(String iteration_name) {
        this.iteration_name = iteration_name;
    }

    public Long getIteration_id() {
        return iteration_id;
    }

    public void setIteration_id(Long iteration_id) {
        this.iteration_id = iteration_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public String getEffort_completed() {
        return effort_completed;
    }

    public void setEffort_completed(String effort_completed) {
        this.effort_completed = effort_completed;
    }

    public String getEffort_total() {
        return effort_total;
    }

    public void setEffort_total(String effort_total) {
        this.effort_total = effort_total;
    }

    public float getExceed() {
        return exceed;
    }

    public void setExceed(float exceed) {
        this.exceed = exceed;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public String getCustom_field_one() {
        return custom_field_one;
    }

    public void setCustom_field_one(String custom_field_one) {
        this.custom_field_one = custom_field_one;
    }

    public String getCustom_field_two() {
        return custom_field_two;
    }

    public void setCustom_field_two(String custom_field_two) {
        this.custom_field_two = custom_field_two;
    }

    public String getCustom_field_three() {
        return custom_field_three;
    }

    public void setCustom_field_three(String custom_field_three) {
        this.custom_field_three = custom_field_three;
    }

    public String getCustom_field_four() {
        return custom_field_four;
    }

    public void setCustom_field_four(String custom_field_four) {
        this.custom_field_four = custom_field_four;
    }

    public String getCustom_field_five() {
        return custom_field_five;
    }

    public void setCustom_field_five(String custom_field_five) {
        this.custom_field_five = custom_field_five;
    }

    public String getCustom_field_six() {
        return custom_field_six;
    }

    public void setCustom_field_six(String custom_field_six) {
        this.custom_field_six = custom_field_six;
    }

    public String getCustom_field_seven() {
        return custom_field_seven;
    }

    public void setCustom_field_seven(String custom_field_seven) {
        this.custom_field_seven = custom_field_seven;
    }

    public String getCustom_field_eight() {
        return custom_field_eight;
    }

    public void setCustom_field_eight(String custom_field_eight) {
        this.custom_field_eight = custom_field_eight;
    }
}
