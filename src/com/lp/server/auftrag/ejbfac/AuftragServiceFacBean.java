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
package com.lp.server.auftrag.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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

import com.lp.server.auftrag.ejb.Auftragart;
import com.lp.server.auftrag.ejb.Auftragartspr;
import com.lp.server.auftrag.ejb.AuftragartsprPK;
import com.lp.server.auftrag.ejb.Auftragauftragdokument;
import com.lp.server.auftrag.ejb.Auftragbegruendung;
import com.lp.server.auftrag.ejb.Auftragdokument;
import com.lp.server.auftrag.ejb.Auftragpositionart;
import com.lp.server.auftrag.ejb.Auftragpositionstatus;
import com.lp.server.auftrag.ejb.Auftragstatus;
import com.lp.server.auftrag.ejb.Auftragtext;
import com.lp.server.auftrag.ejb.Auftragwiederholungsintervall;
import com.lp.server.auftrag.ejb.Auftragwiederholungsintervallspr;
import com.lp.server.auftrag.ejb.AuftragwiederholungsintervallsprPK;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragstatus;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragStatusDto;
import com.lp.server.auftrag.service.AuftragStatusDtoAssembler;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragartDtoAssembler;
import com.lp.server.auftrag.service.AuftragartsprDto;
import com.lp.server.auftrag.service.AuftragartsprDtoAssembler;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragauftragdokumentDtoAssembler;
import com.lp.server.auftrag.service.AuftragbegruendungDto;
import com.lp.server.auftrag.service.AuftragbegruendungDtoAssembler;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragdokumentDtoAssembler;
import com.lp.server.auftrag.service.AuftragpositionArtDto;
import com.lp.server.auftrag.service.AuftragpositionArtDtoAssembler;
import com.lp.server.auftrag.service.AuftragpositionstatusDto;
import com.lp.server.auftrag.service.AuftragpositionstatusDtoAssembler;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.auftrag.service.AuftragtextDtoAssembler;
import com.lp.server.auftrag.service.AuftragwiederholungsintervallDto;
import com.lp.server.auftrag.service.AuftragwiederholungsintervallDtoAssembler;
import com.lp.server.auftrag.service.AuftragwiederholungsintervallsprDto;
import com.lp.server.auftrag.service.AuftragwiederholungsintervallsprDtoAssembler;

