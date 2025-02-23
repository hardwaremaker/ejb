package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellposition implements Serializable {

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
    private Short b_artikelbezuebersteuert;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String c_textinhalt;

    /** nullable persistent field */
    private String bestellpositionstatus_c_nr;

    /** nullable persistent field */
    private String bestellpositionart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Date t_auftragsbestaetigungstermin;

    /** nullable persistent field */
    private Date t_abursprungstermin;

    /** nullable persistent field */
    private String c_abnummer;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private Integer bestellposition_i_id_rahmenposition;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRLossollmaterial flrlossollmaterial;

    /** full constructor */
    public FLRBestellposition(Integer i_sort, BigDecimal n_menge, String einheit_c_nr, String c_bezeichnung, String c_zusatzbezeichnung, Short b_artikelbezuebersteuert, BigDecimal n_nettoeinzelpreis, BigDecimal n_nettogesamtpreis, String c_textinhalt, String bestellpositionstatus_c_nr, String bestellpositionart_c_nr, BigDecimal n_offenemenge, Date t_uebersteuerterliefertermin, Date t_auftragsbestaetigungstermin, Date t_abursprungstermin, String c_abnummer, Integer position_i_id_artikelset, Integer bestellposition_i_id_rahmenposition, Integer lossollmaterial_i_id, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, FLRLossollmaterial flrlossollmaterial) {
        this.i_sort = i_sort;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bezeichnung = c_bezeichnung;
        this.c_zusatzbezeichnung = c_zusatzbezeichnung;
        this.b_artikelbezuebersteuert = b_artikelbezuebersteuert;
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.c_textinhalt = c_textinhalt;
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
        this.n_offenemenge = n_offenemenge;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
        this.t_abursprungstermin = t_abursprungstermin;
        this.c_abnummer = c_abnummer;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.bestellposition_i_id_rahmenposition = bestellposition_i_id_rahmenposition;
        this.lossollmaterial_i_id = lossollmaterial_i_id;
        this.flrbestellung = flrbestellung;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrlossollmaterial = flrlossollmaterial;
    }

    /** default constructor */
    public FLRBestellposition() {
    }

    /** minimal constructor */
    public FLRBestellposition(Integer i_sort) {
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

    public Short getB_artikelbezuebersteuert() {
        return this.b_artikelbezuebersteuert;
    }

    public void setB_artikelbezuebersteuert(Short b_artikelbezuebersteuert) {
        this.b_artikelbezuebersteuert = b_artikelbezuebersteuert;
    }

    public BigDecimal getN_nettoeinzelpreis() {
        return this.n_nettoeinzelpreis;
    }

    public void setN_nettoeinzelpreis(BigDecimal n_nettoeinzelpreis) {
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getC_textinhalt() {
        return this.c_textinhalt;
    }

    public void setC_textinhalt(String c_textinhalt) {
        this.c_textinhalt = c_textinhalt;
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

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Date getT_auftragsbestaetigungstermin() {
        return this.t_auftragsbestaetigungstermin;
    }

    public void setT_auftragsbestaetigungstermin(Date t_auftragsbestaetigungstermin) {
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
    }

    public Date getT_abursprungstermin() {
        return this.t_abursprungstermin;
    }

    public void setT_abursprungstermin(Date t_abursprungstermin) {
        this.t_abursprungstermin = t_abursprungstermin;
    }

    public String getC_abnummer() {
        return this.c_abnummer;
    }

    public void setC_abnummer(String c_abnummer) {
        this.c_abnummer = c_abnummer;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public Integer getBestellposition_i_id_rahmenposition() {
        return this.bestellposition_i_id_rahmenposition;
    }

    public void setBestellposition_i_id_rahmenposition(Integer bestellposition_i_id_rahmenposition) {
        this.bestellposition_i_id_rahmenposition = bestellposition_i_id_rahmenposition;
    }

    public Integer getLossollmaterial_i_id() {
        return this.lossollmaterial_i_id;
    }

    public void setLossollmaterial_i_id(Integer lossollmaterial_i_id) {
        this.lossollmaterial_i_id = lossollmaterial_i_id;
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

    public FLRLossollmaterial getFlrlossollmaterial() {
        return this.flrlossollmaterial;
    }

    public void setFlrlossollmaterial(FLRLossollmaterial flrlossollmaterial) {
        this.flrlossollmaterial = flrlossollmaterial;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
