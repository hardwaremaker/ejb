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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.personal.ejb.Maschinemaschinenzm;
import com.lp.server.personal.ejb.Maschinenzm;
import com.lp.server.personal.ejb.Maschinenzmtagesart;
import com.lp.server.personal.ejb.Tagesart;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinemaschinenzm;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.MaschineFac;
import com.lp.server.personal.service.MaschinemaschinenzmDto;
import com.lp.server.personal.service.MaschinemaschinenzmDtoAssembler;
import com.lp.server.personal.service.MaschinenVerfuegbarkeitsStundenDto;
import com.lp.server.personal.service.MaschinenzmDto;
import com.lp.server.personal.service.MaschinenzmDtoAssembler;
import com.lp.server.personal.service.MaschinenzmtagesartDto;
import com.lp.server.personal.service.MaschinenzmtagesartDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class MaschineFacBean extends Facade implements MaschineFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createMaschinemaschinenzm(MaschinemaschinenzmDto dto) {

		dto.setTGueltigab(Helper.cutTimestamp(dto.getTGueltigab()));

		try {
			Query query = em
					.createNamedQuery("MaschinemaschinenzmfindByMaschineIIdMaschinezmIIdTGueltigab");
			query.setParameter(1, dto.getMaschineIId());
			query.setParameter(2, dto.getMaschinenzmIId());
			query.setParameter(3, dto.getTGueltigab());
			Maschinemaschinenzm doppelt = (Maschinemaschinenzm) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINEMASCHINENZM.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINEMASCHIENEZM);
		dto.setIId(pk);

		try {

			Maschinemaschinenzm maschinemaschinenzm = new Maschinemaschinenzm(
					dto.getIId(), dto.getMaschineIId(),
					dto.getMaschinenzmIId(), dto.getTGueltigab());
			em.persist(maschinemaschinenzm);
			em.flush();
			setMaschinemaschinenzmFromMaschinemaschinenzmDto(
					maschinemaschinenzm, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return dto.getIId();
	}

	private void setMaschinemaschinenzmFromMaschinemaschinenzmDto(
			Maschinemaschinenzm maschinemaschinenzm,
			MaschinemaschinenzmDto maschinemaschinenzmDto) {
		maschinemaschinenzm.setMaschineIId(maschinemaschinenzmDto
				.getMaschineIId());
		maschinemaschinenzm.setMaschinezmIId(maschinemaschinenzmDto
				.getMaschinenzmIId());
		maschinemaschinenzm.setTGueltigab(maschinemaschinenzmDto
				.getTGueltigab());
		em.merge(maschinemaschinenzm);
		em.flush();
	}

	private void setMaschinenzmFromMaschinenzmDto(Maschinenzm maschinenzm,
			MaschinenzmDto maschinenzmDto) {
		maschinenzm.setMandantCNr(maschinenzmDto.getMandantCNr());

		maschinenzm.setCBez(maschinenzmDto.getCBez());
		em.merge(maschinenzm);
		em.flush();
	}

	private void setMaschinenzmtagesartFromMaschinenzmtagesartDto(
			Maschinenzmtagesart maschinenzmtagesart,
			MaschinenzmtagesartDto maschinenzmtagesartDto) {
		maschinenzmtagesart.setLVerfuegbarkeit(maschinenzmtagesartDto
				.getLVerfuegbarkeit());
		maschinenzmtagesart.setTagesartIId(maschinenzmtagesartDto
				.getTagesartIId());
		maschinenzmtagesart.setMaschinenzmIId(maschinenzmtagesartDto
				.getMaschinenzmIId());

		em.merge(maschinenzmtagesart);
		em.flush();
	}

	public void updateMaschinemaschinenzm(MaschinemaschinenzmDto dto) {

		dto.setTGueltigab(Helper.cutTimestamp(dto.getTGueltigab()));

		Integer iId = dto.getIId();

		Maschinemaschinenzm maschinemaschinenzm = em.find(
				Maschinemaschinenzm.class, iId);

		try {
			Query query = em
					.createNamedQuery("MaschinemaschinenzmfindByMaschineIIdMaschinezmIIdTGueltigab");
			query.setParameter(1, dto.getMaschineIId());
			query.setParameter(2, dto.getMaschinenzmIId());
			query.setParameter(3, dto.getTGueltigab());
			Integer iIdVorhanden = ((Maschinemaschinenzm) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINEMASCHINENZM.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setMaschinemaschinenzmFromMaschinemaschinenzmDto(maschinemaschinenzm,
				dto);

	}

	public void removeMaschinemaschinenzm(MaschinemaschinenzmDto dto) {

		Maschinemaschinenzm maschinemaschinenzm = null;
		try {
			maschinemaschinenzm = em.find(Maschinemaschinenzm.class,
					dto.getIId());
			if (maschinemaschinenzm == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			em.remove(maschinemaschinenzm);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	private MaschinemaschinenzmDto assembleMaschinemaschinenzmDto(
			Maschinemaschinenzm maschinemaschinenzm) {
		return MaschinemaschinenzmDtoAssembler.createDto(maschinemaschinenzm);
	}

	private MaschinenzmDto assembleMaschinenzmDto(Maschinenzm maschinenzm) {
		return MaschinenzmDtoAssembler.createDto(maschinenzm);
	}

	private MaschinenzmtagesartDto assembleMaschinenzmtagesartDto(
			Maschinenzmtagesart maschinenzmtagesart) {
		return MaschinenzmtagesartDtoAssembler.createDto(maschinenzmtagesart);
	}

	public MaschinemaschinenzmDto maschinemaschinenzmFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) {
		Maschinemaschinenzm maschinemaschinenzm = em.find(
				Maschinemaschinenzm.class, iId);
		MaschinemaschinenzmDto personalzeitmodellDto = assembleMaschinemaschinenzmDto(maschinemaschinenzm);
		return personalzeitmodellDto;
	}

	public MaschinenzmDto maschinezmFindByPrimaryKey(Integer iId) {
		Maschinenzm maschinemaschinenzm = em.find(Maschinenzm.class, iId);
		MaschinenzmDto maschinenzmDto = assembleMaschinenzmDto(maschinemaschinenzm);
		return maschinenzmDto;
	}

	public void removeMaschinezm(MaschinenzmDto dto) {

		Maschinenzm maschinenzm = null;
		try {
			maschinenzm = em.find(Maschinenzm.class, dto.getIId());
			em.remove(maschinenzm);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public Integer createMaschinenzm(MaschinenzmDto dto,
			TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("MaschinenzmfindByMandantCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Maschinenzm doppelt = (Maschinenzm) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINENZM.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENZM);
		dto.setIId(pk);

		try {

			Maschinenzm maschinenzm = new Maschinenzm(dto.getIId(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(maschinenzm);
			em.flush();
			setMaschinenzmFromMaschinenzmDto(maschinenzm, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return dto.getIId();
	}

	public void updateMaschinenzm(MaschinenzmDto dto) {

		Integer iId = dto.getIId();

		Maschinenzm maschinemaschinenzm = em.find(Maschinenzm.class, iId);

		try {
			Query query = em.createNamedQuery("MaschinenzmfindByMandantCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());

			Integer iIdVorhanden = ((Maschinenzm) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINEMASCHINENZM.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setMaschinenzmFromMaschinenzmDto(maschinemaschinenzm, dto);

	}

	public Integer createMaschinenzmtagesart(MaschinenzmtagesartDto dto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("MaschinenzmtagesartfindByMaschinenzmIIdTagesartIId");
			query.setParameter(1, dto.getMaschinenzmIId());
			query.setParameter(2, dto.getTagesartIId());
			Maschinenzmtagesart doppelt = (Maschinenzmtagesart) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINENZMTAGESART.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENZMTAGESART);
		dto.setIId(pk);

		try {

			Maschinenzmtagesart maschinenzmtagesart = new Maschinenzmtagesart(
					dto.getIId(), dto.getMaschinenzmIId(),
					dto.getTagesartIId(), dto.getLVerfuegbarkeit());
			em.persist(maschinenzmtagesart);
			em.flush();
			setMaschinenzmtagesartFromMaschinenzmtagesartDto(
					maschinenzmtagesart, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return dto.getIId();
	}

	public void updateMaschinenzmtagesart(MaschinenzmtagesartDto dto) {

		Integer iId = dto.getIId();

		Maschinenzmtagesart maschinenzmtagesart = em.find(
				Maschinenzmtagesart.class, iId);

		try {
			Query query = em
					.createNamedQuery("MaschinenzmtagesartfindByMaschinenzmIIdTagesartIId");
			query.setParameter(1, dto.getMaschinenzmIId());
			query.setParameter(2, dto.getTagesartIId());

			Integer iIdVorhanden = ((Maschinenzmtagesart) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINEMASCHINENZM.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setMaschinenzmtagesartFromMaschinenzmtagesartDto(maschinenzmtagesart,
				dto);

	}

	public void removeMaschinenzmtagesart(MaschinenzmtagesartDto dto) {

		Maschinenzmtagesart maschinenzmtagesart = null;
		try {
			maschinenzmtagesart = em.find(Maschinenzmtagesart.class,
					dto.getIId());
			em.remove(maschinenzmtagesart);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public MaschinenzmtagesartDto maschinezmtagesartFindByPrimaryKey(Integer iId) {
		Maschinenzmtagesart maschinenzmtagesart = em.find(
				Maschinenzmtagesart.class, iId);
		MaschinenzmtagesartDto maschinenzmtagesartDto = assembleMaschinenzmtagesartDto(maschinenzmtagesart);
		return maschinenzmtagesartDto;
	}

	private Integer getTagesartZuDatum(
			java.sql.Date d_datum, TheClientDto theClientDto) throws EJBExceptionLP {
		d_datum = Helper.cutDate(d_datum);

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(
							new Timestamp(d_datum.getTime()),
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				tagesartIId = dto.getTagesartIId();
			}

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
		return tagesartIId;
	}

	public BigDecimal getVerfuegbarkeitInStundenZuDatum(Integer maschineIId,
			java.sql.Date dDatum, TheClientDto theClientDto) {
		return getVerfuegbarkeitInStunden(maschineIId, dDatum, theClientDto).getVerfuegbarkeitH() ;
//		
//		BigDecimal verf = java.math.BigDecimal.ZERO;
//
//		String sQuery = "select zm FROM FLRMaschinemaschinenzm zm WHERE zm.maschine_i_id="
//				+ maschineIId
//				+ " AND zm.t_gueltigab<='"
//				+ Helper.formatDateWithSlashes(dDatum)
//				+ "'  ORDER BY zm.t_gueltigab DESC";
//
//		Session session = FLRSessionFactory.getFactory().openSession();
//
//		org.hibernate.Query letztesZeitmodell = session.createQuery(sQuery);
//		letztesZeitmodell.setMaxResults(1);
//
//		List<?> resultList = letztesZeitmodell.list();
//
//		Iterator<?> resultListIterator = resultList.iterator();
//
//		if (resultListIterator.hasNext()) {
//			FLRMaschinemaschinenzm zm = (FLRMaschinemaschinenzm) resultListIterator
//					.next();
//
//			Integer tagesartIId = getTagesartZuDatum(dDatum, theClientDto);
//
//			if (tagesartIId != null) {
//
//				try {
//					Query query = em
//							.createNamedQuery("MaschinenzmtagesartfindByMaschinenzmIIdTagesartIId");
//					query.setParameter(1, zm.getFlrzeitmodell().getI_id());
//					query.setParameter(2, tagesartIId);
//					Maschinenzmtagesart zmtagesart = (Maschinenzmtagesart) query
//							.getSingleResult();
//
//					verf = Helper
//							.verfuegbarkeitvonLongNachStundenEinesTages(zmtagesart
//									.getLVerfuegbarkeit());
//
//				} catch (NoResultException ex1) {
//
//				}
//
//			}
//
//		}
//		return verf;
//
	}


	private MaschinenVerfuegbarkeitsStundenDto getVerfuegbarkeitInStunden(Integer maschineIId,
			java.sql.Date dDatum, TheClientDto theClientDto) {		
		MaschinenVerfuegbarkeitsStundenDto dto = 
			new MaschinenVerfuegbarkeitsStundenDto(maschineIId, dDatum) ;
		
		String sQuery = "select zm FROM FLRMaschinemaschinenzm zm WHERE zm.maschine_i_id="
				+ maschineIId
				+ " AND zm.t_gueltigab<='"
				+ Helper.formatDateWithSlashes(dDatum)
				+ "' ORDER BY zm.t_gueltigab DESC";

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query letztesZeitmodell = session.createQuery(sQuery);
		letztesZeitmodell.setMaxResults(1);
		List<?> resultList = letztesZeitmodell.list();

		if(resultList.size() > 0){
			FLRMaschinemaschinenzm zm = (FLRMaschinemaschinenzm) resultList.get(0) ;
			dto.setTagesartId(getTagesartZuDatum(dDatum, theClientDto)) ;
			if (dto.getTagesartId() != null) {
				try {
					Query query = em
							.createNamedQuery("MaschinenzmtagesartfindByMaschinenzmIIdTagesartIId");
					query.setParameter(1, zm.getFlrzeitmodell().getI_id());
					query.setParameter(2, dto.getTagesartId());
					Maschinenzmtagesart zmtagesart = (Maschinenzmtagesart) query
							.getSingleResult();

					dto.setVerfuegbarkeitH(Helper
							.verfuegbarkeitvonLongNachStundenEinesTages(zmtagesart
									.getLVerfuegbarkeit()));
				} catch (NoResultException ex1) {
				}
			}
			
		}

		session.close() ;
		return dto ;
	}
	
	
	public List<MaschinenVerfuegbarkeitsStundenDto> getVerfuegbarkeitInStunden(
			Integer maschineId, Date startDate, int days, TheClientDto theClientDto) {
		List<MaschinenVerfuegbarkeitsStundenDto> dtos = new ArrayList<MaschinenVerfuegbarkeitsStundenDto>() ;
		
		Calendar c = Calendar.getInstance() ;
		Date d = startDate ;
		c.setTime(d) ;
		do {
			dtos.add(getVerfuegbarkeitInStunden(maschineId, d, theClientDto)) ;

			c.add(Calendar.DAY_OF_YEAR, 1);
			d = new Date(c.getTime().getTime()) ;
		} while(--days > 0) ;
		
		return dtos ;
 	}
}