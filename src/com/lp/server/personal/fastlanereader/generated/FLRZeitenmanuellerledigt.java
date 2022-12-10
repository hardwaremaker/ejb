package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitenmanuellerledigt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer zeitdaten_i_id;

    /** nullable persistent field */
    private Integer maschinenzeitdaten_i_id;

    /** nullable persistent field */
    private Integer reise_i_id;

    /** nullable persistent field */
    private Integer telefonzeiten_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten flrmaschinenzeitdaten;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRReise flrreise;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten;

    /** full constructor */
    public FLRZeitenmanuellerledigt(Integer zeitdaten_i_id, Integer maschinenzeitdaten_i_id, Integer reise_i_id, Integer telefonzeiten_i_id, com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten, com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten flrmaschinenzeitdaten, com.lp.server.personal.fastlanereader.generated.FLRReise flrreise, com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten) {
        this.zeitdaten_i_id = zeitdaten_i_id;
        this.maschinenzeitdaten_i_id = maschinenzeitdaten_i_id;
        this.reise_i_id = reise_i_id;
        this.telefonzeiten_i_id = telefonzeiten_i_id;
        this.flrzeitdaten = flrzeitdaten;
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
        this.flrreise = flrreise;
        this.flrtelefonzeiten = flrtelefonzeiten;
    }

    /** default constructor */
    public FLRZeitenmanuellerledigt() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getZeitdaten_i_id() {
        return this.zeitdaten_i_id;
    }

    public void setZeitdaten_i_id(Integer zeitdaten_i_id) {
        this.zeitdaten_i_id = zeitdaten_i_id;
    }

    public Integer getMaschinenzeitdaten_i_id() {
        return this.maschinenzeitdaten_i_id;
    }

    public void setMaschinenzeitdaten_i_id(Integer maschinenzeitdaten_i_id) {
        this.maschinenzeitdaten_i_id = maschinenzeitdaten_i_id;
    }

    public Integer getReise_i_id() {
        return this.reise_i_id;
    }

    public void setReise_i_id(Integer reise_i_id) {
        this.reise_i_id = reise_i_id;
    }

    public Integer getTelefonzeiten_i_id() {
        return this.telefonzeiten_i_id;
    }

    public void setTelefonzeiten_i_id(Integer telefonzeiten_i_id) {
        this.telefonzeiten_i_id = telefonzeiten_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRZeitdaten getFlrzeitdaten() {
        return this.flrzeitdaten;
    }

    public void setFlrzeitdaten(com.lp.server.personal.fastlanereader.generated.FLRZeitdaten flrzeitdaten) {
        this.flrzeitdaten = flrzeitdaten;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten getFlrmaschinenzeitdaten() {
        return this.flrmaschinenzeitdaten;
    }

    public void setFlrmaschinenzeitdaten(com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten flrmaschinenzeitdaten) {
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRReise getFlrreise() {
        return this.flrreise;
    }

    public void setFlrreise(com.lp.server.personal.fastlanereader.generated.FLRReise flrreise) {
        this.flrreise = flrreise;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten getFlrtelefonzeiten() {
        return this.flrtelefonzeiten;
    }

    public void setFlrtelefonzeiten(com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten) {
        this.flrtelefonzeiten = flrtelefonzeiten;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
