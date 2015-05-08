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
package com.lp.server.partner.fastlanereader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.artikel.fastlanereader.generated.FLRFarbcode;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
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

public class PartnerReferenzHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Object[]> hmDaten = null;

	private int SPALTE_MANDANT = 1;
	private int SPALTE_ART = 2;

	private int ANZAHL_SPALTEN = 3;

	/**
	 * Konstruktor.
	 */
	public PartnerReferenzHandler() {
		super();
		setAnzahlSpalten(3);
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(new Class[] { Object.class, String.class,
					String.class, },
			// die Spaltenueberschriften werden durch die Kriterien
			// bestimmt
					new String[] {
							" ",
							getTextRespectUISpr("report.mandant", mandantCNr, locUI),
							getTextRespectUISpr("lp.art", mandantCNr, locUI), },
					// die Breite der Spalten festlegen
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, }, // hidden
					new String[] { "", "", "", });
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

	private void setInhalt() throws Throwable {

		hmDaten = new ArrayList<Object[]>();

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();

		Integer partnerIId = new Integer(aFilterKriterium[0].value);

	

		int iLfd = 0;

	

		// Kunden

		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session
				.createQuery("SELECT kd FROM FLRKunde kd WHERE kd.flrpartner.i_id="
						+ partnerIId + " ORDER BY kd.mandant_c_nr");

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRKunde kd = (FLRKunde) resultListIterator.next();

			Object[] zeile = new Object[ANZAHL_SPALTEN];

			zeile[0] = iLfd;
			zeile[SPALTE_MANDANT] = kd.getMandant_c_nr();
			zeile[SPALTE_ART] = getTextRespectUISpr("lp.kunde",
					theClientDto.getMandant(), theClientDto.getLocUi());

			hmDaten.add(zeile);

			iLfd++;

		}

		session.close();

		// Lieferanten

		session = FLRSessionFactory.getFactory().openSession();
		query = session
				.createQuery("SELECT lf FROM FLRLieferant lf WHERE lf.flrpartner.i_id="
						+ partnerIId + " ORDER BY lf.mandant_c_nr");

		resultList = query.list();
		resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRLieferant lf = (FLRLieferant) resultListIterator.next();

			Object[] zeile = new Object[ANZAHL_SPALTEN];

			zeile[0] = iLfd;
			zeile[SPALTE_MANDANT] = lf.getMandant_c_nr();
			zeile[SPALTE_ART] = getTextRespectUISpr("lp.lieferant",
					theClientDto.getMandant(), theClientDto.getLocUi());

			hmDaten.add(zeile);

			iLfd++;

		}

		session.close();
		
		
		// Personal

				session = FLRSessionFactory.getFactory().openSession();
				query = session
						.createQuery("SELECT p FROM FLRPersonal p WHERE p.flrpartner.i_id="
								+ partnerIId + " ORDER BY p.mandant_c_nr");

				resultList = query.list();
				resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {

			FLRPersonal p = (FLRPersonal) resultListIterator.next();

					Object[] zeile = new Object[ANZAHL_SPALTEN];

					zeile[0] = iLfd;
					zeile[SPALTE_MANDANT] = p.getMandant_c_nr();
					zeile[SPALTE_ART] = getTextRespectUISpr("lp.personal",
							theClientDto.getMandant(), theClientDto.getLocUi());

					hmDaten.add(zeile);

					iLfd++;

				}

				session.close();
		

		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);
	}
}
