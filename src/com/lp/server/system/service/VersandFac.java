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

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.ejb.Remote;
import javax.persistence.PersistenceException;

import com.lp.server.system.jcr.service.FehlerVersandauftraegeDto;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailTestMessage;
import com.lp.server.system.mail.service.MailTestMessageResult;
import com.lp.util.EJBExceptionLP;

@Remote
public interface VersandFac {
	public final static int MAX_EMPFAENGER = 300;
	public final static int MAX_CCEMPFAENGER = 300;
	public final static int MAX_BETREFF = 100;
	public final static int MAX_ABSENDER = 100;
	public final static int MAX_STATUSTEXT = 100;
	public final static int MAX_DATEINAME = 260;
	public final static int MAX_BCCEMPFAENGER = 300;

	public final static String MAIL_PARAMETER_ANREDE_ANSPRECHPARTNER = "anrede_anprechpartner";
	public final static String MAIL_PARAMETER_BELEGNUMMER = "belegnummer";
	public final static String MAIL_PARAMETER_BEZEICHNUNG = "bezeichnung";
	public final static String MAIL_PARAMETER_BELEGDATUM = "belegdatum";
	public final static String MAIL_PARAMETER_BELEGDATUM_YYMMDD = "belegdatumYYMMDD";
	public final static String MAIL_PARAMETER_PROJEKT = "projekt";
	public final static String MAIL_PARAMETER_PROJEKT1 = "projekt1";
	public final static String MAIL_PARAMETER_BEARBEITER = "bearbeiter";
	public final static String MAIL_PARAMETER_FUSSTEXT = "fusstext";
	public final static String MAIL_PARAMETER_TEXT = "text";
	public final static String MAIL_PARAMETER_KUNDENBESTELLNUMMER = "kundenbestellnummer";
	public final static String MAIL_PARAMETER_ABNUMMER = "abnummer";

	public final static String MAIL_PARAMETER_BEARBEITER_VORNAME = "bearbeiter_vorname";
	public final static String MAIL_PARAMETER_BEARBEITER_NACHNAME = "bearbeiter_nachname";
	public final static String MAIL_PARAMETER_BEARBEITER_TITEL = "bearbeiter_titel";
	public final static String MAIL_PARAMETER_BEARBEITER_NTITEL = "bearbeiter_ntitel";
	public final static String MAIL_PARAMETER_BEARBEITER_TELEFONFIRMA = "bearbeiter_telefonfirma";
	public final static String MAIL_PARAMETER_BEARBEITER_TELEFONDWFIRMA = "bearbeiter_telefondwfirma";
	public final static String MAIL_PARAMETER_BEARBEITER_EMAIL = "bearbeiter_email";
	public final static String MAIL_PARAMETER_BEARBEITER_MOBIL = "bearbeiter_mobil";
	public final static String MAIL_PARAMETER_BEARBEITER_TELEFONFIRMAMITDW = "bearbeiter_telefonfirmamitdw";
	public final static String MAIL_PARAMETER_BEARBEITER_DIREKTFAX = "bearbeiter_direktfax";
	public final static String MAIL_PARAMETER_BEARBEITER_FAXDWFIRMA = "bearbeiter_faxdwfirma";
	public final static String MAIL_PARAMETER_BEARBEITER_FAXFIRMAMITDW = "bearbeiter_faxfirmamitdw";	
	public final static String MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSFUNKTION = "bearbeiter_unterschriftsfunktion";	
	public final static String MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSTEXT = "bearbeiter_unterschriftstext";
	public final static String MAIL_PARAMETER_BEARBEITER_SIGNATUR = "bearbeiter_signatur";

	public final static String MAIL_PARAMETER_VERTRETER_VORNAME = "vertreter_vorname";
	public final static String MAIL_PARAMETER_VERTRETER_NACHNAME = "vertreter_nachname";
	public final static String MAIL_PARAMETER_VERTRETER_TITEL = "vertreter_titel";
	public final static String MAIL_PARAMETER_VERTRETER_NTITEL = "vertreter_ntitel";
	public final static String MAIL_PARAMETER_VERTRETER_TELEFONFIRMA = "vertreter_telefonfirma";
	public final static String MAIL_PARAMETER_VERTRETER_TELEFONDWFIRMA = "vertreter_telefondwfirma";
	public final static String MAIL_PARAMETER_VERTRETER_EMAIL = "vertreter_email";
	public final static String MAIL_PARAMETER_VERTRETER_MOBIL = "vertreter_mobil";
	public final static String MAIL_PARAMETER_VERTRETER_TELEFONFIRMAMITDW = "vertreter_telefonfirmamitdw";
	public final static String MAIL_PARAMETER_VERTRETER_DIREKTFAX = "vertreter_direktfax";
	public final static String MAIL_PARAMETER_VERTRETER_FAXDWFIRMA = "vertreter_faxdwfirma";
	public final static String MAIL_PARAMETER_VERTRETER_FAXFIRMAMITDW = "vertreter_faxfirmamitdw";	
	public final static String MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSFUNKTION = "vertreter_unterschriftsfunktion";	
	public final static String MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSTEXT = "vertreter_unterschriftstext";
	public final static String MAIL_PARAMETER_VERTRETER_SIGNATUR = "vertreter_signatur";
	
