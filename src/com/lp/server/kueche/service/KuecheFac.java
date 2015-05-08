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
package com.lp.server.kueche.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;

@Remote
public interface KuecheFac {
	public Integer createKassaartikel(KassaartikelDto speisekassaDto)
			throws RemoteException;

	public Integer createSpeiseplan(SpeiseplanDto speiseplanDto,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createTageslos(TageslosDto tageslosDto)
			throws RemoteException;

	public void removeKassaartikel(KassaartikelDto speisekassaDto)
			throws RemoteException;

	public void updateKassaartikel(KassaartikelDto speisekassaDto)
			throws RemoteException;

	public KassaartikelDto kassaartikelFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public int stiftdatenImportieren(Kdc100logDto[] kdc100lodDtos,
			Integer lagerIId_Ziel, TheClientDto theClientDto)
			throws RemoteException;

	public void removeSpeiseplan(SpeiseplanDto speiseplanDto)
			throws RemoteException;

	public void updateSpeiseplan(SpeiseplanDto speiseplanDto,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getTheoretischerWareneinsatz(Integer speiseplanIId,
			Integer artikelIId, BigDecimal nMenge, TheClientDto theClientDto)
			throws RemoteException;

	public SpeiseplanDto speiseplanFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public int getAnzahlKassaimportZuSpeiseplan(Integer speiseplanIId)
			throws RemoteException;

	public void removeTageslos(TageslosDto tageslosDto) throws RemoteException;

	public ArrayList<String> importiereKassenfileOscar(
			ArrayList<String> zeilen, TheClientDto theClientDto)
			throws RemoteException;

	public void importiereAlleKassenfiles(TheClientDto theClientDto);

	public void updateTageslos(TageslosDto tageslosDto) throws RemoteException;

	public ArrayList<String> importiereKassenfileGaestehaus(
			ArrayList<String> zeilen, TheClientDto theClientDto)
			throws RemoteException;

	public TageslosDto tageslosFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public TageslosDto tageslosFindByLagerIIdBSofortverbrauch(
			Integer lagerIIdAbbuchung, Short bSofortverbrauch)
			throws RemoteException;

	public SpeiseplanpositionDto speiseplanpositionFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public void vertauscheKassaartikel(Integer iId1I, Integer iId2I)
			throws RemoteException;

	public Integer createKuecheumrechnung(
			KuecheumrechnungDto kuecheumrechnungDto);

	public void removeKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto);

	public void updateKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto);

	public KuecheumrechnungDto kuecheumrechnungFindByPrimaryKey(Integer iId);

	public void removeBedienerlager(BedienerlagerDto bedienerlagerDto);

	public Integer createBedienerlager(BedienerlagerDto bedienerlagerDto);

	public BedienerlagerDto bedienerlagerFindByPrimaryKey(Integer iId);
	
	public void updateBedienerlager(BedienerlagerDto bedienerlagerDto) ;

}
