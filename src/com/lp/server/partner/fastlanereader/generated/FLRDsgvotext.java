package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRDsgvotext implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Short b_kopftext;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer dsgvokategorie_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String x_inhalt;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie flrdsgvokategorie;

    /** full constructor */
    public FLRDsgvotext(Short b_kopftext, Integer i_sort, Integer dsgvokategorie_i_id, String mandant_c_nr, String x_inhalt, com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie flrdsgvokategorie) {
        this.b_kopftext = b_kopftext;
        this.i_sort = i_sort;
        this.dsgvokategorie_i_id = dsgvokategorie_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.x_inhalt = x_inhalt;
        this.flrdsgvokategorie = flrdsgvokategorie;
    }

    /** default constructor */
    public FLRDsgvotext() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Short getB_kopftext() {
        return this.b_kopftext;
    }

    public void setB_kopftext(Short b_kopftext) {
        this.b_kopftext = b_kopftext;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getDsgvokategorie_i_id() {
        return this.dsgvokategorie_i_id;
    }

    public void setDsgvokategorie_i_id(Integer dsgvokategorie_i_id) {
        this.dsgvokategorie_i_id = dsgvokategorie_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getX_inhalt() {
        return this.x_inhalt;
    }

    public void setX_inhalt(String x_inhalt) {
        this.x_inhalt = x_inhalt;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie getFlrdsgvokategorie() {
        return this.flrdsgvokategorie;
    }

    public void setFlrdsgvokategorie(com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie flrdsgvokategorie) {
        this.flrdsgvokategorie = flrdsgvokategorie;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
