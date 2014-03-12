/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;
import com.lp.server.finanz.ejb.FinanzamtPK; 

@NamedQueries( 
		{ @NamedQuery(name = "KontofindByCNrMandant", query = "SELECT OBJECT(o) FROM Konto o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "KontofindByKontotypSteuerkategorie", query = "SELECT OBJECT(o) FROM Konto o WHERE o.kontotypCNr=?1 AND o.steuerkategorieIId=?2"),
		@NamedQuery(name = "KontofindByCNrKontotypMandant", query = "SELECT OBJECT(o) FROM Konto o WHERE o.cNr=?1 AND o.kontotypCNr=?2 AND o.mandantCNr=?3"),
		@NamedQuery(name = Konto.QUERY_ALL_KONTOTYP_MANDANT, query = "SELECT OBJECT(o) FROM Konto o WHERE o.kontotypCNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = Konto.QUERY_ALL_KONTOART_MANDANT, query = "SELECT OBJECT(o) FROM Konto o WHERE o.kontoartCNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = Konto.QUERY_ALL_KONTOART_MANDANT_FINANZAMT, query = "SELECT OBJECT(o) FROM Konto o WHERE o.kontoartCNr=?1 AND o.mandantCNr=?2 AND o.finanzamtIId=?3"),
		@NamedQuery(name = "KontofindByErgebnisgruppeMandant", query = "SELECT OBJECT(o) FROM Konto o WHERE o.ergebnisgruppeIId=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "KontofindByErgebnisgruppeNegativMandant", query = "SELECT OBJECT(o) FROM Konto o WHERE o.ergebnisgruppeIId_negativ=?1 AND o.mandantCNr=?2")
		})

@Entity
@Table(name = "FB_KONTO")
public class Konto implements Serializable {
	private static final long serialVersionUID = 9035511981535928860L;

	public static final String QUERY_ALL_KONTOTYP_MANDANT = "KontofindAllByKontotypMandant" ;
	public static final String QUERY_ALL_KONTOART_MANDANT = "KontofindAllByKontoartMandant";
	public static final String QUERY_ALL_KONTOART_MANDANT_FINANZAMT = "KontofindAllByKontoartMandantFinanzamt";
	public static final String QUERY_ALL_ERGEBNISGRUPPE_MANDANT = "KontofindByErgebnisgruppeMandant";
	public static final String QUERY_ALL_ERGEBNISGRUPPENEGATIV_MANDANT = "KontofindByErgebnisgruppeNegativMandant";
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "T_GUELTIGVON")
	private Date tGueltigvon;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;
	
	@Column(name = "T_GUELTIGBIS")
	private Date tGueltigbis;

	@Column(name = "B_AUTOMEROEFFNUNGSBUCHUNG")
	private Short bAutomeroeffnungsbuchung;

	@Column(name = "B_ALLGEMEINSICHTBAR")
	private Short bAllgemeinsichtbar;

	@Column(name = "B_MANUELLBEBUCHBAR")
	private Short bManuellbebuchbar;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_LETZTESORTIERUNG")
	private String cLetztesortierung;

	@Column(name = "I_LETZTESELEKTIERTEBUCHUNG")
	private Integer iLetzteselektiertebuchung;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "ERGEBNISGRUPPE_I_ID")
	private Integer ergebnisgruppeIId;

	@Column(name = "ERGEBNISGRUPPE_I_ID_NEGATIV")
	private Integer ergebnisgruppeIId_negativ;
	
	@Column(name = "B_OHNEUST")
	private Short bOhneUst;
	
	public Integer getErgebnisgruppeIId_negativ() {
		return ergebnisgruppeIId_negativ;
	}

	public void setErgebnisgruppeIId_negativ(Integer ergebnisgruppeIId_negativ) {
		this.ergebnisgruppeIId_negativ = ergebnisgruppeIId_negativ;
	}

	@Column(name = "KONTO_I_ID_WEITERFUEHRENDSKONTO")
	private Integer kontoIIdWeiterfuehrendskonto;

	@Column(name = "KONTO_I_ID_WEITERFUEHRENDUST")
	private Integer kontoIIdWeiterfuehrendust;

	@Column(name = "KONTOART_C_NR")
	private String kontoartCNr;

	@Column(name = "KONTOTYP_C_NR")
	private String kontotypCNr;

	@Column(name = "RECHENREGEL_C_NR_WEITERFUEHRENDSKONTO")
	private String rechenregelCNrWeiterfuehrendskonto;

	@Column(name = "RECHENREGEL_C_NR_WEITERFUEHRENDUST")
	private String rechenregelCNrWeiterfuehrendust;

	@Column(name = "RECHENREGEL_C_NR_WEITERFUEHRENDBILANZ")
	private String rechenregelCNrWeiterfuehrendbilanz;

	@Column(name = "UVAART_I_ID")
	private Integer uvaartIId;

	@Column(name = "WAEHRUNG_C_NR_DRUCK")
	private String waehrungCNrDruck;
	
	//@JoinColumns( {
	//	@JoinColumn(name = "finanzamt_i_id", referencedColumnName = "partner_i_id"),
	//	@JoinColumn(name = "mandant_c_nr", referencedColumnName = "mandant_c_nr") })
	//private FinanzamtPK PkFinanzamt;

	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtIId;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "STEUERKATEGORIE_I_ID")
	private Integer steuerkategorieIId;
	
	@Column(name = "STEUERKATEGORIE_I_ID_REVERSE")
	private Integer steuerkategorieIIdReverse;
	
	@Column(name = "C_SORTIERT")
	private String cSortierung;

	@Column(name = "I_GJ_EB")
	private Integer iGeschaeftsjahrEB ;

	@Column(name = "T_EB_ANLEGEN")
	private Timestamp tEBAnlegen ;
	
	public Konto() {
		super();
	}

	public Konto(Integer id, java.lang.String mandantCNr, java.lang.String nr,
			java.lang.String bez, java.lang.Integer uvaartIId,
			Short automeroeffnungsbuchung, Short allgemeinsichtbar,
			Short manuellbebuchbar, java.lang.String kontoartCNr,
			java.lang.String kontotypCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setCBez(bez);
		setUvaartIId(uvaartIId);
		setBAutomeroeffnungsbuchung(automeroeffnungsbuchung);
		setBAllgemeinsichtbar(allgemeinsichtbar);
		setBManuellbebuchbar(manuellbebuchbar);
		setKontoartCNr(kontoartCNr);
		setKontotypCNr(kontotypCNr);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
	    setBVersteckt(Helper.boolean2Short(false));
	    setBOhneUst(Helper.boolean2Short(false));
	}
	
	public Konto(Integer id, java.lang.String mandantCNr, java.lang.String nr,
			java.lang.String bez, java.lang.String uvaartCNr,
			Short automeroeffnungsbuchung, Short allgemeinsichtbar,
			Short manuellbebuchbar, java.lang.String kontoartCNr,
			java.lang.String kontotypCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Short versteckt) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setCBez(bez);
		setUvaartIId(uvaartIId);
		setBAutomeroeffnungsbuchung(automeroeffnungsbuchung);
		setBAllgemeinsichtbar(allgemeinsichtbar);
		setBManuellbebuchbar(manuellbebuchbar);
		setKontoartCNr(kontoartCNr);
		setKontotypCNr(kontotypCNr);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
	    setBVersteckt(versteckt);
	    //setcSortierung(sortiert);
	}

	public Konto(Integer id, java.lang.String mandantCNr, java.lang.String nr,
			java.lang.String bez, java.lang.String uvaartCNr,
			Short automeroeffnungsbuchung, Short allgemeinsichtbar,
			Short manuellbebuchbar, java.lang.String kontoartCNr,
			java.lang.String kontotypCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Short versteckt, Short bOhneUst) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setCBez(bez);
		setUvaartIId(uvaartIId);
		setBAutomeroeffnungsbuchung(automeroeffnungsbuchung);
		setBAllgemeinsichtbar(allgemeinsichtbar);
		setBManuellbebuchbar(manuellbebuchbar);
		setKontoartCNr(kontoartCNr);
		setKontotypCNr(kontotypCNr);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
	    setBVersteckt(versteckt);
	    setBOhneUst(bOhneUst);
	    //setcSortierung(sortiert);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getXBemerkung() {
		return this.xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	
	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return this.finanzamtIId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Date getTGueltigvon() {
		return this.tGueltigvon;
	}

	public void setTGueltigvon(Date tGueltigvon) {
		this.tGueltigvon = tGueltigvon;
	}

	public Date getTGueltigbis() {
		return this.tGueltigbis;
	}

	public void setTGueltigbis(Date tGueltigbis) {
		this.tGueltigbis = tGueltigbis;
	}

	public Short getBAutomeroeffnungsbuchung() {
		return this.bAutomeroeffnungsbuchung;
	}

	public void setBAutomeroeffnungsbuchung(Short bAutomeroeffnungsbuchung) {
		this.bAutomeroeffnungsbuchung = bAutomeroeffnungsbuchung;
	}

	public Short getBAllgemeinsichtbar() {
		return this.bAllgemeinsichtbar;
	}

	public void setBAllgemeinsichtbar(Short bAllgemeinsichtbar) {
		this.bAllgemeinsichtbar = bAllgemeinsichtbar;
	}

	public Short getBManuellbebuchbar() {
		return this.bManuellbebuchbar;
	}

	public void setBManuellbebuchbar(Short bManuellbebuchbar) {
		this.bManuellbebuchbar = bManuellbebuchbar;
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

	public String getCLetztesortierung() {
		return this.cLetztesortierung;
	}

	public void setCLetztesortierung(String cLetztesortierung) {
		this.cLetztesortierung = cLetztesortierung;
	}

	public Integer getILetzteselektiertebuchung() {
		return this.iLetzteselektiertebuchung;
	}

	public void setILetzteselektiertebuchung(Integer iLetzteselektiertebuchung) {
		this.iLetzteselektiertebuchung = iLetzteselektiertebuchung;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getErgebnisgruppeIId() {
		return this.ergebnisgruppeIId;
	}

	public void setErgebnisgruppeIId(Integer ergebnisgruppeIId) {
		this.ergebnisgruppeIId = ergebnisgruppeIId;
	}

	public Integer getKontoIIdWeiterfuehrendskonto() {
		return this.kontoIIdWeiterfuehrendskonto;
	}

	public void setKontoIIdWeiterfuehrendskonto(
			Integer kontoIIdWeiterfuehrendskonto) {
		this.kontoIIdWeiterfuehrendskonto = kontoIIdWeiterfuehrendskonto;
	}

	public Integer getKontoIIdWeiterfuehrendust() {
		return this.kontoIIdWeiterfuehrendust;
	}

	public void setKontoIIdWeiterfuehrendust(Integer kontoIIdWeiterfuehrendust) {
		this.kontoIIdWeiterfuehrendust = kontoIIdWeiterfuehrendust;
	}

	public String getKontoartCNr() {
		return this.kontoartCNr;
	}

	public void setKontoartCNr(String kontoartCNr) {
		this.kontoartCNr = kontoartCNr;
	}

	public String getKontotypCNr() {
		return this.kontotypCNr;
	}

	public void setKontotypCNr(String kontotypCNr) {
		this.kontotypCNr = kontotypCNr;
	}

	public String getRechenregelCNrWeiterfuehrendskonto() {
		return this.rechenregelCNrWeiterfuehrendskonto;
	}

	public void setRechenregelCNrWeiterfuehrendskonto(
			String rechenregelCNrWeiterfuehrendskonto) {
		this.rechenregelCNrWeiterfuehrendskonto = rechenregelCNrWeiterfuehrendskonto;
	}

	public String getRechenregelCNrWeiterfuehrendust() {
		return this.rechenregelCNrWeiterfuehrendust;
	}

	public void setRechenregelCNrWeiterfuehrendust(
			String rechenregelCNrWeiterfuehrendust) {
		this.rechenregelCNrWeiterfuehrendust = rechenregelCNrWeiterfuehrendust;
	}

	public String getRechenregelCNrWeiterfuehrendbilanz() {
		return this.rechenregelCNrWeiterfuehrendbilanz;
	}

	public void setRechenregelCNrWeiterfuehrendbilanz(
			String rechenregelCNrWeiterfuehrendbilanz) {
		this.rechenregelCNrWeiterfuehrendbilanz = rechenregelCNrWeiterfuehrendbilanz;
	}

	public FinanzamtPK getPkFinanzamt() {
		return new FinanzamtPK(getFinanzamtIId(),getMandantCNr());
		//return this.PkFinanzamt;
	}

	public void setPkFinanzamt(FinanzamtPK PkFinanzamt) {
		this.setMandantCNr(PkFinanzamt.getMandantCNr());
		this.setFinanzamtIId(PkFinanzamt.getPartnerIId());
		//this.PkFinanzamt = PkFinanzamt;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setSteuerkategorieIId(Integer steuerkategorieIId) {
		this.steuerkategorieIId = steuerkategorieIId;
	}

	public Integer getSteuerkategorieIId() {
		return steuerkategorieIId;
	}

	public void setSteuerkategorieIIdReverse(Integer steuerkategorieIIdReverse) {
		this.steuerkategorieIIdReverse = steuerkategorieIIdReverse;
	}

	public Integer getSteuerkategorieIIdReverse() {
		return steuerkategorieIIdReverse;
	}

	public void setUvaartIId(Integer uvaartIId) {
		this.uvaartIId = uvaartIId;
	}

	public Integer getUvaartIId() {
		return uvaartIId;
	}

	public String getcSortierung() {
		return cSortierung;
	}

	public void setcSortierung(String cSortierung) {
		this.cSortierung = cSortierung;
	}

	public void setWaehrungCNrDruck(String waehrungCNrDruck) {
		this.waehrungCNrDruck = waehrungCNrDruck;
	}

	public String getWaehrungCNrDruck() {
		return waehrungCNrDruck;
	}

	public Integer getiGeschaeftsjahrEB() {
		return iGeschaeftsjahrEB;
	}

	public void setiGeschaeftsjahrEB(Integer iGeschaeftsjahrEB) {
		this.iGeschaeftsjahrEB = iGeschaeftsjahrEB;
	}

	public Timestamp getTEBAnlegen() {
		return tEBAnlegen;
	}

	public void setTEBAnlegen(Timestamp tEBAnlegen) {
		this.tEBAnlegen = tEBAnlegen;
	}

	public void setBOhneUst(Short bOhneUst) {
		this.bOhneUst = bOhneUst;
	}
	
	public Short getBOhneUst() {
		return this.bOhneUst;
	}
	
}
