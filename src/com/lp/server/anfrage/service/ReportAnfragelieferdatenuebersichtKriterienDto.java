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
package com.lp.server.anfrage.service;

import java.io.Serializable;
import java.sql.Date;

/**
 * <p>
 * Kriterien fuer Anfrage Lieferdatenuebersicht Report.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch; 30.09.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/05 09:35:13 $
 */
public class ReportAnfragelieferdatenuebersichtKriterienDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.sql.Date dVon = null;
	private java.sql.Date dBis = null;
	private String artikelCNrVon = null;
	private String artikelCNrBis = null;
	private boolean bNurProjekt = false;
	private String projektCBez = null;
	private boolean bMitLiefermengenNull = false;
	private boolean bSortierungNachProjekt = false;

	public Date getDVon() {
		return this.dVon;
	}

	public void setDVon(Date dVonI) {
		this.dVon = dVonI;
	}

	public Date getDBis() {
		return this.dBis;
	}

	public void setDBis(Date dBisI) {
		this.dBis = dBisI;
	}

	public String getArtikelCNrVon() {
		return this.artikelCNrVon;
	}

	public void setArtikelCNrVon(String artikelCNrVonI) {
		this.artikelCNrVon = artikelCNrVonI;
	}

	public String getArtikelCNrBis() {
		return this.artikelCNrBis;
	}

	public void setArtikelCNrBis(String artikelCNrBisI) {
		this.artikelCNrBis = artikelCNrBisI;
	}

	public boolean getBNurProjekt() {
		return this.bNurProjekt;
	}

	public void setBNurProjekt(boolean bNurProjektI) {
		this.bNurProjekt = bNurProjektI;
	}

	public boolean getBMitLiefermengenNull() {
		return this.bMitLiefermengenNull;
	}

	public void setBMitLiefermengenNull(boolean bMitLiefermengenNullI) {
		this.bMitLiefermengenNull = bMitLiefermengenNullI;
	}

	public boolean getBSortierungNachProjekt() {
		return this.bSortierungNachProjekt;
	}

	public void setBSortierungNachProjekt(boolean bSortierungNachProjektI) {
		this.bSortierungNachProjekt = bSortierungNachProjektI;
	}

	public String getProjektCBez() {
		return this.projektCBez;
	}

	public void setProjektCBez(String projektCBezI) {
		this.projektCBez = projektCBezI;
	}
}
