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
package com.lp.server.stueckliste.ejbfac;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentarart;
import com.lp.server.artikel.ejb.Material;
import com.lp.server.artikel.ejb.Verschleissteil;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.stueckliste.ejb.Profirstignore;
import com.lp.server.stueckliste.ejb.StuecklisteScriptart;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.ApkommentarsprDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.PruefkombinationsprDto;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Einheit;
import com.lp.server.system.ejb.Einheitspr;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.ImportErroInfo;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Stateless
public class StuecklisteimportFacBean extends Facade implements StuecklisteimportFac {
	@PersistenceContext
	private EntityManager em;

	private String fehlerZeileXLSImport = "";

	public static String XLS_ARBEITSPLANIMPORT_SPALTE_KOPFSTUECKLISTE = "Kopfstueckliste";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_KOPFBEZEICHNUNG = "Kopfbezeichnung";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AG_NUMMER = "AGNummer";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELBEZEICHNUNG = "Artikelbezeichnung";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_MASCHINENNUMMER = "Maschinennummer";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_RUESTZEIT = "Ruestzeit";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_STUECKZEIT = "Stueckzeit";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_KURZ = "KommentarKurz";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_LANG = "KommentarLang";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_NUR_MSCHINENZEIT = "NurMaschinenzeit";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AG_ART = "AGArt";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AG_BEGINN = "AGBeginn";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AUFSPANNUNG = "Aufspannung";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_PPMMENGE = "PPMMenge";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AP_KOMMENTARKENNUNG = "APKommentarkennung";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_AP_KOMMENTARBEZEICHNUNG = "APKommentarbezeichnung";
	public static String XLS_ARBEITSPLANIMPORT_SPALTE_MGZ = "Mgz";

	public static String XLS_MATERIALIMPORT_SPALTE_KOPFSTUECKLISTE = "Kopfstueckliste";
	public static String XLS_MATERIALIMPORT_SPALTE_KOPFBEZEICHNUNG = "Kopfbezeichnung";
	public static String XLS_MATERIALIMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";
	public static String XLS_MATERIALIMPORT_SPALTE_ARTIKELBEZEICHNUNG = "Artikelbezeichnung";
	public static String XLS_MATERIALIMPORT_SPALTE_POSITION = "Position";
	public static String XLS_MATERIALIMPORT_SPALTE_LFDNR = "LfdNr";
	public static String XLS_MATERIALIMPORT_SPALTE_KOMMENTAR = "Kommentar";
	public static String XLS_MATERIALIMPORT_SPALTE_MENGE = "Menge";
	public static String XLS_MATERIALIMPORT_SPALTE_MENGENEINHEIT = "Mengeneinheit";
	public static String XLS_MATERIALIMPORT_SPALTE_STUECKLISTENART = "Stuecklistenart";
	public static String XLS_MATERIALIMPORT_SPALTE_SCRIPTBEZEICHNUNG = "ScriptBezeichnung";
	public static String XLS_MATERIALIMPORT_SPALTE_KUNDEKBEZ = "KundeKbez";
	public static String XLS_MATERIALIMPORT_SPALTE_DEFAULTDURCHLAUFZEIT = "Defaultdurchlaufzeit";
	public static String XLS_MATERIALIMPORT_SPALTE_FERTIGUNGSGRUPPE = "Fertigungsgruppe";
	public static String XLS_MATERIALIMPORT_SPALTE_MONTAGEART = "Montageart";
	public static String XLS_MATERIALIMPORT_SPALTE_REIHENFOLGE = "Reihenfolge";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_AG_NUMMER = "AP_AGNummer";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_ARTIKELNUMMER = "AP_Artikelnummer";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_ARTIKELBEZEICHNUNG = "AP_Artikelbezeichnung";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_MASCHINENNUMMER = "AP_Maschinennummer";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_RUESTZEIT = "AP_Ruestzeit";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_STUECKZEIT = "AP_Stueckzeit";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_KURZ = "AP_KommentarKurz";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_LANG = "AP_KommentarLang";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_NUR_MSCHINENZEIT = "AP_NurMaschinenzeit";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_AG_ART = "AP_AGArt";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_AG_BEGINN = "AP_AGBeginn";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_AUFSPANNUNG = "AP_Aufspannung";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_PPMMENGE = "AP_PPMMenge";
	public static String XLS_MATERIALIMPORT_SPALTE_AP_MGZ = "AP_Mgz";
	public static String XLS_MATERIALIMPORT_SPALTE_ERFASSUNGSFAKTOR = "Erfassungsfaktor";
	public static String XLS_MATERIALIMPORT_SPALTE_DIMENSION1 = "Dimension1";
	public static String XLS_MATERIALIMPORT_SPALTE_DIMENSION2 = "Dimension2";
	public static String XLS_MATERIALIMPORT_SPALTE_DIMENSION3 = "Dimension3";

	public static String XLS_CREOIMPORT_SPALTE_MENGE = "Menge";
	public static String XLS_CREOIMPORT_SPALTE_ARTIKELBEZEICHNUNG = "Artikelbezeichnung";
	public static String XLS_CREOIMPORT_SPALTE_ZUSATZBEZEICHNUNG = "Zusatzbezeichnung";
	public static String XLS_CREOIMPORT_SPALTE_ZUSATZBEZEICHNUNG2 = "Zusatzbezeichnung2";
	public static String XLS_CREOIMPORT_SPALTE_MENGENEINHEIT = "Mengeneinheit";
	public static String XLS_CREOIMPORT_SPALTE_POSITION = "Position";
	public static String XLS_CREOIMPORT_SPALTE_BAUFORM = "Bauform";
	public static String XLS_CREOIMPORT_SPALTE_VERPACKUNGSART = "Verpackungsart";
	public static String XLS_CREOIMPORT_SPALTE_MATERIAL = "Material";
	public static String XLS_CREOIMPORT_SPALTE_HERSTELLER = "Hersteller";
	public static String XLS_CREOIMPORT_SPALTE_HERSTELLERNUMMER = "Herstellernummer";
	public static String XLS_CREOIMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";
	public static String XLS_CREOIMPORT_SPALTE_GEWICHT_IN_KG = "Gewicht";
	public static String XLS_CREOIMPORT_SPALTE_INDEX = "Index";
	public static String XLS_CREOIMPORT_SPALTE_REVISION = "Revision";
	public static String XLS_CREOIMPORT_SPALTE_LIEFERGRUPPE = "Liefergruppe";

	public static String loeschen = "<LOESCHEN>";

	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_PRUEFART = "Pruefart";

	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT = "ArtikelnummerKontakt";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE = "ArtikelnummerLitze";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE2 = "ArtikelnummerLitze2";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_DOPPELANSCHLAG = "Doppelanschlag";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_DRAHT = "CrimphoeheDraht";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_DRAHT = "CrimpbreiteDraht";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_ISOLATION = "CrimphoeheIsolation";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_ISOLATION = "CrimpbreiteIsolation";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPHOEHE_DRAHT = "ToleranzCrimphoeheDraht";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPBREITE_DRAHT = "ToleranzCrimpbreiteDraht";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPHOEHE_ISOLATION = "ToleranzCrimphoeheIsolation";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPBREITE_ISOLATION = "ToleranzCrimpbreiteIsolation";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_VERSCHLEISSTEIL = "Verschleissteil";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ABZUGSKRAFT_LITZE = "AbzugskraftLitze";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ABZUGSKRAFT_LITZE2 = "AbzugskraftLitze2";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_WERT = "Wert";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_WERT = "ToleranzWert";
	public static String XLS_PRUEFKOMBINATIONIMPORT_SPALTE_KOMMENTAR = "Kommentar";

