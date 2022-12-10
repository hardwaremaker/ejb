package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnsprechpartner implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_ansprechpartner;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer ansprechpartnerfunktion_i_id;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_durchwahl;

    /** nullable persistent field */
    private String x_bemerkung;

    /** nullable persistent field */
    private String c_abteilung;

    /** nullable persistent field */
    private String c_email;

    /** nullable persistent field */
    private String c_fax;

    /** nullable persistent field */
    private String c_telefon;

    /** nullable persistent field */
    private String c_handy;

    /** nullable persistent field */
    private String c_direktfax;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private String c_exchangeid;

    /** nullable persistent field */
    private Date t_zuletzt_exportiert;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRNewslettergrund flrnewslettergrund;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion;

    /** full constructor */
    public FLRAnsprechpartner(Integer partner_i_id, Integer partner_i_id_ansprechpartner, Integer i_sort, Integer ansprechpartnerfunktion_i_id, Short b_versteckt, Short b_durchwahl, String x_bemerkung, String c_abteilung, String c_email, String c_fax, String c_telefon, String c_handy, String c_direktfax, Date t_aendern, String c_exchangeid, Date t_zuletzt_exportiert, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner, com.lp.server.partner.fastlanereader.generated.FLRNewslettergrund flrnewslettergrund, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion) {
        this.partner_i_id = partner_i_id;
        this.partner_i_id_ansprechpartner = partner_i_id_ansprechpartner;
        this.i_sort = i_sort;
        this.ansprechpartnerfunktion_i_id = ansprechpartnerfunktion_i_id;
        this.b_versteckt = b_versteckt;
        this.b_durchwahl = b_durchwahl;
        this.x_bemerkung = x_bemerkung;
        this.c_abteilung = c_abteilung;
        this.c_email = c_email;
        this.c_fax = c_fax;
        this.c_telefon = c_telefon;
        this.c_handy = c_handy;
        this.c_direktfax = c_direktfax;
        this.t_aendern = t_aendern;
        this.c_exchangeid = c_exchangeid;
        this.t_zuletzt_exportiert = t_zuletzt_exportiert;
        this.flrpartneransprechpartner = flrpartneransprechpartner;
        this.flrnewslettergrund = flrnewslettergrund;
        this.flrpartner = flrpartner;
        this.flransprechpartnerfunktion = flransprechpartnerfunktion;
    }

    /** default constructor */
    public FLRAnsprechpartner() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public Integer getPartner_i_id_ansprechpartner() {
        return this.partner_i_id_ansprechpartner;
    }

    public void setPartner_i_id_ansprechpartner(Integer partner_i_id_ansprechpartner) {
        this.partner_i_id_ansprechpartner = partner_i_id_ansprechpartner;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getAnsprechpartnerfunktion_i_id() {
        return this.ansprechpartnerfunktion_i_id;
    }

    public void setAnsprechpartnerfunktion_i_id(Integer ansprechpartnerfunktion_i_id) {
        this.ansprechpartnerfunktion_i_id = ansprechpartnerfunktion_i_id;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_durchwahl() {
        return this.b_durchwahl;
    }

    public void setB_durchwahl(Short b_durchwahl) {
        this.b_durchwahl = b_durchwahl;
    }

    public String getX_bemerkung() {
        return this.x_bemerkung;
    }

    public void setX_bemerkung(String x_bemerkung) {
        this.x_bemerkung = x_bemerkung;
    }

    public String getC_abteilung() {
        return this.c_abteilung;
    }

    public void setC_abteilung(String c_abteilung) {
        this.c_abteilung = c_abteilung;
    }

    public String getC_email() {
        return this.c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_fax() {
        return this.c_fax;
    }

    public void setC_fax(String c_fax) {
        this.c_fax = c_fax;
    }

    public String getC_telefon() {
        return this.c_telefon;
    }

    public void setC_telefon(String c_telefon) {
        this.c_telefon = c_telefon;
    }

    public String getC_handy() {
        return this.c_handy;
    }

    public void setC_handy(String c_handy) {
        this.c_handy = c_handy;
    }

    public String getC_direktfax() {
        return this.c_direktfax;
    }

    public void setC_direktfax(String c_direktfax) {
        this.c_direktfax = c_direktfax;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public String getC_exchangeid() {
        return this.c_exchangeid;
    }

    public void setC_exchangeid(String c_exchangeid) {
        this.c_exchangeid = c_exchangeid;
    }

    public Date getT_zuletzt_exportiert() {
        return this.t_zuletzt_exportiert;
    }

    public void setT_zuletzt_exportiert(Date t_zuletzt_exportiert) {
        this.t_zuletzt_exportiert = t_zuletzt_exportiert;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartneransprechpartner() {
        return this.flrpartneransprechpartner;
    }

    public void setFlrpartneransprechpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner) {
        this.flrpartneransprechpartner = flrpartneransprechpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRNewslettergrund getFlrnewslettergrund() {
        return this.flrnewslettergrund;
    }

    public void setFlrnewslettergrund(com.lp.server.partner.fastlanereader.generated.FLRNewslettergrund flrnewslettergrund) {
        this.flrnewslettergrund = flrnewslettergrund;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion getFlransprechpartnerfunktion() {
        return this.flransprechpartnerfunktion;
    }

    public void setFlransprechpartnerfunktion(com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion) {
        this.flransprechpartnerfunktion = flransprechpartnerfunktion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
