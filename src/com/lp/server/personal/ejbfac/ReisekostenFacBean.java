package com.lp.server.personal.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRReisespesen;
import com.lp.server.personal.service.AbschnittEinerReiseDto;
import com.lp.server.personal.service.DiaetentagessatzDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.personal.service.ReisekostenFac;
import com.lp.server.personal.service.ReisezeitenEinerPersonDto;
import com.lp.server.personal.service.ZeilensummenEinesTages;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.IJasperPrintTransformer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class ReisekostenFacBean extends LPReport implements ReisekostenFac {

	@PersistenceContext
	private EntityManager em;

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private IJasperPrintTransformer jasperPrintTransformer;

	private static int REPORT_REISEZEITEN_TAG = 0;
	private static int REPORT_REISEZEITEN_BEGINN = 1;
	private static int REPORT_REISEZEITEN_ENDE = 2;
	private static int REPORT_REISEZEITEN_KOMMENTAR = 3;
	private static int REPORT_REISEZEITEN_PARTNER = 4;
	private static int REPORT_REISEZEITEN_ENTFERNUNG = 5;
	private static int REPORT_REISEZEITEN_LAND = 6;
	private static int REPORT_REISEZEITEN_SPESEN = 7;
	private static int REPORT_REISEZEITEN_DIAETEN = 8;
	private static int REPORT_REISEZEITEN_AUSLAND = 9;
	private static int REPORT_REISEZEITEN_ZAEHLER = 10;
	private static int REPORT_REISEZEITEN_ECHTESENDE = 12;
	private static int REPORT_REISEZEITEN_TAGESSATZ = 13;
	private static int REPORT_REISEZEITEN_STUNDENSATZ = 14;
	private static int REPORT_REISEZEITEN_MINDESTSATZ = 15;
	private static int REPORT_REISEZEITEN_ABSTUNDEN = 16;
	private static int REPORT_REISEZEITEN_FAHRZEUG_PRIVAT = 17;
	private static int REPORT_REISEZEITEN_FAHRZEUG_FIRMA = 18;
	private static int REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KENNZEICHEN = 19;
	private static int REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KMKOSTEN = 20;
	private static int REPORT_REISEZEITEN_BELEGART = 21;
	private static int REPORT_REISEZEITEN_BELEGNUMMER = 22;
	private static int REPORT_REISEZEITEN_DIAETEN_AUS_SCRIPT = 23;
	private static int REPORT_REISEZEITEN_LKZ = 24;
	private static int REPORT_REISEZEITEN_SCRIPTNAME_INTERNAL = 25;
	private static int REPORT_REISEZEITEN_FEHLER_IN_KM = 26;
	private static int REPORT_REISEZEITEN_SUBREPORT_ZUSAETZLICHE_SPESEN = 27;
	private static int REPORT_REISEZEITEN_ANTEILIGE_KOSTEN_AUS_TAGEWEISEN_DIAETEN = 28;
	private static int REPORT_REISEZEITEN_MITFAHRER = 29;
	private static int REPORT_REISEZEITEN_FAHRZEUG_FIRMA_VERWENDUNGSART = 30;
	private static int REPORT_REISEZEITEN_ANZAHL_SPALTEN = 31;

	protected void setData(Object[][] newData) {
		data = newData;
		jasperPrintTransformer = null;
	}

	protected void setDataTransformer(IJasperPrintTransformer transformer) {
		data = null;
		jasperPrintTransformer = transformer;
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(REPORT_REISEZEITEN)) {
			if ("Beginn".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_BEGINN];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ENDE];
			} else if ("Entfernung".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ENTFERNUNG];
			} else if ("Mitfahrer".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_MITFAHRER];
			} else if ("Kommentar".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_REISEZEITEN_KOMMENTAR]);
			} else if ("Land".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_LAND];
			} else if ("Ausland".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_AUSLAND];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_PARTNER];
			} else if ("EchtesEnde".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ECHTESENDE];
			} else if ("Tag".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_TAG];
			} else if ("Spesen".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_SPESEN];
			} else if ("SubreportZusaetzlicheSpesen".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_SUBREPORT_ZUSAETZLICHE_SPESEN];
			} else if ("Diaeten".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_DIAETEN];
			} else if ("AnteiligeKostenAusTageweisenDiaeten".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ANTEILIGE_KOSTEN_AUS_TAGEWEISEN_DIAETEN];
			} else if ("Zaehler".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ZAEHLER];
			} else if ("Abstunden".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_ABSTUNDEN];
			} else if ("Mindestsatz".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_MINDESTSATZ];
			} else if ("Stundensatz".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_STUNDENSATZ];
			} else if ("Tagessatz".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_TAGESSATZ];
			} else if ("FahrzeugPrivat".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FAHRZEUG_PRIVAT];
			} else if ("FahrzeugFirma".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FAHRZEUG_FIRMA];
			} else if ("FahrzeugFirmaVerwendungsart".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_VERWENDUNGSART];
			} else if ("FahrzeugFirmaKennzeichen".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KENNZEICHEN];
			} else if ("FahrzeugFirmaKMKosten".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KMKOSTEN];
			} else if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_BELEGNUMMER];
			} else if ("DiaetenAusScript".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_DIAETEN_AUS_SCRIPT];
			} else if ("FehlerInKM".equals(fieldName)) {
				value = data[index][REPORT_REISEZEITEN_FEHLER_IN_KM];
			}
		}
		return value;
	}

	public boolean next() throws JRException {
		index++;
		if (data != null) {
			return (index < data.length);
		} else {
			return jasperPrintTransformer.next(index);
		}
	}

	public BigDecimal getKmKostenEinesAbschnitts(AbschnittEinerReiseDto abschnittEinerReiseDto,
			TheClientDto theClientDto) {

		BigDecimal kmKosten = new BigDecimal(0);

		Integer iKm = getZeiterfassungFac().getEntfernungInKmEinerReise(abschnittEinerReiseDto.getReiseIId());

		if (iKm != null) {

			if (abschnittEinerReiseDto.getFahrzeug_privat() != null) {
				// KM-Kosten aus Personalgehalt

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(abschnittEinerReiseDto.gettBeginn().getTime());

				try {
					PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
							abschnittEinerReiseDto.getPersonalIId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH));

					if (pgDto != null) {
						BigDecimal kmGeld1 = pgDto.getNKmgeld1();
						if (kmGeld1 != null) {

							if (pgDto.getFBiskilometer() != null) {

								if (iKm <= pgDto.getFBiskilometer() || pgDto.getFBiskilometer() == 0) {
									kmKosten = kmKosten.add(kmGeld1.multiply(new BigDecimal(iKm.doubleValue())));
								} else {

									kmKosten = kmKosten.add(kmGeld1.multiply(new BigDecimal(pgDto.getFBiskilometer())));
									if (pgDto.getNKmgeld2() != null) {
										Integer km2 = iKm - pgDto.getFBiskilometer().intValue();
										kmKosten = kmKosten.add(pgDto.getNKmgeld2().multiply(new BigDecimal(km2)));
									}
								}

							} else {
								kmKosten = kmKosten.multiply(new BigDecimal(iKm.doubleValue()));
							}

						}
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			} else if (abschnittEinerReiseDto.getFahrzeugIId() != null) {
				// KM-Kosten aus Fahrzeug

				BigDecimal bdKMKostenFahrzeug = getPersonalFac().getKMKostenInZielwaehrung(
						abschnittEinerReiseDto.getFahrzeugIId(), abschnittEinerReiseDto.gettBeginn(),
						theClientDto.getSMandantenwaehrung(), theClientDto);

				if (bdKMKostenFahrzeug != null) {
					kmKosten = bdKMKostenFahrzeug.multiply(new BigDecimal(iKm));
				}

			}

		}

		return kmKosten;
	}

	public ArrayList<AbschnittEinerReiseDto> holeReisenEinesBeleges(String belegartCNr, Integer belegartIId,
			TheClientDto theClientDto) {

		Integer landIIdHeimat = null;

		try {
			Integer partnerMandant = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getPartnerIId();
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerMandant, theClientDto);
			if (partnerDto.getLandplzortDto() != null) {
				landIIdHeimat = partnerDto.getLandplzortDto().getIlandID();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_B_BEGINN, Helper.boolean2Short(true)));

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_BELEGART_C_NR, belegartCNr));

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_I_BELEGARTID, belegartIId));

		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<AbschnittEinerReiseDto> alReisen = new ArrayList<AbschnittEinerReiseDto>();

		HashMap<Integer, String> hmReiseBereitsGefunden = new HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRReise flrReise = (FLRReise) resultListIterator.next();
			// Nun das zugehoerige Ende suchen
			Session session2 = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria critNaechstesEnde = session2.createCriteria(FLRReise.class);
			critNaechstesEnde.add(Restrictions.gt(ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReise.getT_zeit()));
			critNaechstesEnde
					.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, flrReise.getPersonal_i_id()));
			critNaechstesEnde.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_B_BEGINN, Helper.boolean2Short(false)));
			critNaechstesEnde.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
			critNaechstesEnde.setMaxResults(1);
			List<?> resultListNaechstesEnde = critNaechstesEnde.list();
			Iterator<?> resultListIteratortNaechstesEnde = resultListNaechstesEnde.iterator();
			if (resultListIteratortNaechstesEnde.hasNext()) {
				FLRReise flrReiseEnde = (FLRReise) resultListIteratortNaechstesEnde.next();

				if (!hmReiseBereitsGefunden.containsKey(flrReiseEnde.getI_id())) {

					// Nun das erste dazugehoerige beginn suchen

					Session session3 = FLRSessionFactory.getFactory().openSession();
					org.hibernate.Criteria critErstesBeginn = session3.createCriteria(FLRReise.class);
					critErstesBeginn.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReiseEnde.getT_zeit()));
					critErstesBeginn.add(
							Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, flrReise.getPersonal_i_id()));

					critErstesBeginn.addOrder(Order.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

					List<?> resultListErstesBeginn = critErstesBeginn.list();
					Iterator<?> resultListIteratortErstesBeginn = resultListErstesBeginn.iterator();

					FLRReise letzterEintrag = null;

					while (resultListIteratortErstesBeginn.hasNext()) {
						FLRReise rTemp = (FLRReise) resultListIteratortErstesBeginn.next();
						if (!Helper.short2boolean(rTemp.getB_beginn())) {
							break;
						}
						letzterEintrag = rTemp;
					}

					Timestamp tVon = null;
					if (letzterEintrag != null) {
						tVon = new Timestamp(letzterEintrag.getT_zeit().getTime());
					} else {
						tVon = new Timestamp(flrReise.getT_zeit().getTime());
					}
					session3.close();
					ArrayList<AbschnittEinerReiseDto> alAbschnitte = erstelleEinzelneReiseeintraege(tVon,
							new Timestamp(flrReiseEnde.getT_zeit().getTime()), theClientDto, landIIdHeimat,
							flrReise.getPersonal_i_id(),true);

					ReisezeitenEinerPersonDto rzDto = getSummenDerEinzelnenTageUndVerteileKostenAufAbschnitte(
							alAbschnitte, theClientDto);
					alAbschnitte = rzDto.getAlAbschnitteEinerReise();

					for (int k = 0; k < alAbschnitte.size(); k++) {
						AbschnittEinerReiseDto o = (AbschnittEinerReiseDto) alAbschnitte.get(k);
						if (o.getBelegartCNr() != null && o.getBelegartCNr().equals(belegartCNr)) {
							if (o.getBelegartIId() != null && o.getBelegartIId().equals(belegartIId)) {
								alReisen.add(o);
							}
						}

					}

					hmReiseBereitsGefunden.put(flrReiseEnde.getI_id(), "");
				}

			}
			session2.close();

		}

		session.close();
		return alReisen;
	}

	public ArrayList<AbschnittEinerReiseDto> erstelleEinzelneReiseeintraege(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto, Integer landIIdHeimat, Integer personalIId, boolean bMitReisespesen) {

		ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise = new ArrayList<AbschnittEinerReiseDto>();

		// Hole Alle Eintraege des gewuenschten Zeitraums
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, personalIId));

		crit.add(Restrictions.ge(ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
		crit.add(Restrictions.le(ZeiterfassungFac.FLR_REISE_T_ZEIT, tBis));
		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			FLRReise letzterEintrag = (FLRReise) resultListIterator.next();

			// ERSTER EINTRAG
			// Hole den Reiseeintrag vor dem ersten im Zeitraum
			Session sessReiseLetztesBeginn = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria criteriaLetztesBeginn = sessReiseLetztesBeginn.createCriteria(FLRReise.class);
			criteriaLetztesBeginn.add(
					Expression.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, letzterEintrag.getFlrpersonal().getI_id()));
			criteriaLetztesBeginn.add(Expression.le(ZeiterfassungFac.FLR_REISE_T_ZEIT, letzterEintrag.getT_zeit()));
			criteriaLetztesBeginn.addOrder(Order.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
			criteriaLetztesBeginn.setMaxResults(2);
			List<?> listLetzttesBeginn = criteriaLetztesBeginn.list();
			Iterator<?> letzterEintragIterator = listLetzttesBeginn.iterator();

			if (letzterEintragIterator.hasNext()) {
				FLRReise flrAnfang = (FLRReise) letzterEintragIterator.next();
				if (letzterEintragIterator.hasNext()) {
					FLRReise flrLetzterEintrag = (FLRReise) letzterEintragIterator.next();
					// Wenn dieser ein Beginn-Eintrag ist
					if (Helper.short2boolean(flrLetzterEintrag.getB_beginn()) == true) {
						AbschnittEinerReiseDto eintrag = befuelleReiseeintragFuerReport(flrLetzterEintrag, tVon,
								flrAnfang, flrAnfang.getT_zeit(), landIIdHeimat, false,bMitReisespesen, theClientDto);
						// SP8154
						eintrag.setSpesen(BigDecimal.ZERO);

						alAbschnitteEinerReise.add(eintrag);
					}
					// Wenn dieser ein Ende-Eintrag ist, dann ignorieren

				}
			}

			// ENDE-ERSTER EINTRAG

			while (resultListIterator.hasNext()) {
				FLRReise flrReise = (FLRReise) resultListIterator.next();
				// Wenn ENDE, dann neue Zeile hinzufuegen
				if (Helper.short2boolean(flrReise.getB_beginn()) == false) {
					alAbschnitteEinerReise
							.add(befuelleReiseeintragFuerReport(letzterEintrag, letzterEintrag.getT_zeit(), flrReise,
									flrReise.getT_zeit(), landIIdHeimat, true,bMitReisespesen, theClientDto));
				} else if (Helper.short2boolean(flrReise.getB_beginn()) == true
						&& Helper.short2boolean(letzterEintrag.getB_beginn()) == true) {
					// Wenn beginn und vorheriger eintrag beginn, dann
					// ebenfalss neue Zeile hinzufuegen
					alAbschnitteEinerReise
							.add(befuelleReiseeintragFuerReport(letzterEintrag, letzterEintrag.getT_zeit(), flrReise,
									flrReise.getT_zeit(), landIIdHeimat, false,bMitReisespesen, theClientDto));
				}
				letzterEintrag = flrReise;
			}

			if (Helper.short2boolean(letzterEintrag.getB_beginn()) == true) {
				// LETZTER EINTRAG
				// Hole den Reiseeintrag vor dem ersten im Zeitraum
				Session sessReiseLetztesEnde = FLRSessionFactory.getFactory().openSession();
				org.hibernate.Criteria criteriaLetztesEnde = sessReiseLetztesEnde.createCriteria(FLRReise.class);
				criteriaLetztesEnde.add(
						Expression.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, letzterEintrag.getPersonal_i_id()));
				criteriaLetztesEnde.add(Expression.gt(ZeiterfassungFac.FLR_REISE_T_ZEIT, letzterEintrag.getT_zeit()));
				criteriaLetztesEnde.addOrder(Order.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
				criteriaLetztesEnde.setMaxResults(2);
				List<?> listLetzttesEnde = criteriaLetztesEnde.list();
				Iterator<?> letztesEndeIterator = listLetzttesEnde.iterator();

				if (letztesEndeIterator.hasNext()) {
					FLRReise flrNachLetztemEintrag = (FLRReise) letztesEndeIterator.next();

					FLRReise ende = new FLRReise();
					ende.setB_beginn(flrNachLetztemEintrag.getB_beginn());
					ende.setC_kommentar(flrNachLetztemEintrag.getC_kommentar());
					ende.setFlrdiaeten(flrNachLetztemEintrag.getFlrdiaeten());
					ende.setFlrpartner(flrNachLetztemEintrag.getFlrpartner());
					ende.setFlrpersonal(flrNachLetztemEintrag.getFlrpersonal());
					ende.setI_id(flrNachLetztemEintrag.getI_id());
					ende.setPersonal_i_id(flrNachLetztemEintrag.getPersonal_i_id());
					ende.setT_zeit(flrNachLetztemEintrag.getT_zeit());

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(tBis.getTime());
					c.set(Calendar.SECOND, c.get(Calendar.SECOND) - 1);

					alAbschnitteEinerReise
							.add(befuelleReiseeintragFuerReport(letzterEintrag, letzterEintrag.getT_zeit(), ende,
									new java.sql.Date(c.getTimeInMillis()), landIIdHeimat, false, bMitReisespesen, theClientDto));
				}
				sessReiseLetztesEnde.close();

			}
			// ENDE-ERSTER EINTRAG

			sessReiseLetztesBeginn.close();

		}
		session.close();

		return alAbschnitteEinerReise;
	}

	public ReisezeitenEinerPersonDto getSummenDerEinzelnenTageUndVerteileKostenAufAbschnitte(
			ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise, TheClientDto theClientDto) {

		// Anhand Datum und LKZ aufteilen
		TreeMap<Long, TreeMap<Integer, ZeilensummenEinesTages>> abschnitteAufgeteiltInLaenderUndTage = new TreeMap<Long, TreeMap<Integer, ZeilensummenEinesTages>>();

		Iterator<?> itAbschnitte = alAbschnitteEinerReise.iterator();

		String personalartCNr = null;

		while (itAbschnitte.hasNext()) {
			AbschnittEinerReiseDto abschnittEinerReiseDto = (AbschnittEinerReiseDto) itAbschnitte.next();

			personalartCNr = abschnittEinerReiseDto.getPersonalartCNr();

			if (abschnittEinerReiseDto.gettBeginn().before(abschnittEinerReiseDto.gettEnde())) {
				Calendar cVon = Calendar.getInstance();
				cVon.setTimeInMillis(abschnittEinerReiseDto.gettBeginn().getTime());

				Calendar cBis = Calendar.getInstance();
				cBis.setTimeInMillis(abschnittEinerReiseDto.gettEnde().getTime());

				while (cBis.after(cVon)) {
					java.sql.Time tDauer = null;

					java.sql.Timestamp tBis = abschnittEinerReiseDto.gettEnde();
					if (cVon.get(Calendar.DAY_OF_YEAR) == cBis.get(Calendar.DAY_OF_YEAR)) {

						tDauer = new java.sql.Time(cBis.getTimeInMillis() - cVon.getTimeInMillis());

					} else {
						Calendar cTemp = Calendar.getInstance();
						cTemp.setTimeInMillis(cVon.getTimeInMillis());
						cTemp.set(Calendar.HOUR_OF_DAY, 23);
						cTemp.set(Calendar.MINUTE, 59);
						cTemp.set(Calendar.MILLISECOND, 999);
						cTemp.set(Calendar.SECOND, 59);

						tBis = new java.sql.Timestamp(cTemp.getTime().getTime());

						tDauer = new java.sql.Time(cTemp.getTimeInMillis() - cVon.getTimeInMillis());

					}

					tDauer.setTime(tDauer.getTime() - 3600000);
					Double dDauer = Helper.time2Double(tDauer);

					Integer diaetenIId_land = abschnittEinerReiseDto.getDiaetenIId();

					long datum = Helper.cutDate(cVon.getTime()).getTime();

					TreeMap<Integer, ZeilensummenEinesTages> tagesdiaetenProDatum = null;

					if (abschnitteAufgeteiltInLaenderUndTage.containsKey(datum)) {
						tagesdiaetenProDatum = abschnitteAufgeteiltInLaenderUndTage.get(datum);
					} else {
						tagesdiaetenProDatum = new TreeMap<Integer, ZeilensummenEinesTages>();
					}

					ZeilensummenEinesTages zeileTagesDiaeten = null;

					if (tagesdiaetenProDatum.containsKey(diaetenIId_land)) {

						zeileTagesDiaeten = tagesdiaetenProDatum.get(diaetenIId_land);

						if (zeileTagesDiaeten.tVon.after(new java.sql.Timestamp(cVon.getTimeInMillis()))) {
							zeileTagesDiaeten.tVon = new java.sql.Timestamp(cVon.getTimeInMillis());
						}
						if (zeileTagesDiaeten.tBis.before(tBis)) {
							zeileTagesDiaeten.tBis = tBis;
						}

					} else {
						zeileTagesDiaeten = new ZeilensummenEinesTages();

						zeileTagesDiaeten.tVon = new java.sql.Timestamp(cVon.getTimeInMillis());
						zeileTagesDiaeten.tBis = tBis;
						zeileTagesDiaeten.lkz = abschnittEinerReiseDto.getLkz();
						zeileTagesDiaeten.diaetenIId = diaetenIId_land;
						zeileTagesDiaeten.tDatum = Helper.cutTimestamp(zeileTagesDiaeten.tVon);
						if (diaetenIId_land.equals(abschnittEinerReiseDto.getDiaetenIId_Haupteintrag())) {
							zeileTagesDiaeten.haupteintrag = true;
						} else {
							zeileTagesDiaeten.haupteintrag = false;
							zeileTagesDiaeten.diaetenIIdHaupteintrag = abschnittEinerReiseDto
									.getDiaetenIId_Haupteintrag();

							zeileTagesDiaeten.dDauer = zeileTagesDiaeten.dDauer + dDauer;

						}

					}

					// Zu Haupteintrag addieren
					if (!abschnittEinerReiseDto.getDiaetenIId()
							.equals(abschnittEinerReiseDto.getDiaetenIId_Haupteintrag())) {
						ZeilensummenEinesTages zeileTagesDiaetenHaupteintrag = null;
						if (tagesdiaetenProDatum.containsKey(abschnittEinerReiseDto.getDiaetenIId_Haupteintrag())) {
							zeileTagesDiaetenHaupteintrag = tagesdiaetenProDatum
									.get(abschnittEinerReiseDto.getDiaetenIId_Haupteintrag());

							if (zeileTagesDiaetenHaupteintrag.tVon
									.after(new java.sql.Timestamp(cVon.getTimeInMillis()))) {
								zeileTagesDiaetenHaupteintrag.tVon = new java.sql.Timestamp(cVon.getTimeInMillis());
							}
							if (zeileTagesDiaetenHaupteintrag.tBis.before(tBis)) {
								zeileTagesDiaetenHaupteintrag.tBis = tBis;
							}

						} else {
							zeileTagesDiaetenHaupteintrag = new ZeilensummenEinesTages();

							zeileTagesDiaetenHaupteintrag.tVon = new java.sql.Timestamp(cVon.getTimeInMillis());
							zeileTagesDiaetenHaupteintrag.tBis = tBis;
							zeileTagesDiaetenHaupteintrag.lkz = abschnittEinerReiseDto.getLkzHaupteintrag();

							zeileTagesDiaetenHaupteintrag.diaetenIId = abschnittEinerReiseDto
									.getDiaetenIId_Haupteintrag();
							zeileTagesDiaetenHaupteintrag.tDatum = Helper.cutTimestamp(zeileTagesDiaeten.tVon);
							zeileTagesDiaetenHaupteintrag.haupteintrag = true;
						}
						tagesdiaetenProDatum.put(abschnittEinerReiseDto.getDiaetenIId_Haupteintrag(),
								zeileTagesDiaetenHaupteintrag);

					}

					tagesdiaetenProDatum.put(diaetenIId_land, zeileTagesDiaeten);
					abschnitteAufgeteiltInLaenderUndTage.put(datum, tagesdiaetenProDatum);

					cVon.set(Calendar.DAY_OF_MONTH, cVon.get(Calendar.DAY_OF_MONTH) + 1);
					cVon.set(Calendar.HOUR_OF_DAY, 0);
					cVon.set(Calendar.MINUTE, 0);
					cVon.set(Calendar.MILLISECOND, 0);
					cVon.set(Calendar.SECOND, 0);
				}

			}

		}

		ArrayList<ZeilensummenEinesTages> alZeilen = new ArrayList<ZeilensummenEinesTages>();

		Iterator<?> itDatum = abschnitteAufgeteiltInLaenderUndTage.keySet().iterator();

		while (itDatum.hasNext()) {

			Long lDatum = (Long) itDatum.next();

			TreeMap<Integer, ZeilensummenEinesTages> tagesdiaetenProDatum = (TreeMap<Integer, ZeilensummenEinesTages>) abschnitteAufgeteiltInLaenderUndTage
					.get(lDatum);

			Iterator<?> it = tagesdiaetenProDatum.keySet().iterator();
			while (it.hasNext()) {
				Integer diaetenIId_Land = (Integer) it.next();

				ZeilensummenEinesTages zeile = tagesdiaetenProDatum.get(diaetenIId_Land);

				// Nun die Betraege berechnen

				try {
					if (zeile.haupteintrag) {

						BigDecimal diaeten = getZeiterfassungFac().berechneDiaetenAusScript(zeile.diaetenIId,
								zeile.tVon, zeile.tBis, theClientDto, personalartCNr);

						// Dauer berechnen

						long lDauer = zeile.tBis.getTime() - zeile.tVon.getTime();

						zeile.dDauer = (double) lDauer / 3600000;
						zeile.bdBetrag = diaeten;

					} else {

						Calendar cVonUeberDauer = Calendar.getInstance();
						cVonUeberDauer.setTimeInMillis(zeile.tVon.getTime());

						cVonUeberDauer.set(Calendar.HOUR_OF_DAY, 0);
						cVonUeberDauer.set(Calendar.MINUTE, 0);
						cVonUeberDauer.set(Calendar.MILLISECOND, 0);
						cVonUeberDauer.set(Calendar.SECOND, 0);

						Timestamp tVon = new Timestamp(cVonUeberDauer.getTimeInMillis());

						cVonUeberDauer.add(Calendar.MILLISECOND, (int) (zeile.dDauer * 3600000));

						Timestamp tBis = new Timestamp(cVonUeberDauer.getTimeInMillis());

						// Zuerst Diaeten berechnen, wie wenn Haupteintrag
						BigDecimal diaetenWennHauptLand = getZeiterfassungFac().berechneDiaetenAusScript(
								zeile.diaetenIIdHaupteintrag, tVon, tBis, theClientDto, personalartCNr);

						BigDecimal diaetenSubLand = getZeiterfassungFac().berechneDiaetenAusScript(zeile.diaetenIId,
								tVon, tBis, theClientDto, personalartCNr);

						if (diaetenSubLand.subtract(diaetenWennHauptLand).doubleValue() > 0) {
							zeile.bdBetrag = diaetenSubLand.subtract(diaetenWennHauptLand);
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				alZeilen.add(zeile);

			}

		}

		Iterator<?> itTage = alZeilen.iterator();

		BigDecimal bdGesamtsumme = BigDecimal.ZERO;
		while (itTage.hasNext()) {
			ZeilensummenEinesTages zeile = (ZeilensummenEinesTages) itTage.next();

			if (zeile.bdBetrag != null) {
				bdGesamtsumme = bdGesamtsumme.add(zeile.bdBetrag);
			}

		}

		// Nun die Nebeneintraege zu den Haupeintraegen hinzufuegen

		ArrayList<ZeilensummenEinesTages> alZeilenHaupteintraege = new ArrayList<ZeilensummenEinesTages>();

		itTage = alZeilen.iterator();
		while (itTage.hasNext()) {
			ZeilensummenEinesTages zeile = (ZeilensummenEinesTages) itTage.next();
			if (zeile.haupteintrag == true) {
				alZeilenHaupteintraege.add(ZeilensummenEinesTages.clone(zeile));
			}
		}

		itTage = alZeilen.iterator();
		while (itTage.hasNext()) {
			ZeilensummenEinesTages zeile = (ZeilensummenEinesTages) itTage.next();
			if (zeile.haupteintrag == false) {

				// Haupteintrag suchen

				Iterator itHaupteintrag = alZeilenHaupteintraege.iterator();
				while (itHaupteintrag.hasNext()) {
					ZeilensummenEinesTages zeileHaupteintrag = (ZeilensummenEinesTages) itHaupteintrag.next();

					if (zeileHaupteintrag.tDatum.equals(zeile.tDatum)) {
						if (zeileHaupteintrag.bdBetrag == null) {
							zeileHaupteintrag.bdBetrag = BigDecimal.ZERO;
						}

						if (zeile.bdBetrag != null) {
							zeileHaupteintrag.bdBetrag = zeileHaupteintrag.bdBetrag.add(zeile.bdBetrag);
						}
					}
				}

			}
		}

		// Nun die Summe des Tages auf die Abschnitte, welche an diesem Tag sind
		// aufteilen

		itTage = alZeilenHaupteintraege.iterator();
		while (itTage.hasNext()) {
			ZeilensummenEinesTages zeile = (ZeilensummenEinesTages) itTage.next();

			if (zeile.bdBetrag.doubleValue() > 0 && zeile.dDauer > 0) {

				Iterator<?> itAbschnitte2 = alAbschnitteEinerReise.iterator();
				while (itAbschnitte2.hasNext()) {
					AbschnittEinerReiseDto abschnitt = (AbschnittEinerReiseDto) itAbschnitte2.next();

					if (abschnitt.gettBeginn().getTime() <= zeile.tBis.getTime()
							&& abschnitt.gettEnde().getTime() >= zeile.tVon.getTime()) {
						// d.h. Der Abschnitt ist an diesem Tag
						// Anteilige Kosten Berechnen

						BigDecimal bdKostenProStunde = zeile.bdBetrag.divide(new BigDecimal(zeile.dDauer), 12,
								BigDecimal.ROUND_HALF_EVEN);

						// Wieviele Stunden des Abschnitts sind an diesem Tag

						Timestamp tVon = abschnitt.gettBeginn();
						if (tVon.before(zeile.tVon)) {
							tVon = zeile.tVon;
						}

						Timestamp tBis = abschnitt.gettEnde();
						if (tBis.after(zeile.tBis)) {
							tBis = zeile.tBis;
						}

						double dStunden = (tBis.getTime() - tVon.getTime()) / (double) 3600000;

						BigDecimal bdAnteiligeKosten = abschnitt.getBdAnteiligeKostenAusTageweisenDiaeten();

						if (bdAnteiligeKosten == null) {
							bdAnteiligeKosten = BigDecimal.ZERO;
						}

						bdAnteiligeKosten = bdAnteiligeKosten.add(
								Helper.rundeKaufmaennisch(bdKostenProStunde.multiply(new BigDecimal(dStunden)), 2));

						abschnitt.setBdAnteiligeKostenAusTageweisenDiaeten(bdAnteiligeKosten);

					}

				}
			}

		}

		/*
		 * BigDecimal bdGesamtDauerAbschnitte = BigDecimal.ZERO;
		 * 
		 * // Nun Gesamtsumme auf Abschnitte verteilen Iterator<?> itAbschnitte2 =
		 * alAbschnitteEinerReise.iterator(); while (itAbschnitte2.hasNext()) {
		 * AbschnittEinerReiseDto zeile = (AbschnittEinerReiseDto) itAbschnitte2.next();
		 * bdGesamtDauerAbschnitte = bdGesamtDauerAbschnitte.add(new
		 * BigDecimal(zeile.getdDauer())); }
		 * 
		 * itAbschnitte2 = alAbschnitteEinerReise.iterator(); while
		 * (itAbschnitte2.hasNext()) { AbschnittEinerReiseDto zeile =
		 * (AbschnittEinerReiseDto) itAbschnitte2.next();
		 * 
		 * BigDecimal bdAnteiligeKostenAusTageweisenDiaeten = bdGesamtsumme
		 * .divide(bdGesamtDauerAbschnitte, 4, BigDecimal.ROUND_HALF_EVEN) .multiply(new
		 * BigDecimal(zeile.getdDauer()));
		 * 
		 * zeile.setBdAnteiligeKostenAusTageweisenDiaeten(
		 * bdAnteiligeKostenAusTageweisenDiaeten); }
		 */

		ReisezeitenEinerPersonDto rzDto = new ReisezeitenEinerPersonDto();
		rzDto.setAlAbschnitteEinerReise(alAbschnitteEinerReise);
		rzDto.setZeilensummenEinesTages(alZeilen);
		return rzDto;

	}

	private AbschnittEinerReiseDto befuelleReiseeintragFuerReport(FLRReise flrBeginn, java.util.Date tBeginn,
			FLRReise flrEnde, java.util.Date tEnde, Integer landIIdHeimat, Boolean bEchtesEnde, boolean bMitReisespesen,
			TheClientDto theClientDto) {
		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(theClientDto.getLocUi()).getShortWeekdays();

		AbschnittEinerReiseDto abschnittDto = new AbschnittEinerReiseDto();

		abschnittDto.setFehlerInKm(false);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tBeginn.getTime());

		abschnittDto.setTag(kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)]);
		abschnittDto.settBeginn(new java.sql.Timestamp(tBeginn.getTime()));
		abschnittDto.settEnde(new java.sql.Timestamp(tEnde.getTime()));

		long lDauer = tEnde.getTime() - tBeginn.getTime();

		Double dDauer = (double) lDauer / 3600000;
		abschnittDto.setdDauer(dDauer);
		abschnittDto.setPersonalIId(flrBeginn.getPersonal_i_id());
		abschnittDto.setPersonalartCNr(flrBeginn.getFlrpersonal().getPersonalart_c_nr());
		if (flrBeginn.getT_erledigt() != null) {
			abschnittDto.setTErledigt(new java.sql.Timestamp(flrBeginn.getT_erledigt().getTime()));
		}

		abschnittDto.setReiseIId(flrBeginn.getI_id());
		abschnittDto.setFVerrechenbar(flrBeginn.getF_verrechenbar());

		// SP7315 Wenn es ENDE-KM gibt, muss zumindest in einem beliebigen Beginn ein
		// KM-BEGINN vorhanden sein.

		// Wenn eine Reise aus 2 Beginn ein einem Ende besteht, dann ist es auch
		// gueltig,
		// wenn die Beginn Kilometer erst im 2. Beginn eingetragen werden und der erste
		// leer bleibt,
		// d.h. der erste Abschnitt hat keine Entfernung und auf das Flag FehlerInKM ist
		// false

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);
		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, flrBeginn.getPersonal_i_id()));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, flrEnde.getT_zeit()));
		crit.addOrder(Order.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		// Erster Eintrag ist beginn
		FLRReise erstesBeginnMitKilometer = null;
		FLRReise erstesBeginnDerReise = null;
		while (resultListIterator.hasNext()) {
			FLRReise reiseTemp = (FLRReise) resultListIterator.next();

			if (Helper.short2boolean(reiseTemp.getB_beginn()) == true) {
				erstesBeginnDerReise = reiseTemp;
				if (flrEnde.getI_kmende() != null && reiseTemp.getI_kmbeginn() != null) {
					erstesBeginnMitKilometer = reiseTemp;
				}
			} else {
				break;
			}

		}

		if (flrEnde.getI_kmende() != null && erstesBeginnMitKilometer != null) {

			// ALLES OK

		} else if (flrEnde.getI_kmende() != null && erstesBeginnMitKilometer == null) {
			abschnittDto.setFehlerInKm(Boolean.TRUE);
		} else if (flrEnde.getI_kmende() == null && erstesBeginnMitKilometer != null) {
			abschnittDto.setFehlerInKm(Boolean.TRUE);
		}

		abschnittDto.setEchtesEnde(bEchtesEnde);

		abschnittDto.setKommentar(flrBeginn.getC_kommentar());

		if (Helper.short2boolean(flrBeginn.getB_beginn()) == true) {
			abschnittDto.setLand(flrBeginn.getFlrdiaeten().getC_bez());
			abschnittDto.setLkz(flrBeginn.getFlrdiaeten().getFlrland().getC_lkz());

			abschnittDto.setDiaetenIId(flrBeginn.getFlrdiaeten().getI_id());

			if (erstesBeginnDerReise != null) {
				abschnittDto.setDiaetenIId_Haupteintrag(erstesBeginnDerReise.getFlrdiaeten().getI_id());
				abschnittDto.setLkzHaupteintrag(erstesBeginnDerReise.getFlrdiaeten().getFlrland().getC_lkz());
			} else {
				abschnittDto.setDiaetenIId_Haupteintrag(flrBeginn.getFlrdiaeten().getI_id());
				abschnittDto.setLkzHaupteintrag(flrBeginn.getFlrdiaeten().getFlrland().getC_lkz());
			}

		}
		session.close();
		abschnittDto.setEntfernungInKm(getZeiterfassungFac().getEntfernungInKmEinerReise(flrBeginn.getI_id()));

		try {

			if (flrBeginn.getFlrdiaeten() == null) {
				ArrayList alInfo = new ArrayList();

				PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(flrBeginn.getPersonal_i_id(),
						theClientDto);

				alInfo.add(pDto.formatAnrede());
				alInfo.add(flrEnde.getT_zeit());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_REISEZEITEN, alInfo,
						new Exception("FEHLER_IN_REISEZEITEN"));

			}

			abschnittDto.setDiaeten(getZeiterfassungFac().berechneDiaetenAusScript(flrBeginn.getFlrdiaeten().getI_id(),
					new Timestamp(tBeginn.getTime()), new Timestamp(tEnde.getTime()), theClientDto,
					flrBeginn.getFlrpersonal().getPersonalart_c_nr()));

			if (!abschnittDto.getDiaetenIId_Haupteintrag().equals(flrBeginn.getFlrdiaeten().getI_id())) {
				BigDecimal diaetenTemp = getZeiterfassungFac().berechneDiaetenAusScript(
						abschnittDto.getDiaetenIId_Haupteintrag(), new Timestamp(tBeginn.getTime()),
						new Timestamp(tEnde.getTime()), theClientDto, flrBeginn.getFlrpersonal().getPersonalart_c_nr());

				if (diaetenTemp.doubleValue() > abschnittDto.getDiaeten().doubleValue()) {
					abschnittDto
							.setDifferenzDiaetenWennNichImBeginnLand(diaetenTemp.subtract(abschnittDto.getDiaeten()));
				}

			}

			if (flrBeginn.getFlrpartner() != null) {
				com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(flrBeginn.getFlrpartner().getI_id(), theClientDto);

				abschnittDto.setPartner(partnerDto.formatFixTitelName1Name2());

			}

			DiaetentagessatzDto[] dtos = getZeiterfassungFac().diaetentagessatzFindGueltigenTagessatzZuDatum(
					flrBeginn.getFlrdiaeten().getI_id(), new Timestamp(flrBeginn.getT_zeit().getTime()));
			if (dtos != null && dtos.length > 0) {

				abschnittDto.setStundensatz(dtos[0].getNStundensatz());
				abschnittDto.setTagessatz(dtos[0].getNTagessatz());
				abschnittDto.setAbstunden(dtos[0].getIAbstunden());
				abschnittDto.setMindestsatz(dtos[0].getNMindestsatz());
				abschnittDto.setScriptname(dtos[0].getCFilenameScript());
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
		BigDecimal bdSpesen = new BigDecimal(0);

		if (flrBeginn.getN_spesen() != null) {
			bdSpesen = flrBeginn.getN_spesen();
		}

		if (bMitReisespesen) {

			bdSpesen = bdSpesen
					.add(getZeiterfassungFac().getZusaetzlicheReisespesenInMandantenwaehrung(flrBeginn.getI_id()));
		}

		abschnittDto.setSpesen(bdSpesen);

		// Subreport fuer zus. Spesen

		String sQueryZusSpesen = "SELECT s FROM FLRReisespesen s WHERE s.reise_i_id=" + flrBeginn.getI_id()
				+ " ORDER BY s.flreingangsrechnung.c_nr";

		session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryZusSpesen);

		resultList = zeiten.list();

		String[] fieldnames = new String[] { "Eingangsrechnung", "Bruttobetrag", "Belegdatum", "Text", "Zahlungsziel",
				"Status", "Bezahlt am", "Bank", "Auszug" };

		Iterator<?> itSpesen = resultList.iterator();

		Object[][] dataSub = new Object[resultList.size()][fieldnames.length];

		int i = 0;
		while (itSpesen.hasNext()) {
			FLRReisespesen spesen = (FLRReisespesen) itSpesen.next();

			dataSub[i][0] = spesen.getFlreingangsrechnung().getC_nr();
			dataSub[i][1] = spesen.getFlreingangsrechnung().getN_betrag();
			dataSub[i][2] = spesen.getFlreingangsrechnung().getT_belegdatum();
			dataSub[i][3] = spesen.getFlreingangsrechnung().getC_text();
			try {
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(spesen.getEingangsrechnung_i_id());

				if (erDto.getZahlungszielIId() != null) {
					dataSub[i][4] = getMandantFac()
							.zahlungszielFindByPrimaryKey(erDto.getZahlungszielIId(), theClientDto).getBezeichnung();
				}

				dataSub[i][5] = spesen.getFlreingangsrechnung().getStatus_c_nr();

				EingangsrechnungzahlungDto[] zahlungen = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByEingangsrechnungIId(spesen.getEingangsrechnung_i_id());

				if (zahlungen != null && zahlungen.length > 0) {

					dataSub[i][6] = zahlungen[0].getTZahldatum();

					if (zahlungen[0].getBankverbindungIId() != null) {
						BankverbindungDto bvDto = getFinanzFac()
								.bankverbindungFindByPrimaryKey(zahlungen[0].getBankverbindungIId());
						BankDto bankDto = getBankFac().bankFindByPrimaryKey(bvDto.getBankIId(), theClientDto);
						dataSub[i][7] = bankDto.getPartnerDto().formatFixName1Name2();
					}

					dataSub[i][8] = zahlungen[0].getIAuszug();

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			i++;
		}

		abschnittDto.setSubreportZusaetzlicheSpesen(new LPDatenSubreport(dataSub, fieldnames));

		if (flrBeginn.getFlrfahrzeug() != null) {

			abschnittDto.setFahrzeugIId(flrBeginn.getFlrfahrzeug().getI_id());

			abschnittDto.setFahrzeug_firma(flrBeginn.getFlrfahrzeug().getC_bez());
			abschnittDto.setFahrzeug_firma_kennzeichen(flrBeginn.getFlrfahrzeug().getC_kennzeichen());
			abschnittDto.setFahrzeug_firma_verwendungsart(flrBeginn.getFlrfahrzeug().getFahrzeugverwendungsart_c_nr());
			abschnittDto.setFahrzeug_firma_kmkosten(
					getPersonalFac().getKMKostenInZielwaehrung(flrBeginn.getFlrfahrzeug().getI_id(),
							flrEnde.getT_zeit(), theClientDto.getSMandantenwaehrung(), theClientDto));

		}

		abschnittDto.setFahrzeug_privat(flrBeginn.getC_fahrzeug());

		abschnittDto.setMitfahrer(flrBeginn.getI_mitfahrer());

		abschnittDto.setBelegartCNr(flrBeginn.getBelegart_c_nr());

		abschnittDto.setBelegartIId(flrBeginn.getI_belegartid());

		if (flrBeginn.getFlrpartner() != null) {
			abschnittDto.setPartnerIId(flrBeginn.getFlrpartner().getI_id());
		}

		if (flrBeginn.getBelegart_c_nr() != null && flrBeginn.getI_belegartid() != null) {
			if (flrBeginn.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(flrBeginn.getI_belegartid());
				if (aDto != null) {

					abschnittDto.setBelegnummer(aDto.getCNr());

				}
			} else if (flrBeginn.getBelegart_c_nr().equals(LocaleFac.BELEGART_PROJEKT)) {
				ProjektDto pDto = getProjektFac().projektFindByPrimaryKeyOhneExc(flrBeginn.getI_belegartid());
				if (pDto != null) {
					abschnittDto.setBelegnummer(pDto.getCNr());
				}
			}
		}

		if (flrBeginn.getFlrdiaeten().getFlrland().getI_id().equals(landIIdHeimat)) {
			abschnittDto.setAusland(false);
		} else {
			abschnittDto.setAusland(true);
		}

		return abschnittDto;
	}

	public JasperPrintLP printReisezeiten(Integer personalIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, boolean bPlusVersteckte, boolean bNurAnwesende, TheClientDto theClientDto) {
		if (tVon == null || tBis == null || personalIId.equals(iOption)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tVon == null || tBis == null || personalIId == iOption"));
		}
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		if (tVon.after(tBis)) {
			java.sql.Timestamp h = tVon;
			tVon = tBis;
			tBis = h;
		}
		PersonalDto[] personalDtos = null;
		JasperPrintLP print = null;
		Integer landIIdHeimat = null;
		try {
			Integer partnerMandant = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getPartnerIId();
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerMandant, theClientDto);
			if (partnerDto.getLandplzortDto() != null) {
				landIIdHeimat = partnerDto.getLandplzortDto().getIlandID();
			}
			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
				personalDtos = getPersonalFac().personalFindAllPersonenEinerAbteilung(
						kostenstelleIIdAbteilung, theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac().personalFindAllArbeiterEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac().personalFindAllAngestellteEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac().entferneNichtAnwesendePersonen(tVon, tBis, personalDtos,
						theClientDto);
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		for (int i = 0; i < personalDtos.length; i++) {
			PersonalDto personalDto = personalDtos[i];

			personalDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));

			ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise = erstelleEinzelneReiseeintraege(tVon, tBis,
					theClientDto, landIIdHeimat, personalDto.getIId(),true);

			sAktuellerReport = REPORT_REISEZEITEN;
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("P_PERSONAL", personalDto.getPartnerDto().formatFixTitelName1Name2());
			parameter.put("P_PERSONALART", getPersonalFac()
					.personalartFindByPrimaryKey(personalDto.getPersonalartCNr(), theClientDto).getBezeichnung());

			parameter.put("P_VON", tVon);

			parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());

			// KM-Geld
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tVon.getTime());
				PersonalgehaltDto personalgehaltDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
						personalDto.getIId(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

				if (personalgehaltDto != null) {
					parameter.put("P_KMGELD1", personalgehaltDto.getNKmgeld1());
					parameter.put("P_KMGELD2", personalgehaltDto.getNKmgeld2());
					parameter.put("P_KMGELDMITFAHRER", personalgehaltDto.getNKmgeldMitfahrer());
					parameter.put("P_KMGELD1BISKILOMETER", personalgehaltDto.getFBiskilometer().doubleValue());

				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tBis.getTime());
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

			parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));
			// data = new
			// Object[alReiseeintraege.size()][REPORT_REISEZEITEN_ANZAHL_SPALTEN];
			setData(new Object[alAbschnitteEinerReise.size()][REPORT_REISEZEITEN_ANZAHL_SPALTEN]);

			// Subreport Diaeten

			ReisezeitenEinerPersonDto rzDto = getSummenDerEinzelnenTageUndVerteileKostenAufAbschnitte(
					alAbschnitteEinerReise, theClientDto);
			alAbschnitteEinerReise = rzDto.getAlAbschnitteEinerReise();

			ArrayList<ZeilensummenEinesTages> alDatenSubreport = rzDto.getZeilensummenEinesTages();

			String[] fieldnames = new String[] { "Datum", "Von", "Bis", "Lkz", "Stunden", "Betrag", "Abfahrtsland" };

			Iterator<?> itSpesen = alDatenSubreport.iterator();

			Object[][] dataSub = new Object[alDatenSubreport.size()][fieldnames.length];

			int j = 0;
			while (itSpesen.hasNext()) {
				ZeilensummenEinesTages zeile = (ZeilensummenEinesTages) itSpesen.next();

				dataSub[j][0] = zeile.tDatum;
				dataSub[j][1] = zeile.tVon;
				dataSub[j][2] = zeile.tBis;
				dataSub[j][3] = zeile.lkz;
				dataSub[j][4] = zeile.dDauer;
				dataSub[j][5] = zeile.bdBetrag;
				dataSub[j][6] = zeile.haupteintrag;

				j++;
			}

			parameter.put("P_SUBREPORT_DIAETEN", new LPDatenSubreport(dataSub, fieldnames));

			BigDecimal summeSpesen = BigDecimal.ZERO.setScale(2);
			// summeSpesen.setScale(2);

			BigDecimal summeDiaeten = BigDecimal.ZERO.setScale(2);
			// summeDiaeten.setScale(2);
			BigDecimal summeDiaetenAusland = BigDecimal.ZERO.setScale(2);
			// summeDiaetenAusland.setScale(2);

			BigDecimal reisekostenDiaetenAusScript = null;
			BigDecimal summeDiaetenAusScript = null;

			HashMap<Timestamp, String> hmReisetage = new HashMap<Timestamp, String>();

			int iZaehler = 1;

			for (int k = 0; k < alAbschnitteEinerReise.size(); k++) {
				AbschnittEinerReiseDto o = (AbschnittEinerReiseDto) alAbschnitteEinerReise.get(k);
				data[k][REPORT_REISEZEITEN_BEGINN] = o.gettBeginn();
				data[k][REPORT_REISEZEITEN_ENDE] = o.gettEnde();
				data[k][REPORT_REISEZEITEN_FEHLER_IN_KM] = o.isFehlerInKm();
				data[k][REPORT_REISEZEITEN_FAHRZEUG_FIRMA] = o.getFahrzeug_firma();
				data[k][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_VERWENDUNGSART] = o.getFahrzeug_firma_verwendungsart();
				data[k][REPORT_REISEZEITEN_FAHRZEUG_PRIVAT] = o.getFahrzeug_privat();
				data[k][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KENNZEICHEN] = o.getFahrzeug_firma_kennzeichen();
				data[k][REPORT_REISEZEITEN_FAHRZEUG_FIRMA_KMKOSTEN] = o.getFahrzeug_firma_kmkosten();
				data[k][REPORT_REISEZEITEN_BELEGART] = o.getBelegartCNr();
				data[k][REPORT_REISEZEITEN_BELEGNUMMER] = o.getBelegnummer();
				data[k][REPORT_REISEZEITEN_MITFAHRER] = o.getMitfahrer();

				if (o.gettBeginn() != null && o.gettEnde() != null) {

					Calendar cVon = Calendar.getInstance();
					cVon.setTimeInMillis(o.gettBeginn().getTime());

					Calendar cBis = Calendar.getInstance();
					cBis.setTimeInMillis(o.gettEnde().getTime());

					// //////////////////////
					// Daten fuer JRuby Script
					if (o.getScriptname() != null) {

						reisekostenDiaetenAusScript = o.getDiaeten();
					} else {
						reisekostenDiaetenAusScript = null;
					}

					// //////////////////////

					// while (cVon.get(Calendar.DAY_OF_YEAR) <= cBis
					// .get(Calendar.DAY_OF_YEAR)) {
					while (!cVon.after(cBis)) {
						Timestamp tTag = Helper.cutTimestamp(new Timestamp(cVon.getTimeInMillis()));

						if (!hmReisetage.containsKey(tTag)) {
							hmReisetage.put(tTag, "");
						}

						cVon.add(Calendar.DAY_OF_MONTH, 1);
						// cVon.set(Calendar.DAY_OF_MONTH,
						// cVon.get(Calendar.DAY_OF_MONTH) + 1);

					}

				}

				data[k][REPORT_REISEZEITEN_ENTFERNUNG] = o.getEntfernungInKm();
				data[k][REPORT_REISEZEITEN_KOMMENTAR] = o.getKommentar();
				data[k][REPORT_REISEZEITEN_LAND] = o.getLand();
				data[k][REPORT_REISEZEITEN_PARTNER] = o.getPartner();
				data[k][REPORT_REISEZEITEN_ZAEHLER] = iZaehler;
				data[k][REPORT_REISEZEITEN_TAG] = o.getTag();
				data[k][REPORT_REISEZEITEN_SPESEN] = o.getSpesen();
				data[k][REPORT_REISEZEITEN_SUBREPORT_ZUSAETZLICHE_SPESEN] = o.getSubreportZusaetzlicheSpesen();

				data[k][REPORT_REISEZEITEN_ECHTESENDE] = o.isEchtesEnde();

				if (o.isEchtesEnde() == true) {
					iZaehler++;
				}

				if (o.getSpesen() != null) {
					summeSpesen = summeSpesen.add(o.getSpesen());
				}

				data[k][REPORT_REISEZEITEN_ABSTUNDEN] = o.getAbstunden();
				data[k][REPORT_REISEZEITEN_STUNDENSATZ] = o.getStundensatz();
				data[k][REPORT_REISEZEITEN_MINDESTSATZ] = o.getMindestsatz();
				data[k][REPORT_REISEZEITEN_TAGESSATZ] = o.getTagessatz();

				data[k][REPORT_REISEZEITEN_DIAETEN] = o.getDiaeten();
				if (o.getDiaeten() != null) {
					summeDiaeten = summeDiaeten.add(o.getDiaeten());
				}

				data[k][REPORT_REISEZEITEN_ANTEILIGE_KOSTEN_AUS_TAGEWEISEN_DIAETEN] = o
						.getBdAnteiligeKostenAusTageweisenDiaeten();

				data[k][REPORT_REISEZEITEN_AUSLAND] = o.isAusland();
				if (o.isAusland() == true) {
					summeDiaetenAusland = summeDiaetenAusland.add(o.getDiaeten());
				}

				// //////////////////////

				data[k][REPORT_REISEZEITEN_DIAETEN_AUS_SCRIPT] = reisekostenDiaetenAusScript;
				if (reisekostenDiaetenAusScript != null) {
					if (summeDiaetenAusScript == null) {
						summeDiaetenAusScript = BigDecimal.ZERO.setScale(2);
					}
					summeDiaetenAusScript = summeDiaetenAusScript.add(reisekostenDiaetenAusScript);
				}

				// //////////////////////

			}
			parameter.put("P_SUMMESPESEN", summeSpesen);
			parameter.put("P_SUMMEDIAETEN", summeDiaeten);
			parameter.put("P_SUMMEDIAETENAUSLAND", summeDiaetenAusland);
			parameter.put("P_SUMMEDIAETENAUSSCRIPT", summeDiaetenAusScript);
			parameter.put("P_SUMMEREISETAGE", hmReisetage.size());

			parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));

			initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, REPORT_REISEZEITEN, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			if (print != null) {

				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				print = getReportPrint();
			}
		}
		return print;
	}

}
