package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNewslettergrund implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_angemeldet;

    /** full constructor */
    public FLRNewslettergrund(String c_bez, Short b_angemeldet) {
        this.c_bez = c_bez;
        this.b_angemeldet = b_angemeldet;
    }

    /** default constructor */
    public FLRNewslettergrund() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Short getB_angemeldet() {
        return this.b_angemeldet;
    }

    public void setB_angemeldet(Short b_angemeldet) {
        this.b_angemeldet = b_angemeldet;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
