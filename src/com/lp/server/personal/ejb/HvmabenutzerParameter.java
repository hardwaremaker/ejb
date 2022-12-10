package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;
import com.lp.server.util.IIId;

@NamedQueries({
	@NamedQuery(name = HvmabenutzerParameterQuery.ByCnrKategorie, 
			query = "SELECT OBJECT(p) FROM HvmabenutzerParameter p WHERE " +
					"p.hvmabenutzerId = :benutzerId AND p.cnr = :cnr AND " +
					"p.kategorie = :kategorie"),
	@NamedQuery(name = HvmabenutzerParameterQuery.ByBenutzerIdMobil,
			query = "SELECT OBJECT(p) FROM HvmabenutzerParameter p WHERE " +
					"p.hvmabenutzerId = :benutzerId AND p.parameter.uebertragen = 1")
})
@Entity
@Table(name="PERS_HVMABENUTZERPARAMETER")
public class HvmabenutzerParameter implements Serializable, IIId, ICNr {
	private static final long serialVersionUID = 5907035228358783204L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "HVMABENUTZER_I_ID")
	private Integer hvmabenutzerId;
	
	@Column(name = "C_NR")
	private String cnr;
	
	@Column(name = "C_KATEGORIE")
	private String kategorie;
	
	@Column(name = "C_WERT")
	private String wert;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;
	
	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "C_NR", referencedColumnName="C_NR", insertable=false, updatable=false),
		@JoinColumn(name = "C_KATEGORIE", referencedColumnName="C_KATEGORIE", insertable=false, updatable=false)
	})
	private Hvmaparameter parameter;


	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}
	
	public Integer getHvmabenutzerId() {
		return hvmabenutzerId;
	}

	public void setHvmabenutzerId(Integer hvmabenutzerId) {
		this.hvmabenutzerId = hvmabenutzerId;
	}

	@Override
	public String getCNr() {
		return cnr;
	}

	@Override
	public void setCNr(String cnr) {
		this.cnr = cnr;
	}

	public String getKategorie() {
		return kategorie;
	}

	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}

	public String getWert() {
		return wert;
	}

	public void setWert(String wert) {
		this.wert = wert;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public HvmabenutzerParameter() {
		
	}
	
	public HvmabenutzerParameter(Integer id, Integer benutzerIId, String cNr, String kategorie, String wert,Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setHvmabenutzerId(benutzerIId);
		setCNr(cNr);
		setKategorie(kategorie);
		setWert(wert);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);

	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnr == null) ? 0 : cnr.hashCode());
		result = prime * result + ((hvmabenutzerId == null) ? 0 : hvmabenutzerId.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((kategorie == null) ? 0 : kategorie.hashCode());
		result = prime * result + ((personalIIdAendern == null) ? 0 : personalIIdAendern.hashCode());
		result = prime * result + ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime * result + ((wert == null) ? 0 : wert.hashCode());
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
		HvmabenutzerParameter other = (HvmabenutzerParameter) obj;
		if (cnr == null) {
			if (other.cnr != null)
				return false;
		} else if (!cnr.equals(other.cnr))
			return false;
		if (hvmabenutzerId == null) {
			if (other.hvmabenutzerId != null)
				return false;
		} else if (!hvmabenutzerId.equals(other.hvmabenutzerId))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (kategorie == null) {
			if (other.kategorie != null)
				return false;
		} else if (!kategorie.equals(other.kategorie))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (wert == null) {
			if (other.wert != null)
				return false;
		} else if (!wert.equals(other.wert))
			return false;
		return true;
	}
}
