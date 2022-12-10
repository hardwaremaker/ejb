package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstklposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private String agstklpositionsart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_position;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis;

    /** nullable persistent field */
    private BigDecimal n_gestehungspreis;

    /** nullable persistent field */
    private Short b_drucken;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** full constructor */
    public FLRAgstklposition(Integer i_sort, Integer agstkl_i_id, String agstklpositionsart_c_nr, BigDecimal n_menge, String einheit_c_nr, String c_position, String c_bez, BigDecimal n_nettogesamtpreis, BigDecimal n_nettoeinzelpreis, BigDecimal n_gestehungspreis, Short b_drucken, com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste) {
        this.i_sort = i_sort;
        this.agstkl_i_id = agstkl_i_id;
        this.agstklpositionsart_c_nr = agstklpositionsart_c_nr;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_position = c_position;
        this.c_bez = c_bez;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
        this.n_gestehungspreis = n_gestehungspreis;
        this.b_drucken = b_drucken;
        this.flragstkl = flragstkl;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
    }

    /** default constructor */
    public FLRAgstklposition() {
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

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
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

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getC_position() {
        return this.c_position;
    }

    public void setC_position(String c_position) {
        this.c_position = c_position;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public BigDecimal getN_nettoeinzelpreis() {
        return this.n_nettoeinzelpreis;
    }

    public void setN_nettoeinzelpreis(BigDecimal n_nettoeinzelpreis) {
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
    }

    public BigDecimal getN_gestehungspreis() {
        return this.n_gestehungspreis;
    }

    public void setN_gestehungspreis(BigDecimal n_gestehungspreis) {
        this.n_gestehungspreis = n_gestehungspreis;
    }

    public Short getB_drucken() {
        return this.b_drucken;
    }

    public void setB_drucken(Short b_drucken) {
        this.b_drucken = b_drucken;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
