package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerschleissteil implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_bez2;

    /** full constructor */
    public FLRVerschleissteil( String c_nr, String c_bez, String c_bez2) {
     
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.c_bez2 = c_bez2;
      
    }

    /** default constructor */
    public FLRVerschleissteil() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_bez2() {
        return this.c_bez2;
    }

    public void setC_bez2(String c_bez2) {
        this.c_bez2 = c_bez2;
    }


    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
