package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.fastlanereader.generated.FLRLieferart;
import com.lp.server.system.fastlanereader.generated.FLRSpediteur;
import com.lp.server.system.fastlanereader.generated.FLRZahlungsziel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_vollstaendig_geliefert;

    /** nullable persistent field */
    private Date t_komissionierung_geplant;

    /** nullable persistent field */
    private Date t_komissionierung_durchgefuehrt;

    /** nullable persistent field */
    private Date t_uebergabe_technik;

    /** nullable persistent field */
    private Date t_manuellgeliefert;

    /** nullable persistent field */
    private BigDecimal n_bestellwert;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String bestellungstatus_c_nr;

    /** nullable persistent field */
    private String bestellungart_c_nr;

    /** nullable persistent field */
    private Integer bestellung_i_id_rahmenbestellung;

    /** nullable persistent field */
    private String waehrung_c_nr_bestellwaehrung;

    /** nullable persistent field */
    private Integer lieferant_i_id_bestelladresse;

    /** nullable persistent field */
    private Integer lieferant_i_id_rechnungsadresse;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private String c_bezprojektbezeichnung;

    /** nullable persistent field */
    private String c_lieferantenangebot;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungbestellungswaehrung;

    /** nullable persistent field */
    private Integer mahnstufe_i_id;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private Date t_mahnsperrebis;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Short b_poenale;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private Integer lieferart_i_id;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private String x_externerkommentar;

    /** nullable persistent field */
    private FLRLieferart flrlieferart;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellungtextsuche flrbestellungtextsuche;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRPartner flrpartnerlieferadresse;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRZahlungsziel flrzahlungsziel;

    /** persistent field */
    private Set bestellpositionen;

    /** nullable persistent field */
    private Integer spediteur_i_id;
    private FLRSpediteur flrspediteur;

    /** full constructor */
    public FLRBestellung(String c_nr, Date t_belegdatum, Date t_liefertermin, Date t_vollstaendig_geliefert, Date t_komissionierung_geplant, Date t_komissionierung_durchgefuehrt, Date t_uebergabe_technik, Date t_manuellgeliefert, BigDecimal n_bestellwert, String mandant_c_nr, String bestellungstatus_c_nr, String bestellungart_c_nr, Integer bestellung_i_id_rahmenbestellung, String waehrung_c_nr_bestellwaehrung, Integer lieferant_i_id_bestelladresse, Integer lieferant_i_id_rechnungsadresse, Integer kostenstelle_i_id, Integer auftrag_i_id, String c_bezprojektbezeichnung, String c_lieferantenangebot, Double f_wechselkursmandantwaehrungbestellungswaehrung, Integer mahnstufe_i_id, Date t_versandzeitpunkt, Date t_mahnsperrebis, String c_versandtype, Short b_poenale, Integer projekt_i_id, Integer ansprechpartner_i_id, Integer lieferart_i_id, String x_internerkommentar, String x_externerkommentar, FLRLieferart flrlieferart, FLRLieferant flrlieferant, FLRKostenstelle flrkostenstelle, FLRPersonal flrpersonal, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, com.lp.server.bestellung.fastlanereader.generated.FLRBestellungtextsuche flrbestellungtextsuche, FLRAuftrag flrauftrag, FLRPartner flrpartnerlieferadresse, FLRProjekt flrprojekt, FLRZahlungsziel flrzahlungsziel, Set bestellpositionen, Integer spediteur_i_id, FLRSpediteur flrspediteur) {
        this.c_nr = c_nr;
        this.t_belegdatum = t_belegdatum;
        this.t_liefertermin = t_liefertermin;
        this.t_vollstaendig_geliefert = t_vollstaendig_geliefert;
        this.t_komissionierung_geplant = t_komissionierung_geplant;
        this.t_komissionierung_durchgefuehrt = t_komissionierung_durchgefuehrt;
        this.t_uebergabe_technik = t_uebergabe_technik;
        this.t_manuellgeliefert = t_manuellgeliefert;
        this.n_bestellwert = n_bestellwert;
        this.mandant_c_nr = mandant_c_nr;
        this.bestellungstatus_c_nr = bestellungstatus_c_nr;
        this.bestellungart_c_nr = bestellungart_c_nr;
        this.bestellung_i_id_rahmenbestellung = bestellung_i_id_rahmenbestellung;
        this.waehrung_c_nr_bestellwaehrung = waehrung_c_nr_bestellwaehrung;
        this.lieferant_i_id_bestelladresse = lieferant_i_id_bestelladresse;
        this.lieferant_i_id_rechnungsadresse = lieferant_i_id_rechnungsadresse;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.auftrag_i_id = auftrag_i_id;
        this.c_bezprojektbezeichnung = c_bezprojektbezeichnung;
        this.c_lieferantenangebot = c_lieferantenangebot;
        this.f_wechselkursmandantwaehrungbestellungswaehrung = f_wechselkursmandantwaehrungbestellungswaehrung;
        this.mahnstufe_i_id = mahnstufe_i_id;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.t_mahnsperrebis = t_mahnsperrebis;
        this.c_versandtype = c_versandtype;
        this.b_poenale = b_poenale;
        this.projekt_i_id = projekt_i_id;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.lieferart_i_id = lieferart_i_id;
        this.x_internerkommentar = x_internerkommentar;
        this.x_externerkommentar = x_externerkommentar;
        this.flrlieferart = flrlieferart;
        this.flrlieferant = flrlieferant;
        this.flrkostenstelle = flrkostenstelle;
        this.flrpersonal = flrpersonal;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrbestellungtextsuche = flrbestellungtextsuche;
        this.flrauftrag = flrauftrag;
        this.flrpartnerlieferadresse = flrpartnerlieferadresse;
        this.flrprojekt = flrprojekt;
        this.flrzahlungsziel = flrzahlungsziel;
        this.bestellpositionen = bestellpositionen;
    }

    /** default constructor */
    public FLRBestellung() {
    }

    /** minimal constructor */
    public FLRBestellung(Set bestellpositionen) {
        this.bestellpositionen = bestellpositionen;
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

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_vollstaendig_geliefert() {
        return this.t_vollstaendig_geliefert;
    }

    public void setT_vollstaendig_geliefert(Date t_vollstaendig_geliefert) {
        this.t_vollstaendig_geliefert = t_vollstaendig_geliefert;
    }

    public Date getT_komissionierung_geplant() {
        return this.t_komissionierung_geplant;
    }

    public void setT_komissionierung_geplant(Date t_komissionierung_geplant) {
        this.t_komissionierung_geplant = t_komissionierung_geplant;
    }

    public Date getT_komissionierung_durchgefuehrt() {
        return this.t_komissionierung_durchgefuehrt;
    }

    public void setT_komissionierung_durchgefuehrt(Date t_komissionierung_durchgefuehrt) {
        this.t_komissionierung_durchgefuehrt = t_komissionierung_durchgefuehrt;
    }

    public Date getT_uebergabe_technik() {
        return this.t_uebergabe_technik;
    }

    public void setT_uebergabe_technik(Date t_uebergabe_technik) {
        this.t_uebergabe_technik = t_uebergabe_technik;
    }

    public Date getT_manuellgeliefert() {
        return this.t_manuellgeliefert;
    }

    public void setT_manuellgeliefert(Date t_manuellgeliefert) {
        this.t_manuellgeliefert = t_manuellgeliefert;
    }

    public BigDecimal getN_bestellwert() {
        return this.n_bestellwert;
    }

    public void setN_bestellwert(BigDecimal n_bestellwert) {
        this.n_bestellwert = n_bestellwert;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getBestellungstatus_c_nr() {
        return this.bestellungstatus_c_nr;
    }

    public void setBestellungstatus_c_nr(String bestellungstatus_c_nr) {
        this.bestellungstatus_c_nr = bestellungstatus_c_nr;
    }

    public String getBestellungart_c_nr() {
        return this.bestellungart_c_nr;
    }

    public void setBestellungart_c_nr(String bestellungart_c_nr) {
        this.bestellungart_c_nr = bestellungart_c_nr;
    }

    public Integer getBestellung_i_id_rahmenbestellung() {
        return this.bestellung_i_id_rahmenbestellung;
    }

    public void setBestellung_i_id_rahmenbestellung(Integer bestellung_i_id_rahmenbestellung) {
        this.bestellung_i_id_rahmenbestellung = bestellung_i_id_rahmenbestellung;
    }

    public String getWaehrung_c_nr_bestellwaehrung() {
        return this.waehrung_c_nr_bestellwaehrung;
    }

    public void setWaehrung_c_nr_bestellwaehrung(String waehrung_c_nr_bestellwaehrung) {
        this.waehrung_c_nr_bestellwaehrung = waehrung_c_nr_bestellwaehrung;
    }

    public Integer getLieferant_i_id_bestelladresse() {
        return this.lieferant_i_id_bestelladresse;
    }

    public void setLieferant_i_id_bestelladresse(Integer lieferant_i_id_bestelladresse) {
        this.lieferant_i_id_bestelladresse = lieferant_i_id_bestelladresse;
    }

    public Integer getLieferant_i_id_rechnungsadresse() {
        return this.lieferant_i_id_rechnungsadresse;
    }

    public void setLieferant_i_id_rechnungsadresse(Integer lieferant_i_id_rechnungsadresse) {
        this.lieferant_i_id_rechnungsadresse = lieferant_i_id_rechnungsadresse;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public String getC_bezprojektbezeichnung() {
        return this.c_bezprojektbezeichnung;
    }

    public void setC_bezprojektbezeichnung(String c_bezprojektbezeichnung) {
        this.c_bezprojektbezeichnung = c_bezprojektbezeichnung;
    }

    public String getC_lieferantenangebot() {
        return this.c_lieferantenangebot;
    }

    public void setC_lieferantenangebot(String c_lieferantenangebot) {
        this.c_lieferantenangebot = c_lieferantenangebot;
    }

    public Double getF_wechselkursmandantwaehrungbestellungswaehrung() {
        return this.f_wechselkursmandantwaehrungbestellungswaehrung;
    }

    public void setF_wechselkursmandantwaehrungbestellungswaehrung(Double f_wechselkursmandantwaehrungbestellungswaehrung) {
        this.f_wechselkursmandantwaehrungbestellungswaehrung = f_wechselkursmandantwaehrungbestellungswaehrung;
    }

    public Integer getMahnstufe_i_id() {
        return this.mahnstufe_i_id;
    }

    public void setMahnstufe_i_id(Integer mahnstufe_i_id) {
        this.mahnstufe_i_id = mahnstufe_i_id;
    }

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public Date getT_mahnsperrebis() {
        return this.t_mahnsperrebis;
    }

    public void setT_mahnsperrebis(Date t_mahnsperrebis) {
        this.t_mahnsperrebis = t_mahnsperrebis;
    }

    public String getC_versandtype() {
        return this.c_versandtype;
    }

    public void setC_versandtype(String c_versandtype) {
        this.c_versandtype = c_versandtype;
    }

    public Short getB_poenale() {
        return this.b_poenale;
    }

    public void setB_poenale(Short b_poenale) {
        this.b_poenale = b_poenale;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public Integer getLieferart_i_id() {
        return this.lieferart_i_id;
    }

    public void setLieferart_i_id(Integer lieferart_i_id) {
        this.lieferart_i_id = lieferart_i_id;
    }

    public String getX_internerkommentar() {
        return this.x_internerkommentar;
    }

    public void setX_internerkommentar(String x_internerkommentar) {
        this.x_internerkommentar = x_internerkommentar;
    }

    public String getX_externerkommentar() {
        return this.x_externerkommentar;
    }

    public void setX_externerkommentar(String x_externerkommentar) {
        this.x_externerkommentar = x_externerkommentar;
    }

    public FLRLieferart getFlrlieferart() {
        return this.flrlieferart;
    }

    public void setFlrlieferart(FLRLieferart flrlieferart) {
        this.flrlieferart = flrlieferart;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRPersonal getFlrpersonalanleger() {
        return this.flrpersonalanleger;
    }

    public void setFlrpersonalanleger(FLRPersonal flrpersonalanleger) {
        this.flrpersonalanleger = flrpersonalanleger;
    }

    public FLRPersonal getFlrpersonalaenderer() {
        return this.flrpersonalaenderer;
    }

    public void setFlrpersonalaenderer(FLRPersonal flrpersonalaenderer) {
        this.flrpersonalaenderer = flrpersonalaenderer;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellungtextsuche getFlrbestellungtextsuche() {
        return this.flrbestellungtextsuche;
    }

    public void setFlrbestellungtextsuche(com.lp.server.bestellung.fastlanereader.generated.FLRBestellungtextsuche flrbestellungtextsuche) {
        this.flrbestellungtextsuche = flrbestellungtextsuche;
    }

    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRPartner getFlrpartnerlieferadresse() {
        return this.flrpartnerlieferadresse;
    }

    public void setFlrpartnerlieferadresse(FLRPartner flrpartnerlieferadresse) {
        this.flrpartnerlieferadresse = flrpartnerlieferadresse;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRZahlungsziel getFlrzahlungsziel() {
        return this.flrzahlungsziel;
    }

    public void setFlrzahlungsziel(FLRZahlungsziel flrzahlungsziel) {
        this.flrzahlungsziel = flrzahlungsziel;
    }

    public Set getBestellpositionen() {
        return this.bestellpositionen;
    }

    public void setBestellpositionen(Set bestellpositionen) {
        this.bestellpositionen = bestellpositionen;
    }

    public Integer getSpediteur_i_id() {
        return this.spediteur_i_id;
    }

    public void setSpediteur_i_id(Integer spediteur_i_id) {
        this.spediteur_i_id = spediteur_i_id;
    }

    public FLRSpediteur getFlrspediteur() {
        return this.flrspediteur;
    }

    public void setFlrspediteur(FLRSpediteur flrspediteur) {
        this.flrspediteur = flrspediteur;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
