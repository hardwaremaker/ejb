/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.inserat.ejb.Inseratrechnung;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.KundeQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeDtoAssembler;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundesachbearbeiterDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.ILPLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class KundeFacBean extends Facade implements KundeFac {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Create einen neuen Partner und dann einen neuen Kunden.
	 * 
	 * @param kundeDto
	 *            kunde mit partner
	 * @param theClientDto
	 *            id
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return Integer kundeKey
	 */
	public Integer createKunde(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		// precondition
		if (kundeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kundeDto == null"));
		}
		if (kundeDto.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kundeDto.getPartnerDto() == null"));
		}

		Kunde kunde = null;
		try {
			// exccatch: test begin; JO: immer kommentiert, testing only!
			// getArtikelFac().artikellieferantFindByPrimaryKey(new Integer(11),
			// cNrUser);
			// exccatch: test end

			// Partner.
			Integer iIdPartner = kundeDto.getPartnerDto().getIId();

			if (iIdPartner != null) {
				getPartnerFac().updatePartner(kundeDto.getPartnerDto(),
						theClientDto);
			} else {
				iIdPartner = getPartnerFac().createPartner(
						kundeDto.getPartnerDto(), theClientDto);
			}
			// Verbinde Partner mit Kunden.
			kundeDto.setPartnerIId(iIdPartner);

			// Partner lesen wegen generierter Daten.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					iIdPartner, theClientDto);
			kundeDto.setPartnerDto(partnerDto);

			// 2. Kunde selbst
			// befuelle Felder am Server.
			kundeDto.setPersonalAendernIID(theClientDto.getIDPersonal());
			kundeDto.setPersonalAnlegenIID(theClientDto.getIDPersonal());

			// Generieren eines PK.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUNDE);
			kundeDto.setIId(pk);

			generateKundennr(kundeDto);

			if (kundeDto.getBVersteckterlieferant() == null) {
				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
			}
			if (kundeDto.getBReversecharge() == null) {
				kundeDto.setBReversecharge(Helper.boolean2Short(false));
			}
			if (kundeDto.getBAkzeptiertteillieferung() == null) {
				kundeDto.setBAkzeptiertteillieferung(Helper
						.boolean2Short(false));
			}
			if (kundeDto.getBZollpapier() == null) {
				kundeDto.setBZollpapier(Helper.boolean2Short(false));
			}
			// Default abbuchungslager ist Hauptlager
			if (kundeDto.getLagerIIdAbbuchungslager() == null) {
				kundeDto.setLagerIIdAbbuchungslager(getLagerFac()
						.getHauptlagerDesMandanten(theClientDto).getIId());
			}

			if (kundeDto.getIKundennummer() == null) {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);
				boolean bKundeMitNummer = ((Boolean) parameter
						.getCWertAsObject()).booleanValue();
				if (bKundeMitNummer) {
					kundeDto.setIKundennummer(getNextKundennummer(theClientDto));
				}
			}

			if (kundeDto.getILieferdauer() == null) {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_DEFAULT_LIEFERDAUER);
				Integer iLieferdauer = (Integer) parameter.getCWertAsObject();

				kundeDto.setILieferdauer(iLieferdauer);

			}

			if (partnerDto.getLandplzortIId() != null) {
				LandplzortDto plzortDto = getSystemFac()
						.landplzortFindByPrimaryKey(
								partnerDto.getLandplzortIId());
				if (plzortDto.getLandDto().getWaehrungCNr() != null) {
					kundeDto.setCWaehrung(plzortDto.getLandDto()
							.getWaehrungCNr());
				}
			}

			KundeDto kundeDto1 = getKundeFac()
					.kundeFindByiIdPartnercNrMandantOhneExc(
							partnerDto.getIId(), theClientDto.getMandant(),
							theClientDto);

			if (kundeDto1 == null) {
				// create, erst die PKs und Notnulls.

				kunde = new Kunde(kundeDto.getIId(), kundeDto.getPartnerIId(),
						kundeDto.getMandantCNr(), kundeDto.getCWaehrung(),
						kundeDto.getLieferartIId(), kundeDto.getSpediteurIId(),
						kundeDto.getZahlungszielIId(),
						kundeDto.getKostenstelleIId(),
						kundeDto.getMwstsatzbezIId(),
						kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
						kundeDto.getPersonalAnlegenIId(),
						kundeDto.getPersonalAendernIId(),
						kundeDto.getBAkzeptiertteillieferung(),
						kundeDto.getPersonaliIdProvisionsempfaenger(),
						kundeDto.getBReversecharge(),
						kundeDto.getBVersteckterlieferant(),
						kundeDto.getLagerIIdAbbuchungslager(),
						kundeDto.getILieferdauer(), kundeDto.getBZollpapier());
				em.persist(kunde);
				em.flush();

				kundeDto.setTAendern(kunde.getTAendern());
				kundeDto.setTAnlegen(kunde.getTAnlegen());
			} else {
				kunde = em.find(Kunde.class, kundeDto1.getIId());
				if (kunde == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				kundeDto.setTAnlegen(getTimestamp());
				kundeDto.setTAendern(getTimestamp());
				kundeDto.setPersonalAendernIID(theClientDto.getIDPersonal());
			}
			// update, jetzt die "Nutzfelder".
			setKundeFromKundeDto(kunde, kundeDto);
		}
		// exccatch: hier ist die Catchkaskade
		catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		catch (EntityExistsException ex) {
			System.out.println("ex(0): " + ex.getMessage());
			System.out.println("ex(1): " + ex.getCause().getMessage());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return kunde.getIId();
	}

	public Integer getNextKundennummer(TheClientDto theClientDto)
			throws EJBExceptionLP {

		Integer i = null;
		try {
			Query query = em
					.createNamedQuery("KundeejbSelectNextPersonalnummer");
			query.setParameter(1, theClientDto.getMandant());
			i = (Integer) query.getSingleResult();
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// new Exception(ex));
		// }

		return i;
	}

	/**
	 * Create einen neuen Kunden aus Lieferant wenn es keinen vorhanden ist.
	 * 
	 * @param iIdLieferant
	 *            lieferant mit partner
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return Integer kundeKey
	 */
	public Integer createVerstecktenKundenAusLieferant(Integer iIdLieferant,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdKunde = null;

		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(iIdLieferant, theClientDto);
		KundeDto kundeDto = getKundeFac()
				.kundeFindByiIdPartnercNrMandantOhneExc(
						lieferantDto.getPartnerIId(),
						lieferantDto.getMandantCNr(), theClientDto);
		// Neuen versteckten Kunde anlegen wenn Lieferant kein Kunde ist
		if (kundeDto == null) {
			kundeDto = new KundeDto();
			kundeDto.setPartnerDto(lieferantDto.getPartnerDto());
			kundeDto.setPartnerIId(lieferantDto.getPartnerIId());
			kundeDto.setBVersteckterlieferant(Helper.boolean2Short(true));
			// Vorbelegungen werden vom Mandanten geholt
			MandantDto mandant = getMandantFac().mandantFindByPrimaryKey(
					lieferantDto.getMandantCNr(), theClientDto);
			kundeDto.setMandantCNr(mandant.getCNr());
			kundeDto.setKostenstelleIId(mandant.getIIdKostenstelle());
			kundeDto.setMwstsatzbezIId(mandant
					.getMwstsatzbezIIdStandardinlandmwstsatz());
			kundeDto.setLieferartIId(mandant.getLieferartIIdKunde());
			kundeDto.setSpediteurIId(mandant.getSpediteurIIdKunde());
			kundeDto.setZahlungszielIId(mandant.getZahlungszielIIdKunde());
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandant
					.getVkpfArtikelpreislisteIId());
			/**
			 * @todo MB->VF mit WH klaeren (ist auch am Client beim
			 *       Kundenanlegen PanelKundeKopfdaten) PJ 4330
			 */
			kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto
					.getIDPersonal());
			kundeDto.setCWaehrung(lieferantDto.getWaehrungCNr());
			kundeDto.setbIstinteressent(Helper.boolean2Short(false));
			kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
			kundeDto.setBDistributor(Helper.boolean2Short(false));
			kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
			kundeDto.setBLsgewichtangeben(Helper.boolean2Short(false));
			kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
			kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
			kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
			kundeDto.setBReversecharge(Helper.boolean2Short(false));

			iIdKunde = createKunde(kundeDto, theClientDto);

		}

		else {
			iIdKunde = kundeDto.getIId();
		}

		return iIdKunde;
	}

	public Integer createKundeAusPartner(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdKunde = null;

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				partnerIId, theClientDto);
		KundeDto kundeDto = getKundeFac()
				.kundeFindByiIdPartnercNrMandantOhneExc(partnerIId,
						theClientDto.getMandant(), theClientDto);
		// Neuen versteckten Kunde anlegen wenn Partner kein Kunde ist
		if (kundeDto == null) {
			kundeDto = new KundeDto();
			kundeDto.setPartnerDto(partnerDto);
			kundeDto.setPartnerIId(partnerDto.getIId());
			kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
			// Vorbelegungen werden vom Mandanten geholt
			MandantDto mandant = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			kundeDto.setMandantCNr(mandant.getCNr());
			kundeDto.setKostenstelleIId(mandant.getIIdKostenstelle());
			kundeDto.setMwstsatzbezIId(mandant
					.getMwstsatzbezIIdStandardinlandmwstsatz());
			kundeDto.setLieferartIId(mandant.getLieferartIIdKunde());
			kundeDto.setSpediteurIId(mandant.getSpediteurIIdKunde());
			kundeDto.setZahlungszielIId(mandant.getZahlungszielIIdKunde());
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandant
					.getVkpfArtikelpreislisteIId());

			kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto
					.getIDPersonal());
			kundeDto.setCWaehrung(theClientDto.getSMandantenwaehrung());
			kundeDto.setbIstinteressent(Helper.boolean2Short(false));
			kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
			kundeDto.setBDistributor(Helper.boolean2Short(false));
			kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
			kundeDto.setBLsgewichtangeben(Helper.boolean2Short(false));
			kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
			kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
			kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
			kundeDto.setBReversecharge(Helper.boolean2Short(false));

			iIdKunde = createKunde(kundeDto, theClientDto);

		}

		else {
			iIdKunde = kundeDto.getIId();
		}

		return iIdKunde;
	}

	private void generateKundennr(KundeDto kundeDto) {

		if (kundeDto.getBbGeneriereKurznr() != null
				&& kundeDto.getBbGeneriereKurznr().booleanValue()) {
			// --generiere eine Kundennummer zB. G01.
			kundeDto.setCKurznr(generateNextFreeKurznr(kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1().substring(0, 1)
					.toUpperCase()));
		}
	}

	public void removeKunde(KundeDto kundeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("kundeDto == null"));
		}

		// Erst den Kunde loeschen.
		try {
			Kunde toRemove = em.find(Kunde.class, kundeDtoI.getIId());
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
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	/**
	 * Update einen Kunden mit Partner.
	 * 
	 * @param kundeDto
	 *            KundeDto
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateKunde(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (kundeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("kundeDto == null"));
		}
		if (kundeDto.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("kundeDto.getPartnerDto() == null"));
		}

		Integer iId = kundeDto.getIId();

		try {
			// Debitorenkto.
			if (kundeDto.getIDebitorenkontoAsIntegerNotiId() != null) {
				workoutDebitoren(kundeDto, theClientDto);
			} else {
				kundeDto.setIidDebitorenkonto(null);
			}

			// Partner.
			getPartnerFac().updatePartner(kundeDto.getPartnerDto(),
					theClientDto);

			generateKundennr(kundeDto);

			Kunde kunde = em.find(Kunde.class, iId);
			if (kunde == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// Wer wann aendert.
			kundeDto.setPersonalAnlegenIID(theClientDto.getIDPersonal());
			kundeDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			setKundeFromKundeDto(kunde, kundeDto);

			String sToLog = kundeDto.toString();
			myLogger.logKritisch(sToLog.substring(0,
					ILPLogger.MESSAGELENGTH_MAX));
			myLogger.logKritisch(sToLog.substring(ILPLogger.MESSAGELENGTH_MAX,
					sToLog.length()));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void workoutDebitoren(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			if (kundeDto.getUpdateModeDebitorenkonto() == KundeDto.I_UPD_DEBITORENKONTO_PRUEFE_AUF_DOPPELTE) {
				/**
				 * @todo MB->JO andere methode verwenden und die alte loeschen
				 *       PJ 4338
				 */

				long t = System.currentTimeMillis();
				KundeDto[] ak = findAllKundeByDebitorennr(kundeDto,
						theClientDto);
				myLogger.info(">" + (System.currentTimeMillis() - t));

				if (ak.length > 0) {
					ArrayList<Object> alInfo = new ArrayList<Object>();
					for (int i = 0; i < ak.length; i++) {
						alInfo.add(ak[i].getPartnerDto()
								.formatFixTitelName1Name2());
						alInfo.add(ak[i].getPartnerDto().formatAdresse());
					}
					// svr2clt: 0 hier werden die Daten gesetzt.
					throw new EJBExceptionLP(
							EJBExceptionLP.WARNUNG_KTO_BESETZT, alInfo, null);
				}
			}

			KontoDto kontoDtoIch = getFinanzFac()
					.kontoFindByCnrKontotypMandantOhneExc(
							kundeDto.getIDebitorenkontoAsIntegerNotiId()
									.toString(),
							FinanzServiceFac.KONTOTYP_DEBITOR,
							kundeDto.getMandantCNr(), theClientDto);

			if (kontoDtoIch == null) {
				// --Kto nicht gefunden: create

				// AD: gleiche Methode verwenden wie fuer Nummer generieren!
				KontoDto k = createDebitorenkontoZuKundenAutomatisch(
						kundeDto.getIId(),
						true,
						kundeDto.getIDebitorenkontoAsIntegerNotiId().toString(),
						theClientDto);
				/*
				 * KontoDto k = new KontoDto(); k.setIId(null);
				 * k.setCNr(kundeDto.getIDebitorenkontoAsIntegerNotiId()
				 * .toString()); k.setCBez(kundeDto.getPartnerDto()
				 * .getCName1nachnamefirmazeile1()); UvaartDto uvaartDto =
				 * getFinanzServiceFac() .uvaartFindByCnrMandant(
				 * FinanzServiceFac.UVAART_NICHT_DEFINIERT, theClientDto);
				 * k.setUvaartIId(uvaartDto.getIId());
				 * k.setKontoartCNr(FinanzServiceFac.KONTOART_NICHT_STEUERBAR);
				 * k.setBAutomeroeffnungsbuchung(Helper.boolean2Short(true));
				 * k.setBAllgemeinsichtbar(Helper.boolean2Short(true));
				 * k.setBManuellbebuchbar(Helper.boolean2Short(true)); / @todo
				 * JO->WH,MB Innergemeinschaftlich, ... PJ 4340
				 */
				/*
				 * k.setKontotypCNr(FinanzServiceFac.KONTOTYP_DEBITOR);
				 * k.setMandantCNr(kundeDto.getMandantCNr());
				 * 
				 * k = getFinanzFac().createKonto(k, theClientDto);
				 */
				kundeDto.setIidDebitorenkonto(k.getIId());
			} else {
				// --Kto gefunden: update
				kontoDtoIch.setCBez(kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1());
				kundeDto.setIidDebitorenkonto(kontoDtoIch.getIId());
				getFinanzFac().updateKonto(kontoDtoIch, theClientDto);
			}
			kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private String generateNextFreeKurznr(String sKurznrFirstLetterI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("KundefindAllKurznr");
		query.setParameter(1, sKurznrFirstLetterI);
		Collection<?> c = query.getResultList();
		int iF = 0;
		if (c != null && c.size() > 0) {
			Kunde kunde = null;
			iF = -1;
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				kunde = (Kunde) iter.next();
				String sN = kunde.getCKurznr();
				int iiN = Integer.parseInt(sN.substring(1, sN.length()));
				if (iiN > iF) {
					iF = iiN;
				}
			}
		}
		iF++;

		if (iF > KundeFac.MAX_KURZNR_LP5) {
			// Pro Buchstabe 99 Kunden.
			throw new EJBExceptionLP(
					EJBExceptionLP.WARNUNG_99_KUNDEN_PRO_BUCHSTABE, "");
		}

		return sKurznrFirstLetterI + (iF < 10 ? ("0" + iF) : iF + "");
	}

	public KundeDto kundeFindByPrimaryKeySmall(Integer iId) {
		Kunde kunde = em.find(Kunde.class, iId);
		return assembleKundeDto(kunde);
	}

	public KundeDto kundeFindByPrimaryKeySmallWithNull(Integer iId) {
		Kunde kunde = em.find(Kunde.class, iId);
		if (kunde == null)
			return null;
		return assembleKundeDto(kunde);
	}

	public KundeDto kundeFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");

		KundeDto kundeDto = null;

		try {
			kundeDto = kundeFindByPrimaryKeyImpl(iId, theClientDto);
			if (kundeDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return kundeDto;
	}

	private void setKundeFromKundeDto(Kunde kunde, KundeDto kundeDto) {
		kunde.setBAkzeptiertteillieferung(kundeDto
				.getBAkzeptiertteillieferung());
		kunde.setBDistributor(kundeDto.getBDistributor());
		kunde.setBIstinteressent(kundeDto.getbIstinteressent());
		kunde.setBIstreempfaenger(kundeDto.getBIstreempfaenger());
		kunde.setBLsgewichtangeben(kundeDto.getBLsgewichtangeben());
		kunde.setBMindermengenzuschlag(kundeDto.getBMindermengenzuschlag());
		kunde.setBMonatsrechnung(kundeDto.getBMonatsrechnung());
		kunde.setBPreiseanlsandrucken(kundeDto.getBPreiseanlsandrucken());
		kunde.setBRechnungsdruckmitrabatt(kundeDto
				.getBRechnungsdruckmitrabatt());
		kunde.setBSammelrechnung(kundeDto.getBSammelrechnung());
		kunde.setPartnerIId(kundeDto.getPartnerIId());
		kunde.setMandantCNr(kundeDto.getMandantCNr());
		kunde.setWaehrungCNr(kundeDto.getCWaehrung());
		kunde.setMwstsatzIId(((Mwstsatzbez) em.find(Mwstsatzbez.class,
				kundeDto.getMwstsatzbezIId())).getIId());
		kunde.setVkpfartikelpreislisteIIdstdpreisliste(kundeDto
				.getVkpfArtikelpreislisteIIdStdpreisliste());
		kunde.setLieferartIId(kundeDto.getLieferartIId());
		kunde.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

		kunde.setZahlungszielIId(kundeDto.getZahlungszielIId());
		kunde.setKostenstelleIId(kundeDto.getKostenstelleIId());
		kunde.setFRabattsatz(kundeDto.getFRabattsatz());
		kunde.setIGarantieinmonaten(kundeDto.getIGarantieinmonaten());
		kunde.setKontoIIdDebitorenkonto(kundeDto.getIidDebitorenkonto());
		kunde.setKontoIIdErloesekonto(kundeDto.getIidErloeseKonto());
		kunde.setCKurznr(kundeDto.getCKurznr());
		kunde.setSpediteurIId(kundeDto.getSpediteurIId());
		kunde.setPersonalIIdBekommeprovision(kundeDto
				.getPersonaliIdProvisionsempfaenger());
		kunde.setNKreditlimit(kundeDto.getNKreditlimit());
		kunde.setTBonitaet(kundeDto.getTBonitaet());
		kunde.setTLiefersperream(kundeDto.getTLiefersperream());
		kunde.setIKundennummer(kundeDto.getIKundennummer());
		kunde.setIDefaultlskopiendrucken(kundeDto.getIDefaultlskopiendrucken());
		kunde.setIDefaultrekopiendrucken(kundeDto.getIDefaultrekopiendrucken());
		kunde.setXKommentar(kundeDto.getXKommentar());
		kunde.setIMitarbeiteranzahl(kundeDto.getIMitarbeiteranzahl());
		kunde.setCTour(kundeDto.getCTour());
		kunde.setCLieferantennr(kundeDto.getCLieferantennr());
		kunde.setCAbc(kundeDto.getCAbc());
		kunde.setTAgbuebermittelung(kundeDto.getTAgbuebermittelung());
		kunde.setCFremdsystemnr(kundeDto.getCFremdsystemnr());
		kunde.setPersonalIIdAendern(kundeDto.getPersonalAendernIId());
		kunde.setPersonalIIdAnlegen(kundeDto.getPersonalAnlegenIId());
		kunde.setTAendern(kundeDto.getTAendern());
		kunde.setTAnlegen(kundeDto.getTAnlegen());
		kunde.setPartnerIIdRechnungsadresse(kundeDto
				.getPartnerIIdRechnungsadresse());
		kunde.setCHinweisextern(kundeDto.getSHinweisextern());
		kunde.setCHinweisintern(kundeDto.getSHinweisintern());
		kunde.setFZessionsfaktor(kundeDto.getFZessionsfaktor());
		kunde.setBReversecharge(kundeDto.getBReversecharge());
		kunde.setBVersteckterlieferant(kundeDto.getBVersteckterlieferant());
		kunde.setTErwerbsberechtigung(kundeDto.getTErwerbsberechtigung());
		kunde.setCErwerbsberechtigungsbegruendung(kundeDto
				.getCErwerbsberechtigungsbegruendung());
		kunde.setILieferdauer(kundeDto.getILieferdauer());
		kunde.setCIdExtern(kundeDto.getCIdExtern());
		kunde.setBZollpapier(kundeDto.getBZollpapier());

		em.merge(kunde);
		em.flush();
	}

	/**
	 * Referenz auf PartnerFacHome erzeugen.
	 * 
	 * @param kunde
	 *            Kunde
	 * @return KundeDto
	 */
	private KundeDto assembleKundeDto(Kunde kunde) {
		return KundeDtoAssembler.createDto(kunde);
	}

	public void updateKundeRechnungsadresse(KundeDto kundeDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("kundeDtoI == null"));
		}

		if (kundeDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("kundeDtoI.getPartnerDto() == null"));
		}

		try {
			// Partnerrechnungsadresse des Kunden CU.
			Integer iIdPartnerRE = kundeDtoI.getPartnerRechnungsadresseDto()
					.getIId();
			if (iIdPartnerRE != null) {
				// Update.
				getPartnerFac()
						.updatePartner(
								kundeDtoI.getPartnerRechnungsadresseDto(),
								theClientDto);
			} else {
				// Create; Rechnungsadr. ist erfasst worden.
				iIdPartnerRE = getPartnerFac()
						.createPartner(
								kundeDtoI.getPartnerRechnungsadresseDto(),
								theClientDto);
			}
			kundeDtoI.setPartnerIIdRechnungsadresse(iIdPartnerRE);

			// Kunde updaten.
			updateKunde(kundeDtoI, theClientDto);

			// Der ausgewaehlte Partner muss jetzt noch als Kunde angelegt
			// werden (wenn noch nicht vorhanden)
			KundeDto kundeDtoREAdr = kundeFindByiIdPartnercNrMandantOhneExc(
					iIdPartnerRE, kundeDtoI.getMandantCNr(), theClientDto);
			if (kundeDtoREAdr == null) {
				// er ist noch nicht angelegt -> jetzt anlegen
				KundeDto kundeDtoNew = new KundeDto();
				kundeDtoNew.setMandantCNr(kundeDtoI.getMandantCNr());
				kundeDtoNew.setPartnerIId(kundeDtoI
						.getPartnerIIdRechnungsadresse());
				kundeDtoNew.setPartnerDto(kundeDtoI
						.getPartnerRechnungsadresseDto());
				kundeDtoNew.setKostenstelleIId(kundeDtoI.getKostenstelleIId());
				kundeDtoNew.setMwstsatzbezIId(kundeDtoI.getMwstsatzbezIId());
				kundeDtoNew.setLieferartIId(kundeDtoI.getLieferartIId());
				kundeDtoNew.setSpediteurIId(kundeDtoI.getSpediteurIId());
				kundeDtoNew.setPersonaliIdProvisionsempfaenger(kundeDtoI
						.getPersonaliIdProvisionsempfaenger());
				kundeDtoNew.setVkpfArtikelpreislisteIIdStdpreisliste(kundeDtoI
						.getVkpfArtikelpreislisteIIdStdpreisliste());
				kundeDtoNew.setZahlungszielIId(kundeDtoI.getZahlungszielIId());
				kundeDtoNew.setCWaehrung(kundeDtoI.getCWaehrung());
				kundeDtoNew.setBAkzeptiertteillieferung(kundeDtoI
						.getBAkzeptiertteillieferung());
				kundeDtoNew.setBbGeneriereKurznr(kundeDtoI
						.getBbGeneriereKurznr());
				kundeDtoNew.setBIstkunde(kundeDtoI.getBIstkunde());
				kundeDtoNew.setBDistributor(kundeDtoI.getBDistributor());
				kundeDtoNew.setbIstinteressent(kundeDtoI.getbIstinteressent());
				kundeDtoNew
						.setBIstreempfaenger(kundeDtoI.getBIstreempfaenger());
				kundeDtoNew.setBLsgewichtangeben(kundeDtoI
						.getBLsgewichtangeben());
				kundeDtoNew.setBMindermengenzuschlag(kundeDtoI
						.getBMindermengenzuschlag());
				kundeDtoNew.setBMonatsrechnung(kundeDtoI.getBMonatsrechnung());
				kundeDtoNew.setBPreiseanlsandrucken(kundeDtoI
						.getBPreiseanlsandrucken());
				kundeDtoNew.setBRechnungsdruckmitrabatt(kundeDtoI
						.getBRechnungsdruckmitrabatt());
				kundeDtoNew.setBReversecharge(kundeDtoI.getBReversecharge());
				kundeDtoNew.setBSammelrechnung(kundeDtoI.getBSammelrechnung());
				kundeDtoNew.setBsIndikator(kundeDtoI.getBsIndikator());
				kundeDtoNew.setBVersteckterlieferant(kundeDtoI
						.getBVersteckterlieferant());
				createKunde(kundeDtoNew, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeKundeRechnungsadresse(KundeDto kundeDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("kundeDtoI == null"));
		}

		// 1 Partnerrechnungsadresse des Lieferanten = null.
		// Wer hat wann geloescht?
		Integer iIdPartnerRE = kundeDtoI.getPartnerRechnungsadresseDto()
				.getIId();

		if (iIdPartnerRE != null) {
			// Update.
			kundeDtoI.setPartnerIIdRechnungsadresse(null);
			kundeDtoI.setPersonalAendernIID(theClientDto.getIDPersonal());
			kundeDtoI.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			kundeDtoI
					.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);
			// 3 Kunde update.
			updateKunde(kundeDtoI, theClientDto);
		}
	}

	public KundeDto kundeFindByPrimaryKeyOhneExc(Integer iIdKundeI,
			TheClientDto theClientDto) {
		if (null == iIdKundeI)
			return null;

		KundeDto kundeDto = null;
		try {
			kundeDto = kundeFindByPrimaryKeyImpl(iIdKundeI, theClientDto);
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(), "iIdKundeI=" + iIdKundeI, t);
		}
		return kundeDto;
	}

	private KundeDto kundeFindByPrimaryKeyImpl(Integer iId,
			TheClientDto theClientDto) throws RemoteException {
		Kunde kunde = em.find(Kunde.class, iId);
		if (kunde == null)
			return null;

		KundeDto kundeDto = assembleKundeDto(kunde);
		kundeDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
				kundeDto.getPartnerIId(), theClientDto));

		// Partnerrechnungsadresse.
		Integer iIdPartnerRE = kundeDto.getPartnerIIdRechnungsadresse();

		if (iIdPartnerRE != null) {
			kundeDto.setPartnerRechnungsadresseDto(getPartnerFac()
					.partnerFindByPrimaryKey(iIdPartnerRE, theClientDto));
		}

		KontoDtoSmall k = null;
		if (kundeDto.getIidDebitorenkonto() != null) {
			k = getFinanzFac().kontoFindByPrimaryKeySmall(
					kundeDto.getIidDebitorenkonto());
		}
		Integer iD = null;
		if (k != null && k.getCNr() != null) {
			iD = new Integer(Integer.parseInt(k.getCNr()));
		}
		kundeDto.setIDebitorenkontoAsIntegerNotiId(iD);

		return kundeDto;
	}

	public KundeDto[] kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
			Integer iIdVkpfArtikelpreislisteStdpreislisteI,
			TheClientDto theClientDto) {

		if (iIdVkpfArtikelpreislisteStdpreislisteI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception(
							"iIdVkpfArtikelpreislisteStandardpreislisteI == null"));
		}

		KundeDto[] aKundeDto = new KundeDto[0];

		try {
			Query query = em
					.createNamedQuery("KundefindByVkpfArtikelpreislisteIIdStdpreisliste");
			query.setParameter(1, iIdVkpfArtikelpreislisteStdpreislisteI);
			Collection<?> cl = query.getResultList();

			aKundeDto = KundeDtoAssembler.createDtos(cl);
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(),
					"iIdVkpfArtikelpreislisteStdpreislisteI="
							+ iIdVkpfArtikelpreislisteStdpreislisteI, t);
		}

		return aKundeDto;
	}

	private KundeDto[] findAllKundeByDebitorennr(KundeDto kundeDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<KundeDto> list = new ArrayList<KundeDto>();
		if (kundeDtoI.getIDebitorenkontoAsIntegerNotiId() != null) {
			Session session = null;
			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				// Kunden finden
				Criteria c = session.createCriteria(FLRKunde.class);
				// alle mit diesem Debitorenkonto
				c.createCriteria(KundeFac.FLR_KONTO).add(
						Restrictions
								.eq(FinanzFac.FLR_KONTO_C_NR, kundeDtoI
										.getIDebitorenkontoAsIntegerNotiId()
										.toString()));
				// aber nicht denselben
				c.add(Restrictions.not(Restrictions.eq(KundeFac.FLR_KUNDE_I_ID,
						kundeDtoI.getIId())));
				c.add(Restrictions.eq(KundeFac.FLR_KUNDE_MANDANT_C_NR,
						theClientDto.getMandant()));
				// query ausfuehren
				List<?> resultList = c.list();
				for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
					FLRKunde item = (FLRKunde) iter.next();
					list.add(kundeFindByPrimaryKey(item.getI_id(), theClientDto));
				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
		KundeDto[] returnArray = new KundeDto[list.size()];
		return (KundeDto[]) list.toArray(returnArray);
	}

	public KundeDto[] kundefindByKontoIIdDebitorenkonto(Integer kontoIId) {
		Query query = em.createNamedQuery("KundefindByKontoIIdDebitorenkonto");
		query.setParameter(1, kontoIId);
		Collection<?> cl = query.getResultList();

		KundeDto[] kundeDto = KundeDtoAssembler.createDtos(cl);
		return kundeDto;
	}

	public KundeDto kundeFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI,
			String cNrMandantI, TheClientDto theClientDto) {

		// precondition
		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNrMandantI == null"));
		}
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdPartnerI == null"));
		}

		KundeDto kundeDto = null;
		try {
			Query query = em
					.createNamedQuery("KundefindByiIdPartnercNrMandant");
			query.setParameter(1, iIdPartnerI);
			query.setParameter(2, cNrMandantI);
			Kunde kunde = (Kunde) query.getSingleResult();
			kundeDto = assembleKundeDto(kunde);
		} catch (NoResultException ex) {
			// nothing here
		}
		return kundeDto;
	}

	public KundeDto kundeFindByLieferantenCnrMandantCnrNull(
			String lieferantenCnr, String mandantCnr) {
		if (Helper.isStringEmpty(lieferantenCnr))
			return null;
		if (Helper.isStringEmpty(mandantCnr))
			return null;

		KundeDto kundeDto = null;

		try {
			HvTypedQuery<Kunde> query = KundeQuery.byLieferantCnrMandantCnr(em,
					lieferantenCnr.trim(), mandantCnr.trim());
			Kunde kunde = query.getSingleResult();
			kundeDto = assembleKundeDto(kunde);
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
		}

		return kundeDto;
	}

	public List<KundeDto> kundeFindByMandantCnr(TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "mandantCnr");

		List<Kunde> kunden = KundeQuery.listByMandantCnr(em,
				theClientDto.getMandant());
		List<KundeDto> kundeDtos = new ArrayList<KundeDto>(kunden.size());
		for (Kunde kunde : kunden) {
			kundeDtos.add(kundeFindByPrimaryKey(kunde.getIId(), theClientDto));
		}

		return kundeDtos;
	}

	public List<KundeDto> kundeFindByKbezMandantCnr(String kbez,
			TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(kbez, "kbez");
		Validator.notEmpty(theClientDto.getMandant(), "mandantCnr");

		List<Partner> partners = PartnerQuery.listByKbez(em, kbez);
		List<KundeDto> kundeDtos = new ArrayList<KundeDto>();
		for (Partner partner : partners) {
			KundeDto kundeDto = kundeFindByiIdPartnercNrMandantOhneExc(
					partner.getIId(), theClientDto.getMandant(), theClientDto);
			if (kundeDto != null) {
				kundeDtos.add(kundeDto);
			}
		}

		return kundeDtos;
	}

	/**
	 * Ein Debitorenkonto fuer einen Kunden automatisch erstellen. Anhand der
	 * definierten Nummernkreise bzw. der in der FiBu hinterlegten Regeln.
	 * 
	 * @param kundeIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	public KontoDto createDebitorenkontoZuKundenAutomatisch(Integer kundeIId,
			boolean kontoAnlegen, String kontonummerVorgabe,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			// Kunde holen
			KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto);
			// Konto anlegen
			KontoDto kontoDto = getFinanzFac()
					.createKontoFuerPartnerAutomatisch(
							kundeDto.getPartnerDto(),
							FinanzServiceFac.KONTOTYP_DEBITOR, kontoAnlegen,
							kontonummerVorgabe, theClientDto);
			// Konto beim Kunden anhaengen
			kundeDto.setIidDebitorenkonto(kontoDto.getIId());
			kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);
			updateKunde(kundeDto, theClientDto);
			return kontoDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Pruefen, ob der Kunde eine UID-Nummer hat. Nur fuer oesterreichische
	 * Mandanten.
	 * 
	 * @param kundeIId
	 *            Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             wenn der Kunde keine UID-Nummer hat
	 */
	public void pruefeKundenUIDNummer(Integer kundeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// wenn der Mandant ein LKZ hat und EU-Mitglied ist
			if (mandantDto.getPartnerDto().getLandplzortDto() != null
					&& mandantDto.getPartnerDto().getLandplzortDto()
							.getLandDto().getEUMitglied() != null
					&& mandantDto.getPartnerDto().getLandplzortDto()
							.getLandDto().getEUMitglied().before(getDate())) {
				KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId,
						theClientDto);
				// wenn der Kunde EU-Mitglied und keine UID hat -> Exception
				if (kundeDto.getPartnerDto().getLandplzortDto() != null
						&& kundeDto.getPartnerDto().getLandplzortDto()
								.getLandDto().getEUMitglied() != null
						&& kundeDto.getPartnerDto().getLandplzortDto()
								.getLandDto().getEUMitglied().before(getDate())
						&& kundeDto.getPartnerDto().getCUid() == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT,
							new Exception("Kunde i_id=" + kundeIId
									+ " hat keine UID-Nummer"));
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public String getKundeAsXML(String cNrI) {
		return "";
	}

	/**
	 * Uuml;berpr&uuml;ft, ob Ziel- und Quellkunde vollst&auml;ndige Kunden sind
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @throws EJBExceptionLP
	 */
	private void checkInputParamsZielQuellKundeDtos(KundeDto kundeZielDto,
			KundeDto kundeQuellDto) throws EJBExceptionLP {

		if (kundeZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kundeDto == null (Ziel)"));
		}

		if (kundeZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kundeDto.getIId() == null (Ziel)"));
		}

		if (kundeQuellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kundeDto == null (Quell)"));
		}

		if (kundeQuellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kundeDto.getIId() == null (Quell)"));
		}
	}

	/**
	 * H&auml;ngt beim Lieferschein (falls vorhanden) die
	 * Lieferadresse/Rechnungsadresse vom Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignLieferscheinBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// LS_LIEFERSCHEIN kunde_i_id_lieferadresse (mandant_cnr) = normale
		// kundenadresse
		try {
			LieferscheinDto[] aLieferscheinDtos = null;
			aLieferscheinDtos = getLieferscheinFac()
					.lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aLieferscheinDtos.length; j++) {
				if (aLieferscheinDtos[j] != null) {
					Lieferschein zeile = em.find(Lieferschein.class,
							aLieferscheinDtos[j].getIId());
					zeile.setKundeIIdLieferadresse(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// LS_LIEFERSCHEIN kunde_i_id_rechnungsadresse (mandant_cnr) =
		// kunde-rechnungsadresse
		try {
			LieferscheinDto[] aLieferscheinDtos = null;
			aLieferscheinDtos = getLieferscheinFac()
					.lieferscheinFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aLieferscheinDtos.length; j++) {
				if (aLieferscheinDtos[j] != null) {
					aLieferscheinDtos[j]
							.setKundeIIdRechnungsadresse(kundeZielDto.getIId());
					getLieferscheinFac().updateLieferscheinOhneWeitereAktion(
							aLieferscheinDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei der Reklamation (falls vorhanden) den Kunden vom
	 * Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignReklamationBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// REKLA_REKLAMATION kunde_i_id (mandant_cnr)

		ReklamationDto[] aReklamationDtos = null;

		aReklamationDtos = getReklamationFac()
				.reklamationFindByKundeIIdMandantCNrOhneExc(
						kundeQuellDto.getIId(), mandantCNr);
		for (int j = 0; j < aReklamationDtos.length; j++) {
			if (aReklamationDtos[j] != null) {

				Reklamation zeile = em.find(Reklamation.class,
						aReklamationDtos[j].getIId());
				zeile.setKundeIId(kundeZielDto.getIId());
				zeile.setAnsprechpartnerIId(null);
				em.merge(zeile);
				em.flush();

			}
		}

	}

	public void reassignInseratBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("InseratrechnungfindByKundeIId");
		query.setParameter(1, kundeQuellDto.getIId());
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Inseratrechnung b = (Inseratrechnung) it.next();
			b.setKundeIId(kundeZielDto.getIId());
			b.setAnsprechpartnerIId(null);
			em.merge(b);
			em.flush();
		}

	}

	/**
	 * H&auml;ngt bei der Rechnung (falls vorhanden) den Kunden und die
	 * Statistikadresse vom Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignRechnungBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// die bei kunden hinterlegte adresse wird in der RE als
		// statistikadresse verwendet
		// die beim kunden hinterlegte rechnungsadresse wird in der RE als kunde
		// verwendet, falls vorhanden

		// RECH_RECHNUNG kunde_i_id (mandant_cnr)
		try {
			RechnungDto[] aRechnungDtos = null;
			aRechnungDtos = getRechnungFac()
					.rechnungFindByKundeIIdMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aRechnungDtos.length; j++) {
				if (aRechnungDtos[j] != null) {

					Rechnung zeile = em.find(Rechnung.class,
							aRechnungDtos[j].getIId());
					zeile.setKundeIId(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// RECH_RECHNUNG kunde_i_id_statistikadresse (mandant_cnr)
		try {
			RechnungDto[] aRechnungDtos = null;
			aRechnungDtos = getRechnungFac()
					.rechnungFindByKundeIIdStatistikadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aRechnungDtos.length; j++) {
				if (aRechnungDtos[j] != null) {
					aRechnungDtos[j].setKundeIIdStatistikadresse(kundeZielDto
							.getIId());
					getRechnungFac().updateRechnungBeimZusammenfuehren(
							aRechnungDtos[j]);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt beim Auftrag (falls vorhanden) die
	 * Auftrags-/Liefer-/Rechnungsadresse vom Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */

	public void reassignEinkaufsangebotZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("EinkaufsangebotfindByKundeIId");
		query.setParameter(1, kundeQuellDto.getIId());
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Einkaufsangebot b = (Einkaufsangebot) it.next();
			b.setKundeIId(kundeZielDto.getIId());
			b.setAnsprechpartnerIId(null);
			em.merge(b);
			em.flush();
		}

	}

	public void reassignAuftragBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);
		// AUFT_AUFTRAG - kunde_i_id_auftragsadresse (mandant_cnr)
		try {
			AuftragDto[] aAuftragDtos = null;
			aAuftragDtos = getAuftragFac()
					.auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					Auftrag zeile = em.find(Auftrag.class,
							aAuftragDtos[j].getIId());
					zeile.setKundeIIdAuftragsadresse(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// AUFT_AUFTRAG - kunde_i_id_lieferadresse (mandant_cnr)
		try {
			AuftragDto[] aAuftragDtos = null;
			aAuftragDtos = getAuftragFac()
					.auftragFindByKundeIIdLieferadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					aAuftragDtos[j].setKundeIIdLieferadresse(kundeZielDto
							.getIId());
					getAuftragFac().updateAuftragOhneWeitereAktion(
							aAuftragDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// AUFT_AUFTRAG - kunde_i_id_rechnungsadresse (mandant_cnr)
		// die RE-Adr an sich kann im Client beim Zusammenfuehren ausgewaehlt
		// werden
		try {
			AuftragDto[] aAuftragDtos = null;
			aAuftragDtos = getAuftragFac()
					.auftragFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					aAuftragDtos[j].setKundeIIdRechnungsadresse(kundeZielDto
							.getIId());
					getAuftragFac().updateAuftragOhneWeitereAktion(
							aAuftragDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt die Kundesoko auf den Kunden um, von welchem sie
	 * gew&uuml;nscht werden; die Soko vom anderen Kunden werden inkl.
	 * Mengenstaffel gel&ouml;scht
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param kundesokoKundeIId
	 *            KundenIId von dem Kunden, von welchem die Sokos
	 *            &uuml;bernommen werden
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignKundesokoBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, int kundesokoKundeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		if (kundesokoKundeIId != kundeZielDto.getIId().intValue()
				&& kundesokoKundeIId != kundeQuellDto.getIId().intValue()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"kundesokoKundeIId != Ziel- oder Quellkunde"));
		}

		try {
			// PART_KUNDESOKO kunde_i_id (zusatzfunktionsberechtigung)
			if (kundesokoKundeIId == kundeQuellDto.getIId().intValue()) {
				// loesche ziel sokos/mengenstaffeln
				KundesokoDto[] aKundesokoZielDtos = null;
				aKundesokoZielDtos = getKundesokoFac()
						.kundesokoFindByKundeIIdOhneExc(kundeZielDto.getIId());
				for (int j = 0; j < aKundesokoZielDtos.length; j++) {
					if (aKundesokoZielDtos[j] != null) {
						getKundesokoFac().removeKundesoko(
								aKundesokoZielDtos[j], theClientDto);
					}
				}
				// ersetze iids bei sokos/mengenst. der quelle durch ziel
				KundesokoDto[] aKundesokoQuellDtos = null;
				aKundesokoQuellDtos = getKundesokoFac()
						.kundesokoFindByKundeIIdOhneExc(kundeQuellDto.getIId());
				for (int j = 0; j < aKundesokoQuellDtos.length; j++) {
					if (aKundesokoQuellDtos[j] != null) {
						aKundesokoQuellDtos[j].setKundeIId(kundeZielDto
								.getIId());
						getKundesokoFac().updateKundesoko(
								aKundesokoQuellDtos[j], null, theClientDto);
					}
				}
			} else {
				// loesche quell sokos/mengenstaffeln
				KundesokoDto[] aKundesokoQuellDtos = null;
				aKundesokoQuellDtos = getKundesokoFac()
						.kundesokoFindByKundeIIdOhneExc(kundeQuellDto.getIId());
				for (int j = 0; j < aKundesokoQuellDtos.length; j++) {
					if (aKundesokoQuellDtos[j] != null) {
						getKundesokoFac().removeKundesoko(
								aKundesokoQuellDtos[j], theClientDto);
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt beim Sachbearbeiter den Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignKundesachbearbeiterBeimZusammenfuehren(
			KundeDto kundeZielDto, KundeDto kundeQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// PART_KUNDESACHBEARBEITER kunde_i_id
		try {
			KundesachbearbeiterDto[] aKundesachbearbeiterQuellDtos = null;
			aKundesachbearbeiterQuellDtos = getKundesachbearbeiterFac()
					.kundesachbearbeiterFindByKundeIIdOhneExc(
							kundeQuellDto.getIId());
			for (int j = 0; j < aKundesachbearbeiterQuellDtos.length; j++) {
				if (aKundesachbearbeiterQuellDtos[j] != null) {
					// pruefen, ob es beim ziel schon diesen sachbearbeiter mit
					// datum gibt
					KundesachbearbeiterDto kundesachbearbeiterZielDto = getKundesachbearbeiterFac()
							.kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAbOhneExc(
									kundeZielDto.getIId(),
									aKundesachbearbeiterQuellDtos[j]
											.getPersonalIId(),
									aKundesachbearbeiterQuellDtos[j]
											.getTGueltigab());
					// wenn es beim zielkunden diesen sachbearbeiter gibt
					if (kundesachbearbeiterZielDto != null) {
						// quellsachbearbeiter loeschen, vorher auf
						// abhaengigkeiten pruefen
						getKundesachbearbeiterFac().removeKundesachbearbeiter(
								aKundesachbearbeiterQuellDtos[j]);
					} else {
						aKundesachbearbeiterQuellDtos[j]
								.setKundeIId(kundeZielDto.getIId());
						getKundesachbearbeiterFac().updateKundesachbearbeiter(
								aKundesachbearbeiterQuellDtos[j], theClientDto);
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt beim Angebot (wenn vorhanden) die KundenIIdAngebotsadresse vom
	 * Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignAngebotBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// ANGB_ANGEBOT - kunde_i_id_angebotsadresse (mandant_cnr)
		try {
			AngebotDto[] aAngebotDtos = null;
			aAngebotDtos = getAngebotFac()
					.angebotFindByKundeIIdAngebotsadresseMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAngebotDtos.length; j++) {
				if (aAngebotDtos[j] != null) {
					Angebot zeile = em.find(Angebot.class,
							aAngebotDtos[j].getIId());
					zeile.setKundeIIdAngebotsadresse(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei den AngebotStkl (wenn vorhanden) die KundenIId vom
	 * Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDto
	 *            KundeDto
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void reassignAngebotStklBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// AS_AGSTKL - kunde_i_id (mandant_cnr)
		try {
			AgstklDto[] aAgstklDtos = null;
			aAgstklDtos = getAngebotstklFac()
					.agstklFindByKundeIIdMandantCNrOhneExc(
							kundeQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aAgstklDtos.length; j++) {
				if (aAgstklDtos[j] != null) {
					Agstkl zeile = em.find(Agstkl.class,
							aAgstklDtos[j].getIId());
					zeile.setKundeIId(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * Zwei Kunden werden zu einem Kunden vereint; die
	 * Tabellen-Verkn&uuml;pfungen auf den zu l&ouml;schenden Kunden werden
	 * durch den &uuml;brigbleibenden ersetzt, zuletzt wird ein Kunde
	 * gel&ouml;scht, der dazugeh&ouml;rige Partner bleibt aber erhalten
	 * 
	 * @param kundeZielDto
	 *            KundeDto
	 * @param kundeQuellDtoIId
	 *            int
	 * @param kundesokoKundeIId
	 *            int - von welchem Kunden die Sokos &uuml;bernommen werden
	 * @param kundePartnerIId
	 *            Integer Wird der Kunde mit einem anderen Partner
	 *            verkn&uuml;pft, so steht hier die zu verkn&uuml;pfende
	 *            PartnerIId
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void zusammenfuehrenKunde(KundeDto kundeZielDto,
			int kundeQuellDtoIId, int kundesokoKundeIId,
			Integer kundePartnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		KundeDto kundeQuellDto = null;
		MandantDto[] aMandantDtos = null;
		if (kundeZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kundeDto == null (Ziel oder Quell)"));
		}

		if (kundeZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kundeZielDto.getIId() == null (Ziel)"));
		}

		kundeQuellDto = kundeFindByPrimaryKey(kundeQuellDtoIId, theClientDto);

		Kunde kundeQuelle = em.find(Kunde.class, kundeQuellDtoIId);

		Kunde kundeZiel = em.find(Kunde.class, kundeZielDto.getIId());

		if (kundeQuelle.getKontoIIdDebitorenkonto() != null
				&& kundeZiel.getKontoIIdDebitorenkonto() != null
				&& !kundeQuelle.getKontoIIdDebitorenkonto().equals(
						kundeZiel.getKontoIIdDebitorenkonto())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN,
					new Exception(
							"FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN"));
		}

		if (kundeQuelle.getKontoIIdDebitorenkonto() != null
				&& kundeZiel.getKontoIIdDebitorenkonto() == null) {

			// Quelle besetzt, Ziel jedoch nicht -> Ziel bekommt Quelle
			kundeZielDto.setIidDebitorenkonto(kundeQuelle
					.getKontoIIdDebitorenkonto());

		}

		if (kundeQuelle.getMwstsatzIId() != null
				&& kundeZiel.getMwstsatzIId() != null
				&& !kundeQuelle.getMwstsatzIId().equals(
						kundeZiel.getMwstsatzIId())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception(
							"FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		if ((kundeQuelle.getMwstsatzIId() != null && kundeZiel.getMwstsatzIId() == null)
				|| (kundeZiel.getMwstsatzIId() != null && kundeQuelle
						.getMwstsatzIId() == null)) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception(
							"FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		myLogger.info("Zielkunde: " + kundeZielDto.toString());
		myLogger.info("Quellkunde: " + kundeQuellDto.toString());
		myLogger.info("KundesokoKundeIId: " + kundesokoKundeIId);
		myLogger.info("kundePartnerIId: " + kundePartnerIId);

		// neue Ziel-Dto-Daten in die DB zurueckschreiben
		if ((kundeZielDto.getPartnerDto() == null && kundeZielDto
				.getPartnerIId() != null)
				|| (kundeZielDto.getPartnerDto() != null
						&& kundeZielDto.getPartnerIId() != null && kundeZielDto
						.getPartnerDto().getIId() == null)) {
			kundeZielDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					kundeZielDto.getPartnerIId(), theClientDto));
		}

		kundeZielDto
				.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_UPDATE);

		if (kundeZiel.getKontoIIdDebitorenkonto() != null) {
			kundeZielDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
					getFinanzFac().kontoFindByPrimaryKey(
							kundeZiel.getKontoIIdDebitorenkonto()).getCNr()));
		}

		updateKunde(kundeZielDto, theClientDto);

		try {

			// mandantenunabhaengig
			getDokumenteFac().vertauscheBelegartIdBeiBelegartdokumenten(
					LocaleFac.BELEGART_KUNDE, kundeQuellDto.getIId(),
					kundeZielDto.getIId(), theClientDto);
			getJCRDocFac().fuehreDokumenteZusammen(kundeZielDto, kundeQuellDto);
			reassignKundesachbearbeiterBeimZusammenfuehren(kundeZielDto,
					kundeQuellDto, theClientDto);
			reassignKundesokoBeimZusammenfuehren(kundeZielDto, kundeQuellDto,
					kundesokoKundeIId, theClientDto);

			// mandantenabhaengig
			aMandantDtos = getMandantFac().mandantFindAll(theClientDto);
			int i = 0;
			while (i < aMandantDtos.length) {

				reassignAngebotBeimZusammenfuehren(kundeZielDto, kundeQuellDto,
						aMandantDtos[i].getCNr(), theClientDto);
				reassignAngebotStklBeimZusammenfuehren(kundeZielDto,
						kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignAuftragBeimZusammenfuehren(kundeZielDto, kundeQuellDto,
						aMandantDtos[i].getCNr(), theClientDto);
				reassignEinkaufsangebotZusammenfuehren(kundeZielDto,
						kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignInseratBeimZusammenfuehren(kundeZielDto, kundeQuellDto,
						aMandantDtos[i].getCNr(), theClientDto);
				reassignRechnungBeimZusammenfuehren(kundeZielDto,
						kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignLieferscheinBeimZusammenfuehren(kundeZielDto,
						kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignReklamationBeimZusammenfuehren(kundeZielDto,
						kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);

				i++;
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// Quellkunde loeschen
		if (kundeQuellDto != null) {
			try {
				getKundeFac().removeKunde(kundeQuellDto, theClientDto);
				// Den Partner aendern, falls gewuenscht (ist erst hier moeglich
				// wegen uk in part_kunde)
				if (kundePartnerIId != null) {
					kundeZielDto.setPartnerIId(kundePartnerIId);
					kundeZielDto.setPartnerDto(getPartnerFac()
							.partnerFindByPrimaryKey(kundePartnerIId,
									theClientDto));
					updateKunde(kundeZielDto, theClientDto);
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

	}
	
	public boolean hatKundeVersandweg(Integer kundeIId, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto) ;
		return kundeDto.getPartnerDto().getVersandwegIId() != null ;
	}
}
