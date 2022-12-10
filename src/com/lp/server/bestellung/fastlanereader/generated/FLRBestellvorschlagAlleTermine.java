package com.lp.server.bestellung.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellvorschlagAlleTermine implements Serializable {

    /** identifier field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** full constructor */
    public FLRBestellvorschlagAlleTermine(String mandant_c_nr, Integer personal_i_id) {
        this.mandant_c_nr = mandant_c_nr;
        this.personal_i_id = personal_i_id;
    }

    /** default constructor */
    public FLRBestellvorschlagAlleTermine() {
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("t_liefertermin", getT_liefertermin())
            .toString();
    }

}
