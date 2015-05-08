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
package com.lp.server.finanz.service;

import java.io.Serializable;



public class PrintKontoblaetterModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum EnumSortOrder  {
		SORT_UNDEFINED,
		SORT_NACH_DATUM, 
		SORT_NACH_BELEG,
		SORT_NACH_AUSZUG 
	} ;

	private java.sql.Timestamp tVon ;
	private java.sql.Timestamp tBis ;
	private java.sql.Timestamp tVonReport ;
	private java.sql.Timestamp tBisReport ;
	
	private String geschaeftsjahrS ;
	private Integer geschaeftsjahr ;
	private boolean enableSaldo ;
	private Integer periodeImGJ ;
	
	private EnumSortOrder sortOrder ;
	private EnumSortOrder defaultSortOrder ;
	
	private Integer kontoIId ;
	private String vonKontoNr ;
	private String bisKontoNr ;
	private String kontotypCNr ;
	
	private boolean druckInMandantenWaehrung;
	private boolean sortiereNachAZK;
	
	public PrintKontoblaetterModel() {
	}
	
	public PrintKontoblaetterModel(java.sql.Timestamp tVon, java.sql.Timestamp tBis, String geschaeftsjahr) {			
		settVon(tVon) ;
		settBis(tBis) ;
		setGeschaeftsjahrString(geschaeftsjahr) ;
	}
	
	public String getKontotypCNr() {
		return kontotypCNr;
	}

	public void setKontotypCNr(String kontotypCNr) {
		this.kontotypCNr = kontotypCNr;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getVonKontoNr() {
		return vonKontoNr;
	}

	public void setVonKontoNr(String vonKontoNr) {
		this.vonKontoNr = vonKontoNr;
	}

	public String getBisKontoNr() {
		return bisKontoNr;
	}

	public void setBisKontoNr(String bisKontoNr) {
		this.bisKontoNr = bisKontoNr;
	}

	public java.sql.Timestamp gettVon() {
		return tVon;
	}
	
	/**
	 * Das Beginndatum der Berechnung/Ausdruck setzen
	 * @param tVon
	 */
	public void settVon(java.sql.Timestamp tVon) {
		this.tVon = tVon;
	}
	
	public java.sql.Timestamp gettBis() {
		return tBis;
	}

	public void settBis(java.sql.Timestamp tBis) {
		this.tBis = tBis;
	}
	
	public String getGeschaeftsjahrString() {
		return geschaeftsjahrS;
	}
	
	public Integer getGeschaeftsjahr() {
		return geschaeftsjahr ;
	}
	
	public void setGeschaeftsjahrString(String geschaeftsjahrS) {
		this.geschaeftsjahrS = geschaeftsjahrS;
		if(this.geschaeftsjahrS != null) {
			try {
				this.geschaeftsjahr = new Integer(this.geschaeftsjahrS) ;
			} catch(NumberFormatException e) {
				this.geschaeftsjahr = null ;
			}				
		} else {
			this.geschaeftsjahr = null ;
		}
	}
	

	public boolean isEnableSaldo() {
		return enableSaldo;
	}

	public void setEnableSaldo(boolean enableSaldo) {
		this.enableSaldo = enableSaldo;
	}

	public boolean isSortDatum() {
		return sortOrder == EnumSortOrder.SORT_NACH_DATUM ;
	}

	public void setSortDatum(boolean sortDatum) {
		if(sortDatum) {
			sortOrder = EnumSortOrder.SORT_NACH_DATUM ;
		}
	}

	public boolean isSortAuszug() {
		return sortOrder == EnumSortOrder.SORT_NACH_AUSZUG ;
	}

	public void setSortAuszug(boolean sortAuszug) {
		if(sortAuszug) {
			sortOrder = EnumSortOrder.SORT_NACH_AUSZUG ;
		}
	}

	public boolean isSortBeleg() {
		return sortOrder == EnumSortOrder.SORT_NACH_BELEG ;
	}

	public void setSortBeleg(boolean sortBeleg) {
		if(sortBeleg) {
			sortOrder = EnumSortOrder.SORT_NACH_BELEG ;
		}
	}
		
	public void setSortOrder(boolean sortDatum, boolean sortBeleg) {
		/* 
		 * Furchtbarer Krimskams.
		 * 
		 * Damit die Umstellung auf Combobox-Darstellung leichter geht,
		 * hier eine zentrale Methode f&uuml;r die Altlasten.
		 * 
		 * Fr&uuml;her gab es wohl "SortDatum" und dann "SortBeleg". Irgendwann
		 * kam "SortAuszug" hinzu, das wurde aber nicht explizit angegeben,
		 * sondern dadurch erreicht, dass weder SortDatum noch SortBeleg
		 * gesetzt wurden.
		 * 
		 */
		if(sortDatum) setSortDatum(true) ;
		if(sortBeleg) setSortBeleg(true) ;
		
		if(!sortDatum && !sortBeleg) setSortAuszug(true) ;
	}
	

	public void setSortOrder(EnumSortOrder newSortOrder) {
		sortOrder = newSortOrder ;
	}

	public EnumSortOrder getSortOrder() {
		return sortOrder ;
	}
	
	public String getSortOrderParameterString() {
		return getSorderOrderParameterString(getSortOrder()) ;
	}

	public String getSorderOrderParameterString(EnumSortOrder order) {
		if(order == EnumSortOrder.SORT_NACH_DATUM) return "Datum" ;
		if(order == EnumSortOrder.SORT_NACH_BELEG) return "Beleg" ;
		if(order == EnumSortOrder.SORT_NACH_AUSZUG) return "Auszug" ;
		
		return "Nicht definiert" ;		
	}

	public EnumSortOrder getDefaultSortOrder() {
		return defaultSortOrder;
	}

	public void setDefaultSortOrder(EnumSortOrder defaultSortOrder) {
		this.defaultSortOrder = defaultSortOrder;
	}

	public Integer getPeriodeImGJ() {
		return periodeImGJ;
	}

	public void setPeriodeImGJ(Integer periodeImGJ) {
		this.periodeImGJ = periodeImGJ;
	}

	public java.sql.Timestamp gettVonReport() {
		return tVonReport;
	}

	public void settVonReport(java.sql.Timestamp tVonReport) {
		this.tVonReport = tVonReport;
	}

	public java.sql.Timestamp gettBisReport() {
		return tBisReport;
	}

	public void settBisReport(java.sql.Timestamp tBisReport) {
		this.tBisReport = tBisReport;
	}

	public void setDruckInMandantenWaehrung(boolean druckInMandantenWaehrung) {
		this.druckInMandantenWaehrung = druckInMandantenWaehrung;
	}

	public boolean isDruckInMandantenWaehrung() {
		return druckInMandantenWaehrung;
	}

	public boolean isSortiereNachAZK() {
		return sortiereNachAZK;
	}

	public void setSortiereNachAZK(boolean sortiereNachAZK) {
		this.sortiereNachAZK = sortiereNachAZK;
	}	
}
