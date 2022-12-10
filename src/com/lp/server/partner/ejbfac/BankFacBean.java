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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.partner.ejb.Bank;
import com.lp.server.partner.ejb.BankQuery;
import com.lp.server.partner.ejb.Partnerbank;
import com.lp.server.partner.ejb.PartnerbankQuery;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BankDtoAssembler;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.partner.service.PartnerbankDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;

@Stateless
public class BankFacBean extends Facade implements BankFac {

	@PersistenceContext
	private EntityManager em;

	public Integer createPartnerbank(PartnerbankDto partnerbankDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerbankDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerbankDtoI == null"));
		}

		Partnerbank partnerbank = null;
		try {
			Query query = em.createNamedQuery("PartnerbankfindByPartnerIIdBankPartnerIId");
			query.setParameter(1, partnerbankDtoI.getPartnerIId());
			query.setParameter(2, partnerbankDtoI.getBankPartnerIId());
			query.setParameter(3, partnerbankDtoI.getCKtonr());
			partnerbank = (com.lp.server.partner.ejb.Partnerbank) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
		} catch (NoResultException e) {

		}
		Integer iIdBank = null;
		try {
			// Partnerbank.

			// Generieren eines PK.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iIdBank = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERBANK);
			partnerbankDtoI.setIId(iIdBank);

			partnerbank = new Partnerbank(partnerbankDtoI.getIId(), partnerbankDtoI.getPartnerIId(),
					partnerbankDtoI.getBankPartnerIId(), partnerbankDtoI.getCKtonr(), partnerbankDtoI.getISort());
			em.persist(partnerbank);
			em.flush();
			setPartnerbankFromPartnerbankDto(partnerbank, partnerbankDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iIdBank;
	}

	public Integer getMaxISort(Integer iIdPartnerI) {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("PartnerbankejbSelectMaxISort");
			query.setParameter(1, iIdPartnerI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, e);
		}
		return iiMaxISortO;
	}

	public void removePartnerbank(PartnerbankDto partnerbankDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (partnerbankDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("kundeDto == null"));
		}

