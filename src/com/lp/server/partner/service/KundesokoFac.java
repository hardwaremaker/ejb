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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface KundesokoFac {
	public static final String FLR_KUNDESOKO_KUNDE_I_ID = "kunde_i_id";
	public static final String FLR_KUNDESOKO_PREISGUELTIGAB = "t_preisgueltigab";
	public static final String FLR_KUNDESOKO_PREISGUELTIGBIS = "t_preisgueltigbis";
	public static final String FLR_KUNDESOKO_KUNDEARTIKELNUMMER = "c_kundeartikelnummer";

	public static final String FLR_KUNDESOKOMENGESTAFFEL_KUNDESOKO_I_ID = "kundesoko_i_id";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_N_MENGE = "n_menge";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_N_FIXPREIS = "n_fixpreis";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_F_RABATTSATZ = "f_rabattsatz";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_N_BERECHNETERPREIS = "n_berechneterpreis";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_T_PREISGUELTIGAB = "t_preisgueltigab";
	public static final String FLR_KUNDESOKOMENGESTAFFEL_T_PREISGUELTIGBIS = "t_preisgueltigbis";

	public Integer createKundesoko(KundesokoDto kundesokoDtoI,
			KundesokomengenstaffelDto defaultMengenstaffelDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKundesoko(KundesokoDto kundesokoDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKundesoko(KundesokoDto kundesokoDtoI,
			KundesokomengenstaffelDto defaultMengenstaffelDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KundesokoDto kundesokoFindByPrimaryKey(Integer iIdI)
			throws EJBExceptionLP, RemoteException;

	public KundesokoDto[] kundesokoFindByKundeIIdArtikelIId(Integer kundeIIdI,
			Integer artikelIIdI) throws EJBExceptionLP, RemoteException;

	public KundesokoDto[] kundesokoFindByKundeIIdArtgruIId(Integer kundeIIdI,
			Integer artgruIIdI) throws EJBExceptionLP, RemoteException;

	public KundesokoDto[] kundesokoFindByKundeIId(Integer kundeIIdI)
			throws EJBExceptionLP, RemoteException;

	public KundesokoDto[] kundesokoFindByKundeIIdOhneExc(Integer kundeIIdI)
			throws RemoteException;

	public KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatum(
			Integer kundeIIdI, Integer artikelIIdI, Date tGueltigkeitsdatumI)
			throws EJBExceptionLP, RemoteException;

	public KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
			Integer kundeIIdI, Integer artikelIIdI, Date tGueltigkeitsdatumI)
			throws RemoteException;

	public KundesokoDto kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatum(
			Integer kundeIIdI, Integer artgruIIdI, Date tGueltigkeitsdatumI)
			throws EJBExceptionLP, RemoteException;

	public KundesokoDto kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
			Integer kundeIIdI, Integer artgruIIdI, Date tGueltigkeitsdatumI)
			throws RemoteException;

	public Integer createKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KundesokomengenstaffelDto kundesokomengenstaffelFindByPrimaryKey(
			Integer iIdI) throws EJBExceptionLP, RemoteException;

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIId(
			Integer iIdKundesokoIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
			Integer iIdKundesokoIId, Date tGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto);
	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdOhneExc(
			Integer iIdKundesokoIId, TheClientDto theClientDto) throws RemoteException;

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdNMenge(
			Integer kundesokoIIdI, BigDecimal nMengeI) throws EJBExceptionLP,
			RemoteException;
}
