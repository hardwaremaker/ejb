package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArbeitsplatz implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_pcname;

    /** nullable persistent field */
    private String c_standort;

    /** nullable persistent field */
    private String c_typ;

    /** full constructor */
    public FLRArbeitsplatz(String c_pcname, String c_standort, String c_typ) {
        this.c_pcname = c_pcname;
        this.c_standort = c_standort;
        this.c_typ = c_typ;
    }

    /** default constructor */
    public FLRArbeitsplatz() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_pcname() {
        return this.c_pcname;
    }

    public void setC_pcname(String c_pcname) {
        this.c_pcname = c_pcname;
    }

    public String getC_standort() {
        return this.c_standort;
    }

    public void setC_standort(String c_standort) {
        this.c_standort = c_standort;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
