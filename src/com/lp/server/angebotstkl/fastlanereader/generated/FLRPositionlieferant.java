package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPositionlieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer einkaufsangebotposition_i_id;

    /** nullable persistent field */
    private Integer ekaglieferant_i_id;

    /** nullable persistent field */
    private String c_artikelnrlieferant;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private String c_bemerkung_intern;

    /** nullable persistent field */
    private String c_bemerkung_verkauf;

    /** nullable persistent field */
    private BigDecimal n_preis_menge1;

    /** nullable persistent field */
    private BigDecimal n_preis_menge2;

    /** nullable persistent field */
    private BigDecimal n_preis_menge3;

    /** nullable persistent field */
    private BigDecimal n_preis_menge4;

    /** nullable persistent field */
    private BigDecimal n_preis_menge5;

    /** nullable persistent field */
    private BigDecimal n_lagerstand;

    /** nullable persistent field */
    private BigDecimal n_mindestbestellmenge;

    /** nullable persistent field */
    private BigDecimal n_tranportkosten;

    /** nullable persistent field */
    private BigDecimal n_verpackungseinheit;

    /** nullable persistent field */
    private Integer i_lieferzeitinkw;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Short b_menge1_bestellen;

    /** nullable persistent field */
    private Short b_menge2_bestellen;

    /** nullable persistent field */
    private Short b_menge3_bestellen;

    /** nullable persistent field */
    private Short b_menge4_bestellen;

    /** nullable persistent field */
    private Short b_menge5_bestellen;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition flreinkaufsangebotposition;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant flrekaglieferant;

    /** full constructor */
    public FLRPositionlieferant(Integer einkaufsangebotposition_i_id, Integer ekaglieferant_i_id, String c_artikelnrlieferant, String c_bemerkung, String c_bemerkung_intern, String c_bemerkung_verkauf, BigDecimal n_preis_menge1, BigDecimal n_preis_menge2, BigDecimal n_preis_menge3, BigDecimal n_preis_menge4, BigDecimal n_preis_menge5, BigDecimal n_lagerstand, BigDecimal n_mindestbestellmenge, BigDecimal n_tranportkosten, BigDecimal n_verpackungseinheit, Integer i_lieferzeitinkw, Date t_aendern, Short b_menge1_bestellen, Short b_menge2_bestellen, Short b_menge3_bestellen, Short b_menge4_bestellen, Short b_menge5_bestellen, com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition flreinkaufsangebotposition, com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant flrekaglieferant) {
        this.einkaufsangebotposition_i_id = einkaufsangebotposition_i_id;
        this.ekaglieferant_i_id = ekaglieferant_i_id;
        this.c_artikelnrlieferant = c_artikelnrlieferant;
        this.c_bemerkung = c_bemerkung;
        this.c_bemerkung_intern = c_bemerkung_intern;
        this.c_bemerkung_verkauf = c_bemerkung_verkauf;
        this.n_preis_menge1 = n_preis_menge1;
        this.n_preis_menge2 = n_preis_menge2;
        this.n_preis_menge3 = n_preis_menge3;
        this.n_preis_menge4 = n_preis_menge4;
        this.n_preis_menge5 = n_preis_menge5;
        this.n_lagerstand = n_lagerstand;
        this.n_mindestbestellmenge = n_mindestbestellmenge;
        this.n_tranportkosten = n_tranportkosten;
        this.n_verpackungseinheit = n_verpackungseinheit;
        this.i_lieferzeitinkw = i_lieferzeitinkw;
        this.t_aendern = t_aendern;
        this.b_menge1_bestellen = b_menge1_bestellen;
        this.b_menge2_bestellen = b_menge2_bestellen;
        this.b_menge3_bestellen = b_menge3_bestellen;
        this.b_menge4_bestellen = b_menge4_bestellen;
        this.b_menge5_bestellen = b_menge5_bestellen;
        this.flreinkaufsangebotposition = flreinkaufsangebotposition;
        this.flrekaglieferant = flrekaglieferant;
    }

    /** default constructor */
    public FLRPositionlieferant() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEinkaufsangebotposition_i_id() {
        return this.einkaufsangebotposition_i_id;
    }

    public void setEinkaufsangebotposition_i_id(Integer einkaufsangebotposition_i_id) {
        this.einkaufsangebotposition_i_id = einkaufsangebotposition_i_id;
    }

    public Integer getEkaglieferant_i_id() {
        return this.ekaglieferant_i_id;
    }

    public void setEkaglieferant_i_id(Integer ekaglieferant_i_id) {
        this.ekaglieferant_i_id = ekaglieferant_i_id;
    }

    public String getC_artikelnrlieferant() {
        return this.c_artikelnrlieferant;
    }

    public void setC_artikelnrlieferant(String c_artikelnrlieferant) {
        this.c_artikelnrlieferant = c_artikelnrlieferant;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public String getC_bemerkung_intern() {
        return this.c_bemerkung_intern;
    }

    public void setC_bemerkung_intern(String c_bemerkung_intern) {
        this.c_bemerkung_intern = c_bemerkung_intern;
    }

    public String getC_bemerkung_verkauf() {
        return this.c_bemerkung_verkauf;
    }

    public void setC_bemerkung_verkauf(String c_bemerkung_verkauf) {
        this.c_bemerkung_verkauf = c_bemerkung_verkauf;
    }

    public BigDecimal getN_preis_menge1() {
        return this.n_preis_menge1;
    }

    public void setN_preis_menge1(BigDecimal n_preis_menge1) {
        this.n_preis_menge1 = n_preis_menge1;
    }

    public BigDecimal getN_preis_menge2() {
        return this.n_preis_menge2;
    }

    public void setN_preis_menge2(BigDecimal n_preis_menge2) {
        this.n_preis_menge2 = n_preis_menge2;
    }

    public BigDecimal getN_preis_menge3() {
        return this.n_preis_menge3;
    }

    public void setN_preis_menge3(BigDecimal n_preis_menge3) {
        this.n_preis_menge3 = n_preis_menge3;
    }

    public BigDecimal getN_preis_menge4() {
        return this.n_preis_menge4;
    }

    public void setN_preis_menge4(BigDecimal n_preis_menge4) {
        this.n_preis_menge4 = n_preis_menge4;
    }

    public BigDecimal getN_preis_menge5() {
        return this.n_preis_menge5;
    }

    public void setN_preis_menge5(BigDecimal n_preis_menge5) {
        this.n_preis_menge5 = n_preis_menge5;
    }

    public BigDecimal getN_lagerstand() {
        return this.n_lagerstand;
    }

    public void setN_lagerstand(BigDecimal n_lagerstand) {
        this.n_lagerstand = n_lagerstand;
    }

    public BigDecimal getN_mindestbestellmenge() {
        return this.n_mindestbestellmenge;
    }

    public void setN_mindestbestellmenge(BigDecimal n_mindestbestellmenge) {
        this.n_mindestbestellmenge = n_mindestbestellmenge;
    }

    public BigDecimal getN_tranportkosten() {
        return this.n_tranportkosten;
    }

    public void setN_tranportkosten(BigDecimal n_tranportkosten) {
        this.n_tranportkosten = n_tranportkosten;
    }

    public BigDecimal getN_verpackungseinheit() {
        return this.n_verpackungseinheit;
    }

    public void setN_verpackungseinheit(BigDecimal n_verpackungseinheit) {
        this.n_verpackungseinheit = n_verpackungseinheit;
    }

    public Integer getI_lieferzeitinkw() {
        return this.i_lieferzeitinkw;
    }

    public void setI_lieferzeitinkw(Integer i_lieferzeitinkw) {
        this.i_lieferzeitinkw = i_lieferzeitinkw;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Short getB_menge1_bestellen() {
        return this.b_menge1_bestellen;
    }

    public void setB_menge1_bestellen(Short b_menge1_bestellen) {
        this.b_menge1_bestellen = b_menge1_bestellen;
    }

    public Short getB_menge2_bestellen() {
        return this.b_menge2_bestellen;
    }

    public void setB_menge2_bestellen(Short b_menge2_bestellen) {
        this.b_menge2_bestellen = b_menge2_bestellen;
    }

    public Short getB_menge3_bestellen() {
        return this.b_menge3_bestellen;
    }

    public void setB_menge3_bestellen(Short b_menge3_bestellen) {
        this.b_menge3_bestellen = b_menge3_bestellen;
    }

    public Short getB_menge4_bestellen() {
        return this.b_menge4_bestellen;
    }

    public void setB_menge4_bestellen(Short b_menge4_bestellen) {
        this.b_menge4_bestellen = b_menge4_bestellen;
    }

    public Short getB_menge5_bestellen() {
        return this.b_menge5_bestellen;
    }

    public void setB_menge5_bestellen(Short b_menge5_bestellen) {
        this.b_menge5_bestellen = b_menge5_bestellen;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition getFlreinkaufsangebotposition() {
        return this.flreinkaufsangebotposition;
    }

    public void setFlreinkaufsangebotposition(com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition flreinkaufsangebotposition) {
        this.flreinkaufsangebotposition = flreinkaufsangebotposition;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant getFlrekaglieferant() {
        return this.flrekaglieferant;
    }

    public void setFlrekaglieferant(com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant flrekaglieferant) {
        this.flrekaglieferant = flrekaglieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
