/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "PersonalgehaltfindByPersonalIIdTGueltigab", query = "SELECT OBJECT(C) FROM Personalgehalt c WHERE c.personalIId = ?1 AND c.tGueltigab = ?2"),
		@NamedQuery(name = "PersonalgehaltfindLetztePersonalgehalt", query = "SELECT OBJECT(C) FROM Personalgehalt c WHERE c.personalIId = ?1 AND c.tGueltigab <= ?2 ORDER BY c.tGueltigab DESC") })
@Entity
@Table(name = "PERS_PERSONALGEHALT")
public class Personalgehalt implements Serializable {
	public BigDecimal getNGehaltNetto() {
		return nGehaltNetto;
	}

	public void setNGehaltNetto(BigDecimal nGehaltNetto) {
		this.nGehaltNetto = nGehaltNetto;
	}

	public BigDecimal getNGehaltBruttobrutto() {
		return nGehaltBruttobrutto;
	}

	public void setNGehaltBruttobrutto(BigDecimal nGehaltBruttobrutto) {
		this.nGehaltBruttobrutto = nGehaltBruttobrutto;
	}

	public BigDecimal getNPraemieBruttobrutto() {
		return nPraemieBruttobrutto;
	}

	public void setNPraemieBruttobrutto(BigDecimal nPraemieBruttobrutto) {
		this.nPraemieBruttobrutto = nPraemieBruttobrutto;
	}

	public Double getFKalkIstJahresstunden() {
		return fKalkIstJahresstunden;
	}

	public void setFKalkIstJahresstunden(Double fKalkIstJahresstunden) {
		this.fKalkIstJahresstunden = fKalkIstJahresstunden;
	}

	public BigDecimal getNLohnmittelstundensatz() {
		return nLohnmittelstundensatz;
	}

	public void setNLohnmittelstundensatz(BigDecimal nLohnmittelstundensatz) {
		this.nLohnmittelstundensatz = nLohnmittelstundensatz;
	}

	public BigDecimal getNAufschlagLohnmittelstundensatz() {
		return nAufschlagLohnmittelstundensatz;
	}

	public void setNAufschlagLohnmittelstundensatz(BigDecimal nAufschlagLohnmittelstundensatz) {
		this.nAufschlagLohnmittelstundensatz = nAufschlagLohnmittelstundensatz;
	}

	public Double getFFaktorLohnmittelstundensatz() {
		return fFaktorLohnmittelstundensatz;
	}

