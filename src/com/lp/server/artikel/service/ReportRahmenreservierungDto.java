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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReportRahmenreservierungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String auftragCNr;
	private String cKundenname;
	private String cBez;
	private Timestamp tUebersteuerterLiefertermin;
	private BigDecimal nMenge;
	private BigDecimal nOffeneMenge;
	private BigDecimal nGelieferteMenge;
	private Integer auftragIId;
	private Integer artikelIId;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public String getAuftragCNr() {
		return auftragCNr;
	}

	public void setAuftragCNr(String auftragCNrI) {
		this.auftragCNr = auftragCNrI;
	}

	public String getCKundenname() {
		return cKundenname;
	}

	public void setCKundenname(String cKundennameI) {
		this.cKundenname = cKundennameI;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBezI) {
		this.cBez = cBezI;
	}

	public Timestamp getTUebersteuerterLiefertermin() {
		return tUebersteuerterLiefertermin;
	}

	public void setTUebersteuerterLiefertermin(
			Timestamp tUebersteuerterLiefertermin) {
		this.tUebersteuerterLiefertermin = tUebersteuerterLiefertermin;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMengeI) {
		this.nMenge = nMengeI;
	}

	public BigDecimal getNOffeneMenge() {
		return nOffeneMenge;
	}

	public void setNOffeneMenge(BigDecimal nOffeneMengeI) {
		this.nOffeneMenge = nOffeneMengeI;
	}

	public BigDecimal getNGelieferteMenge() {
		return nGelieferteMenge;
	}

	public void setNGelieferteMenge(BigDecimal nOffeneRahmenMenge) {
		this.nGelieferteMenge = nOffeneRahmenMenge;
	}

}
