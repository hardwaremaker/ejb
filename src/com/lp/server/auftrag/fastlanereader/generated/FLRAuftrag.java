package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRPaneldatenckey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String auftragart_c_nr;

    /** nullable persistent field */
    private String auftragstatus_c_nr;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id2;

    /** nullable persistent field */
    private Integer bestellung_i_id_anderermandant;

    /** nullable persistent field */
    private Integer lager_i_id_abbuchungslager;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Integer kunde_i_id_auftragsadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_rechnungsadresse;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_auftragsfreigabe;

    /** nullable persistent field */
    private Date t_finaltermin;

    /** nullable persistent field */
    private Date t_lauftermin;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Short b_poenale;

    /** nullable persistent field */
    private Short b_lieferterminunverbindlich;

    /** nullable persistent field */
    private BigDecimal n_indexanpassung;

    /** nullable persistent field */
    private BigDecimal n_gesamtauftragswertinauftragswaehrung;

    /** nullable persistent field */
    private String waehrung_c_nr_auftragswaehrung;

    /** nullable persistent field */
    private Short b_rohs;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private Date t_verrechenbar;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_kunde;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_lieferadresse;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private String x_externerkommentar;

    /** nullable persistent field */
    private FLRPersonal flrpersonalverrechenbar;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRPersonal flrvertreter2;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche;

    /** nullable persistent field */
    private FLRPaneldatenckey flrpaneldatenckey;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragbegruendung flrbegruendung;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRAngebot flrangebot;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** nullable persistent field */
    private FLRKunde flrlieferkunde;

    /** persistent field */
    private Set flrauftragteilnehmer;

    /** full constructor */
    public FLRAuftrag(String mandant_c_nr, String c_nr, String auftragart_c_nr, String auftragstatus_c_nr, String c_bestellnummer, Integer vertreter_i_id, Integer vertreter_i_id2, Integer bestellung_i_id_anderermandant, Integer lager_i_id_abbuchungslager, String c_bez, Integer kunde_i_id_auftragsadresse, Integer kunde_i_id_lieferadresse, Integer kunde_i_id_rechnungsadresse, Date t_liefertermin, Date t_auftragsfreigabe, Date t_finaltermin, Date t_lauftermin, Date t_belegdatum, Short b_poenale, Short b_lieferterminunverbindlich, BigDecimal n_indexanpassung, BigDecimal n_gesamtauftragswertinauftragswaehrung, String waehrung_c_nr_auftragswaehrung, Short b_rohs, Short b_versteckt, Date t_versandzeitpunkt, Date t_verrechenbar, Integer projekt_i_id, String c_versandtype, Integer ansprechpartner_i_id_kunde, Integer ansprechpartner_i_id_lieferadresse, String x_internerkommentar, String x_externerkommentar, FLRPersonal flrpersonalverrechenbar, FLRKunde flrkunde, FLRPersonal flrvertreter, FLRPersonal flrvertreter2, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche, FLRPaneldatenckey flrpaneldatenckey, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragbegruendung flrbegruendung, com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar, FLRProjekt flrprojekt, FLRAngebot flrangebot, FLRLager flrlager, FLRKunde flrlieferkunde, Set flrauftragteilnehmer) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.auftragart_c_nr = auftragart_c_nr;
        this.auftragstatus_c_nr = auftragstatus_c_nr;
        this.c_bestellnummer = c_bestellnummer;
        this.vertreter_i_id = vertreter_i_id;
        this.vertreter_i_id2 = vertreter_i_id2;
        this.bestellung_i_id_anderermandant = bestellung_i_id_anderermandant;
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
        this.c_bez = c_bez;
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
        this.t_liefertermin = t_liefertermin;
        this.t_auftragsfreigabe = t_auftragsfreigabe;
        this.t_finaltermin = t_finaltermin;
        this.t_lauftermin = t_lauftermin;
        this.t_belegdatum = t_belegdatum;
        this.b_poenale = b_poenale;
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
        this.n_indexanpassung = n_indexanpassung;
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
        this.b_rohs = b_rohs;
        this.b_versteckt = b_versteckt;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.t_verrechenbar = t_verrechenbar;
        this.projekt_i_id = projekt_i_id;
        this.c_versandtype = c_versandtype;
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
        this.ansprechpartner_i_id_lieferadresse = ansprechpartner_i_id_lieferadresse;
        this.x_internerkommentar = x_internerkommentar;
        this.x_externerkommentar = x_externerkommentar;
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
        this.flrkunde = flrkunde;
        this.flrvertreter = flrvertreter;
        this.flrvertreter2 = flrvertreter2;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrauftragtextsuche = flrauftragtextsuche;
        this.flrpaneldatenckey = flrpaneldatenckey;
        this.flrbegruendung = flrbegruendung;
        this.flrverrechenbar = flrverrechenbar;
        this.flrprojekt = flrprojekt;
        this.flrangebot = flrangebot;
        this.flrlager = flrlager;
        this.flrlieferkunde = flrlieferkunde;
        this.flrauftragteilnehmer = flrauftragteilnehmer;
    }

    /** default constructor */
    public FLRAuftrag() {
    }

    /** minimal constructor */
    public FLRAuftrag(Set flrauftragteilnehmer) {
        this.flrauftragteilnehmer = flrauftragteilnehmer;
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

    public String getAuftragstatus_c_nr() {
        return this.auftragstatus_c_nr;
    }

    public void setAuftragstatus_c_nr(String auftragstatus_c_nr) {
        this.auftragstatus_c_nr = auftragstatus_c_nr;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
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

    public Integer getBestellung_i_id_anderermandant() {
        return this.bestellung_i_id_anderermandant;
    }

    public void setBestellung_i_id_anderermandant(Integer bestellung_i_id_anderermandant) {
        this.bestellung_i_id_anderermandant = bestellung_i_id_anderermandant;
    }

    public Integer getLager_i_id_abbuchungslager() {
        return this.lager_i_id_abbuchungslager;
    }

    public void setLager_i_id_abbuchungslager(Integer lager_i_id_abbuchungslager) {
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
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

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_auftragsfreigabe() {
        return this.t_auftragsfreigabe;
    }

    public void setT_auftragsfreigabe(Date t_auftragsfreigabe) {
        this.t_auftragsfreigabe = t_auftragsfreigabe;
    }

    public Date getT_finaltermin() {
        return this.t_finaltermin;
    }

    public void setT_finaltermin(Date t_finaltermin) {
        this.t_finaltermin = t_finaltermin;
    }

    public Date getT_lauftermin() {
        return this.t_lauftermin;
    }

    public void setT_lauftermin(Date t_lauftermin) {
        this.t_lauftermin = t_lauftermin;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Short getB_poenale() {
        return this.b_poenale;
    }

    public void setB_poenale(Short b_poenale) {
        this.b_poenale = b_poenale;
    }

    public Short getB_lieferterminunverbindlich() {
        return this.b_lieferterminunverbindlich;
    }

    public void setB_lieferterminunverbindlich(Short b_lieferterminunverbindlich) {
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
    }

    public BigDecimal getN_indexanpassung() {
        return this.n_indexanpassung;
    }

    public void setN_indexanpassung(BigDecimal n_indexanpassung) {
        this.n_indexanpassung = n_indexanpassung;
    }

    public BigDecimal getN_gesamtauftragswertinauftragswaehrung() {
        return this.n_gesamtauftragswertinauftragswaehrung;
    }

    public void setN_gesamtauftragswertinauftragswaehrung(BigDecimal n_gesamtauftragswertinauftragswaehrung) {
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
    }

    public String getWaehrung_c_nr_auftragswaehrung() {
        return this.waehrung_c_nr_auftragswaehrung;
    }

    public void setWaehrung_c_nr_auftragswaehrung(String waehrung_c_nr_auftragswaehrung) {
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
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

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public Date getT_verrechenbar() {
        return this.t_verrechenbar;
    }

    public void setT_verrechenbar(Date t_verrechenbar) {
        this.t_verrechenbar = t_verrechenbar;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
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

    public Integer getAnsprechpartner_i_id_lieferadresse() {
        return this.ansprechpartner_i_id_lieferadresse;
    }

    public void setAnsprechpartner_i_id_lieferadresse(Integer ansprechpartner_i_id_lieferadresse) {
        this.ansprechpartner_i_id_lieferadresse = ansprechpartner_i_id_lieferadresse;
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

    public FLRPersonal getFlrpersonalverrechenbar() {
        return this.flrpersonalverrechenbar;
    }

    public void setFlrpersonalverrechenbar(FLRPersonal flrpersonalverrechenbar) {
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
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

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche getFlrauftragtextsuche() {
        return this.flrauftragtextsuche;
    }

    public void setFlrauftragtextsuche(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche) {
        this.flrauftragtextsuche = flrauftragtextsuche;
    }

    public FLRPaneldatenckey getFlrpaneldatenckey() {
        return this.flrpaneldatenckey;
    }

    public void setFlrpaneldatenckey(FLRPaneldatenckey flrpaneldatenckey) {
        this.flrpaneldatenckey = flrpaneldatenckey;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragbegruendung getFlrbegruendung() {
        return this.flrbegruendung;
    }

    public void setFlrbegruendung(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragbegruendung flrbegruendung) {
        this.flrbegruendung = flrbegruendung;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar getFlrverrechenbar() {
        return this.flrverrechenbar;
    }

    public void setFlrverrechenbar(com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar) {
        this.flrverrechenbar = flrverrechenbar;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRAngebot getFlrangebot() {
        return this.flrangebot;
    }

    public void setFlrangebot(FLRAngebot flrangebot) {
        this.flrangebot = flrangebot;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLRKunde getFlrlieferkunde() {
        return this.flrlieferkunde;
    }

    public void setFlrlieferkunde(FLRKunde flrlieferkunde) {
        this.flrlieferkunde = flrlieferkunde;
    }

    public Set getFlrauftragteilnehmer() {
        return this.flrauftragteilnehmer;
    }

    public void setFlrauftragteilnehmer(Set flrauftragteilnehmer) {
        this.flrauftragteilnehmer = flrauftragteilnehmer;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
