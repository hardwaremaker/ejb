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
package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AnsprechpartnerFac {

	// FLR Spaltennamen aus Hibernate Mapping.
	public static final String FLR_ANSPRECHPARTNER_PARTNER = "flrpartner";
	public static final String FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER = "flrpartneransprechpartner";
	public static final String FLR_ANSPRECHPARTNER_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_ANSPRECHPARTNER_I_SORT = "i_sort";
	public static final String FLR_ANSPRECHPARTNER_PARTNER_I_ID_ANSPRECHPARTNER = "partner_i_id_ansprechpartner";
	public static final String FLR_ANSPRECHPARTNER_ANSPRECHPARTNERFUNKTION_I_ID = "ansprechpartnerfunktion_i_id";

	public static final String FLR_ANSPRECHPARTNER_VERSTECKT = "b_versteckt";
	public static final String FLR_ANSPRECHPARTNER_NEWSLETTER_EMPFAENGER = "b_newsletter_empfaenger";

	public void removeAnsprechpartner(AnsprechpartnerDto ansprechpartnerDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createAnsprechpartner(
			AnsprechpartnerDto ansprechpartnerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnsprechpartner(AnsprechpartnerDto ansprechpartnerDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKeyOhneExc(
			Integer iIdI, TheClientDto theClientDto) throws RemoteException;

	public Map<?, ?> getAllAnsprechpartnerfunktion(String loI, TheClientDto theClientDto)
			throws RemoteException;

	public Integer createAnsprechpartnerfunktion(
			AnsprechpartnerfunktionDto ansprechpartnerfunktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAnsprechpartnerfunktion(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAnsprechpartnerfunktion(
			AnsprechpartnerfunktionDto ansprechpartnerfunktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAnsprechpartnerfunktion(
			AnsprechpartnerfunktionDto ansprechpartnerfunktionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByPrimaryKey(
			Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByCnr(
			String sCnrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnsprechpartnerfunktionDto[] ansprechpartnerfunktionFindAll(
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<?> getAllAnsprechpartner(Integer iIdPartnerI, TheClientDto theClientDto)
			throws RemoteException;

	public void createAnsprechpartnerfunktionspr(
			AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDtoI)
			throws EJBExceptionLP, RemoteException;

	public void removeAnsprechpartnerfunktionspr(String localeCNr,
			Integer ansprechpartnerIId) throws EJBExceptionLP, RemoteException;

	public void removeAnsprechpartnerfunktionspr(
			AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnsprechpartnerfunktionspr(
			AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto)
			throws EJBExceptionLP, RemoteException;

	public AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprFindByPrimaryKey(
			String localeCNr, Integer ansprechpartnerIId)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheAnsprechpartner(Integer iIdansprechpartner1,
			Integer iIdansprechpartner2, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByAnsprechpartnerIId(
			Integer idAnsprechpartnerI, TheClientDto theClientDto) throws RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIId(
			Integer idPpartnerI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdAnsprechpartner(
			Integer idPpartnerI, TheClientDto theClientDto);
	
	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOhneExc(
			Integer idPpartnerI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer getMaxISort(Integer iIdPartnerI) throws RemoteException;

	public AnsprechpartnerDto ansprechpartnerFindErstenEinesPartnersOhneExc(
			Integer partnerIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdAndPartnerIIdAnsprechpartner(
			Integer iIdPartnerI, Integer iIdPartnerAnsprechpartnerI,
			TheClientDto theClientDto) throws RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOrPartnerIIdAnsprechpartner(
			Integer iIdPartnerI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOrPartnerIIdAnsprechpartnerOhneExc(
			Integer iIdPartnerI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void zusammenfuehrenAnsprechpartner(
			AnsprechpartnerDto ansprechpartnerZielDto,
			int ansprechpartnerQuellDtoIid, PartnerDto partnerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public String getUebersteuerteEmpfaenger(PartnerDto partnerDto,
			String reportname, boolean bEmail, TheClientDto theClientDto);
}
