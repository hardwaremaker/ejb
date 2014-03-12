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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.lp.server.system.service.KostenstelleDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;

public class KontoImporter implements Serializable {
	private static final long serialVersionUID = 1L;

	private TransformKonto kontoTransformer = null  ;
	private List<CsvKonto> importOrder = new ArrayList<CsvKonto>() ;
	private FinanzamtDto[] finanzaemter = null ;
	
	private KontoImportStats stats ;

	private IKontoImporterBeanServices beanServices ;
	
	
	public KontoImporter(IKontoImporterBeanServices beanServices) {		
		this(beanServices, new TransformSachkonto()) ;
	}
	
	public KontoImporter(IKontoImporterBeanServices beanServices, TransformKonto transformer) {
		this.beanServices = beanServices ;
		kontoTransformer = transformer ;
		stats = new KontoImportStats() ;
	}
	
	public TransformKonto getTransformer() {
		return kontoTransformer ;
	}

	public KontoImportStats getStats() {
		return stats ;
	}
	
	public Integer getTotalRowCount() {
		return stats.getTotalRows() ;
	}
	
	public Integer getGoodRowCount() {
		return stats.getGoodRowCount() ;
	}
	
	public Integer getErrorRowCount() {
		return stats.getErrorRowCount() ;
	}
	
	public Integer getIgnoredRowCount() {
		return stats.getIgnoredRowCount() ;
	}
	
	public Integer getPossibleRowCount() {
		return importOrder == null ? 0 : importOrder.size() ;
	}

	public KontoImporterResult importCsvKontos(List<String[]> csvKontos) {
		KontoImporterResult result = checkImportCsvKontos(csvKontos) ;
		
		result.getMessages().addAll(importKontos()) ;
		return result ;
	}

	/**
	 * Prueft die csv-Daten auf syntaktische und lexikalische Korrektheit
	 * 
	 * @param csvKontos
	 * @return Eine (leere) Liste von Fehlermeldungen
	 */
	public KontoImporterResult checkImportCsvKontos(List<String[]> csvKontos) {
		HashMap<String, CsvKonto> kontos = new HashMap<String, CsvKonto>() ;
		List<EJBLineNumberExceptionLP> importErrors = new ArrayList<EJBLineNumberExceptionLP>() ;
		
		int linenumber = 1 ; // In der ersten Zeile ist der Header definiert
		int linesForImportCount = 0 ;

		stats.reset() ;
		
		for (String[] strings : csvKontos) {
			try {
				++linenumber ;
				if(isEmptyLine(strings)) continue ;
				++linesForImportCount ;
				
				final CsvKonto konto = kontoTransformer.getKonto(strings) ;

				if(existsKontoInImportMap(kontos, konto)) {
					stats.incrementErrorRowCount() ;
					throw new EJBLineNumberExceptionLP(
						linenumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_KONTONUMMER_BEREITS_VORHANDEN, 
								new ArrayList<Object>(){{add(konto.getCNr());}}, null)) ;
				}

				checkCsvKonto(konto) ;
				
				kontos.put(konto.getCNr(), konto) ;
			} catch(EJBExceptionLP e) {
				stats.incrementErrorRowCount() ;
				
				if(e instanceof EJBLineNumberExceptionLP) {
					EJBLineNumberExceptionLP le = (EJBLineNumberExceptionLP) e ;
					le.setLinenumber(linenumber) ;
					importErrors.add(le) ;
				} else {
					importErrors.add(new EJBLineNumberExceptionLP(linenumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, e)) ;
				}
			} catch (Exception e) {
				e.printStackTrace() ;
			} catch(Throwable t) {
				t.printStackTrace() ;
			}
 		}

		stats.setTotalRows(linesForImportCount) ;
		
