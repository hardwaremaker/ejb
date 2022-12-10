package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmarolle implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer systemrolle_i_id;

    /** nullable persistent field */
    private Integer hvmarecht_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRHvmarecht flrhvmarecht;

    /** nullable persistent field */
    private FLRSystemrolle flrsystemrolle;

    /** full constructor */
    public FLRHvmarolle(Integer systemrolle_i_id, Integer hvmarecht_i_id, com.lp.server.personal.fastlanereader.generated.FLRHvmarecht flrhvmarecht, FLRSystemrolle flrsystemrolle) {
        this.systemrolle_i_id = systemrolle_i_id;
        this.hvmarecht_i_id = hvmarecht_i_id;
        this.flrhvmarecht = flrhvmarecht;
        this.flrsystemrolle = flrsystemrolle;
    }

    /** default constructor */
    public FLRHvmarolle() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getSystemrolle_i_id() {
        return this.systemrolle_i_id;
    }

    public void setSystemrolle_i_id(Integer systemrolle_i_id) {
        this.systemrolle_i_id = systemrolle_i_id;
    }

    public Integer getHvmarecht_i_id() {
        return this.hvmarecht_i_id;
    }

    public void setHvmarecht_i_id(Integer hvmarecht_i_id) {
        this.hvmarecht_i_id = hvmarecht_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRHvmarecht getFlrhvmarecht() {
        return this.flrhvmarecht;
    }

    public void setFlrhvmarecht(com.lp.server.personal.fastlanereader.generated.FLRHvmarecht flrhvmarecht) {
        this.flrhvmarecht = flrhvmarecht;
    }

    public FLRSystemrolle getFlrsystemrolle() {
        return this.flrsystemrolle;
    }

    public void setFlrsystemrolle(FLRSystemrolle flrsystemrolle) {
        this.flrsystemrolle = flrsystemrolle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
