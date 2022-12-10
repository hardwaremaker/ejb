package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjekttaetigkeit implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLRProjekttaetigkeit(Integer projekt_i_id, Integer artikel_i_id, com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt, FLRArtikel flrartikel) {
        this.projekt_i_id = projekt_i_id;
        this.artikel_i_id = artikel_i_id;
        this.flrprojekt = flrprojekt;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRProjekttaetigkeit() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