		importErrors.addAll(buildImportOrderByReferencedFields(kontos)) ;
		stats.setPossibleGoodRowCount(getPossibleRowCount()) ;
		stats.setGoodRowCount(0) ;
		return new KontoImporterResult(importErrors, stats) ;
	}
	

	protected boolean isEmptyLine(String[] cells) {
		for (String cell : cells) {
			if(cell.trim().length() > 0) return false ;
		}
		return true ;
	}

	protected void checkCsvKonto(final CsvKonto konto) throws EJBLineNumberExceptionLP {
		if(existsKontoInDb(konto)) {
			stats.incrementWarningRowCount() ;
			throw new EJBLineNumberExceptionLP(
					-1, EJBLineNumberExceptionLP.SEVERITY_WARNING, 
					new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_IMPORT_KONTONUMMER_BEREITS_VORHANDEN,
				new ArrayList<Object>(){{add(konto.getCNr());}}, null)) ;
		}

		if(!existsFinanzamtCnr(konto.getFinanzamtCnr())) {
			stats.incrementErrorRowCount() ;
			throw new EJBLineNumberExceptionLP(
					-1, EJBLineNumberExceptionLP.SEVERITY_ERROR,
					new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_IMPORT_FINANZAMT_NICHT_VORHANDEN,
							new ArrayList<Object>(){{add(konto.getFinanzamtCnr());}}, null)) ;
		}
		
		if(!existsUvaartCnr(konto.getUvaartCnr())) {
			stats.incrementErrorRowCount() ;
			throw new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_IMPORT_UVAART_NICHT_VORHANDEN,
				new ArrayList<Object>(){{add(konto.getUvaartCnr());}}, null)) ;
		}

		if(!existsKostenstelle(konto.getKostenstelleCnr())) {
			stats.incrementErrorRowCount() ;
			throw new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_IMPORT_KOSTENSTELLE_NICHT_VORHANDEN,
				new ArrayList<Object>(){{add(konto.getKostenstelleCnr());}}, null)) ;			
		}
	}
	
	
	private List<EJBLineNumberExceptionLP> importKontos() {
		List<EJBLineNumberExceptionLP> importErrors = new ArrayList<EJBLineNumberExceptionLP>() ;
		
		for (CsvKonto csvKonto : importOrder) {
			KontoDto kontoDto = new KontoDto() ;
			try {
				kontoDto.setCNr(csvKonto.getCNr()) ;
				kontoDto.setCBez(csvKonto.getCBez()) ;
				kontoDto.setDGueltigvon(csvKonto.getDGueltigvon()) ;
				kontoDto.setDGueltigbis(csvKonto.getDGueltigbis()) ;
				kontoDto.setBAutomeroeffnungsbuchung(csvKonto.getBAutomeroeffnungsbuchung()) ;
				kontoDto.setBAllgemeinsichtbar(csvKonto.getBAllgemeinsichtbar()) ;
				kontoDto.setBManuellbebuchbar(csvKonto.getBManuellbebuchbar()) ;
				kontoDto.setCsortierung(csvKonto.getCLetztesortierung()) ;
				kontoDto.setBVersteckt(csvKonto.getBVersteckt()) ;
				kontoDto.setKontoartCNr(csvKonto.getKontoartCNr()) ;
				kontoDto.setKontotypCNr(csvKonto.getKontotypCNr()) ;
				
				kontoDto.setErgebnisgruppeIId(null);
				kontoDto.setKontoIIdWeiterfuehrendSkonto(getKontoIId(csvKonto.getKontoCnrWeiterfuehrendskonto())) ;
				kontoDto.setKontoIIdWeiterfuehrendUst(getKontoIId(csvKonto.getKontoCnrWeiterfuehrendust())) ;
				kontoDto.setUvaartIId(findUvaartIIdByCnrInDb(csvKonto.getUvaartCnr())) ;
				kontoDto.setFinanzamtIId(getFinanzamtIIdByCnr(csvKonto.getFinanzamtCnr())) ;
				kontoDto.setKostenstelleIId(findKostenstelleIIdByCnrInDb(csvKonto.getKostenstelleCnr())) ;
				kontoDto.setSteuerkategorieIId(null) ; /* ??? */
				kontoDto.setSteuerkategorieIIdReverse(null) ; /* ??? */
				kontoDto.setBOhneUst(new Short((short)0));
				beanServices.updateKonto(kontoDto) ;
				stats.incrementGoodRowCount() ;
			} catch(EJBExceptionLP ex) {
				stats.incrementErrorRowCount() ;
				importErrors.add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, ex)) ;
			} catch(Exception e) {
				stats.incrementErrorRowCount() ;
				importErrors.add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_UNCATCHED, e))) ;
			}			
		}
		
		return importErrors ;
	}
	
	private boolean existsKostenstelle(String cnr) {
		if(cnr.length() == 0) return true ;
		return findKostenstelleIIdByCnrInDb(cnr) != null ;
	}
	
	protected Integer findKostenstelleIIdByCnrInDb(String cnr) {
		if(cnr.length() == 0) return null ;
		
		try {
			KostenstelleDto kostenstelleDto = beanServices.kostenstelleFindByNummerMandant(cnr) ;
			return kostenstelleDto == null ? null : kostenstelleDto.getIId() ;
		} catch(Throwable t) {
		}

		return null ;
	}
		
		
	private boolean existsUvaartCnr(String uvaartCnr) {
		return findUvaartIIdByCnrInDb(uvaartCnr) != null ;		
	}

	protected Integer findUvaartIIdByCnrInDb(String cnr) {
		if(cnr.length() == 0) return null ;
		
		try {
			UvaartDto uvaartDto = beanServices.uvaartFindByCnrMandant(cnr) ;
			return uvaartDto == null ? null : uvaartDto.getIId() ;
		} catch(Throwable t) {
		}

		return null ;
	}
		
	private Integer getKontoIId(String cnr) {
		return cnr.length() == 0 ? null : findKontoIIdByCnrInDb(cnr) ;
	}
	
	
	private List<EJBLineNumberExceptionLP> buildImportOrderByReferencedFields(HashMap<String, CsvKonto> knownKontos) {
		List<EJBLineNumberExceptionLP> importErrors = new ArrayList<EJBLineNumberExceptionLP>() ;
		Collection<CsvKonto> eachKonto = knownKontos.values() ;
	
		importOrder = new ArrayList<CsvKonto>() ;
		
		for (CsvKonto konto : eachKonto) {
			try {
				handleReferencedKontoCnr(
						konto.getKontoCnrWeiterfuehrendbilanz(), konto, "weiterfuehrendes Bilanzkonto", knownKontos) ;
				handleReferencedKontoCnr(
						konto.getKontoCnrWeiterfuehrendskonto(), konto, "weiterfuehrendes Skontokonto", knownKontos) ;
				handleReferencedKontoCnr(
						konto.getKontoCnrWeiterfuehrendust(), konto, "weiterfuehrendes Ustkonto", knownKontos) ;

				if(!importOrder.contains(konto)) {
					importOrder.add(konto) ;
				}
			} catch(EJBLineNumberExceptionLP  e) {
				stats.incrementErrorRowCount() ;
				importErrors.add(e) ;
			} catch(EJBExceptionLP ex) {
				stats.incrementErrorRowCount() ;
				importErrors.add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, ex)) ;
			}				
		}

		return importErrors ;
	}

	protected void handleReferencedKontoCnr(String cnr, 
			CsvKonto baseKonto, String message, HashMap<String, CsvKonto> eachKonto) throws EJBExceptionLP{
		if(cnr.length() == 0) return ;
		if(existsKontoCnrInDb(cnr)) return ;
		if(!existsKontoCnrInImportMap(eachKonto, cnr))
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_KONTONUMMER_REFERENZ, 
				new IllegalArgumentException(
						"Das referenzierte Konto '" + cnr + 
						"' (" + message + ") im Sachkonto '" + baseKonto.getCNr() + "' existiert weder in der Datenbank noch in der Importdatei!")) ;

		CsvKonto referencedKonto = eachKonto.get(cnr) ;
		if(baseKonto.getCNr().equals(cnr)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_KONTONUMMER_REFERENZ, 
				new IllegalArgumentException("Referenziertes Konto '" + cnr + "' kann nicht auf sich selbst verweisen!")) ;
		}
		
		if(existsKontoCnrInImportMap(eachKonto, referencedKonto.getKontoCnrWeiterfuehrendbilanz())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ZYKLISCHER_VERWEIS,
				new IllegalArgumentException(
						"Referenziertes Konto '" + cnr + "' enthaelt Verweis auf zu importierendes Konto '" +
								referencedKonto.getKontoCnrWeiterfuehrendbilanz())) ;
		}

		if(existsKontoCnrInImportMap(eachKonto, referencedKonto.getKontoCnrWeiterfuehrendskonto())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ZYKLISCHER_VERWEIS,
				new IllegalArgumentException(
						"Referenziertes Konto '" + cnr + "' enthaelt Verweis auf zu importierendes Konto '" +
								referencedKonto.getKontoCnrWeiterfuehrendskonto())) ;
		}
		
		if(existsKontoCnrInImportMap(eachKonto, referencedKonto.getKontoCnrWeiterfuehrendust())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_ZYKLISCHER_VERWEIS,
				new IllegalArgumentException(
						"Referenziertes Konto '" + cnr + "' enthaelt Verweis auf zu importierendes Konto '" +
								referencedKonto.getKontoCnrWeiterfuehrendust())) ;
		}

		if(!importOrder.contains(referencedKonto)) {
			importOrder.add(referencedKonto) ;
		}
		// eachKonto.remove(referencedKonto.getCNr()) ; // brauchen wir nicht noch mal importieren 			
	}

	protected boolean existsKontoInImportMap(HashMap<String, CsvKonto> eachKonto, CsvKonto konto) {
		return existsKontoCnrInImportMap(eachKonto, konto.getCNr()) ;
	}

	protected boolean existsKontoCnrInImportMap(HashMap<String, CsvKonto>eachKonto, String cnr) {
		return eachKonto.containsKey(cnr) ;
	}
	
	protected boolean existsKontoInDb(CsvKonto konto) {
		return existsKontoCnrInDb(konto.getCNr()) ;
	}
	
	protected boolean existsKontoCnrInDb(String kontoCnr) {
		return findKontoIIdByCnrInDb(kontoCnr) != null ;
	}
	
	protected Integer findKontoIIdByCnrInDb(String kontoCnr) {
		if(kontoCnr.length() == 0) return null ;
		
		try {
			KontoDto kontoDto = beanServices.kontoFindByCnrMandant(kontoCnr) ;
			return kontoDto == null ? null : kontoDto.getIId() ;
		} catch(Throwable t) {
			
		}

		return null ;
	}
	
	protected boolean existsFinanzamtCnr(String finanzamtCnr) {
		return getFinanzamtIIdByCnr(finanzamtCnr) != null ;
	}

	protected Integer getFinanzamtIIdByCnr(String finanzamtCnr) {
		try {
			if(null == finanzaemter) {
				finanzaemter = beanServices.finanzamtFindAllByMandantCNr() ;
			}

			if(null == finanzaemter) return null ;
			
			for (int i = 0; i < finanzaemter.length; i++) {
				if(finanzaemter[i].getPartnerDto().getCName1nachnamefirmazeile1()
						.equals(finanzamtCnr)) return finanzaemter[i].getPartnerDto().getIId() ;
			}
		} catch(Throwable t) {
			System.out.println("") ;
		}

		return null ;		
	}
}