	public final static String MAIL_PARAMETER_PERSON_ANDREDE = "person_anrede";
	public final static String MAIL_PARAMETER_PERSON_PERSONALNUMMER = "person_personalnummer";
	public final static String MAIL_PARAMETER_PERSON_TITEL = "person_titel";	
	public final static String MAIL_PARAMETER_PERSON_VORNAME1 = "person_vorname1";	
	public final static String MAIL_PARAMETER_PERSON_VORNAME2 = "person_vorname2";	
	public final static String MAIL_PARAMETER_PERSON_NACHNAME = "person_nachname";	
	public final static String MAIL_PARAMETER_PERSON_NTITEL = "person_ntitel";	
	
	public final static String MAIL_PARAMETER_LS_VERSANDNUMMER = "ls_versandnummer";	
	public final static String MAIL_PARAMETER_LS_VERSANDNUMMER2 = "ls_versandnummer2";	
	
	public final static String MAIL_PARAMETER_LS_SPEDITEUR_NAME = "ls_spediteur_name";
	public final static String MAIL_PARAMETER_LS_SPEDITEUR_WEBSITE = "ls_spediteur_website";
	
	public final static String MAIL_PARAMETER_REKLA_LIEFERSCHEIN = "rekla_lieferschein";	
	public final static String MAIL_PARAMETER_REKLA_RECHNUNG = "rekla_rechung";	
	public final static String MAIL_PARAMETER_REKLA_KNDREKLANR = "rekla_kndreklanr";	
	public final static String MAIL_PARAMETER_REKLA_KNDLSNR = "rekla_kndlsnr";	
	public final static String MAIL_PARAMETER_REKLA_WE_LSNR = "rekla_we_lsnr";	
	public final static String MAIL_PARAMETER_REKLA_WE_WEDATUM = "rekla_we_wedatum";	
	public final static String MAIL_PARAMETER_REKLA_WE_LSDATUM = "rekla_we_lsdatum";	

	public final static String MAIL_PARAMETER_FIRMA_NAME1 = "firma_name1";	
	public final static String MAIL_PARAMETER_FIRMA_NAME2 = "firma_name2";	
	public final static String MAIL_PARAMETER_FIRMA_NAME3 = "firma_name3";	

	public final static String MAIL_PARAMETER_FIRMA_LKZ = "firma_lkz";	
	public final static String MAIL_PARAMETER_FIRMA_PLZ = "firma_plz";	
	public final static String MAIL_PARAMETER_FIRMA_ORT = "firma_ort";	
	public final static String MAIL_PARAMETER_FIRMA_STRASSE = "firma_strasse";	

	public final static String MAIL_PARAMETER_PROJEKTNUMMER = "projektnummer";
	public final static String MAIL_PARAMETER_ANGEBOT_ANFRAGENUMMER = "angebot_anfragenummer";
	
	public final static String MAIL_PARAMETER_ANWESENHEIT_LFDNR = "anwesenheit_lfdnr";
	
	public final static String MAIL_PARAMETER_PARTNER_NAME1 = "partner_name1";
	public final static String MAIL_PARAMETER_PARTNER_NAME2 = "partner_name2";	
	public final static String MAIL_PARAMETER_PARTNER_NAME3 = "partner_name3";	
	public final static String MAIL_PARAMETER_PARTNER_LKZ = "partner_lkz";	
	public final static String MAIL_PARAMETER_PARTNER_PLZ = "partner_plz";	
	public final static String MAIL_PARAMETER_PARTNER_ORT = "partner_ort";	
	public final static String MAIL_PARAMETER_PARTNER_STRASSE = "partner_strasse";
	public final static String MAIL_PARAMETER_PARTNER_KBEZ = "partner_kbez";
	
