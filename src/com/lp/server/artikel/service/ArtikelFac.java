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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ejb.Remote;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.shop.service.WebshopConnectionDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;
import com.lp.server.util.ArtikelTruTopsMetadatenId;
import com.lp.server.util.BaseIntegerKey;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KundeId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.PreislisteId;
import com.lp.server.util.ShopgruppeId;
import com.lp.server.util.WebshopId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.KeyValue;

@Remote
public interface ArtikelFac {

	final static public String SCHEMA_OF_ITEM = "Item";
	final static public String SCHEMA_OF_ITEM_DESCRIPTION = "Description";
	final static public String SCHEMA_OF_ITEM_ID_CUSTOMER = "ItemIDCustomer";
	final static public String SCHEMA_OF_ITEM_ID_SUPPLIER = "ItemIDSupplier";
	final static public String SCHEMA_OF_ITEM_VARIANT_ID_CUSTOMER = "VariantIDCustomer";
	final static public String SCHEMA_OF_ITEM_VARIANT_ID_SUPPLIER = "VariantIDSupplier";
	final static public String SCHEMA_OF_ITEM_ENGINEERING_ID = "EngineeringID";
	final static public String SCHEMA_OF_ITEM_EAN = "EAN";
	final static public String SCHEMA_OF_ITEM_COUNTRY_OF_ORIGIN = "CountryOfOrigin";
	final static public String SCHEMA_OF_ITEM_COUNTRY_OF_PRODUCTION = "CountryOfProduction";
	final static public String SCHEMA_OF_ITEM_CUSTOMS_TARIFF = "CustomsTariff";
	final static public String SCHEMA_OF_ITEM_EXPORT_PROCEDURE_INDICATOR = "ExportProcedureIndicator";
	final static public String SCHEMA_OF_ITEM_HAZARD = "Hazard";

	public static final String ARTIKELART_ARBEITSZEIT = "Arbeitszeit    ";
	public static final String ARTIKELART_ARTIKEL = "Artikel        ";
	public static final String ARTIKELART_HANDARTIKEL = "Handartikel    ";

	public static final int REISEZEIT_KEINE = 0;
	public static final int REISEZEIT_PASSIV = 1;
	public static final int REISEZEIT_AKTIV = 2;

	
	public static final int ZUSCHNITTSARTIKEL_KEINER = 0;
	public static final int ZUSCHNITTSARTIKEL_BASISARTIKEL = 1;
	public static final int ZUSCHNITTSARTIKEL_ZUSCHNITTARTIKEL = 2;
	
	public final static String REPORT_MODUL = "artikel";

	public static final String FLR_ARTIKELGRUPPE_FLRARTIKELGRUPPE = "flrartikelgruppe";
	public static final String FLR_ARTIKELGRUPPE_FLRKONTO = "flrkonto";
	public static final String FLR_ARTIKELGRUPPE_ARTIKELGRUPPESPRSET = "artikelgruppesprset";

	public static final String FLR_ARTIKELKLASSE_FLRARTIKELKLASSE = "flrartikelklasse";
	public static final String FLR_ARTIKELKLASSE_ARTIKELKLASSESPRSET = "artikelklassesprset";

	public static final String FLR_ARTIKEL_I_ID = "i_id";
	public static final String FLR_ARTIKEL_ARTIKELART_C_NR = "artikelart_c_nr";
	public static final String FLR_ARTIKEL_C_NR = "c_nr";
	public static final String FLR_ARTIKEL_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_ARTIKEL_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_ARTIKEL_B_LAGERBEWERTET = "b_lagerbewertet";
	public static final String FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET = "b_lagerbewirtschaftet";
	public static final String FLR_ARTIKEL_B_SERIENNRTRAGEND = "b_seriennrtragend";
	public static final String FLR_ARTIKEL_B_CHARGENNRTRAGEND = "b_chargennrtragend";
	public static final String FLR_ARTIKEL_I_GARANTIEZEIT = "i_garantiezeit";
	public static final String FLR_ARTIKEL_FLRARTIKELKLASSE = "flrartikelklasse";
	public static final String FLR_ARTIKEL_FLRARTIKELGRUPPE = "flrartikelgruppe";
	public static final String FLR_ARTIKEL_I_WARTUNGSINTERVALL = "i_wartungsintervall";

	public static final String FLR_ARTIKELLISTE_C_BEZ = "c_bez";
	public static final String FLR_ARTIKELLISTE_C_REFERENZNR = "c_referenznr";
	public static final String FLR_ARTIKELLISTE_C_VOLLTEXT = "c_volltext";
	public static final String FLR_ARTIKELLISTE_ARTIKELART_C_NR = "artikelart_c_nr";
	public static final String FLR_ARTIKELLISTE_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_ARTIKELLISTE_B_LAGERBEWERTET = "b_lagerbewertet";
	public static final String FLR_ARTIKELLISTE_B_LAGERBEWIRTSCHAFTET = "b_lagerbewirtschafet";
	public static final String FLR_ARTIKELLISTE_B_VERSTECKT = "b_versteckt";
	public static final String FLR_ARTIKELLISTE_B_SERIENNRTRAGEND = "b_seriennrtragend";
	public static final String FLR_ARTIKELLISTE_B_CHARGENNRTRAGEND = "b_chargennrtragend";
	public static final String FLR_ARTIKELLISTE_FLRHERSTELLER = "flrhersteller";

	public static final String FLR_ARTIKELLISTE_STUECKLISTE_PARTNER_ID = "stuecklisten.partner_i_id";
	public static final String FLR_ARTIKELLISTE_SHOPGRUPPE_ID = "shopgruppe_i_id";
	public static final String FLR_ARTIKELLISTE_C_UL = "c_ul";

	public static final String FLR_HERSTELLER_FLRPARTNER = "flrpartner";

	public static final String SETARTIKEL_TYP_KOPF = "Kopf";
	public static final String SETARTIKEL_TYP_POSITION = "Position";

	public static final String FLR_ARTIKELLIEFERANT_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_ARTIKELLIEFERANT_LIEFERANT_I_ID = "lieferant_i_id";
	public static final String FLR_ARTIKELLIEFERANT_N_EINZELPREIS = "n_einzelpreis";
	public static final String FLR_ARTIKELLIEFERANT_N_NETTOPREIS = "n_nettopreis";
	public static final String FLR_ARTIKELLIEFERANT_T_PREISGUELTIGAB = "t_preisgueltigab";
	public static final String FLR_ARTIKELLIEFERANT_I_SORT = "i_sort";
	public static final String FLR_ARTIKELLIEFERANT_C_BEZBEILIEFERANT = "c_bezbeilieferant";
	public static final String FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT = "c_artikelnrlieferant";
	public static final String FLR_ARTIKELLIEFERANT_FLRLIEFERANT = "flrlieferant";

	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_ARTIKELLIEFERANT_I_ID = "artikellieferant_i_id";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE = "n_menge";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_F_RABATT = "f_rabatt";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_N_NETTOPREIS = "n_nettopreis";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_D_PREISGUELITGAB = "t_preisgueltigab";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_D_PREISGUELITGBIS = "t_preisgueltigbis";
	public static final String FLR_ARTIKELLIEFERANTSTAFFEL_FLRARTIKELLIEFERANT = "flrartikellieferant";

	public static final String FLR_KATALOG_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_KATALOG_C_KATALOG = "c_katalog";
	public static final String FLR_KATALOG_C_SEITE = "c_seite";

	public static final String FLR_VKPFARTIKELPREISLISTE_B_PREISLISTEAKTIV = "b_preislisteaktiv";