		// try {
		Partnerbank toRemove = em.find(Partnerbank.class, partnerbankDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updatePartnerbank(PartnerbankDto partnerbankDto) throws EJBExceptionLP {
		if (partnerbankDto != null) {
			Integer iId = partnerbankDto.getIId();
			// try {
			Partnerbank partnerbank = em.find(Partnerbank.class, partnerbankDto.getIId());
			if (partnerbank == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPartnerbankFromPartnerbankDto(partnerbank, partnerbankDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public PartnerbankDto partnerbankFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		PartnerbankDto partnerbankDto = null;
		// Partnerbank.
		Partnerbank partnerbank = em.find(Partnerbank.class, iIdI);
		if (partnerbank == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		partnerbankDto = assemblePartnerbankDto(partnerbank);

		return partnerbankDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public PartnerbankDto[] partnerbankFindByPartnerIId(Integer partnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("PartnerbankfindByPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		// }
		PartnerbankDto[] partnerbankDto = new PartnerbankDto[c.size()];
		int i = 0;
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Partnerbank item = (Partnerbank) iter.next();
			partnerbankDto[i] = assemblePartnerbankDto(item);
			i++;
		}
		return partnerbankDto;
	}
	public PartnerbankDto[] partnerbankfindByPartnerbankIIdCKtonr(Integer partnerbankIId, String cktonr, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("PartnerbankfindByPartnerbankIIdCKtonr");
		query.setParameter(1, partnerbankIId);
		query.setParameter(2, cktonr);
		Collection<?> c = query.getResultList();
		

		PartnerbankDto[] partnerbankDto = new PartnerbankDto[c.size()];
		int i = 0;
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Partnerbank item = (Partnerbank) iter.next();
			partnerbankDto[i] = assemblePartnerbankDto(item);
			i++;
		}
		return partnerbankDto;
	}
	
	
	
	public PartnerbankDto[] partnerbankFindByPartnerIIdOhneExc(Integer partnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		PartnerbankDto[] partnerbankDtos = null;
		Query query = em.createNamedQuery("PartnerbankfindByPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		partnerbankDtos = new PartnerbankDto[c.size()];
		int i = 0;
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Partnerbank item = (Partnerbank) iter.next();
			partnerbankDtos[i] = assemblePartnerbankDto(item);
			i++;
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return partnerbankDtos;
	}

	public PartnerbankDto[] partnerbankFindByPartnerBankIId(Integer partnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		PartnerbankDto[] partnerbankDtos = null;
		Query query = em.createNamedQuery("PartnerbankfindByBankPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		partnerbankDtos = assemblePartnerbankDtos(cl);
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return partnerbankDtos;
	}

	public PartnerbankDto[] partnerbankFindByPartnerBankIIdOhneExc(Integer partnerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		PartnerbankDto[] partnerbankDtos = null;
		Query query = em.createNamedQuery("PartnerbankfindByBankPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> partnerbanks = query.getResultList();
		// if (partnerbanks.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		partnerbankDtos = assemblePartnerbankDtos(partnerbanks);
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return partnerbankDtos;
	}

	private void setPartnerbankFromPartnerbankDto(Partnerbank partnerbank, PartnerbankDto partnerbankDto) {
		partnerbank.setCIban(partnerbankDto.getCIban());
		partnerbank.setPartnerbankIId(partnerbankDto.getBankPartnerIId());
		partnerbank.setCKtonr(partnerbankDto.getCKtonr());
		partnerbank.setPartnerIId(partnerbankDto.getPartnerIId());
		partnerbank.setISort(partnerbankDto.getISort());
		partnerbank.setTSepaerteilt(partnerbankDto.getTSepaerteilt());
		partnerbank.setCSepamandatsnummer(partnerbankDto.getCSepamandatsnummer());
		partnerbank.setCEsr(partnerbankDto.getCEsr());
		partnerbank.setWaehrungCNr(partnerbankDto.getWaehrungCNr());
		em.merge(partnerbank);
		em.flush();
	}

	private PartnerbankDto assemblePartnerbankDto(Partnerbank partnerbank) {
		return PartnerbankDtoAssembler.createDto(partnerbank);
	}

	private PartnerbankDto[] assemblePartnerbankDtos(Collection<?> partnerbanks) {
		List<PartnerbankDto> list = new ArrayList<PartnerbankDto>();
		if (partnerbanks != null) {
			Iterator<?> iterator = partnerbanks.iterator();
			while (iterator.hasNext()) {
				Partnerbank partnerbank = (Partnerbank) iterator.next();
				list.add(assemblePartnerbankDto(partnerbank));
			}
		}
		PartnerbankDto[] returnArray = new PartnerbankDto[list.size()];
		return (PartnerbankDto[]) list.toArray(returnArray);
	}

	public Integer createBank(BankDto bankDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (bankDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("bankDtoI == null"));
		}
		if (bankDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bankDtoI.getPartnerDto() == null"));
		}

		Integer iIdPartnerAusNeu = null;
		try {
			// 1. Partner.
			iIdPartnerAusNeu = bankDtoI.getPartnerIIdNeuAus();
			if (iIdPartnerAusNeu != null) {
				bankDtoI.getPartnerDto().setIId(iIdPartnerAusNeu);
				getPartnerFac().updatePartner(bankDtoI.getPartnerDto(), theClientDto);
			} else {
				iIdPartnerAusNeu = getPartnerFac().createPartner(bankDtoI.getPartnerDto(), theClientDto);
			}

			// verbinde Partner mit Kunden
			bankDtoI.setPartnerIId(iIdPartnerAusNeu);

			// Partner lesen wegen generierter Daten.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerAusNeu, theClientDto);
			bankDtoI.setPartnerDto(partnerDto);

			// 2. Bank.
			bankDtoI.setPartnerIId(iIdPartnerAusNeu);

			Bank bank = new Bank(bankDtoI.getPartnerIId());
			em.persist(bank);
			em.flush();
			setBankFromBankDto(bank, bankDtoI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		HvDtoLogger<BankDto> logger = new HvDtoLogger<BankDto>(em, theClientDto);
		logger.logInsert(bankDtoI);
		return iIdPartnerAusNeu;
	}

	/**
	 * @param partnerIIdI  Integer; in diesem Fall der PK der Bank.
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void removeBank(Integer partnerIIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("partnerDtoI.getIId()"));
		}

		Bank toRemove = em.find(Bank.class, partnerIIdI);
		BankDto bankDto = assembleBankDto(toRemove);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			HvDtoLogger<BankDto> partnerLogger = new HvDtoLogger<BankDto>(em, bankDto.getIId(), theClientDto);
			partnerLogger.logDelete(bankDto);

			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateBank(BankDto bankDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (bankDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("bankDtoI == null"));
		}
		if (bankDtoI.getPartnerIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("bankDtoI.getPartnerIId() == null"));
		}

		Integer partnerIId = bankDtoI.getPartnerIId();
		try {
			// Partner.
			getPartnerFac().updatePartner(bankDtoI.getPartnerDto(), theClientDto);

			// Bank.
			Bank bank = em.find(Bank.class, partnerIId);

			BankDto dtoVorher = assembleBankDto(bank);

			if (bank == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBankFromBankDto(bank, bankDtoI);

			HvDtoLogger<BankDto> artikelLogger = new HvDtoLogger<BankDto>(em, bank.getPartnerIId(), theClientDto);
			artikelLogger.log(dtoVorher, bankDtoI);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public BankDto bankFindByPrimaryKey(Integer partnerIIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		BankDto bankDto = bankFindByPrimaryKeyOhneExc(partnerIIdI, theClientDto);

		if (bankDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return bankDto;
	}

	public BankDto bankFindByPrimaryKeyOhneExc(Integer partnerIIdI, TheClientDto theClientDto) {

		BankDto bankDto = null;

		// Bank.
		Bank bank = em.find(Bank.class, partnerIIdI);
		if (bank == null) {
			return null;
		}

		bankDto = assembleBankDto(bank);

		// Partner.
		bankDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(bankDto.getPartnerIId(), theClientDto));

		return bankDto;
	}

	private void setBankFromBankDto(Bank bank, BankDto bankDto) {
		bank.setCBlz(bankDto.getCBlz());
		bank.setCBic(bankDto.getCBic());
		
		em.merge(bank);
		em.flush();
	}

	private BankDto assembleBankDto(Bank bank) {
		return BankDtoAssembler.createDto(bank);
	}

	@Override
	public List<BankDto> bankFindByBIC(String bic, TheClientDto theClientDto) {
		Validator.notNull(bic, "BIC");
		Validator.notNull(theClientDto, "theClientDto");

		List<Bank> banken = BankQuery.listByBIC(em, bic);

		List<BankDto> bankDtos = new ArrayList<BankDto>();
		for (Bank bank : banken) {
			bankDtos.add(assembleBankDto(bank));
		}

		return bankDtos;
	}
	
	public List<PartnerbankDto> partnerbankFindByESRUndKontonummer(String esr,String kontonummer, TheClientDto theClientDto) {
		List<Partnerbank> banken = PartnerbankQuery.listByESRUndKontonummer(em, esr,kontonummer);

		List<PartnerbankDto> bankDtos = new ArrayList<PartnerbankDto>();
		for (Partnerbank bank : banken) {
			bankDtos.add(assemblePartnerbankDto(bank));
		}

		return bankDtos;
	}
	

}
