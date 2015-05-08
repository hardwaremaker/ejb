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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.system.ejb.TheJudgePK;
import com.lp.server.system.ejb.Thejudge;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheJudgeDtoAssembler;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class TheJudgeFacBean extends Facade implements TheJudgeFac {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Prueft, ob ein best. Benutzer ein gewisses Recht hat
	 * 
	 * @param theClientDto
	 *            User-Id
	 * @return boolean
	 * @throws EJBExceptionLP
	 */

	public Integer getSystemrolleIId(TheClientDto theClientDto) {
		try {
			int iLength = 0;
			if (theClientDto.getBenutzername().indexOf("|") == -1) {
				iLength = theClientDto.getBenutzername().length();
			} else {
				iLength = theClientDto.getBenutzername().indexOf("|");
			}
			String sBenutzer = theClientDto.getBenutzername().substring(0,
					iLength);
			// AD wegen lpwebappzemecs aus Webservice
			sBenutzer = sBenutzer.trim();

			BenutzermandantsystemrolleDto bmsr = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerCNrMandantCNr(
							sBenutzer, theClientDto.getMandant());
			SystemrolleDto rolle = getBenutzerFac().systemrolleFindByPrimaryKey(bmsr.getSystemrolleIId());
			
			return rolle.getAliasRolleIId() == null ? rolle.getIId() : rolle.getAliasRolleIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public boolean hatRecht(String rechtCNr, TheClientDto theClientDto) {
		if(theClientDto == null) return false;
		Integer systemrolleIId = theClientDto.getSystemrolleIId();
		if (systemrolleIId == null) {
			systemrolleIId = getSystemrolleIId(theClientDto);
			theClientDto.setSystemrolleIId(systemrolleIId);
		}

		return getBenutzerServicesFac().hatRecht(rechtCNr, theClientDto);
	}

	/**
	 * Feststellen, ob ein Lock auf diesen Datensatz schon existiert.
	 * 
	 * @param lockMeI
	 *            LockMeDto
	 * @param theClientDto
	 *            String
	 * @return boolean
	 * @throws EJBExceptionLP
	 */
	public boolean isLocked(LockMeDto lockMeI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition.
		if (lockMeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"lockMeI == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrUserI == null"));
		}
		if (lockMeI.getPersonalIIdLocker() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"lockMeI.getPersonalIIdLocker() != null"));
		}

		boolean found = true;

		Thejudge theJudge = em.find(Thejudge.class,
				new TheJudgePK(lockMeI.getCWer(), lockMeI.getCWas(),/*
																	 * theClientDto.
																	 * getIDPersonal
																	 * (),
																	 */
				theClientDto.getIDUser()));
		if (theJudge == null) {
			found = false;
		}
		// }
		// catch (FinderException fe) {
		// found = false;

		return found;
	}

	/**
	 * Einen Lock hinzufuegen.
	 * 
	 * @param lockMe
	 *            LockMeDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void addLockedObject(LockMeDto lockMe, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition.
		if (lockMe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"theJudgeDto == null"));
		}
		try {
			myLogger.logData(lockMe, theClientDto.getIDUser());
			Thejudge theJudge = new Thejudge(lockMe.getCWer(),
					lockMe.getCWas(), lockMe.getCUsernr(),
					lockMe.getPersonalIIdLocker());
			em.persist(theJudge);
			em.flush();
			lockMe.setTWann(theJudge.getTWann());
			lockMe.setPersonalIIdLocker(theJudge.getPersonalIIdLocker());
			setTheJudgeFromTheJudgeDto(theJudge, lockMe);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	/**
	 * Einen Lock entfernen.
	 * 
	 * @param lockMeDto
	 *            LockedObject
	 * @throws EJBExceptionLP
	 */
	public void removeLockedObject(LockMeDto lockMeDto) throws EJBExceptionLP {
		TheJudgePK pk = new TheJudgePK(lockMeDto.getCWer(),
				lockMeDto.getCWas(),
				// lockMeDto.getPersonalIIdLocker(),
				lockMeDto.getCUsernr());
		Thejudge toRemove = em.find(Thejudge.class, pk);
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

	private void setTheJudgeFromTheJudgeDto(Thejudge thejudge,
			LockMeDto theJudgeDto) {
		thejudge.setTWann(theJudgeDto.getTWann());
		em.merge(thejudge);
		em.flush();
	}

	private LockMeDto assembleTheJudgeDto(Thejudge theJudge) {
		return TheJudgeDtoAssembler.createDto(theJudge);
	}

	private LockMeDto[] assembleTheJudgeDtos(Collection<?> theJudges) {
		List<LockMeDto> list = new ArrayList<LockMeDto>();
		if (theJudges != null) {
			Iterator<?> iterator = theJudges.iterator();
			while (iterator.hasNext()) {
				Thejudge theJudge = (Thejudge) iterator.next();
				list.add(assembleTheJudgeDto(theJudge));
			}
		}
		LockMeDto[] returnArray = new LockMeDto[list.size()];
		return (LockMeDto[]) list.toArray(returnArray);
	}

	public LockMeDto theJudgeFindByPrimaryKey(String sWerI, String sWasI,
			Integer iIdPersonal, String cNrUser) {
		LockMeDto lockMeDto = null;
		// try {
		Thejudge theJudge = em.find(Thejudge.class, new TheJudgePK(sWerI,
				sWasI, /* iIdPersonal, */cNrUser));
		if (theJudge == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lockMeDto = assembleTheJudgeDto(theJudge);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
		return lockMeDto;
	}

	public LockMeDto[] findByWerWas(String sWerI, String sWasI) {
		LockMeDto[] aLockMeDto = null;
		// try {
		Query query = em.createNamedQuery("TheJudgefindByWerWas");
		query.setParameter(1, sWerI);
		query.setParameter(2, sWasI);
		Collection<?> colLoclMe = query.getResultList();
		// if (colLoclMe.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		aLockMeDto = assembleTheJudgeDtos(colLoclMe);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
		return aLockMeDto;
	}

	public LockMeDto[] findByWerWasOhneExc(String sWerI, String sWasI) {
		LockMeDto[] aLockMeDto = null;
		// try {
		Query query = em.createNamedQuery("TheJudgefindByWerWas");
		query.setParameter(1, sWerI);
		query.setParameter(2, sWasI);
		Collection<?> colLoclMe = query.getResultList();
		// if (colLoclMe.isEmpty()) {
		// return null;
		// }
		aLockMeDto = assembleTheJudgeDtos(colLoclMe);
		// }
		// catch (FinderException fe) {
		// return null;
		// }
		return aLockMeDto;
	}
	
	public LockMeDto[] findMyLocks(TheClientDto theClientDto) {
		LockMeDto[] aLockMeDto = null;
		Query query = em.createNamedQuery("TheJudgefindByUsernr");
		query.setParameter("usernr", theClientDto.getIDUser());
		Collection<Thejudge> colLoclMe = query.getResultList();
		aLockMeDto = assembleTheJudgeDtos(colLoclMe);
		return aLockMeDto;		
	}
	
	public void removeMyLocks(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("TheJudgefindByUsernr");
		query.setParameter("usernr", theClientDto.getIDUser());
		Collection<Thejudge> colMyLocks = query.getResultList();
		for (Thejudge myLock : colMyLocks) {
			em.remove(myLock);
		}
		em.flush();
	}
}












































