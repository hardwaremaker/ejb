package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLand implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_lkz;

    /** nullable persistent field */
    private String c_name;

    /** nullable persistent field */
    private String c_telvorwahl;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private Short b_praeferenzbeguenstigt;

    /** persistent field */
    private Set spr_set;

    /** full constructor */
    public FLRLand(String c_lkz, String c_name, String c_telvorwahl, String waehrung_c_nr, Short b_praeferenzbeguenstigt, Set spr_set) {
        this.c_lkz = c_lkz;
        this.c_name = c_name;
        this.c_telvorwahl = c_telvorwahl;
        this.waehrung_c_nr = waehrung_c_nr;
        this.b_praeferenzbeguenstigt = b_praeferenzbeguenstigt;
        this.spr_set = spr_set;
    }

    /** default constructor */
    public FLRLand() {
    }

    /** minimal constructor */
    public FLRLand(Set spr_set) {
        this.spr_set = spr_set;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_lkz() {
        return this.c_lkz;
    }

    public void setC_lkz(String c_lkz) {
        this.c_lkz = c_lkz;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_telvorwahl() {
        return this.c_telvorwahl;
    }

    public void setC_telvorwahl(String c_telvorwahl) {
        this.c_telvorwahl = c_telvorwahl;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public Short getB_praeferenzbeguenstigt() {
        return this.b_praeferenzbeguenstigt;
    }

    public void setB_praeferenzbeguenstigt(Short b_praeferenzbeguenstigt) {
        this.b_praeferenzbeguenstigt = b_praeferenzbeguenstigt;
    }

    public Set getSpr_set() {
        return this.spr_set;
    }

    public void setSpr_set(Set spr_set) {
        this.spr_set = spr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
