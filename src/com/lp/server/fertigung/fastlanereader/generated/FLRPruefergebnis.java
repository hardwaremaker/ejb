package com.lp.server.fertigung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPruefergebnis implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer losablieferung_i_id;

    /** nullable persistent field */
    private Integer lospruefplan_i_id;

    /** nullable persistent field */
    private BigDecimal n_crimphoehe_draht;

    /** nullable persistent field */
    private BigDecimal n_crimphoehe_isolation;

    /** nullable persistent field */
    private BigDecimal n_crimpbreite_draht;

    /** nullable persistent field */
    private BigDecimal n_crimpbreite_isolation;

    /** nullable persistent field */
    private BigDecimal n_wert;

    /** nullable persistent field */
    private Short b_wert;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLospruefplan flrlospruefplan;

    /** full constructor */
    public FLRPruefergebnis(Integer losablieferung_i_id, Integer lospruefplan_i_id, BigDecimal n_crimphoehe_draht, BigDecimal n_crimphoehe_isolation, BigDecimal n_crimpbreite_draht, BigDecimal n_crimpbreite_isolation, BigDecimal n_wert, Short b_wert, com.lp.server.fertigung.fastlanereader.generated.FLRLospruefplan flrlospruefplan) {
        this.losablieferung_i_id = losablieferung_i_id;
        this.lospruefplan_i_id = lospruefplan_i_id;
        this.n_crimphoehe_draht = n_crimphoehe_draht;
        this.n_crimphoehe_isolation = n_crimphoehe_isolation;
        this.n_crimpbreite_draht = n_crimpbreite_draht;
        this.n_crimpbreite_isolation = n_crimpbreite_isolation;
        this.n_wert = n_wert;
        this.b_wert = b_wert;
        this.flrlospruefplan = flrlospruefplan;
    }

    /** default constructor */
    public FLRPruefergebnis() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLosablieferung_i_id() {
        return this.losablieferung_i_id;
    }

    public void setLosablieferung_i_id(Integer losablieferung_i_id) {
        this.losablieferung_i_id = losablieferung_i_id;
    }

    public Integer getLospruefplan_i_id() {
        return this.lospruefplan_i_id;
    }

    public void setLospruefplan_i_id(Integer lospruefplan_i_id) {
        this.lospruefplan_i_id = lospruefplan_i_id;
    }

    public BigDecimal getN_crimphoehe_draht() {
        return this.n_crimphoehe_draht;
    }

    public void setN_crimphoehe_draht(BigDecimal n_crimphoehe_draht) {
        this.n_crimphoehe_draht = n_crimphoehe_draht;
    }

    public BigDecimal getN_crimphoehe_isolation() {
        return this.n_crimphoehe_isolation;
    }

    public void setN_crimphoehe_isolation(BigDecimal n_crimphoehe_isolation) {
        this.n_crimphoehe_isolation = n_crimphoehe_isolation;
    }

    public BigDecimal getN_crimpbreite_draht() {
        return this.n_crimpbreite_draht;
    }

    public void setN_crimpbreite_draht(BigDecimal n_crimpbreite_draht) {
        this.n_crimpbreite_draht = n_crimpbreite_draht;
    }

    public BigDecimal getN_crimpbreite_isolation() {
        return this.n_crimpbreite_isolation;
    }

    public void setN_crimpbreite_isolation(BigDecimal n_crimpbreite_isolation) {
        this.n_crimpbreite_isolation = n_crimpbreite_isolation;
    }

    public BigDecimal getN_wert() {
        return this.n_wert;
    }

    public void setN_wert(BigDecimal n_wert) {
        this.n_wert = n_wert;
    }

    public Short getB_wert() {
        return this.b_wert;
    }

    public void setB_wert(Short b_wert) {
        this.b_wert = b_wert;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLospruefplan getFlrlospruefplan() {
        return this.flrlospruefplan;
    }

    public void setFlrlospruefplan(com.lp.server.fertigung.fastlanereader.generated.FLRLospruefplan flrlospruefplan) {
        this.flrlospruefplan = flrlospruefplan;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
