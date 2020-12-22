package com.ext.tapd.tapd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_test_plan_story_tcase")
public class TestPlanStoryTcaseRelation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    //项目ID
    private Long workspace_id;
    //测试计划ID
    private Long test_plan_id;
    //需求ID
    private Long story_id;
    //测试用例ID
    private Long tcase_id;

    private int sort;
    //创建人
    private String creator;
    //关系创建时间
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkspace_id() {
        return workspace_id;
    }

    public void setWorkspace_id(Long workspace_id) {
        this.workspace_id = workspace_id;
    }

    public Long getTest_plan_id() {
        return test_plan_id;
    }

    public void setTest_plan_id(Long test_plan_id) {
        this.test_plan_id = test_plan_id;
    }

    public Long getStory_id() {
        return story_id;
    }

    public void setStory_id(Long story_id) {
        this.story_id = story_id;
    }

    public Long getTcase_id() {
        return tcase_id;
    }

    public void setTcase_id(Long tcase_id) {
        this.tcase_id = tcase_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
}
