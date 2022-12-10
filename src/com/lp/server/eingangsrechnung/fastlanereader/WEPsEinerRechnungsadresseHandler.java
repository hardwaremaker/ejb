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
package com.lp.server.eingangsrechnung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeWEPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>FLR-Handler fuer Wareneingangspositionen auf Basis der
 * Bestellpositionen</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>10.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Josef Erlinger
 * @version 1.0
 */
public class WEPsEinerRechnungsadresseHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
	 * 
	 * @param rowIndex the index of the row that should be contained in the page.
	 * @return the data page for the specified row.
	 * @throws EJBExceptionLP
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = WEPsEinerRechnungsadresseHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
					theClientDto);
			// flrextradata: 1 Extra Daten, die man im FLR auswerten kann,
			// lesen.

			while (resultListIterator.hasNext()) {

				FLRWareneingangspositionen weposition = (FLRWareneingangspositionen) resultListIterator.next();

				FLRBestellposition position = weposition.getFlrbestellposition();

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = weposition.getI_id();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.bespos.nr")] = getBestellpositionFac()
						.getPositionNummerReadOnly(position.getI_id());

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.bspos_menge")] = position.getN_menge();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bestellnummer")] = position
						.getFlrbestellung().getC_nr();

				// die sprachabhaengig Artikelbezeichnung anzeigen

				if (position.getBestellpositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
					if (position.getC_textinhalt() != null && position.getC_textinhalt().length() > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.bezeichnung")] = Helper
								.strippHTML(position.getC_textinhalt());
					}
				} else {
					if (position.getFlrartikel() != null) {
						String sBezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
								position.getFlrartikel().getI_id(), theClientDto.getLocUi());
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("artikel.artikelnummerlang")] = position.getFlrartikel().getC_nr();

						rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.bezeichnung")] = sBezeichnung;

						if (bReferenznummerInPositionen) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.referenznummer")] = position
									.getFlrartikel().getC_referenznr();
						}

					}
				}

				String sStatusUebersetzt = getSystemMultilanguageFac()
						.uebersetzeStatusOptimal(position.getBestellpositionstatus_c_nr(), theClientDto.getLocUi());
				rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.erledigt")] = sStatusUebersetzt;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.lieferscheinnr")] = weposition
						.getFlrwareneingang().getC_lieferscheinnr();

				WareneingangspositionDto aWEPOSDto = getWareneingangFac()
						.wareneingangspositionFindByPrimaryKey(weposition.getI_id());

				// insgesamt gelieferte Menge dieser Bestellposition.
				BigDecimal bdGelieferteMengeDerBespos = new BigDecimal(0);

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.gelmenge")] = aWEPOSDto
						.getNGeliefertemenge();
				if (aWEPOSDto.getBPreiseErfasst() == null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.preis")] = false;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.preis")] = aWEPOSDto
							.getBPreiseErfasst();
				}
				if (bDarfPreiseSehen) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.gelpreis")] = aWEPOSDto
							.getNGelieferterpreis(); // gelieferter
					// Preis
					if (aWEPOSDto.getNGelieferterpreis() == null || aWEPOSDto.getNGeliefertemenge() == null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.zeilensumme")] = null;
					} else {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.zeilensumme")] = aWEPOSDto
								.getNGelieferterpreis().multiply(aWEPOSDto.getNGeliefertemenge());
					}
				}

				bdGelieferteMengeDerBespos = bdGelieferteMengeDerBespos.add(aWEPOSDto.getNGeliefertemenge());

			

				BigDecimal bdOffen = getBestellpositionFac().berechneOffeneMenge(position.getI_id());
				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.offenemenge")] = bdOffen;
				// }

				rows[row] = rowToAddCandidate;

				row++;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * gets the total number of rows available using the current query.
	 * 
	 * @return the number of rows in the query.
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "select count(*) from FLRWareneingangspositionen position " + this.buildWhereClause();

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} finally {
			closeSession(session);
		}
		return rowCount;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					if (filterKriterien[i].kritName.equals("textsuche")) {
						where.append("( lower(position.c_bezeichnung)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(position.flrartikel.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(aspr.c_bez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(aspr.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(aspr.c_zbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(aspr.c_zbez2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(position.flrartikel.c_artikelbezhersteller)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(position.flrartikel.c_artikelnrhersteller)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value);
						where.append(" OR lower(position.c_zusatzbezeichnung)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" LOWER(position." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" position." + filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 * 
	 * @return the HQL order by clause.
	 */
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("position." + kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				// orderBy.append("position.i_id ASC ");
				orderBy.append("position.flrwareneingang.flrbestellung.c_nr").append(" ASC ");

				sortAdded = true;
			}

			if (orderBy.indexOf("position.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" position.i_id ");
				sortAdded = true;
			}

			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "SELECT position from FLRWareneingangspositionen position ";
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {
			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(mandantCNr);

			columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");
			columns.add("lp.bestellnummer", String.class, getTextRespectUISpr("lp.bestellnummer", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			columns.add("bes.lieferscheinnr", String.class, getTextRespectUISpr("bes.lieferscheinnr", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			columns.add("bes.bespos.nr", Integer.class, getTextRespectUISpr("bes.bespos.nr", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

			columns.add("bes.offenemenge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("bes.offenemenge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("bes.gelmenge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("bes.gelmenge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("bes.bspos_menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("bes.bspos_menge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					"flrbestellposition."+BestellpositionFac.FLR_BESTELLPOSITION_N_MENGE);
			columns.add("artikel.artikelnummerlang", String.class,
					getTextRespectUISpr("artikel.artikelnummerlang", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					"flrbestellposition."+AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM,
						"flrbestellposition."+AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr");
			}

			columns.add("bes.bezeichnung", String.class, getTextRespectUISpr("bes.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrbestellposition."+BestellpositionFac.FLR_BESTELLPOSITION_C_BEZEICHNUNG);
			columns.add("bes.gelpreis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("bes.gelpreis", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.zeilensumme", super.getUIClassBigDecimalNachkommastellen(2),
					getTextRespectUISpr("lp.zeilensumme", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.preis", Boolean.class, getTextRespectUISpr("lp.preis", mandant, locUi),
					QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);
			columns.add("auft.erledigt", String.class, getTextRespectUISpr("auft.erledigt", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	/**
	 * sorts the data of the current query using the specified criterias and returns
	 * the page of data where the row of selectedId is contained.
	 * 
	 * @param sortierKriterien the new sort criterias.
	 * @param selectedId       the id of the entity that should be included in the
	 *                         result page.
	 * @return the sorted data containing the page where the entity with the
	 *         specified id is located.
	 * @throws EJBExceptionLP
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select position." + "i_id" + " from FLRWareneingangspositionen position "
						+ this.buildWhereClause() + this.buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = (Integer) scrollableResult.getInteger(0);
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				closeSession(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		WareneingangspositionDto wePosDto = null;
		WareneingangDto weDto = null;
		BestellungDto bestDto = null;
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {

			if (key != null) {

				wePosDto = getWareneingangFac().wareneingangspositionFindByPrimaryKey((Integer) key);
				weDto = getWareneingangFac().wareneingangFindByPrimaryKey(wePosDto.getWareneingangIId());
				bestDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(bestDto.getLieferantIIdBestelladresse(),
						theClientDto);
				if (lieferantDto != null) {
					partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
				}
			}
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (bestDto != null && weDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_BESTELLUNG.trim() + "/"
			// + bestDto.getBelegartCNr().trim() + "/"
			// + bestDto.getCNr().replace("/", ".") + "/"
			// + "Wareneingaenge/" + "Wareneingang " + weDto.getIId() + "/"
			// + "Wareneinganspositionen/" + "Wareneingangsposition"
			// + wePosDto.getIId();
			DocPath docPath = new DocPath(new DocNodeWEPosition(wePosDto, weDto, bestDto));
			Integer sPartnerIId = null;
			if (partnerDto != null) {
				sPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, sPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "WARENEINGANGSPOSITIONEN";
	}
}
