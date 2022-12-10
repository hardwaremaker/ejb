package com.lp.server.reklamation.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSchwere implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer i_punkte;

    /** persistent field */
    private Set sprset;

    /** full constructor */
    public FLRSchwere(String c_nr, Integer i_punkte, Set sprset) {
        this.c_nr = c_nr;
        this.i_punkte = i_punkte;
        this.sprset = sprset;
    }

    /** default constructor */
    public FLRSchwere() {
    }

    /** minimal constructor */
    public FLRSchwere(Set sprset) {
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

    public Integer getI_punkte() {
        return this.i_punkte;
    }

    public void setI_punkte(Integer i_punkte) {
        this.i_punkte = i_punkte;
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
