package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitdaten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private String c_belegartnr;

    /** nullable persistent field */
    private Short b_taetigkeitgeaendert;

    /** nullable persistent field */
    private Short b_automatikbuchung;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer taetigkeit_i_id;

    /** nullable persistent field */
    private String c_bemerkungzubelegart;

    /** nullable persistent field */
    private String c_wowurdegebucht;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private String x_kommentar_intern;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private Double f_dauer_uebersteuert;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine;

    /** full constructor */
    public FLRZeitdaten(Date t_zeit, String c_belegartnr, Short b_taetigkeitgeaendert, Short b_automatikbuchung, Integer i_belegartid, Integer i_belegartpositionid, Date t_aendern, Integer personal_i_id, Integer artikel_i_id, Integer taetigkeit_i_id, String c_bemerkungzubelegart, String c_wowurdegebucht, String x_kommentar, String x_kommentar_intern, Double f_verrechenbar, Double f_dauer_uebersteuert, Date t_erledigt, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt, com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, FLRArtikelliste flrartikel, com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine) {
        this.t_zeit = t_zeit;
        this.c_belegartnr = c_belegartnr;
        this.b_taetigkeitgeaendert = b_taetigkeitgeaendert;
        this.b_automatikbuchung = b_automatikbuchung;
        this.i_belegartid = i_belegartid;
        this.i_belegartpositionid = i_belegartpositionid;
        this.t_aendern = t_aendern;
        this.personal_i_id = personal_i_id;
        this.artikel_i_id = artikel_i_id;
        this.taetigkeit_i_id = taetigkeit_i_id;
        this.c_bemerkungzubelegart = c_bemerkungzubelegart;
        this.c_wowurdegebucht = c_wowurdegebucht;
        this.x_kommentar = x_kommentar;
        this.x_kommentar_intern = x_kommentar_intern;
        this.f_verrechenbar = f_verrechenbar;
        this.f_dauer_uebersteuert = f_dauer_uebersteuert;
        this.t_erledigt = t_erledigt;
        this.flrpersonal_erledigt = flrpersonal_erledigt;
        this.flrtaetigkeit = flrtaetigkeit;
        this.flrpersonal = flrpersonal;
        this.flrartikel = flrartikel;
        this.flrmaschine = flrmaschine;
    }

    /** default constructor */
    public FLRZeitdaten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public String getC_belegartnr() {
        return this.c_belegartnr;
    }

    public void setC_belegartnr(String c_belegartnr) {
        this.c_belegartnr = c_belegartnr;
    }

    public Short getB_taetigkeitgeaendert() {
        return this.b_taetigkeitgeaendert;
    }

    public void setB_taetigkeitgeaendert(Short b_taetigkeitgeaendert) {
        this.b_taetigkeitgeaendert = b_taetigkeitgeaendert;
    }

    public Short getB_automatikbuchung() {
        return this.b_automatikbuchung;
    }

    public void setB_automatikbuchung(Short b_automatikbuchung) {
        this.b_automatikbuchung = b_automatikbuchung;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getTaetigkeit_i_id() {
        return this.taetigkeit_i_id;
    }

    public void setTaetigkeit_i_id(Integer taetigkeit_i_id) {
        this.taetigkeit_i_id = taetigkeit_i_id;
    }

    public String getC_bemerkungzubelegart() {
        return this.c_bemerkungzubelegart;
    }

    public void setC_bemerkungzubelegart(String c_bemerkungzubelegart) {
        this.c_bemerkungzubelegart = c_bemerkungzubelegart;
    }

    public String getC_wowurdegebucht() {
        return this.c_wowurdegebucht;
    }

    public void setC_wowurdegebucht(String c_wowurdegebucht) {
        this.c_wowurdegebucht = c_wowurdegebucht;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public String getX_kommentar_intern() {
        return this.x_kommentar_intern;
    }

    public void setX_kommentar_intern(String x_kommentar_intern) {
        this.x_kommentar_intern = x_kommentar_intern;
    }

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public Double getF_dauer_uebersteuert() {
        return this.f_dauer_uebersteuert;
    }

    public void setF_dauer_uebersteuert(Double f_dauer_uebersteuert) {
        this.f_dauer_uebersteuert = f_dauer_uebersteuert;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_erledigt() {
        return this.flrpersonal_erledigt;
    }

    public void setFlrpersonal_erledigt(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_erledigt) {
        this.flrpersonal_erledigt = flrpersonal_erledigt;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit getFlrtaetigkeit() {
        return this.flrtaetigkeit;
    }

    public void setFlrtaetigkeit(com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit) {
        this.flrtaetigkeit = flrtaetigkeit;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
