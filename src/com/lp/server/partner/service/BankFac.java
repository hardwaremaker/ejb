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
package com.lp.server.partner.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BankFac {

	// Feldlaengen.
	static public final int MAX_BIC = 11;
	static public final int MAX_BLZ = 11;
	static public final int MAX_BANK = 40;
	static public final int MAX_IBAN = 40;
	static public final int MAX_KTONR = 50;

	// FLR Spaltennamen aus Hibernate Mapping.
	// FLRPartnerbank
	public static final String FLR_PARTNERBANK_FLRPARTNER = "flrpartner";
	public static final String FLR_PARTNERBANK_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_PARTNERBANK__C_BLZ = "c_blz";
	public static final String FLR_PARTNERBANK__C_BIC = "c_bic";

	public static final String FLR_PARTNERBANK = "flrpartnerbank";

	// Partnerbank.
	public static final String FLR_PARTNERBANK_KTONR = "c_ktonr";
	public static final String FLR_PARTNERBANK_IBAN = "c_iban";
	public static final String FLR_PARTNERBANK_BLZ = "c_blz";
	public static final String FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 = FLR_PARTNERBANK_FLRPARTNER
			+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;
	public static final String FLR_PARTNERBANK_LANDPLZORT_ORT_NAME = FLR_PARTNERBANK_FLRPARTNER
			+ "."
			+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
			+ "."
			+ SystemFac.FLR_LP_FLRORT + "." + SystemFac.FLR_LP_ORTNAME;

	public Integer createPartnerbank(PartnerbankDto partnerbankDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer getMaxISort(Integer iIdPartnerI) throws RemoteException;

	public void removePartnerbank(PartnerbankDto partnerbankDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePartnerbank(PartnerbankDto partnerbankDto)
			throws EJBExceptionLP, RemoteException;

	public PartnerbankDto partnerbankFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createBank(BankDto bankDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBank(Integer partnerIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBank(BankDto bankDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BankDto bankFindByPrimaryKey(Integer partnerIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BankDto bankFindByPrimaryKeyOhneExc(Integer partnerIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerbankDto[] partnerbankFindByPartnerIId(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerbankDto[] partnerbankFindByPartnerIIdOhneExc(
			Integer partnerIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public PartnerbankDto[] partnerbankFindByPartnerBankIId(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerbankDto[] partnerbankFindByPartnerBankIIdOhneExc(
			Integer partnerIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
}
