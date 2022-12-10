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
package com.lp.server.projekt.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungAuftragszuordnung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosAuftrag;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.reklamation.fastlanereader.generated.FLRReklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
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

public class ProjektverlaufHandler extends UseCaseHandlerTabelle {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Object[]> hmDaten = null;

	private LinkedHashMap<String, ProjektVerlaufHelperDto> hmDatenBereitsVerwendet = null;
	private int SPALTE_PROJEKTVERLAUF_HELPER = 0;
	private int SPALTE_TYP0 = 1;
	private int SPALTE_TYP1 = 2;
	private int SPALTE_TYP2 = 3;
	private int SPALTE_TYP3 = 4;
	private int SPALTE_TYP4 = 5;

	private int SPALTE_DATUM = 6;
	private int SPALTE_VERTRETER = 7;
	private int SPALTE_STATUS = 8;
	private int SPALTE_NETTOWERT = 9;
	private int SPALTE_WAEHRUNG = 10;
	private int ANZAHL_SPALTEN = 11;

	private Boolean bSchlussrechnung = Boolean.FALSE;

	/**
	 * Konstruktor.
	 */
	public ProjektverlaufHandler() {
		super();
		setAnzahlSpalten(9);
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(
					new Class[] { Object.class, String.class, String.class, String.class, String.class, String.class,
							java.util.Date.class, String.class, Icon.class, BigDecimal.class, String.class },
					// die Spaltenueberschriften werden durch die Kriterien
					// bestimmt
					new String[] { " ", getTextRespectUISpr("proj.verlauf.ebene", mandantCNr, locUI) + "1",
							getTextRespectUISpr("proj.verlauf.ebene", mandantCNr, locUI) + "2",
							getTextRespectUISpr("proj.verlauf.ebene", mandantCNr, locUI) + "3",
							getTextRespectUISpr("proj.verlauf.ebene", mandantCNr, locUI) + "4",
							getTextRespectUISpr("proj.verlauf.ebene", mandantCNr, locUI) + "5",
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.vertreter", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI),
							getTextRespectUISpr("lp.whg", mandantCNr, locUI) },
					// die Breite der Spalten festlegen
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden

							QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_XS, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M }, // hidden
					new String[] { "", "", "", "", "", "", "", "", "", "", "" });
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

				if (hmDaten.get(row)[SPALTE_STATUS] != null
						&& hmDaten.get(row)[SPALTE_STATUS].equals(LocaleFac.STATUS_STORNIERT)) {
					hmDaten.get(row)[SPALTE_NETTOWERT] = new BigDecimal(0);
				}

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
	 * @param bFillData false, wenn der Inhalt nicht befuellt werden soll
	 * @throws Throwable Ausnahme
	 */

