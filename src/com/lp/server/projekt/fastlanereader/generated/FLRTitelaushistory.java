package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTitelaushistory implements Serializable {

    /** identifier field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private String c_titel;

    /** full constructor */
    public FLRTitelaushistory(String c_titel) {
        this.c_titel = c_titel;
    }

    /** default constructor */
    public FLRTitelaushistory() {
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("projekt_i_id", getProjekt_i_id())
            .toString();
    }

}
