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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.WebabfrageArtikellieferantSuchparameterDto;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.system.assembler.ArbeitsplatzkonfigurationDtoAssembler;
import com.lp.server.system.ejb.Arbeitsplatz;
import com.lp.server.system.ejb.Arbeitsplatzkonfiguration;
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
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ArbeitsplatzparameterDtoAssembler;
import com.lp.server.system.service.HttpProxyConfig;
import com.lp.server.system.service.HvBelegnummernformat;
import com.lp.server.system.service.HvBelegnummernformatHistorisch;
import com.lp.server.system.service.MailServiceParameterSource;
import com.lp.server.system.service.ParameterDto;
import com.lp.server.system.service.ParameterDtoAssembler;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.server.system.service.ParameteranwenderDtoAssembler;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ParametermandantDtoAssembler;
import com.lp.server.system.service.ParametermandantgueltigabDto;
import com.lp.server.system.service.ParametermandantgueltigabDtoAssembler;
import com.lp.server.system.service.PostPlcApiKeyDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBExceptionLPwoRollback;
import com.lp.util.Helper;

@Stateless
// @RemoteBinding(jndiBinding="ParameterFac")
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

			{ ParameterFac.ARBEITSPLATZPARAMETER_MELDUNG_MEHRSPRACHIGKEIT_ANGEZEIGT, "java.lang.Boolean", "0",
					"Die Meldung \"Mehrsprachigkeit steht auf Ihrem Helium V nicht zur Verf\u00FCgung\" wurde bereits einmal angezeigt." },

			{ ParameterFac.ARBEITSPLATZPARAMETER_TAPI_LINE, "java.lang.Integer", "16", "Line-ID des TAPI-Services." },
			{ ParameterFac.ARBEITSPLATZPARAMETER_PORT_FINGERPRINTLESER, "java.lang.String", "COM3",
					"Port, an den der Fingerprintleser angeschlossen ist." },
			{ ParameterFac.ARBEITSPLATZPARAMETER_FERTIGUNG_ANSICHT_OFFENE_LOSE, "java.lang.Integer", "0",
					"In der Losauswahlliste werden standardm\u00E4\u00DFig 0=zu produzierende / 1=offene / 2=alle Lose angezeigt." },
			{ ParameterFac.ARBEITSPLATZPARAMETER_PFAD_MIT_PARAMETER_SCANTOOL, "java.lang.String",
					"c:/Programme/LPScan/LPScan.exe /F PDF /D 0 /S V /C 0 /A 200 /M H /P",
					"Pfad samt Parameter f\u00FCr das Logp-Scan-Tool. (c:/Programme/LPScan/LPScan.exe /F PDF /D 0 /S V /C 0 /A 200 /M H /P)" },
			{ ParameterFac.ARBEITSPLATZPARAMETER_BACKGROUND_ENABLED, "java.lang.Boolean", "1",
					"Hintergrundbild anzeigen" },
			{ ParameterFac.ARBEITSPLATZPARAMETER_AUFTRAGSABLIEFERUNG_IM_LOS, "java.lang.Boolean", "0",
					"Auftagsablieferung-Symbol im Los anzeigen" },

	};

	// MANDANTENPARAMETER
	// Fix verdrahtete Parameter, Formatierung so belassen!
	public static final String[][] progMandantParameter = {
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT, "1",
					"java.lang.Integer", "Monat, in dem das Geschf\u00E4tsjahr beginnt. 1=J\u00E4nner 12=Dezember",
					"Monat, in dem das Gesch\u00E4ftsjahr beginnt. 1=J\u00E4nner 12=Dezember" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS, "0", "java.lang.Integer",
					"Zur Jahreszahl des Beginndatums eines Gesch\u00E4ftsjahres 1 addieren",
					"Zur Jahreszahl des Beginndatums eines Gesch\u00E4ftsjahres 1 addieren" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_WAEHRUNGKURSAELTERALS, "7", "java.lang.Integer",
					"Wie alt darf W\u00E4hrungsinfo sein", "Wie alt darf W\u00E4hrungsinfo sein" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZESSION, "70", "java.lang.Integer", "Zession",
					"Default Zessionsbetrag f\u00FCr NeuKunden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZESSIONSTEXT, "", "java.lang.String",
					"Zessionstext", "Default Zessionstext" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART,
					ArtikelFac.ARTIKELART_ARTIKEL, "java.lang.String", "Default Artikelart", "Default Artikelart" },
			/*
			 * { ParameterFac.KATEGORIE_KUNDE,
			 * ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART,
			 * ArtikelFac.ARTIKELART_ARTIKEL, "java.lang.Integer"},
			 */

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_AUFSCHLAG, "0", "java.lang.Double",
					"Default Aufschlag", "Default Aufschlag" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_DECKUNGSBEITRAG, "30",
					"java.lang.Double", "Default Deckungsbeitrag", "Default Artikeldeckungsbeitrag" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT, SystemFac.EINHEIT_STUECK,
					"java.lang.String", "Default Einheit", "Default Artikeleinheit" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_SOLLVERKAUF, "0",
					"java.lang.Double", "Default Soll", "Default Soll" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER, "12",
					"java.lang.Integer", "Maximall\u00E4nge Artikelnummer", "Maximall\u00E4nge Artikelnummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_KURZE_ARTIKELNUMMER_ERLAUBT, "Ja",
					"java.lang.String", "kurze Artikelnummer erlaubt", "kurze Artikelnummer erlaubt" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_ARTIKELNUMMER, "5",
					"java.lang.Integer", "Mindestl\u00E4nge Artikelnummer", "Mindestl\u00E4nge Artikelnummer" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE, "6", "java.lang.Integer",
					"Lieferantenrechnungsnummer", "L\u00E4nge der Lieferantenrechnungsnummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG, "3",
					"java.lang.Integer", "Artikell\u00E4nge Herstellerbezeichnung",
					"Artikell\u00E4nge Herstellerbezeichnung" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER, "5",
					"java.lang.Integer", "Mindestl\u00E4nge Seriennummer", "Mindestl\u00E4nge Seriennummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER, "15",
					"java.lang.Integer", "Maximall\u00E4nge Seriennummer", "Maximall\u00E4nge Seriennummer" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_HANDLAGERBUCHUNG_MENGE_OBERGRENZE, "100",
					"java.math.BigDecimal", "Maximal abbuchbare Menge bis Warnung",
					"Maximal abbuchbare Menge bis Warnung" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_HANDLAGERBUCHUNG_WERT_OBERGRENZE, "100000",
					"java.math.BigDecimal", "Maximal abbuchbarer Wert bis Warnung",
					"Maximal abbuchbarer Wert bis Warnung" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG, "0",
					"java.lang.Boolean", "Positionskontierung",
					"Boolean Wert Postionskontierung wenn 1 (ja) Artikel bestimmt MWST, wenn 0 (nein) gilt MWST des Kunden" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_EK_PREISE, "KRITISCH", "java.lang.String",
					"EK-Preise", "Anzeigen der EK-Preise nur wenn Recht darauf" },

			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG, ParameterFac.PARAMETER_FRACHTIDENT, "", "java.lang.String",
					"Fracht-Ident", "Sonderkreis f\u00FCr Frachtartikel" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEBITORENNUMMER_VON, "200000", "java.lang.Integer",
					"Debitorennummer-Von", "Debitorennummer-Vonl" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEBITORENNUMMER_BIS, "299999", "java.lang.Integer",
					"Debitorennummer-Bis", "Debitorennummer-Bis" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KREDITLIMIT, "1000", "java.lang.Integer",
					"Kreditlimit", "Kreditlimit" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KURZWAHL_VON, "0", "java.lang.Integer",
					"Kurzwahlkreis-Von", "Kurzwahlkreis-Von" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KURZWAHL_BIS, "4999", "java.lang.Integer",
					"Kurzwahlkreis-Bis", "Kurzwahlkreis-Bis" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_NETTOPREISE, "1", "java.lang.Boolean",
					"Nettopreise", "Kunden mit NettoPreisen anlegen" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_OFFENER_WE, "0", "java.lang.Boolean", "Offener WE",
					"Pro Kunden den offenen Wareneingang ermitteln" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_TOURSTATISTIK, "0", "java.lang.Boolean",
					"Tourstatistik", "Tourstatistik" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_NUMMER, "0", "java.lang.Boolean",
					"Kundennnummer rein numerisch erzeugen", "Kundennnummer rein numerisch erzeugen" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_KREDITLIMIT, "1000", "java.lang.Integer",
					"Kreditlimit", "Kreditlimit" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_KREDITOREN_VON, "300000", "java.lang.Integer",
					"Kreditoren-Von", "Kreditoren-Von" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_KREDITOREN_BIS, "399999", "java.lang.Integer",
					"Kreditoren-Bis", "Kreditoren-Bis" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_KURZWAHL_VON, "5000", "java.lang.Integer",
					"Kurzwahlkreis-Von", "Kurzwahlkreis-Von" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_KURZWAHL_BIS, "9999", "java.lang.Integer",
					"Kurzwahlkreis-Bis", "Kurzwahlkreis-Bis" },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_NUMMER, "0", "java.lang.Boolean",
					"Lieferantennummer rein numerisch erzeugen", "Lieferantennummer rein numerisch erzeugen" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DELAY_E_MAILVERSAND, "5", "java.lang.Integer",
					"E-Mail Delay", "Zeit bis die E-Mail wirklich verschickt werden in Minuten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_EINKAUSPREIS_ABWEICHUNG, "10",
					"java.lang.Integer", "Einkaufspreisabweichung", "Einkaufspreisabweichung" },

			// { ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.PARAMETER_DEFAULT_KOSTENSTELLE, "10",
			// "java.lang.String", "Default Kostenstelle",
			// "Default Kostenstelle" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR, "0", "java.lang.Integer",
					"Gemeinkostenfaktor", "Gemeinkostenfaktor" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR, "2",
					"java.lang.String", "Anzahl der Stellen des Gesch\u00E4ftsjahres",
					"Anzahl der Stellen des Gesch\u00E4ftsjahres" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN, "/",
					"java.lang.String", "Belegnummerntrennzeichen",
					"Trennzeichen zwischen Gesch\u00E4ftsjahr und laufender Nummer" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER, "7",
					"java.lang.String", "Anzahl der Stellen der laufenden Nummer",
					"Anzahl der Stellen der laufenden Nummer" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN, "6",
					"java.lang.Integer", "Stellen der Kontonummer f\u00FCr Sachkonten",
					"Stellen der Kontonummer f\u00FCr Sachkonten" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_DEBITORENKONTEN, "6",
					"java.lang.Integer", "Stellen der Kontonummer f\u00FCr Debitorenkonten",
					"Stellen der Kontonummer f\u00FCr Debitorenkonten" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_KREDITORENKONTEN, "6",
					"java.lang.Integer", "Stellen der Kontonummer f\u00FCr Kreditorenkonten",
					"Stellen der Kontonummer f\u00FCr Kreditorenkonten" },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT, "42",
					"java.lang.Integer", "Default f\u00FCr die G\u00FCltigkeit eines Angebots in Tagen ab Belegdatum",
					"Default f\u00FCr die G\u00FCltigkeit eines Angebots in Tagen ab Belegdatum" },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEITSART, "1",
					"java.lang.Integer", "Legt fest, ob Parameter DEFAULT_ANGEBOT_GUELTIGKEIT verwendet wird",
					"G\u00FCltigkeitsart 0: Das Angebot gilt bis zum Ende des laufenden Gesch\u00E4ftsjahres, G\u00FCltigkeitsart 1: Das Angebot gilt ab Belegdatum f\u00FCr DEFAULT_ANGEBOT_GUELTIGKEIT Tage" },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_PRUEFESTANDARDDBARTIKEL, "1",
					"java.lang.Boolean", "Abfrage beim Abspeichern einer Ident Position?",
					"Soll es eine Benutzerabfrage beim Abspeichern einer Ident Position geben, wenn der DB des Artikels geringer ist, als der Standard DB des Artikels beim Mandanten?" },

			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_AUFTRAG, "7",
					"java.lang.Integer", "", "" }, // UW->JE

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_MAHNUNG_AB_RECHNUNGSBETRAG, "7",
					"java.math.BigDecimal", "Ab welchem Rechnungsbetrag werden Rechnungen gemahnt",
					"Ab welchem Rechnungsbetrag werden Rechnungen gemahnt (in Mandantenw\u00E4hrung)" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_MAHNUNG_ZAHLUNGSFRIST, "7",
					"java.lang.Integer", "Zahlungsfrist ab heute in Tagen", "Zahlungsfrist ab heute in Tagen" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_LAGER, "12", "java.lang.Integer", // UW->CK
																												// fix
																												// verdrahteten
																												// Wert
					// pruefen, kommt vom Helium
					"Default Lager", "Default Lager" },

			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT, "h",
					"java.lang.String", "Default Zeiteinheit f\u00FCr den St\u00FCcklisten Arbeitsplan",
					"Legt fest, in welcher Einheit die St\u00FCckzeit und R\u00FCstzeit angezeigt werden soll. M\u00F6glich sind (h/min/s)" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN, "2",
					"java.lang.Integer", "Anzahl der Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK, "2",
					"java.lang.Integer", "Anzahl der EK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der EK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK, "2",
					"java.lang.Integer", "Anzahl der VK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der VK-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN, "3",
					"java.lang.Integer", "Anzahl der Nachkommastellen f\u00FCr Mengenfelder im UI",
					"Anzahl der Nachkommastellen f\u00FCr Mengenfelder im UI" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.MATERIALGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Materialkosten", "Gemeinkostenfaktor fuer Materialkosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ARBEITSZEITGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Arbeitszeit", "Gemeinkostenfaktor fuer Arbeitszeit" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Fertigungskosten", "Gemeinkostenfaktor fuer Fertigungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Entwicklungskosten", "Gemeinkostenfaktor fuer Entwicklungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Verwaltungskosten", "Gemeinkostenfaktor fuer Verwaltungskosten" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR, "0", "java.lang.Double",
					"Gemeinkostenfaktor fuer Vertriebskosten", "Gemeinkostenfaktor fuer Vertriebskosten" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT, "14",
					"java.lang.Integer", "Default-Durchlaufzeit in Tagen (f\u00FCr die interne Bestellung)",
					"Default-Durchlaufzeit in Tagen (f\u00FCr die interne Bestellung)" },

			{ ParameterFac.KATEGORIE_GUTSCHRIFT, ParameterFac.GUTSCHRIFT_VERWENDET_NUMMERNKREIS_DER_RECHNUNG, "0",
					"java.lang.Boolean", "Gutschriften und Rechnungen verwenden denselben Nummernkreis",
					"Gutschriften und Rechnungen verwenden denselben Nummernkreis" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.FERTIGUNG_FERTIGUNGSBEGLEITSCHEIN_MIT_SOLLDATEN, "1",
					"java.lang.Boolean", "Fertigungsbegleitschein mit Solldaten drucken",
					"Fertigungsbegleitschein mit Solldaten drucken" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_EINGANGSRECHNUNG,
					"c:\\export_er.csv", "java.lang.String", "Default Export-Zieldatei Eingangsrechnung",
					"Default Export-Zieldatei Eingangsrechnung" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_RECHNUNG, "c:\\export_ar.csv",
					"java.lang.String", "Default Export-Zieldatei Rechnung", "Default Export-Zieldatei Rechnung" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_GUTSCHRIFT, "c:\\export_gs.csv",
					"java.lang.String", "Default Export-Zieldatei Gutschrift", "Default Export-Zieldatei Gutschrift" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_MONATE, "1",
					"java.lang.Integer", "UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Monaten",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Monaten" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_TAGE, "15",
					"java.lang.Integer", "UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Tagen",
					"UVA Verprobung: Erlaubter R\u00FCckbuchungszeitraum in Tagen" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.ANWESENHEITSLISTE_TELEFON_PRIVAT_ANZEIGEN, "1",
					"java.lang.Boolean", "Anzeige der Privaten Telefonnummer in der Anwesenheitsliste",
					"Anzeige der Privaten Telefonnummer in der Anwesenheitsliste" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS, "15",
					"java.lang.Integer", "Wie lange darf man die Zeitbuchungen des Vormonats ver\u00E4ndern",
					"Ist der Parameter positiv (> 0) d\u00FCrfen Zeitbuchungen des Vormonats bis zum angegebenen Tag (= Wert) des "
							+ "aktuellen Monats ver\u00E4ndert werden.\n"
							+ "Bei einem Wert von -1 gibt es keine Einschr\u00E4nkung.\n"
							+ "Ist der Parameter negativ (< -1), legt der Wert die Tage fest, die vom aktuellen Datum an zur\u00FCckgerechnet werden. In "
							+ "diesem Zeitraum d\u00FCrfen Zeitbuchungen ver\u00E4ndert werden." },

			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG, "10",
					"java.lang.Integer", "Erh\u00F6hung, wenn eine neue Position eingef\u00FCgt wird",
					"Wert, um den die Arbeitsgangnummer erh\u00F6ht wird, wenn eine neue Position eingef\u00FCgt wird" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_SACHKONTEN,
					"c:\\export_sachkonten.csv", "java.lang.String", "Default Export-Zieldatei Sachkonten",
					"Default Export-Zieldatei Sachkonten" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_DEBITORENKONTEN,
					"c:\\export_debitorenkonten.csv", "java.lang.String", "Default Export-Zieldatei Debitorenkonten",
					"Default Export-Zieldatei Debitorenkonten" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_KREDITORENKONTEN,
					"c:\\export_kreditorenkonten.csv", "java.lang.String", "Default Export-Zieldatei Kreditorenkonten",
					"Default Export-Zieldatei Kreditorenkonten" },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_AG_STKL_AUFLOESUNG_TIEFE, "3",
					"java.lang.Integer", "Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_AGVORKALK_STKL_AUFLOESUNG_TIEFE, "3",
					"java.lang.Integer", "Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Vorkalkulation Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AG Vorkalkulation Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE, "3",
					"java.lang.Integer", "Tiefe der St\u00FCcklistenaufl\u00F6sung am AB Vorkalkulation Druck",
					"Tiefe der St\u00FCcklistenaufl\u00F6sung am AB Vorkalkulation Druck.\n0 = die St\u00FCckliste wird ohne Positionen angedruckt,\n1 = die Positionen der St\u00FCckliste werden ohne weitere Aufl\u00F6sung angedruckt, 2 = die Positionen der St\u00FCckliste werden mit der ersten Ebene der Unterpositionen angedruckt, usw." },

			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN, "1",
					"java.lang.Boolean", "Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN, "1",
					"java.lang.Boolean", "Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN, "1",
					"java.lang.Boolean", "Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN, "1",
					"java.lang.Boolean", "Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN, "1",
					"java.lang.Boolean", "Konditionen m\u00FCssen best\u00E4tigt werden",
					"Konditionen m\u00FCssen vor dem erstmaligen Aktivieren eines Belegs best\u00E4tigt werden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL, "0", "java.lang.String",
					"Amtsleitungsvorwahl", "Amtsleitungsvorwahl zum direkten Faxversand" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_INLANDSVORWAHL, "0", "java.lang.String",
					"Inlandsvorwahl.", "Inlandsvorwahl. z.B. 0" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUSLANDSVORWAHL, "00", "java.lang.String",
					"Auslandsvorwahl.", "Auslandsvorwahl. z.B. 00" },

			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_OFFSET_LIEFERZEIT_IN_TAGEN, "14",
					"java.lang.Integer", "Anzahl der Tage ab Bestelldatum bis zur Lieferung",
					"Anzahl der Tage ab Bestelldatum bis zur Lieferung" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN, "1",
					"java.lang.Boolean", "Kommentar am Fertigungsbegleitschein drucken",
					"Kommentar am Fertigungsbegleitschein drucken" },

			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG, "14",
					"java.lang.Integer", "Default- Lieferzeit im Auftrag", "Default- Lieferzeit im Auftrag" },

			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_ANGEBOT, "0",
					"java.lang.Integer", "Default- Lieferzeit im Angebot", "Default- Lieferzeit im Angebot" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER, "1",
					"java.lang.Boolean", "F\u00FCr die Berechnung des Gestpreises wird das Hauptlager herangezogen.",
					"Wenn Ja:F\u00FCr die Berechnung des Gestehungspreises wird das Hauptlager herangezogen. Wenn Nein: Der Gestehungspreis berechnet sich aus dem Mittelwert der Gestehungspreise aller L\u00E4ger." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN, "0", "java.lang.Boolean",
					"Logo immer drucken", "Logo immer drucken" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_A, "80", "java.lang.Integer",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse A",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse A" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_B, "15", "java.lang.Integer",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse B",
					"Prozent- Wert f\u00FCr die Einteilung in Klasse B" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_AG_VORKALK_AUFLOESUNG_MAX_TIEFE, "99",
					"java.lang.Integer", "Maximale Tiefe der Ebenenaufl\u00F6sung in der AG- Vorkalkulation",
					"Maximale Tiefe der Aufl\u00F6sung von St\u00FCcklisten und Angebotst\u00FCcklisten zusammen in der Angebotsvorkalkulation." },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GERINGEREN_VKPREIS_VORSCHLAGEN, "1",
					"java.lang.Boolean", "Geringeren VK-Preis f\u00FCr einen Artikel vorschlagen",
					"Wenn aufgrund der hinterlegten Preisinformationen ein geringerer VK-Preis f\u00FCr einen Artikel m\u00F6glich w\u00E4re, als die VK-Preisfindung liefert, erh\u00E4lt der Benutzer einen entsprechenden Hinweis." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL, "",
					"java.lang.String", "Default-Arbeitszeitartikel f\u00FCr BDE (Artikelnummer)",
					"Default-Arbeitszeitartikel f\u00FCr BDE (Artikelnummer)." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LAENGE_ZESTIFTKENNUNG, "6", "java.lang.Integer",
					"L\u00E4nge der Kennung eines ZE-Stiftes", "L\u00E4nge der Kennung eines ZE-Stiftes." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZESTIFT_ABSTAND_BUCHUNGEN, "10",
					"java.lang.Integer", "Mindestabstand gleicher Zeitbuchungen in Sekunden",
					"Wenn dieser unterschritten wird, wird die Zeitbuchung des ZE-Stiftes verworfen." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZESTIFT_STUECKRUECKMELDUNG, "0",
					"java.lang.Boolean", "Gutst\u00FCck um 1 erh\u00F6hen bei ZE-Stiftbuchung.",
					"Gutst\u00FCck um 1 erh\u00F6hen, wenn per ZE-Stift auf einen Arbeitsgang gebucht wird." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LEERZEILE_NACH_MENGENBEHAFTETERPOSITION, "0",
					"java.lang.Boolean", "Leerzeile nach einer mengenbehafteten Position",
					"Am Belegdruck automatisch eine Leerzeile nach einer mengenbehafteten Position einf\u00FCgen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_RABATTIERBAR, "1",
					"java.lang.Boolean", "Default- Einstellung der Option -Rabattierbar-",
					"Default- Einstellung der Option -Rabattierbar-" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT, "",
					"java.lang.String", "Unterdr\u00FCcke Position mit diesem Artikel am Fertigungsbegleitschein.",
					"Eine Arbeitsplanposition mit diesem Artikel in Kombination mit einer Maschine wird am Fertigungsbegleitschein nicht angedruckt." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZUTRITT_DATEN_VORLADEN, "15", "java.lang.Integer",
					"Anzahl der Tage, f\u00FCr die Zutrittsdaten generiert werden.",
					"Setzt die Anzahl der Tage fest, f\u00FCr die Zutrittsdaten generiert werden." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZUTRITT_DEFAULT_ZUTRITTSKLASSE, "",
					"java.lang.String", "Default- Zutrittsklasse.", "Default- Zutrittsklasse." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ANZAHL_GRUPPEN,
					"3", "java.lang.Integer", "Anzahl der dargestellten T\u00E4tigkeitsgruppen",
					"Anzahl der dargestellten T\u00E4tigkeitsgruppen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ZEITRAUM, "20",
					"java.lang.Integer", "Darstellungszeitraum der Kapazit\u00E4tsvorschau",
					"Darstellungszeitraum der Kapazit\u00E4tsvorschau" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LOGON_LOESCHEN, "30", "java.lang.Integer",
					"Anzahl der Tage, nach der Logon-Eintr\u00E4ge gel\u00F6scht werden",
					"Anzahl der Tage, nach der Logon-Eintr\u00E4ge gel\u00F6scht werden" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_EXPORTZIEL,
					"c:\\export_zahlungen.csv", "java.lang.String", "Default Export-Zieldatei Zahlungen",
					"Default Export-Zieldatei Zahlungen" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_MIT_SKONTO, "1",
					"java.lang.Boolean", "Default mit Skonto bezahlen", "Default mit Skonto bezahlen" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_PERIODE, "7",
					"java.lang.Integer", "Default Zeitraum zwischen den Zahlungsl\u00E4ufen in Tagen",
					"Default Zeitraum zwischen den Zahlungsl\u00E4ufen in Tagen" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
					ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_SKONTOUEBERZIEHUNGSFRIST, "1", "java.lang.Integer",
					"Default Skonto\u00FCberziehungsfrist in Tagen", "Default Skonto\u00FCberziehungsfrist in Tagen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE, "0",
					"java.lang.Boolean", "Vorbelegung des Vertreters in den Belegen.",
					"0 = Angemeldete Person, 1 = Provisionsempfaenger aus Kunde." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
					"0", "java.lang.Boolean",
					"Arbeitszeitartikel f\u00FCr ZE kommen aus der Personalverf\u00FCgbarkeit.",
					"Arbeitszeitartikel f\u00FCr Zeiterfassung kommen aus der Personalverf\u00FCgbarkeit." },
			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG, ParameterFac.PARAMETER_RECHNUNG_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Rechnungsnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Rechnungsnummer im neuen Gesch\u00E4ftsjahr." },

			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG, ParameterFac.PARAMETER_PROFORMARECHNUNG_BELEGNUMMERSTARTWERT, "1",
						"java.lang.Integer", "Startwert der Proforma-Rechnungsnummer im neuen Gesch\u00E4ftsjahr.",
						"Startwert der Proforma-Rechnungsnummer im neuen Gesch\u00E4ftsjahr." },

				{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_BELEGNUMMERSTARTWERT,
					"1", "java.lang.Integer", "Startwert der ER-Nummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der ER-Nummer im neuen Gesch\u00E4ftsjahr." },

			{ ParameterFac.KATEGORIE_GUTSCHRIFT, ParameterFac.PARAMETER_GUTSCHRIFT_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_ANFRAGE_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_LIEFERSCHEIN_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BESTELLUNG_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },

			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_ANFRAGE_ANSP_VORBESETZEN, "1", "java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BESTELLUNG_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean", "Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Lieferanten vorbesetzen." },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_ANGEBOTSTKL_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean", "Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_ANSP_VORBESETZEN, "1", "java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_ANSP_VORBESETZEN, "1", "java.lang.Boolean",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_LIEFERSCHEIN_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean", "Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },
			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG, ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN, "1",
					"java.lang.Boolean", "Den Ansprechpartner nach Auswahl des Kunden vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Kunden vorbesetzen." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LAENGE_PARTNER_KURZBEZEICHNUNG, "25",
					"java.lang.Integer", "L\u00E4nge des Feldes Kurzbezeichnung in Partner/Kunde/Lieferant (Max.:40)",
					"L\u00E4nge des Feldes Kurzbezeichnung in Partner/Kunde/Lieferant. Gr\u00F6sstm\u00F6glicher Wert: 40" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ, "1", "java.lang.Boolean",
					"Suchen inklusive Kurzbezeichnung in Partner/Kunde/Lieferant",
					"Wenn nach dem Filterkriterium -Firma- in Partner/Kunde/Lieferant gesucht wird, wird die Kurzbezeichnung ebenfalls durchsucht." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH, "0",
					"java.lang.Boolean", "Zeitbuchungen sind auch auf erledigte Auftr\u00E4ge/Lose m\u00F6glich.",
					"Zeitbuchungen sind auch auf erledigte Auftr\u00E4ge/Lose m\u00F6glich." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH, "0",
					"java.lang.Boolean", "Zeitbuchungen sind auch auf angelegte Lose m\u00F6glich.",
					"Zeitbuchungen sind auch auf angelegte Lose m\u00F6glich." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KUNDE_STATT_BEZEICHNUNG_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Kunde statt Artikelbezeichnung in der Los-Auswahlliste anzeigen.",
					"Kunde statt Artikelbezeichnung in der Los-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KUNDE_IN_LOSAUSWAHLLISTE, "0",
					"java.lang.Boolean", "Kunde in der Los-Auswahlliste anzeigen.",
					"Kunde in der Los-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG, "0",
					"java.lang.Boolean", "Beidseitige Wildcard im Partnernamen in der Auswahlliste",
					"Beidseitige Wildcard im Filterkriterium Firma in der Kunden/Partner/Lieferantenauswahlliste." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN, "0",
					"java.lang.Boolean", "Default-Wert f\u00FCr Option -Akzeptiert Teillieferungen- in Kunde.",
					"Default-Wert f\u00FCr Option -Akzeptiert Teillieferungen- in Kundenkonditionen." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_BDE_MIT_TAETIGKEIT, "1", "java.lang.Boolean",
					"Die HTML BDE-Station ben\u00F6tigt eine T\u00E4tigkeit.",
					"Die HTML BDE-Station ben\u00F6tigt eine T\u00E4tigkeit." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LEAD_IN_AUSWEISNUMMER_MECS, "",
					"java.lang.String", "Lead-In f\u00FCr Ausweisnummer im MECS-Terminal (Barcodescanner).",
					"Lead-In f\u00FCr Ausweisnummer im MECS-Terminal in Verbindung mit einem Barcodescanner. Der Wert ist per Default leer und wird mit $P bef\u00FCllt, wenn ein MECS-Terminal in Verbindung mit einem Barcodescanner verwendet wird." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT, "Kalenderwochen",
					"java.lang.String", "Wiederbeschaffungszeit in Kalenderwochen oder Tagen.",
					"Artikel- Wiederbeschaffungszeit in Kalenderwochen oder Tagen. Zul\u00E4ssige Werte: Kalenderwochen und Tage." },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_DEFAULT_CHARGENNUMMERNBEHAFTET, "0",
					"java.lang.Boolean", "Artikel ist per Default Chargennummernbehaftet.",
					"Artikel ist per Default Chargennummernbehaftet." },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_DEFAULT_SERIENNUMMERNBEHAFTET, "0",
					"java.lang.Boolean", "Artikel ist per Default Seriennummernbehaftet.",
					"Artikel ist per Default Seriennummernbehaftet." },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
					"0", "java.lang.Boolean", "Die Chargennummer beinhaltet das Mindesthaltbarkeitsdatum.",
					"Die Chargennummer beinhaltet in den ersten 8 Stellen das Mindesthaltbarkeitsdatum. (JJJJMMTT)" },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_EINGANG,
					"c:\\intrastat_eingang.csv", "java.lang.String", "Default Export-Zieldatei Intrastat-Eingang.",
					"Default Export-Zieldatei Intrastat-Eingang." },

			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_VERSAND,
					"c:\\intrastat_versand.csv", "java.lang.String", "Default Export-Zieldatei Intrastat-Versand.",
					"Default Export-Zieldatei Intrastat-Versand." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL_TELEFON, "",
					"java.lang.String", "Amtsleitungsvorwahl f\u00FCr Telefon.",
					"Amtsleitungsvorwahl f\u00FCr Telefon (Direktes w\u00E4hlen/TAPI)." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ, "", "java.lang.Integer",
					"Default- Mehrwersteuersatz.",
					"Default- Mehrwersteuersatz. Hier wird die Id des Mehrwertsteuersatzes aus der Datenbank (Tabelle LP_MWSTSATZ) eingetragen. Die Id muss vom richtigen Mandanten sein." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_BONITAETSWARNUNGSZEITRAUM, "11",
					"java.lang.Integer", "Bonit\u00E4tswarnungszeitraum im Monaten.",
					"Bonit\u00E4tswarnungszeitraum im Monaten." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM, "14",
					"java.lang.Integer", "Verdichtungszeitraum in Tagen.", "Verdichtungszeitraum in Tagen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN, "0",
					"java.lang.Boolean", "Die Istzeiten werden den Sollzeiten gleichgesetzt (Nachkalkulation).",
					"F\u00FCr die Nachkalkulation werden die Istzeiten den Sollzeiten gleichgesetzt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL, "0",
					"java.lang.Boolean", "Im Fertigungsbegleitschein wird das Material mit angedruckt.",
					"Im Fertigungsbegleitschein wird das Material mit angedruckt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOS_BELEGNUMMERSTARTWERT_FREIE_LOSE, "1",
					"java.lang.Integer", "Startwert der Losnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Losnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN, "0",
					"java.lang.Integer", "Auftragsbezogene Losnummern erzeugen.",
					"0= Losnummer fortlaufend, 1= Auftragsbezogene Losnummer , 2= Auftragsbezogene Losnummer nach Bereichen" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDE_MIT_NUMMER, "0", "java.lang.Integer",
					"Kunden haben eine Kundennummer.",
					"Kunden haben eine Kundennummer (0=Aus, 1= Pflichtfeld, 2=kein Pflichtfeld)" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_AUTOMATISCHES_KOMMT, "0", "java.lang.Integer",
					"Automatisches Kommt.",
					"0= Aus, 1= Kommt/ Unterbrechung- Ende wird automatisch gebucht, wenn noch nicht vorhanden, 2= Kommt wird gebucht, wenn noch nicht vorhanden" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE,
					FibuExportFac.VARIANTE_KOSTENSTELLEN, "java.lang.String", "Definiert die Art des Exports.",
					"Definiert die Art des Exports." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_ZIELPROGRAMM, FibuExportFac.ZIEL_RZL,
					"java.lang.String", "Ziel-Buchhaltungssoftware.", "Ziel-Buchhaltungssoftware." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT, "0",
					"java.lang.Boolean", "\u00DCberschriften exportieren.",
					"Die Spalten-\u00DCberschriften in der Exportdatei angeben." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR,
					"n/a", "java.lang.String", "Default-Konto f\u00FCr Positionen ohne Artikelgruppe.",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe (Rechnung/Gutschrift)." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_ER,
					"n/a", "java.lang.String", "Default-Konto f\u00FCr Positionen ohne Artikelgruppe.",
					"Default-Konto f\u00FCr Positionen ohne Artikelgruppe (Eingangsrechnung)." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_EXPORT_BMD_BENUTZERNUMMER, "01",
					"java.lang.String", "Benutzernummer in BMD.", "Benutzernummer in BMD (2 Ziffern)." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BEWEGUNGSMODULE_SORTIERUNG_PARTNER_ORT, "0",
					"java.lang.Boolean", "Default-Ausschaltet",
					"Einschalten/ Ausschalten - Sortierung nach Partner und Ort in die Bewegungsmodule.Neustart erforderlich." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELNUMMER_ZEICHENSATZ,
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_\u00C4\u00D6\u00DC", "java.lang.String",
					"Bestimmt die in einer Artikelnummer verwendbaren Zeichen.",
					"Bestimmt die in einer Artikelnummer verwendbaren Zeichen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER,
					"0", "java.lang.Boolean", "Direktfilter -Suche nach AG/AK- statt Referenznummer.",
					"Direktfilter -Suche nach Artikelgruppe/Artikelklasse- statt Referenznummer." },
			{ ParameterFac.KATEGORIE_AUSGANGSRECHNUNG, ParameterFac.PARAMETER_WA_NUR_MIT_LS, "0", "java.lang.Boolean",
					"Der Warenausgang darf ausschlie\u00DFlich mit LS gebucht werden.",
					"Der Warenausgang darf ausschlie\u00DFlich mit LS gebucht werden." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSERIENNUMMERN_MIT_ETIKETTEN, "0",
					"java.lang.Boolean", "Auftrags Seriennummer fortlaufend ab Start mit Etiketten.",
					"Auftrags Seriennummer fortlaufend ab Start mit Etiketten." },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN, "1",
					"java.lang.Boolean", "Unterst\u00FCcklisten werden automatisch ausgegeben.",
					"Unterst\u00FCcklisten werden automatisch ausgegeben." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN, "0",
					"java.lang.Boolean", "Bei Erledigung eines Loses wird das fehlende Material nachgebucht.",
					"Bei Erledigung eines Loses wird das fehlende Material nachgebucht." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_RAHMENAUFTRAEGE_IN_FERTIGUNG_VERWENDBAR, "0",
					"java.lang.Boolean",
					"In den Los-Kopfdaten k\u00F6nnen auch Rahmenauftr\u00E4ge ausgew\u00E4hlt werden.",
					"In den Los-Kopfdaten k\u00F6nnen auch Rahmenauftr\u00E4ge ausgew\u00E4hlt werden." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Abmessungen statt Zusatzbez. in der Artikel-Auswahlliste anzeigen.",
					"Abmessungen statt Zusatzbezeichnung in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_RANKINGLISTE_ZUSATZSTATUS, "",
					"java.lang.Integer", "I_ID des Zusatzstatus f\u00FCr Rankingsliste.",
					"I_ID des Zusatzstatus f\u00FCr Rankingsliste." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VKPREIS_STATT_GESTPREIS_IN_ARTIKELAUSWAHL, "0",
					"java.lang.Integer",
					"0= Gestpreis - 1=erster berechneter VK-Preis - 2=Lief1Preis in der Artikel-Auswahlliste anzeigen - 3= VK-PReis/Gestpreis/Lief1Preis anzeigen.",
					"0= Gestpreis - 1=erster berechneter VK-Preis - 2=Lief1Preis in der Artikel-Auswahlliste anzeigen - 3= VK-PReis/Gestpreis/Lief1Preis anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_AB_MONATE, "6",
					"java.lang.Integer", "Ab wievielen Monaten wird der Gestehungspreis abgewertet.",
					"Ab wievielen Monaten wird der Gestehungspreis abgewertet." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_PROZENT_PRO_MONAT, "10",
					"java.lang.Double", "Um wieviele Prozent wird der Gestehungspreis pro Monat abgewertet.",
					"Um wieviele Prozent wird der Gestehungspreis pro Monat abgewertet." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER, "0",
					"java.lang.Integer", "Anzeige des Anlegers/\u00C4nderers anstatt des Vertreters in VK-Belegen.",
					"0-Zeigt den Vertreter.\n1-Zeigt den Anleger.\n2-Zeigt den \u00C4nderer" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_EINKAUFSPREIS_ZURUECKPFLEGEN, "0",
					"java.lang.Integer", "Der Einkaufspreis wird in den Artikellieferant zur\u00FCckgepflegt.",
					"0 ... nicht zur\u00FCckpflegen 1 ... Staffelpreis zur\u00FCckpflegen per default angehakt. 2 ... immer nur Einzelpreiszur\u00FCckpflegen per default angehakt." },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_OFFSET_ZIELTERMIN, "90",
					"java.lang.Integer", "Default-Zieltermin in 90 Tage.", "Default-Zieltermin in 90 Tage." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK, "0.10",
					"java.lang.Double", "Abweichung Summenausdruck.", "Abweichung Summenausdruck." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_ZAHLUNGSMORAL_MONATE, "12", "java.lang.Integer",
					"Anzahl der Monate, die f\u00FCr die Zahlungsmoral ber\u00FCcksichtigt werden.",
					"Anzahl der Monate, die f\u00FCr die Berechnung der Zahlungsmoral ber\u00FCcksichtigt werden." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_ZAHLUNGSMORAL_ANZAHL_RECHNUNGEN, "3",
					"java.lang.Integer",
					"Anzahl d. Rechnungen, die f\u00FCr die Zahlungsmoral ber\u00FCcksichtigt werden",
					"Anzahl der Rechnungen, die f\u00FCr die Berechnung der Zahlungsmoral ber\u00FCcksichtigt werden" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_WIEDERBSCHAFFUNGSMORAL_MONATE, "24",
					"java.lang.Integer",
					"Anzahl der Monate, die f\u00FCr die Wiederbeschaffungsmoral ber\u00FCcksichtigt werden.",
					"Anzahl der Monate, die f\u00FCr die Berechnung der Wiederbeschaffungsmoral ber\u00FCcksichtigt werden." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_WIEDERBSCHAFFUNGSMORAL_ANZAHL_BESTELLUNGEN, "6",
					"java.lang.Integer",
					"Anzahl d. Bestellungen, die f\u00FCr die Wiederbeschaffungsmoral ber\u00FCcksichtigt werden",
					"Anzahl der Bestellungen, die f\u00FCr die Berechnung der Wiederbeschaffungsmoral ber\u00FCcksichtigt werden" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG, "0",
					"java.lang.Boolean", "Default-Wert f\u00FCr die Option 'Materialbuchung bei Ablieferung'",
					"Default-Wert f\u00FCr die Option 'Materialbuchung bei Ablieferung'" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM, "15",
					"java.lang.Integer", "Legt fest, in welchem Zeitraum Belege angelegt werden d\u00FCrfen.",
					"Ist der Parameter positiv (> 0) d\u00FCrfen Belege bis zum angegebenen Tag (= Wert) im alten Monat angelegt werden.\n"
							+ "Bei einem Wert von -1 ist ein Anlegen eines Belegs innerhalb des aktuellen sowie des vorangegangenen Gesch\u00E4ftsjahres"
							+ " (sofern es nicht gesperrt ist) erlaubt.\n"
							+ "Ist der Parameter negativ (< -1), legt der Wert die Tage fest, die vom aktuellen Datum an zur\u00FCckgerechnet werden. In "
							+ "diesem Zeitraum d\u00FCrfen Belege angelegt werden." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "In den Artikelkopfdaten ist die Artikelgruppe ein Pflichtfeld.",
					"In den Artikelkopfdaten ist die Artikelgruppe ein Pflichtfeld." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELKLASSE_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "In den Artikelkopfdaten ist die Artikelklasse ein Pflichtfeld.",
					"In den Artikelkopfdaten ist die Artikelklasse ein Pflichtfeld." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERSTANDSLISTE_EKPREIS_WENN_GESTPREIS_NULL, "1",
					"java.lang.Boolean", "EK-Preis des 1. Lieferanten, wenn der Gestehungspreis 0 ist.",
					"Wenn der Gestehungspreis 0 ist, dann wird stattdessen der EK-Preis des 1. Lieferanten in der Lagerstandsliste angezeigt." },

			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_A, "1600",
					"java.lang.Integer", "Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse A",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse A" },
			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_B, "1200",
					"java.lang.Integer", "Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse B",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse B" },
			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_C, "800",
					"java.lang.Integer", "Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse C",
					"Anzahl der ben\u00F6tigten Punkte f\u00FCr die Beurteilungsklasse C" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MAXIMALELAENGE_DURCHWAHL_ZENTRALE, "3",
					"java.lang.Integer", "Anzahl der Stellen der Durchwahl der Zentrale",
					"Anzahl der Stellen der Durchwahl der Zentrale" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_URL_ANWENDERSPEZIFISCHE_HILFE, " ",
					"java.lang.String", "URL der Anwenderspezifischen Hilfe", "URL der Anwenderspezifischen Hilfe" },
			{ ParameterFac.KATEGORIE_KUECHE, ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_OSCAR, " ",
					"java.lang.String", "Pfad zu den OSCAR- Kassenimport- Dateien des K\u00FCchenmoduls",
					"Pfad zu den OSCAR- Kassenimport- Dateien des K\u00FCchenmoduls" },
			{ ParameterFac.KATEGORIE_KUECHE, ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_ADS, " ",
					"java.lang.String", "Pfad zu den ADS- Kassenimport- Dateien des K\u00FCchenmoduls",
					"Pfad zu den ADS- Kassenimport- Dateien des K\u00FCchenmoduls" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_AELTESTE_CHARGENNUMMER_VORSCHLAGEN, "0",
					"java.lang.Boolean", "Automatisch die \u00E4lteste Charge bei der Losausgabe vorschlagen.",
					"Automatisch die \u00E4lteste Charge bei der Losausgabe vorschlagen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSAUSGABE_AUTOMATISCH, "0", "java.lang.Integer",
					"Losausgabe ohne Snr/Chnr-Ausgabedialog.",
					"Losausgabe ohne Snr/Chnr-Ausgabedialog. Serien-/Chargennummer werden der Reihe nach verwendet. 0 = Aus , 1 = Dialog erscheint nur, wenn benoetigte Menge groesser als vorhandene Menge, 2 = Dialog erscheint nie." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_LIEFERSCHEIN_UEBERNAHME_NACH_ANSPRECHPARTNER, "0",
					"java.lang.Boolean", "Reihung nach Ansp., wenn mehrere Lieferscheine \u00FCbernommen werden.",
					"Wenn mehrere Lieferscheine in die Rechnung \u00FCbernommen werden dann werden diese nach dem Ansprechpartner sortiert eingef\u00FCgt." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGER_LOGGING, "0", "java.lang.Boolean",
					"Loggt jede Zu- bzw. Abgangsbuchung in der Protokolltabelle mit",
					"Loggt jede Zu- bzw. Abgangsbuchung in LP_PROTOKOLL mit." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_GANZE_CHARGEN_VERWENDEN, "0",
					"java.lang.Boolean", "Nach M\u00F6glichkeit immer ganze Chargen im Los verwenden.",
					"Nach M\u00F6glichkeit immer ganze Chargen im Los verwenden." },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ, "0", "java.lang.Boolean",
					"Geplanten Umsatz in der Projektauswahl anzeigen.",
					"Geplanten Umsatz in der Projektauswahl anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DEFAULT_DOKUMENTE_ANHAENGEN, "0",
					"java.lang.Boolean", "Default-Wert der CheckBox -Dokumente anh\u00E4ngen-",
					"Default-Wert der CheckBox -Dokumente anh\u00E4ngen- im Lieferschein-Versand." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER, "0",
					"java.lang.Boolean", "Debitorennummer bei Ausgangsbelegen anlegen, wenn nicht vorhanden.",
					"Debitorennummer bei Ausgangsbelegen automatisch anlegen, wenn nicht vorhanden." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ERLAUBTE_ABWEICHUNG_INVENTURLISTE, "1000",
					"java.lang.Integer", "Bringt Warnung wenn Abweichung des Inventurstand gr\u00F6\u00DFer als Wert.",
					"Bringt eine Warnung wenn Abweichung des Inventurstand gr\u00F6\u00DFer als Wert ist." },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_FERTIGUNGSGRUPPE_VORBESETZEN, "1",
					"java.lang.Boolean", "Die Fertigungsgruppe wird mit der ersten Fertigungsgruppe vorbesetzt.",
					"Die Fertigungsgruppe wird mit der ersten Fertigungsgruppe vorbesetzt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ENDE_TERMIN_ANSTATT_BEGINN_TERMIN_ANZEIGEN, "0",
					"java.lang.Boolean", "Ende-Termin anstatt des Beginn-Termins anzeigen.",
					"Ende-Termin anstatt des Beginn-Termins anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANREDE_MIT_VORNAME, "0", "java.lang.Boolean",
					"Briefanrede mit Vorname", "Briefanrede mit Vorname" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BRIEFANREDE_MIT_TITEL, "0", "java.lang.Integer",
					"Briefanrede mit Titeln",
					"Briefanrede mit Titeln: 0 = nur vorangestellter Titel / 1 = kein Titel / 2 = beide Titel / 3 = nur nachgestellter Titel" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WE_REFERENZ_IN_STATISTIK, "0", "java.lang.Boolean",
					"Wareneingangsreferenz in Artikelstatistik andrucken.",
					"Wareneingangsreferenz in Artikelstatistik andrucken." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG, "0",
					"java.lang.Boolean", "Lagerzugang mit Hersteller/Ursprungsland.",
					"Lagerzugang mit Hersteller/Ursprungsland." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_EINZELRECHNUNG_EXPORTPFAD, "", "java.lang.String",
					"Server-Pfad des Einzelrechnungsexportes.", "Server-Pfad des Einzelrechnungsexportes." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_NEGATIVE_SOLLMENGEN_BUCHEN_AUF_ZIELLAGER, "0",
					"java.lang.Boolean", "Negative Sollmengen werden auf Ziellager gebucht.",
					"Negative Sollmengen werden auf Ziellager gebucht. Wenn 1 / dann wird das erste Lager der Los-Lagerentnahme verwendet." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_LIEFERDAUER, "2", "java.lang.Integer",
					"Default- Lieferdauer.", "Default- Lieferdauer." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ABSENDERKENNUNG_STRASSE_ORT, "0",
					"java.lang.Boolean", "Absenderkennung im Format Name / Strasse Lkz-Plz-Ort.",
					"Absenderkennung im Format Name / Strasse Lkz-Plz-Ort." },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN, "3", "java.lang.Integer",
					"Default-Nachfasstermin in Tagen.", "Default-Nachfasstermin in Tagen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ABLIEFERUNGSPREIS_IST_DURCHSCHNITTSPREIS, "0",
					"java.lang.Boolean", "Der Preis der Losablieferung ist der Durchschnittspreis.",
					"Der Preis der Losablieferung ist der Durchschnittspreis." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKELKOMMENTAR_IST_HINWEIS, "0",
					"java.lang.Boolean", "Default Kommentarart ist -Hinweis bei Artikelauswahl-.",
					"Default Kommentarart ist -Hinweis bei Artikelauswahl-." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ERWEITERTER_URLAUBSANSPRUCH, "0",
					"java.lang.Integer", "Berechnungsart des Urlaubsanspruches",
					"0 = Standard.  1 = Voller Urlaubsanspruch bis zum Eintritt am 30.6. 2 = Berechnung nach TV\u00D6D" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE, "1",
					"java.lang.Integer", "Mindestl\u00E4nge der Chargennummer", "Mindestl\u00E4nge der Chargennummer" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE, "0", "java.lang.Integer",
					"Quelle der Personalkosten",
					"Quelle der Personalkosten: 0=Gestehungspreis aus T\u00E4tigkeit / 1=Kosten aus Personalgruppe / 2=Stundensatz aus Personalgehalt" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0", "java.lang.Boolean",
					"Lagermindeststandswarnung im Auftrag.", "Lagermindeststandswarnung im Auftrag." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0",
					"java.lang.Boolean", "Lagermindeststandswarnung im Lieferschein.",
					"Lagermindeststandswarnung im Lieferschein." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG, "0", "java.lang.Boolean",
					"Lagermindeststandswarnung in der Rechnung.", "Lagermindeststandswarnung in der Rechnung." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOSAUSWAHL_TECHNIKERFILTER, "0",
					"java.lang.Boolean", "Losauswahl \u00FCber Techniker.",
					"Losauswahl \u00FCber Techniker / Gilt nur f\u00FCr Zeiterfassung." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_MAXIMALE_EINTRAEGE_SOLLZEITPRUEFUNG, "50",
					"java.lang.Integer", "Maximale Eintr\u00E4ge f\u00FCr Sollzeitpr\u00FCfung.",
					"Maximale Eintr\u00E4ge an Zeitdaten die f\u00FCr die Sollzeitpr\u00FCfung vorhanden sein d\u00FCrfen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG, "0",
					"java.lang.Boolean", "Grunds\u00E4tzlich keine automatische Materialentnahmen.",
					"Grunds\u00E4tzlich keine automatische Materialentnahmen weder bei Losausgabe noch bei Erledigung." },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_NUR_TERMINEINGABE, "0", "java.lang.Boolean",
					"Termineingabe ohne Durchlaufzeit.", "Termineingabe ohne Durchlaufzeit." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Auftrag statt Artikel in Losauswahlliste.",
					"Auftrag statt Artikel in Losauswahlliste." },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DURCHLAUFZEIT_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "Durchlaufzeit in St\u00FCckliste ist Pflichtfeld.",
					"Durchlaufzeit in St\u00FCckliste ist Pflichtfeld." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG, "0",
					"java.lang.Boolean", "Theoretische Ist-Zeit Rechnung.",
					"Theoretische Ist-Zeit Rechnung bei Umspann/R\u00FCstzeiten." },
			{ ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_SALDENABFRAGE_NUR_IST_STUNDEN_DES_AKTUELLEN_MONATS, "0", "java.lang.Boolean",
					"Es wird nur der aktuelle Monats IST-Saldo angezeigt.",
					"In der BDE-Station und am Terminal wird nur der aktuelle Monats IST-Saldo angezeigt, ohne \u00DCbertr\u00E4ge aus dem Vormonat." },
			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_SCHECKNUMMER, "-11", "java.lang.Integer",
					"Schecknummer.",
					"Schecknummer. Wird bei ER-Druck um 1 erh\u00F6ht. In ER-Druck nicht sichtbar bei -1" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_STATUSZEILE_KBEZ_STATT_VERPACKUNGSART, "0",
					"java.lang.Boolean", "Kurzbezeichnung statt Verpackungsart in Statuszeile",
					"Kurzbezeichnung+ Index statt Verpackungsart+ Bauform in Statuszeile" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MASCHINENBEZEICHNUNG_ANZEIGEN, "0",
					"java.lang.Boolean", "Maschinenbezeichnung statt Artikelbezeichnung anzeigen.",
					"Im Lossollarbeitsplan die Maschinenbezeichnung statt Artikelbezeichnung anzeigen." },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOS_BEI_AUSGABE_GESTOPPT, "0",
					"java.lang.Boolean", "Das Los ist nach der Ausgabe automatisch gestoppt.",
					"Das Los ist nach der Ausgabe automatisch gestoppt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KUNDE_IST_PFLICHTFELD, "0", "java.lang.Boolean",
					"Kunde im Los ist Pflichtfeld", "Kunde im Los ist Pflichtfeld" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ARBEITSPLAN_DEFAULT_STUECKLISTE, "",
					"java.lang.String", "Default-Arbeitsplan einer neuen St\u00FCckliste.",
					"Artikelnummer des Default-Arbeitsplans einer neuen St\u00FCckliste." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FEHLMENGE_ENTSTEHEN_WARNUNG, "0",
					"java.lang.Boolean", "Warnung bei der Los-Ausgabe wenn Fehlmengen entstehen w\u00FCrden.",
					"Warnung bei der Los-Ausgabe wenn Fehlmengen entstehen w\u00FCrden." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_LIEFERANTEN_AUTOMATISCH_SORTIEREN, "0",
					"java.lang.Boolean", "Automatische Reihung von Lieferanten durch Bestellung.",
					"Gibt an ob beim Statuswechsel der Bestellung von Angelegt auf Offen der ausgew\u00E4hlte Lieferant als bevorzugter Lieferant hinterlegt wird." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Brutto-Summe statt Netto-Summe in Auswahlliste (RE+ER).",
					"Brutto-Summe statt Netto-Summe in Auswahlliste (RE+ER)." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_OFFENE_MENGE_IN_SICHT_AUFTRAG_VORSCHLAGEN,
					"1", "java.lang.Integer", "Die offene Menge in -Sicht Auftrag- als Positionsmenge vorschlagen.",
					"Die offene Menge in -Sicht Auftrag- als Positionsmenge vorschlagen. 0 = Aus, 1 = offene Menge, 2 = Lagerstand des Abbuchungslagers" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_IN_WEP_TEXTEINGABEN_ANZEIGEN, "0",
					"java.lang.Boolean", "Texteingaben in den Wareneingangspositionen anzeigen.",
					"Texteingaben in den Wareneingangspositionen anzeigen." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_MAHNUNGSABSENDER, "0", "java.lang.Integer",
					"0= Administrator / 1=Anforderer", "0= Administrator / 1=Anforderer" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_MAHNUNG_AUTO_CC, "0", "java.lang.Boolean",
					"Absender des Versandauftrages bekommt eine Kopie",
					"Absender des Versandauftrages bekommt eine Kopie" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANFRAGEDATUM_VORBESETZEN, "1", "java.lang.Boolean",
					"Anfragedatum vorbesetzen", "Anfragedatum vorbesetzen" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_LIEFERADRESSE_IST_AUFTRAGSADRESSE, "1",
					"java.lang.Boolean", "Wenn Lieferadresse = Auftragsadresse dann diese nicht drucken.",
					"Wenn Lieferadresse = Auftragsadresse dann diese nicht drucken." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PARTNERBRANCHE_DEFINIERT_KOMMENTAR, "0",
					"java.lang.Boolean", "Es werden die Kommentare der entsprechenden Branche angedruckt.",
					"Es werden die Kommentare der entsprechenden Branche angedruckt." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG, "0",
					"java.lang.Boolean", "Eindeutige Seriennummer \u00FCber alle Artikel.",
					"Eindeutige Seriennummer \u00FCber alle Artikel." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ALLE_LOSE_BERUECKSICHTIGEN, "0",
					"java.lang.Boolean", "In der Nachkalkulation werden alle Lose ber\u00FCcksichtigt.",
					"In der Nachkalkulation werden alle Lose ber\u00FCcksichtigt." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_ARTIKELAUSWAHL, "1",
					"java.lang.Boolean", "0 = Lieferant der Bestellung 1 = Alle.",
					"0 = Lieferant der Bestellung 1 = Alle." },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_FIRMENNUMMER, "PZE001",
					"java.lang.String", "Zeitdatenexport Firmennummer", "Zeitdatenexport Firmennummer" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIELPROGRAMM, "VARIAL",
					"java.lang.String", "Zeitdatenexport Zielprogramm",
					"Zeitdatenexport Zielprogramm Moegliche Optionen: VARIAL / EGECKO / TEXT / CPU-LOHN" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIEL,
					"c:/export_zeitdaten.csv", "java.lang.String", "Zeitdatenexport Exportpfad",
					"Zeitdatenexport Exportpfad" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_PERIODENVERSATZ, "0",
					"java.lang.Integer", "Periodenversatz im Lohndatenxport", "Periodenversatz im Lohndatenxport" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_LIEFERZEITVERSATZ, "14",
					"java.lang.Integer", "Default Lieferterminversatz in den Kopfdaten.",
					"Default Lieferterminversatz in den Kopfdaten. (-1 = Kein Vorschlag)" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG, "0", "java.lang.Boolean",
					"Default Teillieferung.", "Default Teillieferung." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Artikelgruppe statt Zusatzbez. in der Artikel-Auswahlliste anzeigen.",
					"Artikelgruppe statt Zusatzbezeichnung in der Artikel-Auswahlliste anzeigen. Wirkt nur wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_WIEDERHOLENDER_AUFTRAG_VERSTECKT, "0",
					"java.lang.Boolean", "Bei einer Neuanlage.",
					"Wenn ein wiederholender Auftrag neu angelegt wird ist dieser per Default versteckt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WARNUNG_WENN_LAGERPLATZ_BELEGT, "0",
					"java.lang.Boolean", "Warnung wenn Lagerplatz bereits durch einen Artikel belegt ist.",
					"Warnung wenn Lagerplatz bereits durch einen Artikel belegt ist." },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN, "1", "java.lang.Boolean",
					"Reverse Charge verwenden.", "Reverse Charge verwenden." },
			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_MENGEN_IN_STAFFELN_UEBERNEHMEN, "0",
					"java.lang.Boolean", "Mengen in EK-Staffeln \u00FCbernehmen.",
					"Anlieferdaten mit Mengen >1 werden in die EK-Mengenstaffeln \u00FCbernommen." },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_BUCHUNG_IMMER_NACHHER_EINFUEGEN, "0",
					"java.lang.Boolean", "Zeitbuchung immer nachher einf\u00FCgen.",
					"Zeitbuchung immer nachher einf\u00FCgen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Artikelgruppe in der Artikel-Auswahlliste anzeigen.",
					"Artikelgruppe in der Artikel-Auswahlliste anzeigen. Wirkt nur wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSAUSGABELISTE2_AUTOMATISCH_DRUCKEN, "0",
					"java.lang.Boolean", "Losausgabeliste2 bei Los-Ausgabe automatisch drucken.",
					"Losausgabeliste2 bei Los-Ausgabe automatisch drucken." },
			{ ParameterFac.KATEGORIE_REKLAMATION, ParameterFac.PARAMETER_KUNDENREKLAMATION_DEFAULT, "1",
					"java.lang.Integer", "1 = Fertigung / 2 = Lieferant", "1 = Fertigung / 2 = Lieferant" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN, "1",
					"java.lang.Integer", "M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen, 1 = erster Ansp. aus Lieferadresse, 2 = Ansp aus Auftragsadresse \u00FCbernehmen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ANZEIGE_IN_ARBEITSPLAN, "0", "java.lang.Integer",
					"0 = h - ms / 1 = t - m", "0 = h - ms / 1 = t - m" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG, "0",
					"java.lang.Boolean", "Default-Wert der Option -mit Zusammenfassung-",
					"Default-Wert der Option -mit Zusammenfassung- in AG + AB" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_KURZBEZEICHNUNG_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Kurzbezeichnung in der Artikel/Stueckliste/Los-Auswahlliste anzeigen.",
					"Kurzbezeichnung in der Artikel/Stueckliste/Los-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_NEUER_ARTIKEL_IST_LAGERBEWIRTSCHAFTET, "1",
					"java.lang.Boolean", "Ein neuer Artikel ist Lagerbewirtschaftet.",
					"Ein neuer Artikel ist Lagerbewirtschaftet." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_HERSTELLERKURZZEICHENAUTOMATIK, "1",
					"java.lang.Boolean", "Herstellerkurzzeichen wird automatisch vergeben.",
					"Herstellerkurzzeichen wird automatisch vergeben." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS, "0", "java.lang.Boolean",
					"'Ausgegeben'-Status vor 'In Produktion'.", "'Ausgegeben'-Status vor 'In Produktion'." },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT, "0",
					"java.lang.Integer", "0 = Abgeschaltet / 1 = Solid Works / 2 = Siemens NX / 3 = INFRA",
					"0 = Abgeschaltet / 1 = Solid Works / 2 = Siemens NX / 3 = INFRA" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAHNUNGEN_AB_GF_JAHR, "1900", "java.lang.Integer",
					"GF-Jahr ab dem gemahnt wird.", "GF-Jahr ab dem gemahnt wird." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VKPREISEINGABE_NUR_NETTO, "0",
					"java.lang.Boolean", "In den VK-Belegen kann nur der Netto-Preis eingegeben werden.",
					"In den VK-Belegen kann nur der Netto-Preis eingegeben werden." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_DEFAULT_UNVERBINDLICH, "0", "java.lang.Boolean",
					"Default-Wert f\u00FCr -unverbindlich- in den Auftragskopfdaten.",
					"Default-Wert f\u00FCr -unverbindlich- in den Auftragskopfdaten." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_ARTIKELBEZEICHNUNG_IN_LS_EINFRIEREN, "0",
					"java.lang.Boolean", "Bei LS-Erfassung auf -Sicht Auftrag- Artikelbezeichnungen einfrieren.",
					"Bei LS-Erfassung auf -Sicht Auftrag- Artikelbezeichnungen einfrieren." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ARTIKELLANGTEXTE_UEBERSTEUERBAR, "0",
					"java.lang.Boolean", "In den Bewegungsmodulen sind die Artikellangtexte \u00FCbersteuerbar.",
					"In den Bewegungsmodulen sind die Artikellangtexte \u00FCbersteuerbar." },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SOLLZEITPRUEFUNG, "1", "java.lang.Integer",
					"Sollzeitpr\u00FCfung in Zeitdaten",
					"Sollzeitpr\u00FCfung in Zeitdaten: 0=Aus, 1=Nur Lose, 2=Auftragspsition, 3=Auftrag gesamt" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_TOLERANZ_BETRAGSUCHE, "10", "java.lang.Integer",
					"Toleranz der Betragssuche im Buchungsjournal (in %).",
					"Toleranz der Betragssuche im Buchungsjournal (in %)." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_ARTIKELKLASSE_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Artikelklasse in der Artikel-Auswahlliste anzeigen.",
					"Artikelklasse in der Artikel-Auswahlliste anzeigen. Wirkt nur, wenn der Parameter ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE=0 und ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE=0 " },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DRUCKVORSCHAU_AUTOMATISCH_BEENDEN, "1",
					"java.lang.Boolean", "Druckvorschau nach Druck beenden", "Druckvorschau nach Druck beenden" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNE_BESTELLUNG_MIT_AUFTRAGSPOSITIONSBEZUG,
					"1", "java.lang.Boolean", "Lose aus interner Bestellung mit Auftragspositionsbezug.",
					"Lose aus interner Bestellung mit Auftragspositionsbezug." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN, "0",
					"java.lang.Boolean", "Beim Speichern einer SNR werden f\u00FChrende Nullen entfernt.",
					"Beim Speichern einer SNR werden f\u00FChrende Nullen entfernt." },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_PREISBASIS_VERKAUF, "0", "java.lang.Integer",
					"Werte 0 - 3",
					"0 = Verkaufspreisbasis  1 = Preisbasis lt. Kundenpreisliste (Es wird als Preisbasis der lt. Preisliste berechnete Betrag verwendet) 2 = Verkaufspreisbasis mit Mengenstaffelrabatt im Zusatzrabatt, 3=Wie 1 jedoch nur aktive Preislisten" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_AB_ERLAUBTEM_KOMMT, "0",
					"java.lang.Boolean", "Automatische Pausen ab -fr\u00FChestem erlaubtem Kommt-",
					"Automatische Pausen ab -fr\u00FChestem erlaubtem Kommt-" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION, "0",
					"java.lang.Integer", "0 = \u00FCbergeordnete St\u00FCckliste  1 = Fertigungssatzgr\u00F6\u00DFe",
					"0 = \u00FCbergeordnete St\u00FCckliste  1 = Fertigungssatzgr\u00F6\u00DFe" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_KUNDENAUSWAHL, "1", "java.lang.Integer",
					"1 = Alle - 2 = Kunde - 3 = Interessent", "1 = Alle - 2 = Kunde - 3 = Interessent" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_IMPORT_PLANKOSTEN_DATEI, "", "java.lang.String",
					"Dateiname zu XLS-Spreadsheet mit Plankostendaten",
					"Dateiname zu XLS-Spreadsheet mit Plankostendaten" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_LIEFERSPERRE_AB_MAHNSTUFE, "3",
					"java.lang.Integer", "Mahnstufe, ab der eine Liefersperre gesetzt wird",
					"Mahnstufe, ab der eine Liefersperre gesetzt wird" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LV_POSITION, "0", "java.lang.Boolean",
					"LV-Position in VK-Belegen anzeigen", "LV-Position in VK-Belegen anzeigen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FEHLMENGEN_LOSBEZOGEN_SOFORT_AUFLOESEN, "0",
					"java.lang.Boolean", "Losbezogene Fehlmengen sofort aufloesen",
					"Losbezogene Fehlmengen sofort aufloesen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SCHWERWIEGENDE_FEHLER_VERSENDEN, "1",
					"java.lang.Boolean", "Schwerwiegende Fehlermeldungen an HeliumV versenden",
					"Schwerwiegende Fehlermeldungen an HeliumV versenden" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZUSAETZLICHER_DOKUMENTENSPEICHERPFAD, " ",
					"java.lang.String", "Zus\u00E4tzlicher Dokumentenspeicherpfad",
					"Zus\u00E4tzlicher Dokumentenspeicherpfad" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ADRESSE_FUER_AUSDRUCK_MIT_ANREDE, "0",
					"java.lang.Boolean", "Anrede mitandrucken wenn keine Firma",
					"Anrede mitandrucken wenn keine Firma" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE, "0", "java.lang.Boolean",
					"Los-Ablieferung von Terminal bucht automatisch ein ENDE",
					"Los-Ablieferung von Terminal bucht automatisch ein ENDE" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DEFAULT_UEBERLIEFERBAR, "1",
					"java.lang.Boolean", "Default-Wert f\u00FCr die Option -\u00DCberlieferbar-",
					"Default-Wert f\u00FCr die Option -\u00DCberlieferbar-" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_TOLERANZ_SUCHE_NACH_BETRAG, "10",
					"java.lang.Integer", "Toleranz in Prozent, wenn nach Betraegen gefiltert wird.",
					"Toleranz in Prozent, wenn nach Betraegen gefiltert wird." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_HANDBUCHUNGSART, "0", "java.lang.Integer",
					"0=Zugang 1=Abgang 2=Umbuchung 3=Lagerstand",
					"Default-Wert f\u00FCr die Buchungsart in der Handlagerbewegung: 0=Zugang 1=Abgang 2=Umbuchung 3=Lagerstand" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FEHLMENGE_STATT_PREIS_IN_LOSMATERIAL, "0",
					"java.lang.Boolean", "Fehlmenge statt Preis in Los-Material anzeigen.",
					"Fehlmenge statt Preis in Los-Material anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_LAGERPLATZ_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Lagerplaetze in Artikelauswahl anzeigen.",
					"Lagerplaetze in Artikelauswahl anzeigen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_VERKNUEPFTE_BESTELLDETAILS_ANZEIGEN, "0",
					"java.lang.Boolean", "Verknuepfte Bestelldetails in Losmaterial anzeigen.",
					"Verknuepfte Bestelldetails in Losmaterial anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DAZUGEHOERT_BERUECKSICHTIGEN, "1",
					"java.lang.Integer", "Den -Zugehoerigen Artikel- in allen Belegen ber\u00FCcksichtigen.",
					"Den -Zugehoerigen Artikel- in allen Belegen ber\u00FCcksichtigen. 0 = Aus, 1 = mit R\u00FCckfrage, 2 = ohne R\u00FCckfrage." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_PREISUEBERWACHUNG, "0", "java.lang.Boolean",
					"Preis\u00FCberwachung in der Liste der Bestellpositionen.",
					"Preis\u00FCberwachung in der Liste der Bestellpositionen." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN, "0",
					"java.lang.Integer", "M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen 1 = erster Ansp. aus Rechnungsadresse 2 = Ansp aus Auftragsadresse \u00FCbernehmen" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Projekt-Nummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Projekt-Nummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_MONATSBESTELLUNGSART, "0", "java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = mit automatischem Wareneingang 1 = ohne automatischem Wareneingang 2 = Bestellungsimport" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ADRESSVORBELEGUNG, "0", "java.lang.Integer",
					"M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nach H\u00E4ufigkeit 1 = Auftragsadresse = Lieferadresse und Rechnungsadresse anhand Auftragsadresse, 2 = Lieferadresse anhand H\u00E4ufigkeit und Rechnungsadresse anhand Auftragsadresse" },
			{ ParameterFac.KATEGORIE_REKLAMATION, ParameterFac.PARAMETER_BESTELLUNG_UND_WARENEINGANG_SIND_PFLICHTFELDER,
					"1", "java.lang.Boolean", "Bestellung und Wareneingang sind Pflichtfelder.",
					"Bestellung und Wareneingang sind Pflichtfelder." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOSBUCHUNG_OHNE_POSITIONSBEZUG_BEI_STUECKRUECK,
					"0", "java.lang.Boolean",
					"Los-Position ist kein Pflichtfeld, wenn St\u00FCckr\u00FCckmeldung eingeschaltet",
					"Los-Position in Zeiterfassung ist kein Pflichtfeld, wenn St\u00FCckr\u00FCckmeldung eingeschaltet" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_AUFTRAGSBUCHUNG_OHNE_POSITIONSBEZUG, "0",
					"java.lang.Boolean", "Auftrags-Position ist in der Zeiterfassung kein Pflichtfeld",
					"Auftrags-Position ist in der Zeiterfassung kein Pflichtfeld" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_TRIGGERT_TRUMPF_TOPS_ABLIEFERUNG, "",
					"java.lang.String", "Arbeitszeitartikel f\u00FCr TOPS-Ablieferung in HTML-BDE (Artikelnummer)",
					"Arbeitszeitartikel f\u00FCr TOPS-Ablieferung in HTML-BDE (Artikelnummer)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL, "", "java.lang.String",
					"Werbeabgabeartikel (Artikelnummer)",
					"Artikelnummer, welche als Werbeabgabe in die Rechnung eingef\u00FCgt wird" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WERBEABGABE_PROZENT, "5", "java.lang.Integer",
					"Prozent- Wert der Werbeabgabe", "Prozent- Wert der Werbeabgabe" },

			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BESTELLUNG_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_LIEFERSCHEIN_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_ANFRAGE_ANSPRECHPARTNER_ANDRUCKEN, "1",
					"java.lang.Boolean", "Ansprechpartner im Adressblock andrucken",
					"Ansprechpartner im Adressblock andrucken" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_BEI_LOS_AKTUALISIERUNG_MATERIAL_NACHBUCHEN,
					"1", "java.lang.Boolean", "Bei Los- Aktualisierung fehlendes Material nachbuchen",
					"Bei Los- Aktualisierung aus St\u00FCckliste fehlendes Material nachbuchen" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_FRUEHZEITIGE_BESCHAFFUNG, "0",
					"java.lang.Boolean",
					"0 = Beginntermin als Eingabe, 1= Checkbox f\u00FCr fr\u00FChzeitige Beschaffung",
					"0 = Beginntermin als Eingabe, 1= Checkbox f\u00FCr fr\u00FChzeitige Beschaffung" },
			{ ParameterFac.KATEGORIE_REKLAMATION, ParameterFac.PARAMETER_REKLAMATION_BELEGNUMMERSTARTWERT, "1",
					"java.lang.Integer", "Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr.",
					"Startwert der Belegnummer im neuen Gesch\u00E4ftsjahr." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_KUNDESONDERKONDITIONEN_WEBSHOP_VERWENDEN, "0",
					"java.lang.Boolean", "0 = Kundensoko nicht an Webshop \u00FCbermitteln 1 = \u00FCbermitteln",
					"0 = Kundensoko nicht an Webshop \u00FCbermitteln 1 = \u00FCbermitteln" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG, "0",
					"java.lang.Boolean", "Empfangsbestaetigung im Bestellungsdruck",
					"Empfangsbestaetigung im Bestellungsdruck" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WERBEABGABE_VOM_EINZELPREIS, "0",
					"java.lang.Boolean", "Werbeabgabe vom Einzelpreis anstatt vom Nettopreis",
					"Werbeabgabe vom Einzelpreis anstatt vom Nettopreis" },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_KALKULATIONSART, "1",
					"java.lang.Integer", "1=Verkaufspreisbezug 2=Einkaufspreisbezug 3=Einkauf mit Aufschlag",
					"1=Verkaufspreisbezug 2=Einkaufspreisbezug 3=Einkauf mit Aufschlag" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR, "0",
					"java.lang.Boolean", "0 = RoHs wird angezeigt, 1 = Verrechenbar anstatt RoHs",
					"0 = RoHs wird angezeigt, 1 = Verrechenbar anstatt RoHs" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUPFERZAHL, "1.3", "java.lang.Double",
					"Kupferzahl f\u00FCr VK-Belege", "Kupferzahl f\u00FCr VK-Belege" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_LOSE_AKTUALISIEREN, "0", "java.lang.Integer",
					"Auswahlmoeglichkeit 0 / 1",
					"0 = nur Lose der gewaehlten Stueckliste aktualisieren, 1 = globale Aktualisierung aller Stuecklisten" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_PERSONALNUMMER_ZEICHENSATZ,
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", "java.lang.String",
					"Bestimmt die in einer Personalnummer verwendbaren Zeichen.",
					"Bestimmt die in einer Personalnummer verwendbaren Zeichen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN, "0",
					"java.lang.Boolean", "Nicht lagerbewirtschaftete Artikel sofort ausgeben.",
					"Nicht lagerbewirtschaftete Artikel sofort ausgeben, wenn -Materialbuchung bei Ablieferung- in der Stueckliste angehakt ist. Im Lagercockpit werden bei der Materialumlagerung die nicht lagerbewirtschafteten Artikel ein/ausgeblendet." },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG, "30",
					"java.lang.Double", "Default- Aufschlag in Prozent",
					"Default- Aufschlag in Prozent, wenn KALKULATIONSART = 3" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER, "0",
					"java.lang.Boolean", "Artikeltextsuche inklusive Artikelnummer",
					"Artikeltextsuche inklusive Artikelnummer" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION, "0",
					"java.lang.Boolean", "Artikeltextsuche inklusive Index/Revision",
					"Artikeltextsuche inklusive Index/Revision" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR, "0",
					"java.lang.Integer", "Lagerzubuchungsautomatik bei unzureichendem Lagerstand",
					"Lagerzubuchungsautomatik bei unzureichendem Lagerstand ( 0 - Aus, 1 - Einstandspreis kommt zuerst aus Gestehungspreis, 2 - Einstandspreis kommt zuerst aus Lief1Preis)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_STARTWERT_ARTIKELNUMMER, "", "java.lang.String",
					"Startwert, um eine Artikelnummer zu generieren",
					"Startwert, um eine Artikelnummer zu generieren" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KALENDERWOCHEN, "0",
					"java.lang.Double", "Anzahl der Kalenderwochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Kalenderwochen f\u00FCr die Lohnstundensatzkalkulation." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KRANKWOCHEN, "0",
					"java.lang.Double", "Anzahl der Krankwochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Krankwochen f\u00FCr die Lohnstundensatzkalkulation." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_FEIERTAGSWOCHEN, "0",
					"java.lang.Double", "Anzahl der Feiertagswochen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Feiertagswochen f\u00FCr die Lohnstundensatzkalkulation." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_SONDERZAHLUNGEN, "0",
					"java.lang.Double", "Anzahl der Sonderzahlungen f\u00FCr die Lohnstundensatzkalkulation.",
					"Anzahl der Sonderzahlungen f\u00FCr die Lohnstundensatzkalkulation." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN, "1",
					"java.lang.Integer", "Auftrag automatisch vollst\u00E4ndig erledigen",
					"Auftrag automatisch vollst\u00E4ndig erledigen. Wenn Einstellung = 0 k\u00F6nnen Auftr\u00E4ge nur manuell erledigt werden, wenn das Recht AUFT_DARF_AUFTRAG_ERLEDIGEN vorhanden ist. Wenn 2: Wie 1, nur m\u00FCssen die Auftragspositionen zus\u00E4tzlich vollst\u00E4ndig verrechnet sein." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "Das Projekt ist in den VK-Belegen ein Pflichtfeld.",
					"Das Projekt ist in den VK-Belegen ein Pflichtfeld." },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_KURZBEZEICHNUNG_STATT_ORT_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Kurzbezeichnung statt Ort in Projekt-Auswahl.",
					"In der Projekt-Auswahl wird anstatt des Ortes die Kurzbezeichnung des Partners angezeigt." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERKAUFSPREIS_RUECKPFLEGE, "0",
					"java.lang.Boolean", "VK-Preisbasis Rueckpflege im Auftrag.",
					"VK-Preisbasis Rueckpflege im Auftrag." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELNUMMER_AUSWAHL_ABSCHNEIDEN, "0",
					"java.lang.Integer", "Vorbesetzte Artikelnummer abschneiden.",
					"Vorbesetzte Artikelnummer bei der Artikelauswahl um eine best. Anzahl von Stellen abschneiden." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_LS_DEFAULT_VERRECHENBAR, "1",
					"java.lang.Boolean", "Default-Wert fuer -Verrechenbar- im Lieferschein.",
					"Default-Wert fuer -Verrechenbar- im Lieferschein." },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_LIEFERANT_ANGEBEN, "0", "java.lang.Boolean",
					"Lieferant in Angebots- und Auftragsposition angebbar",
					"Lieferant in Angebots- und Auftragsposition angebbar" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_VON_BIS_ERFASSUNG, "0", "java.lang.Boolean",
					"Belege koennen mit von-bis-Zeit erfasst werden",
					"Belege koennen mit von-bis-Zeit erfasst werden" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_INTERN_ERLEDIGT_BEBUCHBAR, "1",
					"java.lang.Boolean", "Zeiterfassung auf intern erledigte Projekte moeglich",
					"Zeiterfassung auf intern erledigte Projekte moeglich" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_URLAUBSANTRAG, "0", "java.lang.Boolean",
					"Urlaubsantrag mit Genehmigung", "Urlaubsantrag mit Genehmigung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_ZUSATZBEZ2_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Zusatzbezeichnung2 in der Artikel-Auswahlliste anzeigen.",
					"Zusatzbezeichnung2 in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_BUILD_ANZEIGEN, "0", "java.lang.Boolean",
					"Build-Nummer in Kopfdaten anzeigen.", "Build-Nummer in Kopfdaten anzeigen." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_WE, "2",
					"java.lang.Integer", "Anzahl der WE-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI",
					"Anzahl der WE-Nachkommastellen f\u00FCr Preis- und Rabattfelder im UI" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_POSITIONSREIHENFOLGE_AUS_AUFTRAG_ERHALTEN,
					"0", "java.lang.Boolean", "Positionsreihenfolge aus Auftrag erhalten",
					"Positionsreihenfolge aus Auftrag erhalten, wenn LS-Position aus -Sicht Auftrag- erzeugt wird" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_WEP_PREIS_ZURUECKSCHREIBEN, "0",
					"java.lang.Integer", "WEP-Preis automatisch in Artikellieferant zurueckschreiben.",
					"WEP-Preis automatisch in Artikellieferant zurueckschreiben. 0 = deaktiviert, 1 = als Einzelpreis zurueckschreiben" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_BESTELLVORSCHLAG_UEBERLEITUNG, "4",
					"java.lang.Integer", "Default-Vorschlag fuer die Ueberleitung des Bestellvorschlages",
					"1 = fuer jeden Lieferant + gleichen Termin eine Bestellung anlegen, 2 = je Lieferant eine Bestellung anlegen, 3 = ein Lieferant und ein Termin, 4 = ein Lieferant und alle Positionen des Lieferanten in einer Bestellung, 5 = Abruf zu vorhandenen Rahmen erzeugen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GENERIERE_ARTIKELNUMMER_ZIFFERNBLOCK, "0",
					"java.lang.Boolean", "Gibt an, welcher Ziffernblock beim generieren herangezogen wird",
					"0 = der Erste, 1 = der Letzte" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_URLAUBSTAGE_RUNDUNG, "0", "java.lang.Integer",
					"Rundung der Urlaubstage (im Report und im Uebertrag)",
					"0 = keine, 1 = kaufmaennisch, 2 = generell abrunden, 3 = generell aufrunden" },
			{ ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_SELEKTIONEN_MANDANTENABHAENGIG, "1",
					"java.lang.Boolean", "Selektionen sind mandantenabhaengig", "Selektionen sind mandantenabhaengig" },

			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN, "1",
					"java.lang.Boolean", "KOMMT-GEHT muss bei VON-BIS Zeiterfassung gebucht werden",
					"KOMMT-GEHT muss bei VON-BIS Zeiterfassung gebucht werden" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERSION_BEI_SNR_MITANGEBEN, "0",
					"java.lang.Boolean", "Version bei SNR-Lagerbuchung mitangeben",
					"Version bei SNR-Lagerbuchung mitangeben" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERSION_BEI_CHNR_MITANGEBEN, "0",
					"java.lang.Boolean", "Version bei CHNR-Lagerbuchung mitangeben",
					"Version bei CHNR-Lagerbuchung mitangeben" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_KOPIEN_RECHNUNG, "0", "java.lang.Integer",
					"Default Kopien Rechnung", "Default Kopien Rechnung" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_KOPIEN_LIEFERSCHEIN, "0",
					"java.lang.Integer", "Default Kopien Lieferschein", "Default Kopien Lieferschein" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ZUSATZBEZEICHNUNG_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Zusatzbezeichnung in Auswahlliste",
					"Zusatzbezeichnung in Auswahlliste (Wenn Parameter AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE=0)" },

			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_ANSPRECHPARTNER_VORBESETZEN, "1",
					"java.lang.Boolean", "Den Ansprechpartner nach Auswahl des Partners vorbesetzen.",
					"Den Ansprechpartner nach Auswahl des Partners vorbesetzen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH, "0", "java.lang.Boolean",
					"Seriennummern sind numerisch", "Seriennummern sind numerisch" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_GUTSTUNDEN_ZU_UESTD50_ADDIEREN, "1",
					"java.lang.Boolean", "Gutstunden in Monatsabrechung zu Uestd50Pflichtig addieren",
					"Gutstunden in Monatsabrechung zu Uestd50Pflichtig addieren" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INT_BEST_VERDICHTEN_RAHMENPRUEFUNG, "0",
					"java.lang.Boolean", "Interne Bestellung verdichten mit Rahmenpruefung",
					"Interne Bestellung verdichten mit Rahmenpruefung" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_REFERENZNUMMER_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Referenznummer in der Artikel-Auswahlliste anzeigen.",
					"Referenznummer in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ANZEIGEN_HERSTELLER_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Herstellernummer/bezeichnung in der Artikel-Auswahlliste anzeigen.",
					"Herstellernummer/bezeichnung in der Artikel-Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAENGE_CHARGENNUMMER_EINEINDEUTIG, "0",
					"java.lang.Integer", "Eineindeutige Chargennummern werden vorgeschlagen",
					"Wenn die Laenge > 0, dann werden in der Losablieferung eineindeutige CHNrs vorgeschlagen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSABLIEFERUNG_GESAMTE_ISTZEITEN_ZAEHLEN, "0",
					"java.lang.Boolean", "Fuer den Ablieferwert zaehlen nur die Zeiten bis zum Abliefzeitpunkt.",
					"Fuer den Ablieferwert zaehlen nur die Zeiten bis zum Abliefzeitpunkt." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AB_NACHKALKULATION_NUR_RECHNUNGS_ERLOESE, "0",
					"java.lang.Boolean", "Fuer AB-Nachkalkulation zaehlen nur Rechnungen.",
					"Fuer AB-Nachkalkulation zaehlen nur Rechnungen." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_NUR_VATERGRUPPEN_ANZEIGEN, "0",
					"java.lang.Integer", "In Artikel-Auswahllisten nur Vatergruppen anzeigen.",
					"In Artikel-Auswahllisten nur Vatergruppen anzeigen ( 0 - Alle Artikelgruppen anzeigen, 1 - Nur Vatergruppen anzeigen, 2 - Alle Artikelgruppen anzeigen, Kindgruppen sind eingerueckt)" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_NACH_CBEZ_ODER_CNR_ANZEIGEN, "0",
					"java.lang.Boolean", "Artikel-Auswahlliste nach Kennung sortieren.",
					"Artikel-Auswahlliste nach Kennung sortieren (0 - Sortiert nach Bezeichnung, 1 - Sortiert nach Kennung)." },

			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Kurzzeichen statt Name in Auswahlliste.",
					"Kurzzeichen statt Name in Auswahlliste." },

			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_WARENEINGANG_LAGERPLATZ_NUR_DEFINIERTE, "0",
					"java.lang.Boolean", "Beim Druck des WEP-Etiketts nur definierte Lagerplaetze auswaehlen.",
					"Beim Druck des WEP-Etiketts koennen nur mehr Lagerplaetze, welche im Artikel definiert sind, ausgewaehlt werden" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELSUCHE_MIT_HERSTELLER, "0",
					"java.lang.Boolean", "Textsuche inkl. Hersteller", "Textsuche inkl. Hersteller" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG, "0",
					"java.lang.Boolean", "Automatische Pausen nur Warnung anzeigen",
					"Automatische Pausen nur Warnung anzeigen, wenn zuwenig gebucht." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL, "0",
					"java.lang.Boolean", "Sammelbuchungen werden manuell durchgef\u00FChrt",
					"Sammelbuchungen werden manuell durchgef\u00FChrt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_INVENTUR_BASISPREIS, "0", "java.lang.Integer",
					"Inventurbasispreis kommt aus Gestehungspreis/EK-Preis",
					"Inventurbasispreis des Inventurstand kommt aus Gestehungspreis/EK-Preis: Gestehungspreis = 0, EK-Preis = 1" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER, "_",
					"java.lang.String", "Trennzeichen zwischen Artikelgruppe und Auftragsnummer",
					"Trennzeichen zwischen Artikelgruppe und Auftragsnummer. z.B. bei Auftragsschnellanlage" },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_EK_PREISBASIS, "1",
					"java.lang.Integer", "0=Lief1Preis, 1=Nettopreis", "0=Lief1Preis, 1=Nettopreis" },
			{ ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_AUS_BESTVOR_POSITIONEN_MIT_LOSZUORDNUNG, "1", "java.lang.Boolean",
					"Bestellvorschlag erzeugt Loszuordnungen", "Bestellvorschlag erzeugt Loszuordnungen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERSTAND_DES_ANDEREN_MANDANTEN_ANZEIGEN, "0",
					"java.lang.Boolean", "Lagerstand des anderen Mandanten anzeigen (in Auswahlliste)",
					"Lagerstand des anderen Mandanten anzeigen (in Auswahlliste)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOFERTIG_ABLIEFERUNG_TERMINAL, "0",
					"java.lang.Boolean", "Automatische Fertigbuchung Ablieferung per Terminal",
					"Automatische Fertigbuchung bei vollst\u00E4ndiger Ablieferung per Terminal" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_BELEGDRUCK_MIT_RABATT, "0",
					"java.lang.Boolean", "Default- Einstellung der Option -Belegdrucke mit Rabatt-",
					"Default- Einstellung der Option -Belegdrucke mit Rabatt-" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_KUNDESOKO_WIRKT_NICHT_IN_PREISFINDUNG, "0",
					"java.lang.Boolean", "Default- Einstellung der Option -wirkt nicht in Preisfindung-",
					"Default- Einstellung der Option -wirkt nicht in Preisfindung-" },
			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_AUTOMATISCHE_KREDITORENNUMMER, "0",
					"java.lang.Boolean", "Kreditorennummer bei Belegen anlegen, wenn nicht vorhanden.",
					"Kreditorennummer bei Belegen automatisch anlegen, wenn nicht vorhanden" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_EDITOR_BREITE_KOMMENTAR, "210",
					"java.lang.Integer", "Kommentarbreite", "Breite des Texteditors bei Kommentaren" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_EDITOR_BREITE_SONSTIGE, "520",
					"java.lang.Integer", "Standardbreite des Texteditors", "Standardbreite des Texteditors" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_EDITOR_BREITE_TEXTEINGABE, "470",
					"java.lang.Integer", "Standardbreite des Texteditors", "Breite des Texteditors bei Positionen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_EDITOR_BREITE_TEXTMODUL, "520",
					"java.lang.Integer", "Standardbreite des Texteditors", "Breite des Texteditors bei Textmodulen" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_DEBITORENNUMMER_FORTLAUFEND, "0",
					"java.lang.Boolean", "Debitorennummer fortlaufend",
					"0 = Anfangsbuchstabe des Partners wird f\u00fcr 2+3. Stelle verwendet 1 = fortlaufend gilt fuer Debitoren und Kreditoren" },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG, ParameterFac.PARAMETER_IMAPSERVER, "", "java.lang.String",
					"IMAP Server", "IMAP Server f\u00fcr Versandablage" },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG, ParameterFac.PARAMETER_IMAPSERVER_ADMIN, "", "java.lang.String",
					"IMAP Server Admin Konto", "IMAP Server Admin Konto f\u00FCr Versandablage." },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG, ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT, "",
					"java.lang.String", "IMAP Server Admin Konto Kennwort",
					"IMAP Server Kennwort des Admin Kontos f\u00FCr Versandablage." },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG, ParameterFac.PARAMETER_IMAPSERVER_SENTFOLDER, "",
					"java.lang.String", "IMAP Server Sent-Folder", "IMAP Server Ordner f\u00FCr gesendete Objekte." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT, "0",
					"java.lang.Boolean", "Lieferantenrechnungsnummer als Buchungstext bei ERs",
					"0 = Name des Lieferanteranten, 1 = Lieferantrechnungsnummer als Buchungstext" },
			{ ParameterFac.KATEGORIE_GUTSCHRIFT, ParameterFac.PARAMETER_GUTSCHRIFT_NENNT_SICH_RECHNUNGSKORREKTUR, "0",
					"java.lang.Boolean", "Gutschrift nennt sich Rechnungskorrektur",
					"Gutschrift nennt sich Rechnungskorrektur" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MINDEST_MAHNBETRAG, "5", "java.lang.Double",
					"Mindestmahnbetrag, ab der eine Sammelmahnung versendet wird.",
					"Mindestmahnbetrag, ab der eine Sammelmahnung versendet wird." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_BEWERTUNG_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Fortschritt in Auswahlliste anzeigen.",
					"Fortschritt in Auswahlliste anzeigen." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAEGE_KOENNEN_VERSTECKT_WERDEN, "0",
					"java.lang.Boolean", "Auftraege koennen versteckt werden.", "Auftraege koennen versteckt werden." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_UST_MONAT, "2", "java.lang.Integer",
					"Anzahl der Monate, um die die UST im vorhinein bezahlt werden muss.",
					"Anzahl der Monate, um die die UST im vorhinein bezahlt werden muss." },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_UST_STICHTAG, "15", "java.lang.Integer",
					"Stichtag an den die UST an das Finanzamt bezahlt werden muss.",
					"Stichtag an den die UST an das Finanzamt bezahlt werden muss. (Fuer Liquiditaetsvorschau)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER, "0", "java.lang.Boolean",
					"Lagermindest/Sollstand und Standort je Lager", "Lagermindest/Sollstand und Standort je Lager" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLASTUNGSZEITENBERECHNUNG, "0",
					"java.lang.Integer", "0 / 1 ",
					"0 = nur Sollzeiten von AGs die noch nicht Fertig sind / 1 = Sollzeiten x (1 - Erfuellungsgrad)" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLASTUNGSANZEIGEART, "0", "java.lang.Integer",
					"0", "0 = Maschine" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_DETAIL_AG, "0",
					"java.lang.Integer", "0 ... 1",
					"Darstellung der Arbeitsgaenge: 0 = Los u. Artikelnr / 1 = Projekt u Artikelkurzbezeichnung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_IMMER_VKPREISDIALOG_MENGENSTAFFEL_ANZEIGEN, "0",
					"java.lang.Boolean", "Immer VK-Preisdialog Mengenstaffel anzeigen",
					"Immer VK-Preisdialog Mengenstaffel anzeigen (auch bei Menge 1 bzw. wenn keine Staffeln definiert)" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_MIT_TAETIGKEIT, "0", "java.lang.Boolean",
					"Projektzeiterfassung mit zugeordneten Taetigkeiten",
					"Projektzeiterfassung mit zugeordneten Taetigkeiten" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_NACHKOMMASTELLEN_LOSGROESSE, "0",
					"java.lang.Integer", "Nachkommastellen der Losgroesse / Ablieferung",
					"Nachkommastellen der Losgroesse / Ablieferung" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_NACHKOMMASTELLEN_DIMENSIONEN, "2",
					"java.lang.Integer", "Nachkommastellen der Dimensionen", "Nachkommastellen der Dimensionen" },
			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_AUSGANGSGUTSCHRIFT_AN_KUNDE, "0",
					"java.lang.Boolean", "Ausgangsgutschrift an Kunde", "Ausgangsgutschrift an Kunde" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VERPACKUNGSMENGEN_EINGABE, "0",
					"java.lang.Integer", "Eingabe der Verpackungsmengen in den Belegpositionen",
					"Eingabe der Verpackungsmengen in den Belegpositionen ( 0 - Keine Verpackungsmengeneingabe, 1 - Verpackungsmenge kommt im VK aus Artikel-Sonstiges und im EK aus Artikellieferant, 2 - Verpackungsmenge kommt immer aus Artikel-Sonstiges)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_AG_BEGINN, "0",
					"java.lang.Integer", "Automatische Ermittlung des AG-Beginn (in Stunden)",
					"Automatische Ermittlung des AG-Beginn (in Stunden)  0 = Deaktiviert" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_FINDCHIPS_APIKEY, "", "java.lang.String",
					"Apikey der Findchips API", "Bitte geben Sie Ihren Apikey der Findchips API an" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_HTTP_PROXY, "", "java.lang.String",
					"HTTP Proxy host[:port]",
					"Bitte geben Sie den HTTP Proxy Host (sofern notwendig) ein. Optional kann auch der Port mittels \":portnummer\" angegeben werden" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_DEBITORENNUMMER_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Debitorennummer in Auswahlliste", "Debitorennummer in Auswahlliste" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERFUEGBARKEIT_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Verfuegbarkeit in Auswahlliste", "Verfuegbarkeit in Auswahlliste" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_AM_LS_GEWICHT_ANDRUCKEN, "0",
					"java.lang.Boolean", "Default-Wert in Kunden-Konditionen", "Default-Wert in Kunden-Konditionen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_KOSTENSTELLE_IN_VK_BELEGEN_VORBESETZT, "1",
					"java.lang.Boolean", "Kostenstelle in VK-Belegen vorbesetzt",
					"Kostenstelle in VK-Belegen vorbesetzt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_REKLAMATIONSWARNUNG_NUR_BEI_SNR_CHNR, "0",
					"java.lang.Boolean", "Reklamationswarnnung bei Artikelauswahl nur bei Snr/Chnr-Artikeln",
					"Reklamationswarnnung bei Artikelauswahl nur bei Snr/Chnr-Artikeln" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_WEP, "0",
					"java.lang.Integer", "Automatische Chargennummer bei Wareneingangsposition",
					"0 = Deaktiviert, 1 = Bestellnummer-WENr-Posnr, 2 = Tafelnummern" },
			{ ParameterFac.KATEGORIE_HYDRA, ParameterFac.PARAMETER_HYDRA_SERVER, "", "java.lang.String",
					"HTTP HvToHydraServer host[:port]",
					"Bitte geben Sie den HTTP Host des HvToHydraServers (sofern notwendig) ein. Optional kann auch der Port mittels \":portnummer\" angegeben werden" },
			{ ParameterFac.KATEGORIE_HYDRA, ParameterFac.PARAMETER_HYDRA_FERTIGUNGSGRUPPE, "", "java.lang.Integer",
					"Id der Hydra Fertigungsgruppe",
					"Bitte geben Sie die Id jener Fertigungsgruppe (aus der Datenbank, Tabelle STK_FERTIGUNGSGRUPPE) ein, deren Lose nach Hydra exportiert werden sollen." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_VON_BIS_VERSATZ, "15", "java.lang.Integer",
					"Von-Bis Versatz bei Von-Bis Zeiterfassung", "Von-Bis Versatz bei Von-Bis Zeiterfassung" },

			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_GUENSTIGER_MELDUNG_ANZEIGEN, "1",
					"java.lang.Boolean", "-Lieferant waere guenstiger- Meldung in Bestellung anzeigen",
					"-Lieferant waere guenstiger- Meldung in Bestellung anzeigen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VK_MENGENSTAFFEL_ANSTATT_SOKO_MENGESTAFFEL, "0",
					"java.lang.Boolean", "Bei Kunden-Soko greift die VK-Mengenstaffel anstatt der Soko-Mengenstaffel",
					"Wenn eine Kunden-Soko gefunden wird und eine passende VK-Mengenstaffel gefunden wird, wird der zusaetzliche Rabatt als Zusatzrabatt in den Belegpositionen angefuehrt. Die Soko-Mengenstaffel ist am Client nicht mehr sichtbar." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ETIKETTENDRUCK_NACH_SCHNELLANLAGE, "1",
					"java.lang.Integer", "0 = gar nicht - 1 = sofort nach Auftragsanlage - 2 = vor AG-Auswahl",
					"0 = gar nicht - 1 = sofort nach Auftragsanlage - 2 = vor AG-Auswahl" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_LIEFERAVISO_TAGE_ZUSAETZLICH, "0",
					"java.lang.Integer",
					"Anzahl der Tage die zus\u00e4tzlich zur Lieferdauer beim Kunden ben\u00f6tigt werden um den Eintrefftermin zu erreichen",
					"Anzahl der Tage die zus\u00e4tzlich zur Lieferdauer beim Kunden ben\u00f6tigt werden um den Eintrefftermin zu erreichen. Es k\u00f6nnen auch negative Werte verwendet werden, wenn beispielsweise die Lieferdauer zum Kunden Puffertage enth\u00e4lt." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ZUSAETZLICHES_LAGER_IN_AUSWAHLLISTE, "",
					"java.lang.String", "Zusaetzliches Lager in Auswahlliste",
					"Zusaetzliches Lager in Auswahlliste wenn das Lager mit der Kennung vorhanden ist." },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ERFASSUNGSFAKTOR_UI_NACHKOMMASTELLEN, "0",
					"java.lang.Integer",
					"Anzahl der Nachkommastellen f\u00FCr Erfassungsfaktor der St\u00FCckliste im UI",
					"Anzahl der Nachkommastellen f\u00FCr Erfassungsfaktor der St\u00FCckliste im UI" },

			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_VORNAME_UND_STRASSE_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Vorname und Strasse in Auswahlliste.",
					"Vorname und Strasse in Auswahlliste." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GUELTIGKEIT_MANDATSREFERENZ, "24",
					"java.lang.Integer", "Gueltigkeit der SEPA-Mandatsreferenz in Monaten",
					"Gueltigkeit der SEPA-Mandatsreferenz in Monaten" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_DEFAULT_FREIGABE, "1",
					"java.lang.Boolean", "Default f\u00FCr die Freigabe zur \u00DCberweisung im Zahlungsvorschlagslauf",
					"Default f\u00FCr die Freigabe zur \u00DCberweisung im Zahlungsvorschlagslauf. 1 = Eingangsrechnungen im Zahlungsvorschlag werden per Default zur \u00DCberweisung freigegeben" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_HANDLINGDAUER, "3", "java.lang.Integer",
					"Handlingdauer in Tagen bei mandanten\u00FCbergreifender Bestellung",
					"Handlingdauer in Tagen bei mandanten\u00FCbergreifender Bestellung" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUTOMATIKREGEL, "0", "java.lang.Integer",
					"0 = Aus - 1 = Rundung FSG/VPE + Auftragsfreigabe - 2 = ohne Rundung + Freigabe",
					"Autokmatikregel bei mandanten\u00FCbergreifender Bestellung" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUTOMATIK_POSITIONSSCHWELLWERT, "50",
					"java.lang.Integer", "Positionsschwellwert bei mandanten\u00FCbergreifender Bestellung",
					"Positionsschwellwert bei mandanten\u00FCbergreifender Bestellung" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MEHRERE_LOSE_AUSGEBEN_FERTIGUNGSPAPIERE_DRUCKEN,
					"0", "java.lang.Boolean", "Nach der Ausgabe mehrerer Lose Fertigungspapiere drucken",
					"Nach der Ausgabe mehrerer Lose Fertigungspapiere drucken" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_IMMER_CHARGE_FRAGEN, "0", "java.lang.Integer",
					"Immer nach einer Chargennummer fragen. Auch wenn nur eine Charge lagernd ist",
					"Immer nach einer Chargennummer fragen. Auch wenn nur eine Charge lagernd ist" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_EINLASTUNGRASTER, "60",
					"java.lang.Integer", "Alle <Minutenanzahl> Minuten wird ein Arbeitsgang platziert",
					"Alle <Minutenanzahl> Minuten wird ein Arbeitsgang platziert. 60 bedeutet also, dass"
							+ " zu jeder vollen Stunde der n\u00e4chste Arbeitsgang platziert wird. Etwaige Zeit"
							+ " dazwischen wird als Pufferzeit angesehen." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_PUFFERDAUER, "15",
					"java.lang.Integer", "Die kleinste <Minutenanzahl> zwischen zwei Arbeitsg\u00e4ngen",
					"Beim Platzieren der Arbeitsg\u00e4nge wird zuerst die Pufferdauer an den Vorg\u00e4nger"
							+ " angeh\u00e4ngt und dann der folgende Arbeitsgang am n\u00e4chsten Raster"
							+ " begonnen." },

			{ ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_DEFAULT_ANSPRECHPARTNER_DURCHWAHL, "1",
					"java.lang.Boolean", "Default-Wert der Einstellung -Durchwahl- im Ansprechpartner",
					"Default-Wert der Einstellung -Durchwahl- im Ansprechpartner" },

			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBURL, "", "java.lang.String",
					"DB-Url fuer ProFirst-Import", "DB-Url fuer ProFirst-Import" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBUSER, "", "java.lang.String",
					"DB-User fuer ProFirst-Import", "DB-User fuer ProFirst-Import" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBPASSWORD, "", "java.lang.String",
					"DB-Passwort fuer ProFirst-Import", "DB-Passwort fuer ProFirst-Import" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_ANZAHL_ZU_IMPORTIERENDE_DATENSAETZE,
					"1", "java.lang.Integer", "Anzahl zu importierende Datensaetze",
					"Anzahl zu importierende Datensaetze" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE, "0", "java.lang.Boolean",
					"Auftragsfreigabe", "Auftragsfreigabe" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_LEAD_IN_AZ_ARTIKEL, "AZ_",
					"java.lang.String", "Lead-IN fuer AZ-Artikel", "Lead-IN fuer AZ-Artikel" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PRO_FIRST_VERZEICHNIS, "", "java.lang.String",
					"ProFirst Verzeichnis", "ProFirst Verzeichnis" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DRUCK_BEWEGUNGSVORSCHAU_LAGER_ALLER_MANDANTEN, "0",
					"java.lang.Boolean", "Andruck der Lagerstaende aller Mandanten im Druck der Bewegungsvorschau",
					"Andruck der Lagerstaende aller Mandanten im Druck der Bewegungsvorschau, obwohl ZENTRALER_ARTIKELSTAMM und GETRENNTE_LAEGER" },

			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_BILD_PFAD, "", "java.lang.String",
					"Pfad zu den Bildern fuer Pro-First-Import",
					"Pfad zu den Bildern fuer Pro-First-Import aus Sicht des Servers. Achtung: Es werden .JPG Dateien erwartet" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL, "",
					"java.lang.String", "Fremdfertigungsartikel fuer Pro-First-Import",
					"Fremdfertigungsartikel fuer Pro-First-Import" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ZUGEHOERIGER_KUNDE_IN_BEWEGUNGSVORSCHAU_ANZEIGEN,
					"0", "java.lang.Boolean",
					"Zugehoerigen Kunden des Loses anstatt des Partners in Bewegungsvorschau anzeigen",
					"Zugehoerigen Kunden des Loses anstatt des Partners in Bewegungsvorschau anzeigen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GROSSE_DRUCKVORSCHAU_AKTIVIERT, "1",
					"java.lang.Boolean", "Die grosse Druckvorschau aktiviert und archiviert den Beleg",
					"Die grosse Druckvorschau aktiviert und archiviert den Beleg (Deaktivieren wird nicht empfohlen!)" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VP_ETIKETT_BASIS_IST_VERPACKUNGSMITTELMENGE, "0",
					"java.lang.Boolean", "Basis fuer Versandetikett ist die Verpackungsmittelmenge",
					"Basis fuer die Anzahl der Etiketten des LS-Versandetiketts ist die Verpackungsmittelmenge anstatt der Verpackungsmenge" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_OFFENE_AUFTRAGSPOSITIONEN_WERKTAGE_IN_ZUKUNFT, "3",
					"java.lang.Integer",
					"Web-Auswertung -offene Auftragspositionen- zum Stichtag Heute + Anzahl der Werktage in die Zukunft",
					"Web-Auswertung -offene Auftragspositionen- zum Stichtag Heute + Anzahl der Werktage in die Zukunft" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_LIEFERPLAN_TAGE_IN_ZUKUNFT, "18",
					"java.lang.Integer", "Anzahl der Tage die der Lieferplan in die Zukunft detailliert ausweist",
					"Anzahl der Tage die der Lieferplan in die Zukunft detailliert ausweist" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_MATERIALZUSCHLAG_STATT_NETTOPREIS_BS_MNG_EHT, "0",
					"java.lang.Boolean",
					"Im Artikellieferant wird anstatt des Nettopreises der Bestellmengeneinheit der Materialzuschlag angezeigt",
					"Im Artikellieferant wird anstatt des Nettopreises der Bestellmengeneinheit der Materialzuschlag angezeigt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_MATERIALKURS_AUF_BASIS_RECHNUNGSDATUM, "0",
					"java.lang.Boolean", "Der Materialkurs wird erst beim Aktivieren der Rechnung bestimmt",
					"Der Materialkurs wird erst beim Aktivieren der Rechnung bestimmt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_PRO_FIRST_ARTIKELGRUPPE, "", "java.lang.String",
					"Artikelgruppe fuer Pro-First-Import", "Artikelgruppe fuer Pro-First-Import" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMISSIONIERUNG_FERTIGUNGSGRUPPE, "",
					"java.lang.String", "Fertigungsgruppe f\u00FCr die Kommissionierung",
					"Fertigungsgruppe f\u00FCr die Kommissionierung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VK_PREIS_BASIS_MATERIAL, "", "java.lang.Boolean",
					"Kupferbasis = Notierung aus dem Artikelmaterial",
					"Kupferbasis = Notierung aus dem Artikelmaterial" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_PRO_FIRST_SCHACHTELPLAN_PFAD, "",
					"java.lang.String", "Pfad zu den Schachtelplaenen aus Pro-First",
					"Pfad zu den Schachtelplaenen aus Pro-First" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMISSIONIERUNG_AUSGESCHLOSSENE_ARTIKELGRUPPEN,
					"", "java.lang.String",
					"Artikelgruppen, die f\u00FCr die Kommissionierung nicht ber\u00FCcksichtigt werden",
					"Artikelgruppen, die f\u00FCr die Kommissionierung nicht ber\u00FCcksichtigt werden, getrennt durch \"\u007C\"" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_MINDESTBESTELLWERT, "0",
					"java.math.BigDecimal", "Mindest-Nettobestellwert aller Kunden. 0 = Aus",
					"Mindest-Nettobestellwert aller Kunden. 0 = Aus" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MINDESTBESTELLWERT_ARTIKEL, "",
					"java.lang.String", "Artikelnummer des Mindestbestellwert-Artikels",
					"Artikelnummer des Artikels, welcher eingefuegt wird, wenn der Mindestbestellwert nicht erreicht wird." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGEWICHT_GRAMM_STATT_KILO, "0",
					"java.lang.Boolean", "Artikelgewicht in Gramm statt Kilo (nur bei der Eingabe)",
					"Artikelgewicht in Gramm statt Kilo (nur bei der Eingabe)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_URSPRUNGSLAND_IST_PFLICHTFELD, "0",
					"java.lang.Boolean", "Das Ursprungsland ein Pflichtfeld.",
					"In den Artikelkopfdaten/Sonstiges ist das Ursprungsland ein Pflichtfeld." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITERFASSUNG_MIT_ZEITGUTSCHRIFT, "1",
					"java.lang.Boolean", "Zeiterfassung mit Zeitgutschrift.",
					"Sichtbarkeit des Reiters Zeitgutschrift in der Zeiterfassung und der Zeitgutschrift-Felder im Zeitmodell." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_LASTSCHRIFT_FAELLIGKEIT_NACH_ZAHLUNGSZIEL, "2",
					"java.lang.Integer",
					"Anzahl der Tage, die bei Berechnung des F\u00e4lligkeitsdatums einer Lastschrift zus\u00e4tzlich addiert werden.",
					"Anzahl der Tage, die bei Berechnung des F\u00e4lligkeitsdatums einer Lastschrift zus\u00e4tzlich addiert werden. Ausgangsdatum ist das F\u00e4lligkeitsdatums der betreffenden Rechnung" },

			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AG_AB_PROJEKT, "0",
					"java.lang.Boolean",
					"Der Projekttitel des zugehoerigen Projektes wird in der Spalte Projekt in AG und AB mitangezeigt.",
					"Der Projekttitel des zugehoerigen Projektes wird in der Spalte Projekt/Bestellnummer des Auftrages bzw. in der Spalte Projekt/Kundenanfrage Nr. des Angebotes mitangezeigt. Der Direktfilter beruecksichtigt den Projekttitel des zugehoerigen Projektes ebenfalls." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_WIEDERHOLENDE_AUFTRAEGE_ZUSAMMENFASSEN, "0",
					"java.lang.Boolean", "Wiederholende Auftraege zusammenfassen.",
					"Wiederholende Auftraege zusammenfassen, wenn Rechnungsadresse und Wiederholungsintervall gleich ist." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_HTML_SIGNATUR_MAILVERSAND, "",
					"java.lang.String", "HTML Signatur f\u00fcr E-Mail.",
					"Erfassen Sie hier jenen HTML-Tag den Sie f\u00fcr Ihre Signatur (Beispiel Bild) benutzen wollen. "
							+ "Links und Bilder m\u00fcssen vom Internet erreichbar sein. Ist dieser Parameter definiert, wird "
							+ "mail_signature.xsl anstatt mail.xsl verwendet." },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG_UNTERLOSE,
					"7", "java.lang.Integer", "Default-Vorlaufzeit fuer Unterlose in der internen Bestellung",
					"Default-Vorlaufzeit fuer Unterlose in der internen Bestellung bei der Neu-Erstellung einer internen Bestellung" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_EINRICHTELAGER_ERFASSUNGSTERMINAL, "",
					"java.lang.String", "Einrichtelager f\u00fcr das Erfassungsterminal",
					"Einrichtelager f\u00fcr das Erfassungsterminal, das f\u00fcr die Ablieferungen der Freigabemuster verwendet wird." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GS1_BASISNUMMER_GTIN, "", "java.lang.String",
					"GS1 Basisnummer f\u00fcr die Zusammenstellung der GTIN",
					"Tragen Sie hier die 7- oder 9-stellige GS1 Basisnummer f\u00fcr die Zusammenstellung der Global Trading Item Number (GTIN) ein" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GS1_BASISNUMMER_SSCC, "", "java.lang.String",
					"Erweiterungsziffer und GS1 Basisnummer f\u00fcr die Zusammenstellung der SSCC",
					"Tragen Sie hier 9-stellige Einleitung (XYYYYYYYY) des 18-stelligen Serial Ship Container Codes (SSCC) ein, "
							+ "wobei 'X' die Erweiterungsziffer und 'YYYYYYYYY' die GS1 Basisnummer darstellt." },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LIEF1INFO_IN_ARTIKELAUSWAHLLISTE, "0",
					"java.lang.Boolean", "Lief1-Infos in Artikelauswahlliste",
					"Lief1-Infos in Artikelauswahlliste = Kbez d. Lieferanten / Lief1Preis in Mandantenwaehrung / WBZ" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_CHARGEN_BEI_GENERIERUNG_NICHT_BERUECKSICHTIGEN, "",
					"java.lang.String", "Chargennummer mit den im Wert enthaltenen Zeichen nicht beruecksichtigen",
					"Chargennummer mit den im Wert enthaltenen Zeichen werden beim generieren einer neuen Chargnenummer nicht beruecksichtigt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSGABELISTE_TROTZDEM_DRUCKEN, "0",
					"java.lang.Boolean", "Ausgabeliste trotzdem drucken wenn FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL = 1",
					"Ausgabeliste trotzdem drucken wenn FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL = 1" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_ZUFALL, "0",
					"java.lang.Integer",
					"Anzahl der Stellen am Ende der Belegnummer, welche aus einer Zufallszahl bestehen.",
					"Anzahl der Stellen am Ende der Belegnummer, welche aus einer Zufallszahl bestehen (Bei AG/AB/LS/RE/GS)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KASSENIMPORT_LEERE_ARTIKELNUMMER_DEFAULT_ARTIKEL,
					"", "java.lang.String", "Default-Artikel f\u00fcr Kassenimport bei leeren Artikelnummern",
					"Tragen Sie hier die Artikelnummer ein, die dem Default-Artikel entspricht, der verwendet wird, "
							+ "wenn in einer Zeile des CSV-Kassenimport keine Artikelnummer eingetragen ist." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VK_STANDARD_MENGE, "1", "java.lang.Integer",
					"Default-Menge in den VK-Positionen beim Einfuegen neuer Artikel",
					"Default-Menge in den VK-Positionen beim Einfuegen neuer Artikel. 0 = Keine (Mengenfeld bleibt leer)" },
			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_KUNDENDATEN_VORBESETZEN,
					"1", "java.lang.Boolean", "Die Kundendaten der Eingangsrechnung vorbesetzen.",
					"Die Kundendaten der Eingangsrechnung werden mit der Kundennummer des Lieferanten vorbesetzt." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_RECHNUNG_POSITION_WERT0_PRUEFEN, "0",
					"java.lang.Boolean", "Sollen Rechnungspositionen auf Einzelpreis Wert 0 gepr\u00fcft werden?",
					"Sollen Rechnungspositionen auf Einzelpreis Wert 0 gepr\u00fcft werden? Ist die Pr\u00fcfung aktiv, werden beim Aktivieren der Rechnung Positionen mit Wert 0 ausgewiesen." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_URLAUBSABRECHNUNG_ZUM_EINTRITT, "0",
					"java.lang.Boolean", "Urlaubsabrechnung zum Eintrittsdatum anstatt zum Kalenderjahr",
					"Urlaubsabrechnung zum Eintrittsdatum anstatt zum Kalenderjahr" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_MONATSABRECHNUNG_NUR_AB_TAGESIST, "0",
					"java.lang.Double",
					"Monatsabrechnung nur anzeigen, wenn mindestens eine Tagesistsumme groesser als Parameterwert in Std.",
					"Monatsabrechnung nur anzeigen, wenn mindestens eine Tagesistsumme groesser als Parameterwert in Std. (0=Deaktiviert)" },
			{ ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_URL_ONLINE_KARTENDIENST,
					"https://www.openstreetmap.org/search?query=", "java.lang.String",
					"Url die verwendet wird um die Adresse zu suchen.",
					"Url die verwendet wird um die Adresse zu suchen, z.B.: 'https://www.openstreetmap.org/search?query=' oder 'https://www.google.com/maps/search/?api=1&query='" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_WARNUNG_WENN_KEIN_PROJEKT_ANGEGEBEN, "0",
					"java.lang.Boolean",
					"Warnung bei Auftrags-Neuanlage, wenn kein Projekt angegeben (Wenn PROJEKTKLAMMER)",
					"Warnung bei Auftrags-Neuanlage, wenn kein Projekt angegeben (Wenn PROJEKTKLAMMER)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_MATERIALBEDARFSVORSCHAU_OFFSET_REALISIERUNGSTERMIN,
					"3", "java.lang.Integer",
					"Materialbedarfsvorschau: Default Offset des Realisierungstermines bei Angeboten (in Monaten, ab Heute)",
					"Materialbedarfsvorschau: Default Offset des Realisierungstermines bei Angeboten (in Monaten, ab Heute)" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_AUFTRAG_AUS_POSITIONEN_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean",
					"Die niedrigste Auftragsnummer aus den Positionen in der Auswahlliste anzeigen",
					"Die niedrigste Auftragsnummer aus den Positionen in der Auswahlliste anzeigen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_MATERIAL_IN_AUSWAHLLISTE, "0", "java.lang.Boolean",
					"Kennung des Materials in Artikel-Auswahlliste", "Kennung des Materials in Artikel-Auswahlliste" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_AUFGELOESTE_FEHLMENGEN_DRUCKEN, "1",
					"java.lang.Boolean", "Aufgeloeste Fehlmengen beim Beenden einen Moduls drucken",
					"Aufgeloeste Fehlmengen beim Beenden einen Moduls drucken" },
			{ ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_GOOGLE_APIKEY, "", "java.lang.String",
					"Apikey der Google API", "Bitte geben Sie Ihren Apikey der Google API an" },
			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_4VENDING_ER_AR_IMPORT_BRUTTO_STATT_NETTO,
					"", "java.lang.Boolean", "Zahlbetr\u00e4ge als Brutto interpretieren",
					"Zahlbetr\u00e4ge einer 4Vending-Importdatei f\u00fcr Ausgangsgutschriften/Ausgangsrechnungen werden als Brutto- statt Nettobetr\u00e4ge interpretiert" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_LIEFERSCHEIN_POSITIONEN_MIT_RESTAPI_VERDICHTEN, "0", "java.lang.Boolean",
					"Position mit gleichem Artikel verdichten",
					"Soll bei der Neuanlage einer Lieferscheinposition \u00fcber die REST API gepr\u00fcft werden, ob bereits eine Position mit diesem Artikel vorhanden ist und diese verwendet werden?\n"
							+ "0 = Es soll immer eine neue Position angelegt werden\n"
							+ "1 = Es soll die vorhandene Positionen verwendet und die Menge addiert bzw. Serien-/Chargennr hinzugef\u00fcgt werden" },
			{ ParameterFac.KATEGORIE_REKLAMATION, ParameterFac.PARAMETER_LIEFERANTENREKLAMATION_BESTELLUNG_MANUELL, "0",
					"java.lang.Boolean",
					"In der Lieferantenreklamation Bestellnummer und Wareneingang manuell eingeben",
					"In der Lieferantenreklamation Bestellnummer und Wareneingang manuell eingeben" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENHINWEIS_AB_MAHNSTUFE, "0",
					"java.lang.Integer", "Hinweis bei der Kundenauswahl ab Mahnstufe (0= aus)",
					"Hinweis bei der Kundenauswahl ab Mahnstufe (0= aus)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_BEGRUENDUNG_BEI_VKPREISAENDERUNG, "0",
					"java.lang.Integer", "Begr\u00fcndung bei VK-Preis\u00e4nderung eingeben",
					"Beim Speichern eines VK-Preises muss eine Begr\u00fcndung angegeben werden: 1 = nur bei Aenderung , 2 = Bei Neuanlage und Aenderung" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSVERTRETER_AUS_ANGEBOT, "1",
					"java.lang.Boolean", "Regel der Vorbesetzung des Vertreters bei Auftrag aus Angebot",
					"Bei Auftrag aus Angebot: Wenn 0: Der Vertreter wird anhand des Parameters VERTRETER_VORSCHLAG_AUS_KUNDE gew\u00e4hlt, wenn 1: Der Vertreter kommt 1:1 aus dem Angebot." },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_RECHNUNGSEMPFAENGER_NUR_AUS_RECHNUNGS_EMAIL, "0",
					"java.lang.Boolean",
					"Empf\u00e4nger-Vorbelegung beim Email-Versand der Rechnung immer aus RE-Email des Kunden",
					"Empf\u00e4nger-Vorbelegung beim Email-Versand der Rechnung immer aus RE-Email des Kunden, auch wenn diese Leer ist" },
			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_ANGEBOTSABGABETERMIN_OFFSET, "10",
					"java.lang.Integer", "Angebotsabgabetermin Offset in Tagen",
					"Angebotsabgabetermin Offset in Tagen" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ANZAHL_STELLEN_LFD_NR_PRODUKTSTUECKLISTE, "4",
					"java.lang.Integer", "Anzahl Stellen lfd. Nr. Produktstueckliste",
					"Anzahl Stellen lfd. Nr. Produktstueckliste (Zusaetzlich zur Artikelnummernlaenge)" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_AUFSCHLAG1, "0", "java.lang.Double",
					"Aufschlag1 fuer Druck des Projekverlaufs", "Aufschlag1 fuer Druck des Projekverlaufs" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_AUFSCHLAG2, "0", "java.lang.Double",
					"Aufschlag2 fuer Druck des Projekverlaufs", "Aufschlag2 fuer Druck des Projekverlaufs" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REFERENZNUMMER_IN_INTERNER_BESTELLUNG, "0",
					"java.lang.Boolean", "Referenznummer in interner Bestellung",
					"Referenznummer in interner Bestellung" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSGABELISTE_MATERIAL_NULLMENGEN_ANDRUCKEN, "0",
					"java.lang.Boolean",
					"Im Fertigungsbegleitschein und in der Ausgabeliste Materialpositionen mit Menge 0 andrucken",
					"Im Fertigungsbegleitschein und in der Ausgabeliste Materialpositionen mit Menge 0 andrucken" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG, "0",
					"java.lang.Boolean", "Empfangsbestaetigung im E-Mail Versand des Rechnungsdrucks",
					"Empfangsbestaetigung im E-Mail Versand des Rechnungsdrucks" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_TOLERANZ_ZEITABWEICHUNG_TERMINAL, "10",
					"java.lang.Integer", "Toleranz der Zeitabweichung zum Terminal in Sekunden",
					"Toleranz der Zeitabweichung zum Terminal in Sekunden" },
			{ ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_NETTO_DIFFERENZ_ERLAUBT, "10.0",
					"java.math.BigDecimal", "Erlaubte Differenz zwischen Fibu-Nettobetrag und Rechnungsnettobetrag",
					"Erlaubte Differenz zwischen Fibu-Nettobetrag und Rechnungsnettobetrag. "
							+ "Diese Differenz entsteht beispielsweise dann, wenn "
							+ "f\u00fcr eine Artikelsetposition zwar ein "
							+ "Erl\u00f6skonto definiert ist, aber keine "
							+ "Verkaufspreisbasis. Dieser Parameter kommt nur "
							+ "zur Anwendung, wenn in der Rechnung kein " + "Allgemeiner Rabatt definiert ist." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERPLAETZE_BEI_LIEFERUNG_UND_LAGER_NULL_LOESCHEN,
					"0", "java.lang.Boolean", "Lagerplaetze l\u00f6schen, wenn der Lagerstand des Lagers 0 wird.",
					"Lagerplaetze des Artikels l\u00f6schen, wenn der Lagerstand des Lagers 0 aufgrund einer Lagerabbuchung 0 wird." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BCC_VERTRETER, "0", "java.lang.Boolean",
					"Provisionsempfaenger beim E-Mailversand als BCC vorbesetzen",
					"Provisionsempfaenger beim E-Mailversand als BCC in AG/AB/LS/RE/GS vorbesetzen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DIMENSIONEN_BESTELLEN, "0", "java.lang.Boolean",
					"Artikel mit Dimensionen > 0 mit Abmessungen bestellen",
					"Artikel mit Dimensionen > 0 mit Abmessungen bestellen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_GEWICHTSHINWEIS, "0", "java.lang.Integer",
					"Hinweis bei der Losanlage, wenn das Gesamtgewicht (in kg) einer Stueckliste x Losgroesse ueberschritten wird.",
					"Hinweis bei der Losanlage, wenn das Gesamtgewicht (in kg) einer Stueckliste x Losgroesse ueberschritten wird. 0 = Aus" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_BCC_RECHNUNG, "", "java.lang.String",
					"BCC Rechnungsversand an angegebene E-Mail-Adresse",
					"BCC Rechnungsversand an angegebene E-Mail-Adresse" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_BCC_GUTSCHRIFT, "", "java.lang.String",
					"BCC Gutschriftsversand an angegebene E-Mail-Adresse",
					"BCC Gutschriftsversand an angegebene E-Mail-Adresse" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERKAUFSPREIS_AUTOMATIK_LIEF1PREIS, "-1",
					"java.math.BigDecimal",
					"Wenn Lief1Preis geaendert wird, dann automatisch VK-Preisbasis anpassen (-1 = Aus)",
					"Wenn Lief1Preis geaendert wird, dann automatisch VK-Preisbasis anpassen (VK-Preisbasis = Lief1 Nettopreis + %-Wert des Parameters, -1 = Aus)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_EINZELARBEITSGANG_BUCHEN, "0",
					"java.lang.Boolean", "Am TimeTerminal wird nur der letzte offene Arbeitsgang angezeigt",
					"Am TimeTerminal wird nur der letzte offene Arbeitsgang angezeigt (bei St\u00fcckr\u00fcckmeldung)" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DEFAULT_MOBIL_DRUCKERNAME_SEITE, "",
					"java.lang.String",
					"Der Name des Druckers der bei mobilen Anwendungen (REST-API) f\u00fcr seitenweises Drucken verwendet werden soll",
					"Dieser Drucker wird f\u00fcr seitenweises Drucken immer dann verwendet, wenn kein spezifischer Name eines Druckers (durch die API) bekanntgegeben wurde. Der hier angegebene Drucker kann durch den gleichnamigen Arbeitsplatzparameter \u00fcbersteuert werden." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DEFAULT_MOBIL_DRUCKERNAME_ETIKETT, "",
					"java.lang.String",
					"Der Name des Druckers der bei mobilen Anwendungen (REST-API) f\u00fcr das Drucken von Etiketten verwendet werden soll",
					"Dieser Drucker wird f\u00fcr das Drucken von Etiketten immer dann verwendet, wenn kein spezifischer Name eines Druckers (durch die API) bekanntgegeben wurde. Der hier angegebene Drucker kann durch den gleichnamigen Arbeitsplatzparameter \u00fcbersteuert werden." },
			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANT_DEFAULT_GESPERRT, "0",
					"java.lang.Boolean", "Bei der Neuanlage eines Lieferanten wird eine Bestellsperre eingetragen",
					"Bei der Neuanlage eines Lieferanten wird eine Bestellsperre eingetragen" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_LOS_ARBEITSGANG_IST_PFLICHT, "0",
					"java.lang.Boolean", "Los-Arbeitsgang in den Zeitdaten ist ein Pflichtfeld",
					"Los-Arbeitsgang in den Zeitdaten ist ein Pflichtfeld" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ERSATZTYPEN_SOLLMENGE, "0", "java.lang.Integer",
					"Berechnung der Sollmenge der Ersatztypen",
					"0 = Ersatzartikel haben Menge 0, 1 = billigster Lief1Preis bekommt gesamte Sollmenge, 2 = Sollmenge wird anhand der Lagermengen der Ersatzartikel verteilt, 3 = Zuerst Original und dann Ersatzypen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSLIEFERLISTE_BEI_RAHMEN_NACH_ENDETERMIN, "0",
					"java.lang.Boolean",
					"Die Sortierung der Auslieferliste erfolgt bei einem Rahmenauftrag nach dem Los-Ende Termin",
					"Die Sortierung der Auslieferliste erfolgt bei einem Rahmenauftrag nach dem Los-Ende Termin anstatt dem Abliefertermin" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VK_PREIS_IMMER_AKTUALISIEREN, "0",
					"java.lang.Boolean",
					"In den VK-Positionen wird der VK-Preis and VK-Preisfindung immer ohne Preisdialog aktualisiert",
					"In den VK-Positionen wird der VK-Preis and VK-Preisfindung immer ohne Preisdialog aktualisiert" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MOBIL_DRUCKERNAME_BEDARFSUEBERNAHME, "",
					"java.lang.String",
					"Der Name des Druckers, der bei mobilen Anwendungen (REST-API) f\u00fcr das Drucken der Bedarfs\u00fcbernahme verwendet werden soll.",
					"Ist kein Druckername definiert, so wird f\u00fcr das Drucken der Bedarfs\u00fcbernahme bei mobilen Anwendungen (REST-API) der Druckername \u00fcber den Mandantenparameter DEFAULT_MOBIL_DRUCKERNAME_SEITE ermittelt." },

			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_AUSLIEFERDATUM_HEUTE, "0",
					"java.lang.Boolean", "Das Auslieferdatum des Lieferscheines wird beim Aktivieren auf Heute gesetzt",
					"Das Auslieferdatum des Lieferscheines wird beim Aktivieren auf Heute gesetzt" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNE_BESTELLUNG_ANGELEGTE_ENTFERNEN, "0",
					"java.lang.Boolean",
					"Bei einer neuen internen Bestellung werden alle Lose im Status Angelegt+Storniert komplett entfernt",
					"Bei einer neuen internen Bestellung werden alle Lose im Status Angelegt+Storniert komplett entfernt" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNE_BESTELLUNG_TERMINE_BELASSEN, "0",
					"java.lang.Boolean", "Los Beginn-Endeberechnung in der internen Bestellung",
					"0 -> Produktionsende ist Bedarfstermin und Produktionsbeginn ist Ende minus Durchlaufzeit (kann auch vor heute sein). 1 -> Produktionsende ist Bedarfstermin jedoch nicht vor heute und Produktionsbeginn ist Ende minus Durchlaufzeit, ebenfalls nicht vor heute" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BV_UEBERLEITUNG_MAXIMALE_ANZAHL_POSITIONEN,
					"250", "java.lang.Integer",
					"Maximale Anzahl an Positionen einer Bestellung beim Ueberleiten des Bestellvorschlages",
					"Maximale Anzahl an Positionen einer Bestellung beim Ueberleiten des Bestellvorschlages" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REIHENFOLGENPLANUNG, "0", "java.lang.Boolean",
					"Reihenfolgenplanung bei -Lose aus Auftrag-", "Reihenfolgenplanung bei -Lose aus Auftrag-" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ABRUFPOSITIONSREIHENFOLGE_WIE_ERFASST, "0",
					"java.lang.Boolean", "Abrufpositionsreihenfolge wie erfasst",
					"Abrufpositionsreihenfolge wie erfasst" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_EINMALARTIKEL_DEFAULT_IST_HANDARTIKEL, "0",
					"java.lang.Boolean", "Default im Dialog Einmalartikel",
					"Default im Dialog Einmalartikel 0 = Einmalartikel , 1= Handartikel" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_URLAUBSANSPRUCH_LINEAR_VERTEILT, "1",
					"java.lang.Boolean", "Urlaubsanspruch linear verteilt",
					"Urlaubsanspruch linear verteilt. 0 = Anspruch wird aus Zeitmodellen und Eintritt/Austritt zum Abrechnungsstichtag berechnet" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ARBEITSGANG_NUMMERIERUNG_BELASSEN, "0",
					"java.lang.Boolean",
					"Beim einf\u00fcgen aus der Zwischenablage Arbeitsgangnummer belassen (0= hinten anh\u00e4ngen, 1= AG-Nummer u\u00fcbernehmen)",
					"Beim einf\u00fcgen aus der Zwischenablage Arbeitsgangnummer belassen (0= hinten anh\u00e4ngen, 1= AG-Nummer u\u00fcbernehmen)" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_LIEFERZEIT_AUS_ANGEBOT, "0", "java.lang.Boolean",
					"Bei Auftrag aus Angebot wird der Liefertermin anhand der Lieferzeit aus den Angebot berechnet",
					"Bei Auftrag aus Angebot wird der Liefertermin anhand der Lieferzeit aus den Angebot berechnet (anhand Betriebskalender / ohne Samstag Sonntag)" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_PERSOENLICHER_BESTELLVORSCHLAG, "0",
					"java.lang.Boolean",
					"Bestell/Anfragevorschlag kann von mehreren Benutzern gleichzeitig erstellt werden",
					"Bestell/Anfragevorschlag kann von mehreren Benutzern gleichzeitig erstellt werden" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INDIREKTER_AUFTRAGSPOSITIONSBEZUG, "0",
					"java.lang.Boolean", "AB-Positionsbezug auch bei Unterlosen",
					"AB-Positionsbezug auch bei Unterlosen wenn Lose aus Auftrag erstellt werden" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DEFAULT_STKL_XLSIMPORT_POSITIONEN_LOESCHEN,
					"0", "java.lang.Boolean", "Default-Wert der Checkbox beim XLS-Import der Stkl-Positionen",
					"Default-Wert der Checkbox beim XLS-Import der Stkl-Positionen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_XLSIMPORT_ARTIKEL_UEBERSCHREIBEN,
					"0", "java.lang.Boolean", "Default-Wert der Checkbox beim XLS-Import der Artikel",
					"Default-Wert der Checkbox beim XLS-Import der Artikel" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_XLSIMPORT_EKPREISE_UEBERSCHREIBEN,
					"1", "java.lang.Boolean", "Default-Wert der Checkbox beim XLS-Import der Artikel",
					"Default-Wert der Checkbox beim XLS-Import der Artikel" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_XLSIMPORT_BEVORZUGTER_LIEFERANT,
					"1", "java.lang.Boolean", "Default-Wert der Checkbox beim XLS-Import der Artikel",
					"Default-Wert der Checkbox beim XLS-Import der Artikel" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_DEFAULT_INTERNEBESTELLUNG_LOESCHEN, "0",
					"java.lang.Boolean", "Default-Wert der Checkbox beim Erzeugen einer internen Bestellung",
					"Default-Wert der Checkbox beim Erzeugen einer internen Bestellung" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_REFERENZNUMMER_IN_POSITIONEN, "0",
					"java.lang.Boolean", "Referenznummer in Belegpositionen + Los + Stkl",
					"Referenznummer in Belegpositionen + Los + Stkl" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ZWEITER_VERTRETER, "0", "java.lang.Boolean",
					"Zweiter Vertreter in Angebotskopfdaten", "Zweiter Vertreter in Angebotskopfdaten" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_KOPFTEXT_UEBERNEHMEN, "0", "java.lang.Boolean",
					"Kopftext von AB - RE \\u00fcbernehmen", "Kopftext von AB - RE \\\\u00fcbernehmen" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ARBEITSGAENGE_BEI_HILFSSTUECKLISTEN_VERDICHTEN,
					"0", "java.lang.Integer", "Arbeitsplan bei Stkl-Ueberleitung in Los verdichten",
					"Arbeitsplan bei Stkl-Ueberleitung in Los verdichten. 0 = Aus, 1= Verdichten, 2 Verdichten anhand Artikelgruppe" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SCHNELLANLAGE_SUCHE_ZUERST_NACH_EAN, "0",
					"java.lang.Boolean", "Bei LS/RE-Pos Schnellanlage zuerst nach EAN suchen",
					"Bei LS/RE-Pos Schnellanlage zuerst nach EAN suchen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_RUESTMENGE_DARF_FEHLMENGE_BUCHEN, "0",
					"java.lang.Boolean", "R\u00fcstmenge darf Fehlmenge buchen",
					"R\u00fcstmenge darf Fehlmenge buchen" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ZIELMENGE_GLEICH_0_ZULAESSIG, "0",
					"java.lang.Boolean", "Die Zielmenge einer Materialposition darf 0 ergeben",
					"Die Zielmenge einer Materialposition darf 0 ergeben" },
			{ ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_POST_PLC_APIKEY, "0", "java.lang.String",
					"Die Webservice IDs der Post PLC Schnittstelle in der Reihenfolge ClientID;OrgUnitID;OrgUnitGUID;Url",
					"Die Webservice IDs der Post PLC Schnittstelle in der Reihenfolge ClientID;OrgUnitID;OrgUnitGUID:Url.\n"
							+ "Die Eintr\u00e4ge sind durch ; voneinander zu trennen. ClientID und OrgUnitID sind numerisch"
							+ " die OrgUnitGUID alphanumerisch.\n"
							+ "Beispiel: 2121;2275;c5ababababa-6fae-3cee-b999-72abcdeef999;https://https://plc.post.at/DataService/Post.Webservice/ShippingService.svc/secure" },
			{ ParameterFac.KATEGORIE_VERSANDAUFTRAG, ParameterFac.PARAMETER_MAIL_SERVICE_PARAMETER, "0",
					"java.lang.Integer", "Quelle der Mail-Service-Parameter.",
					"Aus welcher Quelle sollen die Mail-Service-Parameter bezogen werden:\n" + "0 = XML\n"
							+ "1 = Datenbank\n" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MATERIALWARNUNG_BEI_LOSABLIEFERUNG, "0",
					"java.lang.Boolean", "Materialwarnung bei der ersten Losablieferung",
					"Materialwarnung bei der ersten Losablieferung wenn sich seit der Losausgabe das Material der St\u00fcckliste ge\u00e4ndert hat." },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_RECHNUNGSADRESSE_IN_LIEFERSCHEINAUSWAHL, "0",
					"java.lang.Boolean", "Rechnungsadresse in Lieferscheinauswahl anzeigen",
					"Rechnungsadresse in Lieferscheinauswahl anzeigen" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_MINDERMENGENZUSCHLAG, "0",
					"java.lang.Boolean", "Default- Einstellung der Option Mindermengenzuschlag.",
					"Default- Einstellung der Option Mindermengenzuschlag in den Kunden- Konditionen" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_VORSTEUER_BEI_WE_EINSTANDSPREIS, "0",
					"java.lang.Boolean", "Beim Einstandspreis des Wareneingangs wird die Vorsteuer hinzugerechnet.",
					"Beim Einstandspreis des Wareneingangs wird die Vorsteuer hinzugerechnet" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_STAFFELPREIS_RUECKPFLEGEN, "0",
					"java.lang.Boolean", "Default Einstellung der CheckBox -Staffelpreis rueckpflegen-",
					"Default Einstellung der CheckBox -Staffelpreis rueckpflegen- in den Bestellpositionen" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_AZ_ARTIKEL_AUS_AUFTRAGSPOSITION, "0",
					"java.lang.Boolean", "AZ-Artikel kommt aus Auftragsposition",
					"In den Zeitdaten wird der AZ-Artikel anhand der Auftragsposition vorbesetzt" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG, "0",
					"java.lang.Boolean", "Verschnitt nach Abmessung",
					"Verschnitt nach Abmessung fuer Tafel- und Kantlberechnung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WEBABFRAGE_ARTIKELLIEFERANT_SUCHPARAMETER, "0",
					"java.lang.Integer",
					"Mit welchen Suchparametern soll die Webpreisabfrage eines Artikels im Artikellieferanten durchgef\u00fchrt werden.",
					"Artikel sollen \u00fcber die Webpreisabfrage im Artikellieferanten gesucht werden \u00fcber:\n"
							+ "0 = die Lieferantenartikelnummer und wenn nicht eindeutig gefunden, dann \u00fcber die Herstellernummer\n"
							+ "1 = die Lieferantenartikelnummer\n" + "2 = die Herstellernummer" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTPREIS_ANZEIGEN, "0", "java.lang.Boolean",
					"Istpreis in Los-Material anzeigen", "Istpreis in Los-Material anzeigen" },
			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_KOMMENTARE_IM_KOPF, "0", "java.lang.Boolean",
					"Kommentare in Kopfdaten anzeigen", "Kommentare in Kopfdaten anzeigen" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_LIEFERADRESSE_ANSP_VORBESETZEN, "1",
					"java.lang.Integer", "M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen, 1 = erster Ansp. aus Lieferadresse, 2 = Ansp aus Angebotsadresse \u00FCbernehmen" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_RECHNUNGSADRESSE_ANSP_VORBESETZEN, "0",
					"java.lang.Integer", "M\u00F6glichkeiten: 0 / 1 / 2",
					"0 = nicht vorbesetzen 1 = erster Ansp. aus Rechnungsadresse 2 = Ansp aus Angebotsadresse \u00FCbernehmen" },
			{ ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENAUSWAHL_STRUKTURIERT, "0",
					"java.lang.Boolean", "Kundenauswahllisten strukturiert", "Kundenauswahllisten strukturiert" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DASHBOARD_AKTUALISIEREN, "0",
					"java.lang.Integer", "Dashboard aktualisieren in Minuten", "Dashboard aktualisieren in Minuten" },

			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_LAENGE_BEZEICHNUNG, "40",
					"java.lang.Integer", "Max. L\u00E4nge Artikelbezeichnungen (bis 80 Zeichen)",
					"Max. L\u00E4nge Artikelbezeichnungen (bis 80 Zeichen)" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAXIMALER_NETTO_RECHNUNGSWERT, "-1",
					"java.lang.Integer", "Max. Rechnungswert einer Rechnung",
					"Max. Rechnungswert einer Rechnung beim -offene Lieferscheine verrechnen-" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_MONATSABRECHNUNGSVERSAND_PFAD_ANHAENGE, ".",
					"java.lang.String", "Pfad aus Server-Sicht fuer die Anh\u00E4nge beim Versand der Monatsabrechnung",
					"Pfad aus Server-Sicht fuer die Anh\u00E4nge beim Versand der Monatsabrechnung." },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_MONATSABRECHNUNGSVERSAND_PFAD_ANHAENGE_PREFIX,
					".", "java.lang.String", "Prefix der zusaetzlichen Anh\u00E4nge beim Versand der Monatsabrechnung",
					"Prefix der zusaetzlichen Anh\u00E4nge beim Versand der Monatsabrechnung" },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_VKPREIS_BEI_MENGENSTAFFEL_AUTOMATISCH,
					"0", "java.lang.Integer", "0=Aus  1=VK-Preis aus Agstkl  2=VK-Preis aus Preisfindung",
					"Preisvorschlag fuer VK-Preis gewaehlt in Mengenstaffel kommt aus: 0=Aus  1=VK-Preis aus Agstkl  2=VK-Preis aus Preisfindung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELKOMMENTAR_PDF_VORSCHAU_ANZEIGEN, "1",
					"java.lang.Boolean", "PDF-Vorschau in Artikelkommentar anzeigen",
					"PDF-Vorschau in Artikelkommentar anzeigen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_PDF_IN_ARTIKELDETAIL_ANZEIGEN, "0",
					"java.lang.Boolean", "PDF In Artikeldetail anzeigen",
					"PDF In Artikeldetail anzeigen (als zus\u00E4tzliche Bilder)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MATERIAL_BEI_ERSTEM_AG_NACHBUCHEN, "0",
					"java.lang.Boolean", "Material bei erstem AG nachbuchen",
					"Bei der ersten Zeitbuchung auf ein Los wird das komplette Material (ausser SNR/CHNR) auf das Los gebucht" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ABBUCHUNGSLAGER_IN_AUSWAHLLISTE, "0",
					"java.lang.Boolean", "Abbuchungslager in Auswahlliste anzeigen",
					"Abbuchungslager in Auswahlliste anzeigen" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ZWEITER_VERTRETER, "0", "java.lang.Boolean",
					"Zweiter Vertreter in Auftragskopfdaten", "Zweiter Vertreter in Auftragskopfdaten" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGS_VAT_IMPORT, "0", "java.lang.Boolean",
					"VAT-Import im Auftrag", "VAT-Import im Auftrag" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELSUCHE_OHNE, "", "java.lang.String",
					"Artikelsuche ohne Zeichenfolge im Parameter-Wert",
					"Artikelsuche ohne Zeichenfolge im Parameter-Wert" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_UMSATZUEBERSICHT_GESCHAEFTSJAHR, "0",
					"java.lang.Boolean", "Umsatz\u00FCbersicht nach Gesch\u00E4ftsjahr anstatt Kalenderjahr",
					"Umsatz\u00FCbersicht nach Gesch\u00E4ftsjahr anstatt Kalenderjahr" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ABLIEFERETIKETT_BEI_EXTERNEM_AG_DRUCKEN, "1",
					"java.lang.Boolean", "Ablieferetikett bei externem AG drucken",
					"Ablieferetikett bei externem AG und Fehlmengenaufl\u00f6sung drucken" },
			{ ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_NACHFASSTERMIN_AUS_PROJEKT_AKTUALISIEREN, "0",
					"java.lang.Boolean", "Nachfasstermin aus Projekt aktualisieren",
					"Nachfasstermin des Angbots aus Projekt aktualisieren" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AF_BS_LS_RE_LOS, "0",
					"java.lang.Boolean",
					"Der Projekttitel des zugehoerigen Projektes wird in der Spalte Projekt mitangezeigt.",
					"Der Projekttitel des zugehoerigen Projektes wird in der Spalte Projekt in AF/BS/LS/RE/LOS angezeigt." },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MAXIMALLAENGE_INTERNE_TELEFONNUMMER, "4",
					"java.lang.Integer", "Maximall\u00E4nge interne Telefonnummer",
					"Maximall\u00E4nge interne Telefonnummer" },
			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_AUS_QR_CODE, "0",
					"java.lang.Boolean", "Eingangsrechnung aus QR-Code erzeugen",
					"Eingangsrechnung aus QR-Code erzeugen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ABLIEFERTERMIN_IN_LOS_AUSWAHL, "0",
					"java.lang.Boolean", "Abliefertermin in Los-Auswahlliste", "Abliefertermin in Los-Auswahlliste" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_DEFAULT_ARTIKELAUSWAHL, "1",
					"java.lang.Boolean", "0 = lagernde Artikel 1 = Alle.", "0 = lagernde Artikel 1 = Alle." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_KOMMENTAR_HAT_VORRANG_IM_ARTIKELDETAIL, "0",
					"java.lang.Boolean", "Artikelkommentar im Detail hat Vorrang vor den Ersatztypen",
					"Artikelkommentar im Detail hat Vorrang vor den Ersatztypen" },

			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_LF_LAGER_LIEFERZEIT_IN_KW, "2",
					"java.lang.Integer", "Lieferzeit wenn Lagerstand bei Lieferant ausreichend",
					"Lieferzeit wenn Lagerstand bei Lieferant ausreichend" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELSUCHE_MIT_REFERENZNUMMER, "0",
					"java.lang.Boolean", "Artikelnummernsuche incl. Referenznummer",
					"Artikelnummernsuche incl. Referenznummer" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_SERIENNUMMER_BEGINNT_MIT, "", "java.lang.String",
					"Firmenseriennummer generieren",
					"Unternehmensweite Seriennummer eindeutig \u00FCber alle Artikel, beginnend mit -Parameterwert- generieren" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_KOMMENTAR_BEI_ARTIKELFREIGABE_AENDERBAR, "1",
					"java.lang.Boolean", "Kommentar darf bearbeitet werden wenn Artikel freigegeben",
					"Kommentar darf bearbeitet werden wenn Artikel freigegeben (Achtung: Wenn = 0 dann gilt das Benutzerrecht WW_ARTIKELKOMMENTAR_FREIGEGEBENER_ARTIKEL_CUD" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_DATEIVERWEIS, "0", "java.lang.Boolean",
					"Default der CheckBox Dateiverweis", "Default der CheckBox Dateiverweis" },

			{ ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_WEP_INFO, "0", "java.lang.Boolean",
					"Default der CheckBox WEP-Info in den Bestellpositionen",
					"Default der CheckBox WEP-Info in den Bestellpositionen" },
			{ ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_LAGERINFO_IN_POSITIONEN, "0",
					"java.lang.Boolean", "Lagerinfo in den Materialpositionen",
					"Lagerinfo in den Materialpositionen (Lagerstand/Verfuegbar/Bestellt)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAGERINFO_IN_POSITIONEN, "0",
					"java.lang.Boolean", "Lagerinfo in den Materialpositionen",
					"Lagerinfo in den Materialpositionen (Lagerstand/Verfuegbar/Bestellt)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAGERINFO_IN_LOSPOSITIONEN_LAGERSTAND_KNAPP,
					"10", "java.lang.Integer", "Materialposition in Los ist knapp",
					"Materialposition in Los (Zeile ist Orange) ist knapp wenn Lagerstand < (Fehlmenge + PARAMETERWERT)" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSPOSITIONSTERMIN_IN_AUSWAHL_ANZEIGEN, "0",
					"java.lang.Boolean", "Naechster faelliger Positionstermin in Auswahl anzeigen",
					"Naechster faelliger Positionstermin in Auswahl anzeigen" },
			{ ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_POSITIONSTEXT_AUS_STKL_IN_LOSSOLLMATERIAL_ANZEIGEN, "0", "java.lang.Boolean",
					"Positionstext aus Stueckliste in Los-Sollmaterial anzeigen",
					"Positionstext aus Stueckliste in Los-Sollmaterial anzeigen" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_VORHERIGE_ZEITBUCHUNG_LOESCHEN, "0",
					"java.lang.Integer", "vorherige Zeitbuchung innerhalb Anzahl Sekunden l\u00f6schen",
					"vorherige KOMMT/GEHT/UNTER-Buchung bei Buchung von KOMMT/GEHT/UNTER am Terminal innerhalb Anzahl von Sekunden l\u00f6schen" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DEFAULT_SNR_DIALOG_KOMMA, "1",
					"java.lang.Boolean", "Default der CheckBox Komma im Seriennummerndialog",
					"Default der CheckBox Komma im Seriennummerndialog" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_LOSABLIEFERUNG,
					"0", "java.lang.Integer", "Automatische Chargennummer bei Losablieferung",
					"0 = Deaktiviert, 1 = Losnummmer-Abliefernummmer, 2 = hierarchische Chargennummern" },
			{ ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_BEREITS_ANGEFRAGT, "90", "java.lang.Integer",
					"Anzahl der Anfragen",
					"Anzahl der Anfragen zu einen Artikel in den letzten x Tagen im Anfragevorschlag" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITVERTEILUNG_TYP, "0", "java.lang.Integer",
					"Zeitverteilungsmethode",
					"0= Anhand Losgr\u00f6ssen, 1= Stueckzeit x Gut+Schlechtst\u00FCck, 2= wie 1, jeodch wird zus\u00E4tzlich R\u00FCsten ber\u00FCcksichtigt" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITVERTEILUNG_SOLLZEIT_WENN_0, "60",
					"java.lang.Integer", "Angenommene Sollzeit wenn Sollzeit 0 in Minuten",
					"Angenommene Sollzeit wenn Sollzeit 0 in Minuten" },
			{ ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_ZEITBUCHUNG_AG_AB_GEMEINSAM, "0",
					"java.lang.Boolean", "Gemeinsame Auswahl von AG/AB in Zeiterfassung",
					"Gemeinsame Auswahl von AG/AB in Zeiterfassung" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_RASTERLIEGEND_NACHKOMMASTELLEN, "2",
					"java.lang.Integer", "Anzahl der Nachkommastellen f\u00FCr Raster liegend in der Artikeltechnik",
					"Anzahl der Nachkommastellen f\u00FCr Raster liegend in der Artikeltechnik" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DIMENSIONSERFASSUNG_VK, "0", "java.lang.Boolean",
					"Dimensionserfassung in den VK-Positionen", "Dimensionserfassung in den VK-Positionen" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_VKPREISPFLICHTIG, "1", "java.lang.Boolean",
					"Default-Wert der CheckBox VK-Preispflichtig", "Default-Wert der CheckBox VK-Preispflichtig" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERPACKUNGSKOSTEN_ARTIKEL, "", "java.lang.String",
					"Verpackungskostenartikel (Artikelnummer)",
					"Artikelnummer, welche als Verpackungskostenartikel in AG/AB/RE eingef\u00FCgt wird" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAENGE_HIERARCHISCHE_CHARGENNUMMER, "6",
					"java.lang.Integer", "L\u00E4nge der hierarchischen Chargennummer",
					"L\u00E4nge der hierarchischen Chargennummer" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOPIEN_VERSANDETIKETT_ABLIEFERUNG, "0",
					"java.lang.Integer", "Default-Wert der Kopien im Versandetikett Ablieferung",
					"Default-Wert der Kopien im Versandetikett Ablieferung" },
			{ ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_SICHT_AUFTRAG_MEHRFACHAUSWAHL, "1",
					"java.lang.Boolean", "Abfrage ob alle oder nur markierte Positionen in Sicht Auftrag",
					"Abfrage ob alle oder nur markierte Positionen in Sicht Auftrag" },
			{ ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_SICHT_AUFTRAG_MEHRFACHAUSWAHL, "1",
					"java.lang.Boolean", "Abfrage ob alle oder nur markierte Positionen in Sicht Auftrag",
					"Abfrage ob alle oder nur markierte Positionen in Sicht Auftrag" },
			{ ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					ParameterFac.PARAMETER_PREIS_AUS_GESAMTKALKULATION_WENN_STUECKLISTE, "0", "java.lang.Boolean",
					"Preis kommt aus Stkl-Gesamtkalkulation wenn kein VK-Preis hinterlegt",
					"Preis kommt aus Stkl-Gesamtkalkulation wenn kein VK-Preis hinterlegt (Wenn der Artikel eine St\u00FCckliste ist)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_TECHNIK_BEI_ARTIKELFREIGABE_AENDERBAR, "0",
					"java.lang.Boolean", "Artikel - Technik trotz Artikelfreigabe \u00E4nderbar",
					"Artikel - Technik trotz Artikelfreigabe \u00E4nderbar" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GETRENNTE_EINKAUFSPREISE, "0", "java.lang.Boolean",
					"Getrennte Einkaufspreise trotz ZENTRALER_ARTIKELSTAMM",
					"Getrennte Einkaufspreise trotz ZENTRALER_ARTIKELSTAMM" },

			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_STUECKRUECKMELDUNG_BENOETIGT_ZEITERFASSUNG, "1",
					"java.lang.Boolean", "St\u00fceckr\u00fcckmeldung (Los Gut/Schlecht) ben\u00f6tigt Zeitbuchung",
					"St\u00fceckr\u00fcckmeldung (Los Gut/Schlecht) ben\u00f6tigt eine zugeordnete Zeitbuchung." },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAG_SERIENNUMMERN, "0", "java.lang.Boolean",
					"Seriennummmern bereits im Auftrag", "Seriennummmern bereits im Auftrag" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VERSANDKOSTENARTIKEL, "", "java.lang.String",
					"Versandkostenartikel (Artikelnummer)",
					"Artikelnummer, welcher als Versandkostenartikel bei Shop-Importen verwendet wird" },

			{ ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN, "0",
					"java.lang.Integer", "Eingangsrechnungen m\u00fcssen vor Freigabe gepr\u00fcft werden",
					"Eingangsrechnungen m\u00fcssen vor Freigabe gepr\u00fcft werden" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE, "0",
					"java.lang.Boolean",
					"Automatische Ermittlung des AG-Beginn und des Produktionsendes anhand des Firmenzeitmodells",
					"Automatische Ermittlung des AG-Beginn und des Produktionsendes anhand des Firmenzeitmodells (hat Vorrang gegenueber AUTOMATISCHE_ERMITTLUNG_AG_BEGINN)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_MATERIAL_ANHAND_GLEICHER_ARTIKELGRUPPE_BUCHEN,
					"0", "java.lang.Boolean",
					"Bei der erstmaligen Zeitbuchung auf einen Los-AG wird das Material der gleichen Artikelgruppe aufs Los gebucht",
					"Bei der erstmaligen Zeitbuchung auf einen Los-AG wird das Material der gleichen Artikelgruppe aufs Los gebucht" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_BEGINNTERMINOFFSET, "0",
					"java.lang.Boolean",
					"Der Beginnterminoffset des Losmateriales wird anhand der Artikelgruppe des Arbeitsganges ermittelt.",
					"Der Beginnterminoffset des Losmateriales wird anhand der Artikelgruppe des Arbeitsganges ermittelt." },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_WIEDERBESCHAFFUNGSZEIT, "2",
					"java.lang.Integer", "Default Wiederbeschaffungszeit in Tagen bzw. Kalenderwochen.",
					"Default Wiederbeschaffungszeit in Tagen bzw. Kalenderwochen. (Je nach Parameter ARTIKELWIEDERBESCHAFFUNGSZEIT)" },
			{ ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_HISTORISCH, "",
					"java.lang.String", "Fr\u00fchere Belegnummerformate",
					"Angabe des Patterns von Belegnummernformaten, die fr\u00fcher verwendet wurden.\n"
							+ "Dabei kennzeichnet ''J'' die Stellen des Gesch\u00E4ftsjahres und ''#'' die Stellen der laufenden Nummer. Das verwendete Trennzeichen (ev. mit Mandantenkennzeichnung) wird 1:1 abgebildet.\n"
							+ "Beispiele: JJJJ/##### (f\u00fcr 2022/00001), JJ_1###### (f\u00fcr 22_1000001)" },
			{ ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LIEF1PREIS_GUELTIG_IN_MONATEN_FUER_REPORT, "",
					"java.lang.Integer",
					"Wert wird an AG-Vorkalkulation und Stkl-Gesamtkalkulation \u00fcbergeben (P_LIEF1PREIS_GUELTIG_IN_MONATEN)",
					"Wert wird an AG-Vorkalkulation und Stkl-Gesamtkalkulation \u00fcbergeben (P_LIEF1PREIS_GUELTIG_IN_MONATEN)" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_VORHERIGE_ARBEITSGAENGE_MITERLEDIGEN, "0",
					"java.lang.Boolean", "Die vorherigen Arbeitsg\u00E4nge werden bei -Fertig- ebenfalls auf -Fertig- gesetzt.",
					"Die vorherigen Arbeitsg\u00E4nge werden bei -Fertig- ebenfalls auf -Fertig- gesetzt." },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_TRUTOPS_BOOST_SERVER, "",
						"java.lang.String", "HTTP HvToTruTopsBoost host[:port]",
						"Bitte geben Sie den HTTP Host des HvToTruTopsBoostServers (sofern notwendig) ein. Optional kann auch der Port mittels \":portnummer\" angegeben werden" },
			{ ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_WOOCOMMERCE_PARTNER_ZUSAMMENZIEHEN, "0", "java.lang.Boolean",
							"Vorname + Nachname wird in PartnerName1 zusammengezogen (wenn keine Firma)", "Vorname + Nachname wird in PartnerName1 zusammengezogen (wenn keine Firma)" },
			{ ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_INITIALKOSTEN_ARTIKEL, "", "java.lang.String",
								"Initialkosten (Artikelnummer)",
								"Artikelnummer, welche als Initialkosten in das Angebot eingef\u00FCgt wird" },
			{ ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FERTIGUNGSPAPIERE_OHNE_NACHFRAGE_DRUCKEN, "0",
									"java.lang.Boolean", "Fertigungspapiere ohne Nachfrage drucken","Fertigungspapiere ohne Nachfrage drucken" },
							
			// parametermandant: 1 fix verdrahteten Wert einfuegen, Zeilensetzung wie
			// oben, damit sie bei Formatierung nicht verloren geht...
	};

	// parametermandant: 2 UpdateXX des DBManagers entsprechend erweitern
	private ParametermandantDto[] aFixverdrahteteParametermandantDtos = null;

	/**
	 * Berechnet das aktuelle Geschaeftsjahr (4-stellig) anhand der Einstellung in
	 * den Firmenparametern.
	 * 
	 * @param mandantCNr String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer getGeschaeftsjahr(String mandantCNr) throws EJBExceptionLP {
		return getGeschaeftsjahr(mandantCNr, super.getDate());
	}

	/**
	 * Berechnet das Geschaeftsjahr (4-stellig) fuer ein beliebiges Datum anhand der
	 * Einstellung in den Firmenparametern
	 * 
	 * @return Integer z.B. 2004
	 * @param mandantCNr  String
	 * @param dBelegdatum Date
	 * @throws EJBExceptionLP
	 */
	public Integer getGeschaeftsjahr(String mandantCNr, java.util.Date dBelegdatum) throws EJBExceptionLP {

		int beginnMonat;
		boolean plusEins;

		try {
			// -1 wegen Jaenner in DB == 1, in Calendar == 0
			beginnMonat = getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT).asInteger() - 1;
			plusEins = getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS).asBoolean();
			// pmBeginnMonat = getMandantparameter(mandantCNr,
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
			// pmPlusEins = getMandantparameter(mandantCNr,
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS);
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, ex);
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
	 * @param belegDatum  ist das Belegdatum
	 * @param beginnMonat ist der Monat (0 basierend!) in dem das neue GJ beginnt
	 * @param plusEins    ist true wenn es sich um ein Folgejahr handelt
	 * @return das Geschaeftsjahr
	 */
	public Integer calculateGeschaeftsjahr(Calendar belegDatum, int beginnMonat, boolean plusEins) {
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

	private ParametermandantPK createParametermandant(ParametermandantDto parametermandantDtoI,
			TheClientDto theClientDto, boolean bReload) throws EJBExceptionLP {
		myLogger.entry();
		checkParametermandantDto(parametermandantDtoI);

		ParametermandantPK pkParametermandant = null;

		try {
			Parametermandant parametermandant = new Parametermandant(parametermandantDtoI.getCNr(),
					parametermandantDtoI.getMandantCMandant(), parametermandantDtoI.getCKategorie(),
					parametermandantDtoI.getCWert(), theClientDto.getIDPersonal(),
					parametermandantDtoI.getCBemerkungsmall(), parametermandantDtoI.getCDatentyp());
			em.persist(parametermandant);
			em.flush();

			// die generierten Daten setzen
			parametermandantDtoI.setTAendern(parametermandant.getTAendern());
			parametermandantDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			setParametermandantFromParametermandantDto(parametermandant, parametermandantDtoI);

			pkParametermandant = new ParametermandantPK();
			pkParametermandant.setCNr(parametermandantDtoI.getCNr());
			pkParametermandant.setMandantCNr(parametermandantDtoI.getMandantCMandant());
			pkParametermandant.setCKategorie(parametermandantDtoI.getCKategorie());

			if (bReload == true) {
				getBenutzerServicesFac().markMandantparameterModified(pkParametermandant);
			}

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return pkParametermandant;
	}

	// Parametermandant
	// ----------------------------------------------------------
	public ParametermandantPK createParametermandant(ParametermandantDto parametermandantDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return createParametermandant(parametermandantDtoI, theClientDto, true);
	}

	public void removeParametermandant(ParametermandantDto parametermandantDto) throws EJBExceptionLP {
		myLogger.entry();
		checkParametermandantDto(parametermandantDto);

		ParametermandantPK pkParametermandant = new ParametermandantPK();
		pkParametermandant.setCNr(parametermandantDto.getCNr());
		pkParametermandant.setMandantCNr(parametermandantDto.getMandantCMandant());
		pkParametermandant.setCKategorie(parametermandantDto.getCKategorie());

		Parametermandant toRemove = em.find(Parametermandant.class, pkParametermandant);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeParametermandantgueltigab(ParametermandantgueltigabDto parametermandantgueltigabDto) {

		ParametermandantgueltigabPK pkParametermandantgueltigab = new ParametermandantgueltigabPK();
		pkParametermandantgueltigab.setCNr(parametermandantgueltigabDto.getCNr());
		pkParametermandantgueltigab.setMandantCNr(parametermandantgueltigabDto.getMandantCNr());
		pkParametermandantgueltigab.setCKategorie(parametermandantgueltigabDto.getCKategorie());
		pkParametermandantgueltigab.setTGueltigab(parametermandantgueltigabDto.getTGueltigab());

		Parametermandantgueltigab toRemove = em.find(Parametermandantgueltigab.class, pkParametermandantgueltigab);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		getBenutzerServicesFac()
				.markMandantparameterModified(new ParametermandantPK(pkParametermandantgueltigab.getCNr(),
						pkParametermandantgueltigab.getMandantCNr(), pkParametermandantgueltigab.getCKategorie()));

	}

	public void updateParametermandant(ParametermandantDto parametermandantDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkParametermandantDto(parametermandantDtoI);

		vergleicheParametermandantDtoVorherNachherUndLoggeAenderungen(parametermandantDtoI, theClientDto);

		ParametermandantPK pkParametermandant = new ParametermandantPK();
		pkParametermandant.setCNr(parametermandantDtoI.getCNr());
		pkParametermandant.setMandantCNr(parametermandantDtoI.getMandantCMandant());
		pkParametermandant.setCKategorie(parametermandantDtoI.getCKategorie());

		Parametermandant parametermandant = em.find(Parametermandant.class, pkParametermandant);
		if (parametermandant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		parametermandantDtoI.setTAendern(getTimestamp());
		parametermandantDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

		// UW->WH Wie soll der Eintrag im kritischen Log genau aussehen?
		myLogger.logKritisch("Update " + parametermandantDtoI.toString());

		setParametermandantFromParametermandantDto(parametermandant, parametermandantDtoI);
		getBenutzerServicesFac().markMandantparameterModified(pkParametermandant);
	}

	public void updateParametermandantgueltigab(ParametermandantgueltigabDto parametermandantgueltigabDtoI,
			TheClientDto theClientDto) {

		parametermandantgueltigabDtoI.setTGueltigab(Helper.cutTimestamp(parametermandantgueltigabDtoI.getTGueltigab()));
		ParametermandantgueltigabPK pkParametermandant = new ParametermandantgueltigabPK();
		pkParametermandant.setCNr(parametermandantgueltigabDtoI.getCNr());
		pkParametermandant.setMandantCNr(parametermandantgueltigabDtoI.getMandantCNr());
		pkParametermandant.setCKategorie(parametermandantgueltigabDtoI.getCKategorie());
		pkParametermandant.setTGueltigab(parametermandantgueltigabDtoI.getTGueltigab());

		Parametermandantgueltigab parametermandantgueltigab = em.find(Parametermandantgueltigab.class,
				pkParametermandant);
		if (parametermandantgueltigab == null) {
			// Neu anlegen

			parametermandantgueltigab = new Parametermandantgueltigab(parametermandantgueltigabDtoI.getCNr(),
					parametermandantgueltigabDtoI.getMandantCNr(), parametermandantgueltigabDtoI.getCKategorie(),
					parametermandantgueltigabDtoI.getCWert(), parametermandantgueltigabDtoI.getTGueltigab());

		}

		// und wert setzen
		parametermandantgueltigab.setCWert(parametermandantgueltigabDtoI.getCWert());
		em.merge(parametermandantgueltigab);
		em.flush();

		myLogger.logKritisch("Update " + parametermandantgueltigabDtoI.toString());

		getBenutzerServicesFac().markMandantparameterModified(new ParametermandantPK(pkParametermandant.getCNr(),
				pkParametermandant.getMandantCNr(), pkParametermandant.getCKategorie()));
	}

	public ParametermandantDto parametermandantFindByPrimaryKey(ParametermandantPK pkParametermandantI)
			throws EJBExceptionLP {
		HvOptional<ParametermandantDto> optional = parametermandantFindByPrimaryKeyOptional(pkParametermandantI);
		if (optional.isPresent())
			return optional.get();
		else
			throw new EJBExceptionLPwoRollback(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	}

	public HvOptional<ParametermandantDto> parametermandantFindByPrimaryKeyOptional(
			ParametermandantPK pkParametermandantI) {
		Validator.notNull(pkParametermandantI, "pkParametermandantI");

		Parametermandant parametermandant = em.find(Parametermandant.class, pkParametermandantI);
		if (parametermandant == null) {
			return HvOptional.empty();
		}
		return HvOptional.of(assembleParametermandantDto(parametermandant));
	}

	public ParametermandantgueltigabDto parametermandantgueltigabFindByPrimaryKey(
			ParametermandantgueltigabPK pkParametermandantI) {
		if (pkParametermandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pkParametermandantI == null"));
		}
		Parametermandantgueltigab parametermandant = em.find(Parametermandantgueltigab.class, pkParametermandantI);
		if (parametermandant == null) {
			throw new EJBExceptionLPwoRollback(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return ParametermandantgueltigabDtoAssembler.createDto(parametermandant);

	}

	public TreeMap<Timestamp, String> parametermandantgueltigabGetWerteZumZeitpunkt(String mandantCNr, String cNr,
			String cKategorie) {

		TreeMap<Timestamp, String> tm = null;

		Query query = em.createNamedQuery("ParametermandantgueltigabFindByMandantCNrCNrCKategorie");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, cNr);
		query.setParameter(3, cKategorie);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			tm = new TreeMap<Timestamp, String>();

			Iterator it = cl.iterator();

			while (it.hasNext()) {
				Parametermandantgueltigab pmg = (Parametermandantgueltigab) it.next();
				tm.put(pmg.getPk().getTGueltigab(), pmg.getCWert());
			}

		}
		return tm;

	}

	public ParametermandantDto parametermandantFindByPrimaryKey(String cNrI, String cNrKategorieI, String cNrMandantI)
			throws EJBExceptionLP {
		// try {
		ParametermandantPK parametermandantPK = new ParametermandantPK();
		parametermandantPK.setCNr(cNrI);
		parametermandantPK.setMandantCNr(cNrMandantI);
		parametermandantPK.setCKategorie(cNrKategorieI);

		myLogger.logData(parametermandantPK); // UW: das bleibt
		Parametermandant parametermandant = em.find(Parametermandant.class, parametermandantPK);
		if (parametermandant == null) {
			throw new EJBExceptionLPwoRollback(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, "");
		}
		return assembleParametermandantDto(parametermandant);
		// }
		// catch (FinderException e) {
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
		// e);
		// }
	}

	public ParameteranwenderDto getAnwenderparameter(String cKategorieI, String anwenderparameter_c_nr) {
		ParameteranwenderDto parameteranwenderDto = null;
		ParameteranwenderPK parameteranwenderPK = new ParameteranwenderPK(anwenderparameter_c_nr, cKategorieI);
		try {
			parameteranwenderDto = parameteranwenderFindByPrimaryKey(parameteranwenderPK);
		} catch (Throwable th) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, new Exception("Anwenderparameter " + cKategorieI
					+ "." + anwenderparameter_c_nr + " kann nicht gefunden werden !"));
		}
		return parameteranwenderDto;
	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr, String cKategorieI,
			String mandantparameter_c_nr) {
		return getBenutzerServicesFac().getMandantparameter(mandant_c_nr, cKategorieI, mandantparameter_c_nr);
	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr, String cKategorieI,
			String mandantparameter_c_nr, java.sql.Timestamp tZeitpunkt) {
		return getBenutzerServicesFac().getMandantparameter(mandant_c_nr, cKategorieI, mandantparameter_c_nr,
				tZeitpunkt);
	}

	public void createFixverdrahteteParametermandant(TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "createFixverdrahteteParametermandant";
		myLogger.entry();

		if (aFixverdrahteteParametermandantDtos == null) {
			aFixverdrahteteParametermandantDtos = new ParametermandantDto[progMandantParameter.length];

			for (int i = 0; i < progMandantParameter.length; i++) {
				ParametermandantDto parametermandantDto = new ParametermandantDto();
				parametermandantDto.setCNr(progMandantParameter[i][1]);
				parametermandantDto.setCKategorie(progMandantParameter[i][0]);
				parametermandantDto.setMandantCMandant(theClientDto.getMandant());
				parametermandantDto.setCWert(progMandantParameter[i][2]);
				parametermandantDto.setCDatentyp(progMandantParameter[i][3]);
				parametermandantDto.setCBemerkungsmall(progMandantParameter[i][4]);
				parametermandantDto.setCBemerkunglarge(progMandantParameter[i][5]);
				parametermandantDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
				parametermandantDto.setTAendern(getTimestamp());

				aFixverdrahteteParametermandantDtos[i] = parametermandantDto;
			}
		}

		for (int i = 0; i < aFixverdrahteteParametermandantDtos.length; i++) {
			createParametermandant(aFixverdrahteteParametermandantDtos[i], theClientDto, false);
		}

		getBenutzerServicesFac().markAllMandantenparameterModified();

	}

	public void leereMandantenparameterCache() {
		getBenutzerServicesFac().markAllMandantenparameterModified();
	}

	private void checkParametermandantDto(ParametermandantDto oParametermandantDtoI) throws EJBExceptionLP {
		if (oParametermandantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oParametermandantDtoI == null"));
		}
		if (oParametermandantDtoI.getCDatentyp().equals("java.lang.Double")
				|| oParametermandantDtoI.getCDatentyp().equals("java.math.BigDecimal")) {
			oParametermandantDtoI.setCWert(oParametermandantDtoI.getCWert().replaceAll(",", "."));
		}
	}

	private void setParametermandantFromParametermandantDto(Parametermandant parametermandant,
			ParametermandantDto parametermandantDto) {

		parametermandant.setCWert(parametermandantDto.getCWert());
		parametermandant.setTAendern(parametermandantDto.getTAendern());
		parametermandant.setPersonalIIdAendern(parametermandantDto.getPersonalIIdAendern());
		parametermandant.setCBemerkungsmall(parametermandantDto.getCBemerkungsmall());
		parametermandant.setCBemerkunglarge(parametermandantDto.getCBemerkunglarge());
		em.merge(parametermandant);
		em.flush();
	}

	private ParametermandantDto assembleParametermandantDto(Parametermandant parametermandant) {
		return ParametermandantDtoAssembler.createDto(parametermandant);
	}

	private ParametermandantDto[] assembleParametermandantDtos(Collection<?> parametermandants) {
		List<ParametermandantDto> list = new ArrayList<ParametermandantDto>();
		if (parametermandants != null) {
			Iterator<?> iterator = parametermandants.iterator();
			while (iterator.hasNext()) {
				Parametermandant parametermandant = (Parametermandant) iterator.next();
				list.add(assembleParametermandantDto(parametermandant));
			}
		}
		ParametermandantDto[] returnArray = new ParametermandantDto[list.size()];
		return (ParametermandantDto[]) list.toArray(returnArray);
	}

	// Parameteranwender
	// ---------------------------------------------------------

	public ParameteranwenderPK createParameteranwender(ParameteranwenderDto parameteranwenderDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDtoI);

		ParameteranwenderPK pkParameteranwender = null;

		try {
			Parameteranwender parameteranwender = new Parameteranwender(parameteranwenderDtoI.getCNr(),
					parameteranwenderDtoI.getCWert(), parameteranwenderDtoI.getCKategorie(),
					theClientDto.getIDPersonal(), parameteranwenderDtoI.getCDatentyp());
			em.persist(parameteranwender);
			em.flush();

			// die generierten Daten setzen
			parameteranwenderDtoI.setTAendern(parameteranwender.getTAendern());
			parameteranwenderDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			setParameteranwenderFromParameteranwenderDto(parameteranwender, parameteranwenderDtoI);

			pkParameteranwender = new ParameteranwenderPK();
			pkParameteranwender.setCNr(parameteranwenderDtoI.getCNr());
			pkParameteranwender.setCKategorie(parameteranwenderDtoI.getCKategorie());
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return pkParameteranwender;
	}

	public void removeParameteranwender(ParameteranwenderDto parameteranwenderDto) throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDto);

		ParameteranwenderPK pkParameteranwender = new ParameteranwenderPK();
		pkParameteranwender.setCNr(parameteranwenderDto.getCNr());
		pkParameteranwender.setCKategorie(parameteranwenderDto.getCKategorie());

		Parameteranwender toRemove = em.find(Parameteranwender.class, pkParameteranwender);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateParameteranwender(ParameteranwenderDto parameteranwenderDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		myLogger.entry();
		checkParameteranwenderDto(parameteranwenderDtoI);

		// try {
		ParameteranwenderPK pkParameteranwender = new ParameteranwenderPK();
		pkParameteranwender.setCNr(parameteranwenderDtoI.getCNr());
		pkParameteranwender.setCKategorie(parameteranwenderDtoI.getCKategorie());

		Parameteranwender parameteranwender = em.find(Parameteranwender.class, pkParameteranwender);
		if (parameteranwender == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		parameteranwenderDtoI.setTAendern(getTimestamp());
		parameteranwenderDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

		setParameteranwenderFromParameteranwenderDto(parameteranwender, parameteranwenderDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ParameteranwenderDto parameteranwenderFindByPrimaryKey(ParameteranwenderPK pkParameteranwenderI)
			throws EJBExceptionLP {
		if (pkParameteranwenderI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pkParameteranwenderI == null"));
		}

		// try {
		Parameteranwender parameteranwender = em.find(Parameteranwender.class, pkParameteranwenderI);
		if (parameteranwender == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleParameteranwenderDto(parameteranwender);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void checkParameteranwenderDto(ParameteranwenderDto oParameteranwenderDtoI) throws EJBExceptionLP {
		if (oParameteranwenderDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oParameteranwenderDtoI == null"));
		}
	}

	private void setParameteranwenderFromParameteranwenderDto(Parameteranwender parameteranwender,
			ParameteranwenderDto parameteranwenderDto) {
		parameteranwender.setCWert(parameteranwenderDto.getCWert());
		parameteranwender.setCBemerkungsmall(parameteranwenderDto.getCBemerkungsmall());
		parameteranwender.setCBemerkunglarge(parameteranwenderDto.getCBemerkunglarge());
		em.merge(parameteranwender);
		em.flush();
	}

	private ParameteranwenderDto assembleParameteranwenderDto(Parameteranwender parameteranwender) {
		return ParameteranwenderDtoAssembler.createDto(parameteranwender);
	}

	private ParameteranwenderDto[] assembleParameteranwenderDtos(Collection<?> parameteranwenders) {
		List<ParameteranwenderDto> list = new ArrayList<ParameteranwenderDto>();
		if (parameteranwenders != null) {
			Iterator<?> iterator = parameteranwenders.iterator();
			while (iterator.hasNext()) {
				Parameteranwender parameteranwender = (Parameteranwender) iterator.next();
				list.add(assembleParameteranwenderDto(parameteranwender));
			}
		}
		ParameteranwenderDto[] returnArray = new ParameteranwenderDto[list.size()];
		return (ParameteranwenderDto[]) list.toArray(returnArray);
	}

	public Integer createArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto) throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getCPcname() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("arbeitsplatzDto.getCPcname() == null"));
		}

		try {
			Query query = em.createNamedQuery("ArbeitsplatzfindByCPcname");
			query.setParameter(1, arbeitsplatzDto.getCPcname());
			Arbeitsplatz arbeitsplatz = (Arbeitsplatz) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_ARBEITSPLATZ.C_PCNAME"));
		} catch (NoResultException ex) {
			//
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARBEITSPLATZ);
			arbeitsplatzDto.setIId(pk);

			Arbeitsplatz arbeitsplatz = new Arbeitsplatz(arbeitsplatzDto.getIId(), arbeitsplatzDto.getCPcname());
			em.persist(arbeitsplatz);
			em.flush();
			setArbeitsplatzFromArbeitsplatzDto(arbeitsplatz, arbeitsplatzDto);

			return arbeitsplatzDto.getIId();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto) throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("arbeitsplatzDto.getIId() == null"));
		}

		Integer iId = arbeitsplatzDto.getIId();
		Arbeitsplatz toRemove = em.find(Arbeitsplatz.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto) throws EJBExceptionLP {
		if (arbeitsplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("arbeitsplatzDto == null"));
		}
		if (arbeitsplatzDto.getIId() == null && arbeitsplatzDto.getCPcname() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("arbeitsplatzDto.getIId() == null && arbeitsplatzDto.getCPcname() == null"));
		}

		Integer iId = arbeitsplatzDto.getIId();
		Arbeitsplatz arbeitsplatz = em.find(Arbeitsplatz.class, iId);
		if (arbeitsplatz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setArbeitsplatzFromArbeitsplatzDto(arbeitsplatz, arbeitsplatzDto);

		getBenutzerServicesFac().reloadArbeitsplatzparameter();
	}

	public ArbeitsplatzDto arbeitsplatzFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Arbeitsplatz arbeitsplatz = em.find(Arbeitsplatz.class, iId);
		if (arbeitsplatz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArbeitsplatzDto(arbeitsplatz);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArbeitsplatzDto arbeitsplatzFindByCPcname(String cPcname) throws EJBExceptionLP {

		if (cPcname == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cPcname == null"));
		}

		try {
			Query query = em.createNamedQuery("ArbeitsplatzfindByCPcname");
			query.setParameter(1, cPcname);
			Arbeitsplatz arbeitsplatz = (Arbeitsplatz) query.getSingleResult();
			return assembleArbeitsplatzDto(arbeitsplatz);

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
	}

	public ArbeitsplatzDto arbeitsplatzFindByCTypCGeraetecode(String cTyp, String cGeraetecode) {

		if (cGeraetecode == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cGeraetecode == null"));
		}

		try {
			Query query = em.createNamedQuery("ArbeitsplatzFindByCTypCGeraetecode");
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

	private void setArbeitsplatzFromArbeitsplatzDto(Arbeitsplatz arbeitsplatz, ArbeitsplatzDto arbeitsplatzDto) {
		arbeitsplatz.setCStandort(arbeitsplatzDto.getCStandort());
		arbeitsplatz.setCBemerkung(arbeitsplatzDto.getCBemerkung());
		arbeitsplatz.setCPcname(arbeitsplatzDto.getCPcname());
		em.merge(arbeitsplatz);
		em.flush();
	}

	private ArbeitsplatzDto assembleArbeitsplatzDto(Arbeitsplatz arbeitsplatz) {
		return ArbeitsplatzDtoAssembler.createDto(arbeitsplatz);
	}

	private ArbeitsplatzDto[] assembleArbeitsplatzDtos(Collection<?> arbeitsplatzs) {
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

	public void createParameter(ParameterDto parameterDto) throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null"));
		}

		// try {
		Parameter parameter = em.find(Parameter.class, parameterDto.getCNr());
		if (parameter != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_PARAMETER.C_NRE"));
		}
		// catch (FinderException ex) {
		//
		// }

		try {
			parameter = new Parameter(parameterDto.getCNr(), parameterDto.getCDatentyp());
			em.persist(parameter);
			em.flush();
			setParameterFromParameterDto(parameter, parameterDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeParameter(ParameterDto parameterDto) throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("parameterDto.getCNr() == null"));
		}

		String cNr = parameterDto.getCNr();
		Parameter toRemove = em.find(Parameter.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateParameter(ParameterDto parameterDto) throws EJBExceptionLP {
		if (parameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("parameterDto == null"));
		}
		if (parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("parameterDto.getCNr() == null && parameterDto.getCDatentyp() == null"));
		}

		String cNr = parameterDto.getCNr();
		// try {
		Parameter parameter = em.find(Parameter.class, cNr);
		if (parameter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setParameterFromParameterDto(parameter, parameterDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ParameterDto parameterFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNr == null"));
		}

		// try {
		Parameter parameter = em.find(Parameter.class, cNr);
		if (parameter == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleParameterDto(parameter);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setParameterFromParameterDto(Parameter parameter, ParameterDto parameterDto) {
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

	public Integer createArbeitsplatzparameter(ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws EJBExceptionLP {

		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getArbeitsplatzIId() == null && arbeitsplatzparameterDto.getParameterCNr() == null
				&& arbeitsplatzparameterDto.getCWert() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"arbeitsplatzparameterDto.getArbeitsplatzIId() == null && arbeitsplatzparameterDto.getParameterCNr() == null && arbeitsplatzparameterDto.getCWert() == null"));
		}
		try {
			Query query = em.createNamedQuery("ArbeitsplatzparameterfindByArbeitsplatzIIdParameterCNr");
			query.setParameter(1, arbeitsplatzparameterDto.getArbeitsplatzIId());
			query.setParameter(2, arbeitsplatzparameterDto.getParameterCNr());
			Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_ARBEITSPLATZPARAMETER.UK"));
		} catch (NoResultException ex) {
			//
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARBEITSPLATZPARAMETER);
			arbeitsplatzparameterDto.setIId(pk);

			Arbeitsplatzparameter arbeitsplatzparameter = new Arbeitsplatzparameter(arbeitsplatzparameterDto.getIId(),
					arbeitsplatzparameterDto.getArbeitsplatzIId(), arbeitsplatzparameterDto.getParameterCNr());
			em.persist(arbeitsplatzparameter);
			em.flush();
			setArbeitsplatzparameterFromArbeitsplatzparameterDto(arbeitsplatzparameter, arbeitsplatzparameterDto);

			getBenutzerServicesFac().reloadArbeitsplatzparameter();

			return arbeitsplatzparameterDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeArbeitsplatzparameter(ArbeitsplatzparameterDto arbeitsplatzparameterDto) throws EJBExceptionLP {
		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("arbeitsplatzparameterDto.getIId() == null"));
		}

		Integer iId = arbeitsplatzparameterDto.getIId();
		Arbeitsplatzparameter toRemove = em.find(Arbeitsplatzparameter.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		getBenutzerServicesFac().reloadArbeitsplatzparameter();

	}

	public void updateArbeitsplatzparameter(ArbeitsplatzparameterDto arbeitsplatzparameterDto) throws EJBExceptionLP {
		if (arbeitsplatzparameterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("arbeitsplatzparameterDto == null"));
		}
		if (arbeitsplatzparameterDto.getIId() == null && arbeitsplatzparameterDto.getArbeitsplatzIId() == null
				&& arbeitsplatzparameterDto.getParameterCNr() == null && arbeitsplatzparameterDto.getCWert() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"arbeitsplatzparameterDto.getIId() == null && arbeitsplatzparameterDto.getArbeitsplatzIId() == null && arbeitsplatzparameterDto.getParameterCNr() == null && arbeitsplatzparameterDto.getCWert() == null"));
		}

		Integer iId = arbeitsplatzparameterDto.getIId();
		// try {
		Arbeitsplatzparameter arbeitsplatzparameter = em.find(Arbeitsplatzparameter.class, iId);
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setArbeitsplatzparameterFromArbeitsplatzparameterDto(arbeitsplatzparameter, arbeitsplatzparameterDto);
		getBenutzerServicesFac().reloadArbeitsplatzparameter();

	}

	public ArbeitsplatzparameterDto arbeitsplatzparameterFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Arbeitsplatzparameter arbeitsplatzparameter = em.find(Arbeitsplatzparameter.class, iId);
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArbeitsplatzparameterDto(arbeitsplatzparameter);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(String cPcname, String parameterCNr) {
		return getBenutzerServicesFac().holeArbeitsplatzparameter(cPcname, parameterCNr);
	}

	public ArbeitsplatzparameterDto arbeitsplatzparameterFindByArbeitsplatzIIdParameterCNr(Integer arbeitsplatzIId,
			String parameterCNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ArbeitsplatzparameterfindByArbeitsplatzIIdParameterCNr");
		query.setParameter(1, arbeitsplatzIId);
		query.setParameter(2, parameterCNr);
		Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) query.getSingleResult();
		if (arbeitsplatzparameter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleArbeitsplatzparameterDto(arbeitsplatzparameter);

	}

	private void setArbeitsplatzparameterFromArbeitsplatzparameterDto(Arbeitsplatzparameter arbeitsplatzparameter,
			ArbeitsplatzparameterDto arbeitsplatzparameterDto) {
		arbeitsplatzparameter.setArbeitsplatzIId(arbeitsplatzparameterDto.getArbeitsplatzIId());
		arbeitsplatzparameter.setParameterCNr(arbeitsplatzparameterDto.getParameterCNr());
		arbeitsplatzparameter.setCWert(arbeitsplatzparameterDto.getCWert());
		em.merge(arbeitsplatzparameter);
		em.flush();
	}

	private ArbeitsplatzparameterDto assembleArbeitsplatzparameterDto(Arbeitsplatzparameter arbeitsplatzparameter) {
		return ArbeitsplatzparameterDtoAssembler.createDto(arbeitsplatzparameter);
	}

	private ArbeitsplatzparameterDto[] assembleArbeitsplatzparameterDtos(Collection<?> arbeitsplatzparameters) {
		List<ArbeitsplatzparameterDto> list = new ArrayList<ArbeitsplatzparameterDto>();
		if (arbeitsplatzparameters != null) {
			Iterator<?> iterator = arbeitsplatzparameters.iterator();
			while (iterator.hasNext()) {
				Arbeitsplatzparameter arbeitsplatzparameter = (Arbeitsplatzparameter) iterator.next();
				list.add(assembleArbeitsplatzparameterDto(arbeitsplatzparameter));
			}
		}
		ArbeitsplatzparameterDto[] returnArray = new ArbeitsplatzparameterDto[list.size()];
		return (ArbeitsplatzparameterDto[]) list.toArray(returnArray);
	}

	private void vergleicheParametermandantDtoVorherNachherUndLoggeAenderungen(
			ParametermandantDto parametermandantDto_Aktuell, TheClientDto theClientDto) {

		// Die BenutzerservicesFacBean hat eine eigene Logik um "fehlende"
		// Parameter zu laden. TestcaseAutoKommmt
		ParametermandantDto parametermandantDto_Vorher = getMandantparameter(
				parametermandantDto_Aktuell.getMandantCMandant(), parametermandantDto_Aktuell.getCKategorie(),
				parametermandantDto_Aktuell.getCNr());

		// ParametermandantDto parametermandantDto_Vorher =
		// parametermandantFindByPrimaryKey(
		// parametermandantDto_Aktuell.getCNr(),
		// parametermandantDto_Aktuell.getCKategorie(),
		// theClientDto.getMandant());

		HvDtoLogger<ParametermandantDto> artikelLogger = new HvDtoLogger<ParametermandantDto>(em, theClientDto);
		artikelLogger.log(parametermandantDto_Vorher, parametermandantDto_Aktuell);
	}

	/**
	 * Liefert die Standard-Anzahl der Lieferschein-Kopien
	 * 
	 * @return Integer
	 * @param mandantCNr String
	 */
	public Integer getAnzahlDefaultKopienLieferschein(String mandantCNr) throws EJBExceptionLP {

		ParametermandantDto parameter = getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_DEFAULT_KOPIEN_LIEFERSCHEIN);

		return (Integer) parameter.getCWertAsObject();

		// Integer kopienLieferschein = 0;
		// if (parameter.getCWert().length() > 0 && parameter.getCWert() !=
		// null)
		// kopienLieferschein = (Integer) parameter.getCWertAsObject();

		// return kopienLieferschein;

	}

	public Integer getArtikelLaengeBezeichungen(String mandantCNr) {

		ParametermandantDto parameter = getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_ARTIKEL_LAENGE_BEZEICHNUNG);

		return (Integer) parameter.getCWertAsObject();

	}

	/**
	 * Liefert die Standard-Anzahl der Rechnungs-Kopien
	 * 
	 * @return Integer
	 * @param mandantCNr String
	 */
	public Integer getAnzahlDefaultKopienRechnung(String mandantCNr) throws EJBExceptionLP {

		ParametermandantDto parameter = getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_DEFAULT_KOPIEN_RECHNUNG);

		return (Integer) parameter.getCWertAsObject();
	}

	public boolean getLogoImmerDrucken(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN);
		return Helper.short2boolean(Short.parseShort(parameter.getCWert()));
	}

	public String getFindChipsApiKey(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_FINDCHIPS_APIKEY);
		String s = parameter.getCWert();
		if (s == null || s.trim().length() < 5)
			return null;
		return s.trim();
	}

	public HttpProxyConfig getHttpProxy(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_HTTP_PROXY);
		return new HttpProxyConfig(parameter.getCWert());
	}

	@Override
	public Boolean getKundenPositionskontierung(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
		return Helper.short2boolean(Short.parseShort(parameter.getCWert()));
	}

	public Boolean getFinanzRechnungWert0Erlaubt(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_RECHNUNG_WERT0_ERLAUBT);
		return "1".equals(parameter.getCWert());
	}

	public Boolean getReversechargeVerwenden(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		return "1".equals(parameter.getCWert());
	}

	@Override
	public HttpProxyConfig getHvToHydraHost(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_HYDRA,
				ParameterFac.PARAMETER_HYDRA_SERVER);
		return new HttpProxyConfig(parameter.getCWert());
	}

	@Override
	public Integer getHydraFertigungsgruppeIId(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_HYDRA,
				ParameterFac.PARAMETER_HYDRA_FERTIGUNGSGRUPPE);
		return parameter.asInteger();
	}

	@Override
	public Integer getLieferavisoTageZusaetzlich(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_LIEFERAVISO_TAGE_ZUSAETZLICH);
		return parameter.asInteger();
	}

	public ArbeitsplatzkonfigurationDto arbeitsplatzkonfigurationFindByPrimaryKey(Integer arbeitsplatzId) {
		Validator.pkFieldNotNull(arbeitsplatzId, "arbeitsplatzId");
		Arbeitsplatzkonfiguration konfiguration = em.find(Arbeitsplatzkonfiguration.class, arbeitsplatzId);
		return konfiguration == null ? null : ArbeitsplatzkonfigurationDtoAssembler.createDto(konfiguration);
	}

	public Integer createArbeitsplatzkonfiguration(ArbeitsplatzkonfigurationDto dto) {
		Validator.dtoNotNull(dto, "dto");
		Validator.notNull(dto.getArbeitsplatzIId(), "arbeitsplatzId");

		Arbeitsplatzkonfiguration konfiguration = new Arbeitsplatzkonfiguration(dto.getArbeitsplatzIId());
		em.persist(konfiguration);
		em.flush();

		ArbeitsplatzkonfigurationDtoAssembler.setEntityFromDto(konfiguration, dto);
		em.merge(konfiguration);
		em.flush();

		return konfiguration.getArbeitsplatzIId();
	}

	public void updateArbeitsplatzkonfiguration(ArbeitsplatzkonfigurationDto dto) {
		Validator.dtoNotNull(dto, "dto");
		Validator.pkFieldNotNull(dto.getArbeitsplatzIId(), "arbeitsplatzIId");

		Arbeitsplatzkonfiguration konfiguration = em.find(Arbeitsplatzkonfiguration.class, dto.getArbeitsplatzIId());
		if (konfiguration == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, dto.getArbeitsplatzIId().toString());
		}

		ArbeitsplatzkonfigurationDtoAssembler.setEntityFromDto(konfiguration, dto);
		em.merge(konfiguration);
		em.flush();
	}

	public void removeArbeitsplatzkonfiguration(ArbeitsplatzkonfigurationDto dto) {
		Validator.dtoNotNull(dto, "dto");
		Validator.pkFieldNotNull(dto.getArbeitsplatzIId(), "arbeitsplatzIId");

		Arbeitsplatzkonfiguration entity = em.find(Arbeitsplatzkonfiguration.class, dto.getArbeitsplatzIId());
		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, dto.getArbeitsplatzIId().toString());
		}

		try {
			em.remove(entity);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	@Override
	public Boolean getZahlungsvorschlagDefaultFreigabe(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_DEFAULT_FREIGABE);
		return "1".equals(parameter.getCWert());
	}

	@Override
	public String getMailadresseAdmin(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_VERSANDAUFTRAG,
				ParameterFac.PARAMETER_MAILADRESSE_ADMIN);
		return parameter.getCWert();
	}

	@Override
	public Boolean getSuchenInklusiveKBez(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		return "1".equals(parameter.getCWert());
	}

	@Override
	public String getKommissionierungFertigungsgruppe(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_KOMMISSIONIERUNG_FERTIGUNGSGRUPPE);
		return parameter.getCWert();
	}

	@Override
	public List<String> getKommissionierungAusgeschlosseneArtikelgruppen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_KOMMISSIONIERUNG_AUSGESCHLOSSENE_ARTIKELGRUPPEN);
		String s = parameter.getCWert();
		if (s == null || s.isEmpty())
			return new ArrayList<String>();

		String[] splited = s.split("\\s*\\u007C\\s*");
		return Arrays.asList(splited);
	}

	public ArtikelDto getMindestbestellwertArtikel(TheClientDto theClientDto) {
		ArtikelDto artikelDto = null;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MINDESTBESTELLWERT_ARTIKEL);

			if (parameterDto != null && parameterDto.getCWert() != null && !parameterDto.getCWert().trim().equals("")) {
				try {

					artikelDto = getArtikelFac().artikelFindByCNr(parameterDto.getCWert(), theClientDto);

				} catch (EJBExceptionLP ex2) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT,
							new Exception("FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT"));
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT,
						new Exception("FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT"));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return artikelDto;
	}

	public Boolean getSollsatzgroessePruefen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_SOLLSATZGROESSE_PRUEFEN);
		return "1".equals(parameter.getCWert());
	}

	@Override
	public Boolean getAutofertigAblieferungTerminal(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_AUTOFERTIG_ABLIEFERUNG_TERMINAL);
		return "1".equals(parameter.getCWert());
	}

	@Override
	public Integer getMaximaleDokumentengroesse(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_ALLGEMEIN_DOKUMENTE_MAXIMALE_GROESSE);
		return parameter.asInteger();
	}

	@Override
	public Integer getLastschriftFaelligkeitNachZahlungsziel(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_RECHNUNG,
				ParameterFac.PARAMETER_LASTSCHRIFT_FAELLIGKEIT_NACH_ZAHLUNGSZIEL);
		return parameter.asInteger();
	}

	@Override
	public String getHtmlSignaturMailversand(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_HTML_SIGNATUR_MAILVERSAND);
		return parameter.getCWert().trim();
	}

	@Override
	public Integer getKalkulationsart(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
				ParameterFac.PARAMETER_KALKULATIONSART);
		return parameter.asInteger();
	}

	public Double getDefaultAufschlag(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
				ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG);
		return parameter.asDouble();
	};

	@Override
	public String getEinrichtelagerErfassungsterminal(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_EINRICHTELAGER_ERFASSUNGSTERMINAL);
		return parameter.getCWert().trim();
	}

	@Override
	public String getGS1BasisnummerGTIN(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_GS1_BASISNUMMER_GTIN);
		return parameter.getCWert();
	}

	public String getGS1BasisnummerSSCC(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_GS1_BASISNUMMER_SSCC);
		return parameter.getCWert();
	}

	@Override
	public Integer getGeschaeftsjahrBeginnmonat(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
		return parameter.asInteger();
	}

	@Override
	public String getUvaAbrechnungszeitraum(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_UVA_ABRECHNUNGSZEITRAUM);
		return parameter.getCWert();
	}

	@Override
	public Boolean getMaterialkursAufBasisRechnungsdatum(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_MATERIALKURS_AUF_BASIS_RECHNUNGSDATUM);
		return parameter.asBoolean();
	}

	@Override
	public String getKassenimportLeereArtikelnummerDefaultArtikel(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_KASSENIMPORT_LEERE_ARTIKELNUMMER_DEFAULT_ARTIKEL);
		return parameter.getCWert();
	}

	@Override
	public Boolean getEingangsrechnungKundendatenVorbesetzen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_EINGANGSRECHNUNG_KUNDENDATEN_VORBESETZEN);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getRechnungPositionWert0Pruefen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_RECHNUNG,
				ParameterFac.PARAMETER_RECHNUNG_POSITION_WERT0_PRUEFEN);
		return parameter.asBoolean();
	}

	@Override
	public String getKartenUrl(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_PARTNER,
				ParameterFac.PARAMETER_URL_ONLINE_KARTENDIENST);
		return parameter.getCWert();
	}

	@Override
	public Boolean getAusgabeEigenerStatus(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getPersoenlicherBestellvorschlag(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_BESTELLUNG,
				ParameterFac.PARAMETER_PERSOENLICHER_BESTELLVORSCHLAG);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getLosBeiAusgabeGestoppt(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_LOS_BEI_AUSGABE_GESTOPPT);
		return parameter.asBoolean();
	}

	@Override
	public String getGoogleApiKey(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_PARTNER,
				ParameterFac.PARAMETER_GOOGLE_APIKEY);
		return parameter.getCWert();
	}

	@Override
	public Boolean get4VendingErArImportBruttoStattNetto(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_4VENDING_ER_AR_IMPORT_BRUTTO_STATT_NETTO);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getLieferscheinPositionenMitAPIVerdichten(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_LIEFERSCHEIN_POSITIONEN_MIT_RESTAPI_VERDICHTEN);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getPositionskontierung(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getAusfuehrlicherMahnungsdruckAr(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_AUSFUEHRLICHER_MAHNUNGSDRUCK_AR);
		return parameter.asBoolean();
	}

	@Override
	public BigDecimal getErlaubteFibuDifferenz(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_NETTO_DIFFERENZ_ERLAUBT);
		return parameter.asBigDecimal();
	}

	@Override
	public Integer getMaximallaengeArtikelnummer(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
		return parameter.asInteger();
	}

	@Override
	public Integer getBelegnummernformatStellenGeschaeftsjahr(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);
		try {
			return new Integer(parameter.getCWert());
		} catch (NumberFormatException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL, ex);
		}
	}

	@Override
	public Integer getBelegnummernformatStellenBelegnummer(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
		try {
			return new Integer(parameter.getCWert());
		} catch (NumberFormatException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL, ex);
		}
	}

	private String getPrinterName(ParametermandantDto parameter) {
		if (parameter.getCWert() == null)
			return null;
		String v = parameter.getCWert().trim();
		if (v.length() == 0)
			return null;
		if (v.equals("."))
			return null;

		return v;
	}

	@Override
	public String getDefaultMobilDruckernameEtikett(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_DEFAULT_MOBIL_DRUCKERNAME_ETIKETT);
		return getPrinterName(parameter);
	}

	@Override
	public String getDefaultMobilDruckernameSeite(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_DEFAULT_MOBIL_DRUCKERNAME_SEITE);
		return getPrinterName(parameter);
	}

	@Override
	public String getGroessenaenderungZusatzstatus(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_FERTIGUNG_GROESSENAENDERUNG_ZUSATZSTATUS);
		return parameter.getCWert();
	}

	@Override
	public String getMobilDruckernameBedarfsuebernahme(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_MOBIL_DRUCKERNAME_BEDARFSUEBERNAHME);
		return getPrinterName(parameter);
	}

	public Integer getLosnummerAuftragsbezogen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
		return parameter.asInteger();
	}

	@Override
	public String getExportDatevMitlaufendesKonto(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_EXPORT_DATEV_MITLAUFENDES_KONTO);
		return parameter.getCWert();
	}

	@Override
	public List<String> getExportDatevKontoklassenOhneBuSchluessel(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_EXPORT_DATEV_KONTOKLASSEN_OHNE_BU_SCHLUESSEL);
		String paramWert = parameter.getCWert();
		return Arrays.asList(paramWert.split(","));
	}

	@Override
	public Boolean getBeiLosErledigenMaterialNachbuchen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN);
		return parameter.asBoolean();
	}

	@Override
	public Boolean getNegativeSollmengenBuchenAufZiellager(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_NEGATIVE_SOLLMENGEN_BUCHEN_AUF_ZIELLAGER);
		return parameter.asBoolean();
	}

	@Override
	public PostPlcApiKeyDto getPostPlcApiKey(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_POST_PLC_APIKEY);
		return new PostPlcApiKeyDto(parameter.getCWert());
	}

	@Override
	public MailServiceParameterSource getMailServiceParameter(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_VERSANDAUFTRAG,
				ParameterFac.PARAMETER_MAIL_SERVICE_PARAMETER);
		Integer source = parameter.asInteger();
		return new Integer(1).equals(source) ? MailServiceParameterSource.DB : MailServiceParameterSource.XML;
	}

	@Override
	public WebabfrageArtikellieferantSuchparameterDto getWebabfrageArtikellieferantSuchparameter(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_WEBABFRAGE_ARTIKELLIEFERANT_SUCHPARAMETER);
		return new WebabfrageArtikellieferantSuchparameterDto(parameter.asInteger());
	}

	@Override
	public Integer getEingangsrechnungLieferantenrechnungsnummerLaenge(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, KATEGORIE_EINGANGSRECHNUNG,
				PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE);
		return parameter.asInteger();
	}

	@Override
	public Boolean getANABMitZusammenfassung(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ANGEBOT,
				ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG);
		return parameter.asBoolean();
	}

	@Override
	public Integer getDefaultAuftragsLieferzeitTage(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, KATEGORIE_AUFTRAG,
				PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
		return parameter.asInteger();
	}

	@Override
	public String getBelegnummernformatTrennzeichen(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
		return parameter.getCWert();
	}

	@Override
	public String getBelegnummerMandantkennung(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG);
		return parameter.getCWert();
	}

	@Override
	public HvBelegnummernformat getHvBelegnummernformat(String mandantCnr) {
		return new HvBelegnummernformat(getBelegnummernformatStellenGeschaeftsjahr(mandantCnr),
				getBelegnummernformatStellenBelegnummer(mandantCnr), getBelegnummernformatTrennzeichen(mandantCnr),
				getBelegnummerMandantkennung(mandantCnr));
	}

	@Override
	public HvBelegnummernformatHistorisch getHvBelegnummernformatHistorisch(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_HISTORISCH, getTimestamp());
		return new HvBelegnummernformatHistorisch(parameter.getCWert());
	}

	@Override
	public List<HvBelegnummernformatHistorisch> getHvBelegnummernformatHistorischAll(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_HISTORISCH);
		List<HvBelegnummernformatHistorisch> bnList = new ArrayList<HvBelegnummernformatHistorisch>();
		if (parameter.getTmWerteGueltigab() != null) {
			parameter.getTmWerteGueltigab().entrySet()
					.forEach(entry -> bnList.add(new HvBelegnummernformatHistorisch(entry.getValue())));
		}

		return bnList;
	}
	
	@Override
	public HttpProxyConfig getTruTopsBoostHost(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_TRUTOPS_BOOST_SERVER);
		return new HttpProxyConfig(parameter.getCWert());
	}
	
	@Override
	public Boolean getAutomatischeDebitorennummer(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
		return parameter.asBoolean();
	}

	@Override
	public Integer getGeschaeftsjahrbeginnmonat(String mandantCnr) {
		ParametermandantDto parameter = getMandantparameter(mandantCnr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
		return parameter.asInteger();
	}
}