	private void befuelleMitRechnungDto(RechnungDto rDto, String auftrag, String typ1, String typ2, String typ3) {
		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[0] = "";

		oZeile[SPALTE_TYP0] = auftrag;
		if (typ1 != null) {

			if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
				typ1 = "PROFORMA" + rDto.getCNr();
			} else if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
				typ1 = "ANZAHLUNG" + rDto.getCNr();
			} else if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				typ1 = "SCHLUSSRE" + rDto.getCNr();
			}
		} else if (typ2 != null) {

			if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
				typ2 = "PROFORMA" + rDto.getCNr();
			} else if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
				typ2 = "ANZAHLUNG" + rDto.getCNr();
			} else if (rDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				typ2 = "SCHLUSSRE" + rDto.getCNr();
			}
		}

		oZeile[SPALTE_TYP1] = typ1;
		oZeile[SPALTE_TYP2] = typ2;
		oZeile[SPALTE_TYP3] = typ3;

		oZeile[SPALTE_DATUM] = rDto.getTBelegdatum();

		if (typ1 != null && typ1.equals("GS") || typ2 != null && typ2.equals("GS")
				|| typ3 != null && typ3.equals("GS")) {
			if (rDto.getNGesamtwertinbelegwaehrung() != null) {
				oZeile[SPALTE_NETTOWERT] = rDto.getNGesamtwertinbelegwaehrung().negate();
			}
		} else if (typ1 != null && typ1.equals("SCHLUSSRE") || typ2 != null && typ2.equals("SCHLUSSRE")
				|| typ3 != null && typ3.equals("SCHLUSSRE")) {

			BigDecimal bdAnzahlungen = getRechnungFac().getAnzahlungenZuSchlussrechnungFw(rDto.getIId());

			oZeile[SPALTE_NETTOWERT] = rDto.getNGesamtwertinbelegwaehrung().subtract(bdAnzahlungen);
		} else {
			oZeile[SPALTE_NETTOWERT] = rDto.getNGesamtwertinbelegwaehrung();
		}

		oZeile[SPALTE_STATUS] = rDto.getStatusCNr();
		if (rDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(rDto.getPersonalIIdVertreter(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		oZeile[SPALTE_WAEHRUNG] = rDto.getWaehrungCNr();

		hmDaten.add(oZeile);

		// Gutschriften
		RechnungDto[] gsDtos = getRechnungFac().rechnungFindByRechnungIIdZuRechnung(rDto.getIId());
		for (int i = 0; i < gsDtos.length; i++) {

			if (typ1 != null) {
				befuelleMitRechnungDto(gsDtos[i], auftrag, null, "GS" + gsDtos[i].getCNr(), null);
			} else {
				befuelleMitRechnungDto(gsDtos[i], auftrag, null, null, "GS" + gsDtos[i].getCNr());
			}
		}

	}

	private void setzeLosInEbene(FLRLosAuftrag flrlos, int iEbene) throws RemoteException {

		String los = "LO" + flrlos.getC_nr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = los;

		oZeile[SPALTE_DATUM] = flrlos.getT_produktionsbeginn();

		LosDto lDto = getFertigungFac().losFindByPrimaryKey(flrlos.getI_id());

		if (lDto.getPersonalIIdTechniker() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(lDto.getPersonalIIdTechniker(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, lDto);

		if (flrlos.getFlrauftrag() != null) {
			pvDto.setPartnerIId(flrlos.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id());
		} else if (flrlos.getFlrauftragposition() != null) {
			pvDto.setPartnerIId(flrlos.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner().getI_id());
		}

		pvDto.setBelegpositionenDtos(getFertigungFac().lossollmaterialFindByLosIIdOrderByISort(flrlos.getI_id()));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(los)) {
			oZeile[SPALTE_STATUS] = flrlos.getStatus_c_nr();

			hmDatenBereitsVerwendet.put(los, pvDto);
		}

		hmDaten.add(oZeile);

		// Reklamationen einfuegen
		iEbene++;
		int iEbeneReklamation = iEbene;
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(
				"SELECT rk FROM FLRReklamation rk WHERE  rk.los_i_id=" + lDto.getIId() + " ORDER BY rk.c_nr ASC");

		List<?> resultListRK = query.list();
		Iterator resultListIteratorRK = resultListRK.iterator();
		while (resultListIteratorRK.hasNext()) {
			FLRReklamation flrreklamation = (FLRReklamation) resultListIteratorRK.next();

			setzeReklamationInEbene(flrreklamation, iEbeneReklamation);
		}

		session.close();

	}

	private void setzeBestellungInEbene(FLRBestellung flrBestellung, int iEbene) throws RemoteException {

		String los = "BS" + flrBestellung.getC_nr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = los;

		oZeile[SPALTE_DATUM] = flrBestellung.getT_belegdatum();

		BestellungDto bDto = getBestellungFac().bestellungFindByPrimaryKey(flrBestellung.getI_id());

		if (bDto.getPersonalIIdAnforderer() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(bDto.getPersonalIIdAnforderer(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();

			oZeile[SPALTE_NETTOWERT] = bDto.getNBestellwert();
			oZeile[SPALTE_WAEHRUNG] = bDto.getWaehrungCNr();

		}
		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, bDto);
		pvDto.setPartnerIId(flrBestellung.getFlrlieferant().getFlrpartner().getI_id());

		pvDto.setBelegpositionenDtos(
				getBestellpositionFac().bestellpositionFindByBestellung(flrBestellung.getI_id(), theClientDto));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(los)) {
			oZeile[SPALTE_STATUS] = flrBestellung.getBestellungstatus_c_nr();

			hmDatenBereitsVerwendet.put(los, pvDto);
		}

		hmDaten.add(oZeile);

		// Reklamationen einfuegen
		iEbene++;
		int iEbeneReklamation = iEbene;
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery("SELECT rk FROM FLRReklamation rk WHERE  rk.bestellung_i_id="
				+ flrBestellung.getI_id() + " ORDER BY rk.c_nr ASC");

		List<?> resultListRK = query.list();
		Iterator resultListIteratorRK = resultListRK.iterator();
		while (resultListIteratorRK.hasNext()) {
			FLRReklamation flrreklamation = (FLRReklamation) resultListIteratorRK.next();

			setzeReklamationInEbene(flrreklamation, iEbene);
		}

		session.close();

	}

	private void setzeReklamationInEbene(FLRReklamation flrReklamation, int iEbene) throws RemoteException {

		String reklamation = "RK" + flrReklamation.getC_nr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = reklamation;

		oZeile[SPALTE_DATUM] = flrReklamation.getT_belegdatum();

		ReklamationDto rDto = getReklamationFac().reklamationFindByPrimaryKey(flrReklamation.getI_id());

		BigDecimal bdKosten = BigDecimal.ZERO;

		if (rDto.getNKostenarbeitszeit() != null) {
			bdKosten = bdKosten.add(rDto.getNKostenarbeitszeit());
		}
		if (rDto.getNKostenmaterial() != null) {
			bdKosten = bdKosten.add(rDto.getNKostenmaterial());
		}

		oZeile[SPALTE_NETTOWERT] = bdKosten;
		oZeile[SPALTE_WAEHRUNG] = theClientDto.getSMandantenwaehrung();

		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, rDto);
		if (flrReklamation.getFlrkunde() != null) {
			pvDto.setPartnerIId(flrReklamation.getFlrkunde().getFlrpartner().getI_id());
		} else if (flrReklamation.getFlrlieferant() != null) {
			pvDto.setPartnerIId(flrReklamation.getFlrlieferant().getFlrpartner().getI_id());
		}
		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(reklamation)) {
			oZeile[SPALTE_STATUS] = flrReklamation.getStatus_c_nr();

			hmDatenBereitsVerwendet.put(reklamation, pvDto);
			hmDaten.add(oZeile);
		}

	}

	private void setzeAnfrageInEbene(AnfrageDto aDto, int iEbene) throws RemoteException {

		String los = "AF" + aDto.getCNr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = los;

		oZeile[SPALTE_DATUM] = aDto.getTBelegdatum();

		if (aDto.getPersonalIIdAnlegen() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(aDto.getPersonalIIdAnlegen(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, aDto);

		if (aDto.getLieferantIIdAnfrageadresse() != null) {

			pvDto.setPartnerIId(getLieferantFac()
					.lieferantFindByPrimaryKey(aDto.getLieferantIIdAnfrageadresse(), theClientDto).getPartnerIId());
		}

		pvDto.setBelegpositionenDtos(getAnfragepositionFac().anfragepositionFindByAnfrage(aDto.getIId(), theClientDto));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(los)) {
			oZeile[SPALTE_STATUS] = aDto.getStatusCNr();
			hmDatenBereitsVerwendet.put(los, pvDto);
			hmDaten.add(oZeile);
		}

	}

	private void setzeLieferscheinInEbene(LieferscheinDto lieferscheinDto, int iEbene) throws RemoteException {

		String ls = "LS" + lieferscheinDto.getCNr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = ls;

		oZeile[SPALTE_DATUM] = lieferscheinDto.getTBelegdatum();

		if (lieferscheinDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(lieferscheinDto.getPersonalIIdVertreter(),
					theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, lieferscheinDto);

		pvDto.setPartnerIId(
				getKundeFac().kundeFindByPrimaryKeySmall(lieferscheinDto.getKundeIIdLieferadresse()).getPartnerIId());

		pvDto.setBelegpositionenDtos(
				getLieferscheinpositionFac().lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId()));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(ls)) {
			oZeile[SPALTE_NETTOWERT] = lieferscheinDto.getNGesamtwertInLieferscheinwaehrung();
			oZeile[SPALTE_WAEHRUNG] = lieferscheinDto.getWaehrungCNr();
			oZeile[SPALTE_STATUS] = lieferscheinDto.getStatusCNr();

			hmDatenBereitsVerwendet.put(ls, pvDto);
			hmDaten.add(oZeile);
		}

		// Reklamationen einfuegen
		iEbene++;
		int iEbeneReklamation = iEbene;
		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session.createQuery("SELECT rk FROM FLRReklamation rk WHERE  rk.lieferschein_i_id="
				+ lieferscheinDto.getIId() + " ORDER BY rk.c_nr ASC");

		List<?> resultListRK = query.list();
		Iterator resultListIteratorRK = resultListRK.iterator();
		while (resultListIteratorRK.hasNext()) {
			FLRReklamation flrreklamation = (FLRReklamation) resultListIteratorRK.next();

			setzeReklamationInEbene(flrreklamation, iEbeneReklamation);
		}
		session.close();

		// Rechnungen zu Lieferschein

		HashMap<Integer, RechnungDto> rechnungen = new HashMap<Integer, RechnungDto>();

		RechnungDto[] rechnungDtos = getRechnungFac().rechnungFindByLieferscheinIId(lieferscheinDto.getIId());

		for (int i = 0; i < rechnungDtos.length; i++) {
			if (!rechnungen.containsKey(rechnungDtos[i].getIId())) {
				rechnungen.put(rechnungDtos[i].getIId(), rechnungDtos[i]);
				if (rechnungDtos[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					bSchlussrechnung = Boolean.TRUE;
				}

			}
		}

		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT re FROM FLRRechnungPosition re WHERE re.flrlieferschein.i_id=" + lieferscheinDto.getIId());

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			FLRRechnungPosition repos = (FLRRechnungPosition) resultListIterator.next();

			if (!rechnungen.containsKey(repos.getFlrrechnung().getI_id())) {

				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(repos.getFlrrechnung().getI_id());
				if (reDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					bSchlussrechnung = Boolean.TRUE;
				}

				rechnungen.put(repos.getFlrrechnung().getI_id(), reDto);
			}
		}

		// Lieferscheine einfuegen
		iEbene++;
		int iEbeneRechnung = iEbene;

		Iterator<Integer> re = rechnungen.keySet().iterator();
		while (re.hasNext()) {

			RechnungDto reDto = (RechnungDto) rechnungen.get(re.next());
			reDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
			setzeRechnungInEbene(reDto, iEbeneRechnung);
		}

		session.close();

	}

	private void setzeRechnungInEbene(RechnungDto rechnungDto, int iEbene) throws RemoteException {

		boolean bGutschrift = false;

		RechnungartDto raDto = getRechnungServiceFac().rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr(),
				theClientDto);

		if (raDto.getRechnungtypCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			bGutschrift = true;
		}

		String kuerzel = "RE";

		if (raDto.getCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
			kuerzel = "AZ";
		} else if (raDto.getCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
			kuerzel = "GS";
		} else if (raDto.getCNr().equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
			kuerzel = "PR";
		} else if (raDto.getCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			kuerzel = "SZ";
		} else if (raDto.getCNr().equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
			kuerzel = "WE";
		}

		String re = kuerzel + rechnungDto.getCNr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = re;

		oZeile[SPALTE_DATUM] = rechnungDto.getTBelegdatum();

		if (rechnungDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(rechnungDto.getPersonalIIdVertreter(),
					theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		ProjektVerlaufHelperDto pvReDto = new ProjektVerlaufHelperDto(iEbene, rechnungDto);

		pvReDto.setPartnerIId(getKundeFac().kundeFindByPrimaryKeySmall(rechnungDto.getKundeIId()).getPartnerIId());

		pvReDto.setBelegpositionenDtos(getRechnungFac().rechnungPositionFindByRechnungIId(rechnungDto.getIId()));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvReDto;
		if (!hmDatenBereitsVerwendet.containsKey(re)) {
			if (bGutschrift == true) {
				if (rechnungDto.getNGesamtwertinbelegwaehrung() != null) {
					oZeile[SPALTE_NETTOWERT] = rechnungDto.getNGesamtwertinbelegwaehrung().negate();
				}
			} else {
				oZeile[SPALTE_NETTOWERT] = rechnungDto.getNGesamtwertinbelegwaehrung();
			}

			oZeile[SPALTE_WAEHRUNG] = rechnungDto.getWaehrungCNr();
			oZeile[SPALTE_STATUS] = rechnungDto.getStatusCNr();

			ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, rechnungDto);
			pvDto.setPartnerIId(getKundeFac().kundeFindByPrimaryKeySmall(rechnungDto.getKundeIId()).getPartnerIId());
			pvDto.setBelegpositionenDtos(getRechnungFac().rechnungPositionFindByRechnungIId(rechnungDto.getIId()));

			hmDatenBereitsVerwendet.put(re, pvDto);
			hmDaten.add(oZeile);
		}

		// Gutschriften
		RechnungDto[] gsDtos = getRechnungFac().rechnungFindByRechnungIIdZuRechnung(rechnungDto.getIId());
		for (int i = 0; i < gsDtos.length; i++) {

			String gs = "GS" + gsDtos[i].getCNr();

			oZeile = new Object[ANZAHL_SPALTEN];
			oZeile[iEbene] = gs;

			oZeile[SPALTE_DATUM] = gsDtos[i].getTBelegdatum();

			if (gsDtos[i].getPersonalIIdVertreter() != null) {
				PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(gsDtos[i].getPersonalIIdVertreter(),
						theClientDto);
				oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
			}
			ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, gsDtos[i]);

			pvReDto.setBelegpositionenDtos(getRechnungFac().rechnungPositionFindByRechnungIId(gsDtos[i].getIId()));

			pvReDto.setPartnerIId(getKundeFac().kundeFindByPrimaryKeySmall(gsDtos[i].getKundeIId()).getPartnerIId());

			if (!hmDatenBereitsVerwendet.containsKey(gs)) {
				oZeile[SPALTE_NETTOWERT] = gsDtos[i].getNGesamtwertinbelegwaehrung();
				oZeile[SPALTE_WAEHRUNG] = gsDtos[i].getWaehrungCNr();
				oZeile[SPALTE_STATUS] = gsDtos[i].getStatusCNr();

				hmDatenBereitsVerwendet.put(gs, pvDto);
				oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
				hmDaten.add(oZeile);
			}

		}

		// Reklamationen einfuegen
		iEbene++;
		int iEbeneReklamation = iEbene;
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery("SELECT rk FROM FLRReklamation rk WHERE  rk.rechnung_i_id="
				+ rechnungDto.getIId() + " ORDER BY rk.c_nr ASC");

		List<?> resultListRK = query.list();
		Iterator resultListIteratorRK = resultListRK.iterator();
		while (resultListIteratorRK.hasNext()) {
			FLRReklamation flrreklamation = (FLRReklamation) resultListIteratorRK.next();

			setzeReklamationInEbene(flrreklamation, iEbeneReklamation);
		}

		session.close();

	}

	private void setzeReiseInEbene(ReiseDto reiseDto, BigDecimal bdKosten, int iEbene) {

		String auftrag = "REISE";

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = auftrag;

		oZeile[SPALTE_DATUM] = reiseDto.getTZeit();

		reiseDto.setNKostenDesAbschnitts(bdKosten);

		PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(reiseDto.getPersonalIId(), theClientDto);
		oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();

		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, reiseDto);
		pvDto.setPartnerIId(reiseDto.getPartnerIId());
		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(auftrag + reiseDto.getTZeit())) {

			oZeile[SPALTE_NETTOWERT] = bdKosten;
			oZeile[SPALTE_WAEHRUNG] = theClientDto.getSMandantenwaehrung();
			oZeile[SPALTE_STATUS] = null;

			hmDatenBereitsVerwendet.put(auftrag + reiseDto.getTZeit(), pvDto);
		}
		hmDaten.add(oZeile);
	}

	private void setzeAuftragInEbene(FLRAuftrag flrauftrag, int iEbene) throws RemoteException {

		String auftrag = "AB" + flrauftrag.getC_nr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = auftrag;

		oZeile[SPALTE_DATUM] = flrauftrag.getT_belegdatum();

		AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(flrauftrag.getI_id());
		if (aDto.getPersonalIIdVertreter() != null) {
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(aDto.getPersonalIIdVertreter(), theClientDto);
			oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
		}
		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, aDto);

		pvDto.setPartnerIId(flrauftrag.getFlrkunde().getFlrpartner().getI_id());
		pvDto.setBelegpositionenDtos(getAuftragpositionFac().auftragpositionFindByAuftrag(flrauftrag.getI_id()));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(auftrag)) {

			oZeile[SPALTE_NETTOWERT] = flrauftrag.getN_gesamtauftragswertinauftragswaehrung();
			oZeile[SPALTE_WAEHRUNG] = flrauftrag.getWaehrung_c_nr_auftragswaehrung();
			oZeile[SPALTE_STATUS] = flrauftrag.getAuftragstatus_c_nr();

			hmDatenBereitsVerwendet.put(auftrag, pvDto);
		}
		hmDaten.add(oZeile);
		
		pvDto.setTelefonzeitenZuBeleg(telefonzeitenHinzufuegen(null, null,  flrauftrag.getI_id()));
		
	}

	private void setzeAgstklInEbene(FLRAgstkl flragstkl, int iEbene) throws RemoteException {

		String agstkl = "AS" + flragstkl.getC_nr();

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = agstkl;

		oZeile[SPALTE_DATUM] = flragstkl.getT_belegdatum();

		AgstklDto aDto = getAngebotstklFac().agstklFindByPrimaryKey(flragstkl.getI_id());

		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene, aDto);
		pvDto.setPartnerIId(flragstkl.getFlrkunde().getFlrpartner().getI_id());

		pvDto.setBelegpositionenDtos(
				getAngebotstklpositionFac().agstklpositionFindByAgstklIId(flragstkl.getI_id(), theClientDto));

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
		if (!hmDatenBereitsVerwendet.containsKey(agstkl)) {

			oZeile[SPALTE_WAEHRUNG] = flragstkl.getWaehrung_c_nr();

			hmDatenBereitsVerwendet.put(agstkl, pvDto);
		}
		hmDaten.add(oZeile);
	}

	public LinkedHashMap<String, ProjektVerlaufHelperDto> setInhalt() throws RemoteException {
		hmDaten = new ArrayList<Object[]>();
		hmDatenBereitsVerwendet = new LinkedHashMap();
		bSchlussrechnung = Boolean.FALSE;

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();
		Integer projektIId = new Integer(aFilterKriterium[0].value);

		// Alle Angebote holen, die ein Projekt hinterlegt haben
		Session session = FLRSessionFactory.getFactory().openSession();
		Query query = session.createQuery("SELECT ag FROM FLRAngebot ag WHERE ag.flrprojekt.i_id=" + projektIId);

		List<?> resultListAG = query.list();
		Iterator<?> resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {

			FLRAngebot flrAngebot = (FLRAngebot) resultListIteratorAG.next();

			Object[] oZeile = new Object[ANZAHL_SPALTEN];
			oZeile[SPALTE_TYP0] = "AG" + flrAngebot.getC_nr();

			oZeile[SPALTE_DATUM] = flrAngebot.getT_belegdatum();
			oZeile[SPALTE_NETTOWERT] = flrAngebot.getN_gesamtangebotswertinangebotswaehrung();
			oZeile[SPALTE_WAEHRUNG] = flrAngebot.getWaehrung_c_nr_angebotswaehrung();
			oZeile[SPALTE_STATUS] = flrAngebot.getAngebotstatus_c_nr();

			if (flrAngebot.getFlrvertreter() != null) {
				PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(flrAngebot.getFlrvertreter().getI_id(),
						theClientDto);
				oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
			}

			hmDaten.add(oZeile);

			ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(SPALTE_TYP0,
					getAngebotFac().angebotFindByPrimaryKey(flrAngebot.getI_id(), theClientDto));
			pvDto.setBelegpositionenDtos(
					getAngebotpositionFac().angebotpositionFindByAngebotIId(flrAngebot.getI_id(), theClientDto));
			pvDto.setPartnerIId(flrAngebot.getFlrkunde().getFlrpartner().getI_id());

			hmDatenBereitsVerwendet.put("AG" + flrAngebot.getC_nr(), pvDto);

			oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;

			// alle Auftraege holen, welche auf dem Angebot haengen
			auftraegeHinzufuegen(projektIId, flrAngebot.getI_id(), true);

			pvDto.setTelefonzeitenZuBeleg(telefonzeitenHinzufuegen(null, flrAngebot.getI_id(), null));

		}

		session.close();

		// alle Auftraege holen, welche kein Angebot als Vorgaenger haben
		auftraegeHinzufuegen(projektIId, null, false);

		// Alle Los ohne Auftragsbezuh holen
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery("SELECT los FROM FLRLosAuftrag los WHERE los.projekt_i_id=" + projektIId);

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRLosAuftrag flrlos = (FLRLosAuftrag) resultListIteratorAG.next();
			setzeLosInEbene(flrlos, SPALTE_TYP0);
		}
		session.close();

		// Nun noch alle Lieferscheine holen, welche ein Projekt haben, jeodch
		// keinen Vorgaenger haben
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT ls FROM FLRLieferschein ls WHERE ls.auftrag_i_id is null AND ls.projekt_i_id=" + projektIId);

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRLieferschein flrLieferschein = (FLRLieferschein) resultListIteratorAG.next();
			LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(flrLieferschein.getI_id());
			setzeLieferscheinInEbene(lsDto, SPALTE_TYP0);
		}
		session.close();

		// und alle Rechnungen holen, welche ein Projekt haben, jeodch
		// keinen Vorgaenger haben
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT re FROM FLRRechnung re WHERE re.lieferschein_i_id is null AND re.projekt_i_id=" + projektIId);

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRRechnung flrrechnung = (FLRRechnung) resultListIteratorAG.next();
			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(flrrechnung.getI_id());
			reDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
			setzeRechnungInEbene(reDto, SPALTE_TYP0);
		}
		session.close();

		// alle agstkl holen, die dem PJ zugeordnet sind

		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT ag FROM FLRAgstkl ag WHERE ag.projekt_i_id=" + projektIId + " ORDER BY ag.c_nr ASC");

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRAgstkl flrAgstkl = (FLRAgstkl) resultListIteratorAG.next();

			setzeAgstklInEbene(flrAgstkl, SPALTE_TYP0);
		}
		session.close();

		// und alle Bestellungen holen, welche ein Projekt haben, jeodch
		// keinen Vorgaenger haben
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery("SELECT bs FROM FLRBestellung bs WHERE bs.auftrag_i_id is null AND bs.projekt_i_id="
				+ projektIId + "ORDER BY bs.c_nr ASC");

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRBestellung flrBestellung = (FLRBestellung) resultListIteratorAG.next();

			BestellungDto bDto = getBestellungFac().bestellungFindByPrimaryKey(flrBestellung.getI_id());

			if (bDto.getAnfrageIId() != null) {

				AnfrageDto aDto = getAnfrageFac().anfrageFindByPrimaryKey(bDto.getAnfrageIId(), theClientDto);

				setzeAnfrageInEbene(aDto, SPALTE_TYP0);
				setzeBestellungInEbene(flrBestellung, SPALTE_TYP0 + 1);
			} else {
				setzeBestellungInEbene(flrBestellung, SPALTE_TYP0);
			}

		}
		session.close();
		// und alle Anfragen holen, welche ein Projekt haben, jeodch
		// keinen Vorgaenger haben
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT af FROM FLRAnfrage af WHERE  af.projekt_i_id=" + projektIId + " ORDER BY af.c_nr ASC");

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRAnfrage flranfrage = (FLRAnfrage) resultListIteratorAG.next();

			AnfrageDto aDto = getAnfrageFac().anfrageFindByPrimaryKey(flranfrage.getI_id(), theClientDto);
			setzeAnfrageInEbene(aDto, SPALTE_TYP0);
		}
		session.close();

		// und alle Reklamationen holen, welche ein Projekt haben
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(
				"SELECT rk FROM FLRReklamation rk WHERE  rk.projekt_i_id=" + projektIId + " ORDER BY rk.c_nr ASC");

		resultListAG = query.list();
		resultListIteratorAG = resultListAG.iterator();
		while (resultListIteratorAG.hasNext()) {
			FLRReklamation flrreklamation = (FLRReklamation) resultListIteratorAG.next();

			setzeReklamationInEbene(flrreklamation, SPALTE_TYP0);
		}
		session.close();

		reisezeitenHinzufuegen(LocaleFac.BELEGART_PROJEKT, projektIId, SPALTE_TYP0);

		telefonzeitenHinzufuegen(projektIId, null, null);

		int iAnzahlZeilen = hmDaten.size();

		setAnzahlZeilen(iAnzahlZeilen);

		return hmDatenBereitsVerwendet;

	}

	private ArrayList<TelefonzeitenDto> telefonzeitenHinzufuegen(Integer projektIId, Integer angebotIId,
			Integer auftragIId) throws RemoteException {
		// Telefonzeiten

		ArrayList<TelefonzeitenDto> alTZ = new ArrayList<TelefonzeitenDto>();

		Session sessionTelefon = FLRSessionFactory.getFactory().openSession();
		Query queryTelefon = null;

		if (projektIId != null) {
			queryTelefon = sessionTelefon
					.createQuery("SELECT tz FROM FLRTelefonzeiten tz WHERE tz.projekt_i_id=" + projektIId);
		}

		if (angebotIId != null) {
			queryTelefon = sessionTelefon
					.createQuery("SELECT tz FROM FLRTelefonzeiten tz WHERE tz.angebot_i_id=" + angebotIId);
		}

		if (auftragIId != null) {
			queryTelefon = sessionTelefon
					.createQuery("SELECT tz FROM FLRTelefonzeiten tz WHERE tz.auftrag_i_id=" + auftragIId);
		}

		List<?> resultListTelefon = queryTelefon.list();
		Iterator<?> resultListIteratorTelefon = resultListTelefon.iterator();
		while (resultListIteratorTelefon.hasNext()) {

			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) resultListIteratorTelefon.next();

			Object[] oZeile = new Object[ANZAHL_SPALTEN];
			oZeile[SPALTE_TYP0] = "TELEFON";

			oZeile[SPALTE_DATUM] = flrTelefonzeiten.getT_von();

			// Die Kosten kommen aus dem Stundensatz
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(flrTelefonzeiten.getT_von().getTime());

			PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
					flrTelefonzeiten.getPersonal_i_id(), c.get(Calendar.YEAR), c.get(Calendar.MONTH));
			if (pgDto != null && pgDto.getNStundensatz() != null && flrTelefonzeiten.getT_bis() != null) {

				Double dauer = new Double(
						((double) (flrTelefonzeiten.getT_bis().getTime() - flrTelefonzeiten.getT_von().getTime())
								/ 3600000));

				oZeile[SPALTE_NETTOWERT] = pgDto.getNStundensatz().multiply(new BigDecimal(dauer));
				oZeile[SPALTE_WAEHRUNG] = theClientDto.getSMandantenwaehrung();

			}

			if (flrTelefonzeiten.getFlrpersonal() != null) {
				PersonalDto pDto = getPersonalFac()
						.personalFindByPrimaryKey(flrTelefonzeiten.getFlrpersonal().getI_id(), theClientDto);
				oZeile[SPALTE_VERTRETER] = pDto.getCKurzzeichen();
			}

			TelefonzeitenDto tzDto = getZeiterfassungFac().telefonzeitenFindByPrimaryKey(flrTelefonzeiten.getI_id());

			if (projektIId != null) {
				hmDaten.add(oZeile);

				ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(SPALTE_TYP0, tzDto);
				pvDto.setPartnerIId(tzDto.getPartnerIId());
				hmDatenBereitsVerwendet.put("TELEFON" + flrTelefonzeiten.getT_von(), pvDto);

				oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;
			}

			if (angebotIId != null || auftragIId != null) {
				alTZ.add(tzDto);
			}

		}
		sessionTelefon.close();
		return alTZ;
	}

	private void setzeEingangsrechnungInEbene(FLREingangsrechnungAuftragszuordnung ea, int iEbene)
			throws RemoteException {
		// Reisezeiten des Projekts

		String er = "ER";

		Object[] oZeile = new Object[ANZAHL_SPALTEN];
		oZeile[iEbene] = er + ea.getFlreingangsrechnung().getC_nr();

		oZeile[SPALTE_DATUM] = ea.getFlreingangsrechnung().getT_belegdatum();

		ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(iEbene,
				getEingangsrechnungFac().eingangsrechnungAuftragszuordnungFindByPrimaryKey(ea.getI_id()));

		pvDto.setPartnerIId(ea.getFlreingangsrechnung().getFlrlieferant().getFlrpartner().getI_id());

		oZeile[SPALTE_PROJEKTVERLAUF_HELPER] = pvDto;

		oZeile[SPALTE_NETTOWERT] = ea.getN_betrag();
		oZeile[SPALTE_WAEHRUNG] = ea.getFlreingangsrechnung().getWaehrung_c_nr();
		oZeile[SPALTE_STATUS] = ea.getFlreingangsrechnung().getStatus_c_nr();

		hmDatenBereitsVerwendet.put(er + ea.getFlreingangsrechnung().getC_nr(), pvDto);

		hmDaten.add(oZeile);

	}

	private void reisezeitenHinzufuegen(String belegartCNr, Integer belegIId, int iEbene) throws RemoteException {
		// Reisezeiten des Projekts
		ArrayList<ReiseKomplettDto> alReisen = getZeiterfassungFac().holeReisenKomplett(belegartCNr, belegIId, null,
				null, theClientDto);
		for (int k = 0; k < alReisen.size(); k++) {

			ReiseKomplettDto rkDto = alReisen.get(k);

			Iterator it = rkDto.getTmReiseBeginn().keySet().iterator();
			ReiseDto rDtoErstesBeginn = null;
			while (it.hasNext()) {
				ReiseDto rDto = (ReiseDto) rkDto.getTmReiseBeginn().get(it.next());
				// Kosten

				if (rDtoErstesBeginn == null) {
					rDtoErstesBeginn = rDto;
				}

				if ((rDto.getBelegartCNr() != null && rDto.getBelegartCNr().equals(belegartCNr))
						&& rDto.getIBelegartid() != null && rDto.getIBelegartid().equals(belegIId)) {

					BigDecimal kmKostenKomplett = getZeiterfassungFac().getKmKostenEinerReise(rkDto, theClientDto);

					Timestamp tBis = rkDto.getReiseEnde().getTZeit();
					if (it.hasNext()) {

						Iterator itNaechster = rkDto.getTmReiseBeginn().keySet().iterator();
						while (itNaechster.hasNext()) {
							ReiseDto rDtoNaechster = (ReiseDto) rkDto.getTmReiseBeginn().get(itNaechster.next());
							if (rDtoNaechster.getIId().equals(rDto.getIId())) {
								ReiseDto temp = (ReiseDto) rkDto.getTmReiseBeginn().get(itNaechster.next());
								tBis = temp.getTZeit();
							}
						}

					}

					// ///////////////////////////////////////////////////

					// Daten fuer JRuby Script
					String personalart = getPersonalFac().personalFindByPrimaryKey(rDto.getPersonalIId(), theClientDto)
							.getPersonalartCNr();

					BigDecimal bdDiaeten = getZeiterfassungFac().berechneDiaetenAusScript(rDto.getDiaetenIId(),
							rDto.getTZeit(), tBis, theClientDto, personalart);

					// ///////////////////////////////////////////////////

					// BigDecimal bdDiaeten = getZeiterfassungFac()
					// .berechneDiaeten(rDto.getDiaetenIId(),
					// rDto.getTZeit(), tBis, theClientDto);

					BigDecimal kostenDesProjekts = rkDto.getAnteiligeKostenEinesAbschnitts(rDto.getIId(),
							kmKostenKomplett);

					setzeReiseInEbene(rDto, kostenDesProjekts.add(bdDiaeten), iEbene);

				}

			}

		}
	}

	private void auftraegeHinzufuegen(Integer projektIId, Integer angebotIId, boolean bAuftragHatAngebotAlsVorgaenger)
			throws RemoteException {
		Query query;
		// Nun alle Auftraege dazu

		Session sessionAB = FLRSessionFactory.getFactory().openSession();

		Query queryAB = null;

		int iEbene = SPALTE_TYP0;

		if (bAuftragHatAngebotAlsVorgaenger) {
			queryAB = sessionAB.createQuery("SELECT ab FROM FLRAuftrag ab left join ab.flrangebot as ag WHERE ag.id="
					+ angebotIId + " ORDER BY ab.c_nr ASC");
			iEbene++;

		} else {
			queryAB = sessionAB.createQuery(
					"SELECT ab FROM FLRAuftrag ab left join ab.flrangebot as ag WHERE ag.i_id is null AND  ab.projekt_i_id="
							+ projektIId + " ORDER BY ab.c_nr ASC");
		}

		int iEbeneAuftrag = iEbene;
		iEbene++;
		int iEbeneLieferschein = iEbene;
		int iEbeneRechnung = iEbene;

		List<?> resultListAB = queryAB.list();
		Iterator<?> resultListIteratorAB = resultListAB.iterator();
		while (resultListIteratorAB.hasNext()) {

			FLRAuftrag flrauftrag = (FLRAuftrag) resultListIteratorAB.next();

			setzeAuftragInEbene(flrauftrag, iEbeneAuftrag);

			// Lose holen

			TreeMap<String, FLRLosAuftrag> tmLose = new TreeMap<String, FLRLosAuftrag>();

			Session sessionLos = FLRSessionFactory.getFactory().openSession();
			query = sessionLos.createQuery(
					"SELECT los FROM FLRLosAuftrag los left join los.flrauftragposition ap  left join los.flrauftrag a WHERE ap.flrauftrag.i_id="
							+ flrauftrag.getI_id() + " OR a.i_id=" + flrauftrag.getI_id());

			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {

				FLRLosAuftrag flrlos = (FLRLosAuftrag) resultListIterator.next();

				tmLose.put(flrlos.getC_nr(), flrlos);

			}

			Iterator itTM = tmLose.keySet().iterator();

			while (itTM.hasNext()) {
				FLRLosAuftrag flrlos = tmLose.get(itTM.next());
				setzeLosInEbene(flrlos, iEbeneAuftrag + 1);
			}
			sessionLos.close();

			// Bestellungen holen
			Session sessionBestellung = FLRSessionFactory.getFactory().openSession();
			query = sessionBestellung.createQuery("SELECT best FROM FLRBestellung best WHERE best.auftrag_i_id="
					+ flrauftrag.getI_id() + " ORDER BY best.c_nr ASC");

			List<?> resultListBestellungen = query.list();
			Iterator<?> resultListIteratorBestellungen = resultListBestellungen.iterator();
			while (resultListIteratorBestellungen.hasNext()) {

				FLRBestellung flrbestellung = (FLRBestellung) resultListIteratorBestellungen.next();

				BestellungDto bDto = getBestellungFac().bestellungFindByPrimaryKey(flrbestellung.getI_id());

				if (bDto.getAnfrageIId() != null) {

					AnfrageDto aDto = getAnfrageFac().anfrageFindByPrimaryKey(bDto.getAnfrageIId(), theClientDto);

					setzeAnfrageInEbene(aDto, iEbeneAuftrag + 1);
					setzeBestellungInEbene(flrbestellung, iEbeneAuftrag + 2);
				} else {
					setzeBestellungInEbene(flrbestellung, iEbeneAuftrag + 1);
				}

			}

			sessionBestellung.close();

			// Alle Lieferscheine, welche am Auftrag haengen
			HashMap<Integer, LieferscheinDto> lieferscheine = new HashMap<Integer, LieferscheinDto>();
			LieferscheinDto[] lieferscheinDtos = getLieferscheinFac().lieferscheinFindByAuftrag(flrauftrag.getI_id(),
					theClientDto);

			for (int i = 0; i < lieferscheinDtos.length; i++) {
				if (!lieferscheine.containsKey(lieferscheinDtos[i].getIId())) {
					lieferscheine.put(lieferscheinDtos[i].getIId(), lieferscheinDtos[i]);
				}
			}

			// Alle Lieferscheine die ueber die Auftragsposition zugeordnet
			// sind

			Session session = FLRSessionFactory.getFactory().openSession();
			query = session.createQuery(
					"SELECT ls FROM FLRLieferscheinposition ls WHERE ls.flrpositionensichtauftrag.auftrag_i_id="
							+ flrauftrag.getI_id());

			resultList = query.list();
			resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {

				FLRLieferscheinposition lspos = (FLRLieferscheinposition) resultListIterator.next();

				if (!lieferscheine.containsKey(lspos.getFlrlieferschein().getI_id())) {

					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(lspos.getFlrlieferschein().getI_id());
					lieferscheine.put(lspos.getFlrlieferschein().getI_id(), lsDto);
				}
			}

			session.close();

			// Lieferscheine einfuegen

			Iterator<Integer> ls = lieferscheine.keySet().iterator();
			while (ls.hasNext()) {

				LieferscheinDto lsDto = (LieferscheinDto) lieferscheine.get(ls.next());

				setzeLieferscheinInEbene(lsDto, iEbeneLieferschein);

				// befuelleMitLieferscheinDto(lsDto,
				// "AB" + flrauftrag.getC_nr(), "LS" + lsDto.getCNr(),
				// null, null);
			}

			// Rechnungen

			HashMap<Integer, RechnungDto> rechnungen = new HashMap<Integer, RechnungDto>();

			RechnungDto[] rechnungDtos = getRechnungFac().rechnungFindByAuftragIId(flrauftrag.getI_id());

			for (int i = 0; i < rechnungDtos.length; i++) {
				if (!rechnungen.containsKey(rechnungDtos[i].getIId())) {
					rechnungen.put(rechnungDtos[i].getIId(), rechnungDtos[i]);
				}
			}

			session = FLRSessionFactory.getFactory().openSession();
			query = session.createQuery(
					"SELECT re FROM FLRRechnungPosition re WHERE re.flrpositionensichtauftrag.auftrag_i_id="
							+ flrauftrag.getI_id());

			resultList = query.list();
			resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {

				FLRRechnungPosition repos = (FLRRechnungPosition) resultListIterator.next();

				if (!rechnungen.containsKey(repos.getFlrrechnung().getI_id())) {
					RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(repos.getFlrrechnung().getI_id());
					rechnungen.put(repos.getFlrrechnung().getI_id(), reDto);
				}
			}

			// Rechnungen einfuegen

			Iterator<Integer> re = rechnungen.keySet().iterator();
			while (re.hasNext()) {

				RechnungDto reDto = (RechnungDto) rechnungen.get(re.next());
				reDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
				if (reDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
						&& bSchlussrechnung == true) {
					continue;
				}

				setzeRechnungInEbene(reDto, iEbeneRechnung);

				// befuelleMitRechnungDto(reDto, "AB" + flrauftrag.getC_nr(),
				// "RE", null, null);
			}

			// PJ18663 zuegordnete ERs hinzufuegen
			session = FLRSessionFactory.getFactory().openSession();
			query = session
					.createQuery("SELECT ea FROM FLREingangsrechnungAuftragszuordnung ea WHERE ea.flrauftrag.i_id="
							+ flrauftrag.getI_id() + " ORDER BY ea.flreingangsrechnung.c_nr");

			resultList = query.list();
			resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {

				FLREingangsrechnungAuftragszuordnung ea = (FLREingangsrechnungAuftragszuordnung) resultListIterator
						.next();

				setzeEingangsrechnungInEbene(ea, iEbeneAuftrag + 1);

			}

			// Reisezeiten eines Auftrags hinzufuegen
			reisezeitenHinzufuegen(LocaleFac.BELEGART_AUFTRAG, flrauftrag.getI_id(), iEbeneAuftrag + 1);

			
			
		}

		sessionAB.close();

	}
}
