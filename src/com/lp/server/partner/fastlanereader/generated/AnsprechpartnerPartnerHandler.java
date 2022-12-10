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
package com.lp.server.partner.fastlanereader.generated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.HelperFuerPartnerGoto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich Ansprechpartner-FLR.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 28.10.04
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version $Revision: 1.2 $ Date $Date: 2012/09/04 09:48:55 $
 */

public class AnsprechpartnerPartnerHandler extends UseCaseHandlerTabelle {

	private ArrayList<Object[]> hmDaten = null;

	private int SPALTE_MANDANT = 1;

	private int SPALTE_ART = 2;
	private int SPALTE_PARTNERART = 3;
	private int SPALTE_NAME1 = 4;
	private int SPALTE_NAME2 = 5;
	private int SPALTE_LKZ = 6;
	private int SPALTE_PLZ = 7;
	private int SPALTE_ORT = 8;

	private int ANZAHL_SPALTEN = 9;

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
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
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

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
							String.class, String.class, String.class },
					new String[] { "", getTextRespectUISpr("report.mandant", mandantCNr, locUI),
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("lp.firma_nachname", mandantCNr, locUI),
							getTextRespectUISpr("lp.firma_vorname", mandantCNr, locUI),
							getTextRespectUISpr("lp.lkz", mandantCNr, locUI),
							getTextRespectUISpr("lp.plz", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M },
					new String[] { "", "", "", "", "", "", "",

							"",

							""

					});
		}
		return tableInfo;
	}

	private void setInhalt() throws Throwable {

		hmDaten = new ArrayList<Object[]>();

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();

		Integer partnerIId = new Integer(aFilterKriterium[0].value);

		int iLfd = 0;

		// Ansprechpartner

		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session
				.createQuery("SELECT ansp FROM FLRAnsprechpartner ansp WHERE ansp.partner_i_id_ansprechpartner="
						+ partnerIId + " ORDER BY ansp.i_sort");

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRAnsprechpartner ansprechpartner = (FLRAnsprechpartner) resultListIterator.next();

			Object[] zeile = new Object[ANZAHL_SPALTEN];

			zeile[0] = new HelperFuerPartnerGoto(ansprechpartner.getPartner_i_id(), HelperFuerPartnerGoto.PARTNER,
					theClientDto.getMandant(), ansprechpartner.getI_id());

			zeile[SPALTE_PARTNERART] = ansprechpartner.getFlrpartner().getPartnerart_c_nr();
			zeile[SPALTE_NAME1] = ansprechpartner.getFlrpartner().getC_name1nachnamefirmazeile1();
			zeile[SPALTE_NAME2] = ansprechpartner.getFlrpartner().getC_name2vornamefirmazeile2();

			if (ansprechpartner.getFlrpartner().getFlrlandplzort() != null) {
				zeile[SPALTE_LKZ] = ansprechpartner.getFlrpartner().getFlrlandplzort().getFlrland().getC_lkz();
				zeile[SPALTE_PLZ] = ansprechpartner.getFlrpartner().getFlrlandplzort().getC_plz();
				zeile[SPALTE_ORT] = ansprechpartner.getFlrpartner().getFlrlandplzort().getFlrort().getC_name();
			}

			boolean bZeileAdded = false;

			// Kunden

			Session sessionKD = FLRSessionFactory.getFactory().openSession();
			Query queryKD = sessionKD.createQuery("SELECT kd FROM FLRKunde kd WHERE kd.flrpartner.i_id="
					+ ansprechpartner.getPartner_i_id() + " ORDER BY kd.mandant_c_nr");

			List<?> resultListKD = queryKD.list();
			Iterator<?> resultListIteratorKD = resultListKD.iterator();
			while (resultListIteratorKD.hasNext()) {
				FLRKunde kd = (FLRKunde) resultListIteratorKD.next();
				Object[] zeileKD = zeile.clone();

				zeileKD[0] = new HelperFuerPartnerGoto(kd.getI_id(), HelperFuerPartnerGoto.KUNDE, kd.getMandant_c_nr(),
						ansprechpartner.getI_id());

				zeileKD[SPALTE_MANDANT] = kd.getMandant_c_nr();
				zeileKD[SPALTE_ART] = getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
						theClientDto.getLocUi());
				hmDaten.add(zeileKD);
				iLfd++;
				bZeileAdded = true;
			}
			sessionKD.close();

			// Lieferanten

			Session sessionLF = FLRSessionFactory.getFactory().openSession();
			Query queryLF = sessionLF.createQuery("SELECT lf FROM FLRLieferant lf WHERE lf.flrpartner.i_id="
					+ ansprechpartner.getPartner_i_id() + " ORDER BY lf.mandant_c_nr");

			List<?> resultListLF = queryLF.list();
			Iterator<?> resultListIteratorLF = resultListLF.iterator();
			while (resultListIteratorLF.hasNext()) {
				FLRLieferant kd = (FLRLieferant) resultListIteratorLF.next();
				Object[] zeileKD = zeile.clone();

				zeileKD[0] = new HelperFuerPartnerGoto(kd.getI_id(), HelperFuerPartnerGoto.LIEFERANT,
						kd.getMandant_c_nr(), ansprechpartner.getI_id());

				zeileKD[SPALTE_MANDANT] = kd.getMandant_c_nr();
				zeileKD[SPALTE_ART] = getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(),
						theClientDto.getLocUi());
				hmDaten.add(zeileKD);
				iLfd++;
				bZeileAdded = true;
			}
			sessionLF.close();

			if (bZeileAdded == false) {
				hmDaten.add(zeile);

			}

			iLfd++;

		}

		session.close();

		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);
	}
}
