package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = GeodatenQuery.ByPartnerIId, query = "SELECT OBJECT (o) FROM Geodaten o WHERE o.partnerIId = :partnerIId")
})

@Entity
@Table(name = ITablenames.PART_GEODATEN)
public class Geodaten implements Serializable {
	private static final long serialVersionUID = -8615465000352159799L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name = "geodaten_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME",
		pkColumnValue = "geodaten", valueColumnName = "I_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "geodaten_id")
	private Integer iId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;
	@Column(name = "N_LAENGENGRAD")
	private BigDecimal laengengrad;
	@Column(name = "N_BREITENGRAD")
	private BigDecimal breitengrad;

	public Geodaten() {
	}
	
	public Integer getIId() {
		return iId;
	}
	
	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}
	
	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
	
	public BigDecimal getLaengengrad() {
		return laengengrad;
	}
	
	public void setLaengengrad(BigDecimal laengengrad) {
		this.laengengrad = laengengrad;
	}
	
	public BigDecimal getBreitengrad() {
		return breitengrad;
	}
	
	public void setBreitengrad(BigDecimal breitengrad) {
		this.breitengrad = breitengrad;
	}
}
