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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.KundeId;
import com.lp.server.util.StuecklisteId;
import com.lp.util.EJBExceptionLP;

@Remote
public interface StuecklisteFac {

	public static final String FLR_MONTAGEART_C_BEZ = "c_bez";
	public static final String FLR_MONTAGEART_ARTIKEL_CNR = "artikel_c_nr";
	public static final String FLR_MONTAGEART_ARTIKEL_BEZ = "artikel_c_bez";

	public static final String FLR_POSERSATZ_FLRSTUECKLISTEPOSITION = "flrstuecklisteposition";

	public static final String FLR_STUECKLISTEEIGENSCHAFTART_C_BEZ = "c_bez";

	public static final String FLR_STUECKLISTEEIGENSCHAFT_C_BEZ = "c_bez";
	public static final String FLR_STUECKLISTEEIGENSCHAFT_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_STUECKLISTEEIGENSCHAFT_FLRSTUECKLISTEEIGENSCHAFTART = "flrstuecklisteeigenschaftart";

	public static final String FLR_STUECKLISTE_FERTIGUNGSGRUPPE_I_ID = "fertigungsgruppe_i_id";
	public static final String FLR_STUECKLISTE_ARTIKEL_I_ID = "artikel_i_id";

	public static final int MAX_POSITION = 3000;
	public static final int MAX_FORMEL = 6000;

	public static final String FLR_STUECKLISTE_STUECKLISTEART_C_NR = "stuecklisteart_c_nr";
	public static final String FLR_STUECKLISTE_B_FREMDFERTIGUNG = "b_fremdfertigung";
	public static final String FLR_STUECKLISTE_B_MIT_FORMELN = "b_mitFormeln";
	public static final String FLR_STUECKLISTE_B_MATERIALBUCHUNGBEIABLIEFERUNG = "b_materialbuchungbeiablieferung";
	public static final String FLR_STUECKLISTE_B_AUSGABEUNTERSTUECKLISTE = "b_ausgabeunterstueckliste";
	public static final String FLR_STUECKLISTE_FLRARTIKEL = "flrartikel";
	public static final String FLR_STUECKLISTE_FLRFERTIGUNGSGRUPPE = "flrfertigungsgruppe";

	public static final String FLR_STUECKLISTEPOSITION_I_SORT = "i_sort";
	public static final String FLR_STUECKLISTEPOSITION_N_MENGE = "n_menge";
	public static final String FLR_STUECKLISTEPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_STUECKLISTEPOSITION_F_DIMENSION1 = "f_dimension1";
	public static final String FLR_STUECKLISTEPOSITION_F_DIMENSION2 = "f_dimension2";
	public static final String FLR_STUECKLISTEPOSITION_F_DIMENSION3 = "f_dimension3";
	public static final String FLR_STUECKLISTEPOSITION_C_KOMMENTAR = "c_kommentar";
	public static final String FLR_STUECKLISTEPOSITION_C_POSITION = "c_position";
	public static final String FLR_STUECKLISTEPOSITION_STUECKLISTE_I_ID = "stueckliste_i_id";
	public static final String FLR_STUECKLISTEPOSITION_B_MITDRUCKEN = "b_mitdrucken";
	public static final String FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_STUECKLISTEPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_STUECKLISTEPOSITION_FLRMONTAGEART = "flrmontageart";

	public static final String FLR_STUECKLISTEARBEITSPLAN_L_RUESTZEIT = "l_ruestzeit";
	public static final String FLR_STUECKLISTEARBEITSPLAN_L_STUECKZEIT = "l_stueckzeit";
	public static final String FLR_STUECKLISTEARBEITSPLAN_I_ARBEITSGANG = "i_arbeitsgang";
	public static final String FLR_STUECKLISTEARBEITSPLAN_I_UNTERARBEITSGANG = "i_unterarbeitsgang";
	public static final String FLR_STUECKLISTEARBEITSPLAN_I_MASCHINENVERSATZTAGE = "i_maschinenversatztage";
	public static final String FLR_STUECKLISTEARBEITSPLAN_STUECKLISTE_I_ID = "stueckliste_i_id";
	public static final String FLR_STUECKLISTEARBEITSPLAN_MASCHINE_I_ID = "maschine_i_id";
	public static final String FLR_STUECKLISTEARBEITSPLAN_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL = "flrartikel";
	public static final String FLR_STUECKLISTEARBEITSPLAN_FLRMASCHINE = "flrmaschine";
	public static final String FLR_STUECKLISTEARBEITSPLAN_N_PPM = "n_ppm";

