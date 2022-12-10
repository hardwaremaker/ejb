package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKennung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_nr;

    /** persistent field */
    private String c_bez;

    /** persistent field */
    private Set kennungspr_set;

    /** full constructor */
    public FLRKennung(String c_nr, String c_bez, Set kennungspr_set) {
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.kennungspr_set = kennungspr_set;
    }

    /** default constructor */
    public FLRKennung() {
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

    public Set getKennungspr_set() {
        return this.kennungspr_set;
    }

    public void setKennungspr_set(Set kennungspr_set) {
        this.kennungspr_set = kennungspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
