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
package com.lp.server.benutzer.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BenutzerFac {

	public final static String REPORT_BENUTZERSTATISTIK = "pers_benutzerstatistik.jasper";

	
	public static final String FLR_BENUTZER_C_BENUTZERKENNUNG = "c_benutzerkennung";
	public static final String FLR_BENUTZER_D_GUELTIGBIS = "t_gueltigbis";
	public static final String FLR_BENUTZER_FLRMANDANTDEFAULT = "flrmandantdefault";
	public static final String FLR_BENUTZER_FLRPERSONAL = "flrpersonal";

	public static final String FLR_SYSTEMROLLE_C_BEZ = "c_bez";

	public static final String FLR_BENUTZERMANDANTSYSTEMROLLE_FLRBENUTZER = "flrbenutzer";
	public static final String FLR_BENUTZERMANDANTSYSTEMROLLE_FLRSYSTEMROLLE = "flrsystemrolle";
	public static final String FLR_BENUTZERMANDANTSYSTEMROLLE_FLRMANDANT = "flrmandant";

	
	public static final String FLR_NACHRICHTART_FLRTHEMA = "flrthema";
	public static final String FLR_NACHRICHTART_B_POPUP = "b_popup";
	public static final String FLR_NACHRICHTART_B_ARCHIVIEREN = "b_archivieren";
	
	public static final String FLR_NACHRICHTARCHIV_C_NACHRICHT = "c_nachricht";
	public static final String FLR_NACHRICHTARCHIV_T_ZEIT = "t_zeit";
	public static final String FLR_NACHRICHTARCHIV_T_ERLEDIGT = "t_erledigt";
	public static final String FLR_NACHRICHTARCHIV_T_BEARBEITUNG = "t_bearbeitung";
	public static final String FLR_NACHRICHTARCHIV_FLRPERSONAL_ANLEGEN = "flrpersonal_anlegen";
	public static final String FLR_NACHRICHTARCHIV_FLRPERSONAL_BEARBEITER = "flrpersonal_bearbeiter";
	public static final String FLR_NACHRICHTARCHIV_FLRPERSONAL_ERLEDIGT = "flrpersonal_erledigt";
	public static final String FLR_NACHRICHTARCHIV_FLRNACHRICHTART = "flrnachrichtart";
	
		
	public static final String FLR_THEMAROLLE_FLRTHEMA = "flrthema";
	public static final String FLR_THEMAROLLE_FLRSYSTEMROLLE = "flrsystemrolle";
	
	public static final String FLR_LAGERROLLE_FLRLAGER = "flrlager";
	public static final String FLR_LAGERROLLE_FLRSYSTEMROLLE = "flrsystemrolle";
	public static final String FLR_FERTIGUNGSGRUPPEROLLE_FLRFERTIGUNGSGRUPPE = "flrfertigungsgruppe";
	public static final String FLR_FERTIGUNGSGRUPPEROLLE_FLRSYSTEMROLLE = "flrsystemrolle";
	
	// Nachrichtenarten
	public static final Integer NA_RUESTZEIT_UEBERSCHRITTEN_ID = 1;
	public static final String NA_RUESTZEIT_UEBERSCHRITTEN = "NA_RUESTZEIT";
	public static final String NA_AUFTRAG_NICHT_MEHR_ERFUELLBAR = "AUFTRAG_NICHT_MEHR_ERFUELLBAR";
	
	// Nachrichten Properties
    static final public String NPROP_NACHRICHTARCHIV_I_ID = "NACHRICHTARCHIV_I_ID";
    static final public String NPROP_POPUP = "POPUP";

	
	// Feldlaengen
	public static final int MAX_BENUTZER_KENNUNG = 40;
	public static final int MAX_BENUTZER_KENNWORT = 40;

	public static final int MAX_BENUTZERMANDANTSYSTEMROLLE_C_UNTERSCHRIFTSTEXT = 40;
	public static final int MAX_BENUTZERMANDANTSYSTEMROLLE_C_UNTERSCHRIFTSFUNKTION = 15;

	static final int MIN_BENUTZER_KENNUNG = 4;
	static final int MIN_BENUTZER_KENNWORT = 4;

	public static final int MAX_SYSTEMROLLE_NAME = 80;

	public Integer createBenutzer(BenutzerDto benutzerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int getAnzahlDerMandantenEinesBenutzers(String benutzername,
			String kennwort) throws RemoteException;

	public void kopiereLagerRechteEinerRolle(Integer systemrolleIIdQuelle,
			Integer systemrolleIIdZiel, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public void removeBenutzer(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateBenutzer(BenutzerDto benutzerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BenutzerDto benutzerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public BenutzerDto benutzerFindByPrimaryKeyOhneExc(Integer iId);

	public BenutzerDto benutzerFindByCBenutzerkennung(String cBenutzerkennung,
			String cKennwort) throws EJBExceptionLP, RemoteException;
	
	public BenutzerDto benutzerFindByCBenutzerkennungOhneExc(String cBenutzerkennung);

	public BenutzerDto benutzerFindByCBenutzerkennungOhneEx(
			String cBenutzerkennung, String cKennwort) throws EJBExceptionLP,
			RemoteException;

	public Integer createSystemrolle(SystemrolleDto systemrolleDto)
			throws EJBExceptionLP, RemoteException;

	public void removeSystemrolle(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateSystemrolle(SystemrolleDto systemrolleDto)
			throws EJBExceptionLP, RemoteException;

	public SystemrolleDto systemrolleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public SystemrolleDto systemrolleFindByCBez(String cBez)
			throws EJBExceptionLP, RemoteException;

	public SystemrolleDto systemrolleFindByCBezOhneExc(String cBez)
			throws EJBExceptionLP, RemoteException;

	public Integer createBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBenutzermandantsystemrolle(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public void updateBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByBenutzerIId(
			Integer benutzerIId) throws EJBExceptionLP, RemoteException;

	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByBenutzerIIdOhneExc(
			Integer benutzerIId);

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
			Integer benutzerIId, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerIIdMandantCNrOhneExc(
			Integer benutzerIId, String mandantCNr);

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerCNrMandantCNr(
			String benutzerCNr, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByMandantCNrOhneExc(String mandantCNr);
	
	public void updateNachrichtart(NachrichtartDto nachrichtartDto);

	public Integer createNachrichtart(NachrichtartDto nachrichtartDto);

	public NachrichtartDto nachrichtartFindByPrimaryKey(Integer iId);

	public void removeNachrichtart(Integer iId);

	public void updateThema(ThemaDto themaDto);

	public ThemaDto themaFindByPrimaryKey(String cNr);
	
	public void updateThemarolle(ThemarolleDto themarolleDto);
	public ThemarolleDto themarolleFindByPrimaryKey(Integer iId);
	public Integer createThemarolle(ThemarolleDto themarolleDto);
	public void removeThemarolle(Integer iId);
	
	public void sendJmsMessageMitArchiveintrag(String nachrichtartCNr,String zusatz,TheClientDto theClientDto);
	
	public Integer createNachrichtarchiv(NachrichtarchivDto nachrichtarchivDto,TheClientDto theClientDto);
	public NachrichtarchivDto nachrichtarchivFindByPrimaryKey(Integer iId);
	public void updateNachrichtarchiv(NachrichtarchivDto nachrichtarchivDto);
	
	public Integer[] getBerechtigteLagerIIdsEinerSystemrolle(Integer systemrolleIId);
	
	public Integer weiseNachrichtPersonZu(Integer nachrichtarchivIId,
			TheClientDto theClientDto);
	public void erledigeNachricht(Integer nachrichtarchivIId, String cErledigungsgrund,
			TheClientDto theClientDto);
	
	
	public String[] getThemenDesAngemeldetenBenutzers(TheClientDto theClientDto);
	
	public Integer createLagerrolle(LagerrolleDto lagerrolleDto);
	public void updateLagerrolle(LagerrolleDto lagerrolleDto);
	public void removeLagerrolle(Integer iId);
	public LagerrolleDto lagerrolleFindByPrimaryKey(Integer iId);
	
	public int getAnzahlDerUnbearbeitetenMeldungen(TheClientDto theClientDto);
	public int getAnzahlDerNochNichtErledigtenAberNochZuBearbeitendenMeldungen(
			TheClientDto theClientDto);
	
	public JasperPrintLP printBenutzerstatistik(java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto);
	public void updateFertigungsgrupperolle(FertigungsgrupperolleDto fertigungsgrupperolleDto);
	public Integer createFertigungsgrupperolle(FertigungsgrupperolleDto dto);
	public FertigungsgrupperolleDto fertigungsgrupperolleFindByPrimaryKey(Integer iId);
	public void removeFertigungsgrupperolle(Integer iId);
	
	
}
