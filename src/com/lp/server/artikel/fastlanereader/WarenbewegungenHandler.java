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
package com.lp.server.artikel.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.MandantFac;
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

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Handlagerbewegungen
 * implementiert. Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2004-11-25
 * </p>
 * <p>
 * </p>
 * 
 * @author Christian Kollmann
 * @version 1.0
 */

public class WarenbewegungenHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String DOCNODE_CHARGENDOKUMENTE = "Chargendokumente";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator.next();
				rows[row][col++] = lagerbewegung.getI_id_buchung();
				rows[row][col++] = lagerbewegung.getFlrartikel().getC_nr();
				rows[row][col++] = lagerbewegung.getFlrartikel().getC_referenznr();
				rows[row][col++] = lagerbewegung.getC_seriennrchargennr();
				rows[row][col++] = lagerbewegung.getN_menge();
				rows[row][col++] = lagerbewegung.getC_belegartnr().trim();
				try {
					rows[row][col++] = getLagerFac().getBelegInfos(lagerbewegung.getC_belegartnr(),
							lagerbewegung.getI_belegartid(), null, theClientDto).formatBelegnummerBezeichnung();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				rows[row][col++] = new java.sql.Date(lagerbewegung.getT_belegdatum().getTime()) + " - "
						+ new java.sql.Time(lagerbewegung.getT_belegdatum().getTime()).toString();

				rows[row][col++] = new java.sql.Date(lagerbewegung.getT_buchungszeit().getTime()) + " - "
						+ new java.sql.Time(lagerbewegung.getT_buchungszeit().getTime()).toString();
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
				}
			}
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

		String mandant = null;

		mandant = theClientDto.getMandant();

		where = new StringBuffer(" WHERE 1=1 ");

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)
				|| (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)
						&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto))) {
			where.append("AND lagerbewegung." + LagerFac.FLR_LAGERBEWEGUNG_FLRLAGER + ".mandant_c_nr='" + mandant + "' ");
		}

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = true;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" lower(" + filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toLowerCase());
					} else {
						where.append(" " + filterKriterien[i].value);
					}
				}
			}
			// /if (filterAdded) {
			// where.insert(0, " WHERE");
			// }
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
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append("lagerbewegung." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("lagerbewegung." + LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("lagerbewegung." + LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" lagerbewegung." + LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT + " ");
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
		return " from FLRLagerbewegung lagerbewegung ";
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {

		ArtikelDto artikelDto = null;
		try {
			LagerbewegungDto buchung = getLagerFac().getJuengsteBuchungEinerBuchungsNummer((Integer) key);

			String snrchr = "KEINE_SNRCHNR";

			if (buchung.getCSeriennrchargennr() != null) {
				snrchr = buchung.getCSeriennrchargennr();
			}

			artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(buchung.getArtikelIId(), theClientDto);

			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new HeliumDocPath().add(new DocNodeLiteral(theClientDto.getMandant()))
					.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL)).add(new DocNodeFolder(artikelDto.getCNr()))
					.add(new DocNodeFolder(WarenbewegungenHandler.DOCNODE_CHARGENDOKUMENTE))
					.add(new DocNodeFolder(snrchr)));

			values.setTable("");
			return values;
		} catch (Exception e) {
			return null;
		}

	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null /* && ((Integer) selectedId).intValue() >= 0 */) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select lagerbewegung." + LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG
						+ " from FLRLagerbewegung lagerbewegung " + this.buildWhereClause() + this.buildOrderByClause();
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
			} catch (HibernateException e) {
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class, String.class, BigDecimal.class,
							String.class, String.class, String.class, String.class },
					new String[] { "PK", getTextRespectUISpr("artikel.artikelnummerlang", mandantCNr, locUI),
							getTextRespectUISpr("lp.referenznummer", mandantCNr, locUI),
							getTextRespectUISpr("lp.snrchargennr", mandantCNr, locUI),
							getTextRespectUISpr("lp.menge", mandantCNr, locUI),
							getTextRespectUISpr("lp.belegart", mandantCNr, locUI),
							getTextRespectUISpr("lp.belegnummer", mandantCNr, locUI),
							getTextRespectUISpr("bes.belegdatum", mandantCNr, locUI),
							getTextRespectUISpr("lp.buchungszeitpunkt", mandantCNr, locUI) },
					new String[] { LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG,
							LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL + ".c_nr",
							LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL + ".c_referenznr",
							LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR, LagerFac.FLR_LAGERBEWEGUNG_N_MENGE,
							LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, Facade.NICHT_SORTIERBAR,
							LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT }));

		}
		return super.getTableInfo();
	}
}
