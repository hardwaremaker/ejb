package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "LiefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum", query = "SELECT OBJECT(C) FROM Liefermengen c WHERE c.artikelIId = ?1 AND  c.kundeIIdLieferadresse = ?2 AND  c.tDatum = ?3") })
@Entity
@Table(name = "PART_LIEFERMENGEN")
public class Liefermengen implements Serializable {
	private static final long serialVersionUID = 2981524321590962943L;

	public Liefermengen() {
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "T_DATUM")
	private java.sql.Timestamp tDatum;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "C_LSTEXT")
	private String cLstext;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public java.sql.Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(java.sql.Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public String getCLstext() {
		return cLstext;
	}

	public void setCLstext(String cLstext) {
		this.cLstext = cLstext;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Liefermengen(Integer iId, Integer artikelIId,
			Integer kundeIIdLieferadresse, java.sql.Timestamp tDatum,
			BigDecimal nMenge) {
		setIId(iId);
		setArtikelIId(artikelIId);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setTDatum(tDatum);
		setNMenge(nMenge);

	}

}
