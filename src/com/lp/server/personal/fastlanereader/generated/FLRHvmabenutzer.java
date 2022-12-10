package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmabenutzer implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer benutzer_i_id;

    /** nullable persistent field */
    private Integer hvmalizenz_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz;

    /** nullable persistent field */
    private FLRBenutzer flrbenutzer;

    /** full constructor */
    public FLRHvmabenutzer(Integer benutzer_i_id, Integer hvmalizenz_i_id, com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz, FLRBenutzer flrbenutzer) {
        this.benutzer_i_id = benutzer_i_id;
        this.hvmalizenz_i_id = hvmalizenz_i_id;
        this.flrhvmalizenz = flrhvmalizenz;
        this.flrbenutzer = flrbenutzer;
    }

    /** default constructor */
    public FLRHvmabenutzer() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getBenutzer_i_id() {
        return this.benutzer_i_id;
    }

    public void setBenutzer_i_id(Integer benutzer_i_id) {
        this.benutzer_i_id = benutzer_i_id;
    }

    public Integer getHvmalizenz_i_id() {
        return this.hvmalizenz_i_id;
    }

    public void setHvmalizenz_i_id(Integer hvmalizenz_i_id) {
        this.hvmalizenz_i_id = hvmalizenz_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz getFlrhvmalizenz() {
        return this.flrhvmalizenz;
    }

    public void setFlrhvmalizenz(com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz) {
        this.flrhvmalizenz = flrhvmalizenz;
    }

    public FLRBenutzer getFlrbenutzer() {
        return this.flrbenutzer;
    }

    public void setFlrbenutzer(FLRBenutzer flrbenutzer) {
        this.flrbenutzer = flrbenutzer;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
