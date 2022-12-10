package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREintrittaustritt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Date t_eintritt;

    /** nullable persistent field */
    private Date t_austritt;

    /** nullable persistent field */
    private Short b_wiedereintritt;

    /** nullable persistent field */
    private String c_austrittsgrund;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** full constructor */
    public FLREintrittaustritt(Integer personal_i_id, Date t_eintritt, Date t_austritt, Short b_wiedereintritt, String c_austrittsgrund, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.personal_i_id = personal_i_id;
        this.t_eintritt = t_eintritt;
        this.t_austritt = t_austritt;
        this.b_wiedereintritt = b_wiedereintritt;
        this.c_austrittsgrund = c_austrittsgrund;
        this.flrpersonal = flrpersonal;
    }

    /** default constructor */
    public FLREintrittaustritt() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Date getT_eintritt() {
        return this.t_eintritt;
    }

    public void setT_eintritt(Date t_eintritt) {
        this.t_eintritt = t_eintritt;
    }

    public Date getT_austritt() {
        return this.t_austritt;
    }

    public void setT_austritt(Date t_austritt) {
        this.t_austritt = t_austritt;
    }

    public Short getB_wiedereintritt() {
        return this.b_wiedereintritt;
    }

    public void setB_wiedereintritt(Short b_wiedereintritt) {
        this.b_wiedereintritt = b_wiedereintritt;
    }

    public String getC_austrittsgrund() {
        return this.c_austrittsgrund;
    }

    public void setC_austrittsgrund(String c_austrittsgrund) {
        this.c_austrittsgrund = c_austrittsgrund;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
