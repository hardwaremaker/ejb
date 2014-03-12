/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import com.lp.server.system.service.ReportJournalKriterienDto;

/**
 * <p>
 * Report Journal Auftrag: Fuer Offene und Alle.
 * <p>
 * Copyright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 02.10.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Victor Finder
 * @version $Revision: 1.4 $
 */
public class ReportAuftragJournalKriterienDto extends ReportJournalKriterienDto
		implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @deprecated MB: weg damit. das ist schon in der superklasse definiert.
	 */
	public final static int KRIT_SORT_NACH_VERTRETER = 2;

	/**
	 * @deprecated MB: weg damit. das ist schon in der superklasse definiert.
	 */
	private Integer vertreterIId;
	private boolean bNurErledigteAuftraege;

	/**
	 * 
	 * @deprecated MB: weg damit. das ist schon in der superklasse definiert.
	 * @return Integer
	 */
	public Integer getVertreterIId() {
		return this.vertreterIId;
	}

	/**
	 * 
	 * @deprecated MB: weg damit. das ist schon in der superklasse definiert.
	 * @param vertreterIId
	 *            Integer
	 */
	public void setVertrterIId(Integer vertreterIId) {
		this.vertreterIId = vertreterIId;
	}

	public boolean getBNurErledigteAngebote() {
		return this.bNurErledigteAuftraege;
	}

	public void setBNurErledigteAngebote(boolean bNurErledigteAngebote) {
		this.bNurErledigteAuftraege = bNurErledigteAngebote;
	}

	/**
	 * 
	 * @deprecated MB: weg damit. das ist eine Fehlerquelle. da fehlt jetzt
	 *             schon einiges.
	 * @param basiskritDtoI
	 *            ReportJournalKriterienDto
	 */
	public void befuelleBasiskrit(ReportJournalKriterienDto basiskritDtoI) {
		this.iSortierung = basiskritDtoI.iSortierung;
		this.bSortiereNachKostenstelle = basiskritDtoI.bSortiereNachKostenstelle;
		this.kostenstelleIId = basiskritDtoI.kostenstelleIId;
		this.dVon = basiskritDtoI.dVon;
		this.dBis = basiskritDtoI.dBis;
		this.sBelegnummerVon = basiskritDtoI.sBelegnummerVon;
		this.sBelegnummerBis = basiskritDtoI.sBelegnummerBis;
		this.kundeIId = basiskritDtoI.kundeIId;
	}
}
