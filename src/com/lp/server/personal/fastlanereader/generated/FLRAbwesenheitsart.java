package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAbwesenheitsart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** persistent field */
    private Set sprset;

    /** full constructor */
    public FLRAbwesenheitsart(String c_nr, Integer i_sort, Set sprset) {
        this.c_nr = c_nr;
        this.i_sort = i_sort;
        this.sprset = sprset;
    }

    /** default constructor */
    public FLRAbwesenheitsart() {
    }

    /** minimal constructor */
    public FLRAbwesenheitsart(Set sprset) {
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

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
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
