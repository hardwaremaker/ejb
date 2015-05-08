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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.StklImportSpezifikation;

/**
 * Diese Klasse dient als Hilfsklasse f&uuml;r die Suche nach Artikeln des intelligenten
 * St&uuml;cklistenimports. Sie speichert die ben&ouml;tigten Variablen und stellt
 * Methoden f&uuml;r die Erstellung der verschiedenen Datenbank-Queries zur Verf&uuml;gung.
 * Dabei bedient sie sich bei den verschiedenen ImportColumnQueryBuilder.
 * In Unterklassen k&ouml;nnen je nach Art der St&uuml;ckliste die Bedingungen f&uuml;r die
 * Suche angepasst werden werden.
 * 
 * @author andi
 *
 */
public class StklImportSearchHelper {
	
	private Map<String, String> values;
	private StklImportSpezifikation spez;
	private String basicFrom;
	private String basicWhere;
	private TheClientDto theClientDto;
	private Map<String, IImportColumnQueryBuilder> importColumns;
	private Integer kundeIId;
	
	private List<String> columnPriorityOrder = Arrays.asList(
			StklImportSpezifikation.KUNDENARTIKELNUMMER,
			StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
			StklImportSpezifikation.ARTIKELNUMMER,
			StklImportSpezifikation.HERSTELLERBEZ,
			StklImportSpezifikation.SI_WERT,
			StklImportSpezifikation.BEZEICHNUNG,
			StklImportSpezifikation.BEZEICHNUNG1,
			StklImportSpezifikation.BEZEICHNUNG2,
			StklImportSpezifikation.BEZEICHNUNG3,
			StklImportSpezifikation.BAUFORM,
			StklImportSpezifikation.KOMMENTAR);

	public StklImportSearchHelper(Map<String, String> values,
			StklImportSpezifikation spez, String queryFrom,
			String queryWhere, TheClientDto theClientDto, Integer kundeIId) {

		this.values = values;
		this.spez = spez;
		this.basicFrom = queryFrom;
		this.basicWhere = queryWhere;
		this.theClientDto = theClientDto;
		this.kundeIId = kundeIId;
		initImportColumnsMap();
	}
	
	public StklImportSearchHelper(StklImportSpezifikation spez, 
			TheClientDto theClientDto, Integer kundeIId) {
		this(null, spez, null, null, theClientDto, kundeIId);
	}
	
	/**
	 * Mappt die verschiedenen QueryBuilder zur entsprechenden ImportColumn
	 */
	private void initImportColumnsMap() {
		importColumns = new HashMap<String, IImportColumnQueryBuilder>();
		
		importColumns.put(FertigungsStklImportSpezifikation.KUNDENARTIKELNUMMER,
				new ImportColumnQueryBuilderKndArtikelNr());
		importColumns.put(StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
				new ImportColumnQueryBuilderHerstellernummer());
		importColumns.put(StklImportSpezifikation.ARTIKELNUMMER,
				new ImportColumnQueryBuilderArtikelnr());
		importColumns.put(StklImportSpezifikation.HERSTELLERBEZ,
				new ImportColumnQueryBuilderHerstellerBez());
		importColumns.put(StklImportSpezifikation.SI_WERT,
				new ImportColumnQueryBuilderSIWert());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG1,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG2,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG3,
				new ImportColumnQueryBuilderBezeichnung());	
	}

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
		
		if(type.equals(StklImportSpezifikation.KUNDENARTIKELNUMMER) && kundeIId != null) {
			String whereKundeNr = ((ImportColumnQueryBuilderKndArtikelNr)getImportColumns()
					.get(type)).buildWhereKundeIidQuery(kundeIId);
			wheres.add(whereKundeNr);
		}

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
}
