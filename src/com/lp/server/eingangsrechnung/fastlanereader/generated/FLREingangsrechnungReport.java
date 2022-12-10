package com.lp.server.eingangsrechnung.fastlanereader.generated;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.fastlanereader.generated.FLRZahlungsziel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_text;

    /** nullable persistent field */
    private String c_weartikel;

    /** nullable persistent field */
    private String c_zollimportpapier;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_geschaeftsjahr;

    /** nullable persistent field */
    private String eingangsrechnungart_c_nr;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Date t_freigabedatum;

    /** nullable persistent field */
    private BigDecimal n_kurs;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_betragfw;

    /** nullable persistent field */
    private BigDecimal n_ustbetrag;

    /** nullable persistent field */
    private BigDecimal n_ustbetragfw;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private Date t_bezahltdatum;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private Short b_mitpositionen;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Integer mahnstufe_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private String c_lieferantenrechnungsnummer;

    /** nullable persistent field */
    private Date t_faelligkeit;

    /** nullable persistent field */
    private Date t_faelligkeit_skonto1;

    /** nullable persistent field */
    private Date t_faelligkeit_skonto2;

    /** nullable persistent field */
    private Date t_faelligkeit_belegdatum;

    /** nullable persistent field */
    private Date t_faelligkeit_skonto1_belegdatum;

    /** nullable persistent field */
    private Date t_faelligkeit_skonto2_belegdatum;

    /** nullable persistent field */
    private Integer personal_i_id_geprueft;

    /** nullable persistent field */
    private Date t_geprueft;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRZahlungsziel flrzahlungsziel;

    /** nullable persistent field */
    private FLRFinanzKonto flrkonto;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_geprueft;

    /** full constructor */
    public FLREingangsrechnungReport(String c_nr, String c_text, String c_weartikel, String c_zollimportpapier, String mandant_c_nr, Integer i_geschaeftsjahr, String eingangsrechnungart_c_nr, Date t_belegdatum, Date t_freigabedatum, BigDecimal n_kurs, BigDecimal n_betrag, BigDecimal n_betragfw, BigDecimal n_ustbetrag, BigDecimal n_ustbetragfw, String status_c_nr, String waehrung_c_nr, Date t_bezahltdatum, Date t_manuellerledigt, Short b_mitpositionen, Integer zahlungsziel_i_id, Integer mahnstufe_i_id, Integer lieferant_i_id, Integer kostenstelle_i_id, String c_lieferantenrechnungsnummer, Date t_faelligkeit, Date t_faelligkeit_skonto1, Date t_faelligkeit_skonto2, Date t_faelligkeit_belegdatum, Date t_faelligkeit_skonto1_belegdatum, Date t_faelligkeit_skonto2_belegdatum, Integer personal_i_id_geprueft, Date t_geprueft, FLRKostenstelle flrkostenstelle, FLRLieferant flrlieferant, FLRZahlungsziel flrzahlungsziel, FLRFinanzKonto flrkonto, FLRPersonal flrpersonal_geprueft) {
        this.c_nr = c_nr;
        this.c_text = c_text;
        this.c_weartikel = c_weartikel;
        this.c_zollimportpapier = c_zollimportpapier;
        this.mandant_c_nr = mandant_c_nr;
        this.i_geschaeftsjahr = i_geschaeftsjahr;
        this.eingangsrechnungart_c_nr = eingangsrechnungart_c_nr;
        this.t_belegdatum = t_belegdatum;
        this.t_freigabedatum = t_freigabedatum;
        this.n_kurs = n_kurs;
        this.n_betrag = n_betrag;
        this.n_betragfw = n_betragfw;
        this.n_ustbetrag = n_ustbetrag;
        this.n_ustbetragfw = n_ustbetragfw;
        this.status_c_nr = status_c_nr;
        this.waehrung_c_nr = waehrung_c_nr;
        this.t_bezahltdatum = t_bezahltdatum;
        this.t_manuellerledigt = t_manuellerledigt;
        this.b_mitpositionen = b_mitpositionen;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.mahnstufe_i_id = mahnstufe_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.c_lieferantenrechnungsnummer = c_lieferantenrechnungsnummer;
        this.t_faelligkeit = t_faelligkeit;
        this.t_faelligkeit_skonto1 = t_faelligkeit_skonto1;
        this.t_faelligkeit_skonto2 = t_faelligkeit_skonto2;
        this.t_faelligkeit_belegdatum = t_faelligkeit_belegdatum;
        this.t_faelligkeit_skonto1_belegdatum = t_faelligkeit_skonto1_belegdatum;
        this.t_faelligkeit_skonto2_belegdatum = t_faelligkeit_skonto2_belegdatum;
        this.personal_i_id_geprueft = personal_i_id_geprueft;
        this.t_geprueft = t_geprueft;
        this.flrkostenstelle = flrkostenstelle;
        this.flrlieferant = flrlieferant;
        this.flrzahlungsziel = flrzahlungsziel;
        this.flrkonto = flrkonto;
        this.flrpersonal_geprueft = flrpersonal_geprueft;
    }

    /** default constructor */
    public FLREingangsrechnungReport() {
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

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public String getC_weartikel() {
        return this.c_weartikel;
    }

    public void setC_weartikel(String c_weartikel) {
        this.c_weartikel = c_weartikel;
    }

    public String getC_zollimportpapier() {
        return this.c_zollimportpapier;
    }

    public void setC_zollimportpapier(String c_zollimportpapier) {
        this.c_zollimportpapier = c_zollimportpapier;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_geschaeftsjahr() {
        return this.i_geschaeftsjahr;
    }

    public void setI_geschaeftsjahr(Integer i_geschaeftsjahr) {
        this.i_geschaeftsjahr = i_geschaeftsjahr;
    }

    public String getEingangsrechnungart_c_nr() {
        return this.eingangsrechnungart_c_nr;
    }

    public void setEingangsrechnungart_c_nr(String eingangsrechnungart_c_nr) {
        this.eingangsrechnungart_c_nr = eingangsrechnungart_c_nr;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_freigabedatum() {
        return this.t_freigabedatum;
    }

    public void setT_freigabedatum(Date t_freigabedatum) {
        this.t_freigabedatum = t_freigabedatum;
    }

    public BigDecimal getN_kurs() {
        return this.n_kurs;
    }

    public void setN_kurs(BigDecimal n_kurs) {
        this.n_kurs = n_kurs;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_betragfw() {
        return this.n_betragfw;
    }

    public void setN_betragfw(BigDecimal n_betragfw) {
        this.n_betragfw = n_betragfw;
    }

    public BigDecimal getN_ustbetrag() {
        return this.n_ustbetrag;
    }

    public void setN_ustbetrag(BigDecimal n_ustbetrag) {
        this.n_ustbetrag = n_ustbetrag;
    }

    public BigDecimal getN_ustbetragfw() {
        return this.n_ustbetragfw;
    }

    public void setN_ustbetragfw(BigDecimal n_ustbetragfw) {
        this.n_ustbetragfw = n_ustbetragfw;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public Date getT_bezahltdatum() {
        return this.t_bezahltdatum;
    }

    public void setT_bezahltdatum(Date t_bezahltdatum) {
        this.t_bezahltdatum = t_bezahltdatum;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
    }

    public Short getB_mitpositionen() {
        return this.b_mitpositionen;
    }

    public void setB_mitpositionen(Short b_mitpositionen) {
        this.b_mitpositionen = b_mitpositionen;
    }

    public Integer getZahlungsziel_i_id() {
        return this.zahlungsziel_i_id;
    }

    public void setZahlungsziel_i_id(Integer zahlungsziel_i_id) {
        this.zahlungsziel_i_id = zahlungsziel_i_id;
    }

    public Integer getMahnstufe_i_id() {
        return this.mahnstufe_i_id;
    }

    public void setMahnstufe_i_id(Integer mahnstufe_i_id) {
        this.mahnstufe_i_id = mahnstufe_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public String getC_lieferantenrechnungsnummer() {
        return this.c_lieferantenrechnungsnummer;
    }

    public void setC_lieferantenrechnungsnummer(String c_lieferantenrechnungsnummer) {
        this.c_lieferantenrechnungsnummer = c_lieferantenrechnungsnummer;
    }

    public Date getT_faelligkeit() {
        return this.t_faelligkeit;
    }

    public void setT_faelligkeit(Date t_faelligkeit) {
        this.t_faelligkeit = t_faelligkeit;
    }

    public Date getT_faelligkeit_skonto1() {
        return this.t_faelligkeit_skonto1;
    }

    public void setT_faelligkeit_skonto1(Date t_faelligkeit_skonto1) {
        this.t_faelligkeit_skonto1 = t_faelligkeit_skonto1;
    }

    public Date getT_faelligkeit_skonto2() {
        return this.t_faelligkeit_skonto2;
    }

    public void setT_faelligkeit_skonto2(Date t_faelligkeit_skonto2) {
        this.t_faelligkeit_skonto2 = t_faelligkeit_skonto2;
    }

    public Date getT_faelligkeit_belegdatum() {
        return this.t_faelligkeit_belegdatum;
    }

    public void setT_faelligkeit_belegdatum(Date t_faelligkeit_belegdatum) {
        this.t_faelligkeit_belegdatum = t_faelligkeit_belegdatum;
    }

    public Date getT_faelligkeit_skonto1_belegdatum() {
        return this.t_faelligkeit_skonto1_belegdatum;
    }

    public void setT_faelligkeit_skonto1_belegdatum(Date t_faelligkeit_skonto1_belegdatum) {
        this.t_faelligkeit_skonto1_belegdatum = t_faelligkeit_skonto1_belegdatum;
    }

    public Date getT_faelligkeit_skonto2_belegdatum() {
        return this.t_faelligkeit_skonto2_belegdatum;
    }

    public void setT_faelligkeit_skonto2_belegdatum(Date t_faelligkeit_skonto2_belegdatum) {
        this.t_faelligkeit_skonto2_belegdatum = t_faelligkeit_skonto2_belegdatum;
    }

    public Integer getPersonal_i_id_geprueft() {
        return this.personal_i_id_geprueft;
    }

    public void setPersonal_i_id_geprueft(Integer personal_i_id_geprueft) {
        this.personal_i_id_geprueft = personal_i_id_geprueft;
    }

    public Date getT_geprueft() {
        return this.t_geprueft;
    }

    public void setT_geprueft(Date t_geprueft) {
        this.t_geprueft = t_geprueft;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRZahlungsziel getFlrzahlungsziel() {
        return this.flrzahlungsziel;
    }

    public void setFlrzahlungsziel(FLRZahlungsziel flrzahlungsziel) {
        this.flrzahlungsziel = flrzahlungsziel;
    }

    public FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public FLRPersonal getFlrpersonal_geprueft() {
        return this.flrpersonal_geprueft;
    }

    public void setFlrpersonal_geprueft(FLRPersonal flrpersonal_geprueft) {
        this.flrpersonal_geprueft = flrpersonal_geprueft;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
