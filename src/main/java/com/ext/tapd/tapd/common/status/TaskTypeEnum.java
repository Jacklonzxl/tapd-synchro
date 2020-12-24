//package com.ext.tapd.tapd.common.status;
//
//public enum TaskTypeEnum {
//    DEVELOPMENT("development","开发"),
//    REQUIREMENTS_REVIEW("requirements_review","需求评审"),
//    TECHNICAL_ARCHITECTURE_DESIGN("technical_architecture_design","技术架构设计"),
//    TECHNICAL_SCHEME_DESIGN("technical_scheme_design","技术方案设计"),
//    INTERFACE_DESIGN("interface_design","接口设计"),
//    API_DESIGN("api_design","api设计"),
//    UI_DESIGN("ui_design","ui设计"),
//    VIEW_DESIGN("view_design","视觉设计"),
//    OTHER("other","其他"),
//    EMPTY("empty","");
//    private String code;
//    private String name;
//
//    TaskTypeEnum(String code, String name) {
//        this.code = code;
//        this.name = name;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public static String getValue(String code) {
//        for (TaskTypeEnum el : values()) {
//            if (el.getCode().equals(code)) return el.getName();
//        }
//        return null;
//    }
//}
