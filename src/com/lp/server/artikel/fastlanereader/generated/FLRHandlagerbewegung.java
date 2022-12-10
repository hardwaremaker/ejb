package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHandlagerbewegung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_verkaufspreis;

    /** nullable persistent field */
    private BigDecimal n_einstandspreis;

    /** nullable persistent field */
    private BigDecimal n_gestehungspreis;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Date t_buchungszeit;

    /** nullable persistent field */
    private Short b_abgang;

    /** nullable persistent field */
    private String c_snrchnr_mig;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_aendern;

    /** full constructor */
    public FLRHandlagerbewegung(BigDecimal n_menge, BigDecimal n_verkaufspreis, BigDecimal n_einstandspreis, BigDecimal n_gestehungspreis, String c_kommentar, Date t_buchungszeit, Short b_abgang, String c_snrchnr_mig, com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel, com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager, FLRPersonal flrpersonal_aendern) {
        this.n_menge = n_menge;
        this.n_verkaufspreis = n_verkaufspreis;
        this.n_einstandspreis = n_einstandspreis;
        this.n_gestehungspreis = n_gestehungspreis;
        this.c_kommentar = c_kommentar;
        this.t_buchungszeit = t_buchungszeit;
        this.b_abgang = b_abgang;
        this.c_snrchnr_mig = c_snrchnr_mig;
        this.flrartikel = flrartikel;
        this.flrlager = flrlager;
        this.flrpersonal_aendern = flrpersonal_aendern;
    }

    /** default constructor */
    public FLRHandlagerbewegung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_verkaufspreis() {
        return this.n_verkaufspreis;
    }

    public void setN_verkaufspreis(BigDecimal n_verkaufspreis) {
        this.n_verkaufspreis = n_verkaufspreis;
    }

    public BigDecimal getN_einstandspreis() {
        return this.n_einstandspreis;
    }

    public void setN_einstandspreis(BigDecimal n_einstandspreis) {
        this.n_einstandspreis = n_einstandspreis;
    }

    public BigDecimal getN_gestehungspreis() {
        return this.n_gestehungspreis;
    }

    public void setN_gestehungspreis(BigDecimal n_gestehungspreis) {
        this.n_gestehungspreis = n_gestehungspreis;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Date getT_buchungszeit() {
        return this.t_buchungszeit;
    }

    public void setT_buchungszeit(Date t_buchungszeit) {
        this.t_buchungszeit = t_buchungszeit;
    }

    public Short getB_abgang() {
        return this.b_abgang;
    }

    public void setB_abgang(Short b_abgang) {
        this.b_abgang = b_abgang;
    }

    public String getC_snrchnr_mig() {
        return this.c_snrchnr_mig;
    }

    public void setC_snrchnr_mig(String c_snrchnr_mig) {
        this.c_snrchnr_mig = c_snrchnr_mig;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLRPersonal getFlrpersonal_aendern() {
        return this.flrpersonal_aendern;
    }

    public void setFlrpersonal_aendern(FLRPersonal flrpersonal_aendern) {
        this.flrpersonal_aendern = flrpersonal_aendern;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
