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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.assembler.BelegbuchungDtoAssembler;
import com.lp.server.finanz.assembler.KontoDtoAssembler;
import com.lp.server.finanz.assembler.SteuerkategorieDtoAssembler;
import com.lp.server.finanz.assembler.SteuerkategoriekontoDtoAssembler;
import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.bl.FinanzValidator;
import com.lp.server.finanz.ejb.Belegbuchung;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Reversechargeart;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Steuerkategoriekonto;
import com.lp.server.finanz.ejb.SteuerkategoriekontoQuery;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BucheBelegPeriodeInfoDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungInfoDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DebitorkontoId;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KontoId;
import com.lp.server.util.SachkontoId;
import com.lp.server.util.Validator;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.ISelect;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.EventLogger;
import com.lp.server.util.logger.LogEventProducer;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegbuchungFacBean extends Facade implements BelegbuchungFac {
	@PersistenceContext
	private EntityManager em;
	
	private EventLogger evLog = new EventLogger(getEventLoggerFac(), BelegbuchungFacBean.class) ;

	public BelegbuchungDto createBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
				
		// Primary Key bestimmen
		PKGeneratorObj pkGen = getPKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BELEGBUCHUNG);
		belegbuchungDto.setIId(pk);

		evLog.info("verbuche", LogEventProducer.create(belegbuchungDto));
		try {
			Belegbuchung belegbuchung = new Belegbuchung(
					belegbuchungDto.getIId(), belegbuchungDto.getBelegartCNr(),
					belegbuchungDto.getIBelegiid(),
					belegbuchungDto.getBuchungIId());
			em.persist(belegbuchung);
			em.flush();
			setBelegbuchungFromBelegbuchungDto(belegbuchung, belegbuchungDto);
			return belegbuchungDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
		if (belegbuchungDto != null) {

			evLog.info("storniere", LogEventProducer.create(belegbuchungDto));
			
			Integer iId = belegbuchungDto.getIId();
			Belegbuchung toRemove = em.find(Belegbuchung.class, iId);
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
	}

	public BelegbuchungDto updateBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
		Integer iId = belegbuchungDto.getIId();
		Belegbuchung belegbuchung = em.find(Belegbuchung.class, iId);
		if (belegbuchung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setBelegbuchungFromBelegbuchungDto(belegbuchung, belegbuchungDto);
		return belegbuchungDto;
	}

	public BelegbuchungDto belegbuchungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Belegbuchung belegbuchung = em.find(Belegbuchung.class, iId);
		if (belegbuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBelegbuchungDto(belegbuchung);
	}

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiid(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBelegartCNrBelegiid);
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegiid);
		Belegbuchung belegbuchung = null;
		try {
			belegbuchung = (Belegbuchung) query.getSingleResult();
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Belegbuchung nicht gefunden. Belegart: " + belegartCNr
							+ " ID: " + iBelegiid.intValue());
		}
		return assembleBelegbuchungDto(belegbuchung);
	}

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiidOhneExc(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP {
		HvTypedQuery<Belegbuchung> query = new HvTypedQuery<Belegbuchung>(
				em.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBelegartCNrBelegiid));
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegiid);
		List<Belegbuchung> belegbuchung = query.getResultList();
		return belegbuchung.size() == 1 ? assembleBelegbuchungDto(belegbuchung
				.get(0)) : null;
	}

	public BelegbuchungDto belegbuchungFindByBuchungIId(Integer buchungIId)
			throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBuchungIId);
			query.setParameter(1, buchungIId);
			Belegbuchung belegbuchung = (Belegbuchung) query.getSingleResult();
			return assembleBelegbuchungDto(belegbuchung);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	public BelegbuchungDto belegbuchungFindByBuchungIIdOhneExc(
			Integer buchungIId) {
			HvTypedQuery<Belegbuchung> query = new HvTypedQuery<Belegbuchung>(
					em.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBuchungIId));
			query.setParameter(1, buchungIId);
			List<Belegbuchung> list = query.getResultList();
			if(list == null || list.size() == 0) return null;
			return assembleBelegbuchungDto(list.get(0));
	}

	private void setBelegbuchungFromBelegbuchungDto(Belegbuchung belegbuchung,
			BelegbuchungDto belegbuchungDto) {
		belegbuchung.setBelegartCNr(belegbuchungDto.getBelegartCNr());
		belegbuchung.setIBelegiid(belegbuchungDto.getIBelegiid());
		belegbuchung.setBuchungIId(belegbuchungDto.getBuchungIId());
		belegbuchung.setBelegartCNr(belegbuchungDto.getBelegartCNr());
		belegbuchung.setBuchungIIdZahlung(belegbuchungDto
				.getBuchungIIdZahlung());
		em.merge(belegbuchung);
		em.flush();
	}

	protected BelegbuchungDto assembleBelegbuchungDto(Belegbuchung belegbuchung) {
		return BelegbuchungDtoAssembler.createDto(belegbuchung);
	}

	private BelegbuchungDto[] assembleBelegbuchungDtos(
			Collection<?> belegbuchungs) {
		List<BelegbuchungDto> list = new ArrayList<BelegbuchungDto>();
		if (belegbuchungs != null) {
			Iterator<?> iterator = belegbuchungs.iterator();
			while (iterator.hasNext()) {
				Belegbuchung belegbuchung = (Belegbuchung) iterator.next();
				list.add(assembleBelegbuchungDto(belegbuchung));
			}
		}
		BelegbuchungDto[] returnArray = new BelegbuchungDto[list.size()];
		return (BelegbuchungDto[]) list.toArray(returnArray);
	}

	
	public BuchungInfoDto verbucheRechnung(final Integer rechnungIId, 
			final TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
//		
//		ExperimentAction<BuchungDto> useAction = new ExperimentAction<BuchungDto>() {
//			@Override
//			public String name() {
//				return "verbucheRechnungOriginal" ;
//			}
//			@Override
//			public BuchungDto run() throws RemoteException {
//				return verbucheRechnungOriginal(rechnungIId, theClientDto);
//			}
//		};
//
//		ExperimentAction<BuchungDto> tryAction = new ExperimentAction<BuchungDto>() {
//			@Override
//			public String name() {
//				return "verbucheRechnungMitReversechargeart" ;
//			}
//			@Override
//			public BuchungDto run() throws RemoteException {
//				return verbucheRechnungNew(rechnungIId, theClientDto);
//			}
//		};
//		
//		Experiment<BuchungDto> e = scientistFac.run(useAction, tryAction) ;
//		return e.getResult() ;
		
		return verbucheRechnungNew(rechnungIId, theClientDto) ;
	}

	private BuchungInfoDto verbucheRechnungNew(Integer rechnungIId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			return null ;
		}
		
		long startTime = System.currentTimeMillis();
		BuchungDto buchungDto = null;
		RechnungDto rechnungDto = getRechnungFac()
				.rechnungFindByPrimaryKey(rechnungIId);
		RechnungartDto rechnungartDto = getRechnungServiceFac()
				.rechnungartFindByPrimaryKey(
						rechnungDto.getRechnungartCNr(), theClientDto);
		String rechnungtyp = rechnungartDto.getRechnungtypCNr();
		String buchungBelegart = null ;
		String belegbuchungBelegart = null ;
		BelegbuchungDto bbDto = null;
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			buchungBelegart = LocaleFac.BELEGART_FIBU_RECHNUNG ;
			belegbuchungBelegart = LocaleFac.BELEGART_RECHNUNG ;
			bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(
							LocaleFac.BELEGART_RECHNUNG, rechnungIId);
		} else if (rechnungtyp
				.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			buchungBelegart = LocaleFac.BELEGART_GUTSCHRIFT ;
			belegbuchungBelegart = LocaleFac.BELEGART_GUTSCHRIFT ;
			bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(
							LocaleFac.BELEGART_GUTSCHRIFT, rechnungIId);
		} else if (rechnungtyp
				.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
			// ignorieren, wird unten uebersprungen
		} else {
			myLogger.error("Ungueltiger Rechnungstyp '" + rechnungtyp + ", keine Verbuchung moeglich");
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Ungueltiger Rechnungstyp: " + rechnungtyp);
		}

		if (bbDto != null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
					"Beleg bereits verbucht: ID=" + rechnungIId);
		}
		
		if(rechnungDto.isStorniert() || 
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG.equals(rechnungtyp))  {
			// keine buchung notwendig
			return null ;
		}
		
		// wenn nicht, passts ja
		boolean bRechnung0Erlaubt = getParameterFac()
				.getFinanzRechnungWert0Erlaubt(theClientDto.getMandant());
		if (!bRechnung0Erlaubt && 
				(rechnungDto.getNWert() == null || rechnungDto.getNWert().signum() == 0)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT, "",
					rechnungDto.getCNr());
		}
		
		if(rechnungDto.getNWert() == null || rechnungDto.getNWert().signum() == 0) {
			return null ;
		}
		
		// SP 2013/01327 nur Rechnungen mit Wert <> 0 abhaengig vom
		// Parameter

		// TODO: bei Anzahlungsrechnung wird auf
		// Anzahlungserloeskonto
		// gebucht
		// bei Schlussrechnung werden die Anzahlungen am
		// Anzahlungserloeskonto gegengebucht und der Erloes der
		// Schlussrechnung gebucht
		// (Anzahlung- + Schlussrechnung = gleicher Auftrag)
		// noch Googlen wie das ueblicherweise gebucht wird
		//
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(belegbuchungBelegart);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(
						getExportVariante(theClientDto),
						getExportFormat(theClientDto),
						fibuExportKriterienDto, theClientDto);
		FibuexportDto[] exportDaten;
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			exportDaten = manager.getExportdatenRechnung(
					rechnungIId, null);
		} else if (rechnungtyp
				.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			exportDaten = manager.getExportdatenGutschrift(
					rechnungIId, null);
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Rechnungstyp fuer Fibu nicht definiert: Typ="
							+ rechnungtyp);
		}

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto);
		FinanzValidator.debitorkontoDefinition(kundeDto);
		Konto debitorKonto = em.find(Konto.class,
				kundeDto.getIidDebitorenkonto());
		FinanzValidator.steuerkategorieDefinition(debitorKonto, rechnungDto);

		buchungDto = setupBuchungDto(rechnungDto, kundeDto, theClientDto) ;
		buchungDto.setBelegartCNr(buchungBelegart);					

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBelegartCNr(belegbuchungBelegart);
		Integer steuerkategorieIId = null;
		Integer debitorenKontoId = exportDaten[0].getKontoDto().getIId();
		Integer debitorenKontoUebersteuertId = null ;
		if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
			steuerkategorieIId = verifySteuerkategorieIId(debitorKonto, 
					rechnungDto.getReversechargeartId(), theClientDto) ;
		} else {
			debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
			steuerkategorieIId = verifySteuerkategorieIId(debitorenKontoUebersteuertId, rechnungDto.getReversechargeartId(), theClientDto) ;
		}

		boolean isReversecharge = isRechnungDtoReversecharge(rechnungDto) ;
		List<BuchungdetailDto> details = 
				getBuchungdetailsVonExportDtos(exportDaten, steuerkategorieIId, 
						debitorenKontoId, debitorenKontoUebersteuertId, true, false, isReversecharge, theClientDto);
		
		try {
			if (rechnungDto.isSchlussRechnung()) {
				Integer kontoId = debitorenKontoUebersteuertId == null ? debitorenKontoId : debitorenKontoUebersteuertId;
				List<BuchungdetailDto> detailsAnzahlungen = 
						createSchlussrechnungGegenbuchung(
								rechnungDto, kontoId, steuerkategorieIId, theClientDto);
				details.addAll(detailsAnzahlungen);
			}

			if(rechnungDto.isAnzahlungsRechnung()) {				
				if(rechnungDto.getNWertust().signum() != 0) {					
					if(details.size() != 2) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_ANZAHLUNG,
								new Exception(rechnungDto.getCNr()));
					}					
				}
				
				// SP8324 Den Bruttobetrag auf AnzahlungVerrechnet verbuchen
				// Es gibt noch keine Steuerbuchung, die wird erst zum 
				// Zeitpunkt des Bezahlens der Anzahlungsrechnung 
				// durchgefuehrt.
				BuchungdetailDto anzVer = details.get(1);
				if(anzVer.getNUst().signum() != 0) {
					anzVer.setNBetrag(anzVer.getNBetrag().add(anzVer.getNUst()));
					anzVer.setNUst(BigDecimal.ZERO);
//					details.get(0).setKontoIIdGegenkonto(anzVer.getKontoIId());
					details.remove(2);
				}
			}
			
			if(isReversecharge) {
				if(rechnungDto.getMwstsatzIId() == null) {
					myLogger.error("Rechnung '" + rechnungDto.getCNr() + "' hat keine RC MwstSatzId definiert!");
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC,
							new Exception(rechnungDto.getCNr()));
				}
				MwstsatzDto mwstSatz = getMandantFac().mwstsatzFindByPrimaryKey(rechnungDto.getMwstsatzIId(), theClientDto);
				BigDecimal steuer = rechnungDto.getNWert().multiply(new BigDecimal(mwstSatz.getFMwstsatz()).divide(new BigDecimal("100")));
				steuer = Helper.rundeKaufmaennisch(steuer, FinanzFac.NACHKOMMASTELLEN);

//				int debitorenZeile = LocaleFac.BELEGART_GUTSCHRIFT.equals(buchungBelegart) ? 1 : 0 ;
				int debitorenZeile = LocaleFac.BELEGART_GUTSCHRIFT.equals(buchungBelegart) ? details.size() - 1 : 0;
				Integer skId = verifySteuerkategorieIId(
						details.get(debitorenZeile).getKontoIId(), rechnungDto.getReversechargeartId(), theClientDto) ;
				Integer ustKontoIId = getFinanzServiceFac()
						.getUstKontoFuerSteuerkategorie(skId,
								mwstSatz.getIIMwstsatzbezId(), rechnungDto.getTBelegdatum());
				Integer rcStKontoIId = getFinanzServiceFac().getEUstKontoFuerSteuerkategorie(
						skId, mwstSatz.getIIMwstsatzbezId(), rechnungDto.getTBelegdatum());

				if(ustKontoIId != null && rcStKontoIId != null) {
					BuchungdetailDto ustHaben = new BuchungdetailDto();
					ustHaben.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					ustHaben.setNBetrag(steuer);
					ustHaben.setNUst(BigDecimal.ZERO);
					ustHaben.setKontoIId(ustKontoIId);

					BuchungdetailDto ustSoll = new BuchungdetailDto();
					ustSoll.setBuchungdetailartCNr(BuchenFac.SollBuchung);
					ustSoll.setNBetrag(steuer);
					ustSoll.setNUst(BigDecimal.ZERO);
					ustSoll.setKontoIId(rcStKontoIId);

					details.add(ustHaben);
					details.add(ustSoll);
				}
			}

			// 	HelperServer.printBuchungssatz(details, getFinanzFac(), System.out);

			// Das Verbuchen einer Rechnung muss immer den
			// Buchungsregeln entsprechen
			buchungDto = getBuchenFac().buchen(buchungDto,
					details.toArray(new BuchungdetailDto[0]), rechnungDto.getReversechargeartId(), true, theClientDto);

			// in Tabelle buchungRechnung speichern
			belegbuchungDto.setBuchungIId(buchungDto.getIId());
			belegbuchungDto.setIBelegiid(rechnungDto.getIId());
			createBelegbuchung(belegbuchungDto, theClientDto);

/*
 * SP4508 Bei Schlussrechnung wird nicht mehr ausgeziffert
 * Unter anderem kann die auszuziffernde Anzahlungsrechnung im bereits 
 * geschlossenen Geschaeftsjahr sein.
 * 
			if (rechnungDto.isSchlussRechnung()) {
				List<BelegbuchungDto> ausziffernListe = new ArrayList<BelegbuchungDto>();
				ausziffernListe.add(belegbuchungDto);

				for(RechnungDto rech : getRechnungFac().rechnungFindByAuftragIId(rechnungDto.getAuftragIId())) {
					if(rech.isAnzahlungsRechnung()) {
						ausziffernListe.addAll(getAlleBelegbuchungenInklZahlungenAR(rech.getIId()));
					}
				}
				FinanzamtDto famt = getFinanzFac().finanzamtFindByPrimaryKey(
						debitorKonto.getFinanzamtIId(), debitorKonto.getMandantCNr(), theClientDto);
				
				Integer kontoId = famt.getKontoIIdAnzahlungErhaltBezahlt() ;
				KontoDto kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoId, rechnungDto.getIId()) ;
				belegbuchungenAusziffernWennNoetig(kontoDto.getIId(), theClientDto, ausziffernListe);
				
				kontoId = famt.getKontoIIdAnzahlungErhaltVerr() ;
				kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoId, rechnungDto.getIId()) ;
				belegbuchungenAusziffernWennNoetig(kontoDto.getIId(), theClientDto, ausziffernListe);
			}
 * 			
 */
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		long dauer = System.currentTimeMillis() - startTime;
		myLogger.logData(
				"Verbuchen der Rechnung '" + rechnungDto.getCNr() +
				"' dauerte " + dauer + " ms.");

		return new BuchungInfoDto(buchungDto, exportDaten);
	}
	
	/**
	 * Verbuchen einer Rechnung
	 *
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return null wenn keine integrierte Fibu, ansonsten der Buchungsdatensatz
	 * @throws EJBExceptionLP
	 */
/*	
	public BuchungDto verbucheRechnungOriginal(Integer rechnungIId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			return null ;
		}
		
		long startTime = System.currentTimeMillis();
		BuchungDto buchungDto = null;
		RechnungDto rechnungDto = getRechnungFac()
				.rechnungFindByPrimaryKey(rechnungIId);
		RechnungartDto rechnungartDto = getRechnungServiceFac()
				.rechnungartFindByPrimaryKey(
						rechnungDto.getRechnungartCNr(), theClientDto);
		String rechnungtyp = rechnungartDto.getRechnungtypCNr();
		String buchungBelegart = null ;
		String belegbuchungBelegart = null ;
		BelegbuchungDto bbDto = null;
		try {
			if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				buchungBelegart = LocaleFac.BELEGART_FIBU_RECHNUNG ;
				belegbuchungBelegart = LocaleFac.BELEGART_RECHNUNG ;
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_RECHNUNG, rechnungIId);
			} else if (rechnungtyp
					.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				buchungBelegart = LocaleFac.BELEGART_GUTSCHRIFT ;
				belegbuchungBelegart = LocaleFac.BELEGART_GUTSCHRIFT ;
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_GUTSCHRIFT, rechnungIId);
			} else if (rechnungtyp
					.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				// ignorieren, wird unten uebersprungen
			} else {
				myLogger.error("Ungueltiger Rechnungstyp, keine Verbuchung moeglich");
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
						"Ungueltiger Rechnungstyp: " + rechnungtyp);
			}

			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Beleg bereits verbucht: ID=" + rechnungIId);
			}
		} catch (RemoteException ex2) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex2);
		}
		
		if(rechnungDto.isStorniert() || 
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG.equals(rechnungtyp))  {
			// keine buchung notwendig
			return null ;
		}
		
		// wenn nicht, passts ja
		boolean bRechnung0Erlaubt = getParameterFac()
				.getFinanzRechnungWert0Erlaubt(theClientDto.getMandant());
		if (!bRechnung0Erlaubt && 
				(rechnungDto.getNWert() == null || rechnungDto.getNWert().signum() == 0)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT, "",
					rechnungDto.getCNr());
		}
		
		if(rechnungDto.getNWert() == null || rechnungDto.getNWert().signum() == 0) {
			return null ;
		}
		
		// SP 2013/01327 nur Rechnungen mit Wert <> 0 abhaengig vom
		// Parameter

		// TODO: bei Anzahlungsrechnung wird auf
		// Anzahlungserloeskonto
		// gebucht
		// bei Schlussrechnung werden die Anzahlungen am
		// Anzahlungserloeskonto gegengebucht und der Erloes der
		// Schlussrechnung gebucht
		// (Anzahlung- + Schlussrechnung = gleicher Auftrag)
		// noch Googlen wie das ueblicherweise gebucht wird
		//
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(belegbuchungBelegart);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(
						getExportVariante(theClientDto),
						getExportFormat(theClientDto),
						fibuExportKriterienDto, theClientDto);
		FibuexportDto[] exportDaten;
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			exportDaten = manager.getExportdatenRechnung(
					rechnungIId, null);
		} else if (rechnungtyp
				.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			exportDaten = manager.getExportdatenGutschrift(
					rechnungIId, null);
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Rechnungstyp fuer Fibu nicht definiert: Typ="
							+ rechnungtyp);
		}

		Konto debitorKonto = null;
		try {
			KundeDto kundeDto = getKundeFac()
					.kundeFindByPrimaryKey(
							rechnungDto.getKundeIId(), theClientDto);
			FinanzValidator.debitorkontoDefinition(kundeDto);
			debitorKonto = em.find(Konto.class,
					kundeDto.getIidDebitorenkonto());
			if (debitorKonto.getSteuerkategorieIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
						"Keine Steuerkategorie bei Debitorenkonto ",
						debitorKonto.getCNr() + " "
								+ debitorKonto.getCBez());
			}

			buchungDto = setupBuchungDto(rechnungDto, kundeDto, theClientDto) ;
			buchungDto.setBelegartCNr(buchungBelegart);					
		} catch (Exception ex3) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex3);
		}

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBelegartCNr(belegbuchungBelegart);
		Integer steuerkategorieIId = null;
		Integer debitorenKontoId = exportDaten[0].getKontoDto().getIId();
		Integer debitorenKontoUebersteuertId = null ;
		boolean reverseCharge = rechnungDto.isReverseCharge() ;
//		if (reverseCharge) {
//			if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
//				steuerkategorieIId = debitorKonto
//						.getSteuerkategorieIIdReverse();
//				if(steuerkategorieIId == null)
//					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
//									"Keine Steuerkategorie bei Debitorenkonto "
//											+ debitorKonto.getCNr(), debitorKonto
//											.getCNr() + " " + debitorKonto.getCBez());
//			} else {
//				debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
//				Konto debKontouebersteuert = em
//						.find(Konto.class, debitorenKontoUebersteuertId);
//				steuerkategorieIId = debKontouebersteuert
//						.getSteuerkategorieIIdReverse();
//				if(steuerkategorieIId == null)
//					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
//									"Keine Steuerkategorie bei Debitorenkonto "
//											+ debKontouebersteuert.getCNr(), debKontouebersteuert
//											.getCNr() + " " + debKontouebersteuert.getCBez());
//
//			}
//		} else
			
		if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
			steuerkategorieIId = verifySteuerkategorieIId(debitorKonto) ;
		} else {
			debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
			steuerkategorieIId = verifySteuerkategorieIId(debitorenKontoUebersteuertId) ;
		}

		List<BuchungdetailDto> details = 
				getBuchungdetailsVonExportDtos(exportDaten, steuerkategorieIId, 
						debitorenKontoId, debitorenKontoUebersteuertId, true, false, reverseCharge, theClientDto);
		
		try {
			if (rechnungDto.isSchlussRechnung()) {
				// zusaetzlich alle Anzahlungsrechnungen
				// ausbuchen
				details.addAll(getDetailsSchlussrechnungAnzahlungen(
						rechnungDto, theClientDto));
			}

			if(reverseCharge) {
				if(rechnungDto.getMwstsatzIId() == null) {
					myLogger.error("Rechnung '" + rechnungDto.getCNr() + "' hat keine RC MwstSatzId definiert!");
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC,
							new Exception(rechnungDto.getCNr()));
				}
				MwstsatzDto mwstSatz = getMandantFac().mwstsatzFindByPrimaryKey(rechnungDto.getMwstsatzIId(), theClientDto);
				BigDecimal steuer = rechnungDto.getNWert().multiply(new BigDecimal(mwstSatz.getFMwstsatz()).divide(new BigDecimal("100")));
				steuer = Helper.rundeKaufmaennisch(steuer, FinanzFac.NACHKOMMASTELLEN);

				KontoDto debitor = getFinanzFac().kontoFindByPrimaryKey(details.get(0).getKontoIId());
				Integer ustKontoIId = getFinanzServiceFac().getUstKontoFuerSteuerkategorie(debitor.getSteuerkategorieIIdReverse(), mwstSatz.getIIMwstsatzbezId());
				Integer rcStKontoIId = getFinanzServiceFac().getEUstKontoFuerSteuerkategorie(debitor.getSteuerkategorieIIdReverse(), mwstSatz.getIIMwstsatzbezId());

				if(ustKontoIId != null && rcStKontoIId != null) {
					BuchungdetailDto ustHaben = new BuchungdetailDto();
					ustHaben.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					ustHaben.setNBetrag(steuer);
					ustHaben.setNUst(BigDecimal.ZERO);
					ustHaben.setKontoIId(ustKontoIId);

					BuchungdetailDto ustSoll = new BuchungdetailDto();
					ustSoll.setBuchungdetailartCNr(BuchenFac.SollBuchung);
					ustSoll.setNBetrag(steuer);
					ustSoll.setNUst(BigDecimal.ZERO);
					ustSoll.setKontoIId(rcStKontoIId);

					details.add(ustHaben);
					details.add(ustSoll);
				}
			}

			// 	HelperServer.printBuchungssatz(details, getFinanzFac(), System.out);

			// Das Verbuchen einer Rechnung muss immer den
			// Buchungsregeln entsprechen
			buchungDto = getBuchenFac().buchen(buchungDto,
					details.toArray(new BuchungdetailDto[0]), null, true, theClientDto);

			// in Tabelle buchungRechnung speichern
			belegbuchungDto.setBuchungIId(buchungDto.getIId());
			belegbuchungDto.setIBelegiid(rechnungDto.getIId());
			createBelegbuchung(belegbuchungDto, theClientDto);

			if (rechnungDto.isSchlussRechnung()) {
				List<BelegbuchungDto> ausziffernListe = new ArrayList<BelegbuchungDto>();
				ausziffernListe.add(belegbuchungDto);

				for(RechnungDto rech : getRechnungFac().rechnungFindByAuftragIId(rechnungDto.getAuftragIId())) {
					if(rech.isAnzahlungsRechnung()) {
						ausziffernListe.addAll(getAlleBelegbuchungenInklZahlungenAR(rech.getIId()));
					}
				}
				FinanzamtDto famt = getFinanzFac().finanzamtFindByPrimaryKey(
						debitorKonto.getFinanzamtIId(), debitorKonto.getMandantCNr(), theClientDto);
				
				Integer kontoId = famt.getKontoIIdAnzahlungErhaltBezahlt() ;
				KontoDto kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoId, rechnungDto.getIId()) ;
				belegbuchungenAusziffernWennNoetig(kontoDto.getIId(), theClientDto, ausziffernListe);
				
				kontoId = famt.getKontoIIdAnzahlungErhaltVerr() ;
				kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoId, rechnungDto.getIId()) ;
				belegbuchungenAusziffernWennNoetig(kontoDto.getIId(), theClientDto, ausziffernListe);
				
//								belegbuchungenAusziffernWennNoetig(rechnungDto.isReverseCharge() ?
//										famt.getKontoIIdRCAnzahlungErhaltBezahlt() :
//										famt.getKontoIIdAnzahlungErhaltBezahlt(), theClientDto, ausziffernListe);
//								belegbuchungenAusziffernWennNoetig(rechnungDto.isReverseCharge() ?
//										famt.getKontoIIdRCAnzahlungErhaltVerr() :
//										famt.getKontoIIdAnzahlungErhaltVerr(), theClientDto, ausziffernListe);
			}

			// Status der Rechnung auf verbucht setzen
			// AD: nicht auf verbucht setzen, macht erst die UVA
			// rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT
			// ); getRechnungFac().updateRechnung(rechnungDto,
			// theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		long dauer = System.currentTimeMillis() - startTime;
		myLogger.logData("Verbuchen der Rechnung dauerte " + dauer
				+ " ms.");

		return buchungDto;
	}
*/

	
	private Integer verifySteuerkategorieIId(Integer kontoId, Integer reversechargeartId, TheClientDto theClientDto) {
		Konto debitorenKonto = em.find(Konto.class, kontoId);
		return verifySteuerkategorieIId(debitorenKonto, reversechargeartId, theClientDto) ;
	}	
	
	private Integer verifySteuerkategorieIId(Konto konto, 
			Integer reversechargeartId, TheClientDto theClientDto) {
		FinanzValidator.steuerkategorieDefinition(konto);

		SteuerkategorieDto skDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
				konto.getSteuerkategorieCNr(), reversechargeartId, konto.getFinanzamtIId(), theClientDto) ;
		if(skDto == null || skDto.getIId() == null) {
			throw EJBExcFactory.steuerkategorieFehlt(
					konto.getFinanzamtIId(), reversechargeartId,
					konto.getSteuerkategorieCNr()) ;
		}
		
		return skDto.getIId() ;
	}

	private class AnzahlungrechnungFilter implements ISelect<RechnungDto> {
		@Override
		public boolean select(RechnungDto element) {
				return element.isAnzahlungsRechnung() &&
						!element.isStorniert() && 
						element.getNWertfw() != null;
		}
	}
	
	private class SchlussrechnungFilter implements ISelect<RechnungDto> {
		@Override
		public boolean select(RechnungDto element) {
			return element.isSchlussRechnung() &&
					!(element.isStorniert() || element.isAngelegt());
		}
	}
	
	/**
	 * Aktive Anzahlungsrechnungen mit Wert ermitteln</br>
	 * <p>Es werden alle Anzahlungsrechnungen die nicht storniert sind 
	 * und einen Rechnungswert haben geliefert</br>
	 * 
	 * @param auftragId die Id jenes Auftrags der Anzahlungs- und Schlussrechnung
	 * verbindet
	 * @return eine (leere) Liste von Anzahlungsrechnungen
	 * @throws RemoteException
	 */
	private Collection<RechnungDto> findAnzahlungsRechnungen(
			Integer auftragId) throws RemoteException {
		RechnungDto[] reDtos = getRechnungFac()
				.rechnungFindByAuftragIId(auftragId);
		return CollectionTools.select(reDtos, new AnzahlungrechnungFilter());
	}
	
	/**
	 * Schlussrechnung zum Auftrag ermitteln</br>
	 * <p>Die Schlussrechnung muss zumindest offen sein</p>
	 * @param auftragId
	 * @return HvOptional der Schlussrechnung
	 * @throws RemoteException
	 */
	protected HvOptional<RechnungDto> findSchlussRechnung(
			Integer auftragId) throws RemoteException {
		RechnungDto[] reDtos = getRechnungFac()
				.rechnungFindByAuftragIId(auftragId);
		Collection<RechnungDto> c = CollectionTools.select(
				reDtos, new SchlussrechnungFilter());
		if(c.size() != 1) return HvOptional.empty();
		return HvOptional.of(c.iterator().next());
	}
	
	/**
	 * Gegenbuchungen der Anzahlungsrechnungszahlungen durchfuehren</br>
	 * <p>Die Schlussrechnung muss die bisher erhaltenen Anzahlungsrechnungsbezahlungen
	 * gegenbuchen (aka "zuruecknehmen").</p>
	 * <p>Die Anzahlungsbetraege waren Kredite und werden nun mit der 
	 * Schlussrechnung verbraucht. D.h. der Debitor muss fuer die Schlussrechnung
	 * nur noch den "offenen" Betrag bezahlen.</p>
	 * <p>Da die Schlussrechnung ueber den kompletten Betrag USt.Buchungen macht, 
	 * muessen die Steuerbuchungen der Anzahlungsbetragsbuchungen ebenfalls
	 * "zurueckgenommen" bzw. fuer die Verringerung der Steuerbetrags der SR
	 * verwendet werden.</p>
	 * <p>Wir haben uns dazu entschlossen, diese Gegenbuchung mit einer neuen
	 * Fibu-Belegart ("Schlussrechnung") zu verbuchen. Damit sollte fuer allen
	 * Beteiligten klarer sein, was Sache ist. Es macht auch die Ruecknahme
	 * von Buchungen einfacher, speziell in den komplizierteren Faellen, wie</br>
	 * 
	 * <li> Ruecknahme einer Anzahlungszahlung</li>
	 * <li> Aenderung der Schlussrechnung und erneute Aktivierung</li>
	 * <li> Zahlung einer Anzahlungsrechnung nachdem die Schlussrechnung
	 * erstellt wurde (in diesem Falle muss auch gleich die Gegenbuchung 
	 * erfolgen).
	 * </p>
	 * <p>Diese Gegenbuchung (neue Belegbuchung und Fibubuchung) wird fuer
	 * jeden Zahlungsvorgang einer Anzahlungsrechnung durchgefuehrt. Die
	 * Belegnummer der Belegbuchung ist jedoch die Schlussrechnungsbelegnummer.
	 * Hintergrund: Bei einer eventuellen Periodenuebernahme (mit offener 
	 * Schlussrechnung und/oder offener Anzhlungsrechnung) bleibt so die
	 * Uebersicht erhalten</p>
	 * 
	 * @param schlussrechnungDto
	 * @param debitorenKontoId
	 * @param steuerkategorieId
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 */
	private List<BuchungdetailDto> createSchlussrechnungGegenbuchung(
			RechnungDto schlussrechnungDto, Integer debitorenKontoId, 
			Integer steuerkategorieId, TheClientDto theClientDto) throws RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();

		Collection<RechnungDto> azDtos = findAnzahlungsRechnungen(
				schlussrechnungDto.getAuftragIId());
				
		for (RechnungDto azDto : azDtos) {
			createGegenbuchungAnzahlungsrechnung(schlussrechnungDto, 
					azDto, debitorenKontoId, steuerkategorieId, theClientDto);

			createGegenbuchungOffeneAnzahlungen(schlussrechnungDto, azDto,
					debitorenKontoId, theClientDto);
			
//			list.addAll(detailsSchlussrechnungOffeneAnzahlungen(
//					schlussrechnungDto, azDto, debitorenKontoId, steuerkategorieId, theClientDto));
//

//			list.addAll(buildDetailsAusAnzahlungFuerSchlussrechnung1(
//					azDto, theClientDto));

			// Kursdifferenz Datum der Zahlung zu Schlussrechnungsdatum
/*			
			RechnungzahlungDto[] zahlungDtos = getRechnungFac()
					.zahlungFindByRechnungIId(azDto.getIId());
			for (RechnungzahlungDto zahlungDto : zahlungDtos) {
				if(zahlungDto.isGutschrift()) continue;

				BigDecimal azWert = zahlungDto.getNBetragfw().add(zahlungDto.getNBetragUstfw())
						.divide(schlussrechnungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal kursDifferenz = Helper.rundeGeldbetrag(
						zahlungDto.getNBetrag().add(zahlungDto.getNBetragUst()).subtract(azWert));
				if(kursDifferenz.signum() != 0) {
					boolean kursVerlust = kursDifferenz.signum() > 0; // Wir wandeln Fremdwaehrung in Mandantenwaehrung
					SachkontoId kursKonto = getKursKonto(kursVerlust, steuerkategorieId, schlussrechnungDto);
					
					BuchungdetailDto bd1 = BuchungdetailDto.soll(
							debitorenKontoId, null, kursDifferenz.abs(), BigDecimal.ZERO);
					BuchungdetailDto bd2 = BuchungdetailDto.haben(kursKonto.id(),
							debitorenKontoId, kursDifferenz.abs(), BigDecimal.ZERO);
					if(!kursVerlust) {
						bd1.swapSollHaben();
						bd2.swapSollHaben();
					}
					list.add(bd1);
					list.add(bd2);
				}
			}
*/

			// Kursdifferenz berechnen
			// Das Anzahlungsrechnungsdatum auf Schlussrechnungsdatum beziehen
/*
 * SP8324 Diese Variante ist nicht mehr notwendig, weil die Gegenbuchung der Anzahlungszahlung
 * eh bereits die Wertstellung zur Schlussrechnung durchfuehrt
 * 
*/
			BigDecimal azWert = azDto.getNWertfw().add(azDto.getNWertustfw())
					.divide(schlussrechnungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal kursDifferenz = Helper.rundeGeldbetrag(
					azDto.getNWert().add(azDto.getNWertust()).subtract(azWert));
			if(kursDifferenz.signum() != 0) {
				boolean kursVerlust = kursDifferenz.signum() > 0; // Wir wandeln Fremdwaehrung in Mandantenwaehrung
				SachkontoId kursKonto = getKursKonto(kursVerlust, steuerkategorieId, schlussrechnungDto);
				
				BuchungdetailDto bd1 = BuchungdetailDto.soll(
						debitorenKontoId, null, kursDifferenz.abs(), BigDecimal.ZERO);
				BuchungdetailDto bd2 = BuchungdetailDto.haben(kursKonto.id(),
						debitorenKontoId, kursDifferenz.abs(), BigDecimal.ZERO);
				if(!kursVerlust) {
					bd1.swapSollHaben();
					bd2.swapSollHaben();
				}
				list.add(bd1);
				list.add(bd2);
			}
		
		}
		return list;
	}

	
	private HvOptional<BuchungdetailDto> findDetail(
			Integer kontoId, BuchungdetailDto[] details) {
		for(BuchungdetailDto detail : details) {
			if(detail.getKontoIId().equals(kontoId)) return HvOptional.of(detail);
		}
		
		return HvOptional.empty();
	}
	
	/**
	 * Etwaige Fremdwaehrungsdifferenz ermitteln und als "Gegenbuchung"
	 * fuer die Schlussrechnungsgegenbuchung zur Verfuegung stellen.
	 * 
	 * @param zahlungsDetailDtos der Anzahlungsrechnungszahlung
	 * @param debitorKontoId KontoId des Debitors
	 * @param steuerkategorieId der Anzahlungsrechnung (Ermittlung der Kurskonten)
	 * @return eine (leere) Liste der beiden Details auf AnzBez und Debitor
	 */
	private List<BuchungdetailDto> detailsFremdwaehrung(
			BuchungdetailDto[] zahlungsDetailDtos, 
			Integer debitorKontoId, Integer steuerkategorieId) {
		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, steuerkategorieId);
		Validator.entityFound(steuerkategorie, steuerkategorieId);
	
		List<BuchungdetailDto> gegenDetails = new ArrayList<BuchungdetailDto>();
		HvOptional<BuchungdetailDto> detail = findDetail(
				steuerkategorie.getKontoIIdKursverlust(), zahlungsDetailDtos);
		if(detail.isPresent()) {
			// Debitor
			BuchungdetailDto b0 = BuchungdetailDto.haben(
					debitorKontoId,
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b0);
			// AnzBez 
			BuchungdetailDto b1 = BuchungdetailDto.soll(
					zahlungsDetailDtos[1].getKontoIId(), 
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b1);
			return gegenDetails;
		}
		
		detail = findDetail(
				steuerkategorie.getKontoIIdKursgewinn(), zahlungsDetailDtos);
		if(detail.isPresent()) {
			// Debitor
			BuchungdetailDto b0 = BuchungdetailDto.soll(
					debitorKontoId,
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b0);
			// AnzBez 
			BuchungdetailDto b1 = BuchungdetailDto.haben(
					zahlungsDetailDtos[1].getKontoIId(), 
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b1);
			return gegenDetails;
		}
		
		return gegenDetails;
	}
	
	/**
	 * Etwaige Skontodetails ermitteln und als "Gegenbuchung"
	 * fuer die Schlussrechnungsgegenbuchung zur Verfuegung stellen.</br>
	 * <p>Es wird in den Buchungsdetails der Zahlung nach einem
	 * Detail gesucht, dessen Konto dem Skonto-Konto entspricht</p>
	 * 
	 * @param zahlungsDetailDtos der Anzahlungsrechnungszahlung
	 * @param debitorKontoId KontoId des Debitors
	 * @param steuerkategorieId der Anzahlungsrechnung (Ermittlung der Kurskonten)
	 * @return eine (leere) Liste der beiden Details auf AnzBez und Debitor
	 */
	private List<BuchungdetailDto> detailsSkonto(
			BuchungdetailDto[] zahlungsDetailDtos, 
			Integer debitorKontoId, Integer steuerkategorieId,
			Integer mwstsatzId, Timestamp gueltigAm) {
		Mwstsatz mwstsatz = em.find(Mwstsatz.class, mwstsatzId);
		Validator.entityFound(mwstsatz, mwstsatzId);
		
		HvOptional<SteuerkategoriekontoDto> skk = getFinanzServiceFac()
				.steuerkategoriekontoZuDatum(steuerkategorieId,
						mwstsatz.getMwstsatzbezIId(), gueltigAm);
		if(!skk.isPresent()) {
			return new ArrayList<BuchungdetailDto>();
		}
		
		List<BuchungdetailDto> gegenDetails = new ArrayList<BuchungdetailDto>();
		HvOptional<BuchungdetailDto> detail = findDetail(
				skk.get().getKontoIIdSkontoVk(), zahlungsDetailDtos);
		if(detail.isPresent()) {
			// Debitor
			BuchungdetailDto b0 = BuchungdetailDto.haben(
					debitorKontoId,
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b0);
			// AnzBez 
			BuchungdetailDto b1 = BuchungdetailDto.soll(
					zahlungsDetailDtos[1].getKontoIId(), 
					null,
					detail.get().getNBetrag().abs(), BigDecimal.ZERO);
			gegenDetails.add(b1);
			return gegenDetails;
		}
		
		return gegenDetails;
	}
	
	/**
	 * Liefert MwstsatzId der Anzahlungsposition</br>
	 * <p>Sucht die erste Handeingabeposition. Es wird davon
	 * ausgegangen, dass dort ein MwstsatzId hinterlegt ist,
	 * ansonsten eine NullpointerException durch HvOptional.</p>
	 * @param rechnungId
	 * @return HvOptional.empty wenn es keine Handeingabeposition
	 * gibt, ansonsten HvOptional der mwstsatzId
	 * 
	 * @throws RemoteException
	 */
	private HvOptional<Integer> getMwstsatzFromAnzahlungsrechnung(
			Integer rechnungId) throws RemoteException {
		RechnungPositionDto[] posDtos = getRechnungFac()
				.rechnungPositionFindByRechnungIId(rechnungId);
		for(RechnungPositionDto posDto : posDtos) {
			if(posDto.isHandeingabe()) {
				return HvOptional.of(posDto.getMwstsatzIId());
			}
		}
		
		return HvOptional.empty();
	}
	
	
	private void createGegenbuchungAnzahlungsrechnung(
			RechnungDto srDto, RechnungDto azDto, 
			RechnungzahlungDto zahlungDto,
			Integer debitorenKontoId, Integer steuerkategorieId,
			TheClientDto theClientDto) throws RemoteException {
		BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
				LocaleFac.BELEGART_REZAHLUNG,
				zahlungDto.getIId());
		BuchungdetailDto[] zahlungdetailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(
				bbDto.getBuchungIId());				

		// Zahlungsdetails
		// 0 ... Bank -> AnzBez, Brutto, SOLL
		// 1 ... AnzBez -> Bank, Nettobetrag, HABEN
		// 2 ... UST -> Bank, Steuerbetrag, HABEN
        // 3 ... Debitor
		List<BuchungdetailDto> details = new ArrayList<BuchungdetailDto>();
		// Debitor (AnzBetrag + Steuer)
		BigDecimal ust = zahlungDto.getNBetragUst().signum() != 0 
				? zahlungdetailDtos[2].getNBetrag() : BigDecimal.ZERO;
		BuchungdetailDto b0 = BuchungdetailDto.haben(
				debitorenKontoId,
				null,
				zahlungdetailDtos[0].getNBetrag(), ust);
		details.add(b0);
		// AnzBez -> Debitor
		BuchungdetailDto b1 = BuchungdetailDto.soll(
				zahlungdetailDtos[1].getKontoIId(), 
				b0.getKontoIId(),
				zahlungdetailDtos[1].getNBetrag(), BigDecimal.ZERO);
		details.add(b1);
		if(ust.signum() != 0) {
			BuchungdetailDto b2 = BuchungdetailDto.soll(
					zahlungdetailDtos[2].getKontoIId(), 
					b0.getKontoIId(), zahlungdetailDtos[2].getNBetrag(), BigDecimal.ZERO);
			details.add(b2);				
		}

		details.addAll(detailsFremdwaehrung(
				zahlungdetailDtos, debitorenKontoId, steuerkategorieId));
		
		HvOptional<Integer> mwstsatz = 
				getMwstsatzFromAnzahlungsrechnung(azDto.getIId());
		if(mwstsatz.isPresent()) {
			details.addAll(detailsSkonto(
					zahlungdetailDtos, debitorenKontoId, 
					steuerkategorieId, mwstsatz.get(), azDto.getTBelegdatum()));				
		}
				
		BuchungDto buchungDto = new BuchungDto();
		buchungDto.setAutomatischeBuchung(false);
		buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_SCHLUSSRECHNUNG);
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
		buchungDto.setCBelegnummer(srDto.getCNr());
		KundeDto kundeDto = getKundeFac()
				.kundeFindByPrimaryKey(
						srDto.getKundeIId(), theClientDto);
		buchungDto.setCText(kundeDto.getPartnerDto()
				.getCName1nachnamefirmazeile1());

		/*
		 * SP8815 Hintergrund:
		 * Eine Zahlung einer Anzahlung ist istversteuert. Die SR ist sollversteuert.
		 * Die Gegenbuchung muss prinzipiell zum spaeteren der beiden Zeitpunkte
		 * erfolgen (sonst wuerde in einer Periode gebucht, die moeglicherweise 
		 * schon abgeschlossen ist). Zusaetzlich hebt sich damit das Dilemma
		 * AZ Zahlung ist istversteuert, die SR ist sollversteuert auf.
		 * 
		 * Die Zahlung der AZ muss nicht zwangslaeufig in der gleichen 
		 * Periode der SR erfolgen.
		 * 
		 * | AZ | AZ Zahlung | SR |
		 * | AZ | SR | AZ Zahlung |
		 * | AZ | AZ Teilzahlung | SR | SR Zahlung | AZ Zahlung |
		 */
		Date dBelegDatum = Helper.asDate(srDto.getTBelegdatum());
		if (dBelegDatum.after(zahlungDto.getDZahldatum())) {
			buchungDto.setDBuchungsdatum(dBelegDatum);
		} else {
			buchungDto.setDBuchungsdatum(zahlungDto.getDZahldatum());
		}
