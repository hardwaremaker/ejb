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
package com.lp.server.partner.ejbfac;

import java.sql.Date;
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

import com.lp.server.partner.ejb.Kundesachbearbeiter;
import com.lp.server.partner.service.KundesachbearbeiterDto;
import com.lp.server.partner.service.KundesachbearbeiterDtoAssembler;
import com.lp.server.partner.service.KundesachbearbeiterFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class KundesachbearbeiterFacBean extends Facade implements
		KundesachbearbeiterFac {

	@PersistenceContext
	private EntityManager em;

	public Integer createKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (kundesachbearbeiterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kundesachbearbeiterDto == null"));
		}


		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUNDESACHBEARBEITER);
		kundesachbearbeiterDto.setIId(pk);

		// anlegen ...
		kundesachbearbeiterDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		kundesachbearbeiterDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());

		Kundesachbearbeiter kundesachbearbeiter = null;
		try {
			kundesachbearbeiter = new Kundesachbearbeiter(
					kundesachbearbeiterDto.getIId(), kundesachbearbeiterDto
							.getKundeIId(), kundesachbearbeiterDto
							.getPersonalIId(), kundesachbearbeiterDto
							.getFunktionIId(), kundesachbearbeiterDto
							.getTGueltigab(), kundesachbearbeiterDto
							.getPersonalIIdAnlegen(), kundesachbearbeiterDto
							.getPersonalIIdAendern());
			em.persist(kundesachbearbeiter);
  em.flush();

			// jetzt die ts holen und setzen wegen update
			kundesachbearbeiterDto.setTAendern(kundesachbearbeiter
					.getTAendern());
			kundesachbearbeiterDto.setTAnlegen(kundesachbearbeiter
					.getTAnlegen());

			setKundesachbearbeiterFromKundesachbearbeiterDto(
					kundesachbearbeiter, kundesachbearbeiterDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return kundesachbearbeiter.getIId();
	}

	public void removeKundesachbearbeiter(Integer iId) throws EJBExceptionLP {
		// try {
		Kundesachbearbeiter toRemove = em.find(Kundesachbearbeiter.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
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

	public void removeKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto)
			throws EJBExceptionLP {
		if (kundesachbearbeiterDto != null) {
			Integer iId = kundesachbearbeiterDto.getIId();
			removeKundesachbearbeiter(iId);
		}
	}

	public void updateKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (kundesachbearbeiterDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kundesachbearbeiterDto == null"));
		}


		// Uniquecheck: Person bei Kunde Gueltig ab bereits vorhanden?
		try {
			Query query = em
					.createNamedQuery("KundesachbearbeiterfindByKundePersonalGueltigAb");
			query.setParameter(1, kundesachbearbeiterDto.getKundeIId());
			query.setParameter(2, kundesachbearbeiterDto.getPersonalIId());
			query.setParameter(3, kundesachbearbeiterDto.getTGueltigab());
			// @todo getSingleResult oder getResultList ?
			Kundesachbearbeiter kundesachbearbeiter = (com.lp.server.partner.ejb.Kundesachbearbeiter) query
					.getSingleResult();
			if (kundesachbearbeiter.getIId().intValue() != kundesachbearbeiterDto
					.getIId().intValue()) {

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,"");
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		Integer iId = kundesachbearbeiterDto.getIId();
		// try {
		Kundesachbearbeiter kundesachbearbeiter = em.find(
				Kundesachbearbeiter.class, iId);
		if (kundesachbearbeiter == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}

		// Serverseitige Felder setzen.
		kundesachbearbeiter.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		kundesachbearbeiter.setPersonalIIdAendern(theClientDto.getIDPersonal());

		setKundesachbearbeiterFromKundesachbearbeiterDto(kundesachbearbeiter,
				kundesachbearbeiterDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public KundesachbearbeiterDto kundesachbearbeiterFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		// try {
		Kundesachbearbeiter kundesachbearbeiter = em.find(
				Kundesachbearbeiter.class, iId);
		if (kundesachbearbeiter == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}
		return assembleKundesachbearbeiterDto(kundesachbearbeiter);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public KundesachbearbeiterDto[] kundesachbearbeiterFindByKundeIId(
			Integer iIdKundeI) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KundesachbearbeiterfindByKundeIId");
		query.setParameter(1, iIdKundeI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKundesachbearbeiterDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesachbearbeiterDto[] kundesachbearbeiterFindByKundeIIdOhneExc(
			Integer iIdKundeI) {

		Query query = em.createNamedQuery("KundesachbearbeiterfindByKundeIId");
		query.setParameter(1, iIdKundeI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleKundesachbearbeiterDtos(cl);
	}

	public KundesachbearbeiterDto kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAb(
			Integer iIdKundeI, Integer iIdPersonalI, java.sql.Date dGueltigAb)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("KundesachbearbeiterfindByKundePersonalGueltigAb");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, iIdPersonalI);
		query.setParameter(3, dGueltigAb);
		Kundesachbearbeiter kundesachbearbeiter = (Kundesachbearbeiter) query
				.getSingleResult();
		if (kundesachbearbeiter == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		}
		return assembleKundesachbearbeiterDto(kundesachbearbeiter);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesachbearbeiterDto kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAbOhneExc(
			Integer iIdKundeI, Integer iIdPersonalI, Date dGueltigAb) {
		Kundesachbearbeiter kundesachbearbeiter;
		try {
			Query query = em
					.createNamedQuery("KundesachbearbeiterfindByKundePersonalGueltigAb");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, iIdPersonalI);
			query.setParameter(3, dGueltigAb);
			kundesachbearbeiter = (Kundesachbearbeiter) query
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

		return assembleKundesachbearbeiterDto(kundesachbearbeiter);
	}

	private void setKundesachbearbeiterFromKundesachbearbeiterDto(
			Kundesachbearbeiter kundesachbearbeiter,
			KundesachbearbeiterDto kundesachbearbeiterDto) {

		kundesachbearbeiter.setKundeIId(kundesachbearbeiterDto.getKundeIId());
		kundesachbearbeiter.setPersonalIId(kundesachbearbeiterDto
				.getPersonalIId());
		kundesachbearbeiter.setFunktionIId(kundesachbearbeiterDto
				.getFunktionIId());
		kundesachbearbeiter.setTGueltigab(kundesachbearbeiterDto
				.getTGueltigab());
		kundesachbearbeiter.setTAnlegen(kundesachbearbeiterDto.getTAnlegen());
		kundesachbearbeiter.setPersonalIIdAnlegen(kundesachbearbeiterDto
				.getPersonalIIdAnlegen());
		kundesachbearbeiter.setTAendern(kundesachbearbeiterDto.getTAendern());
		kundesachbearbeiter.setPersonalIIdAendern(kundesachbearbeiterDto
				.getPersonalIIdAendern());
		em.merge(kundesachbearbeiter);
  em.flush();
	}

	private KundesachbearbeiterDto assembleKundesachbearbeiterDto(
			Kundesachbearbeiter kundesachbearbeiter) {
		return KundesachbearbeiterDtoAssembler.createDto(kundesachbearbeiter);
	}

	private KundesachbearbeiterDto[] assembleKundesachbearbeiterDtos(
			Collection<?> kundesachbearbeiters) {
		List<KundesachbearbeiterDto> list = new ArrayList<KundesachbearbeiterDto>();
		if (kundesachbearbeiters != null) {
			Iterator<?> iterator = kundesachbearbeiters.iterator();
			while (iterator.hasNext()) {
				Kundesachbearbeiter kundesachbearbeiter = (Kundesachbearbeiter) iterator
						.next();
				list.add(assembleKundesachbearbeiterDto(kundesachbearbeiter));
			}
		}
		KundesachbearbeiterDto[] returnArray = new KundesachbearbeiterDto[list
				.size()];
		return (KundesachbearbeiterDto[]) list.toArray(returnArray);
	}

}
