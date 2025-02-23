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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
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
	public static final String FLR_BESTELLVORSCHLAG_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_BESTELLVORSCHLAG_PARTNER_I_ID_STANDORT = "partner_i_id_standort";
	public static final String FLR_BESTELLVORSCHLAG_T_BEARBEITET = "t_bearbeitet";

	public static final int SORT_KRITERIUM_BESTELLVORSCHLAG = 0;

	public static final int IDX_KRIT_ARTIKEL_ID = 0;
	public static final int IDX_KRIT_LIEFERANT_ID = 1;

	public Integer createBestellvorschlag(BestellvorschlagDto bestellvorschlagDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellvorschlag(Integer iId) throws EJBExceptionLP, RemoteException;

	public void erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(java.sql.Date dLiefertermin,
			boolean vormerklisteLoeschen, TheClientDto theClientDto, Integer partnerIIdStandort,boolean bBestellvorschlagLoechen);

	public void removeBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellvorschlags(BestellvorschlagDto[] bestellvorschlagDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BestellvorschlagDto bestellvorschlagFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNr(Integer iLieferantId, String cNrMandant)
			throws EJBExceptionLP, RemoteException;

	public void uebernimmLieferantAusLieferantOptimieren(Integer bestellvorschlagIId, Integer lieferantIIdNeu,
			TheClientDto theClientDto);

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNrOhneExc(Integer iLieferantId,
			String cNrMandant) throws RemoteException;

	public long getAnzahlBestellvorschlagDesMandanten(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erstelleBestellvorschlag(Integer iVorlaufzeit, Integer iToleranz,
			Date dateFuerEintraegeOhneLiefertermin, ArrayList<Integer> arLosIId, ArrayList<Integer> arAuftragIId,
			boolean bMitNichtlagerbewirtschafteten, boolean bNurLospositionenBeruecksichtigen,
			boolean vormerklisteLoeschen, boolean bBestellvorschlagLoechen,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, TheClientDto theClientDto, Integer partnerIIdStandort, boolean bArtikelNurAufAuftraegeIgnorieren, boolean bExakterAuftragsbezug)
			throws EJBExceptionLP, RemoteException;

	public void erstelleBestellvorschlagAnhandEinesAngebots(Integer angebotIId, java.sql.Date dLiefertermin,
			TheClientDto theClientDto);

	public void erstelleBestellvorschlagAnhandEkag(Integer einkaufsangebotIId, int menge,  Timestamp tGeplanterFertigungstermin,
			Integer vorlaufzeit, TheClientDto theClientDto);

	public RueckgabeUeberleitungDto createBESausBVfuerAlleLieferantenMitGleichenTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen, Integer standortIId ,boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel)
			throws EJBExceptionLP, RemoteException;

	public boolean mindestbestellwertErreicht(Integer iLieferantId, TheClientDto theClientDto);

	public RueckgabeUeberleitungDto createBESausBVjeLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId ,boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) throws EJBExceptionLP, RemoteException;

	public RueckgabeUeberleitungDto createBESausBVfuerBestimmtenLieferantUndTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen, Integer standortIId ,boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel)
			throws EJBExceptionLP, RemoteException;

	public RueckgabeUeberleitungDto createBESausBVfueBestimmtenLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId ,boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) throws EJBExceptionLP, RemoteException;

	public void removeLockDesBestellvorschlagesWennIchIhnSperre(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBearbeitenDesBestellvorschlagsErlaubt(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void bestellvorschlagInLiefergruppenanfragenUmwandeln(String projekt, TheClientDto theClientDto);

	public void verdichteBestellvorschlag(Long iVerdichtungszeitraum, boolean bMindestbestellmengenBeruecksichtigt,
			boolean bBeruecksichtigeProjektklammer,boolean bPreiseaktualisieren, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void loescheSpaeterWiederbeschaffbarePositionen(Date tNaechsterBV, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void loescheBestellvorlaegeEinesMandaten(boolean vormerklisteLoeschen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public java.util.TreeMap befuellenDerBestellungUndBestellposition(BestellvorschlagDto[] aBestellvorschlagDto,
			Integer kostenstelleIId, Integer partnerIIdStandort, boolean bRahmenbestellungErzeugen, TheClientDto theClientDto) throws EJBExceptionLP;

	public java.util.TreeMap befuellenDerBestellungausBVfuerBestimmtenLieferant(
			BestellvorschlagDto[] aBestellvorschlagDto, Integer kostenstelleIId, Integer partnerIIdStandort,boolean bRahmenbestellungErzeugen,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void loescheBestellvorschlaegeAbTermin(Date dTermin, TheClientDto theClientDto);

	public RueckgabeUeberleitungDto createBESausBVzuRahmen(FilterKriterium[] fk, SortierKriterium[] ski,
			Integer standortIId, boolean gemeinsameArtikelBestellen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RueckgabeUeberleitungDto erstelleAbrufbestellungenAusBV(BestellvorschlagDto[] bestellvorschlagDto,
			Integer partnerIIdStandort, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void bestellvorschlagDtoErzeugen(String belegartCNr, String mandantCNr, Integer artikelIId, Integer belegIId,
			Integer belegpositionIId, java.sql.Timestamp tTermin, BigDecimal nMenge, Integer projektIId,
			String xTextinhalt, Integer lieferantIId, BigDecimal nEinkaufspreis,Double fLagermindest, Integer partnerIIdStandort,
			boolean bZentralerArtikelstamm, TheClientDto theClientDto);

	public Map getAllLieferantenDesBestellvorschlages(TheClientDto theClientDto);

	public void artikellieferantZuruecksetzen(ArrayList<Integer> bestellvorschlagIIds, TheClientDto theClientDto);

	public void verdichteEinenArtikel(Long lVerdichtungszeitraumparam, boolean bBeruecksichtigeProjektklammer,
			TheClientDto theClientDto, boolean lagerminJeLager, ArtikelDto artikelDto);

	public void verdichteBestellvorschlagNachDatum(Long lVerdichtungszeitraumparam,
			boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto);

	public void verdichteBestellvorschlagNachMindesbestellmengen(boolean bBeruecksichtigeProjektklammer,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public String getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(TheClientDto theClientDto);

	public void gemeinsameArtikelLoeschen(TheClientDto theClientDto);

	List<BestellvorschlagDto> bestellvorschlagFindByArtikelIdVormerkungMandantCNr(ArtikelId artikelId,
			String mandantCNr);

	Integer setupCreateBestellvorschlag(CreateBestellvorschlagDto createDto, TheClientDto theClientDto);

	BestellvorschlagDto bestellvorschlagFindByPrimaryKeyOhneExc(Integer iId);

	public String pruefeUndImportiereBestellvorschlagXLS(byte[] xlsDatei, java.sql.Timestamp tLiefertermin,
			boolean bVorhandenenBestellvorschlagLoeschen, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto);
	public void termineAnhandLiefertagVerschieben(TheClientDto theClientDto);

	public void toggleBearbeitet(Integer bestellvorschlagIId, TheClientDto theClientDto);
	
	public void aktualisierePreiseWbz(TheClientDto theClientDto);
	
}