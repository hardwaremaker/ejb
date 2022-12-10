package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBedarfsuebernahme implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private BigDecimal n_wunschmenge;

    /** nullable persistent field */
    private Short b_abgang;

    /** nullable persistent field */
    private Short b_zusaetzlich;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_wunschtermin;

    /** nullable persistent field */
    private Date t_verbucht_gedruckt;

    /** nullable persistent field */
    private Integer personal_i_id_verbucht_gedruckt;

    /** nullable persistent field */
    private Integer personal_i_id_anlegen;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String c_losnummer;

    /** nullable persistent field */
    private String c_artikelnummer;

    /** nullable persistent field */
    private String c_artikelbezeichnung;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_anlegen;

    /** full constructor */
    public FLRBedarfsuebernahme(Integer los_i_id, Integer artikel_i_id, BigDecimal n_wunschmenge, Short b_abgang, Short b_zusaetzlich, Date t_aendern, Date t_anlegen, Date t_wunschtermin, Date t_verbucht_gedruckt, Integer personal_i_id_verbucht_gedruckt, Integer personal_i_id_anlegen, Integer lossollmaterial_i_id, String status_c_nr, String c_losnummer, String c_artikelnummer, String c_artikelbezeichnung, String c_kommentar, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial, FLRPersonal flrpersonal_anlegen) {
        this.los_i_id = los_i_id;
        this.artikel_i_id = artikel_i_id;
        this.n_wunschmenge = n_wunschmenge;
        this.b_abgang = b_abgang;
        this.b_zusaetzlich = b_zusaetzlich;
        this.t_aendern = t_aendern;
        this.t_anlegen = t_anlegen;
        this.t_wunschtermin = t_wunschtermin;
        this.t_verbucht_gedruckt = t_verbucht_gedruckt;
        this.personal_i_id_verbucht_gedruckt = personal_i_id_verbucht_gedruckt;
        this.personal_i_id_anlegen = personal_i_id_anlegen;
        this.lossollmaterial_i_id = lossollmaterial_i_id;
        this.status_c_nr = status_c_nr;
        this.c_losnummer = c_losnummer;
        this.c_artikelnummer = c_artikelnummer;
        this.c_artikelbezeichnung = c_artikelbezeichnung;
        this.c_kommentar = c_kommentar;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.flrlos = flrlos;
        this.flrlossollmaterial = flrlossollmaterial;
        this.flrpersonal_anlegen = flrpersonal_anlegen;
    }

    /** default constructor */
    public FLRBedarfsuebernahme() {
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

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public BigDecimal getN_wunschmenge() {
        return this.n_wunschmenge;
    }

    public void setN_wunschmenge(BigDecimal n_wunschmenge) {
        this.n_wunschmenge = n_wunschmenge;
    }

    public Short getB_abgang() {
        return this.b_abgang;
    }

    public void setB_abgang(Short b_abgang) {
        this.b_abgang = b_abgang;
    }

    public Short getB_zusaetzlich() {
        return this.b_zusaetzlich;
    }

    public void setB_zusaetzlich(Short b_zusaetzlich) {
        this.b_zusaetzlich = b_zusaetzlich;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_wunschtermin() {
        return this.t_wunschtermin;
    }

    public void setT_wunschtermin(Date t_wunschtermin) {
        this.t_wunschtermin = t_wunschtermin;
    }

    public Date getT_verbucht_gedruckt() {
        return this.t_verbucht_gedruckt;
    }

    public void setT_verbucht_gedruckt(Date t_verbucht_gedruckt) {
        this.t_verbucht_gedruckt = t_verbucht_gedruckt;
    }

    public Integer getPersonal_i_id_verbucht_gedruckt() {
        return this.personal_i_id_verbucht_gedruckt;
    }

    public void setPersonal_i_id_verbucht_gedruckt(Integer personal_i_id_verbucht_gedruckt) {
        this.personal_i_id_verbucht_gedruckt = personal_i_id_verbucht_gedruckt;
    }

    public Integer getPersonal_i_id_anlegen() {
        return this.personal_i_id_anlegen;
    }

    public void setPersonal_i_id_anlegen(Integer personal_i_id_anlegen) {
        this.personal_i_id_anlegen = personal_i_id_anlegen;
    }

    public Integer getLossollmaterial_i_id() {
        return this.lossollmaterial_i_id;
    }

    public void setLossollmaterial_i_id(Integer lossollmaterial_i_id) {
        this.lossollmaterial_i_id = lossollmaterial_i_id;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getC_losnummer() {
        return this.c_losnummer;
    }

    public void setC_losnummer(String c_losnummer) {
        this.c_losnummer = c_losnummer;
    }

    public String getC_artikelnummer() {
        return this.c_artikelnummer;
    }

    public void setC_artikelnummer(String c_artikelnummer) {
        this.c_artikelnummer = c_artikelnummer;
    }

    public String getC_artikelbezeichnung() {
        return this.c_artikelbezeichnung;
    }

    public void setC_artikelbezeichnung(String c_artikelbezeichnung) {
        this.c_artikelbezeichnung = c_artikelbezeichnung;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial() {
        return this.flrlossollmaterial;
    }

    public void setFlrlossollmaterial(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial) {
        this.flrlossollmaterial = flrlossollmaterial;
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
