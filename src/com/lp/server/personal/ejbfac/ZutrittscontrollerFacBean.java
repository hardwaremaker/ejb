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
package com.lp.server.personal.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.personal.ejb.Fingerart;
import com.lp.server.personal.ejb.Personalfinger;
import com.lp.server.personal.ejb.Personalzutrittsklasse;
import com.lp.server.personal.ejb.Zutrittdaueroffen;
import com.lp.server.personal.ejb.Zutrittonlinecheck;
import com.lp.server.personal.ejb.Zutrittscontroller;
import com.lp.server.personal.ejb.Zutrittsklasse;
import com.lp.server.personal.ejb.Zutrittsklasseobjekt;
import com.lp.server.personal.ejb.Zutrittsleser;
import com.lp.server.personal.ejb.Zutrittslog;
import com.lp.server.personal.ejb.Zutrittsmodell;
import com.lp.server.personal.ejb.Zutrittsmodelltag;
import com.lp.server.personal.ejb.Zutrittsmodelltagdetail;
import com.lp.server.personal.ejb.Zutrittsobjekt;
import com.lp.server.personal.ejb.Zutrittsobjektverwendung;
import com.lp.server.personal.ejb.Zutrittsoeffnungsart;
import com.lp.server.personal.fastlanereader.generated.FLRZutrittonlinecheck;
import com.lp.server.personal.fastlanereader.generated.FLRZutrittsklasseobjekt;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.FingerartDto;
import com.lp.server.personal.service.FingerartDtoAssembler;
import com.lp.server.personal.service.PersonalfingerDto;
import com.lp.server.personal.service.PersonalfingerDtoAssembler;
import com.lp.server.personal.service.PersonalzutrittsklasseDto;
import com.lp.server.personal.service.PersonalzutrittsklasseDtoAssembler;
import com.lp.server.personal.service.TagesartDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZutrittdaueroffenDto;
import com.lp.server.personal.service.ZutrittdaueroffenDtoAssembler;
import com.lp.server.personal.service.ZutrittonlinecheckDto;
import com.lp.server.personal.service.ZutrittonlinecheckDtoAssembler;
import com.lp.server.personal.service.ZutrittscontrollerDto;
import com.lp.server.personal.service.ZutrittscontrollerDtoAssembler;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.personal.service.ZutrittsklasseDtoAssembler;
import com.lp.server.personal.service.ZutrittsklasseobjektDto;
import com.lp.server.personal.service.ZutrittsklasseobjektDtoAssembler;
import com.lp.server.personal.service.ZutrittsleserDto;
import com.lp.server.personal.service.ZutrittsleserDtoAssembler;
import com.lp.server.personal.service.ZutrittslogDto;
import com.lp.server.personal.service.ZutrittslogDtoAssembler;
import com.lp.server.personal.service.ZutrittsmodellDto;
import com.lp.server.personal.service.ZutrittsmodellDtoAssembler;
import com.lp.server.personal.service.ZutrittsmodelltagDto;
import com.lp.server.personal.service.ZutrittsmodelltagDtoAssembler;
import com.lp.server.personal.service.ZutrittsmodelltagdetailDto;
import com.lp.server.personal.service.ZutrittsmodelltagdetailDtoAssembler;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.personal.service.ZutrittsobjektDtoAssembler;
import com.lp.server.personal.service.ZutrittsobjektverwendungDto;
import com.lp.server.personal.service.ZutrittsobjektverwendungDtoAssembler;
import com.lp.server.personal.service.ZutrittsoeffnungsartDto;
import com.lp.server.personal.service.ZutrittsoeffnungsartDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ZutrittscontrollerFacBean extends Facade implements
		ZutrittscontrollerFac {
	@PersistenceContext
	private EntityManager em;
	private Zutrittsmodelltag zutrittsmodelltag;

	public Integer createZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP {
		if (zutrittscontrollerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittscontrollerDto == null"));
		}
		if (zutrittscontrollerDto.getCBez() == null
				|| zutrittscontrollerDto.getCAdresse() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittscontrollerDto.getCBez() == null || zutrittscontrollerDto.getCAdresse() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZutrittscontrollerfindByCNr");
			query.setParameter(1, zutrittscontrollerDto.getCNr());
			Zutrittscontroller doppelt = (Zutrittscontroller) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSCONTROLER.C_NR"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSCONTROLLER);
		zutrittscontrollerDto.setIId(pk);

		try {
			Zutrittscontroller zutrittscontroller = new Zutrittscontroller(
					zutrittscontrollerDto.getIId(), zutrittscontrollerDto
							.getCBez(), zutrittscontrollerDto.getCAdresse(),
					zutrittscontrollerDto.getCNr());
			em.persist(zutrittscontroller);
			em.flush();
			setZutrittscontrollerFromZutrittscontrollerDto(zutrittscontroller,
					zutrittscontrollerDto);

			return zutrittscontrollerDto.getIId();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittscontrollerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzutrittsklasseDto == null"));
		}
		if (zutrittscontrollerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittscontroller toRemove = em.find(Zutrittscontroller.class,
				zutrittscontrollerDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updateZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP {
		if (zutrittscontrollerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittscontrollerDto == null"));
		}
		if (zutrittscontrollerDto.getIId() == null
				|| zutrittscontrollerDto.getCBez() == null
				|| zutrittscontrollerDto.getCAdresse() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittscontrollerDto.getIId() == null || zutrittscontrollerDto.getCBez() == null || zutrittscontrollerDto.getCAdresse() == null"));
		}

		Integer iId = zutrittscontrollerDto.getIId();
		// try {
		Zutrittscontroller zutrittscontroller = em.find(
				Zutrittscontroller.class, iId);
		if (zutrittscontroller == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("ZutrittscontrollerfindByCNr");
			query.setParameter(1, zutrittscontrollerDto.getCNr());
			Integer iIdVorhanden = ((Zutrittscontroller) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSCONTROLLER.C_NR"));
			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setZutrittscontrollerFromZutrittscontrollerDto(zutrittscontroller,
				zutrittscontrollerDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittscontrollerDto zutrittscontrollerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zutrittscontroller zutrittscontroller = em.find(
				Zutrittscontroller.class, iId);
		if (zutrittscontroller == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittscontrollerDto(zutrittscontroller);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZutrittscontrollerDto zutrittscontrollerFindByCNr(String cBez)
			throws EJBExceptionLP {
		if (cBez == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cBez == null"));
		}
		try {
			Query query = em.createNamedQuery("ZutrittscontrollerfindByCNr");
			query.setParameter(1, cBez);
			Zutrittscontroller zutrittscontroller = (Zutrittscontroller) query
					.getSingleResult();
			if (zutrittscontroller == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleZutrittscontrollerDto(zutrittscontroller);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	private void setZutrittscontrollerFromZutrittscontrollerDto(
			Zutrittscontroller zutrittscontroller,
			ZutrittscontrollerDto zutrittscontrollerDto) {
		zutrittscontroller.setCBez(zutrittscontrollerDto.getCBez());
		zutrittscontroller.setCNr(zutrittscontrollerDto.getCNr());
		zutrittscontroller.setCAdresse(zutrittscontrollerDto.getCAdresse());
		em.merge(zutrittscontroller);
		em.flush();
	}

	private ZutrittscontrollerDto assembleZutrittscontrollerDto(
			Zutrittscontroller zutrittscontroller) {
		return ZutrittscontrollerDtoAssembler.createDto(zutrittscontroller);
	}

	private ZutrittscontrollerDto[] assembleZutrittscontrollerDtos(
			Collection<?> zutrittscontrollers) {
		List<ZutrittscontrollerDto> list = new ArrayList<ZutrittscontrollerDto>();
		if (zutrittscontrollers != null) {
			Iterator<?> iterator = zutrittscontrollers.iterator();
			while (iterator.hasNext()) {
				Zutrittscontroller zutrittscontroller = (Zutrittscontroller) iterator
						.next();
				list.add(assembleZutrittscontrollerDto(zutrittscontroller));
			}
		}
		ZutrittscontrollerDto[] returnArray = new ZutrittscontrollerDto[list
				.size()];
		return (ZutrittscontrollerDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP {
		if (zutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseDto == null"));
		}
		if (zutrittsklasseDto.getCNr() == null
				|| zutrittsklasseDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsklasseDto.getCNr() == null || zutrittsklasseDto.getMandantCNr() == null"));
		}
		if (zutrittsklasseDto.getCNr().equals(
				ZutrittscontrollerFac.ZUTRITTSKLASSE_ONLINECHECK)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception(
							"Zutrittsklasse 'ONL' darf nicht agelegt werden"));

		}
		try {
			Query query = em.createNamedQuery("ZutrittsklassefindByCNr");
			query.setParameter(1, zutrittsklasseDto.getCNr());
			Zutrittsklasse doppelt = (Zutrittsklasse) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSKLASSE.C_NR"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSKLASSE);
		zutrittsklasseDto.setIId(pk);

		try {
			Zutrittsklasse zutrittsklasse = new Zutrittsklasse(
					zutrittsklasseDto.getIId(), zutrittsklasseDto.getCNr(),
					zutrittsklasseDto.getMandantCNr());
			em.persist(zutrittsklasse);
			em.flush();
			setZutrittsklasseFromZutrittsklasseDto(zutrittsklasse,
					zutrittsklasseDto);
			return zutrittsklasseDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseDto == null"));
		}
		if (zutrittsklasseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsklasse toRemove = em.find(Zutrittsklasse.class,
				zutrittsklasseDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP {
		if (zutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseDto == null"));
		}
		if (zutrittsklasseDto.getIId() == null
				|| zutrittsklasseDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsklasseDto.getIId() == null || zutrittsklasseDto.getCNr() == null"));
		}
		if (zutrittsklasseDto.getCNr().equals(
				ZutrittscontrollerFac.ZUTRITTSKLASSE_ONLINECHECK)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception(
							"Zutrittsklasse 'ONL' darf nicht agelegt werden"));

		}

		Integer iId = zutrittsklasseDto.getIId();
		// try {
		Zutrittsklasse zutrittsklasse = em.find(Zutrittsklasse.class, iId);
		if (zutrittsklasse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("ZutrittsklassefindByCNr");
			query.setParameter(1, zutrittsklasseDto.getCNr());
			Integer iIdVorhanden = ((Zutrittsklasse) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSKLASSE.C_NR"));
			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setZutrittsklasseFromZutrittsklasseDto(zutrittsklasse,
				zutrittsklasseDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsklasseDto zutrittsklasseFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zutrittsklasse zutrittsklasse = em.find(Zutrittsklasse.class, iId);
		if (zutrittsklasse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsklasseDto(zutrittsklasse);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZutrittsklasseDto zutrittsklasseFindByCNr(String cNr)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}
		try {
			Query query = em.createNamedQuery("ZutrittsklassefindByCNr");
			query.setParameter(1, cNr);
			Zutrittsklasse zutrittsklasse = (Zutrittsklasse) query
					.getSingleResult();
			if (zutrittsklasse == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleZutrittsklasseDto(zutrittsklasse);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	private void setZutrittsklasseFromZutrittsklasseDto(
			Zutrittsklasse zutrittsklasse, ZutrittsklasseDto zutrittsklasseDto) {
		zutrittsklasse.setCNr(zutrittsklasseDto.getCNr());
		zutrittsklasse.setCBez(zutrittsklasseDto.getCBez());
		zutrittsklasse.setMandantCNr(zutrittsklasseDto.getMandantCNr());
		em.merge(zutrittsklasse);
		em.flush();
	}

	private ZutrittsklasseDto assembleZutrittsklasseDto(
			Zutrittsklasse zutrittsklasse) {
		return ZutrittsklasseDtoAssembler.createDto(zutrittsklasse);
	}

	private ZutrittsklasseDto[] assembleZutrittsklasseDtos(
			Collection<?> zutrittsklasses) {
		List<ZutrittsklasseDto> list = new ArrayList<ZutrittsklasseDto>();
		if (zutrittsklasses != null) {
			Iterator<?> iterator = zutrittsklasses.iterator();
			while (iterator.hasNext()) {
				Zutrittsklasse zutrittsklasse = (Zutrittsklasse) iterator
						.next();
				list.add(assembleZutrittsklasseDto(zutrittsklasse));
			}
		}
		ZutrittsklasseDto[] returnArray = new ZutrittsklasseDto[list.size()];
		return (ZutrittsklasseDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP {
		if (zutrittsmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodellDto == null"));
		}
		if (zutrittsmodellDto.getCNr() == null
				|| zutrittsmodellDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodellDto.getCNr() == null || zutrittsmodellDto.getMandantCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZutrittsmodellfindByCNr");
			query.setParameter(1, zutrittsmodellDto.getCNr());
			Zutrittsmodell doppelt = (Zutrittsmodell) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSMODELL.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSMODELL);
		zutrittsmodellDto.setIId(pk);
		try {
			Zutrittsmodell zutrittsmodell = new Zutrittsmodell(
					zutrittsmodellDto.getIId(), zutrittsmodellDto.getCNr(),
					zutrittsmodellDto.getMandantCNr());
			em.persist(zutrittsmodell);
			em.flush();
			setZutrittsmodellFromZutrittsmodellDto(zutrittsmodell,
					zutrittsmodellDto);
			return zutrittsmodellDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodellDto == null"));
		}
		if (zutrittsmodellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsmodell toRemove = em.find(Zutrittsmodell.class,
				zutrittsmodellDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP {
		if (zutrittsmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodellDto == null"));
		}
		if (zutrittsmodellDto.getIId() == null
				|| zutrittsmodellDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodellDto.getIId() == null || zutrittsmodellDto.getCNr() == null"));
		}
		if (zutrittsmodellDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("zutrittsmodellDto.getMandantCNr() == null"));
		}

		Integer iId = zutrittsmodellDto.getIId();
		// try {
		Zutrittsmodell zutrittsmodell = em.find(Zutrittsmodell.class, iId);
		if (zutrittsmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittsmodellFromZutrittsmodellDto(zutrittsmodell,
				zutrittsmodellDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsmodellDto zutrittsmodellFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zutrittsmodell zutrittsmodell = em.find(Zutrittsmodell.class, iId);
		if (zutrittsmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsmodellDto(zutrittsmodell);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittsmodellFromZutrittsmodellDto(
			Zutrittsmodell zutrittsmodell, ZutrittsmodellDto zutrittsmodellDto) {
		zutrittsmodell.setCNr(zutrittsmodellDto.getCNr());
		zutrittsmodell.setMandantCNr(zutrittsmodellDto.getMandantCNr());
		zutrittsmodell.setXBem(zutrittsmodellDto.getXBem());
		em.merge(zutrittsmodell);
		em.flush();
	}

	private ZutrittsmodellDto assembleZutrittsmodellDto(
			Zutrittsmodell zutrittsmodell) {
		return ZutrittsmodellDtoAssembler.createDto(zutrittsmodell);
	}

	private ZutrittsmodellDto[] assembleZutrittsmodellDtos(
			Collection<?> zutrittsmodells) {
		List<ZutrittsmodellDto> list = new ArrayList<ZutrittsmodellDto>();
		if (zutrittsmodells != null) {
			Iterator<?> iterator = zutrittsmodells.iterator();
			while (iterator.hasNext()) {
				Zutrittsmodell zutrittsmodell = (Zutrittsmodell) iterator
						.next();
				list.add(assembleZutrittsmodellDto(zutrittsmodell));
			}
		}
		ZutrittsmodellDto[] returnArray = new ZutrittsmodellDto[list.size()];
		return (ZutrittsmodellDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP {
		if (zutrittsobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsobjektDto == null"));
		}
		if (zutrittsobjektDto.getCNr() == null
				|| zutrittsobjektDto.getZutrittscontrollerIId() == null
				|| zutrittsobjektDto.getCRelais() == null
				|| zutrittsobjektDto.getZutrittsleserCNr() == null
				|| zutrittsobjektDto.getFOeffnungszeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsobjektDto.getCNr() == null || zutrittsobjektDto.getZutrittscontrollerIId() == null || zutrittsobjektDto.getCTyp() == null || zutrittsobjektDto.getCRelais() == null || zutrittsobjektDto.getFOeffnungszeit() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZutrittsobjektfindByCNr");
			query.setParameter(1, zutrittsobjektDto.getCNr());
			Zutrittsobjekt doppelt = (Zutrittsobjekt) query.getSingleResult();
			if (doppelt == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSOBJEKT.C_NR"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSOBJEKT);
		zutrittsobjektDto.setIId(pk);

		try {
			Zutrittsobjekt zutrittsobjekt = new Zutrittsobjekt(
					zutrittsobjektDto.getIId(), zutrittsobjektDto.getCNr(),
					zutrittsobjektDto.getCAdresse(), zutrittsobjektDto
							.getZutrittscontrollerIId(), zutrittsobjektDto
							.getCRelais(), zutrittsobjektDto
							.getFOeffnungszeit(), zutrittsobjektDto
							.getZutrittsleserCNr());
			em.persist(zutrittsobjekt);
			em.flush();
			setZutrittsobjektFromZutrittsobjektDto(zutrittsobjekt,
					zutrittsobjektDto);
			return zutrittsobjektDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsobjektDto == null"));
		}
		if (zutrittsobjektDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsobjekt toRemove = em.find(Zutrittsobjekt.class,
				zutrittsobjektDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP {
		if (zutrittsobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsobjektDto == null"));
		}
		if (zutrittsobjektDto.getIId() == null
				|| zutrittsobjektDto.getCNr() == null
				|| zutrittsobjektDto.getZutrittscontrollerIId() == null
				|| zutrittsobjektDto.getCRelais() == null
				|| zutrittsobjektDto.getFOeffnungszeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsobjektDto.getIId() == null || zutrittsobjektDto.getCNr() == null || zutrittsobjektDto.getZutrittscontrollerIId() == null || zutrittsobjektDto.getCAnschluss() == null || zutrittsobjektDto.getFOeffnungszeit() == null"));
		}

		Integer iId = zutrittsobjektDto.getIId();
		// try {
		Zutrittsobjekt zutrittsobjekt = em.find(Zutrittsobjekt.class, iId);
		if (zutrittsobjekt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("ZutrittsobjektfindByCNr");
			query.setParameter(1, zutrittsobjektDto.getCNr());
			Integer iIdVorhanden = ((Zutrittsobjekt) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSOBJEKT.CNR"));
			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setZutrittsobjektFromZutrittsobjektDto(zutrittsobjekt,
				zutrittsobjektDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsobjektDto zutrittsobjektFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsobjekt zutrittsobjekt = em.find(Zutrittsobjekt.class, iId);
		if (zutrittsobjekt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsobjektDto(zutrittsobjekt);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZutrittsobjektDto[] zutrittsobjektFindByZutrittscontrollerIId(
			Integer zutrittscontrollerIId) throws EJBExceptionLP {
		if (zutrittscontrollerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zutrittscontrollerIId == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("ZutrittsobjektfindByZutrittscontrollerIId");
		query.setParameter(1, zutrittscontrollerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleZutrittsobjektDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ZutrittsobjektDto zutrittsobjektFindByCNr(String cNr)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		try {
			Query query = em.createNamedQuery("ZutrittsobjektfindByCNr");
			query.setParameter(1, cNr);
			Zutrittsobjekt zutrittsobjekt = (Zutrittsobjekt) query
					.getSingleResult();
			if (zutrittsobjekt == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleZutrittsobjektDto(zutrittsobjekt);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	private void setZutrittsobjektFromZutrittsobjektDto(
			Zutrittsobjekt zutrittsobjekt, ZutrittsobjektDto zutrittsobjektDto) {
		zutrittsobjekt.setCNr(zutrittsobjektDto.getCNr());
		zutrittsobjekt.setCBez(zutrittsobjektDto.getCBez());
		zutrittsobjekt.setZutrittsleserCNr(zutrittsobjektDto
				.getZutrittsleserCNr());
		zutrittsobjekt.setCAdresse(zutrittsobjektDto.getCAdresse());
		zutrittsobjekt.setZutrittscontrollerIId(zutrittsobjektDto
				.getZutrittscontrollerIId());
		zutrittsobjekt.setCRelais(zutrittsobjektDto.getCRelais());
		zutrittsobjekt.setFOeffnungszeit(zutrittsobjektDto.getFOeffnungszeit());
		zutrittsobjekt.setMandantCNr(zutrittsobjektDto.getMandantCNr());
		em.merge(zutrittsobjekt);
		em.flush();
	}

	private ZutrittsobjektDto assembleZutrittsobjektDto(
			Zutrittsobjekt zutrittsobjekt) {
		return ZutrittsobjektDtoAssembler.createDto(zutrittsobjekt);
	}

	private ZutrittsobjektDto[] assembleZutrittsobjektDtos(
			Collection<?> zutrittsobjekts) {
		List<ZutrittsobjektDto> list = new ArrayList<ZutrittsobjektDto>();
		if (zutrittsobjekts != null) {
			Iterator<?> iterator = zutrittsobjekts.iterator();
			while (iterator.hasNext()) {
				Zutrittsobjekt zutrittsobjekt = (Zutrittsobjekt) iterator
						.next();
				list.add(assembleZutrittsobjektDto(zutrittsobjekt));
			}
		}
		ZutrittsobjektDto[] returnArray = new ZutrittsobjektDto[list.size()];
		return (ZutrittsobjektDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP {
		if (zutrittsmodelltagdetailDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagdetailDto == null"));
		}
		if (zutrittsmodelltagdetailDto.getZutrittsmodelltagIId() == null
				|| zutrittsmodelltagdetailDto.getUOffenvon() == null
				|| zutrittsmodelltagdetailDto.getBRestdestages() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagdetailDto.getZutrittsmodelltagIId() == null || zutrittsmodelltagdetailDto.getUOffenvon() == null || zutrittsmodelltagdetailDto.getBRestdestages() == null"));
		}

		if (zutrittsmodelltagdetailDto.getUOffenbis() == null
				&& Helper.short2Boolean(zutrittsmodelltagdetailDto
						.getBRestdestages()) == false) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagdetailDto.getUOffenbis() == null && Helper.short2Boolean(zutrittsmodelltagdetailDto.getBRestdestages())==false"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_ZUTRITTSMODELLTAGDETAIL);
		zutrittsmodelltagdetailDto.setIId(pk);

		try {
			Zutrittsmodelltagdetail zutrittsmodelltagdetail = new Zutrittsmodelltagdetail(
					zutrittsmodelltagdetailDto.getIId(),
					zutrittsmodelltagdetailDto.getZutrittsmodelltagIId(),
					zutrittsmodelltagdetailDto.getZutrittsoeffnungsartCNr(),
					zutrittsmodelltagdetailDto.getBRestdestages(),
					zutrittsmodelltagdetailDto.getUOffenvon());
			em.persist(zutrittsmodelltagdetail);
			em.flush();
			setZutrittsmodelltagdetailFromZutrittsmodelltagdetailDto(
					zutrittsmodelltagdetail, zutrittsmodelltagdetailDto);
			return zutrittsmodelltagdetailDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Map getAllZutrittsoeffnungsarten() throws EJBExceptionLP {

		myLogger.entry();
		TreeMap<Object, String> tmArten = new TreeMap<Object, String>();
		// try {
		Query query = em.createNamedQuery("ZutrittsoeffnungsartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Zutrittsoeffnungsart artikelartTemp = (Zutrittsoeffnungsart) itArten
					.next();
			Object key = artikelartTemp.getCNr();
			String value = artikelartTemp.getCNr().trim();
			if (artikelartTemp.getCBez() != null) {
				value += " " + artikelartTemp.getCBez();
			}
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public Map getAllZutrittsleser() throws EJBExceptionLP {

		myLogger.entry();
		TreeMap<Object, String> tmArten = new TreeMap<Object, String>();
		// try {
		Query query = em.createNamedQuery("ZutrittsleserfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Zutrittsleser artikelartTemp = (Zutrittsleser) itArten.next();
			Object key = artikelartTemp.getCNr();
			String value = artikelartTemp.getCNr().trim();
			if (artikelartTemp.getCBez() != null) {
				value += " " + artikelartTemp.getCBez();
			}
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public void removeZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsmodelltagdetailDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagdetailDto == null"));
		}
		if (zutrittsmodelltagdetailDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsmodelltagdetail toRemove = em.find(
				Zutrittsmodelltagdetail.class, zutrittsmodelltagdetailDto
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP {
		if (zutrittsmodelltagdetailDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagdetailDto == null"));
		}
		if (zutrittsmodelltagdetailDto.getIId() == null
				|| zutrittsmodelltagdetailDto.getZutrittsmodelltagIId() == null
				|| zutrittsmodelltagdetailDto.getUOffenvon() == null
				|| zutrittsmodelltagdetailDto.getBRestdestages() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagdetailDto.getIId() == null || zutrittsmodelltagdetailDto.getZutrittsmodelltagIId() == null || zutrittsmodelltagdetailDto.getUOffenvon() == null || zutrittsmodelltagdetailDto.getBRestdestages() == null"));
		}
		if (zutrittsmodelltagdetailDto.getUOffenbis() == null
				&& Helper.short2Boolean(zutrittsmodelltagdetailDto
						.getBRestdestages()) == false) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagdetailDto.getUOffenbis() == null && Helper.short2Boolean(zutrittsmodelltagdetailDto.getBRestdestages())==false"));
		}

		Integer iId = zutrittsmodelltagdetailDto.getIId();
		// try {
		Zutrittsmodelltagdetail zutrittsmodelltagdetail = em.find(
				Zutrittsmodelltagdetail.class, iId);
		if (zutrittsmodelltagdetail == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittsmodelltagdetailFromZutrittsmodelltagdetailDto(
				zutrittsmodelltagdetail, zutrittsmodelltagdetailDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsmodelltagdetailDto zutrittsmodelltagdetailFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		// try {
		Zutrittsmodelltagdetail zutrittsmodelltagdetail = em.find(
				Zutrittsmodelltagdetail.class, iId);
		if (zutrittsmodelltagdetail == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsmodelltagdetailDto(zutrittsmodelltagdetail);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittsmodelltagdetailFromZutrittsmodelltagdetailDto(
			Zutrittsmodelltagdetail zutrittsmodelltagdetail,
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto) {
		zutrittsmodelltagdetail
				.setZutrittsmodelltagIId(zutrittsmodelltagdetailDto
						.getZutrittsmodelltagIId());
		zutrittsmodelltagdetail.setUOffenvon(zutrittsmodelltagdetailDto
				.getUOffenvon());
		zutrittsmodelltagdetail.setUOffenbis(zutrittsmodelltagdetailDto
				.getUOffenbis());
		zutrittsmodelltagdetail.setBRestdestages(zutrittsmodelltagdetailDto
				.getBRestdestages());
		zutrittsmodelltagdetail
				.setZutrittsoeffnungsartCNr(zutrittsmodelltagdetailDto
						.getZutrittsoeffnungsartCNr());
		em.merge(zutrittsmodelltagdetail);
		em.flush();
	}

	private ZutrittsmodelltagdetailDto assembleZutrittsmodelltagdetailDto(
			Zutrittsmodelltagdetail zutrittsmodelltagdetail) {
		return ZutrittsmodelltagdetailDtoAssembler
				.createDto(zutrittsmodelltagdetail);
	}

	private ZutrittsmodelltagdetailDto[] assembleZutrittsmodelltagdetailDtos(
			Collection<?> zutrittsmodelltagdetails) {
		List<ZutrittsmodelltagdetailDto> list = new ArrayList<ZutrittsmodelltagdetailDto>();
		if (zutrittsmodelltagdetails != null) {
			Iterator<?> iterator = zutrittsmodelltagdetails.iterator();
			while (iterator.hasNext()) {
				Zutrittsmodelltagdetail zutrittsmodelltagdetail = (Zutrittsmodelltagdetail) iterator
						.next();
				list
						.add(assembleZutrittsmodelltagdetailDto(zutrittsmodelltagdetail));
			}
		}
		ZutrittsmodelltagdetailDto[] returnArray = new ZutrittsmodelltagdetailDto[list
				.size()];
		return (ZutrittsmodelltagdetailDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zutrittsklasseobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseobjektDto == null"));
		}
		if (zutrittsklasseobjektDto.getZutrittsklasseIId() == null
				|| zutrittsklasseobjektDto.getZutrittsmodellIId() == null
				|| zutrittsklasseobjektDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsklasseobjektDto.getTGueltigab() == null || zutrittsklasseobjektDto.getZutrittsklasseIId() == null || zutrittsklasseobjektDto.getZutrittsmodellIId() == null || zutrittsklasseobjektDto.getZutrittsobjektIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ZutrittsklasseobjektfindByZutrittsobjektIIdZutrittsklasseIId");
			query.setParameter(1, zutrittsklasseobjektDto
					.getZutrittsobjektIId());
			query.setParameter(2, zutrittsklasseobjektDto
					.getZutrittsklasseIId());
			Zutrittsklasseobjekt doppelt = (Zutrittsklasseobjekt) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSKLASSEOBJEKT.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSKLASSEOBJEKT);
		zutrittsklasseobjektDto.setIId(pk);

		try {
			Zutrittsklasseobjekt zutrittsklasseobjekt = new Zutrittsklasseobjekt(
					zutrittsklasseobjektDto.getIId(), zutrittsklasseobjektDto
							.getZutrittsmodellIId(), zutrittsklasseobjektDto
							.getZutrittsklasseIId(), zutrittsklasseobjektDto
							.getZutrittsobjektIId());
			em.persist(zutrittsklasseobjekt);
			em.flush();
			setZutrittsklasseobjektFromZutrittsklasseobjektDto(
					zutrittsklasseobjekt, zutrittsklasseobjektDto);
			return zutrittsklasseobjektDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsklasseobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseobjektDto == null"));
		}
		if (zutrittsklasseobjektDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsklasseobjekt toRemove = em.find(Zutrittsklasseobjekt.class,
				zutrittsklasseobjektDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zutrittsklasseobjektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsklasseobjektDto == null"));
		}
		if (zutrittsklasseobjektDto.getIId() == null
				|| zutrittsklasseobjektDto.getZutrittsklasseIId() == null
				|| zutrittsklasseobjektDto.getZutrittsmodellIId() == null
				|| zutrittsklasseobjektDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsklasseobjektDto.getIId() == null || zutrittsklasseobjektDto.getZutrittsklasseIId() == null || zutrittsklasseobjektDto.getZutrittsmodellIId() == null || zutrittsklasseobjektDto.getZutrittsobjektIId() == null"));
		}

		Integer iId = zutrittsklasseobjektDto.getIId();

		// try {
		Query query = em
				.createNamedQuery("ZutrittsklasseobjektfindByZutrittsobjektIIdZutrittsklasseIId");
		query.setParameter(1, zutrittsklasseobjektDto.getZutrittsobjektIId());
		query.setParameter(2, zutrittsklasseobjektDto.getZutrittsklasseIId());
		Integer iIdVorhanden = ((Zutrittsklasseobjekt) query.getSingleResult())
				.getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZUTRITTSKLASSEOBJEKT.UK"));
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }

		// try {
		Zutrittsklasseobjekt zutrittsklasseobjekt = em.find(
				Zutrittsklasseobjekt.class, iId);
		if (zutrittsklasseobjekt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittsklasseobjektFromZutrittsklasseobjektDto(
				zutrittsklasseobjekt, zutrittsklasseobjektDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	@SuppressWarnings("static-access")
	public String getZutrittsdatenFuerEinObjektFuerMecs(
			Integer zutrittsobjektIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Timestamp d_datum = new Timestamp(System.currentTimeMillis());

		d_datum = Helper.cutTimestamp(d_datum);
		Integer tagesartIId = null;

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		ArrayList<String> daten = new ArrayList<String>();
		int iTageVorraus = 1;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_ZUTRITT_DATEN_VORLADEN);
			iTageVorraus = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		for (int i = 0; i < iTageVorraus; i++) {

			String sDatum = c.get(c.YEAR) + "";
			int iMonat = c.get(c.MONTH) + 1;
			int iTag = c.get(c.DATE);
			sDatum = sDatum
					+ Helper.fitString2LengthAlignRight(iMonat + "", 2, '0')
					+ Helper.fitString2LengthAlignRight(iTag + "", 2, '0');

			// Besucherausweise- Onlinecheck
			StringBuffer onlinecheck = new StringBuffer();
			onlinecheck.append("10"); // readerid
			onlinecheck.append(Helper.fitString2Length(
					ZutrittscontrollerFac.ZUTRITTSKLASSE_ONLINECHECK, 3, ' ')); // readerid
			onlinecheck.append(sDatum); // datum
			onlinecheck.append("0000"); // vonzeit
			onlinecheck.append("O"); // status
			onlinecheck.append("\r\n");
			daten.add(new String(onlinecheck));

			try {
				Integer tagesartIId_Feiertag = getZeiterfassungFac()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG,
								theClientDto).getIId();
				Integer tagesartIId_Halbtag = getZeiterfassungFac()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG,
								theClientDto).getIId();

				tagesartIId = getZeiterfassungFac().tagesartFindByCNr(
						Helper.holeTagbezeichnungLang(c.get(c.DAY_OF_WEEK)),
						theClientDto).getIId();

				ZutrittsobjektDto zutrittsobjektDto = zutrittsobjektFindByPrimaryKey(zutrittsobjektIId);
				String mandantderTuere = zutrittsobjektDto.getMandantCNr();
				if (mandantderTuere == null) {
					mandantderTuere = getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto)
							.getAnwenderDto().getMandantCNrHauptmandant();
				}

				BetriebskalenderDto dto = getPersonalFac()
						.betriebskalenderFindByMandantCNrDDatum(d_datum,
								mandantderTuere, theClientDto);
				if (dto != null) {
					if (dto.getReligionIId() == null) {
						if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
								|| dto.getTagesartIId().equals(
										tagesartIId_Halbtag)) {
							tagesartIId = dto.getTagesartIId();
						} else {
							tagesartIId = dto.getTagesartIId();
						}
					}
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Criteria crit = session
					.createCriteria(FLRZutrittsklasseobjekt.class)
					.add(
							Restrictions
									.eq(
											ZutrittscontrollerFac.FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSOBJEKT_I_ID,
											zutrittsobjektIId));

			crit
					.addOrder(Order
							.asc(ZutrittscontrollerFac.FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSKLASSE_I_ID));

			List<?> resultList = crit.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRZutrittsklasseobjekt flrzutrittsklasseobjekt = (FLRZutrittsklasseobjekt) resultListIterator
						.next();

				try {
					Query query = em
							.createNamedQuery("ZutrittsmodelltagfindByZutrittsmodellIIdTagesartIId");
					query.setParameter(1, flrzutrittsklasseobjekt
							.getZutrittsmodell_i_id());
					query.setParameter(2, tagesartIId);
					Zutrittsmodelltag zutrittsmodelltag = (Zutrittsmodelltag) query
							.getSingleResult();

					Integer zutrittsmodeltagIId = assembleZutrittsmodelltagDto(
							zutrittsmodelltag).getIId();

					query = em
							.createNamedQuery("ZutrittsmodelltagdetailfindByZutrittsmodelltagIId");
					query.setParameter(1, zutrittsmodeltagIId);
					ZutrittsmodelltagdetailDto[] tagdetaildtos = assembleZutrittsmodelltagdetailDtos(query
							.getResultList());

					for (int detail = 0; detail < tagdetaildtos.length; detail++) {

						ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto = tagdetaildtos[detail];

						String von = zutrittsmodelltagdetailDto.getUOffenvon()
								.toString();
						von = von.substring(0, 2) + von.substring(3, 5);

						StringBuffer string = new StringBuffer();

						string.append("10"); // readerid
						string.append(Helper.fitString2Length(
								flrzutrittsklasseobjekt.getFlrzutrittsklasse()
										.getC_nr(), 3, ' ')); // readerid
						string.append(sDatum); // datum
						string.append(von); // vonzeit
						string.append(zutrittsmodelltagdetailDto
								.getZutrittsoeffnungsartCNr().trim()); // status
						string.append("\r\n");
						daten.add(new String(string));

						if (Helper.short2Boolean(zutrittsmodelltagdetailDto
								.getBRestdestages()) == false) {
							string = new StringBuffer();
							String bis = zutrittsmodelltagdetailDto
									.getUOffenbis().toString();
							bis = bis.substring(0, 2) + bis.substring(3, 5);
							string.append("10"); // readerid
							string.append(Helper.fitString2Length(
									flrzutrittsklasseobjekt
											.getFlrzutrittsklasse().getC_nr(),
									3, ' ')); // readerid
							string.append(sDatum); // datum
							string.append(bis); // vonzeit
							string.append("Z"); // status
							string.append("\r\n");
							daten.add(new String(string));

						}
					}

				} catch (NoResultException ex) {
					// nothing here
				}

			}
			session.close();

			c.set(c.DATE, c.get(c.DATE) + 1);
			d_datum = new java.sql.Timestamp(c.getTimeInMillis());

		}

		String datenGesamt = "";

		// sortieren

		for (int i = daten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (((String) daten.get(j))
						.compareTo((String) daten.get(j + 1)) > 0) {
					String lagerbewegungDtoTemp = (String) daten.get(j);
					daten.set(j, daten.get(j + 1));
					daten.set(j + 1, lagerbewegungDtoTemp);
				}
			}
		}

		for (int i = 0; i < daten.size(); i++) {
			datenGesamt += daten.get(i);
		}

		return new String(datenGesamt);
	}

	@SuppressWarnings("static-access")
	public String getZutrittsEventsFuerMecs(Integer zutrittsobjektIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		String relais = "";
		ZutrittsobjektDto zutrittsobjektDto = zutrittsobjektFindByPrimaryKey(zutrittsobjektIId);
		if (zutrittsobjektDto.getCRelais() != null) {
			relais = zutrittsobjektDto.getCRelais();
		}

		Timestamp d_datum = new Timestamp(System.currentTimeMillis());

		d_datum = Helper.cutTimestamp(d_datum);
		Integer tagesartIId = null;

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		StringBuffer string = new StringBuffer();

		// Damit bei Pin-Eingabe immer ein Online-Check durchgefuehrt wird:
		string.append("        ");
		string.append(Helper.fitString2Length("read", 20, ' '));
		string.append(Helper.fitString2Length("<pin>*</pin>", 200, ' '));
		string.append(Helper.fitString2Length("<door relais=\"0" + relais
				+ "\" open=\"online\"/>", 100, ' '));
		string.append(Helper.fitString2Length("PINONLINE", 10, ' '));
		string.append("\r\n");

		int iTageVorraus = 1;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_ZUTRITT_DATEN_VORLADEN);
			iTageVorraus = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		for (int i = 0; i < iTageVorraus; i++) {

			String sDatum = c.get(c.YEAR) + "";
			int iMonat = c.get(c.MONTH) + 1;
			int iTag = c.get(c.DATE);
			sDatum = sDatum
					+ Helper.fitString2LengthAlignRight(iMonat + "", 2, '0')
					+ Helper.fitString2LengthAlignRight(iTag + "", 2, '0');

			try {
				Integer tagesartIId_Feiertag = getZeiterfassungFac()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG,
								theClientDto).getIId();
				Integer tagesartIId_Halbtag = getZeiterfassungFac()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG,
								theClientDto).getIId();

				tagesartIId = getZeiterfassungFac().tagesartFindByCNr(
						Helper.holeTagbezeichnungLang(c.get(c.DAY_OF_WEEK)),
						theClientDto).getIId();

				String mandantderTuere = zutrittsobjektDto.getMandantCNr();
				if (mandantderTuere == null) {
					mandantderTuere = getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto)
							.getAnwenderDto().getMandantCNrHauptmandant();
				}
				BetriebskalenderDto dto = getPersonalFac()
						.betriebskalenderFindByMandantCNrDDatum(d_datum,
								mandantderTuere, theClientDto);
				if (dto != null) {

					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
						tagesartIId = dto.getTagesartIId();
					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			// try {
			Query query = em
					.createNamedQuery("ZutrittdaueroffenfindByZutrittsobjektIIdTagesartIId");
			query.setParameter(1, zutrittsobjektIId);
			query.setParameter(2, tagesartIId);
			ZutrittdaueroffenDto[] zutrittdaueroffenDtos = assembleZutrittdaueroffenDtos(query
					.getResultList());
			for (int detail = 0; detail < zutrittdaueroffenDtos.length; detail++) {
				ZutrittdaueroffenDto zutrittsmodelltagdetailDto = zutrittdaueroffenDtos[detail];

				long time = System.currentTimeMillis() % (3600000 * 24);
				// Wenn HEUTE und BIS schon vorbei:
				if (i == 0
						&& time > zutrittsmodelltagdetailDto.getUOffenbis()
								.getTime()) {
					// dann auslassen
				} else {
					String von = zutrittsmodelltagdetailDto.getUOffenvon()
							.toString();
					von = von.substring(0, 2) + von.substring(3, 5);
					string.append(sDatum);
					string.append(Helper.fitString2Length("moment", 20, ' '));
					string.append(Helper.fitString2Length("<time event=\""
							+ von + "\" makeup=\"true\"/>", 200, ' '));
					string.append(Helper.fitString2Length("<door relais=\"0"
							+ relais + "\" open=\"always\"/>", 100, ' '));
					string
							.append(Helper.fitString2Length("DAUEROPEN", 10,
									' '));
					string.append("\r\n");

					String bis = zutrittsmodelltagdetailDto.getUOffenbis()
							.toString();
					bis = bis.substring(0, 2) + bis.substring(3, 5);

					string.append(sDatum);
					string.append(Helper.fitString2Length("moment", 20, ' '));
					string.append(Helper.fitString2Length("<time event=\""
							+ bis + "\" makeup=\"true\"/>", 200, ' '));
					string.append(Helper.fitString2Length("<door relais=\"0"
							+ relais + "\" open=\"closestd\"/>", 100, ' '));
					string.append(Helper
							.fitString2Length("DAUERCLOSE", 10, ' '));
					string.append("\r\n");

				}
			}
			// }
			// catch (FinderException ex) {
			//
			// }

			c.set(c.DATE, c.get(c.DATE) + 1);
			d_datum = new java.sql.Timestamp(c.getTimeInMillis());

		}
		return new String(string);

	}

	public ZutrittsklasseobjektDto zutrittsklasseobjektFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zutrittsklasseobjekt zutrittsklasseobjekt = em.find(
				Zutrittsklasseobjekt.class, iId);
		if (zutrittsklasseobjekt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsklasseobjektDto(zutrittsklasseobjekt);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZutrittsklasseobjektDto[] zutrittsklasseobjektFindByZutrittsklasseIId(
			Integer zutrittsklasseIId) throws EJBExceptionLP {
		if (zutrittsklasseIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zutrittsklasseIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ZutrittsklasseobjektfindByZutrittsklasseIId");
		query.setParameter(1, zutrittsklasseIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleZutrittsklasseobjektDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	private void setZutrittsklasseobjektFromZutrittsklasseobjektDto(
			Zutrittsklasseobjekt zutrittsklasseobjekt,
			ZutrittsklasseobjektDto zutrittsklasseobjektDto) {
		zutrittsklasseobjekt.setZutrittsmodellIId(zutrittsklasseobjektDto
				.getZutrittsmodellIId());
		zutrittsklasseobjekt.setZutrittsklasseIId(zutrittsklasseobjektDto
				.getZutrittsklasseIId());
		zutrittsklasseobjekt.setZutrittsobjektIId(zutrittsklasseobjektDto
				.getZutrittsobjektIId());
		em.merge(zutrittsklasseobjekt);
		em.flush();
	}

	private ZutrittsklasseobjektDto assembleZutrittsklasseobjektDto(
			Zutrittsklasseobjekt zutrittsklasseobjekt) {
		return ZutrittsklasseobjektDtoAssembler.createDto(zutrittsklasseobjekt);
	}

	private ZutrittsklasseobjektDto[] assembleZutrittsklasseobjektDtos(
			Collection<?> zutrittsklasseobjekts) {
		List<ZutrittsklasseobjektDto> list = new ArrayList<ZutrittsklasseobjektDto>();
		if (zutrittsklasseobjekts != null) {
			Iterator<?> iterator = zutrittsklasseobjekts.iterator();
			while (iterator.hasNext()) {
				Zutrittsklasseobjekt zutrittsklasseobjekt = (Zutrittsklasseobjekt) iterator
						.next();
				list.add(assembleZutrittsklasseobjektDto(zutrittsklasseobjekt));
			}
		}
		ZutrittsklasseobjektDto[] returnArray = new ZutrittsklasseobjektDto[list
				.size()];
		return (ZutrittsklasseobjektDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP {
		if (zutrittsmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagDto == null"));
		}
		if (zutrittsmodelltagDto.getTagesartIId() == null
				|| zutrittsmodelltagDto.getZutrittsmodellIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagDto.getTagesartIId() == null || zutrittsmodelltagDto.getZutrittsmodellIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ZutrittsmodelltagfindByZutrittsmodellIIdTagesartIId");
			query.setParameter(1, zutrittsmodelltagDto.getZutrittsmodellIId());
			query.setParameter(2, zutrittsmodelltagDto.getTagesartIId());
			Zutrittsmodelltag doppelt = (Zutrittsmodelltag) query
					.getSingleResult();
			if (zutrittsmodelltag != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSMODELLTAG.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSMODELLTAG);
		zutrittsmodelltagDto.setIId(pk);

		try {
			Zutrittsmodelltag zutrittsmodelltag = new Zutrittsmodelltag(
					zutrittsmodelltagDto.getIId(), zutrittsmodelltagDto
							.getZutrittsmodellIId(), zutrittsmodelltagDto
							.getTagesartIId());
			em.persist(zutrittsmodelltag);
			em.flush();
			setZutrittsmodelltagFromZutrittsmodelltagDto(zutrittsmodelltag,
					zutrittsmodelltagDto);
			return zutrittsmodelltagDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagDto == null"));
		}
		if (zutrittsmodelltagDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsmodelltag toRemove = em.find(Zutrittsmodelltag.class,
				zutrittsmodelltagDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP {
		if (zutrittsmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsmodelltagDto == null"));
		}
		if (zutrittsmodelltagDto.getIId() == null
				|| zutrittsmodelltagDto.getTagesartIId() == null
				|| zutrittsmodelltagDto.getZutrittsmodellIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsmodelltagDto.getIId() == null || zutrittsmodelltagDto.getTagesartIId() == null || zutrittsmodelltagDto.getZutrittsmodellIId() == null"));
		}

		Integer iId = zutrittsmodelltagDto.getIId();
		// try {
		Zutrittsmodelltag zutrittsmodelltag = em.find(Zutrittsmodelltag.class,
				iId);
		if (zutrittsmodelltag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittsmodelltagFromZutrittsmodelltagDto(zutrittsmodelltag,
				zutrittsmodelltagDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsmodelltagDto zutrittsmodelltagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsmodelltag zutrittsmodelltag = em.find(Zutrittsmodelltag.class,
				iId);
		if (zutrittsmodelltag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsmodelltagDto(zutrittsmodelltag);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittsmodelltagFromZutrittsmodelltagDto(
			Zutrittsmodelltag zutrittsmodelltag,
			ZutrittsmodelltagDto zutrittsmodelltagDto) {
		zutrittsmodelltag.setZutrittsmodellIId(zutrittsmodelltagDto
				.getZutrittsmodellIId());
		zutrittsmodelltag.setTagesartIId(zutrittsmodelltagDto.getTagesartIId());
		zutrittsmodelltag.setXBem(zutrittsmodelltagDto.getXBem());
		em.merge(zutrittsmodelltag);
		em.flush();
	}

	private ZutrittsmodelltagDto assembleZutrittsmodelltagDto(
			Zutrittsmodelltag zutrittsmodelltag) {
		return ZutrittsmodelltagDtoAssembler.createDto(zutrittsmodelltag);
	}

	private ZutrittsmodelltagDto[] assembleZutrittsmodelltagDtos(
			Collection<?> zutrittsmodelltags) {
		List<ZutrittsmodelltagDto> list = new ArrayList<ZutrittsmodelltagDto>();
		if (zutrittsmodelltags != null) {
			Iterator<?> iterator = zutrittsmodelltags.iterator();
			while (iterator.hasNext()) {
				Zutrittsmodelltag zutrittsmodelltag = (Zutrittsmodelltag) iterator
						.next();
				list.add(assembleZutrittsmodelltagDto(zutrittsmodelltag));
			}
		}
		ZutrittsmodelltagDto[] returnArray = new ZutrittsmodelltagDto[list
				.size()];
		return (ZutrittsmodelltagDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalzutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzutrittsklasseDto == null"));
		}
		if (personalzutrittsklasseDto.getPersonalIId() == null
				|| personalzutrittsklasseDto.getTGueltigab() == null
				|| personalzutrittsklasseDto.getZutrittsklasseIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalzutrittsklasseDto.getPersonalIId() == null || personalzutrittsklasseDto.getTGueltigab() == null || personalzutrittsklasseDto.getZutrittsklasseIId() == null"));
		}

		personalzutrittsklasseDto.setTGueltigab(Helper
				.cutTimestamp(personalzutrittsklasseDto.getTGueltigab()));

		try {
			Query query = em
					.createNamedQuery("PersonalzutrittsklassefindByPersonalIIdZutrittklasseIIdTGueltigab");
			query.setParameter(1, personalzutrittsklasseDto.getPersonalIId());
			query.setParameter(2, personalzutrittsklasseDto
					.getZutrittsklasseIId());
			query.setParameter(3, personalzutrittsklasseDto.getTGueltigab());
			Personalzutrittsklasse doppelt = (Personalzutrittsklasse) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALZUTRITTSKLASSE.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		pruefeZutrittsobjektverwendung(personalzutrittsklasseDto
				.getZutrittsklasseIId(), theClientDto);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALZUTRITTSKLASSE);
		personalzutrittsklasseDto.setIId(pk);

		try {
			Personalzutrittsklasse personalzutrittsklasse = new Personalzutrittsklasse(
					personalzutrittsklasseDto.getIId(),
					personalzutrittsklasseDto.getPersonalIId(),
					personalzutrittsklasseDto.getZutrittsklasseIId(),
					personalzutrittsklasseDto.getTGueltigab());
			em.persist(personalzutrittsklasse);
			em.flush();
			setPersonalzutrittsklasseFromPersonalzutrittsklasseDto(
					personalzutrittsklasse, personalzutrittsklasseDto);
			return personalzutrittsklasseDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (personalzutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzutrittsklasseDto == null"));
		}
		if (personalzutrittsklasseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Personalzutrittsklasse toRemove = em.find(Personalzutrittsklasse.class,
				personalzutrittsklasseDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updatePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalzutrittsklasseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzutrittsklasseDto == null"));
		}
		if (personalzutrittsklasseDto.getIId() == null
				|| personalzutrittsklasseDto.getPersonalIId() == null
				|| personalzutrittsklasseDto.getTGueltigab() == null
				|| personalzutrittsklasseDto.getZutrittsklasseIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalzutrittsklasseDto.getIId() == null || personalzutrittsklasseDto.getPersonalIId() == null || personalzutrittsklasseDto.getTGueltigab() == null || personalzutrittsklasseDto.getZutrittsklasseIId() == null"));
		}

		personalzutrittsklasseDto.setTGueltigab(Helper
				.cutTimestamp(personalzutrittsklasseDto.getTGueltigab()));

		Integer iId = personalzutrittsklasseDto.getIId();
		// try {
		Personalzutrittsklasse personalzutrittsklasse = em.find(
				Personalzutrittsklasse.class, iId);
		if (personalzutrittsklasse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setPersonalzutrittsklasseFromPersonalzutrittsklasseDto(
				personalzutrittsklasse, personalzutrittsklasseDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public PersonalzutrittsklasseDto personalzutrittsklasseFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalzutrittsklasse personalzutrittsklasse = em.find(
				Personalzutrittsklasse.class, iId);
		if (personalzutrittsklasse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalzutrittsklasseDto(personalzutrittsklasse);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PersonalzutrittsklasseDto personalzutrittsklasseFindZutrittsklasseZuDatum(
			Integer personalIId, java.sql.Timestamp dDatum,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalIId == null || dDatum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null || dDatum == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("PersonalzutrittsklassefindByPersonalIIdTGueltigab");
		query.setParameter(1, personalIId);
		query.setParameter(2, dDatum);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		PersonalzutrittsklasseDto[] personalzeitmodellDtos = assemblePersonalzutrittsklasseDtos(cl);
		PersonalzutrittsklasseDto personalzutrittsklasseDto = null;
		if (personalzeitmodellDtos.length > 0) {
			personalzutrittsklasseDto = personalzeitmodellDtos[0];
		}
		if (personalzutrittsklasseDto != null) {
			personalzutrittsklasseDto
					.setZutrittsklasseDto(zutrittsklasseFindByPrimaryKey(personalzutrittsklasseDto
							.getZutrittsklasseIId()));

		}
		return personalzutrittsklasseDto;
		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	public PersonalzutrittsklasseDto[] personalzutrittsklassenFindByTGueltigab(
			Timestamp tDatum, TheClientDto theClientDto) throws EJBExceptionLP {
		if (tDatum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tDatum == null"));
		}
		tDatum = Helper.cutTimestamp(tDatum);
		String sQuery = "select distinct personalzutrittsklasse.personal_i_id from FLRPersonalzutrittsklasse personalzutrittsklasse WHERE personalzutrittsklasse.t_gueltigab<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime()))
				+ "' AND personalzutrittsklasse.flrpersonal.c_ausweis is not null";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<PersonalzutrittsklasseDto> a = new ArrayList<PersonalzutrittsklasseDto>();

		int row = 0;
		while (resultListIterator.hasNext()) {

			Integer o = (Integer) resultListIterator.next();
			try {
				if (!getPersonalFac().istPersonalAusgetreten(o, tDatum,
						theClientDto)) {
					PersonalzutrittsklasseDto dto = new PersonalzutrittsklasseDto();

					// try {
					Query query = em
							.createNamedQuery("PersonalzutrittsklassefindByPersonalIIdTGueltigab");
					query.setParameter(1, o);
					query.setParameter(2, tDatum);
					Collection<?> cl = query.getResultList();
					// if (cl.isEmpty()) {
					// throw new
					// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY
					// ,
					// null);
					// }
					PersonalzutrittsklasseDto[] dtoTemp = assemblePersonalzutrittsklasseDtos(cl);
					dto = dtoTemp[0];
					// }
					// catch (FinderException ex1) {
					// throw new
					// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY
					// ,
					// ex1);
					// }
					/*
					 * dto.setPersonalDto(getPersonalFac().
					 * personalFindByPrimaryKey(o, cNrUserI));
					 * dto.setZutrittsklasseDto(
					 * zutrittsklasseFindByPrimaryKey(dto.
					 * getZutrittsklasseIId()));
					 */
					a.add(dto);

				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		PersonalzutrittsklasseDto[] returnArray = new PersonalzutrittsklasseDto[a
				.size()];
		return (PersonalzutrittsklasseDto[]) a.toArray(returnArray);
	}

	private void setPersonalzutrittsklasseFromPersonalzutrittsklasseDto(
			Personalzutrittsklasse personalzutrittsklasse,
			PersonalzutrittsklasseDto personalzutrittsklasseDto) {
		personalzutrittsklasse.setPersonalIId(personalzutrittsklasseDto
				.getPersonalIId());
		personalzutrittsklasse.setZutrittsklasseIId(personalzutrittsklasseDto
				.getZutrittsklasseIId());
		personalzutrittsklasse.setTGueltigab(personalzutrittsklasseDto
				.getTGueltigab());
		em.merge(personalzutrittsklasse);
		em.flush();
	}

	private PersonalzutrittsklasseDto assemblePersonalzutrittsklasseDto(
			Personalzutrittsklasse personalzutrittsklasse) {
		return PersonalzutrittsklasseDtoAssembler
				.createDto(personalzutrittsklasse);
	}

	private PersonalzutrittsklasseDto[] assemblePersonalzutrittsklasseDtos(
			Collection<?> personalzutrittsklasses) {
		List<PersonalzutrittsklasseDto> list = new ArrayList<PersonalzutrittsklasseDto>();
		if (personalzutrittsklasses != null) {
			Iterator<?> iterator = personalzutrittsklasses.iterator();
			while (iterator.hasNext()) {
				Personalzutrittsklasse personalzutrittsklasse = (Personalzutrittsklasse) iterator
						.next();
				list
						.add(assemblePersonalzutrittsklasseDto(personalzutrittsklasse));
			}
		}
		PersonalzutrittsklasseDto[] returnArray = new PersonalzutrittsklasseDto[list
				.size()];
		return (PersonalzutrittsklasseDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zutrittonlinecheckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittonlinecheckDto == null"));
		}
		if (zutrittonlinecheckDto.getTGueltigab() == null
				|| zutrittonlinecheckDto.getTGueltigbis() == null
				|| zutrittonlinecheckDto.getZutrittsklasseIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittonlinecheckDto.getTGueltigab() == null || zutrittonlinecheckDto.getTGueltigbis() == null || zutrittonlinecheckDto.getZutrittsklasseIId() == null"));
		}

		if (zutrittonlinecheckDto.getCAusweis() == null
				&& zutrittonlinecheckDto.getCPincode() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zutrittonlinecheckDto.getCAusweis() == null && zutrittonlinecheckDto.getCPincode() == null"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTONLINECHECK);
		zutrittonlinecheckDto.setIId(pk);

		zutrittonlinecheckDto.setMandantCNr(theClientDto.getMandant());

		try {
			Zutrittonlinecheck zutrittonlinecheck = new Zutrittonlinecheck(
					zutrittonlinecheckDto.getIId(), zutrittonlinecheckDto
							.getZutrittsklasseIId(), zutrittonlinecheckDto
							.getMandantCNr(), zutrittonlinecheckDto
							.getTGueltigab(), zutrittonlinecheckDto
							.getTGueltigbis());
			em.persist(zutrittonlinecheck);
			em.flush();
			setZutrittonlinecheckFromZutrittonlinecheckDto(zutrittonlinecheck,
					zutrittonlinecheckDto);
			return zutrittonlinecheckDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittonlinecheckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittonlinecheckDto == null"));
		}
		if (zutrittonlinecheckDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittonlinecheck toRemove = em.find(Zutrittonlinecheck.class,
				zutrittonlinecheckDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto) throws EJBExceptionLP {
		if (zutrittonlinecheckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittonlinecheckDto == null"));
		}
		if (zutrittonlinecheckDto.getIId() == null
				|| zutrittonlinecheckDto.getTGueltigab() == null
				|| zutrittonlinecheckDto.getTGueltigbis() == null
				|| zutrittonlinecheckDto.getZutrittsklasseIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittonlinecheckDto.getIId() == null || zutrittonlinecheckDto.getTGueltigab() == null || zutrittonlinecheckDto.getTGueltigbis() == null || zutrittonlinecheckDto.getZutrittsklasseIId() == null"));
		}
		if (zutrittonlinecheckDto.getCAusweis() == null
				&& zutrittonlinecheckDto.getCPincode() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zutrittonlinecheckDto.getCAusweis() == null && zutrittonlinecheckDto.getCPincode() == null"));
		}

		Integer iId = zutrittonlinecheckDto.getIId();
		// try {
		Zutrittonlinecheck zutrittonlinecheck = em.find(
				Zutrittonlinecheck.class, iId);
		if (zutrittonlinecheck == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittonlinecheckFromZutrittonlinecheckDto(zutrittonlinecheck,
				zutrittonlinecheckDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void kopiereRestlicheZutrittsmodelltage(Integer zutrittsmodelIId)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("ZutrittsmodelltagfindByZutrittsmodellIId");
		query.setParameter(1, zutrittsmodelIId);
		Collection<?> cl = query.getResultList();
		// if (! cl.isEmpty()) {
		ZutrittsmodelltagDto[] zutrittsmodelltagDtos = assembleZutrittsmodelltagDtos(cl);

		try {
			TagesartDto[] tagesartDtos = getZeiterfassungFac()
					.tagesartFindAll();

			if (tagesartDtos != null && tagesartDtos.length > 0) {
				for (int i = 0; i < tagesartDtos.length; i++) {
					TagesartDto t = tagesartDtos[i];
					boolean bGefunden = false;
					for (int j = 0; j < zutrittsmodelltagDtos.length; j++) {
						if (t.getIId().equals(
								zutrittsmodelltagDtos[j].getTagesartIId())) {
							bGefunden = true;
						}
					}
					if (!bGefunden) {
						ZutrittsmodelltagDto dto = new ZutrittsmodelltagDto();
						dto.setTagesartIId(t.getIId());
						dto.setZutrittsmodellIId(zutrittsmodelIId);
						createZutrittsmodelltag(dto);
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// }
		// catch (FinderException ex) {
		// //keiner da
		// }
	}

	public boolean onlineCheck(String cAusweis, String pinMd5,
			java.sql.Timestamp tZeitpunkt, Integer zutrittsobjektIId)
			throws EJBExceptionLP {
		if (tZeitpunkt == null || zutrittsobjektIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tZeitpunkt == null || zutrittsobjektIId == null"));
		}
		if (cAusweis == null && pinMd5 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cAusweis == null && pinMd5 == null"));
		}

		boolean bZutritt = false;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		org.hibernate.Criteria zutrittonclinecheck = session
				.createCriteria(FLRZutrittonlinecheck.class);

		zutrittonclinecheck.add(Expression.le(
				ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_T_GUELITGAB,
				tZeitpunkt));
		zutrittonclinecheck.add(Expression.ge(
				ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_T_GUELITGBIS,
				tZeitpunkt));
		if (cAusweis != null && cAusweis.length() > 0) {
			zutrittonclinecheck.add(Expression.eq(
					ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_C_AUSWEIS,
					cAusweis));
		}
		if (pinMd5 != null && pinMd5.length() > 0) {
			zutrittonclinecheck
					.add(Expression
							.isNotNull(ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_C_PINCODE));
		}

		List<?> resultListZeitdaten = zutrittonclinecheck.list();

		// Nichts gefunden - nicht reinlassen
		if (resultListZeitdaten.size() == 0) {
			return false;
		}

		Iterator<?> resultListIterator = resultListZeitdaten.iterator();

		while (resultListIterator.hasNext()) {
			FLRZutrittonlinecheck zutrittonlinecheck = (FLRZutrittonlinecheck) resultListIterator
					.next();

			// try {
			Query query = em
					.createNamedQuery("ZutrittsklasseobjektfindByZutrittsobjektIIdZutrittsklasseIId");
			query.setParameter(1, zutrittsobjektIId);
			query.setParameter(2, zutrittonlinecheck.getFlrzutrittsklasse()
					.getI_id());
			Zutrittsklasseobjekt zutrittsklasseobjekt = (Zutrittsklasseobjekt) query
					.getSingleResult();

			if (zutrittsklasseobjekt == null) {
				// Tuer gehoert nicht zur Zutrittsklasse
				return false;
			}

			// Ausweis/Zutrittsobjekt wurde gefunden und pincode war leer: true
			if (pinMd5 == null || pinMd5.length() == 0) {
				return true;
			}

			byte[] md5Hash = org.apache.commons.codec.digest.DigestUtils
					.md5(zutrittonlinecheck.getC_pincode());
			String pinHash = new String(Base64.encodeBase64(md5Hash));
			// workaround, da aus dem +-Zeichen in der URL bei getParameter()
			// ein Leerzeichen wird.
			pinHash = pinHash.replace('+', ' ');
			System.out.println(pinHash);
			// Wenn Pincode stimmt - TRUE
			if (pinMd5.equals(pinHash)) {
				return true;
			}
			// }
			// catch (FinderException ex) {
			// //next
			// }
		}
		return bZutritt;
	}

	public ZutrittonlinecheckDto zutrittonlinecheckFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittonlinecheck zutrittonlinecheck = em.find(
				Zutrittonlinecheck.class, iId);
		if (zutrittonlinecheck == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittonlinecheckDto(zutrittonlinecheck);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittonlinecheckFromZutrittonlinecheckDto(
			Zutrittonlinecheck zutrittonlinecheck,
			ZutrittonlinecheckDto zutrittonlinecheckDto) {
		zutrittonlinecheck.setZutrittsklasseIId(zutrittonlinecheckDto
				.getZutrittsklasseIId());
		zutrittonlinecheck.setCPincode(zutrittonlinecheckDto.getCPincode());
		zutrittonlinecheck.setCAusweis(zutrittonlinecheckDto.getCAusweis());
		zutrittonlinecheck.setTGueltigab(zutrittonlinecheckDto.getTGueltigab());
		zutrittonlinecheck.setTGueltigbis(zutrittonlinecheckDto
				.getTGueltigbis());
		em.merge(zutrittonlinecheck);
		em.flush();
	}

	private ZutrittonlinecheckDto assembleZutrittonlinecheckDto(
			Zutrittonlinecheck zutrittonlinecheck) {
		return ZutrittonlinecheckDtoAssembler.createDto(zutrittonlinecheck);
	}

	private ZutrittonlinecheckDto[] assembleZutrittonlinecheckDtos(
			Collection<?> zutrittonlinechecks) {
		List<ZutrittonlinecheckDto> list = new ArrayList<ZutrittonlinecheckDto>();
		if (zutrittonlinechecks != null) {
			Iterator<?> iterator = zutrittonlinechecks.iterator();
			while (iterator.hasNext()) {
				Zutrittonlinecheck zutrittonlinecheck = (Zutrittonlinecheck) iterator
						.next();
				list.add(assembleZutrittonlinecheckDto(zutrittonlinecheck));
			}
		}
		ZutrittonlinecheckDto[] returnArray = new ZutrittonlinecheckDto[list
				.size()];
		return (ZutrittonlinecheckDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws EJBExceptionLP {
		if (zutrittsobjektverwendungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsobjektverwendungDto == null"));
		}
		if (zutrittsobjektverwendungDto.getMandantCNr() == null
				|| zutrittsobjektverwendungDto.getIAnzahlverwendung() == null
				|| zutrittsobjektverwendungDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsobjektverwendungDto.getMandantCNr() == null || zutrittsobjektverwendungDto.getIAnzahlverwendung() == null || zutrittsobjektverwendungDto.getZutrittsobjektIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ZutrittsobjektverwendungfindByMandantCNrZutrittsobjektIId");
			query.setParameter(1, zutrittsobjektverwendungDto.getMandantCNr());
			query.setParameter(2, zutrittsobjektverwendungDto
					.getZutrittsobjektIId());
			Zutrittsobjektverwendung doppelt = (Zutrittsobjektverwendung) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZUTRITTSOBJEKTVERWENDUNG.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_ZUTRITTSOBJEKTVERWENDUNG);
		zutrittsobjektverwendungDto.setIId(pk);

		try {
			Zutrittsobjektverwendung zutrittsobjektverwendung = new Zutrittsobjektverwendung(
					zutrittsobjektverwendungDto.getIId(),
					zutrittsobjektverwendungDto.getMandantCNr(),
					zutrittsobjektverwendungDto.getIAnzahlverwendung(),
					zutrittsobjektverwendungDto.getZutrittsobjektIId());
			em.persist(zutrittsobjektverwendung);
			em.flush();
			setZutrittsobjektverwendungFromZutrittsobjektverwendungDto(
					zutrittsobjektverwendung, zutrittsobjektverwendungDto);
			return zutrittsobjektverwendungDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void pruefeZutrittsobjektverwendung(Integer zutrittsklasseIId,
			TheClientDto theClientDto) {

		if (zutrittsklasseIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zutrittsobjektIId == null"));
		}

		java.sql.Timestamp tHeute = new Timestamp(System.currentTimeMillis());
		tHeute = Helper.cutTimestamp(tHeute);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String sQuery = "select count(distinct personalzutrittsklasse.personal_i_id) from FLRPersonalzutrittsklasse personalzutrittsklasse WHERE personalzutrittsklasse.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND personalzutrittsklasse.t_gueltigab <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tHeute
						.getTime())) + "'";

		org.hibernate.Query hqlquery = session.createQuery(sQuery);

		List<?> resultList = hqlquery.list();

		long iAnzahlKlasseVerwendet = (Long) resultList.iterator().next();

		// try {
		Query query = em
				.createNamedQuery("ZutrittsobjektverwendungfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		// if (! cl.isEmpty()) {
		ZutrittsobjektverwendungDto[] zutrittsobjektverwendungDtos = assembleZutrittsobjektverwendungDtos(query
				.getResultList());
		if (zutrittsobjektverwendungDtos != null) {
			for (int i = 0; i < zutrittsobjektverwendungDtos.length; i++) {
				ZutrittsobjektverwendungDto zutrittsobjektverwendungDto = zutrittsobjektverwendungDtos[i];
				int iAnzahlDarfVerwendetwerden = zutrittsobjektverwendungDto
						.getIAnzahlverwendung();
				try {
					query = em
							.createNamedQuery("ZutrittsklasseobjektfindByZutrittsobjektIIdZutrittsklasseIId");
					query.setParameter(1, zutrittsobjektverwendungDto
							.getZutrittsobjektIId());
					query.setParameter(2, zutrittsklasseIId);
					Zutrittsklasseobjekt zutrittsklasseobjekt = (Zutrittsklasseobjekt) query
							.getSingleResult();
					if (zutrittsklasseobjekt != null) {
						ZutrittsklasseobjektDto zutrittsklasseobjektDto = assembleZutrittsklasseobjektDto(zutrittsklasseobjekt);
						if (zutrittsklasseobjektDto != null) {
							if (iAnzahlKlasseVerwendet >= iAnzahlDarfVerwendetwerden) {
								Zutrittsobjekt zutrittsobjekt = em.find(
										Zutrittsobjekt.class,
										zutrittsklasseobjektDto
												.getZutrittsobjektIId());
								if (zutrittsobjekt != null) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_ZUTRITTSOBJEKT_VERWENDUNGSUEBERSCHREITUNG,
											new Exception(zutrittsobjekt
													.getCBez()));
								}

							}
						}

					}
				} catch (javax.persistence.NoResultException ex1) {
					// nix
				}
			}
		}

		// }
		// catch (FinderException ex) {
		// //nix da
		// }
	}

	public void removeZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittsobjektverwendungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittsobjektverwendung toRemove = em.find(
				Zutrittsobjektverwendung.class, zutrittsobjektverwendungDto
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws EJBExceptionLP {
		if (zutrittsobjektverwendungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittsobjektverwendungDto == null"));
		}
		if (zutrittsobjektverwendungDto.getIId() == null
				|| zutrittsobjektverwendungDto.getMandantCNr() == null
				|| zutrittsobjektverwendungDto.getIAnzahlverwendung() == null
				|| zutrittsobjektverwendungDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittsobjektverwendungDto.getIId() == null || zutrittsobjektverwendungDto.getMandantCNr() == null || zutrittsobjektverwendungDto.getIAnzahlverwendung() == null || zutrittsobjektverwendungDto.getZutrittsobjektIId() == null"));
		}

		Integer iId = zutrittsobjektverwendungDto.getIId();
		// try {
		Zutrittsobjektverwendung zutrittsobjektverwendung = em.find(
				Zutrittsobjektverwendung.class, iId);
		if (zutrittsobjektverwendung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittsobjektverwendungFromZutrittsobjektverwendungDto(
				zutrittsobjektverwendung, zutrittsobjektverwendungDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZutrittsobjektverwendungDto zutrittsobjektverwendungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zutrittsobjektverwendung zutrittsobjektverwendung = em.find(
				Zutrittsobjektverwendung.class, iId);
		if (zutrittsobjektverwendung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittsobjektverwendungDto(zutrittsobjektverwendung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZutrittsobjektverwendungDto[] zutrittsobjektverwendungFindByZutrittsobjektIId(
			Integer zutrittsobjektIId) throws EJBExceptionLP {
		if (zutrittsobjektIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zutrittsobjektIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ZutrittsobjektverwendungfindByZutrittsobjektIId");
		query.setParameter(1, zutrittsobjektIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZutrittsobjektverwendungDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittsobjektverwendungFromZutrittsobjektverwendungDto(
			Zutrittsobjektverwendung zutrittsobjektverwendung,
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto) {
		zutrittsobjektverwendung.setMandantCNr(zutrittsobjektverwendungDto
				.getMandantCNr());
		zutrittsobjektverwendung
				.setIAnzahlverwendung(zutrittsobjektverwendungDto
						.getIAnzahlverwendung());
		zutrittsobjektverwendung
				.setZutrittsobjektIId(zutrittsobjektverwendungDto
						.getZutrittsobjektIId());
		em.merge(zutrittsobjektverwendung);
		em.flush();
	}

	private ZutrittsobjektverwendungDto assembleZutrittsobjektverwendungDto(
			Zutrittsobjektverwendung zutrittsobjektverwendung) {
		return ZutrittsobjektverwendungDtoAssembler
				.createDto(zutrittsobjektverwendung);
	}

	private ZutrittsobjektverwendungDto[] assembleZutrittsobjektverwendungDtos(
			Collection<?> zutrittsobjektverwendungs) {
		List<ZutrittsobjektverwendungDto> list = new ArrayList<ZutrittsobjektverwendungDto>();
		if (zutrittsobjektverwendungs != null) {
			Iterator<?> iterator = zutrittsobjektverwendungs.iterator();
			while (iterator.hasNext()) {
				Zutrittsobjektverwendung zutrittsobjektverwendung = (Zutrittsobjektverwendung) iterator
						.next();
				list
						.add(assembleZutrittsobjektverwendungDto(zutrittsobjektverwendung));
			}
		}
		ZutrittsobjektverwendungDto[] returnArray = new ZutrittsobjektverwendungDto[list
				.size()];
		return (ZutrittsobjektverwendungDto[]) list.toArray(returnArray);
	}

	public void createZutrittsleser(ZutrittsleserDto zutrittsleserDto)
			throws EJBExceptionLP {
		if (zutrittsleserDto == null) {
			return;
		}
		try {
			Zutrittsleser zutrittsleser = new Zutrittsleser(zutrittsleserDto
					.getCNr());
			em.persist(zutrittsleser);
			em.flush();
			setZutrittsleserFromZutrittsleserDto(zutrittsleser,
					zutrittsleserDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateZutrittsleser(ZutrittsleserDto zutrittsleserDto)
			throws EJBExceptionLP {
		if (zutrittsleserDto != null) {
			String cNr = zutrittsleserDto.getCNr();
			try {
				Zutrittsleser zutrittsleser = em.find(Zutrittsleser.class, cNr);
				setZutrittsleserFromZutrittsleserDto(zutrittsleser,
						zutrittsleserDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public ZutrittsleserDto zutrittsleserFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		try {
			Zutrittsleser zutrittsleser = em.find(Zutrittsleser.class, cNr);
			if (zutrittsleser == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleZutrittsleserDto(zutrittsleser);
		}
		// catch (FinderException fe) {
		// throw fe;
		// }
		catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public ZutrittsleserDto[] zutrittsleserFindAll() throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("ZutrittsleserfindAll");
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,null);
			// }
			return assembleZutrittsleserDtos(cl);

		}
		// catch (FinderException fe) {
		// throw fe;
		// }
		catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		}
	}

	private void setZutrittsleserFromZutrittsleserDto(
			Zutrittsleser zutrittsleser, ZutrittsleserDto zutrittsleserDto) {
		zutrittsleser.setCBez(zutrittsleserDto.getCBez());
		em.merge(zutrittsleser);
		em.flush();
	}

	private ZutrittsleserDto assembleZutrittsleserDto(
			Zutrittsleser zutrittsleser) {
		return ZutrittsleserDtoAssembler.createDto(zutrittsleser);
	}

	private ZutrittsleserDto[] assembleZutrittsleserDtos(
			Collection<?> zutrittslesers) {
		List<ZutrittsleserDto> list = new ArrayList<ZutrittsleserDto>();
		if (zutrittslesers != null) {
			Iterator<?> iterator = zutrittslesers.iterator();
			while (iterator.hasNext()) {
				Zutrittsleser zutrittsleser = (Zutrittsleser) iterator.next();
				list.add(assembleZutrittsleserDto(zutrittsleser));
			}
		}
		ZutrittsleserDto[] returnArray = new ZutrittsleserDto[list.size()];
		return (ZutrittsleserDto[]) list.toArray(returnArray);
	}

	public void createZutrittsoeffnungsart(
			ZutrittsoeffnungsartDto zutrittsoeffnungsartDto)
			throws EJBExceptionLP {
		if (zutrittsoeffnungsartDto == null) {
			return;
		}
		try {
			Zutrittsoeffnungsart zutrittsoeffnungsart = new Zutrittsoeffnungsart(
					zutrittsoeffnungsartDto.getCNr());
			em.persist(zutrittsoeffnungsart);
			em.flush();
			setZutrittsoeffnungsartFromZutrittsoeffnungsartDto(
					zutrittsoeffnungsart, zutrittsoeffnungsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateZutrittsoeffnungsart(
			ZutrittsoeffnungsartDto zutrittsoeffnungsartDto)
			throws EJBExceptionLP {
		if (zutrittsoeffnungsartDto != null) {
			String cNr = zutrittsoeffnungsartDto.getCNr();
			try {
				Zutrittsoeffnungsart zutrittsoeffnungsart = em.find(
						Zutrittsoeffnungsart.class, cNr);
				setZutrittsoeffnungsartFromZutrittsoeffnungsartDto(
						zutrittsoeffnungsart, zutrittsoeffnungsartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public ZutrittsoeffnungsartDto zutrittsoeffnungsartFindByPrimaryKey(
			String cNr) throws EJBExceptionLP {
		try {
			Zutrittsoeffnungsart zutrittsoeffnungsart = em.find(
					Zutrittsoeffnungsart.class, cNr);
			if (zutrittsoeffnungsart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleZutrittsoeffnungsartDto(zutrittsoeffnungsart);
		}
		// catch (FinderException fe) {
		// throw fe;
		// }
		catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public ZutrittsoeffnungsartDto[] zutrittsoeffnungsartFindAll()
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("ZutrittsoeffnungsartfindAll");
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
			// null);
			// }
			return assembleZutrittsoeffnungsartDtos(cl);
		}
		// catch (FinderException fe) {
		// throw fe;
		// }
		catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		}
	}

	private void setZutrittsoeffnungsartFromZutrittsoeffnungsartDto(
			Zutrittsoeffnungsart zutrittsoeffnungsart,
			ZutrittsoeffnungsartDto zutrittsoeffnungsartDto) {
		zutrittsoeffnungsart.setCBez(zutrittsoeffnungsartDto.getCBez());
		em.merge(zutrittsoeffnungsart);
		em.flush();
	}

	private ZutrittsoeffnungsartDto assembleZutrittsoeffnungsartDto(
			Zutrittsoeffnungsart zutrittsoeffnungsart) {
		return ZutrittsoeffnungsartDtoAssembler.createDto(zutrittsoeffnungsart);
	}

	private ZutrittsoeffnungsartDto[] assembleZutrittsoeffnungsartDtos(
			Collection<?> zutrittsoeffnungsarts) {
		List<ZutrittsoeffnungsartDto> list = new ArrayList<ZutrittsoeffnungsartDto>();
		if (zutrittsoeffnungsarts != null) {
			Iterator<?> iterator = zutrittsoeffnungsarts.iterator();
			while (iterator.hasNext()) {
				Zutrittsoeffnungsart zutrittsoeffnungsart = (Zutrittsoeffnungsart) iterator
						.next();
				list.add(assembleZutrittsoeffnungsartDto(zutrittsoeffnungsart));
			}
		}
		ZutrittsoeffnungsartDto[] returnArray = new ZutrittsoeffnungsartDto[list
				.size()];
		return (ZutrittsoeffnungsartDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittslog(ZutrittslogDto zutrittslogDto)
			throws EJBExceptionLP {
		if (zutrittslogDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittslogDto == null"));
		}
		if (zutrittslogDto.getBErlaubt() == null
				|| zutrittslogDto.getCPerson() == null
				|| zutrittslogDto.getCZutrittscontroller() == null
				|| zutrittslogDto.getCZutrittsobjekt() == null
				|| zutrittslogDto.getMandantCNr() == null
				|| zutrittslogDto.getTZeitpunkt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittslogDto.getBErlaubt() == null || zutrittslogDto.getCPerson() == null || zutrittslogDto.getCZutrittscontroller() == null || zutrittslogDto.getCZutrittsobjekt() == null || zutrittslogDto.getMandantCNr() == null || zutrittslogDto.getTZeitpunkt() == null"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTSLOG);
		zutrittslogDto.setIId(pk);
		try {
			Zutrittslog zutrittslog = new Zutrittslog(zutrittslogDto.getIId(),
					zutrittslogDto.getCZutrittscontroller(), zutrittslogDto
							.getCZutrittsobjekt(), zutrittslogDto
							.getTZeitpunkt(), zutrittslogDto.getMandantCNr(),
					zutrittslogDto.getBErlaubt(), zutrittslogDto
							.getMandantCNrObjekt());
			em.persist(zutrittslog);
			em.flush();
			setZutrittslogFromZutrittslogDto(zutrittslog, zutrittslogDto);
			return zutrittslogDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ZutrittslogDto zutrittslogFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittslog zutrittslog = em.find(Zutrittslog.class, iId);
		if (zutrittslog == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittslogDto(zutrittslog);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	private void setZutrittslogFromZutrittslogDto(Zutrittslog zutrittslog,
			ZutrittslogDto zutrittslogDto) {
		zutrittslog.setCAusweis(zutrittslogDto.getCAusweis());
		zutrittslog.setCPerson(zutrittslogDto.getCPerson());
		zutrittslog.setCZutrittscontroller(zutrittslogDto
				.getCZutrittscontroller());
		zutrittslog.setCZutrittsobjekt(zutrittslogDto.getCZutrittsobjekt());
		zutrittslog.setTZeitpunkt(zutrittslogDto.getTZeitpunkt());
		zutrittslog.setMandantCNr(zutrittslogDto.getMandantCNr());
		zutrittslog.setBErlaubt(zutrittslogDto.getBErlaubt());
		em.merge(zutrittslog);
		em.flush();
	}

	private ZutrittslogDto assembleZutrittslogDto(Zutrittslog zutrittslog) {
		return ZutrittslogDtoAssembler.createDto(zutrittslog);
	}

	private ZutrittslogDto[] assembleZutrittslogDtos(Collection<?> zutrittslogs) {
		List<ZutrittslogDto> list = new ArrayList<ZutrittslogDto>();
		if (zutrittslogs != null) {
			Iterator<?> iterator = zutrittslogs.iterator();
			while (iterator.hasNext()) {
				Zutrittslog zutrittslog = (Zutrittslog) iterator.next();
				list.add(assembleZutrittslogDto(zutrittslog));
			}
		}
		ZutrittslogDto[] returnArray = new ZutrittslogDto[list.size()];
		return (ZutrittslogDto[]) list.toArray(returnArray);
	}

	public Integer createZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws EJBExceptionLP {
		if (zutrittdaueroffenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittdaueroffenDto == null"));
		}
		if (zutrittdaueroffenDto.getTagesartIId() == null
				|| zutrittdaueroffenDto.getUOffenbis() == null
				|| zutrittdaueroffenDto.getUOffenvon() == null
				|| zutrittdaueroffenDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittdaueroffenDto.getTagesartIId() == null || zutrittdaueroffenDto.getUOffenbis() == null || zutrittdaueroffenDto.getUOffenvon() == null || zutrittdaueroffenDto.getZutrittsobjektIId() == null"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUTRITTDAUEROFFEN);
		zutrittdaueroffenDto.setIId(pk);

		try {
			Zutrittdaueroffen zutrittdaueroffen = new Zutrittdaueroffen(
					zutrittdaueroffenDto.getIId(), zutrittdaueroffenDto
							.getTagesartIId(), zutrittdaueroffenDto
							.getZutrittsobjektIId(), zutrittdaueroffenDto
							.getUOffenvon(), zutrittdaueroffenDto
							.getUOffenbis());
			em.persist(zutrittdaueroffen);
			em.flush();
			setZutrittdaueroffenFromZutrittdaueroffenDto(zutrittdaueroffen,
					zutrittdaueroffenDto);
			return zutrittdaueroffenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zutrittdaueroffenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittdaueroffenDto == null"));
		}
		if (zutrittdaueroffenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittdaueroffen toRemove = em.find(Zutrittdaueroffen.class,
				zutrittdaueroffenDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws EJBExceptionLP {
		if (zutrittdaueroffenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zutrittdaueroffenDto == null"));
		}
		if (zutrittdaueroffenDto.getIId() == null
				|| zutrittdaueroffenDto.getTagesartIId() == null
				|| zutrittdaueroffenDto.getUOffenbis() == null
				|| zutrittdaueroffenDto.getUOffenvon() == null
				|| zutrittdaueroffenDto.getZutrittsobjektIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zutrittdaueroffenDto.getIId() == null || zutrittdaueroffenDto.getTagesartIId() == null || zutrittdaueroffenDto.getUOffenbis() == null || zutrittdaueroffenDto.getUOffenvon() == null || zutrittdaueroffenDto.getZutrittsobjektIId() == null"));
		}

		Integer iId = zutrittdaueroffenDto.getIId();
		// try {
		Zutrittdaueroffen zutrittdaueroffen = em.find(Zutrittdaueroffen.class,
				iId);
		if (zutrittdaueroffen == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZutrittdaueroffenFromZutrittdaueroffenDto(zutrittdaueroffen,
				zutrittdaueroffenDto);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public String[] zutrittonlinecheckAusweiseFindByTGueltigab(Timestamp tDatum)
			throws EJBExceptionLP {
		if (tDatum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tDatum == null"));
		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRZutrittonlinecheck.class)
				.add(
						Restrictions
								.isNotNull(ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_C_AUSWEIS));
		crit.add(Restrictions.ge(
				ZutrittscontrollerFac.FLR_ZUTRITTONLINECHECK_T_GUELITGBIS,
				tDatum));
		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		String[] ausweise = new String[resultList.size()];
		int row = 0;
		while (resultListIterator.hasNext()) {

			FLRZutrittonlinecheck o = (FLRZutrittonlinecheck) resultListIterator
					.next();
			ausweise[row] = o.getC_ausweis();
			row++;

		}
		return ausweise;
	}

	public ZutrittdaueroffenDto zutrittdaueroffenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zutrittdaueroffen zutrittdaueroffen = em.find(Zutrittdaueroffen.class,
				iId);
		if (zutrittdaueroffen == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZutrittdaueroffenDto(zutrittdaueroffen);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZutrittdaueroffenFromZutrittdaueroffenDto(
			Zutrittdaueroffen zutrittdaueroffen,
			ZutrittdaueroffenDto zutrittdaueroffenDto) {
		zutrittdaueroffen.setTagesartIId(zutrittdaueroffenDto.getTagesartIId());
		zutrittdaueroffen.setZutrittsobjektIId(zutrittdaueroffenDto
				.getZutrittsobjektIId());
		zutrittdaueroffen.setUOffenvon(zutrittdaueroffenDto.getUOffenvon());
		zutrittdaueroffen.setUOffenbis(zutrittdaueroffenDto.getUOffenbis());
		em.merge(zutrittdaueroffen);
		em.flush();
	}

	private ZutrittdaueroffenDto assembleZutrittdaueroffenDto(
			Zutrittdaueroffen zutrittdaueroffen) {
		return ZutrittdaueroffenDtoAssembler.createDto(zutrittdaueroffen);
	}

	private ZutrittdaueroffenDto[] assembleZutrittdaueroffenDtos(
			Collection<?> zutrittdaueroffens) {
		List<ZutrittdaueroffenDto> list = new ArrayList<ZutrittdaueroffenDto>();
		if (zutrittdaueroffens != null) {
			Iterator<?> iterator = zutrittdaueroffens.iterator();
			while (iterator.hasNext()) {
				Zutrittdaueroffen zutrittdaueroffen = (Zutrittdaueroffen) iterator
						.next();
				list.add(assembleZutrittdaueroffenDto(zutrittdaueroffen));
			}
		}
		ZutrittdaueroffenDto[] returnArray = new ZutrittdaueroffenDto[list
				.size()];
		return (ZutrittdaueroffenDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalfinger(PersonalfingerDto personalfingerDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalfingerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalfingerDto == null"));
		}
		if (personalfingerDto.getPersonalIId() == null
				||
				// personalfingerDto.getIFingerid() == null ||
				personalfingerDto.getFingerartIId() == null
				|| personalfingerDto.getOTemplate1() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalfingerDto.getPersonalIId() == null || personalfingerDto.getIFingerid() == null || personalfingerDto.getFingerartIId() == null ||         personalfingerDto.getOTemplate1() == null "));
		}

		try {
			Query query = em
					.createNamedQuery("PersonalfingerfindByPersonalIIdFingerartIId");
			query.setParameter(1, personalfingerDto.getPersonalIId());
			query.setParameter(2, personalfingerDto.getFingerartIId());
			// @todo getSingleResult oder getResultList ?
			Personalfinger doppelt = (Personalfinger) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALFINGER.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALFINGER);
			personalfingerDto.setIId(pk);
			personalfingerDto.setIFingerid(pk);

			personalfingerDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			personalfingerDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());

			Personalfinger personalfinger = new Personalfinger(
					personalfingerDto.getIId(), personalfingerDto
							.getPersonalIId(),
					personalfingerDto.getIFingerid(), personalfingerDto
							.getOTemplate1(), personalfingerDto.getTAendern(),
					personalfingerDto.getPersonalIIdAendern(),
					personalfingerDto.getFingerartIId());
			em.persist(personalfinger);
			em.flush();

			setPersonalfingerFromPersonalfingerDto(personalfinger,
					personalfingerDto);
			return personalfingerDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePersonalfinger(PersonalfingerDto personalfingerDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (personalfingerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalfingerDto == null"));
		}
		if (personalfingerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Personalfinger toRemove = em.find(Personalfinger.class,
				personalfingerDto.getIId());
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updatePersonalfinger(PersonalfingerDto personalfingerDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalfingerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalfingerDto == null"));
		}
		if (personalfingerDto.getIId() == null
				|| personalfingerDto.getPersonalIId() == null
				|| personalfingerDto.getIFingerid() == null
				|| personalfingerDto.getOTemplate1() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalfingerDto.getIId() == null || personalfingerDto.getPersonalIId() == null || personalfingerDto.getIFingerid() == null || personalfingerDto.getIFingersubid() == null || personalfingerDto.getOTemplate() == null"));
		}

		Integer iId = personalfingerDto.getIId();
		// try {
		// try {
		Query query = em
				.createNamedQuery("PersonalfingerfindByPersonalIIdFingerartIId");
		query.setParameter(1, personalfingerDto.getPersonalIId());
		query.setParameter(2, personalfingerDto.getFingerartIId());
		Integer iIdVorhanden = ((Personalfinger) query.getSingleResult())
				.getIId();

		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONALFINGER.UK"));
		}
		// }
		// catch (FinderException ex) {
		// // nothing here
		// }
		personalfingerDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		personalfingerDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		Personalfinger personalfinger = em.find(Personalfinger.class, iId);
		if (personalfinger == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setPersonalfingerFromPersonalfingerDto(personalfinger,
				personalfingerDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public PersonalfingerDto personalfingerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalfinger personalfinger = em.find(Personalfinger.class, iId);
		if (personalfinger == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalfingerDto(personalfinger);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PersonalfingerDto[] personalfingerFindByTAendern(
			java.sql.Timestamp tAendern, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (tAendern == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tAendern == null"));
		}

		// try {
		Query query = em.createNamedQuery("PersonalfingerfindByTAendern");
		query.setParameter(1, tAendern);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assemblePersonalfingerDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PersonalfingerDto[] personalfingerFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PersonalfingerfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assemblePersonalfingerDtos(query.getResultList());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	/*
	 * public PersonalfingerDto personalfingerFindByIFingerid(Integer iFingerid)
	 * throws EJBException { try { Query query =
	 * em.createNamedQuery("PersonalfingerfindByFingerid");
	 * query.setParameter(1, iFingerid); // @todo getSingleResult oder
	 * getResultList ? return
	 * assemblePersonalfingerDto((Personalfinger)query.getSingleResult()); }
	 * catch (FinderException e) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e); } }
	 */

	private void setPersonalfingerFromPersonalfingerDto(
			Personalfinger personalfinger, PersonalfingerDto personalfingerDto) {
		personalfinger.setPersonalIId(personalfingerDto.getPersonalIId());
		personalfinger.setIFingerid(personalfingerDto.getIFingerid());
		personalfinger.setOTemplate1(personalfingerDto.getOTemplate1());
		personalfinger.setOTemplate2(personalfingerDto.getOTemplate2());
		personalfinger.setFingerartIId(personalfingerDto.getFingerartIId());
		personalfinger.setTAendern(personalfingerDto.getTAendern());
		personalfinger.setPersonalIIdAendern(personalfingerDto
				.getPersonalIIdAendern());
		em.merge(personalfinger);
		em.flush();
	}

	private PersonalfingerDto assemblePersonalfingerDto(
			Personalfinger personalfinger) {
		return PersonalfingerDtoAssembler.createDto(personalfinger);
	}

	private PersonalfingerDto[] assemblePersonalfingerDtos(
			Collection<?> personalfingers) {
		List<PersonalfingerDto> list = new ArrayList<PersonalfingerDto>();
		if (personalfingers != null) {
			Iterator<?> iterator = personalfingers.iterator();
			while (iterator.hasNext()) {
				Personalfinger personalfinger = (Personalfinger) iterator
						.next();
				list.add(assemblePersonalfingerDto(personalfinger));
			}
		}
		PersonalfingerDto[] returnArray = new PersonalfingerDto[list.size()];
		return (PersonalfingerDto[]) list.toArray(returnArray);
	}

	public Integer createFingerart(FingerartDto fingerartDto)
			throws EJBExceptionLP {
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FINGERART);
			fingerartDto.setIId(pk);
			Fingerart fingerart = new Fingerart(fingerartDto.getIId(),
					fingerartDto.getCBez());
			em.persist(fingerart);
			em.flush();
			setFingerartFromFingerartDto(fingerart, fingerartDto);
			return pk;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeFingerart(Integer iId) throws EJBExceptionLP {
		// try {
		Fingerart toRemove = em.find(Fingerart.class, iId);
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
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void removeFingerart(FingerartDto fingerartDto)
			throws EJBExceptionLP {
		if (fingerartDto != null) {
			Integer iId = fingerartDto.getIId();
			removeFingerart(iId);
		}
	}

	public void updateFingerart(FingerartDto fingerartDto)
			throws EJBExceptionLP {
		if (fingerartDto != null) {
			Integer iId = fingerartDto.getIId();
			try {
				Fingerart fingerart = em.find(Fingerart.class, iId);
				setFingerartFromFingerartDto(fingerart, fingerartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public FingerartDto fingerartFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Fingerart fingerart = em.find(Fingerart.class, iId);
			if (fingerart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleFingerartDto(fingerart);
		}
		// catch (FinderException fe) {
		// throw fe;
		// }
		catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setFingerartFromFingerartDto(Fingerart fingerart,
			FingerartDto fingerartDto) {
		fingerart.setCBez(fingerartDto.getCBez());
		em.merge(fingerart);
		em.flush();
	}

	private FingerartDto assembleFingerartDto(Fingerart fingerart) {
		return FingerartDtoAssembler.createDto(fingerart);
	}

	private FingerartDto[] assembleFingerartDtos(Collection<?> fingerarts) {
		List<FingerartDto> list = new ArrayList<FingerartDto>();
		if (fingerarts != null) {
			Iterator<?> iterator = fingerarts.iterator();
			while (iterator.hasNext()) {
				Fingerart fingerart = (Fingerart) iterator.next();
				list.add(assembleFingerartDto(fingerart));
			}
		}
		FingerartDto[] returnArray = new FingerartDto[list.size()];
		return (FingerartDto[]) list.toArray(returnArray);
	}
}
