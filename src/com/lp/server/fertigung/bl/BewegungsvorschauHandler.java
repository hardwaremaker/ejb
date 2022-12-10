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
package com.lp.server.fertigung.bl;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer die Bewegungsvorschau. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-20
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class BewegungsvorschauHandler extends UseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 500;
	private int iAnzahlSpalten = 9;

	boolean bZentralerArtikelstamm = false;

	boolean bAuftragsfreigabe = false;

	boolean bZugehoerigerKundeStattPartnerBeiLos = false;

	/**
	 * Konstruktor.
	 */
	public BewegungsvorschauHandler() {
		super();

		if (bZentralerArtikelstamm == true) {
			iAnzahlSpalten = 10;
		}

		setAnzahlZeilen(iAnzahlZeilen);
		setAnzahlSpalten(iAnzahlSpalten);
	}

	/**
	 * Jede ableitende Klasse muss in dieser Methode die Variable tableInfo
	 * befuellen.
	 * 
	 * @throws EJBExceptionLP
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() throws EJBExceptionLP {
		if (tableInfo == null) {

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			bZentralerArtikelstamm = getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
							theClientDto);

			try {
				ParametermandantDto param = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
				bAuftragsfreigabe = (Boolean) param.getCWertAsObject();

				param = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_ZUGEHOERIGER_KUNDE_IN_BEWEGUNGSVORSCHAU_ANZEIGEN);
				bZugehoerigerKundeStattPartnerBeiLos = (Boolean) param
						.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (bZentralerArtikelstamm == false) {

				tableInfo = new TableInfo(new Class[] { String.class,
						String.class, String.class, String.class, Date.class,
						Date.class, BigDecimal.class, BigDecimal.class,
						Color.class },
						new String[] {
								" ",
								getTextRespectUISpr("lp.belegart", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.belegnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.partner", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.abliefertermin",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.termin", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr(
										"lp.theoretischerlagerstand",
										mandantCNr, locUI), "" }, new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M, 1 },
						new String[] { "", "", "", "", "", "", "", "", "" });
			} else {

				setAnzahlSpalten(getAnzahlSpalten() + 1);

				tableInfo = new TableInfo(new Class[] { String.class,
						String.class, String.class, String.class, String.class,
						Date.class, Date.class, BigDecimal.class,
						BigDecimal.class, Color.class },
						new String[] {
								" ",
								getTextRespectUISpr("lp.belegart", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.belegnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("report.mandant",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.partner", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.abliefertermin",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.termin", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr(
										"lp.theoretischerlagerstand",
										mandantCNr, locUI), "" }, new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M, 1 },
						new String[] { "", "", "", "", "", "", "", "", "", "" });
			}

		}
		return this.tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;

		try {

			getFilterKriterien();

			// enthaelt KRIT_ARTIKEL_ID
			Integer iArtikelId = null;
			boolean bInternebestellungBeruecksichtigen = false;
			boolean bBestellvorschlagBeruecksichtigen = false;
			boolean bInterneBestellung = false;
			boolean bMitRahmen = false;

			Integer partnerIIdStandort = null;

			for (int i = 0; i < aFilterKriterium.length; i++) {
				if (aFilterKriterium[i].kritName
						.equals(InternebestellungFac.KRIT_ARTIKEL_I_ID)) {
					iArtikelId = new Integer(aFilterKriterium[i].value);
				} else if (aFilterKriterium[i].kritName
						.equals(InternebestellungFac.KRIT_INTERNEBESTELLUNG_BERUECKSICHTIGEN)) {
					bInternebestellungBeruecksichtigen = new Boolean(
							aFilterKriterium[i].value).booleanValue();
				} else if (aFilterKriterium[i].kritName
						.equals(InternebestellungFac.KRIT_PARTNER_I_ID_STANDORT)) {
					partnerIIdStandort = new Integer(aFilterKriterium[i].value);

				} else if (aFilterKriterium[i].kritName
						.equals(InternebestellungFac.KRIT_INTERNEBESTELLUNG)) {
					bInterneBestellung = new Boolean(aFilterKriterium[i].value)
							.booleanValue();
				} else if (aFilterKriterium[i].kritName
						.equals(InternebestellungFac.KRIT_MIT_RAHMEN)) {
					bMitRahmen = new Boolean(aFilterKriterium[i].value)
							.booleanValue();
				}
			}

			// SP4960
			if (bInterneBestellung == false) {
				bBestellvorschlagBeruecksichtigen = true;
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			// wenn kein Filter dann Tabelle leer

			Collection<?> cBewegungvorschau = getInternebestellungFac()
					.getBewegungsvorschauSortiert(iArtikelId,
							bInternebestellungBeruecksichtigen,
							bBestellvorschlagBeruecksichtigen, theClientDto,
							partnerIIdStandort, bMitRahmen, true, true, null);
			Iterator<?> it = cBewegungvorschau.iterator();

			BigDecimal bdLagerstand = new BigDecimal(0);/*
														 * getLagerFac() .
														 * getLagerstandAllerLagerEinesMandanten
														 * (iArtikelId,
														 * theClientDto);
														 */
			LagerDto[] lagerDto = getLagerFac().lagerFindByMandantCNr(
					theClientDto.getMandant());
			for (int i = 0; i < lagerDto.length; i++) {
				if (bInterneBestellung) {
					if (Helper.short2boolean(lagerDto[i]
							.getBInternebestellung())) {
						bdLagerstand = bdLagerstand.add(getLagerFac()
								.getLagerstand(iArtikelId,
										lagerDto[i].getIId(), theClientDto));
					}
				} else {
					if (Helper
							.short2boolean(lagerDto[i].getBBestellvorschlag())) {
						bdLagerstand = bdLagerstand.add(getLagerFac()
								.getLagerstand(iArtikelId,
										lagerDto[i].getIId(), theClientDto));
					}
				}
			}

			Object[][] rows = new Object[cBewegungvorschau.size()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;
			while (it.hasNext()) {

				BewegungsvorschauDto oBewegungsvorschauDto = (BewegungsvorschauDto) it
						.next();

				if (bAuftragsfreigabe == true) {

					if (oBewegungsvorschauDto.getCBelegartCNr() != null
							&& oBewegungsvorschauDto.getCBelegartCNr().equals(
									LocaleFac.BELEGART_AUFTRAG)
							&& oBewegungsvorschauDto
									.isbKommtAusInternerBstellung() == false && oBewegungsvorschauDto
									.isBKommtAusBestellvorschlag() == false) {

						if (oBewegungsvorschauDto.getTAuftragsfreigabe() != null) {
							bdLagerstand = bdLagerstand
									.add(oBewegungsvorschauDto.getNMenge());
						}

					} else {
						bdLagerstand = bdLagerstand.add(oBewegungsvorschauDto
								.getNMenge());
					}
				} else {
					bdLagerstand = bdLagerstand.add(oBewegungsvorschauDto
							.getNMenge());
				}

				Color c = null;

				rows[row][col++] = null; // IId bleibt leer

				if (oBewegungsvorschauDto.isbKommtAusInternerBstellung()) {
					rows[row][col++] = "Interne Bestellung";
				} else if (oBewegungsvorschauDto.isBKommtAusBestellvorschlag()) {
					rows[row][col++] = "Bestellvorschlag";
				} else {
					if (oBewegungsvorschauDto.getCBelegartCNr() != null) {
						rows[row][col++] = oBewegungsvorschauDto
								.getCBelegartCNr();

						if (bAuftragsfreigabe == true
								&& oBewegungsvorschauDto.getCBelegartCNr()
										.equals(LocaleFac.BELEGART_AUFTRAG)
								&& oBewegungsvorschauDto.getTAuftragsfreigabe() == null) {
							c = Color.LIGHT_GRAY;
						}

					} else {
						rows[row][col++] = "Interne Bestellung";
					}
				}

				rows[row][col++] = oBewegungsvorschauDto.getCBelegnummer();

				if (bZentralerArtikelstamm == true) {
					rows[row][col++] = oBewegungsvorschauDto.getMandantCNr();
				}

				if (bZugehoerigerKundeStattPartnerBeiLos
						&& oBewegungsvorschauDto.getCBelegartCNr() != null
						&& (oBewegungsvorschauDto.getCBelegartCNr().equals(
								LocaleFac.BELEGART_LOS) || oBewegungsvorschauDto
								.getCBelegartCNr().equals(
										LocaleFac.BELEGART_LOSABLIEFERUNG))) {
					if (oBewegungsvorschauDto.getKundeDto() != null) {
						rows[row][col++] = oBewegungsvorschauDto.getKundeDto()
								.getPartnerDto().formatFixTitelName1Name2();

					} else {
						rows[row][col++] = null;
					}
				} else {

					// PartnerDto (wenn vorhanden) richtig formatieren
					if (oBewegungsvorschauDto.getPartnerDto() != null) {
						rows[row][col++] = oBewegungsvorschauDto
								.getPartnerDto().formatFixTitelName1Name2();
					} else {
						rows[row][col++] = null;
					}
				}

				rows[row][col++] = oBewegungsvorschauDto.getTAbliefertermin();

				if (oBewegungsvorschauDto.getTABTerminBestellung() != null) {
					rows[row][col++] = oBewegungsvorschauDto
							.getTABTerminBestellung();
				} else {
					rows[row][col++] = oBewegungsvorschauDto.getTLiefertermin();
				}

				rows[row][col++] = oBewegungsvorschauDto.getNMenge();
				rows[row][col++] = bdLagerstand;
				rows[row][col++] = c;
				row++;
				col = 0;
			}
			result = new QueryResult(rows, row, startIndex, endIndex, 0);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return result;
	}
}

