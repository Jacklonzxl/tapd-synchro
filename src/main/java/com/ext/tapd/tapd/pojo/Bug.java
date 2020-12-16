package com.ext.tapd.tapd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_bugs")
public class Bug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;
    //标题
    private String title;
    //详细描述
    private String description;
    //优先级
    private String priority;
    //严重程度
    private String severity;
    //模块
    private String module;
    //状态
    private String status;
    //创建人
    private String reporter;
    //解决期限
    private Date deadline;
    //创建时间
    private Date created;
    //缺陷类型
    private String bugtype;
    //解决时间
    private String resolved;
    //关闭时间
    private String closed;
    //最后修改时间
    private Date modified;
    //最后修改人
    private String lastmodify;
    //审核人
    private String auditer;
    //开发人员
    private String de;
    //修复人
    private String fixer;
    //验证版本
    private String version_test;
    //发现版本
    private String version_report;
    //关闭版本
    private String version_close;
    //合入版本
    private String version_fix;
    //发现基线
    private String baseline_find;
    //合入基线
    private String baseline_join;
    //关闭基线
    private String baseline_close;
    //验证基线
    private String baseline_test;
    //引入阶段
    private String sourcephase;
    //测试人员
    private String te;
    //当前处理人
    private String current_owner;
    //迭代
    private Long iteration_id;
    //迭代
    private String iteration_name;
    //解决方法
    private String resolution;
    //缺陷根源
    private String source;
    //发现阶段
    private String originphase;
    //验证人
    private String confirmer;
    //
    private String milestone;
    //参与人
    private String participator;
    //关闭人
    private String closer;
    //软件平台
    private String platform;
    //操作系统
    private String os;
    //测试类型
    private String testtype;
    //测试阶段
    private String testphase;
    //重现规律
    private String frequency;
    //抄送人
    private String cc;
    //
    private String regression_number;
    private String flows;
    private String feature;
    //测试方式
    private String testmode;
    private String estimate;
    private Long issue_id;
    private String created_from;
    //接受处理时间
    private Date in_progress_time;
    //验证时间
    private Date verify_time;
    //拒绝时间
    private Date reject_time;

    private Date reopen_time;
    private Date audit_time;
    private Date suspend_time;
    //预计结束
    private Date due;
    //预计开始
    private Date begin;
    private String custom_field_one;
    private String custom_field_two;
    private String custom_field_three;
    private String custom_field_four;
    private String custom_field_five;
    private String custom_field_6;
    private String custom_field_7;
    private String custom_field_8;
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
    //项目ID
    private Long workspace_id;
    //项目名称
    private String workspace_name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getBugtype() {
        return bugtype;
    }

    public void setBugtype(String bugtype) {
        this.bugtype = bugtype;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(String lastmodify) {
        this.lastmodify = lastmodify;
    }

    public String getAuditer() {
        return auditer;
    }

    public void setAuditer(String auditer) {
        this.auditer = auditer;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getFixer() {
        return fixer;
    }

    public void setFixer(String fixer) {
        this.fixer = fixer;
    }

    public String getVersion_test() {
        return version_test;
    }

    public void setVersion_test(String version_test) {
        this.version_test = version_test;
    }

    public String getVersion_report() {
        return version_report;
    }

    public void setVersion_report(String version_report) {
        this.version_report = version_report;
    }

    public String getVersion_close() {
        return version_close;
    }

    public void setVersion_close(String version_close) {
        this.version_close = version_close;
    }

    public String getVersion_fix() {
        return version_fix;
    }

    public void setVersion_fix(String version_fix) {
        this.version_fix = version_fix;
    }

    public String getBaseline_find() {
        return baseline_find;
    }

    public void setBaseline_find(String baseline_find) {
        this.baseline_find = baseline_find;
    }

    public String getBaseline_join() {
        return baseline_join;
    }

    public void setBaseline_join(String baseline_join) {
        this.baseline_join = baseline_join;
    }

    public String getBaseline_close() {
        return baseline_close;
    }

    public void setBaseline_close(String baseline_close) {
        this.baseline_close = baseline_close;
    }

    public String getBaseline_test() {
        return baseline_test;
    }

    public void setBaseline_test(String baseline_test) {
        this.baseline_test = baseline_test;
    }

    public String getSourcephase() {
        return sourcephase;
    }

    public void setSourcephase(String sourcephase) {
        this.sourcephase = sourcephase;
    }

    public String getTe() {
        return te;
    }

    public void setTe(String te) {
        this.te = te;
    }

    public String getCurrent_owner() {
        return current_owner;
    }

    public void setCurrent_owner(String current_owner) {
        this.current_owner = current_owner;
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

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOriginphase() {
        return originphase;
    }

    public void setOriginphase(String originphase) {
        this.originphase = originphase;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getParticipator() {
        return participator;
    }

    public void setParticipator(String participator) {
        this.participator = participator;
    }

    public String getCloser() {
        return closer;
    }

    public void setCloser(String closer) {
        this.closer = closer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getTesttype() {
        return testtype;
    }

    public void setTesttype(String testtype) {
        this.testtype = testtype;
    }

    public String getTestphase() {
        return testphase;
    }

    public void setTestphase(String testphase) {
        this.testphase = testphase;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getRegression_number() {
        return regression_number;
    }

    public void setRegression_number(String regression_number) {
        this.regression_number = regression_number;
    }

    public String getFlows() {
        return flows;
    }

    public void setFlows(String flows) {
        this.flows = flows;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getTestmode() {
        return testmode;
    }

    public void setTestmode(String testmode) {
        this.testmode = testmode;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public Long getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(Long issue_id) {
        this.issue_id = issue_id;
    }

    public String getCreated_from() {
        return created_from;
    }

    public void setCreated_from(String created_from) {
        this.created_from = created_from;
    }

    public Date getIn_progress_time() {
        return in_progress_time;
    }

    public void setIn_progress_time(Date in_progress_time) {
        this.in_progress_time = in_progress_time;
    }

    public Date getVerify_time() {
        return verify_time;
    }

    public void setVerify_time(Date verify_time) {
        this.verify_time = verify_time;
    }

    public Date getReject_time() {
        return reject_time;
    }

    public void setReject_time(Date reject_time) {
        this.reject_time = reject_time;
    }

    public Date getReopen_time() {
        return reopen_time;
    }

    public void setReopen_time(Date reopen_time) {
        this.reopen_time = reopen_time;
    }

    public Date getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(Date audit_time) {
        this.audit_time = audit_time;
    }

    public Date getSuspend_time() {
        return suspend_time;
    }

    public void setSuspend_time(Date suspend_time) {
        this.suspend_time = suspend_time;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
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

    public String getCustom_field_6() {
        return custom_field_6;
    }

    public void setCustom_field_6(String custom_field_6) {
        this.custom_field_6 = custom_field_6;
    }

    public String getCustom_field_7() {
        return custom_field_7;
    }

    public void setCustom_field_7(String custom_field_7) {
        this.custom_field_7 = custom_field_7;
    }

    public String getCustom_field_8() {
        return custom_field_8;
    }

    public void setCustom_field_8(String custom_field_8) {
        this.custom_field_8 = custom_field_8;
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
}
