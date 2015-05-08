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
package com.lp.server.inserat.service;

import java.io.Serializable;


/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 01.09.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/08/11 13:57:20 $
 */
public class ReportJournalInseratDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int KRIT_SORT_NACH_BELEGNUMMER = 0;
	public final static int KRIT_SORT_NACH_LIEFERANT = 1;
	public final static int KRIT_SORT_NACH_KUNDE = 2;
	public final static int KRIT_SORT_NACH_VERTRETER = 3;

	// Default Sortierung nach Belegnummer
	public int iSortierung = KRIT_SORT_NACH_BELEGNUMMER;
	
	public final static int KRIT_OPTION_STICHTAG_NICHT_BESTELLT = 0;
	public final static int KRIT_OPTION_STICHTAG_NICHT_ERSCHIENEN = 1;
	public final static int KRIT_OPTION_STICHTAG_NICHT_VERRECHNET = 2;
	public final static int KRIT_OPTION_STICHTAG_OHNE_ER = 3;
	public int iOptionStichtag = KRIT_OPTION_STICHTAG_NICHT_BESTELLT;
	
	public java.sql.Date dStichtag = null;
	
	public Integer personalIId = null;
	public java.sql.Date dVon = null;
	public java.sql.Date dBis = null;
	public String sBelegnummerVon = null;
	public String sBelegnummerBis = null;
	public Integer kundeIId = null;
	public Integer lieferantIId = null;
}
