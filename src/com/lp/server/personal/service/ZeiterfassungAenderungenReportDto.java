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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lp.server.partner.service.IdValueDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Pair;

public class ZeiterfassungAenderungenReportDto implements Serializable {

	private static final long serialVersionUID = -3430806293853377129L;

	//Item-Liste mit Von- und Nach-Dto
	private List<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>> items;
	
	//Parameter
	private IdValueDto person;
	private Timestamp zeitbuchungenVon;
	private Timestamp zeitbuchungenBis;
	private Timestamp aenderungenVon;
	private Timestamp aenderungenBis;
	private Boolean mitInserts;
	private Boolean mitUpdates;
	private Boolean mitDeletes;
	
	//Schl&uuml;sselw&ouml;rter, die auf &Auml;nderungen &uuml;berpr&uuml;ft werden sollen
	private List<String> keys;
	private List<String> operationen;
	private Integer sortierungsart;

	public static final int REPORT_AENDERUNGEN_SORTIERUNG_ZEITBUCHUNGEN = 0;
	public static final int REPORT_AENDERUNGEN_SORTIERUNG_AENDERUNGSZEIT = 1;
	public static final int REPORT_AENDERUNGEN_SORTIERUNG_ENTITIES = 2;
	
	public ZeiterfassungAenderungenReportDto() {
		items = new ArrayList<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>>();
		keys = new ArrayList<String>();
		operationen = new ArrayList<String>();
		sortierungsart = REPORT_AENDERUNGEN_SORTIERUNG_ZEITBUCHUNGEN;
		
		keys.add("TZeit");
		keys.add("TaetigkeitIId");
		keys.add("IBelegartid");
		keys.add("IBelegartpositionid");
		keys.add("ArtikelIId");
		
		operationen.add(ZeiterfassungReportFac.REPORT_AENDERUNGEN_OP_INSERT);
		operationen.add(ZeiterfassungReportFac.REPORT_AENDERUNGEN_OP_UPDATE);
		operationen.add(ZeiterfassungReportFac.REPORT_AENDERUNGEN_OP_DELETE);
	}

	public List<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>> getItems() {
		return items;
	}

	public void setItems(List<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>> items) {
		this.items = items;
	}

	public IdValueDto getPerson() {
		return person;
	}

	public void setPerson(IdValueDto person) {
		this.person = person;
	}

	public Timestamp getZeitbuchungenVon() {
		return zeitbuchungenVon;
	}

	public void setZeitbuchungenVon(Timestamp tVon) {
		this.zeitbuchungenVon = tVon;
	}

	public Timestamp getZeitbuchungenBis() {
		return zeitbuchungenBis;
	}

	public void setZeitbuchungenBis(Timestamp tBis) {
		this.zeitbuchungenBis = tBis;
	}

	public Timestamp getAenderungenVon() {
		return aenderungenVon;
	}

	public void setAenderungenVon(Timestamp tVon) {
		this.aenderungenVon = tVon;
	}

	public Timestamp getAenderungenBis() {
		return aenderungenBis;
	}

	public void setAenderungenBis(Timestamp tBis) {
		this.aenderungenBis = tBis;
	}

	public Boolean getMitInserts() {
		return mitInserts;
	}

	public void setMitInserts(Boolean mitInserts) {
		this.mitInserts = mitInserts;
	}

	public Boolean getMitUpdates() {
		return mitUpdates;
	}

	public void setMitUpdates(Boolean mitUpdates) {
		this.mitUpdates = mitUpdates;
	}

	public Boolean getMitDeletes() {
		return mitDeletes;
	}

	public void setMitDeletes(Boolean mitDeletes) {
		this.mitDeletes = mitDeletes;
	}

	public List<String> getKeys() {
		return keys;
	}

	public List<String> getOperationen() {
		return operationen;
	}

	public void removeOperation(String op) {
		operationen.remove(op);
	}
	
	public void setSortierungsart(Integer sortierungsart) {
		this.sortierungsart = sortierungsart;
	}
	
	public Integer getSortierungsart() {
		return sortierungsart;
	}
	
	public void sortItems() {
		if (sortierungsart == REPORT_AENDERUNGEN_SORTIERUNG_ZEITBUCHUNGEN) {
			sortItemsComparingEntitiesPerDay();
			//sortItemsComparingTZeit();
		} else if (sortierungsart == REPORT_AENDERUNGEN_SORTIERUNG_AENDERUNGSZEIT) {
			sortItemsComparingAenderungszeit();
		} else if (sortierungsart == REPORT_AENDERUNGEN_SORTIERUNG_ENTITIES) {
			sortItemsComparingEntitiesPerDay();
		}
	}

	/**
	 * Sortierung nur nach Zeit der Buchung
	 */
	private void sortItemsComparingTZeit() {
		Collections.sort(items, 
				new Comparator<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>>() {

					@Override
					public int compare(
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair1,
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair2) {
						
						Timestamp tZeitPair1 = pair1.getValue().getZeit() != null ? 
								pair1.getValue().getZeit() : pair1.getKey().getZeit();
						Timestamp tZeitPair2 = pair2.getValue().getZeit() != null ? 
								pair2.getValue().getZeit() : pair2.getKey().getZeit();
								
						int iCompare = tZeitPair1.before(tZeitPair2) ? -1 : (tZeitPair1.after(tZeitPair2) ? 1 : 0);
						
						if(iCompare != 0) {
							return iCompare;
						}
								
						return compareItemsAenderungszeit(pair1, pair2);
					}
			
		});
	}
	
	/**
	 * Sortierung nach dem &Auml;nderungszeitpunkt
	 */
	private void sortItemsComparingAenderungszeit() {
		Collections.sort(items, 
				new Comparator<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>>() {

					@Override
					public int compare(
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair1,
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair2) {
						
						return compareItemsAenderungszeit(pair1, pair2);
					}
			
		});
	}
	
	private int compareItemsAenderungszeit(
			Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair1,
			Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair2) {
		Timestamp tZeitPair1 = pair1.getKey().getZeitAendern();
		Timestamp tZeitPair2 = pair2.getKey().getZeitAendern();
				
		return tZeitPair1.before(tZeitPair2) ? -1 : (tZeitPair1.after(tZeitPair2) ? 1 : 0);
	}
	 
	/**
	 * Sortiert die Elemente aufsteigend nach den Tagen der Zeitbuchungen.
	 * In der zweiten Ebene wird nach gleichen Entities und &Auml;nderungszeitpunkt
	 * sortiert.  
	 */
	private void sortItemsComparingEntitiesPerDay() {
		Collections.sort(items, 
				new Comparator<Pair<ZeiterfassungAenderungenItemDto,ZeiterfassungAenderungenItemDto>>() {

					@Override
					public int compare(
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair1,
							Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto> pair2) {
						
						Timestamp tZeitPair1 = pair1.getValue().getZeit() != null ? 
								pair1.getValue().getZeit() : pair1.getKey().getZeit();
						Timestamp tZeitPair2 = pair2.getValue().getZeit() != null ? 
								pair2.getValue().getZeit() : pair2.getKey().getZeit();
								
						SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
						if(fmt.format(tZeitPair1).equals(fmt.format(tZeitPair2))) {
							int iCompare = pair1.getKey().getEntityId().compareTo(pair2.getKey().getEntityId());
							if(iCompare != 0) {
								return iCompare;
							}
							return compareItemsAenderungszeit(pair1, pair2);
						}
						return tZeitPair1.before(tZeitPair2) ? -1 : (tZeitPair1.after(tZeitPair2) ? 1 : 0);
					}
			
		});
	}
}
