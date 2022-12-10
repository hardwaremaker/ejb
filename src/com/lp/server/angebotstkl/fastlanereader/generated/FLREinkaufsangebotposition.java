package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREinkaufsangebotposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer einkaufsangebot_i_id;

    /** nullable persistent field */
    private String agstklpositionsart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_preis1;

    /** nullable persistent field */
    private BigDecimal n_preis2;

    /** nullable persistent field */
    private BigDecimal n_preis3;

    /** nullable persistent field */
    private BigDecimal n_preis4;

    /** nullable persistent field */
    private BigDecimal n_preis5;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private String c_artikelnrhersteller;

    /** nullable persistent field */
    private String c_position;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private Short b_mitdrucken;

    /** nullable persistent field */
    private String c_kommentar1;

    /** nullable persistent field */
    private String c_kommentar2;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** full constructor */
    public FLREinkaufsangebotposition(Integer i_sort, Integer einkaufsangebot_i_id, String agstklpositionsart_c_nr, BigDecimal n_menge, BigDecimal n_preis1, BigDecimal n_preis2, BigDecimal n_preis3, BigDecimal n_preis4, BigDecimal n_preis5, String einheit_c_nr, String c_bez, String c_zbez, String c_artikelnrhersteller, String c_position, String c_bemerkung, Short b_mitdrucken, String c_kommentar1, String c_kommentar2, com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot, FLRArtikelliste flrartikel) {
        this.i_sort = i_sort;
        this.einkaufsangebot_i_id = einkaufsangebot_i_id;
        this.agstklpositionsart_c_nr = agstklpositionsart_c_nr;
        this.n_menge = n_menge;
        this.n_preis1 = n_preis1;
        this.n_preis2 = n_preis2;
        this.n_preis3 = n_preis3;
        this.n_preis4 = n_preis4;
        this.n_preis5 = n_preis5;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.c_artikelnrhersteller = c_artikelnrhersteller;
        this.c_position = c_position;
        this.c_bemerkung = c_bemerkung;
        this.b_mitdrucken = b_mitdrucken;
        this.c_kommentar1 = c_kommentar1;
        this.c_kommentar2 = c_kommentar2;
        this.flreinkaufsangebot = flreinkaufsangebot;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLREinkaufsangebotposition() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getEinkaufsangebot_i_id() {
        return this.einkaufsangebot_i_id;
    }

    public void setEinkaufsangebot_i_id(Integer einkaufsangebot_i_id) {
        this.einkaufsangebot_i_id = einkaufsangebot_i_id;
    }

    public String getAgstklpositionsart_c_nr() {
        return this.agstklpositionsart_c_nr;
    }

    public void setAgstklpositionsart_c_nr(String agstklpositionsart_c_nr) {
        this.agstklpositionsart_c_nr = agstklpositionsart_c_nr;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_preis1() {
        return this.n_preis1;
    }

    public void setN_preis1(BigDecimal n_preis1) {
        this.n_preis1 = n_preis1;
    }

    public BigDecimal getN_preis2() {
        return this.n_preis2;
    }

    public void setN_preis2(BigDecimal n_preis2) {
        this.n_preis2 = n_preis2;
    }

    public BigDecimal getN_preis3() {
        return this.n_preis3;
    }

    public void setN_preis3(BigDecimal n_preis3) {
        this.n_preis3 = n_preis3;
    }

    public BigDecimal getN_preis4() {
        return this.n_preis4;
    }

    public void setN_preis4(BigDecimal n_preis4) {
        this.n_preis4 = n_preis4;
    }

    public BigDecimal getN_preis5() {
        return this.n_preis5;
    }

    public void setN_preis5(BigDecimal n_preis5) {
        this.n_preis5 = n_preis5;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public String getC_artikelnrhersteller() {
        return this.c_artikelnrhersteller;
    }

    public void setC_artikelnrhersteller(String c_artikelnrhersteller) {
        this.c_artikelnrhersteller = c_artikelnrhersteller;
    }

    public String getC_position() {
        return this.c_position;
    }

    public void setC_position(String c_position) {
        this.c_position = c_position;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public Short getB_mitdrucken() {
        return this.b_mitdrucken;
    }

    public void setB_mitdrucken(Short b_mitdrucken) {
        this.b_mitdrucken = b_mitdrucken;
    }

    public String getC_kommentar1() {
        return this.c_kommentar1;
    }

    public void setC_kommentar1(String c_kommentar1) {
        this.c_kommentar1 = c_kommentar1;
    }

    public String getC_kommentar2() {
        return this.c_kommentar2;
    }

    public void setC_kommentar2(String c_kommentar2) {
        this.c_kommentar2 = c_kommentar2;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot getFlreinkaufsangebot() {
        return this.flreinkaufsangebot;
    }

    public void setFlreinkaufsangebot(com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot) {
        this.flreinkaufsangebot = flreinkaufsangebot;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
