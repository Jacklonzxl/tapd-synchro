package com.ext.tapd.tapd.pojo;

import com.google.gson.internal.LinkedTreeMap;

import java.util.IdentityHashMap;
import java.util.List;

public class ResultEntity {

    private Integer status;
    private List<LinkedTreeMap> data;
    private String info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<LinkedTreeMap> getData() {
        return data;
    }

    public void setData(List<LinkedTreeMap> data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
