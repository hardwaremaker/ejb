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
package com.lp.server.artikel.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CellReferenceHelper;
import jxl.read.biff.BiffException;

import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.ArtikelQuery;
import com.lp.server.artikel.ejb.Artikelalergen;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentarart;
import com.lp.server.artikel.ejb.Artikelkommentardruck;
import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprPK;
import com.lp.server.artikel.ejb.Artikellager;
import com.lp.server.artikel.ejb.ArtikellagerPK;
import com.lp.server.artikel.ejb.Artikellagerplaetze;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Automotive;
import com.lp.server.artikel.ejb.Farbcode;
import com.lp.server.artikel.ejb.Hersteller;
import com.lp.server.artikel.ejb.HerstellerQuery;
import com.lp.server.artikel.ejb.Lagerplatz;
import com.lp.server.artikel.ejb.Material;
import com.lp.server.artikel.ejb.Medical;
import com.lp.server.artikel.ejb.Reach;
import com.lp.server.artikel.ejb.Rohs;
import com.lp.server.artikel.ejb.Verschleissteil;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.ejb.VkPreisfindungPreisliste;
import com.lp.server.artikel.ejb.Vkpfartikelpreisliste;
import com.lp.server.artikel.service.AlergenDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelImportDto;
import com.lp.server.artikel.service.ArtikelalergenDto;
import com.lp.server.artikel.service.ArtikelimportFac;
import com.lp.server.artikel.service.ArtikelimportFehlerDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantImportDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.AutomotiveDto;
import com.lp.server.artikel.service.FarbcodeDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MedicalDto;
import com.lp.server.artikel.service.MontageDto;
import com.lp.server.artikel.service.ReachDto;
import com.lp.server.artikel.service.RohsDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.Kundesokomengenstaffel;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.ejb.Einheit;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ArtikelimportFacBean extends Facade implements ArtikelimportFac {
	@PersistenceContext
	private EntityManager em;

	public static String XLS_IMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";
	public static String XLS_IMPORT_SPALTE_ARTIKELART = "Artikelart";
	public static String XLS_IMPORT_SPALTE_KBEZ = "Kurzbezeichnung";
	public static String XLS_IMPORT_SPALTE_BEZ = "Bezeichnung";
	public static String XLS_IMPORT_SPALTE_ZBEZ = "Zusatzbezeichnung";
	public static String XLS_IMPORT_SPALTE_ZBEZ2 = "Zusatzbezeichnung2";
	public static String XLS_IMPORT_SPALTE_REFERENZNUMMER = "Referenznummer";
	public static String XLS_IMPORT_SPALTE_INDEX = "Index";
	public static String XLS_IMPORT_SPALTE_REVISION = "Revision";
	public static String XLS_IMPORT_SPALTE_HERSTELLER = "Hersteller";
	public static String XLS_IMPORT_SPALTE_HERSTELLERNUMMER = "Herstellernummer";
	public static String XLS_IMPORT_SPALTE_HERSTELLERBEZEICHNUNG = "Herstellerbezeichnung";
	public static String XLS_IMPORT_SPALTE_MENGENEINHEIT = "Mengeneinheit";
	public static String XLS_IMPORT_SPALTE_UMRECHNUNGSFAKTOR_BESTELLEMGNENEINHEIT = "UmrechnungsfaktorBestellmengeneinheit";
	public static String XLS_IMPORT_SPALTE_BESTELLEMGNENEINHEIT = "Bestellmengeneinheit";
	public static String XLS_IMPORT_SPALTE_ARTIKELKLASSE = "Artikelklasse";
	public static String XLS_IMPORT_SPALTE_ARTIKELGRUPPE = "Artikelgruppe";
	public static String XLS_IMPORT_SPALTE_VERKAUFSPREISBASIS = "Verkaufspreisbasis";
	public static String XLS_IMPORT_SPALTE_ARTIKELLIEFERANT = "Artikellieferant";
	public static String XLS_IMPORT_SPALTE_ARTIKELNUMMER_LIEFERANT = "ArtikelnummerLieferant";
	public static String XLS_IMPORT_SPALTE_ARTIKELBEZEICHNUNG_LIEFERANT = "ArtikelbezeichnungLieferant";
	public static String XLS_IMPORT_SPALTE_EINKAUFSPREIS_LIEFERANT = "EinkaufspreisLieferant";

	public static String XLS_IMPORT_SPALTE_WIEDERBESCHAFFUNGSZEIT_LIEFERANT = "Wiederbeschaffungszeit";
	public static String XLS_IMPORT_SPALTE_MINDESTBESTELLMENGE_LIEFERANT = "Mindestbestellmenge";
	public static String XLS_IMPORT_SPALTE_VERPACKUNGSEINHEIT_LIEFERANT = "Verpackungseinheit";
	public static String XLS_IMPORT_SPALTE_STANDARDMENGE_LIEFERANT = "Standardmenge";

	public static String XLS_IMPORT_SPALTE_RABATT_LIEFERANT = "RabattLieferant";
	public static String XLS_IMPORT_SPALTE_NETTOPREIS_LIEFERANT = "NettopreisLieferant";
	public static String XLS_IMPORT_SPALTE_LAGERPLATZ_HAUPTLAGER = "LagerplatzHauptlager";
	public static String XLS_IMPORT_SPALTE_BREITE = "Breite";
	public static String XLS_IMPORT_SPALTE_HOEHE = "Hoehe";
	public static String XLS_IMPORT_SPALTE_TIEFE = "Tiefe";
	public static String XLS_IMPORT_SPALTE_GEWICHT = "Gewicht";
	public static String XLS_IMPORT_SPALTE_MATERIALGEWICHT = "Materialgewicht";
	public static String XLS_IMPORT_SPALTE_MATERIAL = "Material";
	public static String XLS_IMPORT_SPALTE_LAGERMINDESTSTAND = "LagerMindeststand";
	public static String XLS_IMPORT_SPALTE_LAGERSOLLSTAND = "LagerSollstand";
	public static String XLS_IMPORT_SPALTE_FERTIGUNGSSATZGROESSE = "Fertigungssatzgroesse";
	public static String XLS_IMPORT_SPALTE_VERSCHNITTFAKTOR = "Verschnittfaktor";
	public static String XLS_IMPORT_SPALTE_VERSCHNITTBASIS = "Verschnittbasis";
	public static String XLS_IMPORT_SPALTE_UEBERPRODUKTION = "Ueberproduktion";
	public static String XLS_IMPORT_SPALTE_MINDESTDECKUNGSBEITRAG = "Mindestdeckungsbeitrag";
	public static String XLS_IMPORT_SPALTE_WARENVERKEHRSNUMMMER = "Warenverkehrsnummer";
	public static String XLS_IMPORT_SPALTE_CHARGENNUMMERNGEFUEHRT = "Chargennummerngefuehrt";
	public static String XLS_IMPORT_SPALTE_SERIENNUMMERNGEFUEHRT = "Seriennummerngefuehrt";
	public static String XLS_IMPORT_SPALTE_LAGERBEWIRTSCHAFTET = "Lagerbewirtschaftet";
	public static String XLS_IMPORT_SPALTE_KOMMISSIONIEREN = "Kommissionieren";
	public static String XLS_IMPORT_SPALTE_GESTEHUNGSPREIS = "Gestehungspreis";
	public static String XLS_IMPORT_SPALTE_PREISEINHEIT = "Preiseinheit";

	public static String XLS_IMPORT_SPALTE_KOMMENTARART = "Kommentarart";

	public static String XLS_IMPORT_SPALTE_DATEIVERWEIS = "Dateiverweis";
	public static String XLS_IMPORT_SPALTE_KOMMENTAR = "Kommentar";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_ANGEBOT = "MitdruckenAngebot";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_ANFRAGE = "MitdruckenAnfrage";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_STUECKLISTE = "MitdruckenStueckliste";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_AUFTRAG = "MitdruckenAuftrag";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_BESTELLUNG = "MitdruckenBestellung";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_FERTIGUNG = "MitdruckenFertigung";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_LIEFERSCHEIN = "MitdruckenLieferschein";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_WARENEINGANG = "MitdruckenWareneingang";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_RECHNUNG = "MitdruckenRechnung";
	public static String XLS_IMPORT_SPALTE_MITDRUCKEN_EINGANGSRECHNUNG = "MitdruckenEingangsrechnung";

	// PJ18763
	public static String XLS_IMPORT_SPALTE_BREITE_TEXT = "BreiteText";
	public static String XLS_IMPORT_SPALTE_RASTER_STEHEND = "RasterStehend";
	public static String XLS_IMPORT_SPALTE_RASTER_LIEGEND = "RasterLiegend";
	public static String XLS_IMPORT_SPALTE_STROMVERBRAUCH_TYPISCH = "StromverbrauchTypisch";
	public static String XLS_IMPORT_SPALTE_STROMVERBRAUCH_MAXIMAL = "StromverbrauchMaximal";
	public static String XLS_IMPORT_SPALTE_AUFSCHLAG_BETRAG = "AufschlagBetrag";
	public static String XLS_IMPORT_SPALTE_AUFSCHLAG_PROZENT = "AufschlagProzent";
	public static String XLS_IMPORT_SPALTE_BAUFORM = "Bauform";
	public static String XLS_IMPORT_SPALTE_VERPACKUNGSART = "Verpackungsart";
	public static String XLS_IMPORT_SPALTE_HOCHSETZEN = "Hochsetzen";
	public static String XLS_IMPORT_SPALTE_HOCHSTELLEN = "Hochstellen";
	public static String XLS_IMPORT_SPALTE_POLARISIERT = "Polarisiert";
	public static String XLS_IMPORT_SPALTE_ANTISTATICMASSNAHMEN = "AntistaticMassnahmen";
	public static String XLS_IMPORT_SPALTE_REACH = "Reach";
	public static String XLS_IMPORT_SPALTE_ROHS = "Rohs";
	public static String XLS_IMPORT_SPALTE_AUTOMOTIVE = "Automotive";
	public static String XLS_IMPORT_SPALTE_MEDICAL = "Medical";
	public static String XLS_IMPORT_SPALTE_UL = "UL";
	public static String XLS_IMPORT_SPALTE_FARBCODE = "Farbcode";
	public static String XLS_IMPORT_SPALTE_LIEFERGRUPPE = "Liefergruppe";
	public static String XLS_IMPORT_SPALTE_ECCN = "ECCN";
	public static String XLS_IMPORT_SPALTE_MWSTSATZBEZ = "Mwstsatzbez";
	public static String XLS_IMPORT_SPALTE_VK_EAN = "VerkaufsEAN";
	public static String XLS_IMPORT_SPALTE_VP_EAN = "VerpackungsEAN";
	public static String XLS_IMPORT_SPALTE_VERPACKUNGSMENGE = "Verpackungsmenge";
	public static String XLS_IMPORT_SPALTE_VERPACKUNGSMITTELMENGE = "Verpackungsmittelmenge";
	public static String XLS_IMPORT_SPALTE_VERPACKUNGSMITTEL = "Verpackungsmittel";

	public static String VERSCHLEISSTEIL_IMPORT_KENNUNG = "Kennung";
	public static String VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG = "Bezeichnung";
	public static String VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG2 = "Bezeichnung2";

	public static String KUNDESOKO_IMPORT_ARTIKELNUMMER = "Artikelnummer";
	public static String KUNDESOKO_IMPORT_KUNDENARTIKELNUMMER = "Kundenartikelnummer";
	public static String KUNDESOKO_IMPORT_MENGE = "Menge";
	public static String KUNDESOKO_IMPORT_FIXPREIS = "Fixpreis";
	public static String KUNDESOKO_IMPORT_RABATT = "Rabatt";
	public static String KUNDESOKO_IMPORT_GUELTIG_AB = "GueltigAb";
	public static String KUNDESOKO_IMPORT_WIRKT_NICHT_IN_PREISFINDUNG = "WirktNichtInPreisfindung";
	public static String KUNDESOKO_IMPORT_KUNDENARTIKELBEZEICHNUNG = "Kundenartikelbez";
	public static String KUNDESOKO_IMPORT_KUNDENARTIKELZUSATZBEZEICHNUNG = "Kundenartikelzbez";
	public static String KUNDESOKO_IMPORT_KUNDENKURZBEZEICHNUNG = "Kundenkurzbezeichnung";
	public static String KUNDESOKO_IMPORT_BEMERKUNG = "Bemerkung";

	public static String LAGERMINSOLL_IMPORT_ARTIKELNUMMER = "Artikelnummer";
	public static String LAGERMINSOLL_IMPORT_LAGER = "Lager";
	public static String LAGERMINSOLL_IMPORT_MIN = "Mindeststand";
	public static String LAGERMINSOLL_IMPORT_SOLL = "Sollstand";

	public static String VKSTAFFEL_IMPORT_ARTIKELNUMMER = "Artikelnummer";
	public static String VKSTAFFEL_IMPORT_MENGE = "Menge";
	public static String VKSTAFFEL_IMPORT_RABATT = "Rabatt";
	public static String VKSTAFFEL_IMPORT_FIXPREIS = "Fixpreis";
	public static String VKSTAFFEL_IMPORT_GUELTIG_AB = "GueltigAb";
	public static String VKSTAFFEL_IMPORT_GUELTIG_BIS = "GueltigBis";
	public static String VKSTAFFEL_IMPORT_PREISLISTE = "Preisliste";
	public static String VKSTAFFEL_IMPORT_WIRKT_FUER_ALLE_PREISLITEN = "WirktFuerAllePreislisten";

	public String importiereVerschleissteileXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			Workbook workbook = Workbook.getWorkbook(is);

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

			if (!hmVorhandeneSpalten.containsKey(VERSCHLEISSTEIL_IMPORT_KENNUNG)) {
				rueckgabe += "Es muss zumindest die Spalte 'Kennung' vorhanden sein" + new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);
						fehlerZeileXLSImport = "";

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						String kennung = getStringAusXLS(sZeile, hmVorhandeneSpalten, VERSCHLEISSTEIL_IMPORT_KENNUNG,
								40, i);

						if (kennung == null || kennung.length() == 0) {
							rueckgabe += "Die Kennung darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						String bezeichnung = null;

						if (hmVorhandeneSpalten.containsKey(VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG)) {
							bezeichnung = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG, 80, i);
						}

						String bezeichnung2 = null;

						if (hmVorhandeneSpalten.containsKey(VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG2)) {
							bezeichnung2 = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									VERSCHLEISSTEIL_IMPORT_BEZEICHNUNG2, 80, i);
						}

						Verschleissteil v = null;

						try {
							Query query = em.createNamedQuery("VerschleissteilfindByCNr");
							query.setParameter(1, kennung);

							v = (Verschleissteil) query.getSingleResult();

							// Dann Update

						} catch (NoResultException ex1) {
							// nothing here

						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler == true) {

							if (v == null) {

								VerschleissteilDto vDto = new VerschleissteilDto();
								vDto.setCNr(kennung);
								vDto.setCBez(bezeichnung);
								vDto.setCBez2(bezeichnung2);

								getArtikelFac().createVerschleissteil(vDto);
							} else {
								if (bezeichnung != null) {
									v.setCBez(bezeichnung);
								}
								if (bezeichnung2 != null) {
									v.setCBez2(bezeichnung2);
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

		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String importiereKundesokoXLS(byte[] xlsDatei, Integer kundeIId, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			Workbook workbook = Workbook.getWorkbook(is);

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
			if (kundeIId == null && (!hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_ARTIKELNUMMER)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_ARTIKELNUMMER)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_FIXPREIS)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_RABATT)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_MENGE)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_GUELTIG_AB))) {
				rueckgabe += "Es muessen zumindest die Spalten 'Kundenkurzbezeichnung/Artikelnummer/Menge/Fixpreis/Rabatt/GueltigAb' vorhanden sein"
						+ new String(CRLFAscii);
			} else if (kundeIId != null && (!hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_ARTIKELNUMMER)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_FIXPREIS)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_RABATT)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_MENGE)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_GUELTIG_AB))) {
				rueckgabe += "Es muessen zumindest die Spalten 'Artikelnummer/Menge/Fixpreis/Rabatt/GueltigAb' vorhanden sein"
						+ new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);
						fehlerZeileXLSImport = "";

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						Integer kundeIIdZeile = null;
						if (kundeIId != null) {
							kundeIIdZeile = kundeIId;
						} else {
							// Kunde ueber kbez suchen

							String kundekbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
									KUNDESOKO_IMPORT_KUNDENKURZBEZEICHNUNG, 40, i);

							if (kundekbez == null || kundekbez.length() == 0) {
								rueckgabe += "Die Kundenkurzbezeichnung darf nicht leer sein. Zeile " + i
										+ new String(CRLFAscii);
								continue;
							}

							List<KundeDto> list = getKundeFac().kundeFindByKbezMandantCnr(kundekbez, theClientDto);

							if (list.size() > 0) {
								kundeIIdZeile = list.get(0).getIId();
							} else {
								rueckgabe += "Es konnte kein Kunde mit der Kurzbezeichnung '" + kundekbez
										+ "' gefunden werden'. Zeile " + i + new String(CRLFAscii);
								continue;
							}

						}

						String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_ARTIKELNUMMER, 40, i);

						String kundenartikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_KUNDENARTIKELNUMMER, 40, i);

						String kundenartikelbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_KUNDENARTIKELBEZEICHNUNG, 80, i);

						String kundenartikelzbez = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_KUNDENARTIKELZUSATZBEZEICHNUNG, 80, i);

						if (artikelnummer == null || artikelnummer.length() == 0) {
							rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);
						if (aDto == null) {
							rueckgabe += "Die Artikelnummer '" + artikelnummer
									+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Gueltigab

						java.util.Date gueltigab = getDateAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_GUELTIG_AB, i);

						if (gueltigab == null) {
							rueckgabe += "Die Spalte GueltigAb darf nicht leer sein. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

						// Menge

						BigDecimal menge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, KUNDESOKO_IMPORT_MENGE, i);

						if (menge == null) {
							rueckgabe += "Die Spalte Menge darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Preis/Rabatt

						BigDecimal fixpreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								KUNDESOKO_IMPORT_FIXPREIS, i);

						Double rabatt = getDoubleAusXLS(sZeile, hmVorhandeneSpalten, KUNDESOKO_IMPORT_RABATT, i);

						Short wirktnichtinpreisfindung = Helper.boolean2Short(false);

						String cBemerkung = null;
						if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_WIRKT_NICHT_IN_PREISFINDUNG)) {
							wirktnichtinpreisfindung = getShortAusXLS(sZeile, hmVorhandeneSpalten,
									KUNDESOKO_IMPORT_WIRKT_NICHT_IN_PREISFINDUNG, i);
							// SP5152
							if (fixpreis == null && rabatt == null
									&& Helper.short2boolean(wirktnichtinpreisfindung) == true) {
								rabatt = 0D;
							}
						}

						if (fixpreis == null && rabatt == null) {
							rueckgabe += "Es muss zumindest eine der beiden Spalten 'Fixpreis' bzw. 'Rabatt' befuellt sein. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						if (fixpreis != null && rabatt != null) {
							rueckgabe += "Es muss entweder die Spalte 'Fixpreis', oder die Spalte 'Rabatt' befuellt sein. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						if (fixpreis != null) {
							rabatt = 0D;
						}

						if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_BEMERKUNG)) {
							cBemerkung = getStringAusXLS(sZeile, hmVorhandeneSpalten, KUNDESOKO_IMPORT_BEMERKUNG, 40,
									i);
						}

						Kundesoko kdsoko = null;

						try {
							Query query = em.createNamedQuery("KundesokofindByKundeIIdArtikelIIdTPreisgueltigab");
							query.setParameter(1, kundeIIdZeile);
							query.setParameter(2, aDto.getIId());
							query.setParameter(3, gueltigab);

							kdsoko = (Kundesoko) query.getSingleResult();

							// Dann Update

						} catch (NoResultException ex1) {
							// nothing here

						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler == true) {

							if (kdsoko == null) {

								KundesokoDto kdsokoDto = new KundesokoDto();
								kdsokoDto.setArtikelIId(aDto.getIId());
								kdsokoDto.setKundeIId(kundeIIdZeile);
								kdsokoDto.setBWirktNichtFuerPreisfindung(wirktnichtinpreisfindung);
								kdsokoDto.setBBemerkungdrucken(Helper.boolean2Short(false));
								kdsokoDto.setBDrucken(Helper.boolean2Short(false));
								kdsokoDto.setBRabattsichtbar(Helper.boolean2Short(false));
								kdsokoDto.setTPreisgueltigab(new java.sql.Date(gueltigab.getTime()));
								kdsokoDto.setCKundeartikelnummer(kundenartikelnummer);
								kdsokoDto.setCKundeartikelbez(kundenartikelbez);
								kdsokoDto.setCKundeartikelzbez(kundenartikelzbez);
								kdsokoDto.setCBemerkung(cBemerkung);
								KundesokomengenstaffelDto mngstaf = new KundesokomengenstaffelDto();

								mngstaf.setNMenge(menge);
								mngstaf.setNArtikelfixpreis(fixpreis);
								mngstaf.setFArtikelstandardrabattsatz(rabatt);

								getKundesokoFac().createKundesoko(kdsokoDto, mngstaf, theClientDto);
							} else {

								KundesokoDto kdsokoDto = getKundesokoFac().kundesokoFindByPrimaryKey(kdsoko.getIId());

								if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_KUNDENARTIKELNUMMER)) {

									kdsokoDto.setCKundeartikelnummer(kundenartikelnummer);
								}

								if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_KUNDENARTIKELBEZEICHNUNG)) {

									kdsokoDto.setCKundeartikelbez(kundenartikelbez);
								}

								if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_BEMERKUNG)) {

									kdsokoDto.setCBemerkung(cBemerkung);
								}

								if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_KUNDENARTIKELZUSATZBEZEICHNUNG)) {

									kdsokoDto.setCKundeartikelbez(kundenartikelzbez);
								}

								if (hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_WIRKT_NICHT_IN_PREISFINDUNG)) {
									kdsokoDto.setBWirktNichtFuerPreisfindung(wirktnichtinpreisfindung);
								}

								getKundesokoFac().updateKundesoko(kdsokoDto, null, theClientDto);

								Kundesokomengenstaffel mngstaf = null;

								try {
									Query query = em.createNamedQuery("KundesokomengenstaffelfindByUniqueKey");
									query.setParameter(1, kdsoko.getIId());
									query.setParameter(2, menge);

									mngstaf = (Kundesokomengenstaffel) query.getSingleResult();

									// Dann Update

								} catch (NoResultException ex1) {
									// nothing here

								}

								if (mngstaf != null) {
									KundesokomengenstaffelDto mngstfDto = getKundesokoFac()
											.kundesokomengenstaffelFindByPrimaryKey(mngstaf.getIId());

									mngstfDto.setNArtikelfixpreis(fixpreis);
									mngstfDto.setFArtikelstandardrabattsatz(rabatt);

									getKundesokoFac().updateKundesokomengenstaffel(mngstfDto, theClientDto);

								} else {

									KundesokomengenstaffelDto mngstfDto = new KundesokomengenstaffelDto();

									mngstfDto.setKundesokoIId(kdsoko.getIId());
									mngstfDto.setNArtikelfixpreis(fixpreis);
									mngstfDto.setFArtikelstandardrabattsatz(rabatt);
									mngstfDto.setNMenge(menge);

									getKundesokoFac().createKundesokomengenstaffel(mngstfDto, theClientDto);

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

		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String importiereVKMengenstgaffelXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			String cBegruendung, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			Workbook workbook = Workbook.getWorkbook(is);

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

			if (!hmVorhandeneSpalten.containsKey(VKSTAFFEL_IMPORT_ARTIKELNUMMER)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_FIXPREIS)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_RABATT)
					|| !hmVorhandeneSpalten.containsKey(VKSTAFFEL_IMPORT_MENGE)
					|| !hmVorhandeneSpalten.containsKey(KUNDESOKO_IMPORT_GUELTIG_AB)) {
				rueckgabe += "Es muessen die Spalten 'Artikelnummer/Menge/Fixpreis/Rabatt/GueltigAb' vorhanden sein"
						+ new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);
						fehlerZeileXLSImport = "";

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								VKSTAFFEL_IMPORT_ARTIKELNUMMER, 40, i);

						if (artikelnummer == null || artikelnummer.length() == 0) {
							rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);
						if (aDto == null) {
							rueckgabe += "Die Artikelnummer '" + artikelnummer
									+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Gueltigab

						java.util.Date gueltigab = getDateAusXLS(sZeile, hmVorhandeneSpalten,
								VKSTAFFEL_IMPORT_GUELTIG_AB, i);

						if (gueltigab == null) {
							rueckgabe += "Die Spalte GueltigAb darf nicht leer sein. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

						gueltigab = Helper.cutDate(gueltigab);

						java.util.Date gueltigbis = getDateAusXLS(sZeile, hmVorhandeneSpalten,
								VKSTAFFEL_IMPORT_GUELTIG_BIS, i);

						// Menge

						BigDecimal menge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, VKSTAFFEL_IMPORT_MENGE, i);

						if (menge == null) {
							rueckgabe += "Die Spalte Menge darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Preis/Rabatt

						BigDecimal fixpreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								VKSTAFFEL_IMPORT_FIXPREIS, i);

						Double rabatt = getDoubleAusXLS(sZeile, hmVorhandeneSpalten, VKSTAFFEL_IMPORT_RABATT, i);

						if (fixpreis == null && rabatt == null) {
							rueckgabe += "Es muss zumindest eine der beiden Spalten 'Fixpreis' bzw. 'Rabatt' befuellt sein. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						if (fixpreis != null && rabatt != null) {
							rueckgabe += "Es muss entweder die Spalte 'Fixpreis', oder die Spalte 'Rabatt' befuellt sein. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						if (fixpreis != null) {
							rabatt = null;
						}

						if (rabatt == null) {
							rabatt = 0D;
						}

						String preisliste = getStringAusXLS(sZeile, hmVorhandeneSpalten, VKSTAFFEL_IMPORT_PREISLISTE,
								40, i);

						Short allePreislisten = Helper.boolean2Short(false);

						if (hmVorhandeneSpalten.containsKey(VKSTAFFEL_IMPORT_WIRKT_FUER_ALLE_PREISLITEN)) {
							allePreislisten = getShortAusXLS(sZeile, hmVorhandeneSpalten,
									VKSTAFFEL_IMPORT_WIRKT_FUER_ALLE_PREISLITEN, i);
						}

						if (allePreislisten == null) {
							allePreislisten = Helper.boolean2Short(false);
						}

						Integer preislisteIId = null;
						if (preisliste != null) {
							Vkpfartikelpreisliste oPreisliste = null;
							try {
								Query query = em.createNamedQuery("VkpfartikelpreislistefindByMandantCNrAndCNr");
								query.setParameter(1, theClientDto.getMandant());
								query.setParameter(2, preisliste);
								oPreisliste = (Vkpfartikelpreisliste) query.getSingleResult();

								preislisteIId = oPreisliste.getIId();

							} catch (NoResultException ex) {

								rueckgabe += "Die Preisliste " + preisliste + " konnte nicht gefunden werden . Zeile "
										+ i + new String(CRLFAscii);
								continue;

							}
						}

						if (Helper.short2boolean(allePreislisten) == true && preislisteIId != null) {
							rueckgabe += "Die Spalte WirktFuerAllePreislisten darf nur 1 sein, wenn keine Preisliste angegeben ist. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						VkpfMengenstaffelDto vkpfMengenstaffelDto = null;
						try {
							vkpfMengenstaffelDto = getVkPreisfindungFac().vkpfMengenstaffelFindByUniqueKey(
									aDto.getIId(), menge, new java.sql.Date(gueltigab.getTime()), preislisteIId);
						} catch (Throwable e) {
							// Nicht vorhanden
						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler == true) {

							if (vkpfMengenstaffelDto == null) {

								vkpfMengenstaffelDto = new VkpfMengenstaffelDto();
								vkpfMengenstaffelDto.setArtikelIId(aDto.getIId());
								vkpfMengenstaffelDto.setNMenge(menge);

								vkpfMengenstaffelDto.setCBemerkung(cBegruendung);

								vkpfMengenstaffelDto.setVkpfartikelpreislisteIId(preislisteIId);

								vkpfMengenstaffelDto.setBAllepreislisten(allePreislisten);

								vkpfMengenstaffelDto.setTPreisgueltigab(new java.sql.Date(gueltigab.getTime()));
								if (gueltigbis != null) {
									vkpfMengenstaffelDto.setTPreisgueltigbis(new java.sql.Date(gueltigbis.getTime()));
								}

								vkpfMengenstaffelDto.setNArtikelfixpreis(fixpreis);
								vkpfMengenstaffelDto.setFArtikelstandardrabattsatz(rabatt);

								getVkPreisfindungFac().createVkpfMengenstaffel(vkpfMengenstaffelDto, theClientDto);
							} else {

								if (gueltigbis != null) {
									vkpfMengenstaffelDto.setTPreisgueltigbis(new java.sql.Date(gueltigbis.getTime()));
								}
								vkpfMengenstaffelDto.setNArtikelfixpreis(fixpreis);
								vkpfMengenstaffelDto.setFArtikelstandardrabattsatz(rabatt);
								vkpfMengenstaffelDto.setCBemerkung(cBegruendung);

								getVkPreisfindungFac().updateVkpfMengenstaffel(vkpfMengenstaffelDto, theClientDto);

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

	public String importiereLagerminSollXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			Workbook workbook = Workbook.getWorkbook(is);

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

			if (!hmVorhandeneSpalten.containsKey(LAGERMINSOLL_IMPORT_ARTIKELNUMMER)
					|| !hmVorhandeneSpalten.containsKey(LAGERMINSOLL_IMPORT_LAGER)
					|| !hmVorhandeneSpalten.containsKey(LAGERMINSOLL_IMPORT_MIN)
					|| !hmVorhandeneSpalten.containsKey(LAGERMINSOLL_IMPORT_SOLL)) {
				rueckgabe += "Es muessen die Spalten 'Artikelnummer/Lager/Mindeststand/Sollstand' vorhanden sein"
						+ new String(CRLFAscii);
			} else {

				if (sheet.getRows() > 1) {

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] sZeile = sheet.getRow(i);
						fehlerZeileXLSImport = "";

						System.out.println("Zeile " + i + " von " + sheet.getRows());

						String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								LAGERMINSOLL_IMPORT_ARTIKELNUMMER, 40, i);

						if (artikelnummer == null || artikelnummer.length() == 0) {
							rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);
						if (aDto == null) {
							rueckgabe += "Die Artikelnummer '" + artikelnummer
									+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						// Lagger

						String lager = getStringAusXLS(sZeile, hmVorhandeneSpalten, LAGERMINSOLL_IMPORT_LAGER, 40, i);

						if (lager == null) {
							rueckgabe += "Die Spalte 'Lager' darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
						}

						LagerDto lDto = getLagerFac().lagerFindByCNrByMandantCNrOhneExc(lager,
								theClientDto.getMandant());

						if (lDto == null) {
							rueckgabe += "Das Lager '" + lager + "' konnte nicht gefunden werden. Zeile " + i
									+ new String(CRLFAscii);
							continue;
						}

						// Preis/Rabatt

						Double soll = getDoubleAusXLS(sZeile, hmVorhandeneSpalten, LAGERMINSOLL_IMPORT_SOLL, i);

						Double min = getDoubleAusXLS(sZeile, hmVorhandeneSpalten, LAGERMINSOLL_IMPORT_MIN, i);

						if (soll == null && min == null) {
							rueckgabe += "Es muss zumindest eine der beiden Spalten 'Mindeststand' bzw. 'Sollstand' befuellt sein. Zeile "
									+ i + new String(CRLFAscii);
							continue;
						}

						Artikellager artikellager = null;

						try {
							Query query = em.createNamedQuery("ArtikellagerfindByArtikelIIdLagerIId");

							query.setParameter(1, aDto.getIId());
							query.setParameter(2, lDto.getIId());

							artikellager = (Artikellager) query.getSingleResult();

							// Dann Update

						} catch (NoResultException ex1) {
							// nothing here

						}

						if (fehlerZeileXLSImport.length() > 0) {
							rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
						}

						if (bImportierenWennKeinFehler == true) {

							if (artikellager == null) {

								artikellager = new Artikellager(new ArtikellagerPK(aDto.getIId(), lDto.getIId()),
										theClientDto.getMandant());

								artikellager.setFLagermindest(min);
								artikellager.setFLagersoll(soll);
							} else {

								if (min != null) {
									artikellager.setFLagermindest(min);
								}
								if (soll != null) {
									artikellager.setFLagersoll(soll);
								}

							}

							em.merge(artikellager);
							em.flush();

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

	public String importiereAllergeneXLS(byte[] xlsDatei, Integer lieferantIId, TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			Workbook workbook = Workbook.getWorkbook(is);

			Sheet sheet = workbook.getSheet(0);

			AlergenDto[] alergeneDto = getArtikelFac().allergenFindByMandantCNr(theClientDto);

			HashMap<String, Integer> hmAllergene = new HashMap<String, Integer>();

			for (int i = 0; i < alergeneDto.length; i++) {
				hmAllergene.put(alergeneDto[i].getCBez(), alergeneDto[i].getIId());
			}

			if (sheet.getRows() > 1) {

				Cell[] sZeileUeberschriften = sheet.getRow(1);

				for (int i = 2; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);
					fehlerZeileXLSImport = "";

					if (sZeile.length > 0) {

						String lieferantenArtikelnummer = sZeile[0].getContents();

						Query queryAL = em.createNamedQuery("ArtikellieferantfindByCArtikelnrlieferantLieferantIId");
						queryAL.setParameter(1, lieferantenArtikelnummer);
						queryAL.setParameter(2, lieferantIId);

						Collection cArtikellieferant = queryAL.getResultList();

						if (cArtikellieferant.size() > 0) {
							Artikellieferant al = (Artikellieferant) cArtikellieferant.iterator().next();

							Integer artikelIId = al.getArtikelIId();
							// Zuerst alle vorhandene Allergene loeschen

							Query query = em.createNamedQuery("ArtikelalergenfindByArtikelIId");
							query.setParameter(1, artikelIId);

							Collection c = query.getResultList();
							Iterator it = c.iterator();
							while (it.hasNext()) {
								Artikelalergen aa = (Artikelalergen) it.next();
								em.remove(aa);
							}

							// Nun alle wieder anlegen

							if (sZeile.length > 1) {

								for (int u = 2; u < sZeile.length; u++) {

									if (sZeile[u].getContents() != null
											&& sZeile[u].getContents().trim().toUpperCase().equals("X")) {
										// Alergen anlegen

										if (sZeileUeberschriften.length >= u
												&& sZeileUeberschriften[u].getContents() != null
												&& sZeileUeberschriften[u].getContents().trim().length() > 0) {

											if (hmAllergene.containsKey(sZeileUeberschriften[u].getContents().trim())) {

												Integer alergenIId = hmAllergene
														.get(sZeileUeberschriften[u].getContents().trim());

												// Artikelalergen anlegen
												ArtikelalergenDto aaDto = new ArtikelalergenDto();
												aaDto.setAlergenIId(alergenIId);
												aaDto.setArtikelIId(artikelIId);

												getArtikelFac().createArtikelallergen(aaDto, theClientDto);

											} else {
												// Alergen ebenfalls anlegen?
											}

										}

									}

								}

							}

						} else {
							rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
							continue;
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

	private String fehlerZeileXLSImport = "";

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArtikelimportFehlerDto pruefeUndImportiereArtikelXLS(byte[] xlsDatei, java.sql.Timestamp tDefaultEK,
			java.sql.Timestamp tDefaultVK, boolean bImportierenWennKeinFehler, boolean bPreisUeberschreiben,
			boolean bAlsLief1Reihen, boolean bBestehendeartikelUeberschreiben, boolean bFreigabestatusIgnorieren,
			TheClientDto theClientDto) {

		ArtikelimportFehlerDto fehlerDto = new ArtikelimportFehlerDto();

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {

			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = getVkPreisfindungFac()
					.getAlleAktivenPreislisten(Helper.boolean2Short(true), theClientDto);

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

			String default_artikelart = null;
			String default_artikeleinheit = null;
			Integer default_mwstsaztIId = null;
			Integer lagerIId_Hauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART);

			default_artikelart = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

			if (default_mwstsaztIId != null) {
				HvOptional<MwstsatzbezDto> mwstsatz = getMandantFac()
						.mwstsatzbezFindByPrimaryKeyOhneExc(default_mwstsaztIId, theClientDto);
				if (!mwstsatz.isPresent()) {
					// nicht gefunden
					rueckgabe += "Es konnte kein Mehrwertsteuersatz mit ID " + default_mwstsaztIId
							+ " gefunden werden, dieser ist aber als Parameter DEFAULT_ARTIKEL_MWSTSATZ definiert."
							+ System.lineSeparator();
					default_mwstsaztIId = null;
				}
			}

			boolean bArtikelfreigabe = getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE, theClientDto);
			boolean bHerstellerkopplung = getMandantFac().hatZusatzfunktionberechtigung(
					com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG, theClientDto);
			// Artikelnummer muss immer vorhanden sein
			if (!hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ARTIKELNUMMER)) {
				rueckgabe += "Es muss zumindest die Spalte 'Artikelnummer' vorhanden sein" + new String(CRLFAscii);
			} else {

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);
					fehlerZeileXLSImport = "";

					System.out.println("Zeile " + (i + 1) + " von " + sheet.getRows());

					Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(XLS_IMPORT_SPALTE_ARTIKELNUMMER);

					String artikelnummer = null;
					if (sZeile.length == 0) {
						continue;

					}
					artikelnummer = sZeile[iSpalteArtikelnummer].getContents();

					String herstellernummer = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_HERSTELLERNUMMER, ArtikelFac.MAX_ARTIKEL_HERSTELLERNR, i);

					parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);

					int maximalLaengeArtikelnr = parameter.asInteger();

					if (artikelnummer == null || artikelnummer.length() == 0) {

						if (herstellernummer != null) {

						} else {
							rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + (i + 1)
									+ new String(CRLFAscii);

							continue;
						}

					} else if (artikelnummer.length() > maximalLaengeArtikelnr) {
						rueckgabe += "Die Artikelnummer darf maximal " + maximalLaengeArtikelnr
								+ " Zeichen lang sein, ist aber " + artikelnummer.length() + " (" + artikelnummer
								+ "). Zeile " + (i + 1) + new String(CRLFAscii);
						continue;
					}

					ArtikelDto artikelDto = null;

					String herstellerName = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HERSTELLER,
							40, i);
					HerstellerDto herstellerDto = null;
					if (herstellerName != null) {
						Integer herstellerIId = herstellerSuchenUndAnlegen(theClientDto, herstellerName);
						herstellerDto = getArtikelFac().herstellerFindByPrimaryKey(herstellerIId, theClientDto);
					}

					if ((artikelnummer == null || artikelnummer.length() == 0) && herstellernummer != null) {

						List<ArtikelDto> vorhandeneArtikel = getArtikelFac()
								.artikelFindByArtikelnrHersteller(herstellernummer, theClientDto);

						if (vorhandeneArtikel.size() > 0) {
							artikelDto = vorhandeneArtikel.iterator().next();
						} else {

							// Diese alle an Client-Zwischenablage uebergeben
							fehlerDto.addHerstellernrNichtGefunden(herstellernummer);
							rueckgabe += "Es konnte kein Artikel mit der Herstellernummer '" + herstellernummer
									+ "' gefunden werden. Zeile " + (i + 1) + new String(CRLFAscii);

							continue;
						}

					} else if (bHerstellerkopplung && herstellerDto != null) {
						List<Artikel> artikelList = ArtikelQuery.listByCNrHerstellerkopplung(em, artikelnummer,
								herstellerDto.getCNr());
						if (!artikelList.isEmpty()) {
							artikelDto = getArtikelFac().artikelFindByPrimaryKeyOhneExc(artikelList.get(0).getIId(),
									theClientDto);
						}

					} else {
						artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);
					}

					if (artikelDto != null) {
						// PJ20562
						if (bBestehendeartikelUeberschreiben == false) {
							continue;
						}

						artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelDto.getIId(), theClientDto);

						// PJ21596
						if (bArtikelfreigabe && artikelDto.getTFreigabe() != null) {

							if (bFreigabestatusIgnorieren == false) {
								rueckgabe += "Der Artikel '" + artikelDto.getCNr()
										+ "' ist freigegeben und darf nicht mehr veraendert werden. Zeile " + (i + 1)
										+ new String(CRLFAscii);
								continue;
							}
						}

					} else {
						artikelDto = new ArtikelDto();
						artikelDto.setCNr(artikelnummer);
					}

					if (artikelDto.getMwstsatzbezIId() == null) {
						artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
					}

					// Bezeichnungen
					if (artikelDto.getArtikelsprDto() == null) {
						artikelDto.setArtikelsprDto(new ArtikelsprDto());
					} else {
						// SO8057
						if (!artikelDto.getArtikelsprDto().getLocaleCNr().equals(theClientDto.getLocUiAsString())) {
							artikelDto.getArtikelsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
							artikelDto.getArtikelsprDto().setCBez(null);
							artikelDto.getArtikelsprDto().setCKbez(null);
							artikelDto.getArtikelsprDto().setCZbez(null);
							artikelDto.getArtikelsprDto().setCZbez2(null);

						}
					}

					int iLaengenBezeichnung = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BEZ)) {
						artikelDto.getArtikelsprDto().setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_BEZ, iLaengenBezeichnung, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_KBEZ)) {
						artikelDto.getArtikelsprDto().setCKbez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_KBEZ, ArtikelFac.MAX_ARTIKEL_KURZBEZEICHNUNG, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ZBEZ)) {
						artikelDto.getArtikelsprDto().setCZbez(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_ZBEZ, iLaengenBezeichnung, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ZBEZ2)) {
						artikelDto.getArtikelsprDto().setCZbez2(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_ZBEZ2, iLaengenBezeichnung, i));
					}

					// PJ21607
					java.util.Map mLocales = getLocaleFac().getAllLocales(theClientDto.getLocUi());

					Iterator it = mLocales.keySet().iterator();

					java.util.Map<String, ArtikelsprDto> bezeichnungenFremd = new java.util.HashMap<String, ArtikelsprDto>();

					while (it.hasNext()) {

						String locale = (String) it.next();

						if (!locale.equals(theClientDto.getLocUiAsString())) {

							ArtikelsprDto sprDto = new ArtikelsprDto();
							sprDto.setLocaleCNr(locale);

							String spalteBez = locale.trim() + "_" + XLS_IMPORT_SPALTE_BEZ;
							String spalteKBez = locale.trim() + "_" + XLS_IMPORT_SPALTE_KBEZ;
							String spalteZBez = locale.trim() + "_" + XLS_IMPORT_SPALTE_ZBEZ;
							String spalteZBez2 = locale.trim() + "_" + XLS_IMPORT_SPALTE_ZBEZ2;

							boolean bZumindestEineSpalteVorhanden = false;
							if (hmVorhandeneSpalten.containsKey(spalteBez)) {
								bZumindestEineSpalteVorhanden = true;
								sprDto.setCBez(getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteBez,
										iLaengenBezeichnung, i));
							}

							if (hmVorhandeneSpalten.containsKey(spalteKBez)) {
								bZumindestEineSpalteVorhanden = true;
								sprDto.setCKbez(getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteKBez,
										iLaengenBezeichnung, i));
							}

							if (hmVorhandeneSpalten.containsKey(spalteZBez)) {
								bZumindestEineSpalteVorhanden = true;
								sprDto.setCZbez(getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteZBez,
										iLaengenBezeichnung, i));
							}

							if (hmVorhandeneSpalten.containsKey(spalteZBez2)) {
								bZumindestEineSpalteVorhanden = true;
								sprDto.setCZbez2(getStringAusXLS(sZeile, hmVorhandeneSpalten, spalteZBez2,
										iLaengenBezeichnung, i));
							}
							if (bZumindestEineSpalteVorhanden == true) {
								bezeichnungenFremd.put(locale, sprDto);
							}

						}

					}

					// Artikeleigenschaften
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_REFERENZNUMMER)) {
						artikelDto.setCReferenznr(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_REFERENZNUMMER, ArtikelFac.MAX_ARTIKEL_REFERENZNUMMER, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HERSTELLERNUMMER)) {

						String artikelnrhersteller = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_HERSTELLERNUMMER, ArtikelFac.MAX_ARTIKEL_HERSTELLERNR, i);
						if (artikelnrhersteller != null) {
							artikelDto.setCArtikelnrhersteller(artikelnrhersteller);
						}
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HERSTELLERBEZEICHNUNG)) {

						String artikelbezhersteller = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_HERSTELLERBEZEICHNUNG, ArtikelFac.MAX_ARTIKEL_HERSTELLERBEZEICHNUNG,
								i);
						if (artikelbezhersteller != null) {
							artikelDto.setCArtikelbezhersteller(artikelbezhersteller);
						}
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_INDEX)) {
						artikelDto.setCIndex(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_INDEX, 15, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_REVISION)) {
						artikelDto.setCRevision(getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_REVISION,
								ArtikelFac.MAX_ARTIKEL_REVISION, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ARTIKELART)) {

						String artikelart = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELART,
								15, i);

						if (artikelart != null) {
							if (artikelart.trim().equals(ArtikelFac.ARTIKELART_ARTIKEL.trim())
									|| artikelart.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT.trim())) {
								artikelDto.setArtikelartCNr(artikelart);
							} else {
								rueckgabe += "Die Artikelart muss 'Artikel' oder 'Arbeitszeit' sein. Zeile " + (i + 1)
										+ new String(CRLFAscii);
								continue;
							}
						}

					}

					if (artikelDto.getArtikelartCNr() == null) {
						artikelDto.setArtikelartCNr(default_artikelart);
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_MENGENEINHEIT)) {

						String einheit = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MENGENEINHEIT,
								15, i);
						if (einheit != null) {
							artikelDto.setEinheitCNr(einheit);
						}
					}

					if (artikelDto.getEinheitCNr() == null) {
						artikelDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					}

					artikelDto.setEinheitCNrBestellung(getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_BESTELLEMGNENEINHEIT, 15, i));

					artikelDto.setNUmrechnungsfaktor(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_UMRECHNUNGSFAKTOR_BESTELLEMGNENEINHEIT, i));

					if ((artikelDto.getEinheitCNrBestellung() != null && artikelDto.getNUmrechnungsfaktor() == null)
							|| (artikelDto.getEinheitCNrBestellung() == null
									&& artikelDto.getNUmrechnungsfaktor() != null)) {

						fehlerZeileXLSImport += "Wenn eine Bestellmengeneinheit definiert ist, muss auch ein Umrechnungsfaktor definiert sein, bzw. umgekehrt. Zeile "
								+ i + new String(CRLFAscii);

					}

					if (artikelDto.getGeometrieDto() == null) {
						artikelDto.setGeometrieDto(new GeometrieDto());
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BREITE)) {
						artikelDto.getGeometrieDto()
								.setFBreite(getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BREITE, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BREITE_TEXT)) {
						artikelDto.getGeometrieDto().setCBreitetext(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BREITE_TEXT, 2, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HOEHE)) {
						artikelDto.getGeometrieDto()
								.setFHoehe(getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HOEHE, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_TIEFE)) {
						artikelDto.getGeometrieDto()
								.setFTiefe(getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_TIEFE, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_GEWICHT)) {
						artikelDto.setFGewichtkg(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_GEWICHT, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_MATERIALGEWICHT)) {
						artikelDto.setFMaterialgewicht(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MATERIALGEWICHT, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LAGERMINDESTSTAND)) {

						artikelDto.setFLagermindest(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LAGERMINDESTSTAND, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_LAGERSOLLSTAND)) {

						artikelDto.setFLagersoll(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LAGERSOLLSTAND, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_FERTIGUNGSSATZGROESSE)) {
						artikelDto.setFFertigungssatzgroesse(getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_FERTIGUNGSSATZGROESSE, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERSCHNITTFAKTOR)) {
						artikelDto.setFVerschnittfaktor(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VERSCHNITTFAKTOR, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERSCHNITTBASIS)) {
						artikelDto.setFVerschnittbasis(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VERSCHNITTBASIS, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_UEBERPRODUKTION)) {
						artikelDto.setFUeberproduktion(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_UEBERPRODUKTION, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_WARENVERKEHRSNUMMMER)) {
						artikelDto.setCWarenverkehrsnummer(getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_WARENVERKEHRSNUMMMER, 10, i));
					}

					// PJ18763

					if (artikelDto.getMontageDto() == null) {
						artikelDto.setMontageDto(new MontageDto());
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_RASTER_STEHEND)) {
						artikelDto.getMontageDto().setFRasterstehend(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_RASTER_STEHEND, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_RASTER_LIEGEND)) {
						artikelDto.getMontageDto().setFRasterliegend(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_RASTER_LIEGEND, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_STROMVERBRAUCH_TYPISCH)) {
						artikelDto.setFStromverbrauchtyp(getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_STROMVERBRAUCH_TYPISCH, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_STROMVERBRAUCH_MAXIMAL)) {
						artikelDto.setFStromverbrauchmax(getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_STROMVERBRAUCH_MAXIMAL, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_AUFSCHLAG_PROZENT)) {
						artikelDto.setFAufschlagProzent(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_AUFSCHLAG_PROZENT, i));
					}

					// PJ19744
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERPACKUNGSMITTELMENGE)) {
						BigDecimal bd = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_VERPACKUNGSMITTELMENGE, i);
						if (bd != null) {
							artikelDto.setNVerpackungsmittelmenge(bd);
						}
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERPACKUNGSMITTEL)) {

						String verpackungsmittel = getStringAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_VERPACKUNGSMITTEL, 15, i);

						if (verpackungsmittel != null) {
							VerpackungsmittelDto vmDto = getArtikelFac()
									.verpackungsmittelfindByCNrMandantCNrOhneExc(verpackungsmittel, theClientDto);

							if (vmDto != null) {
								artikelDto.setVerpackungsmittelIId(vmDto.getIId());
							} else {
								fehlerZeileXLSImport += "Verpackungsmittel '" + verpackungsmittel
										+ "' ist nicht vorhanden. Zeile " + (i + 1) + new String(CRLFAscii);

							}

						}

					}
					/*
					 * SP9041 es soll jetzt auch gehen, wenn nur eines definiert ist if
					 * ((artikelDto.getNVerpackungsmittelmenge() != null &&
					 * artikelDto.getVerpackungsmittelIId() == null) ||
					 * (artikelDto.getNVerpackungsmittelmenge() == null &&
					 * artikelDto.getVerpackungsmittelIId() != null)) {
					 * 
					 * fehlerZeileXLSImport +=
					 * "Wenn ein Verpackungsmittel definiert ist, muss auch eine Verpackungsmittelmenge definiert sein, bzw. umgekehrt. Zeile "
					 * + i + new String(CRLFAscii); }
					 */

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERPACKUNGSMENGE)) {
						artikelDto.setFVerpackungsmenge(
								getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VERPACKUNGSMENGE, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_AUFSCHLAG_BETRAG)) {
						artikelDto.setNAufschlagBetrag(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_AUFSCHLAG_BETRAG, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HOCHSETZEN)) {
						artikelDto.getMontageDto().setBHochsetzen(
								getShortAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HOCHSETZEN, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HOCHSTELLEN)) {
						artikelDto.getMontageDto().setBHochstellen(
								getShortAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HOCHSTELLEN, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_POLARISIERT)) {
						artikelDto.getMontageDto().setBPolarisiert(
								getShortAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_POLARISIERT, i));
					}

					if (artikelDto.getVerpackungDto() == null) {
						artikelDto.setVerpackungDto(new VerpackungDto());
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BAUFORM)) {
						artikelDto.getVerpackungDto().setCBauform(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BAUFORM, 20, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ECCN)) {
						artikelDto
								.setCEccn(getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ECCN, 20, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VK_EAN)) {
						artikelDto.setCVerkaufseannr(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VK_EAN, 15, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VP_EAN)) {
						artikelDto.setCVerpackungseannr(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VP_EAN, 15, i));
					}

					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_VERPACKUNGSART)) {
						artikelDto.getVerpackungDto().setCVerpackungsart(
								getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VERPACKUNGSART, 20, i));
					}
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_UL)) {
						artikelDto.setCUL(getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_UL, 40, i));
					}

					Short chargengefuehrt = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_CHARGENNUMMERNGEFUEHRT, i);

					Short snrtragend = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_SERIENNUMMERNGEFUEHRT, i);

					Short lagerbewirtschaftet = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_LAGERBEWIRTSCHAFTET, i);

					Short kommissionieren = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_KOMMISSIONIEREN, i);

					Double mindesteckungsbeitrag = getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MINDESTDECKUNGSBEITRAG, i);

					Short antistatic = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_ANTISTATICMASSNAHMEN, i);

					if (artikelDto.getIId() == null) {

						artikelDto.setFMindestdeckungsbeitrag(mindesteckungsbeitrag);
						artikelDto.setBChargennrtragend(chargengefuehrt);
						artikelDto.setBSeriennrtragend(snrtragend);
						artikelDto.setBLagerbewirtschaftet(lagerbewirtschaftet);
						artikelDto.setBKommissionieren(kommissionieren);
						artikelDto.setBAntistatic(antistatic);
					} else {
						if (chargengefuehrt != null) {
							artikelDto.setBChargennrtragend(chargengefuehrt);
						}
						if (snrtragend != null) {
							artikelDto.setBSeriennrtragend(snrtragend);
						}
						if (lagerbewirtschaftet != null) {
							artikelDto.setBLagerbewirtschaftet(lagerbewirtschaftet);
						}
						if (kommissionieren != null) {
							artikelDto.setBKommissionieren(kommissionieren);
						}
						if (mindesteckungsbeitrag != null) {
							artikelDto.setFMindestdeckungsbeitrag(mindesteckungsbeitrag);
						}
						if (antistatic != null) {
							artikelDto.setBAntistatic(antistatic);
						}

					}
					// /
					String artikelklasse = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELKLASSE,
							15, i);

					String artikelgruppe = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELGRUPPE,
							15, i);

					String mwstbez = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MWSTSATZBEZ, 40, i);

					if (mwstbez != null) {

						try {
							// Mwstsatzbez suchen
							Query query = em.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
							query.setParameter(1, theClientDto.getMandant());
							query.setParameter(2, mwstbez);
							Mwstsatzbez doppelt = (Mwstsatzbez) query.getSingleResult();
							artikelDto.setMwstsatzbezIId(doppelt.getIId());
						} catch (NoResultException ex) {

							fehlerZeileXLSImport += "Mwstsatzbez " + mwstbez + " nicht vorhanden. Zeile " + (i + 1)
									+ new String(CRLFAscii);

						}

					}

					BigDecimal vkPreisbasis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_VERKAUFSPREISBASIS, i);

					// Lagerplatz
					String lagerplatz = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_LAGERPLATZ_HAUPTLAGER, 40, i);
					// Material
					String material = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MATERIAL, 50, i);

					// Material
					String reach = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_REACH, 80, i);
					// Material
					String rohs = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ROHS, 80, i);
					// Material
					String automotive = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_AUTOMOTIVE, 80,
							i);
					// Material
					String medical = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MEDICAL, 80, i);
					// Material
					String farbcode = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_FARBCODE, 15, i);

					// Liefergruppe
					String liefergruppe = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_LIEFERGRUPPE,
							40, i);

					// Artikellieferant
					String artikellieferantKBEZ = getStringAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_ARTIKELLIEFERANT, 40, i);

					if (artikellieferantKBEZ != null) {

						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELNUMMER_LIEFERANT, 40, i);
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELBEZEICHNUNG_LIEFERANT, 80,
								i);

						BigDecimal einzelpreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_EINKAUFSPREIS_LIEFERANT, i);
						BigDecimal rabatt = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_RABATT_LIEFERANT, i);
						BigDecimal nettopreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_NETTOPREIS_LIEFERANT, i);

						// lt. WH muss auch Lieferant ohne Preise importiert
						// werden
						/*
						 * if (einzelpreis == null && nettopreis == null) { fehlerZeileXLSImport +=
						 * " Wenn ein Lieferant definiert ist, muss auch Einzelpreis oder Nettopeis definiert sein. Zeile "
						 * + i + new String(CRLFAscii); }
						 */

						getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_WIEDERBESCHAFFUNGSZEIT_LIEFERANT, i);
						getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MINDESTBESTELLMENGE_LIEFERANT,
								i);
						getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_VERPACKUNGSEINHEIT_LIEFERANT,
								i);
						getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_STANDARDMENGE_LIEFERANT, i);

					}

					// PJ18655
					BigDecimal preiseinheit = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_PREISEINHEIT, i);

					// PJ18654
					String kommentarart = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_KOMMENTARART,
							15, i);

					String kommentar = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_KOMMENTAR, 9999,
							i);

					if (kommentar != null && kommentar.length() > 0 && kommentarart == null) {
						fehlerZeileXLSImport += " Wenn ein Kommentar definiert ist, muss eine Kommentarart definiert sein. Zeile "
								+ (i + 1) + new String(CRLFAscii);
					}

					ArrayList alBelege = new ArrayList();

					Short mitruckenAnfrage = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_ANFRAGE, i);
					if (Helper.short2boolean(mitruckenAnfrage) == true) {
						alBelege.add(LocaleFac.BELEGART_ANFRAGE);
					}

					Short mitruckenAngebot = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_ANGEBOT, i);
					if (Helper.short2boolean(mitruckenAngebot) == true) {
						alBelege.add(LocaleFac.BELEGART_ANGEBOT);
					}

					Short mitruckenAuftrag = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_AUFTRAG, i);
					if (Helper.short2boolean(mitruckenAuftrag) == true) {
						alBelege.add(LocaleFac.BELEGART_AUFTRAG);
					}

					Short mitruckenBestellung = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_BESTELLUNG, i);
					if (Helper.short2boolean(mitruckenBestellung) == true) {
						alBelege.add(LocaleFac.BELEGART_BESTELLUNG);
					}

					Short mitruckenEingangsrechnung = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_EINGANGSRECHNUNG, i);
					if (Helper.short2boolean(mitruckenEingangsrechnung) == true) {
						alBelege.add(LocaleFac.BELEGART_EINGANGSRECHNUNG);
					}

					Short mitruckenFertigung = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_FERTIGUNG, i);
					if (Helper.short2boolean(mitruckenFertigung) == true) {
						alBelege.add(LocaleFac.BELEGART_LOS);
					}

					Short mitruckenLieferschein = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_LIEFERSCHEIN, i);
					if (Helper.short2boolean(mitruckenLieferschein) == true) {
						alBelege.add(LocaleFac.BELEGART_LIEFERSCHEIN);
					}
					Short mitruckenRechnung = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_RECHNUNG, i);
					if (Helper.short2boolean(mitruckenRechnung) == true) {
						alBelege.add(LocaleFac.BELEGART_RECHNUNG);
					}
					Short mitrucken_Stueckliste = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_STUECKLISTE, i);
					if (Helper.short2boolean(mitrucken_Stueckliste) == true) {
						alBelege.add(LocaleFac.BELEGART_STUECKLISTE);
					}
					Short mitruckenWareneingang = getShortAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_IMPORT_SPALTE_MITDRUCKEN_WARENEINGANG, i);
					if (Helper.short2boolean(mitruckenWareneingang) == true) {
						alBelege.add(LocaleFac.BELEGART_WARENEINGANG);
					}

					if (fehlerZeileXLSImport.length() > 0) {
						rueckgabe += fehlerZeileXLSImport + new String(CRLFAscii);
					}

					if (bImportierenWennKeinFehler) {

						// Artikelgruppe anlegen

						if (artikelgruppe != null) {

							artikelDto.setArtgruIId(
									getArtikelimportFac().artikelgruppeSuchenUndAnlegen(theClientDto, artikelgruppe));

						}
						// Artikelklasse anlegen
						if (artikelklasse != null) {
							artikelDto.setArtklaIId(
									getArtikelimportFac().artikelklasseSuchenUndAnlegen(theClientDto, artikelklasse));
						}

						getArtikelimportFac().einheitSuchenUndAnlegen(theClientDto, artikelDto.getEinheitCNr());

						// Bestellmengeneinheit anlegen
						if (artikelDto.getEinheitCNrBestellung() != null) {
							getArtikelimportFac().einheitSuchenUndAnlegen(theClientDto,
									artikelDto.getEinheitCNrBestellung());
						}

						// Material anlegen
						if (material != null) {
							artikelDto.setMaterialIId(
									getArtikelimportFac().materialSuchenUndAnlegen(theClientDto, material));
						}

						if (liefergruppe != null) {
							artikelDto.setLfliefergruppeIId(liefergruppeSuchenUndAnlegen(theClientDto, liefergruppe));
						}

						// Reach anlegen
						if (reach != null) {
							artikelDto.setReachIId(getArtikelimportFac().reachSuchenUndAnlegen(theClientDto, reach));
						}
						// Rohs anlegen
						if (rohs != null) {
							artikelDto.setRohsIId(getArtikelimportFac().rohsSuchenUndAnlegen(theClientDto, rohs));
						}
						// Automotive anlegen
						if (automotive != null) {
							artikelDto.setAutomotiveIId(
									getArtikelimportFac().automotiveSuchenUndAnlegen(theClientDto, automotive));
						}
						// Medical anlegen
						if (medical != null) {
							artikelDto.setMedicalIId(
									getArtikelimportFac().medicalSuchenUndAnlegen(theClientDto, medical));
						}
						// Farbcode anlegen
						if (farbcode != null) {
							artikelDto.setFarbcodeIId(
									getArtikelimportFac().farbcodeSuchenUndAnlegen(theClientDto, farbcode));
						}

						// Hersteller anlegen
						if (herstellerDto != null) {

							artikelDto.setHerstellerIId(herstellerDto.getIId());

							if (bHerstellerkopplung) {
								ParametermandantDto paramArtCNRLaenge = getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);

								int maximallaenge = ((Integer) paramArtCNRLaenge.getCWertAsObject()).intValue();

								String c_nr = artikelDto.getCNr();
								c_nr = Helper.fitString2Length(c_nr, maximallaenge, ' ');
								artikelDto.setCNr(c_nr + herstellerDto.getCNr());
							}
						}

						Integer artikelIId = null;

						if (artikelDto.getIId() != null) {

							artikelIId = artikelDto.getIId();

							getArtikelFac().updateArtikel(artikelDto, theClientDto);
						} else {

							artikelDto.setBVersteckt(Helper.boolean2Short(false));

							artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
						}

						// PJ21607 Sprachabhaengige Bezeichnungen

						Iterator itLocale = bezeichnungenFremd.keySet().iterator();

						while (itLocale.hasNext()) {

							String locale = (String) itLocale.next();

							ArtikelsprDto sprDto = bezeichnungenFremd.get(locale);
							sprDto.setArtikelIId(artikelIId);

							ArtikelsprDto artikelSprVorhanden = getArtikelFac()
									.artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId, locale, theClientDto);
							if (artikelSprVorhanden == null) {
								getArtikelFac().createArtikelspr(sprDto, theClientDto);
							} else {
								getArtikelFac().updateArtikelspr(sprDto, theClientDto);
							}
						}

						// Kommentarart anlegen
						if (kommentarart != null && kommentar != null) {
							Short bDateiverweis = getShortAusXLS(sZeile, hmVorhandeneSpalten,
									XLS_IMPORT_SPALTE_DATEIVERWEIS, i);

							getArtikelimportFac().artikelkommentartAnlegen(theClientDto, kommentarart, kommentar,
									alBelege, artikelIId, bDateiverweis);

						}

						// VK-Preisbasis anlegen
						if (vkPreisbasis != null) {

							vkPreisbasis = getArtikelimportFac().vkPreisAnlegen(tDefaultVK, theClientDto, vkPreisbasis,
									preiseinheit, artikelIId, vkpfartikelpreislisteDtos);

						}

						// PJ18655
						BigDecimal gestehungspreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
								XLS_IMPORT_SPALTE_GESTEHUNGSPREIS, i);
						if (gestehungspreis != null) {

							rueckgabe = getArtikelimportFac().gestpreisImportieren(theClientDto, CRLFAscii, rueckgabe,
									artikelnummer, artikelDto, preiseinheit, artikelIId, gestehungspreis);

						}

						// Lagerplatz anlegen

						if (lagerplatz != null) {

							getArtikelimportFac().lagerplatzAnlegen(theClientDto, lagerIId_Hauptlager, lagerplatz,
									artikelIId);

						}

						// Artikellieferant anlegen

						if (artikellieferantKBEZ != null) {

							artikellieferantAnlegen(tDefaultEK, theClientDto, hmVorhandeneSpalten, i, sZeile,
									artikellieferantKBEZ, preiseinheit, artikelIId, bPreisUeberschreiben,
									bAlsLief1Reihen);

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		fehlerDto.setError(rueckgabe);

		return fehlerDto;
	}

	public void artikellieferantAnlegen(java.sql.Timestamp tDefaultEK, TheClientDto theClientDto,
			HashMap<String, Integer> hmVorhandeneSpalten, int i, Cell[] sZeile, String artikellieferantKBEZ,
			BigDecimal preiseinheit, Integer artikelIId, boolean bPreisUeberschreiben, boolean bAlsLief1Reihen)
			throws RemoteException {
		List<Partner> partners = PartnerQuery.listByKbez(em, artikellieferantKBEZ);

		PartnerDto pDto = null;
		if (partners.size() == 0) {
			pDto = new PartnerDto();
			pDto.setCKbez(artikellieferantKBEZ);
			pDto.setCName1nachnamefirmazeile1(artikellieferantKBEZ);
			pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
			pDto.setBVersteckt(false);
			pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

			Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

			pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

		} else {
			Partner partner = partners.get(0);
			pDto = getPartnerFac().partnerFindByPrimaryKey(partner.getIId(), theClientDto);
		}

		LieferantDto lfDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
				theClientDto.getMandant(), theClientDto);

		Integer lieferantIId = null;

		if (lfDto != null) {
			lieferantIId = lfDto.getIId();
		} else {
			LieferantDto lfDtoNeu = new LieferantDto();
			lfDtoNeu.setPartnerIId(pDto.getIId());
			lfDtoNeu.setPartnerDto(pDto);
			lfDtoNeu.setMandantCNr(theClientDto.getMandant());
			lfDtoNeu.setWaehrungCNr(theClientDto.getSMandantenwaehrung());

			MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			lfDtoNeu.setIdSpediteur(mDto.getSpediteurIIdLF());
			lfDtoNeu.setLieferartIId(mDto.getLieferartIIdLF());
			lfDtoNeu.setZahlungszielIId(mDto.getZahlungszielIIdLF());
			lfDtoNeu.setBIgErwerb(Helper.boolean2Short(false));

			lieferantIId = getLieferantFac().createLieferant(lfDtoNeu, theClientDto);
		}

		BigDecimal einzelpreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
				XLS_IMPORT_SPALTE_EINKAUFSPREIS_LIEFERANT, i);
		BigDecimal rabatt = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_RABATT_LIEFERANT, i);
		BigDecimal nettopreis = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_NETTOPREIS_LIEFERANT,
				i);

		if (preiseinheit != null && preiseinheit.doubleValue() != 0 && nettopreis != null) {
			nettopreis = nettopreis.divide(preiseinheit, 4, BigDecimal.ROUND_HALF_EVEN);
		}
		if (preiseinheit != null && preiseinheit.doubleValue() != 0 && einzelpreis != null) {
			einzelpreis = einzelpreis.divide(preiseinheit, 4, BigDecimal.ROUND_HALF_EVEN);
		}

		if (rabatt == null) {
			rabatt = BigDecimal.ZERO;
		}

		if (einzelpreis != null) {

			if (nettopreis == null) {
				nettopreis = einzelpreis.subtract(Helper.getProzentWert(einzelpreis, rabatt, 4));
			}

		} else {

			if (nettopreis != null) {

				if (rabatt.doubleValue() != 0) {
					// SP9391
					einzelpreis = nettopreis.divide(BigDecimal.ONE.subtract(new BigDecimal(rabatt.doubleValue())
							.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN)), 4,
							BigDecimal.ROUND_HALF_EVEN);
				} else {
					einzelpreis = nettopreis;
				}

			}
		}

		ArtikellieferantDto alDto = getArtikelFac().artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
				artikelIId, lieferantIId, tDefaultEK, theClientDto);

		Integer artikellieferantIId = null;

		if (alDto != null) {
			artikellieferantIId = alDto.getIId();
			if (bPreisUeberschreiben == true) {

				if (nettopreis == null && einzelpreis == null) {

				} else {
					alDto.setFRabatt(rabatt.doubleValue());
				}

				alDto.setNEinzelpreis(einzelpreis);

				alDto.setNNettopreis(nettopreis);

				alDto.setCArtikelnrlieferant(
						getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELNUMMER_LIEFERANT, 40, i));
				alDto.setCBezbeilieferant(getStringAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_IMPORT_SPALTE_ARTIKELBEZEICHNUNG_LIEFERANT, 80, i));

				BigDecimal bdWbz = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_IMPORT_SPALTE_WIEDERBESCHAFFUNGSZEIT_LIEFERANT, i);
				if (bdWbz != null) {
					alDto.setIWiederbeschaffungszeit(bdWbz.intValue());
				} else {
					alDto.setIWiederbeschaffungszeit(null);
				}

				alDto.setFMindestbestelmenge(getDoubleAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_IMPORT_SPALTE_MINDESTBESTELLMENGE_LIEFERANT, i));
				alDto.setNVerpackungseinheit(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
						XLS_IMPORT_SPALTE_VERPACKUNGSEINHEIT_LIEFERANT, i));
				alDto.setFStandardmenge(
						getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_STANDARDMENGE_LIEFERANT, i));

				getArtikelFac().updateArtikellieferant(alDto, theClientDto);
			}
		} else {
			// Neu anlegen
			alDto = new ArtikellieferantDto();
			alDto.setArtikelIId(artikelIId);
			alDto.setLieferantIId(lieferantIId);
			alDto.setTPreisgueltigab(tDefaultEK);
			alDto.setNEinzelpreis(einzelpreis);
			alDto.setFRabatt(rabatt.doubleValue());
			alDto.setNNettopreis(nettopreis);
			alDto.setBRabattbehalten(Helper.boolean2Short(false));
			alDto.setCArtikelnrlieferant(
					getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELNUMMER_LIEFERANT, 40, i));
			alDto.setCBezbeilieferant(getStringAusXLS(sZeile, hmVorhandeneSpalten,
					XLS_IMPORT_SPALTE_ARTIKELBEZEICHNUNG_LIEFERANT, 80, i));
			alDto.setFMindestbestelmenge(
					getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_MINDESTBESTELLMENGE_LIEFERANT, i));
			alDto.setNVerpackungseinheit(getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
					XLS_IMPORT_SPALTE_VERPACKUNGSEINHEIT_LIEFERANT, i));
			alDto.setFStandardmenge(
					getDoubleAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_STANDARDMENGE_LIEFERANT, i));
			BigDecimal bdWbz = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
					XLS_IMPORT_SPALTE_WIEDERBESCHAFFUNGSZEIT_LIEFERANT, i);
			if (bdWbz != null) {
				alDto.setIWiederbeschaffungszeit(bdWbz.intValue());
			} else {
				alDto.setIWiederbeschaffungszeit(null);
			}
			artikellieferantIId = getArtikelFac().createArtikellieferant(alDto, theClientDto);
		}

		if (artikellieferantIId != null && bAlsLief1Reihen == true) {
			getArtikelFac().artikellieferantAlsErstesReihen(artikelIId, artikellieferantIId);
		}

	}

	public void lagerplatzAnlegen(TheClientDto theClientDto, Integer lagerIId_Hauptlager, String lagerplatz,
			Integer artikelIId) throws RemoteException {
		Integer lagerplatzIId = null;
		try {
			Query query = em.createNamedQuery("LagerplatzfindByLagerIIdCLagerplatz");
			query.setParameter(1, lagerIId_Hauptlager);
			query.setParameter(2, lagerplatz);
			Lagerplatz vorhanden = (Lagerplatz) query.getSingleResult();
			lagerplatzIId = vorhanden.getIId();
		} catch (NoResultException ex) {
			// Neu anlegen
			LagerplatzDto lpDto = new LagerplatzDto();
			lpDto.setLagerIId(lagerIId_Hauptlager);
			lpDto.setCLagerplatz(lagerplatz);
			lagerplatzIId = getLagerFac().createLagerplatz(lpDto, theClientDto);

		}

		try {
			Query query = em.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdLagerplatzIId");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lagerplatzIId);
			Artikellagerplaetze doppelt = (Artikellagerplaetze) query.getSingleResult();
			// Wenn vorhanden, dann nichts tun
		} catch (NoResultException ex) {
			// ansonsten anlegen
			ArtikellagerplaetzeDto artikellagerplaetzeDto = new ArtikellagerplaetzeDto();
			artikellagerplaetzeDto.setArtikelIId(artikelIId);
			artikellagerplaetzeDto.setLagerplatzIId(lagerplatzIId);
			getLagerFac().createArtikellagerplaetze(artikellagerplaetzeDto, theClientDto);

		}
	}

	public BigDecimal vkPreisAnlegen(java.sql.Timestamp tDefaultEK, TheClientDto theClientDto, BigDecimal vkPreisbasis,
			BigDecimal preiseinheit, Integer artikelIId, VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos) {
		if (preiseinheit != null && preiseinheit.doubleValue() != 0) {
			vkPreisbasis = vkPreisbasis.divide(preiseinheit, 4, BigDecimal.ROUND_HALF_EVEN);
		}

		Query query = em.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, artikelIId);
		query.setParameter(3, tDefaultEK);

		try {
			VkPreisfindungEinzelverkaufspreis preis = (VkPreisfindungEinzelverkaufspreis) query.getSingleResult();

			try {
				preis.setNVerkaufspreisbasis(vkPreisbasis);

			} catch (NumberFormatException e) {
				//
			}

		} catch (NoResultException ex) {
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = new VkPreisfindungEinzelverkaufspreisDto();
			vkPreisfindungEinzelverkaufspreisDto.setArtikelIId(artikelIId);
			vkPreisfindungEinzelverkaufspreisDto.setMandantCNr(theClientDto.getMandant());
			vkPreisfindungEinzelverkaufspreisDto.setNVerkaufspreisbasis(vkPreisbasis);
			vkPreisfindungEinzelverkaufspreisDto
					.setTVerkaufspreisbasisgueltigab(new java.sql.Date(tDefaultEK.getTime()));
			try {

				getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(vkPreisfindungEinzelverkaufspreisDto,
						theClientDto);

				if (vkpfartikelpreislisteDtos != null) {

					for (VkpfartikelpreislisteDto preisliste : vkpfartikelpreislisteDtos) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(preisliste.getIId());

						if (preisliste.getNStandardrabattsatz() != null) {
							vkPreisfindungPreislisteDto
									.setNArtikelstandardrabattsatz(preisliste.getNStandardrabattsatz());
						} else {
							vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(BigDecimal.ZERO);

						}

						vkPreisfindungPreislisteDto.setTPreisgueltigab(new java.sql.Date(tDefaultEK.getTime()));

						vkPreisfindungPreislisteDto.setArtikelIId(artikelIId);
						vkPreisfindungPreislisteDto.setTAendern(new Timestamp(System.currentTimeMillis()));
						vkPreisfindungPreislisteDto.setTPreisgueltigab(new java.sql.Date(tDefaultEK.getTime()));
						getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
								theClientDto);
					}

				}

			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

		}

		return vkPreisbasis;
	}

	public void artikelkommentartAnlegen(TheClientDto theClientDto, String kommentarart, String kommentar,
			ArrayList alBelege, Integer artikelIId, Short bDateiverweis) throws RemoteException {
		Integer kommentarartIId = null;

		try {
			Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
			query.setParameter(1, kommentarart);
			Artikelkommentarart art = (Artikelkommentarart) query.getSingleResult();
			kommentarartIId = art.getIId();

		} catch (NoResultException ex) {
			//
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTARART);

			Artikelkommentarart artikelkommentarart = new Artikelkommentarart(pk, kommentarart,
					Helper.boolean2Short(false), Helper.boolean2Short(false), Helper.boolean2Short(false));
			em.persist(artikelkommentarart);
			em.flush();
			kommentarartIId = pk;
		}

		Integer artikelkommentarIId = null;

		Short bDateiverweisLocal = Helper.boolean2Short(false);
		if (bDateiverweis != null) {
			bDateiverweisLocal = bDateiverweis;
		}

		try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, kommentarartIId);

			if (Helper.short2boolean(bDateiverweisLocal) == true) {
				query.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
			} else {
				query.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
			}

			Artikelkommentar ak = (Artikelkommentar) query.getSingleResult();
			if (Helper.short2boolean(bDateiverweisLocal) == true) {
				ak.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN);
			}
			ak.setBDateiverweis(bDateiverweisLocal);

			artikelkommentarIId = ak.getIId();

		} catch (NoResultException ex) {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTAR);

			Query queryNext = em.createNamedQuery("ArtikelkommentarejbSelectNextReihung");
			queryNext.setParameter(1, artikelIId);
			Integer iSort = (Integer) queryNext.getSingleResult();
			if (iSort == null) {
				iSort = 0;
			}

			iSort = new Integer(iSort + 1);

			Artikelkommentar artikelkommentar = new Artikelkommentar(pk, artikelIId, kommentarartIId,
					MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, Helper.boolean2Short(false),
					ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN, iSort, bDateiverweisLocal);

			if (Helper.short2boolean(bDateiverweisLocal) == true) {

				artikelkommentar = new Artikelkommentar(pk, artikelIId, kommentarartIId,
						MediaFac.DATENFORMAT_MIMETYPE_APP_PDF, Helper.boolean2Short(false),
						ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN, iSort, bDateiverweisLocal);
			} else {

				artikelkommentar = new Artikelkommentar(pk, artikelIId, kommentarartIId,
						MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, Helper.boolean2Short(false),
						ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN, iSort, bDateiverweisLocal);

			}

			em.persist(artikelkommentar);
			em.flush();
			artikelkommentarIId = pk;
		}

		// Nun noch spr anlegen
		Artikelkommentarspr spr = em.find(Artikelkommentarspr.class,
				new ArtikelkommentarsprPK(artikelkommentarIId, theClientDto.getLocUiAsString()));
		if (spr == null) {
			spr = new Artikelkommentarspr(artikelkommentarIId, theClientDto.getLocUiAsString(),
					theClientDto.getIDPersonal(), new java.sql.Timestamp(System.currentTimeMillis()));

			if (Helper.short2boolean(bDateiverweisLocal) == true) {
				spr.setCDateiname(kommentar);
			} else {
				spr.setXKommentar(kommentar);
			}

			em.persist(spr);
			em.flush();

		} else {
			if (Helper.short2boolean(bDateiverweisLocal) == true) {
				spr.setCDateiname(kommentar);
			} else {
				spr.setXKommentar(kommentar);
			}

			em.persist(spr);
			em.flush();
		}

		// Und Druck anlegen

		// Zuerst alle Loeschen
		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
		query.setParameter(1, artikelIId);
		query.setParameter(2, artikelkommentarIId);
		Collection<?> allDruck = query.getResultList();
		Iterator<?> iterAllDruck = allDruck.iterator();
		try {
			while (iterAllDruck.hasNext()) {
				Artikelkommentardruck artklasprTemp = (Artikelkommentardruck) iterAllDruck.next();
				em.remove(artklasprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex2);
		}
		if (alBelege != null) {

			if (Helper.short2boolean(bDateiverweisLocal) == true) {
				alBelege = new ArrayList();
				alBelege.add(LocaleFac.BELEGART_LOS);
			}

			for (int j = 0; j < alBelege.size(); j++) {
				ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
				dto.setArtikelkommentarIId(artikelkommentarIId);
				dto.setArtikelIId(artikelIId);
				dto.setBelegartCNr((String) alBelege.get(j));
				getArtikelkommentarFac().createArtikelkommentardruck(dto);
			}
		}
	}

	public Integer herstellerSuchenUndAnlegen(TheClientDto theClientDto, String hersteller) throws RemoteException {
		List<Partner> partners = PartnerQuery.listByKbez(em, hersteller);

		String herstellercode = null;
		PartnerDto pDto = null;
		if (partners.size() == 0) {
			pDto = new PartnerDto();
			pDto.setCKbez(hersteller);
			pDto.setCName1nachnamefirmazeile1(hersteller);
			pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
			pDto.setBVersteckt(false);
			pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

			Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

			pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

			herstellercode = getArtikelFac().getHerstellercode(pDto.getIId(), theClientDto);

		} else {
			// Es kann mehrere Partner mit gleicher Kurzbezeichnung geben
			// Es kann bei irgendeinem davon ein Hersteller definiert sein
			Integer partnerIId = null;
			for (Partner partner : partners) {
				Integer nextPartnerIId = partner.getIId();
				List<Hersteller> herstellerVonPartner = HerstellerQuery.listByPartnerIId(nextPartnerIId, em);
				for (Hersteller hst : herstellerVonPartner) {
					if (ArtikelFac.patternHerstellerkuerzel.matcher(hst.getCNr()).matches()) {
						// Hat richtiges format
						partnerIId = nextPartnerIId;
						herstellercode = hst.getCNr();
						break;
					}
				}
				if (partnerIId != null) {
					break;
				}
			}
			if (partnerIId == null) {
				// kein Hersteller gefunden.
				partnerIId = partners.get(0).getIId();
				herstellercode = getArtikelFac().getHerstellercode(partnerIId, theClientDto);
			}
			pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
		}

		try {
			Query query = em.createNamedQuery("HerstellerfindByCNr");
			if (herstellercode != null) {
				query.setParameter(1, herstellercode);
			} else {
				query.setParameter(1, hersteller);
			}

			Hersteller vorhanden = (Hersteller) query.getSingleResult();
			return vorhanden.getIId();
		} catch (NoResultException ex) {
			// Neu anlegen
			HerstellerDto herstellerDto = new HerstellerDto();

			herstellerDto.setPartnerIId(pDto.getIId());
			if (herstellercode != null) {
				herstellerDto.setCNr(herstellercode);
			} else {
				herstellerDto.setCNr(hersteller);
			}

			if (herstellerDto.getCNr().length() > ArtikelFac.MAX_HERSTELLER_NAME) {
				herstellerDto.setCNr(herstellerDto.getCNr().substring(0, ArtikelFac.MAX_HERSTELLER_NAME));

			}

			Integer herstellerIId = getArtikelFac().createHersteller(herstellerDto);
			return herstellerIId;
		}
	}

	public Integer materialSuchenUndAnlegen(TheClientDto theClientDto, String material) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("MaterialfindByCNr");
			query.setParameter(1, material);

			Material materialBean = (Material) query.getSingleResult();
			return materialBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			MaterialDto materialDto = new MaterialDto();
			materialDto.setCNr(material);
			Integer materialIId = getMaterialFac().createMaterial(materialDto, theClientDto);
			return materialIId;

		}
	}

	public Integer farbcodeSuchenUndAnlegen(TheClientDto theClientDto, String farbcode) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("FarbcodefindByCNr");
			query.setParameter(1, farbcode);

			Farbcode farbcodeBean = (Farbcode) query.getSingleResult();
			return farbcodeBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			FarbcodeDto farbcodeDto = new FarbcodeDto();
			farbcodeDto.setCNr(farbcode);
			Integer materialIId = getArtikelFac().createFarbcode(farbcodeDto);
			return materialIId;

		}
	}

	public Integer reachSuchenUndAnlegen(TheClientDto theClientDto, String reach) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("ReachfindByMandantCNrCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, reach);

			Reach reachBean = (Reach) query.getSingleResult();
			return reachBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			ReachDto reachDto = new ReachDto();
			reachDto.setCBez(reach);
			reachDto.setMandantCNr(theClientDto.getMandant());
			Integer reachIId = getArtikelFac().createReach(reachDto);
			return reachIId;

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

	public Integer automotiveSuchenUndAnlegen(TheClientDto theClientDto, String automotive) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("AutomotivefindByMandantCNrCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, automotive);

			Automotive automotiveBean = (Automotive) query.getSingleResult();
			return automotiveBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			AutomotiveDto automotiveDto = new AutomotiveDto();
			automotiveDto.setCBez(automotive);
			automotiveDto.setMandantCNr(theClientDto.getMandant());
			Integer automotiveIId = getArtikelFac().createAutomotive(automotiveDto);
			return automotiveIId;

		}
	}

	public Integer rohsSuchenUndAnlegen(TheClientDto theClientDto, String rohs) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("RohsfindByMandantCNrCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, rohs);

			Rohs rohsBean = (Rohs) query.getSingleResult();
			return rohsBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			RohsDto rohsDto = new RohsDto();
			rohsDto.setCBez(rohs);
			rohsDto.setMandantCNr(theClientDto.getMandant());
			Integer rohsIId = getArtikelFac().createRohs(rohsDto);
			return rohsIId;

		}
	}

	public Integer medicalSuchenUndAnlegen(TheClientDto theClientDto, String medical) throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("MedicalfindByMandantCNrCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, medical);

			Medical medicalBean = (Medical) query.getSingleResult();
			return medicalBean.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			MedicalDto medicalDto = new MedicalDto();
			medicalDto.setCBez(medical);
			medicalDto.setMandantCNr(theClientDto.getMandant());
			Integer materialIId = getArtikelFac().createMedicale(medicalDto);
			return materialIId;

		}
	}

	public String einheitSuchenUndAnlegen(TheClientDto theClientDto, String einheitCNr) throws RemoteException {
		// Einheit anlegen
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("EinheitFindByCNr");
			query.setParameter(1, einheitCNr);

			Einheit einheit = (Einheit) query.getSingleResult();

		} catch (NoResultException ex) {

			EinheitDto einheitDto = new EinheitDto();
			einheitDto.setCNr(einheitCNr);
			einheitDto.setIDimension(0);
			getSystemFac().createEinheit(einheitDto, theClientDto);

		}
		return einheitCNr;

	}

	public Integer artikelklasseSuchenUndAnlegen(TheClientDto theClientDto, String artikelklasse)
			throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelklasse
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
			query.setParameter(1, artikelklasse);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				query.setParameter(2, getSystemFac().getHauptmandant());
			} else {
				query.setParameter(2, theClientDto.getMandant());
			}

			Artkla artkla = (Artkla) query.getSingleResult();
			return artkla.getIId();
		} catch (NoResultException ex) {

			// Neu anlegen
			ArtklaDto artklaDto = new ArtklaDto();
			artklaDto.setCNr(artikelklasse);
			artklaDto.setBTops(Helper.boolean2Short(false));
			return getArtikelFac().createArtkla(artklaDto, theClientDto);

		}
	}

	public Integer artikelgruppeSuchenUndAnlegen(TheClientDto theClientDto, String artikelgruppe)
			throws RemoteException {
		try {
			// duplicateunique: Pruefung: Artikelgruppe
			// bereits
			// vorhanden.
			Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
			query.setParameter(1, artikelgruppe);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				query.setParameter(2, getSystemFac().getHauptmandant());
			} else {
				query.setParameter(2, theClientDto.getMandant());
			}
			Artgru artgru = (Artgru) query.getSingleResult();
			return artgru.getIId();
		} catch (NoResultException ex) {
			// Neu anlegen
			ArtgruDto artgruDto = new ArtgruDto();
			artgruDto.setCNr(artikelgruppe);
			artgruDto.setBRueckgabe(Helper.boolean2Short(false));
			artgruDto.setBZertifizierung(Helper.boolean2Short(false));
			artgruDto.setBKeinevkwarnmeldungimls(Helper.boolean2Short(false));
			return getArtikelFac().createArtgru(artgruDto, theClientDto);

		}
	}

	public String gestpreisImportieren(TheClientDto theClientDto, byte[] CRLFAscii, String rueckgabe,
			String artikelnummer, ArtikelDto artikelDto, BigDecimal preiseinheit, Integer artikelIId,
			BigDecimal gestehungspreis) throws RemoteException {
		if (preiseinheit != null && preiseinheit.doubleValue() != 0) {
			gestehungspreis = gestehungspreis.divide(preiseinheit, 4, BigDecimal.ROUND_HALF_EVEN);
		}

		// Wenn Lagerstand aller Laeger 0 ist, dann

		BigDecimal bdLagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(artikelIId, theClientDto);

		LagerDto[] lagerDtos = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());

		if (bdLagerstand.doubleValue() == 0) {

			for (int k = 0; k < lagerDtos.length; k++) {
				LagerDto lDto = lagerDtos[k];

				Artikellager al = em.find(Artikellager.class, new ArtikellagerPK(artikelIId, lDto.getIId()));

				if (al != null) {
					al.setNGestehungspreis(gestehungspreis);
				} else {
					al = new Artikellager(artikelIId, lDto.getIId(), gestehungspreis, BigDecimal.ZERO,
							theClientDto.getMandant());

				}
				em.merge(al);
				em.flush();

			}

		} else {
			// LOG
			String artikelbez = artikelnummer;
			if (artikelDto.getArtikelsprDto() != null && artikelDto.getArtikelsprDto().getCBez() != null) {
				artikelbez += " " + artikelDto.getArtikelsprDto().getCBez();
			}

			fehlerZeileXLSImport += "Der Gestehungspreis des Artikels '" + artikelbez
					+ "' konnte nicht geaendert werden, da der Lagerstand > 0 ist. SollPreis: "
					+ Helper.formatZahl(gestehungspreis, 4, theClientDto.getLocUi()) + new String(CRLFAscii);

			rueckgabe += fehlerZeileXLSImport;
		}
		return rueckgabe;
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

						fehlerZeileXLSImport += feldname + " ist zu lang (>" + iLaenge + ") Zeile " + (iZeile + 1)
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

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().trim().equals("0")) {
						return new Short((short) 0);
					} else if (c.getContents().trim().equals("1")) {
						return new Short((short) 1);
					} else {

						fehlerZeileXLSImport += feldname + " darf nur die Werte 0 bzw. 1 enthalten. Zeile "
								+ (iZeile + 1) + new String(CRLFAscii);

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

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Date' sein. Zeile " + (iZeile + 1)
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

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' sein. Zeile " + (iZeile + 1)
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

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' sein. Zeile " + (iZeile + 1)
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

	public void importiereArtikel(ArtikelImportDto[] daten, boolean bBestehendeArtikelUeberschreiben,
			TheClientDto theClientDto) {

		String default_artikelart = null;
		String default_artikeleinheit = null;
		Integer default_mwstsaztIId = null;
		VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
		try {

			vkpfartikelpreislisteDtos = getVkPreisfindungFac().getAlleAktivenPreislisten(Helper.boolean2Short(true),
					theClientDto);

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART);

			default_artikelart = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			default_artikeleinheit = parameter.getCWert();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ);
			if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
				default_mwstsaztIId = (Integer) parameter.getCWertAsObject();
			}

		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		for (int i = 0; i < daten.length; i++) {

			ArtikelImportDto zeile = daten[i];

			ArtikelDto artikelDto = new ArtikelDto();
			boolean bArtikelVorhanden = false;
			try {
				try {
					// duplicateunique: Pruefung: Artikelgruppe bereits
					// vorhanden.
					Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelnummer());
					query.setParameter(2, theClientDto.getMandant());
					Artikel artikel = (Artikel) query.getSingleResult();
					bArtikelVorhanden = true;
					if (bBestehendeArtikelUeberschreiben == true) {
						artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikel.getIId(), theClientDto);

					} else {
						continue;
					}

				} catch (NoResultException ex) {
					//
				}

			} catch (Throwable e) {
				// Dann ist noch kein Artikel mit der Nummer vorhanden
			}

			ArtikelsprDto artikelsprDto = new ArtikelsprDto();
			artikelDto.setCNr(zeile.getArtikelnummer());
			artikelDto.setBVersteckt(Helper.boolean2Short(false));
			if (zeile.getBezeichnung().length() > 0) {
				artikelsprDto.setCBez(zeile.getBezeichnung());
			}
			if (zeile.getKurzbezeichnung().length() > 0) {
				artikelsprDto.setCKbez(zeile.getKurzbezeichnung());
			}
			if (zeile.getZusatzbezeichnung().length() > 0) {
				artikelsprDto.setCZbez(zeile.getZusatzbezeichnung());
			}
			if (zeile.getZusatzbezeichnung2().length() > 0) {
				artikelsprDto.setCZbez2(zeile.getZusatzbezeichnung2());
			}

			artikelDto.setArtikelsprDto(artikelsprDto);

			if (zeile.getReferenznummer().length() > 0) {
				artikelDto.setCReferenznr(zeile.getReferenznummer());
			}
			if (zeile.getIndex().length() > 0) {
				artikelDto.setCIndex(zeile.getIndex());
			}
			if (zeile.getRevision().length() > 0) {
				artikelDto.setCRevision(zeile.getRevision());
			}

			if (zeile.getChargenbehaftet().length() > 0) {
				artikelDto.setBChargennrtragend(new Short(zeile.getChargenbehaftet()));
			}
			if (zeile.getSnrbehaftet().length() > 0) {
				artikelDto.setBSeriennrtragend(new Short(zeile.getSnrbehaftet()));
			}

			if (zeile.getArtikelart().length() > 0) {
				artikelDto.setArtikelartCNr(zeile.getArtikelart());

			} else {
				artikelDto.setArtikelartCNr(default_artikelart);
			}

			if (zeile.getEinheit().length() > 0) {
				artikelDto.setEinheitCNr(zeile.getEinheit());

			} else {
				artikelDto.setEinheitCNr(default_artikeleinheit);
			}

			if (zeile.getArtikelgruppe().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelgruppe bereits
					// vorhanden.
					Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelgruppe());
					query.setParameter(2, theClientDto.getMandant());
					Artgru artgru = (Artgru) query.getSingleResult();
					artikelDto.setArtgruIId(artgru.getIId());
				} catch (NoResultException ex) {
					//
				}
			}

			if (zeile.getArtikelklasse().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelklasse());
					query.setParameter(2, theClientDto.getMandant());

					Artkla artkla = (Artkla) query.getSingleResult();
					artikelDto.setArtklaIId(artkla.getIId());
				} catch (NoResultException ex) {
					//
				}
			}

			if (zeile.getMwstsatz().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, zeile.getMwstsatz());
					Mwstsatzbez mwstsatz = (Mwstsatzbez) query.getSingleResult();
					artikelDto.setMwstsatzbezIId(mwstsatz.getIId());
				} catch (NoResultException ex) {
					//
				}
			} else {
				// Parameter
				if (default_mwstsaztIId != null) {
					artikelDto.setMwstsatzbezIId(default_mwstsaztIId);
				}
			}
			Integer artikelIId = null;

			try {

				if (bArtikelVorhanden == true && bBestehendeArtikelUeberschreiben == true) {
					getArtikelFac().updateArtikel(artikelDto, theClientDto);
					artikelIId = artikelDto.getIId();
				} else {
					artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			if (zeile.getVkpreisbasis().length() > 0) {
				java.sql.Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				try {

					date = new java.sql.Date(format.parse(zeile.getVkpreisbasisgueltigab()).getTime());

				} catch (ParseException e) {
					//
				}

				try {
					BigDecimal bdvkpreisbasis = new BigDecimal(zeile.getVkpreisbasis());

				} catch (NumberFormatException e) {
					//
				}

				Query query = em
						.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, artikelIId);
				query.setParameter(3, date);

				try {
					VkPreisfindungEinzelverkaufspreis preis = (VkPreisfindungEinzelverkaufspreis) query
							.getSingleResult();

					try {
						preis.setNVerkaufspreisbasis(new BigDecimal(zeile.getVkpreisbasis()));

					} catch (NumberFormatException e) {
						//
					}

				} catch (NoResultException ex) {
					VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = new VkPreisfindungEinzelverkaufspreisDto();
					vkPreisfindungEinzelverkaufspreisDto.setArtikelIId(artikelIId);
					vkPreisfindungEinzelverkaufspreisDto.setMandantCNr(theClientDto.getMandant());
					vkPreisfindungEinzelverkaufspreisDto
							.setNVerkaufspreisbasis(new BigDecimal(zeile.getVkpreisbasis()));
					vkPreisfindungEinzelverkaufspreisDto.setTVerkaufspreisbasisgueltigab(date);
					try {

						getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(
								vkPreisfindungEinzelverkaufspreisDto, theClientDto);
					} catch (RemoteException e1) {
						throwEJBExceptionLPRespectOld(e1);
					}

				}

				if (vkpfartikelpreislisteDtos.length > 0) {

					if (zeile.getFixpreispreisliste1().length() > 0 || zeile.getRabattsatzpreisliste1().length() > 0) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(vkpfartikelpreislisteDtos[0].getIId());
						vkPreisfindungPreislisteDto.setArtikelIId(artikelIId);
						vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(new BigDecimal(0));

						if (zeile.getFixpreispreisliste1().length() > 0) {
							try {
								BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste1());
								vkPreisfindungPreislisteDto.setNArtikelfixpreis(bdFixpreis);
							} catch (NumberFormatException e) {
								//
							}
						} else {
							try {
								BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste1());
								vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(bdRabattsatz);
							} catch (NumberFormatException e) {
								//
							}
						}

						try {
							java.sql.Date date1 = new java.sql.Date(
									format.parse(zeile.getGueltigabpreisliste1()).getTime());
							vkPreisfindungPreislisteDto.setTPreisgueltigab(date1);
						} catch (ParseException e) {
							//
						}

						try {

							try {
								Query queryPreisliste = em.createNamedQuery(
										"VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
								queryPreisliste.setParameter(1,
										vkPreisfindungPreislisteDto.getVkpfartikelpreislisteIId());
								queryPreisliste.setParameter(2, vkPreisfindungPreislisteDto.getArtikelIId());
								queryPreisliste.setParameter(3, vkPreisfindungPreislisteDto.getTPreisgueltigab());
								VkPreisfindungPreisliste preisliste = (VkPreisfindungPreisliste) queryPreisliste
										.getSingleResult();

								preisliste.setNArtikelfixpreis(vkPreisfindungPreislisteDto.getNArtikelfixpreis());
								preisliste.setNArtikelstandardrabattsatz(
										vkPreisfindungPreislisteDto.getNArtikelstandardrabattsatz());

							} catch (NoResultException ex) {
								getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
										theClientDto);
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					}
				}

				// Preisliste2
				if (vkpfartikelpreislisteDtos.length > 1) {
					if (zeile.getFixpreispreisliste2().length() > 0 || zeile.getRabattsatzpreisliste2().length() > 0) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(vkpfartikelpreislisteDtos[1].getIId());
						vkPreisfindungPreislisteDto.setArtikelIId(artikelIId);
						vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(new BigDecimal(0));

						if (zeile.getFixpreispreisliste2().length() > 0) {
							try {
								BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste2());
								vkPreisfindungPreislisteDto.setNArtikelfixpreis(bdFixpreis);
							} catch (NumberFormatException e) {
								//
							}
						} else {
							try {
								BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste2());
								vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(bdRabattsatz);
							} catch (NumberFormatException e) {
								//
							}
						}

						try {
							java.sql.Date date2 = new java.sql.Date(
									format.parse(zeile.getGueltigabpreisliste2()).getTime());
							vkPreisfindungPreislisteDto.setTPreisgueltigab(date2);
						} catch (ParseException e) {
							//
						}
						try {
							try {
								Query queryPreisliste = em.createNamedQuery(
										"VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
								queryPreisliste.setParameter(1,
										vkPreisfindungPreislisteDto.getVkpfartikelpreislisteIId());
								queryPreisliste.setParameter(2, vkPreisfindungPreislisteDto.getArtikelIId());
								queryPreisliste.setParameter(3, vkPreisfindungPreislisteDto.getTPreisgueltigab());
								VkPreisfindungPreisliste preisliste = (VkPreisfindungPreisliste) queryPreisliste
										.getSingleResult();

								preisliste.setNArtikelfixpreis(vkPreisfindungPreislisteDto.getNArtikelfixpreis());
								preisliste.setNArtikelstandardrabattsatz(
										vkPreisfindungPreislisteDto.getNArtikelstandardrabattsatz());

							} catch (NoResultException ex) {
								getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
										theClientDto);
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}
				}
				if (vkpfartikelpreislisteDtos.length > 2) {
					// Preisliste3

					if (zeile.getFixpreispreisliste3().length() > 0 || zeile.getRabattsatzpreisliste3().length() > 0) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(vkpfartikelpreislisteDtos[2].getIId());
						vkPreisfindungPreislisteDto.setArtikelIId(artikelIId);
						vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(new BigDecimal(0));

						if (zeile.getFixpreispreisliste3().length() > 0) {
							try {
								BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste3());
								vkPreisfindungPreislisteDto.setNArtikelfixpreis(bdFixpreis);
							} catch (NumberFormatException e) {
								//
							}
						} else {
							try {
								BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste3());
								vkPreisfindungPreislisteDto.setNArtikelstandardrabattsatz(bdRabattsatz);
							} catch (NumberFormatException e) {
								//
							}
						}

						try {
							java.sql.Date date3 = new java.sql.Date(
									format.parse(zeile.getGueltigabpreisliste3()).getTime());
							vkPreisfindungPreislisteDto.setTPreisgueltigab(date3);
						} catch (ParseException e) {
							//
						}
						try {
							try {
								Query queryPreisliste = em.createNamedQuery(
										"VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
								queryPreisliste.setParameter(1,
										vkPreisfindungPreislisteDto.getVkpfartikelpreislisteIId());
								queryPreisliste.setParameter(2, vkPreisfindungPreislisteDto.getArtikelIId());
								queryPreisliste.setParameter(3, vkPreisfindungPreislisteDto.getTPreisgueltigab());
								VkPreisfindungPreisliste preisliste = (VkPreisfindungPreisliste) queryPreisliste
										.getSingleResult();

								preisliste.setNArtikelfixpreis(vkPreisfindungPreislisteDto.getNArtikelfixpreis());
								preisliste.setNArtikelstandardrabattsatz(
										vkPreisfindungPreislisteDto.getNArtikelstandardrabattsatz());

							} catch (NoResultException ex) {
								getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
										theClientDto);
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}
				}

			}

		}
	}

}
