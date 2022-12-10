package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREkweblieferant implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer webpartner_i_id;

    /** nullable persistent field */
    private Integer einkaufsangebot_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private FLRWebpartner flrwebpartner;


    public FLREkweblieferant(Integer webpartner_i_id,
			Integer einkaufsangebot_i_id, Short b_gesperrt, Integer i_sort,
			FLRWebpartner flrwebpartner) {
		this.webpartner_i_id = webpartner_i_id;
		this.einkaufsangebot_i_id = einkaufsangebot_i_id;
		this.i_sort = i_sort;
		this.flrwebpartner = flrwebpartner;
	}

	/** default constructor */
    public FLREkweblieferant() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEinkaufsangebot_i_id() {
        return this.einkaufsangebot_i_id;
    }

    public void setEinkaufsangebot_i_id(Integer einkaufsangebot_i_id) {
        this.einkaufsangebot_i_id = einkaufsangebot_i_id;
    }

    public Integer getWebpartner_i_id() {
		return webpartner_i_id;
	}

	public void setWebpartner_i_id(Integer webpartner_i_id) {
		this.webpartner_i_id = webpartner_i_id;
	}

	public FLRWebpartner getFlrwebpartner() {
		return flrwebpartner;
	}

	public void setFlrwebpartner(FLRWebpartner flrwebpartner) {
		this.flrwebpartner = flrwebpartner;
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
