package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitplantypdetail implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Integer zeitplantyp_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRZeitplantyp flrzeitplantyp;

    /** full constructor */
    public FLRZeitplantypdetail(String c_kommentar, Integer zeitplantyp_i_id, Integer i_sort, com.lp.server.auftrag.fastlanereader.generated.FLRZeitplantyp flrzeitplantyp) {
        this.c_kommentar = c_kommentar;
        this.zeitplantyp_i_id = zeitplantyp_i_id;
        this.i_sort = i_sort;
        this.flrzeitplantyp = flrzeitplantyp;
    }

    /** default constructor */
    public FLRZeitplantypdetail() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Integer getZeitplantyp_i_id() {
        return this.zeitplantyp_i_id;
    }

    public void setZeitplantyp_i_id(Integer zeitplantyp_i_id) {
        this.zeitplantyp_i_id = zeitplantyp_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRZeitplantyp getFlrzeitplantyp() {
        return this.flrzeitplantyp;
    }

    public void setFlrzeitplantyp(com.lp.server.auftrag.fastlanereader.generated.FLRZeitplantyp flrzeitplantyp) {
        this.flrzeitplantyp = flrzeitplantyp;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
