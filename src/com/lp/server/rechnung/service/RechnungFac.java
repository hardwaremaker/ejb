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
package com.lp.server.rechnung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Remote;
import javax.naming.NamingException;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

@Remote
public interface RechnungFac extends IAktivierbarControlled {

	/**
	 * Die m&oumlglichen Gutschriftarten
	 */
	public enum Gutschriftart {
		/**
		 * Eine Mengengutschrift
		 */
		GUTSCHRIFT {
			@Override
			public String asCnr() {
				return RechnungFac.RECHNUNGART_GUTSCHRIFT;
			}
		},
		/**
		 * Eine Wertgutschrift
		 */
		WERTGUTSCHRIFT {
			@Override
			public String asCnr() {
				return RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;
			}
		};

		public abstract String asCnr();

		public static Gutschriftart fromCnr(String cnr) {
			if (cnr == null)
				throw new NullPointerException("cnr");
			for (Gutschriftart art : values()) {
				if (art.asCnr().equalsIgnoreCase(cnr))
					return art;
			}
			throw new IllegalArgumentException("Unknown value '" + cnr + "'");
		}
	}

	public static final String LOCKME_RECHNUNG = "lockme_rechnung";

	public static final int ANZAHL_KRITERIEN = 2;
	public static final int IDX_KRIT_JAHR = 0;
	public static final int IDX_KRIT_AUSWERTUNG = 1;

	public static final String KRIT_MIT_GUTSCHRIFTEN = "mit Gutschriften";
	public static final String KRIT_OHNE_GUTSCHRIFTEN = "ohne Gutschriften";

	public static final String KRIT_JAHR_KALENDERJAHR = "Kalenderjahr";
	public static final String KRIT_JAHR_GESCHAEFTSJAHR = "Geschaeftsjahr";

	// Stati einer Rechnung
	public static final String STATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String STATUS_BEZAHLT = LocaleFac.STATUS_BEZAHLT;
	public static final String STATUS_TEILBEZAHLT = LocaleFac.STATUS_TEILBEZAHLT;
	public static final String STATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String STATUS_VERBUCHT = LocaleFac.STATUS_VERBUCHT;
	public static final String STATUS_OFFEN = LocaleFac.STATUS_OFFEN;

	// Positionsarten
	public static final String POSITIONSART_RECHNUNG_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String POSITIONSART_RECHNUNG_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String POSITIONSART_RECHNUNG_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String POSITIONSART_RECHNUNG_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;
	public static final String POSITIONSART_RECHNUNG_POSITION = LocaleFac.POSITIONSART_POSITION;
	public static final String POSITIONSART_RECHNUNG_LIEFERSCHEIN = LocaleFac.POSITIONSART_LIEFERSCHEIN;
	public static final String POSITIONSART_RECHNUNG_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;
	public static final String POSITIONSART_RECHNUNG_ZWISCHENSUMME = LocaleFac.POSITIONSART_ZWISCHENSUMME;
	public static final String POSITIONSART_RECHNUNG_TRANSPORTSPESEN = LocaleFac.POSITIONSART_TRANSPORTSPESEN;
	public static final String POSITIONSART_RECHNUNG_URSPRUNGSLAND = LocaleFac.POSITIONSART_URSPRUNGSLAND;
	public static final String POSITIONSART_RECHNUNG_ANZAHLUNG = LocaleFac.POSITIONSART_ANZAHLUNG;
	public static final String POSITIONSART_RECHNUNG_VORAUSZAHLUNG = LocaleFac.POSITIONSART_VORAUSZAHLUNG;
	public static final String POSITIONSART_RECHNUNG_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String POSITIONSART_RECHNUNG_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String POSITIONSART_RECHNUNG_PAUSCHALPOSITION = LocaleFac.POSITIONSART_PAUSCHALPOSITION;
	public static final String POSITIONSART_RECHNUNG_REPARATUR = LocaleFac.POSITIONSART_REPARATUR;
	public static final String POSITIONSART_RECHNUNG_RUECKSCHEIN = LocaleFac.POSITIONSART_RUECKSCHEIN;
	public static final String POSITIONSART_RECHNUNG_GUTSCHRIFT = LocaleFac.POSITIONSART_GUTSCHRIFT;
	public static final String POSITIONSART_RECHNUNG_FREMDARTIKEL = LocaleFac.POSITIONSART_FREMDARTIKEL;
	public static final String POSITIONSART_RECHNUNG_ENDSUMME = LocaleFac.POSITIONSART_ENDSUMME;
	public static final String POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME = LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME;
	public static final String POSITIONSART_RECHNUNG_AUFTRAGSDATEN = LocaleFac.POSITIONSART_AUFTRAGSDATEN; // nicht
	// in
	// der
	// DB
	// ,
	// wied
	// nur
	// zum
	// Drucken
	// verwendet
	public static final String POSITIONSART_RECHNUNG_LS_BEGINN = "LSBeginn       "; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String POSITIONSART_RECHNUNG_LS_ENDE = "LSEnde       "; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String POSITIONSART_RECHNUNG_SERIENNR = "SERIENNR"; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String POSITIONSART_RECHNUNG_CHARGENR = "CHARGENR"; // diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken

	// Rechnungstypen
	public static final String RECHNUNGTYP_RECHNUNG = "Rechnung";
	public static final String RECHNUNGTYP_GUTSCHRIFT = "Gutschrift";
	public static final String RECHNUNGTYP_PROFORMARECHNUNG = "Proformare";

