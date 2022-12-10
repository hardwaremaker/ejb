package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPASelektion implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer selektion_i_id;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion;

    /** full constructor */
    public FLRPASelektion(Integer partner_i_id, Integer selektion_i_id, String c_bemerkung, com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion) {
        this.partner_i_id = partner_i_id;
        this.selektion_i_id = selektion_i_id;
        this.c_bemerkung = c_bemerkung;
        this.flrselektion = flrselektion;
    }

    /** default constructor */
    public FLRPASelektion() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public Integer getSelektion_i_id() {
        return this.selektion_i_id;
    }

    public void setSelektion_i_id(Integer selektion_i_id) {
        this.selektion_i_id = selektion_i_id;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRSelektion getFlrselektion() {
        return this.flrselektion;
    }

    public void setFlrselektion(com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion) {
        this.flrselektion = flrselektion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
