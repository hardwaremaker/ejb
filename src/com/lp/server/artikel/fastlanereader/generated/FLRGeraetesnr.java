package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGeraetesnr implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_snr;

    /** nullable persistent field */
    private Integer i_id_buchung;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** full constructor */
    public FLRGeraetesnr(String c_snr, Integer i_id_buchung, Integer artikel_i_id, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.c_snr = c_snr;
        this.i_id_buchung = i_id_buchung;
        this.artikel_i_id = artikel_i_id;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRGeraetesnr() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_snr() {
        return this.c_snr;
    }

    public void setC_snr(String c_snr) {
        this.c_snr = c_snr;
    }

    public Integer getI_id_buchung() {
        return this.i_id_buchung;
    }

    public void setI_id_buchung(Integer i_id_buchung) {
        this.i_id_buchung = i_id_buchung;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
