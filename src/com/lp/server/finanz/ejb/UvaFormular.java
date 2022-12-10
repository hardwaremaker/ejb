package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries(
		{ @NamedQuery(name = UvaFormularQuery.ByFinanzamtId, 
			query = "SELECT OBJECT(o) FROM UvaFormular o " + 
					"WHERE o.finanzamtId=?1 AND o.mandantCnr=?2 " + 
					"ORDER BY o.iGruppe, o.iSort"), 
		@NamedQuery(name = UvaFormularQuery.ByFinanzamtIdUvaartId,
			query = "SELECT OBJECT(o) FROM UvaFormular o " + 
					"WHERE o.finanzamtId=?1 AND o.mandantCnr=?2 " + 
					"AND o.uvaartId=?3")}
)

@Entity
@Table(name="FB_UVAFORMULAR")
public class UvaFormular  implements Serializable {
	private static final long serialVersionUID = -2978906457716973008L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCnr;

	@Column(name = "UVAART_I_ID")
	private Integer uvaartId;

	@Column(name = "I_GRUPPE")
	private Integer iGruppe;
	
	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_KENNZEICHEN")
	private String cKennzeichen;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;



	public UvaFormular() {
	}
	
	public UvaFormular(Integer pk) {
		this.iId = pk;
	}

	public UvaFormular(Integer finanzamtId, String mandantCnr, Integer uvaartId) {
		setFinanzamtIId(finanzamtId);
		setMandantCNr(mandantCnr);
		setUvaartIId(uvaartId);
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIId() {
		return iId;
	}
	
	public void setFinanzamtIId(Integer finanzamtId) {
		this.finanzamtId = finanzamtId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCnr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCnr;
	}
	
	public void setUvaartIId(Integer uvaartId) {
		this.uvaartId = uvaartId;
	}

	public Integer getUvaartId() {
		return uvaartId;
	}

	public void setCKennzeichen(String cKennzeichen) {
		this.cKennzeichen = cKennzeichen;
	}

	public String getCKennzeichen() {
		return cKennzeichen;
	}
	
	public Integer getIGruppe() {
		return iGruppe;
	}
	
	public void setIGruppe(Integer gruppe) {
		this.iGruppe = gruppe;
	}
	
	public Integer getISort() {
		return iSort;
	}
	public void setISort(Integer isort) {
		this.iSort = isort;
	}
	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
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

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}	
}
