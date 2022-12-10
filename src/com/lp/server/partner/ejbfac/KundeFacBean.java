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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.xml.sax.SAXException;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.inserat.ejb.Inseratrechnung;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.bl.VendidataCustomersExportTransformer;
import com.lp.server.partner.bl.VendidataCustomersMarshaller;
import com.lp.server.partner.bl.VendidataXmlCustomerTransformResult;
import com.lp.server.partner.ejb.Ansprechpartner;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.KundeKennung;
import com.lp.server.partner.ejb.KundeKennungQuery;
import com.lp.server.partner.ejb.KundeQuery;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.Kundespediteur;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.service.IVendidataCustomersExportBeanServices;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeKennungDto;
import com.lp.server.partner.service.KundenInfoFuerStrukturAnsicht;
import com.lp.server.partner.service.KundesachbearbeiterDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundespediteurDto;
import com.lp.server.partner.service.KundespediteurDtoAssembler;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.ejb.Kennung;
import com.lp.server.system.ejb.KennungQuery;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejb.VersandwegPartner;
import com.lp.server.system.ejb.VersandwegPartnerQuery;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KennungDto;
import com.lp.server.system.service.KennungType;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KennungId;
import com.lp.server.util.KundeId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
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
	 * @param kundeDto     kunde mit partner
	 * @param theClientDto id
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return Integer kundeKey
	 */
	public Integer createKunde(KundeDto kundeDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// precondition
		if (kundeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeDto == null"));
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
				getPartnerFac().updatePartner(kundeDto.getPartnerDto(), theClientDto);
			} else {
				iIdPartner = getPartnerFac().createPartner(kundeDto.getPartnerDto(), theClientDto);
			}
			// Verbinde Partner mit Kunden.
			kundeDto.setPartnerIId(iIdPartner);

			// Partner lesen wegen generierter Daten.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartner, theClientDto);
			kundeDto.setPartnerDto(partnerDto);

			// 2. Kunde selbst
			// befuelle Felder am Server.
			kundeDto.setPersonalAendernIID(theClientDto.getIDPersonal());
			kundeDto.setPersonalAnlegenIID(theClientDto.getIDPersonal());

			// Generieren eines PK.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUNDE);
			kundeDto.setIId(pk);

			if (kundeDto.getBVersteckterlieferant() == null) {
				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
			}
			if (kundeDto.getBReversecharge() == null) {
				kundeDto.setBReversecharge(Helper.boolean2Short(false));
			}
			if (kundeDto.getBZuschlagInklusive() == null) {
				kundeDto.setBZuschlagInklusive(Helper.boolean2Short(false));
			}
			if (kundeDto.getBVkpreisAnhandLSDatum() == null) {
				kundeDto.setBVkpreisAnhandLSDatum(Helper.boolean2Short(false));
			}
			if (kundeDto.getReversechargeartId() == null) {
				ReversechargeartDto rcArtOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
				kundeDto.setReversechargeartId(rcArtOhneDto.getIId());
			}

			if (kundeDto.getBAkzeptiertteillieferung() == null) {
				kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
			}
			if (kundeDto.getBZollpapier() == null) {
				kundeDto.setBZollpapier(Helper.boolean2Short(false));
			}
			if (kundeDto.getBRechnungJeLieferadresse() == null) {
				kundeDto.setBRechnungJeLieferadresse(Helper.boolean2Short(false));
			}

			// Default abbuchungslager ist Hauptlager
			if (kundeDto.getLagerIIdAbbuchungslager() == null) {
				kundeDto.setLagerIIdAbbuchungslager(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
			}

			if (kundeDto.getIKundennummer() == null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);
				int bKundeMitNummer = ((Integer) parameter.getCWertAsObject()).intValue();
				if (bKundeMitNummer == 1) {
					kundeDto.setIKundennummer(getNextKundennummer(theClientDto));
				}
			}

			if (kundeDto.getILieferdauer() == null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_LIEFERDAUER);
				Integer iLieferdauer = (Integer) parameter.getCWertAsObject();

				kundeDto.setILieferdauer(iLieferdauer);

			}
			if (kundeDto.getBMindermengenzuschlag() == null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_MINDERMENGENZUSCHLAG);
				Boolean bMMz = (Boolean) parameter.getCWertAsObject();

				kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(bMMz));

			}
			if (kundeDto.getNMindestbestellwert() == null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_MINDESTBESTELLWERT);
				java.math.BigDecimal bd = (java.math.BigDecimal) parameter.getCWertAsObject();

				if (bd != null && bd.doubleValue() > 0) {
					kundeDto.setNMindestbestellwert(bd);
				}

			}

			if (kundeDto.getBLsgewichtangeben() == null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_AM_LS_GEWICHT_ANDRUCKEN);

				kundeDto.setBLsgewichtangeben(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			}

			if (partnerDto.getLandplzortIId() != null) {
				LandplzortDto plzortDto = getSystemFac().landplzortFindByPrimaryKey(partnerDto.getLandplzortIId());
				if (plzortDto.getLandDto().getWaehrungCNr() != null) {
					kundeDto.setCWaehrung(plzortDto.getLandDto().getWaehrungCNr());
				}
			}

			kundeDto.setIDefaultlskopiendrucken(
					getParameterFac().getAnzahlDefaultKopienLieferschein(theClientDto.getMandant()));

			kundeDto.setIDefaultrekopiendrucken(
					getParameterFac().getAnzahlDefaultKopienRechnung(theClientDto.getMandant()));

			KundeDto kundeDto1 = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(),
					theClientDto.getMandant(), theClientDto);

			if (kundeDto1 == null) {
				// create, erst die PKs und Notnulls.

				kunde = new Kunde(kundeDto.getIId(), kundeDto.getPartnerIId(), kundeDto.getMandantCNr(),
						kundeDto.getCWaehrung(), kundeDto.getLieferartIId(), kundeDto.getSpediteurIId(),
						kundeDto.getZahlungszielIId(), kundeDto.getKostenstelleIId(), kundeDto.getMwstsatzbezIId(),
						kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), kundeDto.getPersonalAnlegenIId(),
						kundeDto.getPersonalAendernIId(), kundeDto.getBAkzeptiertteillieferung(),
						kundeDto.getPersonaliIdProvisionsempfaenger(), kundeDto.getBReversecharge(),
						kundeDto.getBVersteckterlieferant(), kundeDto.getLagerIIdAbbuchungslager(),
						kundeDto.getILieferdauer(), kundeDto.getBZollpapier(), kundeDto.getReversechargeartId(),
						kundeDto.getBMindermengenzuschlag(), kundeDto.getBRechnungJeLieferadresse(),
						kundeDto.getBVkpreisAnhandLSDatum());
				em.persist(kunde);
				em.flush();

				kundeDto.setTAendern(kunde.getTAendern());
				kundeDto.setTAnlegen(kunde.getTAnlegen());
			} else {
				kunde = em.find(Kunde.class, kundeDto1.getIId());
				if (kunde == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

		HvDtoLogger<KundeDto> logger = new HvDtoLogger<KundeDto>(em, theClientDto);
		logger.logInsert(kundeDto);

		return kunde.getIId();
	}

	public Integer getNextKundennummer(TheClientDto theClientDto) throws EJBExceptionLP {

		Integer i = null;
		try {
			Query query = em.createNamedQuery("KundeejbSelectNextPersonalnummer");
			query.setParameter(1, theClientDto.getMandant());
			i = (Integer) query.getSingleResult();
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// new Exception(ex));
		// }

		return i;
	}

	public Set<Integer> getSpediteurIIdsEinesKunden(Integer kundeIId, TheClientDto theClientDto) {

		Set<Integer> ids = new HashSet();

		Query query = em.createNamedQuery("KundespediteurfindByKundeIId");
		query.setParameter(1, kundeIId);
		Collection<?> c = query.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Kundespediteur ks = (Kundespediteur) it.next();
			ids.add(ks.getSpediteurIId());
		}
		return ids;
	}

	public String getAccountingNr(Integer kundeIId, Integer spediteurIId, TheClientDto theClientDto) {
		try {
			Query query = em.createNamedQuery("KundespediteurfindByKundeIIdSpediteurIId");
			query.setParameter(1, kundeIId);
			query.setParameter(2, spediteurIId);
			Kundespediteur ks = (Kundespediteur) query.getSingleResult();
			return ks.getCAccounting();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public KundespediteurDto kundespediteurFindByPrimaryKey(Integer iId) {
		Kundespediteur kundespediteur = em.find(Kundespediteur.class, iId);
		return KundespediteurDtoAssembler.createDto(kundespediteur);
	}

	public KundeKennungDto kundekennungFindByPrimaryKey(Integer iId) {
		KundeKennung bean = em.find(KundeKennung.class, iId);

		KundeKennungDto dto = new KundeKennungDto();

		dto.setIId(bean.getIId());
		dto.setKennungIId(bean.getKennungIId());
		dto.setKundeIId(bean.getKundeIId());
		dto.setWert(bean.getCWert());

		return dto;
	}

	public void removeKundespediteur(KundespediteurDto dto) {
		Kundespediteur kundespediteur = em.find(Kundespediteur.class, dto.getIId());
		em.remove(kundespediteur);
		em.flush();

	}

	public void removeKundeKennung(KundeKennungDto dto) {
		KundeKennung kundeKennung = em.find(KundeKennung.class, dto.getIId());
		em.remove(kundeKennung);
		em.flush();

	}

	public void updateKundespediteur(KundespediteurDto dto, TheClientDto theClientDto) {
		Integer iId = dto.getIId();
		Kundespediteur bean = em.find(Kundespediteur.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("KundespediteurfindByKundeIIdSpediteurIId");
			query.setParameter(1, dto.getKundeIId());
			query.setParameter(2, dto.getSpediteurIId());
			Integer iIdVorhanden = ((Kundespediteur) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_KUNDESPEDITEUR.UK"));
			}
		} catch (NoResultException ex) {
			// NIX
		}
		setKundespediteurFromKundespediteurDto(bean, dto);

	}

	public void updateKundeKennung(KundeKennungDto dto, TheClientDto theClientDto) {
		Integer iId = dto.getIId();
		KundeKennung bean = em.find(KundeKennung.class, dto.getIId());

		try {
			List<KundeKennung> doppelt = KundeKennungQuery.findByKundeIdKennungId(em, new KundeId(dto.getKundeIId()),
					new KennungId(dto.getKennungIId()));

			for (int i = 0; i < doppelt.size(); i++) {
				Integer iIdVorhanden = doppelt.get(i).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PART_KUNDEKENNUNG.UK"));
				}
			}
		} catch (NoResultException ex) {
			// NIX
		}
		bean.setCWert(dto.getWert());
		bean.setKennungIId(dto.getKennungIId());
		bean.setKundeIId(dto.getKundeIId());
		em.merge(bean);
		em.flush();

	}

	public Integer createKundespediteur(KundespediteurDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("KundespediteurfindByKundeIIdSpediteurIId");
			query.setParameter(1, dto.getKundeIId());
			query.setParameter(2, dto.getSpediteurIId());
			Kundespediteur doppelt = (Kundespediteur) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_KUNDESPEDITEUR.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUNDESPEDITEUR);
			dto.setIId(pk);

			Kundespediteur spediteur = new Kundespediteur(dto.getIId(), dto.getKundeIId(), dto.getSpediteurIId(),
					dto.getNGewichtinkg(), dto.getCAccounting());
			em.persist(spediteur);
			em.flush();
			setKundespediteurFromKundespediteurDto(spediteur, dto);

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	private void setKundespediteurFromKundespediteurDto(Kundespediteur bean, KundespediteurDto dto) {
		bean.setKundeIId(dto.getKundeIId());
		bean.setSpediteurIId(dto.getSpediteurIId());
		bean.setCAccounting(dto.getCAccounting());
		bean.setNGewichtinkg(dto.getNGewichtinkg());
		em.merge(bean);
		em.flush();
	}

	/**
	 * Create einen neuen Kunden aus Lieferant wenn es keinen vorhanden ist.
	 * 
	 * @param iIdLieferant lieferant mit partner
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return Integer kundeKey
	 */
	public Integer createVerstecktenKundenAusLieferant(Integer iIdLieferant, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Integer iIdKunde = null;

		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(iIdLieferant, theClientDto);
		KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(lieferantDto.getPartnerIId(),
				lieferantDto.getMandantCNr(), theClientDto);
		// Neuen versteckten Kunde anlegen wenn Lieferant kein Kunde ist
		if (kundeDto == null) {
			kundeDto = new KundeDto();
			kundeDto.setPartnerDto(lieferantDto.getPartnerDto());
			kundeDto.setPartnerIId(lieferantDto.getPartnerIId());
			kundeDto.setBVersteckterlieferant(Helper.boolean2Short(true));
			// Vorbelegungen werden vom Mandanten geholt
			MandantDto mandant = getMandantFac().mandantFindByPrimaryKey(lieferantDto.getMandantCNr(), theClientDto);
			kundeDto.setMandantCNr(mandant.getCNr());
			kundeDto.setKostenstelleIId(mandant.getIIdKostenstelle());
			kundeDto.setMwstsatzbezIId(mandant.getMwstsatzbezIIdStandardinlandmwstsatz());
			kundeDto.setLieferartIId(mandant.getLieferartIIdKunde());
			kundeDto.setSpediteurIId(mandant.getSpediteurIIdKunde());
			kundeDto.setZahlungszielIId(mandant.getZahlungszielIIdKunde());
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandant.getVkpfArtikelpreislisteIId());
			/**
			 * @todo MB->VF mit WH klaeren (ist auch am Client beim Kundenanlegen
			 *       PanelKundeKopfdaten) PJ 4330
			 */
			kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());
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

	public Integer createKundeAusPartner(Integer partnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Integer iIdKunde = null;

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
		KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerIId, theClientDto.getMandant(),
				theClientDto);
		// Neuen versteckten Kunde anlegen wenn Partner kein Kunde ist
		if (kundeDto == null) {
			kundeDto = new KundeDto();
			kundeDto.setPartnerDto(partnerDto);
			kundeDto.setPartnerIId(partnerDto.getIId());
			kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
			// Vorbelegungen werden vom Mandanten geholt
			MandantDto mandant = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			kundeDto.setMandantCNr(mandant.getCNr());
			kundeDto.setKostenstelleIId(mandant.getIIdKostenstelle());

			// SP4564
			if (partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getLandDto() != null) {
				Integer mwstsatzIId = getPartnerFac()
						.getDefaultMWSTSatzIIdAnhandLand(partnerDto.getLandplzortDto().getLandDto(), theClientDto);
				kundeDto.setMwstsatzbezIId(mwstsatzIId);
			} else {
				kundeDto.setMwstsatzbezIId(mandant.getMwstsatzbezIIdStandardinlandmwstsatz());
			}

			kundeDto.setLieferartIId(mandant.getLieferartIIdKunde());
			kundeDto.setSpediteurIId(mandant.getSpediteurIIdKunde());
			kundeDto.setZahlungszielIId(mandant.getZahlungszielIIdKunde());
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandant.getVkpfArtikelpreislisteIId());

			kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());
			kundeDto.setCWaehrung(theClientDto.getSMandantenwaehrung());
			// SP8446
			kundeDto.setbIstinteressent(Helper.boolean2Short(true));
			kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
			kundeDto.setBDistributor(Helper.boolean2Short(false));
			kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));

			kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
			kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_DEFAULT_BELEGDRUCK_MIT_RABATT);
			boolean b = ((Boolean) parameter.getCWertAsObject());
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(b));

			kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
			kundeDto.setBReversecharge(Helper.boolean2Short(false));

			iIdKunde = createKunde(kundeDto, theClientDto);

		}

		else {
			iIdKunde = kundeDto.getIId();
		}

		return iIdKunde;
	}

	public ArrayList<KundeDto> holeAlleWurzelkundenFuerStruktursicht(boolean bMitVersteckten,
			TheClientDto theClientDto) {
		ArrayList<KundeDto> al = new ArrayList<KundeDto>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		String queryString = "SELECT (SELECT KDR.I_ID FROM PART_KUNDE KDR WHERE KDR.PARTNER_I_ID=KD.PARTNER_I_ID_RECHNUNGSADRESSE AND KDR.MANDANT_C_NR='"
				+ theClientDto.getMandant()
				+ "') AS KUNDE_I_ID_AUS_PARTNER_I_ID FROM PART_KUNDE KD LEFT OUTER JOIN PART_PARTNER PA ON KD.PARTNER_I_ID_RECHNUNGSADRESSE=PA.I_ID WHERE KD.PARTNER_I_ID_RECHNUNGSADRESSE IS NOT NULL AND KD.MANDANT_C_NR='"
				+ theClientDto.getMandant() + "' ORDER BY  PA.C_NAME1NACHNAMEFIRMAZEILE1 ASC";

		org.hibernate.Query query = session.createSQLQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Integer kunde_i_id_rechnungsadresse = (Integer) resultListIterator.next();

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kunde_i_id_rechnungsadresse, theClientDto);
			if (bMitVersteckten == false) {
				if (Helper.short2boolean(kdDto.getPartnerDto().getBVersteckt())) {
					continue;
				}

			}

			al.add(kdDto);

		}

		session.close();
		return al;
	}

	public ArrayList<KundenInfoFuerStrukturAnsicht> holeNaechsteEbene(Integer kundeIId, boolean bMitVersteckten,
			TheClientDto theClientDto) {
		ArrayList<KundenInfoFuerStrukturAnsicht> al = new ArrayList<KundenInfoFuerStrukturAnsicht>();

		KundeDto kdDtoInput = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT  kd,  (SELECT count(kd2.i_id) FROM FLRKunde kd2 WHERE kd2.flrpartnerRechnungsAdresse.i_id=kd.flrpartner.i_id ) FROM FLRKunde kd WHERE kd.flrpartnerRechnungsAdresse.i_id="
				+ kdDtoInput.getPartnerIId() + "";

		if (bMitVersteckten == false) {
			queryString += " AND kd.flrpartner.b_versteckt=0";
		}

		queryString += " ORDER BY kd.flrpartner.c_name1nachnamefirmazeile1 ASC";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			Object[] oZeile = (Object[]) resultListIterator.next();

			FLRKunde kd = (FLRKunde) oZeile[0];

			Boolean bHasLieferadressen = Boolean.FALSE;
			Long l = (Long) oZeile[1];
			if (l != null && l > 0) {
				bHasLieferadressen = true;
			}

			kd.getFlrpartner().getC_name1nachnamefirmazeile1();

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kd.getI_id(), theClientDto);

			al.add(new KundenInfoFuerStrukturAnsicht(kdDto, bHasLieferadressen));

		}

		session.close();
		return al;
	}

	public void provisionsempfaengerErsetzen(ArrayList personalIId, Integer personalIIdNeu, TheClientDto theClientDto) {

		for (int i = 0; i < personalIId.size(); i++) {

			Session session = FLRSessionFactory.getFactory().openSession();

			String s = "SELECT kunde.i_id FROM FLRKunde kunde WHERE kunde.personal_i_id_bekommeprovision="
					+ personalIId.get(i);

			org.hibernate.Query inventurliste = session.createQuery(s);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList alDaten = new ArrayList();

			while (resultListIterator.hasNext()) {

				Integer kundeIId = (Integer) resultListIterator.next();

				Kunde kunde = em.find(Kunde.class, kundeIId);
				kunde.setPersonalIIdBekommeprovision(personalIIdNeu);
				kunde.setPersonalIIdAendern(theClientDto.getIDPersonal());
				kunde.setTAendern(getTimestamp());
				em.merge(kunde);
				em.flush();
			}

			session.close();

		}

	}

	public String generateKundennr(Integer kundeIId) {

		Kunde kunde = em.find(Kunde.class, kundeIId);
		Partner partner = em.find(Partner.class, kunde.getPartnerIId());

		// --generiere eine Kundennummer zB. G01.
		String kurznr = generateNextFreeKurznr(partner.getCName1nachnamefirmazeile1().substring(0, 1).toUpperCase());

		kunde.setCKurznr(kurznr);

		em.merge(kunde);
		em.flush();

		return kunde.getCKurznr();

	}

	public void removeKunde(KundeDto kundeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("kundeDto == null"));
		}

		// Erst den Kunde loeschen.
		try {
			Kunde toRemove = em.find(Kunde.class, kundeDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {

				HvDtoLogger<KundeDto> partnerLogger = new HvDtoLogger<KundeDto>(em, kundeDtoI.getIId(), theClientDto);
				partnerLogger.logDelete(kundeDtoI);

				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
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
	 * @param kundeDto     KundeDto
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateKunde(KundeDto kundeDto, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (kundeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("kundeDto == null"));
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

			// PJ21181 Kombination von Mindermengenzuschlag und Mindestbestellwert nicht
			// moeglich
			if (Helper.short2boolean(kundeDto.getBMindermengenzuschlag()) == true
					&& kundeDto.getNMindestbestellwert() != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KUNDE_KOMBINATION_MMZ_MINBW_NICHT_MOEGLICH,
						new Exception("FEHLER_KUNDE_KOMBINATION_MMZ_MINBW_NICHT_MOEGLICH"));
			}

			PartnerDto partnerDtovorher = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerDto().getIId(),
					theClientDto);

			// Partner.
			// SP7031
			if (!partnerDtovorher.equals(kundeDto.getPartnerDto())) {
				getPartnerFac().updatePartner(kundeDto.getPartnerDto(), theClientDto);
			}

			Kunde kunde = em.find(Kunde.class, iId);
			if (kunde == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			KundeDto dtoVorher = assembleKundeDto(kunde);

			// Wer wann aendert.
			kundeDto.setPersonalAendernIID(theClientDto.getIDPersonal());
			kundeDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			setKundeFromKundeDto(kunde, kundeDto);

			HvDtoLogger<KundeDto> artikelLogger = new HvDtoLogger<KundeDto>(em, kunde.getIId(), theClientDto);
			artikelLogger.log(dtoVorher, kundeDto);

			String sToLog = kundeDto.toString();
			myLogger.logKritisch(sToLog.substring(0, ILPLogger.MESSAGELENGTH_MAX));
			myLogger.logKritisch(sToLog.substring(ILPLogger.MESSAGELENGTH_MAX, sToLog.length()));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void workoutDebitoren(KundeDto kundeDto, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (kundeDto.getUpdateModeDebitorenkonto() == KundeDto.I_UPD_DEBITORENKONTO_PRUEFE_AUF_DOPPELTE) {
				/**
				 * @todo MB->JO andere methode verwenden und die alte loeschen PJ 4338
				 */

//				long t = System.currentTimeMillis();
				KundeDto[] kundeDtos = findAllKundeByDebitorennr(kundeDto, theClientDto);
//				myLogger.info(">" + (System.currentTimeMillis() - t));

				verifyGleicheFibuBedingungen(kundeDto, kundeDtos, theClientDto);

				if (kundeDtos.length > 0) {
					List<Object> entries = buildPartnerExc(Arrays.asList(kundeDtos));
					throw new EJBExceptionLP(EJBExceptionLP.WARNUNG_KTO_BESETZT, entries, null);
				}
			}

			KontoDto kontoDtoIch = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
					kundeDto.getIDebitorenkontoAsIntegerNotiId().toString(), FinanzServiceFac.KONTOTYP_DEBITOR,
					kundeDto.getMandantCNr(), theClientDto);

			if (kontoDtoIch == null) {
				// --Kto nicht gefunden: create

				// AD: gleiche Methode verwenden wie fuer Nummer generieren!
				KontoDto k = createDebitorenkontoZuKundenAutomatisch(kundeDto.getIId(), true,
						kundeDto.getIDebitorenkontoAsIntegerNotiId().toString(), theClientDto);
				/*
				 * KontoDto k = new KontoDto(); k.setIId(null);
				 * k.setCNr(kundeDto.getIDebitorenkontoAsIntegerNotiId() .toString());
				 * k.setCBez(kundeDto.getPartnerDto() .getCName1nachnamefirmazeile1());
				 * UvaartDto uvaartDto = getFinanzServiceFac() .uvaartFindByCnrMandant(
				 * FinanzServiceFac.UVAART_NICHT_DEFINIERT, theClientDto);
				 * k.setUvaartIId(uvaartDto.getIId());
				 * k.setKontoartCNr(FinanzServiceFac.KONTOART_NICHT_STEUERBAR);
				 * k.setBAutomeroeffnungsbuchung(Helper.boolean2Short(true));
				 * k.setBAllgemeinsichtbar(Helper.boolean2Short(true));
				 * k.setBManuellbebuchbar(Helper.boolean2Short(true)); / @todo JO->WH,MB
				 * Innergemeinschaftlich, ... PJ 4340
				 */
				/*
				 * k.setKontotypCNr(FinanzServiceFac.KONTOTYP_DEBITOR);
				 * k.setMandantCNr(kundeDto.getMandantCNr());
				 * 
				 * k = getFinanzFac().createKonto(k, theClientDto);
				 */
				kundeDto.setIidDebitorenkonto(k.getIId());
			} else {
				PartnerDto partnerDto = kundeDto.getPartnerDto();
				if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
					Integer finanzamtId = getFinanzFac().findDefaultFinanzamtIdForPartner(partnerDto, theClientDto);
					kontoDtoIch.setFinanzamtIId(finanzamtId);
				}

				// --Kto gefunden: update
				kontoDtoIch.setCBez(partnerDto.getCName1nachnamefirmazeile1());
				kundeDto.setIidDebitorenkonto(kontoDtoIch.getIId());
				getFinanzFac().updateKonto(kontoDtoIch, theClientDto);
			}
			kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Wenn es mehr als einen Lieferanten (mit der gleichen Kreditorennr) gibt, muss
	 * gelten: a) Ohne Fibu: Abweichendes UST-Land muss das gleiche sein b) Mit
	 * Fibu: Finanzamt muss das gleiche sein
	 * 
	 * @param lieferantDtos die zu pruefenden Lieferanten (mit der gleichen
	 *                      Kreditorennummer)
	 */
	private void verifyGleicheFibuBedingungen(KundeDto updateKundeDto, KundeDto[] kundeDtos, TheClientDto theClientDto)
			throws RemoteException {
		List<KundeDto> all = new ArrayList<KundeDto>(Arrays.asList(kundeDtos));
		all.add(0, updateKundeDto);
		if (all.size() < 2)
			return;

		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			verifyGleichesUSTLand(all, theClientDto);
		}
	}

	private void verifyGleichesUSTLand(List<KundeDto> kundeDtos, TheClientDto theClientDto) throws RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		Integer mandantFinanzamtId = mandantDto.getPartnerIIdFinanzamt();
		if (mandantFinanzamtId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_FINANZAMT_IM_MANDANT_NITCH_DEFINIERT,
					"Finanzamt im Mandant " + mandantDto.getCNr() + "' nicht definiert");
		}

		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPrimaryKey(mandantFinanzamtId,
				theClientDto.getMandant(), theClientDto);
		Integer ustlandId = null;
		for (KundeDto kundeDto : kundeDtos) {
			Integer landId = kundeDto.getPartnerDto().getLandIIdAbweichendesustland();
			if (landId == null) {
				landId = finanzamtDto.getPartnerDto().getLandplzortDto().getIlandID();
			}
			if (ustlandId == null) {
				ustlandId = landId;
			}
			if (!ustlandId.equals(landId)) {
				List<Object> entries = buildPartnerExc(kundeDtos);
				entries.add(0, kundeDto.getIId());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KUNDE_ABWEICHENDESUSTLAND_UNTERSCHIEDLICH, entries,
						null);
			}
		}
	}

	private List<Object> buildPartnerExc(List<KundeDto> kundeDtos) {
		List<Object> entries = new ArrayList<Object>();
		for (KundeDto kundeDto : kundeDtos) {
			entries.add(kundeDto.getPartnerDto().formatFixTitelName1Name2());
			entries.add(kundeDto.getPartnerDto().formatAdresse());
		}
		return entries;
	}

	private String generateNextFreeKurznr(String sKurznrFirstLetterI) throws EJBExceptionLP {

		for (int i = 0; i <= KundeFac.MAX_KURZNR_LP5; i++) {
			String toSearch = sKurznrFirstLetterI + Helper.fitString2LengthAlignRight(i + "", 2, '0');

			Query query2 = em.createNamedQuery("KundefindByCKurznr");
			query2.setParameter(1, toSearch);

			Collection c2 = query2.getResultList();

			if (c2.size() == 0) {
				return toSearch;
			}

		}

		// Pro Buchstabe 99 Kunden.
		throw new EJBExceptionLP(EJBExceptionLP.WARNUNG_99_KUNDEN_PRO_BUCHSTABE, "");

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

	public KundeDto kundeFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");

		KundeDto kundeDto = null;

		try {
			kundeDto = kundeFindByPrimaryKeyImpl(iId, theClientDto);
			if (kundeDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return kundeDto;
	}

	private void setKundeFromKundeDto(Kunde kunde, KundeDto kundeDto) {
		kunde.setBAkzeptiertteillieferung(kundeDto.getBAkzeptiertteillieferung());
		kunde.setBDistributor(kundeDto.getBDistributor());
		kunde.setBIstinteressent(kundeDto.getbIstinteressent());
		kunde.setBIstreempfaenger(kundeDto.getBIstreempfaenger());
		kunde.setBLsgewichtangeben(kundeDto.getBLsgewichtangeben());
		kunde.setBMindermengenzuschlag(kundeDto.getBMindermengenzuschlag());
		kunde.setBMonatsrechnung(kundeDto.getBMonatsrechnung());
		kunde.setBPreiseanlsandrucken(kundeDto.getBPreiseanlsandrucken());
		kunde.setBRechnungsdruckmitrabatt(kundeDto.getBRechnungsdruckmitrabatt());
		kunde.setBSammelrechnung(kundeDto.getBSammelrechnung());
		kunde.setPartnerIId(kundeDto.getPartnerIId());
		kunde.setMandantCNr(kundeDto.getMandantCNr());
		kunde.setWaehrungCNr(kundeDto.getCWaehrung());
		kunde.setMwstsatzIId(((Mwstsatzbez) em.find(Mwstsatzbez.class, kundeDto.getMwstsatzbezIId())).getIId());
		kunde.setVkpfartikelpreislisteIIdstdpreisliste(kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste());
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
		kunde.setPersonalIIdBekommeprovision(kundeDto.getPersonaliIdProvisionsempfaenger());
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
		kunde.setPartnerIIdRechnungsadresse(kundeDto.getPartnerIIdRechnungsadresse());
		kunde.setCHinweisextern(kundeDto.getSHinweisextern());
		kunde.setCHinweisintern(kundeDto.getSHinweisintern());
		kunde.setFZessionsfaktor(kundeDto.getFZessionsfaktor());
		kunde.setBReversecharge(kundeDto.getBReversecharge());
		kunde.setBVersteckterlieferant(kundeDto.getBVersteckterlieferant());
		kunde.setTErwerbsberechtigung(kundeDto.getTErwerbsberechtigung());
		kunde.setCErwerbsberechtigungsbegruendung(kundeDto.getCErwerbsberechtigungsbegruendung());
		kunde.setILieferdauer(kundeDto.getILieferdauer());
		kunde.setCIdExtern(kundeDto.getCIdExtern());
		kunde.setBZollpapier(kundeDto.getBZollpapier());
		kunde.setReversechargeartId(kundeDto.getReversechargeartId());
		kunde.setNKupferzahl(kundeDto.getNKupferzahl());
		kunde.setBZuschlagInklusive(kundeDto.getBZuschlagInklusive());
		kunde.setNMindestbestellwert(kundeDto.getNMindestbestellwert());
		kunde.setCEmailRechnungsempfang(kundeDto.getCEmailRechnungsempfang());
		kunde.setLaenderartCnr(kundeDto.getLaenderartCnr());
		kunde.setVerrechnungsmodellIId(kundeDto.getVerrechnungsmodellIId());
		kunde.setIMaxRepos(kundeDto.getIMaxRepos());
		kunde.setBRechnungJeLieferadresse(kundeDto.getBRechnungJeLieferadresse());
		kunde.setfVerpackungskostenInProzent(kundeDto.getfVerpackungskostenInProzent());
		kunde.setBVkpreisAnhandLSDatum(kundeDto.getBVkpreisAnhandLSDatum());

		em.merge(kunde);
		em.flush();
	}

	/**
	 * Referenz auf PartnerFacHome erzeugen.
	 * 
	 * @param kunde Kunde
	 * @return KundeDto
	 */
	private KundeDto assembleKundeDto(Kunde kunde) {
		return KundeDtoAssembler.createDto(kunde);
	}

	public void updateKundeRechnungsadresse(KundeDto kundeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("kundeDtoI == null"));
		}

		if (kundeDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("kundeDtoI.getPartnerDto() == null"));
		}

		try {
			// Partnerrechnungsadresse des Kunden CU.
			Integer iIdPartnerRE = kundeDtoI.getPartnerRechnungsadresseDto().getIId();
			if (iIdPartnerRE != null) {
				// Update.
				getPartnerFac().updatePartner(kundeDtoI.getPartnerRechnungsadresseDto(), theClientDto);
			} else {
				// Create; Rechnungsadr. ist erfasst worden.
				iIdPartnerRE = getPartnerFac().createPartner(kundeDtoI.getPartnerRechnungsadresseDto(), theClientDto);
			}
			kundeDtoI.setPartnerIIdRechnungsadresse(iIdPartnerRE);

			// Kunde updaten.
			updateKunde(kundeDtoI, theClientDto);

			// Der ausgewaehlte Partner muss jetzt noch als Kunde angelegt
			// werden (wenn noch nicht vorhanden)
			KundeDto kundeDtoREAdr = kundeFindByiIdPartnercNrMandantOhneExc(iIdPartnerRE, kundeDtoI.getMandantCNr(),
					theClientDto);
			if (kundeDtoREAdr == null) {
				// er ist noch nicht angelegt -> jetzt anlegen
				KundeDto kundeDtoNew = new KundeDto();
				kundeDtoNew.setMandantCNr(kundeDtoI.getMandantCNr());
				kundeDtoNew.setPartnerIId(kundeDtoI.getPartnerIIdRechnungsadresse());
				kundeDtoNew.setPartnerDto(kundeDtoI.getPartnerRechnungsadresseDto());
				kundeDtoNew.setKostenstelleIId(kundeDtoI.getKostenstelleIId());
				kundeDtoNew.setMwstsatzbezIId(kundeDtoI.getMwstsatzbezIId());
				kundeDtoNew.setLieferartIId(kundeDtoI.getLieferartIId());
				kundeDtoNew.setSpediteurIId(kundeDtoI.getSpediteurIId());
				kundeDtoNew.setPersonaliIdProvisionsempfaenger(kundeDtoI.getPersonaliIdProvisionsempfaenger());
				kundeDtoNew
						.setVkpfArtikelpreislisteIIdStdpreisliste(kundeDtoI.getVkpfArtikelpreislisteIIdStdpreisliste());
				kundeDtoNew.setZahlungszielIId(kundeDtoI.getZahlungszielIId());
				kundeDtoNew.setCWaehrung(kundeDtoI.getCWaehrung());
				kundeDtoNew.setBAkzeptiertteillieferung(kundeDtoI.getBAkzeptiertteillieferung());
				kundeDtoNew.setBIstkunde(kundeDtoI.getBIstkunde());
				kundeDtoNew.setBDistributor(kundeDtoI.getBDistributor());
				kundeDtoNew.setbIstinteressent(kundeDtoI.getbIstinteressent());
				kundeDtoNew.setBIstreempfaenger(kundeDtoI.getBIstreempfaenger());
				kundeDtoNew.setBLsgewichtangeben(kundeDtoI.getBLsgewichtangeben());
				kundeDtoNew.setBMindermengenzuschlag(kundeDtoI.getBMindermengenzuschlag());
				kundeDtoNew.setBMonatsrechnung(kundeDtoI.getBMonatsrechnung());
				kundeDtoNew.setBPreiseanlsandrucken(kundeDtoI.getBPreiseanlsandrucken());
				kundeDtoNew.setBRechnungsdruckmitrabatt(kundeDtoI.getBRechnungsdruckmitrabatt());
				kundeDtoNew.setBReversecharge(kundeDtoI.getBReversecharge());
				kundeDtoNew.setBSammelrechnung(kundeDtoI.getBSammelrechnung());
				kundeDtoNew.setBsIndikator(kundeDtoI.getBsIndikator());
				kundeDtoNew.setBVersteckterlieferant(kundeDtoI.getBVersteckterlieferant());
				kundeDtoNew.setReversechargeartId(kundeDtoI.getReversechargeartId());
				createKunde(kundeDtoNew, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeKundeRechnungsadresse(KundeDto kundeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (kundeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("kundeDtoI == null"));
		}

		// 1 Partnerrechnungsadresse des Lieferanten = null.
		// Wer hat wann geloescht?
		Integer iIdPartnerRE = kundeDtoI.getPartnerRechnungsadresseDto().getIId();

		if (iIdPartnerRE != null) {
			// Update.
			kundeDtoI.setPartnerIIdRechnungsadresse(null);
			kundeDtoI.setPersonalAendernIID(theClientDto.getIDPersonal());
			kundeDtoI.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			kundeDtoI.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);
			// 3 Kunde update.
			updateKunde(kundeDtoI, theClientDto);
		}
	}

	public KundeDto kundeFindByPrimaryKeyOhneExc(Integer iIdKundeI, TheClientDto theClientDto) {
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

	private KundeDto kundeFindByPrimaryKeyImpl(Integer iId, TheClientDto theClientDto) throws RemoteException {
		Kunde kunde = em.find(Kunde.class, iId);
		if (kunde == null)
			return null;

		KundeDto kundeDto = assembleKundeDto(kunde);
		return kundeFindByPrimaryKeyImpl(kundeDto, theClientDto);
		// kundeDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
		// kundeDto.getPartnerIId(), theClientDto));
		//
		// // Partnerrechnungsadresse.
		// Integer iIdPartnerRE = kundeDto.getPartnerIIdRechnungsadresse();
		//
		// if (iIdPartnerRE != null) {
		// kundeDto.setPartnerRechnungsadresseDto(getPartnerFac()
		// .partnerFindByPrimaryKey(iIdPartnerRE, theClientDto));
		// }
		//
		// KontoDtoSmall k = null;
		// if (kundeDto.getIidDebitorenkonto() != null) {
		// k = getFinanzFac().kontoFindByPrimaryKeySmall(
		// kundeDto.getIidDebitorenkonto());
		// }
		// Integer iD = null;
		// if (k != null && k.getCNr() != null) {
		// iD = new Integer(Integer.parseInt(k.getCNr()));
		// }
		// kundeDto.setIDebitorenkontoAsIntegerNotiId(iD);
		//
		// return kundeDto;
	}

	private KundeDto kundeFindByPrimaryKeyImpl(KundeDto kundeDto, TheClientDto theClientDto) throws RemoteException {
		kundeDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto));

		// Partnerrechnungsadresse.
		Integer iIdPartnerRE = kundeDto.getPartnerIIdRechnungsadresse();

		if (iIdPartnerRE != null) {
			kundeDto.setPartnerRechnungsadresseDto(getPartnerFac().partnerFindByPrimaryKey(iIdPartnerRE, theClientDto));
		}

		KontoDtoSmall k = null;
		if (kundeDto.getIidDebitorenkonto() != null) {
			k = getFinanzFac().kontoFindByPrimaryKeySmall(kundeDto.getIidDebitorenkonto());
		}
		Integer iD = null;
		if (k != null && k.getCNr() != null) {
			iD = new Integer(Integer.parseInt(k.getCNr()));
		}
		kundeDto.setIDebitorenkontoAsIntegerNotiId(iD);

		return kundeDto;
	}

	public KundeDto[] kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
			Integer iIdVkpfArtikelpreislisteStdpreislisteI, TheClientDto theClientDto) {

		if (iIdVkpfArtikelpreislisteStdpreislisteI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("iIdVkpfArtikelpreislisteStandardpreislisteI == null"));
		}

		KundeDto[] aKundeDto = new KundeDto[0];

		try {
			Query query = em.createNamedQuery("KundefindByVkpfArtikelpreislisteIIdStdpreisliste");
			query.setParameter(1, iIdVkpfArtikelpreislisteStdpreislisteI);
			Collection<Kunde> cl = query.getResultList();

			aKundeDto = KundeDtoAssembler.createDtos(cl);
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(),
					"iIdVkpfArtikelpreislisteStdpreislisteI=" + iIdVkpfArtikelpreislisteStdpreislisteI, t);
		}

		return aKundeDto;
	}

	public boolean pruefeObFixpreiseBeiWaehrungsaenderungVorhanden(Integer kundeIId, String waehrungCNr,
			TheClientDto theClientDto) {

		Kunde kunde = em.find(Kunde.class, kundeIId);

		if (!waehrungCNr.equals(kunde.getWaehrungCNr())) {

			// Nachsehen, ob es Fixpreise in Kundesoko gibt

			String sQuery = "SELECT count(s) FROM FLRKundesokomengenstaffel s WHERE s.flrkundesoko.kunde_i_id="
					+ kundeIId;

			sQuery += " AND s.n_fixpreis <> 0";

			org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList alDaten = new ArrayList();
			while (resultListIterator.hasNext()) {
				Long l = (Long) resultListIterator.next();

				if (l != null && l > 0) {
					return true;
				}
			}

		}

		return false;
	}

	private KundeDto[] findAllKundeByDebitorennr(KundeDto kundeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<KundeDto> list = new ArrayList<KundeDto>();
		if (kundeDtoI.getIDebitorenkontoAsIntegerNotiId() != null) {
			Session session = null;
			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				// Kunden finden
				Criteria c = session.createCriteria(FLRKunde.class);
				// alle mit diesem Debitorenkonto
				c.createCriteria(KundeFac.FLR_KONTO).add(Restrictions.eq(FinanzFac.FLR_KONTO_C_NR,
						kundeDtoI.getIDebitorenkontoAsIntegerNotiId().toString()));
				// aber nicht denselben
				c.add(Restrictions.not(Restrictions.eq(KundeFac.FLR_KUNDE_I_ID, kundeDtoI.getIId())));
				c.add(Restrictions.eq(KundeFac.FLR_KUNDE_MANDANT_C_NR, theClientDto.getMandant()));
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

	public Integer erstelleBzwHoleKundeAnhandKbez(String cKbez, TheClientDto theClientDto) {

		// Als erstes Partner Suchen
		List<Partner> partners = PartnerQuery.listByKbez(em, cKbez);

		PartnerDto pDto = null;
		try {
			if (partners.size() == 0) {
				pDto = new PartnerDto();
				pDto.setCKbez(cKbez);
				pDto.setCName1nachnamefirmazeile1(cKbez);
				pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
				pDto.setBVersteckt(false);
				pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

				Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

				pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

			} else {
				Partner partner = partners.get(0);
				pDto = getPartnerFac().partnerFindByPrimaryKey(partner.getIId(), theClientDto);
			}

			// Kunde dazu anlegen

			KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
					theClientDto.getMandant(), theClientDto);

			if (kundeDto == null) {

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				kundeDto = new KundeDto();
				kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());
				kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId());
				kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
				kundeDto.setbIstinteressent(new Short((short) 0));

				kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());

				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				// Vorbelegungen werden vom Mandanten geholt

				kundeDto.setMandantCNr(mandantDto.getCNr());
				kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
				kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
				kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
				kundeDto.setCWaehrung(mandantDto.getWaehrungCNr());
				kundeDto.setbIstinteressent(Helper.boolean2Short(false));
				kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
				kundeDto.setBDistributor(Helper.boolean2Short(false));
				kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
				kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
				kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
				kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
				kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
				kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
				kundeDto.setBReversecharge(Helper.boolean2Short(false));

				// damit die Debitorenkto. nicht anschlaegt.
				kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

				kundeDto.setPartnerDto(pDto);

				Integer kundeIId = getKundeFac().createKunde(kundeDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return pDto.getIId();
	}

	public KundeDto[] kundefindByKontoIIdDebitorenkonto(Integer kontoIId) {
		Query query = em.createNamedQuery("KundefindByKontoIIdDebitorenkonto");
		query.setParameter(1, kontoIId);
		Collection<Kunde> cl = query.getResultList();

		KundeDto[] kundeDto = KundeDtoAssembler.createDtos(cl);
		return kundeDto;
	}

	public KundeDto kundeFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI, String cNrMandantI,
			TheClientDto theClientDto) {

		// precondition
		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNrMandantI == null"));
		}
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		KundeDto kundeDto = null;
		try {
			Query query = em.createNamedQuery("KundefindByiIdPartnercNrMandant");
			query.setParameter(1, iIdPartnerI);
			query.setParameter(2, cNrMandantI);
			Kunde kunde = (Kunde) query.getSingleResult();
			kundeDto = assembleKundeDto(kunde);
		} catch (NoResultException ex) {
			// nothing here
		}
		return kundeDto;
	}

	public KundeDto kundeFindByAnsprechpartnerIdcNrMandantOhneExc(Integer ansprechpartnerId, String mandantCnr,
			TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(ansprechpartnerId, "ansprechpartnerId");
		Validator.notEmpty(mandantCnr, "mandantCnr");

		Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, ansprechpartnerId);
		if (ansprechpartner == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ansprechpartnerId.toString());
		}

		KundeDto kundeDto = kundeFindByiIdPartnercNrMandantOhneExc(ansprechpartner.getPartnerIId(), mandantCnr,
				theClientDto);
		if (kundeDto != null) {
			kundeDto = kundeFindByPrimaryKeyImpl(kundeDto, theClientDto);
		}
		return kundeDto;
	}

	public KundeDto kundeFindByLieferantenCnrMandantCnrNull(String lieferantenCnr, String mandantCnr) {
		if (Helper.isStringEmpty(lieferantenCnr))
			return null;
		if (Helper.isStringEmpty(mandantCnr))
			return null;

		KundeDto kundeDto = null;

		try {
			HvTypedQuery<Kunde> query = KundeQuery.byLieferantCnrMandantCnr(em, lieferantenCnr.trim(),
					mandantCnr.trim());
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

		List<Kunde> kunden = KundeQuery.listByMandantCnr(em, theClientDto.getMandant());
		List<KundeDto> kundeDtos = new ArrayList<KundeDto>(kunden.size());
		for (Kunde kunde : kunden) {
			kundeDtos.add(kundeFindByPrimaryKey(kunde.getIId(), theClientDto));
		}

		return kundeDtos;
	}

	public List<KundeDto> kundeFindByKbezMandantCnr(String kbez, TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(kbez, "kbez");
		Validator.notEmpty(theClientDto.getMandant(), "mandantCnr");

		List<Partner> partners = PartnerQuery.listByKbez(em, kbez);
		List<KundeDto> kundeDtos = new ArrayList<KundeDto>();
		for (Partner partner : partners) {
			KundeDto kundeDto = kundeFindByiIdPartnercNrMandantOhneExc(partner.getIId(), theClientDto.getMandant(),
					theClientDto);
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
	 * @param kundeIId     Integer
	 * @param theClientDto String
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	public KontoDto createDebitorenkontoZuKundenAutomatisch(Integer kundeIId, boolean kontoAnlegen,
			String kontonummerVorgabe, TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			// Kunde holen
			KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto);
			// Konto anlegen
			KontoDto kontoDto = getFinanzFac().createKontoFuerPartnerAutomatisch(kundeDto.getPartnerDto(),
					FinanzServiceFac.KONTOTYP_DEBITOR, kontoAnlegen, kontonummerVorgabe, theClientDto);
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
	 * @param kundeIId     Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP wenn der Kunde keine UID-Nummer hat
	 */
	public void pruefeKundenUIDNummer(Integer kundeIId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			LandplzortDto mandantLandplzort = mandantDto.getPartnerDto().getLandplzortDto();
			// wenn der Mandant ein LKZ hat und EU-Mitglied ist
			if (mandantLandplzort != null && getSystemFac().isEUMitglied(mandantLandplzort.getLandDto())) {
				KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto);
				LandplzortDto kundeLandplzort = kundeDto.getPartnerDto().getLandplzortDto();
				// wenn der Kunde EU-Mitglied und keine UID hat -> Exception
				if (kundeLandplzort != null && getSystemFac().isEUMitglied(kundeLandplzort.getLandDto())
						&& kundeDto.getPartnerDto().getCUid() == null) {

					// SP3858 Wenn UID Laenge des Landes gleich 0, dann Pruefung
					// auslassen
					if (kundeDto.getPartnerDto().getLandplzortDto().getLandDto().getILaengeuidnummer() > 0) {
						throw new EJBExceptionLP(EJBExceptionLP.WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT,
								new Exception("Kunde i_id=" + kundeIId + " hat keine UID-Nummer"));
					}
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
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @throws EJBExceptionLP
	 */
	private void checkInputParamsZielQuellKundeDtos(KundeDto kundeZielDto, KundeDto kundeQuellDto)
			throws EJBExceptionLP {

		if (kundeZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kundeDto == null (Ziel)"));
		}

		if (kundeZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kundeDto.getIId() == null (Ziel)"));
		}

		if (kundeQuellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kundeDto == null (Quell)"));
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
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignLieferscheinBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// LS_LIEFERSCHEIN kunde_i_id_lieferadresse (mandant_cnr) = normale
		// kundenadresse
		try {
			LieferscheinDto[] aLieferscheinDtos = null;
			aLieferscheinDtos = getLieferscheinFac().lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(
					kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aLieferscheinDtos.length; j++) {
				if (aLieferscheinDtos[j] != null) {
					Lieferschein zeile = em.find(Lieferschein.class, aLieferscheinDtos[j].getIId());
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
			aLieferscheinDtos = getLieferscheinFac().lieferscheinFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
					kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aLieferscheinDtos.length; j++) {
				if (aLieferscheinDtos[j] != null) {
					aLieferscheinDtos[j].setKundeIIdRechnungsadresse(kundeZielDto.getIId());
					getLieferscheinFac().updateLieferscheinOhneWeitereAktion(aLieferscheinDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public void reassignBestellpositionBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		Query query = em.createNamedQuery("BestellpositionfindByKundeIId");
		query.setParameter(1, kundeQuellDto.getIId());
		Collection cl = query.getResultList();
		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Bestellposition b = (Bestellposition) it.next();
			b.setKundeIId(kundeZielDto.getIId());
			em.merge(b);
			em.flush();
		}
	}

	/**
	 * H&auml;ngt bei der Reklamation (falls vorhanden) den Kunden vom Quellkunden
	 * auf den Zielkunden um
	 * 
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignReklamationBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// REKLA_REKLAMATION kunde_i_id (mandant_cnr)

		ReklamationDto[] aReklamationDtos = null;

		aReklamationDtos = getReklamationFac().reklamationFindByKundeIIdMandantCNrOhneExc(kundeQuellDto.getIId(),
				mandantCNr);
		for (int j = 0; j < aReklamationDtos.length; j++) {
			if (aReklamationDtos[j] != null) {

				Reklamation zeile = em.find(Reklamation.class, aReklamationDtos[j].getIId());
				zeile.setKundeIId(kundeZielDto.getIId());
				zeile.setAnsprechpartnerIId(null);
				em.merge(zeile);
				em.flush();

			}
		}

	}

	public void reassignInseratBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

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
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignRechnungBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// die bei kunden hinterlegte adresse wird in der RE als
		// statistikadresse verwendet
		// die beim kunden hinterlegte rechnungsadresse wird in der RE als kunde
		// verwendet, falls vorhanden

		// RECH_RECHNUNG kunde_i_id (mandant_cnr)
		try {
			RechnungDto[] aRechnungDtos = null;
			aRechnungDtos = getRechnungFac().rechnungFindByKundeIIdMandantCNrOhneExc(kundeQuellDto.getIId(),
					mandantCNr);
			for (int j = 0; j < aRechnungDtos.length; j++) {
				if (aRechnungDtos[j] != null) {

					Rechnung zeile = em.find(Rechnung.class, aRechnungDtos[j].getIId());
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
					.rechnungFindByKundeIIdStatistikadresseMandantCNrOhneExc(kundeQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aRechnungDtos.length; j++) {
				if (aRechnungDtos[j] != null) {
					aRechnungDtos[j].setKundeIIdStatistikadresse(kundeZielDto.getIId());
					getRechnungFac().updateRechnungBeimZusammenfuehren(aRechnungDtos[j]);
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
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */

	public void reassignEinkaufsangebotZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) {

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

	public void reassignAuftragBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);
		// AUFT_AUFTRAG - kunde_i_id_auftragsadresse (mandant_cnr)
		try {
			AuftragDto[] aAuftragDtos = null;
			aAuftragDtos = getAuftragFac().auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(kundeQuellDto.getIId(),
					mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					Auftrag zeile = em.find(Auftrag.class, aAuftragDtos[j].getIId());
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
			aAuftragDtos = getAuftragFac().auftragFindByKundeIIdLieferadresseMandantCNrOhneExc(kundeQuellDto.getIId(),
					mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					aAuftragDtos[j].setKundeIIdLieferadresse(kundeZielDto.getIId());
					getAuftragFac().updateAuftragOhneWeitereAktion(aAuftragDtos[j], theClientDto);
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
			aAuftragDtos = getAuftragFac().auftragFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
					kundeQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					aAuftragDtos[j].setKundeIIdRechnungsadresse(kundeZielDto.getIId());
					getAuftragFac().updateAuftragOhneWeitereAktion(aAuftragDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public boolean sindBeiQuellUndZielKundeSokosVorhanden(Integer partnerIIdQuelle, Integer partnerIIdZiel,
			TheClientDto theClientDto) {
		KundeDto kundeDtoQuelle = kundeFindByiIdPartnercNrMandantOhneExc(partnerIIdQuelle, theClientDto.getMandant(),
				theClientDto);
		try {
			int iKundeSokosQuelle = 0;
			if (kundeDtoQuelle != null) {
				KundesokoDto[] sokoQuelle = getKundesokoFac().kundesokoFindByKundeIId(kundeDtoQuelle.getIId());
				iKundeSokosQuelle = sokoQuelle.length;
			}

			KundeDto kundeDtoZiel = kundeFindByiIdPartnercNrMandantOhneExc(partnerIIdZiel, theClientDto.getMandant(),
					theClientDto);
			int iKundeSokosZiel = 0;
			if (kundeDtoZiel != null) {
				KundesokoDto[] sokoZiel = getKundesokoFac().kundesokoFindByKundeIId(kundeDtoZiel.getIId());
				iKundeSokosZiel = sokoZiel.length;
			}

			if (iKundeSokosQuelle > 0 && iKundeSokosZiel > 0) {
				return true;
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return false;

	}

	/**
	 * H&auml;ngt die Kundesoko auf den Kunden um, von welchem sie gew&uuml;nscht
	 * werden; die Soko vom anderen Kunden werden inkl. Mengenstaffel gel&ouml;scht
	 * 
	 * @param kundeZielDto      KundeDto
	 * @param kundeQuellDto     KundeDto
	 * @param kundesokoKundeIId KundenIId von dem Kunden, von welchem die Sokos
	 *                          &uuml;bernommen werden
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignKundesokoBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		try {

			// ersetze iids bei sokos/mengenst. der quelle durch ziel
			KundesokoDto[] aKundesokoQuellDtos = null;
			aKundesokoQuellDtos = getKundesokoFac().kundesokoFindByKundeIIdOhneExc(kundeQuellDto.getIId());
			for (int j = 0; j < aKundesokoQuellDtos.length; j++) {
				if (aKundesokoQuellDtos[j] != null) {
					Kundesoko zeile = em.find(Kundesoko.class, aKundesokoQuellDtos[j].getIId());
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
	 * H&auml;ngt beim Sachbearbeiter den Quellkunden auf den Zielkunden um
	 * 
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignKundesachbearbeiterBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// PART_KUNDESACHBEARBEITER kunde_i_id
		try {
			KundesachbearbeiterDto[] aKundesachbearbeiterQuellDtos = null;
			aKundesachbearbeiterQuellDtos = getKundesachbearbeiterFac()
					.kundesachbearbeiterFindByKundeIIdOhneExc(kundeQuellDto.getIId());
			for (int j = 0; j < aKundesachbearbeiterQuellDtos.length; j++) {
				if (aKundesachbearbeiterQuellDtos[j] != null) {
					// pruefen, ob es beim ziel schon diesen sachbearbeiter mit
					// datum gibt
					KundesachbearbeiterDto kundesachbearbeiterZielDto = getKundesachbearbeiterFac()
							.kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAbOhneExc(kundeZielDto.getIId(),
									aKundesachbearbeiterQuellDtos[j].getPersonalIId(),
									aKundesachbearbeiterQuellDtos[j].getTGueltigab());
					// wenn es beim zielkunden diesen sachbearbeiter gibt
					if (kundesachbearbeiterZielDto != null) {
						// quellsachbearbeiter loeschen, vorher auf
						// abhaengigkeiten pruefen
						getKundesachbearbeiterFac().removeKundesachbearbeiter(aKundesachbearbeiterQuellDtos[j]);
					} else {
						aKundesachbearbeiterQuellDtos[j].setKundeIId(kundeZielDto.getIId());
						getKundesachbearbeiterFac().updateKundesachbearbeiter(aKundesachbearbeiterQuellDtos[j],
								theClientDto);
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
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignAngebotBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// ANGB_ANGEBOT - kunde_i_id_angebotsadresse (mandant_cnr)
		try {
			AngebotDto[] aAngebotDtos = null;
			aAngebotDtos = getAngebotFac().angebotFindByKundeIIdAngebotsadresseMandantCNrOhneExc(kundeQuellDto.getIId(),
					mandantCNr, theClientDto);
			for (int j = 0; j < aAngebotDtos.length; j++) {
				if (aAngebotDtos[j] != null) {
					Angebot zeile = em.find(Angebot.class, aAngebotDtos[j].getIId());
					zeile.setKundeIIdAngebotsadresse(kundeZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		Query query = em.createNamedQuery("AngebotfindByKundeIIdLieferadresseMandantCNr");
		query.setParameter(1, kundeQuellDto.getIId());
		query.setParameter(2, mandantCNr);
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Angebot b = (Angebot) it.next();
			b.setKundeIIdLieferadresse(kundeZielDto.getIId());

			em.merge(b);
			em.flush();
		}

		query = em.createNamedQuery("AngebotfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, kundeQuellDto.getIId());
		query.setParameter(2, mandantCNr);
		cl = query.getResultList();

		it = cl.iterator();
		while (it.hasNext()) {
			Angebot b = (Angebot) it.next();
			b.setKundeIIdRechnungsadresse(kundeZielDto.getIId());

			em.merge(b);
			em.flush();
		}

	}

	/**
	 * H&auml;ngt bei den AngebotStkl (wenn vorhanden) die KundenIId vom Quellkunden
	 * auf den Zielkunden um
	 * 
	 * @param kundeZielDto  KundeDto
	 * @param kundeQuellDto KundeDto
	 * @param mandantCNr    String
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void reassignAngebotStklBeimZusammenfuehren(KundeDto kundeZielDto, KundeDto kundeQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellKundeDtos(kundeZielDto, kundeQuellDto);

		// AS_AGSTKL - kunde_i_id (mandant_cnr)
		try {
			AgstklDto[] aAgstklDtos = null;
			aAgstklDtos = getAngebotstklFac().agstklFindByKundeIIdMandantCNrOhneExc(kundeQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aAgstklDtos.length; j++) {
				if (aAgstklDtos[j] != null) {
					Agstkl zeile = em.find(Agstkl.class, aAgstklDtos[j].getIId());
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
	 * Zwei Kunden werden zu einem Kunden vereint; die Tabellen-Verkn&uuml;pfungen
	 * auf den zu l&ouml;schenden Kunden werden durch den &uuml;brigbleibenden
	 * ersetzt, zuletzt wird ein Kunde gel&ouml;scht, der dazugeh&ouml;rige Partner
	 * bleibt aber erhalten
	 * 
	 * @param kundeZielDto      KundeDto
	 * @param kundeQuellDtoIId  int
	 * @param kundesokoKundeIId int - von welchem Kunden die Sokos &uuml;bernommen
	 *                          werden
	 * @param kundePartnerIId   Integer Wird der Kunde mit einem anderen Partner
	 *                          verkn&uuml;pft, so steht hier die zu
	 *                          verkn&uuml;pfende PartnerIId
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void zusammenfuehrenKunde(KundeDto kundeZielDto, int kundeQuellDtoIId, Integer kundePartnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

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

		if (kundeQuelle.getKontoIIdDebitorenkonto() != null && kundeZiel.getKontoIIdDebitorenkonto() != null
				&& !kundeQuelle.getKontoIIdDebitorenkonto().equals(kundeZiel.getKontoIIdDebitorenkonto())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN,
					new Exception("FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN"));
		}

		if (kundeQuelle.getKontoIIdDebitorenkonto() != null && kundeZiel.getKontoIIdDebitorenkonto() == null) {

			// Quelle besetzt, Ziel jedoch nicht -> Ziel bekommt Quelle
			kundeZielDto.setIidDebitorenkonto(kundeQuelle.getKontoIIdDebitorenkonto());

			KontoDtoSmall k = getFinanzFac().kontoFindByPrimaryKeySmall(kundeQuelle.getKontoIIdDebitorenkonto());

			Integer iD = null;
			if (k != null && k.getCNr() != null) {
				iD = new Integer(Integer.parseInt(k.getCNr()));
			}
			kundeZielDto.setIDebitorenkontoAsIntegerNotiId(iD);

		}

		if (kundeQuelle.getMwstsatzIId() != null && kundeZiel.getMwstsatzIId() != null
				&& !kundeQuelle.getMwstsatzIId().equals(kundeZiel.getMwstsatzIId())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		if ((kundeQuelle.getMwstsatzIId() != null && kundeZiel.getMwstsatzIId() == null)
				|| (kundeZiel.getMwstsatzIId() != null && kundeQuelle.getMwstsatzIId() == null)) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		myLogger.info("Zielkunde: " + kundeZielDto.toString());
		myLogger.info("Quellkunde: " + kundeQuellDto.toString());

		myLogger.info("kundePartnerIId: " + kundePartnerIId);

		// neue Ziel-Dto-Daten in die DB zurueckschreiben
		if ((kundeZielDto.getPartnerDto() == null && kundeZielDto.getPartnerIId() != null)
				|| (kundeZielDto.getPartnerDto() != null && kundeZielDto.getPartnerIId() != null
						&& kundeZielDto.getPartnerDto().getIId() == null)) {
			kundeZielDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(kundeZielDto.getPartnerIId(), theClientDto));
		}

		kundeZielDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_UPDATE);

		if (kundeZiel.getKontoIIdDebitorenkonto() != null) {
			kundeZielDto.setIDebitorenkontoAsIntegerNotiId(
					new Integer(getFinanzFac().kontoFindByPrimaryKey(kundeZiel.getKontoIIdDebitorenkonto()).getCNr()));
		}

		updateKunde(kundeZielDto, theClientDto);

		try {

			// mandantenunabhaengig
			getDokumenteFac().vertauscheBelegartIdBeiBelegartdokumenten(LocaleFac.BELEGART_KUNDE,
					kundeQuellDto.getIId(), kundeZielDto.getIId(), theClientDto);
			getJCRDocFac().fuehreDokumenteZusammen(kundeZielDto, kundeQuellDto);
			reassignKundesachbearbeiterBeimZusammenfuehren(kundeZielDto, kundeQuellDto, theClientDto);
			reassignKundesokoBeimZusammenfuehren(kundeZielDto, kundeQuellDto, theClientDto);

			getPartnerFac().reassignPartnerkommentarBeimZusammenfuehren(kundeZielDto.getPartnerIId(),
					kundeQuellDto.getPartnerIId(), true, theClientDto);

			// mandantenabhaengig
			aMandantDtos = getMandantFac().mandantFindAll(theClientDto);
			int i = 0;
			while (i < aMandantDtos.length) {

				reassignAngebotBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignAngebotStklBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignAuftragBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignEinkaufsangebotZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignInseratBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(), theClientDto);
				reassignRechnungBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignLieferscheinBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignReklamationBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignBestellpositionBeimZusammenfuehren(kundeZielDto, kundeQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);

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
					kundeZielDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(kundePartnerIId, theClientDto));
					updateKunde(kundeZielDto, theClientDto);
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

	}

	public boolean hatKundeVersandweg(Integer kundeIId, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto);
		List<VersandwegPartner> versandwegPartners = VersandwegPartnerQuery.listByPartnerIId(em,
				kundeDto.getPartnerIId(), theClientDto.getMandant());
		// TODO: Ueberpruefen, ob die Versandwegberechtigung mit den
		// Versandwegen uebereinstimmt
		return versandwegPartners.size() > 0;
	}

	public KundeDto kundeFindByKundennummer(Integer kundenummer, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = kundeFindByKundennummerOhneExc(kundenummer, theClientDto);
		if (kundeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, kundenummer.toString());
		}

		return kundeDto;
	}

	public KundeDto kundeFindByKundennummerOhneExc(Integer kundenummer, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(kundenummer, "kundenummer");

		Kunde kunde = KundeQuery.findByKundenummer(em, kundenummer);
		return kunde == null ? null : KundeDtoAssembler.createDto(kunde);
	}

	@Override
	public VendidataPartnerExportResult exportiere4VendingKunden(boolean checkOnly, TheClientDto theClientDto)
			throws RemoteException {

		IVendidataCustomersExportBeanServices customerBeanServices = new VendidataCustomerExportBeanServices(
				getKundeFac(), getSystemFac(), theClientDto);
		VendidataCustomersExportTransformer exporter = new VendidataCustomersExportTransformer(customerBeanServices);

		if (checkOnly) {
			VendidataXmlCustomerTransformResult exportXmlResult = exporter.checkExportCustomers();
			return new VendidataPartnerExportResult(exportXmlResult.getExportErrors(),
					exportXmlResult.getExportStats());
		}

		try {
			VendidataXmlCustomerTransformResult exportXmlResult = exporter.exportCustomers();
			VendidataCustomersMarshaller marshaller = new VendidataCustomersMarshaller();
			String xmlString = marshaller.marshal(exportXmlResult.getXmlCustomers());
			return new VendidataPartnerExportResult(xmlString, exportXmlResult.getExportErrors(),
					exportXmlResult.getExportStats());
		} catch (JAXBException e) {
			throw new EJBExceptionLP(e);
		} catch (SAXException e) {
			throw new EJBExceptionLP(e);
		}
	}

	@Override
	public boolean hatKundeVersandweg(Integer kundeIId, String belegartCnr, TheClientDto theClientDto)
			throws RemoteException {
		KundeDto kundeDto = kundeFindByPrimaryKey(kundeIId, theClientDto);

		// TODO: Das ist suboptimal, wir braeuchten eine LP_VERSANDWEGTYP
		// Tabelle,
		// oder eine Erweiterung der LP_VERSANDWEG um einen "Typ"
		String[] versandWegCnrs = getVersandwegFrom(belegartCnr);
		for (String versandwegCnr : versandWegCnrs) {
			VersandwegPartner versandwegPartner = VersandwegPartnerQuery.findByPartnerIIdVersandwegCnr(em,
					kundeDto.getPartnerIId(), versandwegCnr, theClientDto.getMandant());
			if (versandwegPartner != null) {
				return true;
			}
		}

		return false;
	}

	private String[] getVersandwegFrom(String belegartCnr) {
		if (LocaleFac.BELEGART_AUFTRAG.equals(belegartCnr)) {
			return new String[] { SystemFac.VersandwegType.CleverCureVerkauf,
					SystemFac.VersandwegType.EdiOrderResponse };
		}
		if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(belegartCnr)) {
			return new String[] { SystemFac.VersandwegType.CleverCureVerkauf, SystemFac.VersandwegType.Edi4AllDesadv,
					SystemFac.VersandwegType.Linienabruf, SystemFac.VersandwegType.EdiDesadv };
		}
		if (LocaleFac.BELEGART_RECHNUNG.equals(belegartCnr)) {
			return new String[] { SystemFac.VersandwegType.Edi4AllInvoice };
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERSANDWEG_FUER_BELEGART_UNBEKANNT, belegartCnr);
	}

	@Override
	public List<KundeDto> kundeFindByFremdsystemnummerMandantCnrOhneExc(String fremdsystemnummer, String mandantCnr)
			throws RemoteException {
		Validator.notNull(fremdsystemnummer, "fremdsystemnummer");
		Validator.notNull(mandantCnr, "mandantCnr");

		List<Kunde> kunden = KundeQuery.listByFremdsystemnummer(em, fremdsystemnummer, mandantCnr);
		return kunden == null ? null : Arrays.asList(KundeDtoAssembler.createDtos(kunden));
	}

	@Override
	public Integer generiere4VendingCustomerId(Integer kundeIId, TheClientDto theClientDto) throws RemoteException {
		Validator.pkFieldNotNull(kundeIId, "kundeIId");
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer highestCustomerId = pkGen.getLastPrimaryKey(PKConst.PK_4VENDINGCUSTOMERID);
		if (new Integer(0).equals(highestCustomerId)) {
			highestCustomerId = KundeQuery.resultMaxFremdsystemnr(em);
			pkGen.createSequenceIfNotExists(PKConst.PK_4VENDINGCUSTOMERID, highestCustomerId);
		}

		Integer nextPk = pkGen.getNextPrimaryKey(PKConst.PK_4VENDINGCUSTOMERID);
		List<Kunde> kunden = KundeQuery.listByFremdsystemnummer(em, nextPk.toString(), theClientDto.getMandant());
		if (!kunden.isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"Generierte 4Vending-Customer-Id (Fremdsystemnummer) bereits vorhanden!");
		}

		Kunde kunde = em.find(Kunde.class, kundeIId);
		kunde.setCFremdsystemnr(nextPk.toString());
		em.merge(kunde);
		em.flush();

		return nextPk;
	}

	@Override
	public KundeDto[] kundeFindByPartnerIId(Integer partnerId, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.pkFieldNotNull(partnerId, "partnerId");

		List<Kunde> kunden = KundeQuery.listByPartnerId(em, partnerId);
		return KundeDtoAssembler.createDtos(kunden);
	}

	@Override
	public List<KundeDto> kundeFindByKennung(KennungId kennungId, String value, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(kennungId, "kennungId");
		Validator.notEmpty(value, "value");

		List<Kunde> kunden = KundeQuery.listByKennung(em, kennungId, value);
		return KundeDtoAssembler.createListDtos(kunden);
	}

	@Override
	public List<KundeDto> kundeFindByKennungCnr(String kennungCnr, String value, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(kennungCnr, "kennungCnr");
		Validator.notEmpty(value, "value");

		HvOptional<Kennung> kennung = KennungQuery.findByCnr(em, kennungCnr);
		if (kennung.isPresent()) {
			return kundeFindByKennung(new KennungId(kennung.get().getIId()), value, theClientDto);
		}
		return new ArrayList<KundeDto>();
	}

	@Override
	public List<KundeDto> kundeFindByKennungType(KennungType kennung, String value, TheClientDto theClientDto) {
		Validator.notEmpty(value, "value");
		return kundeFindByKennungCnr(kennung.getText(), value, theClientDto);
	}

	@Override
	public Integer createKundeKennung(KundeKennungDto kundeKennungDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(kundeKennungDto, "kundeKennungDto");

		try {
			KennungId kennungId = new KennungId(kundeKennungDto.getKennungIId());
			KennungDto kennungDto = getSystemFac().kennungFindByPrimaryKey(kennungId);
			KennungType kennungType = KennungType.fromString(kennungDto.getCnr());

			if (kennungType.isUnique()) {
				List<KundeKennung> doppelt = KundeKennungQuery.findByKundeIdKennungId(em,
						new KundeId(kundeKennungDto.getKundeIId()), kennungId);
				if (doppelt.size() > 0) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PART_KUNDEKENNUNG_UK"));
				}
			} else {
				// PJ22181 mehrfache gleiche Kennungen erlaubt, Wert unterschiedlich
				KundeKennung doppelt = KundeKennungQuery.findByKundeIdKennungIdValue(em,
						new KundeId(kundeKennungDto.getKundeIId()), new KennungId(kundeKennungDto.getKennungIId()),
						kundeKennungDto.getWert());
				if (doppelt != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PART_KUNDEKENNUNG_UK"));
				}
			}

//			List<KundeKennung> doppelt = KundeKennungQuery.findByKundeIdKennungId(em,
//					new KundeId(kundeKennungDto.getKundeIId()), new KennungId(kundeKennungDto.getKennungIId()));
//			if (doppelt.size() > 0) {
//				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_KUNDEKENNUNG_UK"));
//			}
		} catch (NoResultException ex) {

		}

		try {
			Integer pk = new PKGeneratorObj().getNextPrimaryKey(PKConst.PK_KUNDEKENNUNG);
			KundeKennung entity = new KundeKennung(kundeKennungDto.getKundeIId(), kundeKennungDto.getKennungIId(),
					kundeKennungDto.getWert());
			entity.setIId(pk);
			em.persist(entity);
			em.flush();

			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}
	
	@Override
	public void checkAndCreateAutoDebitorennummerZuKunden(KundeId kundeId, TheClientDto theClientDto) {
		Validator.notNull(kundeId, "kundeId");
		
		Boolean isAutoDebitor = getParameterFac().getAutomatischeDebitorennummer(theClientDto.getMandant());
		if (Boolean.FALSE.equals(isAutoDebitor))
			return;
		
		KundeDto kundeDto = kundeFindByPrimaryKey(kundeId.id(), theClientDto);
		if (kundeDto.getIidDebitorenkonto() != null)
			return;
		
		KontoDto kontoDto = createDebitorenkontoZuKundenAutomatisch(
				kundeId.id(), false, null, theClientDto);
		kundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(kontoDto.getCNr()));
		updateKunde(kundeDto, theClientDto);
	}
}
