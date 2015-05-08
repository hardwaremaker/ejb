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
package com.lp.server.system.pkgenerator.bl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.system.pkgenerator.ejbfac.BNGeneratorFacLocal;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Belegnummern-Generator</I>
 * </p>
 * <p>
 * Generiert Belegnummern anhand definierbarer Nummernkreise
 * </p>
 * <p>
 * Basierend auf dem Primary Key Generator
 * </p>
 * <p>
 * Zur Zeit implementierte Funktionalitaeten:
 * </p>
 * <p>
 * Eigener Nummernkreis je Mandant und Geschaeftsjahr
 * </p>
 * <p>
 * Mehrere verschiedene Nummernkreise fuer eine DB-Tabelle
 * </p>
 * <p>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>25. 10. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class BelegnummerGeneratorObj {
	private Context context;
	private BNGeneratorFacLocal bnGeneratorLocal;

	public BelegnummerGeneratorObj() {
		try {
			context = new InitialContext();
			bnGeneratorLocal = (BNGeneratorFacLocal) context
					.lookup("lpserver/BNGeneratorFacBean/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Liefert eine neue Belegnummer fuer ein beliebige Geschaeftsjahr
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer
	 * @return LpBelegnummer
	 * @throws EJBExceptionLP
	 */
	public LpBelegnummer getNextBelegNr(Integer geschaeftsjahr, String name,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		return bnGeneratorLocal.getNextBelegNr(geschaeftsjahr, name,
				mandantCNr, theClientDto);
	}
	
	/**
	 * Liefert eine neue Belegnummer fuer Buchungen in einem beliebigen Geschaeftsjahr
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer
	 * @return LpBelegnummer
	 * @throws EJBExceptionLP
	 */
	public LpBelegnummer getNextBelegNrFinanz(Integer geschaeftsjahr, String name,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		return bnGeneratorLocal.getNextBelegNrFinanz(geschaeftsjahr, name,
				mandantCNr, theClientDto);
	}

	/**
	 * Liefert eine neue Belegnummer fuer das aktuelle Geschaeftsjahr
	 * 
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return LpBelegnummer
	 */
	public LpBelegnummer getNextBelegNr(String name, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return bnGeneratorLocal.getNextBelegNr(name, mandantCNr, theClientDto);
	}

	/**
	 * Liefert die Anzahl der in einem Geschaeftsjahr fuer einen Mandanten
	 * erstellten Belege eines Moduls
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @return Integer
	 */
	public Integer getBelegAnzahl(Integer geschaeftsjahr, String name,
			String mandantCNr) {
		return bnGeneratorLocal
				.getBelegAnzahl(geschaeftsjahr, name, mandantCNr);
	}

	/**
	 * Liefert die Summe der erstellten Belege eines Moduls fuer einen Mandanten
	 * ueber alle Geschaeftsjahre
	 * 
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @return Integer
	 */
	public Integer getBelegAnzahl(String name, String mandantCNr) {
		return bnGeneratorLocal.getBelegAnzahl(name, mandantCNr);
	}

	/**
	 * Liefert die Summe der insgesamt fuer ein Modul erstellten Belege
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 */
	public Integer getBelegAnzahl(String name) {
		return bnGeneratorLocal.getBelegAnzahl(name);
	}

	/**
	 * Liefert eine neue Belegnummer fuer das aktuelle Geschaeftsjahr fuer
	 * Multiple Belegnummern innerhalb einer Tabelle
	 * 
	 * @param nameTabelle
	 *            String
	 * @param nameBeleg
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer 
	 * @throws EJBExceptionLP
	 * @return LpBelegnummer
	 */
	public LpBelegnummer getNextBelegNr(String nameTabelle, String nameBeleg,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		return bnGeneratorLocal.getNextBelegNr(nameTabelle, nameBeleg,
				mandantCNr, theClientDto);
	}

	/**
	 * Liefert eine neue Belegnummer fuer Multiple Belegnummern innerhalb einer
	 * Tabelle
	 * 
	 * @param geschaeftsjahr
	 *            String
	 * @param nameTabelle
	 *            String
	 * @param nameBeleg
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer 
	 * @throws EJBExceptionLP
	 * @return LpBelegnummer
	 */
	public LpBelegnummer getNextBelegNr(Integer geschaeftsjahr,
			String nameTabelle, String nameBeleg, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return bnGeneratorLocal.getNextBelegNr(geschaeftsjahr, nameTabelle,
				nameBeleg, mandantCNr, theClientDto);
	}

	public LpBelegnummerFormat getBelegnummernFormat(String mandantCNr)
			throws EJBExceptionLP {
		return bnGeneratorLocal.getBelegnummernFormat(mandantCNr);
	}
}
