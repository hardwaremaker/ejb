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
package com.lp.server.system.ejbfac;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.system.ejb.Arbeitsplatz;
import com.lp.server.system.ejb.Arbeitsplatzparameter;
import com.lp.server.system.ejb.Parameter;
import com.lp.server.system.ejb.Parameteranwender;
import com.lp.server.system.ejb.ParameteranwenderPK;
import com.lp.server.system.ejb.Parametermandant;
import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.ejb.Parametermandantgueltigab;
import com.lp.server.system.ejb.ParametermandantgueltigabPK;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzDtoAssembler;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ArbeitsplatzparameterDtoAssembler;
import com.lp.server.system.service.ParameterDto;
import com.lp.server.system.service.ParameterDtoAssembler;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.server.system.service.ParameteranwenderDtoAssembler;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ParametermandantDtoAssembler;
import com.lp.server.system.service.ParametermandantgueltigabDto;
import com.lp.server.system.service.ParametermandantgueltigabDtoAssembler;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBExceptionLPwoRollback;
import com.lp.util.Helper;

@Stateless
public class ParameterFacBean extends Facade implements ParameterFac {

	@PersistenceContext
	private EntityManager em;

	// ARBEITSPLATZPARAMETER
	// Fix verdrahtete ArbeitsplatzParameter, Formatierung so belassen!
	public static final String[][] progArbeitsplatzParameter = {
			// Zeile1: Parameter
			// Zeile2: Datentyp
			// Zeile3: Wert
			// Zeile4: Bemerkung

			{
					ParameterFac.ARBEITSPLATZPARAMETER_MELDUNG_MEHRSPRACHIGKEIT_ANGEZEIGT,
					"java.lang.Boolean",
					"0",
					"Die Meldung \"Mehrsprachigkeit steht auf Ihrem Helium V nicht zur Verf\u00FCgung\" wurde bereits einmal angezeigt." },

			{ ParameterFac.ARBEITSPLATZPARAMETER_TAPI_LINE,
					"java.lang.Integer", "16", "Line-ID des TAPI-Services." },
			{ ParameterFac.ARBEITSPLATZPARAMETER_PORT_FINGERPRINTLESER,
					"java.lang.String", "COM3",
					"Port, an den der Fingerprintleser angeschlossen ist." },
			{
					ParameterFac.ARBEITSPLATZPARAMETER_FERTIGUNG_ANSICHT_OFFENE_LOSE,
					"java.lang.Boolean",
					"0",
					"In der Losauswahlliste werden standardm\u00E4\u00DFig nur die offenen Lose angezeigt." },
			{
					ParameterFac.ARBEITSPLATZPARAMETER_PFAD_MIT_PARAMETER_SCANTOOL,
					"java.lang.String",
					"c:/Programme/LPScan/LPScan.exe /F PDF /D 0 /S V /C 0 /A 200 /M H /P",
					"Pfad samt Parameter f\u00FCr das Logp-Scan-Tool. (c:/Programme/LPScan/LPScan.exe /F PDF /D 0 /S V /C 0 /A 200 /M H /P)" },
			{ ParameterFac.ARBEITSPLATZPARAMETER_BACKGROUND_ENABLED,
					"java.lang.Boolean", "1", "Hintergrundbild anzeigen" },
			{ ParameterFac.ARBEITSPLATZPARAMETER_AUFTRAGSABLIEFERUNG_IM_LOS,
					"java.lang.Boolean", "0",
					"Auftagsablieferung-Symbol im Los anzeigen" },

	};

