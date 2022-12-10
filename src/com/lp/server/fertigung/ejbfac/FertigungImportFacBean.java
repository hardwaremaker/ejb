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
 *******************************************************************************/
package com.lp.server.fertigung.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;


import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.bl.CsvVerbrauchsartikelTransformer;
import com.lp.server.fertigung.bl.VendidataArticleConsumptionImporter;
import com.lp.server.fertigung.bl.VendidataConsumptionTransformer;
import com.lp.server.fertigung.bl.VerbrauchsartikelImporter;
import com.lp.server.fertigung.service.FertigungImportFac;
import com.lp.server.fertigung.service.IVendidataArticleImporterBeanServices;
import com.lp.server.fertigung.service.IVerbrauchsartikelImporterBeanServices;
import com.lp.server.fertigung.service.ImportPruefergebnis;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.VendidataArticleConsumption;
import com.lp.server.fertigung.service.VendidataArticleConsumptionImportResult;
import com.lp.server.fertigung.service.VerbrauchsartikelImportResult;
import com.lp.server.fertigung.service.errors.ImportException;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.ClientRemoteFac;
import com.lp.server.system.service.ImportProgressDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.PayloadDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataArticleExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FertigungImportFacBean extends Facade implements
		FertigungImportFac {
	@PersistenceContext
	private EntityManager em;

	private ArrayList<String> fehlerZeileXLSImport = new ArrayList<String>();

	public static String XLS_LOSIMPORT_SPALTE_PROJEKT = "Projekt";
	public static String XLS_LOSIMPORT_SPALTE_STUECKLISTE = "Stueckliste";
	public static String XLS_LOSIMPORT_SPALTE_LOSGROESSE = "Losgroesse";
	public static String XLS_LOSIMPORT_SPALTE_ENDETERMIN = "Endetermin";
	public static String XLS_LOSIMPORT_SPALTE_KOMMENTAR = "Kommentar";

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void importiereLoseXLS(ImportPruefergebnis importPruefergebnis,
			TheClientDto theClientDto) {

		ArrayList<LosDto> losDtos = importPruefergebnis.getLosDtos();

		for (int i = 0; i < losDtos.size(); i++) {
			try {
				getFertigungFac().createLos(losDtos.get(i), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ImportPruefergebnis pruefeLosimportXLS(byte[] xlsDatei,
			TheClientDto theClientDto) {
		// PJ19137

		fehlerZeileXLSImport = new ArrayList<String>();

		ImportPruefergebnis ergebnis = new ImportPruefergebnis();
		try {
			FertigungsgruppeDto[] ftgDtos = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(
							theClientDto.getMandant(), theClientDto);

			MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			LagerDto lDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null
							&& sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(),
								new Integer(i));
					}

				}

			}

			if (hmVorhandeneSpalten.containsKey(XLS_LOSIMPORT_SPALTE_PROJEKT) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_LOSIMPORT_SPALTE_STUECKLISTE) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_LOSIMPORT_SPALTE_LOSGROESSE) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_LOSIMPORT_SPALTE_ENDETERMIN) == false) {

				ergebnis.addFehler("Es muessen zumindest die Spalten 'Projekt/Stueckliste/Losgroesse/Endetermin' vorhanden sein");
				return ergebnis;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);

				System.out.println(i + " von " + sheet.getRows());

				Integer iSpalteStueckliste = hmVorhandeneSpalten
						.get(XLS_LOSIMPORT_SPALTE_STUECKLISTE);
				String artikelnummer = sZeile[iSpalteStueckliste].getContents();

				if (artikelnummer == null || artikelnummer.length() == 0) {
					ergebnis.addFehler("Die Spalte Stueckliste darf nicht leer sein. Zeile "
							+ (i+1));
					continue;
				}

				BigDecimal losgroesse = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten, XLS_LOSIMPORT_SPALTE_LOSGROESSE, i);

				if (losgroesse == null) {
					ergebnis.addFehler("Die Spalte Losgroesse darf nicht leer sein. Zeile "
							+ (i+1));
					continue;
				}

				String projekt = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_LOSIMPORT_SPALTE_PROJEKT, 80, i);


				Integer iSpalteEndetermin = hmVorhandeneSpalten
						.get(XLS_LOSIMPORT_SPALTE_ENDETERMIN);
				java.sql.Date endetermin = null;
				if (sZeile[iSpalteEndetermin].getType() == CellType.DATE
						|| sZeile[iSpalteEndetermin].getType() == CellType.DATE_FORMULA) {

					jxl.DateCell dc = (jxl.DateCell) sZeile[iSpalteEndetermin];

					if (dc.getDate() == null) {
						ergebnis.addFehler("Die Spalte Endetermin darf nicht leer sein. Zeile "
								+ (i+1));
						continue;
					} else {

						endetermin = Helper.cutDate(new java.sql.Date(
								((jxl.DateCell) sZeile[iSpalteEndetermin])
										.getDate().getTime()));
					}

				} else {
					ergebnis.addFehler("Die Spalte Endetermin muss vom Typ Datum sein. Zeile "
							+ (i+1));
					continue;
				}

				String kommentar = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_LOSIMPORT_SPALTE_KOMMENTAR, 3000, i);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByCNrOhneExc(artikelnummer, theClientDto);

				if (artikelDto != null) {
					artikelDto = getArtikelFac().artikelFindByPrimaryKey(
							artikelDto.getIId(), theClientDto);

				} else {
					ergebnis.addFehler("Die Artikelnummer '" + artikelnummer
							+ "' ist nicht vorhanden. Zeile " + (i+1));
					continue;
				}

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								artikelDto.getIId(), theClientDto);
				if (stklDto == null) {
					ergebnis.addFehler("Die Stueckliste '" + artikelnummer
							+ "' ist nicht vorhanden. Zeile " + (i+1));
					continue;
				}

				ArrayList<KeyvalueDto> hinweise = getArtikelkommentarFac()
						.getArtikelhinweise(artikelDto.getIId(),
								LocaleFac.BELEGART_LOS, theClientDto);
				for (int j = 0; j < hinweise.size(); j++) {
					ergebnis.addWarnung("Stueckliste " + artikelnummer
							+ ":\r\n" + Helper.strippHTML(hinweise.get(j).getCValue()));
				}

				LosDto losDto = new LosDto();

				losDto.setStuecklisteIId(stklDto.getIId());

				losDto.setTProduktionsende(endetermin);

				int durchlaufzeit = 0;
				if (stklDto.getNDefaultdurchlaufzeit() != null) {
					durchlaufzeit = stklDto.getNDefaultdurchlaufzeit()
							.intValue();
				}

				losDto.setTProduktionsbeginn(Helper.addiereTageZuDatum(
						endetermin, -durchlaufzeit));

				losDto.setXText(kommentar);
				losDto.setNLosgroesse(losgroesse);
				losDto.setCProjekt(projekt);

				losDto.setPartnerIIdFertigungsort(mDto.getPartnerIId());

				losDto.setKostenstelleIId(mDto.getIIdKostenstelle());

				if(stklDto.getFertigungsgruppeIId()!=null) {
					losDto.setFertigungsgruppeIId(stklDto.getFertigungsgruppeIId());
				}else {
					losDto.setFertigungsgruppeIId(ftgDtos[0].getIId());
				}
				
				
				losDto.setMandantCNr(theClientDto.getMandant());
				losDto.setLagerIIdZiel(lDto.getIId());

			
				if(losgroesse.doubleValue()<=0) {
				
					ergebnis.addWarnung("Ein Los mit der Losgroesse <= 0 kann nicht angelegt werden. Zeile "
							+ (i+1)
							+ " wird ignoriert.");
				}else {
					ergebnis.addLosDto(losDto);
				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		for (int i = 0; i < fehlerZeileXLSImport.size(); i++) {
			ergebnis.addFehler(fehlerZeileXLSImport.get(i));
		}

		return ergebnis;
	}

	private String getStringAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iLaenge, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						fehlerZeileXLSImport.add(feldname + " ist zu lang (>"
								+ iLaenge + ") Zeile " + iZeile);

					}

					return c.getContents();
				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	private Short getShortAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getContents().trim().equals("0")) {
						return new Short((short) 0);
					} else if (c.getContents().trim().equals("1")) {
						return new Short((short) 1);
					} else {

						fehlerZeileXLSImport
								.add(feldname
										+ " darf nur die Werte 0 bzw. 1 enthalten. Zeile ");

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	private BigDecimal getBigDecimalAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER
							|| c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new BigDecimal(d);

					} else {

						fehlerZeileXLSImport.add(feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	private Double getDoubleAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER
							|| c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new Double(d);

					} else {

						fehlerZeileXLSImport.add(feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	@Override
	public VendidataArticleConsumptionImportResult importVendidataArticleConsumptionXML(String xmlContent,
			boolean checkOnly, TheClientDto theClientDto) {

		VendidataConsumptionTransformer transformer = new VendidataConsumptionTransformer();
		List<VendidataArticleConsumption> consumptionList = transformer.transform(xmlContent);
		ClientRemoteFac clientRemoteFacMock = new ClientRemoteFac() {
			public void publish(PayloadDto payloadDto) throws RemoteException {
			}
			public void neueNachrichtenVerfuegbar() throws RemoteException {
			}
		};
	
		if (checkOnly) {
			return checkArticleConsumptionImpl(consumptionList, clientRemoteFacMock, "", theClientDto);
		}
		
		Comparator<VendidataArticleConsumption> comparator = new Comparator<VendidataArticleConsumption>() {
			@Override
			public int compare(VendidataArticleConsumption arg0, VendidataArticleConsumption arg1) {
				int tourCompare = arg0.getTourNr().compareTo(arg1.getTourNr());
				return tourCompare == 0 ? arg0.getBuchungsdatum().compareTo(arg1.getBuchungsdatum()) : tourCompare;
			}
		};
		Collections.sort(consumptionList, comparator);
		logArticleConsumptionListInfo(consumptionList);
		
		return importArtikelConsumptionImpl(consumptionList, clientRemoteFacMock, "", theClientDto);
	}

	/**
	 * @param consumptionList
	 */
	private void logArticleConsumptionListInfo(
			List<VendidataArticleConsumption> consumptionList) {
		int count = 0;
		for (int i=1; i<consumptionList.size(); i++) {
			count++;
			Calendar cal1 = new GregorianCalendar();
			cal1.setTime(consumptionList.get(i-1).getBuchungsdatum());
			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(consumptionList.get(i).getBuchungsdatum());
			if (!consumptionList.get(i-1).getTourNr().equals(consumptionList.get(i).getTourNr()) 
					|| cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
				myLogger.info("Tour" + consumptionList.get(i-1).getTourNr() + ", Monat: " + (cal1.get(Calendar.MONTH)+1) + ", Anzahl=" + count);
				count = 0;
			}
		}
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(consumptionList.get(consumptionList.size()-1).getBuchungsdatum());
		myLogger.info("Tour" + consumptionList.get(consumptionList.size()-1).getTourNr() + ", Monat: " + (cal1.get(Calendar.MONTH)+1) + ", Anzahl=" + count);
	}
	
	private VendidataArticleConsumptionImportResult checkArticleConsumptionImpl(List<VendidataArticleConsumption> consumptionList, 
			ClientRemoteFac clientRemoteFac, String payloadReference, TheClientDto theClientDto) {
		VendidataArticleImportBeanHolder beanHolder = new VendidataArticleImportBeanHolder(getMandantFac(),
				getFertigungFac(), this, getArtikelFac(), getStuecklisteFac(), getLagerFac(), 
				getSystemFac(), getFehlmengeFac());
		IVendidataArticleImporterBeanServices beanServices = new VendidataArticleImporterBeanServices(theClientDto, beanHolder);
		VendidataArticleConsumptionImporter importer = new VendidataArticleConsumptionImporter(beanServices);
		
		Double dSteps = new Double(consumptionList.size()) / 100 * 5;
		int steps = dSteps < 1 ? 1 : dSteps.intValue();
		int count = 0;
		VendidataArticleConsumptionImportResult finalResult = new VendidataArticleConsumptionImportResult();
		for (VendidataArticleConsumption vac : consumptionList) {
			List<VendidataArticleConsumption> list = new ArrayList<VendidataArticleConsumption>();
			list.add(vac);
			VendidataArticleConsumptionImportResult result = importer.checkImportXMLDaten(list);
			finalResult.addData(result);
			if (++count % steps == 0) {
				try {
					clientRemoteFac.publish(new PayloadDto(payloadReference, 
							new ImportProgressDto(consumptionList.size(), count)));
				} catch (RemoteException e) {
					myLogger.error("Error while publishing payload", e);
				}
			}
		}
		
		finalResult.addData(importer.checkLosstatus(consumptionList));
		
		return finalResult;
	}
	
	private VendidataArticleConsumptionImportResult importArtikelConsumptionImpl(List<VendidataArticleConsumption> consumptionList, 
			ClientRemoteFac clientRemoteFac, String payloadReference, TheClientDto theClientDto) {
		if (consumptionList == null) return new VendidataArticleConsumptionImportResult();
		
		myLogger.info("Starting import of 4Vending article consumption list, number of articles = " + consumptionList.size());
		Double dSteps = new Double(consumptionList.size()) / 100 * 5;
		int steps = dSteps < 1 ? 1 : dSteps.intValue();
		int count = 0;
		VendidataArticleConsumptionImportResult finalResult = new VendidataArticleConsumptionImportResult();
		Set<String> fehlmengenLose = new HashSet<String>();
		
		for (VendidataArticleConsumption vac : consumptionList) {
			List<VendidataArticleConsumption> list = new ArrayList<VendidataArticleConsumption>();
			list.add(vac);
			try {
				VendidataArticleConsumptionImportResult result = getFertigungImportFac().importArtikelConsumption(list, theClientDto);
				finalResult.addData(result);
			} catch (EJBExceptionLP ex) {
				if (EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER == ex.getCode()) {
					try {
						VendidataArticleConsumptionImportResult fehlmengeResult = 
								getFertigungImportFac().importFehlmengeFromVendidataArtikelConsumption(vac, theClientDto);
						if (fehlmengeResult.getUsedLosDtosAsList().size() == 1) {
							fehlmengenLose.add(fehlmengeResult.getUsedLosDtosAsList().get(0).getCNr());
						}
						finalResult.addData(fehlmengeResult);
					} catch (EJBVendidataArticleExceptionLP vEx) {
						finalResult.getImportErrors().add(vEx);
					}
				} else {
					throw ex;
				}
			}
			if (++count % steps == 0) {
				try {
					clientRemoteFac.publish(new PayloadDto(payloadReference, 
							new ImportProgressDto(consumptionList.size(), count)));
				} catch (RemoteException e) {
					myLogger.error("Error while publishing payload", e);
				}
			}
			
		}
		
		for (String cnr : fehlmengenLose) {
			finalResult.getImportErrors().add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_WARNING, 
					EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER, cnr));
			finalResult.getStats().incrementWarningCounts();
		}
		myLogger.info("Finished import of article consumption list");
		return finalResult;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public VendidataArticleConsumptionImportResult importArtikelConsumption(List<VendidataArticleConsumption> consumption, TheClientDto theClientDto) {
		VendidataArticleImportBeanHolder beanHolder = new VendidataArticleImportBeanHolder(getMandantFac(),
				getFertigungFac(), this, getArtikelFac(), getStuecklisteFac(), getLagerFac(), 
				getSystemFac(), getFehlmengeFac());
		IVendidataArticleImporterBeanServices beanServices = new VendidataArticleImporterBeanServices(theClientDto, beanHolder);
		VendidataArticleConsumptionImporter importer = new VendidataArticleConsumptionImporter(beanServices);
		
		return importer.importXMLDaten(consumption);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public VendidataArticleConsumptionImportResult importFehlmengeFromVendidataArtikelConsumption(VendidataArticleConsumption consumption, TheClientDto theClientDto) {
		VendidataArticleImportBeanHolder beanHolder = new VendidataArticleImportBeanHolder(getMandantFac(),
				getFertigungFac(), this, getArtikelFac(), getStuecklisteFac(), getLagerFac(), 
				getSystemFac(), getFehlmengeFac());
		IVendidataArticleImporterBeanServices beanServices = new VendidataArticleImporterBeanServices(theClientDto, beanHolder);
		VendidataArticleConsumptionImporter importer = new VendidataArticleConsumptionImporter(beanServices);
		
		return importer.importFehlmenge(consumption);
	}
	
	@Override
	public VendidataArticleConsumptionImportResult importVendidataExpiredProductsXML(
			String xmlContent, boolean checkOnly, TheClientDto theClientDto) {
//		
//		VendidataArticleImportBeanHolder beanHolder = new VendidataArticleImportBeanHolder(getMandantFac(),
//				getFertigungFac(), this, getArtikelFac(), getStuecklisteFac(), getLagerFac(), 
//				getSystemFac(), getFehlmengeFac());
//		IVendidataArticleImporterBeanServices beanServices = new VendidataArticleImporterBeanServices(theClientDto, beanHolder);
//		VendidataArticleConsumptionImporter importer = new VendidataArticleConsumptionImporter(beanServices);
//		
//		return checkOnly ? importer.checkImportXMLDaten(xmlContent) : importer.importXMLDaten(xmlContent);

		return new VendidataArticleConsumptionImportResult();
	}
	
	private Context getInitialContextForRMI(String host, Integer port) throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>();

		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		environment.put(Context.PROVIDER_URL, "rmi://" + host + ":" + port);
		
		return new InitialContext(environment);
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(6000)
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public VendidataArticleConsumptionImportResult importVendidataArticleConsumptionXML(
			String xmlContent, String payloadReference, boolean checkOnly, TheClientDto theClientDto) {
		VendidataConsumptionTransformer transformer = new VendidataConsumptionTransformer();
		List<VendidataArticleConsumption> consumptionList = transformer.transform(xmlContent);

		ClientRemoteFac clientRemoteFac = new ClientRemoteFac() {
			public void publish(PayloadDto payloadDto) throws RemoteException {
			}
			public void neueNachrichtenVerfuegbar() throws RemoteException {
			}
		};
		try {
			VerfuegbareHostsDto host = getTheClientFacLocal().getVerfuegbarenHost(theClientDto);
			Context context = getInitialContextForRMI(host.getHostname(), host.getPort());
			clientRemoteFac = (ClientRemoteFac) context.lookup(ClientRemoteFac.REMOTE_BIND_NAME);
		} catch (Throwable t) {
			myLogger.error("Error during client remote host acquisition", t);
			myLogger.info("Starting import without remote notification");
		}
		
		if (checkOnly) {
			return checkArticleConsumptionImpl(consumptionList, clientRemoteFac, payloadReference, theClientDto);
		}

		return importArtikelConsumptionImpl(consumptionList, clientRemoteFac, payloadReference, theClientDto);
	}
	
	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public VerbrauchsartikelImportResult importCsvVerbrauchsartikel(List<String[]> allLines, 
			boolean checkOnly, TheClientDto theClientDto) throws RemoteException {
		if (allLines.size() < 2) {
			return new VerbrauchsartikelImportResult(new ArrayList<ImportException>());
		}
		
		CsvVerbrauchsartikelTransformer transformer = new CsvVerbrauchsartikelTransformer();
		transformer.buildFieldIndexer(allLines.get(0));
		
		VerbrauchsartikelImporterBeanHolder beanHolder = new VerbrauchsartikelImporterBeanHolder(
				getMandantFac(), getFertigungFac(), getArtikelFac(), getStuecklisteFac(), 
				getLagerFac(), getSystemFac(), getParameterFac());
		IVerbrauchsartikelImporterBeanServices beanServices = new VerbrauchsartikelImporterBeanServices(theClientDto, beanHolder);
		VerbrauchsartikelImporter importer = new VerbrauchsartikelImporter(beanServices, transformer);
		List<String[]> dataLines = allLines.subList(1, allLines.size());
		
		return checkOnly ? importer.checkImportCsv(dataLines) : importer.importCsv(dataLines);
	}
}