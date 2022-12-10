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
		@NamedQuery(name = "VorkassepositionFindByRechnungIIdAuftragspositionIId", query = "SELECT OBJECT(o) FROM Vorkasseposition o WHERE o.rechnungIId=?1 AND o.auftragspositionIId=?2"),
		@NamedQuery(name = "VorkassepositionFindByRechnungIId", query = "SELECT OBJECT(o) FROM Vorkasseposition o WHERE o.rechnungIId=?1"),
		@NamedQuery(name = "VorkassepositionFindByAuftragspositionIId", query = "SELECT OBJECT(o) FROM Vorkasseposition o WHERE o.auftragspositionIId=?1")})
@Entity
@Table(name = "RECH_VORKASSEPOSITION")
public class Vorkasseposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getAuftragspositionIId() {
		return auftragspositionIId;
	}

	public void setAuftragspositionIId(Integer auftragspositionIId) {
		this.auftragspositionIId = auftragspositionIId;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	@Column(name = "AUFTRAGSPOSITION_I_ID")
	private Integer auftragspositionIId;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private static final long serialVersionUID = 1L;

	public Vorkasseposition() {
		super();
	}

	public Vorkasseposition(Integer id, Integer rechnungIId, Integer auftragspositionIId, BigDecimal nBetrag) {
		setIId(id);
		setRechnungIId(rechnungIId);
		setAuftragspositionIId(auftragspositionIId);
		setNBetrag(nBetrag);

	}

}
