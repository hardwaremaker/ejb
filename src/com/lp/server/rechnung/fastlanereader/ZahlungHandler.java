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
package com.lp.server.rechnung.fastlanereader;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungZahlung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeRechnungZahlung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.BigDecimalFinanz;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Zahlung implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 *
 * @author Martin Bluehweis
 * @version 1.0
 */

public class ZahlungHandler extends UseCaseHandler {

	private static final long serialVersionUID = 1L;

	protected static int SPALTE_I_ID = 0;
	protected static int SPALTE_ZAHLDATUM = 1;
	protected static int SPALTE_ART = 2;
	protected static int SPALTE_BETRAG = 3;
	protected static int SPALTE_BETRAGUST = 4;
	protected static int SPALTE_OFFEN = 5;
	protected static int SPALTE_KOMMENTAR = 6;

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 *
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			BigDecimal bdOffen = null;

			String[] tooltipData = new String[resultList.size()];

			while (resultListIterator.hasNext()) {
				FLRRechnungZahlung zahlung = (FLRRechnungZahlung) resultListIterator
						.next();

				RechnungDto rDto = getRechnungFac().rechnungFindByPrimaryKey(
						zahlung.getRechnung_i_id());

				BigDecimal nettowert = rDto.getNWertfw();
				BigDecimal ustwert = rDto.getNWertustfw();

				bdOffen = nettowert.add(ustwert);

				if (bdOffen != null) {

					bdOffen = bdOffen
							.subtract(
									getRechnungFac()
											.getBereitsBezahltWertVonRechnungFw(
													zahlung.getRechnung_i_id(),
													zahlung.getI_id())
											.add(getRechnungFac()
													.getBereitsBezahltWertVonRechnungUstFw(
															zahlung.getRechnung_i_id(),
															zahlung.getI_id())))
							.subtract(
									zahlung.getN_betragfw().add(
											zahlung.getN_betrag_ustfw()));

					if (rDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG))
						bdOffen = bdOffen.subtract(
								getRechnungFac()
										.getAnzahlungenZuSchlussrechnungFw(
												rDto.getIId())).subtract(
								getRechnungFac()
										.getAnzahlungenZuSchlussrechnungUstFw(
												rDto.getIId()));
				}

				rows[row][SPALTE_I_ID] = zahlung.getI_id();
				rows[row][SPALTE_ZAHLDATUM] = zahlung.getD_zahldatum();
				// Brutto vorbesetzen
				rows[row][SPALTE_BETRAG] = zahlung.getN_betragfw().add(
						zahlung.getN_betrag_ustfw());
				rows[row][SPALTE_BETRAGUST] = zahlung.getN_betrag_ustfw();

				rows[row][SPALTE_OFFEN] = bdOffen;

				if (zahlung.getC_kommentar() != null && !zahlung.getC_kommentar().isEmpty()) {
					rows[row][SPALTE_KOMMENTAR] = true ;
					tooltipData[row] = zahlung.getC_kommentar().isEmpty() ? null
							: Helper.removeStyles(zahlung.getC_kommentar());
				} else {
					rows[row][SPALTE_KOMMENTAR] = false;
				}

				String zahlungsart = zahlung.getZahlungsart_c_nr();
				String bezahlungsbelegart = zahlung.getFlrrechnung().getFlrrechnungart().getC_nr() ;
				if (bezahlungsbelegart.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT) ||
						bezahlungsbelegart.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {

					if (!zahlung.getZahlungsart_c_nr().equals(
							RechnungFac.ZAHLUNGSART_BANK)
							&& !zahlung.getZahlungsart_c_nr().equals(
									RechnungFac.ZAHLUNGSART_BAR)) {
						zahlungsart = RechnungFac.RECHNUNGART_RECHNUNG;
					}
				}
				String zahlungsartUebersetzt = getRechnungServiceFac()
						.uebersetzeZahlungsartOptimal(zahlungsart,
								theClientDto.getLocUi(),
								theClientDto.getLocMandant());

