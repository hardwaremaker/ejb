package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String auftragart_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_liefertermin_timestamp;

    /** nullable persistent field */
    private Date t_auftragsfreigabe;

    /** nullable persistent field */
    private Date t_bestelldatum;

    /** nullable persistent field */
    private Date t_finaltermin;

    /** nullable persistent field */
    private Date t_finaltermin_timestamp;

    /** nullable persistent field */
    private Integer bestellung_i_id_anderermandant;

    /** nullable persistent field */
    private Date t_wunschtermin;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private String auftragstatus_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_auftragswaehrung;

    /** nullable persistent field */
    private BigDecimal n_gesamtauftragswertinauftragswaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuauftragswaehrung;

    /** nullable persistent field */
    private Integer kunde_i_id_auftragsadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_rechnungsadresse;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_kunde;

    /** nullable persistent field */
    private Integer lager_i_id_abbuchungslager;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Integer personal_i_id_erledigt;

    /** nullable persistent field */
    private Short b_teillieferungmoeglich;

    /** nullable persistent field */
    private Short b_poenale;

    /** nullable persistent field */
    private Short b_rohs;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_lieferterminunverbindlich;

    /** nullable persistent field */
    private Double f_erfuellungsgrad;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private Date t_verrechenbar;

    /** nullable persistent field */
    private Integer i_aenderungsauftrag_version;

    /** nullable persistent field */
    private Date t_lauftermin;

    /** nullable persistent field */
    private Date t_lauftermin_bis;

    /** nullable persistent field */
    private String auftragwiederholungsintervall_c_nr;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar;

    /** nullable persistent field */
    private FLRPersonal flrpersonalverrechenbar;

    /** nullable persistent field */
    private FLRPersonal flrpersonalauftragsfreigabe;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRAnsprechpartner flrkundeansprechpartner;

    /** nullable persistent field */
    private FLRKunde flrkunderechnungsadresse;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRKunde flrkundelieferadresse;

    /** nullable persistent field */
    private FLRAngebot flrangebot;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag;

    /** persistent field */
    private Set flrauftragkostenstellen;

    /** full constructor */
    public FLRAuftragReport(String mandant_c_nr, String c_nr, String auftragart_c_nr, String c_bez, String c_bestellnummer, Date t_liefertermin, Date t_liefertermin_timestamp, Date t_auftragsfreigabe, Date t_bestelldatum, Date t_finaltermin, Date t_finaltermin_timestamp, Integer bestellung_i_id_anderermandant, Date t_wunschtermin, Date t_belegdatum, String auftragstatus_c_nr, String waehrung_c_nr_auftragswaehrung, BigDecimal n_gesamtauftragswertinauftragswaehrung, Double f_wechselkursmandantwaehrungzuauftragswaehrung, Integer kunde_i_id_auftragsadresse, Integer kunde_i_id_lieferadresse, Integer kunde_i_id_rechnungsadresse, Integer ansprechpartner_i_id_kunde, Integer lager_i_id_abbuchungslager, Integer kostenstelle_i_id, Integer zahlungsziel_i_id, Integer vertreter_i_id, Date t_erledigt, Integer personal_i_id_erledigt, Short b_teillieferungmoeglich, Short b_poenale, Short b_rohs, Short b_versteckt, Short b_lieferterminunverbindlich, Double f_erfuellungsgrad, String x_internerkommentar, Date t_verrechenbar, Integer i_aenderungsauftrag_version, Date t_lauftermin, Date t_lauftermin_bis, String auftragwiederholungsintervall_c_nr, com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar, FLRPersonal flrpersonalverrechenbar, FLRPersonal flrpersonalauftragsfreigabe, FLRKunde flrkunde, FLRAnsprechpartner flrkundeansprechpartner, FLRKunde flrkunderechnungsadresse, FLRKostenstelle flrkostenstelle, FLRPersonal flrvertreter, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche, FLRProjekt flrprojekt, FLRKunde flrkundelieferadresse, FLRAngebot flrangebot, com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag, Set flrauftragkostenstellen) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.auftragart_c_nr = auftragart_c_nr;
        this.c_bez = c_bez;
        this.c_bestellnummer = c_bestellnummer;
        this.t_liefertermin = t_liefertermin;
        this.t_liefertermin_timestamp = t_liefertermin_timestamp;
        this.t_auftragsfreigabe = t_auftragsfreigabe;
        this.t_bestelldatum = t_bestelldatum;
        this.t_finaltermin = t_finaltermin;
        this.t_finaltermin_timestamp = t_finaltermin_timestamp;
        this.bestellung_i_id_anderermandant = bestellung_i_id_anderermandant;
        this.t_wunschtermin = t_wunschtermin;
        this.t_belegdatum = t_belegdatum;
        this.auftragstatus_c_nr = auftragstatus_c_nr;
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
        this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.t_erledigt = t_erledigt;
        this.personal_i_id_erledigt = personal_i_id_erledigt;
        this.b_teillieferungmoeglich = b_teillieferungmoeglich;
        this.b_poenale = b_poenale;
        this.b_rohs = b_rohs;
        this.b_versteckt = b_versteckt;
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
        this.f_erfuellungsgrad = f_erfuellungsgrad;
        this.x_internerkommentar = x_internerkommentar;
        this.t_verrechenbar = t_verrechenbar;
        this.i_aenderungsauftrag_version = i_aenderungsauftrag_version;
        this.t_lauftermin = t_lauftermin;
        this.t_lauftermin_bis = t_lauftermin_bis;
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
        this.flrverrechenbar = flrverrechenbar;
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
        this.flrpersonalauftragsfreigabe = flrpersonalauftragsfreigabe;
        this.flrkunde = flrkunde;
        this.flrkundeansprechpartner = flrkundeansprechpartner;
        this.flrkunderechnungsadresse = flrkunderechnungsadresse;
        this.flrkostenstelle = flrkostenstelle;
        this.flrvertreter = flrvertreter;
        this.flrauftragtextsuche = flrauftragtextsuche;
        this.flrprojekt = flrprojekt;
        this.flrkundelieferadresse = flrkundelieferadresse;
        this.flrangebot = flrangebot;
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
        this.flrauftragkostenstellen = flrauftragkostenstellen;
    }

    /** default constructor */
    public FLRAuftragReport() {
    }

    /** minimal constructor */
    public FLRAuftragReport(Set flrauftragkostenstellen) {
        this.flrauftragkostenstellen = flrauftragkostenstellen;
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

    public String getAuftragart_c_nr() {
        return this.auftragart_c_nr;
    }

    public void setAuftragart_c_nr(String auftragart_c_nr) {
        this.auftragart_c_nr = auftragart_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_liefertermin_timestamp() {
        return this.t_liefertermin_timestamp;
    }

    public void setT_liefertermin_timestamp(Date t_liefertermin_timestamp) {
        this.t_liefertermin_timestamp = t_liefertermin_timestamp;
    }

    public Date getT_auftragsfreigabe() {
        return this.t_auftragsfreigabe;
    }

    public void setT_auftragsfreigabe(Date t_auftragsfreigabe) {
        this.t_auftragsfreigabe = t_auftragsfreigabe;
    }

    public Date getT_bestelldatum() {
        return this.t_bestelldatum;
    }

    public void setT_bestelldatum(Date t_bestelldatum) {
        this.t_bestelldatum = t_bestelldatum;
    }

    public Date getT_finaltermin() {
        return this.t_finaltermin;
    }

    public void setT_finaltermin(Date t_finaltermin) {
        this.t_finaltermin = t_finaltermin;
    }

    public Date getT_finaltermin_timestamp() {
        return this.t_finaltermin_timestamp;
    }

    public void setT_finaltermin_timestamp(Date t_finaltermin_timestamp) {
        this.t_finaltermin_timestamp = t_finaltermin_timestamp;
    }

    public Integer getBestellung_i_id_anderermandant() {
        return this.bestellung_i_id_anderermandant;
    }

    public void setBestellung_i_id_anderermandant(Integer bestellung_i_id_anderermandant) {
        this.bestellung_i_id_anderermandant = bestellung_i_id_anderermandant;
    }

    public Date getT_wunschtermin() {
        return this.t_wunschtermin;
    }

    public void setT_wunschtermin(Date t_wunschtermin) {
        this.t_wunschtermin = t_wunschtermin;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public String getAuftragstatus_c_nr() {
        return this.auftragstatus_c_nr;
    }

    public void setAuftragstatus_c_nr(String auftragstatus_c_nr) {
        this.auftragstatus_c_nr = auftragstatus_c_nr;
    }

    public String getWaehrung_c_nr_auftragswaehrung() {
        return this.waehrung_c_nr_auftragswaehrung;
    }

    public void setWaehrung_c_nr_auftragswaehrung(String waehrung_c_nr_auftragswaehrung) {
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
    }

    public BigDecimal getN_gesamtauftragswertinauftragswaehrung() {
        return this.n_gesamtauftragswertinauftragswaehrung;
    }

    public void setN_gesamtauftragswertinauftragswaehrung(BigDecimal n_gesamtauftragswertinauftragswaehrung) {
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
    }

    public Double getF_wechselkursmandantwaehrungzuauftragswaehrung() {
        return this.f_wechselkursmandantwaehrungzuauftragswaehrung;
    }

    public void setF_wechselkursmandantwaehrungzuauftragswaehrung(Double f_wechselkursmandantwaehrungzuauftragswaehrung) {
        this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
    }

    public Integer getKunde_i_id_auftragsadresse() {
        return this.kunde_i_id_auftragsadresse;
    }

    public void setKunde_i_id_auftragsadresse(Integer kunde_i_id_auftragsadresse) {
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
    }

    public Integer getKunde_i_id_lieferadresse() {
        return this.kunde_i_id_lieferadresse;
    }

    public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
    }

    public Integer getKunde_i_id_rechnungsadresse() {
        return this.kunde_i_id_rechnungsadresse;
    }

    public void setKunde_i_id_rechnungsadresse(Integer kunde_i_id_rechnungsadresse) {
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
    }

    public Integer getAnsprechpartner_i_id_kunde() {
        return this.ansprechpartner_i_id_kunde;
    }

    public void setAnsprechpartner_i_id_kunde(Integer ansprechpartner_i_id_kunde) {
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
    }

    public Integer getLager_i_id_abbuchungslager() {
        return this.lager_i_id_abbuchungslager;
    }

    public void setLager_i_id_abbuchungslager(Integer lager_i_id_abbuchungslager) {
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
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

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Integer getPersonal_i_id_erledigt() {
        return this.personal_i_id_erledigt;
    }

    public void setPersonal_i_id_erledigt(Integer personal_i_id_erledigt) {
        this.personal_i_id_erledigt = personal_i_id_erledigt;
    }

    public Short getB_teillieferungmoeglich() {
        return this.b_teillieferungmoeglich;
    }

    public void setB_teillieferungmoeglich(Short b_teillieferungmoeglich) {
        this.b_teillieferungmoeglich = b_teillieferungmoeglich;
    }

    public Short getB_poenale() {
        return this.b_poenale;
    }

    public void setB_poenale(Short b_poenale) {
        this.b_poenale = b_poenale;
    }

    public Short getB_rohs() {
        return this.b_rohs;
    }

    public void setB_rohs(Short b_rohs) {
        this.b_rohs = b_rohs;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_lieferterminunverbindlich() {
        return this.b_lieferterminunverbindlich;
    }

    public void setB_lieferterminunverbindlich(Short b_lieferterminunverbindlich) {
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
    }

    public Double getF_erfuellungsgrad() {
        return this.f_erfuellungsgrad;
    }

    public void setF_erfuellungsgrad(Double f_erfuellungsgrad) {
        this.f_erfuellungsgrad = f_erfuellungsgrad;
    }

    public String getX_internerkommentar() {
        return this.x_internerkommentar;
    }

    public void setX_internerkommentar(String x_internerkommentar) {
        this.x_internerkommentar = x_internerkommentar;
    }

    public Date getT_verrechenbar() {
        return this.t_verrechenbar;
    }

    public void setT_verrechenbar(Date t_verrechenbar) {
        this.t_verrechenbar = t_verrechenbar;
    }

    public Integer getI_aenderungsauftrag_version() {
        return this.i_aenderungsauftrag_version;
    }

    public void setI_aenderungsauftrag_version(Integer i_aenderungsauftrag_version) {
        this.i_aenderungsauftrag_version = i_aenderungsauftrag_version;
    }

    public Date getT_lauftermin() {
        return this.t_lauftermin;
    }

    public void setT_lauftermin(Date t_lauftermin) {
        this.t_lauftermin = t_lauftermin;
    }

    public Date getT_lauftermin_bis() {
        return this.t_lauftermin_bis;
    }

    public void setT_lauftermin_bis(Date t_lauftermin_bis) {
        this.t_lauftermin_bis = t_lauftermin_bis;
    }

    public String getAuftragwiederholungsintervall_c_nr() {
        return this.auftragwiederholungsintervall_c_nr;
    }

    public void setAuftragwiederholungsintervall_c_nr(String auftragwiederholungsintervall_c_nr) {
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar getFlrverrechenbar() {
        return this.flrverrechenbar;
    }

    public void setFlrverrechenbar(com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar) {
        this.flrverrechenbar = flrverrechenbar;
    }

    public FLRPersonal getFlrpersonalverrechenbar() {
        return this.flrpersonalverrechenbar;
    }

    public void setFlrpersonalverrechenbar(FLRPersonal flrpersonalverrechenbar) {
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
    }

    public FLRPersonal getFlrpersonalauftragsfreigabe() {
        return this.flrpersonalauftragsfreigabe;
    }

    public void setFlrpersonalauftragsfreigabe(FLRPersonal flrpersonalauftragsfreigabe) {
        this.flrpersonalauftragsfreigabe = flrpersonalauftragsfreigabe;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRAnsprechpartner getFlrkundeansprechpartner() {
        return this.flrkundeansprechpartner;
    }

    public void setFlrkundeansprechpartner(FLRAnsprechpartner flrkundeansprechpartner) {
        this.flrkundeansprechpartner = flrkundeansprechpartner;
    }

    public FLRKunde getFlrkunderechnungsadresse() {
        return this.flrkunderechnungsadresse;
    }

    public void setFlrkunderechnungsadresse(FLRKunde flrkunderechnungsadresse) {
        this.flrkunderechnungsadresse = flrkunderechnungsadresse;
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

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche getFlrauftragtextsuche() {
        return this.flrauftragtextsuche;
    }

    public void setFlrauftragtextsuche(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche) {
        this.flrauftragtextsuche = flrauftragtextsuche;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRKunde getFlrkundelieferadresse() {
        return this.flrkundelieferadresse;
    }

    public void setFlrkundelieferadresse(FLRKunde flrkundelieferadresse) {
        this.flrkundelieferadresse = flrkundelieferadresse;
    }

    public FLRAngebot getFlrangebot() {
        return this.flrangebot;
    }

    public void setFlrangebot(FLRAngebot flrangebot) {
        this.flrangebot = flrangebot;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag getFlrauftrag_rahmenauftrag() {
        return this.flrauftrag_rahmenauftrag;
    }

    public void setFlrauftrag_rahmenauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag) {
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
    }

    public Set getFlrauftragkostenstellen() {
        return this.flrauftragkostenstellen;
    }

    public void setFlrauftragkostenstellen(Set flrauftragkostenstellen) {
        this.flrauftragkostenstellen = flrauftragkostenstellen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
