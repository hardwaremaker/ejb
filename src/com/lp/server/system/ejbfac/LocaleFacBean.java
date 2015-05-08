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
package com.lp.server.system.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.finanz.ejbfac.FinanzServiceFacBean;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.ejb.Belegart;
import com.lp.server.system.ejb.Belegartspr;
import com.lp.server.system.ejb.BelegartsprPK;
import com.lp.server.system.ejb.Funktion;
import com.lp.server.system.ejb.Funktionspr;
import com.lp.server.system.ejb.FunktionsprPK;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.LieferartQuery;
import com.lp.server.system.ejb.Lieferartspr;
import com.lp.server.system.ejb.LieferartsprPK;
import com.lp.server.system.ejb.LocaleLP;
import com.lp.server.system.ejb.Positionsart;
import com.lp.server.system.ejb.Positionsartspr;
import com.lp.server.system.ejb.PositionsartsprPK;
import com.lp.server.system.ejb.Status;
import com.lp.server.system.ejb.Statusspr;
import com.lp.server.system.ejb.StatussprPK;
import com.lp.server.system.ejb.Waehrung;
import com.lp.server.system.ejb.Wechselkurs;
import com.lp.server.system.ejb.WechselkursPK;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.BelegartDtoAssembler;
import com.lp.server.system.service.BelegartsprDto;
import com.lp.server.system.service.BelegartsprDtoAssembler;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.FunktionDtoAssembler;
import com.lp.server.system.service.FunktionsprDto;
import com.lp.server.system.service.FunktionsprDtoAssembler;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LieferartDtoAssembler;
import com.lp.server.system.service.LieferartsprDto;
import com.lp.server.system.service.LieferartsprDtoAssembler;
import com.lp.server.system.service.LocaleDto;
import com.lp.server.system.service.LocaleDtoAssembler;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.PositionsartDto;
import com.lp.server.system.service.PositionsartDtoAssembler;
import com.lp.server.system.service.PositionsartsprDto;
import com.lp.server.system.service.PositionsartsprDtoAssembler;
import com.lp.server.system.service.StatusDto;
import com.lp.server.system.service.StatusDtoAssembler;
import com.lp.server.system.service.Statusspr2DtoAssembler;
import com.lp.server.system.service.StatussprDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WaehrungDtoAssembler;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.WechselkursDtoAssembler;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LocaleFacBean extends Facade implements LocaleFac {

	@PersistenceContext
	private EntityManager em;

	// auslief: 3
	static public final Locale T_LOC_KONZERN = new Locale("de", "AT");
	static public final Locale T_LOC_MANDANT = new Locale("de", "AT");
	static public final Locale T_LOC_UI = new Locale("de", "AT");

	//***Locale*****************************************************************
	// **
	public String createLocale(LocaleDto localeDto) throws EJBExceptionLP {
		if (localeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("localeDto == null"));
		}
		if (localeDto.getBAktiv() == null) {
			localeDto.setBAktiv(Helper.boolean2Short(true));
		}
		String cNrWieKey = null;
		try {
			LocaleLP locale = new LocaleLP(localeDto.getCNr(), localeDto
					.getBAktiv());
			em.persist(locale);
			em.flush();
			setLpLocaleFromLocaleDto(locale, localeDto);
			LocaleLP temp = em.find(LocaleLP.class, localeDto.getCNr());
			if (temp == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			cNrWieKey = temp.getCNr(); // Wegen Blanks rechts!}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return cNrWieKey; // Wegen Blanks rechts!
	}

	public void removeLocale(String cNr) throws EJBExceptionLP {
		Locale toRemove = em.find(Locale.class, cNr);
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

	public void removeLocale(LocaleDto localeDto) throws EJBExceptionLP {
		if (localeDto != null) {
			String cNr = localeDto.getCNr();
			removeLocale(cNr);
		}
	}

	public void updateLocale(LocaleDto localeDto) throws EJBExceptionLP {

		if (localeDto != null) {
			String cNr = localeDto.getCNr();
			// try {
			LocaleLP localeLP = em.find(LocaleLP.class, cNr);
			if (localeLP == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLpLocaleFromLocaleDto(localeLP, localeDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public LocaleDto localeFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		LocaleLP localeLP = em.find(LocaleLP.class, cNr);
		if (localeLP == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLpLocaleDto(localeLP);

	}

	public LocaleDto localeFindByPrimaryKeyOhneExc(String cNr)
			throws EJBExceptionLP {
		// try {
		LocaleLP localeLP = em.find(LocaleLP.class, cNr);
		if (localeLP == null) {
			return null;
		}
		return assembleLpLocaleDto(localeLP);
	}

	private void setLpLocaleFromLocaleDto(LocaleLP localeLP, LocaleDto localeDto) {
		localeLP.setBAktiv(localeDto.getBAktiv());
		em.merge(localeLP);
		em.flush();
	}

	private LocaleDto assembleLpLocaleDto(LocaleLP localeLP) {
		return LocaleDtoAssembler.createDto(localeLP);
	}

	/**
	 * Hole alle Locales und zeige sie in der Sprache der inLocale an. Wenn
	 * inLocale null ist wird default locale der Java VM genommen.
	 * 
	 * @param inLocale
	 *            Locale in der die Locales angezeigt werden sollen.
	 * @return Map
	 * @throws FinderException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> getAllLocales(Locale inLocale) {
		Query query = em.createNamedQuery("LocaleLPfindAll");
		Collection<?> c = query.getResultList();
		TreeMap t = new TreeMap();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			LocaleLP localeLP = ((LocaleLP) iter.next());
			String itemLocaleAsString = localeLP.getCNr();
			try {
				String sDisplayLanguage = null;
				String sDisplayCountry = null;
				Locale locale = Helper.string2Locale(itemLocaleAsString);
				if (inLocale == null) {
					sDisplayLanguage = locale.getDisplayLanguage();
					sDisplayCountry = locale.getDisplayCountry();
				} else {
					sDisplayLanguage = locale.getDisplayLanguage(inLocale);
					sDisplayCountry = locale.getDisplayCountry(inLocale);
				}

				String zusatz = "";
				if (itemLocaleAsString.length() > 4) {
					zusatz = " " + itemLocaleAsString.substring(4);
				}

				if (Helper.short2boolean(localeLP.getBAktiv())) {
					t.put(itemLocaleAsString, sDisplayLanguage + " "
							+ sDisplayCountry + zusatz);
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
		}
		return t;
	}

	/**
	 * Lieferart *************************************************************
	 * 
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllLieferarten() throws EJBExceptionLP {

		Map<Integer, LieferartDto> alleLieferarten = new TreeMap<Integer, LieferartDto>();
		// try {
		Query query = em.createNamedQuery("LieferartfindAll");
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		LieferartDto[] la = assembleLieferartDtos(c);
		for (int i = 0; i < la.length; i++) {
			alleLieferarten.put(la[i].getIId(), la[i]);
		}
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
		// t);
		// }
		return alleLieferarten;
	}

	/**
	 * Liefert Map mit allen Lieferarten und deren Code in der jeweiligen
	 * Landessprache
	 * 
	 * @param loSuchErstHierI
	 *            Stati
	 * @param loDannHierI
	 *            Locale
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map getAllSprLieferarten(Locale loSuchErstHierI, Locale loDannHierI)
			throws EJBExceptionLP {

		Map<Integer, String> alleLieferarten = null;
		Collection<?> lieferartenColl = null;
		Query query = em.createNamedQuery("LieferartfindAll");

		// try {
		lieferartenColl = query.getResultList();
		// if (lieferartenColl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		// }
		// catch (FinderException t) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
		// t);
		// }

		alleLieferarten = uebersetzeLieferartOptimal(
				assembleLieferartDtos(lieferartenColl), loSuchErstHierI,
				loDannHierI);

		return alleLieferarten;
	}

	/**
	 * Hole die Uebersetzungen f&uuml;r ein Array von Lieferarten
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return Map
	 */
	private Map<Integer, String> uebersetzeLieferartOptimal(
			LieferartDto[] pArray, Locale locale1, Locale locale2) {
		Map<Integer, String> uebersetzung = new TreeMap<Integer, String>();
		for (int i = 0; i < pArray.length; i++) {
			Integer key = pArray[i].getIId();
			String value = uebersetzeLieferartOptimal(pArray[i].getIId(),
					pArray[i].getCNr(), locale1, locale2);
			uebersetzung.put(key, value);
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Lieferart optimal 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param iID
	 *            Integer
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	private String uebersetzeLieferartOptimal(Integer iID, String cNr,
			Locale locale1, Locale locale2) {
		String uebersetzung;
		uebersetzung = uebersetzeLieferart(iID, locale1);
		if (uebersetzung == null) {
			uebersetzung = uebersetzeLieferart(iID, locale2);
		}
		if (uebersetzung == null) {
			uebersetzung = cNr;
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Lieferart in die Sprache des uebergebenen Locales
	 * 
	 * @param iID
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeLieferart(Integer iID, Locale locale) {
		String cLocale = null;

		cLocale = Helper.locale2String(locale);
		Lieferartspr lieferartspr = em.find(Lieferartspr.class,
				new LieferartsprPK(iID, cLocale));
		if (lieferartspr == null) {
			return null;
		}
		return lieferartspr.getCBezeichnung();
	}

	/**
	 * Im UI und auf Drucken soll die Lieferart in einem bestimmten Locale bzw.
	 * der bestmoeglichen Uebersetzung angezeigt werden.
	 * 
	 * @param iIdLieferartI
	 *            PK der Lieferart
	 * @param localeI
	 *            das gewuenschte Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die bestmoegliche Uebersetzung, null ist moeglich
	 */
	public String lieferartFindByIIdLocaleOhneExc(Integer iIdLieferartI,
			Locale localeI, TheClientDto theClientDto) {
		String cLieferart = null;

		if (iIdLieferartI != null) {
			// Schritt 1: Uebersetzung in gewuenschtes Locale
			LieferartsprDto lieferartsprDto = lieferartsprFindByPrimaryKeyOhneExc(
					iIdLieferartI, Helper.locale2String(localeI), theClientDto);

			if (lieferartsprDto != null
					&& lieferartsprDto.getCBezeichnung() != null) {
				cLieferart = lieferartsprDto.getCBezeichnung();
			} else {
				// Schritt 2: Uebersetzung in die UI Sprache des Benutzers
				lieferartsprDto = lieferartsprFindByPrimaryKeyOhneExc(
						iIdLieferartI, theClientDto.getLocUiAsString(),
						theClientDto);

				if (lieferartsprDto != null
						&& lieferartsprDto.getCBezeichnung() != null) {
					cLieferart = lieferartsprDto.getCBezeichnung();
				} else {
					// Schritt 3: Uebersetzung in Konzerndatensprache
					lieferartsprDto = lieferartsprFindByPrimaryKeyOhneExc(
							iIdLieferartI,
							theClientDto.getLocKonzernAsString(), theClientDto);

					if (lieferartsprDto != null
							&& lieferartsprDto.getCBezeichnung() != null) {
						cLieferart = lieferartsprDto.getCBezeichnung();
					} else {
						// Schritt 4: Die cNr der Lieferart
						LieferartDto lieferartDto = lieferartFindByPrimaryKey(
								iIdLieferartI, theClientDto);

						cLieferart = lieferartDto.getCNr();
					}
				}
			}
		}

		return cLieferart;
	}

	public void createWaehrung(WaehrungDto waehrungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(waehrungDto);

		// hier wird erwartet, dass die personalId's schon dran sind, sonst
		// funktioniert der aufbautest nicht
		if (waehrungDto.getPersonalIIdAendern() == null
				|| waehrungDto.getPersonalIIdAnlegen() == null) {
			waehrungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			waehrungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		}
		waehrungDto.setPersonalIIdAendern(waehrungDto.getPersonalIIdAendern());
		waehrungDto.setPersonalIIdAnlegen(waehrungDto.getPersonalIIdAnlegen());

		try {
			Waehrung waehrung = new Waehrung(waehrungDto.getCNr(), waehrungDto
					.getPersonalIIdAendern(), waehrungDto
					.getPersonalIIdAnlegen());
			em.persist(waehrung);
			em.flush();
			waehrungDto.setTAendern(waehrung.getTAendern());
			waehrungDto.setTAnlegen(waehrung.getTAnlegen());
			setWaehrungFromWaehrungDto(waehrung, waehrungDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeWaehrung(WaehrungDto waehrungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(waehrungDto);
		if (waehrungDto != null) {
			Waehrung toRemove = em.find(Waehrung.class, waehrungDto.getCNr());
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

	public void createUpdateWaehrung(WaehrungDto waehrungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (waehrungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("waehrungDto == null"));
		}

		myLogger.logData(waehrungDto);

		waehrungDto.setTAendern(getTimestamp());

		Waehrung waehrung = null;
		// try {
		waehrung = em.find(Waehrung.class, waehrungDto.getCNr());
		if (waehrung == null) {
			createWaehrung(waehrungDto, theClientDto);
			waehrung = em.find(Waehrung.class, waehrungDto.getCNr());
		}
		// personal Id und Aenderungsdatum
		waehrungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		waehrungDto.setTAendern(getTimestamp());
		setWaehrungFromWaehrungDto(waehrung, waehrungDto);
		// }
		// catch (FinderException ex) {
		// createWaehrung(waehrungDto, idUser);
		// }

	}

	private void setWaehrungFromWaehrungDto(Waehrung waehrung,
			WaehrungDto waehrungDto) {
		waehrung.setCKommentar(waehrungDto.getCKommentar());
		waehrung.setPersonalIIdAnlegen(waehrungDto.getPersonalIIdAnlegen());
		waehrung.setTAnlegen(waehrungDto.getTAnlegen());
		waehrung.setPersonalIIdAendern(waehrungDto.getPersonalIIdAendern());
		waehrung.setTAendern(waehrungDto.getTAendern());
		em.merge(waehrung);
		em.flush();
	}

	private WaehrungDto assembleWaehrungDto(Waehrung waehrung) {
		return WaehrungDtoAssembler.createDto(waehrung);
	}

	private WaehrungDto[] assembleWaehrungDtos(Collection<?> waehrungs) {
		List<WaehrungDto> list = new ArrayList<WaehrungDto>();
		if (waehrungs != null) {
			Iterator<?> iterator = waehrungs.iterator();
			while (iterator.hasNext()) {
				Waehrung waehrung = (Waehrung) iterator.next();
				list.add(assembleWaehrungDto(waehrung));
			}
		}
		WaehrungDto[] returnArray = new WaehrungDto[list.size()];
		return (WaehrungDto[]) list.toArray(returnArray);
	}

	/**
	 * getAllWaehrungen.
	 * 
	 * @return Map
	 */
	public Map getAllWaehrungen() {
		WaehrungDto[] waehrungDto = waehrungFindAll();
		TreeMap<String, String> tmWaehrungen = new TreeMap<String, String>();
		for (int i = 0; i < waehrungDto.length; i++) {
			tmWaehrungen.put(waehrungDto[i].getCNr(), waehrungDto[i].getCNr());
		}
		return tmWaehrungen;
	}

	//***Wechselkurs************************************************************
	// **
	public void createWechselkurs(WechselkursDto wechselkursDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(wechselkursDto);
		wechselkursDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		wechselkursDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		// pruefen, ob der nicht schon "vertauscht" eingegeben wurde

		// Die Vertauschtpruefung findet nicht mehr statt, ghp, 2012-07-12
		// Hintergrund: Ein anderer Mandant kann genau die umgekehrte Waehrungen
		// haben (CHF -> EUR, EUR -> CHF)
		//
		// WechselkursDto wechselkursDtoVertauscht =
		// wechselkursFindByPrimaryKeyOhneExc(
		// wechselkursDto.getWaehrungCNrZu(),
		// wechselkursDto.getWaehrungCNrVon(), wechselkursDto.getTDatum());
		// if (wechselkursDtoVertauscht != null) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, new Exception(
		// "Dieser kurs ist bereits hinterlegt: "
		// + wechselkursDto.toString()));
		// }
		try {
			Wechselkurs wechselkurs = new Wechselkurs(wechselkursDto
					.getWaehrungCNrVon(), wechselkursDto.getWaehrungCNrZu(),
					wechselkursDto.getTDatum(), wechselkursDto.getNKurs(),
					wechselkursDto.getPersonalIIdAnlegen(), wechselkursDto
							.getPersonalIIdAendern());
			em.persist(wechselkurs);
			em.flush();

			// jetzt die ts holen und setzen wegen update
			wechselkursDto.setTAendern(wechselkurs.getTAendern());
			wechselkursDto.setTAnlegen(wechselkurs.getTAnlegen());

			setWechselkursFromWechselkursDto(wechselkurs, wechselkursDto);
			myLogger.logKritisch("CreateWechselkurs: "
					+ wechselkursDto.toString());
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeWechselkurs(WechselkursDto wechselkursDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(wechselkursDto);
		if (wechselkursDto != null) {
			myLogger.logKritisch("RemoveWechselkurs: "
					+ wechselkursDto.toString());
			String waehrungLocaleCNrVon = wechselkursDto.getWaehrungCNrVon();
			String waehrungLocaleCNrZu = wechselkursDto.getWaehrungCNrZu();
			java.sql.Date tDatum = wechselkursDto.getTDatum();
			Wechselkurs toRemove = em.find(Wechselkurs.class,
					new WechselkursPK(waehrungLocaleCNrVon,
							waehrungLocaleCNrZu, tDatum));
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

	public void updateWechselkurs(WechselkursDto wechselkursDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(wechselkursDto);
		wechselkursDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		wechselkursDto.setTDatum(Helper.cutDate(wechselkursDto.getTDatum()));
		wechselkursDto.setTAendern(getTimestamp());
		if (wechselkursDto != null) {
			WechselkursPK wechselkursPK = new WechselkursPK();
			wechselkursPK.setWaehrungCNrVon(wechselkursDto.getWaehrungCNrVon());
			wechselkursPK.setWaehrungCNrZu(wechselkursDto.getWaehrungCNrZu());
			wechselkursPK.setTDatum(wechselkursDto.getTDatum());

			Wechselkurs wechselkurs = em.find(Wechselkurs.class, wechselkursPK);
			if (wechselkurs == null) {
				createWechselkurs(wechselkursDto, theClientDto);

				myLogger.logKritisch("CreateWechselkurs: "
						+ wechselkursDto.toString());
			} else {
				// schaut boese aus, muss aber sein
				// passiert dann wenn am client einer "neu" drueckt, aber
				// eigentlich
				// einen bestehenden datensatz updatet
				if (wechselkursDto.getPersonalIIdAnlegen() == null) {
					wechselkursDto.setPersonalIIdAnlegen(wechselkurs
							.getPersonalIIdAnlegen());
					wechselkursDto.setTAnlegen(wechselkurs.getTAnlegen());
				}
				setWechselkursFromWechselkursDto(wechselkurs, wechselkursDto);

				myLogger.logKritisch("UpdateWechselkurs: von "
						+ wechselkurs.getNKurs() + " nach "
						+ wechselkursDto.toString());

			}

		}
	}

	public WechselkursDto wechselkursFindByPrimaryKey(String waehrungCNrVon,
			String waehrungCNrZu, java.util.Date tDatum) throws EJBExceptionLP {
		// try {
		WechselkursPK wechselkursPK = new WechselkursPK();
		wechselkursPK.setWaehrungCNrVon(waehrungCNrVon);
		wechselkursPK.setWaehrungCNrZu(waehrungCNrZu);
		wechselkursPK.setTDatum(new java.sql.Date(tDatum.getTime()));
		Wechselkurs wechselkurs = em.find(Wechselkurs.class, wechselkursPK);
		if (wechselkurs == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleWechselkursDto(wechselkurs);
	}

	public WechselkursDto wechselkursFindByPrimaryKeyOhneExc(
			String waehrungCNrVon, String waehrungCNrZu, java.util.Date tDatum)
			throws EJBExceptionLP {
		// try {
		WechselkursPK wechselkursPK = new WechselkursPK();
		wechselkursPK.setWaehrungCNrVon(waehrungCNrVon);
		wechselkursPK.setWaehrungCNrZu(waehrungCNrZu);
		wechselkursPK.setTDatum(new java.sql.Date(tDatum.getTime()));
		Wechselkurs wechselkurs = em.find(Wechselkurs.class, wechselkursPK);
		if (wechselkurs == null) {
			return null;
		}
		return assembleWechselkursDto(wechselkurs);

	}

	/**
	 * exists: 0 Diese Methode kann innerhalb einer geschachtelten Transaktion
	 * aufgerufen werden, wenn das Nichtvorhandensein eines Wechselkurses keine
	 * Laufzeit-Exception zur Folge haben darf.
	 * 
	 * @param sWaehrungVonI
	 *            von dieser Waehrung
	 * @param sWaehrungZuI
	 *            in diese Waehrung
	 * @return boolean true, wenn der Wechselkurs existiert
	 */
	public boolean wechselkursExists(String sWaehrungVonI, String sWaehrungZuI) {
		boolean exists = true;
		// try {
		WechselkursPK wechselkursPK = new WechselkursPK();
		wechselkursPK.setWaehrungCNrVon(sWaehrungVonI);
		wechselkursPK.setWaehrungCNrZu(sWaehrungZuI);
		Wechselkurs wechselkurs = em.find(Wechselkurs.class, wechselkursPK);

		return wechselkurs != null;

		// if (wechselkurs == null) { // @ToDo null Pruefung?
		// // diese Exception darf nicht geworfen werden
		// exists = false;
		// }
		// }
		// catch (FinderException fex) {
		// diese Exception darf nicht geworfen werden
		// exists = false;
		// }
		// return exists;
	}

	private void setWechselkursFromWechselkursDto(Wechselkurs wechselkurs,
			WechselkursDto wechselkursDto) {
		// Kurs <=0 ist nciht zulaessig
		if (wechselkursDto.getNKurs().compareTo(new BigDecimal(0)) >= 0) {
			wechselkurs.setNKurs(wechselkursDto.getNKurs());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"Kurs <=0 ist nicht zulaessig"));
		}
		wechselkurs.setTAnlegen(wechselkursDto.getTAnlegen());
		wechselkurs.setPersonalIIdAnlegen(wechselkursDto
				.getPersonalIIdAnlegen());
		wechselkurs.setTAendern(wechselkursDto.getTAendern());
		wechselkurs.setPersonalIIdAendern(wechselkursDto
				.getPersonalIIdAendern());
		em.merge(wechselkurs);
		em.flush();
	}

	private WechselkursDto assembleWechselkursDto(Wechselkurs wechselkurs) {
		return WechselkursDtoAssembler.createDto(wechselkurs);
	}

	private WechselkursDto[] assembleWechselkursDtos(Collection<?> wechselkurss) {
		List<WechselkursDto> list = new ArrayList<WechselkursDto>();
		if (wechselkurss != null) {
			Iterator<?> iterator = wechselkurss.iterator();
			while (iterator.hasNext()) {
				Wechselkurs wechselkurs = (Wechselkurs) iterator.next();
				list.add(assembleWechselkursDto(wechselkurs));
			}
		}
		WechselkursDto[] returnArray = new WechselkursDto[list.size()];
		return (WechselkursDto[]) list.toArray(returnArray);
	}

	public WaehrungDto[] waehrungFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("WaehrungfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleWaehrungDtos(cl);
		// }
		// catch (FinderException e) {}
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	public WaehrungDto waehrungFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		// try {
		Waehrung waehrung = em.find(Waehrung.class, cNr);
		if (waehrung == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return WaehrungDtoAssembler.createDto(waehrung);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	/**
	 * Einen Wechselkurs zwischen zwei Waehrungen bestimmen. <br>
	 * Der Wechselkurs ist kalenderfaehig.
	 * 
	 * @param waehrungCNrVonI
	 *            umgerechnet wird aus dieser Waehrung
	 * @param waehrungCNrNachI
	 *            umgerechnet wird in diese Waehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Float der aktuelle Wechselkurs, wenn kein gueltiger vorhanden ist
	 *         null
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal getWechselkurs2(String waehrungCNrVonI,
			String waehrungCNrNachI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		myLogger.info("Von: " + waehrungCNrVonI + " nach: " + waehrungCNrNachI);

		BigDecimal ffWechselkursO = null;

		if (waehrungCNrVonI.equals(waehrungCNrNachI)) {
			ffWechselkursO = BigDecimal.ONE
					.setScale(ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		} else {
			// jetzt nach waehrung von -> waehrung zu suchen
			WechselkursDto wechselkursDto = getLetztenWechselkurs(
					waehrungCNrVonI, waehrungCNrNachI, theClientDto);
			if (wechselkursDto != null) {
				ffWechselkursO = wechselkursDto.getNKurs();
			}
		}

		return ffWechselkursO;
	}

	public WechselkursDto getKursZuDatum(String waehrungCNrVonI,
			String waehrungCNrNachI, java.sql.Date dDatum,
			TheClientDto theClientDto) {
		WechselkursDto kurs = null;

		if (waehrungCNrVonI.equals(waehrungCNrNachI)) {
			kurs = new WechselkursDto();
			kurs.setWaehrungCNrVon(waehrungCNrVonI);
			kurs.setWaehrungCNrZu(waehrungCNrNachI);
			kurs.setNKurs(BigDecimal.ONE
					.setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS));
			return kurs;
		}

		String mandantenWaehrung = theClientDto.getSMandantenwaehrung();
		if (!(mandantenWaehrung.equals(waehrungCNrVonI) || mandantenWaehrung
				.equals(waehrungCNrNachI))) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_IM_WECHSELKURS_KEINE_MANDANTENWAEHRUNG_ENTHALTEN,
					new Exception("Weder '" + waehrungCNrVonI + "' noch '"
							+ waehrungCNrNachI + "'"
							+ "enthalten die Mandantenwaehrung '"
							+ mandantenWaehrung + "'."));
		}

		WechselkursDto[] kurse = getAlleWechselkurse(mandantenWaehrung,
				waehrungCNrNachI);
		if (kurse.length > 0) {
			for (int i = kurse.length - 1; i >= 0; i--) {
				if (kurse[i].getTDatum().compareTo(dDatum) <= 0) {
					kurs = kurse[i];
					return kurs;
				}
			}
		}

		kurse = getAlleWechselkurse(mandantenWaehrung, waehrungCNrVonI);
		if (kurse.length > 0) {
			for (int i = kurse.length - 1; i >= 0; i--) {
				if (kurse[i].getTDatum().compareTo(dDatum) <= 0) {
					kurs = kurse[i];
					kurs.setWaehrungCNrVon(waehrungCNrVonI);
					kurs.setWaehrungCNrZu(mandantenWaehrung);
					kurs.setNKurs(BigDecimal.ONE.divide(kurs.getNKurs(),
							LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
							BigDecimal.ROUND_HALF_EVEN));
					return kurs;
				}
			}
		}

		return null;
	}

	private WechselkursDto getLetztenWechselkurs(String waehrungCNrVonI,
			String waehrungCNrNachI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		WechselkursDto kurs = null;

		if (waehrungCNrVonI.equals(waehrungCNrNachI)) {
			kurs = new WechselkursDto();
			kurs.setWaehrungCNrVon(waehrungCNrVonI);
			kurs.setWaehrungCNrZu(waehrungCNrNachI);
			kurs.setNKurs(BigDecimal.ONE
					.setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS));
			return kurs;
		}

		String mandantenWaehrung = theClientDto.getSMandantenwaehrung();
		if (!(mandantenWaehrung.equals(waehrungCNrVonI) || mandantenWaehrung
				.equals(waehrungCNrNachI))) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_IM_WECHSELKURS_KEINE_MANDANTENWAEHRUNG_ENTHALTEN,
					new Exception("Weder '" + waehrungCNrVonI + "' noch '"
							+ waehrungCNrNachI + "'"
							+ "enthalten die Mandantenwaehrung '"
							+ mandantenWaehrung + "'."));
		}

		WechselkursDto[] kurse = getAlleWechselkurse(mandantenWaehrung,
				waehrungCNrNachI);
		if (kurse.length > 0) {
			return kurse[kurse.length - 1];
		}

		kurse = getAlleWechselkurse(mandantenWaehrung, waehrungCNrVonI);
		if (kurse.length > 0) {
			kurs = kurse[kurse.length - 1];
			kurs.setWaehrungCNrVon(waehrungCNrVonI);
			kurs.setWaehrungCNrZu(mandantenWaehrung);
			kurs.setNKurs(BigDecimal.ONE.divide(kurs.getNKurs(),
					LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
					BigDecimal.ROUND_HALF_EVEN));
			return kurs;
		}

		return null;
	}

	private WechselkursDto[] getAlleWechselkurse(String waehrungCNrVon,
			String waehrungCNrBis) throws EJBExceptionLP {

		Query query = em.createNamedQuery("WechselkursfindByVonZu");
		query.setParameter(1, waehrungCNrVon);
		query.setParameter(2, waehrungCNrBis);
		Collection<?> cl = query.getResultList();

		return assembleWechselkursDtos(cl);
	}

	// *** Belegart
	// ****************************************************************
	/**
	 * 
	 * @param belegartDto
	 *            BelegartDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String createBelegart(BelegartDto belegartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();

		// precondition.
		if (belegartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("belegartDto == null"));
		}

		if (belegartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartDto.getCNr() == null"));
		}

		String cNrWieKey = null;
		try {
			// Erst die Belegart.
			Belegart belegart = new Belegart(belegartDto.getCNr(), belegartDto
					.getIStandarderledigungszeitInTagen(), belegartDto
					.getCKurzbezeichnung(), belegartDto.getISort());
			em.persist(belegart);
			em.flush();

			setBelegartFromBelegartDto(belegart, belegartDto);

			if (belegartDto.getBelegartsprDto() != null) {
				// Jetzt die Belegartspr.
				Belegartspr belegartspr = new Belegartspr(belegartDto
						.getBelegartsprDto().getLocaleCNr(), belegartDto
						.getCNr());
				em.persist(belegartspr);
				em.flush();

				setBelegartsprFromBelegartsprDto(belegartspr, belegartDto
						.getBelegartsprDto());
			}
			Belegart temp = em.find(Belegart.class, belegartDto.getCNr());
			if (temp == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			cNrWieKey = temp.getCNr();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return cNrWieKey;
	}

	public void removeBelegart(String cNr) throws EJBExceptionLP {

		myLogger.entry();

		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		try {
			Query query = em.createNamedQuery("BelegartsprfindByBelegartCNr");
			query.setParameter(1, cNr);
			Collection<?> allBelegartspr = query.getResultList();
			Iterator<?> iter = allBelegartspr.iterator();
			while (iter.hasNext()) {
				Belegartspr belegartsprTemp = (Belegartspr) iter.next();
				em.remove(belegartsprTemp);
			}
			Belegart toRemove = em.find(Belegart.class, cNr);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeBelegart(BelegartDto belegartDto) throws EJBExceptionLP {
		if (belegartDto != null) {
			String cNr = belegartDto.getCNr();
			removeBelegart(cNr);
		}
	}

	public void updateBelegart(BelegartDto belegartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegartDto != null) {
			String cNr = belegartDto.getCNr();
			// try {
			// erst die dto
			Belegart belegart = em.find(Belegart.class, cNr);
			if (belegart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBelegartFromBelegartDto(belegart, belegartDto);

			// dann die sprDto
			BelegartsprDto belegartsprDto = belegartDto.getBelegartsprDto();

			if (belegartsprDto != null
					&& belegartsprDto.getBelegartCNr() != null) {
				BelegartsprPK belegartsprPK = new BelegartsprPK();
				belegartsprPK.setLocaleCNr(belegartsprDto.getLocaleCNr());
				belegartsprPK.setBelegartCNr(belegartsprDto.getBelegartCNr());
				// try {
				Belegartspr belegartspr = em.find(Belegartspr.class,
						belegartsprPK);
				if (belegartspr == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				setBelegartsprFromBelegartsprDto(belegartspr, belegartsprDto);
				// }
				// catch (FinderException e) {
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				// e);
				// }
			} else {
				try {
					Belegartspr belegartspr = new Belegartspr(belegartsprDto
							.getLocaleCNr(), belegartDto.getCNr());
					em.persist(belegartspr);
					em.flush();
					setBelegartsprFromBelegartsprDto(belegartspr,
							belegartsprDto);
				} catch (EntityExistsException e) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
				}

			}

			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public BelegartDto belegartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		BelegartDto belegartDto = null;
		// try {
		// Erst die Belegart lesen.
		Belegart belegart = em.find(Belegart.class, cNrI);
		if (belegart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		belegartDto = assembleBelegartDto(belegart);

		BelegartsprDto belegartsprDto = null;
		try {
			// Jetzt die UI-Belegartspr lesen.
			BelegartsprPK belegartsprPK = new BelegartsprPK(theClientDto
					.getLocUiAsString(), cNrI);
			Belegartspr belegartspr = em.find(Belegartspr.class, belegartsprPK);
			if (belegartspr != null) {
				belegartsprDto = assembleBelegartsprDto(belegartspr);
			}
		} catch (Throwable ex) {
			// nothing here.
		}
		belegartDto.setBelegartsprDto(belegartsprDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return belegartDto;
	}

	public BelegartDto[] belegartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BelegartfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,"");
		// }
		return assembleBelegartDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

	}

	private void setBelegartFromBelegartDto(Belegart belegart,
			BelegartDto belegartDto) {
		belegart.setISort(belegartDto.getISort());
		belegart.setCKbez(belegartDto.getCKurzbezeichnung());
		belegart.setIStandarderledigungszeitintagen(belegartDto
				.getIStandarderledigungszeitInTagen());
		em.merge(belegart);
		em.flush();
	}

	private BelegartDto assembleBelegartDto(Belegart belegart) {
		return BelegartDtoAssembler.createDto(belegart);
	}

	private BelegartDto[] assembleBelegartDtos(Collection<?> belegarts) {
		List<BelegartDto> list = new ArrayList<BelegartDto>();
		if (belegarts != null) {
			Iterator<?> iterator = belegarts.iterator();
			while (iterator.hasNext()) {
				Belegart belegart = (Belegart) iterator.next();
				list.add(assembleBelegartDto(belegart));
			}
		}
		BelegartDto[] returnArray = new BelegartDto[list.size()];
		return (BelegartDto[]) list.toArray(returnArray);
	}

	public void createBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP {
		if (belegartsprDto == null) {

		}
		try {
			Belegartspr belegartspr = new Belegartspr(belegartsprDto
					.getLocaleCNr(), belegartsprDto.getBelegartCNr());
			em.persist(belegartspr);
			em.flush();
			setBelegartsprFromBelegartsprDto(belegartspr, belegartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeBelegartspr(String spracheCNr, String belegartCNr)
			throws EJBExceptionLP {
		BelegartsprPK belegartsprPK = new BelegartsprPK();
		belegartsprPK.setLocaleCNr(spracheCNr);
		belegartsprPK.setBelegartCNr(belegartCNr);
		Belegartspr toRemove = em.find(Belegartspr.class, belegartsprPK);
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

	public void removeBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP {
		if (belegartsprDto != null) {
			String spracheCNr = belegartsprDto.getLocaleCNr();
			String belegartCNr = belegartsprDto.getBelegartCNr();
			removeBelegartspr(spracheCNr, belegartCNr);
		}
	}

	public void updateBelegartspr(BelegartsprDto belegartsprDto)
			throws EJBExceptionLP {
		if (belegartsprDto != null) {
			if (belegartsprDto.getLocaleCNr() != null
					&& belegartsprDto.getBelegartCNr() != null) {

				BelegartsprPK belegartsprPK = new BelegartsprPK();
				belegartsprPK.setLocaleCNr(belegartsprDto.getLocaleCNr());
				belegartsprPK.setBelegartCNr(belegartsprDto.getBelegartCNr());
				// try {
				Belegartspr belegartspr = em.find(Belegartspr.class,
						belegartsprPK);
				if (belegartspr == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				setBelegartsprFromBelegartsprDto(belegartspr, belegartsprDto);
				// }
				// catch (FinderException ex) {
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				// ex);
				// }
			}
		}
	}

	public BelegartsprDto belegartsprFindByPrimaryKey(String belegartCNr,
			String spracheCNr) throws EJBExceptionLP {

		// try {
		Belegartspr belegartspr = em.find(Belegartspr.class, new BelegartsprPK(
				spracheCNr, belegartCNr));
		if (belegartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBelegartsprDto(belegartspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setBelegartsprFromBelegartsprDto(Belegartspr belegartspr,
			BelegartsprDto belegartsprDto) {
		belegartspr.setCBez(belegartsprDto.getCBez());
		em.merge(belegartspr);
		em.flush();
	}

	private BelegartsprDto assembleBelegartsprDto(Belegartspr belegartspr) {
		return BelegartsprDtoAssembler.createDto(belegartspr);
	}

	private BelegartsprDto[] assembleBelegartsprDtos(Collection<?> belegartsprs) {
		List<BelegartsprDto> list = new ArrayList<BelegartsprDto>();
		if (belegartsprs != null) {
			Iterator<?> iterator = belegartsprs.iterator();
			while (iterator.hasNext()) {
				Belegartspr belegartspr = (Belegartspr) iterator.next();
				list.add(assembleBelegartsprDto(belegartspr));
			}
		}
		BelegartsprDto[] returnArray = new BelegartsprDto[list.size()];
		return (BelegartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Alle Belegarten in bestmoeglicher Uebersetzung holen.
	 * 
	 * @param pLocale1
	 *            Uebersteuerung der UI Sprache des Benutzers
	 * @param pLocale2
	 *            UI Sprache des Benutzers
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllBelegartenUebersetzt(Locale pLocale1, Locale pLocale2)
			throws EJBExceptionLP {
		Map<String, String> alleBelegarten = null;
		Collection<?> belegartenColl = null;

		// try {
		Query query = em.createNamedQuery("BelegartfindAll");

		belegartenColl = query.getResultList();
		// if (belegartenColl.isEmpty()) { // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		// }
		// catch (FinderException t) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
		// t);
		// }

		alleBelegarten = this.uebersetzeBelegartenOptimal(this
				.assembleBelegartDtos(belegartenColl), pLocale1, pLocale2);

		return alleBelegarten;
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von Belegarten.
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return ComboBoxContentDto[]
	 */
	private Map<String, String> uebersetzeBelegartenOptimal(
			BelegartDto[] pArray, Locale locale1, Locale locale2) {
		Map<String, String> uebersetzung = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeBelegartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			uebersetzung.put(key, value);
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Belegart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeBelegart(String cNr, Locale locale) {
		String cLocale = null;

		cLocale = Helper.locale2String(locale);
		Belegartspr belegartspr = em.find(Belegartspr.class, new BelegartsprPK(
				cLocale, cNr));
		if (belegartspr == null) {
			return null;
		}
		return belegartspr.getCBez();
	}

	/**
	 * Uebersetzt eine Belegart optimal 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzeBelegartOptimal(String cNr, Locale locale1,
			Locale locale2) {
		String uebersetzung;
		uebersetzung = uebersetzeBelegart(cNr, locale1);
		if (uebersetzung == null) {
			uebersetzung = uebersetzeBelegart(cNr, locale2);
		}
		if (uebersetzung == null) {
			uebersetzung = cNr;
		}
		return uebersetzung;
	}

	/**
	 * Zu einer Belegart das gesamte Dto holen. Enthaelt die Kurzbezeichnung.
	 * 
	 * @param pCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return BelegartDto
	 */
	public BelegartDto belegartFindByCNr(String pCNr) throws EJBExceptionLP {

		BelegartDto belegartDto = null;

		try {
			Query query = em.createNamedQuery("BelegartfindBycNr");
			query.setParameter(1, pCNr);
			Belegart belegart = (Belegart) query.getSingleResult();
			belegartDto = assembleBelegartDto(belegart);

		} catch (NoResultException t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		return belegartDto;
	}

	/**
	 * Einen Betrag von einer Waehrung in die andere umrechnen.
	 * 
	 * @param bdBetragI
	 *            der Betrag in der Ausgangswaehrung
	 * @param cCurrency1I
	 *            Ausgangswaehrung
	 * @param cCurrency2I
	 *            Zielwaehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Betrag in der Zielwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal rechneUmInAndereWaehrungZuDatum(BigDecimal bdBetragI,
			String cCurrency1I, String cCurrency2I, Date dDatumI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bdBetragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdBetragI == null"));
		}
		if (cCurrency1I == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cCurrency1I == null"));
		}
		if (cCurrency2I == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cCurrency2I == null"));
		}

		BigDecimal bdBetragO = new BigDecimal(0);

		try {
			if (cCurrency1I.equals(cCurrency2I)) {
				bdBetragO = bdBetragI;
			} else {
				if (!cCurrency1I.equals(theClientDto.getSMandantenwaehrung())
						&& !cCurrency2I.equals(theClientDto
								.getSMandantenwaehrung())) {
					// Zuerst in Mandantenwaehrung umrechnen
					bdBetragI = rechneUmInAndereWaehrungZuDatum(bdBetragI,
							cCurrency1I, theClientDto.getSMandantenwaehrung(),
							dDatumI, theClientDto);
					cCurrency1I = theClientDto.getSMandantenwaehrung();
				}

				WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
						cCurrency1I, cCurrency2I, dDatumI, theClientDto);

				if (kursDto != null && kursDto.getNKurs() != null) {
					bdBetragO = bdBetragI.multiply(kursDto.getNKurs());
				} else {
					ArrayList alDaten = new ArrayList();
					alDaten.add(cCurrency1I);
					alDaten.add(cCurrency2I);

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
							alDaten, new Exception("kein Wechselkurs von "
									+ cCurrency1I + " auf " + cCurrency2I));
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return bdBetragO;
	}

	/**
	 * Einen Betrag von einer Waehrung in die andere umrechnen.
	 * 
	 * @param bdBetragI
	 *            der Betrag in der Ausgangswaehrung
	 * @param bdKursMandantenwaehrungZuBelegwaehrung
	 *            Wechselkurs
	 * @return BigDecimal der Betrag in der Zielwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal rechneUmInMandantenWaehrung(BigDecimal bdBetragI,
			BigDecimal bdKursMandantenwaehrungZuBelegwaehrung)
			throws EJBExceptionLP {
		if (bdBetragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bgBetragI == null"));
		}
		if (bdKursMandantenwaehrungZuBelegwaehrung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"bdKursMandantenwaehrungZuBelegwaehrung == null"));
		}
		return bdBetragI.multiply(Helper
				.getKehrwert(bdKursMandantenwaehrungZuBelegwaehrung));
	}

	/**
	 * Einen Betrag von einer Waehrung in die andere umrechnen. <br>
	 * Diese Methode wirft keine Exceptions.
	 * 
	 * @param bdBetragI
	 *            der Betrag in der Ausgangswaehrung
	 * @param sCurrencyVonI
	 *            Ausgangswaehrung
	 * @param sCurrencyNachI
	 *            Zielwaehrung
	 * @param theClientDto
	 *            String
	 * @return BigDecimal der Betrag in der Zielwaehrung
	 */
	public BigDecimal rechneUmInAndereWaehrungZuDatumOhneExc(
			BigDecimal bdBetragI, String sCurrencyVonI, String sCurrencyNachI,
			Date dDatumI, TheClientDto theClientDto) {
		BigDecimal bdBetragO = null;

		try {
			bdBetragO = rechneUmInAndereWaehrungZuDatum(bdBetragI,
					sCurrencyVonI, sCurrencyNachI, dDatumI, theClientDto);
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(), "sCurrencyVonI="
					+ sCurrencyVonI + "sCurrencyNachI" + sCurrencyNachI, t);
		}

		return bdBetragO;
	}

	public Integer createLieferart(LieferartDto lieferartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// precondition
		if (lieferartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("lieferartDtoI == null"));
		}
		if (lieferartDtoI.getIId() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("lieferartDtoI.getIId() != null"));
		}

		// befuellen von mandantCNr
		if (lieferartDtoI.getMandantCNr() == null) {
			lieferartDtoI.setMandantCNr(theClientDto.getMandant());
		}
		if (lieferartDtoI.getBVersteckt() == null) {
			lieferartDtoI.setBVersteckt(Helper.boolean2Short(false));
		}

		try {
			Query query = em.createNamedQuery("LieferartfindbyCNrMandantCNr");
			query.setParameter(1, lieferartDtoI.getCNr());
			query.setParameter(2, lieferartDtoI.getMandantCNr());

			Lieferart lieferart = (Lieferart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_LIEFERART.UK"));
		} catch (NoResultException ex) {
			//
		}
		PKGeneratorObj pkGen = getPKGeneratorObj();
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_LIEFERART);
		lieferartDtoI.setIId(iId);

		try {
			Lieferart lieferart = new Lieferart(lieferartDtoI.getIId(),
					lieferartDtoI.getCNr(), lieferartDtoI
							.getBFrachtkostenalserledigtverbuchen(),
					lieferartDtoI.getMandantCNr(), lieferartDtoI
							.getBVersteckt());
			em.persist(lieferart);
			em.flush();
			setLieferartFromLieferartDto(lieferart, lieferartDtoI);
			// Spr anlegen
			if (lieferartDtoI.getLieferartsprDto() != null) {
				lieferartDtoI.getLieferartsprDto().setLieferartIId(
						lieferartDtoI.getIId());
				createLieferartspr(lieferartDtoI.getLieferartsprDto(),
						theClientDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iId;
	}

	public void removeLieferart(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdI == null"));
		}

		try {
			Query query = em.createNamedQuery("LieferartsprfindByLieferartIId");
			query.setParameter(1, iIdI);
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Lieferartspr item = (Lieferartspr) iter.next();
				em.remove(item);
			}
			Lieferart lieferart = em.find(Lieferart.class, iIdI);
			if (lieferart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lieferart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeLieferart(LieferartDto lieferartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (lieferartDtoI != null) {
			Integer iId = lieferartDtoI.getIId();
			removeLieferart(iId, theClientDto);
		}
	}

	public void updateLieferart(LieferartDto lieferartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (lieferartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"lieferartDtoI == null"));
		}

		// befuellen der mandantCNr
		if (lieferartDtoI.getMandantCNr() == null) {
			lieferartDtoI.setMandantCNr(theClientDto.getMandant());
		}

		Integer iId = lieferartDtoI.getIId();
		// try {
		Lieferart lieferart = em.find(Lieferart.class, iId);
		if (lieferart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("LieferartfindbyCNrMandantCNr");
			query.setParameter(1, lieferartDtoI.getCNr());
			query.setParameter(2, lieferartDtoI.getMandantCNr());
			Integer iIdVorhanden = null;
			Lieferart temp = (Lieferart) query.getSingleResult();
			if (temp != null) {
				iIdVorhanden = temp.getIId();
			}

			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_LIEFERART.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setLieferartFromLieferartDto(lieferart, lieferartDtoI);

		if (lieferartDtoI.getLieferartsprDto() != null) {
			// -- upd oder create
			lieferartDtoI.getLieferartsprDto().setLieferartIId(
					lieferartDtoI.getIId());
			lieferartDtoI.getLieferartsprDto().setLocaleCNr(
					theClientDto.getLocUiAsString());

			Lieferartspr lieferartspr = em.find(Lieferartspr.class,
					new LieferartsprPK(lieferartDtoI.getIId(), theClientDto
							.getLocUiAsString()));

			if (lieferartspr != null) {
				lieferartspr.setCBezeichnung(lieferartDtoI.getLieferartsprDto()
						.getCBezeichnung());
			} else {

				createLieferartspr(lieferartDtoI.getLieferartsprDto(),
						theClientDto);
			}

		}

	}

	public LieferartDto lieferartFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdI == null"));
		}
//		if (theClientDto.getIDUser() == null) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
//					"cNrUserI == null"));
//		}

		LieferartDto lieferartDto = null;

		// try {
		Lieferart lieferart = em.find(Lieferart.class, iIdI);
		if (lieferart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferartDto = assembleLieferartDto(lieferart);

		try {
			Lieferartspr lieferartspr = em.find(Lieferartspr.class,
					new LieferartsprPK(lieferartDto.getIId(), theClientDto
							.getLocUiAsString()));
			lieferartDto
					.setLieferartsprDto(assembleLieferartsprDto(lieferartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// t);
		// }
		return lieferartDto;
	}

	public LieferartDto lieferartFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
//			Query query = em.createNamedQuery("LieferartfindbyCNrMandantCNr");
//			query.setParameter(1, cNr);
//			query.setParameter(2, theClientDto.getMandant());
			Query query = LieferartQuery.byCnrMandantCnr(em, cNr, theClientDto.getMandant()) ;
			Lieferart lieferart = (Lieferart) query.getSingleResult();
			return lieferartFindByPrimaryKey(lieferart.getIId(), theClientDto);
		} catch (NoResultException ex) {

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	
	private void setLieferartFromLieferartDto(Lieferart lieferart,
			LieferartDto lieferartDto) {
		lieferart.setCNr(lieferartDto.getCNr());
		lieferart.setBFrachtkostenalserledigtverbuchen(lieferartDto
				.getBFrachtkostenalserledigtverbuchen());
		lieferart.setBVersteckt(lieferartDto.getBVersteckt());
		lieferart.setCVersandort(lieferartDto.getCVersandort());
		em.merge(lieferart);
		em.flush();
	}

	private LieferartDto assembleLieferartDto(Lieferart lieferartI) {
		return LieferartDtoAssembler.createDto(lieferartI);
	}

	private LieferartDto[] assembleLieferartDtos(Collection<?> lieferartsI) {
		List<LieferartDto> list = new ArrayList<LieferartDto>();
		if (lieferartsI != null) {
			Iterator<?> iterator = lieferartsI.iterator();
			while (iterator.hasNext()) {
				Lieferart lieferart = (Lieferart) iterator.next();
				list.add(assembleLieferartDto(lieferart));
			}
		}
		LieferartDto[] returnArray = new LieferartDto[list.size()];
		return (LieferartDto[]) list.toArray(returnArray);
	}

	public LieferartsprPK createLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Lieferartspr lieferartspr = new Lieferartspr(lieferartsprDtoI
					.getLieferartIId(), lieferartsprDtoI.getLocaleCNr());
			em.persist(lieferartspr);
			em.flush();
			setLieferartsprFromLieferartspr2Dto(lieferartspr, lieferartsprDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return null;
	}

	public void removeLieferartspr(Integer lieferartIIdI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferartsprPK lieferartsprPK = new LieferartsprPK();
		lieferartsprPK.setLieferartIId(lieferartIIdI);
		lieferartsprPK.setLocaleCNr(localeCNrI);
		Lieferartspr toRemove = em.find(Lieferartspr.class, lieferartsprPK);
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

	public void removeLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lieferartsprDtoI != null) {
			Integer lieferartIId = lieferartsprDtoI.getLieferartIId();
			String localeCNr = lieferartsprDtoI.getLocaleCNr();
			removeLieferartspr(lieferartIId, localeCNr, theClientDto);
		}
	}

	public void updateLieferartspr(LieferartsprDto lieferartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lieferartsprDtoI != null) {
			LieferartsprPK lieferartsprPK = new LieferartsprPK();
			lieferartsprPK.setLieferartIId(lieferartsprDtoI.getLieferartIId());
			lieferartsprPK.setLocaleCNr(lieferartsprDtoI.getLocaleCNr());
			// try {
			Lieferartspr lieferartspr = em.find(Lieferartspr.class,
					lieferartsprPK);
			if (lieferartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferartsprFromLieferartspr2Dto(lieferartspr, lieferartsprDtoI);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public LieferartsprDto lieferartsprFindByPrimaryKey(Integer lieferartIIdI,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		LieferartsprPK lieferartsprPK = new LieferartsprPK();
		lieferartsprPK.setLieferartIId(lieferartIIdI);
		lieferartsprPK.setLocaleCNr(localeCNrI);
		Lieferartspr lieferartspr = em.find(Lieferartspr.class, lieferartsprPK);
		if (lieferartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferartsprDto(lieferartspr);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LieferartsprDto lieferartsprFindByPrimaryKeyOhneExc(
			Integer iIdLieferartI, String sLocaleCNrI, TheClientDto theClientDto) {

		LieferartsprDto oSprDtoO = null;
		try {
			oSprDtoO = lieferartsprFindByPrimaryKey(iIdLieferartI, sLocaleCNrI,
					theClientDto);
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(), "iIdLieferartI="
					+ iIdLieferartI, t);
		}
		return oSprDtoO;
	}

	private void setLieferartsprFromLieferartspr2Dto(Lieferartspr lieferartspr,
			LieferartsprDto lieferartsprDto) {
		lieferartspr.setCBezeichnung(lieferartsprDto.getCBezeichnung());
		em.merge(lieferartspr);
		em.flush();
	}

	private LieferartsprDto assembleLieferartsprDto(Lieferartspr lieferartspr) {
		return LieferartsprDtoAssembler.createDto(lieferartspr);
	}

	private LieferartsprDto[] assembleLieferartsprDtos(
			Collection<?> lieferartsprs) {
		List<LieferartsprDto> list = new ArrayList<LieferartsprDto>();
		if (lieferartsprs != null) {
			Iterator<?> iterator = lieferartsprs.iterator();
			while (iterator.hasNext()) {
				Lieferartspr lieferartspr = (Lieferartspr) iterator.next();
				list.add(assembleLieferartsprDto(lieferartspr));
			}
		}
		LieferartsprDto[] returnArray = new LieferartsprDto[list.size()];
		return (LieferartsprDto[]) list.toArray(returnArray);
	}

	// *** Positionsart
	// *************************************************************
	public String createPositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// precondition
		if (positionsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("positionsartDtoI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		try {
			Positionsart positionsart = new Positionsart(positionsartDtoI
					.getCNr());
			em.persist(positionsart);
			em.flush();

			if (positionsartDtoI.getPositionsartsprDto() != null) {
				positionsartDtoI.getPositionsartsprDto().setPositionsartCNr(
						positionsartDtoI.getCNr());
				createPositionsartspr(positionsartDtoI.getPositionsartsprDto(),
						theClientDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return positionsartDtoI.getCNr();
	}

	public void removePositionsart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrI == null"));
		}

		try {
			Query query = em
					.createNamedQuery("PositionsartsprfindByPositionsartCNr");
			query.setParameter(1, cNrI);
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Positionsartspr item = (Positionsartspr) iter.next();
				em.remove(item);
			}
			Positionsart positionsart = em.find(Positionsart.class, cNrI);
			if (positionsart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(positionsart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removePositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (positionsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"positionsartDtoI == null"));
		}

		String cNr = positionsartDtoI.getCNr();

		removePositionsart(cNr, theClientDto);
	}

	public void updatePositionsart(PositionsartDto positionsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (positionsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"positionsartDtoI == null"));
		}

		String cNr = positionsartDtoI.getCNr();
		// try {

		Positionsart positionsart = em.find(Positionsart.class, cNr);
		if (positionsart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (positionsartDtoI.getPositionsartsprDto() != null
				&& positionsartDtoI.getPositionsartsprDto().getCBez() != null) {
			// -- upd oder create
			if (positionsartDtoI.getPositionsartsprDto().getPositionsartCNr() == null) {
				// create
				// Key(teil) setzen.
				positionsartDtoI.getPositionsartsprDto().setPositionsartCNr(
						positionsartDtoI.getCNr());
				createPositionsartspr(positionsartDtoI.getPositionsartsprDto(),
						theClientDto);
			} else {
				// upd
				updatePositionsartspr(positionsartDtoI.getPositionsartsprDto(),
						theClientDto);
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public PositionsartDto positionsartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		PositionsartDto positionsartDto = null;

		// try {
		Positionsart positionsart = em.find(Positionsart.class, cNrI);
		if (positionsart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		positionsartDto = assemblePositionsartDto(positionsart);

		try {
			Positionsartspr positionsartspr = em.find(Positionsartspr.class,
					new PositionsartsprPK(positionsartDto.getCNr(),
							theClientDto.getLocUiAsString()));
			positionsartDto
					.setPositionsartsprDto(assemblePositionsartsprDto(positionsartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// t);
		// }
		return positionsartDto;
	}

	private PositionsartDto assemblePositionsartDto(Positionsart positionsart) {
		return PositionsartDtoAssembler.createDto(positionsart);
	}

	// *** Status
	// *****************************************************************
	public void removeStatus(StatusDto statusDtoI, TheClientDto theClientDto) {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (statusDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"statusDtoI == null"));
		}

		try {
			Query query = em.createNamedQuery("StatussprfindByStatusCNr");
			query.setParameter(1, statusDtoI.getCNr());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Statusspr item = (Statusspr) iter.next();
				em.remove(item);
			}
			Status status = em.find(Status.class, statusDtoI.getCNr());
			if (status == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			em.remove(status);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateStatus(StatusDto statusDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (statusDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"statusDtoI == null"));
		}

		String cNr = statusDtoI.getCNr();
		// try {
		Status status = em.find(Status.class, cNr);
		if (status == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		status.setOBild(statusDtoI.getOBild());
		try {
			if (statusDtoI.getStatussprDto() != null) {
				// try {
				Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(
						cNr, statusDtoI.getStatussprDto().getLocaleCNr()));
				if (statusspr == null) {
					statusspr = new Statusspr(cNr, statusDtoI.getStatussprDto()
							.getLocaleCNr());
					statusspr.setCBez(statusDtoI.getStatussprDto().getCBez());
					em.persist(statusspr);
					em.flush();
				} else {
					statusspr.setCBez(statusDtoI.getStatussprDto().getCBez());
					em.merge(statusspr);
				}
				// }
				// catch (FinderException ex) {
				// Statusspr statusspr = new Statusspr(cNr,
				// statusDtoI.getStatussprDto().getLocaleCNr());
				// em.persist(statusspr);

				// statusspr.setCBez(statusDtoI.getStatussprDto().getCBez());
				// }

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public HashMap getAllStatiIcon() {
		HashMap<String, Object> hmStatiMitBild = new HashMap<String, Object>();
		// try {
		Query query = em.createNamedQuery("StatusfindByOBildNotNull");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		StatusDto[] statusDtos = assembleStatusDtos(cl);

		for (int i = 0; i < statusDtos.length; i++) {
			hmStatiMitBild
					.put(statusDtos[i].getCNr(), statusDtos[i].getOBild());
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return hmStatiMitBild;
	}

	public String createStatus(StatusDto statusDtoI, TheClientDto theClientDto)
			throws RemoteException {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (statusDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("statusDtoI == null"));
		}

		try {
			Status status = new Status(statusDtoI.getCNr());
			em.persist(status);
			em.flush();
			status.setOBild(statusDtoI.getOBild());
			if (statusDtoI.getStatussprDto() != null) {
				statusDtoI.getStatussprDto().setStatusCNr(statusDtoI.getCNr());
				createStatusspr(statusDtoI.getStatussprDto(), theClientDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return statusDtoI.getCNr();
	}

	public StatusDto statusFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) {
		Validator.notNull(cNrI, "cNrI");
		Validator.notNull(theClientDto.getIDUser(), "getIDUser()") ;

		StatusDto statusDto = null;

		Status status = em.find(Status.class, cNrI);
		if (status == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		statusDto = assembleStatusDto(status);

		try {
			Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(
					statusDto.getCNr(), theClientDto.getLocUiAsString()));
			if(statusspr != null) {
				statusDto.setStatussprDto(assembleStatussprDto(statusspr));
			}
		} catch (Throwable t) {
			// nothing here.
		}

		return statusDto;
	}

	private StatusDto assembleStatusDto(Status status) {
		return StatusDtoAssembler.createDto(status);
	}

	private StatusDto[] assembleStatusDtos(Collection<?> statuss) {
		List<StatusDto> list = new ArrayList<StatusDto>();
		if (statuss != null) {
			Iterator<?> iterator = statuss.iterator();
			while (iterator.hasNext()) {
				Status status = (Status) iterator.next();
				list.add(assembleStatusDto(status));
			}
		}
		StatusDto[] returnArray = new StatusDto[list.size()];
		return (StatusDto[]) list.toArray(returnArray);
	}

	/**
	 * Liefert die (&uuml;bersetzte) Bezeichnung f&uuml;r eine Status-CNr
	 * @param statusCnr die Kennung des Status
	 * @param theClientDto
	 * @return die Bezeichnung sofern vorhanden, ansonsten die CNr 
	 */
	public String getStatusCBez(String statusCnr, TheClientDto theClientDto) {
		StatusDto statusDto = statusFindByPrimaryKey(statusCnr, theClientDto) ;
		StatussprDto sprDto = statusDto.getStatussprDto() ;
		
		if(sprDto != null && sprDto.getCBez() != null) {
			return sprDto.getCBez() ;
		}

		return statusDto.getCNr() ;
	}
	
	// *** Statusspr
	// **************************************************************
	public StatussprDto getStatusspr(String cNrI, String sLocUiI,
			TheClientDto theClientDto) {

		StatussprDto statussprDto = null;
		try {
			statussprDto = statussprFindByPrimaryKey(cNrI, sLocUiI,
					theClientDto);
		} catch (Exception ex) {
			// nothing here.
		}
		return statussprDto;
	}

	public StatussprDto statussprFindByPrimaryKey(String cNrI, String sLocUiI,
			TheClientDto theClientDto) {
		// precondition
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrI == null"));
		}
		if (sLocUiI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"sLocUiI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		// try {
		Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(cNrI,
				sLocUiI));
		if (statusspr == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStatussprDto(statusspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public String createStatusspr(StatussprDto statusspr2DtoI,
			TheClientDto theClientDto) {

		// precondition
		if (statusspr2DtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("statusspr2DtoI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		Statusspr statusspr = null;
		try {
			statusspr = new Statusspr(statusspr2DtoI.getStatusCNr(),
					statusspr2DtoI.getLocaleCNr());
			em.persist(statusspr);
			em.flush();
			statusspr.setCBez(statusspr2DtoI.getCBez());
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return statusspr.getPk().getStatusCNr();
	}

	public void removeStatusspr(StatussprDto statusspr2DtoI,
			TheClientDto theClientDto) {
		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (statusspr2DtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"statusspr2DtoI == null"));
		}

		Statusspr toRemove = em.find(Statusspr.class, new StatussprPK(
				statusspr2DtoI.getStatusCNr(), statusspr2DtoI.getLocaleCNr()));
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

	public void updateStatusspr(StatussprDto statussprDtoI,
			TheClientDto theClientDto) {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (statussprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"statussprDtoI == null"));
		}

		// try {
		Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(
				statussprDtoI.getStatusCNr(), statussprDtoI.getLocaleCNr()));
		if (statusspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		statusspr.setCBez(statussprDtoI.getCBez());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private StatussprDto assembleStatussprDto(Statusspr statusspr) {
		return Statusspr2DtoAssembler.createDto(statusspr);
	}

	// *** Funktion
	// *****************************************************************
	public Integer createFunktion(FunktionDto funktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();

		if (funktionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("funktionDtoI == null"));
		}
		if (funktionDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("funktionDto.getCNr() == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		Integer iId = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			iId = pkGen.getNextPrimaryKey(PKConst.PK_FUNKTION);
			funktionDtoI.setIId(iId);

			Funktion funktion = new Funktion(funktionDtoI.getIId(),
					funktionDtoI.getCNr());
			em.persist(funktion);
			em.flush();
			setFunktionFromFunktionDto(funktion, funktionDtoI);

			if (funktionDtoI.getFunktionsprDto() != null) {
				Funktionspr funktionspr = new Funktionspr(
						funktionDtoI.getIId(), theClientDto
								.getLocMandantAsString(), funktionDtoI
								.getFunktionsprDto().getCBezeichnung());
				em.persist(funktionspr);
				em.flush();
				setFunktionsprFromFunktionsprDto(funktionspr, funktionDtoI
						.getFunktionsprDto());
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return iId;
	}

	public void removeFunktion(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		try {
			Query query = em.createNamedQuery("FunktionsprfindByFunktionIId");
			query.setParameter(1, iIdI);
			Collection<?> allFunktionspr = query.getResultList();
			Iterator<?> iter = allFunktionspr.iterator();
			while (iter.hasNext()) {
				Funktionspr funktionsprTemp = (Funktionspr) iter.next();
				em.remove(funktionsprTemp);
			}
			Funktion funktion = em.find(Funktion.class, iIdI);
			if (funktion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(funktion);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeFunktion(FunktionDto funktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Integer iId = funktionDtoI.getIId();
		removeFunktion(iId, theClientDto);
	}

	public void updateFunktion(FunktionDto funktionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (funktionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"funktionDtoI == null"));
		}

		Integer iId = funktionDtoI.getIId();
		// try {
		Funktion funktion = em.find(Funktion.class, iId);
		if (funktion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setFunktionFromFunktionDto(funktion, funktionDtoI);
		if (funktionDtoI.getFunktionsprDto() != null
				&& funktionDtoI.getFunktionsprDto().getCBezeichnung() != null) {
			// -- upd oder create
			FunktionsprPK funktionsprPK = new FunktionsprPK(funktionDtoI
					.getIId(), theClientDto.getLocUiAsString());
			Funktionspr funktionspr = em.find(Funktionspr.class, funktionsprPK);

			if (funktionspr == null) {
				// create
				// Key(teil) setzen.
				Funktionspr funktionsprNeu = new Funktionspr(funktionDtoI
						.getIId(), theClientDto.getLocUiAsString(),
						funktionDtoI.getFunktionsprDto().getCBezeichnung());
				em.persist(funktionsprNeu);
				em.flush();

			} else {
				funktionspr.setCBezeichnung(funktionDtoI.getFunktionsprDto()
						.getCBezeichnung());
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public FunktionDto funktionFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		FunktionDto funktionDto = null;

		// try {
		Funktion funktion = em.find(Funktion.class, iIdI);
		if (funktion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		funktionDto = assembleFunktionDto(funktion);

		try {
			Funktionspr funktionspr = em.find(Funktionspr.class,
					new FunktionsprPK(funktionDto.getIId(), theClientDto
							.getLocUiAsString()));
			funktionDto.setFunktionsprDto(assembleFunktionsprDto(funktionspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return funktionDto;
	}

	private void setFunktionFromFunktionDto(Funktion funktion,
			FunktionDto funktionDto) {
		funktion.setCNr(funktionDto.getCNr());
		em.merge(funktion);
		em.flush();
	}

	private FunktionDto assembleFunktionDto(Funktion funktion) {
		return FunktionDtoAssembler.createDto(funktion);
	}

	public FunktionsprPK createFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (funktionsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("funktionsprDtoI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		try {
			Funktionspr funktionspr = new Funktionspr(funktionsprDtoI
					.getFunktionIId(), funktionsprDtoI.getLocaleCNr(),
					funktionsprDtoI.getCBezeichnung());
			em.persist(funktionspr);
			em.flush();
			setFunktionsprFromFunktionsprDto(funktionspr, funktionsprDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new FunktionsprPK(funktionsprDtoI.getFunktionIId(),
				funktionsprDtoI.getLocaleCNr());
	}

	public void removeFunktionspr(Integer funktionIIdI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (funktionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"funktionIIdI == null"));
		}
		if (localeCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"localeCNrI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		FunktionsprPK funktionsprPK = new FunktionsprPK();
		funktionsprPK.setFunktionIId(funktionIIdI);
		funktionsprPK.setLocaleCNr(localeCNrI);

		Funktionspr toRemove = em.find(Funktionspr.class, funktionsprPK);
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

	public void removeFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (funktionsprDtoI != null) {
			Integer funktionIId = funktionsprDtoI.getFunktionIId();
			String localeCNr = funktionsprDtoI.getLocaleCNr();
			removeFunktionspr(funktionIId, localeCNr, theClientDto);
		}
	}

	public void updateFunktionspr(FunktionsprDto funktionsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (funktionsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"funktionsprDtoI == null"));
		}

		FunktionsprPK funktionsprPK = new FunktionsprPK();
		funktionsprPK.setFunktionIId(funktionsprDtoI.getFunktionIId());
		funktionsprPK.setLocaleCNr(funktionsprDtoI.getLocaleCNr());
		// try {
		Funktionspr funktionspr = em.find(Funktionspr.class, funktionsprPK);
		if (funktionspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setFunktionsprFromFunktionsprDto(funktionspr, funktionsprDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public com.lp.server.system.service.FunktionsprDto funktionsprFindByPrimaryKey(
			Integer funktionIIdI, String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (funktionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"funktionIIdI == null"));
		}
		if (localeCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"localeCNrI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		// try {
		FunktionsprPK funktionsprPK = new FunktionsprPK();
		funktionsprPK.setFunktionIId(funktionIIdI);
		funktionsprPK.setLocaleCNr(localeCNrI);
		Funktionspr funktionspr = em.find(Funktionspr.class, funktionsprPK);
		if (funktionspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleFunktionsprDto(funktionspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public FunktionsprDto[] funktionsprFindByFunktionIId(Integer funktionIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (funktionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"funktionIIdI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		// try {
		Query query = em.createNamedQuery("FunktionsprfindByFunktionIId");
		query.setParameter(1, funktionIIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleFunktionsprDtos(cl);

		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		// }
	}

	private void setFunktionsprFromFunktionsprDto(Funktionspr funktionspr,
			com.lp.server.system.service.FunktionsprDto funktionsprDtoI) {
		funktionspr.setCBezeichnung(funktionsprDtoI.getCBezeichnung());
		em.merge(funktionspr);
		em.flush();
	}

	private com.lp.server.system.service.FunktionsprDto assembleFunktionsprDto(
			Funktionspr funktionspr) {
		return FunktionsprDtoAssembler.createDto(funktionspr);
	}

	private FunktionsprDto[] assembleFunktionsprDtos(Collection<?> funktionsprs) {
		List<FunktionsprDto> list = new ArrayList<FunktionsprDto>();
		if (funktionsprs != null) {
			Iterator<?> iterator = funktionsprs.iterator();
			while (iterator.hasNext()) {
				Funktionspr funktionspr = (Funktionspr) iterator.next();
				list.add(assembleFunktionsprDto(funktionspr));
			}
		}
		FunktionsprDto[] returnArray = new FunktionsprDto[list.size()];
		return (FunktionsprDto[]) list.toArray(returnArray);
	}

	// *** Positionsartspr
	// **********************************************************
	public PositionsartsprDto getPositionsartspr(String cNrI, String sLocUiI,
			TheClientDto theClientDto) {

		PositionsartsprDto positionsartsprDto = null;
		try {
			positionsartsprDto = positionsartsprFindByPrimaryKey(cNrI, sLocUiI,
					theClientDto);
		} catch (Exception ex) {
			// nothing here.
		}
		return positionsartsprDto;
	}

	public PositionsartsprPK createPositionsartspr(
			PositionsartsprDto positionsartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (positionsartsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("positionsartDtoI == null"));
		}
		if (theClientDto.getIDUser() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}

		try {
			Positionsartspr positionsartspr = new Positionsartspr(
					positionsartsprDtoI.getPositionsartCNr(),
					positionsartsprDtoI.getLocaleCNr(), positionsartsprDtoI
							.getCBez());
			em.persist(positionsartspr);
			em.flush();

			setPositionsartsprFromPositionsartsprDto(positionsartspr,
					positionsartsprDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new PositionsartsprPK(positionsartsprDtoI.getPositionsartCNr(),
				positionsartsprDtoI.getLocaleCNr());
	}

	public void removePositionsartspr(String positionsartCNr,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		PositionsartsprPK positionsartsprPK = new PositionsartsprPK();
		positionsartsprPK.setPositionsartCNr(positionsartCNr);
		positionsartsprPK.setLocaleCNr(localeCNrI);
		Positionsartspr toRemove = em.find(Positionsartspr.class,
				positionsartsprPK);
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

	public void removePositionsartspr(PositionsartsprDto positionsartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		String positionsartCNr = positionsartsprDtoI.getPositionsartCNr();
		String localeCNr = positionsartsprDtoI.getLocaleCNr();
		removePositionsartspr(positionsartCNr, localeCNr, theClientDto);
	}

	public void updatePositionsartspr(PositionsartsprDto positionsartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (positionsartsprDtoI != null) {
			PositionsartsprPK positionsartsprPK = new PositionsartsprPK();
			positionsartsprPK.setPositionsartCNr(positionsartsprDtoI
					.getPositionsartCNr());
			positionsartsprPK.setLocaleCNr(positionsartsprDtoI.getLocaleCNr());
			// try {
			Positionsartspr positionsartspr = em.find(Positionsartspr.class,
					positionsartsprPK);
			if (positionsartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPositionsartsprFromPositionsartsprDto(positionsartspr,
					positionsartsprDtoI);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public PositionsartsprDto positionsartsprFindByPrimaryKey(
			String positionsartCNrI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		PositionsartsprPK positionsartsprPK = new PositionsartsprPK();
		positionsartsprPK.setPositionsartCNr(positionsartCNrI);
		positionsartsprPK.setLocaleCNr(localeCNrI);
		Positionsartspr positionsartspr = em.find(Positionsartspr.class,
				positionsartsprPK);
		if (positionsartspr == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePositionsartsprDto(positionsartspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setPositionsartsprFromPositionsartsprDto(
			Positionsartspr positionsartspr,
			PositionsartsprDto positionsartsprDto) {
		positionsartspr.setCBez(positionsartsprDto.getCBez());
		em.merge(positionsartspr);
		em.flush();
	}

	private PositionsartsprDto assemblePositionsartsprDto(
			Positionsartspr positionsartspr) {
		return PositionsartsprDtoAssembler.createDto(positionsartspr);
	}

	public BigDecimal rechneUmInAndereWaehrungGerundetZuDatum(
			BigDecimal bdBetragI, String currency1I, String currency2I,
			Date dDatumI, TheClientDto theClient) throws EJBExceptionLP,
			RemoteException {
		BigDecimal bdBetragO = rechneUmInAndereWaehrungZuDatum(bdBetragI,
				currency1I, currency2I, dDatumI, theClient);
		return Helper.rundeKaufmaennisch(bdBetragO, FinanzFac.NACHKOMMASTELLEN);
	}

	@Override
	public Map<?, ?> getAllSpr(Locale locale, String mandantCNr) throws RemoteException {
		Map<Object, Object> sprMap = new TreeMap<Object, Object>();
		sprMap.putAll((Map<?,?>)getAllBelegartenUebersetzt(locale, locale));
		sprMap.putAll(getArtikelFac().getAllSprArtikelarten(Helper.locale2String(locale)));
		//sprMap.putAll(getFinanzServiceFac().getAllBuchungsarten(locale, locale));
		sprMap.put(FinanzServiceFacBean.KONTOTYP_SACHKONTO, getFinanzServiceFac().uebersetzeKontotypOptimal(FinanzServiceFacBean.KONTOTYP_SACHKONTO, locale, locale));
		sprMap.put(FinanzServiceFacBean.KONTOTYP_DEBITOR, getFinanzServiceFac().uebersetzeKontotypOptimal(FinanzServiceFacBean.KONTOTYP_DEBITOR, locale, locale));
		sprMap.put(FinanzServiceFacBean.KONTOTYP_KREDITOR, getFinanzServiceFac().uebersetzeKontotypOptimal(FinanzServiceFacBean.KONTOTYP_KREDITOR, locale, locale));
		
		
		return (Map<?,?>)sprMap;
	}

}
