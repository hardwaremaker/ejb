package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRGebinde;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellpositionReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bezeichnung;

    /** nullable persistent field */
    private String c_zusatzbezeichnung;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String bestellpositionstatus_c_nr;

    /** nullable persistent field */
    private String bestellpositionart_c_nr;

    /** nullable persistent field */
    private Timestamp t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Date t_lieferterminbestaetigt;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private Timestamp t_auftragsbestaetigungstermin;

    /** nullable persistent field */
    private String c_abnummer;

    /** nullable persistent field */
    private String c_abkommentar;

    /** nullable persistent field */
    private Date t_abursprungstermin;

    /** nullable persistent field */
    private BigDecimal n_fixkosten;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private Integer gebinde_i_id;

    /** nullable persistent field */
    private BigDecimal n_anzahlgebinde;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRGebinde flrgebinde;

    /** persistent field */
    private Set abrufpositionenset;

    /** full constructor */
    public FLRBestellpositionReport(Integer i_sort, BigDecimal n_menge, String einheit_c_nr, String c_bezeichnung, String c_zusatzbezeichnung, BigDecimal n_nettogesamtpreis, String bestellpositionstatus_c_nr, String bestellpositionart_c_nr, Timestamp t_uebersteuerterliefertermin, Date t_lieferterminbestaetigt, BigDecimal n_offenemenge, Timestamp t_auftragsbestaetigungstermin, String c_abnummer, String c_abkommentar, Date t_abursprungstermin, BigDecimal n_fixkosten, Integer position_i_id_artikelset, Integer gebinde_i_id, BigDecimal n_anzahlgebinde, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, FLRGebinde flrgebinde, Set abrufpositionenset) {
        this.i_sort = i_sort;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bezeichnung = c_bezeichnung;
        this.c_zusatzbezeichnung = c_zusatzbezeichnung;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
        this.n_offenemenge = n_offenemenge;
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
        this.c_abnummer = c_abnummer;
        this.c_abkommentar = c_abkommentar;
        this.t_abursprungstermin = t_abursprungstermin;
        this.n_fixkosten = n_fixkosten;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.gebinde_i_id = gebinde_i_id;
        this.n_anzahlgebinde = n_anzahlgebinde;
        this.flrbestellung = flrbestellung;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrgebinde = flrgebinde;
        this.abrufpositionenset = abrufpositionenset;
    }

    /** default constructor */
    public FLRBestellpositionReport() {
    }

    /** minimal constructor */
    public FLRBestellpositionReport(Integer i_sort, Set abrufpositionenset) {
        this.i_sort = i_sort;
        this.abrufpositionenset = abrufpositionenset;
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

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getC_bezeichnung() {
        return this.c_bezeichnung;
    }

    public void setC_bezeichnung(String c_bezeichnung) {
        this.c_bezeichnung = c_bezeichnung;
    }

    public String getC_zusatzbezeichnung() {
        return this.c_zusatzbezeichnung;
    }

    public void setC_zusatzbezeichnung(String c_zusatzbezeichnung) {
        this.c_zusatzbezeichnung = c_zusatzbezeichnung;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getBestellpositionstatus_c_nr() {
        return this.bestellpositionstatus_c_nr;
    }

    public void setBestellpositionstatus_c_nr(String bestellpositionstatus_c_nr) {
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
    }

    public String getBestellpositionart_c_nr() {
        return this.bestellpositionart_c_nr;
    }

    public void setBestellpositionart_c_nr(String bestellpositionart_c_nr) {
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
    }

    public Timestamp getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Timestamp t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Date getT_lieferterminbestaetigt() {
        return this.t_lieferterminbestaetigt;
    }

    public void setT_lieferterminbestaetigt(Date t_lieferterminbestaetigt) {
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
    }

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public Timestamp getT_auftragsbestaetigungstermin() {
        return this.t_auftragsbestaetigungstermin;
    }

    public void setT_auftragsbestaetigungstermin(Timestamp t_auftragsbestaetigungstermin) {
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
    }

    public String getC_abnummer() {
        return this.c_abnummer;
    }

    public void setC_abnummer(String c_abnummer) {
        this.c_abnummer = c_abnummer;
    }

    public String getC_abkommentar() {
        return this.c_abkommentar;
    }

    public void setC_abkommentar(String c_abkommentar) {
        this.c_abkommentar = c_abkommentar;
    }

    public Date getT_abursprungstermin() {
        return this.t_abursprungstermin;
    }

    public void setT_abursprungstermin(Date t_abursprungstermin) {
        this.t_abursprungstermin = t_abursprungstermin;
    }

    public BigDecimal getN_fixkosten() {
        return this.n_fixkosten;
    }

    public void setN_fixkosten(BigDecimal n_fixkosten) {
        this.n_fixkosten = n_fixkosten;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public Integer getGebinde_i_id() {
        return this.gebinde_i_id;
    }

    public void setGebinde_i_id(Integer gebinde_i_id) {
        this.gebinde_i_id = gebinde_i_id;
    }

    public BigDecimal getN_anzahlgebinde() {
        return this.n_anzahlgebinde;
    }

    public void setN_anzahlgebinde(BigDecimal n_anzahlgebinde) {
        this.n_anzahlgebinde = n_anzahlgebinde;
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

    public FLRGebinde getFlrgebinde() {
        return this.flrgebinde;
    }

    public void setFlrgebinde(FLRGebinde flrgebinde) {
        this.flrgebinde = flrgebinde;
    }

    public Set getAbrufpositionenset() {
        return this.abrufpositionenset;
    }

    public void setAbrufpositionenset(Set abrufpositionenset) {
        this.abrufpositionenset = abrufpositionenset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
