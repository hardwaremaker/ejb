package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.stueckliste.service.FormelArtikel;
import com.lp.service.plscript.ScriptReportLogging;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStuecklisteposition implements Serializable {
	private static final long serialVersionUID = 1818733446843100693L;

	public BigDecimal bdZielmenge=null;
	
	public ScriptReportLogging reportLogging;
	
	/**
	 * Die vom Formel-Anwender gesetzten Attribute landen hier
	 * und koennem beim Erzeugen der Produktionsst&uuml;ckliste
	 * fuer den Artikel der St&uuml;cklistenposition verwendet werden.
	 */
	public FormelArtikel formelArtikel = new FormelArtikel();
	
	/**
	 * Die vom Formel-Anwender gesetzten Attribute landen hier 
	 * und k&ouml;nnen beim Erzeugen der Produktionsst&uuml;ckliste
	 * f&uuml;r den Artikel der &uuml;bergeordneten (Kopf)
	 * St&uuml;ckliste verwendet werden.
	 */
	public FormelArtikel formelUebergeordneterArtikel = new FormelArtikel();
	
	public FLRKunde formelFlrKunde;
    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_kalkpreis;

    /** nullable persistent field */
    private String c_position;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Integer i_lfdnummer;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private Double f_dimension1;

    /** nullable persistent field */
    private Double f_dimension2;

    /** nullable persistent field */
    private Double f_dimension3;

    /** nullable persistent field */
    private Short b_mitdrucken;

    /** nullable persistent field */
    private Short b_ruestmenge;

    /** nullable persistent field */
    private Short b_initial;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private String x_formel;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRMontageart flrmontageart;

    /** persistent field */
    private Set posersatzset;

    /** full constructor */
    public FLRStuecklisteposition(Integer i_sort, BigDecimal n_menge, BigDecimal n_kalkpreis, String c_position, String c_kommentar, Integer i_lfdnummer, String einheit_c_nr, Double f_dimension1, Double f_dimension2, Double f_dimension3, Short b_mitdrucken, Short b_ruestmenge, Short b_initial, Integer stueckliste_i_id, String x_formel, com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste, FLRArtikelliste flrartikel, com.lp.server.stueckliste.fastlanereader.generated.FLRMontageart flrmontageart, Set posersatzset) {
        this.i_sort = i_sort;
        this.n_menge = n_menge;
        this.n_kalkpreis = n_kalkpreis;
        this.c_position = c_position;
        this.c_kommentar = c_kommentar;
        this.i_lfdnummer = i_lfdnummer;
        this.einheit_c_nr = einheit_c_nr;
        this.f_dimension1 = f_dimension1;
        this.f_dimension2 = f_dimension2;
        this.f_dimension3 = f_dimension3;
        this.b_mitdrucken = b_mitdrucken;
        this.b_ruestmenge = b_ruestmenge;
        this.b_initial = b_initial;
        this.stueckliste_i_id = stueckliste_i_id;
        this.x_formel = x_formel;
        this.flrstueckliste = flrstueckliste;
        this.flrartikel = flrartikel;
        this.flrmontageart = flrmontageart;
        this.posersatzset = posersatzset;
    }

    /** default constructor */
    public FLRStuecklisteposition() {
    }

    /** minimal constructor */
    public FLRStuecklisteposition(Set posersatzset) {
        this.posersatzset = posersatzset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_kalkpreis() {
        return this.n_kalkpreis;
    }

    public void setN_kalkpreis(BigDecimal n_kalkpreis) {
        this.n_kalkpreis = n_kalkpreis;
    }

    public String getC_position() {
        return this.c_position;
    }

    public void setC_position(String c_position) {
        this.c_position = c_position;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Integer getI_lfdnummer() {
        return this.i_lfdnummer;
    }

    public void setI_lfdnummer(Integer i_lfdnummer) {
        this.i_lfdnummer = i_lfdnummer;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public Double getF_dimension1() {
        return this.f_dimension1;
    }

    public void setF_dimension1(Double f_dimension1) {
        this.f_dimension1 = f_dimension1;
    }

    public Double getF_dimension2() {
        return this.f_dimension2;
    }

    public void setF_dimension2(Double f_dimension2) {
        this.f_dimension2 = f_dimension2;
    }

    public Double getF_dimension3() {
        return this.f_dimension3;
    }

    public void setF_dimension3(Double f_dimension3) {
        this.f_dimension3 = f_dimension3;
    }

    public Short getB_mitdrucken() {
        return this.b_mitdrucken;
    }

    public void setB_mitdrucken(Short b_mitdrucken) {
        this.b_mitdrucken = b_mitdrucken;
    }

    public Short getB_ruestmenge() {
        return this.b_ruestmenge;
    }

    public void setB_ruestmenge(Short b_ruestmenge) {
        this.b_ruestmenge = b_ruestmenge;
    }

    public Short getB_initial() {
        return this.b_initial;
    }

    public void setB_initial(Short b_initial) {
        this.b_initial = b_initial;
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

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRMontageart getFlrmontageart() {
        return this.flrmontageart;
    }

    public void setFlrmontageart(com.lp.server.stueckliste.fastlanereader.generated.FLRMontageart flrmontageart) {
        this.flrmontageart = flrmontageart;
    }

    public Set getPosersatzset() {
        return this.posersatzset;
    }

    public void setPosersatzset(Set posersatzset) {
        this.posersatzset = posersatzset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
