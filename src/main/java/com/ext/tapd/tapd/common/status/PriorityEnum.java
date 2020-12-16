package com.ext.tapd.tapd.common.status;


public enum PriorityEnum {

    URGENT("urgent","紧急"),HIGH("high","高"),MEDIUM("medium","中"),
    LOW("low","低"),INSIGNIFICANT("insignificant","无关紧要");
    private String code;
    private String name;

    PriorityEnum(String code, String name){
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

    public static String getValue(String code){
        for(PriorityEnum el : values()){
            if(el.getCode().equals(code)) return el.getName();
        }
        return null;
    }
}
