package com.ext.tapd.tapd.common.status;

/**
 * 状态枚举
 * @author lx
 */
public enum StatusEnum {
    OPEN("open", "未开始"),
    PROGRESSING("progressing", "进行中"),
    DONE("done", "已完成");

    private String code;
    private String name;

    StatusEnum(String code, String name) {
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
        for (StatusEnum el : values()) {
            if (el.getCode().equals(code)) {
                return el.getName();
            }
        }
        return null;
    }
}
