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
 package com.lp.server.stueckliste.ejbfac;

import java.util.List;

import com.lp.service.StklImportSpezifikation;

/**
 * Dieses Interface stellt Methoden zur Verf&uuml;gung, um nach
 * Artikeln anhand eines Feldinhaltes zu suchen. Auch das Verhalten bei einem Fund wird
 * hier definiert.
 * @author robert
 *
 */
public interface IImportColumnQueryBuilder {

	public static final String Artikel = "artikel";
	public static final String ArtikelSpr = "artikelspr";
	public static final String KundeSoko = "kundesoko";

	public static final String JOIN_ARTIKELSPR = "LEFT OUTER JOIN artikel.artikelsprset AS " + ArtikelSpr;
	public static final String JOIN_KUNDESOKO = "LEFT OUTER JOIN artikel.kundesokoset AS " + KundeSoko;
	
	/**
	 * 
	 * @return true wenn ein Treffer dieser Spalte 100%ige &uuml;bereinstimmung bedeuted.
	 */
	public boolean isTotalMatch();

	/**
	 * Wird ein JOIN im FROM-Teil des Querys ben&ouml;tigt, muss dieser
	 * hier zur&uuml;ckgegeben werden. Bitte die static Strings in dieser Klasse mit dem
	 * Anfrang JOIN_* verwenden und erweitern wenn zus&auml;tzliche JOINs hinzugef&uuml;gt
	 * werden.
	 * @return der String, welcher an die FROM-Bedingung des Querys angehaengt wird, oder
	 * <code>null</code>, wenn kein JOIN n&ouml;tig ist.
	 */
	public String buildJoinQuery();
	
	/**
	 * 
	 * @param columnValue der Text der Spalte
	 * @param spez die Spezifikation des Imports
	 * @return der String, welcher an die WHERE-Bedingung des Querys angehaengt wird.
	 */
	public String buildWhereQuery(String columnValue, StklImportSpezifikation spez);
	
	/**
	 * Findet man mit mehrere Artikel, sind diese
	 * ColumnTypen f&uuml;r eine detailiertere Suche zus&auml;tzlich zu verwenden.
	 * @return die Liste der ColumnTypen oder <code>null</code> wenn eine detailiertere Suche
	 * nicht verf&uuml;gbar ist.
	 */
	public List<String> getDeeperColumnQueryBuilders();
	
}
