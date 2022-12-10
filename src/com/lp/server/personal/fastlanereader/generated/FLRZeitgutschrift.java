package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitgutschrift implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private Date u_gutschrift_kommt;

    /** nullable persistent field */
    private Date u_gutschrift_geht;

    /** full constructor */
    public FLRZeitgutschrift(Integer personal_i_id, Date t_datum, Date u_gutschrift_kommt, Date u_gutschrift_geht) {
        this.personal_i_id = personal_i_id;
        this.t_datum = t_datum;
        this.u_gutschrift_kommt = u_gutschrift_kommt;
        this.u_gutschrift_geht = u_gutschrift_geht;
    }

    /** default constructor */
    public FLRZeitgutschrift() {
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

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public Date getU_gutschrift_kommt() {
        return this.u_gutschrift_kommt;
    }

    public void setU_gutschrift_kommt(Date u_gutschrift_kommt) {
        this.u_gutschrift_kommt = u_gutschrift_kommt;
    }

    public Date getU_gutschrift_geht() {
        return this.u_gutschrift_geht;
    }

    public void setU_gutschrift_geht(Date u_gutschrift_geht) {
        this.u_gutschrift_geht = u_gutschrift_geht;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
