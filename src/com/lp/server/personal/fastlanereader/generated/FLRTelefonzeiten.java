package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKontaktart;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTelefonzeiten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private String x_kommentarext;

    /** nullable persistent field */
    private String x_kommentarint;

    /** nullable persistent field */
    private Date t_von;

    /** nullable persistent field */
    private Date t_bis;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer angebot_i_id;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_wiedervorlage;

    /** nullable persistent field */
    private Date t_wiedervorlage_erledigt;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private Integer kontaktart_i_id;

    /** nullable persistent field */
    private Double f_dauer_uebersteuert;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRAnsprechpartner flransprechpartner;

    /** nullable persistent field */
    private FLRKontaktart flrkontaktart;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRAngebot flrangebot;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** full constructor */
    public FLRTelefonzeiten(Integer personal_i_id, Integer projekt_i_id, Integer partner_i_id, String x_kommentarext, String x_kommentarint, Date t_von, Date t_bis, Integer auftrag_i_id, Integer angebot_i_id, Double f_verrechenbar, Date t_erledigt, Date t_wiedervorlage, Date t_wiedervorlage_erledigt, String c_titel, Integer kontaktart_i_id, Double f_dauer_uebersteuert, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener, FLRPartner flrpartner, FLRAnsprechpartner flransprechpartner, FLRKontaktart flrkontaktart, FLRAuftrag flrauftrag, FLRAngebot flrangebot, FLRProjekt flrprojekt) {
        this.personal_i_id = personal_i_id;
        this.projekt_i_id = projekt_i_id;
        this.partner_i_id = partner_i_id;
        this.x_kommentarext = x_kommentarext;
        this.x_kommentarint = x_kommentarint;
        this.t_von = t_von;
        this.t_bis = t_bis;
        this.auftrag_i_id = auftrag_i_id;
        this.angebot_i_id = angebot_i_id;
        this.f_verrechenbar = f_verrechenbar;
        this.t_erledigt = t_erledigt;
        this.t_wiedervorlage = t_wiedervorlage;
        this.t_wiedervorlage_erledigt = t_wiedervorlage_erledigt;
        this.c_titel = c_titel;
        this.kontaktart_i_id = kontaktart_i_id;
        this.f_dauer_uebersteuert = f_dauer_uebersteuert;
        this.flrpersonal_erledigt = flrpersonal_erledigt;
        this.flrpersonal = flrpersonal;
        this.flrpersonal_zugewiesener = flrpersonal_zugewiesener;
        this.flrpartner = flrpartner;
        this.flransprechpartner = flransprechpartner;
        this.flrkontaktart = flrkontaktart;
        this.flrauftrag = flrauftrag;
        this.flrangebot = flrangebot;
        this.flrprojekt = flrprojekt;
    }

    /** default constructor */
    public FLRTelefonzeiten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public String getX_kommentarext() {
        return this.x_kommentarext;
    }

    public void setX_kommentarext(String x_kommentarext) {
        this.x_kommentarext = x_kommentarext;
    }

    public String getX_kommentarint() {
        return this.x_kommentarint;
    }

    public void setX_kommentarint(String x_kommentarint) {
        this.x_kommentarint = x_kommentarint;
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

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
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

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public Integer getKontaktart_i_id() {
        return this.kontaktart_i_id;
    }

    public void setKontaktart_i_id(Integer kontaktart_i_id) {
        this.kontaktart_i_id = kontaktart_i_id;
    }

    public Double getF_dauer_uebersteuert() {
        return this.f_dauer_uebersteuert;
    }

    public void setF_dauer_uebersteuert(Double f_dauer_uebersteuert) {
        this.f_dauer_uebersteuert = f_dauer_uebersteuert;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_erledigt() {
        return this.flrpersonal_erledigt;
    }

    public void setFlrpersonal_erledigt(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt) {
        this.flrpersonal_erledigt = flrpersonal_erledigt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_zugewiesener() {
        return this.flrpersonal_zugewiesener;
    }

    public void setFlrpersonal_zugewiesener(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_zugewiesener) {
        this.flrpersonal_zugewiesener = flrpersonal_zugewiesener;
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

    public FLRKontaktart getFlrkontaktart() {
        return this.flrkontaktart;
    }

    public void setFlrkontaktart(FLRKontaktart flrkontaktart) {
        this.flrkontaktart = flrkontaktart;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
