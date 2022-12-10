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
package com.lp.server.system.jcr.service.docnode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.lp.util.Pair;

public abstract class DocNodeBase implements Serializable {

	private static final long serialVersionUID = -8679020503834968474L;

	public static final String SLASH_REPLACEMENT = "&#47";
	public static final int LITERAL = 1;
	public static final int FOLDER = 2;
	public static final int JCRDOC = 3;
	public static final int VERSION = 4;
	public static final int FILE = 5;
	public static final int LAGERSTANDSLISTE = 6;
	public static final int MAIL = 7;
	public static final int SYMBOLIC_LINK = 8;
	public static final int BEDARFSZUSAMMENSCHAU = 9;

	public static final String BELEGART_ANFRAGE = "Anfrage        ";
	public static final String BELEGART_ANGEBOT = "Angebot        ";
	public static final String BELEGART_AGSTUECKLISTE = "AGStueckliste  ";
	public static final String BELEGART_ARTIKEL = "Artikel        ";
	public static final String BELEGART_AUFTRAG = "Auftrag        ";
	public static final String BELEGART_BANK = "Bank           ";
	public static final String BELEGART_BANKVERBINDUNG = "Bankverbindung ";
	public static final String BELEGART_BENUTZER = "Benutzer       ";
	public static final String BELEGART_BESTELLUNG = "Bestellung     ";
	public static final String BELEGART_EINGANGSRECHNG = "Eingangsrechng ";
	public static final String BELEGART_FINANZBUCHHALTG = "Finanzbuchhaltg";
	public static final String BELEGART_GUTSCHRIFT = "Gutschrift     ";
	public static final String BELEGART_HAND = "Hand           ";
	public static final String BELEGART_INVENTUR = "Inventur       ";
	public static final String BELEGART_KUNDE = "Kunde          ";
	public static final String BELEGART_LIEFERANT = "Lieferant      ";
	public static final String BELEGART_LIEFERSCHEIN = "Lieferschein   ";
	public static final String BELEGART_LSPOSITION = "Lieferscheinpos";
	public static final String BELEGART_LOS = "Los            ";
	public static final String BELEGART_INTERNEBESTELLUNG = "IntBestellung";
	public static final String BELEGART_LOSABLIEFERUNG = "Losablieferung ";
	public static final String BELEGART_PARTNER = "Partner        ";
	public static final String BELEGART_PERSONAL = "Personal       ";
	public static final String BELEGART_PROFORMARECHNG = "Proformarechng ";
	public static final String BELEGART_PROJEKT = "Projekt        ";
	public static final String BELEGART_RECHNUNG = "Rechnung       ";
	public static final String BELEGART_REPARATUR = "Reparatur      ";
	public static final String BELEGART_RUECKLIEFERUNG = "Ruecklieferung ";
	public static final String BELEGART_RUECKSCHEIN = "Rueckschein    ";
	public static final String BELEGART_STUECKLISTE = "Stueckliste    ";
	public static final String BELEGART_SYSTEM = "System         ";
	public static final String BELEGART_ZEITERFASSUNG = "Zeigerfassung  ";
	public static final String BELEGART_ZUTRITT = "Zutritt        ";
	public static final String BELEGART_REKLAMATION = "Reklamation    ";
	public static final String BELEGART_LS_ZIELLAGER = "LSZiellager    ";
	public static final String BELEGART_WARENEINGANG = "Wareneingang   ";
	public static final String BELEGART_KUECHE = "Kueche         ";
	public static final String BELEGART_RE_ZAHLUNG = "REZahlung      ";
	public static final String BELEGART_INSTANDHALTUNG = "Instandhaltung ";
	public static final String BELEGART_ER_ZAHLUNG = "ERZahlung      ";
	public static final String BELEGART_INSERAT = 		  "Inserat        ";
	public static final String BELEGART_WOCHENABSCHLUSS = "Wochenabschluss";

	public static final String BELEGART_VERSANDAUFTRAG = "Versandauftrag ";
	public static final String BELEGART_VERSANDANHANG = "Versandanhang  ";
	public static final String BELEGART_UVA = "UVA            ";
	public static final String BELEGART_KASSENBUCH = "Kassenbuch     ";
	public static final String BELEGART_SALDENLISTE = "Saldenliste    ";
	public static final String BELEGART_PROJHISTORY = "ProjektHistory ";
	public static final String BELEGART_SEPA_EXPORT = "Sepa Export    ";
	public static final String BELEGART_SEPA_IMPORT = "Sepa Import    ";
	public static final String BELEGART_GESCHAEFTSJAHR = "Geschaeftsjahr ";
	public static final String BELEGART_ZAHLUNGSVORSCHLAGLAUF = "Zhlngsvrschlglf";
	public static final String BELEGART_MAHNLAUF = "Mahnlauf       ";
	public static final String BELEGART_BILANZ =      "Bilanz            ";
	public static final String BELEGART_ERFOLGSRECHNUNG =      "Erfolgsrechnung   ";

