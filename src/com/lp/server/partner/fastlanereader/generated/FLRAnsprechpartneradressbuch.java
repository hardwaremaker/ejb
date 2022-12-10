package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnsprechpartneradressbuch implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private String c_emailadresse;

    /** nullable persistent field */
    private String c_exchangeid;

    /** nullable persistent field */
    private Date t_zuletzt_exportiert;

    /** full constructor */
    public FLRAnsprechpartneradressbuch(Integer ansprechpartner_i_id, String c_emailadresse, String c_exchangeid, Date t_zuletzt_exportiert) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.c_emailadresse = c_emailadresse;
        this.c_exchangeid = c_exchangeid;
        this.t_zuletzt_exportiert = t_zuletzt_exportiert;
    }

    /** default constructor */
    public FLRAnsprechpartneradressbuch() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public String getC_emailadresse() {
        return this.c_emailadresse;
    }

    public void setC_emailadresse(String c_emailadresse) {
        this.c_emailadresse = c_emailadresse;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
