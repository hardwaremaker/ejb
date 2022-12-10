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
package com.lp.server.projekt.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ProjektServiceFac {

	public static final String PROJEKTKATEGORIE_PROJEKT = "Projekt        ";

	public static final String PROJEKTTYP_TODO = "ToDo";

	public static final String PROJEKTKONTAKTART_EMAIL = "Email";

	// Projekt Status
	public static final String PROJEKT_STATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String PROJEKT_STATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String PROJEKT_STATUS_GETESTET = "Getestet       ";
	public static final String PROJEKT_STATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String PROJEKT_STATUS_FERTIG = LocaleFac.STATUS_FERTIG;
	public static final String PROJEKT_STATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	
	public static final int PROJEKT_VERRECHENBAR_NICHT_DEFINIERT=0;
	public static final int PROJEKT_VERRECHENBAR_VERRECHENBAR=1;
	public static final int PROJEKT_VERRECHENBAR_NICHT_VERRECHENBAR=2;
	

	public boolean sindErledigugnsgruendeVorhanden(TheClientDto theclientDto);

	public String createKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKategorie(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KategorieDto kategorieFindByPrimaryKey(String cNr,
			String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getKategorie(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getTyp(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getProjektStatus(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKategoriespr(String projektkategorieCNr, String localeCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKategoriesprs(KategoriesprDto[] kategoriesprDtos)
			throws EJBExceptionLP, RemoteException;

	public KategoriesprDto kategoriesprFindByPrimaryKey(
			String projektkategorieCNr, String localeCNr)
			throws EJBExceptionLP, RemoteException;

	public String createProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createBereich(BereichDto bereichDto,
			TheClientDto theClientDto);

	public void updateBereich(BereichDto bereichDto, TheClientDto theClientDto);

	public void removeBereich(BereichDto dto);

	public BereichDto bereichFindByPrimaryKey(Integer iId);

	public void removeProjekttyp(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ProjekttypDto projekttypFindByPrimaryKey(String cNr,
			String mandantCNr, TheClientDto theClientDto)
			throws RemoteException;

	public void createProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP, RemoteException;

	public void removeProjekttypspr(String typCNr, String localeCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP, RemoteException;

	public ProjekttypsprDto projekttypsprFindByPrimaryKey(String typCNr,
			String localeCNr, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public ProjekttypsprDto projekttypsprFindByPrimaryKeyohneEx(String typCNr,
			String localeCNr, String mandantCNr) throws RemoteException;

	public void updateHistoryart(HistoryartDto historyartDto);

	public Integer createHistoryart(HistoryartDto historyartDto);

	public HistoryartDto historyartFindByPrimaryKey(Integer iId);

	public void removeHistoryart(HistoryartDto dto);

	public String createProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeProjektStatus(String statusCNr, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Map getAllBereich(TheClientDto theClientDto);

	public void removeProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ProjektStatusDto projektStatusFindByPrimaryKey(String statusCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ProjektStatusDto[] projektStatusFindAll() throws RemoteException;

	public Integer createProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto,
			TheClientDto theClientDto);

	public void removeProjekterledigungsgrund(Integer iId);

	public void updateProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto);

	public ProjekterledigungsgrundDto projekterledigungsgrundFindByPrimaryKey(
			Integer iId);

	public void vertauscheBereich(Integer iId1I, Integer iId2I);

	public ProjekttechnikerDto projekttechnikerFindByPrimaryKey(Integer iId);

	public void removeProjekttechniker(ProjekttechnikerDto projekttechnikerDto);

	public void updateProjekttechniker(ProjekttechnikerDto projekttechnikerDto,
			TheClientDto theClientDto);

	public Integer createProjekttechniker(
			ProjekttechnikerDto projekttechnikerDto, TheClientDto theClientDto);

	public Integer createProjekttaetigkeit(
			ProjekttaetigkeitDto projekttaetigkeitDto, TheClientDto theClientDto);

	public void removeProjekttaetigkeit(ProjekttaetigkeitDto dto);

	public void updateProjekttaetigkeit(ProjekttaetigkeitDto dto,
			TheClientDto theClientDto);

	public ProjekttaetigkeitDto projekttaetigkeitFindByPrimaryKey(Integer iId);
	public ProjekttaetigkeitDto[] projekttaetigkeitFindByProjektIId(Integer projektIId);

	public static final String FLR_PROJEKTTECHNIKER_FLRPROJEKT = "flrprojekt";
	public static final String FLR_PROJEKTTECHNIKER_FLRPERSONAL = "flrpersonal";

	public static final String FLR_PROJEKTTAETIGKEIT_FLRPROJEKT = "flrprojekt";
	public static final String FLR_PROJEKTTAETIGKEIT_FLRARTIKEL = "flrartikel";
	
	public boolean istTaetigkeitBeiProjekthinterlegt(Integer projektIId,
			Integer artikelIId);
	public boolean istMeinProjekt(Integer projektIId, Integer personalIId);
	
	public ProjektgruppeDto projektgruppeFindByPrimaryKey(Integer iId);
	public void removeProjektgruppe(ProjektgruppeDto projektgruppeDto);
	public void updateProjektgruppe(ProjektgruppeDto projektgruppeDto,
			TheClientDto theClientDto);
	public Integer createProjektgruppe(ProjektgruppeDto projektgruppeDto,
			TheClientDto theClientDto);
	
	public Integer createVkfortschritt(VkfortschrittDto dto,
			TheClientDto theClientDto);
	public void updateVkfortschritt(VkfortschrittDto dto,
			TheClientDto theClientDto);
	public void removeVkfortschritt(VkfortschrittDto dto,
			TheClientDto theClientDto);
	public VkfortschrittDto vkfortschrittFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);
	
	public void vertauscheVkfortschritt(Integer iId1I, Integer iId2I);
	public Map getAllSprVkfortschritt(TheClientDto theClientDto);
	public Map getAllLeadstatus(TheClientDto theClientDto);
	
	public boolean esGibtMindestensEinenBereichMitBetreiber(TheClientDto theClientDto);
	
	public boolean esGibtMindestensEinenBereichMitArtikel(TheClientDto theClientDto);
	
	public int getAnzahlTechniker(Integer projektIId);
	public HistoryartDto getHistoryartInAuswahllisteAnzeigen( TheClientDto theClientDto);
	public String getTextVerrechenbar(Integer iVerrechenbar,TheClientDto theClientDto);
	public Map getAllVerrechenbar(TheClientDto theClientDto);
	
}
