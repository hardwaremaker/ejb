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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Automatikjobs;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobDtoAssembler;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutomatikjobFacBean extends Facade implements AutomatikjobFac {

	@PersistenceContext
	private EntityManager em;

	public AutomatikjobDto[] automatikjobFindByBActive(Integer active)
	throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobfindByBActive");
		query.setParameter(1, active);
		return assembleAutomatikjobDtos(query.getResultList());
	}

	public AutomatikjobDto[] automatikjobFindByBMonthjob(Integer monthjob)
	throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobfindByBMonthjob");
		query.setParameter(1, monthjob);
		return assembleAutomatikjobDtos(query.getResultList());
	}

	public AutomatikjobDto automatikjobFindByCMandantCNr(String mandantCNr)
	throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobfindByCMandantCNr");
		query.setParameter(1, mandantCNr);
		return assembleAutomatikjobDto((Automatikjobs) query.getSingleResult());
	}

	public AutomatikjobDto automatikjobFindByCName(String name)
	throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobfindByCName");
		query.setParameter(1, name);
		return assembleAutomatikjobDto((Automatikjobs) query.getSingleResult());
	}

	public AutomatikjobDto automatikjobFindByISort(Integer sort)
	throws RemoteException {
		try{
			Query query = em.createNamedQuery("AutomatikjobfindByISort");
			query.setParameter(1, sort);
			Automatikjobs automatikjobs = (Automatikjobs)query.getSingleResult();
			return assembleAutomatikjobDto(automatikjobs);
		} catch(NoResultException ex){
			throw new RemoteException();
		}
	}


	public AutomatikjobDto automatikjobFindByPrimaryKey(Integer id)
	throws RemoteException {
		Automatikjobs automatikjobs = em.find(Automatikjobs.class, id);
		if(automatikjobs==null){
			return null;
		}
		return assembleAutomatikjobDto(automatikjobs);
	}

	public AutomatikjobDto[] automatikjobFindBydNextperform(
			Timestamp nextPerform) throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobfindBydNextperform");
		query.setParameter(1, nextPerform);
		return assembleAutomatikjobDtos(query.getResultList());
	}

	public void createAutomatikjob(AutomatikjobDto automatikjobDto)
	throws RemoteException {
		Automatikjobs automatikjobs = new Automatikjobs();
		automatikjobs = setAutomatikjobFromAutomatikjobDto(automatikjobs,
				automatikjobDto);
		em.persist(automatikjobs);
		em.flush();
	}

	public void removeAutomatikjob(Integer id) throws RemoteException {
		Automatikjobs toRemove = em.find(Automatikjobs.class, id);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAutomatikjob(AutomatikjobDto automatikjobDto)
	throws RemoteException {
		removeAutomatikjob(automatikjobDto.getIId());

	}

	public void updateAutomatikjob(AutomatikjobDto automatikjobDto)
	throws RemoteException {
		Automatikjobs automatikjobs = em.find(Automatikjobs.class, automatikjobDto.getIId());
		automatikjobs = setAutomatikjobFromAutomatikjobDto(automatikjobs,
				automatikjobDto);
		em.merge(automatikjobs);
		em.flush();
	}

	public void updateAutomatikjobs(AutomatikjobDto[] automatikjobDtos)
	throws RemoteException {
		for (int i = 0; i < automatikjobDtos.length; i++) {
			Automatikjobs automatikjobs = new Automatikjobs();
			automatikjobs = setAutomatikjobFromAutomatikjobDto(automatikjobs,
					automatikjobDtos[i]);
			em.persist(automatikjobs);
			em.flush();
		}

	}

	private AutomatikjobDto[] assembleAutomatikjobDtos(
			Collection<?> automatikjobs) {
		List<AutomatikjobDto> list = new ArrayList<AutomatikjobDto>();
		if (automatikjobs != null) {
			Iterator<?> iterator = automatikjobs.iterator();
			while (iterator.hasNext()) {
				Automatikjobs automatikjob = (Automatikjobs) iterator.next();
				list.add(assembleAutomatikjobDto(automatikjob));
			}
		}
		AutomatikjobDto[] returnArray = new AutomatikjobDto[list.size()];
		return (AutomatikjobDto[]) list.toArray(returnArray);
	}

	private AutomatikjobDto assembleAutomatikjobDto(Automatikjobs automatikjob) {
		return AutomatikjobDtoAssembler.createDto(automatikjob);
	}

	private Automatikjobs setAutomatikjobFromAutomatikjobDto(
			Automatikjobs automatikjob, AutomatikjobDto automatikjobDto) {
		automatikjob.setCName(automatikjobDto.getCName());
		automatikjob.setCBeschreibung(automatikjobDto.getCBeschreibung());
		automatikjob.setBActive(automatikjobDto.getBActive());
		automatikjob.setDLastperformed(automatikjobDto.getDLastperformed());
		automatikjob.setDNextperform(automatikjobDto.getDNextperform());
		automatikjob.setIIntervall(automatikjobDto.getIIntervall());
		automatikjob.setBMonthjob(automatikjobDto.getBMonthjob());
		automatikjob.setISort(automatikjobDto.getISort());
		automatikjob.setIAutomatikjobtypeIid(automatikjobDto
				.getIAutomatikjobtypeIid());
		automatikjob.setBPerformonnonworkingdays(automatikjobDto
				.getBPerformOnNonWOrkingDays());
		return automatikjob;
	}

}
