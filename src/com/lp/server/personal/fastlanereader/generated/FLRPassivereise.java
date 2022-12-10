package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPassivereise implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date u_bis;

    /** nullable persistent field */
    private Date u_ab;

    /** nullable persistent field */
    private Short b_restdestages;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** full constructor */
    public FLRPassivereise(Date u_bis, Date u_ab, Short b_restdestages, com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.u_bis = u_bis;
        this.u_ab = u_ab;
        this.b_restdestages = b_restdestages;
        this.flrkollektiv = flrkollektiv;
        this.flrtagesart = flrtagesart;
    }

    /** default constructor */
    public FLRPassivereise() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getU_bis() {
        return this.u_bis;
    }

    public void setU_bis(Date u_bis) {
        this.u_bis = u_bis;
    }

    public Date getU_ab() {
        return this.u_ab;
    }

    public void setU_ab(Date u_ab) {
        this.u_ab = u_ab;
    }

    public Short getB_restdestages() {
        return this.b_restdestages;
    }

    public void setB_restdestages(Short b_restdestages) {
        this.b_restdestages = b_restdestages;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRKollektiv getFlrkollektiv() {
        return this.flrkollektiv;
    }

    public void setFlrkollektiv(com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv) {
        this.flrkollektiv = flrkollektiv;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
