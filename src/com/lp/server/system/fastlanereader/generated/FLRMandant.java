package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMandant implements Serializable {

    /** identifier field */
    private String c_nr;

    /** persistent field */
    private String c_kbez;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_lieferadresse;

    /** full constructor */
    public FLRMandant(String c_kbez, Integer partner_i_id, Integer partner_i_id_lieferadresse) {
        this.c_kbez = c_kbez;
        this.partner_i_id = partner_i_id;
        this.partner_i_id_lieferadresse = partner_i_id_lieferadresse;
    }

    /** default constructor */
    public FLRMandant() {
    }

    /** minimal constructor */
    public FLRMandant(String c_kbez) {
        this.c_kbez = c_kbez;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_kbez() {
        return this.c_kbez;
    }

    public void setC_kbez(String c_kbez) {
        this.c_kbez = c_kbez;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public Integer getPartner_i_id_lieferadresse() {
        return this.partner_i_id_lieferadresse;
    }

    public void setPartner_i_id_lieferadresse(Integer partner_i_id_lieferadresse) {
        this.partner_i_id_lieferadresse = partner_i_id_lieferadresse;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .toString();
    }

}
