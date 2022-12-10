package com.lp.server.stueckliste.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPruefart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private Set pruefartspr_set;

    /** full constructor */
    public FLRPruefart(String c_nr, Set pruefartspr_set) {
        this.c_nr = c_nr;
        this.pruefartspr_set = pruefartspr_set;
    }

    /** default constructor */
    public FLRPruefart() {
    }

    /** minimal constructor */
    public FLRPruefart(Set pruefartspr_set) {
        this.pruefartspr_set = pruefartspr_set;
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

    public Set getPruefartspr_set() {
        return this.pruefartspr_set;
    }

    public void setPruefartspr_set(Set pruefartspr_set) {
        this.pruefartspr_set = pruefartspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
