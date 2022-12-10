package com.lp.server.lieferschein.fastlanereader.generated;

import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPackstueck implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Long l_nummer;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer lieferschein_i_id;

    /** nullable persistent field */
    private Integer losablieferung_i_id;

    /** nullable persistent field */
    private Integer lieferscheinposition_i_id;

    /** nullable persistent field */
    private FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein;

    /** full constructor */
    public FLRPackstueck(Long l_nummer, Integer los_i_id, Integer lieferschein_i_id, Integer losablieferung_i_id, Integer lieferscheinposition_i_id, FLRLos flrlos, com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein) {
        this.l_nummer = l_nummer;
        this.los_i_id = los_i_id;
        this.lieferschein_i_id = lieferschein_i_id;
        this.losablieferung_i_id = losablieferung_i_id;
        this.lieferscheinposition_i_id = lieferscheinposition_i_id;
        this.flrlos = flrlos;
        this.flrlieferschein = flrlieferschein;
    }

    /** default constructor */
    public FLRPackstueck() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Long getL_nummer() {
        return this.l_nummer;
    }

    public void setL_nummer(Long l_nummer) {
        this.l_nummer = l_nummer;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getLieferschein_i_id() {
        return this.lieferschein_i_id;
    }

    public void setLieferschein_i_id(Integer lieferschein_i_id) {
        this.lieferschein_i_id = lieferschein_i_id;
    }

    public Integer getLosablieferung_i_id() {
        return this.losablieferung_i_id;
    }

    public void setLosablieferung_i_id(Integer losablieferung_i_id) {
        this.losablieferung_i_id = losablieferung_i_id;
    }

    public Integer getLieferscheinposition_i_id() {
        return this.lieferscheinposition_i_id;
    }

    public void setLieferscheinposition_i_id(Integer lieferscheinposition_i_id) {
        this.lieferscheinposition_i_id = lieferscheinposition_i_id;
    }

    public FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein getFlrlieferschein() {
        return this.flrlieferschein;
    }

    public void setFlrlieferschein(com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein) {
        this.flrlieferschein = flrlieferschein;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
