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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ejb.Remote;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LosId;
import com.lp.server.util.LossollmaterialId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.KeyValue;

@Remote
public interface FertigungFac {
	public final static int NACHKOMMASTELLEN_ARBEITSPLAN = 4;

	public static final String STATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String STATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String STATUS_AUSGEGEBEN = LocaleFac.STATUS_AUSGEGEBEN;
	public static final String STATUS_IN_PRODUKTION = LocaleFac.STATUS_IN_PRODUKTION;
	public static final String STATUS_TEILERLEDIGT = LocaleFac.STATUS_TEILERLEDIGT;
	public static final String STATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String STATUS_GESTOPPT = LocaleFac.STATUS_GESTOPPT;

	public static final String ZUSATZSTATUS_P1 = "P1";
	public static final String ZUSATZSTATUS_ABLIEFERPREISE = LocaleFac.STATUS_ABLIEFERPREISE;

	public static final String LOSART_IDENT = "Ident";
	public static final String LOSART_MATERIALLISTE = "Materialliste";

	public static final String FLR_LOSSTATUS_STATUS_C_NR = "status_c_nr";

	public static final String FLR_LOSKLASSE_I_ID = "i_id";
	public static final String FLR_LOSKLASSE_C_NR = "c_nr";

	public static final String FLR_LOS_I_ID = "i_id";
	public static final String FLR_LOS_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_LOS_C_NR = "c_nr";
	public static final String FLR_LOS_STATUS_C_NR = "status_c_nr";
	public static final String FLR_LOS_C_PROJEKT = "c_projekt";
	public static final String FLR_LOS_STUECKLISTE_I_ID = "stueckliste_i_id";
	public static final String FLR_LOS_PERSONAL_I_ID_TECHNIKER = "personal_i_id_techniker";
	public static final String FLR_LOS_T_PRODUKTIONSENDE = "t_produktionsende";
	public static final String FLR_LOS_T_PRODUKTIONSBEGINN = "t_produktionsbeginn";
	public static final String FLR_LOS_T_AUSGABE = "t_ausgabe";
	public static final String FLR_LOS_T_ERLEDIGT = "t_erledigt";
	public static final String FLR_LOS_T_PRODUKTIONSSTOP = "t_produktionsstop";
	public static final String FLR_LOS_N_LOSGROESSE = "n_losgroesse";
	public static final String FLR_LOS_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_LOS_TECHNIKERSET = "technikerset";
	public static final String FLR_LOS_F_BEWERTUNG = "f_bewertung";

	public static final String FLR_LOSREPORT_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_LOSREPORT_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_LOSREPORT_FLRAUFTRAGPOSITION = "flrauftragposition";
	public static final String FLR_LOSREPORT_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_LOSREPORT_T_ANLEGEN = "t_anlegen";
	public static final String FLR_LOSREPORT_T_MANUELLERLEDIGT = "t_manuellerledigt";
	public static final String FLR_LOSREPORT_FERTIGUGN = "t_manuellerledigt";
	public static final String FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID = "flrfertigungsgruppe.i_id";
	public static final String FLR_LOSREPORT_FLRFERTIGUNGSGRUPPE = "flrfertigungsgruppe";
	public static final String FLR_LOSREPORT_FLRWIEDERHOLENDELOSE = "flrwiederholendelose";
	public static final String FLR_LOSREPORT_PARTNER_I_ID_FERTIGUNGSORT = "partner_i_id_fertigungsort";
	public static final String FLR_LOSREPORT_T_NACHTRAEGLICH_GEOEFFNET = "t_nachtraeglich_geoeffnet";

	public static final String FLR_WIEDERHOLENDELOSE_T_TERMIN = "t_termin";
	public static final String FLR_WIEDERHOLENDELOSE_C_PROJEKT = "c_projekt";
	public static final String FLR_WIEDERHOLENDELOSE_AUFTRAGWIEDERHOLUNGSINTERVALL_C_NR = "auftragwiederholungsintervall_c_nr";
	public static final String FLR_WIEDERHOLENDELOSE_STUECKLISTE_I_ID = "stueckliste_i_id";
	public static final String FLR_WIEDERHOLENDELOSE_N_LOSGROESSE = "n_losgroesse";
	public static final String FLR_WIEDERHOLENDELOSE_B_VERSTECKT = "b_versteckt";
	public static final String FLR_WIEDERHOLENDELOSE_I_TAGEVOREILEND = "i_tagevoreilend";
	public static final String FLR_WIEDERHOLENDELOSE_FLRSTUECKLISTE = "flrstueckliste";

	public static final String FLR_LOSSOLLMATERIAL_I_ID = "i_id";
	public static final String FLR_LOSSOLLMATERIAL_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSSOLLMATERIAL_N_MENGE = "n_menge";
	public static final String FLR_LOSSOLLMATERIAL_N_SOLLPREIS = "n_sollpreis";
	public static final String FLR_LOSSOLLMATERIAL_B_NACHTRAEGLICH = "b_nachtraeglich";
	public static final String FLR_LOSSOLLMATERIAL_MONTAGEART_I_ID = "montageart_i_id";
	public static final String FLR_LOSSOLLMATERIAL_I_LFDNUMMER = "i_lfdnummer";
	public static final String FLR_LOSSOLLMATERIAL_FLRARTIKEL = "flrartikel";
	public static final String FLR_LOSSOLLMATERIAL_FLRLOS = "flrlos";