import com.lp.server.system.fastlanereader.generated.FLRStatusspr;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AuftragServiceFacBean extends Facade implements AuftragServiceFac {
	@PersistenceContext
	private EntityManager em;

	public String createAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragartDto(auftragartDtoI);
		try {
			Auftragart auftragart = new Auftragart(auftragartDtoI.getCNr(),
					auftragartDtoI.getISort());
			em.persist(auftragart);
			em.flush();
			setAuftragartFromAuftragartDto(auftragart, auftragartDtoI);

			if (auftragartDtoI.getAuftragartsprDto() != null) {
				auftragartDtoI.getAuftragartsprDto().setAuftragartCNr(
						auftragartDtoI.getCNr());
				createAuftragartspr(auftragartDtoI.getAuftragartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return auftragartDtoI.getCNr();
	}

	
	

	public Integer createAuftragbegruendung(AuftragbegruendungDto auftragbegruendungDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_AUFTRAGBEGRUENDUNG);
		auftragbegruendungDto.setIId(iId);
		try {

			try {
				Query query = em.createNamedQuery("AuftragbegruendungFindByCNr");
				query.setParameter(1, auftragbegruendungDto.getCNr());
				Auftragbegruendung doppelt = (Auftragbegruendung) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_BEGRUENDUNG.C_NR"));
			} catch (NoResultException ex) {

			}

			Auftragbegruendung begruendung = new Auftragbegruendung(auftragbegruendungDto.getIId(),
					auftragbegruendungDto.getCNr(), auftragbegruendungDto.getCBez());
			em.persist(begruendung);
			em.flush();
			setAuftragbegruendungFromAuftragbegruendungDto(begruendung, auftragbegruendungDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iId;
	}

	public void updateAuftragbegruendung(AuftragbegruendungDto begruendungDto) {

		try {
			Integer iId = begruendungDto.getIId();
			// try {
			Auftragbegruendung begruendung = em.find(Auftragbegruendung.class, iId);

			try {
				Query query = em.createNamedQuery("AuftragbegruendungFindByCNr");
				query.setParameter(1, begruendungDto.getCNr());
				Auftragbegruendung vorhanden = (Auftragbegruendung) query.getSingleResult();
				if (vorhanden != null
						&& iId.equals(vorhanden.getIId()) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("AUFT_AUFTRAGBEGRUENDUNG.C_NR"));
				}

			} catch (NoResultException ex) {
			}

			setAuftragbegruendungFromAuftragbegruendungDto(begruendung, begruendungDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	
	}

	public void removeAuftragbegruendung(Integer iId) {
		Auftragbegruendung toRemove = em.find(Auftragbegruendung.class, iId);
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

	public AuftragbegruendungDto auftragbegruendungFindByPrimaryKey(Integer iId) {
		Auftragbegruendung begruendung = em.find(Auftragbegruendung.class, iId);
		if (begruendung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragbegruendungDto(begruendung);
	}
	private AuftragbegruendungDto assembleAuftragbegruendungDto(Auftragbegruendung begruendung) {
		return AuftragbegruendungDtoAssembler.createDto(begruendung);
	}
	private void setAuftragbegruendungFromAuftragbegruendungDto(Auftragbegruendung begruendung,
			AuftragbegruendungDto begruendungDto) {
		begruendung.setCNr(begruendungDto.getCNr());
		begruendung.setCBez(begruendungDto.getCBez());
		em.merge(begruendung);
		em.flush();
	}

	
	
	
	
	private void checkAuftragartDto(AuftragartDto auftragartDtoI)
			throws EJBExceptionLP {
		if (auftragartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("auftragartDtoI == null"));
		}

		myLogger.info("AuftragartDto: " + auftragartDtoI.toString());
	}

	public void removeAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragartDto(auftragartDtoI);
		try {
			Query query = em
					.createNamedQuery("AuftragartsprfindByAuftragartCNr");
			query.setParameter(1, auftragartDtoI.getCNr());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Auftragartspr item = (Auftragartspr) iter.next();
				em.remove(item);
			}
			Auftragart auftragart = em.find(Auftragart.class, auftragartDtoI
					.getCNr());
			if (auftragart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(auftragart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateAuftragart(AuftragartDto auftragartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragartDto(auftragartDtoI);
		String cNr = auftragartDtoI.getCNr();
		try {
			Auftragart auftragart = em.find(Auftragart.class, cNr);
			if (auftragart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setAuftragartFromAuftragartDto(auftragart, auftragartDtoI);

			// jetzt die Spr
			AuftragartsprDto auftragartsprDto = auftragartDtoI
					.getAuftragartsprDto();

			if (auftragartsprDto != null
					&& auftragartsprDto.getAuftragartCNr() != null) {
				AuftragartsprPK auftragartsprPK = new AuftragartsprPK();
				auftragartsprPK.setLocaleCNr(auftragartsprDto.getLocaleCNr());
				auftragartsprPK.setAuftragartCNr(auftragartsprDto
						.getAuftragartCNr());

				Auftragartspr auftragartspr = em.find(Auftragartspr.class,
						auftragartsprPK);

				setAuftragartsprFromAuftragartsprDto(auftragartspr,
						auftragartsprDto);
			} else {
				Auftragartspr auftragartspr = new Auftragartspr(auftragartDtoI
						.getAuftragartsprDto().getLocaleCNr(), auftragartDtoI
						.getCNr(), auftragartDtoI.getAuftragartsprDto()
						.getCBez());
				em.persist(auftragartspr);
				em.flush();

				setAuftragartsprFromAuftragartsprDto(auftragartspr,
						auftragartDtoI.getAuftragartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void updateAuftragdokumente(Integer auftragIId,
			ArrayList<AuftragdokumentDto> dtos) {

		// Zuerst alle loeschen

		Query query = em
				.createNamedQuery("AuftragauftragdokumentfindByAuftragIId");
		query.setParameter(1, auftragIId);
		Collection<?> cl = query.getResultList();

		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Auftragauftragdokument auftragauftragdokument = (Auftragauftragdokument) iterator
						.next();

				em.remove(auftragauftragdokument);
				em.flush();
			}
		}

		// Und dann wieder anlegen

		if (auftragIId != null && dtos != null) {
			for (int i = 0; i < dtos.size(); i++) {
				Integer iId = getPKGeneratorObj().getNextPrimaryKey(
						PKConst.PK_AUFTRAGAUFTRAGDOKUMENT);
				Auftragauftragdokument auftragauftragdokument = new Auftragauftragdokument(
						iId, auftragIId, dtos.get(i).getIId());
				em.merge(auftragauftragdokument);
				em.flush();

			}
		}

	}

	public AuftragartDto auftragartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}
		AuftragartDto auftragartDto = null;
		Auftragart auftragart = em.find(Auftragart.class, cNrI);
		if (auftragart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		auftragartDto = assembleAuftragartDto(auftragart);
		// jetzt die Spr
		AuftragartsprDto auftragartsprDto = null;

		try {
			AuftragartsprPK auftragartsprPK = new AuftragartsprPK(theClientDto
					.getLocUiAsString(), auftragartDto.getCNr());
			Auftragartspr auftragartspr = em.find(Auftragartspr.class,
					auftragartsprPK);
			if (auftragartspr != null) {
				auftragartsprDto = assembleAuftragartsprDto(auftragartspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		auftragartDto.setAuftragartsprDto(auftragartsprDto);
		return auftragartDto;
	}

	public AuftragartDto[] auftragartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("AuftragartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleAuftragartDtos(cl);
	}

	/**
	 * Alle Auftragarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAuftragart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getAuftragart";
		myLogger.entry();
		Map<String, String> map = null;
		Query query = em.createNamedQuery("AuftragartfindAll");
		Collection<?> arten = query.getResultList();
		AuftragartDto[] artDtos = this.assembleAuftragartDtos(arten);
		map = this.uebersetzeAuftragartOptimal(artDtos, locale1, locale2);
		return map;
	}

	public HashMap<String, String> getAuftragStatus(Locale locale,
			String cNrUser) throws EJBExceptionLP {
		long l = System.currentTimeMillis();
		myLogger.entry();
		TheClientDto theClientDto = check(cNrUser);
		String sLocale = Helper.locale2String(locale);
		HashMap<String, String> hm = new HashMap<String, String>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cStatus = session.createCriteria(FLRAuftragstatus.class);
			List<?> lProjektStatus = cStatus.list();
			for (Iterator<?> iter = lProjektStatus.iterator(); iter.hasNext();) {
				FLRAuftragstatus status = (FLRAuftragstatus) iter.next();
				Iterator<?> sprsetIterator = status
						.getAuftragstatus_statusspr_set().iterator();
				while (sprsetIterator.hasNext()) {
					FLRStatusspr statusspr = (FLRStatusspr) sprsetIterator
							.next();
					if (statusspr.getLocale().getC_nr().trim().equals(
							sLocale.trim())) {
						hm.put(statusspr.getStatus().getC_nr(), statusspr
								.getC_bez());
						break;
					}
				}

			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		System.out.print("hibernate" + (System.currentTimeMillis() - l)
				+ " ms.");
		return hm;
	}

	/**
	 * Alle Auftragarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAuftragwiederholungsintervall(Locale locale1)
			throws EJBExceptionLP {
		myLogger.entry();

		Map<String, String> map = new TreeMap<String, String>();
		Query query = em
				.createNamedQuery("AuftragwiederholungsintervallfindAll");
		Collection<?> arten = query.getResultList();
		AuftragwiederholungsintervallDto[] artDtos = this
				.assembleAuftragwiederholungsintervallDtos(arten);
		for (int i = 0; i < artDtos.length; i++) {
			String key = artDtos[i].getCNr();
			String locale = Helper.locale2String(locale1);
			String value = null;
			try {
				query = em
						.createNamedQuery("AuftragwiederholungsintervallsprfindBySpracheAndCNr");
				query.setParameter(1, locale);
				query.setParameter(2, key);
				Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = (Auftragwiederholungsintervallspr) query
						.getSingleResult();
				value = assembleAuftragwiederholungsintervallsprDto(
						auftragwiederholungsintervallspr).getCBez();
			} catch (NoResultException ex1) {
				value = artDtos[i].getCNr();
			}

			map.put(key, value);
		}
		return map;
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von Auftragarten.
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	private Map<String, String> uebersetzeAuftragartOptimal(
			AuftragartDto[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "uebersetzeAuftragartOptimal";
		myLogger.entry();

		// @todo check param PJ 3810

		Map<String, String> uebersetzung = new TreeMap<String, String>();

		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = this.uebersetzeAuftragartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			uebersetzung.put(key, value);
		}

		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Auftragart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            der Name der Auftragart
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            Locale Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Auftragarten mit Uebersetzung
	 */
	private String uebersetzeAuftragartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "uebersetzeAuftragartOptimal";
		myLogger.entry();

		String uebersetzung = "";

		try {
			uebersetzung = this.uebersetzeAuftragart(locale1, cNr);
		} catch (Throwable t1) {
			try {
				uebersetzung = this.uebersetzeAuftragart(locale2, cNr);
			} catch (Throwable t2) {
				uebersetzung = cNr;
			}
		}

		return uebersetzung;
	}

	private void setAuftragartFromAuftragartDto(Auftragart auftragart,
			AuftragartDto auftragartDto) {
		auftragart.setISort(auftragartDto.getISort());
		em.merge(auftragart);
		em.flush();
	}

	private AuftragartDto assembleAuftragartDto(Auftragart auftragart) {
		return AuftragartDtoAssembler.createDto(auftragart);
	}

	private AuftragartDto[] assembleAuftragartDtos(Collection<?> auftragarts) {
		List<AuftragartDto> list = new ArrayList<AuftragartDto>();
		if (auftragarts != null) {
			Iterator<?> iterator = auftragarts.iterator();
			while (iterator.hasNext()) {
				Auftragart auftragart = (Auftragart) iterator.next();
				list.add(assembleAuftragartDto(auftragart));
			}
		}
		AuftragartDto[] returnArray = new AuftragartDto[list.size()];
		return (AuftragartDto[]) list.toArray(returnArray);
	}

	public void createAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws EJBExceptionLP {
		if (auftragartsprDto == null) {
			return;
		}
		try {
			Auftragartspr auftragartspr = new Auftragartspr(auftragartsprDto
					.getLocaleCNr(), auftragartsprDto.getAuftragartCNr(),
					auftragartsprDto.getCBez());
			em.persist(auftragartspr);
			em.flush();
			setAuftragartsprFromAuftragartsprDto(auftragartspr,
					auftragartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws EJBExceptionLP {
	}

	public void updateAuftragartspr(AuftragartsprDto auftragartsprDto)
			throws EJBExceptionLP {
		if (auftragartsprDto != null) {
			AuftragartsprPK auftragartsprPK = new AuftragartsprPK();
			auftragartsprPK.setLocaleCNr(auftragartsprDto.getLocaleCNr());
			auftragartsprPK.setAuftragartCNr(auftragartsprDto
					.getAuftragartCNr());
			Auftragartspr auftragartspr = em.find(Auftragartspr.class,
					auftragartsprPK);
			if (auftragartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragartsprFromAuftragartsprDto(auftragartspr,
					auftragartsprDto);
		}
	}

	/**
	 * Eine Auftragart in eine bestimmte Sprache uebersetzen.
	 * 
	 * @param pLocale
	 *            Locale
	 * @param pArt
	 *            String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	private String uebersetzeAuftragart(Locale pLocale, String pArt)
			throws EJBExceptionLP {
		final String METHOD_NAME = "uebersetzeAuftragart";
		myLogger.entry();
		Auftragartspr spr = null;
		String locale = Helper.locale2String(pLocale);
		Query query = em.createNamedQuery("AuftragartsprfindBySpracheAndCNr");
		query.setParameter(1, locale);
		query.setParameter(2, pArt);
		spr = (Auftragartspr) query.getSingleResult();
		if (spr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return spr.getCBez();
	}

	public AuftragartsprDto auftragartsprFindByPrimaryKey(String spracheCNr,
			String auftragartCNr) throws EJBExceptionLP {
		AuftragartsprPK auftragartsprPK = new AuftragartsprPK();
		auftragartsprPK.setLocaleCNr(spracheCNr);
		auftragartsprPK.setAuftragartCNr(auftragartCNr);
		Auftragartspr auftragartspr = em.find(Auftragartspr.class,
				auftragartsprPK);
		if (auftragartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragartsprDto(auftragartspr);
	}

	public AuftragartsprDto auftragartsprFindBySpracheAndCNr(String pSprache,
			String pNummer) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("AuftragartsprfindBySpracheAndCNr");
			query.setParameter(1, pSprache);
			query.setParameter(2, pNummer);
			// @todo getSingleResult oder getResultList ?
			Auftragartspr auftragartspr = (Auftragartspr) query
					.getSingleResult();

			return assembleAuftragartsprDto(auftragartspr);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	private void setAuftragartsprFromAuftragartsprDto(
			Auftragartspr auftragartspr, AuftragartsprDto auftragartsprDto) {
		auftragartspr.setCBez(auftragartsprDto.getCBez());
		em.merge(auftragartspr);
		em.flush();
	}

	private AuftragartsprDto assembleAuftragartsprDto(
			Auftragartspr auftragartspr) {
		return AuftragartsprDtoAssembler.createDto(auftragartspr);
	}

	private AuftragartsprDto[] assembleAuftragartsprDtos(
			Collection<?> auftragartsprs) {
		List<AuftragartsprDto> list = new ArrayList<AuftragartsprDto>();
		if (auftragartsprs != null) {
			Iterator<?> iterator = auftragartsprs.iterator();
			while (iterator.hasNext()) {
				Auftragartspr auftragartspr = (Auftragartspr) iterator.next();
				list.add(assembleAuftragartsprDto(auftragartspr));
			}
		}
		AuftragartsprDto[] returnArray = new AuftragartsprDto[list.size()];
		return (AuftragartsprDto[]) list.toArray(returnArray);
	}

	private AuftragdokumentDto[] assembleAuftragdokumentDtos(
			Collection<?> auftragartsprs) {
		List<AuftragdokumentDto> list = new ArrayList<AuftragdokumentDto>();
		if (auftragartsprs != null) {
			Iterator<?> iterator = auftragartsprs.iterator();
			while (iterator.hasNext()) {
				Auftragdokument auftragartspr = (Auftragdokument) iterator
						.next();
				list.add(assembleAuftragdokumentDto(auftragartspr));
			}
		}
		AuftragdokumentDto[] returnArray = new AuftragdokumentDto[list.size()];
		return (AuftragdokumentDto[]) list.toArray(returnArray);
	}

	// Auftragpositionart
	// --------------------------------------------------------

	public String createAuftragpositionArt(
			AuftragpositionArtDto auftragpositionArtDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragpositionartDto(auftragpositionArtDtoI);
		try {
			Auftragpositionart auftragpositionart = new Auftragpositionart(
					auftragpositionArtDtoI.getCNr(), auftragpositionArtDtoI
							.getISort(), auftragpositionArtDtoI.getBVersteckt());
			em.persist(auftragpositionart);
			em.flush();
			setAuftragpositionArtFromAuftragpositionArtDto(auftragpositionart,
					auftragpositionArtDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return auftragpositionArtDtoI.getCNr();
	}

	public void removeAuftragpositionArt(String cNrAuftragpositionartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragpositionartCNr(cNrAuftragpositionartI);
		Auftragpositionart toRemove = em.find(Auftragpositionart.class,
				cNrAuftragpositionartI);
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

	public void updateAuftragpositionArt(
			AuftragpositionArtDto auftragpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragpositionartDto(auftragpositionartDtoI);
		try {
			Auftragpositionart auftragpositionart = em.find(
					Auftragpositionart.class, auftragpositionartDtoI.getCNr());
			if (auftragpositionart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setAuftragpositionArtFromAuftragpositionArtDto(auftragpositionart,
					auftragpositionartDtoI);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}
	}

	public AuftragpositionArtDto auftragpositionartFindByPrimaryKey(
			String cNrAuftragpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragpositionArtDto auftragpositionartDto = null;
		Auftragpositionart auftragpositionart = em.find(
				Auftragpositionart.class, cNrAuftragpositionartI);
		if (auftragpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		auftragpositionartDto = assembleAuftragpositionArtDto(auftragpositionart);
		return auftragpositionartDto;
	}

	public Map<String, String> auftragpositionartFindAll(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLocale(locale1I);
		checkLocale(locale2I);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			Query query = em
					.createNamedQuery("AuftragpositionArtfindAllEnable");
			Collection<?> cl = query.getResultList();
			AuftragpositionArtDto[] aAuftragpositionartDto = assembleAuftragpositionArtDtos(cl);

			for (int i = 0; i < aAuftragpositionartDto.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(
								aAuftragpositionartDto[i].getCNr(), locale1I,
								locale2I);
				map.put(aAuftragpositionartDto[i].getCNr(), sUebersetzung);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return map;
	}

	public Map getAuftragpositionart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		myLogger.entry();

		// @todo param PJ 3810

		Map<?, ?> map = null;

		try {
			Query query = em.createNamedQuery("AuftragpositionArtfindAll");
			Collection<?> arten = query.getResultList();
			if (arten == null) { // @ToDo FinderException
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, "");
			}
			AuftragpositionArtDto[] artenDtos = this
					.assembleAuftragpositionArtDtos(arten);
			map = getSystemMultilanguageFac().uebersetzePositionsartOptimal(
					artenDtos, locale1, locale2);
			// }
			// catch (FinderException ex) {

			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return map;
	}

	private void checkAuftragpositionartDto(
			AuftragpositionArtDto auftragpositionartDtoI) throws EJBExceptionLP {
		if (auftragpositionartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("auftragpositionartDtoI == null"));
		}

		myLogger.info("AuftragpositionartDtoI: "
				+ auftragpositionartDtoI.toString());
	}

	private void checkAuftragpositionartCNr(String cNrAuftragpositionartI)
			throws EJBExceptionLP {
		if (cNrAuftragpositionartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAuftragpositionartI == null"));
		}

		myLogger.info("AuftragpositionartCNr: " + cNrAuftragpositionartI);
	}

	private void setAuftragpositionArtFromAuftragpositionArtDto(
			Auftragpositionart auftragpositionart,
			AuftragpositionArtDto auftragpositionArtDto) {
		auftragpositionart.setISort(auftragpositionArtDto.getISort());
		auftragpositionart.setBVersteckt(auftragpositionArtDto.getBVersteckt());
		em.merge(auftragpositionart);
		em.flush();
	}

	private AuftragpositionArtDto assembleAuftragpositionArtDto(
			Auftragpositionart auftragpositionArt) {
		return AuftragpositionArtDtoAssembler.createDto(auftragpositionArt);
	}

	private AuftragpositionArtDto[] assembleAuftragpositionArtDtos(
			Collection<?> auftragpositionArts) {
		List<AuftragpositionArtDto> list = new ArrayList<AuftragpositionArtDto>();
		if (auftragpositionArts != null) {
			Iterator<?> iterator = auftragpositionArts.iterator();
			while (iterator.hasNext()) {
				Auftragpositionart auftragpositionArt = (Auftragpositionart) iterator
						.next();
				list.add(assembleAuftragpositionArtDto(auftragpositionArt));
			}
		}
		AuftragpositionArtDto[] returnArray = new AuftragpositionArtDto[list
				.size()];
		return (AuftragpositionArtDto[]) list.toArray(returnArray);
	}

	public void createAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws EJBExceptionLP {
		if (auftragpositionstatusDto == null) {
			return;
		}
		try {
			Auftragpositionstatus auftragpositionstatus = new Auftragpositionstatus(
					auftragpositionstatusDto.getStatusCNr(),
					auftragpositionstatusDto.getISort());
			em.persist(auftragpositionstatus);
			em.flush();
			setAuftragpositionstatusFromAuftragpositionstatusDto(
					auftragpositionstatus, auftragpositionstatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAuftragpositionstatus(String statusCNr)
			throws EJBExceptionLP {
		// try {
		Auftragpositionstatus toRemove = em.find(Auftragpositionstatus.class,
				statusCNr);
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws EJBExceptionLP {
		if (auftragpositionstatusDto != null) {
			String statusCNr = auftragpositionstatusDto.getStatusCNr();
			removeAuftragpositionstatus(statusCNr);
		}
	}

	public void updateAuftragpositionstatus(
			AuftragpositionstatusDto auftragpositionstatusDto)
			throws EJBExceptionLP {
		if (auftragpositionstatusDto != null) {
			String statusCNr = auftragpositionstatusDto.getStatusCNr();
			// try {
			Auftragpositionstatus auftragpositionstatus = em.find(
					Auftragpositionstatus.class, statusCNr);
			if (auftragpositionstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragpositionstatusFromAuftragpositionstatusDto(
					auftragpositionstatus, auftragpositionstatusDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	/**
	 * Einen Auftragpositionstatus in seiner Uebersetzung holen.
	 * 
	 * @param pStatus
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String getAuftragpositionstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "getAuftragpositionstatus";
		myLogger.entry();

		// @todo check parameter PJ 3810

		String uebersetzung = null;

		try {
			Auftragpositionstatus status = this.em.find(
					Auftragpositionstatus.class, pStatus);
			if (status == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			AuftragpositionstatusDto statusDto = this
					.assembleAuftragpositionstatusDto(status);

			// uebersetzung von system holen
			uebersetzung = getSystemMultilanguageFac().uebersetzeStatusOptimal(
					pStatus, locale1, locale2);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return uebersetzung;
	}

	public AuftragpositionstatusDto auftragpositionstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP {
		// try {
		Auftragpositionstatus auftragpositionstatus = em.find(
				Auftragpositionstatus.class, statusCNr);
		if (auftragpositionstatus == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragpositionstatusDto(auftragpositionstatus);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setAuftragpositionstatusFromAuftragpositionstatusDto(
			Auftragpositionstatus auftragpositionstatus,
			AuftragpositionstatusDto auftragpositionstatusDto) {
		auftragpositionstatus.setISort(auftragpositionstatusDto.getISort());
		em.merge(auftragpositionstatus);
		em.flush();
	}

	private AuftragpositionstatusDto assembleAuftragpositionstatusDto(
			Auftragpositionstatus auftragpositionstatus) {
		return AuftragpositionstatusDtoAssembler
				.createDto(auftragpositionstatus);
	}

	private AuftragpositionstatusDto[] assembleAuftragpositionstatusDtos(
			Collection<?> auftragpositionstatuss) {
		List<AuftragpositionstatusDto> list = new ArrayList<AuftragpositionstatusDto>();
		if (auftragpositionstatuss != null) {
			Iterator<?> iterator = auftragpositionstatuss.iterator();
			while (iterator.hasNext()) {
				Auftragpositionstatus auftragpositionstatus = (Auftragpositionstatus) iterator
						.next();
				list
						.add(assembleAuftragpositionstatusDto(auftragpositionstatus));
			}
		}
		AuftragpositionstatusDto[] returnArray = new AuftragpositionstatusDto[list
				.size()];
		return (AuftragpositionstatusDto[]) list.toArray(returnArray);
	}

	public void createAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws EJBExceptionLP {
		if (auftragStatusDto == null) {
			return;
		}
		try {
			Auftragstatus auftragStatus = new Auftragstatus(auftragStatusDto
					.getStatusCNr(), auftragStatusDto.getISort());
			em.persist(auftragStatus);
			em.flush();
			setAuftragStatusFromAuftragStatusDto(auftragStatus,
					auftragStatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAuftragStatus(String statusCNr) throws EJBExceptionLP {
		// try {
		Auftragstatus toRemove = em.find(Auftragstatus.class, statusCNr);
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws EJBExceptionLP {
		if (auftragStatusDto != null) {
			String statusCNr = auftragStatusDto.getStatusCNr();
			removeAuftragStatus(statusCNr);
		}
	}

	public void updateAuftragStatus(AuftragStatusDto auftragStatusDto)
			throws EJBExceptionLP {
		if (auftragStatusDto != null) {
			String statusCNr = auftragStatusDto.getStatusCNr();
			// try {
			Auftragstatus auftragStatus = em.find(Auftragstatus.class,
					statusCNr);
			if (auftragStatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragStatusFromAuftragStatusDto(auftragStatus,
					auftragStatusDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public AuftragStatusDto auftragStatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		// try {
		Auftragstatus auftragstatus = em.find(Auftragstatus.class, statusCNr);
		if (auftragstatus == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragStatusDto(auftragstatus);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public AuftragStatusDto[] auftragStatusFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("AuftragStatusfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleAuftragStatusDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
	}

	/**
	 * Einen Auftragstatus in seiner Uebersetzung holen.
	 * 
	 * @param pStatus
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String getAuftragstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "getAuftragstatus";
		myLogger.entry();

		// @todo check parameter PJ 3810

		String uebersetzung = null;

		try {
			Auftragstatus status = em.find(Auftragstatus.class, pStatus);
			if (status == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			AuftragStatusDto statusDto = this.assembleAuftragStatusDto(status);

			// uebersetzung von system holen
			uebersetzung = getSystemMultilanguageFac().uebersetzeStatusOptimal(
					pStatus, locale1, locale2);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return uebersetzung;
	}

	private void setAuftragStatusFromAuftragStatusDto(
			Auftragstatus auftragstatus, AuftragStatusDto auftragStatusDto) {
		auftragstatus.setISort(auftragStatusDto.getISort());
		em.merge(auftragstatus);
		em.flush();
	}

	private AuftragStatusDto assembleAuftragStatusDto(
			Auftragstatus auftragStatus) {
		return AuftragStatusDtoAssembler.createDto(auftragStatus);
	}

	private AuftragStatusDto[] assembleAuftragStatusDtos(
			Collection<?> auftragStatuss) {
		List<AuftragStatusDto> list = new ArrayList<AuftragStatusDto>();
		if (auftragStatuss != null) {
			Iterator<?> iterator = auftragStatuss.iterator();
			while (iterator.hasNext()) {
				Auftragstatus auftragStatus = (Auftragstatus) iterator.next();
				list.add(assembleAuftragStatusDto(auftragStatus));
			}
		}
		AuftragStatusDto[] returnArray = new AuftragStatusDto[list.size()];
		return (AuftragStatusDto[]) list.toArray(returnArray);
	}

	// Auftragtext
	// ---------------------------------------------------------------

	public Integer createAuftragtext(AuftragtextDto auftragtextDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "createAuftragtext";
		myLogger.entry();
		checkAuftragtextDto(auftragtextDto);

		// den PK erzeugen und setzen
		Integer iId = null;

		iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUFTRAGTEXT);
		auftragtextDto.setIId(iId);

		try {
			Auftragtext auftragtext = new Auftragtext(auftragtextDto.getIId(),
					auftragtextDto.getMandantCNr(), auftragtextDto
							.getLocaleCNr(), auftragtextDto.getMediaartCNr(),
					auftragtextDto.getCTextinhalt());
			em.persist(auftragtext);
			em.flush();
			setAuftragtextFromAuftragtextDto(auftragtext, auftragtextDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public Integer createAuftragdokument(AuftragdokumentDto auftragdokumentDto) {
		// den PK erzeugen und setzen

		if (auftragdokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragdokumentDto == null"));
		}
		if (auftragdokumentDto.getCNr() == null
				|| auftragdokumentDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"auftragdokumentDto.getCNr() == null || auftragdokumentDto.getBVersteckt() == null"));
		}
		try {
			Query query = em.createNamedQuery("AuftragdokumentfindByCNr");
			query.setParameter(1, auftragdokumentDto.getCNr());
			Auftragdokument doppelt = (Auftragdokument) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("AUFT_AUFTRAGDOKUMENT.CNR"));

		} catch (NoResultException ex) {

		}

		Integer iId = null;

		iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUFTRAGDOKUMENT);
		auftragdokumentDto.setIId(iId);

		try {
			Auftragdokument auftragdokument = new Auftragdokument(
					auftragdokumentDto.getIId(), auftragdokumentDto.getCNr(),
					auftragdokumentDto.getCBez(), auftragdokumentDto
							.getBVersteckt());
			em.persist(auftragdokument);
			em.flush();
			setAuftragdokumentFromAuftragdokumentDto(auftragdokument,
					auftragdokumentDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	/**
	 * Einen Auftragtext in der db aktualisieren.
	 * 
	 * @param auftragtextDto
	 *            AuftragtextDto
	 * @throws EJBExceptionLP
	 */
	public void updateAuftragtext(AuftragtextDto auftragtextDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "updateAuftragtext";
		myLogger.entry();
		checkAuftragtextDto(auftragtextDto);

		// try {
		Auftragtext auftragtext = em.find(Auftragtext.class, auftragtextDto
				.getIId());
		if (auftragtext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setAuftragtextFromAuftragtextDto(auftragtext, auftragtextDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void updateAuftragdokument(AuftragdokumentDto auftragdokumentDto) {

		if (auftragdokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragdokumentDto == null"));
		}
		if (auftragdokumentDto.getIId() == null
				|| auftragdokumentDto.getCNr() == null
				|| auftragdokumentDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"auftragdokumentDto.getIId() == null || auftragdokumentDto.getCNr() == null || auftragdokumentDto.getBVersteckt() == null"));
		}
		Integer iId = auftragdokumentDto.getIId();

		Auftragdokument auftragdokument = em.find(Auftragdokument.class, iId);

		try {
			Query query = em.createNamedQuery("AuftragdokumentfindByCNr");
			query.setParameter(1, auftragdokumentDto.getCNr());
			Integer iIdVorhanden = ((Auftragdokument) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"AUFT_AUFTRAGDOKUMENT.CNR"));
			}

		} catch (NoResultException ex) {

		}

		setAuftragdokumentFromAuftragdokumentDto(auftragdokument,
				auftragdokumentDto);

	}

	public void removeAuftragtext(AuftragtextDto auftragtextDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "removeAuftragtext";
		myLogger.entry();
		checkAuftragtextDto(auftragtextDto);

		// try {
		Auftragtext toRemove = em.find(Auftragtext.class, auftragtextDto
				.getIId());
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeAuftragdokument(AuftragdokumentDto auftragdokumentDto) {
		Auftragdokument toRemove = em.find(Auftragdokument.class,
				auftragdokumentDto.getIId());
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

	/**
	 * Einen Auftragtext ueber einen Schluessel finden.
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return AuftragtextDto
	 */
	public AuftragtextDto auftragtextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}

		AuftragtextDto textDto = null;

		// try {
		Auftragtext text = em.find(Auftragtext.class, iId);
		if (text == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		textDto = assembleAuftragtextDto(text);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return textDto;
	}

	public AuftragdokumentDto auftragdokumentFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}

		AuftragdokumentDto auftragdokumentDto = null;

		Auftragdokument auftragdokument = em.find(Auftragdokument.class, iId);
		if (auftragdokument == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		auftragdokumentDto = assembleAuftragdokumentDto(auftragdokument);

		return auftragdokumentDto;
	}

	public AuftragauftragdokumentDto[] auftragauftragdokumentFindByAuftragIId(
			Integer auftragIId) {
		if (auftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}

		AuftragauftragdokumentDto[] auftragauftragdokumentDtos = null;

		Query query = em
				.createNamedQuery("AuftragauftragdokumentfindByAuftragIId");
		query.setParameter(1, auftragIId);
		Collection cl = query.getResultList();

		auftragauftragdokumentDtos = assembleAuftragauftragdokumentDtos(cl);

		return auftragauftragdokumentDtos;
	}

	public AuftragdokumentDto[] auftragdokumentFindByBVersteckt() {

		AuftragdokumentDto[] auftragdokumentDtos = null;

		Query query = em.createNamedQuery("AuftragdokumentfindByBVersteckt");
		query.setParameter(1, Helper.boolean2Short(false));
		Collection cl = query.getResultList();

		auftragdokumentDtos = assembleAuftragdokumentDtos(cl);

		return auftragdokumentDtos;
	}

	public AuftragtextDto auftragtextFindByMandantLocaleCNr(String sMandantI,
			String sLocaleCNrI, String sCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "auftragtextFindByMandantLocaleCNr";
		myLogger.entry();

		if (sMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sMandantI == null"));
		}

		if (sLocaleCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sLocaleI == null"));
		}

		if (sCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sCNrI == null"));
		}

		AuftragtextDto text = null;
		Auftragtext textObject = null;

		try {
			// Schritt 1 : Sprache des Kunden ist Parameter
			Query query = em
					.createNamedQuery("AuftragtextfindByMandantLocaleMediaartCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, sLocaleCNrI);
			query.setParameter(3, sCNrI);
			textObject = (Auftragtext) query.getSingleResult();
		} catch (NoResultException ex) {
		}
		if (textObject == null) {
			// Schritt 2 : Den Auftragtext in Gewaehlte UI-Sprache des Users
			// anlegen
			createDefaultAuftragtext(sCNrI, sLocaleCNrI, theClientDto);
			Query query = em
					.createNamedQuery("AuftragtextfindByMandantLocaleMediaartCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, sLocaleCNrI);
			query.setParameter(3, sCNrI);
			textObject = (Auftragtext) query.getSingleResult();
		}
		text = assembleAuftragtextDto(textObject);
		myLogger.info(text.toString());
		return text;

	}

	public AuftragtextDto createDefaultAuftragtext(String cNrMediaartI,
			String sLocaleCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "createDefaultAuftragtext";
		myLogger.entry();

		AuftragtextDto oAuftragtextDto = new AuftragtextDto();
		oAuftragtextDto.setMediaartCNr(cNrMediaartI);
		oAuftragtextDto.setLocaleCNr(sLocaleCNrI);
		oAuftragtextDto.setMandantCNr(theClientDto.getMandant());
		String cTextinhalt = null;
		if (cNrMediaartI.equals(MediaFac.MEDIAART_KOPFTEXT)) {
			cTextinhalt = AuftragServiceFac.AUFTRAG_DEFAULT_KOPFTEXT;
		} else if (cNrMediaartI.equals(MediaFac.MEDIAART_FUSSTEXT)) {
			cTextinhalt = AuftragServiceFac.AUFTRAG_DEFAULT_FUSSTEXT;
		}
		oAuftragtextDto.setCTextinhalt(cTextinhalt);
		oAuftragtextDto.setIId(createAuftragtext(oAuftragtextDto));

		return oAuftragtextDto;
	}

	private void checkAuftragtextDto(AuftragtextDto oAuftragtextDtoI)
			throws EJBExceptionLP {
		if (oAuftragtextDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oAuftragtextDtoI == null"));
		}
	}

	private void setAuftragtextFromAuftragtextDto(Auftragtext auftragtext,
			AuftragtextDto auftragtextDto) {
		auftragtext.setMandantCNr(auftragtextDto.getMandantCNr());
		auftragtext.setLocaleCNr(auftragtextDto.getLocaleCNr());
		auftragtext.setMediaartCNr(auftragtextDto.getMediaartCNr());
		auftragtext.setXTextinhalt(auftragtextDto.getCTextinhalt());
		em.merge(auftragtext);
		em.flush();
	}

	private void setAuftragdokumentFromAuftragdokumentDto(
			Auftragdokument auftragdokument,
			AuftragdokumentDto auftragdokumentDto) {
		auftragdokument.setCNr(auftragdokumentDto.getCNr());
		auftragdokument.setCBez(auftragdokumentDto.getCBez());
		auftragdokument.setBVersteckt(auftragdokumentDto.getBVersteckt());
		em.merge(auftragdokument);
		em.flush();
	}

	private AuftragtextDto assembleAuftragtextDto(Auftragtext auftragtext) {
		return AuftragtextDtoAssembler.createDto(auftragtext);
	}

	private AuftragdokumentDto assembleAuftragdokumentDto(
			Auftragdokument auftragdokument) {
		return AuftragdokumentDtoAssembler.createDto(auftragdokument);
	}

	private AuftragauftragdokumentDto assembleAuftragauftragdokumentDto(
			Auftragauftragdokument auftragdokument) {
		return AuftragauftragdokumentDtoAssembler.createDto(auftragdokument);
	}

	private AuftragauftragdokumentDto[] assembleAuftragauftragdokumentDtos(
			Collection<?> auftragtexts) {
		List<AuftragauftragdokumentDto> list = new ArrayList<AuftragauftragdokumentDto>();
		if (auftragtexts != null) {
			Iterator<?> iterator = auftragtexts.iterator();
			while (iterator.hasNext()) {
				Auftragauftragdokument auftragtext = (Auftragauftragdokument) iterator
						.next();
				list.add(assembleAuftragauftragdokumentDto(auftragtext));
			}
		}
		AuftragauftragdokumentDto[] returnArray = new AuftragauftragdokumentDto[list
				.size()];
		return (AuftragauftragdokumentDto[]) list.toArray(returnArray);
	}

	private AuftragtextDto[] assembleAuftragtextDtos(Collection<?> auftragtexts) {
		List<AuftragtextDto> list = new ArrayList<AuftragtextDto>();
		if (auftragtexts != null) {
			Iterator<?> iterator = auftragtexts.iterator();
			while (iterator.hasNext()) {
				Auftragtext auftragtext = (Auftragtext) iterator.next();
				list.add(assembleAuftragtextDto(auftragtext));
			}
		}
		AuftragtextDto[] returnArray = new AuftragtextDto[list.size()];
		return (AuftragtextDto[]) list.toArray(returnArray);
	}

	public void createAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (auftragwiederholungsintervallDto == null) {
			return;
		}
		try {
			Auftragwiederholungsintervall auftragwiederholungsintervall = new Auftragwiederholungsintervall(
					auftragwiederholungsintervallDto.getCNr(),
					auftragwiederholungsintervallDto.getISort());
			em.persist(auftragwiederholungsintervall);
			em.flush();
			setAuftragwiederholungsintervallFromAuftragwiederholungsintervallDto(
					auftragwiederholungsintervall,
					auftragwiederholungsintervallDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAuftragwiederholungsintervall(String cNr)
			throws EJBExceptionLP {
		// try {
		Auftragwiederholungsintervall toRemove = em.find(
				Auftragwiederholungsintervall.class, cNr);
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (auftragwiederholungsintervallDto != null) {
			String cNr = auftragwiederholungsintervallDto.getCNr();
			removeAuftragwiederholungsintervall(cNr);
		}
	}

	public void updateAuftragwiederholungsintervall(
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (auftragwiederholungsintervallDto != null) {
			String cNr = auftragwiederholungsintervallDto.getCNr();
			// try {
			Auftragwiederholungsintervall auftragwiederholungsintervall = em
					.find(Auftragwiederholungsintervall.class, cNr);
			if (auftragwiederholungsintervall == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragwiederholungsintervallFromAuftragwiederholungsintervallDto(
					auftragwiederholungsintervall,
					auftragwiederholungsintervallDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public AuftragwiederholungsintervallDto auftragwiederholungsintervallFindByPrimaryKey(
			String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Auftragwiederholungsintervall auftragwiederholungsintervall = em.find(
				Auftragwiederholungsintervall.class, cNr);
		if (auftragwiederholungsintervall == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragwiederholungsintervallDto(auftragwiederholungsintervall);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setAuftragwiederholungsintervallFromAuftragwiederholungsintervallDto(
			Auftragwiederholungsintervall auftragwiederholungsintervall,
			AuftragwiederholungsintervallDto auftragwiederholungsintervallDto) {
		auftragwiederholungsintervall.setISort(auftragwiederholungsintervallDto
				.getISort());
		em.merge(auftragwiederholungsintervall);
		em.flush();
	}

	private AuftragwiederholungsintervallDto assembleAuftragwiederholungsintervallDto(
			Auftragwiederholungsintervall auftragwiederholungsintervall) {
		return AuftragwiederholungsintervallDtoAssembler
				.createDto(auftragwiederholungsintervall);
	}

	private AuftragwiederholungsintervallDto[] assembleAuftragwiederholungsintervallDtos(
			Collection<?> auftragwiederholungsintervalls) {
		List<AuftragwiederholungsintervallDto> list = new ArrayList<AuftragwiederholungsintervallDto>();
		if (auftragwiederholungsintervalls != null) {
			Iterator<?> iterator = auftragwiederholungsintervalls.iterator();
			while (iterator.hasNext()) {
				Auftragwiederholungsintervall auftragwiederholungsintervall = (Auftragwiederholungsintervall) iterator
						.next();
				list
						.add(assembleAuftragwiederholungsintervallDto(auftragwiederholungsintervall));
			}
		}
		AuftragwiederholungsintervallDto[] returnArray = new AuftragwiederholungsintervallDto[list
				.size()];
		return (AuftragwiederholungsintervallDto[]) list.toArray(returnArray);
	}

	public void createAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws EJBExceptionLP {
		if (auftragwiederholungsintervallsprDto == null) {
			return;
		}
		try {
			Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = new Auftragwiederholungsintervallspr(
					auftragwiederholungsintervallsprDto.getLocaleCNr(),
					auftragwiederholungsintervallsprDto.getCBez(),
					auftragwiederholungsintervallsprDto
							.getAuftragwiederholungsintervallCNr());
			em.persist(auftragwiederholungsintervallspr);
			em.flush();
			setAuftragwiederholungsintervallsprFromAuftragwiederholungsintervallsprDto(
					auftragwiederholungsintervallspr,
					auftragwiederholungsintervallsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAuftragwiederholungsintervallspr(String localeCNr,
			String auftragwiederholungsintervallCNr) throws EJBExceptionLP {
		AuftragwiederholungsintervallsprPK auftragwiederholungsintervallsprPK = new AuftragwiederholungsintervallsprPK();
		auftragwiederholungsintervallsprPK.setLocaleCNr(localeCNr);
		auftragwiederholungsintervallsprPK
				.setAuftragwiederholungsintervallCNr(auftragwiederholungsintervallCNr);
		// try {
		Auftragwiederholungsintervallspr toRemove = em.find(
				Auftragwiederholungsintervallspr.class,
				auftragwiederholungsintervallsprPK);
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws EJBExceptionLP {
		if (auftragwiederholungsintervallsprDto != null) {
			String localeCNr = auftragwiederholungsintervallsprDto
					.getLocaleCNr();
			String auftragwiederholungsintervallCNr = auftragwiederholungsintervallsprDto
					.getAuftragwiederholungsintervallCNr();
			removeAuftragwiederholungsintervallspr(localeCNr,
					auftragwiederholungsintervallCNr);
		}
	}

	public void updateAuftragwiederholungsintervallspr(
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto)
			throws EJBExceptionLP {
		if (auftragwiederholungsintervallsprDto != null) {
			AuftragwiederholungsintervallsprPK auftragwiederholungsintervallsprPK = new AuftragwiederholungsintervallsprPK();
			auftragwiederholungsintervallsprPK
					.setLocaleCNr(auftragwiederholungsintervallsprDto
							.getLocaleCNr());
			auftragwiederholungsintervallsprPK
					.setAuftragwiederholungsintervallCNr(auftragwiederholungsintervallsprDto
							.getAuftragwiederholungsintervallCNr());
			// try {
			Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = em
					.find(Auftragwiederholungsintervallspr.class,
							auftragwiederholungsintervallsprPK);
			if (auftragwiederholungsintervallspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragwiederholungsintervallsprFromAuftragwiederholungsintervallsprDto(
					auftragwiederholungsintervallspr,
					auftragwiederholungsintervallsprDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateAuftragwiederholungsintervallsprs(
			AuftragwiederholungsintervallsprDto[] auftragwiederholungsintervallsprDtos)
			throws EJBExceptionLP {
		if (auftragwiederholungsintervallsprDtos != null) {
			for (int i = 0; i < auftragwiederholungsintervallsprDtos.length; i++) {
				updateAuftragwiederholungsintervallspr(auftragwiederholungsintervallsprDtos[i]);
			}
		}
	}

	public AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprFindByPrimaryKey(
			String localeCNr, String auftragwiederholungsintervallCNr)
			throws EJBExceptionLP {
		// try {
		AuftragwiederholungsintervallsprPK auftragwiederholungsintervallsprPK = new AuftragwiederholungsintervallsprPK();
		auftragwiederholungsintervallsprPK.setLocaleCNr(localeCNr);
		auftragwiederholungsintervallsprPK
				.setAuftragwiederholungsintervallCNr(auftragwiederholungsintervallCNr);
		Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = em
				.find(Auftragwiederholungsintervallspr.class,
						auftragwiederholungsintervallsprPK);
		if (auftragwiederholungsintervallspr == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragwiederholungsintervallsprDto(auftragwiederholungsintervallspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * @deprecated MB->VF ??? das ist doch dasselbe wie findByPrimaryKey. bitte
	 *             entfernen.
	 * 
	 * @param pSprache
	 *            String
	 * @param pCNr
	 *            String
	 * @return AuftragwiederholungsintervallsprDto
	 * @throws EJBExceptionLP
	 */
	public AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprFindBySpracheAndCNr(
			String pSprache, String pCNr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("AuftragwiederholungsintervallsprfindBySpracheAndCNr");
		query.setParameter(1, pSprache);
		query.setParameter(2, pCNr);
		Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = (Auftragwiederholungsintervallspr) query
				.getSingleResult();
		if (auftragwiederholungsintervallspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleAuftragwiederholungsintervallsprDto(auftragwiederholungsintervallspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	private void setAuftragwiederholungsintervallsprFromAuftragwiederholungsintervallsprDto(
			Auftragwiederholungsintervallspr auftragwiederholungsintervallspr,
			AuftragwiederholungsintervallsprDto auftragwiederholungsintervallsprDto) {
		auftragwiederholungsintervallspr
				.setCBez(auftragwiederholungsintervallsprDto.getCBez());
		em.merge(auftragwiederholungsintervallspr);
		em.flush();
	}

	private AuftragwiederholungsintervallsprDto assembleAuftragwiederholungsintervallsprDto(
			Auftragwiederholungsintervallspr auftragwiederholungsintervallspr) {
		return AuftragwiederholungsintervallsprDtoAssembler
				.createDto(auftragwiederholungsintervallspr);
	}

	private AuftragwiederholungsintervallsprDto[] assembleAuftragwiederholungsintervallsprDtos(
			Collection<?> auftragwiederholungsintervallsprs) {
		List<AuftragwiederholungsintervallsprDto> list = new ArrayList<AuftragwiederholungsintervallsprDto>();
		if (auftragwiederholungsintervallsprs != null) {
			Iterator<?> iterator = auftragwiederholungsintervallsprs.iterator();
			while (iterator.hasNext()) {
				Auftragwiederholungsintervallspr auftragwiederholungsintervallspr = (Auftragwiederholungsintervallspr) iterator
						.next();
				list
						.add(assembleAuftragwiederholungsintervallsprDto(auftragwiederholungsintervallspr));
			}
		}
		AuftragwiederholungsintervallsprDto[] returnArray = new AuftragwiederholungsintervallsprDto[list
				.size()];
		return (AuftragwiederholungsintervallsprDto[]) list
				.toArray(returnArray);
	}
}
