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
package com.lp.server.projekt.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@NamedQueries( {
		@NamedQuery(name = "ProjektfindByPartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Projekt o WHERE o.partnerIId=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "ProjektejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Projekt AS o WHERE o.personalIIdZugewiesener=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "ProjektfindByPersonalIIdMandantCNrISort", query = "SELECT OBJECT (O) FROM Projekt AS o WHERE o.personalIIdZugewiesener=?1 AND o.mandantCNr=?2 AND NOT o.iSort IS NULL"),
		@NamedQuery(name = "ProjektfindByProjektIIdNachfolger", query = "SELECT OBJECT (O) FROM Projekt AS o WHERE o.projektIIdNachfolger=?1 ORDER BY o.cNr"),
		@NamedQuery(name = "ProjektfindByAnsprechpartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Projekt o WHERE o.ansprechpartnerIId=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "ProjektfindByCNrMandantCNr", query = "SELECT OBJECT (O) FROM Projekt AS o WHERE o.cNr=?1 AND o.mandantCNr=?2") })		
@Entity
@Table(name = "PROJ_PROJEKT")
public class Projekt implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_TITEL")
	private String cTitel;

	@Column(name = "I_PRIO")
	private Integer iPrio;
	
	@Column(name = "BEREICH_I_ID")
	private Integer bereichIId;

	@Column(name = "C_BUILDNUMBER")
	private String buildNumber;
	
	@Column(name = "C_DEPLOYNUMBER")
	private String deployNumber;

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public String getBuildNumber() {
		return buildNumber;
	}
	
	public void setDeployNumber(String deployNumber) {
		this.deployNumber = deployNumber;
	}
	
	public String getDeployNumber() {
		return deployNumber;
	}
	
	public Integer getBereichIId() {
		return bereichIId;
	}

	public void setBereichIId(Integer bereichIId) {
		this.bereichIId = bereichIId;
	}

	@Column(name = "O_ATTACHMENTS")
	private byte[] oAttachments;

	@Column(name = "X_FREETEXT")
	private String xFreetext;

	@Column(name = "T_ZIELWUNSCHDATUM")
	private Timestamp tZielwunschdatum;

	@Column(name = "B_VERRECHENBAR")
	private Short bVerrechenbar;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_ZEIT")
	private Timestamp tZeit;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;

	@Column(name = "PERSONAL_I_ID_ERLEDIGER")
	private Integer personalIIdErlediger;

	@Column(name = "F_DAUER")
	private Double fDauer;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_FREIGEGEBEN")
	private Short bFreigegeben;

	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "PROJEKT_I_ID_NACHFOLGER")
	private Integer projektIIdNachfolger;

	public Integer getProjektIIdNachfolger() {
		return projektIIdNachfolger;
	}

	public void setProjektIIdNachfolger(Integer projektIIdNachfolger) {
		this.projektIIdNachfolger = projektIIdNachfolger;
	}

	@Column(name = "C_ATTACHMENTSTYPE")
	private String cAttachmentstype;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_ERZEUGER")
	private Integer personalIIdErzeuger;

	@Column(name = "PERSONAL_I_ID_ZUGEWIESENER")
	private Integer personalIIdZugewiesener;

	@Column(name = "PROJEKTERLEDIGUNGSGRUND_I_ID")
	private Integer projekterledigungsgrundIId;
	
	public Integer getProjekterledigungsgrundIId() {
		return projekterledigungsgrundIId;
	}

	public void setProjekterledigungsgrundIId(Integer projekterledigungsgrundIId) {
		this.projekterledigungsgrundIId = projekterledigungsgrundIId;
	}

	@Column(name = "KATEGORIE_C_NR")
	private String kategorieCNr;
	
	@Column(name = "I_WAHRSCHEINLICHKEIT")
	private Integer  iWahrscheinlichkeit;
	
	@Column(name = "N_UMSATZGEPLANT")
	private BigDecimal nUmsatzgeplant;

	public Integer getIWahrscheinlichkeit() {
		return iWahrscheinlichkeit;
	}

	public void setIWahrscheinlichkeit(Integer wahrscheinlichkeit) {
		iWahrscheinlichkeit = wahrscheinlichkeit;
	}

	public BigDecimal getNUmsatzgeplant() {
		return nUmsatzgeplant;
	}

	public void setNUmsatzgeplant(BigDecimal umsatzgeplant) {
		nUmsatzgeplant = umsatzgeplant;
	}

	@Column(name = "PROJEKTSTATUS_C_NR")
	private String projProjektstatusCNr;

	@Column(name = "PROJEKTTYP_C_NR")
	private String projProjekttypCNr;
	
	@OneToMany(mappedBy="PROJEKT") 
	@OrderBy("tBelegdatum DESC")
	private List<History> historyCollection;


	@Column(name = "PERSONAL_I_ID_INTERNERLEDIGT")
	private Integer personalIIdInternerledigt;
	public Integer getPersonalIIdInternerledigt() {
		return personalIIdInternerledigt;
	}

	public void setPersonalIIdInternerledigt(Integer personalIIdInternerledigt) {
		this.personalIIdInternerledigt = personalIIdInternerledigt;
	}

	public Timestamp getTInternerledigt() {
		return tInternerledigt;
	}

	public void setTInternerledigt(Timestamp internerledigt) {
		tInternerledigt = internerledigt;
	}

	@Column(name = "T_INTERNERLEDIGT")
	private Timestamp  tInternerledigt;
	
	private static final long serialVersionUID = 1L;

	public Projekt() {
		super();
	}

	public Projekt(Integer iId, 
			String projKategorieCNr,
			String cTitel,
			Integer personalIIdErzeuger,
			Integer personalIIdZugewiesener,
			Integer iPrio,
			Timestamp tZielwunschdatum,
			Integer partnerIId,
			Short bVerrechenbar,
			Integer personalIIdAnlegen,
			Integer personalIIdAendern,
			String statusCNr,
			String mandantCNr,
			Timestamp tZeit,
			String projekttypCNr,
			Double fDauer,
			String cNr,
			Short bFreigegeben ,Integer bereichIId) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
	    this.setTAnlegen(t);
	    this.setTAendern(t);
		setIId(iId);
		setKategorieCNr(projKategorieCNr);
		setCTitel(cTitel);
		setPersonalIIdErzeuger(personalIIdErzeuger);
		setPersonalIIdZugewiesener(personalIIdZugewiesener);
		setProjProjekttypCNr(projekttypCNr);
		setIPrio(iPrio);
		setProjProjektstatusCNr(statusCNr);
		setTZielwunschdatum(tZielwunschdatum);
		setPartnerIId(partnerIId);
		setBVerrechenbar(bVerrechenbar);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setBFreigegeben(bFreigegeben);
		setCNr(cNr);
		setTZeit(tZeit);
		setFDauer(fDauer);
		setMandantCNr(mandantCNr);
		setBereichIId(bereichIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCTitel() {
		return this.cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public Integer getIPrio() {
		return this.iPrio;
	}

	public void setIPrio(Integer iPrio) {
		this.iPrio = iPrio;
	}

	public byte[] getOAttachments() {
		return this.oAttachments;
	}

	public void setOAttachments(byte[] oAttachments) {
		this.oAttachments = oAttachments;
	}

	public String getXFreetext() {
		return this.xFreetext;
	}

	public void setXFreetext(String xFreetext) {
		this.xFreetext = xFreetext;
	}

	public Timestamp getTZielwunschdatum() {
		return this.tZielwunschdatum;
	}

	public void setTZielwunschdatum(Timestamp tZielwunschdatum) {
		this.tZielwunschdatum = tZielwunschdatum;
	}

	public Short getBVerrechenbar() {
		return this.bVerrechenbar;
	}

	public void setBVerrechenbar(Short bVerrechenbar) {
		this.bVerrechenbar = bVerrechenbar;
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

	public Timestamp getTZeit() {
		return this.tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public String getCDateiname() {
		return this.cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public Timestamp getTErledigt() {
		return this.tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Integer getPersonalIIdErlediger() {
		return this.personalIIdErlediger;
	}

	public void setPersonalIIdErlediger(Integer personalIIdErlediger) {
		this.personalIIdErlediger = personalIIdErlediger;
	}

	public Double getFDauer() {
		return this.fDauer;
	}

	public void setFDauer(Double fDauer) {
		this.fDauer = fDauer;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBFreigegeben() {
		return this.bFreigegeben;
	}

	public void setBFreigegeben(Short bFreigegeben) {
		this.bFreigegeben = bFreigegeben;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCAttachmentstype() {
		return this.cAttachmentstype;
	}

	public void setCAttachmentstype(String cAttachmentstype) {
		this.cAttachmentstype = cAttachmentstype;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
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

	public Integer getPersonalIIdErzeuger() {
		return this.personalIIdErzeuger;
	}

	public void setPersonalIIdErzeuger(Integer personalIIdErzeuger) {
		this.personalIIdErzeuger = personalIIdErzeuger;
	}

	public Integer getPersonalIIdZugewiesener() {
		return this.personalIIdZugewiesener;
	}

	public void setPersonalIIdZugewiesener(Integer personalIIdZugewiesener) {
		this.personalIIdZugewiesener = personalIIdZugewiesener;
	}

	public String getKategorieCNr() {
		return this.kategorieCNr;
	}

	public void setKategorieCNr(String projKategorieCNr) {
		this.kategorieCNr = projKategorieCNr;
	}

	public String getProjProjektstatusCNr() {
		return this.projProjektstatusCNr;
	}

	public void setProjProjektstatusCNr(String projProjektstatusCNr) {
		this.projProjektstatusCNr = projProjektstatusCNr;
	}

	public String getProjProjekttypCNr() {
		return this.projProjekttypCNr;
	}

	public void setProjProjekttypCNr(String projProjekttypCNr) {
		this.projProjekttypCNr = projProjekttypCNr;
	}

	public List<History> getHistoryCollection() {
		return this.historyCollection;
	}

	public void setHistoryCollection(List<History> historyCollection) {
		this.historyCollection = historyCollection;
	}

}
