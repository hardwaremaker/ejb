package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.finanz.fastlanereader.generated.FLRReversechargeart;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRRechnungReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_geschaeftsjahr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_zollpapier;

    /** nullable persistent field */
    private String c_mahnungsanmerkung;

    /** nullable persistent field */
    private Date d_belegdatum;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private BigDecimal n_wert;

    /** nullable persistent field */
    private BigDecimal n_wertust;

    /** nullable persistent field */
    private BigDecimal n_wertfw;

    /** nullable persistent field */
    private BigDecimal n_wertustfw;

    /** nullable persistent field */
    private Date t_bezahltdatum;

    /** nullable persistent field */
    private Date t_storniert;

    /** nullable persistent field */
    private Date t_fibuuebernahme;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id_statistikadresse;

    /** nullable persistent field */
    private BigDecimal n_kurs;

    /** nullable persistent field */
    private Date t_mahnsperrebis;

    /** nullable persistent field */
    private Date t_faelligkeit;

    /** nullable persistent field */
    private Integer reversechargeartId;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRKunde flrstatistikadresse;

    /** nullable persistent field */
    private FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport flrrechnung;

    /** nullable persistent field */
    private FLRReversechargeart flrreversechargeart;

    /** nullable persistent field */
    private FLRLieferschein flrlieferschein;

    /** full constructor */
    public FLRRechnungReport(String mandant_c_nr, Integer i_geschaeftsjahr, String c_nr, String c_zollpapier, String c_mahnungsanmerkung, Date d_belegdatum, String waehrung_c_nr, String status_c_nr, BigDecimal n_wert, BigDecimal n_wertust, BigDecimal n_wertfw, BigDecimal n_wertustfw, Date t_bezahltdatum, Date t_storniert, Date t_fibuuebernahme, Integer kunde_i_id, Integer vertreter_i_id, Integer kostenstelle_i_id, Integer zahlungsziel_i_id, Integer kunde_i_id_statistikadresse, BigDecimal n_kurs, Date t_mahnsperrebis, Date t_faelligkeit, Integer reversechargeartId, FLRKunde flrkunde, com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart, FLRKostenstelle flrkostenstelle, FLRPersonal flrvertreter, FLRKunde flrstatistikadresse, FLRAuftragReport flrauftrag, com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport flrrechnung, FLRReversechargeart flrreversechargeart, FLRLieferschein flrlieferschein) {
        this.mandant_c_nr = mandant_c_nr;
        this.i_geschaeftsjahr = i_geschaeftsjahr;
        this.c_nr = c_nr;
        this.c_zollpapier = c_zollpapier;
        this.c_mahnungsanmerkung = c_mahnungsanmerkung;
        this.d_belegdatum = d_belegdatum;
        this.waehrung_c_nr = waehrung_c_nr;
        this.status_c_nr = status_c_nr;
        this.n_wert = n_wert;
        this.n_wertust = n_wertust;
        this.n_wertfw = n_wertfw;
        this.n_wertustfw = n_wertustfw;
        this.t_bezahltdatum = t_bezahltdatum;
        this.t_storniert = t_storniert;
        this.t_fibuuebernahme = t_fibuuebernahme;
        this.kunde_i_id = kunde_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.kunde_i_id_statistikadresse = kunde_i_id_statistikadresse;
        this.n_kurs = n_kurs;
        this.t_mahnsperrebis = t_mahnsperrebis;
        this.t_faelligkeit = t_faelligkeit;
        this.reversechargeartId = reversechargeartId;
        this.flrkunde = flrkunde;
        this.flrrechnungart = flrrechnungart;
        this.flrkostenstelle = flrkostenstelle;
        this.flrvertreter = flrvertreter;
        this.flrstatistikadresse = flrstatistikadresse;
        this.flrauftrag = flrauftrag;
        this.flrrechnung = flrrechnung;
        this.flrreversechargeart = flrreversechargeart;
        this.flrlieferschein = flrlieferschein;
    }

    /** default constructor */
    public FLRRechnungReport() {
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

    public Integer getI_geschaeftsjahr() {
        return this.i_geschaeftsjahr;
    }

    public void setI_geschaeftsjahr(Integer i_geschaeftsjahr) {
        this.i_geschaeftsjahr = i_geschaeftsjahr;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_zollpapier() {
        return this.c_zollpapier;
    }

    public void setC_zollpapier(String c_zollpapier) {
        this.c_zollpapier = c_zollpapier;
    }

    public String getC_mahnungsanmerkung() {
        return this.c_mahnungsanmerkung;
    }

    public void setC_mahnungsanmerkung(String c_mahnungsanmerkung) {
        this.c_mahnungsanmerkung = c_mahnungsanmerkung;
    }

    public Date getD_belegdatum() {
        return this.d_belegdatum;
    }

    public void setD_belegdatum(Date d_belegdatum) {
        this.d_belegdatum = d_belegdatum;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public BigDecimal getN_wert() {
        return this.n_wert;
    }

    public void setN_wert(BigDecimal n_wert) {
        this.n_wert = n_wert;
    }

    public BigDecimal getN_wertust() {
        return this.n_wertust;
    }

    public void setN_wertust(BigDecimal n_wertust) {
        this.n_wertust = n_wertust;
    }

    public BigDecimal getN_wertfw() {
        return this.n_wertfw;
    }

    public void setN_wertfw(BigDecimal n_wertfw) {
        this.n_wertfw = n_wertfw;
    }

    public BigDecimal getN_wertustfw() {
        return this.n_wertustfw;
    }

    public void setN_wertustfw(BigDecimal n_wertustfw) {
        this.n_wertustfw = n_wertustfw;
    }

    public Date getT_bezahltdatum() {
        return this.t_bezahltdatum;
    }

    public void setT_bezahltdatum(Date t_bezahltdatum) {
        this.t_bezahltdatum = t_bezahltdatum;
    }

    public Date getT_storniert() {
        return this.t_storniert;
    }

    public void setT_storniert(Date t_storniert) {
        this.t_storniert = t_storniert;
    }

    public Date getT_fibuuebernahme() {
        return this.t_fibuuebernahme;
    }

    public void setT_fibuuebernahme(Date t_fibuuebernahme) {
        this.t_fibuuebernahme = t_fibuuebernahme;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getZahlungsziel_i_id() {
        return this.zahlungsziel_i_id;
    }

    public void setZahlungsziel_i_id(Integer zahlungsziel_i_id) {
        this.zahlungsziel_i_id = zahlungsziel_i_id;
    }

    public Integer getKunde_i_id_statistikadresse() {
        return this.kunde_i_id_statistikadresse;
    }

    public void setKunde_i_id_statistikadresse(Integer kunde_i_id_statistikadresse) {
        this.kunde_i_id_statistikadresse = kunde_i_id_statistikadresse;
    }

    public BigDecimal getN_kurs() {
        return this.n_kurs;
    }

    public void setN_kurs(BigDecimal n_kurs) {
        this.n_kurs = n_kurs;
    }

    public Date getT_mahnsperrebis() {
        return this.t_mahnsperrebis;
    }

    public void setT_mahnsperrebis(Date t_mahnsperrebis) {
        this.t_mahnsperrebis = t_mahnsperrebis;
    }

    public Date getT_faelligkeit() {
        return this.t_faelligkeit;
    }

    public void setT_faelligkeit(Date t_faelligkeit) {
        this.t_faelligkeit = t_faelligkeit;
    }

    public Integer getReversechargeartId() {
        return this.reversechargeartId;
    }

    public void setReversechargeartId(Integer reversechargeartId) {
        this.reversechargeartId = reversechargeartId;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart getFlrrechnungart() {
        return this.flrrechnungart;
    }

    public void setFlrrechnungart(com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart) {
        this.flrrechnungart = flrrechnungart;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
    }

    public FLRKunde getFlrstatistikadresse() {
        return this.flrstatistikadresse;
    }

    public void setFlrstatistikadresse(FLRKunde flrstatistikadresse) {
        this.flrstatistikadresse = flrstatistikadresse;
    }

    public FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport getFlrrechnung() {
        return this.flrrechnung;
    }

    public void setFlrrechnung(com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport flrrechnung) {
        this.flrrechnung = flrrechnung;
    }

    public FLRReversechargeart getFlrreversechargeart() {
        return this.flrreversechargeart;
    }

    public void setFlrreversechargeart(FLRReversechargeart flrreversechargeart) {
        this.flrreversechargeart = flrreversechargeart;
    }

    public FLRLieferschein getFlrlieferschein() {
        return this.flrlieferschein;
    }

    public void setFlrlieferschein(FLRLieferschein flrlieferschein) {
        this.flrlieferschein = flrlieferschein;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
