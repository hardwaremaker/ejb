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
package com.lp.server.personal.fastlanereader;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdatenpruefen;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektDto;
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

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Zeitdaten implementiert.
 * Pro UseCase gibt es einen Handler.
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
public class ZeitdatenpruefenHandler extends UseCaseHandler {
	private static final long serialVersionUID = 1L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length + 1;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<Object[]> resultList = query.list();

			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			for (Object[] o : resultList) {			
				FLRZeitdatenpruefen zeitdaten = (FLRZeitdatenpruefen) o[0];
				rows[row][col++] = zeitdaten.getI_id();

				if (zeitdaten.getFlrtaetigkeit() != null) {
					try {
						rows[row][col++] = getZeiterfassungFac()
								.taetigkeitFindByPrimaryKey(
										zeitdaten.getFlrtaetigkeit().getI_id(),
										theClientDto).getBezeichnung();
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}
				} else {
					rows[row][col++] = zeitdaten.getFlrartikel() != null ? zeitdaten.getFlrartikel().getC_nr() : null;
				}

				rows[row][col++] = getZusatztext(zeitdaten);
				rows[row][col++] = new java.sql.Date(zeitdaten.getT_zeit().getTime()).toString();
				rows[row][col++] = new java.sql.Time(zeitdaten.getT_zeit()
						.getTime()).toString();
				rows[row][col++] = zeitdaten.getI_fehlercode();
				rows[row++][col++] = zeitdaten.getC_wowurdegebucht();

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		
		return result;
	}


	private String getZusatztext(FLRZeitdatenpruefen zeitdaten) {
		if(zeitdaten.getArtikel_i_id() == null) return null;
		if(zeitdaten.getI_belegartid() == null) return null;
		
		if(LocaleFac.BELEGART_AUFTRAG.equals(zeitdaten.getC_belegartnr())) {
			AuftragDto auftragDto = getAuftragFac()
					.auftragFindByPrimaryKeyOhneExc(
							zeitdaten.getI_belegartid());

			String sZusatz = "AB" + (auftragDto != null ? auftragDto.getCNr() : "?");
			if (auftragDto != null && auftragDto.getCBezProjektbezeichnung() != null) {
				sZusatz += ","
						+ auftragDto
								.getCBezProjektbezeichnung();
			}
			return sZusatz;
		} else if (LocaleFac.BELEGART_PROJEKT.equals(zeitdaten.getC_belegartnr())) {
			ProjektDto projektDto = getProjektFac()
						.projektFindByPrimaryKeyOhneExc(
								zeitdaten.getI_belegartid());

			String sZusatz = "PJ" + (projektDto != null ? projektDto.getCNr() : "?");
			if (projektDto != null && projektDto.getCTitel() != null) {
				sZusatz += ", " + projektDto.getCTitel();
			}
			return sZusatz;				
		} else if (LocaleFac.BELEGART_ANGEBOT.equals(
					zeitdaten.getC_belegartnr())) {
			AngebotDto angebot = getAngebotFac()
					.angebotFindByPrimaryKeyOhneExec(
							zeitdaten.getI_belegartid());

			String sZusatz = "AG" + angebot.getCNr();
			if (angebot.getCBez() != null) {
				sZusatz += "," + angebot.getCBez();
			}
			
			return sZusatz;
		}
		
		return null;
	}

	
	protected long getRowCountFromDataBase() {
		String queryString = "select count(*) " + 
				getFromClause() + buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
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
						where.append(" upper(" + filterKriterien[i].kritName
								+ ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
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
					// nodbfeld: 2: Hier alle Spaltennamen, die mit X enden beim
					// sortieren ignorieren.
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("zeitdaten." + kriterien[i].kritName);
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
				orderBy.append("zeitdaten."
						+ ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("zeitdaten."
					+ ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" zeitdaten."
						+ ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ");
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
		return "from FLRZeitdatenpruefen zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer
				&& ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select zeitdaten.i_id from FLRZeitdatenpruefen zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr  "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = scrollableResult.getInteger(0);
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class,
							String.class, String.class, String.class, String.class,
							Integer.class,
							String.class, String.class },
					new String[] { "Id",
							getTextRespectUISpr("lp.taetigkeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.zusatz", mandantCNr, locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.zeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.fehlercode", mandantCNr, locUI),
							getTextRespectUISpr("lp.bem", mandantCNr, locUI),
							getTextRespectUISpr("lp.quelle", mandantCNr, locUI), },
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_M },
					new String[] {
							"i_id",
							// nodbfeld: 1: Hier an jede Spalte, die nicht
							// sortiert werden kann ein Facade.NICHT_SORTIERBAR
							// anfuegen, damit
							// damit am Client das Symbol 'nosort.png' im
							// TabelHeader angezeigt wird
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATENPRUEFEN_T_DATUM,
							ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
							ZeiterfassungFac.FLR_ZEITDATENPRUEFEN_I_FEHLERCODE,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATEN_C_WOWURDEGEBUCHT }));
		}

		return super.getTableInfo();
	}
}
