package com.ext.tapd.tapd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "t_stories")
public class Story implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    //标题
    private String name;
    //详细描述
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
    //规模
    private Double size;
    //优先级
    private String priority;
    //开发人员
    private String developer;
    //迭代
    private Long iteration_id;
    //迭代名称
    private String iteration_name;
    //测试重点
    private String test_focus;
    //类型
    private String type;
    //来源
    private String source;
    //模块
    private String module;
    //版本
    private String version;
    //完成时间
    private String completed;
    //需求分类
    private Long category_id;
    //需求分类名称
    private String category_name;

    private String path;
    //父需求
    private Long parent_id;
    //子需求
    private String children_id;
    //
    private Long ancestor_id;
    //业务价值
    private Long business_value;
    //预估工时
    private String effort;
    //完成工时
    private String effort_completed;
    //超出工时
    private float exceed;
    //剩余工时
    private float remain;
    //发布计划
    private Long release_id;

    private String custom_field_one;
    private String custom_field_two;
    private String custom_field_three;
    private String custom_field_four;
    private String custom_field_five;
    private String custom_field_six;
    private String custom_field_seven;
    private String custom_field_eight;

    private String custom_field_9;
    private String custom_field_10;
    private String custom_field_11;
    private String custom_field_12;
    private String custom_field_13;
    private String custom_field_14;
    private String custom_field_15;
    private String custom_field_16;
    private String custom_field_17;
    private String custom_field_18;
    private String custom_field_19;
    private String custom_field_20;
//    private String custom_field_21;
//    private String custom_field_22;
//    private String custom_field_23;
//    private String custom_field_24;
//    private String custom_field_25;
//    private String custom_field_26;
//    private String custom_field_27;
//    private String custom_field_28;
//    private String custom_field_29;
//    private String custom_field_30;
//    private String custom_field_31;
//    private String custom_field_32;
//    private String custom_field_33;
//    private String custom_field_34;
//    private String custom_field_35;
//    private String custom_field_36;
//    private String custom_field_37;
//    private String custom_field_38;
//    private String custom_field_39;
//    private String custom_field_40;
//    private String custom_field_41;
//    private String custom_field_42;
//    private String custom_field_43;
//    private String custom_field_44;
//    private String custom_field_45;
//    private String custom_field_46;
//    private String custom_field_47;
//    private String custom_field_48;
//    private String custom_field_49;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Long getIteration_id() {
        return iteration_id;
    }

    public void setIteration_id(Long iteration_id) {
        this.iteration_id = iteration_id;
    }

    public String getIteration_name() {
        return iteration_name;
    }

    public void setIteration_name(String iteration_name) {
        this.iteration_name = iteration_name;
    }

    public String getTest_focus() {
        return test_focus;
    }

    public void setTest_focus(String test_focus) {
        this.test_focus = test_focus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMdule() {
        return module;
    }

    public void setMdule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getChildren_id() {
        return children_id;
    }

    public void setChildren_id(String children_id) {
        this.children_id = children_id;
    }

    public Long getAncestor_id() {
        return ancestor_id;
    }

    public void setAncestor_id(Long ancestor_id) {
        this.ancestor_id = ancestor_id;
    }

    public Long getBusiness_value() {
        return business_value;
    }

    public void setBusiness_value(Long business_value) {
        this.business_value = business_value;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public String getEffort_completed() {
        return effort_completed;
    }

    public void setEffort_completed(String effort_completed) {
        this.effort_completed = effort_completed;
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

    public Long getRelease_id() {
        return release_id;
    }

    public void setRelease_id(Long release_id) {
        this.release_id = release_id;
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

    public String getCustom_field_9() {
        return custom_field_9;
    }

    public void setCustom_field_9(String custom_field_9) {
        this.custom_field_9 = custom_field_9;
    }

    public String getCustom_field_10() {
        return custom_field_10;
    }

    public void setCustom_field_10(String custom_field_10) {
        this.custom_field_10 = custom_field_10;
    }

    public String getCustom_field_11() {
        return custom_field_11;
    }

    public void setCustom_field_11(String custom_field_11) {
        this.custom_field_11 = custom_field_11;
    }

    public String getCustom_field_12() {
        return custom_field_12;
    }

    public void setCustom_field_12(String custom_field_12) {
        this.custom_field_12 = custom_field_12;
    }

    public String getCustom_field_13() {
        return custom_field_13;
    }

    public void setCustom_field_13(String custom_field_13) {
        this.custom_field_13 = custom_field_13;
    }

    public String getCustom_field_14() {
        return custom_field_14;
    }

    public void setCustom_field_14(String custom_field_14) {
        this.custom_field_14 = custom_field_14;
    }

    public String getCustom_field_15() {
        return custom_field_15;
    }

    public void setCustom_field_15(String custom_field_15) {
        this.custom_field_15 = custom_field_15;
    }

    public String getCustom_field_16() {
        return custom_field_16;
    }

    public void setCustom_field_16(String custom_field_16) {
        this.custom_field_16 = custom_field_16;
    }

    public String getCustom_field_17() {
        return custom_field_17;
    }

    public void setCustom_field_17(String custom_field_17) {
        this.custom_field_17 = custom_field_17;
    }

    public String getCustom_field_18() {
        return custom_field_18;
    }

    public void setCustom_field_18(String custom_field_18) {
        this.custom_field_18 = custom_field_18;
    }

    public String getCustom_field_19() {
        return custom_field_19;
    }

    public void setCustom_field_19(String custom_field_19) {
        this.custom_field_19 = custom_field_19;
    }

    public String getCustom_field_20() {
        return custom_field_20;
    }

    public void setCustom_field_20(String custom_field_20) {
        this.custom_field_20 = custom_field_20;
    }
}
