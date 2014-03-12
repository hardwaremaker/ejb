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
package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class VersandauftragDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cEmpfaenger;
	private String cCcempfaenger;
	private String cBetreff;
	private String cText;
	private String cAbsenderadresse;
	private String cDateiname;
	private Timestamp tSendezeitpunktwunsch;
	private Timestamp tSendezeitpunkt;
	private Integer personalIId;
	private Timestamp tAnlegen;
	private Integer partnerIIdEmpfaenger;
	private Integer partnerIIdSender;
	private String belegartCNr;
	private Integer iIdBeleg;
	private String statusCNr;
	private String cStatustext;
	private byte[] oInhalt;
	private Short bEmpfangsbestaetigung;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCEmpfaenger() {
		return cEmpfaenger;
	}

	public void setCEmpfaenger(String cEmpfaenger) {
		this.cEmpfaenger = cEmpfaenger;
	}

	public String getCCcempfaenger() {
		return cCcempfaenger;
	}

	public void setCCcempfaenger(String cCcempfaenger) {
		this.cCcempfaenger = cCcempfaenger;
	}

	public String getCBetreff() {
		return cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
	}

	public String getCText() {
		return cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public String getCAbsenderadresse() {
		return cAbsenderadresse;
	}

	public void setCAbsenderadresse(String cAbsenderadresse) {
		this.cAbsenderadresse = cAbsenderadresse;
	}

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public Timestamp getTSendezeitpunktwunsch() {
		return tSendezeitpunktwunsch;
	}

	public void setTSendezeitpunktwunsch(Timestamp tSendezeitpunktwunsch) {
		this.tSendezeitpunktwunsch = tSendezeitpunktwunsch;
	}

	public Timestamp getTSendezeitpunkt() {
		return tSendezeitpunkt;
	}

	public void setTSendezeitpunkt(Timestamp tSendezeitpunkt) {
		this.tSendezeitpunkt = tSendezeitpunkt;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPartnerIIdEmpfaenger() {
		return partnerIIdEmpfaenger;
	}

	public void setPartnerIIdEmpfaenger(Integer partnerIIdEmpfaenger) {
		this.partnerIIdEmpfaenger = partnerIIdEmpfaenger;
	}

	public Integer getPartnerIIdSender() {
		return partnerIIdSender;
	}

	public void setPartnerIIdSender(Integer partnerIIdSender) {
		this.partnerIIdSender = partnerIIdSender;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getIIdBeleg() {
		return iIdBeleg;
	}

	public void setIIdBeleg(Integer iIdBeleg) {
		this.iIdBeleg = iIdBeleg;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public String getCStatustext() {
		return cStatustext;
	}

	public void setCStatustext(String cStatustext) {
		this.cStatustext = cStatustext;
	}

	public byte[] getOInhalt() {
		return oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public Short getBEmpfangsbestaetigung() {
		return bEmpfangsbestaetigung;
	}

	public void setBEmpfangsbestaetigung(Short bEmpfangsbestaetigung) {
		this.bEmpfangsbestaetigung = bEmpfangsbestaetigung;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VersandauftragDto))
			return false;
		VersandauftragDto that = (VersandauftragDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cEmpfaenger == null ? this.cEmpfaenger == null
				: that.cEmpfaenger.equals(this.cEmpfaenger)))
			return false;
		if (!(that.cCcempfaenger == null ? this.cCcempfaenger == null
				: that.cCcempfaenger.equals(this.cCcempfaenger)))
			return false;
		if (!(that.cBetreff == null ? this.cBetreff == null : that.cBetreff
				.equals(this.cBetreff)))
			return false;
		if (!(that.cText == null ? this.cText == null : that.cText
				.equals(this.cText)))
			return false;
		if (!(that.cAbsenderadresse == null ? this.cAbsenderadresse == null
				: that.cAbsenderadresse.equals(this.cAbsenderadresse)))
			return false;
		if (!(that.cDateiname == null ? this.cDateiname == null
				: that.cDateiname.equals(this.cDateiname)))
			return false;
		if (!(that.tSendezeitpunktwunsch == null ? this.tSendezeitpunktwunsch == null
				: that.tSendezeitpunktwunsch.equals(this.tSendezeitpunktwunsch)))
			return false;
		if (!(that.tSendezeitpunkt == null ? this.tSendezeitpunkt == null
				: that.tSendezeitpunkt.equals(this.tSendezeitpunkt)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.partnerIIdEmpfaenger == null ? this.partnerIIdEmpfaenger == null
				: that.partnerIIdEmpfaenger.equals(this.partnerIIdEmpfaenger)))
			return false;
		if (!(that.partnerIIdSender == null ? this.partnerIIdSender == null
				: that.partnerIIdSender.equals(this.partnerIIdSender)))
			return false;
		if (!(that.belegartCNr == null ? this.belegartCNr == null
				: that.belegartCNr.equals(this.belegartCNr)))
			return false;
		if (!(that.iIdBeleg == null ? this.iIdBeleg == null : that.iIdBeleg
				.equals(this.iIdBeleg)))
			return false;
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr
				.equals(this.statusCNr)))
			return false;
		if (!(that.cStatustext == null ? this.cStatustext == null
				: that.cStatustext.equals(this.cStatustext)))
			return false;
		if (that.oInhalt != this.oInhalt)
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cEmpfaenger.hashCode();
		result = 37 * result + this.cCcempfaenger.hashCode();
		result = 37 * result + this.cBetreff.hashCode();
		result = 37 * result + this.cText.hashCode();
		result = 37 * result + this.cAbsenderadresse.hashCode();
		result = 37 * result + this.cDateiname.hashCode();
		result = 37 * result + this.tSendezeitpunktwunsch.hashCode();
		result = 37 * result + this.tSendezeitpunkt.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.partnerIIdEmpfaenger.hashCode();
		result = 37 * result + this.partnerIIdSender.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.iIdBeleg.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.cStatustext.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cEmpfaenger;
		returnString += ", " + cCcempfaenger;
		returnString += ", " + cBetreff;
		returnString += ", " + cText;
		returnString += ", " + cAbsenderadresse;
		returnString += ", " + cDateiname;
		returnString += ", " + tSendezeitpunktwunsch;
		returnString += ", " + tSendezeitpunkt;
		returnString += ", " + personalIId;
		returnString += ", " + tAnlegen;
		returnString += ", " + partnerIIdEmpfaenger;
		returnString += ", " + partnerIIdSender;
		returnString += ", " + belegartCNr;
		returnString += ", " + iIdBeleg;
		returnString += ", " + statusCNr;
		returnString += ", " + cStatustext;
		returnString += ", " + oInhalt;
		return returnString;
	}

	public String toLogString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[MAIL] ");
			sb.append("ID:" + iId + " ");
			sb.append("AN:" + cEmpfaenger + " ");
			sb.append("VON:" + cAbsenderadresse + " ");
			sb.append("BETR:" + cBetreff + " ");
			if (cCcempfaenger != null)
				sb.append("CC:" + cCcempfaenger + " ");
			if (oInhalt != null) {
				if (oInhalt.length>0) {
					if (cDateiname == null)
						sb.append("ANHANG:" + "Anhang.pdf" + " " + oInhalt.length + "Bytes");
					else
						sb.append("ANHANG:" + cDateiname + " " + oInhalt.length + "Bytes");
				}
			} else {
				sb.append("ANHANG: keiner");
			}
			return new String(sb);
	}
}
