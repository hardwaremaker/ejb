package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLiefermengen implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_lstext;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLRLiefermengen(String c_lstext, Date t_datum, Integer artikel_i_id, Integer kunde_i_id_lieferadresse, BigDecimal n_menge, FLRArtikel flrartikel) {
        this.c_lstext = c_lstext;
        this.t_datum = t_datum;
        this.artikel_i_id = artikel_i_id;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.n_menge = n_menge;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRLiefermengen() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_lstext() {
        return this.c_lstext;
    }

    public void setC_lstext(String c_lstext) {
        this.c_lstext = c_lstext;
    }

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getKunde_i_id_lieferadresse() {
        return this.kunde_i_id_lieferadresse;
    }

    public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
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
