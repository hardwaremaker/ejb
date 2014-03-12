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
package com.lp.server.anfrage.ejbfac;

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

import com.lp.server.anfrage.ejb.Anfrageart;
import com.lp.server.anfrage.ejb.Anfrageartspr;
import com.lp.server.anfrage.ejb.AnfrageartsprPK;
import com.lp.server.anfrage.ejb.Anfragepositionart;
import com.lp.server.anfrage.ejb.Anfragestatus;
import com.lp.server.anfrage.ejb.Anfragetext;
import com.lp.server.anfrage.ejb.Zertifikatart;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfrageartDto;
import com.lp.server.anfrage.service.AnfrageartDtoAssembler;
import com.lp.server.anfrage.service.AnfrageartsprDto;
import com.lp.server.anfrage.service.AnfrageartsprDtoAssembler;
import com.lp.server.anfrage.service.AnfragepositionartDto;
import com.lp.server.anfrage.service.AnfragepositionartDtoAssembler;
import com.lp.server.anfrage.service.AnfragestatusDto;
import com.lp.server.anfrage.service.AnfragestatusDtoAssembler;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.anfrage.service.AnfragetextDtoAssembler;
import com.lp.server.anfrage.service.ZertifikatartDto;
import com.lp.server.anfrage.service.ZertifikatartDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AnfrageServiceFacBean extends Facade implements AnfrageServiceFac {
	@PersistenceContext
	private EntityManager em;
	private Anfragepositionart anfragepositionart;
	private Anfragetext anfragetext;
	private Anfrageart anfrageart;
	private Anfrageartspr anfrageartspr;

	// Anfragestatus
	// -------------------------------------------------------------

	public void createAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP {
		if (anfragestatusDto == null) {
			return;
		}
		try {
			Anfragestatus anfragestatus = new Anfragestatus(anfragestatusDto
					.getStatusCNr(), anfragestatusDto.getISort());
			em.persist(anfragestatus);
			em.flush();

			setAnfragestatusFromAnfragestatusDto(anfragestatus,
					anfragestatusDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP {
		if (anfragestatusDto != null) {
			String statusCNr = anfragestatusDto.getStatusCNr();
			removeAnfragestatus(statusCNr);
		}
	}

	private void removeAnfragestatus(String statusCNr) throws EJBExceptionLP {
		try {
			Anfragestatus toRemove = em.find(Anfragestatus.class, statusCNr);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAnfrageStatus. Es gibt keine Status mit cnr "
								+ statusCNr);
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP {
		if (anfragestatusDto != null) {
			String statusCNr = anfragestatusDto.getStatusCNr();
			// try {
			Anfragestatus anfragestatus = em.find(Anfragestatus.class,
					statusCNr);
			if (anfragestatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAnfragestatus. Es gibt keinen Status mit cnr "
								+ anfragestatusDto.getStatusCNr()
								+ "\nAnfragestatusSto.toString: "
								+ anfragestatusDto.toString());
			}
			setAnfragestatusFromAnfragestatusDto(anfragestatus,
					anfragestatusDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public AnfragestatusDto anfragestatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		// try {
		Anfragestatus anfragestatus = em.find(Anfragestatus.class, statusCNr);
		if (anfragestatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfragestatusfindbyPrimarykey. Es gibt keinen Status mit cnr "
							+ statusCNr);
		}
		return assembleAnfragestatusDto(anfragestatus);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void setAnfragestatusFromAnfragestatusDto(
			Anfragestatus anfragestatus, AnfragestatusDto anfragestatusDto) {
		anfragestatus.setISort(anfragestatusDto.getISort());
		em.merge(anfragestatus);
		em.flush();
	}

	private AnfragestatusDto assembleAnfragestatusDto(
			Anfragestatus anfragestatus) {
		return AnfragestatusDtoAssembler.createDto(anfragestatus);
	}

	private AnfragestatusDto[] assembleAnfragestatusDtos(
			Collection<?> anfragestatuss) {
		List<AnfragestatusDto> list = new ArrayList<AnfragestatusDto>();
		if (anfragestatuss != null) {
			Iterator<?> iterator = anfragestatuss.iterator();
			while (iterator.hasNext()) {
				Anfragestatus anfragestatus = (Anfragestatus) iterator.next();
				list.add(assembleAnfragestatusDto(anfragestatus));
			}
		}
		AnfragestatusDto[] returnArray = new AnfragestatusDto[list.size()];
		return (AnfragestatusDto[]) list.toArray(returnArray);
	}

	// Anfragepositionart
	// --------------------------------------------------------

	public String createAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfragepositionartDto(anfragepositionartDtoI);

		try {
			Anfragepositionart anfragepositionart = new Anfragepositionart(
					anfragepositionartDtoI.getPositionsartCNr(),
					anfragepositionartDtoI.getISort(), anfragepositionartDtoI
							.getBVersteckt());
			em.persist(anfragepositionart);
			em.flush();

			setAnfragepositionartFromAnfragepositionartDto(anfragepositionart,
					anfragepositionartDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return anfragepositionartDtoI.getPositionsartCNr();
	}

	public void removeAnfragepositionart(String cNrAnfragepositionartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfragepositionartCNr(cNrAnfragepositionartI);

		// try {
		Anfragepositionart toRemove = em.find(Anfragepositionart.class,
				cNrAnfragepositionartI);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler beim l\u00F6schen der Anfragepositionart. Es gibt keine Anfragepositionart mit der cnr "
							+ cNrAnfragepositionartI);
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

	public void updateAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfragepositionartDto(anfragepositionartDtoI);

		// try {
		Anfragepositionart anfragepositionart = em.find(
				Anfragepositionart.class, anfragepositionartDtoI.getCNr());
		if (anfragepositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateAnfragepositionArt. Es gibt keine Positionsart mit Cnr "
							+ anfragepositionartDtoI.getCNr()
							+ "\n anfragepositionartDto.toString: "
							+ anfragepositionartDtoI.toString());
		}

		setAnfragepositionartFromAnfragepositionartDto(anfragepositionart,
				anfragepositionartDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public AnfragepositionartDto anfragepositionartFindByPrimaryKey(
			String cNrAnfragepositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// check2(cNrUserI);
		// checkAnfragepositionartCNr(cNrAnfragepositionartI);

		AnfragepositionartDto anfragepositionartDto = null;

		// try {
		Anfragepositionart anfragepositionart = em.find(
				Anfragepositionart.class, cNrAnfragepositionartI);
		if (anfragepositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfragepositionartFindbyPrimaryKey. Es gibt keine Positionart mit der cnr "
							+ cNrAnfragepositionartI);
		}
		anfragepositionartDto = assembleAnfragepositionartDto(anfragepositionart);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return anfragepositionartDto;
	}

	public Map<String, String> getAnfragepositionart(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLocale(locale1I);
		checkLocale(locale2I);

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		try {
			Query query = em
					.createNamedQuery("AnfragepositionartfindAllEnable");

			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
			// null);
			// }
			AnfragepositionartDto[] aAnfragepositionartDto = assembleAnfragepositionartDtos(cl);

			for (int i = 0; i < aAnfragepositionartDto.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(
								aAnfragepositionartDto[i].getPositionsartCNr(),
								locale1I, locale2I);
				map.put(aAnfragepositionartDto[i].getPositionsartCNr(),
						sUebersetzung);
			}
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return map;
	}

	private void checkAnfragepositionartDto(
			AnfragepositionartDto anfragepositionartDtoI) throws EJBExceptionLP {
		if (anfragepositionartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("anfragepositionartDtoI == null"));
		}

		myLogger.info("AnfragepositionartDtoI: "
				+ anfragepositionartDtoI.toString());
	}

	private void checkAnfragepositionartCNr(String cNrAnfragepositionartI)
			throws EJBExceptionLP {
		if (cNrAnfragepositionartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAnfragepositionartI == null"));
		}

		myLogger.info("AnfragepositionartCNr: " + cNrAnfragepositionartI);
	}

	private void setAnfragepositionartFromAnfragepositionartDto(
			Anfragepositionart anfragepositionart,
			AnfragepositionartDto anfragepositionartDto) {
		anfragepositionart.setISort(anfragepositionartDto.getISort());
		anfragepositionart.setBVersteckt(anfragepositionartDto.getBVersteckt());
		em.merge(anfragepositionart);
		em.flush();
	}

	private AnfragepositionartDto assembleAnfragepositionartDto(
			Anfragepositionart anfragepositionart) {
		return AnfragepositionartDtoAssembler.createDto(anfragepositionart);
	}

	private AnfragepositionartDto[] assembleAnfragepositionartDtos(
			Collection<?> anfragepositionarts) {
		List<AnfragepositionartDto> list = new ArrayList<AnfragepositionartDto>();
		if (anfragepositionarts != null) {
			Iterator<?> iterator = anfragepositionarts.iterator();
			while (iterator.hasNext()) {
				Anfragepositionart anfragepositionart = (Anfragepositionart) iterator
						.next();
				list.add(assembleAnfragepositionartDto(anfragepositionart));
			}
		}
		AnfragepositionartDto[] returnArray = new AnfragepositionartDto[list
				.size()];
		return (AnfragepositionartDto[]) list.toArray(returnArray);
	}

	// Anfragetext
	// ---------------------------------------------------------------

	/**
	 * Einen Anfragetext anlegen.
	 * 
	 * @param anfragetextDto
	 *            der Anfragetext
	 * @return Integer PK des Anfragetextes
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragetextDto(anfragetextDto);

		// den PK erzeugen und setzen
		Integer iId = null;

		// try {
		iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ANFRAGETEXT);
		anfragetextDto.setIId(iId);
		/*
		 * } catch (Throwable t) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, t); }
		 */

		try {
			Anfragetext anfragetext = new Anfragetext(anfragetextDto.getIId(),
					anfragetextDto.getMandantCNr(), anfragetextDto
							.getLocaleCNr(), anfragetextDto.getMediaartCNr(),
					anfragetextDto.getXTextinhalt());
			em.persist(anfragetext);
			em.flush();

			setAnfragetextFromAnfragetextDto(anfragetext, anfragetextDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public Integer createZertifikatart(ZertifikatartDto anfragetextDto) {

		if (anfragetextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("anfragetextDto == null"));
		}
		if (anfragetextDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("anfragetextDto.getCBez() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ZertifikatartfindByCBezMandantCNr");
			query.setParameter(1, anfragetextDto.getCBez());
			query.setParameter(2, anfragetextDto.getMandantCNr());
			Zertifikatart doppelt = (Zertifikatart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("ANF_ZERTIFIKATART.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		// den PK erzeugen und setzen
		Integer iId = null;

		iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ZERTIFIKATART);
		anfragetextDto.setIId(iId);

		try {
			Zertifikatart anfragetext = new Zertifikatart(anfragetextDto
					.getIId(), anfragetextDto.getMandantCNr(), anfragetextDto
					.getCBez());
			em.persist(anfragetext);
			em.flush();

			setZertifikatartFromZertifikatartDto(anfragetext, anfragetextDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public void updateZertifikatart(ZertifikatartDto zertifikatartDto) {
		if (zertifikatartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zertifikatartDto == null"));
		}
		if (zertifikatartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zertifikatartDto.getIId() == null"));
		}
		if (zertifikatartDto.getCBez() == null
				&& zertifikatartDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zertifikatartDto.getCBez() == null && zertifikatartDto.getMandantCNr() == null"));
		}

		Integer iId = zertifikatartDto.getIId();
		try {
			Zertifikatart zertifikatart = em.find(Zertifikatart.class, iId);

			try {
				Query query = em
						.createNamedQuery("ZertifikatartfindByCBezMandantCNr");
				query.setParameter(1, zertifikatartDto.getCBez());
				query.setParameter(2, zertifikatartDto.getMandantCNr());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Zertifikatart) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("ANF_ZERTIFIKATART.UK"));
				}

			} catch (NoResultException ex) {

			}

			setZertifikatartFromZertifikatartDto(zertifikatart,
					zertifikatartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public ZertifikatartDto zertifikatartFindByPrimaryKey(Integer iId) {
		// try {
		Zertifikatart zertifikatart = em.find(Zertifikatart.class, iId);
		if (zertifikatart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei farbcodeFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleZertifikatartDto(zertifikatart);

	}

	public void removeZertifikatart(ZertifikatartDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Zertifikatart toRemove = em.find(Zertifikatart.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei remove. Es gibt keine iid " + dto.getIId()
							+ "\ndto.toString: " + dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	/**
	 * Einen Anfragetext loeschen.
	 * 
	 * @param anfragetextDto
	 *            der Anfragetext
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragetextDto(anfragetextDto);

		// try {
		Anfragetext toRemove = em.find(Anfragetext.class, anfragetextDto
				.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAnfragetext. Es gibt keinen Anfragetext mit iid "
							+ anfragetextDto.getIId()
							+ "\nanfragetextDto.toString: "
							+ anfragetextDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, t);
		// }
	}

	/**
	 * Einen Anfragetext aktualisieren.
	 * 
	 * @param anfragetextDto
	 *            der Anfragetext
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragetextDto(anfragetextDto);

		// try {
		Anfragetext anfragetext = em.find(Anfragetext.class, anfragetextDto
				.getIId());
		if (anfragetext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateAnfragetext. Es gibt keinen AnfrageText mit der iid "
							+ anfragetextDto.getIId()
							+ "\nanfragetextDto.toString(): "
							+ anfragetextDto.toString());
		}

		setAnfragetextFromAnfragetextDto(anfragetext, anfragetextDto);
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// t);
		// }
	}

	public AnfragetextDto anfragetextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}

		AnfragetextDto textDto = null;

		// try {
		Anfragetext text = em.find(Anfragetext.class, iId);
		if (text == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfragetextfindbyPrimaryKey. Es gibt keine anfragetext mit der iid "
							+ iId);
		}

		textDto = assembleAnfragetextDto(text);
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, t);
		// }

		return textDto;
	}

	/**
	 * Einen anfragespezifischen Text auslesen. Ist der Text in der
	 * Kundensprache nicht verf&uuml;gbar, wird er in dieser angelegt.
	 * 
	 * @param cNrLocaleI
	 *            das gewuenschte Locale
	 * @param cNrMediaartI
	 *            die gewuenschte Mediaart, z.B. MediaFac.MEDIAART_KOPFTEXT
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AnfragetextDto der Anfragetext
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AnfragetextDto anfragetextFindByMandantLocaleCNr(String cNrLocaleI,
			String cNrMediaartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNrLocaleI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLocaleI == null"));
		}

		if (cNrMediaartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrMediaartI == null"));
		}

		AnfragetextDto anfrageTextDto = null;
		Anfragetext anfrageText = null;

		try {
			anfrageText = findAnfragetextImpl(
					cNrLocaleI, cNrMediaartI, theClientDto);
		} catch(NoResultException ex) {			
			createDefaultAnfragetext(cNrMediaartI, cNrLocaleI, theClientDto);
			
			anfrageText = findAnfragetextImpl(
						cNrLocaleI, cNrMediaartI, theClientDto);
		}

		anfrageTextDto = assembleAnfragetextDto(anfrageText);

		myLogger.exit(anfrageTextDto.toString());

		return anfrageTextDto;
	}

	private Anfragetext findAnfragetextImpl(
			String cNrLocaleI, String cNrMediaartI, TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("AnfragetextfindByMandantLocaleMediaartCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cNrLocaleI);
		query.setParameter(3, cNrMediaartI);

		return (Anfragetext) query.getSingleResult();
	}

	
	/**
	 * Einen anfragespezifischen Text in Konzerndatensprache anlegen.
	 * 
	 * @param cNrMediaartI
	 *            die gewuenschte Mediaart, z.B. MediaFac.MEDIAART_KOPFTEXT
	 * @param cNrLocaleI
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AnfragetextDto der Anfragetext
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AnfragetextDto createDefaultAnfragetext(String cNrMediaartI,
			String cNrLocaleI, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		AnfragetextDto oAnfragetextDto = new AnfragetextDto();
		oAnfragetextDto.setMediaartCNr(cNrMediaartI);
		oAnfragetextDto.setLocaleCNr(cNrLocaleI);
		oAnfragetextDto.setMandantCNr(theClientDto.getMandant());

		String cTextinhalt = null;

		if (cNrMediaartI.equals(MediaFac.MEDIAART_KOPFTEXT)) {
			cTextinhalt = AnfrageServiceFac.ANFRAGE_DEFAULT_KOPFTEXT;
		} else if (cNrMediaartI.equals(MediaFac.MEDIAART_FUSSTEXT)) {
			cTextinhalt = AnfrageServiceFac.ANFRAGE_DEFAULT_FUSSTEXT;
		}

		oAnfragetextDto.setXTextinhalt(cTextinhalt);
		oAnfragetextDto.setIId(createAnfragetext(oAnfragetextDto));

		return oAnfragetextDto;
	}

	private void checkAnfragetextDto(AnfragetextDto anfragetextDtoI)
			throws EJBExceptionLP {
		if (anfragetextDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("anfragetextDtoI == null"));
		}
	}

	private void setAnfragetextFromAnfragetextDto(Anfragetext anfragetext,
			AnfragetextDto anfragetextDto) {
		anfragetext.setMandantCNr(anfragetextDto.getMandantCNr());
		anfragetext.setLocaleCNr(anfragetextDto.getLocaleCNr());
		anfragetext.setMediaartCNr(anfragetextDto.getMediaartCNr());
		anfragetext.setXTextinhalt(anfragetextDto.getXTextinhalt());
		em.merge(anfragetext);
		em.flush();
	}

	private void setZertifikatartFromZertifikatartDto(
			Zertifikatart zertifikatart, ZertifikatartDto zertifikatartDto) {
		zertifikatart.setMandantCNr(zertifikatartDto.getMandantCNr());
		zertifikatart.setCBez(zertifikatartDto.getCBez());
		em.merge(zertifikatart);
		em.flush();
	}

	private ZertifikatartDto assembleZertifikatartDto(
			Zertifikatart zertifikatart) {
		return ZertifikatartDtoAssembler.createDto(zertifikatart);
	}

	private AnfragetextDto assembleAnfragetextDto(Anfragetext anfragetext) {
		return AnfragetextDtoAssembler.createDto(anfragetext);
	}

	private AnfragetextDto[] assembleAnfragetextDtos(Collection<?> anfragetexts) {
		List<AnfragetextDto> list = new ArrayList<AnfragetextDto>();
		if (anfragetexts != null) {
			Iterator<?> iterator = anfragetexts.iterator();
			while (iterator.hasNext()) {
				Anfragetext anfragetext = (Anfragetext) iterator.next();
				list.add(assembleAnfragetextDto(anfragetext));
			}
		}
		AnfragetextDto[] returnArray = new AnfragetextDto[list.size()];
		return (AnfragetextDto[]) list.toArray(returnArray);
	}

	// Anfrageart --------------------------------------------------------

	/**
	 * Eine neue Anfrageart anlegen.
	 * 
	 * @param anfrageartDtoI
	 *            die neue Anfrageart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String PK der neuen Anfrageart
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String createAnfrageart(AnfrageartDto anfrageartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageartDto(anfrageartDtoI);
		myLogger.logData(anfrageartDtoI);

		try {
			// zuerst die Anfrageart
			Anfrageart anfrageart = new Anfrageart(anfrageartDtoI.getCNr(),
					anfrageartDtoI.getISort());
			em.persist(anfrageart);
			em.flush();

			setAnfrageartFromAnfrageartDto(anfrageart, anfrageartDtoI);

			// dann die Spr
			if (anfrageartDtoI.getAnfrageartsprDto() != null) {
				Anfrageartspr anfrageartspr = new Anfrageartspr(anfrageartDtoI
						.getAnfrageartsprDto().getLocaleCNr(), anfrageartDtoI
						.getCNr(), anfrageartDtoI.getAnfrageartsprDto()
						.getCBez());
				em.persist(anfrageartspr);
				em.flush();

				setAnfrageartsprFromAnfrageartsprDto(anfrageartspr,
						anfrageartDtoI.getAnfrageartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return anfrageartDtoI.getCNr();
	}

	/**
	 * Eine bestehende Anfrageart loeschen.
	 * 
	 * @param cNrAnfrageartI
	 *            die bestehende Anfrageart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAnfrageart(String cNrAnfrageartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageartCNr(cNrAnfrageartI);

		// try {
		// zuerst alle Sprs loeschen
		Query query = em.createNamedQuery("AnfrageartsprfindByAnfrageartCNr");
		query.setParameter(1, cNrAnfrageartI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Anfrageartspr toRemove = em.find(Anfrageartspr.class, it.next());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAnfrageart. Eine Anfragespr konnte nicht entfernt werden: "
								+ toRemove);
			}
			em.remove(toRemove);
			em.flush();
		}

		// jetzt die Anfrageart loeschen
		Anfrageart toRemove = em.find(Anfrageart.class, cNrAnfrageartI);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAnfrageart. Es gibt keine Anfrageart mit cnr "
							+ cNrAnfrageartI);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	/**
	 * Eine bestehende Anfrageart aktualisieren.
	 * 
	 * @param anfrageartDtoI
	 *            die bestehende Anfrageart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrageart(AnfrageartDto anfrageartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageartDto(anfrageartDtoI);
		myLogger.logData(anfrageartDtoI);

		try {
			// erst die Anfrageart
			Anfrageart anfrageart = em.find(Anfrageart.class, anfrageartDtoI
					.getCNr());
			if (anfrageart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAnfrageart. Es gibt keine Anfrageart mit der cnr "
								+ anfrageartDtoI.getCNr()
								+ "\nanfrageArtDto.toString: "
								+ anfrageartDtoI.toString());
			}

			setAnfrageartFromAnfrageartDto(anfrageart, anfrageartDtoI);

			// jetzt die Spr
			AnfrageartsprDto anfrageartsprDto = anfrageartDtoI
					.getAnfrageartsprDto();
			anfrageartsprDto.setAnfrageartCNr(anfrageartDtoI.getCNr());

			if (anfrageartsprDto != null
					&& anfrageartsprDto.getAnfrageartCNr() != null) {

				AnfrageartsprPK anfrageartsprPK = new AnfrageartsprPK();
				anfrageartsprPK.setLocaleCNr(anfrageartsprDto.getLocaleCNr());
				anfrageartsprPK.setAnfrageartCNr(anfrageartsprDto
						.getAnfrageartCNr());

				Anfrageartspr anfrageartspr = em.find(Anfrageartspr.class,
						anfrageartsprPK);
				if (anfrageartspr == null) {
					Anfrageartspr anfrageartsprneu = new Anfrageartspr(
							anfrageartDtoI.getAnfrageartsprDto().getLocaleCNr(),
							anfrageartDtoI.getCNr(), anfrageartDtoI
									.getAnfrageartsprDto().getCBez());

					em.persist(anfrageartsprneu);
					em.flush();
				} else {
					setAnfrageartsprFromAnfrageartsprDto(anfrageartspr,
							anfrageartsprDto);
				}

			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public AnfrageartDto anfrageartFindByPrimaryKey(String cNrAnfrageartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageartCNr(cNrAnfrageartI);
		myLogger.logData(cNrAnfrageartI);

		AnfrageartDto anfrageartDto = null;

		// try {
		// zuerst die Anfrageart lesen
		Anfrageart anfrageart = em.find(Anfrageart.class, cNrAnfrageartI);
		if (anfrageart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfrageartfindbyPrimaryKey. Es gibt keine Anfrageart mit cnr "
							+ cNrAnfrageartI);
		}
		anfrageartDto = assembleAnfrageartDto(anfrageart);

		// jetzt die Spr
		AnfrageartsprDto anfrageartsprDto = null;

		try {
			AnfrageartsprPK anfrageartsprPK = new AnfrageartsprPK(theClientDto
					.getLocUiAsString(), cNrAnfrageartI);
			Anfrageartspr anfrageartspr = em.find(Anfrageartspr.class,
					anfrageartsprPK);
			if (anfrageartspr != null) {
				anfrageartsprDto = assembleAnfrageartsprDto(anfrageartspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		anfrageartDto.setAnfrageartsprDto(anfrageartsprDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return anfrageartDto;
	}

	private void checkAnfrageartDto(AnfrageartDto anfrageartDtoI)
			throws EJBExceptionLP {
		if (anfrageartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("anfrageartDtoI == null"));
		}
	}

	private void checkAnfrageartCNr(String cNrAnfrageartI)
			throws EJBExceptionLP {
		if (cNrAnfrageartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAnfrageartI == null"));
		}
	}

	private void setAnfrageartFromAnfrageartDto(Anfrageart anfrageart,
			AnfrageartDto anfrageartDto) {
		anfrageart.setISort(anfrageartDto.getISort());
		em.merge(anfrageart);
		em.flush();
	}

	private AnfrageartDto assembleAnfrageartDto(Anfrageart anfrageart) {
		return AnfrageartDtoAssembler.createDto(anfrageart);
	}

	private AnfrageartDto[] assembleAnfrageartDtos(Collection<?> anfragearts) {
		List<AnfrageartDto> list = new ArrayList<AnfrageartDto>();
		if (anfragearts != null) {
			Iterator<?> iterator = anfragearts.iterator();
			while (iterator.hasNext()) {
				Anfrageart anfrageart = (Anfrageart) iterator.next();
				list.add(assembleAnfrageartDto(anfrageart));
			}
		}
		AnfrageartDto[] returnArray = new AnfrageartDto[list.size()];
		return (AnfrageartDto[]) list.toArray(returnArray);
	}

	public void createAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws EJBExceptionLP {
		if (anfrageartsprDto == null) {
			return;
		}
		try {
			Anfrageartspr anfrageartspr = new Anfrageartspr(anfrageartsprDto
					.getLocaleCNr(), anfrageartsprDto.getAnfrageartCNr(),
					anfrageartsprDto.getCBez());
			em.persist(anfrageartspr);
			em.flush();
			setAnfrageartsprFromAnfrageartsprDto(anfrageartspr,
					anfrageartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAnfrageartspr(String localeCNr, String anfrageartCNr)
			throws EJBExceptionLP {
		AnfrageartsprPK anfrageartsprPK = new AnfrageartsprPK();
		anfrageartsprPK.setLocaleCNr(localeCNr);
		anfrageartsprPK.setAnfrageartCNr(anfrageartCNr);
		// try {
		Anfrageartspr toRemove = em.find(Anfrageartspr.class, anfrageartsprPK);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAnfrageartspr. Es gibt keine Anfrageartspr mit der locale "
							+ localeCNr + " und der anfrageartcnr "
							+ anfrageartCNr);
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

	public void removeAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws EJBExceptionLP {
		if (anfrageartsprDto != null) {
			String localeCNr = anfrageartsprDto.getLocaleCNr();
			String anfrageartCNr = anfrageartsprDto.getAnfrageartCNr();
			removeAnfrageartspr(localeCNr, anfrageartCNr);
		}
	}

	public void updateAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws EJBExceptionLP {
		if (anfrageartsprDto != null) {
			AnfrageartsprPK anfrageartsprPK = new AnfrageartsprPK();
			anfrageartsprPK.setLocaleCNr(anfrageartsprDto.getLocaleCNr());
			anfrageartsprPK.setAnfrageartCNr(anfrageartsprDto
					.getAnfrageartCNr());
			// try {
			Anfrageartspr anfrageartspr = em.find(Anfrageartspr.class,
					anfrageartsprPK);
			if (anfrageartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						new Exception(
								"Die Anfrageartsprache konnte nicht gefunden werden. LocaleCnr: "
										+ anfrageartsprDto.getLocaleCNr()
										+ " AnfrageartCnr: "
										+ anfrageartsprDto.getAnfrageartCNr()));
			}
			setAnfrageartsprFromAnfrageartsprDto(anfrageartspr,
					anfrageartsprDto);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
			// }
		}
	}

	public AnfrageartsprDto anfrageartsprFindByPrimaryKey(String localeCNr,
			String anfrageartCNr) throws EJBExceptionLP {
		// try {
		AnfrageartsprPK anfrageartsprPK = new AnfrageartsprPK();
		anfrageartsprPK.setLocaleCNr(localeCNr);
		anfrageartsprPK.setAnfrageartCNr(anfrageartCNr);
		Anfrageartspr anfrageartspr = em.find(Anfrageartspr.class,
				anfrageartsprPK);
		if (anfrageartspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception(
							"Es konnte keine Anfrageartspr gefunden werden f\u00FCr die Anfrageart "
									+ anfrageartCNr + " in der Loacale "
									+ localeCNr));
		}
		return assembleAnfrageartsprDto(anfrageartspr);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	private void setAnfrageartsprFromAnfrageartsprDto(
			Anfrageartspr anfrageartspr, AnfrageartsprDto anfrageartsprDto) {
		anfrageartspr.setCBez(anfrageartsprDto.getCBez());
		em.merge(anfrageartspr);
		em.flush();
	}

	private AnfrageartsprDto assembleAnfrageartsprDto(
			Anfrageartspr anfrageartspr) {
		return AnfrageartsprDtoAssembler.createDto(anfrageartspr);
	}

	private AnfrageartsprDto[] assembleAnfrageartsprDtos(
			Collection<?> anfrageartsprs) {
		List<AnfrageartsprDto> list = new ArrayList<AnfrageartsprDto>();
		if (anfrageartsprs != null) {
			Iterator<?> iterator = anfrageartsprs.iterator();
			while (iterator.hasNext()) {
				Anfrageartspr anfrageartspr = (Anfrageartspr) iterator.next();
				list.add(assembleAnfrageartsprDto(anfrageartspr));
			}
		}
		AnfrageartsprDto[] returnArray = new AnfrageartsprDto[list.size()];
		return (AnfrageartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Alle Anfragearten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            alternatives Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Map die Liste der Anfragearten
	 */
	public Map getAnfragearten(Locale locale1, Locale locale2,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		LinkedHashMap<String, String> resultMap = null;

		// try {
		Query query = em.createNamedQuery("AnfrageartfindAll");

		Collection<?> arten = query.getResultList();
		// if (arten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }

		AnfrageartDto[] artDtos = assembleAnfrageartDtos(arten);
		resultMap = uebersetzeAnfrageartenOptimal(artDtos, locale1, locale2);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

		return resultMap;
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von Anfragearten.
	 * 
	 * @param pArray
	 *            die Liste der Anfragearten
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            alternatives Locale
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return LinkedHashMap die Liste der uebersetzten Anfragearten
	 */
	private LinkedHashMap<String, String> uebersetzeAnfrageartenOptimal(
			AnfrageartDto[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		myLogger.entry();

		if (pArray == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pArray == null"));
		}

		LinkedHashMap<String, String> resultMap = new LinkedHashMap<String, String>(
				10);

		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeAnfrageartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			resultMap.put(key, value);
		}

		return resultMap;
	}

	/**
	 * Uebersetzt eine Anfrageart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            der Name der Anfrageart
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            Locale Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Anfrageart in der bestmoeglichen Uebersetzung
	 */
	private String uebersetzeAnfrageartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		myLogger.entry();

		String uebersetzung = "";

		try {
			uebersetzung = uebersetzeAnfrageart(locale1, cNr);
		} catch (Throwable t1) {
			try {
				uebersetzung = uebersetzeAnfrageart(locale2, cNr);
			} catch (Throwable t2) {
				uebersetzung = cNr;
			}
		}

		return uebersetzung;
	}

	/**
	 * Eine Anfrageart in eine bestimmte Sprache uebersetzen.
	 * 
	 * @param pLocale
	 *            die gewuenschte Sprache
	 * @param pArt
	 *            die Anfrageart
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return String die Uebersetzung
	 */
	private String uebersetzeAnfrageart(Locale pLocale, String pArt)
			throws EJBExceptionLP {
		myLogger.entry();

		Anfrageartspr spr = null;

		// try {
		String locale = Helper.locale2String(pLocale);

		Query query = em
				.createNamedQuery("AnfrageartsprfindByLocaleCNrAnfrageartCNr");
		query.setParameter(1, locale);
		query.setParameter(2, pArt);
		// @todo getSingleResult oder getResultList ?
		spr = (Anfrageartspr) query.getSingleResult();
		if (spr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei uebersetzeAnfrageart. Es gibt keine Anfrageart mit der locale "
							+ pLocale + " und der Art " + pArt);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return spr.getCBez();
	}
}
