package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPositionenSichtAuftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_einkaufpreis;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private BigDecimal n_offene_menge;

    /** nullable persistent field */
    private String positionart_c_nr;

    /** nullable persistent field */
    private String auftragpositionstatus_c_nr;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_bruttogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** full constructor */
    public FLRPositionenSichtAuftrag(Integer i_sort, String c_bez, String c_zbez, BigDecimal n_einkaufpreis, String x_textinhalt, Integer auftrag_i_id, BigDecimal n_offene_menge, String positionart_c_nr, String auftragpositionstatus_c_nr, Date t_uebersteuerterliefertermin, Integer position_i_id, String typ_c_nr, Integer position_i_id_artikelset, BigDecimal n_nettogesamtpreis, BigDecimal n_bruttogesamtpreis, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte, BigDecimal n_menge, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.i_sort = i_sort;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_einkaufpreis = n_einkaufpreis;
        this.x_textinhalt = x_textinhalt;
        this.auftrag_i_id = auftrag_i_id;
        this.n_offene_menge = n_offene_menge;
        this.positionart_c_nr = positionart_c_nr;
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.position_i_id = position_i_id;
        this.typ_c_nr = typ_c_nr;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
        this.n_menge = n_menge;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrauftrag = flrauftrag;
    }

    /** default constructor */
    public FLRPositionenSichtAuftrag() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public BigDecimal getN_einkaufpreis() {
        return this.n_einkaufpreis;
    }

    public void setN_einkaufpreis(BigDecimal n_einkaufpreis) {
        this.n_einkaufpreis = n_einkaufpreis;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public BigDecimal getN_offene_menge() {
        return this.n_offene_menge;
    }

    public void setN_offene_menge(BigDecimal n_offene_menge) {
        this.n_offene_menge = n_offene_menge;
    }

    public String getPositionart_c_nr() {
        return this.positionart_c_nr;
    }

    public void setPositionart_c_nr(String positionart_c_nr) {
        this.positionart_c_nr = positionart_c_nr;
    }

    public String getAuftragpositionstatus_c_nr() {
        return this.auftragpositionstatus_c_nr;
    }

    public void setAuftragpositionstatus_c_nr(String auftragpositionstatus_c_nr) {
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public BigDecimal getN_bruttogesamtpreis() {
        return this.n_bruttogesamtpreis;
    }

    public void setN_bruttogesamtpreis(BigDecimal n_bruttogesamtpreis) {
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() {
        return this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlagminusrabatte(BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte) {
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMediastandard getFlrmediastandard() {
        return this.flrmediastandard;
    }

    public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
        this.flrmediastandard = flrmediastandard;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
