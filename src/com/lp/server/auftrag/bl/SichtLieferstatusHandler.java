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
package com.lp.server.auftrag.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.fastlanereader.FLRAuftragPositionNumberAdapter;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.SichtLieferstatusDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberAdapter;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.PositionNumberHandlerFullPaged;
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

public class SichtLieferstatusHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Alle Informationen fuer die Darstellung des WrapperTable am Client. */
	private TableInfo tableInfo = null;

	private ArrayList<SichtLieferstatusDto> alDaten = null;

	public SichtLieferstatusHandler() {
		super();
		setAnzahlSpalten(15);
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// die Anzahl der Zeilen festlegen und den Inhalt befuellen
			setInhalt();

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			BelegartDto bDto = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_AUFTRAG);

			for (int row = 0; row < getAnzahlZeilen(); row++) {

				SichtLieferstatusDto sichtlieferstatusDto = alDaten.get(row);

				Object[] rowToAddCandidate = new Object[getTableInfo().getColumnClasses().length];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("id")] = sichtlieferstatusDto
						.getIiPosition();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("belegart")] = sichtlieferstatusDto
						.getSBelegart();

				if (sichtlieferstatusDto.getSBelegart() != null
						&& sichtlieferstatusDto.getSBelegart().equals(bDto.getCKurzbezeichnung())) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.nr")] = getAuftragpositionFac()
							.getPositionNummer(sichtlieferstatusDto.getIiPosition());

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.belegnummer")] = sichtlieferstatusDto
						.getSBelegnummer();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.artikelnummer")] = sichtlieferstatusDto
						.getSIdent();

				if (bReferenznummerInPositionen) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.referenznummer")] = sichtlieferstatusDto.getSReferenznummer();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = sichtlieferstatusDto
						.getSBezeichnung();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.menge")] = sichtlieferstatusDto
						.getNMengeGesamt();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.geliefert")] = sichtlieferstatusDto
						.getNMengeGeliefert();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.offen")] = sichtlieferstatusDto
						.getNMengeOffen();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.erledigt")] = sichtlieferstatusDto
						.getBErledigt();

				rows[row] = rowToAddCandidate;

			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

	private String getArtikelBezeichnung(Integer iIdArtikelI) throws Throwable {
		String sBezO = null;

		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(iIdArtikelI, theClientDto);

		if (oArtikelDto.getArtikelsprDto() != null && oArtikelDto.getArtikelsprDto().getCBez() != null) {
			sBezO = oArtikelDto.getArtikelsprDto().getCBez();
		} else {
			sBezO = oArtikelDto.getCNr();
		}

		return sBezO;
	}

	private String getArtikelCNr(Integer iIdArtikelI) throws Throwable {
		if (null == iIdArtikelI)
			return null;

		String sCNr = null;
		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(iIdArtikelI, theClientDto);
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
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;

			// ACHTUNG ! An dieser Stelle muss ich wissen, welche Kriterien ich
			// erwarte
			if (filterKriterien != null && filterKriterien.length > 0) {
				iIdAuftrag = new Integer(filterKriterien[0].value);
			}
		}

		if (iIdAuftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdAuftrag == null"));
		}

		return iIdAuftrag;
	}

	private ArrayList setInhalt() {

		try {

			alDaten = new ArrayList<SichtLieferstatusDto>();

			String belegartKurzAuftrag = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_AUFTRAG)
					.getCKurzbezeichnung();
			String belegartKurzLS = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_LIEFERSCHEIN)
					.getCKurzbezeichnung();
			String belegartKurzRE = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_RECHNUNG).getCKurzbezeichnung();

			// die aktuellen Filter Kriterien bestimmen
			getFilterKriterien();
			Integer auftragIId = new Integer(aFilterKriterium[0].value);

			AuftragDto oAuftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

			// Alle mengenbehafteten Auftragspositionen holen
			Session session = FLRSessionFactory.getFactory().openSession();
			Query query = session.createQuery("SELECT ap FROM FLRAuftragposition ap WHERE ap.flrauftrag.i_id="
					+ auftragIId + " AND ap.flrartikel.i_id IS NOT NULL AND ap.n_menge IS NOT NULL ORDER BY ap.i_sort");

			List<?> resultListAG = query.list();
			Iterator<?> resultListIteratorAG = resultListAG.iterator();
			while (resultListIteratorAG.hasNext()) {

				FLRAuftragposition ap = (FLRAuftragposition) resultListIteratorAG.next();

				SichtLieferstatusDto oDto = new SichtLieferstatusDto();
				oDto.setSBelegart(belegartKurzAuftrag);
				oDto.setSBelegnummer(oAuftragDto.getCNr());

				if (ap.getAuftragpositionstatus_c_nr().equals(LocaleFac.STATUS_ERLEDIGT)) {
					oDto.setBErledigt(true);
				}
				oDto.setIiPosition(ap.getI_id());
				oDto.setNMengeGesamt(ap.getN_menge());

				BigDecimal bdGeliefert = getAuftragpositionFac().getGeliefertMenge(ap.getI_id(), null, theClientDto);

				oDto.setNMengeGeliefert(bdGeliefert);
				oDto.setNMengeOffen(ap.getN_menge().subtract(bdGeliefert));

				oDto.setSIdent(ap.getFlrartikel().getC_nr());
				oDto.setSReferenznummer(ap.getFlrartikel().getC_referenznr());

				String sBezeichnung = null;

				if (ap.getC_bez() != null) {
					sBezeichnung = ap.getC_bez();
				} else {
					sBezeichnung = getArtikelBezeichnung(ap.getFlrartikel().getI_id());
				}
				oDto.setSBezeichnung(sBezeichnung);
				alDaten.add(oDto);
			}

			Set hmLieferscheine = getAuftragReportFac().getAlleLieferscheineEinesAuftrags(auftragIId);

			Iterator itLs = hmLieferscheine.iterator();
			while (itLs.hasNext()) {
				Integer lieferscheinIId = (Integer) itLs.next();

				LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinIId);

				if (!lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_STORNIERT)) {
					Collection<LieferscheinpositionDto> cl = getLieferscheinpositionFac()
							.lieferscheinpositionFindByLieferscheinIId(lieferscheinIId, theClientDto);
					Iterator itLsPos = cl.iterator();

					while (itLsPos.hasNext()) {
						LieferscheinpositionDto lsposDto = (LieferscheinpositionDto) itLsPos.next();
						// Nur mehr nicht zugeordnete
						if (lsposDto.getAuftragpositionIId() == null && lsposDto.getArtikelIId() != null) {

							SichtLieferstatusDto oDto = new SichtLieferstatusDto();
							oDto.setSBelegart(belegartKurzLS);
							oDto.setSBelegnummer(lsDto.getCNr());

							if (lsDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
								oDto.setBErledigt(true);
							}
							oDto.setIiPosition(lsposDto.getIId());
							oDto.setNMengeGesamt(lsposDto.getNMenge());
							oDto.setNMengeGeliefert(lsposDto.getNMenge());

							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(lsposDto.getArtikelIId(),
									theClientDto);
							oDto.setSIdent(aDto.getCNr());
							oDto.setSReferenznummer(aDto.getCReferenznr());

							String sBezeichnung = null;

							if (lsposDto.getCBez() != null) {
								sBezeichnung = lsposDto.getCBez();
							} else {
								sBezeichnung = getArtikelBezeichnung(lsposDto.getArtikelIId());
							}
							oDto.setSBezeichnung(sBezeichnung);
							alDaten.add(oDto);

						}
					}
				}
			}

			// RE-Pos hinzufuegen
			Set hmRechnungen = getAuftragReportFac().getAlleRechnungenEinesAuftrags(auftragIId);

			Iterator itRe = hmRechnungen.iterator();
			while (itRe.hasNext()) {
				Integer rechnungIId = (Integer) itRe.next();

				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);

				if (!reDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

					RechnungPositionDto[] reposDtos = getRechnungFac().rechnungPositionFindByRechnungIId(rechnungIId);

					for (int i = 0; i < reposDtos.length; i++) {

						RechnungPositionDto reposDto = reposDtos[i];

						if (reposDto.getAuftragpositionIId() == null && reposDto.getArtikelIId() != null) {

							SichtLieferstatusDto oDto = new SichtLieferstatusDto();
							oDto.setSBelegart(belegartKurzRE);
							oDto.setSBelegnummer(reDto.getCNr());

							if (reDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
								oDto.setBErledigt(true);
							}
							oDto.setIiPosition(reposDto.getIId());
							oDto.setNMengeGesamt(reposDto.getNMenge());
							oDto.setNMengeGeliefert(reposDto.getNMenge());

							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(reposDto.getArtikelIId(),
									theClientDto);
							oDto.setSIdent(aDto.getCNr());
							oDto.setSReferenznummer(aDto.getCReferenznr());

							String sBezeichnung = null;

							if (reposDto.getCBez() != null) {
								sBezeichnung = reposDto.getCBez();
							} else {
								sBezeichnung = getArtikelBezeichnung(reposDto.getArtikelIId());
							}
							oDto.setSBezeichnung(sBezeichnung);
							alDaten.add(oDto);

						}
					}
				}
			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		int iAnzahlZeilen = alDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);

		return alDaten;
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
		try {

			alDaten = new ArrayList<SichtLieferstatusDto>();
			getFilterKriterien();

			setInhalt();
		} catch (Throwable t) {
			if (t.getCause() instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t.getCause();
			} else {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien SortierKriterium[]
	 * @param selectedId       Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		int rowNumber = 0; // selektiert ist immer die erste zeile
		QueryResult result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {
			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);

			columns.add("id", String.class, " ", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");
			columns.add("belegart", String.class, " ", QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);
			columns.add("lp.nr", Integer.class, getTextRespectUISpr("lp.nr", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);
			columns.add("lp.belegnummer", String.class, getTextRespectUISpr("lp.belegnummer", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			columns.add("lp.artikelnummer", String.class, getTextRespectUISpr("lp.artikelnummer", mandant, locUi),
					getUIBreiteIdent(), Facade.NICHT_SORTIERBAR);

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM, Facade.NICHT_SORTIERBAR);
			}

			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
			columns.add("lp.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.menge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("auft.geliefert", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("auft.geliefert", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.offen", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.offen", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("auft.erledigt", Boolean.class, getTextRespectUISpr("auft.erledigt", mandant, locUi),

					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("auft.erledigt.tooltip", mandantCNr, theClientDto.getLocUi()));

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

	}

	public TableInfo getTableInfo() {

		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_REFERENZNUMMER_IN_POSITIONEN);
			bReferenznummerInPositionen = ((Boolean) parametermandantDto.getCWertAsObject());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	/**
	 * In einer ArrayList von SichtLieferstatusDtos soll festgestellt werden, ob ein
	 * bsteimmtes SichtLieferstatusDto enthalten ist.
	 * 
	 * @param alListeI     ArrayList Liste von SichtLieferstatusDtos
	 * @param sBelegartI   die gesuchte Belegart
	 * @param iIdPositionI die gesuchte Position innerhalb dieser Belegart
	 * @return SichtLieferstatusDto null, wenn die gesuchte Position nicht enthalten
	 *         ist
	 * @throws java.lang.Throwable Ausnahme
	 */
	private SichtLieferstatusDto containsSichtLieferstatusDto(ArrayList<SichtLieferstatusDto> alListeI,
			String sBelegartI, Integer iIdPositionI) throws Throwable {
		SichtLieferstatusDto oDtoO = null;

		for (int i = 0; i < alListeI.size(); i++) {
			SichtLieferstatusDto oCurrentDto = (SichtLieferstatusDto) alListeI.get(i);

			if (oCurrentDto.getSBelegart().equals(sBelegartI)) {
				if (oCurrentDto.getIiPosition().equals(iIdPositionI)) {
					oDtoO = oCurrentDto;
				}
			}
		}

		return oDtoO;
	}

	private SichtLieferstatusDto getErstesSichtLieferstatusDtoMitBelegartkurzbezeichnung(ArrayList alListeI,
			String cBelegartkurzbezeichnung) throws Throwable {
		SichtLieferstatusDto sichtlieferstatusDto = null;

		boolean bGefunden = false;
		int iIndex = 0;

		if (alListeI != null && alListeI.size() > 0) {
			while (!bGefunden && iIndex < alListeI.size()) {
				SichtLieferstatusDto sichtlieferstatusDtoTemp = (SichtLieferstatusDto) alListeI.get(iIndex);

				iIndex++;

				if (sichtlieferstatusDtoTemp.getSBelegart().equals(cBelegartkurzbezeichnung)) {
					sichtlieferstatusDto = sichtlieferstatusDtoTemp;

					bGefunden = true;
				}
			}
		}

		return sichtlieferstatusDto;
	}
}
