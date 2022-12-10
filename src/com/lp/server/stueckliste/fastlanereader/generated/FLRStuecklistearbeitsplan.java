package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.service.plscript.ScriptReportLogging;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStuecklistearbeitsplan implements Serializable {

	public ScriptReportLogging reportLogging;
	
    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_arbeitsgang;

    /** nullable persistent field */
    private Integer i_unterarbeitsgang;

    /** nullable persistent field */
    private Integer i_aufspannung;

    /** nullable persistent field */
    private Integer i_maschinenversatztage;

    /** nullable persistent field */
    private BigDecimal n_ppm;

    /** nullable persistent field */
    private Long l_stueckzeit;

    /** nullable persistent field */
    private Long l_ruestzeit;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Short b_nurmaschinenzeit;

    /** nullable persistent field */
    private Short b_initial;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private String x_formel;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** full constructor */
    public FLRStuecklistearbeitsplan(Integer i_arbeitsgang, Integer i_unterarbeitsgang, Integer i_aufspannung, Integer i_maschinenversatztage, BigDecimal n_ppm, Long l_stueckzeit, Long l_ruestzeit, String c_kommentar, Short b_nurmaschinenzeit, Short b_initial, Integer maschine_i_id, Integer stueckliste_i_id, String x_formel, com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste, FLRArtikel flrartikel, FLRMaschine flrmaschine) {
        this.i_arbeitsgang = i_arbeitsgang;
        this.i_unterarbeitsgang = i_unterarbeitsgang;
        this.i_aufspannung = i_aufspannung;
        this.i_maschinenversatztage = i_maschinenversatztage;
        this.n_ppm = n_ppm;
        this.l_stueckzeit = l_stueckzeit;
        this.l_ruestzeit = l_ruestzeit;
        this.c_kommentar = c_kommentar;
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
        this.b_initial = b_initial;
        this.maschine_i_id = maschine_i_id;
        this.stueckliste_i_id = stueckliste_i_id;
        this.x_formel = x_formel;
        this.flrstueckliste = flrstueckliste;
        this.flrartikel = flrartikel;
        this.flrmaschine = flrmaschine;
    }

    /** default constructor */
    public FLRStuecklistearbeitsplan() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_arbeitsgang() {
        return this.i_arbeitsgang;
    }

    public void setI_arbeitsgang(Integer i_arbeitsgang) {
        this.i_arbeitsgang = i_arbeitsgang;
    }

    public Integer getI_unterarbeitsgang() {
        return this.i_unterarbeitsgang;
    }

    public void setI_unterarbeitsgang(Integer i_unterarbeitsgang) {
        this.i_unterarbeitsgang = i_unterarbeitsgang;
    }

    public Integer getI_aufspannung() {
        return this.i_aufspannung;
    }

    public void setI_aufspannung(Integer i_aufspannung) {
        this.i_aufspannung = i_aufspannung;
    }

    public Integer getI_maschinenversatztage() {
        return this.i_maschinenversatztage;
    }

    public void setI_maschinenversatztage(Integer i_maschinenversatztage) {
        this.i_maschinenversatztage = i_maschinenversatztage;
    }

    public BigDecimal getN_ppm() {
        return this.n_ppm;
    }

    public void setN_ppm(BigDecimal n_ppm) {
        this.n_ppm = n_ppm;
    }

    public Long getL_stueckzeit() {
        return this.l_stueckzeit;
    }

    public void setL_stueckzeit(Long l_stueckzeit) {
        this.l_stueckzeit = l_stueckzeit;
    }

    public Long getL_ruestzeit() {
        return this.l_ruestzeit;
    }

    public void setL_ruestzeit(Long l_ruestzeit) {
        this.l_ruestzeit = l_ruestzeit;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Short getB_nurmaschinenzeit() {
        return this.b_nurmaschinenzeit;
    }

    public void setB_nurmaschinenzeit(Short b_nurmaschinenzeit) {
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
    }

    public Short getB_initial() {
        return this.b_initial;
    }

    public void setB_initial(Short b_initial) {
        this.b_initial = b_initial;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public String getX_formel() {
        return this.x_formel;
    }

    public void setX_formel(String x_formel) {
        this.x_formel = x_formel;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
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
