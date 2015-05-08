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
package com.lp.server.lieferschein.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Rohs;
import com.lp.server.artikel.ejb.Sperren;
import com.lp.server.lieferschein.ejb.Begruendung;
import com.lp.server.lieferschein.ejb.Lieferscheinart;
import com.lp.server.lieferschein.ejb.Lieferscheinartspr;
import com.lp.server.lieferschein.ejb.LieferscheinartsprPK;
import com.lp.server.lieferschein.ejb.Lieferscheinpositionart;
import com.lp.server.lieferschein.ejb.Lieferscheinstatus;
import com.lp.server.lieferschein.ejb.Lieferscheintext;
import com.lp.server.lieferschein.ejb.Verkettet;
import com.lp.server.lieferschein.service.BegruendungDto;
import com.lp.server.lieferschein.service.BegruendungDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinartDto;
import com.lp.server.lieferschein.service.LieferscheinartDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinartsprDto;
import com.lp.server.lieferschein.service.LieferscheinartsprDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinpositionartDto;
import com.lp.server.lieferschein.service.LieferscheinpositionartDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinstatusDto;
import com.lp.server.lieferschein.service.LieferscheinstatusDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheintextDto;
import com.lp.server.lieferschein.service.LieferscheintextDtoAssembler;
import com.lp.server.lieferschein.service.VerkettetDto;
import com.lp.server.lieferschein.service.VerkettetDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class LieferscheinServiceFacBean extends Facade implements
		LieferscheinServiceFac {
	@PersistenceContext
	private EntityManager em;

	// Lieferscheinart
	// -----------------------------------------------------------

	public String createLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinartDto(lieferscheinartDtoI);
		try {
			Lieferscheinart lieferscheinart = new Lieferscheinart(
					lieferscheinartDtoI.getCNr(),
					lieferscheinartDtoI.getISort());
			em.persist(lieferscheinart);
			em.flush();
			setLieferscheinartFromLieferscheinartDto(lieferscheinart,
					lieferscheinartDtoI);

			// jetzt die Sprache
			if (lieferscheinartDtoI.getLieferscheinartsprDto() != null) {
				Lieferscheinartspr lieferscheinartspr = new Lieferscheinartspr(
						lieferscheinartDtoI.getLieferscheinartsprDto()
								.getLocaleCNr(), lieferscheinartDtoI.getCNr(),
						lieferscheinartDtoI.getLieferscheinartsprDto()
								.getCBez());
				em.persist(lieferscheinartspr);
				em.flush();

				setLieferscheinartsprFromLieferscheinartsprDto(
						lieferscheinartspr,
						lieferscheinartDtoI.getLieferscheinartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return lieferscheinartDtoI.getCNr();
	}

	public void removeLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
	}

	public void updateLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinartDto(lieferscheinartDtoI);

		try {
			Lieferscheinart lieferscheinart = em.find(Lieferscheinart.class,
					lieferscheinartDtoI.getCNr());
			if (lieferscheinart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setLieferscheinartFromLieferscheinartDto(lieferscheinart,
					lieferscheinartDtoI);

			// jetzt die Spr
			LieferscheinartsprDto lieferscheinartsprDto = lieferscheinartDtoI
					.getLieferscheinartsprDto();

			if (lieferscheinartsprDto != null
					&& lieferscheinartsprDto.getLieferscheinartCNr() != null) {
				LieferscheinartsprPK lieferscheinartsprPK = new LieferscheinartsprPK();
				lieferscheinartsprPK.setLocaleCNr(lieferscheinartsprDto
						.getLocaleCNr());
				lieferscheinartsprPK
						.setLieferscheinartCNr(lieferscheinartsprDto
								.getLieferscheinartCNr());

				Lieferscheinartspr lieferscheinartspr = em.find(
						Lieferscheinartspr.class, lieferscheinartsprPK);

				setLieferscheinartsprFromLieferscheinartsprDto(
						lieferscheinartspr, lieferscheinartsprDto);
			} else {
				Lieferscheinartspr lieferscheinartspr = new Lieferscheinartspr(
						lieferscheinartDtoI.getLieferscheinartsprDto()
								.getLocaleCNr(), lieferscheinartDtoI.getCNr(),
						lieferscheinartDtoI.getLieferscheinartsprDto()
								.getCBez());
				em.persist(lieferscheinartspr);
				em.flush();

				setLieferscheinartsprFromLieferscheinartsprDto(
						lieferscheinartspr,
						lieferscheinartDtoI.getLieferscheinartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public LieferscheinartDto lieferscheinartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinartDto lieferscheinartDto = null;
		Lieferscheinart lieferscheinart = em.find(Lieferscheinart.class, cNr);
		if (lieferscheinart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinartDto = assembleLieferscheinartDto(lieferscheinart);
		// jetzt die Spr
		LieferscheinartsprDto lieferscheinartsprDto = null;
		try {
			LieferscheinartsprPK lieferscheinartsprPK = new LieferscheinartsprPK(
					theClientDto.getLocUiAsString(), cNr);
			Lieferscheinartspr lieferscheinartspr = em.find(
					Lieferscheinartspr.class, lieferscheinartsprPK);
			if (lieferscheinartspr != null) {
				lieferscheinartsprDto = assembleLieferscheinartsprDto(lieferscheinartspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		lieferscheinartDto.setLieferscheinartsprDto(lieferscheinartsprDto);
		return lieferscheinartDto;
	}

	public LieferscheinartDto[] lieferscheinartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("LieferscheinartfindAll");
		Collection<?> lieferscheinartDtos = query.getResultList();
		return assembleLieferscheinartDtos(lieferscheinartDtos);
	}

	private void checkLieferscheinartDto(LieferscheinartDto lieferscheinartDtoI)
			throws EJBExceptionLP {
		if (lieferscheinartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("lieferscheinartDtoI == null"));
		}

		myLogger.info("LieferscheinartDto: " + lieferscheinartDtoI);
	}

	private void checkLieferscheinartCNr(String cNrLieferscheinartI)
			throws EJBExceptionLP {
		if (cNrLieferscheinartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLieferscheinartI == null"));
		}
	}

	private void setLieferscheinartFromLieferscheinartDto(
			Lieferscheinart lieferscheinart,
			LieferscheinartDto lieferscheinartDto) {
		lieferscheinart.setISort(lieferscheinartDto.getISort());
		em.merge(lieferscheinart);
		em.flush();
	}

	private void setBegruendungFromBegruendungDto(Begruendung begruendung,
			BegruendungDto begruendungDto) {
		begruendung.setCNr(begruendungDto.getCNr());
		begruendung.setCBez(begruendungDto.getCBez());
		em.merge(begruendung);
		em.flush();
	}

	private LieferscheinartDto assembleLieferscheinartDto(
			Lieferscheinart lieferscheinart) {
		return LieferscheinartDtoAssembler.createDto(lieferscheinart);
	}

	private LieferscheinartDto[] assembleLieferscheinartDtos(
			Collection<?> lieferscheinarts) {
		List<LieferscheinartDto> list = new ArrayList<LieferscheinartDto>();
		if (lieferscheinarts != null) {
			Iterator<?> iterator = lieferscheinarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinart lieferscheinart = (Lieferscheinart) iterator
						.next();
				list.add(assembleLieferscheinartDto(lieferscheinart));
			}
		}
		LieferscheinartDto[] returnArray = new LieferscheinartDto[list.size()];
		return (LieferscheinartDto[]) list.toArray(returnArray);
	}

	public void createLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP {
		if (lieferscheinartsprDto == null) {
			return;
		}
		try {
			Lieferscheinartspr lieferscheinartspr = new Lieferscheinartspr(
					lieferscheinartsprDto.getLocaleCNr(),
					lieferscheinartsprDto.getLieferscheinartCNr(),
					lieferscheinartsprDto.getCBez());
			em.persist(lieferscheinartspr);
			em.flush();
			setLieferscheinartsprFromLieferscheinartsprDto(lieferscheinartspr,
					lieferscheinartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP {
	}

	public void updateLieferscheinartspr(
			LieferscheinartsprDto lieferscheinartsprDto) throws EJBExceptionLP {
		if (lieferscheinartsprDto != null) {
			LieferscheinartsprPK lieferscheinartsprPK = new LieferscheinartsprPK();
			lieferscheinartsprPK.setLocaleCNr(lieferscheinartsprDto
					.getLocaleCNr());
			lieferscheinartsprPK.setLieferscheinartCNr(lieferscheinartsprDto
					.getLieferscheinartCNr());
			Lieferscheinartspr lieferscheinartspr = em.find(
					Lieferscheinartspr.class, lieferscheinartsprPK);
			if (lieferscheinartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheinartsprFromLieferscheinartsprDto(lieferscheinartspr,
					lieferscheinartsprDto);
		}
	}

	public LieferscheinartsprDto lieferscheinartsprFindByPrimaryKey(
			String spracheCNr, String lieferscheinartCNr) throws EJBExceptionLP {
		LieferscheinartsprPK lieferscheinartsprPK = new LieferscheinartsprPK();
		lieferscheinartsprPK.setLocaleCNr(spracheCNr);
		lieferscheinartsprPK.setLieferscheinartCNr(lieferscheinartCNr);
		Lieferscheinartspr lieferscheinartsprDto = em.find(
				Lieferscheinartspr.class, lieferscheinartsprPK);
		if (lieferscheinartsprDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferscheinartsprDto(lieferscheinartsprDto);
	}

	public LieferscheinartsprDto lieferscheinartsprFindBySpracheAndCNr(
			String pSprache, String pArt) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("LieferscheinartsprfindBySpracheAndCNr");
			query.setParameter(1, pSprache);
			query.setParameter(2, pArt);
			Lieferscheinartspr lieferscheinartsprDto = (Lieferscheinartspr) query
					.getSingleResult();
			return assembleLieferscheinartsprDto(lieferscheinartsprDto);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

	}

	private void setLieferscheinartsprFromLieferscheinartsprDto(
			Lieferscheinartspr lieferscheinartspr,
			LieferscheinartsprDto lieferscheinartsprDto) {
		lieferscheinartspr.setCBez(lieferscheinartsprDto.getCBez());
		em.merge(lieferscheinartspr);
		em.flush();
	}

	private LieferscheinartsprDto assembleLieferscheinartsprDto(
			Lieferscheinartspr lieferscheinartspr) {
		return LieferscheinartsprDtoAssembler.createDto(lieferscheinartspr);
	}

	private LieferscheinartsprDto[] assembleLieferscheinartsprDtos(
			Collection<?> lieferscheinartsprs) {
		List<LieferscheinartsprDto> list = new ArrayList<LieferscheinartsprDto>();
		if (lieferscheinartsprs != null) {
			Iterator<?> iterator = lieferscheinartsprs.iterator();
			while (iterator.hasNext()) {
				Lieferscheinartspr lieferscheinartspr = (Lieferscheinartspr) iterator
						.next();
				list.add(assembleLieferscheinartsprDto(lieferscheinartspr));
			}
		}
		LieferscheinartsprDto[] returnArray = new LieferscheinartsprDto[list
				.size()];
		return (LieferscheinartsprDto[]) list.toArray(returnArray);
	}

	// Lieferscheinpositionart
	// ---------------------------------------------------

	public String createLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionartDto(lieferscheinpositionartDtoI);
		try {
			Lieferscheinpositionart lieferscheinpositionart = new Lieferscheinpositionart(
					lieferscheinpositionartDtoI.getCNr(),
					lieferscheinpositionartDtoI.getISort(),
					lieferscheinpositionartDtoI.getBVersteckt());
			em.persist(lieferscheinpositionart);
			em.flush();
			setLieferscheinpositionartFromLieferscheinpositionartDto(
					lieferscheinpositionart, lieferscheinpositionartDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return lieferscheinpositionartDtoI.getCNr();
	}

	public void removeLieferscheinpositionart(
			String cNrLieferscheinpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinpositionartCNr(cNrLieferscheinpositionartI);
		Lieferscheinpositionart toRemove = em.find(
				Lieferscheinpositionart.class, cNrLieferscheinpositionartI);
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

	public void updateLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionartDto(lieferscheinpositionartDtoI);
		Lieferscheinpositionart lieferscheinpositionart = em.find(
				Lieferscheinpositionart.class,
				lieferscheinpositionartDtoI.getCNr());
		if (lieferscheinpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheinpositionartFromLieferscheinpositionartDto(
				lieferscheinpositionart, lieferscheinpositionartDtoI);
	}

	public LieferscheinpositionartDto lieferscheinpositionartFindByPrimaryKey(
			String cNrLieferscheinpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinpositionartDto lieferscheinpositionartDto = null;
		Lieferscheinpositionart lieferscheinpositionart = em.find(
				Lieferscheinpositionart.class, cNrLieferscheinpositionartI);
		if (lieferscheinpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinpositionartDto = assembleLieferscheinpositionartDto(lieferscheinpositionart);
		return lieferscheinpositionartDto;
	}

	public Map<String, String> lieferscheinpositionartFindAll(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLocale(locale1I);
		checkLocale(locale2I);

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		try {
			Query query = em.createNamedQuery("LieferscheinpositionartfindAll");
			Collection<?> cl = query.getResultList();
			LieferscheinpositionartDto[] aLieferscheinpositionartDto = assembleLieferscheinpositionartDtos(cl);

			for (int i = 0; i < aLieferscheinpositionartDto.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(
								aLieferscheinpositionartDto[i]
										.getPositionsartCNr(),
								locale1I, locale2I);
				map.put(aLieferscheinpositionartDto[i].getPositionsartCNr(),
						sUebersetzung);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return map;
	}

	private void checkLieferscheinpositionartDto(
			LieferscheinpositionartDto lieferscheinpositionartDtoI)
			throws EJBExceptionLP {
		if (lieferscheinpositionartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("lieferscheinpositionartDtoI == null"));
		}

		myLogger.info("LieferscheinpositionartDtoI: "
				+ lieferscheinpositionartDtoI.toString());
	}

	private void checkLieferscheinpositionartCNr(
			String cNrLieferscheinpositionartI) throws EJBExceptionLP {
		if (cNrLieferscheinpositionartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLieferscheinpositionartI == null"));
		}

		myLogger.info("LieferscheinpositionartCNr: "
				+ cNrLieferscheinpositionartI);
	}

	private void setLieferscheinpositionartFromLieferscheinpositionartDto(
			Lieferscheinpositionart lieferscheinpositionart,
			LieferscheinpositionartDto lieferscheinpositionartDto) {
		lieferscheinpositionart.setISort(lieferscheinpositionartDto.getISort());
		lieferscheinpositionart.setBVersteckt(lieferscheinpositionartDto
				.getBVersteckt());
		em.merge(lieferscheinpositionart);
		em.flush();
	}

	private LieferscheinpositionartDto assembleLieferscheinpositionartDto(
			Lieferscheinpositionart lieferscheinpositionart) {
		return LieferscheinpositionartDtoAssembler
				.createDto(lieferscheinpositionart);
	}

	private LieferscheinpositionartDto[] assembleLieferscheinpositionartDtos(
			Collection<?> lieferscheinpositionarts) {
		List<LieferscheinpositionartDto> list = new ArrayList<LieferscheinpositionartDto>();
		if (lieferscheinpositionarts != null) {
			Iterator<?> iterator = lieferscheinpositionarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinpositionart lieferscheinpositionart = (Lieferscheinpositionart) iterator
						.next();
				list.add(assembleLieferscheinpositionartDto(lieferscheinpositionart));
			}
		}
		LieferscheinpositionartDto[] returnArray = new LieferscheinpositionartDto[list
				.size()];
		return (LieferscheinpositionartDto[]) list.toArray(returnArray);
	}

	public void createLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP {
		if (lieferscheinstatusDto == null) {
			return;
		}
		try {
			Lieferscheinstatus lieferscheinstatus = new Lieferscheinstatus(
					lieferscheinstatusDto.getStatusCNr(),
					lieferscheinstatusDto.getISort());
			em.persist(lieferscheinstatus);
			em.flush();
			setLieferscheinstatusFromLieferscheinstatusDto(lieferscheinstatus,
					lieferscheinstatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public Integer createVerkettet(VerkettetDto dto) {

		try {

			try {
				Query query = em
						.createNamedQuery("VerkettetfindByLieferscheinIIdLieferscheinIIdVerkettet");
				query.setParameter(1, dto.getLieferscheinIId());
				query.setParameter(2, dto.getLieferscheinIIdVerkettet());
				// @todo getSingleResult oder getResultList ?
				Verkettet doppelt = (Verkettet) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LS_VERKETTET.UK"));
			} catch (NoResultException ex1) {
				// nothing here
			}

			// Bin ich schon verkettet
			try {
				Query query = em
						.createNamedQuery("VerkettetfindByLieferscheinIIdVerkettet");
				query.setParameter(1, dto.getLieferscheinIIdVerkettet());
				// @todo getSingleResult oder getResultList ?
				Verkettet doppelt = (Verkettet) query.getSingleResult();

				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(
								doppelt.getLieferscheinIId());

				ArrayList alDaten = new ArrayList();
				alDaten.add(lsDto.getCNr());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_BEREITS_VERKETTET,
						alDaten, new Exception(
								"Lieferschein ist verkettet in Lieferschein "
										+ lsDto.getCNr()));

			} catch (NoResultException ex1) {
				// nothing here
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERKETTET);
			dto.setIId(pk);

			Verkettet verkettet = new Verkettet(dto.getIId(),
					dto.getLieferscheinIId(), dto.getLieferscheinIIdVerkettet());
			em.persist(verkettet);
			em.flush();
			setVerkettetFromVerkettetDto(verkettet, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return dto.getIId();
	}

	public Integer createBegruendung(BegruendungDto begruendungDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BEGRUENDUNG);
		begruendungDto.setIId(iId);
		try {

			try {
				Query query = em.createNamedQuery("BegruendungFindByCNr");
				query.setParameter(1, begruendungDto.getCNr());
				Begruendung doppelt = (Begruendung) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_BEGRUENDUNG.C_NR"));
			} catch (NoResultException ex) {

			}

			Begruendung begruendung = new Begruendung(begruendungDto.getIId(),
					begruendungDto.getCNr(), begruendungDto.getCBez());
			em.persist(begruendung);
			em.flush();
			setBegruendungFromBegruendungDto(begruendung, begruendungDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iId;
	}

	public void updateBegruendung(BegruendungDto begruendungDto) {

		try {
			Integer iId = begruendungDto.getIId();
			// try {
			Begruendung begruendung = em.find(Begruendung.class, iId);

			try {
				Query query = em.createNamedQuery("BegruendungFindByCNr");
				query.setParameter(1, begruendungDto.getCNr());
				Begruendung vorhanden = (Begruendung) query.getSingleResult();
				if (vorhanden != null
						&& iId.equals(vorhanden.getIId()) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("LP_BEGRUENDUNG.C_NR"));
				}

			} catch (NoResultException ex) {
			}

			setBegruendungFromBegruendungDto(begruendung, begruendungDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeLieferscheinstatus(String statusCNr)
			throws EJBExceptionLP {
		Lieferscheinstatus toRemove = em.find(Lieferscheinstatus.class,
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
	}

	public void removeBegruendung(Integer iId) {
		Begruendung toRemove = em.find(Begruendung.class, iId);
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

	public void removeVerkettet(Integer iId) {
		Verkettet toRemove = em.find(Verkettet.class, iId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP {
		if (lieferscheinstatusDto != null) {
			String statusCNr = lieferscheinstatusDto.getStatusCNr();
			removeLieferscheinstatus(statusCNr);
		}
	}

	public void updateLieferscheinstatus(
			LieferscheinstatusDto lieferscheinstatusDto) throws EJBExceptionLP {
		if (lieferscheinstatusDto != null) {
			String statusCNr = lieferscheinstatusDto.getStatusCNr();
			Lieferscheinstatus lieferscheinstatus = em.find(
					Lieferscheinstatus.class, statusCNr);
			if (lieferscheinstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheinstatusFromLieferscheinstatusDto(lieferscheinstatus,
					lieferscheinstatusDto);
		}
	}

	public void updateVerkettet(VerkettetDto dto) {
		Verkettet verkettet = em.find(Verkettet.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("VerkettetfindByLieferscheinIIdLieferscheinIIdVerkettet");
			query.setParameter(1, dto.getLieferscheinIId());
			query.setParameter(2, dto.getLieferscheinIIdVerkettet());
			Integer iIdVorhanden = ((Verkettet) query.getSingleResult())
					.getIId();
			if (verkettet.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LS_VERKETTET.UK"));
			}
		} catch (NoResultException ex) {

		}

		setVerkettetFromVerkettetDto(verkettet, dto);
	}

	public void updateLieferscheinstatuss(
			LieferscheinstatusDto[] lieferscheinstatusDtos)
			throws EJBExceptionLP {
		if (lieferscheinstatusDtos != null) {
			for (int i = 0; i < lieferscheinstatusDtos.length; i++) {
				updateLieferscheinstatus(lieferscheinstatusDtos[i]);
			}
		}
	}

	public LieferscheinstatusDto lieferscheinstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP {
		Lieferscheinstatus lieferscheinstatus = em.find(
				Lieferscheinstatus.class, statusCNr);
		if (lieferscheinstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferscheinstatusDto(lieferscheinstatus);
	}

	public BegruendungDto begruendungFindByPrimaryKey(Integer iId) {
		Begruendung begruendung = em.find(Begruendung.class, iId);
		if (begruendung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBegruendungDto(begruendung);
	}

	public VerkettetDto verkettetFindByPrimaryKey(Integer iId) {
		Verkettet verkettet = em.find(Verkettet.class, iId);
		return VerkettetDtoAssembler.createDto(verkettet);
	}

	public VerkettetDto[] verkettetFindByLieferscheinIId(Integer lieferscheinIId) {
		Query query = em.createNamedQuery("VerkettetfindByLieferscheinIId");
		query.setParameter(1, lieferscheinIId);
		return VerkettetDtoAssembler.createDtos(query.getResultList());
	}

	public VerkettetDto verkettetfindByLieferscheinIIdVerkettet(
			Integer lieferscheinIIdVerkettet) {
		Query query = em
				.createNamedQuery("VerkettetfindByLieferscheinIIdVerkettet");
		query.setParameter(1, lieferscheinIIdVerkettet);
		return VerkettetDtoAssembler.createDto((Verkettet) query
				.getSingleResult());
	}
	public VerkettetDto verkettetfindByLieferscheinIIdVerkettetOhneExc(
			Integer lieferscheinIIdVerkettet) {
		Query query = em
				.createNamedQuery("VerkettetfindByLieferscheinIIdVerkettet");
		query.setParameter(1, lieferscheinIIdVerkettet);
		try {
			return VerkettetDtoAssembler.createDto((Verkettet) query
					.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setLieferscheinstatusFromLieferscheinstatusDto(
			Lieferscheinstatus lieferscheinstatus,
			LieferscheinstatusDto lieferscheinstatusDto) {
		lieferscheinstatus.setISort(lieferscheinstatusDto.getISort());
		em.merge(lieferscheinstatus);
		em.flush();
	}

	private void setVerkettetFromVerkettetDto(Verkettet verkettet,
			VerkettetDto verkettetDto) {
		verkettet.setLieferscheinIId(verkettetDto.getLieferscheinIId());
		verkettet.setLieferscheinIIdVerkettet(verkettetDto
				.getLieferscheinIIdVerkettet());
		em.merge(verkettet);
		em.flush();
	}

	private LieferscheinstatusDto assembleLieferscheinstatusDto(
			Lieferscheinstatus lieferscheinstatus) {
		return LieferscheinstatusDtoAssembler.createDto(lieferscheinstatus);
	}

	private BegruendungDto assembleBegruendungDto(Begruendung begruendung) {
		return BegruendungDtoAssembler.createDto(begruendung);
	}

	private LieferscheinstatusDto[] assembleLieferscheinstatusDtos(
			Collection<?> lieferscheinstatuss) {
		List<LieferscheinstatusDto> list = new ArrayList<LieferscheinstatusDto>();
		if (lieferscheinstatuss != null) {
			Iterator<?> iterator = lieferscheinstatuss.iterator();
			while (iterator.hasNext()) {
				Lieferscheinstatus lieferscheinstatus = (Lieferscheinstatus) iterator
						.next();
				list.add(assembleLieferscheinstatusDto(lieferscheinstatus));
			}
		}
		LieferscheinstatusDto[] returnArray = new LieferscheinstatusDto[list
				.size()];
		return (LieferscheinstatusDto[]) list.toArray(returnArray);
	}

	// Lieferscheintext
	// ----------------------------------------------------------

	public Integer createLieferscheintext(
			LieferscheintextDto lieferscheintextDto) throws EJBExceptionLP {
		final String METHOD_NAME = "createLieferscheintext";
		myLogger.entry();
		checkLieferscheintextDto(lieferscheintextDto);

		// den PK erzeugen und setzen
		Integer iId = null;

		iId = getPKGeneratorObj()
				.getNextPrimaryKey(PKConst.PK_LIEFERSCHEINTEXT);
		lieferscheintextDto.setIId(iId);
		try {
			Lieferscheintext lieferscheintext = new Lieferscheintext(
					lieferscheintextDto.getIId(),
					lieferscheintextDto.getMandantCNr(),
					lieferscheintextDto.getLocaleCNr(),
					lieferscheintextDto.getMediaartCNr(),
					lieferscheintextDto.getCTextinhalt());
			em.persist(lieferscheintext);
			em.flush();

			setLieferscheintextFromLieferscheintextDto(lieferscheintext,
					lieferscheintextDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "removeLieferscheintext";
		myLogger.entry();
		checkLieferscheintextDto(lieferscheintextDto);
		Lieferscheintext toRemove = em.find(Lieferscheintext.class,
				lieferscheintextDto.getIId());
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

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "updateLieferscheintext";
		myLogger.entry();
		checkLieferscheintextDto(lieferscheintextDto);
		Lieferscheintext oLieferscheintext = em.find(Lieferscheintext.class,
				lieferscheintextDto.getIId());
		if (oLieferscheintext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheintextFromLieferscheintextDto(oLieferscheintext,
				lieferscheintextDto);
	}

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(
			Integer iIdLieferscheintextI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheintextDto oLieferscheintextDto = null;
		Lieferscheintext text = em.find(Lieferscheintext.class,
				iIdLieferscheintextI);
		if (text == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		oLieferscheintextDto = assembleLieferscheintextDto(text);
		return oLieferscheintextDto;
	}

	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (sLocaleI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sLocaleI == null"));
		}

		if (sCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sCNrI == null"));
		}

		LieferscheintextDto oLieferscheintextDto = null;
		Lieferscheintext oLieferscheintext = null;

		try {
			// Schritt 1 : Sprache des Kunden ist Parameter
			Query query = em
					.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, sLocaleI);
			query.setParameter(3, sCNrI);
			oLieferscheintext = (Lieferscheintext) query.getSingleResult();
		} catch (NoResultException ex) {
			try {
				// Schritt 2 : Den Lieferscheintext in Gewaehlte UI-Sprache des
				// Users anlegen
				createDefaultLieferscheintext(sCNrI, sLocaleI, theClientDto);
				Query query = em
						.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, sLocaleI);
				query.setParameter(3, sCNrI);
				oLieferscheintext = (Lieferscheintext) query.getSingleResult();
			} catch (Exception exUi) {
			}
		}

		oLieferscheintextDto = assembleLieferscheintextDto(oLieferscheintext);

		myLogger.info(oLieferscheintextDto.toString());

		return oLieferscheintextDto;
	}

	public LieferscheintextDto createDefaultLieferscheintext(String sMediaartI,
			String sLocaleI, TheClientDto theClientDto) throws EJBExceptionLP {

		LieferscheintextDto oLieferscheintextDto = new LieferscheintextDto();
		oLieferscheintextDto.setMediaartCNr(sMediaartI);
		oLieferscheintextDto.setLocaleCNr(sLocaleI); // muss zumindest in
														// Konzerndatensprache
														// hinterlegt sein
		oLieferscheintextDto.setMandantCNr(theClientDto.getMandant());
		String cTextinhalt = null;

		if (sMediaartI.equals(MediaFac.MEDIAART_KOPFTEXT)) {
			cTextinhalt = LieferscheinServiceFac.LIEFERSCHEIN_DEFAULT_KOPFTEXT;
		} else if (sMediaartI.equals(MediaFac.MEDIAART_FUSSTEXT)) {
			cTextinhalt = LieferscheinServiceFac.LIEFERSCHEIN_DEFAULT_FUSSTEXT;
		}

		oLieferscheintextDto.setCTextinhalt(cTextinhalt);
		oLieferscheintextDto
				.setIId(createLieferscheintext(oLieferscheintextDto));

		return oLieferscheintextDto;
	}

	private void checkLieferscheintextDto(
			LieferscheintextDto lieferscheintextDtoI) throws EJBExceptionLP {
		if (lieferscheintextDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("lieferscheintextDtoI == null"));
		}

		myLogger.info("LieferscheintextDto: " + lieferscheintextDtoI.toString());
	}

	private void checkLieferscheintextIId(Integer iIdLieferscheintextI)
			throws EJBExceptionLP {
		if (iIdLieferscheintextI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheintextI == null"));
		}

		myLogger.info("LieferscheintextIId: " + iIdLieferscheintextI);
	}

	private void setLieferscheintextFromLieferscheintextDto(
			Lieferscheintext lieferscheintext,
			LieferscheintextDto lieferscheintextDto) {
		lieferscheintext.setMandantCNr(lieferscheintextDto.getMandantCNr());
		lieferscheintext.setLocaleCNr(lieferscheintextDto.getLocaleCNr());
		lieferscheintext.setMediaartCNr(lieferscheintextDto.getMediaartCNr());
		lieferscheintext.setXTextinhalt(lieferscheintextDto.getCTextinhalt());
		em.merge(lieferscheintext);
		em.flush();
	}

	private LieferscheintextDto assembleLieferscheintextDto(
			Lieferscheintext lieferscheintext) {
		return LieferscheintextDtoAssembler.createDto(lieferscheintext);
	}

	private LieferscheintextDto[] assembleLieferscheintextDtos(
			Collection<?> lieferscheintexts) {
		List<LieferscheintextDto> list = new ArrayList<LieferscheintextDto>();
		if (lieferscheintexts != null) {
			Iterator<?> iterator = lieferscheintexts.iterator();
			while (iterator.hasNext()) {
				Lieferscheintext lieferscheintext = (Lieferscheintext) iterator
						.next();
				list.add(assembleLieferscheintextDto(lieferscheintext));
			}
		}
		LieferscheintextDto[] returnArray = new LieferscheintextDto[list.size()];
		return (LieferscheintextDto[]) list.toArray(returnArray);
	}
}
