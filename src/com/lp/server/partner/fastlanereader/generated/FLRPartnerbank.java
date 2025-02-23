package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPartnerbank implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private String c_ktonr;

    /** nullable persistent field */
    private String c_iban;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private String c_sepamandatsnummer;

    /** nullable persistent field */
    private Date t_sepaerteilt;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** full constructor */
    public FLRPartnerbank(Integer partner_i_id, String c_ktonr, String c_iban, Integer i_sort, String waehrung_c_nr, String c_sepamandatsnummer, Date t_sepaerteilt, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.partner_i_id = partner_i_id;
        this.c_ktonr = c_ktonr;
        this.c_iban = c_iban;
        this.i_sort = i_sort;
        this.waehrung_c_nr = waehrung_c_nr;
        this.c_sepamandatsnummer = c_sepamandatsnummer;
        this.t_sepaerteilt = t_sepaerteilt;
        this.flrpartner = flrpartner;
    }

    /** default constructor */
    public FLRPartnerbank() {
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

    public String getC_ktonr() {
        return this.c_ktonr;
    }

    public void setC_ktonr(String c_ktonr) {
        this.c_ktonr = c_ktonr;
    }

    public String getC_iban() {
        return this.c_iban;
    }

    public void setC_iban(String c_iban) {
        this.c_iban = c_iban;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public String getC_sepamandatsnummer() {
        return this.c_sepamandatsnummer;
    }

    public void setC_sepamandatsnummer(String c_sepamandatsnummer) {
        this.c_sepamandatsnummer = c_sepamandatsnummer;
    }

    public Date getT_sepaerteilt() {
        return this.t_sepaerteilt;
    }

    public void setT_sepaerteilt(Date t_sepaerteilt) {
        this.t_sepaerteilt = t_sepaerteilt;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
