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
package com.lp.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.lieferschein.service.LieferscheinDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:59:36 $
 */
public class AufgeloesteFehlmengenDto implements Serializable {
	public AuftragDto getAuftagDto() {
		return auftagDto;
	}

	public void setAuftagDto(AuftragDto auftagDto) {
		this.auftagDto = auftagDto;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String artikelCNr;
	private LosDto losDto;
	private AuftragDto auftagDto;
	private ArtikelDto artikelDto;
	private String[] sSeriennrChnr;
	private LagerDto lagerDto;
	private String lagerCNr;
	private LieferscheinDto lieferscheinDto;
	
	public ArtikelDto getArtikelDtoErsterOffenerAG() {
		return artikelDtoErsterOffenerAG;
	}

	public void setArtikelDtoErsterOffenerAG(ArtikelDto artikelDtoErsterOffenerAG) {
		this.artikelDtoErsterOffenerAG = artikelDtoErsterOffenerAG;
	}

	private ArtikelDto artikelDtoErsterOffenerAG;

	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}

	public void setLieferscheinDto(LieferscheinDto lieferscheinDto) {
		this.lieferscheinDto = lieferscheinDto;
	}

	public AufgeloesteFehlmengenDto() {
	}

	public void setArtikelCNr(String artikelCNr) {
		this.artikelCNr = artikelCNr;
	}
	
	private BigDecimal lagerstand;

	public BigDecimal getLagerstand() {
		return lagerstand;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}

	private BigDecimal aufgeloesteMenge;

	public void setLosCNr(String losCNr) {
		this.losCNr = losCNr;
	}

	public void setAufgeloesteMenge(BigDecimal aufgeloesteMenge) {
		this.aufgeloesteMenge = aufgeloesteMenge;
	}

	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setLagerDto(LagerDto lagerDto) {
		this.lagerDto = lagerDto;
	}

	public void setLagerCNr(String lagerCNr) {
		this.lagerCNr = lagerCNr;
	}

	public void setSSeriennrChnr(String[] sSeriennrChnr) {
		this.sSeriennrChnr = sSeriennrChnr;
	}

	private String losCNr;

	public String getArtikelCNr() {
		return artikelCNr;
	}

	public String getLosCNr() {
		return losCNr;
	}

	public BigDecimal getAufgeloesteMenge() {
		return aufgeloesteMenge;
	}

	public LosDto getLosDto() {
		return losDto;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public LagerDto getLagerDto() {
		return lagerDto;
	}

	public String getLagerCNr() {
		return lagerCNr;
	}

	public String[] getSSeriennrChnr() {
		return sSeriennrChnr;
	}
	

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	private Timestamp tAnlegen;
	
	private String kurzzeichenPersonalAnlegen;

	

	public String getKurzzeichenPersonalAnlegen() {
		return kurzzeichenPersonalAnlegen;
	}

	public void setKurzzeichenPersonalAnlegen(String kurzzeichenPersonalAnlegen) {
		this.kurzzeichenPersonalAnlegen = kurzzeichenPersonalAnlegen;
	}
	
	
}
