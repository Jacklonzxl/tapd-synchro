package com.ext.tapd.tapd.common.status;

/**
 * 紧急状态枚举
 * @author lx
 */
public enum SPriorityEnum {
    //High
    HIGH("4", "High"),
    //Middle
    MIDDLE("3", "Middle"),
    //Low
    LOW("2", "Low"),
    //NiceToHave
    NICETOHAVE("1", "NiceToHave");

    private String id;
    private String name;

    SPriorityEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getValue(String code) {
        for (SPriorityEnum el : values()) {
            if (el.getId().equals(code)) {
                return el.getName();
            }
        }
        return null;
    }
}
