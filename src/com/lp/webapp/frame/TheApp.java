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
package com.lp.webapp.frame;

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
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/06 07:53:31 $
 */
public class TheApp {

	// diveres cmd-const. alle Webapps
	static public final String CMD_WAP_SHOWMENUE = "wap_showmenue";
	static public final String CMD_WAP_SHOWLOGIN = "wap_showlogin";
	static public final String CMD_WAP_DOLOGIN = "wap_do_login";
	static public final String CMD_WAP_ZEIT_BUCHEN = "wap_zeit_buchen";

	// fuer Zeiterfassung
	static public final String CMD_ZE_BDESTATION = "bdestation";
	static public final String CMD_ZE_BDESTATION2 = "bdestation2";
	static public final String CMD_ZE_BDESTATION3 = "bdestation3";
	static public final String CMD_ZE_BDESTATION3GUTSCHLECHT = "bdestation3gutschlecht";
	static public final String CMD_ZE_BDESTATION4 = "bdestation4";
	static public final String CMD_ZE_MECS_PERSSTAMM = "MECS_PERSSTAMM";
	static public final String CMD_ZE_MECS_SALDO = "MECS_SALDO";
	static public final String CMD_ZE_MECS_AUSWEISE = "MECS_AUSWEISE";
	static public final String CMD_ZE_MECS_ERLAUBTETAETIGKEITEN = "MECS_ERLAUBTETAETIGKEITEN";
	static public final String CMD_ZE_MECS_ZEITBUCHEN = "MECS_ZEITBUCHEN";
	static public final String CMD_ZE_MECS_ONLCHECK = "MECS_ONLCHECK";
	static public final String CMD_ZE_MECS_ONLINECHECK_ABL = "MECS_ONLINECHECK_ABL";
	static public final String CMD_ZE_MECS_ZEITBUCHENFINGERPRINT = "MECS_ZEITBUCHENFINGERPRINT";
	static public final String CMD_ANWESENHEITSLITE = "anwesenheitsliste";
	static public final String CMD_ZE_QUICKZEITERFASSUNG = "quickzeiterfassung";
	static public final String CMD_ZE_QUICKZE = "quickze";
	static public final String CMD_ZE_RECHNERSTART1 = "rechnerstart1";

	// Zutritt
	static public final String CMD_ZU_MECS_PARAM = "MECS_PARAM";
	static public final String CMD_ZU_MECS_AUSWEISE_ZUTRITT = "MECS_AUSWEISE_ZUTRITT";
	static public final String CMD_ZU_MECS_ZUTRITT = "MECS_ZUTRITT";
	static public final String CMD_ZU_MECS_ZUTRITT_ONLINE_CHECK = "MECS_ZUTRITT_ONLINE_CHECK";
	static public final String CMD_ZU_MECS_RELAIS = "MECS_RELAIS";
	static public final String CMD_ZU_MECS_TERMINAL = "MECS_TERMINAL";
	static public final String CMD_ZU_MECS_LOG = "MECS_LOG";
	static public final String CMD_ZU_MECS_ZUTRITT_EVENTS = "MECS_ZUTRITT_EVENTS";
	static public final String CMD_ZU_MECS_MAXTRANSNR = "MECS_MAXTRANSNR";
	// FINGERPRINT
	static public final String CMD_ZU_MECS_TEMPLATES = "MECS_TEMPLATES";

	// diveres subcmd-const. alle Webapps

	// diveres const. alle Webapps
	static public final String AUSWEISNR = "ausweisnr";
	static public final String BUCHUNGSZEIT = "buzeit";
	static public final String BUCHUNGSART = "buart";
	static public final String ERROR_MSG = "error_msg";
	static public final String KOMMEN = "ko";
	static public final String UNTERBRECHEN = "un";
	static public final String GEHEN = "ge";

	private TheApp() {
		// nothing here
	}
}
