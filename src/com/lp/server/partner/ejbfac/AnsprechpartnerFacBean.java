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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.inserat.ejb.Inserat;
import com.lp.server.inserat.ejb.Inseratrechnung;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.ejb.Ansprechpartner;
import com.lp.server.partner.ejb.AnsprechpartnerISort;
import com.lp.server.partner.ejb.AnsprechpartnerQuery;
import com.lp.server.partner.ejb.Ansprechpartnerfunktion;
import com.lp.server.partner.ejb.Ansprechpartnerfunktionspr;
import com.lp.server.partner.ejb.AnsprechpartnerfunktionsprPK;
import com.lp.server.partner.ejb.Kontakt;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.Partnerartspr;
import com.lp.server.partner.ejb.PartnerartsprPK;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerDtoAssembler;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.AnsprechpartnerISortValues;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDtoAssembler;
import com.lp.server.partner.service.AnsprechpartnerfunktionsprDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionsprDtoAssembler;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.NewslettergrundDtoAssembler;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerDtoAssembler;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiselogDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.ejb.Projekt;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AnsprechpartnerFacBean extends Facade implements AnsprechpartnerFac {

	@PersistenceContext
	private EntityManager em;

	public static final int HPK_UPDATE = 1;
	public static final int HPK_CREATE = 2;
	public static final int HPK_DECIDES_INTERN = 3;
	public static final int HPK_READ = 4;
	public static final int HPK_DELETE = 5;

	// private void findPartnerartsprHome()
	// throws EJBExceptionLP {
	//
	// final String ENTITY_NAME = "java:comp/env/ejb/partnerartspr";
	//
	// if (partnerartsprHome == null) {
	// try {
	// ServiceLocator locator = ServiceLocator.getInstance();
	// partnerartsprHome = (PartnerartsprHome) locator.getEjbLocalHome(
	// ENTITY_NAME);
	// }
	// catch (ServiceLocatorException e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL, e);
	// }
	// }
	// }

	public void removeAnsprechpartner(AnsprechpartnerDto ansprechpartnerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (ansprechpartnerDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("ansprechpartnerDtoI == null"));
		}

		Query query = em
				.createQuery("DELETE Telefonnummer x WHERE x.ansprechpartnerIId = " + ansprechpartnerDtoI.getIId());
		query.executeUpdate();

		Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, ansprechpartnerDtoI.getIId());

		Ansprechpartner toRemove = em.find(Ansprechpartner.class, ansprechpartnerDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		AnsprechpartnerDto ansprechpartnerDto = AnsprechpartnerDtoAssembler.createDto(toRemove);
		HvDtoLogger<AnsprechpartnerDto> newslettergrundLogger = new HvDtoLogger<AnsprechpartnerDto>(em,
				ansprechpartnerDto.getPartnerIId(), theClientDto);
		newslettergrundLogger.logDelete(ansprechpartnerDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (Exception er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void pruefeAnsprechpartnerZugehoerigkeit(Integer partnerIId, Integer ansprechpartnerIId,
			TheClientDto theClientDto) {
		if (ansprechpartnerIId != null) {
			Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, ansprechpartnerIId);

			if (!ansprechpartner.getPartnerIId().equals(partnerIId)) {

				Partner partner = em.find(Partner.class, partnerIId);
				Partner partnerAnsprechpartner = em.find(Partner.class, ansprechpartner.getPartnerIIdAnsprechpartner());
				
				ArrayList alInfo = new ArrayList();
				
				alInfo.add(PartnerDtoAssembler.createDto(partnerAnsprechpartner).formatFixName1Name2());
				alInfo.add(PartnerDtoAssembler.createDto(partner).formatFixName1Name2());
				
				
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUORDNUNG_ANSPRECHPARTNER_ZU_PARTNER, alInfo,
						new Exception("FEHLER_ZUORDNUNG_ANSPRECHPARTNER_ZU_PARTNER"));

			}
		}

	}

	public Integer createAnsprechpartner(AnsprechpartnerDto ansprechpartnerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (ansprechpartnerDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("ansprechpartnerDtoI == null"));
		}

		if (ansprechpartnerDtoI.getIId() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("ansprechpartnerDtoI.getIId() != null"));
		}

		// PJ18553
		if (ansprechpartnerDtoI.getCKennwort() != null) {
			// Dann muss es eine E-Mail geben und die muss eindeutig sein
			if (ansprechpartnerDtoI.getCEmail() == null) {
				// Fehler
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT,
						new Exception("FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT"));

			} else {

				// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
				Query query = em.createNamedQuery("AnsprechpartnerfindByCEmail");
				query.setParameter(1, ansprechpartnerDtoI.getCEmail());
				Collection c = query.getResultList();
				if (c.size() > 0) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG,
							new Exception("FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG"));
				}

			}

		}

		try {
			// Generiere einen AnsprechpartnerPK.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer iIdAnsprechpartnerNew = pkGen.getNextPrimaryKey(PKConst.PK_ANSPRECHPARTNER);

			ansprechpartnerDtoI.setIId(iIdAnsprechpartnerNew);
			ansprechpartnerDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			if (ansprechpartnerDtoI.getBVersteckt() == null) {
				ansprechpartnerDtoI.setBVersteckt(Helper.boolean2Short(false));
			}

			if (ansprechpartnerDtoI.getBDurchwahl() == null) {
				ParametermandantDto pDruchwahl = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_PARTNER, ParameterFac.PARAMETER_DEFAULT_ANSPRECHPARTNER_DURCHWAHL);

				ansprechpartnerDtoI.setBDurchwahl(Helper.boolean2Short((Boolean) pDruchwahl.getCWertAsObject()));

			}

			if (ansprechpartnerDtoI.getISort() == null) {
				Integer iSort = getMaxISort(ansprechpartnerDtoI.getPartnerIId());
				ansprechpartnerDtoI.setISort(++iSort);
			}

			// Create.
			Ansprechpartner ansprechpartner = new Ansprechpartner(ansprechpartnerDtoI.getIId(),
					ansprechpartnerDtoI.getPartnerIId(), ansprechpartnerDtoI.getPartnerIIdAnsprechpartner(),
					ansprechpartnerDtoI.getAnsprechpartnerfunktionIId(), ansprechpartnerDtoI.getDGueltigab(),
					ansprechpartnerDtoI.getISort(), ansprechpartnerDtoI.getPersonalIIdAendern(),
					ansprechpartnerDtoI.getBVersteckt(), ansprechpartnerDtoI.getBDurchwahl());
			em.persist(ansprechpartner);
			em.flush();

			ansprechpartnerDtoI.setTAendern(ansprechpartner.getTAendern());

			setAnsprechpartnerFromAnsprechpartnerDto(ansprechpartner, ansprechpartnerDtoI);

			HvDtoLogger<AnsprechpartnerDto> ansprechpartnerLogger = new HvDtoLogger<AnsprechpartnerDto>(em,
					theClientDto);
			ansprechpartnerLogger.logInsert(ansprechpartnerDtoI);

			getPartnerFac().telefonnummerFuerTapiSynchronisieren(ansprechpartnerDtoI.getPartnerIId(),
					ansprechpartnerDtoI.getIId(), theClientDto);

			return ansprechpartnerDtoI.getIId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));

		}
	}

	public void updateAnsprechpartner(AnsprechpartnerDto ansprechpartnerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (ansprechpartnerDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("ansprechpartnerDtoI == null"));
		}
		if (ansprechpartnerDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("ansprechpartnerDtoI.getPartnerDto() == null"));
		}

		// Jetzt der Ansprechpartner.
		Integer iId = ansprechpartnerDtoI.getIId();

		// ueberpruefen, ob beim Ansprechpartner nur der Partner upgedated wird
		// wenn die PartnerIId im uebergebenen und db-datensatz die selbe ist,
		// teste wegen unique
		// try {
		Ansprechpartner ansprechpartnerAlt = em.find(Ansprechpartner.class, ansprechpartnerDtoI.getIId());
		if (ansprechpartnerAlt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		if (ansprechpartnerDtoI.getCKennwort() != null) {
			// Dann muss es eine E-Mail geben und die muss eindeutig sein
			if (ansprechpartnerDtoI.getCEmail() == null) {
				// Fehler
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT,
						new Exception("FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT"));

			} else {
				Query query = em.createNamedQuery("AnsprechpartnerfindByCEmail");
				query.setParameter(1, ansprechpartnerDtoI.getCEmail());
				Collection c = query.getResultList();

				Iterator it = c.iterator();

				while (it.hasNext()) {
					Ansprechpartner a = (Ansprechpartner) it.next();
					if (!a.getIId().equals(ansprechpartnerAlt.getIId())) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG,
								new Exception("FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG"));
					}
				}
			}
		}

		if (ansprechpartnerAlt.getPartnerIId().intValue() == ansprechpartnerDtoI.getPartnerIId().intValue()) {
			// der unique-test
			try {
				Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerFunktionGueltigAb");
				query.setParameter(1, ansprechpartnerDtoI.getPartnerIId());
				query.setParameter(2, ansprechpartnerDtoI.getPartnerIIdAnsprechpartner());
				query.setParameter(3, ansprechpartnerDtoI.getAnsprechpartnerfunktionIId());
				query.setParameter(4, ansprechpartnerDtoI.getDGueltigab());
				// @todo getSingleResult oder getResultList ?
				Ansprechpartner ansprechpartner = (Ansprechpartner) query.getSingleResult();
				if (ansprechpartner.getIId().intValue() != ansprechpartnerDtoI.getIId().intValue()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
				}

			} catch (NoResultException ex) {
				// Ein unique key Feld hat sich geaendert.
			}

		}

		try {

			Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, iId);

			AnsprechpartnerDto dtoVorher = assembleAnsprechpartnerDto(ansprechpartner);

			if (ansprechpartner == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			ansprechpartnerDtoI.setTAendern(new Timestamp(System.currentTimeMillis()));
			setAnsprechpartnerFromAnsprechpartnerDto(ansprechpartner, ansprechpartnerDtoI);

			HvDtoLogger<AnsprechpartnerDto> artikelLogger = new HvDtoLogger<AnsprechpartnerDto>(em,
					ansprechpartner.getPartnerIId(), theClientDto);
			artikelLogger.log(dtoVorher, ansprechpartnerDtoI);

			getPartnerFac().telefonnummerFuerTapiSynchronisieren(ansprechpartnerDtoI.getPartnerIId(),
					ansprechpartnerDtoI.getIId(), theClientDto);

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		}

	}

	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		try {
			// 1 suche den ansprechpartner.
			Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, iIdI);
			if (ansprechpartner == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			AnsprechpartnerDto ansprechpartnerDto = assembleAnsprechpartnerDto(ansprechpartner);

			// 2 suche den zugehoerigen partner.
			PartnerDto partnerDto = getPartnerFac()
					.partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIIdAnsprechpartner(), theClientDto);

			ansprechpartnerDto.setPartnerDto(partnerDto);

			return ansprechpartnerDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKeyOhneExc(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		try {
			return ansprechpartnerFindByPrimaryKey(iIdI, theClientDto);
		} catch (Throwable th) {
			return null;
		}
	}

	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByCnr(String sCnrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
		try {
			Query query = em.createNamedQuery("AnsprechpartnerfunktionfindByCnr");
			query.setParameter(1, sCnrI);
			ansprechpartnerfunktionDto = assembleAnsprechpartnerfunktionDto(
					(Ansprechpartnerfunktion) query.getSingleResult());
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		return ansprechpartnerfunktionDto;
	}

	private void setAnsprechpartnerFromAnsprechpartnerDto(Ansprechpartner ansprechpartner,
			AnsprechpartnerDto ansprechpartnerDto) {

		ansprechpartner.setAnsprechpartnerfunktionIId(ansprechpartnerDto.getAnsprechpartnerfunktionIId());
		ansprechpartner.setPartnerIIdAnsprechpartner(ansprechpartnerDto.getPartnerIIdAnsprechpartner());
		ansprechpartner.setPartnerIId(ansprechpartnerDto.getPartnerIId());
		// ansprechpartner.setPartnerIIdAnsprechpartner(ansprechpartnerDto.
		// getPartnerIIdAnsprechpartner());
		ansprechpartner.setTGueltigab(ansprechpartnerDto.getDGueltigab());
		ansprechpartner.setBVersteckt(ansprechpartnerDto.getBVersteckt());
		ansprechpartner.setISort(ansprechpartnerDto.getISort());
		ansprechpartner.setXBemerkung(ansprechpartnerDto.getXBemerkung());

		ansprechpartner.setTAendern(ansprechpartnerDto.getTAendern());
		ansprechpartner.setPersonalIIdAendern(ansprechpartnerDto.getPersonalIIdAendern());

		ansprechpartner.setCDirektfax(ansprechpartnerDto.getCDirektfax());
		ansprechpartner.setCEmail(ansprechpartnerDto.getCEmail());
		ansprechpartner.setCFax(ansprechpartnerDto.getCFax());
		ansprechpartner.setCHandy(ansprechpartnerDto.getCHandy());
		ansprechpartner.setCTelefon(ansprechpartnerDto.getCTelefon());
		ansprechpartner.setCFremdsystemnr(ansprechpartnerDto.getCFremdsystemnr());
		ansprechpartner.setNewslettergrundIId(ansprechpartnerDto.getNewslettergrundIId());
		ansprechpartner.setCAbteilung(ansprechpartnerDto.getCAbteilung());
		ansprechpartner.setCKennwort(ansprechpartnerDto.getCKennwort());
		ansprechpartner.setBDurchwahl(ansprechpartnerDto.getBDurchwahl());

		em.merge(ansprechpartner);
		em.flush();
	}

	// private void findPartnerFacHome()
	// throws EJBExceptionLP {
	// final String ENTITY_NAME = "PartnerFac";
	//
	// if (this.partnerFacHome == null) {
	// try {
	// ServiceLocator locator = ServiceLocator.getInstance();
	// partnerFacHome = (PartnerFacHome) locator.getEjbHome(
	// ENTITY_NAME, PartnerFacHome.class);
	//
	// }
	// catch (ServiceLocatorException e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL, e);
	// }
	// }
	// }

	private AnsprechpartnerDto assembleAnsprechpartnerDto(Ansprechpartner ansprechpartner) {
		return AnsprechpartnerDtoAssembler.createDto(ansprechpartner);
	}

	// *** Ansprechpartnerfunktion
	// **************************************************
	/**
	 * Lesen aller in der DB vorhandenen Ansprechpartnerfunktionen.
	 * 
	 * @param loI          Sprache
	 * @param theClientDto String
	 * @return Map
	 */
	public Map getAllAnsprechpartnerfunktion(String loI, TheClientDto theClientDto) {

		Map<String, String> content = null;

		// try {
		Query query = em.createNamedQuery("AnsprechpartnerfunktionfindAll");
		Collection<?> allArten = query.getResultList();
		// if(allArten.isEmpty()){
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,null);
		// }
		content = new TreeMap<String, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Ansprechpartnerfunktion ansprechpartnerfunktionTemp = (Ansprechpartnerfunktion) iter.next();
			String key = ansprechpartnerfunktionTemp.getCNr();
			String value = null;
			// try {
			Partnerartspr partnerartspr = em.find(Partnerartspr.class,
					new PartnerartsprPK(loI, ansprechpartnerfunktionTemp.getCNr()));
			if (partnerartspr == null) {
				value = ansprechpartnerfunktionTemp.getCNr();
			} else {
				value = partnerartspr.getCBez();
			}

			// }
			// catch (FinderException ex1) {
			// value = ansprechpartnerfunktionTemp.getCNr();
			// }
			content.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return content;
	}

	public Integer createAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (ansprechpartnerfunktionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerfunktionDtoI == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_ANSPRECHPARTNERFUNKTION);
		ansprechpartnerfunktionDtoI.setIId(iId);

		Ansprechpartnerfunktion ansprechpartnerfunktion = null;
		try {
			ansprechpartnerfunktion = new Ansprechpartnerfunktion(ansprechpartnerfunktionDtoI.getIId(),
					ansprechpartnerfunktionDtoI.getCNr());
			em.persist(ansprechpartnerfunktion);
			em.flush();

			if (ansprechpartnerfunktionDtoI.getAnsprechpartnerfunktionsprDto() != null) {
				ansprechpartnerfunktionDtoI.getAnsprechpartnerfunktionsprDto()
						.setAnsprechpartnerfunktionIId(ansprechpartnerfunktionDtoI.getIId());
				createAnsprechpartnerfunktionspr(ansprechpartnerfunktionDtoI.getAnsprechpartnerfunktionsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return ansprechpartnerfunktion.getIId();
	}

	public void removeAnsprechpartnerfunktion(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Ansprechpartnerfunktion toRemove = em.find(Ansprechpartnerfunktion.class, iIdI);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (Exception er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removeAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (ansprechpartnerfunktionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("ansprechpartnerfunktionDtoI == null"));
		}

		try {
			Query query = em.createNamedQuery("AnsprechpartnerfunktionsprfindByAnsprechpartnerfunktionIId");
			query.setParameter(1, ansprechpartnerfunktionDtoI.getIId());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Ansprechpartnerfunktionspr item = (Ansprechpartnerfunktionspr) iter.next();
				em.remove(item);
			}
			Ansprechpartnerfunktion ansprechpartnerfunktion = em.find(Ansprechpartnerfunktion.class,
					ansprechpartnerfunktionDtoI.getIId());
			if (ansprechpartnerfunktion == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			em.remove(ansprechpartnerfunktion);
			em.flush();
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (ansprechpartnerfunktionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("ansprechpartnerfunktionDto == null"));
		}

		Integer iId = ansprechpartnerfunktionDto.getIId();
		// try {
		Ansprechpartnerfunktion ansprechpartnerfunktion = em.find(Ansprechpartnerfunktion.class, iId);
		if (ansprechpartnerfunktion == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAnsprechpartnerfunktionFromAnsprechpartnerfunktionDto(ansprechpartnerfunktion, ansprechpartnerfunktionDto);

		if (ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto() != null) {
			ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
			// -- upd oder create
			if (ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto().getAnsprechpartnerfunktionIId() == null) {
				// create
				// Key(teil) setzen.
				ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto()
						.setAnsprechpartnerfunktionIId(ansprechpartnerfunktionDto.getIId());
				createAnsprechpartnerfunktionspr(ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto());
			} else {
				// upd
				updateAnsprechpartnerfunktionspr(ansprechpartnerfunktionDto.getAnsprechpartnerfunktionsprDto());
			}
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;

		// try {
		Ansprechpartnerfunktion ansprechpartnerfunktion = em.find(Ansprechpartnerfunktion.class, iIdI);
		if (ansprechpartnerfunktion == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		ansprechpartnerfunktionDto = assembleAnsprechpartnerfunktionDto(ansprechpartnerfunktion);

		try {
			Ansprechpartnerfunktionspr ansprechpartnerfunktionspr = em.find(Ansprechpartnerfunktionspr.class,
					new AnsprechpartnerfunktionsprPK(theClientDto.getLocUiAsString(), iIdI));
			ansprechpartnerfunktionDto.setAnsprechpartnerfunktionsprDto(
					assembleAnsprechpartnerfunktionsprDto(ansprechpartnerfunktionspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return ansprechpartnerfunktionDto;
	}

	public AnsprechpartnerfunktionDto[] ansprechpartnerfunktionFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("AnsprechpartnerfunktionfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleAnsprechpartnerfunktionDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

	}

	private void setAnsprechpartnerfunktionFromAnsprechpartnerfunktionDto(
			Ansprechpartnerfunktion ansprechpartnerfunktion, AnsprechpartnerfunktionDto ansprechpartnerfunktionDto) {
		ansprechpartnerfunktion.setCNr(ansprechpartnerfunktionDto.getCNr());
		ansprechpartnerfunktion.setCReportname(ansprechpartnerfunktionDto.getCReportname());
		em.merge(ansprechpartnerfunktion);
		em.flush();
	}

	private AnsprechpartnerfunktionDto assembleAnsprechpartnerfunktionDto(
			Ansprechpartnerfunktion ansprechpartnerfunktion) {

		return AnsprechpartnerfunktionDtoAssembler.createDto(ansprechpartnerfunktion);
	}

	private AnsprechpartnerfunktionDto[] assembleAnsprechpartnerfunktionDtos(Collection<?> ansprechpartnerfunktions) {
		List<AnsprechpartnerfunktionDto> list = new ArrayList<AnsprechpartnerfunktionDto>();
		if (ansprechpartnerfunktions != null) {
			Iterator<?> iterator = ansprechpartnerfunktions.iterator();
			while (iterator.hasNext()) {
				Ansprechpartnerfunktion ansprechpartnerfunktion = (Ansprechpartnerfunktion) iterator.next();
				list.add(assembleAnsprechpartnerfunktionDto(ansprechpartnerfunktion));
			}
		}
		AnsprechpartnerfunktionDto[] returnArray = new AnsprechpartnerfunktionDto[list.size()];
		return (AnsprechpartnerfunktionDto[]) list.toArray(returnArray);
	}

	private AnsprechpartnerDto[] assembleAnsprechpartnerDtos(Collection<?> ansprechpartners) {

		List<AnsprechpartnerDto> list = new ArrayList<AnsprechpartnerDto>();
		if (ansprechpartners != null) {
			Iterator<?> iterator = ansprechpartners.iterator();
			while (iterator.hasNext()) {
				Ansprechpartner ansprechpartner = (Ansprechpartner) iterator.next();
				list.add(assembleAnsprechpartnerDto(ansprechpartner));
			}
		}
		AnsprechpartnerDto[] returnArray = new AnsprechpartnerDto[list.size()];
		return (AnsprechpartnerDto[]) list.toArray(returnArray);
	}

	public ArrayList getAllAnsprechpartner(Integer iIdPartnerI, TheClientDto theClientDto) {

		ArrayList<AnsprechpartnerDto> a = new ArrayList<AnsprechpartnerDto>();
		Collection<?> ansprechCol = null;
		try {
			Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIId");
			query.setParameter(1, iIdPartnerI);
			// @todo getSingleResult oder getResultList ?
			ansprechCol = query.getResultList();
			for (Iterator<?> iter = ansprechCol.iterator(); iter.hasNext();) {
				Ansprechpartner ansprechpartner = (Ansprechpartner) iter.next();
				AnsprechpartnerDto ansprechpartnerDto = assembleAnsprechpartnerDto(ansprechpartner);
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(ansprechpartner.getPartnerIIdAnsprechpartner(), theClientDto);
				ansprechpartnerDto.setPartnerDto(partnerDto);
				a.add(ansprechpartnerDto);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
		return a;
	}

	public void createAnsprechpartnerfunktionspr(AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDtoI)
			throws EJBExceptionLP {
		if (ansprechpartnerfunktionsprDtoI == null) {
			return;
		}
		try {
			Ansprechpartnerfunktionspr ansprechpartnerfunktionspr = new Ansprechpartnerfunktionspr(
					ansprechpartnerfunktionsprDtoI.getAnsprechpartnerfunktionIId(),
					ansprechpartnerfunktionsprDtoI.getLocaleCNr());
			em.persist(ansprechpartnerfunktionspr);
			em.flush();
			setAnsprechpartnerfunktionsprFromAnsprechpartnerfunktionsprDto(ansprechpartnerfunktionspr,
					ansprechpartnerfunktionsprDtoI);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeAnsprechpartnerfunktionspr(String localeCNr, Integer ansprechpartnerIId) throws EJBExceptionLP {
		AnsprechpartnerfunktionsprPK ansprechpartnerfunktionsprPK = new AnsprechpartnerfunktionsprPK();
		ansprechpartnerfunktionsprPK.setLocaleCNr(localeCNr);
		ansprechpartnerfunktionsprPK.setAnsprechpartnerfunktionIId(ansprechpartnerIId);
		// try {
		Ansprechpartnerfunktionspr toRemove = em.find(Ansprechpartnerfunktionspr.class, ansprechpartnerfunktionsprPK);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removeAnsprechpartnerfunktionspr(AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto)
			throws EJBExceptionLP {
		if (ansprechpartnerfunktionsprDto != null) {
			String localeCNr = ansprechpartnerfunktionsprDto.getLocaleCNr();
			Integer ansprechpartnerIId = ansprechpartnerfunktionsprDto.getAnsprechpartnerfunktionIId();
			removeAnsprechpartnerfunktionspr(localeCNr, ansprechpartnerIId);
		}
	}

	public void updateAnsprechpartnerfunktionspr(AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto)
			throws EJBExceptionLP {
		if (ansprechpartnerfunktionsprDto != null) {
			AnsprechpartnerfunktionsprPK ansprechpartnerfunktionsprPK = new AnsprechpartnerfunktionsprPK();
			ansprechpartnerfunktionsprPK.setLocaleCNr(ansprechpartnerfunktionsprDto.getLocaleCNr());
			ansprechpartnerfunktionsprPK
					.setAnsprechpartnerfunktionIId(ansprechpartnerfunktionsprDto.getAnsprechpartnerfunktionIId());
			// try {
			Ansprechpartnerfunktionspr ansprechpartnerfunktionspr = em.find(Ansprechpartnerfunktionspr.class,
					ansprechpartnerfunktionsprPK);
			if (ansprechpartnerfunktionspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAnsprechpartnerfunktionsprFromAnsprechpartnerfunktionsprDto(ansprechpartnerfunktionspr,
					ansprechpartnerfunktionsprDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprFindByPrimaryKey(String localeCNr,
			Integer ansprechpartnerIId) throws EJBExceptionLP {
		// try {
		AnsprechpartnerfunktionsprPK ansprechpartnerfunktionsprPK = new AnsprechpartnerfunktionsprPK();
		ansprechpartnerfunktionsprPK.setLocaleCNr(localeCNr);
		ansprechpartnerfunktionsprPK.setAnsprechpartnerfunktionIId(ansprechpartnerIId);
		Ansprechpartnerfunktionspr ansprechpartnerfunktionspr = em.find(Ansprechpartnerfunktionspr.class,
				ansprechpartnerfunktionsprPK);
		if (ansprechpartnerfunktionspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAnsprechpartnerfunktionsprDto(ansprechpartnerfunktionspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void setAnsprechpartnerfunktionsprFromAnsprechpartnerfunktionsprDto(
			Ansprechpartnerfunktionspr ansprechpartnerfunktionspr,
			AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto) {
		ansprechpartnerfunktionspr.setCBez(ansprechpartnerfunktionsprDto.getCBez());
		em.merge(ansprechpartnerfunktionspr);
		em.flush();
	}

	private AnsprechpartnerfunktionsprDto assembleAnsprechpartnerfunktionsprDto(
			Ansprechpartnerfunktionspr ansprechpartnerfunktionspr) {
		return AnsprechpartnerfunktionsprDtoAssembler.createDto(ansprechpartnerfunktionspr);
	}

	public void vertauscheAnsprechpartner(Integer iIdansprechpartner1, Integer iIdansprechpartner2)
			throws EJBExceptionLP {

		this.vertauscheAnsprechpartner(null, null, iIdansprechpartner1, iIdansprechpartner2);

	}

	public void vertauscheAnsprechpartner(Integer iSort1, Integer iSort2, Integer iIdansprechpartner1,
			Integer iIdansprechpartner2) throws EJBExceptionLP {

		Ansprechpartner ansprechpartner1 = em.find(Ansprechpartner.class, iIdansprechpartner1);
		if (ansprechpartner1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Ansprechpartner ansprechpartner2 = em.find(Ansprechpartner.class, iIdansprechpartner2);
		if (ansprechpartner2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (iSort1 == null) {
			iSort1 = ansprechpartner1.getISort();
			iSort2 = ansprechpartner2.getISort();
		}

		// iSort der zweiten auf ungueltig setzen, damit UK constraint nicht
		// verletzt wird
		ansprechpartner2.setISort(new Integer(-1));
		ansprechpartner1.setISort(iSort2);
		ansprechpartner2.setISort(iSort1);

		renumberISortAnsprechpartner(ansprechpartner1.getPartnerIId());

	}

	public void renumberISortAnsprechpartner(Integer partnerId) {
		AnsprechpartnerISort helper = new AnsprechpartnerISort(em, partnerId);
		helper.renumber(AnsprechpartnerISortValues.ANSPRECHPARTNER_ISORT_STEP);
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByAnsprechpartnerIId(Integer idAnsprechpartnerI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		if (idAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("idAnsprechpartnerI == null"));
		}

		AnsprechpartnerDto[] ansprechpartnerDtos = null;
		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIIdAnsprechpartner");
		query.setParameter(1, idAnsprechpartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		ansprechpartnerDtos = assembleAnsprechpartnerDtos(cl);

		for (int i = 0; i < ansprechpartnerDtos.length; i++) {
			// 2 suche den zugehoerigen partner.
			PartnerDto partnerDto = getPartnerFac()
					.partnerFindByPrimaryKey(ansprechpartnerDtos[i].getPartnerIIdAnsprechpartner(), theClientDto);

			ansprechpartnerDtos[i].setPartnerDto(partnerDto);
		}
		return ansprechpartnerDtos;
	}

	public Integer getMaxISort(Integer iIdPartnerI) {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("AnsprechpartnerejbSelectMaxISort");
			query.setParameter(1, iIdPartnerI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, t);
		}
		return iiMaxISortO;
	}

	public AnsprechpartnerDto ansprechpartnerFindErstenEinesPartnersOhneExc(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// MB 17.05.06 Hibernate-Implementierung, da ich derzeit mit mehreren
		// ansprechpartnern mit dem gleichen i_sort rechnen muss. (fehlender
		// UK-Constraint).
		AnsprechpartnerDto ansprechpartnerDto = null;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRAnsprechpartner.class);
			// Filter anch Partner.
			c.add(Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNER_I_ID, partnerIId));
			c.add(Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_VERSTECKT, Helper.boolean2Short(false)));
			// Sortierung nach I_SORT.
			c.addOrder(Order.asc(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_I_SORT));
			// Query ausfuehren.
			List<?> list = c.list();
			if (!list.isEmpty()) {
				// wenn die Liste nicht leer ist. dann den ersten holen.
				FLRAnsprechpartner flrAnsp = (FLRAnsprechpartner) list.get(0);
				ansprechpartnerDto = ansprechpartnerFindByPrimaryKey(flrAnsp.getI_id(), theClientDto);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return ansprechpartnerDto;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdAndPartnerIIdAnsprechpartner(Integer iIdPartnerI,
			Integer iIdPartnerAnsprechpartnerI, TheClientDto theClientDto) {

		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}
		if (iIdPartnerAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdPartnerAnsprechpartnerI == null"));
		}

		AnsprechpartnerDto[] ansprechpartnerDtos = null;

		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIIdAndPartnerIIdAnsprechpartner");
		query.setParameter(1, iIdPartnerI);
		query.setParameter(2, iIdPartnerAnsprechpartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		ansprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		for (int i = 0; i < ansprechpartnerDtos.length; i++) {
			// suche den zugehoerigen partner.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(ansprechpartnerDtos[i].getPartnerIId(),
					theClientDto);
			ansprechpartnerDtos[i].setPartnerDto(partnerDto);
		}
		// }
		// catch (FinderException e) {
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);

		return ansprechpartnerDtos;
	}

	/**
	 * Gibt AnsprechpartnerDto[] zur&uuml;ck, wo entweder die partnerIId oder die
	 * partnerIIdAnsprechpatner der &uuml;bergebenen partnerIId entspricht
	 * 
	 * @param iIdPartnerI  Integer
	 * @param theClientDto String
	 * @return AnsprechpartnerDto[]
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOrPartnerIIdAnsprechpartner(Integer iIdPartnerI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIIdOrPartnerIIdAnsprechpartner");
		query.setParameter(1, iIdPartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		aAnsprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		for (int i = 0; i < aAnsprechpartnerDtos.length; i++) {
			// suche den zugehoerigen partner.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(aAnsprechpartnerDtos[i].getPartnerIId(),
					theClientDto);
			aAnsprechpartnerDtos[i].setPartnerDto(partnerDto);
		}

		return aAnsprechpartnerDtos;
	}

	/**
	 * Gibt AnsprechpartnerDto[] zur&uuml;ck, wo entweder die partnerIId oder die
	 * partnerIIdAnsprechpatner der &uuml;bergebenen partnerIId entspricht
	 * 
	 * @param iIdPartnerI  Integer
	 * @param theClientDto String
	 * @return AnsprechpartnerDto[]
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOrPartnerIIdAnsprechpartnerOhneExc(Integer iIdPartnerI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIIdOrPartnerIIdAnsprechpartner");
		query.setParameter(1, iIdPartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		aAnsprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		for (int i = 0; i < aAnsprechpartnerDtos.length; i++) {
			// suche den zugehoerigen partner.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(aAnsprechpartnerDtos[i].getPartnerIId(),
					theClientDto);
			aAnsprechpartnerDtos[i].setPartnerDto(partnerDto);
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + iIdPartnerI, ex);
		// return null;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);

		return aAnsprechpartnerDtos;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIId(Integer idPpartnerI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
		// try {
		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIId");
		query.setParameter(1, idPpartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		aAnsprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return aAnsprechpartnerDtos;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdAnsprechpartner(Integer idPpartnerI,
			TheClientDto theClientDto) {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
		// try {
		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIIdAnsprechpartner");
		query.setParameter(1, idPpartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		aAnsprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return aAnsprechpartnerDtos;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOhneExc(Integer idPpartnerI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
		// try {
		Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerIId");
		query.setParameter(1, idPpartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		aAnsprechpartnerDtos = assembleAnsprechpartnerDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return aAnsprechpartnerDtos;
	}

	/**
	 * &Uuml;berpr&uuml;ft, ob Ziel- und Quellansprechpartnerpartner
	 * vollst&auml;ndige Ansprechpartner sind
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 */
	private void checkInputParamsZielQuellAnsprechpartnerDtos(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto) throws EJBExceptionLP {

		if (ansprechpartnerZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerZielDto == null"));
		}
		if (ansprechpartnerZielDto.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerZielDto.getPartnerDto() == null"));
		}
		if (ansprechpartnerQuellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerQuellDto == null"));
		}
		if (ansprechpartnerQuellDto.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerQuellDto.getPartnerDto() == null"));
		}
	}

	/**
	 * H&auml;ngt beim Kurzbrief den Ansprechpartner (wenn vorhanden) vom
	 * Quellansprechpartner auf den Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignKurzbriefBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// PART_KURZBRIEF - ansprechpartner_i_id
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen
		// partnern kann es vorkommen, dass beim kurzbrief ein ansprechpartner
		// eingetragen ist, der nicht zu diesem partner gehoert (damit aber
		// keine daten verloren gehen, ist das inzwischen so)

		try {
			KurzbriefDto[] aKurzbriefDtos = null;
			aKurzbriefDtos = getPartnerFac().kurzbriefFindByAnsprechpartnerIIdOhneExc(ansprechpartnerQuellDto.getIId(),
					theClientDto);
			for (int j = 0; j < aKurzbriefDtos.length; j++) {
				if (aKurzbriefDtos[j] != null) {
					aKurzbriefDtos[j].setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					getPartnerFac().updateKurzbrief(aKurzbriefDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt bei der Anfrage den Ansprechpartner vom Quellansprechpartner auf
	 * den Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignAnfrageBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// ANF_ANFRAGE - ansprechpartner_iid_lieferant (mandantcnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen
		// lieferanten kann es vorkommen, dass bei der anfrage ein
		// ansprechpartner
		// eingetragen ist, der nicht zu diesem lieferanten gehoert (damit aber
		// keine daten verloren gehen, ist das inzwischen so)
		try {
			AnfrageDto[] aAnfrageDtos = null;
			aAnfrageDtos = getAnfrageFac().anfrageFindByAnsprechpartnerlieferantIIdMandantCNrOhneExc(
					ansprechpartnerQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAnfrageDtos.length; j++) {
				if (aAnfrageDtos[j] != null) {
					Anfrage zeile = em.find(Anfrage.class, aAnfrageDtos[j].getIId());
					zeile.setAnsprechpartnerIIdLieferant(ansprechpartnerZielDto.getIId());

					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&aum;ngt beim Angebot den Ansprechpartner vom Quellansprechpartner auf den
	 * Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignAngebotBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// ANGB_ANGEBOT - ansprechpartner_i_id_kunde (mandant_cnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen kunden
		// kann es vorkommen, dass beim angebot ein ansprechpartner
		// eingetragen ist, der nicht zu diesem kunden gehoert (damit aber keine
		// daten verloren gehen, ist das inzwischen so)
		try {
			AngebotDto[] aAngebotDtos = null;
			aAngebotDtos = getAngebotFac().angebotFindByAnsprechpartnerKundeIIdMandantCNrOhneExc(
					ansprechpartnerQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAngebotDtos.length; j++) {
				if (aAngebotDtos[j] != null) {
					Angebot zeile = em.find(Angebot.class, aAngebotDtos[j].getIId());
					zeile.setAnsprechpartnerIIdKunde(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}

			Query query = em.createNamedQuery("AngebotfindByAnsprechpartnerIIdRechnungsadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			Collection<?> lieferscheinDtos = query.getResultList();
			Iterator it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Angebot zeile = (Angebot) it.next();
				zeile.setAnsprechpartnerIIdRechnungsadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

			query = em.createNamedQuery("AngebotfindByAnsprechpartnerIIdLieferadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			lieferscheinDtos = query.getResultList();
			it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Angebot zeile = (Angebot) it.next();
				zeile.setAnsprechpartnerIIdLieferadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&aum;ngt beim Auftrag den Ansprechpartner vom Quellansprechpartner auf den
	 * Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignAuftragBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// AUFT_AUFTRAG - ansprechpartner_i_id_kunde (mandant_cnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen kunden
		// kann es vorkommen, dass beim auftrag ein ansprechpartner
		// eingetragen ist, der nicht zu diesem kunden gehoert (damit aber keine
		// daten verloren gehen, ist das inzwischen so)

		try {
			AuftragDto[] aAuftragDtos = null;
			aAuftragDtos = getAuftragFac().auftragFindByAnsprechpartnerIIdMandantCNrOhneExc(
					ansprechpartnerQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aAuftragDtos.length; j++) {
				if (aAuftragDtos[j] != null) {
					Auftrag zeile = em.find(Auftrag.class, aAuftragDtos[j].getIId());
					zeile.setAnsprechpartnerIIdKunde(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();

				}
			}

			Query query = em.createNamedQuery("AuftragfindByAnsprechpartnerIIdRechnungsadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			Collection<?> lieferscheinDtos = query.getResultList();
			Iterator it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Auftrag zeile = (Auftrag) it.next();
				zeile.setAnsprechpartnerIIdRechnungsadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

			query = em.createNamedQuery("AuftragfindByAnsprechpartnerIIdLieferadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			lieferscheinDtos = query.getResultList();
			it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Auftrag zeile = (Auftrag) it.next();
				zeile.setAnsprechpartnerIIdLieferadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	public void reassignKontakteBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("KontaktfindByAnsprechpartnerIId");
		query.setParameter(1, ansprechpartnerQuellDto.getIId());
		Collection<Kontakt> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Kontakt k = (Kontakt) it.next();

			k.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());

			em.merge(k);
			em.flush();
		}

	}

	public void reassignInseratBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {

		Query queryIR = em.createNamedQuery("InseratrechnungfindByAnsprechpartnerIId");
		queryIR.setParameter(1, ansprechpartnerQuellDto.getIId());
		Collection<Inseratrechnung> clIR = queryIR.getResultList();

		Iterator itIR = clIR.iterator();
		while (itIR.hasNext()) {
			Inseratrechnung k = (Inseratrechnung) itIR.next();

			k.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());

			em.merge(k);
			em.flush();
		}

		Query query = em.createNamedQuery("InseratfindByAnsprechpartnerIIdLieferant");
		query.setParameter(1, ansprechpartnerQuellDto.getIId());
		Collection<Inserat> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Inserat k = (Inserat) it.next();

			k.setAnsprechpartnerIIdLieferant(ansprechpartnerZielDto.getIId());

			em.merge(k);
			em.flush();
		}

	}

	public String getUebersteuerteEmpfaenger(PartnerDto partnerDto, String reportname, boolean bEmail,
			TheClientDto theClientDto) {

		String empfaenger = "";

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT ansp FROM FLRAnsprechpartner ansp WHERE ansp.flransprechpartnerfunktion.c_reportname='"
				+ reportname + "' AND ansp.partner_i_id=" + partnerDto.getIId();

		if (bEmail) {
			queryString += " AND ansp.c_email IS NOT NULL";
		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAnsprechpartner ansp = (FLRAnsprechpartner) resultListIterator.next();

			if (bEmail) {

				if (ansp.getC_email() != null && ansp.getC_email().length() > 0)
					empfaenger += ansp.getC_email();
				if (!resultListIterator.hasNext() == false) {
					empfaenger += ";";
				}
			} else {
				try {
					String p = getPartnerFac().partnerkommFindOhneExec(partnerDto.getIId(), ansp.getI_id(),
							PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);

					String pDirektFax = getPartnerFac().partnerkommFindOhneExec(partnerDto.getIId(), ansp.getI_id(),
							PartnerFac.KOMMUNIKATIONSART_DIREKTFAX, theClientDto.getMandant(), theClientDto);

					if (pDirektFax != null && pDirektFax.length() > 0) {
						empfaenger += Helper.befreieFaxnummerVonSonderzeichen(pDirektFax) + ";";
					} else {
						if (p != null) {
							empfaenger += Helper.befreieFaxnummerVonSonderzeichen(p) + ";";
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
		session.close();
		return empfaenger;
	}
	public ArrayList<Integer> getUebersteuerteAnsprechpartner(PartnerDto partnerDto, String reportname,
			TheClientDto theClientDto) {

		ArrayList<Integer> ansprechpartnerIIds=new ArrayList<Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT ansp FROM FLRAnsprechpartner ansp WHERE ansp.flransprechpartnerfunktion.c_reportname='"
				+ reportname + "' AND ansp.partner_i_id=" + partnerDto.getIId();

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAnsprechpartner ansp = (FLRAnsprechpartner) resultListIterator.next();

			ansprechpartnerIIds.add(ansp.getI_id());

		}
		session.close();
		return ansprechpartnerIIds;
	}

	/**
	 * H&aum;ngt beim Projekt den Ansprechpartner vom Quellansprechpartner auf den
	 * Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignProjektBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// PROJ_PROJEKT - ansprechpartner_i_id (mandant_cnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen
		// partnern kann es vorkommen, dass beim projekt ein ansprechpartner
		// eingetragen ist, der nicht zu diesem partner gehoert (damit aber
		// keine daten verloren gehen, ist das inzwischen so)

		try {
			ProjektDto[] aProjektDtos = null;
			aProjektDtos = getProjektFac()
					.projektFindByAnsprechpartnerIIdMandantCNrOhneExc(ansprechpartnerQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aProjektDtos.length; j++) {
				if (aProjektDtos[j] != null) {
					Projekt zeile = em.find(Projekt.class, aProjektDtos[j].getIId());
					zeile.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&aum;ngt beim Lieferschein den Ansprechpartner vom Quellansprechpartner auf
	 * den Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignLieferscheinBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// LS_LIEFERSCHEIN - ansprechpartner_i_id_kunde (mandant_cnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen kunden
		// kann es vorkommen, dass beim lieferschein ein ansprechpartner
		// eingetragen ist, der nicht zu diesem partner gehoert (damit aber
		// keine daten verloren gehen, ist das inzwischen so)

		try {
			LieferscheinDto[] aLieferscheinDtos = null;
			aLieferscheinDtos = getLieferscheinFac().lieferscheinFindByAnsprechpartnerIIdMandantCNrOhneExc(
					ansprechpartnerQuellDto.getIId(), mandantCNr, theClientDto);
			for (int j = 0; j < aLieferscheinDtos.length; j++) {
				if (aLieferscheinDtos[j] != null) {
					Lieferschein zeile = em.find(Lieferschein.class, aLieferscheinDtos[j].getIId());
					zeile.setAnsprechpartnerIIdKunde(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();

				}
			}

			Query query = em.createNamedQuery("LieferscheinfindByAnsprechpartnerIIdRechnungsadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			Collection<?> lieferscheinDtos = query.getResultList();
			Iterator it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Lieferschein zeile = (Lieferschein) it.next();
				zeile.setAnsprechpartnerIIdRechnungsadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&aum;ngt bei der Bestellung den Ansprechpartner vom Quellansprechpartner auf
	 * den Zielansprechpartner um
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param mandantCNr              String
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignBestellungBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		// BES_BESTELLUNG - ansprechpartner_i_id (mandant_cnr)
		// beim zusammenfuehren 2er ansprechpartner von unterschiedlichen
		// lieferanten kann es vorkommen, dass bei der bestellung ein
		// ansprechpartner
		// eingetragen ist, der nicht zu diesem lieferanten gehoert (damit aber
		// keine daten verloren gehen, ist das inzwischen so)
		try {
			BestellungDto[] aBestellungDtos = null;
			aBestellungDtos = getBestellungFac()
					.bestellungFindByAnsprechpartnerIIdMandantCNrOhneExc(ansprechpartnerQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aBestellungDtos.length; j++) {
				if (aBestellungDtos[j] != null) {

					Bestellung bestellung = em.find(Bestellung.class, aBestellungDtos[j].getIId());

					bestellung.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					em.merge(bestellung);
					em.flush();

				}
			}

			Query query = em.createNamedQuery("BestellungfindByAnsprechpartnerIIdLieferadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			Collection<?> lieferscheinDtos = query.getResultList();
			Iterator it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Bestellung zeile = (Bestellung) it.next();
				zeile.setAnsprechpartnerIIdLieferadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

			query = em.createNamedQuery("BestellungfindByAnsprechpartnerIIdAbholadresseMandantCNr");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			lieferscheinDtos = query.getResultList();
			it = lieferscheinDtos.iterator();
			while (it.hasNext()) {
				Bestellung zeile = (Bestellung) it.next();
				zeile.setAnsprechpartnerIIdAbholadresse(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * F&uuml;hrt 2 Ansprechpartner zusammen (die zugrundeliegenden Partner werden
	 * nicht zusammengef&uuml;hrt)
	 * 
	 * @param ansprechpartnerZielDto     AnsprechpartnerDto
	 * @param ansprechpartnerQuellDtoIid int
	 * @param partnerDto                 PartnerDto - enthaelt daten wie name1,
	 *                                   name2, titel, anrede vom zugrundeliegenden
	 *                                   partner
	 * @param theClientDto               String
	 * @throws EJBExceptionLP
	 */
	public void zusammenfuehrenAnsprechpartner(AnsprechpartnerDto ansprechpartnerZielDto,
			int ansprechpartnerQuellDtoIid, PartnerDto partnerDto, TheClientDto theClientDto) throws EJBExceptionLP {

		AnsprechpartnerDto ansprechpartnerQuellDto = null;
		MandantDto[] aMandantDtos = null;

		if (ansprechpartnerZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ansprechpartnerZielDto == null (Ziel oder Quell)"));
		}

		if (ansprechpartnerZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("ansprechpartnerZielDto.getIId() == null (Ziel)"));
		}

		if (partnerDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null"));
		}

		if (partnerDto.getLocaleCNrKommunikation() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getLocaleCNrKommunikation() == null"));
		}

		myLogger.info("Zielansprechpartner: " + ansprechpartnerZielDto.toString());
		myLogger.info("zugrundeliegenderPartner: " + partnerDto.toString());

		try {
			ansprechpartnerQuellDto = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(ansprechpartnerQuellDtoIid, theClientDto);

			if (ansprechpartnerQuellDto != null) {
				myLogger.info("Quellansprechpartner: " + ansprechpartnerQuellDto.toString());

				// falls die grundlegenden Partnerdaten angepasst werden sollen
				// (name1, name2, titel, anrede)
				getPartnerFac().updatePartner(partnerDto, theClientDto);

				updateAnsprechpartner(ansprechpartnerZielDto, theClientDto);

				ansprechpartnerZielDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerZielDto.getIId(), theClientDto);
				ansprechpartnerQuellDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerQuellDto.getIId(), theClientDto);

				// mandantenunabhaengig
				reassignKurzbriefBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignReiseBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignKontakteBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignReiseLogBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignTelefonzeitenBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignAgstklBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignRechnungBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignReklamationBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignEinkaufsangebotZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);
				reassignDokumenteZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto);
				reassignInseratBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto, theClientDto);

				// mandantenabhaengig
				aMandantDtos = getMandantFac().mandantFindAll(theClientDto);
				int i = 0;
				while (i < aMandantDtos.length) {
					reassignAnfrageBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignAngebotBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignAuftragBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignProjektBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignLieferscheinBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignBestellungBeimZusammenfuehren(ansprechpartnerZielDto, ansprechpartnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					i++;
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// Quellansprechpartner loeschen
		if (ansprechpartnerQuellDto != null) {
			try {
				getAnsprechpartnerFac().removeAnsprechpartner(ansprechpartnerQuellDto, theClientDto);
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

	}

	private void reassignDokumenteZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto) {
		getJCRDocFac().fuehreDokumenteZusammen(ansprechpartnerZielDto.getPartnerDto(),
				ansprechpartnerQuellDto.getPartnerDto());
	}

	/**
	 * reassignEinkaufsangebotZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignEinkaufsangebotZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		EinkaufsangebotDto[] aEinkaufsangebotDto = null;
		try {
			aEinkaufsangebotDto = getAngebotstklFac()
					.einkaufsangebotFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aEinkaufsangebotDto.length; j++) {
				if (aEinkaufsangebotDto[j] != null) {

					Einkaufsangebot bestellung = em.find(Einkaufsangebot.class, aEinkaufsangebotDto[j].getIId());

					bestellung.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					em.merge(bestellung);
					em.flush();

				}
			}

		} catch (RemoteException ex) {
			// }
			// catch (FinderException ex) {

		}
	}

	/**
	 * reassignReklamationBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignReklamationBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		ReklamationDto[] aReklamationDto = null;
		try {
			aReklamationDto = getReklamationFac().reklamationFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aReklamationDto.length; j++) {
				if (aReklamationDto[j] != null) {

					Reklamation zeile = em.find(Reklamation.class, aReklamationDto[j].getIId());
					zeile.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					zeile.setAnsprechpartnerIId(null);
					em.merge(zeile);
					em.flush();

				}
			}

			Query query = em.createNamedQuery("ReklamationfindByAnsprechpartnerIIdLieferant");
			query.setParameter(1, ansprechpartnerQuellDto.getIId());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Reklamation zeile = (Reklamation) it.next();
				zeile.setAnsprechpartnerIIdLieferant(ansprechpartnerZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		} catch (RemoteException ex) {
			// }
			// catch (FinderException ex) {
		} catch (EJBExceptionLP ex) {
		}
	}

	/**
	 * reassignRechnungBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignRechnungBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		RechnungDto[] aRechnungDto = null;
		try {
			aRechnungDto = getRechnungFac().rechnungFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aRechnungDto.length; j++) {
				if (aRechnungDto[j] != null) {

					Rechnung zeile = em.find(Rechnung.class, aRechnungDto[j].getIId());
					zeile.setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();

				}
			}

		} catch (RemoteException ex) {

		} catch (EJBExceptionLP ex) {
		}

	}

	/**
	 * reassignAgsrklBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignAgstklBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		AgstklDto[] aAgstklDto = null;
		try {
			aAgstklDto = getAngebotstklFac().agstklFindByAnsprechpartnerIIdKunde(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aAgstklDto.length; j++) {
				if (aAgstklDto[j] != null) {
					Agstkl zeile = em.find(Agstkl.class, aAgstklDto[j].getIId());
					zeile.setAnsprechpartnerIIdKunde(ansprechpartnerZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}

		} catch (RemoteException ex) {
			// }
			// catch (FinderException ex) {
		} catch (EJBExceptionLP ex) {
		}

	}

	/**
	 * reassignTelefonzeitenBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignTelefonzeitenBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		TelefonzeitenDto[] aTelefonzeitenDtos = null;
		try {
			aTelefonzeitenDtos = getZeiterfassungFac()
					.telefonzeitenFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aTelefonzeitenDtos.length; j++) {
				if (aTelefonzeitenDtos[j] != null) {
					aTelefonzeitenDtos[j].setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					getZeiterfassungFac().updateTelefonzeiten(aTelefonzeitenDtos[j]);
				}
			}

		} catch (RemoteException ex) {
			// }
			// catch (FinderException ex) {
		} catch (EJBExceptionLP ex) {
		}
	}

	/**
	 * reassignReiseLogBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignReiseLogBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		ReiselogDto[] aReiselogDtos = null;
		try {
			aReiselogDtos = getZeiterfassungFac().reiselogFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			for (int j = 0; j < aReiselogDtos.length; j++) {
				if (aReiselogDtos[j] != null) {
					aReiselogDtos[j].setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					getZeiterfassungFac().updateReiselog(aReiselogDtos[j]);
				}
			}

		} catch (RemoteException ex) {
			// }
			// catch (FinderException ex) {
		} catch (EJBExceptionLP ex) {
		}
	}

	/**
	 * reassignReiseBeimZusammenfuehren
	 * 
	 * @param ansprechpartnerZielDto  AnsprechpartnerDto
	 * @param ansprechpartnerQuellDto AnsprechpartnerDto
	 * @param theClientDto            String
	 */
	public void reassignReiseBeimZusammenfuehren(AnsprechpartnerDto ansprechpartnerZielDto,
			AnsprechpartnerDto ansprechpartnerQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellAnsprechpartnerDtos(ansprechpartnerZielDto, ansprechpartnerQuellDto);

		try {
			ReiseDto[] aReiseDtos = null;
			try {
				aReiseDtos = getZeiterfassungFac().reiseFindByAnsprechpartnerIId(ansprechpartnerQuellDto.getIId());
			} catch (EJBExceptionLP ex) {
				// }
				// catch (FinderException ex) {
			} catch (RemoteException ex) {
			}
			for (int j = 0; j < aReiseDtos.length; j++) {
				if (aReiseDtos[j] != null) {
					aReiseDtos[j].setAnsprechpartnerIId(ansprechpartnerZielDto.getIId());
					getZeiterfassungFac().updateReise(aReiseDtos[j]);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByEmail(String email, TheClientDto theClientDto) {

		Query query = AnsprechpartnerQuery.byEmail(em, email);
		return assembleAnsprechpartnerDtos(query.getResultList());
	}
}
