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
package com.lp.server.angebot.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.IPositionNumber;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotpositionFac extends IPositionNumber {
	public static final String FLR_ANGEBOTPOSITION_ANGEBOT_I_ID = "angebot_i_id";
	public static final String FLR_ANGEBOTPOSITION_I_SORT = "i_sort";
	public static final String FLR_ANGEBOTPOSITION_POSITIONART_C_NR = "positionart_c_nr";
	public static final String FLR_ANGEBOTPOSITION_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_ANGEBOTPOSITION_C_BEZ = "c_bez";
	public static final String FLR_ANGEBOTPOSITION_C_ZBEZ = "c_zbez";
	public static final String FLR_ANGEBOTPOSITION_N_GESTEHUNGSPREIS = "n_gestehungspreis";
	public static final String FLR_ANGEBOTPOSITION_X_TEXTINHALT = "x_textinhalt";
	public static final String FLR_ANGEBOTPOSITION_MEDIASTANDARD_I_ID = "mediastandard_i_id";
	public static final String FLR_ANGEBOTPOSITION_N_MENGE = "n_menge";
	public static final String FLR_ANGEBOTPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_ANGEBOTPOSITION_F_RABATTSATZ = "f_rabattsatz";
	public static final String FLR_ANGEBOTPOSITION_F_ZUSATZRABATTSATZ = "f_zusatzrabattsatz";
	public static final String FLR_ANGEBOTPOSITION_MWSTSATZ_I_ID = "mwstsatz_i_id";
	public static final String FLR_ANGEBOTPOSITION_N_NETTOEINZELPREISPLUSVERSTECKTERAUFSCHLAG = "n_nettoeinzelpreisplusversteckteraufschlag";
	public static final String FLR_ANGEBOTPOSITION_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_ANGEBOTPOSITION_N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAG = "n_nettogesamtpreisplusversteckteraufschlag";
	public static final String FLR_ANGEBOTPOSITION_N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE = "n_nettogesamtpreisplusversteckteraufschlagminusrabatte";
	public static final String FLR_ANGEBOTPOSITION_N_MWSTBETRAG = "n_mwstbetrag";
	public static final String FLR_ANGEBOTPOSITION_AGSTKL_I_ID = "agstkl_i_id";
	public static final String FLR_ANGEBOTPOSITION_B_ALTERNATIVE = "b_alternative";
	public static final String FLR_ANGEBOTPOSITION_FLRANGEBOT = "flrangebot";
	public static final String FLR_ANGEBOTPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_ANGEBOTPOSITION_FLRMEDIASTANDARD = "flrmediastandard";

	public static final String ANGEBOTPOSITION_ALTERNATIVE_SHORT = "A";
	public static final String ANGEBOTPOSITION_ALTERNATIVE_TEXT = "Option";

	public Integer createAngebotposition(
			AngebotpositionDto angebotpositionDtoI, TheClientDto theClientDto);
	public Integer createAngebotposition(AngebotpositionDto angebotpositionDtoI,boolean bArtikelSetAufloesen,
			TheClientDto theClientDto);
	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAngebotposition(AngebotpositionDto angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAngebotposition(AngebotpositionDto angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void vertauscheAngebotpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP;

	public void vertauscheAngebotpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP; 
	
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAngebotI, int iSortierungNeuePositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int getAnzahlMengenbehafteteAngebotpositionen(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AngebotpositionDto angebotpositionFindByPrimaryKey(
			Integer iIdAngebotpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public AngebotpositionDto angebotpositionFindByPrimaryKeyOhneExc(
			Integer iIdAngebotpositionI);

	public AngebotpositionDto[] angebotpositionFindByAngebotIId(
			Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
	
	public AngebotpositionDto[] angebotpositionFindByAngebotIIdOhneAlternative(
			Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AngebotpositionDto angebotpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI)
			throws RemoteException;

	public AngebotpositionDto angebotpositionFindByAngebotIIdAngebotpositionsartCNr(
			Integer iIdAngebotI, String positionsartCNrI)
			throws EJBExceptionLP, RemoteException;
	
	public AngebotpositionDto angebotpositionFindByAngebotIIdISort(
			Integer iIdAngebotI, Integer iSort)
			throws EJBExceptionLP, RemoteException;	

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
    public Integer getPositionNummer(Integer positionIId) throws RemoteException;
    
    public void berechnePauschalposition(BigDecimal wert,Integer positionIId, Integer belegIId,TheClientDto theClientDto) 
	throws EJBExceptionLP, RemoteException;
    
	public Integer getLastPositionNummer(Integer reposIId) throws EJBExceptionLP, RemoteException ;

	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position);

	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer ermitteln
	 * 
	 * @param rechnungIId die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt 
	 *   werden soll.
	 *   
	 * @return 0 ... n 
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId) throws EJBExceptionLP ;

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
	
	public Double berechneArbeitszeitSoll(Integer iIdAngebotI,
			TheClientDto theClientDto) ;
	
}
