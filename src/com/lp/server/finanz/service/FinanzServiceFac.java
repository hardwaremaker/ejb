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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.finanz.ejbfac.SteuerkontoInfo;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.SteuerkategoriekontoId;
import com.lp.server.util.UvaFormularId;
import com.lp.server.util.UvaartId;
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
	public static final String FLR_WARENVERKEHRSNUMMER_C_BM = "c_bm";

	public static final int MAX_STEUERKATEGORIE_BEZEICHNUNG = 80;
	public static final int MAX_REVERSECHARGEART_BEZEICHNUNG = 80 ;
	
	public Integer createMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public static final String KONTOTYP_DEBITOR = "Debitorenkonto";
	public static final String KONTOTYP_KREDITOR = "Kreditorenkonto";
	public static final String KONTOTYP_SACHKONTO = "Sachkonto";

	public static final String KONTOART_UST_ODER_ERWERBSSTEUERKONTO = "UST- oder Erwerbssteuerkonto";
	
	// die neuen Kontoarten fuer das Fibumodul
	public static final String KONTOART_UST = "Ust Konto";
	public static final String KONTOART_VST = "Vst Konto";
	public static final String KONTOART_UST_SAMMEL = "Ust Sammelkonto";
	public static final String KONTOART_VST_SAMMEL = "Vst Sammelkonto";
	public static final String KONTOART_FA_ZAHLLAST = "FA Zahllastkonto";
	public static final String KONTOART_EROEFFNUNG = "Eroeffnungskonto";
	public static final String KONTOART_ABGABEN = "Abgaben";
	public static final String KONTOART_NICHT_ZUTREFFEND = "Nicht zutreffend";
	
	public static final String UVAART_EU_AUSLAND_MIT_UID = "EU Ausland mit UiD";
	public static final String UVAART_EXPORT_DRITTLAND = "Export Drittland";
	public static final String UVAART_FA_VORSTEUERKONTO = "Vorsteuerkonto";
	public static final String UVAART_IG_ERWERB_10 = "IG Erwerb red.Steuer";
	public static final String UVAART_IG_ERWERB_20 = "IG Erwerb Normalsteuer";
	public static final String UVAART_IMPORT_DRITTLAND = "Import Drittland";
	public static final String UVAART_INLAND_10 = "Umsatz Inland red.Steuer";
	public static final String UVAART_INLAND_20 = "Umsatz Inland Normalsteuer";
	public static final String UVAART_NICHT_ZUTREFFEND = "Nicht zutreffend";
	public static final String UVAART_REVERSE_CHARGE = "Reverse Charge Leistung";
	public static final String UVAART_UMSATZ_REVERSE_CHARGE = "Umsatz Reverse Charge";
	public static final String UVAART_ZAHLLASTKONTO = "Zahllastkonto";
	public static final String UVAART_ZAHLUNG_10 = "Anzahlung red.Steuer";
	public static final String UVAART_ZAHLUNG_20 = "Anzahlung Normalsteuer";
	public static final String UVAART_INLAND_STEUERFREI = "Umsatz Inland steuerfrei";
	public static final String UVAART_VERRECHNUNGSKONTO = "Verrechnungskonto";
	public static final String UVAART_VORSTEUER_KFZ = "Vorsteuer betr KFZ";
	public static final String UVAART_VORSTEUER_GEBAEUDE = "Vorsteuer betr Gebaeude";
	public static final String UVAART_VORSTEUER_INVESTITIONEN = "Vorsteuer Investitionen";
	public static final String UVAART_WERBEABGABE = "Werbeabgabe";
	public static final String UVAART_REVERSE_CHARGE_BAULEISTUNG = "Reverse Charge Bauleistung";
	public static final String UVAART_REVERSE_CHARGE_SCHROTT = "Reverse Charge Schrott";
	public static final String UVAART_ZAHLUNG_EU_AUSLAND_MIT_UID = "Anzahlung EU Ausland mit UiD";
	public static final String UVAART_ZAHLUNG_DRITTLAND = "Anzahlung Drittland";
	public static final String UVAART_IMPORT_DRITTLAND_ZAHLUNG_FA = "Import Drittland Zahlung an FA";
	
	public static final String STEUERART_UST = "UST";
	public static final String STEUERART_VST = "VST";
	public static final String STEUERART_EUST = "EUST";
	
	public class ReversechargeArt {
		public static final String OHNE = "OHNE";	
		public static final String LEISTUNG = "Leistung";
		public static final String BAULEISTUNG = "Bauleistung";
		public static final String SCHROTT = "Schrott";
		public static final String TELEKOM = "Telekom";
		/**
		 * Innergemeinschaftlich, versteckte ReversechargeArt
		 */
		public static final String IG = "IG";
	}
	
	/**
	 * Gibt alle IIds der Steuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den SteuerkontoInfo als Value
	 */
	Map<Integer, SteuerkontoInfo> getAllIIdsSteuerkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);

	/**
	 * Gibt alle IIds der Umsatzsteuersteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den SteuerkontoInfo als Value
	 */
	Map<Integer, SteuerkontoInfo> getAllIIdsUstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);
	
	/**
	 * Gibt alle IIds der Einfuhr-Umsatzsteuersteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den SteuerkontoInfo als Value
	 */
	Map<Integer, SteuerkontoInfo> getAllIIdsEUstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);
	
	/**
	 * Gibt alle IIds der Vorsteuerkonten zur&uuml;ck, welche im angegebenen Finanzamt hinterlegt sind.
	 * Die Konto-IId ist der Key in der zur&uuml;ckgegebenen Map, die IId der MehrwertsteuerBezeichung die Value.
	 * Ist ein Konto in mehreren Steuers&auml;tzen in Verwendung ist die Value null.
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	Map<Integer, SteuerkontoInfo> getAllIIdsVstkontoMitIIdMwstBez(Integer finanzamtIId, TheClientDto theClientDto);

	public Set<Integer> getAllMitlaufendeKonten(Integer finanzamtIId, TheClientDto theClientDto);

	/**
	 * Eine Liste aller Steuerkategoriekonten bei denen das gesuchte Konto
	 * entweder als UstKonto oder als VstKonto hinterlegt ist.</br>
	 * <p>Die Erwartungshaltung ist, dass entweder kein Satz gefunden wird 
	 * - dann ist das Konto nicht eingetragen und wir k&ouml;nnen in der 
	 * Folge nicht buchen - oder genau ein Datensatz. Sollte es mehrere
	 * Datens&auml;tze geben, dann bleibt es dem Aufrufer &uuml;berlassen
	 * wie er mit der Situation umgeht.</p>
	 * <p>Die Fibu-Anwender sind dazu angehalten, unterschiedliche Konten
	 * in den Steuerkategorien (Inland, AuslandEumitUID, ...) zu hinterlegen.
	 * Wir pr&uuml;fen das teilweise bei der Erfassung, aber erzwingen es 
	 * noch nicht in allen F&auml;llen.</p>
	 * 
	 * @param kontoIId
	 * @param mwstSatzBezIId
	 * @param gueltigAm die Kategoriekonto-Definition, die zu diesem Zeitpunkt
	 * g&uuml;ltig ist.
	 * @return eine (leere) Liste von Kategorie-Defintionen die das gesuchte
	 * Konto enthalten
	 */
	public List<SteuerkategoriekontoDto> steuerkategorieFindByKontoIIdMwstSatzBezIId(
			Integer kontoIId, Integer mwstSatzBezIId, Timestamp gueltigAm);

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

	public UvaartsprDto uvaartsprFindByPrimaryKey(Integer uvaartIId,String localeCNr) throws EJBExceptionLP,
			RemoteException;

	public KontoartsprDto kontoartsprFindByPrimaryKey() throws EJBExceptionLP,
			RemoteException;

	public KontoartDto kontoartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KontoartDto[] kontoartFindAll() throws EJBExceptionLP,
			RemoteException;

	public Integer createSteuerkategorie(
			SteuerkategorieDto steuerkategorieDto, TheClientDto theClientDto);

	public void removeSteuerkategorie(Integer iId);

	public void removeSteuerkategoriekonto(Integer iId);
	
	public SteuerkategorieDto steuerkategorieFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	/**
	 * Alle Steuerkategoriekonten zu einer Steuerkategorie ermitteln</br>
	 * <p><b>Wer diese Methode verwendet, weiss was er tut.</b></br>
	 * Diese Methode liefert alle definierten Steuerkategoriekonten
	 * f&uuml;r die gew&uuml;nschte Steuerkategorie. Also auch solche, die
	 * zeitlich nicht mehr, oder noch nicht g&uuml;ltig sind.
	 * </p>
	 * 
	 * @param steuerkategorieIId zu der die Steuerkategoriekonten ermittelt werden sollen
	 * @return ein (leeres) Array der Steuerkategoriekonten
	 */
	public SteuerkategoriekontoDto[] steuerkategoriekontoFindAll(
			Integer steuerkategorieIId);

	public SteuerkategoriekontoDto steuerkategoriekontoFindByPrimaryKey(
            Integer steuerkategorieIId, Integer mwstsatzbezIId, Timestamp gueltigAm) ;

	public void updateSteuerkategorie(
			SteuerkategorieDto steuerkategorieDto, TheClientDto theClientDto);

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

	Integer createUvaart(UvaartDto uvaartDto, TheClientDto theClientDto);

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

