package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRDsgvokategorie implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private Set sprset;

    /** full constructor */
    public FLRDsgvokategorie(String c_nr, Set sprset) {
        this.c_nr = c_nr;
        this.sprset = sprset;
    }

    /** default constructor */
    public FLRDsgvokategorie() {
    }

    /** minimal constructor */
    public FLRDsgvokategorie(Set sprset) {
        this.sprset = sprset;
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

    public Set getSprset() {
        return this.sprset;
    }

    public void setSprset(Set sprset) {
        this.sprset = sprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
