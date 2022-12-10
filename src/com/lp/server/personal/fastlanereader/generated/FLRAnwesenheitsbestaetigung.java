package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnwesenheitsbestaetigung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Date t_versandt;

    /** nullable persistent field */
    private Date t_unterschrift;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private String c_name;

    /** nullable persistent field */
    private Integer i_lfdnr;

    /** nullable persistent field */
    private String datenformat_c_nr;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** full constructor */
    public FLRAnwesenheitsbestaetigung(Integer personal_i_id, Integer auftrag_i_id, Integer projekt_i_id, Date t_versandt, Date t_unterschrift, String c_bemerkung, String c_name, Integer i_lfdnr, String datenformat_c_nr, FLRAuftrag flrauftrag, FLRProjekt flrprojekt, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.personal_i_id = personal_i_id;
        this.auftrag_i_id = auftrag_i_id;
        this.projekt_i_id = projekt_i_id;
        this.t_versandt = t_versandt;
        this.t_unterschrift = t_unterschrift;
        this.c_bemerkung = c_bemerkung;
        this.c_name = c_name;
        this.i_lfdnr = i_lfdnr;
        this.datenformat_c_nr = datenformat_c_nr;
        this.flrauftrag = flrauftrag;
        this.flrprojekt = flrprojekt;
        this.flrpersonal = flrpersonal;
    }

    /** default constructor */
    public FLRAnwesenheitsbestaetigung() {
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

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Date getT_versandt() {
        return this.t_versandt;
    }

    public void setT_versandt(Date t_versandt) {
        this.t_versandt = t_versandt;
    }

    public Date getT_unterschrift() {
        return this.t_unterschrift;
    }

    public void setT_unterschrift(Date t_unterschrift) {
        this.t_unterschrift = t_unterschrift;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public Integer getI_lfdnr() {
        return this.i_lfdnr;
    }

    public void setI_lfdnr(Integer i_lfdnr) {
        this.i_lfdnr = i_lfdnr;
    }

    public String getDatenformat_c_nr() {
        return this.datenformat_c_nr;
    }

    public void setDatenformat_c_nr(String datenformat_c_nr) {
        this.datenformat_c_nr = datenformat_c_nr;
    }

    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
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
