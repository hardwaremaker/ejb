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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.LinkedHashMap;

import javax.ejb.Remote;

import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface MahnwesenFac {
	public MahnlaufDto createMahnlaufMitMahnvorschlag(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MahnlaufDto createMahnlaufAusRechnung(Integer rechnungIId,
			Integer mahnstufeIId, Date tMahndatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMahnlauf(MahnlaufDto mahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateMahnlauf(MahnlaufDto mahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MahnlaufDto mahnlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public MahnlaufDto[] mahnlaufFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer createMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MahnstufeDto mahnstufeFindByPrimaryKey(MahnstufePK mahnstufePK)
			throws EJBExceptionLP, RemoteException;

	public MahnstufeDto[] mahnstufeFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public LinkedHashMap<Integer, Integer> getAllMahnstufe(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public MahnungDto createMahnung(MahnungDto mahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMahnung(MahnungDto mahnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MahnungDto updateMahnung(MahnungDto mahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MahnungDto mahnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public MahnungDto[] mahnungFindByMahnlaufIId(Integer mahnlaufIId)
			throws EJBExceptionLP, RemoteException;

	public MahnungDto[] mahnungFindByRechnungIId(Integer rechnungIId)
			throws EJBExceptionLP, RemoteException;

	public MahnungDto mahnungFindByRechnungMahnstufe(Integer rechnungIId,
			Integer mahnstufeIId) throws EJBExceptionLP, RemoteException;

	public int getMahntageVonMahnstufe(Integer mahnstufeIId,
			TheClientDto theClientDto) throws RemoteException;

	public Integer berechneMahnstufeFuerRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) throws RemoteException;

	public void mahneMahnung(Integer mahnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void mahneMahnungRueckgaengig(Integer mahnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void mahneMahnlauf(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void mahneMahnlaufRueckgaengig(Integer mahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Date berechneFaelligkeitsdatumFuerMahnstufe(
			java.util.Date dBelegdatum, Integer zahlungszielIId,
			Integer mahnstufeIId, TheClientDto theClientDto)
			throws RemoteException;

	public Boolean bGibtEsEinenOffenenMahnlauf(String mandantCNr,
			TheClientDto theClientDto) throws RemoteException;

	public boolean istRechnungMahnbar(Integer rechnungIId,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getSummeEinesKundenImMahnlauf(Integer mahnlaufIId,
			Integer kundeIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public MahnlaufDto createMahnlauf(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMahnlauf(Integer iMahnlaufIId);

	public Integer getAktuelleMahnstufeEinerRechnung(Integer rechnungIId,
			TheClientDto theClientDto);

	public java.sql.Date getAktuellesMahndatumEinerRechnung(
			Integer rechnungIId, TheClientDto theClientDto);
}
