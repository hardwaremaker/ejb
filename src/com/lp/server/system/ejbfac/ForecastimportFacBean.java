package com.lp.server.system.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprQuery;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.bl.CallOffDailyXLSTransformer;
import com.lp.server.forecast.bl.CallOffWeeklyXLSTransformer;
import com.lp.server.forecast.bl.CallOffXLSImporter;
import com.lp.server.forecast.bl.CallOffXLSImporterMonthly;
import com.lp.server.forecast.bl.LinienabrufEluxDeTxtImporter;
import com.lp.server.forecast.bl.LinienabrufMultipleTxtImporter;
import com.lp.server.forecast.ejbfac.CallOffXlsImporterBeanService;
import com.lp.server.forecast.ejbfac.CallOffXlsMultiImporterBeanService;
import com.lp.server.forecast.ejbfac.EdifactDelforService;
import com.lp.server.forecast.ejbfac.LinienabrufImporterBeanService;
import com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse;
import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.EdiFileInfo;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastImportFehlerDto;
import com.lp.server.forecast.service.ForecastImportZeissDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ICallOffXlsImporterBeanService;
import com.lp.server.forecast.service.ILinienabrufImporterBeanService;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.KundesokoQuery;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRKundesoko;
import com.lp.server.partner.fastlanereader.generated.FLRKundesokomengenstaffel;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LiefermengenDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.system.service.ForecastImportCallOff;
import com.lp.server.system.service.ForecastImportFileType;
import com.lp.server.system.service.ForecastImportLinienabruf;
import com.lp.server.system.service.ForecastimportFac;
import com.lp.server.system.service.IForecastImportFile;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.ImportFacade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.edifact.DelforCumulativePosition;
import com.lp.service.edifact.DelforPosition;
import com.lp.service.edifact.DelforRepository;
import com.lp.service.edifact.DistinctDelforItemIterator;
import com.lp.service.edifact.EdifactInterpreter;
import com.lp.service.edifact.EdifactProgram;
import com.lp.service.edifact.EdifactProgramDelfor911HV;
import com.lp.service.edifact.EdifactProgramDelfor99bHV;
import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.IEjbService;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.EdifactMessage;
import com.lp.service.edifact.schema.UnhInfoDelfor911;
import com.lp.service.edifact.schema.UnhInfoDelfor99b;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Stateless
public class ForecastimportFacBean extends ImportFacade implements ForecastimportFac {

	@PersistenceContext
	private EntityManager em;

