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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

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
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungUmsatzTabelleDto;
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
	private ArrayList<AngebotUebersichtTabelleDto> hmDaten = null;
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

			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 3758
		}
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<AngebotUebersichtTabelleDto>();
			getFilterKriterien();

			setInhalt();
		} catch (Throwable t) {
			if (t.getCause() instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t.getCause();
			} else {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			tableInfo = new TableInfo(
					new Class[] { String.class, String.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
							BigDecimal.class },
					new String[] { "", " ",
							getTextRespectUISpr("lp.gesamt", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("er.offene", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("angb.offenetermin", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bestellt", theClientDto.getMandant(), theClientDto.getLocUi()) },
					new String[] { "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	private AngebotUebersichtTabelleDto befuelleDtoAnhandDatumsgrenzen(String header, Integer iIdVertreter,
			GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI) throws Throwable {

		AngebotUebersichtTabelleDto zeileDto = new AngebotUebersichtTabelleDto();

		// Spalte gesamt: Basis ist Belegdatum, alle Angebote mit
		// Status != (Angelegt || Storniert)
		BigDecimal nGesamt = getWertInMandantenwaehrung(gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, iIdVertreter,
				IDX_GESAMT, theClientDto);

		// Spalte offen: Basis ist Belegdatum, alle Angebote mit
		// Status Offen
		BigDecimal nOffen = getWertInMandantenwaehrung(gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, iIdVertreter,
				IDX_OFFEN, theClientDto);

		// Spalte offene Termin bewertet: Basis ist
		// Realisierungstermin, alle Angebote mit Status offen, der
		// Wert wird jeweils mit der Auftragswahrscheinlichkeit
		// multipliziert
		BigDecimal nOffenterminbewertet = getWertInMandantenwaehrung(gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
				iIdVertreter, IDX_OFFENTERMINBEWERTET, theClientDto);

		// Spalte bestellt: Basis ist Belegdatum, Erledigungsgrund
		// == Auftrag erhalten
		BigDecimal nBestellt = getWertInMandantenwaehrung(gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, iIdVertreter,
				IDX_BESTELLT, theClientDto);

		zeileDto.setSZeilenheader(header);
		zeileDto.setNGesamt(nGesamt);
		zeileDto.setNOffene(nOffen);
		zeileDto.setNOffenerterminbewertet(nOffenterminbewertet);
		zeileDto.setNBestellt(nBestellt);

		return zeileDto;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			int col = 0;

			for (int row = 0; row < hmDaten.size(); row++) {
				AngebotUebersichtTabelleDto angebotUebersichtTabelleDto = (AngebotUebersichtTabelleDto) hmDaten
						.get(row);
				rows[row][col++] = "";
				rows[row][col++] = angebotUebersichtTabelleDto.getSZeilenHeader();
				rows[row][col++] = angebotUebersichtTabelleDto.getNGesamt();
				rows[row][col++] = angebotUebersichtTabelleDto.getNOffene();
				rows[row][col++] = angebotUebersichtTabelleDto.getNOffenerterminbewertet();
				rows[row][col++] = angebotUebersichtTabelleDto.getNBestellt();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(1, e);
		}

		return result;
	}

	private void setInhalt() throws Throwable {
		mandantCNr = theClientDto.getMandant();
		locUI = theClientDto.getLocUi();
		hmDaten = new ArrayList<AngebotUebersichtTabelleDto>();
		DateFormatSymbols symbols = new DateFormatSymbols(locUI);
		aMonatsnamen = symbols.getMonths();

		getFilterKriterien();

		FilterKriterium fkJahr = aFilterKriterium[AngebotFac.IDX_KRIT_GESCHAEFTSJAHR];
		FilterKriterium fkVertreter = aFilterKriterium[AngebotFac.IDX_KRIT_VERTRETER_I_ID];
		Integer iIdVertreter = null;
		if (fkVertreter.value != null) {
			iIdVertreter = new Integer(fkVertreter.value);
		}

		Integer iJahr = new Integer(fkJahr.value).intValue();

		boolean bGerschaeftsjahr = false;
		if (fkJahr.kritName.equals(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR)) {
			bGerschaeftsjahr = true;
		}

		// Zeile 1 Vorjahr
		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();
		zeileDto.setSZeilenheader(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1));

		GregorianCalendar[] gcVonBis = getVorjahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(
				befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1),
						iIdVertreter, gcVonBis[0], gcVonBis[1]));

		// Leerzeile
		hmDaten.add(new AngebotUebersichtTabelleDto());

		// Nun Monate des aktuellen GF durchgehen

		ArrayList<GregorianCalendar[]> alMonate = getMonateAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		for (int i = 0; i < alMonate.size(); i++) {

			GregorianCalendar[] gcVonBisMonate = alMonate.get(i);

			hmDaten.add(befuelleDtoAnhandDatumsgrenzen(aMonatsnamen[gcVonBisMonate[0].get(GregorianCalendar.MONTH)],
					iIdVertreter, gcVonBisMonate[0], gcVonBisMonate[1]));
		}

		// Leerzeile
		hmDaten.add(new AngebotUebersichtTabelleDto());

		// Summe aktuelles Jahr
		GregorianCalendar[] gcVonBisAktuell = getAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.summe", mandantCNr, locUI) + " " + iJahr,
				iIdVertreter, gcVonBisAktuell[0], gcVonBisAktuell[1]));

		// Summe Gesamt
		GregorianCalendar[] gcVonBisGesamt = getGesamtJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.gesamtsumme", mandantCNr, locUI),
				iIdVertreter, gcVonBisGesamt[0], gcVonBisGesamt[1]));

		setAnzahlZeilen(hmDaten.size());

	}

	/**
	 * Der Wert aller Angebote in Mandantenwaehrung, die lt. Belegdatum in einem
	 * bestimmten Zeitraum angelegt wurden und deren Status != (Angelegt ||
	 * Storniert) ist. Optional kann auch nach einem bestimmten Vertreter
	 * ausgewertet werden.
	 * 
	 * @param gcBerechnungsdatumVonI Belegdatum von
	 * @param gcBerechnungsdatumBisI Belegdatum bis
	 * @param iIdVertreterI          der Vertreter, null erlaubt
	 * @param iIndexSpaltentyp       fuer welche Spalte soll die Berechnung erfolgen
	 * @param cNrUserI               der aktuelle Benutzer
	 * @return BigDecimal der Gesamtwert in Mandantenwaehrung
	 * @throws EJBExceptionLP Ausnahme
	 */
	private BigDecimal getWertInMandantenwaehrung(GregorianCalendar gcBerechnungsdatumVonI,
			GregorianCalendar gcBerechnungsdatumBisI, Integer iIdVertreterI, int iIndexSpaltentyp,
			TheClientDto theClientDto) throws EJBExceptionLP {
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
			Criteria critAngebotposition = session.createCriteria(FLRAngebotpositionReport.class);
			Criteria critAngebot = critAngebotposition
					.createCriteria(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT);

			// zuerst alle jene Einschraenkungen, die fuer alle Spaltentypen der
			// Uebersicht gelten
			critAngebot.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			if (iIdVertreterI != null) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID, iIdVertreterI));
			}

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			critAngebotposition.add(Restrictions.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			critAngebotposition.add(Restrictions.gt(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE, new BigDecimal(0)));
			critAngebotposition
					.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE, new Short((short) 0)));

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
				critAngebot.add(Restrictions.ne(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT));
				critAngebot.add(Restrictions.ne(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_STORNIERT));
			} else if (iIndexSpaltentyp == IDX_OFFEN || iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_OFFEN));
			} else if (iIndexSpaltentyp == IDX_BESTELLT) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR,
						AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN));
			}

			if (iIndexSpaltentyp == IDX_GESAMT || iIndexSpaltentyp == IDX_OFFEN || iIndexSpaltentyp == IDX_BESTELLT) {
				if (gcBerechnungsdatumVonI.getTime() != null) {
					critAngebot.add(
							Restrictions.ge(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, gcBerechnungsdatumVonI.getTime()));
				}

				if (gcBerechnungsdatumBisI.getTime() != null) {
					critAngebot.add(
							Restrictions.lt(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, gcBerechnungsdatumBisI.getTime()));
				}
			}

			if (iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
				if (gcBerechnungsdatumVonI.getTime() != null) {
					critAngebot.add(Restrictions.ge(AngebotFac.FLR_ANGEBOT_T_REALISIERUNGSTERMIN,
							gcBerechnungsdatumVonI.getTime()));
				}

				if (gcBerechnungsdatumBisI.getTime() != null) {
					critAngebot.add(Restrictions.lt(AngebotFac.FLR_ANGEBOT_T_REALISIERUNGSTERMIN,
							gcBerechnungsdatumBisI.getTime()));
				}
			}

			List<?> list = critAngebotposition.list();

			int iIndex = 0;
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) it.next();

				// der Beitrag der einzelnen Position in Angebotswaehrung
				BigDecimal nBeitragDerPositionInAngebotswaehrung = flrangebotposition
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
						.multiply(flrangebotposition.getN_menge());

				BigDecimal nBeitragDerPositionInMandantenwaehrung = nBeitragDerPositionInAngebotswaehrung
						.multiply(Helper.getKehrwert(new BigDecimal(flrangebotposition.getFlrangebot()
								.getF_wechselkursmandantwaehrungzuangebotswaehrung().doubleValue())));

				if (iIndexSpaltentyp == IDX_OFFENTERMINBEWERTET) {
					double dAuftragswahrscheinlichkeit = flrangebotposition.getFlrangebot()
							.getF_auftragswahrscheinlichkeit().doubleValue();

					nBeitragDerPositionInMandantenwaehrung = nBeitragDerPositionInMandantenwaehrung
							.multiply(new BigDecimal(dAuftragswahrscheinlichkeit).movePointLeft(2));
				}

				nBeitragDerPositionInMandantenwaehrung = Helper
						.rundeKaufmaennisch(nBeitragDerPositionInMandantenwaehrung, 4);
				checkNumberFormat(nBeitragDerPositionInMandantenwaehrung);

				nGesamtwertInMandantenwaehrung = nGesamtwertInMandantenwaehrung
						.add(nBeitragDerPositionInMandantenwaehrung);

				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
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