	public void setFFaktorLohnmittelstundensatz(Double fFaktorLohnmittelstundensatz) {
		this.fFaktorLohnmittelstundensatz = fFaktorLohnmittelstundensatz;
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	public Timestamp getTGueltigab() {
		return tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	@Column(name = "F_LEISTUNGSWERT")
	private Double fLeistungswert;

	@Column(name = "N_GEHALT")
	private BigDecimal nGehalt;

	@Column(name = "N_DRZPAUSCHALE")
	private BigDecimal nDrzpauschale;

	public BigDecimal getNDrzpauschale() {
		return nDrzpauschale;
	}

	public void setNDrzpauschale(BigDecimal nDrzpauschale) {
		this.nDrzpauschale = nDrzpauschale;
	}

	@Column(name = "F_UESTPAUSCHALE")
	private Double fUestpauschale;

	@Column(name = "N_STUNDENSATZ")
	private BigDecimal nStundensatz;

	@Column(name = "F_VERFUEGBARKEIT")
	private Double fVerfuegbarkeit;

	@Column(name = "N_KMGELD_MITFAHRER")
	private BigDecimal nKmgeldMitfahrer;
	
	public BigDecimal getNKmgeldMitfahrer() {
		return nKmgeldMitfahrer;
	}

	public void setNKmgeldMitfahrer(BigDecimal nKmgeldMitfahrer) {
		this.nKmgeldMitfahrer = nKmgeldMitfahrer;
	}

	@Column(name = "N_KMGELD1")
	private BigDecimal nKmgeld1;

	@Column(name = "N_LOHNSTEUER")
	private BigDecimal nLohnsteuer;

	public BigDecimal getNLohnsteuer() {
		return nLohnsteuer;
	}

	public void setNLohnsteuer(BigDecimal nLohnsteuer) {
		this.nLohnsteuer = nLohnsteuer;
	}

	@Column(name = "B_NEGATIVSTUNDEN_IN_URLAUB_UMWANDELN_TAGEWEISE_BETRACHTEN")
	private Short bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;
	
	public Short getBNegativstundenInUrlaubUmwandelnTageweiseBetrachten() {
		return bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;
	}

	public void setBNegativstundenInUrlaubUmwandelnTageweiseBetrachten(
			Short bNegativstundenInUrlaubUmwandelnTageweiseBetrachten) {
		this.bNegativstundenInUrlaubUmwandelnTageweiseBetrachten = bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;
	}

	@Column(name = "F_BISKILOMETER")
	private Double fBiskilometer;

	@Column(name = "N_KMGELD2")
	private BigDecimal nKmgeld2;

	@Column(name = "B_KKSGEBBEFREIT")
	private Short bKksgebbefreit;

	@Column(name = "C_GRUNDKKSGEBBEFREIT")
	private String cGrundkksgebbefreit;

	@Column(name = "B_ALLEINVERDIENER")
	private Short bAlleinverdiener;

	@Column(name = "B_ALLEINERZIEHER")
	private Short bAlleinerzieher;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "I_UESTDAUSZAHLEN")
	private Integer iUestdauszahlen;

	@Column(name = "N_UESTDPUFFER")
	private BigDecimal nUestdpuffer;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "N_GEHALT_NETTO")
	private BigDecimal nGehaltNetto;

	@Column(name = "N_GEHALT_BRUTTOBRUTTO")
	private BigDecimal nGehaltBruttobrutto;
	@Column(name = "N_PRAEMIE_BRUTTOBRUTTO")
	private BigDecimal nPraemieBruttobrutto;

	@Column(name = "F_KALK_IST_JAHRESTUNDEN")
	private Double fKalkIstJahresstunden;
	@Column(name = "N_LOHNMITTELSTUNDENSATZ")
	private BigDecimal nLohnmittelstundensatz;
	@Column(name = "N_AUFSCHLAG_LOHNMITTELSTUNDENSATZ")
	private BigDecimal nAufschlagLohnmittelstundensatz;
	@Column(name = "F_FAKTOR_LOHNMITTELSTUNDENSATZ")
	private Double fFaktorLohnmittelstundensatz;
	@Column(name = "B_STUNDENSATZ_FIXIERT")
	private Short bStundensatzFixiert;

	public Short getBStundensatzFixiert() {
		return bStundensatzFixiert;
	}

	public void setBStundensatzFixiert(Short bStundensatzFixiert) {
		this.bStundensatzFixiert = bStundensatzFixiert;
	}

	private static final long serialVersionUID = 1L;

	public Personalgehalt() {
		super();
	}

