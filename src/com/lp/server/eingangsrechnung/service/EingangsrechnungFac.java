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
package com.lp.server.eingangsrechnung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.service.JxlImportErgebnis;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.util.EJBExceptionLP;

@Remote
public interface EingangsrechnungFac {

	public static final String LOCKME_EINGANGSRECHNUNG = "lockme_eingangsrechnung";

	public final static String EINGANGSRECHNUNGART_ANZAHLUNG = "Anzahlung           ";
	public final static String EINGANGSRECHNUNGART_EINGANGSRECHNUNG = "Eingangsrechnung    ";
	public final static String EINGANGSRECHNUNGART_SCHLUSSZAHLUNG = "Schlussrechnung     ";
	public final static String EINGANGSRECHNUNGART_GUTSCHRIFT = "Gutschrift          ";
	public final static String EINGANGSRECHNUNGART_ZUSATZKOSTEN = "Zusatzkosten        ";

	public static final int ANZAHL_KRITERIEN = 3;
	public static final int IDX_KRIT_DATUM = 0;
	public static final int IDX_KRIT_JAHR = 1;
	public static final int IDX_KRIT_ZUSATZKOSTEN = 2;

	public static final String KRIT_DATUM_BELEGDATUM = "Belegdatum";
	public static final String KRIT_DATUM_FREIGABEDATUM = "Freigabedatum";

	public static final String KRIT_JAHR_KALENDERJAHR = "Kalenderjahr";
	public static final String KRIT_JAHR_GESCHAEFTSJAHR = "Geschaeftsjahr";
	public static final String KRIT_ZUSATZKOSTEN = "Zusatzkosten";

	public static final String KRIT_WAEHRUNG = "Waehrung";

	// Stati einer ER
	public static final String STATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String STATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String STATUS_TEILBEZAHLT = LocaleFac.STATUS_TEILBEZAHLT;
	public static final String STATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;

	public final static String FLR_AZ_I_ID = "i_id";
	public final static String FLR_AZ_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public final static String FLR_AZ_N_BETRAG = "n_betrag";
	public final static String FLR_AZ_C_TEXT = "c_text";
	public final static String FLR_AZ_FLRAUFTRAG = "flrauftrag";

	public final static String FLR_ER_I_ID = "i_id";
	public final static String FLR_ER_C_NR = "c_nr";
	public final static String FLR_ER_C_TEXT = "c_text";
	public final static String FLR_ER_I_GESCHEAFTSJAHR = "i_geschaeftsjahr";
	public static final String FLR_ER_T_FIBUUEBERNAHME = "t_fibuuebernahme";
	public final static String FLR_ER_C_LIEFERANTENRECHNUNGSNUMMER = "c_lieferantenrechnungsnummer";
	public final static String FLR_ER_MANDANT_C_NR = "mandant_c_nr";
	public final static String FLR_ER_EINGANGSRECHNUNGART_C_NR = "eingangsrechnungart_c_nr";
	public final static String FLR_ER_D_BELEGDATUM = "t_belegdatum";
	public final static String FLR_ER_D_FREIGABEDATUM = "t_freigabedatum";
	public final static String FLR_ER_N_BETRAG = "n_betrag";
	public final static String FLR_ER_N_BETRAGFW = "n_betragfw";
	public final static String FLR_ER_N_USTBETRAG = "n_ustbetrag";
	public final static String FLR_ER_N_USTBETRAGFW = "n_ustbetragfw";
	public final static String FLR_ER_STATUS_C_NR = "status_c_nr";
	public final static String FLR_ER_PERSONAL_I_ID_WIEDERHOLENDERLEDIGT_C_NR = "personal_i_id_wiederholenderledigt";
	public final static String FLR_ER_WAEHRUNG_C_NR = "waehrung_c_nr";
	public final static String FLR_ER_D_BEZAHLTDATUM = "t_bezahltdatum";
	public static final String FLR_ER_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_ER_ZAHLUNGSZIEL_I_ID = "zahlungsziel_i_id";
	public final static String FLR_ER_LIEFERANT_I_ID = "lieferant_i_id";
	public final static String FLR_ER_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public final static String FLR_ER_FLRLIEFERANT = "flrlieferant";
	public final static String FLR_ER_FLREINGANGSRECHNUNGTEXTSUCHE = "flreingangsrechnungtextsuche";
	public final static String FLR_ER_FLRKOSTENSTELLE = "flrkostenstelle";

