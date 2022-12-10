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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "VersandauftragfindByPersonalIIdStatus", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.personalIId=?1 AND o.statusCNr=?2"),
		@NamedQuery(name = "VersandauftragfindByEmpfaengerPartnerIId", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.partnerIIdEmpfaenger=?1"),
		@NamedQuery(name = "VersandauftragfindAllOhneDaten", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.oInhalt <>null AND o.tSendezeitpunkt <>null AND o.statusCNr='Erledigt'"),
		@NamedQuery(name = "VersandauftragfindBySenderPartnerIId", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.partnerIIdSender=?1"),
		@NamedQuery(name = "VersandauftragfindOffen", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.statusCNr IS NULL AND o.cJobid IS NULL AND o.tSendezeitpunktwunsch<?1 ORDER BY o.tSendezeitpunktwunsch"),
		@NamedQuery(name = "VersandauftragfindStatusCNr", query = "SELECT OBJECT(o) FROM Versandauftrag o WHERE o.statusCNr=?1 AND o.cJobid <> NULL ORDER BY o.tSendezeitpunktwunsch")})
@Entity
@Table(name = "LP_VERSANDAUFTRAG")
public class Versandauftrag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_EMPFAENGER")
	private String cEmpfaenger;

	@Column(name = "C_CCEMPFAENGER")
	private String cCcempfaenger;

	@Column(name = "C_BETREFF")
	private String cBetreff;

	@Column(name = "C_TEXT")
	private String cText;

	@Column(name = "C_ABSENDERADRESSE")
	private String cAbsenderadresse;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "T_SENDEZEITPUNKTWUNSCH")
	private Timestamp tSendezeitpunktwunsch;

	@Column(name = "T_SENDEZEITPUNKT")
	private Timestamp tSendezeitpunkt;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "I_BELEGIID")
	private Integer iBelegiid;

	@Column(name = "C_STATUSTEXT")
	private String cStatustext;

	@Column(name = "O_INHALT")
	private byte[] oInhalt;

	@Column(name = "C_JOBID")
	private String cJobid;

	@Column(name = "B_EMPFANGSBESTAETIGUNG")
	private Short bEmpfangsbestaetigung;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "PARTNER_I_ID_EMPFAENGER")
	private Integer partnerIIdEmpfaenger;

	@Column(name = "PARTNER_I_ID_SENDER")
	private Integer partnerIIdSender;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;
	
	@Column(name = "C_BCCEMPFAENGER")
	private String cBccempfaenger;

	@Column(name = "O_VERSANDINFO")
	private byte[] oVersandinfo;

	@Column(name = "O_MESSAGE")
	private byte[] oMessage;
	
	private static final long serialVersionUID = 1L;

	public Versandauftrag() {
		super();
	}

	public Versandauftrag(Integer id, 
			String empfaenger,
			Timestamp sendezeitpunktwunsch,
			Integer personalIId,
			Integer partnerIIdEmpfaenger2,
			Integer partnerIIdSender2,
			byte[] inhalt, 
			Short empfangsbestaetigung) {
		setIId(id);
		setCEmpfaenger(empfaenger);
		setTSendezeitpunkt(sendezeitpunktwunsch);
		setPersonalIId(personalIId);
		setPartnerIIdEmpfaenger(partnerIIdEmpfaenger2);
		setPartnerIIdSender(partnerIIdSender2);
		setOInhalt(inhalt);
		setBEmpfangsbestaetigung(empfangsbestaetigung);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}
	
	public Versandauftrag(Integer id, 
			String empfaenger,
			Timestamp sendezeitpunktwunsch,
			Integer personalIId,
			Short empfangsbestaetigung) {
		setIId(id);
		setCEmpfaenger(empfaenger);
		setTSendezeitpunkt(sendezeitpunktwunsch);
		setPersonalIId(personalIId);
		setBEmpfangsbestaetigung(empfangsbestaetigung);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCEmpfaenger() {
		return this.cEmpfaenger;
	}

	public void setCEmpfaenger(String cEmpfaenger) {
		this.cEmpfaenger = cEmpfaenger;
	}

	public String getCCcempfaenger() {
		return this.cCcempfaenger;
	}

	public void setCCcempfaenger(String cCcempfaenger) {
		this.cCcempfaenger = cCcempfaenger;
	}

	public String getCBetreff() {
		return this.cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
	}

	public String getCText() {
		return this.cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public String getCAbsenderadresse() {
		return this.cAbsenderadresse;
	}

	public void setCAbsenderadresse(String cAbsenderadresse) {
		this.cAbsenderadresse = cAbsenderadresse;
	}

	public String getCDateiname() {
		return this.cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public Timestamp getTSendezeitpunktwunsch() {
		return this.tSendezeitpunktwunsch;
	}

	public void setTSendezeitpunktwunsch(Timestamp tSendezeitpunktwunsch) {
		this.tSendezeitpunktwunsch = tSendezeitpunktwunsch;
	}

	public Timestamp getTSendezeitpunkt() {
		return this.tSendezeitpunkt;
	}

	public void setTSendezeitpunkt(Timestamp tSendezeitpunkt) {
		this.tSendezeitpunkt = tSendezeitpunkt;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getIBelegIIdd() {
		return this.iBelegiid;
	}

	public void setIBelegIId(Integer iBelegiid) {
		this.iBelegiid = iBelegiid;
	}

	public String getCStatustext() {
		return this.cStatustext;
	}

	public void setCStatustext(String cStatustext) {
		this.cStatustext = cStatustext;
	}

	public byte[] getOInhalt() {
		return this.oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public String getCJobid() {
		return this.cJobid;
	}

	public void setCJobid(String cJobid) {
		this.cJobid = cJobid;
	}

	public Short getBEmpfangsbestaetigung() {
		return this.bEmpfangsbestaetigung;
	}

	public void setBEmpfangsbestaetigung(Short bEmpfangsbestaetigung) {
		this.bEmpfangsbestaetigung = bEmpfangsbestaetigung;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegart) {
		this.belegartCNr = belegart;
	}

	public String getStatusCNr() {
		return this.statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getPartnerIIdEmpfaenger() {
		return this.partnerIIdEmpfaenger;
	}

	public void setPartnerIIdEmpfaenger(Integer partnerIIdEmpfaenger) {
		this.partnerIIdEmpfaenger = partnerIIdEmpfaenger;
	}

	public Integer getPartnerIIdSender() {
		return this.partnerIIdSender;
	}

	public void setPartnerIIdSender(Integer partnerIIdSender) {
		this.partnerIIdSender = partnerIIdSender;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personal) {
		this.personalIId = personal;
	}

	public String getCBccempfaenger() {
		return cBccempfaenger;
	}
	
	public void setCBccempfaenger(String cBccempfaenger) {
		this.cBccempfaenger = cBccempfaenger;
	}
	
	public byte[] getOVersandinfo() {
		return this.oVersandinfo;
	}

	public void setOVersandinfo(byte[] oVersandinfo) {
		this.oVersandinfo = oVersandinfo;
	}

	public byte[] getOMessage() {
		return this.oMessage;
	}

	public void setOMessage(byte[] oMessage) {
		this.oMessage = oMessage;
	}

	public String toLogString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[MAIL] ");
		sb.append("ID:" + iId);
		sb.append("AN:" + cEmpfaenger + " ");
		sb.append("VON:" + cAbsenderadresse + " ");
		sb.append("BETR:" + cBetreff + " ");
		if (cCcempfaenger != null)
			sb.append("CC:" + cCcempfaenger + " ");
		if (cBccempfaenger != null)
			sb.append("BCC:" + cBccempfaenger + " ");
		if (oInhalt != null) {
			if (oInhalt.length>0) {
				if (cDateiname == null)
					sb.append("ANHANG:" + "Anhang.pdf" + " " + oInhalt.length + "Bytes ");
				else
					sb.append("ANHANG:" + cDateiname + " " + oInhalt.length + "Bytes ");
			}
		}
		if (oVersandinfo != null) {
			sb.append("VERSANDINFO:" + oVersandinfo.length + "Bytes ");
		}
		if (oMessage != null) {
			sb.append("MESSAGE:" + oMessage.length + "Bytes ");
		}
		return new String(sb);
	}

}
