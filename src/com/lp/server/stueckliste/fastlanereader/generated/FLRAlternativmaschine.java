package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAlternativmaschine implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer stuecklistearbeitsplan_i_id;

    /** nullable persistent field */
    private BigDecimal n_korrekturfaktor;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan flrstuecklisteposition;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** full constructor */
    public FLRAlternativmaschine(Integer i_sort, Integer stuecklistearbeitsplan_i_id, BigDecimal n_korrekturfaktor, com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan flrstuecklisteposition, FLRMaschine flrmaschine) {
        this.i_sort = i_sort;
        this.stuecklistearbeitsplan_i_id = stuecklistearbeitsplan_i_id;
        this.n_korrekturfaktor = n_korrekturfaktor;
        this.flrstuecklisteposition = flrstuecklisteposition;
        this.flrmaschine = flrmaschine;
    }

    /** default constructor */
    public FLRAlternativmaschine() {
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

    public Integer getStuecklistearbeitsplan_i_id() {
        return this.stuecklistearbeitsplan_i_id;
    }

    public void setStuecklistearbeitsplan_i_id(Integer stuecklistearbeitsplan_i_id) {
        this.stuecklistearbeitsplan_i_id = stuecklistearbeitsplan_i_id;
    }

    public BigDecimal getN_korrekturfaktor() {
        return this.n_korrekturfaktor;
    }

    public void setN_korrekturfaktor(BigDecimal n_korrekturfaktor) {
        this.n_korrekturfaktor = n_korrekturfaktor;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan getFlrstuecklisteposition() {
        return this.flrstuecklisteposition;
    }

    public void setFlrstuecklisteposition(com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan flrstuecklisteposition) {
        this.flrstuecklisteposition = flrstuecklisteposition;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
