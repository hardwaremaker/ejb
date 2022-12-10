package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.reklamation.fastlanereader.generated.FLRFehler;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLosgutschlecht implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer lossollarbeitsplan_i_id;

    /** nullable persistent field */
    private BigDecimal n_gut;

    /** nullable persistent field */
    private BigDecimal n_schlecht;

    /** nullable persistent field */
    private BigDecimal n_inarbeit;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_zeitpunkt;

    /** nullable persistent field */
    private FLRZeitdaten flrzeitdaten;

    /** nullable persistent field */
    private FLRMaschinenzeitdaten flrmaschinenzeitdaten;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan;

    /** nullable persistent field */
    private FLRFehler flrfehler;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_erfasst;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_anlegen;

    /** full constructor */
    public FLRLosgutschlecht(Integer lossollarbeitsplan_i_id, BigDecimal n_gut, BigDecimal n_schlecht, BigDecimal n_inarbeit, String c_kommentar, Date t_anlegen, Date t_zeitpunkt, FLRZeitdaten flrzeitdaten, FLRMaschinenzeitdaten flrmaschinenzeitdaten, com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan, FLRFehler flrfehler, FLRPersonal flrpersonal_erfasst, FLRPersonal flrpersonal_anlegen) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
        this.n_gut = n_gut;
        this.n_schlecht = n_schlecht;
        this.n_inarbeit = n_inarbeit;
        this.c_kommentar = c_kommentar;
        this.t_anlegen = t_anlegen;
        this.t_zeitpunkt = t_zeitpunkt;
        this.flrzeitdaten = flrzeitdaten;
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
        this.flrfehler = flrfehler;
        this.flrpersonal_erfasst = flrpersonal_erfasst;
        this.flrpersonal_anlegen = flrpersonal_anlegen;
    }

    /** default constructor */
    public FLRLosgutschlecht() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLossollarbeitsplan_i_id() {
        return this.lossollarbeitsplan_i_id;
    }

    public void setLossollarbeitsplan_i_id(Integer lossollarbeitsplan_i_id) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
    }

    public BigDecimal getN_gut() {
        return this.n_gut;
    }

    public void setN_gut(BigDecimal n_gut) {
        this.n_gut = n_gut;
    }

    public BigDecimal getN_schlecht() {
        return this.n_schlecht;
    }

    public void setN_schlecht(BigDecimal n_schlecht) {
        this.n_schlecht = n_schlecht;
    }

    public BigDecimal getN_inarbeit() {
        return this.n_inarbeit;
    }

    public void setN_inarbeit(BigDecimal n_inarbeit) {
        this.n_inarbeit = n_inarbeit;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_zeitpunkt() {
        return this.t_zeitpunkt;
    }

    public void setT_zeitpunkt(Date t_zeitpunkt) {
        this.t_zeitpunkt = t_zeitpunkt;
    }

    public FLRZeitdaten getFlrzeitdaten() {
        return this.flrzeitdaten;
    }

    public void setFlrzeitdaten(FLRZeitdaten flrzeitdaten) {
        this.flrzeitdaten = flrzeitdaten;
    }

    public FLRMaschinenzeitdaten getFlrmaschinenzeitdaten() {
        return this.flrmaschinenzeitdaten;
    }

    public void setFlrmaschinenzeitdaten(FLRMaschinenzeitdaten flrmaschinenzeitdaten) {
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan getFlrlossollarbeitsplan() {
        return this.flrlossollarbeitsplan;
    }

    public void setFlrlossollarbeitsplan(com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    public FLRFehler getFlrfehler() {
        return this.flrfehler;
    }

    public void setFlrfehler(FLRFehler flrfehler) {
        this.flrfehler = flrfehler;
    }

    public FLRPersonal getFlrpersonal_erfasst() {
        return this.flrpersonal_erfasst;
    }

    public void setFlrpersonal_erfasst(FLRPersonal flrpersonal_erfasst) {
        this.flrpersonal_erfasst = flrpersonal_erfasst;
    }

    public FLRPersonal getFlrpersonal_anlegen() {
        return this.flrpersonal_anlegen;
    }

    public void setFlrpersonal_anlegen(FLRPersonal flrpersonal_anlegen) {
        this.flrpersonal_anlegen = flrpersonal_anlegen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
