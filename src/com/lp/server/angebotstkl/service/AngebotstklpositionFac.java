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
package com.lp.server.angebotstkl.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotstklpositionFac {
	public static final String FLR_AGSTKLPOSITION_I_SORT = "i_sort";
	public static final String FLR_AGSTKLPOSITION_AGSTKL_I_ID = "agstkl_i_id";
	public static final String FLR_AGSTKLPOSITION_AGSTKLPOSITIONART_C_NR = "agstklpositionsart_c_nr";
	public static final String FLR_AGSTKLPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_AGSTKLPOSITION_N_MENGE = "n_menge";
	public static final String FLR_AGSTKLPOSITION_C_BEZ = "c_bez";
	public static final String FLR_AGSTKLPOSITION_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_AGSTKLPOSITION_B_DRUCKEN = "b_drucken";
	public static final String FLR_AGSTKLPOSITION_FLRAGSTKL = "flragstkl";
	public static final String FLR_AGSTKLPOSITION_FLRARTIKEL = "flrartikel";

	public static final String SCHEMA_HV_FEATURE_DRUCKEN = "Drucken";

	public Integer createAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	public Integer createAngebotpositions(
			AgstklpositionDto[] agstklpositionDtos, TheClientDto theClientDto);

	public void removeAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AgstklpositionDto agstklpositionFindByPrimaryKey(
			Integer iIdAgstklpositionI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;
	
	public AgstklpositionDto agstklpositionFindByPrimaryKeyOhneExc(
			Integer iIdAgstklpositionI);

	public AgstklpositionDto[] agstklpositionFindByAgstklIId(
			Integer iIdAgstklI, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdOhneExc(
			Integer iIdAgstklI, TheClientDto theClientDto) throws RemoteException;

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdBDruckenOhneExc(
			Integer iIdAgstklI, Short bDruckenI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdMengeNotNullOhneExc(
			Integer iIdAgstklI, TheClientDto theClientDto) throws RemoteException;

	public void kopiereAgstklPositionen(Integer agstklIId_Quelle,
			Integer agstklIId_Ziel, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer getMaxISort(Integer iIdAgstklI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public String getAgstklpositionenAsTextblock(
			AgstklpositionDto[] aAgstklpositionDto,
			BigDecimal nMengeStuecklisteI, Locale locDruckI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getPositionAsStringDocumentWS(Integer[] aIIdAngebotStklPosI,
			String idUser) throws RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheAgstklpositionen(Integer idPosition1I,
			Integer idPosition2I) throws EJBExceptionLP, RemoteException;
	public void preiseGemaessKalkulationsartUpdaten(Integer agstklIId,TheClientDto theClientDto);

	/**
	 * Liefert die Bean als PositionImporter zur&uuml;ck, um auf die Methoden
	 * des Interfaces {@link IImportPositionen} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportPositionen asPositionImporter() ;

	/**
	 * Liefert die Bean als HeadImporter zur&uuml;ck, um auf die Methoden
	 * des Interfaces {@link IImportHead} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportHead asHeadImporter();
}
