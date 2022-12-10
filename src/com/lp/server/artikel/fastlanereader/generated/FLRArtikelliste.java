package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelliste implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** persistent field */
    private Short b_lagerbewertet;

    /** persistent field */
    private Short b_lagerbewirtschaftet;

    /** persistent field */
    private Short b_seriennrtragend;

    /** persistent field */
    private Short b_chargennrtragend;

    /** persistent field */
    private Short b_versteckt;

    /** persistent field */
    private Integer i_passive_reisezeit;

    /** persistent field */
    private Short b_bevorzugt;

    /** nullable persistent field */
    private Date t_freigabe;

    /** nullable persistent field */
    private Integer i_garantiezeit;

    /** persistent field */
    private String artikelart_c_nr;

    /** persistent field */
    private String c_referenznr;

    /** persistent field */
    private String einheit_c_nr_bestellung;

    /** persistent field */
    private BigDecimal n_umrechnugsfaktor;

    /** nullable persistent field */
    private Double f_fertigungssatzgroesse;

    /** nullable persistent field */
    private Double f_maxfertigungssatzgroesse;

    /** nullable persistent field */
    private Double f_materialgewicht;

    /** nullable persistent field */
    private Double f_verpackungsmenge;

    /** nullable persistent field */
    private BigDecimal n_verpackungsmittelmenge;

    /** nullable persistent field */
    private String c_artikelbezhersteller;

    /** nullable persistent field */
    private String c_artikelnrhersteller;

    /** nullable persistent field */
    private Integer mwstsatz_i_id;

    /** nullable persistent field */
    private BigDecimal n_verschnittmenge;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private Double f_verschnittfaktor;

    /** nullable persistent field */
    private Double f_verschnittbasis;

    /** nullable persistent field */
    private String c_verkaufseannr;

    /** nullable persistent field */
    private Double f_fertigungs_vpe;

    /** nullable persistent field */
    private String c_index;

    /** nullable persistent field */
    private String c_revision;

    /** nullable persistent field */
    private String c_ul;

    /** nullable persistent field */
    private Short b_bestellmengeneinheitinvers;

    /** nullable persistent field */
    private Short b_meldepflichtig;

    /** nullable persistent field */
    private Short b_bewilligungspflichtig;

    /** nullable persistent field */
    private Integer shopgruppe_i_id;

    /** persistent field */
    private Short b_nurzurinfo;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie;

    /** nullable persistent field */
    private FLRMwstsatzbez flrmwstatzbez;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_freigabe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRVorzug flrvorzug;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller;

    /** persistent field */
    private Set artikelsperreset;

    /** persistent field */
    private Set ersatztypenset;

    /** persistent field */
    private Set artikellieferantset;

    /** persistent field */
    private Set artikelsprset;

    /** persistent field */
    private Set artikellagerset;

    /** persistent field */
    private Set stuecklisten;

    /** persistent field */
    private Set stuecklisten_mandantenunabhaengig;

    /** persistent field */
    private Set artikellagerplatzset;

    /** persistent field */
    private Set kundesokoset;

    /** persistent field */
    private Set kundesokoset2;

    /** persistent field */
    private Set artikelshopgruppeset;

    /** full constructor */
    public FLRArtikelliste(String c_nr, String mandant_c_nr, String einheit_c_nr, Short b_lagerbewertet, Short b_lagerbewirtschaftet, Short b_seriennrtragend, Short b_chargennrtragend, Short b_versteckt, Integer i_passive_reisezeit, Short b_bevorzugt, Date t_freigabe, Integer i_garantiezeit, String artikelart_c_nr, String c_referenznr, String einheit_c_nr_bestellung, BigDecimal n_umrechnugsfaktor, Double f_fertigungssatzgroesse, Double f_maxfertigungssatzgroesse, Double f_materialgewicht, Double f_verpackungsmenge, BigDecimal n_verpackungsmittelmenge, String c_artikelbezhersteller, String c_artikelnrhersteller, Integer mwstsatz_i_id, BigDecimal n_verschnittmenge, Double f_lagermindest, Double f_verschnittfaktor, Double f_verschnittbasis, String c_verkaufseannr, Double f_fertigungs_vpe, String c_index, String c_revision, String c_ul, Short b_bestellmengeneinheitinvers, Short b_meldepflichtig, Short b_bewilligungspflichtig, Integer shopgruppe_i_id, Short b_nurzurinfo, com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial, com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe, com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe, com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse, com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie, FLRMwstsatzbez flrmwstatzbez, FLRPersonal flrpersonal_freigabe, com.lp.server.artikel.fastlanereader.generated.FLRVorzug flrvorzug, com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller, Set artikelsperreset, Set ersatztypenset, Set artikellieferantset, Set artikelsprset, Set artikellagerset, Set stuecklisten, Set stuecklisten_mandantenunabhaengig, Set artikellagerplatzset, Set kundesokoset, Set kundesokoset2, Set artikelshopgruppeset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.einheit_c_nr = einheit_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_chargennrtragend = b_chargennrtragend;
        this.b_versteckt = b_versteckt;
        this.i_passive_reisezeit = i_passive_reisezeit;
        this.b_bevorzugt = b_bevorzugt;
        this.t_freigabe = t_freigabe;
        this.i_garantiezeit = i_garantiezeit;
        this.artikelart_c_nr = artikelart_c_nr;
        this.c_referenznr = c_referenznr;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
        this.f_maxfertigungssatzgroesse = f_maxfertigungssatzgroesse;
        this.f_materialgewicht = f_materialgewicht;
        this.f_verpackungsmenge = f_verpackungsmenge;
        this.n_verpackungsmittelmenge = n_verpackungsmittelmenge;
        this.c_artikelbezhersteller = c_artikelbezhersteller;
        this.c_artikelnrhersteller = c_artikelnrhersteller;
        this.mwstsatz_i_id = mwstsatz_i_id;
        this.n_verschnittmenge = n_verschnittmenge;
        this.f_lagermindest = f_lagermindest;
        this.f_verschnittfaktor = f_verschnittfaktor;
        this.f_verschnittbasis = f_verschnittbasis;
        this.c_verkaufseannr = c_verkaufseannr;
        this.f_fertigungs_vpe = f_fertigungs_vpe;
        this.c_index = c_index;
        this.c_revision = c_revision;
        this.c_ul = c_ul;
        this.b_bestellmengeneinheitinvers = b_bestellmengeneinheitinvers;
        this.b_meldepflichtig = b_meldepflichtig;
        this.b_bewilligungspflichtig = b_bewilligungspflichtig;
        this.shopgruppe_i_id = shopgruppe_i_id;
        this.b_nurzurinfo = b_nurzurinfo;
        this.flrmaterial = flrmaterial;
        this.flrartikelgruppe = flrartikelgruppe;
        this.flrshopgruppe = flrshopgruppe;
        this.flrartikelklasse = flrartikelklasse;
        this.flrgeometrie = flrgeometrie;
        this.flrmwstatzbez = flrmwstatzbez;
        this.flrpersonal_freigabe = flrpersonal_freigabe;
        this.flrvorzug = flrvorzug;
        this.flrhersteller = flrhersteller;
        this.artikelsperreset = artikelsperreset;
        this.ersatztypenset = ersatztypenset;
        this.artikellieferantset = artikellieferantset;
        this.artikelsprset = artikelsprset;
        this.artikellagerset = artikellagerset;
        this.stuecklisten = stuecklisten;
        this.stuecklisten_mandantenunabhaengig = stuecklisten_mandantenunabhaengig;
        this.artikellagerplatzset = artikellagerplatzset;
        this.kundesokoset = kundesokoset;
        this.kundesokoset2 = kundesokoset2;
        this.artikelshopgruppeset = artikelshopgruppeset;
    }

    /** default constructor */
    public FLRArtikelliste() {
    }

    /** minimal constructor */
    public FLRArtikelliste(String c_nr, String mandant_c_nr, Short b_lagerbewertet, Short b_lagerbewirtschaftet, Short b_seriennrtragend, Short b_chargennrtragend, Short b_versteckt, Integer i_passive_reisezeit, Short b_bevorzugt, String artikelart_c_nr, String c_referenznr, String einheit_c_nr_bestellung, BigDecimal n_umrechnugsfaktor, Short b_nurzurinfo, Set artikelsperreset, Set ersatztypenset, Set artikellieferantset, Set artikelsprset, Set artikellagerset, Set stuecklisten, Set stuecklisten_mandantenunabhaengig, Set artikellagerplatzset, Set kundesokoset, Set kundesokoset2, Set artikelshopgruppeset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_chargennrtragend = b_chargennrtragend;
        this.b_versteckt = b_versteckt;
        this.i_passive_reisezeit = i_passive_reisezeit;
        this.b_bevorzugt = b_bevorzugt;
        this.artikelart_c_nr = artikelart_c_nr;
        this.c_referenznr = c_referenznr;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
        this.b_nurzurinfo = b_nurzurinfo;
        this.artikelsperreset = artikelsperreset;
        this.ersatztypenset = ersatztypenset;
        this.artikellieferantset = artikellieferantset;
        this.artikelsprset = artikelsprset;
        this.artikellagerset = artikellagerset;
        this.stuecklisten = stuecklisten;
        this.stuecklisten_mandantenunabhaengig = stuecklisten_mandantenunabhaengig;
        this.artikellagerplatzset = artikellagerplatzset;
        this.kundesokoset = kundesokoset;
        this.kundesokoset2 = kundesokoset2;
        this.artikelshopgruppeset = artikelshopgruppeset;
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

    public Short getB_lagerbewertet() {
        return this.b_lagerbewertet;
    }

    public void setB_lagerbewertet(Short b_lagerbewertet) {
        this.b_lagerbewertet = b_lagerbewertet;
    }

    public Short getB_lagerbewirtschaftet() {
        return this.b_lagerbewirtschaftet;
    }

    public void setB_lagerbewirtschaftet(Short b_lagerbewirtschaftet) {
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
    }

    public Short getB_seriennrtragend() {
        return this.b_seriennrtragend;
    }

    public void setB_seriennrtragend(Short b_seriennrtragend) {
        this.b_seriennrtragend = b_seriennrtragend;
    }

    public Short getB_chargennrtragend() {
        return this.b_chargennrtragend;
    }

    public void setB_chargennrtragend(Short b_chargennrtragend) {
        this.b_chargennrtragend = b_chargennrtragend;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Integer getI_passive_reisezeit() {
        return this.i_passive_reisezeit;
    }

    public void setI_passive_reisezeit(Integer i_passive_reisezeit) {
        this.i_passive_reisezeit = i_passive_reisezeit;
    }

    public Short getB_bevorzugt() {
        return this.b_bevorzugt;
    }

    public void setB_bevorzugt(Short b_bevorzugt) {
        this.b_bevorzugt = b_bevorzugt;
    }

    public Date getT_freigabe() {
        return this.t_freigabe;
    }

    public void setT_freigabe(Date t_freigabe) {
        this.t_freigabe = t_freigabe;
    }

    public Integer getI_garantiezeit() {
        return this.i_garantiezeit;
    }

    public void setI_garantiezeit(Integer i_garantiezeit) {
        this.i_garantiezeit = i_garantiezeit;
    }

    public String getArtikelart_c_nr() {
        return this.artikelart_c_nr;
    }

    public void setArtikelart_c_nr(String artikelart_c_nr) {
        this.artikelart_c_nr = artikelart_c_nr;
    }

    public String getC_referenznr() {
        return this.c_referenznr;
    }

    public void setC_referenznr(String c_referenznr) {
        this.c_referenznr = c_referenznr;
    }

    public String getEinheit_c_nr_bestellung() {
        return this.einheit_c_nr_bestellung;
    }

    public void setEinheit_c_nr_bestellung(String einheit_c_nr_bestellung) {
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
    }

    public BigDecimal getN_umrechnugsfaktor() {
        return this.n_umrechnugsfaktor;
    }

    public void setN_umrechnugsfaktor(BigDecimal n_umrechnugsfaktor) {
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
    }

    public Double getF_fertigungssatzgroesse() {
        return this.f_fertigungssatzgroesse;
    }

    public void setF_fertigungssatzgroesse(Double f_fertigungssatzgroesse) {
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
    }

    public Double getF_maxfertigungssatzgroesse() {
        return this.f_maxfertigungssatzgroesse;
    }

    public void setF_maxfertigungssatzgroesse(Double f_maxfertigungssatzgroesse) {
        this.f_maxfertigungssatzgroesse = f_maxfertigungssatzgroesse;
    }

    public Double getF_materialgewicht() {
        return this.f_materialgewicht;
    }

    public void setF_materialgewicht(Double f_materialgewicht) {
        this.f_materialgewicht = f_materialgewicht;
    }

    public Double getF_verpackungsmenge() {
        return this.f_verpackungsmenge;
    }

    public void setF_verpackungsmenge(Double f_verpackungsmenge) {
        this.f_verpackungsmenge = f_verpackungsmenge;
    }

    public BigDecimal getN_verpackungsmittelmenge() {
        return this.n_verpackungsmittelmenge;
    }

    public void setN_verpackungsmittelmenge(BigDecimal n_verpackungsmittelmenge) {
        this.n_verpackungsmittelmenge = n_verpackungsmittelmenge;
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

    public Integer getMwstsatz_i_id() {
        return this.mwstsatz_i_id;
    }

    public void setMwstsatz_i_id(Integer mwstsatz_i_id) {
        this.mwstsatz_i_id = mwstsatz_i_id;
    }

    public BigDecimal getN_verschnittmenge() {
        return this.n_verschnittmenge;
    }

    public void setN_verschnittmenge(BigDecimal n_verschnittmenge) {
        this.n_verschnittmenge = n_verschnittmenge;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
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

    public String getC_verkaufseannr() {
        return this.c_verkaufseannr;
    }

    public void setC_verkaufseannr(String c_verkaufseannr) {
        this.c_verkaufseannr = c_verkaufseannr;
    }

    public Double getF_fertigungs_vpe() {
        return this.f_fertigungs_vpe;
    }

    public void setF_fertigungs_vpe(Double f_fertigungs_vpe) {
        this.f_fertigungs_vpe = f_fertigungs_vpe;
    }

    public String getC_index() {
        return this.c_index;
    }

    public void setC_index(String c_index) {
        this.c_index = c_index;
    }

    public String getC_revision() {
        return this.c_revision;
    }

    public void setC_revision(String c_revision) {
        this.c_revision = c_revision;
    }

    public String getC_ul() {
        return this.c_ul;
    }

    public void setC_ul(String c_ul) {
        this.c_ul = c_ul;
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

    public Integer getShopgruppe_i_id() {
        return this.shopgruppe_i_id;
    }

    public void setShopgruppe_i_id(Integer shopgruppe_i_id) {
        this.shopgruppe_i_id = shopgruppe_i_id;
    }

    public Short getB_nurzurinfo() {
        return this.b_nurzurinfo;
    }

    public void setB_nurzurinfo(Short b_nurzurinfo) {
        this.b_nurzurinfo = b_nurzurinfo;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRMaterial getFlrmaterial() {
        return this.flrmaterial;
    }

    public void setFlrmaterial(com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial) {
        this.flrmaterial = flrmaterial;
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

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse getFlrartikelklasse() {
        return this.flrartikelklasse;
    }

    public void setFlrartikelklasse(com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse) {
        this.flrartikelklasse = flrartikelklasse;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRGeometrie getFlrgeometrie() {
        return this.flrgeometrie;
    }

    public void setFlrgeometrie(com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie) {
        this.flrgeometrie = flrgeometrie;
    }

    public FLRMwstsatzbez getFlrmwstatzbez() {
        return this.flrmwstatzbez;
    }

    public void setFlrmwstatzbez(FLRMwstsatzbez flrmwstatzbez) {
        this.flrmwstatzbez = flrmwstatzbez;
    }

    public FLRPersonal getFlrpersonal_freigabe() {
        return this.flrpersonal_freigabe;
    }

    public void setFlrpersonal_freigabe(FLRPersonal flrpersonal_freigabe) {
        this.flrpersonal_freigabe = flrpersonal_freigabe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRVorzug getFlrvorzug() {
        return this.flrvorzug;
    }

    public void setFlrvorzug(com.lp.server.artikel.fastlanereader.generated.FLRVorzug flrvorzug) {
        this.flrvorzug = flrvorzug;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRHersteller getFlrhersteller() {
        return this.flrhersteller;
    }

    public void setFlrhersteller(com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller) {
        this.flrhersteller = flrhersteller;
    }

    public Set getArtikelsperreset() {
        return this.artikelsperreset;
    }

    public void setArtikelsperreset(Set artikelsperreset) {
        this.artikelsperreset = artikelsperreset;
    }

    public Set getErsatztypenset() {
        return this.ersatztypenset;
    }

    public void setErsatztypenset(Set ersatztypenset) {
        this.ersatztypenset = ersatztypenset;
    }

    public Set getArtikellieferantset() {
        return this.artikellieferantset;
    }

    public void setArtikellieferantset(Set artikellieferantset) {
        this.artikellieferantset = artikellieferantset;
    }

    public Set getArtikelsprset() {
        return this.artikelsprset;
    }

    public void setArtikelsprset(Set artikelsprset) {
        this.artikelsprset = artikelsprset;
    }

    public Set getArtikellagerset() {
        return this.artikellagerset;
    }

    public void setArtikellagerset(Set artikellagerset) {
        this.artikellagerset = artikellagerset;
    }

    public Set getStuecklisten() {
        return this.stuecklisten;
    }

    public void setStuecklisten(Set stuecklisten) {
        this.stuecklisten = stuecklisten;
    }

    public Set getStuecklisten_mandantenunabhaengig() {
        return this.stuecklisten_mandantenunabhaengig;
    }

    public void setStuecklisten_mandantenunabhaengig(Set stuecklisten_mandantenunabhaengig) {
        this.stuecklisten_mandantenunabhaengig = stuecklisten_mandantenunabhaengig;
    }

    public Set getArtikellagerplatzset() {
        return this.artikellagerplatzset;
    }

    public void setArtikellagerplatzset(Set artikellagerplatzset) {
        this.artikellagerplatzset = artikellagerplatzset;
    }

    public Set getKundesokoset() {
        return this.kundesokoset;
    }

    public void setKundesokoset(Set kundesokoset) {
        this.kundesokoset = kundesokoset;
    }

    public Set getKundesokoset2() {
        return this.kundesokoset2;
    }

    public void setKundesokoset2(Set kundesokoset2) {
        this.kundesokoset2 = kundesokoset2;
    }

    public Set getArtikelshopgruppeset() {
        return this.artikelshopgruppeset;
    }

    public void setArtikelshopgruppeset(Set artikelshopgruppeset) {
        this.artikelshopgruppeset = artikelshopgruppeset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
