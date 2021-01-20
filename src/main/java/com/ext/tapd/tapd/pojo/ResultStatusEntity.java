package com.ext.tapd.tapd.pojo;

import java.util.Map;

/**
 * @author lx
 */
public class ResultStatusEntity {

    private Integer status;
    private Map<String, Object> data;
    private String info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
