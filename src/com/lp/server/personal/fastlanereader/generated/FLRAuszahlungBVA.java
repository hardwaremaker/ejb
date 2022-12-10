package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuszahlungBVA implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private BigDecimal n_gleitzeit;

    /** nullable persistent field */
    private BigDecimal n_uest50_gz;

    /** nullable persistent field */
    private BigDecimal n_uest50_gz_zuschlag;

    /** nullable persistent field */
    private BigDecimal n_uest50;

    /** nullable persistent field */
    private BigDecimal n_uest50_zuschlag;

    /** nullable persistent field */
    private BigDecimal n_uest100;

    /** nullable persistent field */
    private BigDecimal n_uest100_zuschlag;

    /** full constructor */
    public FLRAuszahlungBVA(Integer personal_i_id, Date t_datum, BigDecimal n_gleitzeit, BigDecimal n_uest50_gz, BigDecimal n_uest50_gz_zuschlag, BigDecimal n_uest50, BigDecimal n_uest50_zuschlag, BigDecimal n_uest100, BigDecimal n_uest100_zuschlag) {
        this.personal_i_id = personal_i_id;
        this.t_datum = t_datum;
        this.n_gleitzeit = n_gleitzeit;
        this.n_uest50_gz = n_uest50_gz;
        this.n_uest50_gz_zuschlag = n_uest50_gz_zuschlag;
        this.n_uest50 = n_uest50;
        this.n_uest50_zuschlag = n_uest50_zuschlag;
        this.n_uest100 = n_uest100;
        this.n_uest100_zuschlag = n_uest100_zuschlag;
    }

    /** default constructor */
    public FLRAuszahlungBVA() {
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

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public BigDecimal getN_gleitzeit() {
        return this.n_gleitzeit;
    }

    public void setN_gleitzeit(BigDecimal n_gleitzeit) {
        this.n_gleitzeit = n_gleitzeit;
    }

    public BigDecimal getN_uest50_gz() {
        return this.n_uest50_gz;
    }

    public void setN_uest50_gz(BigDecimal n_uest50_gz) {
        this.n_uest50_gz = n_uest50_gz;
    }

    public BigDecimal getN_uest50_gz_zuschlag() {
        return this.n_uest50_gz_zuschlag;
    }

    public void setN_uest50_gz_zuschlag(BigDecimal n_uest50_gz_zuschlag) {
        this.n_uest50_gz_zuschlag = n_uest50_gz_zuschlag;
    }

    public BigDecimal getN_uest50() {
        return this.n_uest50;
    }

    public void setN_uest50(BigDecimal n_uest50) {
        this.n_uest50 = n_uest50;
    }

    public BigDecimal getN_uest50_zuschlag() {
        return this.n_uest50_zuschlag;
    }

    public void setN_uest50_zuschlag(BigDecimal n_uest50_zuschlag) {
        this.n_uest50_zuschlag = n_uest50_zuschlag;
    }

    public BigDecimal getN_uest100() {
        return this.n_uest100;
    }

    public void setN_uest100(BigDecimal n_uest100) {
        this.n_uest100 = n_uest100;
    }

    public BigDecimal getN_uest100_zuschlag() {
        return this.n_uest100_zuschlag;
    }

    public void setN_uest100_zuschlag(BigDecimal n_uest100_zuschlag) {
        this.n_uest100_zuschlag = n_uest100_zuschlag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
