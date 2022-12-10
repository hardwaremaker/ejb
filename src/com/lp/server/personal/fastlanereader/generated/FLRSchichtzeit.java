package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSchichtzeit implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date u_beginn;

    /** nullable persistent field */
    private Date u_ende;

    /** nullable persistent field */
    private Short b_endedestages;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRSchichtzuschlag flrschichtzuschlag;

    /** full constructor */
    public FLRSchichtzeit(Date u_beginn, Date u_ende, Short b_endedestages, com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht, com.lp.server.personal.fastlanereader.generated.FLRSchichtzuschlag flrschichtzuschlag) {
        this.u_beginn = u_beginn;
        this.u_ende = u_ende;
        this.b_endedestages = b_endedestages;
        this.flrschicht = flrschicht;
        this.flrschichtzuschlag = flrschichtzuschlag;
    }

    /** default constructor */
    public FLRSchichtzeit() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getU_beginn() {
        return this.u_beginn;
    }

    public void setU_beginn(Date u_beginn) {
        this.u_beginn = u_beginn;
    }

    public Date getU_ende() {
        return this.u_ende;
    }

    public void setU_ende(Date u_ende) {
        this.u_ende = u_ende;
    }

    public Short getB_endedestages() {
        return this.b_endedestages;
    }

    public void setB_endedestages(Short b_endedestages) {
        this.b_endedestages = b_endedestages;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRSchicht getFlrschicht() {
        return this.flrschicht;
    }

    public void setFlrschicht(com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht) {
        this.flrschicht = flrschicht;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRSchichtzuschlag getFlrschichtzuschlag() {
        return this.flrschichtzuschlag;
    }

    public void setFlrschichtzuschlag(com.lp.server.personal.fastlanereader.generated.FLRSchichtzuschlag flrschichtzuschlag) {
        this.flrschichtzuschlag = flrschichtzuschlag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
