package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLohnartstundenfaktor implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Double f_faktor;

    /** nullable persistent field */
    private Integer zeitmodell_i_id;

    /** nullable persistent field */
    private Integer taetigkeit_i_id;

    /** nullable persistent field */
    private Integer schichtzeit_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell;

    /** full constructor */
    public FLRLohnartstundenfaktor(Double f_faktor, Integer zeitmodell_i_id, Integer taetigkeit_i_id, Integer schichtzeit_i_id, com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart, com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart, com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell) {
        this.f_faktor = f_faktor;
        this.zeitmodell_i_id = zeitmodell_i_id;
        this.taetigkeit_i_id = taetigkeit_i_id;
        this.schichtzeit_i_id = schichtzeit_i_id;
        this.flrlohnart = flrlohnart;
        this.flrtagesart = flrtagesart;
        this.flrlohnstundenart = flrlohnstundenart;
        this.flrzeitmodell = flrzeitmodell;
    }

    /** default constructor */
    public FLRLohnartstundenfaktor() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Double getF_faktor() {
        return this.f_faktor;
    }

    public void setF_faktor(Double f_faktor) {
        this.f_faktor = f_faktor;
    }

    public Integer getZeitmodell_i_id() {
        return this.zeitmodell_i_id;
    }

    public void setZeitmodell_i_id(Integer zeitmodell_i_id) {
        this.zeitmodell_i_id = zeitmodell_i_id;
    }

    public Integer getTaetigkeit_i_id() {
        return this.taetigkeit_i_id;
    }

    public void setTaetigkeit_i_id(Integer taetigkeit_i_id) {
        this.taetigkeit_i_id = taetigkeit_i_id;
    }

    public Integer getSchichtzeit_i_id() {
        return this.schichtzeit_i_id;
    }

    public void setSchichtzeit_i_id(Integer schichtzeit_i_id) {
        this.schichtzeit_i_id = schichtzeit_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRLohnart getFlrlohnart() {
        return this.flrlohnart;
    }

    public void setFlrlohnart(com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart) {
        this.flrlohnart = flrlohnart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart getFlrlohnstundenart() {
        return this.flrlohnstundenart;
    }

    public void setFlrlohnstundenart(com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart) {
        this.flrlohnstundenart = flrlohnstundenart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRZeitmodell getFlrzeitmodell() {
        return this.flrzeitmodell;
    }

    public void setFlrzeitmodell(com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell) {
        this.flrzeitmodell = flrzeitmodell;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
