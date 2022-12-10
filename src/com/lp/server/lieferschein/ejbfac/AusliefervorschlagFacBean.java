package com.lp.server.lieferschein.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastposition;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.lieferschein.ejb.Ausliefervorschlag;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.fastlanereader.generated.FLRAusliefervorschlag;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.AusliefervorschlagDto;
import com.lp.server.lieferschein.service.AusliefervorschlagDtoAssembler;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.MwstsatzCache;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class AusliefervorschlagFacBean extends LPReport implements AusliefervorschlagFac, JRDataSource {

	public static int REPORT_AUSLIEFERVORSCHLAG_BELEGART = 0;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE = 1;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME1 = 2;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME2 = 3;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME3 = 4;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_STRASSE = 5;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_LKZ = 6;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_PLZ = 7;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_ORT = 8;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE = 9;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME1 = 10;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME2 = 11;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME3 = 12;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_STRASSE = 13;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_LKZ = 14;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_PLZ = 15;
	public static int REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_ORT = 16;
	public static int REPORT_AUSLIEFERVORSCHLAG_AUSLIEFERTERMIN = 17;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELCNR = 18;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELBEZ = 19;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELEINHEIT = 20;
	public static int REPORT_AUSLIEFERVORSCHLAG_MENGE = 21;
	public static int REPORT_AUSLIEFERVORSCHLAG_BELEGNUMMER = 22;
	public static int REPORT_AUSLIEFERVORSCHLAG_LAGERMINDESTSTAND = 23;
	public static int REPORT_AUSLIEFERVORSCHLAG_RESERVIERUNG = 24;
	public static int REPORT_AUSLIEFERVORSCHLAG_LAGERSOLL = 25;
	public static int REPORT_AUSLIEFERVORSCHLAG_LAGERSTAND = 26;
	public static int REPORT_AUSLIEFERVORSCHLAG_FEHLMENGE = 27;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELKBEZ = 28;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ = 29;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ2 = 30;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID = 31;
	public static int REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID_LIEFERADRESSE = 32;
	public static int REPORT_AUSLIEFERVORSCHLAG_VERFUEGBARE_MENGE = 33;
	public static int REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_KENNUNG = 34;
	public static int REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_BEZEICHNUNG = 35;
	public static int REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 36;
	public static int REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTELMENGE = 37;
	public static int REPORT_AUSLIEFERVORSCHLAG_ARTIKELGEWICHT = 38;
	public static int REPORT_AUSLIEFERVORSCHLAG_ANZAHL_SPALTEN = 39;

	private String sAktuellerReport;
	private Object[][] data = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (sAktuellerReport.equals(REPORT_AUSLIEFERVORSCHLAG)) {

			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_BELEGART];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE];
			} else if ("KundeName1".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME1];
			} else if ("KundeName2".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME2];
			} else if ("KundeName3".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME3];
			} else if ("KundeStrasse".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_STRASSE];
			} else if ("KundeLkz".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_LKZ];
			} else if ("KundePlz".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_PLZ];
			} else if ("KundeOrt".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_ORT];
			} else if ("Lieferadresse".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE];
			} else if ("LieferadresseName1".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME1];

			} else if ("LieferadresseName2".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME2];
			} else if ("LieferadresseName3".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME3];
			} else if ("LieferadresseStrasse".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_STRASSE];
			} else if ("LieferadresseLkz".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_LKZ];
			} else if ("LieferadressePlz".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_PLZ];
			} else if ("LieferadresseOrt".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_ORT];
			} else if ("Ausliefertermin".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_AUSLIEFERTERMIN];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELCNR];
			} else if ("ArtikelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELBEZ];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELEINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_MENGE];
			} else if ("VerfuegbareMenge".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_VERFUEGBARE_MENGE];
			} else if ("Belegnummer".equals(fieldName)) {

				value = data[index][REPORT_AUSLIEFERVORSCHLAG_BELEGNUMMER];
			} else if ("Lagermindeststand".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LAGERMINDESTSTAND];
			} else if ("Reservierungen".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_RESERVIERUNG];
			} else if ("Lagersoll".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LAGERSOLL];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_LAGERSTAND];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_FEHLMENGE];
			} else if ("ArtikelKurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELKBEZ];
			} else if ("ArtikelZusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ];
			} else if ("ArtikelZusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ2];
			} else if ("KundeIId".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID];
			} else if ("KundeIIdLieferadresse".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID_LIEFERADRESSE];
			} else if ("ArtikelGewicht".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_ARTIKELGEWICHT];
			} else if ("VerpackungmittelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_BEZEICHNUNG];
			} else if ("VerpackungmittelGewichtInKg".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_GEWICHT_IN_KG];
			} else if ("VerpackungmittelKennung".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_KENNUNG];
			} else if ("VerpackungmittelMenge".equals(fieldName)) {
				value = data[index][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTELMENGE];
			}

		}
		return value;
	}

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public Integer createAusliefervorschlag(AusliefervorschlagDto ausliefervorschlagDto, TheClientDto theClientDto) {

		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdBestellvorschlag = pkGen.getNextPrimaryKey(PKConst.PK_AUSLIEFERVORSCHLAG);
		ausliefervorschlagDto.setIId(iIdBestellvorschlag);

		// myLogger.error("Generated key Auslieferschlag for tenant " +
		// theClientDto.getMandant() + ", id = " + iIdBestellvorschlag);

		try {
			Ausliefervorschlag ausliefervorschlag = new Ausliefervorschlag(ausliefervorschlagDto.getIId(),
					ausliefervorschlagDto.getTAusliefertermin(), ausliefervorschlagDto.getBelegartCNr(),
					ausliefervorschlagDto.getIBelegartid(), ausliefervorschlagDto.getIBelegartpositionid(),
					ausliefervorschlagDto.getNMenge(), ausliefervorschlagDto.getArtikelIId(),
					ausliefervorschlagDto.getMandantCNr(), ausliefervorschlagDto.getKundeIId(),
					ausliefervorschlagDto.getKundeIIdLieferadresse(), ausliefervorschlagDto.getNVerfuegbar());
			em.persist(ausliefervorschlag);
			em.flush();
			setAusliefervorschlagFromAusliefervorschlagDto(ausliefervorschlag, ausliefervorschlagDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return ausliefervorschlagDto.getIId();
	}

	public AusliefervorschlagDto ausliefervorschlagFindByPrimaryKey(Integer iId) {

		Ausliefervorschlag ausliefervorschlag = em.find(Ausliefervorschlag.class, iId);
		if (ausliefervorschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ausliefervorschlagFindByPrimaryKey. Es gibt keinen Ausliefervorschlag mit iid " + iId);
		}
		return AusliefervorschlagDtoAssembler.createDto(ausliefervorschlag);
	}

	public void loescheAusliefervorlaegeEinesMandaten(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String hqlDelete = "delete FLRAusliefervorschlag WHERE 1=1";

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {

				hqlDelete += " and mandant_c_nr ='" + theClientDto.getMandant() + "'";

			} else {
				// SP5294
				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
						theClientDto)) {
					hqlDelete += " and mandant_c_nr ='" + theClientDto.getMandant() + "'";
				}
			}

			session.createQuery(hqlDelete).executeUpdate();
		} finally {
			closeSession(session);
		}
	}

	public String ausliefervorschlagUeberleiten(Set<Integer> iids, Integer kundeIId, Integer kundeIIdLieferadresse,
			TheClientDto theClientDto) {

		String sNichtGeliefert = null;

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT av FROM FLRAusliefervorschlag av WHERE av.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			if (kundeIId != null) {
				sQuery += "	AND av.kunde_i_id=" + kundeIId;
			}
			if (kundeIIdLieferadresse != null) {
				sQuery += "	AND av.kunde_i_id_lieferadresse=" + kundeIIdLieferadresse;
			}

			if (iids != null) {

				sQuery += "	AND av.i_id IN(";

				Iterator it = iids.iterator();

				while (it.hasNext()) {
					sQuery += it.next();

					if (it.hasNext()) {
						sQuery += ",";
					}
				}

				sQuery += ")";

			}

			sQuery += " ORDER BY av.flrartikel.c_nr, av.t_ausliefertermin,  av.belegart_c_nr DESC, av.i_belegartid ASC";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> list = query.list();
			Iterator it = list.iterator();

			// PJ19683
			boolean bMitMaterialzuschlag = !getParameterFac()
					.getMaterialkursAufBasisRechnungsdatum(theClientDto.getMandant());
			boolean bPositionsKontierung = getParameterFac().getKundenPositionskontierung(theClientDto.getMandant());

			while (it.hasNext()) {
				FLRAusliefervorschlag av = (FLRAusliefervorschlag) it.next();

				Integer lieferscheinIId = null;

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(av.getArtikel_i_id(), theClientDto);

				// SNR/CHNR-Artikel derzeit nicht moeglich

				if (aDto.istArtikelSnrOderchargentragend()) {
					continue;
				}

				if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {

					Session session3 = FLRSessionFactory.getFactory().openSession();
					String sQuery3 = "FROM FLRLieferscheinposition lp WHERE lp.flrlieferschein.flrkunde.i_id="
							+ av.getKunde_i_id_lieferadresse()
							+ " AND lp.flrpositionensichtauftrag.flrauftrag.flrkunde.i_id=" + av.getKunde_i_id()
							+ " AND lp.flrlieferschein.d_belegdatum='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis()))
							+ "' AND lp.flrlieferschein.lieferscheinstatus_status_c_nr='" + LocaleFac.STATUS_ANGELEGT
							+ "' AND lp.flrlieferschein.lieferscheinart_c_nr='" + LieferscheinFac.LSART_AUFTRAG
							+ "' ORDER BY lp.flrlieferschein.c_nr DESC";

					org.hibernate.Query query3 = session3.createQuery(sQuery3);
					query3.setMaxResults(1);
					List<?> results3 = query3.list();

					if (results3.size() > 0) {
						FLRLieferscheinposition flrLspos = (FLRLieferscheinposition) results3.iterator().next();

						lieferscheinIId = flrLspos.getFlrlieferschein().getI_id();
					}
					session3.close();

				} else if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
					Session session3 = FLRSessionFactory.getFactory().openSession();
					String sQuery3 = "FROM FLRLieferscheinposition lp WHERE lp.flrlieferschein.flrkunde.i_id="
							+ av.getKunde_i_id_lieferadresse()
							+ " AND lp.flrforecastposition.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.i_id="
							+ av.getKunde_i_id() + " AND lp.flrlieferschein.d_belegdatum='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis()))
							+ "' AND lp.flrlieferschein.lieferscheinstatus_status_c_nr='" + LocaleFac.STATUS_ANGELEGT
							+ "' AND lp.flrlieferschein.lieferscheinart_c_nr='" + LieferscheinFac.LSART_FREI
							+ "' ORDER BY lp.flrlieferschein.c_nr DESC";

					org.hibernate.Query query3 = session3.createQuery(sQuery3);
					query3.setMaxResults(1);
					List<?> results3 = query3.list();

					if (results3.size() > 0) {
						FLRLieferscheinposition flrLspos = (FLRLieferscheinposition) results3.iterator().next();

						lieferscheinIId = flrLspos.getFlrlieferschein().getI_id();
					}
					session3.close();
				}
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(av.getKunde_i_id(), theClientDto);

/*				
				MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(), theClientDto);
*/
				BigDecimal lagerstand = new BigDecimal(9999999999D);

				if (Helper.short2boolean(av.getFlrartikel().getB_lagerbewirtschaftet())) {

					lagerstand = getLagerFac().getLagerstand(av.getArtikel_i_id(),
							kundeDto.getLagerIIdAbbuchungslager(), theClientDto);
				}

				if (lagerstand.doubleValue() >= av.getN_menge().doubleValue()) {

					if (lieferscheinIId == null) {
						// Neu anlegen

						LieferscheinDto lieferscheinDto = new LieferscheinDto();

						lieferscheinDto.setKundeIIdLieferadresse(av.getKunde_i_id_lieferadresse());

						if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
							AuftragDto abDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(av.getI_belegartid());
							if (abDto == null) {
								continue;
							}
							lieferscheinDto.setKundeIIdRechnungsadresse(abDto.getKundeIIdRechnungsadresse());
							lieferscheinDto.setLieferscheinartCNr(LieferscheinFac.LSART_AUFTRAG);
							lieferscheinDto.setAuftragIId(abDto.getIId());
							lieferscheinDto.setWaehrungCNr(abDto.getCAuftragswaehrung());
						} else if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
							ForecastDto fDto = getForecastFac().forecastFindByPrimaryKeyOhneExc(av.getI_belegartid());
							if (fDto == null) {
								continue;
							}

							lieferscheinDto.setKundeIIdRechnungsadresse(fDto.getKundeIId());

							// SP5370
							if (kundeDto.getPartnerIIdRechnungsadresse() != null) {
								KundeDto kundeRechnungsadresseDto = getKundeFac()
										.kundeFindByiIdPartnercNrMandantOhneExc(
												kundeDto.getPartnerIIdRechnungsadresse(), kundeDto.getMandantCNr(),
												theClientDto);
								if (kundeRechnungsadresseDto != null) {
									lieferscheinDto.setKundeIIdRechnungsadresse(kundeRechnungsadresseDto.getIId());
								}
							}

							lieferscheinDto.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
							lieferscheinDto.setWaehrungCNr(kundeDto.getCWaehrung());
						}

						lieferscheinDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());
						lieferscheinDto.setLagerIId(kundeDto.getLagerIIdAbbuchungslager());

						lieferscheinDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1));
						lieferscheinDto.setTBelegdatum(
								Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis())));
						lieferscheinDto.setMandantCNr(theClientDto.getMandant());

						if (kundeDto.getKostenstelleIId() != null) {
							lieferscheinDto.setKostenstelleIId(kundeDto.getKostenstelleIId());
						} else {
							lieferscheinDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
						}

						lieferscheinDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

						lieferscheinDto.setLieferartIId(kundeDto.getLieferartIId());
						lieferscheinDto.setSpediteurIId(kundeDto.getSpediteurIId());
						lieferscheinDto.setZahlungszielIId(kundeDto.getZahlungszielIId());

						try {
							lieferscheinIId = getLieferscheinFac().createLieferschein(lieferscheinDto, theClientDto);
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					}

					// Nun Position anlegen

					LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinIId);

					MwstsatzCache mwstsatzCache = new MwstsatzCache(lsDto, theClientDto);
					final MwstsatzDto mwstsatzDtoAktuell = mwstsatzCache
							.getValueOfKey(kundeDto.getMwstsatzbezIId());

					// Position anlegen
					LieferscheinpositionDto lieferscheinposDto = new LieferscheinpositionDto();
					lieferscheinposDto.setLieferscheinIId(lieferscheinIId);

					if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						lieferscheinposDto.setLieferscheinpositionartCNr(
								LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE);
					} else {
						lieferscheinposDto
								.setLieferscheinpositionartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
					}

					lieferscheinposDto.setNMenge(av.getN_menge());

					// BigDecimal preis = new BigDecimal(0);

					if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragpositionDto apDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKeyOhneExc(av.getI_belegartpositionid());

						if (apDto == null) {
							continue;
						}
						AuftragDto abDto = getAuftragFac().auftragFindByPrimaryKey(apDto.getBelegIId());

						if (abDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
								|| apDto.getAuftragpositionstatusCNr()
										.equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {

							if (sNichtGeliefert == null) {
								sNichtGeliefert = "<font size='3' face='Monospaced'>";
							}

							sNichtGeliefert += "AB";
							sNichtGeliefert += Helper.fitString2Length(abDto.getCNr(), 15, '\u00A0');
							sNichtGeliefert += Helper.fitString2Length(
									"Pos." + getAuftragpositionFac().getPositionNummer(apDto.getIId()), 20, '\u00A0');
							sNichtGeliefert += Helper.fitString2Length(" " + av.getFlrartikel().getC_nr(), 40,
									'\u00A0');

							sNichtGeliefert += "<br>";
							continue;
						}

						lieferscheinposDto.setAuftragpositionIId(apDto.getIId());
						lieferscheinposDto.setFRabattsatz(apDto.getFRabattsatz());
						lieferscheinposDto.setFZusatzrabattsatz(apDto.getFZusatzrabattsatz());
						lieferscheinposDto.setMwstsatzIId(apDto.getMwstsatzIId());
						lieferscheinposDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

						lieferscheinposDto.setNNettoeinzelpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								apDto.getNNettoeinzelpreis(), abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
								new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNNettoeinzelpreisplusversteckteraufschlag(getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(apDto.getNNettoeinzelpreisplusversteckteraufschlag(),
										abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
										new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
								getLocaleFac().rechneUmInAndereWaehrungZuDatum(
										apDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
										abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
										new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNEinzelpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								apDto.getNEinzelpreis(), abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
								new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNRabattbetrag(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								apDto.getNRabattbetrag(), abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
								new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNMwstbetrag(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								apDto.getNMwstbetrag(), abDto.getCAuftragswaehrung(), lsDto.getWaehrungCNr(),
								new java.sql.Date(lsDto.getTBelegdatum().getTime()), theClientDto));

						lieferscheinposDto.setNBruttoeinzelpreis(
								lieferscheinposDto.getNNettoeinzelpreis().add(apDto.getNMwstbetrag()));
						
						
						//SP8004
						lieferscheinposDto.setBArtikelbezeichnunguebersteuert(apDto.getBArtikelbezeichnunguebersteuert());
						//PJ21321
						lieferscheinposDto.setCBez(apDto.getCBez());
						lieferscheinposDto.setCZusatzbez(apDto.getCZusatzbez());
						

					} else {
						// VK-Preisfindung
						ForecastpositionDto foDto = getForecastFac()
								.forecastpositonFindByPrimaryKeyOhneExc(av.getI_belegartpositionid());

						if (foDto == null) {
							continue;
						}

						ForecastauftragDto fcaDto = getForecastFac()
								.forecastauftragFindByPrimaryKey(foDto.getForecastauftragIId());

						if (foDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
								|| fcaDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {

							ForecastDto fDto = getForecastFac().forecastFindByPrimaryKeyOhneExc(av.getI_belegartid());

							if (sNichtGeliefert == null) {
								sNichtGeliefert = "<font size='3' face='Monospaced'>";
							}

							sNichtGeliefert += "FC ";
							sNichtGeliefert += Helper.fitString2Length(fDto.getCNr(), 14, '\u00A0');
							sNichtGeliefert += Helper.fitString2Length(
									Helper.formatTimestamp(fcaDto.getTAnlegen(), theClientDto.getLocUi()), 20,
									'\u00A0');
							sNichtGeliefert += Helper.fitString2Length(" " + av.getFlrartikel().getC_nr(), 40,
									'\u00A0');

							sNichtGeliefert += "<br>";
							continue;
						}

						lieferscheinposDto.setForecastpositionIId(foDto.getIId());

						lieferscheinposDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
						if (bPositionsKontierung) {
							if (mwstsatzDtoAktuell.getFMwstsatz().doubleValue() != 0) {
/*								
								MwstsatzDto mwstsatzAktuell = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
										aDto.getMwstsatzbezIId(), theClientDto);
*/
								MwstsatzDto mwstsatzAktuell = mwstsatzCache
										.getValueOfKey(aDto.getMwstsatzbezIId());
								lieferscheinposDto.setMwstsatzIId(mwstsatzAktuell.getIId());
							}
						}

						BigDecimal preis = BigDecimal.ZERO;
						VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(aDto.getIId(),
								av.getFlrkunde().getI_id(), av.getN_menge(), new Date(System.currentTimeMillis()),
								av.getFlrkunde().getVkpfartikelpreisliste_i_id_stdpreisliste(),
								lieferscheinposDto.getMwstsatzIId(), lsDto.getWaehrungCNr(), bMitMaterialzuschlag,
								theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
						if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
							preis = kundenVKPreisDto.nettopreis;
						}

						if (kundenVKPreisDto != null) {
							lieferscheinposDto.setNEinzelpreis(kundenVKPreisDto.einzelpreis);
							lieferscheinposDto.setFRabattsatz(kundenVKPreisDto.rabattsatz);

							if (kundenVKPreisDto.bdMaterialzuschlag != null && bMitMaterialzuschlag) {
								lieferscheinposDto.setNNettoeinzelpreis(preis.add(kundenVKPreisDto.bdMaterialzuschlag));
							} else {
								lieferscheinposDto.setNNettoeinzelpreis(preis);
							}
							lieferscheinposDto.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);
							lieferscheinposDto.setFZusatzrabattsatz(kundenVKPreisDto.getDdZusatzrabattsatz());
							BigDecimal bdRabattbetrag = lieferscheinposDto.getNEinzelpreis()
									.multiply(new BigDecimal(lieferscheinposDto.getFRabattsatz().doubleValue()))
									.movePointLeft(2);
							lieferscheinposDto.setNRabattbetrag(bdRabattbetrag);

							lieferscheinposDto.setNBruttoeinzelpreis(kundenVKPreisDto.bruttopreis);
							lieferscheinposDto.setNMwstbetrag(kundenVKPreisDto.mwstsumme);
							lieferscheinposDto.setBNettopreisuebersteuert(
									Helper.boolean2Short(!kundenVKPreisDto.bKommtVonFixpreis));
						} else {
							myLogger.warn("Fuer Artikel-Id '" + aDto.getIId() + "' konnte fuer Kunde '"
									+ av.getFlrkunde().getFlrpartner().getC_kbez() + "' (id="
									+ av.getFlrkunde().getI_id() + ") kein Preis ermittelt werden");
							lieferscheinposDto.setNEinzelpreis(preis);
							lieferscheinposDto.setNNettoeinzelpreis(preis);
							lieferscheinposDto.setNBruttoeinzelpreis(preis);
							lieferscheinposDto.setNMwstbetrag(preis);
							lieferscheinposDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							lieferscheinposDto.setFRabattsatz(0D);
							lieferscheinposDto.setFZusatzrabattsatz(0D);
						}
					}

					lieferscheinposDto.setArtikelIId(aDto.getIId());
					lieferscheinposDto.setEinheitCNr(aDto.getEinheitCNr());
					
					
					

					try {

						// PJ20146
						if (av.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
							AuftragpositionDto apDto = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKeyOhneExc(av.getI_belegartpositionid());

							if (apDto == null) {
								continue;
							}

							Query queryVorh = em
									.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdAuftragpositionIId");
							queryVorh.setParameter(1, lieferscheinIId);
							queryVorh.setParameter(2, av.getI_belegartpositionid());
							Collection<?> cl = queryVorh.getResultList();

							// Wenn es bereits eine Auftragsbezogene LS-Position
							// gibt, dann Menge dazuaddieren, damit beim
							// Zubuchen im andweren Mandanten keine UK-Exception
							// in der WEPos passiert
							if (cl.size() > 0) {

								Lieferscheinposition lspos = (Lieferscheinposition) cl.iterator().next();

								LieferscheinpositionDto lsPosDtoVorhanden = getLieferscheinpositionFac()
										.lieferscheinpositionFindByPrimaryKey(lspos.getIId(), theClientDto);
								lsPosDtoVorhanden.setNMenge(lsPosDtoVorhanden.getNMenge().add(av.getN_menge()));
								getLieferscheinpositionFac().updateLieferscheinposition(lsPosDtoVorhanden,
										theClientDto);
								removeAusliefervorschlag(ausliefervorschlagFindByPrimaryKey(av.getI_id()));
								continue;
							}

						}

						getLieferscheinpositionFac().createLieferscheinposition(lieferscheinposDto, true, theClientDto);
						removeAusliefervorschlag(ausliefervorschlagFindByPrimaryKey(av.getI_id()));

						// PJ19779
						getLieferscheinpositionFac().sortiereNachAuftragsnummer(lieferscheinposDto.getLieferscheinIId(),
								theClientDto);
					} catch (EJBExceptionLP e) {
						if (e.getCode() == EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) {
							continue;
						} else {
							throw e;
						}
					}

				} else {
					// auslassen
				}

			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		return sNichtGeliefert;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAusliefervorschlag(Integer kundeIId, Integer kundeIId_Lieferadresse, int iSortierung,
			TheClientDto theClientDto) {
		JasperPrintLP oPrintO = null;
		sAktuellerReport = REPORT_AUSLIEFERVORSCHLAG;
		int iAnzahlZeilen = 0;
		Locale locDruck = theClientDto.getLocUi();

		ArrayList<FLRBestellvorschlag> bestellung = new ArrayList();

		// die Parameter dem Report uebergeben
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		mapParameter.put("P_SUBREPORT_LETZTE_EINSTELLUNGEN", getSystemServicesFac().getSubreportLetzteEinstellungen(
				SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_AUSLIEFERVORSCHLAG + theClientDto.getMandant()));

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {

			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRAusliefervorschlag.class);

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			if (kundeIId != null) {
				crit.add(Restrictions.eq("kunde_i_id", kundeIId));

				mapParameter.put("P_KUNDE", getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto()
						.formatFixName1Name2());

			}
			if (kundeIId_Lieferadresse != null) {
				crit.add(Restrictions.eq("kunde_i_id_lieferadresse", kundeIId_Lieferadresse));

				mapParameter.put("P_LIEFERADRESSE", getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto)
						.getPartnerDto().formatFixName1Name2());
			}

			Criteria critArtikel = crit.createCriteria("flrartikel");

			if (iSortierung == SORT_AUSLIEFERVORSCHLAG_ARTIKEL) {
				critArtikel.addOrder(Order.asc("c_nr"));

				mapParameter.put("P_SORTIERUNG", getTextRespectUISpr("ls.ausliefervorschlag.report.sort.artikel",
						theClientDto.getMandant(), theClientDto.getLocUi()));

			} else {

				Criteria critPartnerKunde = crit.createCriteria("flrkunde").createCriteria("flrpartner");
				Criteria critPartnerLieferadresse = crit.createCriteria("flrkunde_lieferadresse")
						.createCriteria("flrpartner");

				critPartnerKunde.addOrder(Order.asc("c_name1nachnamefirmazeile1"));
				critPartnerLieferadresse.addOrder(Order.asc("c_name1nachnamefirmazeile1"));

				mapParameter.put("P_SORTIERUNG",
						getTextRespectUISpr("ls.ausliefervorschlag.report.sort.kundelieferadresse",
								theClientDto.getMandant(), theClientDto.getLocUi()));

			}

			crit.addOrder(Order.asc("t_ausliefertermin"));
			List<?> list = crit.list();
			Iterator<?> it = list.iterator();

			data = new Object[list.size()][REPORT_AUSLIEFERVORSCHLAG_ANZAHL_SPALTEN];

			int i = 0;
			while (it.hasNext()) {
				FLRAusliefervorschlag flrAv = (FLRAusliefervorschlag) it.next();
				data[i][REPORT_AUSLIEFERVORSCHLAG_BELEGART] = flrAv.getBelegart_c_nr();
				String belegCNr = "";

				if (flrAv.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(flrAv.getI_belegartid());

					if (aDto != null) {
						belegCNr = aDto.getCNr();
					}
				} else if (flrAv.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
					ForecastDto fDto = getForecastFac().forecastFindByPrimaryKeyOhneExc(flrAv.getI_belegartid());
					if (fDto != null) {
						belegCNr = fDto.getCNr();
					}
				}

				data[i][REPORT_AUSLIEFERVORSCHLAG_BELEGNUMMER] = belegCNr;

				data[i][REPORT_AUSLIEFERVORSCHLAG_AUSLIEFERTERMIN] = flrAv.getT_ausliefertermin();

				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELCNR] = flrAv.getFlrartikel().getC_nr();

				data[i][REPORT_AUSLIEFERVORSCHLAG_MENGE] = flrAv.getN_menge();
				data[i][REPORT_AUSLIEFERVORSCHLAG_VERFUEGBARE_MENGE] = flrAv.getN_verfuegbar();
				ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(flrAv.getFlrartikel().getI_id(),
						theClientDto);
				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELBEZ] = oArtikelDto.getArtikelsprDto().getCBez();
				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELKBEZ] = oArtikelDto.getArtikelsprDto().getCKbez();
				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ] = oArtikelDto.getArtikelsprDto().getCZbez();
				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELZBEZ2] = oArtikelDto.getArtikelsprDto().getCZbez2();

				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELGEWICHT] = oArtikelDto.getFGewichtkg();
				data[i][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTELMENGE] = oArtikelDto.getNVerpackungsmittelmenge();
				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELGEWICHT] = oArtikelDto.getFGewichtkg();

				if (oArtikelDto.getVerpackungsmittelIId() != null) {
					VerpackungsmittelDto verpackungsmittelDto = getArtikelFac().verpackungsmittelFindByPrimaryKey(
							oArtikelDto.getVerpackungsmittelIId(),

							theClientDto);
					data[i][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_KENNUNG] = verpackungsmittelDto.getCNr();

					data[i][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_BEZEICHNUNG] = verpackungsmittelDto
							.getBezeichnung();

					data[i][REPORT_AUSLIEFERVORSCHLAG_VERPACKUNGSMITTEL_GEWICHT_IN_KG] = verpackungsmittelDto
							.getNGewichtInKG();
				}

				BigDecimal nLagersoll = new BigDecimal(0);
				BigDecimal nLagermindest = new BigDecimal(0);
				BigDecimal nAnzahlArtikelRes = new BigDecimal(0);
				BigDecimal nFehlMenge = new BigDecimal(0);
				if (oArtikelDto.getFLagermindest() != null) {
					nLagermindest = new BigDecimal(oArtikelDto.getFLagermindest());
				}
				data[i][REPORT_AUSLIEFERVORSCHLAG_LAGERMINDESTSTAND] = nLagermindest;
				if (oArtikelDto.getFLagersoll() != null) {
					nLagersoll = new BigDecimal(oArtikelDto.getFLagersoll());
				}
				data[i][REPORT_AUSLIEFERVORSCHLAG_LAGERSOLL] = nLagersoll;

				// PJ 14828

				BigDecimal bdLagerstand = new BigDecimal(0);

				if (Helper.short2Boolean(flrAv.getFlrartikel().getB_lagerbewirtschaftet())) {
					bdLagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(flrAv.getFlrartikel().getI_id(),
							theClientDto);
				}

				data[i][REPORT_AUSLIEFERVORSCHLAG_LAGERSTAND] = bdLagerstand;

				data[i][REPORT_AUSLIEFERVORSCHLAG_ARTIKELEINHEIT] = flrAv.getFlrartikel().getEinheit_c_nr();

				nAnzahlArtikelRes = getReservierungFac().getAnzahlReservierungen(flrAv.getArtikel_i_id(), theClientDto);
				data[i][REPORT_AUSLIEFERVORSCHLAG_RESERVIERUNG] = nAnzahlArtikelRes;
				nFehlMenge = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(flrAv.getArtikel_i_id(), theClientDto);
				data[i][REPORT_AUSLIEFERVORSCHLAG_FEHLMENGE] = nFehlMenge;

				// Kunde
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE] = HelperServer
						.formatNameAusFLRPartner(flrAv.getFlrkunde().getFlrpartner());
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID] = flrAv.getFlrkunde().getFlrpartner().getI_id();
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME1] = flrAv.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME2] = flrAv.getFlrkunde().getFlrpartner()
						.getC_name2vornamefirmazeile2();
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_NAME3] = flrAv.getFlrkunde().getFlrpartner()
						.getC_name3vorname2abteilung();

				if (flrAv.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
					data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_LKZ] = flrAv.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();

					data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_ORT] = flrAv.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name();
					data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_PLZ] = flrAv.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getC_plz();
				}
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_STRASSE] = flrAv.getFlrkunde().getFlrpartner().getC_strasse();

				// Lieferadresse
				data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE] = HelperServer
						.formatNameAusFLRPartner(flrAv.getFlrkunde_lieferadresse().getFlrpartner());
				data[i][REPORT_AUSLIEFERVORSCHLAG_KUNDE_I_ID_LIEFERADRESSE] = flrAv.getFlrkunde_lieferadresse()
						.getFlrpartner().getI_id();
				data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME1] = flrAv.getFlrkunde_lieferadresse()
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME2] = flrAv.getFlrkunde_lieferadresse()
						.getFlrpartner().getC_name2vornamefirmazeile2();
				data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_NAME3] = flrAv.getFlrkunde_lieferadresse()
						.getFlrpartner().getC_name3vorname2abteilung();

				if (flrAv.getFlrkunde_lieferadresse().getFlrpartner().getFlrlandplzort() != null) {
					data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_LKZ] = flrAv.getFlrkunde_lieferadresse()
							.getFlrpartner().getFlrlandplzort().getFlrland().getC_lkz();

					data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_ORT] = flrAv.getFlrkunde_lieferadresse()
							.getFlrpartner().getFlrlandplzort().getFlrort().getC_name();
					data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_PLZ] = flrAv.getFlrkunde_lieferadresse()
							.getFlrpartner().getFlrlandplzort().getC_plz();
				}
				data[i][REPORT_AUSLIEFERVORSCHLAG_LIEFERADRESSE_STRASSE] = flrAv.getFlrkunde_lieferadresse()
						.getFlrpartner().getC_strasse();

				i++;
			}

			initJRDS(mapParameter, REPORT_MODUL, REPORT_AUSLIEFERVORSCHLAG, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			oPrintO = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return oPrintO;
	}

	public void verfuegbareMengeBerechnen(TheClientDto theClientDto) {

		String sQuery = "SELECT ausliefervorschlag FROM FLRAusliefervorschlag ausliefervorschlag WHERE ausliefervorschlag.mandant_c_nr = '"
				+ theClientDto.getMandant()
				+ "' ORDER BY ausliefervorschlag.flrartikel.c_nr ASC, ausliefervorschlag.t_ausliefertermin ASC, ausliefervorschlag.belegart_c_nr DESC, ausliefervorschlag.i_belegartid ASC,  ausliefervorschlag.i_id";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		org.hibernate.Query query = session.createQuery(sQuery);

		List list = query.list();
		Iterator it = list.iterator();

		HashMap<Integer, HashMap<Integer, BigDecimal>> hmArtikel = new HashMap<Integer, HashMap<Integer, BigDecimal>>();

		while (it.hasNext()) {
			FLRAusliefervorschlag ausliefervorschlag = (FLRAusliefervorschlag) it.next();

			HashMap<Integer, BigDecimal> hmLagerUndLagerstaende = null;
			if (!hmArtikel.containsKey(ausliefervorschlag.getArtikel_i_id())) {
				hmLagerUndLagerstaende = new HashMap<Integer, BigDecimal>();

			} else {
				hmLagerUndLagerstaende = hmArtikel.get(ausliefervorschlag.getArtikel_i_id());
			}

			Integer lagerIId = ausliefervorschlag.getFlrkunde_lieferadresse().getLager_i_id_abbuchungslager();

			try {
				if (!hmLagerUndLagerstaende.containsKey(lagerIId)) {

					if (Helper.short2boolean(ausliefervorschlag.getFlrartikel().getB_lagerbewirtschaftet())) {
						hmLagerUndLagerstaende.put(lagerIId, getLagerFac()
								.getLagerstand(ausliefervorschlag.getArtikel_i_id(), lagerIId, theClientDto));
					} else {
						hmLagerUndLagerstaende.put(lagerIId, new BigDecimal(99999999));
					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			BigDecimal bdVerfuegbar = hmLagerUndLagerstaende.get(lagerIId);

			Ausliefervorschlag av = em.find(Ausliefervorschlag.class, ausliefervorschlag.getI_id());
			av.setNVerfuegbar(bdVerfuegbar);

			bdVerfuegbar = bdVerfuegbar.subtract(ausliefervorschlag.getN_menge());

			hmLagerUndLagerstaende.put(lagerIId, bdVerfuegbar);

			hmArtikel.put(ausliefervorschlag.getArtikel_i_id(), hmLagerUndLagerstaende);

		}
	}

	public Set<Integer> getKundenIIsEinesAusliefervorschlages(Set<Integer> iids, boolean bLieferadresse,
			TheClientDto theClientDto) {

		Set<Integer> kundenIIds = new HashSet<Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT av FROM FLRAusliefervorschlag av WHERE av.mandant_c_nr='" + theClientDto.getMandant()
				+ "'";

		if (iids != null) {

			sQuery += "	AND av.i_id IN(";

			Iterator it = iids.iterator();

			while (it.hasNext()) {
				sQuery += it.next();

				if (it.hasNext()) {
					sQuery += ",";
				}
			}

			sQuery += ")";

		}

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> list = query.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			FLRAusliefervorschlag av = (FLRAusliefervorschlag) it.next();
			if (bLieferadresse) {
				kundenIIds.add(av.getKunde_i_id_lieferadresse());
			} else {
				kundenIIds.add(av.getKunde_i_id());
			}
		}

		return kundenIIds;
	}

	public void erstelleAusliefervorschlag(java.sql.Date dAusliefertermin, TheClientDto theClientDto) {
		long lStart = System.currentTimeMillis();
		boolean bAuftragsfreigabe = false;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
			bAuftragsfreigabe = ((Boolean) parameter.getCWertAsObject());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Einstellungen speichern
		ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();
		KeyvalueDto kvDto = new KeyvalueDto("BisAusliefertermin",
				Helper.formatDatum(dAusliefertermin, theClientDto.getLocUi()));
		alDtos.add(kvDto);

		kvDto = new KeyvalueDto("ErzeugtAm",
				Helper.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()), theClientDto.getLocUi()));
		alDtos.add(kvDto);
		kvDto = new KeyvalueDto("ErzeugtVon", getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).getCKurzzeichen());
		alDtos.add(kvDto);

		loescheAusliefervorlaegeEinesMandaten(theClientDto);

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		Integer tagesartIId_Betriebsurlaub = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, theClientDto).getIId();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dAusliefertermin = Helper.cutDate(dAusliefertermin);

		session = factory.openSession();
		Criteria crit = session.createCriteria(FLRAuftragpositionReport.class);

		crit.createAlias("flrauftrag", "a");

		// Einschraenkung auf den aktuellen Mandanten
		crit.add(Restrictions.eq("a." + AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, theClientDto.getMandant()));
		// Einschraenkung nach Auftragart

		crit.add(Restrictions.ne(AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR,
				AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT));
		crit.add(Restrictions.ne("auftragpositionart_c_nr",
				AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME));
		crit.add(Restrictions.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));

		// SP4977 Ohne Rahmenauftraege
		crit.add(Restrictions.ne("a." + AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, AuftragServiceFac.AUFTRAGART_RAHMEN));

		// Einschraenkung nach Status Offen, Teilerledigt
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

		cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
		crit.add(Restrictions.in("a." + AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

		// Sortierung nach Kunde, Lieferadresse, Artikel,Termin ASC

		crit.createCriteria("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE).createCriteria(KundeFac.FLR_PARTNER)
				.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

		crit.createCriteria("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE).createCriteria(KundeFac.FLR_PARTNER)
				.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1))
				.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2))
				.addOrder(Order.asc(PartnerFac.FLR_PARTNER_C_NAME3VORNAME2ABTEILUNG));

		crit.addOrder(Order.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));

		crit.addOrder(Order.asc("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));

		// es wird in jedem Fall nach der Belegnummer sortiert
		// crit.addOrder(Order.asc("c_nr"));

		List<?> list = crit.list();
		Iterator<?> it = list.iterator();

		Integer searchItemId = new Integer(11424);
		while (it.hasNext()) {
			FLRAuftragpositionReport item = (FLRAuftragpositionReport) it.next();
			FLRAuftragReport flrauftrag = item.getFlrauftrag();

			if (bAuftragsfreigabe == true && flrauftrag.getT_auftragsfreigabe() == null) {
				continue;
			}

			
			Timestamp dAuslieferdatumZeile = umKundenlieferdauerVersetzen(theClientDto, tagesartIId_Feiertag,
					tagesartIId_Halbtag, tagesartIId_Betriebsurlaub,
					new java.sql.Date(item.getT_uebersteuerterliefertermin().getTime()),
					item.getFlrauftrag().getFlrkundelieferadresse().getI_lieferdauer());

			if (dAusliefertermin.getTime() >= dAuslieferdatumZeile.getTime() && item.getN_menge().doubleValue() > 0
					&& item.getN_offenemenge().doubleValue() > 0) {

				if(searchItemId.equals(item.getArtikel_i_id())) {
					myLogger.warn("Item 29396_00");
				}

				AusliefervorschlagDto ausliefervorschlagDto = new AusliefervorschlagDto();

				ausliefervorschlagDto.setMandantCNr(flrauftrag.getMandant_c_nr());

				ausliefervorschlagDto.setArtikelIId(item.getArtikel_i_id());
				ausliefervorschlagDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
				ausliefervorschlagDto.setIBelegartid(flrauftrag.getI_id());
				ausliefervorschlagDto.setIBelegartpositionid(item.getI_id());
				ausliefervorschlagDto.setKundeIId(flrauftrag.getKunde_i_id_auftragsadresse());
				ausliefervorschlagDto.setKundeIIdLieferadresse(flrauftrag.getKunde_i_id_lieferadresse());

				ausliefervorschlagDto.setNMenge(item.getN_offenemenge());

				ausliefervorschlagDto.setNVerfuegbar(BigDecimal.ZERO);
				ausliefervorschlagDto.setTAusliefertermin(new java.sql.Timestamp(dAuslieferdatumZeile.getTime()));

				createAusliefervorschlag(ausliefervorschlagDto, theClientDto);

			}

		}
		session.close();

		// Daily/Weekly Call-Offs holen

		session = factory.openSession();

		String sQuery = "SELECT fp FROM FLRForecastposition fp WHERE  fp.flrforecastauftrag.status_c_nr='"
				+ LocaleFac.STATUS_FREIGEGEBEN
				+ "' AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.status_c_nr='" + LocaleFac.STATUS_ANGELEGT
				// SP6320
				+ "' AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND fp.flrforecastauftrag.t_freigabe IS NOT NULL AND fp.status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT + "' AND fp.flrartikel.b_kommissionieren=0 ";

		org.hibernate.Query query = session.createQuery(sQuery);

		list = query.list();
		it = list.iterator();

		while (it.hasNext()) {
			FLRForecastposition item = (FLRForecastposition) it.next();

			// nur Daily/Weekly Call-Offs

			String forecastartcNr = HelperServer.getForecastartEienrForecastposition(item);

			if (forecastartcNr != null && (forecastartcNr.equals(ForecastFac.FORECASTART_CALL_OFF_TAG)
					|| forecastartcNr.equals(ForecastFac.FORECASTART_CALL_OFF_WOCHE))) {

				java.sql.Date dForecastDatum = new java.sql.Date(item.getT_termin().getTime());

				int iKundenlieferdauer = item.getFlrforecastauftrag().getFlrfclieferadresse()
						.getFlrkunde_lieferadresse().getI_lieferdauer();

				Timestamp dAuslieferdatumZeile = umKundenlieferdauerVersetzen(theClientDto, tagesartIId_Feiertag,
						tagesartIId_Halbtag, tagesartIId_Betriebsurlaub, dForecastDatum, iKundenlieferdauer);

				if (dAusliefertermin.getTime() >= dAuslieferdatumZeile.getTime() && item.getN_menge().signum() > 0) {

					BigDecimal gelieferteMenge = getForecastFac().getBereitsGelieferteMenge(item.getI_id());
					if (gelieferteMenge.compareTo(item.getN_menge()) < 0) {

						// if (!getForecastFac()
						// .sindBereitsLieferscheinpositionenVorhanden(
						// item.getI_id())) {

						// PJ19777 Nur wenn noch keine Linienabrufe vorhanden
						// sind

						BigDecimal bdSummeLinienabrufe = getForecastFac().getSummeLinienabrufe(item.getI_id());
						if (bdSummeLinienabrufe == null) {
							AusliefervorschlagDto ausliefervorschlagDto = new AusliefervorschlagDto();

							ausliefervorschlagDto.setMandantCNr(item.getFlrforecastauftrag().getFlrfclieferadresse()
									.getFlrforecast().getFlrkunde().getMandant_c_nr());

							ausliefervorschlagDto.setArtikelIId(item.getFlrartikel().getI_id());
							ausliefervorschlagDto.setBelegartCNr(LocaleFac.BELEGART_FORECAST);
							ausliefervorschlagDto.setIBelegartid(
									item.getFlrforecastauftrag().getFlrfclieferadresse().getFlrforecast().getI_id());
							ausliefervorschlagDto.setIBelegartpositionid(item.getI_id());
							ausliefervorschlagDto.setKundeIId(item.getFlrforecastauftrag().getFlrfclieferadresse()
									.getFlrforecast().getFlrkunde().getI_id());
							ausliefervorschlagDto.setKundeIIdLieferadresse(item.getFlrforecastauftrag()
									.getFlrfclieferadresse().getFlrkunde_lieferadresse().getI_id());
							ausliefervorschlagDto.setNMenge(item.getN_menge().subtract(gelieferteMenge));
							ausliefervorschlagDto.setNVerfuegbar(BigDecimal.ZERO);
							ausliefervorschlagDto
									.setTAusliefertermin(new java.sql.Timestamp(dAuslieferdatumZeile.getTime()));

							createAusliefervorschlag(ausliefervorschlagDto, theClientDto);
						}
					}

				}
			}

		}

		verfuegbareMengeBerechnen(theClientDto);
		alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
		getSystemServicesFac().replaceKeyvaluesEinerGruppe(
				SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_AUSLIEFERVORSCHLAG + theClientDto.getMandant(),
				alDtos);
	}

	public Timestamp umKundenlieferdauerVersetzen(TheClientDto theClientDto, java.sql.Date dEintrefftermin,
			int kundenlieferdauer) {
		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		Integer tagesartIId_Betriebsurlaub = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, theClientDto).getIId();
		return umKundenlieferdauerVersetzen(theClientDto, tagesartIId_Feiertag, tagesartIId_Halbtag,
				tagesartIId_Betriebsurlaub, dEintrefftermin, kundenlieferdauer);
	}

	public Timestamp umKundenlieferdauerVersetzen(TheClientDto theClientDto, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, Integer tagesartIId_Betriebsurlaub, java.sql.Date dEintrefftermin,
			int kundenlieferdauer) {

		Calendar cAusliefertermin = Calendar.getInstance();
		cAusliefertermin.setTimeInMillis(dEintrefftermin.getTime());

		cAusliefertermin.add(Calendar.DATE, -kundenlieferdauer);

		// PJ19929

		// Ausliefertermin = Eintrefftermin - Kundenlieferdauer.
		// Wenn der daraus resulierende Termin dann auf einen
		// Samstag/Sonntag/Feiertag faellt, dann wird solange um eins
		// verschoben,
		// bis kein Samstag/sonntag/Feiertag

		int iAzahlTageSchonVersetzt = 0;

		while (iAzahlTageSchonVersetzt < 999) {

			boolean bArbeitstag = true;

			if (cAusliefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
					|| cAusliefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				bArbeitstag = false;
			} else {

				String sQuery = "SELECT bk.flrtagesart.i_id FROM FLRBetriebskalender bk WHERE bk.mandant_c_nr='"
						+ theClientDto.getMandant() + "' AND bk.t_datum='"
						+ Helper.formatDateWithSlashes(new java.sql.Date(cAusliefertermin.getTimeInMillis())) + "'";

				Session session = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Query bk = session.createQuery(sQuery);

				bk.setMaxResults(1);

				List<?> resultList = bk.list();

				Iterator<?> resultListIterator = resultList.iterator();

				if (resultListIterator.hasNext()) {
					Integer tagesartIId = (Integer) resultListIterator.next();
					if (tagesartIId.equals(tagesartIId_Feiertag) || tagesartIId.equals(tagesartIId_Halbtag)
							|| tagesartIId.equals(tagesartIId_Betriebsurlaub)) {
						bArbeitstag = false;
					}
				}
				session.close();

			}

			if (bArbeitstag == false) {
				cAusliefertermin.add(Calendar.DAY_OF_MONTH, -1);
				iAzahlTageSchonVersetzt++;
			} else {
				break;
			}

		}
		return new Timestamp(cAusliefertermin.getTime().getTime());

	}

	public void removeAusliefervorschlag(AusliefervorschlagDto ausliefervorschlagDto) {

		Ausliefervorschlag toRemove = em.find(Ausliefervorschlag.class, ausliefervorschlagDto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeLockDesAusliefervorschlagesWennIchIhnSperre(TheClientDto theClientDto) throws EJBExceptionLP {
		try {

			String mandant = theClientDto.getMandant();

			LockMeDto[] lockMeDtoLock = getTheJudgeFac()
					.findByWerWasOhneExc(AusliefervorschlagFac.LOCKME_AUSLIEFERVORSCHLAG_TP, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					getTheJudgeFac().removeLockedObject(lockMeDtoLock[0]);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void pruefeBearbeitenDesAusliefervorschlagsErlaubt(TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			lockMeDto.setCWas(mandant);
			lockMeDto.setCWer(AusliefervorschlagFac.LOCKME_AUSLIEFERVORSCHLAG_TP);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac()
					.findByWerWasOhneExc(AusliefervorschlagFac.LOCKME_AUSLIEFERVORSCHLAG_TP, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUSLIEFERVORSCHLAG_IST_GESPERRT, al,
							new Exception("Ausliefervorschlag auf Mandant " + mandant + " gesperrt durch Personal Id "
									+ lockMeDtoLock[0].getPersonalIIdLocker()));
				}
			} else {
				// dann sperren
				lockMeDto.setPersonalIIdLocker(theClientDto.getIDPersonal());
				getTheJudgeFac().addLockedObject(lockMeDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateAusliefervorschlag(AusliefervorschlagDto ausliefervorschlagDto, TheClientDto theClientDto) {
		Integer iId = ausliefervorschlagDto.getIId();
		Ausliefervorschlag ausliefervorschlag = em.find(Ausliefervorschlag.class, iId);
		setAusliefervorschlagFromAusliefervorschlagDto(ausliefervorschlag, ausliefervorschlagDto);

	}

	private void setAusliefervorschlagFromAusliefervorschlagDto(Ausliefervorschlag bean, AusliefervorschlagDto dto) {
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setBelegartCNr(dto.getBelegartCNr());
		bean.setIBelegartid(dto.getIBelegartid());
		bean.setIBelegartpositionid(dto.getIBelegartpositionid());
		bean.setKundeIId(dto.getKundeIId());
		bean.setKundeIIdLieferadresse(dto.getKundeIIdLieferadresse());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setNMenge(dto.getNMenge());
		bean.setTAusliefertermin(dto.getTAusliefertermin());
		bean.setNVerfuegbar(dto.getNVerfuegbar());

		em.merge(bean);
		em.flush();
	}

}
