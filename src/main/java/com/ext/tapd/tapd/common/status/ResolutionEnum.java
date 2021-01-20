package com.ext.tapd.tapd.common.status;

/**
 * 解决状态枚举
 * @author lx
 */
public enum ResolutionEnum {
    //无需解决
    IGNORE("ignore", "无需解决"),
    //延期解决
    FIX("fix", "延期解决"),
    //无法重现
    FAILED("failed", "无法重现"),
    //外部原因
    EXTERNAL("external", "外部原因"),
    //重复
    DUPLICATED("duplicated", "重复"),
    //设计如此
    INTENTIONAL("intentional", "设计如此"),
    //问题描述不准确
    UNCLEAR("unclear", "问题描述不准确"),
    //挂起
    HOLD("hold", "挂起"),
    //需求变更
    FEATURE("feature", "需求变更"),
    //已解决
    FIXED("fixed", "已解决");

    private String code;
    private String name;

    ResolutionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getValue(String code) {
        for (ResolutionEnum el : values()) {
            if (el.getCode().equals(code)) {
                return el.getName();
            }
        }
        return null;
    }
}
