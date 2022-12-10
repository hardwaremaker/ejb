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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Remote;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface InternebestellungFac {
	public static final String KRIT_ARTIKEL_I_ID = "artikelIId";
	public static final String KRIT_INTERNEBESTELLUNG_BERUECKSICHTIGEN = "internebestellungberuecksichtigen";
	public static final String KRIT_INTERNEBESTELLUNG = "internebestellung";
	public static final String KRIT_PARTNER_I_ID_STANDORT = "partner_i_id_standort";
	public static final String KRIT_MIT_RAHMEN = "mit_rahmen";

	public static final int UEBERLEITEN_MIT_AUFTRAGSBEZUG = 0;
	public static final int UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE = 1;
	public static final int UEBERLEITEN_OHNE_AUFTRAGSBEZUG = 2;

	public static final String LOCKME_INTERNEBESTELLUNG_TP = "lockme_internebestellung_tp";

	public InternebestellungDto createInternebestellung(InternebestellungDto internebestellungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeInternebestellung(InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public InternebestellungDto updateInternebestellung(InternebestellungDto internebestellungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public InternebestellungDto internebestellungFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public void erzeugeInterneBestellung(Boolean bVorhandeneLoeschen, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			Boolean bVerdichten, Integer iVerdichtungsTage, boolean bInterneBestellung, ArrayList<Integer> losIIds,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, ArrayList<Integer> arAuftragIId,
			Integer fertigungsgruppeIId_Entfernen, boolean bExakterAuftragsbezug, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> erzeugeInterneBestellung(Integer iVorlaufzeit, Integer iVorlaufzeitUnterlose,
			Integer iToleranz, java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			boolean bInterneBestellungBeruecksichtigen, Integer stuecklisteIId,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau, boolean bInterneBestellung,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, boolean bTermineBelassen,
			HashSet hmReservierungenVorhanden, boolean bUnterlos, boolean bExakterAuftragsbezug,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<?> pruefeOffeneRahmenmengen(TheClientDto theClientDto) throws RemoteException;

	public ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, Integer partnerIIdStandort,
			HashSet hmReservierungenVorhanden, boolean bExakterAuftragsbezug) throws EJBExceptionLP, RemoteException;

	public ArrayList<MaterialbedarfDto> berechneBedarfe(Integer artikelIId, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, Integer partnerIIdStandort,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP, RemoteException;

	public void removeInternebestellungEinesMandanten(boolean bNurHilfsstuecklisten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(Integer iArtikelId, TheClientDto theClientDto,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean mitAnderenMandanten,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, HashSet hmReservierungenVorhanden)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(Integer iArtikelId,
			boolean bInternebestellungMiteinbeziehen, boolean bBestellvorschlagMiteinbeziehen,
			TheClientDto theClientDto, Integer partnerIIdStandort, boolean bMitRahmen, boolean mitAnderenMandanten,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, HashSet hmReservierungenVorhanden)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(ArtikelDto artikelDto,
			boolean bTermineVorHeuteAufHeute, TheClientDto theClientDto, Integer partnerIIdStandort, boolean bMitRahmen,
			boolean mitAnderenMandanten, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP, RemoteException;

	public Integer interneBestellungUeberleiten(Integer interneBestellungIId, Integer partnerIIdStandort,
			int iTypAuftragsbezug, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Set<Integer> getInternebestellungIIdsEinesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void verdichteInterneBestellung(Integer iVerdichtungsTage, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getFiktivenLagerstandZuZeitpunkt(ArtikelDto artikelDto, TheClientDto theClientDto,
			Timestamp tLagerstandsdatum, Integer partnerIIdStandort);

	public void verdichteInterneBestellung(HashSet<Integer> stuecklisteIIds, TheClientDto theClientDto);

	public void ueberproduktionZuruecknehmen(TheClientDto theClientDto);

	public BigDecimal getAnfangslagerstand(ArtikelDto artikelDto, boolean bInterneBestellung, TheClientDto theClientDto,
			Integer partnerIIdStandort) throws RemoteException;

	public void pruefeBearbeitenDerInternenBestellungErlaubt(TheClientDto theClientDto);

	public void removeLockDerInternenBestellungWennIchIhnSperre(TheClientDto theClientDto);

	public void loescheAngelegtesOderStorniertesLos(Integer losIId, TheClientDto theClientDto);

	public void verdichteInterneBestellungEinerStuecklisteEinesMandanten(Integer stuecklisteIId,
			Integer iVerdichtungsTage, TheClientDto theClientDto);

	public void beginnEndeAllerEintraegeVerschieben(Timestamp tBeginn, Timestamp tEnde,
			TheClientDto theClientDto);
	
	public void loescheAngelegteUndStornierteLoseAufEinmal(Integer fertigungsgruppeIId_Entfernen,
			TheClientDto theClientDto);

	public void loescheAngelegteUndStornierteLoseAufEinmal(String queryStringWhereLOS_I_IDs,
			TheClientDto theClientDto);
	
}
