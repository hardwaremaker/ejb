package com.lp.server.bestellung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBSMahnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer mahnlauf_i_id;

    /** nullable persistent field */
    private Integer mahnstufe_i_id;

    /** nullable persistent field */
    private Date d_mahndatum;

    /** nullable persistent field */
    private Date t_gedruckt;

    /** nullable persistent field */
    private Integer bestellposition_i_id;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnlauf flrmahnlauf;

    /** full constructor */
    public FLRBSMahnung(Integer mahnlauf_i_id, Integer mahnstufe_i_id, Date d_mahndatum, Date t_gedruckt, Integer bestellposition_i_id, BigDecimal n_offenemenge, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition, com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnlauf flrmahnlauf) {
        this.mahnlauf_i_id = mahnlauf_i_id;
        this.mahnstufe_i_id = mahnstufe_i_id;
        this.d_mahndatum = d_mahndatum;
        this.t_gedruckt = t_gedruckt;
        this.bestellposition_i_id = bestellposition_i_id;
        this.n_offenemenge = n_offenemenge;
        this.flrbestellung = flrbestellung;
        this.flrbestellposition = flrbestellposition;
        this.flrmahnlauf = flrmahnlauf;
    }

    /** default constructor */
    public FLRBSMahnung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getMahnlauf_i_id() {
        return this.mahnlauf_i_id;
    }

    public void setMahnlauf_i_id(Integer mahnlauf_i_id) {
        this.mahnlauf_i_id = mahnlauf_i_id;
    }

    public Integer getMahnstufe_i_id() {
        return this.mahnstufe_i_id;
    }

    public void setMahnstufe_i_id(Integer mahnstufe_i_id) {
        this.mahnstufe_i_id = mahnstufe_i_id;
    }

    public Date getD_mahndatum() {
        return this.d_mahndatum;
    }

    public void setD_mahndatum(Date d_mahndatum) {
        this.d_mahndatum = d_mahndatum;
    }

    public Date getT_gedruckt() {
        return this.t_gedruckt;
    }

    public void setT_gedruckt(Date t_gedruckt) {
        this.t_gedruckt = t_gedruckt;
    }

    public Integer getBestellposition_i_id() {
        return this.bestellposition_i_id;
    }

    public void setBestellposition_i_id(Integer bestellposition_i_id) {
        this.bestellposition_i_id = bestellposition_i_id;
    }

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnlauf getFlrmahnlauf() {
        return this.flrmahnlauf;
    }

    public void setFlrmahnlauf(com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnlauf flrmahnlauf) {
        this.flrmahnlauf = flrmahnlauf;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
