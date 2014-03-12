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
package com.lp.server.lieferschein.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LieferscheinpositionFac {
	// Tabellennamen
	public static final String LS_LIEFERSCHEINPOSITION = "LS_LIEFERSCHEINPOSITION";

	// Entitaeten
	public static final String LIEFERSCHEINPOSITION = "Lieferscheinposition";

	// Positionsart
	public static final String LIEFERSCHEINPOSITIONSART_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String LIEFERSCHEINPOSITIONSART_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String LIEFERSCHEINPOSITIONSART_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String LIEFERSCHEINPOSITIONSART_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String LIEFERSCHEINPOSITIONSART_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String LIEFERSCHEINPOSITIONSART_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;
	public static final String LIEFERSCHEINPOSITIONSART_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;
	public static final String LIEFERSCHEINPOSITIONSART_URSPRUNGSLAND = LocaleFac.POSITIONSART_URSPRUNGSLAND;
	public static final String LIEFERSCHEINPOSITIONSART_STUECKLISTENPOSITION = LocaleFac.POSITIONSART_STUECKLISTENPOSITION; // diese
	public static final String LIEFERSCHEINPOSITIONSART_ENDSUMME = LocaleFac.POSITIONSART_ENDSUMME; // endsumme
	public static final String LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME = LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME ;

	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String LIEFERSCHEINPOSITIONSART_AUFTRAGSDATEN = LocaleFac.POSITIONSART_AUFTRAGSDATEN; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String LIEFERSCHEINPOSITIONSART_SERIENNR = "SERIENNR"; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String LIEFERSCHEINPOSITIONSART_CHARGENR = "CHARGENR"; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_LIEFERSCHEINPOSITION_I_ID = "i_id";
	public static final String FLR_LIEFERSCHEINPOSITION_I_SORT = "i_sort";
	public static final String FLR_LIEFERSCHEINPOSITION_AUFTRAGPOSITION_I_ID = "auftragposition_i_id";
	public static final String FLR_LIEFERSCHEINPOSITION_LIEFERSCHEINPOSITIONART_C_NR = "positionsart_c_nr";
	public static final String FLR_LIEFERSCHEINPOSITION_N_MENGE = "n_menge";
	public static final String FLR_LIEFERSCHEINPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_LIEFERSCHEINPOSITION_C_BEZEICHNUNG = "c_bez";
	public static final String FLR_LIEFERSCHEINPOSITION_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_LIEFERSCHEINPOSITION_N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATT = "n_nettogesamtpreisplusversteckteraufschlagminusrabatt";
	public static final String FLR_LIEFERSCHEINPOSITION_X_TEXTINHALT = "x_textinhalt";
	public static final String FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG = "flrpositionensichtauftrag";
	public static final String FLR_LIEFERSCHEINPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN = "flrlieferschein";
	public static final String FLR_LIEFERSCHEINPOSITION_FLRMEDIASTANDARD = "flrmediastandard";
	public static final String FLR_LIEFERSCHEINPOSITION_FLRMWSTSATZ = "flrmwstsatz";

	public Integer createLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI, boolean bArtikelSetAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI, boolean bArtikelSetAufloesen, 
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException ;
	
	public LieferscheinpositionDto befuelleZusaetzlichePreisfelder(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLieferscheinpositionen(Object[] idsI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void storniereLieferscheinposition(Integer iIdLieferscheinpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int berechneAnzahlMengenbehaftetePositionen(
			Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public int berechneAnzahlArtikelpositionen(Integer iIdLieferscheinI)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinIId(
			Integer iIdLieferscheinI) throws EJBExceptionLP, RemoteException;
	
	public Collection<LieferscheinpositionDto> lieferscheinpositionFindByLieferscheinIId(Integer
			iIdLieferscheinI,TheClientDto theClientDto)
	throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinMenge(
			Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void setzeKupferzuschlag(Integer iIdLieferscheinpositionI,
			Double ddKupferzuschlagI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheLieferscheinpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void vertauscheLieferscheinpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ; 
	
	
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdLieferscheinI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKeyOhneExc(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto);

	public LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(
			Integer iIdAuftragpositionI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
	
	public LieferscheinpositionDto lieferscheinpositionFindPositionIIdISort(
			Integer positionIId,Integer iSort) throws EJBExceptionLP ,RemoteException;

	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;

	public boolean enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;


	public void preiseAusAuftragspositionenUebernehmen(Integer auftragIId,
			TheClientDto theClientDto);
	
	public void updateLieferscheinpositionSichtAuftrag(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinpositionOhneWeitereAktion(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void lieferscheinpositionKeinLieferrestEintragen(
			Integer lieferscheinpositionIId, boolean bKeinLieferrest, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean gehoertZuArtikelset(Integer lieferscheinpositionIId);
	
	public LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(
			Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public LieferscheinpositionDto befuelleZusaetzlichePositionfelder(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Die Positionsnummer im Lieferschein unabh&auml;ngig von Lieferauftr&auml;gen
	 * @param lsposIId die Lieferscheinposition f&uuml;r die die Nummer ermittelt werden soll.
	 * @return die Positionsnummer unabh&auml;ngig von irgendwelchen Auftragszuordnungen zwischen 1 ... n
	 */
	public Integer getLSPositionNummer(Integer lsposIId) ;

	public Integer getPositionNummer(Integer lsposIId) throws RemoteException;

	public Integer getPositionIIdFromPositionNummer(Integer lieferscheinIId, Integer position) ;

	public Integer getLSPositionIIdFromPositionNummer(Integer lieferscheinIId,
			Integer position) ;
	
	
	public void berechnePauschalposition(BigDecimal wert,Integer positionIId,Integer belegIId,TheClientDto theClientDto) 
		throws EJBExceptionLP, RemoteException;
	
	public void pruefeVKPreisAufLagerbewegung(TheClientDto theClientDto)
		throws EJBExceptionLP, RemoteException;
	
	public HashMap lieferscheinpositionFindByLieferscheinIIdAuftragIId(Integer lieferscheinIId,Integer iIdAuftragI,
			TheClientDto theClientDto);
	
	public Integer getLastPositionNummer(Integer reposIId) throws EJBExceptionLP, RemoteException ;

	public Integer getLSLastPositionNummer(Integer lsposIId) ;
	
	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer ermitteln
	 * 
	 * @param rechnungIId die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt 
	 *   werden soll.
	 *   
	 * @return 0 ... n 
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId) throws EJBExceptionLP ;

	public Integer getLSHighestPositionNumber(Integer rechnungIId) throws EJBExceptionLP ;
	
	public void bucheAbLager(LieferscheinpositionDto oDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP;
	
	/**
	 * Prueft, ob fuer alle Rechnungspositionen zwischen den beiden angegebenen Positionsnummern
	 * der gleiche Mehrwertsteuersatz definiert ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(
			Integer rechnungIId, Integer vonPositionNumber, Integer bisPositionNumber) throws EJBExceptionLP ;
	public void bucheZuLager(LieferscheinpositionDto oDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP;
	
	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(
			Integer lieferscheinposIId) throws EJBExceptionLP ;	
}
