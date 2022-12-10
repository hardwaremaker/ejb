package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTelefontodo implements Serializable {

    /** identifier field */
    private String c_nr;

    /** nullable persistent field */
    private Date t_von;

    /** nullable persistent field */
    private Date t_bis;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer angebot_i_id;

    /** nullable persistent field */
    private Integer telefonzeiten_i_id;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private Integer personal_i_id_zugewiesener;

    /** nullable persistent field */
    private Date t_wiedervorlage;

    /** nullable persistent field */
    private Date t_wiedervorlage_erledigt;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRAnsprechpartner flransprechpartner;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRAngebot flrangebot;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener;

    /** full constructor */
    public FLRTelefontodo(Date t_von, Date t_bis, Integer partner_i_id, Integer ansprechpartner_i_id, String mandant_c_nr, Integer projekt_i_id, Integer auftrag_i_id, Integer angebot_i_id, Integer telefonzeiten_i_id, String c_titel, Integer personal_i_id_zugewiesener, Date t_wiedervorlage, Date t_wiedervorlage_erledigt, FLRPartner flrpartner, FLRAnsprechpartner flransprechpartner, FLRAuftrag flrauftrag, FLRAngebot flrangebot, FLRProjekt flrprojekt, com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener) {
        this.t_von = t_von;
        this.t_bis = t_bis;
        this.partner_i_id = partner_i_id;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.projekt_i_id = projekt_i_id;
        this.auftrag_i_id = auftrag_i_id;
        this.angebot_i_id = angebot_i_id;
        this.telefonzeiten_i_id = telefonzeiten_i_id;
        this.c_titel = c_titel;
        this.personal_i_id_zugewiesener = personal_i_id_zugewiesener;
        this.t_wiedervorlage = t_wiedervorlage;
        this.t_wiedervorlage_erledigt = t_wiedervorlage_erledigt;
        this.flrpartner = flrpartner;
        this.flransprechpartner = flransprechpartner;
        this.flrauftrag = flrauftrag;
        this.flrangebot = flrangebot;
        this.flrprojekt = flrprojekt;
        this.flrtelefonzeiten = flrtelefonzeiten;
        this.flrpersonal_zugewiesener = flrpersonal_zugewiesener;
    }

    /** default constructor */
    public FLRTelefontodo() {
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Date getT_von() {
        return this.t_von;
    }

    public void setT_von(Date t_von) {
        this.t_von = t_von;
    }

    public Date getT_bis() {
        return this.t_bis;
    }

    public void setT_bis(Date t_bis) {
        this.t_bis = t_bis;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public Integer getAngebot_i_id() {
        return this.angebot_i_id;
    }

    public void setAngebot_i_id(Integer angebot_i_id) {
        this.angebot_i_id = angebot_i_id;
    }

    public Integer getTelefonzeiten_i_id() {
        return this.telefonzeiten_i_id;
    }

    public void setTelefonzeiten_i_id(Integer telefonzeiten_i_id) {
        this.telefonzeiten_i_id = telefonzeiten_i_id;
    }

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public Integer getPersonal_i_id_zugewiesener() {
        return this.personal_i_id_zugewiesener;
    }

    public void setPersonal_i_id_zugewiesener(Integer personal_i_id_zugewiesener) {
        this.personal_i_id_zugewiesener = personal_i_id_zugewiesener;
    }

    public Date getT_wiedervorlage() {
        return this.t_wiedervorlage;
    }

    public void setT_wiedervorlage(Date t_wiedervorlage) {
        this.t_wiedervorlage = t_wiedervorlage;
    }

    public Date getT_wiedervorlage_erledigt() {
        return this.t_wiedervorlage_erledigt;
    }

    public void setT_wiedervorlage_erledigt(Date t_wiedervorlage_erledigt) {
        this.t_wiedervorlage_erledigt = t_wiedervorlage_erledigt;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRAnsprechpartner getFlransprechpartner() {
        return this.flransprechpartner;
    }

    public void setFlransprechpartner(FLRAnsprechpartner flransprechpartner) {
        this.flransprechpartner = flransprechpartner;
    }

    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRAngebot getFlrangebot() {
        return this.flrangebot;
    }

    public void setFlrangebot(FLRAngebot flrangebot) {
        this.flrangebot = flrangebot;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten getFlrtelefonzeiten() {
        return this.flrtelefonzeiten;
    }

    public void setFlrtelefonzeiten(com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten flrtelefonzeiten) {
        this.flrtelefonzeiten = flrtelefonzeiten;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_zugewiesener() {
        return this.flrpersonal_zugewiesener;
    }

    public void setFlrpersonal_zugewiesener(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener) {
        this.flrpersonal_zugewiesener = flrpersonal_zugewiesener;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .toString();
    }

}
