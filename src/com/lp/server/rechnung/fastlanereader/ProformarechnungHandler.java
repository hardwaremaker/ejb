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
import java.rmi.RemoteException;
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

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeGutschrift;
import com.lp.server.system.jcr.service.docnode.DocNodeProformarechnung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.HelperServer;
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
 * Hier wird die FLR Funktionalitaet fuer die Proformarechnungen implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 12.10.2004
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class ProformarechnungHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bBruttoStattNetto = false;
	Integer iAnlegerStattVertreterAnzeigen = 0;

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
			int col = 0;
			HashMap<?, ?> hmStatus = getSystemMultilanguageFac()
					.getAllStatiMitUebersetzung(theClientDto.getLocUi(),
							theClientDto);

			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			while (resultListIterator.hasNext()) {
				FLRRechnung proformarechnung = (FLRRechnung) resultListIterator
						.next();
				rows[row][col++] = proformarechnung.getI_id();
				rows[row][col++] = proformarechnung.getFlrrechnungart() == null ? null
						: proformarechnung.getFlrrechnungart().getC_nr()
								.substring(0, 1);
				rows[row][col++] = proformarechnung.getC_nr();
				rows[row][col++] = proformarechnung.getFlrkunde() == null ? null
						: proformarechnung.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
				
				
				if (proformarechnung.getFlrkunde()!=null && proformarechnung.getFlrkunde().getFlrpartner()
						.getFlrlandplzort() != null) {
					
					FLRLandplzort anschrift =proformarechnung.getFlrkunde().getFlrpartner()
					.getFlrlandplzort();
					
					rows[row][col++] = anschrift.getFlrland().getC_lkz() + "-"
							+ anschrift.getC_plz() + " "
							+ anschrift.getFlrort().getC_name();
				} else {
					rows[row][col++] = "";
				}
				
				String proj_bestellnummer = "";
				if (proformarechnung.getC_bez() != null) {
					proj_bestellnummer = proformarechnung.getC_bez();
				}

				if (proformarechnung.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + proformarechnung.getC_bestellnummer();
				}

				rows[row][col++] = proj_bestellnummer;
				
				rows[row][col++] = proformarechnung.getD_belegdatum();
				
				
				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (proformarechnung.getFlrpersonalanleger() != null) {
						rows[row][col++] = proformarechnung.getFlrpersonalanleger()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (proformarechnung.getFlrpersonalaenderer() != null) {
						rows[row][col++] = proformarechnung.getFlrpersonalaenderer()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (proformarechnung.getFlrvertreter() != null) {
						rows[row][col++] = proformarechnung.getFlrvertreter()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}
				
				rows[row][col++] = getStatusMitUebersetzung(proformarechnung.getStatus_c_nr(),
						proformarechnung.getT_versandzeitpunkt(),
						proformarechnung.getC_versandtype());
				

				if (bDarfPreiseSehen) {
					if (bBruttoStattNetto == false) {
						rows[row][col++] = proformarechnung.getN_wertfw();
					} else {

						if (proformarechnung.getN_wertfw() != null
								&& proformarechnung.getN_wertustfw() != null) {
							rows[row][col++] = proformarechnung.getN_wertfw()
									.add(proformarechnung.getN_wertustfw());
						} else {
							rows[row][col++] = null;
						}

					}

				} else {
					rows[row][col++] = null;
				}

				rows[row++][col++] = proformarechnung.getWaehrung_c_nr();
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
					if (filterKriterien[i].kritName
							.equals(RechnungFac.FLR_RECHNUNG_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							if (!istBelegnummernInJahr(
									"FLRRechnung",
									sValue,
									"flrrechnungart.rechnungtyp_c_nr='"
											+ RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG
											+ "'")) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" proformarechnung."
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(proformarechnung."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" proformarechnung."
									+ filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
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
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append("proformarechnung."
								+ kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("proformarechnung."
						+ RechnungFac.FLR_RECHNUNG_I_GESCHAEFTSJAHR + " DESC,"
						+ "proformarechnung." + RechnungFac.FLR_RECHNUNG_C_NR
						+ " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("proformarechnung."
					+ RechnungFac.FLR_RECHNUNG_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" proformarechnung."
						+ RechnungFac.FLR_RECHNUNG_C_NR + " ");
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
		return "from FLRRechnung proformarechnung ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
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
				String queryString = "select proformarechnung."
						+ RechnungFac.FLR_RECHNUNG_I_ID
						+ " from FLRRechnung proformarechnung "
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
	 * gets information about the proformarechnungstable.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_RECHNUNG,
								ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE);
				bBruttoStattNetto = (Boolean) parameter.getCWertAsObject();

				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
				iAnlegerStattVertreterAnzeigen = (Integer) parameter
						.getCWertAsObject();
				
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			String orderVertreter = "flrvertreter.c_kurzzeichen";
			if (iAnlegerStattVertreterAnzeigen == 1) {
				orderVertreter = "flrpersonalanleger.c_kurzzeichen";
			} else if (iAnlegerStattVertreterAnzeigen == 2) {
				orderVertreter = "flrpersonalaenderer.c_kurzzeichen";

			}
			
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class, String.class, String.class,
							String.class, Date.class,String.class, Icon.class,
							BigDecimal.class, String.class },
					new String[] {
							"Id",
							" ",
							getTextRespectUISpr("lp.rechnr", mandantCNr, locUI),
							getTextRespectUISpr("lp.kunde", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI),
							getTextRespectUISpr("re.projektbestellnummer", mandantCNr, locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.vertreter", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							bBruttoStattNetto ? getTextRespectUISpr(
									"lp.bruttobetrag", mandantCNr, locUI)
									: getTextRespectUISpr("lp.nettobetrag",
											mandantCNr, locUI),
							getTextRespectUISpr("lp.whg", mandantCNr, locUI) },
					new int[] { -1, 1, QueryParameters.FLR_BREITE_M, -1,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG },
					new String[] {
							RechnungFac.FLR_RECHNUNG_I_ID,
							RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
									+ RechnungFac.FLR_RECHNUNGART_C_NR,
							RechnungFac.FLR_RECHNUNG_C_NR,
							RechnungFac.FLR_RECHNUNG_FLRKUNDE
									+ "."
									+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
									RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."
											+ KundeFac.FLR_PARTNER + "."
											+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
											+ SystemFac.FLR_LP_FLRLAND
											+ "."
											+ SystemFac.FLR_LP_LANDLKZ
											+ ", "
											+
											// und dann nach plz
											"proformarechnung." + RechnungFac.FLR_RECHNUNG_FLRKUNDE
											+ "." + KundeFac.FLR_PARTNER + "."
											+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
											+ SystemFac.FLR_LP_LANDPLZORTPLZ,
											RechnungFac.FLR_RECHNUNG_C_BEZ,
							RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
							orderVertreter,
							RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
							RechnungFac.FLR_RECHNUNG_N_WERTFW,
							RechnungFac.FLR_RECHNUNG_WAEHRUNG_C_NR }));

		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
					(Integer) key);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (rechnungDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_RECHNUNG.trim() + "/"
			// + LocaleFac.BELEGART_GUTSCHRIFT.trim() + "/"
			// + rechnungDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeProformarechnung(
					rechnungDto));
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
		return "RECHNUNG";
	};
}
