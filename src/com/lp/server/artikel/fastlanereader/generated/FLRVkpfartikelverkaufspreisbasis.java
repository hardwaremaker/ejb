package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVkpfartikelverkaufspreisbasis implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Date t_verkaufspreisbasisgueltigab;

    /** nullable persistent field */
    private BigDecimal n_verkaufspreisbasis;

    /** full constructor */
    public FLRVkpfartikelverkaufspreisbasis(Integer artikel_i_id, String mandant_c_nr, Date t_verkaufspreisbasisgueltigab, BigDecimal n_verkaufspreisbasis) {
        this.artikel_i_id = artikel_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.t_verkaufspreisbasisgueltigab = t_verkaufspreisbasisgueltigab;
        this.n_verkaufspreisbasis = n_verkaufspreisbasis;
    }

    /** default constructor */
    public FLRVkpfartikelverkaufspreisbasis() {
    }

    /** minimal constructor */
    public FLRVkpfartikelverkaufspreisbasis(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
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

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Date getT_verkaufspreisbasisgueltigab() {
        return this.t_verkaufspreisbasisgueltigab;
    }

    public void setT_verkaufspreisbasisgueltigab(Date t_verkaufspreisbasisgueltigab) {
        this.t_verkaufspreisbasisgueltigab = t_verkaufspreisbasisgueltigab;
    }

    public BigDecimal getN_verkaufspreisbasis() {
        return this.n_verkaufspreisbasis;
    }

    public void setN_verkaufspreisbasis(BigDecimal n_verkaufspreisbasis) {
        this.n_verkaufspreisbasis = n_verkaufspreisbasis;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