				String art = "";
				if (zahlungsart.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK)) {
					if (zahlung.getFlrbankverbindung() != null) {
						art = zahlung.getFlrbankverbindung().getFlrbank()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					} else {
						art = getTextRespectUISpr("rech.keinebankdefiniert",
								theClientDto.getMandant(),
								theClientDto.getLocUi());
					}
				} else if (zahlungsart
						.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR)
						&& zahlung.getFlrkassenbuch() != null) {
					art = zahlung.getFlrkassenbuch().getC_bez();
				} else if (zahlungsart
						.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)
						&& zahlung.getFlrrechnunggutschrift() != null) {
					art = zahlung.getFlrrechnunggutschrift().getC_nr();
				} else if (zahlungsart
						.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
						&& zahlung.getFlreingangsrechnung() != null) {
					art = zahlung.getFlreingangsrechnung().getC_nr();
				} else if (zahlungsart
						.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)
						&& zahlung.getFlrfinanzbuchungdetail() != null) {
					art = zahlung.getFlrfinanzbuchungdetail().getFlrbuchung()
							.getC_text();
				} else if (zahlungsart
						.equalsIgnoreCase(RechnungFac.RECHNUNGART_RECHNUNG)
						&& zahlung.getFlrrechnung() != null) {
					if(zahlung.getFlrrechnunggutschrift() == null) {
						art = "?GU?" + zahlung.getFlrrechnung().getC_nr();
 					} else {
 						art = zahlung.getFlrrechnunggutschrift().getC_nr();
 					}
				}
				rows[row][SPALTE_ART] = zahlungsartUebersetzt + ": " + art;
				row++;
			}

//			result = new QueryResult(rows, this.getRowCount(), startIndex,
//					endIndex, 0);

			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				if(session != null) {
					session.close();
				}
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 *
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return rowCount;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 *
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;

			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(" zahlung." + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("zahlung." + kriterien[i].kritName);
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
				orderBy.append("zahlung.d_zahldatum DESC, zahlung.i_id DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("zahlung.d_zahldatum DESC, zahlung.i_id DESC ") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" zahlung.d_zahldatum DESC, zahlung.i_id DESC  ");
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
		return "from FLRRechnungZahlung zahlung ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 *
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select zahlung.i_id from FLRRechnungZahlung zahlung "
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
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
				}
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	/**
	 * gets information about the Zahlungstable.
	 *
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(
				new TableInfo(
					new Class[] {
						Integer.class,
						Date.class,
						String.class,
						BigDecimal.class,
						BigDecimal.class,
						BigDecimalFinanz.class,
						Boolean.class
					},
					new String[] {
						"Id",
						getTextRespectUISpr("lp.zahldatum", mandantCNr, locUI),
						getTextRespectUISpr("lp.art", mandantCNr, locUI),
						getTextRespectUISpr("lp.betrag", mandantCNr, locUI),
						getTextRespectUISpr("lp.mwst", mandantCNr, locUI),
						getTextRespectUISpr("lp.bruttooffen", mandantCNr, locUI),
						getTextRespectUISpr("lp.kommentar", mandantCNr,	locUI)
					},
					new int[] {
						-1,
						QueryParameters.FLR_BREITE_M,
						-1,
						QueryParameters.FLR_BREITE_PREIS,
						QueryParameters.FLR_BREITE_PREIS,
						QueryParameters.FLR_BREITE_PREIS,
						QueryParameters.FLR_BREITE_S
					},
					new String[] {
						RechnungFac.FLR_RECHNUNG_ZAHLUNG_I_ID,
						Facade.NICHT_SORTIERBAR,
						Facade.NICHT_SORTIERBAR,
						Facade.NICHT_SORTIERBAR,
						Facade.NICHT_SORTIERBAR,
						Facade.NICHT_SORTIERBAR,
						Facade.NICHT_SORTIERBAR
					}
				)
			);
		}

		return super.getTableInfo();
	}

	@Override
	public PrintInfoDto getSDocPathAndPartner(Object key) {
		RechnungDto rechnungDto = null;
		RechnungzahlungDto zahlung = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			zahlung = getRechnungFac()
					.zahlungFindByPrimaryKey((Integer) key);
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
					zahlung.getRechnungIId());
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (rechnungDto != null && zahlung != null) {
			DocPath docPath = new DocPath(new DocNodeRechnungZahlung(zahlung, rechnungDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	@Override
	public String getSTable() {
		return "RE ZAHLUNG";
	}
}
