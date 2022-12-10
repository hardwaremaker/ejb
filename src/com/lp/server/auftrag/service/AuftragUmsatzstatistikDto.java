
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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.partner.service.KundeDto;

public class AuftragUmsatzstatistikDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sKunde = "";
	private String sKundengruppierung = "";

	private java.util.Date dErstumsatz = null;

	public java.util.Date getDErstumsatz() {
		return dErstumsatz;
	}

	public void setDErstumsatz(java.util.Date dErstumsatz) {
		this.dErstumsatz = dErstumsatz;
	}

	public String getSKunde() {
		return sKunde;
	}

	public void setSKunde(String sKunde) {
		this.sKunde = sKunde;
	}

	private Integer iGruppierung = null;

	public void setIGruppierung(Integer iGruppierung) {
		this.iGruppierung = iGruppierung;
	}

	public Integer getIGruppierung() {
		return iGruppierung;
	}

	private BigDecimal bdUmsatz = new BigDecimal(0);

	public void setBdUmsatz(BigDecimal bdUmsatz) {
		this.bdUmsatz = bdUmsatz;
	}

	public void setAbcKlassifizierung(String abcKlassifizierung) {
		this.abcKlassifizierung = abcKlassifizierung;
	}

	public void setSKundengruppierung(String sKundengruppierung) {
		this.sKundengruppierung = sKundengruppierung;
	}


	public void setBdKreditlimit(BigDecimal bdKreditlimit) {
		this.bdKreditlimit = bdKreditlimit;
	}

	public BigDecimal getBdKreditlimit() {
		return bdKreditlimit;
	}

	
	public BigDecimal getBdUmsatz() {
		return bdUmsatz;
	}

	
	private BigDecimal[] subSummeUmsatz = null;
	private String abcKlassifizierung;
	
	private BigDecimal bdKreditlimit = null;

	private KundeDto kundeDto = null;

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	private Integer iKundennummer = 0;
	private String sLkz = "";
	private String sPlz = "";
	private String sOrt = "";

	public String getSOrt() {
		return sOrt;
	}

	public void setSOrt(String sOrt) {
		this.sOrt = sOrt;
	}

	public Integer getIKundennummer() {
		return iKundennummer;
	}

	public void setIKundennummer(Integer kundennummer) {
		iKundennummer = kundennummer;
	}

	public String getSLkz() {
		return sLkz;
	}

	public void setSLkz(String lkz) {
		sLkz = lkz;
	}

	public String getSPlz() {
		return sPlz;
	}

	public void setSPlz(String plz) {
		sPlz = plz;
	}

	public BigDecimal[] getSubSummeUmsatz() {
		return subSummeUmsatz;
	}

	public String getAbcKlassifizierung() {
		return abcKlassifizierung;
	}

	public String getSKundengruppierung() {
		return sKundengruppierung;
	}


	public void setSubsumme(BigDecimal[] subSumme) {
		this.subSummeUmsatz = subSumme;
	}

	public AuftragUmsatzstatistikDto(int anzahlSubsumme) {
		subSummeUmsatz = new BigDecimal[anzahlSubsumme];
		for (int i = 0; i < anzahlSubsumme; i++) {
			subSummeUmsatz[i] = new BigDecimal(0);
		}
	
	}

	private Integer iZahlungsziel = null;

	public void setIZahlungsziel(Integer iZahlungsziel) {
		this.iZahlungsziel = iZahlungsziel;
	}

	public Integer getIZahlungsziel() {
		return iZahlungsziel;
	}

	private Integer lieferantIId = null;

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	private Integer iLieferart = null;

	public void setILieferart(Integer iLieferart) {
		this.iLieferart = iLieferart;
	}

	public Integer getILieferart() {
		return iLieferart;
	}

	private Integer iSpediteur = null;

	public void setISpediteur(Integer iSpediteur) {
		this.iSpediteur = iSpediteur;
	}

	public Integer getISpediteur() {
		return iSpediteur;
	}
}