	// Rechnungsarten
	public static final String RECHNUNGART_ANZAHLUNG = "Anzahlungsrechnung";
	public static final String RECHNUNGART_INNERGEMEINSCHAFTLICHE_VERBRINGUNG = "InnergemeinschaftlicheVerbring";
	public static final String RECHNUNGART_RECHNUNG = "Rechnung";
	public static final String RECHNUNGART_SCHLUSSZAHLUNG = "Schlussrechnung";
	public static final String RECHNUNGART_GUTSCHRIFT = "Gutschrift";
	public static final String RECHNUNGART_WERTGUTSCHRIFT = "Wertgutschrift";
	public static final String RECHNUNGART_PROFORMARECHNUNG = "Proformarechnung";

	// Rechnungtext
	public static final String RECHNUNGTEXT_KOPF = "Kopftext";
	public static final String RECHNUNGTEXT_FUSS = "Fusstext";

	// Zahlungsarten
	public static final String ZAHLUNGSART_BANK = "Bank";
	public static final String ZAHLUNGSART_BAR = "Bar";
	public static final String ZAHLUNGSART_GUTSCHRIFT = "Gutschrift";
	public static final String ZAHLUNGSART_GEGENVERRECHNUNG = "Gegenverrechnung";
	public static final String ZAHLUNGSART_VORAUSZAHLUNG = "Vorauszahlung";

	// FLR-spalten
	public static final String FLR_RECHNUNGART_C_NR = "c_nr";
	public static final String FLR_RECHNUNGART_RECHNUNGTYP_C_NR = "rechnungtyp_c_nr";

	public static final String FLR_RECHNUNG_I_ID = "i_id";
	public static final String FLR_RECHNUNG_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_RECHNUNG_I_GESCHAEFTSJAHR = "i_geschaeftsjahr";
	public static final String FLR_RECHNUNG_C_NR = "c_nr";
	public static final String FLR_RECHNUNG_C_BEZ = "c_bez";
	public static final String FLR_RECHNUNG_D_BELEGDATUM = "d_belegdatum";
	public static final String FLR_RECHNUNG_WAEHRUNG_C_NR = "waehrung_c_nr";
	public static final String FLR_RECHNUNG_STATUS_C_NR = "status_c_nr";
	public static final String FLR_RECHNUNG_N_WERTFW = "n_wertfw";
	public static final String FLR_RECHNUNG_N_KURS = "n_kurs";
	public static final String FLR_RECHNUNG_AUFTRAG_I_ID = "auftrag_i_id";
	public static final String FLR_RECHNUNG_T_BEZAHLTDATUM = "t_bezahltdatum";
	public static final String FLR_RECHNUNG_T_LETZTESMAHNDATUM = "t_letztesmahndatum";
	public static final String FLR_RECHNUNG_T_MAHNSPERREBIS = "t_mahnsperrebis";
	public static final String FLR_RECHNUNG_T_STORNIERT = "t_storniert";
	public static final String FLR_RECHNUNG_T_FIBUUEBERNAHME = "t_fibuuebernahme";
	public static final String FLR_RECHNUNG_KUNDE_I_ID = "kunde_i_id";
	public static final String FLR_RECHNUNG_VERTRETER_I_ID = "vertreter_i_id";
	public static final String FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE = "kunde_i_id_statistikadresse";
	public static final String FLR_RECHNUNG_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_RECHNUNG_ZAHLUNGSZIEL_I_ID = "zahlungsziel_i_id";
	public static final String FLR_RECHNUNG_FLRKUNDE = "flrkunde";
	public static final String FLR_RECHNUNG_FLRRECHNUNGART = "flrrechnungart";
	public static final String FLR_RECHNUNG_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_RECHNUNG_FLRPERSONALANLEGER = "flrpersonalanleger";
	public static final String FLR_RECHNUNG_FLRVERTRETER = "flrvertreter";
	public static final String FLR_RECHNUNG_FLRSTATISTIKADRESSE = "flrstatistikadresse";
	public static final String FLR_RECHNUNG_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_RECHNUNG_T_FAELLIGKEIT = "t_faelligkeit";

