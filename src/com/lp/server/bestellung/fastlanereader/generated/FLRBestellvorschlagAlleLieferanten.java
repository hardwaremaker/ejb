package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellvorschlagAlleLieferanten implements Serializable {

    /** identifier field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** full constructor */
    public FLRBestellvorschlagAlleLieferanten(String mandant_c_nr, Integer personal_i_id, FLRLieferant flrlieferant) {
        this.mandant_c_nr = mandant_c_nr;
        this.personal_i_id = personal_i_id;
        this.flrlieferant = flrlieferant;
    }

    /** default constructor */
    public FLRBestellvorschlagAlleLieferanten() {
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
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

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("lieferant_i_id", getLieferant_i_id())
            .toString();
    }

}
