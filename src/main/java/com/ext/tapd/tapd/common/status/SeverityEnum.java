package com.ext.tapd.tapd.common.status;

/**
 * 严重状态枚举
 * @author lx
 */
public enum SeverityEnum {
    //致命
    FATAL("fatal", "致命"),
    //严重
    SERIOUS("serious", "严重"),
    //一般
    NORMAL("normal", "一般"),
    //提示
    PROMPT("prompt", "提示"),
    //建议
    ADVICE("advice", "建议");

    private String code;
    private String name;

    SeverityEnum(String code, String name) {
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
        for (SeverityEnum el : values()) {
            if (el.getCode().equals(code)) {
                return el.getName();
            }
        }
        return null;
    }
}
