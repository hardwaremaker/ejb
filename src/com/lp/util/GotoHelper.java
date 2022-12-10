package com.lp.util;

public class GotoHelper {

	// gotobutton: 1 Hier neue GOTO- Konstante definieren und ev unten Getter
	// nachtragen, falls diese vom iReport aus als Hyperlinks unterstuetzt
	// werden sollen
	public static final int GOTO_ARTIKEL_AUSWAHL = 1;
	public static final int GOTO_PARTNER_AUSWAHL = 2;
	public static final int GOTO_KUNDE_AUSWAHL = 3;
	public static final int GOTO_LIEFERANT_AUSWAHL = 4;
	public static final int GOTO_FERTIGUNG_AUSWAHL = 5;
	public static final int GOTO_STUECKLISTE_AUSWAHL = 6;
	public static final int GOTO_STUECKLISTE_DETAIL = 7;
	public static final int GOTO_EINGANGSRECHNUNG_AUSWAHL = 8;
	public static final int GOTO_PROJEKT_AUSWAHL = 9;
	public static final int GOTO_AUFTRAG_AUSWAHL = 10;
	public static final int GOTO_ANGEBOT_AUSWAHL = 11;
	public static final int GOTO_BUCHUNGDETAIL = 12;
	public static final int GOTO_RECHNUNG_AUSWAHL = 13;
	public static final int GOTO_LIEFERSCHEIN_AUSWAHL = 14;
	public static final int GOTO_KREDITORENKONTO_AUSWAHL = 15;
	public static final int GOTO_DEBITORENKONTO_AUSWAHL = 16;
	public static final int GOTO_BESTELLUNG_AUSWAHL = 17;
	public static final int GOTO_BESTELLUNG_POSITION = 18;
	public static final int GOTO_KUNDE_KONDITIONEN = 19;
	public static final int GOTO_INSERAT_AUSWAHL = 20;
	public static final int GOTO_ANFRAGE_AUSWAHL = 21;
	public static final int GOTO_GUTSCHRIFT_AUSWAHL = 22;
	public static final int GOTO_PROJEKT_HISTORY = 23;
	public static final int GOTO_PARTNER_KURZBRIEF = 24;
	public static final int GOTO_LIEFERANT_KURZBRIEF = 25;
	public static final int GOTO_KUNDE_KURZBRIEF = 26;
	public static final int GOTO_ANGEBOTSTKL_AUSWAHL = 27;
	public static final int GOTO_PARTNER_BANK_AUSWAHL = 28;
	public static final int GOTO_LIEFERANT_KONDITIONEN = 29;
	public static final int GOTO_STUECKLISTE_ANDERER_MANDANT_AUSWAHL = 30;
	public static final int GOTO_FORECAST_POSITION = 31;
	public static final int GOTO_ZEITERFASSUNG_ZEITDATEN = 32;
	public static final int GOTO_BESTELLUNG_WARENEINGANG = 33;
	public static final int GOTO_REKLAMATION_AUSWAHL = 34;
	public static final int GOTO_STUECKLISTE_POSITION = 35;
	public static final int GOTO_ZEITERFASSUNG_REISE = 36;
	public static final int GOTO_ZEITERFASSUNG_TELEFONZEITEN = 37;
	public static final int GOTO_MASCHINEN_ZEITDATEN = 38;
	public static final int GOTO_ARTIKELLIEFERANT_ANDERER_MANDANT = 39;
	public static final int GOTO_PERSONAL_AUSWAHL = 40;
	public static final int GOTO_COCKPIT = 41;
	public static final int GOTO_ANFRAGEPOSITIONLIEFERDATEN = 42;
	public static final int GOTO_ANSPRECHPARTNER_LIEFERANT = 43;
	public static final int GOTO_ANSPRECHPARTNER_KUNDE = 44;
	public static final int GOTO_ANSPRECHPARTNER_PARTNER = 45;
	public static final int GOTO_MANDANT_AUSWAHL = 46;
	public static final int GOTO_ZUSATZKOSTEN_AUSWAHL = 47;
	public static final int GOTO_COCKPIT_TELEFONZEITEN = 48;
	public static final int GOTO_PROFORMARECHNUNG_AUSWAHL = 49;
	public static final int GOTO_LOS_GUT_SCHLECHT = 50;
	public static final int GOTO_LOSSOLLMATERIAL = 51;
	
	
	public static final String HYPERLINK_TYPE_GOTO = "GOTO";

	public static final String PARAMETER_WHERE_TO_GO = "WhereToGo";
	public static final String PARAMETER_KEY = "Key";
	public static final String PARAMETER_DETAILKEY = "DetailKey";

	// PJ19583
	// Alle nachfolgenden Getter koennen vom iReport ausausgewaehlt werden
	public static int goto_ARTIKEL_AUSWAHL() {
		return GOTO_ARTIKEL_AUSWAHL;
	}

	public static int goto_FERTIGUNG_AUSWAHL() {
		return GOTO_FERTIGUNG_AUSWAHL;
	}


	public static int goto_RECHNUNG_AUSWAHL() {
		return GOTO_RECHNUNG_AUSWAHL;
	}

	public static int goto_PROFORMARECHNUNG_AUSWAHL() {
		return GOTO_PROFORMARECHNUNG_AUSWAHL;
	}

	public static int goto_LIEFERSCHEIN_AUSWAHL() {
		return GOTO_LIEFERSCHEIN_AUSWAHL;
	}

	public static int goto_BESTELLUNG_AUSWAHL() {
		return GOTO_BESTELLUNG_AUSWAHL;
	}

	public static int goto_GUTSCHRIFT_AUSWAHL() {
		return GOTO_GUTSCHRIFT_AUSWAHL;
	}
	public static int goto_STUECKLISTE_AUSWAHL() {
		return GOTO_STUECKLISTE_AUSWAHL;
	}
	public static int goto_ANGEBOTSTKL_AUSWAHL() {
		return GOTO_ANGEBOTSTKL_AUSWAHL;
	}
	public static int goto_AUFTRAG_AUSWAHL() {
		return GOTO_AUFTRAG_AUSWAHL;
	}
	
	public static int goto_FORECAST_POSITION() {
		return GOTO_FORECAST_POSITION;
	}
	
	public static int goto_ZEITERFASSUNG_ZEITDATEN() {
		return GOTO_ZEITERFASSUNG_ZEITDATEN;
	}
	
	//SP5178
	public static int goto_ANGEBOT_AUSWAHL() {
		return GOTO_ANGEBOT_AUSWAHL;
	}
	public static int goto_EINGANGSRECHNUNG_AUSWAHL() {
		return GOTO_EINGANGSRECHNUNG_AUSWAHL;
	}
	public static int goto_REKLAMATION_AUSWAHL() {
		return GOTO_REKLAMATION_AUSWAHL;
	}
	public static int goto_ANFRAGE_AUSWAHL() {
		return GOTO_ANFRAGE_AUSWAHL;
	}
	public static int goto_STUECKLISTE_POSITION() {
		return GOTO_STUECKLISTE_POSITION;
	}
	//PJ21152
	public static int goto_PROJEKT_AUSWAHL() {
		return GOTO_PROJEKT_AUSWAHL;
	}
	public static int goto_ZUSATZKOSTEN_AUSWAHL() {
		return GOTO_ZUSATZKOSTEN_AUSWAHL;
	}
	
}
