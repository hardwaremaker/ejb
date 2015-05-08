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
package com.lp.server.angebot.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebotpositionReport;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotUebersichtTabelleDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle im Angebot.
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 02.11.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AngebotUebersichtHandler extends UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 17;
	private final int iAnzahlSpalten = 6;

	// Spaltentypen der Uebersicht entscheiden ueber die Hibernate Criterias
	private final int IDX_GESAMT = 0;
	private final int IDX_OFFEN = 1;
	private final int IDX_OFFENTERMINBEWERTET = 2;
	private final int IDX_BESTELLT = 3;

	/**
	 * Konstruktor.
	 */
	public AngebotUebersichtHandler() {
		super();

		try {
			setAnzahlZeilen(iAnzahlZeilen);
			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 3758
		}
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			tableInfo = new TableInfo(new Class[] { String.class,String.class,
					BigDecimal.class, BigDecimal.class, BigDecimal.class,
					BigDecimal.class }, new String[] {"",
					" ",
					getTextRespectUISpr("lp.gesamt", theClientDto.getMandant(),
							theClientDto.getLocUi()),
					getTextRespectUISpr("er.offene", theClientDto.getMandant(),
							theClientDto.getLocUi()),
					getTextRespectUISpr("angb.offenetermin", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					getTextRespectUISpr("lp.bestellt", theClientDto.getMandant(),
							theClientDto.getLocUi()) }, new String[] {"", "", "", "",
					"", "" });
		}

		return tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            die markierte Zeile
	 * @return QueryResult Ergebnis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// die aktuellen Filter Kriterien bestimmen
			getFilterKriterien();

			FilterKriterium fkjahr = aFilterKriterium[AngebotFac.IDX_KRIT_GESCHAEFTSJAHR];
			init(fkjahr);
			FilterKriterium fkVertreter = aFilterKriterium[AngebotFac.IDX_KRIT_VERTRETER_I_ID];
			Integer iIdVertreter = null;
			if (fkVertreter.value != null) {
				iIdVertreter = new Integer(fkVertreter.value);
			}
			// das Vorjahr wird summiert dargestellt
			AngebotUebersichtTabelleDto oSummenVorjahr = null;
			// zuerste eine HashMap mit den darzustellenden Daten zusammenbauen
			TreeMap<Integer, AngebotUebersichtTabelleDto> hmSammelstelle = new TreeMap<Integer, AngebotUebersichtTabelleDto>();
			for (int i = 0; i < iAnzahlZeilen; i++) {

				String sZeilenHeader = null;

				GregorianCalendar gcBerechnungsdatumVonI = null;
				GregorianCalendar gcBerechnungsdatumBisI = null;

				BigDecimal nGesamt = null;
				BigDecimal nOffen = null;
				BigDecimal nOffenterminbewertet = null;
				BigDecimal nBestellt = null;

				switch (i) {

				// in Zeile 0 stehen die Summen fuer das gesamte Vorjahr
				case 0:
					sZeilenHeader = getTextRespectUISpr("lp.vorjahr", theClientDto
							.getMandant(), theClientDto.getLocUi());
					sZeilenHeader += " ";
					sZeilenHeader += iVorjahr;

					// das Zeitintervall auf das gesamte Vorjahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iVorjahr,
							iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr,
							iIndexBeginnMonat, 1);

					break;

				// Zeile 1 ist eine Leerzeile zur otpischen Trennung
				case 1:

					sZeilenHeader = null;
					break;

				// Zeile 14 ist eine Leerzeile zur optischen Trennung
				case 14:

					sZeilenHeader = null;
					break;

				// Zeile 15 enthaelt die Summe ueber das gesamte laufende
				// Geschaeftsjahr
				case 15:

					sZeilenHeader = getTextRespectUISpr("lp.summe", theClientDto
							.getMandant(), theClientDto.getLocUi());
					sZeilenHeader += " ";
					sZeilenHeader += fkjahr.value;

					// das Zeitintervall auf das gesamte laufende Jahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iJahr,
							iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,
							iIndexBeginnMonat, 1);

					break;

				// Zeile 16 enthaelt die Summe aus gesamtem laufenden Jahr plus
				// Summe des Vorjahres
				case 16:
					sZeilenHeader = getTextRespectUISpr("lp.gesamtsumme",
							theClientDto.getMandant(), theClientDto.getLocUi());

					// das Zeitintervall auf alle erfassten Auftraege festlegen
					// IMS 749 Summe ueber saemtliche Auftraege
					gcBerechnungsdatumVonI = new GregorianCalendar(1900,
							iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,
							iIndexBeginnMonat, 1);

					break;

				// Zeile 2 bis 13 enthaelt die Summe fuer das jeweilige Monat
				default:

					sZeilenHeader = aMonatsnamen[iCurrentMonat];

					// das Zeitintervall auf das laufende Monat festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(
							iCurrentJahr, iCurrentMonat, 1);

					// dieser Zeitpunkt liegt 1 Monat nach dem Von Datum
					gcBerechnungsdatumBisI = berechneNaechstesZeitintervall(
							iCurrentMonat, iCurrentJahr);

					iCurrentJahr = gcBerechnungsdatumBisI
							.get(GregorianCalendar.YEAR);
					iCurrentMonat = gcBerechnungsdatumBisI
							.get(GregorianCalendar.MONTH);

					break;
				}

				// die Werte fuer die einzelnen Zellen bestimmen
				if (gcBerechnungsdatumVonI != null
						&& gcBerechnungsdatumBisI != null) {
					// Spalte gesamt: Basis ist Belegdatum, alle Angebote mit
					// Status != (Angelegt || Storniert)
					nGesamt = getWertInMandantenwaehrung(
							gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
							iIdVertreter, IDX_GESAMT, theClientDto);

					// Spalte offen: Basis ist Belegdatum, alle Angebote mit
					// Status Offen
					nOffen = getWertInMandantenwaehrung(gcBerechnungsdatumVonI,
							gcBerechnungsdatumBisI, iIdVertreter, IDX_OFFEN,
							theClientDto);

					// Spalte offene Termin bewertet: Basis ist
					// Realisierungstermin, alle Angebote mit Status offen, der
					// Wert wird jeweils mit der Auftragswahrscheinlichkeit
					// multipliziert
					nOffenterminbewertet = getWertInMandantenwaehrung(
							gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
							iIdVertreter, IDX_OFFENTERMINBEWERTET, theClientDto);

					// Spalte bestellt: Basis ist Belegdatum, Erledigungsgrund
					// == Auftrag erhalten
					nBestellt = getWertInMandantenwaehrung(
							gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
							iIdVertreter, IDX_BESTELLT, theClientDto);
				}

				// die Zeilen fuer die Anzeige zusammenbauen
				AngebotUebersichtTabelleDto angebotUebersichtTabelleDto = new AngebotUebersichtTabelleDto();

				if (sZeilenHeader != null) {
					angebotUebersichtTabelleDto.setSZeilenheader(sZeilenHeader);
					angebotUebersichtTabelleDto.setNGesamt(nGesamt);
					angebotUebersichtTabelleDto.setNOffene(nOffen);
					angebotUebersichtTabelleDto
							.setNOffenerterminbewertet(nOffenterminbewertet);
					angebotUebersichtTabelleDto.setNBestellt(nBestellt);
				}

				// die Summen des Vorjahrs muss man sich fuer spaeter merken
				if (i == IDX_SUMMEN_VORJAHR) {
					oSummenVorjahr = angebotUebersichtTabelleDto;
				}

				// die Gesamtsummen bestehen aus den Summen des laufenden Jahres
				// und des Vorjahres
				if (i == IDX_SUMMEN_GESAMT) {
					// @todo IMS 749 linke Spalte Offene enthaelt die Summe
					// ueber alle Auftraege im System PJ 3800
				}

				hmSammelstelle.put(new Integer(i), angebotUebersichtTabelleDto);
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<AngebotUebersichtTabelleDto> clSichtLieferstatus = hmSammelstelle.values();
			Iterator<AngebotUebersichtTabelleDto> it = clSichtLieferstatus.iterator();

			while (it.hasNext()) {
				AngebotUebersichtTabelleDto angebotUebersichtTabelleDto = (AngebotUebersichtTabelleDto) it
						.next();
				rows[row][col++] = "";
				rows[row][col++] = angebotUebersichtTabelleDto
						.getSZeilenHeader();
				rows[row][col++] = angebotUebersichtTabelleDto.getNGesamt();
				rows[row][col++] = angebotUebersichtTabelleDto.getNOffene();
				rows[row][col++] = angebotUebersichtTabelleDto
						.getNOffenerterminbewertet();
				rows[row++][col++] = angebotUebersichtTabelleDto.getNBestellt();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Exception e) {
			throw new EJBExceptionLP(1, e);
		}

		return result;
	}

	/**
	 * Der Wert aller Angebote in Mandantenwaehrung, die lt. Belegdatum in einem
	 * bestimmten Zeitraum angelegt wurden und deren Status != (Angelegt ||
	 * Storniert) ist. Optional kann auch nach einem bestimmten Vertreter
	 * ausgewertet werden.
	 * 
	 * @param gcBerechnungsdatumVonI
	 *            Belegdatum von
	 * @param gcBerechnungsdatumBisI
	 *            Belegdatum bis
	 * @param iIdVertreterI
	 *            der Vertreter, null erlaubt
	 * @param iIndexSpaltentyp
	 *            fuer welche Spalte soll die Berechnung erfolgen
	 * @param cNrUserI
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gesamtwert in Mandantenwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private BigDecimal getWertInMandantenwaehrung(
			GregorianCalendar gcBerechnungsdatumVonI,
			GregorianCalendar gcBerechnungsdatumBisI, Integer iIdVertreterI,
			int iIndexSpaltentyp, TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal nGesamtwertInMandantenwaehrung = new BigDecimal(0);

		Session session = null;

		try {
			// System.out.println("Spaltentyp: " + iIndexSpaltentyp);
			// System.out.println("Von: " +
			// Helper.formatDatum(gcBerechnungsdatumVonI.getTime(),
			// getTheClient(sCurrentUser).getLocUi()));
			// System.out.println("Bis: " +
			// Helper.formatDatum(gcBerechnungsdatumBisI.getTime(),
			// getTheClient(sCurrentUser).getLocUi()));

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen
			Criteria critAngebotposition = session
					.createCriteria(FLRAngebotpositionReport.class);
			Criteria critAngebot = critAngebotposition
					.createCriteria(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT);

			// zuerst alle jene Einschraenkungen, die fuer alle Spaltentypen der
			// Uebersicht gelten
			critAngebot.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			if (iIdVertreterI != null) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID, iIdVertreterI));
			}

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			critAngebotposition.add(Restrictions
					.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			critAngebotposition.add(Restrictions.gt(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
					new BigDecimal(0)));
			critAngebotposition.add(Restrictions.eq(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE,
					new Short((short) 0)));

			// die folgenden Einschraenkungen sind vom Spaltentyp der Ubersicht
			// abhaengig
			// IDX_GESAMT: Basis ist Belegdatum, alle Angebote mit Status !=
			// (Angelegt || Storniert)
			// IDX_OFFEN: Basis ist Belegdatum, alle Angebote mit Status Offen
			// IDX_OFFENTERMINBEWERTET: Basis ist Realisierungstermin, alle
			// Angebote mit Status offen;
			// der Wert wird jeweils mit der Auftragswahrscheinlichkeit
			// multipliziert
			// IDX_BESTELLT: Basis ist Belegdatum, Erledigungsgrund == Auftrag
			// erhalten
			if (iIndexSpaltentyp == IDX_GESAMT) {
				critAngebot.add(Restrictions.ne(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT));
				critAngebot.add(Restrictions.ne(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_STORNIERT));
			} else if (iIndexSpaltentyp == IDX_OFFEN
					|| iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_OFFEN));
			} else if (iIndexSpaltentyp == IDX_BESTELLT) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));
				critAngebot
						.add(Restrictions
								.eq(
										AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR,
										AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN));
			}

			if (iIndexSpaltentyp == IDX_GESAMT || iIndexSpaltentyp == IDX_OFFEN
					|| iIndexSpaltentyp == IDX_BESTELLT) {
				if (gcBerechnungsdatumVonI.getTime() != null) {
					critAngebot.add(Restrictions.ge(
							AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
							gcBerechnungsdatumVonI.getTime()));
				}

				if (gcBerechnungsdatumBisI.getTime() != null) {
					critAngebot.add(Restrictions.lt(
							AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
							gcBerechnungsdatumBisI.getTime()));
				}
			}

			if (iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
				if (gcBerechnungsdatumVonI.getTime() != null) {
					critAngebot.add(Restrictions.ge(
							AngebotFac.FLR_ANGEBOT_T_REALISIERUNGSTERMIN,
							gcBerechnungsdatumVonI.getTime()));
				}

				if (gcBerechnungsdatumBisI.getTime() != null) {
					critAngebot.add(Restrictions.lt(
							AngebotFac.FLR_ANGEBOT_T_REALISIERUNGSTERMIN,
							gcBerechnungsdatumBisI.getTime()));
				}
			}

			List<?> list = critAngebotposition.list();

			int iIndex = 0;
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) it
						.next();

				// der Beitrag der einzelnen Position in Angebotswaehrung
				BigDecimal nBeitragDerPositionInAngebotswaehrung = flrangebotposition
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
						.multiply(flrangebotposition.getN_menge());

				BigDecimal nBeitragDerPositionInMandantenwaehrung = nBeitragDerPositionInAngebotswaehrung
						.multiply(Helper
								.getKehrwert(new BigDecimal(
										flrangebotposition
												.getFlrangebot()
												.getF_wechselkursmandantwaehrungzuangebotswaehrung()
												.doubleValue())));

				if (iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
					double dAuftragswahrscheinlichkeit = flrangebotposition
							.getFlrangebot().getF_auftragswahrscheinlichkeit()
							.doubleValue();

					nBeitragDerPositionInMandantenwaehrung = nBeitragDerPositionInMandantenwaehrung
							.multiply(new BigDecimal(
									dAuftragswahrscheinlichkeit)
									.movePointLeft(2));
				}

				nBeitragDerPositionInMandantenwaehrung = Helper
						.rundeKaufmaennisch(
								nBeitragDerPositionInMandantenwaehrung, 4);
				checkNumberFormat(nBeitragDerPositionInMandantenwaehrung);

				nGesamtwertInMandantenwaehrung = nGesamtwertInMandantenwaehrung
						.add(nBeitragDerPositionInMandantenwaehrung);

				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return nGesamtwertInMandantenwaehrung;
	}
}
