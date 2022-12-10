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
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.ejb.Artikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelreservierungDtoAssembler;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplanposition;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.SpeiseplanpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejb.Mandant;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ReservierungFacBean extends LPReport implements ReservierungFac, JRDataSource {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAG = 0;
	private static int REPORT_RESERVIERUNGSLISTE_KUNDENNAME = 1;
	private static int REPORT_RESERVIERUNGSLISTE_PROJEKTNAME = 2;
	private static int REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN = 3;
	private static int REPORT_RESERVIERUNGSLISTE_MENGE = 4;
	private static int REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER = 5;
	private static int REPORT_RESERVIERUNGSLISTE_STUECKLISTE = 6;
	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_ZEITPUNKT = 7;
	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_PERSON = 8;
	private static int REPORT_RESERVIERUNGSLISTE_FORECASTART = 9;
	private static int REPORT_RESERVIERUNGSLISTE_ZUGEHOERIGER_KUNDE_IM_LOS = 10;
	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_BEZ = 11;
	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_ZBEZ = 12;
	private static int REPORT_RESERVIERUNGSLISTE_MANDANT = 13;
	private static int REPORT_RESERVIERUNGSLISTE_FORECAST_BEMERKUNG = 14;
	private static int REPORT_RESERVIERUNGSLISTE_FORECASTPOSITION_I_ID = 15;
	private static int REPORT_RESERVIERUNGSLISTE_ANZAHL_SPALTEN = 16;

	private static int REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MONAT = 0;
	private static int REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_JAHR = 1;
	private static int REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_FREIGEGEBEN = 2;
	private static int REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_GESAMT = 3;
	private static int REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_ANZAHL_SPALTEN = 4;

	/**
	 * Reservierte Menge eines Artikels beim Mandanten ermitteln.
	 * 
	 * @param artikelIId   Integer
	 * @param theClientDto String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */

	public BigDecimal getAnzahlReservierungen(Integer artikelIId, TheClientDto theClientDto) throws EJBExceptionLP {
		return getAnzahlReservierungen(artikelIId, null, theClientDto.getMandant());
	}

	public BigDecimal getAnzahlReservierungen(Integer artikelIId, java.sql.Timestamp tStichtag, String mandantCNr)
			throws EJBExceptionLP {
		return getAnzahlReservierungen(artikelIId, tStichtag, mandantCNr, null);
	}

	public BigDecimal getAnzahlReservierungen(Integer artikelIId, java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort) {
		return getAnzahlReservierungen(artikelIId, tStichtag, mandantCNr, partnerIIdStandort, false, null);
	}
	
	public BigDecimal getAnzahlReservierungen(Integer artikelIId, java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort, boolean bNurInterne, String belegartCNr) {
		return getAnzahlReservierungen(artikelIId, tStichtag, mandantCNr, partnerIIdStandort, bNurInterne, belegartCNr, false);
	}

	public BigDecimal getAnzahlReservierungen(Integer artikelIId, java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort, boolean bNurInterne, String belegartCNr, boolean bAlle) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelIId == null"));
		}
		boolean bAuftragsfreigabe = false;

		BigDecimal bdReserviert = new BigDecimal(0);
		try {

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE);

			bAuftragsfreigabe = ((Boolean) parameterDto.getCWertAsObject());

			// Alle Reservierungen dieses Artikels.

			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "Select ar, (SELECT sa FROM FLRLossollmaterial sa WHERE sa.i_id=ar.i_belegartpositionid AND ar.c_belegartnr = '"
					+ LocaleFac.BELEGART_LOS + "') FROM FLRArtikelreservierung ar WHERE ar.flrartikel.i_id="
					+ artikelIId;
			org.hibernate.Query fQuery = session.createQuery(sQuery);
			List<?> fList = fQuery.list();
			Iterator<?> fListIterator = fList.iterator();
			while (fListIterator.hasNext()) {

				Object[] oTemp = (Object[]) fListIterator.next();

				FLRArtikelreservierung ar = (FLRArtikelreservierung) oTemp[0];
				FLRLossollmaterial sa = (FLRLossollmaterial) oTemp[1];

				if (belegartCNr != null) {
					if (!ar.getC_belegartnr().equals(belegartCNr)) {
						continue;
					}
				}

				if (tStichtag != null) {
					if (tStichtag.getTime() <= ar.getT_liefertermin().getTime()) {
						continue;
					}
				}
				// pruefen, ob sich die Reservierung auf "meinen" Mandanten
				// bezieht.
				if (ar.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					// Auftragsreservierungen
					AuftragpositionDto abPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKeyOhneExc(ar.getI_belegartpositionid());
					// Sicherheitshalber
					if (abPosDto != null) {
						Integer auftragIId = abPosDto.getBelegIId();

						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

						// PJ19570
						if (bAuftragsfreigabe == true) {
							if (auftragDto.getTAuftragsfreigabe() == null) {
								continue;
							}
						}

						if (auftragDto.getMandantCNr().equals(mandantCNr)) {
							// Menge addieren
							if (ar.getN_menge() != null) {

								if (partnerIIdStandort != null) {
									Integer parnterIIdStandortAuftrag = getLagerFac()
											.getPartnerIIdStandortEinesLagers(auftragDto.getLagerIIdAbbuchungslager());
									// Wenn der Stanord ungleich des AbLagers,
									// dann
									// auslassen
									if (parnterIIdStandortAuftrag == null
											|| !parnterIIdStandortAuftrag.equals(partnerIIdStandort)) {
										continue;
									}

								}

								if (bNurInterne) {
									// PJ19760
									boolean bKundeIstEigenerMandant = false;
									KundeDto kdDto = getKundeFac()
											.kundeFindByPrimaryKeySmall(auftragDto.getKundeIIdAuftragsadresse());

									Query queryM = em.createNamedQuery("MandantfindAlleMandanten");
									Collection c = queryM.getResultList();
									Iterator it = c.iterator();
									while (it.hasNext()) {
										Mandant m = (Mandant) it.next();
										if (m.getPartnerIId().equals(kdDto.getPartnerIId())) {
											bKundeIstEigenerMandant = true;
										}

									}

									if (bKundeIstEigenerMandant == true) {
										bdReserviert = bdReserviert.add(ar.getN_menge());
									}

								} else {
									bdReserviert = bdReserviert.add(ar.getN_menge());
								}

							}
						}
					}
				} else if (ar.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					// Losreservierungen
					// Sicherheitshalber
					if (sa != null) {

						if (sa.getFlrlos().getMandant_c_nr().equals(mandantCNr)) {

							if (partnerIIdStandort != null) {

								LoslagerentnahmeDto[] lolaDtos = getFertigungFac()
										.loslagerentnahmeFindByLosIId(sa.getFlrlos().getI_id());

								if (lolaDtos.length > 0) {
									Integer parnterIIdStandortLos = getLagerFac()
											.getPartnerIIdStandortEinesLagers(lolaDtos[0].getLagerIId());
									// Wenn der Standort ungleich des AbLagers,
									// dann
									// auslassen
									if (parnterIIdStandortLos == null
											|| !parnterIIdStandortLos.equals(partnerIIdStandort)) {
										continue;
									}
								} else {
									continue;
								}

							}

							// Menge addieren
							if (ar.getN_menge() != null) {
								bdReserviert = bdReserviert.add(ar.getN_menge());
							}
						}
					}
				} else if (ar.getC_belegartnr().equals(LocaleFac.BELEGART_KUECHE)) {
					// Menge addieren
					if (ar.getN_menge() != null) {
						bdReserviert = bdReserviert.add(ar.getN_menge());
					}
				} else if (ar.getC_belegartnr().equals(LocaleFac.BELEGART_FORECAST)) {

					ForecastpositionDto fcpDto = getForecastFac()
							.forecastpositonFindByPrimaryKeyOhneExc(ar.getI_belegartpositionid());
					if (fcpDto != null && fcpDto.getForecastauftragIId() != null) {

						ForecastauftragDto fcaDto = getForecastFac()
								.forecastauftragFindByPrimaryKey(fcpDto.getForecastauftragIId());
						FclieferadresseDto flDto = getForecastFac()
								.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());

						ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(flDto.getForecastIId());

						KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(fcDto.getKundeIId());

						if (kdDto.getMandantCNr().equals(mandantCNr)) {

							// Menge addieren
							if (ar.getN_menge() != null) {

								// lt. WH: Forecastauftrag und nicht definiert
								// duerfen nicht mitgerechnet werden
								String forecastartCNr = getForecastFac()
										.getForecastartEienrForecastposition(fcpDto.getIId());

								if (forecastartCNr != null
										&& (forecastartCNr.equals(ForecastFac.FORECASTART_CALL_OFF_TAG)
										|| forecastartCNr.equals(ForecastFac.FORECASTART_CALL_OFF_WOCHE)) ) {

									bdReserviert = bdReserviert.add(ar.getN_menge());
								}else if(bAlle) {
									bdReserviert = bdReserviert.add(ar.getN_menge());
								}
								
								
								
							}
						}

					}

				}
			}
		}
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdReserviert;
	}

	public void createArtikelreservierung(ArtikelreservierungDto artikelreservierungDto) {
		createArtikelreservierung(artikelreservierungDto, true);
	}

	public void createArtikelreservierung(ArtikelreservierungDto artikelreservierungDto, boolean bPruefeAufDoppelte)
			throws EJBExceptionLP {

		if (artikelreservierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragreservierungDto == null"));
		}
		if (artikelreservierungDto.getNMenge() == null || artikelreservierungDto.getCBelegartnr() == null
				|| artikelreservierungDto.getIBelegartpositionid() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL, new Exception(
					"artikelreservierungDto.getNMenge() == null || artikelreservierungDto.getBelegartCNr() == null || artikelreservierungDto.getBelegartpositionIId() == null"));
		}

		if (bPruefeAufDoppelte == true) {
			try {
				Query query = em.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
				query.setParameter(1, artikelreservierungDto.getCBelegartnr());
				query.setParameter(2, artikelreservierungDto.getIBelegartpositionid());
				// @todo getSingleResult oder getResultList ?
				Artikelreservierung doppelt = (Artikelreservierung) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_ARTIKELRESERVIERUNG.UK"));
				}
			} catch (NoResultException ex) {

			}
		}
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELRESERVIERUNG);
			artikelreservierungDto.setIId(pk);

			Artikelreservierung artikelreservierung = new Artikelreservierung(
					artikelreservierungDto.getIBelegartpositionid(), artikelreservierungDto.getArtikelIId(),
					artikelreservierungDto.getTLiefertermin(), artikelreservierungDto.getCBelegartnr(),
					artikelreservierungDto.getIId());
			em.persist(artikelreservierung);
			em.flush();
			artikelreservierung.setNMenge(artikelreservierungDto.getNMenge());
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelreservierung(Integer iId) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		try {
			Artikelreservierung artikelreservierung = null;
			// try {
			artikelreservierung = em.find(Artikelreservierung.class, iId);
			if (artikelreservierung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
			em.remove(artikelreservierung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void removeArtikelreservierung(String belegartCNr, Integer belegartpositionIId) throws EJBExceptionLP {

		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartCNr == null || belegartpositionIId == null"));
		}

		try {
			Query query = em.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelreservierung artikelreservierung = (Artikelreservierung) query.getSingleResult();

			em.remove(artikelreservierung);
			em.flush();
		} catch (NoResultException ex) {
			// Wenn es keine Reservierung gibt - Beispielsweise 0 Menge - dann
			// ists ja gut
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateArtikelreservierung(ArtikelreservierungDto artikelreservierungDto) throws EJBExceptionLP {
		if (artikelreservierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragreservierungDto == null"));

		}
		if (artikelreservierungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("auftragreservierungDto.getIId() == null"));

		}
		if (artikelreservierungDto.getIBelegartpositionid() == null
				|| artikelreservierungDto.getCBelegartnr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelreservierungDto.getBelegartpositionIId() == null || artikelreservierungDto.getBelegartCNr() == null"));

		}
		if (artikelreservierungDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("auftragreservierungDto.getNMenge() == null"));
		}
		// try {
		Artikelreservierung auftragreservierung = em.find(Artikelreservierung.class, artikelreservierungDto.getIId());
		if (auftragreservierung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		auftragreservierung.setNMenge(artikelreservierungDto.getNMenge());
		auftragreservierung.setArtikelIId(artikelreservierungDto.getArtikelIId());
		auftragreservierung.setTLiefertermin(artikelreservierungDto.getTLiefertermin());
		auftragreservierung.setCBelegartnr(artikelreservierungDto.getCBelegartnr());
		auftragreservierung.setIBelegartpositionid(artikelreservierungDto.getIBelegartpositionid());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ArtikelreservierungDto artikelreservierungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Artikelreservierung artikelreservierung = em.find(Artikelreservierung.class, iId);
		if (artikelreservierung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		return assembleArtikelreservierungDto(artikelreservierung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIId(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP {
		ArtikelreservierungDto resDto = artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(belegartCNr,
				belegartpositionIId);
		if (resDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new NoResultException());
		}
		return resDto;
	}

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP {
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelreservierung artikelreservierung = (Artikelreservierung) query.getSingleResult();
			return assembleArtikelreservierungDto(artikelreservierung);
		} catch (NoResultException e) {
			myLogger.warn("belegartCNr=" + belegartCNr + ", belegartpositionIId=" + belegartpositionIId);
			return null;
		}
	}

	private ArtikelreservierungDto assembleArtikelreservierungDto(Artikelreservierung artikelreservierung) {
		return ArtikelreservierungDtoAssembler.createDto(artikelreservierung);
	}

	/**
	 * servertransact: Wenn innerhalb einer Transaktion geprueft werden soll, ob es
	 * eine Auftragreservierung gibt, dann darf durch diese Pruefung keine Exception
	 * ausgeloest werden, das fuehrt zu einem Rollback auch wenn die Exception
	 * abgefangen wird.
	 * 
	 * @param iIdBelegartpositionI pk der Position
	 * @param belegartCNr          Belegart
	 * @throws EJBExceptionLP
	 * @return AuftragreservierungDto die Position, wenn es keine Reservierung gibt
	 *         null
	 */
	public ArtikelreservierungDto getArtikelreservierung(String belegartCNr, Integer iIdBelegartpositionI)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdBelegartpositionI == null || belegartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBelegartpositionI == null || belegartCNr == null"));
		}

		ArtikelreservierungDto oReservierungDto = null;
		try {
			oReservierungDto = artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(belegartCNr,
					iIdBelegartpositionI);
		} catch (Throwable t) {
			// es gibt keine Reservierung, es wird null zurueckgegeben
		}
		return oReservierungDto;
	}

	/**
	 * Reservierungen pruefen. 1. Bestehende Reservierungseintraege aus Auftrag/Los
	 * 2. Lose pruefen, ob die Reservierungen richtig eingetragen sind. 3. Auftraege
	 * pruefen, ob die Reservierungen richtig eingetragen sind.
	 * 
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void pruefeReservierungenPerSQL(TheClientDto theClientDto) {

		// ACHTUNG bei Aenderung der Reservierungslogik auch immer die Methode
		// pruefeReservierungen() anpassen

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery1 = "DELETE FROM WW_ARTIKELRESERVIERUNG";
		String sQuery2 = "INSERT INTO WW_ARTIKELRESERVIERUNG (I_ID, C_BELEGARTNR, I_BELEGARTPOSITIONID,ARTIKEL_I_ID,T_LIEFERTERMIN,N_MENGE) "
				+ "SELECT I_ID, C_BELEGARTNR, I_BELEGARTPOSITIONID,ARTIKEL_I_ID,T_LIEFERTERMIN,N_MENGE "
				+ "FROM WW_ARTIKELRESERVIERUNG_PRUEFFUNKTION";

		int i = session.createSQLQuery(sQuery1).executeUpdate();
		int i2 = session.createSQLQuery(sQuery2).executeUpdate();
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(7500)
	public void pruefeReservierungen(TheClientDto theClientDto) throws EJBExceptionLP {

		// ACHTUNG bei Aenderung der Reservierungslogik auch immer die Methode
		// pruefeReservierungenPerSQL() anpassen

		Session session = FLRSessionFactory.getFactory().openSession();

		String hqlDelete = "delete FROM FLRArtikelreservierung";
		session.createQuery(hqlDelete).executeUpdate();

		session.close();

		// Lose
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria lossollmaterial = session.createCriteria(FLRLossollmaterial.class);
		lossollmaterial.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS)
				.add(Restrictions.eq(FertigungFac.FLR_LOS_STATUS_C_NR, FertigungFac.STATUS_ANGELEGT));
		// Query ausfuehren
		List<?> lossollList = lossollmaterial.list();
		Iterator<?> lossollListIterator = lossollList.iterator();
		while (lossollListIterator.hasNext()) {
			FLRLossollmaterial lossollmat = (FLRLossollmaterial) lossollListIterator.next();
			// Fuer angelegte Lose MUSS es einen Reservierungseintrag geben.
			// nur fuer Artikel
			if (lossollmat.getFlrartikel() != null) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(lossollmat.getFlrartikel().getI_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
				resDto.setIBelegartpositionid(lossollmat.getI_id());
				resDto.setNMenge(lossollmat.getN_menge());

				/// PJ20837
				java.sql.Date dTermin = getFertigungFac().getProduktionsbeginnAnhandZugehoerigemArbeitsgang(
						new java.sql.Date(lossollmat.getFlrlos().getT_produktionsbeginn().getTime()),
						lossollmat.getI_id(), theClientDto);
				// PJ17994
				resDto.setTLiefertermin(Helper.addiereTageZuTimestamp(new java.sql.Timestamp(dTermin.getTime()),
						lossollmat.getI_beginnterminoffset()));

				// anlegen
				createArtikelreservierung(resDto, false);
				myLogger.warn(theClientDto.getIDUser(), "Reservierung nachgetragen: " + resDto);
			}

		}

		session.close();
		// Auftraege
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria abPosCrit = session.createCriteria(FLRAuftragposition.class);
		// nur Artikel-Positionen
		abPosCrit.add(Restrictions.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL));

		org.hibernate.Criteria abCrit = abPosCrit.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
		// Rahmenauftraege werden ignoriert, da diese keine Reservierungen
		// ausloesen.
		abCrit.add(Restrictions.ne(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, AuftragServiceFac.AUFTRAGART_RAHMEN));

		// Nur Sataus Offen und Teilerledigt
		String[] stati = new String[3];
		stati[0] = AuftragServiceFac.AUFTRAGSTATUS_OFFEN;
		stati[1] = AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT;
		stati[2] = AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT;
		abCrit.add(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, stati));

		// Query ausfuehren
		List<?> abposList = abPosCrit.list();
		Iterator<?> abPosListIterator = abposList.iterator();
		while (abPosListIterator.hasNext()) {
			FLRAuftragposition abPos = (FLRAuftragposition) abPosListIterator.next();
			// Fuer offene Auftraege MUSS es einen Reservierungseintrag geben.
			// (ausser positionsstatus = erledigt)

			// nur fuer noch nicht erledigte Auftragspositionen mit offener
			// Menge != 0

			if (abPos.getN_offenemenge().compareTo(new BigDecimal(0)) != 0 && !abPos.getAuftragpositionstatus_c_nr()
					.equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(abPos.getFlrartikel().getI_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				resDto.setIBelegartpositionid(abPos.getI_id());
				resDto.setNMenge(abPos.getN_offenemenge());
				java.sql.Timestamp tLiefertermin;
				if (abPos.getN_offenemenge().compareTo(new BigDecimal(0)) < 0) {
					// Negative Menge -> Finaltermin
					tLiefertermin = new java.sql.Timestamp(abPos.getFlrauftrag().getT_finaltermin().getTime());
				} else {
					// Positive Menge -> Liefertermin

					AuftragpositionDto abPosDto = null;
					try {
						abPosDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(abPos.getI_id());
					} catch (RemoteException ex2) {
						throwEJBExceptionLPRespectOld(ex2);
					}
					tLiefertermin = abPosDto.getTUebersteuerbarerLiefertermin();
				}
				resDto.setTLiefertermin(tLiefertermin);
				// anlegen
				createArtikelreservierung(resDto, false);
				myLogger.warn(theClientDto.getIDUser(), "Reservierung nachgetragen: " + resDto);
			}

		}
		session.close();

		// Kueche
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria speiseplan = session.createCriteria(FLRSpeiseplanposition.class);

		speiseplan.createCriteria("flrspeiseplan")
				.add(Restrictions.ge("t_datum", Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()))));

		// Query ausfuehren
		List<?> speiseplanList = speiseplan.list();
		Iterator<?> speiseplanListIterator = speiseplanList.iterator();
		while (speiseplanListIterator.hasNext()) {
			FLRSpeiseplanposition flrSpeiseplanposition = (FLRSpeiseplanposition) speiseplanListIterator.next();
			if (flrSpeiseplanposition.getN_menge().doubleValue() > 0) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(flrSpeiseplanposition.getArtikel_i_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_KUECHE);
				resDto.setIBelegartpositionid(flrSpeiseplanposition.getI_id());
				resDto.setNMenge(flrSpeiseplanposition.getN_menge());
				resDto.setTLiefertermin(new Timestamp(flrSpeiseplanposition.getFlrspeiseplan().getT_datum().getTime()));
				createArtikelreservierung(resDto, false);
				myLogger.warn(theClientDto.getIDUser(), "Reservierung nachgetragen: " + resDto);
			}
		}

		session.close();

		// Forecast
		session = FLRSessionFactory.getFactory().openSession();

		String s = "SELECT fa FROM FLRForecastauftrag fa WHERE fa.status_c_nr='" + LocaleFac.STATUS_FREIGEGEBEN
				+ "' AND fa.flrfclieferadresse.flrforecast.status_c_nr='" + LocaleFac.STATUS_ANGELEGT
				+ "' AND fa.t_freigabe IS NOT NULL";

		org.hibernate.Query fQuery = session.createQuery(s);

		// Query ausfuehren
		List<?> fList = fQuery.list();
		Iterator<?> fListIterator = fList.iterator();
		while (fListIterator.hasNext()) {
			FLRForecastauftrag flrForecastauftrag = (FLRForecastauftrag) fListIterator.next();
			getForecastFac().reservierungenMitForecastSynchronisieren(null, flrForecastauftrag.getI_id(), null);
		}

		session.close();

		myLogger.exit("Reservierungspr\u00FCfung abgeschlossen");

	}

	/**
	 * Artikelreservierungen drucken.
	 * 
	 * @todo in eine ReportFac verschieben.
	 * 
	 * @param artikelIId   Integer
	 * @param dVon         Date
	 * @param dBis         Date
	 * @param theClientDto der aktuelle Benutzer
	 * @return JasperPrintLP
	 * @throws EJBExceptionLP
	 */
	
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelreservierungen(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis,
			boolean bAlleMandanten, boolean bMonatsstatistik, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ReservierungFac.REPORT_ARTIKELRESERVIERUNG;
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria reservierungen = session.createCriteria(FLRArtikelreservierung.class);
		reservierungen.createAlias(ReservierungFac.FLR_ARTIKELRESERVIERUNG_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId));
		if (dVon != null) {
			reservierungen.add(Restrictions.ge(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN, dVon));
		}
		if (dBis != null) {
			reservierungen.add(Restrictions.lt(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN, dBis));
		}

		reservierungen.addOrder(Order.asc(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN));

		List<?> resultList = reservierungen.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRArtikelreservierung artikelreservierung = (FLRArtikelreservierung) resultListIterator.next();

			String sBelegnummer = null;
			String sStueckliste = null;
			String sZwangsSNR = null;
			String sPartner = null;
			String sZugehoerigerKunde = null;
			String sProjektbezeichnung = null;
			Timestamp tAuftragsfreigabe = null;
			String personAuftragsfreigabe = null;
			String forecastart = null;
			String mandant = null;
			String forecast_bemerkung = null;
			Integer forecastposition_i_id = null;

			Object[] oZeile = new Object[REPORT_RESERVIERUNGSLISTE_ANZAHL_SPALTEN];

			if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragpositionDto auftragpositionDto = null;
				try {
					auftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());
					sZwangsSNR = auftragpositionDto.getCSeriennrchargennr();

					oZeile[REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_BEZ] = auftragpositionDto.getCBez();
					oZeile[REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_ZBEZ] = auftragpositionDto.getCZusatzbez();

				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragpositionDto.getBelegIId());
				sBelegnummer = "A" + auftragDto.getCNr();

				mandant = auftragDto.getMandantCNr();

				sProjektbezeichnung = auftragDto.getCBezProjektbezeichnung();

				sPartner = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
						.getPartnerDto().formatTitelAnrede();

				tAuftragsfreigabe = auftragDto.getTAuftragsfreigabe();

				if (auftragDto.getPersonalIIdAuftragsfreigabe() != null) {
					personAuftragsfreigabe = getPersonalFac()
							.personalFindByPrimaryKey(auftragDto.getPersonalIIdAuftragsfreigabe(), theClientDto)
							.getCKurzzeichen();
				}

			} else if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
				com.lp.server.fertigung.service.LossollmaterialDto auftragpositionDto = null;
				try {
					auftragpositionDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());
				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				LosDto losDto = null;
				try {
					losDto = getFertigungFac().losFindByPrimaryKey(auftragpositionDto.getLosIId());
					sBelegnummer = "L" + losDto.getCNr();
					mandant = losDto.getMandantCNr();
					if (losDto.getStuecklisteIId() != null) {
						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
						ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(),
								theClientDto);
						sStueckliste = dto.getCNr();
					}

					Integer kundeIId = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);
					if (kundeIId != null) {
						sZugehoerigerKunde = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto()
								.formatFixTitelName1Name2();
					}

					sProjektbezeichnung = losDto.getCProjekt();
					if (losDto.getAuftragIId() != null) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
						sPartner = getKundeFac()
								.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
								.getPartnerDto().formatTitelAnrede();
					} else {
						sPartner = "";
					}
				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			} else if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_FORECAST)) {

				ForecastpositionDto forecastpositionDto = null;

				forecastpositionDto = getForecastFac()
						.forecastpositionFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());

				if (forecastpositionDto != null && forecastpositionDto.getIId() != null) {

					ForecastauftragDto forecastauftragDto = getForecastFac()
							.forecastauftragFindByPrimaryKey(forecastpositionDto.getForecastauftragIId());

					forecastart = getForecastFac().getForecastartEienrForecastposition(forecastpositionDto.getIId());

					FclieferadresseDto flDto = getForecastFac()
							.fclieferadresseFindByPrimaryKey(forecastauftragDto.getFclieferadresseIId());

					ForecastDto forecastDto = getForecastFac().forecastFindByPrimaryKey(flDto.getForecastIId());

					sBelegnummer = "F" + forecastDto.getCNr();

					sProjektbezeichnung = forecastDto.getCProjekt();

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(forecastDto.getKundeIId(), theClientDto);

					mandant = kundeDto.getMandantCNr();

					sPartner = kundeDto.getPartnerDto().formatTitelAnrede();
					
					
					forecast_bemerkung = forecastauftragDto.getCBemerkung();
					forecastposition_i_id = forecastpositionDto.getIId();
				}

			} else if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_KUECHE)) {

				SpeiseplanpositionDto speiseplanpositionDto = null;
				try {
					speiseplanpositionDto = getKuecheFac()
							.speiseplanpositionFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());
				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				SpeiseplanDto speiseplanDto = null;
				try {
					speiseplanDto = getKuecheFac().speiseplanFindByPrimaryKey(speiseplanpositionDto.getSpeiseplanIId());
					sBelegnummer = "K";

					// Projekt= Speisekassa

					KassaartikelDto speisekassaDto = getKuecheFac()
							.kassaartikelFindByPrimaryKey(speiseplanDto.getKassaartikelIId());

					sProjektbezeichnung = speisekassaDto.getCBez();

					// Kunde = Stuecklistebezeichnung
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(speiseplanDto.getStuecklisteIId(), theClientDto);

					mandant = stuecklisteDto.getMandantCNr();
					sPartner = stuecklisteDto.getArtikelDto().formatBezeichnung();

				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			}

			if (bAlleMandanten == false) {

				if (mandant != null && !mandant.equals(theClientDto.getMandant())) {
					continue;
				}

			}

			oZeile[REPORT_RESERVIERUNGSLISTE_MANDANT] = mandant;
			oZeile[REPORT_RESERVIERUNGSLISTE_AUFTRAG] = sBelegnummer;
			oZeile[REPORT_RESERVIERUNGSLISTE_STUECKLISTE] = sStueckliste;
			oZeile[REPORT_RESERVIERUNGSLISTE_KUNDENNAME] = sPartner;
			oZeile[REPORT_RESERVIERUNGSLISTE_ZUGEHOERIGER_KUNDE_IM_LOS] = sZugehoerigerKunde;

			oZeile[REPORT_RESERVIERUNGSLISTE_PROJEKTNAME] = sProjektbezeichnung;
			oZeile[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN] = artikelreservierung.getT_liefertermin();
			oZeile[REPORT_RESERVIERUNGSLISTE_MENGE] = artikelreservierung.getN_menge();
			oZeile[REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER] = sZwangsSNR;

			oZeile[REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_ZEITPUNKT] = tAuftragsfreigabe;
			oZeile[REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_PERSON] = personAuftragsfreigabe;
			oZeile[REPORT_RESERVIERUNGSLISTE_FORECASTART] = forecastart;
			oZeile[REPORT_RESERVIERUNGSLISTE_FORECAST_BEMERKUNG] = forecast_bemerkung;
			oZeile[REPORT_RESERVIERUNGSLISTE_FORECASTPOSITION_I_ID] = forecastposition_i_id;

		
			
			alDaten.add(oZeile);

		}
		session.close();

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_RESERVIERUNGSLISTE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		parameter.put("P_ALLE_MANDANTEN", bAlleMandanten);

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		boolean bAuftragsfreigabe=false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
			bAuftragsfreigabe=((Boolean) parameterDto.getCWertAsObject());
			parameter.put("P_PARAMETER_AUFTRAGSFREIGABE", ((Boolean) parameterDto.getCWertAsObject()));
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (bMonatsstatistik == true) {
			sAktuellerReport = REPORT_ARTIKELRESERVIERUNG_MONATSSTATISTIK;
			for (int i = 0; i < alDaten.size() - 1; i = i + 1) {
				for (int j = alDaten.size() - 1; j > i; j = j - 1) {
					Object[] erstes = (Object[]) alDaten.get(j - 1);
					Object[] zweites = (Object[]) alDaten.get(j);

					if (((java.sql.Timestamp) erstes[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN])
							.before(((java.sql.Timestamp) zweites[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN]))) {
						Object[] temp = erstes;
						alDaten.set(j - 1, zweites);
						alDaten.set(j, temp);
					}
				}
			}
			// sortiere nach Jahr/Monat
			java.text.DateFormatSymbols symbols = new java.text.DateFormatSymbols(theClientDto.getLocUi());
			String[] defaultMonths = symbols.getMonths();

			GregorianCalendar cAktuell = new GregorianCalendar();
			if (alDaten.size() > 0) {
				Object[] erste = (Object[]) alDaten.get(alDaten.size() - 1);
				Object[] letzte = (Object[]) alDaten.get(0);

				cAktuell.setTimeInMillis(((Timestamp) letzte[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN]).getTime());
				ArrayList alMonate = new ArrayList();
				while (cAktuell.getTimeInMillis() >= ((Timestamp) erste[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN])
						.getTime()) {
					BigDecimal mengeGesamt = new BigDecimal(0);
					BigDecimal mengeFreigegeben = new BigDecimal(0);
					for (int i = 0; i < alDaten.size(); i++) {
						Object[] zeile = (Object[]) alDaten.get(i);
						Timestamp d = (Timestamp) zeile[REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN];

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(d.getTime());

						if (cAktuell.get(Calendar.MONTH) == cZeile.get(Calendar.MONTH)
								&& cAktuell.get(Calendar.YEAR) == cZeile.get(Calendar.YEAR)) {
							BigDecimal mengeZeile = (BigDecimal) zeile[REPORT_RESERVIERUNGSLISTE_MENGE];

							String forecastart = (String) zeile[REPORT_RESERVIERUNGSLISTE_FORECASTART];
							String auftrag = (String) zeile[REPORT_RESERVIERUNGSLISTE_AUFTRAG];

							if ((forecastart != null && (forecastart.startsWith("F")
									|| forecastart.equals(ForecastFac.FORECASTART_NICHT_DEFINIERT))) || (bAuftragsfreigabe && auftrag.startsWith("A") && zeile[REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_PERSON]==null )) {
								

							} else {
								mengeFreigegeben = mengeFreigegeben.add(mengeZeile);
							}

							mengeGesamt = mengeGesamt.add(mengeZeile);

						}

					}

					Object[] zeileMonate = new Object[REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_ANZAHL_SPALTEN];
					zeileMonate[REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MONAT] = defaultMonths[cAktuell
							.get(Calendar.MONTH)];
					zeileMonate[REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_JAHR] = cAktuell.get(Calendar.YEAR);
					zeileMonate[REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_FREIGEGEBEN] = mengeFreigegeben;
					zeileMonate[REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_GESAMT] = mengeGesamt;
					alMonate.add(zeileMonate);

					cAktuell.set(Calendar.DAY_OF_MONTH, 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.MONTH, cAktuell.get(Calendar.MONTH) - 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.DAY_OF_MONTH, cAktuell.getActualMaximum(Calendar.DAY_OF_MONTH));
					cAktuell.getTimeInMillis();

				}

				dataTemp = new Object[1][1];
				data = (Object[][]) alMonate.toArray(dataTemp);

				initJRDS(parameter, ArtikelFac.REPORT_MODUL, REPORT_ARTIKELRESERVIERUNG_MONATSSTATISTIK,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
				return getReportPrint();
			} else {
				return null;
			}

		} else {

			initJRDS(parameter, ReservierungFac.REPORT_MODUL, ReservierungFac.REPORT_ARTIKELRESERVIERUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		}
		print = getReportPrint();
		return print;
	}

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

		if (sAktuellerReport.equals(ReservierungFac.REPORT_ARTIKELRESERVIERUNG)) {

			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAG];
			} else if ("Kundenname".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_KUNDENNAME];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MANDANT];
			} else if ("ZugehoerigerKundeImLos".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_ZUGEHOERIGER_KUNDE_IM_LOS];
			} else if ("Forecastart".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_FORECASTART];
			} else if ("Projektname".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_PROJEKTNAME];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MENGE];
			} else if ("F_ZWANGSSERIENNUMMER".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER];
			} else if ("F_STUECKLISTE".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_STUECKLISTE];
				if (value == null) {
					value = "";
				}
			} else if ("F_AUFTRAGSFREIGABE_PERSON".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_PERSON];
			} else if ("F_AUFTRAGSFREIGABE_ZEITPUNKT".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAGSFREIGABE_ZEITPUNKT];
			} else if ("F_AUFTRAGSPOSITION_C_BEZ".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_BEZ];
			} else if ("F_AUFTRAGSPOSITION_C_ZBEZ".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAGSPOSITION_C_ZBEZ];
			}else if ("ForecastBemerkung".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_FORECAST_BEMERKUNG];
			} else if ("ForecastpositionIId".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_FORECASTPOSITION_I_ID];
			} 

		} else if (sAktuellerReport.equals(ReservierungFac.REPORT_ARTIKELRESERVIERUNG_MONATSSTATISTIK)) {

			if ("Jahr".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_JAHR];
			} else if ("Monat".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MONAT];
			} else if ("MengeFreigegeben".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_FREIGEGEBEN];
			} else if ("MengeGesamt".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MONATSSTATISTIK_MENGE_GESAMT];
			}

		}
		return value;

	}

	public HashSet getSetOfArtikelIdMitReservierungen() {

		HashSet hs = new HashSet();

		Session session = FLRSessionFactory.getFactory().openSession();

		String s = "SELECT distinct(ar.artikel_i_id) FROM FLRArtikelreservierung ar";

		org.hibernate.Query fQuery = session.createQuery(s);

		// Query ausfuehren
		List<?> fList = fQuery.list();
		Iterator<?> fListIterator = fList.iterator();
		while (fListIterator.hasNext()) {
			Integer artikelIId = (Integer) fListIterator.next();
			hs.add(artikelIId);
		}

		return hs;
	}

	public BigDecimal getAnzahlRahmenreservierungen(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bdReserviert = new BigDecimal(0);
		try {
			ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();
			kritDtoI.setArtikelIId(artikelIId); // keine Datumseinschraenkung.
			ReportRahmenreservierungDto[] aResult = getArtikelReportFac().getReportRahmenreservierung(kritDtoI,
					theClientDto);
			for (int i = 0; i < aResult.length; i++) {
				// negative Rahmenreservierungen bleiben unberuecksichtigt.
				if (aResult[i].getNOffeneMenge() != null && aResult[i].getNOffeneMenge().doubleValue() > 0) {
					bdReserviert = bdReserviert.add(aResult[i].getNOffeneMenge());
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdReserviert;
	}
}
