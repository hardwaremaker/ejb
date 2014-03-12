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
package com.lp.server.finanz.ejbfac;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.hibernate.Session;

import com.lp.server.finanz.ejb.Bankverbindung;
import com.lp.server.finanz.ejb.Ergebnisgruppe;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Kassenbuch;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Kontolaenderart;
import com.lp.server.finanz.ejb.KontolaenderartPK;
import com.lp.server.finanz.ejb.Kontoland;
import com.lp.server.finanz.ejb.KontolandPK;
import com.lp.server.finanz.ejb.Rechenregel;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BankverbindungDtoAssembler;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.ErgebnisgruppeDtoAssembler;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.FinanzamtDtoAssembler;
import com.lp.server.finanz.service.IKontoImporterBeanServices;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KassenbuchDtoAssembler;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoAssembler;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoDtoSmallAssembler;
import com.lp.server.finanz.service.KontoImporter;
import com.lp.server.finanz.service.KontoImporterBeanService;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.KontolaenderartDtoAssembler;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.finanz.service.KontolandDtoAssembler;
import com.lp.server.finanz.service.RechenregelDto;
import com.lp.server.finanz.service.RechenregelDtoAssembler;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FinanzFacBean extends Facade implements FinanzFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Neuanlegen eines Kontos.
	 * 
	 * @param kontoDto
	 *            KontoDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 * @throws RemoteException
	 */
	public KontoDto createKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// Schaun, ob schon ein Konto mit dieser Nummer fuer diesen Kontotyp
		// angelegt ist.
		Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
		query.setParameter(1, kontoDto.getCNr());
		query.setParameter(2, kontoDto.getKontotypCNr());
		query.setParameter(3, kontoDto.getMandantCNr());
		try {
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception());
		} catch (NoResultException e1) {
			//
		}
		// pruefung auf Zykel nicht erforderlich
		// generieren von primary key
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_KONTO);
		kontoDto.setIId(pk);

		// wer hat angelegt
		kontoDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kontoDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		// wenn nicht anders angegeben, ist das konto ab heute gueltig
		if (kontoDto.getDGueltigvon() == null) {
			kontoDto.setDGueltigvon(new java.sql.Date(System
					.currentTimeMillis()));
		}
		// wenn nicht anders angegeben, ist das konto nicht versteckt
		if (kontoDto.getBVersteckt() == null) {
			kontoDto.setBVersteckt(Helper.boolean2Short(false));
		}
		try {
			Konto konto = new Konto(kontoDto.getIId(),
					kontoDto.getMandantCNr(), kontoDto.getCNr(),
					kontoDto.getCBez(), kontoDto.getUvaartIId(),
					kontoDto.getBAutomeroeffnungsbuchung(),
					kontoDto.getBAllgemeinsichtbar(),
					kontoDto.getBManuellbebuchbar(), kontoDto.getKontoartCNr(),
					kontoDto.getKontotypCNr(),
					kontoDto.getPersonalIIdAnlegen(),
					kontoDto.getPersonalIIdAendern());
			em.persist(konto);
			em.flush();
			kontoDto.setTAendern(konto.getTAendern());
			kontoDto.setTAnlegen(konto.getTAnlegen());
			setKontoFromKontoDto(konto, kontoDto);
			return kontoFindByPrimaryKey(konto.getIId());
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Loeschen eines Kontos.
	 * 
	 * @param kontoDto
	 *            KontoDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (kontoDto != null) {
			Integer iId = kontoDto.getIId();
			Konto toRemove = em.find(Konto.class, iId);
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
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kontoDto != null"));
		}
	}

	/**
	 * Update eines Kontos
	 * 
	 * @param kontoDto
	 *            KontoDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDto updateKonto(KontoDto kontoDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (kontoDto != null) {
			// Auf Zykel preufen
			pruefeZykel(kontoDto);
			// Daten der Aenderung
			kontoDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			kontoDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			Integer iId = kontoDto.getIId();
			try {
				Konto konto = em.find(Konto.class, iId);
				setKontoFromKontoDto(konto, kontoDto);
				return kontoFindByPrimaryKey(konto.getIId());
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("Dto is null"));
		}
	}

	/**
	 * Konto anhand Primaerschluessel finden
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDto kontoFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		try {
			Konto konto = em.find(Konto.class, iId);
			if (konto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			KontoDto ktoDto = assembleKontoDto(konto);
			if (konto.getKontotypCNr()
					.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {

				KundeDto[] kdDto = getKundeFac()
						.kundefindByKontoIIdDebitorenkonto(konto.getIId());
				if (kdDto != null && kdDto.length > 0) {
					Partner p = em
							.find(Partner.class, kdDto[0].getPartnerIId());

					ktoDto.setPartnerDto(PartnerDtoAssembler.createDto(p));
				}
			} else if (konto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_KREDITOR)) {
				LieferantDto[] lDto = getLieferantFac()
						.lieferantfindByKontoIIdKreditorenkonto(konto.getIId());
				if (lDto != null && lDto.length > 0) {
					Partner p = em.find(Partner.class, lDto[0].getPartnerIId());

					ktoDto.setPartnerDto(PartnerDtoAssembler.createDto(p));
				}
			}

			return ktoDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	/**
	 * Konto anhand Primaerschluessel finden
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDtoSmall kontoFindByPrimaryKeySmall(Integer iId)
			throws EJBExceptionLP {
		// try {
		Konto konto = em.find(Konto.class, iId);
		if (konto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKontoDtoSmall(konto);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Finden eines Kontos anhand Kontonummer und Mandant
	 * 
	 * @param sCnr
	 *            String
	 * @param sMandant
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDto kontoFindByCnrMandant(String sCnr, String sMandant,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("KontofindByCNrMandant");
			query.setParameter(1, sCnr);
			query.setParameter(2, sMandant);
			// @todo getSingleResult oder getResultList ?
			return assembleKontoDto((Konto) query.getSingleResult());
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	/***
	 * KontoDto auf Entity setzen.** @param konto Konto* @param kontoDto
	 * KontoDto
	 */
	private void setKontoFromKontoDto(Konto konto, KontoDto kontoDto) {
		konto.setMandantCNr(kontoDto.getMandantCNr());
		konto.setCNr(kontoDto.getCNr());
		konto.setCBez(kontoDto.getCBez());
		konto.setKontoIIdWeiterfuehrendust(kontoDto
				.getKontoIIdWeiterfuehrendUst());
		konto.setRechenregelCNrWeiterfuehrendust(kontoDto
				.getRechenregelCNrWeiterfuehrendUst());
		konto.setRechenregelCNrWeiterfuehrendbilanz(kontoDto
				.getRechenregelCNrWeiterfuehrendBilanz());
		konto.setKontoIIdWeiterfuehrendskonto(kontoDto
				.getKontoIIdWeiterfuehrendSkonto());
		konto.setRechenregelCNrWeiterfuehrendskonto(kontoDto
				.getRechenregelCNrWeiterfuehrendSkonto());
		konto.setUvaartIId(kontoDto.getUvaartIId());
		konto.setTGueltigvon(kontoDto.getDGueltigvon());
		konto.setTGueltigbis(kontoDto.getDGueltigbis());
		konto.setFinanzamtIId(kontoDto.getFinanzamtIId());
		konto.setKostenstelleIId(kontoDto.getKostenstelleIId());
		konto.setBAutomeroeffnungsbuchung(kontoDto
				.getBAutomeroeffnungsbuchung());
		konto.setBAllgemeinsichtbar(kontoDto.getBAllgemeinsichtbar());
		konto.setBManuellbebuchbar(kontoDto.getBManuellbebuchbar());
		konto.setKontoartCNr(kontoDto.getKontoartCNr());
		konto.setKontotypCNr(kontoDto.getKontotypCNr());
		konto.setErgebnisgruppeIId(kontoDto.getErgebnisgruppeIId());
		konto.setTAnlegen(kontoDto.getTAnlegen());
		konto.setPersonalIIdAnlegen(kontoDto.getPersonalIIdAnlegen());
		konto.setTAendern(kontoDto.getTAendern());
		konto.setPersonalIIdAendern(kontoDto.getPersonalIIdAendern());
		konto.setBVersteckt(kontoDto.getBVersteckt());
		konto.setSteuerkategorieIId(kontoDto.getSteuerkategorieIId());
		konto.setSteuerkategorieIIdReverse(kontoDto
				.getSteuerkategorieIIdReverse());
		konto.setcSortierung(kontoDto.getCsortierung());
		konto.setXBemerkung(kontoDto.getxBemerkung());
		konto.setWaehrungCNrDruck(kontoDto.getWaehrungCNrDruck());
		// Diese beiden Felder werden mit einer eigenen Methode befuellt. um
		// Lost Updates zu verhindern
		// konto.setCLetzteSortierung(kontoDto.getCLetztesortierung());
		// konto.setILetzteselektiertebuchung(kontoDto.
		// getILetzteselektiertebuchung());
		konto.setiGeschaeftsjahrEB(kontoDto.getiGeschaeftsjahrEB()) ;
		konto.setTEBAnlegen(kontoDto.gettEBAnlegen()) ;
		konto.setErgebnisgruppeIId_negativ(kontoDto.getErgebnisgruppeIId_negativ());
		konto.setBOhneUst(kontoDto.getBOhneUst());
		
		em.merge(konto);
		em.flush();
	}

	public void updateKontoDtoUIDaten(Integer kontoIIdI,
			String cLetztesortierungI, Integer iLetzteselektiertebuchungI)
			throws EJBExceptionLP {
		Konto konto = em.find(Konto.class, kontoIIdI);
		if (konto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		konto.setCLetztesortierung(cLetztesortierungI);
		konto.setILetzteselektiertebuchung(iLetzteselektiertebuchungI);

	}

	private KontoDto assembleKontoDto(Konto konto) {
		return KontoDtoAssembler.createDto(konto);
	}

	private KontoDtoSmall assembleKontoDtoSmall(Konto konto) {
		return KontoDtoSmallAssembler.createDto(konto);
	}

	private KontoDto[] assembleKontoDtos(Collection<?> kontos) {
		return KontoDtoAssembler.createDtos(kontos);
	}

	public int getAnzahlDerFinanzaemter(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("FinanzamtfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return c.size();
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public FinanzamtDto createFinanzamt(FinanzamtDto finanzamtDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		finanzamtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		finanzamtDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		finanzamtDto.setMandantCNr(theClientDto.getMandant());
		try {
			Integer partnerIId = getPartnerFac().createPartner(
					finanzamtDto.getPartnerDto(), theClientDto);
			finanzamtDto.setPartnerIId(partnerIId);
			Finanzamt finanzamt = new Finanzamt(finanzamtDto.getPartnerIId(),
					finanzamtDto.getMandantCNr(), Helper.boolean2Short(false),
					finanzamtDto.getPersonalIIdAnlegen(),
					finanzamtDto.getPersonalIIdAendern());
			em.persist(finanzamt);
			em.flush();
			finanzamtDto.setTAendern(finanzamt.getTAendern());
			finanzamtDto.setTAnlegen(finanzamt.getTAnlegen());
			setFinanzamtFromFinanzamtDto(finanzamt, finanzamtDto);
			return finanzamtFindByPrimaryKey(finanzamt.getPartnerIId(),
					finanzamt.getMandantCNr(), theClientDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeFinanzamt(FinanzamtDto finanzamtDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (finanzamtDto != null) {
			Integer partnerIId = finanzamtDto.getPartnerIId();
			String mandantCNr = finanzamtDto.getMandantCNr();
			// try {
			Finanzamt toRemove = em.find(Finanzamt.class, new FinanzamtPK(
					partnerIId, mandantCNr));
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

	public FinanzamtDto updateFinanzamt(FinanzamtDto finanzamtDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (finanzamtDto != null) {
			finanzamtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			finanzamtDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			Integer partnerIId = finanzamtDto.getPartnerIId();
			String mandantCNr = finanzamtDto.getMandantCNr();
			try {
				// zuerst den partner updaten
				getPartnerFac().updatePartner(finanzamtDto.getPartnerDto(),
						theClientDto);
				// und jetz das FA
				Finanzamt finanzamt = em.find(Finanzamt.class, new FinanzamtPK(
						partnerIId, mandantCNr));
				setFinanzamtFromFinanzamtDto(finanzamt, finanzamtDto);
				return finanzamtFindByPrimaryKey(finanzamt.getPartnerIId(),
						finanzamt.getMandantCNr(), theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}

	}

	public FinanzamtDto finanzamtFindByPrimaryKey(Integer partnerIId,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		Finanzamt finanzamt = em.find(Finanzamt.class, new FinanzamtPK(
				partnerIId, mandantCNr));
		if (finanzamt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		FinanzamtDto finanzamtDto = assembleFinanzamtDto(finanzamt);

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
				finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		FinanzamtDto finanzamtDto = null;

		Query query = em
				.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Finanzamt finanzamt = (Finanzamt) query.getSingleResult();
		if (finanzamt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		finanzamtDto = assembleFinanzamtDto(finanzamt);

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
				finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		FinanzamtDto finanzamtDto = null;

		Query query = em
				.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		// @todo getSingleResult oder getResultList ?
		try {
			finanzamtDto = assembleFinanzamtDto((Finanzamt) query
					.getSingleResult());
			if (finanzamtDto == null) {
				return null;
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY
				// , null);
			}

		} catch (NoResultException ex) {
			return null;
		}

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
				finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}
	
	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(
			Integer partnerIId, String mandantCNr) {

		Query query = em
				.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> finanzamt = query.getResultList();
		if(finanzamt == null)
			return null;
		return finanzamt.size() == 0 ? null : assembleFinanzamtDto((Finanzamt)finanzamt.iterator().next());
	}

	public FinanzamtDto[] finanzamtFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("FinanzamtfindAll");
		Collection<?> cl = query.getResultList();
		FinanzamtDto[] finanzamtDtos = assembleFinanzamtDtos(cl);
		for (int i = 0; i < finanzamtDtos.length; i++) {
			finanzamtDtos[i].setPartnerDto(getPartnerFac()
					.partnerFindByPrimaryKey(finanzamtDtos[i].getPartnerIId(),
							theClientDto));
		}
		return finanzamtDtos;

	}

	public FinanzamtDto[] finanzamtFindAllByMandantCNr(TheClientDto theClientDto)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("FinanzamtfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		FinanzamtDto[] finanzamtDtos = assembleFinanzamtDtos(cl);
		for (int i = 0; i < finanzamtDtos.length; i++) {
			finanzamtDtos[i].setPartnerDto(getPartnerFac()
					.partnerFindByPrimaryKey(finanzamtDtos[i].getPartnerIId(),
							theClientDto));
		}
		return finanzamtDtos;

	}

	private void setFinanzamtFromFinanzamtDto(Finanzamt finanzamt,
			FinanzamtDto finanzamtDto) {
		finanzamt.setCSteuernummer(finanzamtDto.getCSteuernummer());
		finanzamt.setCReferat(finanzamtDto.getCReferat());
		finanzamt.setTAnlegen(finanzamtDto.getTAnlegen());
		finanzamt.setPersonalIIdAnlegen(finanzamtDto.getPersonalIIdAendern());
		finanzamt.setTAendern(finanzamtDto.getTAendern());
		finanzamt.setIFormularnummer(finanzamtDto.getIFormularnummer());
		finanzamt.setBUmsatzRunden(finanzamtDto.getBUmsatzRunden());
		finanzamt.setPersonalIIdAendern(finanzamtDto.getPersonalIIdAendern());
		finanzamt.setKontoIIdEbdebitoren(finanzamtDto.getKontoIIdEbdebitoren());
		finanzamt.setKontoIIdEbkreditoren(finanzamtDto.getKontoIIdEbkreditoren());
		finanzamt.setKontoIIdEbsachkonten(finanzamtDto.getKontoIIdEbsachkonten());
		finanzamt.setKontoIIdAnzahlungErhaltenBezahlt(finanzamtDto.getKontoIIdAnzahlungErhaltBezahlt());
		finanzamt.setKontoIIdAnzahlungErhaltenVerr(finanzamtDto.getKontoIIdAnzahlungErhaltVerr());
		finanzamt.setKontoIIdAnzahlungGegebenBezahlt(finanzamtDto.getKontoIIdAnzahlungGegebenBezahlt());
		finanzamt.setKontoIIdAnzahlungGegebenVerr(finanzamtDto.getKontoIIdAnzahlungGegebenVerr());
		
		finanzamt.setKontoIIdRCAnzahlungErhaltenBezahlt(finanzamtDto.getKontoIIdRCAnzahlungErhaltBezahlt());
		finanzamt.setKontoIIdRCAnzahlungErhaltenVerr(finanzamtDto.getKontoIIdRCAnzahlungErhaltVerr());
		finanzamt.setKontoIIdRCAnzahlungGegebenBezahlt(finanzamtDto.getKontoIIdRCAnzahlungGegebenBezahlt());
		finanzamt.setKontoIIdRCAnzahlungGegebenVerr(finanzamtDto.getKontoIIdRCAnzahlungGegebenVerr());
		em.merge(finanzamt);
		em.flush();
	}

	private FinanzamtDto assembleFinanzamtDto(Finanzamt finanzamt) {
		return FinanzamtDtoAssembler.createDto(finanzamt);
	}

	private FinanzamtDto[] assembleFinanzamtDtos(Collection<?> finanzamts) {
		List<FinanzamtDto> list = new ArrayList<FinanzamtDto>();
		if (finanzamts != null) {
			Iterator<?> iterator = finanzamts.iterator();
			while (iterator.hasNext()) {
				Finanzamt finanzamt = (Finanzamt) iterator.next();
				list.add(assembleFinanzamtDto(finanzamt));
			}
		}
		FinanzamtDto[] returnArray = new FinanzamtDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * Anlegen einer neuen Bankverbindung
	 * 
	 * @param bankverbindungDto
	 *            BankverbindungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BankverbindungDto
	 */
	public BankverbindungDto createBankverbindung(
			BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BANKVERBINDUNG);
		if (bankverbindungDto != null) {
			bankverbindungDto.setIId(pk);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			bankverbindungDto.setTAendern(now);
			bankverbindungDto.setTAnlegen(now);
			bankverbindungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			bankverbindungDto.setPersonalIIdAnlegen(theClientDto
					.getIDPersonal());
			// Gibts schon eine mit dieser Kontonummer bei dieser Bank bei
			// diesem Mandanten?
			BankverbindungDto bvDto = bankverbindungFindByBankIIdMandantCNrCKontonummerOhneExc(
					bankverbindungDto.getBankIId(),
					bankverbindungDto.getMandantCNr(),
					bankverbindungDto.getCKontonummer());
			if (bvDto != null) {
				/**
				 * @todo MB->MB eigener Fehlercode
				 */
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
			}
			try {
				Query query = em
						.createNamedQuery("BankverbindungfindByKontoIId");
				query.setParameter(1, bankverbindungDto.getKontoIId());
				// @todo getSingleResult oder getResultList ?
				Bankverbindung temp = (Bankverbindung) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN,
						"");
			} catch (NoResultException ex) {
				try {
					Bankverbindung bankverbindung = new Bankverbindung(
							bankverbindungDto.getIId(),
							bankverbindungDto.getMandantCNr(),
							bankverbindungDto.getBankIId(),
							bankverbindungDto.getKontoIId(),
							bankverbindungDto.getPersonalIIdAnlegen(),
							bankverbindungDto.getPersonalIIdAendern());
					em.persist(bankverbindung);
					em.flush();
					setBankverbindungFromBankverbindungDto(bankverbindung,
							bankverbindungDto);
					return bankverbindungFindByPrimaryKey(bankverbindung
							.getIId());
				} catch (Exception e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
				}
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto is null"));
		}
	}

	public void removeBankverbindung(BankverbindungDto bankverbindungDto)
			throws EJBExceptionLP {
		if (bankverbindungDto != null) {
			Integer iId = bankverbindungDto.getIId();
			// try {
			Bankverbindung toRemove = em.find(Bankverbindung.class, iId);
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
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("bankverbindungDto != null)"));
		}
	}

	public BankverbindungDto updateBankverbindung(
			BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (bankverbindungDto != null) {
			Integer iId = bankverbindungDto.getIId();
			bankverbindungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			bankverbindungDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Bankverbindung bankverbindung = em.find(Bankverbindung.class, iId);

			try {

				BankverbindungDto bvDto = bankverbindungFindByBankIIdMandantCNrCKontonummerOhneExc(
						bankverbindungDto.getBankIId(),
						bankverbindungDto.getMandantCNr(),
						bankverbindungDto.getCKontonummer());
				if (bvDto != null && iId != bvDto.getIId()) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
				}

				// Schaun, ob das Konto bereits einer anderen bankverbindung
				// zugewiesen ist
				Query query = em
						.createNamedQuery("BankverbindungfindByKontoIId");
				query.setParameter(1, bankverbindungDto.getKontoIId());
				// @todo getSingleResult oder getResultList ?
				Bankverbindung bv = (Bankverbindung) query.getSingleResult();
				// falls es dasselbe ist, dann passts auch ;-)
				if (!bv.getIId().equals(bankverbindungDto.getIId())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN,
							"");
				} else {
					setBankverbindungFromBankverbindungDto(bankverbindung,
							bankverbindungDto);
					return bankverbindungDto;
				}

			} catch (NoResultException ex) {
				try {

					setBankverbindungFromBankverbindungDto(bankverbindung,
							bankverbindungDto);
					return bankverbindungDto;
				} catch (Exception e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
				}
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bankverbindungDto != null"));
		}
	}

	public BankverbindungDto bankverbindungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Bankverbindung bankverbindung = em.find(Bankverbindung.class, iId);
			if (bankverbindung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBankverbindungDto(bankverbindung);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public BankverbindungDto bankverbindungFindByKontoIIdOhneExc(
			Integer kontoIId) throws EJBExceptionLP {
		BankverbindungDto bankverbindungDto = null;
		try {
			Query query = em.createNamedQuery("BankverbindungfindByKontoIId");
			query.setParameter(1, kontoIId);
			bankverbindungDto = assembleBankverbindungDto((Bankverbindung) query
					.getSingleResult());
			// if (bankverbindungDto.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
			// }
		} catch (NoResultException ex) {
			// nothing here.
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		}

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return bankverbindungDto;
	}

	public BankverbindungDto bankverbindungFindByBankIIdMandantCNrCKontonummerOhneExc(
			Integer bankIId, String mandantCNr, String cKontonummer)
			throws EJBExceptionLP {
		BankverbindungDto bankverbindungDto = null;
		try {
			Query query = em
					.createNamedQuery("BankverbindungfindByBankIIdMandantCNrCKontonummer");
			query.setParameter(1, bankIId);
			query.setParameter(2, mandantCNr);
			query.setParameter(3, cKontonummer);
			Bankverbindung bankverbindung = (Bankverbindung) query
					.getSingleResult();
			if (bankverbindung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			bankverbindungDto = assembleBankverbindungDto(bankverbindung);

			// }
			// catch (ObjectNotFoundException ex) {
			// nothing here.
		} catch (NoResultException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return bankverbindungDto;
	}

	private void setBankverbindungFromBankverbindungDto(
			Bankverbindung bankverbindung, BankverbindungDto bankverbindungDto) {
		bankverbindung.setMandantCNr(bankverbindungDto.getMandantCNr());
		bankverbindung.setBankIId(bankverbindungDto.getBankIId());
		bankverbindung.setCKontonummer(bankverbindungDto.getCKontonummer());
		bankverbindung.setKontoIId(bankverbindungDto.getKontoIId());
		bankverbindung.setCBez(bankverbindungDto.getCBez());
		bankverbindung.setCIban(bankverbindungDto.getCIban());
		bankverbindung.setTAnlegen(bankverbindungDto.getTAnlegen());
		bankverbindung.setPersonalIIdAnlegen(bankverbindungDto
				.getPersonalIIdAnlegen());
		bankverbindung.setTAendern(bankverbindungDto.getTAendern());
		bankverbindung.setPersonalIIdAendern(bankverbindungDto
				.getPersonalIIdAendern());
		em.merge(bankverbindung);
		em.flush();
	}

	private BankverbindungDto assembleBankverbindungDto(
			Bankverbindung bankverbindung) {
		return BankverbindungDtoAssembler.createDto(bankverbindung);
	}

	private BankverbindungDto[] assembleBankverbindungDtos(
			Collection<?> bankverbindungs) {
		List<BankverbindungDto> list = new ArrayList<BankverbindungDto>();
		if (bankverbindungs != null) {
			Iterator<?> iterator = bankverbindungs.iterator();
			while (iterator.hasNext()) {
				Bankverbindung bankverbindung = (Bankverbindung) iterator
						.next();
				list.add(assembleBankverbindungDto(bankverbindung));
			}
		}
		BankverbindungDto[] returnArray = new BankverbindungDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * Anlegen eines neuen Kassenbuchs
	 * 
	 * @param kassenbuchDto
	 *            KassenbuchDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto createKassenbuch(KassenbuchDto kassenbuchDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// PK generieren
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_KASSENBUCH);
		kassenbuchDto.setIId(pk);
		// anlegen, aendern
		Timestamp now = new Timestamp(System.currentTimeMillis());
		kassenbuchDto.setTAendern(now);
		kassenbuchDto.setTAnlegen(now);
		kassenbuchDto.setPersonalIIdaendern(theClientDto.getIDPersonal());
		kassenbuchDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Kassenbuch kassenbuch = new Kassenbuch(kassenbuchDto.getIId(),
					kassenbuchDto.getMandantCNr(), kassenbuchDto.getCBez(),
					kassenbuchDto.getKontoIId(),
					kassenbuchDto.getBNegativErlaubt(),
					kassenbuchDto.getBHauptkassenbuch(),
					kassenbuchDto.getDGueltigVon(),
					kassenbuchDto.getPersonalIIdAnlegen(),
					kassenbuchDto.getPersonalIIdaendern());
			em.persist(kassenbuch);
			em.flush();
			setKassenbuchFromKassenbuchDto(kassenbuch, kassenbuchDto);
			return kassenbuchFindByPrimaryKey(kassenbuch.getIId(), theClientDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Loeschen eines Kassenbuchs
	 * 
	 * @param kassenbuchDto
	 *            KassenbuchDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeKassenbuch(KassenbuchDto kassenbuchDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kassenbuchDto != null) {
			Integer iId = kassenbuchDto.getIId();
			// try {
			Kassenbuch toRemove = em.find(Kassenbuch.class, iId);
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
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kassenbuchDto != null"));
		}
	}

	/**
	 * Update eines Kassenbuchs
	 * 
	 * @param kassenbuchDto
	 *            KassenbuchDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto updateKassenbuch(KassenbuchDto kassenbuchDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kassenbuchDto != null) {
			Integer iId = kassenbuchDto.getIId();
			kassenbuchDto.setPersonalIIdaendern(theClientDto.getIDPersonal());
			kassenbuchDto
					.setTAendern(new Timestamp(System.currentTimeMillis()));
			try {
				Kassenbuch kassenbuch = em.find(Kassenbuch.class, iId);
				setKassenbuchFromKassenbuchDto(kassenbuch, kassenbuchDto);
				return kassenbuchFindByPrimaryKey(kassenbuch.getIId(),
						theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("Dto is null"));
		}
	}

	/**
	 * Kassenbuch anhand PK finden
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto kassenbuchFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Kassenbuch kassenbuch = em.find(Kassenbuch.class, iId);
			if (kassenbuch == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKassenbuchDto(kassenbuch);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public KassenbuchDto kassenbuchFindByKontoIIdOhneExc(Integer kontoIId)
			throws EJBExceptionLP {
		KassenbuchDto kassenbuchDto = null;
		try {
			Query query = em.createNamedQuery("KassenbuchfindByKontoIId");
			query.setParameter(1, kontoIId);
			kassenbuchDto = assembleKassenbuchDto((Kassenbuch) query
					.getSingleResult());
		} catch (NoResultException ex) {
			// nothing here.
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		}

		return kassenbuchDto;
	}

	/***
	 * Dto auf Entity uebertragen** @param kassenbuch Kassenbuch* @param
	 * kassenbuchDto KassenbuchDto
	 */
	private void setKassenbuchFromKassenbuchDto(Kassenbuch kassenbuch,
			KassenbuchDto kassenbuchDto) {
		kassenbuch.setMandantCNr(kassenbuchDto.getMandantCNr());
		kassenbuch.setCBez(kassenbuchDto.getCBez());
		kassenbuch.setKontoIId(kassenbuchDto.getKontoIId());
		kassenbuch.setBNegativerlaubt(kassenbuchDto.getBNegativErlaubt());
		kassenbuch.setBHauptkassenbuch(kassenbuchDto.getBHauptkassenbuch());
		kassenbuch.setTGueltigvon(kassenbuchDto.getDGueltigVon());
		kassenbuch.setTGueltigbis(kassenbuchDto.getDGueltigBis());
		kassenbuch.setTAnlegen(kassenbuchDto.getTAnlegen());
		kassenbuch.setPersonalIIdAnlegen(kassenbuchDto.getPersonalIIdAnlegen());
		kassenbuch.setTAendern(kassenbuchDto.getTAendern());
		kassenbuch.setPersonalIIdAendern(kassenbuchDto.getPersonalIIdaendern());
		em.merge(kassenbuch);
		em.flush();
	}

	/**
	 * Entity auf Dto uebertragen
	 * 
	 * @param kassenbuch
	 *            Kassenbuch
	 * @return KassenbuchDto
	 */
	private KassenbuchDto assembleKassenbuchDto(Kassenbuch kassenbuch) {
		return KassenbuchDtoAssembler.createDto(kassenbuch);
	}

	/**
	 * Entitys auf Dtos uebertragen
	 * 
	 * @param kassenbuchs
	 *            Collection
	 * @return KassenbuchDto[]
	 */
	private KassenbuchDto[] assembleKassenbuchDtos(Collection<?> kassenbuchs) {
		List<KassenbuchDto> list = new ArrayList<KassenbuchDto>();
		if (kassenbuchs != null) {
			Iterator<?> iterator = kassenbuchs.iterator();
			while (iterator.hasNext()) {
				Kassenbuch kassenbuch = (Kassenbuch) iterator.next();
				list.add(assembleKassenbuchDto(kassenbuch));
			}
		}
		KassenbuchDto[] returnArray = new KassenbuchDto[list.size()];
		return list.toArray(returnArray);
	}

	private void pruefeZykelUst(Integer kontoIId, Integer kontoIIdWeiterfuehrend)
			throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_ZYKEL_UST, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendUst();
		}
	}

	private void pruefeZykelSkonto(Integer kontoIId,
			Integer kontoIIdWeiterfuehrend) throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_ZYKEL_SKONTO, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendSkonto();
		}
	}

	private void pruefeZykelBilanz(Integer kontoIId,
			Integer kontoIIdWeiterfuehrend) throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_ZYKEL_BILANZ, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendBilanz();
		}

	}

	public void pruefeZykel(KontoDto kontoDto) throws EJBExceptionLP {
		pruefeZykelBilanz(kontoDto.getIId(),
				kontoDto.getKontoIIdWeiterfuehrendBilanz());
		pruefeZykelUst(kontoDto.getIId(),
				kontoDto.getKontoIIdWeiterfuehrendUst());
		pruefeZykelSkonto(kontoDto.getIId(),
				kontoDto.getKontoIIdWeiterfuehrendSkonto());
	}

	public RechenregelDto rechenregelFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		try {
			Rechenregel rechenregel = em.find(Rechenregel.class, cNr);
			if (rechenregel == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechenregelDto(rechenregel);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private RechenregelDto assembleRechenregelDto(Rechenregel rechenregel) {
		return RechenregelDtoAssembler.createDto(rechenregel);
	}

	private RechenregelDto[] assembleRechenregelDtos(Collection<?> rechenregels) {
		List<RechenregelDto> list = new ArrayList<RechenregelDto>();
		if (rechenregels != null) {
			Iterator<?> iterator = rechenregels.iterator();
			while (iterator.hasNext()) {
				Rechenregel rechenregel = (Rechenregel) iterator.next();
				list.add(assembleRechenregelDto(rechenregel));
			}
		}
		RechenregelDto[] returnArray = new RechenregelDto[list.size()];
		return list.toArray(returnArray);
	}

	public ErgebnisgruppeDto createErgebnisgruppe(
			ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BUCHUNGSREGELGEGENKONTO);
		ergebnisgruppeDto.setIId(iId);
		ergebnisgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		ergebnisgruppeDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// Hinten anreihen
		ergebnisgruppeDto.setIReihung(getNextErgebnisgruppeReihung(theClientDto
				.getMandant(),ergebnisgruppeDto.getBBilanzgruppe()));
		try {
			Ergebnisgruppe ergebnisgruppe = new Ergebnisgruppe(
					ergebnisgruppeDto.getIId(),
					ergebnisgruppeDto.getMandantCNr(),
					ergebnisgruppeDto.getCBez(),
					ergebnisgruppeDto.getIReihung(),
					ergebnisgruppeDto.getBSummeNegativ(),
					ergebnisgruppeDto.getBInvertiert(),
					ergebnisgruppeDto.getPersonalIIdAnlegen(),
					ergebnisgruppeDto.getPersonalIIdAendern(),
					ergebnisgruppeDto.getBProzentbasis(),
					ergebnisgruppeDto.getITyp(),
					ergebnisgruppeDto.getBBilanzgruppe());
			em.persist(ergebnisgruppe);
			em.flush();
			ergebnisgruppeDto.setTAendern(ergebnisgruppe.getTAendern());
			ergebnisgruppeDto.setTAnlegen(ergebnisgruppe.getTAnlegen());
			setErgebnisgruppeFromErgebnisgruppeDto(ergebnisgruppe,
					ergebnisgruppeDto);
			return ergebnisgruppeDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (ergebnisgruppeDto != null) {
			Integer iId = ergebnisgruppeDto.getIId();
			// try {
			Ergebnisgruppe toRemove = em.find(Ergebnisgruppe.class, iId);
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

	public ErgebnisgruppeDto updateErgebnisgruppe(
			ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (ergebnisgruppeDto != null) {
			ergebnisgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			ergebnisgruppeDto.setTAendern(super.getTimestamp());
			Integer iId = ergebnisgruppeDto.getIId();
			try {
				Ergebnisgruppe ergebnisgruppe = em.find(Ergebnisgruppe.class,
						iId);
				setErgebnisgruppeFromErgebnisgruppeDto(ergebnisgruppe,
						ergebnisgruppeDto);
				return ergebnisgruppeFindByPrimaryKey(ergebnisgruppe.getIId());
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}
	}

	public ErgebnisgruppeDto ergebnisgruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Ergebnisgruppe ergebnisgruppe = em.find(Ergebnisgruppe.class, iId);
		if (ergebnisgruppe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleErgebnisgruppeDto(ergebnisgruppe);
	}

	public ErgebnisgruppeDto[] ergebnisgruppeFindAll(TheClientDto theClientDto, boolean bilanzgruppe)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("ErgebnisgruppefindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, Helper.boolean2Short(bilanzgruppe)) ;
		Collection<?> cl = query.getResultList();
		ErgebnisgruppeDto[] ergebnisgruppeDtos = assembleErgebnisgruppeDtos(cl);
		return ergebnisgruppeDtos;
	}

	private void setErgebnisgruppeFromErgebnisgruppeDto(
			Ergebnisgruppe ergebnisgruppe, ErgebnisgruppeDto ergebnisgruppeDto) {
		ergebnisgruppe.setMandantCNr(ergebnisgruppeDto.getMandantCNr());
		ergebnisgruppe.setCBez(ergebnisgruppeDto.getCBez());
		ergebnisgruppe.setErgebnisgruppeIIdSumme(ergebnisgruppeDto
				.getErgebnisgruppeIIdSumme());
		ergebnisgruppe.setIReihung(ergebnisgruppeDto.getIReihung());
		ergebnisgruppe.setBSummenegativ(ergebnisgruppeDto.getBSummeNegativ());
		ergebnisgruppe.setBInvertiert(ergebnisgruppeDto.getBInvertiert());
		ergebnisgruppe.setTAnlegen(ergebnisgruppeDto.getTAnlegen());
		ergebnisgruppe.setPersonalIIdAnlegen(ergebnisgruppeDto
				.getPersonalIIdAnlegen());
		ergebnisgruppe.setTAendern(ergebnisgruppeDto.getTAendern());
		ergebnisgruppe.setPersonalIIdAendern(ergebnisgruppeDto
				.getPersonalIIdAendern());
		ergebnisgruppe.setBProzentbasis(ergebnisgruppeDto.getBProzentbasis());
		ergebnisgruppe.setITyp(ergebnisgruppeDto.getITyp());
		em.merge(ergebnisgruppe);
		em.flush();
	}

	private ErgebnisgruppeDto assembleErgebnisgruppeDto(
			Ergebnisgruppe ergebnisgruppe) {
		return ErgebnisgruppeDtoAssembler.createDto(ergebnisgruppe);
	}

	private ErgebnisgruppeDto[] assembleErgebnisgruppeDtos(
			Collection<?> ergebnisgruppes) {
		List<ErgebnisgruppeDto> list = new ArrayList<ErgebnisgruppeDto>();
		if (ergebnisgruppes != null) {
			Iterator<?> iterator = ergebnisgruppes.iterator();
			while (iterator.hasNext()) {
				Ergebnisgruppe ergebnisgruppe = (Ergebnisgruppe) iterator
						.next();
				list.add(assembleErgebnisgruppeDto(ergebnisgruppe));
			}
		}
		ErgebnisgruppeDto[] returnArray = new ErgebnisgruppeDto[list.size()];
		return list.toArray(returnArray);
	}

	public KontoDtoSmall kontoFindByPrimaryKeySmallOhneExc(Integer iIdI) {
		KontoDtoSmall kontoDtoSmall = null;
		try {
			kontoDtoSmall = kontoFindByPrimaryKeySmall(iIdI);
		} catch (Throwable ex) {
			// nothing here
		}
		return kontoDtoSmall;
	}

	// public KontoDto kontoFindByCnrMandantOhneExc(String cNrI, String
	// sMandantI,
	// TheClientDto theClientDto) {
	// // precondition
	// if (cNrI == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
	// "cNrI == null"));
	// }
	// if (sMandantI == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
	// "sMandantI == null"));
	// }
	// if (theClientDto.getIDUser() == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
	// "cNrUserI == null"));
	// }
	//
	// KontoDto kontoDto = null;
	// try {
	// kontoDto = kontoFindByCnrMandant(cNrI, sMandantI, theClientDto);
	// } catch (Throwable ex) {
	// // nothing here
	// }
	// return kontoDto;
	// }

	public KontoDto kontoFindByCnrMandantOhneExc(String cNrI, String sMandantI,
			TheClientDto theClientDto) {
		// precondition
		if (cNrI == null) {
			myLogger.warn("cNrI == null");
			return null;
		}
		if (sMandantI == null) {
			myLogger.warn("sMandantI == null");
			return null;
		}

		KontoDto kontoDto = null;
		try {
			Query query = em.createNamedQuery("KontofindByCNrMandant");
			query.setParameter(1, cNrI);
			query.setParameter(2, sMandantI);
			// @todo getSingleResult oder getResultList ?
			return assembleKontoDto((Konto) query.getSingleResult());
		} catch (Exception e) {
		}
		return kontoDto;
	}

	public KontoDto kontoFindByCnrKontotypMandantOhneExc(String cNrI,
			String kontotypCnr, String mandant, TheClientDto theClientDto)
			throws RemoteException {

		if (cNrI == null) {
			myLogger.warn("cNrI == null");
			return null;
		}
		if (kontotypCnr == null) {
			myLogger.warn("kontotypCnr == null");
			return null;
		}
		if (mandant == null) {
			myLogger.warn("sMandantI == null");
			return null;
		}

		KontoDto kontoDto = null;
		try {
			Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
			query.setParameter(1, cNrI);
			query.setParameter(2, kontotypCnr);
			query.setParameter(3, mandant);
			return assembleKontoDto((Konto) query.getSingleResult());
		} catch (Exception e) {
		}
		return kontoDto;
	}

	public KontoDto[] kontoFindAllByKontotypMandant(String kontotypCNr,
			String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOTYP_MANDANT);
		query.setParameter(1, kontotypCNr);
		query.setParameter(2, mandantCNr);
		return assembleKontoDtos(query.getResultList());
	}

	public KontoDto[] kontoFindAllByKontoartMandant(String kontoartCNr,
			String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT);
		query.setParameter(1, kontoartCNr);
		query.setParameter(2, mandantCNr);
		return assembleKontoDtos(query.getResultList());
	}
	
	public KontoDto[] kontoFindAllByKontoartMandantFinanzamt(String kontoartCNr,	String mandantCNr, Integer finanzamtIid) throws EJBExceptionLP {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT_FINANZAMT);
		query.setParameter(1, kontoartCNr);
		query.setParameter(2, mandantCNr);
		query.setParameter(3, finanzamtIid);
		return assembleKontoDtos(query.getResultList());
	}

	public Integer getAnzahlStellenVonKontoNummer(String kontotypCNr,
			String mandantCNr) throws EJBExceptionLP {
		try {
			String parameterCNr;
			if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN;
			} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_DEBITORENKONTEN;
			} else {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_KREDITORENKONTEN;
			}
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(mandantCNr,
							ParameterFac.KATEGORIE_FINANZ, parameterCNr);
			int iStellen = Integer.parseInt(parameter.getCWert());
			return new Integer(iStellen);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public KontolaenderartDto createKontolaenderart(
			KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(kontolaenderartDto);
		if(kontolaenderartDto.getKontoIId().equals(kontolaenderartDto.getKontoIIdUebersetzt()))
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST,
					"kontoiid = " + kontolaenderartDto.getKontoIId());
		kontolaenderartDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kontolaenderartDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Kontolaenderart kontolaenderart = new Kontolaenderart(
					kontolaenderartDto.getKontoIId(),
					kontolaenderartDto.getLaenderartCNr(),
					kontolaenderartDto.getFinanzamtIId(),
					kontolaenderartDto.getMandantCNr(),
					kontolaenderartDto.getKontoIIdUebersetzt(),
					kontolaenderartDto.getPersonalIIdAnlegen(),
					kontolaenderartDto.getPersonalIIdAendern());
			em.persist(kontolaenderart);
			em.flush();
			kontolaenderartDto.setTAendern(kontolaenderart.getTAendern());
			kontolaenderartDto.setTAnlegen(kontolaenderart.getTAnlegen());
			setKontolaenderartFromKontolaenderartDto(kontolaenderart,
					kontolaenderartDto);
			return kontolaenderartDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKontolaenderart(KontolaenderartDto kontolaenderartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontolaenderartDto != null) {
			KontolaenderartPK kontolaenderartPK = new KontolaenderartPK(
					kontolaenderartDto.getKontoIId(),
					kontolaenderartDto.getLaenderartCNr(),
					kontolaenderartDto.getFinanzamtIId(),
					kontolaenderartDto.getMandantCNr());

			Kontolaenderart toRemove = em.find(Kontolaenderart.class,
					kontolaenderartPK);
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
	}

	public KontolaenderartDto updateKontolaenderart(
			KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(kontolaenderartDto);
		
		if(kontolaenderartDto.getKontoIId().equals(kontolaenderartDto.getKontoIIdUebersetzt()))
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST,
					"kontoiid = " + kontolaenderartDto.getKontoIId());
		kontolaenderartDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kontolaenderartDto.setTAendern(super.getTimestamp());

		KontolaenderartPK kontolaenderartPK = new KontolaenderartPK(
				kontolaenderartDto.getKontoIId(),
				kontolaenderartDto.getLaenderartCNr(),
				kontolaenderartDto.getFinanzamtIId(),
				kontolaenderartDto.getMandantCNr());
		Kontolaenderart kontolaenderart = em.find(Kontolaenderart.class,
				kontolaenderartPK);
		if (kontolaenderart == null) {
			return createKontolaenderart(kontolaenderartDto, theClientDto);
		}
		setKontolaenderartFromKontolaenderartDto(kontolaenderart,
				kontolaenderartDto);
		return kontolaenderartDto;
	}

	public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontoIId,
			String laenderartCNr, Integer finanzamtIId, String mandantCNr)
			throws EJBExceptionLP {

		KontolaenderartPK kontolaenderartPK = new KontolaenderartPK(kontoIId,
				laenderartCNr, finanzamtIId, mandantCNr);
		Kontolaenderart kontolaenderart = em.find(Kontolaenderart.class,
				kontolaenderartPK);
		if (kontolaenderart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Kontolaenderart nicht gefunden: " + laenderartCNr);
		}
		return assembleKontolaenderartDto(kontolaenderart);
	}

	public KontolaenderartDto kontolaenderartFindByPrimaryKeyOhneExc(
			Integer kontoIId, String laenderartCNr, Integer finanzamtIId,
			String mandantCNr) throws EJBExceptionLP {

		KontolaenderartPK kontolaenderartPK = new KontolaenderartPK(kontoIId,
				laenderartCNr, finanzamtIId, mandantCNr);
		Kontolaenderart kontolaenderart = em.find(Kontolaenderart.class,
				kontolaenderartPK);
		if (kontolaenderart == null) {
			myLogger.warn("kontoIId=" + kontoIId);
			return null;
		}
		return assembleKontolaenderartDto(kontolaenderart);
	}

	private void setKontolaenderartFromKontolaenderartDto(
			Kontolaenderart kontolaenderart,
			KontolaenderartDto kontolaenderartDto) {
		kontolaenderart.setKontoIIdUebersetzt(kontolaenderartDto
				.getKontoIIdUebersetzt());
		kontolaenderart.setTAnlegen(kontolaenderartDto.getTAnlegen());
		kontolaenderart.setPersonalIIdAnlegen(kontolaenderartDto
				.getPersonalIIdAnlegen());
		kontolaenderart.setTAendern(kontolaenderartDto.getTAendern());
		kontolaenderart.setPersonalIIdAendern(kontolaenderartDto
				.getPersonalIIdAendern());
		em.merge(kontolaenderart);
		em.flush();
	}

	private KontolaenderartDto assembleKontolaenderartDto(
			Kontolaenderart kontolaenderart) {
		return KontolaenderartDtoAssembler.createDto(kontolaenderart);
	}

	private KontolaenderartDto[] assembleKontolaenderartDtos(
			Collection<?> kontolaenderarts) {
		List<KontolaenderartDto> list = new ArrayList<KontolaenderartDto>();
		if (kontolaenderarts != null) {
			Iterator<?> iterator = kontolaenderarts.iterator();
			while (iterator.hasNext()) {
				Kontolaenderart kontolaenderart = (Kontolaenderart) iterator
						.next();
				list.add(assembleKontolaenderartDto(kontolaenderart));
			}
		}
		KontolaenderartDto[] returnArray = new KontolaenderartDto[list.size()];
		return list.toArray(returnArray);
	}

	public void pruefeBuchungszeitraum(java.util.Date d,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ParametermandantDto pmMonate = null;
		try {
			pmMonate = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_MONATE);
			ParametermandantDto pmTage = null;
			pmTage = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_TAGE);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Ein Personenkonto fuer einen Kunden/Lieferanten anlegen. Die Nummer wird
	 * dabei automatisch generiert
	 * 
	 * @param partnerDto
	 *            PartnerDto
	 * @param kontotypCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	public KontoDto createKontoFuerPartnerAutomatisch(PartnerDto partnerDto,
			String kontotypCNr, boolean kontoAnlegen,
			String kontonummerVorgabe, TheClientDto theClientDto)
			throws EJBExceptionLP {
		KontoDto kontoDto = null;
		
		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			if (mandantDto.getPartnerIIdFinanzamt() == null)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN, 
						"Kein Finanzamt definiert f\u00FCr Mandant " + theClientDto.getMandant());
		} catch (RemoteException e1) {
			throw new EJBExceptionLP(e1);
		}
		if (kontonummerVorgabe != null) {
			try {
				kontoDto = kontoErstellen(kontonummerVorgabe, kontotypCNr,
						partnerDto, kontoAnlegen, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;
			}
			// Es konnte kein Konto erstellt werden
			if (kontoDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN,
						"");
			}
		} else {
			Integer iStellenanzahl = getAnzahlStellenVonKontoNummer(
					kontotypCNr, theClientDto.getMandant());
			// Die Regel zur Zusammenstellung der Kontonummer wird durch einen
			// Mandantenparameter definiert
			// Vorerst wird nur die Standard-Regel implementiert
			// x yy z*
			// x ... Kontoklasse lt. Mandantenparameter/Debitor/Kreditor
			// yy ... Anfangsbuchstabe des Partners (A=01, B=02, ...) zuzueglich
			// Startwert
			// z* ... laufende Nummer (die naechste freie) mit der verbleibenden
			// Stellenanzahl
			//
			// Sonderfaelle: Ziffern 0 - 9 werden nach dem Zahlwort eingeordnet
			// (z.B. 3 als drei)
			//
			// PJ 15226: zusaetzlich zu dieser Regel wird eine fortlaufende
			// Nummerierung
			// ohne Ruecksicht auf den Partnernamen implementiert. Die Anwendung
			// ist abhaengig
			// vom Mandantenparameter FINANZ_DEBITORENNUMMER_FORTLAUFEND
			// (0 ... 2+3 Stelle aus Partner, 1 ... nur fortlaufend)
			// Gilt fuer Debitoren und Kreditoren
			// ACHTUNG: wenn der Parameter nachtraeglich auf 1 gesetzt wird,
			// werden Luecken aufgefuellt!
			//

			String parameterCNr = null;
			String kategorieCNr = null;
			if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
				parameterCNr = ParameterFac.PARAMETER_DEBITORENNUMMER_VON;
				kategorieCNr = ParameterFac.KATEGORIE_KUNDEN;
			} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				parameterCNr = ParameterFac.PARAMETER_KREDITOREN_VON;
				kategorieCNr = ParameterFac.KATEGORIE_LIEFERANT;
			}
			try {
				// Ermittlung der der Kontoklasse
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								kategorieCNr, parameterCNr);
				char cKontoklasse = parameter.getCWert().charAt(0);

				ParametermandantDto paraDebFortlaufend = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_DEBITORENNUMMER_FORTLAUFEND);

				boolean debfortlaufend = (Boolean) paraDebFortlaufend
						.getCWertAsObject();

				String sBereich = "";
				if (!debfortlaufend) {
					// Ermittlung der 2stelligen nummer anhand Anfangsbuchstabe
					// des
					// Partnernamens
					sBereich = getPartnerteilDerNummer(partnerDto, parameter);
				}
				// jetzt die naechste noch nicht vergebene Kontonummer finden
				int iVerbleibendeStellen = iStellenanzahl.intValue() - 1
						- sBereich.length();
				for (int i = 0; i < Math.pow(10, iVerbleibendeStellen); i++) {
					DecimalFormat nf = new DecimalFormat(
							"000000000000".substring(0, iVerbleibendeStellen));
					String sLfdNummer = nf.format(i);
					// Kontonummer zusammenbasteln
					String sKontonummer = cKontoklasse + sBereich + sLfdNummer;
					// Pruefen, ob das Konto schon existiert
					kontoDto = getFinanzFac()
							.kontoFindByCnrKontotypMandantOhneExc(sKontonummer,
									kontotypCNr, theClientDto.getMandant(),
									theClientDto);
					if (kontoDto == null) {
						kontoDto = kontoErstellen(sKontonummer, kontotypCNr,
								partnerDto, kontoAnlegen, theClientDto);
						break;
					}
				}
				// Es konnte kein Konto erstellt werden
				if (kontoDto == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN,
							"");
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}
		}
		return kontoDto;
	}

	private String getPartnerteilDerNummer(PartnerDto partnerDto,
			ParametermandantDto parameter) {
		String sBereich;
		char[] aZiffer = new char[] { 'n', 'e', 'z', 'd', 'v', 'f', 's', 's',
				'a', 'n' };
		String umlaute = "\u00E4\u00F6\u00FC\u00DF";
		char[] aUmlautErsatz = new char[] { 'a', 'o', 'u', 's' };
		int iBereich;
		if (partnerDto.getCName1nachnamefirmazeile1() != null
				&& partnerDto.getCName1nachnamefirmazeile1().trim().length() >= 0) {

			// Lower Case damit ich a und A gleich behandle
			char cAnfangsbuchstabe = partnerDto.getCName1nachnamefirmazeile1()
					.trim().toLowerCase().charAt(0);

			// umlaute umsetzen
			if (umlaute.contains("" + cAnfangsbuchstabe)) {
				int i = umlaute.indexOf(cAnfangsbuchstabe);
				cAnfangsbuchstabe = aUmlautErsatz[i];
			}

			iBereich = cAnfangsbuchstabe - 'a' + 1;
			if (iBereich < 0) {
				// Zahlen dem Zahlwort zuordnen
				iBereich = cAnfangsbuchstabe - '0';
				if (iBereich >= 0 && iBereich < aZiffer.length) {
					iBereich = aZiffer[iBereich] - 'a' + 1;
				} else {
					iBereich = 0;
				}
			}
		} else {
			// Ansonsten einfach in den Bereich 00
			iBereich = 0;
		}
		// Offset addieren
		int iOffset = new Integer(parameter.getCWert().substring(1, 3))
				.intValue();
		iBereich += iOffset;
		if (iBereich > 99)
			// wenn der Wertebereich ueberschritten wird dann auf 00
			iBereich = 0;

		if (iBereich < 10) {
			sBereich = "0" + iBereich;
		} else {
			sBereich = "" + iBereich;
		}
		return sBereich;
	}

	private KontoDto kontoErstellen(String sKontonummer, String kontotypCNr,
			PartnerDto partnerDto, boolean kontoAnlegen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// das Konto gibts noch nicht -> Dto Anlegen
		KontoDto kontoDto = new KontoDto();
		kontoDto.setCNr(sKontonummer);
		kontoDto.setCBez(partnerDto.getCName1nachnamefirmazeile1());
		// Pflichtfelder befuellen
		GeschaeftsjahrMandantDto gjDto = getSystemFac()
				.geschaeftsjahrFindByPrimaryKey(
						getParameterFac().getGeschaeftsjahr(
								theClientDto.getMandant()), theClientDto.getMandant());
		kontoDto.setDGueltigvon(new Date(gjDto.getDBeginndatum().getTime()));
		kontoDto.setBAllgemeinsichtbar(Helper.boolean2Short(true));
		kontoDto.setBAutomeroeffnungsbuchung(Helper.boolean2Short(true));
		kontoDto.setBManuellbebuchbar(Helper.boolean2Short(true));
		kontoDto.setMandantCNr(theClientDto.getMandant());
		kontoDto.setKontotypCNr(kontotypCNr);
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		kontoDto.setFinanzamtIId(mandantDto.getPartnerIIdFinanzamt());
		kontoDto.setBOhneUst(Helper.boolean2Short(false));

		// nur wenn Fibumodul
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			String laenderart = getFinanzServiceFac().getLaenderartZuPartner(
					partnerDto.getIId(), theClientDto);
			SteuerkategorieDto steuerkategorieDto = getSteuerkategorieZuLaenderart(
					kontoDto.getFinanzamtIId(), laenderart, theClientDto);
			if (steuerkategorieDto == null)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT, 
						"Steuerkategorie fehlt f\u00FCr Finanzamt IId:" + kontoDto.getFinanzamtIId() + ", L\u00E4nderart: " + laenderart);
			
			SteuerkategorieDto steuerkategorieReverseDto = getSteuerkategorieReverseZuLaenderart(
					kontoDto.getFinanzamtIId(), laenderart, theClientDto);
			if (steuerkategorieReverseDto == null)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT, 
						"Steuerkategorie Reverse Charge fehlt f\u00FCr Finanzamt IId:" + kontoDto.getFinanzamtIId() + ", L\u00E4nderart: " + laenderart);
			kontoDto.setSteuerkategorieIId(steuerkategorieDto.getIId());
			kontoDto.setSteuerkategorieIIdReverse(steuerkategorieReverseDto.getIId());
		}
		// und speichern
		if (kontoAnlegen) {
			kontoDto = createKonto(kontoDto, theClientDto);
		}
		return kontoDto;
	}
	
	public SteuerkategorieDto getSteuerkategorieReverseZuLaenderart(
		Integer finanzamtIId, String laenderart, TheClientDto theClientDto) {
		return getSteuerkategorieZuLaenderart(finanzamtIId, laenderart, theClientDto, true);
	}
	
	public SteuerkategorieDto getSteuerkategorieZuLaenderart(
			Integer finanzamtIId, String laenderart, TheClientDto theClientDto) {
		return getSteuerkategorieZuLaenderart(finanzamtIId, laenderart, theClientDto, false);
	}

	private SteuerkategorieDto getSteuerkategorieZuLaenderart(
			Integer finanzamtIId, String laenderart, TheClientDto theClientDto, boolean reverse) {
		// Default ist Inland
		
		String cnr = null;
		if (laenderart != null) {
			// Jetzt nach Laenderart waehlen
			if (laenderart.equals(FinanzFac.LAENDERART_INLAND))
				cnr = reverse ? FinanzServiceFac.STEUERKATEGORIE_INLAND_REVERSE : FinanzServiceFac.STEUERKATEGORIE_INLAND;
			else if (reverse)
				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLAND_REVERSE;
			else if (laenderart.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID))
				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID;
			else if (laenderart
					.equals(FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID))
				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU;
			else if (laenderart.equals(FinanzFac.LAENDERART_DRITTLAND))
				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLAND;
		}
		if(cnr == null) return null;
		SteuerkategorieDto steuerkategorieDto = getFinanzServiceFac()
				.steuerkategorieFindByCNrFinanzamtIId(cnr, finanzamtIId,
						theClientDto);
		return steuerkategorieDto;
	}

	private Integer getNextErgebnisgruppeReihung(String mandantCNr, Short bBilanzgruppe)
			throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("mandantCNr == null"));
		}
		// try {
		Query query = em.createNamedQuery("ErgebnisgruppeejbSelectNextReihung");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, bBilanzgruppe);
		Integer i = (Integer) query.getSingleResult();
		if (i != null) {
			i = new Integer(i.intValue() + 1);
		} else {
			i = new Integer(1);
		}
		return i;
		// }
		// catch (FinderException e) {
		// {
		// return new Integer(1);
		// }
		// return new Integer(1);
		// }
	}

	/**
	 * Zwei bestehende Ergebnisgruppen in Bezug auf ihr iReihung umreihen.
	 * 
	 * @param iIdEG1I
	 *            PK der ersten Ergebnisgruppe
	 * @param iIdEG2I
	 *            PK der zweiten Ergebnisgruppe
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void vertauscheErgebnisgruppen(Integer iIdEG1I, Integer iIdEG2I,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Ergebnisgruppe oPosition1 = em.find(Ergebnisgruppe.class, iIdEG1I);
			Ergebnisgruppe oPosition2 = em.find(Ergebnisgruppe.class, iIdEG2I);
			// Positionen muessen zur gleichen Rechnung gehoeren
			if (oPosition1.getMandantCNr().equals(oPosition2.getMandantCNr())) {
				// vertauschen
				Integer iSort1 = oPosition1.getIReihung();
				Integer iSort2 = oPosition2.getIReihung();
				// das zweite iSort auf ungueltig setzen, damit UK constraint
				// nicht verletzt wird
				// oPosition2.setISort(new Integer(-1));
				oPosition1.setIReihung(iSort2);
				oPosition2.setIReihung(iSort1);
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER,
						new Exception(
								"es wurde versucht, ergebnisgruppen von verschiedenen mandanten zu vertauschen: i_id="
										+ iIdEG1I + " bzw. " + iIdEG2I));
			}
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public void createKontoland(KontolandDto kontolandDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontolandDto == null) {
			return;
		}
		try {
			kontolandDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			kontolandDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Calendar cal = Calendar.getInstance();
			kontolandDto.setTAnlegen(new Timestamp(cal.getTime().getTime()));
			kontolandDto.setTAendern(new Timestamp(cal.getTime().getTime()));
			Kontoland kontoland = new Kontoland(kontolandDto.getKontoIId(),
					kontolandDto.getLandIId(),
					kontolandDto.getKontoIIdUebersetzt(),
					kontolandDto.getPersonalIIdAnlegen(),
					kontolandDto.getPersonalIIdAendern());
			em.persist(kontoland);
			em.flush();
			setKontolandFromKontolandDto(kontoland, kontolandDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKontoland(KontolandDto kontolandDto)
			throws EJBExceptionLP {
		try {
			Kontoland toRemove = em.find(Kontoland.class, new KontolandPK(
					kontolandDto.getKontoIId(), kontolandDto.getLandIId()));
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
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateKontoland(KontolandDto kontolandDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontolandDto != null) {
			// try {
			KontolandPK pk = new KontolandPK(kontolandDto.getKontoIId(),
					kontolandDto.getLandIId());
			Kontoland kontoland = em.find(Kontoland.class, pk);
			if (kontoland == null) {
				createKontoland(kontolandDto, theClientDto);
			} else {
				kontolandDto
						.setPersonalIIdAendern(theClientDto.getIDPersonal());
				Calendar cal = Calendar.getInstance();
				kontolandDto
						.setTAendern(new Timestamp(cal.getTime().getTime()));
				setKontolandFromKontolandDto(kontoland, kontolandDto);
			}
			// }
			// catch (FinderException e) {
			// createKontoland(kontolandDto,theClientDto);
			// }
		}
	}

	public void updateKontolands(KontolandDto[] kontolandDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontolandDtos != null) {
			for (int i = 0; i < kontolandDtos.length; i++) {
				updateKontoland(kontolandDtos[i], theClientDto);
			}
		}
	}

	public KontolandDto kontolandFindByPrimaryKey(Integer kontoIId,
			Integer LandIId) throws EJBExceptionLP {
		try {
			KontolandPK pk = new KontolandPK(kontoIId, LandIId);
			Kontoland kontoland = em.find(Kontoland.class, pk);
			if (kontoland == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKontolandDto(kontoland);
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public KontolandDto kontolandFindByPrimaryKeyOhneExc(Integer kontoIId,
			Integer LandIId) throws EJBExceptionLP {
		try {
			KontolandPK pk = new KontolandPK(kontoIId, LandIId);
			Kontoland kontoland = em.find(Kontoland.class, pk);
			if (kontoland == null) {
				return null;
			}
			return assembleKontolandDto(kontoland);
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setKontolandFromKontolandDto(Kontoland kontoland,
			KontolandDto kontolandDto) {
		kontoland.setKontoIIdUebersetzt(kontolandDto.getKontoIIdUebersetzt());
		kontoland.setTAnlegen(kontolandDto.getTAnlegen());
		kontoland.setPersonalIIdAnlegen(kontolandDto.getPersonalIIdAnlegen());
		kontoland.setTAendern(kontolandDto.getTAendern());
		kontoland.setPersonalIIdAendern(kontolandDto.getPersonalIIdAendern());
		em.merge(kontoland);
		em.flush();
	}

	private KontolandDto assembleKontolandDto(Kontoland kontoland) {
		return KontolandDtoAssembler.createDto(kontoland);
	}

	private KontolandDto[] assembleKontolandDtos(Collection<?> kontolands) {
		List<KontolandDto> list = new ArrayList<KontolandDto>();
		if (kontolands != null) {
			Iterator<?> iterator = kontolands.iterator();
			while (iterator.hasNext()) {
				Kontoland kontoland = (Kontoland) iterator.next();
				list.add(assembleKontolandDto(kontoland));
			}
		}
		KontolandDto[] returnArray = new KontolandDto[list.size()];
		return list.toArray(returnArray);
	}

	public boolean isKontoMitSaldo(Integer kontoIId, TheClientDto theClientDto) {
		try {
			KassenbuchDto kbDto = kassenbuchFindByKontoIIdOhneExc(kontoIId);
			if (kbDto != null)
				return true;
		} catch (EJBExceptionLP e) {
		}

		BankverbindungDto bvDto = bankverbindungFindByKontoIIdOhneExc(kontoIId);
		return bvDto != null;
	}

	public KontoImporterResult importCsv(List<String[]> allLines,
			boolean checkOnly, TheClientDto theClientDto) {

		FinanzFac lFinanzFac = getFinanzFac();
		FinanzServiceFac lFinanzServiceFac = getFinanzServiceFac();
		SystemFac lSystemFac = getSystemFac();

		IKontoImporterBeanServices beanServices = new KontoImporterBeanService(
				theClientDto, lFinanzFac, lFinanzServiceFac, lSystemFac);
		KontoImporter importer = new KontoImporter(beanServices);

		if (allLines.size() < 2)
			return new KontoImporterResult(
					new ArrayList<EJBLineNumberExceptionLP>());

		importer.getTransformer().buildFieldIndexer(allLines.get(0));
		List<String[]> linesWithData = allLines.subList(1, allLines.size());

		return checkOnly ? importer.checkImportCsvKontos(linesWithData)
				: importer.importCsvKontos(linesWithData);
	}

	@Override
	public boolean isMitlaufendesKonto(Integer kontoIId) throws RemoteException {
		Session session = FLRSessionFactory.getFactory().openSession();
		kontoFindByPrimaryKey(kontoIId);
		org.hibernate.Query q = session.createQuery("FROM FLRFinanzKonto konto WHERE konto.i_id = " + kontoIId + " AND" + KontoQueryBuilder.buildOhneMitlaufendeKonten("konto"));
		return q.list().size() == 0;
	}
}
