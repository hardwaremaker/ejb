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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface WareneingangFac {
	public final static int I_NACHKOMMASTELLEN_EINSTANDSPREISE = 4;

	public static final int MAX_ZEICHEN = 100;
	public static final int MAX_LIEFERSCHEINNUMMER = 50;

	// Wareneingangposition
	public static final String FLR_WEPOS_N_GELIEFERTEMENGE = "n_geliefertemenge";
	public static final String FLR_WEPOS_N_GELIEFERTERPREIS = "n_gelieferterpreis";
	public static final String FLR_WEPOS_BESTELLPOSITION_I_ID = "bestellposition_i_id";
	public static final String FLR_WEPOS_WARENEINGANG_I_ID = "wareneingang_i_id";
	public static final String FLR_WEPOS_FLRBESTELLPOSITION = "flrbestellposition";
	public static final String FLR_WEPOS_FLRWARENEINGANG = "flrwareneingang";
	public static final String FLR_WEPOS_N_EINSTANDSPREIS = "n_einstandspreis";
	public static final String FLR_WEPOS_B_PREISEERFASST = "b_preiseerfasst";

	// Wareneingang
	public static final String FLR_WE_C_LIEFERSCHEIN = "c_lieferscheinnr";
	public static final String FLR_WE_T_LIEFERSCHEINDATUM = "t_lieferscheindatum";
	public static final String FLR_WE_N_TRANSPORTKOSTEN = "n_transportkosten";
	public static final String FLR_WE_N_WECHSELKURS = "n_wechselkurs";
	public static final String FLR_WE_F_GEMEINKOSTENFAKTOR = "f_gemeinkostenfaktor";
	public static final String FLR_WE_T_WARENEINGANGSDATUM = "t_wareneingansdatum";
	public static final String FLR_WE_BESTELLUNG_I_ID = "bestellung_i_id";
	public static final String FLR_WE_I_SORT = "i_sort";
	public static final String FLR_WE_I_ID = "i_id";
	public static final String FLR_WE_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public static final String FLR_WE_FLRBESTELLUNG = "flrbestellung";

	/** @todo JO->JE :-( PJ 4639 */
	public static final String FLRSPALTE_T_WARENEINGANGSDATUM = "t_wareneingansdatum";

	public static final String WARENEINGANGSPOSITION_KOMMENTAR = "K";

	public Integer createWareneingangsposition(
			WareneingangspositionDto weposDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeWareneingangsposition(
			WareneingangspositionDto wareneingangspositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateWareneingangsposition(WareneingangspositionDto weposDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public WareneingangspositionDto wareneingangspositionFindByPrimaryKey(
			Integer iIdWareneingangpositionI) throws EJBExceptionLP,
			RemoteException;

	public WareneingangspositionDto wareneingangspositionFindByPrimaryKeyOhneExc(
			Integer iIdWareneingangpositionI);

	public Integer createWareneingang(WareneingangDto wareneingangDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeWareneingang(WareneingangDto wareneingangDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateWareneingang(WareneingangDto wareneingangDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public WareneingangDto wareneingangFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public WareneingangDto wareneingangFindByPrimaryKeyOhneExc(Integer iId);

	public void vertauscheWareneingang(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP, RemoteException;

	public WareneingangDto[] wareneingangFindByBestellungIId(
			Integer iIdBestellungI) throws EJBExceptionLP, RemoteException;

	public WareneingangspositionDto[] wareneingangspositionFindByWareneingangIId(
			Integer iIdWareneingangI) throws EJBExceptionLP, RemoteException;

	public WareneingangspositionDto[] wareneingangspositionFindByBestellpositionIId(
			Integer iIdBestellpositionI) throws EJBExceptionLP, RemoteException;

	public WareneingangDto[] wareneingangFindByEingangsrechnungIId(
			Integer eingangsrechnungIId);

	public Integer getAnzahlWE(Integer iIdBestellungI) throws EJBExceptionLP,
			RemoteException;

	public Integer getAnzahlWEP(Integer iIdWareneingangI)
			throws EJBExceptionLP, RemoteException;

	public void updateWareneingangspositionInternenKommentar(
			WareneingangspositionDto wepDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneWertDesWareneingangsInBestellungswaehrung(
			Integer wareneingangIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public WEPBuchenReturnDto uebernimmAlleWepsOhneBenutzerinteraktion(
			Integer iIdWareneingangI, Integer iIdBestellungI, ArrayList<Integer> bestellpositionIIds_selektiert, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer[] erfasseAllePreiseOhneBenutzerinteraktion(
			Integer iIdWareneingangI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean allePreiseFuerWareneingangErfasst(Integer iWareneingangIId);

	public Integer getAnzahlWEImZeitraum(Integer artikelIId, Timestamp tVon,
			Timestamp tBis, TheClientDto theClientDto);

	public void wareneingangspositionAlsVerraeumtKennzeichnen(
			Integer wareneingangspositionIId);

	public void bucheWareneingangspositionAmLager(
			WareneingangspositionDto weposDtoI, boolean bLoescheBuchung,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public String generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(
			Integer bsposIId, Integer wareneingangIId);
	
	public EinstandspreiseEinesWareneingangsDto getBerechnetenEinstandspreisEinerWareneingangsposition(
			Integer wareneingangIId, TheClientDto theClientDto);

	public void geliefertPreiseAllerWEPRueckpflegen(java.sql.Date dVon,
			java.sql.Date dBis, TheClientDto theClientDto);

	Integer createWareneingangsposition(WareneingangspositionDto weposDto,
			boolean setartikelAufloesen, TheClientDto theClientDto);

	void updateWareneingangsposition(WareneingangspositionDto weposDtoI,
			boolean setartikelAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP;

	void removeWareneingangsposition(WareneingangspositionDto positionDto,
			boolean setartikelAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public void wareUnterwegsZubuchen(Integer lieferscheinIId,ArrayList<String> wareunterwegsCNr,
			TheClientDto theClientDto);

	public Integer getLieferscheinIIdAusWareUnterwegs(String wareunterwegsCNr,
			TheClientDto theClientDto);
	
	List<WareneingangDto> wareneingangFindByLieferscheinnummer(
			Integer bestellungId, String lieferscheinnummer);
	
	public BigDecimal getWareneingangWertsumme(WareneingangDto wareneingangDto,
			TheClientDto theClientDto);
	public RueckgabeWEPMitReelIDDto wareneingangspositionMitReelIDBuchen(
			Integer wareneingangIId, Integer bestellpositionIId,
			BigDecimal nMenge, String datecode, String batch1,
			TheClientDto theClientDto);
	
}
