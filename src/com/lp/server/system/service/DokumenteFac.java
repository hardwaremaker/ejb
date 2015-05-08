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
package com.lp.server.system.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface DokumenteFac {

	public final static String FLR_DOKUMENT_DATENFORMAT_C_NR = "datenformat_c_nr";
	public final static String FLR_DOKUMENT_C_DATEINAME = "c_dateiname";
	public final static String FLR_DOKUMENT_T_ANLEGEN = "t_anlegen";

	public final static String FLR_DOKUMENTSCHLAGWORT_DOKUMENT_I_ID = "dokument_i_id";
	public final static String FLR_DOKUMENTSCHLAGWORT_C_SCHLAGWORT = "c_schlagwort";

	public final static String FLR_BELEGARTDOKUMENT_DOKUMENT_I_ID = "dokument_i_id";
	public final static String FLR_BELEGARTDOKUMENT_FLRDOKUMENT = "flrdokument";
	public final static String FLR_BELEGARTDOKUMENT_BELEGART_C_NR = "belegart_c_nr";
	public final static String FLR_BELEGARTDOKUMENT_I_BELEGARTID = "i_belegartid";
	public final static String FLR_BELEGARTDOKUMENT_T_ANLEGEN = "t_anlegen";

	public Integer createDokument(DokumentDto dokumentDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeDokument(DokumentDto dokumentDto) throws EJBExceptionLP,
			RemoteException;

	public void updateDokument(DokumentDto dokumentDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public DokumentDto dokumentFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public DokumentDto dokumentFindByPrimaryKeyOhneInhaltBeiPdf(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP,
			RemoteException;

	public void removeDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP,
			RemoteException;

	public void updateDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP,
			RemoteException;

	public void updateDokumentschlagworts(
			DokumentschlagwortDto[] dokumentschlagwortDtos)
			throws EJBExceptionLP, RemoteException;

	public DokumentschlagwortDto dokumentschlagwortFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public DokumentschlagwortDto[] dokumentschlagwortFindByDokumentIId(
			Integer dokumentIId) throws EJBExceptionLP, RemoteException;

	public Integer createBelegartdokument(
			BelegartdokumentDto belegartdokumentDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeBelegartdokument(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removeBelegartdokument(BelegartdokumentDto belegartdokumentDto)
			throws RemoteException, EJBExceptionLP;

	public void updateBelegartdokument(BelegartdokumentDto belegartdokumentDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public BelegartdokumentDto belegartdokumentFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			String belegartCNr, Integer iBelegartid,
			int iSortierungNeuePositionI) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheBelegartDokument(Integer iId1, Integer iId2)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheBelegartIdBeiBelegartdokumenten(String cNrBelegart,
			Integer iBelegartIdOld, Integer iBelegartIdNew, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BelegartdokumentDto[] belegartdokumentFindByBelegartCNrBelegartId(
			String sBelegartCNr, Integer iBelegartId) throws EJBExceptionLP,
			RemoteException;

	public BelegartdokumentDto[] belegartdokumentFindByBelegartCNrBelegartIdOhneExc(
			String sBelegartCNr, Integer iBelegartId) throws RemoteException;
	
	public BelegartdokumentDto[] belegartdokumentFindAll();
}
