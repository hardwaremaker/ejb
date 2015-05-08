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
package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.ejb.AuftragpositionQuery;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungkontierung;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejb.RechnungpositionQuery;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungKontierung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungZahlung;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.HelperWiederholendeRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungDtoAssembler;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungPositionDtoAssembler;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.rechnung.service.RechnungkontierungDtoAssembler;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.rechnung.service.RechnungzahlungDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.Facade;
import com.lp.server.util.IPositionNumber;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.RechnungPositionNumberAdapter;
import com.lp.server.util.RechnungPositionNumberDtoAdapter;
import com.lp.server.util.Validator;
import com.lp.server.util.ZwsPositionMapper;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.service.Artikelset;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class RechnungFacBean extends Facade implements RechnungFac,
		IPrimitiveSwapper, IAktivierbar, IPositionNumber {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private BelegAktivierungFac belegAktivierungFac;

	/**
	 * Wirft eine EJBException, wenn das Feld Reverse Charge nicht meht den, auf
	 * dem hinterlegen Auftag vorhandenen, Anzahlungen/Schlussrechnungen
	 * uebereinstimmt.
	 * 
	 * @param rech
	 */
	private void pruefeAnzahlungSchlusszahlung(RechnungDto rech) {
		String art = rech.getRechnungartCNr();
		if (art.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)
				|| art.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			boolean anzahlungenVorhanden = false;
			for (RechnungDto re : rechnungFindByAuftragIId(rech.getAuftragIId())) {
				if (re.getIId().equals(rech.getIId()))
					continue;
				if (re.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT))
					continue;

				if (re.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {

					if (art.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
						// Wenn es eine Schlussrechnung gibt, darf man keine
						// weitere machen
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
								"FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN", re
										.getCNr());
					}
					if (STATUS_ANGELEGT.equals(re.getStatusCNr()))
						continue; // ist noch nicht in der Fibu, also darf man
									// weitere Anzahlungen machen

					if (rech.getIId() == null) {
						// Man darf keine neue Anzahlung erzeugen
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
								"FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN",
								re.getCNr());
					} else {
						RechnungDto alt = rechnungFindByPrimaryKeyOhneExc(rech
								.getIId());
						if (alt != null) {
							if (STATUS_STORNIERT.equals(alt.getStatusCNr())) {
								// stornierte darf man nicht wieder aktivieren
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
										"FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN",
										re.getCNr());
							} else if (STATUS_BEZAHLT
									.equals(alt.getStatusCNr())) {
								// bezahlte darf man auch nicht aendern
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
										"FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN",
										re.getCNr());
							}
						}
					}
				} else if (re.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_ANZAHLUNG)) {
					anzahlungenVorhanden = !STATUS_ANGELEGT.equals(re
							.getStatusCNr());
					// if
					// (!re.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) {
					// throw new EJBExceptionLP(
					// EJBExceptionLP.FEHLER_ANZAHLUNGEN_NICHT_BEZAHLT,
					// "FEHLER_ANZAHLUNGEN_NICHT_BEZAHLT", re.getCNr());
					// }
					if (Helper.short2boolean(re.getBReversecharge()) != Helper
							.short2boolean(rech.getBReversecharge())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_AR_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
								"FEHLER_AR_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND",
								re.getCNr());
					}
				}
			}
			if (art.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
					&& !anzahlungenVorhanden) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEINE_ANZAHLUNGEN_VORHANDEN,
						new Exception("FEHLER_KEINE_ANZAHLUNGEN_VORHANDEN"));
			}
		}
	}

	/**
	 * Eine neue Rechnung anlegen
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @param theClientDto
	 *            String
	 * @return RechnungDto
	 * @throws EJBExceptionLP
	 */
	public RechnungDto createRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		pruefeAnzahlungSchlusszahlung(rechnungDto);
		String rechnungart = rechnungDto.getRechnungartCNr();
		try {
			String rechnungTyp = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(rechnungart, theClientDto)
					.getRechnungtypCNr();
			// Gutschriften koennen evtl den Nummernkreis der Rechnung benutzen
			if (rechnungTyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_GUTSCHRIFT,
								ParameterFac.GUTSCHRIFT_VERWENDET_NUMMERNKREIS_DER_RECHNUNG);
				boolean bGutschriftVerwendetNummernkreisDerRechnung = Helper
						.short2boolean(new Short(parameter.getCWert()));
				if (bGutschriftVerwendetNummernkreisDerRechnung) {
					rechnungTyp = RechnungFac.RECHNUNGTYP_RECHNUNG;
				}

				// PJ18843 Wenn FIBU und Gutschrift auf Anzahlung, dann Fehler
				if (rechnungDto.getRechnungIIdZurechnung() != null) {

					RechnungDto rechnungDtoVorhanden = getRechnungFac()
							.rechnungFindByPrimaryKey(
									rechnungDto.getRechnungIIdZurechnung());

					if (rechnungDtoVorhanden.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_ANZAHLUNG)) {
						if (getMandantFac().darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_FINANZBUCHHALTUNG,
								theClientDto)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_RECHNUNG_GS_AUF_ANZAHLUNG_NICHT_MOEGLICH,
									new Exception(
											"FEHLER_RECHNUNG_GS_AUF_ANZAHLUNG_NICHT_MOEGLICH,"));
						}

					}

				}

			}
			return createRechnung(rechnungDto, theClientDto, rechnungTyp);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public boolean gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(
			Integer auftragIId, TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("RechnungfindByAuftragIIdRechnungArtCNr");
		query.setParameter(1, auftragIId);
		query.setParameter(2, RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG);
		Collection<?> cl = query.getResultList();
		RechnungDto[] rDtos = assembleRechnungDtos(cl);
		boolean bSchlussrechnungVorhanden = false;
		for (int i = 0; i < rDtos.length; i++) {
			RechnungDto rDto = rDtos[i];

			if (!Helper.isOneOf(rDto.getStatusCNr(),
					RechnungFac.STATUS_STORNIERT, RechnungFac.STATUS_ANGELEGT)) {
				bSchlussrechnungVorhanden = true;
			}
		}

		return bSchlussrechnungVorhanden;
	}

	/**
	 * Neue Rechnung oder Gutschrift anlegen
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @param theClientDto
	 *            String
	 * @param mode
	 *            String
	 * @return RechnungDto
	 * @throws EJBExceptionLP
	 */
	private RechnungDto createRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto, String mode) throws EJBExceptionLP {
		BelegnummerGeneratorObj bnGen = new BelegnummerGeneratorObj();
		LpBelegnummerFormat f = bnGen.getBelegnummernFormat(rechnungDto
				.getMandantCNr());
		try {
			// Geschaeftsjahr berechnen
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					rechnungDto.getMandantCNr(), rechnungDto.getTBelegdatum());
			rechnungDto.setIGeschaeftsjahr(iGeschaeftsjahr);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		LpBelegnummer bnr = bnGen.getNextBelegNr(
				rechnungDto.getIGeschaeftsjahr(), PKConst.PK_RECHNUNG_TABELLE,
				mode.toLowerCase(), rechnungDto.getMandantCNr(), theClientDto);
		// eine neue hat noch keinen wert
		rechnungDto.setIId(bnr.getPrimaryKey());
		rechnungDto.setIGeschaeftsjahr(bnr.getGeschaeftsJahr());
		String belegNummer = f.format(bnr);
		rechnungDto.setCNr(belegNummer);
		rechnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		rechnungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// flags checken
		if (rechnungDto.getBMindermengenzuschlag() == null) {
			rechnungDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
		}
		if (rechnungDto.getBMwstallepositionen() == null) {
			rechnungDto.setBMwstallepositionen(Helper.boolean2Short(false));
		}
		if (rechnungDto.getBReversecharge() == null) {
			rechnungDto.setBReversecharge(Helper.boolean2Short(false));
		}
		// Auf angelegt setzen
		rechnungDto.setStatusCNr(RechnungFac.STATUS_ANGELEGT);
		try {
			Rechnung rechnung = new Rechnung(rechnungDto.getIId(),
					rechnungDto.getMandantCNr(),
					rechnungDto.getIGeschaeftsjahr(), rechnungDto.getCNr(),
					rechnungDto.getKundeIId(), rechnungDto.getTBelegdatum(),
					rechnungDto.getStatusCNr(),
					rechnungDto.getRechnungartCNr(),
					rechnungDto.getKostenstelleIId(),
					rechnungDto.getWaehrungCNr(),
					rechnungDto.getBMwstallepositionen(),
					rechnungDto.getPersonalIIdAnlegen(),
					rechnungDto.getPersonalIIdAendern(),
					rechnungDto.getBReversecharge(),
					rechnungDto.getPersonalIIdVertreter(),
					rechnungDto.getPersonalIIdVertreter(),
					rechnungDto.getLieferartIId(),
					rechnungDto.getZahlungszielIId(),
					rechnungDto.getSpediteurIId());
			em.persist(rechnung);
			em.flush();
			rechnungDto.setTAendern(rechnung.getTAendern());
			rechnungDto.setTAnlegen(rechnung.getTAnlegen());
			setRechnungFromRechnungDto(rechnung, rechnungDto);

			// PJ14938
			try {
				ParametermandantDto parametermandantautoDebitDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
				if ((Boolean) parametermandantautoDebitDto.getCWertAsObject()) {
					if (rechnungDto.getKundeIId() != null) {

						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										rechnungDto.getKundeIId(), theClientDto);
						if (kundeDto.getIidDebitorenkonto() == null) {
							KontoDto ktoDto = getKundeFac()
									.createDebitorenkontoZuKundenAutomatisch(
											rechnungDto.getKundeIId(), false,
											null, theClientDto);
							kundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
									ktoDto.getCNr()));
							getKundeFac().updateKunde(kundeDto, theClientDto);

						}
					}
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			return rechnungFindByPrimaryKey(bnr.getPrimaryKey());
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void toggleZollpapiereErhalten(Integer rechnungIId,
			String cZollpapier, TheClientDto theClientDto) {
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);

		if (rechnung.getTZollpapier() == null) {
			rechnung.setTZollpapier(new Timestamp(System.currentTimeMillis()));
			rechnung.setPersonalIIdZollpapier(theClientDto.getIDPersonal());
			rechnung.setCZollpapier(cZollpapier);
		} else {
			rechnung.setTZollpapier(null);
			rechnung.setPersonalIIdZollpapier(null);
			rechnung.setCZollpapier(null);
		}
	}

	// rk: wird nirgends verwendet
	// public RechnungDto updateRechnungPartnerIIdRechnungsadresse(
	// RechnungDto rechnungDto, TheClientDto theClientDto)
	// throws EJBExceptionLP {
	// // log
	// myLogger.logData(rechnungDto);
	// RechnungDto rechnungOldDto = null;
	// // begin
	// if (rechnungDto != null) {
	// Integer iId = rechnungDto.getIId();
	// try {
	// rechnungOldDto = getRechnungFac().rechnungFindByPrimaryKey(iId);
	// } catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	//
	// try {
	// Rechnung rechnung = em.find(Rechnung.class, iId);
	// if (rechnung == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
	// "");
	// }
	// // das Lager darf nur geaendert werden, wenn die Rechnung noch
	// // keine posiitonen hat
	// if (rechnung.getLagerIId() != null
	// && !rechnung.getLagerIId().equals(
	// rechnungDto.getLagerIId())
	// && rechnungPositionFindByRechnungIId(rechnungDto
	// .getIId()).length > 0) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_RECHNUNG_DARF_LAGER_NICHT_AENDERN,
	// "");
	// }
	// if (rechnung.getKundeIId() != null
	// && !rechnung.getKundeIId().equals(
	// rechnungDto.getKundeIId())) {
	// RechnungPositionDto[] pos = rechnungPositionFindByRechnungIId(rechnungDto
	// .getIId());
	// for (int i = 0; i < pos.length; i++) {
	// if (pos[i].getRechnungpositionartCNr().equals(
	// RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_RECHNUNG_HAT_LIEFERSCHEINE_EINES_ANDEREN_KUNDEN,
	// "");
	// }
	// }
	// }
	// // belegumdatieren: das Belegdatum darf nur innerhalb des GJ der
	// // Rechnung geaendert werden
	// Integer iGJNeu = getParameterFac()
	// .getGeschaeftsjahr(theClientDto.getMandant(),
	// rechnungDto.getTBelegdatum());
	// if (!iGJNeu.equals(rechnungDto.getIGeschaeftsjahr())) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
	// new Exception("Es wurde versucht, die RE "
	// + rechnungDto.getCNr() + " auf "
	// + rechnungDto.getTBelegdatum() + " (GJ "
	// + iGJNeu + ") umzudatieren"));
	// }
	// // updaten
	// setRechnungFromRechnungDto(rechnung, rechnungDto);
	// // }
	// // catch (FinderException ex) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
	// // ex);
	// } catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	// }
	// return rechnungFindByPrimaryKey(rechnungDto.getIId());
	// }

	/**
	 * Rechnung oder Gutschrift updaten.
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @param theClientDto
	 *            String
	 * @return RechnungDto
	 * @throws EJBExceptionLP
	 */
	public RechnungDto updateRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(rechnungDto);
		pruefeAnzahlungSchlusszahlung(rechnungDto);
		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;
		// begin
		if (rechnungDto != null) {

			Integer iId = rechnungDto.getIId();
			// erlaubt ??
			pruefeUpdateAufRechnungErlaubt(iId, theClientDto);

			rechnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			rechnungDto.setTAendern(getTimestamp());
			try {
				Rechnung rechnung = em.find(Rechnung.class, iId);
				if (rechnung == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
							"");
				}

				if (!rechnungDto.getKundeIId().equals(rechnung.getKundeIId())) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							rechnungDto.getKundeIId(), theClientDto);
					KundeDto kundeDtoVorher = getKundeFac()
							.kundeFindByPrimaryKey(rechnung.getKundeIId(),
									theClientDto);

					// CK: 2013-06-04 Gilt nicht mehr, da die Konditionen nun
					// vorher am Client bestsetigt werden muessen
					// --
					// Wird der kunde geaendert muss man die Konditionen neu
					// holen
					/*
					 * Double dAllgemeinerrabattsatz = new Double(0); if
					 * (kundeDto.getFRabattsatz() != null) {
					 * dAllgemeinerrabattsatz = kundeDto.getFRabattsatz(); }
					 * rechnungDto
					 * .setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz);
					 * 
					 * if (kundeDto.getLieferartIId() != null) {
					 * rechnungDto.setLieferartIId(kundeDto.getLieferartIId());
					 * } if (kundeDto.getZahlungszielIId() != null) {
					 * rechnungDto.setZahlungszielIId(kundeDto
					 * .getZahlungszielIId()); } if (kundeDto.getSpediteurIId()
					 * != null) {
					 * rechnungDto.setSpediteurIId(kundeDto.getSpediteurIId());
					 * }
					 */

					RechnungPositionDto[] aAuftragpositionDto = rechnungPositionFindByRechnungIId(rechnungDto
							.getIId());

					ParametermandantDto parameterPositionskontierung = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_KUNDEN,
									ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

					boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
							.getCWertAsObject();

					for (int i = 0; i < aAuftragpositionDto.length; i++) {
						if (aAuftragpositionDto[i].getPositionsartCNr().equals(
								RechnungFac.POSITIONSART_RECHNUNG_IDENT)
								|| aAuftragpositionDto[i]
										.getPositionsartCNr()
										.equals(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {

							MwstsatzDto mwstsatzDto = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);
							if (bDefaultMwstsatzAusArtikel
									&& aAuftragpositionDto[i]
											.getPositionsartCNr()
											.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
								ArtikelDto artikelDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(
												aAuftragpositionDto[i]
														.getArtikelIId(),
												theClientDto);
								if (artikelDto.getMwstsatzbezIId() != null) {
									mwstsatzDto = getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													artikelDto
															.getMwstsatzbezIId(),
													theClientDto);
								}

							}

							// SP503
							if (bDefaultMwstsatzAusArtikel
									&& aAuftragpositionDto[i]
											.getPositionsartCNr()
											.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

								// Wenn alter und neuer Kunde den gleichen
								// MWST-Satz
								// haben, dann nichts tun
								MwstsatzDto mwstsatzDtoKundeNeu = getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												kundeDto.getMwstsatzbezIId(),
												theClientDto);

								MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												kundeDtoVorher
														.getMwstsatzbezIId(),
												theClientDto);

								if (mwstsatzDtoKundeVorher.getFMwstsatz() == 0
										&& mwstsatzDtoKundeNeu.getFMwstsatz() > 0) {
									bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = true;
								}

								if (mwstsatzDtoKundeNeu.getIId().equals(
										mwstsatzDtoKundeVorher.getIId())) {
									continue;
								}
							}

							if (!aAuftragpositionDto[i].getMwstsatzIId()
									.equals(mwstsatzDto.getIId())) {
								aAuftragpositionDto[i]
										.setMwstsatzIId(mwstsatzDto.getIId());

								BigDecimal mwstBetrag = aAuftragpositionDto[i]
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
										.multiply(
												new BigDecimal(mwstsatzDto
														.getFMwstsatz()
														.doubleValue())
														.movePointLeft(2));
								// aAuftragpositionDto[i].setNMwstbetrag(
								// mwstBetrag);
								aAuftragpositionDto[i]
										.setNBruttoeinzelpreis(mwstBetrag.add(aAuftragpositionDto[i]
												.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
								updateRechnungPosition(aAuftragpositionDto[i],
										theClientDto);
							}
						}
					}

				}

				// wenn sich der Rabattsatz geaendert hat, dann muessen die
				// Postionen im Lager upgedatet werden
				if (rechnungDto.getFAllgemeinerRabattsatz() == null)
					// leeren Rabatt immer mit 0 besetzen, da Pflicht in der
					// Rechnung
					rechnungDto.setFAllgemeinerRabattsatz(new Double(0));
				if (rechnung.getFAllgemeinerrabattsatz().doubleValue() != rechnungDto
						.getFAllgemeinerRabattsatz().doubleValue()) {
					rechnung.setFAllgemeinerrabattsatz(rechnungDto
							.getFAllgemeinerRabattsatz());
					RechnungPositionDto[] aAuftragpositionDto = rechnungPositionFindByRechnungIId(rechnungDto
							.getIId());

					for (int i = 0; i < aAuftragpositionDto.length; i++) {
						if (aAuftragpositionDto[i].getPositionsartCNr().equals(
								RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
							updateRechnungPosition(aAuftragpositionDto[i],
									theClientDto);

						}
					}

				}

				if (!rechnung.getTBelegdatum().equals(
						rechnungDto.getTBelegdatum())) {
					try {

						String rechnungTyp = getRechnungServiceFac()
								.rechnungartFindByPrimaryKey(
										rechnungDto.getRechnungartCNr(),
										theClientDto).getRechnungtypCNr();

						getLagerFac().updateTBelegdatumEinesBelegesImLager(
								rechnungTyp,
								rechnungDto.getIId(),
								new Timestamp(rechnungDto.getTBelegdatum()
										.getTime()), theClientDto);
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}
				}
				// das Lager darf nur geaendert werden, wenn die Rechnung noch
				// keine posiitonen hat
				if (rechnung.getLagerIId() != null
						&& !rechnung.getLagerIId().equals(
								rechnungDto.getLagerIId())
						&& rechnungPositionFindByRechnungIId(rechnungDto
								.getIId()).length > 0) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_DARF_LAGER_NICHT_AENDERN,
							"");
				}

				// PJ15072

				if (!rechnung.getRechnungartCNr().equals(
						rechnungDto.getRechnungartCNr())) {

					// Wenn nach Wertgutschrift bzw.umgekehrt gebucht wird, geht
					// das nur, wenn keine Ident-Posiionen
					if (rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)
							|| rechnung.getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {

						RechnungPositionDto[] dtos = rechnungPositionFindByRechnungIId(rechnungDto
								.getIId());

						for (int i = 0; i < dtos.length; i++) {
							RechnungPositionDto dto = dtos[i];

							if (dto.getPositionsartCNr().equals(
									RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_GUTSCHRIFT_WECHSEL_WERTGUTSCHRIFT_FEHLER,
										"");
							}

						}
					}

				}

				if (rechnung.getKundeIId() != null
						&& !rechnung.getKundeIId().equals(
								rechnungDto.getKundeIId())) {
					RechnungPositionDto[] pos = rechnungPositionFindByRechnungIId(rechnungDto
							.getIId());
					for (int i = 0; i < pos.length; i++) {
						if (pos[i].getRechnungpositionartCNr().equals(
								RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_RECHNUNG_HAT_LIEFERSCHEINE_EINES_ANDEREN_KUNDEN,
									"");
						}
					}
				}
				// belegumdatieren: das Belegdatum darf nur innerhalb des GJ der
				// Rechnung geaendert werden
				Integer iGJNeu = getParameterFac()
						.getGeschaeftsjahr(theClientDto.getMandant(),
								rechnungDto.getTBelegdatum());
				if (!iGJNeu.equals(rechnungDto.getIGeschaeftsjahr())) {
					throw new EJBExceptionLP(
					// EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
							new Exception("Es wurde versucht, die RE "
									+ rechnungDto.getCNr() + " auf "
									+ rechnungDto.getTBelegdatum() + " (GJ "
									+ iGJNeu + ") umzudatieren"));
				}

				// updaten
				setRechnungFromRechnungDto(rechnung, rechnungDto);
				if (!rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
					setzeRechnungKonditionenLSPositionen(rechnungDto,
							theClientDto);

					// PJ2276
					RechnungPositionDto[] rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
							.getIId());
					Set<Integer> modifiedPositions = getBelegVerkaufFac()
							.adaptIntZwsPositions(rePosDto);
					for (Integer index : modifiedPositions) {
						updateRechnungPositionOhneNeuBerechnung(
								rePosDto[index], theClientDto);
					}
					rechnung.setTAendern(getTimestamp());
				}

				em.merge(rechnung);
				em.flush();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		rechnungDto = rechnungFindByPrimaryKey(rechnungDto.getIId());
		rechnungDto
				.setBMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben(bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben);
		return rechnungDto;
	}

	public void updateRechnungBeimZusammenfuehren(RechnungDto rechnungDto)
			throws EJBExceptionLP, RemoteException {
		Rechnung rechnung = null;
		rechnung = em.find(Rechnung.class, rechnungDto.getIId());
		if (rechnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setRechnungFromRechnungDto(rechnung, rechnungDto);
	}

	public RechnungDto rechnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Rechnung rechnung = em.find(Rechnung.class, iId);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRechnungDto(rechnung);
	}

	public RechnungDto rechnungFindByPrimaryKeyOhneExc(Integer iId) {
		Rechnung rechnung = em.find(Rechnung.class, iId);
		if (rechnung == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		return assembleRechnungDto(rechnung);
	}

	// public RechnungDto[] rechnungFindByPartnerIIdRechnungsadresseMandantCNr(
	// Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
	// try {
	// Query query = em
	// .createNamedQuery("RechnungfindByPartnerIIdRechnungsadresseMandantCNr");
	// query.setParameter(1, partnerIId);
	// query.setParameter(2, mandantCNr);
	// Collection<?> cl = query.getResultList();
	// return assembleRechnungDtos(cl);
	// } catch (Exception e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
	// }
	// }
	//
	// public RechnungDto[]
	// rechnungFindByPartnerIIdRechnungsadresseMandantCNrOhneExc(
	// Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
	// try {
	// Query query = em
	// .createNamedQuery("RechnungfindByPartnerIIdRechnungsadresseMandantCNr");
	// query.setParameter(1, partnerIId);
	// query.setParameter(2, mandantCNr);
	// Collection<?> cl = query.getResultList();
	// return assembleRechnungDtos(cl);
	// } catch (Throwable ex) {
	// return null;
	// }
	// }

	public RechnungDto[] rechnungFindByKundeIIdMandantCNr(Integer kundeIId,
			String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByKundeIIdMandantCNr");
			query.setParameter(1, kundeIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungDto[] rechnungFindByKundeIIdMandantCNrOhneExc(
			Integer kundeIId, String mandantCNr) {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByKundeIIdMandantCNr");
			query.setParameter(1, kundeIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Throwable ex) {
			return null;
		}
	}

	public RechnungDto[] rechnungFindByKundeIIdStatistikadresseMandantCNr(
			Integer kundeIId, String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByKundeIIdStatistikadresseMandantCNr");
			query.setParameter(1, kundeIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungDto[] rechnungFindByKundeIIdStatistikadresseMandantCNrOhneExc(
			Integer kundeIId, String mandantCNr) {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByKundeIIdStatistikadresseMandantCNr");
			query.setParameter(1, kundeIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Throwable ex) {
			return null;
		}
	}

	public void setRechnungFromRechnungDto(Rechnung rechnung,
			RechnungDto rechnungDto) {
		rechnung.setMandantCNr(rechnungDto.getMandantCNr());
		rechnung.setIGeschaeftsjahr(rechnungDto.getIGeschaeftsjahr());
		rechnung.setCNr(rechnungDto.getCNr());
		rechnung.setRechnungIIdZurechnung(rechnungDto
				.getRechnungIIdZurechnung());
		rechnung.setKundeIId(rechnungDto.getKundeIId());
		rechnung.setAnsprechpartnerIId(rechnungDto.getAnsprechpartnerIId());
		rechnung.setAuftragIId(rechnungDto.getAuftragIId());
		rechnung.setLieferscheinIId(rechnungDto.getLieferscheinIId());
		rechnung.setLagerIId(rechnungDto.getLagerIId());
		rechnung.setTBelegdatum(Helper.cutTimestamp(rechnungDto
				.getTBelegdatum()));
		rechnung.setStatusCNr(rechnungDto.getStatusCNr());
		rechnung.setRechnungartCNr(rechnungDto.getRechnungartCNr());
		rechnung.setKostenstelleIId(rechnungDto.getKostenstelleIId());
		rechnung.setWaehrungCNr(rechnungDto.getWaehrungCNr());
		rechnung.setNKurs(rechnungDto.getNKurs());
		rechnung.setMwstsatzIId(rechnungDto.getMwstsatzIId());
		rechnung.setBMwstallepositionen(rechnungDto.getBMwstallepositionen());
		rechnung.setNWert(rechnungDto.getNWert());
		rechnung.setNWertfw(rechnungDto.getNWertfw());
		rechnung.setNWertust(rechnungDto.getNWertust());
		rechnung.setNWertustfw(rechnungDto.getNWertustfw());

		if (rechnungDto.getFVersteckterAufschlag() != null)
			rechnung.setFVersteckteraufschlag(rechnungDto
					.getFVersteckterAufschlag());
		if (rechnungDto.getFAllgemeinerRabattsatz() != null)
			rechnung.setFAllgemeinerrabattsatz(rechnungDto
					.getFAllgemeinerRabattsatz());

		rechnung.setBMindermengenzuschlag(rechnungDto
				.getBMindermengenzuschlag());
		rechnung.setNProvision(rechnungDto.getNProvision());
		rechnung.setCProvisiontext(rechnungDto.getCProvisiontext());
		rechnung.setZahlungszielIId(rechnungDto.getZahlungszielIId());
		rechnung.setLieferartIId(rechnungDto.getLieferartIId());
		rechnung.setSpediteurIId(rechnungDto.getSpediteurIId());
		rechnung.setTGedruckt(rechnungDto.getTGedruckt());
		rechnung.setTFibuuebernahme(rechnungDto.getTFibuuebernahme());
		rechnung.setCKopftextuebersteuert(rechnungDto
				.getCKopftextuebersteuert());
		rechnung.setCFusstextuebersteuert(rechnungDto
				.getCFusstextuebersteuert());
		rechnung.setTStorniert(rechnungDto.getTStorniert());
		rechnung.setPersonalIIdStorniert(rechnungDto.getPersonalIIdStorniert());
		rechnung.setTBezahltdatum(rechnungDto.getTBezahltdatum());
		rechnung.setTMahnsperrebis(rechnungDto.getTMahnsperrebis());

		rechnung.setTAnlegen(rechnungDto.getTAnlegen());
		rechnung.setPersonalIIdAnlegen(rechnungDto.getPersonalIIdAnlegen());
		rechnung.setTAendern(rechnungDto.getTAendern());
		rechnung.setPersonalIIdAendern(rechnungDto.getPersonalIIdAendern());
		rechnung.setTManuellerledigt(rechnungDto.getTManuellerledigt());
		rechnung.setPersonalIIdManuellerledigt(rechnungDto
				.getPersonalIIdManuellerledigt());
		rechnung.setCBestellnummer(rechnungDto.getCBestellnummer());
		rechnung.setCBez(rechnungDto.getCBez());
		rechnung.setBReversecharge(rechnungDto.getBReversecharge());
		rechnung.setKundeIIdStatistikadresse(rechnungDto
				.getKundeIIdStatistikadresse());
		rechnung.setPersonalIIdVertreter(rechnungDto.getPersonalIIdVertreter());
		rechnung.setIZahltagMtlZahlbetrag(rechnungDto
				.getIZahltagMtlZahlbetrag());
		rechnung.setNMtlZahlbetrag(rechnungDto.getNMtlZahlbetrag());
		rechnung.setCLieferartort(rechnungDto.getCLieferartort());

		rechnung.setPersonalIIdZollpapier(rechnungDto
				.getPersonalIIdZollpapier());
		rechnung.setTZollpapier(rechnungDto.getTZollpapier());
		rechnung.setCZollpapier(rechnungDto.getCZollpapier());
		rechnung.setProjektIId(rechnungDto.getProjektIId());
		em.merge(rechnung);
		em.flush();
	}

	private RechnungDto assembleRechnungDto(Rechnung rechnung) {
		return RechnungDtoAssembler.createDto(rechnung);
	}

	private RechnungDto[] assembleRechnungDtos(Collection<?> rechnungs) {
		List<RechnungDto> list = new ArrayList<RechnungDto>();
		if (rechnungs != null) {
			Iterator<?> iterator = rechnungs.iterator();
			while (iterator.hasNext()) {
				Rechnung rechnung = (Rechnung) iterator.next();
				list.add(assembleRechnungDto(rechnung));
			}
		}
		RechnungDto[] returnArray = new RechnungDto[list.size()];
		return (RechnungDto[]) list.toArray(returnArray);
	}

	/**
	 * Den Nettowert einer Rechnung aus ihren Positionen berechnen falls
	 * fremdWaehrung=true, dann den Nettowert in der Fremdwaehrung berechnen
	 * 
	 * @param id
	 *            Integer
	 * @return BigDecimal
	 */
	// public BigDecimal berechneRechnungswertNettoInRechnungswaehrung(Integer
	// id) {
	// RechnungDto rechnung = rechnungFindByPrimaryKey(id);
	// BigDecimal wert = new BigDecimal(0);
	//
	// // ist die Rechnung schon gedruckt?
	// if (rechnung.getTGedruckt() != null) {
	// // ja -> den Wert berechnen
	// Collection<?> c = null;
	// Query query = em
	// .createNamedQuery("RechnungPositionfindByRechnungIId");
	// query.setParameter(1, id);
	// c = query.getResultList();
	// RechnungPositionDto[] positionen = RechnungPositionDtoAssembler
	// .createDtos(c);
	// if (positionen != null) {
	// for (int i = 0; i < positionen.length; i++) {
	// wert = wert.add(positionen[i].getNNettoeinzelpreis()
	// .multiply(positionen[i].getNMenge()));
	// }
	// }
	// }
	// wert = Helper.rundeKaufmaennisch(wert, FinanzFac.NACHKOMMASTELLEN);
	// return wert;
	// }
	/**
	 * Pr&uuml;ft, ob die L&auml;nder der Rechnungsposition im Lieferschein in
	 * das gleiche Land gehen. Falls nicht gibt es eine EJBExceptionLP
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void pruefeLieferscheinLaenderAufGleichheit(Integer rechnungIId) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT repos"
				+ " FROM FLRRechnungPosition AS repos WHERE repos.flrlieferschein IS NOT NULL AND repos.rechnung_i_id="
				+ rechnungIId;

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultList.size() > 1) {

			String lkzVorher = "";

			FLRRechnungPosition rp1 = (FLRRechnungPosition) resultListIterator
					.next();
			if (rp1.getFlrlieferschein().getFlrkunde().getFlrpartner()
					.getFlrlandplzort() != null) {
				lkzVorher = rp1.getFlrlieferschein().getFlrkunde()
						.getFlrpartner().getFlrlandplzort().getFlrland()
						.getC_lkz();
			}

			while (resultListIterator.hasNext()) {
				FLRRechnungPosition rp = (FLRRechnungPosition) resultListIterator
						.next();

				String lkz = "";

				if (rp.getFlrlieferschein().getFlrkunde().getFlrpartner()
						.getFlrlandplzort() != null) {
					lkz = rp.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();
				}

				if (!lkz.equals(lkzVorher)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDER,
							"");
				}

			}
		}
		session.close();
	}

	public RechnungPositionDto createRechnungPosition(
			RechnungPositionDto rePosDto, Integer lagerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// return createRechnungPosition(rePosDto, lagerIId,
		// new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
		return createRechnungPositionImpl(rePosDto, lagerIId,
				new ArrayList<SeriennrChargennrMitMengeDto>(), true,
				theClientDto);
	}

	public RechnungPositionDto createRechnungPosition(
			RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return createRechnungPositionImpl(rePosDto, lagerIId, identities, true,
				theClientDto);
	}

	protected RechnungPositionDto createRechnungPositionImpl(
			RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities,
			boolean artikelsetAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_RECHNUNGPOSITION);
		rePosDto.setIId(pk);
		pruefePflichtfelderBelegposition(rePosDto, theClientDto);

		updateTAendern(rePosDto.getRechnungIId(), theClientDto);

		if (rePosDto.getPositionsartCNr().equals(
				RechnungFac.POSITIONSART_RECHNUNG_ENDSUMME)
				&& rechnungpositionFindByRechnungIIdPositionsartCNrOhneExc(
						rePosDto.getBelegIId(),
						RechnungFac.POSITIONSART_RECHNUNG_ENDSUMME) != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
					new Exception("Eine Position Endsumme existiert bereits."));
		}

		if (rePosDto.getISort() == null) {
			// hinten dran haengen
			rePosDto.setISort(getMaxISort(rePosDto.getRechnungIId()) + 1);
		} else {
			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
					rePosDto.getRechnungIId(), rePosDto.getISort());
		}
		if (rePosDto.getRechnungpositionartCNr().equals(
				RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
			if (rePosDto.getMwstsatzIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("rePosDto.getMwstsatzIId() == null"));
			}
		} else if (rePosDto.getRechnungpositionartCNr().equals(
				RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
			Lieferschein ls = em.find(Lieferschein.class,
					rePosDto.getLieferscheinIId());

			Rechnung rechnung = em.find(Rechnung.class,
					rePosDto.getRechnungIId());
			if (rechnung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}

			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(rechnung.getRechnungartCNr(),
							theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					ls.getKundeIIdRechnungsadresse(), theClientDto);

			// aktuellen MWST-Satz holen
			MwstsatzDto mwstSatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kundeDto.getMwstsatzbezIId(), theClientDto);

			// Wenn zum Belegdatum des Lieferscheins ein anderer Mwstsatz galt,
			// dann Fehler
			MwstsatzDto mwstsatzDtoZumLieferscheindatum = getMandantFac()
					.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(),
							new Timestamp(ls.getTBelegdatum().getTime()));
			if (mwstSatzDto != null && mwstsatzDtoZumLieferscheindatum != null) {
				if (!mwstSatzDto.getIId().equals(
						mwstsatzDtoZumLieferscheindatum.getIId())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE,
							new Exception("FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE"));
				}
			}

			Query query = em
					.createNamedQuery("RechnungPositionfindByLieferscheinIId");
			query.setParameter(1, ls.getIId());
			Collection<?> cl = query.getResultList();
			if (cl.size() != 0) {
				RechnungPositionDto[] rep = assembleRechnungpositionDtos(cl);
				// Es handelt sich um eine Lieferscheinposition die gerade
				// doppelt verrechnet werden soll.

				// Wenns eine Proformarechnung ist, dann Hinweis
				for (int i = 0; i < rep.length; i++) {

					Rechnung rechnungMitDerBereitsVerrechnet = em.find(
							Rechnung.class, rep[i].getRechnungIId());
					RechnungartDto rechnungartDtoMitDerBereitsVerrechnet = getRechnungServiceFac()
							.rechnungartFindByPrimaryKey(
									rechnungMitDerBereitsVerrechnet
											.getRechnungartCNr(),
									theClientDto);

					if (rechnungartDtoMitDerBereitsVerrechnet
							.getRechnungtypCNr().equals(
									RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)
							&& rechnungartDto.getRechnungtypCNr().equals(
									RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {

						ArrayList alDaten = new ArrayList();

						alDaten.add(rechnungMitDerBereitsVerrechnet.getCNr());

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_LIEFERSCHEIN_IN_PROFORMARECHNUNG_DOPPELT_VERRECHNET,
								alDaten,
								new Exception(
										"FEHLER_LIEFERSCHEIN_IN_PROFORMARECHNUNG_DOPPELT_VERRECHNET"));
					}

					if (rechnungartDtoMitDerBereitsVerrechnet
							.getRechnungtypCNr().equals(
									RechnungFac.RECHNUNGTYP_RECHNUNG)
							&& rechnungartDto.getRechnungtypCNr().equals(
									RechnungFac.RECHNUNGTYP_RECHNUNG)) {
						EJBExceptionLP e = null;
						e = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RECHNUNG_POSITIONLS_EXISTIERT,
								"");
						ArrayList<Object> alInfo = new ArrayList<Object>();
						alInfo.add(rep[0].getLieferscheinIId());
						e.setAlInfoForTheClient(alInfo);
						throw e;
					}
				}
			}

			if (rePosDto.getISort() != 1) {
				if (!ls.getFAllgemeinerrabatt().equals(
						rechnung.getFAllgemeinerrabattsatz())) {
					ls.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
					if (rechnung.getFAllgemeinerrabattsatz() != null)
						ls.setFAllgemeinerrabatt(rechnung
								.getFAllgemeinerrabattsatz());
					if (rechnung.getFVersteckteraufschlag() != null)
						ls.setFVersteckteraufschlag(rechnung
								.getFVersteckteraufschlag());
					ls.setNGesamtwertinlieferscheinwaehrung(null);
					em.merge(ls);
					try {
						getLieferscheinFac().updateLieferscheinKonditionen(
								ls.getIId(), theClientDto);
					} catch (RemoteException e) {
					}
					ls.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_VERRECHNET);
					em.merge(ls);
				}
			} else {
				rechnung.setFAllgemeinerrabattsatz(ls.getFAllgemeinerrabatt());
			}
			// kontrollieren, ob der Lieferschein auch im GJ der Rechnung liegt
			ParametermandantDto pStellenGJ = super
					.getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);
			int i = new Integer(pStellenGJ.getCWert()).intValue();
			/*
			 * WH->VF PJ 11850 if (! (lsDto.getCNr().substring(0,
			 * i).equals(rechnung.getCNr().substring(0, i)))) { throw new
			 * EJBExceptionLP(EJBExceptionLP.
			 * FEHLER_RECHNUNG_LIEFERSCHEIN_MUSS_IM_SELBEN_GESCHAEFTSJAHR_LIEGEN
			 * , new Exception("LS " + lsDto.getCNr() +
			 * " liegt nicht im selben GJ wie RE " + rechnung.getCNr())); }
			 */
			// Falls der Lieferschein vom gleichen Kunden stammt und es noch
			// keine LS-zuordnung gibt
			if (ls.getKundeIIdRechnungsadresse().equals(rechnung.getKundeIId())
					&& rechnung.getLieferscheinIId() == null) {
				// SP3184
				if (!rechnungartDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
					rechnung.setLieferscheinIId(ls.getIId());
				}
			}
			rePosDto = holeLieferscheinPreise(rePosDto, theClientDto);

			// Bei Proformarechnung wird der Lieferscheinstatus nicht veraendert
			if (!rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {

				getLieferscheinFac().setzeStatusLieferschein(
						rePosDto.getLieferscheinIId(),
						LieferscheinFac.LSSTATUS_VERRECHNET,
						rePosDto.getRechnungIId(), theClientDto);
			}

			// Wenn der Lieferschein ein Projekt hinterlegt hat, und die
			// Rechnung nicht, dann PJ an Rechnung uebergeben
			if (ls.getProjektIId() != null) {
				if (rechnung.getProjektIId() == null) {
					rechnung.setProjektIId(ls.getProjektIId());
				} else {
					// Wenn die Rechnung bereits ein Projekt hinterlegt hat,
					// und dieses ungleich dem Projekt des Lieferscheins ist,
					// dann darf das nicht gehen

					if (!rechnung.getProjektIId().equals(ls.getProjektIId())) {

						ArrayList alInfo = new ArrayList();

						ProjektDto pjDto = getProjektFac()
								.projektFindByPrimaryKey(
										rechnung.getProjektIId());
						String s = "RE" + rechnung.getCNr() + "(PJ"
								+ pjDto.getCNr() + ")";

						pjDto = getProjektFac().projektFindByPrimaryKey(
								ls.getProjektIId());
						s += " -> LS" + ls.getCNr() + " (PJ" + pjDto.getCNr()
								+ ")";
						alInfo.add(s);

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_LIEFERSCHEINE_MIT_VERSCHIEDENEN_PROJEKTEN,
								alInfo,
								new Exception(
										"FEHLER_LIEFERSCHEINE_MIT_VERSCHIEDENEN_PROJEKTEN"));
					}
				}
			}

			// PJ18728
			if (!rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				if (rechnung.getAuftragIId() == null
						&& ls.getAuftragIId() != null) {
					rechnung.setAuftragIId(ls.getAuftragIId());
				}
			}

		} else if (rePosDto.getRechnungpositionartCNr().equalsIgnoreCase(
				RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
			// einen Handartikel anlegen
			ArtikelDto artikelDto = new ArtikelDto();
			artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

			ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
			oArtikelsprDto.setCBez(rePosDto.getCBez());
			oArtikelsprDto.setCZbez(rePosDto.getCZusatzbez());

			artikelDto.setArtikelsprDto(oArtikelsprDto);
			artikelDto.setEinheitCNr(rePosDto.getEinheitCNr());

			// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					rePosDto.getMwstsatzIId(), theClientDto);
			artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
			// Artikel anlegen.
			Integer iIdArtikel = getArtikelFac().createArtikel(artikelDto,
					theClientDto);
			rePosDto.setArtikelIId(iIdArtikel);
		}
		if (rePosDto.getBDrucken() == null) {
			rePosDto.setBDrucken(Helper.boolean2Short(true));
		}

		RechnungPositionDto vorherigeDtoI = null;
		try {
			int iSort = getMaxISort(rePosDto.getBelegIId());
			if (rePosDto.getISort() != null) {
				iSort = rePosDto.getISort() - 1;
			}
			try {
				Query query = em
						.createNamedQuery("RechnungPositionfindRechnungIIdISort");
				query.setParameter(1, rePosDto.getBelegIId());
				query.setParameter(2, iSort);
				vorherigeDtoI = assembleRechnungPositionDto((Rechnungposition) query
						.getSingleResult());

			} catch (EJBExceptionLP ex1) {
			} catch (NoResultException ex1) {
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
			rePosDto = (RechnungPositionDto) befuellepositionBelegpositionDtoVerkauf(
					vorherigeDtoI, rePosDto, theClientDto);

			if (rePosDto.getAuftragpositionIId() != null) {
				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								rePosDto.getAuftragpositionIId());
				rePosDto.setXTextinhalt(auftragpositionDto.getXTextinhalt());
			} else {
				if (rePosDto.getPositionsartCNr().equalsIgnoreCase(
						RechnungFac.POSITIONSART_RECHNUNG_IDENT)
						&& rePosDto.getNMaterialzuschlagKurs() == null) {

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									rePosDto.getArtikelIId(), theClientDto);
					if (artikelDto.getMaterialIId() != null) {
						Rechnung rechnung = em.find(Rechnung.class,
								rePosDto.getRechnungIId());

						MaterialzuschlagDto mDto = getMaterialFac()
								.getKursMaterialzuschlagDtoInZielwaehrung(
										artikelDto.getMaterialIId(),
										new java.sql.Date(rechnung
												.getTBelegdatum().getTime()),
										rechnung.getWaehrungCNr(), theClientDto);
						if (mDto != null) {
							rePosDto.setNMaterialzuschlagKurs(mDto
									.getNZuschlag());
							rePosDto.setTMaterialzuschlagDatum(mDto
									.getTGueltigab());
						}
					}
				}
			}

			// PJ 13679
			istSteuersatzInPositionsartPositionGleich(rePosDto);

			Rechnungposition rechnungPosition = new Rechnungposition(
					rePosDto.getIId(), rePosDto.getRechnungIId(),
					rePosDto.getISort(), rePosDto.getBDrucken(),
					rePosDto.getBRabattsatzuebersteuert(),
					rePosDto.getBMwstsatzuebersteuert(),
					rePosDto.getBNettopreisuebersteuert());
			rechnungPosition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
			em.persist(rechnungPosition);
			em.flush();
			setRechnungpositionFromRechnungpositionDto(rechnungPosition,
					rePosDto);
			befuelleZusaetzlichePreisfelder(rePosDto.getIId(), theClientDto);

			rePosDto.setNEinzelpreisplusversteckteraufschlag(rechnungPosition
					.getNEinzelpreisplusaufschlag());

			rePosDto.setNNettoeinzelpreisplusversteckteraufschlag(rechnungPosition
					.getNNettoeinzelpreisplusaufschlag());
			rePosDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(rechnungPosition
					.getNNettoeinzelpreisplusaufschlagminusrabatt());

			// 15739
			pruefeLieferscheinLaenderAufGleichheit(rePosDto.getRechnungIId());

			// sofortige Lagerbuchung falls Artikel
			if (rePosDto.getRechnungpositionartCNr().equals(
					LocaleFac.POSITIONSART_IDENT)) {
				bucheRechnungPositionAmLager(rePosDto, lagerIId, false,
						theClientDto);
			}
			// Wenn es einen Bezug zum Auftrag gibt, muss die Auftragposition
			// angepasst werden
			if (rePosDto.getAuftragpositionIId() != null
					&& rePosDto.getNMenge() != null) {
				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						rePosDto.getAuftragpositionIId(), theClientDto);
			} else {
				if (rePosDto.getAuftragpositionIId() != null) {
					AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									rePosDto.getAuftragpositionIId());
					auftragpositionDto
							.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
					getAuftragpositionFac()
							.updateAuftragpositionOhneWeitereAktion(
									auftragpositionDto, theClientDto);
				}
			}

			sortierungAnpassenInBezugAufEndsumme(rePosDto.getBelegIId(),
					theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikel ebenfalls
			// buchen:
			if (rePosDto.getArtikelIId() != null) {

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								rePosDto.getArtikelIId(), theClientDto);
				if (stklDto != null
						&& artikelsetAufloesen
						&& stklDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {

					if (null == rePosDto.getAuftragpositionIId()) {
						RechnungPositionDto rechnungPositionDtoKopfartikel = createRechnungPositionFuerArtikelset(
								rePosDto, lagerIId, identities, iSort, stklDto,
								theClientDto);

						preiseEinesArtikelsetsUpdaten(
								rechnungPositionDtoKopfartikel.getIId(),
								theClientDto);
						rePosDto = rechnungPositionDtoKopfartikel;
					} else {
						RechnungPositionDto rechnungPositionDtoKopfartikel = createRechnungPositionFuerAuftragArtikelset(
								rePosDto, lagerIId, identities, iSort,
								theClientDto);

						preiseEinesArtikelsetsUpdaten(
								rechnungPositionDtoKopfartikel.getIId(),
								theClientDto);
						rePosDto = rechnungPositionDtoKopfartikel;

					}
				}
			}

			// das gespeicherte Objekt wieder an den client senden
			return rechnungPositionFindByPrimaryKey(rePosDto.getIId());
		} catch (EntityExistsException ex) {
			if (rePosDto.getLieferscheinIId() != null) {
				// Es handelt sich um eine Lieferscheinposition die gerade
				// doppelt verrechnet werden soll.
				EJBExceptionLP e = null;
				e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_RECHNUNG_POSITIONLS_EXISTIERT, ex);
				ArrayList<Object> alInfo = new ArrayList<Object>();
				alInfo.add(rePosDto.getLieferscheinIId());
				e.setAlInfoForTheClient(alInfo);
				throw e;
			}
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	// protected RechnungPositionDto
	// createRechnungPositionFuerArtikelsetAusAuftrag(
	// RechnungPositionDto rePosDto, Integer lagerIId,
	// List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
	// TheClientDto theClientDto) throws RemoteException {
	//
	// RechnungDto rechnungDto = rechnungFindByPrimaryKey(rePosDto
	// .getRechnungIId());
	// RechnungartDto rechnungartDto = getRechnungServiceFac()
	// .rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr(),
	// theClientDto);
	// boolean zubuchen = RechnungFac.RECHNUNGTYP_GUTSCHRIFT
	// .equals(rechnungartDto.getRechnungtypCNr());
	// getBelegVerkaufFac().setupPositionWithIdentities(zubuchen, rePosDto,
	// notyetUsedIdentities, theClientDto);
	// createRechnungPosition(rePosDto, lagerIId, theClientDto);
	// return rePosDto;
	// }
	//

	protected RechnungPositionDto createRechnungPositionFuerAuftragArtikelset(
			RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities, int iSort,
			TheClientDto theClientDto) throws RemoteException {

		RechnungPositionDto rechnungPositionDtoKopfartikel = rechnungPositionFindByPrimaryKey(rePosDto
				.getIId());

		Integer auftragpositionIId = rePosDto.getAuftragpositionIId();
		Auftragposition headAuftragposition = em.find(Auftragposition.class,
				auftragpositionIId);

		BigDecimal faktor = rePosDto.getNMenge().divide(
				headAuftragposition.getNMenge(), 6, BigDecimal.ROUND_HALF_EVEN);

		List<Auftragposition> auftragpositionen = AuftragpositionQuery
				.listByPositionIIdArtikelset(em, auftragpositionIId);

		for (Auftragposition auftragposition : auftragpositionen) {
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
					identities);

			rePosDto.setNEinzelpreis(BigDecimal.ZERO);
			rePosDto.setNNettoeinzelpreis(BigDecimal.ZERO);
			rePosDto.setFZusatzrabattsatz(0D);
			rePosDto.setFRabattsatz(0D);

			rePosDto.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			rePosDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			rePosDto.setNBruttoeinzelpreis(BigDecimal.ZERO);

			rePosDto.setNMenge(Helper.rundeKaufmaennisch(auftragposition
					.getNMenge().multiply(faktor), 4));

			rePosDto.setArtikelIId(auftragposition.getArtikelIId());
			rePosDto.setEinheitCNr(auftragposition.getEinheitCNr());
			rePosDto.setPositioniIdArtikelset(rechnungPositionDtoKopfartikel
					.getIId());
			rePosDto.setAuftragpositionIId(auftragposition.getIId());
			rePosDto.setIId(null);
			rePosDto.setSeriennrChargennrMitMenge(null);

			int iSortNeu = rePosDto.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
					rePosDto.getLieferscheinIId(), iSort);

			rePosDto.setISort(iSortNeu);

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rePosDto
					.getRechnungIId());
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			boolean zubuchen = RechnungFac.RECHNUNGTYP_GUTSCHRIFT
					.equals(rechnungartDto.getRechnungtypCNr());

			getBelegVerkaufFac().setupPositionWithIdentities(zubuchen,
					rePosDto, notyetUsedIdentities, theClientDto);
			createRechnungPosition(rePosDto, lagerIId, theClientDto);
		}

		return rechnungPositionDtoKopfartikel;
	}

	protected RechnungPositionDto createRechnungPositionFuerArtikelset(
			RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities, int iSort,
			StuecklisteDto stklDto, TheClientDto theClientDto)
			throws RemoteException {
		RechnungPositionDto rechnungPositionDtoKopfartikel = rechnungPositionFindByPrimaryKey(rePosDto
				.getIId());

		List<?> m = null;
		try {
			m = getStuecklisteFac()
					.getStrukturDatenEinerStueckliste(
							stklDto.getIId(),
							theClientDto,
							StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
							0, null, false, false, rePosDto.getNMenge(), null,
							true);
		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		// Zuerst Gesamtwert berechnen
		Iterator<?> it = m.listIterator();

		List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
				identities);

		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();
			StuecklistepositionDto position = struktur
					.getStuecklistepositionDto();

			rePosDto.setNEinzelpreis(new BigDecimal(0));
			rePosDto.setNNettoeinzelpreis(new BigDecimal(0));
			rePosDto.setFZusatzrabattsatz(0D);
			rePosDto.setFRabattsatz(0D);

			rePosDto.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
					0));
			rePosDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
					0));
			rePosDto.setNBruttoeinzelpreis(new BigDecimal(0));

			rePosDto.setNMenge(Helper.rundeKaufmaennisch(position
					.getNZielmenge().multiply(rePosDto.getNMenge()), 4));

			rePosDto.setArtikelIId(position.getArtikelIId());
			rePosDto.setEinheitCNr(position.getEinheitCNr());
			rePosDto.setPositioniIdArtikelset(rechnungPositionDtoKopfartikel
					.getIId());
			rePosDto.setIId(null);
			rePosDto.setSeriennrChargennrMitMenge(null);

			int iSortNeu = rePosDto.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
					rePosDto.getLieferscheinIId(), iSort);

			rePosDto.setISort(iSortNeu);

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rePosDto
					.getRechnungIId());
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			boolean zubuchen = RechnungFac.RECHNUNGTYP_GUTSCHRIFT
					.equals(rechnungartDto.getRechnungtypCNr());

			getBelegVerkaufFac().setupPositionWithIdentities(zubuchen,
					rePosDto, notyetUsedIdentities, theClientDto);
			createRechnungPosition(rePosDto, lagerIId, theClientDto);
		}
		return rechnungPositionDtoKopfartikel;
	}

	private BigDecimal totalSnrMenge(
			List<SeriennrChargennrMitMengeDto> knownSnrs) {
		BigDecimal knownMenge = BigDecimal.ZERO;
		for (SeriennrChargennrMitMengeDto seriennrChargennrMitMengeDto : knownSnrs) {
			knownMenge = knownMenge.add(seriennrChargennrMitMengeDto
					.getNMenge());
		}
		return knownMenge;
	}

	// private void setupRechnungPositionWithIdentities(
	// RechnungPositionDto rePosDto,
	// List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
	// TheClientDto theClientDto) {
	// ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKey(
	// rePosDto.getArtikelIId(), theClientDto);
	// if (!(artikel.isChargennrtragend() || artikel.isSeriennrtragend()))
	// return;
	//
	// BigDecimal knownMenge = BigDecimal.ZERO;
	// boolean foundEntry = false;
	// do {
	// foundEntry = false;
	// if (null == rePosDto.getSeriennrChargennrMitMenge()) {
	// rePosDto.setSeriennrChargennrMitMenge(new
	// ArrayList<SeriennrChargennrMitMengeDto>());
	// }
	//
	// knownMenge = totalSnrMenge(rePosDto.getSeriennrChargennrMitMenge());
	// if (knownMenge.compareTo(rePosDto.getNMenge()) >= 0)
	// break;
	//
	// for (SeriennrChargennrMitMengeDto snrMengeDto : notyetUsedIdentities) {
	// Integer artikelIIdGefunden = getLagerFac()
	// .getArtikelIIdUeberSeriennummer(
	// snrMengeDto.getCSeriennrChargennr(),
	// theClientDto);
	//
	// if (!rePosDto.getArtikelIId().equals(artikelIIdGefunden))
	// continue;
	//
	// if (knownMenge.add(snrMengeDto.getNMenge()).compareTo(
	// rePosDto.getNMenge()) <= 0) {
	// rePosDto.getSeriennrChargennrMitMenge().add(snrMengeDto);
	// notyetUsedIdentities.remove(snrMengeDto);
	// foundEntry = true;
	// break;
	// }
	// }
	// } while (foundEntry && knownMenge.compareTo(rePosDto.getNMenge()) < 0);
	//
	// knownMenge = totalSnrMenge(rePosDto.getSeriennrChargennrMitMenge());
	// if (knownMenge.compareTo(rePosDto.getNMenge()) < 0) {
	//
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH,
	// new Exception(artikel.getCNr() + "("
	// + knownMenge.toString() + " von "
	// + rePosDto.getNMenge() + " vorhanden)"));
	//
	// }
	// }

	private void istSteuersatzInPositionsartPositionGleich(
			RechnungPositionDto rePosDto) {
		if (rePosDto.getTypCNr() != null
				&& (rePosDto.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1) || rePosDto
						.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2))
				&& rePosDto.getPositioniId() != null) {
			RechnungPositionDto[] dtos = rechnungPositionFindByPositionIId(rePosDto
					.getPositioniId());
			for (int i = 0; i < dtos.length; i++) {
				if (rePosDto.getMwstsatzIId() != null) {
					if (dtos[i].getMwstsatzIId() != null) {
						if (!rePosDto.getMwstsatzIId().equals(
								dtos[i].getMwstsatzIId())) {
							// MWST-Saetze innerhalb "Position" muessen
							// immer gleich sein
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH,
									"");
						}
					}
				}
			}
		}
	}

	public RechnungPositionDto befuelleZusaetzlichePreisfelder(
			Integer iIdPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Rechnungposition oPosition = em.find(Rechnungposition.class,
				iIdPositionI);
		if (oPosition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		RechnungPositionDto rechnungPositionDto = rechnungPositionFindByPrimaryKey(iIdPositionI);
		if (oPosition.getPositionsartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
				|| oPosition.getPositionsartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(oPosition
					.getRechnungIId());
			rechnungPositionDto = (RechnungPositionDto) getBelegVerkaufFac()
					.berechneBelegpositionVerkauf(rechnungPositionDto,
							rechnungDto);
			oPosition.setNEinzelpreisplusaufschlag(rechnungPositionDto
					.getNEinzelpreisplusversteckteraufschlag());
			oPosition.setNNettoeinzelpreisplusaufschlag(rechnungPositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			oPosition
					.setNNettoeinzelpreisplusaufschlagminusrabatt(rechnungPositionDto
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

		}
		return rechnungPositionDto;
	}

	public RechnungPositionDto befuelleZusaetzlichePreisfelder(
			Integer iIdPositionI, BigDecimal zwsRabattsatz,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Rechnungposition oPosition = em.find(Rechnungposition.class,
				iIdPositionI);
		if (oPosition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		RechnungPositionDto rechnungPositionDto = rechnungPositionFindByPrimaryKey(iIdPositionI);
		if (oPosition.getPositionsartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
				|| oPosition.getPositionsartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(oPosition
					.getRechnungIId());
			rechnungPositionDto = (RechnungPositionDto) getBelegVerkaufFac()
					.berechneBelegpositionVerkauf(zwsRabattsatz,
							rechnungPositionDto, rechnungDto);
			oPosition.setNEinzelpreisplusaufschlag(rechnungPositionDto
					.getNEinzelpreisplusversteckteraufschlag());
			oPosition.setNNettoeinzelpreisplusaufschlag(rechnungPositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			oPosition
					.setNNettoeinzelpreisplusaufschlagminusrabatt(rechnungPositionDto
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

		}
		return rechnungPositionDto;
	}

	private void pruefePflichtfelderBelegposition(RechnungPositionDto rePosDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDtoVerkauf(rePosDto, theClientDto);
		if (rePosDto.getFKupferzuschlag() == null) {
			rePosDto.setFKupferzuschlag(0.0);
		}
	}

	private RechnungPositionDto holeLieferscheinPreise(
			RechnungPositionDto position, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinpositionDto[] lsDtos = null;
		LieferscheinDto lsDto = null;
		try {
			lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(
					position.getLieferscheinIId(), theClientDto);
			lsDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(
							position.getLieferscheinIId());
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
		// zuerst alle werte null setzen
		BigDecimal wert = new BigDecimal(0);
		// und jetzt kumulieren
		for (int i = 0; i < lsDtos.length; i++) {
			if (lsDtos[i]
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null
					&& lsDtos[i].getNMenge() != null) {
				// PJ15321
				if (lsDtos[i].getPositioniIdArtikelset() == null) {
					wert = wert
							.add(lsDtos[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(lsDtos[i].getNMenge()));

					if (lsDtos[i].getVerleihIId() != null) {
						VerleihDto verleihDto = getArtikelFac()
								.verleihFindByPrimaryKey(

								lsDtos[i].getVerleihIId());

						wert = wert.multiply(new BigDecimal(verleihDto
								.getFFaktor()));
					}

				}
			}
		}

		String sRechnungswaehrung = rechnungFindByPrimaryKey(
				position.getRechnungIId()).getWaehrungCNr();
		String sLieferscheinwaehrung = lsDto.getWaehrungCNr();
		if (!sRechnungswaehrung.equals(sLieferscheinwaehrung)) {
			try {
				wert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(wert,
						sLieferscheinwaehrung, sRechnungswaehrung,
						new Date(System.currentTimeMillis()), theClientDto);
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
			}
		}
		wert = Helper.rundeKaufmaennisch(wert, 4);
		position.setNEinzelpreis(wert);
		position.setNNettoeinzelpreis(wert);
		return position;
	}

	public RechnungPositionDto[] getArtikelsetForIId(Integer kopfartikelIId) {
		Query query = em
				.createNamedQuery("RechnungPositionfindByPositionIIdArtikelset");
		query.setParameter(1, kopfartikelIId);
		Collection<?> rechnungpositionDtos = query.getResultList();
		return assembleRechnungpositionDtos(rechnungpositionDtos);
	}

	public Integer gehoertZuArtikelset(Integer rechnungpositionIId) {

		Rechnungposition oPosition1 = em.find(Rechnungposition.class,
				rechnungpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return oPosition1.getPositionIIdArtikelset();
		}

		RechnungPositionDto[] zugehoerigeREPosDtos = getArtikelsetForIId(rechnungpositionIId);

		// Query query = em
		// .createNamedQuery("RechnungPositionfindByPositionIIdArtikelset");
		// query.setParameter(1, rechnungpositionIId);
		// Collection<?> lieferscheinpositionDtos = query.getResultList();
		// RechnungPositionDto[] zugehoerigeREPosDtos =
		// assembleRechnungpositionDtos(lieferscheinpositionDtos);

		if (zugehoerigeREPosDtos != null && zugehoerigeREPosDtos.length > 0) {
			return rechnungpositionIId;
		}

		return null;
	}

	private int findNextPosition(int startIndex, int endIndex,
			RechnungPositionDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();

		while (startIndex <= dtos.length && (startIndex < endIndex)) {
			if (dtoNumberHandler
					.hasPositionNummer(new RechnungPositionNumberDtoAdapter(
							dtos[startIndex]))) {
				return startIndex;
			}

			++startIndex;
		}

		return -1;
	}

	private int findPreviousPosition(int startIndex, RechnungPositionDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();
		while (startIndex >= 0) {
			if (dtoNumberHandler
					.hasPositionNummer(new RechnungPositionNumberDtoAdapter(
							dtos[startIndex]))) {
				return startIndex;
			}

			--startIndex;
		}

		return -1;
	}

	private int findNextPossiblePosition(int startIndex, int endIndex,
			RechnungPositionDto[] dtos) {
		// Die Von-Position wird geloescht.
		int savedPositionIndex = startIndex;
		startIndex = findNextPosition(startIndex + 1, endIndex, dtos);
		if (-1 == startIndex) {
			startIndex = findPreviousPosition(savedPositionIndex - 1, dtos);
		}

		return startIndex;
	}

	private void processIntelligenteZwischensummeRemove(
			RechnungDto rechnungDto, RechnungPositionDto rechnungPositionDto)
			throws EJBExceptionLP {

		RechnungPositionDto[] dtos = rechnungPositionFindByRechnungIId(rechnungDto
				.getIId());

		getBelegVerkaufFac().processIntelligenteZwischensummeRemove(
				rechnungDto, rechnungPositionDto, dtos);
	}

	public void removeRechnungPosition(RechnungPositionDto rechnungPositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (rechnungPositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"rechnungPositionDto == null"));
		}
		if (rechnungPositionDto.getRechnungIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"rechnungIId == null"));
		}

		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungPositionDto
					.getRechnungIId());

			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);

			processIntelligenteZwischensummeRemove(rechnungDto,
					rechnungPositionDto);

			if (rechnungPositionDto.getPositioniIdArtikelset() == null) {
				Query query = em
						.createNamedQuery("RechnungPositionfindByPositionIIdArtikelset");
				query.setParameter(1, rechnungPositionDto.getIId());
				Collection<?> lieferscheinpositionDtos = query.getResultList();
				RechnungPositionDto[] zugehoerigeREPosDtos = assembleRechnungpositionDtos(lieferscheinpositionDtos);

				for (int i = 0; i < zugehoerigeREPosDtos.length; i++) {
					removeRechnungPosition(zugehoerigeREPosDtos[i],
							theClientDto);
				}
			}

			if (rechnungPositionDto.getAuftragpositionIId() != null
					&& rechnungPositionDto.getNMenge() == null) {
				try {
					AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									rechnungPositionDto.getAuftragpositionIId());
					oAuftragpositionDto
							.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
					getAuftragpositionFac()
							.updateAuftragpositionOhneWeitereAktion(
									oAuftragpositionDto, theClientDto);
				} catch (RemoteException ex) {
				} catch (EJBExceptionLP ex) {
				}

			}

			// ist ein Lieferschein, muss ich den status wieder
			// zuruecksetzen
			if (rechnungPositionDto
					.getRechnungpositionartCNr()
					.trim()
					.equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN
							.trim())) {

				// SP3183 Wenn der LS bereits verrechnet und in einer
				// Progormarechnung verwendet wird, darf dieser nicht mehr aus
				// der Proformarechnung geloescht werden
				if (rechnungartDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {

					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									rechnungPositionDto.getLieferscheinIId());
					if (lsDto.getRechnungIId() != null
							&& lsDto.getStatusCNr().equals(
									LieferscheinFac.LSSTATUS_VERRECHNET)) {

						RechnungDto reDto = getRechnungFac()
								.rechnungFindByPrimaryKey(
										lsDto.getRechnungIId());

						ArrayList al = new ArrayList();
						al.add(reDto.getCNr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_LIEFERSCHEIN_BEREITS_IN_PROFORMARECHNUNG,
								al,
								new Exception(
										"FEHLER_LIEFERSCHEIN_BEREITS_IN_PROFORMARECHNUNG"));
					}

				}

				// Eventuell vorhandene Zuordnung in den Kopfdaten loeschen
				if (rechnungDto.getLieferscheinIId() != null
						&& rechnungDto.getLieferscheinIId().equals(
								rechnungPositionDto.getLieferscheinIId())) {
					rechnungDto.setLieferscheinIId(null);
					updateRechnung(rechnungDto, theClientDto);
					Rechnung re = em.find(Rechnung.class, rechnungDto.getIId());
					re.setLieferscheinIId(null);
					em.merge(re);
					em.flush();
				}
				// Status des Lieferscheins wieder auf Geliefert
				getLieferscheinFac().setzeStatusLieferschein(
						rechnungPositionDto.getLieferscheinIId(),
						LieferscheinFac.LSSTATUS_GELIEFERT, null, theClientDto);
			} else if (rechnungPositionDto.getRechnungpositionartCNr().trim()
					.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT.trim())) {
				bucheRechnungPositionAmLager(rechnungPositionDto,
						rechnungDto.getLagerIId(), true, theClientDto);
			}
			// Sortierung anpassen
			sortierungAnpassenBeiLoeschenEinerPosition(rechnungDto.getIId(),
					rechnungPositionDto.getISort().intValue());
			// jetzt die position loeschen
			Rechnungposition toRemove = em.find(Rechnungposition.class,
					rechnungPositionDto.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();

				if (rechnungPositionDto.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(
							rechnungPositionDto.getPositioniIdArtikelset(),
							theClientDto);
				}

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}

			// Wenn es einen Bezug zum Auftrag gibt, muss die
			// Auftragposition angepasst werden
			if (rechnungPositionDto.getAuftragpositionIId() != null
					&& rechnungPositionDto.getNMenge() != null) {
				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						rechnungPositionDto.getAuftragpositionIId(),
						theClientDto);
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	private RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto, boolean bucheAmLager,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return updateRechnungPositionImpl(rechnungPositionDto, bucheAmLager,
				new ArrayList<SeriennrChargennrMitMengeDto>(), null, true,
				theClientDto);
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return updateRechnungPosition(rechnungPositionDto, true, theClientDto);
	}

	private RechnungPositionDto updateRechnungPositionOhneNeuBerechnung(
			RechnungPositionDto rechnungPositionDto, TheClientDto theClientDto) {
		return updateRechnungPositionImpl(rechnungPositionDto, true,
				new ArrayList<SeriennrChargennrMitMengeDto>(), null, false,
				theClientDto);
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return updateRechnungPositionImpl(rechnungPositionDto, true,
				notyetUsedIdentities, null, true, theClientDto);
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			Artikelset artikelset, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return updateRechnungPositionImpl(rechnungPositionDto, true,
				notyetUsedIdentities, artikelset, true, theClientDto);
	}

	private void preiseEinesArtikelsetsUpdaten(
			Integer rechnungpositionIIdKopfartikel, TheClientDto theClientDto) {

		RechnungPositionDto rechnungPositionDtoKopfartikel = rechnungPositionFindByPrimaryKeyOhneExc(rechnungpositionIIdKopfartikel);

		if (rechnungPositionDtoKopfartikel != null) {

			Query query = em
					.createNamedQuery("RechnungPositionfindByPositionIIdArtikelset");
			query.setParameter(1, rechnungPositionDtoKopfartikel.getIId());
			Collection<?> lieferscheinpositionDtos = query.getResultList();
			try {
				RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(
								rechnungPositionDtoKopfartikel.getRechnungIId());

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnungDto.getKundeIId(), theClientDto);

				Integer mwstsatzbezIId = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								rechnungPositionDtoKopfartikel.getMwstsatzIId(),
								theClientDto).getIIMwstsatzbezId();

				// Zuerst Gesamtwert berechnen
				BigDecimal bdMenge = rechnungPositionDtoKopfartikel.getNMenge();

				BigDecimal bdNettoeinzelpreis = rechnungPositionDtoKopfartikel
						.getNNettoeinzelpreis();

				BigDecimal bdGesamtwertposition = bdMenge
						.multiply(bdNettoeinzelpreis);

				BigDecimal bdGesamtVKwert = new BigDecimal(0);

				Iterator<?> it = lieferscheinpositionDtos.iterator();

				while (it.hasNext()) {
					Rechnungposition struktur = (Rechnungposition) it.next();

					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									struktur.getArtikelIId(),
									rechnungDto.getKundeIId(),

									struktur.getNMenge(),
									new java.sql.Date(System
											.currentTimeMillis()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													mwstsatzbezIId,
													theClientDto).getIId(),
									rechnungDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper
							.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null
							&& kundenVKPreisDto.nettopreis != null) {
						bdGesamtVKwert = bdGesamtVKwert
								.add(kundenVKPreisDto.nettopreis
										.multiply(struktur.getNMenge()));
					}

				}

				bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);

				it = lieferscheinpositionDtos.iterator();

				while (it.hasNext()) {
					Rechnungposition struktur = (Rechnungposition) it.next();

					struktur.setNEinzelpreis(new BigDecimal(0));
					struktur.setNNettoeinzelpreis(new BigDecimal(0));

					struktur.setNNettoeinzelpreisplusaufschlag(new BigDecimal(0));
					struktur.setNNettoeinzelpreisplusaufschlagminusrabatt(new BigDecimal(
							0));
					struktur.setNBruttoeinzelpreis(new BigDecimal(0));

					// Mehrwertsteuersatz: Kommt immer aus dem Kopfartikel,
					// da dieser die Hauptleistung darstellt

					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									struktur.getArtikelIId(),
									rechnungDto.getKundeIId(),
									struktur.getNMenge(),
									new java.sql.Date(System
											.currentTimeMillis()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													mwstsatzbezIId,
													theClientDto).getIId(),
									rechnungDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper
							.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null
							&& kundenVKPreisDto.nettopreis != null
							&& bdGesamtVKwert.doubleValue() != 0) {
						// Preis berechnen
						BigDecimal bdAnteilVKWert = kundenVKPreisDto.nettopreis
								.multiply(
										struktur.getNMenge().multiply(bdMenge))
								.divide(bdGesamtVKwert, 4,
										BigDecimal.ROUND_HALF_EVEN);

						struktur.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);

						struktur.setNEinzelpreis(bdGesamtwertposition.multiply(
								bdAnteilVKWert).divide(
								struktur.getNMenge().multiply(bdMenge), 4,
								BigDecimal.ROUND_HALF_EVEN));

						struktur.setNNettoeinzelpreis(bdGesamtwertposition
								.multiply(bdAnteilVKWert).divide(
										struktur.getNMenge().multiply(bdMenge),
										4, BigDecimal.ROUND_HALF_EVEN));

						struktur.setNBruttoeinzelpreis(bdGesamtwertposition
								.multiply(bdAnteilVKWert).divide(
										struktur.getNMenge().multiply(bdMenge),
										4, BigDecimal.ROUND_HALF_EVEN));

						struktur.setNNettoeinzelpreisplusaufschlag(new BigDecimal(
								0));
						struktur.setNNettoeinzelpreisplusaufschlagminusrabatt(new BigDecimal(
								0));

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);

			}
		}
	}

	private void updateTAendern(Integer rechnungIId, TheClientDto theClientDto) {
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		rechnung.setTAendern(getTimestamp());
		rechnung.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(rechnung);
		em.flush();
	}

	private RechnungPositionDto updateRechnungPositionImpl(
			RechnungPositionDto rechnungPositionDto, boolean bucheAmLager,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			Artikelset artikelset, boolean recalculatePosition,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (rechnungPositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"rechnungPositionDto == null"));
		}
		updateTAendern(rechnungPositionDto.getRechnungIId(), theClientDto);
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungPositionDto
				.getRechnungIId());
		pruefePflichtfelderBelegposition(rechnungPositionDto, theClientDto);
		try {

			// Wurde ein anderer Artikel gewaehlt?
			boolean bArtikelGeaendert = true;
			Rechnungposition rechnungPosition = em.find(Rechnungposition.class,
					rechnungPositionDto.getIId());
			if (rechnungPosition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			if (rechnungPositionDto.getArtikelIId() == null
					|| rechnungPositionDto.getArtikelIId().equals(
							rechnungPosition.getArtikelIId())) {
				bArtikelGeaendert = false;
			}

			Integer rechnungPositionIIdKopfartikel = null;
			if (!bArtikelGeaendert) {
				rechnungPositionIIdKopfartikel = gehoertZuArtikelset(rechnungPositionDto
						.getIId());
				// bArtikelGeaendert = rechnungPositionIIdKopfartikel != null;
			}

			if (bArtikelGeaendert) {
				// Anderer Artikel: Position loeschen und neu anlegen.
				// damit muessen komplizierte dinge wie lagerbuchung nicht extra
				// behandelt werden.
				RechnungPositionDto temp = assembleRechnungPositionDto(rechnungPosition);

				// removeRechnungPosition setzt nmenge auf 0. in
				// rechnungpositiondto.nmenge
				// steht die neue zu verwendende Menge drinnen.
				temp.setNMenge(rechnungPositionDto.getNMenge());
				removeRechnungPosition(rechnungPositionDto, theClientDto);

				if (isArtikelSetHead(rechnungPositionDto.getArtikelIId(),
						theClientDto)
						&& (rechnungPositionDto.getAuftragpositionIId() != null)) {
					AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									rechnungPositionDto.getAuftragpositionIId());

					List<Artikelset> artikelsets = new ArrayList<Artikelset>();

					// Im Artikelset ist die IID der Rechnungsposition. Wir
					// gehen aber nun von der
					// Auftragsposition aus.
					if (artikelset != null) {
						artikelset.getHead().setIId(
								rechnungPositionDto.getAuftragpositionIId());
						artikelset.getHead().setNMenge(temp.getNMenge());
						artikelsets.add(artikelset);
					}

					RechnungPositionDto newPositionDto = uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
							rechnungDto.getIId(),
							auftragpositionDto.getBelegIId(), artikelsets,
							theClientDto);
					rechnungPosition = em.find(Rechnungposition.class,
							newPositionDto.getIId());

					setRechnungpositionFromRechnungpositionDto(
							rechnungPosition, rechnungPositionDto);
					if (recalculatePosition) {
						befuelleZusaetzlichePreisfelder(
								newPositionDto.getIId(), theClientDto);
					}
					return newPositionDto;
				} else {
					RechnungPositionDto newPositionDto = createRechnungPosition(
							temp, rechnungDto.getLagerIId(),
							notyetUsedIdentities, theClientDto);

					rechnungPosition = em.find(Rechnungposition.class,
							newPositionDto.getIId());

					// buchevomLager setzt die Menge explizit auf 0
					rechnungPositionDto.setNMenge(temp.getNMenge());
					setRechnungpositionFromRechnungpositionDto(
							rechnungPosition, rechnungPositionDto);
					if (recalculatePosition) {
						befuelleZusaetzlichePreisfelder(
								newPositionDto.getIId(), theClientDto);
					}
					return newPositionDto;
				}
			} else {
				if (rechnungPositionDto.getRechnungpositionartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
					// Preisupdate auf Lieferscheinpositionen (Rechnung -> Sicht
					// Lieferschein)
					rechnungPositionDto = holeLieferscheinPreise(
							rechnungPositionDto, theClientDto);
					Rechnungposition rechnungposition = em.find(
							Rechnungposition.class,
							rechnungPositionDto.getIId());
					if (!rechnungposition.getLieferscheinIId().equals(
							rechnungPositionDto.getLieferscheinIId())) {
						if (rechnungposition.getISort() == 0) {
							Rechnung rechnung = em.find(Rechnung.class,
									rechnungPositionDto.getRechnungIId());
							rechnung.setLieferscheinIId(rechnungPositionDto
									.getLieferscheinIId());
						}
						// Status des Lieferscheins wieder auf Geliefert
						getLieferscheinFac().setzeStatusLieferschein(
								rechnungposition.getLieferscheinIId(),
								LieferscheinFac.LSSTATUS_GELIEFERT, null,
								theClientDto);
						if (rechnungposition.getLieferscheinIId() != null) {
							Lieferschein ls = em.find(Lieferschein.class,
									rechnungposition.getLieferscheinIId());
							ls.setRechnungIId(null);
						}
						getLieferscheinFac().setzeStatusLieferschein(
								rechnungPositionDto.getLieferscheinIId(),
								LieferscheinFac.LSSTATUS_VERRECHNET,
								rechnungPositionDto.getRechnungIId(),
								theClientDto);
					}
				}
				// Handeingabe. eventuell muss die Bezeichnung des Handartikels
				// upgedatet werden
				else if (rechnungPositionDto.getRechnungpositionartCNr()
						.equals(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
					// Preisupdate auf Lieferscheinpositionen (Rechnung -> Sicht
					// Lieferschein)
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									rechnungPositionDto.getArtikelIId(),
									theClientDto);
					// Wenn der noch kein sprDto hat, dann kriegt er jetzt eins.
					if (artikelDto.getArtikelsprDto() == null) {
						artikelDto.setArtikelsprDto(new ArtikelsprDto());
					}
					artikelDto.getArtikelsprDto().setCBez(
							rechnungPositionDto.getCBez());
					artikelDto.getArtikelsprDto().setCZbez(
							rechnungPositionDto.getCZusatzbez());
					// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(
									rechnungPositionDto.getMwstsatzIId(),
									theClientDto);
					artikelDto.setMwstsatzbezIId(mwstsatzDto
							.getIIMwstsatzbezId());
					// Artikel speichern
					getArtikelFac().updateArtikel(artikelDto, theClientDto);
				}
				BigDecimal bdVorherigeMenge = rechnungPosition.getNMenge();
				// Rechnungsposition speichern.
				setRechnungpositionFromRechnungpositionDto(rechnungPosition,
						rechnungPositionDto);

				if (recalculatePosition) {
					List<SeriennrChargennrMitMengeDto> snrsVorher = rechnungPositionDto
							.getSeriennrChargennrMitMenge();
					rechnungPositionDto = befuelleZusaetzlichePreisfelder(
							rechnungPositionDto.getIId(), theClientDto);
					rechnungPositionDto
							.setSeriennrChargennrMitMenge(snrsVorher);
				}

				// PJ 13679
				istSteuersatzInPositionsartPositionGleich(rechnungPositionDto);

				// Lagerbuchung
				if (bucheAmLager) {
					// sofortige Lagerbuchung falls Artikel
					if (rechnungPositionDto.getRechnungpositionartCNr().equals(
							LocaleFac.POSITIONSART_IDENT)) {
						boolean bLoescheBuchungOrUpdateAufMengeNull = false;
						// Wenn die Menge von einem anderen Wert auf 0 geaendert
						// wurde,
						// dann wirkt das auf die Lagerbuchung wie loeschen.
						if (bdVorherigeMenge != null
								&& bdVorherigeMenge
										.compareTo(new BigDecimal(0)) != 0
								&& rechnungPositionDto.getNMenge().compareTo(
										new BigDecimal(0)) == 0) {
							bLoescheBuchungOrUpdateAufMengeNull = true;
						}

						bucheRechnungPositionAmLager(rechnungPositionDto,
								rechnungDto.getLagerIId(),
								bLoescheBuchungOrUpdateAufMengeNull,
								theClientDto);
					}
				}

				// die offene Menge im Auftrag korrigieren
				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						rechnungPositionDto.getAuftragpositionIId(),
						theClientDto);

				// Wenn Teil eines Artikelsets, dann muessen die Preise neu
				// berechnet werden
				if (rechnungPositionIIdKopfartikel != null) {
					updateArtikelsetMengen(bucheAmLager, bdVorherigeMenge,
							rechnungPositionDto, notyetUsedIdentities,
							theClientDto);

					preiseEinesArtikelsetsUpdaten(
							rechnungPositionIIdKopfartikel, theClientDto);

				}

				RechnungPositionDto positionDto = rechnungPositionFindByPrimaryKey(rechnungPositionDto
						.getIId());
				return positionDto;
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	private void updateArtikelsetMengen(boolean bucheAmLager,
			BigDecimal oldHeadAmount, RechnungPositionDto newPositionDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			TheClientDto theClientDto) throws RemoteException {
		if (newPositionDto.getNMenge() == null)
			return;
		if (newPositionDto.getNMenge().compareTo(oldHeadAmount) == 0)
			return;

		if (newPositionDto.getArtikelIId() == null)
			return;

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						newPositionDto.getArtikelIId(), theClientDto);
		if (stklDto == null)
			return;
		if (!stklDto.getStuecklisteartCNr().equals(
				StuecklisteFac.STUECKLISTEART_SETARTIKEL))
			return;

		// Wir haben einen Setartikel-Kopf und es wurde die Menge geaendert ->
		// die Mengen des Sets anpassen
		RechnungPositionDto[] rechnungPositionDtos = getArtikelsetForIId(newPositionDto
				.getIId());
		BigDecimal newHeadAmount = newPositionDto.getNMenge();

		RechnungDto rechnungDto = rechnungFindByPrimaryKey(newPositionDto
				.getRechnungIId());
		RechnungartDto rechnungartDto = getRechnungServiceFac()
				.rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr(),
						theClientDto);
		boolean zubuchen = RechnungFac.RECHNUNGTYP_GUTSCHRIFT
				.equals(rechnungartDto.getRechnungtypCNr());

		for (RechnungPositionDto rechnungposition : rechnungPositionDtos) {
			if (rechnungposition.getNMenge() != null) {
				BigDecimal savedNMenge = rechnungposition.getNMenge();

				if (bucheAmLager) {
					bucheRechnungPositionAmLager(rechnungposition,
							rechnungDto.getLagerIId(), true, theClientDto);
				}

				rechnungposition.setNMenge(savedNMenge.divide(oldHeadAmount)
						.multiply(newHeadAmount));

				rechnungposition.setSeriennrChargennrMitMenge(null); // TODO:
				// Alte
				// Seriennummern
				// rausschmeissen
				getBelegVerkaufFac().setupPositionWithIdentities(zubuchen,
						rechnungposition, notyetUsedIdentities, theClientDto);

				updateRechnungPositionImpl(rechnungposition, true,
						notyetUsedIdentities, null, true, theClientDto);
			}
		}
	}

	public RechnungPositionDto rechnungPositionFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Rechnungposition rechnungposition = em
				.find(Rechnungposition.class, iId);
		if (rechnungposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		RechnungPositionDto repos = assembleRechnungPositionDto(rechnungposition);

		return repos;
	}

	public RechnungPositionDto rechnungPositionFindByPrimaryKeyOhneExc(
			Integer iId) {
		Rechnungposition rechnungposition = em
				.find(Rechnungposition.class, iId);
		if (rechnungposition == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		return assembleRechnungPositionDto(rechnungposition);
	}

	public RechnungPositionDto[] rechnungPositionFindByRechnungIId(
			Integer rechnungIId) throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungpositionDtos(cl);
	}

	public RechnungPositionDto[] rechnungPositionFindByPositionIId(
			Integer positionIId) throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungPositionfindByPositionIId");
		query.setParameter(1, positionIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungpositionDtos(cl);
	}

	public RechnungPositionDto rechnungPositionFindByLieferscheinIId(
			Integer lieferscheinIId) {
		Query query = em
				.createNamedQuery("RechnungPositionfindByLieferscheinIId");
		query.setParameter(1, lieferscheinIId);
		Rechnungposition rechnungposition = (Rechnungposition) query
				.getSingleResult();
		return assembleRechnungPositionDto(rechnungposition);
	}

	public RechnungPositionDto rechnungPositionFindPositionIIdISort(
			Integer positionIId, Integer iSort) throws EJBExceptionLP {
		Rechnungposition rechnungposition = null;
		try {
			Query query = em
					.createNamedQuery("RechnungPositionfindPositionIIdISort");
			query.setParameter(1, positionIId);
			query.setParameter(2, iSort);
			rechnungposition = (Rechnungposition) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		return assembleRechnungPositionDto(rechnungposition);
	}

	public RechnungPositionDto[] rechnungPositionFindByRechnungIIdArtikelIId(
			Integer rechnungIId, Integer artikelIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungPositionfindByRechnungIIdArtikelIId");
			query.setParameter(1, rechnungIId);
			query.setParameter(2, artikelIId);
			Collection c = query.getResultList();
			return assembleRechnungpositionDtos(c);
		} catch (NoResultException ex) {
			return null;
		}

	}

	public RechnungPositionDto[] rechnungPositionFindByArtikelIId(
			Integer artikelIId) throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungPositionfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungpositionDtos(cl);
	}

	private void setRechnungpositionFromRechnungpositionDto(
			Rechnungposition rechnungPosition,
			RechnungPositionDto rechnungPositionDto) {
		rechnungPosition.setRechnungIId(rechnungPositionDto.getRechnungIId());
		rechnungPosition.setPositionsartCNr(rechnungPositionDto
				.getRechnungpositionartCNr());
		rechnungPosition.setISort(rechnungPositionDto.getISort());
		rechnungPosition.setRechnungIIdGutschrift(rechnungPositionDto
				.getRechnungIIdGutschrift());
		rechnungPosition.setLieferscheinIId(rechnungPositionDto
				.getLieferscheinIId());
		rechnungPosition.setRechnungpositionIId(rechnungPositionDto
				.getRechnungpositionIId());
		rechnungPosition.setAuftragpositionIId(rechnungPositionDto
				.getAuftragpositionIId());
		rechnungPosition.setCBez(rechnungPositionDto.getCBez());
		rechnungPosition.setCZbez(rechnungPositionDto.getCZusatzbez());
		rechnungPosition.setXTextinhalt(rechnungPositionDto.getXTextinhalt());
		rechnungPosition.setMediastandardIId(rechnungPositionDto
				.getMediastandardIId());
		rechnungPosition.setArtikelIId(rechnungPositionDto.getArtikelIId());
		rechnungPosition.setNMenge(rechnungPositionDto.getNMenge());
		rechnungPosition.setEinheitCNr(rechnungPositionDto.getEinheitCNr());
		rechnungPosition.setBDrucken(rechnungPositionDto.getBDrucken());
		rechnungPosition.setFKupferzuschlag(rechnungPositionDto
				.getFKupferzuschlag());
		rechnungPosition.setFRabattsatz(rechnungPositionDto.getFRabattsatz());
		rechnungPosition.setBRabattsatzuebersteuert(rechnungPositionDto
				.getBRabattsatzuebersteuert());
		rechnungPosition.setMwstsatzIId(rechnungPositionDto.getMwstsatzIId());
		rechnungPosition.setBMwstsatzuebersteuert(rechnungPositionDto
				.getBMwstsatzuebersteuert());
		rechnungPosition.setBNettopreisuebersteuert(rechnungPositionDto
				.getBNettopreisuebersteuert());
		rechnungPosition.setNEinzelpreis(rechnungPositionDto.getNEinzelpreis());
		rechnungPosition.setNEinzelpreisplusaufschlag(rechnungPositionDto
				.getNEinzelpreisplusversteckteraufschlag());
		rechnungPosition.setNNettoeinzelpreis(rechnungPositionDto
				.getNNettoeinzelpreis());
		rechnungPosition.setNNettoeinzelpreisplusaufschlag(rechnungPositionDto
				.getNNettoeinzelpreisplusversteckteraufschlag());
		rechnungPosition
				.setNNettoeinzelpreisplusaufschlagminusrabatt(rechnungPositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		rechnungPosition.setNBruttoeinzelpreis(rechnungPositionDto
				.getNBruttoeinzelpreis());
		rechnungPosition.setFZusatzrabattsatz(rechnungPositionDto
				.getFZusatzrabattsatz());
		rechnungPosition.setPositionIId(rechnungPositionDto.getPositioniId());
		rechnungPosition.setTypCNr(rechnungPositionDto.getTypCNr());
		rechnungPosition.setPositionIIdArtikelset(rechnungPositionDto
				.getPositioniIdArtikelset());
		rechnungPosition.setVerleihIId(rechnungPositionDto.getVerleihIId());
		rechnungPosition.setKostentraegerIId(rechnungPositionDto
				.getKostentraegerIId());
		rechnungPosition.setZwsVonPosition(rechnungPositionDto
				.getZwsVonPosition());
		rechnungPosition.setZwsBisPosition(rechnungPositionDto
				.getZwsBisPosition());
		rechnungPosition.setZwsNettoSumme(rechnungPositionDto
				.getZwsNettoSumme());
		if (rechnungPositionDto.getBZwsPositionspreisZeigen() != null) {
			rechnungPosition.setBZwsPositionspreisZeigen(rechnungPositionDto
					.getBZwsPositionspreisZeigen());
		} else {
			rechnungPosition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
		}
		rechnungPosition.setCLvposition(rechnungPositionDto.getCLvposition());
		rechnungPosition.setCBez(rechnungPositionDto.getCBez());
		rechnungPosition.setNMaterialzuschlag(rechnungPositionDto
				.getNMaterialzuschlag());
		rechnungPosition.setNMaterialzuschlagKurs(rechnungPositionDto
				.getNMaterialzuschlagKurs());
		rechnungPosition.setTMaterialzuschlagDatum(rechnungPositionDto
				.getTMaterialzuschlagDatum());
		em.merge(rechnungPosition);
		em.flush();
	}

	private RechnungPositionDto assembleRechnungPositionDto(
			Rechnungposition rechnungPosition) {

		RechnungPositionDto rechnungPositionDto = RechnungPositionDtoAssembler
				.createDto(rechnungPosition);

		Rechnung r = em.find(Rechnung.class,
				rechnungPositionDto.getRechnungIId());

		if (r.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
				|| r.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
			rechnungPositionDto
					.setSeriennrChargennrMitMenge(getLagerFac()
							.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
									LocaleFac.BELEGART_GUTSCHRIFT,
									rechnungPosition.getIId()));
		} else {
			rechnungPositionDto
					.setSeriennrChargennrMitMenge(getLagerFac()
							.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
									LocaleFac.BELEGART_RECHNUNG,
									rechnungPosition.getIId()));
		}

		return rechnungPositionDto;
	}

	private RechnungPositionDto[] assembleRechnungpositionDtos(
			Collection<?> rechnungPositions) {
		List<RechnungPositionDto> list = new ArrayList<RechnungPositionDto>();
		if (rechnungPositions != null) {
			Iterator<?> iterator = rechnungPositions.iterator();
			while (iterator.hasNext()) {
				Rechnungposition rechnungPosition = (Rechnungposition) iterator
						.next();
				list.add(assembleRechnungPositionDto(rechnungPosition));
			}
		}
		RechnungPositionDto[] returnArray = new RechnungPositionDto[list.size()];
		return (RechnungPositionDto[]) list.toArray(returnArray);
	}

	public RechnungDto[] rechnungFindByBelegdatumVonBis(String mandantCNr,
			Date dVon, Date dBis) {
		Query query = em
				.createNamedQuery("RechnungfindByMandantBelegdatumVonBis");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, dVon);
		query.setParameter(3, dBis);
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	public RechnungDto[] rechnungFindAll() {
		Query query = em.createNamedQuery("RechnungfindAll");
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	public RechnungDto[] rechnungFindByAuftragIId(Integer auftragIId)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungfindByAuftragIId");
		query.setParameter(1, auftragIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	public RechnungDto[] rechnungFindByLieferscheinIId(Integer lieferscheinIId) {
		Query query = em.createNamedQuery("RechnungfindByLieferscheinIId");
		query.setParameter(1, lieferscheinIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	public RechnungDto[] rechnungFindByAuftragIIdTBelegdatum(
			Integer auftragIId, java.sql.Date aktuellerTermin)
			throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("RechnungfindByAuftragIIdTBelegdatum");
		query.setParameter(1, auftragIId);
		query.setParameter(2, aktuellerTermin);
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	public RechnungDto[] rechnungFindByAuftragIIdStatusCNr(Integer auftragIId)
			throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("RechnungfindByAuftragIIdNotInStatusCNr");
		query.setParameter(1, auftragIId);
		query.setParameter(2, RechnungFac.STATUS_STORNIERT);
		Collection<?> cl = query.getResultList();
		return assembleRechnungDtos(cl);
	}

	protected Date getSpaetestesZahldatum(int rechnungIId) {
		List<RechnungzahlungDto> zahlungen = Arrays
				.asList(zahlungFindByRechnungIId(rechnungIId));
		if (zahlungen.size() == 0)
			return null;
		return Collections.max(zahlungen, new Comparator<RechnungzahlungDto>() {

			@Override
			public int compare(RechnungzahlungDto o1, RechnungzahlungDto o2) {
				return o1.getDZahldatum().compareTo(o2.getDZahldatum());
			}
		}).getDZahldatum();
	}

	/**
	 * Anlegen einer neuen Zahlung.
	 * 
	 * @param zahlungDto
	 *            ZahlungDto
	 * @param bErledigt
	 *            boolean
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ZahlungDto
	 */
	public RechnungzahlungDto createZahlung(RechnungzahlungDto zahlungDto,
			boolean bErledigt, TheClientDto theClientDto) throws EJBExceptionLP {

		RechnungDto rechnungDto = rechnungFindByPrimaryKey(zahlungDto
				.getRechnungIId());

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ZAHLUNG);
		zahlungDto.setIId(iId);
		zahlungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zahlungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		zahlungDto.setTAendern(now);
		zahlungDto.setTAnlegen(now);
		// Status der Rechnung updaten
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT))
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_BEREITS_BEZAHLT, "Rechnung "
							+ rechnungDto.getCNr() + "bereits bezahlt");
		if (bErledigt) {
			rechnungDto.setStatusCNr(RechnungFac.STATUS_BEZAHLT);
			Date tBezahlt = getSpaetestesZahldatum(rechnungDto.getIId());
			rechnungDto.setTBezahltdatum(tBezahlt == null ? zahlungDto
					.getDZahldatum() : tBezahlt);
		} else {
			rechnungDto.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
			rechnungDto.setTBezahltdatum(null);
		}
		// Update Rechnung ohne theClientDto da dadurch keine Pruefung ob
		// erlaubt
		// Zahlung auf Rechnung buchen ist immer erlaubt
		updateRechnung(rechnungDto);
		try {
			Rechnungzahlung zahlung = new Rechnungzahlung(zahlungDto.getIId(),
					zahlungDto.getRechnungIId(), zahlungDto.getDZahldatum(),
					zahlungDto.getZahlungsartCNr(), zahlungDto.getNKurs(),
					zahlungDto.getNBetrag(), zahlungDto.getNBetragfw(),
					zahlungDto.getNBetragUst(), zahlungDto.getNBetragUstfw(),
					zahlungDto.getPersonalIIdAnlegen(),
					zahlungDto.getPersonalIIdAendern());
			em.persist(zahlung);
			em.flush();
			zahlungDto.setTAendern(zahlung.getTAendern());
			zahlungDto.setTAnlegen(zahlung.getTAnlegen());
			setRechnungzahlungFromRechnungzahlungDto(zahlung, zahlungDto);
			// Zahlung verbuchen
			getBelegbuchungFac(theClientDto.getMandant()).verbucheZahlung(
					zahlungDto.getIId(), theClientDto);

			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {

				EingangsrechnungzahlungDto ez = new EingangsrechnungzahlungDto();

				ez.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG);
				ez.setEingangsrechnungIId(zahlungDto.getEingangsrechnungIId());
				ez.setTZahldatum(zahlungDto.getDZahldatum());

				String erWaehrung = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								zahlungDto.getEingangsrechnungIId())
						.getWaehrungCNr();

				ez.setBKursuebersteuert(Helper.boolean2Short(false));
				ez.setNBetrag(zahlungDto.getNBetrag().add(
						zahlungDto.getNBetragUst()));

				BigDecimal betragFw = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								zahlungDto.getNBetragfw().add(
										zahlungDto.getNBetragUstfw()),
								rechnungDto.getWaehrungCNr(), erWaehrung,
								zahlungDto.getDZahldatum(), theClientDto);

				ez.setNBetragfw(Helper.rundeGeldbetrag(betragFw));

				BigDecimal betragUstFw = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(

						zahlungDto.getNBetragUstfw(),
								rechnungDto.getWaehrungCNr(), erWaehrung,
								zahlungDto.getDZahldatum(), theClientDto);
				ez.setNBetragustfw(Helper.rundeGeldbetrag(betragUstFw));

				// lt. WH muss hier der Kurs -Belegwaehrung zu Mandantenwaehrung
				// eingetragen werden-
				WechselkursDto wechselkursDto = getLocaleFac().getKursZuDatum(
						erWaehrung, theClientDto.getSMandantenwaehrung(),
						zahlungDto.getDZahldatum(), theClientDto);

				if (wechselkursDto != null && wechselkursDto.getNKurs() != null) {
					ez.setNKurs(wechselkursDto.getNKurs());
				} else {
					ez.setNKurs(zahlungDto.getNKurs());
				}

				ez.setNBetrag(zahlungDto.getNBetrag().add(
						zahlungDto.getNBetragUst()));

				ez.setNBetragust(zahlungDto.getNBetragUst());

				ez.setRechnungzahlungIId(zahlungDto.getIId());
				ez = getEingangsrechnungFac().createEingangsrechnungzahlung(ez,
						null, theClientDto);
			}
			alleBelegbuchungenAuziffern(theClientDto, rechnungDto, zahlung);

			return zahlungDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void alleBelegbuchungenAuziffern(TheClientDto theClientDto,
			RechnungDto rechnungDto, Rechnungzahlung zahlung)
			throws RemoteException {
		List<BelegbuchungDto> belegbuchungDtos = getBelegbuchungFac(
				theClientDto.getMandant())
				.getAlleBelegbuchungenInklZahlungenAR(rechnungDto.getIId());
		if (rechnungDto.getRechnungartCNr().equals(
				RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
			belegbuchungDtos.addAll(getBelegbuchungFac(
					theClientDto.getMandant())
					.getAlleBelegbuchungenInklZahlungenAR(
							zahlung.getRechnungIIdGutschrift()));
		}
		Integer kontoIId = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto).getIidDebitorenkonto();

		getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungenAusziffernWennNoetig(kontoIId, belegbuchungDtos);
	}

	/**
	 * Eine Zahlung loeschen.
	 * 
	 * @param zahlungDto
	 *            ZahlungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeZahlung(RechnungzahlungDto zahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = zahlungDto.getIId();
		try {

			// erzahlung vorher loeschen
			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				EingangsrechnungzahlungDto erz = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByRechnungzahlungIId(
								zahlungDto.getIId());
				getEingangsrechnungFac().removeEingangsrechnungzahlung(erz,
						theClientDto);
			}

			// vorher buchungen loeschen
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlungRueckgaengig(zahlungDto, theClientDto);
			Rechnungzahlung toRemove = em.find(Rechnungzahlung.class, iId);
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
			// er updaten
			RechnungzahlungDto[] zahlungen = zahlungFindByRechnungIId(zahlungDto
					.getRechnungIId());
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(zahlungDto
					.getRechnungIId());
			if (zahlungen == null || zahlungen.length == 0) {
				rechnungDto.setStatusCNr(RechnungFac.STATUS_OFFEN);
				rechnungDto.setTBezahltdatum(null);
			} else {
				rechnungDto.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
				rechnungDto.setTBezahltdatum(null);
			}
			updateRechnung(rechnungDto);

		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zahlungDto == null)
			return;

		Integer iId = zahlungDto.getIId();
		zahlungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zahlungDto.setTAendern(getTimestamp());
		try {

			//
			Rechnungzahlung zahlung = em.find(Rechnungzahlung.class, iId);

			// er-zahlung vorher loeschen
			if (zahlung.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
					&& !zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				EingangsrechnungzahlungDto erz = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByRechnungzahlungIId(
								zahlungDto.getIId());
				getEingangsrechnungFac().removeEingangsrechnungzahlung(erz,
						theClientDto);
			}

			// er-zahlung anlegen
			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
					&& !zahlung.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				if (zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {

					EingangsrechnungzahlungDto ez = new EingangsrechnungzahlungDto();

					ez.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG);
					ez.setEingangsrechnungIId(zahlungDto
							.getEingangsrechnungIId());
					ez.setTZahldatum(zahlungDto.getDZahldatum());
					ez.setNKurs(zahlungDto.getNKurs());
					ez.setBKursuebersteuert(Helper.boolean2Short(false));
					ez.setNBetrag(zahlungDto.getNBetrag());
					ez.setNBetragfw(zahlungDto.getNBetragfw());
					ez.setNBetragust(zahlungDto.getNBetragUst());
					ez.setNBetragustfw(zahlungDto.getNBetragUstfw());
					ez.setRechnungzahlungIId(zahlungDto.getIId());
					getEingangsrechnungFac().createEingangsrechnungzahlung(ez,
							false, theClientDto);
				}
			}

			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
					&& zahlung.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {

				EingangsrechnungzahlungDto ez = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByRechnungzahlungIId(
								zahlungDto.getIId());
				ez.setEingangsrechnungIId(zahlungDto.getEingangsrechnungIId());
				ez.setTZahldatum(zahlungDto.getDZahldatum());

				RechnungDto rechnungDto = rechnungFindByPrimaryKey(zahlungDto
						.getRechnungIId());

				String erWaehrung = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								zahlungDto.getEingangsrechnungIId())
						.getWaehrungCNr();

				BigDecimal betragFw = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								zahlungDto.getNBetragfw().add(
										zahlungDto.getNBetragUstfw()),
								rechnungDto.getWaehrungCNr(), erWaehrung,
								zahlungDto.getDZahldatum(), theClientDto);

				ez.setNBetragfw(Helper.rundeGeldbetrag(betragFw));

				BigDecimal betragUstFw = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(

								zahlungDto.getNBetragUstfw(),
								rechnungDto.getWaehrungCNr(),
								getEingangsrechnungFac()
										.eingangsrechnungFindByPrimaryKey(
												zahlungDto
														.getEingangsrechnungIId())
										.getWaehrungCNr(),
								zahlungDto.getDZahldatum(), theClientDto);
				ez.setNBetragustfw(Helper.rundeGeldbetrag(betragUstFw));

				// lt. WH muss hier der Kurs -Belegwaehrung zu
				// Mandantenwaehrung
				// eingetragen werden-
				WechselkursDto wechselkursDto = getLocaleFac().getKursZuDatum(
						erWaehrung, theClientDto.getSMandantenwaehrung(),
						zahlungDto.getDZahldatum(), theClientDto);

				if (wechselkursDto != null && wechselkursDto.getNKurs() != null) {
					ez.setNKurs(wechselkursDto.getNKurs());
				} else {
					ez.setNKurs(zahlungDto.getNKurs());
				}

				ez.setNBetrag(zahlungDto.getNBetrag());
				ez.setNBetragust(zahlungDto.getNBetragUst());
				getEingangsrechnungFac().updateEingangsrechnungzahlung(ez,
						null, theClientDto);

			}

			// er updaten
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(zahlungDto
					.getRechnungIId());
			if (bErledigt) {
				rechnungDto.setStatusCNr(RechnungFac.STATUS_BEZAHLT);
				rechnungDto.setTBezahltdatum(getSpaetestesZahldatum(rechnungDto
						.getIId()));
			} else {
				rechnungDto.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
				rechnungDto.setTBezahltdatum(null);
			}
			updateRechnung(rechnungDto);

			setRechnungzahlungFromRechnungzahlungDto(zahlung, zahlungDto);
			// Zahlung verbuchen, vorher buchungen loeschen
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlungRueckgaengig(zahlungDto, theClientDto);
			getBelegbuchungFac(theClientDto.getMandant()).verbucheZahlung(
					zahlungDto.getIId(), theClientDto);
			alleBelegbuchungenAuziffern(theClientDto, rechnungDto, zahlung);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungzahlungDto zahlungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Rechnungzahlung rechnungzahlung = em.find(Rechnungzahlung.class,
					iId);
			return assembleZahlungDto(rechnungzahlung);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungzahlungDto[] zahlungFindByRechnungIId(Integer rechnungIId) {
		Query query = em.createNamedQuery("RechnungzahlungfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		Collection<?> cl = query.getResultList();
		return assembleZahlungDtos(cl);
	}

	private RechnungzahlungDto assembleZahlungDto(Rechnungzahlung zahlung) {
		return RechnungzahlungDtoAssembler.createDto(zahlung);
	}

	private RechnungzahlungDto[] assembleZahlungDtos(Collection<?> zahlungs) {
		List<RechnungzahlungDto> list = new ArrayList<RechnungzahlungDto>();
		if (zahlungs != null) {
			Iterator<?> iterator = zahlungs.iterator();
			while (iterator.hasNext()) {
				Rechnungzahlung zahlung = (Rechnungzahlung) iterator.next();
				list.add(assembleZahlungDto(zahlung));
			}
		}
		RechnungzahlungDto[] returnArray = new RechnungzahlungDto[list.size()];
		return (RechnungzahlungDto[]) list.toArray(returnArray);
	}

	public Integer createRechnungAusRechnung(Integer rechnungIId,
			java.sql.Date neuDatum, boolean bUebernimmKonditionenDesKunden,
			TheClientDto theClientDto) {

		RechnungDto rechnungDtoVorhanden = rechnungFindByPrimaryKey(rechnungIId);
		rechnungDtoVorhanden.setIId(null);
		rechnungDtoVorhanden.setCNr(null);
		rechnungDtoVorhanden.setLieferscheinIId(null);

		// SP791
		if (!rechnungDtoVorhanden.getRechnungartCNr().equals(
				RechnungFac.RECHNUNGART_ANZAHLUNG)) {
			rechnungDtoVorhanden.setAuftragIId(null);
		}

		rechnungDtoVorhanden.setNWert(null);
		rechnungDtoVorhanden.setNWertfw(null);
		rechnungDtoVorhanden.setNWertust(null);
		rechnungDtoVorhanden.setNWertustfw(null);

		try {
			BigDecimal bdKurs = getLocaleFac().getWechselkurs2(
					theClientDto.getSMandantenwaehrung(),
					rechnungDtoVorhanden.getWaehrungCNr(), theClientDto);
			rechnungDtoVorhanden.setNKurs(bdKurs);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		rechnungDtoVorhanden.setTFibuuebernahme(null);
		rechnungDtoVorhanden.setPersonalIIdStorniert(null);
		rechnungDtoVorhanden.setTBezahltdatum(null);
		rechnungDtoVorhanden.setTGedruckt(null);
		rechnungDtoVorhanden.setRechnungIIdZurechnung(null);
		rechnungDtoVorhanden.setLieferscheinIId(null);
		rechnungDtoVorhanden.setTMahnsperrebis(null);

		if (neuDatum != null) {
			rechnungDtoVorhanden.setTBelegdatum(new java.sql.Timestamp(neuDatum
					.getTime()));
		} else {
			rechnungDtoVorhanden.setTBelegdatum(new java.sql.Timestamp(System
					.currentTimeMillis()));
		}

		if (bUebernimmKonditionenDesKunden) {

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDtoVorhanden.getKundeIId(), theClientDto);
			Double dAllgemeinerrabattsatz = new Double(0);
			if (kundeDto.getFRabattsatz() != null) {
				dAllgemeinerrabattsatz = kundeDto.getFRabattsatz();
			}
			rechnungDtoVorhanden
					.setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz);

			if (kundeDto.getLieferartIId() != null) {
				rechnungDtoVorhanden
						.setLieferartIId(kundeDto.getLieferartIId());
			}
			if (kundeDto.getZahlungszielIId() != null) {
				rechnungDtoVorhanden.setZahlungszielIId(kundeDto
						.getZahlungszielIId());
			}
			if (kundeDto.getSpediteurIId() != null) {
				rechnungDtoVorhanden
						.setSpediteurIId(kundeDto.getSpediteurIId());
			}

		}

		rechnungDtoVorhanden.setTStorniert(null);
		rechnungDtoVorhanden.setPersonalIIdStorniert(null);

		rechnungDtoVorhanden.setTManuellerledigt(null);
		rechnungDtoVorhanden.setPersonalIIdManuellerledigt(null);

		rechnungDtoVorhanden.setIGeschaeftsjahr(getBuchenFac()
				.findGeschaeftsjahrFuerDatum(
						new java.sql.Date(System.currentTimeMillis()),
						theClientDto.getMandant()));

		Integer rechnungIIdNeu = createRechnung(rechnungDtoVorhanden,
				theClientDto).getIId();

		RechnungPositionDto[] posDtos = rechnungPositionFindByRechnungIId(rechnungIId);
		for (int i = 0; i < posDtos.length; i++) {
			RechnungPositionDto posDto = posDtos[i];
			// Lieferschein auslassen
			if (!posDto.getPositionsartCNr().equals(
					RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
				if (posDto.getAuftragpositionIId() == null) {
					try {
						posDto.setIId(null);
						posDto.setRechnungIId(rechnungIIdNeu);
						posDto.setPositioniIdArtikelset(null);
						posDto.setRechnungpositionIId(null);
						posDto.setAuftragpositionIId(null);
						posDto.setLieferscheinIId(null);
						posDto.setRechnungIIdGutschrift(null);
						posDto.setBDrucken(Helper.boolean2Short(true));
						posDto.setNMaterialzuschlagKurs(null);
						posDto.setTMaterialzuschlagDatum(null);

						if (posDto.getPositionsartCNr().equals(
								RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {

							if (posDto.getNMenge() != null
									&& posDto.getNEinzelpreis() != null) {

								// SP2157 Fremdwaehrungsrechnung muss denselben
								// Preis wie damals behalten
								VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac()
										.berechnePreisfelder(
												posDto.getNEinzelpreis(),
												posDto.getFRabattsatz(),
												posDto.getFZusatzrabattsatz(),
												posDto.getMwstsatzIId(), 4, // @
												// todo
												// Konstante
												// PJ
												// 3778
												theClientDto);

								posDto.setNEinzelpreis(verkaufspreisDto.einzelpreis);
								posDto.setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
								posDto.setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

							}

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											posDto.getArtikelIId(),
											theClientDto);

							if (Helper.short2boolean(artikelDto
									.getBLagerbewirtschaftet())) {
								BigDecimal lagerstand = getLagerFac()
										.getLagerstand(
												posDto.getArtikelIId(),
												rechnungDtoVorhanden
														.getLagerIId(),
												theClientDto);
								if (lagerstand.doubleValue() < posDto
										.getNMenge().doubleValue()) {
									posDto.setNMenge(lagerstand);
								}
							}

							if (Helper.short2boolean(artikelDto
									.getBSeriennrtragend())
									|| Helper.short2boolean(artikelDto
											.getBChargennrtragend())) {
								posDto.setNMenge(new BigDecimal(0));
							}

						}

						if (posDto.isIntelligenteZwischensumme()) {
							ZwsPositionMapper mapper = new ZwsPositionMapper(
									this, this);
							mapper.map(posDto, posDtos[i], rechnungIIdNeu);
						}
						createRechnungPosition(posDto,
								rechnungDtoVorhanden.getLagerIId(),
								theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}
		}

		return rechnungIIdNeu;
	}

	public Integer createRechnungAusAngebot(Integer angebotIId,
			java.sql.Date neuDatum, TheClientDto theClientDto) {

		// zuerst die neue Rechnung anlegen
		RechnungDto rechnungDto = new RechnungDto();
		// Auftrag und Kunde holen
		AngebotDto angebotDto = null;
		try {
			angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotIId,
					theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			// ------------------------------------------------------------------
			// ----
			// Ansprechpartner: vorbesetzen entscheidet ein Mandantenparameter
			// ------------------------------------------------------------------
			// ----
			ParametermandantDto parameterAnspVorbesetzen = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
							ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
			if ((Boolean) parameterAnspVorbesetzen.getCWertAsObject()) {
				// vorzugsweise aus dem Auftrag
				if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
					rechnungDto.setAnsprechpartnerIId(angebotDto
							.getAnsprechpartnerIIdKunde());
				}
				// sonst den ersten des Kunden
				else {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeDto.getPartnerIId(), theClientDto);
					if (anspDto != null) {
						rechnungDto.setAnsprechpartnerIId(anspDto.getIId());
					}
				}
			} else {
				// nicht vorbesetzen
				rechnungDto.setAnsprechpartnerIId(null);
			}
			// ------------------------------------------------------------------
			// ----
			// weitere Eigenschaften uebernehmen
			// ------------------------------------------------------------------
			// ----

			rechnungDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			rechnungDto.setBMwstallepositionen(Helper.boolean2Short(false));

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);

			boolean bReversecharge = (Boolean) parameter.getCWertAsObject();
			if (bReversecharge) {
				rechnungDto.setBReversecharge(kundeDto.getBReversecharge());
			} else {
				rechnungDto.setBReversecharge(Helper.boolean2Short(false));
			}

			rechnungDto.setCBez(angebotDto.getCBez());
			rechnungDto.setCFusstextuebersteuert(null);
			rechnungDto.setCKopftextuebersteuert(null);
			rechnungDto.setCNr(null);
			rechnungDto.setCProvisiontext(null);
			rechnungDto.setCProvisiontext(null);
			// ------------------------------------------------------------------
			// ----
			// Allgemeiner Rabatt und Projektrabatt werden zusammengefasst.
			// ------------------------------------------------------------------
			// ----
			double dFaktor = 100.0;
			if (angebotDto.getFAllgemeinerRabattsatz() != null) {
				dFaktor = dFaktor - angebotDto.getFAllgemeinerRabattsatz();
			}
			if (angebotDto.getFProjektierungsrabattsatz() != null) {
				double dFaktor2 = 100.0 - angebotDto
						.getFProjektierungsrabattsatz();
				dFaktor = dFaktor * dFaktor2 / 100.0;
			}
			rechnungDto.setFAllgemeinerRabattsatz(new Double(100.0 - dFaktor));

			rechnungDto.setFVersteckterAufschlag(angebotDto
					.getFVersteckterAufschlag().doubleValue());
			rechnungDto.setIGeschaeftsjahr(null); // das macht der BN-Generator
			rechnungDto.setIId(null); // das macht der BN-Generator
			rechnungDto.setKostenstelleIId(angebotDto.getKostenstelleIId());
			// ------------------------------------------------------------------
			// ----
			// Kunde/Statistikadresse lt. Beschreibung von WH:
			//
			// Bei Wiederhol ABs wird die Ueberleitung in die Statistikadresse
			// der Rechnung so gesteuert, dass
			// der Kunde der AB -> die Statistikadresse wird
			// die Rechnungsadresse wird der Kunde der Rechnung.
			// ------------------------------------------------------------------
			// ----
			rechnungDto.setKundeIId(angebotDto.getKundeIIdAngebotsadresse());
			rechnungDto.setKundeIIdStatistikadresse(angebotDto
					.getKundeIIdAngebotsadresse());
			// Hauptlager des Mandanten vorbesetzen
			rechnungDto.setLagerIId(kundeDto.getLagerIIdAbbuchungslager());
			rechnungDto.setLieferartIId(angebotDto.getLieferartIId());

			// gemahnt sein
			rechnungDto.setMandantCNr(angebotDto.getMandantCNr());
			// ------------------------------------------------------------------
			// ----
			// Mwst-Satz (falls ueber alle Positionen gleich)
			// ------------------------------------------------------------------
			// ----
			MwstsatzbezDto mwstSatzbezDto = null;
			// zuerst der aus dem Kunden
			if (kundeDto.getMwstsatzbezIId() != null) {
				mwstSatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(
						kundeDto.getMwstsatzbezIId(), theClientDto);
			}
			// falls dort nicht definiert, gibt es beim Mandanten einen
			// Default-Wert
			else {
				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);
				if (mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz() != null) {
					mwstSatzbezDto = getMandantFac()
							.mwstsatzbezFindByPrimaryKey(
									mandantDto
											.getMwstsatzbezIIdStandardinlandmwstsatz(),
									theClientDto);
				}
			}
			// Wenn eine gueltige Mwst-satz-bezeichnung gefunden wurde, hol ich
			// den aktuellen Steuersatz dazu.
			if (mwstSatzbezDto != null) {
				MwstsatzDto mwstSatzDto = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								mwstSatzbezDto.getIId(), theClientDto);
				rechnungDto.setMwstsatzIId(mwstSatzDto.getIId());
			}
			// ------------------------------------------------------------------
			// ----
			// Es wird der aktuelle Wechselkurs eingetragen (nicht aus dem
			// Auftrag)
			// der koennte sich ja in der Zwischenzeit geaendert haben.
			// ------------------------------------------------------------------
			// ----
			BigDecimal bdKurs = getLocaleFac().getWechselkurs2(
					theClientDto.getSMandantenwaehrung(),
					angebotDto.getWaehrungCNr(), theClientDto);
			rechnungDto.setNKurs(bdKurs);
			rechnungDto.setNProvision(null);
			// hat noch keine Werte (werden beim Aktivieren gesetzt)
			rechnungDto.setNWert(null);
			rechnungDto.setNWertfw(null);
			rechnungDto.setNWertust(null);
			rechnungDto.setNWertustfw(null);
			// die personal-iid's werden auch woanders gesetzt
			rechnungDto.setPersonalIIdAendern(null);
			rechnungDto.setPersonalIIdAnlegen(null);
			rechnungDto.setPersonalIIdManuellerledigt(null);
			rechnungDto.setPersonalIIdStorniert(null);
			// ------------------------------------------------------------------
			// ----
			// Vertreter kommt aus dem Auftrag, wenn dort definiert
			// Sonst bestimmt ein Mandantenparameter, was hier passiert
			// ------------------------------------------------------------------
			// ----
			if (angebotDto.getPersonalIIdVertreter() != null) {
				// Vertreter aus Auftrag
				rechnungDto.setPersonalIIdVertreter(angebotDto
						.getPersonalIIdVertreter());
			} else {
				// Vertreter ist im Auftrag definiert
				ParametermandantDto parameterVertreterAusKunde = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
								ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
				// kommt er aus dem Kunden?
				if ((Boolean) parameterVertreterAusKunde.getCWertAsObject()) {
					if (kundeDto.getPersonaliIdProvisionsempfaenger() != null) {
						rechnungDto.setPersonalIIdVertreter(kundeDto
								.getPersonaliIdProvisionsempfaenger());
					}
					// wenns beim Kunden keinen gibt, ists der Benutzer selbst
					else {
						rechnungDto.setPersonalIIdVertreter(theClientDto
								.getIDPersonal());
					}
				}
				// nicht aus dem Kunden -> ist der Benutzer
				else {
					rechnungDto.setPersonalIIdVertreter(theClientDto
							.getIDPersonal());
				}
			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		rechnungDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);
		rechnungDto.setRechnungIIdZurechnung(null); // dieses Feld ist nur
		// fuer Gutschriften
		rechnungDto.setSpediteurIId(angebotDto.getSpediteurIId());
		rechnungDto.setStatusCNr(null); // macht das create
		rechnungDto.setTAendern(null); // macht das create
		rechnungDto.setTAnlegen(null); // macht das create
		if (neuDatum != null) {
			rechnungDto.setTBelegdatum(new java.sql.Timestamp(neuDatum
					.getTime()));
		} else {
			rechnungDto.setTBelegdatum(new java.sql.Timestamp(System
					.currentTimeMillis()));
		}
		rechnungDto.setTBezahltdatum(null); // kann noch nicht bezahlt sein
		rechnungDto.setTFibuuebernahme(null); // beim Export
		rechnungDto.setTGedruckt(null); // erst beim Aktivieren/Ausdrucken

		// sein
		rechnungDto.setTMahnsperrebis(null); // default gibts keine
		// Mahnsperre
		rechnungDto.setTManuellerledigt(null); // erst bei manuell erledigen
		rechnungDto.setTStorniert(null); // erst beim Storniern
		rechnungDto.setWaehrungCNr(angebotDto.getWaehrungCNr());
		rechnungDto.setZahlungszielIId(angebotDto.getZahlungszielIId());
		// speichern und dto updaten

		Integer rechnungIIdNeu = createRechnung(rechnungDto, theClientDto)
				.getIId();
		try {
			AngebotpositionDto[] agposDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(angebotIId,
							theClientDto);
			for (int i = 0; i < agposDtos.length; i++) {
				AngebotpositionDto agposDto = agposDtos[i];
				// Lieferschein auslassen
				if (!agposDto.getPositionsartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {

					RechnungPositionDto posDto = new RechnungPositionDto();

					posDto.setRechnungIId(rechnungIIdNeu);
					posDto.setPositionsartCNr(agposDto.getPositionsartCNr());

					posDto = (RechnungPositionDto) getBelegpositionkonvertierungFac()
							.cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(
									posDto, (BelegpositionVerkaufDto) agposDto,
									theClientDto);

					posDto.setNMaterialzuschlagKurs(agposDto
							.getNMaterialzuschlagKurs());
					posDto.setTMaterialzuschlagDatum(agposDto
							.getTMaterialzuschlagDatum());

					posDto.setBDrucken(Helper.boolean2Short(true));

					if (agposDto.getPositionsartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										posDto.getArtikelIId(), theClientDto);

						if (Helper.short2boolean(artikelDto
								.getBLagerbewirtschaftet())) {
							BigDecimal lagerstand = getLagerFac()
									.getLagerstand(posDto.getArtikelIId(),
											rechnungDto.getLagerIId(),
											theClientDto);
							if (lagerstand.doubleValue() < posDto.getNMenge()
									.doubleValue()) {
								posDto.setNMenge(lagerstand);
							}
						}

						if (Helper.short2boolean(artikelDto
								.getBSeriennrtragend())
								|| Helper.short2boolean(artikelDto
										.getBChargennrtragend())) {
							posDto.setNMenge(new BigDecimal(0));
						}

					}

					if (posDto.isIntelligenteZwischensumme()) {
						ZwsPositionMapper mapper = new ZwsPositionMapper(
								getAngebotpositionFac(), this);
						mapper.map(posDto, agposDtos[i], rechnungIIdNeu);
					}

					createRechnungPosition(posDto, rechnungDto.getLagerIId(),
							theClientDto);

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return rechnungIIdNeu;
	}

	public Integer createRechnungAusLieferschein(Integer lieferscheinIId,
			RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinIId, theClientDto);
			if (!lieferscheinDto.getStatusCNr().equals(
					LieferscheinFac.LSSTATUS_GELIEFERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN,
						"LS muss geliefert sein", lieferscheinDto.getCNr());
			}
			// Kunde holen
			KundeDto kundeDto = getKundeFac()
					.kundeFindByPrimaryKey(
							lieferscheinDto.getKundeIIdRechnungsadresse(),
							theClientDto);

			// PJ 14718
			if (Helper.short2boolean(kundeDto.getBVersteckterlieferant())) {
				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				getKundeFac().updateKunde(kundeDto, theClientDto);
			}

			// aktuellen MWST-Satz holen
			MwstsatzDto mwstSatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kundeDto.getMwstsatzbezIId(), theClientDto);

			// Wenn zum Belegdatum des Lieferscheins ein anderer Mwstsatz galt,
			// dann Fehler
			MwstsatzDto mwstsatzDtoZumLieferscheindatum = getMandantFac()
					.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(),
							lieferscheinDto.getTBelegdatum());
			if (mwstSatzDto != null && mwstsatzDtoZumLieferscheindatum != null) {
				if (!mwstSatzDto.getIId().equals(
						mwstsatzDtoZumLieferscheindatum.getIId())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE,
							new Exception("FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE"));
				}
			}

			// zuerst die neue Rechnung anlegen
			if (rechnungDto == null) {
				rechnungDto = new RechnungDto();
			}
			if (rechnungDto.getTBelegdatum() == null) {
				rechnungDto.setTBelegdatum(new java.sql.Timestamp(System
						.currentTimeMillis()));
			}

			if (neuDatum != null) {
				rechnungDto.setTBelegdatum(new java.sql.Timestamp(neuDatum
						.getTime()));
			}

			// ------------------------------------------------------------------
			// ----
			// Ansprechpartner: vorbesetzen entscheidet ein Mandantenparameter
			// ------------------------------------------------------------------
			// ----
			ParametermandantDto parameterAnspVorbesetzen = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
							ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
			if ((Boolean) parameterAnspVorbesetzen.getCWertAsObject()) {
				// vorzugsweise aus dem Lieferschein
				if (lieferscheinDto.getAnsprechpartnerIId() != null) {
					rechnungDto.setAnsprechpartnerIId(lieferscheinDto
							.getAnsprechpartnerIId());
				}
				// sonst den ersten des Kunden
				else {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeDto.getPartnerIId(), theClientDto);
					if (anspDto != null) {
						rechnungDto.setAnsprechpartnerIId(anspDto.getIId());
					}
				}
			} else {
				// nicht vorbesetzen
				rechnungDto.setAnsprechpartnerIId(null);
			}
			// ------------------------------------------------------------------
			// ----
			// und weiter
			// ------------------------------------------------------------------
			// ----

			// PJ18728
			// SP3184
			if (!rechnungstypCNr
					.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				rechnungDto.setAuftragIId(lieferscheinDto.getAuftragIId());
				rechnungDto.setLieferscheinIId(lieferscheinDto.getIId());
			}
			rechnungDto.setBMindermengenzuschlag(lieferscheinDto
					.getBMindermengenzuschlag());
			rechnungDto.setBMwstallepositionen(Helper.boolean2Short(false));

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);

			boolean bReversecharge = (Boolean) parameter.getCWertAsObject();
			if (bReversecharge) {
				rechnungDto.setBReversecharge(kundeDto.getBReversecharge());
			} else {
				rechnungDto.setBReversecharge(Helper.boolean2Short(false));
			}

			rechnungDto.setCFusstextuebersteuert(null);
			rechnungDto.setCKopftextuebersteuert(null);
			rechnungDto.setCProvisiontext(null);
			// lt. WH IMS 1857 den allgemeinen Rabatt nicht uebernehmen - wirkt
			// sonst doppelt
			// rechnungDto.setFAllgemeinerrabattsatz(new
			// Float(lieferscheinDto.getFAllgemeinerRabatt().floatValue()));
			rechnungDto.setFAllgemeinerRabattsatz(new Double(0));
			if (mwstSatzDto != null) {
				rechnungDto.setMwstsatzIId(mwstSatzDto.getIId());
			}
			rechnungDto.setFVersteckterAufschlag(new Double(lieferscheinDto
					.getFVersteckterAufschlag().doubleValue()));
			rechnungDto
					.setKostenstelleIId(lieferscheinDto.getKostenstelleIId());
			rechnungDto.setKundeIId(lieferscheinDto
					.getKundeIIdRechnungsadresse());
			rechnungDto.setKundeIIdStatistikadresse(lieferscheinDto
					.getKundeIIdLieferadresse());
			rechnungDto.setCBez(lieferscheinDto.getCBezProjektbezeichnung());
			rechnungDto.setProjektIId(lieferscheinDto.getProjektIId());
			rechnungDto.setLagerIId(kundeDto.getLagerIIdAbbuchungslager());
			rechnungDto.setLieferartIId(lieferscheinDto.getLieferartIId());

			rechnungDto.setMandantCNr(lieferscheinDto.getMandantCNr());
			rechnungDto.setNKurs(new BigDecimal(lieferscheinDto
					.getFWechselkursmandantwaehrungzubelegwaehrung()
					.doubleValue()).setScale(
					LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
					BigDecimal.ROUND_HALF_EVEN));

			if (rechnungstypCNr
					.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				rechnungDto
						.setRechnungartCNr(RechnungFac.RECHNUNGART_PROFORMARECHNUNG);
			} else {
				rechnungDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);
			}

			rechnungDto.setWaehrungCNr(lieferscheinDto.getWaehrungCNr());
			rechnungDto
					.setZahlungszielIId(lieferscheinDto.getZahlungszielIId());
			rechnungDto.setSpediteurIId(lieferscheinDto.getSpediteurIId());
			rechnungDto.setCBestellnummer(lieferscheinDto.getCBestellnummer());
			rechnungDto.setPersonalIIdVertreter(lieferscheinDto
					.getPersonalIIdVertreter());
			// speichern und dto updaten
			rechnungDto = createRechnung(rechnungDto, theClientDto);
			// jetzt die Position anlegen
			RechnungPositionDto rechnungPositionDto = new RechnungPositionDto();
			rechnungPositionDto.setArtikelIId(null);
			rechnungPositionDto.setAuftragpositionIId(null);
			rechnungPositionDto.setBDrucken(Helper.boolean2Short(true));
			rechnungPositionDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));
			rechnungPositionDto.setBMwstsatzuebersteuert(Helper
					.boolean2Short(false));
			rechnungPositionDto.setCBez(null);
			rechnungPositionDto.setEinheitCNr(null);
			rechnungPositionDto.setBNettopreisuebersteuert(Helper
					.boolean2Short(false));
			if (mwstSatzDto != null) {
				rechnungPositionDto.setMwstsatzIId(mwstSatzDto.getIId());
			}
			rechnungPositionDto.setFRabattsatz(new Double(0));

			rechnungPositionDto.setLieferscheinIId(lieferscheinDto.getIId());
			rechnungPositionDto.setNMenge(null);
			rechnungPositionDto.setRechnungIId(rechnungDto.getIId());
			rechnungPositionDto.setRechnungIIdGutschrift(null);
			rechnungPositionDto.setXTextinhalt(null);
			rechnungPositionDto
					.setRechnungpositionartCNr(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN);
			rechnungPositionDto.setCZusatzbez(null);
			// speichern der position
			// hier brauch ich kein lager, da nix gebucht wird
			createRechnungPosition(rechnungPositionDto, null, theClientDto);
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
		return rechnungDto.getIId();
	}

	public Integer createRechnungAusMehrereLieferscheine(Object[] keys,
			RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {

			LieferscheinDto ls = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							(Integer) keys[keys.length - 1], theClientDto);
			// Kunde holen
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					ls.getKundeIIdRechnungsadresse(), theClientDto);
			// PJ 14718
			if (Helper.short2boolean(kundeDto.getBVersteckterlieferant())) {
				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				getKundeFac().updateKunde(kundeDto, theClientDto);
			}
			// aktuellen MWST-Satz holen
			MwstsatzDto mwstSatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kundeDto.getMwstsatzbezIId(), theClientDto);

			// Wenn zum Belegdatum des Lieferscheins ein anderer Mwstsatz galt,
			// dann Fehler
			MwstsatzDto mwstsatzDtoZumLieferscheindatum = getMandantFac()
					.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(),
							ls.getTBelegdatum());
			if (mwstSatzDto != null && mwstsatzDtoZumLieferscheindatum != null) {
				if (!mwstSatzDto.getIId().equals(
						mwstsatzDtoZumLieferscheindatum.getIId())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE,
							new Exception("FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE"));
				}
			}

			// zuerst die neue Rechnung anlegen
			if (rechnungDto == null) {
				rechnungDto = new RechnungDto();
			}
			if (rechnungDto.getTBelegdatum() == null) {
				rechnungDto.setTBelegdatum(new java.sql.Timestamp(System
						.currentTimeMillis()));
			}

			if (neuDatum != null) {
				rechnungDto.setTBelegdatum(new java.sql.Timestamp(neuDatum
						.getTime()));
			}

			// ------------------------------------------------------------------
			// ----
			// Ansprechpartner: vorbesetzen entscheidet ein Mandantenparameter
			// ------------------------------------------------------------------
			// ----
			ParametermandantDto parameterAnspVorbesetzen = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
							ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
			if ((Boolean) parameterAnspVorbesetzen.getCWertAsObject()) {
				// vorzugsweise aus dem Lieferschein
				if (ls.getAnsprechpartnerIId() != null) {
					rechnungDto.setAnsprechpartnerIId(ls
							.getAnsprechpartnerIId());
				}
				// sonst den ersten des Kunden
				else {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeDto.getPartnerIId(), theClientDto);
					if (anspDto != null) {
						rechnungDto.setAnsprechpartnerIId(anspDto.getIId());
					}
				}
			} else {
				// nicht vorbesetzen
				rechnungDto.setAnsprechpartnerIId(null);
			}
			// ------------------------------------------------------------------
			// ----
			// und weiter
			// ------------------------------------------------------------------
			// ----
			rechnungDto.setAuftragIId(null); // keine bindung
			rechnungDto.setBMindermengenzuschlag(ls.getBMindermengenzuschlag());
			rechnungDto.setBMwstallepositionen(Helper.boolean2Short(false));

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);

			boolean bReversecharge = (Boolean) parameter.getCWertAsObject();
			if (bReversecharge) {
				rechnungDto.setBReversecharge(kundeDto.getBReversecharge());
			} else {
				rechnungDto.setBReversecharge(Helper.boolean2Short(false));
			}

			rechnungDto.setCFusstextuebersteuert(null);
			rechnungDto.setCKopftextuebersteuert(null);
			rechnungDto.setCProvisiontext(null);
			// lt. WH IMS 1857 den allgemeinen Rabatt nicht uebernehmen - wirkt
			// sonst doppelt
			// rechnungDto.setFAllgemeinerrabattsatz(new
			// Float(lieferscheinDto.getFAllgemeinerRabatt().floatValue()));
			rechnungDto.setFAllgemeinerRabattsatz(new Double(0));
			if (mwstSatzDto != null) {
				rechnungDto.setMwstsatzIId(mwstSatzDto.getIId());
			}
			rechnungDto.setFVersteckterAufschlag(new Double(ls
					.getFVersteckterAufschlag().doubleValue()));
			rechnungDto.setKostenstelleIId(ls.getKostenstelleIId());
			rechnungDto.setKundeIId(ls.getKundeIIdRechnungsadresse());
			rechnungDto.setKundeIIdStatistikadresse(ls
					.getKundeIIdLieferadresse());
			rechnungDto.setLagerIId(kundeDto.getLagerIIdAbbuchungslager());
			rechnungDto.setLieferartIId(ls.getLieferartIId());
			// rechnungDto.setLieferscheinIId(ls.getIId());
			rechnungDto.setMandantCNr(ls.getMandantCNr());
			rechnungDto.setNKurs(new BigDecimal(ls
					.getFWechselkursmandantwaehrungzubelegwaehrung()
					.doubleValue()).setScale(
					LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
					BigDecimal.ROUND_HALF_EVEN));
			if (rechnungstypCNr
					.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				rechnungDto
						.setRechnungartCNr(RechnungFac.RECHNUNGART_PROFORMARECHNUNG);
			} else {
				rechnungDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);
			}
			rechnungDto.setWaehrungCNr(ls.getWaehrungCNr());
			rechnungDto.setZahlungszielIId(ls.getZahlungszielIId());
			rechnungDto.setSpediteurIId(ls.getSpediteurIId());
			rechnungDto.setCBestellnummer(ls.getCBestellnummer());
			rechnungDto.setCBez(ls.getCBezProjektbezeichnung());
			rechnungDto.setPersonalIIdVertreter(ls.getPersonalIIdVertreter());
			// speichern und dto updaten
			rechnungDto = createRechnung(rechnungDto, theClientDto);

			List<String> nichtGelieferteLS = new ArrayList<String>();
			for (int i = 0; i < keys.length; i++) {
				Lieferschein lieferschein = this.em.find(Lieferschein.class,
						(Integer) keys[i]);
				if (!lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_GELIEFERT)) {
					nichtGelieferteLS.add(lieferschein.getCNr());
				}

				// Seit PJ18783 koennen LS verschiedener RE-Adressen geliefert
				// werden
				// jetzt die Position anlegen
				RechnungPositionDto rechnungPositionDto = new RechnungPositionDto();
				rechnungPositionDto.setArtikelIId(null);
				rechnungPositionDto.setAuftragpositionIId(null);
				rechnungPositionDto.setBDrucken(Helper.boolean2Short(true));
				rechnungPositionDto.setBRabattsatzuebersteuert(Helper
						.boolean2Short(false));
				rechnungPositionDto.setBMwstsatzuebersteuert(Helper
						.boolean2Short(false));
				rechnungPositionDto.setCBez(null);
				rechnungPositionDto.setEinheitCNr(null);
				rechnungPositionDto.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));
				if (mwstSatzDto != null) {
					rechnungPositionDto.setMwstsatzIId(mwstSatzDto.getIId());
				}
				rechnungPositionDto.setFRabattsatz(new Double(0));
				rechnungPositionDto.setISort(new Integer(0));

				rechnungPositionDto.setLieferscheinIId(lieferschein.getIId());
				rechnungPositionDto.setNMenge(null);
				rechnungPositionDto.setRechnungIId(rechnungDto.getIId());
				rechnungPositionDto.setRechnungIIdGutschrift(null);
				rechnungPositionDto.setXTextinhalt(null);
				rechnungPositionDto
						.setRechnungpositionartCNr(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN);
				rechnungPositionDto.setCZusatzbez(null);
				// speichern der position
				// hier brauch ich kein lager, da nix gebucht wird
				createRechnungPosition(rechnungPositionDto, null, theClientDto);

			}
			if (nichtGelieferteLS.size() > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN,
						"LS muss geliefert sein", nichtGelieferteLS.toString());
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
		return rechnungDto.getIId();
	}

	public void setRechnungStatusAufAngelegt(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		pruefeUpdateAufRechnungErlaubt(rechnungIId, theClientDto);
		// zuerst noetigenfalls die buchung zuruecknehmen
		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungIId);
			BelegbuchungDto bDto = null;
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_RECHNUNG)
					|| rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_ANZAHLUNG)
					|| rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				bDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_RECHNUNG, rechnungIId);
				if (bDto != null) {
					getBelegbuchungFac(theClientDto.getMandant())
							.verbucheRechnungRueckgaengig(rechnungIId,
									theClientDto);
				}
			} else if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_GUTSCHRIFT)
					|| rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
				bDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_GUTSCHRIFT, rechnungIId);
				if (bDto != null) {
					getBelegbuchungFac(theClientDto.getMandant())
							.verbucheGutschriftRueckgaengig(rechnungIId,
									theClientDto);
				}
			} else
				System.out.println("setRechnungStatusAufAngelegt: Belegart="
						+ rechnungDto.getBelegartCNr());
			// wenn schon verbucht
			rechnungDto.setStatusCNr(RechnungFac.STATUS_ANGELEGT);
			rechnungDto.setNWert(null);
			rechnungDto.setNWertust(null);
			rechnungDto.setNWertfw(null);
			rechnungDto.setNWertustfw(null);

			updateRechnung(rechnungDto, theClientDto);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) {
		return getBereitsBezahltWertVonRechnungFw(rechnungIId,
				zahlungIIdAusgenommen, null);
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungBrutto(Integer rechnungIId) {
		// berechnet die Summe der gestellten Anzahlungen! nicht der bezahlten
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			RechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(rechnungIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNWert() != null) {
					anzahlungen = anzahlungen.add(anzRechnungen[i].getNWert()
							.add(anzRechnungen[i].getNWertust()));

					// PJ18843
					RechnungDto[] gsDtos = getRechnungFac()
							.rechnungFindByRechnungIIdZuRechnung(
									anzRechnungen[i].getIId());

					for (int j = 0; j < gsDtos.length; j++) {
						if (!gsDtos[j].getStatusCNr().equals(
								RechnungFac.STATUS_STORNIERT)
								&& gsDtos[j].getNWert() != null) {
							anzahlungen = anzahlungen.subtract(gsDtos[j]
									.getNWert().subtract(
											gsDtos[j].getNWertust()));
						}

					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	private RechnungDto[] getAnzahlungsrechnungenZuSchlussrechnung(
			Integer schlussrechnungIId) throws EJBExceptionLP, RemoteException {
		RechnungDto rDto = getRechnungFac().rechnungFindByPrimaryKey(
				schlussrechnungIId);
		RechnungDto[] anzRechnungen = new RechnungDto[0];
		if (rDto.getAuftragIId() != null
				&& rDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG))
			anzRechnungen = getRechnungFac().rechnungFindByAuftragIId(
					rDto.getAuftragIId());
		return anzRechnungen;
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer rechnungIId) {
		// berechnet die Summe der gestellten Anzahlungen! nicht der bezahlten
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			RechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(rechnungIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNWertfw() != null) {
					anzahlungen = anzahlungen
							.add(anzRechnungen[i].getNWertfw());

					// PJ18843
					RechnungDto[] gsDtos = getRechnungFac()
							.rechnungFindByRechnungIIdZuRechnung(
									anzRechnungen[i].getIId());

					for (int j = 0; j < gsDtos.length; j++) {
						if (!gsDtos[j].getStatusCNr().equals(
								RechnungFac.STATUS_STORNIERT)
								&& gsDtos[j].getNWertfw() != null) {
							anzahlungen = anzahlungen.subtract(gsDtos[j]
									.getNWertfw());
						}

					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer rechnungIId) {
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			RechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(rechnungIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNWertfw() != null) {
					anzahlungen = anzahlungen.add(anzRechnungen[i]
							.getNWertustfw());

					// PJ18843
					RechnungDto[] gsDtos = getRechnungFac()
							.rechnungFindByRechnungIIdZuRechnung(
									anzRechnungen[i].getIId());

					for (int j = 0; j < gsDtos.length; j++) {
						if (!gsDtos[j].getStatusCNr().equals(
								RechnungFac.STATUS_STORNIERT)
								&& gsDtos[j].getNWertustfw() != null) {
							anzahlungen = anzahlungen.subtract(gsDtos[j]
									.getNWertustfw());
						}

					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId,
			Integer zahlungIIdAusgenommen, java.util.Date tStichtag) {
		BigDecimal bdBezahlt = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) { // @ToDo FinderException
			// wenns keine gibt
			return bdBezahlt;
		}
		// wenns keine gibt
		// return bdBezahlt;
		// }
		for (int i = 0; i < zahlungen.length; i++) {
			if (tStichtag == null
					|| !zahlungen[i].getDZahldatum().after(tStichtag)) {

				if (zahlungIIdAusgenommen != null
						&& zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					break;
				}

				if (zahlungIIdAusgenommen == null
						|| !zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					if (zahlungen[i].getFSkonto() == null) {
						bdBezahlt = bdBezahlt.add(zahlungen[i].getNBetragfw());
					} else {
						BigDecimal bdFaktor = new BigDecimal(1)
								.subtract(new BigDecimal(zahlungen[i]
										.getFSkonto().doubleValue())
										.movePointLeft(2));
						bdBezahlt = bdBezahlt.add(zahlungen[i].getNBetragfw()
								.divide(bdFaktor, FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));
					}
				}

			}
		}
		return bdBezahlt;
	}

	public BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
			Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws EJBExceptionLP {
		BigDecimal bdBezahlt = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) { // @ToDo FinderException
			// wenns keine gibt
			return bdBezahlt;
		}
		// wenns keine gibt
		// return bdBezahlt;
		// }
		for (int i = 0; i < zahlungen.length; i++) {
			if (zahlungIIdAusgenommen == null
					|| !zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
				bdBezahlt = bdBezahlt.add(zahlungen[i].getNBetragfw().add(
						zahlungen[i].getNBetragUstfw()));
			}
		}
		return bdBezahlt;
	}

	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(
			Integer rechnungIId, Integer zahlungIIdAusgenommen) {
		return getBereitsBezahltWertVonRechnungUstFw(rechnungIId,
				zahlungIIdAusgenommen, null);
	}

	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(
			Integer rechnungIId, Integer zahlungIIdAusgenommen,
			java.util.Date tStichtag) {
		BigDecimal bdBezahltUST = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) { // @ToDo FinderException
			// wenns keine gibt
			return bdBezahltUST;
		}
		// wenns keine gibt
		// return bdBezahltUST;
		// }
		for (int i = 0; i < zahlungen.length; i++) {
			if (tStichtag == null
					|| !zahlungen[i].getDZahldatum().after(tStichtag)) {

				if (zahlungIIdAusgenommen != null
						&& zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					break;
				}

				if (zahlungIIdAusgenommen == null
						|| !zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					if (zahlungen[i].getFSkonto() == null) {
						bdBezahltUST = bdBezahltUST.add(zahlungen[i]
								.getNBetragUstfw());
					} else {
						BigDecimal bdFaktor = new BigDecimal(1)
								.subtract(new BigDecimal(zahlungen[i]
										.getFSkonto().doubleValue())
										.movePointLeft(2));
						bdBezahltUST = bdBezahltUST.add(zahlungen[i]
								.getNBetragUstfw().divide(bdFaktor,
										FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));
					}
				}
			}
		}
		// ZahlungDto[] zahlungen=zahlungHome.
		return bdBezahltUST;
	}

	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) {
		return getBereitsBezahltWertVonRechnung(rechnungIId,
				zahlungIIdAusgenommen, null);
	}

	public java.util.Date getDatumLetzterZahlungseingang(Integer rechnungIId) {
		RechnungzahlungDto[] zahlungen = zahlungFindByRechnungIId(rechnungIId);

		if (zahlungen.length > 0) {
			return new java.util.Date(zahlungen[zahlungen.length - 1]
					.getDZahldatum().getTime());

		}

		return null;
	}

	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId,
			Integer zahlungIIdAusgenommen, java.util.Date tStichtag) {
		BigDecimal bdBezahlt = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) {
			// wenns keine gibt
			return bdBezahlt;
		}
		// wenns keine gibt
		// return bdBezahlt;
		// }
		for (int i = 0; i < zahlungen.length; i++) {

			if (tStichtag == null
					|| !zahlungen[i].getDZahldatum().after(tStichtag)) {

				if (zahlungIIdAusgenommen != null
						&& zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					break;
				}

				if (zahlungIIdAusgenommen == null
						|| !zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					if (zahlungen[i].getFSkonto() == null) {
						bdBezahlt = bdBezahlt.add(zahlungen[i].getNBetrag());
					} else {
						BigDecimal bdFaktor = new BigDecimal(1)
								.subtract(new BigDecimal(zahlungen[i]
										.getFSkonto().doubleValue())
										.movePointLeft(2));
						bdBezahlt = bdBezahlt.add(zahlungen[i].getNBetrag()
								.divide(bdFaktor, FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));
					}
				}
			}
		}
		return bdBezahlt;
	}

	public BigDecimal getBereitsBezahltWertVonRechnungUst(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) {
		return getBereitsBezahltWertVonRechnungUst(rechnungIId,
				zahlungIIdAusgenommen, null);
	}

	public BigDecimal getBereitsBezahltWertVonRechnungUst(Integer rechnungIId,
			Integer zahlungIIdAusgenommen, java.util.Date tStichtag)
			throws EJBExceptionLP {
		BigDecimal bdBezahltUST = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) {
			// wenns keine gibt
			return bdBezahltUST;
		}
		// wenns keine gibt
		// return bdBezahltUST;
		// }
		for (int i = 0; i < zahlungen.length; i++) {
			if (tStichtag == null
					|| !zahlungen[i].getDZahldatum().after(tStichtag)) {

				if (zahlungIIdAusgenommen != null
						&& zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					break;
				}

				if (zahlungIIdAusgenommen == null
						|| !zahlungIIdAusgenommen.equals(zahlungen[i].getIId())) {
					if (zahlungen[i].getFSkonto() == null) {
						bdBezahltUST = bdBezahltUST.add(zahlungen[i]
								.getNBetragUst());
					} else {
						BigDecimal bdFaktor = new BigDecimal(1)
								.subtract(new BigDecimal(zahlungen[i]
										.getFSkonto().doubleValue())
										.movePointLeft(2));
						bdBezahltUST = bdBezahltUST.add(zahlungen[i]
								.getNBetragUst().divide(bdFaktor,
										FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));
					}
				}
			}
		}
		// ZahlungDto[] zahlungen=zahlungHome.
		return bdBezahltUST;
	}

	/**
	 * Eine Rechnung aus einem Auftrag erstellen.
	 * 
	 * @param auftragIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createRechnungAusAuftrag(Integer auftragIId,
			Double dRabattAusRechnungsadresse, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return createRechnungAusAuftrag(auftragIId, null,
				dRabattAusRechnungsadresse, theClientDto);
	}

	/**
	 * Eine Rechnung aus einem Auftrag erstellen. nur fuer private verwendung.
	 * 
	 * @param auftragIId
	 *            Integer
	 * @param tBelegdatum
	 *            Date
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createRechnungAusAuftrag(Integer auftragIId,
			java.sql.Date tBelegdatum, Double dRabattAusRechnungsadresse,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// zuerst die neue Rechnung anlegen
			RechnungDto rechnungDto = new RechnungDto();
			// Auftrag und Kunde holen
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdRechnungsadresse(), theClientDto);
			// ------------------------------------------------------------------
			// ----
			// Ansprechpartner: vorbesetzen entscheidet ein Mandantenparameter
			// ------------------------------------------------------------------
			// ----
			ParametermandantDto parameterAnspVorbesetzen = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
							ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
			if ((Boolean) parameterAnspVorbesetzen.getCWertAsObject()) {
				// vorzugsweise aus dem Auftrag
				if (auftragDto.getAnsprechparnterIId() != null) {
					rechnungDto.setAnsprechpartnerIId(auftragDto
							.getAnsprechparnterIId());
				}
				// sonst den ersten des Kunden
				else {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeDto.getPartnerIId(), theClientDto);
					if (anspDto != null) {
						rechnungDto.setAnsprechpartnerIId(anspDto.getIId());
					}
				}
			} else {
				// nicht vorbesetzen
				rechnungDto.setAnsprechpartnerIId(null);
			}
			// ------------------------------------------------------------------
			// ----
			// weitere Eigenschaften uebernehmen
			// ------------------------------------------------------------------
			// ----
			rechnungDto.setAuftragIId(auftragIId);

			rechnungDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			rechnungDto.setBMwstallepositionen(Helper.boolean2Short(false));

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);

			boolean bReversecharge = (Boolean) parameter.getCWertAsObject();
			if (bReversecharge) {
				rechnungDto.setBReversecharge(kundeDto.getBReversecharge());
			} else {
				rechnungDto.setBReversecharge(Helper.boolean2Short(false));
			}
			rechnungDto.setCBestellnummer(auftragDto.getCBestellnummer());
			rechnungDto.setCBez(auftragDto.getCBezProjektbezeichnung());
			rechnungDto.setProjektIId(auftragDto.getProjektIId());
			rechnungDto.setCFusstextuebersteuert(null);
			rechnungDto.setCKopftextuebersteuert(null);
			rechnungDto.setCNr(null);
			rechnungDto.setCProvisiontext(null);
			rechnungDto.setCProvisiontext(null);
			// ------------------------------------------------------------------
			// ----
			// Allgemeiner Rabatt und Projektrabatt werden zusammengefasst.
			// ------------------------------------------------------------------
			// ----
			double dFaktor = 100.0;
			if (auftragDto.getFAllgemeinerRabattsatz() != null) {
				dFaktor = dFaktor - auftragDto.getFAllgemeinerRabattsatz();
			}
			if (auftragDto.getFProjektierungsrabattsatz() != null) {
				double dFaktor2 = 100.0 - auftragDto
						.getFProjektierungsrabattsatz();
				dFaktor = dFaktor * dFaktor2 / 100.0;
			}
			rechnungDto.setFAllgemeinerRabattsatz(new Double(100.0 - dFaktor));

			if (dRabattAusRechnungsadresse != null) {
				rechnungDto
						.setFAllgemeinerRabattsatz(dRabattAusRechnungsadresse);
			}

			rechnungDto.setFVersteckterAufschlag(auftragDto
					.getFVersteckterAufschlag().doubleValue());
			rechnungDto.setIGeschaeftsjahr(null); // das macht der BN-Generator
			rechnungDto.setIId(null); // das macht der BN-Generator
			rechnungDto.setKostenstelleIId(auftragDto.getKostIId());
			// ------------------------------------------------------------------
			// ----
			// Kunde/Statistikadresse lt. Beschreibung von WH:
			//
			// Bei Wiederhol ABs wird die Ueberleitung in die Statistikadresse
			// der Rechnung so gesteuert, dass
			// der Kunde der AB -> die Statistikadresse wird
			// die Rechnungsadresse wird der Kunde der Rechnung.
			// ------------------------------------------------------------------
			// ----
			rechnungDto.setKundeIId(auftragDto.getKundeIIdRechnungsadresse());
			rechnungDto.setKundeIIdStatistikadresse(auftragDto
					.getKundeIIdAuftragsadresse());
			// Hauptlager des Mandanten vorbesetzen
			rechnungDto.setLagerIId(auftragDto.getLagerIIdAbbuchungslager());
			rechnungDto.setLieferartIId(auftragDto.getLieferartIId());
			rechnungDto.setLieferscheinIId(null); // keine LS-Bindung

			// gemahnt sein
			rechnungDto.setMandantCNr(auftragDto.getMandantCNr());
			// ------------------------------------------------------------------
			// ----
			// Mwst-Satz (falls ueber alle Positionen gleich)
			// ------------------------------------------------------------------
			// ----
			MwstsatzbezDto mwstSatzbezDto = null;
			// zuerst der aus dem Kunden
			if (kundeDto.getMwstsatzbezIId() != null) {
				mwstSatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(
						kundeDto.getMwstsatzbezIId(), theClientDto);
			}
			// falls dort nicht definiert, gibt es beim Mandanten einen
			// Default-Wert
			else {
				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);
				if (mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz() != null) {
					mwstSatzbezDto = getMandantFac()
							.mwstsatzbezFindByPrimaryKey(
									mandantDto
											.getMwstsatzbezIIdStandardinlandmwstsatz(),
									theClientDto);
				}
			}
			// Wenn eine gueltige Mwst-satz-bezeichnung gefunden wurde, hol ich
			// den aktuellen Steuersatz dazu.
			if (mwstSatzbezDto != null) {
				MwstsatzDto mwstSatzDto = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								mwstSatzbezDto.getIId(), theClientDto);
				rechnungDto.setMwstsatzIId(mwstSatzDto.getIId());
			}
			// ------------------------------------------------------------------
			// ----
			// Es wird der aktuelle Wechselkurs eingetragen (nicht aus dem
			// Auftrag)
			// der koennte sich ja in der Zwischenzeit geaendert haben.
			// ------------------------------------------------------------------
			// ----
			BigDecimal bdKurs = getLocaleFac().getWechselkurs2(
					theClientDto.getSMandantenwaehrung(),
					auftragDto.getCAuftragswaehrung(), theClientDto);
			rechnungDto.setNKurs(bdKurs);
			rechnungDto.setNProvision(null);
			// hat noch keine Werte (werden beim Aktivieren gesetzt)
			rechnungDto.setNWert(null);
			rechnungDto.setNWertfw(null);
			rechnungDto.setNWertust(null);
			rechnungDto.setNWertustfw(null);
			// die personal-iid's werden auch woanders gesetzt
			rechnungDto.setPersonalIIdAendern(null);
			rechnungDto.setPersonalIIdAnlegen(null);
			rechnungDto.setPersonalIIdManuellerledigt(null);
			rechnungDto.setPersonalIIdStorniert(null);
			// ------------------------------------------------------------------
			// ----
			// Vertreter kommt aus dem Auftrag, wenn dort definiert
			// Sonst bestimmt ein Mandantenparameter, was hier passiert
			// ------------------------------------------------------------------
			// ----
			if (auftragDto.getPersonalIIdVertreter() != null) {
				// Vertreter aus Auftrag
				rechnungDto.setPersonalIIdVertreter(auftragDto
						.getPersonalIIdVertreter());
			} else {
				// Vertreter ist im Auftrag definiert
				ParametermandantDto parameterVertreterAusKunde = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
								ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN);
				// kommt er aus dem Kunden?
				if ((Boolean) parameterVertreterAusKunde.getCWertAsObject()) {
					if (kundeDto.getPersonaliIdProvisionsempfaenger() != null) {
						rechnungDto.setPersonalIIdVertreter(kundeDto
								.getPersonaliIdProvisionsempfaenger());
					}
					// wenns beim Kunden keinen gibt, ists der Benutzer selbst
					else {
						rechnungDto.setPersonalIIdVertreter(theClientDto
								.getIDPersonal());
					}
				}
				// nicht aus dem Kunden -> ist der Benutzer
				else {
					rechnungDto.setPersonalIIdVertreter(theClientDto
							.getIDPersonal());
				}
			}
			rechnungDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);
			rechnungDto.setRechnungIIdZurechnung(null); // dieses Feld ist nur
			// fuer Gutschriften
			rechnungDto.setSpediteurIId(auftragDto.getSpediteurIId());
			rechnungDto.setStatusCNr(null); // macht das create
			rechnungDto.setTAendern(null); // macht das create
			rechnungDto.setTAnlegen(null); // macht das create
			if (tBelegdatum != null) {

				// rechnungDto.setTBelegdatum(tBelegdatum);
				rechnungDto.setTBelegdatum(Helper
						.cutTimestamp(new java.sql.Timestamp(tBelegdatum
								.getTime())));
			} else {
				rechnungDto.setTBelegdatum(Helper
						.cutTimestamp(new java.sql.Timestamp(System
								.currentTimeMillis())));
			}
			rechnungDto.setTBezahltdatum(null); // kann noch nicht bezahlt sein
			rechnungDto.setTFibuuebernahme(null); // beim Export
			rechnungDto.setTGedruckt(null); // erst beim Aktivieren/Ausdrucken

			// sein
			rechnungDto.setTMahnsperrebis(null); // default gibts keine
			// Mahnsperre
			rechnungDto.setTManuellerledigt(null); // erst bei manuell erledigen
			rechnungDto.setTStorniert(null); // erst beim Storniern
			rechnungDto.setWaehrungCNr(auftragDto.getCAuftragswaehrung());
			rechnungDto.setZahlungszielIId(auftragDto.getZahlungszielIId());
			// speichern und dto updaten
			rechnungDto = createRechnung(rechnungDto, theClientDto);

			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIId);

			if (aAuftragpositionDto != null && aAuftragpositionDto.length > 0) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					// Kalkulatorische Artikel sofort Erledigen, damit diese im
					// LS nicht aufscheinen
					if (aAuftragpositionDto[i].getArtikelIId() != null) {
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										aAuftragpositionDto[i].getArtikelIId(),
										theClientDto);
						if (Helper.short2boolean(aDto.getBKalkulatorisch())) {

							aAuftragpositionDto[i]
									.setAuftragpositionstatusCNr(LocaleFac.STATUS_ERLEDIGT);
							getAuftragpositionFac()
									.updateAuftragpositionOhneWeitereAktion(
											aAuftragpositionDto[i],
											theClientDto);

						}
					}
				}
			}

			return rechnungDto.getIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Wenn fuer eine Rechnung eine neue Position zwischen den bestehenden
	 * Positionen eingefuegt werden soll, dann schafft diese Methode Platz jener
	 * Position in der Sortierung, an der eingefuegt werden soll. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param iSortierungNeuePositionI
	 *            die Position, an der eingeufegt werden soll
	 * @throws EJBExceptionLP
	 */
	private void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdRechnungI, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, iIdRechnungI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();
		// ArrayList mit allen anzupassenden Positionen in umgekehrter
		// Reihenfolge
		// Die Reihenfolge muss umgekehrt sein, damit der UK-Constraint nicht
		// verletzt wird
		ArrayList<Rechnungposition> a = new ArrayList<Rechnungposition>();
		while (it.hasNext()) {
			Rechnungposition oPosition = (Rechnungposition) it.next();
			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				a.add(0, oPosition);
			}
		}
		// Nun die Positionen updaten
		for (Iterator<?> iter = a.iterator(); iter.hasNext();) {
			Rechnungposition oPosition = (Rechnungposition) iter.next();
			iSortierungNeuePositionI++;
			oPosition.setISort(oPosition.getISort() + 1);
		}
	}

	public RechnungPositionDto rechnungpositionFindByRechnungIIdPositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI) {
		RechnungPositionDto angebotpositionDto = null;
		try {
			angebotpositionDto = rechnungpositionFindByRechnungIIdPositionsartCNr(
					iIdAngebotI, positionsartCNrI);
		} catch (EJBExceptionLP ex) {
			// do nothing
		}
		return angebotpositionDto;
	}

	public RechnungPositionDto rechnungpositionFindByRechnungIIdPositionsartCNr(
			Integer iIdRechnungI, String positionsartCNrI)
			throws EJBExceptionLP {
		RechnungPositionDto pechnungPositionDto = null;
		try {
			Query query = em
					.createNamedQuery("RechnungpositionfindByRechnungIIdPositionsartCNr");
			query.setParameter(1, iIdRechnungI);
			query.setParameter(2, positionsartCNrI);
			Rechnungposition angebotposition = (Rechnungposition) query
					.getSingleResult();
			pechnungPositionDto = assembleRechnungPositionDto(angebotposition);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler. Es gibt mehere Positionen mit positionsart "
							+ positionsartCNrI + " fuer rechnungIId "
							+ iIdRechnungI);
		}
		return pechnungPositionDto;
	}

	private void sortierungAnpassenInBezugAufEndsumme(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdAngebotI);
		RechnungPositionDto rechnungPositionDto = rechnungpositionFindByRechnungIIdPositionsartCNrOhneExc(
				iIdAngebotI, RechnungFac.POSITIONSART_RECHNUNG_ENDSUMME);

		if (rechnungPositionDto != null) {
			RechnungPositionDto[] aRechnungPositionDto = rechnungPositionFindByRechnungIId(iIdAngebotI);

			for (int i = 0; i < aRechnungPositionDto.length; i++) {
				if (aRechnungPositionDto[i].getPositionsartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_ENDSUMME)) {
					int iIndexLetztePreisbehaftetePositionNachEndsumme = -1;

					for (int j = i + 1; j < aRechnungPositionDto.length; j++) {
						if (aRechnungPositionDto[j].getNEinzelpreis() != null) {
							// die Position der letzten preisbehafteten Position
							// nach der Endsumme bestimmen
							iIndexLetztePreisbehaftetePositionNachEndsumme = j;
						}
					}

					if (iIndexLetztePreisbehaftetePositionNachEndsumme != -1) {
						// die Endsumme muss nach die letzte preisbehaftete
						// Position verschoben werden
						for (int k = i; k < iIndexLetztePreisbehaftetePositionNachEndsumme; k++) {
							vertauschePositionen(
									aRechnungPositionDto[i].getIId(),
									aRechnungPositionDto[k + 1].getIId());
						}
					}
				}
			}
		}
	}

	private void sortierungAnpassenBeiLoeschenEinerPosition(
			Integer iIdRechnungI, int iSortierungGeloeschtePositionI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, iIdRechnungI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();
		while (it.hasNext()) {
			Rechnungposition oPosition = (Rechnungposition) it.next();
			if (oPosition.getISort().intValue() >= iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(
						oPosition.getISort().intValue() - 1));
			}
		}
	}

	public Integer getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(
			Integer iIdRechnungI, Integer lieferscheinIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer letzterISortLS = null;
		try {
			String ansprechpartner = "";
			LieferscheinDto lsDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinIId, theClientDto);

			if (lsDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								lsDto.getAnsprechpartnerIId(), theClientDto);

				ansprechpartner = Helper.fitString2Length(anspDto
						.getPartnerDto().formatFixName1Name2(), 200, ' ')
						+ lsDto.getCNr();
			} else {
				ansprechpartner = Helper.fitString2Length("", 200, ' ')
						+ lsDto.getCNr();
			}

			Query query = em
					.createNamedQuery("RechnungPositionfindByRechnungIId");
			query.setParameter(1, iIdRechnungI);
			RechnungPositionDto[] dtos = assembleRechnungpositionDtos(query
					.getResultList());

			for (int i = 0; i < dtos.length; i++) {
				RechnungPositionDto o = dtos[i];

				if (o.getLieferscheinIId() != null) {
					String vergleich = "";

					LieferscheinDto lsVergleichDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									o.getLieferscheinIId(), theClientDto);
					letzterISortLS = o.getISort();
					if (lsVergleichDto.getAnsprechpartnerIId() != null) {
						AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(
										lsVergleichDto.getAnsprechpartnerIId(),
										theClientDto);

						vergleich = Helper.fitString2Length(anspDto
								.getPartnerDto().formatFixName1Name2(), 200,
								' ')
								+ lsVergleichDto.getCNr();
					} else {
						vergleich = Helper.fitString2Length("", 200, ' ')
								+ lsVergleichDto.getCNr();
					}

					if (vergleich.compareTo(ansprechpartner) > 0) {
						return letzterISortLS;
					}

				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return letzterISortLS;
	}

	public void sortiereNachLieferscheinAnsprechpartner(Integer iIdRechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, iIdRechnungI);
		RechnungPositionDto[] dtos = assembleRechnungpositionDtos(query
				.getResultList());

		try {
			for (int i = dtos.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					RechnungPositionDto o = dtos[j];
					RechnungPositionDto o1 = dtos[j + 1];

					String ansprechpartner = "";

					if (o.getLieferscheinIId() != null) {
						LieferscheinDto lsDto = getLieferscheinFac()
								.lieferscheinFindByPrimaryKey(
										o.getLieferscheinIId(), theClientDto);

						if (lsDto.getAnsprechpartnerIId() != null) {
							AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
									.ansprechpartnerFindByPrimaryKey(
											lsDto.getAnsprechpartnerIId(),
											theClientDto);

							ansprechpartner = Helper.fitString2Length(anspDto
									.getPartnerDto().formatFixName1Name2(),
									200, ' ')
									+ lsDto.getCNr();
						} else {
							ansprechpartner = Helper.fitString2Length("", 200,
									' ') + lsDto.getCNr();
						}

					} else {
						ansprechpartner = "ZZZZZZZZZZZZZZZZ";
					}

					String ansprechpartner1 = "";

					if (o1.getLieferscheinIId() != null) {
						LieferscheinDto lsDto = getLieferscheinFac()
								.lieferscheinFindByPrimaryKey(
										o1.getLieferscheinIId(), theClientDto);

						if (lsDto.getAnsprechpartnerIId() != null) {
							AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
									.ansprechpartnerFindByPrimaryKey(
											lsDto.getAnsprechpartnerIId(),
											theClientDto);

							ansprechpartner1 = Helper.fitString2Length(anspDto
									.getPartnerDto().formatFixName1Name2(),
									200, ' ')
									+ lsDto.getCNr();
						} else {
							ansprechpartner1 = Helper.fitString2Length("", 200,
									' ') + lsDto.getCNr();
						}

					} else {
						ansprechpartner1 = "ZZZZZZZZZZZZZZZZ";
					}

					if (ansprechpartner.compareTo(ansprechpartner1) > 0) {
						dtos[j] = o1;
						dtos[j + 1] = o;
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		int iSort = 1;
		for (int i = 0; i < dtos.length; i++) {
			Rechnungposition rechnung = em.find(Rechnungposition.class,
					dtos[i].getIId());

			rechnung.setISort(iSort);

			em.merge(rechnung);
			em.flush();

			iSort++;
		}

		/*
		 * Iterator<?> it = clPositionen.iterator(); while (it.hasNext()) {
		 * Rechnungposition oPosition = (Rechnungposition) it.next(); if
		 * (oPosition.getISort().intValue() >= iSortierungGeloeschtePositionI) {
		 * oPosition.setISort(new Integer( oPosition.getISort().intValue() -
		 * 1)); } }
		 */
	}

	public void sortiereNachLieferscheinNummer(Integer iIdRechnungI,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, iIdRechnungI);
		RechnungPositionDto[] dtos = assembleRechnungpositionDtos(query
				.getResultList());

		try {
			for (int i = dtos.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					RechnungPositionDto o = dtos[j];
					RechnungPositionDto o1 = dtos[j + 1];

					String lsNR = "";

					if (o.getLieferscheinIId() != null) {
						LieferscheinDto lsDto = getLieferscheinFac()
								.lieferscheinFindByPrimaryKey(
										o.getLieferscheinIId(), theClientDto);

						lsNR = lsDto.getCNr();

					} else {
						lsNR = "0000000000";
					}

					String lsNr1 = "";

					if (o1.getLieferscheinIId() != null) {
						LieferscheinDto lsDto = getLieferscheinFac()
								.lieferscheinFindByPrimaryKey(
										o1.getLieferscheinIId(), theClientDto);

						lsNr1 = lsDto.getCNr();

					} else {
						lsNr1 = "0000000000";
					}

					if (lsNR.compareTo(lsNr1) > 0) {
						dtos[j] = o1;
						dtos[j + 1] = o;
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		int iSort = 1;
		for (int i = 0; i < dtos.length; i++) {
			Rechnungposition rechnung = em.find(Rechnungposition.class,
					dtos[i].getIId());

			rechnung.setISort(iSort);

			em.merge(rechnung);
			em.flush();

			iSort++;
		}

	}

	/**
	 * Eine Gutschrift aus einer Rechnung erstellen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createGutschriftAusRechnung(Integer rechnungIId,
			java.sql.Date dBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return createGutschriftAusRechnungImpl(rechnungIId, dBelegdatum,
				Gutschriftart.GUTSCHRIFT, theClientDto);
	}

	/**
	 * Eine Gutschrift aus einer Rechnung erstellen und dabei die Gutschriftsart
	 * setzen
	 * 
	 * G&uuml;tige Gutschriftarten sind: RECHNUNGART_GUTSCHRIFT,
	 * RECHNUNGART_WERTGUTSCHRIFT
	 * 
	 * @return die Id der neu erstellten Gutschrift
	 */
	public Integer createGutschriftAusRechnung(Integer rechnungIId,
			java.sql.Date dBelegdatum, String gutschriftartCnr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Gutschriftart gutschriftart = Gutschriftart.fromCnr(gutschriftartCnr);
		return createGutschriftAusRechnungImpl(rechnungIId, dBelegdatum,
				gutschriftart, theClientDto);
	}

	private Integer createGutschriftAusRechnungImpl(Integer rechnungIId,
			java.sql.Date dBelegdatum, Gutschriftart gutschriftart,
			TheClientDto theClientDto) {
		RechnungDto newR = new RechnungDto();
		try {
			RechnungDto oldR = rechnungFindByPrimaryKey(rechnungIId);
			newR.setAnsprechpartnerIId(oldR.getAnsprechpartnerIId());
			newR.setBMindermengenzuschlag(Helper.boolean2Short(false));
			newR.setBMwstallepositionen(oldR.getBMwstallepositionen());
			newR.setIGeschaeftsjahr(getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant(), dBelegdatum));
			newR.setKostenstelleIId(oldR.getKostenstelleIId());
			newR.setKundeIId(oldR.getKundeIId());
			newR.setKundeIIdStatistikadresse(oldR.getKundeIIdStatistikadresse());
			newR.setLagerIId(oldR.getLagerIId());
			newR.setMandantCNr(oldR.getMandantCNr());
			newR.setMwstsatzIId(oldR.getMwstsatzIId());
			newR.setNKurs(oldR.getNKurs());
			newR.setRechnungartCNr(gutschriftart.asCnr());
			newR.setRechnungIIdZurechnung(oldR.getIId());
			newR.setWaehrungCNr(oldR.getWaehrungCNr());
			newR.setTBelegdatum(new Timestamp(dBelegdatum.getTime()));
			newR.setPersonalIIdVertreter(oldR.getPersonalIIdVertreter());
			newR.setLieferartIId(oldR.getLieferartIId());
			newR.setZahlungszielIId(oldR.getZahlungszielIId());
			newR.setSpediteurIId(oldR.getSpediteurIId());

			// SP1494
			newR.setCBestellnummer(oldR.getCBestellnummer());
			newR.setCBez(oldR.getCBez());

			newR = createRechnung(newR, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return newR.getIId();
	}

	public void mahneRechnung(Integer rechnungIId, Integer mahnstufeIId,
			java.sql.Date dMahndatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// Wenn mahnstufe und Datum == null dann wird die Mahnstufe
			// zurueckgesetzt
			if ((mahnstufeIId == null) && (dMahndatum == null)) {

				// Danach alle Mahnungen die zur Rechnung gehoeren entfernen
				MahnungDto[] mahnungDto = getMahnwesenFac()
						.mahnungFindByRechnungIId(rechnungIId);
				for (int i = 0; i < mahnungDto.length; i++) {
					mahnungDto[i].setTGedruckt(null);
					getMahnwesenFac()
							.removeMahnung(mahnungDto[i], theClientDto);
				}
			}
			// einen Mahnlauf aus der Rechnung generieren
			MahnlaufDto mahnlaufDto = getMahnwesenFac()
					.createMahnlaufAusRechnung(rechnungIId, mahnstufeIId,
							dMahndatum, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public boolean setzeMahnstufeZurueck(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);

		Integer mahnstufeIId = getMahnwesenFac()
				.getAktuelleMahnstufeEinerRechnung(rechnungIId, theClientDto);

		if (mahnstufeIId == null) {
			// ist ja eh noch nicht gemahnt
			return true;
		}
		int iNeueMahnstufe = mahnstufeIId.intValue() - 1;
		// Mahnstufe 1 wird zurueckgenommen
		if (iNeueMahnstufe <= 0) {

			// loeschen der zurueckgenommenen Mahnung
			MahnungDto mahnungDto = getMahnwesenFac()
					.mahnungFindByRechnungMahnstufe(rechnungIId, 1);
			if (mahnungDto == null) {
				return false;
			}

			mahnungDto.setTGedruckt(null);
			getMahnwesenFac().removeMahnung(mahnungDto, theClientDto);
			return true;
		}
		// Mahnstufe 99 wird zurueckgenommen
		if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
			// suchen der hoechsten Mahnstufe und setzen dieser

			MahnstufeDto[] mahnstufenDto = getMahnwesenFac()
					.mahnstufeFindByMandantCNr(theClientDto.getMandant());
			if (mahnstufenDto != null) {
				int iMaxMahnstufe = mahnstufenDto[0].getIId().intValue();
				for (int i = 1; i < mahnstufenDto.length; i++) {
					if ((mahnstufenDto[i].getIId().intValue() > iMaxMahnstufe)
							&& (mahnstufenDto[i].getIId().intValue() != FinanzServiceFac.MAHNSTUFE_99)) {
						iMaxMahnstufe = mahnstufenDto[i].getIId().intValue();
					}
				}

				// zugehoerige Mahnung loeschen
				MahnungDto letzeMahnungDto = getMahnwesenFac()
						.mahnungFindByRechnungMahnstufe(rechnungIId,
								FinanzServiceFac.MAHNSTUFE_99);
				letzeMahnungDto.setTGedruckt(null);
				getMahnwesenFac().removeMahnung(letzeMahnungDto, theClientDto);
				return true;
			}
		}

		// Loeschen der alten Mahnung
		MahnungDto letzteMahnungDto = getMahnwesenFac()
				.mahnungFindByRechnungMahnstufe(rechnungIId, iNeueMahnstufe + 1);
		letzteMahnungDto.setTGedruckt(null);
		getMahnwesenFac().removeMahnung(letzteMahnungDto, theClientDto);
		return true;
	}

	/**
	 * Zwei bestehende Rechnungspositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I
	 *            PK der ersten Position
	 * @param iIdPosition2I
	 *            PK der zweiten Position
	 * @throws EJBExceptionLP
	 */
	public void vertauschePositionen(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP {
		Rechnungposition oPosition1 = em.find(Rechnungposition.class,
				iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		Rechnungposition oPosition2 = em.find(Rechnungposition.class,
				iIdPosition2I);
		// nothing here
		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() == null) {

			// das zweite iSort auf ungueltig setzen, damit UK
			// constraint nicht verletzt wird
			oPosition2.setISort(new Integer(-1));

			oPosition1.setISort(iSort2);
			oPosition2.setISort(iSort1);
		} else if (oPosition1.getTypCNr() == null
				&& oPosition2.getTypCNr() != null) {

			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					Query query = em
							.createNamedQuery("RechnungPositionfindRechnungIIdISort");
					query.setParameter(1, oPosition2.getRechnungIId());
					query.setParameter(2, oPosition2.getISort() - 1);
					Rechnungposition oPos = (Rechnungposition) query
							.getSingleResult();
					oPosition1.setTypCNr(oPos.getTypCNr());
					oPosition1.setPositionIId(oPos.getPositionIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			} else if (oPosition2.getTypCNr().equals(
					LocaleFac.POSITIONTYP_ALLES)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_VERDICHTET)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_OHNEPREISE)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_MITPREISE)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_BEGINN)) {
					oPosition1.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					oPosition1.setPositionIId(oPosition2.getIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() == null) {

		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() != null) {

			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					oPosition1.setTypCNr(null);
					oPosition1.setPositionIId(null);
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				} else {
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));
					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		}
	}

	public void vertauscheRechnungspositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		pruefeUpdateAufRechnungErlaubt(
				rechnungPositionFindByPrimaryKey(iIdBasePosition)
						.getRechnungIId(), theClientDto);
		CompositeISort<Rechnungposition> comp = new CompositeISort<Rechnungposition>(
				new RechnungpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		RechnungPositionDto pos = rechnungPositionFindByPrimaryKey(iIdBasePosition);
		updateTAendern(pos.getRechnungIId(), theClientDto);
	}

	public void vertauscheRechnungspositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		pruefeUpdateAufRechnungErlaubt(
				rechnungPositionFindByPrimaryKey(iIdBasePosition)
						.getRechnungIId(), theClientDto);
		CompositeISort<Rechnungposition> comp = new CompositeISort<Rechnungposition>(
				new RechnungpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		RechnungPositionDto pos = rechnungPositionFindByPrimaryKey(iIdBasePosition);
		updateTAendern(pos.getRechnungIId(), theClientDto);
	}

	public Boolean hatRechnungPositionen(Integer rechnungIId)
			throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungPosition.class);
			c.add(Restrictions
					.eq(RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID,
							rechnungIId));
			List<?> list = c.list();
			if (list.size() > 0) {
				return new Boolean(true);
			} else {
				return new Boolean(false);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void manuellErledigen(Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(iIdRechnungI);
		Rechnung rechnung = em.find(Rechnung.class, iIdRechnungI);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_VERBUCHT)
				|| rechnung.getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
				|| rechnung.getStatusCNr().equals(
						RechnungFac.STATUS_TEILBEZAHLT)) {
			rechnung.setStatusCNr(RechnungFac.STATUS_BEZAHLT);
			rechnung.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			rechnung.setTManuellerledigt(getTimestamp());
			rechnung.setTBezahltdatum(getDate());
		} else if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) {
			// in diesem Fall die Erledigung zuruecknehmen
			rechnung.setPersonalIIdManuellerledigt(null);
			rechnung.setTManuellerledigt(null);
			rechnung.setTBezahltdatum(null);
			Query query = em
					.createNamedQuery("RechnungzahlungfindByRechnungIId");
			query.setParameter(1, iIdRechnungI);
			Collection<?> cl = query.getResultList();
			if (cl.isEmpty()) {
				// noch keine zahlungen
				if (rechnung.getTFibuuebernahme() != null) {
					rechnung.setStatusCNr(RechnungFac.STATUS_VERBUCHT);
				} else {
					rechnung.setStatusCNr(RechnungFac.STATUS_OFFEN);
				}
			} else {
				// es gibt schon zahlungen
				rechnung.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Rechnung kann nicht manuell erledigt werden, Status : "
									+ rechnung.getStatusCNr()));
		}
	}

	/*
	 * public void manuellErledigen(Integer iIdRechnungI, TheClientDto
	 * theClientDto) throws EJBExceptionLP { TheClientDto theClientDto =
	 * check(cNrUserI); myLogger.logData(iIdRechnungI);
	 * 
	 * // try { Rechnung rechnung = em.find(Rechnung.class, iIdRechnungI); if
	 * (rechnung == null) { throw new EJBExceptionLP(
	 * EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null); }
	 * 
	 * if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_VERBUCHT) ||
	 * rechnung.getStatusCNr().equals(RechnungFac.STATUS_OFFEN) ||
	 * rechnung.getStatusCNr().equals( RechnungFac.STATUS_TEILBEZAHLT)) {
	 * rechnung.setStatusCNr(RechnungFac.STATUS_BEZAHLT); rechnung
	 * .setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
	 * rechnung.setTManuellerledigt(getTimestamp()); } else if
	 * (rechnung.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) { // in
	 * diesem Fall die Erledigung zuruecknehmen
	 * rechnung.setPersonalIIdManuellerledigt(null);
	 * rechnung.setTManuellerledigt(null); Query query = em
	 * .createNamedQuery("RechnungzahlungfindByRechnungIId");
	 * query.setParameter(1, iIdRechnungI); Collection<?> cl =
	 * query.getResultList(); // if (cl.isEmpty()) { // // noch keine zahlungen
	 * // if (rechnung.getTFibuuebernahme() != null) { //
	 * rechnung.setStatusCNr(RechnungFac.STATUS_VERBUCHT); // } // else {
	 * rechnung.setStatusCNr(RechnungFac.STATUS_OFFEN); // } } else { // es gibt
	 * schon zahlungen rechnung.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT); }
	 * } else { throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new
	 * Exception( "Rechnung kann nicht manuell erledigt werden, Status : " +
	 * rechnung.getStatusCNr())); } // } // catch (FinderException ex) { //
	 * throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, //
	 * ex); // } }
	 */

	public void setzeMahnsperre(Integer rechnungIId, Date tMahnsperre,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("rechnungIId=" + rechnungIId + " " + tMahnsperre,
				theClientDto.getIDUser());
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		if (rechnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		rechnung.setTMahnsperrebis(tMahnsperre);
	}

	/**
	 * Den Status einer ER von 'Erledigt' auf 'Teilerledigt' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param rechnungIId
	 *            PK der ER
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void erledigungAufheben(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("rechnungIId=" + rechnungIId, theClientDto.getIDUser());
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) {
			if (rechnung.getPersonalIIdManuellerledigt() != null
					&& rechnung.getTManuellerledigt() != null) {

				rechnung.setPersonalIIdManuellerledigt(null);
				rechnung.setTManuellerledigt(null);
				RechnungzahlungDto[] zahlungenDtos = zahlungFindByRechnungIId(rechnungIId);
				if (zahlungenDtos != null && zahlungenDtos.length > 0) {
					rechnung.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
				} else {
					rechnung.setStatusCNr(RechnungFac.STATUS_OFFEN);
				}

			} else {
				// throw new EJBExceptionLP(
				// EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT,
				// new
				// Exception("Dieser Auftrag wurde nicht manuell erledigt"));
				rechnung.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
				myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl die Rechnung nicht manuell erledigt wurde, IId: "
						+ rechnungIId);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung der Rechnung kann nicht aufgehoben werden, Status: "
									+ rechnung.getStatusCNr()));
		}
	}

	public void aktiviereRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		pruefeAktivierbar(rechnungIId, theClientDto);
		// Wert berechnen
		berechneBeleg(rechnungIId, theClientDto);
		// und Status aendern
		aktiviereBeleg(rechnungIId, theClientDto);
	}

	@Override
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.aktiviereBelegControlled(this, iid, t,
				theClientDto);
		// new BelegAktivierungController(this).aktiviereBelegControlled(iid, t,
		// theClientDto);
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneBelegControlled(this, iid,
				theClientDto);
		// return new BelegAktivierungController(this).berechneBelegControlled(
		// iid, theClientDto);
	}

	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		RechnungDto rechnungDto = rechnungFindByPrimaryKey(iid);
		// nur wenn sie noch nicht aktiviert ist
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)
				|| rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_TEILBEZAHLT)
				|| rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
				|| rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_VERBUCHT)) {
			return;
		}

		// Positionen holen
		RechnungPositionDto[] rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
				.getIId());

		// Belege ohne Positionen duerfen nicht aktiviert werden
		if (rePosDto == null || rePosDto.length == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINE_POSITIONEN,
					new Exception(
							rechnungDto.getRechnungartCNr()
									+ " "
									+ rechnungDto.getCNr()
									+ " hat keine Positionen und kann daher nicht aktiviert werden"));
		}

		pruefeObGleicherMwstSatzBeiReverseCharge(rechnungDto, rePosDto);
	}

	/**
	 * Bei ReverseCharge Rechnung m&uuml;ssen alle Positionen den gleichen
	 * Mwstsatz haben</br>
	 * 
	 * Eigentlich ist das ein Workaround f&uuml;r das sp&auml;ter folgende
	 * Verbuchen der Rechnung
	 * 
	 * @param rechnungDto
	 * @param positionDtos
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private void pruefeObGleicherMwstSatzBeiReverseCharge(
			RechnungDto rechnungDto, RechnungPositionDto[] positionDtos)
			throws EJBExceptionLP, RemoteException {
		if (!rechnungDto.isReverseCharge())
			return;

		MwstSatzVerifier mwstVerifier = new MwstSatzVerifier();
		if (!mwstVerifier.isSame(positionDtos)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC,
					new Exception(rechnungDto.getCNr()));
		}

		if (mwstVerifier.getMwstSatzId() != null) {
			if (!mwstVerifier.getMwstSatzId().equals(
					rechnungDto.getMwstsatzIId())) {
				Rechnung rechnung = em.find(Rechnung.class,
						rechnungDto.getIId());
				rechnung.setMwstsatzIId(mwstVerifier.getMwstSatzId());
				em.merge(rechnung);
				em.flush();
				rechnungDto.setMwstsatzIId(mwstVerifier.getMwstSatzId());
			}
		}
	}

	private class MwstSatzVerifier {
		private Integer mwstSatzId;

		public boolean isSame(RechnungPositionDto[] positionDtos)
				throws EJBExceptionLP, RemoteException {
			boolean same = true;

			for (int i = 0; same && (i < positionDtos.length); i++) {
				if (positionDtos[i].isLieferschein()) {
					same &= isSame(getLieferscheinpositionFac()
							.lieferscheinpositionFindByLieferscheinIId(
									positionDtos[i].getLieferscheinIId()));
				} else {
					if (positionDtos[i].isMengenbehaftet()) {
						same &= isSame(positionDtos[i]);
					}
				}
			}

			return same;
		}

		public boolean isSame(BelegpositionVerkaufDto[] positionDtos) {
			for (int i = 0; i < positionDtos.length; i++) {
				if (!isSame(positionDtos[i])) {
					return false;
				}
			}
			return true;
		}

		public boolean isSame(BelegpositionVerkaufDto positionDto) {
			if (positionDto.isMengenbehaftet()) {
				if (mwstSatzId == null) {
					mwstSatzId = positionDto.getMwstsatzIId();
				}
				return mwstSatzId.equals(positionDto.getMwstsatzIId());
			}

			return true;
		}

		public Integer getMwstSatzId() {
			return mwstSatzId;
		}
	}

	@Override
	public void aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Rechnung rechnung = em.find(Rechnung.class, iid);
		if (rechnung.getStatusCNr().equals(STATUS_STORNIERT))
			return;

		// PJ 15710
		Kunde kunde = em.find(Kunde.class, rechnung.getKundeIId());
		kunde.setBIstinteressent(Helper.boolean2Short(false));
		em.merge(kunde);
		em.flush();

		// SP 2012/00448
		ParametermandantDto parametermandantautoDebitDto = getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
		if ((Boolean) parametermandantautoDebitDto.getCWertAsObject()) {
			if (rechnung.getKundeIId() != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnung.getKundeIId(), theClientDto);
				if (kundeDto.getIidDebitorenkonto() == null) {
					KontoDto ktoDto = getKundeFac()
							.createDebitorenkontoZuKundenAutomatisch(
									rechnung.getKundeIId(), false, null,
									theClientDto);
					kundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
							ktoDto.getCNr()));
					getKundeFac().updateKunde(kundeDto, theClientDto);
				}
			}
		}
		if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {
			// neuer Status: OFFEN
			rechnung.setStatusCNr(RechnungFac.STATUS_OFFEN);
			rechnung.setTGedruckt(getTimestamp());
			em.merge(rechnung);
			em.flush();
			// in die Fibu uebernehmen
			getBelegbuchungFac(theClientDto.getMandant()).verbucheRechnung(iid,
					theClientDto);
		}
	}

	@Override
	public Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// int iNachkommaStellen = 2;

		// UW, 01.03.06 Der Druck ist nicht konsistent mit dem berechneten
		// Gesamtwert,
		// weil hier die Nachkommastellen abgeschnitten werden. Wir speichern
		// mit
		// maximaler Genauigkeit in die DB -> decimal(15,4)

		// PJ17873 Werbeabgabeartikel hinzufuegen
		int iNachkommaStellen = 4;

		CoinRoundingHelper rounder = new CoinRoundingHelper(this,
				getKundeFac(), getArtikelFac(), getParameterFac(),
				getMandantFac());

		ParametermandantDto parameterWerbeabgabe = getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL);

		RechnungDto rechnungDto = rechnungFindByPrimaryKey(iid);

		// wenn schon exportiert, nichts rechnen
		if (rechnungDto.getTFibuuebernahme() != null)
			return rechnungDto.getTFibuuebernahme();
		RechnungPositionDto[] rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
				.getIId());
		if (!STATUS_ANGELEGT.equals(rechnungDto.getStatusCNr()))
			return rechnungDto.getTAendern();

		String werbeabgabeArtikel = parameterWerbeabgabe.getCWert();
		if (werbeabgabeArtikel != null
				&& werbeabgabeArtikel.trim().length() > 0) {
			parameterWerbeabgabe = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WERBEABGABE_PROZENT);

			double dProzent = (Integer) parameterWerbeabgabe.getCWertAsObject();

			ArtikelDto aDtoWerbeabgabe = getArtikelFac()
					.artikelFindByCNrMandantCNrOhneExc(werbeabgabeArtikel,
							theClientDto.getMandant());
			if (aDtoWerbeabgabe != null) {

				ParametermandantDto parameterPosKont = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
				boolean isPositionskontierung = ((Boolean) parameterPosKont
						.getCWertAsObject()).booleanValue();

				ParametermandantDto parameterEinzelpreis = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_WERBEABGABE_VOM_EINZELPREIS);
				boolean werbeabgabeVomEinzelpreis = ((Boolean) parameterEinzelpreis
						.getCWertAsObject()).booleanValue();

				MwstsatzDto mwstsatzDtoAktuell = null;

				if (!isPositionskontierung) {
					// Aktuellen MWST-Satz uebersetzen.
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							rechnungDto.getKundeIId(), theClientDto);

					mwstsatzDtoAktuell = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									kundeDto.getMwstsatzbezIId(), theClientDto);

				} else {
					mwstsatzDtoAktuell = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									aDtoWerbeabgabe.getMwstsatzbezIId(),
									theClientDto);
				}

				// Vorhandenen Werbeabgabeartikel entfernen
				for (int i = 0; i < rePosDto.length; i++) {
					if (rePosDto[i].getArtikelIId() != null
							&& rePosDto[i].getArtikelIId().equals(
									aDtoWerbeabgabe.getIId())) {
						removeRechnungPosition(rePosDto[i], theClientDto);
					}
				}

				if (Helper.short2boolean(aDtoWerbeabgabe
						.getBLagerbewirtschaftet())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN,
							new Exception(
									"FEHLER_WERBEABGABEARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN: "
											+ werbeabgabeArtikel));
				}

				RechnungPositionDto reposWerbeabgabeDto = new RechnungPositionDto();
				reposWerbeabgabeDto.setArtikelIId(aDtoWerbeabgabe.getIId());
				reposWerbeabgabeDto
						.setRechnungpositionartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
				reposWerbeabgabeDto.setBelegIId(rechnungDto.getIId());
				reposWerbeabgabeDto.setNMenge(new BigDecimal(1));
				reposWerbeabgabeDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
				reposWerbeabgabeDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());

				// Wert der Werbeabgabepflichtigen Artikelberechnen
				BigDecimal bdWertDerAbgabepflichtigenArtikel = new BigDecimal(
						0.0000);
				for (int i = 0; i < rePosDto.length; i++) {
					if (rePosDto[i].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_IDENT)
							&& rePosDto[i].getArtikelIId() != null) {
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										rePosDto[i].getArtikelIId(),
										theClientDto);
						if (Helper.short2boolean(aDto
								.getBWerbeabgabepflichtig())) {

							if (werbeabgabeVomEinzelpreis) {
								bdWertDerAbgabepflichtigenArtikel = bdWertDerAbgabepflichtigenArtikel
										.add(rePosDto[i]
												.getNEinzelpreis()
												.multiply(
														rePosDto[i].getNMenge()));
							} else {
								bdWertDerAbgabepflichtigenArtikel = bdWertDerAbgabepflichtigenArtikel
										.add(rePosDto[i]
												.getNNettoeinzelpreisplusversteckteraufschlag()
												.multiply(
														rePosDto[i].getNMenge()));
							}

						}
					}
				}

				// Nur eintragen, wenn tatsaechlich Werbeabgabgpflichtige
				// Artikel vorhanden sind

				if (bdWertDerAbgabepflichtigenArtikel.doubleValue() != 0) {
					BigDecimal bdNettoeinzel = bdWertDerAbgabepflichtigenArtikel
							.multiply(new BigDecimal(dProzent / 100));
					reposWerbeabgabeDto
							.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(bdNettoeinzel);
					reposWerbeabgabeDto.setNNettoeinzelpreis(bdNettoeinzel);
					reposWerbeabgabeDto.setNEinzelpreis(bdNettoeinzel);
					reposWerbeabgabeDto.setFRabattsatz(0D);
					reposWerbeabgabeDto.setFZusatzrabattsatz(0D);
					reposWerbeabgabeDto.setBNettopreisuebersteuert(Helper
							.boolean2Short(false));

					BigDecimal mwstBetrag = reposWerbeabgabeDto
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
							.multiply(
									new BigDecimal(mwstsatzDtoAktuell
											.getFMwstsatz()).movePointLeft(2));

					reposWerbeabgabeDto
							.setNBruttoeinzelpreis(reposWerbeabgabeDto
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.add(mwstBetrag));

					createRechnungPosition(reposWerbeabgabeDto,
							rechnungDto.getLagerIId(), theClientDto);

					// Rechnungspositionen neu laden
					rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
							.getIId());
				}
			} else {
				// Werbeabgabeartikel nicht vorhanen
				ArrayList al = new ArrayList();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_NICHT_VORHANDEN,
						al, new Exception(
								"WERBEABGABEARTIKEL_NICHT_VORHANDEN: "
										+ werbeabgabeArtikel));
			}
		}

		/*
		 * Etwaigen Rundungsartikel entfernen. Es wird akzeptiert, dass
		 */
		if (rounder.removeRoundingPosition(rePosDto, theClientDto)) {
			rePosDto = rechnungPositionFindByRechnungIId(rechnungDto.getIId());
		}

		rechnungDto.setNWertfw(getRechnungswertInRechnungswaehrung(rechnungDto,
				rePosDto, theClientDto));
		// bei Reverse Charge ist die UST 0

		// Default Chrgennummernbehaftet
		ParametermandantDto parameter = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
				ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);

		boolean bReversecharge = (Boolean) parameter.getCWertAsObject();

		if (Helper.short2boolean(rechnungDto.getBReversecharge())
				&& bReversecharge) {
			rechnungDto.setNWertustfw(new BigDecimal(0));
		} else {
			rechnungDto.setNWertustfw(getRechnungswertInRechnungswaehrungUST(
					rechnungDto, rePosDto, theClientDto));
		}
		// beim aktivieren wird der aktuelle wechselkurs herangezogen
		BigDecimal fKurs = getLocaleFac().getWechselkurs2(
				theClientDto.getSMandantenwaehrung(),
				rechnungDto.getWaehrungCNr(), theClientDto);
		WechselkursDto wDto = getLocaleFac().getKursZuDatum(
				theClientDto.getSMandantenwaehrung(),
				rechnungDto.getWaehrungCNr(),
				new Date(rechnungDto.getTBelegdatum().getTime()), theClientDto);

		if (wDto != null) {
			fKurs = wDto.getNKurs();
		}

		// den Kurs auf 6 Stellen runden
		BigDecimal bdKurs = fKurs.setScale(
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);
		rechnungDto.setNKurs(bdKurs);
		BigDecimal bdWert = rechnungDto.getNWertfw().divide(
				rechnungDto.getNKurs(), iNachkommaStellen,
				BigDecimal.ROUND_HALF_EVEN);
		// der Rechnungs/Gutschriftswert darf nicht kleiner 0 sein
		// if (bdWert.compareTo(new BigDecimal(0)) < 0) {
		if (bdWert.signum() < 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_WERT_DARF_NICHT_NEGATIV_SEIN,
					new Exception("Wert der " + rechnungDto.getRechnungartCNr()
							+ " " + rechnungDto.getCNr()
							+ " darf nicht <0 sein"));
		}

		BigDecimal bdWertUst;
		// bei Reverse Charge ist die UST 0
		if (Helper.short2boolean(rechnungDto.getBReversecharge())) {
			bdWertUst = new BigDecimal(0);
		} else {
			bdWertUst = rechnungDto.getNWertustfw().divide(
					rechnungDto.getNKurs(), iNachkommaStellen,
					BigDecimal.ROUND_HALF_EVEN);
		}
		rechnungDto.setNWert(bdWert);
		rechnungDto.setNWertust(bdWertUst);
		rechnungDto.setNWert(Helper.rundeKaufmaennisch(rechnungDto.getNWert(),
				FinanzFac.NACHKOMMASTELLEN));
		rechnungDto.setNWertfw(Helper.rundeKaufmaennisch(
				rechnungDto.getNWertfw(), FinanzFac.NACHKOMMASTELLEN));
		rechnungDto.setNWertust(Helper.rundeKaufmaennisch(
				rechnungDto.getNWertust(), FinanzFac.NACHKOMMASTELLEN));
		rechnungDto.setNWertustfw(Helper.rundeKaufmaennisch(
				rechnungDto.getNWertustfw(), FinanzFac.NACHKOMMASTELLEN));

		for (RechnungPositionDto rechnungPositionDto : rePosDto) {
			if (RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME
					.equals(rechnungPositionDto.getPositionsartCNr())) {
				updateRechnungPosition(rechnungPositionDto, theClientDto);
			}
		}

		rounder.round(rechnungDto, rePosDto, theClientDto);

		// wenn Kopf- und Fusstext noch nicht befuellt wurden, dann jetzt
		// befuellen
		if (rechnungDto.getCKopftextuebersteuert() == null
				|| rechnungDto.getCFusstextuebersteuert() == null) {
			// Kunden holen (brauche das Locale)
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			// Kopftext
			if (rechnungDto.getCKopftextuebersteuert() == null) {

				RechnungartDto raDto = getRechnungServiceFac()
						.rechnungartFindByPrimaryKey(
								rechnungDto.getRechnungartCNr(), theClientDto);

				if (raDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					GutschrifttextDto kopftextDto = getRechnungServiceFac()
							.gutschrifttextFindByMandantLocaleCNr(
									rechnungDto.getMandantCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					// anlegen, falls noch nicht vorhanden
					if (kopftextDto == null) {
						kopftextDto = getRechnungServiceFac()
								.createDefaultGutschrifttext(
										MediaFac.MEDIAART_KOPFTEXT,
										RechnungServiceFac.GUTSCHRIFT_DEFAULT_KOPFTEXT,
										kundeDto.getPartnerDto()
												.getLocaleCNrKommunikation(),
										theClientDto);
					}
					rechnungDto.setCKopftextuebersteuert(kopftextDto
							.getCTextinhalt());
				} else if (raDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_RECHNUNG)) {

					RechnungtextDto kopftextDto = getRechnungServiceFac()
							.rechnungtextFindByMandantLocaleCNr(
									rechnungDto.getMandantCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					// anlegen, falls noch nicht vorhanden
					if (kopftextDto == null) {
						kopftextDto = getRechnungServiceFac()
								.createDefaultRechnungtext(
										MediaFac.MEDIAART_KOPFTEXT,
										RechnungServiceFac.RECHNUNG_DEFAULT_KOPFTEXT,
										kundeDto.getPartnerDto()
												.getLocaleCNrKommunikation(),
										theClientDto);
					}
					rechnungDto.setCKopftextuebersteuert(kopftextDto
							.getCTextinhalt());
				}
			}
			// Fusstext
			if (rechnungDto.getCFusstextuebersteuert() == null) {
				if (rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					GutschrifttextDto fusstextDto = getRechnungServiceFac()
							.gutschrifttextFindByMandantLocaleCNr(
									rechnungDto.getMandantCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					// anlegen, falls noch nicht vorhanden
					if (fusstextDto == null) {
						fusstextDto = getRechnungServiceFac()
								.createDefaultGutschrifttext(
										MediaFac.MEDIAART_FUSSTEXT,
										RechnungServiceFac.GUTSCHRIFT_DEFAULT_FUSSTEXT,
										kundeDto.getPartnerDto()
												.getLocaleCNrKommunikation(),
										theClientDto);
					}
					rechnungDto.setCFusstextuebersteuert(fusstextDto
							.getCTextinhalt());
				} else {
					RechnungtextDto fusstextDto = getRechnungServiceFac()
							.rechnungtextFindByMandantLocaleCNr(
									rechnungDto.getMandantCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					// anlegen, falls noch nicht vorhanden
					if (fusstextDto == null) {
						fusstextDto = getRechnungServiceFac()
								.createDefaultRechnungtext(
										MediaFac.MEDIAART_FUSSTEXT,
										RechnungServiceFac.RECHNUNG_DEFAULT_FUSSTEXT,
										kundeDto.getPartnerDto()
												.getLocaleCNrKommunikation(),
										theClientDto);
					}
					rechnungDto.setCFusstextuebersteuert(fusstextDto
							.getCTextinhalt());
				}

			}
		}

		// kein Update, wenn storniert
		if (!rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
			rechnungDto = updateRechnung(rechnungDto, theClientDto);
		}
		return rechnungDto.getTAendern();
	}

	/**
	 * Eine Rechnung/Gutschrift/Proformarechnung stornieren. Wirkt auch auf alle
	 * zugehoerigen Positionen. (bzgl Lagerbuchung, LS-Status, etc.)
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void storniereRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			if (zahlungFindByRechnungIId(rechnungIId).length > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN,
						"FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN");
			}

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungIId);

			// SP3205
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_ANZAHLUNG)) {
				// Wenn bereits eine Schlussrechnun,gibt kann die Anzahlgun
				// nicht storniert werden
				for (RechnungDto re : rechnungFindByAuftragIId(rechnungDto
						.getAuftragIId())) {
					if (re.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
							&& !re.getStatusCNr().equals(
									RechnungFac.STATUS_STORNIERT)) {

						ArrayList al = new ArrayList();
						al.add(re.getCNr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_STORNIEREN_ANZAHLUNG_SCHLUSSRECHNUNG_VORHANDEN,
								al,
								new Exception(
										"FEHLER_STORNIEREN_ANZAHLUNG_SCHLUSSRECHNUNG_VORHANDEN"));
					}
				}
			}

			pruefeAnzahlungSchlusszahlung(rechnungDto);
			rechnungDto.setStatusCNr(RechnungFac.STATUS_STORNIERT);
			// Die Werte null setzen
			rechnungDto.setNWert(null);
			rechnungDto.setNWertfw(null);
			rechnungDto.setNWertust(null);
			rechnungDto.setNWertustfw(null);
			rechnungDto
					.setTStorniert(new Timestamp(System.currentTimeMillis()));
			rechnungDto.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			updateRechnung(rechnungDto, theClientDto);
			RechnungPositionDto[] rechnungPositionDto = rechnungPositionFindByRechnungIId(rechnungIId);
			// Die Positionen
			for (int i = 0; i < rechnungPositionDto.length; i++) {
				// Lieferscheine werden wieder auf nicht verrechnet gesetzt
				if (rechnungPositionDto[i]
						.getRechnungpositionartCNr()
						.trim()
						.equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN
								.trim())) {
					MandantDto mandantDto = getMandantFac()
							.mandantFindByPrimaryKey(theClientDto.getMandant(),
									theClientDto);
					Integer mwstsatzIId = mandantDto
							.getMwstsatzbezIIdStandardinlandmwstsatz();
					Lieferschein ls = em.find(Lieferschein.class,
							rechnungPositionDto[i].getLieferscheinIId());
					Rechnungposition re = em.find(Rechnungposition.class,
							rechnungPositionDto[i].getIId());
					re.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE);
					re.setCBez(getTextRespectUISpr("lp.lieferschein",
							theClientDto.getSMandantenwaehrung(),
							theClientDto.getLocUi())
							+ " " + ls.getCNr());
					re.setLieferscheinIId(null);
					re.setNMenge(new BigDecimal(1));
					re.setEinheitCNr(SystemFac.EINHEIT_STUECK.trim());
					re.setFRabattsatz(new Double(0));
					re.setFZusatzrabattsatz(new Double(0));
					re.setNBruttoeinzelpreis(rechnungPositionDto[i]
							.getNEinzelpreis());
					re.setNEinzelpreis(rechnungPositionDto[i].getNEinzelpreis());
					re.setNEinzelpreisplusaufschlag(rechnungPositionDto[i]
							.getNEinzelpreis());
					re.setNNettoeinzelpreisplusaufschlag(rechnungPositionDto[i]
							.getNEinzelpreis());
					re.setNNettoeinzelpreisplusaufschlagminusrabatt(rechnungPositionDto[i]
							.getNEinzelpreis());
					re.setMwstsatzIId(mwstsatzIId);

					// einen Handartikel anlegen
					ArtikelDto artikelDto = new ArtikelDto();
					artikelDto
							.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
					ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
					oArtikelsprDto.setCBez(re.getCBez());
					oArtikelsprDto.setCZbez(re.getCZbez());
					artikelDto.setArtikelsprDto(oArtikelsprDto);
					artikelDto.setEinheitCNr(re.getEinheitCNr());

					// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(mwstsatzIId, theClientDto);
					artikelDto.setMwstsatzbezIId(mwstsatzDto
							.getIIMwstsatzbezId());
					// Artikel anlegen.
					Integer iIdArtikel = getArtikelFac().createArtikel(
							artikelDto, theClientDto);
					re.setArtikelIId(iIdArtikel);

					try {
						getLieferscheinFac().setzeStatusLieferschein(
								rechnungPositionDto[i].getLieferscheinIId(),
								LieferscheinFac.LSSTATUS_GELIEFERT, null,
								theClientDto);
					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
				}
				// fuer Artikel wird die Lagerbuchung rueckgaengig gemacht
				else if (rechnungPositionDto[i].getRechnungpositionartCNr()
						.trim()
						.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT.trim())) {
					bucheRechnungPositionAmLager(rechnungPositionDto[i],
							rechnungDto.getLagerIId(), true, theClientDto);
				}
			}
			// aus der Fibu ausbuchen
			String rechnungtyp = null;
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			rechnungtyp = rechnungartDto.getRechnungtypCNr();
			if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT))
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheGutschriftRueckgaengig(rechnungIId,
								theClientDto);
			else
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheRechnungRueckgaengig(rechnungIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Das Storno Rechnung/Gutschrift/Proformarechnung aufheben Wirkt auch auf
	 * alle zugehoerigen Positionen. (bzgl Lagerbuchung, LS-Status, etc.)
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void storniereRechnungRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungIId);
			rechnungDto.setStatusCNr(RechnungFac.STATUS_ANGELEGT);
			rechnungDto.setTStorniert(null);
			rechnungDto.setPersonalIIdStorniert(null);
			updateRechnung(rechnungDto, theClientDto);
			RechnungPositionDto[] rechnungPositionDto = rechnungPositionFindByRechnungIId(rechnungIId);
			// Die Positionen
			for (int i = 0; i < rechnungPositionDto.length; i++) {
				// Lieferscheine werden wieder auf verrechnet gesetzt
				if (rechnungPositionDto[i]
						.getRechnungpositionartCNr()
						.trim()
						.equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN
								.trim())) {
					try {
						getLieferscheinFac().setzeStatusLieferschein(
								rechnungPositionDto[i].getLieferscheinIId(),
								LieferscheinFac.LSSTATUS_VERRECHNET,
								rechnungDto.getIId(), theClientDto);
					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
				}
				// fuer Artikel wird die Lagerbuchung wieder durchgefuehrt
				else if (rechnungPositionDto[i].getRechnungpositionartCNr()
						.trim()
						.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT.trim())) {
					bucheRechnungPositionAmLager(rechnungPositionDto[i],
							rechnungDto.getLagerIId(), false, theClientDto);
				}
			}

			// in die Fibu uebernehmen
			if (!rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)
					&& !rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT))
				getBelegbuchungFac(theClientDto.getMandant()).verbucheRechnung(
						rechnungIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public BelegpositionVerkaufDto[] getAll(
			RechnungPositionDto[] allePositionen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<BelegpositionVerkaufDto> list = new ArrayList<BelegpositionVerkaufDto>();
		try {
			for (int i = 0; i < allePositionen.length; i++) {
				if (allePositionen[i].getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
					Collection<LieferscheinpositionDto> lspos = getLieferscheinpositionFac()
							.lieferscheinpositionFindByLieferscheinIId(
									allePositionen[i].getLieferscheinIId(),
									theClientDto);
					list.addAll(lspos);

				} else {
					list.add(allePositionen[i]);
				}
			}
		} catch (RemoteException e) {
		}
		BelegpositionVerkaufDto[] returnArray = new BelegpositionVerkaufDto[list
				.size()];
		return (BelegpositionVerkaufDto[]) list.toArray(returnArray);
	}

	/**
	 * Berechnung des Werts einer Rechnung. <br>
	 * Der Rechnungswert ist die Summe ueber die Nettopreise der enthaltenen
	 * Positionen unter Beruecksichtigung der Zu- und Abschlaege, die in den
	 * Konditionen der Rechnung hinterlegt sind.
	 * 
	 * @param rechnungDto
	 *            Integer
	 * @param allePositionen
	 *            RechnungPositionDto[]
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getRechnungswertInRechnungswaehrung(
			RechnungDto rechnungDto, RechnungPositionDto[] allePositionen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal rechnungswert = new BigDecimal(0);
		BelegpositionVerkaufDto[] alle = getAll(allePositionen, theClientDto);
		rechnungswert = getBelegVerkaufFac().getGesamtwertinBelegwaehrung(alle,
				rechnungDto);
		return rechnungswert;
	}

	/**
	 * Berechnung des Werts einer Rechnung. <br>
	 * Der Rechnungswert ist die Summe ueber die Nettopreise der enthaltenen
	 * Positionen unter Beruecksichtigung der Zu- und Abschlaege, die in den
	 * Konditionen der Rechnung hinterlegt sind.
	 * 
	 * @param rechnungDto
	 *            Integer
	 * @param allePositionen
	 *            RechnungPositionDto[]
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */
	private BigDecimal getRechnungswertInRechnungswaehrungUST(
			RechnungDto rechnungDto, RechnungPositionDto[] allePositionen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal ustwert = new BigDecimal(0);
		BelegpositionVerkaufDto[] alle = getAll(allePositionen, theClientDto);
		ustwert = getBelegVerkaufFac().getGesamtwertInBelegswaehrungUST(alle,
				rechnungDto, theClientDto);
		return ustwert.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void fuehreArtikelZusammen(Integer artikelIIdAlt,
			Integer artikelIIdNeu, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungPositionDto[] positionen = rechnungPositionFindByArtikelIId(artikelIIdAlt);
		for (int i = 0; i < positionen.length; i++) {
			positionen[i].setArtikelIId(artikelIIdNeu);
			updateRechnungPosition(positionen[i], theClientDto);
		}
	}

	public BigDecimal berechneSummeOffenBrutto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		return berechneSummeOffen(mandantCNr, sKriterium, gcVon, gcBis, null,
				true, theClientDto);
	}

	public BigDecimal berechneSummeOffenNetto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, Integer kundeIId,
			boolean bStatistikadresse, TheClientDto theClientDto) {
		return berechneSummeOffen(mandantCNr, sKriterium, gcVon, gcBis,
				kundeIId, false, theClientDto);
	}

	private BigDecimal berechneSummeOffen(String mandantCNr, String sKriterium,
			GregorianCalendar gcVon, GregorianCalendar gcBis, Integer kundeIId,
			boolean brutto, TheClientDto theClientDto) {
		BigDecimal wert = BigDecimal.ZERO;
		BigDecimal wertUst = BigDecimal.ZERO;

		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
		if (sKriterium.equals(RechnungFac.KRIT_MIT_GUTSCHRIFTEN))
			cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);

		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati,
				kundeIId, false);
		for (int i = 0; i < r.length; i++) {
			if (r[i].getFlrrechnungart().getRechnungtyp_c_nr()
					.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {

				wert = wert.add(r[i].getN_wert());
				wertUst = wertUst.add(r[i].getN_wertust());
				wert = wert.subtract(getBereitsBezahltWertVonRechnung(
						r[i].getI_id(), null));
				wertUst = wertUst.subtract(getBereitsBezahltWertVonRechnungUst(
						r[i].getI_id(), null));

				// Anzahlungen abziehen
				if (r[i].getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
						&& r[i].getFlrauftrag() != null) {

					try {
						RechnungDto[] anzRechnungen = getRechnungFac()
								.rechnungFindByAuftragIId(
										r[i].getFlrauftrag().getI_id());

						for (int k = 0; k < anzRechnungen.length; k++) {
							if (anzRechnungen[k].getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_ANZAHLUNG)
									&& anzRechnungen[k].getNWert() != null) {

								wert = wert.subtract(anzRechnungen[k]
										.getNWert());
								wertUst = wertUst.subtract(anzRechnungen[k]
										.getNWertust());

							}
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			} else if (r[i].getFlrrechnungart().getRechnungtyp_c_nr()
					.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				wert = wert.subtract(r[i].getN_wert());
				wertUst = wertUst.subtract(r[i].getN_wertust());
			}
		}
		return brutto ? wert.add(wertUst) : wert;
	}

	public BigDecimal berechneSummeUmsatzBrutto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		return berechneSummeUmsatz(mandantCNr, sKriterium, gcVon, gcBis, true,
				theClientDto);
	}

	public BigDecimal berechneSummeUmsatzNetto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		return berechneSummeUmsatz(mandantCNr, sKriterium, gcVon, gcBis, false,
				theClientDto);
	}

	private BigDecimal berechneSummeUmsatz(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean brutto, TheClientDto theClientDto) {
		BigDecimal wert = BigDecimal.ZERO;
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
		if (sKriterium.equals(KRIT_MIT_GUTSCHRIFTEN))
			cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {
			if (r[i].getFlrrechnungart().getRechnungtyp_c_nr()
					.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				wert = wert.add(r[i].getN_wert());
				if (brutto)
					wert = wert.add(r[i].getN_wertust());
			} else if (r[i].getFlrrechnungart().getRechnungtyp_c_nr()
					.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				wert = wert.subtract((r[i].getN_wert()));
				if (brutto)
					wert = wert.subtract(r[i].getN_wertust());
			}
		}
		return wert;
	}

	public BigDecimal berechneSummeAnzahlungBrutto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bNurNichtabgerechnete,
			TheClientDto theClientDto) {
		return berechneSummeAnzahlung(mandantCNr, sKriterium, gcVon, gcBis,
				bNurNichtabgerechnete, true, theClientDto);
	}

	public BigDecimal berechneSummeAnzahlungNetto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bNurNichtabgerechnete,
			TheClientDto theClientDto) {
		return berechneSummeAnzahlung(mandantCNr, sKriterium, gcVon, gcBis,
				bNurNichtabgerechnete, false, theClientDto);
	}

	private BigDecimal berechneSummeAnzahlung(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bNurNichtabgerechnete,
			boolean brutto, TheClientDto theClientDto) {
		BigDecimal wert = BigDecimal.ZERO;
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {

			if (r[i].getFlrrechnungart().getC_nr()
					.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {

				if (bNurNichtabgerechnete && r[i].getFlrauftrag() != null) {
					try {
						RechnungDto[] anzRechnungen = getRechnungFac()
								.rechnungFindByAuftragIId(
										r[i].getFlrauftrag().getI_id());
						boolean bSchlusszahlungGefunden = false;
						for (int k = 0; k < anzRechnungen.length; k++) {
							if (anzRechnungen[k].getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
								bSchlusszahlungGefunden = true;
								break;
							}
						}
						if (bSchlusszahlungGefunden) {
							continue;
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				wert = wert.add(r[i].getN_wert());
				if (brutto)
					wert = wert.add(r[i].getN_wertust());
			}
		}
		return wert;
	}

	public BigDecimal getUmsatzVomKundenImZeitraum(TheClientDto theClientDto,
			Integer kundeIId, Date dVon, Date dBis, boolean bStatistikadresse)
			throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungReport.class);
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));

			// Projekt 3568

			String[] stati = new String[4];
			stati[0] = RechnungFac.RECHNUNGART_RECHNUNG;
			stati[1] = RechnungFac.RECHNUNGART_GUTSCHRIFT;
			stati[2] = RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;
			stati[3] = RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG;

			c.createAlias(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra");

			c.add(Restrictions.in("ra.c_nr", stati));

			if (bStatistikadresse) {

				if (kundeIId != null) {
					c.add(Restrictions
							.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE,
									kundeIId));
				}
			} else {
				if (kundeIId != null) {
					c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID,
							kundeIId));
				}

			}
			if (dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dVon));
			}
			if (dBis != null) {
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dBis));
			}
			// Filter nach Status
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(RechnungFac.STATUS_BEZAHLT);
			cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
			cStati.add(RechnungFac.STATUS_OFFEN);
			cStati.add(RechnungFac.STATUS_VERBUCHT);
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));
			List<?> list = c.list();
			BigDecimal bdUmsatz = new BigDecimal(0);
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRRechnungReport item = (FLRRechnungReport) iter.next();
				if (item.getN_wert() != null) {

					System.out.println(item.getC_nr() + " RA:"
							+ item.getFlrrechnungart().getC_nr() + " W:"
							+ item.getN_wert());

					if (item.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
							|| item.getFlrrechnungart()
									.getC_nr()
									.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
						BigDecimal wert = new BigDecimal(0).subtract(item
								.getN_wert());
						bdUmsatz = bdUmsatz.add(wert);
					} else {
						bdUmsatz = bdUmsatz.add(item.getN_wert());
					}

				}
			}
			return bdUmsatz;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public BigDecimal getUmsatzVomKundenHeuer(TheClientDto theClientDto,
			Integer kundeIId, boolean bStatistikadresse) throws EJBExceptionLP {
		BigDecimal bdUmsatz = null;
		GregorianCalendar gcVon = new GregorianCalendar();
		GregorianCalendar gcBis = new GregorianCalendar();
		// vom 1.1.xx, 0 Uhr
		gcVon.set(Calendar.DATE, 1);
		gcVon.set(Calendar.MONTH, 0);
		Date dVon = new Date(Helper.cutCalendar(gcVon).getTime().getTime());
		// bis 1.1.xx+1, 0 Uhr
		gcBis.set(Calendar.DATE, 1);
		gcBis.set(Calendar.MONTH, 0);
		gcBis.set(Calendar.YEAR, gcBis.get(Calendar.YEAR) + 1);
		Date dBis = new Date(Helper.cutCalendar(gcBis).getTime().getTime());
		// berechnen
		bdUmsatz = getUmsatzVomKundenImZeitraum(theClientDto, kundeIId, dVon,
				dBis, bStatistikadresse);
		return bdUmsatz;
	}

	public BigDecimal getUmsatzVomKundenVorjahr(TheClientDto theClientDto,
			Integer kundeIId, boolean bStatistikadresse) throws EJBExceptionLP {
		BigDecimal bdUmsatz = null;
		GregorianCalendar gcVon = new GregorianCalendar();
		GregorianCalendar gcBis = new GregorianCalendar();
		// vom 1.1.xx-1, 0 Uhr
		gcVon.set(Calendar.DATE, 1);
		gcVon.set(Calendar.MONTH, 0);
		gcVon.set(Calendar.YEAR, gcBis.get(Calendar.YEAR) - 1);
		Date dVon = new Date(Helper.cutCalendar(gcVon).getTime().getTime());
		// bis 1.1.xx, 0 Uhr
		gcBis.set(Calendar.DATE, 1);
		gcBis.set(Calendar.MONTH, 0);
		Date dBis = new Date(Helper.cutCalendar(gcBis).getTime().getTime());
		// berechnen
		bdUmsatz = getUmsatzVomKundenImZeitraum(theClientDto, kundeIId, dVon,
				dBis, bStatistikadresse);
		return bdUmsatz;
	}

	/**
	 * Die Anzahl der an einen Kunden gelegten Rechnungen in eimem zeitraum
	 * ermitteln. geht ueber hibernate (der performace wegen).
	 * 
	 * @param theClientDto
	 *            String
	 * @param kundeIId
	 *            Integer
	 * @param dVon
	 *            Date
	 * @param dBis
	 *            Date
	 * @param bStatistikadresse
	 *            boolean
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getAnzahlDerRechnungenVomKundenImZeitraum(
			TheClientDto theClientDto, Integer kundeIId, Date dVon, Date dBis,
			boolean bStatistikadresse) throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnung.class);
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.eq(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							RechnungFac.RECHNUNGTYP_RECHNUNG));
			if (kundeIId != null) {
				if (bStatistikadresse) {
					c.add(Restrictions
							.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE,
									kundeIId));
				} else {
					c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE).add(
							Restrictions.eq(KundeFac.FLR_KUNDE_I_ID, kundeIId));
				}
			}
			if (dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dVon));
			}
			if (dBis != null) {
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dBis));
			}
			// Filter nach Status
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(RechnungFac.STATUS_BEZAHLT);
			cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
			cStati.add(RechnungFac.STATUS_OFFEN);
			cStati.add(RechnungFac.STATUS_VERBUCHT);
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));
			List<?> list = c.list();
			return new Integer(list.size());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Integer getZahlungsmoraleinesKunden(Integer kundeIId,
			boolean bStatistikadresse, TheClientDto theClientDto) {
		Session session = null;

		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();

		Criteria c = session.createCriteria(FLRRechnung.class);
		c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
				theClientDto.getMandant()));
		c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
				Restrictions.eq(RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
						RechnungFac.RECHNUNGTYP_RECHNUNG));
		if (kundeIId != null) {
			if (bStatistikadresse) {
				c.add(Restrictions.eq(
						RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE,
						kundeIId));
			} else {
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE).add(
						Restrictions.eq(KundeFac.FLR_KUNDE_I_ID, kundeIId));
			}
		}

		c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
				new java.sql.Date(System.currentTimeMillis())));

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_ZAHLUNGSMORAL_MONATE);
			Integer iAnzalMonate = (Integer) parameter.getCWertAsObject();

			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.MONTH,
					cal.get(Calendar.MONTH) - iAnzalMonate.intValue());

			c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
					new java.sql.Date(cal.getTimeInMillis())));

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Filter nach Status
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);

		c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));
		c.addOrder(Order.desc("c_nr"));
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_ZAHLUNGSMORAL_ANZAHL_RECHNUNGEN);
			Integer iAnzalRechnungen = (Integer) parameter.getCWertAsObject();
			c.setMaxResults(iAnzalRechnungen);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		List<?> list = c.list();

		if (list.size() == 0) {
			return 0;
		}
		Iterator<?> iter = list.iterator();
		int iZaehler = 0;
		int iGesamtTage = 0;

		while (iter.hasNext()) {
			FLRRechnung rech = (FLRRechnung) iter.next();

			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rech.getI_id());
			if (rechnungDto.getTBezahltdatum() != null) {
				iGesamtTage += Helper.ermittleTageEinesZeitraumes(
						rechnungDto.getTBelegdatum(),
						rechnungDto.getTBezahltdatum());

				iZaehler++;
			}
		}

		if (iZaehler != 0) {
			return iGesamtTage / iZaehler;
		} else {
			return 0;
		}

	}

	public Integer getAnzahlDerRechnungenVomKundenHeuer(
			TheClientDto theClientDto, Integer kundeIId,
			boolean bStatistikadresse) throws EJBExceptionLP {
		Integer iAnzahl = null;
		GregorianCalendar gcVon = new GregorianCalendar();
		GregorianCalendar gcBis = new GregorianCalendar();
		// vom 1.1.xx, 0 Uhr
		gcVon.set(Calendar.DATE, 1);
		gcVon.set(Calendar.MONTH, 0);
		Date dVon = new Date(Helper.cutCalendar(gcVon).getTime().getTime());
		// bis 1.1.xx+1, 0 Uhr
		gcBis.set(Calendar.DATE, 1);
		gcBis.set(Calendar.MONTH, 0);
		gcBis.set(Calendar.YEAR, gcBis.get(Calendar.YEAR) + 1);
		Date dBis = new Date(Helper.cutCalendar(gcBis).getTime().getTime());
		// berechnen
		iAnzahl = getAnzahlDerRechnungenVomKundenImZeitraum(theClientDto,
				kundeIId, dVon, dBis, bStatistikadresse);
		return iAnzahl;
	}

	public Integer getAnzahlDerRechnungenVomKundenVorjahr(
			TheClientDto theClientDto, Integer kundeIId,
			boolean bStatistikadresse) throws EJBExceptionLP {
		Integer iAnzahl = null;
		GregorianCalendar gcVon = new GregorianCalendar();
		GregorianCalendar gcBis = new GregorianCalendar();
		// vom 1.1.xx-1, 0 Uhr
		gcVon.set(Calendar.DATE, 1);
		gcVon.set(Calendar.MONTH, 0);
		gcVon.set(Calendar.YEAR, gcBis.get(Calendar.YEAR) - 1);
		Date dVon = new Date(Helper.cutCalendar(gcVon).getTime().getTime());
		// bis 1.1.xx, 0 Uhr
		gcBis.set(Calendar.DATE, 1);
		gcBis.set(Calendar.MONTH, 0);
		Date dBis = new Date(Helper.cutCalendar(gcBis).getTime().getTime());
		// berechnen
		iAnzahl = getAnzahlDerRechnungenVomKundenImZeitraum(theClientDto,
				kundeIId, dVon, dBis, bStatistikadresse);
		return iAnzahl;
	}

	public void bucheRechnungPositionAmLager(
			RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			boolean bLoescheBuchung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungPositionDto
					.getRechnungIId());
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							rechnungPositionDto.getArtikelIId(), theClientDto);
			// Wenn die Position geloescht wird und die Menge eh 0 ist, dann
			// brauch ich gar nix tun.
			if (!(bLoescheBuchung && rechnungPositionDto.getNMenge().compareTo(
					new BigDecimal(0)) == 0)) {
				// gebucht werden nur Positionen mit Menge > 0
				if (bLoescheBuchung
						|| rechnungPositionDto.getNMenge().compareTo(
								new BigDecimal(0)) > 0) {
					if (bLoescheBuchung) {
						rechnungPositionDto.setNMenge(new BigDecimal(0));
					}
					// Rechnung
					if (rechnungartDto.getRechnungtypCNr().equals(
							RechnungFac.RECHNUNGTYP_RECHNUNG)) {
						// Chargennummerntragende.

						getLagerFac()
								.bucheAb(
										LocaleFac.BELEGART_RECHNUNG,
										rechnungPositionDto.getRechnungIId(),
										rechnungPositionDto.getIId(),
										rechnungPositionDto.getArtikelIId(),
										rechnungPositionDto.getNMenge(),
										rechnungPositionDto
												.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
										lagerIId,
										rechnungPositionDto
												.getSeriennrChargennrMitMenge(),
										new Timestamp(rechnungDto
												.getTBelegdatum().getTime()),
										theClientDto);

					}
					// Gutschrift
					else if (rechnungartDto.getRechnungtypCNr().equals(
							RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
						if (rechnungDto.getRechnungartCNr().equals(
								RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
							LagerDto lagerDto = getLagerFac()
									.lagerFindByMandantCNrLagerartCNr(
											rechnungDto.getMandantCNr(),
											LagerFac.LAGERART_WERTGUTSCHRIFT);
							lagerIId = lagerDto.getIId();

							BigDecimal bdGestehungspreis = null;
							;
							if (rechnungDto.getRechnungIIdZurechnung() != null) {
								RechnungPositionDto[] reDtos = getRechnungFac()
										.rechnungPositionFindByRechnungIIdArtikelIId(
												rechnungDto
														.getRechnungIIdZurechnung(),
												rechnungPositionDto
														.getArtikelIId());
								if (reDtos != null && reDtos.length > 0) {
									bdGestehungspreis = getLagerFac()
											.getGemittelterGestehungspreisEinerAbgangsposition(
													LocaleFac.BELEGART_RECHNUNG,
													reDtos[0].getIId());
								}
							} else {
								bdGestehungspreis = getLagerFac()
										.getGemittelterGestehungspreisEinesLagers(
												rechnungPositionDto
														.getArtikelIId(),
												lagerIId, theClientDto);
							}

							if (bdGestehungspreis == null)
								bdGestehungspreis = rechnungPositionDto
										.getNNettoeinzelpreis();

							getLagerFac().bucheZu(
									LocaleFac.BELEGART_GUTSCHRIFT,
									rechnungPositionDto.getRechnungIId(),
									rechnungPositionDto.getIId(),
									rechnungPositionDto.getArtikelIId(),
									rechnungPositionDto.getNMenge(),
									bdGestehungspreis,
									lagerIId,
									rechnungPositionDto
											.getSeriennrChargennrMitMenge(),
									new Timestamp(rechnungDto.getTBelegdatum()
											.getTime()), theClientDto);

						} else {
							// PJ 08/13825
							BigDecimal bdGestehungspreis = null;
							;
							if (rechnungDto.getRechnungIIdZurechnung() != null) {
								RechnungPositionDto[] reDtos = getRechnungFac()
										.rechnungPositionFindByRechnungIIdArtikelIId(
												rechnungDto
														.getRechnungIIdZurechnung(),
												rechnungPositionDto
														.getArtikelIId());
								if (reDtos != null && reDtos.length > 0) {
									bdGestehungspreis = getLagerFac()
											.getGemittelterGestehungspreisEinerAbgangsposition(
													LocaleFac.BELEGART_RECHNUNG,
													reDtos[0].getIId());
								}
							} else {
								bdGestehungspreis = getLagerFac()
										.getGemittelterGestehungspreisEinesLagers(
												rechnungPositionDto
														.getArtikelIId(),
												lagerIId, theClientDto);
							}

							if (bdGestehungspreis == null)
								bdGestehungspreis = rechnungPositionDto
										.getNNettoeinzelpreis();

							getLagerFac().bucheZu(
									LocaleFac.BELEGART_GUTSCHRIFT,
									rechnungPositionDto.getRechnungIId(),
									rechnungPositionDto.getIId(),
									rechnungPositionDto.getArtikelIId(),
									rechnungPositionDto.getNMenge(),
									bdGestehungspreis,
									lagerIId,
									rechnungPositionDto
											.getSeriennrChargennrMitMenge(),
									new Timestamp(rechnungDto.getTBelegdatum()
											.getTime()), theClientDto);

						}
					}
					// die Proformarechnung bucht nicht
					else if (rechnungartDto.getRechnungtypCNr().equals(
							RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
						// nothing here
					}
				}
			}
		} catch (RemoteException ex) {
			// falls die zugebuchte menge schon verbraucht wurde, muss ich das
			// extra behandeln
			Throwable eCause = ex.getCause();
			if (eCause instanceof EJBExceptionLP) {
				EJBExceptionLP e = (EJBExceptionLP) eCause;
				if (e.getCode() == EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN,
							new Exception(
									"FEHLER_FERTIGUNG_ABLIEFERUNG_BEREITS_VOM_LAGER_ENTNOMMEN"));
				}
			}
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public BigDecimal berechneSummeGutschriftOffenBrutto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		BigDecimal wert = new BigDecimal(0);
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {
			wert = wert.add(r[i].getN_wert());
			wert = wert.add(r[i].getN_wertust());
		}
		return wert;
	}

	public BigDecimal berechneSummeGutschriftOffenNetto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		BigDecimal wert = new BigDecimal(0);
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {
			wert = wert.add(r[i].getN_wert());
		}
		return wert;
	}

	public BigDecimal berechneSummeGutschriftUmsatzBrutto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		BigDecimal wert = new BigDecimal(0);
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {
			wert = wert.add(r[i].getN_wert());
			wert = wert.add(r[i].getN_wertust());
		}
		return wert;
	}

	public BigDecimal berechneSummeGutschriftUmsatzNetto(String mandantCNr,
			String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, TheClientDto theClientDto) {
		BigDecimal wert = new BigDecimal(0);
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr,
				new java.sql.Date(gcVon.getTimeInMillis()), new java.sql.Date(
						gcBis.getTimeInMillis()), cRechnungstyp, cStati, null,
				false);
		for (int i = 0; i < r.length; i++) {
			wert = wert.add(r[i].getN_wert());
		}
		return wert;
	}

	public FLRRechnungReport[] rechnungFindByBelegdatumVonBis(
			String mandantCNr, Date dVon, Date dBis,
			Collection<String> cRechnungstyp, Collection<String> cStati,
			Integer kundeIId, boolean bStatistikadresse) {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungReport.class);
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					mandantCNr));
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));
			if (dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dVon));
			}
			if (dBis != null) {
				c.add(Restrictions.lt(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dBis));
			}

			if (kundeIId != null) {
				if (bStatistikadresse) {

					c.add(Restrictions
							.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE,
									kundeIId));
				} else {
					c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID,
							kundeIId));
				}
			}

			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.in(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							cRechnungstyp));
			List<?> list = c.list();
			if (list.isEmpty()) {
				return new FLRRechnungReport[0];
			} else {
				int i = 0;
				FLRRechnungReport[] re = new FLRRechnungReport[list.size()];
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					re[i++] = (FLRRechnungReport) iter.next();
				}
				return re;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void setzeRechnungFibuUebernahme(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("rechnungIId=" + rechnungIId, theClientDto.getIDUser());
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		rechnung.setTFibuuebernahme(getTimestamp());
	}

	public void setzeRechnungFibuUebernahmeRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("rechnungIId=" + rechnungIId, theClientDto.getIDUser());
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		rechnung.setTFibuuebernahme(null);
	}

	public void setzeRechnungKonditionenLSPositionen(RechnungDto rechnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		RechnungPositionDto[] pos = rechnungPositionFindByRechnungIId(rechnungDto
				.getIId());
		for (int i = 0; i < pos.length; i++) {
			if (pos[i].getRechnungpositionartCNr().equals(
					RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
				Lieferschein ls = em.find(Lieferschein.class,
						pos[i].getLieferscheinIId());
				ls.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
				if (rechnungDto.getFAllgemeinerRabattsatz() != null)
					ls.setFAllgemeinerrabatt(rechnungDto
							.getFAllgemeinerRabattsatz());
				if (rechnungDto.getFVersteckterAufschlag() != null)
					ls.setFVersteckteraufschlag(rechnungDto
							.getFVersteckterAufschlag());
				ls.setNGesamtwertinlieferscheinwaehrung(null);
				em.merge(ls);
				try {
					// LieferscheinDto lieferscheinDto =
					// getLieferscheinFac().lieferscheinFindByPrimaryKey
					// (ls.getIId(), theClientDto);
					// getLieferscheinFac().updateLieferschein(lieferscheinDto,
					// theClientDto);
					getLieferscheinFac().updateLieferscheinKonditionen(
							ls.getIId(), theClientDto);
				} catch (RemoteException e) {
				}
				ls.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_VERRECHNET);
				em.merge(ls);
			} else {
				befuelleZusaetzlichePreisfelder(pos[i].getIId(), theClientDto);
			}
		}
	}

	/*
	 * public BigDecimal berechneEinzelpreisLSPosition(RechnungDto rechnungDto,
	 * LieferscheinDto lieferscheinDto, LieferscheinpositionDto lsPosDto,
	 * boolean bInklRabatt, TheClientDto theClientDto) throws EJBExceptionLP {
	 * BigDecimal bdEinzelpreis = lsPosDto
	 * .getNEinzelpreisplusversteckteraufschlag(); // vom Einzelpreis muss man
	 * noch den allg.rabatt aus dem LS wegrechnen double dAllgRabattLS; if
	 * (lieferscheinDto.getFAllgemeinerRabattsatz() != null) { dAllgRabattLS =
	 * lieferscheinDto.getFAllgemeinerRabattsatz() .doubleValue(); } else {
	 * dAllgRabattLS = 0.0; } // Allg. Rabattwert der Position BigDecimal
	 * bdAllgRabattLS = Helper.getProzentWert(bdEinzelpreis, new
	 * Float(dAllgRabattLS), 2); // subtrahieren bdEinzelpreis =
	 * bdEinzelpreis.subtract(bdAllgRabattLS); // Rabatt der Position double
	 * dRabattLSPosition = 0.0; double dZusatzRabattLSPosition = 0.0; if
	 * (bInklRabatt) { if (lsPosDto.getFRabattsatz() != null) {
	 * dRabattLSPosition = lsPosDto.getFRabattsatz().doubleValue(); //
	 * Rabattwert der Position BigDecimal bdRabattLSPosition =
	 * Helper.getProzentWert( bdEinzelpreis, new Float(dRabattLSPosition), 2);
	 * bdEinzelpreis = bdEinzelpreis.subtract(bdRabattLSPosition); } if
	 * (lsPosDto.getFZusatzrabattsatz() != null) { dZusatzRabattLSPosition =
	 * lsPosDto.getFZusatzrabattsatz() .doubleValue(); // Zusatzrabattwert der
	 * Position BigDecimal bdZusatzRabattLSPosition = Helper.getProzentWert(
	 * bdEinzelpreis, new Float(dZusatzRabattLSPosition), 2); bdEinzelpreis =
	 * bdEinzelpreis .subtract(bdZusatzRabattLSPosition); } } return
	 * bdEinzelpreis; }
	 * 
	 * public BigDecimal berechneEinzelpreisLSPositionOhneLSRabatt(RechnungDto
	 * rechnungDto, LieferscheinDto lieferscheinDto, LieferscheinpositionDto
	 * lsPosDto, boolean bInklRabatt, TheClientDto theClientDto) throws
	 * EJBExceptionLP { BigDecimal bdEinzelpreis = lsPosDto
	 * .getNEinzelpreisplusversteckteraufschlag(); // Rabatt der Position double
	 * dRabattLSPosition = 0.0; double dZusatzRabattLSPosition = 0.0; if
	 * (bInklRabatt) { if (lsPosDto.getFRabattsatz() != null) {
	 * dRabattLSPosition = lsPosDto.getFRabattsatz().doubleValue(); //
	 * Rabattwert der Position BigDecimal bdRabattLSPosition =
	 * Helper.getProzentWert( bdEinzelpreis, new Float(dRabattLSPosition), 2);
	 * bdEinzelpreis = bdEinzelpreis.subtract(bdRabattLSPosition); } if
	 * (lsPosDto.getFZusatzrabattsatz() != null) { dZusatzRabattLSPosition =
	 * lsPosDto.getFZusatzrabattsatz() .doubleValue(); // Zusatzrabattwert der
	 * Position BigDecimal bdZusatzRabattLSPosition = Helper.getProzentWert(
	 * bdEinzelpreis, new Float(dZusatzRabattLSPosition), 2); bdEinzelpreis =
	 * bdEinzelpreis .subtract(bdZusatzRabattLSPosition); } } return
	 * bdEinzelpreis; }
	 */
	/*
	 * public BigDecimal berechneNettopreisLSPosition(RechnungDto rechnungDto,
	 * LieferscheinDto lieferscheinDto, LieferscheinpositionDto lsPosDto,
	 * TheClientDto theClientDto) throws EJBExceptionLP { BigDecimal
	 * bdEinzelpreisInklRabatt = berechneEinzelpreisLSPosition( rechnungDto,
	 * lieferscheinDto, lsPosDto, true, theClientDto); return
	 * bdEinzelpreisInklRabatt; }
	 * 
	 * public BigDecimal berechneNettopreisLSPositionOhneLSRabatt(RechnungDto
	 * rechnungDto, LieferscheinDto lieferscheinDto, LieferscheinpositionDto
	 * lsPosDto, TheClientDto theClientDto) throws EJBExceptionLP { BigDecimal
	 * bdEinzelpreisInklRabatt = berechneEinzelpreisLSPositionOhneLSRabatt(
	 * rechnungDto, lieferscheinDto, lsPosDto, true, theClientDto); return
	 * bdEinzelpreisInklRabatt; }
	 */
	public RechnungkontierungDto createRechnungkontierung(
			RechnungkontierungDto rechnungkontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(rechnungkontierungDto, theClientDto.getIDUser());
		// begin
		// erlaubt ??
		pruefeUpdateAufRechnungErlaubt(rechnungkontierungDto.getRechnungIId(),
				theClientDto);
		rechnungkontierungDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		// primary Key generieren
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_RECHNUNGPOSITION);
		rechnungkontierungDto.setIId(pk);
		try {
			Rechnungkontierung rechnungkontierung = new Rechnungkontierung(
					rechnungkontierungDto.getIId(),
					rechnungkontierungDto.getRechnungIId(),
					rechnungkontierungDto.getKostenstelleIId(),
					rechnungkontierungDto.getNProzentsatz(),
					rechnungkontierungDto.getPersonalIIdAendern());
			em.persist(rechnungkontierung);
			em.flush();
			rechnungkontierungDto.setTAendern(rechnungkontierung.getTAendern());
			setRechnungkontierungFromRechnungkontierungDto(rechnungkontierung,
					rechnungkontierungDto);
			return rechnungkontierungDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeRechnungkontierung(
			RechnungkontierungDto rechnungkontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// erlaubt ??
		pruefeUpdateAufRechnungErlaubt(rechnungkontierungDto.getRechnungIId(),
				theClientDto);
		Integer iId = rechnungkontierungDto.getIId();
		Rechnungkontierung toRemove = em.find(Rechnungkontierung.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public RechnungkontierungDto updateRechnungkontierung(
			RechnungkontierungDto rechnungkontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = rechnungkontierungDto.getIId();
		// erlaubt ??
		pruefeUpdateAufRechnungErlaubt(rechnungkontierungDto.getRechnungIId(),
				theClientDto);
		Rechnungkontierung rechnungkontierung = em.find(
				Rechnungkontierung.class, iId);
		if (rechnungkontierung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setRechnungkontierungFromRechnungkontierungDto(rechnungkontierung,
				rechnungkontierungDto);
		updateTAendern(rechnungkontierungDto.getRechnungIId(), theClientDto);
		return rechnungkontierungDto;
	}

	public RechnungkontierungDto rechnungkontierungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Rechnungkontierung rechnungkontierung = em.find(
				Rechnungkontierung.class, iId);
		if (rechnungkontierung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRechnungkontierungDto(rechnungkontierung);
	}

	public RechnungkontierungDto[] rechnungkontierungFindByRechnungIId(
			Integer rechnungIId) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("RechnungkontierungfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		Collection<?> cl = query.getResultList();
		return assembleRechnungkontierungDtos(cl);
	}

	private void setRechnungkontierungFromRechnungkontierungDto(
			Rechnungkontierung rechnungkontierung,
			RechnungkontierungDto rechnungkontierungDto) {
		rechnungkontierung.setRechnungIId(rechnungkontierungDto
				.getRechnungIId());
		rechnungkontierung.setKostenstelleIId(rechnungkontierungDto
				.getKostenstelleIId());
		rechnungkontierung.setNProzentsatz(rechnungkontierungDto
				.getNProzentsatz());
		rechnungkontierung.setPersonalIIdAendern(rechnungkontierungDto
				.getPersonalIIdAendern());
		rechnungkontierung.setTAendern(rechnungkontierungDto.getTAendern());
		em.merge(rechnungkontierung);
		em.flush();
	}

	private RechnungkontierungDto assembleRechnungkontierungDto(
			Rechnungkontierung rechnungkontierung) {
		return RechnungkontierungDtoAssembler.createDto(rechnungkontierung);
	}

	private RechnungkontierungDto[] assembleRechnungkontierungDtos(
			Collection<?> rechnungkontierungs) {
		List<RechnungkontierungDto> list = new ArrayList<RechnungkontierungDto>();
		if (rechnungkontierungs != null) {
			Iterator<?> iterator = rechnungkontierungs.iterator();
			while (iterator.hasNext()) {
				Rechnungkontierung rechnungkontierung = (Rechnungkontierung) iterator
						.next();
				list.add(assembleRechnungkontierungDto(rechnungkontierung));
			}
		}
		RechnungkontierungDto[] returnArray = new RechnungkontierungDto[list
				.size()];
		return (RechnungkontierungDto[]) list.toArray(returnArray);
	}

	public BigDecimal getProzentsatzKontiert(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal bdKontiert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungKontierung.class);
			c.add(Restrictions.eq(RechnungFac.FLR_KONTIERUNG_RECHNUNG_I_ID,
					rechnungIId));
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRRechnungKontierung item = (FLRRechnungKontierung) iter
						.next();
				bdKontiert = bdKontiert.add(item.getN_prozentsatz());
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bdKontiert;
	}

	public RechnungDto pruefeUpdateAufRechnungErlaubt(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungIId);
		if (rechnungDto.getTFibuuebernahme() != null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
					new Exception("RE oder GS i_id=" + rechnungIId
							+ " ist bereits verbucht"));
		}
		if (rechnungDto.getRechnungartCNr().equals(
				RechnungFac.RECHNUNGART_RECHNUNG))
			getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
					LocaleFac.BELEGART_RECHNUNG, rechnungIId, theClientDto);
		else if (rechnungDto.getRechnungartCNr().equals(
				RechnungFac.RECHNUNGART_GUTSCHRIFT)
				|| rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_WERTGUTSCHRIFT))
			getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
					LocaleFac.BELEGART_GUTSCHRIFT, rechnungIId, theClientDto);

		return rechnungDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeRechnungswert(TheClientDto theClientDto) {

		String sFehler = "Rechnung;Mandant;WertAusRechnung;WertAusPositionen;";
		byte[] cRLFAscii = { 13, 10 };
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRRechnungReport.class);

		String[] stati = new String[4];
		stati[0] = RechnungFac.RECHNUNGART_RECHNUNG;
		stati[1] = RechnungFac.RECHNUNGART_GUTSCHRIFT;
		stati[2] = RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;
		stati[3] = RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG;

		c.createAlias(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra");

		c.add(Restrictions.in("ra.c_nr", stati));

		// Filter nach Status
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_VERBUCHT);
		c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));

		c.addOrder(Order.asc("c_nr"));

		List<?> list = c.list();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRRechnungReport item = (FLRRechnungReport) iter.next();

			if (item.getN_wert() != null) {
				RechnungDto rechnungDto = rechnungFindByPrimaryKey(item
						.getI_id());
				// Positionen holen
				RechnungPositionDto[] rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
						.getIId());
				// Belege ohne Positionen duerfen nicht aktiviert werden
				if (rePosDto == null || rePosDto.length == 0) {
				} else {
					BigDecimal wert = getRechnungswertInRechnungswaehrung(
							rechnungDto, rePosDto, theClientDto);
					wert = Helper.rundeKaufmaennisch(wert,
							FinanzFac.NACHKOMMASTELLEN);

					if (item.getN_wert().doubleValue() != wert.doubleValue()) {

						String zeile = rechnungDto.getCNr() + ";"
								+ rechnungDto.getMandantCNr() + ";"
								+ item.getN_wert() + ";" + wert + ";";

						ProtokollDto protokollDto = new ProtokollDto();
						protokollDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
						protokollDto
								.setCTyp(SystemFac.PROTOKOLL_TYP_PRUEFE_RECHNUNGSWERT);
						protokollDto.setCText(zeile);

						erstelleProtokollEintrag(protokollDto, theClientDto);

						sFehler += zeile + new String(cRLFAscii);
					}
				}
			}
		}
		return sFehler;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Integer createRechnungenAusWiederholungsauftrag(Integer auftragIId,
			java.sql.Date dNeuDatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return (Integer) createRechnungenAusWiederholungsauftragGetTermin(
				auftragIId, dNeuDatum, theClientDto, false);
	}

	public java.sql.Date getWiederholungsTermin(Integer auftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return (Date) createRechnungenAusWiederholungsauftragGetTermin(
				auftragIId, getDate(), theClientDto, true);

	}

	private Object createRechnungenAusWiederholungsauftragGetTermin(
			Integer auftragIId, java.sql.Date dNeuDatum,
			TheClientDto theClientDto, boolean getTerminOnly)
			throws EJBExceptionLP {
		int iAnzahl = 0;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			// if (auftragDto.getCNr().equals("12/0000396"))
			// System.out.println();
			// nur wenn der Auftrag offen ist
			if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)
					&& !auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
					&& !auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					&& !auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				// Wiederholungsintervall muss definiert sein
				String sWiederholungsintervall = auftragDto
						.getWiederholungsintervallCNr();
				if (sWiederholungsintervall == null) {
					ArrayList<Object> aInfo = new ArrayList<Object>();
					aInfo.add(auftragDto.getCNr());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_WIEDERHOLUNGSINTERVALL_NICHT_DEFINIERT,
							aInfo, new Exception(
									"Wiederholungsintervall nicht definiert f\u00FCr Auftrag "
											+ auftragDto.getCNr()));
				}
				// Verrechnungsbeginn muss definiert sein
				if (auftragDto.getTLauftermin() == null) {
					ArrayList<Object> aInfo = new ArrayList<Object>();
					aInfo.add(auftragDto.getCNr());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_VERRECHNUNGSBEGINN_NICHT_DEFINIERT,
							aInfo, new Exception(
									"Verrechnungsbeginn nicht definiert f\u00FCr Auftrag "
											+ auftragDto.getCNr()));
				}
				// Rechnungen zu diesem Auftrag suchen
				java.sql.Date dAktuellerTermin = Helper
						.cutDate(new java.sql.Date(auftragDto.getTLauftermin()
								.getTime()));
				RechnungDto[] bisherigeRechnungen = rechnungFindByAuftragIIdTBelegdatum(
						auftragIId, dAktuellerTermin);
				// loggen fuer die Entwicklung
				myLogger.warn(auftragDto.getCNr()
						+ " "
						+ sWiederholungsintervall
						+ " "
						+ Helper.formatDatum(dAktuellerTermin,
								theClientDto.getLocMandant()));

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdRechnungsadresse(), theClientDto);
				Locale localeKunde = Helper.string2Locale(kundeDto
						.getPartnerDto().getLocaleCNrKommunikation());

				HelperWiederholendeRechnung hwr = new HelperWiederholendeRechnung(
						this, bisherigeRechnungen,
						Helper.cutDate(new java.sql.Date(auftragDto
								.getTLauftermin().getTime())),
						sWiederholungsintervall, localeKunde,
						theClientDto.getMandant());
				int anzahl = hwr.init(dNeuDatum);

				if (getTerminOnly)
					// nur naechsten Termin fuer neue Rechnung zurueckliefern!
					return hwr.neuesDatumfuerIntervall();

				if (hwr.anzahlNichtVerrechnet() > 0) {
					Iterator<HelperWiederholendeRechnung.RechnungInfo> it = hwr
							.iteratorZuVerrechnen();
					while (it.hasNext()) {
						HelperWiederholendeRechnung.RechnungInfo ri = it.next();
						myLogger.warn(ri.toString());
						doCreateRechnungAusAuftrag(auftragDto, dNeuDatum,
								ri.solltext, theClientDto);
						iAnzahl++;
					}
				}
			} else {
				myLogger.warn(auftragDto.getCNr()
						+ " ist kein offener WH-Auftrag");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		if (getTerminOnly)
			return null;
		else
			return iAnzahl;
	}

	public int getAnzahlMengenbehafteteRechnungpositionen(Integer rechnungIId,
			TheClientDto theClientDto) {

		int iAnzahl = 0;
		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Rechnungposition pos = ((Rechnungposition) iter.next());

			if (pos.getNMenge() != null) {

				if (pos.getArtikelIId() != null) {
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(pos.getArtikelIId(),
									theClientDto);
					if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
						continue;
					}

				}

				iAnzahl++;
			}
		}
		myLogger.exit("Anzahl: " + iAnzahl);

		return iAnzahl;
	}

	private void doCreateRechnungAusAuftrag(AuftragDto auftragDto,
			Date dNeuDatum, String sKopfText, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// ------------------------------------------------------
		// ----------
		// 1. Kopfdaten der Rechnung erstellen
		// ------------------------------------------------------
		// ----------
		// Belegdatum wird dort uebersteuert
		if (dNeuDatum == null) {
			dNeuDatum = Helper.cutDate(new Date(System.currentTimeMillis()));
		}
		Integer rechnungIId = getRechnungFac().createRechnungAusAuftrag(
				auftragDto.getIId(), dNeuDatum, null, theClientDto);
		// Die Kopfdaten muessen noch etwas nachbearbeitet
		// werden
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungIId);
		// ------------------------------------------------------
		// ----------
		// im Kopftext steht, fuer welchen Zeitraum diese
		// Rechnung gilt, abhaengig vom Wiederholungsintervall
		// zusaetzlich der "normale" Kopftext
		// ------------------------------------------------------
		// ----------
		// dazu brauch ich das Locale des Kunden
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto);

		RechnungtextDto kopftextDto = getRechnungServiceFac()
				.rechnungtextFindByMandantLocaleCNr(theClientDto.getMandant(),
						kundeDto.getPartnerDto().getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_KOPFTEXT);
		if (kopftextDto != null) {
			sKopfText = sKopfText + "\n" + kopftextDto.getCTextinhalt();
		}
		rechnungDto.setCKopftextuebersteuert(sKopfText);
		// ------------------------------------------------------
		// ----------
		// Fusstext wird gleich eingepflegt
		// ------------------------------------------------------
		// ----------
		RechnungtextDto fusstextDto = getRechnungServiceFac()
				.rechnungtextFindByMandantLocaleCNr(theClientDto.getMandant(),
						kundeDto.getPartnerDto().getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_FUSSTEXT);
		if (fusstextDto != null) {
			rechnungDto.setCFusstextuebersteuert(fusstextDto.getCTextinhalt());
		}
		getRechnungFac().updateRechnung(rechnungDto, theClientDto);
		// ------------------------------------------------------
		// ----------
		// 2. Die Auftragspositionen in die Rechnung kopieren
		// ------------------------------------------------------
		// ----------
		// Positionen des Auftrags holen
		AuftragpositionDto[] abPos = getAuftragpositionFac()
				.auftragpositionFindByAuftrag(auftragDto.getIId());
		// Serien- und chargennummernbehaftete Positionen sind
		// nicht zulaessig
		// daher alle Positionen durchschauen
		for (int i = 0; i < abPos.length; i++) {
			if (abPos[i].getArtikelIId() != null) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(abPos[i].getArtikelIId(),
								theClientDto);
				if (Helper.short2Boolean(artikelDto.getBChargennrtragend())
						|| Helper.short2Boolean(artikelDto
								.getBSeriennrtragend())) {
					ArrayList<Object> aInfo = new ArrayList<Object>();
					aInfo.add(auftragDto.getCNr());
					aInfo.add(artikelDto.getCNr());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_WH_AUFTRAG_ENTHAELT_SNR_BEHAFTETE_ARTIKEL,
							aInfo, new Exception(
									"Auftrag enth\u00E4lt Serien- oder Chargennummern tragende Artikel"
											+ auftragDto.getCNr() + " Ident="
											+ artikelDto.getCNr()));
				}
			}
		}
		// in Rechnungspositionen konvertieren
		RechnungPositionDto[] rePos = getBelegpositionkonvertierungFac()
				.konvertiereNachRechnungpositionDto(abPos, theClientDto);
		for (int i = 0; i < rePos.length; i++) {
			rePos[i].setRechnungIId(rechnungIId);

			if (rePos[i].isIntelligenteZwischensumme()) {
				ZwsPositionMapper mapper = new ZwsPositionMapper(
						getAuftragpositionFac(), this);
				mapper.map(abPos[i], rePos[i], rechnungIId);
			}

			getRechnungFac().createRechnungPosition(rePos[i],
					rechnungDto.getLagerIId(), theClientDto);
		}
		myLogger.warn("Rechnung " + rechnungDto.getCNr() + " "
				+ rechnungDto.getTBelegdatum().toString() + " erstellt");
	}

	public StringBuffer getKopftextForIntervall(String sWiederholungsintervall,
			Date dAktuellerTermin, Locale localeCNrKunde, String sMandant) {

		// Monatsnamen localeabhaengig mit Calendar
		// formatieren,nur Monat verwenden.
		SimpleDateFormat dateformat = new SimpleDateFormat("MMMM",
				localeCNrKunde);
		Calendar cal = GregorianCalendar.getInstance(localeCNrKunde);
		cal.set(Calendar.YEAR, GregorianCalendar.JANUARY, 1);

		StringBuffer sbKopftext = new StringBuffer();
		sbKopftext.append(
				getTextRespectUISpr("rechnung.rechnungfuer", sMandant,
						localeCNrKunde)).append(" ");
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dAktuellerTermin);
		if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
			sbKopftext.append(getTextRespectUISpr("lp.kw", sMandant,
					localeCNrKunde));
			sbKopftext.append(" ");
			int iKW1 = gc.get(Calendar.WEEK_OF_YEAR);
			int iJahr1 = gc.get(Calendar.YEAR);
			sbKopftext.append(iKW1);
			// und die Woche drauf - sieht umstaendlich aus,
			// aber kann auch den Jahreswechsel z.B. KW 53, 1
			int iTag = gc.get(Calendar.DATE) + 7; // 1 Woche
			// dazu
			gc.set(Calendar.DATE, iTag);
			int iJahr2 = gc.get(Calendar.YEAR);
			if (iJahr1 != iJahr2) {
				sbKopftext.append(" / " + iJahr1);
			}
			int iKW2 = gc.get(Calendar.WEEK_OF_YEAR);
			sbKopftext.append(", " + iKW2);
			sbKopftext.append(" / " + iJahr2);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_HALBJAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(getTextRespectUISpr("lp.ersteshalbjahr",
						sMandant, localeCNrKunde));
				sbKopftext.append(" " + gc.get(Calendar.YEAR));
			} else if (iMonat == Calendar.JULY) {
				sbKopftext.append(getTextRespectUISpr("lp.zweiteshalbjahr",
						sMandant, localeCNrKunde));
				sbKopftext.append(" " + gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - juli 2007
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 5 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 5);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - jaenner 2008
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 11 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 11);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - jaenner 2008
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 13 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 23);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - jaenner 2008
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 35 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 35);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - jaenner 2008
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 47 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 47);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
			int iMonat = gc.get(Calendar.MONTH);
			if (iMonat == Calendar.JANUARY) {
				sbKopftext.append(gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - jaenner 2008
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 59 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 59);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
			cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));
			sbKopftext.append(dateformat.format(cal.getTime()));
			sbKopftext.append(" " + gc.get(Calendar.YEAR));
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
			int iMonat = gc.get(Calendar.MONTH);
			int iQuartal = iMonat / 3;
			int iOffset = iMonat % 3;

			if (iOffset == 0) {
				if (iQuartal == 0) {
					sbKopftext.append(getTextRespectUISpr("lp.quartal1",
							sMandant, localeCNrKunde));
				} else if (iQuartal == 1) {
					sbKopftext.append(getTextRespectUISpr("lp.quartal2",
							sMandant, localeCNrKunde));
				} else if (iQuartal == 2) {
					sbKopftext.append(getTextRespectUISpr("lp.quartal3",
							sMandant, localeCNrKunde));
				} else if (iQuartal == 3) {
					sbKopftext.append(getTextRespectUISpr("lp.quartal4",
							sMandant, localeCNrKunde));
				}
				sbKopftext.append(" " + gc.get(Calendar.YEAR));
			} else {
				// wenns nicht jaenner oder juli ist, dann z.b.
				// februar 2007 - april 2007
				cal.set(Calendar.YEAR, gc.get(Calendar.YEAR));

				cal.set(Calendar.MONTH, gc.get(Calendar.MONTH));
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR) + " - ");
				// 2 Monate addieren
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 2);
				sbKopftext.append(dateformat.format(cal.getTime()));
				sbKopftext.append(" " + cal.get(Calendar.YEAR));
			}
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
			int iKW1 = gc.get(Calendar.WEEK_OF_YEAR);
			sbKopftext.append(getTextRespectUISpr("lp.kw", sMandant,
					localeCNrKunde));
			sbKopftext.append(" " + iKW1);
			sbKopftext.append(" / " + gc.get(Calendar.YEAR));
		}
		// unbekanntes Intervall: Exception
		else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					new IllegalArgumentException(
							"Wiederholungsintervall wird nicht unterst\u00FCtzt: "
									+ sWiederholungsintervall));
		}
		return sbKopftext;
	}

	public java.sql.Date berechneinterval(String sWiederholungsintervall,
			java.sql.Date dAktuellerTermin) {
		// naechstes Datum bestimmen. abhaengig vom
		// Wiederholungsintervall.
		GregorianCalendar gcNaechstesVerrechnungsdatum = new GregorianCalendar();
		gcNaechstesVerrechnungsdatum.setTime(dAktuellerTermin);
		if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
			int iTag = gcNaechstesVerrechnungsdatum.get(Calendar.DATE);
			iTag = iTag + 14; // 14 Tage
			gcNaechstesVerrechnungsdatum.set(Calendar.DATE, iTag);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_HALBJAHR)) {
			int iMonat = gcNaechstesVerrechnungsdatum.get(Calendar.MONTH);
			iMonat = iMonat + 6; // 6 Monate
			gcNaechstesVerrechnungsdatum.set(Calendar.MONTH, iMonat);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
			int iJahr = gcNaechstesVerrechnungsdatum.get(Calendar.YEAR);
			iJahr = iJahr + 1; // 1 Jahr
			gcNaechstesVerrechnungsdatum.set(Calendar.YEAR, iJahr);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
			int iJahr = gcNaechstesVerrechnungsdatum.get(Calendar.YEAR);
			iJahr = iJahr + 2; // 2 Jahr
			gcNaechstesVerrechnungsdatum.set(Calendar.YEAR, iJahr);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
			int iJahr = gcNaechstesVerrechnungsdatum.get(Calendar.YEAR);
			iJahr = iJahr + 3; // 3 Jahr
			gcNaechstesVerrechnungsdatum.set(Calendar.YEAR, iJahr);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
			int iJahr = gcNaechstesVerrechnungsdatum.get(Calendar.YEAR);
			iJahr = iJahr + 4; // 4 Jahr
			gcNaechstesVerrechnungsdatum.set(Calendar.YEAR, iJahr);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
			int iJahr = gcNaechstesVerrechnungsdatum.get(Calendar.YEAR);
			iJahr = iJahr + 5; // 5 Jahr
			gcNaechstesVerrechnungsdatum.set(Calendar.YEAR, iJahr);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
			int iMonat = gcNaechstesVerrechnungsdatum.get(Calendar.MONTH);
			iMonat = iMonat + 1; // 1 Monat
			gcNaechstesVerrechnungsdatum.set(Calendar.MONTH, iMonat);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
			int iMonat = gcNaechstesVerrechnungsdatum.get(Calendar.MONTH);
			iMonat = iMonat + 3; // 3 Monate
			gcNaechstesVerrechnungsdatum.set(Calendar.MONTH, iMonat);
		} else if (sWiederholungsintervall
				.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
			int iTag = gcNaechstesVerrechnungsdatum.get(Calendar.DATE);
			iTag = iTag + 7; // 7 Tage
			gcNaechstesVerrechnungsdatum.set(Calendar.DATE, iTag);
		}
		// unbekanntes Intervall: Exception
		else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					new IllegalArgumentException(
							"Wiederholungsintervall wird nicht unterst\u00FCtzt: "
									+ sWiederholungsintervall));
		}
		// neues Datum setzen und von vorn beginnen
		dAktuellerTermin = new java.sql.Date(
				gcNaechstesVerrechnungsdatum.getTimeInMillis());
		return dAktuellerTermin;
	}

	public ArrayList<Integer> getAngelegteRechnungen(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		ArrayList<Integer> a = new ArrayList<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnung.class);
			// Filter auf Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// nur Rechnungen
			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.eq(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							RechnungFac.RECHNUNGTYP_RECHNUNG));
			// Filter nach Status: nur angelegte
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
					RechnungFac.STATUS_ANGELEGT));
			// nach RE-Nummer sortieren
			c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
			// Query ausfuehren
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRRechnung item = (FLRRechnung) iter.next();
				a.add(item.getI_id());
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return a;
	}

	public RechnungPositionDto[] rechnungPositionByAuftragposition(
			Integer iIdAuftragpositionI) {

		Query query = em
				.createNamedQuery("RechnungPositionfindByAuftragpositionIId");
		query.setParameter(1, iIdAuftragpositionI);
		return assembleRechnungpositionDtos(query.getResultList());

	}

	/**
	 * Aus einem Lieferschein jene Position heraussuchen, die zu einer
	 * bestimmten Auftragposition gehoert.
	 * 
	 * @param iIdRechnungI
	 *            pk des Lieferscheins
	 * @param iIdAuftragpositionI
	 *            pk der Auftragposition
	 * @throws EJBExceptionLP
	 * @return LieferscheinpositionDto die entsprechende Position, null wenn es
	 *         keine gibt
	 */

	public RechnungPositionDto getRechnungPositionByRechnungAuftragposition(
			Integer iIdRechnungI, Integer iIdAuftragpositionI)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdRechnungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRechnungI == null"));
		}

		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}
		RechnungPositionDto oRechnungpositionDtoO = null;

		try {
			Query query = em
					.createNamedQuery("RechnungPositionfindByRechnungIIdAuftragpositionIId");
			query.setParameter(1, iIdRechnungI);
			query.setParameter(2, iIdAuftragpositionI);
			Rechnungposition rechnungposition = (Rechnungposition) query
					.getSingleResult();
			if (rechnungposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			oRechnungpositionDtoO = assembleRechnungPositionDto(rechnungposition);

		} catch (NoResultException ex) {
			// es gibt keine Rechnungsposition mit diesen Eigenschaften
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		return oRechnungpositionDtoO;
	}

	/**
	 * Das maximale iSort bei den Rechnungspositionen fuer einen bestimmten
	 * Mandanten bestimmen.
	 * 
	 * @param iIdRechnungI
	 *            die aktuelle Rechnung
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private Integer getMaxISort(Integer iIdRechnungI) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungPositionejbSelectMaxISort");
			query.setParameter(1, iIdRechnungI);
			Integer maxISort = (Integer) query.getSingleResult();
			if (maxISort == null) {
				return 0;
			}
			return maxISort;
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	/**
	 * Bei einer auftragbezogenen Rechnung ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param auftragIIdI
	 *            Integer
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	// public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
	// Integer iIdRechnungI, Integer auftragIIdI, TheClientDto theClientDto)
	// throws EJBExceptionLP {
	// try {
	// RechnungDto rechnungDto = rechnungFindByPrimaryKey(iIdRechnungI);
	// AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
	// .auftragpositionFindByAuftrag(auftragIIdI);
	//
	// boolean bEsGibtNochPositiveOffene = false;
	// if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
	// MandantFac.ZUSATZFUNKTION_VERLEIH, theClientDto)) {
	// for (int i = 0; i < aAuftragpositionDto.length; i++) {
	// if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
	// .equals(aAuftragpositionDto[i]
	// .getAuftragpositionstatusCNr())) {
	// if (aAuftragpositionDto[i].getNMenge() != null
	// && aAuftragpositionDto[i].getNMenge()
	// .doubleValue() > 0) {
	// bEsGibtNochPositiveOffene = true;
	// }
	// }
	// }
	// }
	//
	// for (int i = 0; i < aAuftragpositionDto.length; i++) {
	// // IMS 2129
	// if (aAuftragpositionDto[i].getNMenge() != null) {
	//
	// // wenn es noch positive offene gibt, dann duerfen dei
	// // negativen noch nicht geliert werden
	//
	// if (aAuftragpositionDto[i].getNMenge().doubleValue() < 0
	// && bEsGibtNochPositiveOffene) {
	// continue;
	// }
	//
	// // dieses Flag legt fest, ob eine Rechnungsposition fuer die
	// // aktuelle
	// // Auftragposition angleget oder aktualisiert werden soll
	// boolean bRechnungpositionErzeugen = false;
	//
	// // die Menge, mit der eine neue Rechnungsposition angelegt
	// // oder eine
	// // bestehende Rechnungsposition aktualisiert werden soll
	// BigDecimal nMengeFuerRechnungposition = null;
	//
	// // die Serien- oder Chargennummer, die bei der Abbuchung
	// // verwendet werden soll
	// String cSerienchargennummer = null;
	//
	// if (aAuftragpositionDto[i].getPositionsartCNr().equals(
	// AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = aAuftragpositionDto[i]
	// .getNOffeneMenge();
	// } else if (aAuftragpositionDto[i].getPositionsartCNr()
	// .equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
	// ArtikelDto artikelDto = getArtikelFac()
	// .artikelFindByPrimaryKey(
	// aAuftragpositionDto[i].getArtikelIId(),
	// theClientDto);
	//
	// // nicht lagerbewirtschaftete Artikel werden mit der
	// // vollen offenen Menge uebernommen
	// if (!Helper.short2boolean(artikelDto
	// .getBLagerbewirtschaftet())) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = aAuftragpositionDto[i]
	// .getNOffeneMenge();
	// } else {
	// if (Helper.short2boolean(artikelDto
	// .getBSeriennrtragend())) {
	// // seriennummerbehaftete Artikel koennen nicht
	// // automatisch uebernommen werden
	// } else if (Helper.short2boolean(artikelDto
	// .getBChargennrtragend())) {
	// // chargennummernbehaftete Artikel koennen nur
	// // uebernommen werden, wenn
	// // es nur eine Charge gibt und mit der Menge,
	// // die in dieser Charge
	// // vorhanden ist
	// SeriennrChargennrAufLagerDto[] alleChargennummern = getLagerFac()
	// .getAllSerienChargennrAufLager(
	// artikelDto.getIId(),
	// rechnungDto.getLagerIId(),
	// theClientDto, true, false);
	// if (alleChargennummern != null
	// && alleChargennummern.length == 1) {
	// BigDecimal nLagerstd = alleChargennummern[0]
	// .getNMenge();
	// // ist ausreichend auf Lager?
	// if (nLagerstd
	// .compareTo(aAuftragpositionDto[i]
	// .getNOffeneMenge()) >= 0) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = aAuftragpositionDto[i]
	// .getNOffeneMenge();
	// }
	// // nicht genug auf Lager, aber es kann
	// // zumindest ein Teil abgebucht werden.
	// else if (nLagerstd
	// .compareTo(new BigDecimal(0)) > 0) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = nLagerstd;
	// }
	// }
	// } else {
	// // bei lagerbewirtschafteten Artikeln muss die
	// // Menge auf Lager
	// // beruecksichtigt werden
	// BigDecimal nMengeAufLager = getLagerFac()
	// .getMengeAufLager(artikelDto.getIId(),
	// rechnungDto.getLagerIId(),
	// null, theClientDto);
	// if (nMengeAufLager.doubleValue() >= aAuftragpositionDto[i]
	// .getNOffeneMenge().doubleValue()) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = aAuftragpositionDto[i]
	// .getNOffeneMenge();
	// } else if (nMengeAufLager.doubleValue() > 0) {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = nMengeAufLager;
	// }
	// }
	// }
	// } else if (aAuftragpositionDto[i]
	// .getPositionsartCNr()
	// .equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME))
	// {
	// bRechnungpositionErzeugen = true;
	// nMengeFuerRechnungposition = aAuftragpositionDto[i]
	// .getNOffeneMenge();
	// }
	//
	// if (bRechnungpositionErzeugen
	// && nMengeFuerRechnungposition != null) {
	// RechnungPositionDto rechnungpositionBisherDto =
	// getRechnungPositionByRechnungAuftragposition(
	// iIdRechnungI, aAuftragpositionDto[i].getIId());
	//
	// if (rechnungpositionBisherDto == null) {
	// AuftragpositionDto[] abpos = new AuftragpositionDto[] {
	// aAuftragpositionDto[i] };
	// RechnungPositionDto rechnungpositionDto =
	// getBelegpositionkonvertierungFac()
	// .konvertiereNachRechnungpositionDto(abpos,
	// theClientDto)[0];
	//
	// rechnungpositionDto.setRechnungIId(iIdRechnungI);
	// rechnungpositionDto
	// .setNMenge(nMengeFuerRechnungposition);
	//
	// if (aAuftragpositionDto[i]
	// .getPositionsartCNr()
	// .equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME))
	// {
	// Integer von = getAuftragpositionFac()
	// .getPositionNummer(
	// aAuftragpositionDto[i]
	// .getZwsVonPosition());
	// if (von != null) {
	// rechnungpositionDto
	// .setZwsVonPosition(getRechnungFac()
	// .getPositionIIdFromPositionNummer(
	// iIdRechnungI, von));
	// }
	// Integer bis = getAuftragpositionFac()
	// .getPositionNummer(
	// aAuftragpositionDto[i]
	// .getZwsBisPosition());
	// if (bis != null) {
	// rechnungpositionDto
	// .setZwsBisPosition(getRechnungFac()
	// .getPositionIIdFromPositionNummer(
	// iIdRechnungI, von));
	// }
	// }
	// createRechnungPosition(rechnungpositionDto,
	// rechnungDto.getLagerIId(), theClientDto);
	// } else {
	// rechnungpositionBisherDto
	// .setNMenge(nMengeFuerRechnungposition);
	//
	// updateRechnungpositionSichtAuftrag(
	// rechnungpositionBisherDto, theClientDto);
	// }
	// }
	// } else {
	// if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
	// .equals(aAuftragpositionDto[i]
	// .getAuftragpositionstatusCNr())) {
	// AuftragpositionDto[] abpos = new AuftragpositionDto[] {
	// aAuftragpositionDto[i] };
	// RechnungPositionDto rechnungpositionDto =
	// getBelegpositionkonvertierungFac()
	// .konvertiereNachRechnungpositionDto(abpos,
	// theClientDto)[0];
	// rechnungpositionDto.setRechnungIId(iIdRechnungI);
	// rechnungpositionDto.setISort(null);
	// createRechnungPosition(rechnungpositionDto,
	// rechnungDto.getLagerIId(), theClientDto);
	// }
	// }
	// }
	// } catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	// }

	private boolean isArtikelSetHead(Integer artikelIId,
			TheClientDto theClientDto) {
		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);
		return stklDto != null
				&& stklDto.getStuecklisteartCNr().equals(
						StuecklisteFac.STUECKLISTEART_SETARTIKEL);
	}

	private BigDecimal getAvailableAmountArtikelset(Integer auftragpositionIId,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getAvailableAmount();
		}

		return null;
	}

	private Artikelset getAppropriateArtikelset(Integer auftragpositionIId,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset;
		}

		return null;
	}

	private List<SeriennrChargennrMitMengeDto> getAvailableSnrsArtikelset(
			Integer auftragpositionIId, List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getIdentities();
		}

		// TODO: Eigentlich(?) Ist das ein Fehler wenn ich hier kein Artikelset
		// finde!?
		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}

	protected RechnungPositionDto uebernimmAuftragposition(
			AuftragpositionDto auftragpositionDto, RechnungDto rechnungDto,
			boolean bEsGibtNochPositiveOffene, List<Artikelset> artikelsets,
			TheClientDto theClientDto) throws RemoteException {

		RechnungPositionDto createdRePosDto = null;

		if (AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
				.equals(auftragpositionDto.getAuftragpositionstatusCNr()))
			return null;

		// IMS 2129
		if (auftragpositionDto.getNMenge() != null) {

			// wenn es noch positive offene gibt, dann duerfen dei
			// negativen noch nicht geliert werden

			if (auftragpositionDto.getNMenge().signum() < 0
					&& bEsGibtNochPositiveOffene) {
				return null;
			}

			// dieses Flag legt fest, ob eine Rechnungsposition fuer die
			// aktuelle
			// Auftragposition angleget oder aktualisiert werden soll
			boolean bRechnungpositionErzeugen = false;

			// die Menge, mit der eine neue Rechnungsposition angelegt
			// oder eine
			// bestehende Rechnungsposition aktualisiert werden soll
			BigDecimal nMengeFuerRechnungposition = null;

			if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				bRechnungpositionErzeugen = true;
				nMengeFuerRechnungposition = auftragpositionDto
						.getNOffeneMenge();
			} else if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								auftragpositionDto.getArtikelIId(),
								theClientDto);

				// nicht lagerbewirtschaftete Artikel werden mit der
				// vollen offenen Menge uebernommen
				if (!artikelDto.isLagerbewirtschaftet()) {

					bRechnungpositionErzeugen = true;

					if (isArtikelSetHead(artikelDto.getIId(), theClientDto)) {
						BigDecimal verfuegbareMenge = getAvailableAmountArtikelset(
								auftragpositionDto.getIId(), artikelsets);
						if (null != verfuegbareMenge) {
							nMengeFuerRechnungposition = auftragpositionDto
									.getNOffeneMenge().min(verfuegbareMenge);
						}
					} else {
						nMengeFuerRechnungposition = auftragpositionDto
								.getNOffeneMenge();
					}

					// nMengeFuerRechnungposition = auftragpositionDto
					// .getNOffeneMenge();
				} else {
					if (artikelDto.isSeriennrtragend()) {
						// seriennummerbehaftete Artikel werden nur
						// uebernommen wenn sie in einem Artikelset vorhanden
						// sind
						if (auftragpositionDto.getPositioniIdArtikelset() != null) {
							Artikelset artikelset = getAppropriateArtikelset(
									auftragpositionDto
											.getPositioniIdArtikelset(),
									artikelsets);
							if (null != artikelset) {
								BigDecimal sollsatzgroesse = auftragpositionDto
										.getNMenge().divide(
												artikelset.getHead()
														.getNMenge());
								nMengeFuerRechnungposition = sollsatzgroesse
										.multiply(artikelset
												.getAvailableAmount());
								bRechnungpositionErzeugen = true;
							}
						}
					} else if (artikelDto.isChargennrtragend()) {
						// chargennummernbehaftete Artikel koennen nur
						// uebernommen werden, wenn
						// es nur eine Charge gibt und mit der Menge,
						// die in dieser Charge
						// vorhanden ist
						SeriennrChargennrAufLagerDto[] alleChargennummern = getLagerFac()
								.getAllSerienChargennrAufLager(
										artikelDto.getIId(),
										rechnungDto.getLagerIId(),
										theClientDto, true, false);
						if (alleChargennummern != null
								&& alleChargennummern.length == 1) {
							BigDecimal nLagerstd = alleChargennummern[0]
									.getNMenge();
							// ist ausreichend auf Lager?
							if (nLagerstd.compareTo(auftragpositionDto
									.getNOffeneMenge()) >= 0) {
								bRechnungpositionErzeugen = true;
								nMengeFuerRechnungposition = auftragpositionDto
										.getNOffeneMenge();
							}
							// nicht genug auf Lager, aber es kann
							// zumindest ein Teil abgebucht werden.
							else if (nLagerstd.signum() > 0) {
								bRechnungpositionErzeugen = true;
								nMengeFuerRechnungposition = nLagerstd;
							}
						}
					} else {
						// bei lagerbewirtschafteten Artikeln muss die
						// Menge auf Lager
						// beruecksichtigt werden

						BigDecimal nMengeAufLager = null;
						boolean bImmerAusreichendVerfuegbar = false;
						try {
							ParametermandantDto parameterM = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ARTIKEL,
											ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
							bImmerAusreichendVerfuegbar = ((Boolean) parameterM
									.getCWertAsObject()).booleanValue();

						} catch (RemoteException ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
						}
						if (bImmerAusreichendVerfuegbar) {
							nMengeAufLager = new BigDecimal(999999999);
						} else {
							nMengeAufLager = getLagerFac().getMengeAufLager(
									artikelDto.getIId(),
									rechnungDto.getLagerIId(), null,
									theClientDto);
						}

						if (nMengeAufLager.signum() > 0) {
							bRechnungpositionErzeugen = true;
							if (auftragpositionDto.getPositioniIdArtikelset() != null) {

								Artikelset artikelset = getAppropriateArtikelset(
										auftragpositionDto
												.getPositioniIdArtikelset(),
										artikelsets);
								if (null != artikelset) {
									BigDecimal sollsatzGroesse = auftragpositionDto
											.getNMenge().divide(
													artikelset.getHead()
															.getNMenge());
									nMengeFuerRechnungposition = auftragpositionDto
											.getNOffeneMenge()
											.min(artikelset
													.getAvailableAmount())
											.multiply(sollsatzGroesse)
											.min(nMengeAufLager);
								} else {
									nMengeFuerRechnungposition = BigDecimal.ONE;
								}
							} else {
								nMengeFuerRechnungposition = auftragpositionDto
										.getNOffeneMenge().min(nMengeAufLager);
							}
						}
					}
				}
			} else if (auftragpositionDto.isIntelligenteZwischensumme()) {
				bRechnungpositionErzeugen = true;
				nMengeFuerRechnungposition = auftragpositionDto
						.getNOffeneMenge();
			}

			if (bRechnungpositionErzeugen && nMengeFuerRechnungposition != null) {
				RechnungPositionDto rechnungpositionBisherDto = getRechnungPositionByRechnungAuftragposition(
						rechnungDto.getIId(), auftragpositionDto.getIId());

				if (rechnungpositionBisherDto == null) {
					AuftragpositionDto[] abpos = new AuftragpositionDto[] { auftragpositionDto };
					RechnungPositionDto rechnungpositionDto = getBelegpositionkonvertierungFac()
							.konvertiereNachRechnungpositionDto(abpos,
									theClientDto)[0];

					rechnungpositionDto.setRechnungIId(rechnungDto.getIId());
					rechnungpositionDto.setNMenge(nMengeFuerRechnungposition);
					rechnungpositionDto
							.setNMaterialzuschlagKurs(auftragpositionDto
									.getNMaterialzuschlagKurs());
					rechnungpositionDto
							.setTMaterialzuschlagDatum(auftragpositionDto
									.getTMaterialzuschlagDatum());

					if (auftragpositionDto.isIntelligenteZwischensumme()) {
						Integer von = getAuftragpositionFac()
								.getPositionNummer(
										auftragpositionDto.getZwsVonPosition());
						if (von != null) {
							rechnungpositionDto
									.setZwsVonPosition(getRechnungFac()
											.getPositionIIdFromPositionNummer(
													rechnungDto.getIId(), von));
						}
						Integer bis = getAuftragpositionFac()
								.getPositionNummer(
										auftragpositionDto.getZwsBisPosition());
						if (bis != null) {
							rechnungpositionDto
									.setZwsBisPosition(getRechnungFac()
											.getPositionIIdFromPositionNummer(
													rechnungDto.getIId(), bis));
						}
						rechnungpositionDto
								.setBZwsPositionspreisDrucken(auftragpositionDto
										.getBZwsPositionspreisZeigen());
					}

					if (auftragpositionDto.getPositioniIdArtikelset() != null) {
						getBelegVerkaufFac().setupPositionWithIdentities(
								rechnungpositionDto,
								getAvailableSnrsArtikelset(auftragpositionDto
										.getPositioniIdArtikelset(),
										artikelsets), theClientDto);

						RechnungPositionDto headRechPosDto = getRechnungPositionByRechnungAuftragposition(
								rechnungDto.getIId(),
								auftragpositionDto.getPositioniIdArtikelset());
						rechnungpositionDto
								.setPositioniIdArtikelset(headRechPosDto
										.getIId());
					}

					createdRePosDto = createRechnungPositionImpl(
							rechnungpositionDto, rechnungDto.getLagerIId(),
							new ArrayList<SeriennrChargennrMitMengeDto>(),
							false, theClientDto);
					// if(rechnungpositionDto.getAuftragpositionIId() != null) {
					// createdRePosDto = createRechnungPosition(
					// rechnungpositionDto, rechnungDto.getLagerIId(),
					// getAvailableSnrsArtikelset(rechnungpositionDto.getAuftragpositionIId(),
					// artikelsets),
					// theClientDto) ;
					// } else {
					// createdRePosDto = createRechnungPosition(
					// rechnungpositionDto, rechnungDto.getLagerIId(),
					// theClientDto);
					// }
				} else {
					rechnungpositionBisherDto
							.setNMenge(nMengeFuerRechnungposition);

					updateRechnungpositionSichtAuftrag(
							rechnungpositionBisherDto, theClientDto);

					createdRePosDto = rechnungpositionBisherDto;
				}
			}
		} else {
			if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
					.equals(auftragpositionDto.getAuftragpositionstatusCNr())) {
				AuftragpositionDto[] abpos = new AuftragpositionDto[] { auftragpositionDto };
				RechnungPositionDto rechnungpositionDto = getBelegpositionkonvertierungFac()
						.konvertiereNachRechnungpositionDto(abpos, theClientDto)[0];
				rechnungpositionDto.setRechnungIId(rechnungDto.getIId());
				rechnungpositionDto.setISort(null);
				// SP2426 Damit die Auftragsposition nicht Erledigt wird
				rechnungpositionDto.setAuftragpositionIId(null);

				createdRePosDto = createRechnungPosition(rechnungpositionDto,
						rechnungDto.getLagerIId(), theClientDto);
			}
		}

		return createdRePosDto;
	}

	/**
	 * Bei einer auftragbezogenen Rechnung ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param auftragIIdI
	 *            Integer
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public RechnungPositionDto uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdRechnungI, Integer auftragIIdI,
			List<Artikelset> artikelsets, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungPositionDto firstRePosDto = null;

		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(iIdRechnungI);
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIIdI);

			boolean bEsGibtNochPositiveOffene = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_VERLEIH, theClientDto)) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
							.equals(aAuftragpositionDto[i]
									.getAuftragpositionstatusCNr())) {
						if (aAuftragpositionDto[i].getNMenge() != null
								&& aAuftragpositionDto[i].getNMenge()
										.doubleValue() > 0) {
							bEsGibtNochPositiveOffene = true;
						}
					}
				}
			}

			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				RechnungPositionDto reposDto = uebernimmAuftragposition(
						aAuftragpositionDto[i], rechnungDto,
						bEsGibtNochPositiveOffene, artikelsets, theClientDto);
				if (null == firstRePosDto) {
					firstRePosDto = reposDto;
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return firstRePosDto;
	}

	/**
	 * Spezielle Methode zum Erfassen einer mengenbehafteten auftragbezogenen
	 * Rechnungsposition aus der Sicht Auftrag heraus. Diese Methode wird nur
	 * dann aufgerufen, wenn zu einer Auftragposition bereits eine
	 * Rechnungsposition erfasst wurde. Das bedeutet, dass die eingegebene Menge
	 * zur bestehenden Menge addiert werden muss. <br>
	 * Es gilt: Die eingegebene Menge muss > 0 sein.
	 * 
	 * @param rechnungpositionDtoI
	 *            dieser Teil der Auftragposition wird als Rechnungsposition
	 *            erfasst
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateRechnungpositionSichtAuftrag(
			RechnungPositionDto rechnungpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		pruefePflichtfelderBelegposition(rechnungpositionDtoI, theClientDto);
		updateTAendern(rechnungpositionDtoI.getRechnungIId(), theClientDto);
		try {
			RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungpositionDtoI
					.getRechnungIId());
			// IMS 2129
			if (rechnungpositionDtoI.getNMenge().doubleValue() > 0) {

				BigDecimal nZusaetzlicheMenge = rechnungpositionDtoI
						.getNMenge();

				if (nZusaetzlicheMenge.doubleValue() > 0) {

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Rechnungsposition.

					Rechnungposition rechnungpositionBisher = em.find(
							Rechnungposition.class,
							rechnungpositionDtoI.getIId());
					if (rechnungpositionBisher == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
					}

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					rechnungpositionDtoI.setNMenge(rechnungpositionBisher
							.getNMenge().add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (rechnungpositionDtoI.getRechnungpositionartCNr()
							.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
						// die Lagerbuchung ist absolut

						bucheRechnungPositionAmLager(rechnungpositionDtoI,
								rechnungDto.getLagerIId(), false, theClientDto);
						// die Lieferscheinposition mit den neuen Werten
						// aktualisieren
						rechnungpositionBisher.setNMenge(rechnungpositionDtoI
								.getNMenge());

					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN,
								new Exception("nMenge <= 0"));
					}
				} else if (rechnungpositionDtoI.getNMenge().doubleValue() > 0) {

					nZusaetzlicheMenge = rechnungpositionDtoI.getNMenge();

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Lieferscheinposition.

					Rechnungposition rechnungpositionBisher = em.find(
							Rechnungposition.class,
							rechnungpositionDtoI.getIId());

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					rechnungpositionDtoI.setNMenge(rechnungpositionBisher
							.getNMenge().add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (rechnungpositionDtoI.getRechnungpositionartCNr()
							.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
						// die Lagerbuchung ist absolut

						bucheRechnungPositionAmLager(rechnungpositionDtoI,
								rechnungDto.getLagerIId(), false, theClientDto);
					}
					// die Lieferscheinposition mit den neuen Werten
					// aktualisieren
					rechnungpositionBisher.setNMenge(rechnungpositionDtoI
							.getNMenge());

				}

				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						rechnungpositionDtoI.getAuftragpositionIId(),
						theClientDto);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Fuer die Nachkalkulation der Rechnung den Ist-Verkaufswert (=
	 * NettoVerkaufspreisPlusAufschlaegeMinusRabatte pro Stueck * gelieferte
	 * Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Rechnungspositionen.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Verkaufswert der Artikelart Ist in
	 *         Mandantenwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneVerkaufswertIst(Integer iIdRechnungI,
			String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iIdRechnungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRechnungI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertIstO = Helper.getBigDecimalNull();

		RechnungPositionDto[] aRechnungpositionDtos = rechnungPositionFindByRechnungIId(iIdRechnungI);

		for (int i = 0; i < aRechnungpositionDtos.length; i++) {

			// alle mengenbehafteten Positionen beruecksichtigen
			if (aRechnungpositionDtos[i].getNMenge() != null
					&& aRechnungpositionDtos[i].getArtikelIId() != null) {
				// PJ 15859
				if (aRechnungpositionDtos[i].getPositioniIdArtikelset() == null) {

					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aRechnungpositionDtos[i].getArtikelIId(),
									theClientDto);

					BigDecimal bdBeitragDieserPosition = aRechnungpositionDtos[i]
							.getNMenge()
							.multiply(
									aRechnungpositionDtos[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					// je nach Artikelart beruecksichtigen
					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertIstO = bdVerkaufswertIstO
									.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertIstO = bdVerkaufswertIstO
									.add(bdBeitragDieserPosition);
						}
					}
				}
			}
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Ist : "
				+ bdVerkaufswertIstO.toString());

		return bdVerkaufswertIstO;
	}

	public void berechnePauschalposition(BigDecimal neuWert,
			Integer positionIId, Integer belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal altWert = getGesamtpreisPosition(positionIId, theClientDto);
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(belegIId);
		RechnungPositionDto[] rechnungPositionDtos = rechnungPositionFindByPositionIId(positionIId);
		for (int i = 0; i < rechnungPositionDtos.length; i++) {
			RechnungPositionDto rechnungPositionDto = (RechnungPositionDto) getBelegVerkaufFac()
					.berechnePauschalposition(rechnungPositionDtos[i],
							rechnungDto, neuWert, altWert);
			Rechnungposition position = em.find(Rechnungposition.class,
					rechnungPositionDto.getIId());
			position.setNNettoeinzelpreis(rechnungPositionDto
					.getNNettoeinzelpreis());
			position.setNNettoeinzelpreisplusaufschlag(rechnungPositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			position.setNNettoeinzelpreisplusaufschlagminusrabatt(rechnungPositionDto
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			position.setBNettopreisuebersteuert(Helper.boolean2Short(true));
		}
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Ist-Gestehungswert (=
	 * Gestehungswert des Artikels auf Lager Rechnung pro Stueck * gelieferter
	 * Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Rechnungspositionen.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Ist-Gestehungswert der gewuenschten Artikelart in
	 *         Mandantenwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGestehungswertIst(Integer iIdRechnungI,
			String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iIdRechnungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRechnungI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}
		Rechnung oRechnung = null;
		oRechnung = em.find(Rechnung.class, iIdRechnungI);
		if (oRechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		BigDecimal bdGestehungswertIstO = Helper.getBigDecimalNull();
		try {
			RechnungPositionDto[] aRechnungpositionDtos = getRechnungFac()
					.rechnungPositionFindByRechnungIId(iIdRechnungI);

			for (int i = 0; i < aRechnungpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aRechnungpositionDtos[i].getNMenge() != null
						&& aRechnungpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aRechnungpositionDtos[i].getArtikelIId(),
									theClientDto);

					// Grundlage ist der positionsbezogene Gestehungspreis des
					// Artikels.
					BigDecimal bdGestehungswertIst = Helper.getBigDecimalNull();

					if (aRechnungpositionDtos[i].getRechnungpositionartCNr()
							.equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
						bdGestehungswertIst = berechneGestehungswertEinerRechnungsposition(
								aRechnungpositionDtos[i], theClientDto);
					}

					// je nach Artikelart beruecksichtigen // @todo PJ 4399
					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdGestehungswertIstO = bdGestehungswertIstO
									.add(bdGestehungswertIst);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdGestehungswertIstO = bdGestehungswertIstO
									.add(bdGestehungswertIst);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Ist : "
				+ bdGestehungswertIstO.toString());

		return bdGestehungswertIstO;
	}

	/**
	 * Den Gestehungswert einer Rechnungsposition berechnen. <br>
	 * Ein Gestehungswert existiert nur fuer lagerbewirtschaftete Artikel.
	 * 
	 * @param rechnungpositionDtoI
	 *            die Rechnungsposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGestehungswertEinerRechnungsposition(
			RechnungPositionDto rechnungpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		BigDecimal bdGestehungswert = null;
		try {
			bdGestehungswert = new BigDecimal(0);

			if (enthaeltRechnungspositionLagerbewirtschaftetenArtikel(
					rechnungpositionDtoI.getIId(), theClientDto)) {
				BigDecimal bdGestehungpreisEinerEinheit = new BigDecimal(0);
				BigDecimal bdWertderposition = new BigDecimal(0);

				// CK: Gestehungspreise gibt es nur, wenn es eine zugehoerige
				// Lagerbewegung gibt.
				// Es gibt keine Lagerbewegungen fuer Menge <= 0
				if (rechnungpositionDtoI.getNMenge().doubleValue() > 0) {
					// wenn es keine Serien- oder Chargennummer gibt
					if (rechnungpositionDtoI.getSeriennrChargennrMitMenge() == null
							|| rechnungpositionDtoI
									.getSeriennrChargennrMitMenge().size() == 0) {
						bdGestehungpreisEinerEinheit = getLagerFac()
								.getGestehungspreisEinerAbgangsposition(
										LocaleFac.BELEGART_RECHNUNG,
										rechnungpositionDtoI.getIId(), null);

						bdWertderposition = rechnungpositionDtoI.getNMenge()
								.multiply(bdGestehungpreisEinerEinheit);

						bdGestehungswert = bdGestehungswert
								.add(bdWertderposition);
					}

					// getGestehungpreis() greift auf WW_LAGERBEWEGUNG zu. Hier
					// gibt es pro Serien- oder
					// Chargennummer 1 Datensatz
					else {

						for (int i = 0; i < rechnungpositionDtoI
								.getSeriennrChargennrMitMenge().size(); i++) {
							bdGestehungpreisEinerEinheit = getLagerFac()
									.getGestehungspreisEinerAbgangsposition(
											LocaleFac.BELEGART_RECHNUNG,
											rechnungpositionDtoI.getIId(),
											rechnungpositionDtoI
													.getSeriennrChargennrMitMenge()
													.get(i)
													.getCSeriennrChargennr());

							bdWertderposition = rechnungpositionDtoI
									.getSeriennrChargennrMitMenge().get(i)
									.getNMenge()
									.multiply(bdGestehungpreisEinerEinheit);

							bdGestehungswert = bdGestehungswert
									.add(bdWertderposition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdGestehungswert;
	}

	/**
	 * Feststellen, ob eine Rechnungsposition einen lagerbewirtschafteten
	 * Artikel enthaelt.
	 * 
	 * @param iIdRechnungpositionI
	 *            PK der Rechnungposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return boolean true, wenn die Rechnungposition einen
	 *         lagerbewirtschafteten Artikel enthaelt
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean enthaeltRechnungspositionLagerbewirtschaftetenArtikel(
			Integer iIdRechnungpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdRechnungpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRechnungpositionI == null"));
		}

		boolean bLagerbewirtschaftet = false;

		Rechnungposition rechnungposition = em.find(Rechnungposition.class,
				iIdRechnungpositionI);
		if (rechnungposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (rechnungposition.getArtikelIId() != null) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					rechnungposition.getArtikelIId(), theClientDto);

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				bLagerbewirtschaftet = true;
			}
		}

		return bLagerbewirtschaftet;
	}

	/**
	 * Pruefen, ob die auftragsbezogenen Positionen einer Rechnung auch nach
	 * Auftraegen sortiert sind. d.h. zwischen den Positionen die sich auf einen
	 * Auftrag beziehen, duerfen keine Positionen ohne bzw. anderen
	 * Auftragsbezug stehen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return boolean true: wenn die Sortierung wie gewuenscht ist.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefePositionenAufSortierungNachAuftrag(
			Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		boolean bSortierungOK = true;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungPosition.class);
			// Filter nach Rechnung
			c.add(Restrictions
					.eq(RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID,
							rechnungIId));
			// Sortierung nach I_SORT
			c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
			// Query ausfuehren
			List<?> list = c.list();
			// Wenn es mehr als eine Position gibt.
			if (list.size() > 1) {
				Integer auftragIIdLetzte = null;
				HashMap<Integer, Integer> hmAuftraege = new HashMap<Integer, Integer>(); // Alle
				// Auftrage
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					FLRRechnungPosition rePos = (FLRRechnungPosition) iter
							.next();
					// eine Auftragsbezogene Position
					if (rePos.getFlrpositionensichtauftrag() != null) {
						Integer auftragIId = rePos
								.getFlrpositionensichtauftrag()
								.getAuftrag_i_id();
						// die Sortierung ist dann ok, wenn:
						// a) die auftrags-id kommt das erste mal vor. oder:
						// b) die letzte Position hatte die gleiche auftrags-id
						if (hmAuftraege.get(auftragIId) != null) {
							// die kam also schon mal vor.
							if (auftragIIdLetzte == null
									|| !auftragIId.equals(auftragIIdLetzte)) {
								bSortierungOK = false;
								break;
							}
						}
						auftragIIdLetzte = auftragIId;
						// AuftragsId merken
						hmAuftraege.put(auftragIId, auftragIId);
					} else {
						auftragIIdLetzte = null;
					}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bSortierungOK;
	}

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRRechnungPosition.class);
			crit.add(Restrictions.eq("position_i_id", iIdPositionI));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRRechnungPosition pos = (FLRRechnungPosition) iter.next();
				if (pos.getPositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)
						|| pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE)) {
					wert = wert.add(pos.getN_menge().multiply(
							pos.getN_nettoeinzelpreis()));
				} else if (pos.getPositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					if (pos.getC_zusatzbezeichnung().equals(
							LocaleFac.POSITIONBEZ_BEGINN))
						if (pos.getPosition_i_id() != null) {
							BigDecimal posWert = new BigDecimal(0);
							session = factory.openSession();
							Criteria critPosition = session
									.createCriteria(FLRRechnungPosition.class);
							critPosition.add(Restrictions.eq("position_i_id",
									pos.getI_id()));
							List<?> posList = critPosition.list();
							for (Iterator<?> ipos = posList.iterator(); ipos
									.hasNext();) {
								FLRRechnungPosition item = (FLRRechnungPosition) ipos
										.next();
								if (!pos.getPositionsart_c_nr().equals(
										LocaleFac.POSITIONSART_POSITION)) {
									posWert = posWert
											.add(item
													.getN_menge()
													.multiply(
															item.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()));
								}
							}
							wert = wert.add(posWert);
						}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return wert;
	}

	public String getGutschriftenEinerRechnung(Integer rechnungIId) {
		try {

			TreeMap tmRechnungen = new TreeMap<String, String>();

			// PJ 16987 Gutschriften
			RechnungDto[] aGutschriftenDtos = null;
			Query query = em
					.createNamedQuery("RechnungfindByRechnungIIdZuRechnung");
			query.setParameter(1, rechnungIId);

			Collection<?> cl = query.getResultList();
			aGutschriftenDtos = RechnungDtoAssembler.createDtos(cl);

			for (int k = 0; k < aGutschriftenDtos.length; k++) {
				tmRechnungen.put(aGutschriftenDtos[k].getCNr(),
						aGutschriftenDtos[k].getCNr());
			}

			String cFormat = "";
			int iAnzahl = 0;
			Iterator it = tmRechnungen.keySet().iterator();
			while (it.hasNext()) {
				cFormat += (String) it.next();
				iAnzahl++;

				if (iAnzahl == 5) {
					cFormat += "\n";
					iAnzahl = 0;
				} else {
					cFormat += " | ";
				}
			}

			if (cFormat.length() > 3) {
				cFormat = cFormat.substring(0, cFormat.length() - 3);
			}
			return cFormat;
		} catch (Throwable ex) {
			return null;
		}
	}

	public void sortierePositionenNachAuftrag(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		/**
		 * @todo
		 */
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void createMonatsrechnungen(TheClientDto theClientDto) {

		String sQuery = "SELECT ls"
				+ " FROM FLRLieferschein AS ls WHERE ls.flrkunderechnungsadresse.b_monatsrechnung=1 AND ls.lieferscheinstatus_status_c_nr='"
				+ LocaleFac.STATUS_GELIEFERT + "' AND ls.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY ls.c_nr";

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		while (resultListIterator.hasNext()) {

			FLRLieferschein ls = (FLRLieferschein) resultListIterator.next();
			// Gibt es in diesem Monat schon eine angelegte Rechnung?
			String sQuery2 = "SELECT r"
					+ " FROM FLRRechnung AS r WHERE r.flrkunde.i_id="
					+ ls.getKunde_i_id_rechnungsadresse()
					+ " AND r.status_c_nr='"
					+ LocaleFac.STATUS_ANGELEGT
					+ "' AND r.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND r.d_belegdatum>='"
					+ Helper.formatDateWithSlashes(Helper
							.cutDate(new java.sql.Date(c.getTimeInMillis())))
					+ "' AND r.flrrechnungart.c_nr='"
					+ RechnungFac.RECHNUNGART_RECHNUNG
					+ "' ORDER BY r.c_nr DESC";
			Session session2 = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query rechnungen = session2.createQuery(sQuery2);
			rechnungen.setMaxResults(1);
			List<?> resultListR = rechnungen.list();

			Integer letzteRechnungIId = null;

			Integer lagerIId = null;

			if (resultListR.iterator().hasNext()) {
				// Bestehende verwenden
				FLRRechnung r = (FLRRechnung) resultListR.iterator().next();
				letzteRechnungIId = r.getI_id();
			} else {
				// neue anlegen

				RechnungDto reDto = new RechnungDto();
				reDto.setKundeIId(ls.getKunde_i_id_rechnungsadresse());
				reDto.setKundeIIdStatistikadresse(ls
						.getKunde_i_id_rechnungsadresse());

				reDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);

				reDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

				reDto.setLagerIId(ls.getLager_i_id());

				reDto.setWaehrungCNr(theClientDto.getSMandantenwaehrung());
				reDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
						1));

				reDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System
						.currentTimeMillis())));
				reDto.setMandantCNr(theClientDto.getMandant());

				reDto.setKostenstelleIId(ls.getKostenstelle_i_id());

				reDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

				reDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
				reDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
				reDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
				try {
					letzteRechnungIId = getRechnungFac().createRechnung(reDto,
							theClientDto).getIId();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			// Nun Lieferschein als Rechnungsposition anlegen
			RechnungPositionDto rechnungPositionDto = new RechnungPositionDto();
			rechnungPositionDto.setArtikelIId(null);
			rechnungPositionDto.setAuftragpositionIId(null);
			rechnungPositionDto.setBDrucken(Helper.boolean2Short(true));
			rechnungPositionDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));
			rechnungPositionDto.setBMwstsatzuebersteuert(Helper
					.boolean2Short(false));
			rechnungPositionDto.setCBez(null);
			rechnungPositionDto.setEinheitCNr(null);
			rechnungPositionDto.setBNettopreisuebersteuert(Helper
					.boolean2Short(false));

			rechnungPositionDto.setFRabattsatz(new Double(0));
			rechnungPositionDto.setISort(new Integer(0));

			rechnungPositionDto.setLieferscheinIId(ls.getI_id());
			rechnungPositionDto.setNMenge(null);
			rechnungPositionDto.setRechnungIId(letzteRechnungIId);
			rechnungPositionDto.setRechnungIIdGutschrift(null);
			rechnungPositionDto.setXTextinhalt(null);
			rechnungPositionDto
					.setRechnungpositionartCNr(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN);
			rechnungPositionDto.setCZusatzbez(null);

			// hier brauch ich kein lager, da nix gebucht wird
			try {
				getRechnungFac().createRechnungPosition(rechnungPositionDto,
						null, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			session2.close();

		}
	}

	public void createRechnung(RechnungDto rechnungDto) throws EJBExceptionLP {
		if (rechnungDto == null) {
			return;
		}
		try {
			Rechnung rechnung = new Rechnung(rechnungDto.getIId(),
					rechnungDto.getMandantCNr(),
					rechnungDto.getIGeschaeftsjahr(), rechnungDto.getCNr(),
					rechnungDto.getKundeIId(), rechnungDto.getTBelegdatum(),
					rechnungDto.getStatusCNr(),
					rechnungDto.getRechnungartCNr(),
					rechnungDto.getKostenstelleIId(),
					rechnungDto.getWaehrungCNr(),
					rechnungDto.getBMwstallepositionen(),
					rechnungDto.getPersonalIIdAnlegen(),
					rechnungDto.getPersonalIIdAendern(),
					rechnungDto.getBReversecharge(),
					rechnungDto.getPersonalIIdVertreter(),
					rechnungDto.getPersonalIIdVertreter(),
					rechnungDto.getLieferartIId(),
					rechnungDto.getZahlungszielIId(),
					rechnungDto.getSpediteurIId());

			em.persist(rechnung);
			em.flush();
			setRechnungFromRechnungDto(rechnung, rechnungDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeRechnung(Integer iId) throws EJBExceptionLP {
		try {
			Rechnung toRemove = em.find(Rechnung.class, iId);
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

	public void removeRechnung(RechnungDto rechnungDto) throws EJBExceptionLP {
		if (rechnungDto != null) {
			Integer iId = rechnungDto.getIId();
			removeRechnung(iId);
		}
	}

	public void updateRechnungZahlungsplan(Integer rechnungIId,
			BigDecimal bdZahlbetrag, Integer iZahltag) {

		try {
			Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
			rechnung.setNMtlZahlbetrag(bdZahlbetrag);
			rechnung.setIZahltagMtlZahlbetrag(iZahltag);
			em.merge(rechnung);
			em.flush();

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}

	}

	public void updateRechnungStatus(Integer rechnungIId, String statusNeu,
			java.sql.Date bezahltdatum) throws EJBExceptionLP {
		if (rechnungIId != null) {

			try {
				Rechnung rechnung = em.find(Rechnung.class, rechnungIId);

				if ((rechnung.getStatusCNr().equals(RechnungFac.STATUS_OFFEN) || rechnung
						.getStatusCNr().equals(RechnungFac.STATUS_TEILBEZAHLT))
						&& (statusNeu.equals(RechnungFac.STATUS_TEILBEZAHLT) || statusNeu
								.equals(RechnungFac.STATUS_BEZAHLT))) {

					if (statusNeu.equals(RechnungFac.STATUS_BEZAHLT)) {
						rechnung.setTBezahltdatum(bezahltdatum);
					}

					rechnung.setStatusCNr(statusNeu);

				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_GS_STATUSAENDERUNG_AUF_BEZAHLT_NICHT_ERLAUBT,
							new Exception(
									"FEHLER_RECHNUNG_GS_STATUSAENDERUNG_AUF_BEZAHLT_NICHT_ERLAUBT"));

				}

			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateRechnungVertreter(Integer rechnungIId,
			Integer personalIIdVertreter_Neu, TheClientDto theClientDto) {
		if (rechnungIId != null) {
			Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
			rechnung.setPersonalIIdVertreter(personalIIdVertreter_Neu);
			rechnung.setPersonalIIdAendern(theClientDto.getIDPersonal());
			rechnung.setTAendern(getTimestamp());
		}
	}

	public void updateRechnung(RechnungDto rechnungDto) throws EJBExceptionLP {
		if (rechnungDto != null) {
			pruefeAnzahlungSchlusszahlung(rechnungDto);
			Integer iId = rechnungDto.getIId();
			try {
				Rechnung rechnung = em.find(Rechnung.class, iId);
				setRechnungFromRechnungDto(rechnung, rechnungDto);
				// rechnung.setPersonalIIdAendern(theClientDto.getIDPersonal());
				rechnung.setTAendern(getTimestamp());
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateRechnungs(RechnungDto[] rechnungDtos)
			throws EJBExceptionLP {
		if (rechnungDtos != null) {
			for (int i = 0; i < rechnungDtos.length; i++) {
				updateRechnung(rechnungDtos[i]);
			}
		}
	}

	public RechnungDto[] rechnungFindByMandantBelegdatumVonBis(
			String mandantCNr, Date dVon, Date dBis) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByMandantBelegdatumVonBis");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, dVon);
			query.setParameter(3, dBis);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public RechnungDto[] rechnungFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCNr) {
		Query query = em.createNamedQuery("RechnungfindByCNrMandant");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		Collection<?> list = query.getResultList();
		return assembleRechnungDtos(list);
	}

	// public RechnungDto rechnungFindByCNrMandant(String cNr, String
	// mandantCNr)
	// throws EJBExceptionLP {
	// try {
	// RechnungDto rechnung = rechnungFindByCNrMandantCNrOhneExc(cNr,
	// mandantCNr);
	// if (rechnung == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
	// }
	// return rechnung;
	// } catch (Exception e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
	// }
	// }

	public RechnungDto[] rechnungFindByRechnungIIdZuRechnung(Integer rechnungIId) {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByRechnungIIdZuRechnung");
			query.setParameter(1, rechnungIId);
			Collection c = query.getResultList();

			return assembleRechnungDtos(c);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public RechnungDto[] rechnungFindByAnsprechpartnerIId(
			Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungfindByAnsprechpartnerIId");
			query.setParameter(1, iAnsprechpartnerIId);
			Collection<?> cl = query.getResultList();
			return assembleRechnungDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public RechnungPositionDto[] getRechnungPositionenByRechnungIId(
			Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<RechnungPositionDto> dtos = new ArrayList<RechnungPositionDto>();
		RechnungPositionDto[] positionDtos = null;
		Session sesion = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			sesion = factory.openSession();
			Criteria crit = sesion.createCriteria(FLRRechnungPosition.class);
			crit.add(Restrictions.eq("flrrechnung.i_id", iIdRechnungI));
			crit.add(Restrictions.isNull("position_i_id"));
			crit.addOrder(Order.asc("i_sort"));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRRechnungPosition pos = (FLRRechnungPosition) iter.next();
				RechnungPositionDto apositionDto = rechnungPositionFindByPrimaryKey(pos
						.getI_id());
				if (pos.getPositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					if (!pos.getC_zusatzbezeichnung().equals(
							LocaleFac.POSITIONBEZ_ENDE)) {
						dtos.add(apositionDto);
					}
				} else {
					dtos.add(apositionDto);
				}
			}

		} finally {
			if (sesion != null) {
				sesion.close();
			}
		}
		positionDtos = new RechnungPositionDto[dtos.size()];
		for (int i = 0; i < dtos.size(); i++) {
			positionDtos[i] = dtos.get(i);
		}
		return positionDtos;
	}

	public void createRechnungzahlung(RechnungzahlungDto rechnungzahlungDto)
			throws EJBExceptionLP {
		if (rechnungzahlungDto == null) {
			return;
		}
		try {
			Rechnungzahlung rechnungzahlung = new Rechnungzahlung(
					rechnungzahlungDto.getIId(),
					rechnungzahlungDto.getRechnungIId(),
					rechnungzahlungDto.getDZahldatum(),
					rechnungzahlungDto.getZahlungsartCNr(),
					rechnungzahlungDto.getNKurs(),
					rechnungzahlungDto.getNBetrag(),
					rechnungzahlungDto.getNBetragfw(),
					rechnungzahlungDto.getNBetragUst(),
					rechnungzahlungDto.getNBetragUstfw(),
					rechnungzahlungDto.getPersonalIIdAnlegen(),
					rechnungzahlungDto.getPersonalIIdAendern());
			em.persist(rechnungzahlung);
			em.flush();
			setRechnungzahlungFromRechnungzahlungDto(rechnungzahlung,
					rechnungzahlungDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeRechnungzahlung(Integer iId) throws EJBExceptionLP {
		try {
			Rechnungzahlung toRemove = em.find(Rechnungzahlung.class, iId);
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

	public void removeRechnungzahlung(RechnungzahlungDto rechnungzahlungDto)
			throws EJBExceptionLP {
		if (rechnungzahlungDto != null) {
			Integer iId = rechnungzahlungDto.getIId();
			removeRechnungzahlung(iId);
		}
	}

	public void updateRechnungzahlung(RechnungzahlungDto rechnungzahlungDto)
			throws EJBExceptionLP {
		if (rechnungzahlungDto != null) {
			Integer iId = rechnungzahlungDto.getIId();
			try {
				Rechnungzahlung rechnungzahlung = em.find(
						Rechnungzahlung.class, iId);
				setRechnungzahlungFromRechnungzahlungDto(rechnungzahlung,
						rechnungzahlungDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateRechnungzahlungs(RechnungzahlungDto[] rechnungzahlungDtos)
			throws EJBExceptionLP {
		if (rechnungzahlungDtos != null) {
			for (int i = 0; i < rechnungzahlungDtos.length; i++) {
				updateRechnungzahlung(rechnungzahlungDtos[i]);
			}
		}
	}

	public RechnungzahlungDto rechnungzahlungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {

			Rechnungzahlung rechnungzahlung = em.find(Rechnungzahlung.class,
					iId);
			if (rechnungzahlung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleRechnungzahlungDto(rechnungzahlung);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public RechnungzahlungDto[] zahlungFindByRechnungIIdAbsteigendSortiert(
			Integer rechnungIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("RechnungzahlungfindByRechnungIIdAbsteigendSortiert");
			query.setParameter(1, rechnungIId);
			Collection<?> cl = query.getResultList();
			return assembleRechnungzahlungDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> rechnungzahlungIdsByMandantZahldatumVonBis(
			String mandantCNr, Date dVon, Date dBis) {
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRRechnungZahlung.class);
		crit.createAlias("flrrechnung", "r");
		crit.add(Restrictions.eq("r.mandant_c_nr", mandantCNr));
		crit.add(Restrictions.between("d_zahldatum", dVon, dBis));
		List<FLRRechnungZahlung> list = crit.list();
		List<Integer> ids = new ArrayList<Integer>();
		Iterator<FLRRechnungZahlung> it = list.iterator();
		while (it.hasNext())
			ids.add(it.next().getI_id());
		session.close();
		return ids;
	}

	private void setRechnungzahlungFromRechnungzahlungDto(
			Rechnungzahlung rechnungzahlung,
			RechnungzahlungDto rechnungzahlungDto) {
		rechnungzahlung.setRechnungIId(rechnungzahlungDto.getRechnungIId());
		rechnungzahlung.setTZahldatum(rechnungzahlungDto.getDZahldatum());
		rechnungzahlung.setZahlungsartCNr(rechnungzahlungDto
				.getZahlungsartCNr());
		rechnungzahlung.setBankverbindungIId(rechnungzahlungDto
				.getBankkontoIId());
		rechnungzahlung.setKassenbuchIId(rechnungzahlungDto.getKassenbuchIId());
		rechnungzahlung.setRechnungIIdGutschrift(rechnungzahlungDto
				.getRechnungIIdGutschrift());
		rechnungzahlung.setIAuszug(rechnungzahlungDto.getIAuszug());
		rechnungzahlung.setNKurs(rechnungzahlungDto.getNKurs());
		rechnungzahlung.setNBetrag(rechnungzahlungDto.getNBetrag());
		rechnungzahlung.setNBetragfw(rechnungzahlungDto.getNBetragfw());
		rechnungzahlung.setNBetragust(rechnungzahlungDto.getNBetragUst());
		rechnungzahlung.setNBetragustfw(rechnungzahlungDto.getNBetragUstfw());
		rechnungzahlung.setTWechselfaelligam(rechnungzahlungDto
				.getDWechselFaelligAm());
		rechnungzahlung.setTAnlegen(rechnungzahlungDto.getTAnlegen());
		rechnungzahlung.setPersonalIIdAnlegen(rechnungzahlungDto
				.getPersonalIIdAnlegen());
		rechnungzahlung.setTAendern(rechnungzahlungDto.getTAendern());
		rechnungzahlung.setPersonalIIdAendern(rechnungzahlungDto
				.getPersonalIIdAendern());
		rechnungzahlung.setFSkonto(rechnungzahlungDto.getFSkonto());
		rechnungzahlung.setRechnungzahlungIIdGutschrift(rechnungzahlungDto
				.getRechnungzahlungIIdGutschrift());
		rechnungzahlung.setEingangsrechnungIId(rechnungzahlungDto
				.getEingangsrechnungIId());
		rechnungzahlung.setBuchungdetailIId(rechnungzahlungDto
				.getBuchungdetailIId());

		em.merge(rechnungzahlung);
		em.flush();
	}

	private RechnungzahlungDto assembleRechnungzahlungDto(
			Rechnungzahlung rechnungzahlung) {
		return RechnungzahlungDtoAssembler.createDto(rechnungzahlung);
	}

	private RechnungzahlungDto[] assembleRechnungzahlungDtos(
			Collection<?> rechnungzahlungs) {
		List<RechnungzahlungDto> list = new ArrayList<RechnungzahlungDto>();
		if (rechnungzahlungs != null) {
			Iterator<?> iterator = rechnungzahlungs.iterator();
			while (iterator.hasNext()) {
				Rechnungzahlung rechnungzahlung = (Rechnungzahlung) iterator
						.next();
				list.add(assembleRechnungzahlungDto(rechnungzahlung));
			}
		}
		RechnungzahlungDto[] returnArray = new RechnungzahlungDto[list.size()];
		return (RechnungzahlungDto[]) list.toArray(returnArray);
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionIIdFromPositionNummer(rechnungIId,
				position, new RechnungPositionNumberAdapter(em));

		// Integer foundPosition = 0 ;
		//
		// try {
		// Query query =
		// em.createNamedQuery("RechnungPositionfindByRechnungIId");
		// query.setParameter(1, rechnungIId);
		// Collection<Rechnungposition> cl = query.getResultList();
		// Iterator<?> iterator = cl.iterator();
		// while (iterator.hasNext()) {
		// Rechnungposition pos = (Rechnungposition) iterator.next();
		//
		// if(pos.getPositionIId() == null && hasPositionNummer(pos)) {
		// ++foundPosition ;
		// if(foundPosition.equals(position)) return pos.getIId() ;
		// }
		// }
		// } catch (NoResultException er) {
		// return null;
		// }
		//
		// return null ;
	}

	public Integer getPositionNummer(Integer reposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionNummer(reposIId,
				new RechnungPositionNumberAdapter(em));
	}

	public Integer getLastPositionNummer(Integer reposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(reposIId,
				new RechnungPositionNumberAdapter(em));
	}

	public Integer getHighestPositionNumber(Integer rechnungIId)
			throws EJBExceptionLP {
		RechnungPositionDto reposDtos[] = rechnungPositionFindByRechnungIId(rechnungIId);
		if (reposDtos.length == 0)
			return 0;

		return getLastPositionNummer(reposDtos[reposDtos.length - 1].getIId());
	}

	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		RechnungPositionDto dtos[] = rechnungPositionFindByRechnungIId(rechnungIId);

		return getBelegVerkaufFac().pruefeAufGleichenMwstSatz(dtos,
				vonPositionNumber, bisPositionNumber);
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer rechnungIId,
			String druckart) {
		if (rechnungIId != null) {
			Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
			rechnung.setTVersandzeitpunkt(new Timestamp(System
					.currentTimeMillis()));
			rechnung.setCVersandtype(druckart);
			em.merge(rechnung);
			em.flush();
		}

	}

	public void verbucheRechnungNeu(Integer iRechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		pruefeUpdateAufRechnungErlaubt(iRechnungIId, theClientDto);

		RechnungzahlungDto[] zahlungDtos = zahlungFindByRechnungIId(iRechnungIId);
		// rueckbuchen
		for (int i = 0; i < zahlungDtos.length; i++) {
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlungRueckgaengig(zahlungDtos[i], theClientDto);
		}
		getBelegbuchungFac(theClientDto.getMandant())
				.verbucheRechnungRueckgaengig(iRechnungIId, theClientDto);
		// neu verbuchen
		BuchungDto buchungDto = getBelegbuchungFac(theClientDto.getMandant())
				.verbucheRechnung(iRechnungIId, theClientDto);
		if (buchungDto != null) {
			// eventuell ist der Beleg aus einem anderen GJ, daher hier das GJ
			// der Buchung ev. updaten
			Rechnung rechnung = em.find(Rechnung.class, iRechnungIId);
			if (!buchungDto.getIGeschaeftsjahr().equals(
					rechnung.getIGeschaeftsjahr())) {
				buchungDto.setIGeschaeftsjahr(rechnung.getIGeschaeftsjahr());
				getBuchenFac().updateBuchung(buchungDto, theClientDto);
			}
		}
		for (int i = 0; i < zahlungDtos.length; i++) {
			buchungDto = getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlung(zahlungDtos[i].getIId(), theClientDto);
			if (buchungDto != null) {
				// eventuell ist der Beleg aus einem anderen GJ, daher hier das
				// GJ der Buchung ev. updaten
				Integer iGeschaeftsjahr = getBuchenFac()
						.findGeschaeftsjahrFuerDatum(
								zahlungDtos[i].getDZahldatum(),
								theClientDto.getMandant());
				if (iGeschaeftsjahr == null)
					iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
							theClientDto.getMandant(),
							zahlungDtos[i].getDZahldatum());
				if (!buchungDto.getIGeschaeftsjahr().equals(iGeschaeftsjahr)) {
					buchungDto.setIGeschaeftsjahr(iGeschaeftsjahr);
					getBuchenFac().updateBuchung(buchungDto, theClientDto);
				}
			}
		}
	}

	public void verbucheGutschriftNeu(Integer iRechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		pruefeUpdateAufRechnungErlaubt(iRechnungIId, theClientDto);

		RechnungzahlungDto[] zahlungDtos = zahlungFindByRechnungIId(iRechnungIId);
		// rueckbuchen
		for (int i = 0; i < zahlungDtos.length; i++) {
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlungRueckgaengig(zahlungDtos[i], theClientDto);
		}
		getBelegbuchungFac(theClientDto.getMandant())
				.verbucheGutschriftRueckgaengig(iRechnungIId, theClientDto);
		// neu verbuchen
		BuchungDto buchungDto = getBelegbuchungFac(theClientDto.getMandant())
				.verbucheRechnung(iRechnungIId, theClientDto);
		if (buchungDto != null) {
			// eventuell ist der Beleg aus einem anderen GJ, daher hier das GJ
			// der Buchung ev. updaten
			Rechnung rechnung = em.find(Rechnung.class, iRechnungIId);
			if (!buchungDto.getIGeschaeftsjahr().equals(
					rechnung.getIGeschaeftsjahr())) {
				buchungDto.setIGeschaeftsjahr(rechnung.getIGeschaeftsjahr());
				getBuchenFac().updateBuchung(buchungDto, theClientDto);
			}
		}
		for (int i = 0; i < zahlungDtos.length; i++) {
			buchungDto = getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlung(zahlungDtos[i].getIId(), theClientDto);
			if (buchungDto != null) {
				// eventuell ist der Beleg aus einem anderen GJ, daher hier das
				// GJ der Buchung ev. updaten
				Integer iGeschaeftsjahr = getBuchenFac()
						.findGeschaeftsjahrFuerDatum(
								zahlungDtos[i].getDZahldatum(),
								theClientDto.getMandant());
				if (iGeschaeftsjahr == null)
					iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
							theClientDto.getMandant(),
							zahlungDtos[i].getDZahldatum());
				if (!buchungDto.getIGeschaeftsjahr().equals(iGeschaeftsjahr)) {
					buchungDto.setIGeschaeftsjahr(iGeschaeftsjahr);
					getBuchenFac().updateBuchung(buchungDto, theClientDto);
				}
			}
		}
	}

	public BigDecimal getBezahltKursdifferenzBetrag(Integer rechnungIId,
			BigDecimal kurs) {
		BigDecimal wert = new BigDecimal(0);
		RechnungzahlungDto[] zahlungen = null;
		try {
			zahlungen = zahlungFindByRechnungIId(rechnungIId);
		} catch (Exception ex) {
			// wenns keine gibt
			return wert;
		}
		for (int i = 0; i < zahlungen.length; i++) {
			BigDecimal betragRechnungkurs = zahlungen[i]
					.getNBetragfw()
					.add(zahlungen[i].getNBetragUstfw())
					.divide(kurs, FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
			wert = wert.add(betragRechnungkurs.subtract(zahlungen[i]
					.getNBetrag().add(zahlungen[i].getNBetragUst())));
		}
		return wert;
	}

	private boolean hasSeriennrchargennr(List<SeriennrChargennrMitMengeDto> snrs) {
		if (null == snrs)
			return false;
		if (snrs.size() == 0)
			return false;

		if (snrs.size() > 1)
			return true;

		List<GeraetesnrDto> geraete = snrs.get(0).getAlGeraetesnr();
		if (geraete != null && geraete.size() > 0)
			return true;

		return snrs.get(0).getCSeriennrChargennr() != null;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(
			Integer rechnungposIId) throws EJBExceptionLP {
		if (rechnungposIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("rechnungposIId == null"));
		}

		List<SeriennrChargennrMitMengeDto> allSnrs = new ArrayList<SeriennrChargennrMitMengeDto>();
		List<Rechnungposition> positions = RechnungpositionQuery
				.listByPositionIIdArtikelset(em, rechnungposIId);
		for (Rechnungposition rechnungposition : positions) {
			List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
							LocaleFac.BELEGART_RECHNUNG,
							rechnungposition.getIId());
			if (hasSeriennrchargennr(snrs)) {
				allSnrs.addAll(snrs);
			}
		}

		return allSnrs;
	}

	public java.util.List<Timestamp> getAenderungsZeitpunkte(Integer iid)
			throws EJBExceptionLP, RemoteException {
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		RechnungDto r = rechnungFindByPrimaryKey(iid);

		timestamps.add(r.getTAendern());
		timestamps.add(r.getTStorniert());
		timestamps.add(r.getTManuellerledigt());

		return timestamps;
	}

	// PJ2276
	public void repairRechnungZws2276(Integer rechnungId,
			TheClientDto theClientDto) {
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(rechnungId);
		if (RechnungFac.STATUS_STORNIERT.equals(rechnungDto.getStatusCNr()))
			return;

		RechnungPositionDto[] rePosDto = rechnungPositionFindByRechnungIId(rechnungDto
				.getIId());
		getBelegVerkaufFac().prepareIntZwsPositions(rePosDto);
		Set<Integer> modifiedPositions = getBelegVerkaufFac()
				.adaptIntZwsPositions(rePosDto);
		for (Integer index : modifiedPositions) {
			Rechnungposition rechnungposition = em.find(Rechnungposition.class,
					rePosDto[index].getIId());
			rechnungposition
					.setNNettoeinzelpreisplusaufschlagminusrabatt(rePosDto[index]
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			em.merge(rechnungposition);
			em.flush();

			if (LocaleFac.POSITIONSART_IDENT.equals(rechnungposition
					.getPositionsartCNr())) {
				RechnungPositionDto rechnungPositionDto = rechnungPositionFindByPrimaryKey(rechnungposition
						.getIId());
				bucheRechnungPositionAmLager(rechnungPositionDto,
						rechnungDto.getLagerIId(), false, theClientDto);
			}
		}
	}

	public List<Integer> repairRechnungZws2276GetList(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT DISTINCT rechnung_i_id "
				+ " FROM FLRRechnungPosition AS repos WHERE repos.positionsart_c_nr = 'IZwischensumme'";

		org.hibernate.Query rechnungIdsQuery = session.createQuery(sQuery);

		List<Integer> resultList = new ArrayList<Integer>();
		List<Integer> queryList = (List<Integer>) rechnungIdsQuery.list();
		for (Integer rechnung_i_id : queryList) {
			Rechnung rechnung = em.find(Rechnung.class, rechnung_i_id);
			if (theClientDto.getMandant().equals(rechnung.getMandantCNr())) {
				resultList.add(rechnung_i_id);
			}
		}

		session.close();
		return resultList;
	}

	@Override
	public RechnungzahlungDto createUpdateZahlung(
			RechnungzahlungDto rechnungZahlungDto, boolean rechnungErledigt,
			RechnungzahlungDto gutschriftZahlungDto,
			boolean gutschriftErledigt, TheClientDto theClientDto) {
		Validator.notNull(rechnungZahlungDto, "rechnungZahlungDto");

		if (gutschriftZahlungDto != null) {
			if (!rechnungZahlungDto.isGutschrift()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_GUTSCHRIFTZAHLUNG_OHNE_GUTSCHRIFTART,
						rechnungZahlungDto.getRechnungIId().toString());
			}

			if (gutschriftZahlungDto.getIId() == null) {
				gutschriftZahlungDto = createZahlung(gutschriftZahlungDto,
						gutschriftErledigt, theClientDto);
			} else {
				updateZahlung(gutschriftZahlungDto, gutschriftErledigt,
						theClientDto);
			}

			rechnungZahlungDto
					.setRechnungzahlungIIdGutschrift(gutschriftZahlungDto
							.getIId());
			// Gutschrift.rechnungId == Gutschrift-Id
			rechnungZahlungDto.setRechnungIIdGutschrift(gutschriftZahlungDto
					.getRechnungIId());
		}

		if (rechnungZahlungDto.getIId() == null) {
			rechnungZahlungDto = createZahlung(rechnungZahlungDto,
					rechnungErledigt, theClientDto);
		} else {
			updateZahlung(rechnungZahlungDto, rechnungErledigt, theClientDto);
		}

		return rechnungZahlungDto;
	}

	@Override
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneAktiviereControlled(this, iid,
				theClientDto);
		// new BelegAktivierungController(this).berechneAktiviereControlled(iid,
		// theClientDto) ;
	}

	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		RechnungDto rechnungDto = rechnungFindByPrimaryKey(iid);
		rechnungDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		return rechnungDto;
	}

	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return rechnungPositionFindByRechnungIId(iid);
	}

	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return ((RechnungDto) belegDto).getKundeIId();
	}
}
