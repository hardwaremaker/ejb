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
package com.lp.server.angebot.fastlanereader;

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

import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.auftrag.fastlanereader.FLRAuftragPositionNumberAdapter;
import com.lp.server.auftrag.fastlanereader.generated.FLRPositionenSichtAuftrag;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAgPosition;
import com.lp.server.system.jcr.service.docnode.DocNodeAngebot;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberAdapter;
import com.lp.server.util.PositionNumberHandlerPaged;
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
 * FLR fuer ANGB_ANGEBOTPOSITION.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 14.07.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AngebotpositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANGEBOTPOSITION = "flrangebotposition.";
	public static final String FLR_ANGEBOTPOSITION_FROM_CLAUSE = " from FLRAngebotposition flrangebotposition ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AngebotHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);

			List<FLRAngebotposition> resultFLrs = query.list() ; // ja, ich will alle(!) Datensaetze haben
			PositionNumberAdapter adapter = new FLRAngebotpositionNumberAdapter(
					(List<FLRAngebotposition>) resultFLrs) ;
			PositionNumberHandlerPaged numberHandler = new PositionNumberHandlerPaged(adapter) ;
			
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
					.getNachkommastellenPreisVK(mandantCNr);
			
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);


			while (resultListIterator.hasNext()) {
				FLRAngebotposition position = (FLRAngebotposition) resultListIterator
						.next();
				rows[row][col++] = position.getI_id();
//				rows[row][col++] = getAngebotpositionFac().getPositionNummer(position.getI_id());
				rows[row][col++] = numberHandler.getPositionNummer(position.getI_id(), adapter);
				String cAlternative = null;

				if (Helper.short2boolean(position.getB_alternative())) {
					cAlternative = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_SHORT;
				}

				rows[row][col++] = cAlternative;
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

				if (position.getTyp_c_nr() == null) {
					if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTEINGABE)) {
						if (position.getX_textinhalt() != null
								&& position.getX_textinhalt().length() > 0) {
							sBezeichnung = Helper.strippHTML(position
									.getX_textinhalt());
						}
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.seitenumbruch",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_ENDSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.endsumme",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_POSITION)) {
						sBezeichnung = getTextRespectUISpr("lp.position",
								mandantCNr, locUI)
								+ " " + position.getC_bez();
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
						sBezeichnung = position.getFlrmediastandard().getC_nr();
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac()
								.formatArtikelbezeichnungEinzeiligOhneExc(
										position.getFlrartikel().getI_id(),
										theClientDto.getLocUi());
					} else if (position.getPositionart_c_nr()
							.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("angebot.intelligentezwischensumme",
										mandantCNr, locUI) + "] " 
								+ position.getC_bez() ;
					} else {
						// die restlichen Positionsarten
						if (position.getC_bez() != null) {
							sBezeichnung = position.getC_bez();
						}
					}
				} else {
					if (position.getPosition_i_id() == null) {
						if (position.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							sBezeichnung = getTextRespectUISpr("lp.position",
									mandantCNr, locUI);
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						}
					} else {
						if (position.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							if (position.getTyp_c_nr().equals(
									LocaleFac.POSITIONTYP_EBENE1)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI);
							} else {
								sBezeichnung = "  "
										+ getTextRespectUISpr("lp.position",
												mandantCNr, locUI);
							}
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						} else {
							if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTEINGABE)) {
								if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									if (position.getX_textinhalt() != null
											&& position.getX_textinhalt()
													.length() > 0) {
										sBezeichnung = " "
												+ Helper.strippHTML(position
														.getX_textinhalt());
									}
								}
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
								sBezeichnung = "["
										+ getTextRespectUISpr(
												"lp.seitenumbruch", mandantCNr,
												locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_ENDSUMME)) {
								sBezeichnung = "["
										+ getTextRespectUISpr("lp.endsumme",
												mandantCNr, locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_POSITION)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI)
										+ " " + position.getC_bez();
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
								sBezeichnung = position.getFlrmediastandard()
										.getC_nr();
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_IDENT)) {
								if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "  "
											+ getArtikelFac()
													.formatArtikelbezeichnungEinzeiligOhneExc(
															position
																	.getFlrartikel()
																	.getI_id(),
																	theClientDto
																	.getLocUi());
								} else if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE2)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "    "
											+ getArtikelFac()
													.formatArtikelbezeichnungEinzeiligOhneExc(
															position
																	.getFlrartikel()
																	.getI_id(),
																	theClientDto
																	.getLocUi());
								}
							} else {
								if (position.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (position.getTyp_c_nr().equals(
											LocaleFac.POSITIONTYP_EBENE1)) {
										if (position.getC_bez() != null) {
											sBezeichnung = " "
													+ position.getC_bez();
										}
									} else if (position.getTyp_c_nr().equals(
											LocaleFac.POSITIONTYP_EBENE2)) {
										if (position.getC_bez() != null) {
											sBezeichnung = "  "
													+ position.getC_bez();
										}
									}
								}
							}
						}
					}
				}

				rows[row][col++] = sBezeichnung;
				if (bDarfPreiseSehen) {			
				BigDecimal bdNettogesamtpreis = null;
				BigDecimal bdZeilensumme = null;
				if (position.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {

					bdNettogesamtpreis = getUIObjectBigDecimalNachkommastellen(
							getAngebotpositionFac().getGesamtpreisPosition(
									position.getI_id(), theClientDto),
							iNachkommastellenPreis);
					if(!(bdNettogesamtpreis == null || position.getN_menge() == null)){
						bdZeilensumme = bdNettogesamtpreis.multiply(position.getN_menge());
					}
				} else {
					bdNettogesamtpreis = getUIObjectBigDecimalNachkommastellen(
							position.getN_nettogesamtpreis(),
							iNachkommastellenPreis);
					if(!(position.getN_nettogesamtpreis() == null || position.getN_menge() == null)){
						bdZeilensumme = position.getN_nettogesamtpreis().multiply(position.getN_menge());
						if(position.getFlrverleih()!=null){
							bdZeilensumme=bdZeilensumme.multiply(new BigDecimal(position.getFlrverleih().getF_faktor()));
						}
					}
				}
				rows[row][col++] = bdNettogesamtpreis;
				rows[row][col++] = bdZeilensumme;
				}else{
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				//Text
				if(position.getX_textinhalt()  != null && !position.getX_textinhalt().equals("")){
					rows[row][col++] = new Boolean(true);
					String text = position.getX_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}else{
					rows[row][col++] = new Boolean(false);
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0,tooltipData);
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
					where.append(" " + FLR_ANGEBOTPOSITION
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
							orderBy.append(FLR_ANGEBOTPOSITION
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
				orderBy.append(FLR_ANGEBOTPOSITION)
						// poseinfuegen: 0 default Sortierung nach iSort
						.append(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANGEBOTPOSITION + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANGEBOTPOSITION).append("i_id")
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
		return FLR_ANGEBOTPOSITION_FROM_CLAUSE;
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
				String queryString = "select " + FLR_ANGEBOTPOSITION + "i_id"
						+ FLR_ANGEBOTPOSITION_FROM_CLAUSE
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
						.getNachkommastellenPreisVK(mandantCNr);

				setTableInfo(new TableInfo(
						new Class[] {
								Integer.class,
								Integer.class,
								String.class,
								super
										.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class,
								String.class,
								String.class,
								super
										.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								super
										.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								Boolean.class
										
						},
						new String[] {
								"i_id",
								getTextRespectUISpr("lp.nr", theClientDto
										.getMandant(), theClientDto.getLocUi()),			
								"",
								getTextRespectUISpr("lp.menge", theClientDto
										.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einheit", theClientDto
										.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("lp.ident",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung", theClientDto
										.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("lp.preis", theClientDto
										.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("lp.zeilensumme", theClientDto
												.getMandant(), theClientDto.getLocUi()),
												getTextRespectUISpr(
														"lp.text", 
														theClientDto.getMandant(), 
														theClientDto.getLocUi())	
												},
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								QueryParameters.FLR_BREITE_S,
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_XXS,
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
												iNachkommastellenPreis),
												3
						},
						new String[] {
								"i_id",
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR,
								AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
								AngebotpositionFac.FLR_ANGEBOTPOSITION_EINHEIT_C_NR,
								Facade.NICHT_SORTIERBAR,
								AngebotpositionFac.FLR_ANGEBOTPOSITION_C_BEZ,
								AngebotpositionFac.FLR_ANGEBOTPOSITION_N_NETTOGESAMTPREIS,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR}));
			}

			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}
	
	public PrintInfoDto getSDocPathAndPartner(Object key){
		AngebotDto angebotDto = null;
		AngebotpositionDto angebotpositionDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			angebotpositionDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey((Integer) key,theClientDto);
			angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotpositionDto.getBelegIId(), theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			//Nicht gefunden
		}
		if(angebotDto!=null && angebotpositionDto != null){
//			String sPath= JCRDocFac.HELIUMV_NODE + "/" + 
//				LocaleFac.BELEGART_ANGEBOT.trim() + 
//				"/" +LocaleFac.BELEGART_ANGEBOT.trim()+"/" +
//				angebotDto.getCNr().replace("/", ".")  + "/" +
//				"Angebotpositionen/" +
//				"Position " + angebotpositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeAgPosition(angebotpositionDto, angebotDto));
			Integer iPartnerIId = null;
			if(partnerDto!=null){
				iPartnerIId= partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}
	
	public String getSTable(){
		return "ANGEBOTSPOSITION";
	}
}
