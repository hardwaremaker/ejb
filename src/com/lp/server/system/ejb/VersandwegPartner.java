package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.IVersandwegPartner;

@NamedQueries({
	@NamedQuery(name=VersandwegPartnerQuery.ByVersandwegIIdPartnerIId, 
			query = "SELECT OBJECT(C) FROM VersandwegPartner c WHERE c.partnerId = :partnerId AND c.versandwegId = :versandwegId AND c.mandantCnr = :mandantCnr"),
	@NamedQuery(name=VersandwegPartnerQuery.ByPartnerIId, 
		query = "SELECT OBJECT(C) FROM VersandwegPartner c WHERE c.partnerId = :partnerId AND c.mandantCnr = :mandantCnr"),
	@NamedQuery(name=VersandwegPartnerQuery.ByPartnerIIdVersandwegCnr, 
		query = "SELECT OBJECT(C) FROM VersandwegPartner c WHERE c.partnerId = :partnerId AND c.mandantCnr = :mandantCnr AND "
		  + " c.versandwegId = (SELECT v.iId FROM Versandweg v WHERE v.cnr = :versandwegCnr)" )		
})

@Entity
@Table(name="LP_VERSANDWEGPARTNER")
@Inheritance(strategy = InheritanceType.JOINED)
public class VersandwegPartner implements Serializable, IVersandwegPartner {
	private static final long serialVersionUID = 75123797980364454L;

	@Id
	@Column(name="I_ID") 
	@TableGenerator(name="versandwegpartner_id", table="LP_PRIMARYKEY",
	pkColumnName = "C_NAME", pkColumnValue="versandwegpartner", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="versandwegpartner_id")
	private Integer iId;

	@Column(name="MANDANT_C_NR")
	private String mandantCnr;

	@Column(name="PARTNER_I_ID")
	private Integer partnerId;

	@Column(name="VERSANDWEG_I_ID")
	private Integer versandwegId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCnr() {
		return mandantCnr;
	}

	public void setMandantCnr(String mandantCnr) {
		this.mandantCnr = mandantCnr;
	}

	public Integer getVersandwegId() {
		return versandwegId;
	}

	public void setVersandwegId(Integer versandwegId) {
		this.versandwegId = versandwegId;
	}

	public Integer getPartnerIId() {
		return partnerId;
	}

	public void setPartnerIId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((mandantCnr == null) ? 0 : mandantCnr.hashCode());
		result = prime * result
				+ ((partnerId == null) ? 0 : partnerId.hashCode());
		result = prime * result
				+ ((versandwegId == null) ? 0 : versandwegId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersandwegPartner other = (VersandwegPartner) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (mandantCnr == null) {
			if (other.mandantCnr != null)
				return false;
		} else if (!mandantCnr.equals(other.mandantCnr))
			return false;
		if (partnerId == null) {
			if (other.partnerId != null)
				return false;
		} else if (!partnerId.equals(other.partnerId))
			return false;
		if (versandwegId == null) {
			if (other.versandwegId != null)
				return false;
		} else if (!versandwegId.equals(other.versandwegId))
			return false;
		return true;
	}
}
