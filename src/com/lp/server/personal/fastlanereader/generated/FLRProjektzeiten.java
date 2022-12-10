package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjektzeiten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer zeitdaten_i_id;

    /** nullable persistent field */
    private Integer telefonzeiten_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeit;

    /** full constructor */
    public FLRProjektzeiten(Date t_zeit, Integer personal_i_id, Integer zeitdaten_i_id, Integer telefonzeiten_i_id, Integer projekt_i_id, Double f_verrechenbar, String c_bemerkung, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, FLRProjekt flrprojekt, com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten, com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeit) {
        this.t_zeit = t_zeit;
        this.personal_i_id = personal_i_id;
        this.zeitdaten_i_id = zeitdaten_i_id;
        this.telefonzeiten_i_id = telefonzeiten_i_id;
        this.projekt_i_id = projekt_i_id;
        this.f_verrechenbar = f_verrechenbar;
        this.c_bemerkung = c_bemerkung;
        this.flrpersonal = flrpersonal;
        this.flrprojekt = flrprojekt;
        this.flrzeitdaten = flrzeitdaten;
        this.flrtelefonzeit = flrtelefonzeit;
    }

    /** default constructor */
    public FLRProjektzeiten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getZeitdaten_i_id() {
        return this.zeitdaten_i_id;
    }

    public void setZeitdaten_i_id(Integer zeitdaten_i_id) {
        this.zeitdaten_i_id = zeitdaten_i_id;
    }

    public Integer getTelefonzeiten_i_id() {
        return this.telefonzeiten_i_id;
    }

    public void setTelefonzeiten_i_id(Integer telefonzeiten_i_id) {
        this.telefonzeiten_i_id = telefonzeiten_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRZeitdaten getFlrzeitdaten() {
        return this.flrzeitdaten;
    }

    public void setFlrzeitdaten(com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten) {
        this.flrzeitdaten = flrzeitdaten;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten getFlrtelefonzeit() {
        return this.flrtelefonzeit;
    }

    public void setFlrtelefonzeit(com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeit) {
        this.flrtelefonzeit = flrtelefonzeit;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
