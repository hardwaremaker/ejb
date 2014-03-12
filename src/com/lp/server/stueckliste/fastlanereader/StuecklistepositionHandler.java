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
package com.lp.server.stueckliste.fastlanereader;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeStuecklistePosition;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Montageart implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class StuecklistepositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			String queryString = "SELECT stuecklisteposition,(SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=stuecklisteposition.flrartikel.i_id) as sperren from FLRStuecklisteposition stuecklisteposition"
					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRStuecklisteposition stuecklisteposition = (FLRStuecklisteposition) o[0];
				rows[row][col++] = stuecklisteposition.getI_id();

				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								stuecklisteposition.getFlrartikel().getI_id(),
								theClientDto);
				if (stuecklisteDto == null) {
					rows[row][col++] = new Boolean(false);
				} else {
					rows[row][col++] = new Boolean(true);
				}
				ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKeySmall(
						stuecklisteposition.getFlrartikel().getI_id(),
						theClientDto);

				if (dto.getArtikelartCNr() != null
						&& dto.getArtikelartCNr()
								.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					rows[row][col++] = "";

				} else {
					rows[row][col++] = dto.getCNr();

				}

				rows[row][col++] = dto.formatBezeichnung();

				rows[row][col++] = stuecklisteposition.getN_menge();
				rows[row][col++] = stuecklisteposition.getC_position();
				rows[row][col++] = new Boolean(
						Helper.short2boolean(stuecklisteposition
								.getB_mitdrucken()));

				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}

				if (stuecklisteposition.getPosersatzset().size() > 0) {
					rows[row][col++] = new Color(89, 188, 41);
				} else {
					rows[row][col++] = null;
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(stuecklisteposition."
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" stuecklisteposition."
								+ filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" "
								+ filterKriterien[i].value.toUpperCase());
					} else {
						where.append(" " + filterKriterien[i].value);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("stuecklisteposition."
									+ kriterien[i].kritName);
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
				orderBy.append("stuecklisteposition."
						+ StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT
						+ " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("stuecklisteposition."
					+ StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" stuecklisteposition."
						+ StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT + " ");
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
		return "from FLRStuecklisteposition stuecklisteposition ";
	}

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
				String queryString = "select stuecklisteposition.i_id from FLRStuecklisteposition stuecklisteposition "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
//				boolean idFound = false;
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

	public TableInfo getTableInfo() {

		try {
			if (super.getTableInfo() == null) {
				String mandantCNr = theClientDto.getMandant();
				Locale locUI = theClientDto.getLocUi();

				int iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(mandantCNr);

				setTableInfo(new TableInfo(
						new Class[] {
								Integer.class,
								Boolean.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class, Boolean.class, Icon.class,
								Color.class },
						new String[] {
								"Id",
								getTextRespectUISpr("stkl.unterstkl",
										mandantCNr, locUI),
								getTextRespectUISpr("artikel.artikelnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.bezeichnung",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.position", mandantCNr,
										locUI),
								getTextRespectUISpr(
										"stkl.pos.aufbelegmitdrucken",
										mandantCNr, locUI), "S", "" },
						new int[] {
								-1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_L,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_S, // TRUE/FALSE
								QueryParameters.FLR_BREITE_S, 1 },
						new String[] {
								"id",
								Facade.NICHT_SORTIERBAR,
								StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL
										+ ".c_nr",
								Facade.NICHT_SORTIERBAR,
								StuecklisteFac.FLR_STUECKLISTEPOSITION_N_MENGE,
								StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION,
								StuecklisteFac.FLR_STUECKLISTEPOSITION_B_MITDRUCKEN,
								Facade.NICHT_SORTIERBAR, "" }));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		StuecklistepositionDto stuecklistepositionDto = null;
		StuecklisteDto stuecklisteDto = null;
		ArtikelDto artikelDto = null;
		try {
			stuecklistepositionDto = getStuecklisteFac()
					.stuecklistepositionFindByPrimaryKey((Integer) key,
							theClientDto);
			stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
					stuecklistepositionDto.getStuecklisteIId(), theClientDto);
			artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteDto.getArtikelIId(), theClientDto);
			stuecklisteDto.setArtikelDto(artikelDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (artikelDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_ARTIKEL.trim() + "/"
//					+ artikelDto.getArtikelartCNr().trim() + "/"
//					+ artikelDto.getCNr().replace("/", ".") + "/"
//					+ "Stuecklistenpositionen/" + "Position "
//					+ stuecklistepositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeStuecklistePosition(stuecklistepositionDto, artikelDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "STUECKLISTE";
	}
}
