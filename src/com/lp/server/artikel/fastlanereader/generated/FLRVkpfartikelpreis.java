package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVkpfartikelpreis implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer vkpfartikelpreisliste_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Date t_preisgueltigab;

    /** nullable persistent field */
    private BigDecimal n_artikelstandardrabattsatz;

    /** nullable persistent field */
    private BigDecimal n_artikelfixpreis;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrpreisliste;

    /** full constructor */
    public FLRVkpfartikelpreis(Integer vkpfartikelpreisliste_i_id, Integer artikel_i_id, Date t_preisgueltigab, BigDecimal n_artikelstandardrabattsatz, BigDecimal n_artikelfixpreis, com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrpreisliste) {
        this.vkpfartikelpreisliste_i_id = vkpfartikelpreisliste_i_id;
        this.artikel_i_id = artikel_i_id;
        this.t_preisgueltigab = t_preisgueltigab;
        this.n_artikelstandardrabattsatz = n_artikelstandardrabattsatz;
        this.n_artikelfixpreis = n_artikelfixpreis;
        this.flrpreisliste = flrpreisliste;
    }

    /** default constructor */
    public FLRVkpfartikelpreis() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getVkpfartikelpreisliste_i_id() {
        return this.vkpfartikelpreisliste_i_id;
    }

    public void setVkpfartikelpreisliste_i_id(Integer vkpfartikelpreisliste_i_id) {
        this.vkpfartikelpreisliste_i_id = vkpfartikelpreisliste_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Date getT_preisgueltigab() {
        return this.t_preisgueltigab;
    }

    public void setT_preisgueltigab(Date t_preisgueltigab) {
        this.t_preisgueltigab = t_preisgueltigab;
    }

    public BigDecimal getN_artikelstandardrabattsatz() {
        return this.n_artikelstandardrabattsatz;
    }

    public void setN_artikelstandardrabattsatz(BigDecimal n_artikelstandardrabattsatz) {
        this.n_artikelstandardrabattsatz = n_artikelstandardrabattsatz;
    }

    public BigDecimal getN_artikelfixpreis() {
        return this.n_artikelfixpreis;
    }

    public void setN_artikelfixpreis(BigDecimal n_artikelfixpreis) {
        this.n_artikelfixpreis = n_artikelfixpreis;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste getFlrpreisliste() {
        return this.flrpreisliste;
    }

    public void setFlrpreisliste(com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrpreisliste) {
        this.flrpreisliste = flrpreisliste;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
