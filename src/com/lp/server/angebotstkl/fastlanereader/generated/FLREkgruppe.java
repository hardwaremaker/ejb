package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREkgruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** full constructor */
    public FLREkgruppe(String c_bez, String mandant_c_nr) {
        this.c_bez = c_bez;
        this.mandant_c_nr = mandant_c_nr;
    }

    /** default constructor */
    public FLREkgruppe() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
