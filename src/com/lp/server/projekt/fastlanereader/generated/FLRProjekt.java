package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjekt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String kategorie_c_nr;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private String c_dateiname;

    /** nullable persistent field */
    private Integer personal_i_id_erzeuger;

    /** nullable persistent field */
    private Integer personal_i_id_zugewiesener;

    /** nullable persistent field */
    private Integer personal_i_id_internerledigt;

    /** nullable persistent field */
    private Integer personal_i_id_erlediger;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_betreiber;

    /** nullable persistent field */
    private Integer bereich_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Integer i_prio;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Date t_zielwunschdatum;

    /** nullable persistent field */
    private String x_freetext;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private Integer i_verrechenbar;

    /** nullable persistent field */
    private Short b_freigegeben;

    /** nullable persistent field */
    private Date t_internerledigt;

    /** nullable persistent field */
    private Date t_realisierung;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Double d_dauer;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Date t_erledigungsdatum;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String c_buildnumber;

    /** nullable persistent field */
    private String c_deploynumber;

    /** nullable persistent field */
    private BigDecimal n_umsatzgeplant;

    /** nullable persistent field */
    private Integer i_wahrscheinlichkeit;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRTitelaushistory flrtitelaushistory;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRBereich flrbereich;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjektstatus flrprojektstatus;

    /** nullable persistent field */
    private FLRPersonal flrpersonalZugewiesener;

    /** nullable persistent field */
    private FLRPersonal flrpersonalErzeuger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalErlediger;

    /** nullable persistent field */
    private FLRAnsprechpartner flransprechpartner;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRPartner flrpartnerbetreiber;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekterledigungsgrund flrprojekterledigungsgrund;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt flrvkfortschritt;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRTyp flrtyp;

    /** persistent field */
    private Set technikerset;

    /** persistent field */
    private Set historyset;

    /** full constructor */
    public FLRProjekt(String c_nr, String kategorie_c_nr, String c_titel, String c_dateiname, Integer personal_i_id_erzeuger, Integer personal_i_id_zugewiesener, Integer personal_i_id_internerledigt, Integer personal_i_id_erlediger, Integer partner_i_id, Integer partner_i_id_betreiber, Integer bereich_i_id, String typ_c_nr, Integer i_prio, String status_c_nr, String mandant_c_nr, Date t_zielwunschdatum, String x_freetext, Integer ansprechpartner_i_id, Integer i_verrechenbar, Short b_freigegeben, Date t_internerledigt, Date t_realisierung, Date t_anlegen, Date t_aendern, Double d_dauer, Date t_zeit, Date t_erledigungsdatum, Integer i_sort, String c_buildnumber, String c_deploynumber, BigDecimal n_umsatzgeplant, Integer i_wahrscheinlichkeit, Integer artikel_i_id, com.lp.server.projekt.fastlanereader.generated.FLRTitelaushistory flrtitelaushistory, FLRArtikel flrartikel, com.lp.server.projekt.fastlanereader.generated.FLRBereich flrbereich, com.lp.server.projekt.fastlanereader.generated.FLRProjektstatus flrprojektstatus, FLRPersonal flrpersonalZugewiesener, FLRPersonal flrpersonalErzeuger, FLRPersonal flrpersonalErlediger, FLRAnsprechpartner flransprechpartner, FLRPartner flrpartner, FLRPartner flrpartnerbetreiber, com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche, com.lp.server.projekt.fastlanereader.generated.FLRProjekterledigungsgrund flrprojekterledigungsgrund, com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt flrvkfortschritt, com.lp.server.projekt.fastlanereader.generated.FLRTyp flrtyp, Set technikerset, Set historyset) {
        this.c_nr = c_nr;
        this.kategorie_c_nr = kategorie_c_nr;
        this.c_titel = c_titel;
        this.c_dateiname = c_dateiname;
        this.personal_i_id_erzeuger = personal_i_id_erzeuger;
        this.personal_i_id_zugewiesener = personal_i_id_zugewiesener;
        this.personal_i_id_internerledigt = personal_i_id_internerledigt;
        this.personal_i_id_erlediger = personal_i_id_erlediger;
        this.partner_i_id = partner_i_id;
        this.partner_i_id_betreiber = partner_i_id_betreiber;
        this.bereich_i_id = bereich_i_id;
        this.typ_c_nr = typ_c_nr;
        this.i_prio = i_prio;
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.t_zielwunschdatum = t_zielwunschdatum;
        this.x_freetext = x_freetext;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.i_verrechenbar = i_verrechenbar;
        this.b_freigegeben = b_freigegeben;
        this.t_internerledigt = t_internerledigt;
        this.t_realisierung = t_realisierung;
        this.t_anlegen = t_anlegen;
        this.t_aendern = t_aendern;
        this.d_dauer = d_dauer;
        this.t_zeit = t_zeit;
        this.t_erledigungsdatum = t_erledigungsdatum;
        this.i_sort = i_sort;
        this.c_buildnumber = c_buildnumber;
        this.c_deploynumber = c_deploynumber;
        this.n_umsatzgeplant = n_umsatzgeplant;
        this.i_wahrscheinlichkeit = i_wahrscheinlichkeit;
        this.artikel_i_id = artikel_i_id;
        this.flrtitelaushistory = flrtitelaushistory;
        this.flrartikel = flrartikel;
        this.flrbereich = flrbereich;
        this.flrprojektstatus = flrprojektstatus;
        this.flrpersonalZugewiesener = flrpersonalZugewiesener;
        this.flrpersonalErzeuger = flrpersonalErzeuger;
        this.flrpersonalErlediger = flrpersonalErlediger;
        this.flransprechpartner = flransprechpartner;
        this.flrpartner = flrpartner;
        this.flrpartnerbetreiber = flrpartnerbetreiber;
        this.flrprojekttextsuche = flrprojekttextsuche;
        this.flrprojekterledigungsgrund = flrprojekterledigungsgrund;
        this.flrvkfortschritt = flrvkfortschritt;
        this.flrtyp = flrtyp;
        this.technikerset = technikerset;
        this.historyset = historyset;
    }

    /** default constructor */
    public FLRProjekt() {
    }

    /** minimal constructor */
    public FLRProjekt(Set technikerset, Set historyset) {
        this.technikerset = technikerset;
        this.historyset = historyset;
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

    public String getKategorie_c_nr() {
        return this.kategorie_c_nr;
    }

    public void setKategorie_c_nr(String kategorie_c_nr) {
        this.kategorie_c_nr = kategorie_c_nr;
    }

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public String getC_dateiname() {
        return this.c_dateiname;
    }

    public void setC_dateiname(String c_dateiname) {
        this.c_dateiname = c_dateiname;
    }

    public Integer getPersonal_i_id_erzeuger() {
        return this.personal_i_id_erzeuger;
    }

    public void setPersonal_i_id_erzeuger(Integer personal_i_id_erzeuger) {
        this.personal_i_id_erzeuger = personal_i_id_erzeuger;
    }

    public Integer getPersonal_i_id_zugewiesener() {
        return this.personal_i_id_zugewiesener;
    }

    public void setPersonal_i_id_zugewiesener(Integer personal_i_id_zugewiesener) {
        this.personal_i_id_zugewiesener = personal_i_id_zugewiesener;
    }

    public Integer getPersonal_i_id_internerledigt() {
        return this.personal_i_id_internerledigt;
    }

    public void setPersonal_i_id_internerledigt(Integer personal_i_id_internerledigt) {
        this.personal_i_id_internerledigt = personal_i_id_internerledigt;
    }

    public Integer getPersonal_i_id_erlediger() {
        return this.personal_i_id_erlediger;
    }

    public void setPersonal_i_id_erlediger(Integer personal_i_id_erlediger) {
        this.personal_i_id_erlediger = personal_i_id_erlediger;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public Integer getPartner_i_id_betreiber() {
        return this.partner_i_id_betreiber;
    }

    public void setPartner_i_id_betreiber(Integer partner_i_id_betreiber) {
        this.partner_i_id_betreiber = partner_i_id_betreiber;
    }

    public Integer getBereich_i_id() {
        return this.bereich_i_id;
    }

    public void setBereich_i_id(Integer bereich_i_id) {
        this.bereich_i_id = bereich_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Integer getI_prio() {
        return this.i_prio;
    }

    public void setI_prio(Integer i_prio) {
        this.i_prio = i_prio;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Date getT_zielwunschdatum() {
        return this.t_zielwunschdatum;
    }

    public void setT_zielwunschdatum(Date t_zielwunschdatum) {
        this.t_zielwunschdatum = t_zielwunschdatum;
    }

    public String getX_freetext() {
        return this.x_freetext;
    }

    public void setX_freetext(String x_freetext) {
        this.x_freetext = x_freetext;
    }

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public Integer getI_verrechenbar() {
        return this.i_verrechenbar;
    }

    public void setI_verrechenbar(Integer i_verrechenbar) {
        this.i_verrechenbar = i_verrechenbar;
    }

    public Short getB_freigegeben() {
        return this.b_freigegeben;
    }

    public void setB_freigegeben(Short b_freigegeben) {
        this.b_freigegeben = b_freigegeben;
    }

    public Date getT_internerledigt() {
        return this.t_internerledigt;
    }

    public void setT_internerledigt(Date t_internerledigt) {
        this.t_internerledigt = t_internerledigt;
    }

    public Date getT_realisierung() {
        return this.t_realisierung;
    }

    public void setT_realisierung(Date t_realisierung) {
        this.t_realisierung = t_realisierung;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Double getD_dauer() {
        return this.d_dauer;
    }

    public void setD_dauer(Double d_dauer) {
        this.d_dauer = d_dauer;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Date getT_erledigungsdatum() {
        return this.t_erledigungsdatum;
    }

    public void setT_erledigungsdatum(Date t_erledigungsdatum) {
        this.t_erledigungsdatum = t_erledigungsdatum;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getC_buildnumber() {
        return this.c_buildnumber;
    }

    public void setC_buildnumber(String c_buildnumber) {
        this.c_buildnumber = c_buildnumber;
    }

    public String getC_deploynumber() {
        return this.c_deploynumber;
    }

    public void setC_deploynumber(String c_deploynumber) {
        this.c_deploynumber = c_deploynumber;
    }

    public BigDecimal getN_umsatzgeplant() {
        return this.n_umsatzgeplant;
    }

    public void setN_umsatzgeplant(BigDecimal n_umsatzgeplant) {
        this.n_umsatzgeplant = n_umsatzgeplant;
    }

    public Integer getI_wahrscheinlichkeit() {
        return this.i_wahrscheinlichkeit;
    }

    public void setI_wahrscheinlichkeit(Integer i_wahrscheinlichkeit) {
        this.i_wahrscheinlichkeit = i_wahrscheinlichkeit;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRTitelaushistory getFlrtitelaushistory() {
        return this.flrtitelaushistory;
    }

    public void setFlrtitelaushistory(com.lp.server.projekt.fastlanereader.generated.FLRTitelaushistory flrtitelaushistory) {
        this.flrtitelaushistory = flrtitelaushistory;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRBereich getFlrbereich() {
        return this.flrbereich;
    }

    public void setFlrbereich(com.lp.server.projekt.fastlanereader.generated.FLRBereich flrbereich) {
        this.flrbereich = flrbereich;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjektstatus getFlrprojektstatus() {
        return this.flrprojektstatus;
    }

    public void setFlrprojektstatus(com.lp.server.projekt.fastlanereader.generated.FLRProjektstatus flrprojektstatus) {
        this.flrprojektstatus = flrprojektstatus;
    }

    public FLRPersonal getFlrpersonalZugewiesener() {
        return this.flrpersonalZugewiesener;
    }

    public void setFlrpersonalZugewiesener(FLRPersonal flrpersonalZugewiesener) {
        this.flrpersonalZugewiesener = flrpersonalZugewiesener;
    }

    public FLRPersonal getFlrpersonalErzeuger() {
        return this.flrpersonalErzeuger;
    }

    public void setFlrpersonalErzeuger(FLRPersonal flrpersonalErzeuger) {
        this.flrpersonalErzeuger = flrpersonalErzeuger;
    }

    public FLRPersonal getFlrpersonalErlediger() {
        return this.flrpersonalErlediger;
    }

    public void setFlrpersonalErlediger(FLRPersonal flrpersonalErlediger) {
        this.flrpersonalErlediger = flrpersonalErlediger;
    }

    public FLRAnsprechpartner getFlransprechpartner() {
        return this.flransprechpartner;
    }

    public void setFlransprechpartner(FLRAnsprechpartner flransprechpartner) {
        this.flransprechpartner = flransprechpartner;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRPartner getFlrpartnerbetreiber() {
        return this.flrpartnerbetreiber;
    }

    public void setFlrpartnerbetreiber(FLRPartner flrpartnerbetreiber) {
        this.flrpartnerbetreiber = flrpartnerbetreiber;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche getFlrprojekttextsuche() {
        return this.flrprojekttextsuche;
    }

    public void setFlrprojekttextsuche(com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche) {
        this.flrprojekttextsuche = flrprojekttextsuche;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekterledigungsgrund getFlrprojekterledigungsgrund() {
        return this.flrprojekterledigungsgrund;
    }

    public void setFlrprojekterledigungsgrund(com.lp.server.projekt.fastlanereader.generated.FLRProjekterledigungsgrund flrprojekterledigungsgrund) {
        this.flrprojekterledigungsgrund = flrprojekterledigungsgrund;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt getFlrvkfortschritt() {
        return this.flrvkfortschritt;
    }

    public void setFlrvkfortschritt(com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt flrvkfortschritt) {
        this.flrvkfortschritt = flrvkfortschritt;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRTyp getFlrtyp() {
        return this.flrtyp;
    }

    public void setFlrtyp(com.lp.server.projekt.fastlanereader.generated.FLRTyp flrtyp) {
        this.flrtyp = flrtyp;
    }

    public Set getTechnikerset() {
        return this.technikerset;
    }

    public void setTechnikerset(Set technikerset) {
        this.technikerset = technikerset;
    }

    public Set getHistoryset() {
        return this.historyset;
    }

    public void setHistoryset(Set historyset) {
        this.historyset = historyset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
