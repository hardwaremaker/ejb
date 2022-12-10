package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLossollarbeitsplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_zugeordneter;

    /** nullable persistent field */
    private BigDecimal l_ruestzeit;

    /** nullable persistent field */
    private BigDecimal l_stueckzeit;

    /** nullable persistent field */
    private BigDecimal n_gesamtzeit;

    /** nullable persistent field */
    private Integer i_arbeitsgangsnummer;

    /** nullable persistent field */
    private Integer i_maschinenversatztage;

    /** nullable persistent field */
    private Integer i_maschinenversatz_ms;

    /** nullable persistent field */
    private Integer i_unterarbeitsgang;

    /** nullable persistent field */
    private Short b_nachtraeglich;

    /** nullable persistent field */
    private Short b_nurmaschinenzeit;

    /** nullable persistent field */
    private Short b_fertig;

    /** nullable persistent field */
    private Double f_fortschritt;

    /** nullable persistent field */
    private String agart_c_nr;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Integer i_aufspannung;

    /** nullable persistent field */
    private Integer i_reihung;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id;

    /** nullable persistent field */
    private Short b_autoendebeigeht;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_zugeordneter;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrsollmaterial;

    /** nullable persistent field */
    private FLRApkommentar flrapkommentar;

    /** full constructor */
    public FLRLossollarbeitsplan(Integer los_i_id, Integer maschine_i_id, Integer personal_i_id_zugeordneter, BigDecimal l_ruestzeit, BigDecimal l_stueckzeit, BigDecimal n_gesamtzeit, Integer i_arbeitsgangsnummer, Integer i_maschinenversatztage, Integer i_maschinenversatz_ms, Integer i_unterarbeitsgang, Short b_nachtraeglich, Short b_nurmaschinenzeit, Short b_fertig, Double f_fortschritt, String agart_c_nr, String c_kommentar, Integer i_aufspannung, Integer i_reihung, Integer lossollmaterial_i_id, Short b_autoendebeigeht, FLRArtikel flrartikel, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, FLRMaschine flrmaschine, FLRPersonal flrpersonal_zugeordneter, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrsollmaterial, FLRApkommentar flrapkommentar) {
        this.los_i_id = los_i_id;
        this.maschine_i_id = maschine_i_id;
        this.personal_i_id_zugeordneter = personal_i_id_zugeordneter;
        this.l_ruestzeit = l_ruestzeit;
        this.l_stueckzeit = l_stueckzeit;
        this.n_gesamtzeit = n_gesamtzeit;
        this.i_arbeitsgangsnummer = i_arbeitsgangsnummer;
        this.i_maschinenversatztage = i_maschinenversatztage;
        this.i_maschinenversatz_ms = i_maschinenversatz_ms;
        this.i_unterarbeitsgang = i_unterarbeitsgang;
        this.b_nachtraeglich = b_nachtraeglich;
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
        this.b_fertig = b_fertig;
        this.f_fortschritt = f_fortschritt;
        this.agart_c_nr = agart_c_nr;
        this.c_kommentar = c_kommentar;
        this.i_aufspannung = i_aufspannung;
        this.i_reihung = i_reihung;
        this.lossollmaterial_i_id = lossollmaterial_i_id;
        this.b_autoendebeigeht = b_autoendebeigeht;
        this.flrartikel = flrartikel;
        this.flrlos = flrlos;
        this.flrmaschine = flrmaschine;
        this.flrpersonal_zugeordneter = flrpersonal_zugeordneter;
        this.flrsollmaterial = flrsollmaterial;
        this.flrapkommentar = flrapkommentar;
    }

    /** default constructor */
    public FLRLossollarbeitsplan() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public Integer getPersonal_i_id_zugeordneter() {
        return this.personal_i_id_zugeordneter;
    }

    public void setPersonal_i_id_zugeordneter(Integer personal_i_id_zugeordneter) {
        this.personal_i_id_zugeordneter = personal_i_id_zugeordneter;
    }

    public BigDecimal getL_ruestzeit() {
        return this.l_ruestzeit;
    }

    public void setL_ruestzeit(BigDecimal l_ruestzeit) {
        this.l_ruestzeit = l_ruestzeit;
    }

    public BigDecimal getL_stueckzeit() {
        return this.l_stueckzeit;
    }

    public void setL_stueckzeit(BigDecimal l_stueckzeit) {
        this.l_stueckzeit = l_stueckzeit;
    }

    public BigDecimal getN_gesamtzeit() {
        return this.n_gesamtzeit;
    }

    public void setN_gesamtzeit(BigDecimal n_gesamtzeit) {
        this.n_gesamtzeit = n_gesamtzeit;
    }

    public Integer getI_arbeitsgangsnummer() {
        return this.i_arbeitsgangsnummer;
    }

    public void setI_arbeitsgangsnummer(Integer i_arbeitsgangsnummer) {
        this.i_arbeitsgangsnummer = i_arbeitsgangsnummer;
    }

    public Integer getI_maschinenversatztage() {
        return this.i_maschinenversatztage;
    }

    public void setI_maschinenversatztage(Integer i_maschinenversatztage) {
        this.i_maschinenversatztage = i_maschinenversatztage;
    }

    public Integer getI_maschinenversatz_ms() {
        return this.i_maschinenversatz_ms;
    }

    public void setI_maschinenversatz_ms(Integer i_maschinenversatz_ms) {
        this.i_maschinenversatz_ms = i_maschinenversatz_ms;
    }

    public Integer getI_unterarbeitsgang() {
        return this.i_unterarbeitsgang;
    }

    public void setI_unterarbeitsgang(Integer i_unterarbeitsgang) {
        this.i_unterarbeitsgang = i_unterarbeitsgang;
    }

    public Short getB_nachtraeglich() {
        return this.b_nachtraeglich;
    }

    public void setB_nachtraeglich(Short b_nachtraeglich) {
        this.b_nachtraeglich = b_nachtraeglich;
    }

    public Short getB_nurmaschinenzeit() {
        return this.b_nurmaschinenzeit;
    }

    public void setB_nurmaschinenzeit(Short b_nurmaschinenzeit) {
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
    }

    public Short getB_fertig() {
        return this.b_fertig;
    }

    public void setB_fertig(Short b_fertig) {
        this.b_fertig = b_fertig;
    }

    public Double getF_fortschritt() {
        return this.f_fortschritt;
    }

    public void setF_fortschritt(Double f_fortschritt) {
        this.f_fortschritt = f_fortschritt;
    }

    public String getAgart_c_nr() {
        return this.agart_c_nr;
    }

    public void setAgart_c_nr(String agart_c_nr) {
        this.agart_c_nr = agart_c_nr;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Integer getI_aufspannung() {
        return this.i_aufspannung;
    }

    public void setI_aufspannung(Integer i_aufspannung) {
        this.i_aufspannung = i_aufspannung;
    }

    public Integer getI_reihung() {
        return this.i_reihung;
    }

    public void setI_reihung(Integer i_reihung) {
        this.i_reihung = i_reihung;
    }

    public Integer getLossollmaterial_i_id() {
        return this.lossollmaterial_i_id;
    }

    public void setLossollmaterial_i_id(Integer lossollmaterial_i_id) {
        this.lossollmaterial_i_id = lossollmaterial_i_id;
    }

    public Short getB_autoendebeigeht() {
        return this.b_autoendebeigeht;
    }

    public void setB_autoendebeigeht(Short b_autoendebeigeht) {
        this.b_autoendebeigeht = b_autoendebeigeht;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public FLRPersonal getFlrpersonal_zugeordneter() {
        return this.flrpersonal_zugeordneter;
    }

    public void setFlrpersonal_zugeordneter(FLRPersonal flrpersonal_zugeordneter) {
        this.flrpersonal_zugeordneter = flrpersonal_zugeordneter;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrsollmaterial() {
        return this.flrsollmaterial;
    }

    public void setFlrsollmaterial(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrsollmaterial) {
        this.flrsollmaterial = flrsollmaterial;
    }

    public FLRApkommentar getFlrapkommentar() {
        return this.flrapkommentar;
    }

    public void setFlrapkommentar(FLRApkommentar flrapkommentar) {
        this.flrapkommentar = flrapkommentar;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
