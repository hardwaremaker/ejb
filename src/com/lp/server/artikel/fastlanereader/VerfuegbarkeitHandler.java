
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.fertigung.fastlanereader.generated.FLRVerfuegbarkeit;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * Hier wird die FLR Funktionalitaet fuer die Fehlmengen implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007,2005,2006
 * </p>
 * <p>
 * Erstellungsdatum 13.10.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class VerfuegbarkeitHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_VERFUEGBARKEIT = "flrverfuegbarkeit.";
	private static final String FLR_VERFUEGBARKEIT_FROM_CLAUSE = "SELECT flrverfuegbarkeit,aspr,(SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrverfuegbarkeit.flrartikelliste.i_id AND s.i_sort=1) as sperren from FLRVerfuegbarkeit flrverfuegbarkeit LEFT OUTER JOIN flrverfuegbarkeit.flrartikelliste.artikelsprset AS aspr";

	private boolean bLagerinfo = false;
	boolean bErsatztypen = false;

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
			session = setFilter(session);
			String queryString = "SELECT flrverfuegbarkeit,aspr,(SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrverfuegbarkeit.flrartikelliste.i_id AND s.i_sort=1) as sperren, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=flrverfuegbarkeit.artikel_i_id AND artikellager.flrlager.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "'  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT
					+ "')), (SELECT sum(fm.n_menge) FROM FLRFehlmenge AS fm WHERE fm.artikel_i_id=flrverfuegbarkeit.artikel_i_id), (SELECT sum(ar.n_menge) FROM FLRArtikelreservierung AS ar WHERE ar.flrartikel.i_id=flrverfuegbarkeit.artikel_i_id), (SELECT sum(bs.n_menge) FROM FLRArtikelbestellt AS bs WHERE bs.flrartikel.i_id=flrverfuegbarkeit.artikel_i_id)    from FLRVerfuegbarkeit flrverfuegbarkeit LEFT OUTER JOIN flrverfuegbarkeit.flrartikelliste.artikelsprset AS aspr"
					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			LossollmaterialDto[] sollmatDtos = null;

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRVerfuegbarkeit fm = (FLRVerfuegbarkeit) o[0];
				FLRArtikellistespr aspr = (FLRArtikellistespr) o[1];
				rows[row][col++] = fm.getArtikel_i_id();
				rows[row][col++] = fm.getFlrartikelliste().getC_nr();

				if (bReferenznummerInPositionen) {
					rows[row][col++] = fm.getFlrartikelliste().getC_referenznr();
				}

				if (aspr != null) {
					rows[row][col++] = aspr.getC_bez();

					rows[row][col++] = aspr.getC_zbez();

				} else {
					rows[row][col++] = null;

					rows[row][col++] = null;

				}

				rows[row][col++] = fm.getN_sollmenge().subtract(fm.getN_lagerstand());

				if (bLagerinfo) {

					BigDecimal fehlmenge = (BigDecimal) o[4];
					if (fehlmenge == null) {
						fehlmenge = BigDecimal.ZERO;
					}

					BigDecimal reservierungen = (BigDecimal) o[5];
					if (reservierungen == null) {
						reservierungen = BigDecimal.ZERO;
					}

					BigDecimal bestellt = (BigDecimal) o[6];
					if (bestellt == null) {
						bestellt = BigDecimal.ZERO;
					}

					rows[row][col++] = fm.getN_lagerstand();

					if (bErsatztypen) {

						if (sollmatDtos == null) {
							sollmatDtos = getFertigungFac().lossollmaterialFindByLosIId(fm.getLos_i_id());
						}

						// Ich bin Hauptartikel
						BigDecimal bdLagerstandErsatztypen = BigDecimal.ZERO;

						for (LossollmaterialDto sollMatDto : sollmatDtos) {

							if (sollMatDto.getArtikelIId() != null
									&& sollMatDto.getArtikelIId().equals(fm.getArtikel_i_id())) {

								if (fm.getArtikel_i_id() != null
										&& sollMatDto.getLossollmaterialIIdOriginal() == null) {

									HashSet<Integer> hsArtikelIIdsErsatztypen = new HashSet<Integer>();

									LossollmaterialDto[] ersatzDtos = getFertigungFac()
											.lossollmaterialFindByLossollmaterialIIdOriginal(sollMatDto.getIId());
									for (LossollmaterialDto tempDto : ersatzDtos) {
										hsArtikelIIdsErsatztypen.add(tempDto.getArtikelIId());
									}

									ErsatztypenDto[] ersatztypenAusArtikelDtos = getArtikelFac()
											.ersatztypenFindByArtikelIId(fm.getArtikel_i_id());

									for (ErsatztypenDto tempDto : ersatztypenAusArtikelDtos) {
										hsArtikelIIdsErsatztypen.add(tempDto.getArtikelIIdErsatz());
									}

									Iterator<Integer> itArtikelId = hsArtikelIIdsErsatztypen.iterator();
									while (itArtikelId.hasNext()) {
										bdLagerstandErsatztypen = bdLagerstandErsatztypen.add(
												getLagerFac().getLagerstandAllerLagerAllerMandanten(itArtikelId.next(),
														false, theClientDto));
									}

								}
							}
						}
						rows[row][col++] = bdLagerstandErsatztypen;
					}

					rows[row][col++] = fm.getN_lagerstand().subtract(fehlmenge.add(reservierungen));
					rows[row][col++] = bestellt;

				}

				if (fm.getFlrartikelliste().getEinheit_c_nr() != null
						&& fm.getFlrartikelliste().getEinheit_c_nr() != null) {
					rows[row][col++] = fm.getFlrartikelliste().getEinheit_c_nr().trim();
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = fm.getFlrlos().getT_produktionsbeginn();

				rows[row][col++] = getFertigungFac().getFruehesterEintrefftermin(fm.getArtikel_i_id(), theClientDto);

				FLRArtikelsperren as = (FLRArtikelsperren) o[2];

				if (as != null) {
					rows[row][col++] = as.getFlrsperren().getC_bez();
				} else {
					rows[row][col++] = null;
				}

				row++;
				col = 0;
			}

			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "SELECT count(*) from FLRVerfuegbarkeit flrverfuegbarkeit LEFT OUTER JOIN flrverfuegbarkeit.flrartikelliste.artikelsprset AS aspr"
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
					where.append(" " + FLR_VERFUEGBARKEIT + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(kriterien[i].kritName);
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
				orderBy.append(FLR_VERFUEGBARKEIT).append("flrartikelliste.c_nr").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_VERFUEGBARKEIT + "flrartikelliste.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_VERFUEGBARKEIT).append("flrartikelliste.c_nr").append(" ");
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
		return FLR_VERFUEGBARKEIT_FROM_CLAUSE;
	}

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
				String queryString = "select flrverfuegbarkeit.artikel_i_id from FLRVerfuegbarkeit flrverfuegbarkeit LEFT OUTER JOIN flrverfuegbarkeit.flrartikelliste.artikelsprset AS aspr"
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {
			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);

			columns.add("artikel_i_id", Integer.class, "artikel_i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					"artikel_i_id");

			columns.add("lp.artikel", String.class, getTextRespectUISpr("lp.artikel", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "flrverfuegbarkeit.flrartikelliste.c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM, "flrverfuegbarkeit.flrartikelliste.c_referenznr");
			}
			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_bez");
			columns.add("artikel.zusatzbez", String.class, getTextRespectUISpr("artikel.zusatzbez", mandant, locUi),
					QueryParameters.FLR_BREITE_L, "aspr.c_zbez");
			columns.add("lp.fehlmenge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.fehlmenge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);

			// PJ22237
			if (bLagerinfo) {
				columns.add("lp.lagerstand", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
						getTextRespectUISpr("lp.lagerstand", mandant, locUi), 3, Facade.NICHT_SORTIERBAR);
				if (bErsatztypen) {
					columns.add("lp.lagerstand.ersatztypen",
							super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
							getTextRespectUISpr("lp.lagerstand.ersatztypen", mandant, locUi), 3,
							Facade.NICHT_SORTIERBAR);
				}

				columns.add("lp.verfuegbar", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
						getTextRespectUISpr("lp.verfuegbar", mandant, locUi), QueryParameters.FLR_BREITE_M,
						Facade.NICHT_SORTIERBAR);
				columns.add("lp.bestellt", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
						getTextRespectUISpr("lp.bestellt", mandant, locUi), QueryParameters.FLR_BREITE_M,
						Facade.NICHT_SORTIERBAR);
			}

			columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, "flrverfuegbarkeit.flrartikelliste.einheit_c_nr");
			columns.add("lp.begintermin", java.util.Date.class, getTextRespectUISpr("lp.begintermin", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "flrverfuegbarkeit.flrlos.t_produktionsbeginn");
			columns.add("lp.eintrefftermin", java.util.Date.class,
					getTextRespectUISpr("lp.eintrefftermin", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("fert.sperre", SperrenIcon.class, getTextRespectUISpr("fert.sperre", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAGERINFO_IN_POSITIONEN);
			bLagerinfo = (Boolean) parameter.getCWertAsObject();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG,
					theClientDto)) {
				bErsatztypen = true;
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}
}