	public final static String MAIL_PARAMETER_BIS_DATUM = "bis_datum";
	
	public final static String MAIL_PARAMETER_XLS_MAILVERSAND_PROJEKT = "xlsmailversand_projekt";
	public final static String MAIL_PARAMETER_XLS_MAILVERSAND_ENDKUNDE = "xlsmailversand_endkunde";
	public final static String MAIL_PARAMETER_XLS_MAILVERSAND_ABGABETERMIN = "xlsmailversand_abgabetermin";
	public final static String MAIL_PARAMETER_XLS_MAILVERSAND_GEPLANTERFERTIGUNGSTERMIN = "xlsmailversand_geplanterfertigungstermin";
	
	public final static String MAIL_PARAMETER_BELEG_VERSION = "beleg_version";
	public final static String MAIL_PARAMETER_RECHNUNGSART = "rechnungsart";
	public final static String MAIL_PARAMETER_KOPFTEXT = "kopftext";
	
	public final static String MAIL_PARAMETER_MANDANT_HOMEPAGE = "mandant_homepage";
	public final static String MAIL_PARAMETER_MANDANT_GERICHTSSTAND = "mandant_gerichtsstand";
	public final static String MAIL_PARAMETER_MANDANT_FIRMENBUCHNR = "mandant_firmenbuchnr";
	public final static String MAIL_PARAMETER_MANDANT_UID = "mandant_uid";
	public final static String MAIL_PARAMETER_MANDANT_FAX = "mandant_fax";
	public final static String MAIL_PARAMETER_MANDANT_GESCHAEFSTFUEHRER = "mandant_geschaeftsfuehrer";
	
	// TODO: Wo werden diese falschen Pfade verwendet? (ghp)
	public final static String LOGO_IMAGE = "c:/jboss-4.0.1/server/helium/report/allgemein/logo.png";
	public final static String LOGO_SUBREPORT = "c:/jboss-4.0.1/server/helium/report/allgemein/logo.jasper";
	public final static String FUSSZEILEN = "c:/jboss-4.0.1/server/helium/report/allgemein/fusszeilen.jasper";

	// flrconst: 0 hier werden die Spaltennamen aus dem Mapping definiert.
	// sie muessen im package service sein, da sie auch vom client verwendet
	// werden
	public static final String FLR_VERSANDAUFTRAG_I_ID = "i_id";
	public static final String FLR_VERSANDAUFTRAG_C_EMPFAENGER = "c_empfaenger";
	public static final String FLR_VERSANDAUFTRAG_C_BETREFF = "c_betreff";
	public static final String FLR_VERSANDAUFTRAG_C_ABSENDERADRESSE = "c_absenderadresse";
	public static final String FLR_VERSANDAUFTRAG_T_SENDEZEITPUNKTWUNSCH = "t_sendezeitpunktwunsch";
	public static final String FLR_VERSANDAUFTRAG_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_VERSANDAUFTRAG_I_BELEGIID = "i_belegiid";
	public static final String FLR_VERSANDAUFTRAG_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_VERSANDAUFTRAG_STATUS_C_NR = "status_c_nr";
	public static final String FLR_VERSANDAUFTRAG_T_SENDEZEITPUNKT = "t_sendezeitpunkt";
	public static final String FLR_VERSANDAUFTRAG_FLREMPFAENGER = "flrempfaenger";
	public static final String FLR_VERSANDAUFTRAG_FLRSENDER = "flrsender";

	public final static String STATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public final static String STATUS_FEHLGESCHLAGEN = LocaleFac.STATUS_FEHLGESCHLAGEN;
	public final static String STATUS_DATEN_UNGUELTIG = LocaleFac.STATUS_DATEN_UNGUELTIG;
	public final static String STATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public final static String STATUS_TEILERLEDIGT = LocaleFac.STATUS_TEILERLEDIGT;