	public static final String FLR_LOSGUTSCHLECHT_FLRLOS = "flrlos";
	public static final String FLR_LOSGUTSCHLECHT_FLRZEITDATEN = "flrzeitdaten";
	public static final String FLR_LOSGUTSCHLECHT_N_GUT = "n_gut";
	public static final String FLR_LOSGUTSCHLECHT_N_SCHLECHT = "n_schlecht";
	public static final String FLR_LOSGUTSCHLECHT_N_INARBEIT = "n_inarbeit";
	public static final String FLR_LOSGUTSCHLECHT_LOSSOLLARBEITSPLAN_I_ID = "lossollarbeitsplan_i_id";

	public static final String FLR_LOSZUSATZSTATUS_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSZUSATZSTATUS_ZUSATZSTATUS_I_ID = "zusatzstatus_i_id";
	public static final String FLR_LOSZUSATZSTATUS_FLRLOS = "flrlos";
	public static final String FLR_LOSZUSATZSTATUS_FLRZUSATZSTATUS = "flrzusatzstatus";

	public static final String FLR_LOSSOLLARBEITSPLAN_I_ID = "i_id";
	public static final String FLR_LOSSOLLARBEITSPLAN_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSSOLLARBEITSPLAN_MASCHINE_I_ID = "maschine_i_id";
	public static final String FLR_LOSSOLLARBEITSPLAN_L_RUESTZEIT = "l_ruestzeit";
	public static final String FLR_LOSSOLLARBEITSPLAN_L_STUECKZEIT = "l_stueckzeit";
	public static final String FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT = "n_gesamtzeit";
	public static final String FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG = "i_unterarbeitsgang";
	public static final String FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE = "i_maschinenversatztage";
	public static final String FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER = "i_arbeitsgangsnummer";
	public static final String FLR_LOSSOLLARBEITSPLAN_B_NACHTRAEGLICH = "b_nachtraeglich";
	public static final String FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL = "flrartikel";
	public static final String FLR_LOSSOLLARBEITSPLAN_FLRLOS = "flrlos";
	public static final String FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE = "flrmaschine";
	public static final String FLR_LOSSOLLARBEITSPLAN_B_FERTIG = "b_fertig";
	public static final String FLR_LOSSOLLARBEITSPLAN_F_FORTSCHRITT = "f_fortschritt";

	public static final String FLR_LOSLAGERENTNAHME_I_ID = "i_id";
	public static final String FLR_LOSLAGERENTNAHME_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSLAGERENTNAHME_I_SORT = "i_sort";
	public static final String FLR_LOSLAGERENTNAHME_FLRLAGER = "flrlager";

	public static final String FLR_LOSISTMATERIAL_I_ID = "i_id";
	public static final String FLR_LOSISTMATERIAL_LOSSOLLMATERIAL_I_ID = "lossollmaterial_i_id";
	public static final String FLR_LOSISTMATERIAL_LAGER_I_ID = "lager_i_id";
	public static final String FLR_LOSISTMATERIAL_N_MENGE = "n_menge";
	public static final String FLR_LOSISTMATERIAL_B_ABGANG = "b_abgang";
	public static final String FLR_LOSISTMATERIAL_FLRLOSSOLLMATERIAL = "flrlossollmaterial";
	public static final String FLR_LOSISTMATERIAL_FLRLAGER = "flrlager";

	public static final String FLR_LOSLOSKLASSE_I_ID = "i_id";
	public static final String FLR_LOSLOSKLASSE_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSLOSKLASSE_FLRLOSKLASSE = "flrlosklasse";

	public static final String FLR_LOSABLIEFERUNG_I_ID = "i_id";
	public static final String FLR_LOSABLIEFERUNG_LOS_I_ID = "los_i_id";
	public static final String FLR_LOSABLIEFERUNG_N_MENGE = "n_menge";
	public static final String FLR_LOSABLIEFERUNG_N_GESTEHUNGSPREIS = "n_gestehungspreis";
	public static final String FLR_LOSABLIEFERUNG_N_MATERIALWERT = "n_materialwert";
	public static final String FLR_LOSABLIEFERUNG_N_ARBEITSZEITWERT = "n_arbeitszeitwert";
	public static final String FLR_LOSABLIEFERUNG_T_AENDERN = "t_aendern";
	public static final String FLR_LOSABLIEFERUNG_FLRLOS = "flrlos";

	public static final String FLR_LOSTECHNIKER_FLRLOS = "flrlos";
	public static final String FLR_LOSTECHNIKER_FLRPERSONAL = "flrpersonal";

	public static final String FLR_INTERNE_BESTELLUNG_I_ID = "i_id";
	public static final String FLR_INTERNE_BESTELLUNG_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_INTERNE_BESTELLUNG_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_INTERNE_BESTELLUNG_I_BELEGIID = "i_belegiid";
	public static final String FLR_INTERNE_BESTELLUNG_I_BELEGPOSITIONIID = "i_belegpositioniid";
	public static final String FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID = "stueckliste_i_id";
	public static final String FLR_INTERNE_BESTELLUNG_N_MENGE = "n_menge";
	public static final String FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE = "flrstueckliste";

