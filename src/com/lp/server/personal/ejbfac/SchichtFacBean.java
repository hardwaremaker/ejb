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
package com.lp.server.personal.ejbfac;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.personal.ejb.Bereitschafttag;
import com.lp.server.personal.ejb.Schicht;
import com.lp.server.personal.ejb.Schichtzeit;
import com.lp.server.personal.ejb.Schichtzuschlag;
import com.lp.server.personal.service.BereitschafttagDto;
import com.lp.server.personal.service.BereitschafttagDtoAssembler;
import com.lp.server.personal.service.SchichtDto;
import com.lp.server.personal.service.SchichtDtoAssembler;
import com.lp.server.personal.service.SchichtFac;
import com.lp.server.personal.service.SchichtzeitDto;
import com.lp.server.personal.service.SchichtzeitDtoAssembler;
import com.lp.server.personal.service.SchichtzuschlagDto;
import com.lp.server.personal.service.SchichtzuschlagDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class SchichtFacBean extends Facade implements SchichtFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createSchichtzeit(SchichtzeitDto dto) {

		try {
			Query query = em
					.createNamedQuery("SchichtzeitfindBySchichtIIdUBeginn");
			query.setParameter(1, dto.getSchichtIId());
			query.setParameter(2, dto.getuBeginn());
			Schichtzeit doppelt = (Schichtzeit) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_SCHICHTZEIT.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		if (Helper.short2Boolean(dto.getBEndedestages()) == true) {
			dto.setuEnde(null);
		}

		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SCHICHTZEIT);
			dto.setIId(pk);

			Schichtzeit bean = new Schichtzeit(dto.getIId(),
					dto.getSchichtIId(), dto.getSchichtzuschlagIId(),
					dto.getuBeginn(), dto.getBEndedestages());
			em.persist(bean);
			em.flush();
			setSchichtzeitFromSchichtzeitDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public SchichtzeitDto schichtzeitFindByPrimaryKey(Integer iId) {
		Schichtzeit ialle = em.find(Schichtzeit.class, iId);
		return SchichtzeitDtoAssembler.createDto(ialle);
	}

	public SchichtzeitDto[] schichtzeitFindBySchichtIId(Integer schichtIId) {

		Query query = em.createNamedQuery("SchichtzeitfindBySchichtIId");
		query.setParameter(1, schichtIId);

		return SchichtzeitDtoAssembler.createDtos(query.getResultList());
	}

	public void updateSchichtzeit(SchichtzeitDto dto) {
		Schichtzeit ialle = em.find(Schichtzeit.class, dto.getIId());
		if (Helper.short2Boolean(dto.getBEndedestages()) == true) {
			dto.setuEnde(null);
		}
		try {
			Query query = em
					.createNamedQuery("SchichtzeitfindBySchichtIIdUBeginn");
			query.setParameter(1, dto.getSchichtIId());
			query.setParameter(2, dto.getuBeginn());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Schichtzeit) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SCHICHTZEIT.UK"));
			}
		} catch (NoResultException ex) {

		}

		setSchichtzeitFromSchichtzeitDto(ialle, dto);
	}

	public void removeSchichtzeit(SchichtzeitDto dto) {
		Schichtzeit toRemove = em.find(Schichtzeit.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Integer createSchicht(SchichtDto dto) {

		try {

			try {
				Query query = em
						.createNamedQuery(Schicht.QueryFindByMandantCNrCBez);
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getCBez());
				Schicht doppelt = (Schicht) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_SCHICHT.UK"));
				}
			} catch (NoResultException ex1) {
				// nothing here
			}

			
			if (dto.getBBegrenztAufTagessoll()==null) {
				dto.setBBegrenztAufTagessoll(Helper.boolean2Short(false));
			}
			
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SCHICHT);
			dto.setIId(pk);

			Schicht bean = new Schicht(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez(), dto.getBPausenabziehen(),dto.getBBegrenztAufTagessoll());
			em.persist(bean);
			em.flush();
			setSchichtFromSchichtDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public SchichtDto schichtFindByPrimaryKey(Integer iId) {
		Schicht ialle = em.find(Schicht.class, iId);
		return SchichtDtoAssembler.createDto(ialle);
	}

	public void updateSchicht(SchichtDto dto) {
		Schicht ialle = em.find(Schicht.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery(Schicht.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());

			Integer iIdVorhanden = ((Schicht) query.getSingleResult()).getIId();
			if (dto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SCHICHT.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setSchichtFromSchichtDto(ialle, dto);
	}

	public void removeSchicht(SchichtDto dto) {
		Schicht toRemove = em.find(Schicht.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setSchichtFromSchichtDto(Schicht bean, SchichtDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setBPausenabziehen(dto.getBPausenabziehen());
		bean.setBBegrenztAufTagessoll(dto.getBBegrenztAufTagessoll());
		
		em.merge(bean);
		em.flush();
	}

	private void setSchichtzeitFromSchichtzeitDto(Schichtzeit bean,
			SchichtzeitDto dto) {
		bean.setBEndedestages(dto.getBEndedestages());
		bean.setSchichtIId(dto.getSchichtIId());
		bean.setSchichtzuschlagIId(dto.getSchichtzuschlagIId());
		bean.setuBeginn(dto.getuBeginn());
		bean.setuEnde(dto.getuEnde());

		em.merge(bean);
		em.flush();
	}

	private void setSchichtzuschlagFromSchichtzuschlagDto(Schichtzuschlag bean,
			SchichtzuschlagDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setMandantCNr(dto.getMandantCNr());

		em.merge(bean);
		em.flush();
	}

	public Integer createSchichtzuschlag(SchichtzuschlagDto dto) {

		try {

			try {
				Query query = em
						.createNamedQuery(Schichtzuschlag.QueryFindByMandantCNrCBez);
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getCBez());
				Schichtzuschlag doppelt = (Schichtzuschlag) query
						.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_SCHICHTZUSCHLAG.UK"));
				}
			} catch (NoResultException ex1) {
				// nothing here
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SCHICHTZUSCHLAG);
			dto.setIId(pk);

			Schichtzuschlag bean = new Schichtzuschlag(dto.getIId(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setSchichtzuschlagFromSchichtzuschlagDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public SchichtzuschlagDto schichtzuschlagFindByPrimaryKey(Integer iId) {
		Schichtzuschlag ialle = em.find(Schichtzuschlag.class, iId);
		return SchichtzuschlagDtoAssembler.createDto(ialle);
	}

	public void updateSchichtzuschlag(SchichtzuschlagDto dto) {
		Schichtzuschlag ialle = em.find(Schichtzuschlag.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery(Schichtzuschlag.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());

			Integer iIdVorhanden = ((Schichtzuschlag) query.getSingleResult())
					.getIId();
			if (dto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SCHICHTZUSCHLAG.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setSchichtzuschlagFromSchichtzuschlagDto(ialle, dto);
	}

	public void removeSchichtzuschlag(SchichtzuschlagDto dto) {
		Schichtzuschlag toRemove = em.find(Schichtzuschlag.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

}