package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHersteller implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_leadin;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** full constructor */
    public FLRHersteller(String c_nr, String c_leadin, FLRPartner flrpartner) {
        this.c_nr = c_nr;
        this.c_leadin = c_leadin;
        this.flrpartner = flrpartner;
    }

    /** default constructor */
    public FLRHersteller() {
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

    public String getC_leadin() {
        return this.c_leadin;
    }

    public void setC_leadin(String c_leadin) {
        this.c_leadin = c_leadin;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
