package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerplatz;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastpositionProduktion;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLos implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private String c_abposnr;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private Integer lager_i_id_ziel;

    /** nullable persistent field */
    private Integer personal_i_id_techniker;

    /** nullable persistent field */
    private Integer fertigungsgruppe_i_id;

    /** nullable persistent field */
    private Date t_produktionsende;

    /** nullable persistent field */
    private Date t_produktionsbeginn;

    /** nullable persistent field */
    private Date t_ausgabe;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private Date t_produktionsstop;

    /** nullable persistent field */
    private BigDecimal n_losgroesse;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Double f_bewertung;

    /** nullable persistent field */
    private String c_schachtelplan;

    /** nullable persistent field */
    private Date t_abliefertermin;

    /** nullable persistent field */
    private Integer forecastposition_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_fertigungsort;

    /** nullable persistent field */
    private Integer lagerplatz_i_id;

    /** nullable persistent field */
    private FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrfertigungsgruppe;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRForecastpositionProduktion flrforecastposition;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** nullable persistent field */
    private FLRLagerplatz flrlagerplatz;

    /** persistent field */
    private Set ablieferungset;

    /** persistent field */
    private Set lagerentnahmeset;

    /** persistent field */
    private Set verfuegbarkei;

    /** persistent field */
    private Set technikerset;

    /** full constructor */
    public FLRLos(String mandant_c_nr, String c_nr, String status_c_nr, String c_projekt, String c_abposnr, String c_kommentar, String x_text, Integer stueckliste_i_id, Integer lager_i_id_ziel, Integer personal_i_id_techniker, Integer fertigungsgruppe_i_id, Date t_produktionsende, Date t_produktionsbeginn, Date t_ausgabe, Date t_erledigt, Date t_manuellerledigt, Date t_produktionsstop, BigDecimal n_losgroesse, Integer kostenstelle_i_id, Double f_bewertung, String c_schachtelplan, Date t_abliefertermin, Integer forecastposition_i_id, Integer partner_i_id_fertigungsort, Integer lagerplatz_i_id, FLRStueckliste flrstueckliste, FLRAuftragReport flrauftrag, FLRProjekt flrprojekt, FLRLager flrlager, FLRFertigungsgruppe flrfertigungsgruppe, FLRKunde flrkunde, FLRForecastpositionProduktion flrforecastposition, FLRAuftragposition flrauftragposition, FLRLagerplatz flrlagerplatz, Set ablieferungset, Set lagerentnahmeset, Set verfuegbarkei, Set technikerset) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.status_c_nr = status_c_nr;
        this.c_projekt = c_projekt;
        this.c_abposnr = c_abposnr;
        this.c_kommentar = c_kommentar;
        this.x_text = x_text;
        this.stueckliste_i_id = stueckliste_i_id;
        this.lager_i_id_ziel = lager_i_id_ziel;
        this.personal_i_id_techniker = personal_i_id_techniker;
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
        this.t_produktionsende = t_produktionsende;
        this.t_produktionsbeginn = t_produktionsbeginn;
        this.t_ausgabe = t_ausgabe;
        this.t_erledigt = t_erledigt;
        this.t_manuellerledigt = t_manuellerledigt;
        this.t_produktionsstop = t_produktionsstop;
        this.n_losgroesse = n_losgroesse;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.f_bewertung = f_bewertung;
        this.c_schachtelplan = c_schachtelplan;
        this.t_abliefertermin = t_abliefertermin;
        this.forecastposition_i_id = forecastposition_i_id;
        this.partner_i_id_fertigungsort = partner_i_id_fertigungsort;
        this.lagerplatz_i_id = lagerplatz_i_id;
        this.flrstueckliste = flrstueckliste;
        this.flrauftrag = flrauftrag;
        this.flrprojekt = flrprojekt;
        this.flrlager = flrlager;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
        this.flrkunde = flrkunde;
        this.flrforecastposition = flrforecastposition;
        this.flrauftragposition = flrauftragposition;
        this.flrlagerplatz = flrlagerplatz;
        this.ablieferungset = ablieferungset;
        this.lagerentnahmeset = lagerentnahmeset;
        this.verfuegbarkei = verfuegbarkei;
        this.technikerset = technikerset;
    }

    /** default constructor */
    public FLRLos() {
    }

    /** minimal constructor */
    public FLRLos(Set ablieferungset, Set lagerentnahmeset, Set verfuegbarkei, Set technikerset) {
        this.ablieferungset = ablieferungset;
        this.lagerentnahmeset = lagerentnahmeset;
        this.verfuegbarkei = verfuegbarkei;
        this.technikerset = technikerset;
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

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getC_projekt() {
        return this.c_projekt;
    }

    public void setC_projekt(String c_projekt) {
        this.c_projekt = c_projekt;
    }

    public String getC_abposnr() {
        return this.c_abposnr;
    }

    public void setC_abposnr(String c_abposnr) {
        this.c_abposnr = c_abposnr;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public Integer getLager_i_id_ziel() {
        return this.lager_i_id_ziel;
    }

    public void setLager_i_id_ziel(Integer lager_i_id_ziel) {
        this.lager_i_id_ziel = lager_i_id_ziel;
    }

    public Integer getPersonal_i_id_techniker() {
        return this.personal_i_id_techniker;
    }

    public void setPersonal_i_id_techniker(Integer personal_i_id_techniker) {
        this.personal_i_id_techniker = personal_i_id_techniker;
    }

    public Integer getFertigungsgruppe_i_id() {
        return this.fertigungsgruppe_i_id;
    }

    public void setFertigungsgruppe_i_id(Integer fertigungsgruppe_i_id) {
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
    }

    public Date getT_produktionsende() {
        return this.t_produktionsende;
    }

    public void setT_produktionsende(Date t_produktionsende) {
        this.t_produktionsende = t_produktionsende;
    }

    public Date getT_produktionsbeginn() {
        return this.t_produktionsbeginn;
    }

    public void setT_produktionsbeginn(Date t_produktionsbeginn) {
        this.t_produktionsbeginn = t_produktionsbeginn;
    }

    public Date getT_ausgabe() {
        return this.t_ausgabe;
    }

    public void setT_ausgabe(Date t_ausgabe) {
        this.t_ausgabe = t_ausgabe;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
    }

    public Date getT_produktionsstop() {
        return this.t_produktionsstop;
    }

    public void setT_produktionsstop(Date t_produktionsstop) {
        this.t_produktionsstop = t_produktionsstop;
    }

    public BigDecimal getN_losgroesse() {
        return this.n_losgroesse;
    }

    public void setN_losgroesse(BigDecimal n_losgroesse) {
        this.n_losgroesse = n_losgroesse;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Double getF_bewertung() {
        return this.f_bewertung;
    }

    public void setF_bewertung(Double f_bewertung) {
        this.f_bewertung = f_bewertung;
    }

    public String getC_schachtelplan() {
        return this.c_schachtelplan;
    }

    public void setC_schachtelplan(String c_schachtelplan) {
        this.c_schachtelplan = c_schachtelplan;
    }

    public Date getT_abliefertermin() {
        return this.t_abliefertermin;
    }

    public void setT_abliefertermin(Date t_abliefertermin) {
        this.t_abliefertermin = t_abliefertermin;
    }

    public Integer getForecastposition_i_id() {
        return this.forecastposition_i_id;
    }

    public void setForecastposition_i_id(Integer forecastposition_i_id) {
        this.forecastposition_i_id = forecastposition_i_id;
    }

    public Integer getPartner_i_id_fertigungsort() {
        return this.partner_i_id_fertigungsort;
    }

    public void setPartner_i_id_fertigungsort(Integer partner_i_id_fertigungsort) {
        this.partner_i_id_fertigungsort = partner_i_id_fertigungsort;
    }

    public Integer getLagerplatz_i_id() {
        return this.lagerplatz_i_id;
    }

    public void setLagerplatz_i_id(Integer lagerplatz_i_id) {
        this.lagerplatz_i_id = lagerplatz_i_id;
    }

    public FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRForecastpositionProduktion getFlrforecastposition() {
        return this.flrforecastposition;
    }

    public void setFlrforecastposition(FLRForecastpositionProduktion flrforecastposition) {
        this.flrforecastposition = flrforecastposition;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public FLRLagerplatz getFlrlagerplatz() {
        return this.flrlagerplatz;
    }

    public void setFlrlagerplatz(FLRLagerplatz flrlagerplatz) {
        this.flrlagerplatz = flrlagerplatz;
    }

    public Set getAblieferungset() {
        return this.ablieferungset;
    }

    public void setAblieferungset(Set ablieferungset) {
        this.ablieferungset = ablieferungset;
    }

    public Set getLagerentnahmeset() {
        return this.lagerentnahmeset;
    }

    public void setLagerentnahmeset(Set lagerentnahmeset) {
        this.lagerentnahmeset = lagerentnahmeset;
    }

    public Set getVerfuegbarkei() {
        return this.verfuegbarkei;
    }

    public void setVerfuegbarkei(Set verfuegbarkei) {
        this.verfuegbarkei = verfuegbarkei;
    }

    public Set getTechnikerset() {
        return this.technikerset;
    }

    public void setTechnikerset(Set technikerset) {
        this.technikerset = technikerset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
