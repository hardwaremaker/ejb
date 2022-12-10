package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHistory implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private Short b_html;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Double f_erledigungsgrad;

    /** nullable persistent field */
    private BigDecimal n_dauer_geplant;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRHistoryart flrhistoryart;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_aendern;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_wirddurchgefuehrtvon;

    /** full constructor */
    public FLRHistory(Integer personal_i_id, Integer projekt_i_id, Date t_belegdatum, String x_text, String c_titel, Short b_html, Date t_anlegen, Date t_aendern, Double f_erledigungsgrad, BigDecimal n_dauer_geplant, com.lp.server.projekt.fastlanereader.generated.FLRHistoryart flrhistoryart, FLRPersonal flrpersonal, com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt, FLRPersonal flrpersonal_aendern, FLRPersonal flrpersonal_wirddurchgefuehrtvon) {
        this.personal_i_id = personal_i_id;
        this.projekt_i_id = projekt_i_id;
        this.t_belegdatum = t_belegdatum;
        this.x_text = x_text;
        this.c_titel = c_titel;
        this.b_html = b_html;
        this.t_anlegen = t_anlegen;
        this.t_aendern = t_aendern;
        this.f_erledigungsgrad = f_erledigungsgrad;
        this.n_dauer_geplant = n_dauer_geplant;
        this.flrhistoryart = flrhistoryart;
        this.flrpersonal = flrpersonal;
        this.flrprojekt = flrprojekt;
        this.flrpersonal_aendern = flrpersonal_aendern;
        this.flrpersonal_wirddurchgefuehrtvon = flrpersonal_wirddurchgefuehrtvon;
    }

    /** default constructor */
    public FLRHistory() {
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

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public Short getB_html() {
        return this.b_html;
    }

    public void setB_html(Short b_html) {
        this.b_html = b_html;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Double getF_erledigungsgrad() {
        return this.f_erledigungsgrad;
    }

    public void setF_erledigungsgrad(Double f_erledigungsgrad) {
        this.f_erledigungsgrad = f_erledigungsgrad;
    }

    public BigDecimal getN_dauer_geplant() {
        return this.n_dauer_geplant;
    }

    public void setN_dauer_geplant(BigDecimal n_dauer_geplant) {
        this.n_dauer_geplant = n_dauer_geplant;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRHistoryart getFlrhistoryart() {
        return this.flrhistoryart;
    }

    public void setFlrhistoryart(com.lp.server.projekt.fastlanereader.generated.FLRHistoryart flrhistoryart) {
        this.flrhistoryart = flrhistoryart;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRPersonal getFlrpersonal_aendern() {
        return this.flrpersonal_aendern;
    }

    public void setFlrpersonal_aendern(FLRPersonal flrpersonal_aendern) {
        this.flrpersonal_aendern = flrpersonal_aendern;
    }

    public FLRPersonal getFlrpersonal_wirddurchgefuehrtvon() {
        return this.flrpersonal_wirddurchgefuehrtvon;
    }

    public void setFlrpersonal_wirddurchgefuehrtvon(FLRPersonal flrpersonal_wirddurchgefuehrtvon) {
        this.flrpersonal_wirddurchgefuehrtvon = flrpersonal_wirddurchgefuehrtvon;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
