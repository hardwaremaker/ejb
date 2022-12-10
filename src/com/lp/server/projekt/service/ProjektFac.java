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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ProjektFac {

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_PROJEKT_I_ID = "i_id";
	public static final String FLR_PROJEKT_C_NR = "c_nr";
	public static final String FLR_PROJEKT_KATEGORIE_C_NR = "kategorie_c_nr";
	public static final String FLR_PROJEKT_C_TITEL = "c_titel";
	public static final String FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER = "personal_i_id_zugewiesener";
	public static final String FLR_PROJEKT_PERSONAL_I_ID_ERZEUGER = "personal_i_id_erzeuger";
	public static final String FLR_PROJEKT_PERSONAL_I_ID_ERLEDIGER = "personal_i_id_erlediger";
	public static final String FLR_PROJEKT_TYP_C_NR = "typ_c_nr";
	public static final String FLR_PROJEKT_I_PRIO = "i_prio";
	public static final String FLR_PROJEKT_BEREICH_I_ID = "bereich_i_id";
	public static final String FLR_PROJEKT_T_ZIELDATUM = "t_zielwunschdatum";
	public static final String FLR_PROJEKT_T_ERLEDIGUNGSDATUM = "t_erledigungsdatum";
	public static final String FLR_PROJEKT_T_ANLEGEN = "t_anlegen";
	public static final String FLR_PROJEKT_T_REALISIERUNG = "t_realisierung";
	public static final String FLR_PROJEKT_T_AENDERN = "t_aendern";
	public static final String FLR_PROJEKT_D_DAUER = "d_dauer";
	public static final String FLR_PROJEKT_T_ZEIT = "t_zeit";
	public static final String FLR_PROJEKT_STATUS_C_NR = "status_c_nr";
	public static final String FLR_PROJEKT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_PROJEKT_KONTAKTART_C_NR = "kontaktart_c_nr";
	public static final String FLR_PROJEKT_X_FREETEXT = "x_freetext";
	public static final String FLR_PROJEKT_ANSPRECHPARTNER_I_ID = "ansprechpartner_i_id";
	public static final String FLR_PROJEKT_I_VERRECHENBAR = "i_verrechenbar";
	public static final String FLR_PROJEKT_T_INTERNERLEDIGT = "t_internerledigt";
	public static final String FLR_PROJEKT_FLRPERSONALERZEUGER = "flrpersonalErzeuger";
	public static final String FLR_PROJEKT_FLRPERSONALZUGEWIESENER = "flrpersonalZugewiesener";
	public static final String FLR_PROJEKT_FLRPARTNER = "flrpartner";
	public static final String FLR_PROJEKT_FLRPARTNERBETREIBER = "flrpartnerbetreiber";
	public static final String FLR_PROJEKT_FLRANSPRECHPARTNER = "flransprechpartner";
	public static final String FLR_PROJEKT_FLRPROJEKTTEXTSUCHE = "flrprojekttextsuche";
	public static final String FLR_PROJEKT_FLRPERSONALERLEDIGER = "flrpersonalErlediger";
	public static final String FLR_PROJEKT_I_SORT = "i_sort";
	public static final String FLR_PROJEKT_I_WAHRSCHEINLICHKEIT = "i_wahrscheinlichkeit";
	public static final String FLR_PROJEKT_N_UMSATZGEPLANT = "n_umsatzgeplant";
	public static final String FLR_PROJEKT_FLRPROJEKTSTATUS = "flrprojektstatus";


	public static final String FLR_HISTORY_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_HISTORY_PROJEKT_I_ID = "projekt_i_id";
	public static final String FLR_HISTORY_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_HISTORY_X_TEXT = "x_text";
	public static final String FLR_HISTORY_FLRPERSONAL = "flrpersonal";
	public static final String FLR_HISTORY_FLRPROJEKT = "flrprojekt";
	public static final String FLR_HISTORY_T_ANLEGEN = "t_anlegen";
	public static final String FLR_HISTORY_T_AENDERN = "t_aendern";


	public static int IDX_SPALTE_DAUER = 6;
	public static int IDX_SPALTE_KOSTEN = 7;
	public static final int IDX_KRIT_AUSWERTUNG = 0;
	public static final int IDX_KRIT_LOS = 1;

	public static final String KRIT_PROJEKTZEITEN_SORTIERUNG = "Ident";
	
	public static final String KRIT_PROJEKT_I_ID = "Projekt";
	public static final int ANZAHL_KRITERIEN_PROJEKTZEITEN = 2;

	public Integer createProjekt(ProjektDto projektDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeProjekt(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void removeProjekt(ProjektDto projektDto, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void updateProjekt(ProjektDto projektDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ProjektDto projektFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ProjektDto projektFindByPrimaryKeyOhneExc(Integer iId);

	public ProjektDto[] projektFindByCNrMandantCNr(String cNr,  String mandantCNr);

	public ProjektDto projektFindByMandantCNrCNrOhneExc(String cNrMandantI,
			String cNrI);

	public void toggleInternErledigt(Integer projektIId, TheClientDto theClientDto);

	public ProjektDto[] projektFindByPartnerIIdMandantCNr(Integer iPartnerId,
			String cNrMandant) throws EJBExceptionLP, RemoteException;

	public ProjektDto[] projektFindByPartnerIIdMandantCNrOhneExc(
			Integer iPartnerId, String cNrMandant) throws EJBExceptionLP,
			RemoteException;

	public ProjektDto[] projektFindByAnsprechpartnerIIdMandantCNr(
			Integer iAnsprechpartnerId, String cNrMandant)
			throws EJBExceptionLP, RemoteException;

	public ProjektDto[] projektFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iAnsprechpartnerId, String cNrMandant)
			throws RemoteException;

	public Integer createHistory(HistoryDto historyDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeHistory(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public  ArrayList<String> getVorgaengerProjekte(Integer projektIId);

	public void removeHistory(HistoryDto historyDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateHistory(HistoryDto historyDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String istPartnerBeiEinemMandantenGesperrt(Integer partnerIId, TheClientDto theClientDto);

	public HistoryDto historyFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public HistoryDto historyFindByPrimaryKeyOhneExc(Integer iId);

	public void vertauscheProjekte(Integer iIdPosition1I,
			Integer iIdPosition2I, int min) throws EJBExceptionLP,
			RemoteException;

	public void inQueueAufnehmen(Integer iIdPosition1I, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void ausQueueEntfernen(Integer iIdPosition1I, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	 public Double berechneGesamtSchaetzung(Integer personal_i_id_zugewiesener, TheClientDto theClientDto)
	  throws RemoteException;

	 public String getBelegnr(Integer projektNummer, Integer geschaeftsjahr,
			 TheClientDto theClientDto) throws RemoteException ;
	 public LinkedHashMap<String, ProjektVerlaufHelperDto> getProjektVerlauf(Integer projektIId,TheClientDto theClientDto);
	 public HistoryDto[] historyFindByProjektIid(Integer iId)
				throws EJBExceptionLP;
	 
	 public ProjektDto projektfindByBereichIIdArtikelIId(Integer bereichIId, Integer artikelIId);
	 
}
