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
import java.util.ArrayList;
import java.util.HashMap;

public class StrukturierterImportDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getPosnr() {
		return posnr;
	}

	public void setPosnr(String posnr) {
		this.posnr = posnr;
	}

	public String getArtikelnr() {
		return artikelnr;
	}

	public void setArtikelnr(String artikelnr) {
		this.artikelnr = artikelnr;
	}

	public String getArtikelbez() {
		return artikelbez;
	}

	public void setArtikelbez(String artikelbez) {
		this.artikelbez = artikelbez;
	}

	public String getAbmessungen() {
		return abmessungen;
	}

	public void setAbmessungen(String abmessungen) {
		this.abmessungen = abmessungen;
	}

	public Integer getMaterial() {
		return material;
	}

	public void setMaterial(Integer material) {
		this.material = material;
	}

	public Double getGewicht() {
		return gewicht;
	}

	public void setGewicht(Double gewicht) {
		this.gewicht = gewicht;
	}

	public String getLiefergruppe() {
		return liefergruppe;
	}

	public void setLiefergruppe(String liefergruppe) {
		this.liefergruppe = liefergruppe;
	}

	public Double getMenge() {
		return menge;
	}

	public void setMenge(Double menge) {
		this.menge = menge;
	}

	public ArrayList<StrukturierterImportDto> getPositionen() {
		return positionen;
	}

	public void setPositionen(ArrayList<StrukturierterImportDto> positionen) {
		this.positionen = positionen;
	}

	public HashMap<String, byte[]> getAnhaenge() {
		return anhaenge;
	}

	public HashMap<String, Object[]> getAnhaengeMitFileDatum() {
		return anhaengeMitFileDatum;
	}

	public void setAnhaengeMitFileDatum(
			HashMap<String, Object[]> anhaengeMitFileDatum) {
		this.anhaengeMitFileDatum = anhaengeMitFileDatum;
	}

	public void setAnhaenge(HashMap<String, byte[]> anhaenge) {
		this.anhaenge = anhaenge;
	}

	String posnr = "";
	String artikelnr = "";
	String artikelbez = null;
	String abmessungen = null;
	Integer material = null;
	Double gewicht = null;
	String liefergruppe = null;
	Double menge = null;
	
	Double dimension1 = null;
	Double dimension2 = null;
	Double dimension3 = null;
	
	public Double getDimension1() {
		return dimension1;
	}

	public void setDimension1(Double dimension1) {
		this.dimension1 = dimension1;
	}

	public Double getDimension2() {
		return dimension2;
	}

	public void setDimension2(Double dimension2) {
		this.dimension2 = dimension2;
	}

	public Double getDimension3() {
		return dimension3;
	}

	public void setDimension3(Double dimension3) {
		this.dimension3 = dimension3;
	}

	String einheitCNrZielmenge = null;

	public String getEinheitCNrZielmenge() {
		return einheitCNrZielmenge;
	}

	public void setEinheitCNrZielmenge(String einheitCNrZielmenge) {
		this.einheitCNrZielmenge = einheitCNrZielmenge;
	}

	public int getIEbene() {
		if (posnr != null) {
			return posnr.split("\\.").length;
		} else {
			return 0;
		}
	}

	private ArrayList<StrukturierterImportDto> positionen = new ArrayList<StrukturierterImportDto>();
	private HashMap<String, byte[]> anhaenge = new HashMap<String, byte[]>();
	private HashMap<String, Object[]> anhaengeMitFileDatum = new HashMap<String, Object[]>();

}
