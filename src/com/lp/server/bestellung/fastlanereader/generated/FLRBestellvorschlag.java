package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRGebinde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRBelegart;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellvorschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_zubestellendemenge;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis;

    /** nullable persistent field */
    private BigDecimal n_rabattbetrag;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisminusrabatte;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_nettopreisuebersteuert;

    /** nullable persistent field */
    private Short b_vormerkung;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer partner_i_id_standort;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer i_wiederbeschaffungszeit;

    /** nullable persistent field */
    private Date t_bearbeitet;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private Integer gebinde_i_id;

    /** nullable persistent field */
    private BigDecimal n_anzahlgebinde;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRBelegart flrbelegart;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRPartner flrpartner_standort;

    /** nullable persistent field */
    private FLRGebinde flrgebinde;

    /** full constructor */
    public FLRBestellvorschlag(BigDecimal n_zubestellendemenge, Date t_liefertermin, Integer artikel_i_id, Integer lieferant_i_id, BigDecimal n_nettoeinzelpreis, BigDecimal n_rabattbetrag, BigDecimal n_nettogesamtpreisminusrabatte, BigDecimal n_nettogesamtpreis, String belegart_c_nr, Integer i_belegartid, Integer i_belegartpositionid, String mandant_c_nr, Short b_nettopreisuebersteuert, Short b_vormerkung, Integer projekt_i_id, String x_textinhalt, Integer partner_i_id_standort, Integer personal_i_id, Integer i_wiederbeschaffungszeit, Date t_bearbeitet, Double f_lagermindest, Integer gebinde_i_id, BigDecimal n_anzahlgebinde, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, FLRLieferant flrlieferant, FLRBelegart flrbelegart, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRProjekt flrprojekt, FLRPartner flrpartner_standort, FLRGebinde flrgebinde) {
        this.n_zubestellendemenge = n_zubestellendemenge;
        this.t_liefertermin = t_liefertermin;
        this.artikel_i_id = artikel_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
        this.n_rabattbetrag = n_rabattbetrag;
        this.n_nettogesamtpreisminusrabatte = n_nettogesamtpreisminusrabatte;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.belegart_c_nr = belegart_c_nr;
        this.i_belegartid = i_belegartid;
        this.i_belegartpositionid = i_belegartpositionid;
        this.mandant_c_nr = mandant_c_nr;
        this.b_nettopreisuebersteuert = b_nettopreisuebersteuert;
        this.b_vormerkung = b_vormerkung;
        this.projekt_i_id = projekt_i_id;
        this.x_textinhalt = x_textinhalt;
        this.partner_i_id_standort = partner_i_id_standort;
        this.personal_i_id = personal_i_id;
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
        this.t_bearbeitet = t_bearbeitet;
        this.f_lagermindest = f_lagermindest;
        this.gebinde_i_id = gebinde_i_id;
        this.n_anzahlgebinde = n_anzahlgebinde;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.flrlieferant = flrlieferant;
        this.flrbelegart = flrbelegart;
        this.flrbestellung = flrbestellung;
        this.flrprojekt = flrprojekt;
        this.flrpartner_standort = flrpartner_standort;
        this.flrgebinde = flrgebinde;
    }

    /** default constructor */
    public FLRBestellvorschlag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_zubestellendemenge() {
        return this.n_zubestellendemenge;
    }

    public void setN_zubestellendemenge(BigDecimal n_zubestellendemenge) {
        this.n_zubestellendemenge = n_zubestellendemenge;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public BigDecimal getN_nettoeinzelpreis() {
        return this.n_nettoeinzelpreis;
    }

    public void setN_nettoeinzelpreis(BigDecimal n_nettoeinzelpreis) {
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
    }

    public BigDecimal getN_rabattbetrag() {
        return this.n_rabattbetrag;
    }

    public void setN_rabattbetrag(BigDecimal n_rabattbetrag) {
        this.n_rabattbetrag = n_rabattbetrag;
    }

    public BigDecimal getN_nettogesamtpreisminusrabatte() {
        return this.n_nettogesamtpreisminusrabatte;
    }

    public void setN_nettogesamtpreisminusrabatte(BigDecimal n_nettogesamtpreisminusrabatte) {
        this.n_nettogesamtpreisminusrabatte = n_nettogesamtpreisminusrabatte;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_nettopreisuebersteuert() {
        return this.b_nettopreisuebersteuert;
    }

    public void setB_nettopreisuebersteuert(Short b_nettopreisuebersteuert) {
        this.b_nettopreisuebersteuert = b_nettopreisuebersteuert;
    }

    public Short getB_vormerkung() {
        return this.b_vormerkung;
    }

    public void setB_vormerkung(Short b_vormerkung) {
        this.b_vormerkung = b_vormerkung;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getPartner_i_id_standort() {
        return this.partner_i_id_standort;
    }

    public void setPartner_i_id_standort(Integer partner_i_id_standort) {
        this.partner_i_id_standort = partner_i_id_standort;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getI_wiederbeschaffungszeit() {
        return this.i_wiederbeschaffungszeit;
    }

    public void setI_wiederbeschaffungszeit(Integer i_wiederbeschaffungszeit) {
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
    }

    public Date getT_bearbeitet() {
        return this.t_bearbeitet;
    }

    public void setT_bearbeitet(Date t_bearbeitet) {
        this.t_bearbeitet = t_bearbeitet;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
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

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRBelegart getFlrbelegart() {
        return this.flrbelegart;
    }

    public void setFlrbelegart(FLRBelegart flrbelegart) {
        this.flrbelegart = flrbelegart;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRPartner getFlrpartner_standort() {
        return this.flrpartner_standort;
    }

    public void setFlrpartner_standort(FLRPartner flrpartner_standort) {
        this.flrpartner_standort = flrpartner_standort;
    }

    public FLRGebinde getFlrgebinde() {
        return this.flrgebinde;
    }

    public void setFlrgebinde(FLRGebinde flrgebinde) {
        this.flrgebinde = flrgebinde;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
