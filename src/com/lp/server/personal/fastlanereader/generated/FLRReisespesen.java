package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRReisespesen implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer reise_i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRReise flrreise;

    /** nullable persistent field */
    private FLREingangsrechnung flreingangsrechnung;

    /** full constructor */
    public FLRReisespesen(Integer reise_i_id, Integer eingangsrechnung_i_id, com.lp.server.personal.fastlanereader.generated.FLRReise flrreise, FLREingangsrechnung flreingangsrechnung) {
        this.reise_i_id = reise_i_id;
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.flrreise = flrreise;
        this.flreingangsrechnung = flreingangsrechnung;
    }

    /** default constructor */
    public FLRReisespesen() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getReise_i_id() {
        return this.reise_i_id;
    }

    public void setReise_i_id(Integer reise_i_id) {
        this.reise_i_id = reise_i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRReise getFlrreise() {
        return this.flrreise;
    }

    public void setFlrreise(com.lp.server.personal.fastlanereader.generated.FLRReise flrreise) {
        this.flrreise = flrreise;
    }

    public FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
