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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lp.server.system.service.TheClientDto;
import com.lp.service.StklImportSpezifikation;

/**
 * Diese Klasse dient als Hilfsklasse f&uuml;r die Suche nach Artikeln des intelligenten
 * St&uuml;cklistenimports. Sie speichert die ben&ouml;tigten Variablen und stellt
 * Methoden f&uuml;r die Erstellung der verschiedenen Datenbank-Queries zur Verf&uuml;gung.
 * Dabei bedient sie sich bei den verschiedenen ImportColumnQueryBuilder.
 * In Unterklassen werden je nach Art der St&uuml;ckliste die Bedingungen f&uuml;r die
 * Suche angepasst.
 * 
 * @author andi
 *
 */
public abstract class StklImportSearchHelper {
	
	private Map<String, String> values;
	private StklImportSpezifikation spez;
	private String basicFrom;
	private String basicWhere;
	private TheClientDto theClientDto;
	protected Map<String, IImportColumnQueryBuilder> importColumns;
	
	protected List<String> columnPriorityOrder;

	protected StklImportSearchHelper(Map<String, String> values,
			StklImportSpezifikation spez, String queryFrom,
			String queryWhere, TheClientDto theClientDto) {

		this.values = values;
		this.spez = spez;
		this.basicFrom = queryFrom;
		this.basicWhere = queryWhere;
		this.theClientDto = theClientDto;
		initImportColumnsMap();
		initColumnPriorityOrder();
	}
	
	protected StklImportSearchHelper(StklImportSpezifikation spez, 
			TheClientDto theClientDto) {
		this(null, spez, null, null, theClientDto);
	}
	
	/**
	 * Initialsiert die columnPriorityOrder, die die Abfolge der Artikelsuche
	 * des intelligenten Stklimports beschreibt.
	 */
	protected abstract void initColumnPriorityOrder();
	
	/**
	 * Initialisiert die importColumns Map, mappt dabei die verschiedenen 
	 * QueryBuilder zur entsprechenden ImportColumn Bezeichnung.
	 */
	protected abstract void initImportColumnsMap();

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public void setSpez(StklImportSpezifikation spez) {
		this.spez = spez;
	}

	public void setBasicQueryFrom(String queryFrom) {
		this.basicFrom = queryFrom;
	}

	public void setBasicQueryWhere(String queryWhere) {
		this.basicWhere = queryWhere;
	}

	public void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public StklImportSpezifikation getSpez() {
		return spez;
	}

	public String getBasicQueryFrom() {
		return basicFrom;
	}

	public String getBasicQueryWhere() {
		return basicWhere;
	}

	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	public Map<String, IImportColumnQueryBuilder> getImportColumns() {
		return importColumns;
	}

	public List<String> getColumnPriorityOrder() {
		return columnPriorityOrder;
	}

	/**
	 * Setzt die WHERE Query zusammen. Zu der Basis WHERE Klausel wird der aktuelle
	 * QueryBuilder nach einer spezifischen WHERE Klausel abgefragt.
	 * 
	 * @param type ImportColumnTyp &uuml;ber den der richtige QueryBuilder gefunden wird.
	 * @return die WHERE Query; null, wenn der zugeh&ouml;rige QueryBuilder fehlt oder es
	 * keine WHERE Query gibt
	 */
	public List<String> getWhereQuery(String type) {
		IImportColumnQueryBuilder queryBuilder = getImportColumns().get(type);
		if(queryBuilder == null)
			return null;
		
		String where = queryBuilder.buildWhereQuery(
				getValues().get(type).replaceAll("'", "''"), getSpez());
		if(where == null)
			return null;
		
		List<String> wheres = new ArrayList<String>();
		wheres.add(getBasicQueryWhere());
		wheres.add(where);
		
		return wheres;
	}

	/**
	 * Setzt die FROM Query zusammen. Zu der Basis FROM Klausel wird der aktuelle
	 * QueryBuilder nach einem zus&auml;tzlichen JOIN abgefragt.
	 * 
	 * @param type ImportColumnTyp &uuml;ber den der richtige QueryBuilder gefunden wird.
	 * @return die FROM Query
	 */
	public List<String> getFromQuery(String type) {
		List<String> froms = new ArrayList<String>();
		froms.add(basicFrom);
		IImportColumnQueryBuilder queryBuilder = getImportColumns().get(type);
		
		if(queryBuilder != null) {
			String join = queryBuilder.buildJoinQuery();
			if(join != null) {
				froms.add(join);
			}
		}
		
		return froms;
	}
	
	public boolean canBuildQuery(String type) {
		String value = getValue(type);
		if (value == null || value.isEmpty())
			return false;
		
		if (getWhereQuery(type) == null)
			return false;
		
		return true;
	}
	
	public String getValue(String type) {
		return getValues().get(type);
	}
	
	public String buildQuery(String type) {
		List<String> froms = getFromQuery(type);
		List<String> wheres = getWhereQuery(type);
		return ImportQueryBuilder.buildQuery(froms, wheres);
	}
	
	public boolean hasDeeperColumnQuery(String type) {
		List<String> deeperColumnTypes = getImportColumns().get(type).getDeeperColumnQueryBuilders();
		if (deeperColumnTypes == null || deeperColumnTypes.isEmpty()) {
			return false;
		}
		return getValue(deeperColumnTypes.get(0)) != null;
	}
	
	public String buildDeeperColumnQuery(String type, List<Integer> artikelIds) {
		List<String> deeperColumnTypes = getImportColumns().get(type).getDeeperColumnQueryBuilders();
		String deeperColType = deeperColumnTypes.get(0);
		
		List<String> froms = getFromQuery(deeperColType);
		List<String> wheres = getWhereQuery(deeperColType);
		wheres.add(IImportColumnQueryBuilder.Artikel + ".i_id IN (" + StringUtils.join(artikelIds.iterator(), ",") + ")");
		
		return ImportQueryBuilder.buildQuery(froms, wheres);
	}
}
