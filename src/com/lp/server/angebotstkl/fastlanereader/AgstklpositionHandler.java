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
package com.lp.server.angebotstkl.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAgstklPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Angebotspositionsstuecklisten
 * implementiert. Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
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
public class AgstklpositionHandler extends UseCaseHandler {

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int iKalkulationsart = 0;

	/**
	 * The information needed for the kundes table.
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
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
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
				FLRAgstklposition agstklposition = (FLRAgstklposition) o[0];

				rows[row][col++] = agstklposition.getI_id();
				rows[row][col++] = agstklposition.getN_menge();

				String artikelart = null;

				if (agstklposition.getFlrartikelliste() != null) {
					if (agstklposition.getFlrartikelliste().getStuecklisten() != null) {
						Iterator iterator = agstklposition.getFlrartikelliste()
								.getStuecklisten().iterator();
						if (iterator.hasNext()) {
							FLRStueckliste stkl = (FLRStueckliste) iterator
									.next();
							artikelart = stkl.getStuecklisteart_c_nr();

						}
					}
				}
				rows[row][col++] = artikelart;
				rows[row][col++] = agstklposition.getEinheit_c_nr() == null ? ""
						: agstklposition.getEinheit_c_nr().trim();

				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;

				if (agstklposition.getAgstklpositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					// die sprachabhaengig Artikelbezeichnung anzeigen
					sBezeichnung = getArtikelFac()
							.formatArtikelbezeichnungEinzeiligOhneExc(
									agstklposition.getFlrartikel().getI_id(),
									theClientDto.getLocUi());
				}
				if (agstklposition.getAgstklpositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_AGSTUECKLISTE)) {
					sBezeichnung = agstklposition.getFlragstkl().getC_nr();

					if (agstklposition.getFlragstkl().getC_bez() != null
							&& agstklposition.getFlragstkl().getC_bez()
									.length() > 0) {
						sBezeichnung = agstklposition.getFlragstkl().getC_bez();
					}
				} else {
					// die restlichen Positionsarten
					if (agstklposition.getC_bez() != null) {
						sBezeichnung = agstklposition.getC_bez();
					}
				}

				rows[row][col++] = sBezeichnung;

				rows[row][col++] = agstklposition.getN_nettogesamtpreis();

				// PJ18253
				if (iKalkulationsart == 3) {
					if (agstklposition.getAgstklpositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_IDENT)) {

						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(
										agstklposition.getFlrartikel()
												.getI_id(),
										null,
										agstklposition.getN_menge(),
										agstklposition.getFlragstkl()
												.getWaehrung_c_nr(),
										new java.sql.Date(agstklposition
												.getFlragstkl()
												.getT_belegdatum().getTime()),
										theClientDto);
						if (alDto != null) {
							rows[row][col++] = alDto.getNNettopreis();
						} else {
							rows[row][col++] = null;
						}

						Integer wepIId = getLagerFac().getLetzteWEP_IID(
								agstklposition.getFlrartikel().getI_id());
						if (wepIId != null) {
							WareneingangspositionDto wepDto = getWareneingangFac()
									.wareneingangspositionFindByPrimaryKeyOhneExc(
											wepIId);

							if (wepDto != null) {

								WareneingangDto weDto = getWareneingangFac()
										.wareneingangFindByPrimaryKey(
												wepDto.getWareneingangIId());

								
								rows[row][col++] = wepDto
										.getNGelieferterpreis();
								rows[row][col++] = weDto
										.getTWareneingangsdatum();
							} else {
								rows[row][col++] = null;
								rows[row][col++] = null;
							}
						} else {
							rows[row][col++] = null;
							rows[row][col++] = null;
						}

					} else {
						rows[row][col++] = null;
						rows[row][col++] = null;
						rows[row][col++] = null;
					}

				}

				rows[row++][col++] = new Boolean(
						Helper.short2boolean(agstklposition.getB_drucken()));

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
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
			session = setFilter(session);
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
						where.append(" upper(agstklposition."
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" agstklposition."
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
							orderBy.append("agstklposition."
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
				orderBy.append("agstklposition.i_sort ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("agstklposition.i_sort") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" agstklposition.i_sort ");
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
		return "from FLRAgstklposition agstklposition LEFT OUTER JOIN agstklposition.flrartikelliste.stuecklisten AS stuecklisten ";
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
				session = setFilter(session);
				String queryString = "select agstklposition.i_id from FLRAgstklposition agstklposition  LEFT OUTER JOIN agstklposition.flrartikelliste.stuecklisten AS stuecklisten  "
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

	public Session setFilter(Session session) {
		session = super.setFilter(session);
		session.enableFilter("filterMandant").setParameter("paramMandant",
				theClientDto.getMandant());
		return session;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			try {
				ParametermandantDto parameterMand = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
								ParameterFac.PARAMETER_KALKULATIONSART);
				iKalkulationsart = (Integer) parameterMand.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (iKalkulationsart == 3) {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, BigDecimal.class,
								String.class, String.class, String.class,
								BigDecimal.class, BigDecimal.class,
								BigDecimal.class, java.util.Date.class,
								Boolean.class },
						new String[] {
								"i_id",
								getTextRespectUISpr("lp.menge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.stuecklistenart",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einheit",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.preis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("artikel.label.lief1Preis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.gelpreis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.wedatum",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_M, // Format 1234.123
								QueryParameters.FLR_BREITE_M, // Format 1234.123
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								QueryParameters.FLR_BREITE_M, // Format
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_S // TRUE/FALSE
						},
						new String[] {
								"i_id",
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_N_MENGE,
								Facade.NICHT_SORTIERBAR,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_EINHEIT_C_NR,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_C_BEZ,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_N_NETTOGESAMTPREIS,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_B_DRUCKEN }));
			} else {
				setTableInfo(new TableInfo(
						new Class[] { Integer.class, BigDecimal.class,
								String.class, String.class, String.class,
								BigDecimal.class, Boolean.class },
						new String[] {
								"i_id",
								getTextRespectUISpr("lp.menge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.stuecklistenart",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einheit",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.preis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_M, // Format 1234.123
								QueryParameters.FLR_BREITE_M, // Format 1234.123
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								QueryParameters.FLR_BREITE_M, // Format
																// 1234567.12
								QueryParameters.FLR_BREITE_S // TRUE/FALSE
						},
						new String[] {
								"i_id",
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_N_MENGE,
								Facade.NICHT_SORTIERBAR,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_EINHEIT_C_NR,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_C_BEZ,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_N_NETTOGESAMTPREIS,
								AngebotstklpositionFac.FLR_AGSTKLPOSITION_B_DRUCKEN }));
			}

		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AgstklDto agstklDto = null;
		AgstklpositionDto agstklpositionDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			agstklpositionDto = getAngebotstklpositionFac()
					.agstklpositionFindByPrimaryKey((Integer) key, theClientDto);
			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(
					agstklpositionDto.getBelegIId());
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					agstklDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (agstklDto != null && agstklpositionDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_AGSTUECKLISTE.trim() + "/"
			// + LocaleFac.BELEGART_AGSTUECKLISTE.trim() + "/"
			// + agstklDto.getCNr().replace("/", ".") + "/"
			// + "Angebotsstuecklistenpositionen/" + "Position "
			// + agstklpositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeAgstklPosition(
					agstklpositionDto, agstklDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "AGSTUECKLISTENPOSITION";
	}
}
