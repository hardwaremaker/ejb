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
package com.lp.server.kueche.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.kueche.fastlanereader.generated.FLRKassaimport;
import com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplanposition;
import com.lp.server.kueche.service.KuecheReportFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeLieferstatistikDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.FacadeBeauftragter;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class KuecheReportFacBean extends LPReport implements KuecheReportFac {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER = 0;
	private static int REPORT_KUECHENAUSWERTUNG_BEZEICHNUNG = 1;
	private static int REPORT_KUECHENAUSWERTUNG_MENGEVERKAUFT = 2;
	private static int REPORT_KUECHENAUSWERTUNG_PREISVERKAUFT = 3;
	private static int REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT = 4;
	private static int REPORT_KUECHENAUSWERTUNG_KUNDE = 5;
	private static int REPORT_KUECHENAUSWERTUNG_LIEFERSCHEIN = 6;
	private static int REPORT_KUECHENAUSWERTUNG_MENGELIEFERSCEHIN = 7;
	private static int REPORT_KUECHENAUSWERTUNG_PREIS_LIEFERSCHEIN = 8;
	private static int REPORT_KUECHENAUSWERTUNG_THEORETISCHER_WARENEINSATZ = 9;
	private static int REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT_CUT = 10;
	private static int REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP = 11;

	private static int REPORT_KUECHENAUSWERTUNG2_ARTIKELNUMMER = 0;
	private static int REPORT_KUECHENAUSWERTUNG2_BEZEICHNUNG = 1;
	private static int REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT = 2;
	private static int REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ = 3;
	private static int REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH = 4;
	private static int REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL = 5;
	private static int REPORT_KUECHENAUSWERTUNG2_EKPREIS = 6;

	private static int REPORT_DECKUNGSBEITRAG_KOSTENSTELLE = 0;
	private static int REPORT_DECKUNGSBEITRAG_RECHNUNGEN = 1;
	private static int REPORT_DECKUNGSBEITRAG_WARENEINGAENGE = 2;
	private static int REPORT_DECKUNGSBEITRAG_LOSESOFORTVERBRAUCH = 3;
	private static int REPORT_DECKUNGSBEITRAG_LOSEREST = 4;
	private static int REPORT_DECKUNGSBEITRAG_WARENAUSGANG = 5;

	private static int REPORT_DECKUNGSBEITRAG_RECHNUNG = 6;
	private static int REPORT_DECKUNGSBEITRAG_BESTELLUNG = 7;
	private static int REPORT_DECKUNGSBEITRAG_LOSNUMMER = 8;
	private static int REPORT_DECKUNGSBEITRAG_LIEFERSCHEIN_WA = 9;
	private static int REPORT_DECKUNGSBEITRAG_RECHNUNG_WA = 10;

	private static int REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER = 11;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKuechenauswertung2(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		sAktuellerReport = KuecheReportFac.REPORT_KUECHENAUSWERTUNG2;

		Integer fertigungsgruppeIIdSofortverbrauch = null;
		try {
			fertigungsgruppeIIdSofortverbrauch = getStuecklisteFac().fertigungsgruppeFindByMandantCNrCBez(
					theClientDto.getMandant(), StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH).getIId();
		} catch (EJBExceptionLP e1) {
			if (e1.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN,
						new Exception("FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN"));
			} else {
				throw e1;
			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		tVon = Helper.cutTimestamp(tVon);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		String sQuery = "SELECT k, aspr.c_bez FROM FLRKassaimport AS k LEFT OUTER JOIN k.flrartikel.artikelsprset AS aspr  WHERE k.n_menge>0 AND k.t_kassa>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND k.t_kassa<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' AND k.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		// SELECT KUNDE_I_ID,ARTIKEL_I_ID,T_KASSA, SUM(N_MENGE),
		// SUM(N_PREIS*N_MENGE) FROM KUE_KASSAIMPORT WHERE T_KASSA>2009-01-01
		// AND T_KASSA<'2009-07-01' AND N_MENGE>0 GROUP BY
		// KUNDE_I_ID,ARTIKEL_I_ID,T_KASSA

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		TreeMap hmVerbrauchteArtikel = new TreeMap();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLRKassaimport ki = (FLRKassaimport) o[0];

			if (ki.getSpeiseplan_i_id() != null) {

				Set s = ki.getFlrspeiseplan().getSpeiseplanpositionset();

				Iterator iTspeiseplanpos = s.iterator();

				while (iTspeiseplanpos.hasNext()) {
					FLRSpeiseplanposition sppos = (FLRSpeiseplanposition) iTspeiseplanpos.next();

					BigDecimal menge = new BigDecimal(0);

					if (sppos.getN_menge().doubleValue() != 0) {
						menge = sppos.getN_menge().divide(ki.getFlrspeiseplan().getN_menge()).multiply(ki.getN_menge());
					}

					if (hmVerbrauchteArtikel.containsKey(sppos.getFlrartikel().getC_nr())) {
						Object[] oZeile = (Object[]) hmVerbrauchteArtikel.get(sppos.getFlrartikel().getC_nr());

						oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] =

								((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ])
										.add(((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS]).multiply(menge));

						oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT] = ((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT])
								.add(menge);

						hmVerbrauchteArtikel.put(sppos.getFlrartikel().getC_nr(), oZeile);

					} else {
						Object[] oZeile = new Object[7];

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(sppos.getFlrartikel().getI_id(), theClientDto);
						oZeile[REPORT_KUECHENAUSWERTUNG2_ARTIKELNUMMER] = sppos.getFlrartikel().getC_nr();
						oZeile[REPORT_KUECHENAUSWERTUNG2_BEZEICHNUNG] = artikelDto.formatBezeichnung();

						ArtikellieferantDto ekPreis = getArtikelFac().getArtikelEinkaufspreis(sppos.getArtikel_i_id(),
								null, menge, theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(ki.getT_kassa().getTime()), theClientDto);
						if (ekPreis != null && ekPreis.getLief1Preis() != null) {
							oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS] = ekPreis.getLief1Preis();
							oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] = ekPreis.getLief1Preis()
									.multiply(menge);
						} else {
							oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS] = new BigDecimal(0);
							oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] = new BigDecimal(0);
						}

						oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT] = menge;

						oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL] = new BigDecimal(0);
						oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH] = new BigDecimal(0);
						hmVerbrauchteArtikel.put(sppos.getFlrartikel().getC_nr(), oZeile);
					}

				}

			} else {
				if (hmVerbrauchteArtikel.containsKey(ki.getFlrartikel().getC_nr())) {
					Object[] oZeile = (Object[]) hmVerbrauchteArtikel.get(ki.getFlrartikel().getC_nr());

					oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] =

							((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ]).add(
									((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS]).multiply(ki.getN_menge()));

					oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT] = ((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT])
							.add(ki.getN_menge());

					hmVerbrauchteArtikel.put(ki.getFlrartikel().getC_nr(), oZeile);

				} else {
					Object[] oZeile = new Object[7];
					oZeile[REPORT_KUECHENAUSWERTUNG2_ARTIKELNUMMER] = ki.getFlrartikel().getC_nr();
					oZeile[REPORT_KUECHENAUSWERTUNG2_BEZEICHNUNG] = o[1];

					ArtikellieferantDto ekPreis = getArtikelFac().getArtikelEinkaufspreis(ki.getArtikel_i_id(), null,
							ki.getN_menge(), theClientDto.getSMandantenwaehrung(),
							new java.sql.Date(ki.getT_kassa().getTime()), theClientDto);
					if (ekPreis != null && ekPreis.getLief1Preis() != null) {
						oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS] = ekPreis.getLief1Preis();
						oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] = ekPreis.getLief1Preis()
								.multiply(ki.getN_menge());
					} else {
						oZeile[REPORT_KUECHENAUSWERTUNG2_EKPREIS] = new BigDecimal(0);
						oZeile[REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ] = new BigDecimal(0);
					}

					oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT] = ki.getN_menge();

					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL] = new BigDecimal(0);
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH] = new BigDecimal(0);
					hmVerbrauchteArtikel.put(ki.getFlrartikel().getC_nr(), oZeile);
				}
			}

		}

		Session sessionSub = FLRSessionFactory.getFactory().openSession();
		String sQuerySub = "FROM FLRLossollmaterial AS s WHERE s.flrlos.status_c_nr NOT IN('"
				+ FertigungFac.STATUS_GESTOPPT + "','" + FertigungFac.STATUS_ANGELEGT + "') AND s.flrlos.t_ausgabe>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND s.flrlos.t_ausgabe<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' AND s.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
		List<?> resultListSub = hquerySub.list();
		Iterator<?> resultListIteratorSub = resultListSub.iterator();
		while (resultListIteratorSub.hasNext()) {
			FLRLossollmaterial sollmaterial = (FLRLossollmaterial) resultListIteratorSub.next();

			BigDecimal bdAusgegeben = new BigDecimal(0);
			for (Iterator<?> iter = sollmaterial.getIstmaterialset().iterator(); iter.hasNext();) {
				FLRLosistmaterial item = (FLRLosistmaterial) iter.next();
				if (Helper.short2boolean(item.getB_abgang()) == true) {
					bdAusgegeben = bdAusgegeben.add(item.getN_menge());
				} else {
					bdAusgegeben = bdAusgegeben.subtract(item.getN_menge());
				}
			}

			BigDecimal preis = new BigDecimal(0);

			if (bdAusgegeben.doubleValue() != 0) {
				try {
					preis = getFertigungFac().getAusgegebeneMengePreis(sollmaterial.getI_id(), null, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			BigDecimal wert = preis.multiply(bdAusgegeben);

			if (hmVerbrauchteArtikel.containsKey(sollmaterial.getFlrartikel().getC_nr())) {
				Object[] oZeile = (Object[]) hmVerbrauchteArtikel.get(sollmaterial.getFlrartikel().getC_nr());

				if (sollmaterial.getFlrlos().getFertigungsgruppe_i_id().equals(fertigungsgruppeIIdSofortverbrauch)) {
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH] = ((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH])
							.add(wert);
				} else {
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL] = ((BigDecimal) oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL])
							.add(wert);
				}

				hmVerbrauchteArtikel.put(sollmaterial.getFlrartikel().getC_nr(), oZeile);

			} else {
				Object[] oZeile = new Object[7];
				oZeile[REPORT_KUECHENAUSWERTUNG2_ARTIKELNUMMER] = sollmaterial.getFlrartikel().getC_nr();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(sollmaterial.getFlrartikel().getI_id(), theClientDto);
				oZeile[REPORT_KUECHENAUSWERTUNG2_BEZEICHNUNG] = artikelDto.formatBezeichnung();

				oZeile[REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT] = new BigDecimal(0);
				if (sollmaterial.getFlrlos().getFertigungsgruppe_i_id().equals(fertigungsgruppeIIdSofortverbrauch)) {
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH] = wert;
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL] = new BigDecimal(0);
				} else {
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH] = new BigDecimal(0);
					oZeile[REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL] = wert;
				}

				hmVerbrauchteArtikel.put(sollmaterial.getFlrartikel().getC_nr(), oZeile);
			}
		}
		sessionSub.close();
		session.close();
		data = new Object[hmVerbrauchteArtikel.size()][10];
		data = (Object[][]) hmVerbrauchteArtikel.values().toArray(data);

		initJRDS(parameter, KuecheReportFac.REPORT_MODUL, KuecheReportFac.REPORT_KUECHENAUSWERTUNG2,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private void holeWertAllerWareneingaenge(KostenstelleDto kstDto, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			ArrayList alDaten, TheClientDto theClientDto) {
		// Alle Wareneingaenge holen

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT wep FROM FLRWareneingangspositionen wep WHERE wep.flrwareneingang.flrbestellung.kostenstelle_i_id="
				+ kstDto.getIId() + " AND wep.flrwareneingang.t_wareneingansdatum>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND wep.flrwareneingang.t_wareneingansdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "' ORDER BY wep.flrwareneingang.flrbestellung.c_nr";

		Query query = session.createQuery(sQuery);

		List results = query.list();
		Iterator resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRWareneingangspositionen pos = (FLRWareneingangspositionen) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
			oZeile[REPORT_DECKUNGSBEITRAG_KOSTENSTELLE] = kstDto.formatKostenstellenbezeichnung();
			oZeile[REPORT_DECKUNGSBEITRAG_BESTELLUNG] = pos.getFlrbestellposition().getFlrbestellung().getC_nr();

			try {
				BigDecimal einstandspreis = getLagerFac()
						.getGemittelterEinstandspreisEinerZugangsposition(LocaleFac.BELEGART_BESTELLUNG, pos.getI_id());
				if (einstandspreis != null && pos.getN_geliefertemenge() != null) {
					oZeile[REPORT_DECKUNGSBEITRAG_WARENEINGAENGE] = einstandspreis.multiply(pos.getN_geliefertemenge());
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			alDaten.add(oZeile);
		}
		session.close();

	}

	private void holeWarenausgangsGestehungswertZuKostenstelle(KostenstelleDto kstDto, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, ArrayList alDaten, TheClientDto theClientDto) {

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		FacadeBeauftragter fac = new FacadeBeauftragter();
		session = factory.openSession();
		String queryRechnung = " from FLRRechnungPosition rechpos  WHERE rechpos.positionsart_c_nr = '"
				+ LocaleFac.POSITIONSART_IDENT + "' ";

		queryRechnung += " AND rechpos.flrrechnung.status_c_nr NOT IN ('" + RechnungFac.STATUS_STORNIERT + "') ";

		queryRechnung += " AND rechpos.flrrechnung.mandant_c_nr= '" + theClientDto.getMandant() + "'";

		queryRechnung += " AND rechpos.flrrechnung.flrkostenstelle.i_id= '" + kstDto.getIId() + "'";

		if (tVon != null) {
			queryRechnung += " AND rechpos.flrrechnung.d_belegdatum>='"
					+ Helper.formatDateWithSlashes(Helper.cutDate(new java.sql.Date(tVon.getTime()))) + "'";
		}
		if (tBis != null) {
			queryRechnung += " AND rechpos.flrrechnung.d_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";
		}

		queryRechnung += " ORDER BY rechpos.flrrechnung.d_belegdatum DESC";

		Query qRechnung = session.createQuery(queryRechnung);

		List<?> resultListRechnung = qRechnung.list();

		String queryLieferschein = " from FLRLieferscheinposition lspos  WHERE  1=1 ";

		queryLieferschein += " AND lspos.positionsart_c_nr = '" + LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
				+ "'";

		queryRechnung += " AND lspos.flrlieferschein.lieferscheinstatus_status_c_nr NOT IN ('"
				+ LieferscheinFac.LSSTATUS_STORNIERT + "') ";

		queryLieferschein += " AND lspos.flrlieferschein.mandant_c_nr= '" + theClientDto.getMandant() + "'";
		queryLieferschein += " AND lspos.flrlieferschein.flrkostenstelle.i_id= '" + kstDto.getIId() + "'";

		if (tVon != null) {
			queryLieferschein += " AND lspos.flrlieferschein.d_belegdatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "'";
		}
		if (tBis != null) {
			queryLieferschein += " AND lspos.flrlieferschein.d_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";
		}

		queryLieferschein += " ORDER BY lspos.flrlieferschein.d_belegdatum DESC";

		Query qLieferschein = session.createQuery(queryLieferschein);

		List<?> resultListLieferschein = qLieferschein.list();

		Iterator<?> resultListIteratorRechnung = resultListRechnung.iterator();
		// Rechnungspositionen verarbeiten
		ArrayList<KundeLieferstatistikDto> cResult = new ArrayList<KundeLieferstatistikDto>();

		while (resultListIteratorRechnung.hasNext()) {
			FLRRechnungPosition rePos = (FLRRechnungPosition) resultListIteratorRechnung.next();

			try {
				RechnungDto d = fac.getRechnungFac().rechnungFindByPrimaryKey(rePos.getRechnung_i_id());

				String sRechnungsart = d.getRechnungartCNr();

				if (rePos.getFlrartikel() != null) {

					BigDecimal bMenge = rePos.getN_menge();
					BigDecimal bPreis = new BigDecimal(0);
					Object[] oZeile = new Object[REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
					if (sRechnungsart.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
						bPreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
								LocaleFac.BELEGART_GUTSCHRIFT, rePos.getI_id());
						bMenge = bMenge.negate();
						oZeile[REPORT_DECKUNGSBEITRAG_RECHNUNG_WA] = "G" + rePos.getFlrrechnung().getC_nr();
					} else {
						bPreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
								LocaleFac.BELEGART_RECHNUNG, rePos.getI_id());
						oZeile[REPORT_DECKUNGSBEITRAG_RECHNUNG_WA] = "R" + rePos.getFlrrechnung().getC_nr();
					}

					if (rePos.getFlrrechnung().getN_kurs().doubleValue() != 0 && bPreis != null) {
						bPreis = bPreis.divide(rePos.getFlrrechnung().getN_kurs(), 4, BigDecimal.ROUND_HALF_EVEN);
					} else {
						bPreis = new BigDecimal(0);
					}

					oZeile[REPORT_DECKUNGSBEITRAG_KOSTENSTELLE] = kstDto.formatKostenstellenbezeichnung();
					oZeile[REPORT_DECKUNGSBEITRAG_RECHNUNG_WA] = rePos.getFlrrechnung().getC_nr();
					oZeile[REPORT_DECKUNGSBEITRAG_WARENAUSGANG] = bMenge.multiply(bPreis);
					alDaten.add(oZeile);

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		Iterator<?> resultListIteratorLieferschein = resultListLieferschein.iterator();
		// lieferscheinpositionen verarbeiten
		while (resultListIteratorLieferschein.hasNext()) {
			FLRLieferscheinposition lsPos = (FLRLieferscheinposition) resultListIteratorLieferschein.next();

			KundeLieferstatistikDto dto = new KundeLieferstatistikDto();

			dto.setSWarenausgangverursacher(RechnungFac.RECHNUNGART_RECHNUNG);
			if (lsPos.getFlrverleih() != null) {
				dto.setdVerleihfaktor(lsPos.getFlrverleih().getF_faktor());
				dto.setiVerleihtage(lsPos.getFlrverleih().getI_tage());
			}

			dto.setNMenge(lsPos.getN_menge());

			BigDecimal bMenge = lsPos.getN_menge();
			BigDecimal bPreis = new BigDecimal(0);
			try {
				if (lsPos.getN_menge().doubleValue() < 0) {

					if (lsPos.getFlrlieferschein().getFlrziellager() != null) {
						// SP960
						bPreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
								LocaleFac.BELEGART_LSZIELLAGER, lsPos.getI_id());

					} else {
						bPreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
								LocaleFac.BELEGART_LIEFERSCHEIN, lsPos.getI_id());

					}

				} else {
					bPreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
							LocaleFac.BELEGART_LIEFERSCHEIN, lsPos.getI_id());
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			BigDecimal bdKurs = new BigDecimal(
					lsPos.getFlrlieferschein().getF_wechselkursmandantwaehrungzulieferscheinwaehrung().doubleValue());

			if (bdKurs.doubleValue() != 0 && lsPos.getN_nettogesamtpreis() != null) {
				bPreis = bPreis.divide(bdKurs, 4, BigDecimal.ROUND_HALF_EVEN);
			} else {
				bPreis = new BigDecimal(0);
			}

			Object[] oZeile = new Object[REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
			oZeile[REPORT_DECKUNGSBEITRAG_KOSTENSTELLE] = kstDto.formatKostenstellenbezeichnung();
			oZeile[REPORT_DECKUNGSBEITRAG_LIEFERSCHEIN_WA] = lsPos.getFlrlieferschein().getC_nr();
			oZeile[REPORT_DECKUNGSBEITRAG_WARENAUSGANG] = bMenge.multiply(bPreis);
			alDaten.add(oZeile);

		}

	}

	private void holeZuganswerteAllerLose(KostenstelleDto kstDto, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			ArrayList alDaten, TheClientDto theClientDto) {
		// Alle Wareneingaenge holen

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT li FROM FLRLosistmaterial li WHERE li.flrlossollmaterial.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND li.flrlossollmaterial.flrlos.t_ausgabe>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND li.flrlossollmaterial.flrlos.t_ausgabe<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "' AND li.flrlossollmaterial.flrlos.kostenstelle_i_id=" + kstDto.getIId()
				+ " ORDER BY li.flrlossollmaterial.flrlos.c_nr";

		Query query = session.createQuery(sQuery);

		List results = query.list();
		Iterator resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosistmaterial ist = (FLRLosistmaterial) resultListIterator.next();

			try {
				BigDecimal bdMaterialIstpreis = ist.getN_menge().multiply(
						getFertigungFac().getAusgegebeneMengePreis(ist.getLossollmaterial_i_id(), null, theClientDto));

				Object[] oZeile = new Object[REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
				oZeile[REPORT_DECKUNGSBEITRAG_KOSTENSTELLE] = kstDto.formatKostenstellenbezeichnung();
				oZeile[REPORT_DECKUNGSBEITRAG_LOSNUMMER] = ist.getFlrlossollmaterial().getFlrlos().getC_nr();
				if (ist.getFlrlossollmaterial().getFlrartikel().getI_sofortverbrauch() != null) {
					oZeile[REPORT_DECKUNGSBEITRAG_LOSESOFORTVERBRAUCH] = bdMaterialIstpreis;
				} else {
					oZeile[REPORT_DECKUNGSBEITRAG_LOSEREST] = bdMaterialIstpreis;
				}

				alDaten.add(oZeile);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printDeckungsbeitrag(Integer kostenstelleIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		sAktuellerReport = KuecheReportFac.REPORT_DECKUNGSBEITRAG;

		tVon = Helper.cutTimestamp(tVon);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		KostenstelleDto[] kstDtos = null;

		try {
			if (kostenstelleIId == null) {

				kstDtos = getSystemFac().kostenstelleFindByMandant(theClientDto.getMandant());

			} else {
				kstDtos = new KostenstelleDto[1];
				kstDtos[0] = getSystemFac().kostenstelleFindByPrimaryKey(kostenstelleIId);
				parameter.put("P_KOSTENSTELLE", kstDtos[0].formatKostenstellenbezeichnung());

			}

			ArrayList alDaten = new ArrayList();

			for (int i = 0; i < kstDtos.length; i++) {
				// Kostenstelle

				holeZuganswerteAllerLose(kstDtos[i], tVon, tBis, alDaten, theClientDto);

				holeWertAllerWareneingaenge(kstDtos[i], tVon, tBis, alDaten, theClientDto);
				holeWarenausgangsGestehungswertZuKostenstelle(kstDtos[i], tVon, tBis, alDaten, theClientDto);

				// Rechnungswert
				Session session = FLRSessionFactory.getFactory().openSession();

				String sQuery = "SELECT r.c_nr,r.n_wert FROM FLRRechnung r   WHERE r.flrkostenstelle.i_id="
						+ kstDtos[i].getIId() + " AND r.d_belegdatum>='"
						+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND r.d_belegdatum<'"
						+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' ORDER BY r.c_nr";

				Query query = session.createQuery(sQuery);

				List<?> results = query.list();
				Iterator<?> resultListIterator = results.iterator();
				while (resultListIterator.hasNext()) {

					Object[] oDaten = (Object[]) resultListIterator.next();

					Object[] oZeile = new Object[REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
					oZeile[REPORT_DECKUNGSBEITRAG_KOSTENSTELLE] = kstDtos[i].formatKostenstellenbezeichnung();
					oZeile[REPORT_DECKUNGSBEITRAG_RECHNUNG] = oDaten[0];
					oZeile[REPORT_DECKUNGSBEITRAG_RECHNUNGEN] = oDaten[1];
					alDaten.add(oZeile);

				}
				session.close();

			}
			data = new Object[alDaten.size()][REPORT_DECKUNGSBEITRAG_ANZAHL_FELDER];
			data = (Object[][]) alDaten.toArray(data);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		initJRDS(parameter, KuecheReportFac.REPORT_MODUL, KuecheReportFac.REPORT_DECKUNGSBEITRAG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKuechenauswertung1(java.sql.Timestamp tVon, java.sql.Timestamp tBis, String artikelNrVon,
			String artikelNrBis, Integer brancheIId, Integer artikelklasseIId, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);

		HashMap<Integer, KundeDto> cachedKunde = new HashMap<Integer, KundeDto>();
		HashMap<Integer, ArtikelDto> cachedArtikel = new HashMap<Integer, ArtikelDto>();

		if (artikelklasseIId != null) {
			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
		}
		if (brancheIId != null) {
			parameter.put("P_BRANCHE",
					getPartnerServicesFac().brancheFindByPrimaryKey(brancheIId, theClientDto).getCNr());
		}

		sAktuellerReport = KuecheReportFac.REPORT_KUECHENAUSWERTUNG1;

		tVon = Helper.cutTimestamp(tVon);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		String sQuery = "SELECT k.flrartikel.i_id,k.flrartikel.c_nr,k.flrkunde.i_id , k.t_kassa, sum(k.n_menge), sum(k.n_preis*k.n_menge), aspr.c_bez, k.speiseplan_i_id FROM FLRKassaimport k LEFT OUTER JOIN k.flrartikel.artikelsprset AS aspr  WHERE k.n_menge>0 AND k.t_kassa>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND k.t_kassa<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";

		if (artikelNrVon != null) {
			sQuery += " AND k.flrartikel.c_nr >='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {

			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
			sQuery += " AND k.flrartikel.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}

		if (artikelklasseIId != null) {
			sQuery += " AND k.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId.intValue();
		}

		if (brancheIId != null) {
			sQuery += " AND k.flrkunde.flrpartner.branche_i_id=" + brancheIId.intValue();
		}

		sQuery += " GROUP BY k.flrartikel.i_id, k.flrartikel.c_nr,k.flrkunde,  k.t_kassa, aspr.c_bez,k.speiseplan_i_id";

		// SELECT KUNDE_I_ID,ARTIKEL_I_ID,T_KASSA, SUM(N_MENGE),
		// SUM(N_PREIS*N_MENGE) FROM KUE_KASSAIMPORT WHERE T_KASSA>2009-01-01
		// AND T_KASSA<'2009-07-01' AND N_MENGE>0 GROUP BY
		// KUNDE_I_ID,ARTIKEL_I_ID,T_KASSA

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		// HashMap hmKundenIds=new HashMap();

		ArrayList alKassaimport = new ArrayList();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			Integer kundeIId = (Integer) o[2];

			// if(!hmKundenIds.containsKey(kundeIId)){
			// hmKundenIds.put(kundeIId, null);
			// }

			alKassaimport.add(o);

		}

		Session sessionLS = FLRSessionFactory.getFactory().openSession();
		String sQueryLS = "FROM FLRLieferscheinposition lspos WHERE lspos.flrlieferschein.lieferscheinstatus_status_c_nr NOT IN ('"
				+ LocaleFac.STATUS_STORNIERT
				+ "') AND lspos.flrartikel.i_id IS NOT NULL AND lspos.flrlieferschein.d_belegdatum>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND lspos.flrlieferschein.d_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";

		if (artikelNrVon != null) {
			sQueryLS += " AND lspos.flrartikel.c_nr >='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {

			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
			sQueryLS += " AND lspos.flrartikel.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}

		if (artikelklasseIId != null) {
			sQueryLS += " AND lspos.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId.intValue();
		}

		if (brancheIId != null) {
			sQueryLS += " AND lspos.flrlieferschein.flrkunde.flrpartner.branche_i_id=" + brancheIId.intValue();
		}

		Query queryLP = sessionLS.createQuery(sQueryLS);

		List<?> resultsLS = queryLP.list();
		Iterator<?> resultListIteratorLS = resultsLS.iterator();

		// HashMap hmKundenIds=new HashMap();

		ArrayList alLSPositionen = new ArrayList();
		while (resultListIteratorLS.hasNext()) {
			FLRLieferscheinposition lspos = (FLRLieferscheinposition) resultListIteratorLS.next();

			alLSPositionen.add(lspos);

		}

		// Nun beide Listen verknuepfen

		ArrayList alDaten = new ArrayList();

		for (int i = 0; i < alKassaimport.size(); i++) {

			Object[] oImport = (Object[]) alKassaimport.get(i);

			Object[] zeile = new Object[12];

			zeile[REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER] = oImport[1];
			zeile[REPORT_KUECHENAUSWERTUNG_BEZEICHNUNG] = oImport[6];

			BigDecimal menge = (BigDecimal) oImport[4];
			BigDecimal bdGesamtWert = (BigDecimal) oImport[5];

			java.sql.Timestamp tKassa = (java.sql.Timestamp) oImport[3];

			// MWST wurde bereits beim import agbezogen

			zeile[REPORT_KUECHENAUSWERTUNG_MENGEVERKAUFT] = menge;

			zeile[REPORT_KUECHENAUSWERTUNG_PREISVERKAUFT] = bdGesamtWert.divide(menge, BigDecimal.ROUND_HALF_EVEN);
			zeile[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT] = oImport[3];

			zeile[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT_CUT] = Helper.cutTimestamp((Timestamp) oImport[3]);

			try {
				KundeDto kundeDto = null;
				if (cachedKunde.containsKey((Integer) oImport[2])) {
					kundeDto = cachedKunde.get((Integer) oImport[2]);
				} else {
					kundeDto = kundeDto = getKundeFac().kundeFindByPrimaryKey((Integer) oImport[2], theClientDto);
					cachedKunde.put((Integer) oImport[2], kundeDto);
				}

				zeile[REPORT_KUECHENAUSWERTUNG_KUNDE] = kundeDto.getPartnerDto().formatFixName1Name2();

				// PJ 14558 Theoretischer Wareneinsatz
				zeile[REPORT_KUECHENAUSWERTUNG_THEORETISCHER_WARENEINSATZ] = getKuecheFac()
						.getTheoretischerWareneinsatz((Integer) oImport[7], (Integer) oImport[0], menge, theClientDto);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			boolean bZeileImportHinzugefuegt = false;

			for (int j = 0; j < alLSPositionen.size(); j++) {

				FLRLieferscheinposition lspos = (FLRLieferscheinposition) alLSPositionen.get(j);
				// Kunde - oder Lieferadresse
				if (lspos.getFlrlieferschein().getFlrkunde().getI_id().equals(oImport[2])) {
					// Nun muss auch noch der Artikel gleich sein
					if (lspos.getFlrartikel().getI_id().equals(oImport[0])) {
						if (lspos.getFlrlieferschein().getD_belegdatum().getTime() == tKassa.getTime()) {

							if (bZeileImportHinzugefuegt == false) {
								bZeileImportHinzugefuegt = true;
								alDaten.add(zeile);
							}

							Object[] neueZeile = zeile.clone();

							neueZeile[REPORT_KUECHENAUSWERTUNG_MENGEVERKAUFT] = null;
							neueZeile[REPORT_KUECHENAUSWERTUNG_PREISVERKAUFT] = null;

							neueZeile[REPORT_KUECHENAUSWERTUNG_MENGELIEFERSCEHIN] = lspos.getN_menge();
							neueZeile[REPORT_KUECHENAUSWERTUNG_LIEFERSCHEIN] = lspos.getFlrlieferschein().getC_nr();

							if (lspos.getPosition_i_id_artikelset() != null) {
								neueZeile[REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;
							} else {
								if (lspos.getSetartikel_set() != null && lspos.getSetartikel_set().size() > 0) {
									neueZeile[REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
								}
							}

							neueZeile[REPORT_KUECHENAUSWERTUNG_PREIS_LIEFERSCHEIN] = lspos
									.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt();
							alDaten.add(neueZeile);

							// Zeile entfernen
							alLSPositionen.remove(j);
							j--;

						}

					}

				}

			}

			if (bZeileImportHinzugefuegt == false) {
				alDaten.add(zeile);
			}

		}

		// Alle uebrigen LSPos eintragen
		for (int j = 0; j < alLSPositionen.size(); j++) {

			FLRLieferscheinposition lspos = (FLRLieferscheinposition) alLSPositionen.get(j);

			Object[] zeile = new Object[12];

			zeile[REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER] = lspos.getFlrartikel().getC_nr();

			zeile[REPORT_KUECHENAUSWERTUNG_MENGEVERKAUFT] = new BigDecimal(0);

			zeile[REPORT_KUECHENAUSWERTUNG_PREISVERKAUFT] = new BigDecimal(0);

			zeile[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT] = new java.sql.Timestamp(
					lspos.getFlrlieferschein().getD_belegdatum().getTime());
			zeile[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT_CUT] = Helper
					.cutTimestamp(new java.sql.Timestamp(lspos.getFlrlieferschein().getD_belegdatum().getTime()));

			ArtikelDto artikelDto = null;

			if (cachedArtikel.containsKey(lspos.getFlrartikel().getI_id())) {
				artikelDto = cachedArtikel.get(lspos.getFlrartikel().getI_id());
			} else {
				artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(lspos.getFlrartikel().getI_id(),
						theClientDto);
				cachedArtikel.put(lspos.getFlrartikel().getI_id(), artikelDto);
			}

			zeile[REPORT_KUECHENAUSWERTUNG_BEZEICHNUNG] = artikelDto.formatBezeichnung();

			KundeDto kundeDto = null;

			if (cachedKunde.containsKey(lspos.getFlrlieferschein().getKunde_i_id_lieferadresse())) {
				kundeDto = cachedKunde.get(lspos.getFlrlieferschein().getKunde_i_id_lieferadresse());
			} else {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(lspos.getFlrlieferschein().getKunde_i_id_lieferadresse(),
						theClientDto);
				cachedKunde.put(lspos.getFlrlieferschein().getKunde_i_id_lieferadresse(), kundeDto);
			}
			zeile[REPORT_KUECHENAUSWERTUNG_KUNDE] = kundeDto.getPartnerDto().formatFixName1Name2();

			zeile[REPORT_KUECHENAUSWERTUNG_MENGELIEFERSCEHIN] = lspos.getN_menge();
			zeile[REPORT_KUECHENAUSWERTUNG_LIEFERSCHEIN] = lspos.getFlrlieferschein().getC_nr();

			if (lspos.getPosition_i_id_artikelset() != null) {

				zeile[REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

			} else {

				/*
				 * if(lspos.getSetartikel_set()!=null && lspos.getSetartikel_set().size()>0){
				 * zeile[REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP] =
				 * ArtikelFac.SETARTIKEL_TYP_KOPF; }
				 */
			}

			zeile[REPORT_KUECHENAUSWERTUNG_PREIS_LIEFERSCHEIN] = lspos
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt();
			alDaten.add(zeile);

		}

		session.close();
		sessionLS.close();

		// 2 Subreports
		Session sessionSub = FLRSessionFactory.getFactory().openSession();
		String sQuerySub = "FROM FLRLosReport AS l WHERE l.status_c_nr NOT IN('" + FertigungFac.STATUS_GESTOPPT + "','"
				+ FertigungFac.STATUS_ANGELEGT + "') AND l.t_ausgabe>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND l.t_ausgabe<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' ";

		org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
		List<?> resultListSub = hquerySub.list();
		Iterator<?> resultListIteratorSub = resultListSub.iterator();

		ArrayList alSofortverbrauch = new ArrayList();
		ArrayList alLagerbuchung = new ArrayList();

		int SUBREPORT_LOSNUMMER = 0;
		int SUBREPORT_WERT = 1;
		int SUBREPORT_PROJEKT = 2;
		int SUBREPORT_LAGER = 3;
		int SUBREPORT_KOSTENSTELLE = 4;
		int SUBREPORT_AUSGABEDATUM = 5;

		while (resultListIteratorSub.hasNext()) {
			FLRLosReport rep = (FLRLosReport) resultListIteratorSub.next();
			Object[] zeile = new Object[10];
			zeile[SUBREPORT_LOSNUMMER] = rep.getC_nr();
			zeile[SUBREPORT_PROJEKT] = rep.getC_projekt();
			zeile[SUBREPORT_KOSTENSTELLE] = rep.getFlrkostenstelle().getC_nr();
			zeile[SUBREPORT_AUSGABEDATUM] = rep.getT_ausgabe();

			LosDto losDto;
			try {
				losDto = getFertigungFac().losFindByPrimaryKey(rep.getI_id());

				LossollmaterialDto[] sollmat = getFertigungFac().lossollmaterialFindByLosIId(losDto.getIId());
				BigDecimal bdWert = new BigDecimal(0);
				for (int i = 0; i < sollmat.length; i++) {
					BigDecimal bdAusgegeben = getFertigungFac().getAusgegebeneMenge(sollmat[i].getIId(), null,
							theClientDto);
					BigDecimal bdEinzelpreis = getFertigungFac().getAusgegebeneMengePreis(sollmat[i].getIId(), null,
							theClientDto);
					bdWert = bdWert.add(bdAusgegeben.multiply(bdEinzelpreis));
				}

				zeile[SUBREPORT_WERT] = bdWert;

				String laeger = "";
				LoslagerentnahmeDto[] dtos = getFertigungFac().loslagerentnahmeFindByLosIId(rep.getI_id());
				for (int i = 0; i < dtos.length; i++) {
					LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(dtos[i].getLagerIId());
					laeger += lagerDto.getCNr() + ", ";
				}
				zeile[SUBREPORT_LAGER] = laeger;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (rep.getFlrfertigungsgruppe().getC_bez().equals(StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH)) {

				alSofortverbrauch.add(zeile);

			} else {
				alLagerbuchung.add(zeile);
			}

		}

		if (alSofortverbrauch.size() > 0) {
			String[] fieldnames = new String[] { "F_LOSNUMMER", "F_WERT", "F_PROJEKT", "F_LAGER", "F_KOSTENSTELLE",
					"F_AUSGABEDATUM" };
			Object[][] dataSub = new Object[alSofortverbrauch.size()][fieldnames.length];
			dataSub = (Object[][]) alSofortverbrauch.toArray(dataSub);

			parameter.put("DATENSUBREPORT_SOFORTVERBRAUCH", new LPDatenSubreport(dataSub, fieldnames));
		}

		if (alLagerbuchung.size() > 0) {
			String[] fieldnames = new String[] { "F_LOSNUMMER", "F_WERT", "F_PROJEKT", "F_LAGER", "F_KOSTENSTELLE",
					"F_AUSGABEDATUM" };
			Object[][] dataSub = new Object[alLagerbuchung.size()][fieldnames.length];
			dataSub = (Object[][]) alLagerbuchung.toArray(dataSub);

			parameter.put("DATENSUBREPORT_TAGESLOS", new LPDatenSubreport(dataSub, fieldnames));
		}

		// Sortieren nach PJ16694
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] o = (Object[]) alDaten.get(j);
				Object[] o1 = (Object[]) alDaten.get(j + 1);

				Timestamp datum = (Timestamp) o[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT_CUT];
				Timestamp datum1 = (Timestamp) o1[REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT_CUT];
				if (datum.after(datum1)) {
					alDaten.set(j, o1);
					alDaten.set(j + 1, o);
				} else if (datum.equals(datum1)) {
					String artikel = (String) o[REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER];
					String artikel1 = (String) o1[REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER];
					if (artikel.compareTo(artikel1) > 0) {
						alDaten.set(j, o1);
						alDaten.set(j + 1, o);
					}
				}
			}
		}

		data = new Object[alDaten.size()][10];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, KuecheReportFac.REPORT_MODUL, KuecheReportFac.REPORT_KUECHENAUSWERTUNG1,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(KuecheReportFac.REPORT_KUECHENAUSWERTUNG1)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_BEZEICHNUNG];
			} else if ("MengeVerkauft".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_MENGEVERKAUFT];
			} else if ("PreisVerkauft".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_PREISVERKAUFT];
			} else if ("DatumVerkauft".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_DATUMVERKAUFT];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_KUNDE];
			} else if ("LieferscheinMenge".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_MENGELIEFERSCEHIN];
			} else if ("LieferscheinPreis".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_PREIS_LIEFERSCHEIN];
			} else if ("LieferscheinNummer".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_LIEFERSCHEIN];
			} else if ("TheoretischerWareneinsatz".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_THEORETISCHER_WARENEINSATZ];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG_SETARTIKEL_TYP];
			}

		} else if (sAktuellerReport.equals(KuecheReportFac.REPORT_KUECHENAUSWERTUNG2)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_BEZEICHNUNG];
			} else if ("TheoretischerWareneinsatz".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_THEORETISCHER_WARENEINSATZ];
			} else if ("WareneinsatzMaterial".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_MATERIAL];
			} else if ("WareneinsatzSofortverbrauch".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_WARENEINSATZ_SOFORTVERBRAUCH];
			} else if ("MengeVerbraucht".equals(fieldName)) {
				value = data[index][REPORT_KUECHENAUSWERTUNG2_MENGEVERBRAUCHT];
			}
		} else if (sAktuellerReport.equals(KuecheReportFac.REPORT_DECKUNGSBEITRAG)) {
			if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_KOSTENSTELLE];
			} else if ("Rechnungen".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_RECHNUNGEN];
			} else if ("Wareneingaenge".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_WARENEINGAENGE];
			} else if ("LoseSofortverbrauch".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_LOSESOFORTVERBRAUCH];
			} else if ("LoseRest".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_LOSEREST];
			} else if ("Warenausgaenge".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_WARENAUSGANG];
			} else if ("Bestellung".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_BESTELLUNG];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_LOSNUMMER];
			} else if ("Rechnung".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_RECHNUNG];
			} else if ("LieferscheinWA".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_LIEFERSCHEIN_WA];
			} else if ("RechnungWA".equals(fieldName)) {
				value = data[index][REPORT_DECKUNGSBEITRAG_RECHNUNG_WA];
			}
		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

}
