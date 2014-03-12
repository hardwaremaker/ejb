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
package com.lp.server.angebot.ejbfac;

import java.rmi.*;
import java.util.*;

import com.lp.server.angebot.ejb.*;
import com.lp.server.angebot.service.*;
import com.lp.server.system.ejb.Einheitspr;
import com.lp.server.system.ejb.EinheitsprPK;
import com.lp.server.system.pkgenerator.*;
import com.lp.server.util.*;
import com.lp.util.*;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AngebotServiceFacBean extends Facade implements AngebotServiceFac {
	@PersistenceContext
	private EntityManager em;

	// Angebottext
	// ---------------------------------------------------------------

	/**
	 * Einen neuen Angebotstext anlegen.
	 * 
	 * @param angebottextDtoI
	 *            das neue Angebot
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Angebots
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkAngebottextDto(angebottextDtoI);
		myLogger.logData(angebottextDtoI);

		// den PK erzeugen und setzen
		Integer iIdAngebottext = null;

		iIdAngebottext = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_ANGEBOTTEXT);
		angebottextDtoI.setIId(iIdAngebottext);

		try {
			Angebottext angebottext = new Angebottext(angebottextDtoI.getIId(),
					angebottextDtoI.getMandantCNr(),
					angebottextDtoI.getLocaleCNr(),
					angebottextDtoI.getMediaartCNr(),
					angebottextDtoI.getXTextinhalt());
			em.persist(angebottext);
			em.flush();

			setAngebottextFromAngebottextDto(angebottext, angebottextDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iIdAngebottext;
	}

	/**
	 * Einen bestehenden Angebotstext loeschen.
	 * 
	 * @param angebottextDtoI
	 *            das zu loeschende Angebot
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebottextDto(angebottextDtoI);
		myLogger.logData(angebottextDtoI);
		Angebottext toRemove = em.find(Angebottext.class,
				angebottextDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei removeAngebottext. Angebottext mit iid "
									+ angebottextDtoI.getIId()
									+ " und Textinhalt "
									+ angebottextDtoI.getXTextinhalt()
									+ " konnte nicht gefunden werden"));
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	/**
	 * Einen bestehenden Angebotstext aktualisieren.
	 * 
	 * @param angebottextDtoI
	 *            der bestehende Angebotstext
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebottextDto(angebottextDtoI);
		myLogger.logData(angebottextDtoI);
		Angebottext angebottext = em.find(Angebottext.class,
				angebottextDtoI.getIId());
		if (angebottext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei updateAngebottext. Angebottext mit iid "
									+ angebottextDtoI.getIId()
									+ "konnte nicht gefunden werden. AngebottextDto.toString() :"
									+ angebottextDtoI.toString()));
		}
		setAngebottextFromAngebottextDto(angebottext, angebottextDtoI);
	}

	public AngebottextDto angebottextFindByPrimaryKey(Integer iIdAngebottextI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AngebottextDto textDto = null;
		Angebottext text = em.find(Angebottext.class, iIdAngebottextI);
		if (text == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei angebottextFindByPrimaryKey. Es gibt keinen Text mit iid "
									+ iIdAngebottextI));
		}
		textDto = assembleAngebottextDto(text);
		return textDto;
	}

	private void setAngebottextFromAngebottextDto(Angebottext angebottext,
			AngebottextDto angebottextDto) {
		angebottext.setMandantCNr(angebottextDto.getMandantCNr());
		angebottext.setLocaleCNr(angebottextDto.getLocaleCNr());
		angebottext.setMediaartCNr(angebottextDto.getMediaartCNr());
		angebottext.setXTextinhalt(angebottextDto.getXTextinhalt());
		em.merge(angebottext);
		em.flush();
	}

	private AngebottextDto assembleAngebottextDto(Angebottext angebottext) {
		return AngebottextDtoAssembler.createDto(angebottext);
	}

	private AngebottextDto[] assembleAngebottextDtos(Collection<?> angebottexts) {
		List<AngebottextDto> list = new ArrayList<AngebottextDto>();
		if (angebottexts != null) {
			Iterator<?> iterator = angebottexts.iterator();
			while (iterator.hasNext()) {
				Angebottext angebottext = (Angebottext) iterator.next();
				list.add(assembleAngebottextDto(angebottext));
			}
		}
		AngebottextDto[] returnArray = new AngebottextDto[list.size()];
		return (AngebottextDto[]) list.toArray(returnArray);
	}

	private void checkAngebottextDto(AngebottextDto angebottextDtoI)
			throws EJBExceptionLP {
		if (angebottextDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angebottextDtoI == null"));
		}
	}

	private void checkAngebottextIId(Integer iIdAngebottextI)
			throws EJBExceptionLP {
		if (iIdAngebottextI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebottextI == null"));
		}
	}

	public AngebottextDto angebottextFindByMandantCNrLocaleCNrCNr(
			String cNrLocaleI, String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNrLocaleI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLocaleI == null"));
		}

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}

		StringBuffer buff = new StringBuffer();
		buff.append("localeCNr: ").append(cNrLocaleI).append(", cNr:")
				.append(cNrI);

		myLogger.info(buff.toString());

		AngebottextDto text = null;
		Angebottext textObject = null;

		// Schritt 1 : Sprache des Kunden ist Parameter
		Query query = em
				.createNamedQuery("AngebottextfindByMandantCNrLocaleCNrMediaartCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cNrLocaleI);
		query.setParameter(3, cNrI);

		try {
			textObject = (Angebottext) query.getSingleResult();
		} catch(NoResultException e) {
		}
		
		if (textObject == null) {
			try {
				// Schritt 2 : Den Angebottext in Gewaehlte UI-Sprache des Users
				// anlegen
				createDefaultAngebottext(cNrI, cNrLocaleI, theClientDto);
				Query query1 = em
						.createNamedQuery("AngebottextfindByMandantCNrLocaleCNrMediaartCNr");
				query1.setParameter(1, theClientDto.getMandant());
				query1.setParameter(2, cNrLocaleI);
				query1.setParameter(3, cNrI);
				textObject = (Angebottext) query.getSingleResult();
			} catch (Exception exUi) {
			}
		}
		text = assembleAngebottextDto(textObject);

		myLogger.exit(text.getXTextinhalt());

		return text;
	}

	/**
	 * Einen Default Angebottext anlegen.
	 * 
	 * @param cNrMediaartI
	 *            Mediaart Kopftext oder Fusstext
	 * @param cNrLocaleI
	 *            der Textinhalt
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @return AngebottextDto der Default Angebottext
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AngebottextDto createDefaultAngebottext(String cNrMediaartI,
			String cNrLocaleI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNrMediaartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrMediaartI == null"));
		}

		if (cNrLocaleI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLocaleI == null"));
		}
		AngebottextDto angebottextDto = new AngebottextDto();
		angebottextDto.setMediaartCNr(cNrMediaartI);
		angebottextDto.setLocaleCNr(cNrLocaleI);
		angebottextDto.setMandantCNr(theClientDto.getMandant());
		String cTextinhalt = null;

		if (cNrMediaartI.equals(MediaFac.MEDIAART_KOPFTEXT)) {
			cTextinhalt = AngebotServiceFac.ANGEBOT_DEFAULT_KOPFTEXT;
		} else if (cNrMediaartI.equals(MediaFac.MEDIAART_FUSSTEXT)) {
			cTextinhalt = AngebotServiceFac.ANGEBOT_DEFAULT_FUSSTEXT;
		}

		angebottextDto.setXTextinhalt(cTextinhalt);
		angebottextDto.setIId(createAngebottext(angebottextDto, theClientDto));

		return angebottextDto;
	}

	// Angebotartspr
	// -------------------------------------------------------------

	public void createAngebotartspr(AngebotartsprDto angebotartsprDto)
			throws EJBExceptionLP {

		if (angebotartsprDto == null) {
			return;
		}
		try {
			Angebotartspr angebotartspr = new Angebotartspr(
					angebotartsprDto.getLocaleCNr(),
					angebotartsprDto.getAngebotartCNr(),
					angebotartsprDto.getCBez());
			em.persist(angebotartspr);
			em.flush();
			setAngebotartsprFromAngebotartsprDto(angebotartspr,
					angebotartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAngebotartspr(AngebotartsprDto angebotartsprDtoI)
			throws EJBExceptionLP {
		AngebotartsprPK angebotartsprPK = new AngebotartsprPK();
		angebotartsprPK.setLocaleCNr(angebotartsprDtoI.getLocaleCNr());
		angebotartsprPK.setAngebotartCNr(angebotartsprDtoI.getAngebotartCNr());
		Angebotartspr toRemove = em.find(Angebotartspr.class, angebotartsprPK);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei removeAngebotartsprache. Es gibt keine Angebotartspr mit der Locale "
									+ angebotartsprDtoI.getLocaleCNr()
									+ " und der art "
									+ angebotartsprDtoI.getAngebotartCNr()));
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateAngebotartspr(AngebotartsprDto angebotartsprDto)
			throws EJBExceptionLP {
		if (angebotartsprDto != null) {
			AngebotartsprPK angebotartsprPK = new AngebotartsprPK();
			angebotartsprPK.setLocaleCNr(angebotartsprDto.getLocaleCNr());
			angebotartsprPK.setAngebotartCNr(angebotartsprDto
					.getAngebotartCNr());
			Angebotartspr angebotartspr = em.find(Angebotartspr.class,
					angebotartsprPK);
			if (angebotartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei updateAngebotartsprache. Es gibt keine Angebotartspr mit der Locale "
										+ angebotartsprDto.getLocaleCNr()
										+ " und der art "
										+ angebotartsprDto.getAngebotartCNr()));
			}
			setAngebotartsprFromAngebotartsprDto(angebotartspr,
					angebotartsprDto);
		}
	}

	public AngebotartsprDto angebotartsprFindByPrimaryKey(String localeCNrI,
			String angebotartCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AngebotartsprPK angebotartsprPK = new AngebotartsprPK();
		angebotartsprPK.setLocaleCNr(localeCNrI);
		angebotartsprPK.setAngebotartCNr(angebotartCNrI);
		Angebotartspr angebotartspr = em.find(Angebotartspr.class,
				angebotartsprPK);
		if (angebotartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei angebotartsprfindbyprimarykey. Es gibt keine Angebotartspr mit der Locale "
									+ localeCNrI
									+ " und der art "
									+ angebotartCNrI));
		}
		return assembleAngebotartsprDto(angebotartspr);
	}

	public AngebotartsprDto angebotartsprFindByLocaleCNrAngebotartCNr(
			String cNrLocaleI, String cNrAngebotartI) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("AngebotartsprfindByLocaleCNrAngebotartCNr");
		query.setParameter(1, cNrLocaleI);
		query.setParameter(2, cNrAngebotartI);
		Angebotartspr angebotartspr = (Angebotartspr) query.getSingleResult();
		if (angebotartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception(
							"FEhjler bei angebotartspr find by Locale und angebotartCnr. Es gibt keine Angebotartspr mit locale "
									+ cNrLocaleI
									+ " und der Art "
									+ cNrAngebotartI));
		}
		return assembleAngebotartsprDto(angebotartspr);
	}

	private void setAngebotartsprFromAngebotartsprDto(
			Angebotartspr angebotartspr, AngebotartsprDto angebotartsprDto) {
		angebotartspr.setCBez(angebotartsprDto.getCBez());
		em.merge(angebotartspr);
		em.flush();
	}

	private AngebotartsprDto assembleAngebotartsprDto(
			Angebotartspr angebotartspr) {
		return AngebotartsprDtoAssembler.createDto(angebotartspr);
	}

	private AngebotartsprDto[] assembleAngebotartsprDtos(
			Collection<?> angebotartsprs) {
		List<AngebotartsprDto> list = new ArrayList<AngebotartsprDto>();
		if (angebotartsprs != null) {
			Iterator<?> iterator = angebotartsprs.iterator();
			while (iterator.hasNext()) {
				Angebotartspr angebotartspr = (Angebotartspr) iterator.next();
				list.add(assembleAngebotartsprDto(angebotartspr));
			}
		}
		AngebotartsprDto[] returnArray = new AngebotartsprDto[list.size()];
		return (AngebotartsprDto[]) list.toArray(returnArray);
	}

	// Angebotart
	// ----------------------------------------------------------------

	/**
	 * Alle Angebotarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            alternatives Locale
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Map die Liste der Angebotarten
	 */
	public Map getAngebotarten(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		Map<String, String> map = null;
		Query query = em.createNamedQuery("AngebotartfindAll");
		Collection<?> arten = query.getResultList();
		AngebotartDto[] artDtos = assembleAngebotartDtos(arten);
		map = this.uebersetzeAngebotartenOptimal(artDtos, locale1, locale2);
		return map;
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von Angebotarten.
	 * 
	 * @param pArray
	 *            die Liste der Angebotarten
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            alternatives Locale
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Map die Liste der uebersetzten Angebotarten
	 */
	private Map<String, String> uebersetzeAngebotartenOptimal(
			AngebotartDto[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		myLogger.entry();

		// @todo check param PJ 3750

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeAngebotartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			map.put(key, value);
		}

		return map;
	}

	/**
	 * Uebersetzt eine Angebotart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            der Name der Angebotart
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            Locale Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Angebotart in der bestmoeglichen Uebersetzung
	 */
	private String uebersetzeAngebotartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		myLogger.entry();

		String uebersetzung = "";

		try {
			uebersetzung = uebersetzeAngebotart(locale1, cNr);
		} catch (Throwable t1) {
			try {
				uebersetzung = uebersetzeAngebotart(locale2, cNr);
			} catch (Throwable t2) {
				uebersetzung = cNr;
			}
		}

		return uebersetzung;
	}

	/**
	 * Eine Angebotart in eine bestimmte Sprache uebersetzen.
	 * 
	 * @param pLocale
	 *            die gewuenschte Sprache
	 * @param pArt
	 *            die Angebotart
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return String die Uebersetzung
	 */
	private String uebersetzeAngebotart(Locale pLocale, String pArt)
			throws EJBExceptionLP {
		Angebotartspr spr = null;
		String locale = Helper.locale2String(pLocale);
		Query query = em
				.createNamedQuery("AngebotartsprfindByLocaleCNrAngebotartCNr");
		query.setParameter(1, locale);
		query.setParameter(2, pArt);
		spr = (Angebotartspr) query.getSingleResult();
		if (spr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei uebersetzeAngebotart. Es gibt keine Angebotartspr mit der Locale "
									+ locale + " und der Art " + pArt));
		}
		return spr.getCBez();
	}

	/**
	 * Eine neue Angebotart anlegen.
	 * 
	 * @param angebotartDtoI
	 *            die neue Angebotart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String PK der neuen Angebotart
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String createAngebotart(AngebotartDto angebotartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotartDto(angebotartDtoI);
		myLogger.logData(angebotartDtoI);
		try {
			// zuerst die Angebotart
			Angebotart angebotart = new Angebotart(angebotartDtoI.getCNr(),
					angebotartDtoI.getISort());
			em.persist(angebotart);
			em.flush();
			setAngebotartFromAngebotartDto(angebotart, angebotartDtoI);
			// dann die Spr
			if (angebotartDtoI.getAngebotartsprDto() != null) {
				Angebotartspr angebotartspr = new Angebotartspr(angebotartDtoI
						.getAngebotartsprDto().getLocaleCNr(),
						angebotartDtoI.getCNr(), angebotartDtoI
								.getAngebotartsprDto().getCBez());
				em.persist(angebotartspr);
				em.flush();
				setAngebotartsprFromAngebotartsprDto(angebotartspr,
						angebotartDtoI.getAngebotartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return angebotartDtoI.getCNr();
	}

	/**
	 * Eine bestehende Angebotart loeschen.
	 * 
	 * @param cNrAngebotartI
	 *            die bestehende Angebotart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAngebotart(String cNrAngebotartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNrAngebotartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngebotartI == null"));
		}
		myLogger.logData(cNrAngebotartI);
		// zuerst alle Sprs loeschen
		Query query = em.createNamedQuery("AngebotartsprfindByAngebotartCNr");
		query.setParameter(1, cNrAngebotartI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Angebotartspr toRemove = em.find(Angebotartspr.class, it.next());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei removeAngebotart."
										+ "Angebotsprachen konnten nicht gefunden werden"));
			}
			em.remove(toRemove);
			em.flush();
		}

		// jetzt die Angebotart loeschen
		Angebotart toRemove = em.find(Angebotart.class, cNrAngebotartI);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei removeAngebotart.Es gibt keine Angebotart mit Cnr "
									+ cNrAngebotartI));
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	/**
	 * Eine bestehende Angebotart aktualisieren.
	 * 
	 * @param angebotartDtoI
	 *            die bestehende Angebotart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotart(AngebotartDto angebotartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotartDto(angebotartDtoI);
		myLogger.logData(angebotartDtoI);

		try {
			// erst die Angebotart
			Angebotart angebotart = em.find(Angebotart.class,
					angebotartDtoI.getCNr());
			if (angebotart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception("Fehler bei updateAngebotart."
								+ " Es gibt keine Angebotart mit cnr "
								+ angebotartDtoI.getCNr()
								+ " angebotartDto.toString():"
								+ angebotartDtoI.toString()));
			}

			setAngebotartFromAngebotartDto(angebotart, angebotartDtoI);

			// jetzt die Spr
			AngebotartsprDto angebotartsprDto = angebotartDtoI
					.getAngebotartsprDto();

			if (angebotartsprDto != null
					&& angebotartsprDto.getAngebotartCNr() != null) {
				AngebotartsprPK angebotartsprPK = new AngebotartsprPK();
				angebotartsprPK.setLocaleCNr(angebotartsprDto.getLocaleCNr());
				angebotartsprPK.setAngebotartCNr(angebotartsprDto
						.getAngebotartCNr());

				Angebotartspr angebotartspr = em.find(Angebotartspr.class,
						angebotartsprPK);

				setAngebotartsprFromAngebotartsprDto(angebotartspr,
						angebotartsprDto);
			} else {
				Angebotartspr angebotartspr = new Angebotartspr(angebotartDtoI
						.getAngebotartsprDto().getLocaleCNr(),
						angebotartDtoI.getCNr(), angebotartDtoI
								.getAngebotartsprDto().getCBez());
				em.persist(angebotartspr);
				em.flush();

				setAngebotartsprFromAngebotartsprDto(angebotartspr,
						angebotartDtoI.getAngebotartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public AngebotartDto angebotartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkAngebotartCNr(cNrI);
		myLogger.logData(cNrI);
		AngebotartDto angebotartDto = null;
		// zuerst die Angebotart lesen
		Angebotart angebotart = em.find(Angebotart.class, cNrI);
		if (angebotart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei find Angebotart by Primary Key. Es gibt keine Angebotart mit der Cnr "
									+ cNrI));
		}
		angebotartDto = assembleAngebotartDto(angebotart);

		// jetzt die Spr
		AngebotartsprDto angebotartsprDto = null;

		try {
			AngebotartsprPK angebotartsprPK = new AngebotartsprPK(
					theClientDto.getLocUiAsString(), cNrI);
			Angebotartspr angebotartspr = em.find(Angebotartspr.class,
					angebotartsprPK);
			if (angebotartspr != null) {
				angebotartsprDto = assembleAngebotartsprDto(angebotartspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		angebotartDto.setAngebotartsprDto(angebotartsprDto);
		return angebotartDto;
	}

	private void checkAngebotartDto(AngebotartDto angebotartDtoI)
			throws EJBExceptionLP {
		if (angebotartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angebotartDtoI == null"));
		}
	}

	private void checkAngebotartCNr(String cNrAngebotartI)
			throws EJBExceptionLP {
		if (cNrAngebotartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngebotartI == null"));
		}
	}

	private void setAngebotartFromAngebotartDto(Angebotart angebotart,
			AngebotartDto angebotartDto) {
		angebotart.setISort(angebotartDto.getISort());
		em.merge(angebotart);
		em.flush();
	}

	private AngebotartDto assembleAngebotartDto(Angebotart angebotart) {
		return AngebotartDtoAssembler.createDto(angebotart);
	}

	private AngebotartDto[] assembleAngebotartDtos(Collection<?> angebotarts) {
		List<AngebotartDto> list = new ArrayList<AngebotartDto>();
		if (angebotarts != null) {
			Iterator<?> iterator = angebotarts.iterator();
			while (iterator.hasNext()) {
				Angebotart angebotart = (Angebotart) iterator.next();
				list.add(assembleAngebotartDto(angebotart));
			}
		}
		AngebotartDto[] returnArray = new AngebotartDto[list.size()];
		return (AngebotartDto[]) list.toArray(returnArray);
	}

	// Angebotstatus
	// -------------------------------------------------------------

	public void createAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP {
		if (angebotstatusDto == null) {
			return;
		}
		try {
			Angebotstatus angebotstatus = new Angebotstatus(
					angebotstatusDto.getStatusCNr(),
					angebotstatusDto.getISort());
			em.persist(angebotstatus);
			em.flush();
			setAngebotstatusFromAngebotstatusDto(angebotstatus,
					angebotstatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP {

		Angebotstatus toRemove = em.find(Angebotstatus.class,
				angebotstatusDto.getStatusCNr());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAngebotstatus. Es gibt keinen Status mit der CNr "
							+ angebotstatusDto.getStatusCNr()
							+ "angebotstatusdDto.toString():"
							+ angebotstatusDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP {
		if (angebotstatusDto != null) {
			String statusCNr = angebotstatusDto.getStatusCNr();
			Angebotstatus angebotstatus = em.find(Angebotstatus.class,
					statusCNr);
			if (angebotstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAngebotsstatus es gibr krinrn Status mit cnr "
								+ statusCNr + "\nangebotsstatusDto.toString "
								+ angebotstatusDto.toString());
			}
			setAngebotstatusFromAngebotstatusDto(angebotstatus,
					angebotstatusDto);
		}
	}

	public AngebotstatusDto angebotstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		Angebotstatus angebotstatus = em.find(Angebotstatus.class, statusCNr);
		if (angebotstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei Angebotsstatus findbyPrimaryKey. Es gibt keinen Stauts "
							+ statusCNr);
		}
		return assembleAngebotstatusDto(angebotstatus);
	}

	private void setAngebotstatusFromAngebotstatusDto(
			Angebotstatus angebotstatus, AngebotstatusDto angebotstatusDto) {
		angebotstatus.setISort(angebotstatusDto.getISort());
		em.merge(angebotstatus);
		em.flush();
	}

	private AngebotstatusDto assembleAngebotstatusDto(
			Angebotstatus angebotstatus) {
		return AngebotstatusDtoAssembler.createDto(angebotstatus);
	}

	private AngebotstatusDto[] assembleAngebotstatusDtos(
			Collection<?> angebotstatuss) {
		List<AngebotstatusDto> list = new ArrayList<AngebotstatusDto>();
		if (angebotstatuss != null) {
			Iterator<?> iterator = angebotstatuss.iterator();
			while (iterator.hasNext()) {
				Angebotstatus angebotstatus = (Angebotstatus) iterator.next();
				list.add(assembleAngebotstatusDto(angebotstatus));
			}
		}
		AngebotstatusDto[] returnArray = new AngebotstatusDto[list.size()];
		return (AngebotstatusDto[]) list.toArray(returnArray);
	}

	// Angeboteinheit
	// ------------------------------------------------------------

	public void createAngeboteinheit(AngeboteinheitDto angeboteinheitDto)
			throws EJBExceptionLP {
		if (angeboteinheitDto == null) {
			return;
		}
		try {
			Angeboteinheit angeboteinheit = new Angeboteinheit(
					angeboteinheitDto.getEinheitCNr(),
					angeboteinheitDto.getISort());
			em.persist(angeboteinheit);
			em.flush();
			setAngeboteinheitFromAngeboteinheitDto(angeboteinheit,
					angeboteinheitDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAngeboteinheit(String einheitCNr) throws EJBExceptionLP {

		Angeboteinheit toRemove = em.find(Angeboteinheit.class, einheitCNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAngeboteinheit. Es gibt keine Einheit "
							+ einheitCNr);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateAngeboteinheit(AngeboteinheitDto angeboteinheitDto)
			throws EJBExceptionLP {
		if (angeboteinheitDto != null) {
			String einheitCNr = angeboteinheitDto.getEinheitCNr();
			Angeboteinheit angeboteinheit = em.find(Angeboteinheit.class,
					einheitCNr);
			if (angeboteinheit == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAngeboteinheit. Es gibt keine Einheit "
								+ einheitCNr + "\nangeboteinheitDto.toString"
								+ angeboteinheitDto.toString());
			}
			setAngeboteinheitFromAngeboteinheitDto(angeboteinheit,
					angeboteinheitDto);
		}
	}

	public AngeboteinheitDto angeboteinheitFindByPrimaryKey(String einheitCNr)
			throws EJBExceptionLP {
		Angeboteinheit angeboteinheit = em.find(Angeboteinheit.class,
				einheitCNr);
		if (angeboteinheit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AngeboteinheitFindByPrimaryKey. Es gibt keine Einheit "
							+ einheitCNr);
		}
		return assembleAngeboteinheitDto(angeboteinheit);
	}

	/**
	 * Alle Angeboteinheiten holen. Aufbereitet fuer die Darstellung in einer
	 * ComboBox.
	 * 
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Map die Liste der Einheiten
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Map getAngeboteinheiten(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Map<String, String> map = new TreeMap<String, String>();
		Query query = em.createNamedQuery("AngeboteinheitfindAll");
		Collection<?> cl = query.getResultList();
		if (cl.isEmpty()) { // @ToDo FinderException
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDALL,
					"fehler bei AngeboteinheitenFindAll. Es konnten keine angeboteinheiten gefunden werden");
		}
		AngeboteinheitDto[] aEinheitDto = assembleAngeboteinheitDtos(cl);

		for (int i = 0; i < aEinheitDto.length; i++) {
			String key = aEinheitDto[i].getEinheitCNr();
			String value = aEinheitDto[i].getEinheitCNr(); // Ausgabe "mm"
															// (nicht
															// "Millimeter")
															// (WH)

			Einheitspr einheitspr = em.find(Einheitspr.class,
					new EinheitsprPK(aEinheitDto[i].getEinheitCNr(),
							theClientDto.getLocUiAsString()));

			if (einheitspr != null) {
				value = einheitspr.getCBez();
			}

			map.put(key, value);
		}

		myLogger.exit("Anzahl Einheiten: " + map.size());
		return map;
	}

	private void setAngeboteinheitFromAngeboteinheitDto(
			Angeboteinheit angeboteinheit, AngeboteinheitDto angeboteinheitDto) {
		angeboteinheit.setISort(angeboteinheitDto.getISort());
		em.merge(angeboteinheit);
		em.flush();
	}

	private AngeboteinheitDto assembleAngeboteinheitDto(
			Angeboteinheit angeboteinheit) {
		return AngeboteinheitDtoAssembler.createDto(angeboteinheit);
	}

	private AngeboteinheitDto[] assembleAngeboteinheitDtos(
			Collection<?> angeboteinheits) {
		List<AngeboteinheitDto> list = new ArrayList<AngeboteinheitDto>();
		if (angeboteinheits != null) {
			Iterator<?> iterator = angeboteinheits.iterator();
			while (iterator.hasNext()) {
				Angeboteinheit angeboteinheit = (Angeboteinheit) iterator
						.next();
				list.add(assembleAngeboteinheitDto(angeboteinheit));
			}
		}
		AngeboteinheitDto[] returnArray = new AngeboteinheitDto[list.size()];
		return (AngeboteinheitDto[]) list.toArray(returnArray);
	}

	// Angeboterledigungsgrund
	// ---------------------------------------------------

	public String createAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngeboterledigungsgrundDto(angeboterledigungsgrundDtoI);
		myLogger.logData(angeboterledigungsgrundDtoI);

		try {
			Angeboterledigungsgrund angeboterledigungsgrund = new Angeboterledigungsgrund(
					angeboterledigungsgrundDtoI.getCNr(),
					angeboterledigungsgrundDtoI.getMandantCNr(),
					angeboterledigungsgrundDtoI.getISort());
			em.persist(angeboterledigungsgrund);
			em.flush();

			setAngeboterledigungsgrundFromAngeboterledigungsgrundDto(
					angeboterledigungsgrund, angeboterledigungsgrundDtoI);

			if (angeboterledigungsgrundDtoI.getAngeboterledigungsgrundsprDto() != null) {
				Angeboterledigungsgrundspr angeboterledigungsgrundspr = new Angeboterledigungsgrundspr(
						angeboterledigungsgrundDtoI
								.getAngeboterledigungsgrundsprDto()
								.getLocaleCNr(),
						angeboterledigungsgrundDtoI.getCNr(),
						angeboterledigungsgrundDtoI
								.getAngeboterledigungsgrundsprDto().getCBez());
				em.persist(angeboterledigungsgrundspr);
				em.flush();

				setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
						angeboterledigungsgrundspr,
						angeboterledigungsgrundDtoI
								.getAngeboterledigungsgrundsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return angeboterledigungsgrundDtoI.getCNr();
	}

	public void removeAngeboterledigungsgrund(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngeboterledigungsgrundCNr(cNrI);
		myLogger.logData(cNrI);
		// zuerst alle Sprs loeschen
		Query query = em
				.createNamedQuery("AngeboterledigungsgrundsprfindByAngeboterledigungsgrundCNr");
		query.setParameter(1, cNrI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Angeboterledigungsgrundspr toRemove = em.find(
					Angeboterledigungsgrundspr.class, it.next());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler beim loeschen der Angebotserledigungsgrundsprachen");
			}
			em.remove(toRemove);
			em.flush();
		}

		// jetzt den Angeboterlerledigungsgrund loeschen
		Angeboterledigungsgrund toRemove = em.find(
				Angeboterledigungsgrund.class, cNrI);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAngebotserledigungsgrund. Es gibt keinen Grund mit cnr "
							+ cNrI);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngeboterledigungsgrundDto(angeboterledigungsgrundDtoI);
		myLogger.logData(angeboterledigungsgrundDtoI);

		try {
			// erst den Angeboterledigungsgrund
			Angeboterledigungsgrund angeboterledigungsgrund = em.find(
					Angeboterledigungsgrund.class,
					angeboterledigungsgrundDtoI.getCNr());
			if (angeboterledigungsgrund == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"fehler bei updateAngebotserledigungsgrund. Es gibt keinen Grund mit der cnr "
								+ angeboterledigungsgrundDtoI.getCNr()
								+ "\nangeboterledigungsgrundDto.toString: "
								+ angeboterledigungsgrundDtoI.toString());
			}

			setAngeboterledigungsgrundFromAngeboterledigungsgrundDto(
					angeboterledigungsgrund, angeboterledigungsgrundDtoI);

			// jetzt die Spr
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto = angeboterledigungsgrundDtoI
					.getAngeboterledigungsgrundsprDto();

			if (angeboterledigungsgrundsprDto != null
					&& angeboterledigungsgrundsprDto
							.getAngeboterledigungsgrundCNr() != null) {
				AngeboterledigungsgrundsprPK angeboterledigungsgrundsprPK = new AngeboterledigungsgrundsprPK();
				angeboterledigungsgrundsprPK
						.setLocaleCNr(angeboterledigungsgrundsprDto
								.getLocaleCNr());
				angeboterledigungsgrundsprPK
						.setAngeboterledigungsgrundCNr(angeboterledigungsgrundDtoI
								.getCNr());

				Angeboterledigungsgrundspr angeboterledigungsgrundspr = em
						.find(Angeboterledigungsgrundspr.class,
								angeboterledigungsgrundsprPK);

				setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
						angeboterledigungsgrundspr,
						angeboterledigungsgrundsprDto);
			} else {
				Angeboterledigungsgrundspr angeboterledigungsgrundspr = new Angeboterledigungsgrundspr(
						angeboterledigungsgrundDtoI
								.getAngeboterledigungsgrundsprDto()
								.getLocaleCNr(),
						angeboterledigungsgrundDtoI.getCNr(),
						angeboterledigungsgrundDtoI
								.getAngeboterledigungsgrundsprDto().getCBez());
				em.persist(angeboterledigungsgrundspr);
				em.flush();

				setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
						angeboterledigungsgrundspr,
						angeboterledigungsgrundsprDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public AngeboterledigungsgrundDto angeboterledigungsgrundFindByPrimaryKey(
			String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		AngeboterledigungsgrundDto angeboterledigungsgrundDto = null;
		Angeboterledigungsgrund angeboterledigungsgrund = em.find(
				Angeboterledigungsgrund.class, cNrI);
		if (angeboterledigungsgrund == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AngebotserledigungsgrundFindByPrimaryKey. Es gibt keine Grund mit cnr "
							+ cNrI);
		}
		angeboterledigungsgrundDto = assembleAngeboterledigungsgrundDto(angeboterledigungsgrund);

		// jetzt die Spr
		AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto = null;

		try {
			AngeboterledigungsgrundsprPK angeboterledigungsgrundsprPK = new AngeboterledigungsgrundsprPK(
					cNrI, theClientDto.getLocUiAsString());
			Angeboterledigungsgrundspr angeboterledigungsgrundspr = em.find(
					Angeboterledigungsgrundspr.class,
					angeboterledigungsgrundsprPK);
			if (angeboterledigungsgrundspr != null) {
				angeboterledigungsgrundsprDto = assembleAngeboterledigungsgrundsprDto(angeboterledigungsgrundspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		angeboterledigungsgrundDto
				.setAngeboterledigungsgrundsprDto(angeboterledigungsgrundsprDto);
		return angeboterledigungsgrundDto;
	}

	private void checkAngeboterledigungsgrundDto(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI)
			throws EJBExceptionLP {
		if (angeboterledigungsgrundDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angeboterledigungsgrundDtoI == null"));
		}
	}

	private void checkAngeboterledigungsgrundCNr(
			String cNrAngeboterledigungsgrundI) throws EJBExceptionLP {
		if (cNrAngeboterledigungsgrundI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngeboterledigungsgrundI == null"));
		}
	}

	private void setAngeboterledigungsgrundFromAngeboterledigungsgrundDto(
			Angeboterledigungsgrund angeboterledigungsgrund,
			AngeboterledigungsgrundDto angeboterledigungsgrundDto) {
		angeboterledigungsgrund.setISort(angeboterledigungsgrundDto.getISort());
		em.merge(angeboterledigungsgrund);
		em.flush();
	}

	private AngeboterledigungsgrundDto assembleAngeboterledigungsgrundDto(
			Angeboterledigungsgrund angeboterledigungsgrund) {
		return AngeboterledigungsgrundDtoAssembler
				.createDto(angeboterledigungsgrund);
	}

	private AngeboterledigungsgrundDto[] assembleAngeboterledigungsgrundDtos(
			Collection<?> angeboterledigungsgrunds) {
		List<AngeboterledigungsgrundDto> list = new ArrayList<AngeboterledigungsgrundDto>();
		if (angeboterledigungsgrunds != null) {
			Iterator<?> iterator = angeboterledigungsgrunds.iterator();
			while (iterator.hasNext()) {
				Angeboterledigungsgrund angeboterledigungsgrund = (Angeboterledigungsgrund) iterator
						.next();
				list.add(assembleAngeboterledigungsgrundDto(angeboterledigungsgrund));
			}
		}
		AngeboterledigungsgrundDto[] returnArray = new AngeboterledigungsgrundDto[list
				.size()];
		return (AngeboterledigungsgrundDto[]) list.toArray(returnArray);
	}

	public void createAngeboterledigungsgrundspr(
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto)
			throws EJBExceptionLP {
		if (angeboterledigungsgrundsprDto == null) {
			return;
		}
		try {
			Angeboterledigungsgrundspr angeboterledigungsgrundspr = new Angeboterledigungsgrundspr(
					angeboterledigungsgrundsprDto.getLocaleCNr(),
					angeboterledigungsgrundsprDto
							.getAngeboterledigungsgrundCNr(),
					angeboterledigungsgrundsprDto.getCBez());
			em.persist(angeboterledigungsgrundspr);
			em.flush();
			setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
					angeboterledigungsgrundspr, angeboterledigungsgrundsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeAngeboterledigungsgrundspr(String localeCNr,
			String angbeoterledigungsgrundCNr) throws EJBExceptionLP {
	}

	public void updateAngeboterledigungsgrundspr(
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto)
			throws EJBExceptionLP {
		if (angeboterledigungsgrundsprDto != null) {
			AngeboterledigungsgrundsprPK angeboterledigungsgrundsprPK = new AngeboterledigungsgrundsprPK();
			angeboterledigungsgrundsprPK
					.setLocaleCNr(angeboterledigungsgrundsprDto.getLocaleCNr());
			angeboterledigungsgrundsprPK
					.setAngeboterledigungsgrundCNr(angeboterledigungsgrundsprDto
							.getAngeboterledigungsgrundCNr());
			Angeboterledigungsgrundspr angeboterledigungsgrundspr = em.find(
					Angeboterledigungsgrundspr.class,
					angeboterledigungsgrundsprPK);
			if (angeboterledigungsgrundspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAngebotserledigungsgrundspr. Es gibt keine Spr fuer Grund "
								+ angeboterledigungsgrundsprDto
										.getAngeboterledigungsgrundCNr()
								+ "fuer die Locale "
								+ angeboterledigungsgrundsprDto.getLocaleCNr()
								+ "\nangeboterledigungsgrundsprDto.toString(): "
								+ angeboterledigungsgrundsprDto.toString());
			}
			setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
					angeboterledigungsgrundspr, angeboterledigungsgrundsprDto);
		}
	}

	public AngeboterledigungsgrundsprDto angeboterledigungsgrundsprFindByPrimaryKey(
			String localeCNr, String angbeoterledigungsgrundCNr)
			throws EJBExceptionLP {
		AngeboterledigungsgrundsprPK angeboterledigungsgrundsprPK = new AngeboterledigungsgrundsprPK();
		angeboterledigungsgrundsprPK.setLocaleCNr(localeCNr);
		angeboterledigungsgrundsprPK
				.setAngeboterledigungsgrundCNr(angbeoterledigungsgrundCNr);
		Angeboterledigungsgrundspr angeboterledigungsgrundspr = em.find(
				Angeboterledigungsgrundspr.class, angeboterledigungsgrundsprPK);
		if (angeboterledigungsgrundspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei angebotserledigungsgrundFindByPrimaryKey. Es gibt keinen Grund "
							+ angbeoterledigungsgrundCNr + "fuer die Locale "
							+ localeCNr);
		}
		return assembleAngeboterledigungsgrundsprDto(angeboterledigungsgrundspr);
	}

	private void setAngeboterledigungsgrundsprFromAngeboterledigungsgrundsprDto(
			Angeboterledigungsgrundspr angeboterledigungsgrundspr,
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto) {
		angeboterledigungsgrundspr.setCBez(angeboterledigungsgrundsprDto
				.getCBez());
		em.merge(angeboterledigungsgrundspr);
		em.flush();
	}

	private AngeboterledigungsgrundsprDto assembleAngeboterledigungsgrundsprDto(
			Angeboterledigungsgrundspr angeboterledigungsgrundspr) {
		return AngeboterledigungsgrundsprDtoAssembler
				.createDto(angeboterledigungsgrundspr);
	}

	private AngeboterledigungsgrundsprDto[] assembleAngeboterledigungsgrundsprDtos(
			Collection<?> angeboterledigungsgrundsprs) {
		List<AngeboterledigungsgrundsprDto> list = new ArrayList<AngeboterledigungsgrundsprDto>();
		if (angeboterledigungsgrundsprs != null) {
			Iterator<?> iterator = angeboterledigungsgrundsprs.iterator();
			while (iterator.hasNext()) {
				Angeboterledigungsgrundspr angeboterledigungsgrundspr = (Angeboterledigungsgrundspr) iterator
						.next();
				list.add(assembleAngeboterledigungsgrundsprDto(angeboterledigungsgrundspr));
			}
		}
		AngeboterledigungsgrundsprDto[] returnArray = new AngeboterledigungsgrundsprDto[list
				.size()];
		return (AngeboterledigungsgrundsprDto[]) list.toArray(returnArray);
	}

	// Angebotpositionart
	// --------------------------------------------------------

	public String createAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionartDto(angebotpositionartDtoI);
		try {
			Angebotpositionart angebotpositionart = new Angebotpositionart(
					angebotpositionartDtoI.getCNr(),
					angebotpositionartDtoI.getISort(),
					angebotpositionartDtoI.getBVersteckt());
			em.persist(angebotpositionart);
			em.flush();

			setAngebotpositionartFromAngebotpositionartDto(angebotpositionart,
					angebotpositionartDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return angebotpositionartDtoI.getCNr();
	}

	public void removeAngebotpositionart(String cNrAngebotpositionartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionartCNr(cNrAngebotpositionartI);
		Angebotpositionart toRemove = em.find(Angebotpositionart.class,
				cNrAngebotpositionartI);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAngebotpositionsart. Es gibt keine Positionsart mit der cnr "
							+ cNrAngebotpositionartI);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionartDto(angebotpositionartDtoI);
		Angebotpositionart angebotpositionart = em.find(
				Angebotpositionart.class, angebotpositionartDtoI.getCNr());
		if (angebotpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateAngebotspositionart. Es gibt keine Positionsart mit der cnr "
							+ angebotpositionartDtoI.getCNr()
							+ "\nangebotpositionartDtoI.toString(): "
							+ angebotpositionartDtoI.toString());
		}

		setAngebotpositionartFromAngebotpositionartDto(angebotpositionart,
				angebotpositionartDtoI);
	}

	public Map<String, String> getAngebotpositionart(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLocale(locale1I);
		checkLocale(locale2I);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		try {
			Query query = em
					.createNamedQuery("AngebotpositionartfindAllEnable");
			Collection<?> cl = query.getResultList();

			AngebotpositionartDto[] aAngebotpositionartDto = assembleAngebotpositionartDtos(cl);

			for (int i = 0; i < aAngebotpositionartDto.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(
								aAngebotpositionartDto[i].getPositionsartCNr(),
								locale1I, locale2I);
				map.put(aAngebotpositionartDto[i].getPositionsartCNr(),
						sUebersetzung);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return map;
	}

	public AngebotpositionartDto angebotpositionartFindByPrimaryKey(
			String cNrAngebotpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AngebotpositionartDto angebotpositionartDto = null;
		Angebotpositionart angebotpositionart = em.find(
				Angebotpositionart.class, cNrAngebotpositionartI);
		if (angebotpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei AngebotspositionartFindByPrimaryKey. Es gibt keine Positionsart mit der cnr "
							+ cNrAngebotpositionartI);
		}
		angebotpositionartDto = assembleAngebotpositionartDto(angebotpositionart);
		return angebotpositionartDto;
	}

	private void checkAngebotpositionartDto(
			AngebotpositionartDto angebotpositionartDtoI) throws EJBExceptionLP {
		if (angebotpositionartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angebotpositionartDtoI == null"));
		}

		myLogger.info("AngebotpositionartDtoI: "
				+ angebotpositionartDtoI.toString());
	}

	private void checkAngebotpositionartCNr(String cNrAngebotpositionartI)
			throws EJBExceptionLP {
		if (cNrAngebotpositionartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngebotpositionartI == null"));
		}

		myLogger.info("AngebotpositionartCNr: " + cNrAngebotpositionartI);
	}

	private void setAngebotpositionartFromAngebotpositionartDto(
			Angebotpositionart angebotpositionart,
			AngebotpositionartDto angebotpositionartDto) {
		angebotpositionart.setISort(angebotpositionartDto.getISort());
		angebotpositionart.setBVersteckt(angebotpositionartDto.getBVersteckt());
		em.merge(angebotpositionart);
		em.flush();
	}

	private AngebotpositionartDto assembleAngebotpositionartDto(
			Angebotpositionart angebotpositionart) {
		return AngebotpositionartDtoAssembler.createDto(angebotpositionart);
	}

	private AngebotpositionartDto[] assembleAngebotpositionartDtos(
			Collection<?> angebotpositionarts) {
		List<AngebotpositionartDto> list = new ArrayList<AngebotpositionartDto>();
		if (angebotpositionarts != null) {
			Iterator<?> iterator = angebotpositionarts.iterator();
			while (iterator.hasNext()) {
				Angebotpositionart angebotpositionart = (Angebotpositionart) iterator
						.next();
				list.add(assembleAngebotpositionartDto(angebotpositionart));
			}
		}
		AngebotpositionartDto[] returnArray = new AngebotpositionartDto[list
				.size()];
		return (AngebotpositionartDto[]) list.toArray(returnArray);
	}

	public void checkAngebotIId(Integer iIdAngebotI) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}

		myLogger.info("AngebotIId: " + iIdAngebotI.toString());
	}

	/**
	 * Die in Stunden hinterlegte Lieferzeit in die gewuenschte Angebotseinheit
	 * umrechnen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param cNrAngeboteinheitI
	 *            die gewuenschte Einheit
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer die Lieferzeit in der gewuenschten Einheit
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer getLieferzeitInAngeboteinheit(Integer iIdAngebotI,
			String cNrAngeboteinheitI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);

		if (cNrAngeboteinheitI == null
				|| (!cNrAngeboteinheitI
						.equals(AngebotServiceFac.ANGEBOTEINHEIT_TAG)
						&& !cNrAngeboteinheitI
								.equals(AngebotServiceFac.ANGEBOTEINHEIT_STUNDE) && !cNrAngeboteinheitI
						.equals(AngebotServiceFac.ANGEBOTEINHEIT_WOCHE))) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngeboteinheitI ungueltig"));
		}

		Integer iiLieferzeit = null;

		try {
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);

			// die Lieferzeit in Stunden
			double dLieferzeit = angebotDto.getILieferzeitinstunden()
					.doubleValue();

			if (cNrAngeboteinheitI.equals(AngebotServiceFac.ANGEBOTEINHEIT_TAG)) {
				dLieferzeit /= 24;
			} else if (cNrAngeboteinheitI
					.equals(AngebotServiceFac.ANGEBOTEINHEIT_WOCHE)) {
				dLieferzeit /= 24 * 7;
			}

			iiLieferzeit = new Integer(new Double(dLieferzeit).intValue());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iiLieferzeit;
	}
}
