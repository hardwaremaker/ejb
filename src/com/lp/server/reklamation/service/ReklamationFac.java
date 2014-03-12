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
package com.lp.server.reklamation.service;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ReklamationFac

{
	public static final String REKLAMATIONART_KUNDE = "Kunde          ";
	public static final String REKLAMATIONART_LIEFERANT = "Lieferant      ";
	public static final String REKLAMATIONART_FERTIGUNG = "Fertigung      ";
	
	
	public static final Integer REKLAMATION_KUNDEUNTERART_FERTIGUNG = 1;
	public static final Integer REKLAMATION_KUNDEUNTERART_LIEFERANT = 2;
	

	public static final String FLR_REKLAMATION_REKLAMATIONART_C_NR = "reklamationart_c_nr";
	public static final String FLR_REKLAMATION_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_REKLAMATION_T_ERLEDIGT = "t_erledigt";
	public static final String FLR_REKLAMATION_X_ANALYSE = "x_analyse";
	public static final String FLR_REKLAMATION_C_GRUND = "c_grund";
	public static final String FLR_REKLAMATION_N_KOSTENMATERIAL = "n_kostenmaterial";
	public static final String FLR_REKLAMATION_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_REKLAMATION_KUNDE_I_ID = "kunde_i_id";
	public static final String FLR_REKLAMATION_LIEFERANT_I_ID = "lieferant_i_id";
	public static final String FLR_REKLAMATION_LOS_I_ID = "los_i_id";
	public static final String FLR_REKLAMATION_B_BERECHTIGT = "b_berechtigt";
	public static final String FLR_REKLAMATION_N_KOSTENARBEITSZEIT = "n_kostenarbeitszeit";
	public static final String FLR_REKLAMATION_FRLKUNDE = "flrkunde";
	public static final String FLR_REKLAMATION_FLRLOS = "flrlos";
	public static final String FLR_REKLAMATION_FLRMASCHINE = "flrmaschine";
	public static final String FLR_REKLAMATION_FLRLIEFERANT = "flrlieferant";
	public static final String FLR_REKLAMATION_FLRFEHLER = "flrfehler";
	public static final String FLR_REKLAMATION_FLRFEHLERANGABE = "flrfehlerangabe";
	public static final String FLR_REKLAMATION_FLRVERURSACHER = "flrverursacher";
	public static final String FLR_REKLAMATION_FLRARTIKEL = "flrartikel";
	public static final String FLR_REKLAMATION_STATUS_C_NR = "status_c_nr";
	public static final String FLR_REKLAMATION_I_KUNDEUNTERART = "i_kundeunterart";

	public static final String FLR_SCHWERE_I_PUNKTE = "i_punkte";

	public void createReklamationart(ReklamationartDto reklamationartDto)
			throws RemoteException;

	public void removeReklamationart(ReklamationartDto reklamationartDto)
			throws RemoteException;

	public void updateReklamationart(ReklamationartDto reklamationartDto)
			throws RemoteException;

	public ReklamationartDto reklamationartFindByPrimaryKey(String cNr)
			throws RemoteException;

	public Integer createReklamation(ReklamationDto reklamationDto,
			TheClientDto theClientDto);

	public void removeReklamation(ReklamationDto reklamationDto);

	public void updateReklamation(ReklamationDto reklamationDto,
			TheClientDto theClientDto);

	public ReklamationDto reklamationFindByPrimaryKey(Integer iId);

	public ReklamationDto reklamationFindByPrimaryKeyOhneExc(Integer iId);
	
	public ReklamationDto[] reklamationFindByKundeIIdMandantCNr(
			Integer iKundeIId, String mandantCNrI);

	public ReklamationDto[] reklamationFindByKundeIIdMandantCNrOhneExc(
			Integer iKundeIId, String mandantCNrI);

	public ReklamationDto[] reklamationFindByLieferantIIdMandantCNr(
			Integer iLieferantIId, String mandantCNrI);
	public ReklamationDto[] reklamationfindOffeneReklamationenEinesArtikels(
			Integer artikelIId);
	public ReklamationDto[] reklamationFindByLieferantIIdMandantCNrOhneExc(
			Integer iLieferantIId, String mandantCNrI);

	public Integer createFehlerangabe(FehlerangabeDto fehlerangabeDto,
			TheClientDto theClientDto);

	public void removeFehlerangabe(FehlerangabeDto fehlerangabeDto)
			throws RemoteException;

	public void reklamationErledigenOderAufheben(Integer reklamationIId,
			Integer behandlungIId, TheClientDto theClientDto)
			throws RemoteException;

	public Map<?, ?> getAllReklamationart() throws RemoteException;

	public void updateFehlerangabe(FehlerangabeDto fehlerangabeDto,
			TheClientDto theClientDto);

	public FehlerangabeDto fehlerangabeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public Integer createAufnahmeart(AufnahmeartDto aufnahmeartDto,TheClientDto theClientDto);

	public void updateAufnahmeart(AufnahmeartDto aufnahmeartDto,TheClientDto theClientDto);

	public AufnahmeartDto aufnahmeartFindByPrimaryKey(Integer iId,TheClientDto theClientDto);

	public Integer createFehler(FehlerDto fehlerDto,
			TheClientDto theClientDto);

	public void removeAufnahmeart(AufnahmeartDto dto);

	public void updateReklamationKommentar(ReklamationDto reklamationDto,
			TheClientDto theClientDto) throws RemoteException;

	public void removeFehler(FehlerDto fehlerDto);

	public void updateFehler(FehlerDto fehlerDto,
			TheClientDto theClientDto);

	public FehlerDto fehlerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public Integer createMassnahme(MassnahmeDto massnahmeDto,
			TheClientDto theClientDto);

	public void removeMassnahme(MassnahmeDto massnahmeDto);

	public void updateMassnahme(MassnahmeDto massnahmeDto,
			TheClientDto theClientDto);

	public MassnahmeDto massnahmeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public Integer createSchwere(SchwereDto schwereDto) throws RemoteException;

	public void removeSchwere(SchwereDto schwereDto) throws RemoteException;

	public void updateSchwere(SchwereDto schwereDto) throws RemoteException;

	public SchwereDto schwereFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public void removeReklamation(Integer iId) throws RemoteException;



	public ReklamationDto reklamationFindByMandantCNrCNr(String string,
			String string1) throws RemoteException;
	
	public ReklamationDto reklamationFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCNr);

	public ReklamationDto[] reklamationFindByAnsprechpartnerIId(
			Integer AnsprechpartnerIId) throws RemoteException;

	public Integer createBehandlung(BehandlungDto beurteilungDto)
			throws RemoteException;

	public void removeBehandlung(BehandlungDto beurteilungDto)
			throws RemoteException;

	public void updateBehandlung(BehandlungDto beurteilungDto)
			throws RemoteException;

	public BehandlungDto behandlungFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public Integer createTermintreue(TermintreueDto termintreueDto)
			throws RemoteException;

	public void removeTermintreue(TermintreueDto termintreueDto)
			throws RemoteException;

	public void updateTermintreue(TermintreueDto termintreueDto)
			throws RemoteException;

	public TermintreueDto termintreueFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public ReklamationDto[] reklamationFindByWareneingangIIdMandantCNr(
			Integer wareneingangIId, TheClientDto theClientDto)
			throws RemoteException;

	public TermintreueDto[] termintreueFindAll() throws RemoteException;

	public Integer createWirksamkeit(WirksamkeitDto wirksamkeitDto,
			TheClientDto theClientDto);

	public void removeWirksamkeit(WirksamkeitDto dto) throws RemoteException;

	public void updateWirksamkeit(WirksamkeitDto wirksamkeitDto,
			TheClientDto theClientDto);

	public WirksamkeitDto wirksamkeitFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public ReklamationbildDto reklamationbildFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public void updateReklamationbild(ReklamationbildDto reklamationbildDto)
			throws RemoteException;

	public void removeReklamationbild(ReklamationbildDto reklamationbildDto)
			throws RemoteException;

	public Integer createReklamationbild(ReklamationbildDto reklamationbildDto)
			throws RemoteException;

	public Integer getNextReklamationbild(Integer reklamationIId)
			throws RemoteException;
	public ReklamationbildDto[] reklamationbildFindByReklamationIId(
			Integer reklamationIId)throws RemoteException;

}
