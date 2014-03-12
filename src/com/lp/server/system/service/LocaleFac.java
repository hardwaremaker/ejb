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
package com.lp.server.system.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.ejb.FunktionsprPK;
import com.lp.server.system.ejb.LieferartsprPK;
import com.lp.server.system.ejb.PositionsartsprPK;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LocaleFac {

	// Lieferarten.
	public static String LIEFERART_AB_WERK = "Ab Werk";
	public static String LIEFERART_ZENTRALE = "Zentrale";
	public static String LIEFERART_PER_BAHNEXPRESS = "Per Bahnexpress";
	public static String LIEFERART_FREI_HAUS = "Frei Haus";
	public static String LIEFERART_PER_POST = "Per Post";
	public static String LIEFERART_UNFREI = "Unfrei";

	// Wichtige Locale-Strings
	public static final String LP_LOCALE_DE_AT = "deAT      ";
	public static final String LP_LOCALE_DE_DE = "deDE      ";
	public static final String LP_LOCALE_EN_US = "enUS      ";
	public static final String LP_LOCALE_IT_IT = "itIT      ";
	public static final String LP_LOCALE_PL_PL = "plPL      ";
	public static final String LP_LOCALE_HI_IN = "hiIN      ";
	public static final String LP_LOCALE_EN_GB = "enGB      ";
	public static final String LP_LOCALE_JA = "jaJP      ";
	public static final String LP_LOCALE_CH = "deCH      ";

	// Feldlaengen
	public static final int MAX_BELEGART = 15;

	// Belegarten
	public static final String BELEGART_ANGEBOT = "Angebot        ";
	public static final String BELEGART_AUFTRAG = "Auftrag        ";
	public static final String BELEGART_GUTSCHRIFT = "Gutschrift     ";
	public static final String BELEGART_HAND = "Hand           ";
	public static final String BELEGART_LIEFERSCHEIN = "Lieferschein   ";
	public static final String BELEGART_LSZIELLAGER = "LS_Ziellager   ";
	public static final String BELEGART_RECHNUNG = "Rechnung       ";
	public static final String BELEGART_BESTELLUNG = "Bestellung     ";
	public static final String BELEGART_RUECKSCHEIN = "Rueckschein    ";
	public static final String BELEGART_INVENTUR = "Inventur       ";
	public static final String BELEGART_LOS = "Los            ";
	public static final String BELEGART_LOSABLIEFERUNG = "Losablieferung ";
	public static final String BELEGART_REPARATUR = "Reparatur      ";
	public static final String BELEGART_RUECKLIEFERUNG = "Ruecklieferung ";
	public static final String BELEGART_EINGANGSRECHNUNG = "Eingangsrechng ";
	public static final String BELEGART_PROFORMARECHNUNG = "Proformarechng";
	public static final String BELEGART_ANFRAGE = "Anfrage        ";
	public static final String BELEGART_PARTNER = "Partner        ";
	public static final String BELEGART_PROJEKT = "Projekt        ";
	public static final String BELEGART_KUNDE = "Kunde          ";
	public static final String BELEGART_LIEFERANT = "Lieferant      ";
	public static final String BELEGART_ARTIKEL = "Artikel        ";
	public static final String BELEGART_PERSONAL = "Personal       ";
	public static final String BELEGART_ZEITERFASSUNG = "Zeiterfassung  ";
	public static final String BELEGART_FINANZBUCHHALTUNG = "Finanzbuchhaltg";
	public static final String BELEGART_SYSTEM = "System         ";
	public static final String BELEGART_BENUTZER = "Benutzer       ";
	public static final String BELEGART_STUECKLISTE = "Stueckliste    ";
	public static final String BELEGART_AGSTUECKLISTE = "AGStueckliste  ";
	public static final String BELEGART_REZAHLUNG = "REZahlung      ";
	public static final String BELEGART_ERZAHLUNG = "ERZahlung      ";
	public static final String BELEGART_ZUTRITT = "Zutritt        ";
	public static final String BELEGART_REKLAMATION = "Reklamation    ";
	public static final String BELEGART_WARENEINGANG = "Wareneingang   ";
	public static final String BELEGART_KUECHE =       "Kueche         ";
	public static final String BELEGART_INSTANDHALTUNG = "Instandhaltung ";
	public static final String BELEGART_INSERAT =        "Inserat        ";

	// Belegarten in der Fibu (FB_BELEGART)
	public static final String BELEGART_FIBU_RECHNUNG = "Rechnung       ";
	public static final String BELEGART_FIBU_EINGANGSRECHNUNG = "Eingangsrechng ";
	public static final String BELEGART_FIBU_GUTSCHRIFT = "Gutschrift     ";
	
	// Wechselkurs
	public static final int ANZAHL_VORKOMMASTELLEN_WECHSELKURS = 3;
	public static final int ANZAHL_NACHKOMMASTELLEN_WECHSELKURS = 6;

	// Positionsarten
	public static final String POSITIONSART_IDENT = "Ident          ";
	public static final String POSITIONSART_HANDEINGABE = "Handeingabe    ";
	public static final String POSITIONSART_TEXTEINGABE = "Texteingabe    ";
	public static final String POSITIONSART_BETRIFFT = "Betrifft       ";
	public static final String POSITIONSART_POSITION = "Position       ";
	public static final String POSITIONSART_LIEFERSCHEIN = "Lieferschein   ";
	public static final String POSITIONSART_TEXTBAUSTEIN = "Textbaustein   ";
	public static final String POSITIONSART_ZWISCHENSUMME = "Zwischensumme  ";
	public static final String POSITIONSART_TRANSPORTSPESEN = "Transportspesen";
	public static final String POSITIONSART_URSPRUNGSLAND = "Ursprungsland  ";
	public static final String POSITIONSART_ANZAHLUNG = "Anzahlung      ";
	public static final String POSITIONSART_VORAUSZAHLUNG = "Vorauszahlung  ";
	public static final String POSITIONSART_LEERZEILE = "Leerzeile      ";
	public static final String POSITIONSART_SEITENUMBRUCH = "Seitenumbruch  ";
	public static final String POSITIONSART_PAUSCHALPOSITION = "Pauschalpositio";
	public static final String POSITIONSART_RECHNUNG = "Rechnung       ";
	public static final String POSITIONSART_GUTSCHRIFT = "Gutschrift     ";
	public static final String POSITIONSART_REPARATUR = "Reparatur      ";
	public static final String POSITIONSART_RUECKSCHEIN = "Rueckschein    ";
	public static final String POSITIONSART_FREMDARTIKEL = "Fremdartikel   ";
	public static final String POSITIONSART_BILD = "Bild           ";
	public static final String POSITIONSART_AGSTUECKLISTE = "AGStueckliste  ";
	public static final String POSITIONSART_STUECKLISTENPOSITION = "Stuecklistenpos"; // unterstkl
																						// :
																						// 0
																						// diese
																						// Positionsart
																						// existiert
																						// nicht
																						// auf
																						// der
																						// DB
																						// ,
																						// sie
																						// dient
																						// zum
																						// Drucken
	public static final String POSITIONSART_ENDSUMME = "Endsumme       "; // endsumme
																			// :
																			// 0
																			// systemweite
																			// Positionsart
	public static final String POSITIONSART_AUFTRAGSDATEN = "Auftragsdaten  "; // diese
																				// Positionsart
																				// existiert
																				// nicht
																				// auf
																				// der
																				// DB
																				// ,
																				// sie
																				// dient
																				// zum
																				// Drucken

	
	public static final String POSITIONSART_ZUSAMMENFASSUNG_NUR_REPORT = "Zusammenfassung";
	public static final String POSITIONSART_INTELLIGENTE_ZWISCHENSUMME = "IZwischensumme " ;
	
	// Stati
	public static final String STATUS_ANGELEGT = "Angelegt       ";
	public static final String STATUS_BESTAETIGT = "Bestaetigt     ";
	public static final String STATUS_BEZAHLT = "Bezahlt        ";
	public static final String STATUS_ERLEDIGT = "Erledigt       ";
	public static final String STATUS_DATEN_UNGUELTIG = "Daten ungueltig";
	public static final String STATUS_ERFASST = "Erfasst        ";
	public static final String STATUS_FEHLGESCHLAGEN = "Fehlgeschlagen ";
	public static final String STATUS_FERTIG = "Fertig         ";
	public static final String STATUS_FREIGEGEBEN = "Freigegeben    ";
	public static final String STATUS_GELIEFERT = "Geliefert      ";
	public static final String STATUS_OFFEN = "Offen          ";
	public static final String STATUS_STORNIERT = "Storniert      ";
	public static final String STATUS_ABGERUFEN = "Abgerufen      ";
	public static final String STATUS_TEILBEZAHLT = "Teilbezahlt    ";
	public static final String STATUS_TEILERLEDIGT = "Teilerledigt   ";
	public static final String STATUS_TEILGELIEFERT = "Teilgeliefert  ";
	public static final String STATUS_VERBUCHT = "Verbucht       ";
	public static final String STATUS_VERRECHNET = "Verrechnet     ";
	public static final String STATUS_AUSGEGEBEN = "Ausgegeben     ";
	public static final String STATUS_IN_PRODUKTION = "In Produktion  ";
	public static final String STATUS_GESTOPPT = "Gestoppt       ";
	public static final String STATUS_GESPERRT = "Gesperrt       ";
	public static final String STATUS_EMAIL =      "Email          ";
	public static final String STATUS_BESTELLT =   "Bestellt       ";
	public static final String STATUS_ERSCHIENEN = "Erschienen     ";
	public static final String STATUS_VERRECHENBAR="Verrechenbar   ";

	public static final String POSITIONTYP_ALLES = "Alles";
	public static final String POSITIONTYP_VERDICHTET = "Verdichtet";
	public static final String POSITIONTYP_OHNEPREISE = "Ohnepreise";
	public static final String POSITIONTYP_MITPREISE = "Mitpreise";
	public static final String POSITIONTYP_EBENE1 = "Ebene1";
	public static final String POSITIONTYP_EBENE2 = "Ebene2";

	public static final String POSITIONBEZ_BEGINN = "Beginn";
	public static final String POSITIONBEZ_ENDE = "Ende";

	public String createLocale(LocaleDto localeDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLocale(String cNr) throws EJBExceptionLP, RemoteException;

	public void removeLocale(LocaleDto localeDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLocale(LocaleDto localeDto) throws EJBExceptionLP,
			RemoteException;

	public LocaleDto localeFindByPrimaryKey(String cNr) throws EJBExceptionLP,
			RemoteException;

	public LocaleDto localeFindByPrimaryKeyOhneExc(String cNr)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllLocales(Locale inLocale) 
			throws RemoteException;
	
	public Map<?, ?> getAllSpr(Locale locale, String mandantCNr) throws RemoteException;

	public Map<?, ?> getAllLieferarten() throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprLieferarten(Locale loSuchErstHierI, Locale loDannHierI)
			throws EJBExceptionLP, RemoteException;

	public String lieferartFindByIIdLocaleOhneExc(Integer iIdLieferartI,
			Locale localeI, TheClientDto theClientDto) throws RemoteException;

	public void removeWaehrung(WaehrungDto waehrungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createUpdateWaehrung(WaehrungDto waehrungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllWaehrungen() throws RemoteException;

	public void removeWechselkurs(WechselkursDto wechselkursDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateWechselkurs(WechselkursDto wechselkursDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public WechselkursDto wechselkursFindByPrimaryKey(String waehrungCNrVon,
			String waehrungCNrZu, java.util.Date tDatum) throws EJBExceptionLP,
			RemoteException;

	public boolean wechselkursExists(String sWaehrungVonI, String sWaehrungZuI)
			throws RemoteException;

	public WaehrungDto[] waehrungFindAll() throws EJBExceptionLP,
			RemoteException;

	public WaehrungDto waehrungFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getWechselkurs2(String waehrungCNrVonI,
			String waehrungCNrNachI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
	
	public WechselkursDto getKursZuDatum(String waehrungCNrVonI,
			String waehrungCNrNachI,Date dDatum, TheClientDto theClientDto) throws RemoteException;

	public String createBelegart(BelegartDto belegartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBelegart(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeBelegart(BelegartDto belegartDto) throws EJBExceptionLP,
			RemoteException;

	public void updateBelegart(BelegartDto belegartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BelegartDto belegartFindByPrimaryKey(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BelegartDto[] belegartFindAll() throws EJBExceptionLP,
			RemoteException;

	public void createBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBelegartspr(String spracheCNr, String belegartCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP, RemoteException;

	public BelegartsprDto belegartsprFindByPrimaryKey(String belegartCNr,
			String spracheCNr) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllBelegartenUebersetzt(Locale pLocale1, Locale pLocale2)
			throws EJBExceptionLP, RemoteException;

	public String uebersetzeBelegartOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public BelegartDto belegartFindByCNr(String pCNr) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal rechneUmInAndereWaehrungZuDatum(BigDecimal bdBetragI,
			String cCurrency1I, String cCurrency2I, Date dDatumI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal rechneUmInAndereWaehrungZuDatumOhneExc(BigDecimal bdBetragI,
			String sCurrencyVonI, String sCurrencyNachI, Date dDatumI, TheClientDto theClientDto)
			throws RemoteException;

	public Integer createLieferart(LieferartDto lieferartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLieferart(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLieferart(LieferartDto lieferartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferart(LieferartDto lieferartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferartDto lieferartFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferartDto lieferartFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferartsprPK createLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferartspr(Integer lieferartIIdI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferartsprDto lieferartsprFindByPrimaryKey(Integer lieferartIIdI,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public LieferartsprDto lieferartsprFindByPrimaryKeyOhneExc(
			Integer iIdLieferartI, String sLocaleCNrI, TheClientDto theClientDto)
			throws RemoteException;

	public String createPositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePositionsart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PositionsartDto positionsartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStatus(StatusDto statusDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void updateStatus(StatusDto statusDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public HashMap<?, ?> getAllStatiIcon() throws RemoteException;

	public String createStatus(StatusDto statusDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public StatusDto statusFindByPrimaryKey(String cNrI, TheClientDto theClientDto)
			throws RemoteException;

	public StatussprDto getStatusspr(String cNrI, String sLocUiI,
			TheClientDto theClientDto) throws RemoteException;

	public StatussprDto statussprFindByPrimaryKey(String cNrI, String sLocUiI,
			TheClientDto theClientDto) throws RemoteException;

	public String createStatusspr(StatussprDto statusspr2DtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void removeStatusspr(StatussprDto statusspr2DtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void updateStatusspr(StatussprDto statussprDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public Integer createFunktion(FunktionDto funktionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeFunktion(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeFunktion(FunktionDto funktionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateFunktion(FunktionDto funktionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FunktionDto funktionFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FunktionsprPK createFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeFunktionspr(Integer funktionIIdI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public FunktionsprDto funktionsprFindByPrimaryKey(Integer funktionIIdI,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public FunktionsprDto[] funktionsprFindByFunktionIId(Integer funktionIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PositionsartsprDto getPositionsartspr(String cNrI, String sLocUiI,
			TheClientDto theClientDto) throws RemoteException;

	public PositionsartsprPK createPositionsartspr(
			PositionsartsprDto positionsartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePositionsartspr(String positionsartCNr,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void removePositionsartspr(PositionsartsprDto positionsartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePositionsartspr(PositionsartsprDto positionsartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PositionsartsprDto positionsartsprFindByPrimaryKey(
			String positionsartCNrI, String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createWaehrung(WaehrungDto waehrungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createWechselkurs(WechselkursDto wechselkursDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public WechselkursDto wechselkursFindByPrimaryKeyOhneExc(
			String waehrungCNrVon, String waehrungCNrZu, java.util.Date tDatum)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal rechneUmInMandantenWaehrung(BigDecimal bdBetragI,
			BigDecimal bdKursMandantenwaehrungZuBelegwaehrung)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal rechneUmInAndereWaehrungGerundetZuDatum(BigDecimal bdBetragI,
			String currency1I, String currency2I, Date dDatumI, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;
}
