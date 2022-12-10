package com.lp.server.personal.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@Entity
@Table(name = "PERS_HVMAPARAMETER")
public class Hvmaparameter implements Serializable, ICNr {
	private static final long serialVersionUID = -2638317053054407853L;

	@EmbeddedId
	private HvmaparameterPK pk;
	
	@Column(name = "C_DATENTYP")
	private String datentyp;
	
	@Column(name = "C_BEMERKUNG")
	private String bemerkung;
	
	@Column(name = "C_DEFAULT")
	private String defaultWert;
	
	@Column(name = "B_UEBERTRAGEN")
	private Short uebertragen;
	
	public HvmaparameterPK getPk() {
		return pk;
	}
	
	public void setPk(HvmaparameterPK pk) {
		this.pk = pk;
	}
	
	@Override
	public String getCNr() {
		return pk.getCNr();
	}

	@Override
	public void setCNr(String cnr) {
		pk.setCNr(cnr);
	}
	
	public String getDatentyp() {
		return datentyp;
	}

	public void setDatentyp(String datentyp) {
		this.datentyp = datentyp;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public String getDefaultWert() {
		return defaultWert;
	}

	public void setDefaultWert(String defaultWert) {
		this.defaultWert = defaultWert;
	}

	public boolean isUebertragen() {
		return uebertragen == 1;
	}
	
	public Short getUebertragen() {
		return uebertragen;
	}

	public void setUebertragen(Short uebertragen) {
		this.uebertragen = uebertragen;
	}

	public String getKategorie() {
		return pk.getKategorie();
	}
}
