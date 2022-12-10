package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellager implements Serializable {

    /** identifier field */
    private WwArtikellagerPK compId;

    /** persistent field */
    private BigDecimal n_lagerstand;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private Double f_lagersoll;

    /** persistent field */
    private BigDecimal n_gestehungspreis;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste;

    /** full constructor */
    public FLRArtikellager(WwArtikellagerPK compId, BigDecimal n_lagerstand, Double f_lagermindest, Double f_lagersoll, BigDecimal n_gestehungspreis, String mandant_c_nr, com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste) {
        this.compId = compId;
        this.n_lagerstand = n_lagerstand;
        this.f_lagermindest = f_lagermindest;
        this.f_lagersoll = f_lagersoll;
        this.n_gestehungspreis = n_gestehungspreis;
        this.mandant_c_nr = mandant_c_nr;
        this.flrlager = flrlager;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
    }

    /** default constructor */
    public FLRArtikellager() {
    }

    /** minimal constructor */
    public FLRArtikellager(WwArtikellagerPK compId, BigDecimal n_lagerstand, BigDecimal n_gestehungspreis, String mandant_c_nr) {
        this.compId = compId;
        this.n_lagerstand = n_lagerstand;
        this.n_gestehungspreis = n_gestehungspreis;
        this.mandant_c_nr = mandant_c_nr;
    }

    public WwArtikellagerPK getCompId() {
        return this.compId;
    }

    public void setCompId(WwArtikellagerPK compId) {
        this.compId = compId;
    }

    public BigDecimal getN_lagerstand() {
        return this.n_lagerstand;
    }

    public void setN_lagerstand(BigDecimal n_lagerstand) {
        this.n_lagerstand = n_lagerstand;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
    }

    public Double getF_lagersoll() {
        return this.f_lagersoll;
    }

    public void setF_lagersoll(Double f_lagersoll) {
        this.f_lagersoll = f_lagersoll;
    }

    public BigDecimal getN_gestehungspreis() {
        return this.n_gestehungspreis;
    }

    public void setN_gestehungspreis(BigDecimal n_gestehungspreis) {
        this.n_gestehungspreis = n_gestehungspreis;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compId", getCompId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRArtikellager) ) return false;
        FLRArtikellager castOther = (FLRArtikellager) other;
        return new EqualsBuilder()
            .append(this.getCompId(), castOther.getCompId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCompId())
            .toHashCode();
    }

}
