package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRErsatztypen implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id_ersatz;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel_ersatz;

    /** full constructor */
    public FLRErsatztypen(Integer artikel_i_id, Integer artikel_i_id_ersatz, Integer i_sort, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel_ersatz) {
        this.artikel_i_id = artikel_i_id;
        this.artikel_i_id_ersatz = artikel_i_id_ersatz;
        this.i_sort = i_sort;
        this.flrartikel = flrartikel;
        this.flrartikel_ersatz = flrartikel_ersatz;
    }

    /** default constructor */
    public FLRErsatztypen() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getArtikel_i_id_ersatz() {
        return this.artikel_i_id_ersatz;
    }

    public void setArtikel_i_id_ersatz(Integer artikel_i_id_ersatz) {
        this.artikel_i_id_ersatz = artikel_i_id_ersatz;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel_ersatz() {
        return this.flrartikel_ersatz;
    }

    public void setFlrartikel_ersatz(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel_ersatz) {
        this.flrartikel_ersatz = flrartikel_ersatz;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
