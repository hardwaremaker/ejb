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

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.partner.ejb.Branche;
import com.lp.server.partner.ejb.Branchespr;
import com.lp.server.partner.ejb.BranchesprPK;
import com.lp.server.partner.ejb.Kommunikationsart;
import com.lp.server.partner.ejb.Kommunikationsartspr;
import com.lp.server.partner.ejb.KommunikationsartsprPK;
import com.lp.server.partner.ejb.Kontaktart;
import com.lp.server.partner.ejb.Selektion;
import com.lp.server.partner.ejb.Selektionspr;
import com.lp.server.partner.ejb.SelektionsprPK;
import com.lp.server.partner.ejb.Serienbrief;
import com.lp.server.partner.ejb.Serienbriefselektion;
import com.lp.server.partner.ejb.SerienbriefselektionPK;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.BrancheDtoAssembler;
import com.lp.server.partner.service.BranchesprDto;
import com.lp.server.partner.service.BranchesprDtoAssembler;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.KommunikationsartDtoAssembler;
import com.lp.server.partner.service.KommunikationsartsprDto;
import com.lp.server.partner.service.KommunikationsartsprDtoAssembler;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.KontaktartDtoAssembler;
import com.lp.server.partner.service.KundeSelectCriteriaDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.SelektionDtoAssembler;
import com.lp.server.partner.service.SelektionsprDto;
import com.lp.server.partner.service.SelektionsprDtoAssembler;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefDtoAssembler;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class PartnerServicesFacBean extends Facade implements
		PartnerServicesFac {

	@PersistenceContext
	private EntityManager em;

	public String createKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kommunikationsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kommunikationsartDtoI == null"));
		}

		Kommunikationsart kommunikationsart = null;
		try {
			kommunikationsart = new Kommunikationsart(
					kommunikationsartDtoI.getCNr());
			em.persist(kommunikationsart);
			em.flush();
			if (kommunikationsartDtoI.getKommunikationsartsprDto() != null) {
				kommunikationsartDtoI.getKommunikationsartsprDto()
						.setKommunikatiosartCNr(kommunikationsartDtoI.getCNr());
				Kommunikationsartspr kommunikationsartspr = new Kommunikationsartspr(
						kommunikationsartDtoI.getCNr(),
						theClientDto.getLocUiAsString());
				em.persist(kommunikationsartspr);
				em.flush();
				setKommunikationsartsprFromKommunikationsartsprDto(
						kommunikationsartspr,
						kommunikationsartDtoI.getKommunikationsartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return kommunikationsart.getCNr();
	}

	public void removeKommunikationsart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNrI == null"));
		}

		Query query = em
				.createNamedQuery("KommunikationsartsprfindByKommunikationsartCNr");
		query.setParameter(1, cNrI);
		Collection<?> c = query.getResultList();
		try {
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Kommunikationsartspr item = (Kommunikationsartspr) iter.next();
				em.remove(item);
			}
			Kommunikationsart kommunikationsart = em.find(
					Kommunikationsart.class, cNrI);
			if (kommunikationsart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(kommunikationsart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kommunikationsartDtoI != null) {
			String cNr = kommunikationsartDtoI.getCNr();
			removeKommunikationsart(cNr, theClientDto);
		}
	}

	public void updateKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kommunikationsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"kommunikationsartDtoI == null"));
		}

		String cNr = kommunikationsartDtoI.getCNr();

		try {
			Kommunikationsart kommunikationsart = em.find(
					Kommunikationsart.class, cNr);
			if (kommunikationsart == null) { // @ToDo null Pruefung?
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (kommunikationsartDtoI.getKommunikationsartsprDto() != null
					&& kommunikationsartDtoI.getCNr() != null) {

				// upd
				Kommunikationsartspr kommunikationsartspr = em.find(
						Kommunikationsartspr.class, new KommunikationsartsprPK(
								theClientDto.getLocUiAsString(),
								kommunikationsartDtoI.getCNr()));

				if (kommunikationsartspr == null) {
					Kommunikationsartspr kommunikationsartsprneu = new Kommunikationsartspr(
							kommunikationsartDtoI.getCNr(),
							theClientDto.getLocUiAsString());
					kommunikationsartsprneu.setCBez(kommunikationsartDtoI
							.getKommunikationsartsprDto().getCBez());

					em.persist(kommunikationsartsprneu);
					em.flush();
				} else {
					setKommunikationsartsprFromKommunikationsartsprDto(
							kommunikationsartspr,
							kommunikationsartDtoI.getKommunikationsartsprDto());

				}

			}
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public KommunikationsartDto kommunikationsartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		KommunikationsartDto kommunikationsartDto = null;
		// try {
		Kommunikationsart kommunikationsart = em.find(Kommunikationsart.class,
				cNrI);
		if (kommunikationsart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		kommunikationsartDto = assembleKommunikationsartDto(kommunikationsart);

		try {
			Kommunikationsartspr kommunikationsartspr = em.find(
					Kommunikationsartspr.class, new KommunikationsartsprPK(
							theClientDto.getLocUiAsString(), cNrI));
			kommunikationsartDto
					.setKommunikationsartsprDto(assembleKommunikationsartsprDto(kommunikationsartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return kommunikationsartDto;
	}

	public KommunikationsartDto[] kommunikationsartFindAll()
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KommunikationsartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleKommunikationsartDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

	}

	private KommunikationsartDto assembleKommunikationsartDto(
			Kommunikationsart kommunikationsart) {
		return KommunikationsartDtoAssembler.createDto(kommunikationsart);
	}

	private KommunikationsartDto[] assembleKommunikationsartDtos(
			Collection<?> kommunikationsarts) {

		List<KommunikationsartDto> list = new ArrayList<KommunikationsartDto>();
		if (kommunikationsarts != null) {
			Iterator<?> iterator = kommunikationsarts.iterator();
			while (iterator.hasNext()) {
				Kommunikationsart kommunikationsart = (Kommunikationsart) iterator
						.next();
				list.add(assembleKommunikationsartDto(kommunikationsart));
			}
		}
		KommunikationsartDto[] returnArray = new KommunikationsartDto[list
				.size()];
		return (KommunikationsartDto[]) list.toArray(returnArray);
	}

	public KundeSelectCriteriaDto getSerienbriefSelektionsKriterien(
			Integer serienbriefIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		SerienbriefDto serienbriefDto = null;
		try {
			serienbriefDto = getPartnerServicesFac()
					.serienbriefFindByPrimaryKey(serienbriefIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		KundeSelectCriteriaDto alKundeSelectCriteriaDtoI = new KundeSelectCriteriaDto();

		alKundeSelectCriteriaDtoI.setCNrMandant(serienbriefDto.getMandantCNr());

		alKundeSelectCriteriaDtoI.setBAnsprechpartnerfktAuchOhne(Helper
				.short2boolean(serienbriefDto
						.getBAnsprechpartnerfunktionAuchOhne()));

		alKundeSelectCriteriaDtoI.setBInteressenten(Helper
				.short2boolean(serienbriefDto.getBGehtAnInteressenten()));

		alKundeSelectCriteriaDtoI.setBKunden(Helper
				.short2boolean(serienbriefDto.getBGehtAnKunden()));

		alKundeSelectCriteriaDtoI.setBVersteckt(Helper
				.short2boolean(serienbriefDto.getBVersteckteDabei()));

		alKundeSelectCriteriaDtoI.setIIdSerienbrief(serienbriefIId);

		alKundeSelectCriteriaDtoI.setIIdAnsprechpartnerfkt(serienbriefDto
				.getAnsprechpartnerfunktionIId());

		if (serienbriefDto.getCPlz() != null
				&& !serienbriefDto.getCPlz().equals("")) {
			alKundeSelectCriteriaDtoI.setSPLZ(serienbriefDto.getCPlz() + "%");
		} else {
			alKundeSelectCriteriaDtoI.setSPLZ(null);
		}

		if (serienbriefDto.getLandIId() != null) {

			alKundeSelectCriteriaDtoI.setSLKZ(getSystemFac()
					.landFindByPrimaryKey(serienbriefDto.getLandIId())
					.getCLkz());
		} else {
			alKundeSelectCriteriaDtoI.setSLKZ(null);
		}
		return alKundeSelectCriteriaDtoI;

	}

	public Map getAllKommunikationsArten(String cNrLocaleI,
			TheClientDto theClientDto) {

		Map<String, String> content = null;

		// try {
		Query query = em.createNamedQuery("KommunikationsartfindAll");
		Collection<?> allArten = query.getResultList();
		content = new TreeMap<String, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Kommunikationsart kommunikationsartTemp = (Kommunikationsart) iter
					.next();

			String key = kommunikationsartTemp.getCNr();
			String value = null;
			// try {
			Kommunikationsartspr kommunikationsartspr = em.find(
					Kommunikationsartspr.class, new KommunikationsartsprPK(
							cNrLocaleI, kommunikationsartTemp.getCNr()));
			if (kommunikationsartspr == null) {
				value = kommunikationsartTemp.getCNr();
			} else {
				value = kommunikationsartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// value = kommunikationsartTemp.getCNr();
			// }
			content.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return content;
	}

	public Integer createKontaktart(KontaktartDto kontaktartDto) {
		if (kontaktartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kontaktartDto == null"));
		}
		if (kontaktartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("kontaktartDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("KontaktartfindByCBez");
			query.setParameter(1, kontaktartDto.getCBez());
			Kontaktart doppelt = (Kontaktart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PART_KONTAKTART.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KONTAKTART);
			kontaktartDto.setIId(pk);

			Kontaktart kontaktart = new Kontaktart(kontaktartDto.getIId(),
					kontaktartDto.getCBez());
			em.persist(kontaktart);
			em.flush();
			setKontaktartFromKontaktartDto(kontaktart, kontaktartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return kontaktartDto.getIId();
	}

	public void removeKontaktart(KontaktartDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		// try {
		Integer iId = dto.getIId();
		Kontaktart toRemove = em.find(Kontaktart.class, iId);
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
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateKontaktart(KontaktartDto kontaktartDto) {
		if (kontaktartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kontaktartDto == null"));
		}
		if (kontaktartDto.getIId() == null || kontaktartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"kontaktartDto.getIId() == null || kontaktartDto.getCBez() == null"));
		}

		Integer iId = kontaktartDto.getIId();
		// try {
		Kontaktart kontaktart = em.find(Kontaktart.class, iId);
		if (kontaktart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("KontaktartfindByCBez");
			query.setParameter(1, kontaktartDto.getCBez());
			Integer iIdVorhanden = ((Kontaktart) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PART_KONTAKTART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setKontaktartFromKontaktartDto(kontaktart, kontaktartDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public KontaktartDto kontaktartFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Kontaktart kontaktart = em.find(Kontaktart.class, iId);
		if (kontaktart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKontaktartDto(kontaktart);

	}

	private void setKontaktartFromKontaktartDto(Kontaktart kontaktart,
			KontaktartDto kontaktartDto) {
		kontaktart.setCBez(kontaktartDto.getCBez());
		em.merge(kontaktart);
		em.flush();
	}

	private KontaktartDto assembleKontaktartDto(Kontaktart kontaktart) {
		return KontaktartDtoAssembler.createDto(kontaktart);
	}

	private KontaktartDto[] assembleKontaktartDtos(Collection<?> kontaktarts) {
		List<KontaktartDto> list = new ArrayList<KontaktartDto>();
		if (kontaktarts != null) {
			Iterator<?> iterator = kontaktarts.iterator();
			while (iterator.hasNext()) {
				Kontaktart aufnahmeart = (Kontaktart) iterator.next();
				list.add(assembleKontaktartDto(aufnahmeart));
			}
		}
		KontaktartDto[] returnArray = new KontaktartDto[list.size()];
		return (KontaktartDto[]) list.toArray(returnArray);
	}

	public Integer createBranche(BrancheDto brancheDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (brancheDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("brancheDtoI == null"));
		}

		Integer iId = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			iId = pkGen.getNextPrimaryKey(PKConst.PK_BRANCHE);
			brancheDtoI.setIId(iId);

			Branche branche = new Branche(brancheDtoI.getCNr(),
					brancheDtoI.getIId());
			em.persist(branche);
			em.flush();

			if (brancheDtoI.getBranchesprDto() != null) {
				Branchespr branchespr = new Branchespr(brancheDtoI.getIId(),
						theClientDto.getLocUiAsString());
				em.persist(branchespr);
				em.flush();
				setBranchesprFromBranchesprDto(branchespr,
						brancheDtoI.getBranchesprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public void removeBranche(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdI == null"));
		}

		try {
			Query query = em.createNamedQuery("BranchesprfindByBrancheIId");
			query.setParameter(1, iIdI);
			Collection<?> allSpr = query.getResultList();
			Iterator<?> iter = allSpr.iterator();
			while (iter.hasNext()) {
				Branchespr sprTemp = (Branchespr) iter.next();
				em.remove(sprTemp);
			}
			Branche branche = em.find(Branche.class, iIdI);
			em.remove(branche);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeBranche(BrancheDto brancheDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (brancheDtoI != null) {
			Integer iId = brancheDtoI.getIId();
			removeBranche(iId, theClientDto);
		}
	}

	public void updateBranche(BrancheDto brancheDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (brancheDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("brancheDtoI == null"));
		}

		Integer iId = brancheDtoI.getIId();
		try {
			Branche branche = em.find(Branche.class, iId);
			setBrancheFromBrancheDto(branche, brancheDtoI);

			if (brancheDtoI.getBranchesprDto() != null) {
				// -- upd oder create
				if (brancheDtoI.getBranchesprDto().getBrancheIId() == null) {
					// create
					// Key(teil) setzen.
					brancheDtoI.getBranchesprDto().setBrancheIId(iId);
					createBranchespr(brancheDtoI.getBranchesprDto(),
							theClientDto);
				} else {
					// upd
					updateBranchespr(brancheDtoI.getBranchesprDto(),
							theClientDto);
				}
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public BrancheDto brancheFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdI == null"));
		}

		BrancheDto brancheDto = null;
		// try {
		Branche branche = em.find(Branche.class, iIdI);
		if (branche == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		brancheDto = assembleBrancheDto(branche);

		try {
			Branchespr branchespr = em.find(Branchespr.class, new BranchesprPK(
					iIdI, theClientDto.getLocUiAsString()));
			if (branchespr != null) {
				BranchesprDto branchesprDto = assembleBranchesprDto(branchespr);
				brancheDto.setBranchesprDto(branchesprDto);
			}
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return brancheDto;
	}

	private void setBrancheFromBrancheDto(Branche branche,
			BrancheDto brancheDtoI) {
		branche.setCNr(brancheDtoI.getCNr());
		em.merge(branche);
		em.flush();
	}

	private BrancheDto assembleBrancheDto(Branche branche) {
		return BrancheDtoAssembler.createDto(branche);
	}

	public BranchesprPK createBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Branchespr branchespr = new Branchespr(
					branchesprDtoI.getBrancheIId(),
					branchesprDtoI.getLocaleCNr());
			em.persist(branchespr);
			em.flush();
			setBranchesprFromBranchesprDto(branchespr, branchesprDtoI);
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return null;
	}

	public void removeBranchespr(Integer brancheIId, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		BranchesprPK branchesprPK = new BranchesprPK();
		branchesprPK.setBrancheIId(brancheIId);
		branchesprPK.setLocaleCNr(localeCNrI);
		Branchespr toRemove = em.find(Branchespr.class, branchesprPK);
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

	public void removeBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (branchesprDtoI != null) {
			Integer brancheIId = branchesprDtoI.getBrancheIId();
			String localeCNr = branchesprDtoI.getLocaleCNr();
			removeBranchespr(brancheIId, localeCNr, theClientDto);
		}
	}

	public void updateBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (branchesprDtoI != null) {
			BranchesprPK branchesprPK = new BranchesprPK();
			branchesprPK.setBrancheIId(branchesprDtoI.getBrancheIId());
			branchesprPK.setLocaleCNr(branchesprDtoI.getLocaleCNr());
			// try {
			Branchespr branchespr = em.find(Branchespr.class, branchesprPK);
			if (branchespr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBranchesprFromBranchesprDto(branchespr, branchesprDtoI);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public BranchesprDto branchesprFindByPrimaryKey(Integer brancheIId,
			String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		BranchesprPK branchesprPK = new BranchesprPK();
		branchesprPK.setBrancheIId(brancheIId);
		branchesprPK.setLocaleCNr(localeCNrI);
		Branchespr branchespr = em.find(Branchespr.class, branchesprPK);
		if (branchespr == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBranchesprDto(branchespr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public BranchesprDto[] branchesprFindByBrancheIId(Integer iIdBrancheI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BranchesprfindByBrancheIId");
		query.setParameter(1, iIdBrancheI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleBranchesprDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setBranchesprFromBranchesprDto(Branchespr branchespr,
			BranchesprDto branchesprDtoI) {
		branchespr.setCBez(branchesprDtoI.getCBez());
		em.merge(branchespr);
		em.flush();
	}

	private void setKommunikationsartsprFromKommunikationsartsprDto(
			Kommunikationsartspr kommunikationsartspr,
			KommunikationsartsprDto kommunikationsartsprDtoI) {

		kommunikationsartspr.setCBez(kommunikationsartsprDtoI.getCBez());
		em.merge(kommunikationsartspr);
		em.flush();
	}

	private BranchesprDto assembleBranchesprDto(Branchespr branchespr) {
		return BranchesprDtoAssembler.createDto(branchespr);
	}

	private KommunikationsartsprDto assembleKommunikationsartsprDto(
			Kommunikationsartspr kommunikationsartspr) {
		return KommunikationsartsprDtoAssembler.createDto(kommunikationsartspr);
	}

	private BranchesprDto[] assembleBranchesprDtos(Collection<?> branchesprs) {
		List<BranchesprDto> list = new ArrayList<BranchesprDto>();
		if (branchesprs != null) {
			Iterator<?> iterator = branchesprs.iterator();
			while (iterator.hasNext()) {
				Branchespr branchespr = (Branchespr) iterator.next();
				list.add(assembleBranchesprDto(branchespr));
			}
		}
		BranchesprDto[] returnArray = new BranchesprDto[list.size()];
		return (BranchesprDto[]) list.toArray(returnArray);
	}

	/**
	 * F&uuml;r einen Beleg muss die Briefanrede localeabh&auml;ngig angezeigt werden
	 * k&ouml;nnen.
	 * 
	 * @param iIdAnsprechpartnerI
	 *            PK des Ansprechpartners, null erlaubt
	 * @param iIdPartnerI
	 *            PK des Partners, not null
	 * @param locBelegI
	 *            in diesem Locale soll die Anrede erscheinen, not null
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Briefanrede
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String getBriefanredeFuerBeleg(Integer iIdAnsprechpartnerI,
			Integer iIdPartnerI, Locale locBelegI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdPartnerI == null"));
		}
		if (locBelegI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("locBelegI == null"));
		}

		String sBriefanrede = null;
		try {
			if (iIdAnsprechpartnerI != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI,
								theClientDto);
				sBriefanrede = getPartnerFac().formatBriefAnrede(
						ansprechpartnerDto.getPartnerDto(), locBelegI,
						theClientDto);
			} else if (iIdPartnerI != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
				sBriefanrede = getPartnerFac().formatBriefAnrede(partnerDto,
						locBelegI, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return sBriefanrede;
	}

	public Integer createSelektion(SelektionDto selektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (selektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("selektionDtoI == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERSELEKTION);
		selektionDtoI.setIId(iId);

		Selektion selektion = null;
		try {
			selektion = new Selektion(selektionDtoI.getIId(),
					selektionDtoI.getCNr(), selektionDtoI.getMandantCNr());
			em.persist(selektion);
			em.flush();
			setSelektionFromSelektionDto(selektion, selektionDtoI);
			if (selektionDtoI.getSelektionsprDto() != null) {
				selektionDtoI.getSelektionsprDto().setSelektionIId(
						selektionDtoI.getIId());
				Selektionspr selektionspr = new Selektionspr(
						selektionDtoI.getIId(), theClientDto.getLocUiAsString());
				em.persist(selektionspr);
				em.flush();
				setSelektionsprFromSelektionsprDto(selektionspr,
						selektionDtoI.getSelektionsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return selektion.getIId();
	}

	public void removeSelektion(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iId == null"));
		}

		try {
			Query query = em.createNamedQuery("SelektionsprfindBySelektionIId");
			query.setParameter(1, iId);
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Selektionspr item = (Selektionspr) iter.next();
				em.remove(item);
			}
			Selektion selektion = em.find(Selektion.class, iId);
			if (selektion == null) { // @ToDo null Pruefung?
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(selektion);
			em.flush();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateSelektion(SelektionDto selektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Validator.notNull(selektionDtoI, "selektionDtoI") ;

		Integer iId = selektionDtoI.getIId();
		try {
			Selektion selektion = em.find(Selektion.class, iId);
			if (selektion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (selektionDtoI.getSelektionsprDto() != null) {
				// -- upd oder create
				if (selektionDtoI.getSelektionsprDto().getSelektionIId() == null) {
					// create
					// Key(teil) setzen.
					selektionDtoI.getSelektionsprDto().setSelektionIId(
							selektionDtoI.getIId());

					Selektionspr selektionspr = new Selektionspr(
							selektionDtoI.getIId(),
							theClientDto.getLocUiAsString());
					em.persist(selektionspr);
					em.flush();

					setSelektionsprFromSelektionsprDto(selektionspr,
							selektionDtoI.getSelektionsprDto());
				} else {
					// upd
					Selektionspr selektionspr = em.find(Selektionspr.class,
							new SelektionsprPK(selektionDtoI.getIId(),
									theClientDto.getLocUiAsString()));

					setSelektionsprFromSelektionsprDto(selektionspr,
							selektionDtoI.getSelektionsprDto());
				}
			}
			setSelektionFromSelektionDto(selektion, selektionDtoI) ;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public SelektionDto selektionFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		SelektionDto selektionDto = null;
		// try {
		Selektion selektion = em.find(Selektion.class, iId);
		if (selektion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		selektionDto = assembleSelektionDto(selektion);

		try {
			Selektionspr selektionspr = em.find(Selektionspr.class,
					new SelektionsprPK(iId, theClientDto.getLocUiAsString()));
			selektionDto
					.setSelektionsprDto(assembleSelektionsprDto(selektionspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return selektionDto;
	}

	public SelektionDto selektionFindByCNrMandantCNr(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		SelektionDto selektionDto = null;

		try {

			Query query = em.createNamedQuery("SelektionfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, theClientDto.getMandant());

			Selektion selektion = (Selektion) query.getSingleResult();

			selektionDto = assembleSelektionDto(selektion);

		} catch (NoResultException ex) {
			// nothing here
		}

		return selektionDto;
	}

	private void setSelektionFromSelektionDto(Selektion selektion,
			SelektionDto selektionDto) {
		selektion.setCNr(selektionDto.getCNr());
		selektion.setMandantCNr(selektionDto.getMandantCNr());
		selektion.setbWebshop(selektionDto.getbWebshop()) ;
		
		em.merge(selektion);
		em.flush();
	}

	private SelektionDto assembleSelektionDto(Selektion selektion) {
		return SelektionDtoAssembler.createDto(selektion);
	}

	public Integer createSelektionspr(SelektionsprDto selektionsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Selektionspr selektionspr = new Selektionspr(
					selektionsprDto.getSelektionIId(),
					selektionsprDto.getLocaleCNr());
			em.persist(selektionspr);
			em.flush();
			setSelektionsprFromSelektionsprDto(selektionspr, selektionsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
		return null;
	}

	public void removeSelektionspr(Integer selektionIId, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		SelektionsprPK selektionPK = new SelektionsprPK();
		selektionPK.setSelektionIId(selektionIId);
		selektionPK.setLocaleCNr(localeCNrI);
		Selektionspr toRemove = em.find(Selektionspr.class, selektionPK);
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

	public void updateSelektionspr(SelektionsprDto selektionsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (selektionsprDto != null) {
			SelektionsprPK selektionsprPK = new SelektionsprPK();
			selektionsprPK.setSelektionIId(selektionsprDto.getSelektionIId());
			selektionsprPK.setLocaleCNr(selektionsprDto.getLocaleCNr());
			try {
				Selektionspr selektionspr = em.find(Selektionspr.class,
						selektionsprPK);
				setSelektionsprFromSelektionsprDto(selektionspr,
						selektionsprDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(e);
			}
		}
	}

	public SelektionsprDto selektionsprFindByPrimaryKey(Integer selektionIId,
			String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			SelektionsprPK selektionsprPK = new SelektionsprPK();
			selektionsprPK.setSelektionIId(selektionIId);
			selektionsprPK.setLocaleCNr(localeCNr);
			Selektionspr selektionspr = em.find(Selektionspr.class,
					selektionsprPK);
			if (selektionspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleSelektionsprDto(selektionspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	private void setSelektionsprFromSelektionsprDto(Selektionspr selektionspr,
			SelektionsprDto selektionsprDto) {
		selektionspr.setCBez(selektionsprDto.getCBez());
		em.merge(selektionspr);
		em.flush();
	}

	private SelektionsprDto assembleSelektionsprDto(Selektionspr selektionspr) {
		return SelektionsprDtoAssembler.createDto(selektionspr);
	}

	public void updateSerienbriefMailtext(SerienbriefDto serienbriefDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Serienbrief stueckliste = em.find(Serienbrief.class,
				serienbriefDto.getIId());
		stueckliste.setXMailtext(serienbriefDto.getXMailtext());
	}

	public Integer createSerienbrief(SerienbriefDto serienbriefDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (serienbriefDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("serienbriefDto == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERSERIENBRIEF);
		serienbriefDto.setIId(iId);

		// befuelle Felder am Server.
		serienbriefDto.setMandantCNr(theClientDto.getMandant());
		serienbriefDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		serienbriefDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// die ts anlegen, aendern nur am server
		serienbriefDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		serienbriefDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		Serienbrief serienbrief = null;
		try {
			serienbrief = new Serienbrief(serienbriefDto.getIId(),
					serienbriefDto.getCBez(), serienbriefDto.getMandantCNr(),
					serienbriefDto.getPersonalIIdAnlegen(),
					serienbriefDto.getPersonalIIdAendern(),
					serienbriefDto.getBGehtAnKunden(),
					serienbriefDto.getBGehtAnInteressenten(),
					serienbriefDto.getBVersteckteDabei(),
					serienbriefDto.getBAnsprechpartnerfunktionAuchOhne(),
					serienbriefDto.getBGehtanlieferanten(),
					serienbriefDto.getBGehtanmoeglichelieferanten(),
					serienbriefDto.getBGehtanpartner(),
					serienbriefDto.getBMitzugeordnetenfirmen(), 
					Helper.boolean2Short(serienbriefDto.isNewsletter()));
			em.persist(serienbrief);
			em.flush();
			setSerienbriefFromSerienbriefDto(serienbrief, serienbriefDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return serienbrief.getIId();
	}

	public void removeSerienbrief(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdI == null"));
		}

		Serienbrief toRemove = em.find(Serienbrief.class, iIdI);
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

	public void updateSerienbrief(SerienbriefDto serienbriefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (serienbriefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"serienbriefDtoI == null"));
		}

		Integer iId = serienbriefDtoI.getIId();
		// try {
		Serienbrief serienbrief = em.find(Serienbrief.class, iId);
		if (serienbrief == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSerienbriefFromSerienbriefDto(serienbrief, serienbriefDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public SerienbriefDto serienbriefFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdI == null"));
		}

		// try {
		Serienbrief serienbrief = em.find(Serienbrief.class, iIdI);
		if (serienbrief == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSerienbriefDto(serienbrief);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setSerienbriefFromSerienbriefDto(Serienbrief serienbrief,
			SerienbriefDto serienbriefDto) {
		serienbrief.setCBez(serienbriefDto.getCBez());
		serienbrief.setCPlz(serienbriefDto.getCPlz());
		serienbrief.setMandantCNr(serienbriefDto.getMandantCNr());
		serienbrief.setTAnlegen(serienbriefDto.getTAnlegen());
		serienbrief.setPersonalIIdAnlegen(serienbriefDto
				.getPersonalIIdAnlegen());
		serienbrief.setTAendern(serienbriefDto.getTAendern());
		serienbrief.setPersonalIIdAendern(serienbriefDto
				.getPersonalIIdAendern());
		serienbrief.setBGehtankunden(serienbriefDto.getBGehtAnKunden());
		serienbrief.setBGehtaninteressenten(serienbriefDto
				.getBGehtAnInteressenten());
		serienbrief.setBGehtanlieferanten(serienbriefDto
				.getBGehtanlieferanten());
		serienbrief.setBGehtanpartner(serienbriefDto.getBGehtanpartner());
		serienbrief.setBGehtanmoeglichelieferanten(serienbriefDto
				.getBGehtanmoeglichelieferanten());
		serienbrief.setBVerstecktedabei(serienbriefDto.getBVersteckteDabei());
		serienbrief.setAnsprechpartnerfunktionIId(serienbriefDto
				.getAnsprechpartnerfunktionIId());
		serienbrief.setBAnsprechpartnerfunktionauchohne(serienbriefDto
				.getBAnsprechpartnerfunktionAuchOhne());
		serienbrief.setCBetreff(serienbriefDto.getSBetreff());
		serienbrief.setXText(serienbriefDto.getSXText());
		serienbrief.setLandIId(serienbriefDto.getLandIId());
		serienbrief.setNAbumsatz(serienbriefDto.getNAbumsatz());
		serienbrief.setNBisumsatz(serienbriefDto.getNBisumsatz());
		serienbrief.setTUmsatzab(serienbriefDto.getTUmsatzab());
		serienbrief.setTUmsatzbis(serienbriefDto.getTUmsatzbis());
		serienbrief.setBMitzugeordnetenfirmen(serienbriefDto
				.getBMitzugeordnetenfirmen());
		serienbrief.setBrancheIId(serienbriefDto.getBrancheIId());
		serienbrief.setPartnerklasseIId(serienbriefDto.getPartnerklasseIId());
		serienbrief.setXMailtext(serienbriefDto.getXMailtext());
		serienbrief.setbNewsletter(Helper.boolean2Short(serienbriefDto.isNewsletter()));

		em.merge(serienbrief);
		em.flush();
	}

	private SerienbriefDto assembleSerienbriefDto(Serienbrief serienbrief) {
		return SerienbriefDtoAssembler.createDto(serienbrief);
	}

	public SerienbriefselektionPK createSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (serienbriefselektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("serienbriefselektionDtoI == null"));
		}

		Serienbriefselektion serienbriefselektion = null;
		try {
			serienbriefselektion = new Serienbriefselektion(
					serienbriefselektionDtoI.getSerienbriefIId(),
					serienbriefselektionDtoI.getSelektionIId());
			em.persist(serienbriefselektion);
			em.flush();
			setSerienbriefselektionFromSerienbriefselektionDto(
					serienbriefselektion, serienbriefselektionDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new SerienbriefselektionPK(serienbriefselektion.getPk()
				.getSerienbriefIId(), serienbriefselektion.getPk()
				.getSelektionIId());
	}

	public void removeSerienbriefselektion(Integer serienbriefIIdI,
			Integer selektionIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"selektionIIdI == null"));
		}

		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionPK.setSelektionIId(selektionIIdI);
		Serienbriefselektion toRemove = em.find(Serienbriefselektion.class,
				serienbriefselektionPK);
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

	public void updateSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (serienbriefselektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"serienbriefselektionDtoI == null"));
		}

		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefselektionDtoI
				.getSerienbriefIId());
		serienbriefselektionPK.setSelektionIId(serienbriefselektionDtoI
				.getSelektionIId());
		// try {
		Serienbriefselektion serienbriefselektion = em.find(
				Serienbriefselektion.class, serienbriefselektionPK);
		if (serienbriefselektion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSerienbriefselektionFromSerienbriefselektionDto(
				serienbriefselektion, serienbriefselektionDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public SerienbriefselektionDto[] serienbriefselektionFindBySerienbriefIId(
			Integer serienbriefIId) {
		if (serienbriefIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"serienbriefIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("SerienbriefselektionfindBySerienbriefIId");
		query.setParameter(1, serienbriefIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleSerienbriefselektionDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public SerienbriefselektionDto serienbriefselektionFindByPrimaryKey(
			Integer serienbriefIIdI, Integer selektionIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition

		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"selektionIIdI == null"));
		}

		// try {
		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionPK.setSelektionIId(selektionIIdI);
		Serienbriefselektion serienbriefselektion = em.find(
				Serienbriefselektion.class, serienbriefselektionPK);
		if (serienbriefselektion == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSerienbriefselektionDto(serienbriefselektion);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setSerienbriefselektionFromSerienbriefselektionDto(
			Serienbriefselektion serienbriefselektion,
			SerienbriefselektionDto serienbriefselektionDto) {
		serienbriefselektion.setCBemerkung(serienbriefselektionDto
				.getSBemerkung());
		em.merge(serienbriefselektion);
		em.flush();
	}

	private SerienbriefselektionDto assembleSerienbriefselektionDto(
			Serienbriefselektion serienbriefselektion) {
		return SerienbriefselektionDtoAssembler.createDto(serienbriefselektion);
	}

	private SerienbriefselektionDto[] assembleSerienbriefselektionDtos(
			Collection<?> branchesprs) {
		List<SerienbriefselektionDto> list = new ArrayList<SerienbriefselektionDto>();
		if (branchesprs != null) {
			Iterator<?> iterator = branchesprs.iterator();
			while (iterator.hasNext()) {
				Serienbriefselektion serienbriefselektion = (Serienbriefselektion) iterator
						.next();
				list.add(assembleSerienbriefselektionDto(serienbriefselektion));
			}
		}
		SerienbriefselektionDto[] returnArray = new SerienbriefselektionDto[list
				.size()];
		return (SerienbriefselektionDto[]) list.toArray(returnArray);
	}

}
