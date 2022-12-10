package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppe;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String artikelart_c_nr;

    /** nullable persistent field */
    private Short b_lagerbewertet;

    /** nullable persistent field */
    private Short b_dokumentenpflicht;

    /** nullable persistent field */
    private Short b_lagerbewirtschaftet;

    /** nullable persistent field */
    private Short b_kommissionieren;

    /** nullable persistent field */
    private Short b_vkpreispflichtig;

    /** nullable persistent field */
    private Short b_keine_lagerzubuchung;

    /** nullable persistent field */
    private Short b_kalkulatorisch;

    /** nullable persistent field */
    private Short b_seriennrtragend;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_chargennrtragend;

    /** nullable persistent field */
    private Double f_verschnittfaktor;

    /** nullable persistent field */
    private Double f_verschnittbasis;

    /** nullable persistent field */
    private Double f_vertreterprovisionmax;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private Double f_lagersoll;

    /** nullable persistent field */
    private Double f_fertigungssatzgroesse;

    /** nullable persistent field */
    private String c_artikelbezhersteller;

    /** nullable persistent field */
    private String c_artikelnrhersteller;

    /** nullable persistent field */
    private Integer i_sofortverbrauch;

    /** nullable persistent field */
    private Integer i_wartungsintervall;

    /** nullable persistent field */
    private String einheit_c_nr_bestellung;

    /** persistent field */
    private BigDecimal n_umrechnungsfaktor;

    /** nullable persistent field */
    private Short b_bestellmengeneinheitinvers;

    /** nullable persistent field */
    private Short b_meldepflichtig;

    /** nullable persistent field */
    private Short b_bewilligungspflichtig;

    /** nullable persistent field */
    private Date t_freigabe;

    /** persistent field */
    private String c_referenznr;

    /** nullable persistent field */
    private String c_verkaufseannr;

    /** nullable persistent field */
    private String c_verpackungseannr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_freigabe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse;

    /** nullable persistent field */
    private FLRLiefergruppe flrliefergruppe;

    /** persistent field */
    private Set artikellagerplatzset;

    /** persistent field */
    private Set stuecklisten;

    /** full constructor */
    public FLRArtikel(String c_nr, String mandant_c_nr, String einheit_c_nr, String artikelart_c_nr, Short b_lagerbewertet, Short b_dokumentenpflicht, Short b_lagerbewirtschaftet, Short b_kommissionieren, Short b_vkpreispflichtig, Short b_keine_lagerzubuchung, Short b_kalkulatorisch, Short b_seriennrtragend, Short b_versteckt, Short b_chargennrtragend, Double f_verschnittfaktor, Double f_verschnittbasis, Double f_vertreterprovisionmax, Double f_lagermindest, Double f_lagersoll, Double f_fertigungssatzgroesse, String c_artikelbezhersteller, String c_artikelnrhersteller, Integer i_sofortverbrauch, Integer i_wartungsintervall, String einheit_c_nr_bestellung, BigDecimal n_umrechnungsfaktor, Short b_bestellmengeneinheitinvers, Short b_meldepflichtig, Short b_bewilligungspflichtig, Date t_freigabe, String c_referenznr, String c_verkaufseannr, String c_verpackungseannr, com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe, com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe, com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial, FLRPersonal flrpersonal_freigabe, com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse, FLRLiefergruppe flrliefergruppe, Set artikellagerplatzset, Set stuecklisten) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.einheit_c_nr = einheit_c_nr;
        this.artikelart_c_nr = artikelart_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_dokumentenpflicht = b_dokumentenpflicht;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_kommissionieren = b_kommissionieren;
        this.b_vkpreispflichtig = b_vkpreispflichtig;
        this.b_keine_lagerzubuchung = b_keine_lagerzubuchung;
        this.b_kalkulatorisch = b_kalkulatorisch;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_versteckt = b_versteckt;
        this.b_chargennrtragend = b_chargennrtragend;
        this.f_verschnittfaktor = f_verschnittfaktor;
        this.f_verschnittbasis = f_verschnittbasis;
        this.f_vertreterprovisionmax = f_vertreterprovisionmax;
        this.f_lagermindest = f_lagermindest;
        this.f_lagersoll = f_lagersoll;
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
        this.c_artikelbezhersteller = c_artikelbezhersteller;
        this.c_artikelnrhersteller = c_artikelnrhersteller;
        this.i_sofortverbrauch = i_sofortverbrauch;
        this.i_wartungsintervall = i_wartungsintervall;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
        this.b_bestellmengeneinheitinvers = b_bestellmengeneinheitinvers;
        this.b_meldepflichtig = b_meldepflichtig;
        this.b_bewilligungspflichtig = b_bewilligungspflichtig;
        this.t_freigabe = t_freigabe;
        this.c_referenznr = c_referenznr;
        this.c_verkaufseannr = c_verkaufseannr;
        this.c_verpackungseannr = c_verpackungseannr;
        this.flrartikelgruppe = flrartikelgruppe;
        this.flrshopgruppe = flrshopgruppe;
        this.flrmaterial = flrmaterial;
        this.flrpersonal_freigabe = flrpersonal_freigabe;
        this.flrartikelklasse = flrartikelklasse;
        this.flrliefergruppe = flrliefergruppe;
        this.artikellagerplatzset = artikellagerplatzset;
        this.stuecklisten = stuecklisten;
    }

    /** default constructor */
    public FLRArtikel() {
    }

    /** minimal constructor */
    public FLRArtikel(String mandant_c_nr, BigDecimal n_umrechnungsfaktor, String c_referenznr, Set artikellagerplatzset, Set stuecklisten) {
        this.mandant_c_nr = mandant_c_nr;
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
        this.c_referenznr = c_referenznr;
        this.artikellagerplatzset = artikellagerplatzset;
        this.stuecklisten = stuecklisten;
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

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getArtikelart_c_nr() {
        return this.artikelart_c_nr;
    }

    public void setArtikelart_c_nr(String artikelart_c_nr) {
        this.artikelart_c_nr = artikelart_c_nr;
    }

    public Short getB_lagerbewertet() {
        return this.b_lagerbewertet;
    }

    public void setB_lagerbewertet(Short b_lagerbewertet) {
        this.b_lagerbewertet = b_lagerbewertet;
    }

    public Short getB_dokumentenpflicht() {
        return this.b_dokumentenpflicht;
    }

    public void setB_dokumentenpflicht(Short b_dokumentenpflicht) {
        this.b_dokumentenpflicht = b_dokumentenpflicht;
    }

    public Short getB_lagerbewirtschaftet() {
        return this.b_lagerbewirtschaftet;
    }

    public void setB_lagerbewirtschaftet(Short b_lagerbewirtschaftet) {
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
    }

    public Short getB_kommissionieren() {
        return this.b_kommissionieren;
    }

    public void setB_kommissionieren(Short b_kommissionieren) {
        this.b_kommissionieren = b_kommissionieren;
    }

    public Short getB_vkpreispflichtig() {
        return this.b_vkpreispflichtig;
    }

    public void setB_vkpreispflichtig(Short b_vkpreispflichtig) {
        this.b_vkpreispflichtig = b_vkpreispflichtig;
    }

    public Short getB_keine_lagerzubuchung() {
        return this.b_keine_lagerzubuchung;
    }

    public void setB_keine_lagerzubuchung(Short b_keine_lagerzubuchung) {
        this.b_keine_lagerzubuchung = b_keine_lagerzubuchung;
    }

    public Short getB_kalkulatorisch() {
        return this.b_kalkulatorisch;
    }

    public void setB_kalkulatorisch(Short b_kalkulatorisch) {
        this.b_kalkulatorisch = b_kalkulatorisch;
    }

    public Short getB_seriennrtragend() {
        return this.b_seriennrtragend;
    }

    public void setB_seriennrtragend(Short b_seriennrtragend) {
        this.b_seriennrtragend = b_seriennrtragend;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_chargennrtragend() {
        return this.b_chargennrtragend;
    }

    public void setB_chargennrtragend(Short b_chargennrtragend) {
        this.b_chargennrtragend = b_chargennrtragend;
    }

    public Double getF_verschnittfaktor() {
        return this.f_verschnittfaktor;
    }

    public void setF_verschnittfaktor(Double f_verschnittfaktor) {
        this.f_verschnittfaktor = f_verschnittfaktor;
    }

    public Double getF_verschnittbasis() {
        return this.f_verschnittbasis;
    }

    public void setF_verschnittbasis(Double f_verschnittbasis) {
        this.f_verschnittbasis = f_verschnittbasis;
    }

    public Double getF_vertreterprovisionmax() {
        return this.f_vertreterprovisionmax;
    }

    public void setF_vertreterprovisionmax(Double f_vertreterprovisionmax) {
        this.f_vertreterprovisionmax = f_vertreterprovisionmax;
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

    public Double getF_fertigungssatzgroesse() {
        return this.f_fertigungssatzgroesse;
    }

    public void setF_fertigungssatzgroesse(Double f_fertigungssatzgroesse) {
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
    }

    public String getC_artikelbezhersteller() {
        return this.c_artikelbezhersteller;
    }

    public void setC_artikelbezhersteller(String c_artikelbezhersteller) {
        this.c_artikelbezhersteller = c_artikelbezhersteller;
    }

    public String getC_artikelnrhersteller() {
        return this.c_artikelnrhersteller;
    }

    public void setC_artikelnrhersteller(String c_artikelnrhersteller) {
        this.c_artikelnrhersteller = c_artikelnrhersteller;
    }

    public Integer getI_sofortverbrauch() {
        return this.i_sofortverbrauch;
    }

    public void setI_sofortverbrauch(Integer i_sofortverbrauch) {
        this.i_sofortverbrauch = i_sofortverbrauch;
    }

    public Integer getI_wartungsintervall() {
        return this.i_wartungsintervall;
    }

    public void setI_wartungsintervall(Integer i_wartungsintervall) {
        this.i_wartungsintervall = i_wartungsintervall;
    }

    public String getEinheit_c_nr_bestellung() {
        return this.einheit_c_nr_bestellung;
    }

    public void setEinheit_c_nr_bestellung(String einheit_c_nr_bestellung) {
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
    }

    public BigDecimal getN_umrechnungsfaktor() {
        return this.n_umrechnungsfaktor;
    }

    public void setN_umrechnungsfaktor(BigDecimal n_umrechnungsfaktor) {
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
    }

    public Short getB_bestellmengeneinheitinvers() {
        return this.b_bestellmengeneinheitinvers;
    }

    public void setB_bestellmengeneinheitinvers(Short b_bestellmengeneinheitinvers) {
        this.b_bestellmengeneinheitinvers = b_bestellmengeneinheitinvers;
    }

    public Short getB_meldepflichtig() {
        return this.b_meldepflichtig;
    }

    public void setB_meldepflichtig(Short b_meldepflichtig) {
        this.b_meldepflichtig = b_meldepflichtig;
    }

    public Short getB_bewilligungspflichtig() {
        return this.b_bewilligungspflichtig;
    }

    public void setB_bewilligungspflichtig(Short b_bewilligungspflichtig) {
        this.b_bewilligungspflichtig = b_bewilligungspflichtig;
    }

    public Date getT_freigabe() {
        return this.t_freigabe;
    }

    public void setT_freigabe(Date t_freigabe) {
        this.t_freigabe = t_freigabe;
    }

    public String getC_referenznr() {
        return this.c_referenznr;
    }

    public void setC_referenznr(String c_referenznr) {
        this.c_referenznr = c_referenznr;
    }

    public String getC_verkaufseannr() {
        return this.c_verkaufseannr;
    }

    public void setC_verkaufseannr(String c_verkaufseannr) {
        this.c_verkaufseannr = c_verkaufseannr;
    }

    public String getC_verpackungseannr() {
        return this.c_verpackungseannr;
    }

    public void setC_verpackungseannr(String c_verpackungseannr) {
        this.c_verpackungseannr = c_verpackungseannr;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe getFlrartikelgruppe() {
        return this.flrartikelgruppe;
    }

    public void setFlrartikelgruppe(com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe) {
        this.flrartikelgruppe = flrartikelgruppe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe getFlrshopgruppe() {
        return this.flrshopgruppe;
    }

    public void setFlrshopgruppe(com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe) {
        this.flrshopgruppe = flrshopgruppe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRMaterial getFlrmaterial() {
        return this.flrmaterial;
    }

    public void setFlrmaterial(com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial) {
        this.flrmaterial = flrmaterial;
    }

    public FLRPersonal getFlrpersonal_freigabe() {
        return this.flrpersonal_freigabe;
    }

    public void setFlrpersonal_freigabe(FLRPersonal flrpersonal_freigabe) {
        this.flrpersonal_freigabe = flrpersonal_freigabe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse getFlrartikelklasse() {
        return this.flrartikelklasse;
    }

    public void setFlrartikelklasse(com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse) {
        this.flrartikelklasse = flrartikelklasse;
    }

    public FLRLiefergruppe getFlrliefergruppe() {
        return this.flrliefergruppe;
    }

    public void setFlrliefergruppe(FLRLiefergruppe flrliefergruppe) {
        this.flrliefergruppe = flrliefergruppe;
    }

    public Set getArtikellagerplatzset() {
        return this.artikellagerplatzset;
    }

    public void setArtikellagerplatzset(Set artikellagerplatzset) {
        this.artikellagerplatzset = artikellagerplatzset;
    }

    public Set getStuecklisten() {
        return this.stuecklisten;
    }

    public void setStuecklisten(Set stuecklisten) {
        this.stuecklisten = stuecklisten;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
