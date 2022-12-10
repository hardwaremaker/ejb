package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBereich implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Short b_projekt_mit_betreiber;

    /** nullable persistent field */
    private Short b_projekt_mit_artikel;

    /** nullable persistent field */
    private Short b_duchgefuehrt_von_in_offene;

    /** full constructor */
    public FLRBereich(String c_bez, String mandant_c_nr, Integer i_sort, Short b_projekt_mit_betreiber, Short b_projekt_mit_artikel, Short b_duchgefuehrt_von_in_offene) {
        this.c_bez = c_bez;
        this.mandant_c_nr = mandant_c_nr;
        this.i_sort = i_sort;
        this.b_projekt_mit_betreiber = b_projekt_mit_betreiber;
        this.b_projekt_mit_artikel = b_projekt_mit_artikel;
        this.b_duchgefuehrt_von_in_offene = b_duchgefuehrt_von_in_offene;
    }

    /** default constructor */
    public FLRBereich() {
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

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Short getB_projekt_mit_betreiber() {
        return this.b_projekt_mit_betreiber;
    }

    public void setB_projekt_mit_betreiber(Short b_projekt_mit_betreiber) {
        this.b_projekt_mit_betreiber = b_projekt_mit_betreiber;
    }

    public Short getB_projekt_mit_artikel() {
        return this.b_projekt_mit_artikel;
    }

    public void setB_projekt_mit_artikel(Short b_projekt_mit_artikel) {
        this.b_projekt_mit_artikel = b_projekt_mit_artikel;
    }

    public Short getB_duchgefuehrt_von_in_offene() {
        return this.b_duchgefuehrt_von_in_offene;
    }

    public void setB_duchgefuehrt_von_in_offene(Short b_duchgefuehrt_von_in_offene) {
        this.b_duchgefuehrt_von_in_offene = b_duchgefuehrt_von_in_offene;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
