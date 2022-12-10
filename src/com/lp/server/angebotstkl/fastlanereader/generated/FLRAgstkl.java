package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstkl implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zeichnungsnummer;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id_kunde;

    /** nullable persistent field */
    private Short b_vorlage;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** persistent field */
    private Set angebotspositionen;

    /** full constructor */
    public FLRAgstkl(String c_nr, String mandant_c_nr, String belegart_c_nr, String c_bez, String c_zeichnungsnummer, String waehrung_c_nr, Date t_belegdatum, Integer projekt_i_id, Integer ansprechpartner_i_id_kunde, Short b_vorlage, FLRKunde flrkunde, Set angebotspositionen) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.belegart_c_nr = belegart_c_nr;
        this.c_bez = c_bez;
        this.c_zeichnungsnummer = c_zeichnungsnummer;
        this.waehrung_c_nr = waehrung_c_nr;
        this.t_belegdatum = t_belegdatum;
        this.projekt_i_id = projekt_i_id;
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
        this.b_vorlage = b_vorlage;
        this.flrkunde = flrkunde;
        this.angebotspositionen = angebotspositionen;
    }

    /** default constructor */
    public FLRAgstkl() {
    }

    /** minimal constructor */
    public FLRAgstkl(Set angebotspositionen) {
        this.angebotspositionen = angebotspositionen;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zeichnungsnummer() {
        return this.c_zeichnungsnummer;
    }

    public void setC_zeichnungsnummer(String c_zeichnungsnummer) {
        this.c_zeichnungsnummer = c_zeichnungsnummer;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getAnsprechpartner_i_id_kunde() {
        return this.ansprechpartner_i_id_kunde;
    }

    public void setAnsprechpartner_i_id_kunde(Integer ansprechpartner_i_id_kunde) {
        this.ansprechpartner_i_id_kunde = ansprechpartner_i_id_kunde;
    }

    public Short getB_vorlage() {
        return this.b_vorlage;
    }

    public void setB_vorlage(Short b_vorlage) {
        this.b_vorlage = b_vorlage;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public Set getAngebotspositionen() {
        return this.angebotspositionen;
    }

    public void setAngebotspositionen(Set angebotspositionen) {
        this.angebotspositionen = angebotspositionen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
