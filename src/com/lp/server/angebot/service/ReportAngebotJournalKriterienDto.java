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
package com.lp.server.angebot.service;

import java.io.Serializable;

import com.lp.server.system.service.ReportJournalKriterienDto;

/**
 * <p>
 * Report Journal Angebot: Fuer Offene und Alle.
 * <p>
 * Copyright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 02.10.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class ReportAngebotJournalKriterienDto extends ReportJournalKriterienDto
		implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean bNurErledigteAngebote;
	private boolean bMitDetails;
	
	private boolean bSortiertNachNachfassterminNachrangig=false;
	

	public boolean isBSortiertNachNachfassterminNachrangig() {
		return bSortiertNachNachfassterminNachrangig;
	}

	public void setBSortiertNachNachfassterminNachrangig(
			boolean bSortiertNachNachfassterminNachrangig) {
		this.bSortiertNachNachfassterminNachrangig = bSortiertNachNachfassterminNachrangig;
	}

	public boolean getBNurErledigteAngebote() {
		return this.bNurErledigteAngebote;
	}

	public void setBNurErledigteAngebote(boolean bNurErledigteAngebote) {
		this.bNurErledigteAngebote = bNurErledigteAngebote;
	}
	
	public boolean getBMitDetails(){
		return this.bMitDetails;
	}
	
	public void setBMitDetails(boolean bMitDetails){
		this.bMitDetails = bMitDetails;
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
		this.provisionsempfaengerIId = basiskritDtoI.provisionsempfaengerIId;
	}
}
