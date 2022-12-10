package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellungAuftragsbestaetigung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String c_bezeichnung;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String bestellpositionart_c_nr;

    /** nullable persistent field */
    private String bestellpositionstatus_c_nr;

    /** nullable persistent field */
    private Date t_auftragsbestaetigungstermin;

    /** nullable persistent field */
    private Date t_lieferterminbestaetigt;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** full constructor */
    public FLRBestellungAuftragsbestaetigung(Integer i_sort, BigDecimal n_menge, String c_bezeichnung, String einheit_c_nr, String bestellpositionart_c_nr, String bestellpositionstatus_c_nr, Date t_auftragsbestaetigungstermin, Date t_lieferterminbestaetigt, Date t_uebersteuerterliefertermin, BigDecimal n_nettogesamtpreis, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRArtikel flrartikel, FLRMediastandard flrmediastandard) {
        this.i_sort = i_sort;
        this.n_menge = n_menge;
        this.c_bezeichnung = c_bezeichnung;
        this.einheit_c_nr = einheit_c_nr;
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.flrbestellung = flrbestellung;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
    }

    /** default constructor */
    public FLRBestellungAuftragsbestaetigung() {
    }

    /** minimal constructor */
    public FLRBestellungAuftragsbestaetigung(Integer i_sort) {
        this.i_sort = i_sort;
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

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public String getC_bezeichnung() {
        return this.c_bezeichnung;
    }

    public void setC_bezeichnung(String c_bezeichnung) {
        this.c_bezeichnung = c_bezeichnung;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getBestellpositionart_c_nr() {
        return this.bestellpositionart_c_nr;
    }

    public void setBestellpositionart_c_nr(String bestellpositionart_c_nr) {
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
    }

    public String getBestellpositionstatus_c_nr() {
        return this.bestellpositionstatus_c_nr;
    }

    public void setBestellpositionstatus_c_nr(String bestellpositionstatus_c_nr) {
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
    }

    public Date getT_auftragsbestaetigungstermin() {
        return this.t_auftragsbestaetigungstermin;
    }

    public void setT_auftragsbestaetigungstermin(Date t_auftragsbestaetigungstermin) {
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
    }

    public Date getT_lieferterminbestaetigt() {
        return this.t_lieferterminbestaetigt;
    }

    public void setT_lieferterminbestaetigt(Date t_lieferterminbestaetigt) {
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMediastandard getFlrmediastandard() {
        return this.flrmediastandard;
    }

    public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
        this.flrmediastandard = flrmediastandard;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
