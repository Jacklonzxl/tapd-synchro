package com.ext.tapd.tapd.pojo;

import java.math.BigInteger;

/**
 * @author lx
 */
public class TaskTypeEntity {

    private BigInteger totalnum;
    private String name;
    private String status;

    public BigInteger getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(BigInteger totalnum) {
        this.totalnum = totalnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
