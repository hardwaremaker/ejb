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
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.ejb.LflfliefergruppePK;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LieferantFac {

	// Paneltabelle
	public static final int ANZAHL_KRITERIEN = 2;
	public static final int IDX_KRIT_GESCHAEFTSJAHR = 0;
	public static final int IDX_KRIT_LIEFERANT_ODER_KUNDE_I_ID = 1;

	// Feldlaengen
	public static final int MAX_HINWEIS = 80;

	// FLR Spaltennamen aus Hibernate Mapping.
	public static final String FLR_PARTNER = "flrpartner";
	public static final String FLR_KONTO = "flrkonto";

	public static final String FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 = FLR_PARTNER
			+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;

	public static final String FLR_LIEFERANT_TBESTELLSPERREAM = "t_bestellsperream";

	public static final String FLR_PARTNER_LANDPLZORT_ORT_NAME = FLR_PARTNER
			+ "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
			+ SystemFac.FLR_LP_FLRORT + "." + SystemFac.FLR_LP_ORTNAME;

	public static final String FLR_PARTNER_LANDPLZORT_PLZ = FLR_PARTNER + "."
			+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
			+ SystemFac.FLR_LP_LANDPLZORTPLZ;

	public static final String FLR_PARTNER_LANDCLKZ = FLR_PARTNER + "."
			+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
			+ SystemFac.FLR_LP_FLRLAND + "." + SystemFac.FLR_LP_LANDLKZ;

	public static final String FLR_LF_LIEFERGRUPPE = "flrliefergruppe";
	public static final String FLR_ID_COMP_LF_LIEFERANT_I_ID = "id_comp.lieferant_i_id";
	public static final String FLR_KONTO_I_ID_KREDITORENKONTO = "konto_i_id_kreditorenkonto";
	public static final String FLR_LIEFERANT_I_ID = "i_id";
	public static final String FLR_LIEFERANT_B_MOEGLICHERLIEFERANT = "b_moeglicherlieferant";
	public static final String FLR_LIEFERANT_T_FREIGABE = "t_freigabe";
	public static final String FLR_LIEFERANT_MANDANT_C_NR = "mandant_c_nr";
	
	// Maximale, minimale Zahlenbereiche.
	static public final int MAX_RABATTSATZ = 1000;
	static public final int MIN_RABATTSATZ = -1000;
	static public final int FRACTION_RABATTSATZ = 2;

	static public final long MAX_KREDITLIMIT = 99999999;
	static public final int MIN_KREDITLIMIT = 0;
	static public final int FRACTION_KREDITLIMIT = 2;

	static public final long MAX_JAHRESBONUS = 99999999;
	static public final int MIN_JAHRESBONUS = 0;
	static public final int FRACTION_JAHRESBONUS = 0;

	static public final long MAX_BEURTEILUNG = 100000;
	static public final int MIN_BEURTEILUNG = 0;
	static public final int FRACTION_BEURTEILUNG = 0;

	public LieferantDto lieferantFindByCKundennrcNrMandant(String cKundennr,
			String cNrMandantI, TheClientDto theClientDto);
	
	public Integer createLieferant(LieferantDto lieferantDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLieferant(LieferantDto lieferantDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLieferant(LieferantDto lieferantDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void zusammenfuehrenLieferant(LieferantDto lieferantZielDto,
			int lieferantQuellDtoIId, Integer iLieferantPartnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferantDto lieferantFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto);
	public LieferantDto lieferantFindByPrimaryKeySmall(Integer iIdI);
	
	public LieferantDto lieferantFindByPrimaryKeySmallWithNull(Integer iIdI);
	
	public LieferantDto lieferantFindByPrimaryKeyOhneExc(Integer iIdI,
			TheClientDto theClientDto) throws RemoteException;

	public void removeLieferantPartnerRechnungsadresse(
			LieferantDto lieferantDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void updateLieferantRechnungsadresse(LieferantDto lieferantDtoI,
			TheClientDto theClientDto) throws RemoteException;

	public ArrayList<?> getWareneingangspositionen(
			StatistikParamDto statistikParamDtoI, String sWaehrungI,
			boolean bVerdichtetNachArtikel,boolean bEingeschraenkt, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LflfliefergruppePK createLflfliefergruppe(
			LflfliefergruppeDto lflfliefergruppeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLflfliefergruppe(Integer lieferantIId,
			Integer lfliefergruppeIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LflfliefergruppeDto lflfliefergruppeFindByPrimaryKey(
			Integer lieferantIId, Integer lfliefergruppeIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIId(
			Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdOhneExc(
			Integer lieferantIId, TheClientDto theClientDto)
			throws RemoteException;

	public LieferantbeurteilungDto[] lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(
			Integer lieferantIId, Timestamp tBis) throws RemoteException;

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdLiefergruppeIId(
			Integer lieferantIId, Integer liefergruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdLiefergruppeIIdOhneExc(
			Integer lieferantIId, Integer liefergruppeIId,
			TheClientDto theClientDto);

	public ArrayList<?> getAllLFGroupsWithMinOneLF() throws EJBExceptionLP,
			RemoteException;

	public LieferantDto[] lieferantFindByLiefergruppeIId(
			Integer iIdLiefergruppeI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferantDto[] lieferantfindByKontoIIdKreditorenkonto(Integer kontoIId);
	
	public LieferantDto lieferantFindByiIdPartnercNrMandantOhneExc(
			Integer iIdPartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public LieferantDto[] lieferantFindByRechnungsadresseiIdPartnercNrMandant(
			Integer iIdRechnungsadressePartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferantDto[] lieferantFindByRechnungsadresseiIdPartnercNrMandantOhneExc(
			Integer iIdRechnungsadressePartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException;

	public KontoDto createKreditorenkontoZuLieferantenAutomatisch(
			Integer lieferantIId, boolean nichtErstellen, String kontonummerVorgabe,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferantDto[] lieferantFindByPartnerIId(Integer iIdPartnerI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferantDto[] lieferantFindByPartnerIIdOhneExc(Integer iIdPartnerI,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createLieferantbeurteilung(
			LieferantbeurteilungDto beurteilungDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeLieferantbeurteilung(
			LieferantbeurteilungDto lieferantbeurteilungDto)
			throws RemoteException;

	public void updateLieferantbeurteilung(
			LieferantbeurteilungDto beurteilungDto, TheClientDto theClientDto)
			throws RemoteException;

	public LieferantbeurteilungDto lieferantbeurteilungFindByPrimaryKey(
			Integer iId) throws RemoteException;

	public LieferantbeurteilungDto lieferantbeurteilungfindByLieferantIIdTDatum(
			Integer lieferantIId, Timestamp tDatum) throws RemoteException;

	public String importiereArtikellieferant(Integer lieferantIId, ArrayList<String[]> daten, boolean bImportieren,
			TheClientDto theClientDto);

}
