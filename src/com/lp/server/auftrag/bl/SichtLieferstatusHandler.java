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
package com.lp.server.auftrag.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.SichtLieferstatusDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Sicht des Auftrags auf seinen Lieferstatus.
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-06
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */

public class SichtLieferstatusHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Alle Informationen fuer die Darstellung des WrapperTable am Client. */
	private TableInfo tableInfo = null;

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// man weiss vorher nicht, wieviele Datensaetze im Ergebnis sind,
			// deshalb werden
			// sie vor der Darstellung hier gesammelt; Datensaetze, die dieselbe
			// Auftragposition betreffen, muessen kummuliert dargestellt werden.
			//
			// Achtung: Die Sammelstelle enthaelt an der ersten Position eines
			// Datensatzes
			// die PositionsId. Nachdem Positionen von verschiedenen Belegarten
			// auftauchen koennen, muss es moeglich sein, doppelte PositionsIds
			// zu verwenden.
			// Die Sammelstelle ist daher vom Typ ArrayList. Keine Map
			// verwenden!

			ArrayList<SichtLieferstatusDto> alSammelstelle = new ArrayList<SichtLieferstatusDto>();

			Integer iIdAuftrag = getDefaultFilterAuftragIId();

			// Schritt 1 : Alle Lieferscheine zu diesem Auftrag durchforsten.
			// Fuer
			// stornierte Lieferscheine gilt: Die Referenzen auf
			// Auftragpositionen
			// exisiteren noch. Die Lieferscheinpositionen duerfen jedoch nicht
			// mehr
			// in der Ansicht erscheinen.
			LieferscheinDto[] aLieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByAuftrag(iIdAuftrag, theClientDto);

			if (aLieferscheinDto != null) {
				for (int i = 0; i < aLieferscheinDto.length; i++) {
					if (!aLieferscheinDto[i].getStatusCNr().equals(
							LieferscheinFac.LSSTATUS_STORNIERT)) {
						// zu jedem nicht stornierten Lieferschein die
						// positionen durchkaemmen
						LieferscheinpositionDto[] aPosDto = getLieferscheinpositionFac()
								.lieferscheinpositionFindByLieferscheinIId(
										aLieferscheinDto[i].getIId());

						if (aPosDto != null) {
							for (int j = 0; j < aPosDto.length; j++) {

								// alle mengenbehafteten Positionen pruefen
								if (aPosDto[j].getNMenge() != null) {
									SichtLieferstatusDto oDto = new SichtLieferstatusDto();

									boolean bSchonVorhanden = false;

									// Schritt 1a : Hat die relevante Position
									// einen Bezug zum Auftrag?
									if (aPosDto[j].getAuftragpositionIId() != null) {

										AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
												.auftragpositionFindByPrimaryKey(
														aPosDto[j]
																.getAuftragpositionIId());

										// wenn es schon einen Eintrag zu dieser
										// Position gibt;
										// Vorsicht: fuer die UI-Anzeige wurde
										// die Kurzbezeichnung hinterlegt!
										SichtLieferstatusDto oSchonVorhanden = containsSichtLieferstatusDto(
												alSammelstelle,
												getLocaleFac()
														.belegartFindByCNr(
																LocaleFac.BELEGART_AUFTRAG)
														.getCKurzbezeichnung(),
												oAuftragpositionDto.getIId());

										if (oSchonVorhanden != null) {
											bSchonVorhanden = true;
											oSchonVorhanden
													.setNMengeGeliefert(oSchonVorhanden
															.getNMengeGeliefert()
															.add(
																	aPosDto[j]
																			.getNMenge()));
											oDto = oSchonVorhanden;
										} else {
											AuftragDto oAuftragDto = getAuftragFac()
													.auftragFindByPrimaryKey(
															oAuftragpositionDto
																	.getBelegIId());

											oDto
													.setIiPosition(oAuftragpositionDto
															.getIId());
											oDto
													.setSBelegart(getLocaleFac()
															.belegartFindByCNr(
																	oAuftragDto
																			.getBelegartCNr())
															.getCKurzbezeichnung());
											oDto.setSBelegnummer(oAuftragDto
													.getCNr());
											oDto
													.setNMengeGesamt(oAuftragpositionDto
															.getNMenge());
											oDto.setNMengeGeliefert(aPosDto[j]
													.getNMenge());

											String sBezeichnung = null;

											if (oAuftragpositionDto.getCBez() != null) {
												sBezeichnung = oAuftragpositionDto
														.getCBez();
											} else {
												sBezeichnung = getArtikelBezeichnung(oAuftragpositionDto
														.getArtikelIId());
											}
											oDto
													.setSIdent(getArtikelCNr(oAuftragpositionDto
															.getArtikelIId()));
											oDto.setSBezeichnung(sBezeichnung);

											if (oAuftragpositionDto
													.getAuftragpositionstatusCNr()
													.equals(
															AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
												oDto.setBErledigt(true);
											} else {
												oDto.setBErledigt(false);
											}
										}
									}

									// Schritt 1b : Die relevante Position
									// existiert nur im Lieferschein
									else {
										LieferscheinDto oLieferscheinDto = getLieferscheinFac()
												.lieferscheinFindByPrimaryKey(
														aPosDto[j]
																.getLieferscheinIId(),
																theClientDto);

										oDto.setIiPosition(aPosDto[j].getIId());
										oDto
												.setSBelegart(getLocaleFac()
														.belegartFindByCNr(
																oLieferscheinDto
																		.getBelegartCNr())
														.getCKurzbezeichnung());
										oDto.setSBelegnummer(oLieferscheinDto
												.getCNr());
										oDto.setNMengeGesamt(null);
										oDto.setNMengeGeliefert(aPosDto[j]
												.getNMenge());

										String sBezeichnung = null;

										if (aPosDto[j].getCBez() != null) {
											sBezeichnung = aPosDto[j].getCBez();
										} else {
											sBezeichnung = getArtikelBezeichnung(aPosDto[j]
													.getArtikelIId());
										}
										oDto.setSIdent(getArtikelCNr(aPosDto[j]
												.getArtikelIId()));
										oDto.setSBezeichnung(sBezeichnung);

										oDto.setBErledigt(true);
									}

									// Fuer die Kombination Belegart,
									// PositionIId ist bereits ein
									// SichtLieferstatusDto oDto ist in der
									// Liste vorhanden. Dieser
									// Eintrag muss entfernt und durch den neuen
									// ersetzt werden.
									if (bSchonVorhanden) {
										removeSichtLieferstatusDto(
												alSammelstelle, oDto
														.getSBelegart(), oDto
														.getIiPosition());
									}

									alSammelstelle.add(oDto);
								}
							}
						}
					}
				}
			}

			// Schritt 2 : die offenen Positionen aus dem Auftrag dranhaengen
			AuftragpositionDto[] aPos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftrag);

			if (aPos != null && aPos.length > 0) {
				for (int i = 0; i < aPos.length; i++) {
					if (aPos[i].getNMenge() != null) {
						if (aPos[i].getAuftragpositionstatusCNr().equals(
								AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN)) {
							AuftragDto oAuftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(
											aPos[i].getBelegIId());

							SichtLieferstatusDto oDto = new SichtLieferstatusDto();
							oDto.setIiPosition(aPos[i].getIId());
							oDto.setSBelegart(getLocaleFac().belegartFindByCNr(
									oAuftragDto.getBelegartCNr())
									.getCKurzbezeichnung());
							oDto.setSBelegnummer(oAuftragDto.getCNr());
							oDto.setNMengeGesamt(aPos[i].getNMenge());
							
							BigDecimal bdOffeneMenge=aPos[i].getNOffeneMenge();
							if(bdOffeneMenge==null){
								bdOffeneMenge=aPos[i].getNMenge();
							}
							oDto.setNMengeGeliefert(aPos[i].getNMenge()
									.subtract(bdOffeneMenge));

							String sBezeichnung = null;

							if (aPos[i].getCBez() != null) {
								sBezeichnung = aPos[i].getCBez();
							} else {
								sBezeichnung = getArtikelBezeichnung(aPos[i]
										.getArtikelIId());
							}
							oDto.setSIdent(getArtikelCNr(aPos[i]
									.getArtikelIId()));
							oDto.setSBezeichnung(sBezeichnung);

							oDto.setBErledigt(false);

							alSammelstelle.add(oDto);
						}
					}
				}
			}

			
			// in diesem Fall liegt ein logischer Implementierungsfehler vor,
			// fuehrt
			// entweder zu leerer Anzeige oder zu ENDLOSSCHLEIFE!
			if (super.getRowCount() != alSammelstelle.size()) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
						new Exception("rowCount != alSammelstelle.size"));
			}

			// IMS ID 458 Alle AB Positionen oben, alle LS Positionen unten
			String[] aBelegartenKurzbezeichung = new String[] {
					getLocaleFac()
							.belegartFindByCNr(LocaleFac.BELEGART_AUFTRAG)
							.getCKurzbezeichnung(),
					getLocaleFac().belegartFindByCNr(
							LocaleFac.BELEGART_LIEFERSCHEIN)
							.getCKurzbezeichnung(),
					getLocaleFac().belegartFindByCNr(
							LocaleFac.BELEGART_RECHNUNG).getCKurzbezeichnung() };

			ArrayList<SichtLieferstatusDto> alSammelstelleGeordnet = new ArrayList<SichtLieferstatusDto>();
			int iIndex = 0;

			for (int i = 0; i < aBelegartenKurzbezeichung.length; i++) {
				String cBelegartKurzbezeichnung = aBelegartenKurzbezeichung[i];

				for (int j = 0; j < alSammelstelle.size(); j++) {
					if (((SichtLieferstatusDto) alSammelstelle.get(j))
							.getSBelegart().equals(cBelegartKurzbezeichnung)) {
						alSammelstelleGeordnet.add(iIndex, alSammelstelle
								.get(j));

						iIndex++;
					}
				}
			}

			// jetzt die darstellung in der tabelle zusammenbauen
			int startIndex = 0;
			int endIndex = startIndex + alSammelstelle.size() - 1;

			Object[][] rows = new Object[alSammelstelle.size()][getTableInfo()
					.getColumnHeaderValues().length];
			int row = 0;
			int col = 0;

			Iterator<SichtLieferstatusDto> it = alSammelstelleGeordnet.iterator();

			while (it.hasNext()) {
				SichtLieferstatusDto oStatusDto = (SichtLieferstatusDto) it
						.next();

				rows[row][col++] = oStatusDto.getIiPosition(); // i_id der
				// Position
				rows[row][col++] = oStatusDto.getSBelegart(); // Belegart der
				// Position
				rows[row][col++] = oStatusDto.getSBelegnummer();
				rows[row][col++] = oStatusDto.getSIdent();
				rows[row][col++] = oStatusDto.getSBezeichnung();
				rows[row][col++] = oStatusDto.getNMengeGesamt();
				rows[row][col++] = oStatusDto.getNMengeGeliefert();
				rows[row++][col++] = new Boolean((oStatusDto).getBErledigt());

				col = 0;
			}

			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		}

		return result;
	}

	private String getArtikelBezeichnung(Integer iIdArtikelI) throws Throwable {
		String sBezO = null;

		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
				iIdArtikelI, theClientDto);

		if (oArtikelDto.getArtikelsprDto() != null
				&& oArtikelDto.getArtikelsprDto().getCBez() != null) {
			sBezO = oArtikelDto.getArtikelsprDto().getCBez();
		} else {
			sBezO = oArtikelDto.getCNr();
		}

		return sBezO;
	}

	private String getArtikelCNr(Integer iIdArtikelI) throws Throwable {
		if(null == iIdArtikelI) return null ;
		
		String sCNr = null;
		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
				iIdArtikelI, theClientDto);
		sCNr = oArtikelDto.getCNr();
		return sCNr;
	}

	/**
	 * Das Filter Kriterum fuer diese Ansicht ist die iId des Auftrags.
	 * 
	 * @throws NumberFormatException
	 * @return Integer
	 */
	private Integer getDefaultFilterAuftragIId() throws NumberFormatException {
		Integer iIdAuftrag = null;

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;

			// ACHTUNG ! An dieser Stelle muss ich wissen, welche Kriterien ich
			// erwarte
			if (filterKriterien != null && filterKriterien.length > 0) {
				iIdAuftrag = new Integer(filterKriterien[0].value);
			}
		}

		if (iIdAuftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdAuftrag == null"));
		}

		return iIdAuftrag;
	}

	/**
	 * gets the total number of rows represented by the current query. <br>
	 * Das ist der erste Teil des Aufbaus der Tabelle, er bestimmt ueber die
	 * korrekte Anzeige der Scrollbar.
	 * 
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;

		try {
			// Datensaetze, die dieselbe Auftragposition betreffen werden
			// kummuliert
			// dargestellt
			ArrayList<Integer> alSammelstelle = new ArrayList<Integer>();

			Integer iIdAuftrag = getDefaultFilterAuftragIId();

			// Schritt 1 : Alle Lieferscheine zu diesem Auftrag durchforsten
			LieferscheinDto[] aLieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByAuftrag(iIdAuftrag, theClientDto);

			if (aLieferscheinDto != null) {
				for (int i = 0; i < aLieferscheinDto.length; i++) {
					if (!aLieferscheinDto[i].getStatusCNr().equals(
							LieferscheinFac.LSSTATUS_STORNIERT)) {
						// zu jedem nicht stornierten Lieferschein die
						// Positionen durchkaemmen
						LieferscheinpositionDto[] aPosDto = getLieferscheinpositionFac()
								.lieferscheinpositionFindByLieferscheinIId(
										aLieferscheinDto[i].getIId());

						if (aPosDto != null) {
							for (int j = 0; j < aPosDto.length; j++) {

								// alle mengenbehafteten Positionen pruefen
								if (aPosDto[j].getNMenge() != null) {

									// Schritt 1a : Hat die relevante Position
									// einen Bezug zum Auftrag?
									if (aPosDto[j].getAuftragpositionIId() != null) {
										AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
												.auftragpositionFindByPrimaryKey(
														aPosDto[j]
																.getAuftragpositionIId());

										// wenn es noch keinen Eintrag zu dieser
										// Position gibt
										if (!alSammelstelle
												.contains(oAuftragpositionDto
														.getIId())) {
											alSammelstelle
													.add(oAuftragpositionDto
															.getIId());
											rowCount++;
										}
									}

									// Schritt 1b : Die relevante Position
									// existiert nur im Lieferschein
									else {
										rowCount++;
									}
								}
							}
						}
					}
				}
			}

			// Schritt 2 : die offenen Positionen aus dem Auftrag dranhaengen
			rowCount += getAuftragpositionFac()
					.berechneAnzahlArtikelpositionenMitStatus(
							getDefaultFilterAuftragIId(),
							AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		}

		// myLogger.info("rowCount: " + rowCount);

		return rowCount;
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
		int rowNumber = 0; // selektiert ist immer die erste zeile
		QueryResult result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	/**
	 * gets information about the table.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(new Class[] { String.class, String.class,
					String.class, String.class, String.class, BigDecimal.class,
					BigDecimal.class, Boolean.class }, new String[] { " ", " ",
					getTextRespectUISpr("lp.belegnummer", mandantCNr, locUI),
					getTextRespectUISpr("lp.artikelnummer", mandantCNr, locUI),
					getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
					getTextRespectUISpr("lp.menge", mandantCNr, locUI),
					getTextRespectUISpr("auft.geliefert", mandantCNr, locUI),
					getTextRespectUISpr("auft.erledigt", mandantCNr, locUI) },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // variabel
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S }, new String[] { "",
							"", "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	/**
	 * In einer ArrayList von SichtLieferstatusDtos soll festgestellt werden, ob
	 * ein bsteimmtes SichtLieferstatusDto enthalten ist.
	 * 
	 * @param alListeI
	 *            ArrayList Liste von SichtLieferstatusDtos
	 * @param sBelegartI
	 *            die gesuchte Belegart
	 * @param iIdPositionI
	 *            die gesuchte Position innerhalb dieser Belegart
	 * @return SichtLieferstatusDto null, wenn die gesuchte Position nicht
	 *         enthalten ist
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private SichtLieferstatusDto containsSichtLieferstatusDto(
			ArrayList<SichtLieferstatusDto> alListeI, String sBelegartI, Integer iIdPositionI)
			throws Throwable {
		SichtLieferstatusDto oDtoO = null;

		for (int i = 0; i < alListeI.size(); i++) {
			SichtLieferstatusDto oCurrentDto = (SichtLieferstatusDto) alListeI
					.get(i);

			if (oCurrentDto.getSBelegart().equals(sBelegartI)) {
				if (oCurrentDto.getIiPosition().equals(iIdPositionI)) {
					oDtoO = oCurrentDto;
				}
			}
		}

		return oDtoO;
	}

	private void removeSichtLieferstatusDto(ArrayList<SichtLieferstatusDto> alListeI,
			String sBelegartI, Integer iIdPositionI) throws Throwable {
		for (int i = 0; i < alListeI.size(); i++) {
			SichtLieferstatusDto oCurrentDto = (SichtLieferstatusDto) alListeI
					.get(i);

			if (oCurrentDto.getSBelegart().equals(sBelegartI)) {
				if (oCurrentDto.getIiPosition().equals(iIdPositionI)) {
					alListeI.remove(oCurrentDto);
				}
			}
		}
	}

	private SichtLieferstatusDto getErstesSichtLieferstatusDtoMitBelegartkurzbezeichnung(
			ArrayList alListeI, String cBelegartkurzbezeichnung)
			throws Throwable {
		SichtLieferstatusDto sichtlieferstatusDto = null;

		boolean bGefunden = false;
		int iIndex = 0;

		if (alListeI != null && alListeI.size() > 0) {
			while (!bGefunden && iIndex < alListeI.size()) {
				SichtLieferstatusDto sichtlieferstatusDtoTemp = (SichtLieferstatusDto) alListeI
						.get(iIndex);

				iIndex++;

				if (sichtlieferstatusDtoTemp.getSBelegart().equals(
						cBelegartkurzbezeichnung)) {
					sichtlieferstatusDto = sichtlieferstatusDtoTemp;

					bGefunden = true;
				}
			}
		}

		return sichtlieferstatusDto;
	}
}
