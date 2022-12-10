package com.lp.server.anfrage.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppe;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnfrage implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String anfrageart_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Integer lfliefergruppe_i_id;

    /** nullable persistent field */
    private String anfragestatus_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_anfragewaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuanfragewaehrung;

    /** nullable persistent field */
    private BigDecimal n_gesamtanfragewertinanfragewaehrung;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer anfrage_i_id_liefergruppenanfrage;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_lieferant;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage_liefergruppenanfrage;

    /** nullable persistent field */
    private FLRPartner flrpartner_lieferadresse;

    /** nullable persistent field */
    private FLRLiefergruppe flrliefergruppe;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private com.lp.server.anfrage.fastlanereader.generated.FLRAnfragetextsuche flranfragetextsuche;

    /** full constructor */
    public FLRAnfrage(String mandant_c_nr, String c_nr, String anfrageart_c_nr, String c_bez, Date t_belegdatum, Integer lfliefergruppe_i_id, String anfragestatus_c_nr, String waehrung_c_nr_anfragewaehrung, Double f_wechselkursmandantwaehrungzuanfragewaehrung, BigDecimal n_gesamtanfragewertinanfragewaehrung, Date t_versandzeitpunkt, String c_versandtype, Integer projekt_i_id, Integer partner_i_id_lieferadresse, Integer anfrage_i_id_liefergruppenanfrage, Integer ansprechpartner_i_id_lieferant, FLRLieferant flrlieferant, com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage_liefergruppenanfrage, FLRPartner flrpartner_lieferadresse, FLRLiefergruppe flrliefergruppe, FLRKostenstelle flrkostenstelle, FLRProjekt flrprojekt, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, com.lp.server.anfrage.fastlanereader.generated.FLRAnfragetextsuche flranfragetextsuche) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.anfrageart_c_nr = anfrageart_c_nr;
        this.c_bez = c_bez;
        this.t_belegdatum = t_belegdatum;
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
        this.anfragestatus_c_nr = anfragestatus_c_nr;
        this.waehrung_c_nr_anfragewaehrung = waehrung_c_nr_anfragewaehrung;
        this.f_wechselkursmandantwaehrungzuanfragewaehrung = f_wechselkursmandantwaehrungzuanfragewaehrung;
        this.n_gesamtanfragewertinanfragewaehrung = n_gesamtanfragewertinanfragewaehrung;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.c_versandtype = c_versandtype;
        this.projekt_i_id = projekt_i_id;
        this.partner_i_id_lieferadresse = partner_i_id_lieferadresse;
        this.anfrage_i_id_liefergruppenanfrage = anfrage_i_id_liefergruppenanfrage;
        this.ansprechpartner_i_id_lieferant = ansprechpartner_i_id_lieferant;
        this.flrlieferant = flrlieferant;
        this.flranfrage_liefergruppenanfrage = flranfrage_liefergruppenanfrage;
        this.flrpartner_lieferadresse = flrpartner_lieferadresse;
        this.flrliefergruppe = flrliefergruppe;
        this.flrkostenstelle = flrkostenstelle;
        this.flrprojekt = flrprojekt;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flranfragetextsuche = flranfragetextsuche;
    }

    /** default constructor */
    public FLRAnfrage() {
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

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getAnfrageart_c_nr() {
        return this.anfrageart_c_nr;
    }

    public void setAnfrageart_c_nr(String anfrageart_c_nr) {
        this.anfrageart_c_nr = anfrageart_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Integer getLfliefergruppe_i_id() {
        return this.lfliefergruppe_i_id;
    }

    public void setLfliefergruppe_i_id(Integer lfliefergruppe_i_id) {
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
    }

    public String getAnfragestatus_c_nr() {
        return this.anfragestatus_c_nr;
    }

    public void setAnfragestatus_c_nr(String anfragestatus_c_nr) {
        this.anfragestatus_c_nr = anfragestatus_c_nr;
    }

    public String getWaehrung_c_nr_anfragewaehrung() {
        return this.waehrung_c_nr_anfragewaehrung;
    }

    public void setWaehrung_c_nr_anfragewaehrung(String waehrung_c_nr_anfragewaehrung) {
        this.waehrung_c_nr_anfragewaehrung = waehrung_c_nr_anfragewaehrung;
    }

    public Double getF_wechselkursmandantwaehrungzuanfragewaehrung() {
        return this.f_wechselkursmandantwaehrungzuanfragewaehrung;
    }

    public void setF_wechselkursmandantwaehrungzuanfragewaehrung(Double f_wechselkursmandantwaehrungzuanfragewaehrung) {
        this.f_wechselkursmandantwaehrungzuanfragewaehrung = f_wechselkursmandantwaehrungzuanfragewaehrung;
    }

    public BigDecimal getN_gesamtanfragewertinanfragewaehrung() {
        return this.n_gesamtanfragewertinanfragewaehrung;
    }

    public void setN_gesamtanfragewertinanfragewaehrung(BigDecimal n_gesamtanfragewertinanfragewaehrung) {
        this.n_gesamtanfragewertinanfragewaehrung = n_gesamtanfragewertinanfragewaehrung;
    }

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public String getC_versandtype() {
        return this.c_versandtype;
    }

    public void setC_versandtype(String c_versandtype) {
        this.c_versandtype = c_versandtype;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getPartner_i_id_lieferadresse() {
        return this.partner_i_id_lieferadresse;
    }

    public void setPartner_i_id_lieferadresse(Integer partner_i_id_lieferadresse) {
        this.partner_i_id_lieferadresse = partner_i_id_lieferadresse;
    }

    public Integer getAnfrage_i_id_liefergruppenanfrage() {
        return this.anfrage_i_id_liefergruppenanfrage;
    }

    public void setAnfrage_i_id_liefergruppenanfrage(Integer anfrage_i_id_liefergruppenanfrage) {
        this.anfrage_i_id_liefergruppenanfrage = anfrage_i_id_liefergruppenanfrage;
    }

    public Integer getAnsprechpartner_i_id_lieferant() {
        return this.ansprechpartner_i_id_lieferant;
    }

    public void setAnsprechpartner_i_id_lieferant(Integer ansprechpartner_i_id_lieferant) {
        this.ansprechpartner_i_id_lieferant = ansprechpartner_i_id_lieferant;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage getFlranfrage_liefergruppenanfrage() {
        return this.flranfrage_liefergruppenanfrage;
    }

    public void setFlranfrage_liefergruppenanfrage(com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage_liefergruppenanfrage) {
        this.flranfrage_liefergruppenanfrage = flranfrage_liefergruppenanfrage;
    }

    public FLRPartner getFlrpartner_lieferadresse() {
        return this.flrpartner_lieferadresse;
    }

    public void setFlrpartner_lieferadresse(FLRPartner flrpartner_lieferadresse) {
        this.flrpartner_lieferadresse = flrpartner_lieferadresse;
    }

    public FLRLiefergruppe getFlrliefergruppe() {
        return this.flrliefergruppe;
    }

    public void setFlrliefergruppe(FLRLiefergruppe flrliefergruppe) {
        this.flrliefergruppe = flrliefergruppe;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
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

    public com.lp.server.anfrage.fastlanereader.generated.FLRAnfragetextsuche getFlranfragetextsuche() {
        return this.flranfragetextsuche;
    }

    public void setFlranfragetextsuche(com.lp.server.anfrage.fastlanereader.generated.FLRAnfragetextsuche flranfragetextsuche) {
        this.flranfragetextsuche = flranfragetextsuche;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
