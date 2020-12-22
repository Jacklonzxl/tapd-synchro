package com.ext.tapd.tapd.pojo;

import java.util.List;

public class ResultTestPlanStoryTcaseRelationEntity {

    private Integer status;
    private List<TcaseRelationEntity> data;
    private String info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TcaseRelationEntity> getData() {
        return data;
    }

    public void setData(List<TcaseRelationEntity> data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
