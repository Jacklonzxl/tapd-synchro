package com.ext.tapd.tapd.pojo;

import java.util.Map;

/**
 * @author lx
 */
public class ResultCountEntity {
    private Integer status;
    private Map data;
    private String info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
