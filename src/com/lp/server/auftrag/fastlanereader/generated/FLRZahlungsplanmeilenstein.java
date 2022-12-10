package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsplanmeilenstein implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer zahlungsplan_i_id;

    /** nullable persistent field */
    private Integer meilenstein_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein;

    /** nullable persistent field */
    private FLRPersonal flrpersonalerledigt;

    /** full constructor */
    public FLRZahlungsplanmeilenstein(Integer zahlungsplan_i_id, Integer meilenstein_i_id, Integer i_sort, String c_kommentar, String x_text, Date t_erledigt, com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein, FLRPersonal flrpersonalerledigt) {
        this.zahlungsplan_i_id = zahlungsplan_i_id;
        this.meilenstein_i_id = meilenstein_i_id;
        this.i_sort = i_sort;
        this.c_kommentar = c_kommentar;
        this.x_text = x_text;
        this.t_erledigt = t_erledigt;
        this.flrmeilenstein = flrmeilenstein;
        this.flrpersonalerledigt = flrpersonalerledigt;
    }

    /** default constructor */
    public FLRZahlungsplanmeilenstein() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getZahlungsplan_i_id() {
        return this.zahlungsplan_i_id;
    }

    public void setZahlungsplan_i_id(Integer zahlungsplan_i_id) {
        this.zahlungsplan_i_id = zahlungsplan_i_id;
    }

    public Integer getMeilenstein_i_id() {
        return this.meilenstein_i_id;
    }

    public void setMeilenstein_i_id(Integer meilenstein_i_id) {
        this.meilenstein_i_id = meilenstein_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein getFlrmeilenstein() {
        return this.flrmeilenstein;
    }

    public void setFlrmeilenstein(com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein) {
        this.flrmeilenstein = flrmeilenstein;
    }

    public FLRPersonal getFlrpersonalerledigt() {
        return this.flrpersonalerledigt;
    }

    public void setFlrpersonalerledigt(FLRPersonal flrpersonalerledigt) {
        this.flrpersonalerledigt = flrpersonalerledigt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
