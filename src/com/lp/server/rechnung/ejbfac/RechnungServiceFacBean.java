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
package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.forecast.ejb.Kommdrucker;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.KommdruckerDtoAssembler;
import com.lp.server.rechnung.ejb.Gutschriftpositionsart;
import com.lp.server.rechnung.ejb.Gutschriftsgrund;
import com.lp.server.rechnung.ejb.Gutschriftsgrundspr;
import com.lp.server.rechnung.ejb.GutschriftsgrundsprPK;
import com.lp.server.rechnung.ejb.Gutschrifttext;
import com.lp.server.rechnung.ejb.Mmz;
import com.lp.server.rechnung.ejb.Proformarechnungpositionsart;
import com.lp.server.rechnung.ejb.Rechnungart;
import com.lp.server.rechnung.ejb.Rechnungartspr;
import com.lp.server.rechnung.ejb.RechnungartsprPK;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejb.Rechnungpositionsart;
import com.lp.server.rechnung.ejb.Rechnungstatus;
import com.lp.server.rechnung.ejb.Rechnungtext;
import com.lp.server.rechnung.ejb.Rechnungtyp;
import com.lp.server.rechnung.ejb.Zahlungsart;
import com.lp.server.rechnung.ejb.Zahlungsartspr;
import com.lp.server.rechnung.ejb.ZahlungsartsprPK;
import com.lp.server.rechnung.fastlanereader.generated.FLRMmz;
import com.lp.server.rechnung.service.GutschriftpositionsartDto;
import com.lp.server.rechnung.service.GutschriftpositionsartDtoAssembler;
import com.lp.server.rechnung.service.GutschriftsgrundDto;
import com.lp.server.rechnung.service.GutschriftsgrundDtoAssembler;
import com.lp.server.rechnung.service.GutschriftsgrundsprDto;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.GutschrifttextDtoAssembler;
import com.lp.server.rechnung.service.MmzDto;
import com.lp.server.rechnung.service.MmzDtoAssembler;
import com.lp.server.rechnung.service.ProformarechnungpositionsartDto;
import com.lp.server.rechnung.service.ProformarechnungpositionsartDtoAssembler;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungSichtAuftragDto;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungartDtoAssembler;
import com.lp.server.rechnung.service.RechnungartsprDto;
import com.lp.server.rechnung.service.RechnungartsprDtoAssembler;
import com.lp.server.rechnung.service.RechnungpositionsartDto;
import com.lp.server.rechnung.service.RechnungpositionsartDtoAssembler;
import com.lp.server.rechnung.service.RechnungstatusDto;
import com.lp.server.rechnung.service.RechnungstatusDtoAssembler;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.RechnungtextDtoAssembler;
import com.lp.server.rechnung.service.RechnungtypDto;
import com.lp.server.rechnung.service.RechnungtypDtoAssembler;
import com.lp.server.rechnung.service.ZahlungsartDto;
import com.lp.server.rechnung.service.ZahlungsartDtoAssembler;
import com.lp.server.rechnung.service.ZahlungsartsprDto;
import com.lp.server.rechnung.service.ZahlungsartsprDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class RechnungServiceFacBean extends Facade implements RechnungServiceFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createRechnungtext(RechnungtextDto rechnungtextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(rechnungtextDto);
		if (rechnungtextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("rechnungtextDto == null"));
		}
		rechnungtextDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_RECHNUNGTEXT));
		try {
			Rechnungtext rechnungtext = new Rechnungtext(rechnungtextDto.getIId(), rechnungtextDto.getMandantCNr(),
					rechnungtextDto.getLocaleCNr(), rechnungtextDto.getCNr(), rechnungtextDto.getCTextinhalt());
			em.persist(rechnungtext);
			em.flush();
			setRechnungtextFromRechnungtextDto(rechnungtext, rechnungtextDto);
			return rechnungtextDto.getIId();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGutschrifttext(GutschrifttextDto gutschrifttextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(gutschrifttextDto);
		if (gutschrifttextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("rechnungtextDto == null"));
		}
		gutschrifttextDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_GUTSCHRIFTTEXT));
		try {
			Gutschrifttext text = new Gutschrifttext(gutschrifttextDto.getIId(), gutschrifttextDto.getMandantCNr(),
					gutschrifttextDto.getLocaleCNr(), gutschrifttextDto.getCNr(), gutschrifttextDto.getCTextinhalt());
			em.persist(text);
			em.flush();
			setGutschrifttextFromGutschrifttextDto(text, gutschrifttextDto);
			return gutschrifttextDto.getIId();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(gutschriftsgrundDto);
		if (gutschriftsgrundDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("gutschriftsgrundDto == null"));
		}
		gutschriftsgrundDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_GUTSCHRIFTSGRUND));
		try {
			Gutschriftsgrund grund = new Gutschriftsgrund(gutschriftsgrundDto.getCNr(), gutschriftsgrundDto.getIId());
			em.persist(grund);
			em.flush();
			setGutschriftsgrundFromGutschriftsgrundDto(grund, gutschriftsgrundDto);
			return gutschriftsgrundDto.getIId();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeRechnungtext(RechnungtextDto rechnungtextDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(rechnungtextDto);
		if (rechnungtextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("rechnungtextDto == null"));
		}
		try {
			if (rechnungtextDto != null) {
				Integer iId = rechnungtextDto.getIId();
				Rechnungtext toRemove = em.find(Rechnungtext.class, iId);
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
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeGutschrifttext(GutschrifttextDto gutschrifttextDto, TheClientDto theClientDto) {
		if (gutschrifttextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("gutschrifttextDto == null"));
		}
		try {
			if (gutschrifttextDto != null) {
				Integer iId = gutschrifttextDto.getIId();
				Gutschrifttext toRemove = em.find(Gutschrifttext.class, iId);
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
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateRechnungtext(RechnungtextDto rechnungtextDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(rechnungtextDto);
		if (rechnungtextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("rechnungtextDto == null"));
		}
		if (rechnungtextDto != null) {
			Integer iId = rechnungtextDto.getIId();
			try {
				Rechnungtext rechnungtext = em.find(Rechnungtext.class, iId);
				setRechnungtextFromRechnungtextDto(rechnungtext, rechnungtextDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateGutschrifttext(GutschrifttextDto gutschrifttextDto, TheClientDto theClientDto) {
		if (gutschrifttextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("gutschrifttextDto == null"));
		}
		if (gutschrifttextDto != null) {
			Integer iId = gutschrifttextDto.getIId();
			try {
				Gutschrifttext rechnungtext = em.find(Gutschrifttext.class, iId);
				setGutschrifttextFromGutschrifttextDto(rechnungtext, gutschrifttextDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public RechnungtextDto rechnungtextFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iId == null"));
		}
		try {
			Rechnungtext rechnungtext = em.find(Rechnungtext.class, iId);
			if (rechnungtext == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechnungtextDto(rechnungtext);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public GutschrifttextDto gutschrifttextFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iId == null"));
		}
		try {
			Gutschrifttext gutschrifttext = em.find(Gutschrifttext.class, iId);
			if (gutschrifttext == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleGutschrifttextDto(gutschrifttext);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public GutschriftsgrundDto gutschriftsgrundFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iId == null"));
		}
		try {
			Gutschriftsgrund gutschriftsgrund = em.find(Gutschriftsgrund.class, iId);
			if (gutschriftsgrund == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleGutschriftsgrundDto(gutschriftsgrund);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public RechnungtextDto rechnungtextFindByMandantLocaleCNr(String pMandant, String pSprache, String pText)
			throws EJBExceptionLP {
		if (pMandant == null || pSprache == null || pText == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pMandant == null || pSprache == null || pText == null"));
		}
		try {
			Query query = em.createNamedQuery("RechnungtextfindByMandantLocaleCNr");
			query.setParameter(1, pMandant);
			query.setParameter(2, pSprache);
			query.setParameter(3, pText);
			Rechnungtext rechnungtext = (Rechnungtext) query.getSingleResult();
			if (rechnungtext == null) {
				return null;
			}
			return assembleRechnungtextDto(rechnungtext);
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public GutschrifttextDto gutschrifttextFindByMandantLocaleCNr(String pMandant, String pSprache, String pText)
			throws EJBExceptionLP {
		if (pMandant == null || pSprache == null || pText == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pMandant == null || pSprache == null || pText == null"));
		}
		try {
			Query query = em.createNamedQuery("GutschrifttextfindByMandantLocaleCNr");
			query.setParameter(1, pMandant);
			query.setParameter(2, pSprache);
			query.setParameter(3, pText);
			Gutschrifttext text = (Gutschrifttext) query.getSingleResult();
			if (text == null) {
				return null;
			}
			return assembleGutschrifttextDto(text);
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	private void setRechnungtextFromRechnungtextDto(Rechnungtext rechnungtext, RechnungtextDto rechnungtextDto) {
		rechnungtext.setMandantCNr(rechnungtextDto.getMandantCNr());
		rechnungtext.setLocaleCNr(rechnungtextDto.getLocaleCNr());
		rechnungtext.setCNr(rechnungtextDto.getCNr());
		rechnungtext.setXTextinhalt(rechnungtextDto.getCTextinhalt());
		em.merge(rechnungtext);
		em.flush();
	}

	private void setGutschrifttextFromGutschrifttextDto(Gutschrifttext gutschrifttext,
			GutschrifttextDto gutschrifttextDto) {
		gutschrifttext.setMandantCNr(gutschrifttextDto.getMandantCNr());
		gutschrifttext.setLocaleCNr(gutschrifttextDto.getLocaleCNr());
		gutschrifttext.setCNr(gutschrifttextDto.getCNr());
		gutschrifttext.setXTextinhalt(gutschrifttextDto.getCTextinhalt());
		em.merge(gutschrifttext);
		em.flush();
	}

	private void setGutschriftsgrundFromGutschriftsgrundDto(Gutschriftsgrund grund, GutschriftsgrundDto grundDto) {
		grund.setCNr(grundDto.getCNr());
		em.merge(grund);
		em.flush();
	}

	private RechnungtextDto assembleRechnungtextDto(Rechnungtext rechnungtext) {
		return RechnungtextDtoAssembler.createDto(rechnungtext);
	}

	private GutschrifttextDto assembleGutschrifttextDto(Gutschrifttext gutschrifttext) {
		return GutschrifttextDtoAssembler.createDto(gutschrifttext);
	}

	private GutschriftsgrundDto assembleGutschriftsgrundDto(Gutschriftsgrund gutschriftsgrund) {
		return GutschriftsgrundDtoAssembler.createDto(gutschriftsgrund);
	}

	private RechnungtextDto[] assembleRechnungtextDtos(Collection<?> rechnungtexts) {
		List<RechnungtextDto> list = new ArrayList<RechnungtextDto>();
		if (rechnungtexts != null) {
			Iterator<?> iterator = rechnungtexts.iterator();
			while (iterator.hasNext()) {
				Rechnungtext rechnungtext = (Rechnungtext) iterator.next();
				list.add(assembleRechnungtextDto(rechnungtext));
			}
		}
		RechnungtextDto[] returnArray = new RechnungtextDto[list.size()];
		return (RechnungtextDto[]) list.toArray(returnArray);
	}

	public RechnungtextDto createDefaultRechnungtext(String sMediaartI, String sTextinhaltI, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(sMediaartI + "," + sTextinhaltI + "," + localeCNr);
		if (sMediaartI == null || sTextinhaltI == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sMediaartI == null || sTextinhaltI == null || localeCNr == null"));
		}
		RechnungtextDto oRechnungtextDto = new RechnungtextDto();
		oRechnungtextDto.setCNr(sMediaartI);
		oRechnungtextDto.setLocaleCNr(localeCNr);
		oRechnungtextDto.setMandantCNr(theClientDto.getMandant());
		oRechnungtextDto.setCTextinhalt(sTextinhaltI);
		oRechnungtextDto.setIId(createRechnungtext(oRechnungtextDto, theClientDto));

		return oRechnungtextDto;
	}

	public GutschrifttextDto createDefaultGutschrifttext(String sMediaartI, String sTextinhaltI, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(sMediaartI + "," + sTextinhaltI + "," + localeCNr);
		if (sMediaartI == null || sTextinhaltI == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sMediaartI == null || sTextinhaltI == null || localeCNr == null"));
		}
		GutschrifttextDto gutschrifttextDto = new GutschrifttextDto();
		gutschrifttextDto.setCNr(sMediaartI);
		gutschrifttextDto.setLocaleCNr(localeCNr);
		gutschrifttextDto.setMandantCNr(theClientDto.getMandant());
		gutschrifttextDto.setCTextinhalt(sTextinhaltI);
		gutschrifttextDto.setIId(createGutschrifttext(gutschrifttextDto, theClientDto));

		return gutschrifttextDto;
	}

	/**
	 * Lesen aller in der DB vorhandenen RechnungsPositionsarten inkl. uebersetzung
	 * 
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<String, String> getAllRechnungpositionsart(Locale locale1, Locale locale2) throws EJBExceptionLP {
		if (locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("locale1 == null || locale2 == null"));
		}
		RechnungpositionsartDto[] allArten = null;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Query query = em.createNamedQuery("RechnungpositionsartfindAllEnable");
		Collection<?> c = query.getResultList();
		allArten = RechnungpositionsartDtoAssembler.createDtos(c);
		try {
			for (int i = 0; i < allArten.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(allArten[i].getPositionsartCNr(), locale1, locale2);
				map.put(allArten[i].getPositionsartCNr(), sUebersetzung);
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return map;
	}

	/**
	 * Lesen aller in der DB vorhandenen Gutschriftositionsarten inkl. uebersetzung
	 * 
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<String, String> getAllGutschriftpositionsart(Locale locale1, Locale locale2) throws EJBExceptionLP {
		if (locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("locale1 == null || locale2 == null"));
		}
		GutschriftpositionsartDto[] allArten = null;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Query query = em.createNamedQuery("GutschriftpositionsartfindAllEnable");
		Collection<?> c = query.getResultList();
		allArten = GutschriftpositionsartDtoAssembler.createDtos(c);
		try {
			for (int i = 0; i < allArten.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(allArten[i].getPositionsartCNr(), locale1, locale2);
				map.put(allArten[i].getPositionsartCNr(), sUebersetzung);
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return map;
	}

	/**
	 * Lesen aller in der DB vorhandenen Proformarechnungpositionsarten. inkl.
	 * uebersetzung
	 * 
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<String, String> getAllProformarechnungpositionsart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		if (locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("locale1 == null || locale2 == null"));
		}
		ProformarechnungpositionsartDto[] allArten = null;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Query query = em.createNamedQuery("ProformarechnungpositionsartfindAll");
		Collection<?> c = query.getResultList();
		allArten = ProformarechnungpositionsartDtoAssembler.createDtos(c);
		try {
			for (int i = 0; i < allArten.length; i++) {
				String sUebersetzung = getSystemMultilanguageFac()
						.uebersetzePositionsartOptimal(allArten[i].getPositionsartCNr(), locale1, locale2);
				map.put(allArten[i].getPositionsartCNr(), sUebersetzung);
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return map;
	}

	public RechnungartsprDto rechnungartsprFindByPrimaryKey(String rechnungartCNr, Locale locale)
			throws EJBExceptionLP {
		if (rechnungartCNr == null || locale == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("rechnungartCNr == null || locale == null"));
		}
		try {
			RechnungartsprPK rechnungartsprPK = new RechnungartsprPK();
			rechnungartsprPK.setRechnungartCNr(rechnungartCNr);
			rechnungartsprPK.setLocaleCNr(Helper.locale2String(locale));
			Rechnungartspr rechnungartspr = em.find(Rechnungartspr.class, rechnungartsprPK);
			if (rechnungartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechnungartsprDto(rechnungartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungartsprDto rechnungartsprFindByPrimaryKeyOhneExc(String rechnungartCNr, Locale locale) {

		try {
			RechnungartsprPK rechnungartsprPK = new RechnungartsprPK();
			rechnungartsprPK.setRechnungartCNr(rechnungartCNr);
			rechnungartsprPK.setLocaleCNr(Helper.locale2String(locale));
			Rechnungartspr rechnungartspr = em.find(Rechnungartspr.class, rechnungartsprPK);
			if (rechnungartspr == null) {
				return null;
			}
			return assembleRechnungartsprDto(rechnungartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void setRechnungartsprFromRechnungartsprDto(Rechnungartspr rechnungartspr,
			RechnungartsprDto rechnungartsprDto) {
		rechnungartspr.setCBez(rechnungartsprDto.getCBez());
		em.merge(rechnungartspr);
		em.flush();
	}

	private void setGutschriftsgrundsprFromGutschriftsgrundsprDto(Gutschriftsgrundspr gutschriftsgrundspr,
			GutschriftsgrundsprDto gutschriftsgrundsprDto) {
		gutschriftsgrundspr.setCBez(gutschriftsgrundsprDto.getCBez());
		em.merge(gutschriftsgrundspr);
		em.flush();
	}

	private void setZahlungsartsprFromZahlungsartsprDto(Zahlungsartspr zahlungsartspr,
			ZahlungsartsprDto zahlungsartsprDto) {
		zahlungsartspr.setCBez(zahlungsartsprDto.getCBez());
		em.merge(zahlungsartspr);
		em.flush();
	}

	private RechnungartsprDto assembleRechnungartsprDto(Rechnungartspr rechnungartspr) {
		return RechnungartsprDtoAssembler.createDto(rechnungartspr);
	}

	private RechnungartsprDto[] assembleRechnungartsprDtos(Collection<?> rechnungartsprs) {
		List<RechnungartsprDto> list = new ArrayList<RechnungartsprDto>();
		if (rechnungartsprs != null) {
			Iterator<?> iterator = rechnungartsprs.iterator();
			while (iterator.hasNext()) {
				Rechnungartspr rechnungartspr = (Rechnungartspr) iterator.next();
				list.add(assembleRechnungartsprDto(rechnungartspr));
			}
		}
		RechnungartsprDto[] returnArray = new RechnungartsprDto[list.size()];
		return (RechnungartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von Rechnungarten.
	 * 
	 * @param pArray  Positionsarten
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return Map
	 */
	public Map<String, String> uebersetzeRechnungartOptimal(DatenspracheIf[] pArray, Locale locale1, Locale locale2) {
		if (pArray == null || locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pArray == null || locale1 == null || locale2 == null"));
		}
		TreeMap<String, String> uebersetzung = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeRechnungartOptimal(pArray[i].getCNr(), locale1, locale2);
			uebersetzung.put(key, value);
		}
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Rechnungart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr     String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return String
	 */
	public String uebersetzeRechnungartOptimal(String cNr, Locale locale1, Locale locale2) {
		String value = null;
		if (cNr == null || locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNr == null || locale1 == null || locale2 == null"));
		}
		value = uebersetzeRechnungart(cNr, locale1);
		if (value == null) {
			value = uebersetzeRechnungart(cNr, locale2);
		}
		if (value == null) {
			value = cNr;
		}
		return value;
	}

	/**
	 * Uebersetzt eine Rechnungart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr    String
	 * @param locale Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeRechnungart(String cNr, Locale locale) {
		String cLocale = null;
		cLocale = Helper.locale2String(locale);
		Rechnungartspr rechnungartspr = em.find(Rechnungartspr.class, new RechnungartsprPK(cNr, cLocale));
		if (rechnungartspr == null) {
			return null;
		}
		return rechnungartspr.getCBez();
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von Zahlungsarten.
	 * 
	 * @param pArray  Positionsarten
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return Map
	 */
	public Map<String, String> uebersetzeZahlungsartOptimal(DatenspracheIf[] pArray, Locale locale1, Locale locale2) {
		if (pArray == null || locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pArray == null || locale1 == null || locale2 == null"));
		}
		LinkedHashMap<String, String> tmZahlungsarten = new LinkedHashMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeZahlungsartOptimal(key, locale1, locale2);
			tmZahlungsarten.put(key, value);
		}
		return tmZahlungsarten;
	}

	/**
	 * Uebersetzt eine Zahlungsart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr     String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return String
	 */
	public String uebersetzeZahlungsartOptimal(String cNr, Locale locale1, Locale locale2) {
		if (cNr == null || locale1 == null || locale2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNr == null || locale1 == null || locale2 == null"));
		}
		String tempCnr = uebersetzeZahlungsart(cNr, locale1);
		;
		if (tempCnr == null) {
			tempCnr = uebersetzeZahlungsart(cNr, locale2);
		}
		if (tempCnr == null) {
			tempCnr = cNr;
		}
		return tempCnr;
	}

	/**
	 * Uebersetzt eine Zahlungsart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr    String
	 * @param locale Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeZahlungsart(String cNr, Locale locale) {
		String cLocale = null;
		try {
			cLocale = Helper.locale2String(locale);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		Zahlungsartspr zahlungsartspr = em.find(Zahlungsartspr.class, new ZahlungsartsprPK(cNr, cLocale));
		if (zahlungsartspr == null) {
			return null;
		}
		return zahlungsartspr.getCBez();
	}

	private Collection<?> rechnungartsprFindAll() {
		Query query = em.createNamedQuery("RechnungartsprfindAll");
		Collection<?> cl = query.getResultList();
		return cl;
	}

	private ZahlungsartsprDto assembleZahlungsartsprDto(Zahlungsartspr zahlungsartspr) {
		return ZahlungsartsprDtoAssembler.createDto(zahlungsartspr);
	}

	private ZahlungsartsprDto[] assembleZahlungsartsprDtos(Collection<?> zahlungsartsprs) {
		List<ZahlungsartsprDto> list = new ArrayList<ZahlungsartsprDto>();
		if (zahlungsartsprs != null) {
			Iterator<?> iterator = zahlungsartsprs.iterator();
			while (iterator.hasNext()) {
				Zahlungsartspr zahlungsartspr = (Zahlungsartspr) iterator.next();
				list.add(assembleZahlungsartsprDto(zahlungsartspr));
			}
		}
		ZahlungsartsprDto[] returnArray = new ZahlungsartsprDto[list.size()];
		return (ZahlungsartsprDto[]) list.toArray(returnArray);
	}

	public ZahlungsartsprDto zahlungsartsprFindByPrimaryKey(String zahlungsartCNr, Locale locale)
			throws EJBExceptionLP {
		if (zahlungsartCNr == null || locale == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("zahlungsartCNr == null || locale == null"));
		}
		try {
			ZahlungsartsprPK zahlungsartsprPK = new ZahlungsartsprPK();
			zahlungsartsprPK.setZahlungsartCNr(zahlungsartCNr);
			zahlungsartsprPK.setLocaleCNr(Helper.locale2String(locale));
			Zahlungsartspr zahlungsartspr = em.find(Zahlungsartspr.class, zahlungsartsprPK);
			if (zahlungsartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleZahlungsartsprDto(zahlungsartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	/**
	 * Erstellen einer GS-Positionsart.
	 * 
	 * @param gutschriftpositionsartDto GutschriftpositionsartDto
	 * @param theClientDto              String
	 * @throws EJBExceptionLP
	 */
	public void createGutschriftpositionsart(GutschriftpositionsartDto gutschriftpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(gutschriftpositionsartDto);
		if (gutschriftpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("gutschriftpositionsartDto == null"));
		}
		try {
			Gutschriftpositionsart gutschriftpositionsart = new Gutschriftpositionsart(
					gutschriftpositionsartDto.getPositionsartCNr(), gutschriftpositionsartDto.getISort(),
					gutschriftpositionsartDto.getBVersteckt());
			em.persist(gutschriftpositionsart);
			em.flush();
			setGutschriftpositionsartFromGutschriftpositionsartDto(gutschriftpositionsart, gutschriftpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Update einer GS-Positionsart.
	 * 
	 * @param gutschriftpositionsartDto GutschriftpositionsartDto
	 * @param theClientDto              String
	 * @throws EJBExceptionLP
	 */
	public void updateGutschriftpositionsart(GutschriftpositionsartDto gutschriftpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(gutschriftpositionsartDto);
		if (gutschriftpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("gutschriftpositionsartDto == null"));
		}
		if (gutschriftpositionsartDto != null) {
			String positionsartCNr = gutschriftpositionsartDto.getPositionsartCNr();
			try {
				Gutschriftpositionsart gutschriftpositionsart = em.find(Gutschriftpositionsart.class, positionsartCNr);
				setGutschriftpositionsartFromGutschriftpositionsartDto(gutschriftpositionsart,
						gutschriftpositionsartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	/**
	 * Finden einer GS-Positionsart.
	 * 
	 * @param positionsartCNr GutschriftpositionsartDto
	 * @throws EJBExceptionLP
	 * @return GutschriftpositionsartDto
	 */
	public GutschriftpositionsartDto gutschriftpositionsartFindByPrimaryKey(String positionsartCNr)
			throws EJBExceptionLP {
		// myLogger.logData(positionsartCNr);
		// myLogger.entry();
		if (positionsartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("positionsartCNr == null"));
		}
		try {
			Gutschriftpositionsart gutschriftpositionsart = em.find(Gutschriftpositionsart.class, positionsartCNr);
			if (gutschriftpositionsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleGutschriftpositionsartDto(gutschriftpositionsart);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	/**
	 * Finden aller GS-Positionsarten.
	 * 
	 * @throws EJBExceptionLP
	 * @return GutschriftpositionsartDto
	 */
	public GutschriftpositionsartDto[] gutschriftpositionsartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("GutschriftpositionsartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleGutschriftpositionsartDtos(cl);
	}

	private void setGutschriftpositionsartFromGutschriftpositionsartDto(Gutschriftpositionsart gutschriftpositionsart,
			GutschriftpositionsartDto gutschriftpositionsartDto) {
		gutschriftpositionsart.setISort(gutschriftpositionsartDto.getISort());
		gutschriftpositionsart.setBVersteckt(gutschriftpositionsartDto.getBVersteckt());
		em.merge(gutschriftpositionsart);
		em.flush();
	}

	private GutschriftpositionsartDto assembleGutschriftpositionsartDto(Gutschriftpositionsart gutschriftpositionsart) {
		return GutschriftpositionsartDtoAssembler.createDto(gutschriftpositionsart);
	}

	private GutschriftpositionsartDto[] assembleGutschriftpositionsartDtos(Collection<?> gutschriftpositionsarts) {
		List<GutschriftpositionsartDto> list = new ArrayList<GutschriftpositionsartDto>();
		if (gutschriftpositionsarts != null) {
			Iterator<?> iterator = gutschriftpositionsarts.iterator();
			while (iterator.hasNext()) {
				Gutschriftpositionsart gutschriftpositionsart = (Gutschriftpositionsart) iterator.next();
				list.add(assembleGutschriftpositionsartDto(gutschriftpositionsart));
			}
		}
		GutschriftpositionsartDto[] returnArray = new GutschriftpositionsartDto[list.size()];
		return (GutschriftpositionsartDto[]) list.toArray(returnArray);
	}

	/**
	 * Erstellen einer Rechnungpositionsart.
	 * 
	 * @param rechnungpositionsartDto RechnungpositionsartDto
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void createRechnungpositionsart(RechnungpositionsartDto rechnungpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(rechnungpositionsartDto);
		if (rechnungpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechnungpositionsartDto == null"));
		}
		try {
			Rechnungpositionsart rechnungpositionsart = new Rechnungpositionsart(
					rechnungpositionsartDto.getPositionsartCNr(), rechnungpositionsartDto.getISort(),
					rechnungpositionsartDto.getBVersteckt());
			em.persist(rechnungpositionsart);
			em.flush();
			setRechnungpositionsartFromRechnungpositionsartDto(rechnungpositionsart, rechnungpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Update einer Rechnungpositionsart.
	 * 
	 * @param rechnungpositionsartDto RechnungpositionsartDto
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void updateRechnungpositionsart(RechnungpositionsartDto rechnungpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(rechnungpositionsartDto);
		if (rechnungpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechnungpositionsartDto == null"));
		}
		String positionsartCNr = rechnungpositionsartDto.getPositionsartCNr();
		try {
			Rechnungpositionsart rechnungpositionsart = em.find(Rechnungpositionsart.class, positionsartCNr);
			setRechnungpositionsartFromRechnungpositionsartDto(rechnungpositionsart, rechnungpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	/**
	 * Finden einer Rechnungpositionsart.
	 * 
	 * @param positionsartCNr RechnungpositionsartDto
	 * @throws EJBExceptionLP
	 * @return RechnungpositionsartDto
	 */
	public RechnungpositionsartDto rechnungpositionsartFindByPrimaryKey(String positionsartCNr) throws EJBExceptionLP {
		if (positionsartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("positionsartCNr == null"));
		}
		try {
			Rechnungpositionsart rechnungpositionsart = em.find(Rechnungpositionsart.class, positionsartCNr);
			if (rechnungpositionsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechnungpositionsartDto(rechnungpositionsart);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public RechnungpositionsartDto[] rechnungpositionsartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungpositionsartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleRechnungpositionsartDtos(cl);
	}

	private void setRechnungpositionsartFromRechnungpositionsartDto(Rechnungpositionsart rechnungpositionsart,
			RechnungpositionsartDto rechnungpositionsartDto) {
		rechnungpositionsart.setISort(rechnungpositionsartDto.getISort());
		rechnungpositionsart.setBVersteckt(rechnungpositionsartDto.getBVersteckt());
		em.merge(rechnungpositionsart);
		em.flush();
	}

	private RechnungpositionsartDto assembleRechnungpositionsartDto(Rechnungpositionsart rechnungpositionsart) {
		return RechnungpositionsartDtoAssembler.createDto(rechnungpositionsart);
	}

	private RechnungpositionsartDto[] assembleRechnungpositionsartDtos(Collection<?> rechnungpositionsarts) {
		List<RechnungpositionsartDto> list = new ArrayList<RechnungpositionsartDto>();
		if (rechnungpositionsarts != null) {
			Iterator<?> iterator = rechnungpositionsarts.iterator();
			while (iterator.hasNext()) {
				Rechnungpositionsart rechnungpositionsart = (Rechnungpositionsart) iterator.next();
				list.add(assembleRechnungpositionsartDto(rechnungpositionsart));
			}
		}
		RechnungpositionsartDto[] returnArray = new RechnungpositionsartDto[list.size()];
		return (RechnungpositionsartDto[]) list.toArray(returnArray);
	}

	public Integer createMmz(MmzDto dto) {

		try {
			Query query = null;
			if (dto.getLandIId() != null) {
				query = em.createNamedQuery("MmzByMandantCNrNBisWertLandIId");
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getNBisWert());
				query.setParameter(3, dto.getLandIId());
			} else {
				query = em.createNamedQuery("MmzByMandantCNrNBisWert");
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getNBisWert());
			}

			Mmz doppelt = (Mmz) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("RECH_MMZ.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MMZ);
			dto.setIId(pk);

			Mmz bean = new Mmz(dto.getIId(), dto.getMandantCNr(), dto.getArtikelIId(), dto.getNBisWert(),
					dto.getNZuschlag());
			em.persist(bean);
			em.flush();
			setMmzFromMmzDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public MmzDto mmzFindByPrimaryKey(Integer iId) {
		Mmz bean = em.find(Mmz.class, iId);
		return MmzDtoAssembler.createDto(bean);
	}

	public void removeMmz(MmzDto dto) {
		Mmz toRemove = em.find(Mmz.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateMmz(MmzDto dto) {
		Mmz bean = em.find(Mmz.class, dto.getIId());

		try {
			Query query = null;
			if (dto.getLandIId() != null) {
				query = em.createNamedQuery("MmzByMandantCNrNBisWertLandIId");
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getNBisWert());
				query.setParameter(3, dto.getLandIId());
			} else {
				query = em.createNamedQuery("MmzByMandantCNrNBisWert");
				query.setParameter(1, dto.getMandantCNr());
				query.setParameter(2, dto.getNBisWert());
			}
			Integer iIdVorhanden = ((Mmz) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("RECH_MMZ.UK"));
			}
		} catch (NoResultException ex) {

		}

		setMmzFromMmzDto(bean, dto);
	}

	/**
	 * Erstellen einer Proformarechnungpositionsart.
	 * 
	 * @param proformarechnungpositionsartDto ProformarechnungpositionsartDto
	 * @param theClientDto                    String
	 * @throws EJBExceptionLP
	 */
	public void createProformarechnungpositionsart(ProformarechnungpositionsartDto proformarechnungpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(proformarechnungpositionsartDto);
		if (proformarechnungpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("proformarechnungpositionsartDto == null"));
		}
		try {
			Proformarechnungpositionsart proformarechnungpositionsart = new Proformarechnungpositionsart(
					proformarechnungpositionsartDto.getPositionsartCNr(), proformarechnungpositionsartDto.getISort());
			em.persist(proformarechnungpositionsart);
			em.flush();
			setProformarechnungpositionsartFromProformarechnungpositionsartDto(proformarechnungpositionsart,
					proformarechnungpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Update einer Proformarechnungpositionsart.
	 * 
	 * @param proformarechnungpositionsartDto ProformarechnungpositionsartDto
	 * @param theClientDto                    String
	 * @throws EJBExceptionLP
	 */
	public void updateProformarechnungpositionsart(ProformarechnungpositionsartDto proformarechnungpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(proformarechnungpositionsartDto);
		if (proformarechnungpositionsartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("proformarechnungpositionsartDto == null"));
		}
		if (proformarechnungpositionsartDto != null) {
			String positionsartCNr = proformarechnungpositionsartDto.getPositionsartCNr();
			try {
				Proformarechnungpositionsart proformarechnungpositionsart = em.find(Proformarechnungpositionsart.class,
						positionsartCNr);
				setProformarechnungpositionsartFromProformarechnungpositionsartDto(proformarechnungpositionsart,
						proformarechnungpositionsartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	/**
	 * Finden einer Proformarechnungpositionsart.
	 * 
	 * @param positionsartCNr ProformarechnungpositionsartDto
	 * @throws EJBExceptionLP
	 * @return ProformarechnungpositionsartDto
	 */
	public ProformarechnungpositionsartDto proformarechnungpositionsartFindByPrimaryKey(String positionsartCNr)
			throws EJBExceptionLP {
		if (positionsartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("positionsartCNr == null"));
		}
		try {
			Proformarechnungpositionsart proformarechnungpositionsart = em.find(Proformarechnungpositionsart.class,
					positionsartCNr);
			if (proformarechnungpositionsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleProformarechnungpositionsartDto(proformarechnungpositionsart);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	/**
	 * Finden aller Proformarechnungpositionsarten.
	 * 
	 * @throws EJBExceptionLP
	 * @return ProformarechnungpositionsartDto
	 */
	public ProformarechnungpositionsartDto[] proformarechnungpositionsartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("ProformarechnungpositionsartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleProformarechnungpositionsartDtos(cl);
	}

	private void setProformarechnungpositionsartFromProformarechnungpositionsartDto(
			Proformarechnungpositionsart proformarechnungpositionsart,
			ProformarechnungpositionsartDto proformarechnungpositionsartDto) {
		proformarechnungpositionsart.setISort(proformarechnungpositionsartDto.getISort());
		em.merge(proformarechnungpositionsart);
		em.flush();
	}

	private ProformarechnungpositionsartDto assembleProformarechnungpositionsartDto(
			Proformarechnungpositionsart proformarechnungpositionsart) {
		return ProformarechnungpositionsartDtoAssembler.createDto(proformarechnungpositionsart);
	}

	private ProformarechnungpositionsartDto[] assembleProformarechnungpositionsartDtos(
			Collection<?> proformarechnungpositionsarts) {
		List<ProformarechnungpositionsartDto> list = new ArrayList<ProformarechnungpositionsartDto>();
		if (proformarechnungpositionsarts != null) {
			Iterator<?> iterator = proformarechnungpositionsarts.iterator();
			while (iterator.hasNext()) {
				Proformarechnungpositionsart proformarechnungpositionsart = (Proformarechnungpositionsart) iterator
						.next();
				list.add(assembleProformarechnungpositionsartDto(proformarechnungpositionsart));
			}
		}
		ProformarechnungpositionsartDto[] returnArray = new ProformarechnungpositionsartDto[list.size()];
		return (ProformarechnungpositionsartDto[]) list.toArray(returnArray);
	}

	public void updateRechnungart(RechnungartDto rechnungartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(rechnungartDto);
		// code begin
		if (rechnungartDto != null) {
			String cNr = rechnungartDto.getCNr();
			try {
				Rechnungart rechnungart = em.find(Rechnungart.class, cNr);
				setRechnungartFromRechnungartDto(rechnungart, rechnungartDto);
				// sprache
				if (rechnungartDto.getRechnungartsprDto() != null) {
					rechnungartDto.getRechnungartsprDto().setRechnungartCNr(rechnungartDto.getCNr());
					rechnungartDto.getRechnungartsprDto().setSpracheCNr(theClientDto.getLocUiAsString());
					updateRechnungartspr(rechnungartDto.getRechnungartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(gutschriftsgrundDto);
		// code begin
		if (gutschriftsgrundDto != null) {
			Integer iId = gutschriftsgrundDto.getIId();
			try {
				Gutschriftsgrund gutschriftsgrund = em.find(Gutschriftsgrund.class, iId);
				setGutschriftsgrundFromGutschriftsgrundDto(gutschriftsgrund, gutschriftsgrundDto);
				// sprache
				if (gutschriftsgrundDto.getGutschriftsgrundsprDto() != null) {
					gutschriftsgrundDto.getGutschriftsgrundsprDto()
							.setGutschriftsgrundIId(gutschriftsgrundDto.getIId());
					gutschriftsgrundDto.getGutschriftsgrundsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
					updateGutschriftsgrundspr(gutschriftsgrundDto.getGutschriftsgrundsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateZahlungsart(ZahlungsartDto zahlungsartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(zahlungsartDto);
		// code begin
		if (zahlungsartDto != null) {
			String cNr = zahlungsartDto.getCNr();
			try {
				Zahlungsart zahlungsart = em.find(Zahlungsart.class, cNr);
				setZahlungsartFromZahlungsartDto(zahlungsart, zahlungsartDto);
				// sprache
				if (zahlungsartDto.getZahlungsartsprDto() != null) {
					zahlungsartDto.getZahlungsartsprDto().setZahlungsartCNr(zahlungsartDto.getCNr());
					zahlungsartDto.getZahlungsartsprDto().setSpracheCNr(theClientDto.getLocUiAsString());
					updateZahlungsartspr(zahlungsartDto.getZahlungsartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void createRechnungtyp(RechnungtypDto rechnungtypDto, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Rechnungtyp rechnungtyp = new Rechnungtyp(rechnungtypDto.getCNr(), rechnungtypDto.getISort());
			em.persist(rechnungtyp);
			em.flush();
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public RechnungtypDto rechnungtypFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		try {
			Rechnungtyp rechnungtyp = em.find(Rechnungtyp.class, cNr);
			if (rechnungtyp == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechnungtypDto(rechnungtyp);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungtypDto[] rechnungtypFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungtypfindAll");
		Collection<?> cl = query.getResultList();
		return assembleRechnungtypDtos(cl);
	}

	private void setRechnungtypFromRechnungtypDto(Rechnungtyp rechnungtyp, RechnungtypDto rechnungtypDto) {
		rechnungtyp.setISort(rechnungtypDto.getISort());
		em.merge(rechnungtyp);
		em.flush();
	}

	private RechnungtypDto assembleRechnungtypDto(Rechnungtyp rechnungtyp) {
		return RechnungtypDtoAssembler.createDto(rechnungtyp);
	}

	private RechnungtypDto[] assembleRechnungtypDtos(Collection<?> rechnungtyps) {
		List<RechnungtypDto> list = new ArrayList<RechnungtypDto>();
		if (rechnungtyps != null) {
			Iterator<?> iterator = rechnungtyps.iterator();
			while (iterator.hasNext()) {
				Rechnungtyp rechnungtyp = (Rechnungtyp) iterator.next();
				list.add(assembleRechnungtypDto(rechnungtyp));
			}
		}
		RechnungtypDto[] returnArray = new RechnungtypDto[list.size()];
		return (RechnungtypDto[]) list.toArray(returnArray);
	}

	/**
	 * Lesen aller in der DB vorhandenen Rechnungsarten nach Rechnungstyp inkl.
	 * uebersetzung
	 * 
	 * @param rechnungstypCNr String
	 * @param locale1         Locale
	 * @param locale2         Locale
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	private Map<String, String> getAllRechnungart(String rechnungstypCNr, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		RechnungtypDto typDto = rechnungtypFindByPrimaryKey(rechnungstypCNr);
		RechnungartDto[] allArten = rechnungartFindByRechnungtyp(typDto.getCNr());
		return uebersetzeRechnungartOptimal(allArten, locale1, locale2);
	}

	public MmzDto getMindermengenzuschlag(BigDecimal bdWert, Integer landIId, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		Integer mmzIId = null;
		if (landIId != null) {
			org.hibernate.Criteria crit = session.createCriteria(FLRMmz.class)
					.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()))
					.add(Restrictions.eq("land_i_id", landIId)).add(Restrictions.gt("n_bis_wert", bdWert))
					.addOrder(Order.asc("n_bis_wert")).setMaxResults(1);
			List<?> results = crit.list();

			Iterator<?> resultListIterator = results.iterator();

			if (resultListIterator.hasNext()) {
				FLRMmz flrMmz = (FLRMmz) resultListIterator.next();
				mmzIId = flrMmz.getI_id();
			}

		}

		if (mmzIId == null) {
			org.hibernate.Criteria crit = session.createCriteria(FLRMmz.class)
					.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()))
					.add(Restrictions.gt("n_bis_wert", bdWert)).add(Restrictions.isNull("land_i_id"))
					.addOrder(Order.asc("n_bis_wert")).setMaxResults(1);
			List<?> results = crit.list();

			Iterator<?> resultListIterator = results.iterator();

			if (resultListIterator.hasNext()) {
				FLRMmz flrMmz = (FLRMmz) resultListIterator.next();
				mmzIId = flrMmz.getI_id();
			}

		}

		if (mmzIId != null) {
			MmzDto mmzDto = mmzFindByPrimaryKey(mmzIId);

			return mmzDto;
		}

		return null;

	}

	/**
	 * Alle Zahlungsarten mit Uebersetzung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return Map
	 * @param locale1 Locale
	 * @param locale2 Locale
	 */
	public Map getAllZahlungsarten(Locale locale1, Locale locale2) throws EJBExceptionLP {
		ZahlungsartDto[] arten = zahlungsartFindAll();
		try {
			return getRechnungServiceFac().uebersetzeZahlungsartOptimal(arten, locale1, locale2);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		}
	}

	/**
	 * Alle Arten einer Rechnung mit uebersetzung holen
	 * 
	 * @throws EJBExceptionLP
	 * @return Map
	 * @param locale1 Locale
	 * @param locale2 Locale
	 */
	public Map<String, String> getAllRechnungartRechnung(Locale locale1, Locale locale2) throws EJBExceptionLP {
		return getAllRechnungart(RechnungFac.RECHNUNGTYP_RECHNUNG, locale1, locale2);
	}

	/**
	 * Alle Arten einer Gutschrift mit uebersetzung holen
	 * 
	 * @throws EJBExceptionLP
	 * @return Map
	 * @param locale1 Locale
	 * @param locale2 Locale
	 */
	public Map<String, String> getAllRechnungartGutschrift(Locale locale1, Locale locale2) throws EJBExceptionLP {
		return getAllRechnungart(RechnungFac.RECHNUNGTYP_GUTSCHRIFT, locale1, locale2);
	}

	/**
	 * Alle Arten einer Proformarechnung mit uebersetzung holen
	 * 
	 * @throws EJBExceptionLP
	 * @return Map
	 * @param locale1 Locale
	 * @param locale2 Locale
	 */
	public Map<String, String> getAllRechnungartProformarechnung(Locale locale1, Locale locale2) throws EJBExceptionLP {
		return getAllRechnungart(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG, locale1, locale2);
	}

	public void createRechnungart(RechnungartDto rechnungartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Rechnungart rechnungart = new Rechnungart(rechnungartDto.getCNr(), rechnungartDto.getRechnungtypCNr(),
					rechnungartDto.getISort());
			em.persist(rechnungart);
			em.flush();
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public RechnungartDto rechnungartFindByPrimaryKey(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Rechnungart rechnungart = em.find(Rechnungart.class, cNr);
			if (rechnungart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			RechnungartDto rechnungartDto = assembleRechnungartDto(rechnungart);
			rechnungartDto.setRechnungartsprDto(new RechnungartsprDto());
			rechnungartDto.getRechnungartsprDto().setRechnungartCNr(cNr);
			rechnungartDto.getRechnungartsprDto().setSpracheCNr(theClientDto.getLocUiAsString());
			rechnungartDto.getRechnungartsprDto().setCBez(uebersetzeRechnungart(cNr, theClientDto.getLocUi()));
			return rechnungartDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public RechnungartDto[] rechnungartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleRechnungartDtos(cl);
	}

	public RechnungartDto[] rechnungartFindByRechnungtyp(String rechnungtypCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery("RechnungartfindByRechnungtyp");
		query.setParameter(1, rechnungtypCNr);
		Collection<?> cl = query.getResultList();
		return assembleRechnungartDtos(cl);
	}

	private void setRechnungartFromRechnungartDto(Rechnungart rechnungart, RechnungartDto rechnungartDto) {
		rechnungart.setRechnungtypCNr(rechnungartDto.getRechnungtypCNr());
		rechnungart.setISort(rechnungartDto.getISort());
		em.merge(rechnungart);
		em.flush();
	}

	private void setZahlungsartFromZahlungsartDto(Zahlungsart zahlungsart, ZahlungsartDto zahlungsartDto) {
		zahlungsart.setISort(zahlungsartDto.getISort());
		em.merge(zahlungsart);
		em.flush();
	}

	private RechnungartDto assembleRechnungartDto(Rechnungart rechnungart) {
		return RechnungartDtoAssembler.createDto(rechnungart);
	}

	private RechnungartDto[] assembleRechnungartDtos(Collection<?> rechnungarts) {
		List<RechnungartDto> list = new ArrayList<RechnungartDto>();
		if (rechnungarts != null) {
			Iterator<?> iterator = rechnungarts.iterator();
			while (iterator.hasNext()) {
				Rechnungart rechnungart = (Rechnungart) iterator.next();
				list.add(assembleRechnungartDto(rechnungart));
			}
		}
		RechnungartDto[] returnArray = new RechnungartDto[list.size()];
		return (RechnungartDto[]) list.toArray(returnArray);
	}

	public void createZahlungsart(ZahlungsartDto zahlungsartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Zahlungsart zahlungsart = new Zahlungsart(zahlungsartDto.getCNr(), zahlungsartDto.getISort());
			em.persist(zahlungsart);
			em.flush();
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public ZahlungsartDto[] zahlungsartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("ZahlungsartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleZahlungsartDtos(cl);
	}

	private ZahlungsartDto assembleZahlungsartDto(Zahlungsart zahlungsart) {
		return ZahlungsartDtoAssembler.createDto(zahlungsart);
	}

	private ZahlungsartDto[] assembleZahlungsartDtos(Collection<?> zahlungsarts) {
		List<ZahlungsartDto> list = new ArrayList<ZahlungsartDto>();
		if (zahlungsarts != null) {
			Iterator<?> iterator = zahlungsarts.iterator();
			while (iterator.hasNext()) {
				Zahlungsart zahlungsart = (Zahlungsart) iterator.next();
				list.add(assembleZahlungsartDto(zahlungsart));
			}
		}
		ZahlungsartDto[] returnArray = new ZahlungsartDto[list.size()];
		return (ZahlungsartDto[]) list.toArray(returnArray);
	}

	public void createRechnungstatus(RechnungstatusDto rechnungstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechnungstatusDto == null) {
			return;
		}
		try {
			Rechnungstatus rechnungstatus = new Rechnungstatus(rechnungstatusDto.getStatusCNr(),
					rechnungstatusDto.getISort());
			em.persist(rechnungstatus);
			em.flush();
			setRechnungstatusFromRechnungstatusDto(rechnungstatus, rechnungstatusDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeRechnungstatus(RechnungstatusDto rechnungstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechnungstatusDto != null) {
			String statusCNr = rechnungstatusDto.getStatusCNr();
			try {
				Rechnungstatus toRemove = em.find(Rechnungstatus.class, statusCNr);
				if (toRemove == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			}
		}
	}

	public void removeGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (gutschriftsgrundDto != null) {
			Integer iid = gutschriftsgrundDto.getIId();
			try {
				Query query = em.createNamedQuery("GutschriftsgrundsprfindByGutschriftsgrundIId");
				query.setParameter(1, gutschriftsgrundDto.getIId());
				Collection<?> c = query.getResultList();
				// Erst alle SPRs dazu loeschen.
				for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
					Gutschriftsgrundspr item = (Gutschriftsgrundspr) iter.next();
					em.remove(item);
				}

				Gutschriftsgrund toRemove = em.find(Gutschriftsgrund.class, iid);
				if (toRemove == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			}
		}
	}

	public void updateRechnungstatus(RechnungstatusDto rechnungstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechnungstatusDto != null) {
			String statusCNr = rechnungstatusDto.getStatusCNr();
			try {
				Rechnungstatus rechnungstatus = em.find(Rechnungstatus.class, statusCNr);
				setRechnungstatusFromRechnungstatusDto(rechnungstatus, rechnungstatusDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public RechnungstatusDto rechnungstatusFindByPrimaryKey(String statusCNr) throws EJBExceptionLP {
		Rechnungstatus rechnungstatus = em.find(Rechnungstatus.class, statusCNr);
		if (rechnungstatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleRechnungstatusDto(rechnungstatus);
	}

	private void setRechnungstatusFromRechnungstatusDto(Rechnungstatus rechnungstatus,
			RechnungstatusDto rechnungstatusDto) {
		rechnungstatus.setISort(rechnungstatusDto.getISort());
		em.merge(rechnungstatus);
		em.flush();
	}

	private void setMmzFromMmzDto(Mmz bean, MmzDto dto) {
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setNBisWert(dto.getNBisWert());
		bean.setNZuschlag(dto.getNZuschlag());
		bean.setLandIId(dto.getLandIId());
		em.merge(bean);
		em.flush();
	}

	private RechnungstatusDto assembleRechnungstatusDto(Rechnungstatus rechnungstatus) {
		return RechnungstatusDtoAssembler.createDto(rechnungstatus);
	}

	private RechnungstatusDto[] assembleRechnungstatusDtos(Collection<?> rechnungstatuss) {
		List<RechnungstatusDto> list = new ArrayList<RechnungstatusDto>();
		if (rechnungstatuss != null) {
			Iterator<?> iterator = rechnungstatuss.iterator();
			while (iterator.hasNext()) {
				Rechnungstatus rechnungstatus = (Rechnungstatus) iterator.next();
				list.add(assembleRechnungstatusDto(rechnungstatus));
			}
		}
		RechnungstatusDto[] returnArray = new RechnungstatusDto[list.size()];
		return (RechnungstatusDto[]) list.toArray(returnArray);
	}

	public ZahlungsartDto zahlungsartFindByPrimaryKey(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Zahlungsart zahlungsart = em.find(Zahlungsart.class, cNrI);
			if (zahlungsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			ZahlungsartDto zahlungsartDto = assembleZahlungsartDto(zahlungsart);
			zahlungsartDto.setZahlungsartsprDto(new ZahlungsartsprDto());
			zahlungsartDto.getZahlungsartsprDto().setZahlungsartCNr(cNrI);
			zahlungsartDto.getZahlungsartsprDto().setSpracheCNr(theClientDto.getLocUiAsString());
			zahlungsartDto.getZahlungsartsprDto().setCBez(uebersetzeZahlungsart(cNrI, theClientDto.getLocUi()));
			return zahlungsartDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public ZahlungsartDto zahlungsartFindByPrimaryKeyUndLocale(String cNrI, Locale locale) throws EJBExceptionLP {
		try {
			Zahlungsart zahlungsart = em.find(Zahlungsart.class, cNrI);
			if (zahlungsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			ZahlungsartDto zahlungsartDto = assembleZahlungsartDto(zahlungsart);
			zahlungsartDto.setZahlungsartsprDto(new ZahlungsartsprDto());
			zahlungsartDto.getZahlungsartsprDto().setZahlungsartCNr(cNrI);
			zahlungsartDto.getZahlungsartsprDto().setSpracheCNr(Helper.locale2String(locale));
			zahlungsartDto.getZahlungsartsprDto().setCBez(uebersetzeZahlungsart(cNrI, locale));
			return zahlungsartDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void updateRechnungartspr(RechnungartsprDto rechnungartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(rechnungartsprDto);
		// code begin
		if (rechnungartsprDto != null) {
			String cNr = rechnungartsprDto.getRechnungartCNr();
			String localeCNr = rechnungartsprDto.getSpracheCNr();
			try {
				RechnungartsprPK pk = new RechnungartsprPK(cNr, localeCNr);
				Rechnungartspr rechnungartSpr = em.find(Rechnungartspr.class, pk);
				if (rechnungartSpr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createRechnungartspr(rechnungartsprDto, theClientDto);
				} else {
					setRechnungartsprFromRechnungartsprDto(rechnungartSpr, rechnungartsprDto);
				}

			} catch (NoResultException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void updateGutschriftsgrundspr(GutschriftsgrundsprDto gutschriftsgrundsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(gutschriftsgrundsprDto);
		// code begin
		if (gutschriftsgrundsprDto != null) {
			Integer iId = gutschriftsgrundsprDto.getGutschriftsgrundIId();
			String localeCNr = gutschriftsgrundsprDto.getLocaleCNr();
			try {
				GutschriftsgrundsprPK pk = new GutschriftsgrundsprPK(iId, localeCNr);
				Gutschriftsgrundspr spr = em.find(Gutschriftsgrundspr.class, pk);
				if (spr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createGutschriftsgrundspr(gutschriftsgrundsprDto, theClientDto);
				} else {
					setGutschriftsgrundsprFromGutschriftsgrundsprDto(spr, gutschriftsgrundsprDto);
				}

			} catch (NoResultException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void updateZahlungsartspr(ZahlungsartsprDto zahlungsartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(zahlungsartsprDto);
		// code begin
		if (zahlungsartsprDto != null) {
			String cNr = zahlungsartsprDto.getZahlungsartCNr();
			String localeCNr = zahlungsartsprDto.getSpracheCNr();
			try {
				ZahlungsartsprPK pk = new ZahlungsartsprPK(cNr, localeCNr);
				Zahlungsartspr zahlungsartSpr = em.find(Zahlungsartspr.class, pk);
				if (zahlungsartSpr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createZahlungsartspr(zahlungsartsprDto, theClientDto);
				} else {
					setZahlungsartsprFromZahlungsartsprDto(zahlungsartSpr, zahlungsartsprDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void createRechnungartspr(RechnungartsprDto rechnungartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechnungartsprDto == null) {
			return;
		}
		try {
			Rechnungartspr rechnungartspr = new Rechnungartspr(rechnungartsprDto.getRechnungartCNr(),
					rechnungartsprDto.getSpracheCNr());
			em.persist(rechnungartspr);
			em.flush();
			setRechnungartsprFromRechnungartsprDto(rechnungartspr, rechnungartsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void createGutschriftsgrundspr(GutschriftsgrundsprDto gutschriftsgrundsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (gutschriftsgrundsprDto == null) {
			return;
		}
		try {
			Gutschriftsgrundspr gutschriftsgrundspr = new Gutschriftsgrundspr(
					gutschriftsgrundsprDto.getGutschriftsgrundIId(), gutschriftsgrundsprDto.getLocaleCNr());
			em.persist(gutschriftsgrundspr);
			em.flush();
			setGutschriftsgrundsprFromGutschriftsgrundsprDto(gutschriftsgrundspr, gutschriftsgrundsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void createZahlungsartspr(ZahlungsartsprDto zahlungsartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (zahlungsartsprDto == null) {
			return;
		}
		try {
			Zahlungsartspr zahlungsartspr = new Zahlungsartspr(zahlungsartsprDto.getZahlungsartCNr(),
					zahlungsartsprDto.getSpracheCNr());
			em.persist(zahlungsartspr);
			em.flush();
			setZahlungsartsprFromZahlungsartsprDto(zahlungsartspr, zahlungsartsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public RechnungSichtAuftragDto rechnungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		RechnungSichtAuftragDto sichtAuftragDto = new RechnungSichtAuftragDto();

		try {
			sichtAuftragDto.setRechnungDto(getRechnungFac().rechnungFindByPrimaryKey(iId));
			if (sichtAuftragDto.getRechnungDto().getAuftragIId() != null) {
				Integer hauptAuftragId = sichtAuftragDto.getRechnungDto().getAuftragIId();
				sichtAuftragDto.setAuftragDto(getAuftragFac().auftragFindByPrimaryKey(hauptAuftragId));

				Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
				query.setParameter(1, iId);
				Collection<Rechnungposition> cl = query.getResultList();
				for (Rechnungposition rechnungposition : cl) {
					if (rechnungposition.getAuftragpositionIId() != null) {
						Auftragposition position = em.find(Auftragposition.class,
								rechnungposition.getAuftragpositionIId());
						if (position != null) {
							if (!position.getAuftragIId().equals(hauptAuftragId)) {
								sichtAuftragDto.setMehrAlsHauptAuftrag(true);
								break;
							}
						}
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return sichtAuftragDto;
	}
}
