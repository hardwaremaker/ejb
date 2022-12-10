package com.lp.server.anfrage.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FLRAnfragetextsuche implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String i_id;

	/** nullable persistent field */
	private String c_typ;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String c_suche;

	/** full constructor */
	public FLRAnfragetextsuche(String c_typ, String c_nr, String c_suche) {
		this.c_typ = c_typ;
		this.c_nr = c_nr;
		this.c_suche = c_suche;
	}

	/** default constructor */
	public FLRAnfragetextsuche() {
	}

	public String getI_id() {
		return this.i_id;
	}

	public void setI_id(String i_id) {
		this.i_id = i_id;
	}

	public String getC_typ() {
		return this.c_typ;
	}

	public void setC_typ(String c_typ) {
		this.c_typ = c_typ;
	}

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public String getC_suche() {
		return this.c_suche;
	}

	public void setC_suche(String c_suche) {
		this.c_suche = c_suche;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