// //Los
// if (oBewegungsvorschauDto.getCBelegartCNr().equals(LocaleFac.BELEGART_LOS)) {
// LossollmaterialDto losmatDto = getFertigungFac().
// lossollmaterialFindByPrimaryKey(oBewegungsvorschauDto.getIBelegartId());
// LosDto losDto = getFertigungFac().losFindByPrimaryKey(losmatDto.getLosIId());
// rows[row][col++] = losDto.getCNr();
// if (losDto.getAuftragpositionIId() != null) {
// AuftragpositionDto aPosDto = getAuftragpositionFac().
// auftragpositionFindByPrimarayKeyOhneExc(losDto.getAuftragpositionIId());
// AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(
// aPosDto.getAuftragIId());
// KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(auftragDto.
// getKundeIIdAuftragsadresse(), sCurrentUser);
// rows[row][col++] = kundeDto.getPartnerDto().formatFixName2Name1();
// }
// else {
// rows[row][col++] = null;
// }
// }
// else if (oBewegungsvorschauDto.getCBelegartCNr().equals(LocaleFac.
// BELEGART_LOSABLIEFERUNG)) {
// LosDto losDto = getFertigungFac().losFindByPrimaryKey(oBewegungsvorschauDto.
// getIBelegartId());
// rows[row][col++] = losDto.getCNr();
// if (losDto.getAuftragpositionIId() != null) {
// AuftragpositionDto aPosDto = getAuftragpositionFac().
// auftragpositionFindByPrimarayKeyOhneExc(losDto.getAuftragpositionIId());
// AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(
// aPosDto.getAuftragIId());
// KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(auftragDto.
// getKundeIIdAuftragsadresse(), sCurrentUser);
// rows[row][col++] = kundeDto.getPartnerDto().formatFixName2Name1();
// }
// else {
// rows[row][col++] = null;
// }
// }
// else if (oBewegungsvorschauDto.getCBelegartCNr().equals(InternebestellungFac.
// BEWEGUNGSVORSCHAU_FEHLMENGE)) {
// LossollmaterialDto losmatDto = getFertigungFac().
// lossollmaterialFindByPrimaryKey(oBewegungsvorschauDto.getIBelegartId());
// LosDto losDto = getFertigungFac().losFindByPrimaryKey(losmatDto.getLosIId());
// rows[row][col++] = losDto.getCNr();
// rows[row][col++] = ""; // kein Lieferant
// }
// else if (oBewegungsvorschauDto.getCBelegartCNr().equals(InternebestellungFac.
// BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG)) {
// InternebestellungDto ibDto = getInternebestellungFac().
// internebestellungFindByPrimaryKey(oBewegungsvorschauDto.getIBelegartId());
// rows[row][col++] = "";
// rows[row][col++] = ""; // kein Lieferant
// }
// else if (oBewegungsvorschauDto.getCBelegartCNr().equals(LocaleFac.
// BELEGART_AUFTRAG)) {
//
// AuftragDto auftragDto = null;
// AuftragpositionDto auftragposDto = null;
//
// auftragposDto = this.getAuftragpositionFac().
// auftragpositionFindByPrimarayKeyOhneExc(oBewegungsvorschauDto.
// getIBelegartId());
// if (auftragposDto != null) {
// auftragDto = this.getAuftragFac().auftragFindByPrimaryKeyOhneExc(
// auftragposDto.getAuftragIId());
// }
// if (auftragDto != null) {
// rows[row][col++] = auftragDto.getCNr();
// KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
// auftragDto.getKundeIIdAuftragsadresse(), sCurrentUser);
// rows[row][col++] = kundeDto.getPartnerDto().formatFixName2Name1();
// }
// else {
// rows[row][col++] = null;
// rows[row][col++] = null;
// }
// }
// else if (oBewegungsvorschauDto.getCBelegartCNr().equals(LocaleFac.
// BELEGART_BESTELLUNG)) {
// BestellungDto bestellungDto = null;
// BestellpositionDto bestellpositionDto = null;
// bestellpositionDto = this.getBestellpositionFac().
// bestellpositionFindByPrimaryKey(
// oBewegungsvorschauDto.
// getIBelegartId());
//
// if (bestellpositionDto != null) {
// bestellungDto = this.getBestellungFac().bestellungFindByPrimaryKeyOhneExc(
// bestellpositionDto.getBestellungIId());
// }
// if (bestellungDto != null) {
// rows[row][col++] = bestellungDto.getCNr();
// LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
// bestellungDto.getLieferantIIdBestelladresse(), sCurrentUser);
// rows[row][col++] = lieferantDto.getPartnerDto().formatFixName2Name1();
// }
// else {
// rows[row][col++] = null;
// rows[row][col++] = null;
// }
// }
