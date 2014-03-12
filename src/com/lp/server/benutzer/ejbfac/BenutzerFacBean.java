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
package com.lp.server.benutzer.ejbfac;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;

import com.lp.server.benutzer.ejb.Benutzer;
import com.lp.server.benutzer.ejb.Benutzermandantsystemrolle;
import com.lp.server.benutzer.ejb.Fertigungsgrupperolle;
import com.lp.server.benutzer.ejb.Lagerrolle;
import com.lp.server.benutzer.ejb.Nachrichtarchiv;
import com.lp.server.benutzer.ejb.Nachrichtart;
import com.lp.server.benutzer.ejb.Systemrolle;
import com.lp.server.benutzer.ejb.Thema;
import com.lp.server.benutzer.ejb.Themarolle;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerDtoAssembler;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDtoAssembler;
import com.lp.server.benutzer.service.FertigungsgrupperolleDto;
import com.lp.server.benutzer.service.FertigungsgrupperolleDtoAssembler;
import com.lp.server.benutzer.service.LagerrolleDto;
import com.lp.server.benutzer.service.LagerrolleDtoAssembler;
import com.lp.server.benutzer.service.NachrichtarchivDto;
import com.lp.server.benutzer.service.NachrichtarchivDtoAssembler;
import com.lp.server.benutzer.service.NachrichtartDto;
import com.lp.server.benutzer.service.NachrichtartDtoAssembler;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.benutzer.service.SystemrolleDtoAssembler;
import com.lp.server.benutzer.service.ThemaDto;
import com.lp.server.benutzer.service.ThemaDtoAssembler;
import com.lp.server.benutzer.service.ThemarolleDto;
import com.lp.server.benutzer.service.ThemarolleDtoAssembler;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.fastlanereader.generated.FLRUsercount;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BenutzerFacBean extends LPReport implements BenutzerFac {
	@PersistenceContext
	private EntityManager em;

	private static int REPORT_BENUTZERSTATISTIK_DATUM = 0;
	private static int REPORT_BENUTZERSTATISTIK_MAX_ANZAHL = 1;
	private static int REPORT_BENUTZERSTATISTIK_ANZAHL_SPALTEN = 2;

	private Object[][] data = null;

	public Integer createBenutzer(BenutzerDto benutzerDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (benutzerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("benutzerDto == null"));
		}

		if (benutzerDto.getCKennwort() == null
				|| benutzerDto.getCBenutzerkennung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"benutzerDto.getCKennwort() == null || benutzerDto.getCBenutzerkennung() == null"));
		}
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, benutzerDto.getCBenutzerkennung());
			Benutzer temp = (Benutzer) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BENUTZER.C_BENUTZERKENNUNG"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BENUTZER);
		benutzerDto.setIId(pk);
		try {
			benutzerDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			benutzerDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			benutzerDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			benutzerDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			Benutzer benutzer = new Benutzer(benutzerDto.getIId(),
					benutzerDto.getCBenutzerkennung(), new String(
							benutzerDto.getCKennwort()),
					benutzerDto.getPersonalIIdAnlegen(),
					benutzerDto.getPersonalIIdAendern());
			em.persist(benutzer);
			em.flush();

			if (benutzerDto.getBAendern() == null) {
				benutzerDto.setBAendern(benutzer.getBAendern());
			}

			if (benutzerDto.getBGesperrt() == null) {
				benutzerDto.setBGesperrt(benutzer.getBGesperrt());
			}

			setBenutzerFromBenutzerDto(benutzer, benutzerDto);
			return benutzerDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * L&ouml;scht einen Bneutzer, wenn dieser nicht mehr in Verwendung ist, anhand
	 * seiner eindeutigen ID
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeBenutzer(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Benutzer benutzer = em.find(Benutzer.class, iId);
			if (benutzer == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(benutzer);
			em.flush();
			// benutzer.remove();
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void sendJmsMessageMitArchiveintrag(String nachrichtartCNr,
			String zusatz, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("NachrichtartfindByCNr");
			query.setParameter(1, nachrichtartCNr);
			// @todo getSingleResult oder getResultList ?
			Nachrichtart nachrichtart = (Nachrichtart) query.getSingleResult();

			String nachricht = "";

			if (zusatz != null) {
				nachricht = zusatz;
			}

			// Archiveintrag erstellen
			NachrichtarchivDto naDto = new NachrichtarchivDto(
					nachrichtart.getIId(), theClientDto.getIDPersonal(),
					nachricht);
			Integer nachrichtArchivIId = getBenutzerFac()
					.createNachrichtarchiv(naDto, theClientDto);
			// JMS aufruf
			String thema = nachrichtart.getThemaCNr();
			try {
				HelperServer.sendJmsMessage(getInitialContext(), thema,
						nachricht, nachrichtArchivIId, true, theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SYSTEM_JMS, e);
			}
		} catch (NoResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"JMS-Nachrichtenart ist nicht vorhanden!");
		}

	}

	public void removeNachrichtart(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Nachrichtart nachrichtart = em.find(Nachrichtart.class, iId);
			if (nachrichtart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(nachrichtart);
			em.flush();

		}

		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Schreibt &Auml;nderungen eines Benutzers in der DB fest
	 * 
	 * @param benutzerDto
	 *            BenutzerDto
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 */
	public void updateBenutzer(BenutzerDto benutzerDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (benutzerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("benutzerDto == null"));
		}
		if (benutzerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("benutzerDto.getIId() == null"));
		}
		if (benutzerDto.getBAendern() == null
				|| benutzerDto.getBGesperrt() == null
				|| benutzerDto.getCKennwort() == null
				|| benutzerDto.getCBenutzerkennung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"benutzerDto.getBAendern() == null || benutzerDto.getBGesperrt() == null || benutzerDto.getCKennwort() == null || benutzerDto.getCBenutzerkennung() == null"));
		}
		Integer iId = benutzerDto.getIId();
		// try {
		Benutzer benutzer = em.find(Benutzer.class, iId);
		if (benutzer == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, benutzerDto.getCBenutzerkennung());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Benutzer) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_BENUTZER.C_BENUTZERKENNUNG"));
			}

		} catch (NoResultException ex) {

		}

		benutzerDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		benutzerDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		setBenutzerFromBenutzerDto(benutzer, benutzerDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Findet einen Benutzer anhand seiner eindeutigen ID
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 * @return BenutzerDto
	 */
	public BenutzerDto benutzerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		
		BenutzerDto benutzerDto = benutzerFindByPrimaryKeyOhneExc(iId);
		
		if (benutzerDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return benutzerDto;
	}
	
	public BenutzerDto benutzerFindByPrimaryKeyOhneExc(Integer iId) {
		
		Benutzer benutzer = em.find(Benutzer.class, iId);
		if (benutzer == null) {
			return null;
		}
		return assembleBenutzerDto(benutzer);
	}

	/**
	 * Liefert alle Eintraege eines Benutzer zurueck
	 * 
	 * @param benutzerIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BenutzermandantsystemrolleDto[]
	 */
	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByBenutzerIId(
			Integer benutzerIId) throws EJBExceptionLP {
		if (benutzerIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("benutzerIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIId");
		query.setParameter(1, benutzerIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleBenutzermandantsystemrolleDtos(cl);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByBenutzerIIdOhneExc(
			Integer benutzerIId) {
		
		Query query = em
				.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIId");
		query.setParameter(1, benutzerIId);
		
		Collection<?> cl = query.getResultList();

		return assembleBenutzermandantsystemrolleDtos(cl);
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();

		if ("Datum".equals(fieldName)) {
			value = data[index][REPORT_BENUTZERSTATISTIK_DATUM];
		} else if ("MaxAnzahl".equals(fieldName)) {
			value = data[index][REPORT_BENUTZERSTATISTIK_MAX_ANZAHL];
		}
		return value;
	}

	public int getAnzahlDerUnbearbeitetenMeldungen(TheClientDto theClientDto) {

		Long result = null;

		Integer systemrolleIId = getTheJudgeFac().getSystemrolleIId(
				theClientDto);
		String queryString = "select count(nachrichtarchiv.i_id) from FLRNachrichtarchiv nachrichtarchiv INNER JOIN nachrichtarchiv.flrnachrichtart.flrthema.themarollen AS tr WHERE tr.i_id="
				+ systemrolleIId
				+ " AND nachrichtarchiv.flrpersonal_bearbeiter IS NULL";

		org.hibernate.Session session = FLRSessionFactory.getFactory()
				.openSession();
		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();

		if (resultList.size() > 0) {
			result = (Long) resultList.iterator().next();
		}

		return result.intValue();
	}

	public int getAnzahlDerNochNichtErledigtenAberNochZuBearbeitendenMeldungen(
			TheClientDto theClientDto) {
		Long result = null;
		Integer systemrolleIId = getTheJudgeFac().getSystemrolleIId(
				theClientDto);
		String queryString = "select count(nachrichtarchiv.i_id) from FLRNachrichtarchiv nachrichtarchiv INNER JOIN nachrichtarchiv.flrnachrichtart.flrthema.themarollen AS tr WHERE tr.i_id="
				+ systemrolleIId
				+ " AND nachrichtarchiv.flrpersonal_bearbeiter IS NOT NULL AND nachrichtarchiv.flrpersonal_erledigt  IS NULL";

		org.hibernate.Session session = FLRSessionFactory.getFactory()
				.openSession();
		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();

		if (resultList.size() > 0) {
			result = (Long) resultList.iterator().next();
		}

		return result.intValue();
	}

	public String[] getThemenDesAngemeldetenBenutzers(TheClientDto theClientDto) {

		Integer systemrolleIId = getTheJudgeFac().getSystemrolleIId(
				theClientDto);

		Query query = em.createNamedQuery("ThemarollefindBySystemrolleIId");
		query.setParameter(1, systemrolleIId);

		Collection<?> cl = query.getResultList();

		String[] s = new String[cl.size()];

		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				Themarolle themarolle = (Themarolle) iterator.next();
				s[i] = themarolle.getThemaCNr();
				i++;
			}
		}
		return s;

	}

	public int getAnzahlDerMandantenEinesBenutzers(String benutzername,
			String kennwort) {
		Integer benutzerIId = benutzerFindByCBenutzerkennung(benutzername,
				kennwort).getIId();
		return benutzermandantsystemrolleFindByBenutzerIId(benutzerIId).length;
	}

	/**
	 * Findet einen Benutzer anhand seiner Benutzerkennung, da diese in der DB
	 * unique ist und prueft ob das mitgelieferte Kennwort gueltig ist.
	 * 
	 * @param cBenutzerkennung
	 *            String
	 * @param cKennwort
	 *            String
	 * @throws EJBExceptionLP
	 * @return BenutzerDto Wenn Kennwort gueltig ist
	 */
	public BenutzerDto benutzerFindByCBenutzerkennung(String cBenutzerkennung,
			String cKennwort) throws EJBExceptionLP {
		if (cBenutzerkennung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cBenutzerkennung == null"));
		}
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, cBenutzerkennung);
			Benutzer benutzer = (Benutzer) query.getSingleResult();
			BenutzerDto benutzerDto = assembleBenutzerDto(benutzer);

			if (new String(cKennwort).equals(new String(benutzerDto
					.getCKennwort()))) {
				return benutzerDto;
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IM_KENNWORT, "");
			}

		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		}

	}
	
	public BenutzerDto benutzerFindByCBenutzerkennungOhneExc(String cBenutzerkennung) {
		Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
		query.setParameter(1, cBenutzerkennung);
		try {
			Benutzer benutzer = (Benutzer) query.getSingleResult();
			return assembleBenutzerDto(benutzer);
		} catch(NoResultException ex) {
			return null;
		}
	}

	public BenutzerDto benutzerFindByCBenutzerkennungOhneEx(
			String cBenutzerkennung, String cKennwort) throws EJBExceptionLP {
		if (cBenutzerkennung == null) return null ;

		BenutzerDto benutzerDto = null ;
		
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, cBenutzerkennung);
			Benutzer benutzer = (Benutzer) query.getSingleResult();
			benutzerDto = assembleBenutzerDto(benutzer);

			if(null == cKennwort && null != benutzerDto.getCKennwort()) return null ;
			
			if (cKennwort.equals(new String(benutzerDto.getCKennwort()))) {
				return benutzerDto;
			}
			
			benutzerDto = null ;
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e1) {
		}

		return benutzerDto ;
		
