package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKollektivuestdBVA implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date u_100_ende;

    /** nullable persistent field */
    private Date u_50_beginn;

    /** nullable persistent field */
    private Date u_50_ende;

    /** nullable persistent field */
    private Date u_100_beginn;

    /** nullable persistent field */
    private Date u_gleitzeit_bis;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** full constructor */
    public FLRKollektivuestdBVA(Date u_100_ende, Date u_50_beginn, Date u_50_ende, Date u_100_beginn, Date u_gleitzeit_bis, com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.u_100_ende = u_100_ende;
        this.u_50_beginn = u_50_beginn;
        this.u_50_ende = u_50_ende;
        this.u_100_beginn = u_100_beginn;
        this.u_gleitzeit_bis = u_gleitzeit_bis;
        this.flrkollektiv = flrkollektiv;
        this.flrtagesart = flrtagesart;
    }

    /** default constructor */
    public FLRKollektivuestdBVA() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getU_100_ende() {
        return this.u_100_ende;
    }

    public void setU_100_ende(Date u_100_ende) {
        this.u_100_ende = u_100_ende;
    }

    public Date getU_50_beginn() {
        return this.u_50_beginn;
    }

    public void setU_50_beginn(Date u_50_beginn) {
        this.u_50_beginn = u_50_beginn;
    }

    public Date getU_50_ende() {
        return this.u_50_ende;
    }

    public void setU_50_ende(Date u_50_ende) {
        this.u_50_ende = u_50_ende;
    }

    public Date getU_100_beginn() {
        return this.u_100_beginn;
    }

    public void setU_100_beginn(Date u_100_beginn) {
        this.u_100_beginn = u_100_beginn;
    }

    public Date getU_gleitzeit_bis() {
        return this.u_gleitzeit_bis;
    }

    public void setU_gleitzeit_bis(Date u_gleitzeit_bis) {
        this.u_gleitzeit_bis = u_gleitzeit_bis;
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
