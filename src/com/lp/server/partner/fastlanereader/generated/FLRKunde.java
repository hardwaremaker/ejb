package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKunde implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_abc;

    /** nullable persistent field */
    private Date t_liefersperream;

    /** nullable persistent field */
    private String c_hinweisintern;

    /** nullable persistent field */
    private String c_hinweisextern;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private Integer i_kundennummer;

    /** nullable persistent field */
    private String c_kurznr;

    /** nullable persistent field */
    private Integer konto_i_id_debitorenkonto;

    /** nullable persistent field */
    private Integer lager_i_id_abbuchungslager;

    /** nullable persistent field */
    private Integer i_lieferdauer;

    /** nullable persistent field */
    private Short b_istinteressent;

    /** nullable persistent field */
    private Short b_versteckterlieferant;

    /** nullable persistent field */
    private Short b_monatsrechnung;

    /** nullable persistent field */
    private Integer personal_i_id_bekommeprovision;

    /** nullable persistent field */
    private Integer vkpfartikelpreisliste_i_id_stdpreisliste;

    /** nullable persistent field */
    private BigDecimal n_kreditlimit;

    /** nullable persistent field */
    private String c_id_extern;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartnerRechnungsAdresse;

    /** nullable persistent field */
    private FLRFinanzKonto flrkonto;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** full constructor */
    public FLRKunde(String mandant_c_nr, String c_abc, Date t_liefersperream, String c_hinweisintern, String c_hinweisextern, String x_kommentar, String waehrung_c_nr, Integer i_kundennummer, String c_kurznr, Integer konto_i_id_debitorenkonto, Integer lager_i_id_abbuchungslager, Integer i_lieferdauer, Short b_istinteressent, Short b_versteckterlieferant, Short b_monatsrechnung, Integer personal_i_id_bekommeprovision, Integer vkpfartikelpreisliste_i_id_stdpreisliste, BigDecimal n_kreditlimit, String c_id_extern, Date t_aendern, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartnerRechnungsAdresse, FLRFinanzKonto flrkonto, FLRPersonal flrpersonal, FLRKostenstelle flrkostenstelle) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_abc = c_abc;
        this.t_liefersperream = t_liefersperream;
        this.c_hinweisintern = c_hinweisintern;
        this.c_hinweisextern = c_hinweisextern;
        this.x_kommentar = x_kommentar;
        this.waehrung_c_nr = waehrung_c_nr;
        this.i_kundennummer = i_kundennummer;
        this.c_kurznr = c_kurznr;
        this.konto_i_id_debitorenkonto = konto_i_id_debitorenkonto;
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
        this.i_lieferdauer = i_lieferdauer;
        this.b_istinteressent = b_istinteressent;
        this.b_versteckterlieferant = b_versteckterlieferant;
        this.b_monatsrechnung = b_monatsrechnung;
        this.personal_i_id_bekommeprovision = personal_i_id_bekommeprovision;
        this.vkpfartikelpreisliste_i_id_stdpreisliste = vkpfartikelpreisliste_i_id_stdpreisliste;
        this.n_kreditlimit = n_kreditlimit;
        this.c_id_extern = c_id_extern;
        this.t_aendern = t_aendern;
        this.flrpartner = flrpartner;
        this.flrpartnerRechnungsAdresse = flrpartnerRechnungsAdresse;
        this.flrkonto = flrkonto;
        this.flrpersonal = flrpersonal;
        this.flrkostenstelle = flrkostenstelle;
    }

    /** default constructor */
    public FLRKunde() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_abc() {
        return this.c_abc;
    }

    public void setC_abc(String c_abc) {
        this.c_abc = c_abc;
    }

    public Date getT_liefersperream() {
        return this.t_liefersperream;
    }

    public void setT_liefersperream(Date t_liefersperream) {
        this.t_liefersperream = t_liefersperream;
    }

    public String getC_hinweisintern() {
        return this.c_hinweisintern;
    }

    public void setC_hinweisintern(String c_hinweisintern) {
        this.c_hinweisintern = c_hinweisintern;
    }

    public String getC_hinweisextern() {
        return this.c_hinweisextern;
    }

    public void setC_hinweisextern(String c_hinweisextern) {
        this.c_hinweisextern = c_hinweisextern;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public Integer getI_kundennummer() {
        return this.i_kundennummer;
    }

    public void setI_kundennummer(Integer i_kundennummer) {
        this.i_kundennummer = i_kundennummer;
    }

    public String getC_kurznr() {
        return this.c_kurznr;
    }

    public void setC_kurznr(String c_kurznr) {
        this.c_kurznr = c_kurznr;
    }

    public Integer getKonto_i_id_debitorenkonto() {
        return this.konto_i_id_debitorenkonto;
    }

    public void setKonto_i_id_debitorenkonto(Integer konto_i_id_debitorenkonto) {
        this.konto_i_id_debitorenkonto = konto_i_id_debitorenkonto;
    }

    public Integer getLager_i_id_abbuchungslager() {
        return this.lager_i_id_abbuchungslager;
    }

    public void setLager_i_id_abbuchungslager(Integer lager_i_id_abbuchungslager) {
        this.lager_i_id_abbuchungslager = lager_i_id_abbuchungslager;
    }

    public Integer getI_lieferdauer() {
        return this.i_lieferdauer;
    }

    public void setI_lieferdauer(Integer i_lieferdauer) {
        this.i_lieferdauer = i_lieferdauer;
    }

    public Short getB_istinteressent() {
        return this.b_istinteressent;
    }

    public void setB_istinteressent(Short b_istinteressent) {
        this.b_istinteressent = b_istinteressent;
    }

    public Short getB_versteckterlieferant() {
        return this.b_versteckterlieferant;
    }

    public void setB_versteckterlieferant(Short b_versteckterlieferant) {
        this.b_versteckterlieferant = b_versteckterlieferant;
    }

    public Short getB_monatsrechnung() {
        return this.b_monatsrechnung;
    }

    public void setB_monatsrechnung(Short b_monatsrechnung) {
        this.b_monatsrechnung = b_monatsrechnung;
    }

    public Integer getPersonal_i_id_bekommeprovision() {
        return this.personal_i_id_bekommeprovision;
    }

    public void setPersonal_i_id_bekommeprovision(Integer personal_i_id_bekommeprovision) {
        this.personal_i_id_bekommeprovision = personal_i_id_bekommeprovision;
    }

    public Integer getVkpfartikelpreisliste_i_id_stdpreisliste() {
        return this.vkpfartikelpreisliste_i_id_stdpreisliste;
    }

    public void setVkpfartikelpreisliste_i_id_stdpreisliste(Integer vkpfartikelpreisliste_i_id_stdpreisliste) {
        this.vkpfartikelpreisliste_i_id_stdpreisliste = vkpfartikelpreisliste_i_id_stdpreisliste;
    }

    public BigDecimal getN_kreditlimit() {
        return this.n_kreditlimit;
    }

    public void setN_kreditlimit(BigDecimal n_kreditlimit) {
        this.n_kreditlimit = n_kreditlimit;
    }

    public String getC_id_extern() {
        return this.c_id_extern;
    }

    public void setC_id_extern(String c_id_extern) {
        this.c_id_extern = c_id_extern;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartnerRechnungsAdresse() {
        return this.flrpartnerRechnungsAdresse;
    }

    public void setFlrpartnerRechnungsAdresse(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartnerRechnungsAdresse) {
        this.flrpartnerRechnungsAdresse = flrpartnerRechnungsAdresse;
    }

    public FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
