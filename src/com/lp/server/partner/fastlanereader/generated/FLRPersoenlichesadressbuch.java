package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPersoenlichesadressbuch implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private String c_emailadresse_partneradressbuch;

    /** nullable persistent field */
    private String c_exchangeid_partneradressbuch;

    /** nullable persistent field */
    private Date t_zuletzt_exportiert_partneradressbuch;

    /** nullable persistent field */
    private String c_emailadresse_ansprechpartneradressbuch;

    /** nullable persistent field */
    private String c_exchangeid_ansprechpartneradressbuch;

    /** nullable persistent field */
    private Date t_zuletzt_exportiert_ansprechpartneradressbuch;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner;

    /** full constructor */
    public FLRPersoenlichesadressbuch(Integer partner_i_id, Integer ansprechpartner_i_id, String c_emailadresse_partneradressbuch, String c_exchangeid_partneradressbuch, Date t_zuletzt_exportiert_partneradressbuch, String c_emailadresse_ansprechpartneradressbuch, String c_exchangeid_ansprechpartneradressbuch, Date t_zuletzt_exportiert_ansprechpartneradressbuch, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner) {
        this.partner_i_id = partner_i_id;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.c_emailadresse_partneradressbuch = c_emailadresse_partneradressbuch;
        this.c_exchangeid_partneradressbuch = c_exchangeid_partneradressbuch;
        this.t_zuletzt_exportiert_partneradressbuch = t_zuletzt_exportiert_partneradressbuch;
        this.c_emailadresse_ansprechpartneradressbuch = c_emailadresse_ansprechpartneradressbuch;
        this.c_exchangeid_ansprechpartneradressbuch = c_exchangeid_ansprechpartneradressbuch;
        this.t_zuletzt_exportiert_ansprechpartneradressbuch = t_zuletzt_exportiert_ansprechpartneradressbuch;
        this.flrpartner = flrpartner;
        this.flransprechpartner = flransprechpartner;
    }

    /** default constructor */
    public FLRPersoenlichesadressbuch() {
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

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public String getC_emailadresse_partneradressbuch() {
        return this.c_emailadresse_partneradressbuch;
    }

    public void setC_emailadresse_partneradressbuch(String c_emailadresse_partneradressbuch) {
        this.c_emailadresse_partneradressbuch = c_emailadresse_partneradressbuch;
    }

    public String getC_exchangeid_partneradressbuch() {
        return this.c_exchangeid_partneradressbuch;
    }

    public void setC_exchangeid_partneradressbuch(String c_exchangeid_partneradressbuch) {
        this.c_exchangeid_partneradressbuch = c_exchangeid_partneradressbuch;
    }

    public Date getT_zuletzt_exportiert_partneradressbuch() {
        return this.t_zuletzt_exportiert_partneradressbuch;
    }

    public void setT_zuletzt_exportiert_partneradressbuch(Date t_zuletzt_exportiert_partneradressbuch) {
        this.t_zuletzt_exportiert_partneradressbuch = t_zuletzt_exportiert_partneradressbuch;
    }

    public String getC_emailadresse_ansprechpartneradressbuch() {
        return this.c_emailadresse_ansprechpartneradressbuch;
    }

    public void setC_emailadresse_ansprechpartneradressbuch(String c_emailadresse_ansprechpartneradressbuch) {
        this.c_emailadresse_ansprechpartneradressbuch = c_emailadresse_ansprechpartneradressbuch;
    }

    public String getC_exchangeid_ansprechpartneradressbuch() {
        return this.c_exchangeid_ansprechpartneradressbuch;
    }

    public void setC_exchangeid_ansprechpartneradressbuch(String c_exchangeid_ansprechpartneradressbuch) {
        this.c_exchangeid_ansprechpartneradressbuch = c_exchangeid_ansprechpartneradressbuch;
    }

    public Date getT_zuletzt_exportiert_ansprechpartneradressbuch() {
        return this.t_zuletzt_exportiert_ansprechpartneradressbuch;
    }

    public void setT_zuletzt_exportiert_ansprechpartneradressbuch(Date t_zuletzt_exportiert_ansprechpartneradressbuch) {
        this.t_zuletzt_exportiert_ansprechpartneradressbuch = t_zuletzt_exportiert_ansprechpartneradressbuch;
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
