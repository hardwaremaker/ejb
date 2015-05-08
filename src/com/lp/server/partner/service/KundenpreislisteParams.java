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
package com.lp.server.partner.service;

import java.sql.Date;
import java.util.GregorianCalendar;

public class KundenpreislisteParams {
	private Integer kundeId ;
	private Integer artikelgruppeId ;
	private Integer artikelklasseId ;
	private String itemCnrVon ;
	private String itemCnrBis ;
	private boolean mitVersteckten ;
	private Date    gueltigkeitsDatum ;
	private boolean nurSonderkonditionen ;
	private boolean mitMandantensprache ;
	private boolean mitInaktiven ;
	private boolean nurWebshop ;
	
	public KundenpreislisteParams() {
		setDefaults() ;
	}
	
	public KundenpreislisteParams(Integer kundeId) {
		setDefaults() ;
		this.kundeId = kundeId ;
	}
	
	private void setDefaults() {
		mitInaktiven = true ;		
	}
	
	public Integer getKundeId() {
		return kundeId;
	}
	public void setKundeId(Integer kundeId) {
		this.kundeId = kundeId;
	}
	public Integer getArtikelgruppeId() {
		return artikelgruppeId;
	}
	public void setArtikelgruppeId(Integer artikelgruppeId) {
		this.artikelgruppeId = artikelgruppeId;
	}
	public Integer getArtikelklasseId() {
		return artikelklasseId;
	}
	public void setArtikelklasseId(Integer artikelklasseId) {
		this.artikelklasseId = artikelklasseId;
	}
	public String getItemCnrVon() {
		return itemCnrVon;
	}
	public void setItemCnrVon(String itemCnrVon) {
		this.itemCnrVon = itemCnrVon;
	}
	public String getItemCnrBis() {
		return itemCnrBis;
	}
	public void setItemCnrBis(String itemCnrBis) {
		this.itemCnrBis = itemCnrBis;
	}
	public boolean isMitVersteckten() {
		return mitVersteckten;
	}
	public void setMitVersteckten(boolean mitVersteckten) {
		this.mitVersteckten = mitVersteckten;
	}
	public Date getGueltigkeitsDatum() {
		if(gueltigkeitsDatum == null) {
			gueltigkeitsDatum = new Date(GregorianCalendar.getInstance().getTimeInMillis()) ;
		}
		return gueltigkeitsDatum;
	}
	public void setGueltigkeitsDatum(Date gueltigkeitsDatum) {
		this.gueltigkeitsDatum = gueltigkeitsDatum;
	}
	public boolean isNurSonderkonditionen() {
		return nurSonderkonditionen;
	}
	public void setNurSonderkonditionen(boolean nurSonderkonditionen) {
		this.nurSonderkonditionen = nurSonderkonditionen;
	}
	public boolean isMitMandantensprache() {
		return mitMandantensprache;
	}
	public void setMitMandantensprache(boolean mitMandantensprache) {
		this.mitMandantensprache = mitMandantensprache;
	}
	public boolean isMitInaktiven() {
		return mitInaktiven;
	}
	public void setMitInaktiven(boolean mitInaktiven) {
		this.mitInaktiven = mitInaktiven;
	}

	public boolean isNurWebshop() {
		return nurWebshop;
	}

	public void setNurWebshop(boolean nurWebshop) {
		this.nurWebshop = nurWebshop;
	}	
}