	public static final String FLR_RECHNUNGPOSITION_I_ID = "i_id";
	public static final String FLR_RECHNUNGPOSITION_I_SORT = "i_sort";
	public static final String FLR_RECHNUNGPOSITION_RECHNUNG_I_ID = "rechnung_i_id";
	public static final String FLR_RECHNUNGPOSITION_RECHNUNG_I_ID_GUTSCHRIFT = "rechnung_i_id_gutschrift";
	public static final String FLR_RECHNUNGPOSITION_AUFTRAGPOSITION_I_ID = "auftragposition_i_id";
	public static final String FLR_RECHNUNGPOSITION_POSITIONSART_C_NR = "positionsart_c_nr";
	public static final String FLR_RECHNUNGPOSITION_ARTIKEL_I_ID = "artikel_i_id";
	public static final String FLR_RECHNUNGPOSITION_C_BEZ = "c_bez";
	public static final String FLR_RECHNUNGPOSITION_C_ZUSATZBEZEICHNUNG = "c_zusatzbezeichnung";
	public static final String FLR_RECHNUNGPOSITION_X_TEXTINHALT = "x_textinhalt";
	public static final String FLR_RECHNUNGPOSITION_C_TEXTINHALT = "c_textinhalt";
	public static final String FLR_RECHNUNGPOSITION_N_MENGE = "n_menge";
	public static final String FLR_RECHNUNGPOSITION_N_EINZELPREIS = "n_einzelpreis";
	public static final String FLR_RECHNUNGPOSITION_N_EINZELPREIS_PLUS_AUFSCHLAG = "n_einzelpreis_plus_aufschlag";
	public static final String FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS = "n_nettoeinzelpreis";
	public static final String FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS_PLUS_AUFSCHLAG = "n_nettoeinzelpreis_plus_aufschlag";
	public static final String FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS_PLUS_AUFSCHLAG_MINUS_RABATT = "n_nettoeinzelpreis_plus_aufschlag_minus_rabatt";
	public static final String FLR_RECHNUNGPOSITION_F_RABATTSATZ = "f_rabattsatz";
	public static final String FLR_RECHNUNGPOSITION_N_BRUTTOEINZELPREIS = "n_bruttoeinzelpreis";
	public static final String FLR_RECHNUNGPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_RECHNUNGPOSITION_FLRPOSITIONENSICHTAUFTRAG = "flrpositionensichtauftrag";
	public static final String FLR_RECHNUNGPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_RECHNUNGPOSITION_FLRRECHNUNG = "flrrechnung";
	public static final String FLR_RECHNUNGPOSITION_FLRLIEFERSCHEIN = "flrlieferschein";
	public static final String FLR_RECHNUNGPOSITION_FLRMWSTSATZ = "flrmwstsatz";

	public static final String FLR_RECHNUNG_ZAHLUNG_I_ID = "i_id";
	public static final String FLR_RECHNUNG_ZAHLUNG_RECHNUNG_I_ID = "rechnung_i_id";
	public static final String FLR_RECHNUNG_ZAHLUNG_BANKVERBINDUNG_I_ID = "bankverbindung_i_id";
	public final static String FLR_RECHNUNG_ZAHLUNG_D_ZAHLDATUM = "d_zahldatum";
	public final static String FLR_RECHNUNG_ZAHLUNG_ZAHLUNGSART_C_NR = "zahlungsart_c_nr";
	public final static String FLR_RECHNUNG_ZAHLUNG_N_KURS = "n_kurs";
	public final static String FLR_RECHNUNG_ZAHLUNG_N_BETRAG = "n_betrag";
	public final static String FLR_RECHNUNG_ZAHLUNG_N_BETRAGFW = "n_betragfw";
	public final static String FLR_RECHNUNG_ZAHLUNG_N_BETRAG_UST = "n_betrag_ust";
	public final static String FLR_RECHNUNG_ZAHLUNG_N_BETRAG_USTFW = "n_betrag_ustfw";
	public final static String FLR_RECHNUNG_ZAHLUNG_I_AUSZUG = "i_auszug";
	public final static String FLR_RECHNUNG_ZAHLUNG_FLRBANKVERBINDUNG = "flrbankverbindung";
	public final static String FLR_RECHNUNG_ZAHLUNG_FLRKASSENBUCH = "flrkassenbuch";
	public static final String FLR_RECHNUNG_ZAHLUNG_FLRRECHNUNG = "flrrechnung";

	public final static String FLR_RECHNUNGPOSITIONSART_POSITIONSART_C_NR = "positionsart_c_nr";
	// sprmapping2: 1: die konstante wie ueblich
	public final static String FLR_RECHNUNGPOSITIONSART_RECHNUNGPOSITIONSART_POSITIONSART_SET = "rechnungpositionsart_positionsart_set";
	public final static String FLR_RECHNUNGPOSITIONSART_I_SORT = "i_sort";

	public final static String FLR_GUTSCHRIFTPOSITIONSART_POSITIONSART_C_NR = "positionsart_c_nr";
	public final static String FLR_GUTSCHRIFTPOSITIONSART_GUTSCHRIFTPOSITIONSART_POSITIONSART_SET = "gutschriftpositionsart_positionsart_set";
	public final static String FLR_GUTSCHRIFTPOSITIONSART_I_SORT = "i_sort";

	public final static String FLR_PROFORMARECHNUNGPOSITIONSART_POSITIONSART_C_NR = "positionsart_c_nr";
	public final static String FLR_PROFORMARECHNUNGPOSITIONSART_PROFORMARECHNUNGPOSITIONSART_POSITIONSART_SET = "proformarechnungpositionsart_positionsart_set";
	public final static String FLR_PROFORMARECHNUNGPOSITIONSART_I_SORT = "i_sort";

	public final static String FLR_KONTIERUNG_I_ID = "i_id";
	public final static String FLR_KONTIERUNG_RECHNUNG_I_ID = "rechnung_i_id";
	public final static String FLR_KONTIERUNG_N_PROZENTSATZ = "n_prozentsatz";
	public final static String FLR_KONTIERUNG_FLRKOSTENSTELLE = "flrkostenstelle";
	public final static String FLR_KONTIERUNG_FLRRECHNUNG = "flrrechnung";

	public final static int FLR_SPALTE_GS_UMSATZ_I_ID = 0;
	public final static int FLR_SPALTE_GS_UMSATZ_HEADER = 1;
	public final static int FLR_SPALTE_GS_UMSATZ_OFFEN_BRUTTO = 2;
	public final static int FLR_SPALTE_GS_UMSATZ_OFFEN_NETTO = 3;
	public final static int FLR_SPALTE_GS_UMSATZ_LEER = 4;
	public final static int FLR_SPALTE_GS_UMSATZ_UMSATZ_BRUTTO = 5;
	public final static int FLR_SPALTE_GS_UMSATZ_UMSATZ_NETTO = 6;

