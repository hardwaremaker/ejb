package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LP_HVMAUSERCOUNT")
public class LpHvmaUserCount implements Serializable {
	private static final long serialVersionUID = 8275433878388437875L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "T_ZEITPUNKT")
	private Timestamp tZeitpunkt;

	@Column(name = "I_ANZAHL")
	private Integer iAnzahl;

	@Column(name = "SYSTEMROLLE_I_ID")
	private Integer systemrolleIId;
	
	@Column(name = "HVMALIZENZ_I_ID")
	private Integer hvmaLizenzId;
	
	@Column(name = "HVMARESOURCE")
	private String hvmaResource;
	
	public Integer getIId() {
		return this.iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Timestamp getTZeitpunkt() {
		return tZeitpunkt;
	}
	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}
	
	public Integer getIAnzahl() {
		return iAnzahl;
	}
	public void setIAnzahl(Integer iAnzahl) {
		this.iAnzahl = iAnzahl;
	}
	
	
	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}
	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}
	
	public Integer getHvmaLizenzIId() {
		return hvmaLizenzId;
	}
	public void setHvmaLizenzIId(Integer hvmaLizenzIId) {
		this.hvmaLizenzId = hvmaLizenzIId;
	}
	
	public String getHvmaResource() {
		return hvmaResource;
	}
	public void setHvmaResource(String resource) {
		this.hvmaResource = resource;
	}
}
