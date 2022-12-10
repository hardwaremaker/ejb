package com.lp.server.partner.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.hibernate.Session;

import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.partner.ejb.Branche;
import com.lp.server.partner.ejb.Partnerklasse;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerImportDto;
import com.lp.server.partner.service.PartnerimportFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.ejb.Spediteur;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.fastlanereader.generated.FLRLand;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class PartnerimportFacBean extends Facade implements PartnerimportFac {

	@PersistenceContext
	private EntityManager em;

	public static String XLS_IMPORT_SPALTE_ANREDE = "Anrede";
	public static String XLS_IMPORT_SPALTE_TITEL = "Titel";
	public static String XLS_IMPORT_SPALTE_NAME1 = "Name1";
	public static String XLS_IMPORT_SPALTE_NAME2 = "Name2";
	public static String XLS_IMPORT_SPALTE_NAME3 = "Name3";
	public static String XLS_IMPORT_SPALTE_STRASSE = "Strasse";
	public static String XLS_IMPORT_SPALTE_LAND = "Land";
	public static String XLS_IMPORT_SPALTE_POSTLEITZAHL = "Postleitzahl";
	public static String XLS_IMPORT_SPALTE_ORT = "Ort";
	public static String XLS_IMPORT_SPALTE_TELEFON = "Telefon";
	public static String XLS_IMPORT_SPALTE_FAX = "Fax";
	public static String XLS_IMPORT_SPALTE_KOMMUNIKATIONSSPRACHE = "Kommunikationssprache";
	public static String XLS_IMPORT_SPALTE_EMAIL = "EMail";
	public static String XLS_IMPORT_SPALTE_HOMEPAGE = "Homepage";
	public static String XLS_IMPORT_SPALTE_BEMERKUNG = "Bemerkung";
	public static String XLS_IMPORT_SPALTE_SELEKTION = "Selektion";
	public static String XLS_IMPORT_SPALTE_ANSP_FUNKTION = "Ansp-Funktion";
	public static String XLS_IMPORT_SPALTE_ANSP_ANREDE = "Ansp-Anrede";
	public static String XLS_IMPORT_SPALTE_ANSP_TITEL = "Ansp-Titel";
	public static String XLS_IMPORT_SPALTE_ANSP_VORNAME = "Ansp-Vorname";
	public static String XLS_IMPORT_SPALTE_ANSP_NACHNAME = "Ansp-Nachname";
	public static String XLS_IMPORT_SPALTE_ANSP_ABTEILUNG = "Ansp-Abteilung";
	public static String XLS_IMPORT_SPALTE_ANSP_TELEFON = "Ansp-TelefonDw";
	public static String XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL = "Ansp-BDurchwahl";
	public static String XLS_IMPORT_SPALTE_ANSP_FAXDW = "Ansp-FaxDw";
	public static String XLS_IMPORT_SPALTE_ANSP_EMAIL = "Ansp-EMail";
	public static String XLS_IMPORT_SPALTE_ANSP_MOBIL = "Ansp-Mobil";
	public static String XLS_IMPORT_SPALTE_ANSP_GUELTIGAB = "Ansp-Gueltigab";
	public static String XLS_IMPORT_SPALTE_ANSP_BEMERKUNG = "Ansp-Bemerkung";
	public static String XLS_IMPORT_SPALTE_ANSP_DIREKTFAX = "Ansp-Direktfax";
	public static String XLS_IMPORT_SPALTE_PARTNERKLASSE = "Partnerklasse";
	public static String XLS_IMPORT_SPALTE_BRANCHE = "Branche";
	public static String XLS_IMPORT_SPALTE_ILN = "ILN";
	public static String XLS_IMPORT_SPALTE_FILIALNUMMER = "Filialnummer";
	public static String XLS_IMPORT_SPALTE_UID = "Uid";
	public static String XLS_IMPORT_SPALTE_GERICHTSSTAND = "Gerichtsstand";
	public static String XLS_IMPORT_SPALTE_FIIRMENBUCHNUMMER = "Firmenbuchnummer";
	public static String XLS_IMPORT_SPALTE_POSTFACH = "Postfach";
	public static String XLS_IMPORT_SPALTE_GEBURTSDATUMANSPRECHPARTNER = "Geburtsdatumansprechpartner";
	public static String XLS_IMPORT_SPALTE_GMT_ZEITVERSATZ = "GMT-Zeitversatz";
	public static String XLS_IMPORT_SPALTE_KONTONUMMER = "Kontonummer";

	public static String XLS_IMPORT_SPALTE_POSTFACH_LAND = "PostfachLand";
	public static String XLS_IMPORT_SPALTE_POSTFACH_POSTLEITZAHL = "PostfachPostleitzahl";
	public static String XLS_IMPORT_SPALTE_POSTFACH_ORT = "PostfachOrt";
	public static String XLS_IMPORT_SPALTE_KURZBEZEICHNUNG = "Kurzbezeichnung";

	public static String XLS_IMPORT_SPALTE_SPEDITEUR = "Spediteur";
	public static String XLS_IMPORT_SPALTE_LIEFERART = "Lieferart";
	public static String XLS_IMPORT_SPALTE_ZAHLUNGSZIEL = "Zahlungsziel";
	public static String XLS_IMPORT_SPALTE_KD_LIEFERANTENNUMMER = "KDLieferantennr";
	public static String XLS_IMPORT_SPALTE_KD_KUNDENNUMMER = "KDKundennummer";
	public static String XLS_IMPORT_SPALTE_LF_KUNDENNUMMER = "LFKundennummer";
	public static String XLS_IMPORT_SPALTE_KUPFERZAHL = "Kupferzahl";
	public static String XLS_IMPORT_SPALTE_NEWSLETTERGRUND = "Newslettergrund";
	public static String XLS_IMPORT_SPALTE_ANSP_NEWSLETTERGRUND = "Ansp-Newslettergrund";
	public static String XLS_IMPORT_SPALTE_EORI = "EORI";
	public static String XLS_IMPORT_SPALTE_WAEHRUNG = "Waehrung";
	public static String XLS_IMPORT_SPALTE_KURZBEZEICHNUNG_ALS_KENNUNG = "KurzbezeichnungAlsKennung";

	private String fehlerZeileXLSImport = "";

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportierePartnerXLS(byte[] xlsDatei, boolean bErzeugeKunde, boolean bErzeugeLieferant,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto) {

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		fehlerZeileXLSImport = "";

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
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
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			// Artikelnummer muss immer vorhanden sein
			if (!hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_NAME1)) {
				return "Es muss zumindest die Spalte 'Name1' vorhanden sein" + new String(CRLFAscii);
			} else {

				for (int j = 1; j < sheet.getRows(); j++) {
					Cell[] sZeile = sheet.getRow(j);

					int i = j + 1;

					System.out.println("Zeile " + i + " von " + sheet.getRows());

					Integer iSpaltePartnerName = hmVorhandeneSpalten.get(XLS_IMPORT_SPALTE_NAME1);
					if (sZeile != null && sZeile.length > iSpaltePartnerName) {
						String name1 = sZeile[iSpaltePartnerName].getContents();

						if (name1 == null || name1.length() == 0) {
							fehlerZeileXLSImport += "Die Spalte 'Name1' darf nicht leer sein. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_NAME1, PartnerFac.MAX_NAME, i);

						PartnerDto partnerDto = new PartnerDto();
						partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
						partnerDto.setBVersteckt(Helper.boolean2Short(false));
						partnerDto.setCName1nachnamefirmazeile1(name1);
						partnerDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG)) {
							String kbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_KURZBEZEICHNUNG, 40, i);
							if (kbez == null || kbez.length() == 0) {

								fehlerZeileXLSImport += "Die Spalte 'Kurzbezeichnung' darf nicht leer sein. Zeile " + i
										+ new String(CRLFAscii);
							}
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG_ALS_KENNUNG)
								&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG)) {
							return "Die Spalten 'KurzbezeichnungAlsKennung' und 'Kurzbezeichnung' duerfen nicht gleichzeitig vorhanden sein"
									+ new String(CRLFAscii);
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_NAME2)) {
							partnerDto.setCName2vornamefirmazeile2(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_NAME2, PartnerFac.MAX_NAME, i));
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_NAME3)) {
							partnerDto.setCName3vorname2abteilung(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_NAME3, PartnerFac.MAX_NAME, i));

						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KOMMUNIKATIONSSPRACHE)) {
							String locale = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_KOMMUNIKATIONSSPRACHE, 40, i);
							if (locale == null || locale.length() == 0) {
								fehlerZeileXLSImport += "Die Spalte 'Kommunikationssprache' darf nicht leer sein. Zeile "
										+ i + new String(CRLFAscii);
								continue;
							} else {

								try {
									getLocaleFac().localeFindByPrimaryKey(locale);
									partnerDto.setLocaleCNrKommunikation(locale);
								} catch (Exception e) {
									fehlerZeileXLSImport += "Die Kommunikationssprache '" + locale
											+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
									continue;
								}

							}
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_STRASSE)) {

							partnerDto.setCStrasse(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_STRASSE, PartnerFac.MAX_STRASSE, i));
						}

						Integer anspNewslettergrundIId = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_NEWSLETTERGRUND)) {
							String anspNewslettergrund = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_ANSP_NEWSLETTERGRUND, 80, i);

							if (anspNewslettergrund != null) {

								NewslettergrundDto newslettergrundDto = getPartnerServicesFac()
										.newslettergrundFindByCBez(anspNewslettergrund);

								if (newslettergrundDto == null) {
									fehlerZeileXLSImport += "Der Ansprechpartner-Newslettergrund '"
											+ anspNewslettergrund + "' ist nicht vorhanden. Zeile " + i
											+ new String(CRLFAscii);
								} else {
									anspNewslettergrundIId = newslettergrundDto.getIId();
								}

							}

						}

						String anspNachname = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_NACHNAME)) {
							anspNachname = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_NACHNAME,
									PartnerFac.MAX_NAME, i);
						}

						String anspVorname = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_VORNAME)) {
							anspVorname = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_VORNAME,
									PartnerFac.MAX_NAME, i);
						}

						Session session = FLRSessionFactory.getFactory().openSession();

						String queryString = "SELECT p FROM FLRPartner as p WHERE p.c_name1nachnamefirmazeile1='"
								+ name1.replaceAll("'", "''") + "'";

						if (partnerDto.getCName2vornamefirmazeile2() != null
								&& partnerDto.getCName2vornamefirmazeile2().length() > 0) {
							queryString += " AND p.c_name2vornamefirmazeile2 ='"
									+ partnerDto.getCName2vornamefirmazeile2().replaceAll("'", "''") + "'";
						}

						if (partnerDto.getCName3vorname2abteilung() != null
								&& partnerDto.getCName3vorname2abteilung().length() > 0) {
							queryString += " AND p.c_name3vorname2abteilung ='"
									+ partnerDto.getCName3vorname2abteilung().replaceAll("'", "''") + "'";
						}

						if (partnerDto.getCStrasse() != null && partnerDto.getCStrasse().length() > 0) {
							queryString += " AND p.c_strasse ='" + partnerDto.getCStrasse().replaceAll("'", "''") + "'";
						}

						// PJ21563

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG_ALS_KENNUNG)) {
							String kbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_KURZBEZEICHNUNG_ALS_KENNUNG, 40, i);
							if (kbez == null || kbez.length() == 0) {

								fehlerZeileXLSImport += "Die Spalte 'KurzbezeichnungAlsKennung' darf nicht leer sein. Zeile "
										+ i + new String(CRLFAscii);
							}

							queryString += " AND p.c_kbez ='" + kbez.replaceAll("'", "''") + "'";
							partnerDto.setCKbez(kbez);
						}

						org.hibernate.Query query = session.createQuery(queryString);
						List<?> resultList = query.list();
						Iterator<?> resultListIterator = resultList.iterator();

						AnsprechpartnerDto anspDto = null;

						if (resultList.size() > 0) {

							FLRPartner p = (FLRPartner) resultListIterator.next();

							partnerDto = getPartnerFac().partnerFindByPrimaryKey(p.getI_id(), theClientDto);

							if (anspNachname != null) {

								// Ansprechpartner
								java.util.Set ansprechpartner = p.getAnsprechpartner();
								if (ansprechpartner.size() > 0) {
									Iterator anspIt = ansprechpartner.iterator();
									while (anspIt.hasNext()) {
										FLRAnsprechpartner flrAnsprechpartner = (FLRAnsprechpartner) anspIt.next();

										if (anspNachname.equals(flrAnsprechpartner.getFlrpartneransprechpartner()
												.getC_name1nachnamefirmazeile1())) {

											if (anspVorname == null) {
												anspDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
														flrAnsprechpartner.getI_id(), theClientDto);

											} else if (anspVorname != null && anspVorname.equals(flrAnsprechpartner
													.getFlrpartneransprechpartner().getC_name2vornamefirmazeile2())) {
												anspDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
														flrAnsprechpartner.getI_id(), theClientDto);

											}

										}

									}

								}

							}

						}

						String anspFunktion = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_FUNKTION)) {
							anspFunktion = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_FUNKTION,
									100, i);
						}

						if (anspDto == null) {

							if (anspFunktion != null) {
								try {
									getAnsprechpartnerFac().ansprechpartnerfunktionFindByCnr(anspFunktion,
											theClientDto);

								} catch (Throwable e) {
									//

									fehlerZeileXLSImport += "Die Ansprechpartner-Funktion '" + anspFunktion
											+ "' ist nicht vorhanden. Zeile " + i + new String(CRLFAscii);
									continue;

								}
							} else {
								if (anspNachname != null) {

									fehlerZeileXLSImport += "Die Ansprechpartner-Funktion darf nicht leer sein. Zeile "
											+ i + new String(CRLFAscii);
									continue;
								}
							}
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_TITEL)) {

							String cTitel = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_TITEL,
									PartnerFac.MAX_TITEL, i);

							if (cTitel != null) {
								partnerDto.setCTitel(cTitel);
							}
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_FIIRMENBUCHNUMMER)) {
							partnerDto.setCFirmenbuchnr(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_FIIRMENBUCHNUMMER, PartnerFac.MAX_FIRMENBUCHNR, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_GERICHTSSTAND)) {
							partnerDto.setCGerichtsstand(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_GERICHTSSTAND, PartnerFac.MAX_GERICHTSSTAND, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH)) {
							partnerDto.setCPostfach(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_POSTFACH, PartnerFac.MAX_POSTFACH, i));
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ILN)) {
							partnerDto.setCIln(getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ILN,
									PartnerFac.MAX_ILN, i));
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_UID)) {
							partnerDto.setCUid(getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_UID,
									PartnerFac.MAX_UID, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_FILIALNUMMER)) {
							partnerDto.setCFilialnummer(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_FILIALNUMMER, PartnerFac.MAX_TITEL, i));
						}

						String kdLieferantennummer = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KD_LIEFERANTENNUMMER)) {
							kdLieferantennummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_KD_LIEFERANTENNUMMER, 20, i);
						}

						Integer iKDKundennummer = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KD_KUNDENNUMMER)) {
							Double dKundennummer = getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_KD_KUNDENNUMMER, i);

							if (dKundennummer != null) {
								iKDKundennummer = dKundennummer.intValue();
							}
						}

						String waehrung = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_WAEHRUNG)) {
							waehrung = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_WAEHRUNG, 20, i);
							if (waehrung != null) {

								WaehrungDto whgDto = getLocaleFac().waehrungFindByPrimaryKeyWithNull(waehrung);
								if (whgDto == null) {
									fehlerZeileXLSImport += "Die Waehrung '" + waehrung
											+ "' ist nicht vorhanden. Zeile " + i + new String(CRLFAscii);
									continue;
								}
							}

						}

						Double dKupferzahl = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KUPFERZAHL)) {
							dKupferzahl = getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_KUPFERZAHL, i);

						}

						String lfKundennummer = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LF_KUNDENNUMMER)) {
							lfKundennummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_LF_KUNDENNUMMER, 40, i);
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_GMT_ZEITVERSATZ)) {
							partnerDto.setFGmtversatz(
									getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_GMT_ZEITVERSATZ,

											i));
						}

						// EORI
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_EORI)) {
							partnerDto.setCEori(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_EORI, 25, i));
						}

						// Partnerklasse
						String partnerklasse = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_PARTNERKLASSE)) {
							partnerklasse = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_PARTNERKLASSE, 15, i);
						}

						// Branche
						String sBranche = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BRANCHE)) {
							sBranche = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BRANCHE, 120, i);

						}

						// Zahlungsziel
						String sZahlungsziel = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ZAHLUNGSZIEL)) {
							sZahlungsziel = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ZAHLUNGSZIEL,
									MandantFac.MAX_ZAHLUNGSZIEL_C_BEZ, i);

						}
						// Spediteur
						String sSpediteur = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_SPEDITEUR)) {
							sSpediteur = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_SPEDITEUR,
									MandantFac.MAX_SPEDITEUR_C_NAMEDESSPEDITEURS, i);

						}
						// Lieferart
						String sLieferart = null;
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LIEFERART)) {
							sLieferart = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LIEFERART,
									MandantFac.MAX_LIEFERART_C_NR, i);

						}

						// Anrede
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANREDE)) {

							String anrede = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANREDE, 15,
									i);

							if (anrede != null) {
								if (anrede.equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {
									partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_HERR);
								} else if (anrede.equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {
									partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FRAU);

								} else if (anrede.equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {
									partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FIRMA);
								}
							}
						}

						// Landplzort
						// LAND

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LAND)
								&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTLEITZAHL)
								&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ORT)) {

							String land = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LAND, 15, i);

							String sPlz = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_POSTLEITZAHL,
									15, i);

							String sOrt = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ORT, 50, i);

						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_LAND)
								&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_POSTLEITZAHL)
								&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_ORT)) {

							String land = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_POSTFACH_LAND,
									15, i);

							String sPlz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_POSTFACH_POSTLEITZAHL, 15, i);

							String sOrt = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_POSTFACH_ORT,
									50, i);

						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_TELEFON)) {
							partnerDto.setCTelefon(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_TELEFON, 80, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_FAX)) {
							partnerDto.setCFax(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_FAX, 80, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_EMAIL)) {
							partnerDto.setCEmail(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_EMAIL, 80, i));
						}
						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HOMEPAGE)) {
							partnerDto.setCHomepage(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HOMEPAGE, 80, i));
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BEMERKUNG)) {
							partnerDto.setXBemerkung(
									getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BEMERKUNG, 3000, i));
						}

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_NEWSLETTERGRUND)) {
							String newslettergrund = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_NEWSLETTERGRUND, 80, i);

							if (newslettergrund != null && newslettergrund.length() > 0) {

								NewslettergrundDto newslettergrundDto = getPartnerServicesFac()
										.newslettergrundFindByCBez(newslettergrund);

								if (newslettergrundDto == null) {
									fehlerZeileXLSImport += "Der Newslettergrund '" + newslettergrund
											+ "' ist nicht vorhanden. Zeile " + i + new String(CRLFAscii);

								} else {
									partnerDto.setNewslettergrundIId(newslettergrundDto.getIId());
								}

							}

						}

						// Nun noch alle Spaleten auf laenge und Typ pruefen

						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_SELEKTION, 80, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_VORNAME,
								PartnerFac.MAX_NAME, i);

						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_TITEL, PartnerFac.MAX_TITEL,
								i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_ANREDE, 15, i);

						getDateAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_GEBURTSDATUMANSPRECHPARTNER, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_BEMERKUNG, 3000, i);
						getDateAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_GUELTIGAB, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_FAXDW, 80, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_TELEFON, 80, i);
						getShortAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_MOBIL, 80, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ANSP_DIREKTFAX, 80, i);

						if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KONTONUMMER)) {
							getIntegerAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_KONTONUMMER, i);
						}

						if (bImportierenWennKeinFehler) {

							if (partnerklasse != null) {
								partnerDto.setPartnerklasseIId(
										partnerklasseSuchenUndAnlegen(theClientDto, partnerklasse));
							}

							// Branche

							if (sBranche != null) {
								partnerDto.setBrancheIId(brancheSuchenUndAnlegen(theClientDto, sBranche));

							}

							if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LAND)
									&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTLEITZAHL)
									&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ORT)) {

								String land = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LAND, 15,
										i);

								String sPlz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_POSTLEITZAHL, 15, i);

								String sOrt = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ORT, 50,
										i);

								if (land != null && land.length() > 0 && sPlz != null && sPlz.length() > 0
										&& sOrt != null && sOrt.length() > 0) {

									Integer landPlzOrtIId = landplzortSuchenUndAnlegen(theClientDto, land, sPlz, sOrt);
									if (landPlzOrtIId != null) {
										partnerDto.setLandplzortIId(landPlzOrtIId);
									}
								}
							}

							if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_LAND)
									&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_POSTLEITZAHL)
									&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POSTFACH_ORT)) {

								String land = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_POSTFACH_LAND, 15, i);

								String sPlz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_POSTFACH_POSTLEITZAHL, 15, i);

								String sOrt = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_POSTFACH_ORT, 50, i);

								if (land != null && land.length() > 0 && sPlz != null && sPlz.length() > 0
										&& sOrt != null && sOrt.length() > 0) {
									Integer landPlzOrtIId = landplzortSuchenUndAnlegen(theClientDto, land, sPlz, sOrt);
									if (landPlzOrtIId != null) {
										partnerDto.setLandplzortIIdPostfach(landPlzOrtIId);
									}
								}
							}

							if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG)) {

								partnerDto.setCKbez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_KURZBEZEICHNUNG, 40, i));

							} else {
								if (partnerDto.getIId() == null) {

									if (!hmVorhandeneSpalten
											.containsKey(XLS_IMPORT_SPALTE_KURZBEZEICHNUNG_ALS_KENNUNG)) {

										try {
											String sN1 = partnerDto.getCName1nachnamefirmazeile1() + " ";

											int iE = sN1.indexOf(" ");
											if (iE > PartnerFac.MAX_KBEZ / 2) {
												iE = PartnerFac.MAX_KBEZ / 2;
											}
											partnerDto.setCKbez(sN1.substring(0, iE));
										} catch (Exception e1) {
											partnerDto.setCKbez("KBEZ");
										}
									}
								}
							}

							if (partnerDto.getIId() == null) {
								Integer partnerIId = getPartnerFac().createPartner(partnerDto, theClientDto);
								partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
							} else {
								getPartnerFac().updatePartner(partnerDto, theClientDto);
							}

							// Selektion

							if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_SELEKTION)) {

								String selektion = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_IMPORT_SPALTE_SELEKTION, 80, i);

								if (selektion != null) {

									try {
										SelektionDto selektionDto = getPartnerServicesFac()
												.selektionFindByCNrMandantCNr(selektion, theClientDto);
										if (selektionDto != null) {

											PASelektionDto paselDto = new PASelektionDto();
											paselDto.setPartnerIId(partnerDto.getIId());
											paselDto.setSelektionIId(selektionDto.getIId());
											getPartnerFac().createPASelektion(paselDto, theClientDto);
										}
									} catch (Exception e) {
										// Dann ist sie bereits vorhanden
									}
								}
							}

							if (anspDto == null && anspNachname != null && anspFunktion != null) {

								// Partner vorher anlegen
								PartnerDto partnerDtoAnsprechpartner = new PartnerDto();
								partnerDtoAnsprechpartner.setCName1nachnamefirmazeile1(anspNachname);

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_VORNAME)) {

									String name2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_VORNAME, PartnerFac.MAX_NAME, i);
									if (name2 != null) {
										partnerDtoAnsprechpartner.setCName2vornamefirmazeile2(name2);
									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_TITEL)) {

									String titel = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_TITEL, PartnerFac.MAX_TITEL, i);
									if (titel != null) {
										partnerDtoAnsprechpartner.setCTitel(titel);
									}
								}

								partnerDtoAnsprechpartner.setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
								partnerDtoAnsprechpartner.setBVersteckt(com.lp.util.Helper.boolean2Short(false));
								partnerDtoAnsprechpartner.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

								String kbez = anspNachname;
								if (kbez.length() > 14) {
									kbez = anspNachname.substring(0, 13);
								}

								partnerDtoAnsprechpartner.setCKbez(kbez);

								// Anrede

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_ANREDE)) {

									String anrede = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_ANREDE, 15, i);
									if (anrede != null) {

										if (anrede.equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {
											partnerDtoAnsprechpartner.setAnredeCNr(PartnerFac.PARTNER_ANREDE_HERR);
										} else if (anrede.equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {
											partnerDtoAnsprechpartner.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FRAU);

										} else if (anrede.equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {
											partnerDtoAnsprechpartner.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FIRMA);
										}

									} else {
										partnerDtoAnsprechpartner.setAnredeCNr(null);
									}
								}
								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_GEBURTSDATUMANSPRECHPARTNER)) {

									java.util.Date d = getDateAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_GEBURTSDATUMANSPRECHPARTNER, i);

									if (d != null) {
										partnerDtoAnsprechpartner
												.setDGeburtsdatumansprechpartner(new java.sql.Date(d.getTime()));
									} else {
										partnerDtoAnsprechpartner.setDGeburtsdatumansprechpartner(null);
									}

								}

								anspDto = new AnsprechpartnerDto();
								try {
									anspDto.setPartnerIIdAnsprechpartner(
											getPartnerFac().createPartner(partnerDtoAnsprechpartner, theClientDto));
								} catch (RemoteException e1) {
									throwEJBExceptionLPRespectOld(e1);
								}
								anspDto.setPartnerIId(partnerDto.getIId());

								anspDto.setNewslettergrundIId(anspNewslettergrundIId);
								anspDto.setBVersteckt(com.lp.util.Helper.boolean2Short(false));

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_BEMERKUNG)) {

									String bem = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_BEMERKUNG, 3000, i);

									anspDto.setXBemerkung(bem);

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_GUELTIGAB)) {
									java.util.Date d = getDateAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_GUELTIGAB, i);
									if (d != null) {

										anspDto.setDGueltigab(new java.sql.Date(d.getTime()));

									} else {
										anspDto.setDGueltigab(new java.sql.Date(System.currentTimeMillis()));
									}
								} else {
									anspDto.setDGueltigab(new java.sql.Date(System.currentTimeMillis()));
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_FAXDW)) {

									String fax = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_FAXDW, 80, i);

									anspDto.setCFax(fax);

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_TELEFON)) {

									String tel = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_TELEFON, 80, i);

									anspDto.setCTelefon(tel);

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_ABTEILUNG)) {

									String abteilung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_ABTEILUNG, 40, i);

									anspDto.setCAbteilung(abteilung);
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL)) {

									Short tel = getShortAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL, i);
									if (tel != null) {
										anspDto.setBDurchwahl(tel);
									}

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_MOBIL)) {

									String handy = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_MOBIL, 80, i);

									anspDto.setCHandy(handy);

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_EMAIL)) {

									String email = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_EMAIL, 80, i);
									if (email != null) {
										anspDto.setCEmail(email);
									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_DIREKTFAX)) {

									String direktfax = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_DIREKTFAX, 80, i);

									anspDto.setCDirektfax(direktfax);

								}

								// Ansprpechpartnerfunktion

								AnsprechpartnerfunktionDto dto = null;
								try {
									dto = getAnsprechpartnerFac().ansprechpartnerfunktionFindByCnr(anspFunktion,
											theClientDto);

									anspDto.setAnsprechpartnerfunktionIId(dto.getIId());
									anspDto.setISort(
											getAnsprechpartnerFac().getMaxISort(partnerDto.getIId()).intValue() + 1);

								} catch (Throwable e) {
									e.printStackTrace();
									// auslassen
								}

								getAnsprechpartnerFac().createAnsprechpartner(anspDto, theClientDto);

							} else if (anspDto != null) {

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_BEMERKUNG)) {

									String bem = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_BEMERKUNG, 3000, i);
									if (bem != null) {
										anspDto.setXBemerkung(bem);
									}

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_ABTEILUNG)) {

									String abteilung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_ABTEILUNG, 40, i);
									if (abteilung != null) {
										anspDto.setCAbteilung(abteilung);
									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_GUELTIGAB)) {
									java.util.Date d = getDateAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_GUELTIGAB, i);
									if (d != null) {

										anspDto.setDGueltigab(new java.sql.Date(d.getTime()));

									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_FAXDW)) {

									String fax = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_FAXDW, 80, i);
									if (fax != null) {
										anspDto.setCFax(fax);
									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_TELEFON)) {

									String tel = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_TELEFON, 80, i);
									if (tel != null) {
										anspDto.setCTelefon(tel);
									}

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL)) {

									Short tel = getShortAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_BDURCHWAHL, i);
									if (tel != null) {
										anspDto.setBDurchwahl(tel);
									}

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_MOBIL)) {

									String handy = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_MOBIL, 80, i);
									if (handy != null) {
										anspDto.setCHandy(handy);
									}

								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_EMAIL)) {

									String email = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_EMAIL, 80, i);
									if (email != null) {
										anspDto.setCEmail(email);
									}
								}

								if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ANSP_DIREKTFAX)) {

									String direktfax = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_IMPORT_SPALTE_ANSP_DIREKTFAX, 80, i);
									if (direktfax != null) {
										anspDto.setCDirektfax(direktfax);
									}

								}

								anspDto.setNewslettergrundIId(anspNewslettergrundIId);
								// Ansprpechpartnerfunktion

								if (anspFunktion != null) {
									AnsprechpartnerfunktionDto dto = null;
									try {
										dto = getAnsprechpartnerFac().ansprechpartnerfunktionFindByCnr(anspFunktion,
												theClientDto);

										anspDto.setAnsprechpartnerfunktionIId(dto.getIId());

									} catch (Throwable e) {
										e.printStackTrace();
										// auslassen
									}
								}

								getAnsprechpartnerFac().updateAnsprechpartner(anspDto, theClientDto);
							}
							// Kunde anlegen

							if (partnerDto.getIId() != null && bErzeugeKunde == true) {
								try {

									KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
											partnerDto.getIId(), theClientDto.getMandant(), theClientDto);

									if (kundeDto == null) {
										kundeDto = new KundeDto();

										// SP5455
										Integer mwstsatzbezIId = mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz();
										if (partnerDto.getLandplzortIId() != null) {

											Integer mwstsatzbezIIdTemp = getPartnerFac()
													.getDefaultMWSTSatzIIdAnhandLand(
															getSystemFac().landplzortFindByPrimaryKey(
																	partnerDto.getLandplzortIId()).getLandDto(),
															theClientDto);
											if (mwstsatzbezIIdTemp != null) {
												mwstsatzbezIId = mwstsatzbezIIdTemp;
											}

										}
										kundeDto.setMwstsatzbezIId(mwstsatzbezIId);

										kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(
												mandantDto.getVkpfArtikelpreislisteIId());
										kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
										kundeDto.setbIstinteressent(new Short((short) 0));

										kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());
										if (iKDKundennummer != null) {
											kundeDto.setIKundennummer(iKDKundennummer);
										}

										if (dKupferzahl != null) {
											kundeDto.setNKupferzahl(new BigDecimal(dKupferzahl));
										}

										if (kdLieferantennummer != null) {
											kundeDto.setCLieferantennr(kdLieferantennummer);
										}

										kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
										// Vorbelegungen werden vom Mandanten
										// geholt

										kundeDto.setMandantCNr(mandantDto.getCNr());
										kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
										kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
										kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());

										if (waehrung != null) {
											kundeDto.setCWaehrung(waehrung);
										} else {
											kundeDto.setCWaehrung(mandantDto.getWaehrungCNr());
										}

										kundeDto.setbIstinteressent(Helper.boolean2Short(false));
										kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
										kundeDto.setBDistributor(Helper.boolean2Short(false));
										kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
										kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
										kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
										kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
										kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
										kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
										kundeDto.setBReversecharge(Helper.boolean2Short(false));

										if (sZahlungsziel != null && sZahlungsziel.length() > 0) {
											kundeDto.setZahlungszielIId(
													zahlungszielSuchenUndAnlegen(theClientDto, sZahlungsziel));
										}

										if (sLieferart != null && sLieferart.length() > 0) {
											kundeDto.setLieferartIId(
													lieferartSuchenUndAnlegen(theClientDto, sLieferart));
										}
										if (sSpediteur != null && sSpediteur.length() > 0) {
											kundeDto.setSpediteurIId(
													spediteurSuchenUndAnlegen(theClientDto, sSpediteur));
										}

										// damit die Debitorenkto. nicht
										// anschlaegt.
										kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

										kundeDto.setPartnerDto(partnerDto);

										Integer kundeIId = getKundeFac().createKunde(kundeDto, theClientDto);

										if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KONTONUMMER)) {

											// String konto =
											// getStringAusXLS(sZeile,
											// hmVorhandeneSpalten,
											// XLS_IMPORT_SPALTE_KONTONUMMER,
											// 15, i);

											Integer intKonto = getIntegerAusXLS(sZeile, hmVorhandeneSpalten,
													XLS_IMPORT_SPALTE_KONTONUMMER, i);
											if (intKonto != null) {
												getPartnerFac().kontoFuerPartnerImportAnlegen(intKonto.toString(),
														FinanzServiceFac.KONTOTYP_DEBITOR, kundeIId, null,
														theClientDto);
											}
										}
									} else {

										kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeDto.getIId(), theClientDto);

										// PJ19302
										if (iKDKundennummer != null) {
											kundeDto.setIKundennummer(iKDKundennummer);
										}

										if (dKupferzahl != null) {
											kundeDto.setNKupferzahl(new BigDecimal(dKupferzahl));
										}
										if (waehrung != null) {
											kundeDto.setCWaehrung(waehrung);
										}

										kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

										getKundeFac().updateKunde(kundeDto, theClientDto);

									}

								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}
							}

							// Lieferant anlegen

							if (partnerDto.getIId() != null && bErzeugeLieferant == true) {
								try {

									LieferantDto lieferantDto = getLieferantFac()
											.lieferantFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(),
													theClientDto.getMandant(), theClientDto);

									if (lieferantDto == null) {
										lieferantDto = new LieferantDto();
										lieferantDto.setMwstsatzbezIId(
												mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());

										lieferantDto.setIIdKostenstelle((mandantDto.getIIdKostenstelle()));
										lieferantDto.setBMoeglicherLieferant(new Short((short) 0));

										lieferantDto.setBVersteckt(Helper.boolean2Short(false));
										// Vorbelegungen werden vom Mandanten
										// geholt

										lieferantDto.setMandantCNr(mandantDto.getCNr());
										lieferantDto.setLieferartIId(mandantDto.getLieferartIIdLF());
										lieferantDto.setIdSpediteur(mandantDto.getSpediteurIIdLF());
										lieferantDto.setZahlungszielIId(mandantDto.getZahlungszielIIdLF());
										if (waehrung != null) {
											lieferantDto.setWaehrungCNr(waehrung);
										} else {
											lieferantDto.setWaehrungCNr(mandantDto.getWaehrungCNr());
										}

										if (sZahlungsziel != null && sZahlungsziel.length() > 0) {
											lieferantDto.setZahlungszielIId(
													zahlungszielSuchenUndAnlegen(theClientDto, sZahlungsziel));
										}

										if (sLieferart != null && sLieferart.length() > 0) {
											lieferantDto.setLieferartIId(
													lieferartSuchenUndAnlegen(theClientDto, sLieferart));
										}
										if (sSpediteur != null && sSpediteur.length() > 0) {
											lieferantDto.setIdSpediteur(
													spediteurSuchenUndAnlegen(theClientDto, sSpediteur));
										}

										if (lfKundennummer != null) {
											lieferantDto.setCKundennr(lfKundennummer);
										}

										if (dKupferzahl != null) {
											lieferantDto.setNKupferzahl(new BigDecimal(dKupferzahl));
										}

										lieferantDto.setBIgErwerb(Helper.boolean2Short(false));

										ReversechargeartDto rcartOhneDto = getFinanzServiceFac()
												.reversechargeartFindOhne(theClientDto);
										lieferantDto.setReversechargeartId(rcartOhneDto.getIId());
										// lieferantDto.setBReversecharge(Helper
										// .boolean2Short(false));

										lieferantDto.setPartnerDto(partnerDto);

										Integer liferantIId = getLieferantFac().createLieferant(lieferantDto,
												theClientDto);

										if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KONTONUMMER)) {

											Integer intKonto = getIntegerAusXLS(sZeile, hmVorhandeneSpalten,
													XLS_IMPORT_SPALTE_KONTONUMMER, i);

											if (intKonto != null) {
												getPartnerFac().kontoFuerPartnerImportAnlegen(intKonto.toString(),
														FinanzServiceFac.KONTOTYP_KREDITOR, null, liferantIId,
														theClientDto);
											}
										}

									} else {

										lieferantDto = getLieferantFac()
												.lieferantFindByPrimaryKey(lieferantDto.getIId(), theClientDto);

										if (dKupferzahl != null) {
											lieferantDto.setNKupferzahl(new BigDecimal(dKupferzahl));
										}

										if (waehrung != null) {
											lieferantDto.setWaehrungCNr(waehrung);
										}

										getLieferantFac().updateLieferant(lieferantDto, theClientDto);

									}

								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}
							}
						}
					}
				}
				if (fehlerZeileXLSImport != null && fehlerZeileXLSImport.length() > 0) {
					return fehlerZeileXLSImport;
				}

			}

		} catch (

		BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeCSVImport(PartnerImportDto[] daten, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		for (int i = 2; i < daten.length + 2; i++) {

			String fehler = "";
			PartnerImportDto zeile = daten[i - 2];

			if (zeile.getAnrede().length() > 15) {
				fehler += "Feld Anrede zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getAnrede().length() > 0) {
				if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {

				} else if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {

				} else if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {

				} else {
					fehler += " Anrede '" + zeile.getAnrede() + "' nicht vorhanden, Zeile:" + i + "; ";
				}

			}

			if (zeile.getAnsprechpartnerNachname().length() == 0 && zeile.getAnsprechpartnerFunktion().length() == 0) {
				// Es gibt keinen Ansprechpartner

			} else {

				if (zeile.getAnsprechpartnerAnrede().length() > 15) {
					fehler += "Feld AnsprechpartnerAnrede zu Lang, Zeile:" + i + "; ";
				}

				if (zeile.getAnsprechpartnerAnrede().length() > 0) {
					if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {

					} else if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {

					} else if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {

					} else {
						fehler += " AnsprechpartnerAnrede '" + zeile.getAnsprechpartnerAnrede()
								+ "' nicht vorhanden, Zeile:" + i + "; ";
					}

				}

				if (zeile.getAnsprechpartnerBemerkung().length() > 300) {
					fehler += "Feld AnsprechpartnerBemerkung zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerEmail().length() > 80) {
					fehler += "Feld AnsprechpartnerEmail zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerFaxDW().length() > 80) {
					fehler += "Feld AnsprechpartnerFaxDW zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerDirektfax().length() > 80) {
					fehler += "Feld AnsprechpartnerDirektfax zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerFunktion().length() == 0) {
					fehler += "Feld AnsprechpartnerFunktion nicht bef\u00FCllt, Zeile:" + i + "; ";
				} else {
					try {
						getAnsprechpartnerFac().ansprechpartnerfunktionFindByCnr(zeile.getAnsprechpartnerFunktion(),
								theClientDto);
					} catch (Throwable e) {
						fehler += " AnsprechpartnerFunktion '" + zeile.getAnsprechpartnerFunktion()
								+ "' nicht vorhanden, Zeile:" + i + "; ";
					}

				}
				if (zeile.getAnsprechpartnerGueltigab() == null) {
					fehler += "Feld AnsprechpartnerGueltigab nicht bef\u00FCllt, Zeile:" + i + "; ";
				}

				if (zeile.getAnsprechpartnerMobil().length() > 80) {
					fehler += "Feld AnsprechpartnerMobil zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerNachname().length() > 40) {
					fehler += "Feld AnsprechpartnerNachname zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerTelefonDW().length() > 80) {
					fehler += "Feld AnsprechpartnerTelefonDW zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerTitel().length() > 80) {
					fehler += "Feld AnsprechpartnerTitel zu Lang, Zeile:" + i + "; ";
				}
				if (zeile.getAnsprechpartnerVorname().length() > 40) {
					fehler += "Feld AnsprechpartnerVorname zu Lang, Zeile:" + i + "; ";
				}

			}

			if (zeile.getBemerkung().length() > 3000) {
				fehler += "Feld Bemerkung zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getEmail().length() > 80) {
				fehler += "Feld Email zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getFax().length() > 80) {
				fehler += "Feld Fax zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getHomepage().length() > 80) {
				fehler += "Feld Homepage zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getLand().length() > 50) {
				fehler += "Feld Land zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getName1().length() > 40) {
				fehler += "Feld Name1 zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getName2().length() > 40) {
				fehler += "Feld Name2 zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getName3().length() > 40) {
				fehler += "Feld Name3 zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getOrt().length() > 50) {
				fehler += "Feld Ort zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getFirmenbuchnummer().length() > 50) {
				fehler += "Feld Firmenbuchnummer zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getGerichtsstand().length() > 40) {
				fehler += "Feld Gerichtsstand zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getPostfach().length() > 15) {
				fehler += "Feld Postfach zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getPlz().length() > 15) {
				fehler += "Feld Plz zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getSelektion().length() > 20) {
				fehler += "Feld Selektion zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getIln().length() > 15) {
				fehler += "Feld ILN zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getFilialnr().length() > 15) {
				fehler += "Feld Filialnr. zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getUid().length() > 20) {
				fehler += "Feld UIDNr zu Lang, Zeile:" + i + "; ";
			}

			if (zeile.getPartnerklasse().length() > 15) {
				fehler += "Feld Partnerklass zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getBranche().length() > 50) {
				fehler += "Feld Branche zu Lang, Zeile:" + i + "; ";
			}

			if (zeile.getSelektion().length() > 0) {

				try {
					SelektionDto selektionDto = getPartnerServicesFac()
							.selektionFindByCNrMandantCNr(zeile.getSelektion(), theClientDto);
					if (selektionDto == null) {
						fehler += "Selektion '" + zeile.getSelektion() + "' nicht vorhanden, Zeile:" + i + "; ";

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			if (zeile.getAnsprechpartnernewslettergrund().length() > 0) {
				NewslettergrundDto nDto = getPartnerServicesFac()
						.newslettergrundFindByCBez(zeile.getAnsprechpartnernewslettergrund());
				if (nDto == null) {
					fehler += "Ansp-Newslettergrund '" + zeile.getAnsprechpartnernewslettergrund()
							+ "' nicht vorhanden, Zeile:" + i + "; ";

				}

			}

			if (zeile.getNewslettergrund().length() > 0) {
				NewslettergrundDto nDto = getPartnerServicesFac().newslettergrundFindByCBez(zeile.getNewslettergrund());
				if (nDto == null) {
					fehler += "Newslettergrund '" + zeile.getNewslettergrund() + "' nicht vorhanden, Zeile:" + i + "; ";

				}

			}

			if (zeile.getStrasse().length() > 80) {
				fehler += "Feld Strasse zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getTelefon().length() > 80) {
				fehler += "Feld Telefon zu Lang, Zeile:" + i + "; ";
			}
			if (zeile.getTitel().length() > 80) {
				fehler += "Feld Titel zu Lang, Zeile:" + i + "; ";
			}

			if (!Helper.isStringEmpty(zeile.getGmtversatz())) {
				try {
					Double.parseDouble(zeile.getGmtversatz());
				} catch (NumberFormatException e) {
					fehler += "Feld Gmtversatz muss numerisch sein, Zeile:" + i + "; ";
				}
			}

			if (!Helper.isStringEmpty(zeile.getKontonummer()) && !Helper.istStringNumerisch(zeile.getKontonummer())) {
				fehler += "Feld Kontonummer muss ganzzahlig numerisch sein, Zeile:" + i + "; ";
			}

			// Feldlaengen
			// zeile

			if (fehler.length() > 0) {
				rueckgabe += fehler + new String(CRLFAscii);
			}

		}

		return rueckgabe;
	}

	public Integer spediteurSuchenUndAnlegen(TheClientDto theClientDto, String spediteur) throws RemoteException {
		try {

			Query query = em.createNamedQuery("SpediteurfindByMandantSpediteurname");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, spediteur);

			Spediteur bean = (Spediteur) query.getSingleResult();
			return bean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			SpediteurDto spediteurDto = new SpediteurDto();
			spediteurDto.setCNamedesspediteurs(spediteur);
			spediteurDto.setMandantCNr(theClientDto.getMandant());
			Integer iId = getMandantFac().createSpediteur(spediteurDto, theClientDto);
			return iId;

		}
	}

	public Integer lieferartSuchenUndAnlegen(TheClientDto theClientDto, String lieferart) throws RemoteException {
		try {

			Query query = em.createNamedQuery("LieferartfindbyCNrMandantCNr");
			query.setParameter(1, lieferart);
			query.setParameter(2, theClientDto.getMandant());

			Lieferart bean = (Lieferart) query.getSingleResult();
			return bean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			LieferartDto dto = new LieferartDto();
			dto.setCNr(lieferart);
			dto.setMandantCNr(theClientDto.getMandant());
			dto.setBFrachtkostenalserledigtverbuchen(Helper.boolean2Short(true));
			dto.setBVersteckt(Helper.boolean2Short(false));
			Integer iId = getLocaleFac().createLieferart(dto, theClientDto);
			return iId;

		}
	}

	public Integer zahlungszielSuchenUndAnlegen(TheClientDto theClientDto, String zahlungsziel) throws RemoteException {
		try {

			Query query = em.createNamedQuery("ZahlungszielfindByCBezMandantCNr");

			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, zahlungsziel);

			Zahlungsziel bean = (Zahlungsziel) query.getSingleResult();
			return bean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			ZahlungszielDto dto = new ZahlungszielDto();
			dto.setCBez(zahlungsziel);
			dto.setMandantCNr(theClientDto.getMandant());
			Integer iId = getMandantFac().createZahlungsziel(dto, theClientDto);
			return iId;

		}
	}

	public Integer partnerklasseSuchenUndAnlegen(TheClientDto theClientDto, String partnerklasse)
			throws RemoteException {

		try {

			Query query = em.createNamedQuery("PartnerklassefindByCNr");
			query.setParameter(1, partnerklasse);

			Partnerklasse pk = (Partnerklasse) query.getSingleResult();
			return pk.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			Integer partnerklasseIId = null;
			PartnerklasseDto klasseDto = new PartnerklasseDto();
			klasseDto.setCNr(partnerklasse);
			try {
				partnerklasseIId = getPartnerFac().createPartnerklasse(klasseDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			return partnerklasseIId;

		}
	}

	public Integer landplzortSuchenUndAnlegen(TheClientDto theClientDto, String land, String sPlz, String sOrt)
			throws RemoteException {

		if (land != null) {
			Session session1 = FLRSessionFactory.getFactory().openSession();

			String querystring1 = "SELECT l FROM FLRLand as l WHERE l.c_lkz='" + land + "'";

			org.hibernate.Query query1 = session1.createQuery(querystring1);
			List<?> results = query1.list();

			Integer landIId = null;

			if (results.size() > 0) {

				FLRLand flrLand = (FLRLand) results.iterator().next();
				// bereits vorhanden, dann ID holen
				landIId = flrLand.getI_id();
			} else {
				LandDto landDto = new LandDto();
				landDto.setCLkz(land);
				landDto.setCName(land);
				landDto.setILaengeuidnummer(10);
				landDto.setBSepa(Helper.boolean2Short(false));

				try {
					landIId = getSystemFac().createLand(landDto, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			session1.close();

			// ORT

			Query queryOrt = em.createNamedQuery("OrtfindByCName");
			queryOrt.setParameter(1, sOrt);

			results = queryOrt.getResultList();

			Integer ortIId = null;

			if (results.size() > 0) {

				Ort ort = (Ort) results.iterator().next();
				// bereits vorhanden, dann ID holen
				ortIId = ort.getIId();
			} else {
				OrtDto landDto = new OrtDto();
				landDto.setCName(sOrt);
				try {
					ortIId = getSystemFac().createOrt(landDto, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			// PLZ
			session1 = FLRSessionFactory.getFactory().openSession();

			querystring1 = "SELECT plz FROM FLRLandplzort as plz WHERE plz.c_plz='" + sPlz + "' AND plz.flrort.i_id="
					+ ortIId + " AND plz.flrland.i_id=" + landIId;

			query1 = session1.createQuery(querystring1);
			results = query1.list();

			Integer landplzortIId = null;

			if (results.size() > 0) {

				FLRLandplzort flrLandplzort = (FLRLandplzort) results.iterator().next();
				// bereits vorhanden, dann ID holen
				landplzortIId = flrLandplzort.getI_id();
			} else {
				LandplzortDto landplzortDto = new LandplzortDto();
				landplzortDto.setCPlz(sPlz);
				landplzortDto.setOrtIId(ortIId);
				landplzortDto.setIlandID(landIId);

				LandDto landDto = new LandDto();
				landDto.setCLkz(land);
				landDto.setCName(land);
				landDto.setILaengeuidnummer(10);
				landDto.setIID(landIId);
				landplzortDto.setLandDto(landDto);

				OrtDto ortDto = new OrtDto();
				ortDto.setCName(sOrt);
				ortDto.setIId(ortIId);
				landplzortDto.setOrtDto(ortDto);
				try {
					landplzortIId = getSystemFac().createLandplzort(landplzortDto, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			session1.close();

			return landplzortIId;
		} else {
			return null;
		}
	}

	public Integer brancheSuchenUndAnlegen(TheClientDto theClientDto, String branche) throws RemoteException {
		Integer brancheIId = null;
		try {
			Query query = em.createNamedQuery("BranchefindByCNr");
			query.setParameter(1, branche);
			brancheIId = ((Branche) query.getSingleResult()).getIId();

		} catch (NoResultException ex) {
			// Neu anlegen
			BrancheDto brancheDto = new BrancheDto();
			brancheDto.setCNr(branche);
			try {
				brancheIId = getPartnerServicesFac().createBranche(brancheDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		return brancheIId;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void importierePartner(PartnerImportDto[] daten, TheClientDto theClientDto, boolean bErzeugeKunde,
			boolean bErzeugeLieferant) {

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		for (int i = 0; i < daten.length; i++) {

			System.out.println(i + " von " + daten.length);

			PartnerImportDto zeile = daten[i];

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT p FROM FLRPartner as p WHERE p.c_name1nachnamefirmazeile1='"
					+ zeile.getName1().replaceAll("'", "''") + "'";

			if (zeile.getName2() != null && zeile.getName2().length() > 0) {
				queryString += " AND p.c_name2vornamefirmazeile2 ='" + zeile.getName2().replaceAll("'", "''") + "'";
			}

			if (zeile.getName3() != null && zeile.getName3().length() > 0) {
				queryString += " AND p.c_name3vorname2abteilung ='" + zeile.getName3().replaceAll("'", "''") + "'";
			}

			if (zeile.getStrasse() != null && zeile.getStrasse().length() > 0) {
				queryString += " AND p.c_strasse ='" + zeile.getStrasse().replaceAll("'", "''") + "'";
			}

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			boolean bIstNeu = false;
			boolean bAnsprechpartnerVorhanden = false;

			Integer partnerIIdFuerAnsprechpartner = null;

			if (resultList.size() > 0) {

				while (resultListIterator.hasNext()) {
					FLRPartner p = (FLRPartner) resultListIterator.next();
					partnerIIdFuerAnsprechpartner = p.getI_id();

					if (zeile.getAnsprechpartnerNachname().length() == 0
							&& zeile.getAnsprechpartnerFunktion().length() == 0) {
						// Es gibt keinen Ansprechpartner

					} else {

						if (zeile.getAnsprechpartnerNachname().length() > 0) {

							// Ansprechpartner
							java.util.Set ansprechpartner = p.getAnsprechpartner();
							if (ansprechpartner.size() > 0) {
								Iterator anspIt = ansprechpartner.iterator();
								while (anspIt.hasNext()) {
									FLRAnsprechpartner flrAnsprechpartner = (FLRAnsprechpartner) anspIt.next();

									if (zeile.getAnsprechpartnerNachname().equals(flrAnsprechpartner
											.getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1())) {
										bAnsprechpartnerVorhanden = true;

									}

								}

							}

						}
					}
				}
			} else {
				bIstNeu = true;
			}
			if (bIstNeu) {
				// Zuerst partner anlegen
				PartnerDto partnerDto = new PartnerDto();
				partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
				partnerDto.setBVersteckt(Helper.boolean2Short(false));
				partnerDto.setCName1nachnamefirmazeile1(zeile.getName1());
				partnerDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

				if (zeile.getName2().length() > 0) {
					partnerDto.setCName2vornamefirmazeile2(zeile.getName2());
				}
				if (zeile.getName3().length() > 0) {
					partnerDto.setCName3vorname2abteilung(zeile.getName3());
				}
				if (zeile.getTitel().length() > 0) {
					partnerDto.setCTitel(zeile.getTitel());
				}
				if (zeile.getStrasse().length() > 0) {
					partnerDto.setCStrasse(zeile.getStrasse());
				}

				if (zeile.getFirmenbuchnummer().length() > 0) {
					partnerDto.setCFirmenbuchnr(zeile.getFirmenbuchnummer());
				}
				if (zeile.getGerichtsstand().length() > 0) {
					partnerDto.setCGerichtsstand(zeile.getGerichtsstand());
				}
				if (zeile.getPostfach().length() > 0) {
					partnerDto.setCPostfach(zeile.getPostfach());
				}

				// Newslettergrund
				if (zeile.getNewslettergrund() != null && zeile.getNewslettergrund().length() > 0) {
					partnerDto.setNewslettergrundIId(
							getPartnerServicesFac().newslettergrundFindByCBez(zeile.getNewslettergrund()).getIId());
				}

				partnerDto.setCIln(zeile.getIln());
				partnerDto.setCUid(zeile.getUid());
				partnerDto.setCFilialnummer(zeile.getFilialnr());

				if (zeile.getGmtversatz().length() > 0) {

					try {
						Double d = new Double(zeile.getGmtversatz());

						partnerDto.setFGmtversatz(d);
					} catch (NumberFormatException e) {
						//
					}
				}

				// Partnerklasse

				Integer partnerklasseIId = null;

				if (zeile.getPartnerklasse().length() > 0) {
					try {
						Query klasse = em.createNamedQuery("PartnerklassefindByCNr");
						klasse.setParameter(1, zeile.getPartnerklasse());
						partnerklasseIId = ((Partnerklasse) klasse.getSingleResult()).getIId();

					} catch (NoResultException ex) {
						// Neu anlegen
						PartnerklasseDto klasseDto = new PartnerklasseDto();
						klasseDto.setCNr(zeile.getPartnerklasse());
						try {
							partnerklasseIId = getPartnerFac().createPartnerklasse(klasseDto, theClientDto);
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}
				}
				partnerDto.setPartnerklasseIId(partnerklasseIId);

				// Branche

				Integer brancheIId = null;

				if (zeile.getBranche().length() > 0) {
					try {
						Query branche = em.createNamedQuery("BranchefindByCNr");
						branche.setParameter(1, zeile.getBranche());
						brancheIId = ((Branche) branche.getSingleResult()).getIId();

					} catch (NoResultException ex) {
						// Neu anlegen
						BrancheDto brancheDto = new BrancheDto();
						brancheDto.setCNr(zeile.getBranche());
						try {
							brancheIId = getPartnerServicesFac().createBranche(brancheDto, theClientDto);
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}
				}
				partnerDto.setBrancheIId(brancheIId);

				// Anrede
				if (zeile.getAnrede().length() > 0) {
					if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {
						partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_HERR);
					} else if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {
						partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FRAU);

					} else if (zeile.getAnrede().equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {
						partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FIRMA);
					}
				}

				// Landplzort
				// LAND
				Session session1 = FLRSessionFactory.getFactory().openSession();

				String querystring1 = "SELECT l FROM FLRLand as l WHERE l.c_lkz='" + zeile.getLand() + "'";

				org.hibernate.Query query1 = session1.createQuery(querystring1);
				List<?> results = query1.list();

				Integer landIId = null;

				if (results.size() > 0) {

					FLRLand flrLand = (FLRLand) results.iterator().next();
					// bereits vorhanden, dann ID holen
					landIId = flrLand.getI_id();
				} else {
					LandDto landDto = new LandDto();
					landDto.setCLkz(zeile.getLand());
					landDto.setCName(zeile.getLand());
					landDto.setILaengeuidnummer(10);
					landDto.setBSepa(Helper.boolean2Short(false));

					try {
						landIId = getSystemFac().createLand(landDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				session1.close();

				// ORT

				Query queryOrt = em.createNamedQuery("OrtfindByCName");
				queryOrt.setParameter(1, zeile.getOrt());

				results = queryOrt.getResultList();

				Integer ortIId = null;

				if (results.size() > 0) {

					Ort ort = (Ort) results.iterator().next();
					// bereits vorhanden, dann ID holen
					ortIId = ort.getIId();
				} else {
					OrtDto landDto = new OrtDto();
					landDto.setCName(zeile.getOrt());
					try {
						ortIId = getSystemFac().createOrt(landDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// PLZ
				session1 = FLRSessionFactory.getFactory().openSession();

				querystring1 = "SELECT plz FROM FLRLandplzort as plz WHERE plz.c_plz='" + zeile.getPlz()
						+ "' AND plz.flrort.i_id=" + ortIId + " AND plz.flrland.i_id=" + landIId;

				query1 = session1.createQuery(querystring1);
				results = query1.list();

				Integer landplzortIId = null;

				if (results.size() > 0) {

					FLRLandplzort flrLandplzort = (FLRLandplzort) results.iterator().next();
					// bereits vorhanden, dann ID holen
					landplzortIId = flrLandplzort.getI_id();
				} else {
					LandplzortDto landplzortDto = new LandplzortDto();
					landplzortDto.setCPlz(zeile.getPlz());
					landplzortDto.setOrtIId(ortIId);
					landplzortDto.setIlandID(landIId);

					LandDto landDto = new LandDto();
					landDto.setCLkz(zeile.getLand());
					landDto.setCName(zeile.getLand());
					landDto.setILaengeuidnummer(10);
					landDto.setIID(landIId);
					landplzortDto.setLandDto(landDto);

					OrtDto ortDto = new OrtDto();
					ortDto.setCName(zeile.getOrt());
					ortDto.setIId(ortIId);
					landplzortDto.setOrtDto(ortDto);
					try {
						landplzortIId = getSystemFac().createLandplzort(landplzortDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				session1.close();

				partnerDto.setLandplzortIId(landplzortIId);

				// Kommunikation
				// Telefon
				if (zeile.getTelefon().length() > 0) {
					partnerDto.setCTelefon(zeile.getTelefon());
				}
				// Fax
				if (zeile.getFax().length() > 0) {
					partnerDto.setCFax(zeile.getFax());
				}
				// Email
				if (zeile.getEmail().length() > 0) {
					partnerDto.setCEmail(zeile.getEmail());
				}
				// Homepage
				if (zeile.getHomepage().length() > 0) {
					partnerDto.setCHomepage(zeile.getHomepage());
				}

				partnerDto.setXBemerkung(zeile.getBemerkung());

				// Kurzbezeichnung

				try {
					String sN1 = partnerDto.getCName1nachnamefirmazeile1() + " ";

					int iE = sN1.indexOf(" ");
					if (iE > PartnerFac.MAX_KBEZ / 2) {
						iE = PartnerFac.MAX_KBEZ / 2;
					}
					partnerDto.setCKbez(sN1.substring(0, iE));
				} catch (Exception e1) {
					partnerDto.setCKbez("KBEZ");
				}

				Integer partnerIId = null;
				try {
					partnerIId = getPartnerFac().createPartner(partnerDto, theClientDto);
				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}
				partnerIIdFuerAnsprechpartner = partnerIId;
				// Selektion
				if (zeile.getSelektion().length() > 0) {

					try {
						SelektionDto selektionDto = getPartnerServicesFac()
								.selektionFindByCNrMandantCNr(zeile.getSelektion(), theClientDto);
						if (selektionDto != null) {

							PASelektionDto paselDto = new PASelektionDto();
							paselDto.setPartnerIId(partnerIId);
							paselDto.setSelektionIId(selektionDto.getIId());
							getPartnerFac().createPASelektion(paselDto, theClientDto);
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// LAND-PLZ-ORT
				if (zeile.getLand().length() > 0 && zeile.getPlz().length() > 0 && zeile.getOrt().length() > 0) {

					// getSystemFac().

				}

				// Ansprechpartner anlegen
				// Wenn Vorname/Nachname noch nicht vorhanden

			}

			if (bAnsprechpartnerVorhanden == false && partnerIIdFuerAnsprechpartner != null) {
				if (zeile.getAnsprechpartnerNachname().length() > 0) {

					// Partner vorher anlegen
					PartnerDto partnerDto = new PartnerDto();
					partnerDto.setCName1nachnamefirmazeile1(zeile.getAnsprechpartnerNachname());
					partnerDto.setCName2vornamefirmazeile2(zeile.getAnsprechpartnerVorname());
					partnerDto.setCTitel(zeile.getAnsprechpartnerTitel());
					partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
					partnerDto.setBVersteckt(com.lp.util.Helper.boolean2Short(false));
					partnerDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

					String kbez = zeile.getAnsprechpartnerNachname();
					if (kbez.length() > 14) {
						kbez = zeile.getAnsprechpartnerNachname().substring(0, 13);
					}

					partnerDto.setCKbez(kbez);

					// Anrede
					if (zeile.getAnsprechpartnerAnrede().length() > 0) {
						if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_HERR.trim())) {
							partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_HERR);
						} else if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_FRAU.trim())) {
							partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FRAU);

						} else if (zeile.getAnsprechpartnerAnrede().equals(PartnerFac.PARTNER_ANREDE_FIRMA.trim())) {
							partnerDto.setAnredeCNr(PartnerFac.PARTNER_ANREDE_FIRMA);
						}
					}

					if (zeile.getGeburtsdatumansprechpartner() != null) {
						partnerDto.setDGeburtsdatumansprechpartner(
								new java.sql.Date(zeile.getGeburtsdatumansprechpartner().getTime()));
					}

					AnsprechpartnerDto ansprechpartnerDto = new AnsprechpartnerDto();
					try {
						ansprechpartnerDto
								.setPartnerIIdAnsprechpartner(getPartnerFac().createPartner(partnerDto, theClientDto));
					} catch (RemoteException e1) {
						throwEJBExceptionLPRespectOld(e1);
					}
					ansprechpartnerDto.setPartnerIId(partnerIIdFuerAnsprechpartner);

					ansprechpartnerDto.setBVersteckt(com.lp.util.Helper.boolean2Short(false));
					ansprechpartnerDto.setXBemerkung(zeile.getAnsprechpartnerBemerkung());

					if (zeile.getAnsprechpartnerGueltigab() != null) {
						ansprechpartnerDto
								.setDGueltigab(new java.sql.Date(zeile.getAnsprechpartnerGueltigab().getTime()));
					} else {
						ansprechpartnerDto.setDGueltigab(new java.sql.Date(System.currentTimeMillis()));
					}

					// Newslettergrund
					if (zeile.getAnsprechpartnernewslettergrund() != null
							&& zeile.getAnsprechpartnernewslettergrund().length() > 0) {
						ansprechpartnerDto.setNewslettergrundIId(getPartnerServicesFac()
								.newslettergrundFindByCBez(zeile.getAnsprechpartnernewslettergrund()).getIId());
					}

					// FAX-DW
					if (zeile.getAnsprechpartnerFaxDW().length() > 0) {
						ansprechpartnerDto.setCFax(zeile.getAnsprechpartnerFaxDW());
					}
					// TEL-DW
					if (zeile.getAnsprechpartnerTelefonDW().length() > 0) {
						ansprechpartnerDto.setCTelefon(zeile.getAnsprechpartnerTelefonDW());
					}
					// Handy

					if (zeile.getAnsprechpartnerMobil().length() > 0) {
						ansprechpartnerDto.setCHandy(zeile.getAnsprechpartnerMobil());
					}
					// Email

					if (zeile.getAnsprechpartnerEmail().length() > 0) {
						ansprechpartnerDto.setCEmail(zeile.getAnsprechpartnerEmail());
					}
					// Direktfax

					if (zeile.getAnsprechpartnerDirektfax().length() > 0) {
						ansprechpartnerDto.setCDirektfax(zeile.getAnsprechpartnerDirektfax());
					}

					// Ansprpechpartnerfunktion
					AnsprechpartnerfunktionDto dto = null;
					try {
						dto = getAnsprechpartnerFac()
								.ansprechpartnerfunktionFindByCnr(zeile.getAnsprechpartnerFunktion(), theClientDto);

						ansprechpartnerDto.setAnsprechpartnerfunktionIId(dto.getIId());
						ansprechpartnerDto.setISort(
								getAnsprechpartnerFac().getMaxISort(partnerIIdFuerAnsprechpartner).intValue() + 1);

						getAnsprechpartnerFac().createAnsprechpartner(ansprechpartnerDto, theClientDto);

					} catch (Throwable e) {
						e.printStackTrace();
						// auslassen
					}

				}

			}
			// Kunde anlegen

			if (partnerIIdFuerAnsprechpartner != null && bErzeugeKunde == true) {
				try {

					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIIdFuerAnsprechpartner,
							theClientDto);

					KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							partnerIIdFuerAnsprechpartner, theClientDto.getMandant(), theClientDto);

					if (kundeDto == null) {
						kundeDto = new KundeDto();
						kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());
						kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId());
						kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
						kundeDto.setbIstinteressent(new Short((short) 0));

						kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());

						kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
						// Vorbelegungen werden vom Mandanten geholt

						kundeDto.setMandantCNr(mandantDto.getCNr());
						kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
						kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
						kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
						kundeDto.setCWaehrung(mandantDto.getWaehrungCNr());
						kundeDto.setbIstinteressent(Helper.boolean2Short(false));
						kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
						kundeDto.setBDistributor(Helper.boolean2Short(false));
						kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
						kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
						kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
						kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
						kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
						kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
						kundeDto.setBReversecharge(Helper.boolean2Short(false));

						// damit die Debitorenkto. nicht anschlaegt.
						kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

						kundeDto.setPartnerDto(partnerDto);

						Integer kundeIId = getKundeFac().createKunde(kundeDto, theClientDto);

						if (zeile.getKontonummer() != null && zeile.getKontonummer().length() > 0) {
							getPartnerFac().kontoFuerPartnerImportAnlegen(zeile.getKontonummer(),
									FinanzServiceFac.KONTOTYP_DEBITOR, kundeIId, null, theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			// Lieferant anlegen

			if (partnerIIdFuerAnsprechpartner != null && bErzeugeLieferant == true) {
				try {

					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIIdFuerAnsprechpartner,
							theClientDto);

					LieferantDto lieferantDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
							partnerIIdFuerAnsprechpartner, theClientDto.getMandant(), theClientDto);

					if (lieferantDto == null) {
						lieferantDto = new LieferantDto();
						lieferantDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());

						lieferantDto.setIIdKostenstelle((mandantDto.getIIdKostenstelle()));
						lieferantDto.setBMoeglicherLieferant(new Short((short) 0));

						lieferantDto.setBVersteckt(Helper.boolean2Short(false));
						// Vorbelegungen werden vom Mandanten geholt

						lieferantDto.setMandantCNr(mandantDto.getCNr());
						lieferantDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
						lieferantDto.setIdSpediteur(mandantDto.getSpediteurIIdKunde());
						lieferantDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
						lieferantDto.setWaehrungCNr(mandantDto.getWaehrungCNr());

						lieferantDto.setBIgErwerb(Helper.boolean2Short(false));

						// lieferantDto.setBReversecharge(Helper
						// .boolean2Short(false));
						lieferantDto.setReversechargeartId(
								getFinanzServiceFac().reversechargeartFindOhne(theClientDto).getIId());
						lieferantDto.setPartnerDto(partnerDto);

						Integer liferantIId = getLieferantFac().createLieferant(lieferantDto, theClientDto);

						if (zeile.getKontonummer() != null && zeile.getKontonummer().length() > 0) {
							getPartnerFac().kontoFuerPartnerImportAnlegen(zeile.getKontonummer(),
									FinanzServiceFac.KONTOTYP_KREDITOR, null, liferantIId, theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			session.close();

		}

	}

	private Short getShortAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().trim().equals("0")) {
						return new Short((short) 0);
					} else if (c.getContents().trim().equals("1")) {
						return new Short((short) 1);
					} else {

						fehlerZeileXLSImport += feldname + " darf nur die Werte 0 bzw. 1 enthalten. Zeile " + iZeile
								+ new String(CRLFAscii);

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

	private java.util.Date getDateAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten,
			String feldname, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.DATE || c.getType() == CellType.DATE) {

						return ((jxl.DateCell) c).getDate();
					} else {

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Date' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

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

	private Double getDoubleAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new Double(d);

					} else {

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

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

	private String getStringAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iLaenge, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						fehlerZeileXLSImport += feldname + " ist zu lang (>" + iLaenge + ") Zeile " + iZeile
								+ new String(CRLFAscii);

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

	private Integer getIntegerAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (!hmVorhandeneSpalten.containsKey(feldname)) {
			return null;
		}

		Integer iSpalte = hmVorhandeneSpalten.get(feldname);

		if (iSpalte != null && zeilen.length > iSpalte) {

			Cell c = zeilen[iSpalte];

			if (c != null && c.getContents() != null && c.getContents().length() > 0) {

				if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

					double d = ((NumberCell) c).getValue();

					double dTest = new Double(d).intValue();
					if (dTest != d) {
						fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' und ganzzahlig sein. Zeile " + iZeile
								+ new String(CRLFAscii);
						return null;
					}

					return new Integer(new Double(d).intValue());
				} else {

					fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' und ganzzahlig sein. Zeile " + iZeile
							+ new String(CRLFAscii);
					return null;
				}

			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
