package com.lp.server.rechnung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { 
	@NamedQuery(name = LastschriftvorschlagQuery.ByCAuftraggeberreferenz, query = "SELECT OBJECT(o) FROM Lastschriftvorschlag o WHERE o.cAuftraggeberreferenz=:referenz"),
	@NamedQuery(name = LastschriftvorschlagQuery.ByMahnlaufIId, query = "SELECT OBJECT(o) FROM Lastschriftvorschlag o WHERE o.mahnlaufIId=:mahnlaufIId")
})

@Entity
@Table(name = "RECH_LASTSCHRIFTVORSCHLAG")
public class Lastschriftvorschlag implements Serializable {

	private static final long serialVersionUID = -4396556620548651865L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "MAHNLAUF_I_ID")
	private Integer mahnlaufIId;
	
	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;
	
	@Column(name = "T_FAELLIG")
	private Date tFaellig;
	
	@Column(name = "N_RECHNUNGSBETRAG")
	private BigDecimal nRechnungsbetrag;
	
	@Column(name = "N_BEREITS_BEZAHLT")
	private BigDecimal nBereitsBezahlt;
	
	@Column(name = "N_ZAHLBETRAG")
	private BigDecimal nZahlbetrag;
	
	@Column(name = "C_AUFTRAGGEBERREFERENZ")
	private String cAuftraggeberreferenz;
	
	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_GESPEICHERT")
	private Integer personalIIdGespeichert;
	
	@Column(name = "T_GESPEICHERT")
	private Timestamp tGespeichert;
	
	@Column(name = "C_VERWENDUNGSZWECK")
	private String cVerwendungszweck;
	
	public Lastschriftvorschlag() {
	}

	public Lastschriftvorschlag(Integer iId, Integer mahnlaufIId,
			Integer rechnungIId, Date tFaellig, BigDecimal nRechnungsbetrag,
			BigDecimal nBereitsBezahlt, BigDecimal nZahlbetrag,
			String cAuftraggeberreferenz, Integer personalIIdAendern) {
		super();
		this.iId = iId;
		this.mahnlaufIId = mahnlaufIId;
		this.rechnungIId = rechnungIId;
		this.tFaellig = tFaellig;
		this.nRechnungsbetrag = nRechnungsbetrag;
		this.nBereitsBezahlt = nBereitsBezahlt;
		this.nZahlbetrag = nZahlbetrag;
		this.cAuftraggeberreferenz = cAuftraggeberreferenz;
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public Integer getMahnlaufIId() {
		return mahnlaufIId;
	}

	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
	}

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Date gettFaellig() {
		return tFaellig;
	}

	public void settFaellig(Date tFaellig) {
		this.tFaellig = tFaellig;
	}

	public BigDecimal getnRechnungsbetrag() {
		return nRechnungsbetrag;
	}

	public void setnRechnungsbetrag(BigDecimal nRechnungsbetrag) {
		this.nRechnungsbetrag = nRechnungsbetrag;
	}

	public BigDecimal getnBereitsBezahlt() {
		return nBereitsBezahlt;
	}

	public void setnBereitsBezahlt(BigDecimal nBereitsBezahlt) {
		this.nBereitsBezahlt = nBereitsBezahlt;
	}

	public BigDecimal getnZahlbetrag() {
		return nZahlbetrag;
	}

	public void setnZahlbetrag(BigDecimal nZahlbetrag) {
		this.nZahlbetrag = nZahlbetrag;
	}

	public String getcAuftraggeberreferenz() {
		return cAuftraggeberreferenz;
	}

	public void setcAuftraggeberreferenz(String cAuftraggeberreferenz) {
		this.cAuftraggeberreferenz = cAuftraggeberreferenz;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp gettAendern() {
		return tAendern;
	}

	public void settAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdGespeichert() {
		return personalIIdGespeichert;
	}

	public void setPersonalIIdGespeichert(Integer personalIIdGespeichert) {
		this.personalIIdGespeichert = personalIIdGespeichert;
	}

	public Timestamp gettGespeichert() {
		return tGespeichert;
	}

	public void settGespeichert(Timestamp tGespeichert) {
		this.tGespeichert = tGespeichert;
	}

	public String getCVerwendungszweck() {
		return cVerwendungszweck;
	}
	
	public void setCVerwendungszweck(String cVerwendungszweck) {
		this.cVerwendungszweck = cVerwendungszweck;
	}
}
