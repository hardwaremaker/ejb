package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitmodell implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht;

    /** persistent field */
    private Set zeitmodellsprset;

    /** persistent field */
    private Set zeitmodelltagset;

    /** full constructor */
    public FLRZeitmodell(String c_nr, String mandant_c_nr, Short b_versteckt, com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht, Set zeitmodellsprset, Set zeitmodelltagset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.b_versteckt = b_versteckt;
        this.flrschicht = flrschicht;
        this.zeitmodellsprset = zeitmodellsprset;
        this.zeitmodelltagset = zeitmodelltagset;
    }

    /** default constructor */
    public FLRZeitmodell() {
    }

    /** minimal constructor */
    public FLRZeitmodell(Set zeitmodellsprset, Set zeitmodelltagset) {
        this.zeitmodellsprset = zeitmodellsprset;
        this.zeitmodelltagset = zeitmodelltagset;
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

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRSchicht getFlrschicht() {
        return this.flrschicht;
    }

    public void setFlrschicht(com.lp.server.personal.fastlanereader.generated.FLRSchicht flrschicht) {
        this.flrschicht = flrschicht;
    }

    public Set getZeitmodellsprset() {
        return this.zeitmodellsprset;
    }

    public void setZeitmodellsprset(Set zeitmodellsprset) {
        this.zeitmodellsprset = zeitmodellsprset;
    }

    public Set getZeitmodelltagset() {
        return this.zeitmodelltagset;
    }

    public void setZeitmodelltagset(Set zeitmodelltagset) {
        this.zeitmodelltagset = zeitmodelltagset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
