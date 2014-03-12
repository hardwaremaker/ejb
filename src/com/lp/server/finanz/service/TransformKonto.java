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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.lp.server.finanz.ejbfac.FinanzReportFacBean;
import com.lp.server.util.DefaultFieldnameIndexer;
import com.lp.server.util.IFieldnameIndexer;
import com.lp.util.EJBExceptionLP;


public class TransformKonto  implements Serializable  {	
	private static final long serialVersionUID = 1L;

	private ParseBooleanShort booleanParser = new ParseBooleanShort() ;
	private ParseNoneEmptySqlDateFromGMTString noneEmptyDateParser = new ParseNoneEmptySqlDateFromGMTString() ;
	private ParseSqlDateFromGMTString dateParser = new ParseSqlDateFromGMTString() ;
	private ParseNoneEmptyString noneEmptyStringParser = new ParseNoneEmptyString() ;

	private IFieldnameIndexer fieldIndex = new DefaultFieldnameIndexer(FinanzReportFacBean.reportKontenListeIndexer) ;
	
	protected TransformKonto() {		
	}
	
	public void setFieldIndexer(IFieldnameIndexer newFieldNameIndexer) {
		fieldIndex = newFieldNameIndexer ;
	}

	/**
	 * Die "Kopfzeile" der CSV Datei analysieren. Aus ihr werden die Spaltennamen entnommen
	 * und in dieser Reihenfolge die Spaltennummern ermittelt und der FieldIndexer gesetzt.
	 * 
	 * @param csvFields
	 */
	public void buildFieldIndexer(String[] csvFields) {
		Map<String, Integer> map = new HashMap<String, Integer>() ;
		
		for (int i = 0; i < csvFields.length; i++) {
			String s =  csvFields[i].trim() ;
			if(s.length() == 0) continue ;
			
			map.put(s, i) ;
		}
		
		fieldIndex.setMap(map) ;
	}

	
	protected String getValue(String[] csvFields, String fieldname) {
		Integer index = fieldIndex.getIndex(fieldname) ;
		if(index >= csvFields.length)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ANZAHL_FELDER, 
					index.toString() + "/" + String.valueOf(csvFields.length)) ;
		return csvFields[index] ;
	}


	public CsvKonto getKonto(String[] csvFields) throws EJBExceptionLP {
		if(null == csvFields) 
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ANZAHL_FELDER, new IllegalArgumentException("null")) ;
		if(csvFields.length < 20) 
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ANZAHL_FELDER,
					new IllegalArgumentException(String.valueOf(csvFields.length))) ;
	
		CsvKonto csvKonto = createDefaultKonto() ;
		csvKonto.setCNr(noneEmptyStringParser.parse(getValue(csvFields, "F_KONTONUMMER"), "F_KONTONUMMER")) ;
		csvKonto.setCBez(noneEmptyStringParser.parse(getValue(csvFields, "F_BEZEICHNUNG"), "F_BEZEICHNUNG")) ;
		csvKonto.setKontoartCNr(noneEmptyStringParser.parse(getValue(csvFields, "F_KONTOART"), "F_KONTOART")) ;
		csvKonto.setFinanzamtCnr(noneEmptyStringParser.parse(getValue(csvFields, "F_FINANZAMT"), "F_FINANZAMT")) ;
		csvKonto.setUvaartCnr(noneEmptyStringParser.parse(getValue(csvFields, "F_UVAART_CNR"), "F_UVAART")) ;
		csvKonto.setDGueltigvon(noneEmptyDateParser.parse(getValue(csvFields, "F_GUELTIG_VON"), "F_GUELTIG_VON")) ;

		csvKonto.setKontoCnrWeiterfuehrendust(getTrimmed(getValue(csvFields, "F_USTKONTONUMMER"))) ;
		csvKonto.setKontoCnrWeiterfuehrendskonto(getTrimmed(getValue(csvFields, "F_SKONTOKONTONUMMER"))) ;
		csvKonto.setSteuerkategorieCnr(getTrimmed(getValue(csvFields, "F_STEUERKATEGORIE"))) ;
		csvKonto.setErgebnisgruppeCnr(getTrimmed(getValue(csvFields, "F_ERGEBNISSGRUPPE"))) ;
		csvKonto.setKostenstelleCnr(getTrimmed(getValue(csvFields, "F_KOSTENSTELLE"))) ;
		csvKonto.setKontoCnrWeiterfuehrendbilanz(getTrimmed(getValue(csvFields, "F_WEITERFUEHRENDEBILANZ_KONTONUMMER"))) ;
		csvKonto.setDGueltigbis(dateParser.parse(getValue(csvFields, "F_GUELTIG_BIS"), "F_GUELTIG_BIS")) ;
		csvKonto.setBAutomeroeffnungsbuchung(booleanParser.parse(getValue(csvFields, "F_AUTOMATISCHE_EROEFFNUNGSBUCHUNG"), "F_AUTOMATISCHE_EROEFFNUNGSBUCHUNG")) ;
		csvKonto.setBAllgemeinsichtbar(booleanParser.parse(getValue(csvFields, "F_ALLGEMEIN_SICHTBAR"), "F_ALLGEMEIN_SICHTBAR")) ;
		csvKonto.setBManuellbebuchbar(booleanParser.parse(getValue(csvFields, "F_MANUELL_BEBUCHBAR"), "F_MANUELL_BEBUCHBAR")) ;
		csvKonto.setBVersteckt(booleanParser.parse(getValue(csvFields, "F_VERSTECKT"), "F_VERSTECKT")) ;
		csvKonto.setCsortierung(getTrimmed(getValue(csvFields, "F_SORTIERUNG"))) ;
		
		return csvKonto ;
	}	
	
	protected CsvKonto createDefaultKonto() {
		CsvKonto konto = new CsvKonto() ;
		return konto ;
	}
	
	private String getTrimmed(String value) {
		if(null == value) return "" ;
		return value.trim();
	}
}
