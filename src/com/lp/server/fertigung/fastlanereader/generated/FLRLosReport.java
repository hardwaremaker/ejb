package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastposition;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLosReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private Date t_nachtraeglich_geoeffnet;

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
    private Date t_aktualisierungstueckliste;

    /** nullable persistent field */
    private Date t_aktualisierungarbeitszeit;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_produktionsstop;

    /** nullable persistent field */
    private BigDecimal n_losgroesse;

    /** nullable persistent field */
    private Double f_bewertung;

    /** nullable persistent field */
    private Integer wiederholendelose_i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer fertigungsgruppe_i_id;

    /** nullable persistent field */
    private Integer forecastposition_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_fertigungsort;

    /** nullable persistent field */
    private Date t_material_vollstaendig;

    /** nullable persistent field */
    private FLRForecastposition flrforecastposition;

    /** nullable persistent field */
    private FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** nullable persistent field */
    private FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrfertigungsgruppe;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrwiederholendelose;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** persistent field */
    private Set ablieferungset;

    /** full constructor */
    public FLRLosReport(String mandant_c_nr, String c_nr, String c_kommentar, String status_c_nr, String c_projekt, Integer stueckliste_i_id, Date t_nachtraeglich_geoeffnet, Date t_produktionsende, Date t_produktionsbeginn, Date t_ausgabe, Date t_erledigt, Date t_manuellerledigt, Date t_aktualisierungstueckliste, Date t_aktualisierungarbeitszeit, Date t_anlegen, Date t_produktionsstop, BigDecimal n_losgroesse, Double f_bewertung, Integer wiederholendelose_i_id, Integer kostenstelle_i_id, Integer fertigungsgruppe_i_id, Integer forecastposition_i_id, Integer partner_i_id_fertigungsort, Date t_material_vollstaendig, FLRForecastposition flrforecastposition, FLRStueckliste flrstueckliste, FLRKostenstelle flrkostenstelle, FLRAuftragposition flrauftragposition, FLRAuftragReport flrauftrag, FLRKunde flrkunde, FLRFertigungsgruppe flrfertigungsgruppe, FLRFertigungsgruppe flrwiederholendelose, FLRProjekt flrprojekt, Set ablieferungset) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.c_kommentar = c_kommentar;
        this.status_c_nr = status_c_nr;
        this.c_projekt = c_projekt;
        this.stueckliste_i_id = stueckliste_i_id;
        this.t_nachtraeglich_geoeffnet = t_nachtraeglich_geoeffnet;
        this.t_produktionsende = t_produktionsende;
        this.t_produktionsbeginn = t_produktionsbeginn;
        this.t_ausgabe = t_ausgabe;
        this.t_erledigt = t_erledigt;
        this.t_manuellerledigt = t_manuellerledigt;
        this.t_aktualisierungstueckliste = t_aktualisierungstueckliste;
        this.t_aktualisierungarbeitszeit = t_aktualisierungarbeitszeit;
        this.t_anlegen = t_anlegen;
        this.t_produktionsstop = t_produktionsstop;
        this.n_losgroesse = n_losgroesse;
        this.f_bewertung = f_bewertung;
        this.wiederholendelose_i_id = wiederholendelose_i_id;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
        this.forecastposition_i_id = forecastposition_i_id;
        this.partner_i_id_fertigungsort = partner_i_id_fertigungsort;
        this.t_material_vollstaendig = t_material_vollstaendig;
        this.flrforecastposition = flrforecastposition;
        this.flrstueckliste = flrstueckliste;
        this.flrkostenstelle = flrkostenstelle;
        this.flrauftragposition = flrauftragposition;
        this.flrauftrag = flrauftrag;
        this.flrkunde = flrkunde;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
        this.flrwiederholendelose = flrwiederholendelose;
        this.flrprojekt = flrprojekt;
        this.ablieferungset = ablieferungset;
    }

    /** default constructor */
    public FLRLosReport() {
    }

    /** minimal constructor */
    public FLRLosReport(Set ablieferungset) {
        this.ablieferungset = ablieferungset;
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

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
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

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public Date getT_nachtraeglich_geoeffnet() {
        return this.t_nachtraeglich_geoeffnet;
    }

    public void setT_nachtraeglich_geoeffnet(Date t_nachtraeglich_geoeffnet) {
        this.t_nachtraeglich_geoeffnet = t_nachtraeglich_geoeffnet;
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

    public Date getT_aktualisierungstueckliste() {
        return this.t_aktualisierungstueckliste;
    }

    public void setT_aktualisierungstueckliste(Date t_aktualisierungstueckliste) {
        this.t_aktualisierungstueckliste = t_aktualisierungstueckliste;
    }

    public Date getT_aktualisierungarbeitszeit() {
        return this.t_aktualisierungarbeitszeit;
    }

    public void setT_aktualisierungarbeitszeit(Date t_aktualisierungarbeitszeit) {
        this.t_aktualisierungarbeitszeit = t_aktualisierungarbeitszeit;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
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

    public Double getF_bewertung() {
        return this.f_bewertung;
    }

    public void setF_bewertung(Double f_bewertung) {
        this.f_bewertung = f_bewertung;
    }

    public Integer getWiederholendelose_i_id() {
        return this.wiederholendelose_i_id;
    }

    public void setWiederholendelose_i_id(Integer wiederholendelose_i_id) {
        this.wiederholendelose_i_id = wiederholendelose_i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getFertigungsgruppe_i_id() {
        return this.fertigungsgruppe_i_id;
    }

    public void setFertigungsgruppe_i_id(Integer fertigungsgruppe_i_id) {
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
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

    public Date getT_material_vollstaendig() {
        return this.t_material_vollstaendig;
    }

    public void setT_material_vollstaendig(Date t_material_vollstaendig) {
        this.t_material_vollstaendig = t_material_vollstaendig;
    }

    public FLRForecastposition getFlrforecastposition() {
        return this.flrforecastposition;
    }

    public void setFlrforecastposition(FLRForecastposition flrforecastposition) {
        this.flrforecastposition = flrforecastposition;
    }

    public FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public FLRFertigungsgruppe getFlrwiederholendelose() {
        return this.flrwiederholendelose;
    }

    public void setFlrwiederholendelose(FLRFertigungsgruppe flrwiederholendelose) {
        this.flrwiederholendelose = flrwiederholendelose;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public Set getAblieferungset() {
        return this.ablieferungset;
    }

    public void setAblieferungset(Set ablieferungset) {
        this.ablieferungset = ablieferungset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
