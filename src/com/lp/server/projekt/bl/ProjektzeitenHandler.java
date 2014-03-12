/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.projekt.bl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

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

public class ProjektzeitenHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuftragzeitenDto[] aProjektzeitenDtos = null;
	private HashMap<String, String> hmKoepfe = null; // Liste der anzuzeigenden

	// Koepfe

	/**
	 * Konstruktor.
	 */
	public ProjektzeitenHandler() {
		super();
		setAnzahlSpalten(8);
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(new Class[] { Object.class, Object.class,
					Object.class, Object.class, Object.class, Object.class,
					Double.class, BigDecimal.class },
			// die Spaltenueberschriften werden durch die Kriterien
					// bestimmt
					new String[] {
							" ", // kein Zeilenheader
							" ", // je nach Filter
							" ", // je nach Filter
							" ", // je nach Filter
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.beginn", mandantCNr, locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("lp.kosten", mandantCNr, locUI) },
					// die Breite der Spalten festlegen
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M, 7,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST }, // hidden
					new String[] { "", "", "", "", "", "", "", "" });
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
			setTableProperties(true);

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			int col = 0;
			int iAktuellerAuftrag = 0;

			for (int row = 0; row < getAnzahlZeilen(); row++) {

				// enthaelt KRIT_PERSONAL = true || KRIT_IDENT = true
				FilterKriterium fkAuswertung = aFilterKriterium[ProjektFac.IDX_KRIT_AUSWERTUNG];

				AuftragzeitenDto oAuftragzeitenDto = aProjektzeitenDtos[iAktuellerAuftrag];

				// die erste Zeile pro Auftrag ist der Kopf
				boolean doAnzeige = false;

				if (fkAuswertung.kritName.equals(ProjektFac.KRIT_PERSONAL)) {
					if (hmKoepfe.containsKey(oAuftragzeitenDto
							.getSPersonalnummer())) {
						// in diesem Fall gibt es den Kopf noch nicht
						doAnzeige = true;
						hmKoepfe.remove(oAuftragzeitenDto.getSPersonalnummer());
					}
				} else if (fkAuswertung.kritName.equals(ProjektFac.KRIT_IDENT)) {
					if (hmKoepfe
							.containsKey(oAuftragzeitenDto.getSArtikelcnr())) {
						// in diesem Fall gibt es den Kopf noch nicht
						doAnzeige = true;
						hmKoepfe.remove(oAuftragzeitenDto.getSArtikelcnr());
					}
				}

				if (doAnzeige) {
					rows[row][col++] = ""; // leerer Zeilenheader

					// Anordnung der Spalten je nach Filterkriterien
					if (fkAuswertung.kritName.equals(ProjektFac.KRIT_PERSONAL)) {
						rows[row][col++] = oAuftragzeitenDto
								.getSPersonalnummer();
						rows[row][col++] = oAuftragzeitenDto
								.getSPersonalMaschinenname();
						rows[row][col++] = null;
					} else if (fkAuswertung.kritName
							.equals(ProjektFac.KRIT_IDENT)) {
						rows[row][col++] = oAuftragzeitenDto.getSArtikelcnr();
						rows[row][col++] = oAuftragzeitenDto
								.getSArtikelbezeichnung();
						rows[row][col++] = null;
					}

					rows[row][col++] = null;
					rows[row][col++] = null;
					rows[row][col++] = null;
					rows[row][col++] = null;

					row++;
					col = 0;
				}

				// die zweite Zeile pro Auftrags
				rows[row][col++] = ""; // leerer Zeilenheader

				// Anordnung der Spalten je nach Filterkriterien
				if (fkAuswertung.kritName.equals(ProjektFac.KRIT_PERSONAL)) {
					rows[row][col++] = "";
					rows[row][col++] = oAuftragzeitenDto.getSArtikelcnr();
					rows[row][col++] = oAuftragzeitenDto
							.getSArtikelbezeichnung();
				} else if (fkAuswertung.kritName.equals(ProjektFac.KRIT_IDENT)) {
					rows[row][col++] = "";
					rows[row][col++] = oAuftragzeitenDto.getSPersonalnummer();
					rows[row][col++] = oAuftragzeitenDto
							.getSPersonalMaschinenname();
				}

				rows[row][col++] = Helper.formatDatum(new Date(
						oAuftragzeitenDto.getTsBeginn().getTime()),
						theClientDto.getLocUi());
				rows[row][col++] = Helper.formatTime(oAuftragzeitenDto
						.getTsBeginn(), theClientDto.getLocUi());
				rows[row][col++] = oAuftragzeitenDto.getDdDauer();
				rows[row][col++] = oAuftragzeitenDto.getBdKosten();

				row++;
				col = 0;

				// die dritte Zeile pro Auftrag
				rows[row][col++] = ""; // leerer Zeilenheader
				rows[row][col++] = "";
				rows[row][col++] = oAuftragzeitenDto.getSBewegungsart();
				rows[row][col++] = oAuftragzeitenDto.getSZeitbuchungtext();
				rows[row][col++] = Helper.formatDatum(new Date(
						oAuftragzeitenDto.getTsEnde().getTime()), theClientDto
						.getLocUi());
				rows[row][col++] = Helper.formatTime(oAuftragzeitenDto
						.getTsEnde(), theClientDto.getLocUi());
				rows[row][col++] = null;
				rows[row][col++] = null;

				// Variable row wird in der for-Schleife hochgezaehlt
				col = 0;
				iAktuellerAuftrag++; // den naechsten Auftrag der Liste
				// verarbeiten
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
			setTableProperties(false);
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
	private void setTableProperties(boolean bFillData) throws Throwable {

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();

		// enthaelt Auftrag = AUFTRAG_I_ID
		FilterKriterium fkAuftrag = aFilterKriterium[ProjektFac.IDX_KRIT_LOS];

		// enthaelt KRIT_PERSONAL = true || KRIT_IDENT = true
		FilterKriterium fkAuswertung = aFilterKriterium[ProjektFac.IDX_KRIT_AUSWERTUNG];

		AuftragzeitenDto[] personalZeitenDtos = null;

		// Anordnung der Spalten je nach Filterkriterien
		if (fkAuswertung.kritName.equals(ProjektFac.KRIT_PERSONAL)) {
			personalZeitenDtos = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
							new Integer(Integer.parseInt(fkAuftrag.value)),
							null, null, null, null, false, // order by
							// artikelcnr
							true, // order by personal
							theClientDto);
		} else if (fkAuswertung.kritName.equals(ProjektFac.KRIT_IDENT)) {
			personalZeitenDtos = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
							new Integer(Integer.parseInt(fkAuftrag.value)),
							null, null, null, null, true, // order by artikelcnr
							false, // order by personal
							theClientDto);
		}

		aProjektzeitenDtos = new AuftragzeitenDto[personalZeitenDtos.length];

		int row = 0;

		for (int i = 0; i < personalZeitenDtos.length; i++) {
			aProjektzeitenDtos[row] = personalZeitenDtos[i];
			row++;
		}
		// Berechnung Anzahl der Zeilen :
		// Pro Auftragzeit 1 Kopf pro distinct Personal/Ident + je zwei Zeilen
		// pro Zeit
		//
		// - bei Sortierung nach Personal pro Auftragzeit
		//----------------------------------------------------------------------
		// --------------
		// Header | Pers. Nr. | Pers. name | ------------- | ----- | ------ |
		// Dauer | Kosten
		// Header | ---------- | Identnr. | Identbez. | Datum | Beginn | Dauer |
		// Kosten
		// Header | Grund Ende | ----------- | Zeitb. text | Datum | Ende |
		// Dauer | Kosten
		//----------------------------------------------------------------------
		// --------------
		//
		// - bei Sortierung nach Ident pro Auftragzeit
		//----------------------------------------------------------------------
		// --------------
		// Header | Ident | Identbez. | ------------- | ----- | ------ | Dauer |
		// Kosten
		// Header | ---------- | Pers. Nr. | Pers. name | Datum | Beginn | Dauer
		// | Kosten
		// Header | Grund Ende | ----------- | Zeitb. text | Datum | Ende |
		// Dauer | Kosten
		//----------------------------------------------------------------------
		// --------------

		// hier werden die Koepfe gesammelt, in der HashMap gibt es keine
		// doppelten Keys
		hmKoepfe = new HashMap<String, String>();

		for (int i = 0; i < aProjektzeitenDtos.length; i++) {
			if (fkAuswertung.kritName.equals(ProjektFac.KRIT_PERSONAL)) {
				hmKoepfe.put(aProjektzeitenDtos[i].getSPersonalnummer(), null);
			} else if (fkAuswertung.kritName.equals(ProjektFac.KRIT_IDENT)) {
				hmKoepfe.put(aProjektzeitenDtos[i].getSArtikelcnr(), null);
			}
		}

		int iAnzahlZeilen = hmKoepfe.size() + aProjektzeitenDtos.length * 2;

		setAnzahlZeilen(iAnzahlZeilen);
	}
}
