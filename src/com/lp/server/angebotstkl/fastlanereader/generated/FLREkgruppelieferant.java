package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREkgruppelieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer ekgruppe_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** full constructor */
    public FLREkgruppelieferant(Integer ekgruppe_i_id, Integer lieferant_i_id, FLRLieferant flrlieferant) {
        this.ekgruppe_i_id = ekgruppe_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.flrlieferant = flrlieferant;
    }

    /** default constructor */
    public FLREkgruppelieferant() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEkgruppe_i_id() {
        return this.ekgruppe_i_id;
    }

    public void setEkgruppe_i_id(Integer ekgruppe_i_id) {
        this.ekgruppe_i_id = ekgruppe_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
