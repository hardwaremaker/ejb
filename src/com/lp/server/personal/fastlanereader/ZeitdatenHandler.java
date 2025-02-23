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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
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
public class ZeitdatenHandler extends UseCaseHandler {

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
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			Date letzte_zeit = null;
			String sLetzteTaetigkeit = null;
			boolean bEnde = false;

			FLRZeitdaten flrZeitdatenVorgaenger = null;

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRZeitdaten zeitdaten = (FLRZeitdaten) o[0];
				String dauer = null;

				if (letzte_zeit != null) {

					if (zeitdaten.getFlrtaetigkeit() != null
							&& zeitdaten.getFlrtaetigkeit().getC_nr().equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)) {
						// PJ 19944 Bei Kommt Dauer auslassen
					} else {
						long l_zeitdec = zeitdaten.getT_zeit().getTime() - letzte_zeit.getTime();

						Double d = Helper.time2Double(new java.sql.Time(l_zeitdec - 3600000));

						java.text.DecimalFormat df = new java.text.DecimalFormat("00.00");
						dauer = df.format(d.doubleValue()).toString();
					}
				} else {
					dauer = null;
				}

				rows[row][col++] = zeitdaten.getI_id();

				if (zeitdaten.getFlrtaetigkeit() != null) {
					try {
						rows[row][col++] = getZeiterfassungFac()
								.taetigkeitFindByPrimaryKey(zeitdaten.getFlrtaetigkeit().getI_id(), theClientDto)
								.getBezeichnung();
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}
				} else {

					if (zeitdaten.getFlrartikel() != null) {

						rows[row][col++] = zeitdaten.getFlrartikel().getC_nr();
					} else {
						rows[row][col++] = null;
					}
				}

				String sZusatz = null;

				Integer partnerIId = null;
				Integer kundeIId = null;

				if (bEnde == false) {

					if (sLetzteTaetigkeit != null && zeitdaten.getFlrtaetigkeit() != null) {
						if (sLetzteTaetigkeit.equals(zeitdaten.getFlrtaetigkeit().getC_nr())
								&& zeitdaten.getFlrtaetigkeit().getTaetigkeitart_c_nr()
										.equals(ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT)) {
							if (zeitdaten.getFlrtaetigkeit().getC_nr().equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
									|| zeitdaten.getFlrtaetigkeit().getC_nr().equals(ZeiterfassungFac.TAETIGKEIT_GEHT)
									|| zeitdaten.getFlrtaetigkeit().getC_nr()
											.equals(ZeiterfassungFac.TAETIGKEIT_ENDE)) {
							} else {
								sZusatz = "Ende";

								bEnde = true;

								// PJ20931
								String projektZusatz = " ";
								if (zeitdaten.getFlrtaetigkeit().getC_nr()
										.equals(ZeiterfassungFac.TAETIGKEIT_TELEFON)) {
									try {
										TelefonzeitenDto telefonzeitenDto = getZeiterfassungFac()
												.telefonzeitenFindByPersonalIIdTVon(zeitdaten.getPersonal_i_id(),
														new java.sql.Timestamp(
																flrZeitdatenVorgaenger.getT_zeit().getTime()));

										partnerIId=telefonzeitenDto.getPartnerIId();
										
										if (telefonzeitenDto.getProjektIId() != null) {
											ProjektDto pjDto = getProjektFac()
													.projektFindByPrimaryKey(telefonzeitenDto.getProjektIId());

											partnerIId = pjDto.getPartnerIId();

											// PJ21601
											String bemerkung = "";
											if (telefonzeitenDto.getXKommentarint() != null) {
												bemerkung = " "
														+ Helper.strippHTML(telefonzeitenDto.getXKommentarint());
											}

											BereichDto bereichDto = getProjektServiceFac()
													.bereichFindByPrimaryKey(pjDto.getBereichIId());

											projektZusatz = bemerkung + " " + bereichDto.getCBez() + " "
													+ pjDto.getCNr() + " " + pjDto.getCTitel();

										} else if (telefonzeitenDto.getAngebotIId() != null) {
											com.lp.server.angebot.service.AngebotDto angebot = getAngebotFac()
													.angebotFindByPrimaryKey(telefonzeitenDto.getAngebotIId(),
															theClientDto);

											kundeIId = angebot.getKundeIIdAngebotsadresse();

											projektZusatz += " AG" + angebot.getCNr();
											if (angebot.getCBez() != null) {
												projektZusatz = projektZusatz + "," + angebot.getCBez();
											}
										} else if (telefonzeitenDto.getAuftragIId() != null) {
											com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
													.auftragFindByPrimaryKeyOhneExc(telefonzeitenDto.getAuftragIId());

											kundeIId = auftragDto.getKundeIIdAuftragsadresse();

											if (auftragDto != null) {
												projektZusatz += " AB" + auftragDto.getCNr();
												if (auftragDto.getCBezProjektbezeichnung() != null) {
													projektZusatz = projektZusatz + "," + auftragDto.getCBezProjektbezeichnung();
												}
											}
										}

									} catch (Throwable e) {
										// throwEJBExceptionLPRespectOld(e);
									}

								}

								rows[row - 1][col+1] = "Beginn" + projektZusatz;
								
								if (partnerIId != null || kundeIId != null) {

									PartnerDto partnerDto = null;

									if (kundeIId != null) {
										partnerDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto();
									} else {
										partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
									}

									rows[row - 1][col] = partnerDto.formatFixName1Name2();

								} 
								partnerIId=null;
								kundeIId=null;

							}
						}

					}
				} else {
					bEnde = false;
				}

				if (zeitdaten.getArtikel_i_id() != null) {
					if (zeitdaten.getC_belegartnr() != null && zeitdaten.getI_belegartid() != null) {
						if (zeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {

							com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
									.auftragFindByPrimaryKeyOhneExc(zeitdaten.getI_belegartid());

							if (auftragDto != null) {

								kundeIId = auftragDto.getKundeIIdAuftragsadresse();

								sZusatz = "AB" + auftragDto.getCNr();
								if (auftragDto.getCBezProjektbezeichnung() != null) {
									sZusatz = sZusatz + "," + auftragDto.getCBezProjektbezeichnung();
								}
							}

						} else if (zeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
							try {
								com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
										.losFindByPrimaryKey(zeitdaten.getI_belegartid());

								sZusatz = "LO" + losDto.getCNr();

								if (losDto.getKundeIId() != null) {
									kundeIId = losDto.getKundeIId();
								} else if (losDto.getAuftragIId() != null) {
									com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
											.auftragFindByPrimaryKey(losDto.getAuftragIId());
									kundeIId = auftragDto.getKundeIIdAuftragsadresse();
								}

								// PJ18515
								if (losDto.getStuecklisteIId() != null) {
									StuecklisteDto stklDto = getStuecklisteFac()
											.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
									if (stklDto.getArtikelDto() != null
											&& stklDto.getArtikelDto().getArtikelsprDto() != null) {
										sZusatz += "," + stklDto.getArtikelDto().getArtikelsprDto().getCBez();
									}
								}

								if (losDto.getCProjekt() != null) {
									sZusatz = sZusatz + "," + losDto.getCProjekt() + ", ";
								}

								// PJ16029
								if (zeitdaten.getI_belegartpositionid() != null) {
									LossollarbeitsplanDto soapDto = getFertigungFac()
											.lossollarbeitsplanFindByPrimaryKeyOhneExc(
													zeitdaten.getI_belegartpositionid());

									if (soapDto != null) {
										sZusatz += " AG:" + soapDto.getIArbeitsgangnummer();
										if (soapDto.getIUnterarbeitsgang() != null) {
											sZusatz += " UAG:" + soapDto.getIUnterarbeitsgang();
										}
									}

								}

							} catch (RemoteException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
							}
						} else if (zeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {

							com.lp.server.projekt.service.ProjektDto projektDto = null;
							try {
								projektDto = getProjektFac().projektFindByPrimaryKey(zeitdaten.getI_belegartid());

								partnerIId = projektDto.getPartnerIId();

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							sZusatz = "PJ" + projektDto.getCNr();
							if (projektDto.getCTitel() != null) {
								sZusatz = sZusatz + ", " + projektDto.getCTitel();
							}

						} else if (zeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_ANGEBOT)) {
							try {
								com.lp.server.angebot.service.AngebotDto angebot = getAngebotFac()
										.angebotFindByPrimaryKey(zeitdaten.getI_belegartid(), theClientDto);

								kundeIId = angebot.getKundeIIdAngebotsadresse();

								sZusatz = "AG" + angebot.getCNr();
								if (angebot.getCBez() != null) {
									sZusatz = sZusatz + "," + angebot.getCBez();
								}
							} catch (RemoteException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
							}
						}

					}
				}

				// PJ17866
				LosgutschlechtDto[] lgsDtos = getFertigungFac().losgutschlechtFindByZeitdatenIId(zeitdaten.getI_id());
				if (lgsDtos.length > 0) {
					if (sZusatz != null && sZusatz.length() > 0) {
						sZusatz += "; ";
					}
					if (sZusatz == null) {
						sZusatz = "";
					}

					for (int u = 0; u < lgsDtos.length; u++) {
						try {
							LossollarbeitsplanDto lossollaDto = getFertigungFac()
									.lossollarbeitsplanFindByPrimaryKey(lgsDtos[u].getLossollarbeitsplanIId());

							com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
									.losFindByPrimaryKey(lossollaDto.getLosIId());
							sZusatz += "LO" + losDto.getCNr() + ",";

							if (losDto.getStuecklisteIId() != null) {
								StuecklisteDto stklDto = getStuecklisteFac()
										.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
								ArtikelDto artikelDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(stklDto.getArtikelIId(), theClientDto);

								sZusatz += artikelDto.getCNr() + ", ";

							}

							if (lossollaDto.getIArbeitsgangnummer() != null) {
								sZusatz += " AG:" + lossollaDto.getIArbeitsgangnummer();
							}
							if (lossollaDto.getIUnterarbeitsgang() != null) {
								sZusatz += " UAG:" + lossollaDto.getIUnterarbeitsgang();
							}

							sZusatz += ", G:" + Helper.formatZahl(lgsDtos[u].getNGut(), 0, theClientDto.getLocUi());
							sZusatz += ", S:"
									+ Helper.formatZahl(lgsDtos[u].getNSchlecht(), 0, theClientDto.getLocUi());
							sZusatz += ", I:"
									+ Helper.formatZahl(lgsDtos[u].getNInarbeit(), 0, theClientDto.getLocUi());

							sZusatz += "; ";
						} catch (RemoteException ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
						}
					}

				}

				if (partnerIId != null || kundeIId != null) {

					PartnerDto partnerDto = null;

					if (kundeIId != null) {
						partnerDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto();
					} else {
						partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
					}

					rows[row][col++] = partnerDto.formatFixName1Name2();

				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = sZusatz;
				rows[row][col++] = new java.sql.Time(zeitdaten.getT_zeit().getTime()).toString();

				// Damit Dauer eines vorher steht
				if (row > 0) {
					rows[row - 1][col++] = dauer;
				} else {
					rows[row][col++] = dauer;

				}

				String sNurTaetigkeitGeaendert = "";
				if (Helper.short2boolean(zeitdaten.getB_taetigkeitgeaendert()) == true) {
					sNurTaetigkeitGeaendert = " B";
				}
				String sAutomatikbuchung = "";
				if (Helper.short2boolean(zeitdaten.getB_automatikbuchung()) == true) {
					sAutomatikbuchung = " A";
				}

				rows[row][col++] = istBuchungManipuliert(zeitdaten.getT_zeit(), zeitdaten.getT_aendern())
						+ sNurTaetigkeitGeaendert + sAutomatikbuchung;

				rows[row++][col++] = zeitdaten.getC_wowurdegebucht();

				letzte_zeit = zeitdaten.getT_zeit();
				if (zeitdaten.getFlrtaetigkeit() != null) {
					sLetzteTaetigkeit = zeitdaten.getFlrtaetigkeit().getC_nr();
				} else {
					sLetzteTaetigkeit = null;
				}

				flrZeitdatenVorgaenger = zeitdaten;

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

	public static String istBuchungManipuliert(Date t1, Date t2) throws EJBExceptionLP {
		String sManipuliert = "";

		if ((t1.getTime() - t2.getTime()) > 180000 || (t1.getTime() - t2.getTime()) < -180000) {
			sManipuliert = "H";
		}

		return sManipuliert;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
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
						where.append(" upper(" + filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toUpperCase());
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
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
				orderBy.append("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ");
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
		return "from FLRZeitdaten zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select zeitdaten.i_id from FLRZeitdaten zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr  "
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
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
							String.class, String.class },
					new String[] { "Id", getTextRespectUISpr("lp.taetigkeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.firma", mandantCNr, locUI),
							getTextRespectUISpr("lp.zusatz", mandantCNr, locUI),
							getTextRespectUISpr("lp.zeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("lp.bem", mandantCNr, locUI),
							getTextRespectUISpr("lp.quelle", mandantCNr, locUI), },
					new int[] { -1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, 8, QueryParameters.FLR_BREITE_L },
					new String[] { "i_id",
							// nodbfeld: 1: Hier an jede Spalte, die nicht
							// sortiert werden kann ein Facade.NICHT_SORTIERBAR
							// anfuegen, damit
							// damit am Client das Symbol 'nosort.png' im
							// TabelHeader angezeigt wird
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATEN_C_WOWURDEGEBUCHT }));

		}

		return super.getTableInfo();
	}
}