	public static String XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_STUECKLISTE = "ArtikelnummerStueckliste";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_PRUEFART = "Pruefart";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT = "ArtikelnummerKontakt";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE = "ArtikelnummerLitze";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE2 = "ArtikelnummerLitze2";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_POSITION_KONTAKT = "PositionKontakt";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE = "PositionLitze";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE2 = "PositionLitze2";
	public static String XLS_PRUEFPLANIMPORT_SPALTE_VERSCHLEISSTEIL = "Verschleissteil";

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ImportErroInfo pruefeUndImportiereMaterialXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, String einheitStueckRuestZeit, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };

		ImportErroInfo info = new ImportErroInfo();
		String rueckgabe = "";
		String rueckgabeHinweis = "";
		try {

			HashSet hsStuecklistenIIdPositionenBereitsGeloescht = new HashSet();
			HashSet hsStuecklistenIIdArbeitsplanBereitsGeloescht = new HashSet();

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

			Integer montageartIId = null;
			MontageartDto[] dtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

			String default_artikeleinheit = null;
			Integer default_mwstsaztIId = null;

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);

			boolean bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();

			// Artikelnummer muss immer vorhanden sein
			if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_KOPFSTUECKLISTE) == false
					|| hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_ARTIKELNUMMER) == false
					|| hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_MENGE) == false) {
				rueckgabe += "Es muessen zumindest die Spalten 'Kopfstueckliste/Artikelnummer/Menge' vorhanden sein"
						+ new String(CRLFAscii);

				info.add2Error(rueckgabe);
				return info;
			}

			int iLaengenBezeichnung = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);
				fehlerZeileXLSImport = "";

				System.out.println(i + " von " + sheet.getRows());

				if (dtos != null && dtos.length > 0) {
					montageartIId = dtos[0].getIId();

				} else {
					// SP6892
					rueckgabe += "Es ist keine Default-Montageart definiert. Zeile " + (i+1) + new String(CRLFAscii);
					continue;
				}

				Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(XLS_MATERIALIMPORT_SPALTE_KOPFSTUECKLISTE);

				if (iSpalteArtikelnummer > sZeile.length - 1) {

					rueckgabeHinweis += "Die Artikelnummer der Kopfstueckliste enthaelt keine Daten. Zeile " + (i+1)
							+ " wird ausgelassen. " + new String(CRLFAscii);
					continue;
				}

				String artikelnummer = sZeile[iSpalteArtikelnummer].getContents();

				if (artikelnummer == null || artikelnummer.length() == 0) {
					rueckgabeHinweis += "Die Artikelnummer der Kopfstueckliste enthaelt keine Daten. Zeile " + (i+1)
							+ " wird ausgelassen. " + new String(CRLFAscii);
					continue;
				}

				String artikelnummerMaterial = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_ARTIKELNUMMER, 25, i);

				String sHandeingabe = null;

				if (artikelnummerMaterial == null || artikelnummerMaterial.length() == 0) {

					sHandeingabe = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_ARTIKELBEZEICHNUNG, iLaengenBezeichnung, i);

				}

				String stuecklistenart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_STUECKLISTENART, 40, i);

				String kundeKbez = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_KUNDEKBEZ, 40,
						i);

				//
				if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_MONTAGEART)) {
					String montageart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_MONTAGEART, 40, i);

					if (montageart != null && montageart.length() > 0) {
						boolean bGefunden = false;
						for (int k = 0; k < dtos.length; k++) {
							if (dtos[k].getCBez().trim().equals(montageart)) {
								montageartIId = dtos[k].getIId();
								bGefunden = true;
							}
						}
						if (bGefunden == false) {
							rueckgabe += "Die Montageart '" + montageart + "' konnte nicht gefunden werden. Zeile " + (i+1)
									+ new String(CRLFAscii);
							continue;
						}
					}

				}

				if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_SCRIPTBEZEICHNUNG)) {
					String scriptBezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_SCRIPTBEZEICHNUNG, 40, i);
					Integer scriptartIId = null;
					if (scriptBezeichnung != null && scriptBezeichnung.length() > 0
							&& !scriptBezeichnung.equals(loeschen)) {

						try {
							// duplicateunique: Pruefung: Artikelklasse
							// bereits
							// vorhanden.
							Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNrCBez");
							query.setParameter(1, theClientDto.getMandant());
							query.setParameter(2, scriptBezeichnung);

							StuecklisteScriptart scriptart = (StuecklisteScriptart) query.getSingleResult();
							scriptartIId = scriptart.getIId();
						} catch (NoResultException ex) {

							rueckgabe += "Die Stuecklisten-Scriptart '" + scriptBezeichnung
									+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;

						}

					}
				}

				if (stuecklistenart != null) {

					if (stuecklistenart.trim().equals("Stueckliste")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_STUECKLISTE;
					} else if (stuecklistenart.trim().equals("Hilfsstueckliste")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE;
					} else if (stuecklistenart.trim().equals("Setartikel")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_SETARTIKEL;
					} else {
						rueckgabe += "Die Stuecklistenart muss Stueckliste,Hilfsstueckliste oder Setartikel sein. Zeile "
								+ (i+1) + new String(CRLFAscii);
						continue;
					}

				} else {
					stuecklistenart = StuecklisteFac.STUECKLISTEART_STUECKLISTE;
				}

				ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);

				if (artikelDto != null) {
					artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelDto.getIId(), theClientDto);

					// Wenn Zentraler Artikelstamm, dann nur die eigenen
					// Stuecklisten importieren
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
						StuecklisteDto[] stklDtos = getStuecklisteFac()
								.stuecklisteFindByArtikelIId(artikelDto.getIId());

						if (stklDtos != null & stklDtos.length > 0) {

							if (stklDtos[0].getMandantCNr().equals(theClientDto.getMandant())) {

							} else {
								rueckgabe += "Die Stueckliste '" + artikelnummer + "' ist in Mandant "
										+ stklDtos[0].getMandantCNr() + " angelegt. Zeile " + (i+1) + new String(CRLFAscii);
								continue;
							}

						}

					}

					StuecklisteDto stklDtoVorhanden = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
							artikelDto.getIId(), theClientDto.getMandant());

					if (stklDtoVorhanden != null && stklDtoVorhanden.getTFreigabe() != null) {
						rueckgabe += "Die Stueckliste '" + artikelnummer
								+ "' ist freigegeben und darf nicht mehr veraendert werden. Zeile " + (i+1)
								+ new String(CRLFAscii);
						continue;
					}

				} else {
					artikelDto = new ArtikelDto();
					artikelDto.setCNr(artikelnummer);

					// Bezeichnungen
					if (artikelDto.getArtikelsprDto() == null) {
						artikelDto.setArtikelsprDto(new ArtikelsprDto());
						// Spr nur bei Neuanlage Updaten
						if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_KOPFBEZEICHNUNG)) {
							artikelDto.getArtikelsprDto().setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_KOPFBEZEICHNUNG, 40, i));
						}

					}

					artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
					artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);

					artikelDto.setEinheitCNr(default_artikeleinheit);

				}

				String mengeneinheit = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_MENGENEINHEIT, 15, i+1);

				if (mengeneinheit == null) {
					mengeneinheit = SystemFac.EINHEIT_STUECK;
				}

				// Dimension

				BigDecimal fDimension1 = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_DIMENSION1, i+1);
				BigDecimal fDimension2 = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_DIMENSION2, i+1);
				BigDecimal fDimension3 = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_DIMENSION3, i+1);

				String kommentar = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_KOMMENTAR, 80,
						i+1);

				String position = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_POSITION,
						StuecklisteFac.MAX_POSITION, i+1);

				BigDecimal lfdNummer = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_LFDNR,
						i+1);

				if (fehlerZeileXLSImport.length() > 0) {
					rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
				}

				if (bImportierenWennKeinFehler) {

					Integer artikelIId = null;

					if (artikelDto.getIId() != null) {

						artikelIId = artikelDto.getIId();

					} else {

						artikelDto.setBVersteckt(Helper.boolean2Short(false));

						artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
					}

					// Stueckliste
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId, theClientDto.getMandant());
					// Wenn Zentraler Artikelstamm, dann nur die eigenen
					// Stuecklisten importieren
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
						StuecklisteDto[] stklDtos = getStuecklisteFac().stuecklisteFindByArtikelIId(artikelIId);

						if (stklDtos != null && stklDtos.length > 0) {
							if (stklDtos[0].getMandantCNr().equals(theClientDto.getMandant())) {
								stklDto = stklDtos[0];
							} else {
								continue;
							}

						}

					}

					String xlsFertigungsgruppe = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_FERTIGUNGSGRUPPE, 40, i);
					FertigungsgruppeDto fertigungsgruppeDto = findFertigungsgruppe(xlsFertigungsgruppe, theClientDto);
					BigDecimal xlsDurchlaufzeit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_DEFAULTDURCHLAUFZEIT, i);

					BigDecimal xlsErfassungsfaktor = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_ERFASSUNGSFAKTOR, i);

					BigDecimal xlsReihenfolge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_MATERIALIMPORT_SPALTE_REIHENFOLGE, i);

					if (stklDto != null) {

						if (bVorhandenePositionenLoeschen) {
							if (!hsStuecklistenIIdPositionenBereitsGeloescht.contains(stklDto.getIId())) {

								// SP4478
								StklpruefplanDto[] ppDtos = getStuecklisteFac()
										.stklpruefplanFindByStuecklisteIId(stklDto.getIId());
								for (int j = 0; j < ppDtos.length; j++) {
									getStuecklisteFac().removeStklpruefplan(ppDtos[j].getIId());
								}

								StuecklistepositionDto[] posDtos = getStuecklisteFac()
										.stuecklistepositionFindByStuecklisteIId(stklDto.getIId(), theClientDto);

								for (int j = 0; j < posDtos.length; j++) {
									try {
										getStuecklisteFac().removeStuecklisteposition(posDtos[j], theClientDto);
									} catch (EJBExceptionLP e) {
										// Wenn nicht moeglich wg.
										// DB-Beziehungen, dann Menge auf 0
										// setzen
										posDtos[j].setNMenge(BigDecimal.ZERO);
										getStuecklisteFac().updateStuecklisteposition(posDtos[j], theClientDto);

									}
								}

								hsStuecklistenIIdPositionenBereitsGeloescht.add(stklDto.getIId());
							}
						}

						boolean hasToUpdate = false;
						// PJ19399
						if (!isStringEmpty(xlsFertigungsgruppe)) {
							stklDto.setFertigungsgruppeIId(fertigungsgruppeDto.getIId());
							hasToUpdate = true;
						}
						if (xlsDurchlaufzeit != null) {
							stklDto.setNDefaultdurchlaufzeit(xlsDurchlaufzeit);
							hasToUpdate = true;
						}

						if (xlsErfassungsfaktor != null) {
							stklDto.setNErfassungsfaktor(xlsErfassungsfaktor);
							hasToUpdate = true;
						}

						if (bReihenfolgenplanung && xlsReihenfolge != null) {
							if (xlsReihenfolge.intValue() < 0 || xlsReihenfolge.intValue() > 89) {
								rueckgabe += "Die Spalte 'Reihenfolge' muss einen Wert zwischen 0 und 89 enthalten. Zeile "
										+ (i+1) + new String(CRLFAscii);
								continue;
							} else {
								stklDto.setIReihenfolge(xlsReihenfolge.intValue());
								hasToUpdate = true;
							}
						}

						// PJ19142 Kundenzuordnung
						if (kundeKbez != null && kundeKbez.length() > 0) {
							stklDto.setPartnerIId(
									getKundeFac().erstelleBzwHoleKundeAnhandKbez(kundeKbez, theClientDto));
							hasToUpdate = true;
						}

						if (hasToUpdate) {
							getStuecklisteFac().updateStueckliste(stklDto, theClientDto);

						}
					} else {
						// Stueckliste neu anlegen
						stklDto = new StuecklisteDto();
						stklDto.setArtikelIId(artikelIId);

						try {
							parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stklDto.setBAusgabeunterstueckliste(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stklDto.setBMaterialbuchungbeiablieferung(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							stklDto.setFertigungsgruppeIId(fertigungsgruppeDto.getIId());
							stklDto.setLagerIIdZiellager(
									getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
							stklDto.setNLosgroesse(new BigDecimal(1));
							stklDto.setNErfassungsfaktor(new BigDecimal(1));

							stklDto.setBFremdfertigung(Helper.boolean2Short(false));

							stklDto.setStuecklisteartCNr(stuecklistenart);
							// PJ19399
							stklDto.setNDefaultdurchlaufzeit(xlsDurchlaufzeit);

							// PJ20196
							if (xlsErfassungsfaktor != null) {
								stklDto.setNErfassungsfaktor(xlsErfassungsfaktor);
							} else {
								stklDto.setNErfassungsfaktor(BigDecimal.ONE);
							}

							// PJ19142 Kundenzuordnung

							if (kundeKbez != null && kundeKbez.length() > 0) {
								stklDto.setPartnerIId(
										getKundeFac().erstelleBzwHoleKundeAnhandKbez(kundeKbez, theClientDto));
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						if (bReihenfolgenplanung) {
							if (xlsReihenfolge == null || xlsReihenfolge.intValue() < 0
									|| xlsReihenfolge.intValue() > 89) {
								rueckgabe += "Die Spalte 'Reihenfolge' muss vorhanden sein und einen Wert zwischen 0 und 89 enthalten. Zeile "
										+ (i+1) + new String(CRLFAscii);
								continue;
							} else {
								stklDto.setIReihenfolge(xlsReihenfolge.intValue());

							}
						}

						Integer stuecklisteIId = getStuecklisteFac().createStueckliste(stklDto, theClientDto);

						stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

						hsStuecklistenIIdPositionenBereitsGeloescht.add(stklDto.getIId());
						hsStuecklistenIIdArbeitsplanBereitsGeloescht.add(stklDto.getIId());
					}

					// PJ19268
					if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_SCRIPTBEZEICHNUNG)) {
						String scriptBezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_MATERIALIMPORT_SPALTE_SCRIPTBEZEICHNUNG, 40, i);

						if (scriptBezeichnung != null && scriptBezeichnung.length() > 0
								&& !scriptBezeichnung.equals(loeschen)) {

							try {

								Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNrCBez");
								query.setParameter(1, theClientDto.getMandant());
								query.setParameter(2, scriptBezeichnung);

								StuecklisteScriptart scriptart = (StuecklisteScriptart) query.getSingleResult();

								stklDto.setStuecklisteScriptartIId(scriptart.getIId());

							} catch (NoResultException ex) {

							}

						} else if (scriptBezeichnung != null && scriptBezeichnung.length() > 0
								&& scriptBezeichnung.equals(loeschen)) {
							stklDto.setStuecklisteScriptartIId(null);
						}

						getStuecklisteFac().updateStueckliste(stklDto, theClientDto);
					}

					// Material

					if ((sHandeingabe == null || sHandeingabe.length() == 0)
							&& (artikelnummerMaterial == null || artikelnummerMaterial.length() == 0)) {
						// Keine Position
					} else {
						BigDecimal menge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_MATERIALIMPORT_SPALTE_MENGE, i+1);

						if (menge == null) {

							rueckgabe += fehlerZeileXLSImport;
							rueckgabe += "Die Menge der Position darf nicht leer sein. Zeile " + (i+1)
									+ new String(CRLFAscii);
							continue;
						}
						boolean bVorhanden = false;
						Integer artikelIIdMaterial = null;
						if (sHandeingabe == null) {

							ArtikelDto artikelDtoMateiral = getArtikelFac()
									.artikelFindByCNrOhneExc(artikelnummerMaterial, theClientDto);

							if (artikelDtoMateiral != null) {
								artikelDtoMateiral = getArtikelFac()
										.artikelFindByPrimaryKey(artikelDtoMateiral.getIId(), theClientDto);

								artikelIIdMaterial = artikelDtoMateiral.getIId();

							} else {
								artikelDtoMateiral = new ArtikelDto();
								artikelDtoMateiral.setCNr(artikelnummerMaterial);
								artikelDtoMateiral.setMwstsatzbezIId(default_mwstsaztIId);
								artikelDtoMateiral.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
								artikelDtoMateiral.setBVersteckt(Helper.boolean2Short(false));

								// Einheit anlegen

								// Zuerast in Spr nachsehen

								try {

									Query query = em.createNamedQuery("EinheitsprfindByCBez");
									query.setParameter(1, mengeneinheit);
									query.setParameter(2, theClientDto.getLocUiAsString());

									Einheitspr einheitspr = (Einheitspr) query.getSingleResult();
									mengeneinheit = einheitspr.getPk().getEinheitCNr();

								} catch (NoResultException ex1) {

									try {
										// duplicateunique: Pruefung:
										// Artikelklasse
										// bereits
										// vorhanden.
										Query query = em.createNamedQuery("EinheitFindByCNr");
										query.setParameter(1, mengeneinheit);

										Einheit einheit = (Einheit) query.getSingleResult();

									} catch (NoResultException ex) {

										EinheitDto einheitDto = new EinheitDto();
										einheitDto.setCNr(mengeneinheit);
										einheitDto.setIDimension(0);
										getSystemFac().createEinheit(einheitDto, theClientDto);

									}

								}

								artikelDtoMateiral.setEinheitCNr(mengeneinheit);

								if (artikelDtoMateiral.getArtikelsprDto() == null) {

									artikelDtoMateiral.setArtikelsprDto(new ArtikelsprDto());
									artikelDtoMateiral.getArtikelsprDto()
											.setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
													XLS_MATERIALIMPORT_SPALTE_ARTIKELBEZEICHNUNG, iLaengenBezeichnung,
													i));
								}

								artikelIIdMaterial = getArtikelFac().createArtikel(artikelDtoMateiral, theClientDto);
							}

							StuecklistepositionDto[] vorhandenePositionenDesArtikels = getStuecklisteFac()
									.stuecklistepositionFindByStuecklisteIIdArtikelIId(stklDto.getIId(),
											artikelIIdMaterial, theClientDto);

							for (int u = 0; u < vorhandenePositionenDesArtikels.length; u++) {
								if (vorhandenePositionenDesArtikels[u].getCPosition() == null && position == null) {

									bVorhanden = true;

								} else if (vorhandenePositionenDesArtikels[u].getCPosition() != null && position != null
										&& vorhandenePositionenDesArtikels[u].getCPosition().equals(position)) {
									bVorhanden = true;
								}
							}

						} else {

							Session session = FLRSessionFactory.getFactory().openSession();

							String sQuery = "SELECT sp FROM FLRStuecklisteposition sp  LEFT OUTER JOIN  sp.flrartikel.artikelsprset as spr WHERE sp.stueckliste_i_id="
									+ stklDto.getIId() + " AND spr.c_bez='" + sHandeingabe + "'";

							org.hibernate.Query query = session.createQuery(sQuery);
							List<?> results = query.list();

							if (results.size() > 0) {
								bVorhanden = true;
							}

							session.close();

						}

						if (bVorhanden == false) {
							// Position
							StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
							stuecklistepositionDto.setStuecklisteIId(stklDto.getIId());

							if (sHandeingabe != null) {
								stuecklistepositionDto.setArtikelIId(null);
								stuecklistepositionDto.setSHandeingabe(sHandeingabe);
								stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
							} else {
								stuecklistepositionDto.setArtikelIId(artikelIIdMaterial);
								stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
							}

							// Einheit anlegen

							try {

								Query query = em.createNamedQuery("EinheitsprfindByCBez");
								query.setParameter(1, mengeneinheit);
								query.setParameter(2, theClientDto.getLocUiAsString());

								Einheitspr einheitspr = (Einheitspr) query.getSingleResult();
								mengeneinheit = einheitspr.getPk().getEinheitCNr();

							} catch (NoResultException ex1) {

								try {
									// duplicateunique: Pruefung: Artikelklasse
									// bereits
									// vorhanden.
									Query query = em.createNamedQuery("EinheitFindByCNr");
									query.setParameter(1, mengeneinheit);

									Einheit einheit = (Einheit) query.getSingleResult();

								} catch (NoResultException ex) {

									EinheitDto einheitDto = new EinheitDto();
									einheitDto.setCNr(mengeneinheit);
									einheitDto.setIDimension(0);
									getSystemFac().createEinheit(einheitDto, theClientDto);

								}
							}
							stuecklistepositionDto.setEinheitCNr(mengeneinheit);
							if (artikelIIdMaterial != null) {

								String mengeneinheitAusImportFile = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_MATERIALIMPORT_SPALTE_MENGENEINHEIT, 15, i);

								if (mengeneinheitAusImportFile == null) {

									ArtikelDto artikelDtoMateiral = getArtikelFac()
											.artikelFindByPrimaryKeySmall(artikelIIdMaterial, theClientDto);
									stuecklistepositionDto.setEinheitCNr(artikelDtoMateiral.getEinheitCNr());

									mengeneinheitAusImportFile = artikelDtoMateiral.getEinheitCNr();

								} else {

									try {

										Query query = em.createNamedQuery("EinheitsprfindByCBez");
										query.setParameter(1, mengeneinheitAusImportFile);
										query.setParameter(2, theClientDto.getLocUiAsString());

										Einheitspr einheitspr = (Einheitspr) query.getSingleResult();
										mengeneinheitAusImportFile = einheitspr.getPk().getEinheitCNr();

									} catch (NoResultException ex1) {

									}

								}

								EinheitDto ehtDto = getSystemFac().einheitFindByPrimaryKey(
										Helper.fitString2Length(mengeneinheitAusImportFile, 15, ' '), theClientDto);

								if (ehtDto.getIDimension() > 0) {

									if (fDimension1 != null) {
										stuecklistepositionDto.setFDimension1(fDimension1.floatValue());
									} else {
										stuecklistepositionDto.setFDimension1(0F);
									}

									if (ehtDto.getIDimension() > 1) {

										if (fDimension2 != null) {
											stuecklistepositionDto.setFDimension2(fDimension2.floatValue());
										} else {
											stuecklistepositionDto.setFDimension2(0F);
										}
										if (ehtDto.getIDimension() > 2) {
											if (fDimension3 != null) {
												stuecklistepositionDto.setFDimension3(fDimension3.floatValue());
											} else {
												stuecklistepositionDto.setFDimension3(0F);
											}
										}
									}

								}

							}

							if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_LFDNR) && lfdNummer != null) {
								stuecklistepositionDto.setILfdnummer(lfdNummer.intValue());

							}

							if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_KOMMENTAR)) {
								stuecklistepositionDto.setCKommentar(kommentar);
							}
							if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_POSITION)) {
								stuecklistepositionDto.setCPosition(position);
							}

							stuecklistepositionDto.setNMenge(menge);
							stuecklistepositionDto.setBMitdrucken(Helper.boolean2Short(false));
							stuecklistepositionDto.setMontageartIId(montageartIId);

							getStuecklisteFac().createStuecklisteposition(stuecklistepositionDto, theClientDto);
						}
					}
					// Arbeitsplan
					if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_AG_NUMMER) == true
							&& hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_ARTIKELNUMMER) == true) {

						String artikelnummerArbeitsplan = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_MATERIALIMPORT_SPALTE_AP_ARTIKELNUMMER, 25, i);

						BigDecimal agNummer = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_MATERIALIMPORT_SPALTE_AP_AG_NUMMER, i);

						if ((artikelnummerArbeitsplan == null

								|| artikelnummerArbeitsplan.length() == 0) || agNummer == null) {
							// Kein Arbeitsplan
						} else {

							BigDecimal stueckzeit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_STUECKZEIT, i);

							BigDecimal ruestzeit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_RUESTZEIT, i);

							String inventarnummer_maschine = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_MASCHINENNUMMER, 40, i);
							Integer maschineIId = null;
							if (inventarnummer_maschine != null) {

								Query query = em.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
								query.setParameter(1, theClientDto.getMandant());
								query.setParameter(2, inventarnummer_maschine);
								Maschine maschine;
								try {
									maschine = (Maschine) query.getSingleResult();
									maschineIId = maschine.getIId();
								} catch (NoResultException e) {
									rueckgabe += "Ein Maschine mit der Inventarnummer '" + inventarnummer_maschine
											+ "' ist nicht vorhanden. Zeile " + (i+1) + new String(CRLFAscii);
									continue;
								}

							}

							String agart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_AG_ART, 15, i);

							if (agart != null) {
								if (agart.trim().equals(StuecklisteFac.AGART_LAUFZEIT.trim())
										|| agart.trim().equals(StuecklisteFac.AGART_UMSPANNZEIT.trim())) {
								} else {
									rueckgabe += "AG-Art muss entweder 'Laufzeit' oder 'Umspannzeit' sein. Zeile " + (i+1)
											+ new String(CRLFAscii);
									continue;
								}

							}

							String kommentarKurz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_KURZ, 80, i);

							String kommentarLang = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_LANG, 3000, i);

							BigDecimal aufspannung = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_AUFSPANNUNG, i);

							BigDecimal ppmmenge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_PPMMENGE, i);

							BigDecimal mgz = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_MGZ, i);

							BigDecimal agBeginn = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_AG_BEGINN, i);

							Short nurMaschinenzeit = getShortAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_MATERIALIMPORT_SPALTE_AP_NUR_MSCHINENZEIT, i);

							if (nurMaschinenzeit == null) {
								nurMaschinenzeit = Helper.boolean2Short(false);
							}

							if (fehlerZeileXLSImport.length() > 0) {
								rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
							}

							if (bImportierenWennKeinFehler) {

								if (bVorhandenePositionenLoeschen) {
									if (!hsStuecklistenIIdArbeitsplanBereitsGeloescht.contains(stklDto.getIId())) {

										StuecklistearbeitsplanDto[] posDtos = getStuecklisteFac()
												.stuecklistearbeitsplanFindByStuecklisteIId(stklDto.getIId(),
														theClientDto);

										for (int j = 0; j < posDtos.length; j++) {
											getStuecklisteFac().removeStuecklistearbeitsplan(posDtos[j], theClientDto);
										}

										hsStuecklistenIIdArbeitsplanBereitsGeloescht.add(stklDto.getIId());
									}
								}

								// Arbeitsplan

								ArtikelDto artikelDtoArbeitsplan = getArtikelFac()
										.artikelFindByCNrOhneExc(artikelnummerArbeitsplan, theClientDto);

								Integer artikelIIdArbeitsplan = null;
								if (artikelDtoArbeitsplan != null) {
									artikelDtoArbeitsplan = getArtikelFac()
											.artikelFindByPrimaryKey(artikelDtoArbeitsplan.getIId(), theClientDto);

									artikelIIdArbeitsplan = artikelDtoArbeitsplan.getIId();

								} else {
									artikelDtoArbeitsplan = new ArtikelDto();
									artikelDtoArbeitsplan.setCNr(artikelnummerArbeitsplan);
									artikelDtoArbeitsplan.setMwstsatzbezIId(default_mwstsaztIId);
									artikelDtoArbeitsplan.setArtikelartCNr(ArtikelFac.ARTIKELART_ARBEITSZEIT);
									artikelDtoArbeitsplan.setBVersteckt(Helper.boolean2Short(false));
									artikelDtoArbeitsplan.setEinheitCNr(SystemFac.EINHEIT_STUNDE);

									if (artikelDtoArbeitsplan.getArtikelsprDto() == null) {
										artikelDtoArbeitsplan.setArtikelsprDto(new ArtikelsprDto());
										artikelDtoArbeitsplan.getArtikelsprDto()
												.setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
														XLS_MATERIALIMPORT_SPALTE_AP_ARTIKELBEZEICHNUNG,
														iLaengenBezeichnung, i));
									}

									artikelIIdArbeitsplan = getArtikelFac().createArtikel(artikelDtoArbeitsplan,
											theClientDto);
								}

								Query query = em.createNamedQuery(
										"StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
								query.setParameter(1, stklDto.getIId());
								query.setParameter(2, agNummer.intValue());
								Collection<?> cl = query.getResultList();

								// Nur importieren, wenn noch kein AG vorhanden
								if (cl.size() == 0) {
									// Arbeitsplan
									StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
									stuecklistearbeitsplanDto.setStuecklisteIId(stklDto.getIId());
									stuecklistearbeitsplanDto.setArtikelIId(artikelIIdArbeitsplan);
									stuecklistearbeitsplanDto.setIArbeitsgang(agNummer.intValue());
									if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_AG_ART)) {

										stuecklistearbeitsplanDto.setAgartCNr(agart);
									}
									stuecklistearbeitsplanDto.setBNurmaschinenzeit(nurMaschinenzeit);

									if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_MASCHINENNUMMER)) {

										stuecklistearbeitsplanDto.setMaschineIId(maschineIId);
									}

									if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_KURZ)) {
										stuecklistearbeitsplanDto.setCKommentar(kommentarKurz);
									}
									if (hmVorhandeneSpalten.containsKey(XLS_MATERIALIMPORT_SPALTE_AP_KOMMENTAR_LANG)) {
										stuecklistearbeitsplanDto.setXLangtext(kommentarLang);
									}

									if (aufspannung != null) {
										stuecklistearbeitsplanDto.setIAufspannung(aufspannung.intValue());
									}

									if (ppmmenge != null) {
										stuecklistearbeitsplanDto.setNPpm(ppmmenge);
									}

									if (mgz != null) {
										stuecklistearbeitsplanDto.setIMitarbeitergleichzeitig(mgz.intValue());
									}

									Long lStueckzeit = 0L;
									if (stueckzeit != null) {

										if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_STUNDE)) {
											lStueckzeit = stueckzeit.multiply(new BigDecimal(3600000)).longValue();
										} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_MINUTE)) {
											lStueckzeit = stueckzeit.multiply(new BigDecimal(60000)).longValue();
										} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_SEKUNDE)) {
											lStueckzeit = stueckzeit.multiply(new BigDecimal(1000)).longValue();
										}

									}
									stuecklistearbeitsplanDto.setLStueckzeit(lStueckzeit);

									Long lRuestzeit = 0L;
									if (ruestzeit != null) {

										if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_STUNDE)) {
											lRuestzeit = ruestzeit.multiply(new BigDecimal(3600000)).longValue();
										} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_MINUTE)) {
											lRuestzeit = ruestzeit.multiply(new BigDecimal(60000)).longValue();
										} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_SEKUNDE)) {
											lRuestzeit = ruestzeit.multiply(new BigDecimal(1000)).longValue();
										}

									}
									stuecklistearbeitsplanDto.setLRuestzeit(lRuestzeit);

									if (agBeginn != null) {
										stuecklistearbeitsplanDto.setIMaschinenversatztage(agBeginn.intValue());
									}

									getStuecklisteFac().createStuecklistearbeitsplan(stuecklistearbeitsplanDto,
											theClientDto);
								}

							}
						}
					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		if (rueckgabe != null && rueckgabe.length() > 0) {
			info.add2Error(rueckgabe);
		}

		if (rueckgabeHinweis != null && rueckgabeHinweis.length() > 0) {
			info.add2Info(rueckgabeHinweis);
		}

		return info;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ImportErroInfo pruefeUndImportiereCreoXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };

		ImportErroInfo info = new ImportErroInfo();
		String rueckgabe = "";
		String rueckgabeHinweis = "";
		try {

			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			if (sheet.getRows() > 5) {

				// Artikel/Stklnummer in Spalte A1

				Cell[] sZeile = sheet.getRow(0);

				String artikelnummerStueckliste = sZeile[0].getContents();

				if (artikelnummerStueckliste == null || artikelnummerStueckliste.length() == 0) {
					rueckgabe += "In Zelle A1 muss ein Stkl-Artikelnummer definiert sein" + new String(CRLFAscii);
				} else {

					String artikelbezeichnung = null;

					if (sheet.getRow(4).length > 0) {
						artikelbezeichnung = sheet.getRow(4)[0].getContents();
					}

					if (artikelbezeichnung == null || artikelbezeichnung.length() == 0) {
						rueckgabe += "In Zelle A5 muss die Stkl-Bezeichnug definiert sein" + new String(CRLFAscii);
					} else {

						HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

						sZeile = sheet.getRow(5);
						for (int i = 0; i < sZeile.length; i++) {
							if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
								hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
							}
						}

						Integer montageartIId = null;
						MontageartDto[] dtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

						String default_artikeleinheit = null;
						Integer default_mwstsaztIId = null;

						ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
						default_artikeleinheit = parameter.getCWert();

						parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
						if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
							default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
						}

						// Es muessen immer die Spalten Menge
						if (hmVorhandeneSpalten.containsKey(XLS_CREOIMPORT_SPALTE_MENGE) == false
								|| hmVorhandeneSpalten.containsKey(XLS_CREOIMPORT_SPALTE_ARTIKELNUMMER) == false
								|| hmVorhandeneSpalten.containsKey(XLS_CREOIMPORT_SPALTE_POSITION) == false) {
							rueckgabe += "Es muessen zumindest die Spalten 'Artikelnummer/Menge/Position' vorhanden sein"
									+ new String(CRLFAscii);

							info.add2Error(rueckgabe);
							return info;
						}

						// Ist Stueckliste schon vorhanden

						ArtikelDto artikelDtoStueckliste = getArtikelFac()
								.artikelFindByCNrOhneExc(artikelnummerStueckliste, theClientDto);
						Integer stuecklisteIId = null;
						if (artikelDtoStueckliste == null && bImportierenWennKeinFehler) {
							artikelDtoStueckliste = erzeugeArtikel(artikelnummerStueckliste, artikelbezeichnung,
									theClientDto);
							StuecklisteDto stklDto = erzeugeStueckliste(artikelDtoStueckliste.getIId(), theClientDto);
							stuecklisteIId = stklDto.getIId();

							if (bVorhandenePositionenLoeschen == true) {
								// SP4478
								StklpruefplanDto[] ppDtos = getStuecklisteFac()
										.stklpruefplanFindByStuecklisteIId(stklDto.getIId());
								for (int j = 0; j < ppDtos.length; j++) {
									getStuecklisteFac().removeStklpruefplan(ppDtos[j].getIId());
								}

								StuecklistepositionDto[] posDtos = getStuecklisteFac()
										.stuecklistepositionFindByStuecklisteIId(stklDto.getIId(), theClientDto);

								for (int j = 0; j < posDtos.length; j++) {
									try {
										getStuecklisteFac().removeStuecklisteposition(posDtos[j], theClientDto);
									} catch (EJBExceptionLP e) {
										// Wenn nicht moeglich wg.
										// DB-Beziehungen, dann Menge auf 0
										// setzen
										posDtos[j].setNMenge(BigDecimal.ZERO);
										getStuecklisteFac().updateStuecklisteposition(posDtos[j], theClientDto);

									}
								}
							}
						} else if (artikelDtoStueckliste != null && bImportierenWennKeinFehler) {
							StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
									artikelDtoStueckliste.getIId(), theClientDto.getMandant());
							if (stklDto == null) {
								stklDto = erzeugeStueckliste(artikelDtoStueckliste.getIId(), theClientDto);
								stuecklisteIId = stklDto.getIId();
							}
							stuecklisteIId = stklDto.getIId();

							if (bVorhandenePositionenLoeschen == true) {
								// SP4478
								StklpruefplanDto[] ppDtos = getStuecklisteFac()
										.stklpruefplanFindByStuecklisteIId(stklDto.getIId());
								for (int j = 0; j < ppDtos.length; j++) {
									getStuecklisteFac().removeStklpruefplan(ppDtos[j].getIId());
								}

								StuecklistepositionDto[] posDtos = getStuecklisteFac()
										.stuecklistepositionFindByStuecklisteIId(stklDto.getIId(), theClientDto);

								for (int j = 0; j < posDtos.length; j++) {
									try {
										getStuecklisteFac().removeStuecklisteposition(posDtos[j], theClientDto);
									} catch (EJBExceptionLP e) {
										// Wenn nicht moeglich wg.
										// DB-Beziehungen, dann Menge auf 0
										// setzen
										posDtos[j].setNMenge(BigDecimal.ZERO);
										getStuecklisteFac().updateStuecklisteposition(posDtos[j], theClientDto);

									}
								}
							}

						} else if (artikelDtoStueckliste != null && bImportierenWennKeinFehler == false) {
							StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
									artikelDtoStueckliste.getIId(), theClientDto.getMandant());
							if (stklDto != null && stklDto.getTFreigabe() != null) {
								rueckgabe += "Die Stueckliste " + artikelDtoStueckliste.getCNr()
										+ " ist freigegeben und darf nicht mehr veraendert werden."
										+ new String(CRLFAscii);
								info.add2Error(rueckgabe);
								return info;
							}
						}

						int iRows = sheet.getRows();

						for (int i = 6; i < iRows; i++) {
							sZeile = sheet.getRow(i);
							fehlerZeileXLSImport = "";

							System.out.println(i + " von " + sheet.getRows());

							if (dtos != null && dtos.length > 0) {
								montageartIId = dtos[0].getIId();

							} else {
								// SP6892
								rueckgabe += "Es ist keine Default-Montageart definiert. Zeile " + i
										+ new String(CRLFAscii);
								continue;
							}

							// Artikelnummer kommt entweder aus der Spalte Artikelnummer oder wenn diese
							// leer ist, wird die Artikelnummer aus
							// Stkl-Nr * + PosNr erzeugt

							String artikelnummerMaterial = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_ARTIKELNUMMER, 25, i);

							String posNr = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_CREOIMPORT_SPALTE_POSITION,
									25, i);

							if (artikelnummerMaterial == null && posNr == null) {
								// rueckgabe += "Es ist weder die Spalte Artikelnummer noch die Spalte Position
								// befuellt. Zeile "
								// + i + " wird ausgelassen. " + new String(CRLFAscii);
								continue;
							}

							if (artikelnummerMaterial == null) {
								artikelnummerMaterial = artikelnummerStueckliste + "_" + posNr;
							}

							// PJ22044
							artikelnummerMaterial = artikelnummerMaterial.toUpperCase();

							try {
								getArtikelFac().pruefeArtikelnummer(artikelnummerMaterial, theClientDto);
							} catch (EJBExceptionLP e) {
								rueckgabe += fehlerZeileXLSImport;
								rueckgabe += "Die Artikelnummmer '" + artikelnummerMaterial
										+ "' enthaelt ungueltige Zeichen. Zeile " + i + new String(CRLFAscii);
								continue;
							}

							BigDecimal menge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_MENGE, i);

							if (menge == null) {

								rueckgabe += fehlerZeileXLSImport;
								rueckgabe += "Die Menge der Position darf nicht leer sein. Zeile " + i
										+ new String(CRLFAscii);
								continue;
							}

							// Material
							String material = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_MATERIAL, 50, i);
							// Verpackungsart
							String verpackungart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_VERPACKUNGSART, 20, i);

							// Gewicht
							BigDecimal gewichtInKg = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_GEWICHT_IN_KG, i);

							// Index
							String index = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_CREOIMPORT_SPALTE_INDEX, 15,
									i);

							// Revision
							String revision = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_REVISION, 40, i);

							// Liefergruppe
							String liefergruppe = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_LIEFERGRUPPE, 40, i);

							// Hersteller
							String hersteller = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_HERSTELLER, 40, i);

							// Herstellernummer
							String herstellernummmer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_HERSTELLERNUMMER, 300, i);

							// Bauform
							String bauform = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_CREOIMPORT_SPALTE_BAUFORM,
									20, i);

							int iLaenge = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());

							// Bezeichnung
							String bezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_ARTIKELBEZEICHNUNG, iLaenge, i);

							// Zusatzbezeichnung
							String zusatzbezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_ZUSATZBEZEICHNUNG, iLaenge, i);

							// Zusatzbezeichnung
							String zusatzbezeichnung2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_CREOIMPORT_SPALTE_ZUSATZBEZEICHNUNG2, iLaenge, i);

							// Dimension

							if (fehlerZeileXLSImport.length() > 0) {
								rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
							}

							if (bImportierenWennKeinFehler) {

								// Material

								Integer artikelIIdMaterial = null;

								ArtikelDto artikelDtoMateiral = getArtikelFac()
										.artikelFindByCNrOhneExc(artikelnummerMaterial, theClientDto);

								if (artikelDtoMateiral != null) {
									artikelDtoMateiral = getArtikelFac()
											.artikelFindByPrimaryKey(artikelDtoMateiral.getIId(), theClientDto);

									artikelIIdMaterial = artikelDtoMateiral.getIId();

								} else {
									artikelDtoMateiral = new ArtikelDto();
									artikelDtoMateiral.setCNr(artikelnummerMaterial);
									artikelDtoMateiral.setMwstsatzbezIId(default_mwstsaztIId);
									artikelDtoMateiral.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
									artikelDtoMateiral.setBVersteckt(Helper.boolean2Short(false));

									// Einheit anlegen

									// Zuerast in Spr nachsehen

									String mengeneinheit = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_CREOIMPORT_SPALTE_MENGENEINHEIT, 15, i);

									if (mengeneinheit == null) {
										mengeneinheit = SystemFac.EINHEIT_STUECK;
									}

									try {

										Query query = em.createNamedQuery("EinheitsprfindByCBez");
										query.setParameter(1, mengeneinheit);
										query.setParameter(2, theClientDto.getLocUiAsString());

										Einheitspr einheitspr = (Einheitspr) query.getSingleResult();
										mengeneinheit = einheitspr.getPk().getEinheitCNr();

									} catch (NoResultException ex1) {

										try {
											// duplicateunique: Pruefung:
											// Artikelklasse
											// bereits
											// vorhanden.
											Query query = em.createNamedQuery("EinheitFindByCNr");
											query.setParameter(1, mengeneinheit);

											Einheit einheit = (Einheit) query.getSingleResult();

										} catch (NoResultException ex) {

											EinheitDto einheitDto = new EinheitDto();
											einheitDto.setCNr(mengeneinheit);
											einheitDto.setIDimension(0);
											getSystemFac().createEinheit(einheitDto, theClientDto);

										}

									}

									artikelDtoMateiral.setEinheitCNr(mengeneinheit);

									if (artikelDtoMateiral.getArtikelsprDto() == null) {

										artikelDtoMateiral.setArtikelsprDto(new ArtikelsprDto());
										artikelDtoMateiral.getArtikelsprDto().setCBez(bezeichnung);

										// Zusatzbezeichnung

										artikelDtoMateiral.getArtikelsprDto().setCZbez(zusatzbezeichnung);
										artikelDtoMateiral.getArtikelsprDto().setCZbez2(zusatzbezeichnung2);

									}

									artikelIIdMaterial = getArtikelFac().createArtikel(artikelDtoMateiral,
											theClientDto);

								}

								// Artikeleigenschaften ergaenzen

								artikelDtoMateiral = getArtikelFac().artikelFindByPrimaryKey(artikelIIdMaterial,
										theClientDto);

								if (artikelDtoMateiral.getVerpackungDto() == null) {
									artikelDtoMateiral.setVerpackungDto(new VerpackungDto());
								}

								if (bauform != null) {
									artikelDtoMateiral.getVerpackungDto().setCBauform(bauform);
								}
								if (verpackungart != null) {
									artikelDtoMateiral.getVerpackungDto().setCVerpackungsart(verpackungart);
								}
								if (index != null) {
									artikelDtoMateiral.setCIndex(index);
								}
								if (revision != null) {
									artikelDtoMateiral.setCRevision(revision);
								}
								if (gewichtInKg != null) {
									artikelDtoMateiral.setFGewichtkg(gewichtInKg.doubleValue());
								}

								if (material != null) {
									artikelDtoMateiral.setMaterialIId(
											getArtikelimportFac().materialSuchenUndAnlegen(theClientDto, material));
								}

								if (liefergruppe != null) {
									artikelDtoMateiral.setLfliefergruppeIId(
											liefergruppeSuchenUndAnlegen(theClientDto, liefergruppe));
								}

								if (artikelDtoMateiral.getArtikelsprDto() == null) {
									artikelDtoMateiral.setArtikelsprDto(new ArtikelsprDto());
								}

								if (bezeichnung != null) {
									artikelDtoMateiral.getArtikelsprDto().setCBez(bezeichnung);
								}

								if (herstellernummmer != null) {
									artikelDtoMateiral.setCArtikelnrhersteller(herstellernummmer);
								}

								if (zusatzbezeichnung != null) {
									artikelDtoMateiral.getArtikelsprDto().setCZbez(zusatzbezeichnung);
								}
								if (zusatzbezeichnung2 != null) {
									artikelDtoMateiral.getArtikelsprDto().setCZbez2(zusatzbezeichnung2);
								}

								if (hersteller != null) {
									artikelDtoMateiral.setHerstellerIId(
											getArtikelimportFac().herstellerSuchenUndAnlegen(theClientDto, hersteller));
								}

								getArtikelFac().updateArtikel(artikelDtoMateiral, theClientDto);

								// Position
								StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
								stuecklistepositionDto.setStuecklisteIId(stuecklisteIId);

								stuecklistepositionDto.setArtikelIId(artikelIIdMaterial);
								stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);

								stuecklistepositionDto.setEinheitCNr(artikelDtoMateiral.getEinheitCNr());

								if (hmVorhandeneSpalten.containsKey(XLS_CREOIMPORT_SPALTE_POSITION) && posNr != null) {
									stuecklistepositionDto.setCPosition(posNr);
								}

								stuecklistepositionDto.setNMenge(menge);
								stuecklistepositionDto.setBMitdrucken(Helper.boolean2Short(false));
								stuecklistepositionDto.setMontageartIId(montageartIId);

								getStuecklisteFac().createStuecklisteposition(stuecklistepositionDto, theClientDto);

							}
						}
					}
				}
			} else {
				rueckgabe += "Die Import-Datei muss zumindest 6 Zeilen haben." + new String(CRLFAscii);

			}
		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		if (rueckgabe != null && rueckgabe.length() > 0) {
			info.add2Error(rueckgabe);
		}

		if (rueckgabeHinweis != null && rueckgabeHinweis.length() > 0) {
			info.add2Info(rueckgabeHinweis);
		}

		return info;
	}

	public Integer getProFirstArtikelgruppe(TheClientDto theClientDto) {

		Integer artgruIId = null;
		try {

			ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_PRO_FIRST_ARTIKELGRUPPE);

			if (parameterDtoDefaultarbeitszeit != null && parameterDtoDefaultarbeitszeit.getCWert() != null
					&& !parameterDtoDefaultarbeitszeit.getCWert().trim().equals("")) {
				try {
					String sMandant = theClientDto.getMandant();
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
						sMandant = getSystemFac().getHauptmandant();
					}

					Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
					query.setParameter(1, parameterDtoDefaultarbeitszeit.getCWert());
					query.setParameter(2, sMandant);
					Artgru doppelt = (Artgru) query.getSingleResult();
					return doppelt.getIId();
				} catch (NoResultException ex) {

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT,
							new Exception("FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT"));
				}
			} else {
				parameterDtoDefaultarbeitszeit = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD);

				if ((Boolean) parameterDtoDefaultarbeitszeit.getCWertAsObject()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT,
							new Exception("FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT"));
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return artgruIId;
	}

	private boolean isStringEmpty(String string) {
		return string == null || string.isEmpty();
	}

	private FertigungsgruppeDto findFertigungsgruppe(String xlsFertigungsgruppe, TheClientDto theClientDto)
			throws RemoteException {
		if (xlsFertigungsgruppe != null) {
			FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNrCBezOhneExc(theClientDto.getMandant(), xlsFertigungsgruppe);
			if (fertigungsgruppeDto != null)
				return fertigungsgruppeDto;

			// create if not exist
			fertigungsgruppeDto = getStuecklisteFac().setupDefaultFertigungsgruppe(xlsFertigungsgruppe, theClientDto);
			fertigungsgruppeDto.setIId(getStuecklisteFac().createFertigungsgruppe(fertigungsgruppeDto, theClientDto));
			return fertigungsgruppeDto;
		}

		FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
				.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);
		if (fertigungsgruppeDtos.length > 0) {
			return fertigungsgruppeDtos[0];
		}

		return new FertigungsgruppeDto();
	}

	public String pruefeUndImportierePruefkombinationXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {
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
			fehlerZeileXLSImport = "";
			if (!hmVorhandeneSpalten.containsKey(XLS_PRUEFKOMBINATIONIMPORT_SPALTE_PRUEFART)) {
				rueckgabe += "Es muss zumindest die Spalte 'Pruefart' vorhanden sein." + new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						String pruefart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_PRUEFKOMBINATIONIMPORT_SPALTE_PRUEFART, 40, i);

						if (pruefart == null || pruefart.length() == 0) {
							rueckgabe += "Die Pruefart darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						Integer artikelIIdKontakt = null;
						Integer artikelIIdLitze = null;
						Integer artikelIIdLitze2 = null;
						Integer verschleissteilIId = null;

						String sQuery = "";

						if (StuecklisteFac.PRUEFART_MATERIALSTATUS.equals(pruefart)) {
							rueckgabe += "Fuer Pruefart '" + StuecklisteFac.PRUEFART_MATERIALSTATUS
									+ "' sind keine Pruefkombinationen vorgesehen. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Kontakt
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
							// Hier muss ein Kontakt vorhanden sein
							// Kontakt
							String artikelKontakt = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT,
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
							if (artikelKontakt == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO/elekrische Pruefung/optische Pruefung/Kraftmessung' oder 'Kraftmessung' muss das Feld '"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT
										+ "' befuellt sein. Zeile" + i + new String(CRLFAscii);
								continue;
							} else {
								ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByCNrOhneExc(artikelKontakt,
										theClientDto);
								if (aDtoKontakt == null) {
									rueckgabe += "Die Artikelnummer '" + artikelKontakt + "' in der Spalte '"
											+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT
											+ "' konnte nicht gefunden werden. Zeile" + i + new String(CRLFAscii);
									continue;
								} else {
									artikelIIdKontakt = aDtoKontakt.getIId();
								}
							}

						}

						// Litze
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
							// Hier muss eine Litze vorhanden sein
							// Kontakt
							String artikelLitze = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE,
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
							if (artikelLitze == null) {

								if (!pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

									rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' oder 'Kraftmessung/Masspruefung' muss das Feld '"
											+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE
											+ "' befuellt sein. Zeile" + i + new String(CRLFAscii);
									continue;
								}
							} else {
								ArtikelDto aDtoLitze = getArtikelFac().artikelFindByCNrOhneExc(artikelLitze,
										theClientDto);
								if (aDtoLitze == null) {
									rueckgabe += "Die Artikelnummer '" + artikelLitze + "' in der Spalte '"
											+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE
											+ "' konnte nicht gefunden werden. Zeile" + i + new String(CRLFAscii);
									continue;
								} else {
									artikelIIdLitze = aDtoLitze.getIId();
								}
							}

						}

						// Litze2
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
							// Doppelanschlag
							Short doppelanschlag = getShortAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_DOPPELANSCHLAG, i);

							if (doppelanschlag == null) {
								rueckgabe += "Bei der Pruefart Crimpen mit/ohne ISO muss das Feld 'Doppelanschlag' mit 0/1 befuellt sein"
										+ i + new String(CRLFAscii);
								continue;
							} else {
								if (Helper.short2boolean(doppelanschlag)) {

									// Hier muss eine Litze vorhanden sein
									// Kontakt
									String artikelLitze2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
											XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE2,
											ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
									if (artikelLitze2 == null) {
										rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' und 'Doppelanschlag' muss das Feld '"
												+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE2
												+ "' befuellt sein. Zeile" + i + new String(CRLFAscii);
										continue;
									} else {
										ArtikelDto aDtoLitze2 = getArtikelFac().artikelFindByCNrOhneExc(artikelLitze2,
												theClientDto);
										if (aDtoLitze2 == null) {
											rueckgabe += "Die Artikelnummer '" + artikelLitze2 + "' in der Spalte '"
													+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ARTIKELNUMMER_LITZE2
													+ "' konnte nicht gefunden werden. Zeile" + i
													+ new String(CRLFAscii);
											continue;
										} else {
											artikelIIdLitze2 = aDtoLitze2.getIId();
										}
									}

								}
							}

							// Ausserdem muss ein Verschleissteil definiert sein

							String verschleissteil = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_VERSCHLEISSTEIL, 40, i);
							if (verschleissteil == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' muss das Feld '"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_VERSCHLEISSTEIL + "' befuellt sein. Zeile"
										+ i + new String(CRLFAscii);
								continue;
							} else {
								try {
									Query query = em.createNamedQuery("VerschleissteilfindByCNr");
									query.setParameter(1, verschleissteil);

									Verschleissteil v = (Verschleissteil) query.getSingleResult();
									verschleissteilIId = v.getIId();
									// Dann Update

								} catch (NoResultException ex1) {
									rueckgabe += "Verschleissteil '" + verschleissteil
											+ "' konnte nicht gefunden. Zeile" + i + new String(CRLFAscii);
									continue;

								}
							}

						}

						if (Helper.isOneOf(pruefart, StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO,
								StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO, StuecklisteFac.PRUEFART_MASSPRUEFUNG,
								StuecklisteFac.PRUEFART_KRAFTMESSUNG, StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG,
								StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG, StuecklisteFac.PRUEFART_FREIE_PRUEFUNG)) {

						} else {
							rueckgabe += "Pruefart '" + pruefart + "' nicht vorhanden. Zeile" + i
									+ new String(CRLFAscii);
							continue;
						}

						Integer pruefartIId = getStuecklisteFac().pruefartFindByCNr(pruefart, theClientDto).getIId();

						Integer pruefkombinationIId = getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(
								null, pruefartIId, artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2,
								verschleissteilIId, null, false, theClientDto);

						PruefkombinationDto pkDto = null;
						if (pruefkombinationIId == null) {
							// Neu anlegen
							pkDto = new PruefkombinationDto();
							pkDto.setPruefartIId(pruefartIId);
							pkDto.setVerschleissteilIId(verschleissteilIId);
							pkDto.setArtikelIIdKontakt(artikelIIdKontakt);
							pkDto.setArtikelIIdLitze(artikelIIdLitze);
							pkDto.setArtikelIIdLitze2(artikelIIdLitze2);
							pkDto.setBStandard(Helper.boolean2Short(false));
							pkDto.setBDoppelanschlag(Helper.boolean2Short(false));

							if (artikelIIdLitze2 != null) {
								pkDto.setBDoppelanschlag(Helper.boolean2Short(true));
							}
						} else {
							pkDto = getStuecklisteFac().pruefkombinationFindByPrimaryKey(pruefkombinationIId,
									theClientDto);
						}

						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {

							// Crimphoehe
							BigDecimal crimpHoeheDraht = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_DRAHT, i);

							// Crimpbreite
							BigDecimal crimpBreiteDraht = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_DRAHT, i);

							if (crimpHoeheDraht == null || crimpBreiteDraht == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' muessen die Felder '"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_DRAHT + "/"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_DRAHT + "' befuellt sein. Zeile"
										+ i + new String(CRLFAscii);

								continue;
							}

							pkDto.setNCrimphoeheDraht(crimpHoeheDraht);
							pkDto.setNCrimpbreitDraht(crimpBreiteDraht);

							// Ab hier nicht Pflicht
							pkDto.setNAbzugskraftLitze(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ABZUGSKRAFT_LITZE, i));
							pkDto.setNAbzugskraftLitze2(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_ABZUGSKRAFT_LITZE2, i));
							pkDto.setNToleranzCrimpbreitDraht(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPBREITE_DRAHT, i));
							pkDto.setNToleranzCrimpbreiteIsolation(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPBREITE_ISOLATION, i));
							pkDto.setNToleranzCrimphoeheDraht(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPHOEHE_DRAHT, i));
							pkDto.setNToleranzCrimphoeheIsolation(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_CRIMPHOEHE_ISOLATION, i));

						}

						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
							// CrimphoeheISO
							BigDecimal crimpHoeheISO = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_ISOLATION, i);
							// CrimpbreiteISO
							BigDecimal crimpBreiteISO = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_ISOLATION, i);

							if (crimpHoeheISO == null || crimpBreiteISO == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit ISO' muessen die Felder '"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPHOEHE_ISOLATION + "/"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_CRIMPBREITE_ISOLATION
										+ "' befuellt sein. Zeile" + i + new String(CRLFAscii);

								continue;
							}

							pkDto.setNCrimphoeheIsolation(crimpHoeheISO);
							pkDto.setNCrimpbreiteIsolation(crimpBreiteISO);

						}

						// Werte
						if (pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
							BigDecimal wert = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_WERT, i);

							if (wert == null) {
								rueckgabe += "Bei der Pruefart 'Kraftmessung' muss das Feld '"
										+ XLS_PRUEFKOMBINATIONIMPORT_SPALTE_WERT + "' befuellt sein. Zeile" + i
										+ new String(CRLFAscii);

								continue;
							}
							pkDto.setNWert(wert);

						}

						if (pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

							pkDto.setNToleranzWert(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFKOMBINATIONIMPORT_SPALTE_TOLERANZ_WERT, i));

						}

						String komentar = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_PRUEFKOMBINATIONIMPORT_SPALTE_KOMMENTAR, 3000, i);
						if (komentar != null && komentar.length() > 0) {
							if (pkDto.getPruefkombinationsprDto() == null) {
								pkDto.setPruefkombinationsprDto(new PruefkombinationsprDto());
							}

							pkDto.getPruefkombinationsprDto().setCBez(komentar);

						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler) {
							if (pruefkombinationIId == null) {
								getStuecklisteFac().createPruefkombination(pkDto, theClientDto);
							} else {
								getStuecklisteFac().updatePruefkombination(pkDto, theClientDto);
							}

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;

	}

	private Integer findStuecklistePosition(Integer stuecklisteIId, Integer artikelIId, String cPosition) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT sp FROM FLRStuecklisteposition sp WHERE sp.stueckliste_i_id=" + stuecklisteIId
				+ " AND sp.flrartikel.i_id=" + artikelIId + " AND sp.c_position='" + cPosition + "'";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();

		if (results.size() > 0) {
			FLRStuecklisteposition sp = (FLRStuecklisteposition) results.iterator().next();
			return sp.getI_id();
		} else {
			session.close();
			return null;
		}

	}

	public boolean wirdKundeVonProFirstIgnoriert(String kbez) {
		Query artikelQuery = em.createNamedQuery("ProfirstignorefindByCKbez");
		artikelQuery.setParameter(1, kbez);
		if (artikelQuery.getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void removeProFirstIgnore(String kbez) {
		Query artikelQuery = em.createNamedQuery("ProfirstignorefindByCKbez");
		artikelQuery.setParameter(1, kbez);
		if (artikelQuery.getResultList().size() > 0) {
			Profirstignore pi = (Profirstignore) artikelQuery.getResultList().iterator().next();
			em.remove(pi);
		}

	}

	public void ignoriereKundeBeiProfirstImport(String kbez) {

		Profirstignore bean = em.find(Profirstignore.class, kbez);
		if (bean == null) {
			bean = new Profirstignore(kbez);
			em.merge(bean);
			em.flush();
		}

	}

	public ArrayList<String> getAllProFirstIgnore() {
		ArrayList<String> al = new ArrayList();
		Query query = em.createNamedQuery("ProfirstignorefindAll");
		Collection c = query.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Profirstignore pi = (Profirstignore) it.next();
			al.add(pi.getCKbez());
		}
		return al;
	}

	private Integer proFirstRohmaterialArtikelFindenBzwNeuAnlegen(String artikelnummerRohmaterial,
			String bezRohmaterial, String farbe, String material, Double dicke, Double gewicht,
			TheClientDto theClientDto) {
		Integer artikelIId = null;

		try {
			Query artikelQuery = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
			artikelQuery.setParameter(1, artikelnummerRohmaterial);
			artikelQuery.setParameter(2, theClientDto.getMandant());
			Artikel artikel = (Artikel) artikelQuery.getSingleResult();

			artikelIId = artikel.getIId();

		} catch (NoResultException e) {

		}

		if (artikelIId == null) {
			// Neu anlegen
			ArtikelDto artikelDto = new ArtikelDto();

			artikelDto.setCNr(artikelnummerRohmaterial);

			artikelDto.setBVersteckt(Helper.boolean2Short(false));

			artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);

			ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();

			if (bezRohmaterial != null) {
				oArtikelsprDto.setCBez(bezRohmaterial);
			}

			artikelDto.setArtikelsprDto(oArtikelsprDto);

			artikelDto.setEinheitCNr(SystemFac.EINHEIT_QUADRATMETER);

			artikelDto.setVerpackungDto(new VerpackungDto());
			artikelDto.getVerpackungDto().setCBauform(farbe);

			artikelDto.setGeometrieDto(new GeometrieDto());
			artikelDto.getGeometrieDto().setFHoehe(dicke);

			artikelDto.setFGewichtkg(gewicht);

			// Material

			try {
				if (material != null) {
					Integer materialIId = null;
					try {
						// duplicateunique: Pruefung: Artikelklasse
						// bereits
						// vorhanden.
						Query query = em.createNamedQuery("MaterialfindByCNr");
						query.setParameter(1, material);

						Material materialBean = (Material) query.getSingleResult();
						materialIId = materialBean.getIId();
					} catch (NoResultException ex) {

						// Neu anlegen
						MaterialDto materialDto = new MaterialDto();
						materialDto.setCNr(material);
						materialIId = getMaterialFac().createMaterial(materialDto, theClientDto);

					}

					artikelDto.setMaterialIId(materialIId);

				}

				artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		return artikelIId;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbezI, TheClientDto theClientDto) {
		return pruefeUndImportiereProFirst(stuecklisteIId, kundeKbezI, false, theClientDto);
	}

	private String toUpperOhneUmlaut(String value) {
		return value.toUpperCase().replaceAll("\u00c4", "AE").replaceAll("\u00d6", "OE").replaceAll("\u00dc", "UE")
				.replaceAll("\u00df", "SS").replaceAll("'", "");
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbezI, boolean bNurVKPreisUpdaten,
			TheClientDto theClientDto) {

		String errors = "";

		java.sql.Connection sqlcon = null;
		Statement statement = null;
		ResultSet rs = null;
		String bildPfad = null;

		try {

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_ARTIKELNUMMER);
			int iMinLaenge = (Integer) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBURL);

			String dbUrl = (String) parameter.getCWertAsObject();
			MontageartDto[] dtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBUSER);

			String dbUser = (String) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBPASSWORD);

			String dbPassword = (String) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_LEAD_IN_AZ_ARTIKEL);

			String leadInAZArtikel = (String) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_PRO_FIRST_ANZAHL_ZU_IMPORTIERENDE_DATENSAETZE);

			Integer iAnzahl = (Integer) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_BILD_PFAD);

			bildPfad = (String) parameter.getCWertAsObject();

			Integer artgruIId = getProFirstArtikelgruppe(theClientDto);

			// lt. WH: Wenn kein Bild-Pfad, dann Fehler:
			if (bildPfad != null && bildPfad.trim().length() == 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_BILD_PFAD_NICHT_DEFINIERT,
						new Exception("FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_BILD_PFAD_NICHT_DEFINIERT"));
			}

			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			sqlcon = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPassword);

			statement = sqlcon.createStatement();

			Integer artikelkommentarart_i_id = getStuecklisteimportFac().kommentarartProFirstAnlegen(theClientDto);

			// QUERY

			String sQuery = "select PART.IDPART as StklFremdsystemnummer, replace(upper(substring(PART.NAME,1,20)),' ','') as StklArtikelnummer,PART.NAME as StklArtikelnummerOriginal,  PART.NAME as StklBezeichnung, PART.DESCRIPTION as StklZusatzbezeichnung, "
					+ "PART.PFILE as Kommentardatei, PART.PARTDATE as Anlagedatum, "
					+ "THICKNESS.THICKNESS as RohmaterialDickeHoehe, replace(right(('00000' + replace(cast(THICKNESS.THICKNESS as char(5)),' ','') ),5),' ','')+'_'+MATERIAL_TYPE.CODE as RohmaterialArtikelnummer, "
					+ "MATERIAL.TYPE+' '+MATERIAL_TYPE.CODE as Rohmaterialbezeichnung, MATERIAL_TYPE.CODE as Material, "
					+ "PART.WEIGHT as StklGewicht, PART.SURFACE as Flaecheinm2, PART.CUTTINGLENGTH as Schneidlaenge, "
					+ "PART.OUTERRECTANGLEX as StklArtBreiteinmm, PART.OUTERRECTANGLEY as StklArtTiefeinmm, "
					+ "PART.COLOR as Farbe, upper(PRULE.RULECODE) as Taetigkeit, PART.MANUFACTURINGTIME as Stueckzeitinsekunden, MACHINE.NAME as Maschine, "
					+ "LTRIM(RTRIM(CUSTOMERS.CUSTOMER))  as KundeKurzbezeichnung, CUSTOMERS.IDCUSTOMER as KundeFremdsystemnummer, PART_PICTURE.PICTURE as StklBild, "
					+ " PART_FIELDS.FIELD1 AS NettoVK , PART_FIELDS.FIELD2 AS Kundenartikelnummer , PART_FIELDS.FIELD3 AS Referenznummer, PART_FIELDS.FIELD5 AS Zusatzbezeichnung2 , PART_FIELDS.FIELD4 AS Anarbeitungsmenge , PART_FIELDS.FIELD6 AS Bauform , PART_FIELDS.FIELD7 AS Verpackungsart "
					+ " from PART  " + "inner join CUSTOMERS on PART.IDCUSTOMER = CUSTOMERS.IDCUSTOMER "
					+ "inner join MATERIAL on PART.IDMATERIAL = MATERIAL.IDMATERIAL "
					+ "inner join MATERIAL_TYPE on PART.IDMATERIALTYPE = MATERIAL_TYPE.IDMATERIALTYPE "
					+ "inner join THICKNESS on PART.IDTHICKNESS = THICKNESS.IDTHICKNESS "
					+ "left outer join PRULE on PART.IDRULE = PRULE.IDRULE "
					+ "left outer join MACHINE on PART.IDMACHINE = MACHINE.IDMACHINE "
					+ "inner join PART_PICTURE on PART.IDPART = PART_PICTURE.IDPART "
					+ "left outer join PART_FIELDS on PART.IDPART =PART_FIELDS.IDPART "
					+ " where  len( replace(upper(substring(PART.NAME,1,20)),' ',''))>=" + iMinLaenge
					+ " AND (PART.IDRULE = -1 OR PART.IDRULE IS NULL OR  ( PRULE.LANGUAGECODE='d' and PRULE.IDMACHINE=PART.IDMACHINE  and  PRULE.IDMATERIAL=PART.IDMATERIAL)) ";

			if (kundeKbezI != null) {
				sQuery += " AND LTRIM(RTRIM(CUSTOMERS.CUSTOMER))='" + kundeKbezI + "'";
			} else {

				ArrayList al = getStuecklisteimportFac().getAllProFirstIgnore();
				if (al.size() > 0) {
					String in = "(";
					Iterator it = al.iterator();

					while (it.hasNext()) {

						in += "'" + it.next() + "'";

						if (it.hasNext()) {
							in += ",";
						}
					}

					in += ")";
					sQuery += " AND LTRIM(RTRIM(CUSTOMERS.CUSTOMER)) NOT IN " + in;

				}
			}
			// Hoechste Fremdsystemnummer von uns holen
			StuecklisteDto stklDtoZuAktualisieren = null;
			if (stuecklisteIId == null) {

				Session session = FLRSessionFactory.getFactory().openSession();

				String sQueryMAXFremdsystemnr = "SELECT s FROM FLRStueckliste s WHERE s.mandant_c_nr='"
						+ theClientDto.getMandant()
						+ "' AND s.c_fremdsystemnr IS NOT NULL ORDER BY cast( s.c_fremdsystemnr as integer) DESC";

				org.hibernate.Query query = session.createQuery(sQueryMAXFremdsystemnr);
				query.setMaxResults(1);
				List<?> results = query.list();

				if (results.size() > 0) {
					FLRStueckliste stkl = (FLRStueckliste) results.iterator().next();
					if (stkl.getC_fremdsystemnr() != null) {

						if (kundeKbezI == null) {
							sQuery += " AND PART.IDPART >'" + stkl.getC_fremdsystemnr() + "'";
						} else {
							// Beim Kundenimport duerfen nur bestehende
							// aktualisiert werden, da ich mir ansonsten die
							// hoechste C_FREMDSYSTEMNR zerstoere
							sQuery += " AND PART.IDPART <='" + stkl.getC_fremdsystemnr() + "'";
						}

					}

				}
				session.close();

			} else if (stuecklisteIId != null) {

				stklDtoZuAktualisieren = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
				if (stklDtoZuAktualisieren.getCFremdsystemnr() != null) {
					sQuery += " AND PART.IDPART ='" + stklDtoZuAktualisieren.getCFremdsystemnr() + "'";
				} else {
					return "Es konnte kein Artikel mit der Fremdsystemnummer "
							+ stklDtoZuAktualisieren.getCFremdsystemnr() + " gefunden werden.";
				}

			}

			sQuery += " ORDER BY PART.IDPART ASC";

			if (kundeKbezI == null) {
				statement.setMaxRows(iAnzahl);
			}

			statement.execute(sQuery);

			rs = statement.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {

				String stklFremdsystemnummer = rs.getString("StklFremdsystemnummer");

				String stklArtikelnummer = toUpperOhneUmlaut(rs.getString("StklArtikelnummer"));
				String stklArtikelnummerOriginal = rs.getString("StklArtikelnummerOriginal");

				String kundeKbez = rs.getString("KundeKurzbezeichnung");
				String kundeFremdsystemnummer = rs.getString("KundeFremdsystemnummer");

				String stklZusatzbezeichnung = rs.getString("StklZusatzbezeichnung");

				// ArtikelSTKL

				String artikelnummerRohmaterial = toUpperOhneUmlaut(rs.getString("RohmaterialArtikelnummer"));

				String bezRohmaterial = rs.getString("Rohmaterialbezeichnung");

				String farbe = rs.getString("Farbe");

				String material = rs.getString("Material");

				Double dicke = rs.getDouble("RohmaterialDickeHoehe");

				Double gewicht = rs.getDouble("StklGewicht");

				String schneidlaenge = rs.getString("Schneidlaenge");

				Float breite = rs.getFloat("StklArtBreiteinmm");
				Float tiefe = rs.getFloat("StklArtTiefeinmm");

				String artikelnummerAZArtikel = rs.getString("Taetigkeit");

				if (leadInAZArtikel != null && artikelnummerAZArtikel != null) {
					artikelnummerAZArtikel = leadInAZArtikel + artikelnummerAZArtikel;
				}

				String maschineNr = rs.getString("Maschine");
				Double stueckzeit = rs.getDouble("Stueckzeitinsekunden");

				// Zusatzfelder lt. PJ 19725

				String verpackungsart = rs.getString("Verpackungsart");
				String nettovkpreis = rs.getString("NettoVK");
				String referenznummer = rs.getString("Referenznummer");
				String kundenartikelnummer = rs.getString("Kundenartikelnummer");
				String anarbeitungsmenge = rs.getString("Anarbeitungsmenge");

				String bauform = rs.getString("Bauform");
				String zusatzbezeichnung2 = rs.getString("Zusatzbezeichnung2");

				// Bilder kommen als JPEG
				byte[] image = null;
				if (bildPfad != null && bildPfad.trim().length() > 0) {

					File f = new File(bildPfad + "/" + stklArtikelnummerOriginal + ".JPG");
					if (f.exists()) {
						try {

							BufferedImage img = ImageIO.read(f);
							image = Helper.imageToByteArray(img);

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (kundeKbezI != null) {
					stklDtoZuAktualisieren = getStuecklisteFac().stuecklistefindByCFremdsystemnrMandantCNr(
							stklFremdsystemnummer, theClientDto.getMandant());
				}

				getStuecklisteimportFac().proFirstImportZeilenweise(stklDtoZuAktualisieren, theClientDto,
						dtos[0].getIId(), stklFremdsystemnummer, stklArtikelnummer, kundeKbez, kundeFremdsystemnummer,
						artikelnummerRohmaterial, bezRohmaterial, null, farbe, material, dicke, gewicht, schneidlaenge,
						breite, tiefe, artikelnummerAZArtikel, maschineNr, stueckzeit, null, artikelkommentarart_i_id,
						verpackungsart, stklZusatzbezeichnung, zusatzbezeichnung2, bauform, nettovkpreis,
						referenznummer, kundenartikelnummer, anarbeitungsmenge, image, artgruIId,
						stklArtikelnummerOriginal, bNurVKPreisUpdaten);

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (SQLException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG, ex);
		} catch (ClassNotFoundException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG, ex);
		}
		return errors;

	}

	public void proFirstImportZeilenweise(StuecklisteDto stklDtoVorhanden, TheClientDto theClientDto,
			Integer montageartIId, String stklFremdsystemnummer, String stklArtikelnummer, String kundeKbez,
			String kundeFremdsystemnummer, String artikelnummerRohmaterial, String bezRohmaterial,
			String kommentardatei, String farbe, String material, Double dicke, Double gewicht, String schneidlaenge,
			Float breite, Float tiefe, String artikelnummerAZArtikel, String maschineNr, Double stueckzeit,
			String parameterKommentarPfad, Integer artikelkommentarart, String verpackungsart,
			String stklZusatzbezeichnung, String zusatzbezeichnung2, String bauform, String nettovkpreis,
			String referenznummer, String kundenartikelnummer, String anarbeitungsmenge, byte[] image,
			Integer artgruIId, String stklArtikelnummerOriginal, boolean bNurVKPreisUpdaten) throws RemoteException {

		// PJ20046
		if (bNurVKPreisUpdaten) {
			if (stklDtoVorhanden != null && stklDtoVorhanden.getArtikelIId() != null) {
				vkpreisUpdaten(theClientDto, nettovkpreis, stklDtoVorhanden.getArtikelIId());
			}
			return;
		}

		// PJ19936
		String artikelbezeichnung = material + "_" + stklArtikelnummerOriginal;

		/*
		 * String zusatzbezeichnung = null; if (bezRohmaterial != null && dicke != null)
		 * { zusatzbezeichnung = bezRohmaterial + " " + Helper.formatZahl(dicke, 2,
		 * theClientDto.getLocUi()) + "mm"; }
		 */

		Integer artikelIIdNeu = null;
		if (stklDtoVorhanden == null) {

			ArtikelDto aDto = null;

			int lfdNr = 1;
			String artnr_Neu = stklArtikelnummer;

			while (lfdNr < 99) {

				aDto = getArtikelFac().artikelFindByCNrOhneExc(artnr_Neu, theClientDto);

				if (aDto != null) {

					artnr_Neu = stklArtikelnummer + "_" + Helper.fitString2LengthAlignRight(lfdNr + "", 2, '0');

				} else {
					break;
				}
				lfdNr++;
			}

			stklArtikelnummer = artnr_Neu;
			ArtikelDto artikelNeuDto = new ArtikelDto();
			artikelNeuDto.setCNr(stklArtikelnummer);
			artikelNeuDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
			artikelNeuDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
			artikelNeuDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
			artikelNeuDto.setArtikelsprDto(new ArtikelsprDto());
			artikelNeuDto.getArtikelsprDto().setCBez(stklArtikelnummerOriginal);

			artikelNeuDto = befuelleArtikelDto(theClientDto, artikelbezeichnung, dicke, breite, tiefe, gewicht,
					stklZusatzbezeichnung, zusatzbezeichnung2, verpackungsart, bauform, referenznummer,
					kundenartikelnummer, artgruIId, artikelNeuDto);

			artikelNeuDto.setBVersteckt(Helper.boolean2Short(false));
			artikelIIdNeu = getArtikelFac().createArtikel(artikelNeuDto, theClientDto);
		} else {

			ArtikelDto aDtoVorhanden = getArtikelFac().artikelFindByPrimaryKey(stklDtoVorhanden.getArtikelIId(),
					theClientDto);

			// Wenn die 20Stellige Artikelnummer gleich ist, dann veraendern wir
			// die Artikelnummer nicht

			String artiklenummerVorhanden = aDtoVorhanden.getCNr();
			if (artiklenummerVorhanden.length() >= 20) {
				artiklenummerVorhanden = artiklenummerVorhanden.substring(0, 20);
			}

			if (artiklenummerVorhanden.equals(stklArtikelnummer)) {

			} else {

				ArtikelDto aDto = null;

				int lfdNr = 1;
				String artnr_Neu = stklArtikelnummer;

				while (lfdNr < 99) {

					aDto = getArtikelFac().artikelFindByCNrOhneExc(artnr_Neu, theClientDto);

					if (aDto != null && !aDto.getIId().equals(aDtoVorhanden.getIId())) {

						artnr_Neu = stklArtikelnummer + "_" + Helper.fitString2LengthAlignRight(lfdNr + "", 2, '0');

					} else {
						break;
					}
					lfdNr++;
				}
				aDtoVorhanden.setCNr(artnr_Neu);

			}

			aDtoVorhanden = befuelleArtikelDto(theClientDto, artikelbezeichnung, dicke, breite, tiefe, gewicht,
					stklZusatzbezeichnung, zusatzbezeichnung2, verpackungsart, bauform, referenznummer,
					kundenartikelnummer, artgruIId, aDtoVorhanden);

			getArtikelFac().updateArtikel(aDtoVorhanden, theClientDto);

			artikelIIdNeu = stklDtoVorhanden.getArtikelIId();
		}

		// VK-Basis anlegen

		vkpreisUpdaten(theClientDto, nettovkpreis, artikelIIdNeu);

		// Kommentarfile

		if (kommentardatei != null && kommentardatei.length() > 3) {

			String pfad = parameterKommentarPfad + kommentardatei.substring(3).replace("\\", "/");
			File f = new File(pfad);

			byte[] fileAlsByteArray = null;

			if (f.exists()) {
				try {
					ByteArrayOutputStream ous = null;
					InputStream ios = null;
					try {
						byte[] buffer = new byte[4096];

						ous = new ByteArrayOutputStream();
						ios = new FileInputStream(f.getAbsolutePath());
						int read = 0;
						while ((read = ios.read(buffer)) != -1) {
							ous.write(buffer, 0, read);
						}
					} finally {
						try {
							if (ous != null)
								ous.close();
						} catch (IOException ex) {
						}

						try {
							if (ios != null)
								ios.close();
						} catch (IOException ex) {
						}
					}

					fileAlsByteArray = ous.toByteArray();

					ArtikelkommentarDto kommDto = new ArtikelkommentarDto();
					kommDto.setArtikelIId(artikelIIdNeu);
					kommDto.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_ANHANG);
					kommDto.setArtikelkommentarartIId(artikelkommentarart);
					kommDto.setBDefaultbild(Helper.boolean2Short(false));
					kommDto.setBDateiverweis(Helper.boolean2Short(false));

					kommDto.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT);

					ArtikelkommentarsprDto sprDto = new ArtikelkommentarsprDto();
					sprDto.setCDateiname(kommentardatei);
					sprDto.setOMedia(fileAlsByteArray);

					kommDto.setArtikelkommentarsprDto(sprDto);

					Query artikelkommenentarVorhanden = em
							.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
					artikelkommenentarVorhanden.setParameter(1, artikelIIdNeu);
					artikelkommenentarVorhanden.setParameter(2, artikelkommentarart);
					artikelkommenentarVorhanden.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
					Collection c = artikelkommenentarVorhanden.getResultList();
					if (c.size() == 0) {

						getArtikelkommentarFac().createArtikelkommentar(kommDto, theClientDto);
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				ArtikelkommentarDto kommDto = new ArtikelkommentarDto();
				kommDto.setArtikelIId(artikelIIdNeu);
				kommDto.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_ANHANG);
				kommDto.setArtikelkommentarartIId(artikelkommentarart);
				kommDto.setBDefaultbild(Helper.boolean2Short(false));

				kommDto.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);

				ArtikelkommentarsprDto sprDto = new ArtikelkommentarsprDto();
				sprDto.setXKommentar("Kommentarfile " + pfad + " konnte nicht importiert werden");

				kommDto.setArtikelkommentarsprDto(sprDto);

				Query artikelkommenentarVorhanden = em
						.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
				artikelkommenentarVorhanden.setParameter(1, artikelIIdNeu);
				artikelkommenentarVorhanden.setParameter(2, artikelkommentarart);
				artikelkommenentarVorhanden.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
				Collection c = artikelkommenentarVorhanden.getResultList();
				if (c.size() == 0) {

					getArtikelkommentarFac().createArtikelkommentar(kommDto, theClientDto);
				}
			}

		}

		if (image != null) {

			ArtikelkommentarDto kommDto = new ArtikelkommentarDto();
			kommDto.setArtikelIId(artikelIIdNeu);
			kommDto.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_ANHANG);
			kommDto.setArtikelkommentarartIId(artikelkommentarart);
			kommDto.setBDefaultbild(Helper.boolean2Short(false));

			kommDto.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);

			ArtikelkommentarsprDto sprDto = new ArtikelkommentarsprDto();

			sprDto.setOMedia(image);

			kommDto.setArtikelkommentarsprDto(sprDto);

			try {
				Query artikelkommenentarVorhanden = em
						.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
				artikelkommenentarVorhanden.setParameter(1, artikelIIdNeu);
				artikelkommenentarVorhanden.setParameter(2, artikelkommentarart);
				artikelkommenentarVorhanden.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
				Artikelkommentar vorhanden = (Artikelkommentar) artikelkommenentarVorhanden.getSingleResult();
				if (vorhanden != null) {

					ArtikelkommentarDto dtoVorhanden = getArtikelkommentarFac()
							.artikelkommentarFindByPrimaryKey(vorhanden.getIId(), theClientDto);
					dtoVorhanden.getArtikelkommentarsprDto().setOMedia(image);

					getArtikelkommentarFac().updateArtikelkommentar(dtoVorhanden, theClientDto);

				}
			} catch (NoResultException ex) {
				ArtikelkommentardruckDto[] druckDto = new ArtikelkommentardruckDto[0];

				kommDto.setArtikelkommentardruckDto(druckDto);
				try {
					getArtikelkommentarFac().createArtikelkommentar(kommDto, theClientDto);
				} catch (RemoteException ex3) {

					throwEJBExceptionLPRespectOld(ex3);
				}
			}
		}

		StuecklisteDto stuecklisteDto = null;
		ArtikelDto artikelDtoFremdfertigungsartikel = null;

		if (stklDtoVorhanden == null) {
			stuecklisteDto = new StuecklisteDto();
			stuecklisteDto.setArtikelIId(artikelIIdNeu);

			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
						ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

				stuecklisteDto
						.setBAusgabeunterstueckliste(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

				parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_STUECKLISTE,
						ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

				stuecklisteDto.setBMaterialbuchungbeiablieferung(
						Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

				FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
						.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);

				if (fertigungsgruppeDtos.length > 0) {
					stuecklisteDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
				}
				stuecklisteDto.setLagerIIdZiellager(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
				stuecklisteDto.setNLosgroesse(new BigDecimal(1));
				stuecklisteDto.setNErfassungsfaktor(new BigDecimal(1));

				stuecklisteDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));

			stuecklisteDto.setCFremdsystemnr(stklFremdsystemnummer);

		} else {
			stuecklisteDto = stklDtoVorhanden;
		}

		// Kunde

		// Zuerst nach Fremdsystemnummer suchen

		Query queryKDFremd = em.createNamedQuery("KundefindBycFremdsystemnrcNrMandant");
		queryKDFremd.setParameter(1, kundeFremdsystemnummer);
		queryKDFremd.setParameter(2, theClientDto.getMandant());

		Collection c = queryKDFremd.getResultList();

		if (c.size() > 0) {
			Kunde kunde = (Kunde) c.iterator().next();

			stuecklisteDto.setPartnerIId(kunde.getPartnerIId());
		} else {

			// FEHLER-KEIN-KUNDE

			if (kundeKbez != null) {
				kundeKbez = kundeKbez.trim();
			}

			List<Partner> partners = PartnerQuery.listByKbez(em, kundeKbez);

			PartnerDto pDto = null;
			if (partners.size() == 0) {

				ArrayList alInfo = new ArrayList();
				alInfo.add(kundeKbez);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN, alInfo,
						new Exception("FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN"));
			} else {

				KundeDto kundeDto = null;
				for (int i = 0; i < partners.size(); i++) {
					// Kunde
					KundeDto kundeDtoTemp = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							partners.get(i).getIId(), theClientDto.getMandant(), theClientDto);

					if (kundeDtoTemp == null) {

					} else {

						kundeDtoTemp = getKundeFac().kundeFindByPrimaryKey(kundeDtoTemp.getIId(), theClientDto);

						kundeDtoTemp.setCFremdsystemnr(kundeFremdsystemnummer);

						// SP5031
						kundeDtoTemp.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

						getKundeFac().updateKunde(kundeDtoTemp, theClientDto);

						kundeDto = kundeDtoTemp;
					}

				}

				if (kundeDto == null) {
					ArrayList alInfo = new ArrayList();
					alInfo.add(kundeKbez);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN, alInfo,
							new Exception("FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN"));
				}

				pDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
			}

			stuecklisteDto.setPartnerIId(pDto.getIId());

		}

		Integer stuecklisteIIdNeu = null;
		if (stuecklisteDto.getIId() == null) {
			stuecklisteIIdNeu = getStuecklisteFac().createStueckliste(stuecklisteDto, theClientDto);
		} else {

			// Bei vorhandener Stueckliste zuerst Material und arbeitsplan
			// loeschen

			StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteDto.getIId(), theClientDto);
			for (int j = 0; j < apDtos.length; j++) {
				getStuecklisteFac().removeStuecklistearbeitsplan(apDtos[j], theClientDto);
			}

			StuecklistepositionDto[] posDtos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteDto.getIId(), theClientDto);
			for (int j = 0; j < posDtos.length; j++) {
				getStuecklisteFac().removeStuecklisteposition(posDtos[j], theClientDto);
			}
			stuecklisteIIdNeu = stuecklisteDto.getIId();
			getStuecklisteFac().updateStueckliste(stuecklisteDto, theClientDto);
		}

		// Materialposition

		// Position
		// Artikel des Rohmaterials suchen bzw neu anlegen

		StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
		stuecklistepositionDto.setStuecklisteIId(stuecklisteIIdNeu);

		stuecklistepositionDto.setArtikelIId(proFirstRohmaterialArtikelFindenBzwNeuAnlegen(artikelnummerRohmaterial,
				bezRohmaterial, farbe, material, dicke, gewicht, theClientDto));
		stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);

		stuecklistepositionDto.setEinheitCNr(SystemFac.EINHEIT_QUADRATMILLIMETER);

		stuecklistepositionDto.setCKommentar("Schneidlaenge: " + schneidlaenge);

		// /mm2 in Pos

		stuecklistepositionDto.setFDimension1(breite);

		stuecklistepositionDto.setFDimension2(tiefe);

		stuecklistepositionDto.setNMenge(new BigDecimal(1));
		stuecklistepositionDto.setBMitdrucken(Helper.boolean2Short(false));
		stuecklistepositionDto.setMontageartIId(montageartIId);

		Integer positionIId = getStuecklisteFac().createStuecklisteposition(stuecklistepositionDto, theClientDto);

		// Anarbeitungsartikel in Postitionen einfuegen

		// Arbeitsplan-Position
		ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
				ParameterFac.PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL);
		String fremdfertigungsartikel = parameter.getCWert();

		if (fremdfertigungsartikel == null || fremdfertigungsartikel.trim().length() == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT,
					new Exception("FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT"));

		} else {
			ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(fremdfertigungsartikel,
					theClientDto.getMandant());

			if (aDto == null) {

				ArrayList al = new ArrayList();
				al.add(fremdfertigungsartikel);

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT, al,
						new Exception("FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT"));
			} else {
				artikelDtoFremdfertigungsartikel = aDto;
			}

		}

		// PJ
		if (artikelDtoFremdfertigungsartikel != null && anarbeitungsmenge != null && anarbeitungsmenge.length() > 0) {

			try {
				BigDecimal bd = new BigDecimal(anarbeitungsmenge.replaceAll("\\.", "").replaceAll(",", "\\."));

				StuecklistepositionDto stuecklistepositionDtoAnarbeitungsartikel = new StuecklistepositionDto();
				stuecklistepositionDtoAnarbeitungsartikel.setStuecklisteIId(stuecklisteIIdNeu);

				stuecklistepositionDtoAnarbeitungsartikel.setArtikelIId(artikelDtoFremdfertigungsartikel.getIId());
				stuecklistepositionDtoAnarbeitungsartikel.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);

				stuecklistepositionDtoAnarbeitungsartikel
						.setEinheitCNr(artikelDtoFremdfertigungsartikel.getEinheitCNr());

				stuecklistepositionDtoAnarbeitungsartikel.setNMenge(bd);
				stuecklistepositionDtoAnarbeitungsartikel.setBMitdrucken(Helper.boolean2Short(false));
				stuecklistepositionDtoAnarbeitungsartikel.setMontageartIId(montageartIId);

				getStuecklisteFac().createStuecklisteposition(stuecklistepositionDtoAnarbeitungsartikel, theClientDto);

			} catch (Exception e) {
				//
			}

		}

		// Arbeitsplan
		StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
		stuecklistearbeitsplanDto.setStuecklisteIId(stuecklisteIIdNeu);

		// AZ-Artikel muss niccht vorhanden sein -> ansonsten Default-AZ-Artikel
		// verwenden

		if (artikelnummerAZArtikel == null) {

			ArtikelDto aDto = getZeiterfassungFac().getDefaultArbeitszeitartikel(theClientDto);
			stuecklistearbeitsplanDto.setArtikelIId(aDto.getIId());
		} else {

			try {
				Query artikelQuery = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
				artikelQuery.setParameter(1, artikelnummerAZArtikel);
				artikelQuery.setParameter(2, theClientDto.getMandant());
				Artikel artikel = (Artikel) artikelQuery.getSingleResult();

				Integer artikelIIdAZ = artikel.getIId();
				stuecklistearbeitsplanDto.setArtikelIId(artikelIIdAZ);

			} catch (NoResultException e) {
				// AZARTIKEL-FEHLER

				ArrayList alInfo = new ArrayList();
				alInfo.add(artikelnummerAZArtikel);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_TAETIGKEIT_NICHT_VORHANDEN, alInfo,
						new Exception("FEHLER_PRO_FIRST_IMPORT_TAETIGKEIT_NICHT_VORHANDEN"));

			}

			// SP4695 Es muss keine Maschine definiert sein
			if (maschineNr != null && maschineNr.length() > 0) {
				Query queryMaschine = em.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
				queryMaschine.setParameter(1, theClientDto.getMandant());
				queryMaschine.setParameter(2, maschineNr);

				try {
					Maschine maschine = (Maschine) queryMaschine.getSingleResult();
					stuecklistearbeitsplanDto.setMaschineIId(maschine.getIId());

				} catch (NoResultException e) {
					// MASCHINE FEHLER
					ArrayList alInfo = new ArrayList();
					alInfo.add(maschineNr);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_MASCHINE_NICHT_VORHANDEN, alInfo,
							new Exception("FEHLER_PRO_FIRST_IMPORT_MASCHINE_NICHT_VORHANDEN"));

				}
			}
		}

		stuecklistearbeitsplanDto.setIArbeitsgang(10);

		stuecklistearbeitsplanDto.setStuecklistepositionIId(positionIId);

		stuecklistearbeitsplanDto.setAgartCNr(StuecklisteFac.AGART_LAUFZEIT);

		stuecklistearbeitsplanDto.setBNurmaschinenzeit(Helper.boolean2Short(false));

		Long lStueckzeit = new BigDecimal(stueckzeit * 1000).longValue();
		stuecklistearbeitsplanDto.setLStueckzeit(lStueckzeit);

		stuecklistearbeitsplanDto.setLRuestzeit(0L);

		getStuecklisteFac().createStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);

	}

	private void vkpreisUpdaten(TheClientDto theClientDto, String nettovkpreis, Integer artikelIIdNeu)
			throws RemoteException {
		if (nettovkpreis != null && nettovkpreis.length() > 0) {
			try {
				BigDecimal bdVKBasis = new BigDecimal(nettovkpreis.replaceAll("\\.", "").replaceAll(",", "\\."));

				java.sql.Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
				// Nun nachsehen, ob die VK-Basis schon
				// vorhanden ist

				try {
					Query query = em
							.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, artikelIIdNeu);
					query.setParameter(3, tHeute);

					VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) query
							.getSingleResult();

					// Wenn vorhanden, und ungleich dann updaten
					if (bdVKBasis.doubleValue() != vkPreisfindungEinzelverkaufspreis.getNVerkaufspreisbasis()
							.doubleValue()) {
						vkPreisfindungEinzelverkaufspreis.setNVerkaufspreisbasis(bdVKBasis);
					}
				} catch (NoResultException ex) {

					VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = new VkPreisfindungEinzelverkaufspreisDto();
					vkPreisfindungEinzelverkaufspreisDto.setTVerkaufspreisbasisgueltigab(new Date(tHeute.getTime()));
					vkPreisfindungEinzelverkaufspreisDto.setNVerkaufspreisbasis(bdVKBasis);
					vkPreisfindungEinzelverkaufspreisDto.setArtikelIId(artikelIIdNeu);
					vkPreisfindungEinzelverkaufspreisDto.setMandantCNr(theClientDto.getMandant());
					getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(vkPreisfindungEinzelverkaufspreisDto,
							theClientDto);
				}

				VkpfartikelpreislisteDto[] preislistenDtos = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByMandantCNr(theClientDto.getMandant());

				for (int i = 0; i < preislistenDtos.length; i++) {
					VkpfartikelpreislisteDto preislisteDto = preislistenDtos[i];

					Session session2 = FLRSessionFactory.getFactory().openSession();
					String queryString2 = "SELECT p FROM FLRVkpfartikelpreis p WHERE p.artikel_i_id=" + artikelIIdNeu
							+ " AND p.vkpfartikelpreisliste_i_id=" + preislisteDto.getIId();
					org.hibernate.Query q2 = session2.createQuery(queryString2);
					List<?> resultList2 = q2.list();

					if (resultList2.size() < 1) {

						BigDecimal stdRabattsatz = preislisteDto.getNStandardrabattsatz();

						if (stdRabattsatz == null) {
							stdRabattsatz = BigDecimal.ZERO;
						}

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(preislisteDto.getIId());
						vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(stdRabattsatz);
						vkPreisfindungPreislisteDto.setTPreisgueltigab(new Date(tHeute.getTime()));

						vkPreisfindungPreislisteDto.setArtikelIId(artikelIIdNeu);
						vkPreisfindungPreislisteDto.setTAendern(new Timestamp(System.currentTimeMillis()));

						try {
							getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
									theClientDto);
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}

				}

			} catch (NumberFormatException e) {
				// auslassen
			}

		}
	}

	private ArtikelDto befuelleArtikelDto(TheClientDto theClientDto, String cBezeichnung, Double dicke, Float breite,
			Float tiefe, Double gewicht, String zusatzbezeichnung, String zusatzbezeichnung2, String verpackungsart,
			String bauform, String referenznummer, String kundenartikelnummer, Integer artgruIId,
			ArtikelDto artikelNeuDto) throws RemoteException {

		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_NACHKOMMASTELLEN_DIMENSIONEN);
		Integer iNachkommastellenDimension = (Integer) parameter.getCWertAsObject();
		if (artikelNeuDto.getGeometrieDto() == null) {
			artikelNeuDto.setGeometrieDto(new GeometrieDto());
		}

		if (artikelNeuDto.getVerpackungDto() == null) {
			artikelNeuDto.setVerpackungDto(new VerpackungDto());
		}

		if (breite != null) {
			artikelNeuDto.getGeometrieDto().setFBreite(
					Helper.rundeKaufmaennisch(new BigDecimal(breite), iNachkommastellenDimension).doubleValue());
		}

		if (tiefe != null) {
			artikelNeuDto.getGeometrieDto().setFTiefe(new Double(
					Helper.rundeKaufmaennisch(new BigDecimal(tiefe), iNachkommastellenDimension).doubleValue()));
		}

		if (dicke != null) {
			artikelNeuDto.getGeometrieDto().setFHoehe(new Double(
					Helper.rundeKaufmaennisch(new BigDecimal(dicke), iNachkommastellenDimension).doubleValue()));
		}

		if (gewicht != null) {
			artikelNeuDto
					.setFGewichtkg(new Double(Helper.rundeKaufmaennisch(new BigDecimal(gewicht), 4).doubleValue()));
		}

		artikelNeuDto.setArtgruIId(artgruIId);

		int iLaenge = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());

		if (cBezeichnung != null && cBezeichnung.length() > iLaenge) {
			cBezeichnung = cBezeichnung.substring(0, iLaenge - 1);
		}

		if (zusatzbezeichnung != null && zusatzbezeichnung.length() > iLaenge) {
			zusatzbezeichnung = zusatzbezeichnung.substring(0, iLaenge - 1);
		}
		if (zusatzbezeichnung2 != null && zusatzbezeichnung2.length() > iLaenge) {
			zusatzbezeichnung2 = zusatzbezeichnung2.substring(0, iLaenge - 1);
		}

		if (verpackungsart != null && verpackungsart.length() > 20) {
			verpackungsart = verpackungsart.substring(0, 19);
		}

		if (bauform != null && bauform.length() > 20) {
			bauform = bauform.substring(0, 19);
		}

		if (referenznummer != null && referenznummer.length() > 30) {
			referenznummer = referenznummer.substring(0, 29);
		}

		if (kundenartikelnummer != null && kundenartikelnummer.length() > 30) {
			kundenartikelnummer = kundenartikelnummer.substring(0, 29);
		}

		artikelNeuDto.getVerpackungDto().setCBauform(bauform);
		artikelNeuDto.getVerpackungDto().setCVerpackungsart(verpackungsart);

		artikelNeuDto.getArtikelsprDto().setCBez(cBezeichnung);

		artikelNeuDto.getArtikelsprDto().setCZbez(zusatzbezeichnung);
		artikelNeuDto.getArtikelsprDto().setCZbez2(zusatzbezeichnung2);

		artikelNeuDto.setCReferenznr(referenznummer);
		artikelNeuDto.getArtikelsprDto().setCKbez(kundenartikelnummer);

		return artikelNeuDto;
	}

	public String pruefeUndImportierePruefplanXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto) {
		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);
			HashSet hsStuecklistenIIdPositionenBereitsGeloescht = new HashSet();
			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			/*
			 * public static String XLS_PRUEFPLANIMPORT_SPALTE_POSITION_KONTAKT =
			 * "ArtikelnummerKontakt"; public static String
			 * XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE = "ArtikelnummerLitze"; public
			 * static String XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE2 =
			 * "ArtikelnummerLitze2";
			 */

			fehlerZeileXLSImport = "";
			if (!hmVorhandeneSpalten.containsKey(XLS_PRUEFPLANIMPORT_SPALTE_PRUEFART)
					|| !hmVorhandeneSpalten.containsKey(XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_STUECKLISTE)) {
				rueckgabe += "Es muessen zumindest die Spalte 'Pruefart/ArtikelnummerStueckliste' vorhanden sein."
						+ new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {
					List<Integer> pruefartenOhneKombi = getStuecklisteFac()
							.getPruefartenOhnePruefkombination(theClientDto);

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						String pruefart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_PRUEFPLANIMPORT_SPALTE_PRUEFART, 40, i);

						if (pruefart == null || pruefart.length() == 0) {
							rueckgabe += "Die Pruefart darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						String artikelnummmerStueckliste = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_STUECKLISTE,
								ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);

						if (artikelnummmerStueckliste == null || artikelnummmerStueckliste.length() == 0) {
							rueckgabe += "Die Spalte 'ArtikelnummerStueckliste' darf nicht leer sein. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

						Integer artikelIIdKontakt = null;
						Integer artikelIIdLitze = null;
						Integer artikelIIdLitze2 = null;
						Integer stuecklistepositionIIdKontakt = null;
						Integer stuecklistepositionIIdLitze = null;
						Integer stuecklistepositionIIdLitze2 = null;
						Integer verschleissteilIId = null;

						// Stueckliste
						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummmerStueckliste,
								theClientDto);

						if (aDto == null) {
							rueckgabe += "Der Artikel mit der Nummmer '" + artikelnummmerStueckliste
									+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByArtikelIIdMandantCNrOhneExc(aDto.getIId(), theClientDto.getMandant());

						if (stklDto == null) {
							rueckgabe += "Es konnte keine Stueckliste mit der Nummmer '" + artikelnummmerStueckliste
									+ "' im angemeldeten Mandanten gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;
						} else {
							if (stklDto != null && stklDto.getTFreigabe() != null) {
								rueckgabe += "Die Stueckliste '" + aDto.getCNr()
										+ "' ist freigegeben und darf nicht mehr veraendert werden. Zeile " + i
										+ new String(CRLFAscii);
								continue;

							}
						}

						// Kontakt
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
							// Hier muss ein Kontakt vorhanden sein
							// Kontakt
							String artikelKontakt = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT,
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
							if (artikelKontakt == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO/elekrische Pruefung/optische Pruefung/Kraftmessung' oder 'Kraftmessung' muss das Feld '"
										+ XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT + "' befuellt sein. Zeile"
										+ i + new String(CRLFAscii);
								continue;
							} else {
								ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByCNrOhneExc(artikelKontakt,
										theClientDto);
								if (aDtoKontakt == null) {
									rueckgabe += "Die Artikelnummer '" + artikelKontakt + "' in der Spalte '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_KONTAKT
											+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
									continue;
								} else {
									artikelIIdKontakt = aDtoKontakt.getIId();
								}
							}

							// Position
							String positionKontakt = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFPLANIMPORT_SPALTE_POSITION_KONTAKT, 40, i);
							if (positionKontakt == null || positionKontakt.length() == 0) {
								rueckgabe += "Wenn die Spalte 'ArtikelnummerKontakt' befuellt ist, muss auch die Spalte '"
										+ XLS_PRUEFPLANIMPORT_SPALTE_POSITION_KONTAKT + "' befuellt sein. Zeile" + i
										+ new String(CRLFAscii);
								continue;
							}

							// Nun entsprechende Stkl-Position suchen
							stuecklistepositionIIdKontakt = findStuecklistePosition(stklDto.getIId(), artikelIIdKontakt,
									positionKontakt);

							if (stuecklistepositionIIdKontakt == null) {
								rueckgabe += "In der Stueckliste '" + artikelnummmerStueckliste
										+ "' konnte keine Position mit der Artikelnummer '" + artikelKontakt
										+ "' und der Positionsnummer '" + positionKontakt + "' gefunden werden. Zeile"
										+ i + new String(CRLFAscii);
								continue;
							}

						}

						// Litze
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)
								|| pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
							// Hier muss eine Litze vorhanden sein
							// Kontakt
							String artikelLitze = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE,
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
							if (artikelLitze == null) {

								if (!pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

									rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' oder 'Kraftmessung/Masspruefung' muss das Feld '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE + "' befuellt sein. Zeile"
											+ i + new String(CRLFAscii);
									continue;
								}
							} else {
								ArtikelDto aDtoLitze = getArtikelFac().artikelFindByCNrOhneExc(artikelLitze,
										theClientDto);
								if (aDtoLitze == null) {
									rueckgabe += "Die Artikelnummer '" + artikelLitze + "' in der Spalte '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE
											+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
									continue;
								} else {
									artikelIIdLitze = aDtoLitze.getIId();
								}
							}

							if (artikelIIdLitze != null) {
								// Position
								String positionLitze = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE, 40, i);
								if (positionLitze == null || positionLitze.length() == 0) {
									rueckgabe += "Wenn die Spalte 'ArtikelnummerLitze' befuellt ist, muss auch die Spalte '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE + "' befuellt sein. Zeile" + i
											+ new String(CRLFAscii);
									continue;
								}

								// Nun entsprechende Stkl-Position suchen
								stuecklistepositionIIdLitze = findStuecklistePosition(stklDto.getIId(), artikelIIdLitze,
										positionLitze);

								if (stuecklistepositionIIdLitze == null) {
									rueckgabe += "In der Stueckliste '" + artikelnummmerStueckliste
											+ "' konnte keine Position mit der Artikelnummer '" + artikelLitze
											+ "' und der Positionsnummer '" + positionLitze + "' gefunden werden. Zeile"
											+ i + new String(CRLFAscii);
									continue;
								}

							}

						}

						// Litze2
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {

							// Hier muss eine Litze vorhanden sein
							// Kontakt
							String artikelLitze2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE2,
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);
							if (artikelLitze2 == null) {
								// Dann kein Doppelanschlag
							} else {
								ArtikelDto aDtoLitze2 = getArtikelFac().artikelFindByCNrOhneExc(artikelLitze2,
										theClientDto);
								if (aDtoLitze2 == null) {
									rueckgabe += "Die Artikelnummer '" + artikelLitze2 + "' in der Spalte '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_ARTIKELNUMMER_LITZE2
											+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
									continue;
								} else {
									artikelIIdLitze2 = aDtoLitze2.getIId();
								}

								// Position
								String positionLitze2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE2, 40, i);
								if (positionLitze2 == null || positionLitze2.length() == 0) {
									rueckgabe += "Wenn die Spalte 'ArtikelnummerLitze2' befuellt ist, muss auch die Spalte '"
											+ XLS_PRUEFPLANIMPORT_SPALTE_POSITION_LITZE2 + "' befuellt sein. Zeile" + i
											+ new String(CRLFAscii);
									continue;
								}

								// Nun entsprechende Stkl-Position suchen
								stuecklistepositionIIdLitze2 = findStuecklistePosition(stklDto.getIId(),
										artikelIIdLitze2, positionLitze2);

								if (stuecklistepositionIIdLitze2 == null) {
									rueckgabe += "In der Stueckliste '" + artikelnummmerStueckliste
											+ "' konnte keine Position mit der Artikelnummer '" + artikelLitze2
											+ "' und der Positionsnummer '" + positionLitze2
											+ "' gefunden werden. Zeile" + i + new String(CRLFAscii);
									continue;
								}

							}

						}

						// Ausserdem muss ein Verschleissteil definiert sein
						if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
							String verschleissteil = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_PRUEFPLANIMPORT_SPALTE_VERSCHLEISSTEIL, 40, i);
							if (verschleissteil == null) {
								rueckgabe += "Bei der Pruefart 'Crimpen mit/ohne ISO' muss das Feld '"
										+ XLS_PRUEFPLANIMPORT_SPALTE_VERSCHLEISSTEIL + "' befuellt sein. Zeile" + i
										+ new String(CRLFAscii);
								continue;
							} else {
								try {
									Query query = em.createNamedQuery("VerschleissteilfindByCNr");
									query.setParameter(1, verschleissteil);

									Verschleissteil v = (Verschleissteil) query.getSingleResult();
									verschleissteilIId = v.getIId();
									// Dann Update

								} catch (NoResultException ex1) {
									rueckgabe += "Verschleissteil '" + verschleissteil
											+ "' konnte nicht gefunden. Zeile " + i + new String(CRLFAscii);
									continue;

								}
							}
						}
						if (!Helper.isOneOf(pruefart, StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO,
								StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO, StuecklisteFac.PRUEFART_MASSPRUEFUNG,
								StuecklisteFac.PRUEFART_KRAFTMESSUNG, StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG,
								StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG, StuecklisteFac.PRUEFART_MATERIALSTATUS)) {
							rueckgabe += "Pruefart '" + pruefart + "' nicht vorhanden. Zeile" + i
									+ new String(CRLFAscii);
							continue;
						}

						Integer pruefartIId = getStuecklisteFac().pruefartFindByCNr(pruefart, theClientDto).getIId();

						Integer pruefkombinationIId = getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(
								stklDto.getIId(), pruefartIId, artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2,
								verschleissteilIId, null, false, theClientDto);

						if (!pruefartenOhneKombi.contains(pruefartIId) && pruefkombinationIId == null) {
							rueckgabe += "Es konnte keine entsprechende Pruefkombination gefunden werden. Zeile" + i
									+ new String(CRLFAscii);
							continue;
						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler) {

							if (bVorhandenePositionenLoeschen) {
								if (!hsStuecklistenIIdPositionenBereitsGeloescht.contains(stklDto.getIId())) {

									StklpruefplanDto[] posDtos = getStuecklisteFac()
											.stklpruefplanFindByStuecklisteIId(stklDto.getIId());

									for (int j = 0; j < posDtos.length; j++) {
										getStuecklisteFac().removeStklpruefplan(posDtos[j].getIId());
									}

									hsStuecklistenIIdPositionenBereitsGeloescht.add(stklDto.getIId());
								}
							}

							StklpruefplanDto stklPruefplanDto = new StklpruefplanDto();
							stklPruefplanDto.setPruefartIId(pruefartIId);
							stklPruefplanDto.setStuecklisteId(stklDto.getIId());
							stklPruefplanDto.setVerschleissteilIId(verschleissteilIId);
							stklPruefplanDto.setBDoppelanschlag(Helper.boolean2Short(false));
							if (stklPruefplanDto.getStuecklistepositionIIdLitze2() != null) {
								stklPruefplanDto.setBDoppelanschlag(Helper.boolean2Short(true));
							}

							stklPruefplanDto.setStuecklistepositionIIdKontakt(stuecklistepositionIIdKontakt);
							stklPruefplanDto.setStuecklistepositionIIdLitze(stuecklistepositionIIdLitze);
							stklPruefplanDto.setStuecklistepositionIIdLitze2(stuecklistepositionIIdLitze2);

							getStuecklisteFac().createStklpruefplan(stklPruefplanDto, theClientDto);

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;

	}

	private ArtikelDto erzeugeArtikel(String artikelnummer, String bezeichnung, TheClientDto theClientDto)
			throws RemoteException {

		String default_artikeleinheit = null;
		Integer default_mwstsaztIId = null;

		ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
		default_artikeleinheit = parameter.getCWert();

		parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
		}

		ArtikelDto artikelDto = new ArtikelDto();

		artikelDto.setCNr(artikelnummer);

		artikelDto.setBVersteckt(Helper.boolean2Short(false));

		artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);

		ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();

		if (bezeichnung != null) {
			oArtikelsprDto.setCBez(bezeichnung);
		}

		artikelDto.setArtikelsprDto(oArtikelsprDto);

		artikelDto.setEinheitCNr(default_artikeleinheit);
		artikelDto.setMwstsatzbezIId(default_mwstsaztIId);

		Integer artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
		return getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

	}

	private StuecklisteDto erzeugeStueckliste(Integer artikelIId, TheClientDto theClientDto) throws RemoteException {
		// Stueckliste neu anlegen
		StuecklisteDto stklDto = new StuecklisteDto();
		stklDto.setArtikelIId(artikelIId);

		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

			stklDto.setBAusgabeunterstueckliste(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

			stklDto.setBMaterialbuchungbeiablieferung(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);

			if (fertigungsgruppeDtos.length > 0) {
				stklDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
			}
			stklDto.setLagerIIdZiellager(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
			stklDto.setNLosgroesse(new BigDecimal(1));
			stklDto.setNErfassungsfaktor(new BigDecimal(1));

			stklDto.setBFremdfertigung(Helper.boolean2Short(false));

			stklDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Integer stuecklisteIId = getStuecklisteFac().createStueckliste(stklDto, theClientDto);

		return getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereArbeitsplanXLS(byte[] xlsDatei, String einheitStueckRuestZeit,
			boolean bImportierenWennKeinFehler, boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);
			HashSet hsStuecklistenIIdPositionenBereitsGeloescht = new HashSet();
			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			String default_artikeleinheit = null;
			Integer default_mwstsaztIId = null;

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART);

			String default_artikelart = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

			// Artikelnummer muss immer vorhanden sein
			if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFSTUECKLISTE) == false
					|| hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELNUMMER) == false
					|| hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_AG_NUMMER) == false) {
				rueckgabe += "Es muessen zumindest die Spalten 'Kopfstueckliste/AGNummer/Artikelnummer' vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);
				fehlerZeileXLSImport = "";

				Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFSTUECKLISTE);

				if (sZeile != null && sZeile.length > 0) {

					String artikelnummer = sZeile[iSpalteArtikelnummer].getContents();

					if (artikelnummer == null || artikelnummer.length() == 0) {
						rueckgabe += "Die Artikelnummer der Kopfstueckliste darf nicht leer sein. Zeile " + i
								+ new String(CRLFAscii);
						continue;
					}

					String artikelnummerArbeitsplan = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELNUMMER, 25, i);

					if (artikelnummerArbeitsplan == null || artikelnummerArbeitsplan.length() == 0) {
						rueckgabe += "Die Artikelnummer des AZ-Artikel darf nicht leer sein. Zeile " + i
								+ new String(CRLFAscii);
						continue;
					}

					BigDecimal agNummer = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_AG_NUMMER, i);

					if (agNummer == null) {
						rueckgabe += "Die AGNummer des Arbeitsplans darf nicht leer sein. Zeile " + i
								+ new String(CRLFAscii);
						continue;
					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);

					if (artikelDto != null) {
						artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelDto.getIId(), theClientDto);
						StuecklisteDto stklDtoVorhanden = getStuecklisteFac()
								.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelDto.getIId(),
										theClientDto.getMandant());

						if (stklDtoVorhanden != null && stklDtoVorhanden.getTFreigabe() != null) {

							boolean bHatRcht = getTheJudgeFac()
									.hatRecht(RechteFac.RECHT_STK_SOLLZEITEN_FREIGEGEBENE_STK_CUD, theClientDto);

							if (!bHatRcht) {
								rueckgabe += "Die Stueckliste '" + artikelnummer
										+ "' ist freigegeben und darf nicht mehr veraendert werden. Zeile " + i
										+ new String(CRLFAscii);
								continue;
							}
						}

					} else {
						artikelDto = new ArtikelDto();
						artikelDto.setCNr(artikelnummer);

						// Bezeichnungen
						if (artikelDto.getArtikelsprDto() == null) {
							artikelDto.setArtikelsprDto(new ArtikelsprDto());
							// Spr nur bei Neuanlage Updaten
							if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFBEZEICHNUNG)) {
								artikelDto.getArtikelsprDto().setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
										XLS_ARBEITSPLANIMPORT_SPALTE_KOPFBEZEICHNUNG, 40, i));
							}

						}

						artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
						artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
						artikelDto.setEinheitCNr(default_artikeleinheit);

					}

					BigDecimal stueckzeit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_STUECKZEIT, i);

					BigDecimal ruestzeit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_RUESTZEIT, i);

					String inventarnummer_maschine = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_MASCHINENNUMMER, 40, i);
					Integer maschineIId = null;
					if (inventarnummer_maschine != null) {

						Query query = em.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
						query.setParameter(1, theClientDto.getMandant());
						query.setParameter(2, inventarnummer_maschine);
						Maschine maschine;
						try {
							maschine = (Maschine) query.getSingleResult();
							maschineIId = maschine.getIId();
						} catch (NoResultException e) {
							rueckgabe += "Ein Maschine mit der Inventarnummer '" + inventarnummer_maschine
									+ "' ist nicht vorhanden. Zeile " + i + new String(CRLFAscii);
							continue;
						}

					}

					String agart = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_ARBEITSPLANIMPORT_SPALTE_AG_ART, 15,
							i);

					if (agart != null) {
						if (agart.trim().equals(StuecklisteFac.AGART_LAUFZEIT.trim())
								|| agart.trim().equals(StuecklisteFac.AGART_UMSPANNZEIT.trim())) {
						} else {
							rueckgabe += "AG-Art muss entweder 'Laufzeit' oder 'Umspannzeit' sein. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

					}

					String kommentarKurz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_KURZ, 80, i);

					String kommentarLang = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_LANG, 3000, i);

					BigDecimal aufspannung = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_AUFSPANNUNG, i);

					BigDecimal ppmmenge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_PPMMENGE, i);

					BigDecimal mgz = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_ARBEITSPLANIMPORT_SPALTE_MGZ,
							i);

					BigDecimal agBeginn = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_AG_BEGINN, i);

					Short nurMaschinenzeit = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_ARBEITSPLANIMPORT_SPALTE_NUR_MSCHINENZEIT, i);

					if (nurMaschinenzeit == null) {
						nurMaschinenzeit = Helper.boolean2Short(false);
					}

					if (fehlerZeileXLSImport.length() > 0) {
						rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
					}

					if (bImportierenWennKeinFehler) {

						Integer artikelIId = null;

						if (artikelDto.getIId() != null) {

							artikelIId = artikelDto.getIId();

						} else {

							artikelDto.setBVersteckt(Helper.boolean2Short(false));

							artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
						}

						// Stueckliste
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId, theClientDto.getMandant());
						if (stklDto != null) {

							if (bVorhandenePositionenLoeschen) {
								if (!hsStuecklistenIIdPositionenBereitsGeloescht.contains(stklDto.getIId())) {

									StuecklistearbeitsplanDto[] posDtos = getStuecklisteFac()
											.stuecklistearbeitsplanFindByStuecklisteIId(stklDto.getIId(), theClientDto);

									for (int j = 0; j < posDtos.length; j++) {
										getStuecklisteFac().removeStuecklistearbeitsplan(posDtos[j], theClientDto);
									}

									hsStuecklistenIIdPositionenBereitsGeloescht.add(stklDto.getIId());
								}
							}

						} else {
							// Stueckliste neu anlegen
							stklDto = new StuecklisteDto();
							stklDto.setArtikelIId(artikelIId);

							try {
								parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
										ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

								stklDto.setBAusgabeunterstueckliste(
										Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

								parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
										ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

								stklDto.setBMaterialbuchungbeiablieferung(
										Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

								FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
										.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);

								if (fertigungsgruppeDtos.length > 0) {
									stklDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
								}
								stklDto.setLagerIIdZiellager(
										getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
								stklDto.setNLosgroesse(new BigDecimal(1));
								stklDto.setNErfassungsfaktor(new BigDecimal(1));

								stklDto.setBFremdfertigung(Helper.boolean2Short(false));

								stklDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							Integer stuecklisteIId = getStuecklisteFac().createStueckliste(stklDto, theClientDto);

							stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
							hsStuecklistenIIdPositionenBereitsGeloescht.add(stklDto.getIId());
						}

						// Arbeitsplan

						ArtikelDto artikelDtoArbeitsplan = getArtikelFac()
								.artikelFindByCNrOhneExc(artikelnummerArbeitsplan, theClientDto);

						Integer artikelIIdArbeitsplan = null;
						if (artikelDtoArbeitsplan != null) {
							artikelDtoArbeitsplan = getArtikelFac()
									.artikelFindByPrimaryKey(artikelDtoArbeitsplan.getIId(), theClientDto);

							artikelIIdArbeitsplan = artikelDtoArbeitsplan.getIId();

						} else {
							artikelDtoArbeitsplan = new ArtikelDto();
							artikelDtoArbeitsplan.setCNr(artikelnummerArbeitsplan);
							artikelDtoArbeitsplan.setMwstsatzbezIId(default_mwstsaztIId);
							artikelDtoArbeitsplan.setArtikelartCNr(ArtikelFac.ARTIKELART_ARBEITSZEIT);
							artikelDtoArbeitsplan.setBVersteckt(Helper.boolean2Short(false));
							artikelDtoArbeitsplan.setEinheitCNr(SystemFac.EINHEIT_STUNDE);

							if (artikelDtoArbeitsplan.getArtikelsprDto() == null) {
								artikelDtoArbeitsplan.setArtikelsprDto(new ArtikelsprDto());

								int iLaenge = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());

								artikelDtoArbeitsplan.getArtikelsprDto()
										.setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
												XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELBEZEICHNUNG, iLaenge, i));
							}

							artikelIIdArbeitsplan = getArtikelFac().createArtikel(artikelDtoArbeitsplan, theClientDto);
						}

						Query query = em
								.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
						query.setParameter(1, stklDto.getIId());
						query.setParameter(2, agNummer.intValue());
						Collection<?> cl = query.getResultList();

						// Nur importieren, wenn noch kein AG vorhanden
						if (cl.size() == 0) {
							// Arbeitsplan
							StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
							stuecklistearbeitsplanDto.setStuecklisteIId(stklDto.getIId());
							stuecklistearbeitsplanDto.setArtikelIId(artikelIIdArbeitsplan);
							stuecklistearbeitsplanDto.setIArbeitsgang(agNummer.intValue());
							if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_AG_ART)) {

								stuecklistearbeitsplanDto.setAgartCNr(agart);
							}
							stuecklistearbeitsplanDto.setBNurmaschinenzeit(nurMaschinenzeit);

							if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_MASCHINENNUMMER)) {

								stuecklistearbeitsplanDto.setMaschineIId(maschineIId);
							}

							if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_KURZ)) {
								stuecklistearbeitsplanDto.setCKommentar(kommentarKurz);
							}
							if (hmVorhandeneSpalten.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_LANG)) {
								stuecklistearbeitsplanDto.setXLangtext(kommentarLang);
							}

							if (aufspannung != null) {
								stuecklistearbeitsplanDto.setIAufspannung(aufspannung.intValue());
							}

							if (ppmmenge != null) {
								stuecklistearbeitsplanDto.setNPpm(ppmmenge);
							}

							if (mgz != null) {
								stuecklistearbeitsplanDto.setIMitarbeitergleichzeitig(mgz.intValue());
							}

							Long lStueckzeit = 0L;
							if (stueckzeit != null) {

								if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_STUNDE)) {
									lStueckzeit = stueckzeit.multiply(new BigDecimal(3600000)).longValue();
								} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_MINUTE)) {
									lStueckzeit = stueckzeit.multiply(new BigDecimal(60000)).longValue();
								} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_SEKUNDE)) {
									lStueckzeit = stueckzeit.multiply(new BigDecimal(1000)).longValue();
								}

							}
							stuecklistearbeitsplanDto.setLStueckzeit(lStueckzeit);

							Long lRuestzeit = 0L;
							if (ruestzeit != null) {

								if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_STUNDE)) {
									lRuestzeit = ruestzeit.multiply(new BigDecimal(3600000)).longValue();
								} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_MINUTE)) {
									lRuestzeit = ruestzeit.multiply(new BigDecimal(60000)).longValue();
								} else if (einheitStueckRuestZeit.equals(SystemFac.EINHEIT_SEKUNDE)) {
									lRuestzeit = ruestzeit.multiply(new BigDecimal(1000)).longValue();
								}

							}
							stuecklistearbeitsplanDto.setLRuestzeit(lRuestzeit);

							if (agBeginn != null) {
								stuecklistearbeitsplanDto.setIMaschinenversatztage(agBeginn.intValue());
							}

							String apKommentarKennung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_ARBEITSPLANIMPORT_SPALTE_AP_KOMMENTARKENNUNG, 15, i);
							String apKommentarBezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_ARBEITSPLANIMPORT_SPALTE_AP_KOMMENTARBEZEICHNUNG, 3000, i);
							ApkommentarDto apkommentarDto = findApkommentar(apKommentarKennung, apKommentarBezeichnung,
									theClientDto);
							stuecklistearbeitsplanDto.setApkommentarIId(apkommentarDto.getIId());

							getStuecklisteFac().createStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);
						}

					}
				}
			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;
	}

	private ApkommentarDto findApkommentar(String kennung, String bezeichnung, TheClientDto theClientDto) {
		if (isStringEmpty(kennung))
			return new ApkommentarDto();

		ApkommentarDto apkommentarDto = getStuecklisteFac().apkommentartFindByCNrMandantCNrOhneExc(kennung,
				theClientDto.getMandant());
		if (apkommentarDto != null)
			return apkommentarDto;

		apkommentarDto = new ApkommentarDto();
		ApkommentarsprDto apkommentarSprDto = new ApkommentarsprDto();
		apkommentarSprDto.setCBez(bezeichnung);
		apkommentarDto.setApkommentarsprDto(apkommentarSprDto);
		apkommentarDto.setCNr(kennung);
		apkommentarDto.setIId(getStuecklisteFac().createApkommentar(apkommentarDto, theClientDto));

		return apkommentarDto;
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

	private Short getShortAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c.getType() == CellType.BOOLEAN || c.getType() == CellType.BOOLEAN_FORMULA) {
					boolean b = ((jxl.BooleanCell) c).getValue();
					return Helper.boolean2Short(b);

				} else {

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
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	public Integer kommentarartProFirstAnlegen(TheClientDto theClientDto) {

		Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
		query.setParameter(1, "ProFirst");
		Collection<?> cl = query.getResultList();

		if (cl.size() == 0) {
			ArtikelkommentarartDto artikelkommentarartDto = new ArtikelkommentarartDto();
			artikelkommentarartDto.setCNr("ProFirst");
			artikelkommentarartDto.setBTooltip(Helper.boolean2Short(false));
			artikelkommentarartDto.setBWebshop(Helper.boolean2Short(false));

			try {
				return getArtikelkommentarFac().createArtikelkommentarart(artikelkommentarartDto, theClientDto);
			} catch (RemoteException e) {
				return null;
			}
		} else {
			return ((Artikelkommentarart) cl.iterator().next()).getIId();
		}

	}

	public Integer liefergruppeSuchenUndAnlegen(TheClientDto theClientDto, String liefergruppe) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("LfliefergruppeFindByCNrMandantCNr");
			query.setParameter(1, liefergruppe);
			query.setParameter(2, theClientDto.getMandant());

			Lfliefergruppe bean = (Lfliefergruppe) query.getSingleResult();
			return bean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			LfliefergruppeDto dto = new LfliefergruppeDto();
			dto.setCNr(liefergruppe);
			dto.setMandantCNr(theClientDto.getMandant());
			Integer liefergruppeIId = getLieferantServicesFac().createLfliefergruppe(dto, theClientDto);
			return liefergruppeIId;

		}
	}

	private BigDecimal getBigDecimalAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten,
			String feldname, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new BigDecimal(d);

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

}