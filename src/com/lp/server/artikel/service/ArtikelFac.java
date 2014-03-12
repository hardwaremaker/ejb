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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

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

	// Feldlaengen
	public static final int MAX_ARTIKEL_ARTIKELNUMMER = 25;
	public static final int MAX_ARTIKEL_REFERENZNUMMER = 25;
	public static final int MAX_ARTIKEL_VERKAUFEANNR = 15;
	public static final int MAX_ARTIKEL_WARENVERKEHRSNUMMER = 10;
	public static final int MAX_ARTIKEL_ARTIKELBEZEICHNUNG = 40;
	public static final int MAX_ARTIKEL_ZUSATZBEZEICHNUNG = 40;
	public static final int MAX_ARTIKEL_ZUSATZBEZEICHNUNG2 = 40;
	public static final int MAX_ARTIKEL_KURZBEZEICHNUNG = 25;
	public static final int MAX_ARTIKEL_BAUFORM = 20;
	public static final int MAX_ARTIKEL_VERPACKUNGSART = 20;
	public static final int MAX_ARTIKEL_TEXTBREITE = 2;

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

	public static final String ARTIKEL_KOPIEREN_HERSTELLER = "ARTIKEL_KOPIEREN_HERSTELLER";
	public static final String ARTIKEL_KOPIEREN_ARTIKELGRUPPE = "ARTIKEL_KOPIEREN_ARTIKELGRUPPE";
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
	public static final String ARTIKEL_KOPIEREN_POLARISIERT = "ARTIKEL_KOPIEREN_POLARISIERT";

	public static final String ARTIKEL_KOPIEREN_INDEX = "ARTIKEL_KOPIEREN_INDEX";
	public static final String ARTIKEL_KOPIEREN_REVISION = "ARTIKEL_KOPIEREN_REVISION";

	public static final String ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE = "ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE";
	public static final String ARTIKEL_KOPIEREN_SNRBEHAFTET = "ARTIKEL_KOPIEREN_SNRBEHAFTET";
	public static final String ARTIKEL_KOPIEREN_CHNRBEHAFTET = "ARTIKEL_KOPIEREN_CHNRBEHAFTET";

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
	public static final String ARTIKEL_LOG_REFERENZNUMMER = "REFERENZNUMMER";
	public static final String ARTIKEL_LOG_INDEX = "INDEX";
	public static final String ARTIKEL_LOG_ARTIKELKLASSE = "ARTIKELKLASSE";
	public static final String ARTIKEL_LOG_ARTIKELGRUPPE = "ARTIKELGRUPPE";
	public static final String ARTIKEL_LOG_SHOPGRUPPE = "SHOPGRUPPE";
	public static final String ARTIKEL_LOG_LIEFERGRUPPE = "LIEFERGRUPPE";
	public static final String ARTIKEL_LOG_VERSTECKT = "VERSTECKT";
	public static final String ARTIKEL_LOG_NUR_ZUR_INFO = "NUR_ZUR_INFO";
	public static final String ARTIKEL_LOG_REINE_MANNZEIT = "REINE_MANNZEIT";
	public static final String ARTIKEL_LOG_LETZTE_WARTUNG = "LETZTE_WARTUNG";

	public Integer createArtikel(ArtikelDto artikelDto,
			TheClientDto theClientDto);

	public void removeArtikel(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikel(ArtikelDto artikelDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public ArtikelDto artikelFindByPrimaryKeySmall(Integer iId,
			TheClientDto theClientDto);

	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer iId,
			TheClientDto theClientDto);

	public ArtikelsprDto getDefaultArtikelbezeichnungen(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelDto artikelFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCnr);

	public ArtikelDto artikelFindByCNrOhneExc(String cNr,
			TheClientDto theClientDto) throws RemoteException;

	public ArtikelDto[] artikelFindByCNr(String cNr) throws EJBExceptionLP,
			RemoteException;

	public ArtikelDto[] artikelFindByCNrOhneExc(String cNr)
			throws RemoteException;

	public String getHerstellercode(Integer partnerIId,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createArtkla(ArtklaDto artklaDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtkla(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateArtkla(ArtklaDto artklaDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtklaDto[] artklaFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtklaDto[] artklaFindByMandantCNr(TheClientDto theClientDto);

	public ArtgruDto[] artgruFindByMandantCNr(TheClientDto theClientDto);

	public ArtklaDto artklaFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public Integer createArtgru(ArtgruDto artgruDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtgru(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateArtgru(ArtgruDto artgruDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtgruDto getLetzteVatergruppe(Integer artgruIId)
			throws RemoteException;

	public ArtklaDto getLetzteVaterklasse(Integer artklaIId)
			throws RemoteException;

	public ArtgruDto artgruFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtgruDto[] artgruFindAll() throws EJBExceptionLP, RemoteException;

	public void alleSIwerteNachtragen(TheClientDto theClientDto);

	public Integer createKatalog(KatalogDto katalogDto) throws EJBExceptionLP,
			RemoteException;

	public void removeKatalog(KatalogDto dto) throws EJBExceptionLP,
			RemoteException;

	public void updateKatalog(KatalogDto katalogDto) throws EJBExceptionLP,
			RemoteException;

	public KatalogDto katalogFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public KatalogDto katalogFindByArtikelIIdCKatalog(Integer iId,
			String cKatalog) throws EJBExceptionLP, RemoteException;

	public void artikellieferantAlsErstesReihen(Integer artikelIId,
			Integer artikellieferantIId);

	public Integer createArtikellieferant(
			ArtikellieferantDto artikellieferantDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikellieferant(ArtikellieferantDto dto)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheArtikellieferanten(Integer iiDLieferant1,
			Integer iIdLieferant2) throws EJBExceptionLP, RemoteException;

	public void vertauscheArtikelsperren(Integer iId1, Integer iId2);

	public boolean sindVorschlagstexteVorhanden();

	public HashMap getAllSperrenIcon(TheClientDto theClientDto);

	public void updateArtikellieferant(ArtikellieferantDto artLiefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	// exccatch: hier immer EJBExceptionLP deklarieren
	public ArtikellieferantDto artikellieferantFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByArtikelIId(
			Integer artikelIId, TheClientDto theClientDto);

	public void preiseAusAnfrageRueckpflegen(Integer anfrageIId,
			Integer anfragepositionlieferdatenIId, boolean bStaffelnLoeschen,
			boolean bLieferantVorreihen, TheClientDto theClientDto);

	public EinkaufseanDto einkaufseanFindByCEan(String cEan)
			throws RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByLieferantIId(
			Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto[] artikellieferantFindByLieferantIIdOhneExc(
			Integer lieferantIId, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
			Integer artikelIId, Integer lieferantIId, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIId(
			Integer artikelIId, Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
			Integer artikelIId, Integer lieferantIId,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIIdFMenge(
			Integer artikellieferantIId, BigDecimal fMenge, java.sql.Date dDatum);

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(
			Integer artikelIId, Integer lieferantIId, String cWunschwaehrung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto artikellieferantFindByIIdInWunschwaehrung(
			Integer artikellieferantIId, String cWunschwaehrung,
			TheClientDto theClientDto);

	public Integer createHersteller(HerstellerDto herstellerDto)
			throws RemoteException, EJBExceptionLP;

	public void removeHersteller(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public Map<?, ?> getAllSprArtikelarten(String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public void updateHersteller(HerstellerDto herstellerDto)
			throws RemoteException, EJBExceptionLP;

	public HerstellerDto herstellerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public HerstellerDto[] herstellerFindByPartnerIId(Integer iPartnerId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public String pruefeCSVImport(ArtikelImportDto[] daten,
			TheClientDto theClientDto) throws RemoteException;

	public HerstellerDto[] herstellerFindByPartnerIIdOhneExc(
			Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException;

	public void createArtikelart(ArtikelartDto artikelartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeArtikelart(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeArtikelart(ArtikelartDto artikelartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelart(ArtikelartDto artikelartDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelartDto artikelartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public ArtikelartDto[] artikelartFindAll() throws EJBExceptionLP,
			RemoteException;

	public ArtikelDto getErsatzartikel(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeArtikellieferantstaffel(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public void removeArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws RemoteException, EJBExceptionLP;

	public void updateArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateArtikellieferantstaffels(
			ArtikellieferantstaffelDto[] artikellieferantstaffelDtos,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId,
			BigDecimal fMenge, String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId,
			Integer lieferantIId, BigDecimal fMenge, String waehrungCNr,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto);

	public ArtikellieferantstaffelDto artikellieferantstaffelFindByPrimaryKey(
			Integer iId) throws RemoteException, EJBExceptionLP;

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIId(
			Integer artikellieferantIId) throws EJBExceptionLP, RemoteException;

	public String formatArtikelbezeichnungEinzeiligOhneExc(Integer iIdArtikelI,
			Locale locBezeichnungI) throws RemoteException;

	public void importiereArtikel(ArtikelImportDto[] daten,
			boolean bBestehendeArtikelUeberschreiben, TheClientDto theClientDto)
			throws RemoteException;

	public String baueArtikelBezeichnungMehrzeilig(Integer iIdArtikelI,
			String cNrPositionsartI, String cBezUebersteuertI,
			String cZBezUebersteuertI, boolean bIncludeCNrI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String baueArtikelBezeichnungMehrzeiligOhneExc(Integer iIdArtikelI,
			String cNrPositionsartI, String cBezUebersteuertI,
			String cZBezUebersteuertI, boolean bIncludeCNrI, Locale locale,
			TheClientDto theClientDto) throws RemoteException;

	public Node getItemAsNode(Document docI, Integer iIdArtikelI, String idUser)
			throws RemoteException;

	public String getItemAsStringDocumentWS(String sArtikelI, String idUser)
			throws RemoteException;

	public Integer createFarbcode(FarbcodeDto farbcodeDto)
			throws RemoteException, EJBExceptionLP;

	public void removeFarbcode(FarbcodeDto dto) throws RemoteException,
			EJBExceptionLP;

	public void updateFarbcode(FarbcodeDto farbcodeDto) throws RemoteException,
			EJBExceptionLP;

	public FarbcodeDto farbcodeFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public String generiereNeueArtikelnummer(String beginnArtikelnummer,
			TheClientDto theClientDto) throws RemoteException;

	public ArtikelsprDto artikelsprFindByArtikelIIdLocaleCNrOhneExc(
			Integer artikelIId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Object[] kopiereArtikel(Integer artikelIId, String artikelnummerNeu,
			java.util.HashMap zuKopieren,Integer herstellerIIdNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createSperren(SperrenDto sperrenDto,
			TheClientDto theClientDto) throws RemoteException;

	public void removeSperren(SperrenDto sperrenDto) throws RemoteException;

	public void updateSperren(SperrenDto sperrenDto) throws RemoteException;

	public SperrenDto sperrenFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public Integer createArtikelsperren(ArtikelsperrenDto artikelsperrenDto,
			TheClientDto theClientDto) throws RemoteException;

	public void removeArtikelsperren(ArtikelsperrenDto artikelsperrenDto)
			throws RemoteException;

	public void updateArtikelsperren(ArtikelsperrenDto artikelsperrenDto,
			TheClientDto theClientDto) throws RemoteException;

	public ArtikelsperrenDto artikelsperrenFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public ArtikelsperrenDto[] artikelsperrenFindByArtikelIId(Integer artikelId)
			throws EJBExceptionLP, RemoteException;

	public void updateTrumphtopslog(String artikelnummer,
			String kurzbezeichnungMaterial, String importfileName,
			BigDecimal gewicht, long iBearbeitsungszeit,
			BigDecimal laserkostenProStunde, Integer lagerIId,
			String mandantCNr, boolean kalkulationsart1,
			int mehrverbrauchfuerlaserinmm, double breiteArtikel,
			double laengeArtikel, Double hoeheArtikel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createZugehoerige(ZugehoerigeDto zugehoerigeDto)
			throws RemoteException;

	public void erzeugeTrumphTopsLogeintrag(TrumphtopslogDto ttlogDto)
			throws RemoteException;

	public void removeZugehoerige(ZugehoerigeDto zugehoerigeDto)
			throws RemoteException;

	public void updateZugehoerige(ZugehoerigeDto zugehoerigeDto)
			throws RemoteException;

	public ZugehoerigeDto zugehoerigeFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public SperrenDto sperrenFindBDurchfertigung(TheClientDto theClientDto);

	public Integer[] getZugehoerigeArtikel(Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelsperrenDto artikelsperrenFindByArtikelIIdSperrenIIdOhneExc(
			Integer artikelId, Integer sperrenlId);

	public String getArtikelsperrenText(Integer artikelIId)
			throws RemoteException;

	public Integer createEinkaufsean(EinkaufseanDto einkaufseanDto)
			throws RemoteException;

	public void removeEinkaufsean(EinkaufseanDto dto) throws RemoteException;

	public void updateEinkaufsean(EinkaufseanDto einkaufseanDto)
			throws RemoteException;

	public EinkaufseanDto einkaufseanFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public ArtikellieferantDto getGuenstigstenEKPreis(Integer artikelIId,
			BigDecimal bdMenge, java.sql.Date zeitpunkt, String waehrungCNr,
			Integer lieferantIIdVergleich, TheClientDto theClientDto);

	public ArtikelDto[] artikelFindSpecial(String bauteil, String bauform)
			throws RemoteException;

	public Integer createVerleih(VerleihDto verleihDto);

	public Integer createVorschlagstext(VorschlagstextDto vorschlagstextDto,
			TheClientDto theClientDto);

	public void updateVorschlagstext(VorschlagstextDto vorschlagstextDto,
			TheClientDto theClientDto);

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

	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public Integer createShopgruppe(ShopgruppeDto dto, TheClientDto theClientDto);

	public Integer createShopgruppeVor(ShopgruppeDto dto, Integer vorIId,
			TheClientDto theClientDto);

	public void removeShopgruppe(Integer iId);

	public void updateShopgruppe(ShopgruppeDto shopgruppeDto,
			TheClientDto theClientDto);

	public Integer createShopgruppewebshop(ShopgruppewebshopDto dto,
			TheClientDto theClientDto);

	public void removeShopgruppewebshop(ShopgruppewebshopDto dto);

	public ShopgruppewebshopDto shopgruppewebshopFindByPrimaryKey(Integer iId);

	public ShopgruppewebshopDto[] shopgruppeFindByWebshopId(Integer webshopIId);

	public void updateShopgruppewebshop(ShopgruppewebshopDto dto);

	public ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr,
			TheClientDto theClientDto);

	public Map getAlleGueltigenStaffelneinesLieferanten(
			Integer artikellieferantIId, java.sql.Date dDatum,
			String waehrungCNrGewuenschteWaehrung, TheClientDto theClientDto);

	public void updateArtikelshopgruppe(ArtikelshopgruppeDto dto,
			TheClientDto theClientDto);

	public ArtikelshopgruppeDto artikelshopgruppeFindByPrimaryKey(Integer iId);

	public Integer createArtikelshopgruppe(ArtikelshopgruppeDto dto,
			TheClientDto theClientDto);

	public void removeArtikelshopgruppe(ArtikelshopgruppeDto dto);

	public Integer[] getBereitsVerwendeteShopgruppen(Integer artikelIId);

	public void vertauscheShopgruppen(Integer pos1, Integer pos2)
			throws EJBExceptionLP;

	public List<ArtgruDto> artgruFindByMandantCNrSpr(TheClientDto theClientDto);
	public void setzeArtikelSNRtragendOhneWeitereAktion(Integer artikelIId);
	
	public byte[] getXLSForPreispflege(Integer artikelgruppeIId,
			Integer artikelklasseIId, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, TheClientDto theClientDto);
	public void preiseXLSForPreispflege(byte[] xlsFile, TheClientDto theClientDto);
	public void importiereDigiraster(byte[] xlsFile, TheClientDto theClientDto);
}