	public static final String FLR_FEHLMENGE_I_ID = "i_id";
	public static final String FLR_FEHLMENGE_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_FEHLMENGE_C_BELEGARTNR = "c_belegartnr";
	public static final String FLR_FEHLMENGE_I_BELEGARTPOSITIONID = "i_belegartpositionid";
	public static final String FLR_FEHLMENGE_N_MENGE = "n_menge";
	public static final String FLR_FEHLMENGE_FLRARTIKEL = "flrartikel";
	public static final String FLR_FEHLMENGE_FLRLOSSOLLMATERIAL = "flrlossollmaterial";
	public static final String FLR_FEHLMENGE_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_FEHLMENGE_AB_TERMIN = "ab_termin";

	public static final String FLR_SPERREN_B_GESPERRT = "b_gesperrt";
	public static final String FLR_SPERREN_B_GESPERRTEINKAUF = "b_gesperrteinkauf";
	public static final String FLR_SPERREN_B_GESPERRTVERKAUF = "b_gesperrtverkauf";
	public static final String FLR_SPERREN_B_GESPERRTLOS = "b_gesperrtlos";
	public static final String FLR_SPERREN_B_GESPERRTSTUECKLISTE = "b_gesperrtstueckliste";
	public static final String FLR_SPERREN_B_DURCHFERTIGUNG = "b_durchfertigung";

	public static final String FLR_ARTIKELSPERREN_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_ARTIKELSPERREN_SPERREN_I_ID = "sperren_i_id";
	public static final String FLR_ARTIKELSPERREN_FLRARTIKEL = "flrartikel";
	public static final String FLR_ARTIKELSPERREN_FLRSPERREN = "flrsperren";

	public static final String FLR_TRUMPHTOPSLOS_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_TRUMPHTOPSLOS_N_GESTPREISNEU = "n_gestpreisneu";
	public static final String FLR_TRUMPHTOPSLOS_T_ANLEGEN = "t_anlegen";

	public static final String FLR_ZUGEHOERIGE_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_ZUGEHOERIGE_ARTIKEL_I_ID_ZUGEHOERIG = "artikel_i_id_zugehoerig";

	public static final String WEBSHOPART_NICHT_ZUTREFFEND = "NichtZutreffend";
	public static final String WEBSHOPART_MAGENTO2REST = "Magento2Rest";

	// Feldlaengen
	public static final int MAX_ARTIKEL_ARTIKELNUMMER = 25;
	public static final int MAX_ARTIKEL_REFERENZNUMMER = 30;
	public static final int MAX_ARTIKEL_VERKAUFEANNR = 15;
	public static final int MAX_ARTIKEL_WARENVERKEHRSNUMMER = 10;
	public static final int MAX_ARTIKEL_KURZBEZEICHNUNG = 30;
	public static final int MAX_ARTIKEL_BAUFORM = 20;
	public static final int MAX_ARTIKEL_VERPACKUNGSART = 20;
	public static final int MAX_ARTIKEL_TEXTBREITE = 2;
	public static final int MAX_ARTIKEL_ECCN = 15;
	public static final int MAX_ARTIKEL_REVISION = 40;
	public static final int MAX_ARTIKEL_HERSTELLERBEZEICHNUNG = 80;
	public static final int MAX_ARTIKEL_HERSTELLERNR = 300;

	public static final int MAX_KATALOG_KATALOG = 15;
	public static final int MAX_KATALOG_SEITE = 5;

	public static final int MAX_HERSTELLER_NAME = 15;

	public static final int MAX_MATERIAL_NAME = 50;
	public static final int MAX_MATERIAL_BEZEICHNUNG = 80;

	public static final int MAX_LAGER_NAME = 15;

	public static final int MAX_ARTIKELGRUPPE_NAME = 15;
	public static final int MAX_ARTIKELGRUPPE_BEZEICHNUNG = 40;
	public static final int MAX_ARTIKELKLASSE_NAME = 15;
	public static final int MAX_ARTIKELKLASSE_BEZEICHNUNG = 40;
	public static final int MAX_SHOPGRUPPE_NAME = 15;

	public static final int MAX_HANDLAGERBEWEGUNG_KOMMENTAR = 50;
	public static final int MAX_HANDLAGERBEWEGUNG_SERIENCHARGENNR = 50;
	public static final int MAX_LAGERPLATZ_NAME = 40;

	public static final int MAX_ARTIKELLIEFERANT_BEZEICHNUNGBEILIEFERANT = 80;
	public static final int MAX_ARTIKELLIEFERANT_ARTIKELNUMMERBEILIEFERANT = 40;

	public final static int REPORT_ARTIKELSTATISTIK_OPTION_ALLE = 1;
	public final static int REPORT_ARTIKELSTATISTIK_OPTION_VK = 2;
	public final static int REPORT_ARTIKELSTATISTIK_OPTION_EK = 3;
	public final static int REPORT_ARTIKELSTATISTIK_OPTION_FERTIGUNG = 4;

	public final static String WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW = "Kalenderwochen";
	public final static String WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE = "Tage";

	public final static String PATTERN_WARENVERKEHRSNUMMER = "#### ## ##";
	public final static String WARENVERKEHRSNUMMER_NULL = "0000 00 00";

	public final static int EXTERNER_ARBEITSGANG_KEIN = 0;
	public final static int EXTERNER_ARBEITSGANG_MIT_SSG_PRUEFUNG_AM_TERMINAL = 1;
	public final static int EXTERNER_ARBEITSGANG_OHNE_SSG_PRUEFUNG_AM_TERMNIAL = 2;

