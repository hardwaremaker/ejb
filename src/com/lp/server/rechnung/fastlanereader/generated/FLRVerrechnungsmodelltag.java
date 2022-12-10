package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.personal.fastlanereader.generated.FLRTagesart;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerrechnungsmodelltag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id_gebucht;

    /** nullable persistent field */
    private Integer artikel_i_id_zuverrchnen;

    /** nullable persistent field */
    private Integer verrechnungsmodell_i_id;

    /** nullable persistent field */
    private Date u_dauer_ab;

    /** nullable persistent field */
    private Date u_zeitraum_von;

    /** nullable persistent field */
    private Date u_zeitraum_bis;

    /** nullable persistent field */
    private Short b_endedestages;

    /** nullable persistent field */
    private FLRArtikel flrartikel_gebucht;

    /** nullable persistent field */
    private FLRArtikel flrartikel_zuverrechnen;

    /** nullable persistent field */
    private FLRTagesart flrtagesart;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRVerrechnungsmodell flrverrechnungsmodell;

    /** full constructor */
    public FLRVerrechnungsmodelltag(Integer artikel_i_id_gebucht, Integer artikel_i_id_zuverrchnen, Integer verrechnungsmodell_i_id, Date u_dauer_ab, Date u_zeitraum_von, Date u_zeitraum_bis, Short b_endedestages, FLRArtikel flrartikel_gebucht, FLRArtikel flrartikel_zuverrechnen, FLRTagesart flrtagesart, com.lp.server.rechnung.fastlanereader.generated.FLRVerrechnungsmodell flrverrechnungsmodell) {
        this.artikel_i_id_gebucht = artikel_i_id_gebucht;
        this.artikel_i_id_zuverrchnen = artikel_i_id_zuverrchnen;
        this.verrechnungsmodell_i_id = verrechnungsmodell_i_id;
        this.u_dauer_ab = u_dauer_ab;
        this.u_zeitraum_von = u_zeitraum_von;
        this.u_zeitraum_bis = u_zeitraum_bis;
        this.b_endedestages = b_endedestages;
        this.flrartikel_gebucht = flrartikel_gebucht;
        this.flrartikel_zuverrechnen = flrartikel_zuverrechnen;
        this.flrtagesart = flrtagesart;
        this.flrverrechnungsmodell = flrverrechnungsmodell;
    }

    /** default constructor */
    public FLRVerrechnungsmodelltag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id_gebucht() {
        return this.artikel_i_id_gebucht;
    }

    public void setArtikel_i_id_gebucht(Integer artikel_i_id_gebucht) {
        this.artikel_i_id_gebucht = artikel_i_id_gebucht;
    }

    public Integer getArtikel_i_id_zuverrchnen() {
        return this.artikel_i_id_zuverrchnen;
    }

    public void setArtikel_i_id_zuverrchnen(Integer artikel_i_id_zuverrchnen) {
        this.artikel_i_id_zuverrchnen = artikel_i_id_zuverrchnen;
    }

    public Integer getVerrechnungsmodell_i_id() {
        return this.verrechnungsmodell_i_id;
    }

    public void setVerrechnungsmodell_i_id(Integer verrechnungsmodell_i_id) {
        this.verrechnungsmodell_i_id = verrechnungsmodell_i_id;
    }

    public Date getU_dauer_ab() {
        return this.u_dauer_ab;
    }

    public void setU_dauer_ab(Date u_dauer_ab) {
        this.u_dauer_ab = u_dauer_ab;
    }

    public Date getU_zeitraum_von() {
        return this.u_zeitraum_von;
    }

    public void setU_zeitraum_von(Date u_zeitraum_von) {
        this.u_zeitraum_von = u_zeitraum_von;
    }

    public Date getU_zeitraum_bis() {
        return this.u_zeitraum_bis;
    }

    public void setU_zeitraum_bis(Date u_zeitraum_bis) {
        this.u_zeitraum_bis = u_zeitraum_bis;
    }

    public Short getB_endedestages() {
        return this.b_endedestages;
    }

    public void setB_endedestages(Short b_endedestages) {
        this.b_endedestages = b_endedestages;
    }

    public FLRArtikel getFlrartikel_gebucht() {
        return this.flrartikel_gebucht;
    }

    public void setFlrartikel_gebucht(FLRArtikel flrartikel_gebucht) {
        this.flrartikel_gebucht = flrartikel_gebucht;
    }

    public FLRArtikel getFlrartikel_zuverrechnen() {
        return this.flrartikel_zuverrechnen;
    }

    public void setFlrartikel_zuverrechnen(FLRArtikel flrartikel_zuverrechnen) {
        this.flrartikel_zuverrechnen = flrartikel_zuverrechnen;
    }

    public FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRVerrechnungsmodell getFlrverrechnungsmodell() {
        return this.flrverrechnungsmodell;
    }

    public void setFlrverrechnungsmodell(com.lp.server.rechnung.fastlanereader.generated.FLRVerrechnungsmodell flrverrechnungsmodell) {
        this.flrverrechnungsmodell = flrverrechnungsmodell;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