	public VersandauftragDto createVersandauftrag(
			VersandauftragDto versandauftragDto,boolean bDokumenteMitanhaengen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VersandauftragDto updateVersandauftrag(
			VersandauftragDto versandauftragDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VersandauftragDto versandauftragFindByPrimaryKeyOhneExc(Integer iId);
	
	public VersandauftragDto versandauftragFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto, String belegartCNr,
			Integer iIdBeleg, Locale locSprache, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	String getDefaultTextForBelegEmail(MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	String getDefaultTextForBelegHtmlEmail(MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP ;
	
	public String getDefaultDateinameForBelegEmail(String belegartCNr,
			Integer iIdBeleg, Locale locSprache, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createVersandstatus(VersandstatusDto versandstatusDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public VersandstatusDto versandstatusFindByPrimaryKey(String statusCNr)
			throws RemoteException, EJBExceptionLP;

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIId(
			Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIIdOhneExc(
			Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public VersandauftragDto[] versandauftragFindBySenderPartnerIId(
			Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public VersandauftragDto[] versandauftragFindBySenderPartnerIIdOhneExc(
			Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public Integer versandauftragFindFehlgeschlagenen(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void removeVersandauftrag(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP;
	
	public void sendeVersandauftragErneut(Integer versandauftragIId,
			Timestamp tSendezeitpunktWunsch, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void storniereVersandauftrag(Integer versandauftragIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void sendeVersandauftragSofort(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void sendeVersandauftragZeitpunktWunsch(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getVersandstatus(String belegartCNr, Integer i_belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getFormattedVersandstatus(String belegartCNr,
			Integer i_belegIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public VersandanhangDto createVersandanhang(
			VersandanhangDto versandanhangDto, TheClientDto theClientDto);

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(
			Integer versandauftragIID) throws EJBExceptionLP, RemoteException;

	public VersandanhangDto versandanhangFindByPrimaryKeyOhneExc(Integer iId);

	public VersandauftragDto versandauftragFindNextNotInDoc();
	
	VersandanhangDto updateVersandanhang(VersandanhangDto versandanhangDto);
	void createVersandanhaenge(List<VersandanhangDto> alAnhaenge, TheClientDto theClientDto);
	public String getUebersteuertenAbsenderFuerBelegEmail(
			MailtextDto mailtextDto,  TheClientDto theClientDto) throws EJBExceptionLP;

	String getDefaultAnhangForBelegEmail(MailtextDto mailtextDto, String belegartCNr, Integer iIdBeleg,
			Locale locSprache, TheClientDto theClientDto);

	String getUebersteuertenDateinamenForBeleg(MailtextDto mailtextDto, String belegartCNr, Integer iIdBeleg,
			Locale locSprache, TheClientDto theClientDto);

	MailTestMessageResult testMailConfiguration(MailTestMessage testMessage, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Einen Versandauftrag und optionale Anhaenge in der DatenDB
	 * speichern, in die Dokumentablage speichern und dann per E-Mail/Fax
	 * versenden.</br>
	 * <p>Diese optionalen Anhaenge sind jene, die der Benutzer selbst noch
	 * hinzufuegen will. Die Belegspezifischen (Lieferschein) und/oder
	 * Anhaenge durch Artikelkommentare werden immer entsprechend dem 
	 * Beleg erzeugt.</p>
	 * <p>
	 * Hinweis: Neben dem eigentliche Erzeugen der Versandauftrags und
	 * der optionalen Anhaenge geht es darum, dass weder der Versandauftrag
	 * noch die Anhaenge selbst die Dokumentenablage erzeugen. Das soll 
	 * einzig und alleine der saveVersandauftrag machen. Es geht darum, 
	 * dass die ganzen Dokumentenablagemodifikationen in der gleichen 
	 * JcrSession erfolgen.</p>
	 * 
	 */
	VersandauftragDto createVersandauftrag(
			VersandauftragDto versandauftragDto,
			List<VersandanhangDto> anhaenge,
			boolean dokumenteAnhaengen, TheClientDto theClientDto);

	List<VersandauftragDto> createVersandauftrags(
			List<VersandauftragDto> versandauftragDtos,
			boolean dokumenteAnhaengen, TheClientDto theClientDto);
	
	VersandauftragDto[] versandauftragFindOffen(Integer anzahl)
			throws EJBExceptionLP;

	VersandauftragDto[] versandauftragFindAblage(Integer anzahl)
			throws EJBExceptionLP;

	public FehlerVersandauftraegeDto getOffeneUndFehlgeschlageneAuftraege( TheClientDto theClientDto);
		
	void ablageIMAP(Integer iid, String cMandant);

	void anhaengeLoeschen(Integer versandauftragIId);
	
	void performJobSmtp(TheClientDto theClientDto);
	
	void performJobImap(TheClientDto theClientDto);
	
	boolean setJobid(Integer versandauftragIId, String jobId) throws PersistenceException;
	
	boolean sendMail(VersandauftragDto versandauftragDto, LPMailDto lpMailDto);
	
	boolean setStatus(Integer versandauftragIId, String statusCNr, String cStatustext, 
			Timestamp tSendezeitpunkt, byte[] oMessage, boolean clearInhalt);

}
