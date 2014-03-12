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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface VersandFac {
	public final static int MAX_EMPFAENGER = 300;
	public final static int MAX_CCEMPFAENGER = 300;
	public final static int MAX_BETREFF = 100;
	public final static int MAX_ABSENDER = 100;
	public final static int MAX_STATUSTEXT = 100;
	public final static int MAX_DATEINAME = 260;

	public final static String MAIL_PARAMETER_ANREDE_ANSPRECHPARTNER = "anrede_anprechpartner";
	public final static String MAIL_PARAMETER_BELEGNUMMER = "belegnummer";
	public final static String MAIL_PARAMETER_BEZEICHNUNG = "bezeichnung";
	public final static String MAIL_PARAMETER_BELEGDATUM = "belegdatum";
	public final static String MAIL_PARAMETER_PROJEKT = "projekt";
	public final static String MAIL_PARAMETER_BEARBEITER = "bearbeiter";
	public final static String MAIL_PARAMETER_FUSSTEXT = "fusstext";
	public final static String MAIL_PARAMETER_TEXT = "text";
	public final static String MAIL_PARAMETER_KUNDENBESTELLNUMMER = "kundenbestellnummer";
	public final static String MAIL_PARAMETER_ABNUMMER = "abnummer";

	public final static String MAIL_PARAMETER_BEARBEITER_VORNAME = "bearbeiter_vorname";
	public final static String MAIL_PARAMETER_BEARBEITER_NACHNAME = "bearbeiter_nachname";
	public final static String MAIL_PARAMETER_BEARBEITER_TITEL = "bearbeiter_titel";
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

	public final static String MAIL_PARAMETER_VERTRETER_VORNAME = "vertreter_vorname";
	public final static String MAIL_PARAMETER_VERTRETER_NACHNAME = "vertreter_nachname";
	public final static String MAIL_PARAMETER_VERTRETER_TITEL = "vertreter_titel";
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

	public String getDefaultTextForBelegEmail(MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

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

	public String getVersandstatus(String belegartCNr, Integer i_belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getFormattedVersandstatus(String belegartCNr,
			Integer i_belegIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public VersandanhangDto createVersandanhang(
			VersandanhangDto versandanhangDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(
			Integer versandauftragIID) throws EJBExceptionLP, RemoteException;

	public VersandanhangDto versandanhangFindByPrimaryKeyOhneExc(Integer iId);

	public VersandauftragDto versandauftragFindNextNotInDoc();
	
	public VersandanhangDto updateVersandanhang(VersandanhangDto versandanhangDto);
}