	public static final String ARTIKEL_KOPIEREN_HERSTELLER = "ARTIKEL_KOPIEREN_HERSTELLER";
	public static final String ARTIKEL_KOPIEREN_ARTIKELGRUPPE = "ARTIKEL_KOPIEREN_ARTIKELGRUPPE";
	public static final String ARTIKEL_KOPIEREN_SHOPGRUPPE = "ARTIKEL_KOPIEREN_SHOPGRUPPE";
	public static final String ARTIKEL_KOPIEREN_ARTIKELKLASSE = "ARTIKEL_KOPIEREN_ARTIKELKLASSE";
	public static final String ARTIKEL_KOPIEREN_REFERENZNUMMER = "ARTIKEL_KOPIEREN_REFERENZNUMMER";
	public static final String ARTIKEL_KOPIEREN_LAGERMINDESTSTAND = "ARTIKEL_KOPIEREN_LAGERMINDESTSTAND";
	public static final String ARTIKEL_KOPIEREN_LAGERSOLLSTAND = "ARTIKEL_KOPIEREN_LAGERSOLLSTAND";
	public static final String ARTIKEL_KOPIEREN_VERPACKUNSMENGE = "ARTIKEL_KOPIEREN_VERPACKUNSMENGE";
	public static final String ARTIKEL_KOPIEREN_VERSCHNITTFAKTOR = "ARTIKEL_KOPIEREN_VERSCHNITTFAKTOR";
	public static final String ARTIKEL_KOPIEREN_VERSCHNITTBASIS = "ARTIKEL_KOPIEREN_VERSCHNITTBASIS";
	public static final String ARTIKEL_KOPIEREN_JAHRESMENGE = "ARTIKEL_KOPIEREN_JAHRESMENGE";
	public static final String ARTIKEL_KOPIEREN_MWSTSATZ = "ARTIKEL_KOPIEREN_MWSTSATZ";
	public static final String ARTIKEL_KOPIEREN_MATERIAL = "ARTIKEL_KOPIEREN_MATERIAL";
	public static final String ARTIKEL_KOPIEREN_GEWICHT = "ARTIKEL_KOPIEREN_GEWICHT";
	public static final String ARTIKEL_KOPIEREN_MATERIALGEWICHT = "ARTIKEL_KOPIEREN_MATERIALGEWICHT";
	public static final String ARTIKEL_KOPIEREN_ZUGEHOERIGERARTIKEL = "ARTIKEL_KOPIEREN_ZUGEHOERIGERARTIKEL";
	public static final String ARTIKEL_KOPIEREN_VERTRETERPROVISION = "ARTIKEL_KOPIEREN_VERTRETERPROVISION";
	public static final String ARTIKEL_KOPIEREN_MINUTENFAKTOR1 = "ARTIKEL_KOPIEREN_MINUTENFAKTOR1";
	public static final String ARTIKEL_KOPIEREN_MINUTENFAKTOR2 = "ARTIKEL_KOPIEREN_MINUTENFAKTOR2";
	public static final String ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG = "ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG";
	public static final String ARTIKEL_KOPIEREN_VERKAUFSEAN = "ARTIKEL_KOPIEREN_VERKAUFSEAN";
	public static final String ARTIKEL_KOPIEREN_WARENVERKEHRSNUMMER = "ARTIKEL_KOPIEREN_WARENVERKEHRSNUMMER";
	public static final String ARTIKEL_KOPIEREN_RABATTIERBAR = "ARTIKEL_KOPIEREN_RABATTIERBAR";
	public static final String ARTIKEL_KOPIEREN_GARANTIEZEIT = "ARTIKEL_KOPIEREN_GARANTIEZEIT";
	public static final String ARTIKEL_KOPIEREN_FARBCODE = "ARTIKEL_KOPIEREN_FARBCODE";
	public static final String ARTIKEL_KOPIEREN_BESTELLMENGENEINHEIT = "ARTIKEL_KOPIEREN_BESTELLMENGENEINHEIT";
	public static final String ARTIKEL_KOPIEREN_ERSATZARTIKEL = "ARTIKEL_KOPIEREN_ERSATZARTIKEL";
	public static final String ARTIKEL_KOPIEREN_URSPRUNGSLAND = "ARTIKEL_KOPIEREN_URSPRUNGSLAND";

	public static final String ARTIKEL_KOPIEREN_KATALOG = "ARTIKEL_KOPIEREN_KATALOG";
	public static final String ARTIKEL_KOPIEREN_VKPREISE = "ARTIKEL_KOPIEREN_VKPREISE";
	public static final String ARTIKEL_KOPIEREN_EKPREISE = "ARTIKEL_KOPIEREN_EKPREISE";
	public static final String ARTIKEL_KOPIEREN_KOMMENTARE = "ARTIKEL_KOPIEREN_KOMMENTARE";
	public static final String ARTIKEL_KOPIEREN_EIGENSCHAFTEN = "ARTIKEL_KOPIEREN_EIGENSCHAFTEN";
	public static final String ARTIKEL_KOPIEREN_TECHNIK_EIGENSCHAFTEN = "ARTIKEL_KOPIEREN_TECHNIK_EIGENSCHAFTEN";

	public static final String ARTIKEL_KOPIEREN_BREITE = "ARTIKEL_KOPIEREN_BREITE";
	public static final String ARTIKEL_KOPIEREN_HOEHE = "ARTIKEL_KOPIEREN_HOEHE";
	public static final String ARTIKEL_KOPIEREN_TIEFE = "ARTIKEL_KOPIEREN_TIEFE";

	public static final String ARTIKEL_KOPIEREN_BAUFORM = "ARTIKEL_KOPIEREN_BAUFORM";
	public static final String ARTIKEL_KOPIEREN_VERPACKUNGSART = "ARTIKEL_KOPIEREN_VERPACKUNGSART";

	public static final String ARTIKEL_KOPIEREN_AUFSCHLAG = "ARTIKEL_KOPIEREN_AUFSCHLAG";
	public static final String ARTIKEL_KOPIEREN_SOLLVERKAUF = "ARTIKEL_KOPIEREN_SOLLVERKAUF";

	public static final String ARTIKEL_KOPIEREN_RASTERLIEGEND = "ARTIKEL_KOPIEREN_RASTERLIEGEND";
	public static final String ARTIKEL_KOPIEREN_RASTERSTEHEND = "ARTIKEL_KOPIEREN_RASTERSTEHEND";
	public static final String ARTIKEL_KOPIEREN_HOCHSTELLEN = "ARTIKEL_KOPIEREN_HOCHSTELLEN";
	public static final String ARTIKEL_KOPIEREN_HOCHSETZEN = "ARTIKEL_KOPIEREN_HOCHSETZEN";
	public static final String ARTIKEL_KOPIEREN_ANTISTATIC = "ARTIKEL_KOPIEREN_ANTISTATIC";

	public static final String ARTIKEL_KOPIEREN_INDEX = "ARTIKEL_KOPIEREN_INDEX";
	public static final String ARTIKEL_KOPIEREN_REVISION = "ARTIKEL_KOPIEREN_REVISION";

	public static final String ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE = "ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE";
	public static final String ARTIKEL_KOPIEREN_SNRBEHAFTET = "ARTIKEL_KOPIEREN_SNRBEHAFTET";
	public static final String ARTIKEL_KOPIEREN_CHNRBEHAFTET = "ARTIKEL_KOPIEREN_CHNRBEHAFTET";
	public static final String ARTIKEL_KOPIEREN_URSPRUNGSTEIL_VERLINKEN = "ARTIKEL_KOPIEREN_URSPRUNGSTEIL_VERLINKEN";

	public static final String ARTIKEL_LOG_NUMMER = "NUMMER";
	public static final String ARTIKEL_LOG_BEZEICHNUNG = "BEZEICHNUNG";
	public static final String ARTIKEL_LOG_KURZBEZEICHNUNG = "KURZBEZEICHNUNG";
	public static final String ARTIKEL_LOG_ZUSATZBEZ = "ZUSATZBEZ";
	public static final String ARTIKEL_LOG_ZUSATZBEZ2 = "ZUSATZBEZ2";
	public static final String ARTIKEL_LOG_EINHEIT = "EINHEIT";
	public static final String ARTIKEL_LOG_MWSTSATZ = "MWSTSATZ";
	public static final String ARTIKEL_LOG_HERSTELLER = "HERSTELLER";
	public static final String ARTIKEL_LOG_ARTIKELART = "ARTIKELART";
	public static final String ARTIKEL_LOG_REVISION = "REVISION";
	public static final String ARTIKEL_LOG_BESTELLEINHEIT = "BESTELLEINHEIT";
	public static final String ARTIKEL_LOG_UMRECHNUNGSFAKTOR = "UMRECHNUNGSFAKTOR";
	public static final String ARTIKEL_LOG_LAGERMINDEST = "LAGERMINDEST";
	public static final String ARTIKEL_LOG_GESTPREIS = "GESTPREIS";
	public static final String ARTIKEL_LOG_LAGERSOLL = "LAGERSOLL";
	public static final String ARTIKEL_LOG_REFERENZNUMMER = "REFERENZNUMMER";
	public static final String ARTIKEL_LOG_INDEX = "INDEX";
	public static final String ARTIKEL_LOG_ARTIKELKLASSE = "ARTIKELKLASSE";
	public static final String ARTIKEL_LOG_ARTIKELGRUPPE = "ARTIKELGRUPPE";
	public static final String ARTIKEL_LOG_SHOPGRUPPE = "SHOPGRUPPE";
	public static final String ARTIKEL_LOG_LIEFERGRUPPE = "LIEFERGRUPPE";
	public static final String ARTIKEL_LOG_VERSTECKT = "VERSTECKT";
	public static final String ARTIKEL_LOG_SNRTRAGEND = "SNTRAGEND";
	public static final String ARTIKEL_LOG_CHNRTRAGEND = "CHNRTRAGEND";
	public static final String ARTIKEL_LOG_LAGERBEWIRTSCHAFTET = "LAGERBEWIRTSCHAFTET";
	public static final String ARTIKEL_LOG_NUR_ZUR_INFO = "NUR_ZUR_INFO";
	public static final String ARTIKEL_LOG_REINE_MANNZEIT = "REINE_MANNZEIT";
	public static final String ARTIKEL_LOG_LETZTE_WARTUNG = "LETZTE_WARTUNG";
	public static final String ARTIKEL_LOG_ARTIKELSPERREN_SPERRE = "ARTIKELSPERREN_SPERRE";
	public static final String ARTIKEL_LOG_ARTIKELSPERREN_GRUND = "ARTIKELSPERREN_GRUND";
	public static final String ARTIKEL_LOG_MATERIAL = "MATERIAL";
	public static final String ARTIKEL_LOG_MATERIALGEWICHT = "MATERIALGEWICHT";
	public static final String ARTIKEL_LOG_ZUSAMMENGEFUEHRT = "ZUSAMMENGEFUEHRT";
	public static final String ARTIKEL_LOG_FREIGABE_ZURUECKGENOMMEN = "FREIGABE_ZURUECKGENOMMEN";
	public static final String ARTIKEL_LOG_FREIGABE_PERSON = "FREIGABE_PERSON";
	public static final String ARTIKEL_LOG_FREIGABE_ZEITPUNKT = "FREIGABE_ZEITPUNKT";
	public static final String ARTIKEL_LOG_BEWILLIGUNGSPFLICHTIG = "BEWILLIGUNGSPFLICHTIG";
	public static final String ARTIKEL_LOG_MELDEPFLICHTIG = "MELDEPFLICHTIG";

