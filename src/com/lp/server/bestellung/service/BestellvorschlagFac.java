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
package com.lp.server.bestellung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BestellvorschlagFac {

	public static final String LOCKME_BESTELLVORSCHLAG = "lockme_bestellvorschlag";

	// filterkriterien fuer bestellvorschlagueberleitung
	public static final String BES_NACH_BV_FUER_JEDEN_LF_UND_GLEICHE_TERMIN = "1";
	public static final String BES_NACH_BV_FUER_JEDEN_LF = "2";
	public static final String BES_NACH_BV_FUER_BESTIMMTEN_LF_UND_TERMIN = "3";
	public static final String BES_NACH_BV_FUER_BESTIMMTEN_LF = "4";
	public static final String BES_ABRUFE_ZU_RAHMEN = "5";

	public static final String FLR_BESTELLVORSCHLAG_FLRARTIKEL = "flrartikel";
	public static final String FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_BESTELLVORSCHLAG_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BESTELLVORSCHLAG_FLRLIEFERANT = "flrlieferant";
	public static final String FLR_BESTELLVORSCHLAG_LIEFERANT_I_ID = "lieferant_i_id";
	public static final String FLR_BESTELLVORSCHLAG_N_NETTOGESAMTPREISMINUSRABATTE = "n_nettogesamtpreisminusrabatte";
	public static final String FLR_BESTELLVORSCHLAG_N_ZUBESTELLENDEMENGE = "n_zubestellendemenge";
	public static final String FLR_BESTELLVORSCHLAG_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_BESTELLVORSCHLAG_I_BELEGARTID = "i_belegartid";
	public static final String FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_BESTELLVORSCHLAG_PROJEKT_I_ID = "projekt_i_id";

	  

	public static final int SORT_KRITERIUM_BESTELLVORSCHLAG = 0;

	public static final int IDX_KRIT_ARTIKEL_ID = 0;
	public static final int IDX_KRIT_LIEFERANT_ID = 1;

	public Integer createBestellvorschlag(
			BestellvorschlagDto bestellvorschlagDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellvorschlag(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(java.sql.Date dLiefertermin, TheClientDto theClientDto);
	
	public void removeBestellvorschlag(BestellvorschlagDto bestellvorschlagDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellvorschlags(
			BestellvorschlagDto[] bestellvorschlagDtos) throws EJBExceptionLP,
			RemoteException;

	public BestellvorschlagDto bestellvorschlagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNr(
			Integer iLieferantId, String cNrMandant) throws EJBExceptionLP,
			RemoteException;

	public void uebernimmLieferantAusLieferantOptimieren(
			Integer bestellvorschlagIId, Integer lieferantIIdNeu,
			TheClientDto theClientDto);
	
	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNrOhneExc(
			Integer iLieferantId, String cNrMandant) throws RemoteException;

	public long getAnzahlBestellvorschlagDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erstelleBestellvorschlag(Integer iVorlaufzeit,
			Integer iToleranz, Date dateFuerEintraegeOhneLiefertermin, ArrayList<Integer> arLosIId, ArrayList<Integer> arAuftragIId,
			boolean bMitNichtlagerbewirtschafteten, boolean bNurLospositionenBeruecksichtigen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erstelleBestellvorschlagAnhandEinesAngebots(Integer angebotIId,
			java.sql.Date dLiefertermin, TheClientDto theClientDto);
	
	public Boolean createBESausBVfuerAlleLieferantenMitGleichenTermin(
			FilterKriterium[] fk, SortierKriterium[] ski, TheClientDto theClientDto,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen) throws EJBExceptionLP, RemoteException;

	public boolean mindestbestellwertErreicht(Integer iLieferantId,
			TheClientDto theClientDto);
	
	public Boolean createBESausBVjeLieferant(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen)
			throws EJBExceptionLP, RemoteException;

	public Boolean createBESausBVfuerBestimmtenLieferantUndTermin(
			FilterKriterium[] fk, SortierKriterium[] ski, TheClientDto theClientDto,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen) throws EJBExceptionLP, RemoteException;

	public Boolean createBESausBVfueBestimmtenLieferant(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen)
			throws EJBExceptionLP, RemoteException;

	public void removeLockDesBestellvorschlagesWennIchIhnSperre(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBearbeitenDesBestellvorschlagsErlaubt(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void bestellvorschlagInLiefergruppenanfragenUmwandeln(String projekt,TheClientDto theClientDto);
	
	public void verdichteBestellvorschlag(Long iVerdichtungszeitraum,
			boolean bMindestbestellmengenBeruecksichtigt,boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void loescheSpaeterWiederbeschaffbarePositionen(Date tNaechsterBV,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void loescheBestellvorlaegeEinesMandaten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void befuellenDerBestellungUndBestellposition(
			BestellvorschlagDto[] aBestellvorschlagDto,
			Integer kostenstelleIId, TheClientDto theClientDto) throws EJBExceptionLP;
	
	public void befuellenDerBestellungausBVfuerBestimmtenLieferant(
			BestellvorschlagDto[] aBestellvorschlagDto,
			Integer kostenstelleIId, TheClientDto theClientDto) throws EJBExceptionLP;
	
	public void loescheBestellvorschlaegeAbTermin(Date dTermin, TheClientDto theClientDto);
	
	public BestellungDto[] createBESausBVzuRahmen(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto)
		throws EJBExceptionLP, RemoteException;
	
	public BestellungDto[] erstelleAbrufbestellungenAusBV(BestellvorschlagDto[] bestellvorschlagDto, TheClientDto theClientDto)
		throws EJBExceptionLP, RemoteException;
	public void bestellvorschlagDtoErzeugen(String belegartCNr,
			String mandantCNr, Integer artikelIId, Integer belegIId,
			Integer belegpositionIId, java.sql.Timestamp tTermin,
			BigDecimal nMenge, Integer projektIId, TheClientDto theClientDto);	
	public Map getAllLieferantenDesBestellvorschlages(TheClientDto theClientDto);
	public void artikellieferantZuruecksetzen(ArrayList<Integer> bestellvorschlagIIds, TheClientDto theClientDto);
}
