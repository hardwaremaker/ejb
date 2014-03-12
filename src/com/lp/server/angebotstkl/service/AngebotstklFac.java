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
package com.lp.server.angebotstkl.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotstklFac {

	public static final String POSITIONSART_AGSTKL_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String POSITIONSART_AGSTKL_IDENT = LocaleFac.POSITIONSART_IDENT;

	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG = 1;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION = 2;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_SORT = 3;

	public final static String REPORT_MODUL = "angebotstkl";

	public final static String REPORT_ANGEBOTSTUECKLISTE = "as_angebotstkl.jasper";
	public final static String REPORT_EINKAUFSANGEBOT = "as_einkaufsangebot.jasper";

	public static final String FLR_AGSTKL_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_AGSTKL_WAEHRUNG_C_NR = "waehrung_c_nr";
	public static final String FLR_AGSTKL_FLRKUNDE = "flrkunde";
	public static final String FLR_AGSTKL_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_AGSTKLPOSITION_AGSTKL_I_ID = "agstkl_i_id";
	public static final String FLR_AGSTKLPOSITION_AGSTKLPOSITIONSART_C_NR = "agstklpositionsart_c_nr";
	public static final String FLR_AGSTKLPOSITION_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_AGSTKLPOSITION_B_ARTIKELBEZEICHNUNGUEBERSTEUERT = "b_artikelbezeichnunguebersteuert";
	public static final String FLR_AGSTKLPOSITION_N_MENGE = "n_menge";
	public static final String FLR_AGSTKLPOSITION_EINHEIT_C_NR = "einheit_c_nr";

	public static final String FLR_EINKAUFSANGEBOT_FLRKUNDE = "flrkunde";
	public static final String FLR_EINKAUFSANGEBOT_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_EINKAUFSANGEBOT_C_PROJEKT = "c_projekt";

	public static final String FLR_EINKAUFSANGEBOTPOSITION_EINKAUFSANGEBOT_I_ID = "einkaufsangebot_i_id";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_AGSTKLPOSITIONSART_C_NR = "agstklpositionsart_c_nr";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_MENGE = "n_menge";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS1 = "n_preis1";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS2 = "n_preis2";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS3 = "n_preis3";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS4 = "n_preis4";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS5 = "n_preis5";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_B_DRUCKEN = "b_drucken";

	public static final int MAX_AGSTKL_C_NR = 15;
	public static final int MAX_AGSTKL_C_BEZ = 40;

	public static final int MAX_AGSTKLPOSITION_C_BEZ = 40;

	public Map<String, String> getAllAgstklpositionsart()
			throws EJBExceptionLP, RemoteException;

	/**
	 * TODO: MB->CK bitte in reportFac verschieben.
	 * @param iIdAngebotstkl
	 *            Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @return JasperPrintLP
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstkl,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId,
			int iSortierung, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createAgstkl(AgstklDto agstklDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAgstkl(AgstklDto agstklDto) throws EJBExceptionLP,
			RemoteException;

	public void updateAgstkl(AgstklDto agstklDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public AgstklDto agstklFindByPrimaryKeyOhneExc(Integer iId);

	public AgstklDto[] agstklFindByKundeIIdMandantCNr(Integer iIdKunde,
			String cNrMandant) throws EJBExceptionLP, RemoteException;

	public AgstklDto[] agstklFindByKundeIIdMandantCNrOhneExc(Integer iIdKunde,
			String cNrMandant) throws RemoteException;

	public Integer createAufschlag(AufschlagDto aufschlagDto,TheClientDto theClientDto);
	public void removeAufschlag(Integer aufschlagIId);
	public void updateAufschlag(AufschlagDto aufschlagDto,
			TheClientDto theClientDto);
	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId);
	
	public void createAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP,
			RemoteException;

	public void removeAgstklpositionsart(String positionsartCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP,
			RemoteException;

	public void updateAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createEinkaufsangebotpositions(
			EinkaufsangebotpositionDto[] einkaufsangebotpositionDtos,
			TheClientDto theClientDto) throws RemoteException;

	public AgstklpositionsartDto agstklpositionsartFindByPrimaryKey(
			String positionsartCNr) throws EJBExceptionLP, RemoteException;

	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(
			Integer agstklIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI,
			String cNrWaehrungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto,
			TheClientDto theClientDto) throws RemoteException;

	public void removeEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws RemoteException;

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto,
			TheClientDto theClientDto) throws RemoteException;

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public Integer createEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto,
			TheClientDto theClientDto) throws RemoteException;

	public void  kopierePositionenAusStueckliste(Integer stuecklisteIId, Integer agstklIId, TheClientDto theClientDto);
	
	public void removeEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws RemoteException;

	public void updateEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws RemoteException;

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(
			Integer iId) throws RemoteException;

	public Integer einkaufsangebotpositionGetMaxISort(Integer einkaufsangebotIId)
			throws RemoteException;

	public void vertauscheEinkausangebotpositionen(Integer idPosition1I,
			Integer idPosition2I) throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public void createAgstkl(AgstklDto agstklDto) throws RemoteException;

	public void removeAgstkl(Integer iId) throws RemoteException;

	public void updateAgstkl(AgstklDto agstklDto) throws RemoteException;

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId, Short bMaterial,
			TheClientDto theClientDto);
	
	public void updateAgstkls(AgstklDto[] agstklDtos) throws RemoteException;

	public AgstklDto agstklFindByCNrMandantCNr(String cNr, String mandantCNr)
			throws RemoteException;
	
	public AgstklDto agstklFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr);

	public AgstklDto[] agstklFindByAnsprechpartnerIIdKunde(
			Integer iAnsprechpartnerIId) throws RemoteException;

	public void createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws RemoteException;

	public void removeEinkaufsangebot(Integer iId) throws RemoteException;

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws RemoteException;

	public void updateEinkaufsangebots(EinkaufsangebotDto[] einkaufsangebotDtos)
			throws RemoteException;

	public EinkaufsangebotDto[] einkaufsangebotFindByAnsprechpartnerIId(
			Integer iAnsprechpartnerIId) throws RemoteException;
	
	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(
			Integer iIdAgstklI, TheClientDto theClientDto);
	public void updateAgstklaufschlag(Integer agstklIId,
			AufschlagDto[] aufschlagDtos, TheClientDto theClientDto);
	
	
	public AgstklpositionDto befuellePositionMitPreisenKalkulationsart2(
			TheClientDto theClientDto, String waehrungCNr, Integer artikelIId,
			BigDecimal nMenge, AgstklpositionDto agstklpositionDtoI);
}
