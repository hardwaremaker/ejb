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
package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.system.service.SystemFac;

public class StrukturierterImportSiemensNXDto implements Serializable {

	double dBreite = 0;
	double dLaenge = 0;

	public double getDBreite() {
		return dBreite;
	}

	public void setDBreite(double dBreite) {
		this.dBreite = dBreite;
	}

	public double getDLaenge() {
		return dLaenge;
	}

	public void setDLaenge(double dLaenge) {
		this.dLaenge = dLaenge;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public void setiEbene(int iEbene) {
		this.iEbene = iEbene;
	}

	private static final long serialVersionUID = 1L;

	public BigDecimal getMenge() {
		return menge;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	public ArrayList<StrukturierterImportSiemensNXDto> getPositionen() {
		return positionen;
	}

	public ArrayList<StrukturierterImportSiemensNXDto> getPositionenSortiertNachArtikelIId() {
		ArrayList<StrukturierterImportSiemensNXDto> lTemp = (ArrayList<StrukturierterImportSiemensNXDto>) positionen
				.clone();

		for (int k = lTemp.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				StrukturierterImportSiemensNXDto a1 = (StrukturierterImportSiemensNXDto) lTemp
						.get(j);
				StrukturierterImportSiemensNXDto a2 = (StrukturierterImportSiemensNXDto) lTemp
						.get(j + 1);

				if (a1.getArtikelIId() > a2.getArtikelIId()) {
					lTemp.set(j, a2);
					lTemp.set(j + 1, a1);
				}

			}
		}

		return lTemp;
	}

	public void setPositionen(
			ArrayList<StrukturierterImportSiemensNXDto> positionen) {
		this.positionen = positionen;
	}

	Integer stuecklisteIId = null;
	Integer artikelIId = null;
	BigDecimal menge = null;

	String stklPosEinheit = SystemFac.EINHEIT_STUECK;

	public String getStklPosEinheit() {
		return stklPosEinheit;
	}

	public void setStklPosEinheit(String stklPosEinheit) {
		this.stklPosEinheit = stklPosEinheit;
	}

	String artikelnummer = null;

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	int iEbene = 0;

	public int getIEbene() {
		return iEbene;
	}

	private ArrayList<StrukturierterImportSiemensNXDto> positionen = new ArrayList<StrukturierterImportSiemensNXDto>();

}
