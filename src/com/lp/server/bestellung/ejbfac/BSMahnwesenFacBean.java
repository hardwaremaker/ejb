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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.bestellung.ejb.Bsmahnlauf;
import com.lp.server.bestellung.ejb.Bsmahnstufe;
import com.lp.server.bestellung.ejb.BsmahnstufePK;
import com.lp.server.bestellung.ejb.Bsmahntext;
import com.lp.server.bestellung.ejb.Bsmahnung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionReport;
import com.lp.server.bestellung.fastlanereader.generated.FLRMahngruppe;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BSMahnlaufDtoAssembler;
import com.lp.server.bestellung.service.BSMahnstufeDto;
import com.lp.server.bestellung.service.BSMahnstufeDtoAssembler;
import com.lp.server.bestellung.service.BSMahntextDto;
import com.lp.server.bestellung.service.BSMahntextDtoAssembler;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BSMahnungDtoAssembler;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BSMahnwesenFacBean extends Facade implements BSMahnwesenFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public BSMahnungDto createBSMahnung(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();
		// precondition
		if (bSMahnungDto == null) {
			return null;
		}
		bSMahnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		bSMahnungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		bSMahnungDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BSMAHNUNG));

		try {
			Bsmahnung bSMahnung = new Bsmahnung(bSMahnungDto.getIId(),
					bSMahnungDto.getMandantCNr(),
					bSMahnungDto.getMahnlaufIId(),
					bSMahnungDto.getMahnstufeIId(),
					bSMahnungDto.getBestellungIId(),
					bSMahnungDto.getTMahndatum(),
					bSMahnungDto.getPersonalIIdAnlegen(),
					bSMahnungDto.getPersonalIIdAendern(),
					bSMahnungDto.getBestellpositionIId());
			em.persist(bSMahnung);
			em.flush();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			bSMahnungDto.setTAendern(t);
			bSMahnungDto.setTAnlegen(t);
			setBSMahnungFromBSMahnungDto(bSMahnung, bSMahnungDto);
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bSMahnungDto.getBestellungIId());
			bestellungDto.setIMahnstufeIId(bSMahnungDto.getMahnstufeIId());
			getBestellungFac().updateBestellungMahnstufe(
					bSMahnungDto.getBestellungIId(),
					bSMahnungDto.getMahnstufeIId(), theClientDto);
		} catch (Exception e) {
			if (e instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) e;
			} else {
				throw new EJBExceptionLP(e);
			}
		}
		return bSMahnungDto;
	}

	private void setBSMahnstufeInBestellung(Integer BestellungId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// Alle restlichen Mahnungen der Bestellung holen
		BSMahnungDto[] bsMahnungDto = bsmahnungFindByBestellungIId(
				BestellungId, theClientDto);
		BSMahnungDto neueMahnung = null;
		if (bsMahnungDto != null) {
			// Letzes Mahndatum suchen
			for (int i = 0; i < bsMahnungDto.length; i++) {
				if (neueMahnung == null) {
					neueMahnung = bsMahnungDto[i];
				} else {
					if (neueMahnung.getTMahndatum().before(
							bsMahnungDto[i].getTMahndatum())) {
						neueMahnung = bsMahnungDto[i];
					}
				}
			}
		}
		// Mahnstufe setzen
		BestellungDto bestellungDto = getBestellungFac()
				.bestellungFindByPrimaryKey(BestellungId);
		if (neueMahnung == null) {
			getBestellungFac().updateBestellungMahnstufe(
					bestellungDto.getIId(), null, theClientDto);
		} else {
			getBestellungFac().updateBestellungMahnstufe(
					bestellungDto.getIId(), neueMahnung.getMahnstufeIId(),
					theClientDto);
			bestellungDto.setIMahnstufeIId(neueMahnung.getMahnstufeIId());
		}
	}

	public void removeBSMahnung(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Bsmahnung toRemove = em.find(Bsmahnung.class, iId);
			Integer BestellungId = toRemove.getBestellungIId();
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			setBSMahnstufeInBestellung(BestellungId, theClientDto);

		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	/**
	 * methode wird dann aufgerufen wenn man gesamten Mahnlauf loescht
	 * 
	 * @param bSMahnungDto
	 *            BSMahnungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeBSMahnungAusMahnlauf(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (bSMahnungDto != null) {
			if (bSMahnungDto.getTGedruckt() != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
						"");
			}
			try {
				// SK: vorhandene remove funktion statt doppeltem code
				removeBSMahnung(bSMahnungDto, theClientDto);
				/*
				 * Integer iId = bSMahnungDto.getIId(); Bsmahnung toRemove =
				 * em.find(Bsmahnung.class, iId); if (toRemove == null) { throw
				 * new
				 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				 * null); } try { em.remove(toRemove); em.flush(); } catch
				 * (EntityExistsException er) { throw new
				 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er); }
				 */
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						ex);
			}
		}
	}

	public void removeBSMahnung(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (bSMahnungDto != null) {
			if (bSMahnungDto.getTGedruckt() != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
						"");
			} else {
				// SK: vorhandene remove funktion statt doppeltem code
				removeBSMahnung(bSMahnungDto.getIId(), theClientDto);
				// try {
				/*
				 * Bsmahnung toRemove = em.find(Bsmahnung.class, ); if (toRemove
				 * == null) { throw new
				 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				 * null); } try { em.remove(toRemove); em.flush(); } catch
				 * (EntityExistsException er) { throw new
				 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er); }
				 */
				// }
				// catch (RemoveException ex) {
				// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
				// ex);
				// }
			}
		}
	}

	public BSMahnungDto updateBSMahnung(BSMahnungDto bSMahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnungDto != null) {
			Integer iId = bSMahnungDto.getIId();
			try {
				Bsmahnung bSMahnung = em.find(Bsmahnung.class, iId);
				setBSMahnungFromBSMahnungDto(bSMahnung, bSMahnungDto);
				setBSMahnstufeInBestellung(bSMahnungDto.getBestellungIId(),
						theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(e);
			}
		}
		return bSMahnungDto;
	}

	public void updateBSMahnungs(BSMahnungDto[] bSMahnungDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnungDtos != null) {
			for (int i = 0; i < bSMahnungDtos.length; i++) {
				updateBSMahnung(bSMahnungDtos[i], theClientDto);
			}
		}
	}

	public BSMahnungDto bsmahnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Bsmahnung bsmahnung = em.find(Bsmahnung.class, iId);
			if (bsmahnung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBSMahnungDto(bsmahnung);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	private void setBSMahnungFromBSMahnungDto(Bsmahnung bsmahnung,
			BSMahnungDto bSMahnungDto) {
		bsmahnung.setMandantCNr(bSMahnungDto.getMandantCNr());
		bsmahnung.setBsmahnlaufIId(bSMahnungDto.getMahnlaufIId());
		bsmahnung.setMahnstufeIId(bSMahnungDto.getMahnstufeIId());
		bsmahnung.setBestellungIId(bSMahnungDto.getBestellungIId());
		bsmahnung.setTMahndatum(bSMahnungDto.getTMahndatum());
		bsmahnung.setTGedruckt(bSMahnungDto.getTGedruckt());
		bsmahnung.setPersonalIIdGedruckt(bSMahnungDto.getPersonalIIdGedruckt());
		bsmahnung.setTAnlegen(bSMahnungDto.getTAnlegen());
		bsmahnung.setTAendern(bSMahnungDto.getTAendern());
		bsmahnung.setPersonalIIdAnlegen(bSMahnungDto.getPersonalIIdAnlegen());
		bsmahnung.setPersonalIIdAendern(bSMahnungDto.getPersonalIIdAendern());
		bsmahnung.setNOffenemenge(bSMahnungDto.getNOffeneMenge());
		em.merge(bsmahnung);
		em.flush();
	}

	private BSMahnungDto assembleBSMahnungDto(Bsmahnung bSMahnung) {
		return BSMahnungDtoAssembler.createDto(bSMahnung);
	}

	private BSMahnungDto[] assembleBSMahnungDtos(Collection<?> bSMahnungs) {
		List<BSMahnungDto> list = new ArrayList<BSMahnungDto>();
		if (bSMahnungs != null) {
			Iterator<?> iterator = bSMahnungs.iterator();
			while (iterator.hasNext()) {
				Bsmahnung bSMahnung = (Bsmahnung) iterator.next();
				list.add(assembleBSMahnungDto(bSMahnung));
			}
		}
		BSMahnungDto[] returnArray = new BSMahnungDto[list.size()];
		return (BSMahnungDto[]) list.toArray(returnArray);
	}

	public Integer createBSMahnlauf(BSMahnlaufDto bSMahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			BSMahnlaufDto bsmahnlaufDto = new BSMahnlaufDto();
			bsmahnlaufDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_BSMAHNLAUF));
			bsmahnlaufDto.setMandantCNr(theClientDto.getMandant());
			bsmahnlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			bsmahnlaufDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			Bsmahnlauf bsmahnlauf = new Bsmahnlauf(bsmahnlaufDto.getIId(),
					bsmahnlaufDto.getMandantCNr(),
					bsmahnlaufDto.getPersonalIIdAnlegen(),
					bsmahnlaufDto.getPersonalIIdAendern());
			em.persist(bsmahnlauf);
			em.flush();
			bsmahnlaufDto.setTAendern(bsmahnlauf.getTAendern());
			bsmahnlaufDto.setTAnlegen(bsmahnlauf.getTAnlegen());
			setBSMahnlaufFromBSMahnlaufDto(bsmahnlauf, bsmahnlaufDto);
			return bsmahnlaufDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void removeBSMahnlauf(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Bsmahnlauf toRemove = em.find(Bsmahnlauf.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		BSMahnungDto[] bsmahnungen = bsmahnungFindByBSMahnlaufIId(
				toRemove.getIId(), theClientDto);
		for (int i = 0; i < bsmahnungen.length; i++) {
			if (bsmahnungen[i].getTGedruckt() != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
						"");
			}
		}
		try {
			for (int i = 0; i < bsmahnungen.length; i++) {
				context.getBusinessObject(BSMahnwesenFac.class)
						.removeBSMahnungAusMahnlauf(bsmahnungen[i],
								theClientDto);
			}
			context.getBusinessObject(BSMahnwesenFac.class).removeBSMahnlauf(
					iId);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		} catch (IllegalStateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void removeBSMahnlauf(BSMahnlaufDto bSMahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnlaufDto != null) {
			Integer iId = bSMahnlaufDto.getIId();
			// zuerst die angehaengten Mahnungen loeschen
			try {
				BSMahnungDto[] bsmahnungen = bsmahnungFindByBSMahnlaufIId(iId,
						theClientDto);
				for (int i = 0; i < bsmahnungen.length; i++) {
					if (bsmahnungen[i].getTGedruckt() != null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
								"");
					}
				}
				for (int i = 0; i < bsmahnungen.length; i++) {
					context.getBusinessObject(BSMahnwesenFac.class)
							.removeBSMahnungAusMahnlauf(bsmahnungen[i],
									theClientDto);
				}
				context.getBusinessObject(BSMahnwesenFac.class)
						.removeBSMahnlauf(iId);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			} catch (IllegalStateException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			}
		}
	}

	public void removeBSMahnlauf(Integer id) throws EJBExceptionLP {
		Bsmahnlauf toRemove = em.find(Bsmahnlauf.class, id);
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

	public void updateBSMahnlauf(BSMahnlaufDto bSMahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnlaufDto != null) {
			Integer iId = bSMahnlaufDto.getIId();
			try {
				Bsmahnlauf bSMahnlauf = em.find(Bsmahnlauf.class, iId);
				setBSMahnlaufFromBSMahnlaufDto(bSMahnlauf, bSMahnlaufDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(e);
			}
		}
	}

	public void updateBSMahnlaufs(BSMahnlaufDto[] bSMahnlaufDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnlaufDtos != null) {
			for (int i = 0; i < bSMahnlaufDtos.length; i++) {
				updateBSMahnlauf(bSMahnlaufDtos[i], theClientDto);
			}
		}
	}

	public BSMahnlaufDto bsmahnlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Bsmahnlauf bsmahnlauf = em.find(Bsmahnlauf.class, iId);
			if (bsmahnlauf == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBSMahnlaufDto(bsmahnlauf);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	private void setBSMahnlaufFromBSMahnlaufDto(Bsmahnlauf bSMahnlauf,
			BSMahnlaufDto bSMahnlaufDto) {
		bSMahnlauf.setMandantCNr(bSMahnlaufDto.getMandantCNr());
		bSMahnlauf.setPersonalIIdAnlegen(bSMahnlaufDto.getPersonalIIdAnlegen());
		bSMahnlauf.setPersonalIIdAendern(bSMahnlaufDto.getPersonalIIdAendern());
		bSMahnlauf.setTAendern(bSMahnlaufDto.getTAendern());
		bSMahnlauf.setTAnlegen(bSMahnlaufDto.getTAnlegen());
		em.merge(bSMahnlauf);
		em.flush();
	}

	private BSMahnlaufDto assembleBSMahnlaufDto(Bsmahnlauf bSMahnlauf) {
		return BSMahnlaufDtoAssembler.createDto(bSMahnlauf);
	}

	private BSMahnlaufDto[] assembleBSMahnlaufDtos(Collection<?> bSMahnlaufs) {
		List<BSMahnlaufDto> list = new ArrayList<BSMahnlaufDto>();
		if (bSMahnlaufs != null) {
			Iterator<?> iterator = bSMahnlaufs.iterator();
			while (iterator.hasNext()) {
				Bsmahnlauf bSMahnlauf = (Bsmahnlauf) iterator.next();
				list.add(assembleBSMahnlaufDto(bSMahnlauf));
			}
		}
		BSMahnlaufDto[] returnArray = new BSMahnlaufDto[list.size()];
		return (BSMahnlaufDto[]) list.toArray(returnArray);
	}

	public Integer createBSMahnstufe(BSMahnstufeDto bSMahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(bSMahnstufeDto);
		bSMahnstufeDto.setMandantCNr(theClientDto.getMandant());
		try {
			Bsmahnstufe bSMahnstufe = new Bsmahnstufe(bSMahnstufeDto.getIId(),
					bSMahnstufeDto.getMandantCNr(), bSMahnstufeDto.getITage());
			em.persist(bSMahnstufe);
			em.flush();
			setBSMahnstufeFromBSMahnstufeDto(bSMahnstufe, bSMahnstufeDto);
			return bSMahnstufeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeBSMahnstufe(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Bsmahnstufe toRemove = em.find(Bsmahnstufe.class, iId);
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

	public void removeBSMahnstufe(BSMahnstufeDto bSMahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (bSMahnstufeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bSMahnstufeDto == null"));
		}
		if (bSMahnstufeDto != null) {
			// try {
			Integer iId = bSMahnstufeDto.getIId();
			if (iId.intValue() == BSMahnwesenFac.MAHNSTUFE_1
					|| iId.intValue() == BSMahnwesenFac.MAHNSTUFE_2
					|| iId.intValue() == BSMahnwesenFac.MAHNSTUFE_3
					|| iId.intValue() == BSMahnwesenFac.MAHNSTUFE_0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
						new Exception("BSMahnstufe " + iId.intValue()
								+ " darf nicht geloescht werden"));
			}
			Bsmahnstufe toRemove = em.find(Bsmahnstufe.class,
					new BsmahnstufePK(iId, bSMahnstufeDto.getMandantCNr()));
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public void updateBSMahnstufe(BSMahnstufeDto bSMahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnstufeDto != null) {
			// try {
			Bsmahnstufe bSMahnstufe = em.find(
					Bsmahnstufe.class,
					new BsmahnstufePK(bSMahnstufeDto.getIId(), bSMahnstufeDto
							.getMandantCNr()));
			if (bSMahnstufe == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setBSMahnstufeFromBSMahnstufeDto(bSMahnstufe, bSMahnstufeDto);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
			// }
		}
	}

	public void updateBSMahnstufes(BSMahnstufeDto[] bSMahnstufeDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahnstufeDtos != null) {
			for (int i = 0; i < bSMahnstufeDtos.length; i++) {
				updateBSMahnstufe(bSMahnstufeDtos[i], theClientDto);
			}
		}
	}

	public BSMahnstufeDto bsmahnstufeFindByPrimaryKeyOhneExc(
			BsmahnstufePK bsmahnstufePK) throws EJBExceptionLP {
		// try {
		Bsmahnstufe bsmahnstufe = em.find(Bsmahnstufe.class, bsmahnstufePK);
		if (bsmahnstufe == null) {
			return null;
		}
		return assembleBSMahnstufeDto(bsmahnstufe);
		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	private void setBSMahnstufeFromBSMahnstufeDto(Bsmahnstufe bSMahnstufe,
			BSMahnstufeDto bSMahnstufeDto) {
		bSMahnstufe.setITage(bSMahnstufeDto.getITage());
		em.merge(bSMahnstufe);
		em.flush();
	}

	private BSMahnstufeDto assembleBSMahnstufeDto(Bsmahnstufe bSMahnstufe) {
		return BSMahnstufeDtoAssembler.createDto(bSMahnstufe);
	}

	private BSMahnstufeDto[] assembleBSMahnstufeDtos(Collection<?> bSMahnstufes) {
		List<BSMahnstufeDto> list = new ArrayList<BSMahnstufeDto>();
		if (bSMahnstufes != null) {
			Iterator<?> iterator = bSMahnstufes.iterator();
			while (iterator.hasNext()) {
				Bsmahnstufe bSMahnstufe = (Bsmahnstufe) iterator.next();
				list.add(assembleBSMahnstufeDto(bSMahnstufe));
			}
		}
		BSMahnstufeDto[] returnArray = new BSMahnstufeDto[list.size()];
		return (BSMahnstufeDto[]) list.toArray(returnArray);
	}

	public Integer createBSMahntext(BSMahntextDto bSMahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(bSMahntextDto);
		if (bSMahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bSMahntextDto == null"));
		}

		bSMahntextDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BSMAHNTEXT));
		bSMahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		bSMahntextDto.setMandantCNr(theClientDto.getMandant());
		// wenns dazu noch keine mahnstufe gibt muss ich die kreieren
		try {
			BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac()
					.bsmahnstufeFindByPrimaryKeyOhneExc(
							new BsmahnstufePK(bSMahntextDto.getMahnstufeIId(),
									bSMahntextDto.getMandantCNr()));
			if (bsmahnstufeDto == null) {
				bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(bSMahntextDto.getMahnstufeIId());
				/**
				 * @todo MB woher dieser Wert?
				 */
				bsmahnstufeDto.setITage(new Integer(10));
				getBSMahnwesenFac().createBSMahnstufe(bsmahnstufeDto,
						theClientDto);
			}
			Bsmahntext bsmahntext = new Bsmahntext(bSMahntextDto.getIId(),
					bSMahntextDto.getMandantCNr(),
					bSMahntextDto.getLocaleCNr(),
					bSMahntextDto.getMahnstufeIId(),
					bSMahntextDto.getXTextinhalt());
			em.persist(bsmahntext);
			em.flush();
			setBSMahntextFromBSMahntextDto(bsmahntext, bSMahntextDto);
			return bSMahntextDto.getIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeBSMahntext(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Bsmahntext toRemove = em.find(Bsmahntext.class, iId);
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

	public void removeBSMahntext(BSMahntextDto bSMahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(bSMahntextDto);
		if (bSMahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bSMahntextDto == null"));
		}
		// try {
		if (bSMahntextDto.getMahnstufeIId().intValue() == 1
				|| bSMahntextDto.getMahnstufeIId().intValue() == 2
				|| bSMahntextDto.getMahnstufeIId().intValue() == 3) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_MAHNTEXTE_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					new Exception("Mahntext "
							+ bSMahntextDto.getMahnstufeIId().intValue()
							+ " darf nicht geloescht werden"));
		}
		Integer iId = bSMahntextDto.getIId();
		Bsmahntext toRemove = em.find(Bsmahntext.class, iId);
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

	public void updateBSMahntext(BSMahntextDto bSMahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahntextDto != null) {
			Integer iId = bSMahntextDto.getIId();
			try {
				Bsmahntext bSMahntext = em.find(Bsmahntext.class, iId);
				setBSMahntextFromBSMahntextDto(bSMahntext, bSMahntextDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(e);
			}
		}
	}

	public void updateBSMahntexts(BSMahntextDto[] bSMahntextDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bSMahntextDtos != null) {
			for (int i = 0; i < bSMahntextDtos.length; i++) {
				updateBSMahntext(bSMahntextDtos[i], theClientDto);
			}
		}
	}

	public BSMahntextDto bsmahntextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Bsmahntext bsmahntext = em.find(Bsmahntext.class, iId);
			if (bsmahntext == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBSMahntextDto(bsmahntext);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	private void setBSMahntextFromBSMahntextDto(Bsmahntext bSMahntext,
			BSMahntextDto bSMahntextDto) {
		bSMahntext.setMandantCNr(bSMahntextDto.getMandantCNr());
		bSMahntext.setLocaleCNr(bSMahntextDto.getLocaleCNr());
		bSMahntext.setMahnstufeIId(bSMahntextDto.getMahnstufeIId());
		bSMahntext.setXTextinhalt(bSMahntextDto.getXTextinhalt());
		em.merge(bSMahntext);
		em.flush();
	}

	private BSMahntextDto assembleBSMahntextDto(Bsmahntext bSMahntext) {
		return BSMahntextDtoAssembler.createDto(bSMahntext);
	}

	private BSMahntextDto[] assembleBSMahntextDtos(Collection<?> bSMahntexts) {
		List<BSMahntextDto> list = new ArrayList<BSMahntextDto>();
		if (bSMahntexts != null) {
			Iterator<?> iterator = bSMahntexts.iterator();
			while (iterator.hasNext()) {
				Bsmahntext bSMahntext = (Bsmahntext) iterator.next();
				list.add(assembleBSMahntextDto(bSMahntext));
			}
		}
		BSMahntextDto[] returnArray = new BSMahntextDto[list.size()];
		return (BSMahntextDto[]) list.toArray(returnArray);
	}

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIId(Integer mahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// @ToDo Ungueltiger Prozeduraufruf oder ungueltiges Argument
		Query query = em.createNamedQuery("BSMahnungfindByBSMahnlaufIId");
		query.setParameter(1, mahnlaufIId);
		Collection cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return this.assembleBSMahnungDtos(cl);

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIIdLieferantIId(
			Integer mahnlaufIId, Integer lieferantIId, String MandantCNr)
			throws EJBExceptionLP {
		// @ToDo Ungueltiger Prozeduraufruf oder ungueltiges Argument
		Query query = em
				.createNamedQuery("BSMahnungfindBymahnlaufIIdLieferantIIdMandantCNr");
		query.setParameter(1, mahnlaufIId);
		query.setParameter(2, lieferantIId);
		query.setParameter(3, MandantCNr);
		Collection cl = query.getResultList();
		if (cl.isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return this.assembleBSMahnungDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public BSMahnstufeDto[] bsmahnstufeFindByMandantCNr(String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("BSMahnstufefindByMandantCNr");
			query.setParameter(1, mandantCNr);
			Collection<?> c = query.getResultList();
			Collection<Bsmahnstufe> c2 = new LinkedList<Bsmahnstufe>();
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Bsmahnstufe item = (Bsmahnstufe) iter.next();
				c2.add(item);
			}
			return assembleBSMahnstufeDtos(c2);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		}
	}

	public BSMahnlaufDto[] bsmahnlaufFindByMandantCNr(String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BSMahnlauffindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		return assembleBSMahnlaufDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public BSMahnungDto[] bsmahnungFindByBestellungIId(Integer bestellungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check(theClientDto);
		// try {
		Query query = em.createNamedQuery("BSMahnungfindByBestellungIId");
		query.setParameter(1, bestellungIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleBSMahnungDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public BSMahnungDto[] bsmahnungFindByBestellungBSMahnstufe(
			Integer bestellungIId, Integer mahnstufeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("BSMahnungfindByBestellungBSMahnstufe");
		query.setParameter(1, bestellungIId);
		query.setParameter(2, mahnstufeIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleBSMahnungDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LinkedHashMap getAllBSMahnstufen(String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LinkedHashMap<Integer, Integer> uMap = new LinkedHashMap<Integer, Integer>();
		try {
			BSMahnstufeDto[] bsmahnstufen = bsmahnstufeFindByMandantCNr(
					mandantCNr, theClientDto);
			for (int i = 0; i < bsmahnstufen.length; i++) {
				uMap.put(bsmahnstufen[i].getIId(), bsmahnstufen[i].getIId());
			}
			return uMap;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		}
	}

	public BSMahntextDto bsmahntextFindByMandantLocaleCNr(String mandantCNr,
			String localeCNr, Integer bsmahnstufeIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("BSMahntextfindByMandantLocaleCNr");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, localeCNr);
			query.setParameter(3, bsmahnstufeIId);
			Bsmahntext bsmahntext = (com.lp.server.bestellung.ejb.Bsmahntext) query
					.getSingleResult();

			return assembleBSMahntextDto(bsmahntext);

		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public BSMahntextDto createDefaultBSMahntext(Integer bsmahnstufeIId,
			String sTextinhaltI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BSMahntextDto oBSMahntextDto = new BSMahntextDto();
		BsmahnstufePK mahnstufePK = new BsmahnstufePK(bsmahnstufeIId,
				theClientDto.getMandant());
		BSMahnstufeDto bsMahnstufeDto = bsmahnstufeFindByPrimaryKeyOhneExc(mahnstufePK);
		oBSMahntextDto.setMahnstufeIId(bsmahnstufeIId);
		oBSMahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		oBSMahntextDto.setMandantCNr(theClientDto.getMandant());
		oBSMahntextDto.setXTextinhalt(sTextinhaltI);
		oBSMahntextDto.setIId(createBSMahntext(oBSMahntextDto, theClientDto));
		return oBSMahntextDto;
	}

	public Boolean bGibtEsEinenOffenenBSMahnlauf(String mandantCNr,
			TheClientDto theClientDto) {
		boolean bEsGibtEinenOffenen = false;
		BSMahnlaufDto[] bsmahnlaufDtos = bsmahnlaufFindByMandantCNr(mandantCNr,
				theClientDto);
		for (int i = 0; i < bsmahnlaufDtos.length; i++) {
			BSMahnungDto[] bsmahnungen = bsmahnungFindByBSMahnlaufIId(
					bsmahnlaufDtos[i].getIId(), theClientDto);
			for (int j = 0; j < bsmahnungen.length; j++) {
				if (bsmahnungen[j].getTGedruckt() == null) {
					bEsGibtEinenOffenen = true;
					break;
				}
			}
		}
		return new Boolean(bEsGibtEinenOffenen);
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes mahnen
	 * 
	 * @param bsmahnlaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void mahneBSMahnlaufRueckgaengig(Integer bsmahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BSMahnungDto[] bsmahnungen = bsmahnungFindByBSMahnlaufIId(
				bsmahnlaufIId, theClientDto);
		for (int i = 0; i < bsmahnungen.length; i++) {
			mahneBSMahnungRueckgaengig(bsmahnungen[i].getIId(), theClientDto);
		}
	}

	/**
	 * Eine Mahnung aus einem Mahnlauf durchfuehren.
	 * 
	 * @param mahnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void mahneBSMahnungRueckgaengig(Integer mahnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BSMahnungDto bsMahnungDto = bsmahnungFindByPrimaryKey(mahnungIId);
		bsMahnungDto.setTGedruckt(null);
		bsMahnungDto.setPersonalIIdGedruckt(null);
		try {
			context.getBusinessObject(BSMahnwesenFac.class).updateBSMahnung(
					bsMahnungDto, theClientDto);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		} catch (IllegalStateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	/**
	 * 
	 * @param theClientDto
	 *            String
	 * @return BSMahnlaufDto
	 * @throws EJBExceptionLP
	 */
	public BSMahnlaufDto createBSMahnlaufEchteLiefermahnungen(
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer bsmahnlaufIId = null;
		BSMahnlaufDto bsmahnlaufDto = null;
		BSMahnungDto bsmahnungDto[] = null;

		bsmahnlaufIId = createMahnungen(true, false, false, theClientDto);

		// wenn keine Mahnungen angelegt wurden dann wird auch wieder der
		// Mahnlauf entfernt
		bsmahnungDto = bsmahnungFindByBSMahnlaufIIdOhneExc(bsmahnlaufIId,
				theClientDto);
		if (bsmahnungDto.length == 0) {
			this.removeBSMahnlauf(bsmahnlaufIId, theClientDto);
		} else {
			bsmahnlaufDto = bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
		}
		return bsmahnlaufDto;
	}

	public BSMahnlaufDto createBSMahnlaufLiefererinnerung(
			TheClientDto theClientDto) {
		Integer bsmahnlaufIId = null;
		BSMahnlaufDto bsmahnlaufDto = null;
		BSMahnungDto bsmahnungDto[] = null;

		bsmahnlaufIId = createMahnungen(false, false, true, theClientDto);

		// wenn keine Mahnungen angelegt wurden dann wird auch wieder der
		// Mahnlauf entfernt
		bsmahnungDto = bsmahnungFindByBSMahnlaufIIdOhneExc(bsmahnlaufIId,
				theClientDto);
		if (bsmahnungDto.length == 0) {
			this.removeBSMahnlauf(bsmahnlaufIId, theClientDto);
		} else {
			bsmahnlaufDto = bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
		}
		return bsmahnlaufDto;
	}

	/**
	 * 
	 * @param theClientDto
	 *            String
	 * @return BSMahnlaufDto
	 * @throws EJBExceptionLP
	 */
	public BSMahnlaufDto createBSMahnlaufABMahnungen(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer bsmahnlaufIId = null;
		BSMahnlaufDto bsmahnlaufDto = null;
		BSMahnungDto bsmahnungDto[] = null;

		bsmahnlaufIId = createMahnungen(false, true, false, theClientDto);

		// wenn keine Mahnungen angelegt wurden dann wird auch wieder der
		// Mahnlauf entfernt
		bsmahnungDto = bsmahnungFindByBSMahnlaufIIdOhneExc(bsmahnlaufIId,
				theClientDto);
		if (bsmahnungDto.length == 0) {
			this.removeBSMahnlauf(bsmahnlaufIId, theClientDto);
		} else {
			bsmahnlaufDto = bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
		}
		return bsmahnlaufDto;
	}

	/**
	 * 
	 * @param theClientDto
	 *            String
	 * @return BSMahnlaufDto
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public BSMahnlaufDto createABMahnungenUndLieferMahnungenUndLiefererinnerungen(
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer bsmahnlaufIId = null;
		BSMahnlaufDto bsmahnlaufDto = null;
		BSMahnungDto bsmahnungDto[] = null;

		bsmahnlaufIId = createMahnungen(true, true, true, theClientDto);
		// wenn keine Mahnungen angelegt wurden dann wird auch wieder der
		// Mahnlauf entfernt
		bsmahnungDto = bsmahnungFindByBSMahnlaufIIdOhneExc(bsmahnlaufIId,
				theClientDto);
		if (bsmahnungDto.length == 0) {
			this.removeBSMahnlauf(bsmahnlaufIId, theClientDto);
		} else {
			bsmahnlaufDto = bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
		}
		return bsmahnlaufDto;
	}

	/**
	 * 
	 * @param bestellpositionIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BSMahnungDto
	 */
	public BSMahnungDto[] bsmahnungFindByBestellpositionIIdOhneExc(
			Integer bestellpositionIId, TheClientDto theClientDto) {
		Collection<?> bsmahnung = null;
		BSMahnungDto[] bsmahnungDto = null;
		// try {
		Query query = em.createNamedQuery("BSMahnungfindByBestellposition");
		query.setParameter(1, bestellpositionIId);

		bsmahnung = query.getResultList();
		// if (bsmahnung.isEmpty()) {
		// nothing
		// }
		bsmahnungDto = assembleBSMahnungDtos(bsmahnung);
		// }
		// catch (FinderException ex) {
		// nothing
		// }
		return bsmahnungDto;
	}

	public void mahneBSMahnung(Integer bsmahnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Bsmahnung bsmahnung = em.find(Bsmahnung.class, bsmahnungIId);
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bsmahnung.getBestellungIId());
			// nur dann mahnen, wenn die Bestellung keine mahnsperre hat oder
			// diese schon vorbei ist.
			if (bestellungDto.getTMahnsperreBis() == null
					|| bestellungDto.getTMahnsperreBis().before(getDate())) {
				// jetzt kann die Mahnung als gemahnt markiert werden
				bsmahnung.setTGedruckt(getTimestamp());
				bsmahnung.setPersonalIIdGedruckt(theClientDto.getIDPersonal());
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BESTELLUNG_MAHNSPERRE,
						new Exception("Die Bestellung "
								+ bestellungDto.getCNr()
								+ " darf nicht gemahnt werden: Mahnsperre bis "
								+ bestellungDto.getTMahnsperreBis()));
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public void mahneBSMahnlauf(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BSMahnungDto[] bsmahnungen = bsmahnungFindByBSMahnlaufIId(mahnlaufIId,
				theClientDto);
		for (int i = 0; i < bsmahnungen.length; i++) {
			mahneBSMahnung(bsmahnungen[i].getIId(), theClientDto);
		}
	}

	// **************************************************************************
	// **

	private BestellungDto[] assembleABundLieferterminBestellungDtos(
			BestellungDto bestellungDtoABTermine[],
			BestellungDto bestellungDtoLiefertermine[]) {
		ArrayList<BestellungDto> arlist = new ArrayList<BestellungDto>();

		for (int i = 0; i < bestellungDtoABTermine.length; i++) {
			arlist.add(bestellungDtoABTermine[i]);
		}

		for (int j = 0; j < bestellungDtoLiefertermine.length; j++) {
			arlist.add(bestellungDtoLiefertermine[j]);
		}
		BestellungDto[] returnArray = new BestellungDto[arlist.size()];
		return (BestellungDto[]) arlist.toArray(returnArray);
	}

	/**
	 * erstellt Liefermahnungen, ABMahnungen oder beides ruft dazu die Methoden
	 * echteLiefermahnungen() und (oder) echteABMahnungen() auf
	 * ------------------
	 * ----------------------------------------------------------
	 * 
	 * @param echteLiefermahnung
	 *            boolean
	 * @param echteABMahnungen
	 *            boolean
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 *         ----------------------------------------------------------
	 *         -----------------
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private Integer createMahnungen(boolean echteLiefermahnung,
			boolean echteABMahnungen, boolean liefererinnerung,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Session session = null;
		try {
			// Mahnstufen des Mandanten holen
			BSMahnstufeDto bsmahnstufeDto[] = this.bsmahnstufeFindByMandantCNr(
					theClientDto.getMandant(), theClientDto);
			if (bsmahnstufeDto == null || bsmahnstufeDto.length == 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN,
						"");
			}

			// mahnlaufIId generieren wird fuer Liefermahnung und ABMahnung
			// gebraucht
			Integer bsmahnlaufIId = null;
			BSMahnlaufDto bsmahnlaufDto = null;
			bsmahnlaufIId = context.getBusinessObject(BSMahnwesenFac.class)
					.createBSMahnlauf(bsmahnlaufDto, theClientDto);
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session
					.createCriteria(FLRBestellpositionReport.class);

			// folgende Positionsarten werden ignoriert.
			Collection<String> cPositionsarten = new LinkedList<String>();
			cPositionsarten.add(BestellpositionFac.BESTELLPOSITIONART_BETRIFFT);
			cPositionsarten
					.add(BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH);
			cPositionsarten
					.add(BestellpositionFac.BESTELLPOSITIONART_LEERZEILE);
			cPositionsarten
					.add(BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN);
			cPositionsarten
					.add(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE);
			crit.add(Restrictions.not(Restrictions
					.in(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
							cPositionsarten)));

			// PJ 16536
			String queryString = "SELECT artgru_i_id FROM FLRMahngruppe m"
					+ " WHERE m.flrartikelgruppe.mandant_c_nr ='"
					+ theClientDto.getMandant() + "'";

			Session session2=FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session2.createQuery(queryString);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			Integer[] ids = new Integer[results.size()];
			int row = 0;
			while (resultListIterator.hasNext()) {
				Integer artgruIId = (Integer) resultListIterator.next();
				ids[row] = artgruIId;
				row++;
			}
			session2.close();
			if (ids.length > 0) {
				crit.createAlias("flrartikel", "a");
				crit.createAlias("a.flrartikelgruppe", "ag");

				crit.add(Restrictions.in("ag.i_id", ids));
			}

			Criteria critBestellung = crit
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			// nach mandanten filtern
			critBestellung.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// PJ 15165
			critBestellung.add(Restrictions.or(Restrictions
					.isNull(BestellungFac.FLR_BESTELLUNG_T_MAHNSPERREBIS),
					Restrictions.lt(
							BestellungFac.FLR_BESTELLUNG_T_MAHNSPERREBIS,
							Helper.cutTimestamp(new Timestamp(System
									.currentTimeMillis())))));

			// nach Stati filtern
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
			cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
			// SK added BESTELLSTATUS_TEILERLEDIGT
			cStati.add(BestellungFac.BESTELLSTATUS_TEILERLEDIGT);
			critBestellung
					.add(Restrictions.in(
							BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
							cStati));
			List<?> list = crit.list();

			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRBestellpositionReport flrbespos = (FLRBestellpositionReport) iter
						.next();
				
				/**
				 * nur Liefermahnungen deren Status bestaetigt ist oder deren
				 * Liefertermin vor heute ist
				 */
				if (echteLiefermahnung == true
						&& !(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
								.equals(flrbespos.getFlrbestellung()
										.getBestellungart_c_nr()))) {
					BSMahnungDto[] bsmahnungdto = getBSMahnwesenFac()
							.bsmahnungFindByBestellpositionIIdOhneExc(
									flrbespos.getI_id(), theClientDto);
					if (bsmahnungdto.length > 0) {

						Integer latest = 0;
						for (int y = 0; y < bsmahnungdto.length; y++) {
							if (bsmahnungdto[y].getMahnstufeIId() > latest) {
								latest = bsmahnungdto[y].getMahnstufeIId();
							}
						}
						for (int i = 0; i < bsmahnstufeDto.length; i++) {
							if (latest.equals(bsmahnstufeDto[i].getIId())) {
								echteLiefermahnungen(flrbespos, bsmahnlaufIId,
										bsmahnstufeDto[i], theClientDto);
							}
						}
					} else {
						echteLiefermahnungen(flrbespos, bsmahnlaufIId, null,
								theClientDto);
					}
				}

				// lt. WH
				boolean bEchteLiefermahnungVorhanden = false;
				BSMahnungDto[] bsmahnungdto = getBSMahnwesenFac()
						.bsmahnungFindByBestellpositionIIdOhneExc(
								flrbespos.getI_id(), theClientDto);
				if (bsmahnungdto.length > 0) {
					for (int y = 0; y < bsmahnungdto.length; y++) {
						if (bsmahnungdto[y].getMahnstufeIId() > 0) {
							bEchteLiefermahnungVorhanden = true;
						}
					}
				}

				if (bEchteLiefermahnungVorhanden == false) {

					/**
					 * nur ABMahnungen deren Status offen ist ausser der
					 * Liefertermin ist vor heute dann ist es eine Liefermahnung
					 */
					if (echteABMahnungen == true) {
						// hier wird mahnstufe 0 uebergeben
						for (int i = 0; i < bsmahnstufeDto.length; i++) {
							if (bsmahnstufeDto[i].getIId().equals(
									new Integer(BSMahnwesenFac.MAHNSTUFE_0))) {
								echteABMahnungen(flrbespos, bsmahnstufeDto[i],
										bsmahnlaufIId, theClientDto);
							}
						}
					}

					// lt. WH
					boolean bABLiefermahnungVorhanden = false;
					bsmahnungdto = getBSMahnwesenFac()
							.bsmahnungFindByBestellpositionIIdOhneExc(
									flrbespos.getI_id(), theClientDto);
					if (bsmahnungdto.length > 0) {
						for (int y = 0; y < bsmahnungdto.length; y++) {
							if (bsmahnungdto[y].getMahnstufeIId() == 0) {
								bABLiefermahnungVorhanden = true;
							}
						}
					}

					if (bABLiefermahnungVorhanden == false) {

						// lt. WH Liefererinnerung ist wie echte
						// Liefermahnung,
						// jedoch
						// nur mit Mahnstufe -1
						if (liefererinnerung == true
								&& !(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
										.equals(flrbespos.getFlrbestellung()
												.getBestellungart_c_nr()))) {

							if (flrbespos.getT_lieferterminbestaetigt() == null) {

								// hier wird mahnstufe -1 uebergeben
								for (int i = 0; i < bsmahnstufeDto.length; i++) {
									if (bsmahnstufeDto[i]
											.getIId()
											.equals(new Integer(
													BSMahnwesenFac.MAHNSTUFE_MINUS1))) {

										liefererinnerungen(flrbespos,
												bsmahnlaufIId,
												bsmahnstufeDto[i], theClientDto);

									}
								}
							}
						}
					}
				}

			}
			return bsmahnlaufIId;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	/**
	 * diese Methode erstellt die echten Liefermahnungen
	 * 
	 * @param flrbespos
	 *            FLRBestellpositionReport
	 * @param bsmahnlaufIId
	 *            Integer
	 * @param bsmahnstufeDto
	 *            BSMahnstufeDto[]
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void echteLiefermahnungen(FLRBestellpositionReport flrbespos,
			Integer bsmahnlaufIId, BSMahnstufeDto bsmahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// wenn Bestellposition bestaetigt
			// wenn Bestellposition offen und uebersteuerterLiefertermin ist vor
			// heute dann
			// ist es eine Liefermahnung
			if (((flrbespos.getBestellpositionstatus_c_nr()
					.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT)) || (flrbespos
					.getBestellpositionstatus_c_nr()
					.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN))
					&& getDate().after(
							flrbespos.getT_uebersteuerterliefertermin()))) {
				BestellungDto bestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKeyOhneExc(
								flrbespos.getFlrbestellung().getI_id());
				anlegenVonEchterLiefermahnung(bestellungDto, bsmahnstufeDto,
						flrbespos, bsmahnlaufIId, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void liefererinnerungen(FLRBestellpositionReport flrbespos,
			Integer bsmahnlaufIId, BSMahnstufeDto bsmahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// wenn Bestellposition bestaetigt
			// wenn Bestellposition offen und uebersteuerterLiefertermin ist vor
			// heute dann
			// ist es eine Liefermahnung

			java.sql.Timestamp tTermin=flrbespos.getT_uebersteuerterliefertermin();
			
			if(flrbespos.getT_auftragsbestaetigungstermin()!=null){
				tTermin=flrbespos.getT_auftragsbestaetigungstermin();
			}
			
			if (((flrbespos.getBestellpositionstatus_c_nr()
					.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT))
					|| flrbespos.getBestellpositionstatus_c_nr().equals(
							BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN) || flrbespos
					.getBestellpositionstatus_c_nr()
					.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_TEILGELIEFERT))
					&& getDate()
							.after(Helper.addiereTageZuTimestamp(
									tTermin,
									bsmahnstufeDto.getITage()))) {
				BestellungDto bestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKeyOhneExc(
								flrbespos.getFlrbestellung().getI_id());
				anlegenVonLiefererinnerung(bestellungDto, bsmahnstufeDto,
						flrbespos, bsmahnlaufIId, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * hier werden Kriterien ueberprueft ob Liefermahnung neu angelegt usw...
	 * 
	 * @param bestellungDto
	 *            BestellungDto
	 * @param bsmahnstufeDto
	 *            BSMahnstufeDto
	 * @param flrbespos
	 *            FLRBestellpositionReport
	 * @param bsmahnlaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void anlegenVonEchterLiefermahnung(BestellungDto bestellungDto,
			BSMahnstufeDto bsmahnstufeDto, FLRBestellpositionReport flrbespos,
			Integer bsmahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (bestellungDto != null) {
			// wenn heute - letztesMahndatum kleiner als MahnstufeTage dann
			// mache nichts
			if (!(checkZeitraumVonLetzterMahnung(flrbespos, bsmahnstufeDto,
					theClientDto))) {
				// wenn liefertermin vor heute dann wird hier gemahnt
				Timestamp tLiefertermin = null;
				if (flrbespos.getT_auftragsbestaetigungstermin() != null) {
					// Wenn AB-Termin zieht immer der
					tLiefertermin = flrbespos
							.getT_auftragsbestaetigungstermin();
				} else {
					tLiefertermin = flrbespos.getT_uebersteuerterliefertermin();
				}
				if (getDate().after(tLiefertermin)) {
					// mahnen
					BSMahnungDto bsmahnungDto = null;
					BestellpositionDto besposDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									flrbespos.getI_id());
					bsmahnungDto = new BSMahnungDto();
					bsmahnungDto.setMandantCNr(theClientDto.getMandant());
					bsmahnungDto.setMahnlaufIId(bsmahnlaufIId);
					bsmahnungDto.setBestellungIId(bestellungDto.getIId());
					bsmahnungDto.setTMahndatum(getDate());
					bsmahnungDto.setPersonalIIdAnlegen(theClientDto
							.getIDPersonal());
					bsmahnungDto.setBestellpositionIId(besposDto.getIId());
					if (besposDto.getNOffeneMenge() == null) {
						// Offene Menge null = gesamt Menge ist offen
						bsmahnungDto.setNOffeneMenge(besposDto.getNMenge());
					} else {
						bsmahnungDto.setNOffeneMenge(besposDto
								.getNOffeneMenge());
					}
					// wenn mahnstufeDto null oder 0 dann wird erstmals gemahnt
					if ((bsmahnstufeDto == null)
							|| (bsmahnstufeDto.getIId().intValue() == 0)) {
						bsmahnungDto.setMahnstufeIId(new Integer(1));
					} else {
						int nextMahnstufe = bsmahnstufeDto.getIId() + 1;
						BsmahnstufePK mahnstufePK = new BsmahnstufePK(
								nextMahnstufe, theClientDto.getMandant());
						BSMahnstufeDto newBSMahnstufeDto = bsmahnstufeFindByPrimaryKeyOhneExc(mahnstufePK);
						// Hoechste Mahnstufe ueberschritten oder in Mahnstufe
						// 99
						if ((newBSMahnstufeDto == null)
								|| newBSMahnstufeDto.getIId().intValue() == BSMahnwesenFac.MAHNSTUFE_99) {
							bsmahnungDto
									.setMahnstufeIId(BSMahnwesenFac.MAHNSTUFE_99);
							// Wenn alte Mahnstufe 99 dann gleich auf gedruckt
							// setzen
							if (BSMahnwesenFac.MAHNSTUFE_99 == bsmahnstufeDto
									.getIId()) {
								if (bsmahnungDto.getTGedruckt() == null) {
									bsmahnungDto.setTGedruckt(getTimestamp());
								}
							}
						} else {
							bsmahnungDto.setMahnstufeIId(newBSMahnstufeDto
									.getIId());
						}
					}
					if (!(bsmahnungDto.getNOffeneMenge() == null)) {
						if (!(bsmahnungDto.getNOffeneMenge().intValue() < 1)) {
							context.getBusinessObject(BSMahnwesenFac.class)
									.createBSMahnung(bsmahnungDto, theClientDto);
						}
					}
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void anlegenVonLiefererinnerung(BestellungDto bestellungDto,
			BSMahnstufeDto bsmahnstufeDto, FLRBestellpositionReport flrbespos,
			Integer bsmahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (bestellungDto != null) {
			// mahnen
			BSMahnungDto bsmahnungDto = null;
			BestellpositionDto besposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(flrbespos.getI_id());
			bsmahnungDto = new BSMahnungDto();
			bsmahnungDto.setMandantCNr(theClientDto.getMandant());
			bsmahnungDto.setMahnlaufIId(bsmahnlaufIId);
			bsmahnungDto.setBestellungIId(bestellungDto.getIId());
			bsmahnungDto.setTMahndatum(getDate());
			bsmahnungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			bsmahnungDto.setBestellpositionIId(besposDto.getIId());
			if (besposDto.getNOffeneMenge() == null) {
				// Offene Menge null = gesamt Menge ist offen
				bsmahnungDto.setNOffeneMenge(besposDto.getNMenge());
			} else {
				bsmahnungDto.setNOffeneMenge(besposDto.getNOffeneMenge());
			}

			bsmahnungDto.setMahnstufeIId(BSMahnwesenFac.MAHNSTUFE_MINUS1);

			if (!(bsmahnungDto.getNOffeneMenge() == null)) {
				if (!(bsmahnungDto.getNOffeneMenge().intValue() < 1)) {
					context.getBusinessObject(BSMahnwesenFac.class)
							.createBSMahnung(bsmahnungDto, theClientDto);
				}
			}

		}
	}

	/**
	 * diese Methode erstellt die echte AB-Mahnungen
	 * 
	 * @param flrbespos
	 *            FLRBestellpositionReport
	 * @param bsmahnstufeDto
	 *            BSMahnstufeDto
	 * @param bsmahnlaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void echteABMahnungen(FLRBestellpositionReport flrbespos,
			BSMahnstufeDto bsmahnstufeDto, Integer bsmahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// wenn status offen und kein AB-Termin
		// dann ist es eine ABMahnung
		if (flrbespos.getBestellpositionstatus_c_nr().equals(
				BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN)
				&& (flrbespos.getT_auftragsbestaetigungstermin() == null)) {
			// Wenn vorher bereits Mahnstufe 1 angelegt dann keine AB Mahnung
			BSMahnungDto[] helper = getBSMahnwesenFac()
					.bsmahnungFindByBestellpositionIIdOhneExc(
							flrbespos.getI_id(), theClientDto);
			// PJ 14663 solange Mahnstufe 0, trotzdem mahnen
			int maxStufe = BSMahnwesenFac.MAHNSTUFE_0;
			for (int i = 0; i < helper.length; i++) {
				if (helper[i].getMahnstufeIId().intValue() != BSMahnwesenFac.MAHNSTUFE_0) {
					maxStufe = helper[i].getMahnstufeIId().intValue();
					break;
				}
			}
			if (helper.length == 0 || maxStufe == BSMahnwesenFac.MAHNSTUFE_0) {
				// wenn heute Minus Mahnstufe 0 Tage groeszer als Belegdatum dann
				// mahne AB
				Timestamp tPosTerminPlusMahnstufe = new Timestamp(
						flrbespos.getFlrbestellung().getT_belegdatum()
								.getTime()
								+ Helper.calculateDaysBackIntoMilliseconds(bsmahnstufeDto
										.getITage()));
				tPosTerminPlusMahnstufe = Helper
						.cutTimestamp(tPosTerminPlusMahnstufe);
				if (Helper.cutTimestamp(getTimestamp()).after(
						tPosTerminPlusMahnstufe)) {
					// mahnen
					BestellungDto bestellungDto = getBestellungFac()
							.bestellungFindByPrimaryKeyOhneExc(
									flrbespos.getFlrbestellung().getI_id());
					BestellpositionDto besposDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									flrbespos.getI_id());
					BSMahnungDto bsmahnungDto = null;
					bsmahnungDto = new BSMahnungDto();
					bsmahnungDto.setMandantCNr(theClientDto.getMandant());
					bsmahnungDto.setMahnlaufIId(bsmahnlaufIId);
					bsmahnungDto.setBestellungIId(bestellungDto.getIId());
					bsmahnungDto.setTMahndatum(getDate());
					bsmahnungDto.setPersonalIIdAnlegen(theClientDto
							.getIDPersonal());
					bsmahnungDto.setBestellpositionIId(besposDto.getIId());
					if (besposDto.getNOffeneMenge() == null) {
						// Wenn offene Menge null dann ist die gesamt Menge
						// offen
						bsmahnungDto.setNOffeneMenge(besposDto.getNMenge());
					} else {
						bsmahnungDto.setNOffeneMenge(besposDto
								.getNOffeneMenge());
					}
					// Mahnstufe bei AB-Mahnung ist 0
					bsmahnungDto.setMahnstufeIId(bsmahnstufeDto.getIId());
					if (!(bsmahnungDto.getNOffeneMenge() == null)) {
						if (!(bsmahnungDto.getNOffeneMenge().intValue() < 1)) {
							context.getBusinessObject(BSMahnwesenFac.class)
									.createBSMahnung(bsmahnungDto, theClientDto);
						}
					}
				}
			}
		}
	}

	/**
	 * ueberprueft ob die letzte Mahnung schon so lange zurueckliegt das man neu
	 * mahnen muss
	 * 
	 * @param bestellungDto
	 *            BestellungDto
	 * @param bsmahnstufeDto
	 *            BSMahnstufeDto
	 * @return boolean
	 */
	private boolean checkZeitraumVonLetzterMahnung(
			FLRBestellpositionReport flrbespos, BSMahnstufeDto bsmahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		boolean bool = false;
		// letztes Mahndatum holen
		BSMahnungDto[] bsmahnungdto = getBSMahnwesenFac()
				.bsmahnungFindByBestellpositionIIdOhneExc(flrbespos.getI_id(),
						theClientDto);
		if (bsmahnungdto.length > 0) {
			Date latest = bsmahnungdto[0].getTMahndatum();
			Integer iMahnungIId = bsmahnungdto[0].getIId();
			for (int y = 1; y < bsmahnungdto.length; y++) {
				if (bsmahnungdto[y].getTMahndatum().after(latest)) {
					latest = bsmahnungdto[y].getTMahndatum();
					iMahnungIId = bsmahnungdto[y].getIId();
				}
			}
			BSMahnungDto lastMahnung = getBSMahnwesenFac()
					.bsmahnungFindByPrimaryKey(iMahnungIId);
			// wenn heute - letztesMahndatum kleiner als MahnstufeTage und
			// wenn das letzteMahndatum nicht heute ist dann mache nichts
			if (getDate().equals(lastMahnung.getTMahndatum())) {
				// Wenn heute schon gemahnt nicht nochmal
				bool = true;
			} else {
				if (lastMahnung.getTMahndatum() != null
						&& lastMahnung.getMahnstufeIId() != null
						&& ((getDate().getTime() - lastMahnung.getTMahndatum()
								.getTime()) < Helper
								.calculateDaysBackIntoMilliseconds(bsmahnstufeDto
										.getITage()))
						&& !(lastMahnung.getTMahndatum().equals(getDate()))) {
					// do nothing
					bool = true;
				}
			}
		} else {
			// Bisher nicht gemahnt
			bsmahnstufeDto = getBSMahnwesenFac()
					.bsmahnstufeFindByPrimaryKeyOhneExc(
							new BsmahnstufePK(BSMahnwesenFac.MAHNSTUFE_1,
									theClientDto.getMandant()));
			Timestamp tMaxPosTermin = new Timestamp(getDate().getTime()
					- Helper.calculateDaysBackIntoMilliseconds(bsmahnstufeDto
							.getITage()));
			Timestamp tHelper = null;
			if (flrbespos.getT_auftragsbestaetigungstermin() == null) {
				tHelper = flrbespos.getT_uebersteuerterliefertermin();
			} else {
				tHelper = flrbespos.getT_auftragsbestaetigungstermin();
			}
			if (tHelper.after(tMaxPosTermin)) {
				// nicht mahnen
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * liefert das datum vom ABTermin oder sonst vom uebersteuertenLiefertermin
	 * der Bestellposition
	 * 
	 * @param flrbespos
	 *            FLRBestellpositionReport
	 * @return Date
	 */
	private Date getRichtigesMahndatumFuerABMahnung(
			FLRBestellpositionReport flrbespos) {
		// datum vom Beleg oder sonst Datum vom bestaetigen Liefertermin
		java.sql.Date date = null;
		if (flrbespos.getT_auftragsbestaetigungstermin() == null) {
			date = new Date(flrbespos.getT_uebersteuerterliefertermin()
					.getTime());
		} else {
			date = new Date(flrbespos.getT_auftragsbestaetigungstermin()
					.getTime());
		}
		return date;
	}

	public BSMahnungDto[] bsmahnungFindByBSMahnlaufIIdOhneExc(
			Integer mahnlaufIId, TheClientDto theClientDto) {
		BSMahnungDto bsmahnungDto[] = null;
		// try {
		Query query = em.createNamedQuery("BSMahnungfindByBSMahnlaufIId");
		query.setParameter(1, mahnlaufIId);
		// @todo getSingleResult oder getResultList ?
		bsmahnungDto = assembleBSMahnungDtos(query.getResultList());
		// if (bsmahnungDto.isEmpty()) {
		// nothing
		// }
		// }
		// catch (FinderException ex) {
		// nothing
		// }
		return bsmahnungDto;
	}
}
