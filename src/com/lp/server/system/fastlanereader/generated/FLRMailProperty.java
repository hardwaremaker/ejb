package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

public class FLRMailProperty implements Serializable {
	private static final long serialVersionUID = -3023251342145304481L;

	private String c_nr;
	private String c_wert;
	private String c_defaultwert;
	private String mandant_c_nr;
	private Integer personal_i_id_aendern;
	private Date t_aendern;
	
	public FLRMailProperty() {
	}
	
	public FLRMailProperty(String c_nr, String c_wert, String c_defaultwert, String mandant_c_nr, Integer personal_i_id_aendern, Date t_aendern) {
		this.c_nr = c_nr;
		this.c_wert = c_wert;
		this.c_defaultwert = c_defaultwert;
		this.mandant_c_nr  = mandant_c_nr;
		this.personal_i_id_aendern = personal_i_id_aendern;
		this.t_aendern = t_aendern;
	}
	
	public String getC_nr() {
		return c_nr;
	}
	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}
	
	public String getC_wert() {
		return c_wert;
	}
	public void setC_wert(String c_wert) {
		this.c_wert = c_wert;
	}
	
	public String getC_defaultwert() {
		return c_defaultwert;
	}
	public void setC_defaultwert(String c_defaultwert) {
		this.c_defaultwert = c_defaultwert;
	}
	
	public String getMandant_c_nr() {
		return mandant_c_nr;
	}
	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}
	
	public Integer getPersonal_i_id_aendern() {
		return personal_i_id_aendern;
	}
	public void setPersonal_i_id_aendern(Integer personal_i_id_aendern) {
		this.personal_i_id_aendern = personal_i_id_aendern;
	}
	
	public Date getT_aendern() {
		return t_aendern;
	}
	public void setT_aendern(Date t_aendern) {
		this.t_aendern = t_aendern;
	}
}
