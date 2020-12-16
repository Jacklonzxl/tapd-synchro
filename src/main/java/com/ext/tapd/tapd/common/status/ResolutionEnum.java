package com.ext.tapd.tapd.common.status;

public enum ResolutionEnum {

    IGNORE("ignore","无需解决"),FIX("fix","延期解决"),FAILED("failed","无法重现"),
    EXTERNAL("external","外部原因"),DUPLICATED("duplicated","重复"),INTENTIONAL("intentional","设计如此"),
    UNCLEAR("unclear","问题描述不准确"),HOLD("hold","挂起"),FEATURE("feature","需求变更"),
    FIXED("fixed","已解决");
    private String code;
    private String name;

    ResolutionEnum(String code,String name){
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
        for(ResolutionEnum el : values()){
            if(el.getCode().equals(code)) return el.getName();
        }
        return null;
    }
}
