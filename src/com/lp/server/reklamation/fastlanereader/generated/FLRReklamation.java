package com.lp.server.reklamation.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRReklamation implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String reklamationart_c_nr;

    /** nullable persistent field */
    private String c_grund;

    /** nullable persistent field */
    private String x_grund_lang;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private String c_seriennrchargennr;

    /** nullable persistent field */
    private String c_handartikel;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String x_analyse;

    /** persistent field */
    private Date t_belegdatum;

    /** persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Short b_berechtigt;

    /** nullable persistent field */
    private BigDecimal n_kostenmaterial;

    /** nullable persistent field */
    private BigDecimal n_kostenarbeitszeit;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer bestellung_i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id;

    /** nullable persistent field */
    private Integer lieferschein_i_id;

    /** nullable persistent field */
    private String c_kdreklanr;

    /** nullable persistent field */
    private String c_kdlsnr;

    /** nullable persistent field */
    private Integer i_kundeunterart;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe;

    /** nullable persistent field */
    private FLRPersonal flrverursacher;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler;

    /** nullable persistent field */
    private FLRLos flrlos;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** full constructor */
    public FLRReklamation(String c_nr, String mandant_c_nr, String reklamationart_c_nr, String c_grund, String x_grund_lang, String x_kommentar, String c_seriennrchargennr, String c_handartikel, String c_projekt, String status_c_nr, String x_analyse, Date t_belegdatum, Date t_erledigt, BigDecimal n_menge, Short b_berechtigt, BigDecimal n_kostenmaterial, BigDecimal n_kostenarbeitszeit, Integer kostenstelle_i_id, Integer kunde_i_id, Integer lieferant_i_id, Integer los_i_id, Integer bestellung_i_id, Integer rechnung_i_id, Integer lieferschein_i_id, String c_kdreklanr, String c_kdlsnr, Integer i_kundeunterart, Integer projekt_i_id, com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe, FLRPersonal flrverursacher, FLRMaschine flrmaschine, FLRKunde flrkunde, FLRLieferant flrlieferant, com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler, FLRLos flrlos, FLRArtikelliste flrartikel, FLRProjekt flrprojekt) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.reklamationart_c_nr = reklamationart_c_nr;
        this.c_grund = c_grund;
        this.x_grund_lang = x_grund_lang;
        this.x_kommentar = x_kommentar;
        this.c_seriennrchargennr = c_seriennrchargennr;
        this.c_handartikel = c_handartikel;
        this.c_projekt = c_projekt;
        this.status_c_nr = status_c_nr;
        this.x_analyse = x_analyse;
        this.t_belegdatum = t_belegdatum;
        this.t_erledigt = t_erledigt;
        this.n_menge = n_menge;
        this.b_berechtigt = b_berechtigt;
        this.n_kostenmaterial = n_kostenmaterial;
        this.n_kostenarbeitszeit = n_kostenarbeitszeit;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.kunde_i_id = kunde_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.los_i_id = los_i_id;
        this.bestellung_i_id = bestellung_i_id;
        this.rechnung_i_id = rechnung_i_id;
        this.lieferschein_i_id = lieferschein_i_id;
        this.c_kdreklanr = c_kdreklanr;
        this.c_kdlsnr = c_kdlsnr;
        this.i_kundeunterart = i_kundeunterart;
        this.projekt_i_id = projekt_i_id;
        this.flrfehlerangabe = flrfehlerangabe;
        this.flrverursacher = flrverursacher;
        this.flrmaschine = flrmaschine;
        this.flrkunde = flrkunde;
        this.flrlieferant = flrlieferant;
        this.flrfehler = flrfehler;
        this.flrlos = flrlos;
        this.flrartikel = flrartikel;
        this.flrprojekt = flrprojekt;
    }

    /** default constructor */
    public FLRReklamation() {
    }

    /** minimal constructor */
    public FLRReklamation(Date t_belegdatum, Date t_erledigt) {
        this.t_belegdatum = t_belegdatum;
        this.t_erledigt = t_erledigt;
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

    public String getReklamationart_c_nr() {
        return this.reklamationart_c_nr;
    }

    public void setReklamationart_c_nr(String reklamationart_c_nr) {
        this.reklamationart_c_nr = reklamationart_c_nr;
    }

    public String getC_grund() {
        return this.c_grund;
    }

    public void setC_grund(String c_grund) {
        this.c_grund = c_grund;
    }

    public String getX_grund_lang() {
        return this.x_grund_lang;
    }

    public void setX_grund_lang(String x_grund_lang) {
        this.x_grund_lang = x_grund_lang;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public String getC_seriennrchargennr() {
        return this.c_seriennrchargennr;
    }

    public void setC_seriennrchargennr(String c_seriennrchargennr) {
        this.c_seriennrchargennr = c_seriennrchargennr;
    }

    public String getC_handartikel() {
        return this.c_handartikel;
    }

    public void setC_handartikel(String c_handartikel) {
        this.c_handartikel = c_handartikel;
    }

    public String getC_projekt() {
        return this.c_projekt;
    }

    public void setC_projekt(String c_projekt) {
        this.c_projekt = c_projekt;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getX_analyse() {
        return this.x_analyse;
    }

    public void setX_analyse(String x_analyse) {
        this.x_analyse = x_analyse;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Short getB_berechtigt() {
        return this.b_berechtigt;
    }

    public void setB_berechtigt(Short b_berechtigt) {
        this.b_berechtigt = b_berechtigt;
    }

    public BigDecimal getN_kostenmaterial() {
        return this.n_kostenmaterial;
    }

    public void setN_kostenmaterial(BigDecimal n_kostenmaterial) {
        this.n_kostenmaterial = n_kostenmaterial;
    }

    public BigDecimal getN_kostenarbeitszeit() {
        return this.n_kostenarbeitszeit;
    }

    public void setN_kostenarbeitszeit(BigDecimal n_kostenarbeitszeit) {
        this.n_kostenarbeitszeit = n_kostenarbeitszeit;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getBestellung_i_id() {
        return this.bestellung_i_id;
    }

    public void setBestellung_i_id(Integer bestellung_i_id) {
        this.bestellung_i_id = bestellung_i_id;
    }

    public Integer getRechnung_i_id() {
        return this.rechnung_i_id;
    }

    public void setRechnung_i_id(Integer rechnung_i_id) {
        this.rechnung_i_id = rechnung_i_id;
    }

    public Integer getLieferschein_i_id() {
        return this.lieferschein_i_id;
    }

    public void setLieferschein_i_id(Integer lieferschein_i_id) {
        this.lieferschein_i_id = lieferschein_i_id;
    }

    public String getC_kdreklanr() {
        return this.c_kdreklanr;
    }

    public void setC_kdreklanr(String c_kdreklanr) {
        this.c_kdreklanr = c_kdreklanr;
    }

    public String getC_kdlsnr() {
        return this.c_kdlsnr;
    }

    public void setC_kdlsnr(String c_kdlsnr) {
        this.c_kdlsnr = c_kdlsnr;
    }

    public Integer getI_kundeunterart() {
        return this.i_kundeunterart;
    }

    public void setI_kundeunterart(Integer i_kundeunterart) {
        this.i_kundeunterart = i_kundeunterart;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe getFlrfehlerangabe() {
        return this.flrfehlerangabe;
    }

    public void setFlrfehlerangabe(com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe) {
        this.flrfehlerangabe = flrfehlerangabe;
    }

    public FLRPersonal getFlrverursacher() {
        return this.flrverursacher;
    }

    public void setFlrverursacher(FLRPersonal flrverursacher) {
        this.flrverursacher = flrverursacher;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.reklamation.fastlanereader.generated.FLRFehler getFlrfehler() {
        return this.flrfehler;
    }

    public void setFlrfehler(com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler) {
        this.flrfehler = flrfehler;
    }

    public FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