//		buchungDto.setDBuchungsdatum(new Date(srDto.getTBelegdatum().getTime()));
		buchungDto.setKostenstelleIId(srDto.getKostenstelleIId());
		
		buchungDto = getBuchenFac().buchen(buchungDto,
				details.toArray(new BuchungdetailDto[0]), 
				srDto.getReversechargeartId(), true, theClientDto);

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_SCHLUSSRECHNUNG);
		belegbuchungDto.setIBelegiid(zahlungDto.getIId());
		belegbuchungDto.setBuchungIId(buchungDto.getIId());
		createBelegbuchung(belegbuchungDto, theClientDto);		
	}
	
	
	/**
	 * Die Steuergegenbuchung erzeugen.</br>
	 * 
	 * @param srDto
	 * @param azDto
	 * @param theClientDto
	 * @throws RemoteException
	 */
	private void createGegenbuchungAnzahlungsrechnung(
			RechnungDto srDto, RechnungDto azDto, 
			Integer debitorenKontoId, Integer steuerkategorieId,
			TheClientDto theClientDto) throws RemoteException { 
		if(debitorenKontoId == null) return;
		
		RechnungzahlungDto[] zahlungDtos = getRechnungFac()
				.zahlungFindByRechnungIId(azDto.getIId());

		for (RechnungzahlungDto zahlungDto : zahlungDtos) {
			if(zahlungDto.isGutschrift()) continue;

			createGegenbuchungAnzahlungsrechnung(srDto, azDto, 
					zahlungDto, debitorenKontoId, steuerkategorieId, theClientDto);

			// "Mitlaufendes Buchungspaar erzeugen"
			// Debitor HABEN, AnzahlungVerrBezahlt SOLL
			// Wir stellen damit jenen Betrag den der Debitor per Anzahlung 
			// bezahlt hat ins HABEN, betrachten in also als "bezahlt"
//			BuchungdetailDto bd = BuchungdetailDto.haben(detailDtos[0].getKontoIId(), 
//					null, detailDtos[0].getNBetrag(), detailDtos[0].getNUst());
//			list.add(bd);
			
			
			
			// "Mitlaufendes Buchungspaar erzeugen"
			// Debitor HABEN, AnzahlungVerrBezahlt SOLL
			// Wir stellen damit jenen Betrag den der Debitor per Anzahlung 
			// bezahlt hat ins HABEN, betrachten in also als "bezahlt"
//			BuchungdetailDto bd = BuchungdetailDto.haben(detailDtos[0].getKontoIId(), 
//					null, detailDtos[0].getNBetrag(), detailDtos[0].getNUst());
//			list.add(bd);

		}
	}
	
	private List<BuchungdetailDto> createSchlussrechnungGegenbuchung0(
			RechnungDto rechnungDto, Integer debitorenKontoId, 
			Integer steuerkategorieId, TheClientDto theClientDto) throws RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();

		RechnungDto[] anzRechnungen = getRechnungFac()
				.rechnungFindByAuftragIId(rechnungDto.getAuftragIId());
		for (RechnungDto azDto : anzRechnungen) {
			if(!azDto.isAnzahlungsRechnung() || azDto.getNWertfw() == null) {
				continue;
			}
			
			list.addAll(buildDetailsAusAnzahlungFuerSchlussrechnung1(
					azDto, theClientDto));
		
			// Kursdifferenz berechnen
			// Das Anzahlungsrechnungsdatum auf Schlussrechnungsdatum beziehen
			BigDecimal azWert = azDto.getNWertfw().add(azDto.getNWertustfw())
					.divide(rechnungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal kursDifferenz = Helper.rundeGeldbetrag(
					azDto.getNWert().add(azDto.getNWertust()).subtract(azWert));
			if(kursDifferenz.signum() != 0) {
				boolean kursVerlust = kursDifferenz.signum() > 0; // Wir wandeln Fremdwaehrung in Mandantenwaehrung
				SachkontoId kursKonto = getKursKonto(kursVerlust, steuerkategorieId, rechnungDto);
				
				BuchungdetailDto bd1 = BuchungdetailDto.soll(
						debitorenKontoId, null, kursDifferenz.abs(), BigDecimal.ZERO);
				BuchungdetailDto bd2 = BuchungdetailDto.haben(kursKonto.id(),
						debitorenKontoId, kursDifferenz.abs(), BigDecimal.ZERO);
				if(!kursVerlust) {
					bd1.swapSollHaben();
					bd2.swapSollHaben();
				}
				list.add(bd1);
				list.add(bd2);
			}
		}
		
		return list;
	}


	private List<BuchungdetailDto> buildDetailsAusAnzahlungFuerSchlussrechnung0(
			RechnungDto azDto, TheClientDto theClientDto) throws RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		
		BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
				LocaleFac.BELEGART_RECHNUNG, azDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(belegRech.getBuchungIId());

/*
		boolean bMitUst = azDto.getNWertust().signum() != 0;
		if(bMitUst) {
			BuchungdetailDto anUst = detailDtos[2];
			anUst.swapSollHaben(); 
			list.add(anUst);
		}
*/			
		// "Mitlaufendes Buchungspaar erzeugen"
		// Debitor HABEN, AnzahlungVerrBezahlt SOLL
		// Wir stellen damit jenen Betrag den der Debitor per Anzahlung 
		// bezahlt hat ins HABEN, betrachten in also als "bezahlt"
		BuchungdetailDto bd = BuchungdetailDto.haben(detailDtos[0].getKontoIId(), 
				null, detailDtos[0].getNBetrag(), detailDtos[0].getNUst());
		list.add(bd);

		if(azDto.isBezahlt()) {				
			RechnungzahlungDto[] zahlungDtos = getRechnungFac()
					.zahlungFindByRechnungIId(azDto.getIId());
			for (RechnungzahlungDto zahlungDto : zahlungDtos) {
				if(zahlungDto.isGutschrift()) continue;

				BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_REZAHLUNG,
						zahlungDto.getIId());
				BuchungdetailDto[] zahlungdetailDtos = getBuchenFac()
						.buchungdetailsFindByBuchungIId(
						bbDto.getBuchungIId());

//				HelperServer.printBuchungssatz(detailDtos, getFinanzFac(), System.out);
				
				// zahlungdetailDtos[0].getKontoIIdGegenkonto == AnzahlungBezahlt-Konto
				BuchungdetailDto bdAz = BuchungdetailDto.soll(
						zahlungdetailDtos[0].getKontoIIdGegenkonto(),
						bd.getKontoIId(), bd.getNBetrag(), bd.getNUst());
				list.add(bdAz);
				break;
			}
		} else {
			// "Mitlaufende Buchung"
			// AnzahlungVerrBezahlt SOLL Konto direkt ermitteln
			KontoDto debitorDto = getFinanzFac()
					.kontoFindByPrimaryKey(detailDtos[0].getKontoIId());
			FinanzamtKontenUebersetzung faKu = new FinanzamtKontenUebersetzung(
					debitorDto.getFinanzamtIId(), debitorDto.getMandantCNr());
			SachkontoId anzahlungBez = faKu.getAnzahlungBezahlt(azDto, theClientDto);
			BuchungdetailDto bdAz = BuchungdetailDto.soll(
					anzahlungBez.id(), debitorDto.getIId(), bd.getNBetrag(), bd.getNUst());
			list.add(bdAz);		
		}
			
		return list;
	}
	
	/**
	 * Offene Anzahlungen beim Verbuchen der Schlussrechnung
	 * und beim Teilzahlen einer Anzahlungsrechnung behandeln</br>
	 * 
	 * <p>F&uuml;r (teilbezahlte) Anzahlungen hat bereits 
	 * createSchlussrechnungGegenbuchung alles gemacht.
	 * Es bleibt nun ein Anzahlungsbetrag offen, den wir 
	 * kontentechnisch ausgleichen m&uuml;ssen. Die Schlussrechnung
	 * bestimmt den neuen Rechnungsbetrag, daher sind die
	 * gestellten aber noch nicht bezahlten Anzahlungen zwar 
	 * belegtechnisch "offen", aber die Forderung an den Debitor
	 * ist nur der Betrag der Schlussrechnung (und nicht 
	 * Summe Anzahlungsrechnungsbetraege + Schlussrechnungsbetrag).</p>
	 * 
	 * <p>Das wird damit gel&ouml;st, dass f&uuml;r den Debitor
	 * und das ErhalteneAnzahlungenVerrechnet-Konto der offene 
	 * Betrag als "ausgeglichen" betrachtet wird.</p>
	 * 
	 * <p>Diese Gegenbuchung dient auch als "Marker" f&uuml;r
	 * sp&auml;tere Zahlungen der Anzahlungsrechnung. Da es 
	 * eine aktivierte Schlussrechnung gibt, muss auch diese 
	 * Gegenbuchung immer neu berechnet werden.</p>
	 * 
	 * @param srDto
	 * @param azDto
	 * @param debitorenKontoId
		 * @param theClientDto
	 * @throws RemoteException
	 */
	private void createGegenbuchungOffeneAnzahlungen(
			RechnungDto srDto, RechnungDto azDto, 
			Integer debitorenKontoId, 
			TheClientDto theClientDto) throws RemoteException {
		if(azDto.isBezahlt()) return;
		
		BigDecimal bezahltFw = getRechnungFac()
				.getBereitsBezahltWertVonRechnungFw(azDto.getIId(), null);
		BigDecimal bezahltUstFw = getRechnungFac()
				.getBereitsBezahltWertVonRechnungUstFw(azDto.getIId(), null);
		
		BigDecimal offenFw = azDto.getNWertfw().subtract(bezahltFw);
		BigDecimal offenUstFw = azDto.getNWertustfw().subtract(bezahltUstFw);
		
		// Wir haben ja kein Geld bekommen, deshalb Belegdatumskurs der AZ
		BigDecimal offen = offenFw.add(offenUstFw).divide(azDto.getNKurs(), 
						FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal offenUst = offenUstFw.divide(azDto.getNKurs(), 
				FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
		
		KontoDto debitorenKontoDto = getFinanzFac()
				.kontoFindByPrimaryKey(debitorenKontoId);
		FinanzamtKontenUebersetzung faKu =
				new FinanzamtKontenUebersetzung(
						debitorenKontoDto.getFinanzamtIId(), 
						debitorenKontoDto.getMandantCNr());
		SachkontoId anzahlungVerrKontoId = faKu
				.getAnzahlungVerrechnet(azDto, theClientDto);

		BuchungdetailDto b0 = BuchungdetailDto.haben(
				debitorenKontoId, anzahlungVerrKontoId.id(), offen, offenUst);
		
		BuchungdetailDto b1 = BuchungdetailDto.soll(
				anzahlungVerrKontoId.id(), debitorenKontoId, offen, offenUst);
		
		BuchungDto buchungDto = new BuchungDto();
		buchungDto.setAutomatischeBuchung(false);
		buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_ANZAHLUNGSRECHNUNG);
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
		buchungDto.setCBelegnummer(srDto.getCNr());
		KundeDto kundeDto = getKundeFac()
				.kundeFindByPrimaryKey(
						srDto.getKundeIId(), theClientDto);
		buchungDto.setCText(kundeDto.getPartnerDto()
				.getCName1nachnamefirmazeile1());

		buchungDto.setDBuchungsdatum(new Date(srDto.getTBelegdatum().getTime()));
		buchungDto.setKostenstelleIId(srDto.getKostenstelleIId());
		
		buchungDto = getBuchenFac().buchen(buchungDto,
				new BuchungdetailDto[] {b0, b1},
				srDto.getReversechargeartId(), true, theClientDto);

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_ANZAHLUNGSRECHNUNG);
		belegbuchungDto.setIBelegiid(azDto.getIId());
		belegbuchungDto.setBuchungIId(buchungDto.getIId());
		createBelegbuchung(belegbuchungDto, theClientDto);
	}
	
	/**
	 * Offene Anzahlungen bei der Schlussrechnung behandeln</br>
	 * <p>F&uuml;r (teilbezahlte) Anzahlungen hat bereits 
	 * createSchlussrechnungGegenbuchung alles gemacht.
	 * Es bleibt nun ein Anzahlungsbetrag offen, den wir 
	 * kontentechnisch ausgleichen m&uuml;ssen. Die Schlussrechnung
	 * bestimmt den neuen Rechnungsbetrag, daher sind die
	 * gestellten aber noch nicht bezahlten Anzahlungen zwar 
	 * belegtechnisch "offen", aber die Forderung an den Debitor
	 * ist nur der Betrag der Schlussrechnung (und nicht 
	 * Summe Anzahlungsrechnungsbetraege + Schlussrechnungsbetrag).</p>
	 * 
	 * <p>Das wird damit gel&ouml;st, dass f&uuml;r den Debitor
	 * und das ErhalteneAnzahlungenVerrechnet-Konto der offene 
	 * Betrag als "ausgeglichen" betrachtet wird.</p>
	 * 
	 * @param azDto
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 */
	private List<BuchungdetailDto> detailsSchlussrechnungOffeneAnzahlungen(
			RechnungDto srDto, RechnungDto azDto, Integer debitorenKontoId, 
			Integer steuerkategorieId, TheClientDto theClientDto) throws RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		if(azDto.isBezahlt()) return list;

		BigDecimal bezahltFw = getRechnungFac()
				.getBereitsBezahltWertVonRechnungFw(azDto.getIId(), null);
		BigDecimal bezahltUstFw = getRechnungFac()
				.getBereitsBezahltWertVonRechnungUstFw(azDto.getIId(), null);
		
		BigDecimal offenFw = azDto.getNWertfw().subtract(bezahltFw);
		BigDecimal offenUstFw = azDto.getNWertustfw().subtract(bezahltUstFw);
		
		// Wir haben ja kein Geld bekommen, deshalb Belegdatumskurs der AZ
		BigDecimal offen = offenFw.add(offenUstFw).divide(azDto.getNKurs(), 
						FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal offenUst = offenUstFw.divide(azDto.getNKurs(), 
				FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
		
		KontoDto debitorenKontoDto = getFinanzFac()
				.kontoFindByPrimaryKey(debitorenKontoId);
		FinanzamtKontenUebersetzung faKu =
				new FinanzamtKontenUebersetzung(
						debitorenKontoDto.getFinanzamtIId(), 
						debitorenKontoDto.getMandantCNr());
		SachkontoId anzahlungVerrKontoId = faKu
				.getAnzahlungVerrechnet(azDto, theClientDto);

		BuchungdetailDto b0 = BuchungdetailDto.haben(
				debitorenKontoId, anzahlungVerrKontoId.id(), offen, offenUst);
		list.add(b0);
		
		BuchungdetailDto b1 = BuchungdetailDto.soll(
				anzahlungVerrKontoId.id(), debitorenKontoId, offen, offenUst);
		list.add(b1);
		return list;
	}
	
	private List<BuchungdetailDto> buildDetailsAusAnzahlungFuerSchlussrechnung1(
			RechnungDto azDto, TheClientDto theClientDto) throws RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		
		BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
				LocaleFac.BELEGART_RECHNUNG, azDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(belegRech.getBuchungIId());

		RechnungzahlungDto[] zahlungDtos = getRechnungFac()
				.zahlungFindByRechnungIId(azDto.getIId());
		for (RechnungzahlungDto zahlungDto : zahlungDtos) {
			if(zahlungDto.isGutschrift()) continue;

			BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
					LocaleFac.BELEGART_REZAHLUNG,
					zahlungDto.getIId());
			BuchungdetailDto[] zahlungdetailDtos = getBuchenFac()
					.buchungdetailsFindByBuchungIId(
					bbDto.getBuchungIId());			
		}
				
		return list;
	}
	
	private List<BuchungdetailDto> getDetailsSchlussrechnungAnzahlungen0(
			RechnungDto rechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		RechnungDto[] anzRechnungen = getRechnungFac()
				.rechnungFindByAuftragIId(rechnungDto.getAuftragIId());
		for (int i = 0; i < anzRechnungen.length; i++) {
			if (!anzRechnungen[i].getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_ANZAHLUNG)
					|| anzRechnungen[i].getNWertfw() == null) continue;

			List<BuchungdetailDto> tempList = new ArrayList<BuchungdetailDto>();
			boolean bMitUst = (anzRechnungen[i].getNWertust().signum() != 0);

			BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
					LocaleFac.BELEGART_RECHNUNG, anzRechnungen[i].getIId());

			BuchungdetailDto[] detailDtos = getBuchenFac()
					.buchungdetailsFindByBuchungIId(
							belegRech.getBuchungIId());

//			System.out.println("Anzahlung: " + anzRechnungen[i].getCNr());
//			HelperServer.printBuchungssatz(Arrays.asList(detailDtos), getFinanzFac(), System.out);

			BuchungdetailDto perDebitor = detailDtos[0];
			perDebitor.setNBetrag(perDebitor.getNBetrag().negate());
			tempList.add(perDebitor);
			if(bMitUst) {
				BuchungdetailDto anUst = detailDtos[2];
				anUst.setNBetrag(anUst.getNBetrag().negate());
				// TODO: ghp, muss ja von haben nach soll transponiert werden, weil 
				// die USt. (temporaer) zurueckgenommen werden soll
				// anUst.swapSollHaben(); 
				tempList.add(anUst);
			}

			if(RechnungFac.STATUS_BEZAHLT.equals(anzRechnungen[i].getStatusCNr())) {
				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
						.zahlungFindByRechnungIId(anzRechnungen[i].getIId());
				Integer kontoAnzBezahlt = null;
				for (int j = 0; j < zahlungDtos.length; j++) {
					if(RechnungFac.ZAHLUNGSART_GUTSCHRIFT.equals(zahlungDtos[j].getZahlungsartCNr()))
						continue;
					BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
									LocaleFac.BELEGART_REZAHLUNG,
									zahlungDtos[j].getIId());
					BuchungdetailDto[] zahlungdetailDtos = getBuchenFac()
							.buchungdetailsFindByBuchungIId(
									bbDto.getBuchungIId());
//					HelperServer.printBuchungssatz(detailDtos, getFinanzFac(), System.out);
					
					BuchungdetailAnalyzer analyzer = 
							new BuchungdetailAnalyzer(anzRechnungen[i].getTBelegdatum(),
									detailDtos, zahlungdetailDtos, theClientDto);
					BuchungdetailAnzahlungVisitor visitor = 
							new BuchungdetailAnzahlungVisitor();
					analyzer.evalAnzahlung(visitor);
					List<BuchungdetailDto> anzDetails = visitor.getDetails();
					anzDetails.get(0).setKontoIIdGegenkonto(perDebitor.getKontoIId());
					tempList.addAll(anzDetails);
					kontoAnzBezahlt = visitor.getKontoAnzBezahltId();
	
					if(1 == 0) {
						boolean skonto = false;
						boolean hasKursGewinnVerlust = false;
				
						if(detailDtos.length > 5) {
							if(detailDtos.length > (bMitUst ? 9 : 8)) {
								hasKursGewinnVerlust = true;
								skonto = true;
							} else {
								hasKursGewinnVerlust = isKursGewinnVerlustKonto(perDebitor.getKontoIId(), 
										detailDtos[2].getKontoIId(), theClientDto);							
							}
						}

						int anzahlungIndexDiff = (skonto||hasKursGewinnVerlust) ? 3 : 2;
						if(hasKursGewinnVerlust) {
							anzahlungIndexDiff++;
						}
//						BuchungdetailDto perAnzBezahlt = detailDtos[detailDtos.length-(skonto?3:2)];
						BuchungdetailDto perAnzBezahlt = 
								detailDtos[detailDtos.length - anzahlungIndexDiff];

						//dritte von hinten ist Buchung an erhaltene Anzahlungen bezahlt
						perAnzBezahlt.swapSollHaben();
						perAnzBezahlt.setKontoIIdGegenkonto(null);
						tempList.add(perAnzBezahlt);

						boolean skonto0 = false;
						if(skonto0) {
							BuchungdetailDto perAnzVerrech = detailDtos[detailDtos.length-4];
							perAnzVerrech.setNBetrag(detailDtos[2].getNBetrag().negate());
							perAnzVerrech.setKontoIIdGegenkonto(null);
							kontoAnzBezahlt = perAnzVerrech.getKontoIId();
							//vierte von hinten ist Buchung per erhaltene Anzahlungen verrechnet
							tempList.add(perAnzVerrech);
						}
						
						if(skonto || hasKursGewinnVerlust) {
							/*
							 * Details in der Reihenfolge (Mit Ust, Skonto)
							 * 0...Bank->Debitor
							 * 1...Zahlbetrag
							 * 2...Skonto Betrag Netto
							 * 3...Skonto Debitor
							 * 4...Skonto Ust Betrag
							 * 5...Anzahlung bezahlt
							 * 6...Anzahlung bezahlt Gegenbuchung
							 * 7...Forderungen Debitor Zahlung
							 * 8...Forderungen Debitor Skonto
							 */
							
			
							/*
							 * Details in der Reihenfolge (Fremdwaehrung)
							 * 0...Bank->Debitor
							 * 1...Zahlbetrag
							 * 2...Fremdwaehrung Differenz
							 * 3...Fremdwaehrung Debitor
							 * 4...Anzahlung bezahlt
							 * 5...Anzahlung bezahlt Gegenbuchung
							 * 6...Forderungen Debitor Zahlung
							 * 7...Forderungen Debitor Fremdwaehrung
							 */
				
							/*
							 * Details in der Reihenfolge (Skonto + Fremdwaehrung)
							 *  0...Bank->Debitor
							 *  1...Zahlbetrag
							 *  2...Skonto Betrag Netto
							 *  3...Skonto Debitor
							 *  4...Fremdwaehrung Differenz
							 *  5...Fremdwaehrung Debitor
							 *  6...Anzahlung bezahlt
							 *  7...Anzahlung bezahlt Gegenbuchung
							 *  8...Forderungen Debitor Zahlung
							 *  9...Forderungen Debitor Skonto
							 * 10...Forderungen Debitor Fremdwaehrung
							 */
							int indexBetrag = detailDtos.length-1;
							BuchungdetailDto kursVerlust = detailDtos[indexBetrag];
//							boolean isKursGewinnVerlust = isKursGewinnVerlustKonto(perDebitor.getKontoIId(),
//									kursVerlust.getKontoIIdGegenkonto(), theClientDto);
							if(hasKursGewinnVerlust) {
								BuchungdetailDto perKursverlust = new BuchungdetailDto(true, 
										perAnzBezahlt.getKontoIId(), null,
										kursVerlust.getNBetrag(), kursVerlust.getNUst());
								tempList.add(perKursverlust);
								indexBetrag--;
							}
							
							if(skonto) {
								// vierte von hinten ist Buchung per erhaltene Anzahlungen verrechnet
								// BuchungdetailDto perAnzVerrech = detailDtos[indexBetrag-3];
								BigDecimal betrag = detailDtos[2].getNBetrag().negate();
								BuchungdetailDto perSkonto = new BuchungdetailDto(true, perAnzBezahlt.getKontoIId(), null, betrag, BigDecimal.ZERO);
								kontoAnzBezahlt = perSkonto.getKontoIId();
								tempList.add(perSkonto);
							}
						}						
					}
				}
				korrigiereRundungsUngenauigkeit(tempList, kontoAnzBezahlt);
			} else {
//				HelperServer.printBuchungssatz(detailDtos, getFinanzFac()) ;
				RechnungzahlungDto zwischenZahlung = new RechnungzahlungDto(); //Die Zahlung gibts noch nicht wirklich,
				zwischenZahlung.setNBetrag(anzRechnungen[i].getNWert());// aber wir brauchen sie um die Buchungsdetails zu generieren
				zwischenZahlung.setNBetragUst(anzRechnungen[i].getNWertust());
				BuchungdetailDto[] details = new BuchungdetailDto[]{new BuchungdetailDto()};
				details[0].setKontoIIdGegenkonto(perDebitor.getKontoIId());
				details = addAnzahlungZahlungDetails(details, rechnungDto, zwischenZahlung, theClientDto);
//				BuchungdetailDto perAnzBezahlt = details[detailDtos.length-(2)];
//				//dritte von hinten ist Buchung an erhaltene Anzahlungen bezahlt
				BuchungdetailDto perAnzBezahlt = details[2];
				perAnzBezahlt.swapSollHaben();
				perAnzBezahlt.setKontoIIdGegenkonto(null);
				tempList.add(perAnzBezahlt);
			}
			list.addAll(tempList);
		}
		for(BuchungdetailDto dto : list) {
			dto.setIId(null);
			dto.setBuchungIId(null);
			dto.setIAusziffern(null);
			dto.setIAuszug(null);
		}

