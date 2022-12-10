package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRReise implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer fahrzeug_i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Short b_beginn;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String c_fahrzeug;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_spesen;

    /** nullable persistent field */
    private Integer i_kmbeginn;

    /** nullable persistent field */
    private Integer i_kmende;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Integer i_mitfahrer;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten;

    /** full constructor */
    public FLRReise(Integer personal_i_id, Integer fahrzeug_i_id, Date t_zeit, Short b_beginn, String c_kommentar, String c_fahrzeug, String belegart_c_nr, BigDecimal n_spesen, Integer i_kmbeginn, Integer i_kmende, Integer i_belegartid, Double f_verrechenbar, Date t_erledigt, Integer i_mitfahrer, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt, FLRPartner flrpartner, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug, com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
        this.personal_i_id = personal_i_id;
        this.fahrzeug_i_id = fahrzeug_i_id;
        this.t_zeit = t_zeit;
        this.b_beginn = b_beginn;
        this.c_kommentar = c_kommentar;
        this.c_fahrzeug = c_fahrzeug;
        this.belegart_c_nr = belegart_c_nr;
        this.n_spesen = n_spesen;
        this.i_kmbeginn = i_kmbeginn;
        this.i_kmende = i_kmende;
        this.i_belegartid = i_belegartid;
        this.f_verrechenbar = f_verrechenbar;
        this.t_erledigt = t_erledigt;
        this.i_mitfahrer = i_mitfahrer;
        this.flrpersonal_erledigt = flrpersonal_erledigt;
        this.flrpartner = flrpartner;
        this.flrpersonal = flrpersonal;
        this.flrfahrzeug = flrfahrzeug;
        this.flrdiaeten = flrdiaeten;
    }

    /** default constructor */
    public FLRReise() {
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

    public Integer getFahrzeug_i_id() {
        return this.fahrzeug_i_id;
    }

    public void setFahrzeug_i_id(Integer fahrzeug_i_id) {
        this.fahrzeug_i_id = fahrzeug_i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Short getB_beginn() {
        return this.b_beginn;
    }

    public void setB_beginn(Short b_beginn) {
        this.b_beginn = b_beginn;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getC_fahrzeug() {
        return this.c_fahrzeug;
    }

    public void setC_fahrzeug(String c_fahrzeug) {
        this.c_fahrzeug = c_fahrzeug;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public BigDecimal getN_spesen() {
        return this.n_spesen;
    }

    public void setN_spesen(BigDecimal n_spesen) {
        this.n_spesen = n_spesen;
    }

    public Integer getI_kmbeginn() {
        return this.i_kmbeginn;
    }

    public void setI_kmbeginn(Integer i_kmbeginn) {
        this.i_kmbeginn = i_kmbeginn;
    }

    public Integer getI_kmende() {
        return this.i_kmende;
    }

    public void setI_kmende(Integer i_kmende) {
        this.i_kmende = i_kmende;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Integer getI_mitfahrer() {
        return this.i_mitfahrer;
    }

    public void setI_mitfahrer(Integer i_mitfahrer) {
        this.i_mitfahrer = i_mitfahrer;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_erledigt() {
        return this.flrpersonal_erledigt;
    }

    public void setFlrpersonal_erledigt(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt) {
        this.flrpersonal_erledigt = flrpersonal_erledigt;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRFahrzeug getFlrfahrzeug() {
        return this.flrfahrzeug;
    }

    public void setFlrfahrzeug(com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug) {
        this.flrfahrzeug = flrfahrzeug;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRDiaeten getFlrdiaeten() {
        return this.flrdiaeten;
    }

    public void setFlrdiaeten(com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
        this.flrdiaeten = flrdiaeten;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
