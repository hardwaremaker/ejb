package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmarecht implements Serializable {
	private static final long serialVersionUID = 9099794217614982905L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer hvmalizenz_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz;

    private Short b_aktiv;
    
    /** full constructor */
    public FLRHvmarecht(String c_nr, Integer hvmalizenz_i_id, Short b_aktiv, com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz) {
        this.c_nr = c_nr;
        this.hvmalizenz_i_id = hvmalizenz_i_id;
        this.flrhvmalizenz = flrhvmalizenz;
        this.b_aktiv = b_aktiv;
    }

    /** default constructor */
    public FLRHvmarecht() {
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

	public Short getB_aktiv() {
		return b_aktiv;
	}

	public void setB_aktiv(Short b_aktiv) {
		this.b_aktiv = b_aktiv;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