//		System.out.println("Alle: ");
//		HelperServer.printBuchungssatz(list, getFinanzFac(), System.out);
//		return list.toArray(new BuchungdetailDto[list.size()]);
		return list ;
	}

	/**
	 * Ermittlung, ob das betreffende kontoId ein Kursgewinn- oder verlustkonto ist.
	 * 
	 * Ja, diese Methode ist nicht vollstaendig sauber, da in allen Steuerkategorien
	 * gesucht wird, die fuer dieses Finanzamt hinterlegt sind.
	 */
	private boolean isKursGewinnVerlustKonto(Integer personenKontoId, 
			Integer kontoId, TheClientDto theClientDto) throws RemoteException {
		KontoDto debitorDto = getFinanzFac().kontoFindByPrimaryKey(personenKontoId);
		if(debitorDto.getFinanzamtIId() == null) return false;
		
		SteuerkategorieDto[] dtos = getFinanzServiceFac()
				.steuerkategorieFindByFinanzamtIId(debitorDto.getFinanzamtIId(), theClientDto);
		for (SteuerkategorieDto dto : dtos) {
			if(kontoId.equals(dto.getKontoIIdKursgewinn())) return true;
			if(kontoId.equals(dto.getKontoIIdKursverlust())) return true;
		}
		
		return false;
	}
	
	protected boolean korrigiereRundungsUngenauigkeit(List<BuchungdetailDto> list, Integer kontoIId) {
		BigDecimal saldo = getBuchungSaldo(list);
		if(saldo.signum() == 0) return true;
		if(saldo.abs().compareTo(new BigDecimal("0.01"))>0) return false;

		for(BuchungdetailDto dto : list) {
			if(!dto.getKontoIId().equals(kontoIId)) continue;
			if(dto.getBuchungdetailartCNr().equals(
						BuchenFac.HabenBuchung)) {
				saldo = saldo.negate();
			}
			dto.setNBetrag(dto.getNBetrag().add(saldo));
			return true;
		}
		return false;
	}

	private BigDecimal getBuchungSaldo(List<BuchungdetailDto> list) {
		BigDecimal saldo = BigDecimal.ZERO;
		for(BuchungdetailDto dto : list) {
			if(dto.getBuchungdetailartCNr().equals(
					BuchenFac.HabenBuchung)) {
				saldo = saldo.add(dto.getNBetrag());
			} else {
				saldo = saldo.subtract(dto.getNBetrag());
			}
		}
		return saldo;
	}

	private BuchungdetailDto getSteuerBuchungAnzahlungsrechnung(
			RechnungDto rechnungDto, BuchungdetailDto[] zahlungdetailDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// Anzahlungsrechnung hat nur EINE preistragende Position, daher ist die
		// Position fix
		BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_RECHNUNG, rechnungDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bbDto.getBuchungIId());

		// Steuerbuchung ist die vorletzte Buchung
		BuchungdetailDto steuerbuchungDto = detailDtos[detailDtos.length - 2];
		Integer steuerkontoIId = steuerbuchungDto.getKontoIId();

		// Steuerbetrag um anteilige Steuer aus Skonti korrigieren, falls welche
		// vorhanden
		for (int i = 0; i < zahlungdetailDtos.length; i++)
			if (zahlungdetailDtos[i].getKontoIId().equals(steuerkontoIId))
				if (zahlungdetailDtos[i].isHabenBuchung())
					steuerbuchungDto.setNBetrag(steuerbuchungDto.getNBetrag()
							.add(zahlungdetailDtos[i].getNBetrag()));

		return steuerbuchungDto;
	}

/*
		RechnungartDto rechnungartDto = getRechnungServiceFac()
				.rechnungartFindByPrimaryKey(
						rechnungDto.getRechnungartCNr(), theClientDto);
		String rechnungtyp = rechnungartDto.getRechnungtypCNr();
		String buchungBelegart = null ;
		String belegbuchungBelegart = null ;
		BelegbuchungDto bbDto = null;
		try {
			if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
	
 */
	private class CacheRechnungart extends HvCreatingCachingProvider<String, RechnungartDto> {
		private final TheClientDto theClientDto;
		
		public CacheRechnungart(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}
		
		@Override
		protected RechnungartDto provideValue(String key, String transformedKey) {
			try {
				RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(key, theClientDto);
				return rechnungartDto;
			} catch (RemoteException e) {
				myLogger.error("provideValue remoteEx", e);
				throw EJBExcFactory.respectOld(e);
			}
		}
	}
	
	private List<BucheBelegPeriodeInfoDto> verbucheRechnungenPeriode(Integer geschaeftsjahr, int periode, boolean alleNeu,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<BucheBelegPeriodeInfoDto> infos = new ArrayList<BucheBelegPeriodeInfoDto>();
		
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = getRechnungFac().rechnungFindByBelegdatumVonBis(theClientDto.getMandant(), dBeginn,
				dEnd);
		CacheRechnungart cacheRechnungart = new CacheRechnungart(theClientDto);
		
		for (RechnungDto rechnungDto : rechnungen) {
			if (!rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)
					&& !rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
				
				RechnungartDto reartDto = cacheRechnungart
						.getValueOfKey(rechnungDto.getRechnungartCNr());
				
				BelegbuchungDto bbDto = null;
//				if (rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_RECHNUNG)) {
				if (RechnungFac.RECHNUNGTYP_RECHNUNG.equals(reartDto.getRechnungtypCNr())) {
					if (rechnungDto.isAnzahlungsRechnung() || rechnungDto.isSchlussRechnung()) {
						infos.add(new BucheBelegPeriodeInfoDto(rechnungDto));
					}
					
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_RECHNUNG,
									rechnungDto.getIId());
//				} else if (rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
				} else if (RechnungFac.RECHNUNGTYP_GUTSCHRIFT.equals(reartDto.getRechnungtypCNr())) {
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_GUTSCHRIFT,
									rechnungDto.getIId());
				} else {
					continue;
				}
				
				if ((bbDto != null) && alleNeu) {
					verbucheRechnungGutschriftRueckgaengig(rechnungDto.getIId(),
//							rechnungDto.getRechnungartCNr(), theClientDto);
							reartDto.getRechnungtypCNr(), theClientDto);
					verbucheRechnung(rechnungDto.getIId(), theClientDto);
				} else if (bbDto == null) {
					// dann verbuchen
					verbucheRechnung(rechnungDto.getIId(), theClientDto);
				}
			}
		}
		
		return infos;
	}

	private void verbucheEingangsrechnungenPeriode(Integer geschaeftsjahr,
			int periode, boolean alleNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = getEingangsrechnungFac()
				.eingangsrechnungFindByMandantCNrDatumVonBis(
						theClientDto.getMandant(), dBeginn, dEnd);
		for (int i = 0; i < rechnungen.length; i++) {
			if (!rechnungen[i].getStatusCNr().equals(
					EingangsrechnungFac.STATUS_STORNIERT)) {
				BelegbuchungDto bbDto = getBelegbuchungFac(
						theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								rechnungen[i].getIId());
				if ((bbDto != null) && alleNeu) {
					verbucheEingangsrechnungRueckgaengig(
							rechnungen[i].getIId(), theClientDto);
					verbucheEingangsrechnung(rechnungen[i].getIId(),
							theClientDto);
				} else if (bbDto == null) {
					// dann verbuchen
					verbucheEingangsrechnung(rechnungen[i].getIId(),
							theClientDto);
				}
			}
		}
	}

	private void verbucheZahlungenPeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dVon = new Date(tVonBis[0].getTime());
		Date dBis = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIds = getRechnungFac()
				.rechnungzahlungIdsByMandantZahldatumVonBis(
						theClientDto.getMandant(), dVon, dBis);
		Iterator<Integer> it = zahlungenIds.iterator();
		while (it.hasNext()) {
			Integer iId = it.next();
			BelegbuchungDto bbDto = getBelegbuchungFac(
					theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(
							LocaleFac.BELEGART_REZAHLUNG, iId);
			if ((bbDto != null) && alleNeu) {
				verbucheZahlungRueckgaengig(iId, theClientDto);
				verbucheZahlung(iId, theClientDto);
			} else if (bbDto == null) {
				// dann verbuchen
				verbucheZahlung(iId, theClientDto);
			}
		}
	}

	private void verbucheZahlungenErPeriode(Integer geschaeftsjahr,
			int periode, boolean alleNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dVon = new Date(tVonBis[0].getTime());
		Date dBis = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIds = getEingangsrechnungFac()
				.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
						theClientDto.getMandant(), dVon, dBis);
		Iterator<Integer> it = zahlungenIds.iterator();
		while (it.hasNext()) {
			Integer iId = it.next();
			BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, iId);
			if ((bbDto != null) && alleNeu) {
				verbucheZahlungErRueckgaengig(iId, theClientDto);
				bbDto = null; // damit neu verbucht wird!
			}
			if (bbDto == null) {
				Eingangsrechnungzahlung ez = em.find(
						Eingangsrechnungzahlung.class, iId);
				if (!(ez.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_GUTSCHRIFT) || ez.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG))) {
					Eingangsrechnung er = em.find(Eingangsrechnung.class,
							ez.getEingangsrechnungIId());
					boolean verbuchen = false;
					// nur buchen wenn Rechnung vollstaendig kontiert
					if (er.getKontoIId() != null) {
						verbuchen = true;
					} else {
						try {
							BigDecimal bdOffen = getEingangsrechnungFac()
									.getWertNochNichtKontiert(
											ez.getEingangsrechnungIId());
							if (bdOffen.doubleValue() == 0)
								verbuchen = true;
						} catch (EJBExceptionLP e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					if (verbuchen)
						verbucheZahlungEr(iId, theClientDto);
				}
			}
		}
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2400)
	@Override
	public List<BucheBelegPeriodeInfoDto> verbucheBelegePeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		List<BucheBelegPeriodeInfoDto> infos = new ArrayList<BucheBelegPeriodeInfoDto>();
		infos.addAll(verbucheRechnungenPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto));
		verbucheZahlungenPeriode(geschaeftsjahr, periode, alleNeu, theClientDto);
		verbucheEingangsrechnungenPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
		verbucheZahlungenErPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
		
		return infos;
	}


	protected List<BuchungdetailDto> getBuchungdetailsVonExportDtos(
			FibuexportDto[] exportDaten, Integer steuerkategorieIID,
			Integer debitorenKontoId, Integer debitorenKontoUebersteuertId,
			boolean bVerkauf, boolean bIgErwerb, boolean reverseCharge,
			TheClientDto theClientDto) {

		List<BuchungdetailDto> al = new ArrayList<BuchungdetailDto>();

		for (int i = 0; i < exportDaten.length; i++) {
			BuchungdetailDto detail = new BuchungdetailDto();
			detail.setKontoIId(exportDaten[i].getKontoDto().getIId());
			if(bVerkauf && detail.getKontoIId().equals(debitorenKontoId) && debitorenKontoUebersteuertId != null) {
				detail.setKontoIId(debitorenKontoUebersteuertId);
			}

			if (exportDaten[i].getSollbetragBD() != null) {
				detail.beSollBuchung();
				detail.setNBetrag(exportDaten[i].getSollbetragBD());
			} else {
				detail.beHabenBuchung();
				detail.setNBetrag(exportDaten[i].getHabenbetragBD());
			}

			detail.setNUst(exportDaten[i].getSteuerBD() != null
					? exportDaten[i].getSteuerBD() : BigDecimal.ZERO);

			if (exportDaten[i].getGegenkontoDto() != null) {
				detail.setKontoIIdGegenkonto(exportDaten[i].getGegenkontoDto()
						.getIId());
				if(bVerkauf && detail.getKontoIIdGegenkonto().equals(debitorenKontoId) && debitorenKontoUebersteuertId != null) {
					detail.setKontoIIdGegenkonto(debitorenKontoUebersteuertId);
				}
			}

			detail.setCKommentar(exportDaten[i].getCKommentar());

			al.add(detail);

			if (exportDaten[i].getSteuerBD() != null) {
				if (exportDaten[i].getSteuerBD().signum() != 0) {
					BuchungdetailDto detail0 = new BuchungdetailDto();
					Integer mwstsatzbezIId = null;
					if (exportDaten[i].getMwstsatz() == null) {
						// Handsteuer
						MwstsatzbezDto[] mwstDtos = getMandantFac()
								.mwstsatzbezFindAllByMandantAsDto(
										theClientDto.getMandant(), theClientDto);
						for (int m = 0; m < mwstDtos.length; m++) {
							if (mwstDtos[m].getBHandeingabe()) {
								mwstsatzbezIId = mwstDtos[m].getIId();
								break;
							}
						}
					} else {
						mwstsatzbezIId = exportDaten[i].getMwstsatz()
								.getIIMwstsatzbezId();
					}

					HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
							steuerkategorieIID, mwstsatzbezIId, 
							Helper.asTimestamp(exportDaten[i].getBelegdatum()));
					if (stkk.isPresent()) {
						detail0.setKontoIId(
								bVerkauf ? stkk.get().getKontoIIdVk() 
										 : stkk.get().getKontoIIdEk()) ;
					} else {
						// alte Variante nutzen
						detail0.setKontoIId(exportDaten[i].getKontoDto()
								.getKontoIIdWeiterfuehrendUst());
					}
					if (detail0.getKontoIId() == null) {
						SteuerkategorieDto stkDto = getFinanzServiceFac()
								.steuerkategorieFindByPrimaryKey(steuerkategorieIID, theClientDto);
						throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(
								stkDto, mwstsatzbezIId);
					}

					detail0.beSollBuchung(exportDaten[i].getSollbetragBD() != null); 
					detail0.setNBetrag(exportDaten[i].getSteuerBD());
					detail0.setNUst(BigDecimal.ZERO);
					detail0.setKontoIIdGegenkonto(exportDaten[i]
							.getGegenkontoDto().getIId());
					if(bVerkauf && detail0.getKontoIIdGegenkonto().equals(debitorenKontoId) && debitorenKontoUebersteuertId != null) {
						detail0.setKontoIIdGegenkonto(debitorenKontoUebersteuertId);
					}
					detail0.setCKommentar(exportDaten[i].getCKommentar());

					if (bIgErwerb || reverseCharge) {
						// EinfuhrUst buchen
						BuchungdetailDto detail1 = new BuchungdetailDto();
						if(stkk.isPresent()) {
							detail1.setKontoIId(stkk.get().getKontoIIdEinfuhrUst());							
						}
						if (detail1.getKontoIId() == null) {
							Steuerkategorie steuerkategorie = em.find(
									Steuerkategorie.class, steuerkategorieIID);
							Mwstsatzbez mwstsatzbez = em.find(
									Mwstsatzbez.class, mwstsatzbezIId);
							String error = exportDaten[i].getBelegart() + ": "
									+ exportDaten[i].getBelegnummer() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCBez() + ", "
									+ "Steuersatz: "
									+ mwstsatzbez.getCBezeichnung();
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT,
									error, error);
						}
						detail1.setNBetrag(detail0.getNBetrag());
						detail1.setNUst(detail0.getNUst());
						detail1.setKontoIIdGegenkonto(detail0.getKontoIId());
						detail1.setCKommentar(exportDaten[i].getCKommentar());

						detail1.beSollBuchung(!detail0.isSollBuchung());
//						if (detail0.isHabenBuchung()) {
//							detail1.beSollBuchung() ;
//						} else {
//							detail1.beHabenBuchung();
//						}
						detail0.setKontoIIdGegenkonto(detail1.getKontoIId());
						detail0.setCKommentar(exportDaten[i].getCKommentar());

						al.add(detail0);
						al.add(detail1);
					} else {
						al.add(detail0);
					}
				}
			}
		}

		return al ;
	}

	private Integer getReversechargeartIdFrom(FibuexportDto exportDto, EingangsrechnungDto erDto, Integer rcartOhneId) {
		Integer reversechargeartId = exportDto.getReversechargeartId() ; 
		if(erDto.getKontoIId() != null || Helper.short2boolean(erDto.getBMitpositionen())) {
			reversechargeartId = erDto.getReversechargeartId() ;
		} else {
			if(reversechargeartId == null) {
				reversechargeartId = rcartOhneId ;
			}
		}
		return reversechargeartId ;
	}
	
	protected List<BuchungdetailDto> getBuchungdetailsVonERExportDtos(
			FibuexportDto[] exportDaten, EingangsrechnungDto erDto,
			String steuerkategorieCnr, Integer finanzamtId, TheClientDto theClientDto) throws RemoteException {

		ReversechargeartDto rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto) ;
		List<BuchungdetailDto> al = new ArrayList<BuchungdetailDto>();
		
		for (int i = 0; i < exportDaten.length; i++) {
			BuchungdetailDto detail = new BuchungdetailDto();
			detail.setKontoIId(exportDaten[i].getKontoDto().getIId());

			if (exportDaten[i].getSollbetragBD() != null) {
				detail.beSollBuchung();
				detail.setNBetrag(exportDaten[i].getSollbetragBD());
			} else {
				detail.beHabenBuchung();
				detail.setNBetrag(exportDaten[i].getHabenbetragBD());
			}

			detail.setNUst(exportDaten[i].getSteuerBD() != null
					? exportDaten[i].getSteuerBD() : BigDecimal.ZERO);

			if (exportDaten[i].getGegenkontoDto() != null) {
				detail.setKontoIIdGegenkonto(exportDaten[i].getGegenkontoDto()
						.getIId());
			}

			detail.setCKommentar(exportDaten[i].getCKommentar());

			al.add(detail);

			if (exportDaten[i].getSteuerBD() != null) {
				if (exportDaten[i].getSteuerBD().signum() != 0) {
					BuchungdetailDto detail0 = new BuchungdetailDto();
					Integer mwstsatzbezIId = null;
					if (exportDaten[i].getMwstsatz() == null) {
						// Handsteuer
						MwstsatzbezDto[] mwstDtos = getMandantFac()
								.mwstsatzbezFindAllByMandantAsDto(
										theClientDto.getMandant(), theClientDto);
						for (int m = 0; m < mwstDtos.length; m++) {
							if (mwstDtos[m].getBHandeingabe()) {
								mwstsatzbezIId = mwstDtos[m].getIId();
								break;
							}
						}
					} else {
						mwstsatzbezIId = exportDaten[i].getMwstsatz()
								.getIIMwstsatzbezId();
					}
					
//					Integer reversechargeartId = exportDaten[i].getReversechargeartId() ; 
//					if(erDto.getKontoIId() != null || Helper.short2boolean(erDto.getBMitpositionen())) {
//						reversechargeartId = erDto.getReversechargeartId() ;
//					} else {
//						if(reversechargeartId == null) {
//							reversechargeartId = rcartOhneDto.getIId() ;
//						}
//					}
					Integer reversechargeartId = getReversechargeartIdFrom(exportDaten[i], erDto, rcartOhneDto.getIId()) ; 
					boolean isReversecharge = !rcartOhneDto.getIId().equals(reversechargeartId) ;
					SteuerkategorieDto stkDto =  getFinanzServiceFac()
							.steuerkategorieFindByCNrFinanzamtIId(
									steuerkategorieCnr, reversechargeartId, finanzamtId, theClientDto) ;
					if(stkDto == null) {
						throw EJBExcFactory.steuerkategorieFehlt(erDto, finanzamtId, reversechargeartId, steuerkategorieCnr) ;
//						Reversechargeart reversechargeart = em.find(
//								Reversechargeart.class, reversechargeartId) ;
//						throw new EJBExceptionLP(
//								EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
//								"Keine Steuerkategorie definiert. " 
//										+ exportDaten[i].getBelegart() + ": "
//										+ exportDaten[i].getBelegnummer()
//										+ ", Reversechargeart: " + reversechargeart.getCNr() 
//										+ ", Steuerkategorie: " + steuerkategorieCnr) ;
					}
					HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
							stkDto.getIId(), mwstsatzbezIId, Helper.asTimestamp(erDto.getDBelegdatum()));
					if(!stkk.isPresent() && getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
						// In Abstimmung mit WH, ghp 26.02.2016
						throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(erDto, stkDto, mwstsatzbezIId) ;
					}
					
					if (stkk.isPresent()) {
						detail0.setKontoIId(stkk.get().getKontoIIdEk()) ;
//					} else {
//						// alte Variante nutzen
//						detail0.setKontoIId(exportDaten[i].getKontoDto()
//								.getKontoIIdWeiterfuehrendUst());
					}
					
					if (detail0.getKontoIId() == null) {
						SteuerkategoriekontoDto stkkDto = 
								SteuerkategoriekontoDtoAssembler.createDto(stkk.get()) ;
						throw EJBExcFactory.steuerkategorieKontoFehlt(
								EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_EK_DEFINIERT, erDto, reversechargeartId, stkkDto) ;
					}

					detail0.beSollBuchung(exportDaten[i].getSollbetragBD() != null); 
					detail0.setNBetrag(exportDaten[i].getSteuerBD());
					detail0.setNUst(BigDecimal.ZERO);
					detail0.setKontoIIdGegenkonto(exportDaten[i]
							.getGegenkontoDto().getIId());
					detail0.setCKommentar(exportDaten[i].getCKommentar());

					if (Helper.short2boolean(erDto.getBIgErwerb()) || isReversecharge) {
						// EinfuhrUst buchen
						BuchungdetailDto detail1 = new BuchungdetailDto();
						detail1.setKontoIId(stkk.get().getKontoIIdEinfuhrUst());
						if (detail1.getKontoIId() == null) {
							SteuerkategoriekontoDto stkkDto = 
									SteuerkategoriekontoDtoAssembler.createDto(stkk.get()) ;
							throw EJBExcFactory.steuerkategorieKontoFehlt(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT, erDto, reversechargeartId, stkkDto) ;
						}
						detail1.setNBetrag(detail0.getNBetrag());
						detail1.setNUst(detail0.getNUst());
						detail1.setKontoIIdGegenkonto(detail0.getKontoIId());
						detail1.setCKommentar(exportDaten[i].getCKommentar());

						detail1.beSollBuchung(!detail0.isSollBuchung());
						detail0.setKontoIIdGegenkonto(detail1.getKontoIId());
						detail0.setCKommentar(exportDaten[i].getCKommentar());

						al.add(detail0);
						al.add(detail1);
					} else {
						al.add(detail0);
					}
				}
			}
		}

		return al ;
	}
	
