package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichtenart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Short b_muss_erledigt_werden;

    /** persistent field */
    private Set nachrichtenartsprset;

    /** full constructor */
    public FLRNachrichtenart(String c_nr, Short b_muss_erledigt_werden, Set nachrichtenartsprset) {
        this.c_nr = c_nr;
        this.b_muss_erledigt_werden = b_muss_erledigt_werden;
        this.nachrichtenartsprset = nachrichtenartsprset;
    }

    /** default constructor */
    public FLRNachrichtenart() {
    }

    /** minimal constructor */
    public FLRNachrichtenart(Set nachrichtenartsprset) {
        this.nachrichtenartsprset = nachrichtenartsprset;
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

    public Short getB_muss_erledigt_werden() {
        return this.b_muss_erledigt_werden;
    }

    public void setB_muss_erledigt_werden(Short b_muss_erledigt_werden) {
        this.b_muss_erledigt_werden = b_muss_erledigt_werden;
    }

    public Set getNachrichtenartsprset() {
        return this.nachrichtenartsprset;
    }

    public void setNachrichtenartsprset(Set nachrichtenartsprset) {
        this.nachrichtenartsprset = nachrichtenartsprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