	public Personalgehalt(Integer id, Integer personalIId2, BigDecimal gehalt, Double uestpauschale,
			BigDecimal stundensatz, Integer personalIIdAendern2, Integer jahr, Integer monat, Integer uestdauszahlen,
			BigDecimal uestdpuffer,Short bNegativstundenInUrlaubUmwandelnTageweiseBetrachten) {
		setIId(id);
		setPersonalIId(personalIId2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setNGehalt(gehalt);
		setFUestpauschale(uestpauschale);
		setNStundensatz(stundensatz);
		setPersonalIIdAendern(personalIIdAendern2);
		
		setTGueltigab(new java.sql.Timestamp(new GregorianCalendar(jahr, monat, 1).getTimeInMillis()));
		setIUestdauszahlen(uestdauszahlen);
		setNUestdpuffer(uestdpuffer);
		setBAlleinerzieher(new Short((short) 0));
		setBAlleinverdiener(new Short((short) 0));
		setBKksgebbefreit(new Short((short) 0));
		setBStundensatzFixiert(new Short((short) 0));

		setNKmgeld1(new BigDecimal(0));
		setNKmgeld2(new BigDecimal(0));
		setNKmgeldMitfahrer(new BigDecimal(0));
		setNStundensatz(new BigDecimal(0));
		setFBiskilometer(new Double(0));
		setFVerfuegbarkeit(new Double(0));
		setFUestpauschale(new Double(0));
		setNDrzpauschale(new BigDecimal(0));
		setBNegativstundenInUrlaubUmwandelnTageweiseBetrachten(bNegativstundenInUrlaubUmwandelnTageweiseBetrachten);
	}


	public Double getFLeistungswert() {
		return this.fLeistungswert;
	}

	public void setFLeistungswert(Double fLeistungswert) {
		this.fLeistungswert = fLeistungswert;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNGehalt() {
		return this.nGehalt;
	}

	public void setNGehalt(BigDecimal nGehalt) {
		this.nGehalt = nGehalt;
	}

	public Double getFUestpauschale() {
		return this.fUestpauschale;
	}

	public void setFUestpauschale(Double fUestpauschale) {
		this.fUestpauschale = fUestpauschale;
	}

	public BigDecimal getNStundensatz() {
		return this.nStundensatz;
	}

	public void setNStundensatz(BigDecimal nStundensatz) {
		this.nStundensatz = nStundensatz;
	}

	public Double getFVerfuegbarkeit() {
		return this.fVerfuegbarkeit;
	}

	public void setFVerfuegbarkeit(Double fVerfuegbarkeit) {
		this.fVerfuegbarkeit = fVerfuegbarkeit;
	}

	public BigDecimal getNKmgeld1() {
		return this.nKmgeld1;
	}

	public void setNKmgeld1(BigDecimal nKmgeld1) {
		this.nKmgeld1 = nKmgeld1;
	}

	public Double getFBiskilometer() {
		return this.fBiskilometer;
	}

	public void setFBiskilometer(Double fBiskilometer) {
		this.fBiskilometer = fBiskilometer;
	}

	public BigDecimal getNKmgeld2() {
		return this.nKmgeld2;
	}

	public void setNKmgeld2(BigDecimal nKmgeld2) {
		this.nKmgeld2 = nKmgeld2;
	}

	public Short getBKksgebbefreit() {
		return this.bKksgebbefreit;
	}

	public void setBKksgebbefreit(Short bKksgebbefreit) {
		this.bKksgebbefreit = bKksgebbefreit;
	}

	public String getCGrundkksgebbefreit() {
		return this.cGrundkksgebbefreit;
	}

	public void setCGrundkksgebbefreit(String cGrundkksgebbefreit) {
		this.cGrundkksgebbefreit = cGrundkksgebbefreit;
	}

	public Short getBAlleinverdiener() {
		return this.bAlleinverdiener;
	}

	public void setBAlleinverdiener(Short bAlleinverdiener) {
		this.bAlleinverdiener = bAlleinverdiener;
	}

	public Short getBAlleinerzieher() {
		return this.bAlleinerzieher;
	}

	public void setBAlleinerzieher(Short bAlleinerzieher) {
		this.bAlleinerzieher = bAlleinerzieher;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getIUestdauszahlen() {
		return this.iUestdauszahlen;
	}

	public void setIUestdauszahlen(Integer iUestdauszahlen) {
		this.iUestdauszahlen = iUestdauszahlen;
	}

	public BigDecimal getNUestdpuffer() {
		return this.nUestdpuffer;
	}

	public void setNUestdpuffer(BigDecimal nUestdpuffer) {
		this.nUestdpuffer = nUestdpuffer;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}