/*
	protected BuchungdetailDto[] getBuchungdetailsVonExportDtos(
			FibuexportDto[] exportDaten, Integer steuerkategorieIID,
			boolean bVerkauf, boolean bIgErwerb, boolean reverseCharge,
			TheClientDto theClientDto) {

		ArrayList<BuchungdetailDto> al = new ArrayList<BuchungdetailDto>();

		for (int i = 0; i < exportDaten.length; i++) {
			BuchungdetailDto detail = new BuchungdetailDto();
			detail.setKontoIId(exportDaten[i].getKontoDto().getIId());
			if (exportDaten[i].getSollbetragBD() != null) {
				detail.setBuchungdetailartCNr(BuchenFac.SollBuchung);
				detail.setNBetrag(exportDaten[i].getSollbetragBD());
			} else {
				detail.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				detail.setNBetrag(exportDaten[i].getHabenbetragBD());
			}
			if (exportDaten[i].getSteuerBD() != null) {
				detail.setNUst(exportDaten[i].getSteuerBD());
			} else {
				detail.setNUst(new BigDecimal(0));
			}

			if (exportDaten[i].getGegenkontoDto() != null)
				detail.setKontoIIdGegenkonto(exportDaten[i].getGegenkontoDto()
						.getIId());

			detail.setKommentar(exportDaten[i].getKommentar());

			al.add(detail);
			if (exportDaten[i].getSteuerBD() != null) {
				if (exportDaten[i].getSteuerBD().doubleValue() != 0) {
					detail = new BuchungdetailDto();
					Integer mwstsatzbezIId = null;
					if (exportDaten[i].getMwstsatz() == null) {
						// Handsteuer
						MwstsatzbezDto[] mwstDtos = getMandantFac()
								.mwstsatzbezFindAllByMandantAsDto(
										theClientDto.getMandant(), theClientDto);
						for (int m = 0; m < mwstDtos.length; m++) {
							if (mwstDtos[m].getBHandeingabe()) {
								mwstsatzbezIId = mwstDtos[m].getIId();
								break;
							}
						}
					} else {
						mwstsatzbezIId = exportDaten[i].getMwstsatz()
								.getIIMwstsatzbezId();
					}

					Steuerkategoriekonto stkk = getSteuerkategoriekonto(
							steuerkategorieIID, mwstsatzbezIId);
					if (stkk != null) {
						if (bVerkauf)
							detail.setKontoIId(stkk.getKontoIIdVk());
						else
							detail.setKontoIId(stkk.getKontoIIdEk());
					} else {
						// alte Variante nutzen
						detail.setKontoIId(exportDaten[i].getKontoDto()
								.getKontoIIdWeiterfuehrendUst());
					}
					if (detail.getKontoIId() == null) {
						Steuerkategorie steuerkategorie = em.find(
								Steuerkategorie.class, steuerkategorieIID);
						Mwstsatzbez mwstsatzbez = em.find(Mwstsatzbez.class,
								mwstsatzbezIId);
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
								"Kein Erl\u00F6skonto definiert. "
										+ exportDaten[i].getBelegart() + ": "
										+ exportDaten[i].getBelegnummer()
										+ ", " + "Steuerkategorie: "
										+ steuerkategorie.getCBez() + ", "
										+ "Steuersatz: "
										+ mwstsatzbez.getCBezeichnung());
					}

					if (exportDaten[i].getSollbetragBD() != null) {
						detail.setBuchungdetailartCNr(BuchenFac.SollBuchung);
					} else {
						detail.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					}
					detail.setNBetrag(exportDaten[i].getSteuerBD());
					detail.setNUst(new BigDecimal(0));
					detail.setKontoIIdGegenkonto(exportDaten[i]
							.getGegenkontoDto().getIId());

					detail.setKommentar(exportDaten[i].getKommentar());

					if (bIgErwerb || reverseCharge) {
						// EinfuhrUst buchen
						BuchungdetailDto detail1 = new BuchungdetailDto();
						detail1.setKontoIId(stkk.getKontoIIdEinfuhrUst());
						if (detail1.getKontoIId() == null) {
							Steuerkategorie steuerkategorie = em.find(
									Steuerkategorie.class, steuerkategorieIID);
							Mwstsatzbez mwstsatzbez = em.find(
									Mwstsatzbez.class, mwstsatzbezIId);
							String error = exportDaten[i].getBelegart() + ": "
									+ exportDaten[i].getBelegnummer() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCBez() + ", "
									+ "Steuersatz: "
									+ mwstsatzbez.getCBezeichnung();
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT,
									error, error);
						}
						detail1.setNBetrag(detail.getNBetrag());
						detail1.setNUst(detail.getNUst());
						detail1.setKontoIIdGegenkonto(detail.getKontoIId());

						detail1.setKommentar(exportDaten[i].getKommentar());

						if (detail.getBuchungdetailartCNr().equals(
								BuchenFac.HabenBuchung)) {
							detail1.setBuchungdetailartCNr(BuchenFac.SollBuchung);
						} else {
							detail1.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
						}
						detail.setKontoIIdGegenkonto(detail1.getKontoIId());

						detail.setKommentar(exportDaten[i].getKommentar());

						al.add(detail);
						al.add(detail1);
					} else {
						al.add(detail);
					}
				}
			}
		}
		BuchungdetailDto[] details = new BuchungdetailDto[al.size()];
		for (int i = 0; i < al.size(); i++) {
			details[i] = al.get(i);
		}
		return details;
	}
*/

	// TODO: gemeinsame Methode!! FibuExportFacBean
	protected String getExportVariante(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	// TODO: gemeinsame Methode!! FibuExportFacBean
	protected String getExportFormat(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_ZIELPROGRAMM);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 *
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void verbucheRechnungRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			verbucheRechnungGutschriftRueckgaengig(rechnungIId,
				LocaleFac.BELEGART_RECHNUNG, theClientDto);
		} catch(RemoteException ex) {
			throw EJBExcFactory.respectOld(ex);
		}
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Gutschrift und Wertgutschrift
	 * Anmerkung: Wertgutschrift ist in der Fibu die fb_belegart Gutschrift
	 *
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void verbucheGutschriftRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			verbucheRechnungGutschriftRueckgaengig(rechnungIId,
				LocaleFac.BELEGART_GUTSCHRIFT, theClientDto);
		} catch(RemoteException e) {
			throw EJBExcFactory.respectOld(e);
		}
	}

	/**
	 * Mache die Gegenbuchung einer moeglichen Anzahlungsrechnungszahlung
	 * wieder rueckgaengig</br>
	 * 
	 * @param rechnungZahlungId
	 * @param theClientDto
	 */
	private void verbucheAnzahlungGegenbuchungRueckgaengig(
			Integer rechnungZahlungId, TheClientDto theClientDto) {
		BelegbuchungDto belegSrGegenbuchungDto = 
				belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_SCHLUSSRECHNUNG, rechnungZahlungId);
		if(belegSrGegenbuchungDto == null) return;
		
		Integer buchungZahlungIId = belegSrGegenbuchungDto.getBuchungIId();
		removeBelegbuchung(belegSrGegenbuchungDto, theClientDto);
		getBuchenFac().storniereBuchung(buchungZahlungIId, theClientDto);		
	}
	
	private void verbucheAnzahlungZahlungGegenbuchungRueckgaengig(
			Integer rechnungId, TheClientDto theClientDto) {
		BelegbuchungDto belegAzGegenbuchungDto = 
				belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_ANZAHLUNGSRECHNUNG, rechnungId);
		if(belegAzGegenbuchungDto == null) return;
		
		Integer buchungZahlungIId = belegAzGegenbuchungDto.getBuchungIId();
		removeBelegbuchung(belegAzGegenbuchungDto, theClientDto);
		getBuchenFac().storniereBuchung(buchungZahlungIId, theClientDto);				
	}
	
	protected HvOptional<BelegbuchungDto> existsAnzahlungZahlungGegenbuchung(
			Integer azRechnungId) {
		BelegbuchungDto belegAzGegenbuchungDto = 
				belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_ANZAHLUNGSRECHNUNG, azRechnungId);
		return HvOptional.ofNullable(belegAzGegenbuchungDto);
	}
	
	private void verbucheRechnungGutschriftRueckgaengig(Integer rechnungIId,
			String belegartCNr, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto belegbuchungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					belegartCNr, rechnungIId);
			if (belegbuchungDto == null) return;
			
			getSystemFac().pruefeGeschaeftsjahrSperre(belegbuchungDto,
					theClientDto.getMandant());

			Integer buchungIId = belegbuchungDto.getBuchungIId();
			removeBelegbuchung(belegbuchungDto, theClientDto);
			getBuchenFac().storniereBuchung(buchungIId, theClientDto);
			
			RechnungDto reDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			if(!reDto.isSchlussRechnung()) return;
		
			// Bei einer Schlussrechnung die Gegenbuchungen der 
			// Anzahlungsrechnungszahlungen rueckgaengig machen
			Collection<RechnungDto> azDtos = 
					findAnzahlungsRechnungen(reDto.getAuftragIId());
			for (RechnungDto azDto : azDtos) {
				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
						.zahlungFindByRechnungIId(azDto.getIId());
				for (RechnungzahlungDto zahlungDto : zahlungDtos) {
					verbucheAnzahlungGegenbuchungRueckgaengig(
							zahlungDto.getIId(), theClientDto);
				}
				
				verbucheAnzahlungZahlungGegenbuchungRueckgaengig(
						azDto.getIId(), theClientDto);
			}
		}
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Zahlung
	 *
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 */
	private void verbucheZahlungRueckgaengig(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		RechnungzahlungDto rezDto;
		try {
			rezDto = getRechnungFac().rechnungzahlungFindByPrimaryKey(
					zahlungIId);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		verbucheZahlungRueckgaengig(rezDto, theClientDto);
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Zahlung eventuell vorhandene
	 * Vorauszahlung wieder aktivieren
	 *
	 * @param zahlungDto
	 *            RechnungzahlungDto
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 */
	public void verbucheZahlungRueckgaengig(RechnungzahlungDto zahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(zahlungDto.getIId(), theClientDto.getIDUser());
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_REZAHLUNG, zahlungDto.getIId());
			if (buchungZahlungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungZahlungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungZahlungDto.getBuchungIId();
				removeBelegbuchung(buchungZahlungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				if (zahlungDto.isVorauszahlung()) {
					Integer bdIId = zahlungDto.getBuchungdetailIId();
					try {
						myLogger.logData("Vorauszahlung IID:" + zahlungDto.getIId() +
								", BuchungDetailId:" + bdIId +
								", NBetrag:" + zahlungDto.getNBetrag().toString() +
								", NUSt:" + zahlungDto.getNBetragUst() ) ;

						BuchungDto bDto = bucheVorauszahlungBetrag(
								bdIId,
								zahlungDto.getNBetrag().add(
										zahlungDto.getNBetragUst()),
								theClientDto);
						Rechnungzahlung zahlung = em.find(
								Rechnungzahlung.class, zahlungDto.getIId());
						if (zahlung != null) {
							// es wurde nur eine Zahlung neu in der Fibu
							// verbucht und diese ist nicht geloescht
							// dann das neue Buchungsdetail an die Zahlung
							// haengen
							BuchungdetailDto[] bdtos = getBuchenFac()
									.buchungdetailsFindByBuchungIId(
											bDto.getIId());
							zahlung.setBuchungdetailIId(bdtos[0].getIId());
							em.merge(zahlung);
							em.flush();
						}
					} catch (RemoteException e) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH,
								"Vorauszahlung kann nicht aktiviert werden. "
										+ e.getMessage());
					}
				}
				
				try {
					RechnungDto reDto = getRechnungFac()
							.rechnungFindByPrimaryKey(zahlungDto.getRechnungIId());
					if(reDto.isAnzahlungsRechnung()) {
						verbucheAnzahlungGegenbuchungRueckgaengig(
								zahlungDto.getIId(), theClientDto);
						verbucheAnzahlungZahlungGegenbuchungRueckgaengig(
								reDto.getIId(), theClientDto);
					}
				} catch(RemoteException e) {
					throw EJBExcFactory.respectOld(e);
				}			 
			}
		}
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Rechnung.
	 *
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonRechnung(
			Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		Integer erloesKontoIId = kundeDto.getIidErloeseKonto();
		KontoDto erloesKontoDto = null;
		try {
			erloesKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					erloesKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer ustKontoIId = erloesKontoDto.getKontoIIdWeiterfuehrendUst();
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || erloesKontoIId == null
				|| ustKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die rechnung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		BuchungdetailDto[] details = new BuchungdetailDto[3];
		BigDecimal zero = new BigDecimal(0);
		// Buchung am Kundenkonto
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(debitorenKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(rechnungDto.getNWert().add(
				rechnungDto.getNWertust()));
		details[0].setNUst(rechnungDto.getNWertust());
		details[0].setKontoIIdGegenkonto(erloesKontoIId);
		// Buchung am Erloeskonto
		details[1] = new BuchungdetailDto();
		details[1].setKontoIId(erloesKontoIId);
		details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[1].setNBetrag(rechnungDto.getNWert());
		details[1].setNUst(zero);
		details[1].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am UST-Konto
		details[2] = new BuchungdetailDto();
		details[2].setKontoIId(ustKontoIId);
		details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[2].setNBetrag(rechnungDto.getNWertust());
		details[2].setNUst(zero);
		details[2].setKontoIIdGegenkonto(debitorenKontoIId);
		return details;
	}

	
	protected boolean isRechnungDtoReversecharge(RechnungDto rechnungDto) {
		try {
			ReversechargeartDto rcOhneDto = getFinanzServiceFac()
					.reversechargeartFindOhne(rechnungDto.getMandantCNr()) ;
			return !rcOhneDto.getIId().equals(rechnungDto.getReversechargeartId()) ;
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return false ;
	}
	
	protected boolean isEingangsrechnungDtoReversecharge(EingangsrechnungDto erDto) {
		try {
			ReversechargeartDto rcOhneDto = getFinanzServiceFac()
					.reversechargeartFindOhne(erDto.getMandantCNr()) ;
			return !rcOhneDto.getIId().equals(erDto.getReversechargeartId()) ;
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return false ;		
	}
	
	private SachkontoId getKursKonto(boolean verlust, 
			Integer steuerkategorieId, RechnungDto rechnungDto) throws RemoteException {
		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, steuerkategorieId);
		Validator.entityFound(steuerkategorie, steuerkategorieId);
		
		Integer kursKontoId = verlust
				? steuerkategorie.getKontoIIdKursverlust() 
				: steuerkategorie.getKontoIIdKursgewinn();
		if(kursKontoId == null) {
			SteuerkategorieDto stkDto = SteuerkategorieDtoAssembler.createDto(steuerkategorie) ;
			throw EJBExcFactory.steuerkategorieBasisKontoFehlt(
					verlust ? EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO
							: EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO, rechnungDto, stkDto) ;			
		}
		
		return new SachkontoId(kursKontoId);
	}
	
	private BigDecimal getKursDifferenz(RechnungDto rechnungDto,
			SachkontoId kursGewinn, SachkontoId kursVerlust, Integer ohneZahlungId) throws RemoteException {
		
		BigDecimal sum = BigDecimal.ZERO;
		RechnungzahlungDto[] zahlungsDtos = getRechnungFac()
				.zahlungFindByRechnungIIdAbsteigendSortiert(rechnungDto.getIId());
		for (RechnungzahlungDto zahlungDto : zahlungsDtos) {
			if(zahlungDto.getIId().equals(ohneZahlungId)) continue;
//			if(!zahlungDto.isRechnung()) continue;
			
			BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
					LocaleFac.BELEGART_REZAHLUNG,
					zahlungDto.isGutschrift() ? zahlungDto.getRechnungzahlungIIdGutschrift() : zahlungDto.getIId());
			BuchungdetailDto[] zahlungdetailDtos = getBuchenFac()
					.buchungdetailsFindByBuchungIId(
					bbDto.getBuchungIId());
			for (BuchungdetailDto buchungdetailDto : zahlungdetailDtos) {
				Integer kontoId = buchungdetailDto.getKontoIId();
				if(kursGewinn.id().equals(kontoId)) {
					sum = sum
							.add(buchungdetailDto.getNBetrag())
							.add(buchungdetailDto.getNUst());
				} else if(kursVerlust.id().equals(kontoId)) {
					sum = sum
							.subtract(buchungdetailDto.getNBetrag())
							.subtract(buchungdetailDto.getNUst());
				}
			}
		}
		
		return sum;
	}
	
	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Zahlung.
	 *
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsFuerZahlung(
			RechnungzahlungDto zahlungDto, RechnungDto rechnungDto,
			boolean bBucheMitSkonto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		Integer sollKontoIId = null;
		Integer kundeId = rechnungDto.getKundeIId();
		
		// SP8614 Lieferkunde des Auftrags ist bei Anzahlung der Debitor
		if (rechnungDto.isAnzahlungsRechnung()) {
			AuftragDto abDto = getAuftragFac()
					.auftragFindByPrimaryKey(rechnungDto.getAuftragIId());
			kundeId = abDto.getKundeIIdLieferadresse();
		}
		
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				kundeId, theClientDto);

		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)) {
			BankverbindungDto bankverbindungDto = null;
			try {
				if (zahlungDto.getBankkontoIId() != null) {
					bankverbindungDto = getFinanzFac()
							.bankverbindungFindByPrimaryKey(
									zahlungDto.getBankkontoIId());
					sollKontoIId = bankverbindungDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_BAR)) {
			KassenbuchDto kassenbuchDto = null;
			try {
				if (zahlungDto.getKassenbuchIId() != null) {
					kassenbuchDto = getFinanzFac().kassenbuchFindByPrimaryKey(
							zahlungDto.getKassenbuchIId(), theClientDto);
					sollKontoIId = kassenbuchDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			Lieferant lieferant = null;
			try {
				Eingangsrechnung eingangsrechnung = em.find(
						Eingangsrechnung.class,
						zahlungDto.getEingangsrechnungIId());
				lieferant = em.find(Lieferant.class,
						eingangsrechnung.getLieferantIId());
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			sollKontoIId = lieferant.getKontoIIdKreditorenkonto();
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			if (zahlungDto.getBuchungdetailIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"BuchungdetailDto=null in Vorauszahlung");
			try {
				Buchungdetail bd = em.find(Buchungdetail.class,
						zahlungDto.getBuchungdetailIId());
				sollKontoIId = bd.getKontoIId();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			sollKontoIId = kundeDto.getIidDebitorenkonto();
			//hier das Debitorenkonto verwenden, wird von der aufrufenden Methode sowieso
			//verworfen. Anders waere der Aufwand einfach zu grosz
			// TODO: Alles was Zahlungen betrifft refactoren
		}
		KontoDto debitorenKontoDto = null;
		KontoDto sollKontoDto = null;
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		try {
			debitorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					debitorenKontoIId);
			sollKontoDto = getFinanzFac().kontoFindByPrimaryKey(sollKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || sollKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die Zahlung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		// pruefen ob Skonto zu buchen
		boolean bMitSkonto = false;
		boolean bMehrfachSteuersatz = false;
//		boolean reverseCharge = rechnungDto.isReverseCharge();
		boolean reverseCharge = isRechnungDtoReversecharge(rechnungDto) ;
		int mehrfachSteuerBuchungen = 0;
		Integer ustKontoIId[] = new Integer[1];
		Integer skontoKontoIId[] = new Integer[1];
		BigDecimal skontoBetrag = new BigDecimal(0);
		BigDecimal skontoUstBetrag = new BigDecimal(0);
		BigDecimal skontoProzent = new BigDecimal(0);
		BigDecimal[] skontoBetragTeil = null;
		BigDecimal[] skontoUstBetragTeil = null;
		BigDecimal kursDifferenzBetrag = new BigDecimal(0);

		FibuexportDto[] exportDaten = getExportDatenRechnung(
				rechnungDto, theClientDto);

		// Es wird immer an den Debitor der Lieferadresse gebucht, sofern diese
		// anders als der Debitor der Rechnungsadresse ist.
		if(exportDaten[1].getDebitorenKontoIIdUebersteuert() != null) {
			debitorenKontoIId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
		}

		SteuerkategorieDto stkDto = 
				getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
						exportDaten[0].getKontoDto().getSteuerkategorieCnr(), 
						rechnungDto.getReversechargeartId(), 
						exportDaten[0].getKontoDto().getFinanzamtIId(), theClientDto) ;
		Integer steuerkategorieIId = stkDto.getIId() ;

		// SP3734 Die Kursdifferenz muss bei jeder Zahlung ermittelt werden
		// die nachfolgende Skonto-Berechnung nur einmal wenn die AR erledigt ist  
		BigDecimal kursDifferenz = rechnungDto.getNKurs().subtract(
				zahlungDto.getNKurs());
	
		if (kursDifferenz.signum() != 0) {
/*				
			kursDifferenzBetrag = zahlungDto
					.getNBetragfw()
					.add(zahlungDto.getNBetragUstfw())
					.divide(rechnungDto.getNKurs(),
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
			kursDifferenzBetrag = kursDifferenzBetrag.subtract(
					zahlungDto.getNBetrag().add(
							zahlungDto.getNBetragUst())).negate();
			
			BigDecimal skontoFw = rechnungDto.getNWertfw()
					.add(rechnungDto.getNWertustfw())
					.subtract(zahlungDto.getNBetragfw())
					.subtract(zahlungDto.getNBetragUstfw());
			BigDecimal skonto = skontoFw.divide(zahlungDto.getNKurs(), 
					FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			kursDifferenzBetrag = rechnungDto.getNWert().add(rechnungDto.getNWertust())
					.subtract(
							zahlungDto.getNBetrag().add(zahlungDto.getNBetragUst())
					.add(skonto));
// TODO: So gehoert es!			
			kursDifferenzBetrag = zahlungDto.getNBetrag().add(zahlungDto.getNBetragUst())
					.subtract(rechnungDto.getNWert().add(rechnungDto.getNWertust())).add(skonto);
*/			
			BigDecimal zahlungFw = zahlungDto.getNBetragfw()
					.add(zahlungDto.getNBetragUstfw());
			BigDecimal zahlungZuRechnungsDatum = zahlungFw
					.divide(rechnungDto.getNKurs(), 
							FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			kursDifferenzBetrag = zahlungDto.getNBetrag()
					.add(zahlungDto.getNBetragUst())
					.subtract(zahlungZuRechnungsDatum);
		}

		
		if (rechnungDto.isBezahlt()) {
			try {
				BigDecimal betragFw = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw());
				BigDecimal betragBezahltFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null)
						.add(getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(), null));
				
				BigDecimal betrag = rechnungDto.getNWert().add(
						rechnungDto.getNWertust());
				BigDecimal betragBezahlt = getRechnungFac()
						.getBereitsBezahltWertVonRechnung(rechnungDto.getIId(),
								null).add(
								getRechnungFac()
										.getBereitsBezahltWertVonRechnungUst(
												rechnungDto.getIId(), null));
				if (rechnungDto.isSchlussRechnung()) {
					BigDecimal fw = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungFw(rechnungDto.getIId());
					BigDecimal ustFw = getRechnungFac().getAnzahlungenZuSchlussrechnungUstFw(rechnungDto.getIId());
					betragBezahltFw = betragBezahltFw.add(fw).add(ustFw);

					// anzahlungen beruecksichtigen
					betragBezahlt = betragBezahlt.add(getRechnungFac()
							.getAnzahlungenZuSchlussrechnungBrutto(
									rechnungDto.getIId()));
				}

/*				
				if (bBucheMitSkonto) {
					BigDecimal skontoFw = betragFw.subtract(betragBezahltFw);
					skontoBetrag = skontoFw.divide(zahlungDto.getNKurs(), 
							FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					skontoProzent = skontoBetrag.divide(betrag, 4,
							BigDecimal.ROUND_HALF_EVEN);
					
					kursDifferenzBetrag = kursDifferenzBetrag
							.add(betragBezahlt).add(skontoBetrag).subtract(betrag);
				}
*/
				if(bBucheMitSkonto) {
					BigDecimal skontoFw = betragFw.subtract(betragBezahltFw);
					BigDecimal skontoZahlDatum = skontoFw
							.divide(zahlungDto.getNKurs(), 
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal skontoRechDatum = skontoFw
							.divide(rechnungDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					kursDifferenzBetrag = kursDifferenzBetrag
							.add(skontoZahlDatum).subtract(skontoRechDatum);
					
					// Zwischen Anzahlungs- und Schlussrechnung kann es Kursdifferenz 
					// geben, die aber mit kursDifferenz (zw. Rechnung und Zahlung) 
					// nicht erkannt wird. => Waehrungsueberpruefung notwendig.
					MandantDto mandantDto = getMandantFac()
							.mandantFindByPrimaryKey(rechnungDto.getMandantCNr(), theClientDto);
					if(!rechnungDto.getWaehrungCNr().equals(mandantDto.getWaehrungCNr())) {						
//					if(kursDifferenz.signum() != 0) {					
						skontoBetrag = skontoZahlDatum;
/*	
 * SP8069 Die moegliche Cent-Differenz wird vorerst ignoriert
 * Sie kann durch Rundungsungenauigkeiten beim Beruecksichtigen
 * der Betraege aus anderen Wechselkursen entstehen.
 * 					
						if(skontoBetrag.signum() != 0 && rechnungDto.isAnzahlungsRechnung()) {
							SachkontoId kursGewinnId = getKursKonto(false, steuerkategorieIId, rechnungDto);
							SachkontoId kursVerlustId = getKursKonto(true, steuerkategorieIId, rechnungDto);
							
							BigDecimal kursGV = getKursDifferenz(rechnungDto, kursGewinnId, kursVerlustId, zahlungDto.getIId());
							
							BigDecimal d = betrag;
							BigDecimal d1 = betragBezahlt.add(skontoBetrag)
									.subtract(kursDifferenzBetrag).subtract(kursGV);
							BigDecimal diff = d.subtract(d1);
							if(diff.abs().signum() != 0) {
								if(diff.abs().compareTo(new BigDecimal("0.01")) < 1) {
									skontoBetrag = skontoBetrag.add(diff);
								} else {
									if(!rechnungDto.isSchlussRechnung()) {
										EJBExceptionLP ex = new EJBExceptionLP(
												EJBExceptionLP.FEHLER_FINANZ_EXPORT_NETTODIFFERENZ_ZUHOCH,
												new Exception("Korrekturwert beim Verbuchen der Zahlung zu hoch"));
										List<Object> a = new ArrayList<Object>();
										a.add(rechnungDto.getCNr());
										a.add(Helper.formatZahl(diff, FinanzFac.NACHKOMMASTELLEN, theClientDto.getLocUi()));
										ex.setAlInfoForTheClient(a);
										throw ex;									
									}
								}
							}							
						}
*/	
						skontoProzent = skontoFw.divide(
								betragFw, 4, BigDecimal.ROUND_HALF_EVEN);

						
					} else {
						skontoBetrag = betrag.subtract(betragBezahlt);
						skontoProzent = skontoBetrag.divide(betrag, 4,
								BigDecimal.ROUND_HALF_EVEN);
					}

/*					
					if(rechnungDto.isSchlussRechnung()) {
						BigDecimal betragBezahltZahlDatum = betragBezahltFw.divide(rechnungDto.getNKurs(),
								FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						BigDecimal kursDiffZahlung = betragBezahlt.subtract(betragBezahltZahlDatum);
						kursDifferenzBetrag = kursDifferenzBetrag.add(kursDiffZahlung);						
					}
*/					
/*					
					if(kursDifferenz.signum() != 0) {
						// Skonto in Fremdwaehrung ermitteln
						BigDecimal skontoFw = betragFw.subtract(betragBezahltFw);
						BigDecimal skontoZahlDatumFw = skontoFw
								.divide(zahlungDto.getNKurs(), 
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						BigDecimal skontoRechDatumFw = skontoFw
								.divide(rechnungDto.getNKurs(),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						kursDifferenzBetrag = kursDifferenzBetrag
								.add(skontoZahlDatumFw).subtract(skontoRechDatumFw);
						skontoBetrag = skontoZahlDatumFw;
						skontoProzent = skontoFw.divide(
								betragFw, 4, BigDecimal.ROUND_HALF_EVEN);
					} else {
						skontoBetrag = betrag.subtract(betragBezahlt);
						skontoProzent = skontoBetrag.divide(betrag, 4,
								BigDecimal.ROUND_HALF_EVEN);
					}
*/					
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if (skontoBetrag.signum() != 0) {
				bMitSkonto = true;


				if (exportDaten.length > 2) {
					bMehrfachSteuersatz = true;
					// aufteilen
					skontoBetragTeil = new BigDecimal[exportDaten.length - 1];
					skontoUstBetragTeil = new BigDecimal[exportDaten.length - 1];
					ustKontoIId = new Integer[exportDaten.length - 1];
					skontoKontoIId = new Integer[exportDaten.length - 1];
					int iMax = 0;
					BigDecimal skontobetragmax = new BigDecimal(0);
					BigDecimal skontobetragsumme = new BigDecimal(0);
					BigDecimal skontoustsumme = new BigDecimal(0);
					for (int i = 1; i < exportDaten.length; i++) {
						skontoBetragTeil[i - 1] = Helper.rundeKaufmaennisch(
								exportDaten[i].getHabenbetragBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						BigDecimal skontoUst = Helper.rundeKaufmaennisch(
								exportDaten[i].getSteuerBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						if(reverseCharge) {
							skontoBetragTeil[i - 1] = skontoBetragTeil[i - 1].add(skontoUst);
							skontoUstBetragTeil[i - 1] = BigDecimal.ZERO;
						} else {
							skontoUstBetragTeil[i - 1] = skontoUst;
						}
						if ((skontoBetragTeil[i - 1].subtract(skontobetragmax))
								.doubleValue() > 0) {
							skontobetragmax = skontoBetragTeil[i - 1];
							iMax = i - 1;
						}
						skontobetragsumme = skontobetragsumme
								.add(skontoBetragTeil[i - 1]);
						skontoustsumme = skontoustsumme
								.add(skontoUstBetragTeil[i - 1]);
						if (exportDaten[i].getMwstsatz().getIIMwstsatzbezId() != null) {
							Integer mwstSatzbezIId = exportDaten[i]
									.getMwstsatz().getIIMwstsatzbezId();
							HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
									steuerkategorieIId, mwstSatzbezIId, rechnungDto.getTBelegdatum());
							if (stkk.isPresent()) {
								ustKontoIId[i - 1] = stkk.get().getKontoIIdVk();
								skontoKontoIId[i - 1] = stkk
										.get().getKontoIIdSkontoVk();
							} else {
								// wenn nicht ueber Steuerkategorie definiert,
								// dann alte Methode anwenden
								ustKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendUst();
								skontoKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendSkonto();
								if (skontoKontoIId[i - 1] == null) {
									SteuerkategorieDto stkDebDto = getFinanzServiceFac()
											.steuerkategorieFindByCNrFinanzamtIId(debitorenKontoDto.getSteuerkategorieCnr(),
												rechnungDto.getReversechargeartId(), debitorenKontoDto.getFinanzamtIId(), theClientDto) ;
									skontoKontoIId[i - 1] = getSkontoKonto(
											stkDebDto.getIId(),
//											debitorenKontoDto
//													.getSteuerkategorieIId(),
											ustKontoIId[i - 1], rechnungDto.getTBelegdatum());
								}
							}
						}
						// Pruefung wirft entspechende Exception
						pruefeSteuerSkontoKonten(rechnungDto.getBelegartCNr(),
								rechnungDto.getCNr(), ustKontoIId[i - 1],
								skontoKontoIId[i - 1], steuerkategorieIId,
								exportDaten[i]);

						if (skontoUstBetragTeil[i - 1].doubleValue() != 0)
							mehrfachSteuerBuchungen++;
					}
					BigDecimal diff = skontoBetrag.subtract(skontobetragsumme)
							.subtract(skontoustsumme);
					if (diff.doubleValue() != 0) {
						// hoechsten korrigieren
						skontoBetragTeil[iMax] = skontoBetragTeil[iMax]
								.add(diff);
						// ? muss die Steuer auch korrigiert werden?
					}
				} else {
					Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
							.getIIMwstsatzbezId();
					HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
							steuerkategorieIId, mwstSatzbezIId, rechnungDto.getTBelegdatum());
					if (stkk.isPresent()) {
						ustKontoIId[0] = stkk.get().getKontoIIdVk();
						skontoKontoIId[0] = stkk.get().getKontoIIdSkontoVk();
					} else {
						ustKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendUst();
						skontoKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendSkonto();
						if (skontoKontoIId[0] == null)
							skontoKontoIId[0] = getSkontoKonto(
									debitorenKontoDto.getSteuerkategorieIId(),
									ustKontoIId[0], rechnungDto.getTBelegdatum());
					}
					// Pruefung wirft entspechende Exception
					pruefeSteuerSkontoKonten(rechnungDto.getRechnungartCNr(),
							rechnungDto.getCNr(), ustKontoIId[0],
							skontoKontoIId[0], steuerkategorieIId,
							exportDaten[1]);

					if(!reverseCharge) {
						BigDecimal mwst = new BigDecimal(exportDaten[1]
								.getMwstsatz().getFMwstsatz());
						mwst = mwst.movePointLeft(2);
						skontoUstBetrag = skontoBetrag.multiply(mwst).divide(
								new BigDecimal(1).add(mwst),
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}
		}

		Integer gegenkontoId = debitorenKontoIId;
		SachkontoId anzahlungVerrKontoId = null;
		if(rechnungDto.isAnzahlungsRechnung()) {
			FinanzamtKontenUebersetzung faKu =
					new FinanzamtKontenUebersetzung(
							debitorenKontoDto.getFinanzamtIId(), 
							debitorenKontoDto.getMandantCNr());
			anzahlungVerrKontoId = faKu.getAnzahlungVerrechnet(rechnungDto, theClientDto);
			gegenkontoId = faKu.getAnzahlungBezahlt(rechnungDto, theClientDto).id();
		}
		
		BuchungdetailDto[] details;
		if (bMitSkonto && !rechnungDto.isAnzahlungsRechnung()) {
			if (bMehrfachSteuersatz) {
				details = new BuchungdetailDto[2 + 2 * skontoBetragTeil.length
						+ mehrfachSteuerBuchungen];
				// SOLL SKONTO Buchung am Skonto Konto
				int j = 2 + skontoBetragTeil.length;
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[j] = new BuchungdetailDto();
					details[j].setKontoIId(skontoKontoIId[i]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
					// Buchung mit - gemacht werden !!
					// details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					// details[j].setNBetrag(skontoBetragTeil[i]);
					details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[j].setNBetrag(skontoBetragTeil[i].negate());
					details[j].setNUst(skontoUstBetragTeil[i].negate());
					details[j].setKontoIIdGegenkonto(gegenkontoId);
					j++;
					if (skontoUstBetragTeil[i].doubleValue() != 0) {
						// Ust Korrekturbuchung
						details[j] = new BuchungdetailDto();
						details[j].setKontoIId(ustKontoIId[i]);
						// !! entgegen der Buchungsregel soll lt. wh hier eine
						// HABEN Buchung mit - gemacht werden !!
						// details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
						// details[j].setNBetrag(skontoUstBetragTeil[i]);
						details[j]
								.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
						details[j].setNBetrag(skontoUstBetragTeil[i].negate());
						details[j].setNUst(new BigDecimal(0));
						details[j].setKontoIIdGegenkonto(gegenkontoId);
						j++;
					}
				}
			} else {
				details = new BuchungdetailDto[skontoUstBetrag.signum() != 0 ? 5 : 4];
				// SOLL SKONTO Buchung am Skonto Konto
				details[2] = new BuchungdetailDto();
				details[2].setKontoIId(skontoKontoIId[0]);
				// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
				// Buchung mit - gemacht werden !!
				// details[2].setBuchungdetailartCNr(BuchenFac.SollBuchung);
				// details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag));
				details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag)
						.negate());
				details[2].setNUst(skontoUstBetrag);
				details[2].setKontoIIdGegenkonto(gegenkontoId);
				if (skontoUstBetrag.doubleValue() != 0) {
					// Ust Korrekturbuchung
					details[4] = new BuchungdetailDto();
					details[4].setKontoIId(ustKontoIId[0]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
					// Buchung mit - gemacht werden !!
					// details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					// details[3].setNBetrag(skontoUstBetrag);
					details[4].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[4].setNBetrag(skontoUstBetrag.negate());
					details[4].setNUst(new BigDecimal(0));
					details[4].setKontoIIdGegenkonto(gegenkontoId);
				}
			}
		} else {
			details = new BuchungdetailDto[2];
		}
		// SOLL Buchung Bank
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(sollKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(zahlungDto.getNBetrag().add(
				zahlungDto.getNBetragUst()));
		details[0].setNUst(zahlungDto.getNBetragUst());
		details[0].setKontoIIdGegenkonto(gegenkontoId);
		// HABEN Buchung am Forderungen Debitor
		if (bMitSkonto && !rechnungDto.isAnzahlungsRechnung()) {
			// mit Skonto Buchung aufsplitten
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(gegenkontoId);
			details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			if (bMehrfachSteuersatz) {
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[i + 2] = new BuchungdetailDto();
					details[i + 2].setKontoIId(gegenkontoId);
					details[i + 2]
							.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[i + 2].setKontoIIdGegenkonto(skontoKontoIId[0]);
					details[i + 2].setNBetrag(skontoBetragTeil[i]
							.add(skontoUstBetragTeil[i]));
					details[i + 2].setNUst(skontoUstBetragTeil[i]);
				}
			} else {
				details[3] = new BuchungdetailDto();
				details[3].setKontoIId(gegenkontoId);
				details[3].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				details[3].setKontoIIdGegenkonto(skontoKontoIId[0]);
				details[3].setNBetrag(skontoBetrag);
				details[3].setNUst(skontoUstBetrag);
			}
		} else {
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(gegenkontoId);
			details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		}
		details[1].setNBetrag(zahlungDto.getNBetrag().add(
				zahlungDto.getNBetragUst()));
		// Zahlung ohne UST
		details[1].setNUst(BigDecimal.ZERO);
		details[1].setKontoIIdGegenkonto(sollKontoIId);

/*		
		if(rechnungDto.isAnzahlungsRechnung() &&
				skontoBetrag.signum() != 0) {
			if(zahlungDto.getNBetragUst().signum() != 0) {
				
			} else {
				List<BuchungdetailDto> skontoDetails = 
						BuchungdetailFactory.skontoZahlungAnzahlung(
								skontoBetrag, new SachkontoId(skontoKontoIId[0]),
								new KontoId(gegenkontoId));
				details = BuchungdetailFactory.add(details, skontoDetails);
			}
		}
*/		
		
		if (kursDifferenzBetrag.signum() != 0) {
/*			
			if (steuerkategorieIId == null) {
				
				// dann aus Debitor
				SteuerkategorieDto stkDto = 
						getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
								debitorenKontoDto.getSteuerkategorieCnr(), 
								rechnungDto.getReversechargeartId(),
								debitorenKontoDto.getFinanzamtIId(), theClientDto) ;
				steuerkategorieIId = stkDto.getIId();
			}
*/			
			boolean bVerlust = kursDifferenzBetrag.signum() < 0;
			SachkontoId kontoKursId = getKursKonto(
					bVerlust, steuerkategorieIId, rechnungDto);
/*			
			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
					steuerkategorieIId);
			Integer kontoKursIId = null;
			boolean bVerlust = false;
			if (kursDifferenzBetrag.signum() < 0) {
				kontoKursIId = steuerkategorie.getKontoIIdKursverlust();
				if (kontoKursIId == null) {
					SteuerkategorieDto stkDto = SteuerkategorieDtoAssembler.createDto(steuerkategorie) ;
					throw EJBExcFactory.steuerkategorieBasisKontoFehlt(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO, rechnungDto, stkDto) ;
				}
				bVerlust = true;
			} else {
				kontoKursIId = steuerkategorie.getKontoIIdKursgewinn();
				if (kontoKursIId == null) {
					SteuerkategorieDto stkDto = SteuerkategorieDtoAssembler.createDto(steuerkategorie) ;
					throw EJBExcFactory.steuerkategorieBasisKontoFehlt(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO, rechnungDto, stkDto) ;
				}
			}
*/			
			Integer fwgegenkontoId = gegenkontoId;
			if(rechnungDto.isAnzahlungsRechnung()) {
				fwgegenkontoId = anzahlungVerrKontoId.id();
			}
			List<BuchungdetailDto> kursDetails = detailsFuerKursaenderung(
					kontoKursId, fwgegenkontoId, kursDifferenzBetrag, bVerlust);
			details = Arrays.copyOf(details, details.length + kursDetails.size());
			details[details.length - 2] = kursDetails.get(0);
			details[details.length - 1] = kursDetails.get(1);

/*			
			BuchungdetailDto detailDiff = new BuchungdetailDto();
			detailDiff.setKontoIId(kontoKursId.id());
			detailDiff.setKontoIIdGegenkonto(gegenkontoId);
			detailDiff.setNBetrag(kursDifferenzBetrag.abs());
			detailDiff.setNUst(new BigDecimal(0));
			detailDiff.setBuchungdetailartCNr(
					bVerlust ? BuchenFac.SollBuchung: BuchenFac.HabenBuchung);
			details[details.length - 2] = detailDiff;
			
			BuchungdetailDto detailDiffGegen = new BuchungdetailDto();
			detailDiffGegen.setKontoIId(gegenkontoId);
			detailDiffGegen.setKontoIIdGegenkonto(kontoKursId.id());
			detailDiffGegen.setNBetrag(kursDifferenzBetrag.abs());
			detailDiffGegen.setNUst(new BigDecimal(0));
			details[details.length - 1] = detailDiffGegen;
			detailDiffGegen.setBuchungdetailartCNr(
					bVerlust ? BuchenFac.HabenBuchung : BuchenFac.SollBuchung);
*/					
		}

//		if(rechnungDto.isAnzahlungsRechnung() && bBucheMitSkonto) {
		if(rechnungDto.isAnzahlungsRechnung()) {
			// Die verbuchte AnzahlungVerrechnet wertmaessig rueckgaengig 
			// machen, weil wir den Kredit gerade aktiv verwendet haben.
			// D.h. Debitor Anzahlung rueckgaengig, 
			// AnzahlungVerr rueckgaengig

//			BuchungdetailDto[] bds = createLoeseAnzahlungsrechnungAuf0(
//					rechnungDto, new DebitorkontoId(debitorenKontoIId), 
//					anzahlungVerrKontoId);
			List<BuchungdetailDto> azdetails = createLoeseAnzahlungsrechnungAuf(
					rechnungDto, details, new DebitorkontoId(debitorenKontoIId), 
					anzahlungVerrKontoId, new SachkontoId(gegenkontoId));

			// SP8324 Nun noch die Steuerbuchung durchfuehren
			// Das zweite Buchungsdetail von details ist "falsch". Es ist
			// bisher brutto betrachtet worden. Es muss allerdings netto
			// betrachtet werden, d.h. es muss nun die Steuerbuchung 
			// erzeugt werden.
			List<BuchungdetailDto> bds = new ArrayList<BuchungdetailDto>();
			for(BuchungdetailDto detailDto : details) {
				if(detailDto != null) {
					bds.add(detailDto);	
				}
			}
//			bds.add(details[0]);
//			bds.add(details[1]);
//			if(kursDifferenzBetrag.signum() != 0) {
//				bds.add(details[2]);
//				bds.add(details[3]);
//			}
			if(details[0].getNUst().signum() != 0) {
				BigDecimal brutto = details[0].getNBetrag();
				BigDecimal steuer = details[0].getNUst();
				
				details[1].setNBetrag(brutto.subtract(steuer));
	
				Integer mwstSatzbezIId = exportDaten[1]
						.getMwstsatz().getIIMwstsatzbezId();
				HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
						steuerkategorieIId, mwstSatzbezIId, rechnungDto.getTBelegdatum());
				if (stkk.isPresent()) {
					BuchungdetailDto steuerDetail = BuchungdetailDto.haben(
							stkk.get().getKontoIIdVk(),
							details[1].getKontoIIdGegenkonto(),
							steuer, BigDecimal.ZERO);
					bds.add(steuerDetail);
				} else {
					throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(
							rechnungDto, stkDto, mwstSatzbezIId);
				}
			}
			bds.addAll(azdetails);
	
			// Das was "zuviel/zuwenig" bezahlt wurde durch die Fremdwaehrung
			// wieder gegenbuchen. AnzVer und Debitor sollen den urspruenglichen
			// Rechnungsbetrag haben. Fremdwaehrungsaenderungen muss der 
			// Unternehmer tragen
			if (kursDifferenzBetrag.signum() != 0) { // keine 0.00 Buchungen
				boolean bVerlust = kursDifferenzBetrag.signum() < 0;
				List<BuchungdetailDto> fwDetails = createLoeseAnzahlungsrechnungAufFw(
						kursDifferenzBetrag, new DebitorkontoId(debitorenKontoIId),
						anzahlungVerrKontoId, new SachkontoId(gegenkontoId), bVerlust);
				bds.addAll(fwDetails);				
			}
			
			// Anzahlungsbetrag IST-Versteuerung -> nur  Ausgleich Debitor/AnzVerr,
			// keine UST notwendig fuer einen nicht erhaltenen Betrag
			if(skontoBetrag.signum() != 0) {
				BuchungdetailDto bd1 = BuchungdetailDto.haben(
						debitorenKontoIId, anzahlungVerrKontoId.id(), 
						skontoBetrag, BigDecimal.ZERO);
				bd1.setCKommentar("Skonto");
				bds.add(bd1);
				BuchungdetailDto bd2 = BuchungdetailDto.soll(
						anzahlungVerrKontoId.id(), debitorenKontoIId,
						skontoBetrag, BigDecimal.ZERO);
				bd2.setCKommentar("Skonto");
				bds.add(bd2);
				
				List<BuchungdetailDto> skontoDetails = 
						BuchungdetailFactory.skontoZahlungAnzahlung(
								skontoBetrag, new SachkontoId(skontoKontoIId[0]),
								new KontoId(gegenkontoId));
				bds.addAll(skontoDetails);
			}
			details = bds.toArray(new BuchungdetailDto[0]);
		}
		
		for (int i = 0; i < details.length; i++) {
			details[i].setIAuszug(zahlungDto.getIAuszug());
			details[i].setCKommentar(zahlungDto.getCKommentar());
		}

		return details;
	}

	private List<BuchungdetailDto> detailsFuerKursaenderung(
			SachkontoId kontoKursId, Integer gegenkontoId, BigDecimal kursDifferenzBetrag,
			boolean bVerlust) {
		BuchungdetailDto detailDiff = new BuchungdetailDto();
		detailDiff.setKontoIId(kontoKursId.id());
		detailDiff.setKontoIIdGegenkonto(gegenkontoId);
		detailDiff.setNBetrag(kursDifferenzBetrag.abs());
		detailDiff.setNUst(new BigDecimal(0));
		detailDiff.setBuchungdetailartCNr(
				bVerlust ? BuchenFac.SollBuchung: BuchenFac.HabenBuchung);
		
		BuchungdetailDto detailDiffGegen = new BuchungdetailDto();
		detailDiffGegen.setKontoIId(gegenkontoId);
		detailDiffGegen.setKontoIIdGegenkonto(kontoKursId.id());
		detailDiffGegen.setNBetrag(kursDifferenzBetrag.abs());
		detailDiffGegen.setNUst(new BigDecimal(0));
		detailDiffGegen.setBuchungdetailartCNr(
				bVerlust ? BuchenFac.HabenBuchung : BuchenFac.SollBuchung);
		return Arrays.asList(detailDiff, detailDiffGegen);
	}
	
	// So sollte es eigentlich sein
	/**
	 * Wir verbuchen jede (Teil)Zahlung.</br>
	 * <p>Es geht darum, dass eine Zahlung der Anzahlungsrechnung auch 
	 * bei einem Sollversteuerer als IST-Versteuerer zu buchen ist. D.h.
	 * es f�llt mit der Zahlung die UST an.</p>
	 * 
	 * @param rechnungDto
	 * @param zahlungdetailDtos
	 * @param debitorKonto
	 * @param anzahlungVerrKonto
	 * @param anzahlungVerrBezahltKonto
	 * @return
	 * @throws RemoteException
	 */
	private List<BuchungdetailDto> createLoeseAnzahlungsrechnungAuf(
			RechnungDto rechnungDto,
			BuchungdetailDto[] zahlungdetailDtos,
			DebitorkontoId debitorKonto,
			SachkontoId anzahlungVerrKonto, SachkontoId anzahlungVerrBezahltKonto)  throws RemoteException {
		
		// Die Verbuchung der Anzahlung war "brutto", d.h. der Betrag ist
		// der Bruttobetrag. Im Detail steht auch noch der Steueranteil,
		// aber der ist (brutto) eben schon enthalten. 
		// Wir wollen hier eigentlich nur die AnzahlungVerrechnungsdetails
		// stornieren

		BigDecimal sum = zahlungdetailDtos[0].getNBetrag().negate(); // Zahlung ist brutto
		List<BuchungdetailDto> bds = new ArrayList<BuchungdetailDto>();
		bds.add(BuchungdetailDto.soll(debitorKonto.id(), 
				anzahlungVerrKonto.id(), sum, BigDecimal.ZERO));
		bds.add(BuchungdetailDto.haben(anzahlungVerrKonto.id(),
				debitorKonto.id(), sum, BigDecimal.ZERO));
/*
 		bds[2] = BuchungdetailDto.soll(anzahlungVerrBezahltKonto.id(),
				null, sum.negate(), BigDecimal.ZERO);
 */
//		return new BuchungdetailDto[0];
		return bds;
	}

	private List<BuchungdetailDto> createLoeseAnzahlungsrechnungAufFw(
			BigDecimal kursDifferenzBetrag, DebitorkontoId debitorKonto,
			SachkontoId anzahlungVerrKonto, SachkontoId anzahlungBezKonto,
			boolean kursverlust)  throws RemoteException {		
		List<BuchungdetailDto> bds = new ArrayList<BuchungdetailDto>();
		bds.add(BuchungdetailDto.soll(debitorKonto.id(), 
				anzahlungVerrKonto.id(), kursDifferenzBetrag, BigDecimal.ZERO));
		bds.add(BuchungdetailDto.haben(anzahlungVerrKonto.id(),
				debitorKonto.id(), kursDifferenzBetrag, BigDecimal.ZERO));
		if(kursverlust) {
			bds.add(BuchungdetailDto.soll(anzahlungVerrKonto.id(), 
					anzahlungBezKonto.id(), kursDifferenzBetrag.abs(), BigDecimal.ZERO));
			bds.add(BuchungdetailDto.haben(anzahlungBezKonto.id(), 
					anzahlungVerrKonto.id(), kursDifferenzBetrag.abs(), BigDecimal.ZERO));			
		} else {
			bds.add(BuchungdetailDto.haben(anzahlungVerrKonto.id(), 
					anzahlungBezKonto.id(), kursDifferenzBetrag.abs(), BigDecimal.ZERO));
			bds.add(BuchungdetailDto.soll(anzahlungBezKonto.id(), 
					anzahlungVerrKonto.id(), kursDifferenzBetrag.abs(), BigDecimal.ZERO));			
		}
		
		return bds;
	}
	
	// So sollte es eigentlich sein
	private List<BuchungdetailDto> createLoeseAnzahlungsrechnungAuf1(
			RechnungDto rechnungDto,
			BuchungdetailDto[] zahlungdetailDtos,
			DebitorkontoId debitorKonto,
			SachkontoId anzahlungVerrKonto, SachkontoId anzahlungVerrBezahltKonto)  throws RemoteException {
		
		BelegbuchungDto belegbuchungDto = belegbuchungFindByBelegartCNrBelegiid(
				LocaleFac.BELEGART_FIBU_RECHNUNG, rechnungDto.getIId());
		BuchungdetailDto[] details = getBuchenFac().buchungdetailsFindByBuchungIId(belegbuchungDto.getBuchungIId());

		// Die Verbuchung der Anzahlung war "brutto", d.h. der Betrag ist
		// der Bruttobetrag. Im Detail steht auch noch der Steueranteil,
		// aber der ist (brutto) eben schon enthalten. 
		// Wir wollen hier eigentlich nur die AnzahlungVerrechnungsdetails
		// stornieren
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal sumUst = BigDecimal.ZERO;
		for (BuchungdetailDto buchungdetailDto : details) {
			if(!anzahlungVerrKonto.id().equals(buchungdetailDto.getKontoIId())) {
				continue;
			}
			BigDecimal d = buchungdetailDto.getNBetrag();
//			BigDecimal dUst = buchungdetailDto.getNUst();
			
			if(buchungdetailDto.isSollBuchung()) {
				sum = sum.add(d);
//				sumUst = sumUst.add(dUst);
			} else {
				sum = sum.subtract(d);
//				sumUst = sumUst.subtract(dUst);
			}
		}

		List<BuchungdetailDto> bds = new ArrayList<BuchungdetailDto>();
		bds.add(BuchungdetailDto.soll(debitorKonto.id(), 
				null, sum.add(sumUst), BigDecimal.ZERO));
		bds.add(BuchungdetailDto.haben(anzahlungVerrKonto.id(),
				null, sum.add(sumUst), BigDecimal.ZERO));
/*
 		bds[2] = BuchungdetailDto.soll(anzahlungVerrBezahltKonto.id(),
				null, sum.negate(), BigDecimal.ZERO);
 */
//		return new BuchungdetailDto[0];
		return bds;
	}
	
	// Das ist die Variante bis zum 15.7.2020
	private BuchungdetailDto[] createLoeseAnzahlungsrechnungAuf0 (
			RechnungDto anzahlungReDto, DebitorkontoId debitorKonto, SachkontoId anzahlungVerrKonto) {
		BuchungdetailDto[] bds = new BuchungdetailDto[2];
		bds[0] = BuchungdetailDto.haben(anzahlungVerrKonto.id(),
				null, anzahlungReDto.getNWert().negate(),
				anzahlungReDto.getNWertust().negate());
		bds[1] = BuchungdetailDto.soll(debitorKonto.id(),
				bds[0].getKontoIId(), bds[0].getNBetrag(), bds[0].getNUst());
		return bds;
	}
	
	class FinanzamtKontenUebersetzung {
		private final Finanzamt finanzamt;
		
		public FinanzamtKontenUebersetzung(Integer finanzamtId, String mandantCnr) {
			FinanzamtPK fpk = new FinanzamtPK(finanzamtId, mandantCnr);
			finanzamt = em.find(Finanzamt.class, fpk);
		}
		
		public SachkontoId getAnzahlungVerrechnet(RechnungDto reDto, TheClientDto theClientDto) {
			Integer kontoId = finanzamt.getKontoIIdAnzahlungErhaltenVerr();
			return translate(kontoId, reDto, theClientDto);
		}
		
		public SachkontoId getAnzahlungBezahlt(RechnungDto reDto, TheClientDto theClientDto) {
			Integer kontoId = finanzamt.getKontoIIdAnzahlungErhaltenBezahlt();
			return translate(kontoId, reDto, theClientDto);
		}
		
		private SachkontoId translate(Integer kontoId, RechnungDto reDto, TheClientDto theClientDto) {
			FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
			fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
			fibuExportKriterienDto.setDStichtag(new Date(reDto
								.getTBelegdatum().getTime()));
			
			FibuExportManager manager = FibuExportManagerFactory
					.getFibuExportManager(getExportVariante(theClientDto),
									getExportFormat(theClientDto), fibuExportKriterienDto,
									theClientDto);
			KontoDto kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(
					kontoId, reDto.getIId());
			
			return new SachkontoId(kontoDto.getIId());
		}
	}
	
	
	private FibuexportDto[] getExportDatenRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) {
		// ermitteln der beteiligten Steuersaetze ueber den Exportmanager
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(getExportVariante(theClientDto),
						getExportFormat(theClientDto), fibuExportKriterienDto,
						theClientDto);
		FibuexportDto[] exportDaten = manager.getExportdatenRechnung(
				rechnungDto.getIId(), null);
		return exportDaten;
	}

	private void pruefeSteuerSkontoKonten(String beleg, String belegCNr,
			Integer ustKontoIId, Integer skontoKontoIId,
			Integer steuerkategorieIId, FibuexportDto exportDaten) {
		String mwstbezeichung = null;

		if (ustKontoIId == null
				&& exportDaten.getSteuerBD().signum() != 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEIN_STEUERKONTO,
					"Kein Steuerkonto definiert. " + beleg + ": " + belegCNr
						+ createSteuerkontoInfoForEx(steuerkategorieIId, exportDaten)) ;			
//			
//			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
//					steuerkategorieIId);
//			mwstbezeichung = getMwstBezeichnung(exportDaten);
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_FINANZ_KEIN_STEUERKONTO,
//					"Kein Steuerkonto definiert. " + beleg + ": " + belegCNr
//							+ ", " + "Steuerkategorie: "
//							+ steuerkategorie.getCBez() + ", " + "Steuersatz: "
//							+ mwstbezeichung);
		}

		if (skontoKontoIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEIN_SKONTOKONTO,
					"Kein Skontokonto definiert. " + beleg + ": " + belegCNr
					  + createSteuerkontoInfoForEx(steuerkategorieIId, exportDaten)) ;
//			
//							+ ", " + "Steuerkategorie: "
//							+ steuerkategorie.getCBez() + ", " + "Steuersatz: "
//							+ mwstbezeichung);
//			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
//					steuerkategorieIId);
//			mwstbezeichung = getMwstBezeichnung(exportDaten);
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_FINANZ_KEIN_SKONTOKONTO,
//					"Kein Skontokonto definiert. " + beleg + ": " + belegCNr
//							+ ", " + "Steuerkategorie: "
//							+ steuerkategorie.getCBez() + ", " + "Steuersatz: "
//							+ mwstbezeichung);
		}
	}

	private String createSteuerkontoInfoForEx(Integer steuerkategorieId, FibuexportDto exportDaten) {
		Steuerkategorie sk = em.find(Steuerkategorie.class, steuerkategorieId);
		if(sk == null) {
			return ", Steuerkategorie (Id:" + steuerkategorieId + ") unbekannt"
				+  ", MwSt.Steuersatz: " + getMwstBezeichnung(exportDaten) ;
		}
		
		Reversechargeart rc = em.find(Reversechargeart.class, sk.getReversechargeartIId()) ;
		
		return ", Reversechargeart: " + rc.getCNr()
			+  ", Steuerkategorie: " + sk.getCBez()
			+  ", MwSt.Steuersatz: " + getMwstBezeichnung(exportDaten) ;
	}
	
	private String getMwstBezeichnung(FibuexportDto exportDaten) {
		String mwstbezeichung;
		if (exportDaten.getMwstsatz().getMwstsatzbezDto() == null) {
			Mwstsatzbez mwstbez = em.find(Mwstsatzbez.class, exportDaten
					.getMwstsatz().getIIMwstsatzbezId());
			if (mwstbez != null)
				mwstbezeichung = mwstbez.getCBezeichnung();
			else
				mwstbezeichung = "?";
		} else
			mwstbezeichung = exportDaten.getMwstsatz().getMwstsatzbezDto()
					.getCBezeichnung();
		return mwstbezeichung;
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Zahlung.
	 *
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonZahlungEr(
			EingangsrechnungzahlungDto zahlungDto,
			EingangsrechnungDto rechnungDto, boolean bBucheMitSkonto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer habenKontoIId = null;

		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(rechnungDto.getLieferantIId(),
						theClientDto);
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)) {
			BankverbindungDto bankverbindungDto = null;
			try {
				if (zahlungDto.getBankverbindungIId() != null) {
					bankverbindungDto = getFinanzFac()
							.bankverbindungFindByPrimaryKey(
									zahlungDto.getBankverbindungIId());
					habenKontoIId = bankverbindungDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_BAR)) {
			KassenbuchDto kassenbuchDto = null;
			try {
				if (zahlungDto.getKassenbuchIId() != null) {
					kassenbuchDto = getFinanzFac().kassenbuchFindByPrimaryKey(
							zahlungDto.getKassenbuchIId(), theClientDto);
					habenKontoIId = kassenbuchDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			Kunde kunde = null;
			try {
				Rechnungzahlung rechnungzahlung = em.find(
						Rechnungzahlung.class,
						zahlungDto.getRechnungzahlungIId());
				Rechnung rechnung = em.find(Rechnung.class,
						rechnungzahlung.getRechnungIId());
				kunde = em.find(Kunde.class, rechnung.getKundeIId());
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			habenKontoIId = kunde.getKontoIIdDebitorenkonto();
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			if (zahlungDto.getBuchungdetailIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"BuchungdetailDto=null in Vorauszahlung");
			try {
				Buchungdetail bd = em.find(Buchungdetail.class,
						zahlungDto.getBuchungdetailIId());
				habenKontoIId = bd.getKontoIId();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		}

		KontoDto habenKontoDto = null;
		try {
			habenKontoDto = getFinanzFac().kontoFindByPrimaryKey(habenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer kreditorenKontoIId = lieferantDto.getKontoIIdKreditorenkonto();
		KontoDto kreditorenKontoDto = null;
		try {
			kreditorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					kreditorenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		// es muessen alle Konten definiert sein
		if (kreditorenKontoIId == null || habenKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die Zahlung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		// pruefen ob Skonto zu buchen
		boolean bMitSkonto = false;
		boolean bMehrfachSteuersatz = false;
		boolean reverseCharge = isEingangsrechnungDtoReversecharge(rechnungDto) ;
		int mehrfachSteuerBuchungen = 0;
		Integer vstKontoIId[] = new Integer[1];
		Integer skontoKontoIId[] = new Integer[1];
		BigDecimal skontoBetrag = BigDecimal.ZERO ;
		BigDecimal skontoVstBetrag = BigDecimal.ZERO;
		BigDecimal skontoProzent = BigDecimal.ZERO ;
		BigDecimal[] skontoBetragTeil = null;
		BigDecimal[] skontoVstBetragTeil = null;
		BigDecimal kursDifferenzBetrag = BigDecimal.ZERO;
		Integer steuerkategorieIId = null;
		FibuexportDto[] exportDaten = null ;

		ReversechargeartDto rcartOhneDto = null ;
		try {
			rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto) ;
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		
		if (rechnungDto.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			try {
				BigDecimal betrag = rechnungDto.getNBetrag();
				BigDecimal betragBezahlt = getEingangsrechnungFac()
						.getBezahltBetrag(rechnungDto.getIId(), null);
				BigDecimal kursDifferenz = new BigDecimal(1).divide(
						rechnungDto.getNKurs(), 6, BigDecimal.ROUND_HALF_EVEN);
				kursDifferenz = kursDifferenz.subtract(zahlungDto.getNKurs());
				if (kursDifferenz.doubleValue() != 0) {
					kursDifferenzBetrag = Helper.rundeKaufmaennisch(zahlungDto
							.getNBetragfw().multiply(rechnungDto.getNKurs()),
							FinanzFac.NACHKOMMASTELLEN);
					kursDifferenzBetrag = kursDifferenzBetrag
							.subtract(zahlungDto.getNBetrag());
				}
				BigDecimal kursDifferenzGesamt = getEingangsrechnungFac()
						.getBezahltKursdifferenzBetrag(rechnungDto.getIId(),
								rechnungDto.getNKurs());

				if (bBucheMitSkonto) {
					// Achtung: hier nochmals runden, da es aus frueheren Fehler
					// noch 4 stellige Betraege in der DB gibt!
					skontoBetrag = Helper.rundeKaufmaennisch(
							betrag.subtract(betragBezahlt).subtract(
									kursDifferenzGesamt),
							FinanzFac.NACHKOMMASTELLEN);
					if (rechnungDto
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG))
						skontoBetrag = skontoBetrag
								.subtract(getEingangsrechnungFac()
										.getAnzahlungenGestelltZuSchlussrechnung(
												rechnungDto.getIId()));
					skontoProzent = skontoBetrag.divide(betrag, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			exportDaten = getExportDatenRechnung(rechnungDto, theClientDto);

			SteuerkategorieDto stkDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
					exportDaten[0].getKontoDto().getSteuerkategorieCnr(),
					rechnungDto.getReversechargeartId(), 
					exportDaten[0].getKontoDto().getFinanzamtIId(), theClientDto) ;
			if(stkDto == null) {
				throw EJBExcFactory.steuerkategorieFehlt(rechnungDto,
						exportDaten[0].getKontoDto().getFinanzamtIId(), rechnungDto.getReversechargeartId(), exportDaten[0].getKontoDto().getSteuerkategorieCnr()) ;
			}
			steuerkategorieIId = stkDto.getIId() ;
//			steuerkategorieIId = reverseCharge ?
//					exportDaten[0].getKontoDto().getSteuerkategorieIIdReverse() :
//					exportDaten[0].getKontoDto().getSteuerkategorieIId();
			
			if (skontoBetrag.signum() != 0) {
				bMitSkonto = true;
				if (exportDaten.length > 2) {
					bMehrfachSteuersatz = true;
					// aufteilen
					skontoBetragTeil = new BigDecimal[exportDaten.length - 1];
					skontoVstBetragTeil = new BigDecimal[exportDaten.length - 1];
					vstKontoIId = new Integer[exportDaten.length - 1];
					skontoKontoIId = new Integer[exportDaten.length - 1];
					int iMax = 0;
					BigDecimal skontobetragmax = new BigDecimal(0);
					BigDecimal skontobetragsumme = new BigDecimal(0);
					BigDecimal skontovstsumme = new BigDecimal(0);
					for (int i = 1; i < exportDaten.length; i++) {
						skontoBetragTeil[i - 1] = Helper.rundeKaufmaennisch(
								exportDaten[i].getSollbetragBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						BigDecimal skontoVst = Helper.rundeKaufmaennisch(
								exportDaten[i].getSteuerBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						Integer reversechargeartId = getReversechargeartIdFrom(
								exportDaten[i], rechnungDto, rcartOhneDto.getIId()) ;
						reverseCharge = !rcartOhneDto.getIId().equals(reversechargeartId) ;
						if(reverseCharge) {
							skontoBetragTeil[i - 1]= skontoBetragTeil[i - 1].add(skontoVst);
							skontoVstBetragTeil[i-1] = BigDecimal.ZERO ;
						} else  {
							skontoVstBetragTeil[i - 1] = skontoVst;
						}
						if ((skontoBetragTeil[i - 1].subtract(skontobetragmax))
								.doubleValue() > 0) {
							skontobetragmax = skontoBetragTeil[i - 1];
							iMax = i - 1;
						}
						skontobetragsumme = skontobetragsumme
								.add(skontoBetragTeil[i - 1]);
						skontovstsumme = skontovstsumme
								.add(skontoVstBetragTeil[i - 1]);
						if (exportDaten[i].getMwstsatz().getIIMwstsatzbezId() != null) {
							Integer mwstSatzbezIId = exportDaten[i]
									.getMwstsatz().getIIMwstsatzbezId();
	
							stkDto = getFinanzServiceFac()
									.steuerkategorieFindByCNrFinanzamtIId(
											kreditorenKontoDto.getSteuerkategorieCnr(), reversechargeartId, 
											kreditorenKontoDto.getFinanzamtIId(), theClientDto) ;
							if(stkDto == null) {
								throw EJBExcFactory.steuerkategorieFehlt(rechnungDto,
										kreditorenKontoDto.getFinanzamtIId(), reversechargeartId, kreditorenKontoDto.getSteuerkategorieCnr()) ;
//								Reversechargeart reversechargeart = em.find(
//										Reversechargeart.class, reversechargeartId) ;
//								throw new EJBExceptionLP(
//										EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
//										"Keine Steuerkategorie definiert. " 
//												+ exportDaten[i].getBelegart() + ": "
//												+ exportDaten[i].getBelegnummer()
//												+ ", Reversechargeart: " + reversechargeart.getCNr() 
//												+ ", Steuerkategorie: " + kreditorenKontoDto.getSteuerkategorieCnr()) ;
							}
							steuerkategorieIId = stkDto.getIId();
							Timestamp belegDatum = Helper.asTimestamp(
									rechnungDto.getDBelegdatum());
							HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
									stkDto.getIId(), mwstSatzbezIId, belegDatum);
							
							if (stkk.isPresent()) {
								vstKontoIId[i - 1] = stkk.get().getKontoIIdEk();
								skontoKontoIId[i - 1] = stkk
										.get().getKontoIIdSkontoEk();
							} else {
								// wenn nicht ueber Steuerkategorie definiert,
								// dann alte Methode anwenden
								vstKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendUst();
								skontoKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendSkonto();
								if (skontoKontoIId[i - 1] == null)
									skontoKontoIId[i - 1] = getSkontoKontoVst(
											kreditorenKontoDto
													.getSteuerkategorieIId(),
											vstKontoIId[i - 1], belegDatum);
							}
							// Pruefung wirft entspechende Exception
							pruefeSteuerSkontoKonten(
									rechnungDto.getEingangsrechnungartCNr(),
									rechnungDto.getCNr(), vstKontoIId[i - 1],
									skontoKontoIId[i - 1], steuerkategorieIId,
									exportDaten[i]);
						}
						if (skontoVstBetragTeil[i - 1].doubleValue() != 0)
							mehrfachSteuerBuchungen++;
					}
					BigDecimal diff = skontoBetrag.subtract(skontobetragsumme)
							.subtract(skontovstsumme);
					if (diff.doubleValue() != 0) {
						// hoechsten korrigieren
						skontoBetragTeil[iMax] = skontoBetragTeil[iMax]
								.add(diff);
						// ? muss die Steuer auch korrigiert werden?
					}
				} else {
					Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
							.getIIMwstsatzbezId();
					Timestamp belegDatum = Helper
							.asTimestamp(rechnungDto.getDBelegdatum());
					HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
							steuerkategorieIId, mwstSatzbezIId, belegDatum);
					if (stkk.isPresent()) {
						vstKontoIId[0] = stkk.get().getKontoIIdEk();
						skontoKontoIId[0] = stkk.get().getKontoIIdSkontoEk();
					} else {
						vstKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendUst();
						skontoKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendSkonto();
						if (skontoKontoIId[0] == null)
							skontoKontoIId[0] = getSkontoKontoVst(
									kreditorenKontoDto.getSteuerkategorieIId(),
									vstKontoIId[0], belegDatum);
					}
					// Pruefung wirft entspechende Exception
					pruefeSteuerSkontoKonten(
							rechnungDto.getEingangsrechnungartCNr(),
							rechnungDto.getCNr(), vstKontoIId[0],
							skontoKontoIId[0], steuerkategorieIId,
							exportDaten[1]);

					if (!reverseCharge) {
						BigDecimal mwst = new BigDecimal(exportDaten[1]
								.getMwstsatz().getFMwstsatz());
						mwst = mwst.movePointLeft(2);
						skontoVstBetrag = skontoBetrag.multiply(mwst).divide(
								new BigDecimal(1).add(mwst),
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}
		}
		
		BuchungdetailDto[] details;
		if (bMitSkonto) {
			if (bMehrfachSteuersatz) {
				details = new BuchungdetailDto[2 + 2 * skontoBetragTeil.length
						+ mehrfachSteuerBuchungen];
				// SOLL SKONTO Buchung am Skonto Konto
				int j = 2 + skontoBetragTeil.length;
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[j] = BuchungdetailUtils.soll(skontoKontoIId[i], 
							kreditorenKontoIId, skontoBetragTeil[i].negate(), skontoVstBetragTeil[i].negate()) ;
// 					details[j] = new BuchungdetailDto();
//					details[j].setKontoIId(skontoKontoIId[i]);
//					// !! entgegen der Buchungsregel soll lt. wh hier eine
//					// SOLL
//					// Buchung mit - gemacht werden !!
//					// details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//					// details[j].setNBetrag(skontoBetragTeil[i]);
//					details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
//					details[j].setNBetrag(skontoBetragTeil[i].negate());
//					details[j].setNUst(skontoVstBetragTeil[i].negate());
//					details[j].setKontoIIdGegenkonto(kreditorenKontoIId);
					
					j++;
					if (skontoVstBetragTeil[i].signum() != 0) {
						details[j] = BuchungdetailUtils.soll(vstKontoIId[i], 
								kreditorenKontoIId, skontoVstBetragTeil[i].negate(), BigDecimal.ZERO) ;
						
						// Ust Korrekturbuchung
//						details[j] = new BuchungdetailDto();
//						details[j].setKontoIId(vstKontoIId[i]);
//						// !! entgegen der Buchungsregel soll lt. wh hier
//						// eine
//						// SOLL Buchung mit - gemacht werden !!
//						// details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//						// details[j].setNBetrag(skontoUstBetragTeil[i]);
//						details[j]
//								.setBuchungdetailartCNr(BuchenFac.SollBuchung);
//						details[j].setNBetrag(skontoVstBetragTeil[i].negate());
//						details[j].setNUst(new BigDecimal(0));
//						details[j].setKontoIIdGegenkonto(kreditorenKontoIId);
						j++;
					}
				}
			} else {
				details = new BuchungdetailDto[skontoVstBetrag.signum() != 0 ? 5 : 4] ;
				// SOLL SKONTO Buchung am Skonto Konto
				details[2] = BuchungdetailUtils.soll(skontoKontoIId[0],
						kreditorenKontoIId, skontoBetrag.subtract(skontoVstBetrag).negate(), skontoVstBetrag) ;
				
//				details[2] = new BuchungdetailDto();
//				details[2].setKontoIId(skontoKontoIId[0]);
//				// !! entgegen der Buchungsregel soll lt. wh hier eine SOLL
//				// Buchung mit - gemacht werden !!
//				// details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//				// details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag));
//				details[2].setBuchungdetailartCNr(BuchenFac.SollBuchung);
//				details[2].setNBetrag(skontoBetrag.subtract(skontoVstBetrag)
//						.negate());
//				details[2].setNUst(skontoVstBetrag);
//				details[2].setKontoIIdGegenkonto(kreditorenKontoIId);
				
				if (skontoVstBetrag.signum() != 0) {
					details[3] = BuchungdetailUtils.soll(vstKontoIId[0], 
							kreditorenKontoIId, skontoVstBetrag.negate(), BigDecimal.ZERO) ;
					// Ust Korrekturbuchung
//					details[3] = new BuchungdetailDto();
//					details[3].setKontoIId(vstKontoIId[0]);
//					// !! entgegen der Buchungsregel soll lt. wh hier eine
//					// SOLL
//					// Buchung mit - gemacht werden !!
//					// details[3].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//					// details[3].setNBetrag(skontoUstBetrag);
//					details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
//					details[3].setNBetrag(skontoVstBetrag.negate());
//					details[3].setNUst(new BigDecimal(0));
//					details[3].setKontoIIdGegenkonto(kreditorenKontoIId);
				}
			}
		} else {
			details = new BuchungdetailDto[2];
		}
		// HABEN Buchung Bank
		details[0] = BuchungdetailUtils.haben(habenKontoIId, 
				kreditorenKontoIId, zahlungDto.getNBetrag(), zahlungDto.getNBetragust()) ;
//		details[0] = new BuchungdetailDto();
//		details[0].setKontoIId(habenKontoIId);
//		details[0].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//		details[0].setNBetrag(zahlungDto.getNBetrag());
//		details[0].setNUst(zahlungDto.getNBetragust());
//		details[0].setKontoIIdGegenkonto(kreditorenKontoIId);

		// SOLL Buchung am Verbindlichkeiten Kreditor
		if (bMitSkonto) {
			// mit Skonto Buchung aufsplitten
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(kreditorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
			if (bMehrfachSteuersatz) {
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[i + 2] = BuchungdetailUtils.soll(
							kreditorenKontoIId, skontoKontoIId[0], 
							skontoBetragTeil[i].add(skontoVstBetragTeil[i]), skontoVstBetragTeil[i]) ;
					
//					details[i + 2] = new BuchungdetailDto();
//					details[i + 2].setKontoIId(kreditorenKontoIId);
//					details[i + 2]
//							.setBuchungdetailartCNr(BuchenFac.SollBuchung);
//					details[i + 2].setKontoIIdGegenkonto(skontoKontoIId[0]);
//					details[i + 2].setNBetrag(skontoBetragTeil[i]
//							.add(skontoVstBetragTeil[i]));
//					details[i + 2].setNUst(skontoVstBetragTeil[i]);
				}
			} else {
				details[skontoVstBetrag.signum() != 0 ? 4 : 3] = BuchungdetailUtils.soll(
						kreditorenKontoIId, skontoKontoIId[0], skontoBetrag, skontoVstBetrag) ;
				
//				if (skontoVstBetrag.signum() != 0) {
//					details[4] = BuchungdetailUtils.soll(
//							kreditorenKontoIId, skontoKontoIId[0], skontoBetrag, skontoVstBetrag) ;
////					details[4] = new BuchungdetailDto();
////					details[4].setKontoIId(kreditorenKontoIId);
////					details[4].setBuchungdetailartCNr(BuchenFac.SollBuchung);
////					details[4].setKontoIIdGegenkonto(skontoKontoIId[0]);
////					details[4].setNBetrag(skontoBetrag);
////					details[4].setNUst(skontoVstBetrag);
//				} else {
//					details[3] = BuchungdetailUtils.soll(
//							kreditorenKontoIId, skontoKontoIId[0], skontoBetrag, skontoVstBetrag) ;
////					
////					details[3] = new BuchungdetailDto();
////					details[3].setKontoIId(kreditorenKontoIId);
////					details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
////					details[3].setKontoIIdGegenkonto(skontoKontoIId[0]);
////					details[3].setNBetrag(skontoBetrag);
////					details[3].setNUst(skontoVstBetrag);
//				}
			}
		} else {
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(kreditorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		}
		details[1].setNBetrag(zahlungDto.getNBetrag());
		// Zahlung ohne UST
		details[1].setNUst(BigDecimal.ZERO); // zahlungDto.getNBetragUst());
		details[1].setKontoIIdGegenkonto(habenKontoIId);

		List<BuchungdetailDto> kursDetails = createERKursdifferenzDetails(
				kursDifferenzBetrag, rechnungDto, kreditorenKontoDto.getIId(),
				kreditorenKontoDto.getFinanzamtIId(), kreditorenKontoDto.getSteuerkategorieCnr(), 
				exportDaten, rcartOhneDto, theClientDto) ;
		if(kursDetails.size() > 0) {
			int startIndex = details.length ;
			details = Arrays.copyOf(details, startIndex + kursDetails.size());
			for (BuchungdetailDto buchungdetailDto : kursDetails) {
				details[startIndex++] = buchungdetailDto ;
			}
		}
		
// TODO: Kursverluste ER korrekt buchen. Mit welcher Steuerkategorie? Aufteilen bei Mehrfachkontierung? ghp, 16.02.2016
//		if (kursDifferenzBetrag.signum() != 0) {
//			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
//					steuerkategorieIId);
//			Integer kontoKursIId = null;
//			boolean bVerlust = false;
//			if (kursDifferenzBetrag.signum() < 0) {
//				kontoKursIId = steuerkategorie.getKontoIIdKursverlust();
//				if (kontoKursIId == null)
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
//							"Kein Konto fuer Kursverlust."
//									+ rechnungDto.getEingangsrechnungartCNr()
//									+ ": " + rechnungDto.getCNr() + ", "
//									+ "Steuerkategorie: "
//									+ steuerkategorie.getCNr().trim());
//				bVerlust = true;
//			} else {
//				kontoKursIId = steuerkategorie.getKontoIIdKursgewinn();
//				if (kontoKursIId == null)
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
//							"Kein Konto fuer Kursgewinn."
//									+ rechnungDto.getEingangsrechnungartCNr()
//									+ ": " + rechnungDto.getCNr() + ", "
//									+ "Steuerkategorie: "
//									+ steuerkategorie.getCNr().trim());
//			}
//			details = Arrays.copyOf(details, details.length + 2);
//			BuchungdetailDto detailDiff = new BuchungdetailDto();
//			detailDiff.setKontoIId(kontoKursIId);
//			detailDiff.setKontoIIdGegenkonto(kreditorenKontoIId);
//			detailDiff.setNBetrag(kursDifferenzBetrag.abs());
//			detailDiff.setNUst(new BigDecimal(0));
//			if (bVerlust)
//				detailDiff.setBuchungdetailartCNr(BuchenFac.SollBuchung);
//			else
//				detailDiff.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//			details[details.length - 2] = detailDiff;
//			BuchungdetailDto detailDiffGegen = new BuchungdetailDto();
//			detailDiffGegen.setKontoIId(kreditorenKontoIId);
//			detailDiffGegen.setKontoIIdGegenkonto(kontoKursIId);
//			detailDiffGegen.setNBetrag(kursDifferenzBetrag.abs());
//			detailDiffGegen.setNUst(new BigDecimal(0));
//			details[details.length - 1] = detailDiffGegen;
//			if (bVerlust)
//				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
//			else
//				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.SollBuchung);
//		}
		for (int i = 0; i < details.length; i++){
			details[i].setIAuszug(zahlungDto.getIAuszug());
			details[i].setCKommentar(zahlungDto.getCKommentar());
		}
		return details;
	}
	
	class ErKontierungIterator implements Iterable<FibuexportDto>, Iterator<FibuexportDto> {
		private FibuexportDto[] exportDtos ;
		private int index ;
		
		public ErKontierungIterator(FibuexportDto[] exportDtos) {
			Validator.notNull(exportDtos, "exportDtos");
			if(exportDtos.length < 2) throw new IllegalArgumentException("minimum size 2") ;
			
			this.exportDtos = exportDtos ;
			index = 1 ; // [0] enthaelt den HABEN Teil
		}

		public FibuexportDto getHabenExport() {
			return exportDtos[0] ;
		}
		
		@Override
		public Iterator<FibuexportDto> iterator() {
			return this ;
		}
		
		@Override
		public boolean hasNext() {
			return index < exportDtos.length ;
		}
		
		@Override
		public FibuexportDto next() {
			if(hasNext()) {
				return exportDtos[index++] ;				
			}
			
			throw new NoSuchElementException() ;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException() ;
		}
	}
	
	class BetragMitTeilbetrag {
		private BigDecimal betrag ;
		private BigDecimal teilbetrag ;

		public BetragMitTeilbetrag() {
			this(BigDecimal.ZERO, BigDecimal.ZERO) ;
		}
		
		public BetragMitTeilbetrag(BigDecimal b) {
			this(b, BigDecimal.ZERO) ;
		}
		
		public BetragMitTeilbetrag(BigDecimal betrag, BigDecimal teilbetrag) {
			this.setBetrag(betrag) ;
			this.setTeilbetrag(teilbetrag) ;
		}

		public BigDecimal getBetrag() {
			return betrag;
		}

		public void setBetrag(BigDecimal betrag) {
			this.betrag = betrag;
		}

		public BigDecimal getTeilbetrag() {
			return teilbetrag;
		}

		public void setTeilbetrag(BigDecimal teilbetrag) {
			this.teilbetrag = teilbetrag;
		}
	}
	
	class ReversechargeBetraege extends HvCreatingCachingProvider<Integer, BetragMitTeilbetrag> {
		@Override
		protected BetragMitTeilbetrag provideValue(Integer key, Integer transformedKey) {
			return new BetragMitTeilbetrag() ;
		}	
		
		public BigDecimal total() {
			BigDecimal total = BigDecimal.ZERO ;
			for (BetragMitTeilbetrag v : values()) {
				total = total.add(v.getBetrag()) ;
			}
			
			return total ;
		}
		
		public void verteileEven(BigDecimal differenz) {
			BigDecimal myDiff = BigDecimal.ZERO ;
			BigDecimal total = total() ;
			
			for(BetragMitTeilbetrag v : values()) {
				BigDecimal d = v.getBetrag().multiply(differenz)
						.divide(total, FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
				v.setTeilbetrag(d);
				myDiff = myDiff.add(d) ;
			}
			
			verteileRundungsdifferenz(differenz.subtract(myDiff)) ;
		}
		
		private void verteileRundungsdifferenz(BigDecimal differenz) {
			if(differenz.signum() == 0) return ;
			Integer key = getKeyOfBiggestValue();
			if(key != null) {
				BetragMitTeilbetrag b = getValueOfKey(key) ;
				if(b.getTeilbetrag().abs().compareTo(differenz) > 0) {
					b.setTeilbetrag(b.getTeilbetrag().subtract(differenz));
				}
			}
		}
		
		private Integer getKeyOfBiggestValue() {
			BigDecimal dMax = BigDecimal.ZERO ;
			Integer keyMax = null ;
			
			for(Map.Entry<Integer, BetragMitTeilbetrag> entry : entrySet()) {
				if(entry.getValue().getBetrag().abs().compareTo(dMax) > 0) {
					dMax = entry.getValue().getBetrag().abs() ;
					keyMax = entry.getKey(); 
				}
			}

			return keyMax ;
		}
	}
	
	private List<BuchungdetailDto> createERKursdifferenzDetails(
			BigDecimal kursdifferenz, EingangsrechnungDto erDto,
			Integer kreditorenKontoId, Integer finanzamtId, String steuerkategorieCnr, 
			FibuexportDto[] exportDtos, ReversechargeartDto rcartOhneDto, TheClientDto theClientDto) {
		if(kursdifferenz.signum() == 0) return new ArrayList<BuchungdetailDto>() ;

		ReversechargeBetraege betraege = calculateErKontierung(exportDtos, erDto, rcartOhneDto) ;
		betraege.verteileEven(kursdifferenz);
		
		List<BuchungdetailDto> detailDtos = new ArrayList<BuchungdetailDto>();
		for (Map.Entry<Integer, BetragMitTeilbetrag> entry : betraege.entrySet()) {
			detailDtos.addAll(addDetailFrom(
					erDto, kreditorenKontoId, finanzamtId, steuerkategorieCnr, entry, theClientDto)) ;
		}
		
		return detailDtos ;
	}

	private ReversechargeBetraege calculateErKontierung(
			FibuexportDto[] exportDtos, EingangsrechnungDto erDto, ReversechargeartDto rcartOhneDto) {
		ReversechargeBetraege summe = new ReversechargeBetraege() ;
		
		for (FibuexportDto exportDto : new ErKontierungIterator(exportDtos)) {
			Integer reversechargeartId = getReversechargeartIdFrom(
					exportDto, erDto, rcartOhneDto.getIId()) ;
			BetragMitTeilbetrag bt = summe.getValueOfKey(reversechargeartId) ;
			BigDecimal d = bt.getBetrag().add(exportDto.getSollbetragBD()) ;
			bt.setBetrag(d);
		}
		
		return summe ;
	}
	
	private List<BuchungdetailDto> addDetailFrom(
			EingangsrechnungDto erDto, Integer kreditorenKontoId, Integer finanzamtId, String steuerkategorieCnr, 
			Map.Entry<Integer, BetragMitTeilbetrag> entry, TheClientDto theClientDto) {
		if(entry.getValue().getTeilbetrag().signum() == 0) return new ArrayList<BuchungdetailDto>() ;

		SteuerkategorieDto stkDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
				steuerkategorieCnr, entry.getKey(), finanzamtId, theClientDto) ;
		if(stkDto == null) {
			throw EJBExcFactory.steuerkategorieFehlt(erDto, finanzamtId, entry.getKey(), steuerkategorieCnr) ;
		}
		
		boolean verlust = entry.getValue().getTeilbetrag().signum() < 0;
		Integer kontoId = verlust ? stkDto.getKontoIIdKursverlust() : stkDto.getKontoIIdKursgewinn();
		if(kontoId == null) {
			throw EJBExcFactory.steuerkategorieBasisKontoFehlt(verlust ? EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO 
							: EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO, erDto, stkDto) ;
//			throw new EJBSteuerkategorieBasisKontoExceptionLP(
//					verlust ? EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO 
//							: EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO, erDto, stkDto) ;
//			
//			throw new EJBExceptionLP(
//				verlust ? EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO : EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
//				"Kein Konto in der Steuerkategorie definiert"
//								+ erDto.getEingangsrechnungartCNr()
//								+ ": " + erDto.getCNr() + ", "
//								+ "Steuerkategorie: " + steuerkategorieCnr) ;
		}

		BuchungdetailDto detailDiff = new BuchungdetailDto(
				verlust ? BuchenFac.SollBuchung : BuchenFac.HabenBuchung, kontoId, kreditorenKontoId, 
						entry.getValue().getTeilbetrag().abs(), BigDecimal.ZERO);
		BuchungdetailDto gegenDiff = BuchungdetailUtils.createGegenbuchung(detailDiff) ;
		List<BuchungdetailDto> details = new ArrayList<BuchungdetailDto>() ;
		details.add(detailDiff) ;
		details.add(gegenDiff) ;
		return details ;
	}
	
	protected HvOptional<Steuerkategoriekonto> getSteuerkategoriekonto(
			Integer stkIId,Integer mwstsatzbezIId, Timestamp gueltigAm) {
		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		return q.findZuDatum(stkIId, mwstsatzbezIId, gueltigAm);
	}

	private Integer getSkontoKonto(Integer steuerkategorieId,
			Integer ustKontoIId, Timestamp belegDatum) {
		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		List<Steuerkategoriekonto> entries = q.listSteuerkategorie(
				steuerkategorieId, belegDatum);
		for (Steuerkategoriekonto entry : entries) {
			if(ustKontoIId.equals(entry.getKontoIIdVk())) {
				if(entry.getKontoIIdSkontoVk() != null) {
					return entry.getKontoIIdSkontoVk();
				}
			}
		}

		return null;
/*
		Query query = em
				.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		query.setParameter(1, steuerkategorieIId);
		List<Steuerkategoriekonto> list = query.getResultList();
		Iterator<Steuerkategoriekonto> iter = list.iterator();
		while (iter.hasNext()) {
			Steuerkategoriekonto sk = iter.next();
			if (sk.getKontoIIdVk() != null)
				if (sk.getKontoIIdVk().intValue() == ustKontoIId.intValue())
					return sk.getKontoIIdSkontoVk();
		}
		return null;
*/
	}

	private Integer getSkontoKontoVst(
			Integer steuerkategorieId, Integer vstKontoIId, Timestamp belegDatum) {
		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		List<Steuerkategoriekonto> entries = q.listSteuerkategorie(
				steuerkategorieId, belegDatum);
		for (Steuerkategoriekonto entry : entries) {
			if(entry.getKontoIIdVk() != null && 
					vstKontoIId.equals(entry.getKontoIIdEk())) {
				if(entry.getKontoIIdSkontoEk() != null) {
					return entry.getKontoIIdSkontoEk();
				}
			}
		}

		return null;
/*		
		Query query = em
				.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		query.setParameter(1, steuerkategorieIId);
		List<Steuerkategoriekonto> list = query.getResultList();
		Iterator<Steuerkategoriekonto> iter = list.iterator();
		while (iter.hasNext()) {
			Steuerkategoriekonto sk = iter.next();
			if (sk.getKontoIIdVk() != null)
				if (sk.getKontoIIdEk().intValue() == vstKontoIId.intValue())
					return sk.getKontoIIdSkontoEk();
		}
		return null;
*/		
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Gutschrift.
	 *
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonGutschrift(
			Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		Integer erloesberichtigungenKontoIId = kundeDto.getIidErloeseKonto();
		KontoDto erloesberichtigungenKontoDto = null;
		try {
			erloesberichtigungenKontoDto = getFinanzFac()
					.kontoFindByPrimaryKey(erloesberichtigungenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer ustKontoIId = erloesberichtigungenKontoDto
				.getKontoIIdWeiterfuehrendUst();
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || erloesberichtigungenKontoIId == null
				|| ustKontoIId == null) {
			// sind nicht alle Konten definiert, so braucht die gutschrift nicht
			// verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		BuchungdetailDto[] details = new BuchungdetailDto[3];
		BigDecimal zero = new BigDecimal(0);
		// Buchung am Erloesberichtigungskonto
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(erloesberichtigungenKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(rechnungDto.getNWert());
		details[0].setNUst(zero);
		details[0].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am UST-Konto
		details[1] = new BuchungdetailDto();
		details[1].setKontoIId(ustKontoIId);
		details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[1].setNBetrag(rechnungDto.getNWertust());
		details[1].setNUst(zero);
		details[1].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am Kundenkonto
		details[2] = new BuchungdetailDto();
		details[2].setKontoIId(debitorenKontoIId);
		details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[2].setNBetrag(rechnungDto.getNWert().add(
				rechnungDto.getNWertust()));
		details[2].setNUst(rechnungDto.getNWertust());
		details[2].setKontoIIdGegenkonto(erloesberichtigungenKontoIId);
		return details;
	}

	/**
	 * Verbuchen einer Zahlung einer Rechnung bzw. Gutschrift
	 *
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 *
	 *             Anmerkung: Belegbuchung ist in beiden Faellen REZAHLUNG!
	 */
	public BuchungDto verbucheZahlung(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			long startTime = System.currentTimeMillis();
			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_REZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}

			RechnungzahlungDto zahlungDto = getRechnungzahlung(zahlungIId);

			RechnungDto rechnungDto = getRechnungDto(zahlungDto
					.getRechnungIId());

			boolean bezahlt = rechnungDto.isBezahlt();

			HvOptional<RechnungDto> sr = HvOptional.empty();

			if (rechnungDto.isAnzahlungsRechnung()) {
				sr = findSchlussRechnung(rechnungDto.getAuftragIId());				
			}
			
//			HvOptional<BelegbuchungDto> azZahlungBuchung =
//					existsAnzahlungZahlungGegenbuchung(rechnungDto.getIId());
			
			if (bezahlt) {
				// alle Zahlungen aufrollen und bei einer den Skonto buchen
				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
						.zahlungFindByRechnungIIdAbsteigendSortiert(
								rechnungDto.getIId());
				//alle Zahlungen die:
				// -- Skontobuchungen haben
				// -- spaeteres Zahldatum als die zu buchende Zahlung
				// -- die Zahlung ist die ich buchen will
				List<RechnungzahlungDto> alZahlungen = new ArrayList<RechnungzahlungDto>();
				for (RechnungzahlungDto za : zahlungDtos) {
					if (hatZahlungSkonto(za.getIId(), LocaleFac.BELEGART_REZAHLUNG)
							|| !za.getDZahldatum().before(zahlungDto.getDZahldatum())
							|| za.getIId().equals(zahlungIId)) {
						alZahlungen.add(za);
					}
				}
				
				// dann wieder verbuchen mit Ruecksicht auf Skonto
				boolean bBucheMitSkonto = true;
				
				if(sr.isPresent()) {
					// Die nachfolgende for(ZahlungDto:zahlungen) kann leer sein
					verbucheAnzahlungZahlungGegenbuchungRueckgaengig(
							rechnungDto.getIId(), theClientDto);
				}
				
				for (RechnungzahlungDto zDto : alZahlungen) {

					if (!zDto.getIId().equals(zahlungIId)) {
						verbucheZahlungRueckgaengig(zDto.getIId(), theClientDto);
					}

//					HvOptional<RechnungDto> azIgnorieren = HvOptional.empty();
					HvOptional<RechnungDto> azIgnorieren = sr;
					buchungDto = verbucheZahlungSkonto(bBucheMitSkonto, zDto,
							rechnungDto, azIgnorieren, theClientDto);
					bBucheMitSkonto = buchungDto == null && bBucheMitSkonto;
						// Skontobuchung ist jetzt
						// drin, restliche ohne
				}
			} else {
				// nur diese Zahlung verbuchen
				buchungDto = verbucheZahlungSkonto(false, zahlungDto,
						rechnungDto, sr, theClientDto);				
			}
			
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Zahlung dauerte " + dauer + " ms.");
		}
		return buchungDto;
	}

//	public BuchungDto verbucheZahlung(Integer zahlungId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
//		return verbucheZahlung(zahlungId, theClientDto, null);
//	}
//
//	public BuchungDto verbucheZahlung(Integer zahlungIId,
//			TheClientDto theClientDto, String kommentar) throws EJBExceptionLP, RemoteException {
//		BuchungDto buchungDto = null;
//		if (getMandantFac().darfAnwenderAufModulZugreifen(
//				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
//			long startTime = System.currentTimeMillis();
//			// Zahlung schon verbucht?
//			BelegbuchungDto bbDto = null;
//			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
//					LocaleFac.BELEGART_REZAHLUNG, zahlungIId);
//			if (bbDto != null) {
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
//						"Zahlung bereits verbucht. ID=" + zahlungIId);
//			}
//
//			RechnungzahlungDto zahlungDto = getRechnungzahlung(zahlungIId);
//
//			RechnungDto rechnungDto = getRechnungDto(zahlungDto
//					.getRechnungIId());
//
//			boolean bBezahlt = rechnungDto.getStatusCNr().equals(
//					RechnungFac.STATUS_BEZAHLT);
//
//			if (bBezahlt) {
//				// alle Zahlungen aufrollen und bei einer den Skonto buchen
//				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
//						.zahlungFindByRechnungIIdAbsteigendSortiert(
//								rechnungDto.getIId());
//				//alle Zahlungen die:
//				// -- Skontobuchungen haben
//				// -- spaeteres Zahldatum als die zu buchende Zahlung
//				// -- die Zahlung ist die ich buchen will
//				List<RechnungzahlungDto> alZahlungen = new ArrayList<RechnungzahlungDto>();
//				for (RechnungzahlungDto za : zahlungDtos) {
//					if (hatZahlungSkonto(za.getIId(), LocaleFac.BELEGART_REZAHLUNG)
//							|| !za.getDZahldatum().before(zahlungDto.getDZahldatum())
//							|| za.getIId().equals(zahlungIId)) {
//						alZahlungen.add(za);
//					}
//				}
//				// dann wieder verbuchen mit Ruecksicht auf Skonto
//				boolean bBucheMitSkonto = true;
//				for (RechnungzahlungDto zDto : alZahlungen) {
//
//					if (!zDto.getIId().equals(zahlungIId)) {
//						verbucheZahlungRueckgaengig(zDto.getIId(), theClientDto);
//					} else {
//						zDto.setKommentar(kommentar);
//					}
//
//					buchungDto = verbucheZahlungSkonto(bBucheMitSkonto, zDto,
//							rechnungDto, theClientDto);
//					bBucheMitSkonto = buchungDto == null && bBucheMitSkonto;
//						// Skontobuchung ist jetzt
//						// drin, restliche ohne
//				}
//			} else {
//				// nur diese Zahlung verbuchen
////				zahlungDto.setKommentar(kommentar);
//				buchungDto = verbucheZahlungSkonto(false, zahlungDto,
//						rechnungDto, theClientDto);
//			}
//			long dauer = System.currentTimeMillis() - startTime;
//			myLogger.logData("Verbuchen der Zahlung dauerte " + dauer + " ms.");
//		}
//		return buchungDto;
//	}

	/**
	 *
	 * @param zahlungIId
	 * @param belegart LocaleFac.BELEGART_REZAHLUNG oder LocaleFac.BELEGART_ERZAHLUNG
	 * @return true wenn es mindestens eine Skontobuchung f&uuml;r die Zahlung gibt
	 */
	private boolean hatZahlungSkonto(Integer zahlungIId, String belegart) {
		Validator.notNull(zahlungIId, "zahlungIId");
		Validator.notNull(belegart, "belegart");

		String kontofeld = belegart.equals(LocaleFac.BELEGART_REZAHLUNG)?"kontoiidskontovk":"kontoiidskontoek";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query q = session.createQuery("SELECT COUNT(*) " +
				"FROM FLRFinanzBuchungDetail bdetail, FLRFinanzBelegbuchung belegb " +
				"WHERE belegb.flrbuchung.i_id = bdetail.flrbuchung.i_id " +
				"AND belegb.i_belegiid = " + zahlungIId + " " +
				"AND belegb.belegart_c_nr LIKE '" + belegart + "' " +
				"AND bdetail.konto_i_id IN(SELECT stkk." + kontofeld + " FROM FLRSteuerkategoriekonto stkk)");
		Object res = q.uniqueResult();
		return ((Long)res)>0;

	}

	protected RechnungDto getRechnungDto(Integer rechnungIId) {
		RechnungDto rechnungDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return rechnungDto;
	}

	private PartnerDto getPartnerDtoMitDebKonto(Integer kundeIId,
			TheClientDto theClientDto) {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);
		FinanzValidator.debitorkontoDefinition(kundeDto);

		return getPartnerFac().partnerFindByPrimaryKey(
				kundeDto.getPartnerIId(), theClientDto);
	}

	protected RechnungzahlungDto getRechnungzahlung(Integer zahlungIId) {
		RechnungzahlungDto zahlungDto = null;
		try {
			zahlungDto = getRechnungFac().zahlungFindByPrimaryKey(zahlungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return zahlungDto;
	}

	/**
	 * Verbuchen einer Rechnungs- oder Gutschriftszahlung mit Skonto
	 *
	 * @param bBucheMitSkonto
	 * @param zahlungDto
	 * @param rechnungDto
	 * @param theClientDto
	 * @return
	 *
	 *         Anmerkung: Gutschriftszahlung wird nur anhand der Rechnungsart
	 *         erkannt, Betr&auml;ge muessen negiert werden Anmerkung: bei
	 *         Anzahlungsrechnungen werden zus&auml;tzlich die Buchungen auf
	 *         Verrechnungskonto Anzahlung und Erhaltene Anzahlung hinzugefuegt
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 *
	 */
	protected BuchungDto verbucheZahlungSkonto(boolean bBucheMitSkonto,
			RechnungzahlungDto zahlungDto, RechnungDto rechnungDto,
			HvOptional<RechnungDto> schlussrechnung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		
		evLog.info("verbuche", LogEventProducer.create(zahlungDto));

		PartnerDto partnerDto = getPartnerDtoMitDebKonto(
				rechnungDto.getKundeIId(), theClientDto);
		BuchungDto buchungDto = null;
		if (zahlungDto.isBank()
				|| zahlungDto.isBar() || zahlungDto.isVorauszahlung()) {
			buchungDto = new BuchungDto();
			boolean bIstGutschrift = false;
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_GUTSCHRIFT)
					|| rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
				buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_GUTSCHRIFT);
				bIstGutschrift = true;
			} else {
				buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
			}

			buchungDto.setBuchungsartCNr(
				zahlungDto.isBank() || zahlungDto.isVorauszahlung() ?
					FinanzFac.BUCHUNGSART_BANKBUCHUNG : FinanzFac.BUCHUNGSART_KASSENBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());

			BuchungdetailDto[] details = getBuchungdetailsFuerZahlung(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);

			// nur buchen, wenn die Zahlung auch verbucht werden kann
			if (details != null) {
				if (bIstGutschrift) {
					// Betraege negieren
					for (int i = 0; i < details.length; i++) {
						details[i].setNBetrag(details[i].getNBetrag().negate());
						details[i].setNUst(details[i].getNUst().negate());
					}
				}

				try {
					if (rechnungDto.isAnzahlungsRechnung()) {
						// zusaetzliche Buchungen anhaengen
/* SP8069 direkt auf AnzBezahlt buchen						
						details = addAnzahlungZahlungDetails(details,
								rechnungDto, zahlungDto, theClientDto);
*/
					}
					
					if (zahlungDto.isVorauszahlung()) {
						// buchungdetails kommen aus bestehender Umbuchung mit
						// Ausnahme der evt.
						// vorhandenen Waehrungsverlust und Skontobuchungen
						Buchungdetail bd = em.find(Buchungdetail.class,
								zahlungDto.getBuchungdetailIId());

						BuchungdetailDto[] temp = getBuchungDetailsAsNew(bd);
						if(temp.length != 2) {
							/* 
							 * Die Buchung der Vorauszahlung hat keine USt, kein Skonto,
							 * es kann daher nur 2 Buchungsdetails geben. Alles andere 
							 * koennen wir derzeit nicht.
							 * 
							 * Die Vorauszahlungsbuchung gleicht keine Fremdwaehrungsdifferenzen
							 * aus. Das ist auf der Rechnung selbst handzuhaben.
							 */
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG, 
									"Ungueltiger Betrag bei Zahlung aus Vorauszahlung");							
						}
						for (int i = 0; i < temp.length; i++) {
							details[i] = temp[i];
							details[i].setNBetrag(zahlungDto.getNBetrag().add(
									zahlungDto.getNBetragUst()));
							details[i].setCKommentar(zahlungDto.getCKommentar());
						}
						
/*						
						if (details.length != temp.length) {
							// dies ist eine spezielle Buchung, z.B. Skonto auf Rechnung und keine Bankbuchung
							if (bd.getNBetrag().compareTo(zahlungDto.getNBetrag().add(
								zahlungDto.getNBetragUst())) == 0) {
								// nur zulassen wenn der Zahlbetrag gleich dem Buchungsbetrag
								for (BuchungdetailDto buchungdetailDto : temp) {
									buchungdetailDto.setCKommentar(zahlungDto.getCKommentar());
								}
// Damit werden doch die zuvor ermittelten Skonto-Details vernichtet?								
//								details = temp;
// (SP3821) muesste das nicht eher heissen:
								details[0] = temp[0] ;
								details[1] = temp[1] ;
							} else
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG,"Ungueltiger Betrag bei Zahlung aus Vorauszahlung");
						} else {
							for (int i = 0; i < temp.length; i++) {
								details[i] = temp[i];
								details[i].setNBetrag(zahlungDto.getNBetrag().add(
										zahlungDto.getNBetragUst()));
								details[i].setCKommentar(zahlungDto.getCKommentar());
							}
						}
*/						
						// zusaetzlich Vorauszahlung stornieren
						getBuchenFac().storniereBuchung(bd.getBuchungIId(),
								theClientDto);
						// zusaetzlich Rest als Vorauszahlung einbuchen
						bucheDifferenzVorauszahlung(bd.getIId(), zahlungDto
								.getNBetrag().add(zahlungDto.getNBetragUst()),
								theClientDto);
					}


					buchungDto = getBuchenFac().buchen(buchungDto, details,
							rechnungDto.getReversechargeartId(), true, theClientDto);

					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
					
					
					if(rechnungDto.isAnzahlungsRechnung()) {
						if(schlussrechnung.isPresent()) {
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
							Integer steuerkategorieIId = verifySteuerkategorieIId(
									kundeDto.getIidDebitorenkonto(), 
									rechnungDto.getReversechargeartId(), theClientDto) ;

							createGegenbuchungAnzahlungsrechnung(schlussrechnung.get(), rechnungDto, 
									zahlungDto, kundeDto.getIidDebitorenkonto(),
									steuerkategorieIId, theClientDto);
							
//							createSchlussrechnungAnzahlungGegenbuchung(
//									sr.get(), rechnungDto, kundeDto.getIidDebitorenkonto(),
//									zahlungDto, details, theClientDto);
						}
					}
				
					if(schlussrechnung.isPresent()) {
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
						verbucheAnzahlungZahlungGegenbuchungRueckgaengig(
								rechnungDto.getIId(), theClientDto);
						createGegenbuchungOffeneAnzahlungen(
								schlussrechnung.get(), rechnungDto,
								kundeDto.getIidDebitorenkonto(), theClientDto);
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}

		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			BuchungdetailDto[] details = getBuchungdetailsFuerZahlung(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);
			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
			if (details != null) {
				try {
					buchungDto = getBuchenFac().buchen(buchungDto, details,
							rechnungDto.getReversechargeartId(), true, theClientDto);
					// in Tabelle buchungRechnung speichern
					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		} else if(zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			if(!bBucheMitSkonto) return null;
			if(!rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_GUTSCHRIFT)) return null;

			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_GUTSCHRIFT);
//			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());

			BuchungdetailDto[] details = getBuchungdetailsFuerZahlung(
					zahlungDto, rechnungDto, true, theClientDto);
			List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>(Arrays.asList(details));

			if(list.size() > 2) {
				//die ersten zwei sind egal, nur die skontobuchungen sind wichtig
				list.remove(0);
				list.remove(0);
				for(BuchungdetailDto detail : list) {
					detail.setNBetrag(detail.getNBetrag().negate());
				}
			}

			buchungDto = getBuchenFac().buchen(buchungDto, list.toArray(new BuchungdetailDto[0]),
					rechnungDto.getReversechargeartId(), true, theClientDto);
			BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
			belegbuchungDto
					.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
			belegbuchungDto.setBuchungIId(buchungDto.getIId());
			belegbuchungDto.setIBelegiid(zahlungDto.getIId());
			createBelegbuchung(belegbuchungDto, theClientDto);
		}
		return buchungDto;
	}

	/**
	 * Eine Anzahlungszahlung gegenbuchen</br>
	 * <p>Es wurde gerade eine Anzahlungszahlung verbucht (zahlungDetails),
	 * die nach der Erstellung der Schlussrechnung bezahlt wurde.</p>
	 * <p>Da die Schlussrechnungsverbuchung die Anzahlungen gegengebucht hat
	 * (und damit AnzahlungVerrechnet ausgeglichen hat (Saldo 0) und die
	 * Zahlung der Anzahlung ueber das AnzBezahlt/AnzVerrechnet Konto 
	 * laeuft,
	 * muss nun zusaetzlich auch bei der Zahlung eine Gegenbuchung der
	 * Anzahlungskonten (verrechnet, bezahlt) erfolgen, damit das Geld
	 * auch tatsaechlich am Debitor landet und die Anzahlungskonten
	 * ausgeglichen bleiben.</p>
	 * <p>Weil es sein kann, dass die Schlussrechnung doch noch mal 
	 * auf ANGELEGT gestellt wird (Korrektur von Belegdatum, Texten, 
	 * Betraegen), muss sichergestellt werden, dass diese Gegenbuchung
	 * erkannt wird, damit man sie in diese Falle zuruecknehmen kann.</p>
	 * @param azDto
	 * @param zahlungDetails
	 * @param theClientDto
	 * @throws RemoteException
	 */
	private void createSchlussrechnungAnzahlungGegenbuchung(
			RechnungDto srDto, RechnungDto azDto, Integer debitorenKontoId, 
			RechnungzahlungDto zahlungDto, BuchungdetailDto[] zahlungDetails,
			TheClientDto theClientDto) throws RemoteException {
		KontoDto debitorenKontoDto = getFinanzFac()
				.kontoFindByPrimaryKey(debitorenKontoId);
		FinanzamtKontenUebersetzung faKu =
				new FinanzamtKontenUebersetzung(
						debitorenKontoDto.getFinanzamtIId(), 
						debitorenKontoDto.getMandantCNr());
		SachkontoId anzahlungVerrKontoId = faKu
				.getAnzahlungVerrechnet(azDto, theClientDto);
		SachkontoId anzahlungBezKontoId = faKu
				.getAnzahlungBezahlt(azDto, theClientDto);
		
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		
		for(BuchungdetailDto zahlungDetail : zahlungDetails) {
			if(zahlungDetail.getKontoIId().equals(anzahlungBezKontoId.id())) {
				BuchungdetailDto b0 = BuchungdetailDto.soll(
						anzahlungBezKontoId.id(), anzahlungVerrKontoId.id(),
						zahlungDetail.getNBetrag(), zahlungDetail.getNUst());
				list.add(b0);
				BuchungdetailDto b1 = BuchungdetailDto.haben(
						anzahlungVerrKontoId.id(), anzahlungBezKontoId.id(),
						zahlungDetail.getNBetrag(),
						zahlungDetail.getNUst());
				list.add(b1);
			}
		}
		
		if(list.size() > 0) {
			BuchungDto buchungDto = new BuchungDto();
			buchungDto.setAutomatischeBuchung(false);
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_ANZAHLUNGSRECHNUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			buchungDto.setCBelegnummer(srDto.getCNr());
			KundeDto kundeDto = getKundeFac()
					.kundeFindByPrimaryKey(
							srDto.getKundeIId(), theClientDto);
			buchungDto.setCText(kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());

			buchungDto.setDBuchungsdatum(new Date(srDto.getTBelegdatum().getTime()));
			buchungDto.setKostenstelleIId(srDto.getKostenstelleIId());
			
			buchungDto = getBuchenFac().buchen(buchungDto,
					list.toArray(new BuchungdetailDto[0]), 
					srDto.getReversechargeartId(), true, theClientDto);

			BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
			belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_ANZAHLUNGSRECHNUNG);
			belegbuchungDto.setIBelegiid(azDto.getIId());
			belegbuchungDto.setBuchungIId(buchungDto.getIId());
			createBelegbuchung(belegbuchungDto, theClientDto);
		}
	}
	
	private BuchungdetailDto[] addAnzahlungZahlungDetails(
			BuchungdetailDto[] details, RechnungDto rechnungDto,
			RechnungzahlungDto zahlungDto, TheClientDto theClientDto) {
		// folgende Buchunge anhaengen
		// V. Anz an Erh. Anz
		// an Ust
		BuchungdetailDto[] temp;
		boolean bMitUst = false;
		// Achtung: die jetzige Implementierung bucht hier keine Steuer, da laut
		// Werner unsere Anzahlung immer mit Leistung ist,
		// daher schon bei Rechnungsaktivierung die Steuerbuchung erfolgt
		//
		// if (zahlungDto.getNBetragUst().signum()==0)
		temp = new BuchungdetailDto[details.length + 2];
		// else {
		// temp = new BuchungdetailDto[details.length + 3];
		// bMitUst = true;
		// }
		System.arraycopy(details, 0, temp, 0, details.length);
		Konto debitorenkonto = em.find(Konto.class,
				details[0].getKontoIIdGegenkonto());
		FinanzamtPK fpk = new FinanzamtPK(debitorenkonto.getFinanzamtIId(),
				debitorenkonto.getMandantCNr());
		Finanzamt finanzamt = em.find(Finanzamt.class, fpk);
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		boolean reverseCharge = isRechnungDtoReversecharge(rechnungDto) ;

		Integer kontoIIdAnzahlungErhaltenVerr = finanzamt
				.getKontoIIdAnzahlungErhaltenVerr();
		Integer kontoIIdAnzahlungErhaltenBezahlt = finanzamt
				.getKontoIIdAnzahlungErhaltenBezahlt();

		// Kontouebersetzung 
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
							.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
					.getFibuExportManager(getExportVariante(theClientDto),
									getExportFormat(theClientDto), fibuExportKriterienDto,
									theClientDto);
		KontoDto kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungErhaltenVerr, rechnungDto.getIId()) ;
		kontoIIdAnzahlungErhaltenVerr = kontoDto.getIId() ;
		kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungErhaltenBezahlt, rechnungDto.getIId()) ;
		kontoIIdAnzahlungErhaltenBezahlt = kontoDto.getIId() ;
		
		temp[i].setKontoIId(kontoIIdAnzahlungErhaltenVerr);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenBezahlt);
		if (bMitUst || reverseCharge) {
			temp[i].setNBetrag(zahlungDto.getNBetrag().add(
					zahlungDto.getNBetragUst()));
			temp[i].setNUst(zahlungDto.getNBetragUst());
		} else {
			temp[i].setNBetrag(zahlungDto.getNBetrag());
			temp[i].setNUst(new BigDecimal(0));
		}
		i++;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		temp[i].setKontoIId(kontoIIdAnzahlungErhaltenBezahlt);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenVerr);
		temp[i].setNBetrag(zahlungDto.getNBetrag());
		temp[i].setNUst(zahlungDto.getNBetragUst());
		if (bMitUst) {
			i++;
			FibuexportDto[] exportDaten = getExportDatenRechnung(rechnungDto,
					theClientDto);
			SteuerkategorieDto stkDto = getFinanzServiceFac()
					.steuerkategorieFindByCNrFinanzamtIId(
							exportDaten[0].getKontoDto().getSteuerkategorieCnr(), 
							rechnungDto.getReversechargeartId(), 
							exportDaten[0].getKontoDto().getFinanzamtIId(), theClientDto) ;
//			SteuerkategorieDto stkDto = getSteuerkategorieIdFromSteuerkategorieId(
//					rechnungDto.getReversechargeartId(), exportDaten[0]
//							.getKontoDto().getSteuerkategorieIId(), theClientDto) ;
//			Steuerkategorie stk = em.find(Steuerkategorie.class, exportDaten[0]
//					.getKontoDto().getSteuerkategorieIId());
			Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
					.getIIMwstsatzbezId();
			HvOptional<Steuerkategoriekonto> stkk = getSteuerkategoriekonto(
					stkDto.getIId(), mwstSatzbezIId, rechnungDto.getTBelegdatum());
			if(!stkk.isPresent()) {
				throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(
						rechnungDto, stkDto, mwstSatzbezIId);
			}
			temp[i] = new BuchungdetailDto();
			temp[i].setBuchungIId(details[0].getBuchungIId());
			temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			temp[i].setKontoIId(stkk.get().getKontoIIdVk());
			temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenVerr);
			temp[i].setNBetrag(zahlungDto.getNBetragUst());
			temp[i].setNUst(new BigDecimal(0));
		}
		return temp;
	}

	/**
	 * Automatisches Uebernehmen einer ER in die FiBu
	 *
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public BuchungDto verbucheEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		long startTime = System.currentTimeMillis();

		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto bbDto = null;
			try {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								eingangsrechnungIId);
				if (bbDto != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
							"Beleg bereits verbucht: ID=" + eingangsrechnungIId);
				}
			} catch (RemoteException ex2) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex2);
			}

			EingangsrechnungDto eingangsrechnungDto = null;
			try {
				eingangsrechnungDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}

			evLog.info("verbuche", LogEventProducer.create(eingangsrechnungDto));
			
			FibuexportDto[] exportDaten = getExportDatenRechnung(
					eingangsrechnungDto, theClientDto);

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							eingangsrechnungDto.getLieferantIId(),
							theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					lieferantDto.getPartnerIId(), theClientDto);
			FinanzValidator.kreditorkontoDefinition(lieferantDto);
			Konto kreditorKonto = em.find(Konto.class,
					lieferantDto.getKontoIIdKreditorenkonto());
			// if (lieferantDto.getKontoIIdWarenkonto() == null) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEIN_WARENKONTO_DEFINIERT,
			// "Kein Warenkonto: Lieferant ID=" + lieferantDto.getIId());
			// }
			buchungDto = new BuchungDto();
			buchungDto.setCBelegnummer(eingangsrechnungDto.getCNr());
			Boolean lfReNrAlsText = getParameterFac().getMandantparameter(
					eingangsrechnungDto.getMandantCNr(),
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT)
					.asBoolean();
			buchungDto.setCText((lfReNrAlsText ? eingangsrechnungDto
					.getCLieferantenrechnungsnummer() + " " : "")
					+ (eingangsrechnungDto.getCText() == null ? partnerDto
							.getCName1nachnamefirmazeile1()
							: eingangsrechnungDto.getCText()));

			buchungDto.setDBuchungsdatum(eingangsrechnungDto.getDBelegdatum());
			buchungDto.setKostenstelleIId(eingangsrechnungDto
					.getKostenstelleIId());
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_EINGANGSRECHNUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

			BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
			List<BuchungdetailDto> details = null;
			boolean reverseCharge = isEingangsrechnungDtoReversecharge(eingangsrechnungDto) ;

			if(Helper.isStringEmpty(kreditorKonto.getSteuerkategorieCNr())) {
				KontoDto kreditorDto = KontoDtoAssembler.createDto(kreditorKonto) ;
				throw EJBExcFactory.steuerkategorieDefinitionFehlt(eingangsrechnungDto, kreditorDto) ;
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
//						"Keine Steuerkategorie bei Kreditorenkonto "
//								+ kreditorKonto.getCNr(), kreditorKonto
//								.getCNr() + " " + kreditorKonto.getCBez());
			}
			SteuerkategorieDto stkDto = 
					getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
							kreditorKonto.getSteuerkategorieCNr(), 
							eingangsrechnungDto.getReversechargeartId(), 
							kreditorKonto.getFinanzamtIId(), theClientDto) ;
			Integer steuerkategorieIId = stkDto == null ? null : stkDto.getIId() ;
			
//			Integer steuerkategorieIId = reverseCharge ? kreditorKonto
//					.getSteuerkategorieIIdReverse() : kreditorKonto
//					.getSteuerkategorieIId();
			if (steuerkategorieIId == null) {
				throw EJBExcFactory.steuerkategorieFehlt(eingangsrechnungDto, 
						kreditorKonto.getFinanzamtIId(), eingangsrechnungDto.getReversechargeartId(), kreditorKonto.getSteuerkategorieCNr()) ;
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
//						"Keine Steuerkategorie bei Kreditorenkonto "
//								+ kreditorKonto.getCNr(), kreditorKonto
//								.getCNr() + " " + kreditorKonto.getCBez() + ", Steuerkategorie " + kreditorKonto.getSteuerkategorieCNr());
			}
//			details = getBuchungdetailsVonExportDtos(exportDaten,
//					steuerkategorieIId, null, null, false,
//					Helper.short2boolean(eingangsrechnungDto.getBIgErwerb()),
//					reverseCharge, theClientDto);
			details = getBuchungdetailsVonERExportDtos(exportDaten, eingangsrechnungDto,
					kreditorKonto.getSteuerkategorieCNr(), kreditorKonto.getFinanzamtIId(), theClientDto) ;
			if (eingangsrechnungDto.isEingangsrechnung()
					|| eingangsrechnungDto.isZusatzkosten()
					|| eingangsrechnungDto.isGutschrift() 
					|| eingangsrechnungDto.isAnzahlung()
					|| eingangsrechnungDto.isSchlussrechnung()) {
				belegbuchungDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			} else {
				myLogger.error("Ungueltiger Eingangsrechnungstyp, keine Verbuchung moeglich");
				evLog.error("Ungueltiger Eingangsrechnungstyp", LogEventProducer.create(eingangsrechnungDto));
				// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FIN, eI)
			}
			
			// nur buchen, wenn die Eingangsrechnung auch verbucht werden kann
			if (details != null) {
				try {
					if (eingangsrechnungDto.isSchlussrechnung()) {
						// zusaetzlich alle Anzahlungsrechnungen ausbuchen
						List<BuchungdetailDto> anzahlungdetailDtos = getDetailsSchlussrechnungAnzahlungen(
								eingangsrechnungDto, theClientDto);
						details.addAll(anzahlungdetailDtos) ;
					}

					// Das Verbuchen einer Eingangsrechnung muss immer den
					// Buchungsregeln entsprechen
					//
					// Kostenstelle aus erster Buchung wenn Splittbuchung
					if (buchungDto.getKostenstelleIId() == null) {
						for (int i = 0; i < exportDaten.length; i++) {
							if (exportDaten[i].getKostDto() != null) {
								if (exportDaten[i].getKostDto().getIId() != null) {
									buchungDto
											.setKostenstelleIId(exportDaten[i]
													.getKostDto().getIId());
									break;
								}
							}
						}
					}
					BuchungdetailDto[] detailDtos = details.toArray(new BuchungdetailDto[0]) ;	
//					HelperServer.printBuchungssatz(detailDtos, getFinanzFac(), System.out);
					buchungDto = getBuchenFac().buchen(buchungDto,
							detailDtos, eingangsrechnungDto.getReversechargeartId(), true, theClientDto);
//					if (detailsAll != null)
//						buchungDto = getBuchenFac().buchen(buchungDto,
//								detailsAll, eingangsrechnungDto.getReversechargeartId(), true, theClientDto);
//					else
//						buchungDto = getBuchenFac().buchen(buchungDto, details,
//								eingangsrechnungDto.getReversechargeartId(), true, theClientDto);
					// in Tabelle buchungRechnung speichern
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(eingangsrechnungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
					// Status der Rechnung auf verbucht setzen
					// AD: nicht auf verbucht setzen, macht erst die UVA
					// rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT);
					// getEingangsrechnungFac().updateEingangsrechnung(eingangsrechnungDto,
					// theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Eingangsrechnung dauerte " + dauer
					+ " ms.");
		}

		return buchungDto;
	}

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenER(
			Integer erIId) throws EJBExceptionLP, RemoteException {
		List<BelegbuchungDto> belegbuchungen = new ArrayList<BelegbuchungDto>();
		for (EingangsrechnungzahlungDto erz : getEingangsrechnungFac()
				.eingangsrechnungzahlungFindByEingangsrechnungIId(erIId)) {
			BelegbuchungDto buchung;
			if (erz.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_REZAHLUNG,
						erz.getRechnungzahlungIId());
			} else if (erz.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						erz.getEingangsrechnungIIdGutschrift());
			} else {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_ERZAHLUNG, erz.getIId());
			}
			if (buchung != null)
				belegbuchungen.add(buchung);
		}
		BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_EINGANGSRECHNUNG, erIId);
		if (buchung != null)
			belegbuchungen.add(buchung);
		return belegbuchungen;
	}

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenAR(
			Integer arIId) throws EJBExceptionLP, RemoteException {
		List<BelegbuchungDto> belegbuchungen = new ArrayList<BelegbuchungDto>();
		for (RechnungzahlungDto rez : getRechnungFac()
				.zahlungFindByRechnungIId(arIId)) {
			// if(rez.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG))
			// {
			// buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
			// LocaleFac.BELEGART_REZAHLUNG, rez.getEingangsrechnungIId());
			// } else
			if (rez.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_RECHNUNG,
						rez.getRechnungIIdGutschrift());
				if(buchung != null)
					belegbuchungen.add(buchung);
			}
			BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_REZAHLUNG, rez.getIId());
			if (buchung != null)
				belegbuchungen.add(buchung);
		}
		BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_RECHNUNG, arIId);
		if (buchung == null) { // dann ist es eine Gutschrift
			buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_GUTSCHRIFT, arIId);
		}
		if (buchung != null)
			belegbuchungen.add(buchung);
		return belegbuchungen;
	}

/*	
	public void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
			TheClientDto theClientDto,
			List<BelegbuchungDto> belegbuchungen) throws EJBExceptionLP,
			RemoteException {
//		belegbuchungenAusziffernWennNoetig(kontoIId, theClientDto,
//				belegbuchungen.toArray(new BelegbuchungDto[0]));
		String belegnummer = null;
		boolean ausziffernNoetig = false;

		List<BuchungdetailDto> buchungdetails = new ArrayList<BuchungdetailDto>();

		for (BelegbuchungDto belegbuchung : belegbuchungen) {
			BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(
					belegbuchung.getBuchungIId());

			buchungdetails.addAll(getBuchenFac()
					.buchungdetailsFindByKontoIIdBuchungIId(kontoIId,
							buchung.getIId()));
			if (belegnummer == null) {
				belegnummer = buchung.getCBelegnummer().trim() ;
			} else {
				if (!belegnummer.equals(buchung.getCBelegnummer().trim())) {
					ausziffernNoetig = true;
				}
			}
		}

		List<Integer> iidsOhneAZK = new ArrayList<Integer>();
		Integer vorhandenesAZK = null;
		for (BuchungdetailDto buchungdetailDto : buchungdetails) {
			Integer azk = buchungdetailDto.getIAusziffern();
			if (azk != null) {
				if (vorhandenesAZK == null) {
					vorhandenesAZK = azk;
				} else if (!azk.equals(vorhandenesAZK)) {
					EJBExceptionLP exce = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BUCHUNGDETAIL_VERSCHIEDENE_AZK_VORHANDEN,
							"");
					exce.setAlInfoForTheClient(new ArrayList<Object>(Arrays
							.asList(vorhandenesAZK, buchungdetailDto)));
				}
			} else {
				iidsOhneAZK.add(buchungdetailDto.getIId());
			}
		}

		if (vorhandenesAZK == null && ausziffernNoetig) {
			getFinanzServiceFac().createAuszifferung(
					iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		} else {
			getFinanzServiceFac().updateAuszifferung(vorhandenesAZK,
					iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		}
	}
*/

	public void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
		Integer geschaeftsjahrVerursacher, List<BelegbuchungDto> belegbuchungen,
		TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		String belegnummer = null;
		boolean ausziffernNoetig = false;

		List<BuchungdetailDto> buchungdetails = new ArrayList<BuchungdetailDto>();

		for (BelegbuchungDto belegbuchung : belegbuchungen) {
			BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(
					belegbuchung.getBuchungIId());

			buchungdetails.addAll(getBuchenFac()
					.buchungdetailsFindByKontoIIdBuchungIId(kontoIId,
							buchung.getIId()));
			if (belegnummer == null) {
				belegnummer = buchung.getCBelegnummer().trim() ;
			} else {
				if (!belegnummer.equals(buchung.getCBelegnummer().trim())) {
					ausziffernNoetig = true;
				}
			}
		}

		List<Integer> iidsOhneAZK = new ArrayList<Integer>();
		Integer vorhandenesAZK = null;
		for (BuchungdetailDto buchungdetailDto : buchungdetails) {
			Integer azk = buchungdetailDto.getIAusziffern();
			if (azk != null) {
				if (vorhandenesAZK == null) {
					vorhandenesAZK = azk;
				} else if (!azk.equals(vorhandenesAZK)) {
					EJBExceptionLP exce = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BUCHUNGDETAIL_VERSCHIEDENE_AZK_VORHANDEN,
							"");
					exce.setAlInfoForTheClient(new ArrayList<Object>(Arrays
							.asList(vorhandenesAZK, buchungdetailDto)));
				}
			} else {
				iidsOhneAZK.add(buchungdetailDto.getIId());
			}
		}

		if (vorhandenesAZK == null && ausziffernNoetig) {
			getFinanzServiceFac().createAuszifferung(
					geschaeftsjahrVerursacher, iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		} else {
			getFinanzServiceFac().updateAuszifferung(geschaeftsjahrVerursacher,
					vorhandenesAZK, iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		}
	}

/*	
	private void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
			TheClientDto theClientDto,
			BelegbuchungDto... belegbuchungen) throws EJBExceptionLP,
			RemoteException {
		String belegnummer = null;
		boolean ausziffernNoetig = false;

		List<BuchungdetailDto> buchungdetails = new ArrayList<BuchungdetailDto>();

		for (BelegbuchungDto belegbuchung : belegbuchungen) {
			BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(
					belegbuchung.getBuchungIId());

			buchungdetails.addAll(getBuchenFac()
					.buchungdetailsFindByKontoIIdBuchungIId(kontoIId,
							buchung.getIId()));
			if (belegnummer == null) {
				belegnummer = buchung.getCBelegnummer().trim() ;
			} else {
				if (!belegnummer.equals(buchung.getCBelegnummer().trim())) {
					ausziffernNoetig = true;
				}
			}
		}

		List<Integer> iidsOhneAZK = new ArrayList<Integer>();
		Integer vorhandenesAZK = null;
		for (BuchungdetailDto buchungdetailDto : buchungdetails) {
			Integer azk = buchungdetailDto.getIAusziffern();
			if (azk != null) {
				if (vorhandenesAZK == null) {
					vorhandenesAZK = azk;
				} else if (!azk.equals(vorhandenesAZK)) {
					EJBExceptionLP exce = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BUCHUNGDETAIL_VERSCHIEDENE_AZK_VORHANDEN,
							"");
					exce.setAlInfoForTheClient(new ArrayList<Object>(Arrays
							.asList(vorhandenesAZK, buchungdetailDto)));
				}
			} else {
				iidsOhneAZK.add(buchungdetailDto.getIId());
			}
		}

		if (vorhandenesAZK == null && ausziffernNoetig) {
//			getFinanzServiceFac().createAuszifferung(
//					iidsOhneAZK.toArray(new Integer[0]), null);
			getFinanzServiceFac().createAuszifferung(
					iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		} else {
//			getFinanzServiceFac().updateAuszifferung(vorhandenesAZK,
//					iidsOhneAZK.toArray(new Integer[0]), null);
			getFinanzServiceFac().updateAuszifferung(vorhandenesAZK,
					iidsOhneAZK.toArray(new Integer[0]), theClientDto);
		}
	}
*/
	private List<BuchungdetailDto> getDetailsSchlussrechnungAnzahlungen(
			EingangsrechnungDto eingangsrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		EingangsrechnungDto[] anzRechnungen = getEingangsrechnungFac()
				.eingangsrechnungFindByBestellungIId(
						eingangsrechnungDto.getBestellungIId());
		for (int i = 0; i < anzRechnungen.length; i++) {
			if (anzRechnungen[i].isStorniert()) continue;
			
			evLog.info(LogEventProducer.create(anzRechnungen[i]));
			
			if (anzRechnungen[i].isAnzahlung()
					&& anzRechnungen[i].getNBetragfw() != null) {
				boolean reverseCharge = isEingangsrechnungDtoReversecharge(anzRechnungen[i]);
				boolean bMitUst = (anzRechnungen[i].getNUstBetrag().signum() != 0) && !reverseCharge;

				BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						anzRechnungen[i].getIId());

				BuchungdetailDto[] detailDtos = getBuchenFac()
						.buchungdetailsFindByBuchungIId(
								belegRech.getBuchungIId());
//				HelperServer.printBuchungssatz(detailDtos, getFinanzFac(), System.out);

				BuchungdetailDto anKreditor = detailDtos[0];
				anKreditor.setNBetrag(anKreditor.getNBetrag().negate());
				list.add(anKreditor);
				if(bMitUst) {
					BuchungdetailDto perVst = detailDtos[2];
					perVst.setNBetrag(perVst.getNBetrag().negate());
					list.add(perVst);
				}

				if(EingangsrechnungFac.STATUS_ERLEDIGT.equals(anzRechnungen[i].getStatusCNr())) {
					EingangsrechnungzahlungDto[] zahlungDtos = getEingangsrechnungFac()
							.eingangsrechnungzahlungFindByEingangsrechnungIId(
									anzRechnungen[i].getIId());
					for (int j = 0; j < zahlungDtos.length; j++) {
						BelegbuchungDto bbDto = getBelegbuchungFac(
								theClientDto.getMandant())
								.belegbuchungFindByBelegartCNrBelegiid(
										LocaleFac.BELEGART_ERZAHLUNG,
										zahlungDtos[j].getIId());
						BuchungdetailDto[] zahlungDetailDtos = getBuchenFac()
								.buchungdetailsFindByBuchungIId(
										bbDto.getBuchungIId());

						BuchungdetailAnalyzer analyzer =
								new BuchungdetailERAnalyzer(
										Helper.asTimestamp(anzRechnungen[i].getDBelegdatum()),
										detailDtos, zahlungDetailDtos, theClientDto);
						BuchungdetailAnzahlungVisitor visitor = 
								new BuchungdetailERAnzahlungVisitor();
						analyzer.evalAnzahlung(visitor);
						List<BuchungdetailDto> anzDetails = visitor.getDetails();
						list.addAll(anzDetails);							
						
//						if(1 == 0) {
//							boolean skonto = detailDtos.length > 5;
//							BuchungdetailDto anAnzBez = detailDtos[detailDtos.length-(skonto?4:3)];
//							anAnzBez.swapSollHaben();
//							anAnzBez.setKontoIIdGegenkonto(null);
//							list.add(anAnzBez);
//
//							if(skonto) {
//								BuchungdetailDto anAnzVerr = detailDtos[detailDtos.length-3];
//								anAnzVerr.setNBetrag(detailDtos[2].getNBetrag().negate());
//								anAnzVerr.setKontoIIdGegenkonto(null);
//								list.add(anAnzVerr);
//							}							
//						}
	//					BuchungdetailDto[] temp;
	//					if (bMitUst)
	//						temp = new BuchungdetailDto[3];
	//					else
	//						temp = new BuchungdetailDto[2];
	//					temp[0] = (BuchungdetailDto) detailDtos[1].clone();
	//					temp[0].swapSollHaben();
	//					temp[0].setNBetrag(temp[0].getNBetrag().negate());
	//					temp[0].setNUst(temp[0].getNUst().negate());
	//					temp[1] = (BuchungdetailDto) detailDtos[detailDtos.length - 3]
	//							.clone();
	//					temp[0].setKontoIIdGegenkonto(temp[1].getKontoIId());
	//					temp[1].swapSollHaben();
	//					temp[1].setKontoIIdGegenkonto(temp[0].getKontoIId());
	//					if (bMitUst) {
	//						temp[2] = getSteuerBuchungAnzahlungsrechnung(
	//								anzRechnungen[i], theClientDto);
	//						temp[2].swapSollHaben();
	//						temp[2].setKontoIIdGegenkonto(temp[0].getKontoIId());
	//					}
	//					for (int k = 0; k < temp.length; k++) {
	//						temp[k].setIId(null);
	//						temp[k].setBuchungIId(null);
	//						list.add(temp[k]);
	//					}
					}
				} else {
					EingangsrechnungzahlungDto fakeZahlung = new EingangsrechnungzahlungDto();
					fakeZahlung.setNBetrag(anzRechnungen[i].getNBetrag()); //Die Zahlung gibts noch nicht wirklich,
					fakeZahlung.setNBetragust(anzRechnungen[i].getNUstBetrag()); // aber wir brauchen sie, um die Buchungsdetails zu generieren
					BuchungdetailDto[] details = new BuchungdetailDto[]{new BuchungdetailDto()};
					details[0].setKontoIId(anKreditor.getKontoIIdGegenkonto());
					details = addAnzahlungZahlungDetails(details, anzRechnungen[i], fakeZahlung, theClientDto);

//					BuchungdetailDto anAnzBez = detailDtos[detailDtos.length-(3)];
					BuchungdetailDto anAnzBez = details[2] ;
//					anAnzBez.swapSollHaben();
					anAnzBez.setKontoIIdGegenkonto(null);
					list.add(anAnzBez);
				}
			}
		}
		for(BuchungdetailDto dto : list) {
			dto.setIId(null);
			dto.setBuchungIId(null);
			dto.setIAusziffern(null);
			dto.setIAuszug(null);
		}
//		HelperServer.printBuchungssatz(list, getFinanzFac(), System.out);
		return list;
	}

	private BuchungdetailDto getSteuerBuchungAnzahlungsrechnung(
			EingangsrechnungDto eingangsrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// Anzahlungsrechnung hat KEINE mehrfach Kontierung, daher ist die
		// Position fix
		BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						eingangsrechnungDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bbDto.getBuchungIId());
		return detailDtos[detailDtos.length - 2];
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 *
	 * @param eingangsrechnungIId die IId der Eingangsrechnung
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void verbucheEingangsrechnungRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto buchungRechnungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_EINGANGSRECHNUNG, eingangsrechnungIId);
			if (buchungRechnungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungRechnungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungRechnungDto.getBuchungIId();
				removeBelegbuchung(buchungRechnungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
			}
		}
	}

	/**
	 * Verbuchen der Zahlung einer Eingangsrechnung
	 *
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public BuchungDto verbucheZahlungEr(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			long startTime = System.currentTimeMillis();
			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}
			EingangsrechnungzahlungDto zahlungDto = getEingangsrechnungzahlung(zahlungIId);
			EingangsrechnungDto rechnungDto = getEingangsrechnungDto(zahlungDto
					.getEingangsrechnungIId());

			boolean bBezahlt = rechnungDto.getStatusCNr().equals(
					EingangsrechnungFac.STATUS_ERLEDIGT);

			if (bBezahlt) {
				// alle Zahlungen aufrollen und bei einer den Skonto buchen
				EingangsrechnungzahlungDto[] zahlungDtos = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByEingangsrechnungIId(
								rechnungDto.getIId());
				//alle Zahlungen die:
				// -- Skontobuchungen haben
				// -- spaeteres Zahldatum als die zu buchende Zahlung
				// -- die Zahlung ist die ich buchen will
				List<EingangsrechnungzahlungDto> alZahlungen = new ArrayList<EingangsrechnungzahlungDto>();
				for (EingangsrechnungzahlungDto za : zahlungDtos) {
					if(hatZahlungSkonto(za.getIId(), LocaleFac.BELEGART_ERZAHLUNG)
							|| !za.getTZahldatum().before(zahlungDto.getTZahldatum())
							|| za.getIId().equals(zahlungIId)) {
						alZahlungen.add(za);
					}
				}
				// dann wieder verbuchen mit Ruecksicht auf Skonto
				boolean bBucheMitSkonto = true;
				for (EingangsrechnungzahlungDto zDto : alZahlungen) {
					if(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG.equals(zDto.getZahlungsartCNr())) {
						// Eine AR die mit einer ER gegenverrechnet wird, darf in der 
						// ER nicht verbucht werden
						continue;						
					}
					
					if(!zDto.getIId().equals(zahlungIId)) {
						verbucheZahlungErRueckgaengig(zDto, theClientDto);
						// verbucheZahlungRueckgaengig veraendert die buchungdetailId der Zahlung
						zDto = getEingangsrechnungFac()
								.eingangsrechnungzahlungFindByPrimaryKey(zDto.getIId());
					}
					buchungDto = verbucheZahlungErSkonto(bBucheMitSkonto, zDto,
							rechnungDto, theClientDto);
					bBucheMitSkonto = buchungDto == null && bBucheMitSkonto;
						// Skontobuchung ist jetzt
						// drin, restliche ohne
				}
			} else {
				// nur diese Zahlung verbuchen
				buchungDto = verbucheZahlungErSkonto(false, zahlungDto,
						rechnungDto, theClientDto);
			}
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Zahlung dauerte " + dauer + " ms.");
		}
		return buchungDto;
	}

	protected BuchungDto verbucheZahlungErSkonto(boolean bBucheMitSkonto,
			EingangsrechnungzahlungDto zahlungDto,
			EingangsrechnungDto rechnungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		myLogger.info("verbucheZahlungErSkonto:" + 
			bBucheMitSkonto + ", " + zahlungDto.toString());
		PartnerDto partnerDto = getPartnerDtoMitKredKonto(
				rechnungDto.getLieferantIId(), theClientDto);
		BuchungDto buchungDto = null;
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_BAR)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			BuchungdetailDto[] details = getBuchungdetailsVonZahlungEr(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);
			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_EINGANGSRECHNUNG);
			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_BANK)
					|| zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
					|| zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG))
				buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			else
				buchungDto
						.setBuchungsartCNr(FinanzFac.BUCHUNGSART_KASSENBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			Boolean lfReNrAlsText = getParameterFac().getMandantparameter(
					rechnungDto.getMandantCNr(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT)
					.asBoolean();
			buchungDto.setCText((lfReNrAlsText ? rechnungDto
					.getCLieferantenrechnungsnummer() + " " : "")
					+ (rechnungDto.getCText() == null ? partnerDto
							.getCName1nachnamefirmazeile1() : rechnungDto
							.getCText()));
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getTZahldatum()
					.getTime()));
			if (rechnungDto.getKostenstelleIId() != null) {
				buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
			} else {
				// Kostenstelle aus Buchung der Rechnung holen Splittbuchung
				BelegbuchungDto bbRechnungDto;
				try {
					bbRechnungDto = belegbuchungFindByBelegartCNrBelegiid(
							LocaleFac.BELEGART_EINGANGSRECHNUNG,
							zahlungDto.getEingangsrechnungIId());
					Buchung buchung = em.find(Buchung.class,
							bbRechnungDto.getBuchungIId());
					buchungDto.setKostenstelleIId(buchung.getKostenstelleIId());
				} catch (Exception e1) {
					// Rechnung nicht gebucht da evt. in Vorjahr oder
					// anderer Periode
					// Kostenstelle aus Kontierung holen
					EingangsrechnungKontierungDto[] erkontierungDtos = null;
					try {
						erkontierungDtos = getEingangsrechnungFac()
								.eingangsrechnungKontierungFindByEingangsrechnungIId(
										zahlungDto.getEingangsrechnungIId());
						for (int i = 0; i < erkontierungDtos.length; i++) {
							if (erkontierungDtos[i].getKostenstelleIId() != null) {
								buchungDto
										.setKostenstelleIId(erkontierungDtos[i]
												.getKostenstelleIId());
								break;
							}
						}
					} catch (RemoteException e) {
						//
					}
				}
			}

			// nur buchen, wenn die Zahlung auch verbucht werden kann
			if (details != null) {
				try {
					if (rechnungDto.getEingangsrechnungartCNr().equals(
							EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG))
						// zusaetzliche Buchungen anhaengen
						details = addAnzahlungZahlungDetails(details,
								rechnungDto, zahlungDto, theClientDto);

					if (zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
						// buchungdetails kommen aus bestehender Umbuchung mit
						// Ausnahme der evt.
						// vorhandenen Waehrungsverlust und Skontobuchungen
						Buchungdetail bd = em.find(Buchungdetail.class,
								zahlungDto.getBuchungdetailIId());
						BuchungdetailDto[] temp = getBuchungDetailsAsNew(bd);
						if(temp.length != 2) {
							/* 
							 * Die Buchung der Vorauszahlung hat keine USt, kein Skonto,
							 * es kann daher nur 2 Buchungsdetails geben. Alles andere 
							 * koennen wir derzeit nicht.
							 * 
							 * Die Vorauszahlungsbuchung gleicht keine Fremdwaehrungsdifferenzen
							 * aus. Das ist auf der Eingangsrechnung selbst handzuhaben.
							 */
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG, 
									"Ungueltiger Betrag bei Zahlung aus Vorauszahlung");							
						}

						for (int i = 0; i < temp.length; i++) {
							details[i] = temp[i];
							details[i].setNBetrag(zahlungDto.getNBetrag());
							details[i].setCKommentar(zahlungDto.getCKommentar());
						}
						// zusaetzlich Vorauszahlung stornieren
						getBuchenFac().storniereBuchung(bd.getBuchungIId(),
								theClientDto);
						// zusaetzlich Rest als Vorauszahlung einbuchen
						bucheDifferenzVorauszahlung(bd.getIId(),
								zahlungDto.getNBetrag(), theClientDto);
					}

					buchungDto = getBuchenFac().buchen(buchungDto, details,
							rechnungDto.getReversechargeartId(), true, theClientDto);

					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_ERZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		}
		return buchungDto;
	}

	private BuchungdetailDto[] getBuchungDetailsAsNew(Buchungdetail bd) {
		BuchungdetailDto[] details;
		BuchungdetailDto[] temp = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bd.getBuchungIId());
		details = new BuchungdetailDto[temp.length - 1];
		System.arraycopy(temp, 0, details, 0, temp.length - 1);
		for (int i = 0; i < details.length; i++) {
			details[i].setBuchungIId(null);
			details[i].setIId(null);
		}
		return details;
	}

	private BuchungdetailDto[] addVorauszahlungDetails(
			BuchungdetailDto[] details, Integer buchungdetailIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// die bestehende Buchung mit -Betrag dazuhaengen
		BuchungdetailDto bdDto = getBuchenFac().buchungdetailFindByPrimaryKey(
				buchungdetailIId);
		BuchungdetailDto[] temp;
		temp = new BuchungdetailDto[details.length + 1];
		System.arraycopy(details, 0, temp, 0, details.length);
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungdetailartCNr(bdDto.getBuchungdetailartCNr());
		temp[i].setKontoIId(bdDto.getKontoIId());
		temp[i].setKontoIIdGegenkonto(bdDto.getKontoIIdGegenkonto());
		temp[i].setNBetrag(bdDto.getNBetrag().negate());
		if (bdDto.getNUst() != null)
			temp[i].setNUst(bdDto.getNUst().negate());
		return temp;
	}

	private BuchungdetailDto[] addAnzahlungZahlungDetails(
			BuchungdetailDto[] details, EingangsrechnungDto rechnungDto,
			EingangsrechnungzahlungDto zahlungDto, TheClientDto theClientDto) {
		// folgende Buchunge anhaengen
		// Gel. Anz an Verr. Gel. Anz
		// Vst
		boolean bMitVst = false;
		// Achtung: die jetzige Implementierung bucht hier keine Steuer, da laut
		// Werner unsere Anzahlung immer mit Leistung ist,
		// da schon bei Eingangsrechnung die Steuerbuchung erfolgte
		//
		// if (zahlungDto.getNBetragUst().signum()==0)
		BuchungdetailDto[] temp = new BuchungdetailDto[details.length + 2];
		// else {
		// temp = new BuchungdetailDto[details.length + 3];
		// bMitUst = true;
		// }
		
		System.arraycopy(details, 0, temp, 0, details.length);

		Konto kreditorenkonto = em.find(Konto.class, details[0].getKontoIId());
		FinanzamtPK fpk = new FinanzamtPK(kreditorenkonto.getFinanzamtIId(),
				kreditorenkonto.getMandantCNr());
		Finanzamt finanzamt = em.find(Finanzamt.class, fpk);
		boolean reverseCharge = isEingangsrechnungDtoReversecharge(rechnungDto);
		Integer kontoIIdAnzahlungGegebenBezahlt = finanzamt.getKontoIIdAnzahlungGegebenBezahlt();
		Integer kontoIIdAnzahlungGegebenVerr  = finanzamt.getKontoIIdAnzahlungGegebenVerr();
		
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		
		// Kontouebersetzung 
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
							.getDBelegdatum().getTime()));
		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(getExportVariante(theClientDto),
								getExportFormat(theClientDto), fibuExportKriterienDto,
								theClientDto);
		KontoDto kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungGegebenBezahlt, rechnungDto.getIId()) ;
		kontoIIdAnzahlungGegebenBezahlt = kontoDto.getIId() ;
		kontoDto = manager.getUebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungGegebenVerr, rechnungDto.getIId()) ;
		kontoIIdAnzahlungGegebenVerr = kontoDto.getIId() ;
		
		temp[i].setKontoIId(kontoIIdAnzahlungGegebenBezahlt);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungGegebenVerr);
		if (bMitVst || reverseCharge) {
			// auch bei Reverse Charge Brutto buchen
			temp[i].setNBetrag(zahlungDto.getNBetrag());
			temp[i].setNUst(zahlungDto.getNBetragust());
		} else {
			temp[i].setNBetrag(zahlungDto.getNBetrag().subtract(
					zahlungDto.getNBetragust()));
			temp[i].setNUst(new BigDecimal(0));
		}
		i++;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		temp[i].setKontoIId(kontoIIdAnzahlungGegebenVerr);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungGegebenBezahlt);
		temp[i].setNBetrag(temp[i - 1].getNBetrag());
		temp[i].setNUst(temp[i - 1].getNUst());
		if (bMitVst) {
			i++;
			FibuexportDto[] exportDaten = getExportDatenRechnung(rechnungDto,
					theClientDto);
			SteuerkategorieDto stkDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
					exportDaten[0].getKontoDto().getSteuerkategorieCnr(), 
					rechnungDto.getReversechargeartId(), 
					exportDaten[0].getKontoDto().getFinanzamtIId(), theClientDto) ;
