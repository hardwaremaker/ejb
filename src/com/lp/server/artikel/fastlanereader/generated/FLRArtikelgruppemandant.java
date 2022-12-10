package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelgruppemandant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artgru_i_id;

    /** nullable persistent field */
    private Integer konto_i_id;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private FLRFinanzKonto flrkonto;

    /** full constructor */
    public FLRArtikelgruppemandant(Integer artgru_i_id, Integer konto_i_id, String mandant_c_nr, FLRFinanzKonto flrkonto) {
        this.artgru_i_id = artgru_i_id;
        this.konto_i_id = konto_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.flrkonto = flrkonto;
    }

    /** default constructor */
    public FLRArtikelgruppemandant() {
    }

    /** minimal constructor */
    public FLRArtikelgruppemandant(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtgru_i_id() {
        return this.artgru_i_id;
    }

    public void setArtgru_i_id(Integer artgru_i_id) {
        this.artgru_i_id = artgru_i_id;
    }

    public Integer getKonto_i_id() {
        return this.konto_i_id;
    }

    public void setKonto_i_id(Integer konto_i_id) {
        this.konto_i_id = konto_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
