package com.lp.server.partner.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = KundeKennungQuery.ByKundeIdKennungIdValue,
			query = "SELECT OBJECT(o) from KundeKennung o " +
					"WHERE o.kundeIId = :kundeid AND o.kennungIId = :kennungid AND o.cWert = :value"),
	@NamedQuery(name = KundeKennungQuery.ByKundeIdKennungId,
			query = "SELECT OBJECT(o) from KundeKennung o " + 
					"WHERE o.kundeIId = :kundeid AND o.kennungIId = :kennungid"),
	@NamedQuery(name = KundeKennungQuery.ByKundeIdKennungCnr,
			query = "SELECT OBJECT(o) from KundeKennung o " + 
					"WHERE o.kundeIId = :kundeid AND o.kennungIId = " +
					"(SELECT k.iId FROM Kennung k WHERE k.cNr = :kennungcnr)")
})
@Entity
@Table(name = ITablenames.PART_KUNDEKENNUNG)
public class KundeKennung implements Serializable { 
	private static final long serialVersionUID = -3540356839638050392L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KUNDE_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	@Transient
	private Kunde kunde;

	@Column(name = "KENNUNG_I_ID")
	private Integer kennungIId;
	
	@Column(name = "C_WERT")
	private String cWert;

	public KundeKennung() {
	}
	
	public KundeKennung(Integer kundeId, Integer kennungId, String wert) {
		setKundeIId(kundeId);
		setKennungIId(kennungId);
		setCWert(wert);
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getKennungIId() {
		return kennungIId;
	}

	public void setKennungIId(Integer kennungIId) {
		this.kennungIId = kennungIId;
	}

	public String getCWert() {
		return cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}
	
	public Kunde getKunde() {
		return kunde;
	}
	
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
}
