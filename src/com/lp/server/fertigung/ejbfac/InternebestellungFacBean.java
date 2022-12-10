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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.ejb.Fasessioneintrag;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionReport;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.fertigung.ejb.Internebestellung;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.Loslagerentnahme;
import com.lp.server.fertigung.ejb.Lospruefplan;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.ejb.Lossollmaterial;
import com.lp.server.fertigung.ejb.Lostechniker;
import com.lp.server.fertigung.fastlanereader.generated.FLRInternebestellung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLoslagerentnahme;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.fertigung.service.InternebestellungDtoAssembler;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.MaterialbedarfDto;
import com.lp.server.forecast.ejb.Fclieferadresse;
import com.lp.server.forecast.ejb.Forecast;
import com.lp.server.forecast.ejb.Forecastauftrag;
import com.lp.server.forecast.ejb.Forecastposition;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.kueche.ejb.Speiseplanposition;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.SpeiseplanpositionDto;
import com.lp.server.kueche.service.SpeiseplanpositionDtoAssembler;
import com.lp.server.lieferschein.ejb.Packstueck;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.ejb.Maschinenzeitdaten;
import com.lp.server.personal.ejb.Zeitverteilung;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class InternebestellungFacBean extends Facade implements InternebestellungFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public InternebestellungDto createInternebestellung(InternebestellungDto internebestellungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// PJ22059

		StuecklisteDto stkDto = getStuecklisteFac()
				.stuecklisteFindByPrimaryKey(internebestellungDto.getStuecklisteIId(), theClientDto);

		BigDecimal bdMaxFertigungssatzgroesse = null;
		if (stkDto.getArtikelDto().getFMaxfertigungssatzgroesse() != null) {
			bdMaxFertigungssatzgroesse = new BigDecimal(stkDto.getArtikelDto().getFMaxfertigungssatzgroesse());
		}

		BigDecimal bdNochZuFertigen = internebestellungDto.getNMenge();

		while (bdNochZuFertigen.doubleValue() > 0) {

			BigDecimal losmenge = null;
			if (bdMaxFertigungssatzgroesse != null && bdMaxFertigungssatzgroesse.doubleValue() > 0) {

				if (bdNochZuFertigen.doubleValue() < bdMaxFertigungssatzgroesse.doubleValue()) {
					losmenge = bdNochZuFertigen;
				} else {
					losmenge = bdMaxFertigungssatzgroesse;
				}

			} else {
				losmenge = bdNochZuFertigen;

			}

			// primary key
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_INTERNEBESTELLUNG);
			internebestellungDto.setIId(iId);
			// User
			internebestellungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			internebestellungDto.setTAendern(getTimestamp());
			internebestellungDto.setNMenge(losmenge);

			try {
				Internebestellung internebestellung = new Internebestellung(internebestellungDto.getIId(),
						internebestellungDto.getMandantCNr(), internebestellungDto.getBelegartCNr(),
						internebestellungDto.getIBelegiid(), internebestellungDto.getStuecklisteIId(),
						internebestellungDto.getNMenge(),
						new Timestamp(internebestellungDto.getTLiefertermin().getTime()),
						internebestellungDto.getPersonalIIdAendern(), internebestellungDto.getTProduktionsbeginn());
				em.persist(internebestellung);
				em.flush();
				internebestellungDto.setTAendern(internebestellung.getTAendern());
				setInternebestellungFromInternebestellungDto(internebestellung, internebestellungDto);

			} catch (EntityExistsException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
			}

			bdNochZuFertigen = bdNochZuFertigen.subtract(losmenge);

		}
		return internebestellungDto;
	}

	public void removeInternebestellung(InternebestellungDto internebestellungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		if (internebestellungDto != null) {
			Integer iId = internebestellungDto.getIId();
			Internebestellung toRemove = em.find(Internebestellung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public InternebestellungDto updateInternebestellung(InternebestellungDto internebestellungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (internebestellungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("internebestellungDto == null"));
		}
		internebestellungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		internebestellungDto.setTAendern(getTimestamp());

		Integer iId = internebestellungDto.getIId();
		// try {
		Internebestellung internebestellung = em.find(Internebestellung.class, iId);
		if (internebestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setInternebestellungFromInternebestellungDto(internebestellung, internebestellungDto);
		return internebestellungDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
	}

	public InternebestellungDto internebestellungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Internebestellung internebestellung = em.find(Internebestellung.class, iId);
		if (internebestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleInternebestellungDto(internebestellung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setInternebestellungFromInternebestellungDto(Internebestellung internebestellung,
			InternebestellungDto internebestellungDto) {
		internebestellung.setMandantCNr(internebestellungDto.getMandantCNr());
		internebestellung.setBelegartCNr(internebestellungDto.getBelegartCNr());
		internebestellung.setIBelegiid(internebestellungDto.getIBelegiid());
		internebestellung.setStuecklisteIId(internebestellungDto.getStuecklisteIId());
		internebestellung.setNMenge(internebestellungDto.getNMenge());
		internebestellung.setTLiefertermin(internebestellungDto.getTLiefertermin());
		internebestellung.setPersonalIIdAendern(internebestellungDto.getPersonalIIdAendern());
		internebestellung.setTAendern(internebestellungDto.getTAendern());
		internebestellung.setInternebestellungIIdElternlos(internebestellungDto.getInternebestellungIIdElternlos());
		internebestellung.setIBelegpositionIId(internebestellungDto.getIBelegpositionIId());
		internebestellung.setTProduktionsbeginn(internebestellungDto.getTProduktionsbeginn());
		internebestellung.setPartnerIIdStandort(internebestellungDto.getPartnerIIdStandort());
		internebestellung.setAuftragIIdKopfauftrag(internebestellungDto.getAuftragIIdKopfauftrag());
		internebestellung.setXAusloeser(internebestellungDto.getXAusloeser());
		internebestellung.setFLagermindest(internebestellungDto.getFLagermindest());

		em.merge(internebestellung);
		em.flush();
	}

	private InternebestellungDto assembleInternebestellungDto(Internebestellung internebestellung) {
		return InternebestellungDtoAssembler.createDto(internebestellung);
	}

	private InternebestellungDto[] assembleInternebestellungDtos(Collection<?> internebestellungs) {
		List<InternebestellungDto> list = new ArrayList<InternebestellungDto>();
		if (internebestellungs != null) {
			Iterator<?> iterator = internebestellungs.iterator();
			while (iterator.hasNext()) {
				Internebestellung internebestellung = (Internebestellung) iterator.next();
				list.add(assembleInternebestellungDto(internebestellung));
			}
		}
		InternebestellungDto[] returnArray = new InternebestellungDto[list.size()];
		return (InternebestellungDto[]) list.toArray(returnArray);
	}

	/**
	 * Erzeugen der Internen Bestellung fuer eine Stueckliste mit den Daten des
	 * Mandanten, auf dem der Benutzer angemeldet ist.
	 * 
	 * @param iVorlaufzeit                             Integer
	 * @param dLieferterminFuerArtikelOhneReservierung Date
	 * @param stuecklisteIId                           Integer
	 * @param alZusatzeintraegeBewegungsvorschau       Optional. Hier koennen der
	 *                                                 Bewegungsvorschau
	 *                                                 "kuenstlich" weitere
	 *                                                 Eintraege hinzugefuegt
	 *                                                 werden.
	 * @param theClientDto                             String
	 * @throws EJBExceptionLP
	 * @return BigDecimal bestellte Menge (exklusive eventuell vorhandener
	 *         Unterstuecklisten)
	 */
	public ArrayList<Integer> erzeugeInterneBestellung(Integer iVorlaufzeitAuftrag, Integer iVorlaufzeitUnterlose,
			Integer iToleranz, java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			boolean bInterneBestellungBeruecksichtigen, Integer stuecklisteIId,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau, boolean bInterneBestellung,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, boolean bTermineBelassen,
			HashSet hmReservierungenVorhanden, boolean bUnterlos, boolean bExakterAuftragsbezug,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData("erzeugeInterneBestellung fuer stuecklisteIId=" + stuecklisteIId);
		ArrayList<Integer> alUnterstuecklisten = new ArrayList<Integer>();
		try {
			// Stueckliste holen
			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId,
					theClientDto);
			// Artikel immer holen. Da dieser nicht immer mit dem StuecklisteDto
			// mitgeliefert wird.
			ArtikelDto artikelDto = stuecklisteDto.getArtikelDto();
			if (artikelDto == null) {
				artikelDto = getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(), theClientDto);
			}

			/*
			 * if (artikelDto.getCNr().equals("VAT852036")) { int z = 0; }
			 */

			// CK: Projekt 6699, Wenn Stkl fremdgefertigt, dann darf Sie nicht
			// in der Internen Bestellung aufscheinen
			if (stuecklisteDto != null && Helper.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
				if (stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_STUECKLISTE)
						|| stuecklisteDto.getStuecklisteartCNr()
								.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {

					if (Helper.short2Boolean(artikelDto.getBLagerbewirtschaftet())) {

						Integer partnerIIdStandortStkl = getLagerFac()
								.lagerFindByPrimaryKey(stuecklisteDto.getLagerIIdZiellager()).getPartnerIIdStandort();

						ArrayList<?> aBedarfe = berechneBedarfe(artikelDto, iVorlaufzeitAuftrag, iVorlaufzeitUnterlose,
								iToleranz, dLieferterminFuerArtikelOhneReservierung, bInterneBestellungBeruecksichtigen,
								alZusatzeintraegeBewegungsvorschau, bInterneBestellung, theClientDto, null,
								bNichtFreigegebeneAuftraegeBeruecksichtigen, partnerIIdStandortStkl,
								hmReservierungenVorhanden, bExakterAuftragsbezug);
						// Positionen der Stkl. Da die interne Bestellung auch
						// fuer Unterstuecklisten durchgefuehrt werden muss.
						StuecklistepositionDto[] stkposDto = getStuecklisteFac()
								.stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);
						// fuer jeden Bedarf eine Interne Bestellung anlegen
						for (Iterator<?> iter = aBedarfe.iterator(); iter.hasNext();) {
							MaterialbedarfDto matBed = (MaterialbedarfDto) iter.next();
							InternebestellungDto ibDto = new InternebestellungDto();
							ibDto.setBelegartCNr(matBed.getSBelegartCNr());
							ibDto.setIBelegiid(matBed.getIBelegIId());
							ibDto.setAuftragIIdKopfauftrag(matBed.getAuftragIIdKopfauftrag());

							if (bUnterlos == false || (matBed.getSBelegartCNr() != null
									&& matBed.getSBelegartCNr().equals(LocaleFac.BELEGART_FORECAST))) {
								// SP8326
								if (matBed.getBTemporaererEintrag() == false || (matBed.getSBelegartCNr() != null
										&& matBed.getSBelegartCNr().equals(LocaleFac.BELEGART_FORECAST))) {
									ibDto.setIBelegpositionIId(matBed.getIBelegpositionIId());
								}
							}

							ibDto.setFLagermindest(matBed.getFLagermindest());

							ibDto.setPartnerIIdStandort(matBed.getPartnerIIdStandort());
							ibDto.setInternebestellungIIdElternlos(matBed.getIInternebestellungIIdElternlos());
							ibDto.setMandantCNr(theClientDto.getMandant());

							// SP4344 Standort der Stueckliste kommt aus
							// Ziellager
							ibDto.setPartnerIIdStandort(
									getLagerFac().lagerFindByPrimaryKey(stuecklisteDto.getLagerIIdZiellager())
											.getPartnerIIdStandort());

							// PJ18451
							if (artikelDto.getFUeberproduktion() != null) {
								matBed.setNMenge(matBed.getNMenge().add(Helper.getProzentWert(matBed.getNMenge(),
										new BigDecimal(artikelDto.getFUeberproduktion()), 4)));
							}

							ibDto.setXAusloeser(matBed.getXAusloeser());

							ibDto.setNMenge(matBed.getNMenge());
							ibDto.setStuecklisteIId(matBed.getIStuecklisteIId());

							// SP2708
							// Durchlaufzeit der uebergeordneten
							// Stueckliste bestimmen.
							Integer iDurchlaufzeit;
							if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
								// Ist optional in der Stueckliste
								// definiert.
								iDurchlaufzeit = stuecklisteDto.getNDefaultdurchlaufzeit().intValue();
							} else {
								// Falls in der Stueckliste nicht
								// definiert, dann zieht der
								// Mandantenparameter
								ParametermandantDto parameter = getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
										ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
								iDurchlaufzeit = ((Integer) parameter.getCWertAsObject()).intValue();
							}

							// Hilfsstueckliste hat Durchlaufzeit von 0
							if (stuecklisteDto.getStuecklisteartCNr()
									.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
								iDurchlaufzeit = 0;
							}

							if (bTermineBelassen) {

								// PJ20684
								java.sql.Date dEnde = matBed.getTTermin();

								java.sql.Date dHeute = Helper.cutDate(new java.sql.Date(System.currentTimeMillis()));

								if (dEnde.before(dHeute)) {
									dEnde = dHeute;
								}

								ibDto.setTLiefertermin(dEnde);
								java.sql.Date dProduktionbeginnTermin = Helper.addiereTageZuDatum(dEnde,
										-iDurchlaufzeit);
								if (dProduktionbeginnTermin.before(dHeute)) {
									dProduktionbeginnTermin = dHeute;
								}

								ibDto.setTProduktionsbeginn(dProduktionbeginnTermin);
							} else {
								ibDto.setTLiefertermin(matBed.getTTermin());
								java.sql.Date dProduktionbeginnTermin = Helper.addiereTageZuDatum(matBed.getTTermin(),
										-iDurchlaufzeit);
								ibDto.setTProduktionsbeginn(dProduktionbeginnTermin);
							}

							ibDto.setStuecklisteIId(stuecklisteIId);

							// anlegen

							ibDto = context.getBusinessObject(InternebestellungFac.class).createInternebestellung(ibDto,
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
											.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stkpos.getArtikelIId(),
													theClientDto);

									// Position ist auch eine Stueckliste
									if (stkUnter != null) {

										ArtikelDto aPosDto = getArtikelFac()
												.artikelFindByPrimaryKeySmall(stkpos.getArtikelIId(), theClientDto);

										// Aufgrund von SP5617 auskommentiert
										// if (!stuecklisteDto
										// .getStuecklisteartCNr()
										// .equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE))
										// {

										if (!alUnterstuecklisten.contains(stkUnter.getIId())) {
											alUnterstuecklisten.add(stkUnter.getIId());
											System.out.println("UNTER-ADD " + aPosDto.getCNr());

										}

										// }

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
										bewegungsvorschauDto.setArtikelIId(stkpos.getArtikelIId());

										bewegungsvorschauDto
												.setAuftragIIdKopfauftrag(matBed.getAuftragIIdKopfauftrag());

										bewegungsvorschauDto.setCBelegartCNr(matBed.getSBelegartCNr());
										bewegungsvorschauDto.setCBelegnummer(matBed.getCBelegnummer());

										bewegungsvorschauDto.setIBelegIId(matBed.getIBelegIId());
										if (bUnterlos == false || (matBed.getSBelegartCNr() != null
												&& matBed.getSBelegartCNr().equals(LocaleFac.BELEGART_FORECAST))) {
											// SP8326
											if (matBed.getBTemporaererEintrag() == false
													|| (matBed.getSBelegartCNr() != null && matBed.getSBelegartCNr()
															.equals(LocaleFac.BELEGART_FORECAST))) {
												bewegungsvorschauDto
														.setIBelegPositionIId(matBed.getIBelegpositionIId());
											}
										}
										// Erforderliche Menge = Bedarf der
										// uebergeordneten * positionsmenge
										BigDecimal bdMenge = matBed.getNMenge().multiply(stkpos.getNMenge());

										// PJ19151
										BigDecimal faktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
												aPosDto.getEinheitCNr(), stkpos.getEinheitCNr(), stkpos.getIId(),
												theClientDto);

										if (faktor != null && faktor.doubleValue() != 0) {
											bdMenge = bdMenge.divide(faktor, 12, BigDecimal.ROUND_HALF_EVEN);
										}

										// SP2700
										if (stuecklisteDto.getNErfassungsfaktor() != null
												&& stuecklisteDto.getNErfassungsfaktor().doubleValue() != 0) {
											bdMenge = bdMenge.divide(
													new BigDecimal(stuecklisteDto.getNErfassungsfaktor().doubleValue()),
													4, BigDecimal.ROUND_HALF_EVEN);
										}

										/*
										 * if(aPosDto.getCNr().equals("BG2059")) { int z=0; }
										 */
										// Termin ist der Termin der
										// uebergeordneten minus deren
										// durchlaufzeit
										java.sql.Date dTermin = Helper.addiereTageZuDatum(matBed.getTTermin(),
												-iDurchlaufzeit);
										// Da Vordatierungen vor den heutigen
										// Tag sowie die Vorlaufzeit in der
										// Bedarfsermittlung
										// behandelt werden, brauch ich mich
										// hier nicht drum kuemmern.
										bewegungsvorschauDto
												.setTLiefertermin(new java.sql.Timestamp(dTermin.getTime()));

										if (matBed.getXAusloeser() != null) {
											bewegungsvorschauDto.setAusloeser(matBed.getXAusloeser() + "\n"
													+ getTextRespectUISpr("stkl.unterstkl", theClientDto.getMandant(),
															theClientDto.getLocUi())
													+ " " + artikelDto.getCNr() + " "
													+ Helper.formatZahl(bdMenge, 2, theClientDto.getLocUi()) + " "
													+ Helper.formatDatum(bewegungsvorschauDto.getTLiefertermin(),
															theClientDto.getLocUi()));
										} else {
											bewegungsvorschauDto.setAusloeser(getTextRespectUISpr("stkl.unterstkl",
													theClientDto.getMandant(), theClientDto.getLocUi()) + " "
													+ artikelDto.getCNr() + " "
													+ Helper.formatZahl(bdMenge, 2, theClientDto.getLocUi()) + " "
													+ Helper.formatDatum(bewegungsvorschauDto.getTLiefertermin(),
															theClientDto.getLocUi()));
										}
										bewegungsvorschauDto
												.setNMenge(bdMenge.negate().setScale(4, RoundingMode.HALF_UP));
										bewegungsvorschauDto.setPartnerDto(matBed.getPartnerDto());

										// Temporaeren Eintrag die Liste
										// einfuegen
										bewegungsvorschauDto.setBTemporaererEintrag(true);

										bewegungsvorschauDto.setMandantCNr(ibDto.getMandantCNr());

										alZusatzeintraegeBewegungsvorschau.add(bewegungsvorschauDto);
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
			FLRInternebestellung intBest = (FLRInternebestellung) iterator.next();

			try {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						intBest.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
				BigDecimal bdRahmenRes = getReservierungFac().getAnzahlRahmenreservierungen(artikelDto.getIId(),
						theClientDto);
				boolean bLagermindest = false;
				if (artikelDto.getFLagermindest() != null) {
					if (bdRahmenRes != null) {
						if (artikelDto.getFLagermindest().doubleValue() > bdRahmenRes.doubleValue()) {
							Object[] o = new Object[5];
							o[0] = artikelDto.getCNr();
							o[1] = new BigDecimal(artikelDto.getFLagermindest().doubleValue());
							o[2] = bdRahmenRes;
							o[3] = intBest.getI_id();
							o[4] = getTextRespectUISpr("fert.internebestellung.einlagermindesstand",
									theClientDto.getMandant(), theClientDto.getLocUi());
							al.add(o);
							bLagermindest = true;
						}
					}
				}
				if (!bLagermindest) {
					if (artikelDto.getFFertigungssatzgroesse() != null) {
						if (bdRahmenRes != null) {
							if (artikelDto.getFFertigungssatzgroesse().doubleValue() > bdRahmenRes.doubleValue()) {
								Object[] o = new Object[5];
								o[0] = artikelDto.getCNr();
								o[1] = new BigDecimal(artikelDto.getFFertigungssatzgroesse().doubleValue());
								o[2] = bdRahmenRes;
								o[3] = intBest.getI_id();
								o[4] = getTextRespectUISpr("fert.internebestellung.einefertigungssatzgroesse",
										theClientDto.getMandant(), theClientDto.getLocUi());
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

	// PJ20004
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void ueberproduktionZuruecknehmen(TheClientDto theClientDto) {

		TreeMap<String, Integer> tm = getStuecklisteFac().holeAlleWurzelstuecklisten(true, theClientDto);

		Iterator it = tm.keySet().iterator();
		int zaehler = 0;

		while (it.hasNext()) {
			String stkl = (String) it.next();
			Integer stuecklisteIId = tm.get(stkl);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			if (stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_STUECKLISTE)
					&& stklDto.getArtikelDto().isLagerbewirtschaftet()) {

				ArrayList<Integer> alStuecklistenInDerRichtigenReihenfolge = new ArrayList<Integer>();
				alStuecklistenInDerRichtigenReihenfolge.add(stuecklisteIId);

				alStuecklistenInDerRichtigenReihenfolge = holeAlleUnterstuecklisten(stuecklisteIId,
						alStuecklistenInDerRichtigenReihenfolge, theClientDto);

				for (int i = 0; i < alStuecklistenInDerRichtigenReihenfolge.size(); i++) {
					ueberproduktionZuruecknehmen(alStuecklistenInDerRichtigenReihenfolge.get(i), theClientDto);
				}

				System.out.println("Stkl " + zaehler + " von " + tm.size());
			}
			zaehler++;

		}

	}

	private ArrayList<Integer> holeAlleUnterstuecklisten(Integer stuecklisteIId, ArrayList<Integer> alUnterstuecklisten,
			TheClientDto theClientDto) {

		TreeMap<String, Integer> tmPositionen = getStuecklisteFac().holeNaechsteEbene(stuecklisteIId, true,
				theClientDto);

		Iterator it = tmPositionen.keySet().iterator();

		while (it.hasNext()) {
			String s = (String) it.next();
			Integer artikelIId = tmPositionen.get(s);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
					theClientDto);

			if (stklDto != null) {
				alUnterstuecklisten.add(stklDto.getIId());

				alUnterstuecklisten = holeAlleUnterstuecklisten(stklDto.getIId(), alUnterstuecklisten, theClientDto);

			}

		}

		return alUnterstuecklisten;
	}

	private void ueberproduktionZuruecknehmen(Integer stuecklisteIId, TheClientDto theClientDto) {

		try {
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			Integer partnerIIdStandortStkl = getLagerFac().lagerFindByPrimaryKey(stklDto.getLagerIIdZiellager())
					.getPartnerIIdStandort();

			Double dLagermindest = 0D;
			Double dLagersoll = 0D;
			if (lagerMinJeLager(theClientDto)) {

				BigDecimal[] bd = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(
						stklDto.getArtikelIId(), partnerIIdStandortStkl, theClientDto);

				dLagersoll = new Double(bd[1].doubleValue());
				dLagermindest = new Double(bd[0].doubleValue());

			} else {
				if (stklDto.getArtikelDto().getFLagersoll() != null) {
					dLagersoll = stklDto.getArtikelDto().getFLagersoll();
				}

				if (stklDto.getArtikelDto().getFLagermindest() != null) {
					dLagermindest = stklDto.getArtikelDto().getFLagermindest();
				}

			}

			BigDecimal anfangslagerstand = getAnfangslagerstand(stklDto.getArtikelDto(), true, theClientDto,
					partnerIIdStandortStkl);

			ArrayList<?> list = getInternebestellungFac().getBewegungsvorschauSortiert(stklDto.getArtikelIId(), false,
					false, theClientDto, partnerIIdStandortStkl, false, false, false, new HashSet());

			BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[list.size()];

			BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) list
					.toArray(returnArray);

			for (int i = 0; i < dtos.length; i++) {

				BewegungsvorschauDto dto = dtos[i];

				anfangslagerstand = anfangslagerstand.add(dto.getNMenge());

				dto.setBdFiktiverLagerstand(anfangslagerstand);

			}

			BigDecimal bdLetzerFiktiverLagerstand = anfangslagerstand;

			if (bdLetzerFiktiverLagerstand.subtract(new BigDecimal(dLagersoll)).doubleValue() > 0) {
				BigDecimal bdMengeAnLosablieferungenZuReduzieren = bdLetzerFiktiverLagerstand
						.subtract(new BigDecimal(dLagersoll));

				BigDecimal bdMaximalReduzierbarBisZurNaechstenLosablieferung = bdMengeAnLosablieferungenZuReduzieren;
				for (int i = dtos.length - 1; i >= 0; i--) {

					BewegungsvorschauDto dto = dtos[i];
					if (dto.getBdFiktiverLagerstand().subtract(new BigDecimal(dLagermindest))
							.doubleValue() < bdMaximalReduzierbarBisZurNaechstenLosablieferung.doubleValue()) {
						bdMaximalReduzierbarBisZurNaechstenLosablieferung = dto.getBdFiktiverLagerstand()
								.subtract(new BigDecimal(dLagermindest));
					}

					if (dto.getCBelegartCNr() != null && dto.getCBelegartCNr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)
							&& dto.getNMenge().doubleValue() > 0) {
						LosDto lDto = getFertigungFac().losFindByPrimaryKey(dto.getIBelegIId());

						if (dto.getNMenge().doubleValue() <= bdMaximalReduzierbarBisZurNaechstenLosablieferung
								.doubleValue()) {

							// Wenn das Los kleiner ist

							// Stornieren wenn Angelegt
							// bzw. reduzieren wenn Teilerledigt

							if (lDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)) {
								getFertigungFac().gebeLosAusRueckgaengig(lDto.getIId(), false, theClientDto);
								lDto = getFertigungFac().losFindByPrimaryKey(dto.getIBelegIId());
							}

							if (lDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
								getFertigungFac().storniereLos(lDto.getIId(), false, theClientDto);

							} else {
								getFertigungFac().aendereLosgroesse(dto.getIBelegIId(),
										lDto.getNLosgroesse().subtract(dto.getNMenge()).intValue(), true, theClientDto);
							}

							bdMengeAnLosablieferungenZuReduzieren = bdMengeAnLosablieferungenZuReduzieren
									.subtract(dto.getNMenge());
							bdMaximalReduzierbarBisZurNaechstenLosablieferung = bdMengeAnLosablieferungenZuReduzieren;

						} else {

							// Wenn das Los groesser ist

							if (lDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {

								lDto.setNLosgroesse(lDto.getNLosgroesse()
										.subtract(bdMaximalReduzierbarBisZurNaechstenLosablieferung));
								getFertigungFac().updateLos(lDto, true, theClientDto);

							} else {
								getFertigungFac().aendereLosgroesse(dto.getIBelegIId(),
										lDto.getNLosgroesse()
												.subtract(bdMaximalReduzierbarBisZurNaechstenLosablieferung).intValue(),
										true, theClientDto);
							}

							bdMengeAnLosablieferungenZuReduzieren = bdMengeAnLosablieferungenZuReduzieren
									.subtract(bdMaximalReduzierbarBisZurNaechstenLosablieferung);
							bdMaximalReduzierbarBisZurNaechstenLosablieferung = bdMengeAnLosablieferungenZuReduzieren;

						}

						if (bdMengeAnLosablieferungenZuReduzieren.doubleValue() <= 0) {
							break;
						}

					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void loescheAngelegteUndStornierteLoseAufEinmal(Integer fertigungsgruppeIId_Entfernen,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		System.out.println(
				"loescheAngelegteUndStornierteLoseAufEinmal Start: " + new Timestamp(System.currentTimeMillis()));

		String queryStringWhereLOS_I_IDsQuery = "select l.i_id from FLRLos l where l.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND l.status_c_nr IN ('" + FertigungFac.STATUS_ANGELEGT + "','"
				+ FertigungFac.STATUS_STORNIERT + "')";

		if (fertigungsgruppeIId_Entfernen != null) {
			queryStringWhereLOS_I_IDsQuery += " AND l.fertigungsgruppe_i_id=" + fertigungsgruppeIId_Entfernen;
		}
		queryStringWhereLOS_I_IDsQuery += "GROUP BY l.i_id HAVING  (select count(*) FROM FLRZeitdaten z WHERE z.i_belegartid=l.i_id AND z.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "') = 0";
		org.hibernate.Query queryIN = session.createQuery(queryStringWhereLOS_I_IDsQuery);

		List<?> resultForIN = queryIN.list();
		Iterator itForIn = resultForIN.iterator();

		String queryStringWhereLOS_I_IDs = "";

		int i = 0;
		int iPaket = 0;

		System.out.println("Loesche AngelegteUndStornierte: GESAMT: " + resultForIN.size());

		while (itForIn.hasNext()) {
			Integer losIId = (Integer) itForIn.next();

			queryStringWhereLOS_I_IDs += losIId + "";

			if (i == 100 || itForIn.hasNext() == false) {

				iPaket++;
				System.out.println("Loesche AngelegteUndStornierte: PAKET " + iPaket);
				getInternebestellungFac().loescheAngelegteUndStornierteLoseAufEinmal(queryStringWhereLOS_I_IDs,
						theClientDto);
				i=0;
			} else {
				queryStringWhereLOS_I_IDs += ",";
			}

			i++;

		}

	}

	public void loescheAngelegteUndStornierteLoseAufEinmal(String queryStringWhereLOS_I_IDs,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String hqlDelete = "delete FROM FLRLospruefplan WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "update FLRLossollmaterial pos set pos.lossollmaterial_i_id_original = NULL  WHERE pos.lossollmaterial_i_id_original IS NOT NULL AND pos.los_i_id IN ("
				+ queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRLoslagerentnahme WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		// Sollarbeitsplan

		String queryStringWhereLOSSOLLARBEITSPLAN_I_IDs = "select so.i_id from FLRLossollarbeitsplan so where los_i_id IN ("
				+ queryStringWhereLOS_I_IDs + ")";

		hqlDelete = "delete FROM FLRZeitverteilung WHERE lossollarbeitsplan_i_id IN ("
				+ queryStringWhereLOSSOLLARBEITSPLAN_I_IDs + ")";

		hqlDelete = "delete FROM FLRMaschinenzeitdaten WHERE lossollarbeitsplan_i_id IN ("
				+ queryStringWhereLOSSOLLARBEITSPLAN_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();
		hqlDelete = "delete FROM FLRLossollarbeitsplan WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		// Sollmaterial

		String queryStringWhereLOSSOLLMATERIAL_I_IDs = "select so.i_id from FLRLossollmaterial so where los_i_id IN ("
				+ queryStringWhereLOS_I_IDs + ")";

		session.createQuery(hqlDelete).executeUpdate();
		hqlDelete = "update FLRBestellposition pos set pos.lossollmaterial_i_id = NULL  WHERE pos.lossollmaterial_i_id IN ("
				+ queryStringWhereLOSSOLLMATERIAL_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "update FLRAnfrageposition pos set pos.lossollmaterial_i_id = NULL  WHERE pos.lossollmaterial_i_id IN ("
				+ queryStringWhereLOSSOLLMATERIAL_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "update FLRLossollarbeitsplan pos set pos.lossollmaterial_i_id = NULL  WHERE pos.lossollmaterial_i_id IS NOT NULL pos.los_i_id IN ("
				+ queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRLossollmaterial WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRLoszusatzstatus WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRZeitverteilung WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRLostechniker WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRPackstueck WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		hqlDelete = "delete FROM FLRFasessioneintrag WHERE los_i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		// Zum Schluss die Lose
		hqlDelete = "delete FROM FLRLos WHERE i_id IN (" + queryStringWhereLOS_I_IDs + ")";
		session.createQuery(hqlDelete).executeUpdate();

		System.out.println(
				"loescheAngelegteUndStornierteLoseAufEinmal Stop: " + new Timestamp(System.currentTimeMillis()));

	}

	private void loescheAlleAngelegteUndStornierteLose(Integer fertigungsgruppeIId_Entfernen,
			TheClientDto theClientDto) {
		String queryString = "select l from FLRLos l where l.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND l.status_c_nr IN ('" + FertigungFac.STATUS_ANGELEGT + "','" + FertigungFac.STATUS_STORNIERT
				+ "')";

		if (fertigungsgruppeIId_Entfernen != null) {
			queryString += " AND l.fertigungsgruppe_i_id=" + fertigungsgruppeIId_Entfernen;
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> rowCountResult = query.list();
		// nun die Eintraege fuer jede Stueckliste verdichten.
		for (Iterator<?> itStk = rowCountResult.iterator(); itStk.hasNext();) {
			FLRLos l = (FLRLos) itStk.next();

			// PJ9671
			if (!getZeiterfassungFac().sindBelegzeitenVorhanden(LocaleFac.BELEGART_LOS, l.getI_id())) {
				getInternebestellungFac().loescheAngelegtesOderStorniertesLos(l.getI_id(), theClientDto);
			}

		}

		session.close();
	}

	public void loescheAngelegtesOderStorniertesLos(Integer losIId, TheClientDto theClientDto) {
		Los los = em.find(Los.class, losIId);

		los.setStatusCNr(FertigungFac.STATUS_ANGELEGT);

		try {
			LoslagerentnahmeDto[] loslagerentnahmeDto = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

			for (int i = 0; i < loslagerentnahmeDto.length; i++) {

				Loslagerentnahme loslagerentnahme = em.find(Loslagerentnahme.class, loslagerentnahmeDto[i].getIId());

				em.remove(loslagerentnahme);

			}

			Query queryPruefplan = em.createNamedQuery("LospruefplanFindByLosIId");
			queryPruefplan.setParameter(1, losIId);

			Collection<?> clPruefplan = queryPruefplan.getResultList();

			Iterator<?> iteratorPruefplan = clPruefplan.iterator();
			while (iteratorPruefplan.hasNext()) {
				Lospruefplan lospruefplanTemp = (Lospruefplan) iteratorPruefplan.next();
				em.remove(lospruefplanTemp);

			}

			LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac().lossollarbeitsplanFindByLosIId(losIId);

			for (int i = 0; i < lossollarbeitsplanDto.length; i++) {

				Query q3 = em.createNamedQuery("MaschinenzeitdatenfindByLossollarbeitsplanIId");
				q3.setParameter(1, lossollarbeitsplanDto[i].getIId());

				Collection<?> clQ3 = q3.getResultList();

				Iterator<?> iteratorQ3 = clQ3.iterator();
				while (iteratorQ3.hasNext()) {
					Maschinenzeitdaten mz = (Maschinenzeitdaten) iteratorQ3.next();
					em.remove(mz);

				}

				Query q4 = em.createNamedQuery("ZeitverteilungfindByLossollarbeitsplanIId");
				q4.setParameter(1, lossollarbeitsplanDto[i].getIId());

				Collection<?> clQ4 = q4.getResultList();

				Iterator<?> iteratorQ4 = clQ4.iterator();
				while (iteratorQ4.hasNext()) {
					Zeitverteilung mz = (Zeitverteilung) iteratorQ4.next();
					em.remove(mz);

				}

				getFertigungFac().removeLossollarbeitsplan(lossollarbeitsplanDto[i], theClientDto);

			}

			LossollmaterialDto[] lossollmaterialDto = getFertigungFac().lossollmaterialFindByLosIId(losIId);

			for (int i = 0; i < lossollmaterialDto.length; i++) {
				getFertigungFac().verknuepfungZuBestellpositionUndArbeitsplanLoeschen(lossollmaterialDto[i].getIId(),
						true);
				getFertigungFac().removeLossollmaterial(lossollmaterialDto[i], theClientDto);

			}
			LoszusatzstatusDto[] loszusatzstatusDto = getFertigungFac().loszusatzstatusFindByLosIIdOhneExc(losIId);

			for (int i = 0; i < loszusatzstatusDto.length; i++) {

				getFertigungFac().removeLoszusatzstatus(loszusatzstatusDto[i]);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Query queryFA = em.createNamedQuery("FasessioneintragFindByLosIId");
		queryFA.setParameter(1, losIId);
		Collection c = queryFA.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Fasessioneintrag fa = (Fasessioneintrag) it.next();
			em.remove(fa);
		}

		Query queryLT = em.createNamedQuery("LostechnikerfindByLosIId");
		queryLT.setParameter(1, losIId);
		Collection cLT = queryLT.getResultList();
		Iterator itLT = cLT.iterator();
		while (itLT.hasNext()) {
			Lostechniker lt = (Lostechniker) itLT.next();
			em.remove(lt);
		}

		Query queryPS = em.createNamedQuery("PackstueckFindFyLosIId");
		queryPS.setParameter(1, losIId);
		Collection cPS = queryPS.getResultList();
		Iterator itPS = cPS.iterator();
		while (itPS.hasNext()) {
			Packstueck lt = (Packstueck) itPS.next();
			em.remove(lt);
		}

		em.remove(los);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void erzeugeInterneBestellung(Boolean bVorhandeneLoeschen, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			Boolean bVerdichten, Integer iVerdichtungsTage, boolean bInterneBestellung, ArrayList<Integer> losIIds,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, ArrayList<Integer> arAuftragIId,
			Integer fertigungsgruppeIId_Entfernen, boolean bExakterAuftragsbezug, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException, IllegalStateException {
		long lStart = System.currentTimeMillis();
		Session session = null;
		try {

			// Einstellungen speichern
			ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();

			alDtos.add(new KeyvalueDto("ErzeugtAm", Helper
					.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()), theClientDto.getLocUi())));
			alDtos.add(new KeyvalueDto("ErzeugtVon", getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).getCKurzzeichen()));
			alDtos.add(new KeyvalueDto("Vorlaufzeit", iVorlaufzeitAuftrag + ""));
			alDtos.add(new KeyvalueDto("VorlaufzeitUnterlose", iVorlaufzeitUnterlose + ""));
			alDtos.add(new KeyvalueDto("Toleranz", iToleranz + ""));
			alDtos.add(new KeyvalueDto("TageVerdichten", iVerdichtungsTage + ""));
			alDtos.add(new KeyvalueDto("Verdichten", Helper.boolean2Short(bVerdichten) + ""));
			alDtos.add(new KeyvalueDto("VorhandeneLoeschen", Helper.boolean2Short(bVorhandeneLoeschen) + ""));
			alDtos.add(new KeyvalueDto("LieferterminFuerArtikelOhneReservierung",
					Helper.formatDatum(dLieferterminFuerArtikelOhneReservierung, theClientDto.getLocUi())));
			alDtos.add(new KeyvalueDto("NichtFreigegebeneAuftraegeBeruecksichtigen",
					Helper.boolean2Short(bNichtFreigegebeneAuftraegeBeruecksichtigen) + ""));
			alDtos.add(new KeyvalueDto("exakterAuftragsbezug", Helper.boolean2Short(bExakterAuftragsbezug) + ""));

			if (fertigungsgruppeIId_Entfernen != null) {
				alDtos.add(new KeyvalueDto("LoseEntfernen",
						getStuecklisteFac().fertigungsgruppeFindByPrimaryKey(fertigungsgruppeIId_Entfernen).getCBez()));
			}

			// falls gewuenscht - vorhandene IB-Daten loeschen
			if (bVorhandeneLoeschen) {
				context.getBusinessObject(InternebestellungFac.class).removeInternebestellungEinesMandanten(false,
						theClientDto);
			}
			// Aufgrund PJ22526 auskommentiert
			/*
			 * // PJ20684 ParametermandantDto parameter =
			 * getParameterFac().getMandantparameter(theClientDto.getMandant(),
			 * ParameterFac.KATEGORIE_FERTIGUNG,
			 * ParameterFac.PARAMETER_INTERNE_BESTELLUNG_ANGELEGTE_ENTFERNEN); boolean
			 * bAngelegteEntfernen = ((Boolean) parameter.getCWertAsObject()); if
			 * (bAngelegteEntfernen == true) {
			 * loescheAlleAngelegteUndStornierteLose(fertigungsgruppeIId_Entfernen,
			 * theClientDto); }
			 */

			// PJ20684
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNE_BESTELLUNG_TERMINE_BELASSEN);
			boolean bTermineBelassen = ((Boolean) parameter.getCWertAsObject());

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Alle Stuecklisten dieses Mandanten
			Criteria c = session.createCriteria(FLRStueckliste.class);
			c.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
			// c.add(Restrictions.between("i_id", 161, 161));

			// PJ18740
			if ((losIIds != null && losIIds.size() > 0) || (arAuftragIId != null && arAuftragIId.size() > 0)) {
				ArrayList alStkls = new ArrayList();

				if (losIIds != null && losIIds.size() > 0) {
					String lose = "";

					for (int x = 0; x < losIIds.size(); x++) {

						lose += getFertigungFac().losFindByPrimaryKey(losIIds.get(x)).getCNr() + ",";

						LossollmaterialDto[] sollDtos = getFertigungFac().lossollmaterialFindByLosIId(losIIds.get(x));
						for (int i = 0; i < sollDtos.length; i++) {
							if (sollDtos[i].getArtikelIId() != null) {
								StuecklisteDto stklDto = getStuecklisteFac()
										.stuecklisteFindByMandantCNrArtikelIIdOhneExc(sollDtos[i].getArtikelIId(),
												theClientDto);
								if (stklDto != null) {
									alStkls.add(stklDto.getIId());
								}
							}
						}
					}
					alDtos.add(new KeyvalueDto("Lose", lose));

				}

				if (arAuftragIId != null && arAuftragIId.size() > 0) {
					String auftraege = "";
					if (arAuftragIId != null) {

						for (int i = 0; i < arAuftragIId.size(); i++) {
							auftraege += getAuftragFac().auftragFindByPrimaryKey(arAuftragIId.get(i)).getCNr() + ",";

							AuftragpositionDto[] aufposDtos = getAuftragpositionFac()
									.auftragpositionFindByAuftrag(arAuftragIId.get(i));
							for (int j = 0; j < aufposDtos.length; j++) {
								AuftragpositionDto apDto = aufposDtos[j];

								if (apDto.getArtikelIId() != null) {
									StuecklisteDto stklDto = getStuecklisteFac()
											.stuecklisteFindByMandantCNrArtikelIIdOhneExc(apDto.getArtikelIId(),
													theClientDto);
									if (stklDto != null) {
										alStkls.add(stklDto.getIId());
									}
								}

							}

						}

						c.add(Restrictions.in("i_id", alStkls));

					}
					alDtos.add(new KeyvalueDto("Auftraege", auftraege));

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

			HashSet hmReservierungenVorhanden = getReservierungFac().getSetOfArtikelIdMitReservierungen();

			// DISTINCT

			int iZaehler = 0;

			// fuer jede Stueckliste die interne Bestellung durchfuehren
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRStueckliste stkl = (FLRStueckliste) iter.next();
				alUterstuecklisten.addAll(erzeugeInterneBestellung(iVorlaufzeitAuftrag, iVorlaufzeitUnterlose,
						iToleranz, dLieferterminFuerArtikelOhneReservierung, true, stkl.getI_id(),
						alZusatzeintraegeBewegungsvorschau, bInterneBestellung,
						bNichtFreigegebeneAuftraegeBeruecksichtigen, bTermineBelassen, hmReservierungenVorhanden, false,
						bExakterAuftragsbezug, theClientDto));
				// gegebenenfalls verdichten.
				if (bVerdichten) {
					getInternebestellungFac().verdichteInterneBestellungEinerStuecklisteEinesMandanten(stkl.getI_id(),
							iVerdichtungsTage, theClientDto);
				}

				iZaehler++;
				System.out.println("STKL " + iZaehler + " von " + list.size() + " STKL_I_ID:" + stkl.getI_id());

			}
			int counter = 0;
			while (!alUterstuecklisten.isEmpty()) {
				counter++;
				if (counter > 50) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN,
							new Exception(
									"Mehr als 50 Ebenen von Unterst\u00FCcklisten vorhanden. Betroffen Stueckliste:"
											+ getStuecklisteFac().stuecklisteFindByPrimaryKey(alUterstuecklisten.get(0),
													theClientDto).getArtikelDto().getCNr()));
				}
				ArrayList<Integer> alTempUnterstuecklisten = new ArrayList<Integer>();

				iZaehler = 0;

				for (int i = 0; i < alUterstuecklisten.size(); i++) {

					ArrayList<Integer> alTempUnterstuecklistenProStueckliste = erzeugeInterneBestellung(
							iVorlaufzeitUnterlose, iVorlaufzeitUnterlose, iToleranz,
							dLieferterminFuerArtikelOhneReservierung, true, alUterstuecklisten.get(i),
							alZusatzeintraegeBewegungsvorschau, bInterneBestellung,
							bNichtFreigegebeneAuftraegeBeruecksichtigen, bTermineBelassen, hmReservierungenVorhanden,
							true, bExakterAuftragsbezug, theClientDto);

					for (int j = 0; j < alTempUnterstuecklistenProStueckliste.size(); j++) {
						if (!alTempUnterstuecklisten.contains(alTempUnterstuecklistenProStueckliste.get(j))) {
							alTempUnterstuecklisten.add(alTempUnterstuecklistenProStueckliste.get(j));
						}
					}
					// gegebenenfalls verdichten.
					if (bVerdichten) {
						getInternebestellungFac().verdichteInterneBestellungEinerStuecklisteEinesMandanten(
								alUterstuecklisten.get(i), iVerdichtungsTage, theClientDto);
					}
					iZaehler++;
					System.out.println("UNTERSTKL " + iZaehler + " von " + alUterstuecklisten.size() + " STKL_I_ID:"
							+ alUterstuecklisten.get(i));
				}

				alUterstuecklisten = alTempUnterstuecklisten;
			}

			// SP3203 Alle Hilfsstuecklsten entfernen

			context.getBusinessObject(InternebestellungFac.class).removeInternebestellungEinesMandanten(true,
					theClientDto);
			alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTE_INTERNE_BESTELLUNG, alDtos);
		} finally {
			closeSession(session);
		}
	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. nur zur externen verwendung,
	 * da eintraege aus der internen bestellung unberuecksichtigt bleiben.
	 * 
	 * @param artikelDto                                ArtikelDto
	 * @param iVorlaufzeitInTagen                       Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin Date
	 * @param theClientDto                              String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	// TODO ghp: weg
	// @TransactionTimeout(45)
	@org.jboss.ejb3.annotation.TransactionTimeout(600)
	public ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, Integer partnerIIdStandort,
			HashSet hmReservierungenVorhanden, boolean bExakterAuftragsbezug) throws EJBExceptionLP {
		ArrayList<MaterialbedarfDto> result = berechneBedarfe(artikelDto, iVorlaufzeitAuftrag, iVorlaufzeitUnterlose,
				iToleranz, defaultDatumFuerEintraegeOhneLiefertermin, false, null, bInterneBestellung, theClientDto,
				arLosIId, bNichtFreigegebeneAuftraegeBeruecksichtigen, partnerIIdStandort, hmReservierungenVorhanden,
				bExakterAuftragsbezug);
		return result;
	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. nur zur externen verwendung,
	 * da eintraege aus der internen bestellung unberuecksichtigt bleiben.
	 * 
	 * @param artikelIId                                ArtikelDto
	 * @param iVorlaufzeitInTagen                       Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin Date
	 * @param theClientDto                              String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	public ArrayList<MaterialbedarfDto> berechneBedarfe(Integer artikelIId, Integer iVorlaufzeitAuftrag,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInterneBestellung, TheClientDto theClientDto, ArrayList<Integer> arLosIId,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, Integer partnerIIdStandort,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP {

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		return berechneBedarfe(artikelDto, iVorlaufzeitAuftrag, iVorlaufzeitUnterlose, iToleranz,
				defaultDatumFuerEintraegeOhneLiefertermin, false, null, bInterneBestellung, theClientDto, arLosIId,
				bNichtFreigegebeneAuftraegeBeruecksichtigen, partnerIIdStandort, hmReservierungenVorhanden, false);

	}

	/**
	 * hier wird die Bedarfsermittlung durchgefuehrt. Im ermittelten Liefertermin
	 * (Bedarfstermin) wird die Vorlaufzeit bereits beruecksichtigt. Bedarfe an
	 * gleichen Terminen werden komprimiert.
	 * 
	 * @param artikelDto                                ArtikelDto
	 * @param iVorlaufzeitAuftragInTagen                Integer
	 * @param defaultDatumFuerEintraegeOhneLiefertermin Date
	 * @param bInternebestellungMiteinbeziehen          boolean
	 * @param alZusatzeintraegeBewegungsvorschau        Optional. Hier koennen der
	 *                                                  Bewegungsvorschau
	 *                                                  "kuenstlich" weitere
	 *                                                  Eintraege hinzugefuegt
	 *                                                  werden.
	 * @param theClientDto                              String
	 * @throws EJBExceptionLP
	 * @return ArrayList mit MaterialbedarfDto's
	 */
	private ArrayList<MaterialbedarfDto> berechneBedarfe(ArtikelDto artikelDto, Integer iVorlaufzeitAuftragInTagen,
			Integer iVorlaufzeitUnterlose, Integer iToleranz, Date defaultDatumFuerEintraegeOhneLiefertermin,
			boolean bInternebestellungMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau, boolean bInterneBestellung,
			TheClientDto theClientDto, ArrayList<Integer> arLosIId, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			Integer partnerIIdStandort, HashSet hmReservierungenVorhanden, boolean bExakterAuftragsbezug)
			throws EJBExceptionLP {
		ArrayList<MaterialbedarfDto> bedarfe = new ArrayList<MaterialbedarfDto>();

		// SP4385
		Integer partnerIIdStandortStucklistenZiellager = partnerIIdStandort;
		if (bInterneBestellung == true) {
			partnerIIdStandort = null;
		}

		try {
			// voraussichtlicher lagerstand. Initialisiert mit dem aktuellen
			// Lagerstand.
			// BigDecimal bdVoraussLagerstand = getLagerFac()
			// .getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(),
			// theClientDto);

			boolean bGetrennteLaeger = getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto);

			BigDecimal bdVoraussLagerstand = getAnfangslagerstand(artikelDto, bInterneBestellung, theClientDto,
					partnerIIdStandort);

			/*
			 * if(artikelDto.getCNr().equals("BG2059")) { int z=0; }
			 */

			/*
			 * myLogger.warn("bwvs0: artikelId=" + artikelDto.getIId() + ".");
			 */
			Timestamp ts = getTimestamp();
			ArrayList<BewegungsvorschauDto> aBewegungsvorschau = getBewegungsvorschauSortiert(artikelDto,
					bInternebestellungMiteinbeziehen, false, alZusatzeintraegeBewegungsvorschau, true, theClientDto,
					arLosIId, partnerIIdStandort, false, !bGetrennteLaeger, bNichtFreigegebeneAuftraegeBeruecksichtigen,
					hmReservierungenVorhanden);

			// PJ21794 Fuer alle Auftrags-Eintraege in der internen Bestellung nachsehen, ob
			// es schon Lose mit der gleichen offenen Menge gibt, die direkt dem Auftrag
			// zugeordnet sind
			// diese heben sich dann gegenseitig auf
			// PJ22510 Auch fuer AF/Bestellvorschlag
			if (bExakterAuftragsbezug) {

				for (int y = 0; y < aBewegungsvorschau.size(); y++) {
					BewegungsvorschauDto bewDtoAuftrag = aBewegungsvorschau.get(y);
					if (bewDtoAuftrag.getCBelegartCNr() != null
							&& bewDtoAuftrag.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
							&& bewDtoAuftrag.getIBelegPositionIId() != null) {

						for (int z = 0; z < aBewegungsvorschau.size(); z++) {
							BewegungsvorschauDto bewDtoBewegungsvorschau = aBewegungsvorschau.get(z);

							if (bInterneBestellung) {

								if (bewDtoBewegungsvorschau.getCBelegartCNr() != null
										&& bewDtoBewegungsvorschau.getCBelegartCNr()
												.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)
										&& bewDtoBewegungsvorschau.getIBelegIId() != null) {

									LosDto losDto = getFertigungFac()
											.losFindByPrimaryKey(bewDtoBewegungsvorschau.getIBelegIId());

									if (losDto.getAuftragpositionIId() != null
											&& losDto.getAuftragpositionIId()
													.equals(bewDtoAuftrag.getIBelegPositionIId())
											&& bewDtoAuftrag.getNMenge().negate()
													.doubleValue() == bewDtoBewegungsvorschau.getNMenge()
															.doubleValue()) {

										aBewegungsvorschau.remove(bewDtoAuftrag);
										aBewegungsvorschau.remove(bewDtoBewegungsvorschau);
										break;

									}

								}
							} else {

								if (bewDtoBewegungsvorschau.getCBelegartCNr() != null
										&& bewDtoBewegungsvorschau.getCBelegartCNr()
												.equals(LocaleFac.BELEGART_BESTELLUNG)
										&& bewDtoBewegungsvorschau.getIBelegIId() != null) {

									BestellungDto bestellungDto = getBestellungFac()
											.bestellungFindByPrimaryKey(bewDtoBewegungsvorschau.getIBelegIId());

									if (bestellungDto.getAuftragIId() != null && bewDtoAuftrag.getIBelegIId() != null
											&& bewDtoAuftrag.getIBelegIId().equals(bestellungDto.getAuftragIId())) {

										if (bewDtoAuftrag.getNMenge().negate().doubleValue() == bewDtoBewegungsvorschau
												.getNMenge().doubleValue()) {

											aBewegungsvorschau.remove(bewDtoAuftrag);
											aBewegungsvorschau.remove(bewDtoBewegungsvorschau);
											break;
										}

									}

								}

							}
						}

					}
				}

			}

			Timestamp tsNow = getTimestamp();
			/*
			 * myLogger.warn("bwvs1: artikelId=" + artikelDto.getIId() + " mit " +
			 * (aBewegungsvorschau == null ? -1 : aBewegungsvorschau.size()) +
			 * " Eintraegen (Dauer: " + (tsNow.getTime() - ts.getTime()) + " ms");
			 */
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
					if (LocaleFac.BELEGART_BESTELLUNG.equals(bewDto.getCBelegartCNr())
							|| LocaleFac.BELEGART_LOSABLIEFERUNG.equals(bewDto.getCBelegartCNr())
					// SK: 13.3.09 Mit WH besprochen Bei Bestellung und
					// Losablieferung schieben bei Auftrag nicht
					/*
					 * || bDto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
					 */) {
						Timestamp tBefore = bewDto.getTLiefertermin();

						if (bewDto.getTABTerminBestellung() != null) {
							tBefore = new Timestamp(bewDto.getTABTerminBestellung().getTime());
						}

						Timestamp tAfter = new Timestamp(
								tBefore.getTime() - (new Long(iToleranz) * 24 * 60 * 60 * 1000));
						bewDto.setTLiefertermin(tAfter);
						if (bewDto.getTABTerminBestellung() != null) {
							bewDto.setTABTerminBestellung(new Date(tAfter.getTime()));
						}
					}
				}
				aBewegungsvorschau.set(y, bewDto);
			}
			// Nun sind alle Termine nach Toleranz richtig gesetzt wieder neu
			// sortieren
			Collections.sort(aBewegungsvorschau, new ComparatorBewegungsvorschau());
			BigDecimal bdLagerMindestBestand;
			BigDecimal bdLagerSollBestand;

			if (partnerIIdStandortStucklistenZiellager != null) {
				BigDecimal[] minsoll = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(
						artikelDto.getIId(), partnerIIdStandortStucklistenZiellager, theClientDto);
				bdLagerMindestBestand = minsoll[0];
				bdLagerSollBestand = minsoll[1];

				// SP6679
				if (bdLagerSollBestand.doubleValue() < bdLagerMindestBestand.doubleValue()) {
					bdLagerSollBestand = bdLagerMindestBestand;
				}

			} else {
				if (artikelDto.getFLagermindest() != null) {
					bdLagerMindestBestand = new BigDecimal(artikelDto.getFLagermindest());
				} else {
					bdLagerMindestBestand = new BigDecimal(0);
				}
				bdLagerSollBestand = getLagerFac().getArtikelSollBestand(artikelDto);
			}

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
					materialbedarfDto.setPartnerIIdStandort(partnerIIdStandortStucklistenZiellager);
					materialbedarfDto.setFLagermindest(bdLagerMindestBestand.doubleValue());

					BigDecimal bdMenge = bdLagerSollBestand.subtract(bdVoraussLagerstand);
					// Menge auf den Sollbestand auffuellen. SK Bzw
					// Fertigungssatzgroesse verwenden
					if (bInterneBestellung) {
						if ((artikelDto.getFFertigungssatzgroesse() != null)
								&& (artikelDto.getFFertigungssatzgroesse().doubleValue() > 0)) {
							// SP8191
							materialbedarfDto.setNMenge(Helper.aufVielfachesAufrunden(bdMenge,
									new BigDecimal(artikelDto.getFFertigungssatzgroesse())));
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
					} else {
						materialbedarfDto.setNMenge(bdMenge);
					}

					materialbedarfDto.setXAusloeser(getTextRespectUISpr("fert.internebestellung.lagermindest.ausloeser",
							theClientDto.getMandant(), theClientDto.getLocUi(),
							Helper.formatZahl(bdLagerMindestBestand, 2, theClientDto.getLocUi()),
							Helper.formatZahl(bdLagerSollBestand, 2, theClientDto.getLocUi())));

					// PJ 15540
					materialbedarfDto.setTTermin(Helper.cutDate(defaultDatumFuerEintraegeOhneLiefertermin));
					// Zur Liste
					bedarfe.add(materialbedarfDto);
					// diese Menge wird fuer kuenftige bewegungen als bereits
					// vorhanden betrachtet
					bdVoraussLagerstand = bdVoraussLagerstand.add(materialbedarfDto.getNMenge());
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
						bdVoraussLagerstand = bdVoraussLagerstand.add(bDto.getNMenge());
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
					materialbedarfDto.setPartnerIIdStandort(partnerIIdStandortStucklistenZiellager);
					materialbedarfDto.setFLagermindest(bdLagerMindestBestand.doubleValue());

					BigDecimal bdMenge = bdLagerSollBestand.subtract(bdVoraussLagerstand);
					// Menge auf den Sollbestand auffuellen.
					if (bInterneBestellung && (artikelDto.getFFertigungssatzgroesse() != null)
							&& (artikelDto.getFFertigungssatzgroesse().doubleValue() > 0)) {
						// Fertigungssatzugroesse definiert
						// aber nur verwenden wenn fertigungssatzgroeese > als
						// benoetigeter
						if (bdMenge.compareTo(new BigDecimal(artikelDto.getFFertigungssatzgroesse())) < 0) {
							materialbedarfDto.setNMenge(new BigDecimal(artikelDto.getFFertigungssatzgroesse()));
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
					} else {
						materialbedarfDto.setNMenge(bdMenge);
					}

					// if (artikelDto.getCNr().equals("20002")) {
					// int z = 0;
					// }

					// Termin ist heute
					materialbedarfDto.setTTermin(Helper.cutDate(getDate()));
					// Bezieht sich der Bedarf nur auf einen einzigen
					// Bewegungsvorschau - Eintrag?
					// Dann verknuepf ich ihn mit diesen Daten
					// PJ20652
					// SP9303
					BewegungsvorschauDto bDto = aBewegungsvorschau.get(0);

					// SP7721 Zum letzten Bedarf zuruecksuchen
					if (bDto.getNMenge().doubleValue() > 0) {
						for (int b = aBewegungsvorschau.size() - 1; b >= 0; b--) {
							if (aBewegungsvorschau.get(b).getNMenge().doubleValue() < 0) {
								bDto = aBewegungsvorschau.get(b);
							}
						}
					}

					materialbedarfDto.setAuftragIIdKopfauftrag(bDto.getAuftragIIdKopfauftrag());
					materialbedarfDto.setSBelegartCNr(bDto.getCBelegartCNr());
					materialbedarfDto.setCBelegnummer(bDto.getCBelegnummer());
					materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
					materialbedarfDto.setIBelegpositionIId(bDto.getIBelegPositionIId());
					materialbedarfDto.setXAusloeser(bDto.getAusloeser());

					if (i == 1 &&
					// SK 14435 Bei Bestellung ist Verknuepfung bloedsinn
							!LocaleFac.BELEGART_BESTELLUNG.equals(bDto.getCBelegartCNr())) {

						materialbedarfDto.setCBelegnummer(bDto.getCBelegnummer());
						materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
						materialbedarfDto.setIBelegpositionIId(bDto.getIBelegPositionIId());
						materialbedarfDto.setProjektIId(bDto.getProjektIId());
						materialbedarfDto.setSBelegartCNr(bDto.getCBelegartCNr());
						materialbedarfDto.setNEinkaufpreis(bDto.getNEinkaufpreis());
						materialbedarfDto.setLieferantIId(bDto.getLieferantIId());
						materialbedarfDto.setXTextinhalt(bDto.getXTextinhalt());

					} else {
						// Versuch die Bewegungsvorschau auf einen Eintrag zu
						// minimieren
						ArrayList<BewegungsvorschauDto> aTempBewegungsvorschau = aBewegungsvorschau;
						ArrayList<BewegungsvorschauDto> alToRemove = new ArrayList<BewegungsvorschauDto>();
						for (int y = 0; y < aTempBewegungsvorschau.size(); y++) {
							BewegungsvorschauDto bwTempDto = aTempBewegungsvorschau.get(y);
							if (LocaleFac.BELEGART_LOSABLIEFERUNG.equals(bwTempDto.getCBelegartCNr())) {
								LosDto losDto = getFertigungFac().losFindByPrimaryKey(bwTempDto.getIBelegIId());
								if (losDto.getAuftragIId() != null) {
									// Wenn Auftrag vorhanden dann diesen und
									// die Ablieferung zum entfernen makieren.
									boolean found = false;
									int x = 0;
									while (!found && x < aTempBewegungsvorschau.size()) {
										BewegungsvorschauDto helper = aTempBewegungsvorschau.get(x);
										if (LocaleFac.BELEGART_AUFTRAG.equals(helper.getCBelegartCNr())) {
											if (losDto.getAuftragIId().equals(helper.getIBelegIId())) {
												if (bwTempDto.getNMenge().add(helper.getNMenge()).doubleValue() < 0) {
													bwTempDto.setNMenge(bwTempDto.getNMenge().add(helper.getNMenge()));
													aTempBewegungsvorschau.set(y, bwTempDto);
												} else {
													alToRemove.add(aTempBewegungsvorschau.get(x));
												}
												alToRemove.add(aTempBewegungsvorschau.get(y));
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
									&& !LocaleFac.BELEGART_BESTELLUNG.equals(bDto.getCBelegartCNr())) {
								// Wenn nur noch ein Eintrag diesen verknuepfen
								bDto = aTempBewegungsvorschau.get(0);
								materialbedarfDto.setCBelegnummer(bDto.getCBelegnummer());
								materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
								materialbedarfDto.setProjektIId(bDto.getProjektIId());
								materialbedarfDto.setIBelegpositionIId(bDto.getIBelegPositionIId());
								materialbedarfDto.setSBelegartCNr(bDto.getCBelegartCNr());
								materialbedarfDto.setNEinkaufpreis(bDto.getNEinkaufpreis());
								materialbedarfDto.setLieferantIId(bDto.getLieferantIId());
								materialbedarfDto.setXTextinhalt(bDto.getXTextinhalt());

							} else {

							}
						}
					}
					// Zur Liste
					bedarfe.add(materialbedarfDto);
					// diese Menge wird fuer kuenftige bewegungen als bereits
					// vorhanden betrachtet
					bdVoraussLagerstand = bdVoraussLagerstand.add(materialbedarfDto.getNMenge());
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
					bdVoraussLagerstand = bdVoraussLagerstand.add(bDto.getNMenge());
					// Vorher nachsehen ob ich durch die Toleranz einen anderen
					// Lagerstand habe
					/*
					 * Date dToleranz = new Date(bDto.getTLiefertermin().getTime() + (new
					 * Long(iToleranz)2460601000)); int y=1; BigDecimal bdFiktiverLagerstand =
					 * bdVoraussLagerstand; while(aBewegungsvorschau
					 * .get(i+y).getTLiefertermin().before(dToleranz)){ bdFiktiverLagerstand =
					 * bdFiktiverLagerstand.add(aBewegungsvorschau .get(i+y).getNMenge()); y++; }
					 */
					// wenn der Mindestbestand unterschritten ist, hab ich einen
					// bedarf.
					if (bdVoraussLagerstand.compareTo(bdLagerMindestBestand) < 0) {
						MaterialbedarfDto materialbedarfDto = new MaterialbedarfDto();
						materialbedarfDto.setIArtikelId(artikelDto.getIId());
						materialbedarfDto.setPartnerIIdStandort(partnerIIdStandortStucklistenZiellager);

						materialbedarfDto.setFLagermindest(bdLagerMindestBestand.doubleValue());

						BigDecimal bdMenge = bdLagerSollBestand.subtract(bdVoraussLagerstand);
						// Menge auf den Sollbestand auffuellen.
						if (bInterneBestellung && (artikelDto.getFFertigungssatzgroesse() != null)
								&& (artikelDto.getFFertigungssatzgroesse().doubleValue() > 0)) {
							// SP8191
							materialbedarfDto.setNMenge(Helper.aufVielfachesAufrunden(bdMenge,
									new BigDecimal(artikelDto.getFFertigungssatzgroesse())));
						} else {
							materialbedarfDto.setNMenge(bdMenge);
						}
						// Termin

						// PJ 14861

						java.sql.Date dTermin = new Date(bDto.getTLiefertermin().getTime());

						// wenn die Vorlaufzeit nicht definiert ist, dann gibts
						// es keine
						if (iVorlaufzeitAuftragInTagen == null) {
							iVorlaufzeitAuftragInTagen = new Integer(0);
						}
						if (iVorlaufzeitUnterlose == null) {
							iVorlaufzeitUnterlose = new Integer(0);
						}

						// PJ19998
						Integer iVorlaufzeit = iVorlaufzeitAuftragInTagen;

						if (bDto.getCBelegartCNr() != null && (bDto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
								|| bDto.getCBelegartCNr().equals(LocaleFac.BELEGART_FORECAST))) {
							if (bDto.getKundeDto() != null && bDto.getKundeDto().getILieferdauer() != null) {
								dTermin = Helper.addiereTageZuDatum(new Date(bDto.getTLiefertermin().getTime()),
										-bDto.getKundeDto().getILieferdauer().intValue());
							}

						} else {
							iVorlaufzeit = iVorlaufzeitUnterlose;
						}

						dTermin = Helper.addiereTageZuDatum(dTermin, -iVorlaufzeit.intValue());

						// aber ein Tag vor heute darf nicht rauskommen
						if (dTermin.before(getDate())) {
							dTermin = getDate();
						}
						materialbedarfDto.setBTemporaererEintrag(bDto.getBTemporaererEintrag());
						materialbedarfDto.setSBelegartCNr(bDto.getCBelegartCNr());
						materialbedarfDto.setIBelegpositionIId(bDto.getIBelegPositionIId());
						materialbedarfDto.setIBelegIId(bDto.getIBelegIId());
						materialbedarfDto.setCBelegnummer(bDto.getCBelegnummer());
						materialbedarfDto.setCBelegnummer(bDto.getCBelegnummer());
						materialbedarfDto.setPartnerDto(bDto.getPartnerDto());
						materialbedarfDto.setTTermin(dTermin);
						materialbedarfDto.setProjektIId(bDto.getProjektIId());
						materialbedarfDto.setLieferantIId(bDto.getLieferantIId());
						materialbedarfDto.setXTextinhalt(bDto.getXTextinhalt());
						materialbedarfDto.setNEinkaufpreis(bDto.getNEinkaufpreis());
						materialbedarfDto.setAuftragIIdKopfauftrag(bDto.getAuftragIIdKopfauftrag());
						materialbedarfDto.setXAusloeser(bDto.getAusloeser());
						bedarfe.add(materialbedarfDto);
						// diese Menge wird fuer kuenftige bewegungen als
						// bereits vorhanden betrachtet
						bdVoraussLagerstand = bdVoraussLagerstand.add(materialbedarfDto.getNMenge());
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bedarfe;
	}

	public BigDecimal getAnfangslagerstand(ArtikelDto artikelDto, boolean bInterneBestellung, TheClientDto theClientDto,
			Integer partnerIIdStandort) throws RemoteException {
		BigDecimal bdVoraussLagerstand = new BigDecimal(0);
		LagerDto[] lagerdto = null;

		boolean bGetrennteLaeger = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto);

		// PJ18609
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto) && !bGetrennteLaeger) {
			lagerdto = getLagerFac().lagerFindAll();

		} else {
			lagerdto = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());
		}

		for (int i = 0; i < lagerdto.length; i++) {
			if (bInterneBestellung) {
				// Interne Bestellung
				if (Helper.short2boolean(lagerdto[i].getBInternebestellung())) {
					// Lager wird beruecksichtigt

					bdVoraussLagerstand = bdVoraussLagerstand
							.add(getLagerFac().getLagerstand(artikelDto.getIId(), lagerdto[i].getIId(), theClientDto));

				}
			} else {
				// Bestellvorschlag
				if (Helper.short2boolean(lagerdto[i].getBBestellvorschlag())) {
					// Lager wird beruecksichtigt
					if (partnerIIdStandort == null) {

						bdVoraussLagerstand = bdVoraussLagerstand.add(
								getLagerFac().getLagerstand(artikelDto.getIId(), lagerdto[i].getIId(), theClientDto));
					} else {
						if (partnerIIdStandort.equals(lagerdto[i].getPartnerIIdStandort())) {
							bdVoraussLagerstand = bdVoraussLagerstand.add(getLagerFac()
									.getLagerstand(artikelDto.getIId(), lagerdto[i].getIId(), theClientDto));
						}
					}
				}
			}
		}
		return bdVoraussLagerstand;
	}

	/*
	 * OPTIMIERTE VERSION public BigDecimal getAnfangslagerstand(ArtikelDto
	 * artikelDto, boolean bInterneBestellung, TheClientDto theClientDto, Integer
	 * partnerIIdStandort) throws RemoteException { BigDecimal bdVoraussLagerstand =
	 * new BigDecimal(0);
	 * 
	 * 
	 * boolean bGetrennteLaeger = getMandantFac()
	 * .darfAnwenderAufZusatzfunktionZugreifen(
	 * MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto);
	 * 
	 * Session session = FLRSessionFactory.getFactory().openSession(); String
	 * sQuery=
	 * "SELECT sum (al.n_lagerstand) FROM FLRArtikellager al WHERE al.compId.artikel_i_id="
	 * +artikelDto.getIId();
	 * 
	 * sQuery+=" AND al.flrlager.c_nr NOT IN ('"+LagerFac.LAGER_KEINLAGER+"','"+
	 * LagerFac.LAGER_WERTGUTSCHRIFT+"') ";
	 * 
	 * if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
	 * MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto) &&
	 * !bGetrennteLaeger) {
	 * 
	 * } else { sQuery+=" AND al.flrlager.mandant_c_nr = '"+theClientDto.getMandant
	 * ()+"' "; }
	 * 
	 * 
	 * if (bInterneBestellung) {
	 * sQuery+=" AND al.flrlager.b_internebestellung = 1 "; } else {
	 * sQuery+=" AND al.flrlager.b_bestellvorschlag = 1 "; }
	 * 
	 * 
	 * if(partnerIIdStandort!=null){
	 * sQuery+=" AND al.flrlager.parnter_i_id_standort = "+partnerIIdStandort; }
	 * 
	 * org.hibernate.Query query = session.createQuery(sQuery); List<?> list =
	 * query.list();
	 * 
	 * 
	 * if(list.iterator().hasNext()){ BigDecimal
	 * bdSumme=(BigDecimal)list.iterator().next(); if(bdSumme!=null){
	 * bdVoraussLagerstand=bdSumme; } }
	 * 
	 * 
	 * return bdVoraussLagerstand; }
	 */

	/**
	 * Alle Eintraege in der internen Bestellung fuer einen Mandanten loeschen.
	 * 
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void removeInternebestellungEinesMandanten(boolean bNurHilfsstuecklisten, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		// try {
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRInternebestellung.class);
		c.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
		List<?> list = c.list();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRInternebestellung ib = (FLRInternebestellung) iter.next();
			Internebestellung toRemove = em.find(Internebestellung.class, ib.getI_id());

			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				if (bNurHilfsstuecklisten == true) {
					if (!ib.getFlrstueckliste().getStuecklisteart_c_nr()
							.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
						continue;
					}
				}

				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
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

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(Integer iArtikelId, TheClientDto theClientDto,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean mitAnderenMandanten,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, HashSet hmReservierungenVorhanden)
			throws EJBExceptionLP {
		return getBewegungsvorschauSortiert(iArtikelId, false, false, theClientDto, partnerIIdStandort, bMitRahmen,
				mitAnderenMandanten, bNichtFreigegebeneAuftraegeBeruecksichtigen, hmReservierungenVorhanden);
	}

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(Integer iArtikelId,
			boolean bInternebestellungMiteinbeziehen, boolean bBestellvorschlagMiteinbeziehen,
			TheClientDto theClientDto, Integer partnerIIdStandort, boolean bMitRahmen, boolean mitAnderenMandanten,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, HashSet hmReservierungenVorhanden)
			throws EJBExceptionLP {
		myLogger.logData("artikelIId=" + iArtikelId);

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(iArtikelId, theClientDto);
		return getBewegungsvorschauSortiert(artikelDto, bInternebestellungMiteinbeziehen,
				bBestellvorschlagMiteinbeziehen, null, false, theClientDto, null, partnerIIdStandort, bMitRahmen,
				mitAnderenMandanten, bNichtFreigegebeneAuftraegeBeruecksichtigen, hmReservierungenVorhanden);

	}

	/**
	 * Daten fuer eine Bewegungsvorschau erstellen
	 * 
	 * @param artikelDto                       ArtikelDto
	 * @param bInternebestellungMiteinbeziehen boolean
	 * @param alTheoretischeZusatzeintraege    Optional. Hier koennen der
	 *                                         Bewegungsvorschau "kuenstlich"
	 *                                         weitere Eintraege hinzugefuegt
	 *                                         werden.
	 * @param cNrUserI                         String
	 * @return ArrayList
	 * @throws EJBExceptionLP
	 */
	private ArrayList<BewegungsvorschauDto> getBewegungsvorschau(ArtikelDto artikelDto,
			boolean bInternebestellungMiteinbeziehen, boolean bBestellvorschlagMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alTheoretischeZusatzeintraege, boolean bTermineVorHeuteAufHeute,
			TheClientDto theClientDto, ArrayList<Integer> arLosIId, Integer partnerIIdStandort, boolean bMitRahmen,
			boolean mitAnderenMandanten, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP {

		ArrayList<BewegungsvorschauDto> bewegungsvorschau = new ArrayList<BewegungsvorschauDto>();
		// Zusatzeintraege hinzufuegen
		if (alTheoretischeZusatzeintraege != null) {
			// hier nur Eintraege dieses Artikels beruecksichtigen
			for (Iterator<?> iter = alTheoretischeZusatzeintraege.iterator(); iter.hasNext();) {
				BewegungsvorschauDto item = (BewegungsvorschauDto) iter.next();
				if (artikelDto.getIId().equals(item.getArtikelIId())) {
					bewegungsvorschau.add(item);
				}
			}
		}

		boolean bLagerminJeLager = lagerMinJeLager(theClientDto);

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		Integer tagesartIId_Betriebsurlaub = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, theClientDto).getIId();

		HashMap<Integer, KundeDto> hmKunde = new HashMap<Integer, KundeDto>();
		HashMap<Integer, LieferantDto> hmLieferant = new HashMap<Integer, LieferantDto>();

		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// ------------------------------------------------------------------
			// ----
			// Bestelltliste
			// ------------------------------------------------------------------
			// ----
			Criteria cBestellt = session.createCriteria(FLRArtikelbestellt.class);
			// Filter nach Artikel
			cBestellt.createAlias(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a")
					.add(Restrictions.eq("a.i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cBestellt.addOrder(Order.asc(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN));
			// Query ausfuehren
			List<?> listBestellt = cBestellt.list();
			for (Iterator<?> iter = listBestellt.iterator(); iter.hasNext();) {
				FLRArtikelbestellt item = (FLRArtikelbestellt) iter.next();
				if (item.getC_belegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
					BestellpositionDto bestpos = getBestellpositionFac()
							.bestellpositionFindByPrimaryKeyOhneExc(item.getI_belegartpositionid());
					if (bestpos != null) { // Bestelltliste koennte inkonsistent
						// sein.
						BestellungDto bestellung = getBestellungFac()
								.bestellungFindByPrimaryKey(bestpos.getBestellungIId());

						// wenn die Bestellung "meinem" Mandanten gehoert
						if (!bestellung.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
							// if (bestellung.getTManuellGeliefert() == null) {
							if (bestpos.getBestellpositionstatusCNr() != null
									&& !bestpos.getBestellpositionstatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {

								// Bewegungsvorschaueintrag erzeugen
								BewegungsvorschauDto dto = new BewegungsvorschauDto();
								dto.setArtikelIId(item.getFlrartikel().getI_id());
								dto.setCBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
								dto.setCProjekt(bestellung.getCBez());
								dto.setIBelegIId(bestellung.getIId());
								dto.setIBelegPositionIId(item.getI_belegartpositionid());
								dto.setNMenge(item.getN_menge());
								dto.setTLiefertermin(new Timestamp(item.getT_liefertermin().getTime()));
								dto.setTABTerminBestellung(bestpos.getTAuftragsbestaetigungstermin());
								dto.setCABNummerBestellung(bestpos.getCABNummer());
								dto.setCBelegnummer(bestellung.getCNr());
								// Lieferant holen

								if (!hmLieferant.containsKey(bestellung.getLieferantIIdBestelladresse())) {
									hmLieferant.put(bestellung.getLieferantIIdBestelladresse(),
											getLieferantFac().lieferantFindByPrimaryKey(
													bestellung.getLieferantIIdBestelladresse(), theClientDto));
								}

								LieferantDto lieferantDto = hmLieferant.get(bestellung.getLieferantIIdBestelladresse());

								if (bLagerminJeLager) {
									Integer partnerIIdStandortBestellung = bestellung.getPartnerIIdLieferadresse();

									if (bestellung.getPartnerIIdLieferadresse() == null) {
										// PJ18849 Auch noch mit dem
										// Zubuchungslager des Lieferanten
										// abgleichen
										partnerIIdStandortBestellung = getLagerFac().getPartnerIIdStandortEinesLagers(
												(lieferantDto.getLagerIIdZubuchungslager()));
									} else {
										// SP6667
										KundeDto kdDtoLieferadresse = getKundeFac()
												.kundeFindByiIdPartnercNrMandantOhneExc(
														bestellung.getPartnerIIdLieferadresse(),
														theClientDto.getMandant(), theClientDto);
										if (kdDtoLieferadresse != null) {
											partnerIIdStandortBestellung = getLagerFac()
													.getPartnerIIdStandortEinesLagers(
															(kdDtoLieferadresse.getLagerIIdAbbuchungslager()));
										}

									}
									dto.setPartnerIIdStandort(partnerIIdStandortBestellung);
								}

								if (partnerIIdStandort != null) {

									// Wenn der Standort ungleich der
									// Lieferadresse, dann
									// auslassen
									if (dto.getPartnerIIdStandort() == null
											|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {

										continue;

									}

								}

								dto.setPartnerDto(lieferantDto.getPartnerDto());
								dto.setMandantCNr(bestellung.getMandantCNr());

								BigDecimal preis = bestpos.getNNettogesamtPreisminusRabatte().divide(
										new BigDecimal(bestellung.getFWechselkursmandantwaehrungzubelegwaehrung()), 5,
										BigDecimal.ROUND_HALF_EVEN);

								dto.setBdPreis(preis);

								bewegungsvorschau.add(dto);

							}
							// }
						}
					} else {
						// Eintrag in der Bestelltliste, zu dem es gar keine
						// Bestellposition gibt
						// -> loeschen.
						getArtikelbestelltFac().removeArtikelbestellt(LocaleFac.BELEGART_BESTELLUNG,
								item.getI_belegartpositionid());
					}
				}
			}

			// ------------------------------------------------------------------
			// ----
			// Reservierungsliste
			// ------------------------------------------------------------------
			// ----
			Criteria cReservierung = session.createCriteria(FLRArtikelreservierung.class);
			// Filter nach Artikel
			cReservierung.add(Restrictions.eq("artikel_i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cReservierung.addOrder(Order.asc(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN));
			// Query ausfuehren
			List<?> listReservierung = cReservierung.list();
			for (Iterator<?> iter = listReservierung.iterator(); iter.hasNext();) {
				FLRArtikelreservierung item = (FLRArtikelreservierung) iter.next();
				// --------------------------------------------------------------
				// ------
				// Auftragsreservierung
				// --------------------------------------------------------------
				// ------
				if (item.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragpositionDto aufpos = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKeyOhneExc(item.getI_belegartpositionid());
					if (aufpos != null) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(aufpos.getBelegIId());

						// Bewegungsvorschaueintrag erzeugen
						BewegungsvorschauDto dto = new BewegungsvorschauDto();
						dto.setArtikelIId(item.getFlrartikel().getI_id());
						dto.setAuftragIIdKopfauftrag(auftragDto.getIId());
						dto.setCBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
						dto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
						dto.setProjektIId(auftragDto.getProjektIId());
						dto.setIBelegIId(auftragDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());

						// PJ19570
						if (bNichtFreigegebeneAuftraegeBeruecksichtigen == false
								&& auftragDto.getTAuftragsfreigabe() == null) {

							ParametermandantDto parameter = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
							boolean bAuftragsfreigabe = ((Boolean) parameter.getCWertAsObject());
							if (bAuftragsfreigabe == true) {
								continue;
							}
						}

						dto.setTAuftragsfreigabe(auftragDto.getTAuftragsfreigabe());
						dto.setPersonalIIdAuftragsfreigabe(auftragDto.getPersonalIIdAuftragsfreigabe());

						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);

						// Lieferadresse holen

						if (!hmKunde.containsKey(auftragDto.getKundeIIdLieferadresse())) {
							hmKunde.put(auftragDto.getKundeIIdLieferadresse(), getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto));
						}
						KundeDto kundeDtoLieferadresse = hmKunde.get(auftragDto.getKundeIIdLieferadresse());

						// Abliefertermin
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsAbliefertermin = new Timestamp(tsTermin.getTime());

						tsAbliefertermin = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
								tagesartIId_Feiertag, tagesartIId_Halbtag, tagesartIId_Betriebsurlaub,
								new Date(tsTermin.getTime()), kundeDtoLieferadresse.getILieferdauer());

						if (tsAbliefertermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsAbliefertermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTAbliefertermin(tsAbliefertermin);

						dto.setCBelegnummer(auftragDto.getCNr());
						// Kunde holen

						if (!hmKunde.containsKey(auftragDto.getKundeIIdAuftragsadresse())) {
							hmKunde.put(auftragDto.getKundeIIdAuftragsadresse(), getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto));
						}
						KundeDto kundeDto = hmKunde.get(auftragDto.getKundeIIdAuftragsadresse());

						dto.setPartnerDto(kundeDto.getPartnerDto());
						dto.setKundeDto(kundeDto);

						dto.setXTextinhalt(aufpos.getXTextinhalt());
						dto.setLieferantIId(aufpos.getLieferantIId());
						dto.setNEinkaufpreis(aufpos.getBdEinkaufpreis());

						BigDecimal preis = aufpos.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().divide(
								new BigDecimal(auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung()), 5,
								BigDecimal.ROUND_HALF_EVEN);

						dto.setBdPreis(preis);

						dto.setMandantCNr(auftragDto.getMandantCNr());

						if (bLagerminJeLager) {

							dto.setPartnerIIdStandort(getLagerFac()
									.getPartnerIIdStandortEinesLagers(auftragDto.getLagerIIdAbbuchungslager()));
							if (partnerIIdStandort != null) {

								// Wenn der Stanord ungleich des AbLagers, dann
								// auslassen
								if (dto.getPartnerIIdStandort() == null
										|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {
									continue;
								}

							}
						}

						dto.setAusloeser(LocaleFac.BELEGART_AUFTRAG.trim() + " " + auftragDto.getCNr() + " "
								+ item.getFlrartikel().getC_nr() + " "
								+ Helper.formatZahl(dto.getNMenge().negate(), 2, theClientDto.getLocUi()) + " "
								+ Helper.formatDatum(tsAbliefertermin, theClientDto.getLocUi()));

						bewegungsvorschau.add(dto);

					} else {
						// es wurde eine Auftragsreservierung gefunden, zu der
						// es keine Auftragsposition gibt.
						// -> loeschen.
						getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_AUFTRAG,
								item.getI_belegartpositionid());
					}
				}
				// Losreservierung
				else if (item.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					LossollmaterialDto losmat = getFertigungFac()
							.lossollmaterialFindByPrimaryKeyOhneExc(item.getI_belegartpositionid());
					if (losmat != null) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(losmat.getLosIId());

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

						if (bLagerminJeLager) {

							LoslagerentnahmeDto[] lolaDtos = getFertigungFac()
									.loslagerentnahmeFindByLosIId(losDto.getIId());

							if (lolaDtos.length > 0) {
								Integer parnterIIdStandortLos = getLagerFac()
										.getPartnerIIdStandortEinesLagers(lolaDtos[0].getLagerIId());

								dto.setPartnerIIdStandort(parnterIIdStandortLos);

							}

						}
						if (partnerIIdStandort != null) {
							if (dto.getPartnerIIdStandort() == null
									|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {
								continue;
							}

						}

						dto.setIBelegIId(losDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);
						dto.setCBelegnummer(losDto.getCNr());
						// wenn das Los auftragsbezogen ist, dann hol ich
						// auch den Kunden
						if (losDto.getAuftragpositionIId() != null || losDto.getAuftragIId() != null) {

							Integer auftragIId = losDto.getAuftragIId();
							if (auftragIId == null) {
								AuftragpositionDto aufposDto = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
								auftragIId = aufposDto.getBelegIId();
							}

							dto.setAuftragIIdKopfauftrag(auftragIId);

							// den finde ich ueber die Auftragsposition

							AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

							dto.setProjektIId(auftragDto.getProjektIId());

							if (!hmKunde.containsKey(auftragDto.getKundeIIdAuftragsadresse())) {
								hmKunde.put(auftragDto.getKundeIIdAuftragsadresse(), getKundeFac()
										.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto));
							}
							KundeDto kundeDto = hmKunde.get(auftragDto.getKundeIIdAuftragsadresse());
							dto.setPartnerDto(kundeDto.getPartnerDto());
						}

						// PJ19688
						Integer kundeIIdZugehoerig = getFertigungFac().getZugehoerigenKunden(losDto.getIId(),
								theClientDto);
						if (kundeIIdZugehoerig != null) {
							if (!hmKunde.containsKey(kundeIIdZugehoerig)) {
								hmKunde.put(kundeIIdZugehoerig,
										getKundeFac().kundeFindByPrimaryKey(kundeIIdZugehoerig, theClientDto));
							}
							KundeDto kundeDto = hmKunde.get(kundeIIdZugehoerig);
							dto.setKundeDto(kundeDto);
						}

						// PJ20905
						String stkl = "";
						if (losDto.getStuecklisteIId() != null) {
							StuecklisteDto stklDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
							stkl = stklDto.getArtikelDto().getCNr();
						}

						dto.setAusloeser(LocaleFac.BELEGART_LOS.trim() + " " + losDto.getCNr() + " " + stkl + " "
								+ Helper.formatZahl(dto.getNMenge().negate(), 2, theClientDto.getLocUi()) + " "
								+ Helper.formatDatum(dto.getTLiefertermin(), theClientDto.getLocUi()));

						bewegungsvorschau.add(dto);

					} else {
						// es wurde eine Auftragsreservierung gefunden, zu der
						// es keine Auftragsposition gibt.
						// -> loeschen.
						getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_LOS,
								item.getI_belegartpositionid());
					}
				} else if (item.getC_belegartnr().equals(LocaleFac.BELEGART_KUECHE)) {
					Speiseplanposition speiseplanposition = em.find(Speiseplanposition.class,
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
									.speiseplanFindByPrimaryKey(speiseplanpositionDto.getSpeiseplanIId());
							dto.setCBelegnummer("K");

							// Projekt= Speisekassa

							KassaartikelDto speisekassaDto = getKuecheFac()
									.kassaartikelFindByPrimaryKey(speiseplanDto.getKassaartikelIId());

							dto.setCProjekt(speisekassaDto.getCBez());

							// Kunde = Stuecklistebezeichnung
							/*
							 * StuecklisteDto stuecklisteDto = getStuecklisteFac()
							 * .stuecklisteFindByPrimaryKey( speiseplanDto.getStuecklisteIId(),
							 * theClientDto);
							 */

						} catch (RemoteException ex3) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
						}

						dto.setIBelegIId(speiseplanDto.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());
						dto.setMandantCNr(speiseplanDto.getMandantCNr());
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);

						bewegungsvorschau.add(dto);

					} else {
						getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_KUECHE,
								item.getI_belegartpositionid());
					}

				} else if (item.getC_belegartnr().equals(LocaleFac.BELEGART_FORECAST)) {
					Forecastposition forecastposition = em.find(Forecastposition.class, item.getI_belegartpositionid());
					if (forecastposition != null) {

						Forecastauftrag forecastauftrag = em.find(Forecastauftrag.class,
								forecastposition.getForecastauftragIId());

						Fclieferadresse fclieferadresse = em.find(Fclieferadresse.class,
								forecastauftrag.getFclieferadresseIId());

						Forecast forecast = em.find(Forecast.class, fclieferadresse.getForecastIId());

						// Bewegungsvorschaueintrag erzeugen
						BewegungsvorschauDto dto = new BewegungsvorschauDto();
						dto.setArtikelIId(item.getFlrartikel().getI_id());
						dto.setCBelegartCNr(LocaleFac.BELEGART_FORECAST);

						dto.setCBelegnummer(forecast.getCNr());

						dto.setForecastartCNr(
								getForecastFac().getForecastartEienrForecastposition(forecastposition.getIId()));
						dto.setForecastpositionIId(forecastposition.getIId());
						dto.setForecastBemerkung(forecastauftrag.getCBemerkung());

						if (bNichtFreigegebeneAuftraegeBeruecksichtigen == false
								&& (dto.getForecastartCNr().equals(ForecastFac.FORECASTART_NICHT_DEFINIERT)
										|| dto.getForecastartCNr().equals(ForecastFac.FORECASTART_FORECASTAUFTRAG))) {
							// Wenn nicht COW oder COD, dann auslassen
							continue;
						}

						dto.setCProjekt(forecast.getCProjekt());

						// Kunde

						if (!hmKunde.containsKey(forecast.getKundeIId())) {
							hmKunde.put(forecast.getKundeIId(),
									getKundeFac().kundeFindByPrimaryKey(forecast.getKundeIId(), theClientDto));
						}
						KundeDto kundeDto = hmKunde.get(forecast.getKundeIId());

						dto.setPartnerDto(kundeDto.getPartnerDto());
						dto.setMandantCNr(kundeDto.getMandantCNr());

						dto.setIBelegIId(forecast.getIId());
						dto.setIBelegPositionIId(item.getI_belegartpositionid());
						dto.setNMenge(item.getN_menge().negate());

						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsTermin = new Timestamp(item.getT_liefertermin().getTime());
						if (tsTermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsTermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTLiefertermin(tsTermin);

						// Lieferadresse holen

						if (!hmKunde.containsKey(fclieferadresse.getKundeIIdLieferadresse())) {
							hmKunde.put(fclieferadresse.getKundeIIdLieferadresse(), getKundeFac()
									.kundeFindByPrimaryKey(fclieferadresse.getKundeIIdLieferadresse(), theClientDto));
						}

						KundeDto kundeDtoLieferadresse = hmKunde.get(fclieferadresse.getKundeIIdLieferadresse());

						dto.setKundeDto(kundeDtoLieferadresse);

						// Abliefertermin
						// Termine vor heute werden auf heute umgerechnet
						Timestamp tsAbliefertermin = new Timestamp(tsTermin.getTime());
						tsAbliefertermin = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
								tagesartIId_Feiertag, tagesartIId_Halbtag, tagesartIId_Betriebsurlaub,
								new Date(tsTermin.getTime()), kundeDtoLieferadresse.getILieferdauer());
						if (tsAbliefertermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
							tsAbliefertermin = Helper.cutTimestamp(getTimestamp());
						}
						dto.setTAbliefertermin(tsAbliefertermin);

						dto.setAusloeser(LocaleFac.BELEGART_FORECAST.trim() + " " + forecast.getCNr() + " "
								+ Helper.formatZahl(dto.getNMenge().negate(), 2, theClientDto.getLocUi()) + " "
								+ Helper.formatDatum(dto.getTAbliefertermin(), theClientDto.getLocUi()));

						bewegungsvorschau.add(dto);

					} else {
						getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_FORECAST,
								item.getI_belegartpositionid());
					}

				}
			}

			if (bMitRahmen) {
				// PJ18329 Rahmenreservierungen
				ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();
				kritDtoI.setArtikelIId(artikelDto.getIId()); // keine
																// Datumseinschraenkung.
				ReportRahmenreservierungDto[] aResult = getArtikelReportFac().getReportRahmenreservierung(kritDtoI,
						theClientDto);
				for (int i = 0; i < aResult.length; i++) {
					// negative Rahmenreservierungen bleiben unberuecksichtigt.
					if (aResult[i].getNOffeneMenge() != null && aResult[i].getNOffeneMenge().doubleValue() > 0) {

						// Bewegungsvorschaueintrag erzeugen
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKeyOhneExc(aResult[i].getAuftragIId());

						if (auftragDto != null) {
							// Bewegungsvorschaueintrag erzeugen
							BewegungsvorschauDto dto = new BewegungsvorschauDto();
							dto.setArtikelIId(aResult[i].getArtikelIId());
							dto.setCBelegartCNr(AuftragServiceFac.AUFTRAGART_RAHMEN.trim());
							dto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
							dto.setProjektIId(auftragDto.getProjektIId());
							dto.setIBelegIId(auftragDto.getIId());

							dto.setNMenge(aResult[i].getNOffeneMenge().negate());

							// Termine vor heute werden auf heute umgerechnet
							Timestamp tsTermin = aResult[i].getTUebersteuerterLiefertermin();
							if (tsTermin.before(getTimestamp()) && bTermineVorHeuteAufHeute) {
								tsTermin = Helper.cutTimestamp(getTimestamp());
							}
							dto.setTLiefertermin(tsTermin);
							dto.setCBelegnummer(auftragDto.getCNr());
							// Kunde holen
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
							dto.setPartnerDto(kundeDto.getPartnerDto());
							dto.setKundeDto(kundeDto);

							dto.setMandantCNr(auftragDto.getMandantCNr());

							if (bLagerminJeLager) {

								dto.setPartnerIIdStandort(getLagerFac()
										.getPartnerIIdStandortEinesLagers(auftragDto.getLagerIIdAbbuchungslager()));
								if (partnerIIdStandort != null) {

									// Wenn der Stanord ungleich des AbLagers,
									// dann
									// auslassen
									if (dto.getPartnerIIdStandort() == null
											|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {
										continue;
									}

								}
							}
							dto.setAusloeser(AuftragServiceFac.AUFTRAGART_RAHMEN.trim() + " " + auftragDto.getCNr()
									+ " " + Helper.formatZahl(dto.getNMenge().negate(), 2, theClientDto.getLocUi())
									+ " " + Helper.formatDatum(dto.getTLiefertermin(), theClientDto.getLocUi()));
							bewegungsvorschau.add(dto);
						}

					}
				}

				// PJ18329 Rahmenbestellungen
				Session sessionRahmenBest = factory.openSession();
				Criteria crit = sessionRahmenBest.createCriteria(FLRBestellpositionReport.class);
				Criteria critBestellung = crit.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);

				Criteria critArtikel = crit.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
				// nur diesen Artikel
				critArtikel.add(Restrictions.eq("i_id", artikelDto.getIId()));
				// Filter nach Mandant
				critBestellung
						.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
				// keine stornierten oder erledigten Bestellungen.
				Collection<String> cStati = new LinkedList<String>();
				cStati.add(BestellungFac.BESTELLSTATUS_STORNIERT);
				cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
				critBestellung.add(
						Restrictions.not(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati)));
				// Nur Rahmenbestellungen
				critBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
						BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
				// Query ausfuehren
				List<?> list = crit.list();
				FLRBestellpositionReport[] aResultRB = new FLRBestellpositionReport[list.size()];
				aResultRB = (FLRBestellpositionReport[]) list.toArray(aResultRB);
				for (int i = 0; i < aResultRB.length; i++) {

					// negative Rahmenbestellungen bleiben unberuecksichtigt.
					if (aResultRB[i].getN_offenemenge() != null && aResultRB[i].getN_offenemenge().doubleValue() > 0) {

						BestellpositionDto bestpos = getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(aResultRB[i].getI_id());
						if (bestpos != null) {
							BestellungDto bestellung = getBestellungFac()
									.bestellungFindByPrimaryKey(aResultRB[i].getFlrbestellung().getI_id());

							BewegungsvorschauDto dto = new BewegungsvorschauDto();
							dto.setArtikelIId(aResultRB[i].getFlrartikel().getI_id());
							dto.setCBelegartCNr(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR.trim());
							dto.setCProjekt(bestellung.getCBez());
							dto.setIBelegIId(bestellung.getIId());
							dto.setIBelegPositionIId(bestpos.getIId());
							dto.setNMenge(aResultRB[i].getN_offenemenge());
							dto.setTLiefertermin(bestpos.getTUebersteuerterLiefertermin());
							dto.setTABTerminBestellung(bestpos.getTAuftragsbestaetigungstermin());
							dto.setCABNummerBestellung(bestpos.getCABNummer());
							dto.setCBelegnummer(bestellung.getCNr());
							// Lieferant holen
							LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
									bestellung.getLieferantIIdBestelladresse(), theClientDto);

							if (bLagerminJeLager) {
								Integer partnerIIdStandortBestellung = bestellung.getPartnerIIdLieferadresse();

								if (bestellung.getPartnerIIdLieferadresse() == null) {
									// PJ18849 Auch noch mit dem
									// Zubuchungslager des Lieferanten
									// abgleichen
									partnerIIdStandortBestellung = getLagerFac().getPartnerIIdStandortEinesLagers(
											(lieferantDto.getLagerIIdZubuchungslager()));
								}
								dto.setPartnerIIdStandort(partnerIIdStandortBestellung);
							}

							if (partnerIIdStandort != null) {

								// Wenn der Standort ungleich der
								// Lieferadresse, dann
								// auslassen
								if (dto.getPartnerIIdStandort() == null
										|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {

									continue;

								}

							}

							dto.setPartnerDto(lieferantDto.getPartnerDto());
							dto.setMandantCNr(bestellung.getMandantCNr());

							BigDecimal preis = bestpos.getNNettogesamtPreisminusRabatte().divide(
									new BigDecimal(bestellung.getFWechselkursmandantwaehrungzubelegwaehrung()), 5,
									BigDecimal.ROUND_HALF_EVEN);

							dto.setBdPreis(preis);

							bewegungsvorschau.add(dto);
						}
					}
				}

				sessionRahmenBest.close();

			}
			// ------------------------------------------------------------------
			// ----
			// Fehlmengen
			// ------------------------------------------------------------------
			// ----
			Criteria cFehlmenge = session.createCriteria(FLRFehlmenge.class);
			// Filter nach Artikel
			cFehlmenge.add(Restrictions.eq(ArtikelFac.FLR_FEHLMENGE_ARTIKEL_I_ID, artikelDto.getIId()));
			// Sortierung nach Liefertermin
			cFehlmenge.addOrder(Order.asc(ArtikelFac.FLR_FEHLMENGE_T_LIEFERTERMIN));
			List<?> listFehlmenge = cFehlmenge.list();
			for (Iterator<?> iter = listFehlmenge.iterator(); iter.hasNext();) {
				FLRFehlmenge item = (FLRFehlmenge) iter.next();
				// kontrollieren, ob das los auch von diesem mandanten ist
				if (item.getFlrlossollmaterial() != null) {
					// Los holen
					LosDto losDto = getFertigungFac()
							.losFindByPrimaryKey(item.getFlrlossollmaterial().getFlrlos().getI_id());

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
					dto.setIBelegPositionIId(item.getFlrlossollmaterial().getI_id());
					dto.setCProjekt(losDto.getCProjekt());

					if (bLagerminJeLager) {

						LoslagerentnahmeDto[] lolaDtos = getFertigungFac()
								.loslagerentnahmeFindByLosIId(losDto.getIId());

						if (lolaDtos.length > 0) {
							Integer parnterIIdStandortLos = getLagerFac()
									.getPartnerIIdStandortEinesLagers(lolaDtos[0].getLagerIId());

							dto.setPartnerIIdStandort(parnterIIdStandortLos);

						}

					}
					if (partnerIIdStandort != null) {
						if (dto.getPartnerIIdStandort() == null
								|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {
							continue;
						}

					}

					dto.setNMenge(item.getN_menge().negate());
					// Termine vor heute werden auf heute umgerechnet
					Timestamp tsTermin = new Timestamp(item.getT_liefertermin().getTime());
					if (tsTermin.before(getTimestamp())) {
						tsTermin = Helper.cutTimestamp(getTimestamp());
					}
					dto.setTLiefertermin(tsTermin);
					dto.setCBelegnummer(item.getFlrlossollmaterial().getFlrlos().getC_nr());
					// wenn das Los auftragsbezogen ist, dann hol ich auch den
					// Kunden
					if (losDto.getAuftragpositionIId() != null) {
						// den finde ich ueber die Auftragsposition
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());

						dto.setProjektIId(auftragDto.getProjektIId());

						if (!hmKunde.containsKey(auftragDto.getKundeIIdAuftragsadresse())) {
							hmKunde.put(auftragDto.getKundeIIdAuftragsadresse(), getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto));
						}
						KundeDto kundeDto = hmKunde.get(auftragDto.getKundeIIdAuftragsadresse());

						dto.setPartnerDto(kundeDto.getPartnerDto());

						dto.setAuftragIIdKopfauftrag(auftragDto.getIId());

					}

					if (dto.getAuftragIIdKopfauftrag() == null && losDto.getAuftragIId() != null) {
						dto.setAuftragIIdKopfauftrag(losDto.getAuftragIId());
					}

					// PJ19688
					Integer kundeIIdZugehoerig = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);

					if (kundeIIdZugehoerig != null) {
						if (!hmKunde.containsKey(kundeIIdZugehoerig)) {
							hmKunde.put(kundeIIdZugehoerig,
									getKundeFac().kundeFindByPrimaryKey(kundeIIdZugehoerig, theClientDto));
						}
						KundeDto kundeDto = hmKunde.get(kundeIIdZugehoerig);
						dto.setKundeDto(kundeDto);
					}

					// PJ20905
					String stkl = "";
					if (item.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {
						stkl = item.getFlrlossollmaterial().getFlrlos().getFlrstueckliste().getFlrartikel().getC_nr();
					}

					dto.setAusloeser(LocaleFac.BELEGART_LOS.trim() + " " + losDto.getCNr() + " " + stkl + " "
							+ Helper.formatZahl(dto.getNMenge().negate(), 2, theClientDto.getLocUi()));

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
			// OHNE STORNIERTE
			cAblieferung.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
					new String[] { FertigungFac.STATUS_STORNIERT, FertigungFac.STATUS_ERLEDIGT })));
			// Filter nach Artikel
			cAblieferung.createCriteria(FertigungFac.FLR_LOS_FLRSTUECKLISTE)
					.createCriteria(StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL)
					.add(Restrictions.eq("i_id", artikelDto.getIId()));
			// Sortierung nach Liefertermin (=Produktionsende)
			cAblieferung.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
			// Query ausfuehren
			List<?> listAblieferung = cAblieferung.list();
			for (Iterator<?> iter = listAblieferung.iterator(); iter.hasNext();) {
				FLRLos los = (FLRLos) iter.next();
				BigDecimal bdOffen = los.getN_losgroesse();
				// von der Losgroesse die bisherigen Ablieferungen subtrahieren
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
					FLRLosablieferung item2 = (FLRLosablieferung) iter2.next();
					bdOffen = bdOffen.subtract(item2.getN_menge());
				}
				if (bdOffen.compareTo(new BigDecimal(0)) > 0) {

					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(los.getFlrstueckliste().getFlrartikel().getI_id());
					dto.setCBelegartCNr(LocaleFac.BELEGART_LOSABLIEFERUNG);
					dto.setIBelegIId(los.getI_id());
					dto.setIBelegPositionIId(null);
					dto.setNMenge(bdOffen);
					dto.setMandantCNr(los.getMandant_c_nr());
					dto.setTLiefertermin(new Timestamp(los.getT_produktionsende().getTime()));
					dto.setCBelegnummer(los.getC_nr());
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getI_id());
					// wenn das Los auftragsbezogen ist, dann hol ich auch den
					// Kunden

					if (bLagerminJeLager) {

						dto.setPartnerIIdStandort(
								getLagerFac().getPartnerIIdStandortEinesLagers(los.getLager_i_id_ziel()));
					}

					if (partnerIIdStandort != null) {
						if (dto.getPartnerIIdStandort() == null
								|| !dto.getPartnerIIdStandort().equals(partnerIIdStandort)) {
							continue;
						}
					}

					if (losDto.getAuftragpositionIId() != null) {
						// den finde ich ueber die Auftragsposition
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto);
						dto.setPartnerDto(kundeDto.getPartnerDto());
					}

					// PJ19688
					Integer kundeIIdZugehoerig = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);
					if (kundeIIdZugehoerig != null) {
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIIdZugehoerig, theClientDto);
						dto.setKundeDto(kundeDto);
					}

					bewegungsvorschau.add(dto);
				}
			}
			// Interne Bestellung miteinbeziehen
			if (bInternebestellungMiteinbeziehen) {
				Criteria cIB = session.createCriteria(FLRInternebestellung.class);
				// Filter nach Mandant
				cIB.
				// Filter nach Artikel
						createCriteria(FertigungFac.FLR_INTERNE_BESTELLUNG_FLRSTUECKLISTE)
						.createCriteria(StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL)
						.add(Restrictions.eq("i_id", artikelDto.getIId()));
				// Sortierung nach Liefertermin (=Produktionsende)
				cIB.addOrder(Order.asc(FertigungFac.FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN));
				List<?> listIB = cIB.list();
				for (Iterator<?> iter = listIB.iterator(); iter.hasNext();) {
					FLRInternebestellung ib = (FLRInternebestellung) iter.next();

					if (partnerIIdStandort != null) {
						if (ib.getFlrpartner_standort() == null
								|| !partnerIIdStandort.equals(ib.getFlrpartner_standort().getI_id())) {
							continue;
						}
					}

					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(ib.getFlrstueckliste().getFlrartikel().getI_id());
					dto.setCBelegartCNr(ib.getBelegart_c_nr());
					dto.setAuftragIIdKopfauftrag(ib.getAuftrag_i_id_kopfauftrag());
					dto.setIBelegIId(ib.getI_belegiid());
					dto.setIBelegPositionIId(ib.getI_belegpositioniid());
					dto.setNMenge(ib.getN_menge());
					dto.setTLiefertermin(new Timestamp(ib.getT_liefertermin().getTime()));
					dto.setMandantCNr(ib.getMandant_c_nr());
					dto.setbKommtAusInternerBstellung(true);

					if (ib.getFlrpartner_standort() != null) {
						dto.setPartnerIIdStandort(ib.getFlrpartner_standort().getI_id());
					}

					bewegungsvorschau.add(dto);
				}
			}

			// SP4960
			// Bestellvorschlag miteinbeziehen
			if (bBestellvorschlagMiteinbeziehen) {
				Criteria cIB = session.createCriteria(FLRBestellvorschlag.class);
				// Filter nach Mandant
				cIB.add(Restrictions.eq("artikel_i_id", artikelDto.getIId()));
				// Sortierung nach Liefertermin (=Produktionsende)
				cIB.addOrder(Order.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN));
				List<?> listIB = cIB.list();
				for (Iterator<?> iter = listIB.iterator(); iter.hasNext();) {
					FLRBestellvorschlag bv = (FLRBestellvorschlag) iter.next();

					if (partnerIIdStandort != null) {
						if (bv.getFlrpartner_standort() == null
								|| !partnerIIdStandort.equals(bv.getFlrpartner_standort().getI_id())) {
							continue;
						}
					}

					BewegungsvorschauDto dto = new BewegungsvorschauDto();
					dto.setArtikelIId(bv.getArtikel_i_id());
					dto.setCBelegartCNr(bv.getBelegart_c_nr());
					dto.setIBelegIId(bv.getI_belegartid());
					dto.setIBelegPositionIId(bv.getI_belegartpositionid());
					dto.setNMenge(bv.getN_zubestellendemenge());
					dto.setTLiefertermin(new Timestamp(bv.getT_liefertermin().getTime()));
					dto.setMandantCNr(bv.getMandant_c_nr());
					dto.setBKommtAusBestellvorschlag(true);

					if (bv.getFlrpartner_standort() != null) {
						dto.setPartnerIIdStandort(bv.getFlrpartner_standort().getI_id());
					}

					bewegungsvorschau.add(dto);
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}

		if (mitAnderenMandanten == false) {

			ArrayList<BewegungsvorschauDto> bewegungsvorschauTemp = new ArrayList<BewegungsvorschauDto>();

			Iterator itBV = bewegungsvorschau.iterator();
			while (itBV.hasNext()) {
				BewegungsvorschauDto bvDto = (BewegungsvorschauDto) itBV.next();
				if (bvDto.getMandantCNr() == null) {
					bewegungsvorschauTemp.add(bvDto);
				} else {
					if (bvDto.getMandantCNr().equals(theClientDto.getMandant())) {
						bewegungsvorschauTemp.add(bvDto);
					}
				}
			}

			bewegungsvorschau = bewegungsvorschauTemp;

		}

		return bewegungsvorschau;
	}

	public ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(ArtikelDto artikelDto,
			boolean bTermineVorHeuteAufHeute, TheClientDto theClientDto, Integer partnerIIdStandort, boolean bMitRahmen,
			boolean mitAnderenMandanten, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP {
		ArrayList<BewegungsvorschauDto> a = getBewegungsvorschau(artikelDto, false, false, null,
				bTermineVorHeuteAufHeute, theClientDto, null, partnerIIdStandort, bMitRahmen, mitAnderenMandanten,
				bNichtFreigegebeneAuftraegeBeruecksichtigen, hmReservierungenVorhanden);
		Collections.sort(a, new ComparatorBewegungsvorschau());
		return a;
	}

	private ArrayList<BewegungsvorschauDto> getBewegungsvorschauSortiert(ArtikelDto artikelDto,
			boolean bInternebestellungMiteinbeziehen, boolean bBestellvorschlagMiteinbeziehen,
			ArrayList<BewegungsvorschauDto> alZusatzeintraegeBewegungsvorschau, boolean bTermineVorHeuteAufHeute,
			TheClientDto theClientDto, ArrayList<Integer> arLosIId, Integer partnerIIdStandort, boolean bMitRahmen,
			boolean mitAnderenMandanten, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			HashSet hmReservierungenVorhanden) throws EJBExceptionLP {
		ArrayList<BewegungsvorschauDto> a = getBewegungsvorschau(artikelDto, bInternebestellungMiteinbeziehen,
				bBestellvorschlagMiteinbeziehen, alZusatzeintraegeBewegungsvorschau, bTermineVorHeuteAufHeute,
				theClientDto, arLosIId, partnerIIdStandort, bMitRahmen, mitAnderenMandanten,
				bNichtFreigegebeneAuftraegeBeruecksichtigen, hmReservierungenVorhanden);
		Collections.sort(a, new ComparatorBewegungsvorschau());
		return a;
	}

	public void beginnEndeAllerEintraegeVerschieben(Timestamp tBeginn, Timestamp tEnde, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("InternebestellungUpdateTermine");
		query.setParameter(1, tBeginn);
		query.setParameter(2, tEnde);
		query.setParameter(3, theClientDto.getMandant());
		query.executeUpdate();

	}

	/**
	 * Interne Bestellung in Lose ueberleiten
	 * 
	 * @param interneBestellungIId Integer
	 * @param theClientDto         String
	 * @throws EJBExceptionLP
	 */
	public Integer interneBestellungUeberleiten(Integer interneBestellungIId, Integer partnerIIdStandort,
			int iTypAuftragsbezug, TheClientDto theClientDto) throws EJBExceptionLP {

		ArrayList<Integer> interneBestellungIIds_NichtAngelegt = new ArrayList<Integer>();
		try {

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			// Allgemeine standarddurchlaufzeit
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			int iStandarddurchlaufzeit = ((Integer) parameter.getCWertAsObject()).intValue();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_INTERNE_BESTELLUNG_TERMINE_BELASSEN);
			boolean bTermineBelassen = ((Boolean) parameter.getCWertAsObject());

			// nun den Eintrag ueberleiten
			InternebestellungDto ib = internebestellungFindByPrimaryKey(interneBestellungIId);

			if (partnerIIdStandort == null || partnerIIdStandort.equals(ib.getPartnerIIdStandort())) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(ib.getStuecklisteIId(),
						theClientDto);
				// Los anlegen
				LosDto losDto = new LosDto();

				// SP5973
				if ((iTypAuftragsbezug == UEBERLEITEN_MIT_AUFTRAGSBEZUG
						|| iTypAuftragsbezug == UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE)
						&& ib.getAuftragIIdKopfauftrag() != null) {

					// Daten aus den Auftragskopfdaten soweit moeglich
					// uebernehmen
					if (ib.getAuftragIIdKopfauftrag() != null) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(ib.getAuftragIIdKopfauftrag());

						// Projekt
						losDto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
						// Kostenstelle
						losDto.setKostenstelleIId(auftragDto.getKostIId());

						if (iTypAuftragsbezug == UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE) {
							losDto.setKundeIId(auftragDto.getKundeIIdAuftragsadresse());
						} else {
							losDto.setAuftragIId(ib.getAuftragIIdKopfauftrag());
						}

					}
				}

				// Optional: Auftragszuordnung
				if (ib.getBelegartCNr() != null && ib.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {

					// PJ20358
					if (iTypAuftragsbezug == UEBERLEITEN_MIT_AUFTRAGSBEZUG
							|| iTypAuftragsbezug == UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE) {

						// Zusatznummer ist wie ueblich das I_SORT der
						// Auftragsposition
						parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
						int bLosnummerAuftragsbezogen = ((Integer) parameter.getCWertAsObject());
						if (bLosnummerAuftragsbezogen >= 1) {

							parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_FERTIGUNG,
									ParameterFac.PARAMETER_INTERNE_BESTELLUNG_MIT_AUFTRAGSPOSITIONSBEZUG);
							boolean bInternebestellungMitAuftragspositionsbezug = ((Boolean) parameter
									.getCWertAsObject());

							if (bInternebestellungMitAuftragspositionsbezug == true) {
								if (ib.getIBelegpositionIId() != null) {
									AuftragpositionDto apDto = getAuftragpositionFac()
											.auftragpositionFindByPrimaryKeyOhneExc(ib.getIBelegpositionIId());
									if (apDto != null) {

										if (iTypAuftragsbezug == UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE) {
											AuftragDto auftragDto = getAuftragFac()
													.auftragFindByPrimaryKey(apDto.getBelegIId());
											losDto.setKundeIId(auftragDto.getKundeIIdAuftragsadresse());
											losDto.setKostenstelleIId(auftragDto.getKostenstelleIId());
										} else {
											losDto.setAuftragpositionIId(ib.getIBelegpositionIId());
										}
										losDto.setCZusatznummer("" + apDto.getISort());
									}
								}
							}
						}
					}
				} else if (ib.getBelegartCNr() != null && ib.getBelegartCNr().equals(LocaleFac.BELEGART_FORECAST)) {

					ForecastpositionDto fpDto = getForecastFac()
							.forecastpositonFindByPrimaryKeyOhneExc(ib.getIBelegpositionIId());

					if (fpDto != null) {

						ForecastauftragDto faDto = getForecastFac()
								.forecastauftragFindByPrimaryKey(fpDto.getForecastauftragIId());
						FclieferadresseDto flDto = getForecastFac()
								.fclieferadresseFindByPrimaryKey(faDto.getFclieferadresseIId());
						ForecastDto fDto = getForecastFac().forecastFindByPrimaryKey(flDto.getForecastIId());

						losDto.setKundeIId(fDto.getKundeIId());

					}

				}

				// PJ18047
				if (losDto.getAuftragpositionIId() != null) {

					// Wenns die Auftragsposition nicht mehr gibt, dann
					// auslassen
					Auftragposition ap = em.find(Auftragposition.class, losDto.getAuftragpositionIId());
					if (ap == null) {
						return null;
					}

					ParametermandantDto parameterIndirekterPosBezug = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_INDIREKTER_AUFTRAGSPOSITIONSBEZUG);
					boolean bIndirekterABPosBezug = (Boolean) parameterIndirekterPosBezug.getCWertAsObject();

					if (bIndirekterABPosBezug == false) {

						Query query = em.createNamedQuery("LosfindByAuftragpositionIId");
						query.setParameter(1, losDto.getAuftragpositionIId());
						if (query.getResultList().size() > 0) {
							Los los = (Los) query.getResultList().iterator().next();
							return los.getIId();
						}
					} else {

						Integer posnr = getAuftragpositionFac().getPositionNummer(losDto.getAuftragpositionIId());
						String sNummer = new java.text.DecimalFormat("000").format(posnr);
						losDto.setCAbposnr(sNummer);
					}

				}

				losDto.setCKommentar(
						"Interne Bestellung vom " + Helper.formatDatum(getDate(), theClientDto.getLocUi()));
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
				losDto.setFertigungsgruppeIId(stuecklisteDto.getFertigungsgruppeIId());

				if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
					iStandarddurchlaufzeit = stuecklisteDto.getNDefaultdurchlaufzeit().intValue();
				}

				// PJ20684//SP6483
				if (bTermineBelassen) {

					losDto.setTProduktionsbeginn(ib.getTProduktionsbeginn());
					losDto.setTProduktionsende(ib.getTLiefertermin());
				} else {
					Timestamp tAusliefertermin = new Timestamp(ib.getTLiefertermin().getTime());

					Timestamp tEnde = tAusliefertermin;

					Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde, -iStandarddurchlaufzeit);

					if (tBeginn.before(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())))) {
						tBeginn = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
						tEnde = Helper.addiereTageZuTimestamp(tBeginn, iStandarddurchlaufzeit);
					}

					losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
					losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));
				}

				// Los anlegen

				try {
					losDto = getFertigungFac().createLos(losDto, theClientDto);

					// PJ18845
					if (ib.getPartnerIIdStandort() != null && ib.getStuecklisteIId() != null) {
						getFertigungFac().losLaegerAnhandStandortErstellen(losDto.getIId(), ib.getStuecklisteIId(),
								ib.getPartnerIIdStandort(), theClientDto);

					}

					// Abschliessend wird der IB-Eintrag geloescht.
					removeInternebestellung(ib, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() != EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
						throw new EJBExceptionLP(e.getCode(), e.getAlInfoForTheClient(), new Exception(e.getMessage()));
					} else {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INTERNEBESTELLUNG_LOS_BEREITS_VORHANDEN,
								e.getAlInfoForTheClient(),
								new Exception("FEHLER_INTERNEBESTELLUNG_LOS_BEREITS_VORHANDEN"));
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return null;
	}

	/**
	 * Die I_ID'saller IB-Eintraege eines Mandanten ermitteln.
	 * 
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return Set
	 */
	public Set<Integer> getInternebestellungIIdsEinesMandanten(TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		Set<Integer> set = new HashSet<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRInternebestellung.class);
			c.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
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

	public void verdichteInterneBestellung(HashSet<Integer> stuecklisteIIds, TheClientDto theClientDto) {
		Iterator it = stuecklisteIIds.iterator();
		while (it.hasNext()) {
			Integer stuecklisteIId = (Integer) it.next();
			verdichteInterneBestellungEinerStuecklisteEinesMandanten(stuecklisteIId, 9999, theClientDto);
		}
	}

	public void verdichteInterneBestellungEinerStuecklisteEinesMandanten(Integer stuecklisteIId,
			Integer iVerdichtungsTage, TheClientDto theClientDto) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			if (iVerdichtungsTage == null) {
				// Wenn nicht angegeben, zieht der Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM);
				iVerdichtungsTage = (Integer) parameter.getCWertAsObject();
			}
			session = factory.openSession();
			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId,
					theClientDto);
			// Alle BV-Eintraege des Mandanten auflisten.
			Criteria crit = session.createCriteria(FLRInternebestellung.class);
			// Filter nach Mandant
			crit.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));

			// Filter nach Artikel
			crit.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID, stuecklisteDto.getIId()));
			// Sortierung nach Liefertermin.
			crit.addOrder(Order.asc(FertigungFac.FLR_INTERNE_BESTELLUNG_T_LIEFERTERMIN));
			List<?> resultList = crit.list();
			// Der IB-Eintrag auf den hinverdichtet werden soll.
			Internebestellung ibEntityZiel = null;
			// nun alle Eintraege durchiterieren.
			for (Iterator<?> itBV = resultList.iterator(); itBV.hasNext();) {
				FLRInternebestellung ib = (FLRInternebestellung) itBV.next();
				Internebestellung ibEntity = em.find(Internebestellung.class, ib.getI_id());
				if (ibEntity == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				if (ibEntityZiel == null) {
					ibEntityZiel = ibEntity;
				} else {
					Date dErsteIB = ibEntityZiel.getTLiefertermin();
					Date dZweiteIB = ibEntity.getTLiefertermin();
					// einen Folgeeintrag anschaun.

					// PJ22059 Wenn die MaxFertigungssatzgroesse ueberschritten wird, dann nicht
					// verdichten
					boolean bMaxFertigungssatzgroesseUeberschritten = false;
					if (ib.getFlrstueckliste().getFlrartikel().getF_maxfertigungssatzgroesse() != null && ib
							.getFlrstueckliste().getFlrartikel().getF_maxfertigungssatzgroesse().doubleValue() > 0) {
						if (ibEntityZiel.getNMenge().add(ibEntity.getNMenge()).doubleValue() > ib.getFlrstueckliste()
								.getFlrartikel().getF_maxfertigungssatzgroesse()) {
							bMaxFertigungssatzgroesseUeberschritten = true;
						}
					}

					if (Helper.getDifferenzInTagen(dErsteIB, dZweiteIB) < iVerdichtungsTage
							&& bMaxFertigungssatzgroesseUeberschritten == false) {
						// Menge des 2. Eintrags zum ersten addieren

						InternebestellungDto ibDtoZiel = getInternebestellungFac()
								.internebestellungFindByPrimaryKey(ibEntityZiel.getIId());
						ibDtoZiel.setNMenge(ibEntityZiel.getNMenge().add(ibEntity.getNMenge()));
						getInternebestellungFac().updateInternebestellung(ibDtoZiel, theClientDto);

						// und den 2. loeschen
						InternebestellungDto intBesDto = context.getBusinessObject(InternebestellungFac.class)
								.internebestellungFindByPrimaryKey(ibEntity.getIId());
						context.getBusinessObject(InternebestellungFac.class).removeInternebestellung(intBesDto,
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
	 * @param iVerdichtungsTage Integer
	 * @param theClientDto      der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void verdichteInterneBestellung(Integer iVerdichtungsTage, TheClientDto theClientDto) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			// Zuerst die ID's aller vorkommenden Stuecklisten holen.
			String queryString = "select distinct ib." + FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID
					+ " from FLRInternebestellung ib where ib." + FertigungFac.FLR_INTERNE_BESTELLUNG_MANDANT_C_NR
					+ "='" + theClientDto.getMandant() + "'";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			// nun die Eintraege fuer jede Stueckliste verdichten.
			for (Iterator<?> itStk = rowCountResult.iterator(); itStk.hasNext();) {
				Integer stuecklisteIId = (Integer) itStk.next();
				verdichteInterneBestellungEinerStuecklisteEinesMandanten(stuecklisteIId, iVerdichtungsTage,
						theClientDto);
			}
		} finally {
			closeSession(session);
		}
	}

	public BigDecimal getFiktivenLagerstandZuZeitpunkt(ArtikelDto artikelDto, TheClientDto theClientDto,
			Timestamp tLagerstandsdatum, Integer partnerIIdStandort) {
		ArrayList<BewegungsvorschauDto> alVorschauDtos = getBewegungsvorschauSortiert(artikelDto, false, theClientDto,
				partnerIIdStandort, false, false, false, new HashSet());

		BigDecimal bdfiktiverLagerstand = new BigDecimal(0);

		if (lagerMinJeLager(theClientDto)) {
			// PJ18849 Summe der Lagerstaende des Standortes
			try {

				if (partnerIIdStandort != null) {

					LagerDto[] lagerDtos = getLagerFac().lagerFindByPartnerIIdStandortMandantCNr(partnerIIdStandort,
							theClientDto.getMandant(), true);
					for (int i = 0; i < lagerDtos.length; i++) {
						bdfiktiverLagerstand = bdfiktiverLagerstand.add(getLagerFac()
								.getLagerstandOhneExc(artikelDto.getIId(), lagerDtos[i].getIId(), theClientDto));
					}
				} else {
					// SP4990
					bdfiktiverLagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(),
							false, theClientDto);
				}
			} catch (EJBExceptionLP e) {
				// Wenn kein Lager gefunden wurde mit 0 anfangen.
			} catch (RemoteException e) {
				// Wenn kein Lager gefunden wurde mit 0 anfangen.
			}
		} else {
			try {
				LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);
				bdfiktiverLagerstand = getLagerFac().getLagerstandOhneExc(artikelDto.getIId(), lagerDto.getIId(),
						theClientDto);
			} catch (EJBExceptionLP e) {
				// Wenn kein Lager gefunden wurde mit 0 anfangen.
			} catch (RemoteException e) {
				// Wenn kein Lager gefunden wurde mit 0 anfangen.
			}
		}

		int i = 0;
		boolean finished = false;
		while (i < alVorschauDtos.size() && !finished) {
			BewegungsvorschauDto bwvsDto = alVorschauDtos.get(i);
			if (bwvsDto.getTLiefertermin().before(tLagerstandsdatum)
					|| bwvsDto.getTLiefertermin().equals(tLagerstandsdatum)) {
				bdfiktiverLagerstand = bdfiktiverLagerstand.add(bwvsDto.getNMenge());
			} else {
				finished = true;
			}
			i++;
		}
		return bdfiktiverLagerstand;
	}

	public void removeLockDerInternenBestellungWennIchIhnSperre(TheClientDto theClientDto) {
		try {

			String mandant = theClientDto.getMandant();

			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(LOCKME_INTERNEBESTELLUNG_TP, mandant);
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

	public void pruefeBearbeitenDerInternenBestellungErlaubt(TheClientDto theClientDto) {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			lockMeDto.setCWas(mandant);
			lockMeDto.setCWer(LOCKME_INTERNEBESTELLUNG_TP);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(LOCKME_INTERNEBESTELLUNG_TP, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INTERNEBESTELLUNG_IST_GESPERRT, al,
							new Exception("Interne Bestellung auf Mandant " + mandant + " gesperrt durch Personal Id "
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

}
