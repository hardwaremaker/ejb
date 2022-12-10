package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjektQueue implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String kategorie_c_nr;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private Integer personal_i_id_erzeuger;

    /** nullable persistent field */
    private Integer personal_i_id_zugewiesener;

    /** nullable persistent field */
    private Integer personal_i_id_erlediger;

    /** nullable persistent field */
    private Integer partner_i_id;

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
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche;

    /** persistent field */
    private Set technikerset;

    /** full constructor */
    public FLRProjektQueue(String c_nr, String kategorie_c_nr, String c_titel, Integer personal_i_id_erzeuger, Integer personal_i_id_zugewiesener, Integer personal_i_id_erlediger, Integer partner_i_id, String typ_c_nr, Integer i_prio, String status_c_nr, String mandant_c_nr, Date t_zielwunschdatum, String x_freetext, Integer ansprechpartner_i_id, Short b_verrechenbar, Date t_anlegen, Date t_aendern, Double d_dauer, Date t_zeit, Date t_erledigungsdatum, Integer i_sort, FLRPersonal flrpersonalZugewiesener, FLRPersonal flrpersonalErzeuger, FLRPersonal flrpersonalErlediger, FLRAnsprechpartner flransprechpartner, FLRPartner flrpartner, com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche, Set technikerset) {
        this.c_nr = c_nr;
        this.kategorie_c_nr = kategorie_c_nr;
        this.c_titel = c_titel;
        this.personal_i_id_erzeuger = personal_i_id_erzeuger;
        this.personal_i_id_zugewiesener = personal_i_id_zugewiesener;
        this.personal_i_id_erlediger = personal_i_id_erlediger;
        this.partner_i_id = partner_i_id;
        this.typ_c_nr = typ_c_nr;
        this.i_prio = i_prio;
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.t_zielwunschdatum = t_zielwunschdatum;
        this.x_freetext = x_freetext;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.i_verrechenbar = i_verrechenbar;
        this.t_anlegen = t_anlegen;
        this.t_aendern = t_aendern;
        this.d_dauer = d_dauer;
        this.t_zeit = t_zeit;
        this.t_erledigungsdatum = t_erledigungsdatum;
        this.i_sort = i_sort;
        this.flrpersonalZugewiesener = flrpersonalZugewiesener;
        this.flrpersonalErzeuger = flrpersonalErzeuger;
        this.flrpersonalErlediger = flrpersonalErlediger;
        this.flransprechpartner = flransprechpartner;
        this.flrpartner = flrpartner;
        this.flrprojekttextsuche = flrprojekttextsuche;
        this.technikerset = technikerset;
    }

    /** default constructor */
    public FLRProjektQueue() {
    }

    /** minimal constructor */
    public FLRProjektQueue(Set technikerset) {
        this.technikerset = technikerset;
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

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche getFlrprojekttextsuche() {
        return this.flrprojekttextsuche;
    }

    public void setFlrprojekttextsuche(com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche flrprojekttextsuche) {
        this.flrprojekttextsuche = flrprojekttextsuche;
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