	// MANDANTENPARAMETER
	// Fix verdrahtete Parameter, Formatierung so belassen!
	public static final String[][] progMandantParameter = {
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT,
					"1",
					"java.lang.Integer",
					"Monat, in dem das Geschf\u00E4tsjahr beginnt. 0=J\u00E4nner 11=Dezember",
					"Monat, in dem das Gesch\u00E4ftsjahr beginnt. 0=J\u00E4nner 11=Dezember" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS,
					"0",
					"java.lang.Integer",
					"Zur Jahreszahl des Beginndatums eines Gesch\u00E4ftsjahres 1 addieren",
					"Zur Jahreszahl des Beginndatums eines Gesch\u00E4ftsjahres 1 addieren" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_WAEHRUNGKURSAELTERALS, "7",
					"java.lang.Integer", "Wie alt darf W\u00E4hrungsinfo sein",
					"Wie alt darf W\u00E4hrungsinfo sein" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZESSION,
					"70", "java.lang.Integer", "Zession",
					"Default Zessionsbetrag f\u00FCr NeuKunden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ZESSIONSTEXT, "",
					"java.lang.String", "Zessionstext", "Default Zessionstext" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART,
					ArtikelFac.ARTIKELART_ARTIKEL, "java.lang.String",
					"Default Artikelart", "Default Artikelart" },
			/*
			 * { ParameterFac.KATEGORIE_KUNDE,
			 * ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART,
			 * ArtikelFac.ARTIKELART_ARTIKEL, "java.lang.Integer"},
			 */

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_AUFSCHLAG, "0",
					"java.lang.Double", "Default Aufschlag",
					"Default Aufschlag" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_DECKUNGSBEITRAG,
					"30", "java.lang.Double", "Default Deckungsbeitrag",
					"Default Artikeldeckungsbeitrag" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT,
					SystemFac.EINHEIT_STUECK, "java.lang.String",
					"Default Einheit", "Default Artikeleinheit" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_SOLLVERKAUF, "0",
					"java.lang.Double", "Default Soll", "Default Soll" },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
					"12", "java.lang.Integer",
					"Maximall\u00E4nge Artikelnummer",
					"Maximall\u00E4nge Artikelnummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_KURZE_ARTIKELNUMMER_ERLAUBT,
					"Ja", "java.lang.String", "kurze Artikelnummer erlaubt",
					"kurze Artikelnummer erlaubt" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_ARTIKELNUMMER,
					"5", "java.lang.Integer",
					"Mindestl\u00E4nge Artikelnummer",
					"Mindestl\u00E4nge Artikelnummer" },

			{
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE,
					"6", "java.lang.Integer", "Lieferantenrechnungsnummer",
					"L\u00E4nge der Lieferantenrechnungsnummer" },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG,
					"3", "java.lang.Integer",
					"Artikell\u00E4nge Herstellerbezeichnung",
					"Artikell\u00E4nge Herstellerbezeichnung" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER,
					"5", "java.lang.Integer", "Mindestl\u00E4nge Seriennummer",
					"Mindestl\u00E4nge Seriennummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER,
					"15", "java.lang.Integer",
					"Maximall\u00E4nge Seriennummer",
					"Maximall\u00E4nge Seriennummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_HANDLAGERBUCHUNG_MENGE_OBERGRENZE,
					"100", "java.math.BigDecimal",
					"Maximal abbuchbare Menge bis Warnung",
					"Maximal abbuchbare Menge bis Warnung" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_HANDLAGERBUCHUNG_WERT_OBERGRENZE,
					"100000", "java.math.BigDecimal",
					"Maximal abbuchbarer Wert bis Warnung",
					"Maximal abbuchbarer Wert bis Warnung" },

			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
					"0",
					"java.lang.Boolean",
					"Positionskontierung",
					"Boolean Wert Postionskontierung wenn 1 (ja) Artikel bestimmt MWST, wenn 0 (nein) gilt MWST des Kunden" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_EK_PREISE, "KRITISCH",
					"java.lang.String", "EK-Preise",
					"Anzeigen der EK-Preise nur wenn Recht darauf" },

			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
					ParameterFac.PARAMETER_FRACHTIDENT, "", "java.lang.String",
					"Fracht-Ident", "Sonderkreis f\u00FCr Frachtartikel" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEBITORENNUMMER_VON, "200000",
					"java.lang.Integer", "Debitorennummer-Von",
					"Debitorennummer-Vonl" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEBITORENNUMMER_BIS, "299999",
					"java.lang.Integer", "Debitorennummer-Bis",
					"Debitorennummer-Bis" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KREDITLIMIT, "1000",
					"java.lang.Integer", "Kreditlimit", "Kreditlimit" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KURZWAHL_VON, "0",
					"java.lang.Integer", "Kurzwahlkreis-Von",
					"Kurzwahlkreis-Von" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KURZWAHL_BIS, "4999",
					"java.lang.Integer", "Kurzwahlkreis-Bis",
					"Kurzwahlkreis-Bis" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_NETTOPREISE, "1",
					"java.lang.Boolean", "Nettopreise",
					"Kunden mit NettoPreisen anlegen" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_OFFENER_WE,
					"0", "java.lang.Boolean", "Offener WE",
					"Pro Kunden den offenen Wareneingang ermitteln" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_TOURSTATISTIK, "0",
					"java.lang.Boolean", "Tourstatistik", "Tourstatistik" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_NUMMER,
					"0", "java.lang.Boolean",
					"Kundennnummer rein numerisch erzeugen",
					"Kundennnummer rein numerisch erzeugen" },

			{ ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_KREDITLIMIT, "1000",
					"java.lang.Integer", "Kreditlimit", "Kreditlimit" },

			{ ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_KREDITOREN_VON, "300000",
					"java.lang.Integer", "Kreditoren-Von", "Kreditoren-Von" },

			{ ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_KREDITOREN_BIS, "399999",
					"java.lang.Integer", "Kreditoren-Bis", "Kreditoren-Bis" },

			{ ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_KURZWAHL_VON, "5000",
					"java.lang.Integer", "Kurzwahlkreis-Von",
					"Kurzwahlkreis-Von" },

			{ ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_KURZWAHL_BIS, "9999",
					"java.lang.Integer", "Kurzwahlkreis-Bis",
					"Kurzwahlkreis-Bis" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_NUMMER,
					"0", "java.lang.Boolean",
					"Lieferantennummer rein numerisch erzeugen",
					"Lieferantennummer rein numerisch erzeugen" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DELAY_E_MAILVERSAND, "5",
					"java.lang.Integer", "E-Mail Delay",
					"Zeit bis die E-Mail wirklich verschickt werden in Minuten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_EINKAUSPREIS_ABWEICHUNG, "10",
					"java.lang.Integer", "Einkaufspreisabweichung",
					"Einkaufspreisabweichung" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DEFAULT_KOSTENSTELLE, "10",
					"java.lang.String", "Default Kostenstelle",
					"Default Kostenstelle" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR, "0",
					"java.lang.Integer", "Gemeinkostenfaktor",
					"Gemeinkostenfaktor" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR,
					"2", "java.lang.String",
					"Anzahl der Stellen des Gesch\u00E4ftsjahres",
					"Anzahl der Stellen des Gesch\u00E4ftsjahres" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN,
					"/", "java.lang.String", "Belegnummerntrennzeichen",
					"Trennzeichen zwischen Gesch\u00E4ftsjahr und laufender Nummer" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER,
					"7", "java.lang.String",
					"Anzahl der Stellen der laufenden Nummer",
					"Anzahl der Stellen der laufenden Nummer" },

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN,
					"6", "java.lang.Integer",
					"Stellen der Kontonummer f\u00FCr Sachkonten",
					"Stellen der Kontonummer f\u00FCr Sachkonten" },

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_DEBITORENKONTEN,
					"6", "java.lang.Integer",
					"Stellen der Kontonummer f\u00FCr Debitorenkonten",
					"Stellen der Kontonummer f\u00FCr Debitorenkonten" },

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_KREDITORENKONTEN,
					"6", "java.lang.Integer",
					"Stellen der Kontonummer f\u00FCr Kreditorenkonten",
					"Stellen der Kontonummer f\u00FCr Kreditorenkonten" },

			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT,
					"42",
					"java.lang.Integer",
					"Default f\u00FCr die G\u00FCltigkeit eines Angebots in Tagen ab Belegdatum",
					"Default f\u00FCr die G\u00FCltigkeit eines Angebots in Tagen ab Belegdatum" },

			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEITSART,
					"1",
					"java.lang.Integer",
					"Legt fest, ob Parameter DEFAULT_ANGEBOT_GUELTIGKEIT verwendet wird",
					"G\u00FCltigkeitsart 0: Das Angebot gilt bis zum Ende des laufenden Gesch\u00E4ftsjahres, G\u00FCltigkeitsart 1: Das Angebot gilt ab Belegdatum f\u00FCr DEFAULT_ANGEBOT_GUELTIGKEIT Tage" },

			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_ANGEBOT_PRUEFESTANDARDDBARTIKEL,
					"1",
					"java.lang.Boolean",
					"Abfrage beim Abspeichern einer Ident Position?",
					"Soll es eine Benutzerabfrage beim Abspeichern einer Ident Position geben, wenn der DB des Artikels geringer ist, als der Standard DB des Artikels beim Mandanten?" },

			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_AUFTRAG, "7",
					"java.lang.Integer", "", "" }, // UW->JE

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_MAHNUNG_AB_RECHNUNGSBETRAG,
					"7",
					"java.math.BigDecimal",
					"Ab welchem Rechnungsbetrag werden Rechnungen gemahnt",
					"Ab welchem Rechnungsbetrag werden Rechnungen gemahnt (in Mandantenw\u00E4hrung)" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_MAHNUNG_ZAHLUNGSFRIST, "7",
					"java.lang.Integer", "Zahlungsfrist ab heute in Tagen",
					"Zahlungsfrist ab heute in Tagen" },

			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_LAGER, "12",
					"java.lang.Integer", // UW->CK fix verdrahteten Wert
					// pruefen, kommt vom Helium
					"Default Lager", "Default Lager" },

			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT,
					"h",
					"java.lang.String",
					"Default Zeiteinheit f\u00FCr den St\u00FCcklisten Arbeitsplan",
					"Legt fest, in welcher Einheit die St\u00FCckzeit und R\u00FCstzeit angezeigt werden soll. M\u00F6glich sind (h/min/s)" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN,
					"2",
					"java.lang.Integer",
					"Anzahl der Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK,
					"2",
					"java.lang.Integer",
					"Anzahl der EK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der EK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK,
					"2",
					"java.lang.Integer",
					"Anzahl der VK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der VK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN, "3",
					"java.lang.Integer",
					"Anzahl der Nachkommastellen f\u00FCr Mengenfelder im UI",
					"Anzahl der Nachkommastellen f\u00FCr Mengenfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.MATERIALGEMEINKOSTENFAKTOR, "0",
					"java.lang.Double",
					"Gemeinkostenfaktor fuer Materialkosten",
					"Gemeinkostenfaktor fuer Materialkosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR, "0",
					"java.lang.Double",
					"Gemeinkostenfaktor fuer Fertigungskosten",
					"Gemeinkostenfaktor fuer Fertigungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR, "0",
					"java.lang.Double",
					"Gemeinkostenfaktor fuer Entwicklungskosten",
					"Gemeinkostenfaktor fuer Entwicklungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR, "0",
					"java.lang.Double",
					"Gemeinkostenfaktor fuer Verwaltungskosten",
					"Gemeinkostenfaktor fuer Verwaltungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR, "0",
					"java.lang.Double",
					"Gemeinkostenfaktor fuer Vertriebskosten",
					"Gemeinkostenfaktor fuer Vertriebskosten" },

			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT,
					"14",
					"java.lang.Integer",
					"Default-Durchlaufzeit in Tagen (f\u00FCr die interne Bestellung)",
					"Default-Durchlaufzeit in Tagen (f\u00FCr die interne Bestellung)" },

			{
					ParameterFac.KATEGORIE_GUTSCHRIFT,
					ParameterFac.GUTSCHRIFT_VERWENDET_NUMMERNKREIS_DER_RECHNUNG,
					"0",
					"java.lang.Boolean",
					"Gutschriften und Rechnungen verwenden denselben Nummernkreis",
					"Gutschriften und Rechnungen verwenden denselben Nummernkreis" },

			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.FERTIGUNG_FERTIGUNGSBEGLEITSCHEIN_MIT_SOLLDATEN,
					"1", "java.lang.Boolean",
					"Fertigungsbegleitschein mit Solldaten drucken",
					"Fertigungsbegleitschein mit Solldaten drucken" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_EINGANGSRECHNUNG,
					"c:\\export_er.csv", "java.lang.String",
					"Default Export-Zieldatei Eingangsrechnung",
					"Default Export-Zieldatei Eingangsrechnung" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_RECHNUNG,
					"c:\\export_ar.csv", "java.lang.String",
					"Default Export-Zieldatei Rechnung",
					"Default Export-Zieldatei Rechnung" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_GUTSCHRIFT,
					"c:\\export_gs.csv", "java.lang.String",
					"Default Export-Zieldatei Gutschrift",
					"Default Export-Zieldatei Gutschrift" },

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_MONATE,
					"1",
					"java.lang.Integer",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Monaten",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Monaten" },

			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_TAGE,
					"15",
					"java.lang.Integer",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Tagen",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Tagen" },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.ANWESENHEITSLISTE_TELEFON_PRIVAT_ANZEIGEN,
					"1",
					"java.lang.Boolean",
					"Anzeige der Privaten Telefonnummer in der Anwesenheitsliste",
					"Anzeige der Privaten Telefonnummer in der Anwesenheitsliste" },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS,
					"15",
					"java.lang.Integer",
					"Wie lange darf man die Zeitbuchungen des Vormonats ver\u00E4ndern",
					"Bis zu welchem Tag des Aktuellen Monats darf man die Zeitbuchungen des Vormonats ver\u00E4ndern" },

			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG,
					"10",
					"java.lang.Integer",
					"Erh\u00F6hung, wenn eine neue Position eingef\u00FCgt wird",
					"Wert, um den die Arbeitsgangnummer erh\u00F6ht wird, wenn eine neue Position eingef\u00FCgt wird" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_SACHKONTEN,
					"c:\\export_sachkonten.csv", "java.lang.String",
					"Default Export-Zieldatei Sachkonten",
					"Default Export-Zieldatei Sachkonten" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_DEBITORENKONTEN,
					"c:\\export_debitorenkonten.csv", "java.lang.String",
					"Default Export-Zieldatei Debitorenkonten",
					"Default Export-Zieldatei Debitorenkonten" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_KREDITORENKONTEN,
					"c:\\export_kreditorenkonten.csv", "java.lang.String",
					"Default Export-Zieldatei Kreditorenkonten",
					"Default Export-Zieldatei Kreditorenkonten" },

			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_AG_STKL_AUFLOESUNG_TIEFE,
					"3",
					"java.lang.Integer",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_AGVORKALK_STKL_AUFLOESUNG_TIEFE,
					"3",
					"java.lang.Integer",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Vorkalkulation Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Vorkalkulation Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE,
					"3",
					"java.lang.Integer",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AB Vorkalkulation Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AB Vorkalkulation Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN,
					"1",
					"java.lang.Boolean",
					"Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN,
					"1",
					"java.lang.Boolean",
					"Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN,
					"1",
					"java.lang.Boolean",
					"Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{
					ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN,
					"1",
					"java.lang.Boolean",
					"Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN,
					"1",
					"java.lang.Boolean",
					"Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL, "0",
					"java.lang.String", "Amtsleitungsvorwahl",
					"Amtsleitungsvorwahl zum direkten Faxversand" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_INLANDSVORWAHL, "0",
					"java.lang.String", "Inlandsvorwahl.",
					"Inlandsvorwahl. z.B. 0" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_AUSLANDSVORWAHL, "00",
					"java.lang.String", "Auslandsvorwahl.",
					"Auslandsvorwahl. z.B. 00" },

			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_OFFSET_LIEFERZEIT_IN_TAGEN, "14",
					"java.lang.Integer",
					"Anzahl der Tage ab Bestelldatum bis zur Lieferung",
					"Anzahl der Tage ab Bestelldatum bis zur Lieferung" },

			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN,
					"1", "java.lang.Boolean",
					"Kommentar am Fertigungsbegleitschein drucken",
					"Kommentar am Fertigungsbegleitschein drucken" },

			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG, "14",
					"java.lang.Integer", "Default- Lieferzeit im Auftrag",
					"Default- Lieferzeit im Auftrag" },

			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_ANGEBOT, "0",
					"java.lang.Integer", "Default- Lieferzeit im Angebot",
					"Default- Lieferzeit im Angebot" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER,
					"1",
					"java.lang.Boolean",
					"F\u00FCr die Berechnung des Gestpreises wird das Hauptlager herangezogen.",
					"Wenn Ja:F\u00FCr die Berechnung des Gestehungspreises wird das Hauptlager herangezogen. Wenn Nein: Der Gestehungspreis berechnet sich aus dem Mittelwert der Gestehungspreise aller L\u00E4ger." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN, "0",
					"java.lang.Boolean", "Logo immer drucken",
					"Logo immer drucken" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_A, "80",
					"java.lang.Integer",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse A",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse A" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_B, "15",
					"java.lang.Integer",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse B",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse B" },
			{
					ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_AG_VORKALK_AUFLOESUNG_MAX_TIEFE,
					"99",
					"java.lang.Integer",
					"Maximale Tiefe der Ebenenaufl\u00F6sung in der AG- Vorkalkulation",
					"Maximale Tiefe der Aufl\u00F6sung von St\u00FCcklisten und Angebotst\u00FCcklisten zusammen in der Angebotsvorkalkulation." },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_GERINGEREN_VKPREIS_VORSCHLAGEN,
					"1",
					"java.lang.Boolean",
					"Geringeren VK-Preis f\u00FCr einen Artikel vorschlagen",
					"Wenn aufgrund der hinterlegten Preisinformationen ein geringerer VK-Preis f\u00FCr einen Artikel m\u00F6glich w\u00E4re, als die VK-Preisfindung liefert, erh\u00E4lt der Benutzer einen entsprechenden Hinweis." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL, "",
					"java.lang.String",
					"Default-Arbeitszeitartikel f\u00FCr BDE (Artikelnummer)",
					"Default-Arbeitszeitartikel f\u00FCr BDE (Artikelnummer)." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LAENGE_ZESTIFTKENNUNG, "6",
					"java.lang.Integer",
					"L\u00E4nge der Kennung eines ZE-Stiftes",
					"L\u00E4nge der Kennung eines ZE-Stiftes." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZESTIFT_ABSTAND_BUCHUNGEN,
					"10",
					"java.lang.Integer",
					"Mindestabstand gleicher Zeitbuchungen in Sekunden",
					"Wenn dieser unterschritten wird, wird die Zeitbuchung des ZE-Stiftes verworfen." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZESTIFT_STUECKRUECKMELDUNG,
					"0",
					"java.lang.Boolean",
					"Gutst\u00FCck um 1 erh\u00F6hen bei ZE-Stiftbuchung.",
					"Gutst\u00FCck um 1 erh\u00F6hen, wenn per ZE-Stift auf einen Arbeitsgang gebucht wird." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_LEERZEILE_NACH_MENGENBEHAFTETERPOSITION,
					"0",
					"java.lang.Boolean",
					"Leerzeile nach einer mengenbehafteten Position",
					"Am Belegdruck automatisch eine Leerzeile nach einer mengenbehafteten Position einf\u00FCgen" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_RABATTIERBAR, "1",
					"java.lang.Boolean",
					"Default- Einstellung der Option -Rabattierbar-",
					"Default- Einstellung der Option -Rabattierbar-" },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT,
					"",
					"java.lang.String",
					"Unterdr\u00FCcke Position mit diesem Artikel am Fertigungsbegleitschein.",
					"Eine Arbeitsplanposition mit diesem Artikel in Kombination mit einer Maschine wird am Fertigungsbegleitschein nicht angedruckt." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZUTRITT_DATEN_VORLADEN,
					"15",
					"java.lang.Integer",
					"Anzahl der Tage, f\u00FCr die Zutrittsdaten generiert werden.",
					"Setzt die Anzahl der Tage fest, f\u00FCr die Zutrittsdaten generiert werden." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZUTRITT_DEFAULT_ZUTRITTSKLASSE, "",
					"java.lang.String", "Default- Zutrittsklasse.",
					"Default- Zutrittsklasse." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ANZAHL_GRUPPEN,
					"3", "java.lang.Integer",
					"Anzahl der dargestellten T\u00E4tigkeitsgruppen",
					"Anzahl der dargestellten T\u00E4tigkeitsgruppen" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ZEITRAUM,
					"20", "java.lang.Integer",
					"Darstellungszeitraum der Kapazit\u00E4tsvorschau",
					"Darstellungszeitraum der Kapazit\u00E4tsvorschau" },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_LOGON_LOESCHEN,
					"30",
					"java.lang.Integer",
					"Anzahl der Tage, nach der Logon-Eintr\u00E4ge gel\u00F6scht werden",
					"Anzahl der Tage, nach der Logon-Eintr\u00E4ge gel\u00F6scht werden" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_EXPORTZIEL,
					"c:\\export_zahlungen.csv", "java.lang.String",
					"Default Export-Zieldatei Zahlungen",
					"Default Export-Zieldatei Zahlungen" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_MIT_SKONTO, "1",
					"java.lang.Boolean", "Default mit Skonto bezahlen",
					"Default mit Skonto bezahlen" },

			{
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_PERIODE,
					"7",
					"java.lang.Integer",
					"Default Zeitraum zwischen den Zahlungsl\u00E4ufen in Tagen",
					"Default Zeitraum zwischen den Zahlungsl\u00E4ufen in Tagen" },

			{
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_SKONTOUEBERZIEHUNGSFRIST,
					"1", "java.lang.Integer",
					"Default Skonto\u00FCberziehungsfrist in Tagen",
					"Default Skonto\u00FCberziehungsfrist in Tagen" },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE,
					"0",
					"java.lang.Boolean",
					"Anzeigen der Vertreter in Angebot- bzw. Auftragskopfdaten.",
					"Anzeigen der Vertreter in Angebot- bzw. Auftragskopfdaten." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
					"0",
					"java.lang.Boolean",
					"Arbeitszeitartikel f\u00FCr ZE kommen aus der Personalverf\u00FCgbarkeit.",
					"Arbeitszeitartikel f\u00FCr Zeiterfassung kommen aus der Personalverf\u00FCgbarkeit." },
			{
					ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
					ParameterFac.PARAMETER_RECHNUNG_BELEGNUMMERSTARTWERT,
					"1",
					"java.lang.Integer",
					"Startwert der Rechnungsnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Rechnungsnummer im neuen Gesch\u00E4ftsjahr." },

			{
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_EINGANGSRECHNUNG_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer",
					"Startwert der ER-Nummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der ER-Nummer im neuen Gesch\u00E4ftsjahr." },

			{ ParameterFac.KATEGORIE_GUTSCHRIFT,
					ParameterFac.PARAMETER_GUTSCHRIFT_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_ANFRAGE,
					ParameterFac.PARAMETER_ANFRAGE_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_LIEFERSCHEIN_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },

			{
					ParameterFac.KATEGORIE_ANFRAGE,
					ParameterFac.PARAMETER_ANFRAGE_ANSP_VORBESETZEN,
					"1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen." },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_ANSP_VORBESETZEN,
					"1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen." },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					ParameterFac.PARAMETER_ANGEBOTSTKL_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_LIEFERSCHEIN_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
					ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_LAENGE_PARTNER_KURZBEZEICHNUNG,
					"25",
					"java.lang.Integer",
					"L\u00E4nge des Feldes Kurzbezeichnung in Partner/Kunde/Lieferant (Max.:40)",
					"L\u00E4nge des Feldes Kurzbezeichnung in Partner/Kunde/Lieferant. Gr\u00F6sstm\u00F6glicher Wert: 40" },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ,
					"1",
					"java.lang.Boolean",
					"Suchen inklusive Kurzbezeichnung in Partner/Kunde/Lieferant",
					"Wenn nach dem Filterkriterium -Firma- in Partner/Kunde/Lieferant gesucht wird, wird die Kurzbezeichnung ebenfalls durchsucht." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH,
					"0",
					"java.lang.Boolean",
					"Zeitbuchungen sind auch auf erledigte Auftr\u00E4ge/Lose m\u00F6glich.",
					"Zeitbuchungen sind auch auf erledigte Auftr\u00E4ge/Lose m\u00F6glich." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH,
					"0", "java.lang.Boolean",
					"Zeitbuchungen sind auch auf angelegte Lose m\u00F6glich.",
					"Zeitbuchungen sind auch auf angelegte Lose m\u00F6glich." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_KUNDE_STATT_BEZEICHNUNG_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Kunde statt Artikelbezeichnung in der Los-Auswahlliste anzeigen.",
					"Kunde statt Artikelbezeichnung in der Los-Auswahlliste anzeigen." },
			{
					ParameterFac.KATEGORIE_PARTNER,
					ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
					"0",
					"java.lang.Boolean",
					"Beidseitige Wildcard im Partnernamen in der Auswahlliste",
					"Beidseitige Wildcard im Filterkriterium Firma in der Kunden/Partner/Lieferantenauswahlliste." },
			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN,
					"0",
					"java.lang.Boolean",
					"Default-Wert f\u00FCr Option -Akzeptiert Teillieferungen- in Kunde.",
					"Default-Wert f\u00FCr Option -Akzeptiert Teillieferungen- in Kundenkonditionen." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_BDE_MIT_TAETIGKEIT, "1",
					"java.lang.Boolean",
					"Die HTML BDE-Station ben\u00F6tigt eine T\u00E4tigkeit.",
					"Die HTML BDE-Station ben\u00F6tigt eine T\u00E4tigkeit." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LEAD_IN_AUSWEISNUMMER_MECS,
					"",
					"java.lang.String",
					"Lead-In f\u00FCr Ausweisnummer im MECS-Terminal (Barcodescanner).",
					"Lead-In f\u00FCr Ausweisnummer im MECS-Terminal in Verbindung mit einem Barcodescanner. Der Wert ist per Default leer und wird mit $P bef\u00FCllt, wenn ein MECS-Terminal in Verbindung mit einem Barcodescanner verwendet wird." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
					"Kalenderwochen",
					"java.lang.String",
					"Wiederbeschaffungszeit in Kalenderwochen oder Tagen.",
					"Artikel- Wiederbeschaffungszeit in Kalenderwochen oder Tagen. Zul\u00E4ssige Werte: Kalenderwochen und Tage." },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_DEFAULT_CHARGENNUMMERNBEHAFTET,
					"0", "java.lang.Boolean",
					"Artikel ist per Default Chargennummernbehaftet.",
					"Artikel ist per Default Chargennummernbehaftet." },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_DEFAULT_SERIENNUMMERNBEHAFTET,
					"0", "java.lang.Boolean",
					"Artikel ist per Default Seriennummernbehaftet.",
					"Artikel ist per Default Seriennummernbehaftet." },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
					"0",
					"java.lang.Boolean",
					"Die Chargennummer beinhaltet das Mindesthaltbarkeitsdatum.",
					"Die Chargennummer beinhaltet in den ersten 8 Stellen das Mindesthaltbarkeitsdatum. (JJJJMMTT)" },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_EINGANG,
					"c:\\intrastat_eingang.csv", "java.lang.String",
					"Default Export-Zieldatei Intrastat-Eingang.",
					"Default Export-Zieldatei Intrastat-Eingang." },

			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_VERSAND,
					"c:\\intrastat_versand.csv", "java.lang.String",
					"Default Export-Zieldatei Intrastat-Versand.",
					"Default Export-Zieldatei Intrastat-Versand." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL_TELEFON, "",
					"java.lang.String",
					"Amtsleitungsvorwahl f\u00FCr Telefon.",
					"Amtsleitungsvorwahl f\u00FCr Telefon (Direktes w\u00E4hlen/TAPI)." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ,
					"",
					"java.lang.Integer",
					"Default- Mehrwersteuersatz.",
					"Default- Mehrwersteuersatz. Hier wird die Id des Mehrwertsteuersatzes aus der Datenbank (Tabelle LP_MWSTSATZ) eingetragen. Die Id muss vom richtigen Mandanten sein." },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_BONITAETSWARNUNGSZEITRAUM, "11",
					"java.lang.Integer",
					"Bonit\u00E4tswarnungszeitraum im Monaten.",
					"Bonit\u00E4tswarnungszeitraum im Monaten." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM,
					"14", "java.lang.Integer",
					"Verdichtungszeitraum in Tagen.",
					"Verdichtungszeitraum in Tagen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN,
					"0",
					"java.lang.Boolean",
					"Die Istzeiten werden den Sollzeiten gleichgesetzt (Nachkalkulation).",
					"F\u00FCr die Nachkalkulation werden die Istzeiten den Sollzeiten gleichgesetzt." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL,
					"0",
					"java.lang.Boolean",
					"Im Fertigungsbegleitschein wird das Material mit angedruckt.",
					"Im Fertigungsbegleitschein wird das Material mit angedruckt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOS_BELEGNUMMERSTARTWERT_FREIE_LOSE,
					"1", "java.lang.Integer",
					"Startwert der Losnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Losnummer im neuen Gesch\u00E4ftsjahr." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN,
					"0",
					"java.lang.Integer",
					"Auftragsbezogene Losnummern erzeugen.",
					"0= Losnummer fortlaufend, 1= Auftragsbezogene Losnummer , 2= Auftragsbezogene Losnummer nach Bereichen" },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDE_MIT_NUMMER, "0",
					"java.lang.Boolean", "Kunden haben eine Kundennummer.",
					"Kunden haben eine Kundennummer." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_AUTOMATISCHES_KOMMT,
					"0",
					"java.lang.Integer",
					"Automatisches Kommt.",
					"0= Aus, 1= Kommt/ Unterbrechung- Ende wird automatisch gebucht, wenn noch nicht vorhanden, 2= Kommt wird gebucht, wenn noch nicht vorhanden" },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE,
					FibuExportFac.VARIANTE_KOSTENSTELLEN, "java.lang.String",
					"Definiert die Art des Exports.",
					"Definiert die Art des Exports." },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_ZIELPROGRAMM,
					FibuExportFac.ZIEL_RZL, "java.lang.String",
					"Ziel-Buchhaltungssoftware.", "Ziel-Buchhaltungssoftware." },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT, "0",
					"java.lang.Boolean", "\u00DCberschriften exportieren.",
					"Die Spalten-\u00DCberschriften in der Exportdatei angeben." },
			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR,
					"n/a", "java.lang.String",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe.",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe (Rechnung/Gutschrift)." },
			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_ER,
					"n/a", "java.lang.String",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe.",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe (Eingangsrechnung)." },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_BMD_BENUTZERNUMMER,
					"01", "java.lang.String", "Benutzernummer in BMD.",
					"Benutzernummer in BMD (2 Ziffern)." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BEWEGUNGSMODULE_SORTIERUNG_PARTNER_ORT,
					"0",
					"java.lang.Boolean",
					"Default-Ausschaltet",
					"Einschalten/ Ausschalten - Sortierung nach Partner und Ort in die Bewegungsmodule.Neustart erforderlich." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELNUMMER_ZEICHENSATZ,
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_\u00C4\u00D6\u00DC",
					"java.lang.String",
					"Bestimmt die in einer Artikelnummer verwendbaren Zeichen.",
					"Bestimmt die in einer Artikelnummer verwendbaren Zeichen." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER,
					"0", "java.lang.Boolean",
					"Direktfilter -Suche nach AG/AK- statt Referenznummer.",
					"Direktfilter -Suche nach Artikelgruppe/Artikelklasse- statt Referenznummer." },
			{
					ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
					ParameterFac.PARAMETER_WA_NUR_MIT_LS,
					"0",
					"java.lang.Boolean",
					"Der Warenausgang darf ausschlie\u00DFlich mit LS gebucht werden.",
					"Der Warenausgang darf ausschlie\u00DFlich mit LS gebucht werden." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSERIENNUMMERN_MIT_ETIKETTEN,
					"0",
					"java.lang.Boolean",
					"Auftrags Seriennummer fortlaufend ab Start mit Etiketten.",
					"Auftrags Seriennummer fortlaufend ab Start mit Etiketten." },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN,
					"1", "java.lang.Boolean",
					"Unterst\u00FCcklisten werden automatisch ausgegeben.",
					"Unterst\u00FCcklisten werden automatisch ausgegeben." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN,
					"0",
					"java.lang.Boolean",
					"Bei Erledigung eines Loses wird das fehlende Material nachgebucht.",
					"Bei Erledigung eines Loses wird das fehlende Material nachgebucht." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_RAHMENAUFTRAEGE_IN_FERTIGUNG_VERWENDBAR,
					"0",
					"java.lang.Boolean",
					"In den Los-Kopfdaten k\u00F6nnen auch Rahmenauftr\u00E4ge ausgew\u00E4hlt werden.",
					"In den Los-Kopfdaten k\u00F6nnen auch Rahmenauftr\u00E4ge ausgew\u00E4hlt werden." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Abmessungen statt Zusatzbez. in der Artikel-Auswahlliste anzeigen.",
					"Abmessungen statt Zusatzbezeichnung in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_RANKINGLISTE_ZUSATZSTATUS, "",
					"java.lang.Integer",
					"I_ID des Zusatzstatus f\u00FCr Rankingsliste.",
					"I_ID des Zusatzstatus f\u00FCr Rankingsliste." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREIS_STATT_GESTPREIS_IN_ARTIKELAUSWAHL,
					"0",
					"java.lang.Boolean",
					"Zeigt den ersten berechneten VK-Preis anstatt des Gestpreises an.",
					"Zeigt den ersten berechneten VK-Preis anstatt des Gestpreises in der Artikel-Auswahlliste an." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_AB_MONATE,
					"6",
					"java.lang.Integer",
					"Ab wievielen Monaten wird der Gestehungspreis abgewertet.",
					"Ab wievielen Monaten wird der Gestehungspreis abgewertet." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_PROZENT_PRO_MONAT,
					"10",
					"java.lang.Double",
					"Um wieviele Prozent wird der Gestehungspreis pro Monat abgewertet.",
					"Um wieviele Prozent wird der Gestehungspreis pro Monat abgewertet." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER,
					"0",
					"java.lang.Integer",
					"Anzeige des Anlegers/\u00C4nderers anstatt des Vertreters in VK-Belegen.",
					"0-Zeigt den Vertreter.\n1-Zeigt den Anleger.\n2-Zeigt den \u00C4nderer" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_EINKAUFSPREIS_ZURUECKPFLEGEN,
					"0",
					"java.lang.Integer",
					"Der Einkaufspreis wird in den Artikellieferant zur\u00FCckgepflegt.",
					"0 ... nicht zur\u00FCckpflegen 1 ... Staffelpreis zur\u00FCckpflegen per default angehakt. 2 ... immer nur Einzelpreiszur\u00FCckpflegen per default angehakt." },
			{ ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_PROJEKT_OFFSET_ZIELTERMIN, "90",
					"java.lang.Integer", "Default-Zieltermin in 90 Tage.",
					"Default-Zieltermin in 90 Tage." },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK,
					"0.10", "java.lang.Double", "Abweichung Summenausdruck.",
					"Abweichung Summenausdruck." },
			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_ZAHLUNGSMORAL_MONATE,
					"12",
					"java.lang.Integer",
					"Anzahl der Monate, die f\u00FCr die Zahlungsmoral ber\u00FCcksichtigt werden.",
					"Anzahl der Monate, die f\u00FCr die Berechnung der Zahlungsmoral ber\u00FCcksichtigt werden." },
			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_ZAHLUNGSMORAL_ANZAHL_RECHNUNGEN,
					"3",
					"java.lang.Integer",
					"Anzahl d. Rechnungen, die f\u00FCr die Zahlungsmoral ber\u00FCcksichtigt werden",
					"Anzahl der Rechnungen, die f\u00FCr die Berechnung der Zahlungsmoral ber\u00FCcksichtigt werden" },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG,
					"0",
					"java.lang.Boolean",
					"Default-Wert f\u00FCr die Option 'Materialbuchung bei Ablieferung'",
					"Default-Wert f\u00FCr die Option 'Materialbuchung bei Ablieferung'" },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM,
					"15",
					"java.lang.Integer",
					"Belege d\u00FCrfen bis zum angegebenen Tag im alten Monat angelegt werden.",
					"Belege d\u00FCrfen bis zum angegebenen Tag im alten Monat angelegt werden." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD,
					"0",
					"java.lang.Boolean",
					"In den Artikelkopfdaten ist die Artikelgruppe ein Pflichtfeld.",
					"In den Artikelkopfdaten ist die Artikelgruppe ein Pflichtfeld." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERSTANDSLISTE_EKPREIS_WENN_GESTPREIS_NULL,
					"1",
					"java.lang.Boolean",
					"EK-Preis des 1. Lieferanten, wenn der Gestehungspreis 0 ist.",
					"Wenn der Gestehungspreis 0 ist, dann wird stattdessen der EK-Preis des 1. Lieferanten in der Lagerstandsliste angezeigt." },

			{
					ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_A,
					"1600",
					"java.lang.Integer",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse A",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse A" },
			{
					ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_B,
					"1200",
					"java.lang.Integer",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse B",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse B" },
			{
					ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_C,
					"800",
					"java.lang.Integer",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse C",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse C" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_MAXIMALELAENGE_DURCHWAHL_ZENTRALE,
					"3", "java.lang.Integer",
					"Anzahl der Stellen der Durchwahl der Zentrale",
					"Anzahl der Stellen der Durchwahl der Zentrale" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_URL_ANWENDERSPEZIFISCHE_HILFE, " ",
					"java.lang.String", "URL der Anwenderspezifischen Hilfe",
					"URL der Anwenderspezifischen Hilfe" },
			{
					ParameterFac.KATEGORIE_KUECHE,
					ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_OSCAR,
					" ",
					"java.lang.String",
					"Pfad zu den OSCAR- Kassenimport- Dateien des K\u00FCchenmoduls",
					"Pfad zu den OSCAR- Kassenimport- Dateien des K\u00FCchenmoduls" },
			{
					ParameterFac.KATEGORIE_KUECHE,
					ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_ADS,
					" ",
					"java.lang.String",
					"Pfad zu den ADS- Kassenimport- Dateien des K\u00FCchenmoduls",
					"Pfad zu den ADS- Kassenimport- Dateien des K\u00FCchenmoduls" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_AELTESTE_CHARGENNUMMER_VORSCHLAGEN,
					"0", "java.lang.Boolean",
					"Automatisch die \u00E4lteste Charge vorgeschlagen.",
					"Automatisch die \u00E4lteste Charge vorgeschlagen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOSAUSGABE_AUTOMATISCH,
					"0",
					"java.lang.Boolean",
					"Losausgabe ohne Snr/Chnr-Ausgabedialog.",
					"Losausgabe ohne Snr/Chnr-Ausgabedialog. Serien-/Chargennummer werden der Reihe nach verwendet." },
			{
					ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_LIEFERSCHEIN_UEBERNAHME_NACH_ANSPRECHPARTNER,
					"0",
					"java.lang.Boolean",
					"Reihung nach Ansp., wenn mehrere Lieferscheine \u00FCbernommen werden.",
					"Wenn mehrere Lieferscheine in die Rechnung \u00FCbernommen werden dann werden diese nach dem Ansprechpartner sortiert eingef\u00FCgt." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGER_LOGGING,
					"0",
					"java.lang.Boolean",
					"Loggt jede Zu- bzw. Abgangsbuchung in der Protokolltabelle mit",
					"Loggt jede Zu- bzw. Abgangsbuchung in LP_PROTOKOLL mit." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_GANZE_CHARGEN_VERWENDEN,
					"0",
					"java.lang.Boolean",
					"Nach M\u00F6glichkeit immer ganze Chargen im Los verwenden.",
					"Nach M\u00F6glichkeit immer ganze Chargen im Los verwenden." },
			{ ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ, "0",
					"java.lang.Boolean",
					"Geplanten Umsatz in der Projektauswahl anzeigen.",
					"Geplanten Umsatz in der Projektauswahl anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DEFAULT_DOKUMENTE_ANHAENGEN, "0",
					"java.lang.Boolean",
					"Default-Wert der CheckBox -Dokumente anh\u00E4ngen-",
					"Default-Wert der CheckBox -Dokumente anh\u00E4ngen- im Lieferschein-Versand." },
			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER,
					"0",
					"java.lang.Boolean",
					"Debitorennummer bei Ausgangsbelegen anlegen, wenn nicht vorhanden.",
					"Debitorennummer bei Ausgangsbelegen automatisch anlegen, wenn nicht vorhanden." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ERLAUBTE_ABWEICHUNG_INVENTURLISTE,
					"1000",
					"java.lang.Integer",
					"Bringt Warnung wenn Abweichung des Inventurstand gr\u00F6\u00DFer als Wert.",
					"Bringt eine Warnung wenn Abweichung des Inventurstand gr\u00F6\u00DFer als Wert ist." },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_FERTIGUNGSGRUPPE_VORBESETZEN,
					"1",
					"java.lang.Boolean",
					"Die Fertigungsgruppe wird mit der ersten Fertigungsgruppe vorbesetzt.",
					"Die Fertigungsgruppe wird mit der ersten Fertigungsgruppe vorbesetzt." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ENDE_TERMIN_ANSTATT_BEGINN_TERMIN_ANZEIGEN,
					"0", "java.lang.Boolean",
					"Ende-Termin anstatt des Beginn-Termins anzeigen.",
					"Ende-Termin anstatt des Beginn-Termins anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ANREDE_MIT_VORNAME, "0",
					"java.lang.Boolean", "Briefanrede mit Vorname",
					"Briefanrede mit Vorname" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WE_REFERENZ_IN_STATISTIK, "0",
					"java.lang.Boolean",
					"Wareneingangsreferenz in Artikelstatistik andrucken.",
					"Wareneingangsreferenz in Artikelstatistik andrucken." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG, "0",
					"java.lang.Boolean",
					"Lagerzugang mit Hersteller/Ursprungsland.",
					"Lagerzugang mit Hersteller/Ursprungsland." },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_EINZELRECHNUNG_EXPORTPFAD, "",
					"java.lang.String",
					"Server-Pfad des Einzelrechnungsexportes.",
					"Server-Pfad des Einzelrechnungsexportes." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_NEGATIVE_SOLLMENGEN_BUCHEN_AUF_ZIELLAGER,
					"0",
					"java.lang.Boolean",
					"Negative Sollmengen werden auf Ziellager gebucht.",
					"Negative Sollmengen werden auf Ziellager gebucht. Wenn 1 / dann wird das erste Lager der Los-Lagerentnahme verwendet." },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_LIEFERDAUER, "2",
					"java.lang.Integer", "Default- Lieferdauer.",
					"Default- Lieferdauer." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ABSENDERKENNUNG_STRASSE_ORT, "0",
					"java.lang.Boolean",
					"Absenderkennung im Format Name / Strasse Lkz-Plz-Ort.",
					"Absenderkennung im Format Name / Strasse Lkz-Plz-Ort." },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN, "3",
					"java.lang.Integer", "Default-Nachfasstermin in Tagen.",
					"Default-Nachfasstermin in Tagen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ABLIEFERUNGSPREIS_IST_DURCHSCHNITTSPREIS,
					"0", "java.lang.Boolean",
					"Der Preis der Losablieferung ist der Durchschnittspreis.",
					"Der Preis der Losablieferung ist der Durchschnittspreis." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKELKOMMENTAR_IST_HINWEIS,
					"0", "java.lang.Boolean",
					"Default Kommentarart ist -Hinweis bei Artikelauswahl-.",
					"Default Kommentarart ist -Hinweis bei Artikelauswahl-." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ERWEITERTER_URLAUBSANSPRUCH,
					"0",
					"java.lang.Integer",
					"Berechnungsart des Urlaubsanspruches",
					"0 = Standard.  1 = Voller Urlaubsanspruch bis zum Eintritt am 30.6. 2 = Berechnung nach TV\u00D6D" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE, "1",
					"java.lang.Integer", "Mindestl\u00E4nge der Chargennummer",
					"Mindestl\u00E4nge der Chargennummer" },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE,
					"0",
					"java.lang.Integer",
					"Quelle der Personalkosten",
					"Quelle der Personalkosten: 0=Gestehungspreis aus T\u00E4tigkeit / 1=Kosten aus Personalgruppe / 2=Stundensatz aus Personalgehalt" },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0",
					"java.lang.Boolean",
					"Lagermindeststandswarnung im Auftrag.",
					"Lagermindeststandswarnung im Auftrag." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0",
					"java.lang.Boolean",
					"Lagermindeststandswarnung im Lieferschein.",
					"Lagermindeststandswarnung im Lieferschein." },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0",
					"java.lang.Boolean",
					"Lagermindeststandswarnung in der Rechnung.",
					"Lagermindeststandswarnung in der Rechnung." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOSAUSWAHL_TECHNIKERFILTER, "0",
					"java.lang.Boolean", "Losauswahl \u00FCber Techniker.",
					"Losauswahl \u00FCber Techniker / Gilt nur f\u00FCr Zeiterfassung." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_MAXIMALE_EINTRAEGE_SOLLZEITPRUEFUNG,
					"50",
					"java.lang.Integer",
					"Maximale Eintr\u00E4ge f\u00FCr Sollzeitpr\u00FCfung.",
					"Maximale Eintr\u00E4ge an Zeitdaten die f\u00FCr die Sollzeitpr\u00FCfung vorhanden sein d\u00FCrfen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG,
					"0",
					"java.lang.Boolean",
					"Grunds\u00E4tzlich keine automatische Materialentnahmen.",
					"Grunds\u00E4tzlich keine automatische Materialentnahmen weder bei Losausgabe noch bei Erledigung." },

			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_NUR_TERMINEINGABE, "0",
					"java.lang.Boolean", "Termineingabe ohne Durchlaufzeit.",
					"Termineingabe ohne Durchlaufzeit." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Auftrag statt Artikel in Losauswahlliste.",
					"Auftrag statt Artikel in Losauswahlliste." },
			{ ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_DURCHLAUFZEIT_IST_PFLICHTFELD, "0",
					"java.lang.Boolean",
					"Durchlaufzeit in St\u00FCckliste ist Pflichtfeld.",
					"Durchlaufzeit in St\u00FCckliste ist Pflichtfeld." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG, "0",
					"java.lang.Boolean", "Theoretische Ist-Zeit Rechnung.",
					"Theoretische Ist-Zeit Rechnung bei Umspann/R\u00FCstzeiten." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_SALDENABFRAGE_NUR_IST_STUNDEN_DES_AKTUELLEN_MONATS,
					"0",
					"java.lang.Boolean",
					"Es wird nur der aktuelle Monats IST-Saldo angezeigt.",
					"In der BDE-Station und am Terminal wird nur der aktuelle Monats IST-Saldo angezeigt, ohne \u00DCbertr\u00E4ge aus dem Vormonat." },
			{
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_SCHECKNUMMER,
					"-11",
					"java.lang.Integer",
					"Schecknummer.",
					"Schecknummer. Wird bei ER-Druck um 1 erh\u00F6ht. In ER-Druck nicht sichtbar bei -1" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_STATUSZEILE_KBEZ_STATT_VERPACKUNGSART,
					"0", "java.lang.Boolean",
					"Kurzbezeichnung statt Verpackungsart in Statuszeile",
					"Kurzbezeichnung+ Index statt Verpackungsart+ Bauform in Statuszeile" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_MASCHINENBEZEICHNUNG_ANZEIGEN,
					"0",
					"java.lang.Boolean",
					"Maschinenbezeichnung statt Artikelbezeichnung anzeigen.",
					"Im Lossollarbeitsplan die Maschinenbezeichnung statt Artikelbezeichnung anzeigen." },

			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOS_BEI_AUSGABE_GESTOPPT, "0",
					"java.lang.Boolean",
					"Das Los ist nach der Ausgabe automatisch gestoppt.",
					"Das Los ist nach der Ausgabe automatisch gestoppt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_KUNDE_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "Kunde im Los ist Pflichtfeld",
					"Kunde im Los ist Pflichtfeld" },
			{ ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_ARBEITSPLAN_DEFAULT_STUECKLISTE, "",
					"java.lang.String",
					"Default-Arbeitsplan einer neuen St\u00FCckliste.",
					"Artikelnummer des Default-Arbeitsplans einer neuen St\u00FCckliste." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FEHLMENGE_ENTSTEHEN_WARNUNG,
					"0",
					"java.lang.Boolean",
					"Warnung bei der Los-Ausgabe wenn Fehlmengen entstehen w\u00FCrden.",
					"Warnung bei der Los-Ausgabe wenn Fehlmengen entstehen w\u00FCrden." },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_LIEFERANTEN_AUTOMATISCH_SORTIEREN,
					"0",
					"java.lang.Boolean",
					"Automatische Reihung von Lieferanten durch Bestellung.",
					"Gibt an ob beim Statuswechsel der Bestellung von Angelegt auf Offen der ausgew\u00E4hlte Lieferant als bevorzugter Lieferant hinterlegt wird." },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Brutto-Summe statt Netto-Summe in Auswahlliste (RE+ER).",
					"Brutto-Summe statt Netto-Summe in Auswahlliste (RE+ER)." },
			{
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_OFFENE_MENGE_IN_SICHT_AUFTRAG_VORSCHLAGEN,
					"1",
					"java.lang.Boolean",
					"Die offene Menge in -Sicht Auftrag- als Positionsmenge vorschlagen.",
					"Die offene Menge in -Sicht Auftrag- als Positionsmenge vorschlagen." },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_IN_WEP_TEXTEINGABEN_ANZEIGEN, "0",
					"java.lang.Boolean",
					"Texteingaben in den Wareneingangspositionen anzeigen.",
					"Texteingaben in den Wareneingangspositionen anzeigen." },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_MAHNUNGSABSENDER, "0",
					"java.lang.Integer", "0= Administrator / 1=Anforderer",
					"0= Administrator / 1=Anforderer" },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_MAHNUNG_AUTO_CC, "0",
					"java.lang.Boolean",
					"Absender des Versandauftrages bekommt eine Kopie",
					"Absender des Versandauftrages bekommt eine Kopie" },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANFRAGEDATUM_VORBESETZEN, "1",
					"java.lang.Boolean", "Anfragedatum vorbesetzen",
					"Anfragedatum vorbesetzen" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_LIEFERADRESSE_IST_AUFTRAGSADRESSE,
					"1",
					"java.lang.Boolean",
					"Wenn Lieferadresse = Auftragsadresse dann diese nicht drucken.",
					"Wenn Lieferadresse = Auftragsadresse dann diese nicht drucken." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PARTNERBRANCHE_DEFINIERT_KOMMENTAR,
					"0",
					"java.lang.Boolean",
					"Es werden die Kommentare der entsprechenden Branche angedruckt.",
					"Es werden die Kommentare der entsprechenden Branche angedruckt." },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_PROJEKT_STATT_ORT_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Projekt statt Ort in Auswahlliste.",
					"Projekt statt Ort in Auswahlliste." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG, "0",
					"java.lang.Boolean",
					"Eindeutige Seriennummer \u00FCber alle Artikel.",
					"Eindeutige Seriennummer \u00FCber alle Artikel." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ALLE_LOSE_BERUECKSICHTIGEN,
					"0",
					"java.lang.Boolean",
					"In der Nachkalkulation werden alle Lose ber\u00FCcksichtigt.",
					"In der Nachkalkulation werden alle Lose ber\u00FCcksichtigt." },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_ARTIKELAUSWAHL, "1",
					"java.lang.Boolean",
					"0 = Lieferant der Bestellung 1 = Alle.",
					"0 = Lieferant der Bestellung 1 = Alle." },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_FIRMENNUMMER,
					"PZE001", "java.lang.String",
					"Zeitdatenexport Firmennummer",
					"Zeitdatenexport Firmennummer" },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIELPROGRAMM,
					"VARIAL", "java.lang.String",
					"Zeitdatenexport Zielprogramm",
					"Zeitdatenexport Zielprogramm" },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIEL,
					"c:/export_zeitdaten.csv", "java.lang.String",
					"Zeitdatenexport Exportpfad", "Zeitdatenexport Exportpfad" },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_PERIODENVERSATZ,
					"0", "java.lang.Integer",
					"Periodenversatz im Lohndatenxport",
					"Periodenversatz im Lohndatenxport" },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_LIEFERZEITVERSATZ, "14",
					"java.lang.Integer",
					"Default Lieferterminversatz in den Kopfdaten.",
					"Default Lieferterminversatz in den Kopfdaten. (-1 = Kein Vorschlag)" },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG, "0",
					"java.lang.Boolean", "Default Teillieferung.",
					"Default Teillieferung." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Artikelgruppe statt Zusatzbez. in der Artikel-Auswahlliste anzeigen.",
					"Artikelgruppe statt Zusatzbezeichnung in der Artikel-Auswahlliste anzeigen. Wirkt nur wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_WIEDERHOLENDER_AUFTRAG_VERSTECKT,
					"0",
					"java.lang.Boolean",
					"Bei einer Neuanlage.",
					"Wenn ein wiederholender Auftrag neu angelegt wird ist dieser per Default versteckt" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WARNUNG_WENN_LAGERPLATZ_BELEGT,
					"0",
					"java.lang.Boolean",
					"Warnung wenn Lagerplatz bereits durch einen Artikel belegt ist.",
					"Warnung wenn Lagerplatz bereits durch einen Artikel belegt ist." },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN, "1",
					"java.lang.Boolean", "Reverse Charge verwenden.",
					"Reverse Charge verwenden." },
			{ ParameterFac.KATEGORIE_ANFRAGE,
					ParameterFac.PARAMETER_MENGEN_IN_STAFFELN_UEBERNEHMEN, "0",
					"java.lang.Boolean",
					"Mengen in EK-Staffeln \u00FCbernehmen.",
					"Anlieferdaten mit Mengen >1 werden in die EK-Mengenstaffeln \u00FCbernommen." },

			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_BUCHUNG_IMMER_NACHHER_EINFUEGEN,
					"0", "java.lang.Boolean",
					"Zeitbuchung immer nachher einf\u00FCgen.",
					"Zeitbuchung immer nachher einf\u00FCgen." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Artikelgruppe in der Artikel-Auswahlliste anzeigen.",
					"Artikelgruppe in der Artikel-Auswahlliste anzeigen. Wirkt nur wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOSAUSGABELISTE2_AUTOMATISCH_DRUCKEN,
					"0", "java.lang.Boolean",
					"Losausgabeliste2 bei Los-Ausgabe automatisch drucken.",
					"Losausgabeliste2 bei Los-Ausgabe automatisch drucken." },
			{ ParameterFac.KATEGORIE_REKLAMATION,
					ParameterFac.PARAMETER_KUNDENREKLAMATION_DEFAULT, "1",
					"java.lang.Integer", "1 = Fertigung / 2 = Lieferant",
					"1 = Fertigung / 2 = Lieferant" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
					"1",
					"java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen, 1 = erster Ansp. aus Lieferadresse, 2 = Ansp aus Auftragsadresse \u00FCbernehmen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ANZEIGE_IN_ARBEITSPLAN, "0",
					"java.lang.Integer", "0 = h - ms / 1 = t - m",
					"0 = h - ms / 1 = t - m" },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG, "0",
					"java.lang.Boolean",
					"Default-Wert der Option -mit Zusammenfassung-",
					"Default-Wert der Option -mit Zusammenfassung- in AG + AB" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_KURZBEZEICHNUNG_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Kurzbezeichnung in der Artikel-Auswahlliste anzeigen.",
					"Kurzbezeichnung in der Artikel-Auswahlliste anzeigen." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_NEUER_ARTIKEL_IST_LAGERBEWIRTSCHAFTET,
					"1", "java.lang.Boolean",
					"Ein neuer Artikel ist Lagerbewirtschaftet.",
					"Ein neuer Artikel ist Lagerbewirtschaftet." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_HERSTELLERKURZZEICHENAUTOMATIK, "1",
					"java.lang.Boolean",
					"Herstellerkurzzeichen wird automatisch vergeben.",
					"Herstellerkurzzeichen wird automatisch vergeben." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS, "0",
					"java.lang.Boolean",
					"'Ausgegeben'-Status vor 'In Produktion'.",
					"'Ausgegeben'-Status vor 'In Produktion'." },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT,
					"0",
					"java.lang.Integer",
					"0 = Abgeschaltet / 1 = Solid Works / 2 = Siemens NX / 3 = INFRA",
					"0 = Abgeschaltet / 1 = Solid Works / 2 = Siemens NX / 3 = INFRA" },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_MAHNUNGEN_AB_GF_JAHR, "1900",
					"java.lang.Integer", "GF-Jahr ab dem gemahnt wird.",
					"GF-Jahr ab dem gemahnt wird." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_VKPREISEINGABE_NUR_NETTO,
					"0",
					"java.lang.Boolean",
					"In den VK-Belegen kann nur der Netto-Preis eingegeben werden.",
					"In den VK-Belegen kann nur der Netto-Preis eingegeben werden." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_DEFAULT_UNVERBINDLICH,
					"0",
					"java.lang.Boolean",
					"Default-Wert f\u00FCr -unverbindlich- in den Auftragskopfdaten.",
					"Default-Wert f\u00FCr -unverbindlich- in den Auftragskopfdaten." },
			{
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_ARTIKELBEZEICHNUNG_IN_LS_EINFRIEREN,
					"0",
					"java.lang.Boolean",
					"Bei LS-Erfassung auf -Sicht Auftrag- Artikelbezeichnungen einfrieren.",
					"Bei LS-Erfassung auf -Sicht Auftrag- Artikelbezeichnungen einfrieren." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ARTIKELLANGTEXTE_UEBERSTEUERBAR,
					"0",
					"java.lang.Boolean",
					"In den Bewegungsmodulen sind die Artikellangtexte \u00FCbersteuerbar.",
					"In den Bewegungsmodulen sind die Artikellangtexte \u00FCbersteuerbar." },

			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SOLLZEITPRUEFUNG,
					"1",
					"java.lang.Integer",
					"Sollzeitpr\u00FCfung in Zeitdaten",
					"Sollzeitpr\u00FCfung in Zeitdaten: 0=Aus, 1=Nur Lose, 2=Auftragspsition, 3=Auftrag gesamt" },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_TOLERANZ_BETRAGSUCHE, "10",
					"java.lang.Integer",
					"Toleranz der Betragssuche im Buchungsjournal (in %).",
					"Toleranz der Betragssuche im Buchungsjournal (in %)." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELKLASSE_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Artikelklasse in der Artikel-Auswahlliste anzeigen.",
					"Artikelklasse in der Artikel-Auswahlliste anzeigen. Wirkt nur, wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0 und ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE=0 " },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DRUCKVORSCHAU_AUTOMATISCH_BEENDEN,
					"1", "java.lang.Boolean",
					"Druckvorschau nach Druck beenden",
					"Druckvorschau nach Druck beenden" },

			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_INTERNE_BESTELLUNG_MIT_AUFTRAGSPOSITIONSBEZUG,
					"1", "java.lang.Boolean",
					"Lose aus interner Bestellung mit Auftragspositionsbezug.",
					"Lose aus interner Bestellung mit Auftragspositionsbezug." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN,
					"0",
					"java.lang.Boolean",
					"Beim Speichern einer SNR werden f\u00FChrende Nullen entfernt.",
					"Beim Speichern einer SNR werden f\u00FChrende Nullen entfernt." },

			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_PREISBASIS_VERKAUF,
					"0",
					"java.lang.Integer",
					"0=VKPreisbasis 1=Preisbasis lt. KD-Preisliste 2=Mengenstaffelrabatt",
					"0 = Verkaufspreisbasis  1 = Preisbasis lt. Kundenpreisliste (Es wird als Preisbasis der lt. Preisliste berechnete Betrag verwendet) 2 = Verkaufspreisbasis mit Mengenstaffelrabatt im Zusatzrabatt" },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_AB_ERLAUBTEM_KOMMT,
					"0", "java.lang.Boolean",
					"Automatische Pausen ab -fr\u00FChestem erlaubtem Kommt-",
					"Automatische Pausen ab -fr\u00FChestem erlaubtem Kommt-" },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION,
					"0",
					"java.lang.Integer",
					"0 = \u00FCbergeordnete St\u00FCckliste  1 = Fertigungssatzgr\u00F6\u00DFe",
					"0 = \u00FCbergeordnete St\u00FCckliste  1 = Fertigungssatzgr\u00F6\u00DFe" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KUNDENAUSWAHL, "1",
					"java.lang.Integer",
					"1 = Alle - 2 = Kunde - 3 = Interessent",
					"1 = Alle - 2 = Kunde - 3 = Interessent" },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_IMPORT_PLANKOSTEN_DATEI, "",
					"java.lang.String",
					"Dateiname zu XLS-Spreadsheet mit Plankostendaten",
					"Dateiname zu XLS-Spreadsheet mit Plankostendaten" },
			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_LIEFERSPERRE_AB_MAHNSTUFE, "3",
					"java.lang.Integer",
					"Mahnstufe, ab der eine Liefersperre gesetzt wird",
					"Mahnstufe, ab der eine Liefersperre gesetzt wird" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_LV_POSITION, "0",
					"java.lang.Boolean", "LV-Position in VK-Belegen anzeigen",
					"LV-Position in VK-Belegen anzeigen" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FEHLMENGEN_LOSBEZOGEN_SOFORT_AUFLOESEN,
					"0", "java.lang.Boolean",
					"Losbezogene Fehlmengen sofort aufloesen",
					"Losbezogene Fehlmengen sofort aufloesen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SCHWERWIEGENDE_FEHLER_VERSENDEN,
					"1", "java.lang.Boolean",
					"Schwerwiegende Fehlermeldungen an HeliumV versenden",
					"Schwerwiegende Fehlermeldungen an HeliumV versenden" },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ZUSAETZLICHER_DOKUMENTENSPEICHERPFAD,
					" ", "java.lang.String",
					"Zus\u00E4tzlicher Dokumentenspeicherpfad",
					"Zus\u00E4tzlicher Dokumentenspeicherpfad" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_ADRESSE_FUER_AUSDRUCK_MIT_ANREDE,
					"0", "java.lang.Boolean",
					"Anrede mitandrucken wenn keine Firma",
					"Anrede mitandrucken wenn keine Firma" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE, "0",
					"java.lang.Boolean",
					"Los-Ablieferung von Terminal bucht automatisch ein ENDE",
					"Los-Ablieferung von Terminal bucht automatisch ein ENDE" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_BEGINN_ENDE_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean",
					"Los-Beginn und Ende in Auswahlliste buchbar.",
					"Los-Beginn und Ende in Auswahlliste buchbar." },
			{ ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_DEFAULT_UEBERLIEFERBAR, "1",
					"java.lang.Boolean",
					"Default-Wert f\u00FCr die Option -\u00DCberlieferbar-",
					"Default-Wert f\u00FCr die Option -\u00DCberlieferbar-" },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_TOLERANZ_SUCHE_NACH_BETRAG, "10",
					"java.lang.Integer",
					"Toleranz in Prozent, wenn nach Betraegen gefiltert wird.",
					"Toleranz in Prozent, wenn nach Betraegen gefiltert wird." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_HANDBUCHUNGSART,
					"0",
					"java.lang.Integer",
					"0=Zugang 1=Abgang 2=Umbuchung",
					"Default-Wert f\u00FCr die Buchungsart in der Handlagerbewegung: 0=Zugang 1=Abgang 2=Umbuchung" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FEHLMENGE_STATT_PREIS_IN_LOSMATERIAL,
					"0", "java.lang.Boolean",
					"Fehlmenge statt Preis in Los-Material anzeigen.",
					"Fehlmenge statt Preis in Los-Material anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_LAGERPLATZ_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Lagerplaetze in Artikelauswahl anzeigen.",
					"Lagerplaetze in Artikelauswahl anzeigen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_VERKNUEPFTE_BESTELLDETAILS_ANZEIGEN,
					"0", "java.lang.Boolean",
					"Verknuepfte Bestelldetails in Losmaterial anzeigen.",
					"Verknuepfte Bestelldetails in Losmaterial anzeigen." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DAZUGEHOERT_BERUECKSICHTIGEN,
					"1",
					"java.lang.Boolean",
					"Den -Zugehoerigen Artikel- in allen Belegen ber\u00FCcksichtigen.",
					"Den -Zugehoerigen Artikel- in allen Belegen ber\u00FCcksichtigen." },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_PREISUEBERWACHUNG,
					"0",
					"java.lang.Boolean",
					"Preis\u00FCberwachung in der Liste der Bestellpositionen.",
					"Preis\u00FCberwachung in der Liste der Bestellpositionen." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
					"0",
					"java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen 1 = erster Ansp. aus Rechnungsadresse 2 = Ansp aus Auftragsadresse \u00FCbernehmen" },
			{
					ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_PROJEKT_BELEGNUMMERSTARTWERT,
					"1",
					"java.lang.Integer",
					"Startwert der Projekt-Nummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Projekt-Nummer im neuen Gesch\u00E4ftsjahr." },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_MONATSBESTELLUNGSART,
					"0",
					"java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = mit automatischem Wareneingang 1 = ohne automatischem Wareneingang 2 = Bestellungsimport" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ADRESSVORBELEGUNG,
					"0",
					"java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nach H\u00E4ufigkeit 1 = Auftragsadresse = Lieferadresse und Rechnungsadresse anhand Auftragsadresse, 2 = Lieferadresse anhand H\u00E4ufigkeit und Rechnungsadresse anhand Auftragsadresse" },
			{
					ParameterFac.KATEGORIE_REKLAMATION,
					ParameterFac.PARAMETER_BESTELLUNG_UND_WARENEINGANG_SIND_PFLICHTFELDER,
					"1", "java.lang.Boolean",
					"Bestellung und Wareneingang sind Pflichtfelder.",
					"Bestellung und Wareneingang sind Pflichtfelder." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOSBUCHUNG_OHNE_POSITIONSBEZUG_BEI_STUECKRUECK,
					"0",
					"java.lang.Boolean",
					"Los-Position ist kein Pflichtfeld, wenn St\u00FCckr\u00FCckmeldung eingeschaltet",
					"Los-Position in Zeiterfassung ist kein Pflichtfeld, wenn St\u00FCckr\u00FCckmeldung eingeschaltet" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_TRIGGERT_TRUMPF_TOPS_ABLIEFERUNG,
					"",
					"java.lang.String",
					"Arbeitszeitartikel f\u00FCr TOPS-Ablieferung in HTML-BDE (Artikelnummer)",
					"Arbeitszeitartikel f\u00FCr TOPS-Ablieferung in HTML-BDE (Artikelnummer)" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL, "",
					"java.lang.String", "Werbeabgabeartikel (Artikelnummer)",
					"Artikelnummer, welche als Werbeabgabe in die Rechnung eingef\u00FCgt wird" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WERBEABGABE_PROZENT, "5",
					"java.lang.Integer", "Prozent- Wert der Werbeabgabe",
					"Prozent- Wert der Werbeabgabe" },

			{ ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ANGEBOT_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_LIEFERSCHEIN_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_ANFRAGE,
					ParameterFac.PARAMETER_ANFRAGE_ANSPRECHPARTNER_ANDRUCKEN,
					"1", "java.lang.Boolean",
					"Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_BEI_LOS_AKTUALISIERUNG_MATERIAL_NACHBUCHEN,
					"1", "java.lang.Boolean",
					"Bei Los- Aktualisierung fehlendes Material nachbuchen",
					"Bei Los- Aktualisierung aus St\u00FCckliste fehlendes Material nachbuchen" },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_FRUEHZEITIGE_BESCHAFFUNG,
					"0",
					"java.lang.Boolean",
					"0 = Beginntermin als Eingabe, 1= Checkbox f\u00FCr fr\u00FChzeitige Beschaffung",
					"0 = Beginntermin als Eingabe, 1= Checkbox f\u00FCr fr\u00FChzeitige Beschaffung" },
			{ ParameterFac.KATEGORIE_REKLAMATION,
					ParameterFac.PARAMETER_REKLAMATION_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_KUNDESONDERKONDITIONEN_WEBSHOP_VERWENDEN,
					"0",
					"java.lang.Boolean",
					"0 = Kundensoko nicht an Webshop \u00FCbermitteln 1 = \u00FCbermitteln",
					"0 = Kundensoko nicht an Webshop \u00FCbermitteln 1 = \u00FCbermitteln" },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG, "0",
					"java.lang.Boolean",
					"Empfangsbestaetigung im Bestellungsdruck",
					"Empfangsbestaetigung im Bestellungsdruck" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WERBEABGABE_VOM_EINZELPREIS, "0",
					"java.lang.Boolean",
					"Werbeabgabe vom Einzelpreis anstatt vom Nettopreis",
					"Werbeabgabe vom Einzelpreis anstatt vom Nettopreis" },
			{
					ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					ParameterFac.PARAMETER_KALKULATIONSART,
					"1",
					"java.lang.Integer",
					"1=Verkaufspreisbezug 2=Einkaufspreisbezug 3=Einkauf mit Aufschlag",
					"1=Verkaufspreisbezug 2=Einkaufspreisbezug 3=Einkauf mit Aufschlag" },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR, "0",
					"java.lang.Boolean",
					"0 = RoHs wird angezeigt, 1 = Verrechenbar anstatt RoHs",
					"0 = RoHs wird angezeigt, 1 = Verrechenbar anstatt RoHs" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUPFERZAHL,
					"1.3", "java.lang.Double", "Kupferzahl f\u00FCr VK-Belege",
					"Kupferzahl f\u00FCr VK-Belege" },
			{
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_LOSE_AKTUALISIEREN,
					"0",
					"java.lang.Integer",
					"Auswahlmoeglichkeit 0 / 1",
					"0 = nur Lose der gewaehlten Stueckliste aktualisieren, 1 = globale Aktualisierung aller Stuecklisten" },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALNUMMER_ZEICHENSATZ,
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
					"java.lang.String",
					"Bestimmt die in einer Personalnummer verwendbaren Zeichen.",
					"Bestimmt die in einer Personalnummer verwendbaren Zeichen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN,
					"0",
					"java.lang.Boolean",
					"Nicht lagerbewirtschaftete Artikel sofort ausgeben.",
					"Nicht lagerbewirtschaftete Artikel sofort ausgeben, wenn -Materialbuchung bei Ablieferung- in der Stueckliste angehakt ist. Im Lagercockpit werden bei der Materialumlagerung die nicht lagerbewirtschafteten Artikel ein/ausgeblendet." },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG, "30",
					"java.lang.Double", "Default- Aufschlag in Prozent",
					"Default- Aufschlag in Prozent, wenn KALKULATIONSART = 3" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER,
					"0", "java.lang.Boolean",
					"Artikeltextsuche inklusive Artikelnummer",
					"Artikeltextsuche inklusive Artikelnummer" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION,
					"0", "java.lang.Boolean",
					"Artikeltextsuche inklusive Index/Revision",
					"Artikeltextsuche inklusive Index/Revision" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR,
					"0", "java.lang.Boolean",
					"Lagerzubuchungsautomatik bei unzureichendem Lagerstand",
					"Lagerzubuchungsautomatik bei unzureichendem Lagerstand" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_STARTWERT_ARTIKELNUMMER, "",
					"java.lang.String",
					"Startwert, um eine Artikelnummer zu generieren",
					"Startwert, um eine Artikelnummer zu generieren" },

			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KALENDERWOCHEN,
					"0",
					"java.lang.Double",
					"Anzahl der Kalenderwochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Kalenderwochen f\u00FCr die Lohnstundensatzkalkulation." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KRANKWOCHEN,
					"0",
					"java.lang.Double",
					"Anzahl der Krankwochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Krankwochen f\u00FCr die Lohnstundensatzkalkulation." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_FEIERTAGSWOCHEN,
					"0",
					"java.lang.Double",
					"Anzahl der Feiertagswochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Feiertagswochen f\u00FCr die Lohnstundensatzkalkulation." },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_SONDERZAHLUNGEN,
					"0",
					"java.lang.Double",
					"Anzahl der Sonderzahlungen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Sonderzahlungen f\u00FCr die Lohnstundensatzkalkulation." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN,
					"1",
					"java.lang.Boolean",
					"Auftrag automatisch vollstaendig erledigen",
					"Auftrag automatisch vollstaendig erledigen. Wenn Einstellung = 0 muss koennen Auftraege nur manuell erledigt werden, wenn das Recht AUFT_DARF_AUFTRAG_ERLEDIGEN vorhanden ist." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD, "0",
					"java.lang.Boolean",
					"Das Projekt ist in den VK-Belegen ein Pflichtfeld.",
					"Das Projekt ist in den VK-Belegen ein Pflichtfeld." },
			{
					ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_KURZBEZEICHNUNG_STATT_ORT_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Kurzbezeichnung statt Ort in Projekt-Auswahl.",
					"In der Projekt-Auswahl wird anstatt des Ortes die Kurzbezeichnung des Partners angezeigt." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VERKAUFSPREIS_RUECKPFLEGE, "0",
					"java.lang.Boolean",
					"VK-Preisbasis Rueckpflege im Auftrag.",
					"VK-Preisbasis Rueckpflege im Auftrag." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELNUMMER_AUSWAHL_ABSCHNEIDEN,
					"0",
					"java.lang.Integer",
					"Vorbesetzte Artikelnummer abschneiden.",
					"Vorbesetzte Artikelnummer bei der Artikelauswahl um eine best. Anzahl von Stellen abschneiden." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_LS_DEFAULT_VERRECHENBAR, "1",
					"java.lang.Boolean",
					"Default-Wert fuer -Verrechenbar- im Lieferschein.",
					"Default-Wert fuer -Verrechenbar- im Lieferschein." },
			{ ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_LIEFERANT_ANGEBEN, "0",
					"java.lang.Boolean",
					"Lieferant in Angebots- und Auftragsposition angebbar",
					"Lieferant in Angebots- und Auftragsposition angebbar" },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG, "0",
					"java.lang.Boolean",
					"Belege koennen mit von-bis-Zeit erfasst werden",
					"Belege koennen mit von-bis-Zeit erfasst werden" },
			{ ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_INTERN_ERLEDIGT_BEBUCHBAR, "1",
					"java.lang.Boolean",
					"Zeiterfassung auf intern erledigte Projekte moeglich",
					"Zeiterfassung auf intern erledigte Projekte moeglich" },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_URLAUBSANTRAG, "0",
					"java.lang.Boolean", "Urlaubsantrag mit Genehmigung",
					"Urlaubsantrag mit Genehmigung" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ZUSATZBEZ2_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Zusatzbezeichnung2 in der Artikel-Auswahlliste anzeigen.",
					"Zusatzbezeichnung2 in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_BUILD_ANZEIGEN, "0",
					"java.lang.Boolean", "Build-Nummer in Kopfdaten anzeigen.",
					"Build-Nummer in Kopfdaten anzeigen." },
			{
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_WE,
					"2",
					"java.lang.Integer",
					"Anzahl der WE-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der WE-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },
			{
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_POSITIONSREIHENFOLGE_AUS_AUFTRAG_ERHALTEN,
					"0",
					"java.lang.Boolean",
					"Positionsreihenfolge aus Auftrag erhalten",
					"Positionsreihenfolge aus Auftrag erhalten, wenn LS-Position aus -Sicht Auftrag- erzeugt wird" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_WEP_PREIS_ZURUECKSCHREIBEN,
					"0",
					"java.lang.Integer",
					"WEP-Preis automatisch in Artikellieferant zurueckschreiben.",
					"WEP-Preis automatisch in Artikellieferant zurueckschreiben. 0 = deaktiviert, 1 = als Einzelpreis zurueckschreiben" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_DEFAULT_BESTELLVORSCHLAG_UEBERLEITUNG,
					"4",
					"java.lang.Integer",
					"Default-Vorschlag fuer die Ueberleitung des Bestellvorschlages",
					"1 = fuer jeden Lieferant + gleichen Termin eine Bestellung anlegen, 2 = je Lieferant eine Bestellung anlegen, 3 = ein Lieferant und ein Termin, 4 = ein Lieferant und alle Positionen des Lieferanten in einer Bestellung, 5 = Abruf zu vorhandenen Rahmen erzeugen" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_GENERIERE_ARTIKELNUMMER_ZIFFERNBLOCK,
					"0",
					"java.lang.Boolean",
					"Gibt an, welcher Ziffernblock beim generieren herangezogen wird",
					"0 = der Erste, 1 = der Letzte" },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_URAUBSTAGE_RUNDUNG, "0",
					"java.lang.Integer",
					"Rundung der Urlaubstage (im Report und im Uebertrag)",
					"0 = keine, 1 = kaufmaennisch, 2 = generell abrunden, 3 = generell aufrunden" },
			{ ParameterFac.KATEGORIE_PARTNER,
					ParameterFac.PARAMETER_SELEKTIONEN_MANDANTENABHAENGIG, "1",
					"java.lang.Boolean", "Selektionen sind mandantenabhaengig",
					"Selektionen sind mandantenabhaengig" },

			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN,
					"1", "java.lang.Boolean",
					"KOMMT-GEHT muss bei VON-BIS Zeiterfassung gebucht werden",
					"KOMMT-GEHT muss bei VON-BIS Zeiterfassung gebucht werden" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VERSION_BEI_SNR_MITANGEBEN, "0",
					"java.lang.Boolean",
					"Version bei SNR-Lagerbuchung mitangeben",
					"Version bei SNR-Lagerbuchung mitangeben" },

			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KOPIEN_RECHNUNG, "",
					"java.lang.Integer", "Default Kopien Rechnung",
					"Default Kopien Rechnung" },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KOPIEN_LIEFERSCHEIN, "",
					"java.lang.Integer", "Default Kopien Lieferschein",
					"Default Kopien Lieferschein" },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ZUSATZBEZEICHNUNG_IN_AUSWAHLLISTE,
					"0",
					"java.lang.Boolean",
					"Zusatzbezeichnung in Auswahlliste",
					"Zusatzbezeichnung in Auswahlliste (Wenn Parameter AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE=0)" },

			{
					ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_PROJEKT_ANSPRECHPARTNER_VORBESETZEN,
					"1",
					"java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Partners vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Partners vorbesetzen." },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH, "0",
					"java.lang.Boolean", "Seriennummern sind numerisch",
					"Seriennummern sind numerisch" },
			{
					ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_GUTSTUNDEN_ZU_UESTD50_ADDIEREN,
					"1",
					"java.lang.Boolean",
					"Gutstunden in Monatsabrechung zu Uestd50Pflichtig addieren",
					"Gutstunden in Monatsabrechung zu Uestd50Pflichtig addieren" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_INT_BEST_VERDICHTEN_RAHMENPRUEFUNG,
					"0", "java.lang.Boolean",
					"Interne Bestellung verdichten mit Rahmenpruefung",
					"Interne Bestellung verdichten mit Rahmenpruefung" },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_REFERENZNUMMER_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Referenznummer in der Artikel-Auswahlliste anzeigen.",
					"Referenznummer in der Artikel-Auswahlliste anzeigen." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAENGE_CHARGENNUMMER_EINEINDEUTIG,
					"0",
					"java.lang.Integer",
					"Eineindeutige Chargennummern werden vorgeschlagen",
					"Wenn die Laenge > 0, dann werden in der Losablieferung eineindeutige CHNrs vorgeschlagen." },
			{
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOSABLIEFERUNG_GESAMTE_ISTZEITEN_ZAEHLEN,
					"0",
					"java.lang.Boolean",
					"Fuer den Ablieferwert zaehlen nur die Zeiten bis zum Abliefzeitpunkt.",
					"Fuer den Ablieferwert zaehlen nur die Zeiten bis zum Abliefzeitpunkt." },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AB_NACHKALKULATION_NUR_RECHNUNGS_ERLOESE,
					"0", "java.lang.Boolean",
					"Fuer AB-Nachkalkulation zaehlen nur Rechnungen.",
					"Fuer AB-Nachkalkulation zaehlen nur Rechnungen." },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_NUR_VATERGRUPPEN_ANZEIGEN,
					"0",
					"java.lang.Integer",
					"In Artikel-Auswahllisten nur Vatergruppen anzeigen.",
					"In Artikel-Auswahllisten nur Vatergruppen anzeigen ( 0 - Alle Artikelgruppen anzeigen, 1 - Nur Vatergruppen anzeigen, 2 - Alle Artikelgruppen anzeigen, Kindgruppen sind eingerueckt)" },

			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_NACH_CBEZ_ODER_CNR_ANZEIGEN,
					"0",
					"java.lang.Boolean",
					"Artikel-Auswahlliste nach Kennung sortieren.",
					"Artikel-Auswahlliste nach Kennung sortieren (0 - Sortiert nach Bezeichnung, 1 - Sortiert nach Kennung)." },

			{
					ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE,
					"0", "java.lang.Boolean",
					"Kurzzeichen statt Name in Auswahlliste.",
					"Kurzzeichen statt Name in Auswahlliste." },

			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_WARENEINGANG_LAGERPLATZ_NUR_DEFINIERTE,
					"0",
					"java.lang.Boolean",
					"Beim Druck des WEP-Etiketts nur definierte Lagerplaetze auswaehlen.",
					"Beim Druck des WEP-Etiketts koennen nur mehr Lagerplaetze, welche im Artikel definiert sind, ausgewaehlt werden" },
			{ ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELSUCHE_MIT_HERSTELLER, "0",
					"java.lang.Boolean", "Textsuche inkl. Hersteller",
					"Textsuche inkl. Hersteller" },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG,
					"0", "java.lang.Boolean",
					"Automatische Pausen nur Warnung anzeigen",
					"Automatische Pausen nur Warnung anzeigen, wenn zuwenig gebucht." },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL, "0",
					"java.lang.Boolean",
					"Sammelbuchungen werden manuell durchgef\u00FChrt",
					"Sammelbuchungen werden manuell durchgef\u00FChrt" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_INVENTUR_BASISPREIS,
					"0",
					"java.lang.Integer",
					"Inventurbasispreis kommt aus Gestehungspreis/EK-Preis",
					"Inventurbasispreis des Inventurstand kommt aus Gestehungspreis/EK-Preis: Gestehungspreis = 0, EK-Preis = 1" },
			{
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER,
					"_",
					"java.lang.String",
					"Trennzeichen zwischen Artikelgruppe und Auftragsnummer",
					"Trennzeichen zwischen Artikelgruppe und Auftragsnummer. z.B. bei Auftragsschnellanlage" },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					ParameterFac.PARAMETER_EK_PREISBASIS, "1",
					"java.lang.Integer", "0=Lief1Preis, 1=Nettopreis",
					"0=Lief1Preis, 1=Nettopreis" },
			{
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_AUS_BESTVOR_POSITIONEN_MIT_LOSZUORDNUNG,
					"1", "java.lang.Boolean",
					"Bestellvorschlag erzeugt Loszuordnungen",
					"Bestellvorschlag erzeugt Loszuordnungen" },
			{
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERSTAND_DES_ANDEREN_MANDANTEN_ANZEIGEN,
					"0",
					"java.lang.Boolean",
					"Lagerstand des anderen Mandanten anzeigen (in Auswahlliste)",
					"Lagerstand des anderen Mandanten anzeigen (in Auswahlliste)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_AUTOFERTIG_ABLIEFERUNG_TERMINAL,
					"0", "java.lang.Boolean",
					"Automatische Fertigbuchung Ablieferung per Terminal",
					"Automatische Fertigbuchung bei vollst\u00E4ndiger Ablieferung per Terminal" },
			{ ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_BELEGDRUCK_MIT_RABATT, "0",
					"java.lang.Boolean",
					"Default- Einstellung der Option -Belegdrucke mit Rabatt-",
					"Default- Einstellung der Option -Belegdrucke mit Rabatt-" },
			{
					ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KUNDESOKO_WIRKT_NICHT_IN_PREISFINDUNG,
					"0",
					"java.lang.Boolean",
					"Default- Einstellung der Option -wirkt nicht in Preisfindung-",
					"Default- Einstellung der Option -wirkt nicht in Preisfindung-" },
			{
					ParameterFac.KATEGORIE_LIEFERANT,
					ParameterFac.PARAMETER_AUTOMATISCHE_KREDITORENNUMMER,
					"0",
					"java.lang.Boolean",
					"Kreditorennummer bei Belegen anlegen, wenn nicht vorhanden.",
					"Kreditorennummer bei Belegen automatisch anlegen, wenn nicht vorhanden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_EDITOR_BREITE_KOMMENTAR, "210",
					"java.lang.Integer", "Kommentarbreite",
					"Breite des Texteditors bei Kommentaren" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_EDITOR_BREITE_SONSTIGE, "520",
					"java.lang.Integer", "Standardbreite des Texteditors",
					"Standardbreite des Texteditors" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_EDITOR_BREITE_TEXTEINGABE, "470",
					"java.lang.Integer", "Standardbreite des Texteditors",
					"Breite des Texteditors bei Positionen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_EDITOR_BREITE_TEXTMODUL, "520",
					"java.lang.Integer", "Standardbreite des Texteditors",
					"Breite des Texteditors bei Textmodulen" },
			{
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_DEBITORENNUMMER_FORTLAUFEND,
					"0",
					"java.lang.Boolean",
					"Debitorennummer fortlaufend",
					"0 = Anfangsbuchstabe des Partners wird f\u00fcr 2+3. Stelle verwendet 1 = fortlaufend gilt fuer Debitoren und Kreditoren" },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					ParameterFac.PARAMETER_IMAPSERVER, "", "java.lang.String",
					"IMAP Server", "IMAP Server f\u00fcr Versandablage" },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					ParameterFac.PARAMETER_IMAPSERVER_ADMIN, "",
					"java.lang.String", "IMAP Server Admin Konto",
					"IMAP Server Admin Konto f\u00FCr Versandablage." },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT, "",
					"java.lang.String", "IMAP Server Admin Konto Kennwort",
					"IMAP Server Kennwort des Admin Kontos f\u00FCr Versandablage." },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					ParameterFac.PARAMETER_IMAPSERVER_SENTFOLDER, "",
					"java.lang.String", "IMAP Server Sent-Folder",
					"IMAP Server Ordner f\u00FCr gesendete Objekte." },
			{ ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT, "0",
					"java.lang.Boolean",
					"Lieferantenrechnungsnummer als Buchungstext bei ERs",
					"0 = Name des Lieferanteranten, 1 = Lieferantrechnungsnummer als Buchungstext" },
			{
					ParameterFac.KATEGORIE_GUTSCHRIFT,
					ParameterFac.PARAMETER_GUTSCHRIFT_NENNT_SICH_RECHNUNGSKORREKTUR,
					"0", "java.lang.Boolean",
					"Gutschrift nennt sich Rechnungskorrektur",
					"Gutschrift nennt sich Rechnungskorrektur" },
			{
					ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_MINDEST_MAHNBETRAG,
					"5",
					"java.lang.Double",
					"Mindestmahnbetrag, ab der eine Sammelmahnung versendet wird.",
					"Mindestmahnbetrag, ab der eine Sammelmahnung versendet wird." },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_BEWERTUNG_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean",
					"Fortschritt in Auswahlliste anzeigen.",
					"Fortschritt in Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAEGE_KOENNEN_VERSTECKT_WERDEN,
					"0", "java.lang.Boolean",
					"Auftraege koennen versteckt werden.",
					"Auftraege koennen versteckt werden." },

	// parametermandant: 1 fix verdrahteten Wert einfuegen, Zeilensetzung wie
	// oben, damit sie bei Formatierung nicht verloren geht...
	};

	// parametermandant: 2 UpdateXX des DBManagers entsprechend erweitern
	private ParametermandantDto[] aFixverdrahteteParametermandantDtos = null;

	/**
	 * Berechnet das aktuelle Geschaeftsjahr (4-stellig) anhand der Einstellung
	 * in den Firmenparametern.
	 * 
	 * @param mandantCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer getGeschaeftsjahr(String mandantCNr) throws EJBExceptionLP {
		return getGeschaeftsjahr(mandantCNr, super.getDate());
	}

	/**
	 * Berechnet das Geschaeftsjahr (4-stellig) fuer ein beliebiges Datum anhand
	 * der Einstellung in den Firmenparametern
	 * 
	 * @return Integer z.B. 2004
	 * @param mandantCNr
	 *            String
	 * @param dBelegdatum
	 *            Date
	 * @throws EJBExceptionLP
	 */
	public Integer getGeschaeftsjahr(String mandantCNr,
			java.util.Date dBelegdatum) throws EJBExceptionLP {

		int beginnMonat;
		boolean plusEins;

		try {
			// -1 wegen Jaenner in DB == 1, in Calendar == 0
			beginnMonat = getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT)
					.asInteger() - 1;
			plusEins = getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS).asBoolean();
			// pmBeginnMonat = getMandantparameter(mandantCNr,
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
			// pmPlusEins = getMandantparameter(mandantCNr,
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS);
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, ex);
		}

		// int beginnMonat = Integer.parseInt(pmBeginnMonat.getCWert());
		// boolean plusEins = Helper
		// .short2boolean(Short.parseShort(pmPlusEins.getCWert()));

		// beginnMonat = beginnMonat - 1; // wegen Jaenner = 0, Feb. = 1 etc.

		Calendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dBelegdatum.getTime());

		return calculateGeschaeftsjahr(gc, beginnMonat, plusEins);
		// int iYear = gc.get(Calendar.YEAR);
		// int iMonth = gc.get(Calendar.MONTH);
		// if (plusEins) {
		// if (iMonth < beginnMonat) {
		// // z.B. Oktober gehoert noch zu diesem Jahr
		// return new Integer(iYear);
		// } else {
		// // z.B. November gehoert schon zum naechsten Jahr
		// return new Integer(iYear + 1);
		// }
		// } else {
		// if (iMonth < beginnMonat) {
		// // z.B. Jaenner gehoert noch zum letzten Jahr
		// return new Integer(iYear - 1);
		// } else {
		// // z.B. Februar gehoert aber zu diesem Jahr
		// return new Integer(iYear);
		// }
		// }
	}

	/**
	 * Berechne das Geschaeftsjahr aus einem Belegdatum
	 * 
	 * @param belegDatum
	 *            ist das Belegdatum
	 * @param beginnMonat
	 *            ist der Monat (0 basierend!) in dem das neue GJ beginnt
	 * @param plusEins
	 *            ist true wenn es sich um ein Folgejahr handelt
	 * @return das Geschaeftsjahr
	 */
	public Integer calculateGeschaeftsjahr(Calendar belegDatum,
			int beginnMonat, boolean plusEins) {
		int iYear = belegDatum.get(Calendar.YEAR);
		int iMonth = belegDatum.get(Calendar.MONTH);
		if (plusEins) { // Wir befinden uns in den Folgejahren eins
						// Rumpf-Geschaeftsjahres
			if (iMonth < beginnMonat) {
				// z.B. Oktober gehoert noch zu diesem Jahr
				return new Integer(iYear);
			} else {
				// z.B. November gehoert schon zum naechsten Jahr
				return new Integer(iYear + 1);
			}
		}

		if (iMonth < beginnMonat) {
			// z.B. Jaenner gehoert noch zum letzten Jahr
			return new Integer(iYear - 1);
		} else {
			// z.B. Februar gehoert aber zu diesem Jahr
			return new Integer(iYear);
		}
	}

	// Parametermandant
	// ----------------------------------------------------------
	public ParametermandantPK createParametermandant(
			ParametermandantDto parametermandantDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkParametermandantDto(parametermandantDtoI);

		ParametermandantPK pkParametermandant = null;

		try {
			Parametermandant parametermandant = new Parametermandant(
					parametermandantDtoI.getCNr(),
					parametermandantDtoI.getMandantCMandant(),
					parametermandantDtoI.getCKategorie(),
					parametermandantDtoI.getCWert(),
					theClientDto.getIDPersonal(),
					parametermandantDtoI.getCBemerkungsmall(),
					parametermandantDtoI.getCDatentyp());
			em.persist(parametermandant);
			em.flush();

			// die generierten Daten setzen
			parametermandantDtoI.setTAendern(parametermandant.getTAendern());
			parametermandantDtoI.setPersonalIIdAendern(theClientDto
					.getIDPersonal());

			setParametermandantFromParametermandantDto(parametermandant,
					parametermandantDtoI);

			pkParametermandant = new ParametermandantPK();
			pkParametermandant.setCNr(parametermandantDtoI.getCNr());
			pkParametermandant.setMandantCNr(parametermandantDtoI
					.getMandantCMandant());
			pkParametermandant.setCKategorie(parametermandantDtoI
					.getCKategorie());

			getBenutzerServicesFac().reloadParametermandant();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return pkParametermandant;
	}

	public void removeParametermandant(ParametermandantDto parametermandantDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkParametermandantDto(parametermandantDto);

		ParametermandantPK pkParametermandant = new ParametermandantPK();
		pkParametermandant.setCNr(parametermandantDto.getCNr());
		pkParametermandant.setMandantCNr(parametermandantDto
				.getMandantCMandant());
		pkParametermandant.setCKategorie(parametermandantDto.getCKategorie());

		Parametermandant toRemove = em.find(Parametermandant.class,
				pkParametermandant);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeParametermandantgueltigab(
			ParametermandantgueltigabDto parametermandantgueltigabDto) {

		ParametermandantgueltigabPK pkParametermandantgueltigab = new ParametermandantgueltigabPK();
		pkParametermandantgueltigab.setCNr(parametermandantgueltigabDto
				.getCNr());
		pkParametermandantgueltigab.setMandantCNr(parametermandantgueltigabDto
				.getMandantCNr());
		pkParametermandantgueltigab.setCKategorie(parametermandantgueltigabDto
				.getCKategorie());
		pkParametermandantgueltigab.setTGueltigab(parametermandantgueltigabDto
				.getTGueltigab());

		Parametermandantgueltigab toRemove = em.find(
				Parametermandantgueltigab.class, pkParametermandantgueltigab);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		getBenutzerServicesFac().reloadParametermandant();

	}

	public void updateParametermandant(
			ParametermandantDto parametermandantDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkParametermandantDto(parametermandantDtoI);

		vergleicheParametermandantDtoVorherNachherUndLoggeAenderungen(
				parametermandantDtoI, theClientDto);

		ParametermandantPK pkParametermandant = new ParametermandantPK();
		pkParametermandant.setCNr(parametermandantDtoI.getCNr());
		pkParametermandant.setMandantCNr(parametermandantDtoI
				.getMandantCMandant());
		pkParametermandant.setCKategorie(parametermandantDtoI.getCKategorie());

		Parametermandant parametermandant = em.find(Parametermandant.class,
				pkParametermandant);
		if (parametermandant == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		parametermandantDtoI.setTAendern(getTimestamp());
		parametermandantDtoI
				.setPersonalIIdAendern(theClientDto.getIDPersonal());

		// UW->WH Wie soll der Eintrag im kritischen Log genau aussehen?
		myLogger.logKritisch("Update " + parametermandantDtoI.toString());

		setParametermandantFromParametermandantDto(parametermandant,
				parametermandantDtoI);
		getBenutzerServicesFac().reloadParametermandant();
	}

	public void updateParametermandantgueltigab(
			ParametermandantgueltigabDto parametermandantgueltigabDtoI,
			TheClientDto theClientDto) {

		parametermandantgueltigabDtoI.setTGueltigab(Helper
				.cutTimestamp(parametermandantgueltigabDtoI.getTGueltigab()));
		ParametermandantgueltigabPK pkParametermandant = new ParametermandantgueltigabPK();
		pkParametermandant.setCNr(parametermandantgueltigabDtoI.getCNr());
		pkParametermandant.setMandantCNr(parametermandantgueltigabDtoI
				.getMandantCNr());
		pkParametermandant.setCKategorie(parametermandantgueltigabDtoI
				.getCKategorie());
		pkParametermandant.setTGueltigab(parametermandantgueltigabDtoI
				.getTGueltigab());

		Parametermandantgueltigab parametermandantgueltigab = em.find(
				Parametermandantgueltigab.class, pkParametermandant);
		if (parametermandantgueltigab == null) {
			// Neu anlegen

			parametermandantgueltigab = new Parametermandantgueltigab(
					parametermandantgueltigabDtoI.getCNr(),
					parametermandantgueltigabDtoI.getMandantCNr(),
					parametermandantgueltigabDtoI.getCKategorie(),
					parametermandantgueltigabDtoI.getCWert(),
					parametermandantgueltigabDtoI.getTGueltigab());

		}

		// und wert setzen
		parametermandantgueltigab.setCWert(parametermandantgueltigabDtoI
				.getCWert());
		em.merge(parametermandantgueltigab);
		em.flush();

		myLogger.logKritisch("Update "
				+ parametermandantgueltigabDtoI.toString());

		getBenutzerServicesFac().reloadParametermandant();
	}

	public ParametermandantDto parametermandantFindByPrimaryKey(
			ParametermandantPK pkParametermandantI) throws EJBExceptionLP {
		if (pkParametermandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pkParametermandantI == null"));
		}

		// try {
		Parametermandant parametermandant = em.find(Parametermandant.class,
				pkParametermandantI);
		if (parametermandant == null) {
			throw new EJBExceptionLPwoRollback(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleParametermandantDto(parametermandant);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public ParametermandantgueltigabDto parametermandantgueltigabFindByPrimaryKey(
			ParametermandantgueltigabPK pkParametermandantI) {
		if (pkParametermandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pkParametermandantI == null"));
		}
		Parametermandantgueltigab parametermandant = em.find(
				Parametermandantgueltigab.class, pkParametermandantI);
		if (parametermandant == null) {
			throw new EJBExceptionLPwoRollback(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return ParametermandantgueltigabDtoAssembler
				.createDto(parametermandant);

	}

	public TreeMap<Timestamp, String> parametermandantgueltigabGetWerteZumZeitpunkt(
			String mandantCNr, String cNr, String cKategorie) {

		TreeMap<Timestamp, String> tm = null;

		Query query = em
				.createNamedQuery("ParametermandantgueltigabFindByMandantCNrCNrCKategorie");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, cNr);
		query.setParameter(3, cKategorie);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			tm = new TreeMap<Timestamp, String>();

			Iterator it = cl.iterator();

			while (it.hasNext()) {
				Parametermandantgueltigab pmg = (Parametermandantgueltigab) it
						.next();
				tm.put(pmg.getPk().getTGueltigab(), pmg.getCWert());
			}

		}
		return tm;

	}

	public ParametermandantDto parametermandantFindByPrimaryKey(String cNrI,
			String cNrKategorieI, String cNrMandantI) throws EJBExceptionLP {
		// try {
		ParametermandantPK parametermandantPK = new ParametermandantPK();
		parametermandantPK.setCNr(cNrI);
		parametermandantPK.setMandantCNr(cNrMandantI);
		parametermandantPK.setCKategorie(cNrKategorieI);

		myLogger.logData(parametermandantPK); // UW: das bleibt
		Parametermandant parametermandant = em.find(Parametermandant.class,
				parametermandantPK);
		if (parametermandant == null) {
			throw new EJBExceptionLPwoRollback(
					EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, "");
		}
		return assembleParametermandantDto(parametermandant);
		// }
		// catch (FinderException e) {
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
		// e);
		// }
	}

	public ParameteranwenderDto getAnwenderparameter(String cKategorieI,
			String anwenderparameter_c_nr) {
		ParameteranwenderDto parameteranwenderDto = null;
		ParameteranwenderPK parameteranwenderPK = new ParameteranwenderPK(
				anwenderparameter_c_nr, cKategorieI);
		try {
			parameteranwenderDto = parameteranwenderFindByPrimaryKey(parameteranwenderPK);
		} catch (Throwable th) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception("Anwenderparameter " + cKategorieI + "."
							+ anwenderparameter_c_nr
							+ " kann nicht gefunden werden !"));
		}
		return parameteranwenderDto;
	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr,
			String cKategorieI, String mandantparameter_c_nr) {
		return getBenutzerServicesFac().getMandantparameter(mandant_c_nr,
				cKategorieI, mandantparameter_c_nr);
	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr,
			String cKategorieI, String mandantparameter_c_nr,
			java.sql.Timestamp tZeitpunkt) {
		return getBenutzerServicesFac().getMandantparameter(mandant_c_nr,
				cKategorieI, mandantparameter_c_nr, tZeitpunkt);
	}

	public void createFixverdrahteteParametermandant(TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "createFixverdrahteteParametermandant";
		myLogger.entry();

		if (aFixverdrahteteParametermandantDtos == null) {
			aFixverdrahteteParametermandantDtos = new ParametermandantDto[progMandantParameter.length];

			for (int i = 0; i < progMandantParameter.length; i++) {
				ParametermandantDto parametermandantDto = new ParametermandantDto();
				parametermandantDto.setCNr(progMandantParameter[i][1]);
				parametermandantDto.setCKategorie(progMandantParameter[i][0]);
				parametermandantDto.setMandantCMandant(theClientDto
						.getMandant());
				parametermandantDto.setCWert(progMandantParameter[i][2]);
				parametermandantDto.setCDatentyp(progMandantParameter[i][3]);
				parametermandantDto
						.setCBemerkungsmall(progMandantParameter[i][4]);
				parametermandantDto
						.setCBemerkunglarge(progMandantParameter[i][5]);
				parametermandantDto.setPersonalIIdAendern(theClientDto
						.getIDPersonal());
				parametermandantDto.setTAendern(getTimestamp());

				aFixverdrahteteParametermandantDtos[i] = parametermandantDto;
			}
		}

		for (int i = 0; i < aFixverdrahteteParametermandantDtos.length; i++) {
			createParametermandant(aFixverdrahteteParametermandantDtos[i],
					theClientDto);
		}
	}

	private void checkParametermandantDto(
			ParametermandantDto oParametermandantDtoI) throws EJBExceptionLP {
		if (oParametermandantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oParametermandantDtoI == null"));
		}
		if (oParametermandantDtoI.getCDatentyp().equals("java.lang.Double")
				|| oParametermandantDtoI.getCDatentyp().equals(
						"java.math.BigDecimal")) {
			oParametermandantDtoI.setCWert(oParametermandantDtoI.getCWert()
					.replaceAll(",", "."));
		}
	}

	private void setParametermandantFromParametermandantDto(
			Parametermandant parametermandant,
			ParametermandantDto parametermandantDto) {

		parametermandant.setCWert(parametermandantDto.getCWert());
		parametermandant.setTAendern(parametermandantDto.getTAendern());
		parametermandant.setPersonalIIdAendern(parametermandantDto
				.getPersonalIIdAendern());
		parametermandant.setCBemerkungsmall(parametermandantDto
				.getCBemerkungsmall());
		parametermandant.setCBemerkunglarge(parametermandantDto
				.getCBemerkunglarge());
		em.merge(parametermandant);
		em.flush();
	}

	private ParametermandantDto assembleParametermandantDto(
			Parametermandant parametermandant) {
		return ParametermandantDtoAssembler.createDto(parametermandant);
	}

	private ParametermandantDto[] assembleParametermandantDtos(
			Collection<?> parametermandants) {
		List<ParametermandantDto> list = new ArrayList<ParametermandantDto>();
		if (parametermandants != null) {
			Iterator<?> iterator = parametermandants.iterator();
			while (iterator.hasNext()) {
				Parametermandant parametermandant = (Parametermandant) iterator
						.next();
				list.add(assembleParametermandantDto(parametermandant));
			}
		}
		ParametermandantDto[] returnArray = new ParametermandantDto[list.size()];
		return (ParametermandantDto[]) list.toArray(returnArray);
	}

	// Parameteranwender
	// ---------------------------------------------------------

	public ParameteranwenderPK createParameteranwender(
			ParameteranwenderDto parameteranwenderDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDtoI);

		ParameteranwenderPK pkParameteranwender = null;

		try {
			Parameteranwender parameteranwender = new Parameteranwender(
					parameteranwenderDtoI.getCNr(),
					parameteranwenderDtoI.getCWert(),
					parameteranwenderDtoI.getCKategorie(),
					theClientDto.getIDPersonal(),
					parameteranwenderDtoI.getCDatentyp());
			em.persist(parameteranwender);
			em.flush();

			// die generierten Daten setzen
			parameteranwenderDtoI.setTAendern(parameteranwender.getTAendern());
			parameteranwenderDtoI.setPersonalIIdAendern(theClientDto
					.getIDPersonal());

			setParameteranwenderFromParameteranwenderDto(parameteranwender,
					parameteranwenderDtoI);

			pkParameteranwender = new ParameteranwenderPK();
			pkParameteranwender.setCNr(parameteranwenderDtoI.getCNr());
			pkParameteranwender.setCKategorie(parameteranwenderDtoI
					.getCKategorie());
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return pkParameteranwender;
	}

	public void removeParameteranwender(
			ParameteranwenderDto parameteranwenderDto) throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDto);

		ParameteranwenderPK pkParameteranwender = new ParameteranwenderPK();
		pkParameteranwender.setCNr(parameteranwenderDto.getCNr());
		pkParameteranwender.setCKategorie(parameteranwenderDto.getCKategorie());

		Parameteranwender toRemove = em.find(Parameteranwender.class,
				pkParameteranwender);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateParameteranwender(
			ParameteranwenderDto parameteranwenderDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDtoI);

		// try {
		ParameteranwenderPK pkParameteranwender = new ParameteranwenderPK();
		pkParameteranwender.setCNr(parameteranwenderDtoI.getCNr());
		pkParameteranwender
				.setCKategorie(parameteranwenderDtoI.getCKategorie());

		Parameteranwender parameteranwender = em.find(Parameteranwender.class,
				pkParameteranwender);
		if (parameteranwender == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		parameteranwenderDtoI.setTAendern(getTimestamp());
		parameteranwenderDtoI.setPersonalIIdAendern(theClientDto
				.getIDPersonal());

		setParameteranwenderFromParameteranwenderDto(parameteranwender,
				parameteranwenderDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ParameteranwenderDto parameteranwenderFindByPrimaryKey(
			ParameteranwenderPK pkParameteranwenderI) throws EJBExceptionLP {
		if (pkParameteranwenderI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pkParameteranwenderI == null"));
		}

		// try {
		Parameteranwender parameteranwender = em.find(Parameteranwender.class,
				pkParameteranwenderI);
		if (parameteranwender == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleParameteranwenderDto(parameteranwender);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void checkParameteranwenderDto(
			ParameteranwenderDto oParameteranwenderDtoI) throws EJBExceptionLP {
		if (oParameteranwenderDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oParameteranwenderDtoI == null"));
		}
	}

	private void setParameteranwenderFromParameteranwenderDto(
			Parameteranwender parameteranwender,
			ParameteranwenderDto parameteranwenderDto) {
		parameteranwender.setCWert(parameteranwenderDto.getCWert());
		parameteranwender.setCBemerkungsmall(parameteranwenderDto
				.getCBemerkungsmall());
		parameteranwender.setCBemerkunglarge(parameteranwenderDto
				.getCBemerkunglarge());
		em.merge(parameteranwender);
		em.flush();
	}

	private ParameteranwenderDto assembleParameteranwenderDto(
			Parameteranwender parameteranwender) {
		return ParameteranwenderDtoAssembler.createDto(parameteranwender);
	}

	private ParameteranwenderDto[] assembleParameteranwenderDtos(
			Collection<?> parameteranwenders) {
		List<ParameteranwenderDto> list = new ArrayList<ParameteranwenderDto>();
		if (parameteranwenders != null) {
			Iterator<?> iterator = parameteranwenders.iterator();
			while (iterator.hasNext()) {
				Parameteranwender parameteranwender = (Parameteranwender) iterator
						.next();
				list.add(assembleParameteranwenderDto(parameteranwender));
			}
		}
		ParameteranwenderDto[] returnArray = new ParameteranwenderDto[list
				.size()];
		return (ParameteranwenderDto[]) list.toArray(returnArray);
	}

	public Integer createArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getCPcname() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("arbeitsplatzDto.getCPcname() == null"));
		}

		try {
			Query query = em.createNamedQuery("ArbeitsplatzfindByCPcname");
			query.setParameter(1, arbeitsplatzDto.getCPcname());
			Arbeitsplatz arbeitsplatz = (Arbeitsplatz) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_ARBEITSPLATZ.C_PCNAME"));
		} catch (NoResultException ex) {
			//
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARBEITSPLATZ);
			arbeitsplatzDto.setIId(pk);

			Arbeitsplatz arbeitsplatz = new Arbeitsplatz(
					arbeitsplatzDto.getIId(), arbeitsplatzDto.getCPcname());
			em.persist(arbeitsplatz);
			em.flush();
			setArbeitsplatzFromArbeitsplatzDto(arbeitsplatz, arbeitsplatzDto);

			return arbeitsplatzDto.getIId();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("arbeitsplatzDto.getIId() == null"));
		}

		Integer iId = arbeitsplatzDto.getIId();
		Arbeitsplatz toRemove = em.find(Arbeitsplatz.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getIId() == null
				&& arbeitsplatzDto.getCPcname() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"arbeitsplatzDto.getIId() == null && arbeitsplatzDto.getCPcname() == null"));
		}

		Integer iId = arbeitsplatzDto.getIId();
		Arbeitsplatz arbeitsplatz = em.find(Arbeitsplatz.class, iId);
		if (arbeitsplatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setArbeitsplatzFromArbeitsplatzDto(arbeitsplatz, arbeitsplatzDto);

		getBenutzerServicesFac().reloadArbeitsplatzparameter();
	}

	public ArbeitsplatzDto arbeitsplatzFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Arbeitsplatz arbeitsplatz = em.find(Arbeitsplatz.class, iId);
		if (arbeitsplatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArbeitsplatzDto(arbeitsplatz);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArbeitsplatzDto arbeitsplatzFindByCPcname(String cPcname)
			throws EJBExceptionLP {

		if (cPcname == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cPcname == null"));
		}

		try {
			Query query = em.createNamedQuery("ArbeitsplatzfindByCPcname");
			query.setParameter(1, cPcname);
			Arbeitsplatz arbeitsplatz = (Arbeitsplatz) query.getSingleResult();
			return assembleArbeitsplatzDto(arbeitsplatz);

		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	public ArbeitsplatzDto arbeitsplatzFindByCTypCGeraetecode(String cTyp,
			String cGeraetecode) {

		if (cGeraetecode == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cGeraetecode == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ArbeitsplatzFindByCTypCGeraetecode");
			query.setParameter(1, cTyp);
			query.setParameter(2, cGeraetecode);
			Collection arbeitsplatzs = query.getResultList();
			ArbeitsplatzDto[] dtos = assembleArbeitsplatzDtos(arbeitsplatzs);

			if (dtos != null && dtos.length > 0) {
				return dtos[0];
			} else {
				return null;
			}

		} catch (NoResultException e) {
			return null;
		}
	}

	private void setArbeitsplatzFromArbeitsplatzDto(Arbeitsplatz arbeitsplatz,
			ArbeitsplatzDto arbeitsplatzDto) {
		arbeitsplatz.setCStandort(arbeitsplatzDto.getCStandort());
		arbeitsplatz.setCBemerkung(arbeitsplatzDto.getCBemerkung());
		arbeitsplatz.setCPcname(arbeitsplatzDto.getCPcname());
		em.merge(arbeitsplatz);
		em.flush();
	}

	private ArbeitsplatzDto assembleArbeitsplatzDto(Arbeitsplatz arbeitsplatz) {
		return ArbeitsplatzDtoAssembler.createDto(arbeitsplatz);
	}

	private ArbeitsplatzDto[] assembleArbeitsplatzDtos(
			Collection<?> arbeitsplatzs) {
		List<ArbeitsplatzDto> list = new ArrayList<ArbeitsplatzDto>();
		if (arbeitsplatzs != null) {
			Iterator<?> iterator = arbeitsplatzs.iterator();
			while (iterator.hasNext()) {
				Arbeitsplatz arbeitsplatz = (Arbeitsplatz) iterator.next();
				list.add(assembleArbeitsplatzDto(arbeitsplatz));
			}
		}
		ArbeitsplatzDto[] returnArray = new ArbeitsplatzDto[list.size()];
		return (ArbeitsplatzDto[]) list.toArray(returnArray);
	}

	public void createParameter(ParameterDto parameterDto)
			throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null
				&& parameterDto.getCDatentyp() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null"));
		}

		// try {
		Parameter parameter = em.find(Parameter.class, parameterDto.getCNr());
		if (parameter != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_PARAMETER.C_NRE"));
		}
		// catch (FinderException ex) {
		//
		// }

		try {
			parameter = new Parameter(parameterDto.getCNr(),
					parameterDto.getCDatentyp());
			em.persist(parameter);
			em.flush();
			setParameterFromParameterDto(parameter, parameterDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeParameter(ParameterDto parameterDto)
			throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("parameterDto.getCNr() == null"));
		}

		String cNr = parameterDto.getCNr();
		Parameter toRemove = em.find(Parameter.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateParameter(ParameterDto parameterDto)
			throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null
				&& parameterDto.getCDatentyp() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null"));
		}

		String cNr = parameterDto.getCNr();
		// try {
		Parameter parameter = em.find(Parameter.class, cNr);
		if (parameter == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setParameterFromParameterDto(parameter, parameterDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ParameterDto parameterFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		// try {
		Parameter parameter = em.find(Parameter.class, cNr);
		if (parameter == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleParameterDto(parameter);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setParameterFromParameterDto(Parameter parameter,
			ParameterDto parameterDto) {
		parameter.setCBemerkung(parameterDto.getCBemerkung());
		parameter.setCDatentyp(parameterDto.getCDatentyp());
		em.merge(parameter);
		em.flush();
	}

	private ParameterDto assembleParameterDto(Parameter parameter) {
		return ParameterDtoAssembler.createDto(parameter);
	}

	private ParameterDto[] assembleParameterDtos(Collection<?> parameters) {
		List<ParameterDto> list = new ArrayList<ParameterDto>();
		if (parameters != null) {
			Iterator<?> iterator = parameters.iterator();
			while (iterator.hasNext()) {
				Parameter parameter = (Parameter) iterator.next();
				list.add(assembleParameterDto(parameter));
			}
		}
		ParameterDto[] returnArray = new ParameterDto[list.size()];
		return (ParameterDto[]) list.toArray(returnArray);
	}

	public Integer createArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws EJBExceptionLP {

		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getArbeitsplatzIId() == null
				&& arbeitsplatzparameterDto.getParameterCNr() == null
				&& arbeitsplatzparameterDto.getCWert() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"arbeitsplatzparameterDto.getArbeitsplatzIId() == null && arbeitsplatzparameterDto.getParameterCNr() == null && arbeitsplatzparameterDto.getCWert() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArbeitsplatzparameterfindByArbeitsplatzIIdParameterCNr");
			query.setParameter(1, arbeitsplatzparameterDto.getArbeitsplatzIId());
			query.setParameter(2, arbeitsplatzparameterDto.getParameterCNr());
			Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_ARBEITSPLATZPARAMETER.UK"));
		} catch (NoResultException ex) {
			//
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_ARBEITSPLATZPARAMETER);
			arbeitsplatzparameterDto.setIId(pk);

			Arbeitsplatzparameter arbeitsplatzparameter = new Arbeitsplatzparameter(
					arbeitsplatzparameterDto.getIId(),
					arbeitsplatzparameterDto.getArbeitsplatzIId(),
					arbeitsplatzparameterDto.getParameterCNr());
			em.persist(arbeitsplatzparameter);
			em.flush();
			setArbeitsplatzparameterFromArbeitsplatzparameterDto(
					arbeitsplatzparameter, arbeitsplatzparameterDto);

			getBenutzerServicesFac().reloadArbeitsplatzparameter();

			return arbeitsplatzparameterDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws EJBExceptionLP {
		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("arbeitsplatzparameterDto.getIId() == null"));
		}

		Integer iId = arbeitsplatzparameterDto.getIId();
		Arbeitsplatzparameter toRemove = em.find(Arbeitsplatzparameter.class,
				iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		getBenutzerServicesFac().reloadArbeitsplatzparameter();

	}

	public void updateArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws EJBExceptionLP {
		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getIId() == null
				&& arbeitsplatzparameterDto.getArbeitsplatzIId() == null
				&& arbeitsplatzparameterDto.getParameterCNr() == null
				&& arbeitsplatzparameterDto.getCWert() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"arbeitsplatzparameterDto.getIId() == null && arbeitsplatzparameterDto.getArbeitsplatzIId() == null && arbeitsplatzparameterDto.getParameterCNr() == null && arbeitsplatzparameterDto.getCWert() == null"));
		}

		Integer iId = arbeitsplatzparameterDto.getIId();
		// try {
		Arbeitsplatzparameter arbeitsplatzparameter = em.find(
				Arbeitsplatzparameter.class, iId);
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setArbeitsplatzparameterFromArbeitsplatzparameterDto(
				arbeitsplatzparameter, arbeitsplatzparameterDto);
		getBenutzerServicesFac().reloadArbeitsplatzparameter();

	}

	public ArbeitsplatzparameterDto arbeitsplatzparameterFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		// try {
		Arbeitsplatzparameter arbeitsplatzparameter = em.find(
				Arbeitsplatzparameter.class, iId);
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArbeitsplatzparameterDto(arbeitsplatzparameter);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(String cPcname,
			String parameterCNr) {
		return getBenutzerServicesFac().holeArbeitsplatzparameter(cPcname,
				parameterCNr);
	}

	public ArbeitsplatzparameterDto arbeitsplatzparameterFindByArbeitsplatzIIdParameterCNr(
			Integer arbeitsplatzIId, String parameterCNr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("ArbeitsplatzparameterfindByArbeitsplatzIIdParameterCNr");
		query.setParameter(1, arbeitsplatzIId);
		query.setParameter(2, parameterCNr);
		Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) query
				.getSingleResult();
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleArbeitsplatzparameterDto(arbeitsplatzparameter);

	}

	private void setArbeitsplatzparameterFromArbeitsplatzparameterDto(
			Arbeitsplatzparameter arbeitsplatzparameter,
			ArbeitsplatzparameterDto arbeitsplatzparameterDto) {
		arbeitsplatzparameter.setArbeitsplatzIId(arbeitsplatzparameterDto
				.getArbeitsplatzIId());
		arbeitsplatzparameter.setParameterCNr(arbeitsplatzparameterDto
				.getParameterCNr());
		arbeitsplatzparameter.setCWert(arbeitsplatzparameterDto.getCWert());
		em.merge(arbeitsplatzparameter);
		em.flush();
	}

	private ArbeitsplatzparameterDto assembleArbeitsplatzparameterDto(
			Arbeitsplatzparameter arbeitsplatzparameter) {
		return ArbeitsplatzparameterDtoAssembler
				.createDto(arbeitsplatzparameter);
	}

	private ArbeitsplatzparameterDto[] assembleArbeitsplatzparameterDtos(
			Collection<?> arbeitsplatzparameters) {
		List<ArbeitsplatzparameterDto> list = new ArrayList<ArbeitsplatzparameterDto>();
		if (arbeitsplatzparameters != null) {
			Iterator<?> iterator = arbeitsplatzparameters.iterator();
			while (iterator.hasNext()) {
				Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) iterator
						.next();
				list.add(assembleArbeitsplatzparameterDto(arbeitsplatzparameter));
			}
		}
		ArbeitsplatzparameterDto[] returnArray = new ArbeitsplatzparameterDto[list
				.size()];
		return (ArbeitsplatzparameterDto[]) list.toArray(returnArray);
	}

	private void vergleicheParametermandantDtoVorherNachherUndLoggeAenderungen(
			ParametermandantDto parametermandantDto_Aktuell,
			TheClientDto theClientDto) {

		// Die BenutzerservicesFacBean hat eine eigene Logik um "fehlende"
		// Parameter zu laden. TestcaseAutoKommmt
		ParametermandantDto parametermandantDto_Vorher = getMandantparameter(
				parametermandantDto_Aktuell.getMandantCMandant(),
				parametermandantDto_Aktuell.getCKategorie(),
				parametermandantDto_Aktuell.getCNr());

		// ParametermandantDto parametermandantDto_Vorher =
		// parametermandantFindByPrimaryKey(
		// parametermandantDto_Aktuell.getCNr(),
		// parametermandantDto_Aktuell.getCKategorie(),
		// theClientDto.getMandant());

		HvDtoLogger<ParametermandantDto> artikelLogger = new HvDtoLogger<ParametermandantDto>(
				em, theClientDto);
		artikelLogger.log(parametermandantDto_Vorher,
				parametermandantDto_Aktuell);
	}
}
