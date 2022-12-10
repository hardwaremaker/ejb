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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFavoritenDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.projekt.service.ProjekttaetigkeitDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Handler fuer Loszeiten <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 15.04.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class ZeiterfassungFavoritenHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Object[]> hmDaten = null;

	private HashMap<String, String> hmDatenBereitsVerwendet = null;

	/**
	 * Konstruktor.
	 */
	public ZeiterfassungFavoritenHandler() {
		super();
		setAnzahlSpalten(6);
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
							String.class, String.class },
					// die Spaltenueberschriften werden durch die Kriterien
					// bestimmt
					new String[] { " ", getTextRespectUISpr("lp.partner", mandantCNr, locUI),
							getTextRespectUISpr("auft.projektbestellnummer", mandantCNr, locUI),
							
							getTextRespectUISpr("lp.belegart", mandantCNr, locUI),
							getTextRespectUISpr("lp.belegnummer", mandantCNr, locUI),
							getTextRespectUISpr("lp.taetigkeit", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
							getTextRespectUISpr("lp.bemerkung", mandantCNr, locUI) },
					// die Breite der Spalten festlegen
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden

							QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_M }, // hidden
					new String[] { "", "", "",
							"", "", "",
							"", "", });
		}

		return tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex Zeilenindex
	 * @return QueryResult Ergebnis
	 * @throws EJBExceptionLP Ausnahme
	 */
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
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
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

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	/**
	 * Diese Methode setzt die Anzahl der Zeilen in der Tabelle und den Inhalt.
	 * 
	 * @throws RemoteException Ausnahme
	 */

	public void setInhalt() throws RemoteException {
		hmDaten = new ArrayList<Object[]>();
		hmDatenBereitsVerwendet = new LinkedHashMap();

		ParametermandantDto parameterint = (ParametermandantDto) getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_PROJEKT_MIT_TAETIGKEIT);

		boolean bProjektMitTaetigkeit = ((Boolean) parameterint.getCWertAsObject());

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();
		Integer personalIId = new Integer(aFilterKriterium[0].value);

		// Alle Angebote holen, die ein Projekt hinterlegt haben
		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session.createQuery("SELECT z FROM FLRZeitdaten z WHERE z.personal_i_id=" + personalIId
				+ " AND z.i_belegartid!=null  ORDER BY z.t_zeit DESC");
		query.setMaxResults(50);
		List<?> resultListAG = query.list();
		Iterator<?> resultListIteratorAG = resultListAG.iterator();

		int iSpaltePartner = 1;
		int iSpalteProjekt = 2;

		int iSpalteBelegart = 3;
		int iSpalteBelegnummer = 4;
		int iSpalteTaetigkeit = 5;
		int iSpalteTaetigkeitBezeichnung = 6;
		int iSpalteBemerkung = 7;
		int iAnzahlSpalten = 8;

		while (resultListIteratorAG.hasNext()) {
			FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultListIteratorAG.next();

			String key = null;
			if (bProjektMitTaetigkeit) {
				key = flrZeitdaten.getC_belegartnr() + flrZeitdaten.getI_belegartid() + ""
						+ flrZeitdaten.getArtikel_i_id();
			} else {
				key = flrZeitdaten.getC_belegartnr() + flrZeitdaten.getI_belegartid();
			}

			if (!hmDatenBereitsVerwendet.containsKey(key)) {

				Object[] oZeile = new Object[iAnzahlSpalten];

				oZeile[iSpalteBemerkung] = flrZeitdaten.getC_bemerkungzubelegart();
				ZeiterfassungFavoritenDto zfDto = new ZeiterfassungFavoritenDto(flrZeitdaten.getI_id(), null);

				if (flrZeitdaten.getFlrartikel() != null) {
					oZeile[iSpalteTaetigkeit] = flrZeitdaten.getFlrartikel().getC_nr();

					oZeile[iSpalteTaetigkeitBezeichnung] = getArtikelFac()
							.artikelFindByPrimaryKeySmall(flrZeitdaten.getFlrartikel().getI_id(), theClientDto)
							.formatBezeichnung();

				}

				oZeile[iSpalteBelegart] = flrZeitdaten.getC_belegartnr();

				if (flrZeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {

					com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey(flrZeitdaten.getI_belegartid());

					oZeile[iSpalteBelegnummer] = auftragDto.getCNr();
					if (auftragDto.getCBezProjektbezeichnung() != null) {
						oZeile[iSpalteProjekt] = auftragDto.getCBezProjektbezeichnung();
					}

					oZeile[iSpaltePartner] = getKundeFac()
							.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
							.getPartnerDto().formatFixName1Name2();

				} else if (flrZeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					try {
						com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
								.losFindByPrimaryKey(flrZeitdaten.getI_belegartid());

						oZeile[iSpalteBelegnummer] = losDto.getCNr();
						if (losDto.getCProjekt() != null) {
							oZeile[iSpalteProjekt] = losDto.getCProjekt();
						}
						if (losDto.getKundeIId() != null) {
							oZeile[iSpaltePartner] = getKundeFac()
									.kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto).getPartnerDto()
									.formatFixName1Name2();
						} else {
							if (losDto.getAuftragIId() != null) {
								com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
										.auftragFindByPrimaryKey(losDto.getAuftragIId());
								oZeile[iSpaltePartner] = getKundeFac()
										.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
										.getPartnerDto().formatFixName1Name2();
								oZeile[iSpalteProjekt] = auftragDto.getCBezProjektbezeichnung();
							}
						}
					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
				} else if (flrZeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {

					com.lp.server.projekt.service.ProjektDto projektDto = null;
					try {
						projektDto = getProjektFac().projektFindByPrimaryKey(flrZeitdaten.getI_belegartid());

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					oZeile[iSpalteBelegnummer] = projektDto.getCNr();
					if (projektDto.getCTitel() != null) {
						oZeile[iSpalteProjekt] = projektDto.getCTitel();
					}

					oZeile[iSpaltePartner] = getPartnerFac()
							.partnerFindByPrimaryKey(projektDto.getPartnerIId(), theClientDto).formatFixName1Name2();

				} else if (flrZeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_ANGEBOT)) {
					try {
						com.lp.server.angebot.service.AngebotDto angebot = getAngebotFac()
								.angebotFindByPrimaryKey(flrZeitdaten.getI_belegartid(), theClientDto);

						oZeile[iSpalteBelegnummer] = angebot.getCNr();
						if (angebot.getCBez() != null) {
							oZeile[iSpalteProjekt] = angebot.getCBez();
						}

						oZeile[iSpaltePartner] = getKundeFac()
								.kundeFindByPrimaryKey(angebot.getKundeIIdAngebotsadresse(), theClientDto)
								.getPartnerDto().formatFixName1Name2();

					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
				}

				// Belegspezifisch

				oZeile[0] = zfDto;

				hmDaten.add(oZeile);
			}

			hmDatenBereitsVerwendet.put(key, "");

		}

		session.close();

		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);

	}
}