//			Steuerkategorie stk = em.find(Steuerkategorie.class, exportDaten[0]
//					.getKontoDto().getSteuerkategorieIId());
			Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
					.getIIMwstsatzbezId();
			HvOptional<Steuerkategoriekonto> stkk = 
					getSteuerkategoriekonto(stkDto.getIId(), mwstSatzbezIId,
							Helper.asTimestamp(rechnungDto.getDBelegdatum()));
			if(!stkk.isPresent()) {
				throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(
						rechnungDto, stkDto, mwstSatzbezIId);
			}
			temp[i] = new BuchungdetailDto();
			temp[i].setBuchungIId(details[0].getBuchungIId());
			temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
			temp[i].setKontoIId(kontoIIdAnzahlungGegebenBezahlt);
			temp[i].setKontoIIdGegenkonto(stkk.get().getKontoIIdEk());
			temp[i].setNBetrag(zahlungDto.getNBetragust());
			temp[i].setNUst(new BigDecimal(0));
		}
		return temp;
	}

	protected FibuexportDto[] getExportDatenRechnung(
			EingangsrechnungDto rechnungDto, TheClientDto theClientDto) {
		// ermitteln der beteiligten Steuersaetze ueber den Exportmanager
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto
				.setSBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getDBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(getExportVariante(theClientDto),
						getExportFormat(theClientDto), fibuExportKriterienDto,
						theClientDto);
		FibuexportDto[] exportDaten = manager.getExportdatenEingangsrechnung(
				rechnungDto.getIId(), rechnungDto.getDBelegdatum());
		return exportDaten;
	}

	private PartnerDto getPartnerDtoMitKredKonto(Integer lieferantIId,
			TheClientDto theClientDto) {
		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(lieferantIId, theClientDto);
		FinanzValidator.kreditorkontoDefinition(lieferantDto);
		
		return getPartnerFac().partnerFindByPrimaryKey(
				lieferantDto.getPartnerIId(), theClientDto);
	}

	protected EingangsrechnungzahlungDto getEingangsrechnungzahlung(
			Integer zahlungIId) {
		EingangsrechnungzahlungDto zahlungDto = null;
		try {
			zahlungDto = getEingangsrechnungFac()
					.eingangsrechnungzahlungFindByPrimaryKey(zahlungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return zahlungDto;
	}

	protected EingangsrechnungDto getEingangsrechnungDto(
			Integer eingangsrechnungIId) {
		EingangsrechnungDto rechnungDto = null;
		try {
			rechnungDto = getEingangsrechnungFac()
					.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return rechnungDto;
	}

	public void verbucheZahlungErRueckgaengig(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		EingangsrechnungzahlungDto erzDto;
		try {
			erzDto = getEingangsrechnungFac()
					.eingangsrechnungzahlungFindByPrimaryKey(zahlungIId);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		verbucheZahlungErRueckgaengig(erzDto, theClientDto);
	}

	public void verbucheZahlungErRueckgaengig(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			TheClientDto theClientDto) {

		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(eingangsrechnungzahlungDto, "verbucheZahlungErRueckgaengig");
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG,
					eingangsrechnungzahlungDto.getIId());
			if (buchungZahlungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungZahlungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungZahlungDto.getBuchungIId();
				removeBelegbuchung(buchungZahlungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				if (eingangsrechnungzahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
					Integer bdIId = eingangsrechnungzahlungDto
							.getBuchungdetailIId();
					try {
						BuchungDto bDto = bucheVorauszahlungBetrag(bdIId,
								eingangsrechnungzahlungDto.getNBetrag(),
								theClientDto);

						Eingangsrechnungzahlung zahlung = em.find(
								Eingangsrechnungzahlung.class,
								eingangsrechnungzahlungDto.getIId());
						if (zahlung != null) {
							// es wurde nur eine Zahlung neu in der Fibu
							// verbucht und diese ist nicht geloescht
							// dann das neue Buchungsdetail an die Zahlung
							// haengen
							BuchungdetailDto[] bdtos = getBuchenFac()
									.buchungdetailsFindByBuchungIId(
											bDto.getIId());
							zahlung.setBuchungdetailIId(bdtos[0].getIId());
							em.merge(zahlung);
							em.flush();
						}
					} catch (RemoteException e) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH,
								"Vorauszahlung kann nicht aktiviert werden. "
										+ e.getMessage());
					}
				}
			}
		}
	}

	protected BuchungDto bucheVorauszahlungBetrag(Integer bdIId,
			BigDecimal betrag, TheClientDto theClientDto)
			throws RemoteException {
		// TODO: Reversechargeart.OHNE durch die Reversechargeart des Belegs ersetzen, ghp, 2.2.2016
		ReversechargeartDto rcartDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
		Buchungdetail bd = em.find(Buchungdetail.class, bdIId);
		return bucheVorauszahlungBetrag(bd, betrag, rcartDto.getIId(), theClientDto);
	}

	protected void bucheDifferenzVorauszahlung(Integer bdIId,
			BigDecimal zahlBetrag, TheClientDto theClientDto)
			throws RemoteException {
		// TODO: Reversechargeart.OHNE durch die Reversechargeart des Belegs ersetzen, ghp, 2.2.2016
		ReversechargeartDto rcartDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
		Buchungdetail bd = em.find(Buchungdetail.class, bdIId);
		BigDecimal rest = bd.getNBetrag().subtract(zahlBetrag);
		if (rest.signum() > 0) {
			// Rest als neue Buchung hinzufuegen
			bucheVorauszahlungBetrag(bd, rest, rcartDto.getIId(), theClientDto);
		}
	}

	private BuchungDto bucheVorauszahlungBetrag(Buchungdetail bd,
			BigDecimal betrag, Integer reversechargeartId, TheClientDto theClientDto)
			throws RemoteException {
		BuchungdetailDto[] bdDtos = getBuchungDetailsAsNew(bd);
		BuchungDto bDto = getBuchenFac().buchungFindByPrimaryKey(
				bd.getBuchungIId());
		bDto.setIId(null);
		bDto.setTAendern(null);
		bDto.setTAnlegen(null);
		bDto.setTStorniert(null);
		bDto.setPersonalIIdStorniert(null);
		if (bd.getNBetrag().compareTo(betrag) != 0) {
			myLogger.warn("bucheVorauszahlungbetrag " + bd.getNBetrag() + " != betrag " + betrag + " buchungDetail.iid=" + bd.getIId() + ". Auf betrag setzen!");
			if(bdDtos.length > 2) {
				// UST Details entfernen auf die harte Tour, weil wir keine UST Buchungen bei Vorauszahlungen unterstuetzen (wollen)
				// Index 0 + 1 ... SOLL / HABEN
				myLogger.warn("Entferne UST-Buchungen aus Vorauszahlungsbuchung iid=" + bd.getIId());
				BuchungdetailDto[] targetDtos = new BuchungdetailDto[2] ;
				System.arraycopy(bdDtos, 0, targetDtos, 0, 2);
				bdDtos = targetDtos ;
			}
			for (int i = 0; i < bdDtos.length; i++) {
				myLogger.warn("  [" + i + "] original-betrag " +
						bdDtos[i].getNBetrag() + " auf " + betrag + " setzen fuer Konto " + bdDtos[i].getKontoIId() + ".") ;
				bdDtos[i].setNBetrag(betrag);
			}
		}
		return getBuchenFac().buchen(bDto, bdDtos, reversechargeartId, true, theClientDto);
	}

	public void pruefeUvaVerprobung(String belegartCNr, Integer iBelegiid,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			// nur bei Fibumodul
			BelegbuchungDto belegbuchungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					belegartCNr, iBelegiid);
			if (belegbuchungDto != null) {
				Buchung buchung = em.find(Buchung.class,
						belegbuchungDto.getBuchungIId());
				if (buchung.getUvaverprobungIId() != null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
							new Exception(belegartCNr + " " + iBelegiid
									+ " ist bereits UVA verprobt"));
			}
		}
	}

	public void pruefeUvaVerprobung(Date buchungsdatum, Integer kontoIId,
			TheClientDto theClientDto) {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			// nur bei Fibumodul
			Konto konto = em.find(Konto.class, kontoIId);
			UvaverprobungDto uvapDto = getFinanzServiceFac()
					.getLetzteVerprobung(konto.getFinanzamtIId(), theClientDto);
			if (isInVerprobung(uvapDto, buchungsdatum, theClientDto))
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_UVAVERPROBUNG,
						new Exception("Buchung zum " + buchungsdatum
								+ " ist aufgrund UVA-Verprobung nicht erlaubt"));
		}
	}

	private boolean isInVerprobung(UvaverprobungDto uvapDto,
			Date buchungsdatum, TheClientDto theClientDto) {
		if (null == uvapDto)
			return false;

		Integer periode = null;
		if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_MONAT) {
			periode = uvapDto.getIMonat();
		} else if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL) {
			Integer[] perioden = HelperServer.getMonateFuerQuartal(uvapDto
					.getIMonat());
			periode = perioden[perioden.length - 1];
		} else if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_JAHR) {
			periode = 12;
		}
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				uvapDto.getIGeschaeftsjahr(), periode, theClientDto);
		return buchungsdatum.before(tVonBis[1]);
	}

	public BigDecimal getBuchungsbetragInWahrung(Integer buchungDetailIId,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Buchungdetail buchungdetail = em.find(Buchungdetail.class,
				buchungDetailIId);
		BelegbuchungDto bbDto = belegbuchungFindByBuchungIIdOhneExc(buchungdetail
				.getBuchungIId());

		if (bbDto == null) {
			// keine Belegbuchung, umrechnen ueber Kurs zu Datum
			Buchung buchung = em.find(Buchung.class,
					buchungdetail.getBuchungIId());
			BigDecimal betrag = getLocaleFac()
					.rechneUmInAndereWaehrungGerundetZuDatum(
							buchungdetail.getNBetrag(),
							theClientDto.getSMandantenwaehrung(), waehrungCNr,
							buchung.getTBuchungsdatum(), theClientDto);
			return betrag;
		} else {
			// aus dem Beleg den Kurs ermitteln falls gleiche Waehrung, sonst
			// umrechnen
			BigDecimal belegkurs = null;
			String belegwaehrung = null;
			java.sql.Date belegdatum = null;
			if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
					|| bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_GUTSCHRIFT)) {
				Rechnung rechnung = em.find(Rechnung.class,
						bbDto.getIBelegiid());
				if (rechnung != null) {
					belegkurs = rechnung.getNKurs();
					belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = new java.sql.Date(rechnung.getTBelegdatum()
							.getTime());
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_REZAHLUNG)) {
				Rechnungzahlung zahlung = em.find(Rechnungzahlung.class,
						bbDto.getIBelegiid());
				if (zahlung != null) {
					belegkurs = zahlung.getNKurs();
					Rechnung rechnung = em.find(Rechnung.class,
							zahlung.getRechnungIId());
					if (rechnung != null)
						belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = zahlung.getTZahldatum();
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				Eingangsrechnung rechnung = em.find(Eingangsrechnung.class,
						bbDto.getIBelegiid());
				if (rechnung != null) {
					belegkurs = Helper.rundeKaufmaennisch(Helper
							.getKehrwert(rechnung.getNKurs()), rechnung
							.getNKurs().scale());
					belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = new java.sql.Date(rechnung.getTBelegdatum()
							.getTime());
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_ERZAHLUNG)) {
				Eingangsrechnungzahlung zahlung = em.find(
						Eingangsrechnungzahlung.class, bbDto.getIBelegiid());
				if (zahlung != null) {
					belegkurs = zahlung.getNKurs();
					Eingangsrechnung rechnung = em.find(Eingangsrechnung.class,
							zahlung.getEingangsrechnungIId());
					if (rechnung != null)
						belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = zahlung.getTZahldatum();
				}
			}
			BigDecimal betrag = null;
			if (belegwaehrung.equals(waehrungCNr)) {
				if (belegkurs != null)
					if (belegkurs.compareTo(new BigDecimal(1)) == 0)
						betrag = buchungdetail.getNBetrag();
					else
						betrag = Helper.rundeKaufmaennisch(buchungdetail
								.getNBetrag().multiply(belegkurs),
								FinanzFac.NACHKOMMASTELLEN);
			} else {
				betrag = getLocaleFac()
						.rechneUmInAndereWaehrungGerundetZuDatum(
								buchungdetail.getNBetrag(),
								theClientDto.getSMandantenwaehrung(),
								waehrungCNr, belegdatum, theClientDto);
			}
			return betrag;
		}
	}

	protected BuchungDto setupBuchungDto(RechnungDto rechnungDto, 
			KundeDto rechnungKundeDto, TheClientDto theClientDto) {
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				rechnungKundeDto.getPartnerIId(), theClientDto);
	
		BuchungDto buchungDto = new BuchungDto();
		buchungDto.setCBelegnummer(rechnungDto.getCNr());
		buchungDto.setCText(partnerDto
				.getCName1nachnamefirmazeile1());
		buchungDto.setDBuchungsdatum(new Date(rechnungDto
				.getTBelegdatum().getTime()));
		buchungDto.setKostenstelleIId(rechnungDto
				.getKostenstelleIId());
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);
		
		return buchungDto ;
	}
	
	/**
	 * Im (Sach/Debitor/Kreditor)Konto ist eine SteuerkategorieId enthalten, die zur Reversechargeart "OHNE" gehoert
	 * Wir brauchen aber die ID der Steuerkategorie die zur Reverserechargeart des Beleges gehoert. 
	 * Da fuer die richtige Steuerkategorie auch das Finanzamt notwendig ist, ist es nicht so einfach, anstatt der
	 * SteuerkategorieId die SteuerkategorieCnr zu verwenden. 
	 * 
	 * @param reversechargeartId
	 * @param steuerkategorieId
	 * @return die passende Steuerkategorie
	 */
	private SteuerkategorieDto getSteuerkategorieIdFromSteuerkategorieId(
			Integer reversechargeartId, Integer steuerkategorieId, TheClientDto theClientDto) {
		Steuerkategorie stk = em.find(Steuerkategorie.class, steuerkategorieId);
		return getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
				stk.getCNr(), reversechargeartId, stk.getFinanzamtIId(), theClientDto) ;
	}
	
	abstract class BuchungdetailVisitor {
		public abstract void visitKursGewinn(BuchungdetailDto detailDto);
		public abstract void visitKursVerlust(BuchungdetailDto detailDto);
		public abstract void visitKursGewinnVerlust(BuchungdetailDto detailDto);
		public abstract void visitSkonto(BuchungdetailDto detailDto);
		public abstract void visitUst(BuchungdetailDto detailDto);
		public abstract void visitAnzahlungsDetail(BuchungdetailDto detailDto);
	}
	
	class BuchungdetailAnzahlungVisitor extends BuchungdetailVisitor {
		private List<BuchungdetailDto> details = new ArrayList<BuchungdetailDto>();
		private BuchungdetailDto anzahlungDetail = null;
	
		protected void setAnzahlungsDetail(BuchungdetailDto detailDto) {
			anzahlungDetail = detailDto;
		}
		
		protected void addDetail(BuchungdetailDto detailDto) {
			details.add(detailDto);
		}
		
		@Override
		public void visitAnzahlungsDetail(BuchungdetailDto detailDto) {
//			BuchungdetailDto d = new BuchungdetailDto(
//					true, detailDto.getKontoIId(), null, 
//					detailDto.getNBetrag().negate(), detailDto.getNUst());
/*
			BuchungdetailDto d = new BuchungdetailDto(false,
					details[index].getKontoIIdGegenkonto(), null,
					details[0].getNBetrag().negate(),details[0].getNUst().negate());			
 */
			BuchungdetailDto d = new BuchungdetailDto(
					false, detailDto.getKontoIId(), null, 
					detailDto.getNBetrag().negate(), detailDto.getNUst().negate());
//			setAnzahlungsDetail(d);
			setAnzahlungsDetail(detailDto);
		}
		
		@Override
		public void visitKursGewinnVerlust(BuchungdetailDto detailDto) {
			BuchungdetailDto perKursverlust = new BuchungdetailDto(true, 
					null, null,
					detailDto.getNBetrag(), detailDto.getNUst());
//			addDetail(perKursverlust);
		}
	
		@Override
		public void visitKursGewinn(BuchungdetailDto detailDto) {
			BuchungdetailDto perKursverlust = new BuchungdetailDto(detailDto.isSollBuchung(), 
					null, null,
					detailDto.getNBetrag().negate(), detailDto.getNUst());
//			addDetail(perKursverlust);
		}
		
		@Override
		public void visitKursVerlust(BuchungdetailDto detailDto) {
			BuchungdetailDto perKursverlust = new BuchungdetailDto(detailDto.isSollBuchung(), 
					null, null,
					detailDto.getNBetrag().negate(), detailDto.getNUst());
//			addDetail(perKursverlust);
		}
		
		@Override
		public void visitSkonto(BuchungdetailDto detailDto) {
			BuchungdetailDto perSkonto = new BuchungdetailDto(detailDto.isSollBuchung(), 
					null, null, 
					detailDto.getNBetrag().negate(), detailDto.getNUst().negate());
//			addDetail(perSkonto);
		}
		
		@Override
		public void visitUst(BuchungdetailDto detailDto) {
		}
		
		public List<BuchungdetailDto> getDetails() {
			if(anzahlungDetail == null) {
				throw new IllegalArgumentException("no anzahlungDetail");
			}
			
			for (BuchungdetailDto buchungdetailDto : details) {
				buchungdetailDto.setKontoIId(anzahlungDetail.getKontoIId());
				buchungdetailDto.setKontoIIdGegenkonto(null);
			}
			details.add(0, anzahlungDetail);
			return details;
		}
		
		public Integer getKontoAnzBezahltId() {
			if(anzahlungDetail == null) {
				throw new IllegalArgumentException("no anzahlungDetail");
			}
			return anzahlungDetail.getKontoIId();
		}
	}
	
	enum KursDifferenz {
		Keine,
		Gewinn,
		Verlust
	};

	class BuchungdetailAnalyzer {
		protected BuchungdetailDto[] details;
		protected BuchungdetailDto[] buchungDetails;
		private final TheClientDto theClientDto;
		private final Timestamp belegDatum;
		
		public BuchungdetailAnalyzer(
				Timestamp belegDatum, BuchungdetailDto[] buchungDetails,
				BuchungdetailDto[] details, TheClientDto theClientDto) {
			this.belegDatum = belegDatum;
			this.buchungDetails = buchungDetails;
			this.details = details;
			this.theClientDto = theClientDto;
		}
		
		public boolean hasUst() {
			return details[0].getNUst() != null && 
					details[0].getNUst().signum() != 0;
		}
		
		protected void visitSkonto(BuchungdetailVisitor visitor, int index) {
			visitor.visitSkonto(details[index]);
		}
		
		protected void visitUst(BuchungdetailVisitor visitor, int index) {
			visitor.visitUst(details[index]);
		}
		
		protected void visitKursGewinn(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursGewinn(details[index]);
		}
		
		protected void visitKursVerlust(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursVerlust(details[index]);
		}
		
		protected void visitKursGewinnVerlust(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursGewinnVerlust(details[index]);
		}
		
		protected void visitAnzahlungsDetail(BuchungdetailVisitor visitor, int index) {
//			visitor.visitAnzahlungsDetail(details[index]);
			
			BuchungdetailDto d = new BuchungdetailDto(
					false, details[index].getKontoIId(), null, 
					buchungDetails[0].getNBetrag(), buchungDetails[0].getNUst());

			visitor.visitAnzahlungsDetail(d);
		}
		
		public void evalAnzahlung(BuchungdetailVisitor visitor) throws RemoteException {
			Integer personenKontoId = details[0].getKontoIId();	

			int skontoIndex = -1;
			int ustIndex = -1;
			int kursIndex = -1;
			
			int index = 2;
			boolean skonto = details.length > 4 
					&& isSkontoKonto(personenKontoId, details[index].getKontoIId());
			if(skonto) {
				visitSkonto(visitor, index);
				
				skontoIndex = index;
				index += 2;
			}
			if(hasUst()) {
				visitUst(visitor, index);
				ustIndex = index;
				index += 1;
			}
			
			if(details.length > 4) {
				KursDifferenz differenz = isKursGewinnVerlustKonto(
						personenKontoId, details[index].getKontoIId());
				if(KursDifferenz.Gewinn.equals(differenz)) {
					visitKursGewinn(visitor, index);
					kursIndex = index;
					index += 2;
				} else if (KursDifferenz.Verlust.equals(differenz)) {
					visitKursVerlust(visitor, index);
					kursIndex = index;
					index += 2;
				}
			}
/*			
			boolean kursVerlust = details.length > 4
					&& isKursGewinnVerlustKonto(personenKontoId, details[index].getKontoIId());
			if(kursVerlust) {
				visitKursGewinnVerlust(visitor, index);
				kursIndex = index;
				index += 2;
			}
*/			
			visitAnzahlungsDetail(visitor, index);
		}
		
		/**
		 * Ermittlung, ob das betreffende kontoId ein Kursgewinn- oder verlustkonto ist.
		 * 
		 * Ja, diese Methode ist nicht vollstaendig sauber, da in allen Steuerkategorien
		 * gesucht wird, die fuer dieses Finanzamt hinterlegt sind.
		 */
		protected KursDifferenz isKursGewinnVerlustKonto(Integer personenKontoId, 
				Integer kontoId) throws RemoteException {
			KontoDto debitorDto = getFinanzFac().kontoFindByPrimaryKey(personenKontoId);
			if(debitorDto.getFinanzamtIId() == null) return KursDifferenz.Keine;
			
			SteuerkategorieDto[] dtos = getFinanzServiceFac()
					.steuerkategorieFindByFinanzamtIId(
							debitorDto.getFinanzamtIId(), theClientDto);
			for (SteuerkategorieDto dto : dtos) {
				if(kontoId.equals(dto.getKontoIIdKursgewinn())) return KursDifferenz.Gewinn;
				if(kontoId.equals(dto.getKontoIIdKursverlust())) return KursDifferenz.Verlust;
			}
			
			return KursDifferenz.Keine;
		}
		
		protected boolean isSkontoKonto(Integer personenKontoId, Integer kontoId) throws RemoteException {
			KontoDto debitorDto = getFinanzFac().kontoFindByPrimaryKey(personenKontoId);
			if(debitorDto.getFinanzamtIId() == null) return false;
			SteuerkategorieDto[] dtos = getFinanzServiceFac()
					.steuerkategorieFindByFinanzamtIId(
							debitorDto.getFinanzamtIId(), theClientDto);
			for (SteuerkategorieDto dto : dtos) {
				SteuerkategoriekontoDto[] kontoDtos = getFinanzServiceFac()
						.steuerkategoriekontoAllZuDatum(dto.getIId(), belegDatum);
				for (SteuerkategoriekontoDto skkontoDto : kontoDtos) {					
					if(kontoId.equals(skkontoDto.getKontoIIdSkontoVk())) return true;
					if(kontoId.equals(skkontoDto.getKontoIIdSkontoEk())) return true;
				}
			}
			
			return false;
		}
	}
	
	class BuchungdetailERAnalyzer extends BuchungdetailAnalyzer {
		public BuchungdetailERAnalyzer(
				Timestamp belegDatum, BuchungdetailDto[] belegBuchungDetails,
				BuchungdetailDto[] details, TheClientDto theClientDto) {
			super(belegDatum, belegBuchungDetails, details, theClientDto);
		}
		
		@Override
		protected void visitAnzahlungsDetail(BuchungdetailVisitor visitor, int index) {
			visitor.visitAnzahlungsDetail(details[index]);
		}
		
		@Override
		protected void visitKursGewinnVerlust(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursGewinnVerlust(details[index+1]);
		}
		
		@Override
		protected void visitKursGewinn(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursGewinn(details[index+1]);
		}

		@Override
		protected void visitKursVerlust(BuchungdetailVisitor visitor, int index) {
			visitor.visitKursVerlust(details[index+1]);
		}
		
		@Override
		public void evalAnzahlung(BuchungdetailVisitor visitor) throws RemoteException {
			Integer personenKontoId = details[0].getKontoIId();	

			int skontoIndex = -1;
			int ustIndex = -1;
			int kursIndex = -1;
			
			int index = 2;
			boolean skonto = details.length > 4 
					&& isSkontoKonto(personenKontoId, details[index].getKontoIId());
			if(skonto) {
				visitSkonto(visitor, index);
				
				skontoIndex = index;
				index += 2;
			}
			
			if(details.length > 4) {
				KursDifferenz differenz = isKursGewinnVerlustKonto(
						personenKontoId, details[index].getKontoIId());
				if(KursDifferenz.Gewinn.equals(differenz)) {
					visitKursGewinn(visitor, index);
					kursIndex = index;
					index += 2;
				} else if (KursDifferenz.Verlust.equals(differenz)) {
					visitKursVerlust(visitor, index);
					kursIndex = index;
					index += 2;
				}
			}
/*			
			boolean kursVerlust = details.length > 4
					&& isKursGewinnVerlustKonto(personenKontoId, details[index].getKontoIId());
			if(kursVerlust) {
				visitKursGewinnVerlust(visitor, index);
				kursIndex = index;
				index += 2;
			}
*/			
			visitAnzahlungsDetail(visitor, index);
		}
 	}
	
	class BuchungdetailERAnzahlungVisitor extends BuchungdetailAnzahlungVisitor {
		@Override
		public List<BuchungdetailDto> getDetails() {
			List<BuchungdetailDto> details =  super.getDetails();
			for (BuchungdetailDto detailDto : details) {
				detailDto.swapSollHaben();
			}
			return details;
		}
		
		@Override
		public void visitKursGewinnVerlust(BuchungdetailDto detailDto) {
			BuchungdetailDto d = new BuchungdetailDto(
					detailDto.getBuchungdetailartCNr(), 
					detailDto.getKontoIId(), null, 
					detailDto.getNBetrag(), detailDto.getNUst());
			addDetail(d);
		}

		public void visitKursGewinn(BuchungdetailDto detailDto) {
			BuchungdetailDto d = new BuchungdetailDto(
					detailDto.getBuchungdetailartCNr(), 
					detailDto.getKontoIId(), null, 
					detailDto.getNBetrag(), detailDto.getNUst());
			addDetail(d);
		}

		public void visitKursVerlust(BuchungdetailDto detailDto) {
			BuchungdetailDto d = new BuchungdetailDto(
					detailDto.getBuchungdetailartCNr(), 
					detailDto.getKontoIId(), null, 
					detailDto.getNBetrag(), detailDto.getNUst());
			addDetail(d);
		}

		@Override
		public void visitAnzahlungsDetail(BuchungdetailDto detailDto) {
			BuchungdetailDto d = new BuchungdetailDto(
					detailDto.getBuchungdetailartCNr(), 
					detailDto.getKontoIId(), null, 
					detailDto.getNBetrag(), detailDto.getNUst());
			setAnzahlungsDetail(d);
		}
	}
}
