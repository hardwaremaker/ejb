package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikeltrutops implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private String c_fehlercode;

    /** full constructor */
    public FLRArtikeltrutops(Integer artikel_i_id, String c_fehlercode) {
        this.artikel_i_id = artikel_i_id;
        this.c_fehlercode = c_fehlercode;
    }

    /** default constructor */
    public FLRArtikeltrutops() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public String getC_fehlercode() {
        return this.c_fehlercode;
    }

    public void setC_fehlercode(String c_fehlercode) {
        this.c_fehlercode = c_fehlercode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
