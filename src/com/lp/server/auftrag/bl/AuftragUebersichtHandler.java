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
package com.lp.server.auftrag.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragUebersichtTabelleDto;
import com.lp.server.rechnung.service.GutschriftUmsatzTabelleDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungUmsatzTabelleDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle im Auftrag. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-20
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AuftragUebersichtHandler extends UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<AuftragUebersichtTabelleDto> hmDaten = null;
	private final int iAnzahlSpalten = 9;

	/**
	 * Konstruktor.
	 */
	public AuftragUebersichtHandler() {
		super();

		try {
			setAnzahlZeilen(iAnzahlZeilen);
			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 3818
		}
	}

	private void setInhalt() throws Throwable {
		mandantCNr = theClientDto.getMandant();
		locUI = theClientDto.getLocUi();
		hmDaten = new ArrayList<AuftragUebersichtTabelleDto>();
		DateFormatSymbols symbols = new DateFormatSymbols(locUI);
		aMonatsnamen = symbols.getMonths();

		getFilterKriterien();
		FilterKriterium fkAuswertung = aFilterKriterium[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_AUSWERTUNG];
		FilterKriterium fkJahr = aFilterKriterium[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_JAHR];
		FilterKriterium fkPlusjahre = aFilterKriterium[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_PLUS_JAHRE];

		Integer iJahr = new Integer(fkJahr.value).intValue();

		// PJ21214
		Integer plusJahre = new Integer(fkPlusjahre.value).intValue();

		boolean bGerschaeftsjahr = false;
		if (fkJahr.kritName.equals(AuftragFac.KRIT_UEBERSICHT_GESCHAEFTSJAHR)) {
			bGerschaeftsjahr = true;
		}

		// Zeile 1 Vorjahr
		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();
		zeileDto.setSZeilenheader(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1 + plusJahre));

		GregorianCalendar[] gcVonBis = getVorjahr(iJahr, plusJahre, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(
				getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1 + plusJahre),
				fkAuswertung.kritName, gcVonBis[0], gcVonBis[1]));

		// Leerzeile
		hmDaten.add(new AuftragUebersichtTabelleDto());

		// Nun Monate des aktuellen GF durchgehen

		ArrayList<GregorianCalendar[]> alMonate = getMonateAktuellesJahr(iJahr, plusJahre, bGerschaeftsjahr);

		for (int i = 0; i < alMonate.size(); i++) {

			GregorianCalendar[] gcVonBisMonate = alMonate.get(i);

			hmDaten.add(befuelleDtoAnhandDatumsgrenzen(aMonatsnamen[gcVonBisMonate[0].get(GregorianCalendar.MONTH)],
					fkAuswertung.kritName, gcVonBisMonate[0], gcVonBisMonate[1]));
		}

		// Leerzeile
		hmDaten.add(new AuftragUebersichtTabelleDto());

		// Summe aktuelles Jahr
		GregorianCalendar[] gcVonBisAktuell = getAktuellesJahr(iJahr, plusJahre, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(
				getTextRespectUISpr("lp.summe", mandantCNr, locUI) + " " + (iJahr + plusJahre), fkAuswertung.kritName,
				gcVonBisAktuell[0], gcVonBisAktuell[1]));

		// Summe Gesamt
		GregorianCalendar[] gcVonBisGesamt = getGesamtJahr(iJahr, plusJahre, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.gesamtsumme", mandantCNr, locUI),
				fkAuswertung.kritName, null, null));

		setAnzahlZeilen(hmDaten.size());

	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			tableInfo = new TableInfo(
					new Class[] { String.class, String.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
							String.class, BigDecimal.class, BigDecimal.class, BigDecimal.class },
					new String[] { " ", " ",
							getTextRespectUISpr("auft.freieauftraege", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("auft.abrufautraege", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("auft.rahmenauftraege", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							" ",
							getTextRespectUISpr("auft.freieauftraege", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("auft.abrufautraege", theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("auft.rahmenauftraege", theClientDto.getMandant(),
									theClientDto.getLocUi()) },
					new String[] { "", "", "", "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<AuftragUebersichtTabelleDto>();
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

	private AuftragUebersichtTabelleDto befuelleDtoAnhandDatumsgrenzen(String header, String kriterium,
			GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI) throws Throwable {

		BigDecimal bdOffeneFreie = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_FREI,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
				theClientDto);

		// den offenen Nettoauftragswert fuer Auftragart ABRUF
		// bestimmen
		BigDecimal bdOffeneAbruf = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_ABRUF,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
				theClientDto);

		// den offenen Nettoauftragswert fuer Auftragart RAHMEN
		// bestimmen
		BigDecimal bdOffeneRahmen = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_RAHMEN,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
				theClientDto);

		// den gesamten Nettoauftragswert fuer Auftragart FREI
		// bestimmen
		BigDecimal bdEingangFreie = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_FREI,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
				theClientDto);

		// die Summe fuer Auftragart ABRUF und Auftragstatus OFFEN
		// bestimmen
		BigDecimal bdEingangAbruf = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_ABRUF,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
				theClientDto);

		// die Summe fuer Auftragart RAHMEN und Auftragstatus OFFEN
		// bestimmen
		BigDecimal bdEingangRahmen = getAuftragFac().berechneSummeAuftragsnettowert(AuftragServiceFac.AUFTRAGART_RAHMEN,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
				theClientDto);

		// die Zeilen fuer die Anzeige zusammenbauen
		AuftragUebersichtTabelleDto zeileDto = new AuftragUebersichtTabelleDto();

		zeileDto.setSZeilenheader(header);
		zeileDto.setBdAbrufauftraege1(bdOffeneAbruf);
		zeileDto.setBdFreieauftraege1(bdOffeneFreie);
		zeileDto.setBdRahmenauftrage1(bdOffeneRahmen);

		zeileDto.setBdAbrufauftraege2(bdEingangAbruf);
		zeileDto.setBdFreieauftraege2(bdEingangFreie);
		zeileDto.setBdRahmenauftrage2(bdEingangRahmen);

		return zeileDto;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			setInhalt();
			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			int col = 0;

			for (int row = 0; row < hmDaten.size(); row++) {
				AuftragUebersichtTabelleDto oAuftragUebersichtDto = (AuftragUebersichtTabelleDto) hmDaten.get(row);
				rows[row][col++] = "";
				rows[row][col++] = oAuftragUebersichtDto.getSZeilenHeader();
				rows[row][col++] = oAuftragUebersichtDto.getBdFreieauftraege1();
				rows[row][col++] = oAuftragUebersichtDto.getBdAbrufauftraege1();
				rows[row][col++] = oAuftragUebersichtDto.getBdRahmenauftrage1();
				rows[row][col++] = oAuftragUebersichtDto.getSEmpty();
				rows[row][col++] = oAuftragUebersichtDto.getBdFreieauftraege2();
				rows[row][col++] = oAuftragUebersichtDto.getBdAbrufauftraege2();
				rows[row][col++] = oAuftragUebersichtDto.getBdRahmenauftrage2();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

}
