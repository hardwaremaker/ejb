package com.lp.server.system.fastlanereader.generated;

import com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRUsercount implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_zeitpunkt;

    /** nullable persistent field */
    private Integer i_anzahl;

    /** nullable persistent field */
    private FLRSystemrolle flrsystemrolle;

    /** full constructor */
    public FLRUsercount(Date t_zeitpunkt, Integer i_anzahl, FLRSystemrolle flrsystemrolle) {
        this.t_zeitpunkt = t_zeitpunkt;
        this.i_anzahl = i_anzahl;
        this.flrsystemrolle = flrsystemrolle;
    }

    /** default constructor */
    public FLRUsercount() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_zeitpunkt() {
        return this.t_zeitpunkt;
    }

    public void setT_zeitpunkt(Date t_zeitpunkt) {
        this.t_zeitpunkt = t_zeitpunkt;
    }

    public Integer getI_anzahl() {
        return this.i_anzahl;
    }

    public void setI_anzahl(Integer i_anzahl) {
        this.i_anzahl = i_anzahl;
    }

    public FLRSystemrolle getFlrsystemrolle() {
        return this.flrsystemrolle;
    }

    public void setFlrsystemrolle(FLRSystemrolle flrsystemrolle) {
        this.flrsystemrolle = flrsystemrolle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
