package com.ext.tapd.tapd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lx
 */
@Entity
@Table(name = "t_test_plan")
public class TestPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    //项目ID
    private Long workspace_id;
    //用例名称
    private String name;
    //需求分类描述
    private String description;
    //版本
    private String version;
    //测试负责人
    private String owner;
    //用例状态
    private String status;
    //测试类型
    private String type;
    //开始时间
    private Date start_date;
    //结束时间
    private Date end_date;
    //创建人
    private String creator;
    //创建时间
    private Date created;
    //最后修改时间
    private Date modified;
    //最后修改人
    private String modifier;

    private String created_from;

    private String custom_field_1;
    private String custom_field_2;
    private String custom_field_3;
    private String custom_field_4;
    private String custom_field_5;
    private String custom_field_6;
    private String custom_field_7;
    private String custom_field_8;
    private String custom_field_9;
    private String custom_field_10;
    private String custom_field_11;
    private String custom_field_12;
    private String custom_field_13;
    private String custom_field_14;
    private String custom_field_15;
    private String custom_field_16;
    private String custom_field_17;
    private String custom_field_18;
    private String custom_field_19;
    private String custom_field_20;
    private String custom_field_21;
    private String custom_field_22;
    private String custom_field_23;
    private String custom_field_24;
    private String custom_field_25;
    private String custom_field_26;
    private String custom_field_27;
    private String custom_field_28;
    private String custom_field_29;
    private String custom_field_30;
    private String custom_field_31;
    private String custom_field_32;
    private String custom_field_33;
    private String custom_field_34;
    private String custom_field_35;
    private String custom_field_36;
    private String custom_field_37;
    private String custom_field_38;
    private String custom_field_39;
    private String custom_field_40;
    private String custom_field_41;
    private String custom_field_42;
    private String custom_field_43;
    private String custom_field_44;
    private String custom_field_45;
    private String custom_field_46;
    private String custom_field_47;
    private String custom_field_48;
    private String custom_field_49;
    private String custom_field_50;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkspace_id() {
        return workspace_id;
    }

    public void setWorkspace_id(Long workspace_id) {
        this.workspace_id = workspace_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getCreated_from() {
        return created_from;
    }

    public void setCreated_from(String created_from) {
        this.created_from = created_from;
    }

    public String getCustom_field_1() {
        return custom_field_1;
    }

    public void setCustom_field_1(String custom_field_1) {
        this.custom_field_1 = custom_field_1;
    }

    public String getCustom_field_2() {
        return custom_field_2;
    }

    public void setCustom_field_2(String custom_field_2) {
        this.custom_field_2 = custom_field_2;
    }

    public String getCustom_field_3() {
        return custom_field_3;
    }

    public void setCustom_field_3(String custom_field_3) {
        this.custom_field_3 = custom_field_3;
    }

    public String getCustom_field_4() {
        return custom_field_4;
    }

    public void setCustom_field_4(String custom_field_4) {
        this.custom_field_4 = custom_field_4;
    }

    public String getCustom_field_5() {
        return custom_field_5;
    }

    public void setCustom_field_5(String custom_field_5) {
        this.custom_field_5 = custom_field_5;
    }

    public String getCustom_field_6() {
        return custom_field_6;
    }

    public void setCustom_field_6(String custom_field_6) {
        this.custom_field_6 = custom_field_6;
    }

    public String getCustom_field_7() {
        return custom_field_7;
    }

    public void setCustom_field_7(String custom_field_7) {
        this.custom_field_7 = custom_field_7;
    }

    public String getCustom_field_8() {
        return custom_field_8;
    }

    public void setCustom_field_8(String custom_field_8) {
        this.custom_field_8 = custom_field_8;
    }

    public String getCustom_field_9() {
        return custom_field_9;
    }

    public void setCustom_field_9(String custom_field_9) {
        this.custom_field_9 = custom_field_9;
    }

    public String getCustom_field_10() {
        return custom_field_10;
    }

    public void setCustom_field_10(String custom_field_10) {
        this.custom_field_10 = custom_field_10;
    }

    public String getCustom_field_11() {
        return custom_field_11;
    }

    public void setCustom_field_11(String custom_field_11) {
        this.custom_field_11 = custom_field_11;
    }

    public String getCustom_field_12() {
        return custom_field_12;
    }

    public void setCustom_field_12(String custom_field_12) {
        this.custom_field_12 = custom_field_12;
    }

    public String getCustom_field_13() {
        return custom_field_13;
    }

    public void setCustom_field_13(String custom_field_13) {
        this.custom_field_13 = custom_field_13;
    }

    public String getCustom_field_14() {
        return custom_field_14;
    }

    public void setCustom_field_14(String custom_field_14) {
        this.custom_field_14 = custom_field_14;
    }

    public String getCustom_field_15() {
        return custom_field_15;
    }

    public void setCustom_field_15(String custom_field_15) {
        this.custom_field_15 = custom_field_15;
    }

    public String getCustom_field_16() {
        return custom_field_16;
    }

    public void setCustom_field_16(String custom_field_16) {
        this.custom_field_16 = custom_field_16;
    }

    public String getCustom_field_17() {
        return custom_field_17;
    }

    public void setCustom_field_17(String custom_field_17) {
        this.custom_field_17 = custom_field_17;
    }

    public String getCustom_field_18() {
        return custom_field_18;
    }

    public void setCustom_field_18(String custom_field_18) {
        this.custom_field_18 = custom_field_18;
    }

    public String getCustom_field_19() {
        return custom_field_19;
    }

    public void setCustom_field_19(String custom_field_19) {
        this.custom_field_19 = custom_field_19;
    }

    public String getCustom_field_20() {
        return custom_field_20;
    }

    public void setCustom_field_20(String custom_field_20) {
        this.custom_field_20 = custom_field_20;
    }

    public String getCustom_field_21() {
        return custom_field_21;
    }

    public void setCustom_field_21(String custom_field_21) {
        this.custom_field_21 = custom_field_21;
    }

    public String getCustom_field_22() {
        return custom_field_22;
    }

    public void setCustom_field_22(String custom_field_22) {
        this.custom_field_22 = custom_field_22;
    }

    public String getCustom_field_23() {
        return custom_field_23;
    }

    public void setCustom_field_23(String custom_field_23) {
        this.custom_field_23 = custom_field_23;
    }

    public String getCustom_field_24() {
        return custom_field_24;
    }

    public void setCustom_field_24(String custom_field_24) {
        this.custom_field_24 = custom_field_24;
    }

    public String getCustom_field_25() {
        return custom_field_25;
    }

    public void setCustom_field_25(String custom_field_25) {
        this.custom_field_25 = custom_field_25;
    }

    public String getCustom_field_26() {
        return custom_field_26;
    }

    public void setCustom_field_26(String custom_field_26) {
        this.custom_field_26 = custom_field_26;
    }

    public String getCustom_field_27() {
        return custom_field_27;
    }

    public void setCustom_field_27(String custom_field_27) {
        this.custom_field_27 = custom_field_27;
    }

    public String getCustom_field_28() {
        return custom_field_28;
    }

    public void setCustom_field_28(String custom_field_28) {
        this.custom_field_28 = custom_field_28;
    }

    public String getCustom_field_29() {
        return custom_field_29;
    }

    public void setCustom_field_29(String custom_field_29) {
        this.custom_field_29 = custom_field_29;
    }

    public String getCustom_field_30() {
        return custom_field_30;
    }

    public void setCustom_field_30(String custom_field_30) {
        this.custom_field_30 = custom_field_30;
    }

    public String getCustom_field_31() {
        return custom_field_31;
    }

    public void setCustom_field_31(String custom_field_31) {
        this.custom_field_31 = custom_field_31;
    }

    public String getCustom_field_32() {
        return custom_field_32;
    }

    public void setCustom_field_32(String custom_field_32) {
        this.custom_field_32 = custom_field_32;
    }

    public String getCustom_field_33() {
        return custom_field_33;
    }

    public void setCustom_field_33(String custom_field_33) {
        this.custom_field_33 = custom_field_33;
    }

    public String getCustom_field_34() {
        return custom_field_34;
    }

    public void setCustom_field_34(String custom_field_34) {
        this.custom_field_34 = custom_field_34;
    }

    public String getCustom_field_35() {
        return custom_field_35;
    }

    public void setCustom_field_35(String custom_field_35) {
        this.custom_field_35 = custom_field_35;
    }

    public String getCustom_field_36() {
        return custom_field_36;
    }

    public void setCustom_field_36(String custom_field_36) {
        this.custom_field_36 = custom_field_36;
    }

    public String getCustom_field_37() {
        return custom_field_37;
    }

    public void setCustom_field_37(String custom_field_37) {
        this.custom_field_37 = custom_field_37;
    }

    public String getCustom_field_38() {
        return custom_field_38;
    }

    public void setCustom_field_38(String custom_field_38) {
        this.custom_field_38 = custom_field_38;
    }

    public String getCustom_field_39() {
        return custom_field_39;
    }

    public void setCustom_field_39(String custom_field_39) {
        this.custom_field_39 = custom_field_39;
    }

    public String getCustom_field_40() {
        return custom_field_40;
    }

    public void setCustom_field_40(String custom_field_40) {
        this.custom_field_40 = custom_field_40;
    }

    public String getCustom_field_41() {
        return custom_field_41;
    }

    public void setCustom_field_41(String custom_field_41) {
        this.custom_field_41 = custom_field_41;
    }

    public String getCustom_field_42() {
        return custom_field_42;
    }

    public void setCustom_field_42(String custom_field_42) {
        this.custom_field_42 = custom_field_42;
    }

    public String getCustom_field_43() {
        return custom_field_43;
    }

    public void setCustom_field_43(String custom_field_43) {
        this.custom_field_43 = custom_field_43;
    }

    public String getCustom_field_44() {
        return custom_field_44;
    }

    public void setCustom_field_44(String custom_field_44) {
        this.custom_field_44 = custom_field_44;
    }

    public String getCustom_field_45() {
        return custom_field_45;
    }

    public void setCustom_field_45(String custom_field_45) {
        this.custom_field_45 = custom_field_45;
    }

    public String getCustom_field_46() {
        return custom_field_46;
    }

    public void setCustom_field_46(String custom_field_46) {
        this.custom_field_46 = custom_field_46;
    }

    public String getCustom_field_47() {
        return custom_field_47;
    }

    public void setCustom_field_47(String custom_field_47) {
        this.custom_field_47 = custom_field_47;
    }

    public String getCustom_field_48() {
        return custom_field_48;
    }

    public void setCustom_field_48(String custom_field_48) {
        this.custom_field_48 = custom_field_48;
    }

    public String getCustom_field_49() {
        return custom_field_49;
    }

    public void setCustom_field_49(String custom_field_49) {
        this.custom_field_49 = custom_field_49;
    }

    public String getCustom_field_50() {
        return custom_field_50;
    }

    public void setCustom_field_50(String custom_field_50) {
        this.custom_field_50 = custom_field_50;
    }
}
