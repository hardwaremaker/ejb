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
package com.lp.server.auftrag.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AuftragServiceFac {
	// Tabellennamen
	public static final String AUFT_AUFTRAGTEXT = "AUFT_AUFTRAGTEXT";

	public static final String AUFTRAGTEXT_KOPFTEXT = MediaFac.MEDIAART_KOPFTEXT;
	public static final String AUFTRAGTEXT_FUSSTEXT = MediaFac.MEDIAART_FUSSTEXT;

	// Auftragart
	public static final String AUFTRAGART_FREI = "Freier Auftrag ";
	public static final String AUFTRAGART_RAHMEN = "Rahmenauftrag  ";
	public static final String AUFTRAGART_ABRUF = "Abrufauftrag   ";
	public static final String AUFTRAGART_WIEDERHOLEND = "Wiederholend   ";

	public static final String AUFTRAGART_RAHMEN_SHORT = "R";
	public static final String AUFTRAGART_ABRUF_SHORT = "A";
	public static final String AUFTRAGART_WIEDERHOLEND_SHORT = "W";

	// Auftragstatus
	public static final String AUFTRAGSTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String AUFTRAGSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String AUFTRAGSTATUS_TEILERLEDIGT = LocaleFac.STATUS_TEILERLEDIGT;
	public static final String AUFTRAGSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String AUFTRAGSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;

	// Auftragpositionart
	public static final String AUFTRAGPOSITIONART_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String AUFTRAGPOSITIONART_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String AUFTRAGPOSITIONART_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String AUFTRAGPOSITIONART_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String AUFTRAGPOSITIONART_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String AUFTRAGPOSITIONART_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;
	public static final String AUFTRAGPOSITIONART_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;
	public static final String AUFTRAGPOSITIONART_STUECKLISTENPOSITION = LocaleFac.POSITIONSART_STUECKLISTENPOSITION;
	public static final String AUFTRAGPOSITIONART_POSITION = LocaleFac.POSITIONSART_POSITION;
	public static final String AUFTRAGPOSITIONART_ENDSUMME = LocaleFac.POSITIONSART_ENDSUMME;
	public static final String AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME = LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME;

	// Auftragpositionstatus
	public static final String AUFTRAGPOSITIONSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String AUFTRAGPOSITIONSTATUS_TEILERLEDIGT = LocaleFac.STATUS_TEILERLEDIGT;
	public static final String AUFTRAGPOSITIONSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String AUFTRAGPOSITIONSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;

	// Auftragwiederholungsintervall
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH = "w\u00F6chentlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH = "14t\u00E4gig";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH = "monatlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL = "quartalsweise";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_HALBJAHR = "halbj\u00E4hrlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR = "j\u00E4hrlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR = "2j\u00E4hrlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR = "3j\u00E4hrlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR = "4j\u00E4hrlich";
	public static final String AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR = "5j\u00E4hrlich";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_AUFTRAGTEXT_I_ID = "i_id";
	public static final String FLR_AUFTRAGTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_AUFTRAGTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_AUFTRAGTEXT_MEDIAART_C_NR = "mediaart_c_nr";
	public static final String FLR_AUFTRAGTEXT_X_TEXTINHALT = "x_textinhalt";

	public static final String FLR_AUFTRAGAUFTRAGDOKUMENT_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_AUFTRAGAUFTRAGDOKUMENT_FLRAUFTRAGDOKUMENT = "flrauftragdokument";

	public static final String FLR_AUFTRAGPOSITIONART_POSITIONSART_C_NR = "positionsart_c_nr";
	public static final String FLR_AUFTRAGPOSITIONART_AUFTRAGPOSITIONSART_POSITIONSART_SET = "auftragpositionsart_positionsart_set";

	public static final String FLR_ZEITPLAN_T_TERMIN = "t_termin";
	public static final String FLR_ZEITPLAN_N_MATERIAL = "n_material";
	public static final String FLR_ZEITPLAN_N_DAUER = "n_dauer";
	public static final String FLR_ZEITPLAN_C_KOMMENTAR = "c_kommentar";
	public static final String FLR_ZEITPLAN_T_DAUER_ERLEDIGT = "t_dauer_erledigt";
	public static final String FLR_ZEITPLAN_T_MATERIAL_ERLEDIGT = "t_material_erledigt";

	public static final String FLR_ZAHLUNGSPLAN_T_TERMIN = "t_termin";
	public static final String FLR_ZAHLUNGSPLAN_N_BETRAG = "n_betrag";

	public static final String FLR_ZAHLUNGSPLAN_C_KOMMENTAR = "c_kommentar";
	public static final String FLR_ZAHLUNGSPLAN_T_ERLEDIGT = "t_erledigt";
	public static final String FLR_ZAHLUNGSPLAN_FLRMEILENSTEIN = "flrmeilenstein";

	// Fix verdrahtet Auftragtexte
	public static final String AUFTRAG_DEFAULT_KOPFTEXT = "Wir danken f\u00FCr Ihre Bestellung und best\u00E4tigen diese wie folgt:";
	public static final String AUFTRAG_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihren Auftrag,";

	public String createAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AuftragartDto auftragartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AuftragartDto[] auftragartFindAll() throws RemoteException,
			EJBExceptionLP;

	public Map<?, ?> getAuftragart(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public HashMap<String, String> getAuftragStatus(Locale locale,
			String cNrUser) throws EJBExceptionLP, RemoteException;

	public void createAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws RemoteException, EJBExceptionLP;

	public AuftragartsprDto auftragartsprFindByPrimaryKey(String spracheCNr,
			String auftragartCNr) throws RemoteException, EJBExceptionLP;

	public AuftragartsprDto auftragartsprFindBySpracheAndCNr(String pSprache,
			String pNummer) throws RemoteException, EJBExceptionLP;

	public String createAuftragpositionArt(
			AuftragpositionArtDto auftragpositionArtDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeAuftragpositionArt(String cNrAuftragpositionartI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateAuftragpositionArt(
			AuftragpositionArtDto auftragpositionartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AuftragpositionArtDto auftragpositionartFindByPrimaryKey(
			String cNrAuftragpositionartI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public Map<String, String> auftragpositionartFindAll(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public Map<?, ?> getAuftragpositionart(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public void createAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragpositionstatus(String statusCNr)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws RemoteException, EJBExceptionLP;

	public String getAuftragpositionstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public AuftragpositionstatusDto auftragpositionstatusFindByPrimaryKey(
			String statusCNr) throws RemoteException, EJBExceptionLP;

	public void createAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragStatus(String statusCNr) throws RemoteException,
			EJBExceptionLP;

	public void removeAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws RemoteException, EJBExceptionLP;

	public AuftragStatusDto auftragStatusFindByPrimaryKey(String statusCNr)
			throws RemoteException, EJBExceptionLP;

	public AuftragStatusDto[] auftragStatusFindAll() throws RemoteException,
			EJBExceptionLP;

	public String getAuftragstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAuftragwiederholungsintervall(Locale locale1)
			throws EJBExceptionLP, RemoteException;

	public Integer createAuftragtext(AuftragtextDto auftragtextDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAuftragtext(AuftragtextDto auftragtextDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragtext(AuftragtextDto auftragtextDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragtextDto auftragtextFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public AuftragtextDto auftragtextFindByMandantLocaleCNr(String sMandantI,
			String sLocaleCNrI, String sCNrI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public AuftragtextDto createDefaultAuftragtext(String cNrMediaartI,
			String sLocaleCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public void createAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AuftragwiederholungsintervallDto auftragwiederholungsintervallFindByPrimaryKey(
			String cNr, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public void createAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAuftragwiederholungsintervallspr(String localeCNr,
			String auftragwiederholungsintervallCNr) throws RemoteException,
			EJBExceptionLP;

	public void removeAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAuftragwiederholungsintervallsprs(
			AuftragwiederholungsintervallsprDto[] auftragwiederholungsintervallsprDtos)
			throws RemoteException, EJBExceptionLP;

	public AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprFindByPrimaryKey(
			String localeCNr, String auftragwiederholungsintervallCNr)
			throws RemoteException, EJBExceptionLP;

	public AuftragauftragdokumentDto[] auftragauftragdokumentFindByAuftragIId(
			Integer auftragIId);

	/**
	 * 
	 * @deprecated MB->VF ??? das ist doch dasselbe wie findByPrimaryKey. bitte
	 *             entfernen.
	 * @param pSprache
	 *            String
	 * @param pCNr
	 *            String
	 * @return AuftragwiederholungsintervallsprDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprFindBySpracheAndCNr(
			String pSprache, String pCNr) throws RemoteException,
			EJBExceptionLP;

	public void updateAuftragdokument(AuftragdokumentDto auftragdokumentDto);

	public Integer createAuftragdokument(AuftragdokumentDto auftragdokumentDto);

	public AuftragdokumentDto auftragdokumentFindByPrimaryKey(Integer iId);

	public void removeAuftragdokument(AuftragdokumentDto auftragdokumentDto);

	public void updateAuftragdokumente(Integer auftragIId,
			ArrayList<AuftragdokumentDto> dtos);

	public AuftragdokumentDto[] auftragdokumentFindByBVersteckt();

	public Integer createAuftragbegruendung(
			AuftragbegruendungDto auftragbegruendungDto);

	public void updateAuftragbegruendung(AuftragbegruendungDto begruendungDto);

	public void removeAuftragbegruendung(Integer iId);

	public AuftragbegruendungDto auftragbegruendungFindByPrimaryKey(Integer iId);

	public void updateMeilenstein(MeilensteinDto meilensteinDto,
			TheClientDto theClientDto);

	public Integer createMeilenstein(MeilensteinDto meilensteinDto,
			TheClientDto theClientDto);

	public MeilensteinDto meilensteinFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removeMeilenstein(MeilensteinDto meilensteinDto);

	public ZeitplanDto zeitplanFindByPrimaryKey(Integer iId);

	public void updateZeitplan(ZeitplanDto dto);

	public Integer createZeitplan(ZeitplanDto zeitplanDto);

	public void removeZeitplan(ZeitplanDto zeitplanDto);

	public Integer createZahlungsplan(ZahlungsplanDto zahlungsplanDto,
			TheClientDto theClientDto);

	public ZahlungsplanDto zahlungsplanFindByPrimaryKey(Integer iId);

	public void removeZahlungsplan(ZahlungsplanDto zahlungsplanDto);

	public void updateZahlungsplan(ZahlungsplanDto dto,
			TheClientDto theClientDto);

	public Integer createZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto,
			TheClientDto theClientDto);

	public void removeZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto);

	public ZahlungsplanmeilensteinDto zahlungsplanmeilensteinFindByPrimaryKey(
			Integer iId);

	public void updateZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto,
			TheClientDto theClientDto);

	public void toggleZahlungplanmeilensteinErledigt(
			Integer zahlungsplanmeilensteinIId, TheClientDto theClientDto);

	public void vertauscheMeilenstein(Integer iIdPosition1I,
			Integer iIdPosition2I);

	public void vertauscheZahlungsplanmeilenstein(Integer iIdPosition1I,
			Integer iIdPosition2I);

	public ZahlungsplanmeilensteinDto[] zahlungsplanmeilensteinFindByZahlungplanIIdOrderByTErledigt(
			Integer zahlungsplanIId);

	public void toggleZeitplanDauerErledigt(Integer zeitplanIId,
			TheClientDto theClientDto);

	public void toggleZeitplanMaterialErledigt(Integer zeitplanIId,
			TheClientDto theClientDto);

	public Integer createZeitplantyp(ZeitplantypDto dto,
			TheClientDto theClientDto);

	public void removeZeitplantyp(ZeitplantypDto zeitplantypDto);

	public void removeZeitplantypdetail(
			ZeitplantypdetailDto zeitplantypdetailDto);

	public Integer createZeitplantypdetail(
			ZeitplantypdetailDto zeitplantypdetailDto, TheClientDto theClientDto);

	public ZeitplantypdetailDto zeitplantypdetailFindByPrimaryKey(Integer iId);

	public ZeitplantypDto zeitplantypFindByPrimaryKey(Integer iId);

	public void updateZeitplantypdetail(ZeitplantypdetailDto dto);

	public void updateZeitplantyp(ZeitplantypDto dto, TheClientDto theClientDto);

	public void vertauscheZeitplantypdetail(Integer iIdPosition1I,
			Integer iIdPosition2I);
	
	public void importierteZeitplanAusZeitplantyp(Integer zeitplantypIId, Integer auftragIId,
			TheClientDto theClientDto);
	
	public Integer createVerrechenbar(VerrechenbarDto dto,
			TheClientDto theClientDto);
	public void removeVerrechenbar(VerrechenbarDto dto);
	public VerrechenbarDto verrechenbarFindByPrimaryKey(Integer iId);
	public void vertauscheVerrechenbar(Integer iIdPosition1I,
			Integer iIdPosition2I);
	public void updateVerrechenbar(VerrechenbarDto dto,
			TheClientDto theClientDto);
	public Map getAllVerrechenbar();
	
	public AuftragkostenstelleDto auftragkostenstelleFindByPrimaryKey(
			Integer iId);
	public void updateAuftragkostenstelle(AuftragkostenstelleDto dto);
	public void removeAuftragkostenstelle(
			AuftragkostenstelleDto auftragkostenstelleDto);
	public Integer createAuftragkostenstelle(AuftragkostenstelleDto dto,
			TheClientDto theClientDto);
	public AuftragkostenstelleDto[] auftragkostenstellefindByAuftrag(
			Integer auftragIId);

}
