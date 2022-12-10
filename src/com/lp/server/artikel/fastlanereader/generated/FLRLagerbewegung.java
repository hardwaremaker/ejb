package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRBelegart;
import com.lp.server.system.fastlanereader.generated.FLRLand;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLagerbewegung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_buchungszeit;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private String c_seriennrchargennr;

    /** nullable persistent field */
    private String c_belegartnr;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_id_buchung;

    /** nullable persistent field */
    private Short b_abgang;

    /** nullable persistent field */
    private Short b_historie;

    /** nullable persistent field */
    private String c_version;

    /** nullable persistent field */
    private Short b_vollstaendigverbraucht;

    /** nullable persistent field */
    private BigDecimal n_verkaufspreis;

    /** nullable persistent field */
    private BigDecimal n_einstandspreis;

    /** nullable persistent field */
    private BigDecimal n_gestehungspreis;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer lager_i_id;

    /** nullable persistent field */
    private BigDecimal n_gebindemenge;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

    /** nullable persistent field */
    private FLRBelegart flrbelegart;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller;

    /** nullable persistent field */
    private FLRLand flrland;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde;

    /** full constructor */
    public FLRLagerbewegung(BigDecimal n_menge, Date t_buchungszeit, Date t_belegdatum, String c_seriennrchargennr, String c_belegartnr, Integer i_belegartpositionid, Integer i_belegartid, Integer i_id_buchung, Short b_abgang, Short b_historie, String c_version, Short b_vollstaendigverbraucht, BigDecimal n_verkaufspreis, BigDecimal n_einstandspreis, BigDecimal n_gestehungspreis, Integer artikel_i_id, Integer lager_i_id, BigDecimal n_gebindemenge, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager, FLRBelegart flrbelegart, com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller, FLRLand flrland, FLRPersonal flrpersonal, com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde) {
        this.n_menge = n_menge;
        this.t_buchungszeit = t_buchungszeit;
        this.t_belegdatum = t_belegdatum;
        this.c_seriennrchargennr = c_seriennrchargennr;
        this.c_belegartnr = c_belegartnr;
        this.i_belegartpositionid = i_belegartpositionid;
        this.i_belegartid = i_belegartid;
        this.i_id_buchung = i_id_buchung;
        this.b_abgang = b_abgang;
        this.b_historie = b_historie;
        this.c_version = c_version;
        this.b_vollstaendigverbraucht = b_vollstaendigverbraucht;
        this.n_verkaufspreis = n_verkaufspreis;
        this.n_einstandspreis = n_einstandspreis;
        this.n_gestehungspreis = n_gestehungspreis;
        this.artikel_i_id = artikel_i_id;
        this.lager_i_id = lager_i_id;
        this.n_gebindemenge = n_gebindemenge;
        this.flrartikel = flrartikel;
        this.flrlager = flrlager;
        this.flrbelegart = flrbelegart;
        this.flrhersteller = flrhersteller;
        this.flrland = flrland;
        this.flrpersonal = flrpersonal;
        this.flrgebinde = flrgebinde;
    }

    /** default constructor */
    public FLRLagerbewegung() {
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

    public Date getT_buchungszeit() {
        return this.t_buchungszeit;
    }

    public void setT_buchungszeit(Date t_buchungszeit) {
        this.t_buchungszeit = t_buchungszeit;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public String getC_seriennrchargennr() {
        return this.c_seriennrchargennr;
    }

    public void setC_seriennrchargennr(String c_seriennrchargennr) {
        this.c_seriennrchargennr = c_seriennrchargennr;
    }

    public String getC_belegartnr() {
        return this.c_belegartnr;
    }

    public void setC_belegartnr(String c_belegartnr) {
        this.c_belegartnr = c_belegartnr;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Integer getI_id_buchung() {
        return this.i_id_buchung;
    }

    public void setI_id_buchung(Integer i_id_buchung) {
        this.i_id_buchung = i_id_buchung;
    }

    public Short getB_abgang() {
        return this.b_abgang;
    }

    public void setB_abgang(Short b_abgang) {
        this.b_abgang = b_abgang;
    }

    public Short getB_historie() {
        return this.b_historie;
    }

    public void setB_historie(Short b_historie) {
        this.b_historie = b_historie;
    }

    public String getC_version() {
        return this.c_version;
    }

    public void setC_version(String c_version) {
        this.c_version = c_version;
    }

    public Short getB_vollstaendigverbraucht() {
        return this.b_vollstaendigverbraucht;
    }

    public void setB_vollstaendigverbraucht(Short b_vollstaendigverbraucht) {
        this.b_vollstaendigverbraucht = b_vollstaendigverbraucht;
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

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLager_i_id() {
        return this.lager_i_id;
    }

    public void setLager_i_id(Integer lager_i_id) {
        this.lager_i_id = lager_i_id;
    }

    public BigDecimal getN_gebindemenge() {
        return this.n_gebindemenge;
    }

    public void setN_gebindemenge(BigDecimal n_gebindemenge) {
        this.n_gebindemenge = n_gebindemenge;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLRBelegart getFlrbelegart() {
        return this.flrbelegart;
    }

    public void setFlrbelegart(FLRBelegart flrbelegart) {
        this.flrbelegart = flrbelegart;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRHersteller getFlrhersteller() {
        return this.flrhersteller;
    }

    public void setFlrhersteller(com.lp.server.artikel.fastlanereader.generated.FLRHersteller flrhersteller) {
        this.flrhersteller = flrhersteller;
    }

    public FLRLand getFlrland() {
        return this.flrland;
    }

    public void setFlrland(FLRLand flrland) {
        this.flrland = flrland;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRGebinde getFlrgebinde() {
        return this.flrgebinde;
    }

    public void setFlrgebinde(com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde) {
        this.flrgebinde = flrgebinde;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
