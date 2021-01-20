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
@Table(name = "t_workspace")
public class Workspace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    private String name;

    private String pretty_name;

    private String status;

    private String secrecy;

    private Date created;

    private Long creator_id;

    private int member_count;

    private String creator;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPretty_name() {
        return pretty_name;
    }

    public void setPretty_name(String pretty_name) {
        this.pretty_name = pretty_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(String secrecy) {
        this.secrecy = secrecy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
