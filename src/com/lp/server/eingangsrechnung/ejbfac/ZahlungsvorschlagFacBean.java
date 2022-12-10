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
package com.lp.server.eingangsrechnung.ejbfac;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.eingangsrechnung.assembler.ZahlungsvorschlagDtoAssembler;
import com.lp.server.eingangsrechnung.assembler.ZahlungsvorschlaglaufDtoAssembler;
import com.lp.server.eingangsrechnung.bl.ZahlungsvorschlagExportFormatter;
import com.lp.server.eingangsrechnung.bl.ZahlungsvorschlagExportFormatterFactory;
import com.lp.server.eingangsrechnung.ejb.Zahlungsvorschlag;
import com.lp.server.eingangsrechnung.ejb.ZahlungsvorschlagQuery;
import com.lp.server.eingangsrechnung.ejb.Zahlungsvorschlaglauf;
import com.lp.server.eingangsrechnung.ejb.ZahlungsvorschlaglaufQuery;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlag;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagkriterienDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeSepaExportZahlungsvorschlaglauf;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ZahlungsvorschlagFacBean extends Facade implements
		ZahlungsvorschlagFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createZahlungsvorschlag(ZahlungsvorschlagkriterienDto krit,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iZViid = null;
		Session session = null;
		try {
			
			
			
			
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

			int iERPruefen=(Integer)parameter.getCWertAsObject();
			
			
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session
					.createCriteria(FLREingangsrechnungReport.class);
			// Es werden alle offenen ERs dieses Mandanten ausgewertet.
			// 1. Filter nach Mandant
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
					theClientDto.getMandant()));
			// 2. Filter nach Status
			Collection<String> collStati = new LinkedList<String>();
			collStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
			collStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
			c.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR,
					collStati));
			// 3. Filter nach Waehrung (PJ18093)
			if (krit.getWaehrungCNr() != null) {
				c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_WAEHRUNG_C_NR,
						krit.getWaehrungCNr()));
			}
			// Abfragen
			List<?> list = c.list();
			if (list.size() > 0) {
				// wenn da welche daherkommen, mach ich einen neuen lauf
				ZahlungsvorschlaglaufDto zvlaufDto = new ZahlungsvorschlaglaufDto();
				zvlaufDto.setBankverbindungIId(krit.getBankvertbindungIId());
				zvlaufDto.setBMitskonto(Helper.boolean2Short(krit
						.isBMitSkonto()));
				zvlaufDto.setISkontoueberziehungsfristintagen(krit
						.getISkontoUeberziehungsfristInTagen());
				zvlaufDto.setMandantCNr(theClientDto.getMandant());
				zvlaufDto.setTNaechsterzahlungslauf(krit
						.getDNaechsterZahlungslauf());
				zvlaufDto.setTZahlungsstichtag(krit.getDZahlungsstichtag());
				iZViid = createZahlungsvorschlaglauf(zvlaufDto, theClientDto);
				// Zahlungsziele cachen
				final Map<Integer, ZahlungszielDto> mZahlungsziele = (Map<Integer, ZahlungszielDto>) getMandantFac()
						.zahlungszielFindAllByMandantAsDto(
								theClientDto.getMandant(), theClientDto);
				BankverbindungDto bankverbindungDto = getFinanzFac().bankverbindungFindByPrimaryKey(
						krit.getBankvertbindungIId());
				//PJ19874
				boolean isGeldTransitKonto = bankverbindungDto.isbAlsGeldtransitkonto();
				
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					FLREingangsrechnungReport e = (FLREingangsrechnungReport) iter
							.next();
					// Offenen Werte ermitteln
					BigDecimal bdBruttowertFW = e.getN_betragfw();
					BigDecimal bdBezahltFW = getEingangsrechnungFac()
							.getBezahltBetragFw(e.getI_id(), null);

					if (e.getEingangsrechnungart_c_nr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
						bdBezahltFW = bdBezahltFW.add(getEingangsrechnungFac()
								.getAnzahlungenBezahltZuSchlussrechnungFw(
										e.getI_id()));
					}

					if (!isGeldTransitKonto) {
						// Vorgaenger holen
						Session sessionV = FLRSessionFactory.getFactory()
								.openSession();
						String queryString = "SELECT zv FROM FLRZahlungsvorschlag as zv WHERE zv.eingangsrechnung_i_id="
								+ e.getI_id()
								+ " AND zv.flrzahlungsvorschlaglauf.t_zahlungsstichtag<='"
								+ Helper.formatDateWithSlashes(krit
										.getDZahlungsstichtag())
								+ "' ORDER BY zv.flrzahlungsvorschlaglauf.t_anlegen DESC";
	
						org.hibernate.Query query = sessionV
								.createQuery(queryString);
	
						List<?> results = query.list();
						Iterator<?> resultListIterator = results.iterator();
	
						boolean bNichtMehrRelevant = false;
	
						while (resultListIterator.hasNext()) {
							// Wenn mindestens einer freigegeben ist und
							// vollstaendig bezahlt, dann auslassen
							FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) resultListIterator
									.next();
							if (Helper.short2boolean(zv.getB_bezahlen())) {
								bdBezahltFW = bdBezahltFW.add(zv.getN_zahlbetrag());
							}
	
							if (Helper.short2boolean(zv
									.getB_waere_vollstaendig_bezahlt())
									&& Helper.short2boolean(zv.getB_bezahlen())) {
								bNichtMehrRelevant = true;
							}
	
						}
						sessionV.close();
	
						if (bNichtMehrRelevant == true) {
							continue;
						}
					}
					
					// Wenn noch was offen ist
					if (bdBruttowertFW.subtract(bdBezahltFW).compareTo(
							new BigDecimal(0)) > 0
							|| bdBruttowertFW.doubleValue() < 0) {
						// Faelligkeitsdatum berechnen
						// das ist grundsaetzlich Freigabedatum + Zahlungsziel
						// (sofern definiert)
						java.util.Date dFaellig = e.getT_freigabedatum();

						int iZielTage = 0;
						BigDecimal bdAngewandterSkontoSatz = new BigDecimal(0);
						if (e.getZahlungsziel_i_id() != null) {
							ZahlungszielDto zzDto = mZahlungsziele.get(e
									.getZahlungsziel_i_id());

							// PJ18762
							if (Helper.short2boolean(zzDto
									.getBInzahlungsvorschlagberuecksichtigen()) == false) {
								continue;
							}

							// PJ18305
							if (Helper.short2boolean(zzDto.getBStichtag()) == true) {
								dFaellig = getMandantFac()
										.berechneFaelligkeitAnhandStichtag(
												dFaellig, zzDto, theClientDto);

								zzDto.setAnzahlZieltageFuerNetto(0);
								if (zzDto.getSkontoAnzahlTage1() != null) {
									zzDto.setSkontoAnzahlTage1(-zzDto
											.getSkontoAnzahlTage1());
								}
								if (zzDto.getSkontoAnzahlTage2() != null) {
									zzDto.setSkontoAnzahlTage2(-zzDto
											.getSkontoAnzahlTage2());
								}

							}

							// mit Skonto zahlen
							if (krit.isBMitSkonto()) {
								// Skontoueberziehungstage einrechnen
								java.util.Date dFaelligSkontoueberziehung = Helper.addiereTageZuDatum(dFaellig,
												krit.getISkontoUeberziehungsfristInTagen());
								boolean bSkontoGehtSichAus = false;

								// schaun, ob sich der erste Skonto noch ausgeht
								if (zzDto.getSkontoAnzahlTage1() != null
										&& zzDto.getSkontoProzentsatz1() != null) {
									// Bei diesem Datum waere mit Skonto1 zu
									// zahlen
									java.util.Date dFaelligBeiSkonto1 = Helper
											.addiereTageZuDatum(dFaelligSkontoueberziehung, zzDto
													.getSkontoAnzahlTage1());
									// wenn das spaetestens heute ist ->
									// anwenden
									if (dFaelligBeiSkonto1.compareTo(Helper
											.cutDate(krit.getDZahlungsstichtag())) >= 0) {
										bSkontoGehtSichAus = true;
										bdAngewandterSkontoSatz = zzDto
												.getSkontoProzentsatz1();
										iZielTage = zzDto
												.getSkontoAnzahlTage1();
									}
								}
								// wenn nicht, dann schaun, ob sich der zweite
								// ausgeht
								if (!bSkontoGehtSichAus
										&& zzDto.getSkontoAnzahlTage2() != null
										&& zzDto.getSkontoProzentsatz2() != null) {
									// Bei diesem Datum waere mit Skonto2 zu
									// zahlen
									java.util.Date dFaelligBeiSkonto2 = Helper
											.addiereTageZuDatum(dFaelligSkontoueberziehung, zzDto
													.getSkontoAnzahlTage2());
									// wenn das spaetestens heute ist ->
									// anwenden
									if (dFaelligBeiSkonto2.compareTo(Helper
											.cutDate(krit.getDZahlungsstichtag())) >= 0) {
										bSkontoGehtSichAus = true;
										bdAngewandterSkontoSatz = zzDto
												.getSkontoProzentsatz2();
										iZielTage = zzDto
												.getSkontoAnzahlTage2();
									}
								}
								
								if (bSkontoGehtSichAus) {
									dFaellig = dFaelligSkontoueberziehung;
								}
								// wenn auch das nicht gegangen ist, muss
								// sowieso netto gezahlt werden
								if (!bSkontoGehtSichAus
										&& zzDto.getAnzahlZieltageFuerNetto() != null) {
									iZielTage = zzDto
											.getAnzahlZieltageFuerNetto();
								}
							}
							// nicht mit Skonto zahlen -> nettotage
							else {
								if (zzDto.getAnzahlZieltageFuerNetto() != null) {
									iZielTage = zzDto
											.getAnzahlZieltageFuerNetto();
								}
							}

						}
						// Zieltage addieren
						dFaellig = Helper.addiereTageZuDatum(dFaellig,
								iZielTage);
						// wenn vor dem naechsten Zahlungslauf faellig, dann
						// speichern
						if (dFaellig.before(krit.getDNaechsterZahlungslauf())
								|| bdBruttowertFW.doubleValue() < 0) {
							// vorher noch den Skontosatz anwenden
							BigDecimal bdMultiplikator = new BigDecimal(1)
									.subtract(bdAngewandterSkontoSatz
											.movePointLeft(2));
							BigDecimal bdZahlbetragKomplett = bdBruttowertFW
									.multiply(bdMultiplikator).setScale(
											FinanzFac.NACHKOMMASTELLEN,
											RoundingMode.HALF_UP);
							// Dto zusammenbauen
							ZahlungsvorschlagDto zvDto = new ZahlungsvorschlagDto();
							zvDto.setBBezahlen(Helper.boolean2Short(
									getParameterFac().getZahlungsvorschlagDefaultFreigabe(theClientDto.getMandant())));
							zvDto.setEingangsrechnungIId(e.getI_id());
							zvDto.setNAngewandterskontosatz(bdAngewandterSkontoSatz);
							zvDto.setNErBruttoBetrag(bdBruttowertFW);
							zvDto.setNBereitsBezahlt(bdBezahltFW);
							zvDto.setNZahlbetrag(bdZahlbetragKomplett
									.subtract(bdBezahltFW));
							zvDto.setBWaereVollstaendigBezahlt(Helper
									.boolean2Short(true));
							zvDto.setTFaellig(new java.sql.Date(dFaellig
									.getTime())); // in sql-date umwandeln
							zvDto.setZahlungsvorschlaglaufIId(iZViid);
							
							
							
							//PJ22194
							if(iERPruefen>0 && e.getT_geprueft() == null) {
								zvDto.setBBezahlen(Helper.boolean2Short(false));
							}
							
							
							
							// Eintrag speichern
							createZahlungsvorschlag(zvDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return iZViid;
	}

	/**
	 * Einen ganzen Zahlungsvorschlaglauf loeschen.
	 * 
	 * @param zahlungsvorschlaglaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeZahlungsvorschlaglauf(Integer zahlungsvorschlaglaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.warn(theClientDto.getIDUser(), "removeZahlungsvorschlaglauf:"
				+ zahlungsvorschlaglaufIId);
		Session session = null;
		try {
			// Zuerst alle zugehoerigen Eintraege suchen.
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRZahlungsvorschlag.class);
			c.add(Restrictions.eq(
					EingangsrechnungFac.FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID,
					zahlungsvorschlaglaufIId));
			List<?> list = c.list();
			// und alle loeschen.
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) iter.next();
				removeZahlungsvorschlag(zv.getI_id());
			}
			// jetzt den Kopfdatensatz loeschen.
			removeZahlungsvorschlaglauf(zahlungsvorschlaglaufIId);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private Integer createZahlungsvorschlaglauf(
			ZahlungsvorschlaglaufDto zahlungsvorschlaglaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// PK generieren
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_ZAHLUNGSVORSCHLAGLAUF);
		zahlungsvorschlaglaufDto.setIId(iId);
		// Anleger
		zahlungsvorschlaglaufDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());
		try {
			Zahlungsvorschlaglauf zahlungsvorschlaglauf = new Zahlungsvorschlaglauf(
					zahlungsvorschlaglaufDto.getIId(),
					zahlungsvorschlaglaufDto.getMandantCNr(),
					zahlungsvorschlaglaufDto.getTZahlungsstichtag(),
					zahlungsvorschlaglaufDto.getTNaechsterzahlungslauf(),
					zahlungsvorschlaglaufDto.getBMitskonto(),
					zahlungsvorschlaglaufDto
							.getISkontoueberziehungsfristintagen(),
					zahlungsvorschlaglaufDto.getBankverbindungIId(),
					zahlungsvorschlaglaufDto.getPersonalIIdAnlegen());
			em.persist(zahlungsvorschlaglauf);
			em.flush();
			zahlungsvorschlaglaufDto.setTAnlegen(zahlungsvorschlaglauf
					.getTAnlegen());
			setZahlungsvorschlaglaufFromZahlungsvorschlaglaufDto(
					zahlungsvorschlaglauf, zahlungsvorschlaglaufDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return iId;
	}

	private void removeZahlungsvorschlaglauf(Integer iId) throws EJBExceptionLP {
		// try {
		Zahlungsvorschlaglauf toRemove = em.find(Zahlungsvorschlaglauf.class,
				iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public ZahlungsvorschlaglaufDto zahlungsvorschlaglaufFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		// try {
		Zahlungsvorschlaglauf zahlungsvorschlaglauf = em.find(
				Zahlungsvorschlaglauf.class, iId);
		if (zahlungsvorschlaglauf == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZahlungsvorschlaglaufDto(zahlungsvorschlaglauf);
		// {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		// }
		// catch (javax.ejb.ObjectNotFoundException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	private void setZahlungsvorschlaglaufFromZahlungsvorschlaglaufDto(
			Zahlungsvorschlaglauf zahlungsvorschlaglauf,
			ZahlungsvorschlaglaufDto zahlungsvorschlaglaufDto) {
		zahlungsvorschlaglauf.setMandantCNr(zahlungsvorschlaglaufDto
				.getMandantCNr());
		zahlungsvorschlaglauf.setTAnlegen(zahlungsvorschlaglaufDto
				.getTAnlegen());
		zahlungsvorschlaglauf.setTZahlungsstichtag(zahlungsvorschlaglaufDto
				.getTZahlungsstichtag());
		zahlungsvorschlaglauf
				.setTNaechsterzahlungslauf(zahlungsvorschlaglaufDto
						.getTNaechsterzahlungslauf());
		zahlungsvorschlaglauf.setBMitskonto(zahlungsvorschlaglaufDto
				.getBMitskonto());
		zahlungsvorschlaglauf
				.setISkontoueberziehungsfristintagen(zahlungsvorschlaglaufDto
						.getISkontoueberziehungsfristintagen());
		zahlungsvorschlaglauf.setBankverbindungIId(zahlungsvorschlaglaufDto
				.getBankverbindungIId());
		zahlungsvorschlaglauf.setPersonalIIdAnlegen(zahlungsvorschlaglaufDto
				.getPersonalIIdAnlegen());
		zahlungsvorschlaglauf
				.setPersonalIIdGespeichert(zahlungsvorschlaglaufDto
						.getPersonalIIdGespeichert());
		zahlungsvorschlaglauf.setTGespeichert(zahlungsvorschlaglaufDto
				.getTGespeichert());
		em.merge(zahlungsvorschlaglauf);
		em.flush();
	}

	private ZahlungsvorschlaglaufDto assembleZahlungsvorschlaglaufDto(
			Zahlungsvorschlaglauf zahlungsvorschlaglauf) {
		return ZahlungsvorschlaglaufDtoAssembler
				.createDto(zahlungsvorschlaglauf);
	}

	private void createZahlungsvorschlag(
			ZahlungsvorschlagDto zahlungsvorschlagDto) throws EJBExceptionLP {
		// PK generieren
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_ZAHLUNGSVORSCHLAG);
		zahlungsvorschlagDto.setIId(iId);
		try {
			Zahlungsvorschlag zahlungsvorschlag = new Zahlungsvorschlag(
					zahlungsvorschlagDto.getIId(),
					zahlungsvorschlagDto.getZahlungsvorschlaglaufIId(),
					zahlungsvorschlagDto.getEingangsrechnungIId(),
					zahlungsvorschlagDto.getBBezahlen(),
					zahlungsvorschlagDto.getTFaellig(),
					zahlungsvorschlagDto.getNAngewandterskontosatz(),
					zahlungsvorschlagDto.getNErBruttoBetrag(),
					zahlungsvorschlagDto.getNBereitsBezahlt(),
					zahlungsvorschlagDto.getNZahlbetrag(),
					zahlungsvorschlagDto.getBWaereVollstaendigBezahlt());
			em.persist(zahlungsvorschlag);
			em.flush();
			setZahlungsvorschlagFromZahlungsvorschlagDto(zahlungsvorschlag,
					zahlungsvorschlagDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void removeZahlungsvorschlag(Integer iId) throws EJBExceptionLP {
		// try {
		Zahlungsvorschlag toRemove = em.find(Zahlungsvorschlag.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public ZahlungsvorschlagDto zahlungsvorschlagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Zahlungsvorschlag zahlungsvorschlag = em.find(Zahlungsvorschlag.class,
				iId);
		if (zahlungsvorschlag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZahlungsvorschlagDto(zahlungsvorschlag);
		// {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		// }
		// catch (javax.ejb.ObjectNotFoundException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	public ZahlungsvorschlagDto[] zahlungsvorschlagFindByZahlungsvorschlaglaufIId(
			Integer zahlungsvorschlaglaufIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("ZahlungsvorschlagfindByZahlungsvorschlaglaufIId");
		query.setParameter(1, zahlungsvorschlaglaufIId);
		Collection<?> zahlungsvorschlag = query.getResultList();
		// if(zahlungsvorschlag.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZahlungsvorschlagDtos(zahlungsvorschlag);
		// {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		// }
		// catch (javax.ejb.ObjectNotFoundException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	private void setZahlungsvorschlagFromZahlungsvorschlagDto(
			Zahlungsvorschlag zahlungsvorschlag,
			ZahlungsvorschlagDto zahlungsvorschlagDto) {
		zahlungsvorschlag.setZahlungsvorschlaglaufIId(zahlungsvorschlagDto
				.getZahlungsvorschlaglaufIId());
		zahlungsvorschlag.setEingangsrechnungIId(zahlungsvorschlagDto
				.getEingangsrechnungIId());
		zahlungsvorschlag.setBBezahlen(zahlungsvorschlagDto.getBBezahlen());
		zahlungsvorschlag.setTFaellig(zahlungsvorschlagDto.getTFaellig());
		zahlungsvorschlag.setNAngewandterskontosatz(zahlungsvorschlagDto
				.getNAngewandterskontosatz());
		zahlungsvorschlag.setNErBruttoBetrag(zahlungsvorschlagDto
				.getNErBruttoBetrag());
		zahlungsvorschlag.setNBereitsBezahlt(zahlungsvorschlagDto
				.getNBereitsBezahlt());
		zahlungsvorschlag.setNZahlbetrag(zahlungsvorschlagDto.getNZahlbetrag());
		zahlungsvorschlag.setBWaereVollstaendigBezahlt(zahlungsvorschlagDto
				.getBWaereVollstaendigBezahlt());
		zahlungsvorschlag.setCAuftraggeberreferenz(zahlungsvorschlagDto
				.getCAuftraggeberreferenz());
		em.merge(zahlungsvorschlag);
		em.flush();
	}

	private ZahlungsvorschlagDto assembleZahlungsvorschlagDto(
			Zahlungsvorschlag zahlungsvorschlag) {
		return ZahlungsvorschlagDtoAssembler.createDto(zahlungsvorschlag);
	}

	private ZahlungsvorschlagDto[] assembleZahlungsvorschlagDtos(
			Collection<?> zahlungsvorschlags) {
		List<ZahlungsvorschlagDto> list = new ArrayList<ZahlungsvorschlagDto>();
		if (zahlungsvorschlags != null) {
			Iterator<?> iterator = zahlungsvorschlags.iterator();
			while (iterator.hasNext()) {
				Zahlungsvorschlag zahlungsvorschlag = (Zahlungsvorschlag) iterator
						.next();
				list.add(assembleZahlungsvorschlagDto(zahlungsvorschlag));
			}
		}
		ZahlungsvorschlagDto[] returnArray = new ZahlungsvorschlagDto[list
				.size()];
		return (ZahlungsvorschlagDto[]) list.toArray(returnArray);
	}

	public void updateZahlungsvorschlagBBezahlenMultiSelect(
			List<Integer> zahlungsvorschlagIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {

		List<Zahlungsvorschlag> zvs = new ArrayList<Zahlungsvorschlag>();
		boolean bBezahlen = false;

		for (Integer id : zahlungsvorschlagIIds) {
			Zahlungsvorschlag zv = em.find(Zahlungsvorschlag.class, id);
			if (zv == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (!Helper.short2boolean(zv.getBBezahlen())) {
				bBezahlen = true;
			}

			zvs.add(zv);
		}

		for (Zahlungsvorschlag zv : zvs) {
			zv.setBBezahlen(Helper.boolean2Short(bBezahlen));
			em.merge(zv);
		}
		em.flush();
	}

	public BigDecimal getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(
			Integer zahlungsvorschlagleufIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bdWert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRZahlungsvorschlag.class);
			// Filter nach ZV Lauf
			c.add(Restrictions.eq(
					EingangsrechnungFac.FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID,
					zahlungsvorschlagleufIId));
			// Filter B_BEZAHLEN = true
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ZV_B_BEZAHLEN,
					Helper.boolean2Short(true)));
			// Abfragen
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) iter.next();
				BigDecimal bdWertZV = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								zv.getN_zahlbetrag(),
								zv.getFlreingangsrechnungreport()
										.getWaehrung_c_nr(),
								theClientDto.getSMandantenwaehrung(),
								new Date(System.currentTimeMillis()),
								theClientDto);
				bdWert = bdWert.add(bdWertZV);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bdWert;
	}

	@Override
	public ZahlungsvorschlagExportResult exportiereZahlungsvorschlaglauf(
			Integer zahlungsvorschlagleufIId, Integer iExportTyp,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Validator.pkFieldNotNull(zahlungsvorschlagleufIId, "zahlungsvorschlaglaufIId");
		Validator.notNull(iExportTyp, "iExportTyp");
		
		ZahlungsvorschlagExportFormatter em = ZahlungsvorschlagExportFormatterFactory
				.getZahlungsvorschlagExportFormatter(iExportTyp, theClientDto);

		ZahlungsvorschlaglaufDto zvLauf = zahlungsvorschlaglaufFindByPrimaryKey(zahlungsvorschlagleufIId);
		ZahlungsvorschlagDto[] zvDtos = zahlungsvorschlagFindByZahlungsvorschlaglaufIId(zahlungsvorschlagleufIId);
		if (zvDtos == null || zvDtos.length == 0) {
			myLogger.info("Keine Zahlungsvorschlaege in Zahlungslauf (iId=" + zahlungsvorschlagleufIId + ") enthalten"); 
			return null;
		}
		ZahlungsvorschlagExportResult result = exportiereZV(em, zvLauf, zvDtos, theClientDto);
		
		if (!result.hasFailed()) {
			createEingangsrechnungZahlungenWennTransitKonto(zvLauf, zvDtos, theClientDto);
		}

		return result;
	}
	
	private void createEingangsrechnungZahlungenWennTransitKonto(
			ZahlungsvorschlaglaufDto zvLauf, ZahlungsvorschlagDto[] zvDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (zvLauf.getTGespeichert() != null) return;
		
		BankverbindungDto bvDto = getFinanzFac().bankverbindungFindByPrimaryKey(
				zvLauf.getBankverbindungIId());
		if (!bvDto.isbAlsGeldtransitkonto()) return;

		createEingangsrechnungZahlungenImpl(zvLauf, zvDtos, theClientDto);
	}

	private void createEingangsrechnungZahlungenImpl(ZahlungsvorschlaglaufDto laufDto, 
			ZahlungsvorschlagDto[] zvDtos, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Date dZahlung = getDate();
		for (ZahlungsvorschlagDto zvDto : zvDtos) {
			if (!Helper.short2boolean(zvDto.getBBezahlen())) continue;
			
			EingangsrechnungzahlungDto erZahlungDto = new EingangsrechnungzahlungDto();
			erZahlungDto.setEingangsrechnungIId(zvDto.getEingangsrechnungIId());
			erZahlungDto.setBankverbindungIId(laufDto.getBankverbindungIId());
			erZahlungDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);
			erZahlungDto.setBKursuebersteuert(Helper.getShortFalse());
			erZahlungDto.setIAuszug(laufDto.getIId());

			String mandantWaehrung = getMandantFac()
					.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getWaehrungCNr();
			erZahlungDto.setNBetragfw(zvDto.getNZahlbetrag());
			erZahlungDto.setNBetragustfw(getEingangsrechnungFac().getWertUstAnteiligZuEingangsrechnungUst(
					zvDto.getEingangsrechnungIId(), zvDto.getNZahlbetrag()));
			
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(zvDto.getEingangsrechnungIId());
			String erWaehrung = erDto.getWaehrungCNr();
			erZahlungDto.setNBetrag(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
					erZahlungDto.getNBetragfw(), erWaehrung,
							mandantWaehrung, dZahlung, theClientDto));
			erZahlungDto.setNBetragust(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
					erZahlungDto.getNBetragustfw(), erWaehrung,
					mandantWaehrung, dZahlung, theClientDto));
			if (mandantWaehrung.equals(erWaehrung)) {
				erZahlungDto.setNKurs(BigDecimal.ONE);
			} else {
				WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
						mandantWaehrung, erWaehrung, dZahlung, theClientDto);
				erZahlungDto.setNKurs(kursDto.getNKurs());
			}
			erZahlungDto.setTZahldatum(dZahlung);
			
			getEingangsrechnungFac().createEingangsrechnungzahlung(erZahlungDto, 
					Helper.short2Boolean(zvDto.getBWaereVollstaendigBezahlt()), theClientDto);
		}
	}

	public boolean sindNegativeZuExportierendeZahlungenVorhanden(
			Integer zahlungsvorschlagleufIId, TheClientDto theClientDto) {
		ZahlungsvorschlagDto[] zv = zahlungsvorschlagFindByZahlungsvorschlaglaufIId(zahlungsvorschlagleufIId);

		boolean b = false;

		for (int i = 0; i < zv.length; i++) {
			ZahlungsvorschlagDto zvDto = zv[i];

			if (zvDto.getNZahlbetrag().doubleValue() < 0
					&& Helper.short2boolean(zvDto.getBBezahlen()) == true) {
				return true;
			}

		}

		return b;

	}

	private ZahlungsvorschlagExportResult exportiereZV(ZahlungsvorschlagExportFormatter efm,
			ZahlungsvorschlaglaufDto zvLauf, ZahlungsvorschlagDto[] zvDtos, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ZahlungsvorschlagExportResult result = efm.exportiereDaten(zvLauf, zvDtos, theClientDto);
		if (result.hasFailed()) return result;
		
		Zahlungsvorschlaglauf zvl = em.find(Zahlungsvorschlaglauf.class,
				zvLauf.getIId());
		zvl.setPersonalIIdGespeichert(theClientDto.getIDPersonal());
		zvl.setTGespeichert(getTimestamp());
		em.merge(zvl);

		return result;
	}

	public ZahlungsvorschlagDto updateZahlungsvorschlag(
			ZahlungsvorschlagDto zahlungsvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (zahlungsvorschlagDto != null) {
			Integer iId = zahlungsvorschlagDto.getIId();
			// try {
			Zahlungsvorschlag zahlungsvorschlag = em.find(
					Zahlungsvorschlag.class, iId);
			if (zahlungsvorschlag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			setZahlungsvorschlagFromZahlungsvorschlagDto(zahlungsvorschlag,
					zahlungsvorschlagDto);
		}
		// catch (javax.ejb.ObjectNotFoundException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
		// }
		return zahlungsvorschlagDto;
	}

	@Override
	public ZahlungsvorschlagDto zahlungsvorschlagFindByAuftraggeberreferenzOhneExc(
			String cAuftraggeberreferenz) throws EJBExceptionLP {
		List<Zahlungsvorschlag> zvList = ZahlungsvorschlagQuery
				.listByCAuftraggeberreferenz(em, cAuftraggeberreferenz);
		if (zvList == null || zvList.size() == 0)
			return null;
		if (zvList.size() > 1)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");

		return assembleZahlungsvorschlagDto(zvList.get(0));
	}

	@Override
	public String generateCAuftraggeberreferenzAndUpdateZV(
			ZahlungsvorschlagDto zahlungsvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		boolean uniqueIdFound = false;
		String cAuftraggeberreferenz = "";

		while (!uniqueIdFound) {
			cAuftraggeberreferenz = UUID.randomUUID().toString()
					.replace("-", "");
			if (zahlungsvorschlagFindByAuftraggeberreferenzOhneExc(cAuftraggeberreferenz) == null) {
				uniqueIdFound = true;
			}
		}

		zahlungsvorschlagDto.setCAuftraggeberreferenz(cAuftraggeberreferenz);
		updateZahlungsvorschlag(zahlungsvorschlagDto, theClientDto);

		return cAuftraggeberreferenz;
	}

	@Override
	public String getZahlungsvorschlagSepaExportFilename(
			Integer bankverbindungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		BankverbindungDto bvDto = getFinanzFac()
				.bankverbindungFindByPrimaryKey(bankverbindungIId);

		if (bvDto != null
				&& (bvDto.getCSepaVerzeichnis() == null || bvDto
						.getCSepaVerzeichnis().trim().isEmpty())) {
			BankDto bankDto = getBankFac().bankFindByPrimaryKeyOhneExc(
					bvDto.getBankIId(), theClientDto);
			KontoDtoSmall kontoDto = getFinanzFac()
					.kontoFindByPrimaryKeySmallOhneExc(bvDto.getKontoIId());
			List<String> bvBezeichnungen = new ArrayList<String>();
			if (kontoDto != null)
				bvBezeichnungen.add(kontoDto.getCNr());
			if (bankDto != null)
				bvBezeichnungen.add(bankDto.getPartnerDto()
						.getCName1nachnamefirmazeile1());
			if (bvDto.getCBez() != null)
				bvBezeichnungen.add(bvDto.getCBez());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SEPAEXPORT_KEIN_SEPA_VERZEICHNIS_VORHANDEN,
					"",
					new Object[] { Helper.erzeugeStringAusStringArray(bvBezeichnungen
							.toArray(new String[bvBezeichnungen.size()])) });
		}

		return bvDto.getCSepaVerzeichnis();
	}

	@Override
	public void archiviereSepaZahlungsvorschlag(String xmlZahlungsvorschlag,
			Integer zahlungsvorschlaglaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		ZahlungsvorschlaglaufDto zvDto = zahlungsvorschlaglaufFindByPrimaryKey(zahlungsvorschlaglaufIId);
		BankverbindungDto bankverbindungDto = getFinanzFac()
				.bankverbindungFindByPrimaryKey(zvDto.getBankverbindungIId());
		PartnerDto partnerBankDto = getPartnerFac().partnerFindByPrimaryKey(
				bankverbindungDto.getBankIId(), theClientDto);
		Integer geschaeftsjahr = getBuchenFac().findGeschaeftsjahrFuerDatum(
				new Date(zvDto.getTAnlegen().getTime()),
				theClientDto.getMandant());
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				theClientDto.getIDPersonal(), theClientDto);
		PartnerDto anlegerDto = personalDto.getPartnerDto();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		String sDate = dateFormat.format(zvDto.getTAnlegen());

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocNodeBase docNodeBase = new DocNodeSepaExportZahlungsvorschlaglauf(bankverbindungDto,
				partnerBankDto, geschaeftsjahr, zvDto);
		DocPath docPath = new DocPath(docNodeBase).add(new DocNodeFile(
				"zv_export_sepa.xml"));

		jcrDocDto.setDocPath(docPath);
		try {
			jcrDocDto.setbData(xmlZahlungsvorschlag.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new EJBExceptionLP(e);
		}
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(anlegerDto.getIId());
		jcrDocDto.setlPartner(partnerBankDto.getIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(sDate);
		jcrDocDto.setsFilename("zv_export_sepa");
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Export Sepa " + sDate);
		jcrDocDto.setsRow(zvDto.getIId().toString());
		jcrDocDto.setsTable("");
		jcrDocDto.setsSchlagworte("Export Sepa Zahlungsvorschlag XML");

		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(
				jcrDocDto, theClientDto);
	}

	@Override
	public boolean darfNeuerZahlungsvorschlaglaufErstelltWerden(TheClientDto theClientDto) {
		List<Zahlungsvorschlaglauf> list = ZahlungsvorschlaglaufQuery.listByMandantCnrTGespeichertIsNull(theClientDto.getMandant(), em);
		return list.isEmpty();
	}
	
}
