package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRMandant;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWerkzeug implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String mandant_c_nr_standort;

    /** nullable persistent field */
    private FLRMandant flrmandant;

    /** full constructor */
    public FLRWerkzeug(String c_nr, String c_bez, String mandant_c_nr_standort, FLRMandant flrmandant) {
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.mandant_c_nr_standort = mandant_c_nr_standort;
        this.flrmandant = flrmandant;
    }

    /** default constructor */
    public FLRWerkzeug() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getMandant_c_nr_standort() {
        return this.mandant_c_nr_standort;
    }

    public void setMandant_c_nr_standort(String mandant_c_nr_standort) {
        this.mandant_c_nr_standort = mandant_c_nr_standort;
    }

    public FLRMandant getFlrmandant() {
        return this.flrmandant;
    }

    public void setFlrmandant(FLRMandant flrmandant) {
        this.flrmandant = flrmandant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
