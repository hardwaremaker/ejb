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
package com.lp.server.bestellung.service;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;

import javax.ejb.Remote;

import com.lp.server.bestellung.ejb.BsmahnstufePK;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BSMahnwesenFac {

	// Fix verdrahtet Mahnstufen
	public static final int MAHNSTUFE_MINUS1 = -1;
	public static final int MAHNSTUFE_1 = 1;
	public static final int MAHNSTUFE_2 = 2;
	public static final int MAHNSTUFE_3 = 3;
	public static final int MAHNSTUFE_0 = 0;
	public static final String MAHNSTUFE_ABMAHNSTUFE = "AB";
	public static final int MAHNSTUFE_99 = 99;

	public static final String MAHNART_LIEFERERINNERUNG = "MAHNART_LIEFERERINNERUNG";
	public static final String MAHNART_LIEFER_MAHNUNGEN = "MAHNART_LIEFER_MAHNUNGEN";
	public static final String MAHNART_AB_MAHNUNGEN = "MAHNART_AB_MAHNUNGEN";
	public static final String MAHNART_AB_UND_LIEFER_MAHNUNGEN = "MAHNART_AB_UND_LIEFER_MAHNUNGEN";

	// Fix verdrahtet Mahntext
	public static final String DEFAULT_TEXT_MAHNSTUFE_1 = "Text Mahnstufe 1";
	public static final String DEFAULT_TEXT_MAHNSTUFE_2 = "Text Mahnstufe 2";
	public static final String DEFAULT_TEXT_MAHNSTUFE_3 = "Text Mahnstufe 3";
	public static final String DEFAULT_TEXT_MAHNSTUFE_0 = "Text AB-Mahnung";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_MAHNTEXT_I_ID = "i_id";
	public static final String FLR_MAHNTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_MAHNTEXT_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNTEXT_X_TEXTINHALT = "c_textinhalt";

	public static final String FLR_MAHNLAUF_I_ID = "i_id";
	public static final String FLR_MAHNLAUF_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNLAUF_T_ANLEGEN = "t_anlegen";

	public static final String FLR_MAHNUNG_I_ID = "i_id";
	public static final String FLR_MAHNUNG_MAHNLAUF_I_ID = "mahnlauf_i_id";
	public static final String FLR_MAHNUNG_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNUNG_D_MAHNDATUM = "d_mahndatum";
	public static final String FLR_MAHNUNG_T_GEDRUCKT = "t_gedruckt";
	public static final String FLR_MAHNUNG_FLRBESTELLUNG = "flrbestellung";
	public static final String FLR_MAHNUNG_OFFENEMENGE = "n_offenemenge";
	public static final String FLR_MAHNUNG_FLRPERSONALANLEGER = "flrpersonalanleger";
	public static final String FLR_MAHNSTUFE_I_ID = "i_id";
	public static final String FLR_MAHNSTUFE_MANDANT_C_NR = "i_tage";
	public static final String FLR_MAHNSTUFE_I_TAGE = "i_tage";
	public static final String FLR_MAHNUNG_FLRBESTELLPOSITION = "flrbestellposition";

	public BSMahnungDto createBSMahnung(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	public BSMahnlaufDto createBSMahnlaufLiefererinnerung(
			TheClientDto theClientDto);
	public void removeBSMahnung(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBSMahnung(BSMahnungDto bSMahnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnungDto updateBSMahnung(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBSMahnungs(BSMahnungDto[] bSMahnungDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnungDto bsmahnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createBSMahnlauf(BSMahnlaufDto bSMahnlaufDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBSMahnlauf(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBSMahnlauf(BSMahnlaufDto bSMahnlaufDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahnlauf(BSMahnlaufDto bSMahnlaufDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahnlaufs(BSMahnlaufDto[] bSMahnlaufDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnlaufDto bsmahnlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createBSMahnstufe(BSMahnstufeDto bSMahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBSMahnstufe(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBSMahnstufe(BSMahnstufeDto bSMahnstufeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahnstufe(BSMahnstufeDto bSMahnstufeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahnstufes(BSMahnstufeDto[] bSMahnstufeDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnstufeDto bsmahnstufeFindByPrimaryKeyOhneExc(
			BsmahnstufePK bsmahnstufePK) throws EJBExceptionLP, RemoteException;

	public void removeBSMahntext(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBSMahntext(BSMahntextDto bSMahntextDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahntext(BSMahntextDto bSMahntextDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBSMahntexts(BSMahntextDto[] bSMahntextDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahntextDto bsmahntextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIId(Integer mahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIIdLieferantIId(
			Integer mahnlaufIId, Integer lieferantIId, String MandantCNr)
			throws EJBExceptionLP, RemoteException;

	public BSMahnstufeDto[] bsmahnstufeFindByMandantCNr(String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnlaufDto[] bsmahnlaufFindByMandantCNr(String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBestellungIId(Integer bestellungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBestellungBSMahnstufe(
			Integer bestellungIId, Integer mahnstufeIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LinkedHashMap<?, ?> getAllBSMahnstufen(String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahntextDto bsmahntextFindByMandantLocaleCNr(String mandantCNr,
			String localeCNr, Integer bsmahnstufeIId) throws EJBExceptionLP,
			RemoteException;

	public BSMahntextDto createDefaultBSMahntext(Integer bsmahnstufeIId,
			String sTextinhaltI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createBSMahntext(BSMahntextDto bSMahntextDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Boolean bGibtEsEinenOffenenBSMahnlauf(String mandantCNr,
			TheClientDto theClientDto) throws RemoteException;

	public void mahneBSMahnlaufRueckgaengig(Integer bsmahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void mahneBSMahnungRueckgaengig(Integer mahnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnlaufDto createBSMahnlaufEchteLiefermahnungen(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnlaufDto createBSMahnlaufABMahnungen(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnlaufDto createABMahnungenUndLieferMahnungenUndLiefererinnerungen(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBestellpositionIIdOhneExc(
			Integer bestellpositionIId, TheClientDto theClientDto) throws RemoteException;

	public void mahneBSMahnung(Integer bsmahnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void mahneBSMahnlauf(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIIdOhneExc(
			Integer mahnlaufIId, TheClientDto theClientDto) throws RemoteException;
	
	public void removeBSMahnungAusMahnlauf(BSMahnungDto bSMahnungDto, TheClientDto theClientDto)
	throws EJBExceptionLP, RemoteException;
	
	public void removeBSMahnlauf(Integer id)
	throws EJBExceptionLP, RemoteException;
}
