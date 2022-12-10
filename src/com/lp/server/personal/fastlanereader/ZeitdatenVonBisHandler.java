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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
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
public class ZeitdatenVonBisHandler extends UseCaseHandlerTabelle {

	private int SPALTE_ZEITDATEN_I_ID = 0;
	private int SPALTE_TAETIGKEIT = 1;
	private int SPALTE_BEZEICHNUNG = 2;
	private int SPALTE_ZUSATZ = 3;
	private int SPALTE_ZEIT_VON = 4;
	private int SPALTE_ZEIT_BIS = 5;

	private int SPALTE_DAUER = 6;
	private int SPALTE_BEMERKUNG = 7;
	private int SPALTE_QUELLE = 8;
	private int ANZAHL_SPALTEN = 9;

	private ArrayList<Object[]> hmDaten = null;

	private static final long serialVersionUID = 1L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// die Anzahl der Zeilen festlegen und den Inhalt befuellen
			setInhalt();

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			for (int row = 0; row < getAnzahlZeilen(); row++) {

				rows[row] = hmDaten.get(row);

			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

	public static String istBuchungManipuliert(Date t1, Date t2)
			throws EJBExceptionLP {
		String sManipuliert = "";

		if ((t1.getTime() - t2.getTime()) > 180000
				|| (t1.getTime() - t2.getTime()) < -180000) {
			sManipuliert = "H";
		}

		return sManipuliert;
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<Object[]>();
			getFilterKriterien();

			setInhalt();
		} catch (Throwable t) {
			if (t.getCause() instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t.getCause();
			} else {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
						new Exception(t));
			}
		}
		return getAnzahlZeilen();
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