	public static final String FERTIGUNGSGRUPPE_SOFORTVERBRAUCH = "Sofortverbrauch";

	public static final String STUECKLISTEART_STUECKLISTE = "S              ";
	public static final String STUECKLISTEART_HILFSSTUECKLISTE = "H              ";
	public static final String STUECKLISTEART_SETARTIKEL = "A              ";

	public static final String AGART_LAUFZEIT = "Laufzeit       ";
	public static final String AGART_UMSPANNZEIT = "Umspannzeit    ";

	public static final String PRUEFART_CRIMPEN_MIT_ISO = "Crimpen mit ISO";
	public static final String PRUEFART_CRIMPEN_OHNE_ISO = "Crimpen ohne ISO";
	public static final String PRUEFART_KRAFTMESSUNG = "Kraftmessung";
	public static final String PRUEFART_MASSPRUEFUNG = "Masspruefung";
	public static final String PRUEFART_OPTISCHE_PRUEFUNG = "Optische Pruefung";
	public static final String PRUEFART_ELEKTRISCHE_PRUEFUNG = "Elektrische Pruefung";
	public static final String PRUEFART_MATERIALSTATUS = "Materialstatus";
	public static final String PRUEFART_FREIE_PRUEFUNG = "Freie Pruefung";

	public static final String STKLPARAMETER_TYP_STRING = "java.lang.String";
	public static final String STKLPARAMETER_TYP_INTEGER = "java.lang.Integer";
	public static final String STKLPARAMETER_TYP_DOUBLE = "java.lang.Double";
	public static final String STKLPARAMETER_TYP_BOOLEAN = "java.lang.Boolean";
	public static final String STKLPARAMETER_TYP_BIGDECIMAL = "java.math.BigDecimal";
	public static final String STKLPARAMETER_TYP_ITEM_ID = ItemId.class.getCanonicalName(); // "com.lp.server.stueckliste.service.ItemId";
	public static final String STKLPARAMETER_TYP_KUNDE_ID = KundeId.class.getCanonicalName();

	public static final String FLR_SCRIPTART_C_BEZ = "c_bez";

	static class FieldLength {
		public static final int MAX_STUECKLISTE_ABTEILUNG = 10;
		public static final int STUECKLISTEPOSITION_KOMMENTAR = 80;
		public static final int STUECKLISTEPOSITION_POSITION = 3000;
		public static final int STUECKLISTEPOSITION_CBEZ = 40;
	}

	public static final String LOCKME_PRUEFKOMBINATION = "lockme_pruefkombination";

	public Integer createMontageart(MontageartDto montageartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheStuecklisteposition(Integer iIdPosition1I, Integer iIdPosition2I)
			throws EJBExceptionLP, RemoteException;

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId, boolean bMitVersteckten,
			TheClientDto theClientDto);

