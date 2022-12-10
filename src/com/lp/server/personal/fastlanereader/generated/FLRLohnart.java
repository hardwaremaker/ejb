package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLohnart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String personalart_c_nr;

    /** nullable persistent field */
    private Integer i_lohnart;

    /** nullable persistent field */
    private Integer i_ausfall_wochen;

    /** nullable persistent field */
    private Double f_mindestuestd;

    /** full constructor */
    public FLRLohnart(String c_bez, String personalart_c_nr, Integer i_lohnart, Integer i_ausfall_wochen, Double f_mindestuestd) {
        this.c_bez = c_bez;
        this.personalart_c_nr = personalart_c_nr;
        this.i_lohnart = i_lohnart;
        this.i_ausfall_wochen = i_ausfall_wochen;
        this.f_mindestuestd = f_mindestuestd;
    }

    /** default constructor */
    public FLRLohnart() {
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

    public String getPersonalart_c_nr() {
        return this.personalart_c_nr;
    }

    public void setPersonalart_c_nr(String personalart_c_nr) {
        this.personalart_c_nr = personalart_c_nr;
    }

    public Integer getI_lohnart() {
        return this.i_lohnart;
    }

    public void setI_lohnart(Integer i_lohnart) {
        this.i_lohnart = i_lohnart;
    }

    public Integer getI_ausfall_wochen() {
        return this.i_ausfall_wochen;
    }

    public void setI_ausfall_wochen(Integer i_ausfall_wochen) {
        this.i_ausfall_wochen = i_ausfall_wochen;
    }

    public Double getF_mindestuestd() {
        return this.f_mindestuestd;
    }

    public void setF_mindestuestd(Double f_mindestuestd) {
        this.f_mindestuestd = f_mindestuestd;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
