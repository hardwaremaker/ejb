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
package com.lp.server.eingangsrechnung.ejbfac;

import java.util.*;

import com.lp.server.eingangsrechnung.ejb.*;
import com.lp.server.eingangsrechnung.service.*;
import com.lp.server.system.service.*;
import com.lp.server.util.*;
import com.lp.util.*;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EingangsrechnungServiceFacBean extends Facade implements
		EingangsrechnungServiceFac {
	@PersistenceContext
	private EntityManager em;

	public void createEingangsrechnungart(EingangsrechnungartDto erartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Eingangsrechnungart eingangsrechnungart = new Eingangsrechnungart(
					erartDto.getCNr());
			em.persist(eingangsrechnungart);
			em.flush();
			if (erartDto.getEingangsrechnungartsprDto() != null) {
				// die uebersetzung nur anlegen, wenn auch eine bezeichnung da
				// ist
				if (erartDto.getEingangsrechnungartsprDto().getCBez() != null) {
					erartDto.getEingangsrechnungartsprDto()
							.setEingangsrechnungartCNr(erartDto.getCNr());
					createEingangsrechnungartspr(
							erartDto.getEingangsrechnungartsprDto(),
							theClientDto);
				}
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void createEingangsrechnungartspr(
			EingangsrechnungartsprDto eingangsrechnungartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungartsprDto == null) {
			return;
		}
		try {
			Eingangsrechnungartspr eingangsrechnungartspr = new Eingangsrechnungartspr(
					eingangsrechnungartsprDto.getEingangsrechnungartCNr(),
					eingangsrechnungartsprDto.getLocaleCNr(),
					eingangsrechnungartsprDto.getCBez());
			em.persist(eingangsrechnungartspr);
			em.flush();
			setEingangsrechnungartsprFromEingangsrechnungartsprDto(
					eingangsrechnungartspr, eingangsrechnungartsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateEingangsrechnungart(
			EingangsrechnungartDto eingangsrechnungartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(eingangsrechnungartDto);
		// code begin
		if (eingangsrechnungartDto != null) {
			String cNr = eingangsrechnungartDto.getCNr();
			try {
				Eingangsrechnungart eingangsrechnungart = em.find(
						Eingangsrechnungart.class, cNr);
				setEingangsrechnungartFromEingangsrechnungartDto(
						eingangsrechnungart, eingangsrechnungartDto);
				// sprache
				if (eingangsrechnungartDto.getEingangsrechnungartsprDto() != null) {
					eingangsrechnungartDto.getEingangsrechnungartsprDto()
							.setEingangsrechnungartCNr(
									eingangsrechnungartDto.getCNr());
					eingangsrechnungartDto.getEingangsrechnungartsprDto()
							.setLocaleCNr(theClientDto.getLocUiAsString());
					updateEingangsrechnungartspr(
							eingangsrechnungartDto
									.getEingangsrechnungartsprDto(),
							theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void setEingangsrechnungartFromEingangsrechnungartDto(
			Eingangsrechnungart eingangsrechnungart,
			EingangsrechnungartDto eingangsrechnungartDto) {
		em.merge(eingangsrechnungart);
		em.flush();
	}

	private void updateEingangsrechnungartspr(
			EingangsrechnungartsprDto eingangsrechnungartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(eingangsrechnungartsprDto);
		// code begin
		if (eingangsrechnungartsprDto != null) {
			String cNr = eingangsrechnungartsprDto.getEingangsrechnungartCNr();
			String localeCNr = eingangsrechnungartsprDto.getLocaleCNr();
			try {
				EingangsrechnungartsprPK pk = new EingangsrechnungartsprPK(cNr,
						localeCNr);
				Eingangsrechnungartspr uvaartspr = em.find(
						Eingangsrechnungartspr.class, pk);
				if (uvaartspr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createEingangsrechnungartspr(eingangsrechnungartsprDto,
							theClientDto);
				} else {
					setEingangsrechnungartsprFromEingangsrechnungartsprDto(
							uvaartspr, eingangsrechnungartsprDto);
				}
				// }
				// catch (FinderException ex) {
				// diese Uebersetzung gibt es nocht nicht
				// createEingangsrechnungartspr(eingangsrechnungartsprDto,
				// theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void setEingangsrechnungartsprFromEingangsrechnungartsprDto(
			Eingangsrechnungartspr eingangsrechnungartspr,
			EingangsrechnungartsprDto eingangsrechnungartsprDto) {
		eingangsrechnungartspr.setCBez(eingangsrechnungartsprDto.getCBez());
		em.merge(eingangsrechnungartspr);
		em.flush();
	}

	public EingangsrechnungartDto eingangsrechnungartFindByPrimaryKey(
			String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Eingangsrechnungart eingangsrechnungart = em.find(
					Eingangsrechnungart.class, cNr);
			if (eingangsrechnungart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			EingangsrechnungartDto eingangsrechnungartDto = assembleEingangsrechnungartDto(eingangsrechnungart);
			eingangsrechnungartDto
					.setEingangsrechnungartsprDto(new EingangsrechnungartsprDto());
			eingangsrechnungartDto.getEingangsrechnungartsprDto()
					.setEingangsrechnungartCNr(cNr);
			eingangsrechnungartDto.getEingangsrechnungartsprDto().setLocaleCNr(
					theClientDto.getLocUiAsString());
			eingangsrechnungartDto.getEingangsrechnungartsprDto()
					.setCBez(
							uebersetzeEingangsrechnungart(cNr,
									theClientDto.getLocUi()));
			return eingangsrechnungartDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public EingangsrechnungartDto[] eingangsrechnungartFindAll()
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("EingangsrechnungartfindAll");
		Collection<?> eingangsrechnungart = query.getResultList();
		// if(eingangsrechnungart.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		// try {
		return assembleEingangsrechnungartDtos(eingangsrechnungart);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	private EingangsrechnungartDto assembleEingangsrechnungartDto(
			Eingangsrechnungart eingangsrechnungart) {
		return EingangsrechnungartDtoAssembler.createDto(eingangsrechnungart);
	}

	private EingangsrechnungartDto[] assembleEingangsrechnungartDtos(
			Collection<?> eingangsrechnungarts) {
		List<EingangsrechnungartDto> list = new ArrayList<EingangsrechnungartDto>();
		if (eingangsrechnungarts != null) {
			Iterator<?> iterator = eingangsrechnungarts.iterator();
			while (iterator.hasNext()) {
				Eingangsrechnungart eingangsrechnungart = (Eingangsrechnungart) iterator
						.next();
				list.add(assembleEingangsrechnungartDto(eingangsrechnungart));
			}
		}
		EingangsrechnungartDto[] returnArray = new EingangsrechnungartDto[list
				.size()];
		return (EingangsrechnungartDto[]) list.toArray(returnArray);
	}

	public EingangsrechnungartsprDto eingangsrechnungartsprFindByPrimaryKey(
			String eingangsrechnungartCNr, String localeCNr)
			throws EJBExceptionLP {
		try {
			EingangsrechnungartsprPK eingangsrechnungartsprPK = new EingangsrechnungartsprPK();
			eingangsrechnungartsprPK
					.setEingangsrechnungartCNr(eingangsrechnungartCNr);
			eingangsrechnungartsprPK.setLocaleCNr(localeCNr);
			Eingangsrechnungartspr eingangsrechnungartspr = em.find(
					Eingangsrechnungartspr.class, eingangsrechnungartsprPK);
			if (eingangsrechnungartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleEingangsrechnungartsprDto(eingangsrechnungartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private EingangsrechnungartsprDto assembleEingangsrechnungartsprDto(
			Eingangsrechnungartspr eingangsrechnungartspr) {
		return EingangsrechnungartsprDtoAssembler
				.createDto(eingangsrechnungartspr);
	}

	private EingangsrechnungartsprDto[] assembleEingangsrechnungartsprDtos(
			Collection<?> eingangsrechnungartsprs) {
		List<EingangsrechnungartsprDto> list = new ArrayList<EingangsrechnungartsprDto>();
		if (eingangsrechnungartsprs != null) {
			Iterator<?> iterator = eingangsrechnungartsprs.iterator();
			while (iterator.hasNext()) {
				Eingangsrechnungartspr eingangsrechnungartspr = (Eingangsrechnungartspr) iterator
						.next();
				list.add(assembleEingangsrechnungartsprDto(eingangsrechnungartspr));
			}
		}
		EingangsrechnungartsprDto[] returnArray = new EingangsrechnungartsprDto[list
				.size()];
		return (EingangsrechnungartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Hole alle ER-Arten nach Spr.
	 * 
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map<String, String> getAllSprEingangsrechnungarten(String cNrSpracheI)
			throws EJBExceptionLP {
		TreeMap<String, String> tmArten = new TreeMap<String, String>();
		// try {
		Query query = em.createNamedQuery("EingangsrechnungartfindAll");
		Collection<?> clArten = query.getResultList();
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Eingangsrechnungart eingangsrechnungartTemp = (Eingangsrechnungart) itArten
					.next();

			if (!eingangsrechnungartTemp.getCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {

				String key = eingangsrechnungartTemp.getCNr();
				String value = null;
				Eingangsrechnungartspr eingangsrechnungartspr = em.find(
						Eingangsrechnungartspr.class,
						new EingangsrechnungartsprPK(eingangsrechnungartTemp
								.getCNr(), cNrSpracheI));

				if (eingangsrechnungartspr == null) {
					// fuer locale und C_NR keine Bezeichnung vorhanden ...
					value = eingangsrechnungartTemp.getCNr();
				} else {
					value = eingangsrechnungartspr.getCBez();
				}
				tmArten.put(key, value);
			}
		}
		return tmArten;
	}

	public Map<String, String> getSprEingangsrechnungartNurZusatzkosten(
			String cNrSpracheI) {
		TreeMap<String, String> tmArten = new TreeMap<String, String>();
		// try {
		Query query = em.createNamedQuery("EingangsrechnungartfindAll");
		Collection<?> clArten = query.getResultList();
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Eingangsrechnungart eingangsrechnungartTemp = (Eingangsrechnungart) itArten
					.next();

			if (eingangsrechnungartTemp.getCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {

				String key = eingangsrechnungartTemp.getCNr();
				String value = null;
				// try {
				Eingangsrechnungartspr eingangsrechnungartspr = em.find(
						Eingangsrechnungartspr.class,
						new EingangsrechnungartsprPK(eingangsrechnungartTemp
								.getCNr(), cNrSpracheI));

				if (eingangsrechnungartspr == null) {
					// fuer locale und C_NR keine Bezeichnung vorhanden ...
					value = eingangsrechnungartTemp.getCNr();
				} else {
					value = eingangsrechnungartspr.getCBez();
				}
				tmArten.put(key, value);
			}
		}
		return tmArten;
	}

	public void createEingangsrechnungstatus(
			EingangsrechnungstatusDto eingangsrechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Eingangsrechnungstatus eingangsrechnungstatus = new Eingangsrechnungstatus(
					eingangsrechnungstatusDto.getStatusCNr());
			em.persist(eingangsrechnungstatus);
			em.flush();
			setEingangsrechnungstatusFromEingangsrechnungstatusDto(
					eingangsrechnungstatus, eingangsrechnungstatusDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void removeEingangsrechnungstatus(
			EingangsrechnungstatusDto eingangsrechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (eingangsrechnungstatusDto != null) {
				String statusCNr = eingangsrechnungstatusDto.getStatusCNr();
				Eingangsrechnungstatus toRemove = em.find(
						Eingangsrechnungstatus.class, statusCNr);
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

	private void updateEingangsrechnungstatus(
			EingangsrechnungstatusDto eingangsrechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungstatusDto != null) {
			String statusCNr = eingangsrechnungstatusDto.getStatusCNr();
			try {
				Eingangsrechnungstatus eingangsrechnungstatus = em.find(
						Eingangsrechnungstatus.class, statusCNr);
				setEingangsrechnungstatusFromEingangsrechnungstatusDto(
						eingangsrechnungstatus, eingangsrechnungstatusDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public EingangsrechnungstatusDto eingangsrechnungstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP {
		try {
			Eingangsrechnungstatus eingangsrechnungstatus = em.find(
					Eingangsrechnungstatus.class, statusCNr);
			if (eingangsrechnungstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleEingangsrechnungstatusDto(eingangsrechnungstatus);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setEingangsrechnungstatusFromEingangsrechnungstatusDto(
			Eingangsrechnungstatus eingangsrechnungstatus,
			EingangsrechnungstatusDto eingangsrechnungstatusDto) {
		em.merge(eingangsrechnungstatus);
		em.flush();
	}

	private EingangsrechnungstatusDto assembleEingangsrechnungstatusDto(
			Eingangsrechnungstatus eingangsrechnungstatus) {
		return EingangsrechnungstatusDtoAssembler
				.createDto(eingangsrechnungstatus);
	}

	private EingangsrechnungstatusDto[] assembleEingangsrechnungstatusDtos(
			Collection<?> eingangsrechnungstatuss) {
		List<EingangsrechnungstatusDto> list = new ArrayList<EingangsrechnungstatusDto>();
		if (eingangsrechnungstatuss != null) {
			Iterator<?> iterator = eingangsrechnungstatuss.iterator();
			while (iterator.hasNext()) {
				Eingangsrechnungstatus eingangsrechnungstatus = (Eingangsrechnungstatus) iterator
						.next();
				list.add(assembleEingangsrechnungstatusDto(eingangsrechnungstatus));
			}
		}
		EingangsrechnungstatusDto[] returnArray = new EingangsrechnungstatusDto[list
				.size()];
		return (EingangsrechnungstatusDto[]) list.toArray(returnArray);
	}

	/**
	 * Uebersetzt eine Eingangsrechnungart optimal. 1.Versuch: mit locale1
	 * 2.Versuch: mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzeEingangsrechnungartOptimal(String cNr,
			Locale locale1, Locale locale2) {
		// try {
		String helper = uebersetzeEingangsrechnungart(cNr, locale1);
		if (helper != null) {
			return helper;
		}
		// }
		// catch (FinderException ex) {
		{ // @ToDo FinderException
			// try {
			helper = uebersetzeEingangsrechnungart(cNr, locale2);
			if (helper != null) {
				return helper;
			}
			// }
			// catch (FinderException ex1) {
			// {
			return cNr;
			// }
			// return cNr;
			// }
		}
		// try {
		// return uebersetzeEingangsrechnungart(cNr, locale2);
		// }
		// catch (FinderException ex1) {
		// return cNr;
		// }
		// }
	}

	/**
	 * Uebersetzt eine UVA Art in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeEingangsrechnungart(String cNr, Locale locale) {
		String cLocale = null;
		try {
			cLocale = Helper.locale2String(locale);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		// try {
		Eingangsrechnungartspr eingangsrechnungartspr = em.find(
				Eingangsrechnungartspr.class, new EingangsrechnungartsprPK(cNr,
						cLocale));
		if (eingangsrechnungartspr == null) {
			return cNr;
		}
		return eingangsrechnungartspr.getCBez();
		// }
		// catch (FinderException ex1) {
		// return cNr;
		// }
	}

}
