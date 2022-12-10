package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPersonalgehalt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private BigDecimal n_gehalt;

    /** nullable persistent field */
    private Date t_gueltigab;

    /** nullable persistent field */
    private Double f_uestpauschale;

    /** nullable persistent field */
    private BigDecimal n_stundensatz;

    /** full constructor */
    public FLRPersonalgehalt(Integer personal_i_id, BigDecimal n_gehalt, Date t_gueltigab, Double f_uestpauschale, BigDecimal n_stundensatz) {
        this.personal_i_id = personal_i_id;
        this.n_gehalt = n_gehalt;
        this.t_gueltigab = t_gueltigab;
        this.f_uestpauschale = f_uestpauschale;
        this.n_stundensatz = n_stundensatz;
    }

    /** default constructor */
    public FLRPersonalgehalt() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public BigDecimal getN_gehalt() {
        return this.n_gehalt;
    }

    public void setN_gehalt(BigDecimal n_gehalt) {
        this.n_gehalt = n_gehalt;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public Double getF_uestpauschale() {
        return this.f_uestpauschale;
    }

    public void setF_uestpauschale(Double f_uestpauschale) {
        this.f_uestpauschale = f_uestpauschale;
    }

    public BigDecimal getN_stundensatz() {
        return this.n_stundensatz;
    }

    public void setN_stundensatz(BigDecimal n_stundensatz) {
        this.n_stundensatz = n_stundensatz;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