	public static final String ARTIKEL_LOG_WAFFENKALIBER = "WAFFENKALIBER";
	public static final String ARTIKEL_LOG_WAFFENTYP = "WAFFENTYP";
	public static final String ARTIKEL_LOG_WAFFENTYPFEIN = "WAFFENTYPFEIN";
	public static final String ARTIKEL_LOG_WAFFENZUSATZ = "WAFFENZUSATZ";
	public static final String ARTIKEL_LOG_WAFFENAUSFUEHRUNG = "WAFFENAUSFUEHRUNG";
	public static final String ARTIKEL_LOG_WAFFENKATEGORIE = "WAFFENKATEGORIE";
	public static final String ARTIKEL_LOG_GEWICHTKG = "GEWICHTKG";
	public static final String ARTIKEL_LOG_FERTIGUNGSSATZGROESSE = "FERTIGUNGSSATZGROESSE";
	public static final String ARTIKEL_LOG_MAXFERTIGUNGSSATZGROESSE = "MAXFERTIGUNGSSATZGROESSE";

	public final int SNRCHNR_OHNE = 0;
	public final int SNRCHNR_SNRBEHAFTET = 1;
	public final int SNRCHNR_CHNRBEHAFTET = 2;

	public static final Pattern patternHerstellerkuerzel = Pattern.compile("[A-Z][0-9]+");

