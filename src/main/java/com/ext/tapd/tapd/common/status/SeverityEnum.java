package com.ext.tapd.tapd.common.status;

public enum SeverityEnum {
    FATAL("fatal","致命"),SERIOUS("serious","严重"),NORMAL("normal","一般"),
    PROMPT("prompt","提示"),ADVICE("advice","建议");

    private String code;
    private String name;

    SeverityEnum(String code,String name){
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
        for(SeverityEnum el : values()){
            if(el.getCode().equals(code)) return el.getName();
        }
        return null;
    }
}
