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
package com.lp.server.util;

import com.lp.util.EJBExceptionLP;

public class Validator {
	
	/**
	 * Value darf nicht null sein
	 * 
	 * @param value der zu pruefende Wert
	 * @param nullParameterName ist der Name des Methoden-Parameters der null ist
	 * @throws EJBExceptionLP.FEHLER_PARAMETER_IS_NULL wenn value == null
	 */
	public static void notNull(Object value, String nullParameterName) throws EJBExceptionLP {
		if(value == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(nullParameterName + " == null")) ;
		}
	}

	/**
	 * Value einer KeyId darf nicht null sein
	 * 
	 * @param value der zu pruefende Wert
	 * @param nullParameterName ist der Name des Methoden-Parameters der null ist
	 * @throws EJBExceptionLP.FEHLER_PARAMETER_IS_NULL wenn value == null oder die dahinterliegende Id == null
	 */
	public static void notNull(BaseIntegerKey value, String nullParameterName) throws EJBExceptionLP {
		if(value == null || !value.isValid()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(nullParameterName + " == null")) ;
		}
	}

	/**
	 * Value darf nicht null sein
	 * 
	 * @param value der zu pruefende Wert
	 * @param errorMessage die Fehlernachricht
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static void notNullUserMessage(Object value, String errorMessage) throws EJBExceptionLP {
		if(value == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(errorMessage)) ;
		}
	}

	/**
	 * (String) Value darf weder null noch leer sein
	 * 
	 * @param value der zu pruefende String
	 * @param nullParameterName der Name des Parameters der auf null/leer gepr&uuml;ft werden soll
	 * @throws EJBExceptionLP wenn value == null oder value.trim().length() == 0
	 */
	public static void notEmpty(String value, String nullParameterName) throws EJBExceptionLP {
		if(value == null || value.trim().length() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(nullParameterName + " == null")) ;
		}
	}
	
	/**
	 * (String) Value darf weder null noch leer sein
	 * 
	 * @param value der zu pruefende String
	 * @param errorMessage die Fehlernachricht
	 * @throws EJBExceptionLP wenn value == null oder value.trim().length() == 0
	 */
	public static void notEmptyUserMessage(String value, String errorMessage) throws EJBExceptionLP {
		if(value == null || value.trim().length() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(errorMessage)) ;
		}
	}
	
	/**
	 * Betrachtet value als PK-Field und prueft dieses auf != null
	 * 
	 * @param value ist das zu pruefende PK Field
	 * @param nullParameterName der Name des Parameters
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static void pkFieldNotNull(Integer value, String nullParameterName) throws EJBExceptionLP {
		if(value == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
				new Exception(nullParameterName + " == null"));
		}
	}

	/**
	 * Betrachtet value als PK-Field und prueft dieses auf != null
	 * 
	 * @param value ist das zu pruefende PK Field
	 * @param errorMessage die Nachricht die ausgegeben werden soll
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static void pkFieldNotNullUserMessage(Integer value, String errorMessage) throws EJBExceptionLP {
		if(value == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
				new Exception(errorMessage));
		}
	}
	
	/**
	 * Betrachtet value als PK-Field und prueft dieses auf != null
	 * 
	 * @param value ist das zu pruefende PK Field
	 * @param nullParameterName der Name des Parameters
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static void pkFieldNotNull(String value, String nullParameterName) {
		if(value == null || value.trim().isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
				new Exception(nullParameterName + " == null"));
		}
	}

	/**
	 * Betrachtet value als PK-Field und prueft dieses auf != null
	 * 
	 * @param value ist das zu pruefende PK Field
	 * @param errorMessage die Nachricht die ausgegeben werden soll
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static void pkFieldNotNullUserMessage(String value, String errorMessage) {
		if(value == null || value.trim().isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
				new Exception(errorMessage));
		}
	}
	
	/**
	 * Betrachtet value als PK-Field und prueft dieses auf != null
	 * 
	 * @param key ist das zu pruefende PK Field, weder key noch 
	 * die key.id() darf null sein
	 * @param errorMessage die Nachricht die ausgegeben werden soll
	 * @throws EJBExceptionLP wenn value == null
	 */
	public static BaseIntegerKey pkFieldNotNull(
			BaseIntegerKey key, String parameterName) {
		if (key == null || !key.isValid()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(parameterName + " == null"));
		}
		
		return key;
	}
	
	public static void dtoNotNull(Object dto,  String dtoName) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception(dtoName + " == null"));
		}
	}
	
	/**
	 * Ueberprueft, ob die Entity gefunden wurde (!= null ist)
	 * 
	 * @param entity die zu pruefende Entity
	 * @param key ist der primary key der gesuchten Entity
	 * @throws EJBExceptionLP wenn die Entity nicht gefunden wurde
	 */
	public static void entityFound(Object entity, Integer key) {
		if(entity == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, key.toString());
		}
	}
	
	/**
	 * &Uuml;berpr&uuml;ft, ob die Entity gefunden wurde (!= null ist)
	 * 
	 * @param entity die zu pr&uuml;fende Entity
	 * @param key ist der primary key der gesuchten Entity
	 * @throws EJBExceptionLP wenn die Entity nicht gefunden wurde
	 */
	public static void entityFoundCnr(Object entity, String key) {
		if(entity == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, key);
		}
	}
	
	public static <T> T entityFound(HvOptional<T> entity, Integer key) {
		if (entity.isPresent()) return entity.get();
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, key.toString());
	}
	
	public static <T> T entityFound(HvOptional<T> entity, BaseIntegerKey key) {
		if (entity.isPresent()) return entity.get();
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, key.id().toString());
	}

	
	public static void pkFieldValid(BaseIntegerKey key, String nullParameterName) {
		if (!key.isValid()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(nullParameterName + " == null"));
		}
	}
}
