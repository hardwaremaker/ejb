package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerrechenbar implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_verrechenbar;

    /** nullable persistent field */
    private Integer i_sort;

    /** full constructor */
    public FLRVerrechenbar(String c_bez, Short b_verrechenbar, Integer i_sort) {
        this.c_bez = c_bez;
        this.b_verrechenbar = b_verrechenbar;
        this.i_sort = i_sort;
    }

    /** default constructor */
    public FLRVerrechenbar() {
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

    public Short getB_verrechenbar() {
        return this.b_verrechenbar;
    }

    public void setB_verrechenbar(Short b_verrechenbar) {
        this.b_verrechenbar = b_verrechenbar;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
