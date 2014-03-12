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

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LieferscheinServiceFac {
	// Tabellennamen
	public static final String LS_LIEFERSCHEINTEXT = "LS_LIEFERSCHEINTEXT";

	public static final String LIEFERSCHEINTEXT_KOPFTEXT = MediaFac.MEDIAART_KOPFTEXT;
	public static final String LIEFERSCHEINTEXT_FUSSTEXT = MediaFac.MEDIAART_FUSSTEXT;

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_LIEFERSCHEINTEXT_I_ID = "i_id";
	public static final String FLR_LIEFERSCHEINTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_LIEFERSCHEINTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_LIEFERSCHEINTEXT_MEDIAART_C_NR = "mediaart_c_nr";
	public static final String FLR_LIEFERSCHEINTEXT_X_TEXTINHALT = "x_textinhalt";

	public static final String FLR_LIEFERSCHEINPOSITIONART_POSITIONSART_C_NR = "positionsart_c_nr";

	// Fix verdrahtet Lieferscheintext
	public static final String LIEFERSCHEIN_DEFAULT_KOPFTEXT = "Unsere Lieferung umfa\u00DFt wie folgt:";
	public static final String LIEFERSCHEIN_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihr Vertrauen.";

	public String createLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferscheinartDto lieferscheinartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferscheinartDto[] lieferscheinartFindAll() throws EJBExceptionLP,
			RemoteException;

	public void createLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP,
			RemoteException;

	public LieferscheinartsprDto lieferscheinartsprFindByPrimaryKey(
			String spracheCNr, String lieferscheinartCNr)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinartsprDto lieferscheinartsprFindBySpracheAndCNr(
			String pSprache, String pArt) throws EJBExceptionLP,
			RemoteException;

	public String createLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferscheinpositionart(
			String cNrLieferscheinpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionartDto lieferscheinpositionartFindByPrimaryKey(
			String cNrLieferscheinpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<String, String> lieferscheinpositionartFindAll(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void createLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLieferscheinstatus(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLieferscheinstatuss(
			LieferscheinstatusDto[] lieferscheinstatusDtos)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinstatusDto lieferscheinstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP, RemoteException;

	public Integer createLieferscheintext(
			LieferscheintextDto lieferscheintextDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(
			Integer iIdLieferscheintextI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheintextDto createDefaultLieferscheintext(String sMediaartI,
			String sLocaleI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BegruendungDto begruendungFindByPrimaryKey(Integer iId);

	public void removeBegruendung(Integer iId);

	public Integer createBegruendung(BegruendungDto begruendungDto);
	
	public void updateBegruendung(BegruendungDto begruendungDto);

}