	public static final String BELEGART_AGPOSITION = "AngebotPosition";
	public static final String BELEGART_ANFPOSITION = "AnfragePosition";
	public static final String BELEGART_AUFPOSITION = "AuftragPosition";
	public static final String BELEGART_AGSTKLPOSITION = "AngStklPosition";
	public static final String BELEGART_BESTPOSITION = "Bestellposition";
	public static final String BELEGART_WEPOSITION = "WarEingPosition";
	public static final String BELEGART_LIEFERSPOSITION = "LiefersPosition";
	public static final String BELEGART_STKLPOSITION = "StuecklPosition";
	public static final String BELEGART_RECHPOSITION = "RechnunPosition";
	public static final String BELEGART_GUTSPOSITION = "GutschrPosition";
	public static final String BELEGART_PROFORMAPOS = "ProformPosition";
	public static final String BELEGART_AUFGLFEHLMENGEN = "AufglFehlmengen";
	public static final String BELEGART_LAGERSTANDSLISTE = "Lagerstandslist";
	public static final String BELEGART_ARTIKELGRUPPE = "Artikelgruppe";
	public static final String BELEGART_ARTIKELKLASSE = "Artikelklasse";
	public static final String BELEGART_BUCHUNGDETAIL = "Buchungdetail";
	public static final String BELEGART_ARTIKELETIKETT = "Artikeletikett";
	public static final String BELEGART_PRUEFKOMBINATION = "Pruefkombination";
	public static final String BELEGART_WERKZEUG = "Werkzeug";
	public static final String BELEGART_VERSCHLEISSTEIL = "Verschleissteil";
	public static final String BELEGART_BEAUSKUNFTUNG = "Beauskunftung";
	public static final String BELEGART_EDIFACT       = "Edifact      ";
	public static final String BELEGART_EDIFACTORDERS = "Bestellung   ";
	
	public static final String NODEPROPERTY_IID = "NODEIID";
	public static final String NODEPROPERTY_NODETYPE = "NODETYPE";
	public static final String NODEPROPERTY_BELEGART = "NODEBELEGART";
	public static final String NODEPROPERTY_ARTCNR = "NODEARTCNR";
	public static final String NODEPROPERTY_LITERALVALUE = "NODELITERALVALUE";
	public static final String NODEPROPERTY_SHOWINGVALUE = "NODESHOWINGVALUE";
	public static final String NODEPROPERTY_MANDANTCNR = "NODEMANDANT";
	public static final String NODEPROPERTY_HELPERCNR = "NODEHELPERCNR";
	public static final String NODEPROPERTY_HELPERIID = "NODEHELPERIID";
	public static final String NODEPROPERTY_GESCHAEFTSJAHR = "NODEGESCHAEFTSJAHR";

	public static final String BELEGART_MEDIA_EMAIL      = "MediaEmail     ";
	public static final String BELEGART_MEDIA_ATTACHMENT = "MediaEmailAttmt" ;
	
	private int nodeType;
	private int version = 2;
	private String uuid;

	protected DocNodeBase(int nodeType) {
		this.nodeType = nodeType;
	}

	protected DocNodeBase(Node node) {
		try {
			loadFrom(node);
		} catch (RepositoryException e) {
		}
	}
	
	public abstract String asPath();

	public abstract String asVisualPath();

	public String asEncodedPath() {

		return asPath().replaceAll("/", SLASH_REPLACEMENT).trim();
	}

	public final int getNodeType() {
		return nodeType;
	}

	private final void setNodeType(long nodeType) {
		this.nodeType = (int) nodeType;
	}

	public abstract List<DocNodeBase> getHierarchy();

	protected void loadFrom(Node node) throws RepositoryException {
		setNodeType(node.getProperty(NODEPROPERTY_NODETYPE).getLong());
		loadFromImpl(node);
		initUUId(node);
	}

	public void persistTo(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_NODETYPE, getNodeType());
		persistToImpl(node);
	}

	/**
	 * Die DocNode Variablen-Werte aus den Properties laden. Nodetype muss nicht
	 * geladen werden.
	 */
	protected abstract void loadFromImpl(Node node)
			throws RepositoryException;

	/**
	 * Die DocNode Variablen-Werte in die Properties des Node Object speichern
	 * Nodetype muss nicht gespeichert werden
	 */
	protected abstract void persistToImpl(Node node) throws RepositoryException;

	@Override
	public String toString() {
		return asVisualPath().trim();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	private static List<Pair<String, String>> EscapedJCRCharPairs = Arrays.asList(
			new Pair<String,String>("&", "&#38;"),
			new Pair<String,String>(" ", "&#32;"),
			new Pair<String,String>("/", "&#47;"),
			new Pair<String,String>(":", "&#58;"),
			new Pair<String,String>("<", "&#60;"),
			new Pair<String,String>("=", "&#61;"),
			new Pair<String,String>(">", "&#62;"),
			new Pair<String,String>("?", "&#63;"),
			new Pair<String,String>("[", "&#91;"),
			new Pair<String,String>("]", "&#93;"));
	
	public static String escapeJCRChars(String s) {
		if(s == null) return null;
		
		for (Pair<String, String> pair : EscapedJCRCharPairs) {
			s = s.replace(pair.getKey(), pair.getValue());
		}
		return s;
//		s = s.replace("&", "&#38;");
//		s = s.replace(" ", "&#32;");
//		s = s.replace("/", "&#47;");
//		s = s.replace(":", "&#58;");
//		s = s.replace("<", "&#60;");
//		s = s.replace("=", "&#61;");
//		s = s.replace(">", "&#62;");
//		s = s.replace("?", "&#63;");
//		s = s.replace("[", "&#91;");
//		s = s.replace("]", "&#93;");
//		return s;
////		return "<html>" + s + "</html>";
	}
	
	public static String unescapeJCRChars(String s) {
		if(s == null) return null;
		
		for (Pair<String, String> pair : EscapedJCRCharPairs) {
			s = s.replace(pair.getValue(), pair.getKey());
		}
		return s;
	}
	
	public String getUUId() {
		return uuid;
	}
	
	private void initUUId(Node node) throws RepositoryException {
		if (node.isNodeType("mix:referenceable")) {
			this.uuid = node.getUUID();
		}
	}
}
