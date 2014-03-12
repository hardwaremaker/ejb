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
package com.lp.server.bestellung.fastlanereader;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBestPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
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
 * <I>FLR-Handler fuer Bestellpositionen</I>
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
 * @author GH
 * @version 1.0
 */
public class Bestellpositionhandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean bPreisueberwachung = false;

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
	 * 
	 * @param rowIndex
	 *            the index of the row that should be contained in the page.
	 * @return the data page for the specified row.
	 * @throws EJBExceptionLP
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = BestellungHandler.PAGE_SIZE;
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
			String[] tooltipData = new String[resultList.size()];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);

			int iNachkommastellenMenge = getMandantFac()
					.getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisEK(mandantCNr);

			BestellungDto bestDto = null;

			while (resultListIterator.hasNext()) {
				FLRBestellposition position = (FLRBestellposition) resultListIterator
						.next();

				rows[row][col++] = position.getI_id();
				rows[row][col++] = getBestellpositionFac()
						.getPositionNummerReadOnly(position.getI_id());
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
						position.getN_menge(), iNachkommastellenMenge);
				if (position.getEinheit_c_nr() != null) {
					rows[row][col++] = position.getEinheit_c_nr().trim();
				} else {
					rows[row][col++] = position.getEinheit_c_nr();
				}
				String sIdentnummer = "";
				if (position.getFlrartikel() != null) {
					sIdentnummer = position.getFlrartikel().getC_nr();
				}
				rows[row][col++] = sIdentnummer;
				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;

				if (position.getBestellpositionart_c_nr().equals(
						LocaleFac.POSITIONSART_TEXTEINGABE)) {
					if (position.getC_textinhalt() != null
							&& position.getC_textinhalt().length() > 0) {
						sBezeichnung = Helper.strippHTML(position
								.getC_textinhalt());
					}
				} else if (position.getBestellpositionart_c_nr().equals(
						LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
					sBezeichnung = "["
							+ getTextRespectUISpr("lp.seitenumbruch",
									mandantCNr, locUI) + "]";
				} else if (position.getBestellpositionart_c_nr().equals(
						LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
					sBezeichnung = position.getFlrmediastandard().getC_nr();
				} else if (position.getBestellpositionart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					// die sprachabhaengig Artikelbezeichnung anzeigen
					sBezeichnung = getArtikelFac()
							.formatArtikelbezeichnungEinzeiligOhneExc(
									position.getFlrartikel().getI_id(),
									theClientDto.getLocUi());
				} else {
					// die uebersteuerte bezeichnung
					if (position.getC_bezeichnung() != null) {
						sBezeichnung = position.getC_bezeichnung();
					}
				}
				rows[row][col++] = sBezeichnung;
				boolean bPreisOK = true;
				if (bDarfPreiseSehen) {
					if (bPreisueberwachung && position.getFlrartikel() != null) {

						if (bestDto == null) {
							bestDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											position.getFlrbestellung()
													.getI_id());
						}

						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(
										position.getFlrartikel().getI_id(),
										bestDto.getLieferantIIdBestelladresse(),
										position.getN_menge(),
										bestDto.getWaehrungCNr(),
										bestDto.getDBelegdatum(), theClientDto);

						if (alDto != null && alDto.getNNettopreis() != null) {

							if (!Helper.rundeKaufmaennisch(
									alDto.getNNettopreis(),
									iNachkommastellenPreis).equals(
									Helper.rundeKaufmaennisch(
											position.getN_nettogesamtpreis(),
											iNachkommastellenPreis))) {
								bPreisOK = false;
							}

						}
					}

					if(position.getN_nettogesamtpreis() == null || position.getN_menge() == null) {
						rows[row][col++] = null;						
						rows[row][col++] = null;						
					} else {
						rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
								position.getN_nettogesamtpreis(),
								iNachkommastellenPreis);						
						rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
								position.getN_nettogesamtpreis().multiply(
										position.getN_menge()),
								iNachkommastellenPreis);
					}
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				rows[row][col++] = position.getBestellpositionstatus_c_nr();
				// Text
				if (position.getC_textinhalt() != null
						&& !position.getC_textinhalt().equals("")) {
					rows[row][col++] = new Boolean(true);
					String text = position.getC_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rows[row][col++] = new Boolean(false);
				}
				
				if(bPreisOK==false){
					rows[row][col++] = Color.RED;
				}
				
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);
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
			String queryString = "select count(*) " + this.getFromClause()
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
					where.append(" position." + filterKriterien[i].kritName);
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
				orderBy.append("position.")
						.append(BestellpositionFac.FLR_BESTELLPOSITION_I_SORT)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("position."
					+ BestellpositionFac.FLR_BESTELLPOSITION_I_SORT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" position.")
						.append(BestellpositionFac.FLR_BESTELLPOSITION_I_SORT)
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
		return "from FLRBestellposition position ";
	}

	/**
	 * gets information such as column names an column types used for the table
	 * on the client side.
	 * 
	 * @return the information needed to create the client side table.
	 */
	public TableInfo getTableInfo() {
		try {
			if (super.getTableInfo() == null) {

				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_PREISUEBERWACHUNG);
				bPreisueberwachung = (Boolean) parameter.getCWertAsObject();

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
								String.class, Boolean.class, Color.class },
						new String[] {
								"i_id",
								getTextRespectUISpr("lp.nr",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.menge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.einheit",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.ident",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.bezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.preis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.zeilensumme",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.status",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.text",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_S,
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.MENGE,
										iNachkommastellenMenge),// Menge
								QueryParameters.FLR_BREITE_XS, // z.B. Woche
								getUIBreiteIdent(), //ident
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), // Preis
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis),
								QueryParameters.FLR_BREITE_M, 3, 0 },

						new String[] {
								"i_id",
								Facade.NICHT_SORTIERBAR,
								BestellpositionFac.FLR_BESTELLPOSITION_N_MENGE,
								BestellpositionFac.FLR_BESTELLPOSITION_EINHEIT_C_NR,
								Facade.NICHT_SORTIERBAR,
								BestellpositionFac.FLR_BESTELLPOSITION_C_BEZEICHNUNG,
								BestellpositionFac.FLR_BESTELLPOSITION_N_NETTOGESAMTPREIS,
								Facade.NICHT_SORTIERBAR,
								"bestellpositionstatus_c_nr",
								Facade.NICHT_SORTIERBAR, "" }));

			}
			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * sorts the data of the current query using the specified criterias and
	 * returns the page of data where the row of selectedId is contained.
	 * 
	 * @param sortierKriterien
	 *            the new sort criterias.
	 * @param selectedId
	 *            the id of the entity that should be included in the result
	 *            page.
	 * @return the sorted data containing the page where the entity with the
	 *         specified id is located.
	 * @throws EJBExceptionLP
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
				String queryString = "select position." + "i_id"
						+ " from FLRBestellposition position "
						+ this.buildWhereClause() + this.buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				boolean idFound = false;
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
		BestellungDto bestDto = null;
		BestellpositionDto bestPosDto = null;
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {
			bestPosDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey((Integer) key);
			bestDto = getBestellungFac().bestellungFindByPrimaryKey(
					bestPosDto.getBestellungIId());
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
					bestDto.getLieferantIIdBestelladresse(), theClientDto);
			if (lieferantDto != null) {
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(
						lieferantDto.getPartnerIId(), theClientDto);
			}
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (bestDto != null && bestPosDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_BESTELLUNG.trim() + "/"
			// + bestDto.getBelegartCNr().trim() + "/"
			// + bestDto.getCNr().replace("/", ".") + "/"
			// + "Bestellpositionen/" + "Position " + bestPosDto.getIId();
			DocPath docPath = new DocPath(new DocNodeBestPosition(bestPosDto,
					bestDto));
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
		return "BESTELLPOSITION";
	}
}