//		try {
//			return benutzerFindByCBenutzerkennung(cBenutzerkennung, cKennwort);
//		} catch (Exception e) {
//			return null;
//		}
	}

	private void setBenutzerFromBenutzerDto(Benutzer benutzer,
			BenutzerDto benutzerDto) {
		benutzer.setCBenutzerkennung(benutzerDto.getCBenutzerkennung());
		benutzer.setCKennwort(new String(benutzerDto.getCKennwort()));
		benutzer.setBAendern(benutzerDto.getBAendern());
		benutzer.setBGesperrt(benutzerDto.getBGesperrt());
		benutzer.setTGueltigbis(benutzerDto.getTGueltigbis());
		benutzer.setIFehlversuchegemacht(benutzerDto.getIFehlversuchegemacht());
		benutzer.setMandantCNrDefault(benutzerDto.getMandantCNrDefault());
		benutzer.setPersonalIIdAendern(benutzerDto.getPersonalIIdAendern());
		benutzer.setTAendern(benutzerDto.getTAendern());
		em.merge(benutzer);
		em.flush();
	}

	private BenutzerDto assembleBenutzerDto(Benutzer benutzer) {
		return BenutzerDtoAssembler.createDto(benutzer);
	}

	private NachrichtartDto assembleNachrichtartDto(Nachrichtart nachrichtart) {
		return NachrichtartDtoAssembler.createDto(nachrichtart);
	}

	private BenutzerDto[] assembleBenutzerDtos(Collection<?> benutzers) {
		List<BenutzerDto> list = new ArrayList<BenutzerDto>();
		if (benutzers != null) {
			Iterator<?> iterator = benutzers.iterator();
			while (iterator.hasNext()) {
				Benutzer benutzer = (Benutzer) iterator.next();
				list.add(assembleBenutzerDto(benutzer));
			}
		}
		BenutzerDto[] returnArray = new BenutzerDto[list.size()];
		return (BenutzerDto[]) list.toArray(returnArray);
	}

	/**
	 * Legt eine neue Systemrolle in der DB an
	 * 
	 * @param systemrolleDto
	 *            SystemrolleDto
	 * @return Integer ID
	 * @throws EJBExceptionLP
	 */
	public Integer createSystemrolle(SystemrolleDto systemrolleDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (systemrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("systemrolleDto == null"));
		}
		if (systemrolleDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("systemrolleDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("SystemrollefindByCBez");
			query.setParameter(1, systemrolleDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Systemrolle temp = (Systemrolle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_SYSTEMROLLE.C_BEZ"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SYSTEMROLLE);
		systemrolleDto.setIId(pk);
		try {
			Systemrolle systemrolle = new Systemrolle(systemrolleDto.getIId(),
					systemrolleDto.getCBez());
			em.persist(systemrolle);
			em.flush();

			setSystemrolleFromSystemrolleDto(systemrolle, systemrolleDto);
			return systemrolleDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createNachrichtart(NachrichtartDto nachrichtartDto) {
		if (nachrichtartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("nachrichtartDto == null"));
		}

		try {
			Query query = em.createNamedQuery("NachrichtartfindByCNr");
			query.setParameter(1, nachrichtartDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Nachrichtart temp = (Nachrichtart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_NACHRICHTART.C_BEZ"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTART);
		nachrichtartDto.setIId(pk);
		try {
			Nachrichtart systemrolle = new Nachrichtart(
					nachrichtartDto.getIId(), nachrichtartDto.getCNr(),
					nachrichtartDto.getThemaCNr(),
					nachrichtartDto.getBArchivieren(),
					nachrichtartDto.getBPopup());
			em.persist(systemrolle);
			em.flush();

			setNachrichtartFromNachrichtartDto(systemrolle, nachrichtartDto);
			return nachrichtartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createLagerrolle(LagerrolleDto lagerrolleDto) {
		if (lagerrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerrolleDto == null"));
		}

		try {
			Query query = em
					.createNamedQuery("LagerrollefindBySystemrolleIIdLagerIId");
			query.setParameter(1, lagerrolleDto.getSystemrolleIId());
			query.setParameter(2, lagerrolleDto.getLagerIId());
			// @todo getSingleResult oder getResultList ?
			Lagerrolle temp = (Lagerrolle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_LAGERROLLE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LAGERROLLE);
		lagerrolleDto.setIId(pk);
		try {
			Lagerrolle lagerrolle = new Lagerrolle(lagerrolleDto.getIId(),
					lagerrolleDto.getSystemrolleIId(),
					lagerrolleDto.getLagerIId());
			em.persist(lagerrolle);
			em.flush();

			setLagerrolleFromLagerrolleDto(lagerrolle, lagerrolleDto);
			return lagerrolleDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createFertigungsgrupperolle(FertigungsgrupperolleDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerrolleDto == null"));
		}

		try {
			Query query = em
					.createNamedQuery("FertigungsgrupperollefindBySystemrolleIIdFertigungsgruppeIId");
			query.setParameter(1, dto.getSystemrolleIId());
			query.setParameter(2, dto.getFertigungsgruppeIId());
			// @todo getSingleResult oder getResultList ?
			Fertigungsgrupperolle temp = (Fertigungsgrupperolle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_FERTIGUNGSGRUPPEROLLE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FERTIGUNGSGRUPPEROLLE);
		dto.setIId(pk);
		try {
			Fertigungsgrupperolle bean = new Fertigungsgrupperolle(dto.getIId(),
					dto.getSystemrolleIId(),
					dto.getFertigungsgruppeIId());
			em.persist(bean);
			em.flush();

			setFertigungsgrupperolleFromFertigungsgrupperolleDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}
	public Integer createNachrichtarchiv(NachrichtarchivDto nachrichtarchivDto,
			TheClientDto theClientDto) {
		if (nachrichtarchivDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("nachrichtarchivDto == null"));
		}

		/*
		 * try { Query query = em.createNamedQuery("NachrichtartfindByCNr");
		 * query.setParameter(1, nachrichtartDto.getCNr()); // @todo
		 * getSingleResult oder getResultList ? Nachrichtart temp =
		 * (Nachrichtart) query.getSingleResult(); throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new
		 * Exception("PERS_NACHRICHTART.C_BEZ")); } catch (NoResultException
		 * ex1) { // nothing here }
		 */

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTARCHIV);
		nachrichtarchivDto.setIId(pk);
		try {
			Nachrichtarchiv nachrichtarchiv = new Nachrichtarchiv(
					nachrichtarchivDto.getIId(),
					nachrichtarchivDto.getNachrichtartIId(),
					nachrichtarchivDto.getPersonalIIdAnlegen(),
					nachrichtarchivDto.getCNachricht(),
					nachrichtarchivDto.getTZeit());
			em.persist(nachrichtarchiv);
			em.flush();

			setNachrichtarchivFromNachrichtarchivDto(nachrichtarchiv,
					nachrichtarchivDto);
			return nachrichtarchivDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void erledigeNachricht(Integer nachrichtarchivIId,
			String cErledigungsgrund, TheClientDto theClientDto) {
		Nachrichtarchiv thema = em.find(Nachrichtarchiv.class,
				nachrichtarchivIId);

		if (thema.getPersonalIIdBearbeiter() == null) {
			thema.setPersonalIIdBearbeiter(theClientDto.getIDPersonal());
		}

		if (thema.getTBearbeitung() == null) {
			thema.setTBearbeitung(new Timestamp(System.currentTimeMillis()));
		}

		thema.setTEledigt(new Timestamp(System.currentTimeMillis()));
		thema.setPersonalIIdErledigt(theClientDto.getIDPersonal());
		thema.setCErledigungsgrund(cErledigungsgrund);

		em.merge(thema);
		em.flush();

	}

	public Integer weiseNachrichtPersonZu(Integer nachrichtarchivIId,
			TheClientDto theClientDto) {
		Nachrichtarchiv archiv = em.find(Nachrichtarchiv.class,
				nachrichtarchivIId);

		if (archiv.getPersonalIIdBearbeiter() == null) {
			archiv.setPersonalIIdBearbeiter(theClientDto.getIDPersonal());
			archiv.setTBearbeitung(new Timestamp(System.currentTimeMillis()));
			em.merge(archiv);
			em.flush();
			return null;
		} else {
			return archiv.getPersonalIIdBearbeiter();
		}
	}

	public Integer createThemarolle(ThemarolleDto themarolleDto) {
		if (themarolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("themarolleDto == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ThemarollefindByThemaCNrSystemrolleIId");
			query.setParameter(1, themarolleDto.getThemaCNr());
			query.setParameter(2, themarolleDto.getSystemrolleIId());
			// @todo getSingleResult oder getResultList ?
			Themarolle temp = (Themarolle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_THEMAROLLE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_THEMAROLLE);
		themarolleDto.setIId(pk);
		try {
			Themarolle themarolle = new Themarolle(themarolleDto.getIId(),
					themarolleDto.getThemaCNr(),
					themarolleDto.getSystemrolleIId());
			em.persist(themarolle);
			em.flush();

			setThemarolleFromThemarolleDto(themarolle, themarolleDto);
			return themarolleDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * L&ouml;scht eine Systemrolle, wenn diese nicht mehr verwendet wird
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeSystemrolle(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Systemrolle systemrolle = em.find(Systemrolle.class, iId);
			if (systemrolle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(systemrolle);
			em.flush();
			// systemrolle.remove();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeThemarolle(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Themarolle themarolle = em.find(Themarolle.class, iId);
			if (themarolle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(themarolle);
			em.flush();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeLagerrolle(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Lagerrolle lagerrolle = em.find(Lagerrolle.class, iId);
			if (lagerrolle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lagerrolle);
			em.flush();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeFertigungsgrupperolle(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Fertigungsgrupperolle fertigungsgrupperolle = em.find(Fertigungsgrupperolle.class, iId);
			if (fertigungsgrupperolle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(fertigungsgrupperolle);
			em.flush();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Schreib &Auml;nderungen in der Systemrolle in der DB fest
	 * 
	 * @param systemrolleDto
	 *            SystemrolleDto
	 * @throws EJBExceptionLP
	 */
	public void updateSystemrolle(SystemrolleDto systemrolleDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (systemrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("systemrolleDto == null"));
		}
		if (systemrolleDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("systemrolleDto.getIId() == null"));
		}
		if (systemrolleDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("systemrolleDto.getCBez() == null"));
		}
		Integer iId = systemrolleDto.getIId();
		// try {
		Systemrolle systemrolle = em.find(Systemrolle.class, iId);
		if (systemrolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, systemrolleDto.getCBez());
			Integer iIdVorhanden = ((Benutzer) query.getSingleResult())
					.getIId();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_SYSTEMROLLE.C_BEZ"));

		} catch (NoResultException ex) {

		}
		setSystemrolleFromSystemrolleDto(systemrolle, systemrolleDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
		// e);
		// }
	}

	public void updateThemarolle(ThemarolleDto themarolleDto) {
		if (themarolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("themarolleDto == null"));
		}
		if (themarolleDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("themarolleDto.getIId() == null"));
		}

		Integer iId = themarolleDto.getIId();
		// try {
		Themarolle themarolle = em.find(Themarolle.class, iId);
		if (themarolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		try {
			Query query = em
					.createNamedQuery("ThemarollefindByThemaCNrSystemrolleIId");
			query.setParameter(1, themarolleDto.getThemaCNr());
			query.setParameter(2, themarolleDto.getSystemrolleIId());
			Integer iIdVorhanden = ((Themarolle) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SYSTEMROLLE.C_BEZ"));
			}

		} catch (NoResultException ex) {

		}
		setThemarolleFromThemarolleDto(themarolle, themarolleDto);

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBenutzerstatistik(java.sql.Date dVon,
			java.sql.Date dBis, TheClientDto theClientDto) {

		dVon = Helper.cutDate(dVon);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT uc FROM FLRUsercount AS uc WHERE uc.t_zeitpunkt>='"
				+ Helper.formatDateWithSlashes(dVon)
				+ "' AND uc.t_zeitpunkt<='"
				+ Helper.formatDateWithSlashes(dBis) + "'";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);
		List<?> resultList = inventurliste.list();
		Iterator<?> resultListIterator = resultList.iterator();

		TreeMap<java.sql.Timestamp, Integer> tmTage = new TreeMap<java.sql.Timestamp, Integer>();
		while (resultListIterator.hasNext()) {
			FLRUsercount uc = (FLRUsercount) resultListIterator.next();

			java.sql.Timestamp t = new java.sql.Timestamp(Helper.cutDate(
					uc.getT_zeitpunkt()).getTime());

			if (tmTage.containsKey(t)) {
				Integer iAnzahlVorhanden = tmTage.get(t);
				if (uc.getI_anzahl().intValue() > iAnzahlVorhanden.intValue()) {
					tmTage.put(t, uc.getI_anzahl());
				}

			} else {
				tmTage.put(t, uc.getI_anzahl());
			}

		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dVon.getTime());

		while (c.getTimeInMillis() <= dBis.getTime()) {

			java.sql.Timestamp t = new java.sql.Timestamp(Helper.cutDate(
					c.getTime()).getTime());
			if (!tmTage.containsKey(t)) {
				tmTage.put(t, new Integer(0));
			}
			c.add(Calendar.DATE, 1);
		}

		ArrayList alDaten = new ArrayList();
		Iterator it = tmTage.keySet().iterator();
		while (it.hasNext()) {
			java.sql.Timestamp t = (java.sql.Timestamp) it.next();

			Object[] oZeile = new Object[REPORT_BENUTZERSTATISTIK_ANZAHL_SPALTEN];
			oZeile[REPORT_BENUTZERSTATISTIK_DATUM] = t;
			oZeile[REPORT_BENUTZERSTATISTIK_MAX_ANZAHL] = tmTage.get(t);
			alDaten.add(oZeile);
		}

		Object[][] dataTemp = new Object[1][2];
		data = (Object[][]) alDaten.toArray(dataTemp);

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", new java.sql.Timestamp(dVon.getTime()));
		parameter.put("P_BIS", new java.sql.Timestamp(dBis.getTime()));
		initJRDS(parameter, PersonalFac.REPORT_MODUL,
				BenutzerFac.REPORT_BENUTZERSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocMandant(),
				theClientDto);
		return getReportPrint();
	}

	public void updateLagerrolle(LagerrolleDto lagerrolleDto) {
		if (lagerrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerrolleDto == null"));
		}
		if (lagerrolleDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lagerrolleDto.getIId() == null"));
		}

		Integer iId = lagerrolleDto.getIId();
		// try {
		Lagerrolle themarolle = em.find(Lagerrolle.class, iId);
		if (themarolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		try {
			Query query = em
					.createNamedQuery("LagerrollefindBySystemrolleIIdLagerIId");
			query.setParameter(1, lagerrolleDto.getSystemrolleIId());
			query.setParameter(2, lagerrolleDto.getLagerIId());
			Integer iIdVorhanden = ((Lagerrolle) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_LAGERROLLE.C_BEZ"));
			}

		} catch (NoResultException ex) {

		}
		setLagerrolleFromLagerrolleDto(themarolle, lagerrolleDto);

	}

	public void updateFertigungsgrupperolle(FertigungsgrupperolleDto fertigungsgrupperolleDto) {
		

		Integer iId = fertigungsgrupperolleDto.getIId();
		// try {
		Fertigungsgrupperolle themarolle = em.find(Fertigungsgrupperolle.class, iId);
		if (themarolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		try {
			Query query = em
					.createNamedQuery("FertigungsgrupperollefindBySystemrolleIIdFertigungsgruppeIId");
			query.setParameter(1, fertigungsgrupperolleDto.getSystemrolleIId());
			query.setParameter(2, fertigungsgrupperolleDto.getFertigungsgruppeIId());
			Integer iIdVorhanden = ((Fertigungsgrupperolle) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_FERTIGUNGSGRUPPEROLLE.UK"));
			}

		} catch (NoResultException ex) {

		}
		setFertigungsgrupperolleFromFertigungsgrupperolleDto(themarolle, fertigungsgrupperolleDto);

	}

	public void updateNachrichtart(NachrichtartDto nachrichtartDto) {
		if (nachrichtartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("nachrichtartDto == null"));
		}
		if (nachrichtartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("nachrichtartDto.getIId() == null"));
		}

		Integer iId = nachrichtartDto.getIId();

		Nachrichtart systemrolle = em.find(Nachrichtart.class, iId);
		if (systemrolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		try {
			Query query = em.createNamedQuery("NachrichtartfindByCNr");
			query.setParameter(1, nachrichtartDto.getCBez());
			Integer iIdVorhanden = ((Nachrichtart) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_NACHIRCHTART.C_NR"));
			}
		} catch (NoResultException ex) {

		}
		setNachrichtartFromNachrichtartDto(systemrolle, nachrichtartDto);

	}

	public void updateNachrichtarchiv(NachrichtarchivDto nachrichtarchivDto) {
		if (nachrichtarchivDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("nachrichtarchivDto == null"));
		}
		if (nachrichtarchivDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("nachrichtarchivDto.getIId() == null"));
		}

		Integer iId = nachrichtarchivDto.getIId();

		Nachrichtarchiv nachrichtarchiv = em.find(Nachrichtarchiv.class, iId);
		if (nachrichtarchiv == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		/*
		 * try { Query query = em.createNamedQuery("NachrichtartfindByCNr");
		 * query.setParameter(1, nachrichtartDto.getCBez()); Integer
		 * iIdVorhanden = ((Nachrichtart) query.getSingleResult()) .getIId(); if
		 * (iId.equals(iIdVorhanden) == false) { throw new EJBExceptionLP(
		 * EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
		 * "PERS_NACHIRCHTART.C_NR")); } } catch (NoResultException ex) {
		 * 
		 * }
		 */
		setNachrichtarchivFromNachrichtarchivDto(nachrichtarchiv,
				nachrichtarchivDto);

	}

	public void updateThema(ThemaDto themaDto) {
		if (themaDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("thematDto == null"));
		}

		Thema thema = em.find(Thema.class, themaDto.getCNr());
		if (thema == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		thema.setCBez(themaDto.getCBez());

	}

	/**
	 * Findet eine Systemrolle anhand seiner eindeutigen ID
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return SystemrolleDto
	 */
	public SystemrolleDto systemrolleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Systemrolle systemrolle = em.find(Systemrolle.class, iId);
		if (systemrolle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSystemrolleDto(systemrolle);
	}

	public ThemaDto themaFindByPrimaryKey(String cNr) {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		Thema thema = em.find(Thema.class, cNr);
		if (thema == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleThemaDto(thema);
	}

	public NachrichtarchivDto nachrichtarchivFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		Nachrichtarchiv thema = em.find(Nachrichtarchiv.class, iId);
		if (thema == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleNachrichtarchivDto(thema);
	}

	public ThemarolleDto themarolleFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		Themarolle thema = em.find(Themarolle.class, iId);
		if (thema == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleThemarolleDto(thema);
	}

	public LagerrolleDto lagerrolleFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		Lagerrolle lagerrolle = em.find(Lagerrolle.class, iId);
		if (lagerrolle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerrolleDto(lagerrolle);
	}
	public FertigungsgrupperolleDto fertigungsgrupperolleFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		Fertigungsgrupperolle fertigungsgrupperolle = em.find(Fertigungsgrupperolle.class, iId);
		if (fertigungsgrupperolle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleFertigungsgrupperolleDto(fertigungsgrupperolle);
	}

	public NachrichtartDto nachrichtartFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Nachrichtart nachrichtart = em.find(Nachrichtart.class, iId);
		if (nachrichtart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleNachrichtartDto(nachrichtart);

	}

	/**
	 * Findet eine Systemrolle anhand seiner Bezeichnung, da diese in der DB
	 * unique ist
	 * 
	 * @param cBez
	 *            String
	 * @throws EJBExceptionLP
	 * @return SystemrolleDto
	 */
	public SystemrolleDto systemrolleFindByCBez(String cBez)
			throws EJBExceptionLP {
		myLogger.entry();
		if (cBez == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cBez == null"));
		}
		try {
			Query query = em.createNamedQuery("SystemrollefindByCBez");
			query.setParameter(1, cBez);
			// @todo getSingleResult oder getResultList ?
			Systemrolle systemrolle = (Systemrolle) query.getSingleResult();
			return assembleSystemrolleDto(systemrolle);

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public SystemrolleDto systemrolleFindByCBezOhneExc(String cBez)
			throws EJBExceptionLP {
		myLogger.entry();
		if (cBez == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cBez == null"));
		}
		try {
			Query query = em.createNamedQuery("SystemrollefindByCBez");
			query.setParameter(1, cBez);
			// @todo getSingleResult oder getResultList ?

			return assembleSystemrolleDto((Systemrolle) query.getSingleResult());
			// {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FIND,
			// null);
			// }
			// }
			// catch (ObjectNotFoundException e) {
			// return null;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setSystemrolleFromSystemrolleDto(Systemrolle systemrolle,
			SystemrolleDto systemrolleDto) {
		systemrolle.setCBez(systemrolleDto.getCBez());
		em.merge(systemrolle);
		em.flush();
	}

	private void setNachrichtartFromNachrichtartDto(Nachrichtart nachrichtart,
			NachrichtartDto nachrichtartDto) {
		nachrichtart.setBArchivieren(nachrichtartDto.getBArchivieren());
		nachrichtart.setBPopup(nachrichtartDto.getBPopup());
		nachrichtart.setCBez(nachrichtartDto.getCBez());
		nachrichtart.setCNr(nachrichtartDto.getCNr());
		nachrichtart.setThemaCNr(nachrichtartDto.getThemaCNr());
		em.merge(nachrichtart);
		em.flush();
	}

	private void setNachrichtarchivFromNachrichtarchivDto(
			Nachrichtarchiv nachrichtarchiv,
			NachrichtarchivDto nachrichtarchivDto) {
		nachrichtarchiv.setCNachricht(nachrichtarchivDto.getCNachricht());
		nachrichtarchiv.setNachrichtartIId(nachrichtarchivDto
				.getNachrichtartIId());
		nachrichtarchiv.setPersonalIIdAnlegen(nachrichtarchivDto
				.getPersonalIIdAnlegen());
		nachrichtarchiv.setPersonalIIdErledigt(nachrichtarchivDto
				.getPersonalIIdErledigt());
		nachrichtarchiv.setPersonalIIdBearbeiter(nachrichtarchivDto
				.getPersonalIIdBearbeiter());
		nachrichtarchiv.setTBearbeitung(nachrichtarchivDto.getTBearbeitung());
		nachrichtarchiv.setTEledigt(nachrichtarchivDto.getTEledigt());
		nachrichtarchiv.setTZeit(nachrichtarchivDto.getTZeit());
		nachrichtarchiv.setCErledigungsgrund(nachrichtarchivDto
				.getCErledigungsgrund());
		em.merge(nachrichtarchiv);
		em.flush();
	}

	private void setThemarolleFromThemarolleDto(Themarolle themarolle,
			ThemarolleDto themarolleDto) {
		themarolle.setSystemrolleIId(themarolleDto.getSystemrolleIId());
		themarolle.setThemaCNr(themarolleDto.getThemaCNr());

		em.merge(themarolle);
		em.flush();
	}

	private void setLagerrolleFromLagerrolleDto(Lagerrolle themarolle,
			LagerrolleDto lagerolleDto) {
		themarolle.setSystemrolleIId(lagerolleDto.getSystemrolleIId());
		themarolle.setLagerIId(lagerolleDto.getLagerIId());

		em.merge(themarolle);
		em.flush();
	}

	private void setFertigungsgrupperolleFromFertigungsgrupperolleDto(Fertigungsgrupperolle fertigungsgrupperolle,
			FertigungsgrupperolleDto fertigungsgrupperolleDto) {
		fertigungsgrupperolle.setFertigungsgruppeIId(fertigungsgrupperolleDto.getFertigungsgruppeIId());
		fertigungsgrupperolle.setSystemrolleIId(fertigungsgrupperolleDto.getSystemrolleIId());

		em.merge(fertigungsgrupperolle);
		em.flush();
	}

	private SystemrolleDto assembleSystemrolleDto(Systemrolle systemrolle) {
		return SystemrolleDtoAssembler.createDto(systemrolle);
	}

	private ThemaDto assembleThemaDto(Thema thema) {
		return ThemaDtoAssembler.createDto(thema);
	}

	private NachrichtarchivDto assembleNachrichtarchivDto(
			Nachrichtarchiv nachrichtarchiv) {
		return NachrichtarchivDtoAssembler.createDto(nachrichtarchiv);
	}

	private ThemarolleDto assembleThemarolleDto(Themarolle themarolle) {
		return ThemarolleDtoAssembler.createDto(themarolle);
	}

	private FertigungsgrupperolleDto assembleFertigungsgrupperolleDto(Fertigungsgrupperolle fertigungsgrupperolle) {
		return FertigungsgrupperolleDtoAssembler.createDto(fertigungsgrupperolle);
	}
	private LagerrolleDto assembleLagerrolleDto(Lagerrolle lagerrolle) {
		return LagerrolleDtoAssembler.createDto(lagerrolle);
	}

	
	public Integer[] getBerechtigteLagerIIdsEinerSystemrolle(Integer systemrolleIId){
		Query query = em.createNamedQuery("LagerrollefindBySystemrolleIId");
		query.setParameter(1, systemrolleIId);
		LagerrolleDto[] lagerrolleDtos = assembleLagerrolleDtos(query
				.getResultList());
		Integer[] ids=new Integer[lagerrolleDtos.length];
		for (int i = 0; i < lagerrolleDtos.length; i++) {
			ids[i]=lagerrolleDtos[i].getLagerIId();
		}
		return ids;
	}
	

	public void kopiereLagerRechteEinerRolle(Integer systemrolleIIdQuelle,
			Integer systemrolleIIdZiel, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("LagerrollefindBySystemrolleIId");
		query.setParameter(1, systemrolleIIdQuelle);
		LagerrolleDto[] lagerrolleDtos = assembleLagerrolleDtos(query
				.getResultList());
		query = em.createNamedQuery("LagerrollefindBySystemrolleIIdLagerIId");
		for (int i = 0; i < lagerrolleDtos.length; i++) {
			query.setParameter(1, systemrolleIIdZiel);
			query.setParameter(2, lagerrolleDtos[i].getLagerIId());
			try {
				Lagerrolle lagerrolle = (Lagerrolle) query.getSingleResult();
			} catch (NoResultException ex2) {
				// Dann eintragen
				LagerrolleDto lagerrolleDto = new LagerrolleDto();
				lagerrolleDto.setLagerIId(lagerrolleDtos[i].getLagerIId());
				lagerrolleDto.setSystemrolleIId(systemrolleIIdZiel);
				createLagerrolle(lagerrolleDto);
			}
		}
		getBenutzerServicesFac().reloadRolleRechte();
	}

	
	private SystemrolleDto[] assembleSystemrolleDtos(Collection<?> systemrolles) {
		List<SystemrolleDto> list = new ArrayList<SystemrolleDto>();
		if (systemrolles != null) {
			Iterator<?> iterator = systemrolles.iterator();
			while (iterator.hasNext()) {
				Systemrolle systemrolle = (Systemrolle) iterator.next();
				list.add(assembleSystemrolleDto(systemrolle));
			}
		}
		SystemrolleDto[] returnArray = new SystemrolleDto[list.size()];
		return (SystemrolleDto[]) list.toArray(returnArray);
	}

	private LagerrolleDto[] assembleLagerrolleDtos(Collection<?> systemrolles) {
		List<LagerrolleDto> list = new ArrayList<LagerrolleDto>();
		if (systemrolles != null) {
			Iterator<?> iterator = systemrolles.iterator();
			while (iterator.hasNext()) {
				Lagerrolle lagerrolle = (Lagerrolle) iterator.next();
				list.add(assembleLagerrolleDto(lagerrolle));
			}
		}
		LagerrolleDto[] returnArray = new LagerrolleDto[list.size()];
		return (LagerrolleDto[]) list.toArray(returnArray);
	}

	/**
	 * Legt eine neue Benutzermandantsystemrolle in der DB an
	 * 
	 * @param benutzermandantsystemrolleDto
	 *            BenutzermandantsystemrolleDto
	 * @param theClientDto
	 *            User-ID
	 * @return Integer ID
	 * @throws EJBExceptionLP
	 */
	public Integer createBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (benutzermandantsystemrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("benutzermandantsystemrolleDto == null"));
		}
		if (benutzermandantsystemrolleDto.getBenutzerIId() == null
				|| benutzermandantsystemrolleDto.getSystemrolleIId() == null
				|| benutzermandantsystemrolleDto.getPersonalIIdZugeordnet() == null
				|| benutzermandantsystemrolleDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"benutzermandantsystemrolleDto.getBenutzerIId() == null || benutzermandantsystemrolleDto.getSystemrolleIId() == null || benutzermandantsystemrolleDto.getPersonalIIdZugeordnet() == null || benutzermandantsystemrolleDto.getMandantCNr() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIIdSystemrolleIIdMandantCNr");
			query.setParameter(1,
					benutzermandantsystemrolleDto.getBenutzerIId());
			query.setParameter(2, benutzermandantsystemrolleDto.getMandantCNr());
			// @todo getSingleResult oder getResultList ?
			Benutzermandantsystemrolle temp = (Benutzermandantsystemrolle) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BENUTZERMANDANTSYSTEMROLLE.UK"));

		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_BENUTZERMANDANTSYSTEMROLLE);
		benutzermandantsystemrolleDto.setIId(pk);
		try {
			benutzermandantsystemrolleDto.setPersonalIIdAnlegen(theClientDto
					.getIDPersonal());
			benutzermandantsystemrolleDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			benutzermandantsystemrolleDto.setTAnlegen(new Timestamp(System
					.currentTimeMillis()));
			benutzermandantsystemrolleDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Benutzermandantsystemrolle benutzermandantsystemrolle = new Benutzermandantsystemrolle(
					benutzermandantsystemrolleDto.getIId(),
					benutzermandantsystemrolleDto.getBenutzerIId(),
					benutzermandantsystemrolleDto.getSystemrolleIId(),
					benutzermandantsystemrolleDto.getMandantCNr(),
					benutzermandantsystemrolleDto.getPersonalIIdAnlegen(),
					benutzermandantsystemrolleDto.getPersonalIIdAendern(),
					benutzermandantsystemrolleDto.getPersonalIIdZugeordnet());
			em.persist(benutzermandantsystemrolle);
			em.flush();

			benutzermandantsystemrolleDto.setTAnlegen(new Timestamp(System
					.currentTimeMillis()));
			benutzermandantsystemrolleDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			setBenutzermandantsystemrolleFromBenutzermandantsystemrolleDto(
					benutzermandantsystemrolle, benutzermandantsystemrolleDto);
			return benutzermandantsystemrolleDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * L&ouml;sche eine vorhandene Benutzermandantsystemrolle aus der DB, wenn diese
	 * nicht mehr verwendet wird
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeBenutzermandantsystemrolle(Integer iId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {

			Benutzermandantsystemrolle benutzermandantsystemrolle = em.find(
					Benutzermandantsystemrolle.class, iId);
			if (benutzermandantsystemrolle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(benutzermandantsystemrolle);
			em.flush();
			// benutzermandantsystemrolle.remove();
			// }
			//
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Schreibt &Auml;nderungen einer Benutzermandantsystemrolle in der DB fest
	 * 
	 * @param benutzermandantsystemrolleDto
	 *            BenutzermandantsystemrolleDto
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 */
	public void updateBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (benutzermandantsystemrolleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("benutzermandantsystemrolleDto == null"));
		}
		if (benutzermandantsystemrolleDto.getBenutzerIId() == null
				|| benutzermandantsystemrolleDto.getSystemrolleIId() == null
				|| benutzermandantsystemrolleDto.getPersonalIIdZugeordnet() == null
				|| benutzermandantsystemrolleDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"benutzermandantsystemrolleDto.getBenutzerIId() == null || benutzermandantsystemrolleDto.getSystemrolleIId() == null || benutzermandantsystemrolleDto.getPersonalIIdZugeordnet() == null || benutzermandantsystemrolleDto.getMandantCNr() == null"));
		}
		Integer iId = benutzermandantsystemrolleDto.getIId();
		// try {
		Benutzermandantsystemrolle benutzermandantsystemrolle = em.find(
				Benutzermandantsystemrolle.class, iId);
		if (benutzermandantsystemrolle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		// try {
		Query query = em
				.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIIdSystemrolleIIdMandantCNr");
		query.setParameter(1, benutzermandantsystemrolleDto.getBenutzerIId());
		query.setParameter(2, benutzermandantsystemrolleDto.getMandantCNr());
		// @todo getSingleResult oder getResultList ?
		Integer iIdVorhanden = (Integer) ((Benutzermandantsystemrolle) query
				.getSingleResult()).getIId();

		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BENUTZERMANDANTSYSTEMROLLE.UK"));
		}

		// }
		// catch (FinderException ex) {
		//
		// }
		benutzermandantsystemrolleDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		benutzermandantsystemrolleDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		setBenutzermandantsystemrolleFromBenutzermandantsystemrolleDto(
				benutzermandantsystemrolle, benutzermandantsystemrolleDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
		// e);
		// }
	}

	/**
	 * Findet eine Benutzermandantsystemrolle anhand der eindeutigen ID
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 * @return BenutzermandantsystemrolleDto
	 */
	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		BenutzermandantsystemrolleDto dto = assembleBenutzermandantsystemrolleDto(em
				.find(Benutzermandantsystemrolle.class, iId));
		if (dto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		dto.setBenutzerDto(benutzerFindByPrimaryKey(dto.getBenutzerIId(),
				theClientDto));
		dto.setSystemrolleDto(systemrolleFindByPrimaryKey(dto
				.getSystemrolleIId()));
		return dto;

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Findet eine Benutzermandantsystemrolle anhand des eindeutigen Schl&uuml;ssels
	 * 
	 * @param benutzerIId
	 *            Integer
	 * @param mandantCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return BenutzermandantsystemrolleDto
	 */
	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
			Integer benutzerIId, String mandantCNr) throws EJBExceptionLP {
		myLogger.entry();
		if (benutzerIId == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("benutzerIId == null || mandantCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIIdSystemrolleIIdMandantCNr");
			query.setParameter(1, benutzerIId);
			query.setParameter(2, mandantCNr);
			Benutzermandantsystemrolle benutzermandantsystemrolle = (Benutzermandantsystemrolle) query
					.getSingleResult();
			return assembleBenutzermandantsystemrolleDto(benutzermandantsystemrolle);

		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerIIdMandantCNrOhneExc(
			Integer benutzerIId, String mandantCNr) throws EJBExceptionLP {
		if (benutzerIId == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("benutzerIId == null || mandantCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIIdSystemrolleIIdMandantCNr");
			query.setParameter(1, benutzerIId);
			query.setParameter(2, mandantCNr);
			Benutzermandantsystemrolle benutzermandantsystemrolle = (Benutzermandantsystemrolle) query
					.getSingleResult();
			return assembleBenutzermandantsystemrolleDto(benutzermandantsystemrolle);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Findet eine Benutzermandantsystemrolle anhand des eindeutigen Schl&uuml;ssels
	 * 
	 * @param benutzerCNr
	 *            Integer
	 * @param mandantCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return BenutzermandantsystemrolleDto
	 */
	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerCNrMandantCNr(
			String benutzerCNr, String mandantCNr) throws EJBExceptionLP {
		myLogger.entry();
		if (benutzerCNr == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("benutzerCNr == null || mandantCNr == null"));
		}
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, benutzerCNr);
			Integer benutzerIId = ((Benutzer) query.getSingleResult()).getIId();
			query = em
					.createNamedQuery("BenutzermandantsystemrollefindByBenutzerIIdSystemrolleIIdMandantCNr");
			query.setParameter(1, benutzerIId);
			query.setParameter(2, mandantCNr);
			Benutzermandantsystemrolle benutzermandantsystemrolle = (Benutzermandantsystemrolle) query
					.getSingleResult();
			return assembleBenutzermandantsystemrolleDto(benutzermandantsystemrolle);

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}
	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByMandantCNrOhneExc(String mandantCNr) {
		Query query = em.createNamedQuery(Benutzermandantsystemrolle.FindByMandantCnr);
		query.setParameter(1, mandantCNr);
		try {
			Collection<?> result = query.getResultList();
			return assembleBenutzermandantsystemrolleDtos(result);
		} catch(NoResultException ex) {
			return null;
		}
	}

	private void setBenutzermandantsystemrolleFromBenutzermandantsystemrolleDto(
			Benutzermandantsystemrolle benutzermandantsystemrolle,
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto) {
		benutzermandantsystemrolle.setBenutzerIId(benutzermandantsystemrolleDto
				.getBenutzerIId());
		benutzermandantsystemrolle
				.setSystemrolleIId(benutzermandantsystemrolleDto
						.getSystemrolleIId());
		benutzermandantsystemrolle.setMandantCNr(benutzermandantsystemrolleDto
				.getMandantCNr());

		benutzermandantsystemrolle.setTAnlegen(benutzermandantsystemrolleDto
				.getTAnlegen());
		benutzermandantsystemrolle
				.setPersonalIIdAnlegen(benutzermandantsystemrolleDto
						.getPersonalIIdAnlegen());
		benutzermandantsystemrolle.setTAendern(benutzermandantsystemrolleDto
				.getTAendern());
		benutzermandantsystemrolle
				.setPersonalIIdAendern(benutzermandantsystemrolleDto
						.getPersonalIIdAendern());
		benutzermandantsystemrolle
				.setPersonalIIdZugeordnet(benutzermandantsystemrolleDto
						.getPersonalIIdZugeordnet());
		em.merge(benutzermandantsystemrolle);
		em.flush();
	}

	private BenutzermandantsystemrolleDto assembleBenutzermandantsystemrolleDto(
			Benutzermandantsystemrolle benutzermandantsystemrolle) {
		return BenutzermandantsystemrolleDtoAssembler
				.createDto(benutzermandantsystemrolle);
	}

	private BenutzermandantsystemrolleDto[] assembleBenutzermandantsystemrolleDtos(
			Collection<?> benutzermandantsystemrolles) {
		List<BenutzermandantsystemrolleDto> list = new ArrayList<BenutzermandantsystemrolleDto>();
		if (benutzermandantsystemrolles != null) {
			Iterator<?> iterator = benutzermandantsystemrolles.iterator();
			while (iterator.hasNext()) {
				Benutzermandantsystemrolle benutzermandantsystemrolle = (Benutzermandantsystemrolle) iterator
						.next();
				list.add(assembleBenutzermandantsystemrolleDto(benutzermandantsystemrolle));
			}
		}
		BenutzermandantsystemrolleDto[] returnArray = new BenutzermandantsystemrolleDto[list
				.size()];
		return (BenutzermandantsystemrolleDto[]) list.toArray(returnArray);
	}
}