	public ArrayList<Object[]> setInhalt() throws RemoteException {
		hmDaten = new ArrayList<Object[]>();

		HashSet<Integer> hBereitsAlsBisVerbraucht = new HashSet<Integer>();

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();
		Integer personalIId = new Integer(aFilterKriterium[0].value);
		Session session = FLRSessionFactory.getFactory().openSession();
		setFilter(session);
		Query query = session
				.createQuery("from FLRZeitdaten zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr WHERE zeitdaten.personal_i_id="
						+ personalIId
						+ " AND zeitdaten.t_zeit >= "
						+ aFilterKriterium[1].value
						+ " AND zeitdaten.t_zeit < "
						+ aFilterKriterium[2].value
						+ " ORDER BY zeitdaten.t_zeit ASC");

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLRZeitdaten zeitdaten = (FLRZeitdaten) o[0];

			if (hBereitsAlsBisVerbraucht.contains(zeitdaten.getI_id())) {
				continue;
			}

			Object[] oZeile = new Object[ANZAHL_SPALTEN];

			oZeile[SPALTE_ZEIT_VON] = new java.sql.Time(zeitdaten.getT_zeit()
					.getTime());

			oZeile[SPALTE_ZEITDATEN_I_ID] = zeitdaten.getI_id();

			if (zeitdaten.getFlrtaetigkeit() != null) {
				try {
					oZeile[SPALTE_TAETIGKEIT] = getZeiterfassungFac()
							.taetigkeitFindByPrimaryKey(
									zeitdaten.getFlrtaetigkeit().getI_id(),
									theClientDto).getBezeichnung();
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			} else {
				oZeile[SPALTE_TAETIGKEIT] = zeitdaten.getFlrartikel().getC_nr();

				oZeile[SPALTE_BEZEICHNUNG] = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								zeitdaten.getFlrartikel().getI_id(),
								theClientDto).formatBezeichnung();

			}

			String sZusatz = null;

			if (zeitdaten.getArtikel_i_id() != null) {
				if (zeitdaten.getC_belegartnr() != null
						&& zeitdaten.getI_belegartid() != null) {
					if (zeitdaten.getC_belegartnr().equals(
							LocaleFac.BELEGART_AUFTRAG)) {

						com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(
										zeitdaten.getI_belegartid());

						sZusatz = "AB" + auftragDto.getCNr();
						if (auftragDto.getCBezProjektbezeichnung() != null) {
							sZusatz = sZusatz + ","
									+ auftragDto.getCBezProjektbezeichnung();
						}

					} else if (zeitdaten.getC_belegartnr().equals(
							LocaleFac.BELEGART_LOS)) {
						try {
							com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
									.losFindByPrimaryKey(
											zeitdaten.getI_belegartid());

							sZusatz = "LO" + losDto.getCNr();
							if (losDto.getCProjekt() != null) {
								sZusatz = sZusatz + "," + losDto.getCProjekt();
							}

							// PJ 16029
							if (zeitdaten.getI_belegartpositionid() != null) {
								LossollarbeitsplanDto soapDto = getFertigungFac()
										.lossollarbeitsplanFindByPrimaryKeyOhneExc(
												zeitdaten
														.getI_belegartpositionid());

								if (soapDto != null) {
									sZusatz += " AG:"
											+ soapDto.getIArbeitsgangnummer();
									if (soapDto.getIUnterarbeitsgang() != null) {
										sZusatz += " UAG:"
												+ soapDto
														.getIUnterarbeitsgang();
									}
								}

							}

						} catch (RemoteException ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
						}
					} else if (zeitdaten.getC_belegartnr().equals(
							LocaleFac.BELEGART_PROJEKT)) {

						com.lp.server.projekt.service.ProjektDto projektDto = null;
						try {
							projektDto = getProjektFac()
									.projektFindByPrimaryKey(
											zeitdaten.getI_belegartid());
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						sZusatz = "PJ" + projektDto.getCNr();
						if (projektDto.getCTitel() != null) {
							sZusatz = sZusatz + ", " + projektDto.getCTitel();
						}

					} else if (zeitdaten.getC_belegartnr().equals(
							LocaleFac.BELEGART_ANGEBOT)) {
						try {
							com.lp.server.angebot.service.AngebotDto angebot = getAngebotFac()
									.angebotFindByPrimaryKey(
											zeitdaten.getI_belegartid(),
											theClientDto);

							sZusatz = "AG" + angebot.getCNr();
							if (angebot.getCBez() != null) {
								sZusatz = sZusatz + "," + angebot.getCBez();
							}
						} catch (RemoteException ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
						}
					}

					// PJ17866
					LosgutschlechtDto[] lgsDtos = getFertigungFac()
							.losgutschlechtFindByZeitdatenIId(
									zeitdaten.getI_id());
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
										.lossollarbeitsplanFindByPrimaryKey(
												lgsDtos[u]
														.getLossollarbeitsplanIId());

								com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
										.losFindByPrimaryKey(
												lossollaDto.getLosIId());
								sZusatz += "LO" + losDto.getCNr() + ",";

								if (losDto.getStuecklisteIId() != null) {
									StuecklisteDto stklDto = getStuecklisteFac()
											.stuecklisteFindByPrimaryKey(
													losDto.getStuecklisteIId(),
													theClientDto);
									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													stklDto.getArtikelIId(),
													theClientDto);

									sZusatz += artikelDto.getCNr() + ", ";

								}

								if (lossollaDto.getIArbeitsgangnummer() != null) {
									sZusatz += " AG:"
											+ lossollaDto
													.getIArbeitsgangnummer();
								}
								if (lossollaDto.getIUnterarbeitsgang() != null) {
									sZusatz += " UAG:"
											+ lossollaDto
													.getIUnterarbeitsgang();
								}

								sZusatz += ", G:"
										+ Helper.formatZahl(
												lgsDtos[u].getNGut(), 0,
												theClientDto.getLocUi());
								sZusatz += ", S:"
										+ Helper.formatZahl(
												lgsDtos[u].getNSchlecht(), 0,
												theClientDto.getLocUi());
								sZusatz += ", I:"
										+ Helper.formatZahl(
												lgsDtos[u].getNInarbeit(), 0,
												theClientDto.getLocUi());

								sZusatz += "; ";
							} catch (RemoteException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
										ex);
							}
						}

					}

					oZeile[SPALTE_ZUSATZ] = sZusatz;
					oZeile[SPALTE_ZEIT_VON] = new java.sql.Time(zeitdaten
							.getT_zeit().getTime()).toString();

					String sNurTaetigkeitGeaendert = "";
					if (Helper.short2boolean(zeitdaten
							.getB_taetigkeitgeaendert()) == true) {
						sNurTaetigkeitGeaendert = " B";
					}
					String sAutomatikbuchung = "";
					if (Helper.short2boolean(zeitdaten.getB_automatikbuchung()) == true) {
						sAutomatikbuchung = " A";
					}

					oZeile[SPALTE_BEMERKUNG] = istBuchungManipuliert(
							zeitdaten.getT_zeit(), zeitdaten.getT_aendern())
							+ sNurTaetigkeitGeaendert + sAutomatikbuchung;

					oZeile[SPALTE_QUELLE] = zeitdaten.getC_wowurdegebucht();

				}

			}
			ZeitdatenDto zeitdatenDto = getZeiterfassungFac()
					.zeitdatenFindByPrimaryKey(zeitdaten.getI_id(),
							theClientDto);

			if (zeitdatenDto.gettZeit_Bis() != null) {
				long l_zeitdec = zeitdatenDto.gettZeit_Bis().getTime()
						- zeitdaten.getT_zeit().getTime();

				Double d = Helper.time2Double(new java.sql.Time(
						l_zeitdec - 3600000));

				java.text.DecimalFormat df = new java.text.DecimalFormat(
						"00.00");
				String dauer = df.format(d.doubleValue()).toString();

				oZeile[SPALTE_DAUER] = dauer;
				oZeile[SPALTE_ZEIT_BIS] = new java.sql.Time(zeitdatenDto
						.gettZeit_Bis().getTime()).toString();

				hBereitsAlsBisVerbraucht.add(zeitdatenDto
						.getZeitdatenIId_BisZeit());
			}
			hmDaten.add(oZeile);
		}
		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);

		return hmDaten;

	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer
				&& ((Integer) selectedId).intValue() >= 0) {

			try {

				if (hmDaten != null) {
					Iterator it = hmDaten.iterator();
					int i = 0;
					while (it.hasNext()) {
						
						Object[] row = (Object[]) it.next();
						Integer id = (Integer) row[0];
						if (selectedId.equals(id)) {
							rowNumber = i;
							break;
						}
						i++;
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {

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
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.taetigkeit", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.zusatz", mandantCNr, locUI),
							getTextRespectUISpr("lp.zeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.bis", mandantCNr, locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("lp.bem", mandantCNr, locUI),
							getTextRespectUISpr("lp.quelle", mandantCNr, locUI), },
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, 8,
							QueryParameters.FLR_BREITE_L }, new String[] {
							"i_id",
							// nodbfeld: 1: Hier an jede Spalte, die nicht
							// sortiert werden kann ein Facade.NICHT_SORTIERBAR
							// anfuegen, damit
							// damit am Client das Symbol 'nosort.png' im
							// TabelHeader angezeigt wird
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_ZEITDATEN_C_WOWURDEGEBUCHT });

		}
		return tableInfo;
	}
}
