package com.lp.server.angebot.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAngebot implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_kundenanfrage;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private String angebotstatus_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_angebotswaehrung;

    /** nullable persistent field */
    private BigDecimal n_gesamtangebotswertinangebotswaehrung;

    /** nullable persistent field */
    private Integer kunde_i_id_angebotsadresse;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id2;

    /** nullable persistent field */
    private Date t_realisierungstermin;

    /** nullable persistent field */
    private String angeboterledigungsgrund_c_nr;

    /** nullable persistent field */
    private Double f_auftragswahrscheinlichkeit;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuangebotswaehrung;

    /** nullable persistent field */
    private Date t_nachfasstermin;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private String x_externerkommentar;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_kunde;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRPersonal flrvertreter2;

    /** nullable persistent field */
    private com.lp.server.angebot.fastlanereader.generated.FLRAkquisestatus flrakquisestatus;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private com.lp.server.angebot.fastlanereader.generated.FLRAngebottextsuche flrangebottextsuche;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRKunde flrkunde_rechnungsadresse;

    /** full constructor */
    public FLRAngebot(String mandant_c_nr, String c_nr, String c_bez, String c_kundenanfrage, Date t_belegdatum, String angebotstatus_c_nr, String waehrung_c_nr_angebotswaehrung, BigDecimal n_gesamtangebotswertinangebotswaehrung, Integer kunde_i_id_angebotsadresse, Integer kostenstelle_i_id, Integer projekt_i_id, Integer vertreter_i_id, Integer vertreter_i_id2, Date t_realisierungstermin, String angeboterledigungsgrund_c_nr, Double f_auftragswahrscheinlichkeit, Double f_wechselkursmandantwaehrungzuangebotswaehrung, Date t_nachfasstermin, Date t_manuellerledigt, String x_internerkommentar, String x_externerkommentar, Date t_versandzeitpunkt, String c_versandtype, Integer ansprechpartner_i_id_kunde, FLRKunde flrkunde, FLRKostenstelle flrkostenstelle, FLRPersonal flrvertreter, FLRPersonal flrvertreter2, com.lp.server.angebot.fastlanereader.generated.FLRAkquisestatus flrakquisestatus, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, com.lp.server.angebot.fastlanereader.generated.FLRAngebottextsuche flrangebottextsuche, FLRProjekt flrprojekt, FLRKunde flrkunde_rechnungsadresse) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.c_kundenanfrage = c_kundenanfrage;
        this.t_belegdatum = t_belegdatum;
        this.angebotstatus_c_nr = angebotstatus_c_nr;
        this.waehrung_c_nr_angebotswaehrung = waehrung_c_nr_angebotswaehrung;
        this.n_gesamtangebotswertinangebotswaehrung = n_gesamtangebotswertinangebotswaehrung;
        this.kunde_i_id_angebotsadresse = kunde_i_id_angebotsadresse;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.projekt_i_id = projekt_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.vertreter_i_id2 = vertreter_i_id2;
        this.t_realisierungstermin = t_realisierungstermin;
        this.angeboterledigungsgrund_c_nr = angeboterledigungsgrund_c_nr;
        this.f_auftragswahrscheinlichkeit = f_auftragswahrscheinlichkeit;
        this.f_wechselkursmandantwaehrungzuangebotswaehrung = f_wechselkursmandantwaehrungzuangebotswaehrung;
        this.t_nachfasstermin = t_nachfasstermin;
        this.t_manuellerledigt = t_manuellerledigt;
        this.x_internerkommentar = x_internerkommentar;
        this.x_externerkommentar = x_externerkommentar;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.c_versandtype = c_versandtype;
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
        this.flrkunde = flrkunde;
        this.flrkostenstelle = flrkostenstelle;
        this.flrvertreter = flrvertreter;
        this.flrvertreter2 = flrvertreter2;
        this.flrakquisestatus = flrakquisestatus;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrangebottextsuche = flrangebottextsuche;
        this.flrprojekt = flrprojekt;
        this.flrkunde_rechnungsadresse = flrkunde_rechnungsadresse;
    }

    /** default constructor */
    public FLRAngebot() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_kundenanfrage() {
        return this.c_kundenanfrage;
    }

    public void setC_kundenanfrage(String c_kundenanfrage) {
        this.c_kundenanfrage = c_kundenanfrage;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public String getAngebotstatus_c_nr() {
        return this.angebotstatus_c_nr;
    }

    public void setAngebotstatus_c_nr(String angebotstatus_c_nr) {
        this.angebotstatus_c_nr = angebotstatus_c_nr;
    }

    public String getWaehrung_c_nr_angebotswaehrung() {
        return this.waehrung_c_nr_angebotswaehrung;
    }

    public void setWaehrung_c_nr_angebotswaehrung(String waehrung_c_nr_angebotswaehrung) {
        this.waehrung_c_nr_angebotswaehrung = waehrung_c_nr_angebotswaehrung;
    }

    public BigDecimal getN_gesamtangebotswertinangebotswaehrung() {
        return this.n_gesamtangebotswertinangebotswaehrung;
    }

    public void setN_gesamtangebotswertinangebotswaehrung(BigDecimal n_gesamtangebotswertinangebotswaehrung) {
        this.n_gesamtangebotswertinangebotswaehrung = n_gesamtangebotswertinangebotswaehrung;
    }

    public Integer getKunde_i_id_angebotsadresse() {
        return this.kunde_i_id_angebotsadresse;
    }

    public void setKunde_i_id_angebotsadresse(Integer kunde_i_id_angebotsadresse) {
        this.kunde_i_id_angebotsadresse = kunde_i_id_angebotsadresse;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public Integer getVertreter_i_id2() {
        return this.vertreter_i_id2;
    }

    public void setVertreter_i_id2(Integer vertreter_i_id2) {
        this.vertreter_i_id2 = vertreter_i_id2;
    }

    public Date getT_realisierungstermin() {
        return this.t_realisierungstermin;
    }

    public void setT_realisierungstermin(Date t_realisierungstermin) {
        this.t_realisierungstermin = t_realisierungstermin;
    }

    public String getAngeboterledigungsgrund_c_nr() {
        return this.angeboterledigungsgrund_c_nr;
    }

    public void setAngeboterledigungsgrund_c_nr(String angeboterledigungsgrund_c_nr) {
        this.angeboterledigungsgrund_c_nr = angeboterledigungsgrund_c_nr;
    }

    public Double getF_auftragswahrscheinlichkeit() {
        return this.f_auftragswahrscheinlichkeit;
    }

    public void setF_auftragswahrscheinlichkeit(Double f_auftragswahrscheinlichkeit) {
        this.f_auftragswahrscheinlichkeit = f_auftragswahrscheinlichkeit;
    }

    public Double getF_wechselkursmandantwaehrungzuangebotswaehrung() {
        return this.f_wechselkursmandantwaehrungzuangebotswaehrung;
    }

    public void setF_wechselkursmandantwaehrungzuangebotswaehrung(Double f_wechselkursmandantwaehrungzuangebotswaehrung) {
        this.f_wechselkursmandantwaehrungzuangebotswaehrung = f_wechselkursmandantwaehrungzuangebotswaehrung;
    }

    public Date getT_nachfasstermin() {
        return this.t_nachfasstermin;
    }

    public void setT_nachfasstermin(Date t_nachfasstermin) {
        this.t_nachfasstermin = t_nachfasstermin;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
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

    public Integer getAnsprechpartner_i_id_kunde() {
        return this.ansprechpartner_i_id_kunde;
    }

    public void setAnsprechpartner_i_id_kunde(Integer ansprechpartner_i_id_kunde) {
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
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

    public FLRPersonal getFlrvertreter2() {
        return this.flrvertreter2;
    }

    public void setFlrvertreter2(FLRPersonal flrvertreter2) {
        this.flrvertreter2 = flrvertreter2;
    }

    public com.lp.server.angebot.fastlanereader.generated.FLRAkquisestatus getFlrakquisestatus() {
        return this.flrakquisestatus;
    }

    public void setFlrakquisestatus(com.lp.server.angebot.fastlanereader.generated.FLRAkquisestatus flrakquisestatus) {
        this.flrakquisestatus = flrakquisestatus;
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

    public com.lp.server.angebot.fastlanereader.generated.FLRAngebottextsuche getFlrangebottextsuche() {
        return this.flrangebottextsuche;
    }

    public void setFlrangebottextsuche(com.lp.server.angebot.fastlanereader.generated.FLRAngebottextsuche flrangebottextsuche) {
        this.flrangebottextsuche = flrangebottextsuche;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRKunde getFlrkunde_rechnungsadresse() {
        return this.flrkunde_rechnungsadresse;
    }

    public void setFlrkunde_rechnungsadresse(FLRKunde flrkunde_rechnungsadresse) {
        this.flrkunde_rechnungsadresse = flrkunde_rechnungsadresse;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
