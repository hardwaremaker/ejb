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
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.StuecklisteInfoDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface StuecklisteFac {

	public static final String FLR_MONTAGEART_C_BEZ = "c_bez";

	public static final String FLR_POSERSATZ_FLRSTUECKLISTEPOSITION = "flrstuecklisteposition";

	public static final String FLR_STUECKLISTEEIGENSCHAFTART_C_BEZ = "c_bez";

	public static final String FLR_STUECKLISTEEIGENSCHAFT_C_BEZ = "c_bez";
	public static final String FLR_STUECKLISTEEIGENSCHAFT_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_STUECKLISTEEIGENSCHAFT_FLRSTUECKLISTEEIGENSCHAFTART = "flrstuecklisteeigenschaftart";

	public static final String FLR_STUECKLISTE_FERTIGUNGSGRUPPE_I_ID = "fertigungsgruppe_i_id";
	public static final String FLR_STUECKLISTE_ARTIKEL_I_ID = "artikel_i_id";

	public static final String FLR_STUECKLISTE_STUECKLISTEART_C_NR = "stuecklisteart_c_nr";
	public static final String FLR_STUECKLISTE_B_FREMDFERTIGUNG = "b_fremdfertigung";
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

	public static final String FERTIGUNGSGRUPPE_SOFORTVERBRAUCH = "Sofortverbrauch";

	public static final String STUECKLISTEART_STUECKLISTE = "S              ";
	public static final String STUECKLISTEART_HILFSSTUECKLISTE = "H              ";
	public static final String STUECKLISTEART_SETARTIKEL = "A              ";

	public static final String AGART_LAUFZEIT = "Laufzeit       ";
	public static final String AGART_UMSPANNZEIT = "Umspannzeit    ";

	static class FieldLength {		
		public static final int MAX_STUECKLISTE_ABTEILUNG = 10;
		public static final int STUECKLISTEPOSITION_KOMMENTAR = 80;
		public static final int STUECKLISTEPOSITION_POSITION = 40;
		public static final int STUECKLISTEPOSITION_CBEZ = 40;
	}
	
	public Integer createMontageart(MontageartDto montageartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void vertauscheStuecklisteposition(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP, RemoteException;

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId,
			TheClientDto theClientDto);

	public void vertauscheMontageart(Integer iIdMontageart1I,
			Integer iIdMontageart2I) throws EJBExceptionLP, RemoteException;

	public void vertauscheStuecklisteeigenschaftart(
			Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws EJBExceptionLP,
			RemoteException;

	public void updateStuecklisteLosgroesse(Integer stuecklisteIId,
			BigDecimal nLosgroesse);

	public void removeMontageart(MontageartDto montageartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMontageart(MontageartDto montageartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MontageartDto montageartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<?> importiereStuecklistenstruktur(
			ArrayList<StrukturierterImportDto> struktur,
			Integer stuecklisteIId, TheClientDto theClientDto,
			boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> struktur,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach,
			Integer stuecklisteIId, TheClientDto theClientDto);

	public MontageartDto[] montageartFindByMandantCNr(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MontageartDto montageartFindByMandantCNrCBez(String cBez,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void artikelErsetzten(Integer artikelIIdVon,
			Integer artikelIIdDurch, TheClientDto theClientDto);

	public void removeStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(
			String cBez) throws EJBExceptionLP, RemoteException;

	public Integer createStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public StuecklisteDto stuecklisteFindByPrimaryKeyOhneExc(Integer iId,
			TheClientDto theClientDto);

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer stueckliste, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIId(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto stuecklisteFindByArtikelIIdMandantCNrOhneExc(
			Integer artikelIId, String mandantCNr);

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(
			Integer iIdArtikelI, TheClientDto theClientDto);

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws RemoteException;

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId);

	public Integer createStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId)
			throws EJBExceptionLP, RemoteException;

	public StuecklisteeigenschaftDto[] stuecklisteeigenschaftFindByStuecklisteIId(
			Integer stuecklisteIId) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten(
			TheClientDto theClientDto);

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer getNextArbeitsgang(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer getNextFertigungsgruppe(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllStuecklisteart(TheClientDto theClientDto)
			throws RemoteException;

	public Integer createStuecklistepositions(
			StuecklistepositionDto[] stuecklistepositionDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklistearbeitsplans(
			StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKeyOhneExc(
			Integer iId, TheClientDto theClientDto);

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdBMitdrucken(
			Integer stuecklisteIId, Short bMitdrucken, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdArtikelIId(
			Integer stuecklisteIId, Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public ArrayList<?> getStrukturDatenEinerStueckliste(
			Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList<?> strukturMap,
			boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse, boolean bUnterstklstrukurBelassen)
			throws RemoteException;

	public ArrayList<?> getStrukturDatenEinerStueckliste(
			Integer[] stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList<?> strukturMap,
			boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse, boolean bUnterstklstrukurBelassen);

	public ArrayList<?> getStrukturDatenEinerStuecklisteMitArbeitsplan(
			Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList<?> strukturMap,
			boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse) throws RemoteException;

	public Map<?, ?> getAllFertigungsgrupe(TheClientDto theClientDto);

	public Map<?, ?> getEingeschraenkteFertigungsgruppen(
			TheClientDto theClientDto);

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public StuecklisteInfoDto getStrukturdatenEinesArtikels(
			Integer artikelIIdI, boolean bNurZuDruckendeI,
			StuecklisteInfoDto stuecklisteInfoDtoI, int iEbeneI,
			int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI,
			boolean bFremdfertigungAufloesenI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createFertigungsgruppe(
			FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto)
			throws EJBExceptionLP, RemoteException;

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr(
			String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBez(
			String mandantCNr, String cBez) throws EJBExceptionLP,
			RemoteException;

	public void createStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws RemoteException;

	public void removeStuecklisteart(String cNr) throws RemoteException;

	public void removeStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws RemoteException;

	public void updateStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws RemoteException;

	public void updateStuecklistearts(StuecklisteartDto[] stuecklisteartDtos)
			throws RemoteException;

	public StuecklisteartDto stuecklisteartFindByPrimaryKey(String cNr)
			throws RemoteException;

	public Integer createPosersatz(PosersatzDto posersatzDto,
			TheClientDto theClientDto);

	public void vertauschePosersatz(Integer iIdPosersatz1I,
			Integer iIdPosersatz2I);

	public void removePosersatz(PosersatzDto posersatzDto);

	public void updatePosersatz(PosersatzDto posersatzDto,
			TheClientDto theClientDto);

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public PosersatzDto[] posersatzFindByStuecklistepositionIId(
			Integer stuecklistepositionIId);

	public Integer artikelFindenBzwNeuAnlegen(TheClientDto theClientDto,
			String defaultEinheit, StrukturierterImportDto stkl)
			throws EJBExceptionLP, RemoteException;

	public Integer createKommentarimport(KommentarimportDto dto);

	public void removeKommentarimport(KommentarimportDto dto);

	public KommentarimportDto kommentarimportFindByPrimaryKey(Integer iId);

	public void updateKommentarimport(KommentarimportDto dto);

	public KommentarimportDto[] getAllkommentarimport();

	public List<Integer> getSeriennrChargennrArtikelIIdsFromStueckliste(
			Integer stuecklisteIId, BigDecimal nmenge, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId,
			TheClientDto theClientDto);

	public java.util.HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(
			Integer artikelIId, TheClientDto theClientDto);

	public void importiereStuecklistenINFRA(
			HashMap<String, HashMap<String, byte[]>> dateien,
			TheClientDto theClientDto);

	public void stklINFRAanlegen(ArrayList<StklINFRAHelperDto> alDaten,
			boolean bKopf, Integer stuecklisteIId, Integer montageartIId,
			TheClientDto theClientDto);

	/**
	 * Die Positionen einer St&uuml;ckliste laden</br>
	 * <p>
	 * Es werden alle Positionsdaten geladen, die auch beim
	 * stuecklistepositonFindByPrimaryKey geladen werden.
	 * </p>
	 * 
	 * @param stuecklisteIId
	 *            die Id f&&uml;r die die Positionen ermittelt werden sollen
	 * @param withPrice
	 *            Soll der Verkaufspreis ermittelt werden?
	 * @param theClientDto
	 * @return eine (leere) Liste aller St&uuml;cklistenpositionen
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	List<KundenStuecklistepositionDto> stuecklistepositionFindByStuecklisteIIdAllData(
			Integer stuecklisteIId, boolean withPrice, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Eine Stuecklisteposition ab&auml;ndern.</br>
	 * <p>
	 * Vor dem update wird gepr&uuml;ft, ob sich die Daten ge&auml;ndert haben
	 * k&ouml;nnten. F&uuml;r diese Pr&uuml;fung werden alle Felder
	 * herangezogen.
	 * </p>
	 * 
	 * @param originalDto
	 * @param aenderungDto
	 * @param theClientDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void updateStuecklisteposition(StuecklistepositionDto originalDto,
			StuecklistepositionDto aenderungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

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
	void removeStuecklisteposition(StuecklistepositionDto originalDto,
			StuecklistepositionDto removeDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public StklagerentnahmeDto createStklagerentnahme(
			StklagerentnahmeDto stklagerentnahmeDto, TheClientDto theClientDto);

	public void removeStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto,
			TheClientDto theClientDto);

	public StklagerentnahmeDto stklagerentnahmeFindByPrimaryKey(Integer iId);

	public StklagerentnahmeDto updateStklagerentnahme(
			StklagerentnahmeDto stklagerentnahmeDto, TheClientDto theClientDto);

	public StklagerentnahmeDto[] stklagerentnahmeFindByStuecklisteIId(
			Integer stuecklisteIId);

	public void vertauscheStklagerentnahme(Integer iiDLagerentnahme1,
			Integer iIdLagerentnahme2);

	public void toggleFreigabe(Integer stuecklisteIId, TheClientDto theClientDto);
	
	/**
	 * Liefert die Bean als PositionImporter zur&uuml;ck, um auf die Methoden
	 * des Interfaces {@link IImportPositionen} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportPositionen asPositionImporter() ;

	/**
	 * Liefert die Bean als HeadImporter zur&uuml;ck, um auf die Methoden
	 * des Interfaces {@link IImportHead} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportHead asHeadImporter();
}
