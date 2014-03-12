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
package com.lp.server.system.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.ejb.Positionsartspr;
import com.lp.server.system.ejb.PositionsartsprPK;
import com.lp.server.system.ejb.Statusspr;
import com.lp.server.system.ejb.StatussprPK;
import com.lp.server.system.ejb.Text;
import com.lp.server.system.ejb.TextPK;
import com.lp.server.system.fastlanereader.generated.FLRStatus;
import com.lp.server.system.fastlanereader.generated.FLRStatusspr;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.TextDto;
import com.lp.server.system.service.TextDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class SystemMultilanguageFacBean extends Facade implements
		SystemMultilanguageFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von Stati.
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return Map
	 */
	public Map uebersetzeStatusOptimal(DatenspracheIf[] pArray, Locale locale1,
			Locale locale2) {
		// locale: die optimale uebersetzung fuer ein array finden
		Map<String, String> uebersetzung = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeStatusOptimal(pArray[i].getCNr(), locale1,
					locale2);
			uebersetzung.put(key, value);
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt einen Status optimal. 1.Versuch: mit locale1 2.Versuch: mit
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
	public String uebersetzeStatusOptimal(String cNr, Locale locale1,
			Locale locale2) {
		// locale: die optimale uebersetzung finden
		String uebersetzung;
		uebersetzung = uebersetzeStatus(cNr, locale1);
		if (uebersetzung == null) {
			uebersetzung = uebersetzeStatus(cNr, locale2);
		}
		if (uebersetzung == null) {

			uebersetzung = cNr;
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt einen Status in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @return String
	 */
	private String uebersetzeStatus(String cNr, Locale locale)
			throws EJBExceptionLP {
		// locale: diese methode uebersetzt einen einzelnen status
		String cLocale = null;
		cLocale = Helper.locale2String(locale);
		Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(cNr,
				cLocale));
		if (statusspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return statusspr.getCBez();
	}

	/**
	 * Uebersetzt einen Status in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String uebersetzeStatusOptimal(String cNr, Locale locale)
			throws EJBExceptionLP {
		// locale: diese methode uebersetzt einen einzelnen status
		// try {
		String cLocale = null;
		cLocale = Helper.locale2String(locale);
		Statusspr statusspr = em.find(Statusspr.class, new StatussprPK(cNr,
				cLocale));
		if (statusspr == null) {
			return cNr.trim();
		}
		String uebersetzung = cNr.trim();
		if ((statusspr.getCBez() != null) && !(statusspr.getCBez().equals(""))) {
			uebersetzung = statusspr.getCBez();
		}
		return uebersetzung;
		// }
		// catch (FinderException ex) {
		// return cNr.trim();
		// }
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von Positionsarten
	 * 
	 * @param pArray
	 *            Positionsarten
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return ComboBoxContentDto[]
	 */
	public Map uebersetzePositionsartOptimal(DatenspracheIf[] pArray,
			Locale locale1, Locale locale2) {
		Map<String, String> uebersetzung = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzePositionsartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			uebersetzung.put(key, value);
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Positionsart optimal 1.Versuch: mit locale1 2.Versuch:
	 * mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzePositionsartOptimal(String cNr, Locale locale1,
			Locale locale2) {
		String uebersetzung;
		uebersetzung = uebersetzePositionsart(cNr, locale1);
		if (uebersetzung == null) {
			uebersetzung = uebersetzePositionsart(cNr, locale2);
		}
		if (uebersetzung == null) {
			uebersetzung = cNr;
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Positionsart in die Sprache des &uuml;bergebenen Locales
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @return String
	 */
	private String uebersetzePositionsart(String cNr, Locale locale)
			throws EJBExceptionLP {
		String cLocale = null;
		cLocale = Helper.locale2String(locale);
		Positionsartspr positionsartspr = em.find(Positionsartspr.class,
				new PositionsartsprPK(cNr, cLocale));
		if (positionsartspr == null) {
			return null;
		}
		return positionsartspr.getCBez();
	}

	public HashMap<String, String> getAllStatiMitUebersetzung(Locale locale,
			TheClientDto theClientDto) throws EJBExceptionLP {
		HashMap<String, String> hm = new HashMap<String, String>();
		Session session = null;
		try {
			long l = System.currentTimeMillis();
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRStatus.class);
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRStatus item = (FLRStatus) iter.next();

				Set s = item.getStatus_statusspr_set();

				String statusspr = null;
				Iterator it = s.iterator();
				while (it.hasNext()) {
					FLRStatusspr spr = (FLRStatusspr) it.next();

					if (spr.getLocale().getC_nr()
							.equals(Helper.locale2String(locale))) {
						if (spr.getC_bez() != null
								&& spr.getC_bez().length() > 0) {
							statusspr = spr.getC_bez();
							break;
						}
					}

				}
				if (statusspr == null) {
					statusspr = item.getC_nr().trim();
				}

				hm.put(item.getC_nr(), statusspr);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return hm;
	}

	public TextDto textFindByPrimaryKey(String cToken, String mandantCNr,
			String localeCNr) throws EJBExceptionLP {
		TextDto textDto = null;
		// try {
		TextPK textPK = new TextPK();
		textPK.setCToken(cToken);
		textPK.setMandantCNr(mandantCNr);
		textPK.setLocaleCNr(localeCNr);
		Text text = em.find(Text.class, textPK);
		if (text == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		textDto = assembleTextDto(text);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return textDto;
	}

	public TextDto textFindByPrimaryKeyOhneExc(String cToken,
			String mandantCNr, String localeCNr) throws EJBExceptionLP {
		TextDto textDto = null;
		// try {
		TextPK textPK = new TextPK();
		textPK.setCToken(cToken);
		textPK.setMandantCNr(mandantCNr);
		textPK.setLocaleCNr(localeCNr);
		Text text = em.find(Text.class, textPK);
		if (text == null) {
			return null;
		}
		textDto = assembleTextDto(text);

		// }
		// catch (ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return textDto;
	}

	public TextDto[] textFindByMandantCNrLocaleCNr(String mandantCNr,
			String localeCNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("TextfindByMandantCNrLocaleCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, localeCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		return assembleTextDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	private TextDto assembleTextDto(Text text) {
		return TextDtoAssembler.createDto(text);
	}

	private TextDto[] assembleTextDtos(Collection<?> texts) {
		List<TextDto> list = new ArrayList<TextDto>();
		if (texts != null) {
			Iterator<?> iterator = texts.iterator();
			while (iterator.hasNext()) {
				Text text = (Text) iterator.next();
				list.add(assembleTextDto(text));
			}
		}
		TextDto[] returnArray = new TextDto[list.size()];
		return (TextDto[]) list.toArray(returnArray);
	}

	public HashMap<String, String> getAllTexteRespectMandantCNrLocaleCNr(
			TheClientDto theClientDto) throws EJBExceptionLP {
		HashMap<String, String> hm = new HashMap<String, String>();
		TextDto[] texte = textFindByMandantCNrLocaleCNr(
				theClientDto.getMandant(), theClientDto.getLocUiAsString());
		for (int i = 0; i < texte.length; i++) {
			hm.put(texte[i].getCToken(), texte[i].getCText());
		}
		return hm;
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr,
			Locale loI) throws EJBExceptionLP {
		return super.getTextRespectUISpr(sTokenI, mandantCNr, loI);
	}

	public void createText(TextDto textDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (textDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("textDto == null"));
		}
		try {
			Text text = new Text(textDto.getCToken().toLowerCase(),
					textDto.getMandantCNr(), textDto.getLocaleCNr(),
					textDto.getCText());
			em.persist(text);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeText(TextDto textDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (textDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("textDto == null"));
		}
		Text toRemove = em.find(
				Text.class,
				new TextPK(textDto.getCToken().toLowerCase(), textDto
						.getMandantCNr(), textDto.getLocaleCNr()));
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
}
