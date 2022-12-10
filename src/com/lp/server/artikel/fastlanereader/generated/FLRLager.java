package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLager implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String lagerart_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_lagerstand_bei_0_anzeigen;

    /** nullable persistent field */
    private Short b_bestellvorschlag;

    /** nullable persistent field */
    private Short b_internebestellung;

    /** nullable persistent field */
    private Short b_konsignationslager;

    /** nullable persistent field */
    private Integer i_loslagersort;

    /** nullable persistent field */
    private Integer parnter_i_id_standort;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** persistent field */
    private Set artikellagerset;

    /** full constructor */
    public FLRLager(String c_nr, String mandant_c_nr, String lagerart_c_nr, Short b_versteckt, Short b_lagerstand_bei_0_anzeigen, Short b_bestellvorschlag, Short b_internebestellung, Short b_konsignationslager, Integer i_loslagersort, Integer parnter_i_id_standort, Integer i_sort, FLRPartner flrpartner, Set artikellagerset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.lagerart_c_nr = lagerart_c_nr;
        this.b_versteckt = b_versteckt;
        this.b_lagerstand_bei_0_anzeigen = b_lagerstand_bei_0_anzeigen;
        this.b_bestellvorschlag = b_bestellvorschlag;
        this.b_internebestellung = b_internebestellung;
        this.b_konsignationslager = b_konsignationslager;
        this.i_loslagersort = i_loslagersort;
        this.parnter_i_id_standort = parnter_i_id_standort;
        this.i_sort = i_sort;
        this.flrpartner = flrpartner;
        this.artikellagerset = artikellagerset;
    }

    /** default constructor */
    public FLRLager() {
    }

    /** minimal constructor */
    public FLRLager(String c_nr, String mandant_c_nr, Set artikellagerset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.artikellagerset = artikellagerset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getLagerart_c_nr() {
        return this.lagerart_c_nr;
    }

    public void setLagerart_c_nr(String lagerart_c_nr) {
        this.lagerart_c_nr = lagerart_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_lagerstand_bei_0_anzeigen() {
        return this.b_lagerstand_bei_0_anzeigen;
    }

    public void setB_lagerstand_bei_0_anzeigen(Short b_lagerstand_bei_0_anzeigen) {
        this.b_lagerstand_bei_0_anzeigen = b_lagerstand_bei_0_anzeigen;
    }

    public Short getB_bestellvorschlag() {
        return this.b_bestellvorschlag;
    }

    public void setB_bestellvorschlag(Short b_bestellvorschlag) {
        this.b_bestellvorschlag = b_bestellvorschlag;
    }

    public Short getB_internebestellung() {
        return this.b_internebestellung;
    }

    public void setB_internebestellung(Short b_internebestellung) {
        this.b_internebestellung = b_internebestellung;
    }

    public Short getB_konsignationslager() {
        return this.b_konsignationslager;
    }

    public void setB_konsignationslager(Short b_konsignationslager) {
        this.b_konsignationslager = b_konsignationslager;
    }

    public Integer getI_loslagersort() {
        return this.i_loslagersort;
    }

    public void setI_loslagersort(Integer i_loslagersort) {
        this.i_loslagersort = i_loslagersort;
    }

    public Integer getParnter_i_id_standort() {
        return this.parnter_i_id_standort;
    }

    public void setParnter_i_id_standort(Integer parnter_i_id_standort) {
        this.parnter_i_id_standort = parnter_i_id_standort;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public Set getArtikellagerset() {
        return this.artikellagerset;
    }

    public void setArtikellagerset(Set artikellagerset) {
        this.artikellagerset = artikellagerset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
