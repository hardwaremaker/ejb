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
package com.lp.server.fertigung.ejbfac;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.lp.server.fertigung.ejb.Losbereich;
import com.lp.server.fertigung.ejb.Losklasse;
import com.lp.server.fertigung.ejb.Losklassespr;
import com.lp.server.fertigung.ejb.LosklassesprPK;
import com.lp.server.fertigung.ejb.Losstatus;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.LosbereichDto;
import com.lp.server.fertigung.service.LosbereichDtoAssembler;
import com.lp.server.fertigung.service.LosklasseDto;
import com.lp.server.fertigung.service.LosklasseDtoAssembler;
import com.lp.server.fertigung.service.LosklassesprDto;
import com.lp.server.fertigung.service.LosklassesprDtoAssembler;
import com.lp.server.fertigung.service.LosstatusDto;
import com.lp.server.fertigung.service.LosstatusDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FertigungServiceFacBean extends Facade implements
		FertigungServiceFac {
	@PersistenceContext
	private EntityManager em;

	public void createLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losstatusDto);
		// code begin
		try {
			Losstatus losstatus = new Losstatus(losstatusDto.getStatusCNr(),
					losstatusDto.getISort());
			em.persist(losstatus);
			em.flush();
			setLosstatusFromLosstatusDto(losstatus, losstatusDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losstatusDto);
		// code begin
		try {
			if (losstatusDto != null) {
				String statusCNr = losstatusDto.getStatusCNr();
				Losstatus toRemove = em.find(Losstatus.class, statusCNr);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losstatusDto);
		// code begin
		if (losstatusDto != null) {
			String statusCNr = losstatusDto.getStatusCNr();
			try {
				Losstatus losstatus = em.find(Losstatus.class, statusCNr);
				setLosstatusFromLosstatusDto(losstatus, losstatusDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public LosstatusDto losstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		// code begin
		// try {
		Losstatus losstatus = em.find(Losstatus.class, statusCNr);
		if (losstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLosstatusDto(losstatus);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setLosstatusFromLosstatusDto(Losstatus losstatus,
			LosstatusDto losstatusDto) {
		losstatus.setISort(losstatusDto.getISort());
		em.merge(losstatus);
		em.flush();
	}

	private LosstatusDto assembleLosstatusDto(Losstatus losstatus) {
		return LosstatusDtoAssembler.createDto(losstatus);
	}

	private LosstatusDto[] assembleLosstatusDtos(Collection<?> losstatuss) {
		List<LosstatusDto> list = new ArrayList<LosstatusDto>();
		if (losstatuss != null) {
			Iterator<?> iterator = losstatuss.iterator();
			while (iterator.hasNext()) {
				Losstatus losstatus = (Losstatus) iterator.next();
				list.add(assembleLosstatusDto(losstatus));
			}
		}
		LosstatusDto[] returnArray = new LosstatusDto[list.size()];
		return (LosstatusDto[]) list.toArray(returnArray);
	}

	public Integer createLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklasseDto);
		// code begin
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSKLASSE);
		losklasseDto.setIId(iId);
		losklasseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		losklasseDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Losklasse losklasse = new Losklasse(losklasseDto.getIId(),
					losklasseDto.getCNr(),
					losklasseDto.getPersonalIIdAnlegen(),
					losklasseDto.getPersonalIIdAendern());
			em.persist(losklasse);
			em.flush();
			losklasseDto.setTAendern(losklasse.getTAendern());
			losklasseDto.setTAnlegen(losklasse.getTAnlegen());
			setLosklasseFromLosklasseDto(losklasse, losklasseDto);
			// sprache
			if (losklasseDto.getLosklassesprDto() != null) {
				losklasseDto.getLosklassesprDto().setLosklasseIId(iId);
				updateLosklassespr(losklasseDto.getLosklassesprDto(),
						theClientDto);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return iId;
	}

	public Integer createLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto) {
		// log

		Losbereich losbereichVorhanden = em.find(Losbereich.class, losbereichDto.getIId());

		if (losbereichVorhanden == null) {

			try {
				Losbereich losbereich = new Losbereich(losbereichDto.getIId(),
						losbereichDto.getCBez());
				em.persist(losbereich);
				em.flush();

				setLosbereichFromLosbereichDto(losbereich, losbereichDto);

			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSBEREICH.I_ID"));
		}
		return losbereichDto.getIId();
	}

	public void removeLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklasseDto);
		// code begin
		try {
			if (losklasseDto != null) {
				Integer iId = losklasseDto.getIId();
				Losklasse toRemove = em.find(Losklasse.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto) {
		try {
			if (losbereichDto != null) {
				Integer iId = losbereichDto.getIId();
				Losbereich toRemove = em.find(Losbereich.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklasseDto);
		// code begin
		if (losklasseDto != null) {
			Integer iId = losklasseDto.getIId();
			losklasseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			losklasseDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			try {
				Losklasse losklasse = em.find(Losklasse.class, iId);
				setLosklasseFromLosklasseDto(losklasse, losklasseDto);
				// sprache
				if (losklasseDto.getLosklassesprDto() != null) {
					losklasseDto.getLosklassesprDto().setLosklasseIId(
							losklasseDto.getIId());
					updateLosklassespr(losklasseDto.getLosklassesprDto(),
							theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto) {
		if (losbereichDto != null) {
			Integer iId = losbereichDto.getIId();
			try {
				Losbereich losbereich = em.find(Losbereich.class, iId);
				setLosbereichFromLosbereichDto(losbereich, losbereichDto);

			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public LosklasseDto losklasseFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(iId);
		// code begin
		try {
			Losklasse losklasse = em.find(Losklasse.class, iId);
			if (losklasse == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LosklasseDto losklasseDto = assembleLosklasseDto(losklasse);
			String s = uebersetzeLosklasse(iId, theClientDto.getLocUi());
			if (s != null) {
				losklasseDto.setLosklassesprDto(new LosklassesprDto());
				losklasseDto.getLosklassesprDto().setLosklasseIId(iId);
				losklasseDto.getLosklassesprDto().setLocaleCNr(
						theClientDto.getLocUiAsString());
				losklasseDto.getLosklassesprDto().setCBez(s);
			}
			return losklasseDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public LosbereichDto losbereichFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		try {
			Losbereich losbereich = em.find(Losbereich.class, iId);
			if (losbereich == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LosbereichDto losbereichDto = assembleLosbereichDto(losbereich);

			return losbereichDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setLosklasseFromLosklasseDto(Losklasse losklasse,
			LosklasseDto losklasseDto) {
		losklasse.setCNr(losklasseDto.getCNr());
		losklasse.setTAnlegen(losklasseDto.getTAnlegen());
		losklasse.setPersonalIIdAnlegen(losklasseDto.getPersonalIIdAnlegen());
		losklasse.setTAendern(losklasseDto.getTAendern());
		losklasse.setPersonalIIdAendern(losklasseDto.getPersonalIIdAendern());
		em.merge(losklasse);
		em.flush();
	}

	private void setLosbereichFromLosbereichDto(Losbereich losbereich,
			LosbereichDto losbereichDto) {
		losbereich.setIId(losbereichDto.getIId());
		losbereich.setCBez(losbereichDto.getCBez());
		em.merge(losbereich);
		em.flush();
	}

	private LosbereichDto assembleLosbereichDto(Losbereich losbereich) {
		return LosbereichDtoAssembler.createDto(losbereich);
	}

	private LosklasseDto assembleLosklasseDto(Losklasse losklasse) {
		return LosklasseDtoAssembler.createDto(losklasse);
	}

	private LosklasseDto[] assembleLosklasseDtos(Collection<?> losklasses) {
		List<LosklasseDto> list = new ArrayList<LosklasseDto>();
		if (losklasses != null) {
			Iterator<?> iterator = losklasses.iterator();
			while (iterator.hasNext()) {
				Losklasse losklasse = (Losklasse) iterator.next();
				list.add(assembleLosklasseDto(losklasse));
			}
		}
		LosklasseDto[] returnArray = new LosklasseDto[list.size()];
		return (LosklasseDto[]) list.toArray(returnArray);
	}

	private void createLosklassespr(LosklassesprDto losklassesprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklassesprDto);
		// code begin
		losklassesprDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		losklassesprDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Losklassespr losklassespr = new Losklassespr(
					losklassesprDto.getLosklasseIId(),
					losklassesprDto.getLocaleCNr(),
					losklassesprDto.getPersonalIIdAnlegen(),
					losklassesprDto.getPersonalIIdAendern());
			em.persist(losklassespr);
			em.flush();
			losklassesprDto.setTAendern(losklassespr.getTAendern());
			losklassesprDto.setTAnlegen(losklassespr.getTAnlegen());
			setLosklassesprFromLosklassesprDto(losklassespr, losklassesprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void removeLosklassespr(LosklassesprDto losklassesprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklassesprDto);
		// code begin
		// try {
		if (losklassesprDto != null) {
			Integer losklasseIId = losklassesprDto.getLosklasseIId();
			String localeCNr = losklassesprDto.getLocaleCNr();
			Losklassespr toRemove = em.find(Losklassespr.class,
					new LosklassesprPK(losklasseIId, localeCNr));
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
		}
		// }
		// catch (Exception ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	private void updateLosklassespr(LosklassesprDto losklassesprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(losklassesprDto);
		// code begin
		if (losklassesprDto != null) {
			Integer iId = losklassesprDto.getLosklasseIId();
			String localeCNr = losklassesprDto.getLocaleCNr();
			losklassesprDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			losklassesprDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));
			try {
				LosklassesprPK pk = new LosklassesprPK(iId, localeCNr);
				Losklassespr losklassespr = em.find(Losklassespr.class, pk);
				if (losklassespr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createLosklassespr(losklassesprDto, theClientDto);
				} else {
					setLosklassesprFromLosklassesprDto(losklassespr,
							losklassesprDto);
				}

			} catch (NoResultException ex) {
				// diese Uebersetzung gibt es nocht nicht
				createLosklassespr(losklassesprDto, theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public LosklassesprDto losklassesprFindByPrimaryKey(Integer losklasseIId,
			String localeCNr) throws EJBExceptionLP {
		// try {
		LosklassesprPK losklassesprPK = new LosklassesprPK();
		losklassesprPK.setLosklasseIId(losklasseIId);
		losklassesprPK.setLocaleCNr(localeCNr);
		Losklassespr losklassespr = em.find(Losklassespr.class, losklassesprPK);
		if (losklassespr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return assembleLosklassesprDto(losklassespr);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setLosklassesprFromLosklassesprDto(Losklassespr losklassespr,
			LosklassesprDto losklassesprDto) {
		losklassespr.setCBez(losklassesprDto.getCBez());
		losklassespr.setTAnlegen(losklassesprDto.getTAnlegen());
		losklassespr.setPersonalIIdAnlegen(losklassesprDto
				.getPersonalIIdAnlegen());
		losklassespr.setTAendern(losklassesprDto.getTAendern());
		losklassespr.setPersonalIIdAendern(losklassesprDto
				.getPersonalIIdAendern());
		em.merge(losklassespr);
		em.flush();
	}

	private LosklassesprDto assembleLosklassesprDto(Losklassespr losklassespr) {
		return LosklassesprDtoAssembler.createDto(losklassespr);
	}

	private LosklassesprDto[] assembleLosklassesprDtos(
			Collection<?> losklassesprs) {
		List<LosklassesprDto> list = new ArrayList<LosklassesprDto>();
		if (losklassesprs != null) {
			Iterator<?> iterator = losklassesprs.iterator();
			while (iterator.hasNext()) {
				Losklassespr losklassespr = (Losklassespr) iterator.next();
				list.add(assembleLosklassesprDto(losklassespr));
			}
		}
		LosklassesprDto[] returnArray = new LosklassesprDto[list.size()];
		return (LosklassesprDto[]) list.toArray(returnArray);
	}

	/**
	 * Uebersetzt eine Losklasse optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param iId
	 *            Integer
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public String uebersetzeLosklasseOptimal(Integer iId, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		// try {
		String losklassspr = uebersetzeLosklasse(iId, locale1);
		if (losklassspr != null) {
			return losklassspr;
		}
		// }
		// catch (FinderException ex) {
		// try {
		return uebersetzeLosklasse(iId, locale2);
		// }
		// catch (FinderException ex1) {
		// return null;
		// }
		// try {
		// return uebersetzeLosklasse(iId, locale2);
		// }
		// catch (FinderException ex1) {
		// return null;
		// }
		// }
	}

	/**
	 * Uebersetzt eine Losklasse in die Sprache des uebergebenen Locales.
	 * 
	 * @param iId
	 *            Integer
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @throws EJBExceptionLP
	 * @return String
	 */
	private String uebersetzeLosklasse(Integer iId, Locale locale)
			throws EJBExceptionLP {
		String cLocale = null;
		try {
			cLocale = Helper.locale2String(locale);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		// try {
		Losklassespr losklassespr = em.find(Losklassespr.class,
				new LosklassesprPK(iId, cLocale));
		if (losklassespr == null) {
			return null;
		}
		return losklassespr.getCBez();
		// }
		// catch (FinderException ex1) {
		// Uebersetzung mit diesem Locale nicht vorhanden
		// return null;
		// }
	}

}
