package com.ext.tapd.tapd.common.status;


/**
 * 等级状态 枚举
 * @author lx
 */
public enum PriorityEnum {
    //紧急
    URGENT("urgent", "紧急"),
    //高
    HIGH("high", "高"),
    //中
    MEDIUM("medium", "中"),
    //低
    LOW("low", "低"),
    //无关紧要
    INSIGNIFICANT("insignificant", "无关紧要");

    private String code;
    private String name;

    PriorityEnum(String code, String name) {
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
        for (PriorityEnum el : values()) {
            if (el.getCode().equals(code)) {
                return el.getName();
            }
        }
        return null;
    }
}
