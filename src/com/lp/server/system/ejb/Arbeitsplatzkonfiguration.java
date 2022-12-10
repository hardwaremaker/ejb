package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LP_ARBEITSPLATZKONFIGURATION")
public class Arbeitsplatzkonfiguration implements Serializable {
	private static final long serialVersionUID = 402875272124847294L;

	@Id
	@Column(name = "ARBEITSPLATZ_I_ID")
	private Integer arbeitsplatzIId;

	@Column(name = "C_SYSTEM")
	private String cSystem ;

	@Column(name = "C_BENUTZERDEFINIERT")
	private String cUser ;

	public Arbeitsplatzkonfiguration() {
	}
	
	public Arbeitsplatzkonfiguration(Integer arbeitsplatzId) {
		setArbeitsplatzIId(arbeitsplatzId);
	}
	
	public Arbeitsplatzkonfiguration(Integer arbeitsplatzId, String systemConfig, String userConfig) {
		setArbeitsplatzIId(arbeitsplatzId);
		setCSystem(systemConfig);
		setCUser(userConfig);
	}

	public Integer getArbeitsplatzIId() {
		return arbeitsplatzIId;
	}

	public void setArbeitsplatzIId(Integer arbeitsplatzIId) {
		this.arbeitsplatzIId = arbeitsplatzIId;
	}

	public String getCSystem() {
		return cSystem;
	}

	public void setCSystem(String cSystem) {
		this.cSystem = cSystem;
	}

	public String getCUser() {
		return cUser;
	}

	public void setCUser(String cUser) {
		this.cUser = cUser;
	}	
}
