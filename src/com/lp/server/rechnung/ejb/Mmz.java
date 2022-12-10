package com.lp.server.rechnung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "MmzByMandantCNrNBisWert", query = "SELECT OBJECT(o) FROM Mmz o WHERE o.mandantCNr=?1 AND o.nBisWert=?2 AND o.landIId IS NULL"),
		@NamedQuery(name = "MmzByMandantCNrNBisWertLandIId", query = "SELECT OBJECT(o) FROM Mmz o WHERE o.mandantCNr=?1 AND o.nBisWert=?2 AND o.landIId=?3"),
		@NamedQuery(name = "MmzByMandantCNr", query = "SELECT OBJECT(o) FROM Mmz o WHERE o.mandantCNr=?1") })
@Entity
@Table(name = "RECH_MMZ")
public class Mmz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LAND_I_ID")
	private Integer landIId;

	public Integer getLandIId() {
		return landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	@Column(name = "N_BIS_WERT")
	private BigDecimal nBisWert;

	@Column(name = "N_ZUSCHLAG")
	private BigDecimal nZuschlag;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getNBisWert() {
		return nBisWert;
	}

	public void setNBisWert(BigDecimal nBisWert) {
		this.nBisWert = nBisWert;
	}

	public BigDecimal getNZuschlag() {
		return nZuschlag;
	}

	public void setNZuschlag(BigDecimal nZuschlag) {
		this.nZuschlag = nZuschlag;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private static final long serialVersionUID = 1L;

	public Mmz() {
		super();
	}

	public Mmz(Integer id, String mandantCNr, Integer artikelIId, BigDecimal nBisWert, BigDecimal nZuschlag) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setArtikelIId(artikelIId);
		setNBisWert(nBisWert);
		setNZuschlag(nZuschlag);
	}

}
