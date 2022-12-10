package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelkommentarart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Short b_tooltip;

    /** nullable persistent field */
    private Short b_detail;

    /** persistent field */
    private Set artikelkommentarartsprset;

    /** full constructor */
    public FLRArtikelkommentarart(String c_nr, Short b_tooltip, Short b_detail, Set artikelkommentarartsprset) {
        this.c_nr = c_nr;
        this.b_tooltip = b_tooltip;
        this.b_detail = b_detail;
        this.artikelkommentarartsprset = artikelkommentarartsprset;
    }

    /** default constructor */
    public FLRArtikelkommentarart() {
    }

    /** minimal constructor */
    public FLRArtikelkommentarart(Set artikelkommentarartsprset) {
        this.artikelkommentarartsprset = artikelkommentarartsprset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Short getB_tooltip() {
        return this.b_tooltip;
    }

    public void setB_tooltip(Short b_tooltip) {
        this.b_tooltip = b_tooltip;
    }

    public Short getB_detail() {
        return this.b_detail;
    }

    public void setB_detail(Short b_detail) {
        this.b_detail = b_detail;
    }

    public Set getArtikelkommentarartsprset() {
        return this.artikelkommentarartsprset;
    }

    public void setArtikelkommentarartsprset(Set artikelkommentarartsprset) {
        this.artikelkommentarartsprset = artikelkommentarartsprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
