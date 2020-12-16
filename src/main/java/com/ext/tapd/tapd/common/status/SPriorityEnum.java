package com.ext.tapd.tapd.common.status;

public enum SPriorityEnum {
    HIGH("4", "High"), MIDDLE("3", "Middle"), LOW("2", "Low"), NICETOHAVE("1", "NiceToHave");
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
            if (el.getId().equals(code)) return el.getName();
        }
        return null;
    }
}
