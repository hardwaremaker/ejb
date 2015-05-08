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
package com.lp.server.system.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface MandantFac {

	// Urmandat; kann zZ. (Juni 2005) nie geaendert werden.
	public static String URMANDANT_C_NR = "001";

	// FLR
	public static final String FLR_PARTNER = "flrpartner";
	public static final String FLR_C_KBEZ = "c_kbez";

	// Feldlaengen
	public static final int MAX_MANDANT = 3;
	public static final int MAX_KBEZ = 25;
	public static final int MAX_ZAHLUNGSZIEL_C_BEZ = 40;

	// Mehrwertsteuersaetze
	public static final String MWST_SONSTIGES = "Urheberrechtsabgaben usw.";
	public static final String MWST_LEBENSMITTEL = "Lebensmittel, Buecher";
	public static final String MWST_NORMALSTEUERSATZ = "Allgemeine Waren";
	public static final String MWST_LUXUSGUETER = "Luxusgueter";

	public static final String ZUSATZFUNKTION_STUECKRUECKMELDUNG = "STUECKRUECKMELDUNG            ";
	public static final String ZUSATZFUNKTION_MASCHINENZEITERFASSUNG = "MASCHINENZEITERFASSUNG        ";
	public static final String ZUSATZFUNKTION_STIFTZEITERFASSUNG = "STIFTZEITERFASSUNG            ";
	public static final String ZUSATZFUNKTION_KUNDESONDERKONDITIONEN = "KUNDESONDERKONDITIONEN        ";
	public static final String ZUSATZFUNKTION_KAPAZITAETSVORSCHAU = "KAPAZITAETSVORSCHAU           ";
	public static final String ZUSATZFUNKTION_ZAHLUNGSVORSCHLAG = "ZAHLUNGSVORSCHLAG             ";
	public static final String ZUSATZFUNKTION_SERIENNUMMERN = "SERIENNUMMERN                 ";
	public static final String ZUSATZFUNKTION_CHARGENNUMMERN = "CHARGENNUMMERN                ";
	public static final String ZUSATZFUNKTION_DOKUMENTENABLAGE = "DOKUMENTENABLAGE              ";
	public static final String ZUSATZFUNKTION_MEHRLAGERVERWALTUNG = "MEHRLAGERVERWALTUNG           ";
	public static final String ZUSATZFUNKTION_SAMMELLIEFERSCHEIN = "SAMMELLIEFERSCHEIN            ";
	public static final String ZUSATZFUNKTION_AUFTRAG_RECHNUNG = "AUFTRAG_RECHNUNG              ";
	public static final String ZUSATZFUNKTION_REISEZEITEN = "REISEZEITEN                   ";
	public static final String ZUSATZFUNKTION_MEHRSPRACHIGKEIT = "MEHRSPRACHIGKEIT              ";
	public static final String ZUSATZFUNKTION_FINGERPRINT = "FINGERPRINT                   ";
	public static final String ZUSATZFUNKTION_KOSTENSTELLEN = "KOSTENSTELLEN                 ";
	public static final String ZUSATZFUNKTION_INTRASTAT = "INTRASTAT                     ";
	public static final String ZUSATZFUNKTION_TAPISERVICE = "TAPISERVICE                   ";
	public static final String ZUSATZFUNKTION_PROJEKTZEITERFASSUNG = "PROJEKTZEITERFASSUNG          ";
	public static final String ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG = "ANGEBOTSZEITERFASSUNG         ";
	public static final String ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN = "AUFTRAG_SERIENNUMMERN         ";
	public static final String ZUSATZFUNKTION_TELEFONZEITERFASSUNG = "TELEFONZEITERFASSUNG          ";
	public static final String ZUSATZFUNKTION_RAHMENDETAILBEDARFE = "RAHMENDETAILBEDARFE           ";
	public static final String ZUSATZFUNKTION_STUECKLISTE_ARBEITSPLAN = "STUECKLISTE_ARBEITSPLAN       ";
	public static final String ZUSATZFUNKTION_EINKAUFSANGEBOT = "EINKAUFSANGEBOT               ";
	public static final String ZUSATZFUNKTION_RANKINGLISTE = "RANKINGLISTE                  ";
	public static final String ZUSATZFUNKTION_ZUSAMMENGEHOERIGE_ARTIKEL = "ZUSAMMENGEHOERIGE_ARTIKEL     ";
	public static final String ZUSATZFUNKTION_TOPS_ANBINDUNG = "TOPS_ANBINDUNG                ";
	public static final String ZUSATZFUNKTION_HERSTELLERKOPPLUNG = "HERSTELLERKOPPLUNG            ";
	public static final String ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM = "ZENTRALER_ARTIKELSTAMM        ";
	public static final String ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN = "AUFTRAGTERMIN_STUNDEN_MINUTEN ";
	public static final String ZUSATZFUNKTION_AUFTRAG_MIT_EINKAUFPREIS = "AUFTRAG_MIT_EINKAUFPREIS      ";
	public static final String ZUSATZFUNKTION_DEBUGMODUS = "DEBUGMODUS                    ";
	public static final String ZUSATZFUNKTION_LIEFERANTENBEURTEILUNG = "LIEFERANTENBEURTEILUNG        ";
	public static final String ZUSATZFUNKTION_KONTAKTMANAGMENT = "KONTAKTMANAGEMENT             ";
	public static final String ZUSATZFUNKTION_SETARTIKEL = "SETARTIKEL                    ";
	public static final String ZUSATZFUNKTION_MEHRFACH_LAGERPLATZ_JE_LAGER = "MEHRFACH_LAGERPLATZ_JE_LAGER  ";
	public static final String ZUSATZFUNKTION_ZERTIFIKATART = "ZERTIFIKATART                 ";
	public static final String ZUSATZFUNKTION_FAXVERSAND = "FAXVERSAND                    ";
	public static final String ZUSATZFUNKTION_EMAILVERSAND = "EMAILVERSAND                  ";
	public static final String ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG = "ERSATZTYPENVERWALTUNG         ";
	public static final String ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT = "INTELLIGENTER STKL.IMPORT     ";
	public static final String ZUSATZFUNKTION_VERLEIH = "VERLEIH                       ";
	public static final String ZUSATZFUNKTION_INTERNER_VERSAND = "INTERNER_VERSAND              ";
	public static final String ZUSATZFUNKTION_GERAETESERIENNUMMERN = "GERAETESERIENNUMMERN          ";
	public static final String ZUSATZFUNKTION_ER_ZUSATZKOSTEN = "ER_ZUSATZKOSTEN               ";
	public static final String ZUSATZFUNKTION_LOHNDATENEXPORT = "LOHNDATENEXPORT               ";
	public static final String ZUSATZFUNKTION_LIQUIDITAETSVORSCHAU = "LIQUIDITAETSVORSCHAU          ";
	public static final String ZUSATZFUNKTION_KUNDE_JE_BESTELLPOSITION = "KUNDE_JE_BESTELLPOSITION      ";
	public static final String ZUSATZFUNKTION_KOSTENTRAEGER = "KOSTENTRAEGER                 ";
	public static final String ZUSATZFUNKTION_GETRENNTE_LAGER = "GETRENNTE_LAGER               ";
	public static final String ZUSATZFUNKTION_BEREITSCHAFT = "BEREITSCHAFT                  ";
	public static final String ZUSATZFUNKTION_ISTVERSTEURER = "ISTVERSTEURER                 ";
	public static final String ZUSATZFUNKTION_LAGERCOCKPIT = "LAGERCOCKPIT                  ";
	public static final String ZUSATZFUNKTION_MATERIALZUSCHLAG = "MATERIALZUSCHLAG              ";
	public static final String ZUSATZFUNKTION_PROJEKTKLAMMER = "PROJEKTKLAMMER                ";
	public static final String ZUSATZFUNKTION_SI_WERT = "SI_WERT                       ";
	public static final String ZUSATZFUNKTION_VERSANDWEG = "VERSANDWEG                    ";
	public static final String ZUSATZFUNKTION_WEBSERVICE = "WEBSERVICE                    ";
	public static final String ZUSATZFUNKTION_EMAIL_CLIENT = "EMAIL_CLIENT                  ";
	public static final String ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN = "ZEITEN_ABSCHLIESSEN           ";
	public static final String ZUSATZFUNKTION_MISCHVERSTEURER = "MISCHVERSTEURER               ";
	public static final String ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN = "AGSTKL_ARBEITSPLAN            ";
	public static final String ZUSATZFUNKTION_AUFTRAG_SCHNELLANLAGE = "AUFTRAG_SCHNELLANLAGE         ";
	public static final String ZUSATZFUNKTION_STUECKLISTENFREIGABE = "STUECKLISTENFREIGABE          ";
	public static final String ZUSATZFUNKTION_LIEFERSCHEINE_VERKETTEN = "LIEFERSCHEINE_VERKETTEN       ";
	public static final String ZUSATZFUNKTION_RESERVIERUNGEN_AUFLOESEN = "RESERVIERUNGEN_AUFLOESEN      ";
	public static final String ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG = "ERWEITERTE_PROJEKTSTEUERUNG   ";

	// zusatzberecht:1 Hier neue Berechtigung anlegen
	public Integer createMwstsatz(MwstsatzDto mwstsatzDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMwstsatz(MwstsatzDto mwstsatzDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateMwstsatz(MwstsatzDto mwstsatzDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MwstsatzDto mwstsatzFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MandantDto[] mandantFindAll(TheClientDto theClientDto);

	public Locale getLocaleDesHauptmandanten() throws EJBExceptionLP;

	public MwstsatzDto mwstsatzFindZuDatum(Integer mwstsatzbezIId,
			Timestamp tDatum);

	/**
	 * Alle MWST-Saetze eines Mandanten holen.
	 * 
	 * @deprecated MB: to get the ID's only: use mwstsatzIIdFindAllByMandant to
	 *             get formatted Strings: mwstsatzFindAllByMandant(String
	 *             ssMandantI, Timestamp tBelegdatum, TheClientDto theClientDto)
	 * 
	 * @param ssMandantI
	 *            Mandantennummer
	 * @param theClientDto
	 * @return Map: Key = I_ID des MWST-Satzes. Value = Formatierter Prozentsatz
	 *         + Bezeichnung
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Map<Integer, String> mwstsatzFindAllByMandant(String ssMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Set<Integer> mwstsatzIIdFindAllByMandant(String ssMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	/**
	 * Finde alle MWST-Satzbezeichnungen eines Mandanten.
	 * 
	 * @param ssMandantI
	 *            String
	 * @param theClientDto
	 * @return Map: Key = I_ID der Bezeichnung, Value = Bezeichnung
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Map<Integer, String> mwstsatzbezFindAllByMandant(String ssMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MwstsatzbezDto[] mwstsatzbezFindAllByMandantAsDto(String ssMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP;

	/**
	 * Alle Aktuellen MWST-Saetze eines Mandanten finden.
	 * 
	 * @param ssMandantI
	 *            String
	 * @param tBelegdatum
	 *            Timestamp: optional
	 * @param bInklHandeingabe
	 * @param theClientDto
	 * @return Map
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Map<Integer, String> mwstsatzFindAllByMandant(String ssMandantI,
			Timestamp tBelegdatum, boolean bInklHandeingabe,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createSpediteur(SpediteurDto oSpediteurDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeSpediteur(SpediteurDto spediteurDto)
			throws EJBExceptionLP, RemoteException;

	public void updateSpediteur(SpediteurDto spediteurDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SpediteurDto spediteurFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> spediteurFindAllByMandant(String ssMandantI)
			throws EJBExceptionLP, RemoteException;

	public Integer createZahlungsziel(ZahlungszielDto oZahlungszielDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeZahlungsziel(ZahlungszielDto zahlungszielDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateZahlungsziel(ZahlungszielDto zahlungszielDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ZahlungszielDto zahlungszielFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Map<Integer, String> zahlungszielFindAllByMandant(String sMandantI)
			throws EJBExceptionLP, RemoteException;

	public SpediteurDto spediteurFindByMandantCNrCNamedesspediteursOhneExc(
			String name, String mandant);

	public String createMandant(MandantDto mandantDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMandant(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeMandant(MandantDto mandantDtoI) throws EJBExceptionLP,
			RemoteException;

	public void updateMandant(MandantDto mandantDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MandantDto mandantFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto)
			throws EJBExceptionLP, RemoteException;

	public ZahlungszielsprDto zahlungszielsprFindByPrimaryKey(
			Integer zahlungszielIId, String localeCNr) throws EJBExceptionLP,
			RemoteException;

	public MandantDto mandantFindByPrimaryKeyOhneExc(String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Date berechneZielDatumFuerBelegdatum(java.util.Date dBelegdatum,
			Integer zahlungszielIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public java.sql.Date berechneZielDatumFuerBelegdatum(
			java.util.Date dBelegdatum, ZahlungszielDto zahlungszielDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Date berechneSkontoTage1FuerBelegdatum(Date dBelegdatum,
			Integer zahlungszielIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Date berechneSkontoTage2Belegdatum(Date dBelegdatum,
			Integer zahlungszielIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZahlungszielsprDto zahlungszielsprFindByPrimaryKeyOhneExc(
			Integer iIdZahlungszielI, String sLocaleCNrI)
			throws RemoteException;

	public void createModulberechtigung(
			ModulberechtigungDto modulberechtigungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeModulberechtigung(
			ModulberechtigungDto modulberechtigungDto) throws RemoteException,
			EJBExceptionLP;

	public ModulberechtigungDto modulberechtigungFindByPrimaryKey(
			String belegartCNr, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public ModulberechtigungDto modulberechtigungFindByPrimaryKeyOhneExc(
			String belegartCNr, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public ModulberechtigungDto[] modulberechtigungFindByMandantCNr(
			String mandantCNr) throws RemoteException, EJBExceptionLP;

	public String zahlungszielFindByIIdLocaleOhneExc(Integer iIdZahlungszielI,
			Locale localeI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public MandantDto[] mandantFindByPartnerIId(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean darfAnwenderAufZusatzfunktionZugreifen(
			String zusatzfunktionCNr, TheClientDto theClientDto);

	public MandantDto[] mandantFindByPartnerIIdOhneExc(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer getNachkommastellenPreisAllgemein(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer getNachkommastellenPreisVK(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer getNachkommastellenPreisEK(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer getNachkommastellenPreisWE(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer getNachkommastellenMenge(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Map<Integer, ZahlungszielDto> zahlungszielFindAllByMandantAsDto(
			String sMandantI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	/**
	 * Alle MWST-Saetze finden. inklusive MWST-Satz-Bezeichnung.
	 * 
	 * @param theClientDto
	 * @return Map
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Map<Integer, MwstsatzDto> mwstsatzFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createZusatzfunktion(ZusatzfunktionDto zusatzfunktionDto)
			throws EJBExceptionLP, RemoteException, EJBExceptionLP;

	public DokumentenlinkbelegDto[] getDokumentenlinkbelegs(String belegartCNr,
			Integer belegartIId, TheClientDto theClientDto);

	public void updateDokumentenlinkbeleg(String belegartCNr,
			Integer iBelegartId,
			DokumentenlinkbelegDto[] dokumentenlinkbelegDtos,
			TheClientDto theClientDto);

	public ZusatzfunktionDto[] zusatzfunktionFindAll() throws EJBExceptionLP,
			RemoteException;

	public void createZusatzfunktionberechtigung(
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto)
			throws EJBExceptionLP, RemoteException;

	public ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCNr(
			String mandantCNr) throws EJBExceptionLP, RemoteException;

	public ZusatzfunktionberechtigungDto zusatzfunktionberechtigungFindByPrimaryKey(
			String zusatzfunktionCNr, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public boolean hatZusatzfunktionberechtigung(String zusatzfunktionCNr,
			TheClientDto theClientDto);

	public Integer createMwstsatzbez(MwstsatzbezDto mwstsatzbezDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException,
			EJBExceptionLP;

	public void removeMwstsatzbez(Integer iId) throws EJBExceptionLP,
			RemoteException, EJBExceptionLP;

	public void removeMwstsatzbez(MwstsatzbezDto mwstsatzbezDto)
			throws RemoteException, EJBExceptionLP;

	/**
	 * MWST-Satz-Bezeichnung updaten.
	 * 
	 * @param mwstsatzbezDto
	 *            MwstsatzbezDto
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void updateMwstsatzbez(MwstsatzbezDto mwstsatzbezDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MwstsatzbezDto mwstsatzbezFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean darfAnwenderAufModulZugreifen(String belegart,
			TheClientDto theClientDto);

	/**
	 * Alle MWST-Saetze eines Mandanten als Dtos in einer Map zurueckgeben. Hier
	 * sind auch die nicht mehr gueltigen enthalten.
	 * 
	 * @param ssMandantI
	 *            String
	 * @param theClientDto
	 * @return Map
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Map<Integer, MwstsatzDto> mwstsatzFindAllByMandantAsDto(
			String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Den Aktuellen MWST-Satz zu einer MWST-Bezeichnung finden.
	 * 
	 * @param mwstsatzbezIId
	 *            String
	 * @param theClientDto
	 * @return MwstsatzDto[]
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public MwstsatzDto mwstsatzFindByMwstsatzbezIIdAktuellster(
			Integer mwstsatzbezIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createDokumentenlink(DokumentenlinkDto dokumentenlinkDto,
			TheClientDto theClientDto);

	public void removeDokumentenlink(DokumentenlinkDto dokumentenlinkDto);

	public DokumentenlinkDto dokumentenlinkFindByPrimaryKey(Integer iId);

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(
			String belegartCNr, String mandantCNr, boolean bPfadAbsolut);

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNr(
			String belegartCNr, String mandantCNr);

	public void updateDokumentenlink(DokumentenlinkDto dokumentenlinkDto);

	public Integer createKostentraeger(KostentraegerDto dto);

	public void updateKostentraeger(KostentraegerDto dto);

	public KostentraegerDto kostentraegerFindByPrimaryKey(Integer iId);

	public void removeKostentraeger(KostentraegerDto dto);

	public MwstsatzDto[] mwstsatzfindAllByMandant(String ssMandantI,
			Timestamp tBelegdatum, boolean bInklHandeingabe)
			throws EJBExceptionLP;

	public MwstsatzDto getMwstSatzVonBruttoBetragUndUst(String mandant,
			Timestamp tBelegDatum, BigDecimal bruttoBetrag,
			BigDecimal mwstBetrag);

	public MwstsatzDto getMwstSatzVonNettoBetragUndUst(String mandant,
			Timestamp tBelegDatum, BigDecimal nettoBetrag, BigDecimal mwstBetrag);

	public MwstsatzbezDto getMwstsatzbezSteuerfrei(TheClientDto theClient);

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale)
			throws EJBExceptionLP;

	public ReportMandantDto createReportMandantDto(TheClientDto theClient);

	public boolean darfAnwenderAufZusatzfunktionZugreifen(
			String zusatzfunktionCNr, String mandantCNr);

	public java.sql.Date berechneFaelligkeitAnhandStichtag(
			java.util.Date dBelegdatum, ZahlungszielDto zahlungszielDto,
			TheClientDto theClientDto);

}
