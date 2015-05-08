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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface LagerFac {

	public static final String FLR_LAGERBEWEGUNG_N_MENGE = "n_menge";
	public static final String FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT = "t_buchungszeit";
	public static final String FLR_LAGERBEWEGUNG_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR = "c_seriennrchargennr";
	public static final String FLR_LAGERBEWEGUNG_C_BELEGARTNR = "c_belegartnr";
	public static final String FLR_LAGERBEWEGUNG_I_BELEGARTPOSITIONID = "i_belegartpositionid";
	public static final String FLR_LAGERBEWEGUNG_I_BELEGARTID = "i_belegartid";
	public static final String FLR_LAGERBEWEGUNG_I_ID_BUCHUNG = "i_id_buchung";
	public static final String FLR_LAGERBEWEGUNG_B_ABGANG = "b_abgang";
	public static final String FLR_LAGERBEWEGUNG_B_VOLLSTAENDIGVERBRAUCHT = "b_vollstaendigverbraucht";
	public static final String FLR_LAGERBEWEGUNG_N_VERKAUFSPREIS = "n_verkaufspreis";
	public static final String FLR_LAGERBEWEGUNG_N_EINSTANDSPREIS = "n_einstandspreis";
	public static final String FLR_LAGERBEWEGUNG_N_GESTEHUNGSPREIS = "n_gestehungspreis";
	public static final String FLR_LAGERBEWEGUNG_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_LAGERBEWEGUNG_FLRARTIKEL = "flrartikel";
	public static final String FLR_LAGERBEWEGUNG_FLRLAGER = "flrlager";
	public static final String FLR_LAGERBEWEGUNG_LAGER_I_ID = "lager_i_id";
	public static final String FLR_LAGERBEWEGUNG_B_HISTORIE = "b_historie";

	public static final String FLR_HANDLAGERBEWEGUNG_N_MENGE = "n_menge";

	public static final String FLR_HANDLAGERBEWEGUNG_N_VERKAUFSPREIS = "n_verkaufspreis";
	public static final String FLR_HANDLAGERBEWEGUNG_N_EINSTANDSPREIS = "n_einstandspreis";
	public static final String FLR_HANDLAGERBEWEGUNG_N_GESTEHUNGSPREIS = "n_gestehungspreis";

	public static final String FLR_HANDLAGERBEWEGUNG_C_KOMMENTAR = "c_kommentar";
	public static final String FLR_HANDLAGERBEWEGUNG_T_BUCHUNGSZEIT = "t_buchungszeit";
	public static final String FLR_HANDLAGERBEWEGUNG_B_ABGANG = "b_abgang";
	public static final String FLR_HANDLAGERBEWEGUNG_FLRARTIKEL = "flrartikel";
	public static final String FLR_HANDLAGERBEWEGUNG_FLRLAGER = "flrlager";

	public static final String FLR_LAGERPLATZ_C_LAGERPLATZ = "c_lagerplatz";
	public static final String FLR_LAGERPLATZ_I_MAXMENGE = "i_maxmenge";
	public static final String FLR_LAGERPLATZ_FLRLAGER = "flrlager";

	public static final String FLR_LAGERPLAETZE_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_LAGERPLAETZE_LAGER_I_ID = "lager_i_id";
	public static final String FLR_LAGERPLAETZE_FLRARTIKEL = "flrartikel";
	public static final String FLR_LAGERPLAETZE_FLRLAGER = "flrlager";
	public static final String FLR_LAGERPLAETZE_FLRLAGERPLATZ = "flrlagerplatz";
	public static final String FLR_LAGERPLAETZE_I_SORT = "i_sort";

	public static final String FLR_LAGER_LAGERART_C_NR = "lagerart_c_nr";

	public static final String FLR_ARTIKELLAGER_ARTIKEL_I_ID = "compId.artikel_i_id";
	public static final String FLR_ARTIKELLAGER_LAGER_I_ID = "compId.lager_i_id";
	public static final String FLR_ARTIKELLAGER_N_GESTEHUNGSPREIS = "n_gestehungspreis";
	public static final String FLR_ARTIKELLAGER_N_LAGERSTAND = "n_lagerstand";
	public static final String FLR_ARTIKELLAGER_FLRLAGER = "flrlager";

	public static final String LAGERART_HAUPTLAGER = "Hauptlager     ";
	public static final String LAGERART_ZOLLLAGER = "Zolllager      ";
	public static final String LAGERART_SPERRLAGER = "Sperrlager     ";
	public static final String LAGERART_PERSOENLICH = "Persoenlich    ";
	public static final String LAGERART_NORMAL = "Normal         ";
	public static final String LAGERART_KUNDENLAGER = "Kundenlager    ";
	public static final String LAGERART_SCHROTT = "Schrott        ";
	public static final String LAGERART_WERTGUTSCHRIFT = "Wertgutschrift ";
	public static final String LAGERART_LIEFERANT = "Lieferant      ";
	public static final String LAGERART_HALBFERTIG = "Halbfertig     ";
	public static final String LAGERART_WARENEINGANG = "Wareneingang   ";

	public static final String LAGER_KEINLAGER = "KEIN LAGER     ";
	public static final String LAGER_WERTGUTSCHRIFT = "Wertgutschrift ";

	public final static String REPORT_MODUL = "artikel";

	public static final String REPORT_ZAEHLLISTE = "ww_zaehlliste.jasper";
	public static final String REPORT_SERIENNUMMERN = "ww_seriennummern.jasper";

	public void loescheKompletteLagerbewegungEinerBelgposition(
			String belegartCNr, Integer belegartpositionIId,
			TheClientDto theClientDto);

	public Integer createLager(LagerDto lagerDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createLagerplatz(LagerplatzDto lagerplatzDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLagerplatz(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeLagerplatz(LagerplatzDto lagerplatzDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLagerplatz(LagerplatzDto lagerplatzDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LagerplatzDto lagerplatzFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public void createLagerabgangursprung(
			LagerabgangursprungDto lagerabgangursprungDto)
			throws EJBExceptionLP;

	public void removeLagerabgangursprung(
			LagerabgangursprungDto lagerabgangursprungDto)
			throws EJBExceptionLP;

	public Integer createArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeArtikellagerplaetze(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getLagerstandAllerSperrlaegerEinesMandanten(
			Integer artikelIId, TheClientDto theClientDto);

	public GeraetesnrDto[] getGeraeteseriennummerEinerLagerbewegung(
			String belegartCnr, Integer belegartpositionIId, String cSnr);

	public boolean sindBereitsLagerbewegungenVorhanden(Integer artikelIId);

	public void removeArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void versionInLagerbewegungUpdaten(String belegartCNr,
			Integer belegartpositionIId, String cVersion);

	public void versionInLagerbewegungUpdaten(String belegartCNr,
			Integer belegartpositionIId, String snrChnr, String cVersion);

	public void updateArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public LagerDto[] lagerFindByMandantCNrOrderByILoslagersort(String sMandantI)
			throws EJBExceptionLP;

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIId(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean pruefeObSnrChnrAufBelegGebuchtWurde(String belegartCNr,
			Integer belegartIId, Integer artikelIId, String cSnrChnr);

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerIId(
			Integer artikelIId, Integer lagerIId) throws RemoteException;

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto)
			throws RemoteException;

	public LPDatenSubreport getWareneingangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, boolean bMitJcrDocs,
			TheClientDto theClientDto);

	/**
	 * @deprecated
	 */
	public ArrayList<WarenzugangsreferenzDto> getWareneingangsreferenz(
			String sBelegart, Integer belegartpositionIId,
			String cSeriennrChargennr, boolean bMitJcrDocs,
			TheClientDto theClientDto);

	public ArrayList<WarenzugangsreferenzDto> getWareneingangsreferenz(
			String sBelegart, Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, boolean bMitJcrDocs,
			TheClientDto theClientDto);

	/**
	 * @deprecated
	 */
	public LPDatenSubreport getWareneingangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId, String cSeriennrChargennr,
			boolean bMitJcrDocs, TheClientDto theClientDto);

	public void removeLager(LagerDto lagerDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLager(LagerDto lagerDto) throws EJBExceptionLP,
			RemoteException;

	public String fehlendeAbbuchungenNachtragen(Timestamp tAb,
			TheClientDto theClientDto);

	public BigDecimal getAbgewertetenGestehungspreis(BigDecimal gestpreis,
			Integer artikelIId, Integer lagerIId, Timestamp tStichtag,
			int iMonate, double dProzent) throws RemoteException;

	public LagerDto lagerFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public LagerDto lagerFindByCNrByMandantCNr(String cNr, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	LagerDto lagerFindByCNrByMandantCNrOhneExc(String cnr, String mandantCnr);

	public BigDecimal getGemittelterEinstandspreisAllerLagerndenArtikel(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getGemittelterGestehungspreisDesHauptlagers(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getGemittelterGestehungspreisEinesLagers(
			Integer artikelIId, Integer lagerIId, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getGemittelterGestehungspreisEinesLagers(
			Integer artikelIId, Short bLagerbewirtschaftet,
			String artikelartCNr, Integer lagerIId, TheClientDto theClient)
			throws EJBExceptionLP;

	public BigDecimal getGemittelterGestehungspreisAllerLaegerEinesMandanten(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LagerDto getLagerWertgutschriftDesMandanten(TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getMindestverkaufspreis(Integer artikelIId,
			Integer lagerIId, BigDecimal nMenge, TheClientDto theClientDto)
			throws RemoteException;

	public LagerartDto lagerartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public LagerbewegungDto[] lagerbewegungFindByIIdBuchung(Integer iIdBuchung)
			throws EJBExceptionLP, RemoteException;

	public LagerbewegungDto[] lagerbewegungFindByArtikelIId(Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId);

	public LagerbewegungDto lagerbewegungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrchargennr) throws EJBExceptionLP, RemoteException;

	public LagerabgangursprungDto[] lagerabgangursprungFindByLagerbewegungIIdBuchung(
			Integer iIdBuchung) throws EJBExceptionLP, RemoteException;

	public LagerabgangursprungDto[] lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(
			Integer iIdBuchung) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprLagerArten(String spracheCNr)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getMengeEinerBelegposition(String belegartCNr,
			Integer belegartpositionIId, Timestamp tStichtag)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllLager(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Double getVerbrauchteMenge(Integer lagerbewegungIId)
			throws RemoteException;

	public HashMap<String, BigDecimal> getLagerstaendeAllerLagerartenOhneKeinLager(
			Integer artikelIId, TheClientDto theClientDto);

	public LPDatenSubreport getWarenausgangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto);

	public void pruefeQuickLagerstandGegenEchtenLagerstandUndFuehreAus(
			Integer artikelIIdInput, TheClientDto theClientDto);

	public String pruefeQuickLagerstandGegenEchtenLagerstand(
			Integer artikelIIdInput, TheClientDto theClientDto)
			throws RemoteException;

	public String pruefeBelegeMitLagerbewegungen(TheClientDto theClientDto)
			throws RemoteException;

	public void setzteZugangsBuchungAlsVerbraucht(Integer iIdBuchung,
			boolean bVerbraucht, TheClientDto theClientDto);

	public String pruefeLagerbewegungenMitBelege(TheClientDto theClientDto)
			throws RemoteException;

	public String pruefeLagerabgangurpsrung(TheClientDto theClientDto)
			throws RemoteException;

	public String pruefeVollstaendigVerbraucht(Integer artikelIId,
			boolean bKorrigieren, TheClientDto theClientDto)
			throws RemoteException;

	public String pruefeVerbrauchteMenge(TheClientDto theClientDto)
			throws RemoteException;

	public LagerbewegungDto getJuengsteBuchungEinerBuchungsNummer(
			Integer iIdBuchung) throws EJBExceptionLP, RemoteException;

	public ArtikellagerDto artikellagerFindByPrimaryKey(Integer artikelIId,
			Integer lagerIId) throws EJBExceptionLP, RemoteException;

	public BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeHerstellerUrsprungsland(Integer landIId,
			Integer herstellerIId, String belegartCNr,
			Integer belegartpositionIId, String cSeriennrchargennr,
			TheClientDto theClientDto);

	public BigDecimal getLagerstandOhneExc(Integer artikelIId,
			Integer lagerIId, TheClientDto theClientDto) throws RemoteException;

	public LagerbewegungDto getLetzteintrag(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrchargennr);

	public java.sql.Timestamp getDatumLetzterZugangsOderAbgangsbuchung(
			Integer artikelIId, boolean bAbgang) throws EJBExceptionLP,
			RemoteException;

	public void artikellagerplatzCRUD(Integer artikelIId, Integer lagerIId,
			Integer lagerplatzIId, TheClientDto theClientDto)
			throws RemoteException;

	public int aendereEinzelneSerienChargennummerEinesArtikel(
			Integer artikelIId, String snrChnr_Alt, String snrChnr_Neu,
			String version_Alt, String version_Neu, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getMengeAufLager(Integer artikelIId, Integer lagerIId,
			String cSeriennrchargennr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getDeckungsbeitrag(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getGemittelterGestehungspreisEinerAbgangsposition(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getGemittelterEinstandspreisEinerZugangsposition(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public String istLagerplatzBereitsDurchAnderenArtikelBelegt(
			Integer artikelIId, Integer lagerplatzIId, TheClientDto theClientDto);

	public BigDecimal getGestehungspreisEinerAbgangsposition(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrChargennr) throws EJBExceptionLP, RemoteException;

	public BigDecimal getGestehungspreisEinerAbgangspositionMitTransaktion(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrChargennr) throws EJBExceptionLP, RemoteException;

	public BigDecimal getEinstandspreis(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws EJBExceptionLP, RemoteException;

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId, String cSeriennrchargennr, Timestamp tBelegdatum,
			TheClientDto theClientDto, ArrayList<GeraetesnrDto> alGeraetesnr,
			PaneldatenDto[] paneldatenDtos,
			boolean gestehungspreisNeuKalkulieren) throws EJBExceptionLP,
			RemoteException;

	public String getLagerplaezteEinesArtikels(Integer artikelIId,
			Integer lagerIId) throws RemoteException;

	public void bucheUm(Integer artikelIId_Quelle, Integer lagerIId_Quelle,
			Integer artikelIId_Ziel, Integer lagerIId_Ziel,
			BigDecimal fMengeUmzubuchen,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			String sKommentar, BigDecimal vkpreis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void bucheUmMitAngabeDerQuelle(Integer artikelIId,
			String belegartCNr_Quelle, Integer belegartIId_Quelle,
			Integer belegartpositionIId_Quelle, Integer lagerIId_Quelle,
			Integer lagerIId_Ziel, BigDecimal fMengeUmzubuchen,
			String cSeriennrchargennr, String sKommentar,
			Timestamp tBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdCSeriennrChargennr(
			Integer artikelIId, String cSnrChnr) throws RemoteException;

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstandspreis,
			Integer lagerIId, String cSeriennrchargennr, String cVersion,
			Timestamp tBelegdatum, TheClientDto theClientDto,
			String belegartCNrUrsprung, Integer belegartpositionIIdUrsprung,
			String cSeriennrchargennrUrsprung,
			ArrayList<GeraetesnrDto> alGeraetesnr,
			PaneldatenDto[] paneldatenDtos,
			boolean gestehungspreisNeuKalkulieren);

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto);

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto,
			String belegartCNrUrsprung, Integer belegartpositionIIdUrsprung);

	public void bucheAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId, String cSeriennrchargennr, Timestamp tBelegdatum,
			TheClientDto theClientDto);

	public void bucheAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto);

	public LagerDto lagerFindByMandantCNrLagerartCNr(String sMandantI,
			String sLagerartI) throws EJBExceptionLP, RemoteException;

	public LagerDto[] lagerFindByMandantCNr(String sMandantI)
			throws EJBExceptionLP, RemoteException;

	public LagerDto[] lagerFindAll();

	public BigDecimal getLagerstandAllerLagerAllerMandanten(Integer artikelIId,
			boolean bMitKonsignationslager, TheClientDto theClientDto);

	public LagerDto lagerFindByMandantCNrLagerartCNrOhneExc(String sMandantI,
			String sLagerartI) throws RemoteException;

	public void createLagerart(LagerartDto lagerartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLagerart(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeLagerart(LagerartDto lagerartDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLagerart(LagerartDto lagerartDto) throws EJBExceptionLP,
			RemoteException;

	public ArtikellagerDto[] getAllArtikellager(Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	/**
	 * @deprecated use getAllSerienChargennrAufLagerInfoDtos() instead
	 */
	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto,
			Boolean bNurChargennummern, boolean bSortiertNachSerienChargennummer)
			throws EJBExceptionLP, RemoteException;

	/**
	 * @deprecated use getAllSerienChargennrAufLagerInfoDtos() instead
	 */
	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto,
			Boolean bNurChargennummern,
			boolean bSortiertNachSerienChargennummer, Timestamp tStichtag)
			throws EJBExceptionLP, RemoteException;

	public void updateLagerbewegung(LagerbewegungDto lagerbewegungDto,
			TheClientDto theClientDto);

	public void konstruiereLagergewegungenLOSAusBelegen(
			TheClientDto theClientDto);

	public void konstruiereLagergewegungenHAND(TheClientDto theClientDto);

	public void updateTBelegdatumEinesBelegesImLager(String belegartCNr,
			Integer belegartIId, Timestamp tBelegdatumNeu,
			TheClientDto theClientDto) throws RemoteException;

	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
			String belegartCNr, Integer belegartpositionIId);

	public Integer createHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto)
			throws RemoteException;

	public void updateHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getVerbrauchteMengeEinesArtikels(Integer artikelIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public BigDecimal getLosablieferungenEinesArtikels(Integer artikelIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public void korrigiereVollstaendigVerbraucht(TheClientDto theClientDto,
			Integer i_id_buchung, Integer artikel_i_id, Integer lager_i_id,
			String snrchnr, BigDecimal differenz);

	public HandlagerbewegungDto handlagerbewegungFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void bucheSeriennummernAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId, Integer iMenge,
			BigDecimal nVerkaufspreis, Integer lagerIId, String sSeriennummern,
			Timestamp tBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getMengeMehrererSeriennummernChargennummernAufLager(
			Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> cMehrereSeriennrchargennr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void bucheAbArtikelUpdate(Integer belegartpositionIId_Alt,
			String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId, String cSeriennrchargennr, Timestamp tBelegdatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LagerumbuchungDto lagerumbuchungFindByPrimaryKey(
			Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungAbbuchung) throws RemoteException,
			EJBExceptionLP;

	public LagerumbuchungDto[] lagerumbuchungFindByIdAbbuchung(
			Integer lagerbewegungIIdBuchungAbbuchung) throws EJBExceptionLP,
			RemoteException;

	public LagerumbuchungDto[] lagerumbuchungFindByIdZubuchung(
			Integer lagerbewegungIIdBuchungZubuchung) throws EJBExceptionLP,
			RemoteException;

	public int aendereEigenschaftChargengefuehrt(Integer artikelIId,
			boolean bSnrChargennrtragend, TheClientDto theClientDto);

	public LagerDto getHauptlagerDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printSeriennummern(Integer lagerIId,
			Integer artikelIId, String[] snrs, String snrWildcard,
			Boolean bSortNachIdent, boolean bMitGeraeteseriennummern,
			String versionWildcard, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId,
			boolean bMitKonsignationslager, TheClientDto theClientDto);

	public BigDecimal getLagerstandZumZeitpunkt(Integer artikelIId,
			Integer lagerIId, Timestamp tsZeitpunkt, TheClientDto theClientDto)
			throws RemoteException;

	public HashMap<Integer, BigDecimal> getLagerstandAllerArtikelZumZeitpunkt(
			Integer lagerIId, java.sql.Timestamp tsZeitpunkt,
			TheClientDto theClientDto);

	public BigDecimal getGestehungspreisZumZeitpunkt(Integer artikelIId,
			Integer lagerIId, Timestamp tsZeitpunkt, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal getLagerstandsVeraenderungOhneInventurbuchungen(
			Integer artikelIId, Integer lagerIId, Timestamp tVon,
			Timestamp tBis, String cSnrChnr, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getArtikelSollBestand(ArtikelDto artikelDto)
			throws RemoteException, EJBExceptionLP;

	public String getAsStringDocumentWS(Integer iIdBestellpositionI,
			TheClientDto theClientDto) throws RemoteException;

	public BelegInfos getBelegInfos(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, TheClientDto theClientDto)
			throws RemoteException;

	public Integer getAnzahlVerwendungenEinesLagerplatzes(Integer lagerplatzIId);

	public void updateGestpreisArtikellager(ArtikellagerDto artikellagerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getAllSerienChargennrAufLagerInfo(Integer artikelIId,
			Integer lagerIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String cSeriennrChargennr,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto);

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto)
			throws RemoteException;

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtosMitBereitsVerbrauchten(
			Integer artikelIId, Integer lagerIId,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto);

	public LagerplatzDto[] getAlleLagerplaetze() throws RemoteException;

	public void updateArtikellager(ArtikellagerDto artikellagerDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal sofortverbrauchAllerArtikelAbbuchen(
			TheClientDto theClientDto) throws RemoteException;

	public String wirdLagermindeststandUnterschritten(
			java.sql.Date tPositionsdatum, BigDecimal bdPositionsmenge,
			Integer artikelIId, TheClientDto theClientDto);

	public BigDecimal getPaternosterLagerstand(Integer artikelIId);

	public boolean hatRolleBerechtigungAufLager(Integer lagerIId,
			TheClientDto theClientDto);

	public String pruefeIIdBuchungen(Integer artikelIId,
			TheClientDto theClientDto);

	public Integer getArtikelIIdUeberSeriennummer(String snr,
			TheClientDto theClientDto);

	public Integer getArtikelIIdUeberSeriennummerAbgang(String snr,
			TheClientDto theClientDto);

	public LagerDto lagerFindByPrimaryKeyOhneExc(Integer lagerIId);

	public ArtikellagerplaetzeDto getErstenArtikellagerplatz(
			Integer artikelIId, TheClientDto theClientDto);

	public void vertauscheArtikellagerplaetze(Integer iId1, Integer iId2);

	public LagerDto getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(
			Integer artikelIId);

	public LagerDto getLagerDesErstenArtikellagerplatzes(Integer artikelIId,
			TheClientDto theClientDto);

	public HandlagerbewegungDto getZugehoerigeUmbuchung(
			Integer handlagerbewegungIId, TheClientDto theClientDto);

	public void konstruiereLagergewegungenLSREAusBelegen(
			TheClientDto theClientDto);

	public void konstruiereLagergewegungenBESTAusBelegen(
			TheClientDto theClientDto);

	public Integer getLetzteWEP_IID(Integer artikelIId);

	public String getNaechsteSeriennummer(Integer artikelIId,
			String uebersteuerteSeriennummer, TheClientDto theClientDto);

	public void versionPerEntityManagerUpdaten(Integer lagerbewegungIId,
			String cSnr, String cVersion);

	public void pruefeSeriennummernMitVersion(TheClientDto theClientDto);

	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
			String belegartCNr, Integer belegartpositionIId);

	public void gestehungspreiseImportieren(ArrayList<Object[]> alDaten,
			Integer lagerIId, TheClientDto theClientDto);

	public String getNaechsteChargennummer(Integer artikelIId,
			TheClientDto theClientDto);

	public LPDatenSubreport getSubreportEnthaltenesLosIstMaterial(
			String artikelnummer, String chargennummer,
			TheClientDto theClientDto);

	public LagerDto getHauptlagerEinesMandanten(String mandantCNr);
	
	public BigDecimal getEinstandsWertEinesBeleges(String belegartCNr,
			Integer belegartIId, String sArtikelartI, TheClientDto theClientDto);
}
