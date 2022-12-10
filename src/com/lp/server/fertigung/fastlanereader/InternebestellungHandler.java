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
package com.lp.server.fertigung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRInternebestellung;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
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
 * Hier wird die FLR Funktionalitaet fuer die Interne Bestellung implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 22.11.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class InternebestellungHandler extends UseCaseHandler {

	boolean bStuecklistenfreigabe = false;
	boolean bLagerminJeLager = false;
	boolean bReferenznummer = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_INTBEST = "flrinternebestellung.";

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
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;

			HashMap<Integer,BigDecimal> hmRahmenreservierungCache=new HashMap<Integer,BigDecimal>();
			
			String[] tooltipData = new String[resultList.size()];

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRInternebestellung intbest = (FLRInternebestellung) o[0];

				rows[row][getTableColumnInformation().getViewIndex("i_id")] = intbest.getI_id();

				if (o[1] != null) {
					rows[row][getTableColumnInformation().getViewIndex("S")] = o[1];
				}

				rows[row][getTableColumnInformation().getViewIndex("lp.artikelnummer")] = intbest.getFlrstueckliste()
						.getFlrartikel().getC_nr();
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						intbest.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

				if (artikelDto.getArtikelsprDto() != null) {
					rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelDto
							.getArtikelsprDto().getCBez();
				}

				if (bReferenznummer) {
					rows[row][getTableColumnInformation().getViewIndex("lp.referenznummer")] = artikelDto
							.getCReferenznr();
				}

				BigDecimal bdRahmenRes=null;
				if(hmRahmenreservierungCache.containsKey(artikelDto.getIId())) {
					bdRahmenRes=hmRahmenreservierungCache.get(artikelDto.getIId());
				}else {
					bdRahmenRes= getReservierungFac()
						.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);
					hmRahmenreservierungCache.put(artikelDto.getIId(), bdRahmenRes);
					
				}
				
				rows[row][getTableColumnInformation().getViewIndex("lp.offenimrahmen")] = bdRahmenRes;

				rows[row][getTableColumnInformation().getViewIndex("lp.menge")] = intbest.getN_menge();
				rows[row][getTableColumnInformation().getViewIndex("lp.einh")] = artikelDto.getEinheitCNr().trim();
				rows[row][getTableColumnInformation().getViewIndex("lp.liefertermin")] = intbest.getT_liefertermin();

				rows[row][getTableColumnInformation().getViewIndex("bes.belegart")] = intbest.getBelegart_c_nr();

				if (intbest.getI_belegiid() != null) {
					// Los
					if (intbest.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(intbest.getI_belegiid());
						if (losDto != null) {
							rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = losDto.getCNr();
						}
					} else if (intbest.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(intbest.getI_belegiid());
						if (auftragDto != null) {
							rows[row][getTableColumnInformation().getViewIndex(
									"bes.belegartnummer")] = auftragDto.getCNr() == null ? null : auftragDto.getCNr();
						}
					} else if (intbest.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
						ForecastDto fDto = getForecastFac().forecastFindByPrimaryKeyOhneExc(intbest.getI_belegiid());
						if (fDto != null) {
							rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = fDto.getCNr();
						}
					}
				}

				if (bStuecklistenfreigabe == true && intbest.getFlrstueckliste() != null
						&& intbest.getFlrstueckliste().getT_freigabe() != null) {
					rows[row][getTableColumnInformation().getViewIndex("stk.freigabe")] = FertigungFac.STATUS_ERLEDIGT;
				}

				if (bLagerminJeLager) {
					if (intbest.getFlrpartner_standort() != null) {
						rows[row][getTableColumnInformation().getViewIndex("system.standort")] = intbest
								.getFlrpartner_standort().getC_kbez();
					}
				}

				if (intbest.getAuftrag_i_id_kopfauftrag() != null) {
					rows[row][getTableColumnInformation().getViewIndex("fert.internebestellung.kopfauftrag")] = intbest
							.getFlrauftrag_kopfauftrag().getC_nr();
				}

				
				rows[row][getTableColumnInformation().getViewIndex("ww.lagermindest")] = intbest
						.getF_lagermindest();
				
				
				
				Calendar c = Calendar.getInstance();
				c.setTime(intbest.getT_liefertermin());
				c.add(Calendar.DATE, 1);

				BigDecimal db = getFertigungFac().getAnzahlInFertigung(
						intbest.getFlrstueckliste().getFlrartikel().getI_id(),
						Helper.cutDate(new java.sql.Date(c.getTimeInMillis())), theClientDto);

				if (db != null && db.doubleValue() > 0) {
					rows[row][getTableColumnInformation().getViewIndex("Color")] = new Color(34, 139, 34);
				}

				
				if (intbest.getX_ausloeser() != null) {
					
					
					String text = intbest.getX_ausloeser();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				
				

				row++;

			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
			String queryString = "select count(*) from FLRInternebestellung flrinternebestellung "
					+ this.buildWhereClause();
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

					if (filterKriterien[i].isBIgnoreCase()) {

						where.append(" lower(" + FLR_INTBEST + filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + FLR_INTBEST + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toLowerCase());

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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_INTBEST + kriterien[i].kritName);
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
				orderBy.append(FLR_INTBEST)
						.append(FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE + "."
								+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr")
						.append(" ASC, ").append(FLR_INTBEST + "t_liefertermin ASC, ").append(FLR_INTBEST + "i_id ASC");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_INTBEST + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_INTBEST + "i_id").append(" ");
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
		return "SELECT flrinternebestellung,  (SELECT max(s.flrsperren.c_bez) FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrinternebestellung.flrstueckliste.artikel_i_id AND ( s.flrsperren.b_gesperrtstueckliste=1 OR s.flrsperren.b_gesperrt=1)) as sperren from FLRInternebestellung flrinternebestellung ";
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
				String queryString = "select " + FLR_INTBEST + FertigungFac.FLR_LOSSOLLMATERIAL_I_ID
						+ " from FLRInternebestellung flrinternebestellung " + this.buildWhereClause()
						+ this.buildOrderByClause();
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("S", SperrenIcon.class, getTextRespectUISpr("bes.sperre", mandant, locUi), QueryParameters.FLR_BREITE_S,
				Facade.NICHT_SORTIERBAR, getTextRespectUISpr("bes.sperre.tooltip", mandant, locUi));

		columns.add("lp.artikelnummer", String.class, getTextRespectUISpr("lp.artikelnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_IDENT, FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE + "."
						+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr");
		columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi), 60,
				Facade.NICHT_SORTIERBAR);
		if (bReferenznummer) {
			columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
					QueryParameters.FLR_BREITE_IDENT, FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE + "."
							+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_referenznr");
		}
		columns.add("lp.offenimrahmen", BigDecimal.class, getTextRespectUISpr("lp.offenimrahmen", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS, Facade.NICHT_SORTIERBAR);
		columns.add("lp.menge", BigDecimal.class, getTextRespectUISpr("lp.menge", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS, FertigungFac.FLR_INTERNE_BESTELLUNG_N_MENGE);

		columns.add("lp.einh", String.class, getTextRespectUISpr("lp.einh", mandant, locUi), 5,
				FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE + "." + StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL
						+ "." + ArtikelFac.FLR_ARTIKEL_EINHEIT_C_NR);

		columns.add("lp.liefertermin", Date.class, getTextRespectUISpr("lp.liefertermin", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FertigungFac.FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN);

		columns.add("bes.belegart", String.class, getTextRespectUISpr("bes.belegart", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, FertigungFac.FLR_INTERNE_BESTELLUNG_BELEGART_C_NR);

		columns.add("bes.belegartnummer", String.class, getTextRespectUISpr("bes.belegartnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FertigungFac.FLR_INTERNE_BESTELLUNG_I_BELEGIID);

		if (bStuecklistenfreigabe) {
			columns.add("stk.freigabe", Icon.class, getTextRespectUISpr("stk.freigabe", mandant, locUi), 1,
					FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE + ".t_freigabe");
		}

		if (bLagerminJeLager) {
			columns.add("system.standort", String.class, getTextRespectUISpr("system.standort", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "flrpartner_standort.c_kbez");
		}

		columns.add("fert.internebestellung.kopfauftrag", String.class,
				getTextRespectUISpr("fert.internebestellung.kopfauftrag", mandant, locUi), QueryParameters.FLR_BREITE_M,
				"auftrag_i_id_kopfauftrag");

		
		columns.add("ww.lagermindest", Double.class, getTextRespectUISpr("ww.lagermindest", mandant, locUi),
				QueryParameters.FLR_BREITE_MENGE, "f_lagermindest");
		
		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	public TableInfo getTableInfo() {

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE,
				theClientDto)) {
			bStuecklistenfreigabe = true;
		}

		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
			bLagerminJeLager = (Boolean) param.getCWertAsObject();

			param = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_REFERENZNUMMER_IN_INTERNER_BESTELLUNG);
			bReferenznummer = (Boolean) param.getCWertAsObject();
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames());
		setTableInfo(info);

		return info;

	}
}
