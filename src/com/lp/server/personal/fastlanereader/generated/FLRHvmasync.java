package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmasync implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer systemrolle_i_id_synczeitpunkt;

    /** nullable persistent field */
    private Date t_sync;

    /** nullable persistent field */
    private String c_wowurdegebucht;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRSystemrolle flrsystemrolle;

    /** full constructor */
    public FLRHvmasync(Integer systemrolle_i_id_synczeitpunkt, Date t_sync, String c_wowurdegebucht, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, FLRSystemrolle flrsystemrolle) {
        this.systemrolle_i_id_synczeitpunkt = systemrolle_i_id_synczeitpunkt;
        this.t_sync = t_sync;
        this.c_wowurdegebucht = c_wowurdegebucht;
        this.flrpersonal = flrpersonal;
        this.flrsystemrolle = flrsystemrolle;
    }

    /** default constructor */
    public FLRHvmasync() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getSystemrolle_i_id_synczeitpunkt() {
        return this.systemrolle_i_id_synczeitpunkt;
    }

    public void setSystemrolle_i_id_synczeitpunkt(Integer systemrolle_i_id_synczeitpunkt) {
        this.systemrolle_i_id_synczeitpunkt = systemrolle_i_id_synczeitpunkt;
    }

    public Date getT_sync() {
        return this.t_sync;
    }

    public void setT_sync(Date t_sync) {
        this.t_sync = t_sync;
    }

    public String getC_wowurdegebucht() {
        return this.c_wowurdegebucht;
    }

    public void setC_wowurdegebucht(String c_wowurdegebucht) {
        this.c_wowurdegebucht = c_wowurdegebucht;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
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
