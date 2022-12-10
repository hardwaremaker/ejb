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
package com.lp.server.auftrag.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AuftragFac extends IAktivierbarControlled {

	// Tabellennamen
	public static final String AUFT_AUFTRAG = "AUFT_AUFTRAG";

	// Auftragzeiten
	public static int IDX_SPALTE_DAUER = 6;
	public static int IDX_SPALTE_KOSTEN = 7;

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_AUFTRAG_I_ID = "i_id";
	public static final String FLR_AUFTRAG_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_AUFTRAG_C_NR = "c_nr";
	public static final String FLR_AUFTRAG_B_VERSTECKT = "b_versteckt";
	public static final String FLR_AUFTRAG_AUFTRAGART_C_NR = "auftragart_c_nr";
	public static final String FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG = "c_bez";
	public static final String FLR_AUFTRAG_C_BESTELLNUMMER = "c_bestellnummer";
	public static final String FLR_AUFTRAG_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_AUFTRAG_T_ERLEDIGT = "t_erledigt";
	public static final String FLR_AUFTRAG_VERTRETER_I_ID = "vertreter_i_id";
	public static final String FLR_AUFTRAG_PERSONAL_I_ID_ERLEDIGT = "personal_i_id_erledigt";
	public static final String FLR_AUFTRAG_D_BELEGDATUM = "t_belegdatum";
	public static final String FLR_AUFTRAG_AUFTRAGSTATUS_C_NR = "auftragstatus_c_nr";
	public static final String FLR_AUFTRAG_WAEHRUNG_C_NR_AUFTRAGSWAEHRUNG = "waehrung_c_nr_auftragswaehrung";
	public static final String FLR_AUFTRAG_B_ROHS = "b_rohs";
	public static final String FLR_AUFTRAG_T_VERRECHENBAR = "t_verrechenbar";
	public static final String FLR_AUFTRAG_T_AUFTRAGSFREIGABE = "t_auftragsfreigabe";
	public static final String FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH = "b_lieferterminunverbindlich";
	public static final String FLR_AUFTRAG_N_GESAMTAUFTRAGSWERT_IN_AUFTRAGSWAEHRUNG = "n_gesamtauftragswertinauftragswaehrung";
	public static final String FLR_AUFTRAG_FLRKUNDE = "flrkunde";
	public static final String FLR_AUFTRAG_FLRKUNDERECHNUNGSADRESSE = "flrkunderechnungsadresse";
	public static final String FLR_AUFTRAG_FLRKUNDELIEFERADRESSE = "flrkundelieferadresse";
	public static final String FLR_AUFTRAG_FLRARTIKEL = "flrartikel";
	public static final String FLR_AUFTRAG_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE = "kunde_i_id_auftragsadresse";
	public static final String FLR_AUFTRAG_KUNDE_I_ID_LIEFERADRESSE = "kunde_i_id_lieferadresse";
	public static final String FLR_AUFTRAG_KUNDE_I_ID_RECHNUNGSADRESSE = "kunde_i_id_rechnungsadresse";
	public static final String FLR_AUFTRAG_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_AUFTRAG_F_WECHSELKURSMANDANTWAEHRUNGZUAUFTRAGSWAEHRUNG = "f_wechselkursmandantwaehrungzuauftragswaehrung";
	public static final String FLR_AUFTRAG_T_FINALTERMIN = "t_finaltermin";
	public static final String FLR_AUFTRAG_B_TEILLIEFERUNGMOEGLICH = "b_teillieferungmoeglich";
	public static final String FLR_AUFTRAG_B_POENALE = "b_poenale";
	public static final String FLR_AUFTRAG_FLRVERTRETER = "flrvertreter";
	public static final String FLR_AUFTRAG_FLRAUFTRAGTEXTSUCHE = "flrauftragtextsuche";
	public static final String FLR_AUFTRAG_FLRPERSONALANLEGER = "flrpersonalanleger";
	public static final String FLR_AUFTRAG_FLRTEILNEHMER = "flrteilnehmer";
	public static final String FLR_AUFTRAG_FLRTEILNEHER_PARTNER_ID = "flrteilnehmer_partnerid";
	public static final String FLR_AUFTRAG_FLRTEILNEHER_PARTNER_ID_ODER_VERTRETER = "flrteilnehmer_partnerid_odervertreter";

	// Laenge der Datenbankfelder
	public static final int MAX_AUFT_AUFTRAG_AUFTRAGNUMMER_C_NR = 8;
	public static final int MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG = 80;
	public static final int MAX_AUFT_AUFTRAG_BESTELLNUMMER = 40;
	public static final int MAX_AUFT_AUFTRAG_LIEFERTERMIN = 8;
	public static final int MAX_AUFT_AUFTRAG_BELEGDATUM = 8;
	public static final int MAX_AUFT_AUFTRAG_AUFTRAGSTATUS = 8;
	public static final int MAX_AUFT_AUFTRAG_AUFTRAGWAEHRUNG = 4;
	public static final int MAX_AUFT_AUFTRAG_GESAMTAUFTRAGSWERT = 10;

	// Maximale, minimale Zahlenbereiche.
	public static final long MAX_I_LEIHTAGE = 9999;
	public static final long MIN_I_LEIHTAGE = 0;
	public static final long MAX_I_GARANTIE_IN_MONATEN = 9999;
	public static final long MIN_I_GARANTIE_IN_MONATEN = 0;

	// welche Namen haben die FLR Spalten zum AuftragteilnehmHandler im
	// Hibernate Mapping
	public static final String FLRSPALTE_AUFTRAGTEILNEHMER_I_ID = "i_id";
	public static final String FLRSPALTE_AUFTRAGTEILNEHMER_I_SORT = "i_sort";

	/** ptkrit: 0 Die Auswertung kann erfolgen nach ... */
	public static String KRIT_BELEGDATUM = "Belegdatum";
	public static String KRIT_LIEFERTERMIN = "Liefertermin";
	public static String KRIT_FINALTERMIN = "Finaltermin";
	public static String KRIT_PERSONAL = "Personal";
	public static String KRIT_IDENT = "Ident";
	public static String KRIT_AUFTRAG_I_ID = "Auftrag";

	// Konstanten fuer PanelTabelle Auftrag Umsatzuebersicht
	public static String AUFT_UMSATZUEBERSICHT_OFFEN = "Offene Auftraege";
	public static String AUFT_UMSATZUEBERSICHT_EINGANG = "Eingegangene Auftraege";

	/** ptkrit: 1 Die Eigenschaften fuer die FilterKriterien mussen festsetzen. */
	public static final int ANZAHL_KRITERIEN_UMSATZUEBERSICHT = 3;
	public static final int UMSATZUEBERSICHT_IDX_KRIT_JAHR = 0;
	public static final int UMSATZUEBERSICHT_IDX_KRIT_AUSWERTUNG = 1;
	public static final int UMSATZUEBERSICHT_IDX_KRIT_PLUS_JAHRE = 2;

	/** ptkrit: 1 Die Eigenschaften fuer die FilterKriterien mussen festsetzen. */
	public static final int ANZAHL_KRITERIEN = 1;
	public static final int IDX_KRIT_AUSWERTUNG = 0;

	/** Die Auswertung kann erfolgen nach ... */
	public static String KRIT_UEBERSICHT_GESCHAEFTSJAHR = "Geschaeftsjahr";
	public static String KRIT_UEBERSICHT_KALENDERJAHR = "Kalenderjahr";
	public static String KRIT_UEBERSICHT_PLUS_JAHRE = "Plusjahre";

	public static final int ANZAHL_KRITERIEN_AUFTRAGZEITEN = 2;
	public static final int IDX_KRIT_AUFTRAG = 1;

	// Tabellennamen
	public final String WW_ARTIKELSPR = "WW_ARTIKELSPR";
	public final String WW_LAGERARTSPR = "WW_LAGERARTSPR";
	public final String PART_KUNDE = "PART_KUNDE";
	public final String PART_PARTNER = "PART_PARTNER";
	public final String WW_ARTIKEL = "WW_ARTIKEL";

	public final String ALIAS_PUNKT = ".";
	public final String ALIAS_WHERE = " WHERE ";
	public final String ALIAS_ORDERBY = " ORDER BY ";
	public final String ALIAS_ISNOTNULL = " IS NOT NULL ";
	public final String ALIAS_ISNULL = " IS NULL ";
	public final String ALIAS_I_ID = "I_ID";
	public final String ALIAS_LAGERART_C_NR = "LAGERART_C_NR";
	public final String ALIAS_LOCALE_C_NR = "LOCALE_C_NR";
	public final String ALIAS_N_MENGE = "N_MENGE";
	public final String ALIAS_SPRACHE_C_NR = "SPRACHE_C_NR";
	public final String ALIAS_I_SORT = "I_SORT";
	public final String ALIAS_MANDANT_C_NR = "MANDANT_C_NR";
	public final String ALIAS_AUFTRAGSTATUS_C_NR = "AUFTRAGSTATUS_C_NR";
	public final String ALIAS_N_OFFENE_MENGE = "N_OFFENE_MENGE";
	public final String ALIAS_AUFTRAGART_C_NR = "AUFTRAGART_C_NR";
	public final String ALIAS_C_NR = "C_NR";
	public final String ALIAS_D_LIEFERTERMIN = "D_LIEFERTERMIN";
	public final String ALIAS_D_FINALTERMIN = "D_FINALTERMIN";
	public final String ALIAS_C_NAME1NACHNAMEFIRMAZEILE1 = "C_NAME1NACHNAMEFIRMAZEILE1";

	public final String P_ANSPRECHPARTNER_ANREDE = "P_ANSPRECHPARTNER_ANREDE";
	public final String P_BELEGKENNUNG = "P_BELEGKENNUNG";
	public final String P_BRIEFANREDE = "P_BRIEFANREDE";
	public final String P_DRUCKDATUM = "P_DRUCKDATUM";
	public final String P_GESCHAEFTSFUEHRER_ANREDE = "P_GESCHAEFTSFUEHRER_ANREDE";
	public final String P_I_ID = "P_I_ID";
	public final String P_KEY = "P_KEY";
	public final String P_KUNDE_ANREDE = "P_KUNDE_ANREDE";
	public final String P_KUNDE_ANSCHRIFT = "P_KUNDE_ANSCHRIFT";
	public final String P_MANDANT_ANREDE = "P_MANDANT_ANREDE";
	public final String P_UNSER_ZEICHEN = "P_UNSER_ZEICHEN";
	public final String P_VERTRETER_ANREDE = "P_VERTRETER_ANREDE";

	public Integer createAuftrag(AuftragDto oAuftragDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void toggleVerrechenbar(Integer auftragIId, TheClientDto theClientDto);

	public boolean updateAuftrag(AuftragDto auftragDtoI,
			String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAuftragKonditionen(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void storniereAuftrag(Integer iiAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void korrekturbetragZuruecknehmen(Integer auftragIId);

	public void manuellErledigen(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AuftragDto auftragFindByPrimaryKey(Integer iId);

	public AuftragDto auftragFindByPrimaryKeyOhneExc(Integer iIdAuftragI);

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException;

	public AuftragDto auftragFindByMandantCNrCNr(String cNrMandantI,
			String cNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AuftragDto auftragFindByMandantCNrCNrOhneExc(String cNrMandantI,
			String cNrI);

	public void pruefeUndSetzeAuftragstatusBeiAenderung(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneMaterialwertGesamt(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<String> terminVerschieben(ArrayList<Integer> auftragIIds,
			java.sql.Timestamp tLiefertermin, java.sql.Timestamp tFinaltermin,
			java.sql.Timestamp tWunschtermin, boolean bMehrereAuftraege, TheClientDto theClientDto);

	public ArrayList<KundeDto> getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdAuftragsadresse, TheClientDto theClientDto);

	public ArrayList<KundeDto> getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdRechnungsadresse, TheClientDto theClientDto);

	public BigDecimal berechneSummeAuftragsnettowert(String cNrAuftragartI,
			String whichKriteriumI, GregorianCalendar gcBerechnungsdatumVonI,
			GregorianCalendar gcBerechnungsdatumBisI,
			String offenOderEingegangenI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneNettowertGesamt(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGesamtwertAuftragProKundeProZeitintervall(
			Integer iIdKundeI, Date datVonI, Date datBisI, String cCurrencyI)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGesamtwertAuftragProStatusProKundeProZeitintervall(
			String sStatusI, Integer iIdKundeI, Date datVonI, Date datBisI,
			String cCurrencyI) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneBestellwertAuftrag(Integer iIdAuftrag)
			throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto[] entferneKalkulatorischeArtikel(
			AuftragpositionDto[] aAuftragpositionDto, TheClientDto theClientDto);

	public String[] berechneSummeSplittbetragAuftrag(Integer iIdAuftrag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAuftragVersteckt(Integer auftragIId,
			TheClientDto theClientDto);

	public void updateArtikelnummerInAuftrag() throws RemoteException;

	public boolean aendereAuftragstatus(Integer pkAuftrag, String status,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeAuftragstatusAufgrundAuftragpositionstati(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneVerkaufswertSoll(Integer iIdAuftragI,
			String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGestehungswertSoll(Integer iIdAuftragI,
			String sArtikelartI, boolean bMitEigengefertigtenStuecklisten,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int berechneAnzahlBelegeZuAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void aktiviereAuftrag(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer erzeugeAuftragAusAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer erzeugeNegativeMengeAusAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer erzeugeLieferscheinAusAuftrag(Integer iIdAuftragI,
			LieferscheinDto lieferscheinDtoI,
			Double dRabattAusRechnungsadresse, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByAuftragstatusCNr(String cNrAuftragstatusI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByAngebotIId(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AuftragDto[] abrufauftragFindByAuftragIIdRahmenauftrag(
			Integer iIdRahmenauftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAuftragOhneWeitereAktion(AuftragDto auftragDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByMandantCNrAuftragartCNr(
			String mandantCNrI, String auftragartCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragDto[] auftragFindByMandantCNrAuftragartCNrStatusCNr(
			String mandantCNrI, String auftragartCNrI, String statusCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean checkPositionFormat(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean darfWiederholungsTerminAendern(Integer auftragIId,
			TheClientDto theClientDto) throws RemoteException;

	public void setzeVersandzeitpunktAufJetzt(Integer iAuftragIId,
			String sDruckart);

	public java.sql.Date getWiederholungsTermin(Integer auftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAuftragBegruendung(Integer auftragIId,
			Integer begruendungIId, TheClientDto theClientDto);

	IOrderResponse createOrderResponse(AuftragDto auftragDto,
			TheClientDto theClientDto) throws NamingException, RemoteException;

	String createOrderResponseToString(AuftragDto auftragDto,
			TheClientDto theClientDto) throws NamingException, RemoteException;

	String createOrderResponsePost(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException, NamingException,
			EJBExceptionLP;

	boolean hatAuftragVersandweg(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException;

	boolean hatAuftragVersandweg(Integer auftragIId, TheClientDto theClientDto)
			throws RemoteException;

	public Integer vorhandenenLieferscheinEinesAuftagsHolenBzwNeuAnlegen(
			Integer auftragIId, TheClientDto theClientDto);

	public Integer erzeugeAuftragpositionUeberSchnellanlage(Integer auftragIId,
			ArtikelDto artikelDto, PaneldatenDto[] paneldatenDtos,
			TheClientDto theClientDto);

	public void auftragFreigeben(Integer auftragIId, TheClientDto theClientDto);

	public void aendereRechnungsadresseProjeknummerBestellnummer(
			AuftragDto auftragDto, TheClientDto theClientDto);

	public void erzeugeAuftragAusBestellungeinesAnderenMandanten(
			Integer bestellungIId, String mandantCNr_Zielmandant,
			TheClientDto theClientDto_Quelle);

	public AuftragDto istAuftragBeiAnderemMandantenHinterlegt(
			Integer bestellungIId);

	public void toggleAuftragsfreigabe(Integer auftragIId,
			TheClientDto theClientDto);

	public ArrayList<Integer> wiederholendeAuftraegeUmIndexAnpassen(
			TheClientDto theClientDto);

	public String wiederholendeAuftraegeMitPreisgueltigkeitAnpassen(
			Timestamp tDatumPreisgueltigkeit, double dAbweichung,
			TheClientDto theClientDto);

	void repairAuftragZws5524(Integer auftragId, TheClientDto theClientDto) throws RemoteException;
	List<Integer> repairAuftragZws5524GetList(TheClientDto theClientDto);

	AuftragDto erzeugeAenderungsauftrag(Integer auftragIId, TheClientDto theClientDto);
	public Map getListeDerErfasstenAuftraege(Integer angebotIId,
			TheClientDto theClientDto);

	String formatLieferadresseAuftrag(Integer auftragId, 
			TheClientDto theClientDto) throws RemoteException;
	String formatLieferadresse(Integer kundeId, 
			Integer ansprechpartnerId, TheClientDto theClientDto)
			throws RemoteException;
	
	public LieferstatusDto lieferstatusFindByPrimaryKey(Integer iId);
	
	public BigDecimal berechneOffenenWertEinesAuftrags(String auftragCNr, TheClientDto theClientDto);
	
	public int importiereSON_CSV(LinkedHashMap<String,   ArrayList<ImportSonCsvDto>> hmNachBestellnummerGruppiert, TheClientDto theClientDto);
	
	public int importiereWooCommerce_CSV(LinkedHashMap<String, ArrayList<ImportWooCommerceCsvDto>> hmNachBestellnummerGruppiert,
			TheClientDto theClientDto);
	
	public void mindermengenzuschlagEntfernen(AuftragDto auftragDto, TheClientDto theClientDto);
	
	public int importiereVAT_XLSX(Integer kundeIId, Integer ansprechpartnerIId, LinkedHashMap<String, ArrayList<ImportVATXlsxDto>> hmNachBestellnummerGruppiert,
			TheClientDto theClientDto);

	/**
	 * Einen neuen Auftrag aus einem bestehenden Auftrag erzeugen</p>
	 * <p>Es werden auch die Positionen kopiert</p>
	 * 
	 * @param auftragId die Id des Quell-Auftrags
	 * @param belegDatum optional kann das Belegdatum vorgegeben werden.
	 *  Fehlt es, wird das aktuelle Datum verwendet 
	 * @param theClientDto
	 * @return die Id des neuen Auftrags
	 */
	Integer erzeugeAuftragAusAuftrag(Integer auftragId, HvOptional<Timestamp> belegDatum, Timestamp tLieferterminUebersteuert,  TheClientDto theClientDto);

	AuftragDto[] auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(Integer iIdKundeI, String cNrMandantI,
			String cBestellnummerI) throws RemoteException;
	
	public void uebersteuereIntelligenteZwischensumme(Integer auftragpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert, TheClientDto theClientDto);
	

	/**
	 * Fuer den Auftrag die Original-EDI-ORDERS Datei laden
	 * 
	 * @param auftragId
	 * @param theClientDto
	 * @return den EDI Dateiinhalt
	 * @throws IOException
	 * @throws RepositoryException
	 */
	String loadEdiFileBestellung(Integer auftragId, TheClientDto theClientDto) throws IOException, RepositoryException;

	String loadEdiFileBestellungDto(AuftragDto auftragDto, TheClientDto theClientDto)
			throws IOException, RepositoryException;
	
	DocPath getDocPathEdiImportFile(AuftragDto auftragDto);

}
