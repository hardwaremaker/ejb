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
package com.lp.server.anfrage.fastlanereader;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAnfPosition;
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
 * Hier wird die FLR Funktionalitaet fuer die Anfrageposition implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 14.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author martin werner, uli walch
 * @version 1.0
 */

public class AnfragepositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANFRAGEPOSITION = "flranfrageposition.";
	public static final String FLR_ANFRAGEPOSITION_FROM_CLAUSE = " from FLRAnfrageposition flranfrageposition ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AnfrageHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL= " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			int row = 0;
			int col = 0;

			int iNachkommastellenMenge = getMandantFac()
					.getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisEK(mandantCNr);

			while (resultListIterator.hasNext()) {
				FLRAnfrageposition position = (FLRAnfrageposition) resultListIterator
						.next();
				rows[row][col++] = position.getI_id();
				rows[row][col++] = getAnfragepositionFac().getPositionNummer(
						position.getI_id(), theClientDto);
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
						position.getN_menge(), iNachkommastellenMenge);
				rows[row][col++] = position.getEinheit_c_nr() == null ? ""
						: position.getEinheit_c_nr().trim();

				String sIdentnummer = "";
				if (position.getFlrartikel() != null) {
					sIdentnummer = position.getFlrartikel().getC_nr();
				}
				rows[row][col++] = sIdentnummer;

				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;

				if (position.getAnfragepositionart_c_nr().equals(
						LocaleFac.POSITIONSART_TEXTEINGABE)) {
					if (position.getX_textinhalt() != null
							&& position.getX_textinhalt().length() > 0) {
						sBezeichnung = Helper.strippHTML(position
								.getX_textinhalt());
					}
				} else if (position.getAnfragepositionart_c_nr().equals(
						LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
					sBezeichnung = "["
							+ getTextRespectUISpr("lp.seitenumbruch",
									mandantCNr, locUI) + "]";
				} else if (position.getAnfragepositionart_c_nr().equals(
						LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
					sBezeichnung = position.getFlrmediastandard().getC_nr();
				} else if (position.getAnfragepositionart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					// die sprachabhaengig Artikelbezeichnung anzeigen
					sBezeichnung = getArtikelFac()
							.formatArtikelbezeichnungEinzeiligOhneExc(
									position.getFlrartikel().getI_id(),
									theClientDto.getLocUi());
				} else {
					// die restlichen Positionsarten
					if (position.getC_bez() != null) {
						sBezeichnung = position.getC_bez();
					}
				}

				rows[row][col++] = sBezeichnung;

				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
						position.getN_richtpreis(), iNachkommastellenPreis);
				if (position.getN_richtpreis() == null
						|| position.getN_menge() == null) {
					rows[row][col++] = null;
				} else {
					rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
							position.getN_richtpreis().multiply(
									position.getN_menge()),
							iNachkommastellenPreis);
				}
				// Text
				if (position.getX_textinhalt() != null
						&& !position.getX_textinhalt().equals("")) {
					rows[row][col++] = new Boolean(true);
					String text = position.getX_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rows[row][col++] = new Boolean(false);
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);
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
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
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
					where.append(" " + FLR_ANFRAGEPOSITION
							+ filterKriterien[i].kritName);
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
							orderBy.append(FLR_ANFRAGEPOSITION
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
				orderBy.append(FLR_ANFRAGEPOSITION)
						// poseinfuegen: 0 default Sortierung nach iSort
						.append(AnfragepositionFac.FLR_ANFRAGEPOSITION_I_SORT)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANFRAGEPOSITION + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANFRAGEPOSITION).append("i_id")
						.append(" ");
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
		return FLR_ANFRAGEPOSITION_FROM_CLAUSE;
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
				String queryString = "select " + FLR_ANFRAGEPOSITION + "i_id"
						+ FLR_ANFRAGEPOSITION_FROM_CLAUSE
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

	public TableInfo getTableInfo() {
		try {
			if (super.getTableInfo() == null) {
				String mandantCNr = theClientDto.getMandant();

				int iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(mandantCNr);
				int iNachkommastellenPreis = getMandantFac()
						.getNachkommastellenPreisEK(mandantCNr);

				setTableInfo(new TableInfo(
						new Class[] {
								Integer.class,
								Integer.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								Boolean.class },
						new String[] {
								"i_id",
								getTextRespectUISpr("lp.nr",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.menge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einheit",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.ident",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("anf.richtpreis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.zeilensumme",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.text",
										theClientDto.getMandant(),
										theClientDto.getLocUi()) },

						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_S,
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.MENGE,
										iNachkommastellenMenge), // Menge
								QueryParameters.FLR_BREITE_XS,
								getUIBreiteIdent(), //ident
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), // Preis
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), 3 },
						new String[] {
								"i_id",
								Facade.NICHT_SORTIERBAR,
								AnfragepositionFac.FLR_ANFRAGEPOSITION_N_MENGE,
								AnfragepositionFac.FLR_ANFRAGEPOSITION_EINHEIT_C_NR,
								Facade.NICHT_SORTIERBAR,
								AnfragepositionFac.FLR_ANFRAGEPOSITION_C_BEZ,
								AnfragepositionFac.FLR_ANFRAGEPOSITION_N_RICHTPREIS,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR }));
			}

			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AnfrageDto anfrageDto = null;
		AnfragepositionDto anfragepositionDto = null;
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {
			anfragepositionDto = getAnfragepositionFac()
					.anfragepositionFindByPrimaryKey((Integer) key,
							theClientDto);
			anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(
					anfragepositionDto.getBelegIId(), theClientDto);
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
					anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					lieferantDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (anfragepositionDto != null && anfrageDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_ANFRAGE.trim() + "/"
//					+ LocaleFac.BELEGART_ANFRAGE.trim() + "/"
//					+ anfrageDto.getCNr().replace("/", ".") + "/"
//					+ "Anfragepositionen/" + "Position "
//					+ anfragepositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeAnfPosition(anfragepositionDto, anfrageDto));
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
		return "ANFRAGEPOSITION";
	}
}
