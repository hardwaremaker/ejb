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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Rahmenbedarfe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRRahmenbedarfe;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.DetailbedarfDto;
import com.lp.server.artikel.service.RahmenbedarfeDto;
import com.lp.server.artikel.service.RahmenbedarfeDtoAssembler;
import com.lp.server.artikel.service.RahmenbedarfeFac;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.AufgeloesteFehlmengenDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class RahmenbedarfeFacBean extends LPReport implements RahmenbedarfeFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	private Object[][] data = null;
	private String sAktuellerReport = null;
	private static int REPORT_RAHMENBEDARFE_AUFTRAGSNUMMER = 0;
	private static int REPORT_RAHMENBEDARFE_PROJEKTBEZEICHNUNG = 1;
	private static int REPORT_RAHMENBEDARFE_KUNDE = 2;
	private static int REPORT_RAHMENBEDARFE_MENGE = 3;

	private static int REPORT_ALLERAHMENBEDARFE_LIEFERANT = 0;
	private static int REPORT_ALLERAHMENBEDARFE_ARTIKEL = 1;
	private static int REPORT_ALLERAHMENBEDARFE_BEZEICHNUNG = 2;
	private static int REPORT_ALLERAHMENBEDARFE_RAHMENDETAILBEDARF = 3;
	private static int REPORT_ALLERAHMENBEDARFE_RAHMENBESTELLT = 4;
	private static int REPORT_ALLERAHMENBEDARFE_LAGERSTAND = 5;
	private static int REPORT_ALLERAHMENBEDARFE_RESERVIERT = 6;
	private static int REPORT_ALLERAHMENBEDARFE_BESTELLT = 7;
	private static int REPORT_ALLERAHMENBEDARFE_FEHLMENGE = 8;

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {

		index++;

		return (index < data.length);

	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;

		String fieldName = jRField.getName();

		if (sAktuellerReport.equals(RahmenbedarfeFac.REPORT_RAHMENBEDARFE)) {
			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEDARFE_AUFTRAGSNUMMER];
			} else if ("Projektname".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEDARFE_PROJEKTBEZEICHNUNG];
			} else if ("Kundenname".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEDARFE_KUNDE];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEDARFE_MENGE];
			}

		} else if (sAktuellerReport
				.equals(RahmenbedarfeFac.REPORT_ALLERAHMENBEDARFE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_BEZEICHNUNG];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_BESTELLT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_FEHLMENGE];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_LAGERSTAND];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_LIEFERANT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_RAHMENBESTELLT];
			} else if ("Rahmendetailbedarf".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_RAHMENDETAILBEDARF];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_ALLERAHMENBEDARFE_RESERVIERT];
			}

		}
		return value;
	}

	public BigDecimal getSummeAllerRahmenbedarfeEinesArtikels(Integer artikelIId) {

		BigDecimal bdSumme = new BigDecimal(0);
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria reservierungen = session
				.createCriteria(FLRRahmenbedarfe.class);
		reservierungen.createAlias(
				RahmenbedarfeFac.FLR_RAHMENBEDARFE_FLRARTIKEL, "a").add(
				Restrictions.eq("a.i_id", artikelIId));

		List<?> resultList = reservierungen.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRRahmenbedarfe auftrag = (FLRRahmenbedarfe) resultListIterator
					.next();
			bdSumme = bdSumme.add(auftrag.getN_gesamtmenge());
		}
		return bdSumme;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlleOffenenRahmenbedarfe(
			boolean bSortiertNachArtikelnummer, TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (bSortiertNachArtikelnummer == true) {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("artikel.artikelnummerlang",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("bes.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		index = -1;
		sAktuellerReport = RahmenbedarfeFac.REPORT_ALLERAHMENBEDARFE;
		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		String sQuery = "SELECT r.flrartikel.i_id,  r.flrartikel.c_nr, SUM(r.n_gesamtmenge), (SELECT SUM(fm.n_menge) FROM FLRFehlmenge fm WHERE fm.flrartikel.i_id=r.flrartikel.i_id ),  (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=r.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "') , (SELECT SUM(res.n_menge) FROM FLRArtikelreservierung res WHERE res.flrartikel.i_id=r.flrartikel.i_id), (SELECT SUM(al.n_lagerstand) FROM FLRArtikellager al WHERE al.compId.artikel_i_id=r.flrartikel.i_id ),(SELECT SUM(bes.n_menge) FROM FLRArtikelbestellt bes WHERE bes.flrartikel.i_id=r.flrartikel.i_id), "
				+ " (SELECT SUM(bes.n_menge) FROM FLRArtikelbestellt bes WHERE bes.flrartikel.i_id=r.flrartikel.i_id) FROM FLRRahmenbedarfe r WHERE r.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' GROUP BY r.flrartikel.i_id,r.flrartikel.c_nr ORDER BY r.flrartikel.c_nr ";
		org.hibernate.Query qResult = session.createQuery(sQuery);

		List<?> results = qResult.list();

		data = new Object[results.size()][10];

		Iterator<?> resultListIterator = results.iterator();
		int row = 0;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			data[row][REPORT_ALLERAHMENBEDARFE_ARTIKEL] = o[1];
			data[row][REPORT_ALLERAHMENBEDARFE_BEZEICHNUNG] = o[4];
			data[row][REPORT_ALLERAHMENBEDARFE_FEHLMENGE] = o[3];
			data[row][REPORT_ALLERAHMENBEDARFE_RESERVIERT] = o[5];
			data[row][REPORT_ALLERAHMENBEDARFE_LAGERSTAND] = o[6];
			data[row][REPORT_ALLERAHMENBEDARFE_BESTELLT] = o[7];
			data[row][REPORT_ALLERAHMENBEDARFE_RAHMENDETAILBEDARF] = o[2];

			// Subquery
			Session session2 = factory.openSession();

			String subQuery = "SELECT al.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 FROM FLRArtikellieferant al WHERE al.artikel_i_id="
					+ o[0] + " ORDER BY al.i_sort";

			org.hibernate.Query subResult = session2.createQuery(subQuery);
			subResult.setMaxResults(1);
			List<?> subResults = subResult.list();
			Iterator<?> subResultListIterator = subResults.iterator();

			if (subResultListIterator.hasNext()) {
				String s = (String) subResultListIterator.next();
				data[row][REPORT_ALLERAHMENBEDARFE_LIEFERANT] = s;
			} else {
				data[row][REPORT_ALLERAHMENBEDARFE_LIEFERANT] = "";
			}

			try {
				// Rahmenbestellt
				Hashtable<?, ?> htAnzahlRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt((Integer) o[0], theClientDto);
				if (htAnzahlRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					BigDecimal rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					data[row][REPORT_ALLERAHMENBEDARFE_RAHMENBESTELLT] = rahmenbestellt;
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			session2.close();

			row++;
		}

		session.close();
		if (!bSortiertNachArtikelnummer) {
			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];
					if (((String) a1[REPORT_ALLERAHMENBEDARFE_LIEFERANT])
							.compareTo((String) a2[REPORT_ALLERAHMENBEDARFE_LIEFERANT]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;
					}
				}
			}
		}

		initJRDS(parameter, RahmenbedarfeFac.REPORT_MODUL,
				RahmenbedarfeFac.REPORT_ALLERAHMENBEDARFE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printRahmenbedarfe(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		index = -1;
		sAktuellerReport = RahmenbedarfeFac.REPORT_RAHMENBEDARFE;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria reservierungen = session
				.createCriteria(FLRRahmenbedarfe.class);
		reservierungen.createAlias(
				RahmenbedarfeFac.FLR_RAHMENBEDARFE_FLRARTIKEL, "a").add(
				Restrictions.eq("a.i_id", artikelIId));
		reservierungen.createAlias(
				RahmenbedarfeFac.FLR_RAHMENBEDARFE_FLRAUFTRAG, "auf");

		reservierungen.addOrder(Order.asc("auf.c_nr"));

		List<?> resultList = reservierungen.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][4];
		while (resultListIterator.hasNext()) {
			FLRRahmenbedarfe artikelreservierung = (FLRRahmenbedarfe) resultListIterator
					.next();

			if (artikelreservierung.getFlrauftrag() != null) {
				data[row][REPORT_RAHMENBEDARFE_AUFTRAGSNUMMER] = "AB"
						+ artikelreservierung.getFlrauftrag().getC_nr();
			} else {
				data[row][REPORT_RAHMENBEDARFE_AUFTRAGSNUMMER] = "LO"
						+ artikelreservierung.getFlrlos().getC_nr();
			}

			data[row][REPORT_RAHMENBEDARFE_PROJEKTBEZEICHNUNG] = artikelreservierung
					.getFlrauftrag().getC_bez();
			data[row][REPORT_RAHMENBEDARFE_MENGE] = artikelreservierung
					.getN_gesamtmenge();

			data[row][REPORT_RAHMENBEDARFE_KUNDE] = getKundeFac()
					.kundeFindByPrimaryKey(
							artikelreservierung.getFlrauftrag()
									.getKunde_i_id_auftragsadresse(),
							theClientDto).getPartnerDto().formatTitelAnrede();

			row++;
		}
		session.close();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, RahmenbedarfeFac.REPORT_MODUL,
				RahmenbedarfeFac.REPORT_RAHMENBEDARFE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		print = getReportPrint();
		return print;
	}

	// TODO Ein Riesenauftrag bei Kunde. Funktion sollte jedoch
	// optimiert werden
	@TransactionTimeout(60000)
	public void aktualisiereRahmenbedarfe(DetailbedarfDto b,
			TheClientDto theClientDto) {

		try {

			// Nur wenn noch Positionsmengen offen sind
			if (b.getNMenge().doubleValue() > 0) {
				ArrayList<?> stuecklisteAufegloest = getStuecklisteFac()
						.getStrukturDatenEinerStueckliste(
								b.getStuecklisteIId(),
								theClientDto,
								StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
								0, null, true, true, b.getNMenge(), null, true);

				for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
					StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest
							.get(j);

					System.out.println(j + " von "
							+ stuecklisteAufegloest.size());

					if (strukt.getStuecklistepositionDto() != null) {
						if (strukt.getStuecklistepositionDto().getArtikelIId() != null
								&& strukt.getStuecklistepositionDto()
										.getNZielmenge() != null) {

							BigDecimal positionsmenge = strukt
									.getStuecklistepositionDto()
									.getNZielmenge().multiply(b.getNMenge());

							// CK: PJ 09/0013848
							// Lt. WH Lagerstaende bei
							// STKL-Positionen doch nicht
							// beruecksichtigen
							/*
							 * if (lagerstande != null) {
							 * 
							 * if (strukt .getStuecklistepositionDto()
							 * .getArtikelIId() == 863) { int u = 0; u = 9; u +=
							 * 10; }
							 * 
							 * if (!lagerstande .containsKey(strukt
							 * .getStuecklistepositionDto() .getArtikelIId())) {
							 * BigDecimal lagerstand = getLagerFac()
							 * .getLagerstandOhneExc( strukt
							 * .getStuecklistepositionDto() .getArtikelIId(),
							 * lagerIIdHauptlager, theClientDto); if (lagerstand
							 * != null) { lagerstande .put( strukt
							 * .getStuecklistepositionDto() .getArtikelIId(),
							 * lagerstand); }
							 * 
							 * } if (lagerstande .containsKey(strukt
							 * .getStuecklistepositionDto() .getArtikelIId())) {
							 * 
							 * BigDecimal lagerstandUebrig = (BigDecimal)
							 * lagerstande .get(strukt
							 * .getStuecklistepositionDto() .getArtikelIId());
							 * 
							 * if (lagerstandUebrig .doubleValue() > 0) { if
							 * (lagerstandUebrig .doubleValue() >=
							 * positionsmenge .doubleValue()) {
							 * 
							 * lagerstandUebrig = lagerstandUebrig
							 * .subtract(positionsmenge); positionsmenge = new
							 * BigDecimal( 0); } else { positionsmenge =
							 * positionsmenge .subtract(lagerstandUebrig);
							 * lagerstandUebrig = new BigDecimal( 0); }
							 * lagerstande .put( strukt
							 * .getStuecklistepositionDto() .getArtikelIId(),
							 * lagerstandUebrig); } }
							 * 
							 * }
							 */
							if (positionsmenge.doubleValue() > 0) {

								getRahmenbedarfeFac().createRahmenbedarfe(
										b.getAuftragIId(),
										b.getLosIId(),
										strukt.getStuecklistepositionDto()
												.getArtikelIId(),
										positionsmenge, theClientDto);

							}
						}
					}
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void aktualisiereAlleRahmenauftaegeEinesMandanten(
			TheClientDto theClientDto) throws RemoteException,
			IllegalStateException {

		// Zuerst alle loeschen
		Session session = FLRSessionFactory.getFactory().openSession();

		String hqlDelete = "delete FROM FLRRahmenbedarfe";
		session.createQuery(hqlDelete).executeUpdate();

		session.close();

		ArrayList al = new ArrayList();

		session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT r,(SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=r.flrartikel.i_id) FROM FLRArtikelreservierung r ";

		org.hibernate.Query query = session.createQuery(queryString);

		List resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRArtikelreservierung res = (FLRArtikelreservierung) o[0];
			FLRStueckliste stkl = (FLRStueckliste) o[1];

			if (stkl != null) {
				if (Helper.short2boolean(stkl.getB_fremdfertigung()) == false) {
					DetailbedarfDto d = new DetailbedarfDto();
					d.setArtikelIId(res.getFlrartikel().getI_id());
					d.setTLiefertermin(res.getT_liefertermin());
					d.setNMenge(res.getN_menge());
					d.setStuecklisteIId(stkl.getI_id());
					if (res.getC_belegartnr()
							.equals(LocaleFac.BELEGART_AUFTRAG)) {

						AuftragpositionDto apDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKeyOhneExc(
										res.getI_belegartpositionid());
						if (apDto != null) {
							d.setAuftragIId(apDto.getBelegIId());
						} else {
							continue;
						}

					} else if (res.getC_belegartnr().equals(
							LocaleFac.BELEGART_LOS)) {
						{
							LossollmaterialDto lossollmaterialDto = getFertigungFac()
									.lossollmaterialFindByPrimaryKeyOhneExc(
											res.getI_belegartpositionid());

							if (lossollmaterialDto != null) {
								d.setLosIId(lossollmaterialDto.getLosIId());
							} else {
								continue;
							}
						}

					}

					al.add(d);
				}
			}
		}

		session.close();

		session = FLRSessionFactory.getFactory().openSession();

		queryString = "SELECT f,(SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=f.flrartikel.i_id) FROM FLRFehlmenge f ";

		query = session.createQuery(queryString);

		resultList = query.list();
		resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRFehlmenge f = (FLRFehlmenge) o[0];
			FLRStueckliste stkl = (FLRStueckliste) o[1];

			if (stkl != null) {
				if (Helper.short2boolean(stkl.getB_fremdfertigung()) == false) {
					DetailbedarfDto d = new DetailbedarfDto();
					d.setArtikelIId(f.getArtikel_i_id());
					d.setTLiefertermin(new Timestamp(f.getT_liefertermin()
							.getTime()));
					d.setLosIId(f.getFlrlossollmaterial().getFlrlos().getI_id());
					d.setStuecklisteIId(stkl.getI_id());
					d.setNMenge(f.getN_menge());
					al.add(d);
				}
			}

		}
		session.close();

		ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();

		ReportRahmenreservierungDto[] rr = getArtikelReportFac()
				.getReportRahmenreservierung(kritDtoI, theClientDto);

		for (int i = 0; i < rr.length; i++) {

			if (rr[i].getArtikelIId() != null) {

				StuecklisteDto stkl = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								rr[i].getArtikelIId(), theClientDto);
				if (stkl != null) {
					if (Helper.short2boolean(stkl.getBFremdfertigung()) == false) {
						DetailbedarfDto d = new DetailbedarfDto();
						d.setArtikelIId(rr[i].getArtikelIId());
						d.setTLiefertermin(rr[i]
								.getTUebersteuerterLiefertermin());
						d.setAuftragIId(rr[i].getAuftragIId());
						d.setNMenge(rr[i].getNOffeneMenge());
						d.setStuecklisteIId(stkl.getIId());
						al.add(d);
					}
				}
			}
		}

		// Nach Artikel und Termin sortieren
		for (int i = al.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				DetailbedarfDto a = (DetailbedarfDto) al.get(j);
				DetailbedarfDto b = (DetailbedarfDto) al.get(j + 1);
				if (a.getArtikelIId() > b.getArtikelIId()) {
					DetailbedarfDto h = a;
					al.set(j, b);
					al.set(j + 1, h);
				} else if (a.getArtikelIId().equals(b.getArtikelIId())) {

					if (a.getTLiefertermin().after(b.getTLiefertermin())) {
						DetailbedarfDto h = a;
						al.set(j, b);
						al.set(j + 1, h);
					}

				}
			}
		}

		HashMap hm = new HashMap();

		for (int i = 0; i < al.size(); i++) {

			DetailbedarfDto b = (DetailbedarfDto) al.get(i);

			if (hm.containsKey(b.getArtikelIId())) {
				ArrayList temp = (ArrayList) hm.get(b.getArtikelIId());
				temp.add(b);
				hm.put(b.getArtikelIId(), temp);
			} else {
				ArrayList temp = new ArrayList();
				temp.add(b);
				hm.put(b.getArtikelIId(), temp);
			}

		}

		// Nun lagerstaende und in fertigung abziehen

		Iterator it = hm.keySet().iterator();
		while (it.hasNext()) {
			Integer artikelIId = (Integer) it.next();

			if (artikelIId == 4035) {
				int u = 0;
			}

			// PJ 15179 Lagerstand aller Laeger
			BigDecimal lagerstand = getLagerFac()
					.getLagerstandAllerLagerEinesMandanten(artikelIId,
							theClientDto);

			BigDecimal offeneLosmenge = getFertigungFac().getAnzahlInFertigung(
					artikelIId, theClientDto);

			BigDecimal gesamt = lagerstand.add(offeneLosmenge);

			ArrayList temp = (ArrayList) hm.get(artikelIId);
			for (int i = 0; i < temp.size(); i++) {
				DetailbedarfDto b = (DetailbedarfDto) temp.get(i);

				if (gesamt.doubleValue() > 0) {
					if (b.getNMenge().doubleValue() >= gesamt.doubleValue()) {
						b.setNMenge(b.getNMenge().subtract(gesamt));
						gesamt = new BigDecimal(0);
					} else {

						gesamt = gesamt.subtract(b.getNMenge());
						b.setNMenge(new BigDecimal(0));
					}
				}

				temp.set(i, b);
			}
			hm.put(artikelIId, temp);
		}

		it = hm.keySet().iterator();
		while (it.hasNext()) {
			Integer artikelIId = (Integer) it.next();

			ArrayList temp = (ArrayList) hm.get(artikelIId);
			for (int i = 0; i < temp.size(); i++) {
				DetailbedarfDto b = (DetailbedarfDto) temp.get(i);

				if (b.getNMenge().doubleValue() > 0) {

					System.out.println(i + " von " + temp.size());

					getRahmenbedarfeFac().aktualisiereRahmenbedarfe(b,
							theClientDto);
				}

			}

		}

	}

	public void createRahmenbedarfe(Integer auftragIId, Integer losIId,
			Integer artikelIId, BigDecimal bdMengeMenge,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Rahmenbedarfe rahmenbedarfe = null;

		if (auftragIId == null && losIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("auftragIId == null && losIId == null"));
		}
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_RAHMENBEDARFE);
			rahmenbedarfe = new Rahmenbedarfe(pk, artikelIId, bdMengeMenge);
			rahmenbedarfe.setAuftragIId(auftragIId);
			rahmenbedarfe.setLosIId(losIId);
			em.persist(rahmenbedarfe);
			em.flush();

			// PJ 16656 Lagermindeststand updaten
			Artikel a = em.find(Artikel.class, artikelIId);
			if (a.getFDetailprozentmindeststand() != null) {
				// BigDecimal
				BigDecimal gesamt = getSummeAllerRahmenbedarfeEinesArtikels(a
						.getIId());

				BigDecimal mindeststandNeu = gesamt.multiply(new BigDecimal(a
						.getFDetailprozentmindeststand()).divide(
						new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
				mindeststandNeu = Helper.rundeKaufmaennisch(mindeststandNeu, 0);

				a.setFLagermindest(mindeststandNeu.doubleValue());
				em.merge(a);
				em.flush();

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeRahmenbedarfe(Integer iId) throws EJBExceptionLP {
		try {
			Rahmenbedarfe toRemove = em.find(Rahmenbedarfe.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeRahmenbedarfe(RahmenbedarfeDto rahmenbedarfeDto)
			throws EJBExceptionLP {
		if (rahmenbedarfeDto != null) {
			Integer iId = rahmenbedarfeDto.getIId();
			removeRahmenbedarfe(iId);
		}
	}

	public void updateRahmenbedarfe(RahmenbedarfeDto rahmenbedarfeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (rahmenbedarfeDto != null) {
			Integer iId = rahmenbedarfeDto.getIId();
			try {
				Rahmenbedarfe rahmenbedarfe = em.find(Rahmenbedarfe.class, iId);
				setRahmenbedarfeFromRahmenbedarfeDto(rahmenbedarfe,
						rahmenbedarfeDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public RahmenbedarfeDto rahmenbedarfeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Rahmenbedarfe rahmenbedarfe = em.find(Rahmenbedarfe.class, iId);
		if (rahmenbedarfe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRahmenbedarfeDto(rahmenbedarfe);
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public RahmenbedarfeDto rahmenbedarfeFindByAuftragIIdArtikelIId(
			Integer auftragIId, Integer artikelIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("RahmenbedarfefindByAuftragIIdArtikelIId");
		query.setParameter(1, auftragIId);
		query.setParameter(2, artikelIId);
		Rahmenbedarfe rahmenbedarfe = (Rahmenbedarfe) query.getSingleResult();
		if (rahmenbedarfe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRahmenbedarfeDto(rahmenbedarfe);
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	private void setRahmenbedarfeFromRahmenbedarfeDto(
			Rahmenbedarfe rahmenbedarfe, RahmenbedarfeDto rahmenbedarfeDto) {
		rahmenbedarfe.setAuftragIId(rahmenbedarfeDto.getAuftragIId());
		rahmenbedarfe.setArtikelIId(rahmenbedarfeDto.getArtikelIId());
		rahmenbedarfe.setNGesamtmenge(rahmenbedarfeDto.getNGesamtmenge());
		em.merge(rahmenbedarfe);
		em.flush();
	}

	private RahmenbedarfeDto assembleRahmenbedarfeDto(
			Rahmenbedarfe rahmenbedarfe) {
		return RahmenbedarfeDtoAssembler.createDto(rahmenbedarfe);
	}

	private RahmenbedarfeDto[] assembleRahmenbedarfeDtos(
			Collection<?> rahmenbedarfes) {
		List<RahmenbedarfeDto> list = new ArrayList<RahmenbedarfeDto>();
		if (rahmenbedarfes != null) {
			Iterator<?> iterator = rahmenbedarfes.iterator();
			while (iterator.hasNext()) {
				Rahmenbedarfe rahmenbedarfe = (Rahmenbedarfe) iterator.next();
				list.add(assembleRahmenbedarfeDto(rahmenbedarfe));
			}
		}
		RahmenbedarfeDto[] returnArray = new RahmenbedarfeDto[list.size()];
		return (RahmenbedarfeDto[]) list.toArray(returnArray);
	}
}
