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

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface AuftragteilnehmerFac {

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_AUFTRAGTEILNEHMER_I_ID = "i_id";
	public static final String FLR_AUFTRAGTEILNEHMER_I_SORT = "i_sort";
	public static final String FLR_AUFTRAGTEILNEHMER_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_AUFTRAGTEILNEHMER_FLRPARNTER = "flrpartner";
	public static final String FLR_AUFTRAGTEILNEHMER_FLRFUNKTION = "flrfunktion";
	public static final String FLR_AUFTRAGTEILNEHMER_FLRPERSONAL = "flrpersonal";

	public Integer createAuftragteilnehmer(
			AuftragteilnehmerDto auftragteilnehmerDto, String pUser)
			throws EJBExceptionLP, RemoteException;

	public AuftragteilnehmerDto[] auftragteilnehmerFindByPartnerIIdAuftragteilnehmer(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public void removeAuftragteilnehmer(
			AuftragteilnehmerDto auftragteilnehmerDto) throws EJBExceptionLP,
			RemoteException;

	public void updateAuftragteilnehmer(
			AuftragteilnehmerDto auftragteilnehmerDto) throws EJBExceptionLP,
			RemoteException;

	public AuftragteilnehmerDto auftragteilnehmerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public boolean istPartnerEinAuftragteilnehmer(Integer iIdPartnerI,
			Integer iIdAuftragI) throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAuftragI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public Integer getMaxISort(Integer iIdAuftragI) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheAuftragteilnehmer(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP, RemoteException;
}