	public final static int FLR_SPALTE_RE_UMSATZ_I_ID = 0;
	public final static int FLR_SPALTE_RE_UMSATZ_HEADER = 1;
	public final static int FLR_SPALTE_RE_UMSATZ_OFFEN_BRUTTO = 2;
	public final static int FLR_SPALTE_RE_UMSATZ_OFFEN_NETTO = 3;
	public final static int FLR_SPALTE_RE_UMSATZ_LEER1 = 4;
	public final static int FLR_SPALTE_RE_UMSATZ_UMSATZ_BRUTTO = 5;
	public final static int FLR_SPALTE_RE_UMSATZ_UMSATZ_NETTO = 6;
	public final static int FLR_SPALTE_RE_UMSATZ_LEER2 = 7;
	public final static int FLR_SPALTE_RE_UMSATZ_ANZAHLUNG_BRUTTO = 8;
	public final static int FLR_SPALTE_RE_UMSATZ_ANZAHLUNG_NETTO = 9;

	public RechnungDto createRechnung(RechnungDto rechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungDto updateRechnung(RechnungDto rechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createRechnungAusAuftrag(Integer auftragIId, java.sql.Date tBelegdatum,
			Double dRabattAusRechnungsadresse, boolean bSchlussrechnung, TheClientDto theClientDto) throws EJBExceptionLP;

	public Integer createAnzahlungsrechnungAusAuftrag(Integer auftragIId, java.sql.Date tBelegdatum, TheClientDto theClientDto);
	
	public void updateRechnungBeimZusammenfuehren(RechnungDto rechnungDto) throws EJBExceptionLP, RemoteException;

	public void sortiereNachLieferscheinAnsprechpartner(Integer iIdRechnungI, TheClientDto theClientDto);

	public RechnungDto rechnungFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public RechnungDto rechnungFindByPrimaryKeyOhneExc(Integer iId);

	// public RechnungDto[] rechnungFindByPartnerIIdRechnungsadresseMandantCNr(
	// Integer partnerIId, String mandantCNr) throws EJBExceptionLP,
	// RemoteException;
	//
	// public RechnungDto[]
	// rechnungFindByPartnerIIdRechnungsadresseMandantCNrOhneExc(
	// Integer partnerIId, String mandantCNr) throws EJBExceptionLP,
	// RemoteException;

	public RechnungDto[] rechnungFindByKundeIIdMandantCNr(Integer kundeIId, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public RechnungDto[] rechnungFindByKundeIIdMandantCNrOhneExc(Integer kundeIId, String mandantCNr)
			throws RemoteException;

	public RechnungDto[] rechnungFindByKundeIIdStatistikadresseMandantCNr(Integer kundeIId, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public RechnungDto[] rechnungFindByKundeIIdStatistikadresseMandantCNrOhneExc(Integer kundeIId, String mandantCNr)
			throws RemoteException;

	// public BigDecimal berechneRechnungswertNettoInRechnungswaehrung(Integer
	// id)
	// throws RemoteException;

	public RechnungPositionDto createRechnungPosition(RechnungPositionDto rePosDto, Integer lagerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto createRechnungPosition(RechnungPositionDto rePosDto, Integer lagerIId,
			boolean bNichtMengenbehafteteAuftragspositionErledigen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto createRechnungPosition(RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeRechnungPosition(RechnungPositionDto rechnungPositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void sortiereNachLieferscheinNummer(Integer iIdRechnungI, TheClientDto theClientDto);

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, Artikelset artikelset, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto rechnungPositionFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto rechnungPositionFindByPrimaryKeyOhneExc(Integer iId);

	public java.util.Date getDatumLetzterZahlungseingang(Integer rechnungIId);

	public RechnungPositionDto[] rechnungPositionFindByRechnungIId(Integer rechnungIId)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto[] rechnungPositionFindByArtikelIId(Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	public Integer getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(Integer iIdRechnungI,
			Integer lieferscheinIId, TheClientDto theClientDto);

	public RechnungDto[] rechnungFindByBelegdatumVonBis(String mandantCNr, Date dVon, Date dBis) throws RemoteException;

	public RechnungDto[] rechnungFindAll() throws RemoteException;

	public RechnungDto[] rechnungFindByAuftragIId(Integer auftragIId) throws EJBExceptionLP, RemoteException;

	public RechnungzahlungDto createZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZahlung(RechnungzahlungDto zahlungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungzahlungDto zahlungFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public RechnungzahlungDto[] zahlungFindByRechnungIId(Integer rechnungIId) throws RemoteException;

	public Integer getZahlungsmoraleinesKunden(Integer kundeIId, boolean bStatistikadresse, TheClientDto theClientDto)
			throws RemoteException;

	public boolean gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(Integer auftragIId, TheClientDto theClientDto);

	public String getGutschriftenEinerRechnung(Integer rechnungIId);

	public Integer createRechnungAusLieferschein(Integer lieferscheinIId, RechnungDto rechnungDto,
			String rechnungstypCNr, java.sql.Date neuDatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createRechnungAusMehrereLieferscheine(Object[] keys, RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createRechnungAusMehrereLieferscheine(Object[] keys, RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum,boolean lsPreiseMitABStaffelpreisenUpdaten, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public void setRechnungStatusAufAngelegt(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId, Integer zahlungIIdAusgenommen,
			java.util.Date tStichtag);

	public BigDecimal getBereitsBezahltWertVonRechnungUst(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnungUst(Integer rechnungIId, Integer zahlungIIdAusgenommen,
			java.util.Date tStichtag);

	public Integer createRechnungAusAuftrag(Integer auftragIId, Double dRabattAusRechnungsadresse,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createRechnungAusAngebot(Integer angebotIId, java.sql.Date neuDatum, TheClientDto theClientDto);

	public Integer createRechnungAusRechnung(Integer rechnungIId, java.sql.Date neuDatum,
			boolean bUebernimmKonditionenDesKunden, TheClientDto theClientDto, boolean bZahlungsplanUebernehmen);

	public Integer createGutschriftAusRechnung(Integer rechnungIId, java.sql.Date dBelegdatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createGutschriftAusRechnung(Integer rechnungIId, java.sql.Date dBelegdatum, String gutschriftartCnr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void mahneRechnung(Integer rechnungIId, Integer mahnstufeIId, Date dMahndatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean setzeMahnstufeZurueck(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void vertauscheRechnungspositionenMinus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void vertauscheRechnungspositionenPlus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public Boolean hatRechnungPositionen(Integer rechnungIId) throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeMahnsperre(Integer rechnungIId, Date tMahnsperre, String cMahnungsanmerkung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void aktiviereRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void storniereRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getRechnungswertInRechnungswaehrung(RechnungDto rechnungDto, RechnungPositionDto[] allePositionen,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public void storniereRechnungRueckgaengig(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void fuehreArtikelZusammen(Integer artikelIIdAlt, Integer artikelIIdNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneSummeOffenBrutto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeOffenNetto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, Integer kundeIId, boolean bStatistikadresse, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal berechneSummeUmsatzBrutto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bOhneAndereMandanten, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeUmsatzNetto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bOhneAndereMandanten, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeAnzahlungBrutto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bNurNichtabgerechnete, boolean bOhneAndereMandanten,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeAnzahlungNetto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bNurNichtabgerechnete, boolean bOhneAndereMandanten,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal getUmsatzVomKundenImZeitraum(TheClientDto theClientDto, Integer kundeIId, Date dVon, Date dBis,
			boolean bStatistikadresse) throws EJBExceptionLP, RemoteException;

	public BigDecimal getUmsatzVomKundenHeuer(TheClientDto theClientDto, Integer kundeIId, boolean bStatistikadresse,
			boolean geschaeftsjahr) throws EJBExceptionLP, RemoteException;

	public BigDecimal getUmsatzVomKundenVorjahr(TheClientDto theClientDto, Integer kundeIId, boolean bStatistikadresse,
			boolean geschaeftsjahr) throws EJBExceptionLP, RemoteException;

	public Integer getAnzahlDerRechnungenVomKundenImZeitraum(TheClientDto theClientDto, Integer kundeIId, Date dVon,
			Date dBis, boolean bStatistikadresse) throws EJBExceptionLP, RemoteException;

	public Integer getAnzahlDerRechnungenVomKundenHeuer(TheClientDto theClientDto, Integer kundeIId,
			boolean bStatistikadresse, boolean geschaeftsjahr) throws EJBExceptionLP, RemoteException;

	public Integer getAnzahlDerRechnungenVomKundenVorjahr(TheClientDto theClientDto, Integer kundeIId,
			boolean bStatistikadresse, boolean geschaeftsjahr) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneSummeGutschriftOffenBrutto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeGutschriftOffenNetto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeGutschriftUmsatzBrutto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneSummeGutschriftUmsatzNetto(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) throws RemoteException;

	public void bucheRechnungPositionAmLager(RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			boolean bLoescheBuchung, TheClientDto theClientDto);

	public void setzeRechnungFibuUebernahme(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeRechnungFibuUebernahmeRueckgaengig(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/*
	 * public BigDecimal berechneEinzelpreisLSPosition(RechnungDto rechnungDto,
	 * LieferscheinDto lieferscheinDto, LieferscheinpositionDto lsPosDto, boolean
	 * bInklRabatt, TheClientDto theClientDto) throws EJBExceptionLP,
	 * RemoteException;
	 *
	 * public BigDecimal berechneNettopreisLSPosition(RechnungDto rechnungDto,
	 * LieferscheinDto lieferscheinDto, LieferscheinpositionDto lsPosDto,
	 * TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	 *
	 * public BigDecimal berechneNettopreisLSPositionOhneLSRabatt(RechnungDto
	 * rechnungDto, LieferscheinDto lieferscheinDto, LieferscheinpositionDto
	 * lsPosDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	 *
	 * public BigDecimal berechneEinzelpreisLSPositionOhneLSRabatt(RechnungDto
	 * rechnungDto, LieferscheinDto lieferscheinDto, LieferscheinpositionDto
	 * lsPosDto, boolean bInklRabatt, TheClientDto theClientDto) throws
	 * EJBExceptionLP , RemoteException;
	 */
	public RechnungkontierungDto createRechnungkontierung(RechnungkontierungDto rechnungkontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeRechnungkontierung(RechnungkontierungDto rechnungkontierungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungkontierungDto updateRechnungkontierung(RechnungkontierungDto rechnungkontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public RechnungkontierungDto rechnungkontierungFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public RechnungkontierungDto[] rechnungkontierungFindByRechnungIId(Integer rechnungIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getProzentsatzKontiert(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createRechnungenAusWiederholungsauftrag(Integer auftragIId, java.sql.Date dNeuDatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public java.sql.Date getWiederholungsTermin(Integer auftragIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<Integer> getAngelegteRechnungen(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto getRechnungPositionByRechnungAuftragposition(Integer iIdRechnungI,
			Integer iIdAuftragpositionI) throws EJBExceptionLP, RemoteException;

//	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
//			Integer iIdRechnungI, Integer auftragIIdI, TheClientDto theClientDto)
//			throws EJBExceptionLP, RemoteException;

	public ArrayList<RechnungPositionDto> uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdRechnungI, Integer auftragIIdI, List<Artikelset> artikelsets, List<Integer> auftragspositionIIds,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateRechnungpositionSichtAuftrag(RechnungPositionDto rechnungpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneVerkaufswertIst(RechnungDto reDto, String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneVerkaufswertIst(RechnungDto reDto, Integer auftragIId, String sArtikelartI,
			TheClientDto theClientDto);

	public BigDecimal berechneGestehungswertOderEinstandwertIst(RechnungDto reDto, String sArtikelartI,
			boolean bGestehungswert, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGestehungswertOderEinstandwertIst(RechnungDto reDto, Integer auftragIId,
			String sArtikelartI, boolean bGestehungswert, TheClientDto theClientDto) throws EJBExceptionLP;

	public BigDecimal berechneGestehungswertEinerRechnungsposition(RechnungPositionDto rechnungpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean enthaeltRechnungspositionLagerbewirtschaftetenArtikel(Integer iIdRechnungpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean pruefePositionenAufSortierungNachAuftrag(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String pruefeRechnungswert(TheClientDto theClientDto);

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void sortierePositionenNachAuftrag(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeRechnung(Integer iId) throws EJBExceptionLP, RemoteException;

	public void removeRechnung(RechnungDto rechnungDto) throws EJBExceptionLP, RemoteException;

	public void updateRechnungService(RechnungDto rechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateRechnungStatus(Integer rechnungIId, String statusNeu, java.sql.Date bezahltdatum);

	public void updateRechnungVertreter(Integer rechnungIId, Integer personalIIdVertreter_Neu,
			TheClientDto theClientDto);

	public void updateRechnungsService(RechnungDto[] rechnungDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungDto[] rechnungFindByMandantBelegdatumVonBis(String mandantCNr, Date dVon, Date dBis)
			throws EJBExceptionLP, RemoteException;

	// public RechnungDto rechnungFindByCNrMandant(String cNr, String
	// mandantCNr)
	// throws EJBExceptionLP, RemoteException;

	public RechnungDto[] rechnungFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr);

	public RechnungDto[] rechnungFindByAnsprechpartnerIId(Integer iAnsprechpartnerIId)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto[] getRechnungPositionenByRechnungIId(Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createRechnungzahlung(RechnungzahlungDto rechnungzahlungDto) throws EJBExceptionLP, RemoteException;

	public void removeRechnungzahlung(Integer iId) throws EJBExceptionLP, RemoteException;

	public void removeRechnungzahlung(RechnungzahlungDto rechnungzahlungDto) throws EJBExceptionLP, RemoteException;

	public void updateRechnungzahlung(RechnungzahlungDto rechnungzahlungDto) throws EJBExceptionLP, RemoteException;

	public void updateRechnungzahlungs(RechnungzahlungDto[] rechnungzahlungDtos) throws EJBExceptionLP, RemoteException;

	public RechnungzahlungDto rechnungzahlungFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public RechnungzahlungDto[] zahlungFindByRechnungIIdAbsteigendSortiert(Integer rechnungIId)
			throws EJBExceptionLP, RemoteException;

	public List<Integer> rechnungzahlungIdsByMandantZahldatumVonBis(String mandantCNr, Date dVon, Date dBis)
			throws EJBExceptionLP, RemoteException;;

	public LieferscheinpositionDto lieferscheinpositionWaehrungUmrechnen(RechnungDto rechnungDto,
			TheClientDto theClientDto, LieferscheinDto lsDto, LieferscheinpositionDto lsPosDto) throws RemoteException;

	public RechnungPositionDto rechnungPositionFindPositionIIdISort(Integer positionIId, Integer iSort)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto[] rechnungPositionFindByRechnungIIdArtikelIId(Integer positionIId, Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	public Integer getLastPositionNummer(Integer reposIId) throws EJBExceptionLP, RemoteException;

	public Integer getPositionNummer(Integer reposIId) throws EJBExceptionLP, RemoteException;

	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId, Integer position);

//	public RechnungzahlungDto zahlungMitFibuKommentarByPrimaryKey(Integer zahlungId,
//			TheClientDto theClientDto);

	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer ermitteln
	 *
	 * @param rechnungIId die RechnungsIId fuer die die hoechste Pos.Nummer
	 *                    ermittelt werden soll.
	 *
	 * @return 0 ... n
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId) throws EJBExceptionLP;

	public RechnungPositionDto rechnungPositionFindByLieferscheinIId(Integer lieferscheinIId)
			throws EJBExceptionLP, RemoteException;

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId, Integer belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeVersandzeitpunktAufJetzt(Integer iRechnungIId, String sDruckart);

	public RechnungDto[] rechnungFindByAuftragIIdTBelegdatum(Integer auftragIId, java.sql.Date aktuellerTermin)
			throws EJBExceptionLP, RemoteException;

	public void updateRechnungZahlungsplan(Integer rechnungIId, BigDecimal bdZahlbetrag, Integer iZahltag);

	public void createMonatsrechnungen(TheClientDto theClientDto);

	public void verbucheRechnungNeu(Integer iRechnungIId, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public void verbucheGutschriftNeu(Integer iRechnungIId, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId, Integer zahlungIIdAusgenommen,
			java.util.Date tStichtag);

	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(Integer rechnungIId, Integer zahlungIIdAusgenommen,
			java.util.Date tStichtag);

	public BigDecimal getBezahltKursdifferenzBetrag(Integer id, BigDecimal kurs);

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer rechnungIId);

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer rechnungIId);

	public BigDecimal getAnzahlungenZuSchlussrechnungBrutto(Integer rechnungIId);

	public RechnungDto[] rechnungFindByRechnungIIdZuRechnung(Integer rechnungIId);

	/**
	 * Prueft, ob fuer alle Rechnungspositionen zwischen den beiden angegebenen
	 * Positionsnummern der gleiche Mehrwertsteuersatz definiert ist.
	 *
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId, Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP;

	public RechnungDto[] rechnungFindByLieferscheinIId(Integer lieferscheinIId);

	public void toggleZollpapiereErhalten(Integer rechnungIId, String cZollpapier, TheClientDto theClientDto);

	public RechnungPositionDto[] getArtikelsetForIId(Integer kopfartikelIId);

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(Integer rechnungposIId)
			throws EJBExceptionLP;

	public int getAnzahlMengenbehafteteRechnungpositionen(Integer rechnungIId, TheClientDto theClientDto);

	/**
	 * Eine Liste aller RechnungIids des Mandanten, in denen IZwischensummen
	 * vorkommen
	 *
	 * @param theClientDto
	 * @return eine (leere) Liste aller RechnungsIIds des Mandanten mit
	 *         IZwischensummenpositionen
	 */
	List<Integer> repairRechnungZws2276GetList(TheClientDto theClientDto);

	/**
	 * F&uuml;r die angegebene RechnungId f&uuml;r die Zwischensummenposition den
	 * EinzelpreisMitAufschlagMitRabatten korrieren. Zus&auml;tzlich die
	 * Lagerbuchung um den entsprechenden Preis korrigieren.
	 *
	 * @param rechnungId
	 * @param theClientDto
	 */
	void repairRechnungZws2276(Integer rechnungId, TheClientDto theClientDto);

	/**
	 * Eine Rechnungzahlung durchf&uuml;hren</br>
	 * <p>
	 * Eine optionale Gutschriftzahlung wird ber&uuml;cksichtigt
	 * </p>
	 * 
	 * @param rechnungZahlungDto   ist die Id gesetzt, wird ein update dieser
	 *                             Zahlung durchgef&uuml;hrt
	 * @param rechnungErledigt     true wenn die Rechnung als bezahlt angesehen
	 *                             werden kann
	 * @param gutschriftZahlungDto kann null sein, wenn es keine Gutschriftszahlung
	 *                             gibt. Ist die id gesetzt, wird ein update der
	 *                             Zahlung ausgef&uuml;hrt, ansonsten eine neue
	 *                             Gutschriftzahlung erzeugt
	 * @param gutschriftErledigt   true wenn die Gutschrift als bezahlt angesehen
	 *                             werden kann
	 * @return die erzeugte Rechnungzahlung
	 */
	RechnungzahlungDto createUpdateZahlung(RechnungzahlungDto rechnungZahlungDto, boolean rechnungErledigt,
			RechnungzahlungDto gutschriftZahlungDto, boolean gutschriftErledigt, TheClientDto theClientDto);

	public RechnungPositionDto[] rechnungPositionByAuftragposition(Integer iIdAuftragpositionI);

	public BigDecimal getAnzahlungenZuSchlussrechnungUst(Integer rechnungIId);

	/**
	 * Hat eine Rechnung Positionen die einen auftragsbezug haben?
	 *
	 * @param rechnungId die zu untersuchende Rechnung
	 * @return true wenn die Rechnung Positionen mit Auftragsbezug enth&auml;lt
	 * @throws EJBExceptionLP
	 */
	Boolean hatRechnungPositionenMitAuftragsbezug(Integer rechnungId) throws EJBExceptionLP;

	/**
	 * Eine Rechnung aus einer Rechnung erzeugen
	 *
	 * @param rechnungIId
	 * @param neuDatum
	 * @param bUebernimmKonditionenDesKunden
	 * @param auftragspositionenKopieren     wenn true werden auch auftragsbezogene
	 *                                       Rechnungspositionen kopiert, wobei der
	 *                                       Auftragsbezug selbst nicht
	 *                                       &uuml;bernommen wird
	 * @param theClientDto
	 * @return die Id der neu erzeugten Rechnung
	 */
	Integer createRechnungAusRechnung(Integer rechnungIId, java.sql.Date neuDatum,
			boolean bUebernimmKonditionenDesKunden, boolean auftragspositionenKopieren, TheClientDto theClientDto,
			boolean bZahlungsplanUebernehmen);

	/**
	 * Liefert eine Liste jener Stati, die es einem erlauben, zu einer Rechnung eine
	 * Rechnungzahlung durchzufuehren
	 * 
	 * @return Liste der erlaubten Stati
	 */
	public List<String> getErlaubteStatiFuerRechnungZahlung();

	public BigDecimal getWertUstAnteiligZuRechnungUst(Integer rechnungIId, BigDecimal bruttoBetrag);

	public BigDecimal getAnzahlungenZuSchlussrechnung(Integer rechnungIId);

	/**
	 * Enh&auml;lt die Rechnung Lieferscheinpositionen
	 * 
	 * @param rechnungId
	 * @return true wenn die Rechnung Lieferscheinpositionen enth&auml;lt
	 */
	public boolean hatRechnungLieferscheine(Integer rechnungId);

	/**
	 * Aufgrund der Rechnungsnummer (cnr) die Rechnung ermitteln</br>
	 * <p>
	 * Es wird explizit nur in der Rechnungsart Rechnung, Anzahlungs- oder
	 * Schlussrechnung nach der angegebenen cnr gesucht
	 * </p>
	 * 
	 * @param rechnungCnr die gesuchte Rechnungsnummer.
	 * @param mandantCNr  der Mandant in dem die Rechnung gesucht wird
	 * 
	 * @return eine (leere) Liste aller Rechnungen die dieser Rechnungsnummer
	 *         entsprechen. Die Liste kann eigentlich nur mehr 0 oder 1 Element
	 *         enthalten, sofern die Belegnummerngenerierung fuer Anzahlungs- oder
	 *         Schlussrechnungen nicht ge&auml;ndert wird
	 */
	public RechnungDto[] rechnungFindByRechnungartCNrMandantCNrOhneExc(String rechnungCnr, String mandantCNr);

	public void sortiereNachAuftragsnummer(Integer rechnungIId, TheClientDto theClientDto);

	String createRechnungElektronischPost(Integer rechnungIId, TheClientDto theClientDto)
			throws RemoteException, NamingException;

	void resetRechnungElektronisch(Integer rechnungId, TheClientDto theClientDto) throws RemoteException;

	IRechnungElektronisch createRechnungElektronisch(Integer rechnungId, TheClientDto theClientDto)
			throws NamingException, RemoteException;

	boolean hatRechnungVersandweg(RechnungDto rechnungDto, TheClientDto theClientDto) throws RemoteException;

	boolean hatRechnungVersandweg(Integer rechnungIId, TheClientDto theClientDto) throws RemoteException;

	boolean pruefeKonditionenLieferscheinzuRechnung(Integer rechnungIId, Integer lieferscheinIId,
			TheClientDto theClientDto);

	void uebernehmeKonditionenAusLieferschein(Integer rechnungIId, Integer lieferscheinIId, TheClientDto theClientDto);

	public Integer getZahlungsmoraleinesKunden(Integer kundeIId, boolean bStatistikadresse, Date dVon, Date dBis,
			TheClientDto theClientDto);

	KundeDto getKundeFuerRechnungAusLieferschein(Integer lieferscheinIId, String rechnungstypCNr,
			TheClientDto theClientDto);

	RechnungDto setupDefaultRechnung(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdRechnungI,
			int iSortierungNeuePositionI);

	public void updateRechnungStatistikadresse(Integer rechnungIId, Integer kundeIIdStatistikadresseNeu,
			TheClientDto theClientDto);

	public java.sql.Date berechneinterval(String sWiederholungsintervall, java.sql.Date dAktuellerTermin);

	public RechnungDto[] pruefeObChronologieDesBelegdatumsDerRechnungStimmt(Integer rechnungIId,
			TheClientDto theClientDto);

	public List<Integer> repairRechnungSP6402GetList(TheClientDto theClientDto) throws RemoteException;

	public void repairRechnungSP6402(Integer rechnungId, TheClientDto theClientDto) throws RemoteException;

	void archiviereZugferdResult(ZugferdResult zugferdResult, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungDto[] rechnungFindByAuftragIId_AuchUeberPositionen_TBelegdatum(Integer auftragIId,
			java.sql.Date aktuellerTermin, TheClientDto theClientDto);

	public void sortiereNachArtikelnummer(Integer rechnungIId, TheClientDto theClientDto);

	List<Integer> repairLieferscheinSP6999GetList(TheClientDto theClientDto) throws RemoteException;

	public RechnungDto istLieferscheinBereitsInProformarechnung(Integer lieferscheinIId, TheClientDto theClientDto);

	public VorkassepositionDto vorkassepositionFindByRechnungIIdAuftragspositionIId(Integer rechnungIId,
			Integer auftragpositonIId, TheClientDto theClientDto);

	public void removeVorkassepositionDto(VorkassepositionDto vorkassepositionDto, TheClientDto theClientDto);

	public Integer updateVorkasseposition(VorkassepositionDto vorkassepositionDto, TheClientDto theClientDto);

	public BigDecimal getSummeVorkasseposition(Integer rechnungIId, Integer auftragpositonIId,
			TheClientDto theClientDto);

	public void prozentsatzZuOffeneAnzahlungspositionenAbrechnen(Integer rechnungIId, BigDecimal bdProzentsatz,
			TheClientDto theClientDto);

	public void uebersteuereIntelligenteZwischensumme(Integer rechnungpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert, TheClientDto theClientDto);

	public void offeneLieferscheineMitABStaffelpreisenVerrechnen(Integer kundeIId_rechnungsadresse,
			java.sql.Date dStichtag, java.sql.Date neuDatum, TheClientDto theClientDto);

	public ArrayList<Integer> getKundeIIdsRechnungsadresseOffenerLierferscheine(java.sql.Date dStichtag,
			TheClientDto theClientDto);
	
	public ArrayList<RechnungDto> sindAnzahlungenVorhanden(Integer auftragIId, TheClientDto theClientDto);
	

}
