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

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.server.system.pkgenerator.service.PKGeneratorFac;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Primary Key Generator</I>
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
public class PKGeneratorObj {
	private Context context;
	private PKGeneratorFac pkGenerator;

	public PKGeneratorObj() {
		try {
			context = new InitialContext();
			try {
				pkGenerator = (PKGeneratorFac) context.lookup("lpserver/PKGeneratorFacBean/remote");
			} catch (Exception e) {
				pkGenerator = (PKGeneratorFac) context.lookup("java:global/lpserver/ejb/PKGeneratorFacBean!com.lp.server.system.pkgenerator.service.PKGeneratorFac");
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, ex);
		}
	}

	/**
	 * Generiert einen neuen Primaerschluessel fuer eine Tabelle
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getNextPrimaryKey(String name) throws EJBExceptionLP {
		try {
			return pkGenerator.getNextPrimaryKey(name);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, ex);
		}
	}

	/**
	 * Holt den letzten vergebenen Primaerschluessel fuer eine Tabelle
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getLastPrimaryKey(String name) throws EJBExceptionLP {
		try {
			return pkGenerator.getLastPrimaryKey(name);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, ex);
		}
	}

	/**
	 * Eine Sequenz erzeugen, falls sie noch nicht existiert
	 * 
	 * @param name
	 *            String
	 * @throws RemoteException
	 */
	public void createSequenceIfNotExists(String name) throws RemoteException {
		pkGenerator.createSequenceIfNotExists(name);
	}
	
	public void createSequenceIfNotExists(String name, Integer defaultValue) throws RemoteException {
		pkGenerator.createSequenceIfNotExists(name, defaultValue);
	}
}
