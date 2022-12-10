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
package com.lp.server.benutzer.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.benutzer.ejb.Recht;
import com.lp.server.benutzer.ejb.Rollerecht;
import com.lp.server.benutzer.service.LagerrolleDto;
import com.lp.server.benutzer.service.RechtDto;
import com.lp.server.benutzer.service.RechtDtoAssembler;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.benutzer.service.RollerechtDtoAssembler;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;

@Stateless
public class RechteFacBean extends Facade implements RechteFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createRollerecht(RollerechtDto rollerechtDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (rollerechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rollerechtDto == null"));
		}
		if (rollerechtDto.getRechtCNr() == null
				|| rollerechtDto.getSystemrolleIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"rollerechtDto.getRechtCNr() == null || rollerechtDto.getSystemrolleIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("RollerechtfindBySystemrolleIIdRechtCNr");
			query.setParameter(1, rollerechtDto.getSystemrolleIId());
			query.setParameter(2, rollerechtDto.getRechtCNr());
			Rollerecht temp = (Rollerecht) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ROLLERECHT.UK"));

		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ROLLERECHT);
		rollerechtDto.setIId(pk);

		rollerechtDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		rollerechtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		try {
			Rollerecht rollerecht = new Rollerecht(rollerechtDto.getIId(),
					rollerechtDto.getSystemrolleIId(),
					rollerechtDto.getRechtCNr(),
					rollerechtDto.getPersonalIIdAendern());
			em.persist(rollerecht);
			em.flush();
			
			HvDtoLogger<RollerechtDto> dtoLogger = new HvDtoLogger<RollerechtDto>(
					em, rollerechtDto.getIId(), theClientDto);
			dtoLogger.logInsert(rollerechtDto);
			
			setRollerechtFromRollerechtDto(rollerecht, rollerechtDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		getBenutzerServicesFac().reloadRolleRechte();
		return rollerechtDto.getIId();
	}

	public void removeRollerecht(RollerechtDto rollerechtDto,TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		myLogger.entry();
		if (rollerechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rollerechtDto == null"));
		}
		if (rollerechtDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("rollerechtDto.getIId() == null"));
		}
		try {
			Rollerecht rollerecht = em.find(Rollerecht.class,
					rollerechtDto.getIId());
			if (rollerecht == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			RollerechtDto dto = assembleRollerechtDto(rollerecht);
			HvDtoLogger<RollerechtDto> zsLogger = new HvDtoLogger<RollerechtDto>(
					em, dto.getIId(), theClientDto);
			zsLogger.logDelete(dto);
			
			em.remove(rollerecht);
			em.flush();
		}

		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
		getBenutzerServicesFac().reloadRolleRechte();
	}

	public void updateRollerecht(RollerechtDto rollerechtDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		if (rollerechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rollerechtDto == null"));
		}
		if (rollerechtDto.getIId() == null
				|| rollerechtDto.getRechtCNr() == null
				|| rollerechtDto.getSystemrolleIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"rollerechtDto.getIId() == null || rollerechtDto.getRechtCNr() == null || rollerechtDto.getSystemrolleIId() == null"));
		}

		RollerechtDto rollerechtDto_Vorher = rollerechtFindByPrimaryKey(rollerechtDto
				.getIId());
		HvDtoLogger<RollerechtDto> logger = new HvDtoLogger<RollerechtDto>(
				em, rollerechtDto.getIId(), theClientDto);
		logger.log(rollerechtDto_Vorher, rollerechtDto);
		
		
		Integer iId = rollerechtDto.getIId();
		// try {
		Rollerecht rollerecht = em.find(Rollerecht.class, iId);
		if (rollerecht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em
					.createNamedQuery("RollerechtfindBySystemrolleIIdRechtCNr");
			query.setParameter(1, rollerechtDto.getSystemrolleIId());
			query.setParameter(2, rollerechtDto.getRechtCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Rollerecht) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ROLLERECHT.UK"));
			}

		} catch (NoResultException ex) {
			// nix
		}

		rollerechtDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		rollerechtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		setRollerechtFromRollerechtDto(rollerecht, rollerechtDto);
		getBenutzerServicesFac().reloadRolleRechte();
	}

	public RollerechtDto rollerechtFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Rollerecht rollerecht = em.find(Rollerecht.class, iId);
		if (rollerecht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRollerechtDto(rollerecht);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public RollerechtDto rollerechtFindBySystemrolleIIdRechtCNr(
			Integer systemrolleIId, String rechtCNr) throws EJBExceptionLP {
		if (systemrolleIId == null || rechtCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("systemrolleIId == null || rechtCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("RollerechtfindBySystemrolleIIdRechtCNr");
			query.setParameter(1, systemrolleIId);
			query.setParameter(2, rechtCNr);
			Rollerecht rollerecht = (Rollerecht) query.getSingleResult();
			return assembleRollerechtDto(rollerecht);
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		}
	}

	public RollerechtDto rollerechtFindBySystemrolleIIdRechtCNrOhneExc(
			Integer systemrolleIId, String rechtCNr) throws EJBExceptionLP {
		if (systemrolleIId == null || rechtCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("systemrolleIId == null || rechtCNr == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("RollerechtfindBySystemrolleIIdRechtCNr");
		query.setParameter(1, systemrolleIId);
		query.setParameter(2, rechtCNr);
		try {
			Rollerecht rollerecht = (Rollerecht) query.getSingleResult();
			if (rollerecht == null) {
				return null;
			}
			return assembleRollerechtDto(rollerecht);

		} catch (NoResultException ex1) {
			return null;
		}

	}

	private void setRollerechtFromRollerechtDto(Rollerecht rollerecht,
			RollerechtDto rollerechtDto) {
		rollerecht.setSystemrolleIId(rollerechtDto.getSystemrolleIId());
		rollerecht.setRechtCNr(rollerechtDto.getRechtCNr());
		rollerecht.setTAendern(rollerechtDto.getTAendern());
		rollerecht.setPersonalIIdAendern(rollerechtDto.getPersonalIIdAendern());
		em.merge(rollerecht);
		em.flush();
	}

	private RollerechtDto assembleRollerechtDto(Rollerecht rollerecht) {
		return RollerechtDtoAssembler.createDto(rollerecht);
	}

	private RollerechtDto[] assembleRollerechtDtos(Collection<?> rollerechts) {
		List<RollerechtDto> list = new ArrayList<RollerechtDto>();
		if (rollerechts != null) {
			Iterator<?> iterator = rollerechts.iterator();
			while (iterator.hasNext()) {
				Rollerecht rollerecht = (Rollerecht) iterator.next();
				list.add(assembleRollerechtDto(rollerecht));
			}
		}
		RollerechtDto[] returnArray = new RollerechtDto[list.size()];
		return (RollerechtDto[]) list.toArray(returnArray);
	}

	public void createRecht(RechtDto rechtDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechtDto == null"));
		}
		if (rechtDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("rechtDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("RechtfindByCNr");
			query.setParameter(1, rechtDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Recht temp = (Recht) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_RECHT.C_NR"));
		} catch (NoResultException ex1) {
		}

		try {
			Recht recht = new Recht(rechtDto.getCNr());
			em.persist(recht);
			em.flush();
			setRechtFromRechtDto(recht, rechtDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void kopiereRechteEinerRolle(Integer systemrolleIIdQuelle,
			Integer systemrolleIIdZiel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// try {
		Query query = em.createNamedQuery("RollerechtfindBySystemrolleIId");
		query.setParameter(1, systemrolleIIdQuelle);
		// @todo getSingleResult oder getResultList ?
		RollerechtDto[] rollerechtDtos = assembleRollerechtDtos(query
				.getResultList());
		query = em.createNamedQuery("RollerechtfindBySystemrolleIIdRechtCNr");
		for (int i = 0; i < rollerechtDtos.length; i++) {
			query.setParameter(1, systemrolleIIdZiel);
			query.setParameter(2, rollerechtDtos[i].getRechtCNr());
			try {
				Rollerecht rollerecht = (Rollerecht) query.getSingleResult();
				// Dann eintragen
				/*
				 * RollerechtDto rollerechtDto = new RollerechtDto();
				 * rollerechtDto.setRechtCNr(rollerechtDtos[i].getRechtCNr());
				 * rollerechtDto.setSystemrolleIId(systemrolleIIdZiel);
				 * createRollerecht(rollerechtDto, theClientDto);
				 */

			} catch (NoResultException ex2) {
				// Dann eintragen
				RollerechtDto rollerechtDto = new RollerechtDto();
				rollerechtDto.setRechtCNr(rollerechtDtos[i].getRechtCNr());
				rollerechtDto.setSystemrolleIId(systemrolleIIdZiel);
				createRollerecht(rollerechtDto, theClientDto);
			}
		}
		getBenutzerServicesFac().reloadRolleRechte();
	}

	public void removeRecht(RechtDto rechtDto) throws EJBExceptionLP {
		myLogger.entry();
		if (rechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechtDto == null"));
		}
		if (rechtDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("rechtDto.getCNr() == null"));
		}
		try {
			Recht recht = em.find(Recht.class, rechtDto.getCNr());
			if (recht == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(recht);
			em.flush();
			// recht.remove();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateRecht(RechtDto rechtDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechtDto == null"));
		}
		if (rechtDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("rechtDto.getCNr() == null"));
		}

		String cNr = rechtDto.getCNr();
		// try {
		Recht recht = em.find(Recht.class, cNr);
		if (recht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setRechtFromRechtDto(recht, rechtDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public RechtDto rechtFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		myLogger.entry();
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		// try {
		Recht recht = em.find(Recht.class, cNr);
		if (recht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRechtDto(recht);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setRechtFromRechtDto(Recht recht, RechtDto rechtDto) {
		recht.setXKommentar(rechtDto.getXKommentar());
		em.merge(recht);
		em.flush();
	}

	private RechtDto assembleRechtDto(Recht recht) {
		return RechtDtoAssembler.createDto(recht);
	}

	private RechtDto[] assembleRechtDtos(Collection<?> rechts) {
		List<RechtDto> list = new ArrayList<RechtDto>();
		if (rechts != null) {
			Iterator<?> iterator = rechts.iterator();
			while (iterator.hasNext()) {
				Recht recht = (Recht) iterator.next();
				list.add(assembleRechtDto(recht));
			}
		}
		RechtDto[] returnArray = new RechtDto[list.size()];
		return (RechtDto[]) list.toArray(returnArray);
	}
}
