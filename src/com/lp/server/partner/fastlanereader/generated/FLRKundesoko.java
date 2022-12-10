package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundesoko implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Short b_wirkt_nicht_fuer_preisfindung;

    /** nullable persistent field */
    private String c_kundeartikelnummer;

    /** nullable persistent field */
    private Integer artgru_i_id;

    /** nullable persistent field */
    private Date t_preisgueltigab;

    /** nullable persistent field */
    private Date t_preisgueltigbis;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRArtikelgruppe flrartikelgruppe;

    /** full constructor */
    public FLRKundesoko(Integer kunde_i_id, Integer artikel_i_id, Short b_wirkt_nicht_fuer_preisfindung, String c_kundeartikelnummer, Integer artgru_i_id, Date t_preisgueltigab, Date t_preisgueltigbis, FLRArtikelliste flrartikel, com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde, FLRArtikelgruppe flrartikelgruppe) {
        this.kunde_i_id = kunde_i_id;
        this.artikel_i_id = artikel_i_id;
        this.b_wirkt_nicht_fuer_preisfindung = b_wirkt_nicht_fuer_preisfindung;
        this.c_kundeartikelnummer = c_kundeartikelnummer;
        this.artgru_i_id = artgru_i_id;
        this.t_preisgueltigab = t_preisgueltigab;
        this.t_preisgueltigbis = t_preisgueltigbis;
        this.flrartikel = flrartikel;
        this.flrkunde = flrkunde;
        this.flrartikelgruppe = flrartikelgruppe;
    }

    /** default constructor */
    public FLRKundesoko() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Short getB_wirkt_nicht_fuer_preisfindung() {
        return this.b_wirkt_nicht_fuer_preisfindung;
    }

    public void setB_wirkt_nicht_fuer_preisfindung(Short b_wirkt_nicht_fuer_preisfindung) {
        this.b_wirkt_nicht_fuer_preisfindung = b_wirkt_nicht_fuer_preisfindung;
    }

    public String getC_kundeartikelnummer() {
        return this.c_kundeartikelnummer;
    }

    public void setC_kundeartikelnummer(String c_kundeartikelnummer) {
        this.c_kundeartikelnummer = c_kundeartikelnummer;
    }

    public Integer getArtgru_i_id() {
        return this.artgru_i_id;
    }

    public void setArtgru_i_id(Integer artgru_i_id) {
        this.artgru_i_id = artgru_i_id;
    }

    public Date getT_preisgueltigab() {
        return this.t_preisgueltigab;
    }

    public void setT_preisgueltigab(Date t_preisgueltigab) {
        this.t_preisgueltigab = t_preisgueltigab;
    }

    public Date getT_preisgueltigbis() {
        return this.t_preisgueltigbis;
    }

    public void setT_preisgueltigbis(Date t_preisgueltigbis) {
        this.t_preisgueltigbis = t_preisgueltigbis;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRArtikelgruppe getFlrartikelgruppe() {
        return this.flrartikelgruppe;
    }

    public void setFlrartikelgruppe(FLRArtikelgruppe flrartikelgruppe) {
        this.flrartikelgruppe = flrartikelgruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
