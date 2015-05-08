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

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.ZEITDATEN)
public class ZeitdatenDto implements Serializable, IIId {
	private static final long serialVersionUID = -3027835856637648490L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tZeit;
	private String xKommentar;
	private Integer iBelegartid;
	private Integer taetigkeitIId;
	private Integer hardwareIId;
	private Integer artikelIId;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer iBelegartpositionid;
	private Short bAutomatikbuchung;
	private Integer personalIIdAendern;
	private Short bTaetigkeitgeaendert;
	private String cBemerkungZuBelegart;
	private Timestamp tAendern;
	private String cBelegartnr;
	private String cWowurdegebucht;
	
	private Integer zeitdatenIId_BisZeit=null;
	public Integer getZeitdatenIId_BisZeit() {
		return zeitdatenIId_BisZeit;
	}

	public void setZeitdatenIId_BisZeit(Integer zeitdatenIId_BisZeit) {
		this.zeitdatenIId_BisZeit = zeitdatenIId_BisZeit;
	}

	public Timestamp gettZeit_Bis() {
		return tZeit_Bis;
	}

	public void settZeit_Bis(Timestamp tZeit_Bis) {
		this.tZeit_Bis = tZeit_Bis;
	}

	private Timestamp tZeit_Bis=null;
	
	//Wird nur fuer Stifzeiterfassung F630 verwendet
	public boolean bFertigFuerLossollarbeitsplan=false;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	@HvDtoLogIdCnr(entityClass = Artikel.class)
	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTZeit() {
		return tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Integer getTaetigkeitIId() {
		return taetigkeitIId;
	}

	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}

	public Integer getHardwareIId() {
		return hardwareIId;
	}

	public void setHardwareIId(Integer hardwareIId) {
		this.hardwareIId = hardwareIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public String getCBelegartnr() {
		return cBelegartnr;
	}

	public Integer getIBelegartpositionid() {
		return iBelegartpositionid;
	}

	public String getCBemerkungZuBelegart() {
		return cBemerkungZuBelegart;
	}

	public Integer getIBelegartid() {
		return iBelegartid;
	}

	public Short getBTaetigkeitgeaendert() {
		return bTaetigkeitgeaendert;
	}

	public Short getBAutomatikbuchung() {
		return bAutomatikbuchung;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public String getCWowurdegebucht() {
		return cWowurdegebucht;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public void setCBemerkungZuBelegart(String cBemerkungZuBelegart) {
		this.cBemerkungZuBelegart = cBemerkungZuBelegart;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public void setBTaetigkeitgeaendert(Short bTaetigkeitgeaendert) {
		this.bTaetigkeitgeaendert = bTaetigkeitgeaendert;
	}

	
	public void setBAutomatikbuchung(Short bAutomatikbuchung) {
		this.bAutomatikbuchung = bAutomatikbuchung;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public void setCWowurdegebucht(String cWowurdegebucht) {
		this.cWowurdegebucht = cWowurdegebucht;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZeitdatenDto))
			return false;
		ZeitdatenDto that = (ZeitdatenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tZeit == null ? this.tZeit == null : that.tZeit
				.equals(this.tZeit)))
			return false;
		if (!(that.taetigkeitIId == null ? this.taetigkeitIId == null
				: that.taetigkeitIId.equals(this.taetigkeitIId)))
			return false;
		if (!(that.hardwareIId == null ? this.hardwareIId == null
				: that.hardwareIId.equals(this.hardwareIId)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	
	public static ZeitdatenDto[] kopiereArrayUndVerschiebeAnfangsZeitNachSpaeter(ZeitdatenDto[] daten, long neueZeit) {
		ZeitdatenDto[] kopie=kopiereArray(daten);
		
		for (int o = 0; o < kopie.length; o++) {
			ZeitdatenDto zeitdatenDto_Aktuell = kopie[o];
			if (zeitdatenDto_Aktuell.getTZeit().before(
					new Timestamp(neueZeit))) {
				zeitdatenDto_Aktuell
						.setTZeit(new Timestamp(neueZeit));
				kopie[o] = zeitdatenDto_Aktuell;
			}
		}
		
		return kopie;
	}
	
	public static ZeitdatenDto[] kopiereArrayUndVerschiebeEndZeitNachFrueher(ZeitdatenDto[] daten, long neueZeit) {
		ZeitdatenDto[] kopie=kopiereArray(daten);
		
		for (int o = 0; o < kopie.length; o++) {
			ZeitdatenDto zeitdatenDto_Aktuell = kopie[o];
			if (zeitdatenDto_Aktuell.getTZeit().after(
					new Timestamp(neueZeit))) {
				zeitdatenDto_Aktuell
						.setTZeit(new Timestamp(neueZeit));
				kopie[o] = zeitdatenDto_Aktuell;
			}
		}
		
		return kopie;
	}
	
	public static ZeitdatenDto[] kopiereArray(ZeitdatenDto[] daten) {

		if (daten != null) {

			ZeitdatenDto[] kopie = new ZeitdatenDto[daten.length];

			for (int o = 0; o < daten.length; o++) {
				ZeitdatenDto orig = daten[o];

				ZeitdatenDto klon = clone(orig);

				kopie[o] = klon;
			}

			return kopie;
		} else {
			return null;
		}
	}

	public static ZeitdatenDto clone(ZeitdatenDto orig) {
		ZeitdatenDto klon = new ZeitdatenDto();
		klon.setArtikelIId(orig.getArtikelIId());
		klon.setBAutomatikbuchung(orig.getBAutomatikbuchung());
		klon.setBTaetigkeitgeaendert(orig.getBTaetigkeitgeaendert());
		klon.setCBelegartnr(orig.getCBelegartnr());
		klon.setCBemerkungZuBelegart(orig.getCBemerkungZuBelegart());
		klon.setCWowurdegebucht(orig.getCWowurdegebucht());
		klon.setHardwareIId(orig.getHardwareIId());
		klon.setIBelegartid(orig.getIBelegartid());
		klon.setIBelegartpositionid(orig.getIBelegartpositionid());
		klon.setIId(orig.getIId());

		klon.setPersonalIId(orig.getPersonalIId());
		klon.setPersonalIIdAendern(orig.getPersonalIIdAendern());
		klon.setPersonalIIdAnlegen(orig.getPersonalIIdAnlegen());
		klon.setTaetigkeitIId(orig.getTaetigkeitIId());
		klon.setArtikelIId(orig.getArtikelIId());
		klon.setTZeit(orig.getTZeit());
		klon.setXKommentar(orig.getXKommentar());
		return klon;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tZeit.hashCode();
		result = 37 * result + this.taetigkeitIId.hashCode();
		result = 37 * result + this.hardwareIId.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + cBelegartnr;
		returnString += ", " + iBelegartid;
		returnString += ", " + iBelegartpositionid;
		returnString += ", " + artikelIId;
		returnString += ", " + tZeit;
		returnString += ", " + taetigkeitIId;
		returnString += ", " + hardwareIId;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}