	public void vertauscheMontageart(Integer iIdMontageart1I, Integer iIdMontageart2I)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheStuecklisteeigenschaftart(Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteLosgroesse(Integer stuecklisteIId, BigDecimal nLosgroesse);

	public void removeMontageart(MontageartDto montageartDto) throws EJBExceptionLP, RemoteException;

	public void updateMontageart(MontageartDto montageartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MontageartDto montageartFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<?> importiereStuecklistenstruktur(ArrayList<StrukturierterImportDto> struktur,
			Integer stuecklisteIId, TheClientDto theClientDto, boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag) throws EJBExceptionLP, RemoteException;

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> struktur,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach, Integer stuecklisteIId, TheClientDto theClientDto);

	public MontageartDto[] montageartFindByMandantCNr(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MontageartDto montageartFindByMandantCNrCBez(String cBez, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	public MontageartDto montageartFindByMandantCNrCBezOhneExc(String mandantCNr, String cBez)
			throws EJBExceptionLP, RemoteException;

	public Integer createStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void artikelErsetzen(Integer artikelIIdVon, Integer artikelIIdDurch, TheClientDto theClientDto);

	public void removeStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(String cBez)
			throws EJBExceptionLP, RemoteException;

	public Integer createStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP;

	public StuecklisteDto stuecklisteFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto);

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer stueckliste,
			int iSortierungNeuePositionI) throws EJBExceptionLP, RemoteException;

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIId(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByArtikelIIdMandantCNrOhneExc(Integer artikelIId, String mandantCNr);

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(Integer iIdArtikelI, TheClientDto theClientDto);

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNr(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNrOhneExc(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) throws RemoteException;

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId);

	public Integer createStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftDto[] stuecklisteeigenschaftFindByStuecklisteIId(Integer stuecklisteIId)
			throws EJBExceptionLP, RemoteException;

	public Integer createStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten(boolean bMitVersteckten, TheClientDto theClientDto);

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer getNextArbeitsgang(Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer getNextFertigungsgruppe(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllStuecklisteart(TheClientDto theClientDto) throws RemoteException;

	public Integer createStuecklistepositions(StuecklistepositionDto[] stuecklistepositionDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklistearbeitsplans(StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto);

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP;

	/**
	 * Alle Stuecklistenpositionen und den zugehoerigen Artikel laden
	 * 
	 * @param stuecklisteIId ist die Stueckliste fuer die die Positionen geladen
	 *                       werden sollen
	 * @param theClientDto
	 * @return ein Array aller Stuecklistenpositionen der StuecklistenId
	 * @throws EJBExceptionLP
	 */
	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdAllData(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdBMitdrucken(Integer stuecklisteIId,
			Short bMitdrucken, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdArtikelIId(Integer stuecklisteIId,
			Integer artikelIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public ArrayList<?> getStrukturDatenEinerStueckliste(Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList<?> strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen, boolean fremdfertigungAufloesen) throws RemoteException;

	public ArrayList<?> getStrukturDatenEinerStueckliste(Integer[] stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList<?> strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen, boolean fremdfertigungAufloesen);

	public ArrayList<?> getStrukturDatenEinerStuecklisteMitArbeitsplan(Integer stuecklisteIId,
			TheClientDto theClientDto, int iOptionSortierung, int iEbene, ArrayList<?> strukturMap,
			boolean bMitUnterstuecklisten, boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse, boolean bUeberAlleMandanten, boolean bFremdfertigungAufloesen,
			boolean minBSMengeUndVPEBeruecksichtigen, String mandantCNrStueckliste) throws RemoteException;

	public Map<?, ?> getAllFertigungsgrupe(TheClientDto theClientDto);

	public Map<?, ?> getEingeschraenkteFertigungsgruppen(TheClientDto theClientDto);

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId, TheClientDto theClientDto,
			BigDecimal nLosgroesse) throws EJBExceptionLP, RemoteException;

	public Integer createFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto) throws EJBExceptionLP, RemoteException;

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr(String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBez(String mandantCNr, String cBez)
			throws EJBExceptionLP, RemoteException;

	public void createStuecklisteart(StuecklisteartDto stuecklisteartDto) throws RemoteException;

	public void removeStuecklisteart(String cNr) throws RemoteException;

	public void removeStuecklisteart(StuecklisteartDto stuecklisteartDto) throws RemoteException;

	public void updateStuecklisteart(StuecklisteartDto stuecklisteartDto) throws RemoteException;

	public void updateStuecklistearts(StuecklisteartDto[] stuecklisteartDtos) throws RemoteException;

	public StuecklisteartDto stuecklisteartFindByPrimaryKey(String cNr) throws RemoteException;

	public Integer createPosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto);

	public void vertauschePosersatz(Integer iIdPosersatz1I, Integer iIdPosersatz2I);

	public void removePosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto);

	public void updatePosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto);

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public PosersatzDto[] posersatzFindByStuecklistepositionIId(Integer stuecklistepositionIId);

	public Integer artikelFindenBzwNeuAnlegen(TheClientDto theClientDto, String defaultEinheit,
			StrukturierterImportDto stkl) throws EJBExceptionLP, RemoteException;

	public Integer createKommentarimport(KommentarimportDto dto);

	public void removeKommentarimport(KommentarimportDto dto);

	public KommentarimportDto kommentarimportFindByPrimaryKey(Integer iId);

	public void updateKommentarimport(KommentarimportDto dto);

	public KommentarimportDto[] getAllkommentarimport();

	public List<Integer> getSeriennrChargennrArtikelIIdsFromStueckliste(Integer stuecklisteIId, BigDecimal nmenge,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId, TheClientDto theClientDto);

	public java.util.HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(Integer artikelIId,
			TheClientDto theClientDto);

	public void importiereStuecklistenINFRA(HashMap<String, HashMap<String, byte[]>> dateien,
			TheClientDto theClientDto);

	public void stklINFRAanlegen(ArrayList<StklINFRAHelperDto> alDaten, boolean bKopf, Integer stuecklisteIId,
			Integer montageartIId, TheClientDto theClientDto);

	/**
	 * Die Positionen einer St&uuml;ckliste laden</br>
	 * <p>
	 * Es werden alle Positionsdaten geladen, die auch beim
	 * stuecklistepositonFindByPrimaryKey geladen werden.
	 * </p>
	 * 
	 * @param stuecklisteIId die Id f&&uml;r die die Positionen ermittelt werden
	 *                       sollen
	 * @param withPrice      Soll der Verkaufspreis ermittelt werden?
	 * @param theClientDto
	 * @return eine (leere) Liste aller St&uuml;cklistenpositionen
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	List<KundenStuecklistepositionDto> stuecklistepositionFindByStuecklisteIIdAllData(Integer stuecklisteIId,
			boolean withPrice, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	/**
	 * Eine Stuecklisteposition ab&auml;ndern.</br>
	 * <p>
	 * Vor dem update wird gepr&uuml;ft, ob sich die Daten ge&auml;ndert haben
	 * k&ouml;nnten. F&uuml;r diese Pr&uuml;fung werden alle Felder herangezogen.
	 * </p>
	 * 
	 * @param originalDto
	 * @param aenderungDto
	 * @param theClientDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void updateStuecklisteposition(StuecklistepositionDto originalDto, StuecklistepositionDto aenderungDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	/**
	 * Eine Stuecklisteposition l&oouml;schen.</br>
	 * <p>
	 * Vor dem L&oouml;schen wird gepr&uuml;ft, ob sich die Daten ge&auml;ndert
	 * haben k&ouml;nnten. F&uuml;r diese Pr&uuml;fung werden alle Felder
	 * herangezogen.
	 * </p>
	 * 
	 * @param originalDto
	 * @param removeDto
	 * @param theClientDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void removeStuecklisteposition(StuecklistepositionDto originalDto, StuecklistepositionDto removeDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public StklagerentnahmeDto createStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto,
			TheClientDto theClientDto);

	public void removeStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto, TheClientDto theClientDto);

	public StklagerentnahmeDto stklagerentnahmeFindByPrimaryKey(Integer iId);

	public StklagerentnahmeDto updateStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto,
			TheClientDto theClientDto);

	public StklagerentnahmeDto[] stklagerentnahmeFindByStuecklisteIId(Integer stuecklisteIId);

	public void vertauscheStklagerentnahme(Integer iiDLagerentnahme1, Integer iIdLagerentnahme2);

	public void toggleFreigabe(Integer stuecklisteIId, TheClientDto theClientDto);

	/**
	 * Liefert die Bean als PositionImporter zur&uuml;ck, um auf die Methoden des
	 * Interfaces {@link IImportPositionen} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportPositionen asPositionImporter();

	/**
	 * Liefert die Bean als HeadImporter zur&uuml;ck, um auf die Methoden des
	 * Interfaces {@link IImportHead} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportHead asHeadImporter();

	Integer createStuecklisteScriptart(StuecklisteScriptartDto stuecklisteScriptartDto, TheClientDto theClientDto)
			throws EJBExceptionLP;

	void updateStuecklisteScriptart(StuecklisteScriptartDto scriptartDto, TheClientDto theClientDto)
			throws EJBExceptionLP;

	void removeStuecklisteScriptart(StuecklisteScriptartDto scriptartDto) throws EJBExceptionLP;

	StuecklisteScriptartDto[] stuecklisteScriptartFindByMandantCNr(TheClientDto theClientDto) throws EJBExceptionLP;

	StuecklisteScriptartDto stuecklisteScriptartFindByMandantCNrCBez(String cBez, TheClientDto theClientDto)
			throws EJBExceptionLP;

	StuecklisteScriptartDto stuecklisteScriptartFindByPrimaryKey(Integer iId) throws EJBExceptionLP;

	void vertauscheStuecklisteScriptart(Integer iId1, Integer iId2) throws EJBExceptionLP;

	void createOrUpdatePositionsArbeitsplans(Integer stuecklisteId, StuecklistepositionDto[] positionDtos,
			StuecklistearbeitsplanDto[] arbeitsPlanDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createApkommentar(ApkommentarDto dto, TheClientDto theClientDto);

	public void updateApkommentar(ApkommentarDto dto, TheClientDto theClientDto);

	public void removeApkommentar(ApkommentarDto dto);

	public ApkommentarDto apkommentarFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public StuecklisteDto[] stuecklisteFindByArtikelIId(Integer artikelIId);

	public Integer createPruefkombination(PruefkombinationDto dto, TheClientDto theClientDto);

	public PruefkombinationDto pruefkombinationFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public void removePruefkombination(Integer iId);

	public void updatePruefkombination(PruefkombinationDto dto, TheClientDto theClientDto);

	public Integer createStklpruefplan(StklpruefplanDto dto, TheClientDto theClientDto);

	public void updateStklpruefplan(StklpruefplanDto dto, TheClientDto theClientDto);

	public StklpruefplanDto stklpruefplanFindByPrimaryKey(Integer iId);

	public void removeStklpruefplan(Integer iId);

	public StklpruefplanDto[] stklpruefplanFindByStuecklisteIId(Integer stuecklisteIId);

	public PruefkombinationDto[] pruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze(Integer artikelIIdKontakt,
			Integer artikelIIdLitze, TheClientDto theClientDto);

	public PruefkombinationDto pruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId(
			Integer pruefartIId, Integer artikelIIdKontakt, Integer artikelIIdLitze, Integer verschleissteilIId,
			TheClientDto theClientDto);

	public void pruefeBearbeitenDerPruefkombinationErlaubt(TheClientDto theClientDto);

	public void removeLockDerPruefkombinationWennIchIhnSperre(TheClientDto theClientDto);

	public void wechsleMandantEinerSteckliste(Integer stklIId, String mandantCNrNeu, TheClientDto theClientDto);

	public void updatePruefartspr(PruefartDto dto, TheClientDto theClientDto);

	public PruefartDto pruefartFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public Map getAllPruefart(TheClientDto theClientDto);

	public PruefartDto pruefartFindByCNr(String cNr, TheClientDto theClientDto);

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(Integer iIdArtikelI, String mandantCNr);

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBezOhneExc(String mandantCNr, String cBez);

	public FertigungsgruppeDto setupDefaultFertigungsgruppe(String cBez, TheClientDto theClientDto);

	public ApkommentarDto apkommentartFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr);

	public Integer pruefeObPruefplanInPruefkombinationVorhanden(Integer stuecklisteIId, Integer pruefartIId, Integer artikelIIdKontakt,
			Integer artikelIIdLitze, Integer artikelIIdLitze2, Integer verschleissteilIId, Integer pruefkombinationIId,
			boolean throwException, TheClientDto theClientDto);

	public void vertauscheStklpruefplan(Integer iId1, Integer iId2);

	public ArrayList<Integer> getVorgeschlageneVerschleissteile(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			Integer artikelIIdLitze2, boolean bDoppelanschlag);

	public Map<String, String> getAllArbeitsgangarten();

	public AlternativmaschineDto alternativmaschineFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public void removeAlternativmaschine(AlternativmaschineDto dto);

	public Integer createAlternativmaschine(AlternativmaschineDto alternativmaschineDto, TheClientDto theClientDto);

	public void updateAlternativmaschine(AlternativmaschineDto alternativmaschineDto, TheClientDto theClientDto);

	public void vertauscheAlternativmaschine(Integer iIdPosition1I, Integer iIdPosition2I);

	public List<Integer> getMoeglicheMaschinen(Integer lossollarbeitsplanIId, TheClientDto theClientDto);

	public StuecklisteDto stuecklistefindByCFremdsystemnrMandantCNr(String cFremdsystemnr, String mandantCNr);

	public AlternativmaschineDto[] alternativmaschineFindByStuecklistearbeitsplanIId(Integer stuecklistearbeitsplanIId);

	public Object[] kopiereStueckliste(Integer stuecklisteIId, String artikelnummerNeu, java.util.HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	// StrukturDatenParamDto getStrukturDatenEinerStuecklisteMitArbeitsplanNew(
	// Integer stuecklisteId, StrukturDatenParamDto paramDto,
	// TheClientDto theClientDto);

	public StklparameterDto stklparameterFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public Integer createStklparameter(StklparameterDto dto, TheClientDto theClientDto);

	public void removeStklparameter(Integer iId);

	public void updateStklparameter(StklparameterDto dto, TheClientDto theClientDto);

	public StklparameterDto[] stklparameterFindByStuecklisteIId(Integer stuecklisteIId, TheClientDto theClientDto);

	public void vertauscheStklparameter(Integer iId1, Integer iId2);

	public void kopiereParameterEinerStueckliste(Integer stuecklisteIIdQuelle, Integer stuecklisteIIdZiel,
			Map<String, Object> konfigurationsWerte, TheClientDto theClientDto);

	public StklparameterDto stklparameterFindByStuecklisteIIdCNr(Integer stuecklisteIId, String cNr,
			TheClientDto theClientDto);

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByArtikelIId(Integer stuecklisteIId,
			TheClientDto theClientDto);

	public StuecklistepositionDto[] stuecklistepositionFindByArtikelIId(Integer artikelIId, TheClientDto theClientDto);

	public ArrayList getStrukturDatenEinerStueckliste(Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen) throws RemoteException;

	public Integer createProduktstuecklisteAnhandFormelstueckliste(Integer stuecklisteIId_Formelstueckliste,
			BigDecimal losgroesse, Map<String, Object> konfigurationsWerte, Integer kundeIId,
			TheClientDto theClientDto);

	public Integer createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(Integer angebotIId,
			Integer stuecklisteIId_Formelstueckliste, BigDecimal losgroesse, Integer kundeIId,
			Map<String, Object> konfigurationsWerte, TheClientDto theClientDto);

	boolean istArtikelArtikelset(Integer artikelId, String mandantCnr);

	public BigDecimal getGesamtgewichtEinerStuecklisteInKg(Integer stuecklisteIId, BigDecimal nLosgroesse,
			TheClientDto theClientDto);

	Map<Integer, String> getAllPruefartenFuerPruefkombinationen(TheClientDto theClientDto);

	/**
	 * Liefert die Ids aller Pr&uuml;farten, die keine Pr&uuml;fkombinationen
	 * ben&ouml;tigen.
	 * 
	 * @param theClientDto
	 * @return
	 */
	List<Integer> getPruefartenOhnePruefkombination(TheClientDto theClientDto);

	public void sortiereNachArtikelnummer(Integer stuecklisteIId, TheClientDto theClientDto);

	public void arbeitsgaengeNeuNummerieren(Integer stuecklisteIId, TheClientDto theClientDto);
	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByMaschineIId(Integer stuecklisteIId,
			TheClientDto theClientDto);
	public void maschineErsetzen(Integer maschineIIdVon, Integer maschineIIdDurch, TheClientDto theClientDto);
	public void bevorzugtenArtikelEintragen(Integer artikelIId_Bevorzugt ,TheClientDto theClientDto);
	public Integer wirdArtikelInFreigegebenerStuecklisteVerwendet(Integer artikelIId, boolean freigabeDerStuecklistenZuruecknehmen, TheClientDto theClientDto);
	
	public VerkaufspreisDto getKalkuliertenVerkaufspreisAusGesamtkalkulation(Integer artikelIId, BigDecimal bdMenge,java.sql.Date belegdatum,
			String waehrungCNr, TheClientDto theClientDto);
	public void sollzeitenAnhandLosistzeitenAktualisieren(Integer stuecklisteIId,java.sql.Date tVon, java.sql.Date tBis,
			TheClientDto theClientDto);

	FlatPartlistInfo getFlatPartlistInfo(StuecklisteId stuecklisteId, TheClientDto theClientDto);
	
}
