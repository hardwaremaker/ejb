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
package com.lp.server.auftrag.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AuftragpositionFac {

	// Tabellennamen
	public static final String AUFT_AUFTRAGPOSITION = "AUFT_AUFTRAGPOSITION";

	// Entitaeten
	public static final String AUFTRAGPOSITION = "Auftragposition";

	// FLR Spaltennamen fuer FLRAuftragposition
	public static final String FLR_AUFTRAGPOSITION_I_SORT = "i_sort";
	public static final String FLR_AUFTRAGPOSITIONART_C_NR = "auftragpositionart_c_nr";
	public static final String FLR_AUFTRAGPOSITIONART_POSITION_I_ID = "position_i_id";
	public static final String FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR = "auftragpositionstatus_c_nr";
	public static final String FLR_AUFTRAGPOSITION_N_MENGE = "n_menge";
	public static final String FLR_AUFTRAGPOSITION_N_OFFENEMENGE = "n_offenemenge";
	public static final String FLR_AUFTRAGPOSITION_N_OFFENERAHMENMENGE = "n_offenerahmenmenge";
	public static final String FLR_AUFTRAGPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_AUFTRAGPOSITION_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_AUFTRAGPOSITION_X_TEXTINHALT = "x_textinhalt";
	public static final String FLR_AUFTRAGPOSITION_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_AUFTRAGPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_AUFTRAGPOSITION_FLRMEDIASTANDARD = "flrmediastandard";
	public static final String FLR_AUFTRAGPOSITION_POSITIONART_C_NR = "positionart_c_nr" ;

	public static final String FLR_AUFTRAGPOSITION_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN = "t_uebersteuerterliefertermin";
	public static final String FLR_AUFTRAGPOSITION_AUFTRAG_I_ID = "auftrag_i_id";
	public static final String FLR_AUFTRAGPOSITION_N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE = "n_nettogesamtpreisplusversteckteraufschlagminusrabatte";

	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_ID = "i_id";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_SORT = "i_sort";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID = "auftrag_i_id";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_N_OFFENE_MENGE = "n_offene_menge";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONART_C_NR = "auftragpositionart_c_nr";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONSTATUS_C_NR = "auftragpositionstatus_c_nr";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_T_UEBERSTEUERTERLIEFERTERMIN = "t_uebersteuerterliefertermin";
	public static final String FLR_AUFTRAGPOSITIONSICHTAUFTRAG_FLRARTIKEL = "flrartikel";

	public Integer createAuftragposition(
			AuftragpositionDto auftragpositionDtoI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createAuftragposition(
			AuftragpositionDto auftragpositionDtoI,
			boolean bArtikelSetAufloesen, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeAuftragposition(AuftragpositionDto auftragpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAuftragposition(AuftragpositionDto auftragpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAuftragpositionOhneWeitereAktion(
			AuftragpositionDto auftragpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto auftragpositionFindByPrimaryKey(
			Integer iIdAuftragpositionI) throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto auftragpositionFindByPrimaryKeyOhneExc(
			Integer iIdAuftragpositionI);

	public AuftragpositionDto[] auftragpositionFindByAuftrag(Integer iIdAuftragI)
			throws EJBExceptionLP, RemoteException;

	public Collection<AuftragpositionDto> auftragpositionFindByAuftragList(Integer iIdAuftragI)
			throws EJBExceptionLP ;

	public AuftragpositionDto[] auftragpositionFindByAuftragIIdNMengeNotNull(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto[] auftragpositionFindByAuftragOffeneMenge(
			Integer iIdAuftragI) throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto[] auftragpositionFindByAuftragPositiveMenge(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto auftragpositionFindByAuftragISort(
			Integer iIdAuftrag, Integer iSort) throws RemoteException;

	public AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionIIdRahmenpositionOhneExc(
			Integer iIdAuftragI, Integer iIdRahmenpositionI,
			TheClientDto theClientDto) throws RemoteException;

	public AuftragpositionDto[] auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
			Integer iIdRahmenpositionI, TheClientDto theClientDto)
			throws RemoteException;

	public void manuellErledigungAufgeben(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public int getAnzahlMengenbehafteteAuftragpositionen(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int berechneAnzahlArtikelpositionenMitStatus(Integer iIdAuftragI,
			String sStatusI) throws EJBExceptionLP, RemoteException;

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId,
			Integer belegIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheAuftragpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP;

	public void vertauscheAuftragpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAuftragI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI)
			throws EJBExceptionLP, RemoteException;

	public void updateOffeneMengeAuftragposition(Integer iIdAuftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Double berechneArbeitszeitSoll(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createAuftragseriennrn(
			AuftragseriennrnDto auftragseriennrnDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeAuftragseriennrn(Integer iId, TheClientDto theClientDto)
			throws RemoteException;

	public void removeAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto,
			TheClientDto theClientDto) throws RemoteException;

	public void updateAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto,
			TheClientDto theClientDto) throws RemoteException;

	public AuftragseriennrnDto auftragseriennrnFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, RemoteException;

	public AuftragseriennrnDto auftragseriennrnFindByAuftragpsotionIId(
			Integer iId, TheClientDto theClientDto) throws RemoteException,
			RemoteException;

	public void loescheAuftragseriennrnEinesAuftragposition(
			Integer iIdAuftragposition, TheClientDto theClientDto)
			throws RemoteException;

	public String getNextSeriennr(Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getSeriennummmern(Integer iIdAuftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void pruefeAuftragseriennumern(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer getMinISort(Integer iIdAuftragI) throws EJBExceptionLP,
			RemoteException;

	public Integer getPositionNummer(Integer auftragpositionIId)
			throws RemoteException;

	public Object[][] isAuftragseriennrnVorhanden(String[] cSerienNr,
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Ist das komplette Artikelset in ausreichender Menge verf&uuml;gbar?
	 * 
	 * @param positions
	 *            enth&auml;lt alle jene Auftragspositionen die f&uuml;r das ArtikelSet
	 *            relevant sind
	 * @return true wenn alle im Artikelset befindlichen Artikel in
	 *         ausreichender Menge vorhanden sind, ansonsten false
	 */
	public boolean isArtikelSetLagernd(AuftragpositionDto[] positions,
			Integer lagerIId, TheClientDto theClientDto) throws RemoteException;

	/**
	 * Ermittelt die erf&uuml;llbare Menge/Anzahl eines Artikelsets. Es wird davon
	 * ausgegangen, dass die erste Position (positions[0]) den Kopfartikel
	 * enth&auml;lt und somit auch die Sollmenge (Satzgr&ouml;&szlig;e). Es wird die noch offene
	 * Menge ber&uuml;cksichtigt.
	 * 
	 * @param positions
	 *            enth&auml;lt alle jene Auftragspositionen die f&uuml;r das ArtikelSet
	 *            relevant sind
	 * @param lagerIId
	 *            die Lager-IId von der die Ware entnommen werden soll.
	 * @param theClientDto
	 * @return die erfuellbare Menge fuer dieses Artikelset
	 * 
	 * @throws RemoteException
	 */
	public BigDecimal getErfuellbareMengeArtikelset(
			AuftragpositionDto[] positions, Integer lagerIId,
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Alle Artikelsets mit noch offenen Mengen liefern
	 * 
	 * @param positions
	 *            sind alle Auftragspositionen
	 * @param theClientDto
	 * @return eine Liste jene Artikelsets f&uuml;r die es noch offene Mengen im
	 *         Kopfartikel gibt. Es wird das komplette Artikelset des Auftrags
	 *         geliefert.
	 */
	public List<Artikelset> getOffeneAuftragpositionDtoMitArtikelset(
			AuftragpositionDto[] positions, TheClientDto theClientDto);

	public Integer getLastPositionNummer(Integer reposIId)
			throws EJBExceptionLP, RemoteException;

	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position);

	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer
	 * ermitteln
	 * 
	 * @param rechnungIId
	 *            die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt
	 *            werden soll.
	 * 
	 * @return 0 ... n
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId)
			throws EJBExceptionLP;

	/**
	 * Prueft, ob fuer alle Rechnungspositionen zwischen den beiden angegebenen
	 * Positionsnummern der gleiche Mehrwertsteuersatz definiert ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP;

}
