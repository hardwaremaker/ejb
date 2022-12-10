package com.lp.server.rechnung.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerrechnungsmodell implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** persistent field */
    private Set verrechnungsmodelltagset;

    /** full constructor */
    public FLRVerrechnungsmodell(String c_bez, String mandant_c_nr, Short b_versteckt, Set verrechnungsmodelltagset) {
        this.c_bez = c_bez;
        this.mandant_c_nr = mandant_c_nr;
        this.b_versteckt = b_versteckt;
        this.verrechnungsmodelltagset = verrechnungsmodelltagset;
    }

    /** default constructor */
    public FLRVerrechnungsmodell() {
    }

    /** minimal constructor */
    public FLRVerrechnungsmodell(Set verrechnungsmodelltagset) {
        this.verrechnungsmodelltagset = verrechnungsmodelltagset;
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

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Set getVerrechnungsmodelltagset() {
        return this.verrechnungsmodelltagset;
    }

    public void setVerrechnungsmodelltagset(Set verrechnungsmodelltagset) {
        this.verrechnungsmodelltagset = verrechnungsmodelltagset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
