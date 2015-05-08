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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.stueckliste.ejb.Stuecklistearbeitsplan;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Einheit;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class StuecklisteimportFacBean extends Facade implements
		StuecklisteimportFac {
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereMaterialXLS(byte[] xlsDatei,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto) {

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

					if (sZeile[i].getContents() != null
							&& sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(),
								new Integer(i));
					}

				}

			}

			Integer montageartIId = null;
			MontageartDto[] dtos = getStuecklisteFac()
					.montageartFindByMandantCNr(theClientDto);

			if (dtos != null && dtos.length > 0) {
				montageartIId = dtos[0].getIId();

			}

			String default_artikeleinheit = null;
			Integer default_mwstsaztIId = null;

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null
					&& parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

			// Artikelnummer muss immer vorhanden sein
			if (hmVorhandeneSpalten
					.containsKey(XLS_MATERIALIMPORT_SPALTE_KOPFSTUECKLISTE) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_MATERIALIMPORT_SPALTE_ARTIKELNUMMER) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_MATERIALIMPORT_SPALTE_MENGE) == false) {
				rueckgabe += "Es muessen zumindest die Spalten 'Kopfstueckliste/Artikelnummer/Menge' vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);
				fehlerZeileXLSImport = "";

				System.out.println(i + " von " + sheet.getRows());

				Integer iSpalteArtikelnummer = hmVorhandeneSpalten
						.get(XLS_MATERIALIMPORT_SPALTE_KOPFSTUECKLISTE);
				String artikelnummer = sZeile[iSpalteArtikelnummer]
						.getContents();

				if (artikelnummer == null || artikelnummer.length() == 0) {
					rueckgabe += "Die Artikelnummer der Kopfstueckliste darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				String artikelnummerMaterial = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_ARTIKELNUMMER, 25, i);

				if (artikelnummerMaterial == null
						|| artikelnummerMaterial.length() == 0) {
					rueckgabe += "Die Artikelnummer des Material-Artikels darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				String stuecklistenart = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_STUECKLISTENART, 40, i);

				if (stuecklistenart != null) {

					if (stuecklistenart.trim().equals("Stueckliste")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_STUECKLISTE;
					} else if (stuecklistenart.trim()
							.equals("Hilfsstueckliste")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE;
					} else if (stuecklistenart.trim().equals("Setartikel")) {
						stuecklistenart = StuecklisteFac.STUECKLISTEART_SETARTIKEL;
					} else {
						rueckgabe += "Die Stuecklistenart muss Stueckliste,Hilfsstueckliste oder Setartikel sein. Zeile "
								+ i + new String(CRLFAscii);
						continue;
					}

				} else {
					stuecklistenart = StuecklisteFac.STUECKLISTEART_STUECKLISTE;
				}

				BigDecimal menge = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_MENGE, i);

				if (menge == null) {
					rueckgabe += "Die Menge der Position darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByCNrOhneExc(artikelnummer, theClientDto);

				if (artikelDto != null) {
					artikelDto = getArtikelFac().artikelFindByPrimaryKey(
							artikelDto.getIId(), theClientDto);
				} else {
					artikelDto = new ArtikelDto();
					artikelDto.setCNr(artikelnummer);

					// Bezeichnungen
					if (artikelDto.getArtikelsprDto() == null) {
						artikelDto.setArtikelsprDto(new ArtikelsprDto());
						// Spr nur bei Neuanlage Updaten
						if (hmVorhandeneSpalten
								.containsKey(XLS_MATERIALIMPORT_SPALTE_KOPFBEZEICHNUNG)) {
							artikelDto
									.getArtikelsprDto()
									.setCBez(
											getStringAusXLS(
													sZeile,
													hmVorhandeneSpalten,
													XLS_MATERIALIMPORT_SPALTE_KOPFBEZEICHNUNG,
													40, i));
						}

					}

					artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
					artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);

					artikelDto.setEinheitCNr(default_artikeleinheit);

				}

				String mengeneinheit = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_MENGENEINHEIT, 15, i);

				if (mengeneinheit == null) {
					mengeneinheit = SystemFac.EINHEIT_STUECK;
				}

				String kommentar = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_KOMMENTAR, 80, i);

				String position = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_MATERIALIMPORT_SPALTE_POSITION, 40, i);

				BigDecimal lfdNummer = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten, XLS_MATERIALIMPORT_SPALTE_LFDNR, i);

				if (fehlerZeileXLSImport.length() > 0) {
					rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
				}

				if (bImportierenWennKeinFehler) {

					Integer artikelIId = null;

					if (artikelDto.getIId() != null) {

						artikelIId = artikelDto.getIId();

					} else {

						artikelDto.setBVersteckt(Helper.boolean2Short(false));

						artikelIId = getArtikelFac().createArtikel(artikelDto,
								theClientDto);
					}

					// Stueckliste
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByArtikelIIdMandantCNrOhneExc(
									artikelIId, theClientDto.getMandant());
					if (stklDto != null) {

					} else {
						// Stueckliste neu anlegen
						stklDto = new StuecklisteDto();
						stklDto.setArtikelIId(artikelIId);

						try {
							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stklDto.setBAusgabeunterstueckliste(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stklDto.setBMaterialbuchungbeiablieferung(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
									.fertigungsgruppeFindByMandantCNr(
											theClientDto.getMandant(),
											theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stklDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
										.getIId());
							}
							stklDto.setLagerIIdZiellager(getLagerFac()
									.getHauptlagerDesMandanten(theClientDto)
									.getIId());
							stklDto.setNLosgroesse(new BigDecimal(1));
							stklDto.setIErfassungsfaktor(1);

							stklDto.setBFremdfertigung(Helper
									.boolean2Short(false));

							stklDto.setStuecklisteartCNr(stuecklistenart);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						Integer stuecklisteIId = getStuecklisteFac()
								.createStueckliste(stklDto, theClientDto);

						stklDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(stuecklisteIId,
										theClientDto);
					}

					// Material

					ArtikelDto artikelDtoMateiral = getArtikelFac()
							.artikelFindByCNrOhneExc(artikelnummerMaterial,
									theClientDto);

					Integer artikelIIdMaterial = null;
					if (artikelDtoMateiral != null) {
						artikelDtoMateiral = getArtikelFac()
								.artikelFindByPrimaryKey(
										artikelDtoMateiral.getIId(),
										theClientDto);

						artikelIIdMaterial = artikelDtoMateiral.getIId();

					} else {
						artikelDtoMateiral = new ArtikelDto();
						artikelDtoMateiral.setCNr(artikelnummerMaterial);
						artikelDtoMateiral
								.setMwstsatzbezIId(default_mwstsaztIId);
						artikelDtoMateiral
								.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
						artikelDtoMateiral.setBVersteckt(Helper
								.boolean2Short(false));

						// Einheit anlegen
						try {
							// duplicateunique: Pruefung: Artikelklasse
							// bereits
							// vorhanden.
							Query query = em
									.createNamedQuery("EinheitFindByCNr");
							query.setParameter(1, mengeneinheit);

							Einheit einheit = (Einheit) query.getSingleResult();

						} catch (NoResultException ex) {

							EinheitDto einheitDto = new EinheitDto();
							einheitDto.setCNr(mengeneinheit);
							einheitDto.setIDimension(0);
							getSystemFac().createEinheit(einheitDto,
									theClientDto);

						}

						artikelDtoMateiral.setEinheitCNr(mengeneinheit);

						if (artikelDtoMateiral.getArtikelsprDto() == null) {
							artikelDtoMateiral
									.setArtikelsprDto(new ArtikelsprDto());
							artikelDtoMateiral
									.getArtikelsprDto()
									.setCBez(
											getStringAusXLS(
													sZeile,
													hmVorhandeneSpalten,
													XLS_MATERIALIMPORT_SPALTE_ARTIKELBEZEICHNUNG,
													40, i));
						}

						artikelIIdMaterial = getArtikelFac().createArtikel(
								artikelDtoMateiral, theClientDto);
					}

					StuecklistepositionDto[] vorhandenePositionenDesArtikels = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIIdArtikelIId(
									stklDto.getIId(), artikelIIdMaterial,
									theClientDto);

					boolean bVorhanden = false;

					for (int u = 0; u < vorhandenePositionenDesArtikels.length; u++) {
						if (vorhandenePositionenDesArtikels[u].getCPosition() == null
								&& position == null) {

							bVorhanden = true;

						} else if (vorhandenePositionenDesArtikels[u]
								.getCPosition() != null
								&& position != null
								&& vorhandenePositionenDesArtikels[u]
										.getCPosition().equals(position)) {
							bVorhanden = true;
						}
					}

					if (bVorhanden == false) {
						// Position
						StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
						stuecklistepositionDto.setStuecklisteIId(stklDto
								.getIId());
						stuecklistepositionDto
								.setArtikelIId(artikelIIdMaterial);
						stuecklistepositionDto
								.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stuecklistepositionDto.setEinheitCNr(mengeneinheit);
						if (hmVorhandeneSpalten
								.containsKey(XLS_MATERIALIMPORT_SPALTE_LFDNR)
								&& lfdNummer != null) {
							stuecklistepositionDto.setILfdnummer(lfdNummer
									.intValue());

						}

						if (hmVorhandeneSpalten
								.containsKey(XLS_MATERIALIMPORT_SPALTE_KOMMENTAR)) {
							stuecklistepositionDto.setCKommentar(kommentar);
						}
						if (hmVorhandeneSpalten
								.containsKey(XLS_MATERIALIMPORT_SPALTE_POSITION)) {
							stuecklistepositionDto.setCPosition(position);
						}

						stuecklistepositionDto.setNMenge(menge);
						stuecklistepositionDto.setBMitdrucken(Helper
								.boolean2Short(false));
						stuecklistepositionDto.setMontageartIId(montageartIId);

						getStuecklisteFac().createStuecklisteposition(
								stuecklistepositionDto, theClientDto);
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereArbeitsplanXLS(byte[] xlsDatei,
			String einheitStueckRuestZeit, boolean bImportierenWennKeinFehler,
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

					if (sZeile[i].getContents() != null
							&& sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(),
								new Integer(i));
					}

				}

			}

			String default_artikeleinheit = null;
			Integer default_mwstsaztIId = null;

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART);

			String default_artikelart = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null
					&& parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

			// Artikelnummer muss immer vorhanden sein
			if (hmVorhandeneSpalten
					.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFSTUECKLISTE) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELNUMMER) == false
					|| hmVorhandeneSpalten
							.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_AG_NUMMER) == false) {
				rueckgabe += "Es muessen zumindest die Spalten 'Kopfstueckliste/AGNummer/Artikelnummer' vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);
				fehlerZeileXLSImport = "";

				Integer iSpalteArtikelnummer = hmVorhandeneSpalten
						.get(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFSTUECKLISTE);
				String artikelnummer = sZeile[iSpalteArtikelnummer]
						.getContents();

				if (artikelnummer == null || artikelnummer.length() == 0) {
					rueckgabe += "Die Artikelnummer der Kopfstueckliste darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				String artikelnummerArbeitsplan = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELNUMMER, 25, i);

				if (artikelnummerArbeitsplan == null
						|| artikelnummerArbeitsplan.length() == 0) {
					rueckgabe += "Die Artikelnummer des AZ-Artikel darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				BigDecimal agNummer = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_AG_NUMMER, i);

				if (agNummer == null) {
					rueckgabe += "Die AGNummer des Arbeitsplans darf nicht leer sein. Zeile "
							+ i + new String(CRLFAscii);
					continue;
				}

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByCNrOhneExc(artikelnummer, theClientDto);

				if (artikelDto != null) {
					artikelDto = getArtikelFac().artikelFindByPrimaryKey(
							artikelDto.getIId(), theClientDto);
				} else {
					artikelDto = new ArtikelDto();
					artikelDto.setCNr(artikelnummer);

					// Bezeichnungen
					if (artikelDto.getArtikelsprDto() == null) {
						artikelDto.setArtikelsprDto(new ArtikelsprDto());
						// Spr nur bei Neuanlage Updaten
						if (hmVorhandeneSpalten
								.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOPFBEZEICHNUNG)) {
							artikelDto
									.getArtikelsprDto()
									.setCBez(
											getStringAusXLS(
													sZeile,
													hmVorhandeneSpalten,
													XLS_ARBEITSPLANIMPORT_SPALTE_KOPFBEZEICHNUNG,
													40, i));
						}

					}

					artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
					artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
					artikelDto.setEinheitCNr(default_artikeleinheit);

				}

				BigDecimal stueckzeit = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_STUECKZEIT, i);

				BigDecimal ruestzeit = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_RUESTZEIT, i);

				String inventarnummer_maschine = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_MASCHINENNUMMER, 40, i);
				Integer maschineIId = null;
				if (inventarnummer_maschine != null) {

					Query query = em
							.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, inventarnummer_maschine);
					Maschine maschine;
					try {
						maschine = (Maschine) query.getSingleResult();
						maschineIId = maschine.getIId();
					} catch (NoResultException e) {
						rueckgabe += "Ein Maschine mit der Inventarnummer '"
								+ inventarnummer_maschine
								+ "' ist nicht vorhanden. Zeile " + i
								+ new String(CRLFAscii);
						continue;
					}

				}

				String agart = getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_AG_ART, 15, i);

				if (agart != null) {
					if (agart.trim().equals(
							StuecklisteFac.AGART_LAUFZEIT.trim())
							|| agart.trim().equals(
									StuecklisteFac.AGART_UMSPANNZEIT.trim())) {
					} else {
						rueckgabe += "AG-Art muss entweder 'Laufzeit' oder 'Umspannzeit' sein. Zeile "
								+ i + new String(CRLFAscii);
						continue;
					}

				}

				String kommentarKurz = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_KURZ, 80, i);

				String kommentarLang = getStringAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_LANG, 3000, i);

				BigDecimal aufspannung = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_AUFSPANNUNG, i);
				BigDecimal agBeginn = getBigDecimalAusXLS(sZeile,
						hmVorhandeneSpalten,
						XLS_ARBEITSPLANIMPORT_SPALTE_AG_BEGINN, i);

				Short nurMaschinenzeit = getShortAusXLS(sZeile,
						hmVorhandeneSpalten,
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

						artikelIId = getArtikelFac().createArtikel(artikelDto,
								theClientDto);
					}

					// Stueckliste
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByArtikelIIdMandantCNrOhneExc(
									artikelIId, theClientDto.getMandant());
					if (stklDto != null) {

					} else {
						// Stueckliste neu anlegen
						stklDto = new StuecklisteDto();
						stklDto.setArtikelIId(artikelIId);

						try {
							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stklDto.setBAusgabeunterstueckliste(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stklDto.setBMaterialbuchungbeiablieferung(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
									.fertigungsgruppeFindByMandantCNr(
											theClientDto.getMandant(),
											theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stklDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
										.getIId());
							}
							stklDto.setLagerIIdZiellager(getLagerFac()
									.getHauptlagerDesMandanten(theClientDto)
									.getIId());
							stklDto.setNLosgroesse(new BigDecimal(1));
							stklDto.setIErfassungsfaktor(1);

							stklDto.setBFremdfertigung(Helper
									.boolean2Short(false));

							stklDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						Integer stuecklisteIId = getStuecklisteFac()
								.createStueckliste(stklDto, theClientDto);

						stklDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(stuecklisteIId,
										theClientDto);
					}

					// Arbeitsplan

					ArtikelDto artikelDtoArbeitsplan = getArtikelFac()
							.artikelFindByCNrOhneExc(artikelnummerArbeitsplan,
									theClientDto);

					Integer artikelIIdArbeitsplan = null;
					if (artikelDtoArbeitsplan != null) {
						artikelDtoArbeitsplan = getArtikelFac()
								.artikelFindByPrimaryKey(
										artikelDtoArbeitsplan.getIId(),
										theClientDto);

						artikelIIdArbeitsplan = artikelDtoArbeitsplan.getIId();

					} else {
						artikelDtoArbeitsplan = new ArtikelDto();
						artikelDtoArbeitsplan.setCNr(artikelnummerArbeitsplan);
						artikelDtoArbeitsplan
								.setMwstsatzbezIId(default_mwstsaztIId);
						artikelDtoArbeitsplan
								.setArtikelartCNr(ArtikelFac.ARTIKELART_ARBEITSZEIT);
						artikelDtoArbeitsplan.setBVersteckt(Helper
								.boolean2Short(false));
						artikelDtoArbeitsplan
								.setEinheitCNr(SystemFac.EINHEIT_STUNDE);

						if (artikelDtoArbeitsplan.getArtikelsprDto() == null) {
							artikelDtoArbeitsplan
									.setArtikelsprDto(new ArtikelsprDto());
							artikelDtoArbeitsplan
									.getArtikelsprDto()
									.setCBez(
											getStringAusXLS(
													sZeile,
													hmVorhandeneSpalten,
													XLS_ARBEITSPLANIMPORT_SPALTE_ARTIKELBEZEICHNUNG,
													40, i));
						}

						artikelIIdArbeitsplan = getArtikelFac().createArtikel(
								artikelDtoArbeitsplan, theClientDto);
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
						stuecklistearbeitsplanDto.setStuecklisteIId(stklDto
								.getIId());
						stuecklistearbeitsplanDto
								.setArtikelIId(artikelIIdArbeitsplan);
						stuecklistearbeitsplanDto.setIArbeitsgang(agNummer
								.intValue());
						if (hmVorhandeneSpalten
								.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_AG_ART)) {

							stuecklistearbeitsplanDto.setAgartCNr(agart);
						}
						stuecklistearbeitsplanDto
								.setBNurmaschinenzeit(nurMaschinenzeit);

						if (hmVorhandeneSpalten
								.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_MASCHINENNUMMER)) {

							stuecklistearbeitsplanDto
									.setMaschineIId(maschineIId);
						}

						if (hmVorhandeneSpalten
								.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_KURZ)) {
							stuecklistearbeitsplanDto
									.setCKommentar(kommentarKurz);
						}
						if (hmVorhandeneSpalten
								.containsKey(XLS_ARBEITSPLANIMPORT_SPALTE_KOMMENTAR_LANG)) {
							stuecklistearbeitsplanDto
									.setXLangtext(kommentarLang);
						}

						if (aufspannung != null) {
							stuecklistearbeitsplanDto
									.setIAufspannung(aufspannung.intValue());
						}

						Long lStueckzeit = 0L;
						if (stueckzeit != null) {

							if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_STUNDE)) {
								lStueckzeit = stueckzeit.multiply(
										new BigDecimal(3600000)).longValue();
							} else if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_MINUTE)) {
								lStueckzeit = stueckzeit.multiply(
										new BigDecimal(60000)).longValue();
							} else if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_SEKUNDE)) {
								lStueckzeit = stueckzeit.multiply(
										new BigDecimal(1000)).longValue();
							}

						}
						stuecklistearbeitsplanDto.setLStueckzeit(lStueckzeit);

						Long lRuestzeit = 0L;
						if (ruestzeit != null) {

							if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_STUNDE)) {
								lRuestzeit = ruestzeit.multiply(
										new BigDecimal(3600000)).longValue();
							} else if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_MINUTE)) {
								lRuestzeit = ruestzeit.multiply(
										new BigDecimal(60000)).longValue();
							} else if (einheitStueckRuestZeit
									.equals(SystemFac.EINHEIT_SEKUNDE)) {
								lRuestzeit = ruestzeit.multiply(
										new BigDecimal(1000)).longValue();
							}

						}
						stuecklistearbeitsplanDto.setLRuestzeit(lRuestzeit);

						if (agBeginn != null) {
							stuecklistearbeitsplanDto
									.setIMaschinenversatztage(agBeginn
											.intValue());
						}

						getStuecklisteFac().createStuecklistearbeitsplan(
								stuecklistearbeitsplanDto, theClientDto);
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

						fehlerZeileXLSImport += feldname + " ist zu lang (>"
								+ iLaenge + ") Zeile " + iZeile
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

						fehlerZeileXLSImport += feldname
								+ " darf nur die Werte 0 bzw. 1 enthalten. Zeile "
								+ iZeile + new String(CRLFAscii);

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

						fehlerZeileXLSImport += feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile
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

						fehlerZeileXLSImport += feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile
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