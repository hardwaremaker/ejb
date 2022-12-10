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
package com.lp.server.angebotstkl.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface AngebotstklFac {

	public static final String POSITIONSART_AGSTKL_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String POSITIONSART_AGSTKL_IDENT = LocaleFac.POSITIONSART_IDENT;

	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG = 1;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION = 2;
	public final static int REPORT_AGSTKL_OPTION_SORTIERUNG_SORT = 3;

	public static final String FLR_AGSTKL_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_AGSTKL_WAEHRUNG_C_NR = "waehrung_c_nr";
	public static final String FLR_AGSTKL_FLRKUNDE = "flrkunde";
	public static final String FLR_AGSTKL_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_AGSTKLPOSITION_AGSTKL_I_ID = "agstkl_i_id";
	public static final String FLR_AGSTKLPOSITION_AGSTKLPOSITIONSART_C_NR = "agstklpositionsart_c_nr";
	public static final String FLR_AGSTKLPOSITION_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_AGSTKLPOSITION_B_ARTIKELBEZEICHNUNGUEBERSTEUERT = "b_artikelbezeichnunguebersteuert";
	public static final String FLR_AGSTKLPOSITION_N_MENGE = "n_menge";
	public static final String FLR_AGSTKLPOSITION_EINHEIT_C_NR = "einheit_c_nr";

	public static final String FLR_EINKAUFSANGEBOT_FLRKUNDE = "flrkunde";
	public static final String FLR_EINKAUFSANGEBOT_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_EINKAUFSANGEBOT_C_PROJEKT = "c_projekt";

	public static final String FLR_EINKAUFSANGEBOTPOSITION_EINKAUFSANGEBOT_I_ID = "einkaufsangebot_i_id";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_AGSTKLPOSITIONSART_C_NR = "agstklpositionsart_c_nr";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_MENGE = "n_menge";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS1 = "n_preis1";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS2 = "n_preis2";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS3 = "n_preis3";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS4 = "n_preis4";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_N_PREIS5 = "n_preis5";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_EINKAUFSANGEBOTPOSITION_B_DRUCKEN = "b_drucken";

	public static final String FLR_AGSTKLARBEITSPLAN_L_RUESTZEIT = "l_ruestzeit";
	public static final String FLR_AGSTKLARBEITSPLAN_L_STUECKZEIT = "l_stueckzeit";
	public static final String FLR_AGSTKLARBEITSPLAN_I_ARBEITSGANG = "i_arbeitsgang";
	public static final String FLR_AGSTKLARBEITSPLAN_I_UNTERARBEITSGANG = "i_unterarbeitsgang";
	public static final String FLR_AGSTKLARBEITSPLAN_AGSTKL_I_ID = "agstkl_i_id";
	public static final String FLR_AGSTKLARBEITSPLAN_MASCHINE_I_ID = "maschine_i_id";
	public static final String FLR_AGSTKLARBEITSPLAN_FLRSTUECKLISTE = "flrstueckliste";
	public static final String FLR_AGSTKLARBEITSPLAN_FLRARTIKEL = "flrartikel";
	public static final String FLR_AGSTKLARBEITSPLAN_FLRMASCHINE = "flrmaschine";

	public static final String FLR_WEBPARTNER_KRIT_MANDANT_MIT_LIEFERANT_NULL = "mandant_mit_lieferant_null";
	public static final String FLR_WEBPARTNER_KRIT_MANDANT_OHNE_LIEFERANT_NULL = "mandant_ohne_lieferant_null";

	public static final Integer EK_PREISBASIS_LIEF1PREIS = 0;
	public static final Integer EK_PREISBASIS_NETTOPREIS = 1;

	public static final int MAX_AGSTKL_C_NR = 15;
	public static final int MAX_AGSTKL_C_BEZ = 40;

	public static final int MAX_AGSTKLPOSITION_C_BEZ = 40;

	static class FieldLength {
		public static final int EINKAUFSANGEBOTPOSITION_CBEMERKUNG = 300;
		public static final int EINKAUFSANGEBOTPOSITION_CINTERNEBEMERKUNG = 300;
		public static final int EINKAUFSANGEBOTPOSITION_CPOSITION = 3000;
		public static final int EINKAUFSANGEBOTPOSITION_CHERSTELLERARTIKELNUMMER = 40;
		public static final int EINKAUFSANGEBOTPOSITION_CARTIKELBEZHERSTELLER = 40;
		public static final int EINKAUFSANGEBOTPOSITION_CZBEZ2 = 40;
	}

	static class WebabfrageTyp {
		public static final Integer FINDCHIPS = 1;
		public static final Integer FARNELL = 2;
	}

	public static int VKPREIS_LT_AGTSKLPOSITIONSPREIS = 0;
	public static int VKPREIS_LT_KUNDENPREISFINDUNG = 1;

	
	public static final String MATERIALTYP_QUADER = "Quader         ";
	public static final String MATERIALTYP_RUND = "Rund           ";
	public static final String MATERIALTYP_ROHR = "Rohr           ";
	
	public Map<String, String> getAllAgstklpositionsart() throws EJBExceptionLP, RemoteException;

	/**
	 * TODO: MB->CK bitte in reportFac verschieben.
	 * 
	 * @param iIdAngebotstkl Integer
	 * @param theClientDto   der aktuelle Benutzer
	 * @return JasperPrintLP
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */

	public Integer createAgstkl(AgstklDto agstklDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAgstkl(AgstklDto agstklDto) throws EJBExceptionLP, RemoteException;

	public void updateAgstkl(AgstklDto agstklDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public AgstklDto agstklFindByPrimaryKeyOhneExc(Integer iId);

	public AgstklDto[] agstklFindByKundeIIdMandantCNr(Integer iIdKunde, String cNrMandant)
			throws EJBExceptionLP, RemoteException;

	public AgstklDto[] agstklFindByKundeIIdMandantCNrOhneExc(Integer iIdKunde, String cNrMandant)
			throws RemoteException;

	public Integer createAufschlag(AufschlagDto aufschlagDto, TheClientDto theClientDto);

	public void removeAufschlag(Integer aufschlagIId);

	public void updateAufschlag(AufschlagDto aufschlagDto, TheClientDto theClientDto);

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId);

	public void createAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAgstklpositionsart(String positionsartCNr) throws EJBExceptionLP, RemoteException;

	public void removeAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createEinkaufsangebotpositions(EinkaufsangebotpositionDto[] einkaufsangebotpositionDtos,
			TheClientDto theClientDto) throws RemoteException;

	public AgstklpositionsartDto agstklpositionsartFindByPrimaryKey(String positionsartCNr)
			throws EJBExceptionLP, RemoteException;

	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(Integer agstklIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI, BigDecimal nMengenstaffel,
			String cNrWaehrungI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws RemoteException;

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto, TheClientDto theClientDto)
			throws RemoteException;

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId);

	public Integer createEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto,
			TheClientDto theClientDto) throws RemoteException;

	public void kopierePositionenAusStueckliste(Integer stuecklisteIId, Integer agstklIId, TheClientDto theClientDto);

	public void kopiereArbeitsplanAusStuecklisteInPositionen(Integer stuecklisteIId, Integer agstklIId,
			TheClientDto theClientDto);

	public void kopiereArbeitsplanAusStuecklisteInArbeitsplan(Integer stuecklisteIId, Integer agstklIId,
			TheClientDto theClientDto);

	public void removeEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws RemoteException;

	public void updateEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws RemoteException;

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(Integer iId);

	public List<EinkaufsangebotpositionDto> einkaufsangebotpositionenFindByPrimaryKeys(Integer[] iIds)
			throws EJBExceptionLP;

	public Integer einkaufsangebotpositionGetMaxISort(Integer einkaufsangebotIId) throws RemoteException;

	public void vertauscheEinkausangebotpositionen(Integer idPosition1I, Integer idPosition2I)
			throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public void removeAgstkl(Integer iId) throws RemoteException;

	public void updateAgstkl(AgstklDto agstklDto) throws RemoteException;

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId, Short bMaterial, TheClientDto theClientDto);

	public void updateAgstkls(AgstklDto[] agstklDtos) throws RemoteException;

	public AgstklDto agstklFindByCNrMandantCNr(String cNr, String mandantCNr) throws RemoteException;

	public AgstklDto agstklFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr);

	public AgstklDto[] agstklFindByAnsprechpartnerIIdKunde(Integer iAnsprechpartnerIId) throws RemoteException;

	public void removeEinkaufsangebot(Integer iId) throws RemoteException;

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws RemoteException;

	public void updateEinkaufsangebots(EinkaufsangebotDto[] einkaufsangebotDtos) throws RemoteException;

	public EinkaufsangebotDto[] einkaufsangebotFindByAnsprechpartnerIId(Integer iAnsprechpartnerIId)
			throws RemoteException;

	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(Integer iIdAgstklI, TheClientDto theClientDto);

	public void updateAgstklaufschlag(Integer agstklIId, AufschlagDto[] aufschlagDtos, TheClientDto theClientDto);

	public Integer createAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto);

	public void updateAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto);

	public AgstklarbeitsplanDto agstklarbeitsplanFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public void removeAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto);

	public Integer getNextArbeitsgang(Integer agstklIId, TheClientDto theClientDto);

	public Integer createAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto, TheClientDto theClientDto);

	public void updateAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto, TheClientDto theClientDto);

	public AgstklmengenstaffelDto agstklmengenstaffelFindByPrimaryKey(Integer iId);

	public void removeAgstklmengenstaffel(Integer agstklmengenstaffelIId);

	public BigDecimal getWareneinsatzLief1(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto);

	public BigDecimal getAZeinsatzLief1(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto);

	public BigDecimal[] getVKPreis(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto);

	public LPDatenSubreport getSubreportAgstklMengenstaffel(Integer iIdAngebotstkl, TheClientDto theClientDto);

	public BigDecimal getVKPreisGewaehlt(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto);

	public void kopiereAgstklArbeitsplan(Integer agstklIId_Quelle, Integer agstklIId_Ziel, TheClientDto theClientDto);

	public AgstklarbeitsplanDto[] agstklarbeitsplanFindByAgstklIId(Integer iIdAgstklI, TheClientDto theClientDto);

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

	public void vertauscheWeblieferant(Integer iIdPosition1I, Integer iIdPosition2I);

	public WeblieferantDto weblieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException;

	public WeblieferantDto weblieferantFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException;

	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp, TheClientDto theClientDto)
			throws RemoteException;

	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp, Boolean fullLoad,
			TheClientDto theClientDto) throws RemoteException;

	public void updateWeblieferant(WeblieferantDto dto);

	public void removeWeblieferant(Integer weblieferantIId);

	public Integer createWeblieferant(WeblieferantDto dto, TheClientDto theClientDto);

	public Integer createEkweblieferant(EkweblieferantDto dto, TheClientDto theClientDto);

	public void removeEkweblieferant(Integer ekweblieferantIId);

	public void updateEkweblieferant(EkweblieferantDto dto);

	public EkweblieferantDto ekweblieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws RemoteException;

	public EkweblieferantDto ekweblieferantFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException;

	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer iId,
			Integer webabfrageTyp, TheClientDto theClientDto) throws RemoteException;

	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer iId,
			Integer webabfrageTyp, Boolean fullLoad, TheClientDto theClientDto) throws RemoteException;

	public void vertauscheEkweblieferant(Integer iIdPosition1I, Integer iIdPosition2I);

	public Integer createWebpartner(IWebpartnerDto dto, TheClientDto theClientDto);

	public void updateWebpartner(IWebpartnerDto dto);

	public void removeWebpartner(Integer iId);

	public WebpartnerDto webpartnerFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException;

	public WebpartnerDto webpartnerFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException;

	public WebFindChipsDto webfindchipsFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException;

	public WebFindChipsDto webfindchipsFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException;

	public List<WebFindChipsDto> webfindchipsFindByMandantCNr(Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException;

	public List<WebFindChipsDto> webfindchipsFindByMandantCNr(TheClientDto theClientDto) throws RemoteException;

	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten(Boolean fullLoad,
			TheClientDto theClientDto) throws RemoteException;

	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten(TheClientDto theClientDto)
			throws RemoteException;

	public WebFindChipsDto webfindchipsFindByDistributorId(String distributorId) throws RemoteException;

	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebot(Integer iId, TheClientDto theClientDto)
			throws RemoteException;

	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebotpositionen(List<Integer> iIds,
			TheClientDto theClientDto) throws RemoteException;

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(List<Integer> positionIIds, String artikelCNr,
			TheClientDto theClientDto) throws RemoteException;

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(Integer positionIId, String artikelCNr,
			TheClientDto theClientDto) throws RemoteException;

	public ArtikelDto findeArtikelZuEinkaufsangebotpositionHandeingabe(Integer positionIId, TheClientDto theClientDto)
			throws RemoteException;

	public List<Integer> einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(Integer einkaufsangebotIId);

	public boolean sindMengenstaffelnvorhandenUndPreiseFixiert(Integer iIdAngebotstkl);

	public void preiseDerMengenstaffelnNeuKalkulieren(Integer agstklIId, TheClientDto theClientDto);

	public Integer createEkgruppe(EkgruppeDto ekgruppeDto, TheClientDto theClientDto);

	public void removeEkgruppe(EkgruppeDto ekgruppeDto);

	public void updateEkgruppe(EkgruppeDto ekgruppeDto, TheClientDto theClientDto);

	public EkgruppeDto ekgruppeFindByPrimaryKey(Integer iId);

	public EkgruppelieferantDto ekgruppelieferantFindByPrimaryKey(Integer iId);

	public void removeEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto);

	public void updateEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto, TheClientDto theClientDto);

	public Integer createEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto, TheClientDto theClientDto);

	public Integer createEkaglieferant(EkaglieferantDto ekaglieferantDto, TheClientDto theClientDto);

	public void removeEkaglieferant(EkaglieferantDto ekaglieferantDto);

	public EkaglieferantDto ekaglieferantFindByPrimaryKey(Integer iId);

	public void updateEkaglieferant(EkaglieferantDto ekaglieferantDto, TheClientDto theClientDto);

	public void kopiereEkgruppeInEkaglieferant(Integer einkaufangebotId, Integer ekgruppeIId,
			TheClientDto theClientDto);

	public LinkedHashMap<Integer, byte[]> erzeugeXLSFuerLieferanten(Integer einkaufsangebotIId, Integer lieferantIId,
			TheClientDto theClientDto);

	public Integer createPositionlieferant(PositionlieferantDto positionlieferantDto, TheClientDto theClientDto);

	public PositionlieferantDto positionlieferantFindByPrimaryKey(Integer iId);

	public void removePositionlieferant(PositionlieferantDto positionlieferantDto);

	public void updatePositionlieferant(PositionlieferantDto positionlieferantDto, TheClientDto theClientDto);

	public void leseXLSEinesLieferantenEin(Integer ekaglieferantIId, byte[] xlsDatei, TheClientDto theClientDto);

	public PositionlieferantDto getGuenstigstenLieferant(Integer einkaufsangebotpositionIId, int iMenge,
			TheClientDto theClientDto);

	public PositionlieferantDto getSchnellstenLieferant(Integer einkaufsangebotpositionIId, TheClientDto theClientDto);

	public PositionlieferantDto getBestelltLieferant(Integer positionlieferantIId, int iMenge,
			TheClientDto theClientDto);

	public PositionlieferantDto positionlieferantFindByPrimaryKeyInZielWaehrung(Integer iId, String zielwaehrungCNr,
			TheClientDto theClientDto);

	public PositionlieferantDto getGuenstigstenLieferant(Integer einkaufsangebotpositionIId, int iMenge,
			Integer iLieferzeitInKW, boolean bMindestbestellmengeBeruecksichtigen,
			boolean bVerpackungseinheitBeruecksichtigen, TheClientDto theClientDto);

	public EkagLieferantoptimierenDto getEkagLieferantoptimierenDto(Integer einkaufsangebotIId, Integer iLieferzeitInKW,
			int iMenge, boolean bMindestbestellmengeBeruecksichtigen, boolean bVerpackungseinheitBeruecksichtigen,
			int sortierung, String filterArtikelnummer, String filterArtikelbezeichnung, TheClientDto theClientDto);

	public void lieferantenOptimierenbestellen(EkagLieferantoptimierenDto ekagLieferantoptimierenDto,int iMenge,
			boolean bZuruecknehmen, TheClientDto theClientDto);

	public EkaglieferantDto[] ekaglieferantFindByEinkaufsangebotIId(Integer einkaufsangebotIId);

	public Integer erzeugeStuecklisteAusAgstkl(Integer agstklIId, String artikelnummerNeu, TheClientDto theClientDto);

	public void lieferantenOptimierenArtikelnummerLFSpeichern(Integer einkaufsangebotIId, TheClientDto theClientDto);

	public void verdichteEinkaufsangebotPositionen(Integer einkaufsangebotIId, TheClientDto theClientDto);

	public AgstklmengenstaffelDto[] agstklmengenstaffelFindByAgstklIId(Integer agstklIId, TheClientDto theClientDto);
	
	public Integer createAngebotstuecklisteAusSchnellerfassungUndErzeugeAngebotsposition(AgstklDto agstklDto,
			ArrayList<AgstklarbeitsplanDto> agstklarbeitsplanDtos, ArrayList<AgstklpositionDto> agstklpositionDtos,
			ArrayList<AgstklmengenstaffelSchnellerfassungDto> agstklmengenstaffelDtos, ArrayList<AgstklmaterialDto> agstklmaterialDtos ,Integer angebotIId, BigDecimal bdMenge,
			BigDecimal bdPreis,int iDialoghoehe, AngebotpositionDto angebotpositionDtoVorhanden, TheClientDto theClientDto);
	
	public ArrayList<String> leseLumiquoteXLSEin(Integer einkaufsangebotIId,TreeMap<BigDecimal,LinkedHashMap<String,ArrayList<ImportLumiQuoteXlsxDto>>> hmNachMengenUndLieferantenGruppiert, TheClientDto theClientDto);
	
	public AgstklmengenstaffelSchnellerfassungDto[] agstklmengenstaffelSchnellerfassungFindByAgstklIId(Integer agstklIId, TheClientDto theClientDto);
	
	
	public void updateAgstklmengenstaffelSchnellerfassung(AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelDto, TheClientDto theClientDto);
	public Integer createAgstklmengenstaffelSchnellerfassung(
			AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungDto, TheClientDto theClientDto);
	public AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungFindByPrimaryKey(Integer iId);
	public void removeAgstklmengenstaffelSchnellerfassung(Integer agstklmengenstaffelschnellerfassungIId);
	
	public LPDatenSubreport getSubreportAgstklMengenstaffelSchnellerfassung(Integer iIdAngebotstkl, TheClientDto theClientDto);
	
	public static int MENGE_1 = 1;
	public static int MENGE_2 = 2;
	public static int MENGE_3 = 3;
	public static int MENGE_4 = 4;
	public static int MENGE_5 = 5;

	public static int SORTIERUNG_WIE_ERFASST = 1;
	public static int SORTIERUNG_ARTIKELNUMMER = 2;
	public static int SORTIERUNG_BEZEICHNUNG = 3;
	public static int SORTIERUNG_HERSTELLERNUMER = 4;
	public static int SORTIERUNG_GUENST_PREIS1 = 5;
	public static int SORTIERUNG_GUENST_PREIS2 = 6;
	public static int SORTIERUNG_GUENST_PREIS3 = 7;
	public static int SORTIERUNG_GUENST_PREIS4 = 8;
	public static int SORTIERUNG_GUENST_PREIS5 = 9;

}
