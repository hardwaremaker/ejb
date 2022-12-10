package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.fastlanereader.generated.FLRSpediteur;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragOD implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private String auftragart_c_nr;

    /** nullable persistent field */
    private String auftragstatus_c_nr;

    /** nullable persistent field */
    private Integer kunde_i_id_auftragsadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Date t_auftragsfreigabe;

    /** nullable persistent field */
    private Date t_lauftermin;

    /** nullable persistent field */
    private Date t_lauftermin_bis;

    /** nullable persistent field */
    private String auftragwiederholungsintervall_c_nr;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_finaltermin;

    /** nullable persistent field */
    private Date t_wunschtermin;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Short b_poenale;

    /** nullable persistent field */
    private Short b_rohs;

    /** nullable persistent field */
    private BigDecimal n_gesamtauftragswertinauftragswaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuauftragswaehrung;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private String x_externerkommentar;

    /** nullable persistent field */
    private Short b_lieferterminunverbindlich;

    /** nullable persistent field */
    private Date t_verrechenbar;

    /** nullable persistent field */
    private String waehrung_c_nr_auftragswaehrung;

    /** nullable persistent field */
    private FLRPersonal flrpersonalverrechenbar;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRKunde flrkundelieferadresse;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRSpediteur flrspediteur;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag_rahmenauftrag;

    /** nullable persistent field */
    private FLRPersonal flrpersonalauftragsfreigabe;

    /** persistent field */
    private Set flrauftragpositionen;

    /** persistent field */
    private Set flrbestellung;

    /** full constructor */
    public FLRAuftragOD(String mandant_c_nr, String c_nr, String c_bez, String c_bestellnummer, String auftragart_c_nr, String auftragstatus_c_nr, Integer kunde_i_id_auftragsadresse, Integer kunde_i_id_lieferadresse, Integer kostenstelle_i_id, Integer vertreter_i_id, Integer zahlungsziel_i_id, Date t_auftragsfreigabe, Date t_lauftermin, Date t_lauftermin_bis, String auftragwiederholungsintervall_c_nr, Date t_erledigt, Date t_liefertermin, Date t_finaltermin, Date t_wunschtermin, Date t_belegdatum, Short b_poenale, Short b_rohs, BigDecimal n_gesamtauftragswertinauftragswaehrung, Double f_wechselkursmandantwaehrungzuauftragswaehrung, String x_internerkommentar, String x_externerkommentar, Short b_lieferterminunverbindlich, Date t_verrechenbar, String waehrung_c_nr_auftragswaehrung, FLRPersonal flrpersonalverrechenbar, FLRKunde flrkunde, FLRKunde flrkundelieferadresse, FLRPersonal flrvertreter, FLRKostenstelle flrkostenstelle, FLRSpediteur flrspediteur, FLRLager flrlager, com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag_rahmenauftrag, FLRPersonal flrpersonalauftragsfreigabe, Set flrauftragpositionen, Set flrbestellung) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.c_bestellnummer = c_bestellnummer;
        this.auftragart_c_nr = auftragart_c_nr;
        this.auftragstatus_c_nr = auftragstatus_c_nr;
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.t_auftragsfreigabe = t_auftragsfreigabe;
        this.t_lauftermin = t_lauftermin;
        this.t_lauftermin_bis = t_lauftermin_bis;
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
        this.t_erledigt = t_erledigt;
        this.t_liefertermin = t_liefertermin;
        this.t_finaltermin = t_finaltermin;
        this.t_wunschtermin = t_wunschtermin;
        this.t_belegdatum = t_belegdatum;
        this.b_poenale = b_poenale;
        this.b_rohs = b_rohs;
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
        this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
        this.x_internerkommentar = x_internerkommentar;
        this.x_externerkommentar = x_externerkommentar;
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
        this.t_verrechenbar = t_verrechenbar;
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
        this.flrkunde = flrkunde;
        this.flrkundelieferadresse = flrkundelieferadresse;
        this.flrvertreter = flrvertreter;
        this.flrkostenstelle = flrkostenstelle;
        this.flrspediteur = flrspediteur;
        this.flrlager = flrlager;
        this.flrverrechenbar = flrverrechenbar;
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
        this.flrpersonalauftragsfreigabe = flrpersonalauftragsfreigabe;
        this.flrauftragpositionen = flrauftragpositionen;
        this.flrbestellung = flrbestellung;
    }

    /** default constructor */
    public FLRAuftragOD() {
    }

    /** minimal constructor */
    public FLRAuftragOD(Set flrauftragpositionen, Set flrbestellung) {
        this.flrauftragpositionen = flrauftragpositionen;
        this.flrbestellung = flrbestellung;
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

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
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

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public Integer getZahlungsziel_i_id() {
        return this.zahlungsziel_i_id;
    }

    public void setZahlungsziel_i_id(Integer zahlungsziel_i_id) {
        this.zahlungsziel_i_id = zahlungsziel_i_id;
    }

    public Date getT_auftragsfreigabe() {
        return this.t_auftragsfreigabe;
    }

    public void setT_auftragsfreigabe(Date t_auftragsfreigabe) {
        this.t_auftragsfreigabe = t_auftragsfreigabe;
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

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_finaltermin() {
        return this.t_finaltermin;
    }

    public void setT_finaltermin(Date t_finaltermin) {
        this.t_finaltermin = t_finaltermin;
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

    public Short getB_lieferterminunverbindlich() {
        return this.b_lieferterminunverbindlich;
    }

    public void setB_lieferterminunverbindlich(Short b_lieferterminunverbindlich) {
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
    }

    public Date getT_verrechenbar() {
        return this.t_verrechenbar;
    }

    public void setT_verrechenbar(Date t_verrechenbar) {
        this.t_verrechenbar = t_verrechenbar;
    }

    public String getWaehrung_c_nr_auftragswaehrung() {
        return this.waehrung_c_nr_auftragswaehrung;
    }

    public void setWaehrung_c_nr_auftragswaehrung(String waehrung_c_nr_auftragswaehrung) {
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
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

    public FLRKunde getFlrkundelieferadresse() {
        return this.flrkundelieferadresse;
    }

    public void setFlrkundelieferadresse(FLRKunde flrkundelieferadresse) {
        this.flrkundelieferadresse = flrkundelieferadresse;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRSpediteur getFlrspediteur() {
        return this.flrspediteur;
    }

    public void setFlrspediteur(FLRSpediteur flrspediteur) {
        this.flrspediteur = flrspediteur;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar getFlrverrechenbar() {
        return this.flrverrechenbar;
    }

    public void setFlrverrechenbar(com.lp.server.auftrag.fastlanereader.generated.FLRVerrechenbar flrverrechenbar) {
        this.flrverrechenbar = flrverrechenbar;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD getFlrauftrag_rahmenauftrag() {
        return this.flrauftrag_rahmenauftrag;
    }

    public void setFlrauftrag_rahmenauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag_rahmenauftrag) {
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
    }

    public FLRPersonal getFlrpersonalauftragsfreigabe() {
        return this.flrpersonalauftragsfreigabe;
    }

    public void setFlrpersonalauftragsfreigabe(FLRPersonal flrpersonalauftragsfreigabe) {
        this.flrpersonalauftragsfreigabe = flrpersonalauftragsfreigabe;
    }

    public Set getFlrauftragpositionen() {
        return this.flrauftragpositionen;
    }

    public void setFlrauftragpositionen(Set flrauftragpositionen) {
        this.flrauftragpositionen = flrauftragpositionen;
    }

    public Set getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(Set flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
