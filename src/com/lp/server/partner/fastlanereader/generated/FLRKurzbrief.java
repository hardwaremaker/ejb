package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKurzbrief implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_betreff;

    /** nullable persistent field */
    private Integer personal_i_id_aendern;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner;

    /** full constructor */
    public FLRKurzbrief(String c_betreff, Integer personal_i_id_aendern, Date t_aendern, Integer partner_i_id, String belegart_c_nr, String mandant_c_nr, FLRPersonal flrpersonal, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner) {
        this.c_betreff = c_betreff;
        this.personal_i_id_aendern = personal_i_id_aendern;
        this.t_aendern = t_aendern;
        this.partner_i_id = partner_i_id;
        this.belegart_c_nr = belegart_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.flrpersonal = flrpersonal;
        this.flrpartner = flrpartner;
        this.flransprechpartner = flransprechpartner;
    }

    /** default constructor */
    public FLRKurzbrief() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_betreff() {
        return this.c_betreff;
    }

    public void setC_betreff(String c_betreff) {
        this.c_betreff = c_betreff;
    }

    public Integer getPersonal_i_id_aendern() {
        return this.personal_i_id_aendern;
    }

    public void setPersonal_i_id_aendern(Integer personal_i_id_aendern) {
        this.personal_i_id_aendern = personal_i_id_aendern;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner getFlransprechpartner() {
        return this.flransprechpartner;
    }

    public void setFlransprechpartner(com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner) {
        this.flransprechpartner = flransprechpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
