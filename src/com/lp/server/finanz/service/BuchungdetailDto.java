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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.util.IModificationData;

public class BuchungdetailDto implements Serializable, Cloneable, IModificationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer buchungIId;
	private Integer kontoIId;
	private BigDecimal nBetrag;
	private String buchungdetailartCNr;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer kontoIIdGegenkonto;
	private BigDecimal nUst;
	private Integer iAuszug;
	private Integer iAusziffern;
	private String kommentar;

	public BuchungdetailDto() {
		
	}
	
	public BuchungdetailDto(String buchungdetailartCNr, Integer kontoIId, Integer kontoIIdGegenkonto, BigDecimal nBetrag, BigDecimal nUst) {
		this.buchungdetailartCNr = buchungdetailartCNr;
		this.kontoIId = kontoIId;
		this.kontoIIdGegenkonto = kontoIIdGegenkonto;
		this.nBetrag = nBetrag;
		this.nUst = nUst;
	}
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getBuchungIId() {
		return buchungIId;
	}

	public void setBuchungIId(Integer buchungIId) {
		this.buchungIId = buchungIId;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public Integer getKontoIIdGegenkonto() {
		return kontoIIdGegenkonto;
	}

	public void setKontoIIdGegenkonto(Integer kontoIIdGegenkonto) {
		this.kontoIIdGegenkonto = kontoIIdGegenkonto;
	}

	public BigDecimal getNUst() {
		return nUst;
	}

	public void setNUst(BigDecimal nUst) {
		this.nUst = nUst;
	}

	public Integer getIAuszug() {
		return this.iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BuchungdetailDto)) {
			return false;
		}
		BuchungdetailDto that = (BuchungdetailDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.buchungIId == null ? this.buchungIId == null
				: that.buchungIId.equals(this.buchungIId))) {
			return false;
		}
		if (!(that.kontoIId == null ? this.kontoIId == null : that.kontoIId
				.equals(this.kontoIId))) {
			return false;
		}
		if (!(that.kontoIIdGegenkonto == null ? this.kontoIIdGegenkonto == null
				: that.kontoIIdGegenkonto.equals(this.kontoIIdGegenkonto))) {
			return false;
		}
		if (!(that.nBetrag == null ? this.nBetrag == null : that.nBetrag
				.equals(this.nBetrag))) {
			return false;
		}
		if (!(that.buchungdetailartCNr == null ? this.buchungdetailartCNr == null
				: that.buchungdetailartCNr.equals(this.buchungdetailartCNr))) {
			return false;
		}
		if (!(that.nUst == null ? this.nUst == null : that.nUst
				.equals(this.nUst))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.iAusziffern == null ? this.iAusziffern == null
				: that.iAusziffern.equals(this.iAusziffern))) {
			return false;
		}
		if (!(that.kommentar == null ? this.kommentar == null
				: that.kommentar.equals(this.kommentar))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.buchungIId.hashCode();
		result = 37 * result + this.kontoIId.hashCode();
		result = 37 * result + this.kontoIIdGegenkonto.hashCode();
		result = 37 * result + this.nBetrag.hashCode();
		result = 37 * result + this.buchungdetailartCNr.hashCode();
		result = 37 * result + this.nUst.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.iAusziffern.hashCode();
		result = 37 * result + this.kommentar.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + buchungIId;
		returnString += ", " + kontoIId;
		returnString += ", " + kontoIIdGegenkonto;
		returnString += ", " + nBetrag;
		returnString += ", " + buchungdetailartCNr;
		returnString += ", " + nUst;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + iAusziffern;
		returnString += ", " + kommentar;
		return returnString;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setBuchungdetailartCNr(String buchungdetailartCNr) {
		this.buchungdetailartCNr = buchungdetailartCNr;
	}

	public String getBuchungdetailartCNr() {
		return buchungdetailartCNr;
	}

	public void setIAusziffern(Integer iAusziffern) {
		this.iAusziffern = iAusziffern;
	}

	public Integer getIAusziffern() {
		return iAusziffern;
	}
	
	public boolean isHabenBuchung() {
		return BuchenFac.HabenBuchung.equals(getBuchungdetailartCNr()) ;
 	}
	
	public boolean isSollBuchung() {
		return BuchenFac.SollBuchung.equals(getBuchungdetailartCNr()) ;		
	}
	
	public Object clone() {
		try {
			BuchungdetailDto baseClone = (BuchungdetailDto) super.clone() ;
			return baseClone ;
		} catch(CloneNotSupportedException e) {
			return this ;
		}
	}
	
	public void swapSollHaben() {
		if (this.buchungdetailartCNr.equals(BuchenFac.HabenBuchung))
			this.buchungdetailartCNr = BuchenFac.SollBuchung;
		else
			this.buchungdetailartCNr = BuchenFac.HabenBuchung;
	}
	
	public String getKommentar() {
		return kommentar;
	}
	
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
}
