package com.lp.server.stueckliste.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRApkommentar implements Serializable {

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** persistent field */
	private Set apkommentarspr_set;

	/** full constructor */
	public FLRApkommentar(String c_nr, String mandant_c_nr,
			Set apkommentarspr_set) {
		this.c_nr = c_nr;
		this.mandant_c_nr = mandant_c_nr;
		this.apkommentarspr_set = apkommentarspr_set;
	}

	/** default constructor */
	public FLRApkommentar() {
	}

	/** minimal constructor */
	public FLRApkommentar(Set apkommentarspr_set) {
		this.apkommentarspr_set = apkommentarspr_set;
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

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public Set getApkommentarspr_set() {
		return this.apkommentarspr_set;
	}

	public void setApkommentarspr_set(Set apkommentarspr_set) {
		this.apkommentarspr_set = apkommentarspr_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
