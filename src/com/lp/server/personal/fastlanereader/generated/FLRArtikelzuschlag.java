package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelzuschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_gueltigab;

    /** nullable persistent field */
    private BigDecimal n_zuschlag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLRArtikelzuschlag(Date t_gueltigab, BigDecimal n_zuschlag, FLRArtikel flrartikel) {
        this.t_gueltigab = t_gueltigab;
        this.n_zuschlag = n_zuschlag;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRArtikelzuschlag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public BigDecimal getN_zuschlag() {
        return this.n_zuschlag;
    }

    public void setN_zuschlag(BigDecimal n_zuschlag) {
        this.n_zuschlag = n_zuschlag;
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