	public static String XLS_FORECASTPOSITION_IMPORT_ARTIKELNUMMER = "artikelnummer";
	public static String XLS_FORECASTPOSITION_IMPORT_ARTIKELKBEZ = "artikelkbez";
	public static String XLS_FORECASTPOSITION_IMPORT_KD_ARTIKELNUMMER = "kdartikelnummer";
	public static String XLS_FORECASTPOSITION_IMPORT_BESTELLNUMMER = "bestellnummer";
	public static String XLS_FORECASTPOSITION_IMPORT_MENGE = "menge";
	public static String XLS_FORECASTPOSITION_IMPORT_TERMIN = "termin";
	public static String XLS_FORECASTPOSITION_IMPORT_KUNDE_LIEFERMENGE = "kundeliefermenge";

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereForecastpositionXLS(byte[] xlsDatei, Integer forecastauftragIId,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {

			ForecastauftragDto fcaDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);

			FclieferadresseDto fclDto = getForecastFac()
					.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());

			ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim().toLowerCase(), new Integer(i));
					}

				}

			}

			if (hmVorhandeneSpalten.containsKey(XLS_FORECASTPOSITION_IMPORT_ARTIKELNUMMER) == false
					&& hmVorhandeneSpalten.containsKey(XLS_FORECASTPOSITION_IMPORT_ARTIKELKBEZ) == false
					&& hmVorhandeneSpalten.containsKey(XLS_FORECASTPOSITION_IMPORT_KD_ARTIKELNUMMER) == false) {
				rueckgabe += "Es muss zumindest eine der Spalten 'Artikelnummer/ArtikelKbez/KDArtikelnummer' vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			if (hmVorhandeneSpalten.containsKey(XLS_FORECASTPOSITION_IMPORT_MENGE) == false
					|| hmVorhandeneSpalten.containsKey(XLS_FORECASTPOSITION_IMPORT_TERMIN) == false) {
				rueckgabe += "Es muss zumindest die Spalte 'Menge/Termin' vorhanden sein" + new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);

				if (sZeile.length == 0) {
					continue;
				}

				fehlerZeileXLSImport = "";

				String artikelnummer = null;
				Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(XLS_FORECASTPOSITION_IMPORT_ARTIKELNUMMER);
				if (iSpalteArtikelnummer != null) {

					artikelnummer = sZeile[iSpalteArtikelnummer].getContents();
				}

				String kdartikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_FORECASTPOSITION_IMPORT_KD_ARTIKELNUMMER, 25, i);
				String artikelkbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_FORECASTPOSITION_IMPORT_ARTIKELKBEZ, 25, i);
				BigDecimal bdMenge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_FORECASTPOSITION_IMPORT_MENGE,
						i);

				BigDecimal bdLiefermenge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_FORECASTPOSITION_IMPORT_KUNDE_LIEFERMENGE, i);

				java.util.Date dTermin = getDateAusXLS(sZeile, hmVorhandeneSpalten, XLS_FORECASTPOSITION_IMPORT_TERMIN,
						i);

				String bestellnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_FORECASTPOSITION_IMPORT_BESTELLNUMMER, 40, i);

				// PJ19808 Wenn alle Nutzfelder null sind, dann Zeile auslassen

				if ((artikelnummer == null || artikelnummer.length() == 0) && kdartikelnummer == null
						&& artikelkbez == null && bdMenge == null && dTermin == null && bestellnummer == null) {
					continue;
				}

				if (artikelnummer == null && (kdartikelnummer == null || kdartikelnummer.length() == 0)
						&& (artikelkbez == null || artikelkbez.length() == 0)) {
					rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
					continue;
				}

				if (bdMenge == null) {
					rueckgabe += "Die Menge darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
					continue;
				}

				if (dTermin == null) {
					rueckgabe += "Der Termin darf nicht leer sein, bzw. muss als Datum formatiert sein. Zeile " + i
							+ new String(CRLFAscii);
					continue;
				}

				Integer artikelIId = null;

				if (artikelnummer != null && artikelnummer.length() > 0) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);

					if (artikelDto == null) {
						rueckgabe += "Es konnte kein Artikel mit der Artikelnummer '" + artikelnummer
								+ "' gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;
					} else {
						artikelIId = artikelDto.getIId();
					}

				}

				if (artikelIId == null && artikelkbez != null && artikelkbez.length() >= 0) {

					// Artikelsuche ueber KBez
					javax.persistence.Query q = ArtikelsprQuery.byCKBez(em, artikelkbez,
							theClientDto.getLocMandantAsString());
					Collection c = q.getResultList();

					if (c.size() == 0) {

						rueckgabe += "Es konnte kein Artikel mit der Kurzbezeichnung '" + artikelkbez
								+ "' gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;

					} else {
						Artikelspr spr = ((Artikelspr) c.iterator().next());
						artikelIId = spr.getPk().getArtikelIId();
					}

				}

				if (artikelIId == null && kdartikelnummer != null && kdartikelnummer.length() >= 0) {

					// Artikelsuche �ber KDArtikelnummer
					javax.persistence.Query q = KundesokoQuery.byKundeIIdArtikelnummer(em, fcDto.getKundeIId(),
							kdartikelnummer, Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
					Collection c = q.getResultList();

					if (c.size() == 0) {

						rueckgabe += "Es konnte kein Artikel mit der KD-Artikelnummer '" + kdartikelnummer
								+ "' gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;

					} else {
						Kundesoko soko = ((Kundesoko) c.iterator().next());
						artikelIId = soko.getArtikelIId();
					}

				}

				if (artikelIId == null) {
					rueckgabe += "Es konnte kein Artikel aufgrund der Spalten Artikelnummer/ArtikelKbez/KDArtikelnummer gefunden werden. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				ForecastpositionDto fcpDto = new ForecastpositionDto();

				Timestamp tTermin = Helper.cutTimestamp(new java.sql.Timestamp(dTermin.getTime()));

				fcpDto.setArtikelIId(artikelIId);
				fcpDto.setForecastauftragIId(forecastauftragIId);
				fcpDto.setCBestellnummer(bestellnummer);
				fcpDto.setNMenge(bdMenge);
				fcpDto.setTTermin(new java.sql.Timestamp(dTermin.getTime()));
				fcpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

				if (fehlerZeileXLSImport.length() > 0) {
					rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
				}

				if (bImportierenWennKeinFehler) {

					getForecastFac().createForecastposition(fcpDto);
					// PJ19852
					if (bdLiefermenge != null) {
						LiefermengenDto lmDto = getPartnerServicesFac()
								.liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(artikelIId,
										fclDto.getKundeIIdLieferadresse(), tTermin);

						if (lmDto != null) {
							lmDto.setNMenge(bdLiefermenge);
							getPartnerServicesFac().updateLiefermengen(lmDto);

						} else {

							lmDto = new LiefermengenDto();
							lmDto.setArtikelIId(artikelIId);
							lmDto.setKundeIIdLieferadresse(fclDto.getKundeIIdLieferadresse());
							lmDto.setNMenge(bdLiefermenge);
							lmDto.setTDatum(tTermin);

							getPartnerServicesFac().createLiefermengen(lmDto);
						}

					}
				}
			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;
	}

	public void importiereForecastpositionZEISS_CSV(ArrayList<ForecastImportZeissDto> alZeilenCsv,
			Integer forecastauftragIId, TheClientDto theClientDto) {

		for (int i = 0; i < alZeilenCsv.size(); i++) {
			ForecastImportZeissDto zeissZeileDto = alZeilenCsv.get(i);

			String referenznummer = zeissZeileDto.getReferenznummer();

			ArtikelDto[] aDtosAnhandReferenznummer = getArtikelFac()
					.artikelfindByCReferenznrMandantCNrOhneExc(referenznummer, theClientDto.getMandant());

			// PJ22122
			if (aDtosAnhandReferenznummer.length > 0) {

				HashMap<java.util.Date, BigDecimal> hmDatumUndMenge = zeissZeileDto.getHmDatumUndMenge();
				Iterator it = hmDatumUndMenge.keySet().iterator();
				while (it.hasNext()) {
					java.util.Date datum = (java.util.Date) it.next();
					BigDecimal menge = hmDatumUndMenge.get(datum);

					ForecastpositionDto fcpDto = new ForecastpositionDto();

					fcpDto.setArtikelIId(aDtosAnhandReferenznummer[0].getIId());
					fcpDto.setForecastauftragIId(forecastauftragIId);
					fcpDto.setNMenge(menge);
					fcpDto.setTTermin(new java.sql.Timestamp(datum.getTime()));
					fcpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
					
					getForecastFac().createForecastposition(fcpDto);

				}

			}else {
				ArrayList alInfo = new ArrayList();
				alInfo.add("Es konnte kein Artikel mit der Referenznummer '"+referenznummer+"' gefunden werden (Zeile "+zeissZeileDto.getZeile()+"). Import abgebrochen!");

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZEISS_IMPORT, alInfo, new Exception("FEHLER_ZEISS_IMPORT"));
			}

		}

		
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereForecastpositionXLS_VAT(byte[] xlsDatei, Integer forecastauftragIId,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {

			ForecastauftragDto fcaDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);

			FclieferadresseDto fclDto = getForecastFac()
					.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());

			ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			HashMap<java.util.Date, Integer> hmVorhandeneDatumsSpalten = new HashMap<java.util.Date, Integer>();

			Calendar cErsterDesAktuellenMonats = Calendar.getInstance();
			cErsterDesAktuellenMonats.set(Calendar.DATE, 1);

			java.util.Date dErsterDesMonats = Helper.cutDate(cErsterDesAktuellenMonats.getTime());

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim().toLowerCase(), new Integer(i));

						if (i >= 9 && sZeile[i].getType() == CellType.DATE
								|| sZeile[i].getType() == CellType.DATE_FORMULA) {

							java.util.Date dMonat = ((jxl.DateCell) sZeile[i]).getDate();

							dMonat = Helper.cutDate(dMonat);

							Calendar c = Calendar.getInstance();
							c.setTime(dMonat);

							if (dMonat.getTime() >= dErsterDesMonats.getTime()) {
								hmVorhandeneDatumsSpalten.put(dMonat, new Integer(i));

							}

						}

					}

				}

			}

			String SPALTE_PART_CD = "partcd";
			String SPALTE_MEASURE = "measure";

			if (hmVorhandeneSpalten.containsKey(SPALTE_PART_CD) == false
					|| hmVorhandeneSpalten.containsKey(SPALTE_MEASURE) == false) {
				rueckgabe += "Es muss zumindest die Spalte 'partCd' und 'measure'  vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);

				if (sZeile.length == 0) {
					continue;
				}

				if (i == 214) {
					int x = 0;
				}

				Integer iSpaltemeasure = hmVorhandeneSpalten.get(SPALTE_MEASURE);
				if (sZeile[iSpaltemeasure].getContents() != null
						&& sZeile[iSpaltemeasure].getContents().equals("GROSS")) {
					fehlerZeileXLSImport = "";

					Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(SPALTE_PART_CD);

					String kdartikelnummer = sZeile[iSpalteArtikelnummer].getContents();

					if (kdartikelnummer == null) {
						rueckgabe += "Die Artikelnummer (Spalte 'partCd')  darf nicht leer sein. Zeile " + (i + 1)
								+ new String(CRLFAscii);
						continue;
					}

					Session session = FLRSessionFactory.getFactory().openSession();

					String sQuery = "SELECT a.i_id FROM FLRArtikel a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
							+ "' AND a.c_referenznr = '" + kdartikelnummer + "'";

					org.hibernate.Query query = session.createQuery(sQuery);
					List<?> results = query.list();

					if (results.size() > 0) {

						Iterator itArtikel = results.iterator();
						Integer artikelIId = (Integer) itArtikel.next();

						Iterator it = hmVorhandeneDatumsSpalten.keySet().iterator();

						while (it.hasNext()) {

							Date dMonat = (Date) it.next();

							Integer iSpalte = hmVorhandeneDatumsSpalten.get(dMonat);

							if (sZeile[iSpalte].getContents() != null && sZeile[iSpalte].getContents().length() > 0) {

								if (sZeile[iSpalte].getType() == CellType.NUMBER
										|| sZeile[iSpalte].getType() == CellType.NUMBER_FORMULA) {

									double d = ((NumberCell) sZeile[iSpalte]).getValue();

									if (d > 0) {

										BigDecimal menge = Helper.rundeKaufmaennisch(new BigDecimal(d), 4);

										ForecastpositionDto fcpDto = new ForecastpositionDto();

										fcpDto.setArtikelIId(artikelIId);
										fcpDto.setForecastauftragIId(forecastauftragIId);
										// fcpDto.setCBestellnummer(bestellnummer);
										fcpDto.setNMenge(menge);
										fcpDto.setTTermin(new java.sql.Timestamp(dMonat.getTime()));
										fcpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

										if (bImportierenWennKeinFehler) {

											getForecastFac().createForecastposition(fcpDto);

										}
									}

								} else {
									fehlerZeileXLSImport += "Spalte " + dMonat + " muss vom Typ 'Zahl' sein. Zeile "
											+ (i + 1) + new String(CRLFAscii);
								}
							}
						}
					} else {
						rueckgabe += "Es konnte kein Artikel mit der Referenznummer '" + kdartikelnummer
								+ "' gefunden werden. Zeile " + (i + 1) + new String(CRLFAscii);
						continue;
					}

					if (fehlerZeileXLSImport.length() > 0) {
						rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
					}
				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;
	}

	public int importiereVMI_XLS(byte[] xlsDatei, TheClientDto theClientDto) {

		int iZeilenImportiert = 0;
		try {
			byte[] CRLFAscii = { 13, 10 };

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			HashSet<Integer> hsNeuAngelegteForecastauftraege = new HashSet<Integer>();

			String spalteKunde = "Kunde";
			String spalteLagerort = "Lagerort"; // =Lieferadresse
			String spalteArtikelnummer = "Artikelnummer Lieferant"; // =HV-Artikelnummer

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			String sFehler = "";

			if (hmVorhandeneSpalten.containsKey(spalteKunde) == false
					&& hmVorhandeneSpalten.containsKey(spalteLagerort) == false
					&& hmVorhandeneSpalten.containsKey(spalteArtikelnummer) == false) {
				sFehler += "Die Spalten '" + spalteKunde + "/" + spalteLagerort + "/" + spalteArtikelnummer
						+ "' m\u00fcssen vorhanden sein!" + new String(CRLFAscii);

			} else {

				// Um des Kunden zu suchen, sucen wir in der Partner-KBez mit %LIKE% nach dem
				// letzten wort aus der Spalte Kunde bzw. Lagerort, hier muss es genau ein
				// Ergebnis geben, ansonsten Fehler

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					String forcastKunde = getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteKunde, 40, i);

					// letztesWort

					if (forcastKunde.lastIndexOf(" ") > 0) {
						forcastKunde = forcastKunde.substring(forcastKunde.lastIndexOf(" ") + 1);
					}

					Session session = FLRSessionFactory.getFactory().openSession();

					String sQuery = "SELECT kd FROM FLRKunde kd WHERE kd.mandant_c_nr='" + theClientDto.getMandant()
							+ "' AND kd.flrpartner.c_kbez LIKE '%" + forcastKunde + "%'";

					org.hibernate.Query query = session.createQuery(sQuery);
					List<?> results = query.list();

					if (results.size() == 1) {

						FLRKunde flrKunde = (FLRKunde) results.iterator().next();

						// Lieferadresse suchen

						String forcastLieferadresse = getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteLagerort, 40,
								i);

						// letztesWort

						if (forcastLieferadresse.lastIndexOf(" ") > 0) {
							forcastLieferadresse = forcastLieferadresse
									.substring(forcastLieferadresse.lastIndexOf(" ") + 1);
						}

						Session session2 = FLRSessionFactory.getFactory().openSession();

						String sQuery2 = "SELECT kd FROM FLRKunde kd WHERE kd.mandant_c_nr='"
								+ theClientDto.getMandant() + "' AND kd.flrpartner.c_kbez LIKE '%"
								+ forcastLieferadresse + "%'";

						org.hibernate.Query query2 = session2.createQuery(sQuery2);
						List<?> results2 = query2.list();

						if (results2.size() == 1) {
							// Nun Forecast suchen

							FLRKunde flrKundeLieferadresse = (FLRKunde) results2.iterator().next();

							Session sessionFC = FLRSessionFactory.getFactory().openSession();

							String sQueryFC = "SELECT fcl,(SELECT min(fca.i_id) FROM FLRForecastauftrag fca WHERE fca.flrfclieferadresse.kunde_i_id_lieferadresse=fcl.kunde_i_id_lieferadresse AND fca.status_c_nr='"
									+ LocaleFac.STATUS_ANGELEGT
									+ "') FROM FLRFclieferadresse fcl WHERE fcl.flrforecast.status_c_nr='"
									+ LocaleFac.STATUS_ANGELEGT + "' AND fcl.flrforecast.flrkunde.i_id="
									+ flrKunde.getI_id() + " AND fcl.kunde_i_id_lieferadresse="
									+ flrKundeLieferadresse.getI_id();

							org.hibernate.Query queryFC = sessionFC.createQuery(sQueryFC);
							List<?> resultsFC = queryFC.list();
							if (resultsFC.size() > 0) {

								Object[] o = (Object[]) resultsFC.iterator().next();

								Integer forcast_i_id_angelegt = (Integer) o[1];

								FLRFclieferadresse flrFCLieferadresse = (FLRFclieferadresse) o[0];

								if (forcast_i_id_angelegt != null
										&& !hsNeuAngelegteForecastauftraege.contains(forcast_i_id_angelegt)) {
									sFehler = "F\u00fcr den Kunden "
											+ HelperServer.formatNameAusFLRPartner(flrKunde.getFlrpartner())
											+ " (Kbez: " + flrKunde.getFlrpartner().getC_kbez() + ")"
											+ " und die Lieferadresse "
											+ HelperServer
													.formatNameAusFLRPartner(flrKundeLieferadresse.getFlrpartner())
											+ " (Kbez: " + flrKundeLieferadresse.getFlrpartner().getC_kbez() + ")"
											+ " ist bereits ein Forcastauftrag im Status 'Angelegt' vorhanden.";
								} else {

									if (forcast_i_id_angelegt == null) {

										ForecastauftragDto fcaDto = new ForecastauftragDto();
										fcaDto.setFclieferadresseIId(flrFCLieferadresse.getI_id());
										fcaDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

										forcast_i_id_angelegt = getForecastFac().createForecastauftrag(fcaDto);

									}
									hsNeuAngelegteForecastauftraege.add(forcast_i_id_angelegt);

									// Artikelnummer
									String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											spalteArtikelnummer, 40, i);

									ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelnummer,
											theClientDto.getMandant());

									if (aDto != null) {
										// Mengen (ab Spalte K)

										if (sZeile.length > 10) {
											for (int s = 10; s < sZeile.length; s++) {

												Iterator spaltennamen = hmVorhandeneSpalten.keySet().iterator();

												while (spaltennamen.hasNext()) {

													String spaltenname = (String) spaltennamen.next();

													Integer spaltennummer = hmVorhandeneSpalten.get(spaltenname);

													if (spaltennummer == s && spaltenname.contains("-")) {

														String[] jahrMonat = spaltenname.split("-");

														if (jahrMonat.length > 1) {

															String jahr = jahrMonat[0];

															String monat = jahrMonat[1];

															try {
																Integer iJahr = Integer.parseInt(jahr);

																Integer iMonat = Integer.parseInt(monat);

																Calendar cal = Calendar.getInstance();
																cal.set(iJahr, iMonat - 1, 1, 0, 0, 0);

																if (cal.getTime().after(new java.util.Date())) {

																	Cell c = sZeile[s];

																	if (c != null && c.getContents() != null
																			&& c.getContents().length() > 0) {

																		if (c.getType() == CellType.NUMBER || c
																				.getType() == CellType.NUMBER_FORMULA) {

																			double d = ((NumberCell) c).getValue();

																			ForecastpositionDto fpDto = new ForecastpositionDto();
																			fpDto.setForecastauftragIId(
																					forcast_i_id_angelegt);
																			fpDto.setNMenge(Helper.rundeKaufmaennisch(
																					new BigDecimal(d),
																					getMandantFac()
																							.getNachkommastellenMenge(
																									theClientDto
																											.getMandant())));

																			fpDto.setStatusCNr(
																					LocaleFac.STATUS_ANGELEGT);

																			fpDto.setTTermin(new Timestamp(
																					cal.getTimeInMillis()));
																			fpDto.setArtikelIId(aDto.getIId());

																			getForecastFac()
																					.createForecastposition(fpDto);

																			iZeilenImportiert++;

																		} else {

																			sFehler = "Wert " + c.getContents()
																					+ " in Spalte " + spaltenname
																					+ " keine gueltige Zahl";
																		}

																	}

																}

															} catch (NumberFormatException e) {
																sFehler = "Spaltenname " + spaltenname
																		+ " konnte nicht in Jahr/Monat zerlegt werden.";
															}

														}

													}

												}
											}
										}
									} else {
										sFehler = "Artikelnummer " + artikelnummer + " konnte nicht gefunden werden.";
									}
								}
								// Es darf kein angelegter Forecastauftrag vorhanden sein

							} else {
								sFehler = "Es konnte kein Forecast f\u00fcr den Kunden "
										+ HelperServer.formatNameAusFLRPartner(flrKunde.getFlrpartner()) + " (Kbez: "
										+ flrKunde.getFlrpartner().getC_kbez() + ")" + " und die Lieferadresse "
										+ HelperServer.formatNameAusFLRPartner(flrKundeLieferadresse.getFlrpartner())
										+ " (Kbez: " + flrKundeLieferadresse.getFlrpartner().getC_kbez() + ")"
										+ " gefunden werden";
							}

						} else if (results2.size() == 0) {
							sFehler = "Es wurde kein Kunde (Lieferadresse) gefunden, welchen in der Kurzbezeichnung '"
									+ forcastKunde + "' enth\u00e4lt.";
						} else {
							sFehler = "Es wurden mehrere Kunden (Lieferadresse) gefunden, welche in der Kurzbezeichnung '"
									+ forcastKunde + "' enthalten.";
						}

					} else if (results.size() == 0) {
						sFehler = "Es wurde kein Kunde gefunden, welchen in der Kurzbezeichnung '" + forcastKunde
								+ "' enth\u00e4lt.";
					} else {
						sFehler = "Es wurden mehrere Kunden gefunden, welche in der Kurzbezeichnung '" + forcastKunde
								+ "' enthalten.";
					}

					if (sFehler.length() > 0) {

						sFehler += " (Zeile " + (i + 1) + ")";

						break;
					}

				}

			}

			if (sFehler.length() > 0) {
				ArrayList alInfo = new ArrayList();
				alInfo.add(sFehler);

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VMI_IMPORT, alInfo, new Exception(sFehler));
			}
		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
		return iZeilenImportiert;
	}

	public int importiereVMI_CC_XLS(byte[] xlsDatei, Integer forecastauftragIId, TheClientDto theClientDto) {

		int iZeilenImportiert = 0;
		try {
			byte[] CRLFAscii = { 13, 10 };

			ForecastauftragDto fcaDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);
			FclieferadresseDto fcLAdrDto = getForecastFac()
					.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());
			ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(fcLAdrDto.getForecastIId());

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			// String spalteArtikelnummer = "Artikelnummer Lieferant"; // =HV-Artikelnummer

			String spalteArtikelnummer = "Artikelnummer Kunde"; // =HV-Artikelnummer

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			String sFehler = "";

			if (hmVorhandeneSpalten.containsKey(spalteArtikelnummer) == false) {
				sFehler += "Die Spalte '" + spalteArtikelnummer + "' muss vorhanden sein!" + new String(CRLFAscii);

			} else {
				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					if (sZeile[0].getContents().trim().length() == 0) {
						continue;
					}

					// Artikelnummer
					String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteArtikelnummer, 40, i);

					ArtikelDto aDto = null;

					ArtikelDto[] aDtosAnhandReferenznummer = getArtikelFac()
							.artikelfindByCReferenznrMandantCNrOhneExc(artikelnummer, theClientDto.getMandant());

					// PJ22122
					if (aDtosAnhandReferenznummer.length == 0) {
						// Nochmal nach kundesoko suchen

						String sQuery = "SELECT distinct s.artikel_i_id FROM FLRKundesoko s WHERE s.kunde_i_id="
								+ fcDto.getKundeIId() + " AND s.artikel_i_id IS NOT NULL AND s.c_kundeartikelnummer='"
								+ artikelnummer + "' AND s.flrartikel.mandant_c_nr='" + theClientDto.getMandant()
								+ "' AND  s.t_preisgueltigab<='"
								+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis())) + "'";

						sQuery += " ORDER BY s.artikel_i_id ASC";

						Session session = FLRSessionFactory.getFactory().openSession();

						Query inventurliste = session.createQuery(sQuery);

						List<?> resultList = inventurliste.list();

						Iterator<?> resultListIterator = resultList.iterator();

						aDtosAnhandReferenznummer = new ArtikelDto[resultList.size()];

						if (resultListIterator.hasNext()) {
							Integer artikelIId = (Integer) resultListIterator.next();
							aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
						}
					} else {
						aDto = aDtosAnhandReferenznummer[0];
					}

					if (aDto != null) {
						// Mengen (ab Spalte N)

						if (sZeile.length > 13) {

							for (int s = 13; s < sZeile.length; s++) {

								Iterator spaltennamen = hmVorhandeneSpalten.keySet().iterator();

								while (spaltennamen.hasNext()) {

									String spaltenname = (String) spaltennamen.next();

									Integer spaltennummer = hmVorhandeneSpalten.get(spaltenname);

									if (spaltennummer == s && spaltenname.contains("-")) {

										String[] jahrMonat = spaltenname.split("-");

										if (jahrMonat.length > 1) {

											String jahr = jahrMonat[0];

											String monat = jahrMonat[1];

											try {
												Integer iJahr = Integer.parseInt(jahr);

												Integer iMonat = Integer.parseInt(monat);

												Calendar cal = Calendar.getInstance();
												cal.set(iJahr, iMonat - 1, 1, 0, 0, 0);

												if (cal.getTime().after(new java.util.Date())) {

													Cell c = sZeile[s];

													if (c != null && c.getContents() != null
															&& c.getContents().length() > 0) {

														if (c.getType() == CellType.NUMBER
																|| c.getType() == CellType.NUMBER_FORMULA) {

															double d = ((NumberCell) c).getValue();

															ForecastpositionDto fpDto = new ForecastpositionDto();
															fpDto.setForecastauftragIId(forecastauftragIId);
															fpDto.setNMenge(Helper.rundeKaufmaennisch(new BigDecimal(d),
																	getMandantFac().getNachkommastellenMenge(
																			theClientDto.getMandant())));

															fpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

															fpDto.setTTermin(new Timestamp(cal.getTimeInMillis()));
															fpDto.setArtikelIId(aDto.getIId());

															getForecastFac().createForecastposition(fpDto);

															iZeilenImportiert++;

														} else {

															sFehler = "Wert " + c.getContents() + " in Spalte "
																	+ spaltenname + " keine gueltige Zahl";
														}

													}

												}

											} catch (NumberFormatException e) {
												sFehler = "Spaltenname " + spaltenname
														+ " konnte nicht in Jahr/Monat zerlegt werden.";
											}

										}

									}

								}
							}
						}
					} else {
						sFehler = "Artikelnummer " + artikelnummer
								+ " konnte weder anhand der Referenznummer bzw. Kunden-Soko gefunden werden.";
					}

					if (sFehler.length() > 0) {

						sFehler += " (Zeile " + (i + 1) + ")";

						break;
					}

				}

			}

			if (sFehler.length() > 0) {
				ArrayList alInfo = new ArrayList();
				alInfo.add(sFehler);

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VMI_IMPORT, alInfo, new Exception(sFehler));
			}
		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
		return iZeilenImportiert;
	}

	public ForecastImportFehlerDto importiereRollierendePlanung_XLS(byte[] xlsDatei, Integer forecastauftragIId,
			boolean bReferenznummer, boolean bImportierenWennKeinFehler, TheClientDto theClientDto) {

		ForecastImportFehlerDto fiErrorDto = new ForecastImportFehlerDto();

		String rueckgabe = "";
		try {
			byte[] CRLFAscii = { 13, 10 };

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			HashMap<Integer, java.sql.Date> hmMengenSpalten = new HashMap<Integer, java.sql.Date>();

			String spalteArtikelnummer = "Artikel Nr."; // =Epsilon-Artikelnummer

			if (sheet.getRows() > 8) {
				Cell[] sZeile = sheet.getRow(7);
				Cell[] sNaechsteZeile = sheet.getRow(8);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {

						String sSpaltename = sZeile[i].getContents().trim();

						hmVorhandeneSpalten.put(sSpaltename, new Integer(i));
					}

					if (i > 8) {

						String jahr = sZeile[i].getContents();

						String monat = sNaechsteZeile[i].getContents();

						try {
							Integer iJahr = Integer.parseInt(jahr);

							Integer iMonat = Integer.parseInt(monat);

							Calendar cal = Calendar.getInstance();
							cal.set(iJahr, iMonat - 1, 1, 0, 0, 0);

							if (cal.getTime().after(new java.util.Date())) {

								hmMengenSpalten.put(i, new Date(cal.getTimeInMillis()));
							}

						} catch (NumberFormatException e) {
							// KEINE ZAHL
						}
						Calendar c = Calendar.getInstance();

						// hmMengenSpalten.put(i, value);
					}

				}

			}

			if (hmMengenSpalten.size() == 0) {
				rueckgabe = "Es wurden keine Monate >= heute gefunden!" + new String(CRLFAscii);
			}

			if (hmVorhandeneSpalten.containsKey(spalteArtikelnummer) == false
					&& hmVorhandeneSpalten.containsKey(spalteArtikelnummer) == false) {
				rueckgabe += "Die Spalte '" + spalteArtikelnummer + "/" + spalteArtikelnummer + "' muss vorhanden sein!"
						+ new String(CRLFAscii);

			} else {
				ForecastauftragDto fcaDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);
				FclieferadresseDto fcLAdrDto = getForecastFac()
						.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());
				ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(fcLAdrDto.getForecastIId());

				for (int i = 9; i < sheet.getRows(); i++) {

					String sFehler = "";

					Cell[] sZeile = sheet.getRow(i);

					if (sZeile.length > 21 && sZeile[3].getContents().equals("Demand planned")) {
						// Mengenspalten

						// SP8667 Wenn Artikelnummer in 'Demand Planned', dann sind das neue Teile
						// -> am Client als Warnung ausgeben

						String artikelnummerInDemandPlanned = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								spalteArtikelnummer, 40, i);
						if (artikelnummerInDemandPlanned != null && artikelnummerInDemandPlanned.length() > 0) {

							fiErrorDto.addNeueArtikel("Zeile " + (i + 1) + ": " + artikelnummerInDemandPlanned);

							continue;
						}
					}

					if (sZeile.length > 21 && sZeile[3].getContents().equals("Order")) {

						// Artikelnummer
						String artikelnummerOderReferenznummer = null;

						if (bReferenznummer == false) {
							artikelnummerOderReferenznummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									spalteArtikelnummer, 40, i);
						} else if (bReferenznummer) {
							// PJ21943

							Cell[] sVorherigeZeile = sheet.getRow(i - 1);

							artikelnummerOderReferenznummer = getStringAusXLS(sVorherigeZeile, hmVorhandeneSpalten,
									spalteArtikelnummer, 40, i - 1);
						}

						if (artikelnummerOderReferenznummer != null) {

							// Naechste Zeile
							if (sheet.getRows() > i) {

								// Wenn naechset Zeile vorhanden
								if (sheet.getRows() > i + 1) {

									Cell[] sNaechsteZeile = sheet.getRow(i + 1);
									if (sNaechsteZeile.length > 21
											&& sNaechsteZeile[3].getContents().equals("Demand planned")) {
										// Mengenspalten

										// Artikelnummer kommt aus KBEZ
										ArtikelDto aDto = null;

										ArtikelDto[] aDtosAnhandReferenznummer = null;

										if (bReferenznummer == false) {
											aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(
													artikelnummerOderReferenznummer, theClientDto.getMandant());
										} else {
											aDtosAnhandReferenznummer = getArtikelFac()
													.artikelfindByCReferenznrMandantCNrOhneExc(
															artikelnummerOderReferenznummer, theClientDto.getMandant());

											// PJ22122
											if (aDtosAnhandReferenznummer.length == 0) {
												// Nochmal nach kundesoko suchen

												String sQuery = "SELECT distinct s.artikel_i_id FROM FLRKundesoko s WHERE s.kunde_i_id="
														+ fcDto.getKundeIId()
														+ " AND s.artikel_i_id IS NOT NULL AND s.c_kundeartikelnummer='"
														+ artikelnummerOderReferenznummer
														+ "' AND s.flrartikel.mandant_c_nr='"
														+ theClientDto.getMandant() + "' AND  s.t_preisgueltigab<='"
														+ Helper.formatDateWithSlashes(
																new java.sql.Date(System.currentTimeMillis()))
														+ "'";

												sQuery += " ORDER BY s.artikel_i_id ASC";

												Session session = FLRSessionFactory.getFactory().openSession();

												Query inventurliste = session.createQuery(sQuery);

												List<?> resultList = inventurliste.list();

												Iterator<?> resultListIterator = resultList.iterator();

												aDtosAnhandReferenznummer = new ArtikelDto[resultList.size()];

												int k = 0;
												while (resultListIterator.hasNext()) {
													Integer artikelIId = (Integer) resultListIterator.next();
													aDtosAnhandReferenznummer[k] = getArtikelFac()
															.artikelFindByPrimaryKey(artikelIId, theClientDto);
													k++;
												}
											}

											if (aDtosAnhandReferenznummer.length == 1) {
												aDto = aDtosAnhandReferenznummer[0];
											}
										}

										if (aDto != null) {

											Iterator it = hmMengenSpalten.keySet().iterator();

											while (it.hasNext()) {
												Integer ispalte = (Integer) it.next();

												Date d = hmMengenSpalten.get(ispalte);

												Cell c = sNaechsteZeile[ispalte];

												if (c != null && c.getContents() != null
														&& c.getContents().length() > 0) {

													if (c.getType() == CellType.NUMBER
															|| c.getType() == CellType.NUMBER_FORMULA) {

														double dMenge = ((NumberCell) c).getValue();

														if (dMenge > 0) {

															if (bImportierenWennKeinFehler) {

																ForecastpositionDto fpDto = new ForecastpositionDto();
																fpDto.setForecastauftragIId(forecastauftragIId);
																fpDto.setNMenge(Helper.rundeKaufmaennisch(
																		new BigDecimal(dMenge),
																		getMandantFac().getNachkommastellenMenge(
																				theClientDto.getMandant())));

																fpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

																fpDto.setTTermin(Helper
																		.cutTimestamp(new Timestamp(d.getTime())));
																fpDto.setArtikelIId(aDto.getIId());

																getForecastFac().createForecastposition(fpDto);

															}
														}

													} else {

														sFehler = "Wert in Spalte " + ispalte
																+ " enthaelt keine gueltige Zahl";
													}

												}

											}
										} else {

											if (bReferenznummer == false) {
												sFehler = "Artikel '" + artikelnummerOderReferenznummer
														+ "' konnte nicht gefunden werden.";
											} else {

												if (aDtosAnhandReferenznummer != null
														&& aDtosAnhandReferenznummer.length > 1) {
													sFehler = "Es wurden mehrere Artikel anhand der Referenznummer/Kundensoko '"
															+ artikelnummerOderReferenznummer + "' gefunden.";
												} else {
													sFehler = "Es konnte kein Artikel anhand der Referenznummer/Kundensoko '"
															+ artikelnummerOderReferenznummer + "' gefunden werden.";
												}
											}

										}

									}

								} else {
									if (sheet.getRows() == (i + 1)) {
										// Ausser es ist die letzte Zeile
									} else {
										sFehler = "Auf 'Order' muss es eine darauffolgende Zeile mit 'Demand planned' geben.";
									}

								}

							}

							// Es darf kein angelegter Forecastauftrag vorhanden sein
							if (sFehler.length() > 0) {

								sFehler += " (Zeile " + (i + 1) + ")";
							}
						}
					}

					if (sFehler.length() > 0) {

						rueckgabe += sFehler + new String(CRLFAscii);

					}
				}

			}

		} catch (

		BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		fiErrorDto.setError(rueckgabe);

		return fiErrorDto;
	}

	public int importiereEpsilon_XLS(byte[] xlsDatei, Integer forecastauftragIId, TheClientDto theClientDto) {

		int iZeilenImportiert = 0;
		try {
			byte[] CRLFAscii = { 13, 10 };

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			String maxFertigungslos = "max. Fertigungslos";
			String spalteArtikelnummer = "Teilenummer"; // =Epsilon-Artikelnummer

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {

						String sSpaltename = sZeile[i].getContents().trim();
						if (sSpaltename.startsWith(maxFertigungslos)) {
							sSpaltename = maxFertigungslos;
						}

						hmVorhandeneSpalten.put(sSpaltename, new Integer(i));
					}

				}

			}

			String sFehler = "";

			if (hmVorhandeneSpalten.containsKey(maxFertigungslos) == false
					&& hmVorhandeneSpalten.containsKey(spalteArtikelnummer) == false) {
				sFehler += "Die Spalten '" + maxFertigungslos + "/" + spalteArtikelnummer
						+ "' m\u00fcssen vorhanden sein!" + new String(CRLFAscii);

			} else {

				// Um des Kunden zu suchen, suchen wir in der Partner-KBez mit %LIKE% nach dem
				// letzten wort aus der Spalte Kunde bzw. Lagerort, hier muss es genau ein
				// Ergebnis geben, ansonsten Fehler

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					// Artikelnummer
					String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteArtikelnummer, 40, i);

					if (artikelnummer != null) {
						// Artikelnummer kommt aus KBEZ
						List<ArtikelDto> alAdto = getArtikelFac().artikelFindByCKBezOhneExc(artikelnummer,
								theClientDto);

						if (alAdto.size() == 1) {
							// Menge Spalte M

							BigDecimal bdMenge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, maxFertigungslos, i);

							if (bdMenge != null) {
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.DATE, 1);
								cal.add(Calendar.MONTH, 4);

								ForecastpositionDto fpDto = new ForecastpositionDto();
								fpDto.setForecastauftragIId(forecastauftragIId);
								fpDto.setNMenge(
										Helper.rundeKaufmaennisch(new BigDecimal(Math.ceil(bdMenge.doubleValue())),
												getMandantFac().getNachkommastellenMenge(theClientDto.getMandant())));

								fpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

								fpDto.setTTermin(Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis())));
								fpDto.setArtikelIId(alAdto.get(0).getIId());

								getForecastFac().createForecastposition(fpDto);

								iZeilenImportiert++;

							} else {

								sFehler = "Wert  in Spalte " + maxFertigungslos + " keine gueltige Zahl";
							}

						} else {

							if (alAdto.size() == 0) {

								sFehler = "Artikel mit Kurzbezeichnung '" + artikelnummer
										+ "' konnte nicht gefunden werden.";
							} else {
								sFehler = "Es wurden mehrere Artikel mit Kurzbezeichnung '" + artikelnummer
										+ "' gefunden.";
							}
						}

						// Es darf kein angelegter Forecastauftrag vorhanden sein
						if (sFehler.length() > 0) {

							sFehler += " (Zeile " + (i + 1) + ")";

							break;
						}
					}
				}
			}

			if (sFehler.length() > 0) {
				ArrayList alInfo = new ArrayList();
				alInfo.add(sFehler);

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EPSILON_IMPORT, alInfo, new Exception(sFehler));
			}
		} catch (

		BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
		return iZeilenImportiert;
	}

	public CallOffXlsImporterResult importCallOffDailyXls(Integer forecastIId, byte[] xlsDatei, boolean checkOnly,
			Integer startRow, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(forecastIId, "forecastIId");
		Validator.notNull(xlsDatei, "xlsDatei");
		Validator.notNull(startRow, "startRow");

		ICallOffXlsImporterBeanService beanService = new CallOffXlsImporterBeanService(theClientDto, getForecastFac(),
				getArtikelFac(), getPartnerServicesFac());

		CallOffXLSImporter forecastImporter = new CallOffXLSImporter(beanService, new CallOffDailyXLSTransformer());

		if (checkOnly) {
			return forecastImporter.checkXLSDaten(xlsDatei, startRow, forecastIId);
		} else {
			return forecastImporter.importXLSDaten(xlsDatei, startRow, forecastIId);
		}
	}

	@Override
	public CallOffXlsImporterResult importCallOffWeeklyXls(Integer fclieferadresseIId, byte[] xlsDatei,
			boolean checkOnly, Integer startRow, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(fclieferadresseIId, "fclieferadresseIId");
		Validator.notNull(xlsDatei, "xlsDatei");
		Validator.notNull(startRow, "startRow");

		ICallOffXlsImporterBeanService beanService = new CallOffXlsImporterBeanService(theClientDto, getForecastFac(),
				getArtikelFac(), getPartnerServicesFac());

		CallOffXLSImporter forecastImporter = new CallOffXLSImporter(beanService, new CallOffWeeklyXLSTransformer());

		if (checkOnly) {
			return forecastImporter.checkXLSDaten(xlsDatei, startRow, fclieferadresseIId);
		} else {
			return forecastImporter.importXLSDaten(xlsDatei, startRow, fclieferadresseIId);
		}
	}

	@Override
	public CallOffXlsImporterResult importLinienabrufEluxDeTxt(Integer fclieferadresseIId, List<String> inputLines,
			boolean checkOnly, Date deliveryDate, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(fclieferadresseIId, "fclieferadresseIId");
		Validator.notNull(inputLines, "inputLines");
		Validator.notNull(deliveryDate, "deliveryDate");

		ILinienabrufImporterBeanService beanService = new LinienabrufImporterBeanService(theClientDto, getForecastFac(),
				getArtikelFac());
		LinienabrufEluxDeTxtImporter importer = new LinienabrufEluxDeTxtImporter(beanService, fclieferadresseIId,
				deliveryDate);

		return checkOnly ? importer.checkContent(inputLines) : importer.importContent(inputLines);
	}

	@Override
	public CallOffXlsImporterResult importDelforEdifact(Integer forecastId, List<EdiFileInfo> ediContent,
			boolean checkOnly, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(forecastId, "forecastId");
		Validator.notNull(ediContent, "ediContent");

		CallOffXlsImporterResult result = new CallOffXlsImporterResult(new ArrayList<EJBLineNumberExceptionLP>(),
				new CallOffXlsImportStats());

		List<DelforRepository> repos = new ArrayList<DelforRepository>();
		for (EdiFileInfo fileInfo : ediContent) {
			try {
				DelforRepository repo = transformOneDelfor(fileInfo, checkOnly, theClientDto);
				repos.add(repo);
			} catch (EdifactException e) {
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add("EdifactException: " + e.getMessage());
				result.getMessages()
						.add(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR,
								new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, clientInfo,
										null)));
				result.getStats().incrementErrorCounts();
				myLogger.error("EdifactException importing '" + fileInfo.getName() + "' with "
						+ fileInfo.getContent().length + " bytes", e);
			} catch (IOException e) {
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add("IOException: " + e.getMessage());
				result.getMessages()
						.add(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR,
								new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, clientInfo,
										null)));
				result.getStats().incrementErrorCounts();
				myLogger.error("IOException importing '" + fileInfo.getName() + "' with " + fileInfo.getContent().length
						+ " bytes", e);
			} catch (Throwable t) {
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add("Throwable: " + t.getMessage());
				result.getMessages()
						.add(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR,
								new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, clientInfo,
										null)));
				result.getStats().incrementErrorCounts();
				myLogger.error("Throwable importing '" + fileInfo.getName() + "' with " + fileInfo.getContent().length
						+ " bytes", t);
			}
		}

		removeDuplicateEntries(repos);

		if (checkOnly) {
			return createResultFromRepInfo(result, repos);
		}

		Map<Integer, DelforRepoContainer> mergedRepos = mergeDelforRepositories(repos);
		importMergedRepos(forecastId, mergedRepos, theClientDto);

		return createResultFromRepInfo(result, repos);
		// return new CallOffXlsImporterResult(new
		// ArrayList<EJBLineNumberExceptionLP>(), new CallOffXlsImportStats());
	}

	private void removeDuplicateEntries(List<DelforRepository> repos) {
		if (repos.size() < 2)
			return;

		Collections.sort(repos, new Comparator<DelforRepository>() {
			@Override
			public int compare(DelforRepository o1, DelforRepository o2) {
				return compareRepos(o1, o2);
			}
		});

		for (int i = 1; i < repos.size(); i++) {
			DelforRepository repo = repos.get(i);
			for (int j = i - 1; j >= 0; j--) {
				DelforRepository otherRepo = repos.get(j);
				for (KundeDto deliveryAddress : repo.getDeliveryAddresses()) {
					for (DelforPosition position : new DistinctDelforItemIterator(repo.getPositions(deliveryAddress))) {
						if (otherRepo.hasItem(position, deliveryAddress)) {
//							boolean b = isRepo2Newer(otherRepo, repo);
//							if(b) {
							EJBExceptionLP exLp = new EJBExceptionLP(
									EJBExceptionLP.FEHLER_EDIFACT_ARTIKEL_BESTELLNUMMER_MEHRFACH_VORHANDEN, "", "",
									position.getItemDto().getCNr(), position.getOrderReference(), otherRepo.getName(),
									repo.getName());
							repo.getResult().getMessages().add(new EJBLineNumberExceptionLP(repo.getName(), 0,
									EJBLineNumberExceptionLP.SEVERITY_WARNING, exLp));
							otherRepo.removeMine(position, deliveryAddress);
//							}
						}
					}
				}
			}
		}
	}

	private int compareRepos(DelforRepository repo1, DelforRepository repo2) {
		int diff = repo1.getCreationDate().compareTo(repo2.getCreationDate());
		if (diff == 0) {
			try {
				Long l1 = Long.parseLong(repo1.getControlReference());
				Long l2 = Long.parseLong(repo2.getControlReference());

				return l1.compareTo(l2);
			} catch (NumberFormatException e) {
				return repo1.getControlReference().compareTo(repo2.getControlReference());
			}
		}
		return diff;
	}

	private boolean isRepo2Newer(DelforRepository repo1, DelforRepository repo2) {
		int diff = repo2.getCreationDate().compareTo(repo1.getCreationDate());
		if (diff == 0) {
			try {
				long l1 = Long.parseLong(repo1.getControlReference());
				long l2 = Long.parseLong(repo2.getControlReference());

				return l2 > l1;
			} catch (NumberFormatException e) {
				return repo2.getControlReference().compareTo(repo1.getControlReference()) > 0;
			}
		}
		return diff > 0;
	}

	private CallOffXlsImporterResult createResultFromRepInfo(CallOffXlsImporterResult result,
			List<DelforRepository> repos) {
		for (DelforRepository delforRepository : repos) {
			result.getMessages().addAll(delforRepository.getResult().getMessages());
			int sum = result.getStats().getErrorCounts() + delforRepository.getResult().getStats().getErrorCounts();
			result.getStats().setErrorCounts(sum);
			sum = result.getStats().getErrorExports() + delforRepository.getResult().getStats().getErrorExports();
			result.getStats().setErrorExports(sum);
			sum = result.getStats().getTotalExports() + delforRepository.getResult().getStats().getTotalExports();
			result.getStats().setTotalExports(sum);
		}
		return result;
	}

	private void importMergedRepos(Integer forecastId, Map<Integer, DelforRepoContainer> mergedRepos,
			TheClientDto theClientDto) {
		Map<Integer, ForecastauftragDto> mapAuftraege = new HashMap<Integer, ForecastauftragDto>();

		for (DelforRepoContainer delforContainer : mergedRepos.values()) {
			Integer adrId;
			FclieferadresseDto adrDto = getForecastFac().fclieferadresseFindByForecastIdLieferadresseIdOhneExc(
					forecastId, delforContainer.getKundeDto().getIId());
			if (adrDto == null) {
				adrDto = new FclieferadresseDto();
				adrDto.setForecastIId(forecastId);
				adrDto.setKundeIIdLieferadresse(delforContainer.getKundeDto().getIId());
				adrId = getForecastFac().createFclieferadresse(adrDto);
			} else {
				adrId = adrDto.getiId();
			}

			ForecastauftragDto auftragDto = mapAuftraege.get(adrId);
			if (auftragDto == null) {
				List<ForecastauftragDto> auftragDtos = getForecastFac()
						.forecastauftragFindByFclieferadresseIIdStatusCNr(adrId, LocaleFac.STATUS_ANGELEGT);
				if (!auftragDtos.isEmpty()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_AUFTRAG_MIT_STATUS_ANGELEGT,
							forecastId.toString());
				}

				auftragDto = new ForecastauftragDto();
				auftragDto.setFclieferadresseIId(adrId);
				auftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
				Integer id = getForecastFac().createForecastauftrag(auftragDto);
				auftragDto = getForecastFac().forecastauftragFindByPrimaryKey(id);
				mapAuftraege.put(adrId, auftragDto);
			}

			for (DelforPosition delforPos : delforContainer.getPositions()) {
				ForecastpositionDto fcBacklogDto = createBacklogForecastpositionDto(auftragDto.getIId(), delforPos);
				if (fcBacklogDto != null) {
					getForecastFac().createForecastposition(fcBacklogDto);
				}
				ForecastpositionDto fcPositionDto = createForecastpositionDto(auftragDto.getIId(), delforPos);
				getForecastFac().createForecastposition(fcPositionDto);
			}

			for (DelforCumulativePosition cumulativePos : delforContainer.getCumulativePositions()) {
				LiefermengenDto lmDto = createLiefermengenDto(adrDto, cumulativePos);
				getPartnerServicesFac().createLiefermengenUnique(lmDto);
			}
		}
	}

	private ForecastpositionDto createBacklogForecastpositionDto(Integer forecastAuftragId, DelforPosition delforPos) {
		if (delforPos.getBacklogQuantity() != null) {
			ForecastpositionDto posDto = createForecastpositionDto(forecastAuftragId, delforPos);
			posDto.setTTermin(getTimestamp());
			posDto.setNMenge(delforPos.getBacklogQuantity());
			posDto.setXKommentar("Backlog");
			return posDto;
		}

		return null;
	}

	private ForecastpositionDto createForecastpositionDto(Integer forecastAuftragId, DelforPosition delforPos) {
		ForecastpositionDto fcPositionDto = new ForecastpositionDto();
		fcPositionDto.setForecastauftragIId(forecastAuftragId);
		fcPositionDto.setArtikelIId(delforPos.getItemDto().getIId());
		fcPositionDto.setCBestellnummer(delforPos.getOrderReference());
		fcPositionDto.setForecastartCNr(ForecastFac.FORECASTART_NICHT_DEFINIERT);
		fcPositionDto.setNMenge(delforPos.getQuantity());
		fcPositionDto.setTTermin(new Timestamp(delforPos.getDate().getTime()));
		fcPositionDto.setXKommentar(createXKommentar(delforPos.getTexts()));
		fcPositionDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		return fcPositionDto;
	}

	private LiefermengenDto createLiefermengenDto(FclieferadresseDto adrDto, DelforCumulativePosition cumulativePos) {
		LiefermengenDto lmDto = new LiefermengenDto();
		lmDto.setArtikelIId(cumulativePos.getItemDto().getIId());
		lmDto.setCLstext(cumulativePos.getReference());
		lmDto.setKundeIIdLieferadresse(adrDto.getKundeIIdLieferadresse());
		lmDto.setNMenge(cumulativePos.getQuantity());
		lmDto.setTDatum(new Timestamp(cumulativePos.getDate().getTime()));

		return lmDto;
	}

	private String createXKommentar(List<String> texts) {
		StringBuffer sb = new StringBuffer();
		for (String string : texts) {
			if (sb.length() > 0) {
				sb.append("\r\n");
			}
			sb.append(string);
		}
		return sb.toString();
	}

	private DelforRepository transformOneDelfor(EdiFileInfo ediFileInfo, boolean checkonly, TheClientDto theClientDto)
			throws EdifactException, IOException {
		EdifactReader reader = new EdifactReader();
		reader.setInputStream(new ByteArrayInputStream(ediFileInfo.getContent()));

		DelforRepository repository = new DelforRepository(ediFileInfo.getName());
		IEjbService delforService = new EdifactDelforService(theClientDto, getArtikelFac(), getKundeFac());
		EdifactInterpreter interpreter = new EdifactInterpreter(reader);

		EdifactProgram pgm99b = new EdifactProgramDelfor99bHV(delforService, repository);
		interpreter.registerUserProgram(new UnhInfoDelfor99b(), pgm99b);
		EdifactProgram pgm911 = new EdifactProgramDelfor911HV(delforService, repository);
		interpreter.registerUserProgram(new UnhInfoDelfor911(), pgm911);

		EdifactMessage msg = interpreter.interpret();
		repository.setCreationDate(msg.getUnb().getDate());
		repository.setControlReference(msg.getUnb().getControlReference());

		myLogger.info("Repo " + repository.getName() + " has '" + repository.getDeliveryAddresses().size()
				+ "' different addresses");
		return repository;
	}

	private Map<Integer, DelforRepoContainer> mergeDelforRepositories(List<DelforRepository> repos) {
		myLogger.info("About to merge '" + repos.size() + "' repositories.");
		Map<Integer, DelforRepoContainer> mergedRepos = new HashMap<Integer, DelforRepoContainer>();
		for (DelforRepository repo : repos) {
			for (KundeDto lieferKundeDto : repo.getDeliveryAddresses()) {
				DelforRepoContainer container = mergedRepos.get(lieferKundeDto.getIId());
				if (container == null) {
					container = new DelforRepoContainer(lieferKundeDto, repo.getPositions(lieferKundeDto),
							repo.getCumulativePositions(lieferKundeDto));
					mergedRepos.put(lieferKundeDto.getIId(), container);
				} else {
					container.getPositions().addAll(repo.getPositions(lieferKundeDto));
					container.getCumulativePositions().addAll(repo.getCumulativePositions(lieferKundeDto));
				}
			}
		}

		myLogger.info("Merged '" + repos.size() + "' to '" + mergedRepos.size() + " addresses");
		return mergedRepos;
	}

	public class DelforRepoContainer {
		private KundeDto kundeDto;
		private List<DelforPosition> positions;
		private List<DelforCumulativePosition> cumulativePositions;

		public DelforRepoContainer(KundeDto kundeDto, List<DelforPosition> positions,
				List<DelforCumulativePosition> cumulativePositions) {
			this.kundeDto = kundeDto;
			this.positions = positions;
			this.cumulativePositions = cumulativePositions;
		}

		public KundeDto getKundeDto() {
			return kundeDto;
		}

		public void setKundeDto(KundeDto kundeDto) {
			this.kundeDto = kundeDto;
		}

		public List<DelforPosition> getPositions() {
			return positions;
		}

		public void setPositions(List<DelforPosition> positions) {
			this.positions = positions;
		}

		public List<DelforCumulativePosition> getCumulativePositions() {
			return cumulativePositions;
		}

		public void setCumulativePositions(List<DelforCumulativePosition> cumulativePositions) {
			this.cumulativePositions = cumulativePositions;
		}
	}

	@Override
	public CallOffXlsImporterResult importFiles(List<IForecastImportFile<?>> files, Integer fclieferadresseId,
			boolean checkOnly, TheClientDto theClientDto) {
		List<EJBLineNumberExceptionLP> messages = new ArrayList<EJBLineNumberExceptionLP>();
		CallOffXlsImportStats stats = new CallOffXlsImportStats();
		CallOffXlsImporterResult result = new CallOffXlsImporterResult(messages, stats);

		ICallOffXlsImporterBeanService beanService = new CallOffXlsMultiImporterBeanService(theClientDto,
				getForecastFac(), getArtikelFac(), getPartnerServicesFac());

		rearrangeFiles(files);

		FclieferadresseDto fcAdrDto = getForecastFac().fclieferadresseFindByPrimaryKey(fclieferadresseId);

		for (IForecastImportFile<?> importFile : files) {
			myLogger.info("Verarbeite '" + importFile.getFilename() + "'...");
			try {
				IImporter importer = createImporter(beanService, importFile, fclieferadresseId, theClientDto);
				if (importer == null) {
					EJBExceptionLP exLP = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNBEKANNTE_FORECAST_ART,
							"Unbekanntes Dateiformat '" + importFile.getFilename() + "'", importFile.getFilename());
					messages.add(new EJBLineNumberExceptionLP(importFile.getFilename(), 0, exLP));
					stats.incrementErrorCounts();
				} else {
					if (Helper.short2boolean(fcAdrDto.getBKommissionieren())
							&& ForecastImportFileType.Linienabruf.equals(importFile.getFiletype())) {
						EJBExceptionLP exLP = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FORECAST_IMPORT_LINIENABRUF_MIT_KOMMISSIONIEREN_VORHANDEN, "");
						messages.add(new EJBLineNumberExceptionLP(importFile.getFilename(), 0,
								EJBLineNumberExceptionLP.SEVERITY_ERROR, exLP));
						stats.incrementErrorCounts();
						continue;
					}
					CallOffXlsImporterResult rc = checkOnly ? importer.doVerify(theClientDto)
							: importer.doImport(theClientDto);
					for (EJBLineNumberExceptionLP exInfo : rc.getMessages()) {
						exInfo.setFilename(importFile.getFilename());
					}
					messages.addAll(rc.getMessages());
					stats.addStats(rc.getStats());

					myLogger.info("Verarbeitung von '" + importFile.getFilename() + "' ergab " + rc.getMessages().size()
							+ " Benachrichtigungen, " + "(T:" + rc.getStats().getTotalExports() + ",F:"
							+ rc.getStats().getErrorCounts() + ",C:" + rc.getStats().getErrorExports() + ").");
				}
			} catch (BiffException e) {
				EJBExceptionLP exLP = new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, e);
				messages.add(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, exLP));
				stats.incrementErrorCounts();
			} catch (IOException e) {
				EJBExceptionLP exLP = new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, e);
				messages.add(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, exLP));
				stats.incrementErrorCounts();
			}
		}

		return result;
	}

	private List<IForecastImportFile<?>> rearrangeFiles(List<IForecastImportFile<?>> files) {
		Collections.sort(files, new Comparator<IForecastImportFile<?>>() {
			@Override
			public int compare(IForecastImportFile<?> o1, IForecastImportFile<?> o2) {
				if (o1.getFiletype().equals(o2.getFiletype())) {
					return o1.getFilename().compareTo(o2.getFilename());
				} else {
					if (ForecastImportFileType.CallOff.equals(o1.getFiletype())) {
						return -1;
					}
					return 1;
				}
			}
		});
		return files;
	}

	private interface IImporter {
		CallOffXlsImporterResult doVerify(TheClientDto theClientDto);

		CallOffXlsImporterResult doImport(TheClientDto theClientDto);
	}

	private class LinienabrufImporter implements IImporter {
		private LinienabrufMultipleTxtImporter importer;
		private ForecastImportLinienabruf abruf;

		public LinienabrufImporter(ForecastImportLinienabruf abruf, Integer fclieferadresseId,
				TheClientDto theClientDto) {
			this.abruf = abruf;
			ILinienabrufImporterBeanService beanService = new LinienabrufImporterBeanService(theClientDto,
					getForecastFac(), getArtikelFac());
			importer = new LinienabrufMultipleTxtImporter(beanService, fclieferadresseId, abruf.getCalloffDate());
		}

		@Override
		public CallOffXlsImporterResult doVerify(TheClientDto theClientDto) {
			return importer.checkContent(abruf.getContent());
		}

		@Override
		public CallOffXlsImporterResult doImport(TheClientDto theClientDto) {
			return importer.importContent(abruf.getContent());
		}
	}

	private class CallOffDailyImporter implements IImporter {
		private CallOffXLSImporter importer;
		private Integer fclieferadresseId;
		private ForecastImportCallOff calloff;
		private Integer startRow = 12;

		public CallOffDailyImporter(ICallOffXlsImporterBeanService beanService, ForecastImportCallOff calloff,
				Integer fclieferadresseId, TheClientDto theClientDto) {
			this.calloff = calloff;
			this.fclieferadresseId = fclieferadresseId;
			// ICallOffXlsImporterBeanService beanService = new
			// CallOffXlsImporterBeanService(
			// theClientDto, getForecastFac(), getArtikelFac());

			importer = new CallOffXLSImporter(beanService, new CallOffDailyXLSTransformer());
		}

		@Override
		public CallOffXlsImporterResult doImport(TheClientDto theClientDto) {
			return importer.importXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}

		@Override
		public CallOffXlsImporterResult doVerify(TheClientDto theClientDto) {
			return importer.checkXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}
	}

	private class CallOffWeeklyImporter implements IImporter {
		private CallOffXLSImporter importer;
		private Integer fclieferadresseId;
		private ForecastImportCallOff calloff;
		private Integer startRow = 11;

		public CallOffWeeklyImporter(ICallOffXlsImporterBeanService beanService, ForecastImportCallOff calloff,
				Integer fclieferadresseId, TheClientDto theClientDto) {
			this.calloff = calloff;
			this.fclieferadresseId = fclieferadresseId;
			// ICallOffXlsImporterBeanService beanService = new
			// CallOffXlsImporterBeanService(
			// theClientDto, getForecastFac(), getArtikelFac());

			importer = new CallOffXLSImporter(beanService, new CallOffWeeklyXLSTransformer());
		}

		@Override
		public CallOffXlsImporterResult doImport(TheClientDto theClientDto) {
			return importer.importXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}

		@Override
		public CallOffXlsImporterResult doVerify(TheClientDto theClientDto) {
			return importer.checkXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}
	}

	private class CallOffMonthlyImporter implements IImporter {
		private CallOffXLSImporter importer;
		private Integer fclieferadresseId;
		private ForecastImportCallOff calloff;
		private Integer startRow = 10;

		public CallOffMonthlyImporter(ICallOffXlsImporterBeanService beanService, ForecastImportCallOff calloff,
				Integer fclieferadresseId, TheClientDto theClientDto) {
			this.calloff = calloff;
			this.fclieferadresseId = fclieferadresseId;
			// ICallOffXlsImporterBeanService beanService = new
			// CallOffXlsImporterBeanService(
			// theClientDto, getForecastFac(), getArtikelFac());

			importer = new CallOffXLSImporterMonthly(beanService);
		}

		@Override
		public CallOffXlsImporterResult doImport(TheClientDto theClientDto) {
			return importer.importXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}

		@Override
		public CallOffXlsImporterResult doVerify(TheClientDto theClientDto) {
			return importer.checkXLSDaten(calloff.getContent(), startRow, fclieferadresseId);
		}
	}

	private IImporter createImporter(ICallOffXlsImporterBeanService beanService, IForecastImportFile<?> importFile,
			Integer fclieferadresseId, TheClientDto theClientDto) throws BiffException, IOException {
		if (ForecastImportFileType.Linienabruf.equals(importFile.getFiletype())) {
			ForecastImportLinienabruf abruf = (ForecastImportLinienabruf) importFile;
			return new LinienabrufImporter(abruf, fclieferadresseId, theClientDto);
		}

		if (ForecastImportFileType.CallOff.equals(importFile.getFiletype())) {
			return detectImporter(beanService, importFile, fclieferadresseId, theClientDto);
		}

		return null;
	}

	private IImporter detectImporter(ICallOffXlsImporterBeanService beanService, IForecastImportFile<?> importFile,
			Integer fclieferadresseId, TheClientDto theClientDto) throws BiffException, IOException {
		ForecastImportCallOff callOffFile = (ForecastImportCallOff) importFile;
		Sheet sheet = getFirstSheet(callOffFile.getContent());
		if (sheet.getRows() < 10) {
			myLogger.warn("Importfile '" + importFile.getFilename()
					+ "' sollte zumindest 10 Zeilen haben, hat aber nur '" + sheet.getRows() + ".");
			return null;
		}

		int row = findNotEmptyRow(9, sheet);
		if (row == -1) {
			myLogger.warn("Kann in Datei '" + importFile.getFilename() + "' ab Zeile 10 (von '" + sheet.getRows()
					+ "') keine Zeile finden in der die erste Spalte gefuellt ist");
			return null;
		}

		Cell[] cells = sheet.getRow(row);
		if (isMonthly(cells)) {
			return new CallOffMonthlyImporter(beanService, callOffFile, fclieferadresseId, theClientDto);
		}

		row = findNotEmptyRow(11, sheet);
		if (row == -1) {
			myLogger.warn("Kann in Datei '" + importFile.getFilename() + "' ab Zeile 12 (von '" + sheet.getRows()
					+ "') keine Zeile finden in der die erste Spalte gefuellt ist");
			return null;
		}

		cells = sheet.getRow(row);
		if (isWeekly(cells)) {
			return new CallOffWeeklyImporter(beanService, callOffFile, fclieferadresseId, theClientDto);
		}

		row = findNotEmptyRow(12, sheet);
		if (row == -1) {
			myLogger.warn("Kann in Datei '" + importFile.getFilename() + "' ab Zeile 13 (von '" + sheet.getRows()
					+ "') keine Zeile finden in der die erste Spalte gefuellt ist");
			return null;
		}
		cells = sheet.getRow(row);
		if (isDaily(cells)) {
			return new CallOffDailyImporter(beanService, callOffFile, fclieferadresseId, theClientDto);
		}

		myLogger.warn("Kann kein Datenformat in Datei '" + importFile.getFilename() + " erkennen.");
		return null;
	}

	/**
	 * Monatlicher Calloff wenn zumindest in Spalte D und E ein Datum steht
	 * 
	 * @param cells
	 * @return
	 */
	private boolean isMonthly(Cell[] cells) {
		if (cells.length < 5)
			return false;
		if (isDate(cells[3]) && isDate(cells[4]))
			return true;

		return false;
	}

	/**
	 * Woechentlicher Calloff wenn in Spalte H ein Datum und in I eine Zahl(Menge)
	 * 
	 * @param cells
	 * @return
	 */
	private boolean isWeekly(Cell[] cells) {
		if (cells.length < 9)
			return false;
		if (isNumber(cells[8]) && isCalendarWeek(cells[7]))
			return true;

		return false;
	}

	/**
	 * Taeglicher Calloff wenn in Spalte I ein Datum und in Spalte J eine
	 * Zahl(Menge)
	 * 
	 * @param cells
	 * @return
	 */
	private boolean isDaily(Cell[] cells) {
		if (cells.length < 10)
			return false;
		if (isNumber(cells[9]) && isDate(cells[8]))
			return true;

		return false;
	}

	private boolean isNumber(Cell cell) {
		CellType ct = cell.getType();
		if (CellType.NUMBER.equals(ct) || CellType.NUMBER_FORMULA.equals(ct))
			return true;

		String s = cell.getContents();
		try {
			new BigDecimal(s);
			return true;
		} catch (NumberFormatException e) {
			myLogger.info("Kann '" + s + "' nicht in eine Zahl wandeln");
		}

		return false;
	}

	private boolean isDate(Cell cell) {
		CellType ct = cell.getType();
		if (CellType.DATE.equals(ct) || CellType.DATE_FORMULA.equals(ct))
			return true;
		String s = cell.getContents();
		if (s.startsWith("'")) {
			s = s.substring(1);
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			sdf.parse(s);
			return true;
		} catch (ParseException e) {
			myLogger.info("Kann '" + s + "' nicht in ein Datum wandeln");
		}

		return false;
	}

	private boolean isCalendarWeek(Cell cell) {
		String s = cell.getContents().trim();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ww/yyyy");
			sdf.parse(s);
			return true;
		} catch (ParseException e) {
			myLogger.warn("Kann '" + s + "' nicht in ein Datum mit Kalenderwoche wandeln");
		}

		return false;
	}

	private int findNotEmptyRow(int startRow, Sheet sheet) {
		int maxRows = sheet.getRows();
		do {
			Cell[] cells = sheet.getRow(startRow);
			if (cells.length < 1 || cells[0].getContents() == null) {
				++startRow;
				continue;
			}
			return startRow;
		} while (startRow < maxRows);

		return -1;
	}

	private Sheet getFirstSheet(byte[] xlsDatei) throws BiffException, IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		Workbook workbook = Workbook.getWorkbook(is, ws);
		return workbook.getSheet(0);
	}

}