	public final static String FLR_KONTIERUNG_I_ID = "i_id";
	public final static String FLR_KONTIERUNG_EINGANGRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public final static String FLR_KONTIERUNG_N_BETRAG = "n_betrag";
	public final static String FLR_KONTIERUNG_N_BETRAG_UST = "n_betrag_ust";
	public final static String FLR_KONTIERUNG_FLRKOSTENSTELLE = "flrkostenstelle";
	public final static String FLR_KONTIERUNG_FLRKONTO = "flrkonto";
	public final static String FLR_KONTIERUNG_FLREINGANGSRECHNUNG = "flreingangsrechnung";

	public final static String FLR_ZAHLUNG_I_ID = "i_id";
	public final static String FLR_ZAHLUNG_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public final static String FLR_ZAHLUNG_D_ZAHLDATUM = "t_zahldatum";
	public final static String FLR_ZAHLUNG_ZAHLUNGSART_C_NR = "zahlungsart_c_nr";
	public final static String FLR_ZAHLUNG_N_KURS = "n_kurs";
	public final static String FLR_ZAHLUNG_N_BETRAG = "n_betrag";
	public final static String FLR_ZAHLUNG_N_BETRAGFW = "n_betragfw";
	public final static String FLR_ZAHLUNG_N_BETRAG_UST = "n_betrag_ust";
	public final static String FLR_ZAHLUNG_N_BETRAG_USTFW = "n_betrag_ustfw";
	public final static String FLR_ZAHLUNG_I_AUSZUG = "i_auszug";
	public final static String FLR_ZAHLUNG_T_WECHSELFAELLIGAM = "t_wechsel_faellig_am";
	public final static String FLR_ZAHLUNG_FLRBANKVERBINDUNG = "flrbankverbindung";
	public final static String FLR_ZAHLUNG_FLRKASSENBUCH = "flrkassenbuch";

	public final static String FLR_EINGANGSRECHNUNGSSTATUS_STATUS_C_NR = "status_c_nr";
	public final static String FLR_EINGANGSRECHNUNGSART_C_NR = "c_nr";

	public final static String FLR_EINGANGSRECHNUNGDOKUMENT_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public final static String FLR_EINGANGSRECHNUNGDOKUMENT_DOKUMENT_I_ID = "dokument_i_id";
	public final static String FLR_EINGANGSRECHNUNGDOKUMENT_FLRDOKUMENT = "flrdokument";
	public final static String FLR_EINGANGSRECHNUNGDOKUMENT_FLREINGANGSRECHNUNG = "flreingangsrechnung";

	public final static String FLR_ZV_LAUF_I_ID = "i_id";
	public final static String FLR_ZV_LAUF_MANDANT_C_NR = "mandant_c_nr";
	public final static String FLR_ZV_LAUF_T_ANLEGEN = "t_anlegen";
	public final static String FLR_ZV_LAUF_T_ZAHLUNGSSTICHTAG = "t_zahlungsstichtag";
	public final static String FLR_ZV_LAUF_T_NAECHSTERZAHLUNGSLAUF = "t_naechsterzahlungslauf";
	public final static String FLR_ZV_LAUF_B_MITSKONTO = "b_mitskonto";
	public final static String FLR_ZV_LAUF_I_SKONTOUEBERZIEHUNGSFRISTINTAGEN = "i_skontoueberziehungsfristintagen";
	public final static String FLR_ZV_LAUF_BANKVERBINDUNG_I_ID = "bankverbindung_i_id";
	public final static String FLR_ZV_LAUF_FLRBANKVERBINDUNG = "flrbankverbindung";

