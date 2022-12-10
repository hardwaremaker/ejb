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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

public class TelefonzeitenDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Integer partnerIId;
	private Timestamp tVon;
	private Timestamp tBis;
	private String xKommentarext;
	private String xKommentarint;
	private Integer ansprechpartnerIId;
	private Integer personalIIdErledigt;
	private Timestamp tErledigt;
	private Double fVerrechenbar;

	private Double fDauerUebersteuert;

	public Double getFDauerUebersteuert() {
		return fDauerUebersteuert;
	}

	public void setFDauerUebersteuert(Double fDauerUebersteuert) {
		this.fDauerUebersteuert = fDauerUebersteuert;
	}

	private Integer auftragIId;

	private Integer angebotIId;

	public Integer getAngebotIId() {
		return angebotIId;
	}

	public void setAngebotIId(Integer angebotIId) {
		this.angebotIId = angebotIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	private String cTelefonnrGewaehlt;

	public String getCTelefonnrGewaehlt() {
		return cTelefonnrGewaehlt;
	}

	public void setCTelefonnrGewaehlt(String cTelefonnrGewaehlt) {
		this.cTelefonnrGewaehlt = cTelefonnrGewaehlt;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Double getFVerrechenbar() {
		return fVerrechenbar;
	}

	public void setFVerrechenbar(Double fVerrechenbar) {
		this.fVerrechenbar = fVerrechenbar;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Timestamp getTVon() {
		return tVon;
	}

	public void setTVon(Timestamp tVon) {
		this.tVon = tVon;
	}

	public Timestamp getTBis() {
		return tBis;
	}

	public void setTBis(Timestamp tBis) {
		this.tBis = tBis;
	}

	public String getXKommentarext() {
		return xKommentarext;
	}

	public void setXKommentarext(String xKommentarext) {
		this.xKommentarext = xKommentarext;
	}

	public String getXKommentarint() {
		return xKommentarint;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setXKommentarint(String xKommentarint) {
		this.xKommentarint = xKommentarint;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private String cTitel;

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String titel) {
		cTitel = titel;
	}

	private Integer personalIIdZugewiesener;
	private Integer kontaktartIId;
	private Timestamp tWiedervorlage;
	private Timestamp tWiedervorlageErledigt;

	public Timestamp getTWiedervorlage() {
		return tWiedervorlage;
	}

	public void setTWiedervorlage(Timestamp wiedervorlage) {
		tWiedervorlage = wiedervorlage;
	}

	public void setTWiedervorlageErledigt(Timestamp wiedervorlageErledigt) {
		tWiedervorlageErledigt = wiedervorlageErledigt;
	}

	public Timestamp getTWiedervorlageErledigt() {
		return tWiedervorlageErledigt;
	}

	public Integer getPersonalIIdZugewiesener() {
		return personalIIdZugewiesener;
	}

	public void setPersonalIIdZugewiesener(Integer personalIIdZugewiesener) {
		this.personalIIdZugewiesener = personalIIdZugewiesener;
	}

	public Integer getKontaktartIId() {
		return kontaktartIId;
	}

	public void setKontaktartIId(Integer kontaktartIId) {
		this.kontaktartIId = kontaktartIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TelefonzeitenDto))
			return false;
		TelefonzeitenDto that = (TelefonzeitenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null : that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.partnerIId == null ? this.partnerIId == null : that.partnerIId.equals(this.partnerIId)))
			return false;
		if (!(that.tVon == null ? this.tVon == null : that.tVon.equals(this.tVon)))
			return false;
		if (!(that.tBis == null ? this.tBis == null : that.tBis.equals(this.tBis)))
			return false;
		if (!(that.xKommentarext == null ? this.xKommentarext == null : that.xKommentarext.equals(this.xKommentarext)))
			return false;
		if (!(that.xKommentarint == null ? this.xKommentarint == null : that.xKommentarint.equals(this.xKommentarint)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.tVon.hashCode();
		result = 37 * result + this.tBis.hashCode();
		result = 37 * result + this.xKommentarext.hashCode();
		result = 37 * result + this.xKommentarint.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(256);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("personalIId:").append(personalIId);
		returnStringBuffer.append("partnerIId:").append(partnerIId);
		returnStringBuffer.append("tVon:").append(tVon);
		returnStringBuffer.append("tBis:").append(tBis);
		returnStringBuffer.append("xKommentarext:").append(xKommentarext);
		returnStringBuffer.append("xKommentarint:").append(xKommentarint);
		returnStringBuffer.append("ansprechpartnerIId:").append(ansprechpartnerIId);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