	public static final int IDX_KRIT_AUSWERTUNG = 0;
	public static final int IDX_KRIT_LOS = 1;

	public static final String KRIT_IDENT = "Ident";
	public static final String KRIT_PERSONAL = "Personal";
	public static final String KRIT_LOS_I_ID = "Los";
	public static final int ANZAHL_KRITERIEN_LOSZEITEN = 2;

	public static final int KAPAZITAETSVORSCHAU_NACH_WOCHEN = 0;
	public static final int KAPAZITAETSVORSCHAU_NACH_TAGEN = 1;

	public static final String ZUPRODUZIEREN_TEXTSUCHE = "TEXTSUCHE";

	public LosDto createLos(LosDto losDto, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LosDto createLos(LosDto losDto, boolean bErsatztypenAuslassen, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public LosistmaterialDto createLosistmaterial(LosistmaterialDto losistmaterialDto, String sSerienChargennummer,
			TheClientDto theClientDto);

	public LosDto createLoseAusAuftrag(LosDto losDto, Integer auftragIId, TheClientDto theClientDto)
			throws RemoteException;

	public void removeLos(LosDto losDto, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLosKommentar(Integer losIId, String cKommentar) throws RemoteException;

	public LosDto updateLos(LosDto losDto, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LosDto updateLos(LosDto losDto, boolean bErsatztypenAuslassen, TheClientDto theClientDto);

	public void updateBewertungKommentarImLos(LosDto losDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LosDto losFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public LosDto losFindByPrimaryKeyOhneExc(Integer iId);

	public LosDto[] losFindByAuftragIId(Integer auftragIId);

	public LosDto[] losFindByAuftragIIdMitStornierten(Integer auftragIId);

	public LossollmaterialDto createLossollmaterialWithMaxISort(LossollmaterialDto lossollmaterialDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LossollmaterialDto createLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public LossollmaterialDto updateLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public LossollarbeitsplanDto createLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public BigDecimal getErledigterMaterialwertNEU(LosDto losDto, TheClientDto theClientDto) throws RemoteException;

	public void removeLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public LossollarbeitsplanDto updateLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LossollarbeitsplanDto updateLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			boolean verschiebeNachfolger, TheClientDto theClientDto) throws EJBExceptionLP;

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId);

	public AblieferungByAuftragResultDto perAuftragsnummerLoseAbliefern(Integer auftragIId, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getMengeDerJuengstenLosablieferung(Integer losIId, TheClientDto theClientDto)
			throws RemoteException;

	public String verknuepfungZuBestellpositionUndArbeitsplanDarstellen(Integer lossollmaterialIId,
			TheClientDto theClientDto);

	public LoslagerentnahmeDto createLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, Timestamp tBelegdatum,
			boolean bKommissionierterminal, TheClientDto theClientDto);

	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public LoslagerentnahmeDto updateLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LoslagerentnahmeDto loslagerentnahmeFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId) throws RemoteException, EJBExceptionLP;

	public void aktualisiereSollMaterialAusStueckliste(Integer losIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void alleLoseEinerStuecklisteNachkalkulieren(String artikelnummer, String tsAbErledigtdatum,
			TheClientDto theClientDto);

	public void aendereLosgroesse(Integer losIId, Integer neueLosgroesse, boolean bUeberzaehligesMaterialZurueckgeben,
			TheClientDto theClientDto) throws RemoteException;

	public void aktualisiereSollArbeitsplanAusStueckliste(Integer losIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public EJBExceptionLP gebeLosAus(Integer losIId, boolean bHandausgabe, boolean throwExceptionWhenCreate,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos)
			throws EJBExceptionLP, RemoteException;

	public RueckgabeMehrereLoseAusgeben gebeMehrereLoseAus(Integer fertigungsgruppeIId,
			boolean throwExceptionWhenCreate, boolean bAufAktualisierungPruefen, TheClientDto theClientDto);

	public ArrayList<LosDto> getLoseInFertigung(Integer artikelIId, TheClientDto theClientDto);

	public void bucheMaterialAufLos(LosDto losDto, BigDecimal menge, boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen, boolean bUnterstuecklistenAbbuchen,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) throws RemoteException;

	public void bucheMaterialAufLos(LosDto losDto, LossollmaterialDto[] sollmat, BigDecimal menge, boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen, boolean bUnterstuecklistenAbbuchen,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) throws RemoteException;

	public void bucheTOPSArtikelAufHauptLager(Integer losIId, TheClientDto theClientDto,
			BigDecimal zuzubuchendeSatzgroessen) throws RemoteException;

	public void bucheFehlmengenNach(LosId losId, LossollmaterialDto[] sollmat, boolean bKommissionierterminal,
			TheClientDto theClientDto) throws RemoteException;

	public Integer getNextZusatzstatus(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void gebeLosAusRueckgaengig(Integer losIId,
			boolean bSollmengenBeiNachtraeglichenMaterialentnahmenAktualisieren, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragNachkalkulationDto getWerteAusUnterlosen(LosDto losDto,
			HashMap<Integer, LosInfosFuerWerteAusUnterlosen> hmLosInfos, AuftragNachkalkulationDto abNachkalkulationDto,
			TheClientDto theClientDto);

	public void storniereLos(Integer losIId, boolean bAuftragspositionsbezugEntfernen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void storniereLosRueckgaengig(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void manuellErledigenRueckgaengig(Integer losIId, boolean bAufgrundChargenWegwerfen,
			boolean negativeSollmengenZurueckbuchen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LosistmaterialDto losistmaterialFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public LosistmaterialDto losistmaterialFindByPrimaryKeyOhneExc(Integer iId) throws EJBExceptionLP, RemoteException;

	public LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(Integer lossollmaterialIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getAusgegebeneMenge(Integer lossollmaterialIId, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal wievileTOPSArtikelWurdenBereitsZugebucht(Integer losIId, TheClientDto theClientDto)
			throws RemoteException;

	public TreeMap<String, Object[]> aktualisiereLoseAusStueckliste(Integer stuecklisteIId,
			boolean mitAusgegebenUndInProduktion, TheClientDto theClientDto);

	public String getArtikelhinweiseAllerLossollpositionen(Integer losIId, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getAusgegebeneMengePreis(Integer lossollmaterialIId, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void vertauscheWiederholendelose(Integer iIdMontageart1I, Integer iIdMontageart2I) throws RemoteException;

	public LoslosklasseDto createLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer holeTageslos(Integer lagerIId, String mandantCNr, int iStandarddurchlaufzeit,
			boolean bSofortverbrauch, TheClientDto theClientDto) throws RemoteException;

	public LoslosklasseDto updateLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LoslosklasseDto loslosklasseFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public int wiederholendeLoseAnlegen(TheClientDto theClientDto) throws RemoteException;

	public void terminVerschieben(Integer losIId, Timestamp tsBeginnNeu, Timestamp tsEndeNeu,
			TheClientDto theClientDto);

	public LosablieferungResultDto createLosablieferung(LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt) throws EJBExceptionLP, RemoteException;

	public void removeLosablieferung(Object[] loablieferungIIds, boolean bMaterialZurueckbuchen,
			TheClientDto theClientDto);

	public LosablieferungDto updateLosablieferung(LosablieferungDto losablieferungDto, boolean bMaterialZurueckbuchen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getErledigteMenge(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void stoppeProduktion(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void stoppeProduktionRueckgaengig(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer losIId)
			throws EJBExceptionLP, RemoteException;

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(Integer losIId,
			Integer artikelIIdTaetigkeit) throws EJBExceptionLP, RemoteException;

	public LossollmaterialDto[] lossollmaterialFindByLosIId(Integer losIId) throws EJBExceptionLP, RemoteException;

	public LossollmaterialDto lossollmaterialFindByPrimaryKeyOhneExc(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto, List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto, List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge, boolean bKommissionierterminal, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LosDto losFindByCNrMandantCNr(String cNr, String mandantCNr) throws EJBExceptionLP, RemoteException;

	public LosDto losFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr);

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal bdZuErledigendeMenge,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal bdZuErledigendeMenge,
			boolean bTerminal, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLosProduktionsinformation(Integer losIId, String cProduktionsinformation);

	public LossollmaterialDto[] lossollmaterialFindByLosIIdOrderByISort(Integer losIId)
			throws EJBExceptionLP, RemoteException;

	public LossollmaterialDto getErstesLossollmaterial(Integer losIId) throws EJBExceptionLP, RemoteException;

	public LossollmaterialDto[] lossollmaterialFindByLossollmaterialIIdOriginal(Integer lossollmaterialIId);

	public Integer getNextArbeitsgang(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLosistmaterialGestehungspreis(Integer losistmaterialIId, BigDecimal bdGestehungspreis,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LosablieferungDto losablieferungFindByPrimaryKey(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer iId);

	public LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer defaultArbeitszeitartikelErstellen(LosDto losDto, TheClientDto theClientDto);

	public LosablieferungDto[] losablieferungFindByLosIId(Integer losIId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNr(Integer partnerIId, String cNrMandant)
			throws EJBExceptionLP, RemoteException;

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNrOhneExc(Integer partnerIId, String cNrMandant)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getErledigteArbeitszeitEinerLosablieferung(Integer losablieferungIId, boolean bWertOderZeit,
			boolean bMitMaschinenZeit, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KapazitaetsvorschauDto getKapazitaetsvorschau(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public List<SeriennrChargennrMitMengeDto> getOffenenSeriennummernBeiGeraeteseriennummer(Integer losIId,
			TheClientDto theClientDto);

	public EJBExceptionLP manuellErledigen(Integer losIId, boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeLosablieferungenAufNeuBerechnen(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLosZuordnung(LosDto losDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> getAllLosIIdDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void aktualisiereNachtraeglichPreiseAllerLosablieferungen(Integer losIId, TheClientDto theClientDto,
			boolean bNurLetztenMaterialwertNeuBerechnen) throws EJBExceptionLP, RemoteException;

	public Integer createWiederholendelose(WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeWiederholendelose(WiederholendeloseDto wiederholendeloseDto) throws RemoteException;

	public void updateWiederholendelose(WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws RemoteException;

	public WiederholendeloseDto wiederholendeloseFindByPrimaryKey(Integer iId) throws RemoteException;

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNr(Integer iPartnerId, String iMandantCNr)
			throws EJBExceptionLP, RemoteException;

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNrOhneExc(Integer iPartnerId,
			String iMandantCNr) throws RemoteException;

	public HashMap<Integer, StuecklistepositionDto> holeAlleLossollmaterialFuerStuecklistenAktualisierung(
			Integer stuecklisteIId, BigDecimal bdLosgroesse, int iEbene,
			HashMap<Integer, StuecklistepositionDto> hmPositionen, TheClientDto theClientDto);

	public void vertauscheLoslagerentnahme(Integer iiDLagerentnahme1, Integer iIdLagerentnahme2) throws RemoteException;

	public Integer createZusatzstatus(ZusatzstatusDto zusatzstatusDto, TheClientDto theClientDto)
			throws RemoteException;

	public String erfuellungsgradBerechnen(Integer artikelIId, Integer losIId, TheClientDto theClientDto);

	public void removeZusatzstatus(ZusatzstatusDto zusatzstatusDto) throws RemoteException;

	public void updateZusatzstatus(ZusatzstatusDto zusatzstatusDto, TheClientDto theClientDto) throws RemoteException;

	public ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId) throws RemoteException;

	public void bucheLosAblieferungAufLager(LosablieferungDto losablieferungDto, LosDto losDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public ArrayList<String> getOffeneGeraeteSnrEinerSollPosition(Integer lossollmaterialIId,
			TheClientDto theClientDto);

	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBez(String mandantCNr, String cBez) throws RemoteException;

	public LosgutschlechtDto[] losgutschlechtFindByZeitdatenIId(Integer zeitdatenIId);

	public Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto) throws RemoteException;

	public void updateLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto, TheClientDto theClientDto)
			throws RemoteException;

	public LoszusatzstatusDto loszusatzstatusFindByPrimaryKey(Integer iId) throws RemoteException;

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIId(Integer losIId, Integer zusatzstatusIId)
			throws RemoteException;

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(Integer losIId, Integer zusatzstatusIId)
			throws RemoteException;

	public LoszusatzstatusDto[] loszusatzstatusFindByLosIIdOhneExc(Integer losIId) throws RemoteException;

	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftrag(Integer auftragIId, TheClientDto theClientDto);

	public int loseAusAuftragAnlegen(ArrayList<LosAusAuftragDto> losAusAuftragDto, Integer auftragIId,
			TheClientDto theClientDto);

	public void updateLosgutschlecht(LosgutschlechtDto losgutschlechtDto, TheClientDto theClientDto);

	public BigDecimal getMengeDerLetztenLosablieferungEinerAuftragsposition(Integer auftragIId, Integer artikelIId);

	public LosgutschlechtRueckmeldungDto createLosgutschlecht(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto);

	public LosgutschlechtRueckmeldungDto createLosgutschlechtMitMaschine(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto);

	public LosgutschlechtDto losgutschlechtFindByPrimaryKey(Integer iId);

	public void removeLosgutschlecht(LosgutschlechtDto losgutschlechtDto);

	public LosgutschlechtDto[] losgutschlechtFindByLossollarbeitsplanIId(Integer losIId);

	public LossollarbeitsplanDto[] getAlleOffenenZeitenFuerStueckrueckmeldung(Integer personalIId,
			TheClientDto theClientDto);

	public LossollarbeitsplanDto[] getAlleOffenenMaschinenArbeitsplan(Integer maschineIId, TheClientDto theClientDto);

	public LossollarbeitsplanDto[] getAlleZusatzlichZuBuchuchendenArbeitsgaenge(Integer lossollarbeitsplanIId,
			TheClientDto theClientDto);

	public void vorhandenenArbeitsplanLoeschen(Integer losIId);

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId, Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto);

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKeyOhneExc(Integer iId);

	public Integer createLostechniker(LostechnikerDto lostechnikerDto, TheClientDto theClientDto);

	public void removeLostechniker(LostechnikerDto lostechnikerDto);

	public LostechnikerDto lostechnikerFindByPrimaryKey(Integer iId);

	public void updateLostechniker(LostechnikerDto lostechnikerDto, TheClientDto theClientDto);

	public LossollarbeitsplanDto lossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang(
			Integer losIId, Integer iArbeitsgangnummer);

	public ArrayList<Integer> getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(Integer loslollarbeitsplanIId);

	public LossollmaterialDto getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(Integer losIId,
			TheClientDto theClientDto);

	public LosablieferungDto createLosablieferungFuerTerminal(LosablieferungDto losablieferungDto,
			TheClientDto theClientDto, boolean bErledigt);

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLossollmaterialIId(Integer lossollmaterialIId,
			TheClientDto theClientDto);

	public LosgutschlechtDto[] losgutschlechtFindAllFehler(Integer losIId);

	public void vonQuelllagerUmbuchenUndAnsLosAusgeben(Integer lagerIId_Quelle, Integer lossollmaterialIId,
			BigDecimal nMenge, List<SeriennrChargennrMitMengeDto> l, TheClientDto theClientDto);

	public void vonSollpositionMengeZuruecknehmen(Integer lossollmaterialIId, BigDecimal nMenge, Integer losIIdZiel,
			Integer lagerIIdZiel, TheClientDto theClientDto);

	public LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(Integer losIId, Integer artikelIId,
			TheClientDto theClientDto);

	public ArrayList<Integer> erledigteLoseImZeitraumNachkalkulieren(java.sql.Date tVon, java.sql.Date tBis,
			TheClientDto theClientDto);

	public Map<Integer, String> getAllZusatzstatus(TheClientDto theClientDto);

	public Integer getJuengstenZusatzstatuseinesLoses(Integer losIId);

	public void toggleMaterialVollstaendig(Integer losIId, TheClientDto theClientDto);

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, TheClientDto theClientDto);

	public LosDto[] losFindByAuftragpositionIId(Integer auftragpositionIId);

	public void offenAgsUmreihen(Integer lossollarbeitsplanIId, boolean bNachUntenReihen);

	public TreeSet<Integer> getLoseEinesStuecklistenbaums(Integer losIId, TheClientDto theClientDto);

	public java.sql.Date getFruehesterEintrefftermin(Integer artikelIId, TheClientDto theClientDto);

	public void sollpreiseAllerSollmaterialpositionenNeuKalkulieren(Integer losIId, TheClientDto theClientDto);

	public void losLaegerAnhandStandortErstellen(Integer losIId, Integer stuecklisteIId, Integer panrtnerIIdStandort,
			TheClientDto theClientDto);

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, Integer partnerIIdStandort,
			TheClientDto theClientDto);

	public RueckgabeMehrereLoseAusgeben gebeAlleLoseBisZumBeginnterminaus(java.sql.Date tEnde,
			boolean throwExceptionWhenCreate, TheClientDto theClientDto);

	public LosDto[] losFindByCProjektMandantCNr(String cProjekt, String mandantCNr);

	public LosDto setupDefaultLos(TheClientDto theClientDto) throws RemoteException;

	public LossollmaterialDto setupDefaultLossollmaterial(ArtikelDto artikelDto, Integer losIId, Integer lagerIId,
			Integer montageartIId, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, Integer partnerIIdStandort,
			String mandantCNr, TheClientDto theClientDto);

	public boolean sindZeitenOhneArbeitsplanbezugVorhanden(Integer losIId, TheClientDto theClientDto);

	public void setzeVPEtikettGedruckt(Integer losIId, TheClientDto theClientDto);

	public ArrayList<String[]> getSelektierteAGsForProfirst(Object[] selectedIds, TheClientDto theClientDto);

	public Integer getZugehoerigenKunden(Integer losIId, TheClientDto theClientDto);

	public Boolean hatLosZusatzstatusP1(Integer losIId);

	public void angelegteLoseVerdichten(ArrayList<Integer> losIIs, TheClientDto theClientDto);

	public BigDecimal[] getSummeMengeSollmaterialUndDauerFuerZuProduzieren(ArrayList<Integer> losIIs);

	public RueckgabeMehrereLoseAusgeben gebeMehrereLoseAus(Object[] losIIds, boolean throwExceptionWhenCreate,
			TheClientDto theClientDto);

	public BigDecimal getAnzahlBisherVerwendet(Integer stuecklisteIId, Integer losIId, TheClientDto theClientDto);

	public Object[] nurProduzierbareLose(Object[] losIIds, TheClientDto theClientDto);

	LosDto losFindByForecastpositionIIdOhneExc(Integer forecastpositionIId);

	List<LosDto> losFindByStuecklisteIIdFertigungsgruppeIIdStatusCnr(Integer stuecklisteIId,
			Integer fertigungsgruppeIId, List<String> stati);

	public Integer proFirstSchachtelplanImportieren(ArrayList<String[]> alZeilen, String schachtelplannummer,
			SeriennrChargennrMitMengeDto snrchnrDto, TheClientDto theClientDto);

	public ArrayList<LosDto> getAusgegebeneLoseEinerSchachtelplannummer(String cSchachtelplannummer,
			TheClientDto theClientDto);

	public void loseEinesSchachteplansAbliefern(HashMap<Integer, BigDecimal> hmLose, TheClientDto theClientDto);

	/**
	 * Losablieferung f&uuml;r Erfassungsterminal optional mit Gut- und/oder
	 * Schrottmenge
	 * <p>
	 * Bei Freigabepr&uuml;fungen (erste Ablieferung auf Los) werden auf Gutmengen
	 * auf ein definiertes Einrichtelager
	 * {@value ParameterFac#PARAMETER_EINRICHTELAGER_ERFASSUNGSTERMINAL} gebucht
	 * (oder Los-Ziellager wenn nicht definiert). Schrottmengen generell ins
	 * Schrottlager
	 * </p>
	 * 
	 * @param losablieferungDto
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	LosablieferungDto createLosablieferungFuerTerminal(LosablieferungTerminalDto losablieferungDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, Timestamp tBelegdatum,
			TheClientDto theClientDto);

	EJBExceptionLP gebeLosAusOhneMaterialbuchung(Integer losIId, boolean bHandausgabe,
			boolean throwExceptionWhenCreateFehlmenge, TheClientDto theClientDto,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos, boolean bKommissionierterminal)
			throws EJBExceptionLP;

	LosablieferungResultDto createLosablieferung(LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt, boolean bKommissionierterminal) throws EJBExceptionLP;

	public void nachtraeglichGeoeffneteLoseErledigen(TheClientDto theClientDto);

	public ArrayList<String> importiereIstMaterial(Integer losIId, List<String[]> daten,
			boolean bResultierenderLagerstand, TheClientDto theClientDto);

	public ArrayList<String> importiereSollMaterial(Integer losIId, List<String[]> daten, TheClientDto theClientDto);

	public void toggleArbeitsplanFertig(Integer lossollarbeitsplanIId, TheClientDto theClientDto);

	public ArrayList<String> importiereGeraeteseriennummernablieferung(Integer losIId, List<String[]> daten,
			TheClientDto theClientDto);

//	List<LosDto> losFindOffeneByTechniker(Integer personalIdTechniker, TheClientDto theClientDto);
//	List<LosDto> losFindOffeneByMe(TheClientDto theClientDto);
	public void tollgeLossollmaterialAlsDringendMarkieren(ArrayList<Integer> selectedIds, TheClientDto theClientDto);

	/**
	 * Den Losstatus auf IN PRODUKTION setzen, sofern er in AUSGEGEBEN ist
	 * 
	 * @param losId        die betreffende LosId
	 * @param theClientDto
	 */
	void setzeLosInProduktion(Integer losId, TheClientDto theClientDto);

	ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String mandantCnr, String zusatzstatusCbez);

	public Integer createBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto, TheClientDto theClientDto);

	public BedarfsuebernahmeDto bedarfsuebernahmeFindByPrimaryKey(Integer iId);

	public void verbucheBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto, BigDecimal bdMengeGenehmigt,
			BigDecimal bdMengeGebucht, List<SeriennrChargennrMitMengeDto> listSnrChnr, Integer lagerIId,
			TheClientDto theClientDto);

	public void verbuchungBedarfsuebernahmeZuruecknehmen(Integer bedarfsuebernahmeIId, TheClientDto theClientDto);

	public void verknuepfungZuBestellpositionUndArbeitsplanLoeschen(Integer lossollmaterialIId, boolean inklusiveVerknuepungZuOriginalartikel);

	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftragReihenUndBeginnEndeBerechnen(
			ArrayList<LosAusAuftragDto> losDtos, TheClientDto theClientDto);

	List<LossollarbeitsplanDto> lossollarbeitsplanFindByLosIIdArbeitsgangnummer(Integer losIId,
			Integer arbeitsgangnummer);

	List<LossollarbeitsplanDto> lossollarbeitsplanFindByLosIIdArbeitsgangnummerUnterarbeitsgang(Integer losIId,
			Integer arbeitsgang, Integer unterarbeitsgang);

	public Map getAllMaschinenInOffeAGs(TheClientDto theClientDto);

	public Date getProduktionsbeginnAnhandZugehoerigemArbeitsgang(Date produktionsbeginn, Integer lossollmaterialIId,
			TheClientDto theClientDto);

	public void losSplitten(Integer losIId, BigDecimal bdLosgroesseNeuesLos, java.sql.Date bdBeginnTerminNeuesLos,
			TheClientDto theClientDto);

	List<LosDto> losFindOffeneByTechniker(Integer personalIdTechniker, List<String> stati, Integer tageInZukunft,
			TheClientDto theClientDto);

	List<LosDto> losFindOffeneByMe(List<String> stati, Integer tageInZukunft, TheClientDto theClientDto);

	public void teilerledigteLoseerledigen(Integer iArbeitstageSeitLetzterAblieferung, TheClientDto theClientDto);

	public void arbeitsgaengeNeuNummerieren(Integer losIId, TheClientDto theClientDto);

	public TreeMap<Integer, HashSet<Integer>> getBetroffeneLoseEinesLoses(Integer losIId,
			boolean loseHierarchischNachkalkulieren, TheClientDto theClientDto);

	public String generiereChargennummer(Integer losIId, TheClientDto theClientDto);

	public Map<Integer, RestmengeUndChargennummerDto> getAllLossollmaterialMitRestmengen(Integer losIId,
			TheClientDto theClientDto);

	/**
	 * Vergleiche die tats&auml;chlich gebuchten Zeiten und Maschinen eines Loses
	 * mit den Sollzeiten aus dem Arbeitsplan.
	 */
	public List<LosArbeitsplanZeitVergleichDto> getVergleichArbeitsplanIstZeitbuchungen(Integer losIId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void uebernehmeNeueArbeitsgangMaschinen(Map<LosArbeitsplanZeitVergleichDto, Integer> maschinen);

	public void uebernehmeNeueSollzeiten(Map<LosArbeitsplanZeitVergleichDto, BigDecimal> neueSollzeiten);

	/**
	 * Buche fuer jedes Lossollmaterial eine passende Restmenge auf das Lager. Die
	 * Map sollte zuerst ueber
	 * {@link FertigungFac#getAllLossollmaterialMitRestmengen(Integer, TheClientDto)}
	 * geholt werden, dann nur die Mengen angepasst, falls Chargennummern vorhanden
	 * sind.
	 * 
	 * @param lossollmaterialIIdZuRestmengen Map von LossollmaterialIId -> Restmenge
	 *                                       und Chargennummer info
	 * @param theClientDto
	 */
	public void bucheRestmengenAufLager(Map<Integer, RestmengeUndChargennummerDto> lossollmaterialIIdZuRestmengen,
			TheClientDto theClientDto);

	public String getHierarchischeChargennummer(Integer losIId, TheClientDto theClientDto);

	public ArrayList<AnzahlInFertigungDto> getAnzahlInFertigungDtos(Integer artikelIId, TheClientDto theClientDto);

	public void bucheTraceImport(ArrayList<TraceImportDto> alZuBuchen, TheClientDto theClientDto);

	public void offenAgsNachAGBeginnReihen(Integer maschineIId, TheClientDto theClientDto);
	
	public Map getAllMaschinenOhneMaschinengruppenInOffeAGs(TheClientDto theClientDto);
	
	public void vertauscheLossollarbeitsplanReihung(Integer iIdLossollarbeitsplan1, Integer iIdLossollarbeitsplan2);

	/**
	 * Der angegebene Arbeitsgang wird als erster der Maschine gereiht.</br>
	 * <p>Es muss keine explizite Reihung zuvor festgelegt worden sein
	 *  (i_reihung kann 0 sein).</p>
	 * 
	 * @param maschineIId die Maschine f&uuml;r die gereiht werden soll
	 * @param wirdErsterAgIId der AG, der als erster f&uuml;r diese Maschine
	 *   erscheinen soll
	 * @param theClientDto
	 */
	void offenenAgAlsErstenReihen(Integer maschineIId, 
			Integer wirdErsterAgIId, TheClientDto theClientDto);
	
	public void updateLosLagerplatz(Integer losIId, Integer lagerplatzIId);
	
	public ArrayList<KeyValue> getListeDerArbeitsgaenge(Integer losIId, 
			TheClientDto theClientDto);

	/**
	 * Liefert eine Liste aller Lossollmaterialien, die einer TruTops-Artikelklasse zugeordnet sind.
	 * Dabei ist das Flag B_TOPS {@value ArtklaDto#getBTops()} der Artikelklasse gesetzt.
	 * 
	 * @param losId Id des Loses, f&uuml;r das die Lossollmaterialien ermittelt werden sollen.
	 * @return Liste aller TruTops-Lossollmaterialien bzw. eine leere Liste, wenn keine ermittelt worden sind.
	 */
	List<LossollmaterialDto> getTruTopsLossollmaterial(LosId losId);

	/**
	 * Liefert eine Liste aller Lossollarbeitspl&auml;ne, deren T&auml;tigkeitsartikel einer TruTops-Artikelklasse zugeordnet sind.
	 * Dabei ist das Flag B_TOPS {@value ArtklaDto#getBTops()} der Artikelklasse gesetzt.
	 * 
	 * @param losId Id des Loses, f&uuml;r das die Lossollarbeitspl&auml;ne ermittelt werden sollen.
	 * @return Liste aller TruTops-Lossollarbeitspl&auml;ne bzw. eine leere Liste, wenn keine ermittelt worden sind.
	 */
	List<LossollarbeitsplanDto> getTruTopsLossollarbeitsplan(LosId losId);

	Map<Integer, Object> objFindByNameClientPrimaryKeys(String methodName,
			Collection<Integer> keys, TheClientDto theClientDto) throws NoSuchMethodException;
	Map<Integer, Object> objFindByNamePrimaryKeys(
			String methodName, Collection<Integer> keys) throws NoSuchMethodException;

	/**
	 * Setzt das TruTops-Lossollmaterial zur&uuml;ck, um einen Export zu erzwingen.
	 * 
	 * @param lossollmaterialId Id des TruTops-Lossollmaterials
	 * @param mitArtikel wenn <code>true</code> werden auch die TruTops-Artikeldaten zur&uuml;ckgesetzt
	 */
	void resetLossollmaterialTruTops(LossollmaterialId lossollmaterialId, Boolean mitArtikel);

	/**
	 * Setzt eine Liste von TruTops-Lossollmaterialien zur&uuml;ck, um einen Export zu erzwingen.
	 * 
	 * @param lossollmaterialIds Liste von TruTops-Lossollmaterial Ids
	 * @param mitArtikel wenn <code>true</code> werden auch die TruTops-Artikeldaten zur&uuml;ckgesetzt
	 */
	void resetLossollmaterialTruTops(Collection<LossollmaterialId> lossollmaterialIds, Boolean mitArtikel);

	/**
	 * Setzt alle TruTops-Lossollmaterialien eines Loses zur&uuml;ck, um einen Export zu erzwingen.
	 * 
	 * @param losId Id des Loses
	 * @param mitArtikel wenn <code>true</code> werden auch die TruTops-Artikeldaten zur&uuml;ckgesetzt
	 */
	void resetLossollmaterialTruTops(LosId losId, Boolean mitArtikel);
	
	public  void removeLosistmaterial(LosistmaterialDto losistmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP;

	void updateLossollmaterialExportBeginn(LossollmaterialId lossollmaterialId, Timestamp tExportBeginn, 
			TheClientDto theClientDto);
	
	void updateLossollmaterialExportEnde(LossollmaterialId lossollmaterialId, Timestamp tExportEnde, 
			String cFehlercode, String cFehlertext, TheClientDto theClientDto);
}