	public final static String FLR_ZV_I_ID = "i_id";
	public final static String FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID = "zahlungsvorschlaglauf_i_id";
	public final static String FLR_ZV_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public final static String FLR_ZV_B_BEZAHLEN = "b_bezahlen";
	public final static String FLR_ZV_T_FAELLIG = "t_faellig";
	public final static String FLR_ZV_N_ANGEWANDTERSKONTOSATZ = "n_angewandterskontosatz";
	public final static String FLR_ZV_N_ZAHLBETRAG = "n_zahlbetrag";
	public final static String FLR_ZV_FLREINGANGSRECHNUNG = "flreingangsrechnungreport";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_EINGANGSRECHNUNGTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_EINGANGSRECHNUNGTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_EINGANGSRECHNUNGTEXT_X_TEXTINHALT = "c_textinhalt";
	
	static class FieldLength {
		public static final int KUNDENDATEN = 12;
		public static final int LIEFERANTENRECHNUNGSNR = 20;
		public static final int TEXT = 40;
	}
	
	public EingangsrechnungDto createEingangsrechnung(
			EingangsrechnungDto erDtoI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void storniereEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void storniereEingangsrechnungRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public EingangsrechnungDto updateEingangsrechnung(
			EingangsrechnungDto erDtoI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKeyOhneExc(
			Integer eingangsrechnungIId) throws RemoteException, EJBExceptionLP;

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKeyWithNull(
			Integer eingangsrechnungIId);

	public EingangsrechnungDto[] eingangsrechnungFindByMandantCNr(
			String mandantCNr) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungDto[] eingangsrechnungFindByMandantCNrDatumVonBis(
			String mandantCNr, Date dVon, Date dBis) throws EJBExceptionLP,
			RemoteException;

	public EingangsrechnungDto[] eingangsrechnungFindByBestellungIId(
			Integer bestellungIId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungDto[] eingangsrechnungFindByMandantLieferantIId(
			String mandantCNr, Integer lieferantIId) throws EJBExceptionLP,
			RemoteException;

	public EingangsrechnungDto eingangsrechnungFindByCNrMandantCNr(String cNr,
			String mandantCNr,	boolean bZusatzkosten);

	public EingangsrechnungDto[] eingangsrechnungFindByMandantLieferantIIdOhneExc(
			String mandantCNr, Integer lieferantIId) throws RemoteException;

	public ArrayList<EingangsrechnungDto> eingangsrechnungFindOffeneByLieferantIId(
			Integer lieferantIId);

	public EingangsrechnungAuftragszuordnungDto createEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungAuftragszuordnungDto updateEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungFindByAuftragIId(
			Integer auftragIId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByRechnungzahlungIId(
			Integer rechnungzahlungIId);

	public EingangsrechnungKontierungDto[] eingangsrechnungKontierungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungKontierungDto[] eingangsrechnungKontierungFindAll()
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getWertNochNichtZuAuftraegenZugeordnet(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public BigDecimal getWertNochNichtKontiert(Integer eingangsrechnungIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getSummeZuAuftraegenZugeordnet(Integer eingangsrechnungIId)
			throws EJBExceptionLP, RemoteException;

	public Date getDefaultFreigabeDatum() throws RemoteException;

	public BigDecimal berechneSummeOffenBruttoInMandantenwaehrung(
			TheClientDto theClientDto, String sKriteriumDatum, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten);

	public BigDecimal berechneSummeOffenNettoInMandantenwaehrung(
			TheClientDto theClientDto, String sKriteriumDatum, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten);

	public BigDecimal berechneSummeUmsatzBrutto(TheClientDto theClientDto,Integer lieferantIId,
			String sKriteriumDatum,	String sKriteriumWaehrung, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneSummeUmsatzNetto(TheClientDto theClientDto,Integer lieferantIId,
			String sKriteriumDatum,	String sKriteriumWaehrung, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneSummeAnzahlungenNichtVerrechnetNetto(TheClientDto theClientDto,
			String sKriteriumDatum, GregorianCalendar gcVon,
			GregorianCalendar gcBis) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneSummeAnzahlungenNichtVerrechnetBrutto(TheClientDto theClientDto,
			String sKriteriumDatum, GregorianCalendar gcVon,
			GregorianCalendar gcBis) throws EJBExceptionLP, RemoteException;

	public void updateEingangsrechnungMahndaten(
			Integer eingangsrechnungrechnungIId, Integer mahnstufeIId,
			Timestamp tMahndatum);

	public void updateEingangsrechnungStatus(TheClientDto theClientDto,
			Integer iId, String statusCNr) throws EJBExceptionLP,
			RemoteException;

	public EingangsrechnungKontierungDto createEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungKontierungDto updateEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungKontierungDto eingangsrechnungKontierungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto createEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			Boolean bErledigt, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ZusatzkostenAnlegenResult wiederholendeZusatzkostenAnlegen(TheClientDto theClientDto);

	public EingangsrechnungzahlungDto updateEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			Boolean bErledigt, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto[] eingangsrechnungzahlungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto[] eingangsrechnungzahlungFindAll()
			throws EJBExceptionLP, RemoteException;

	public void toggleWiederholendErledigt(Integer eingangsrechnungIId,
			TheClientDto theClientDto);

	public BigDecimal getBezahltBetragFw(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal getBezahltBetragFwKontierung(Integer eingangsrechnungIId,
			Integer kontierungIId) throws EJBExceptionLP;

	public BigDecimal getBezahltBetrag(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal getBezahltBetragUst(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen);
	
	public BigDecimal getBezahltBetragUstFw(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP,
			RemoteException;

	public BankverbindungDto getZuletztVerwendeteBankverbindung(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public Integer getAuszugDerLetztenZahlung(Integer eingangsrechnungIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneAnzahlungswertVonBestellungInMandantWaehrung(
			Integer bestellungIId) throws RemoteException;

	public Double getWechselkursEingangsrechnungswaehrungZuMandantwaehrung(
			Integer iIdEingangsrechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdEingangsrechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeEingangsrechnungFibuUebernahme(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeEingangsrechnungFibuUebernahmeRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public EingangsrechnungDto[] eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
			Integer lieferantIId, String sLieferantenrechnungsnummer)
			throws EJBExceptionLP, RemoteException;

	public EingangsrechnungzahlungDto getLetzteZahlung(
			Integer eingangsrechnungIId) throws EJBExceptionLP, RemoteException;

	public void removeEingangsrechnungzahlung(Integer iId)
			throws RemoteException;

	public void removeEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto)
			throws RemoteException;

	public void updateEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto)
			throws RemoteException;

	public void updateEingangsrechnungzahlungs(
			EingangsrechnungzahlungDto[] eingangsrechnungzahlungDtos)
			throws RemoteException;

	public void pruefePreise(TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public void updateEingangsrechnungGedruckt(TheClientDto theClientDto,
			Integer iId, Timestamp tGedruckt);

	public List<Integer> eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
			String mandant, Date von, Date bis);

	public void verbucheEingangsrechnungNeu(Integer iRechnungIId,
			TheClientDto theClient) throws EJBExceptionLP, RemoteException;

	public BigDecimal getBezahltKursdifferenzBetrag(
			Integer eingangsrechnungIId, BigDecimal kursRechnung)
			throws EJBExceptionLP, RemoteException;

	public void toggleZollimportpapiereErhalten(Integer eingangsrechnungIId, String cZollimportpapier, Integer eingangsrechnungIId_Zollimport,
			TheClientDto theClientDto);

	public BigDecimal getAnzahlungenGestelltZuSchlussrechnungUstFw(Integer erIId);

	public BigDecimal getAnzahlungenGestelltZuSchlussrechnungFw(Integer erIId);

	public BigDecimal getAnzahlungenGestelltZuSchlussrechnung(Integer erIId);

	public BigDecimal getAnzahlungenBezahltZuSchlussrechnungFw(Integer erIId);

	public BigDecimal getAnzahlungenBezahltZuSchlussrechnungUstFw(Integer erIId);
	public BigDecimal getAnzahlungenGestelltZuSchlussrechnungUst(Integer erIId);

	public void eingangsrechnungAufAngelegtZuruecksetzen(
			Integer eingangangsrechnungIId, TheClientDto theClientDto);
	public void aktiviereBeleg(Integer eingangangsrechnungIId,
			TheClientDto theClientDto);
	public void updateKopfFusstextUebersteuert(Integer eingangsrechnungIId,
			String cKopftext, String cFusstext, TheClientDto theClientDto);
	
	/**
	 * Liefert eine Liste jener Stati, die es einem erlauben, zu einer Eingangsrechnung
	 * eine Eingangsrechnungzahlung durchzufuehren
	 * 
	 * @return Liste der erlaubten Stati
	 */
	public List<String> getErlaubteStatiFuerEingangsrechnungZahlung();
	
	public BigDecimal getWertUstAnteiligZuEingangsrechnungUst(Integer erIId, BigDecimal bruttoBetrag);
	
	public VendidataImporterResult importXML(String xmlContent, boolean checkOnly, TheClientDto theClientDto);
	
	public void eingangsrechnungAuftragszuordnungAnteilsmaessigKopieren(
			EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungDto,
			Integer eingangangsrechnungIId, TheClientDto theClientDto);
	public void eingangsrechnungAuftragszuordnungExaktKopieren(
			EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungDto,
			Integer eingangangsrechnungIId, TheClientDto theClientDto);
	public Integer getZahlungsmoralZuEinemLieferanten(Integer lieferantIId,
			Date dVon,
			Date dBis,
			TheClientDto theClientDto);

	CoinRoundingResult calcMwstBetragFromBrutto(EingangsrechnungDto erDto, TheClientDto theClientDto);
	CoinRoundingResult calcMwstBetragFromNetto(EingangsrechnungDto erDto, TheClientDto theClientDto);

	EingangsrechnungDto getZuletztErstellteEingangsrechnung(String mandantCnr);

	EingangsrechnungDto getZuletztErstellteEingangsrechnung(Integer geschaeftsjahr, String mandantCnr);

	ErImportItemList20475 importXls20475(byte[] xlsFile, boolean checkOnly, TheClientDto theClientDto);

	ErImportItem20475 importXls20475(ErImportItem20475 item, TheClientDto theClientDto) throws RemoteException;

	EingangsrechnungDto createEingangsrechnungMitDokument(
			EingangsrechnungDto erDto, Integer orderId, JCRDocDto jcrDto, TheClientDto theClientDto) throws RemoteException;

	List<EingangsrechnungDto> eingangsrechnungFindByBelegdatumVonBis(Date von, Date bis, TheClientDto theClientDto);
	
	public JxlImportErgebnis importiereEingangsrechnungXLS(byte[] xlsFile,  TheClientDto theClientDto);
	
	public void updateEingangsrechnungFreigabedatum(Integer eingangsrechnungIId, java.sql.Date dFreigabedatum, TheClientDto theClientDto);

	ErZahlungsempfaenger getErZahlungsempfaenger(EingangsrechnungId eingangsrechnungId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void toggleEingangsrechnungGeprueft(Integer eingangsrechnungIId, TheClientDto theClientDto);
	
	
}
