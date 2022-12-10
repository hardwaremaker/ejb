package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRVerleih;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String positionart_c_nr;

    /** nullable persistent field */
    private String auftragpositionstatus_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private BigDecimal n_offenerahmenmenge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_bruttogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer auftragposition_i_id_rahmenposition;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private Integer mwstsatz_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Short b_gesehen;

    /** nullable persistent field */
    private Short b_hvmauebertragen;

    /** nullable persistent field */
    private Integer zwsvonposition_i_id;

    /** nullable persistent field */
    private Integer zwsbisposition_i_id;

    /** nullable persistent field */
    private BigDecimal zwsnettosumme;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRVerleih flrverleih;

    /** full constructor */
    public FLRAuftragposition(Integer i_sort, String positionart_c_nr, String auftragpositionstatus_c_nr, String c_bez, String c_zbez, BigDecimal n_menge, BigDecimal n_offenemenge, BigDecimal n_offenerahmenmenge, String einheit_c_nr, BigDecimal n_nettogesamtpreis, BigDecimal n_bruttogesamtpreis, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte, String x_textinhalt, Integer auftragposition_i_id_rahmenposition, Integer position_i_id, Integer position_i_id_artikelset, Integer mwstsatz_i_id, String typ_c_nr, Date t_uebersteuerterliefertermin, Short b_gesehen, Short b_hvmauebertragen, Integer zwsvonposition_i_id, Integer zwsbisposition_i_id, BigDecimal zwsnettosumme, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, FLRVerleih flrverleih) {
        this.i_sort = i_sort;
        this.positionart_c_nr = positionart_c_nr;
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_menge = n_menge;
        this.n_offenemenge = n_offenemenge;
        this.n_offenerahmenmenge = n_offenerahmenmenge;
        this.einheit_c_nr = einheit_c_nr;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
        this.x_textinhalt = x_textinhalt;
        this.auftragposition_i_id_rahmenposition = auftragposition_i_id_rahmenposition;
        this.position_i_id = position_i_id;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.mwstsatz_i_id = mwstsatz_i_id;
        this.typ_c_nr = typ_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.b_gesehen = b_gesehen;
        this.b_hvmauebertragen = b_hvmauebertragen;
        this.zwsvonposition_i_id = zwsvonposition_i_id;
        this.zwsbisposition_i_id = zwsbisposition_i_id;
        this.zwsnettosumme = zwsnettosumme;
        this.flrauftrag = flrauftrag;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrverleih = flrverleih;
    }

    /** default constructor */
    public FLRAuftragposition() {
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

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public BigDecimal getN_offenerahmenmenge() {
        return this.n_offenerahmenmenge;
    }

    public void setN_offenerahmenmenge(BigDecimal n_offenerahmenmenge) {
        this.n_offenerahmenmenge = n_offenerahmenmenge;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
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

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getAuftragposition_i_id_rahmenposition() {
        return this.auftragposition_i_id_rahmenposition;
    }

    public void setAuftragposition_i_id_rahmenposition(Integer auftragposition_i_id_rahmenposition) {
        this.auftragposition_i_id_rahmenposition = auftragposition_i_id_rahmenposition;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public Integer getMwstsatz_i_id() {
        return this.mwstsatz_i_id;
    }

    public void setMwstsatz_i_id(Integer mwstsatz_i_id) {
        this.mwstsatz_i_id = mwstsatz_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Short getB_gesehen() {
        return this.b_gesehen;
    }

    public void setB_gesehen(Short b_gesehen) {
        this.b_gesehen = b_gesehen;
    }

    public Short getB_hvmauebertragen() {
        return this.b_hvmauebertragen;
    }

    public void setB_hvmauebertragen(Short b_hvmauebertragen) {
        this.b_hvmauebertragen = b_hvmauebertragen;
    }

    public Integer getZwsvonposition_i_id() {
        return this.zwsvonposition_i_id;
    }

    public void setZwsvonposition_i_id(Integer zwsvonposition_i_id) {
        this.zwsvonposition_i_id = zwsvonposition_i_id;
    }

    public Integer getZwsbisposition_i_id() {
        return this.zwsbisposition_i_id;
    }

    public void setZwsbisposition_i_id(Integer zwsbisposition_i_id) {
        this.zwsbisposition_i_id = zwsbisposition_i_id;
    }

    public BigDecimal getZwsnettosumme() {
        return this.zwsnettosumme;
    }

    public void setZwsnettosumme(BigDecimal zwsnettosumme) {
        this.zwsnettosumme = zwsnettosumme;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
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

    public FLRVerleih getFlrverleih() {
        return this.flrverleih;
    }

    public void setFlrverleih(FLRVerleih flrverleih) {
        this.flrverleih = flrverleih;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
