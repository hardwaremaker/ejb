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
package com.lp.server.auftrag.fastlanereader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Handler fuer Loszeiten <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 15.04.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AuftragSichtLSREHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Object[]> hmDaten = null;

	private int SPALTE_TYP1 = 1;
	private int SPALTE_TYP2 = 2;
	private int SPALTE_TYP3 = 3;
	private int SPALTE_BELEG = 4;
	private int SPALTE_DATUM = 5;
	private int SPALTE_VERTRETER = 6;
	private int SPALTE_STATUS = 7;
	private int SPALTE_NETTOWERT = 8;
	private int SPALTE_WAEHRUNG = 9;
	private int ANZAHL_SPALTEN = 10;

	private Boolean bSchlussrechnung = Boolean.FALSE;

	/**
	 * Konstruktor.
	 */
	public AuftragSichtLSREHandler() {
		super();
		setAnzahlSpalten(9);
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(new Class[] { Object.class, String.class,
					String.class, String.class, String.class,
					java.util.Date.class, String.class, Icon.class,
					BigDecimal.class, String.class },
					// die Spaltenueberschriften werden durch die Kriterien
					// bestimmt
					new String[] {
							" ",
							getTextRespectUISpr("lp.typ", mandantCNr, locUI),
							" ",
							" ",
							getTextRespectUISpr("lp.belegnummer", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.vertreter", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI),
							getTextRespectUISpr("lp.whg", mandantCNr, locUI) },
					// die Breite der Spalten festlegen
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M }, // hidden
					new String[] { "", "", "", "", "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            Zeilenindex
	 * @return QueryResult Ergebnis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// die Anzahl der Zeilen festlegen und den Inhalt befuellen
			setInhalt();

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			for (int row = 0; row < getAnzahlZeilen(); row++) {
				rows[row] = hmDaten.get(row);
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<Object[]>();
			getFilterKriterien();

			setInhalt();
		} catch (Throwable t) {
			if (t.getCause() instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t.getCause();
			} else {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
						new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	/**
	 * Diese Methode setzt die Anzahl der Zeilen in der Tabelle und den Inhalt.
	 * 
	 * @param bFillData
	 *            false, wenn der Inhalt nicht befuellt werden soll
	 * @throws Throwable
	 *             Ausnahme
	 */

	private void befuelleMitRechnungDto(RechnungDto rDto, String typ1,
			String typ2, String typ3, boolean bDarfPreiseSehen) {
		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[0] = rDto;

		if (typ1 != null) {

			if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
				typ1 = "PROFORMA";
			} else if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_ANZAHLUNG)) {
				typ1 = "ANZAHLUNG";
			} else if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				typ1 = "SCHLUSSRE";
			}
		} else if (typ2 != null) {

			if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
				typ2 = "PROFORMA";
			} else if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_ANZAHLUNG)) {
				typ2 = "ANZAHLUNG";
			} else if (rDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				typ2 = "SCHLUSSRE";
			}
		}

		oZeile[SPALTE_TYP1] = typ1;
		oZeile[SPALTE_TYP2] = typ2;
		oZeile[SPALTE_TYP3] = typ3;
		oZeile[SPALTE_BELEG] = rDto.getCNr();

		oZeile[SPALTE_DATUM] = rDto.getTBelegdatum();

		if (bDarfPreiseSehen) {

			if (typ1 != null && typ1.equals("GS") || typ2 != null
					&& typ2.equals("GS") || typ3 != null && typ3.equals("GS")) {
				if (rDto.getNGesamtwertinbelegwaehrung() != null) {
					oZeile[SPALTE_NETTOWERT] = rDto
							.getNGesamtwertinbelegwaehrung().negate();
				}
			} else if (typ1 != null && typ1.equals("SCHLUSSRE") || typ2 != null
					&& typ2.equals("SCHLUSSRE") || typ3 != null
					&& typ3.equals("SCHLUSSRE")) {

				if (rDto.getNGesamtwertinbelegwaehrung() != null) {

					BigDecimal bdAnzahlungen = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungFw(rDto.getIId());

					oZeile[SPALTE_NETTOWERT] = rDto
							.getNGesamtwertinbelegwaehrung().subtract(
									bdAnzahlungen);
				}
			} else {
				oZeile[SPALTE_NETTOWERT] = rDto.getNGesamtwertinbelegwaehrung();
			}
		}

		oZeile[SPALTE_STATUS] = rDto.getStatusCNr();
		if (rDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(
					rDto.getPersonalIIdVertreter(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		oZeile[SPALTE_WAEHRUNG] = rDto.getWaehrungCNr();

		hmDaten.add(oZeile);

		// Gutschriften
		RechnungDto[] gsDtos = getRechnungFac()
				.rechnungFindByRechnungIIdZuRechnung(rDto.getIId());
		for (int i = 0; i < gsDtos.length; i++) {

			if (typ1 != null) {
				befuelleMitRechnungDto(gsDtos[i], null, "GS", null,
						bDarfPreiseSehen);
			} else {
				befuelleMitRechnungDto(gsDtos[i], null, null, "GS",
						bDarfPreiseSehen);
			}
		}

	}

	private void befuelleMitLieferscheinDto(LieferscheinDto lDto, String typ1,
			String typ2, String typ3, boolean bDarfPreiseSehen)
			throws Throwable {
		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[0] = lDto;
		oZeile[SPALTE_TYP1] = typ1;
		oZeile[SPALTE_TYP2] = typ2;
		oZeile[SPALTE_TYP3] = typ3;
		oZeile[SPALTE_BELEG] = lDto.getCNr();
		oZeile[SPALTE_DATUM] = lDto.getTBelegdatum();

		if (lDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(
					lDto.getPersonalIIdVertreter(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}

		oZeile[SPALTE_STATUS] = lDto.getStatusCNr();

		if (bDarfPreiseSehen) {

			oZeile[SPALTE_NETTOWERT] = lDto
					.getNGesamtwertInLieferscheinwaehrung();
		}
		oZeile[SPALTE_WAEHRUNG] = lDto.getWaehrungCNr();
		hmDaten.add(oZeile);

		// Rechnungen zu Lieferschein

		HashMap<Integer, RechnungDto> rechnungen = new HashMap<Integer, RechnungDto>();

		RechnungDto[] rechnungDtos = getRechnungFac()
				.rechnungFindByLieferscheinIId(lDto.getIId());

		for (int i = 0; i < rechnungDtos.length; i++) {
			if (!rechnungen.containsKey(rechnungDtos[i].getIId())) {
				rechnungen.put(rechnungDtos[i].getIId(), rechnungDtos[i]);
				if (rechnungDtos[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					bSchlussrechnung = Boolean.TRUE;
				}

			}
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session
				.createQuery("SELECT re FROM FLRRechnungPosition re WHERE re.flrlieferschein.i_id="
						+ lDto.getIId());

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRRechnungPosition repos = (FLRRechnungPosition) resultListIterator
					.next();

			if (!rechnungen.containsKey(repos.getFlrrechnung().getI_id())) {

				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(
						repos.getFlrrechnung().getI_id());
				if (reDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					bSchlussrechnung = Boolean.TRUE;
				}

				rechnungen.put(repos.getFlrrechnung().getI_id(), reDto);
			}
		}

		// Lieferscheine einfuegen

		Iterator<Integer> re = rechnungen.keySet().iterator();
		while (re.hasNext()) {

			RechnungDto reDto = (RechnungDto) rechnungen.get(re.next());

			befuelleMitRechnungDto(reDto, null, "RE", null, bDarfPreiseSehen);
		}
	}

	private void setInhalt() throws Throwable {

		hmDaten = new ArrayList<Object[]>();
		bSchlussrechnung = Boolean.FALSE;

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();

		boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

		HashMap<Integer, LieferscheinDto> lieferscheine = new HashMap<Integer, LieferscheinDto>();

		Integer auftragIId = new Integer(aFilterKriterium[0].value);

		// Alle Lieferscheine, weche am Auftrag haengen

		LieferscheinDto[] lieferscheinDtos = getLieferscheinFac()
				.lieferscheinFindByAuftrag(auftragIId, theClientDto);

		for (int i = 0; i < lieferscheinDtos.length; i++) {
			if (!lieferscheine.containsKey(lieferscheinDtos[i].getIId())) {
				lieferscheine.put(lieferscheinDtos[i].getIId(),
						lieferscheinDtos[i]);
			}
		}

		// Lieferscheine einfuegen

		Iterator<Integer> ls = lieferscheine.keySet().iterator();
		while (ls.hasNext()) {

			LieferscheinDto lsDto = (LieferscheinDto) lieferscheine.get(ls
					.next());
			befuelleMitLieferscheinDto(lsDto, "LS", null, null,
					bDarfPreiseSehen);
		}

		// Rechnungen

		HashMap<Integer, RechnungDto> rechnungen = new HashMap<Integer, RechnungDto>();

		RechnungDto[] rechnungDtos = getRechnungFac().rechnungFindByAuftragIId(
				auftragIId);

		for (int i = 0; i < rechnungDtos.length; i++) {
			if (!rechnungen.containsKey(rechnungDtos[i].getIId())) {
				rechnungen.put(rechnungDtos[i].getIId(), rechnungDtos[i]);
			}
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session
				.createQuery("SELECT re FROM FLRRechnungPosition re WHERE re.flrpositionensichtauftrag.auftrag_i_id="
						+ auftragIId);

		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRRechnungPosition repos = (FLRRechnungPosition) resultListIterator
					.next();

			if (!rechnungen.containsKey(repos.getFlrrechnung().getI_id())) {
				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(
						repos.getFlrrechnung().getI_id());
				rechnungen.put(repos.getFlrrechnung().getI_id(), reDto);
			}
		}

		// Lieferscheine einfuegen

		Iterator<Integer> re = rechnungen.keySet().iterator();
		while (re.hasNext()) {

			RechnungDto reDto = (RechnungDto) rechnungen.get(re.next());

			if (reDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
					&& bSchlussrechnung == true) {
				continue;
			}

			befuelleMitRechnungDto(reDto, "RE", null, null, bDarfPreiseSehen);
		}

		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);
	}
}