	public Integer createArtikel(ArtikelDto artikelDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikel(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateArtikel(ArtikelDto artikelDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public VerpackungsmittelDto verpackungsmittelfindByCNrMandantCNrOhneExc(String cNr, TheClientDto theClientDto);

	public ArtikelDto artikelFindByPrimaryKeySmall(Integer iId, TheClientDto theClientDto);

	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer iId, TheClientDto theClientDto);

	public ArtikelsprDto getDefaultArtikelbezeichnungen(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByCNr(String cNr, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByCNrMandantCNrOhneExc(String cNr, String mandantCnr);

	public ArtikelDto artikelFindByCNrOhneExc(String cNr, TheClientDto theClientDto) throws RemoteException;

	public ArtikelDto[] artikelFindByCNrOhneExcAlleMandanten(String cNr) throws RemoteException;

	public String getHerstellercode(Integer partnerIId, TheClientDto theClientDto) throws RemoteException;

	public Integer createArtkla(ArtklaDto artklaDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeArtkla(Integer iId) throws EJBExceptionLP, RemoteException;

	public void updateArtkla(ArtklaDto artklaDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtklaDto[] artklaFindAll(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtklaDto[] artklaFindByMandantCNr(TheClientDto theClientDto);

	public ArtgruDto[] artgruFindByMandantCNr(TheClientDto theClientDto);

	public ArtklaDto artklaFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public Integer createArtgru(ArtgruDto artgruDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeArtgru(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateArtgru(ArtgruDto artgruDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtgruDto getLetzteVatergruppe(Integer artgruIId) throws RemoteException;

	public ArtklaDto getLetzteVaterklasse(Integer artklaIId) throws RemoteException;

	public ArtgruDto artgruFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public ArtgruDto[] artgruFindAll() throws EJBExceptionLP, RemoteException;

	public void alleSIwerteNachtragen(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createKatalog(KatalogDto katalogDto) throws EJBExceptionLP, RemoteException;

	public void removeKatalog(KatalogDto dto) throws EJBExceptionLP, RemoteException;

	public void updateKatalog(KatalogDto katalogDto) throws EJBExceptionLP, RemoteException;

	public KatalogDto katalogFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public KatalogDto katalogFindByArtikelIIdCKatalog(Integer iId, String cKatalog)
			throws EJBExceptionLP, RemoteException;

	public void artikellieferantAlsErstesReihen(Integer artikelIId, Integer artikellieferantIId);

	public Integer createArtikellieferant(ArtikellieferantDto artikellieferantDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikellieferant(ArtikellieferantDto dto) throws EJBExceptionLP, RemoteException;

	public void vertauscheArtikellieferanten(Integer iiDLieferant1, Integer iIdLieferant2, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheArtikelsperren(Integer iId1, Integer iId2);

	public ArrayList<Integer> getAlleVorgaengerArtikel(Integer artikelIId);

	public boolean sindVorschlagstexteVorhanden();

	public HashMap getAllSperrenIcon(TheClientDto theClientDto);

	public void updateArtikellieferant(ArtikellieferantDto artLiefDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikellieferantImpl(ArtikellieferantDto artLiefDtoI, boolean zuschnittsartikelNeuBerechnen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	// exccatch: hier immer EJBExceptionLP deklarieren
	public ArtikellieferantDto artikellieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByArtikelIId(Integer artikelIId, TheClientDto theClientDto);

	public void preiseAusAnfrageRueckpflegen(Integer anfrageIId, Integer anfragepositionlieferdatenIId,
			boolean bStaffelnLoeschen, boolean bLieferantVorreihen, boolean bAlsStaffelpreisRueckpflegen,
			TheClientDto theClientDto);

	public EinkaufseanDto einkaufseanFindByCEan(String cEan) throws RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByLieferantIId(Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByLieferantIIdOhneExc(Integer lieferantIId,
			TheClientDto theClientDto) throws RemoteException;

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(Integer artikelIId,
			Integer lieferantIId, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIIdFMenge(
			Integer artikellieferantIId, BigDecimal fMenge, java.sql.Date dDatum);

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(Integer artikelIId,
			Integer lieferantIId, String cWunschwaehrung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto artikellieferantFindByIIdInWunschwaehrung(Integer artikellieferantIId,
			String cWunschwaehrung, TheClientDto theClientDto);

	public Integer createHersteller(HerstellerDto herstellerDto) throws RemoteException, EJBExceptionLP;

	public void removeHersteller(Integer iId) throws RemoteException, EJBExceptionLP;

	public Map<?, ?> getAllSprArtikelarten(String cNrSpracheI) throws EJBExceptionLP, RemoteException;

	public void updateHersteller(HerstellerDto herstellerDto) throws RemoteException, EJBExceptionLP;

	public HerstellerDto herstellerFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public HerstellerDto[] herstellerFindByPartnerIId(Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public String pruefeCSVImport(ArtikelImportDto[] daten, boolean bestehendeArtikelUeberschreiben,
			TheClientDto theClientDto) throws RemoteException;

	public HerstellerDto[] herstellerFindByPartnerIIdOhneExc(Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException;

	public void createArtikelart(ArtikelartDto artikelartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikelart(ArtikelartDto artikelartDto) throws EJBExceptionLP, RemoteException;

	public void updateArtikelart(ArtikelartDto artikelartDto) throws EJBExceptionLP, RemoteException;

	public ArtikelartDto artikelartFindByPrimaryKey(String cNr) throws EJBExceptionLP, RemoteException;

	public ArtikelartDto[] artikelartFindAll() throws EJBExceptionLP, RemoteException;

	public ArtikelDto getErsatzartikel(Integer artikelIId, TheClientDto theClientDto) throws RemoteException;

	public Integer createArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws RemoteException, EJBExceptionLP;

	public void updateArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateArtikellieferantstaffels(ArtikellieferantstaffelDto[] artikellieferantstaffelDtos,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public ArtikellieferantDto getArtikelEinkaufspreisDesBevorzugtenLieferanten(Integer artikelIId, BigDecimal fMenge,
			String waehrungCNr, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto getArtikelEinkaufspreisDesBevorzugtenLieferantenZuDatum(Integer artikelIId,
			BigDecimal fMenge, String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId, Integer lieferantIId, BigDecimal fMenge,
			String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto);

	public ArtikellieferantDto getArtikelEinkaufspreisMitOptionGebinde(Integer artikelIId, Integer lieferantIId,
			BigDecimal fMenge, String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId,
			TheClientDto theClientDto);

	public ArtikellieferantDto getArtikelEinkaufspreisEinesLieferantenEinerBestellung(Integer artikelIId,
			Integer bestellungIId, BigDecimal fMenge, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikellieferantstaffelDto artikellieferantstaffelFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIId(Integer artikellieferantIId)
			throws EJBExceptionLP, RemoteException;

	public String formatArtikelbezeichnungEinzeiligOhneExc(Integer iIdArtikelI, Locale locBezeichnungI)
			throws RemoteException;

	public String baueArtikelBezeichnungMehrzeilig(Integer iIdArtikelI, String cNrPositionsartI,
			String cBezUebersteuertI, String cZBezUebersteuertI, boolean bIncludeCNrI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String baueArtikelBezeichnungMehrzeiligOhneExc(Integer iIdArtikelI, String cNrPositionsartI,
			String cBezUebersteuertI, String cZBezUebersteuertI, boolean bIncludeCNrI, Locale locale,
			TheClientDto theClientDto) throws RemoteException;

	public Node getItemAsNode(Document docI, Integer iIdArtikelI, String idUser) throws RemoteException;

	public String getItemAsStringDocumentWS(String sArtikelI, String idUser) throws RemoteException;

	public Integer createFarbcode(FarbcodeDto farbcodeDto) throws RemoteException, EJBExceptionLP;

	public void removeFarbcode(FarbcodeDto dto) throws RemoteException, EJBExceptionLP;

	public void updateFarbcode(FarbcodeDto farbcodeDto) throws RemoteException, EJBExceptionLP;

	public FarbcodeDto farbcodeFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public String generiereNeueArtikelnummer(String beginnArtikelnummer, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikelsprDto artikelsprFindByArtikelIIdLocaleCNrOhneExc(Integer artikelIId, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Object[] kopiereArtikel(Integer artikelIId, String artikelnummerNeu, java.util.HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createSperren(SperrenDto sperrenDto, TheClientDto theClientDto) throws RemoteException;

	public void removeSperren(SperrenDto sperrenDto) throws RemoteException;

	public void updateSperren(SperrenDto sperrenDto) throws RemoteException;

	public SperrenDto sperrenFindByPrimaryKey(Integer iId) throws RemoteException;

	public Integer createArtikelsperren(ArtikelsperrenDto artikelsperrenDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeArtikelsperren(ArtikelsperrenDto artikelsperrenDto, TheClientDto theClientDto)
			throws RemoteException;

	public void updateArtikelsperren(ArtikelsperrenDto artikelsperrenDto, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikelsperrenDto artikelsperrenFindByPrimaryKey(Integer iId) throws RemoteException;

	public ArtikelsperrenDto[] artikelsperrenFindByArtikelIId(Integer artikelId) throws EJBExceptionLP, RemoteException;

	public void updateTrumphtopslog(String artikelnummer, String kurzbezeichnungMaterial, String importfileName,
			BigDecimal gewicht, long iBearbeitsungszeit, BigDecimal laserkostenProStunde, Integer lagerIId,
			String mandantCNr, boolean kalkulationsart1, int mehrverbrauchfuerlaserinmm, double breiteArtikel,
			double laengeArtikel, Double hoeheArtikel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createZugehoerige(ZugehoerigeDto zugehoerigeDto) throws RemoteException;

	public void erzeugeTrumphTopsLogeintrag(TrumphtopslogDto ttlogDto) throws RemoteException;

	public void removeZugehoerige(ZugehoerigeDto zugehoerigeDto) throws RemoteException;

	public void updateZugehoerige(ZugehoerigeDto zugehoerigeDto) throws RemoteException;

	public ZugehoerigeDto zugehoerigeFindByPrimaryKey(Integer iId) throws RemoteException;

	public SperrenDto sperrenFindBDurchfertigung(TheClientDto theClientDto);

	public Integer[] getZugehoerigeArtikel(Integer artikelIId) throws EJBExceptionLP, RemoteException;

	public ArtikelsperrenDto artikelsperrenFindByArtikelIIdSperrenIIdOhneExc(Integer artikelId, Integer sperrenlId);

	public String getArtikelsperrenText(Integer artikelIId) throws RemoteException;

	public Integer createEinkaufsean(EinkaufseanDto einkaufseanDto) throws RemoteException;

	public void removeEinkaufsean(EinkaufseanDto dto) throws RemoteException;

	public void updateEinkaufsean(EinkaufseanDto einkaufseanDto) throws RemoteException;

	public EinkaufseanDto einkaufseanFindByPrimaryKey(Integer iId) throws RemoteException;

	public List<EinkaufseanDto> einkaufseanFindByArtikelIId(Integer artikelIId) throws RemoteException;

	public ArtikellieferantDto getGuenstigstenEKPreis(Integer artikelIId, BigDecimal bdMenge, java.sql.Date zeitpunkt,
			String waehrungCNr, Integer lieferantIIdVergleich, TheClientDto theClientDto);

	public ArtikelDto[] artikelFindSpecial(String bauteil, String bauform) throws RemoteException;

	public Integer createVerleih(VerleihDto verleihDto);

	public Integer createVorschlagstext(VorschlagstextDto vorschlagstextDto, TheClientDto theClientDto);

	public void updateVorschlagstext(VorschlagstextDto vorschlagstextDto, TheClientDto theClientDto);

	public VorschlagstextDto vorschlagstextFindByPrimaryKey(Integer iId);

	public void removeVorschlagstext(VorschlagstextDto dto);

	public void removeVerleih(VerleihDto dto);

	public void updateVerleih(VerleihDto verleihDto);

	public VerleihDto verleihFindByPrimaryKey(Integer iId);

	public Map getAllVerleih();

	public Map getAllSprArtgru(TheClientDto theClientDto);

	public Integer createWebshop(WebshopDto dto);

	public WebshopDto webshopFindByPrimaryKey(Integer iId);

	public void updateWebshop(WebshopDto dto);

	public void removeWebshop(WebshopDto dto);

	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public Integer createShopgruppe(ShopgruppeDto dto, TheClientDto theClientDto);

	public Integer createShopgruppeVor(ShopgruppeDto dto, Integer vorIId, TheClientDto theClientDto);

	public void removeShopgruppe(Integer iId);

	public void updateShopgruppe(ShopgruppeDto shopgruppeDto, TheClientDto theClientDto);

	public Integer createShopgruppewebshop(ShopgruppewebshopDto dto, TheClientDto theClientDto);

	public void removeShopgruppewebshop(ShopgruppewebshopDto dto);

	public ShopgruppewebshopDto shopgruppewebshopFindByPrimaryKey(Integer iId);

	public ShopgruppewebshopDto[] shopgruppeFindByWebshopId(Integer webshopIId);

	public void updateShopgruppewebshop(ShopgruppewebshopDto dto);

	public ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr, TheClientDto theClientDto);

	public Map getAlleGueltigenStaffelneinesLieferanten(Integer artikellieferantIId, java.sql.Date dDatum,
			String waehrungCNrGewuenschteWaehrung, TheClientDto theClientDto);

	public void updateArtikelshopgruppe(ArtikelshopgruppeDto dto, TheClientDto theClientDto);

	public ArtikelshopgruppeDto artikelshopgruppeFindByPrimaryKey(Integer iId);

	public Integer createArtikelshopgruppe(ArtikelshopgruppeDto dto, TheClientDto theClientDto);

	public void removeArtikelshopgruppe(ArtikelshopgruppeDto dto);

	public Integer[] getBereitsVerwendeteShopgruppen(Integer artikelIId);

	public void vertauscheShopgruppen(Integer pos1, Integer pos2) throws EJBExceptionLP;

	public List<ArtgruDto> artgruFindByMandantCNrSpr(TheClientDto theClientDto);

	public void setzeArtikelSNRtragendOhneWeitereAktion(Integer artikelIId);

	public byte[] getXLSForPreispflege(Integer artikelgruppeIId, boolean mitUntergruppen, Integer artikelklasseIId,
			boolean mitUnterklassen, Integer shopgruppeIId, boolean mitShopuntergruppen, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten, TheClientDto theClientDto);

	public void preiseXLSForPreispflege(byte[] xlsFile, String cBegruendung, TheClientDto theClientDto);

	public ArrayList<KeyValue> getListeDerArtikellieferanten(Integer bestellvorschlagIId, BigDecimal nMenge,
			TheClientDto theClientDto);

	public Integer createReach(ReachDto dto);

	public Integer createRohs(RohsDto dto);

	public Integer createAutomotive(AutomotiveDto dto);

	public Integer createMedicale(MedicalDto dto);

	public void updateReach(ReachDto dto);

	public void updateAutomotive(AutomotiveDto dto);

	public void updateMedical(MedicalDto dto);

	public void updateRohs(RohsDto dto);

	public void removeReach(ReachDto dto);

	public void removeRohs(RohsDto dto);

	public void removeAutomotive(AutomotiveDto dto);

	public void removeMedical(MedicalDto dto);

	public ReachDto reachFindByPrimaryKey(Integer iId);

	public RohsDto rohsFindByPrimaryKey(Integer iId);

	public AutomotiveDto automotiveFindByPrimaryKey(Integer iId);

	public MedicalDto medicalFindByPrimaryKey(Integer iId);

	public ArrayList<String> getVorgaengerArtikel(Integer artikelIId);

	public void removeVorzug(VorzugDto dto);

	public VorzugDto vorzugFindByPrimaryKey(Integer iId);

	public Integer createVorzug(VorzugDto dto);

	public void updateVorzug(VorzugDto dto);

	public Map getAllVorzug(TheClientDto theClientDto);

	public void wandleHandeingabeInArtikelUm(Integer positionIId, int iArt, String neueArtikelnummer,
			TheClientDto theClientDto);

	public void updateAllergen(AlergenDto dto);

	public Integer createAllergen(AlergenDto dto);

	public AlergenDto allergenFindByPrimaryKey(Integer iId);

	public void removeAllergen(AlergenDto dto);

	public void vertauscheAlergen(Integer iId1, Integer iId2);

	public Integer createArtikelallergen(ArtikelalergenDto dto, TheClientDto theClientDto);

	public void updateArtikelallergen(ArtikelalergenDto dto, TheClientDto theClientDto);

	public ArtikelalergenDto artikelallergenFindByPrimaryKey(Integer iId);

	public void removeArtikelallergen(ArtikelalergenDto dto);

	public ArtikelalergenDto[] artikelallergenFindByArtikelIId(Integer artikelIId);

	public AlergenDto[] allergenFindByMandantCNr(TheClientDto theClientDto);

	public ArtikellieferantDto artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(Integer artikelIId,
			Integer lieferantIId, java.sql.Timestamp tPreisgueltigab, TheClientDto theClientDto);

	public void siWertNachtragen(Integer artikelIId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public int HANDARTIKEL_UMWANDELN_ANGEBOT = 0;
	public int HANDARTIKEL_UMWANDELN_ANFRAGE = 1;
	public int HANDARTIKEL_UMWANDELN_AUFTRAG = 2;
	public int HANDARTIKEL_UMWANDELN_BESTELLUNG = 3;
	public int HANDARTIKEL_UMWANDELN_STUECKLISTEPOSITION = 4;
	public int HANDARTIKEL_UMWANDELN_AGSTKLPOSITION = 5;
	public int HANDARTIKEL_UMWANDELN_EINKAUFSANGEBOTPOSITION = 6;

	/**
	 * Aktualisiert einen Artikel im Artikelstamm, wenn er sich zum selektierten
	 * Artikel des Intelligenten St&uuml;cklistenimports unterscheidet.
	 * 
	 * @param result       Importresult des St&uuml;cklistenimports
	 * @param theClientDto der aktuelle Benutzer
	 * @throws RemoteException TODO
	 */
	public void updateArtikelAusImportResult(IStklImportResult result, TheClientDto theClientDto)
			throws RemoteException;

	public void updateArtikellieferantOrCreateIfNotExist(Integer artikelIId, Integer lieferantIId,
			String lieferantenArtikelCNr, BigDecimal nettopreis, TheClientDto theClientDto);

	public GebindeDto gebindeFindByPrimaryKey(Integer iId);

	public void removeGebinde(GebindeDto dto);

	public void updateGebinde(GebindeDto dto);

	public Integer createGebinde(GebindeDto dto);

	public ArrayList<GebindeDto> getGebindeEinesArtikelsUndEinesLieferanten(Integer artikelIId, Integer lieferantIId,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto);

	public void erhoeheAlleStaffelnEinesArtikellieferant(Integer artikellieferantIId, Date tGueltigab,
			BigDecimal nProzent, TheClientDto theClientDto);

	public void removeErsatztypen(ErsatztypenDto dto);

	public Integer createErsatztypen(ErsatztypenDto dto, TheClientDto theClientDto);

	public void updateErsatztypen(ErsatztypenDto dto, TheClientDto theClientDto);

	public ErsatztypenDto ersatztypenFindByPrimaryKey(Integer iId);

	public ErsatztypenDto[] ersatztypenFindByArtikelIId(Integer artikelId);

	public ArtikelDto artikelFindBy4VendingIdOhneExc(String fourVendingId, TheClientDto theClientDto);

	public List<ArtikelDto> artikelFindByMandantCNr4VendingIdNotNull(String mandantCNr, TheClientDto theClientDto);

	public VendidataArticleExportResult exportiere4VendingArtikel(boolean checkOnly, TheClientDto theClientDto)
			throws RemoteException;

	public Integer generiere4VendingId(Integer artikelIId, TheClientDto theClientDto) throws RemoteException;

	public void delete4VendingId(Integer artikelIId, TheClientDto theClientDto) throws RemoteException;

	public WerkzeugDto werkzeugFindByPrimaryKey(Integer iId);

	public void updateWerkzeug(WerkzeugDto dto);

	public Integer createWerkzeug(WerkzeugDto dto);

	public void removeWerkzeug(WerkzeugDto dto);

	public VerschleissteilDto verschleissteilFindByPrimaryKey(Integer iId);

	public Integer createVerschleissteil(VerschleissteilDto dto);

	public void removeVerschleissteil(VerschleissteilDto dto);

	public void updateVerschleissteil(VerschleissteilDto dto);

	public Map getAllVerschleissteile(Integer werkzeugIId);

	ArtikelMitVerpackungsgroessenDto artikelFindByEanMandantCnr(String ean, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikellieferantDto[] artikellieferantfindByArtikelIIdTPreisgueltigab(Integer artikelIId,
			java.sql.Date tPreisGuelitgab, TheClientDto theClientDto) throws EJBExceptionLP;

	public void removeVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto);

	public VerschleissteilwerkzeugDto verschleissteilwerkzeugFindByPrimaryKey(Integer iId);

	public Integer createVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto);

	public void updateVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto);

	public VerschleissteilwerkzeugDto[] verschleissteilwerkzeugFindByVerschleissteilIId(Integer verschleissteilIId);

	public void artikelAenderungLoggen(Integer artikelIId, String key, String von, String nach,
			TheClientDto theClientDto);

	public Integer createVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto);

	public void removeVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto);

	public void updateVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto);

	public VerpackungsmittelDto verpackungsmittelFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public VerpackungsmittelDto verpackungsmittelFindByPrimaryKeyUndLocale(Integer iId, String localeCNr,
			TheClientDto theClientDto);

	List<ArtikelDto> artikelFindByCKBezOhneExc(String cKbez, TheClientDto theClientDto);

	public void updateArtgrumandant(Integer artgruIId, Integer kontoIId, TheClientDto theClientDto);

	/**
	 * Artikel &uuml;ber die Lieferantenartikelnr finden
	 * 
	 * @param artikelnrlieferant die Artikelnummer des Lieferanten
	 * @param lieferantId        der Lieferant
	 * @param theClientDto       der angemeldete Benutzer
	 * @return der (erste) Artikel der der Lieferantenartikelnummer entspricht, oder
	 *         null
	 */
	ArtikelDto artikelFindByArtikelnrlieferant(String artikelnrlieferant, Integer lieferantId,
			TheClientDto theClientDto);

	/**
	 * Liste aller Artikel deren Herstellerartikelnummer der gesuchten Artikelnummer
	 * entspricht</br>
	 * <p>
	 * Es wird der zentrale Artikelstamm unterst&uuml;tzt
	 * </p>
	 * 
	 * @param artikelnrhersteller ist die Artikelnummer des Herstellers
	 * @param theClientDto        der angemeldete Benutzer
	 * @return eine (leere) Liste aller Artikel, die die gesuchte
	 *         Herstellerartikelnummer haben.
	 */
	List<ArtikelDto> artikelFindByArtikelnrHersteller(String artikelnrhersteller, TheClientDto theClientDto);

	public boolean sindArtikelgruppenEingeschraenkt(TheClientDto theClientDto);

	String generiereGTIN13VerkaufseanNummer(Integer artikelIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public ErsatztypenDto[] ersatztypenfindByArtikelIIdErsatz(Integer artikelIIdErsatz);

	public KopfGruppeMitUntergruppen holeAlleArtikelgruppen(Integer artikelgruppeIId);

	public KopfGruppeMitUntergruppen holeAlleArtikelklassen(Integer artikelklasseIId);

	public KopfGruppeMitUntergruppen holeAlleShopgruppen(Integer shopgruppeIId);

	public void removeArtikelspr(Integer artikelIId, String locale, TheClientDto theClientDto);

	WebshopConnectionDto webshopConnectionFindByPrimaryKey(WebshopId shopId);

	WebshopShopgruppeDto webshopShopgruppeFindByShopShopgruppe(WebshopId shopId, ShopgruppeId shopgruppeId);

	WebshopShopgruppeDto webshopShopgruppeFindByShopShopgruppeNoExc(WebshopId shopId, ShopgruppeId shopgruppeId);

	WebshopShopgruppeDto webshopShopgruppeFindByShopExternalIdNull(WebshopId shopId, String externalId);

	Integer createWebshopShopgruppe(WebshopShopgruppeDto wssgDto);

	void removeWebshopShopgruppe(Integer webshopShopgruppeId);

	WebshopArtikelDto webshopArtikelFindByShopArtikel(WebshopId shopId, ArtikelId artikelId);

	WebshopArtikelDto webshopArtikelFindByShopArtikelNoExc(WebshopId shopId, ArtikelId artikelId);

	WebshopArtikelDto webshopArtikelFindByShopExternalIdNull(WebshopId shopId, String externalId);

	Integer createWebshopArtikel(WebshopArtikelDto wsaDto);

	WebshopArtikelPreislisteDto webshopPreislisteFindByShopPreisliste(WebshopId shopId, PreislisteId preislisteId);

	WebshopArtikelPreislisteDto webshopPreislisteFindByShopPreislisteNoExc(WebshopId shopId, PreislisteId preislisteId);

	Integer createWebshopPreisliste(WebshopArtikelPreislisteDto wspDto);

	WebshopMwstsatzbezDto webshopMwstsatzbezFindByShopMwstsatzbez(WebshopId shopId, MwstsatzbezId mwstsatzId);

	WebshopMwstsatzbezDto webshopMwstsatzbezFindByShopMwstsatzbezNoExc(WebshopId shopId, MwstsatzbezId mwstsatzbezId);

	Integer createWebshopMwstsatzbez(WebshopMwstsatzbezDto wsmDto);

	Integer createWebshopKunde(WebshopKundeDto wspDto);

	WebshopKundeDto webshopKundeFindByShopKundeNoExc(WebshopId shopId, KundeId kundeId);

	WebshopKundeDto webshopKundeFindByShopKunde(WebshopId shopId, KundeId kundeId);

	void updateWebshopKunde(WebshopKundeDto wskDto);

	WebshopKundeDto webshopKundeFindByShopExternalIdNull(WebshopId shopId, String externalId);

	Map<String, String> getAllSprWebshoparten(String cNrSpracheI) throws EJBExceptionLP;

	public Integer kopiereArtikelFuerDimensionenBestellen(Integer artikelIId, BigDecimal bdPositionsmenge,
			Integer dimension1, Integer dimension2, Integer dimension3, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean gibtEsEKStaffelnZuEinemArtikel(Integer artikelIId);

	List<ArtikelsperrenSperrenDto> artikelsperrenFindByArtikelIIdMitSperren(Integer artikelId) throws EJBExceptionLP;

	public Integer createArtikelMitParameterEinmalartikel(ArtikelDto artikelDto, boolean bEinmalartikel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelDto pruefeObHerstellernummerandererArtikelVerwendet(Integer artikelIId, TheClientDto theClientDto);

	public ArrayList<String> getVerfuegbarkeitErsatztypen(Integer artikelIId, TheClientDto theClientDto);

	WebabfrageArtikellieferantResult aktualisiereArtikellieferantByWebabfrage(
			WebabfrageArtikellieferantProperties properties, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Liefert die Ids aller Artikel, die f&uuml;r den Lieferanten einen Eintrag im
	 * Artikellieferanten haben
	 * 
	 * @param lieferantIId Id des Lieferanten
	 * @return Liste von (eindeutigen) Ids
	 */
	List<Integer> getArtikelIdsArtikellieferantByLieferantIId(Integer lieferantIId);

	/**
	 * Eine Liste aller eingeschr&auml;nkten Artikelgruppen-Ids dieses Clients</br>
	 * <p>
	 * Diese Liste kann leer sein, das bedeutet dann, dass es keine
	 * Einschr&auml;nkungen gibt
	 *
	 * @param theClientDto
	 * @return eine (leere => uneingeschr&auml;nkte) Liste aller Artikelgruppen-Ids,
	 *         auf die dieser Client Zugriff hat.
	 */
	List<Integer> getEingeschraenkteArtikelgruppen(TheClientDto theClientDto);

	/**
	 * Eine Liste aller Artikelgruppen auf die dieser Client zugreifen darf</br>
	 * <p>
	 * Gibt es f&uuml;r diesen Client keine Einschr&auml;nkung, werden alle
	 * Artikelgruppen geliefert, ansonsten nur die, die in der Einschr&auml;nkung
	 * hinterlegt sind.
	 * </p>
	 * 
	 * @param theClientDto
	 * @return eine (leere) Liste aller diesem Client zug&auml;nglichen
	 *         Artikelgruppen
	 */
	List<ArtgruDto> artgruEingeschraenktFindByMandantCNrSpr(TheClientDto theClientDto);

	ArtikelDto artikelFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto);

	public List<ArtikelDto> artikelFindByHerstellernummerausBarcode(String herstellernummer, TheClientDto theClientDto);

	public ArtikelDto artikelFindByEanFuerSchnellerfassung(String ean, TheClientDto theClientDto);

	public ArrayList<String> wandleHandeingabeInBestehendenArtikelUm(Integer positionIId, int iArt, Integer artikelIId,
			TheClientDto theClientDto);

	public void wandleHandeingabeInBestehendenArtikelUmTeil2(Integer positionIId, Integer artikelIId)
			throws RemoteException;

	public Integer gibtEsBereitsEinenBevorzugtenArtikel(String artikelnummer, TheClientDto theClientDto);

	public void vertauscheErsatztypen(Integer iId1, Integer iId2);

	public ArrayList<String> getEKStaffeln(Integer artikellieferantIId, TheClientDto theClientDto);

	public void toggleFreigabe(Integer artikelIId, String cFreigabeZuerueckgenommen, TheClientDto theClientDto);

	public void createArtikelspr(ArtikelsprDto sprDto, TheClientDto theClientDto);

	public void updateArtikelspr(ArtikelsprDto sprDto, TheClientDto theClientDto);

	public String formatArtikelbezeichnungEinzeiligOhneExcUebersteuert(Integer iIdArtikelI, Locale locBezeichnungI,
			String c_bez_uebersteuert, String c_zbez_uebersteuert);

	public ArtikelDto[] artikelfindByCReferenznrMandantCNrOhneExc(String cNr, String mandantCnr);

	/**
	 * Einen Artikel &uuml;ber seine Referenznr finden</br>
	 * <p>
	 * In der Referenznummer werden eventuell enthaltene Trennzeichen wie zum
	 * Beispiel '-' bei der Suche explizit entfernt.
	 * </p>
	 * 
	 * @param value          der zu suchende Wert. Es wird immer nach "like %value"
	 *                       gesucht
	 * @param requiredPrefix wird sofern angegeben als Beginn der Artikelnummer
	 *                       erwartet ("like prefix%"). Damit kann eine eventuell
	 *                       mehrfach vorhandene Referenznummer auf jene Artikel
	 *                       eingeschr&auml;nkt werden, die mit dem Prefix beginnen.
	 * @param theClientDto
	 * @return eine (leere) Liste aller Artikel die die Referenznummer enthalten
	 */
	List<ArtikelDto> artikelFindByReferenzCNrMandantCNr(String value, String requiredPrefix, TheClientDto theClientDto);

	public void pruefeArtikelnummer(String cNr, TheClientDto theClientDto) throws EJBExceptionLP;

	public Integer createLaseroberflaeche(LaseroberflaecheDto dto);

	public LaseroberflaecheDto laseroberflaecheFindByPrimaryKey(Integer iId);

	public void removeLaseroberflaeche(LaseroberflaecheDto dto);

	public void updateLaseroberflaeche(LaseroberflaecheDto dto);
	
	public void preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(Integer artikelIId,
			Integer artikellieferantIId, TheClientDto theClientDto) throws RemoteException;
	public int ichBinZuschittOderBasisArtikel(Integer artikelIId, TheClientDto theClientDto);

	
	HvOptional<ArtikelTruTopsDto> artikelTruTopsFindByPrimaryKeyOhneExc(ArtikelTruTopsId artikelTruTopsId);
	
	ArtikelTruTopsDto artikelTruTopsFindByPrimaryKey(ArtikelTruTopsId artikelTruTopsId);
	
	Integer createArtikelTruTops(ArtikelTruTopsDto dto, TheClientDto theClientDto);
	
	void removeArtikelTruTops(ArtikelTruTopsId artikelTruTopsId);
	
	void updateArtikelTruTops(ArtikelTruTopsDto dto);
	
	HvOptional<ArtikelTruTopsMetadatenDto> artikelTruTopsMetadatenFindByPrimaryKeyNoExc(ArtikelTruTopsMetadatenId id);
	
	ArtikelTruTopsMetadatenDto artikelTruTopsMetadatenFindByPrimaryKey(ArtikelTruTopsMetadatenId id);
	
	Integer createArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenDto dto, TheClientDto theClientDto);
	
	void removeArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenId id);
	
	void updateArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenDto dto);
	
	HvOptional<ArtikelTruTopsDto> artikelTruTopsFindByArtikelId(ArtikelId artikelId);
	
	List<ArtikelTruTopsMetadatenDto> artikelTruTopsMetadatenFindByArtikelId(ArtikelId artikelId);
	
	public ArrayList<String> getTruTopsMetadaten(ArtikelId artikelId, TheClientDto theClientDto);

	/**
	 * Setzt den TruTops-Artikel zur&uuml;ck, um einen Export zu erzwingen.
	 * 
	 * @param artikelTruTopsId Id des TruTops-Artikels
	 */
	void resetArtikelTruTops(ArtikelTruTopsId artikelTruTopsId);

	Map<Integer, Object> objFindByNameClientPrimaryKeys(String methodName,
			Collection<Integer> keys, TheClientDto theClientDto) throws NoSuchMethodException;
	Map<Integer, Object> objFindByNamePrimaryKeys(
			String methodName, Collection<Integer> keys) throws NoSuchMethodException;
	<T extends BaseIntegerKey> Map<T, Object> objFindByNamePrimaryBaseIntegerKeys(
			String methodName, Collection<T> keys) throws NoSuchMethodException;

	void resetArtikelTruTopsByArtikelId(ArtikelId artikelId);	
}
