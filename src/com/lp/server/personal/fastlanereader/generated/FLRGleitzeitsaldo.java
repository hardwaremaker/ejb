package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGleitzeitsaldo implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer i_jahr;

    /** nullable persistent field */
    private Integer i_monat;

    /** nullable persistent field */
    private BigDecimal n_saldo;

    /** nullable persistent field */
    private BigDecimal n_saldomehrstunden;

    /** nullable persistent field */
    private BigDecimal n_saldouestfrei50;

    /** nullable persistent field */
    private BigDecimal n_saldouestpflichtig50;

    /** nullable persistent field */
    private BigDecimal n_saldouestfrei100;

    /** nullable persistent field */
    private BigDecimal n_saldouestpflichtig100;

    /** nullable persistent field */
    private BigDecimal n_saldouest200;

    /** nullable persistent field */
    private BigDecimal n_gz_saldo_mit_uestd_in_normalstunden;

    /** full constructor */
    public FLRGleitzeitsaldo(Integer personal_i_id, Integer i_jahr, Integer i_monat, BigDecimal n_saldo, BigDecimal n_saldomehrstunden, BigDecimal n_saldouestfrei50, BigDecimal n_saldouestpflichtig50, BigDecimal n_saldouestfrei100, BigDecimal n_saldouestpflichtig100, BigDecimal n_saldouest200, BigDecimal n_gz_saldo_mit_uestd_in_normalstunden) {
        this.personal_i_id = personal_i_id;
        this.i_jahr = i_jahr;
        this.i_monat = i_monat;
        this.n_saldo = n_saldo;
        this.n_saldomehrstunden = n_saldomehrstunden;
        this.n_saldouestfrei50 = n_saldouestfrei50;
        this.n_saldouestpflichtig50 = n_saldouestpflichtig50;
        this.n_saldouestfrei100 = n_saldouestfrei100;
        this.n_saldouestpflichtig100 = n_saldouestpflichtig100;
        this.n_saldouest200 = n_saldouest200;
        this.n_gz_saldo_mit_uestd_in_normalstunden = n_gz_saldo_mit_uestd_in_normalstunden;
    }

    /** default constructor */
    public FLRGleitzeitsaldo() {
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

    public Integer getI_jahr() {
        return this.i_jahr;
    }

    public void setI_jahr(Integer i_jahr) {
        this.i_jahr = i_jahr;
    }

    public Integer getI_monat() {
        return this.i_monat;
    }

    public void setI_monat(Integer i_monat) {
        this.i_monat = i_monat;
    }

    public BigDecimal getN_saldo() {
        return this.n_saldo;
    }

    public void setN_saldo(BigDecimal n_saldo) {
        this.n_saldo = n_saldo;
    }

    public BigDecimal getN_saldomehrstunden() {
        return this.n_saldomehrstunden;
    }

    public void setN_saldomehrstunden(BigDecimal n_saldomehrstunden) {
        this.n_saldomehrstunden = n_saldomehrstunden;
    }

    public BigDecimal getN_saldouestfrei50() {
        return this.n_saldouestfrei50;
    }

    public void setN_saldouestfrei50(BigDecimal n_saldouestfrei50) {
        this.n_saldouestfrei50 = n_saldouestfrei50;
    }

    public BigDecimal getN_saldouestpflichtig50() {
        return this.n_saldouestpflichtig50;
    }

    public void setN_saldouestpflichtig50(BigDecimal n_saldouestpflichtig50) {
        this.n_saldouestpflichtig50 = n_saldouestpflichtig50;
    }

    public BigDecimal getN_saldouestfrei100() {
        return this.n_saldouestfrei100;
    }

    public void setN_saldouestfrei100(BigDecimal n_saldouestfrei100) {
        this.n_saldouestfrei100 = n_saldouestfrei100;
    }

    public BigDecimal getN_saldouestpflichtig100() {
        return this.n_saldouestpflichtig100;
    }

    public void setN_saldouestpflichtig100(BigDecimal n_saldouestpflichtig100) {
        this.n_saldouestpflichtig100 = n_saldouestpflichtig100;
    }

    public BigDecimal getN_saldouest200() {
        return this.n_saldouest200;
    }

    public void setN_saldouest200(BigDecimal n_saldouest200) {
        this.n_saldouest200 = n_saldouest200;
    }

    public BigDecimal getN_gz_saldo_mit_uestd_in_normalstunden() {
        return this.n_gz_saldo_mit_uestd_in_normalstunden;
    }

    public void setN_gz_saldo_mit_uestd_in_normalstunden(BigDecimal n_gz_saldo_mit_uestd_in_normalstunden) {
        this.n_gz_saldo_mit_uestd_in_normalstunden = n_gz_saldo_mit_uestd_in_normalstunden;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
