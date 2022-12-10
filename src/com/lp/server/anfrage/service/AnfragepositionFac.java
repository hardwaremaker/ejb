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
package com.lp.server.anfrage.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AnfragepositionFac {
	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_ANFRAGEPOSITION_I_SORT = "i_sort";
	public static final String FLR_ANFRAGEPOSITION_ANFRAGEPOSITIONART_C_NR = "anfragepositionart_c_nr";
	public static final String FLR_ANFRAGEPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_ANFRAGEPOSITION_N_MENGE = "n_menge";
	public static final String FLR_ANFRAGEPOSITION_C_BEZ = "c_bez";
	public static final String FLR_ANFRAGEPOSITION_N_RICHTPREIS = "n_richtpreis";
	public static final String FLR_ANFRAGEPOSITION_X_TEXTINHALT = "x_textinhalt";
	public static final String FLR_ANFRAGEPOSITION_FLRANFRAGE = "flranfrage";
	public static final String FLR_ANFRAGEPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_ANFRAGEPOSITION_FLRMEDIASTANDARD = "flrmediastandard";

	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_I_ANLIEFERZEITINSTUNDEN = "i_anlieferzeitinstunden";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_N_ANLIEFERMENGE = "n_anliefermenge";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_N_NETTOGESAMTPREISMINUSRABATT = "n_nettogesamtpreisminusrabatt";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION = "flranfrageposition";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATENREPORT_FLRANFRAGEPOSITIONREPORT = "flranfragepositionreport";

	public Integer createAnfrageposition(
			AnfragepositionDto anfragepositionDtoI, TheClientDto theClientDto);

	public void removeAnfrageposition(AnfragepositionDto anfragepositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAnfrageposition(AnfragepositionDto anfragepositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfragepositionDto anfragepositionFindByPrimaryKey(
			Integer iIdAnfragepositionI, TheClientDto theClientDto)
			throws RemoteException;
	
	public AnfragepositionDto anfragepositionFindByPrimaryKeyOhneExc(
			Integer iIdAnfragepositionI);

	public Integer createAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void befuelleZusaetzlichesPreisfeld(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void resetAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByPrimaryKey(
			Integer iIdAnfragepositionlieferdatenI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public int getAnzahlMengenbehafteteAnfragepositionen(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfragepositionDto[] anfragepositionFindByAnfrage(
			Integer iIdAnfrageI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnfragepositionlieferdatenDto[] anfragepositionlieferdatenFindByAnfragepositionIIdBErfasst(
			Integer iIdAnfragepositionI, Short bErfasstI)
			throws EJBExceptionLP, RemoteException;

	public String getPositionAsStringDocumentWS(Integer[] aIIdAnfragePOSI,
			String cNrUserI) throws RemoteException;

	public AnfragepositionDto[] anfragepositionFindByAnfrageIIdArtikelIId(
			Integer iIdAnfrageI, Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByAnfragepositionIId(
			Integer iIdAnfragepositionI) throws RemoteException, EJBExceptionLP;

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
			Integer iIdAnfragepositionI) throws RemoteException;

	public void vertauscheAnfragepositionen(Integer iIdPosition1I,
			Integer iIdPosition2I, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAnfrageI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;
	
	public Integer getPositionNummer(Integer anfragepositionIId, TheClientDto theClientDto);
	
	public Integer createAnfrageposition(
			AnfragepositionDto anfragepositionDtoI, boolean bMitErsatztypen,
			TheClientDto theClientDto) throws EJBExceptionLP;
	
	public void vertauscheAnfragepositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP;
	public void vertauscheAnfragepositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP;
	public void sortiereNachArtikelnummer(Integer anfrageIId, TheClientDto theClientDto);
	
}
