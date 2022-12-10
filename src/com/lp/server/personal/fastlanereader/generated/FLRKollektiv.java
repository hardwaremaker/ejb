package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKollektiv implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_berechnungsbasis;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_verbraucheuestd;

    /** nullable persistent field */
    private BigDecimal n_normalstunden;

    /** full constructor */
    public FLRKollektiv(Integer i_berechnungsbasis, String c_bez, Short b_verbraucheuestd, BigDecimal n_normalstunden) {
        this.i_berechnungsbasis = i_berechnungsbasis;
        this.c_bez = c_bez;
        this.b_verbraucheuestd = b_verbraucheuestd;
        this.n_normalstunden = n_normalstunden;
    }

    /** default constructor */
    public FLRKollektiv() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_berechnungsbasis() {
        return this.i_berechnungsbasis;
    }

    public void setI_berechnungsbasis(Integer i_berechnungsbasis) {
        this.i_berechnungsbasis = i_berechnungsbasis;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Short getB_verbraucheuestd() {
        return this.b_verbraucheuestd;
    }

    public void setB_verbraucheuestd(Short b_verbraucheuestd) {
        this.b_verbraucheuestd = b_verbraucheuestd;
    }

    public BigDecimal getN_normalstunden() {
        return this.n_normalstunden;
    }

    public void setN_normalstunden(BigDecimal n_normalstunden) {
        this.n_normalstunden = n_normalstunden;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
