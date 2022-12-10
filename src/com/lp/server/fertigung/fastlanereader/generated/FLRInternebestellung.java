package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRInternebestellung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private Integer i_belegiid;

    /** nullable persistent field */
    private Integer i_belegpositioniid;

    /** nullable persistent field */
    private Integer auftrag_i_id_kopfauftrag;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private String x_ausloeser;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRPartner flrpartner_standort;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag_kopfauftrag;

    /** full constructor */
    public FLRInternebestellung(String mandant_c_nr, String belegart_c_nr, Integer i_belegiid, Integer i_belegpositioniid, Integer auftrag_i_id_kopfauftrag, Integer stueckliste_i_id, BigDecimal n_menge, Date t_liefertermin, String x_ausloeser, Double f_lagermindest, FLRStueckliste flrstueckliste, FLRPartner flrpartner_standort, FLRAuftrag flrauftrag_kopfauftrag) {
        this.mandant_c_nr = mandant_c_nr;
        this.belegart_c_nr = belegart_c_nr;
        this.i_belegiid = i_belegiid;
        this.i_belegpositioniid = i_belegpositioniid;
        this.auftrag_i_id_kopfauftrag = auftrag_i_id_kopfauftrag;
        this.stueckliste_i_id = stueckliste_i_id;
        this.n_menge = n_menge;
        this.t_liefertermin = t_liefertermin;
        this.x_ausloeser = x_ausloeser;
        this.f_lagermindest = f_lagermindest;
        this.flrstueckliste = flrstueckliste;
        this.flrpartner_standort = flrpartner_standort;
        this.flrauftrag_kopfauftrag = flrauftrag_kopfauftrag;
    }

    /** default constructor */
    public FLRInternebestellung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public Integer getI_belegiid() {
        return this.i_belegiid;
    }

    public void setI_belegiid(Integer i_belegiid) {
        this.i_belegiid = i_belegiid;
    }

    public Integer getI_belegpositioniid() {
        return this.i_belegpositioniid;
    }

    public void setI_belegpositioniid(Integer i_belegpositioniid) {
        this.i_belegpositioniid = i_belegpositioniid;
    }

    public Integer getAuftrag_i_id_kopfauftrag() {
        return this.auftrag_i_id_kopfauftrag;
    }

    public void setAuftrag_i_id_kopfauftrag(Integer auftrag_i_id_kopfauftrag) {
        this.auftrag_i_id_kopfauftrag = auftrag_i_id_kopfauftrag;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public String getX_ausloeser() {
        return this.x_ausloeser;
    }

    public void setX_ausloeser(String x_ausloeser) {
        this.x_ausloeser = x_ausloeser;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
    }

    public FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRPartner getFlrpartner_standort() {
        return this.flrpartner_standort;
    }

    public void setFlrpartner_standort(FLRPartner flrpartner_standort) {
        this.flrpartner_standort = flrpartner_standort;
    }

    public FLRAuftrag getFlrauftrag_kopfauftrag() {
        return this.flrauftrag_kopfauftrag;
    }

    public void setFlrauftrag_kopfauftrag(FLRAuftrag flrauftrag_kopfauftrag) {
        this.flrauftrag_kopfauftrag = flrauftrag_kopfauftrag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
