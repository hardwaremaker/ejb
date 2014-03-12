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
package com.lp.server.finanz.service;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FinanzFac {

	// die anzahl der nachkommastellen ist fix!!!
	public static final int NACHKOMMASTELLEN = 2;
	public static final int NACHKOMMASTELLEN_I = new Integer(NACHKOMMASTELLEN) ;
	
	public static final int MAX_BANKVERBINDUNG_BANKKONTONUMMER = 25;
	public static final int MAX_BANKVERBINDUNG_IBAN = 34;
	public static final int MAX_BANKVERBINDUNG_BEZEICHNUNG = 40;

	public static final int MAX_KONTO_NUMMER = 15;
	public static final int MAX_KONTO_BEZEICHNUNG = 80;

	public static final int MAX_FINANZAMT_STEUERNUMMER = 15;
	public static final int MAX_FINANZAMT_REFERAT = 40;

	public static final int MAX_UMBUCHUNG_AUSZUG = 9;
	public static final int MAX_UMBUCHUNG_TEXT = 40;
	public static final int MAX_UMBUCHUNG_BELEG = 10;

	public static final int MAX_WAEHRUNG_C_NR = 3;
	public static final int MAX_WAEHRUNG_C_KOMMENTAR = 80;

	public static final int ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE = 0;
	public static final int ERGEBNISGRUPPE_TYP_LEEREZEILE = 1;
	public static final int ERGEBNISGRUPPE_TYP_LINIE = 2;
	public static final int ERGEBNISGRUPPE_TYP_SEITENUMBRUCH = 3;
	public static final int ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV = 4;
	public static final int ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV = 5;
	
	public static final String UVA_ABRECHNUNGSZEITRAUM_MONAT = "Monatlich";
	public static final String UVA_ABRECHNUNGSZEITRAUM_QUARTAL = "Quartal";
	public static final String UVA_ABRECHNUNGSZEITRAUM_JAHR = "J\u00E4hrlich";

	public static final String FILTER_WECHSELKURS_VON_ZU = "FILTER_WECHSELKURS_VON_ZU";
	public static final String FILTER_BUCHUNGDETAILS_NUR_OFFENE = "nur offene";
	public static final String FILTER_KONTO_OHNE_MITLAUFENDE = "OHNE_MITLAUFENDE";
	public static final String FILTER_KONTO_OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZBEZ = "OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZBEZ";

	//public static final String BUCHUNGSART_AUSGANGSRECHNUNG = "Ausgangsrechnung";
	public static final String BUCHUNGSART_BANKBUCHUNG = "Bankbuchung";
	//public static final String BUCHUNGSART_GUTSCHRIFT = "Gutschrift";
	//public static final String BUCHUNGSART_EINGANGSRECHNUNG = "Eingangsrechnung";
	public static final String BUCHUNGSART_KASSENBUCHUNG = "Kassenbuchung";
	public static final String BUCHUNGSART_UMBUCHUNG = "Umbuchung";
	public static final String BUCHUNGSART_EROEFFNUNG = "Eroeffnung";
	public static final String BUCHUNGSART_BUCHUNG = "Buchung";
	public static final String BUCHUNGSART_SALDOVORTRAG = "Saldovortrag" ;
	
	public static final String KONTO_SORTIERUNG_DATUM = "Datum";
	public static final String KONTO_SORTIERUNG_AUSZUG = "Auszug";
	public static final String KONTO_SORTIERUNG_BELEG = "Beleg";
	
	
	public static final String FLR_KONTO_I_ID = "i_id";
	public static final String FLR_KONTO_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_KONTO_C_NR = "c_nr";
	public static final String FLR_KONTO_C_BEZ = "c_bez";
	public static final String FLR_KONTO_KONTOTYP_C_NR = "kontotyp_c_nr";
	public static final String FLR_KONTO_FLRSTEUERKATEGORIE = "flrsteuerkategorie";
	public static final String FLR_KONTO_ERGEBNISGRUPPE_I_ID = "ergebnisgruppe_i_id";
	public static final String FLR_KONTO_D_GUELTIGBIS = "d_gueltigbis";
	public static final String FLR_KONTO_B_ALLGEMEINSICHTBAR = "b_allgemeinsichtbar";
	public static final String FLR_KONTO_B_VERSTECKT = "b_versteckt";
	public static final String FLR_KONTO_FLRKONTOUST = "flrkontoust";
	public static final String FLR_KONTO_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_KONTO_FLRERGEBNISGRUPPE = "flrergebnisgruppe";
	public static final String FLR_KONTO_FLRKONTOART = "flrkontoart";
	public static final String FLR_KONTO_KONTOART_C_NR = "kontoart_c_nr";
	public static final String FLR_KONTO_I_ID_WEITERFUEHRENDBILANZ = "konto_i_id_weiterfuehrendbilanz";
	public static final String FLR_KONTO_B_MANUELLBEBUCHBAR = "b_manuellbebuchbar";
	public static final String FLR_KONTO_UVAART_I_ID = "uvaart_i_id";
	public static final String FLR_KONTO_FINANZAMT_I_ID = "finanzamt_i_id";
	public static final String FLR_KONTO_STEUERKATEGORIE_C_NR = "flrsteuerkategorie.c_nr";
	
	public static final String FLR_FINANZAMT_I_ID = "i_id";
	public static final String FLR_FINANZAMT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_FINANZAMT_C_STEUERNUMMER = "c_steuernummer";
	public static final String FLR_FINANZAMT_C_REFERAT = "c_referat";
	public static final String FLR_FINANZAMT_FLRPARTNER = "flrpartner";

	public static final String FLR_BUCHUNGSJOURNAL_I_ID = "i_id";
	public static final String FLR_BUCHUNGSJOURNAL_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BUCHUNGSJOURNAL_T_ANLEGEN = "t_anlegen";

	public static final String FLR_BANKKONTO_I_ID = "i_id";
	public static final String FLR_BANKKONTO_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BANKKONTO_C_KONTONUMMER = "c_kontonummer";
	public static final String FLR_BANKKONTO_C_IBAN = "c_iban";
	public static final String FLR_BANKKONTO_C_BEZ = "c_bez";
	public static final String FLR_BANKKONTO_FLRBANK = "flrbank";
	public static final String FLR_BANKKONTO_FLRKONTO = "flrkonto";

	public static final String FLR_KASSENBUCH_I_ID = "i_id";
	public static final String FLR_KASSENBUCH_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_KASSENBUCH_C_BEZ = "c_bez";
	public static final String FLR_KASSENBUCH_B_HAUPTKASSENBUCH = "b_hauptkassenbuch";
	public static final String FLR_KASSENBUCH_FLRKONTO = "flrkonto";

	public static final String FLR_BUCHUNG_I_ID = "i_id";
	public static final String FLR_BUCHUNG_C_TEXT = "c_text";
	public static final String FLR_BUCHUNG_C_BELEGNUMMER = "c_belegnummer";
	public static final String FLR_BUCHUNG_D_BUCHUNGSDATUM = "d_buchungsdatum";
	public static final String FLR_BUCHUNG_BUCHUNGSART_C_NR = "buchungsart_c_nr";
	public static final String FLR_BUCHUNG_T_ANLEGEN = "t_anlegen";
	public static final String FLR_BUCHUNG_T_STORNIERT = "t_storniert";
	public static final String FLR_BUCHUNG_GESCHAEFTSJAHR_I_GESCHAEFTSJAHR = "geschaeftsjahr_i_geschaeftsjahr";
	public static final String FLR_BUCHUNG_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_BUCHUNG_AUTOMBUCHUNG_EB = "b_autombuchungeb" ;
	public static final String FLR_BUCHUNGDETAIL_I_ID = "i_id";
	public static final String FLR_BUCHUNGDETAIL_BUCHUNG_I_ID = "buchung_i_id";
	public static final String FLR_BUCHUNGDETAIL_KONTO_I_ID = "konto_i_id";
	public static final String FLR_BUCHUNGDETAIL_N_BETRAG = "n_betrag";
	public static final String FLR_BUCHUNGDETAIL_N_UST = "n_ust";
	public static final String FLR_BUCHUNGDETAIL_I_AUSZUG = "i_auszug";
	public static final String FLR_BUCHUNGDETAIL_FLRBUCHUNG = "flrbuchung";
	public static final String FLR_BUCHUNGDETAIL_FLRGEGENKONTO = "flrgegenkonto";
	public static final String FLR_BUCHUNGDETAIL_FLRKONTO = "flrkonto";
	public static final String FLR_BUCHUNGDETAIL_I_AUSZIFFERN = "i_ausziffern";
	public static final String FLR_BUCHUNGDETAIL_KOMMENTAR = "kommentar";
	
	public static final String FLR_UVAART_I_ID = "i_id";
	public static final String FLR_UVAART_C_NR = "c_nr";
	public static final String FLR_UVAART_C_KENNZEICHEN = "c_kennzeichen";
	public static final String FLR_UVAART_I_SORT = "i_sort";

	public static final String FLR_UVAARTSPR_I_ID = "i_id";
	public static final String FLR_UVAARTSPR_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_UVAARTSPR_C_BEZ = "c_bez";

	public static final String FLR_KONTOART_C_NR = "c_nr";
	public static final String FLR_KONTOART_I_SORT = "i_sort";

	public static final String FLR_LAENDERART_C_NR = "c_nr";
	public static final String FLR_LAENDERART_I_SORT = "i_sort";

	public static final String FLR_KONTOARTSPR_C_NR = "c_nr";
	public static final String FLR_KONTOARTSPR_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_KONTOARTSPR_C_BEZ = "c_bez";

	public static final String FLR_MAHNLAUF_I_ID = "i_id";
	public static final String FLR_MAHNLAUF_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNLAUF_T_ANLEGEN = "t_anlegen";
	public static final String FLR_MAHNUNG_FLRPERSONALANLEGER = "flrpersonalanleger";
	public static final String FLR_MAHNUNG_I_ID = "i_id";
	public static final String FLR_MAHNUNG_MAHNLAUF_I_ID = "mahnlauf_i_id";
	public static final String FLR_MAHNUNG_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNUNG_D_MAHNDATUM = "d_mahndatum";
	public static final String FLR_MAHNUNG_T_GEDRUCKT = "t_gedruckt";
	public static final String FLR_MAHNUNG_FLRRECHNUNGREPORT = "flrrechnungreport";

	public static final String FLR_ERGEBNISGRUPPE_I_ID = "i_id";
	public static final String FLR_ERGEBNISGRUPPE_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_ERGEBNISGRUPPE_C_BEZ = "c_bez";
	public static final String FLR_ERGEBNISGRUPPE_ERGEBNISGRUPPE_I_ID_SUMME = "ergebnisgruppe_i_id_summe";
	public static final String FLR_ERGEBNISGRUPPE_B_SUMME_NEGATIV = "b_summe_negativ";
	public static final String FLR_ERGEBNISGRUPPE_B_INVERTIERT = "b_invertiert";
	public static final String FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE = "b_bilanzgruppe" ;
	public static final String FLR_ERGEBNISGRUPPE_B_PROZENTBASIS = "b_prozentbasis";
	public static final String FLR_ERGEBNISGRUPPE_I_TYP = "i_typ";
	public static final String FLR_ERGEBNISGRUPPE_I_REIHUNG = "i_reihung";
	public static final String FLR_ERGEBNISGRUPPE_FLRERGEBNISGRUPPE_SUMME = "flrergebnisgruppe_summe";

	public static final String FLR_BUCHUNGSREGEL_I_ID = "i_id";
	public static final String FLR_BUCHUNGSREGEL_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BUCHUNGSREGEL_C_BEZ = "c_bez";
	public static final String FLR_BUCHUNGSREGEL_BUCHUNGSREGELSCHIENE_C_NR = "buchungsregelschiene_c_nr";
	public static final String FLR_BUCHUNGSREGEL_LAENDERART_C_NR = "laenderart_c_nr";
	public static final String FLR_BUCHUNGSREGEL_FLRFINANZAMT = "flrfinanzamt";
	public static final String FLR_BUCHUNGSREGEL_FLRSKONTOKONTO = "flrskontokonto";

	public static final String FLR_BUCHUNGSREGELGEGENKONTO_I_ID = "i_id";
	public static final String FLR_BUCHUNGSREGELGEGENKONTO_BUCHUNGSREGEL_I_ID = "buchungsregel_i_id";
	public static final String FLR_BUCHUNGSREGELGEGENKONTO_FLRKONTO_VON = "flrkonto_von.nummer";
	public static final String FLR_BUCHUNGSREGELGEGENKONTO_FLRKONTO_BIS = "flrkonto_bis.nummer";

	public static final String FLR_MAHNSTUFE_I_ID = "i_id";
	public static final String FLR_MAHNSTUFE_MANDANT_C_NR = "i_tage";
	public static final String FLR_MAHNSTUFE_I_TAGE = "i_tage";
	public static final String FLR_MAHNSTUFE_N_MAHNSPESEN = "n_mahnspesen";
	public static final String FLR_MAHNSTUFE_F_ZINSSATZ = "f_zinssatz";

	public static final String FLR_MAHNSPESEN_I_ID = "i_id";
	public static final String FLR_MAHNSPESEN_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNSPESEN_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNSPESEN_N_MAHNSPESEN = "n_mahnspesen";

	
	public static final String FLR_KONTOLAENDERART_ID_COMP = "id_comp";
	public static final String FLR_KONTOLAENDERART_KONTO_I_ID = "konto_i_id";
	public static final String FLR_KONTOLAENDERART_LAENDERART_C_NR = "laenderart_c_nr";
	public static final String FLR_KONTOLAENDERART_FLRKONTO = "flrkonto";
	public static final String FLR_KONTOLAENDERART_FLRKONTOUEBERSETZT = "flrkonto_uebersetzt";
	public static final String FLR_KONTOLAENDERART_FINANZAMT_I_ID = "finanzamt_i_id";
	
	public static final String FLR_KONTOLAND_ID_COMP = "id_comp";
	public static final String FLR_KONTOLAND_KONTO_I_ID = "konto_i_id";
	public static final String FLR_KONTOLAND_FLRLAND = "flrland";
	public static final String FLR_KONTOLAND_FLRKONTO = "flrkonto";
	public static final String FLR_KONTOLAND_FLRKONTOUEBERSETZT = "flrkonto_uebersetzt";

	public static final String FLR_EXPORTLAUF_I_ID = "i_id";
	public static final String FLR_EXPORTLAUF_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_EXPORTLAUF_T_AENDERN = "t_aendern";
	public static final String FLR_EXPORTLAUF_T_STICHTAG = "t_stichtag";

	public static final String FLR_EXPORTDATEN_I_ID = "i_id";
	public static final String FLR_EXPORTDATEN_FLREXPORTLAUF = "flrexportlauf";
	public static final String FLR_EXPORTDATEN_FLRBELEGART = "flrbelegart";
	public static final String FLR_EXPORTDATEN_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_EXPORTDATEN_I_BELEGIID = "i_belegiid";

	public static final String BUCHUNGSREGELSCHIENE_AR = "Ausgangsrechnung";
	public static final String BUCHUNGSREGELSCHIENE_ER = "Eingangsrechnung";

	public static final String LAENDERART_DRITTLAND = "Drittland";
	public static final String LAENDERART_EU_AUSLAND_MIT_UID = "EU-Ausland mit UID-Nr.";
	public static final String LAENDERART_EU_AUSLAND_OHNE_UID = "EU-Ausland ohne UID-Nr.";
	public static final String LAENDERART_INLAND = "Inland";
	public static final String LAENDERART_SONDERLAND = "Sonderland";
	public static final String LAENDERART_REVCHARGE_INLAND = "Reversecharge Inland";
	public static final String LAENDERART_REVCHARGE_AUSLAND = "Reversecharge Ausland";

	public KontoDto createKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KontoDto updateKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KontoDto kontoFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public KontoDtoSmall kontoFindByPrimaryKeySmall(Integer iId)
			throws EJBExceptionLP, RemoteException;

	@Deprecated	
	public KontoDto kontoFindByCnrMandant(String sCnr, String sMandant,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public FinanzamtDto createFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FinanzamtDto updateFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FinanzamtDto finanzamtFindByPrimaryKey(Integer partnerIId,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(
			Integer partnerIId, String mandantCNr);

	public BankverbindungDto createBankverbindung(
			BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBankverbindung(BankverbindungDto bankverbindungDto)
			throws EJBExceptionLP, RemoteException;

	public BankverbindungDto updateBankverbindung(
			BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BankverbindungDto bankverbindungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BankverbindungDto bankverbindungFindByKontoIIdOhneExc(
			Integer kontoIId) throws EJBExceptionLP, RemoteException;

	public KassenbuchDto createKassenbuch(KassenbuchDto kassenbuchDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKassenbuch(KassenbuchDto kassenbuchDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KassenbuchDto updateKassenbuch(KassenbuchDto kassenbuchDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KassenbuchDto kassenbuchFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void pruefeZykel(KontoDto kontoDto) throws EJBExceptionLP,
			RemoteException;

	public RechenregelDto rechenregelFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public ErgebnisgruppeDto createErgebnisgruppe(
			ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ErgebnisgruppeDto updateErgebnisgruppe(
			ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ErgebnisgruppeDto ergebnisgruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public KontoDtoSmall kontoFindByPrimaryKeySmallOhneExc(Integer iIdI)
			throws RemoteException;

	/**
	 * 
	 * @param cNrI
	 * @param sMandantI
	 * @param theClientDto
	 * @return KontoDto passend zur cNrI
	 * @throws RemoteException
	 * 
	 * @deprecated use {@link #kontoFindByCnrKontotypMandantOhneExc(String, String, String, TheClientDto)}
	 * @see #kontoFindByCnrKontotypMandantOhneExc(String, String, String, TheClientDto)
	 */
	@Deprecated
	public KontoDto kontoFindByCnrMandantOhneExc(String cNrI, String sMandantI,
			TheClientDto theClientDto) throws RemoteException;

	public KontoDto kontoFindByCnrKontotypMandantOhneExc(
		String cNrI, String sKontoTypCnr, String sMandantI, TheClientDto theClientDto) throws RemoteException;

	/**
	 * Ist das Konto ein mitlaufendes Konto?
	 * Mitlaufende Konten sind jene, welche in den Steuerkategorien
	 * als Forderungs- und Verbindlichkeitskonten hinterlegt sind.
	 * @param kontoIId
	 * @return true wenn es ein mitlaufendes Konto ist
	 * @throws RemoteException
	 */
	public boolean isMitlaufendesKonto(Integer kontoIId) throws RemoteException;
	
	/**
	 * Alle Konten vom angegebenen Typ f&uuml;r den Mandanten zur&uuml;ckliefern
	 * 
	 * @param kontotypCNr der gesuchte Kontotyp #see {@link FinanzServiceFac#KONTOTYP_DEBITOR}
	 * @param mandantCNr der gesuchte Mandant
	 * @return ein (leeres) Array von Konten des gesuchten Kontotyps
	 * @throws EJBExceptionLP
	 */
	public KontoDto[] kontoFindAllByKontotypMandant(String kontotypCNr, String mandantCNr) throws EJBExceptionLP ;

	public KontoDto[] kontoFindAllByKontoartMandant(String kontoartCNr,	String mandantCNr) throws EJBExceptionLP ;
	
	public KontoDto[] kontoFindAllByKontoartMandantFinanzamt(String kontoartCNr,	String mandantCNr, Integer finanzamtIid) throws EJBExceptionLP ;

	public Integer getAnzahlStellenVonKontoNummer(String kontotypCNr,
			String mandantCNr) throws EJBExceptionLP, RemoteException;

	public KontolaenderartDto createKontolaenderart(
			KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKontolaenderart(KontolaenderartDto kontolaenderartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KontolaenderartDto updateKontolaenderart(
			KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontoIId, String laenderartCNr,
			Integer finanzamtIId, String mandantCNr) throws EJBExceptionLP, RemoteException;

	public KontolaenderartDto kontolaenderartFindByPrimaryKeyOhneExc(Integer kontoIId, String laenderartCNr,
			Integer finanzamtIId, String mandantCNr) throws EJBExceptionLP,	RemoteException;

	public void pruefeBuchungszeitraum(Date d, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KontoDto createKontoFuerPartnerAutomatisch(PartnerDto partnerDto,
			String kontotypCNr, boolean kontoAnlegen, String kontonummerVorgabe, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void updateKontoDtoUIDaten(Integer kontoIIdI,
			String cLetztesortierungI, Integer iLetzteselektiertebuchungI)
			throws EJBExceptionLP, RemoteException;

	public int getAnzahlDerFinanzaemter(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheErgebnisgruppen(Integer iIdEG1I, Integer iIdEG2I,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public FinanzamtDto[] finanzamtFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BankverbindungDto bankverbindungFindByBankIIdMandantCNrCKontonummerOhneExc(
			Integer bankIId, String mandantCNr, String cKontonummer)
			throws EJBExceptionLP, RemoteException;

	public void createKontoland(KontolandDto kontolandDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeKontoland(KontolandDto kontolandDto)
			throws RemoteException;

	public void updateKontoland(KontolandDto kontolandDto, TheClientDto theClientDto)
			throws RemoteException;

	public void updateKontolands(KontolandDto[] kontolandDtos, TheClientDto theClientDto)
			throws RemoteException;

	public KontolandDto kontolandFindByPrimaryKey(Integer kontoIId,
			Integer LandIId) throws RemoteException;
	
	public KontolandDto kontolandFindByPrimaryKeyOhneExc(Integer kontoIId,
			Integer LandIId) throws RemoteException;

	/**
	 * Handelt es sich beim angegebenen Konto um eines welches einen Vorperiodensaldo unterstuetzt
	 * 
	 * @param kontoIId
	 * @param theClientDto
	 * @return true wenn das Konto einen Vorperiodensaldo kennt. Also beispielsweise Bankkonto oder
	 * Kassenbuch
	 */
	public boolean isKontoMitSaldo(Integer kontoIId, TheClientDto theClientDto) ;

	public KassenbuchDto kassenbuchFindByKontoIIdOhneExc(Integer kontoIId) throws EJBExceptionLP ;

	public FinanzamtDto[] finanzamtFindAllByMandantCNr(TheClientDto theClientDto);

	public SteuerkategorieDto getSteuerkategorieZuLaenderart(
			Integer finanzamtIId, String laenderart, TheClientDto theClientDto);

	public KontoImporterResult importCsv(List<String[]> allLines,
			boolean checkOnly, TheClientDto theClientDto) ;

	public ErgebnisgruppeDto[] ergebnisgruppeFindAll(TheClientDto theClientDto, boolean bBilanzgruppe)
		throws EJBExceptionLP;

}
