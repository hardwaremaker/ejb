package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPanelbeschreibung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_name;

    /** nullable persistent field */
    private String c_typ;

    /** nullable persistent field */
    private String c_fill;

    /** nullable persistent field */
    private String c_anchor;

    /** nullable persistent field */
    private String panel_c_nr;

    /** nullable persistent field */
    private Integer i_gridx;

    /** nullable persistent field */
    private Integer i_gridy;

    /** nullable persistent field */
    private Short b_mandantory;

    /** nullable persistent field */
    private Integer artgru_i_id;

    /** nullable persistent field */
    private String c_default;

    /** nullable persistent field */
    private Integer partnerklasse_i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer bereich_i_id;

    /** nullable persistent field */
    private String projekttyp_c_nr;

    /** full constructor */
    public FLRPanelbeschreibung(String mandant_c_nr, String c_name, String c_typ, String c_fill, String c_anchor, String panel_c_nr, Integer i_gridx, Integer i_gridy, Short b_mandantory, Integer artgru_i_id, String c_default, Integer partnerklasse_i_id, Integer kostenstelle_i_id, Integer bereich_i_id, String projekttyp_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_name = c_name;
        this.c_typ = c_typ;
        this.c_fill = c_fill;
        this.c_anchor = c_anchor;
        this.panel_c_nr = panel_c_nr;
        this.i_gridx = i_gridx;
        this.i_gridy = i_gridy;
        this.b_mandantory = b_mandantory;
        this.artgru_i_id = artgru_i_id;
        this.c_default = c_default;
        this.partnerklasse_i_id = partnerklasse_i_id;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.bereich_i_id = bereich_i_id;
        this.projekttyp_c_nr = projekttyp_c_nr;
    }

    /** default constructor */
    public FLRPanelbeschreibung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getC_fill() {
        return this.c_fill;
    }

    public void setC_fill(String c_fill) {
        this.c_fill = c_fill;
    }

    public String getC_anchor() {
        return this.c_anchor;
    }

    public void setC_anchor(String c_anchor) {
        this.c_anchor = c_anchor;
    }

    public String getPanel_c_nr() {
        return this.panel_c_nr;
    }

    public void setPanel_c_nr(String panel_c_nr) {
        this.panel_c_nr = panel_c_nr;
    }

    public Integer getI_gridx() {
        return this.i_gridx;
    }

    public void setI_gridx(Integer i_gridx) {
        this.i_gridx = i_gridx;
    }

    public Integer getI_gridy() {
        return this.i_gridy;
    }

    public void setI_gridy(Integer i_gridy) {
        this.i_gridy = i_gridy;
    }

    public Short getB_mandantory() {
        return this.b_mandantory;
    }

    public void setB_mandantory(Short b_mandantory) {
        this.b_mandantory = b_mandantory;
    }

    public Integer getArtgru_i_id() {
        return this.artgru_i_id;
    }

    public void setArtgru_i_id(Integer artgru_i_id) {
        this.artgru_i_id = artgru_i_id;
    }

    public String getC_default() {
        return this.c_default;
    }

    public void setC_default(String c_default) {
        this.c_default = c_default;
    }

    public Integer getPartnerklasse_i_id() {
        return this.partnerklasse_i_id;
    }

    public void setPartnerklasse_i_id(Integer partnerklasse_i_id) {
        this.partnerklasse_i_id = partnerklasse_i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getBereich_i_id() {
        return this.bereich_i_id;
    }

    public void setBereich_i_id(Integer bereich_i_id) {
        this.bereich_i_id = bereich_i_id;
    }

    public String getProjekttyp_c_nr() {
        return this.projekttyp_c_nr;
    }

    public void setProjekttyp_c_nr(String projekttyp_c_nr) {
        this.projekttyp_c_nr = projekttyp_c_nr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
