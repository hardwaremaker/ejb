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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

import javax.ejb.Remote;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface InternebestellungFac {
	public static final String KRIT_ARTIKEL_I_ID = "artikelIId";
	public static final String KRIT_INTERNEBESTELLUNG_BERUECKSICHTIGEN = "internebestellungberuecksichtigen";
	public static final String KRIT_INTERNEBESTELLUNG = "internebestellung";

	public InternebestellungDto createInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public InternebestellungDto updateInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public InternebestellungDto internebestellungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public void erzeugeInterneBestellung(Boolean bVorhandeneLoeschen,
			Integer iVorlaufzeit, Integer iToleranz,
			java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			Boolean bVerdichten, Integer iVerdichtungsTage,boolean bInterneBestellung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> erzeugeInterneBestellung(Integer iVorlaufzeit,
			Integer iToleranz,
			java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			boolean bInterneBestellungBeruecksichtigen,
			Integer stuecklisteIId,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau,
			boolean bInterneBestellung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public ArrayList<?> pruefeOffeneRahmenmengen(TheClientDto theClientDto)
			throws RemoteException;

	public ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto,
			Integer iVorlaufzeitInTagen, Integer iToleranz,
			Date defaultDatumFuerEintraegeOhneLiefertermin,boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<MaterialbedarfDto> berechneBedarfe(Integer artikelIId,
			Integer iVorlaufzeitInTagen, Integer iToleranz,
			Date defaultDatumFuerEintraegeOhneLiefertermin,boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId)
			throws EJBExceptionLP, RemoteException;

	public void removeInternebestellungEinesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			Integer iArtikelId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			Integer iArtikelId, boolean bInternebestellungMiteinbeziehen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			ArtikelDto artikelDto,boolean bTermineVorHeuteAufHeute, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void interneBestellungUeberleiten(Integer interneBestellungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Set<Integer> getInternebestellungIIdsEinesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void verdichteInterneBestellung(Integer iVerdichtungsTage,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public BigDecimal getFiktivenLagerstandZuZeitpunkt
			(ArtikelDto artikelDto, TheClientDto theClientDto, Timestamp tLagerstandsdatum);
}
