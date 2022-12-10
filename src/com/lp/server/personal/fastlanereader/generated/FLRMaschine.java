package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaschine implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_inventarnummer;

    /** nullable persistent field */
    private String c_seriennummer;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_identifikationsnr;

    /** nullable persistent field */
    private Short b_autoendebeigeht;

    /** nullable persistent field */
    private Short b_manuelle_bedienung;

    /** nullable persistent field */
    private Date t_kaufdatum;

    /** nullable persistent field */
    private Integer maschinengruppe_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe;

    /** persistent field */
    private Set maschinenzeitdatenset;

    /** full constructor */
    public FLRMaschine(String mandant_c_nr, String c_inventarnummer, String c_seriennummer, String c_bez, Short b_versteckt, String c_identifikationsnr, Short b_autoendebeigeht, Short b_manuelle_bedienung, Date t_kaufdatum, Integer maschinengruppe_i_id, com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe, Set maschinenzeitdatenset) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_inventarnummer = c_inventarnummer;
        this.c_seriennummer = c_seriennummer;
        this.c_bez = c_bez;
        this.b_versteckt = b_versteckt;
        this.c_identifikationsnr = c_identifikationsnr;
        this.b_autoendebeigeht = b_autoendebeigeht;
        this.b_manuelle_bedienung = b_manuelle_bedienung;
        this.t_kaufdatum = t_kaufdatum;
        this.maschinengruppe_i_id = maschinengruppe_i_id;
        this.flrmaschinengruppe = flrmaschinengruppe;
        this.maschinenzeitdatenset = maschinenzeitdatenset;
    }

    /** default constructor */
    public FLRMaschine() {
    }

    /** minimal constructor */
    public FLRMaschine(Set maschinenzeitdatenset) {
        this.maschinenzeitdatenset = maschinenzeitdatenset;
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

    public String getC_inventarnummer() {
        return this.c_inventarnummer;
    }

    public void setC_inventarnummer(String c_inventarnummer) {
        this.c_inventarnummer = c_inventarnummer;
    }

    public String getC_seriennummer() {
        return this.c_seriennummer;
    }

    public void setC_seriennummer(String c_seriennummer) {
        this.c_seriennummer = c_seriennummer;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_identifikationsnr() {
        return this.c_identifikationsnr;
    }

    public void setC_identifikationsnr(String c_identifikationsnr) {
        this.c_identifikationsnr = c_identifikationsnr;
    }

    public Short getB_autoendebeigeht() {
        return this.b_autoendebeigeht;
    }

    public void setB_autoendebeigeht(Short b_autoendebeigeht) {
        this.b_autoendebeigeht = b_autoendebeigeht;
    }

    public Short getB_manuelle_bedienung() {
        return this.b_manuelle_bedienung;
    }

    public void setB_manuelle_bedienung(Short b_manuelle_bedienung) {
        this.b_manuelle_bedienung = b_manuelle_bedienung;
    }

    public Date getT_kaufdatum() {
        return this.t_kaufdatum;
    }

    public void setT_kaufdatum(Date t_kaufdatum) {
        this.t_kaufdatum = t_kaufdatum;
    }

    public Integer getMaschinengruppe_i_id() {
        return this.maschinengruppe_i_id;
    }

    public void setMaschinengruppe_i_id(Integer maschinengruppe_i_id) {
        this.maschinengruppe_i_id = maschinengruppe_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe getFlrmaschinengruppe() {
        return this.flrmaschinengruppe;
    }

    public void setFlrmaschinengruppe(com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe) {
        this.flrmaschinengruppe = flrmaschinengruppe;
    }

    public Set getMaschinenzeitdatenset() {
        return this.maschinenzeitdatenset;
    }

    public void setMaschinenzeitdatenset(Set maschinenzeitdatenset) {
        this.maschinenzeitdatenset = maschinenzeitdatenset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
