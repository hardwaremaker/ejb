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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FinanzServiceFac {
	// Fix verdrahtet Mahnstufen
	public static final int MAHNSTUFE_1 = 1;
	public static final int MAHNSTUFE_2 = 2;
	public static final int MAHNSTUFE_3 = 3;
	public static final int MAHNSTUFE_99 = 99;

	// Fix verdrahtet Mahntext
	public static final String DEFAULT_TEXT_MAHNSTUFE_1 = "Text Mahnstufe 1";
	public static final String DEFAULT_TEXT_MAHNSTUFE_2 = "Text Mahnstufe 2";
	public static final String DEFAULT_TEXT_MAHNSTUFE_3 = "Text Mahnstufe 3";
	public static final String DEFAULT_TEXT_MAHNSTUFE_99 = "Wir erteilen nachfolgenden Auftrag gem\u00E4\u00DF den uns bekannten Allgemeinen Gesch\u00E4ftsbedingungen der Internationalen Mahn- und Inkassodienste";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_MAHNTEXT_I_ID = "i_id";
	public static final String FLR_MAHNTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_MAHNTEXT_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNTEXT_X_TEXTINHALT = "c_textinhalt";

	// Fix verdrahtet Steuerkategorie
	public static final String STEUERKATEGORIE_INLAND = "Inland         ";
	public static final String STEUERKATEGORIE_INLAND_REVERSE = "InlandReverse  ";
	public static final String STEUERKATEGORIE_AUSLANDEU_UID = "AuslandEUmUID  ";
	public static final String STEUERKATEGORIE_AUSLANDEU = "AuslandEUoUID  ";
	public static final String STEUERKATEGORIE_AUSLAND = "Ausland        ";
	public static final String STEUERKATEGORIE_AUSLAND_REVERSE = "AuslandReverse ";

	public static final String FLR_STEUERKATEGORIE_B_REVERSECHRGE = "b_reversecharge";

	public static final String FLR_WARENVERKEHRSNUMMER_C_NR = "c_nr";
	public static final String FLR_WARENVERKEHRSNUMMER_C_BEZ = "c_bez";

	public static final int MAX_STEUERKATEGORIE_BEZEICHNUNG = 80;

	public Integer createMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public static final String KONTOTYP_DEBITOR = "Debitorenkonto";
	public static final String KONTOTYP_KREDITOR = "Kreditorenkonto";
	public static final String KONTOTYP_SACHKONTO = "Sachkonto";

	public final static String KONTOART_UST_ODER_ERWERBSSTEUERKONTO = "UST- oder Erwerbssteuerkonto";
	
	// die neuen Kontoarten fuer das Fibumodul
	public final static String KONTOART_UST = "Ust Konto";
	public final static String KONTOART_VST = "Vst Konto";
	public final static String KONTOART_UST_SAMMEL = "Ust Sammelkonto";
	public final static String KONTOART_VST_SAMMEL = "Vst Sammelkonto";
	public final static String KONTOART_FA_ZAHLLAST = "FA Zahllastkonto";
	public final static String KONTOART_EROEFFNUNG = "Eroeffnungskonto";
	public final static String KONTOART_ABGABEN = "Abgaben";
	public final static String KONTOART_NICHT_ZUTREFFEND = "Nicht zutreffend";
	
	public final static String UVAART_EU_AUSLAND_MIT_UID = "EU Ausland mit UiD";
	public final static String UVAART_EXPORT_DRITTLAND = "Export Drittland";
	public final static String UVAART_FA_VORSTEUERKONTO = "FA Vorsteuerkonto";
	public final static String UVAART_IG_ERWERB_10 = "IG Erwerb red.Steuer"; // "IG Erwerb 10";
	public final static String UVAART_IG_ERWERB_20 = "IG Erwerb Normalsteuer"; // "IG Erwerb 20";
	public final static String UVAART_IMPORT_DRITTLAND = "Import Drittland";
	public final static String UVAART_INLAND_10 = "Umsatz Inland red.Steuer"; // "Inland 10";
	public final static String UVAART_INLAND_20 = "Umsatz Inland Normalsteuer"; // "Inland 20";
	public final static String UVAART_NICHT_ZUTREFFEND = "Nicht zutreffend";
	public final static String UVAART_REVERSE_CHARGE = "Reverse Charge";
	public final static String UVAART_UMSATZ_REVERSE_CHARGE = "Umsatz Reverse Charge";
	public final static String UVAART_ZAHLLASTKONTO = "Zahllastkonto";
	public final static String UVAART_ZAHLUNG_10 = "Anzahlung red.Steuer"; // "Zahlung 10";
	public final static String UVAART_ZAHLUNG_20 = "Anzahlung Normalsteuer"; // "Zahlung 20";

	public static final String STEUERART_UST = "UST";
	public static final String STEUERART_VST = "VST";
	public static final String STEUERART_EUST = "EUST";
	
	/**
	 * Gibt alle IIds der Steuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	public Map<Integer, Integer> getAllIIdsSteuerkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);

	/**
	 * Gibt alle IIds der Umsatzsteuersteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	public Map<Integer, Integer> getAllIIdsUstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);
	
	/**
	 * Gibt alle IIds der Einfuhr-Umsatzsteuersteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	public Map<Integer, Integer> getAllIIdsEUstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);
	
	public List<SteuerkategoriekontoDto> steuerkategorieFindByKontoIIdMwstSatzBezIId(Integer kontoIId, Integer mwstSatzBezIId);
	/**
	 * Gibt alle IIds der Vorsteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	public Map<Integer, Integer> getAllIIdsVstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);

	public Set<Integer> getAllMitlaufendeKonten(Integer finanzamtIId, TheClientDto theClientDto);
	
	public KontotypsprDto kontotypsprFindByPrimaryKey(String kontotypCNr,
			String localeCNr) throws EJBExceptionLP, RemoteException;

	public KontotypDto kontotypFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public UvaartDto uvaartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public UvaartDto uvaartFindByCnrMandant(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public UvaartDto uvaartFindByCnrMandantOhneExc(String cNr,
			TheClientDto theClientDto) throws RemoteException;

	public UvaartsprDto uvaartsprFindByPrimaryKey() throws EJBExceptionLP,
			RemoteException;

	public KontoartsprDto kontoartsprFindByPrimaryKey() throws EJBExceptionLP,
			RemoteException;

	public KontoartDto kontoartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KontoartDto[] kontoartFindAll() throws EJBExceptionLP,
			RemoteException;

	public Integer createSteuerkategorie(SteuerkategorieDto steuerkategorieDto,
			TheClientDto theClientDto);

	public void removeSteuerkategorie(Integer iId);

	public SteuerkategorieDto steuerkategorieFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public SteuerkategoriekontoDto[] steuerkategoriekontoFindAll(
			Integer steuerkategorieIId);

	public void updateSteuerkategorie(SteuerkategorieDto steuerkategorieDto,
			TheClientDto theClientDto);

	public void updateSteuerkategoriekonto(
			SteuerkategoriekontoDto steuerkategoriekontoDto,
			TheClientDto theClientDto);

	public String uebersetzeUvaartOptimal(Integer iId, Locale locale1,
			Locale locale2) throws RemoteException;

	public String uebersetzeKontoartOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public String uebersetzeKontotypOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public void createKontoart(KontoartDto kontoartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createKontotyp(KontotypDto kontotypDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createUvaart(UvaartDto uvaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MahntextDto mahntextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public MahntextDto createDefaultMahntext(Integer mahnstufeIId,
			String sTextinhaltI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MahntextDto mahntextFindByMandantLocaleCNr(String pMandant,
			String pSprache, Integer mahnstufeIId) throws EJBExceptionLP,
			RemoteException;

	public void updateKontoart(KontoartDto kontoartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateUvaart(UvaartDto uvaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LaenderartDto laenderartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeLaenderartspr(LaenderartsprDto laenderartsprDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLaenderartspr(LaenderartsprDto laenderartsprDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LaenderartsprDto laenderartsprFindByPrimaryKey(String laenderartCNr,
			String localeCNr) throws RemoteException, EJBExceptionLP;

	public String uebersetzeLaenderartOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public String getLaenderartZuPartner(Integer partnerIId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public HashMap<String, String> getAllLaenderartenMitUebersetzung(
			Locale locale, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public String getLaenderartZuPartner(MandantDto mandantDto,
			PartnerDto partnerDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public BuchungsartDto buchungsartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public BuchungsartDto[] buchungsartFindAll() throws EJBExceptionLP,
			RemoteException;

	public BuchungsartsprDto buchungsartsprFindByPrimaryKey(
			String buchungsartCNr, String spracheCNr) throws EJBExceptionLP,
			RemoteException;

	public TreeMap<?, ?> getAllBuchungsarten(Locale locale1, Locale locale2, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public void createBuchungsart(BuchungsartDto buchungsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getLaenderartZuPartner(PartnerDto partnerDtoBasis,
			PartnerDto partnerDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TreeMap<String, String> uebersetzeBuchungsartOptimal(
			DatenspracheIf[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public String uebersetzeBuchungsartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public void createWarenverkehrsnummernUndLoescheAlte(
			WarenverkehrsnummerDto[] warenverkehrsnummerDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKeyOhneExc(
			String cNr) throws EJBExceptionLP, RemoteException;

	public void removeAllWarenverkehrsnummern();

	public void createWarenverkehrsnummer(
			WarenverkehrsnummerDto warenverkehrsnummerDto);

	public void vertauscheSteuerkategorie(Integer iiD1, Integer iId2);

//	public void createTagesabschluss(int geschaeftsjahr,
//			TheClientDto theclientDto) throws EJBExceptionLP, RemoteException;

	public UvaartDto[] uvaartFindAll(String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public Integer getUstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId);
	
	public Integer getEUstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId);
	
	public Integer getVstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId);

	public void verbucheBelegePeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ArrayList<FibuFehlerDto> pruefeBelegePeriode(Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public Integer uvaVerprobung(ReportUvaKriterienDto krit,
			TheClientDto theClient);

	public UvaverprobungDto getLetzteVerprobung(Integer partnerIIdFinanzamt,
			TheClientDto theClient);

	public void createDefaultSteuerkategoriekonto(Integer steuerkategorieIId,
			TheClientDto theClient);

	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(String nr,
			Integer finanzamtIId, TheClientDto theClient);

	public void removeUvaverprobung(Integer uvaIId) throws EJBExceptionLP,
			RemoteException;

	/**
	 * Alle im System bekannten Steuerkategorien f&uuml;r das angegebene Finanzamt
	 * und den Mandaten
	 * 
	 * @param finanzamtIId
	 *            die zu betrachtende ID des Finanzamts
	 * @param theClient
	 * @return null wenn es keine gibt, ansonsten die bekannten
	 *         SteuerkategorieDtos
	 * @throws EJBExceptionLP
	 */
	public SteuerkategorieDto[] steuerkategorieFindByFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClient) throws EJBExceptionLP;

	/**
	 * Die Standardsteuerkategorien fuer das angegebene Finanzamt anlegen.
	 * 
	 * Die Standardsteuerkategorien werden nur angelegt, wenn es noch keine
	 * Kategorien fuer dieses Finanzamt gibt.
	 * 
	 * @param finanzamtIId
	 * @param theClientDto
	 * @throws EJBExceptionLP 
	 */
	public void createIfNeededSteuerkategorieForFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	/**
	 * Fuer alle diesem Finanzamt zugehoerigen Steuerkategorien die
	 * Steuerkategoriekonten anlegen sofern nicht vorhanden
	 * 
	 * Sollte es noch keine Steuerkategorien geben fuer dieses Finanzamt werden
	 * auch diese angelegt.
	 * 
	 * @param finanzamtIId
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 */
	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public ArrayList<FibuFehlerDto> pruefeBelegeKurse(Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClient) throws EJBExceptionLP,
			RemoteException;

	public Integer createAuszifferung(Integer[] buchungdetailIds,
			TheClientDto theClient);

	public Integer updateAuszifferung(Integer auszifferKennzeichen,
			Integer[] buchungdetailIds, TheClientDto theClient);

	public void removeAuszifferung(Integer[] buchungdetailIds,
			TheClientDto theClient);

	public boolean isFibuLandunterschiedlich(Integer partner1IId,
			Integer partner2IId);

	public void updateMahnspesen(MahnspesenDto dto);

	public MahnspesenDto mahnspesenFindByPrimaryKey(Integer iId);

	public void removeMahnspesen(MahnspesenDto dto);

	public Integer createMahnspesen(MahnspesenDto dto);

	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode, int finanzamtIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KontoDto getSammelkonto(String kontoartCNr, Integer finanzamtIId, TheClientDto theClientDto);
	
	public UvaverprobungDto uvaVerprobungFindbyFinanzamtIIdGeschaeftsjahrPeriodeAbrechnungszeitraumMandant(
			Integer finanzamtIId, int iGeschaeftsjahr, int iPeriode,
			int iAbrechnungszeitraum, TheClientDto theClientDto);

	/**
	 * Berechnet den Saldo &uuml;ber alle Bankverbindungen bei denen {@link BankverbindungDto#isbInLiquiditaetsVorschau()} = true ist.
	 * @param theClientDto 
	 * @return den Kontostand
	 */
	public BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId, TheClientDto theClientDto);
}
