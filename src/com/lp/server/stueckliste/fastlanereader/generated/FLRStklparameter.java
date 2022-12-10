package com.lp.server.stueckliste.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStklparameter implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_wert;

    /** nullable persistent field */
    private String c_typ;

    /** nullable persistent field */
    private BigDecimal n_min;

    /** nullable persistent field */
    private BigDecimal n_max;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste;

    /** persistent field */
    private Set stklparametersprset;

    /** full constructor */
    public FLRStklparameter(Integer i_sort, Integer stueckliste_i_id, String c_nr, String c_wert, String c_typ, BigDecimal n_min, BigDecimal n_max, com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste, Set stklparametersprset) {
        this.i_sort = i_sort;
        this.stueckliste_i_id = stueckliste_i_id;
        this.c_nr = c_nr;
        this.c_wert = c_wert;
        this.c_typ = c_typ;
        this.n_min = n_min;
        this.n_max = n_max;
        this.flrstueckliste = flrstueckliste;
        this.stklparametersprset = stklparametersprset;
    }

    /** default constructor */
    public FLRStklparameter() {
    }

    /** minimal constructor */
    public FLRStklparameter(Set stklparametersprset) {
        this.stklparametersprset = stklparametersprset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_wert() {
        return this.c_wert;
    }

    public void setC_wert(String c_wert) {
        this.c_wert = c_wert;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public BigDecimal getN_min() {
        return this.n_min;
    }

    public void setN_min(BigDecimal n_min) {
        this.n_min = n_min;
    }

    public BigDecimal getN_max() {
        return this.n_max;
    }

    public void setN_max(BigDecimal n_max) {
        this.n_max = n_max;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public Set getStklparametersprset() {
        return this.stklparametersprset;
    }

    public void setStklparametersprset(Set stklparametersprset) {
        this.stklparametersprset = stklparametersprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
