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
package com.lp.server.fertigung.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.fertigung.ejb.Internebestellung;
import com.lp.server.fertigung.fastlanereader.generated.FLRInternebestellung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.fertigung.service.InternebestellungDtoAssembler;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.MaterialbedarfDto;
import com.lp.server.kueche.ejb.Speiseplanposition;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.SpeiseplanpositionDto;
import com.lp.server.kueche.service.SpeiseplanpositionDtoAssembler;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class InternebestellungFacBean extends Facade implements
		InternebestellungFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public InternebestellungDto createInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_INTERNEBESTELLUNG);
		internebestellungDto.setIId(iId);
		// User
		internebestellungDto
				.setPersonalIIdAendern(theClientDto.getIDPersonal());
		internebestellungDto.setTAendern(getTimestamp());

		try {
			myLogger.info(internebestellungDto.toString());
			Internebestellung internebestellung = new Internebestellung(
					internebestellungDto.getIId(),
					internebestellungDto.getMandantCNr(),
					internebestellungDto.getBelegartCNr(),
					internebestellungDto.getIBelegiid(),
					internebestellungDto.getStuecklisteIId(),
					internebestellungDto.getNMenge(), new Timestamp(
							internebestellungDto.getTLiefertermin().getTime()),
					internebestellungDto.getPersonalIIdAendern(),
					internebestellungDto.getTProduktionsbeginn());
			em.persist(internebestellung);
			em.flush();
			internebestellungDto.setTAendern(internebestellung.getTAendern());
			setInternebestellungFromInternebestellungDto(internebestellung,
					internebestellungDto);
			return internebestellungDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		if (internebestellungDto != null) {
			Integer iId = internebestellungDto.getIId();
			Internebestellung toRemove = em.find(Internebestellung.class, iId);
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
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public InternebestellungDto updateInternebestellung(
			InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (internebestellungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("internebestellungDto == null"));
		}
		internebestellungDto
				.setPersonalIIdAendern(theClientDto.getIDPersonal());
		internebestellungDto.setTAendern(getTimestamp());

		Integer iId = internebestellungDto.getIId();
		// try {
		Internebestellung internebestellung = em.find(Internebestellung.class,
				iId);
		if (internebestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setInternebestellungFromInternebestellungDto(internebestellung,
				internebestellungDto);
		return internebestellungDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
	}

	public InternebestellungDto internebestellungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Internebestellung internebestellung = em.find(Internebestellung.class,
				iId);
		if (internebestellung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleInternebestellungDto(internebestellung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setInternebestellungFromInternebestellungDto(
			Internebestellung internebestellung,
			InternebestellungDto internebestellungDto) {
		internebestellung.setMandantCNr(internebestellungDto.getMandantCNr());
		internebestellung.setBelegartCNr(internebestellungDto.getBelegartCNr());
		internebestellung.setIBelegiid(internebestellungDto.getIBelegiid());
		internebestellung.setStuecklisteIId(internebestellungDto
				.getStuecklisteIId());
		internebestellung.setNMenge(internebestellungDto.getNMenge());
		internebestellung.setTLiefertermin(internebestellungDto
				.getTLiefertermin());
		internebestellung.setPersonalIIdAendern(internebestellungDto
				.getPersonalIIdAendern());
		internebestellung.setTAendern(internebestellungDto.getTAendern());
		internebestellung.setInternebestellungIIdElternlos(internebestellungDto
				.getInternebestellungIIdElternlos());
		internebestellung.setIBelegpositionIId(internebestellungDto
				.getIBelegpositionIId());
		internebestellung.setTProduktionsbeginn(internebestellungDto
				.getTProduktionsbeginn());
		em.merge(internebestellung);
		em.flush();
	}

	private InternebestellungDto assembleInternebestellungDto(
			Internebestellung internebestellung) {
		return InternebestellungDtoAssembler.createDto(internebestellung);
	}

	private InternebestellungDto[] assembleInternebestellungDtos(
			Collection<?> internebestellungs) {
		List<InternebestellungDto> list = new ArrayList<InternebestellungDto>();
		if (internebestellungs != null) {
			Iterator<?> iterator = internebestellungs.iterator();
			while (iterator.hasNext()) {
				Internebestellung internebestellung = (Internebestellung) iterator
						.next();
				list.add(assembleInternebestellungDto(internebestellung));
			}
		}
		InternebestellungDto[] returnArray = new InternebestellungDto[list
				.size()];
		return (InternebestellungDto[]) list.toArray(returnArray);
	}

	/**
	 * Erzeugen der Internen Bestellung fuer eine Stueckliste mit den Daten des
	 * Mandanten, auf dem der Benutzer angemeldet ist.
	 * 
	 * @param iVorlaufzeit
	 *            Integer
	 * @param dLieferterminFuerArtikelOhneReservierung
	 *            Date
	 * @param stuecklisteIId
	 *            Integer
	 * @param alZusatzeintraegeBewegungsvorschau
	 *            Optional. Hier koennen der Bewegungsvorschau "kuenstlich"
	 *            weitere Eintraege hinzugefuegt werden.
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BigDecimal bestellte Menge (exklusive eventuell vorhandener
	 *         Unterstuecklisten)
	 */
	public ArrayList<Integer> erzeugeInterneBestellung(Integer iVorlaufzeit,
			Integer iToleranz,
			java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			boolean bInterneBestellungBeruecksichtigen, Integer stuecklisteIId,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau,
			boolean bInterneBestellung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData("erzeugeInterneBestellung fuer stuecklisteIId="
				+ stuecklisteIId);
		ArrayList<Integer> alUnterstuecklisten = new ArrayList<Integer>();
		try {
			// Stueckliste holen
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			// Artikel immer holen. Da dieser nicht immer mit dem StuecklisteDto
			// mitgeliefert wird.
			ArtikelDto artikelDto = stuecklisteDto.getArtikelDto();
			if (artikelDto == null) {
				artikelDto = getArtikelFac().artikelFindByPrimaryKey(
						stuecklisteDto.getArtikelIId(), theClientDto);
			}
			// CK: Projekt 6699, Wenn Stkl fremdgefertigt, dann darf Sie nicht
			// in der Internen Bestellung aufscheinen
			if (stuecklisteDto != null
					&& Helper
							.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
				if (stuecklisteDto.getStuecklisteartCNr().equals(
						StuecklisteFac.STUECKLISTEART_STUECKLISTE)
						|| stuecklisteDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {

					if (Helper.short2Boolean(artikelDto
							.getBLagerbewirtschaftet())) {
						ArrayList<?> aBedarfe = berechneBedarfe(artikelDto,
								iVorlaufzeit, iToleranz,
								dLieferterminFuerArtikelOhneReservierung,
								bInterneBestellungBeruecksichtigen,
								alZusatzeintraegeBewegungsvorschau,
								bInterneBestellung, theClientDto, null);
						// Positionen der Stkl. Da die interne Bestellung auch
						// fuer Unterstuecklisten durchgefuehrt werden muss.
						StuecklistepositionDto[] stkposDto = getStuecklisteFac()
								.stuecklistepositionFindByStuecklisteIId(
										stuecklisteIId, theClientDto);
						// fuer jeden Bedarf eine Interne Bestellung anlegen
						for (Iterator<?> iter = aBedarfe.iterator(); iter
								.hasNext();) {
							MaterialbedarfDto matBed = (MaterialbedarfDto) iter
									.next();
							InternebestellungDto ibDto = new InternebestellungDto();
							ibDto.setBelegartCNr(matBed.getSBelegartCNr());
							ibDto.setIBelegiid(matBed.getIBelegIId());
							ibDto.setIBelegpositionIId(matBed
									.getIBelegpositionIId());
							ibDto.setInternebestellungIIdElternlos(matBed
									.getIInternebestellungIIdElternlos());
							ibDto.setMandantCNr(theClientDto.getMandant());

							// PJ18451
							if (artikelDto.getFUeberproduktion() != null) {
								matBed.setNMenge(matBed
										.getNMenge()
										.add(Helper.getProzentWert(
												matBed.getNMenge(),
												new BigDecimal(artikelDto
														.getFUeberproduktion()),
												4)));
							}

							ibDto.setNMenge(matBed.getNMenge());
							ibDto.setStuecklisteIId(matBed.getIStuecklisteIId());

							// SP2708
							// Durchlaufzeit der uebergeordneten
							// Stueckliste bestimmen.
							Integer iDurchlaufzeit;
							if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
								// Ist optional in der Stueckliste
								// definiert.
								iDurchlaufzeit = stuecklisteDto
										.getNDefaultdurchlaufzeit().intValue();
							} else {
								// Falls in der Stueckliste nicht
								// definiert, dann zieht der
								// Mandantenparameter
								ParametermandantDto parameter = getParameterFac()
										.getMandantparameter(
												theClientDto.getMandant(),
												ParameterFac.KATEGORIE_FERTIGUNG,
												ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
								iDurchlaufzeit = ((Integer) parameter
										.getCWertAsObject()).intValue();
							}

							// Hilfsstueckliste hat Durchlaufzeit von 0
							if (stuecklisteDto
									.getStuecklisteartCNr()
									.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
								iDurchlaufzeit = 0;
							}

							ibDto.setTLiefertermin(matBed.getTTermin());

							java.sql.Date dProduktionbeginnTermin = Helper
									.addiereTageZuDatum(matBed.getTTermin(),
											-iDurchlaufzeit);
							ibDto.setTProduktionsbeginn(dProduktionbeginnTermin);

							ibDto.setStuecklisteIId(stuecklisteIId);
							// anlegen

							ibDto = context.getBusinessObject(
									InternebestellungFac.class)
									.createInternebestellung(ibDto,
											theClientDto);
							// Mit diesem Eintrag entstehen auch bedarfe zu
							// diesem termin fuer eventuell vorhandene
							// Unterstuecklisten.
							// Dazu jede Position der Stueckliste ansehen, obs
							// eine Unterstueckliste ist
							for (int i = 0; i < stkposDto.length; i++) {
								StuecklistepositionDto stkpos = stkposDto[i];
								if (stkpos.getArtikelIId() != null) {
									StuecklisteDto stkUnter = getStuecklisteFac()
											.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
													stkpos.getArtikelIId(),
													theClientDto);
									// Position ist auch eine Stueckliste
									if (stkUnter != null) {
										alUnterstuecklisten.add(stkUnter
												.getIId());
										// Fuer diese wird rekursiv ebenfalls
										// eine Interne Bestellung
										// durchgefuehrt.
										// Die Bedarfsermittlung basiert auf der
										// Bewegungsvorschau. In dieser ist
										// jedoch der hier entstandene Bedarf
										// nicht ersichtlich, da er nur ein
										// theoretischer vorschlagswert ist.
										// Darum wird der bewegungsvorschau eine
										// Zeile hinzugefuegt, um dies dort
										// abzubilden.
										BewegungsvorschauDto bewegungsvorschauDto = new BewegungsvorschauDto();
										bewegungsvorschauDto
												.setArtikelIId(stkpos
														.getArtikelIId());
										bewegungsvorschauDto
												.setCBelegartCNr(matBed
														.getSBelegartCNr());
										bewegungsvorschauDto
												.setCBelegnummer(matBed
														.getCBelegnummer());

										bewegungsvorschauDto
												.setIBelegIId(matBed
														.getIBelegIId());
										bewegungsvorschauDto
												.setIBelegPositionIId(matBed
														.getIBelegpositionIId());
										// Erforderliche Menge = Bedarf der
										// uebergeordneten * positionsmenge
										BigDecimal bdMenge = matBed.getNMenge()
												.multiply(stkpos.getNMenge());

										// SP2700
										if (stuecklisteDto
												.getIErfassungsfaktor() != null
												&& stuecklisteDto
														.getIErfassungsfaktor()
														.intValue() != 0) {
											bdMenge = bdMenge
													.divide(new BigDecimal(
															stuecklisteDto
																	.getIErfassungsfaktor()
																	.doubleValue()),
															4,
															BigDecimal.ROUND_HALF_EVEN);
										}

										bewegungsvorschauDto.setNMenge(bdMenge
												.negate().setScale(4,
														RoundingMode.HALF_UP));
										bewegungsvorschauDto
												.setPartnerDto(matBed
														.getPartnerDto());

										// Termin ist der Termin der
										// uebergeordneten minus deren
										// durchlaufzeit
										java.sql.Date dTermin = Helper
												.addiereTageZuDatum(
														matBed.getTTermin(),
														-iDurchlaufzeit);
										// Da Vordatierungen vor den heutigen
										// Tag sowie die Vorlaufzeit in der
										// Bedarfsermittlung
										// behandelt werden, brauch ich mich
										// hier nicht drum kuemmern.
										bewegungsvorschauDto
												.setTLiefertermin(new java.sql.Timestamp(
														dTermin.getTime()));
										// Temporaeren Eintrag die Liste
										// einfuegen
										bewegungsvorschauDto
												.setBTemporaererEintrag(true);

										bewegungsvorschauDto
												.setMandantCNr(ibDto
														.getMandantCNr());

										alZusatzeintraegeBewegungsvorschau
												.add(bewegungsvorschauDto);
										// interne Bestellung der
										// Unterstueckliste
										// BigDecimal bdBestellt = context
										// .getBusinessObject(
										// InternebestellungFac.class)
										// .erzeugeInterneBestellung(
										// 0,
										// 0, // keine Vorlaufzeit
										// // fuer
										// // Unterstuecklisten
										// dLieferterminFuerArtikelOhneReservierung
										// ,
										// stkUnter.getIId(),
										// alZusatzeintraegeBewegungsvorschau,
										// bInterneBestellung,
										// theClientDto);
										// Bestellte Menge subtrahieren (add,
										// weil sie ja negativ ist)
										// bewegungsvorschauDto
										// .setNMenge(bewegungsvorschauDto
										// .getNMenge().add(
										// bdBestellt));
									}
								}
							}
						}
					}
				}
			}
			return alUnterstuecklisten;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public ArrayList pruefeOffeneRahmenmengen(TheClientDto theClientDto) {
		ArrayList<Object[]> al = new ArrayList<Object[]>();
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRInternebestellung.class);
		c.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		List<?> list = c.list();

		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			FLRInternebestellung intBest = (FLRInternebestellung) iterator
					.next();

			try {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								intBest.getFlrstueckliste().getFlrartikel()
										.getI_id(), theClientDto);
				BigDecimal bdRahmenRes = getReservierungFac()
						.getAnzahlRahmenreservierungen(artikelDto.getIId(),
								theClientDto);
				boolean bLagermindest = false;
				if (artikelDto.getFLagermindest() != null) {
					if (bdRahmenRes != null) {
						if (artikelDto.getFLagermindest().doubleValue() > bdRahmenRes
								.doubleValue()) {
							Object[] o = new Object[5];
							o[0] = artikelDto.getCNr();
							o[1] = new BigDecimal(artikelDto.getFLagermindest()
									.doubleValue());
							o[2] = bdRahmenRes;
							o[3] = intBest.getI_id();
							o[4] = getTextRespectUISpr(
									"fert.internebestellung.einlagermindesstand",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
							al.add(o);
							bLagermindest = true;
						}
					}
				}
				if (!bLagermindest) {
					if (artikelDto.getFFertigungssatzgroesse() != null) {
						if (bdRahmenRes != null) {
							if (artikelDto.getFFertigungssatzgroesse()
									.doubleValue() > bdRahmenRes.doubleValue()) {
								Object[] o = new Object[5];
								o[0] = artikelDto.getCNr();
								o[1] = new BigDecimal(artikelDto
										.getFFertigungssatzgroesse()
										.doubleValue());
								o[2] = bdRahmenRes;
								o[3] = intBest.getI_id();
								o[4] = getTextRespectUISpr(
										"fert.internebestellung.einefertigungssatzgroesse",
										theClientDto.getMandant(),
										theClientDto.getLocUi());
								al.add(o);
							}
						}
					}
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		return al;
	}

	/**
	 * Erzeugen der internen Bestellung fuer den Mandanten, auf dem der Benutzer
	 * angemeldet ist.
	 * 
	 * @param bVorhandeneLoeschen
	 *            boolean
	 * @param iVorlaufzeit
	 *            Integer
	 * @param dLieferterminFuerArtikelOhneReservierung
	 *            Date
	 * @param bVerdichten
	 *            Boolean
	 * @param iVerdichtungsTage
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws IllegalStateException
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void erzeugeInterneBestellung(Boolean bVorhandeneLoeschen,
			Integer iVorlaufzeit, Integer iToleranz,
			java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			Boolean bVerdichten, Integer iVerdichtungsTage,
			boolean bInterneBestellung, Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException,
			IllegalStateException {
		Session session = null;
		try {
			// falls gewuenscht - vorhandene IB-Daten loeschen
			if (bVorhandeneLoeschen) {
				context.getBusinessObject(InternebestellungFac.class)
						.removeInternebestellungEinesMandanten(false,
								theClientDto);
			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Alle Stuecklisten dieses Mandanten
			Criteria c = session.createCriteria(FLRStueckliste.class);
			c.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// PJ18740
			if (losIId != null) {
				ArrayList alStkls = new ArrayList();
				LossollmaterialDto[] sollDtos = getFertigungFac()
						.lossollmaterialFindByLosIId(losIId);
				for (int i = 0; i < sollDtos.length; i++) {
					if (sollDtos[i].getArtikelIId() != null) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										sollDtos[i].getArtikelIId(),
										theClientDto);
						if (stklDto != null) {
							alStkls.add(stklDto.getIId());
						}
					}
				}

				// Wenn keine Stkl-Vorhanden, dann beenden
				if (alStkls.size() == 0) {
					return;
				}

				c.add(Restrictions.in("i_id", alStkls));
			}

			List<?> list = c.list();
			// Liste mit Zusatzeintraegen anlegen
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau = new ArrayList<BewegungsvorschauDto>();
			// Liste der Unterstuecklisten die evtl noch beruecksichtigt werden
			// muessen
			ArrayList<Integer> alUterstuecklisten = new ArrayList<Integer>();
			// fuer jede Stueckliste die interne Bestellung durchfuehren
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRStueckliste stkl = (FLRStueckliste) iter.next();
				alUterstuecklisten.addAll(erzeugeInterneBestellung(
						iVorlaufzeit, iToleranz,
						dLieferterminFuerArtikelOhneReservierung, true,
						stkl.getI_id(), alZusatzeintraegeBewegungsvorschau,
						bInterneBestellung, theClientDto));
				// gegebenenfalls verdichten.
				if (bVerdichten) {
					verdichteInterneBestellungEinerStuecklisteEinesMandanten(
							stkl.getI_id(), iVerdichtungsTage, theClientDto);
				}
			}
			int counter = 0;
			while (!alUterstuecklisten.isEmpty()) {
				counter++;
				if (counter > 50) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN,
							new Exception(
									"Mehr als 50 Ebenen von Unterst\u00FCcklisten vorhanden. Betroffen Stueckliste:"
											+ getStuecklisteFac()
													.stuecklisteFindByPrimaryKey(
															alUterstuecklisten
																	.get(0),
															theClientDto)
													.getArtikelDto().getCNr()));
				}
				ArrayList<Integer> alTempUnterstuecklisten = new ArrayList<Integer>();
				for (int i = 0; i < alUterstuecklisten.size(); i++) {
					alTempUnterstuecklisten.addAll(erzeugeInterneBestellung(
							iVorlaufzeit, iToleranz,
							dLieferterminFuerArtikelOhneReservierung, true,
							alUterstuecklisten.get(i),
							alZusatzeintraegeBewegungsvorschau,
							bInterneBestellung, theClientDto));
					// gegebenenfalls verdichten.
					if (bVerdichten) {
						verdichteInterneBestellungEinerStuecklisteEinesMandanten(
								alUterstuecklisten.get(i), iVerdichtungsTage,
								theClientDto);
					}
				}
				alUterstuecklisten = alTempUnterstuecklisten;
			}

			// SP3203 Alle Hilfsstuecklsten entfernen

			context.getBusinessObject(InternebestellungFac.class)
					.removeInternebestellungEinesMandanten(true, theClientDto);

		} finally {
			closeSession(session);
		}
	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. nur zur externen
	 * verwendung, da eintraege aus der internen bestellung unberuecksichtigt
	 * bleiben.
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param iVorlaufzeitInTagen
	 *            Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin
	 *            Date
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	public ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto,
			Integer iVorlaufzeitInTagen, Integer iToleranz,
			Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto,
			ArrayList<Integer> arLosIId) throws EJBExceptionLP {
		return berechneBedarfe(artikelDto, iVorlaufzeitInTagen, iToleranz,
				defaultDatumFuerEintraegeOhneLiefertermin, false, null,
				bInterneBestellung, theClientDto, arLosIId);
	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. nur zur externen
	 * verwendung, da eintraege aus der internen bestellung unberuecksichtigt
	 * bleiben.
	 * 
	 * @param artikelIId
	 *            ArtikelDto
	 * @param iVorlaufzeitInTagen
	 *            Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin
	 *            Date
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	public ArrayList<MaterialbedarfDto> berechneBedarfe(Integer artikelIId,
			Integer iVorlaufzeitInTagen, Integer iToleranz,
			Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto,
			ArrayList<Integer> arLosIId) throws EJBExceptionLP {

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				artikelIId, theClientDto);
		return berechneBedarfe(artikelDto, iVorlaufzeitInTagen, iToleranz,
				defaultDatumFuerEintraegeOhneLiefertermin, false, null,
				bInterneBestellung, theClientDto, arLosIId);

	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. Im ermittelten
	 * Liefertermin (Bedarfstermin) wird die Vorlaufzeit bereits
	 * beruecksichtigt. Bedarfe an gleichen Terminen werden komprimiert.
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param iVorlaufzeitInTagen
	 *            Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin
	 *            Date
	 * @param bInternebestellungMiteinbeziehen
	 *            boolean
	 * @param alZusatzeintraegeBewegungsvorschau
	 *            Optional. Hier koennen der Bewegungsvorschau "kuenstlich"
	 *            weitere Eintraege hinzugefuegt werden.
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	private ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto,
			Integer iVorlaufzeitInTagen, Integer iToleranz,
			Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInternebestellungMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau,
			boolean bInterneBestellung, TheClientDto theClientDto,
			ArrayList<Integer> arLosIId) throws EJBExceptionLP {
		ArrayList<MaterialbedarfDto> bedarfe = new ArrayList<MaterialbedarfDto>();
		try {
			// voraussichtlicher lagerstand. Initialisiert mit dem aktuellen
			// Lagerstand.
			// BigDecimal bdVoraussLagerstand = getLagerFac()
			// .getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(),
			// theClientDto);
			BigDecimal bdVoraussLagerstand = new BigDecimal(0);
			LagerDto[] lagerdto = null;

			// PJ18609
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				lagerdto = getLagerFac().lagerFindAll();

			} else {
				lagerdto = getLagerFac().lagerFindByMandantCNr(
						theClientDto.getMandant());
			}

			for (int i = 0; i < lagerdto.length; i++) {
				if (bInterneBestellung) {
					// Interne Bestellung
					if (Helper.short2boolean(lagerdto[i]
							.getBInternebestellung())) {
						// Lager wird beruecksichtigt
						bdVoraussLagerstand = bdVoraussLagerstand
								.add(getLagerFac().getLagerstand(
										artikelDto.getIId(),
										lagerdto[i].getIId(), theClientDto));
					}
				} else {
					// Bestellvorschlag
					if (Helper
							.short2boolean(lagerdto[i].getBBestellvorschlag())) {
						// Lager wird beruecksichtigt
						bdVoraussLagerstand = bdVoraussLagerstand
								.add(getLagerFac().getLagerstand(
										artikelDto.getIId(),
										lagerdto[i].getIId(), theClientDto));
					}
				}
			}

			ArrayList<BewegungsvorschauDto> aBewegungsvorschau = getBewegungsvorschauSortiert(
					artikelDto, bInternebestellungMiteinbeziehen,
					alZusatzeintraegeBewegungsvorschau, true, theClientDto,
					arLosIId);
			// Bewegungsvorschau ist zwar sortiert, kann jedoch durch die
			// Toleranz wieder durcheinander geworfen werden
			// => Toleranz setzen und danach neu sortieren
			// wenn keine Toleranz definiert wurde dann Toleranz 0
			if (iToleranz == null) {
				iToleranz = new Integer(0);
			}
			for (int y = 0; y < aBewegungsvorschau.size(); y++) {
				BewegungsvorschauDto bewDto = aBewegungsvorschau.get(y);
				if (bewDto.getTLiefertermin() != null) {
					if (LocaleFac.BELEGART_BESTELLUNG.equals(bewDto
							.getCBelegartCNr())
							|| LocaleFac.BELEGART_LOSABLIEFERUNG.equals(bewDto
									.getCBelegartCNr())
					// SK: 13.3.09 Mit WH besprochen Bei Bestellung und
					// Losablieferung schieben bei Auftrag nicht
					/*
					 * ||
					 * bDto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
					 */) {
						Timestamp tBefore = bewDto.getTLiefertermin();

						if (bewDto.getTABTerminBestellung() != null) {
							tBefore = new Timestamp(bewDto
									.getTABTerminBestellung().getTime());
						}

						Timestamp tAfter = new Timestamp(tBefore.getTime()
								- (new Long(iToleranz) * 24 * 60 * 60 * 1000));
						bewDto.setTLiefertermin(tAfter);
						if (bewDto.getTABTerminBestellung() != null) {
							bewDto.setTABTerminBestellung(new Date(tAfter
									.getTime()));
						}
					}
				}
				aBewegungsvorschau.set(y, bewDto);
			}
			// Nun sind alle Termine nach Toleranz richtig gesetzt wieder neu
			// sortieren
			Collections.sort(aBewegungsvorschau,
					new ComparatorBewegungsvorschau());
			BigDecimal bdLagerMindestBestand;
			if (artikelDto.getFLagermindest() != null) {
				bdLagerMindestBestand = new BigDecimal(
						artikelDto.getFLagermindest());
			} else {
				bdLagerMindestBestand = new BigDecimal(0);
			}
			BigDecimal bdLagerSollBestand = getLagerFac()
					.getArtikelSollBestand(artikelDto);

			// ------------------------------------------------------------------
			// ----
			// Wenn die Bewegungsvorschau leer ist, dann wird nur auf Mindest-
			// und Sollbestand geprueft.
			// ------------------------------------------------------------------
			// ----
			if (aBewegungsvorschau.size() == 0) {
				if (bdVoraussLagerstand.compareTo(bdLagerMindestBestand) < 0) {
					MaterialbedarfDto materialbedarfDto = new MaterialbedarfDto();
					materialbedarfDto.setIArtikelId(artikelDto.getIId());
					BigDecimal bdMenge = bdLagerSollBestand
							.subtract(bdVoraussLagerstand);
					// Menge auf den Sollbestand auffuellen. SK Bzw
					// Fertigungssatzgroesse verwenden
					if (bInterneBestellung) {
						if ((artikelDto.getFFertigungssatzgroesse() != null)
								&& (artikelDto.getFFertigungssatzgroesse()
										.doubleValue() > 0)) {
							// Fertigungssatzugroesse definiert
							// aber nur verwenden wenn fertigungssatzgroeese >
							// als benoetigeter
							if (bdMenge.compareTo(new BigDecimal(artikelDto
									.getFFertigungssatzgroesse())) < 0) {
								materialbedarfDto
										.setNMenge(new BigDecimal(artikelDto
												.getFFertigungssatzgroesse()));
							} else {
								materialbedarfDto.setNMenge(bdMenge);
							}
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
					} else {
						materialbedarfDto.setNMenge(bdMenge);
					}
					// PJ 15540
					materialbedarfDto
							.setTTermin(Helper
									.cutDate(defaultDatumFuerEintraegeOhneLiefertermin));
					// Zur Liste
					bedarfe.add(materialbedarfDto);
					// diese Menge wird fuer kuenftige bewegungen als bereits
					// vorhanden betrachtet
					bdVoraussLagerstand = bdVoraussLagerstand
							.add(materialbedarfDto.getNMenge());
				}
			} else {
				// --------------------------------------------------------------
				// ------
				// Beginn der Bedarfsermittlung:
				// Die Bewegungsvorschau ist aufsteigend nach Liefertermin
				// sortiert.
				// Zuerst werden alle Bewegungen mit Termin in der Vergangenheit
				// behandelt.
				// Die Summe dieser ergibt einen bzw. keinen Bedarf zum heutigen
				// Tag.
				// --------------------------------------------------------------
				// ------
				boolean bVorHeute = true;
				int i = 0;
				// solange ich "alte" Eintraege krieg.
				while (bVorHeute && i < aBewegungsvorschau.size()) {
					BewegungsvorschauDto bDto = aBewegungsvorschau.get(i);
					// SK 16.3.2009 an begin der Funktion verschoben da sonst
					// durch die Toleranz die Sortierung der Liste verloren geht
					// und bVorHeute nicht mehr stimmt
					// if (bDto.getTLiefertermin() != null) {
					// if (bDto.getCBelegartCNr().equals(LocaleFac.
					// BELEGART_BESTELLUNG)
					// || bDto.getCBelegartCNr().equals(LocaleFac.
					// BELEGART_LOSABLIEFERUNG)
					// //SK: 13.3.09 Mit WH besprochen Bei Bestellung und
					// Losablieferung schieben bei Auftrag nicht
					// /*||
					// bDto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG
					// )*/) {
					//
					// bDto
					// .setTLiefertermin(new Timestamp(
					// bDto.getTLiefertermin().getTime()
					// - (new Long(iToleranz) * 24 * 60 * 60 * 1000)));
					//
					// }
					// }
					// "alter" Eintrag?
					if (!bDto.getTLiefertermin().after(getDate())) {
						bdVoraussLagerstand = bdVoraussLagerstand.add(bDto
								.getNMenge());
						i++;
					}
					// ab jetzt kommen nur mehr zukuenftige Eintraege
					else {
						bVorHeute = false;
					}
				}
				// --------------------------------------------------------------
				// ------
				// Alle "alten" sind abgearbeitet. jetzt pruefe ich, obs einen
				// Bedarf gibt
				// --------------------------------------------------------------
				// ------
				// lieg ich unter Mindestlagerstand?
				if (bdVoraussLagerstand.compareTo(bdLagerMindestBestand) < 0) {
					MaterialbedarfDto materialbedarfDto = new MaterialbedarfDto();
					materialbedarfDto.setIArtikelId(artikelDto.getIId());
					BigDecimal bdMenge = bdLagerSollBestand
							.subtract(bdVoraussLagerstand);
					// Menge auf den Sollbestand auffuellen.
					if ((artikelDto.getFFertigungssatzgroesse() != null)
							&& (artikelDto.getFFertigungssatzgroesse()
									.doubleValue() > 0)) {
						// Fertigungssatzugroesse definiert
						// aber nur verwenden wenn fertigungssatzgroeese > als
						// benoetigeter
						if (bdMenge.compareTo(new BigDecimal(artikelDto
								.getFFertigungssatzgroesse())) < 0) {
							materialbedarfDto.setNMenge(new BigDecimal(
									artikelDto.getFFertigungssatzgroesse()));
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
					} else {
						materialbedarfDto.setNMenge(bdMenge);
					}
					// Termin ist heute
					materialbedarfDto.setTTermin(Helper.cutDate(getDate()));
					// Bezieht sich der Bedarf nur auf einen einzigen
					// Bewegungsvorschau - Eintrag?
					// Dann verknuepf ich ihn mit diesen Daten
					BewegungsvorschauDto bDto = aBewegungsvorschau.get(0);
					if (i == 1 &&
					// SK 14435 Bei Bestellung ist Verknuepfung bloedsinn
							!LocaleFac.BELEGART_BESTELLUNG.equals(bDto
									.getCBelegartCNr())) {

						materialbedarfDto.setCBelegnummer(bDto
								.getCBelegnummer());
						materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
						materialbedarfDto.setIBelegpositionIId(bDto
								.getIBelegPositionIId());
						materialbedarfDto.setProjektIId(bDto.getProjektIId());
						materialbedarfDto.setSBelegartCNr(bDto
								.getCBelegartCNr());
						materialbedarfDto.setNEinkaufpreis(bDto
								.getNEinkaufpreis());
						materialbedarfDto.setLieferantIId(bDto
								.getLieferantIId());
						materialbedarfDto.setXTextinhalt(bDto.getXTextinhalt());
					} else {
						// Versuch die Bewegungsvorschau auf einen Eintrag zu
						// minimieren
						ArrayList<BewegungsvorschauDto> aTempBewegungsvorschau = aBewegungsvorschau;
						ArrayList<BewegungsvorschauDto> alToRemove = new ArrayList<BewegungsvorschauDto>();
						for (int y = 0; y < aTempBewegungsvorschau.size(); y++) {
							BewegungsvorschauDto bwTempDto = aTempBewegungsvorschau
									.get(y);
							if (LocaleFac.BELEGART_LOSABLIEFERUNG
									.equals(bwTempDto.getCBelegartCNr())) {
								LosDto losDto = getFertigungFac()
										.losFindByPrimaryKey(
												bwTempDto.getIBelegIId());
								if (losDto.getAuftragIId() != null) {
									// Wenn Auftrag vorhanden dann diesen und
									// die Ablieferung zum entfernen makieren.
									boolean found = false;
									int x = 0;
									while (!found
											&& x < aTempBewegungsvorschau
													.size()) {
										BewegungsvorschauDto helper = aTempBewegungsvorschau
												.get(x);
										if (LocaleFac.BELEGART_AUFTRAG
												.equals(helper
														.getCBelegartCNr())) {
											if (losDto.getAuftragIId().equals(
													helper.getIBelegIId())) {
												if (bwTempDto
														.getNMenge()
														.add(helper.getNMenge())
														.doubleValue() < 0) {
													bwTempDto
															.setNMenge(bwTempDto
																	.getNMenge()
																	.add(helper
																			.getNMenge()));
													aTempBewegungsvorschau.set(
															y, bwTempDto);
												} else {
													alToRemove
															.add(aTempBewegungsvorschau
																	.get(x));
												}
												alToRemove
														.add(aTempBewegungsvorschau
																.get(y));
												found = true;
											}
										}
										x++;
									}
								}
							}
						}
						// Jetzt alle gefundenen Dtos entfernen
						aTempBewegungsvorschau.removeAll(alToRemove);
						if (aTempBewegungsvorschau.size() > 0) {
							bDto = aTempBewegungsvorschau.get(0);
							if (aTempBewegungsvorschau.size() == 1
									&& !LocaleFac.BELEGART_BESTELLUNG
											.equals(bDto.getCBelegartCNr())) {
								// Wenn nur noch ein Eintrag diesen verknuepfen
								bDto = aTempBewegungsvorschau.get(0);
								materialbedarfDto.setCBelegnummer(bDto
										.getCBelegnummer());
								materialbedarfDto.setIBelegIId(bDto
										.getIBelegIId());
								materialbedarfDto.setProjektIId(bDto
										.getProjektIId());
								materialbedarfDto.setIBelegpositionIId(bDto
										.getIBelegPositionIId());
								materialbedarfDto.setSBelegartCNr(bDto
										.getCBelegartCNr());
								materialbedarfDto.setNEinkaufpreis(bDto
										.getNEinkaufpreis());
								materialbedarfDto.setLieferantIId(bDto
										.getLieferantIId());
								materialbedarfDto.setXTextinhalt(bDto
										.getXTextinhalt());
							} else {

							}
						}
					}
					// Zur Liste
					bedarfe.add(materialbedarfDto);
					// diese Menge wird fuer kuenftige bewegungen als bereits
					// vorhanden betrachtet
					bdVoraussLagerstand = bdVoraussLagerstand
							.add(materialbedarfDto.getNMenge());
				}
				// --------------------------------------------------------------
				// ------
				// weiter mit den zukuenfigen Eintraegen.
				// --------------------------------------------------------------
				// ------
				for (; i < aBewegungsvorschau.size(); i++) {
					// die naechste Bewegung miteinbeziehen.
					BewegungsvorschauDto bDto = aBewegungsvorschau.get(i);
					// SK 16.3.2009 an begin der Funktion verschoben da sonst
					// durch die Toleranz die Sortierung der Liste verloren geht
					// und bVorHeute nicht mehr stimmt
					// if (bDto.getCBelegartCNr() != null) {
					// if (bDto.getCBelegartCNr().equals(
					// LocaleFac.BELEGART_BESTELLUNG)
					// || bDto.getCBelegartCNr().equals(
					// LocaleFac.BELEGART_LOSABLIEFERUNG)) {
					// bDto
					// .setTLiefertermin(new Timestamp(
					// bDto.getTLiefertermin().getTime()
					// - (new Long(iToleranz) * 24 * 60 * 60 * 1000)));
					// }
					// }
					bdVoraussLagerstand = bdVoraussLagerstand.add(bDto
							.getNMenge());
					// Vorher nachsehen ob ich durch die Toleranz einen anderen
					// Lagerstand habe
					/*
					 * Date dToleranz = new
					 * Date(bDto.getTLiefertermin().getTime() + (new
					 * Long(iToleranz)2460601000)); int y=1; BigDecimal
					 * bdFiktiverLagerstand = bdVoraussLagerstand;
					 * while(aBewegungsvorschau
					 * .get(i+y).getTLiefertermin().before(dToleranz)){
					 * bdFiktiverLagerstand =
					 * bdFiktiverLagerstand.add(aBewegungsvorschau
					 * .get(i+y).getNMenge()); y++; }
					 */
					// wenn der Mindestbestand unterschritten ist, hab ich einen
					// bedarf.
					if (bdVoraussLagerstand.compareTo(bdLagerMindestBestand) < 0) {
						MaterialbedarfDto materialbedarfDto = new MaterialbedarfDto();
						materialbedarfDto.setIArtikelId(artikelDto.getIId());
						BigDecimal bdMenge = bdLagerSollBestand
								.subtract(bdVoraussLagerstand);
						// Menge auf den Sollbestand auffuellen.
						if ((artikelDto.getFFertigungssatzgroesse() != null)
								&& (artikelDto.getFFertigungssatzgroesse()
										.doubleValue() > 0)) {
							// Fertigungssatzugroesse definiert
							// aber nur verwenden wenn fertigungssatzgroeese >
							// als benoetigeter
							if (bdMenge.compareTo(new BigDecimal(artikelDto
									.getFFertigungssatzgroesse())) < 0) {
								materialbedarfDto
										.setNMenge(new BigDecimal(artikelDto
												.getFFertigungssatzgroesse()));
							} else {
								materialbedarfDto.setNMenge(bdMenge);
							}
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
						// Termin

						// PJ 14861

						if (bDto.getCBelegartCNr().equals(
								LocaleFac.BELEGART_AUFTRAG)) {
							if (bDto.getKundeDto() != null) {
								iVorlaufzeitInTagen = bDto.getKundeDto()
										.getILieferdauer();
							}
						}

						// wenn die Vorlaufzeit nicht definiert ist, dann gibts
						// es keine
						if (iVorlaufzeitInTagen == null) {
							iVorlaufzeitInTagen = new Integer(0);
						}

						java.sql.Date dTermin = Helper.addiereTageZuDatum(
								new Date(bDto.getTLiefertermin().getTime()),
								-iVorlaufzeitInTagen.intValue());
						// aber ein Tag vor heute darf nicht rauskommen
						if (dTermin.before(getDate())) {
							dTermin = getDate();
						}
						materialbedarfDto.setBTemporaererEintrag(bDto
								.getBTemporaererEintrag());
						materialbedarfDto.setSBelegartCNr(bDto
								.getCBelegartCNr());
						materialbedarfDto.setIBelegpositionIId(bDto
								.getIBelegPositionIId());
						materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
						materialbedarfDto.setCBelegnummer(bDto
								.getCBelegnummer());
						materialbedarfDto.setCBelegnummer(bDto
								.getCBelegnummer());
						materialbedarfDto.setPartnerDto(bDto.getPartnerDto());
						materialbedarfDto.setTTermin(dTermin);
						materialbedarfDto.setProjektIId(bDto.getProjektIId());
						materialbedarfDto.setLieferantIId(bDto
								.getLieferantIId());
						materialbedarfDto.setXTextinhalt(bDto.getXTextinhalt());
						materialbedarfDto.setNEinkaufpreis(bDto
								.getNEinkaufpreis());
						bedarfe.add(materialbedarfDto);
						// diese Menge wird fuer kuenftige bewegungen als
						// bereits vorhanden betrachtet
						bdVoraussLagerstand = bdVoraussLagerstand
								.add(materialbedarfDto.getNMenge());
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bedarfe;
	}

	/**
	 * Alle Eintraege in der internen Bestellung fuer einen Mandanten loeschen.
	 * 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeInternebestellungEinesMandanten(
			boolean bNurHilfsstuecklisten, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		// try {
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRInternebestellung.class);
		c.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR,
				theClientDto.getMandant()));
		List<?> list = c.list();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRInternebestellung ib = (FLRInternebestellung) iter.next();
			Internebestellung toRemove = em.find(Internebestellung.class,
					ib.getI_id());

			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				if (bNurHilfsstuecklisten == true) {
					if (!ib.getFlrstueckliste()
							.getStuecklisteart_c_nr()
							.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
						continue;
					}
				}

				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
		// finally {
		closeSession(session);
		// }
	}

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			Integer iArtikelId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getBewegungsvorschauSortiert(iArtikelId, false, theClientDto);
	}

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			Integer iArtikelId, boolean bInternebestellungMiteinbeziehen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("artikelIId=" + iArtikelId);

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				iArtikelId, theClientDto);
		return getBewegungsvorschauSortiert(artikelDto,
				bInternebestellungMiteinbeziehen, null, false, theClientDto,
				null);

	}

	/**
	 * Daten fuer eine Bewegungsvorschau erstellen
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param bInternebestellungMiteinbeziehen
	 *            boolean
	 * @param alTheoretischeZusatzeintraege
	 *            Optional. Hier koennen der Bewegungsvorschau "kuenstlich"
	 *            weitere Eintraege hinzugefuegt werden.
	 * @param cNrUserI
	 *            String
	 * @return ArrayList
	 * @throws EJBExceptionLP
	 */
	private ArrayList<BewegungsvorschauDto> getBewegungsvorschau(
			ArtikelDto artikelDto, boolean bInternebestellungMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alTheoretischeZusatzeintraege,
			boolean bTermineVorHeuteAufHeute, TheClientDto theClientDto,
			ArrayList<Integer> arLosIId) throws EJBExceptionLP {
		ArrayList<BewegungsvorschauDto> bewegungsvorschau = new ArrayList<BewegungsvorschauDto>();
		// Zusatzeintraege hinzufuegen
		if (alTheoretischeZusatzeintraege != null) {
			// hier nur Eintraege dieses Artikels beruecksichtigen
			for (Iterator<?> iter = alTheoretischeZusatzeintraege.iterator(); iter
					.hasNext();) {
				BewegungsvorschauDto item = (BewegungsvorschauDto) iter.next();
				if (artikelDto.getIId().equals(item.getArtikelIId())) {
					bewegungsvorschau.add(item);
				}
			}
		}

		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// ------------------------------------------------------------------
			// ----
			// Bestelltliste
			// ------------------------------------------------------------------
			// ----
			Criteria cBestellt = session
					.createCriteria(FLRArtikelbestellt.class);
			// Filter nach Artikel
			cBestellt.createAlias(
					ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a")
					.add(Restrictions.eq("a.i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cBestellt
					.addOrder(Order
							.asc(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN));
			// Query ausfuehren
			List<?> listBestellt = cBestellt.list();
			for (Iterator<?> iter = listBestellt.iterator(); iter.hasNext();) {
				FLRArtikelbestellt item = (FLRArtikelbestellt) iter.next();
				if (item.getC_belegartnr()
						.equals(LocaleFac.BELEGART_BESTELLUNG)) {
					BestellpositionDto bestpos = getBestellpositionFac()
							.bestellpositionFindByPrimaryKeyOhneExc(
									item.getI_belegartpositionid());
					if (bestpos != null) { // Bestelltliste koennte inkonsistent
						// sein.
						BestellungDto bestellung = getBestellungFac()
								.bestellungFindByPrimaryKey(
										bestpos.getBestellungIId());
						// wenn die Bestellung "meinem" Mandanten gehoert
						if (!bestellung.getStatusCNr().equals(
								LocaleFac.STATUS_STORNIERT)) {
							// if (bestellung.getTManuellGeliefert() == null) {
							if (bestpos.getBestellpositionstatusCNr() != null
									&& !bestpos.getBestellpositionstatusCNr()
											.equals(LocaleFac.STATUS_ERLEDIGT)) {

								// Bewegungsvorschaueintrag erzeugen
								BewegungsvorschauDto dto = new BewegungsvorschauDto();
								dto.setArtikelIId(item.getFlrartikel()
										.getI_id());
								dto.setCBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
								dto.setCProjekt(bestellung.getCBez());
								dto.setIBelegIId(bestellung.getIId());
								dto.setIBelegPositionIId(item
										.getI_belegartpositionid());
								dto.setNMenge(item.getN_menge());
								dto.setTLiefertermin(new Timestamp(item
										.getT_liefertermin().getTime()));
								dto.setTABTerminBestellung(bestpos
										.getTAuftragsbestaetigungstermin());
								dto.setCBelegnummer(bestellung.getCNr());
								// Lieferant holen
								LieferantDto lieferantDto = getLieferantFac()
										.lieferantFindByPrimaryKey(
												bestellung
														.getLieferantIIdBestelladresse(),
												theClientDto);
								dto.setPartnerDto(lieferantDto.getPartnerDto());
								dto.setMandantCNr(bestellung.getMandantCNr());
								bewegungsvorschau.add(dto);

							}
							// }
						}
					} else {
						// Eintrag in der Bestelltliste, zu dem es gar keine
						// Bestellposition gibt
						// -> loeschen.
						getArtikelbestelltFac().removeArtikelbestellt(
								LocaleFac.BELEGART_BESTELLUNG,
								item.getI_belegartpositionid());
					}
				}
			}
			// ------------------------------------------------------------------
			// ----
			// Reservierungsliste
			// ------------------------------------------------------------------
			// ----
			Criteria cReservierung = session
					.createCriteria(FLRArtikelreservierung.class);
			// Filter nach Artikel
			cReservierung.createAlias(
					ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a")
					.add(Restrictions.eq("a.i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cReservierung
					.addOrder(Order
							.asc(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN));
			// Query ausfuehren
			List<?> listReservierung = cReservierung.list();
			for (Iterator<?> iter = listReservierung.iterator(); iter.hasNext();) {
				FLRArtikelreservierung item = (FLRArtikelreservierung) iter
						.next();
				// --------------------------------------------------------------
				// ------
				// Auftragsreservierung
				// --------------------------------------------------------------
				// ------
				if (item.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragpositionDto aufpos = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKeyOhneExc(
									item.getI_belegartpositionid());
					if (aufpos != null) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(aufpos.getBelegIId());

						// Bewegungsvorschaueintrag erzeugen
						BewegungsvorschauDto dto = new BewegungsvorschauDto();
						dto.setArtikelIId(item.getFlrartikel().getI_id());
						dto.setCBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
						dto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
						dto.setProjektIId(auftragDto.getProjektIId());
						dto.setIBelegIId(auftragDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item
								.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp())
								&& bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);
						dto.setCBelegnummer(auftragDto.getCNr());
						// Kunde holen
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						dto.setPartnerDto(kundeDto.getPartnerDto());
						dto.setKundeDto(kundeDto);

						dto.setXTextinhalt(aufpos.getXTextinhalt());
						dto.setLieferantIId(aufpos.getLieferantIId());
						dto.setNEinkaufpreis(aufpos.getBdEinkaufpreis());
						dto.setMandantCNr(auftragDto.getMandantCNr());
						bewegungsvorschau.add(dto);

					} else {
						// es wurde eine Auftragsreservierung gefunden, zu der
						// es keine Auftragsposition gibt.
						// -> loeschen.
						getReservierungFac().removeArtikelreservierung(
								LocaleFac.BELEGART_AUFTRAG,
								item.getI_belegartpositionid());
					}
				}
				// Losreservierung
				else if (item.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					LossollmaterialDto losmat = getFertigungFac()
							.lossollmaterialFindByPrimaryKeyOhneExc(
									item.getI_belegartpositionid());
					if (losmat != null) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								losmat.getLosIId());

						if (arLosIId != null) {

							// Nur bestimmte Lose beruecksichtigen
							boolean bGefunden = false;
							for (int i = 0; i < arLosIId.size(); i++) {

								if (losDto.getIId().equals(arLosIId.get(i))) {
									bGefunden = true;
									break;
								}

							}

							if (bGefunden == false) {
								// auslassen
								continue;
							}

						}

						// Bewegungsvorschaueintrag erzeugen
						BewegungsvorschauDto dto = new BewegungsvorschauDto();
						dto.setArtikelIId(item.getFlrartikel().getI_id());
						dto.setCBelegartCNr(LocaleFac.BELEGART_LOS);
						dto.setCProjekt(losDto.getCProjekt());
						dto.setMandantCNr(losDto.getMandantCNr());

						dto.setIBelegIId(losDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item
								.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp())
								&& bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);
						dto.setCBelegnummer(losDto.getCNr());
						// wenn das Los auftragsbezogen ist, dann hol ich
						// auch den Kunden
						if (losDto.getAuftragpositionIId() != null
								|| losDto.getAuftragIId() != null) {

							Integer auftragIId = losDto.getAuftragIId();
							if (auftragIId == null) {
								AuftragpositionDto aufposDto = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKey(
												losDto.getAuftragpositionIId());
								auftragIId = aufposDto.getBelegIId();
							}

							// den finde ich ueber die Auftragsposition

							AuftragDto auftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(auftragIId);

							dto.setProjektIId(auftragDto.getProjektIId());

							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(
											auftragDto
													.getKundeIIdAuftragsadresse(),
											theClientDto);
							dto.setPartnerDto(kundeDto.getPartnerDto());
						}
						bewegungsvorschau.add(dto);

					} else {
						// es wurde eine Auftragsreservierung gefunden, zu der
						// es keine Auftragsposition gibt.
						// -> loeschen.
						getReservierungFac().removeArtikelreservierung(
								LocaleFac.BELEGART_LOS,
								item.getI_belegartpositionid());
					}
				} else if (item.getC_belegartnr().equals(
						LocaleFac.BELEGART_KUECHE)) {
					Speiseplanposition speiseplanposition = em.find(
							Speiseplanposition.class,
							item.getI_belegartpositionid());
					if (speiseplanposition != null) {
						SpeiseplanpositionDto speiseplanpositionDto = SpeiseplanpositionDtoAssembler
								.createDto(speiseplanposition);

						// Bewegungsvorschaueintrag erzeugen
						BewegungsvorschauDto dto = new BewegungsvorschauDto();
						dto.setArtikelIId(item.getFlrartikel().getI_id());
						dto.setCBelegartCNr(LocaleFac.BELEGART_KUECHE);

						SpeiseplanDto speiseplanDto = null;
						try {
							speiseplanDto = getKuecheFac()
									.speiseplanFindByPrimaryKey(
											speiseplanpositionDto
													.getSpeiseplanIId());
							dto.setCBelegnummer("K");

							// Projekt= Speisekassa

							KassaartikelDto speisekassaDto = getKuecheFac()
									.kassaartikelFindByPrimaryKey(
											speiseplanDto.getKassaartikelIId());

							dto.setCProjekt(speisekassaDto.getCBez());

							// Kunde = Stuecklistebezeichnung
							/*
							 * StuecklisteDto stuecklisteDto =
							 * getStuecklisteFac() .stuecklisteFindByPrimaryKey(
							 * speiseplanDto.getStuecklisteIId(), theClientDto);
							 */

						} catch (RemoteException ex3) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
						}

						dto.setIBelegIId(speiseplanDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());
						dto.setMandantCNr(speiseplanDto.getMandantCNr());
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item
								.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp())
								&& bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);

						bewegungsvorschau.add(dto);

					} else {
						getReservierungFac().removeArtikelreservierung(
								LocaleFac.BELEGART_KUECHE,
								item.getI_belegartpositionid());
					}

				}
			}
			// ------------------------------------------------------------------
			// ----
			// Fehlmengen
			// ------------------------------------------------------------------
			// ----
			Criteria cFehlmenge = session.createCriteria(FLRFehlmenge.class);
			// Filter nach Artikel
			cFehlmenge
					.add(Restrictions.eq(ArtikelFac.FLR_FEHLMENGE_ARTIKEL_I_ID,
							artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cFehlmenge.addOrder(Order
					.asc(ArtikelFac.FLR_FEHLMENGE_T_LIEFERTERMIN));
			List<?> listFehlmenge = cFehlmenge.list();
			for (Iterator<?> iter = listFehlmenge.iterator(); iter.hasNext();) {
				FLRFehlmenge item = (FLRFehlmenge) iter.next();
				// kontrollieren, ob das los auch von diesem mandanten ist
				if (item.getFlrlossollmaterial() != null) {
					// Los holen
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							item.getFlrlossollmaterial().getFlrlos().getI_id());

					if (arLosIId != null) {

						// Nur bestimmte Lose beruecksichtigen
						boolean bGefunden = false;
						for (int i = 0; i < arLosIId.size(); i++) {

							if (losDto.getIId().equals(arLosIId.get(i))) {
								bGefunden = true;
								break;
							}

						}

						if (bGefunden == false) {
							// auslassen
							continue;
						}

					}

					// Bewegungsvorschaueintrag erzeugen
					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(item.getFlrartikel().getI_id());
					dto.setCBelegartCNr(LocaleFac.BELEGART_LOS);
					dto.setMandantCNr(losDto.getMandantCNr());
					dto.setIBelegIId(losDto.getIId());
					dto.setIBelegPositionIId(item.getFlrlossollmaterial()
							.getI_id());
					dto.setNMenge(item.getN_menge().negate());
					// Termine vor heute werden auf heute umgerechnet
					Timestamp tsTermin = new Timestamp(item.getT_liefertermin()
							.getTime());
					if (tsTermin.before(getTimestamp())) {
						tsTermin = Helper.cutTimestamp(getTimestamp());
					}
					dto.setTLiefertermin(tsTermin);
					dto.setCBelegnummer(item.getFlrlossollmaterial()
							.getFlrlos().getC_nr());
					// wenn das Los auftragsbezogen ist, dann hol ich auch den
					// Kunden
					if (losDto.getAuftragpositionIId() != null) {
						// den finde ich ueber die Auftragsposition
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(
										losDto.getAuftragpositionIId());
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(
										aufposDto.getBelegIId());

						dto.setProjektIId(auftragDto.getProjektIId());

						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						dto.setPartnerDto(kundeDto.getPartnerDto());
					}
					bewegungsvorschau.add(dto);
				}
			}
			// ------------------------------------------------------------------
			// ----
			// noch erwartete Losablieferungen
			// ------------------------------------------------------------------
			// ----
			Criteria cAblieferung = session.createCriteria(FLRLos.class);
			// Filter nach Mandant
			cAblieferung.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			// OHNE STORNIERTE
			cAblieferung.add(Restrictions.not(Restrictions.in(
					FertigungFac.FLR_LOS_STATUS_C_NR, new String[] {
							FertigungFac.STATUS_STORNIERT,
							FertigungFac.STATUS_ERLEDIGT })));
			// Filter nach Artikel
			cAblieferung.createCriteria(FertigungFac.FLR_LOS_FLRSTUECKLISTE)
					.createCriteria(StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL)
					.add(Restrictions.eq("i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin (=Produktionsende)
			cAblieferung.addOrder(Order
					.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
			// Query ausfuehren
			List<?> listAblieferung = cAblieferung.list();
			for (Iterator<?> iter = listAblieferung.iterator(); iter.hasNext();) {
				FLRLos los = (FLRLos) iter.next();
				BigDecimal bdOffen = los.getN_losgroesse();
				// von der Losgroesse die bisherigen Ablieferungen subtrahieren
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2
						.hasNext();) {
					FLRLosablieferung item2 = (FLRLosablieferung) iter2.next();
					bdOffen = bdOffen.subtract(item2.getN_menge());
				}
				if (bdOffen.compareTo(new BigDecimal(0)) > 0) {
					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(los.getFlrstueckliste().getFlrartikel()
							.getI_id());
					dto.setCBelegartCNr(LocaleFac.BELEGART_LOSABLIEFERUNG);
					dto.setIBelegIId(los.getI_id());
					dto.setIBelegPositionIId(null);
					dto.setNMenge(bdOffen);
					dto.setMandantCNr(los.getMandant_c_nr());
					dto.setTLiefertermin(new Timestamp(los
							.getT_produktionsende().getTime()));
					dto.setCBelegnummer(los.getC_nr());
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							los.getI_id());
					// wenn das Los auftragsbezogen ist, dann hol ich auch den
					// Kunden
					if (losDto.getAuftragpositionIId() != null) {
						// den finde ich ueber die Auftragsposition
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(
										losDto.getAuftragpositionIId());
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(
										aufposDto.getBelegIId());
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						dto.setPartnerDto(kundeDto.getPartnerDto());
					}
					bewegungsvorschau.add(dto);
				}
			}
			// Interne Bestellung miteinbeziehen
			if (bInternebestellungMiteinbeziehen) {
				Criteria cIB = session
						.createCriteria(FLRInternebestellung.class);
				// Filter nach Mandant
				cIB.
				// Filter nach Artikel
				createCriteria(
						FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE)
						.createCriteria(
								StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL)
						.add(Restrictions.eq("i_id", artikelDto.getIId()));
				// Sortierung nach Liefertermin (=Produktionsende)
				cIB.addOrder(Order
						.asc(FertigungFac.FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN));
				List<?> listIB = cIB.list();
				for (Iterator<?> iter = listIB.iterator(); iter.hasNext();) {
					FLRInternebestellung ib = (FLRInternebestellung) iter
							.next();
					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(ib.getFlrstueckliste().getFlrartikel()
							.getI_id());
					dto.setCBelegartCNr(ib.getBelegart_c_nr());
					dto.setIBelegIId(ib.getI_belegiid());
					dto.setIBelegPositionIId(ib.getI_belegpositioniid());
					dto.setNMenge(ib.getN_menge());
					dto.setTLiefertermin(new Timestamp(ib.getT_liefertermin()
							.getTime()));
					dto.setMandantCNr(ib.getMandant_c_nr());
					bewegungsvorschau.add(dto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return bewegungsvorschau;
	}

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			ArtikelDto artikelDto, boolean bTermineVorHeuteAufHeute,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<BewegungsvorschauDto> a = getBewegungsvorschau(artikelDto,
				false, null, bTermineVorHeuteAufHeute, theClientDto, null);
		Collections.sort(a, new ComparatorBewegungsvorschau());
		return a;
	}

	private ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(
			ArtikelDto artikelDto, boolean bInternebestellungMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau,
			boolean bTermineVorHeuteAufHeute, TheClientDto theClientDto,
			ArrayList<Integer> arLosIId) throws EJBExceptionLP {
		ArrayList<BewegungsvorschauDto> a = getBewegungsvorschau(artikelDto,
				bInternebestellungMiteinbeziehen,
				alZusatzeintraegeBewegungsvorschau, bTermineVorHeuteAufHeute,
				theClientDto, arLosIId);
		Collections.sort(a, new ComparatorBewegungsvorschau());
		return a;
	}

	/**
	 * Interne Bestellung in Lose ueberleiten
	 * 
	 * @param interneBestellungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void interneBestellungUeberleiten(Integer interneBestellungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Allgemeine standarddurchlaufzeit
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			int iStandarddurchlaufzeit = ((Integer) parameter
					.getCWertAsObject()).intValue();
			int iLieferdauerKunde = 0;
			// nun den Eintrag ueberleiten
			InternebestellungDto ib = internebestellungFindByPrimaryKey(interneBestellungIId);
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(ib.getStuecklisteIId(),
							theClientDto);
			// Los anlegen
			LosDto losDto = new LosDto();
			// Optional: Auftragszuordnung
			if (ib.getBelegartCNr() != null
					&& ib.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				losDto.setAuftragIId(ib.getIBelegiid());

				// Daten aus den Auftragskopfdaten soweit moeglich uebernehmen
				if (ib.getIBelegiid() != null) {
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey(ib.getIBelegiid());
					// Projekt
					losDto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
					// Kostenstelle
					losDto.setKostenstelleIId(auftragDto.getKostIId());

					iLieferdauerKunde = getKundeFac().kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto).getILieferdauer();

				}
				// Zusatznummer ist wie ueblich das I_SORT der Auftragsposition
				parameter = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
				int bLosnummerAuftragsbezogen = ((Integer) parameter
						.getCWertAsObject());
				if (bLosnummerAuftragsbezogen >= 1) {

					parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_FERTIGUNG,
									ParameterFac.PARAMETER_INTERNE_BESTELLUNG_MIT_AUFTRAGSPOSITIONSBEZUG);
					boolean bInternebestellungMitAuftragspositionsbezug = ((Boolean) parameter
							.getCWertAsObject());

					if (bInternebestellungMitAuftragspositionsbezug == true) {

						losDto.setAuftragpositionIId(ib.getIBelegpositionIId());

						if (ib.getIBelegpositionIId() != null) {
							AuftragpositionDto apDto = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKeyOhneExc(
											ib.getIBelegpositionIId());
							if (apDto != null) {
								losDto.setCZusatznummer("" + apDto.getISort());
							}
						}
					}
				}
			}

			// PJ18047
			if (losDto.getAuftragpositionIId() != null) {

				// Wenns die Auftragsposition nicht mehr gibt, dann auslassen
				Auftragposition ap = em.find(Auftragposition.class,
						losDto.getAuftragpositionIId());
				if (ap == null) {
					return;
				}

				Query query = em
						.createNamedQuery("LosfindByAuftragpositionIId");
				query.setParameter(1, losDto.getAuftragpositionIId());
				if (query.getResultList().size() > 0) {
					return;
				}

			}

			losDto.setCKommentar("Interne Bestellung vom "
					+ Helper.formatDatum(getDate(), theClientDto.getLocUi()));
			// Ziel ist default das Hauptlager des Mandanten
			losDto.setLagerIIdZiel(stuecklisteDto.getLagerIIdZiellager());
			// Mandant
			losDto.setMandantCNr(theClientDto.getMandant());
			// Kostenstelle falls vorher noch nicht definiert, kommt aus dem
			// Mandanten
			if (losDto.getKostenstelleIId() == null) {
				losDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
			}
			// Losgroesse
			losDto.setNLosgroesse(ib.getNMenge());
			// Default Fertigungsort ist die Adresse meines Mandanten
			losDto.setPartnerIIdFertigungsort(mandantDto.getPartnerIId());
			// Stueckliste
			losDto.setStuecklisteIId(ib.getStuecklisteIId());
			losDto.setFertigungsgruppeIId(stuecklisteDto
					.getFertigungsgruppeIId());

			if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
				iStandarddurchlaufzeit = stuecklisteDto
						.getNDefaultdurchlaufzeit().intValue();
			}

			Timestamp tAuftragsliefertermin = new Timestamp(ib
					.getTLiefertermin().getTime());

			Timestamp tEnde = Helper.addiereTageZuTimestamp(
					tAuftragsliefertermin, -iLieferdauerKunde);

			Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde,
					-iStandarddurchlaufzeit);

			if (tBeginn.before(Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis())))) {
				tBeginn = Helper.cutTimestamp(new Timestamp(System
						.currentTimeMillis()));
				tEnde = Helper.addiereTageZuTimestamp(tBeginn,
						iStandarddurchlaufzeit);
			}

			losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
			losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));

			// Los anlegen

			try {
				getFertigungFac().createLos(losDto, theClientDto);
				// Abschliessend wird der IB-Eintrag geloescht.
				removeInternebestellung(ib, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() != EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
					throw new EJBExceptionLP(e.getCode(), e.getMessage());
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Die I_ID'saller IB-Eintraege eines Mandanten ermitteln.
	 * 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Set
	 */
	public Set<Integer> getInternebestellungIIdsEinesMandanten(
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		Set<Integer> set = new HashSet<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRInternebestellung.class);
			c.add(Restrictions.eq(
					FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRInternebestellung ib = (FLRInternebestellung) iter.next();
				set.add(ib.getI_id());
			}
		} finally {
			closeSession(session);
		}
		return set;
	}

	public void verdichteInterneBestellung(HashSet<Integer> stuecklisteIIds,
			TheClientDto theClientDto) {
		Iterator it = stuecklisteIIds.iterator();
		while (it.hasNext()) {
			Integer stuecklisteIId = (Integer) it.next();
			verdichteInterneBestellungEinerStuecklisteEinesMandanten(
					stuecklisteIId, 9999, theClientDto);
		}
	}

	private void verdichteInterneBestellungEinerStuecklisteEinesMandanten(
			Integer stuecklisteIId, Integer iVerdichtungsTage,
			TheClientDto theClientDto) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			if (iVerdichtungsTage == null) {
				// Wenn nicht angegeben, zieht der Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM);
				iVerdichtungsTage = (Integer) parameter.getCWertAsObject();
			}
			session = factory.openSession();
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			// Alle BV-Eintraege des Mandanten auflisten.
			Criteria crit = session.createCriteria(FLRInternebestellung.class);
			// Filter nach Mandant
			crit.add(Restrictions.eq(
					FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Filter nach Artikel
			crit.add(Restrictions.eq(
					FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID,
					stuecklisteDto.getIId()));
			// Sortierung nach Liefertermin.
			crit.addOrder(Order
					.asc(FertigungFac.FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN));
			List<?> resultList = crit.list();
			// Der IB-Eintrag auf den hinverdichtet werden soll.
			Internebestellung ibEntityZiel = null;
			// nun alle Eintraege durchiterieren.
			for (Iterator<?> itBV = resultList.iterator(); itBV.hasNext();) {
				FLRInternebestellung ib = (FLRInternebestellung) itBV.next();
				Internebestellung ibEntity = em.find(Internebestellung.class,
						ib.getI_id());
				if (ibEntity == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				if (ibEntityZiel == null) {
					ibEntityZiel = ibEntity;
				} else {
					Date dErsteIB = ibEntityZiel.getTLiefertermin();
					Date dZweiteIB = ibEntity.getTLiefertermin();
					// einen Folgeeintrag anschaun.
					if (Helper.getDifferenzInTagen(dErsteIB, dZweiteIB) < iVerdichtungsTage) {
						// Menge des 2. Eintrags zum ersten addieren
						ibEntityZiel.setNMenge(ibEntityZiel.getNMenge().add(
								ibEntity.getNMenge()));
						// und den 2. loeschen
						InternebestellungDto intBesDto = context
								.getBusinessObject(InternebestellungFac.class)
								.internebestellungFindByPrimaryKey(
										ibEntity.getIId());
						context.getBusinessObject(InternebestellungFac.class)
								.removeInternebestellung(intBesDto,
										theClientDto);
					} else {
						// Differen groesser -> als neues Verdichtungsziel
						// festlegen.
						ibEntityZiel = ibEntity;
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	/**
	 * IB-Eintraege eines Mandanten verdichten.
	 * 
	 * @param iVerdichtungsTage
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void verdichteInterneBestellung(Integer iVerdichtungsTage,
			TheClientDto theClientDto) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			// Zuerst die ID's aller vorkommenden Stuecklisten holen.
			String queryString = "select distinct ib."
					+ FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID
					+ " from FLRInternebestellung ib where ib."
					+ FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR + "='"
					+ theClientDto.getMandant() + "'";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			// nun die Eintraege fuer jede Stueckliste verdichten.
			for (Iterator<?> itStk = rowCountResult.iterator(); itStk.hasNext();) {
				Integer stuecklisteIId = (Integer) itStk.next();
				verdichteInterneBestellungEinerStuecklisteEinesMandanten(
						stuecklisteIId, iVerdichtungsTage, theClientDto);
			}
		} finally {
			closeSession(session);
		}
	}

	public BigDecimal getFiktivenLagerstandZuZeitpunkt(ArtikelDto artikelDto,
			TheClientDto theClientDto, Timestamp tLagerstandsdatum) {
		ArrayList<BewegungsvorschauDto> alVorschauDtos = getBewegungsvorschauSortiert(
				artikelDto, false, theClientDto);
		LagerDto lagerDto;
		BigDecimal bdfiktiverLagerstand = new BigDecimal(0);
		try {
			lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);
			bdfiktiverLagerstand = getLagerFac().getLagerstandOhneExc(
					artikelDto.getIId(), lagerDto.getIId(), theClientDto);
		} catch (EJBExceptionLP e) {
			// Wenn kein Lager gefunden wurde mit 0 anfangen.
		} catch (RemoteException e) {
			// Wenn kein Lager gefunden wurde mit 0 anfangen.
		}
		int i = 0;
		boolean finished = false;
		while (i < alVorschauDtos.size() && !finished) {
			BewegungsvorschauDto bwvsDto = alVorschauDtos.get(i);
			if (bwvsDto.getTLiefertermin().before(tLagerstandsdatum)
					|| bwvsDto.getTLiefertermin().equals(tLagerstandsdatum)) {
				bdfiktiverLagerstand = bdfiktiverLagerstand.add(bwvsDto
						.getNMenge());
			} else {
				finished = true;
			}
			i++;
		}
		return bdfiktiverLagerstand;
	}

}
