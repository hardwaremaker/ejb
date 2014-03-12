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
package com.lp.server.anfrage.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AnfrageFac extends IAktivierbarControlled {

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_ANFRAGE_ANFRAGEART_C_NR = "anfrageart_c_nr";
	public static final String FLR_ANFRAGE_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_ANFRAGE_ANFRAGESTATUS_C_NR = "anfragestatus_c_nr";
	public static final String FLR_ANFRAGE_WAEHRUNG_C_NR_ANFRAGEWAEHRUNG = "waehrung_c_nr_anfragewaehrung";
	public static final String FLR_ANFRAGE_N_GESAMTANFRAGEWERTINANFRAGEWAEHRUNG = "n_gesamtanfragewertinanfragewaehrung";
	public static final String FLR_ANFRAGE_FLRLIEFERANT = "flrlieferant";
	public static final String FLR_ANFRAGE_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_ANFRAGE_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_ANFRAGE_LFLIEFERGRUPPE_I_ID = "lfliefergruppe_i_id";

	public Integer createAnfrage(AnfrageDto anfrageDtoI, TheClientDto theClientDto);

	public void storniereAnfrage(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnfrage(AnfrageDto anfrageDtoI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnfrageDto anfrageFindByPrimaryKey(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public AnfrageDto anfrageFindByPrimaryKeyOhneExc(Integer iIdAnfrageI);

	public AnfrageDto[] anfrageFindByMandantCNr(String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public AnfrageDto anfrageFindByCNrMandantCNrOhneExc(String cNr, String cNrMandantI);

	public AnfrageDto[] anfrageFindByAnsprechpartnerlieferantIIdMandantCNr(
			Integer iAnsprechpartnerLieferantIId, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfrageDto[] anfrageFindByAnsprechpartnerlieferantIIdMandantCNrOhneExc(
			Integer iAnsprechpartnerLieferantIId, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException;

	public AnfrageDto[] anfrageFindByAnfrageIIdLiefergruppenanfrage(
			Integer iIdAnfrageLiefergruppenanfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnfrageDto[] anfrageFindByLieferantIIdAnfrageadresseMandantCNr(
			Integer iLieferantIIdAnfrageadresseI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfrageDto[] anfrageFindByLieferantIIdAnfrageadresseMandantCNrOhneExc(
			Integer iLieferantIIdAnfrageadresseI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException;

	public void pruefeUndSetzeAnfragestatusBeiAenderung(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneNettowertGesamt(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> getAngelegteAnfragenNachUmwandlungDerLiefergruppenanfragen(Integer liefergruppeIId, TheClientDto theClientDto);
	
	public void updateAnfrageKonditionen(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeDruckzeitpunkt(Integer iIdAnfrageI, Timestamp timestampI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getAnfragekennung(Integer iIdAnfrageI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void stornoAufheben(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeAnfragestatus(String cNrStatusI, Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void setzeNettogesamtwert(Integer iIdAnfrageI,
			BigDecimal nNettogesamtwertI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> erzeugeAnfragenAusLiefergruppenanfrage(Integer iIdAnfrageI,ArrayList<Integer> bereitserzeugteAnfragen,
			TheClientDto theClientDto);

	public void erzeugeAnfragenAusBestellvorschlag(
			BestellvorschlagUeberleitungKriterienDto bestellvorschlagUeberleitungKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeVersandzeitpunktAufJetzt(Integer iAnfrageIId, String sDruckart);
	public BigDecimal berechneEinkaufswertIst(Integer iIdAnfrageI,
			String sArtikelartI, TheClientDto theClientDto);

}
