package com.ext.tapd.tapd.pojo;

/**
 * @author lx
 */
public class ResultImplementionEntity {
    private Integer status;
    private Implementation data;
    private String info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Implementation getData() {
        return data;
    }

    public void setData(Implementation data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