//	public String getLaenderartZuPartner(Integer partnerIId,
//			TheClientDto theClientDto) throws RemoteException;
	
	String getLaenderartZuPartner(Integer partnerId,
			Timestamp gueltigZum, TheClientDto theClientDto) throws RemoteException;
	
	public HashMap<String, String> getAllLaenderartenMitUebersetzung(
			Locale locale, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	String getLaenderartZuPartner(MandantDto mandantDto,
			PartnerDto partnerDto, Timestamp gueltigZum, TheClientDto theClientDto)
			throws RemoteException;

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

	String getLaenderartZuPartner(PartnerDto partnerDtoBasis,
			PartnerDto partnerDto, Timestamp gueltigZum, TheClientDto theClientDto)
			throws RemoteException;

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

	Integer getUstKontoFuerSteuerkategorie(
			Integer steuerkategorieIId, Integer mwstsatzbezId, Timestamp gueltigAm);
	
	Integer getEUstKontoFuerSteuerkategorie(
			Integer steuerkategorieIId, Integer mwstsatzbezId, Timestamp gueltigAm);
	
	Integer getVstKontoFuerSteuerkategorie(
			Integer steuerkategorieIId, Integer mwstsatzbezId, Timestamp gueltigAm);

	List<BucheBelegPeriodeInfoDto> verbucheBelegePeriode(Integer geschaeftsjahr, int periode,
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
	 * @deprecated Use createIfNeededSteuerkategoriekontoForFinanzamt(Integer, Integer, TheClientDto) 
	 */
	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(
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
	void createIfNeededSteuerkategoriekontoForFinanzamtIId(
			Integer finanzamtIId, Integer reversechargeartId, 
			Timestamp gueltigAb, TheClientDto theClientDto) ;
	
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
	BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId, TheClientDto theClientDto);

	/**
	 * Pr&uuml;ft die Kontendefinitionen des Finanzamts und alle Steuerkategorien(konten) des Finanzamts
	 * auf doppelte Verwendung von Sachkonten. </br>
	 * <p>Wird ein Konto mehrfach verwendet, so wirft diese Methode eine EJBExceptionLP.
	 * @param finanzamtId das zu pr&uuml;fende Finanzamt
	 * @param theClientDto
	 * @throws EJBExceptionLP.FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET,
	 * 		   RemoteException
	 */
    void pruefeSteuerkonten(Integer finanzamtId, TheClientDto theClientDto) throws RemoteException ;
    
    SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(
    		String nr, Integer reversechargeartId, Integer finanzamtIId, TheClientDto theClient) ;

    /**
     * Eine neue Reversechargeart erzeugen
     * 
     * @param reversechargeartDto
     * @param theClientDto
     * @return die Id der neu erzeugten Reversechargeart
     */
    Integer createReversechargeart(
    		ReversechargeartDto reversechargeartDto, TheClientDto theClientDto) ;

    void updateReversechargeart(ReversechargeartDto reversechargeartDto,
            TheClientDto theClientDto) ;

    /**
     * Eine bestehende Reversechargeart l&ouml;schen
     * 
     * @param reversechargeartId die zu l&ouml;schende Reversechargeart-Id
     */
    void removeReversechargeart(Integer reversechargeartId) ;

    /**
     * Die Reversechargeart mit Hilfe der IId ermitteln
     * 
     * @param reversechargeartId die zu ermittelnde ReversechargeartId
     * @param theClientDto
     * @return ReversechargeartDto sofern f&uuml;r die reversechargeartId ein Satz existiert, sonst ExceptionLP
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindByPrimaryKey(
    		Integer reversechargeartId, TheClientDto theClientDto) throws RemoteException ;
    
    /**
     * Jene Reversechargeart des Mandanten ermitteln, die f&uuml;r "OHNE REVERSECHARGE" steht.</br>
     * <p>OHNE reversecharge ist - neben VERSION1 - die einzige Reversecharge-Art die von uns
     * angelegt wurden. Alle anderen Arten werden vom Anwender selbst definiert. </p>
     * <p><b>Bitte</b> diese Methode wenn man OHNE Variante zu ermitteln ist</p>  
     * @param theClientDto
     * @return die Reversechargeart 
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindOhne(TheClientDto theClientDto) throws RemoteException ;
    
    /**
     * Jene Reversechargeart des Mandanten ermitteln, die f&uuml;r "&Uuml;bernahme der Vorg&auml;ngerdaten" steht.</br>
     * <p>VERSION1 ist - wie auch OHNE - die einzige Reversecharge-Art die von uns
     * angelegt wurden. Alle anderen Arten werden vom Anwender selbst definiert. </p>
     * <p><b>Bitte</b> diese Methode wenn die VERSION1 Variante zu ermitteln ist</p>  
     * @param theClientDto
     * @return die Reversechargeart 
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindLeistung(TheClientDto theClientDto) throws RemoteException ;

    /**
     * Die vom Anwender angelegte Reversechargeart laden  
     * @param reversechargeartCnr die gesuchte Reversechargeart
     * @param theClientDto
     * @return die ReverschargeartDto
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindByCnrMandant(
    		String reversechargeartCnr, TheClientDto theClientDto) throws RemoteException ;
    
    /**
     * Jene Reversechargeart des Mandanten liefern, die "OHNE REVERSECHARGE" entspricht.</br>
     * <p>Es werden keine sprachspezifischen Information geliefert, da ja keine Sprache bekannt</p>
     * 
     * @param mandantCNr
     * @return die ReversechargeartDto
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindOhne(String mandantCNr) throws RemoteException ; 
    
    /**
     * Jene Reversechargeart des Mandanten liefern, die "&Uuml;bernahme der Vorg&auml;ngerdaten" entspricht.</br>
     * <p>Es werden keine sprachspezifischen Information geliefert, da ja keine Sprache bekannt</p>
     * 
     * @param mandantCNr
     * @return die ReversechargeartDto
     * @throws RemoteException
     */
    ReversechargeartDto reversechargeartFindLeistung(String mandantCNr) throws RemoteException ;
    
    /**
     * Ist die angegebene Reversechargeart-IId eine Art mit Reverse Charge?
     * @param reversechargeartId die zu pr&uuml;fende Reversechargeart
     * @param theClientDto
     * @return true wenn die angegebene reversechargeartId eine Reversecharge Art ist, ansonsten false
     */
    boolean isReverseCharge(Integer reversechargeartId, TheClientDto theClientDto) ;
    
    /**
     * Alle im Mandanten definierten Reversechargearten mit der jeweiligen Sprachbezeichnung liefern
     * Versteckte Reversechargearten sind nicht enthalten
     * 
     * @param theClientDto - daraus wird Mandant, Client-Locale und Mandant-Locale ermittelt
     * @return die Map aller im Mandanten definierten Reversechargearten
     */
    Map<Integer, String> getAllReversechargeArt(TheClientDto theClientDto) ;
    
    /**
     * Alle im Mandanten erlaubten Reversechargearten mit der jeweiligen Sprachbezeichnung liefern.
     * Versteckte Reversechargearten nicht inkludiert.</br>
     * <p>Wenn per Parameter Reversecharge abgeschaltet ist, dann wird nur noch die Reversechargeart "OHNE"
     * geliefert, ansonsten alle</p>
     * 
     * @param theClientDto - daraus wird Mandant, Client-Locale und Mandant-Locale ermittelt
     * @return die Map aller erlaubten Reversechargearten
     * @throws RemoteException
     */
    Map<Integer, String> getAllReversechargeartAllowed(TheClientDto theClientDto) throws RemoteException ;

    void createIfNeededSteuerkategorieForFinanzamtIId(
            Integer finanzamtIId, Integer reversechargeartId, TheClientDto theClientDto) ; 
    
    Map<String, String> getAllSteuerkategorieCnr(TheClientDto theClientDto);
    
    Integer getUstKontoFuerSteuerkategorie(String steuerkategorieCnr,
    		Integer finanzamtId, Integer mwstsatzbezId, 
    		Timestamp gueltigAm, TheClientDto theClientDto) throws RemoteException; 

    Integer getVstKontoFuerSteuerkategorie(String steuerkategorieCnr,
    		Integer finanzamtId, Integer mwstsatzbezId, 
    		Timestamp gueltigAm, TheClientDto theClientDto) throws RemoteException;

    Integer getVstKontoFuerSteuerkategorie(String steuerkategorieCnr,
    		Integer reversechargeartId, Integer finanzamtId, 
    		Integer mwstsatzbezId, Timestamp gueltigAm, TheClientDto theClientDto);  
    
    /**
     * Die beiden angegebenen Reversechargearten IIds miteinander in ihrer ISort Position tauschen
     * @param id1
     * @param id2
     */
	void vertauscheReversechargeart(Integer id1, Integer id2) ;
	
	/**
	 * Eine beliebige Anzahl von ReversechargeartDtos anhand ihrer PrimaryKeys in einem Rutsch holen
	 * @param theClientDto
	 * @param keys die gew&uuml;nschten Keys
	 * @return die Liste der Dtos in der Reihenfolge der angegebenen keys 
	 * @throws RemoteException
	 */
    Map<Integer, ReversechargeartDto> reversechargeartFindByPrimaryKeys(
    		TheClientDto theClientDto, Integer... keys) throws RemoteException ;
    
    /**
     * Enth&auml;lt die Rechnung Lieferscheinpositionen und geht die Rechnung ins EU-Ausland (mit UID)
     * @param rechnungId
     * @param theClientDto
     * @return true wenn die Rechnung Lieferscheinpositionen enth&auml;lt, deren Lieferadresse ins 
     *   EU-Ausland mit UID gehen
     * @throws RemoteException 
     */
	boolean hatRechnungIGLieferung(
			Integer rechnungId, TheClientDto theClientDto) throws RemoteException ;    
	
	/**
	 * Ist es ein Lieferschein f&uuml;r eine Innergemeinschaftliche Lieferung (EU-Ausland mit UID)</br>
	 * <p>Derzeit wird eine Innergemeinschaftliche Lieferung dadurch identifiziert, dass
	 * <ol>
	 * <li> bei einem Mandanten mit Fibu das Finanzamt des Debitor und die Lieferadresse EU-Ausland mit UID
	 *    ergeben</li>
	 * <li> bei einem Mandanten ohne Fibu die Mandantenadresse und die Lieferadresse EU-Ausland mit UID ergeben</li>
	 * </ol>   
	 * </p>
	 * 
	 * @param lieferscheinId
	 * @param theClientDto
	 * @return true wenn es sich um eine innergemeinschaftliche Lieferung handelt
	 * @throws RemoteException
	 */
	boolean isIGLieferung(Integer lieferscheinId,
			TheClientDto theClientDto) throws RemoteException ;	
	
	/**
	 * Nur jene Belegbuchungen ausziffern die im Geschaeftsjahr des Verursachers der nun 
	 * durchzuf&uuml;hrenden Auszifferung liegen</br>
	 * <p>Hintergrund: Es kann durchaus vorkommen, dass beispielsweise eine Zahlung zu einer
	 * Rechnung in einem anderen Gesch&auml;ftsjahr erfolgt, als die Ausstellung der Rechnung. Um
	 * hier alle Eventualit&auml;ten korrekt durchf&uuml;hren zu k&ouml;nnen, m&uuml;sste 
	 * beispielsweise m&ouml;glicherweise eine Er&ouml;ffnungsbuchung durchgef&uuml;hrt werden.</p>
	 * <p>
	 * Ausserdem kann in einem gesperrten Gesch&auml;ftsjahr keine &Auml;nderung des AZK 
	 * durchgef&uuml;hrt werden, da das GJ <b>gesperrt</b> ist.</p>
	 * 
	 * @param geschaeftsjahrVerursacher
	 * @param buchungdetailIds
	 * @param theClientDto
	 * 
	 * @return die Id der Auszifferung
	 */	
	Integer createAuszifferung(Integer geschaeftsjahrVerursacher, 
			Integer[] buchungdetailIds, TheClientDto theClientDto);
	
	Integer updateAuszifferung(
			Integer geschaeftsjahrVerursacher,Integer auszifferKennzeichen,
			Integer[] buchungdetailIds, TheClientDto theClientDto);

	/**
	 * Die Kontoart mit ihrer CNR im zum Client passenden Mandanten suchen
	 * 
	 * @param cNr die gesuchte Kontoartcnr
	 * @param theClientDto
	 * @return null wenn nicht vorhanden, ansonsten das Dto
	 * @throws EJBExceptionLP
	 */
	KontoartDto kontoartFindByPrimaryKeyOhneExc(
			String cNr, TheClientDto theClientDto) throws EJBExceptionLP;
	
	List<UstUebersetzungDto> ustUebersetzungFindByMandant(String mandant);

	/**
	 * Gewinnbuchungen f&uuml;r Periode durchf&uuml;hren
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 * 
	 */
	public void createGewinnbuchungen(int geschaeftsjahr, int periode,
			BigDecimal betrag, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	/**
	 * Steuerkategoriekonto-Definition zum passenden Datum ermitteln.</br>
	 *
	 * <p>Ein Beispiel:</br>
	 * 1.1.2014: ....</br>
	 * 1.7.2020: ....</br>
	 * 1.1.2021: ....</br>
	 * 
	 * Wird mit <code>gueltigAm</code> 30.06.2020 gesucht, wird der Satz mit dem
	 * Datum 1.1.2014 geliefert. Wird mit <code>gueltigAm</code> 1.8.2020 gesucht, 
	 * wird der Satz mit dem Datum 1.7.2020 geliefert. F&uuml;r das Suchdatum
	 * 31.12.2013 wird eine Exception geworfen.
	 * </p>
	 * @param steuerkategorieIId gesuchte SteuerkategorieId
	 * @param mwstsatzbezIId gesuchte MwstsatzBezId
	 * @param gueltigAm zu diesem Datum g&uuml;ltige Definition
	 * @return Empty wenn es keinen passenden Satz gibt, ansonsten der Satz 
	 */
	HvOptional<SteuerkategoriekontoDto> steuerkategoriekontoZuDatum(
			Integer steuerkategorieIId, Integer mwstsatzbezIId, Timestamp gueltigAm);

	/**
	 * Steuerkategoriekonto-Definition zum passenden Datum ermitteln.</br>
	 * <p>Konnte kein Satz gefunden werden handelt es sich um einen Konfigurationsfehler,
	 * es wird daher eine PRIMARYKEY_NOT_FOUND Exception erzeugt</br>
	 *
	 * <p>Ein Beispiel:</br>
	 * 1.1.2014: ....</br>
	 * 1.7.2020: ....</br>
	 * 1.1.2021: ....</br>
	 * 
	 * Wird mit <code>gueltigAm</code> 30.06.2020 gesucht, wird der Satz mit dem
	 * Datum 1.1.2014 geliefert. Wird mit <code>gueltigAm</code> 1.8.2020 gesucht, 
	 * wird der Satz mit dem Datum 1.7.2020 geliefert. F&uuml;r das Suchdatum
	 * 31.12.2013 wird eine Exception geworfen.
	 * </p>
	 * @param steuerkategorieIId gesuchte SteuerkategorieId
	 * @param mwstsatzbezIId gesuchte MwstsatzBezId
	 * @param gueltigAm zu diesem Datum g&uuml;ltige Definition
	 * @return 
	 */
	SteuerkategoriekontoDto steuerkategoriekontoZuDatumValidate(
			Integer steuerkategorieIId, Integer mwstsatzbezIId, Timestamp gueltigAm);

	SteuerkategoriekontoDto steuerkategoriekontoFindByPrimaryKey(SteuerkategoriekontoId kontoId);

	Integer getUstKontoFuerSteuerkategorie(String steuerkategorieCnr, Integer reversechargeartId, Integer finanzamtId,
			Integer mwstsatzbezId, Timestamp gueltigAm, TheClientDto theClientDto);

	SteuerkategoriekontoId createSteuerkategoriekonto(SteuerkategoriekontoDto steuerkategoriekontoDto,
			TheClientDto theClientDto);

	/**
	 * Eine Liste aller Steuerkategoriekonten die zum angegebenen Datum 
	 * in allen Mehrwertsteuerkategorien g&uuml;ltig sind.
	 * 
	 * @param steuerkategorieId die gesuchte Steuerkategorie
	 * @param gueltigZum das Datum zu dem das Steuerkategoriekonto g&uuml;ltig sein soll
	 * @return ein (leeres) Array aller Steuerkategoriekonten
	 */
	SteuerkategoriekontoDto[] steuerkategoriekontoAllZuDatum(
			Integer steuerkategorieId, Timestamp gueltigZum);
	
	/**
	 * Die UVA Formulardefinition ermitteln
	 * 
	 * @param formularId die Id des Eintrags
	 * @return die Formulardefinition (oder Exception wenn nicht vorhanden)
	 */
	UvaFormularDto uvaFormularFindByPrimaryKey(UvaFormularId formularId);

	/**
	 * Alle Formulardefinition f&uuml;r dieses Finanzamt/Partner und Mandant</br>
	 * <p>Die Eintr&auml;ge werde nach Formulargruppe und isort sortiert</p>
	 * 
	 * @param finanzamtId
	 * @param mandantCnr
	 * @return ein (leeres) Array aller Formulardefinition f&uuml;r das
	 * gew&uuml;nschten Finanzamt (Partner!) und Mandant
	 */
	UvaFormularDto[] uvaFormularFindAllByPartnerId(
			Integer finanzamtId, String mandantCnr);

	/**
	 * Die Formulardefinition f&uuml;r eine bestimmte Uvaart ermitteln
	 * 
	 * @param finanzamtId gesuchter Finanzamt/Partner
	 * @param mandantCnr im Mandanten
	 * @param uvaartId dieser Uvaart
	 * @return empty, oder die Formulardefinition
	 */
	HvOptional<UvaFormularDto> uvaFormularFindByFinanzamt(
			Integer finanzamtId, String mandantCnr, UvaartId uvaartId);
	
	/**
	 * Es werden alle Finanz&auml;mter des Mandanten gepr&uuml;ft</br>
	 * <p>Die Formularnummer muss entweder null, 1, 2 oder 3 sein</p>
	 * <p>Es muss f&uuml;r den Partner (Finanzamt_i_id = partner_i_id)
	 * ein Land (AT, DE, CH, LI) definiert sein.</p>
	 * 
	 * @param theClientDto
	 */
	void pruefeFinanzaemter(
			TheClientDto theClientDto) throws RemoteException;
	
	/**
	 * Es wird die Pr&uuml;fung von pruefeFinanzaemter auf genau das
	 * angegebene Finanzamt angewandt</br>
	 * <p>Die Formularnummer muss entweder null, 1, 2 oder 3 sein</p>
	 * <p>Es muss f&uuml;r den Partner (Finanzamt_i_id = partner_i_id)
	 * ein Land (AT, DE, CH, LI) definiert sein.</p>
	 * @param finanzamtId das zu pr&uuml;fende Finanzamt
	 * @param theClientDto
	 */
	void pruefeFinanzamt(Integer finanzamtId, 
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Die UVAArten im durch den Client angegebenen Mandaten auf den
	 * aktuellen Stand bringen</br>
	 * <p>Es werden fehlende UVAArten nachgetragen</p>
	 * <p>Die UVAArt ist mandantenweit! Es kann aber Finanz&auml;mter geben,
	 * die nicht im Mandantenland sind und mehr/weniger Uvaarten ben&ouml;tigen.
	 * Wir haben uns dazu entschlossen, alle UVAArten anzulegen. Im Konto 
	 * des jeweiligen Landes werden nur jene UVAArten verwendet, die f&uuml;r
	 * das jeweilige Finanzamt notwendig sind. Im Report werden Uvaarten 
	 * (indirekt &uuml;ber das UvaFormular) herausgefiltert.</p>
	 * 
	 * <p>Uns ist klar, dass das keinen Sch&ouml;nheitspreis an Datenbankdesign
	 * gewinnt. Die Refaktorisierung daf&uuml;r steht aktuell (27.7.2020)
	 * in keinem Verh&auml;ltnis.</p>
	 * 
	 * @param theClientDto
	 * @throws RemoteException
	 */
	void aktualisiereUvaart(TheClientDto theClientDto) throws RemoteException;

	void aktualisiereUvaformulare(TheClientDto theClientDto) throws RemoteException;
	void aktualisiereUvaformular(Integer finanzamtId, TheClientDto theClientDto) throws RemoteException;

	Integer createUvaFormular(UvaFormularDto uvaformularDto, TheClientDto theClientDto);
	void pflegeUvaFormulare(TheClientDto theClientDto) throws RemoteException;

    /**
     * Alle im Mandanten definierten Reversechargearten mit der jeweiligen Sprachbezeichnung liefern
     * 
     * @param mitVersteckten - wenn <code>true</code> werden auch die versteckten mitgeliefert
     * @param theClientDto - daraus wird Mandant, Client-Locale und Mandant-Locale ermittelt
     * @return die Map aller im Mandanten definierten Reversechargearten
     */
	Map<Integer, String> getAllReversechargeArt(Boolean mitVersteckten, TheClientDto theClientDto);

    /**
     * Alle im Mandanten erlaubten Reversechargearten mit der jeweiligen Sprachbezeichnung liefern</br>
     * <p>Wenn per Parameter Reversecharge abgeschaltet ist, dann wird nur noch die Reversechargeart "OHNE"
     * geliefert, ansonsten alle</p>
     * 
     * @param mitVersteckten - wenn <code>true</code> werden auch die versteckten mitgeliefert, sofern Reversecharge eingeschalten ist
     * @param theClientDto - daraus wird Mandant, Client-Locale und Mandant-Locale ermittelt
     * @return die Map aller erlaubten Reversechargearten
     * @throws RemoteException
     */
	Map<Integer, String> getAllReversechargeartAllowed(Boolean mitVersteckten, TheClientDto theClientDto)
			throws RemoteException;

    /**
     * Jene Reversechargeart des Mandanten liefern, die der versteckten "IG" (Innergemeinschaftlich) entspricht.</br>
     * <p>Es werden keine sprachspezifischen Information geliefert, da ja keine Sprache bekannt</p>
     * 
     * @param mandantCNr
     * @return die ReversechargeartDto
     * @throws RemoteException
     */
	ReversechargeartDto reversechargeartFindIg(String mandantCNr) throws RemoteException;

    /**
     * Jene Reversechargeart des Mandanten ermitteln, die f&uuml;r die versteckte "IG" (Innergemeinschaftlich) steht.</br>
     * <p><b>Bitte</b> diese Methode wenn IG Variante zu ermitteln ist</p>  
     * @param theClientDto
     * @return die Reversechargeart 
     * @throws RemoteException
     */
	ReversechargeartDto reversechargeartFindIg(TheClientDto theClientDto) throws RemoteException;

}
