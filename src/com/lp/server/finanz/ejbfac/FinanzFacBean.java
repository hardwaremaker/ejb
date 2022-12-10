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
package com.lp.server.finanz.ejbfac;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.finanz.assembler.BankverbindungDtoAssembler;
import com.lp.server.finanz.assembler.ErgebnisgruppeDtoAssembler;
import com.lp.server.finanz.assembler.FinanzamtDtoAssembler;
import com.lp.server.finanz.assembler.KassenbuchDtoAssembler;
import com.lp.server.finanz.assembler.KontoDtoAssembler;
import com.lp.server.finanz.assembler.KontoDtoSmallAssembler;
import com.lp.server.finanz.assembler.KontolaenderartDtoAssembler;
import com.lp.server.finanz.assembler.KontolandDtoAssembler;
import com.lp.server.finanz.assembler.RechenregelDtoAssembler;
import com.lp.server.finanz.ejb.Bankverbindung;
import com.lp.server.finanz.ejb.BankverbindungQuery;
import com.lp.server.finanz.ejb.Ergebnisgruppe;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Kassenbuch;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Kontolaenderart;
import com.lp.server.finanz.ejb.KontolaenderartPK;
import com.lp.server.finanz.ejb.KontolaenderartQuery;
import com.lp.server.finanz.ejb.Kontoland;
import com.lp.server.finanz.ejb.KontolandQuery;
import com.lp.server.finanz.ejb.Rechenregel;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.IKontoImporterBeanServices;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoImporter;
import com.lp.server.finanz.service.KontoImporterBeanService;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.server.finanz.service.KontoRequest;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.finanz.service.RechenregelDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerDtoAssembler;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.IBatchAction;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
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
	 * @param kontoDto     KontoDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 * @throws RemoteException
	 */
	public KontoDto createKonto(KontoDto kontoDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// Schaun, ob schon ein Konto mit dieser Nummer fuer diesen Kontotyp
		// angelegt ist.
		Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
		query.setParameter(1, kontoDto.getCNr());
		query.setParameter(2, kontoDto.getKontotypCNr());
		query.setParameter(3, kontoDto.getMandantCNr());
		try {
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception());
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
			kontoDto.setDGueltigvon(new java.sql.Date(System.currentTimeMillis()));
		}
		// wenn nicht anders angegeben, ist das konto nicht versteckt
		if (kontoDto.getBVersteckt() == null) {
			kontoDto.setBVersteckt(Helper.boolean2Short(false));
		}
		Konto konto = null;
		try {
			konto = new Konto(kontoDto.getIId(), kontoDto.getMandantCNr(), kontoDto.getCNr(), kontoDto.getCBez(),
					kontoDto.getUvaartIId(), kontoDto.getBAutomeroeffnungsbuchung(), kontoDto.getBAllgemeinsichtbar(),
					kontoDto.getBManuellbebuchbar(), kontoDto.getKontoartCNr(), kontoDto.getKontotypCNr(),
					kontoDto.getPersonalIIdAnlegen(), kontoDto.getPersonalIIdAendern());
			em.persist(konto);
			em.flush();
			kontoDto.setTAendern(konto.getTAendern());
			kontoDto.setTAnlegen(konto.getTAnlegen());
			setKontoFromKontoDto(konto, kontoDto);

			HvDtoLogger<KontoDto> logger = new HvDtoLogger<KontoDto>(em, kontoDto.getIId(), theClientDto);
			logger.logInsert(kontoDto);

			return kontoFindByPrimaryKey(konto.getIId());
		} catch (PersistenceException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
//		} catch (EntityExistsException e) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Loeschen eines Kontos.
	 *
	 * @param kontoDto     KontoDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void removeKonto(KontoDto kontoDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontoDto != null) {
			Integer iId = kontoDto.getIId();
			Konto toRemove = em.find(Konto.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();

				HvDtoLogger<KontoDto> logger = new HvDtoLogger<KontoDto>(em, kontoDto.getIId(), theClientDto);
				logger.logDelete(kontoDto);

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kontoDto != null"));
		}
	}

	private class BatchActionThrow implements IBatchAction {
		private Integer kontoId;
		private TheClientDto theClientDto;

		public BatchActionThrow(Integer kontoId, TheClientDto theClientDto) {
			this.kontoId = kontoId;
			this.theClientDto = theClientDto;
		}

		public boolean isDone() {
			return true;
		}

		public void markAsDone() {
		}

		public void run() {
			KontoDto kontoDto = kontoFindByPrimaryKey(kontoId);
			KassenbuchDto kbDto = getHauptkassabuch(theClientDto);
			String cbez = kbDto.getCBez();
			cbez += ">";
			kbDto.setCBez(cbez);
			updateKassenbuch(kbDto, theClientDto);
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception("ActionThrow")) ;
		}
	}

	private class BatchActionNewThrow implements IBatchAction {
		private Integer kontoId;
		private TheClientDto theClientDto;

		public BatchActionNewThrow(Integer kontoId, TheClientDto theClientDto) {
			this.kontoId = kontoId;
			this.theClientDto = theClientDto;
		}

		public boolean isDone() {
			return true;
		}

		public void markAsDone() {
		}

		public void run() {
			KontoDto kontoDto = kontoFindByPrimaryKey(kontoId);
			String bemerkung = kontoDto.getxBemerkung();
			if (bemerkung == null) {
				bemerkung = "";
			}

			bemerkung += " Hello";
			kontoDto.setxBemerkung(bemerkung);
//				getFinanzFac().updateKonto(kontoDto, theClientDto) ;

			KassenbuchDto kbDto = getHauptkassabuch(theClientDto);
			String cbez = kbDto.getCBez();
			cbez += "<";
			kbDto.setCBez(cbez);
			updateKassenbuch(kbDto, theClientDto);
		}
	}

	/**
	 * Update eines Kontos
	 *
	 * @param kontoDto     KontoDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDto updateKonto(KontoDto kontoDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontoDto != null) {
			Integer iId = kontoDto.getIId();
			Konto konto = em.find(Konto.class, iId);
			try {
				// SP8175
				Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
				query.setParameter(1, kontoDto.getCNr());
				query.setParameter(2, kontoDto.getKontotypCNr());
				query.setParameter(3, kontoDto.getMandantCNr());
				List<?> kl = query.getResultList();
				if (kl.size() > 1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_KONTO.C_NR"));
				}
			} catch (NoResultException ex) {

			}

			KontoDto kontoDto_Vorher = kontoFindByPrimaryKey(kontoDto.getIId());

			HvDtoLogger<KontoDto> kontoLogger = new HvDtoLogger<KontoDto>(em, theClientDto);
			kontoLogger.log(kontoDto_Vorher, kontoDto);

			// Auf Zykel preufen
			pruefeZykel(kontoDto);
			// Daten der Aenderung
			kontoDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			kontoDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			try {

				setKontoFromKontoDto(konto, kontoDto);
				return kontoFindByPrimaryKey(konto.getIId());
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("Dto is null"));
		}
	}

	/**
	 * Konto anhand Primaerschluessel finden
	 *
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDto kontoFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		try {
			Konto konto = em.find(Konto.class, iId);
			if (konto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			KontoDto ktoDto = assembleKontoDto(konto);
			if (konto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {

				KundeDto[] kdDto = getKundeFac().kundefindByKontoIIdDebitorenkonto(konto.getIId());
				if (kdDto != null && kdDto.length > 0) {
					Partner p = em.find(Partner.class, kdDto[0].getPartnerIId());

					ktoDto.setPartnerDto(PartnerDtoAssembler.createDto(p));
				}
			} else if (konto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				LieferantDto[] lDto = getLieferantFac().lieferantfindByKontoIIdKreditorenkonto(konto.getIId());
				if (lDto != null && lDto.length > 0) {
					Partner p = em.find(Partner.class, lDto[0].getPartnerIId());

					ktoDto.setPartnerDto(PartnerDtoAssembler.createDto(p));
				}
			}

			return ktoDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	/**
	 * Konto anhand Primaerschluessel finden
	 *
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	public KontoDtoSmall kontoFindByPrimaryKeySmall(Integer iId) throws EJBExceptionLP {
		// try {
		Konto konto = em.find(Konto.class, iId);
		if (konto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
	 * @param sCnr         String
	 * @param sMandant     String
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return KontoDto
	 */
	@Deprecated
	public KontoDto kontoFindByCnrMandant(String sCnr, String sMandant, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
	 * KontoDto auf Entity setzen.** @param konto Konto* @param kontoDto KontoDto
	 */
	private void setKontoFromKontoDto(Konto konto, KontoDto kontoDto) {
		konto.setMandantCNr(kontoDto.getMandantCNr());
		konto.setCNr(kontoDto.getCNr());
		konto.setCBez(kontoDto.getCBez());
		konto.setKontoIIdWeiterfuehrendust(kontoDto.getKontoIIdWeiterfuehrendUst());
		konto.setRechenregelCNrWeiterfuehrendust(kontoDto.getRechenregelCNrWeiterfuehrendUst());
		konto.setRechenregelCNrWeiterfuehrendbilanz(kontoDto.getRechenregelCNrWeiterfuehrendBilanz());
		konto.setKontoIIdWeiterfuehrendskonto(kontoDto.getKontoIIdWeiterfuehrendSkonto());
		konto.setRechenregelCNrWeiterfuehrendskonto(kontoDto.getRechenregelCNrWeiterfuehrendSkonto());
		konto.setUvaartIId(kontoDto.getUvaartIId());
		konto.setTGueltigvon(kontoDto.getDGueltigvon());
		konto.setTGueltigbis(kontoDto.getDGueltigbis());
		konto.setFinanzamtIId(kontoDto.getFinanzamtIId());
		konto.setKostenstelleIId(kontoDto.getKostenstelleIId());
		konto.setBAutomeroeffnungsbuchung(kontoDto.getBAutomeroeffnungsbuchung());
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
		konto.setSteuerkategorieCNr(kontoDto.getSteuerkategorieCnr());

//		konto.setSteuerkategorieIIdReverse(kontoDto.getSteuerkategorieIIdReverse());
		konto.setcSortierung(kontoDto.getCsortierung());
		konto.setXBemerkung(kontoDto.getxBemerkung());
		konto.setWaehrungCNrDruck(kontoDto.getWaehrungCNrDruck());
		// Diese beiden Felder werden mit einer eigenen Methode befuellt. um
		// Lost Updates zu verhindern
		// konto.setCLetzteSortierung(kontoDto.getCLetztesortierung());
		// konto.setILetzteselektiertebuchung(kontoDto.
		// getILetzteselektiertebuchung());
		konto.setiGeschaeftsjahrEB(kontoDto.getiGeschaeftsjahrEB());
		konto.setTEBAnlegen(kontoDto.gettEBAnlegen());
		konto.setErgebnisgruppeIId_negativ(kontoDto.getErgebnisgruppeIId_negativ());
		konto.setBOhneUst(kontoDto.getBOhneUst());
		konto.setcSteuerart(kontoDto.getcSteuerart());
		konto.setMwstsatzIId(kontoDto.getMwstsatzIId());

		em.merge(konto);
		em.flush();
	}

	public void updateKontoDtoUIDaten(Integer kontoIIdI, String cLetztesortierungI, Integer iLetzteselektiertebuchungI)
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

	private List<KontoDto> assembleKontoDtoList(Collection<?> kontos) {
		return KontoDtoAssembler.createDtoList(kontos);
	}

	public int getAnzahlDerFinanzaemter(TheClientDto theClientDto) throws EJBExceptionLP {
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

	public FinanzamtDto createFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto) throws EJBExceptionLP {
		finanzamtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		finanzamtDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		finanzamtDto.setMandantCNr(theClientDto.getMandant());
		try {
			Integer partnerIId = getPartnerFac().createPartner(finanzamtDto.getPartnerDto(), theClientDto);
			finanzamtDto.setPartnerIId(partnerIId);
			Finanzamt finanzamt = new Finanzamt(finanzamtDto.getPartnerIId(), finanzamtDto.getMandantCNr(),
					Helper.boolean2Short(false), finanzamtDto.getPersonalIIdAnlegen(),
					finanzamtDto.getPersonalIIdAendern());
			em.persist(finanzamt);
			em.flush();
			finanzamtDto.setTAendern(finanzamt.getTAendern());
			finanzamtDto.setTAnlegen(finanzamt.getTAnlegen());
			setFinanzamtFromFinanzamtDto(finanzamt, finanzamtDto);
			return finanzamtFindByPrimaryKey(finanzamt.getPartnerIId(), finanzamt.getMandantCNr(), theClientDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (finanzamtDto != null) {
			Integer partnerIId = finanzamtDto.getPartnerIId();
			String mandantCNr = finanzamtDto.getMandantCNr();
			// try {
			Finanzamt toRemove = em.find(Finanzamt.class, new FinanzamtPK(partnerIId, mandantCNr));
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
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public FinanzamtDto updateFinanzamt(FinanzamtDto finanzamtDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (finanzamtDto != null) {
			finanzamtDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			finanzamtDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			Integer partnerIId = finanzamtDto.getPartnerIId();
			String mandantCNr = finanzamtDto.getMandantCNr();
			try {
				// zuerst den partner updaten
				getPartnerFac().updatePartner(finanzamtDto.getPartnerDto(), theClientDto);
				// und jetz das FA
				Finanzamt finanzamt = em.find(Finanzamt.class, new FinanzamtPK(partnerIId, mandantCNr));
				setFinanzamtFromFinanzamtDto(finanzamt, finanzamtDto);
				return finanzamtFindByPrimaryKey(finanzamt.getPartnerIId(), finanzamt.getMandantCNr(), theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}

	}

	public FinanzamtDto finanzamtFindByPrimaryKey(Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Finanzamt finanzamt = em.find(Finanzamt.class, new FinanzamtPK(partnerIId, mandantCNr));
		if (finanzamt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		FinanzamtDto finanzamtDto = assembleFinanzamtDto(finanzamt);

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNr(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		FinanzamtDto finanzamtDto = null;

		Query query = em.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Finanzamt finanzamt = (Finanzamt) query.getSingleResult();
		if (finanzamt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		finanzamtDto = assembleFinanzamtDto(finanzamt);

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExc(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		FinanzamtDto finanzamtDto = null;

		Query query = em.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		// @todo getSingleResult oder getResultList ?
		try {
			finanzamtDto = assembleFinanzamtDto((Finanzamt) query.getSingleResult());
			if (finanzamtDto == null) {
				return null;
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY
				// , null);
			}

		} catch (NoResultException ex) {
			return null;
		}

		finanzamtDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(finanzamtDto.getPartnerIId(), theClientDto));
		return finanzamtDto;

	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(Integer partnerIId, String mandantCNr) {

		Query query = em.createNamedQuery("FinanzamtfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> finanzamt = query.getResultList();
		if (finanzamt == null)
			return null;
		return finanzamt.size() == 0 ? null : assembleFinanzamtDto((Finanzamt) finanzamt.iterator().next());
	}

	public FinanzamtDto[] finanzamtFindAll(TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("FinanzamtfindAll");
		Collection<?> cl = query.getResultList();
		FinanzamtDto[] finanzamtDtos = assembleFinanzamtDtos(cl);
		for (int i = 0; i < finanzamtDtos.length; i++) {
			finanzamtDtos[i].setPartnerDto(
					getPartnerFac().partnerFindByPrimaryKey(finanzamtDtos[i].getPartnerIId(), theClientDto));
		}
		return finanzamtDtos;

	}

	public FinanzamtDto[] finanzamtFindAllByMandantCNr(TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("FinanzamtfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		FinanzamtDto[] finanzamtDtos = assembleFinanzamtDtos(cl);
		for (int i = 0; i < finanzamtDtos.length; i++) {
			finanzamtDtos[i].setPartnerDto(
					getPartnerFac().partnerFindByPrimaryKey(finanzamtDtos[i].getPartnerIId(), theClientDto));
		}
		return finanzamtDtos;

	}

	private void setFinanzamtFromFinanzamtDto(Finanzamt finanzamt, FinanzamtDto finanzamtDto) {
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

//		finanzamt.setKontoIIdRCAnzahlungErhaltenBezahlt(finanzamtDto.getKontoIIdRCAnzahlungErhaltBezahlt());
		finanzamt.setKontoIIdRCAnzahlungErhaltenVerr(finanzamtDto.getKontoIIdRCAnzahlungErhaltVerr());
//		finanzamt.setKontoIIdRCAnzahlungGegebenBezahlt(finanzamtDto.getKontoIIdRCAnzahlungGegebenBezahlt());
//		finanzamt.setKontoIIdRCAnzahlungGegebenVerr(finanzamtDto.getKontoIIdRCAnzahlungGegebenVerr());

		finanzamt.setKontoIIdGewinnvortrag(finanzamtDto.getKontoIIdGewinnvortrag());
		finanzamt.setKontoIIdJahresgewinn(finanzamtDto.getKontoIIdJahresgewinn());

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
	 * @param bankverbindungDto BankverbindungDto
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 * @return BankverbindungDto
	 */
	public BankverbindungDto createBankverbindung(BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_BANKVERBINDUNG);
		if (bankverbindungDto != null) {
			bankverbindungDto.setIId(pk);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			bankverbindungDto.setTAendern(now);
			bankverbindungDto.setTAnlegen(now);
			bankverbindungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			bankverbindungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			// Gibts schon eine mit dieser Kontonummer oder IBAN bei dieser Bank bei
			// diesem Mandanten?
			Bankverbindung bvEntity = BankverbindungQuery.resultByBankIIdMandantCNrCIban(em,
					bankverbindungDto.getMandantCNr(), bankverbindungDto.getBankIId(), bankverbindungDto.getCIban());
			if (bvEntity != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						"mandantCnr=" + bankverbindungDto.getMandantCNr() + ", bankIId="
								+ bankverbindungDto.getBankIId() + ", cIban="
								+ String.valueOf(bankverbindungDto.getCIban()));
			}
			bvEntity = BankverbindungQuery.resultByBankIIdMandantCNrCKontonummer(em, bankverbindungDto.getMandantCNr(),
					bankverbindungDto.getBankIId(), bankverbindungDto.getCKontonummer());
			if (bvEntity != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						"mandantCnr=" + bankverbindungDto.getMandantCNr() + ", bankIId="
								+ bankverbindungDto.getBankIId() + ", cKontonummer="
								+ String.valueOf(bankverbindungDto.getCKontonummer()));
			}

			// PJ19922
			// bFuerSepaLastschrift darf nur einmal in Bankverbindung pro Mandant gesetzt
			// sein
			if (bankverbindungDto.isbFuerSepaLastschrift()) {
				resetBFuerSepaLastschriftAllerBankverbindungen(theClientDto.getMandant());
			}

			try {
				Query query = em.createNamedQuery("BankverbindungfindByKontoIId");
				query.setParameter(1, bankverbindungDto.getKontoIId());
				// @todo getSingleResult oder getResultList ?
				Bankverbindung temp = (Bankverbindung) query.getSingleResult();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN, "");
			} catch (NoResultException ex) {
				try {
					Bankverbindung bankverbindung = new Bankverbindung(bankverbindungDto.getIId(),
							bankverbindungDto.getMandantCNr(), bankverbindungDto.getBankIId(),
							bankverbindungDto.getKontoIId(), bankverbindungDto.getPersonalIIdAnlegen(),
							bankverbindungDto.getPersonalIIdAendern(),
							Helper.boolean2Short(bankverbindungDto.isbFuerSepaLastschrift()));
					em.persist(bankverbindung);
					em.flush();
					setBankverbindungFromBankverbindungDto(bankverbindung, bankverbindungDto);
					return bankverbindungFindByPrimaryKey(bankverbindung.getIId());
				} catch (Exception e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
				}
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto is null"));
		}
	}

	public void removeBankverbindung(BankverbindungDto bankverbindungDto) throws EJBExceptionLP {
		if (bankverbindungDto != null) {
			Integer iId = bankverbindungDto.getIId();
			// try {
			Bankverbindung toRemove = em.find(Bankverbindung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			Iso20022BankverbindungDto iso20022bankverbindungDto = getSepaImportFac()
					.iso20022BankverbindungFindByBankverbindungIIdNoExc(new BankverbindungId(iId));
			if (iso20022bankverbindungDto != null) {
				getSepaImportFac().removeIso20022Bankverbindung(iso20022bankverbindungDto.getIId());
			}

			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("bankverbindungDto != null)"));
		}
	}

	public BankverbindungDto updateBankverbindung(BankverbindungDto bankverbindungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (bankverbindungDto != null) {
			Integer iId = bankverbindungDto.getIId();
			bankverbindungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			bankverbindungDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			Bankverbindung bankverbindung = em.find(Bankverbindung.class, iId);
			// PJ19922
			// bFuerSepaLastschrift darf nur einmal in Bankverbindung pro Mandant gesetzt
			// sein
			if (bankverbindungDto.isbFuerSepaLastschrift()
					&& !Helper.short2boolean(bankverbindung.getbFuerSepaLastschrift())) {
				resetBFuerSepaLastschriftAllerBankverbindungen(theClientDto.getMandant());
			}

			try {

				Bankverbindung bvEntity = BankverbindungQuery.resultByBankIIdMandantCNrCIban(em,
						bankverbindungDto.getMandantCNr(), bankverbindungDto.getBankIId(),
						bankverbindungDto.getCIban());
				if (bvEntity != null && iId != bvEntity.getIId()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "mandantCnr, bankIId, cIban");
				}
				bvEntity = BankverbindungQuery.resultByBankIIdMandantCNrCKontonummer(em,
						bankverbindungDto.getMandantCNr(), bankverbindungDto.getBankIId(),
						bankverbindungDto.getCKontonummer());
				if (bvEntity != null && iId != bvEntity.getIId()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							"mandantCnr, bankIId, cKontonummer");
				}

				// Schaun, ob das Konto bereits einer anderen bankverbindung
				// zugewiesen ist
				Query query = em.createNamedQuery("BankverbindungfindByKontoIId");
				query.setParameter(1, bankverbindungDto.getKontoIId());
				// @todo getSingleResult oder getResultList ?
				Bankverbindung bv = (Bankverbindung) query.getSingleResult();
				// falls es dasselbe ist, dann passts auch ;-)
				if (!bv.getIId().equals(bankverbindungDto.getIId())) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN, "");
				} else {
					setBankverbindungFromBankverbindungDto(bankverbindung, bankverbindungDto);
					return bankverbindungDto;
				}

			} catch (NoResultException ex) {
				try {

					setBankverbindungFromBankverbindungDto(bankverbindung, bankverbindungDto);
					return bankverbindungDto;
				} catch (Exception e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
				}
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("bankverbindungDto != null"));
		}
	}

	private void resetBFuerSepaLastschriftAllerBankverbindungen(String mandantCnr) {
		List<Bankverbindung> bankverbindungen = BankverbindungQuery.listByMandantCNrBFuerSepaLastschrift(em, mandantCnr,
				true);
		for (Bankverbindung entity : bankverbindungen) {
			entity.setbFuerSepaLastschrift(Helper.boolean2Short(false));
			em.merge(entity);
		}
		em.flush();
	}

	public BankverbindungDto bankverbindungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		try {
			Bankverbindung bankverbindung = em.find(Bankverbindung.class, iId);
			if (bankverbindung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBankverbindungDto(bankverbindung);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public BankverbindungDto bankverbindungFindByKontoIIdOhneExc(Integer kontoIId) throws EJBExceptionLP {
		HvTypedQuery<Bankverbindung> query = new HvTypedQuery<Bankverbindung>(
				em.createNamedQuery("BankverbindungfindByKontoIId"));
		query.setParameter(1, kontoIId);
		List<Bankverbindung> list = query.getResultList();
		if (list == null || list.size() == 0)
			return null;
		if (list.size() > 1)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		return assembleBankverbindungDto(list.get(0));
	}

	public BankverbindungDto bankverbindungFindByBankIIdMandantCNrCKontonummerOhneExc(Integer bankIId,
			String mandantCNr, String cKontonummer) throws EJBExceptionLP {
		BankverbindungDto bankverbindungDto = null;
		try {
			Query query = em.createNamedQuery("BankverbindungfindByBankIIdMandantCNrCKontonummer");
			query.setParameter(1, bankIId);
			query.setParameter(2, mandantCNr);
			query.setParameter(3, cKontonummer);
			Bankverbindung bankverbindung = (Bankverbindung) query.getSingleResult();
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

	private void setBankverbindungFromBankverbindungDto(Bankverbindung bankverbindung,
			BankverbindungDto bankverbindungDto) {
		bankverbindung.setMandantCNr(bankverbindungDto.getMandantCNr());
		bankverbindung.setBankIId(bankverbindungDto.getBankIId());
		bankverbindung.setCKontonummer(bankverbindungDto.getCKontonummer());
		bankverbindung.setKontoIId(bankverbindungDto.getKontoIId());
		bankverbindung.setCBez(bankverbindungDto.getCBez());
		bankverbindung.setCIban(bankverbindungDto.getCIban());
		bankverbindung.setTAnlegen(bankverbindungDto.getTAnlegen());
		bankverbindung.setPersonalIIdAnlegen(bankverbindungDto.getPersonalIIdAnlegen());
		bankverbindung.setTAendern(bankverbindungDto.getTAendern());
		bankverbindung.setPersonalIIdAendern(bankverbindungDto.getPersonalIIdAendern());
		bankverbindung.setbInLiquiditaetsvorschau(Helper.boolean2Short(bankverbindungDto.isbInLiquiditaetsVorschau()));
		bankverbindung.setCSepaVerzeichnis(bankverbindungDto.getCSepaVerzeichnis());
		bankverbindung.setbFuerSepaLastschrift(Helper.boolean2Short(bankverbindungDto.isbFuerSepaLastschrift()));
		bankverbindung.setbAlsGeldtransitkonto(Helper.boolean2Short(bankverbindungDto.isbAlsGeldtransitkonto()));
		bankverbindung.setIStellenAuszugsnummer(bankverbindungDto.getIStellenAuszugsnummer());
		em.merge(bankverbindung);
		em.flush();
	}

	private BankverbindungDto assembleBankverbindungDto(Bankverbindung bankverbindung) {
		return BankverbindungDtoAssembler.createDto(bankverbindung);
	}

	private BankverbindungDto[] assembleBankverbindungDtos(Collection<?> bankverbindungs) {
		List<BankverbindungDto> list = new ArrayList<BankverbindungDto>();
		if (bankverbindungs != null) {
			Iterator<?> iterator = bankverbindungs.iterator();
			while (iterator.hasNext()) {
				Bankverbindung bankverbindung = (Bankverbindung) iterator.next();
				list.add(assembleBankverbindungDto(bankverbindung));
			}
		}
		BankverbindungDto[] returnArray = new BankverbindungDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * Anlegen eines neuen Kassenbuchs
	 *
	 * @param kassenbuchDto KassenbuchDto
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto createKassenbuch(KassenbuchDto kassenbuchDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// PK generieren
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_KASSENBUCH);
		kassenbuchDto.setIId(pk);
		// anlegen, aendern
		Timestamp now = new Timestamp(System.currentTimeMillis());
		kassenbuchDto.setTAendern(now);
		kassenbuchDto.setTAnlegen(now);
		kassenbuchDto.setPersonalIIdaendern(theClientDto.getIDPersonal());
		kassenbuchDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Kassenbuch kassenbuch = new Kassenbuch(kassenbuchDto.getIId(), kassenbuchDto.getMandantCNr(),
					kassenbuchDto.getCBez(), kassenbuchDto.getKontoIId(), kassenbuchDto.getBNegativErlaubt(),
					kassenbuchDto.getBHauptkassenbuch(), kassenbuchDto.getDGueltigVon(),
					kassenbuchDto.getPersonalIIdAnlegen(), kassenbuchDto.getPersonalIIdaendern());
			em.persist(kassenbuch);
			em.flush();
			setKassenbuchFromKassenbuchDto(kassenbuch, kassenbuchDto);

			HvDtoLogger<KassenbuchDto> logger = new HvDtoLogger<KassenbuchDto>(em, theClientDto);
			logger.logInsert(kassenbuchDto);

			return kassenbuchFindByPrimaryKey(kassenbuch.getIId(), theClientDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Loeschen eines Kassenbuchs
	 *
	 * @param kassenbuchDto KassenbuchDto
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	public void removeKassenbuch(KassenbuchDto kassenbuchDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kassenbuchDto != null) {
			Integer iId = kassenbuchDto.getIId();
			// try {
			Kassenbuch toRemove = em.find(Kassenbuch.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			HvDtoLogger<KassenbuchDto> logger = new HvDtoLogger<KassenbuchDto>(em, theClientDto);
			logger.logDelete(kassenbuchDto);

			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kassenbuchDto != null"));
		}
	}

	/**
	 * Update eines Kassenbuchs
	 *
	 * @param kassenbuchDto KassenbuchDto
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto updateKassenbuch(KassenbuchDto kassenbuchDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (kassenbuchDto != null) {
			Integer iId = kassenbuchDto.getIId();
			kassenbuchDto.setPersonalIIdaendern(theClientDto.getIDPersonal());
			kassenbuchDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			try {
				Kassenbuch kassenbuch = em.find(Kassenbuch.class, iId);

				KassenbuchDto dto_Vorher = kassenbuchFindByPrimaryKey(kassenbuchDto.getIId(), theClientDto);

				HvDtoLogger<KassenbuchDto> logger = new HvDtoLogger<KassenbuchDto>(em, theClientDto);
				logger.log(dto_Vorher, kassenbuchDto);

				setKassenbuchFromKassenbuchDto(kassenbuch, kassenbuchDto);
				return kassenbuchFindByPrimaryKey(kassenbuch.getIId(), theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("Dto is null"));
		}
	}

	/**
	 * Kassenbuch anhand PK finden
	 *
	 * @param iId          Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return KassenbuchDto
	 */
	public KassenbuchDto kassenbuchFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Kassenbuch kassenbuch = em.find(Kassenbuch.class, iId);
			if (kassenbuch == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKassenbuchDto(kassenbuch);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public KassenbuchDto getHauptkassabuch(TheClientDto theClientDto) {

		Query query = em.createNamedQuery("KassenbuchfindByBHauptkassenbuch");
		query.setParameter(1, Helper.boolean2Short(true));
		query.setParameter(2, theClientDto.getMandant());
		Collection<Kassenbuch> cl = query.getResultList();

		if (cl.iterator().hasNext()) {
			return assembleKassenbuchDto(cl.iterator().next());
		} else {
			return null;
		}

	}

	public KassenbuchDto kassenbuchFindByKontoIIdOhneExc(Integer kontoIId) throws EJBExceptionLP {
		HvTypedQuery<Kassenbuch> query = new HvTypedQuery<Kassenbuch>(em.createNamedQuery("KassenbuchfindByKontoIId"));
		query.setParameter(1, kontoIId);
		List<Kassenbuch> list = query.getResultList();
		if (list == null || list.size() == 0)
			return null;
		if (list.size() > 1)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "");
		return assembleKassenbuchDto(list.get(0));
	}

	/***
	 * Dto auf Entity uebertragen** @param kassenbuch Kassenbuch* @param
	 * kassenbuchDto KassenbuchDto
	 */
	private void setKassenbuchFromKassenbuchDto(Kassenbuch kassenbuch, KassenbuchDto kassenbuchDto) {
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
	 * @param kassenbuch Kassenbuch
	 * @return KassenbuchDto
	 */
	private KassenbuchDto assembleKassenbuchDto(Kassenbuch kassenbuch) {
		return KassenbuchDtoAssembler.createDto(kassenbuch);
	}

	/**
	 * Entitys auf Dtos uebertragen
	 *
	 * @param kassenbuchs Collection
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

	private void pruefeZykelUst(Integer kontoIId, Integer kontoIIdWeiterfuehrend) throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_UST, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendUst();
		}
	}

	private void pruefeZykelSkonto(Integer kontoIId, Integer kontoIIdWeiterfuehrend) throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_SKONTO, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendSkonto();
		}
	}

	private void pruefeZykelBilanz(Integer kontoIId, Integer kontoIIdWeiterfuehrend) throws EJBExceptionLP {
		// init mit erstem
		Integer kontoIIdNext = kontoIIdWeiterfuehrend;
		while (kontoIIdNext != null) {
			if (kontoIId.equals(kontoIIdNext)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_BILANZ, "");
			}
			// und ab zum naechsten
			KontoDto kontoDtoNext = kontoFindByPrimaryKey(kontoIIdNext);
			kontoIIdNext = kontoDtoNext.getKontoIIdWeiterfuehrendBilanz();
		}

	}

	public void pruefeZykel(KontoDto kontoDto) throws EJBExceptionLP {
		pruefeZykelBilanz(kontoDto.getIId(), kontoDto.getKontoIIdWeiterfuehrendBilanz());
		pruefeZykelUst(kontoDto.getIId(), kontoDto.getKontoIIdWeiterfuehrendUst());
		pruefeZykelSkonto(kontoDto.getIId(), kontoDto.getKontoIIdWeiterfuehrendSkonto());
	}

	public RechenregelDto rechenregelFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		try {
			Rechenregel rechenregel = em.find(Rechenregel.class, cNr);
			if (rechenregel == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleRechenregelDto(rechenregel);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
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

	public ErgebnisgruppeDto createErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_BUCHUNGSREGELGEGENKONTO);
		ergebnisgruppeDto.setIId(iId);
		ergebnisgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		ergebnisgruppeDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		if (ergebnisgruppeDto.getIReihung() == null) {
			// Hinten anreihen
			ergebnisgruppeDto.setIReihung(
					getNextErgebnisgruppeReihung(theClientDto.getMandant(), ergebnisgruppeDto.getBBilanzgruppe()));
		}
		try {
			Ergebnisgruppe ergebnisgruppe = new Ergebnisgruppe(ergebnisgruppeDto.getIId(),
					ergebnisgruppeDto.getMandantCNr(), ergebnisgruppeDto.getCBez(), ergebnisgruppeDto.getIReihung(),
					ergebnisgruppeDto.getBSummeNegativ(), ergebnisgruppeDto.getBInvertiert(),
					ergebnisgruppeDto.getPersonalIIdAnlegen(), ergebnisgruppeDto.getPersonalIIdAendern(),
					ergebnisgruppeDto.getBProzentbasis(), ergebnisgruppeDto.getITyp(),
					ergebnisgruppeDto.getBBilanzgruppe(), ergebnisgruppeDto.getBJahresgewinn());
			em.persist(ergebnisgruppe);
			em.flush();
			ergebnisgruppeDto.setTAendern(ergebnisgruppe.getTAendern());
			ergebnisgruppeDto.setTAnlegen(ergebnisgruppe.getTAnlegen());
			setErgebnisgruppeFromErgebnisgruppeDto(ergebnisgruppe, ergebnisgruppeDto);
			return ergebnisgruppeDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (ergebnisgruppeDto != null) {
			Integer iId = ergebnisgruppeDto.getIId();
			// try {
			Ergebnisgruppe toRemove = em.find(Ergebnisgruppe.class, iId);
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
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public ErgebnisgruppeDto updateErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (ergebnisgruppeDto != null) {
			ergebnisgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			ergebnisgruppeDto.setTAendern(super.getTimestamp());
			Integer iId = ergebnisgruppeDto.getIId();
			try {
				Ergebnisgruppe ergebnisgruppe = em.find(Ergebnisgruppe.class, iId);
				setErgebnisgruppeFromErgebnisgruppeDto(ergebnisgruppe, ergebnisgruppeDto);
				return ergebnisgruppeFindByPrimaryKey(ergebnisgruppe.getIId());
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}
	}

	public ErgebnisgruppeDto ergebnisgruppeFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
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
		query.setParameter(2, Helper.boolean2Short(bilanzgruppe));
		Collection<?> cl = query.getResultList();
		ErgebnisgruppeDto[] ergebnisgruppeDtos = assembleErgebnisgruppeDtos(cl);
		return ergebnisgruppeDtos;
	}

	private void setErgebnisgruppeFromErgebnisgruppeDto(Ergebnisgruppe ergebnisgruppe,
			ErgebnisgruppeDto ergebnisgruppeDto) {
		ergebnisgruppe.setMandantCNr(ergebnisgruppeDto.getMandantCNr());
		ergebnisgruppe.setCBez(ergebnisgruppeDto.getCBez());
		ergebnisgruppe.setErgebnisgruppeIIdSumme(ergebnisgruppeDto.getErgebnisgruppeIIdSumme());
		ergebnisgruppe.setIReihung(ergebnisgruppeDto.getIReihung());
		ergebnisgruppe.setBSummenegativ(ergebnisgruppeDto.getBSummeNegativ());
		ergebnisgruppe.setBInvertiert(ergebnisgruppeDto.getBInvertiert());
		ergebnisgruppe.setTAnlegen(ergebnisgruppeDto.getTAnlegen());
		ergebnisgruppe.setPersonalIIdAnlegen(ergebnisgruppeDto.getPersonalIIdAnlegen());
		ergebnisgruppe.setTAendern(ergebnisgruppeDto.getTAendern());
		ergebnisgruppe.setPersonalIIdAendern(ergebnisgruppeDto.getPersonalIIdAendern());
		ergebnisgruppe.setBProzentbasis(ergebnisgruppeDto.getBProzentbasis());
		ergebnisgruppe.setITyp(ergebnisgruppeDto.getITyp());
		ergebnisgruppe.setBBilanzgruppe(ergebnisgruppeDto.getBBilanzgruppe());
		ergebnisgruppe.setBJahresgewinn(ergebnisgruppeDto.getBJahresgewinn());
		em.merge(ergebnisgruppe);
		em.flush();
	}

	private ErgebnisgruppeDto assembleErgebnisgruppeDto(Ergebnisgruppe ergebnisgruppe) {
		return ErgebnisgruppeDtoAssembler.createDto(ergebnisgruppe);
	}

	private ErgebnisgruppeDto[] assembleErgebnisgruppeDtos(Collection<?> ergebnisgruppes) {
		List<ErgebnisgruppeDto> list = new ArrayList<ErgebnisgruppeDto>();
		if (ergebnisgruppes != null) {
			Iterator<?> iterator = ergebnisgruppes.iterator();
			while (iterator.hasNext()) {
				Ergebnisgruppe ergebnisgruppe = (Ergebnisgruppe) iterator.next();
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

	/**
	 *
	 * @param cNrI
	 * @param sMandantI
	 * @param theClientDto
	 * @return KontoDto passend zur cNrI
	 * @throws RemoteException
	 *
	 * @deprecated use
	 *             {@link #kontoFindByCnrKontotypMandantOhneExc(String, String, String, TheClientDto)}
	 * @see #kontoFindByCnrKontotypMandantOhneExc(String, String, String,
	 *      TheClientDto)
	 */
	@Deprecated
	public KontoDto kontoFindByCnrMandantOhneExc(String cNrI, String sMandantI, TheClientDto theClientDto) {
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

	public KontoDto kontoFindByCnrKontotypMandantOhneExc(String cNrI, String kontotypCnr, String mandant,
			TheClientDto theClientDto) throws RemoteException {

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

		try {
			Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
			query.setParameter(1, cNrI);
			query.setParameter(2, kontotypCnr);
			query.setParameter(3, mandant);
			return assembleKontoDto((Konto) query.getSingleResult());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public KontoDto[] kontoFindAllByKontotypMandant(String kontotypCNr, String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOTYP_MANDANT);
		query.setParameter(1, kontotypCNr);
		query.setParameter(2, mandantCNr);
		return assembleKontoDtos(query.getResultList());
	}

	public KontoDto[] kontoFindAllByKontoartMandant(String kontoartCNr, String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT);
		query.setParameter(1, kontoartCNr);
		query.setParameter(2, mandantCNr);
		return assembleKontoDtos(query.getResultList());
	}

	public KontoDto[] kontoFindAllByKontoartMandantFinanzamt(String kontoartCNr, String mandantCNr,
			Integer finanzamtIid) throws EJBExceptionLP {
		return assembleKontoDtos(kontosFindByKontoartMandantFinanzamt(kontoartCNr, mandantCNr, finanzamtIid));
//		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT_FINANZAMT);
//		query.setParameter(1, kontoartCNr);
//		query.setParameter(2, mandantCNr);
//		query.setParameter(3, finanzamtIid);
//		return assembleKontoDtos(query.getResultList());
	}

	private List<Konto> kontosFindByKontoartMandantFinanzamt(String kontoartCnr, String mandantCnr,
			Integer finanzamtId) {
		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT_FINANZAMT);
		query.setParameter(1, kontoartCnr);
		query.setParameter(2, mandantCnr);
		query.setParameter(3, finanzamtId);
		return query.getResultList();
	}

	public List<KontoDto> kontoFindAllByKontoartMandantFinanzamtList(String kontoartCNr, String mandantCNr,
			Integer finanzamtIid) {
		return assembleKontoDtoList(kontosFindByKontoartMandantFinanzamt(kontoartCNr, mandantCNr, finanzamtIid));
//		Query query = em.createNamedQuery(Konto.QUERY_ALL_KONTOART_MANDANT_FINANZAMT);
//		query.setParameter(1, kontoartCNr);
//		query.setParameter(2, mandantCNr);
//		query.setParameter(3, finanzamtIid);
//		return assembleKontoDtos(query.getResultList());
	}

	public Integer getAnzahlStellenVonKontoNummer(String kontotypCNr, String mandantCNr) throws EJBExceptionLP {
		try {
			String parameterCNr;
			if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN;
			} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_DEBITORENKONTEN;
			} else {
				parameterCNr = ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_KREDITORENKONTEN;
			}
			ParametermandantDto parameter = getParameterFac().getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_FINANZ, parameterCNr);
			int iStellen = Integer.parseInt(parameter.getCWert());
			return new Integer(iStellen);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private void throwIfDuplicate(KontolaenderartDto dto) throws EJBExceptionLP {
		KontolaenderartQuery q = new KontolaenderartQuery(em);
		HvOptional<Kontolaenderart> entity = q.find(dto.getMandantCNr(), dto.getKontoIId(), dto.getFinanzamtIId(),
				dto.getLaenderartCNr(), dto.getReversechargeartIId(), dto.getTGueltigAb());
		boolean duplicate = entity.isPresent() && (dto.getIId() == null || !dto.getIId().equals(entity.get().getIId()));
		if (duplicate) {
			throw EJBExcFactory.duplicateUniqueKey("FB_KONTOLAENDERART", dto.getMandantCNr(), dto.getKontoIId(),
					dto.getFinanzamtIId(), dto.getLaenderartCNr(), dto.getReversechargeartIId(), dto.getTGueltigAb());
		}
	}

	private void throwIfDuplicate(KontolandDto dto) throws EJBExceptionLP {
		KontolandQuery q = new KontolandQuery(em);
		HvOptional<Kontoland> entity = q.find(dto.getLandIId(), dto.getKontoIId(), dto.getTGueltigAb());
		boolean duplicate = entity.isPresent() && (dto.getIId() == null || !dto.getIId().equals(entity.get().getIId()));
		if (duplicate) {
			throw EJBExcFactory.duplicateUniqueKey("FB_KONTOLAND", dto.getLandIId(), dto.getKontoIId(),
					dto.getTGueltigAb());
		}
	}

	public KontolaenderartDto createKontolaenderart(KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.dtoNotNull(kontolaenderartDto, "kontolaenderartDto");
		Validator.notNull(kontolaenderartDto.getKontoIId(), "kontoIId");
		Validator.notNull(kontolaenderartDto.getKontoIIdUebersetzt(), "kontoIIdUebersetzt");
		Validator.notNull(kontolaenderartDto.getTGueltigAb(), "gueltigAb");

		/*
		 * SP8308 Es muss moeglich sein, dass das Konto auf sich selbst zeigt, weil ich
		 * eine Deaktivierung einer Kontouebersetzung benoetige.
		 * 
		 * 01.07.2020 1418 -> 1419 01.01.2021 1418 -> 1418
		 * 
		 * if (kontolaenderartDto.getKontoIId().equals(kontolaenderartDto.
		 * getKontoIIdUebersetzt())) throw new EJBExceptionLP(EJBExceptionLP.
		 * FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST, "kontoiid = " +
		 * kontolaenderartDto.getKontoIId());
		 */
		throwIfDuplicate(kontolaenderartDto);

		kontolaenderartDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kontolaenderartDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KONTOLAENDERART);
			Kontolaenderart kontolaenderart = new Kontolaenderart(kontolaenderartDto.getKontoIId(),
					kontolaenderartDto.getLaenderartCNr(), kontolaenderartDto.getFinanzamtIId(),
					kontolaenderartDto.getMandantCNr(), kontolaenderartDto.getReversechargeartIId(),
					kontolaenderartDto.getKontoIIdUebersetzt(), kontolaenderartDto.getPersonalIIdAnlegen(),
					kontolaenderartDto.getPersonalIIdAendern(), kontolaenderartDto.getTGueltigAb());
			kontolaenderart.setIId(pk);
			em.persist(kontolaenderart);
			em.flush();

			kontolaenderartDto.setTAendern(kontolaenderart.getTAendern());
			kontolaenderartDto.setTAnlegen(kontolaenderart.getTAnlegen());
			setKontolaenderartFromKontolaenderartDto(kontolaenderart, kontolaenderartDto);
			return kontolaenderartDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKontolaenderart(KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(kontolaenderartDto, "kontolaenderartDt");
		Validator.pkFieldNotNull(kontolaenderartDto.getIId(), "IID");

		flushRemove(em, Kontolaenderart.class, kontolaenderartDto.getIId());

		/*
		 * KontolaenderartPK kontolaenderartPK = new
		 * KontolaenderartPK(kontolaenderartDto.getKontoIId(),
		 * kontolaenderartDto.getLaenderartCNr(), kontolaenderartDto.getFinanzamtIId(),
		 * kontolaenderartDto.getMandantCNr(),
		 * kontolaenderartDto.getReversechargeartIId());
		 * 
		 * Kontolaenderart toRemove = em.find(Kontolaenderart.class, kontolaenderartPK);
		 * if (toRemove == null) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ""); } try {
		 * em.remove(toRemove); em.flush(); } catch (EntityExistsException er) { throw
		 * new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er); }
		 */
	}

	public KontolaenderartDto updateKontolaenderart(KontolaenderartDto kontolaenderartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.dtoNotNull(kontolaenderartDto, "kontolaenderartDto");
		Validator.notNull(kontolaenderartDto.getKontoIId(), "kontoIId");
		Validator.notNull(kontolaenderartDto.getKontoIIdUebersetzt(), "kontoIIdUebersetzt");
		Validator.notNull(kontolaenderartDto.getTGueltigAb(), "gueltigAb");

		if (kontolaenderartDto.getIId() == null) {
			return createKontolaenderart(kontolaenderartDto, theClientDto);
		}

		// Auskommentiert aufgrund SP9377
		/*
		 * if (kontolaenderartDto.getKontoIId().equals(
		 * kontolaenderartDto.getKontoIIdUebersetzt())) { throw new
		 * EJBExceptionLP(EJBExceptionLP
		 * .FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST, "kontoiid = " +
		 * kontolaenderartDto.getKontoIId()); }
		 */

		throwIfDuplicate(kontolaenderartDto);

		kontolaenderartDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kontolaenderartDto.setTAendern(getTimestamp());

		Kontolaenderart kontolaenderart = em.find(Kontolaenderart.class, kontolaenderartDto.getIId());
		Validator.entityFound(kontolaenderart, kontolaenderartDto.getIId());
		setKontolaenderartFromKontolaenderartDto(kontolaenderart, kontolaenderartDto);
		return kontolaenderartDto;
	}

	@Override
	public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontolaenderartIId) {
		Validator.notNull(kontolaenderartIId, "kontolaenderartIId");
		Kontolaenderart entity = em.find(Kontolaenderart.class, kontolaenderartIId);
		return assembleKontolaenderartDto(entity);
	}

	/*
	 * @Override public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer
	 * kontoIId, String laenderartCNr, Integer finanzamtIId, String mandantCNr)
	 * throws RemoteException, EJBExceptionLP { KontolaenderartDto
	 * kontolaenderartDto = kontolaenderartFindByPrimaryKeyOhneExc(kontoIId,
	 * laenderartCNr, finanzamtIId, mandantCNr); if (kontolaenderartDto == null) {
	 * throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	 * "Kontolaenderart '" + laenderartCNr + "' fuer KontoId '" + kontoIId +
	 * "' im FinanzamtId '" + finanzamtIId + "' nicht gefunden"); } return
	 * kontolaenderartDto; }
	 */

	@Override
	/**
	 * TODO: Nur im Client, ersetzen durch
	 * 
	 * @see #kontolaenderartFindByPrimaryKey(java.lang.Integer)
	 */
	public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontoIId, Integer reversechargeartId,
			String laenderartCNr, Integer finanzamtIId, String mandantCNr) throws RemoteException, EJBExceptionLP {
		KontolaenderartDto kontolaenderartDto = kontolaenderartFindByPrimaryKeyOhneExc(kontoIId, reversechargeartId,
				laenderartCNr, finanzamtIId, mandantCNr);
		if (kontolaenderartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "Kontolaenderart '" + laenderartCNr
					+ "' fuer KontoId '" + kontoIId + "' im FinanzamtId '" + finanzamtIId + "' nicht gefunden");
		}
		return kontolaenderartDto;
	}

	public KontolaenderartDto kontolaenderartFindByPrimaryKeyOhneExc(Integer kontoIId, Integer reversechargeartId,
			String laenderartCNr, Integer finanzamtIId, String mandantCNr) throws EJBExceptionLP {

		KontolaenderartPK kontolaenderartPK = new KontolaenderartPK(kontoIId, laenderartCNr, finanzamtIId, mandantCNr,
				reversechargeartId);
		Kontolaenderart kontolaenderart = em.find(Kontolaenderart.class, kontolaenderartPK);
		if (kontolaenderart == null) {
			myLogger.warn("Kontolaenderart '" + laenderartCNr + "' fuer kontoIId=" + kontoIId + "', Finanzamt '"
					+ finanzamtIId + "' und ReversechargeartId=" + reversechargeartId + " nicht gefunden.");
			return null;
		}
		return assembleKontolaenderartDto(kontolaenderart);
	}

	@Override
	public HvOptional<KontolaenderartDto> kontolaenderartZuDatum(Integer kontoId, Integer reversechargeartId,
			String laenderartCnr, Integer finanzamtId, String mandantCnr, Timestamp gueltigZum) {
		Validator.notNull(kontoId, "kontoId");
		Validator.notNull(reversechargeartId, "reversechargeartId");
		Validator.notNull(laenderartCnr, "laenderartCnr");
		Validator.notNull(finanzamtId, "finanzamtId");
		Validator.notEmpty(mandantCnr, "mandantCnr");
		Validator.notNull(gueltigZum, "gueltigZum");

		KontolaenderartQuery q = new KontolaenderartQuery(em);
		HvOptional<Kontolaenderart> entity = q.findZuDatum(mandantCnr, kontoId, finanzamtId, laenderartCnr,
				reversechargeartId, gueltigZum);
		if (entity.isPresent()) {
			return HvOptional.of(assembleKontolaenderartDto(entity.get()));
		}
		return HvOptional.empty();
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(int iSortierungNeuePositionI,
			boolean bBilanzgruppe, TheClientDto theClientDto) {
		// try {
		Query query = em.createNamedQuery("ErgebnisgruppefindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, Helper.boolean2Short(bBilanzgruppe));
		Collection<?> cl = query.getResultList();

		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Ergebnisgruppe oPreisliste = (Ergebnisgruppe) it.next();

			if (oPreisliste.getIReihung().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPreisliste.setIReihung(new Integer(iSortierungNeuePositionI));
			}
		}

	}

	private void setKontolaenderartFromKontolaenderartDto(Kontolaenderart kontolaenderart,
			KontolaenderartDto kontolaenderartDto) {
		kontolaenderart.setKontoIIdUebersetzt(kontolaenderartDto.getKontoIIdUebersetzt());
		kontolaenderart.setTAnlegen(kontolaenderartDto.getTAnlegen());
		kontolaenderart.setPersonalIIdAnlegen(kontolaenderartDto.getPersonalIIdAnlegen());
		kontolaenderart.setTAendern(kontolaenderartDto.getTAendern());
		kontolaenderart.setPersonalIIdAendern(kontolaenderartDto.getPersonalIIdAendern());
		kontolaenderart.setReversechargeartId(kontolaenderartDto.getReversechargeartIId());
		kontolaenderart.setTGueltigAb(kontolaenderartDto.getTGueltigAb());
		kontolaenderart.setFinanzamtIId(kontolaenderartDto.getFinanzamtIId());
		kontolaenderart.setLaenderartCNr(kontolaenderartDto.getLaenderartCNr());
		em.merge(kontolaenderart);
		em.flush();
	}

	private KontolaenderartDto assembleKontolaenderartDto(Kontolaenderart kontolaenderart) {
		return KontolaenderartDtoAssembler.createDto(kontolaenderart);
	}

	private KontolaenderartDto[] assembleKontolaenderartDtos(Collection<?> kontolaenderarts) {
		return KontolaenderartDtoAssembler.createDtos(kontolaenderarts);
	}

	public void pruefeBuchungszeitraum(java.util.Date d, TheClientDto theClientDto) throws EJBExceptionLP {
		ParametermandantDto pmMonate = null;
		try {
			pmMonate = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_MONATE);
			ParametermandantDto pmTage = null;
			pmTage = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_UVA_VERPROBUNGSZEITRAUM_TAGE);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Ein Personenkonto fuer einen Kunden/Lieferanten anlegen. Die Nummer wird
	 * dabei automatisch generiert.
	 *
	 * Die Regel zur Zusammenstellung der Kontonummer wird durch einen
	 * Mandantenparameter definiert Vorerst wird nur die Standard-Regel
	 * implementiert x yy z* x ... Kontoklasse lt.
	 * Mandantenparameter/Debitor/Kreditor yy ... Anfangsbuchstabe des Partners
	 * (A=01, B=02, ...) zuzueglich Startwert z* ... laufende Nummer (die naechste
	 * freie) mit der verbleibenden Stellenanzahl
	 *
	 * Sonderfaelle: Ziffern 0 - 9 werden nach dem Zahlwort eingeordnet (z.B. 3 als
	 * drei)
	 *
	 * PJ 15226: zusaetzlich zu dieser Regel wird eine fortlaufende Nummerierung
	 * ohne Ruecksicht auf den Partnernamen implementiert. Die Anwendung ist
	 * abhaengig vom Mandantenparameter FINANZ_DEBITORENNUMMER_FORTLAUFEND (0 ...
	 * 2+3 Stelle aus Partner, 1 ... nur fortlaufend) Gilt fuer Debitoren und
	 * Kreditoren ACHTUNG: wenn der Parameter nachtraeglich auf 1 gesetzt wird,
	 * werden Luecken aufgefuellt!
	 *
	 * @param partnerDto   PartnerDto
	 * @param kontotypCNr  String
	 * @param theClientDto String
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public KontoDto createKontoFuerPartnerAutomatisch(PartnerDto partnerDto, String kontotypCNr, boolean kontoAnlegen,
			String kontonummerVorgabe, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		KontoDto kontoDto = null;

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			if (mandantDto.getPartnerIIdFinanzamt() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN,
						"Kein Finanzamt definiert f\u00FCr Mandant " + theClientDto.getMandant(),
						theClientDto.getMandant());
			}
		} catch (RemoteException e1) {
			throw new EJBExceptionLP(e1);
		}

		String vonParameterCNr = null;
		String bisParameterCNr = null;
		String kategorieCNr = null;
		if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			vonParameterCNr = ParameterFac.PARAMETER_DEBITORENNUMMER_VON;
			bisParameterCNr = ParameterFac.PARAMETER_DEBITORENNUMMER_BIS;
			kategorieCNr = ParameterFac.KATEGORIE_KUNDEN;
		} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			vonParameterCNr = ParameterFac.PARAMETER_KREDITOREN_VON;
			bisParameterCNr = ParameterFac.PARAMETER_KREDITOREN_BIS;
			kategorieCNr = ParameterFac.KATEGORIE_LIEFERANT;
		}
		ParametermandantDto vonParameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				kategorieCNr, vonParameterCNr);
		ParametermandantDto bisParameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				kategorieCNr, bisParameterCNr);

		if (kontonummerVorgabe != null) {
			int kontonummer = Integer.parseInt(kontonummerVorgabe);
			if (kontonummer < Integer.parseInt(vonParameter.getCWert())
					|| kontonummer > Integer.parseInt(bisParameter.getCWert()))
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KONTONUMMER_AUSSERHALB_DEFINITION,
						"FEHLER_FINANZ_KONTONUMMER_AUSSERHALB_DEFINITION", kontonummerVorgabe, vonParameter.getCWert(),
						bisParameter.getCWert());
			kontoDto = kontoErstellen(kontonummerVorgabe, kontotypCNr, partnerDto, kontoAnlegen, theClientDto);
			// Es konnte kein Konto erstellt werden
			if (kontoDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN, "");
			}
			return kontoDto;
		} else {
			Integer iStellenanzahl = getAnzahlStellenVonKontoNummer(kontotypCNr, theClientDto.getMandant());
			//
			// Ermittlung der der Kontoklasse
			char cKontoklasse = vonParameter.getCWert().charAt(0);

			ParametermandantDto paraDebFortlaufend = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_DEBITORENNUMMER_FORTLAUFEND);

			boolean debfortlaufend = (Boolean) paraDebFortlaufend.getCWertAsObject();

			String sBereich = "";
			if (!debfortlaufend) {
				// Ermittlung der 2stelligen nummer anhand Anfangsbuchstabe
				// des
				// Partnernamens
				sBereich = getPartnerteilDerNummer(partnerDto, vonParameter);
			}
			// jetzt die naechste noch nicht vergebene Kontonummer finden
			int iVerbleibendeStellen = iStellenanzahl.intValue() - 1 - sBereich.length();
			int beginnWert = Integer.parseInt(vonParameter.getCWert().substring(1 + sBereich.length()));
			for (int i = beginnWert; i < Math.pow(10, iVerbleibendeStellen); i++) {
				DecimalFormat nf = new DecimalFormat("000000000000".substring(0, iVerbleibendeStellen));
				String sLfdNummer = nf.format(i);
				// Kontonummer zusammenbasteln
				String sKontonummer = cKontoklasse + sBereich + sLfdNummer;

				String sKontonummerPlus1000 = cKontoklasse + sBereich + nf.format(i + 1000);

				if (Integer.parseInt(sKontonummer) > Integer.parseInt(bisParameter.getCWert())) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR,
							"FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR",
							vonParameter.getCWert() + " - " + bisParameter.getCWert());
				}

				// Nun 1000 holen und nachsehen obs ein Luecke gibt

				try {
				Session session = FLRSessionFactory.getFactory().openSession();

				String sQuery = "SELECT k.c_nr FROM FLRFinanzKonto k WHERE k.kontotyp_c_nr = '" + kontotypCNr
						+ "' AND k.mandant_c_nr='" + theClientDto.getMandant() + "' AND CAST( k.c_nr AS int) >="
						+ sKontonummer + " AND CAST(k.c_nr AS int) < " + sKontonummerPlus1000
						+ " ORDER BY CAST(k.c_nr AS int) ASC";

				org.hibernate.Query queryS = session.createQuery(sQuery);

				List<?> results = queryS.list();

				if (results.size() == 0) {
					return kontoErstellen(sKontonummer, kontotypCNr, partnerDto, kontoAnlegen, theClientDto);
				} else if (results.size() == 1000) {

					i = i + 999;

				} else {

					Integer iKontonummerZuVerwenden = new Integer(sKontonummer);

					Iterator<?> resultListIterator = results.iterator();

					while (resultListIterator.hasNext()) {

						Integer iKontonummer = new Integer((String) resultListIterator.next());

						if (iKontonummer > iKontonummerZuVerwenden) {
							return kontoErstellen(iKontonummerZuVerwenden + "", kontotypCNr, partnerDto, kontoAnlegen,
									theClientDto);
						}

						iKontonummerZuVerwenden = iKontonummerZuVerwenden.intValue() + 1;

					}

					// Pruefen, ob das Konto schon existiert
					kontoDto = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(sKontonummer, kontotypCNr,
							theClientDto.getMandant(), theClientDto);
					if (kontoDto == null) {
						return kontoErstellen(sKontonummer, kontotypCNr, partnerDto, kontoAnlegen, theClientDto);
					}
				}
				} catch(Exception e) {
					myLogger.error("SQL", e);
					if (e.getCause() instanceof SQLException) {
						String message =e.getCause().getMessage();
						ArrayList al=new ArrayList();
						al.add(message);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ERZEUGEN_DER_KREDITOREN_DEBITORENNUMMER,al,
								new Exception("FEHLER_BEIM_ERZEUGEN_DER_KREDITOREN_DEBITORENNUMMER"));
					}else {
						throw e;
					}
				}
			}
			// Es konnte kein Konto erstellt werden

			StringBuffer sb = new StringBuffer();
			sb.append(cKontoklasse);
			sb.append(sBereich);
			for (int i = 0; i < iVerbleibendeStellen; i++)
				sb.append("x");
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR,
					"FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR", sb.toString());
		}
	}

	private String getPartnerteilDerNummer(PartnerDto partnerDto, ParametermandantDto parameter) {
		String sBereich;
		char[] aZiffer = new char[] { 'n', 'e', 'z', 'd', 'v', 'f', 's', 's', 'a', 'n' };
		String umlaute = "\u00E4\u00F6\u00FC\u00DF";
		char[] aUmlautErsatz = new char[] { 'a', 'o', 'u', 's' };
		int iBereich;
		if (partnerDto.getCName1nachnamefirmazeile1() != null
				&& partnerDto.getCName1nachnamefirmazeile1().trim().length() >= 0) {

			// Lower Case damit ich a und A gleich behandle
			char cAnfangsbuchstabe = partnerDto.getCName1nachnamefirmazeile1().trim().toLowerCase().charAt(0);

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
		int iOffset = new Integer(parameter.getCWert().substring(1, 3)).intValue();
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

	@Override
	public Integer findDefaultFinanzamtIdForPartner(PartnerDto partnerDto, TheClientDto theClientDto)
			throws RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		Integer finanzamtId = mandantDto.getPartnerIIdFinanzamt();
		if (getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			return finanzamtId;
		}

		FinanzamtDto finanzamtDto = findFinanzamtAbweichendesUstland(partnerDto.getLandIIdAbweichendesustland(),
				theClientDto);
		return finanzamtDto.getPartnerIId();
//		if (partnerDto.getLandIIdAbweichendesustland() != null) {
//			LandDto partnerLandDto = getSystemFac().landFindByPrimaryKey(partnerDto.getLandIIdAbweichendesustland());
//			FinanzamtDto[] faDtos = finanzamtFindAllByMandantCNr(theClientDto);
//			for (FinanzamtDto finanzamtDto : faDtos) {
//				LandplzortDto lpoDto = finanzamtDto.getPartnerDto().getLandplzortDto();
//				if (lpoDto != null && lpoDto.getLandDto() != null) {
//					if (partnerLandDto.getCLkz().equals(lpoDto.getLandDto().getCLkz())) {
//						return finanzamtDto.getPartnerIId();
//					}
//				}
//			}
//		}
//
//		return finanzamtId;
	}

	@Override
	public FinanzamtDto finanzamtFindByKunde(KundeDto kundeDto, TheClientDto theClientDto) throws RemoteException {
		if (getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			Integer kontoId = kundeDto.getIidDebitorenkonto();
			Konto konto = em.find(Konto.class, kontoId);
			return finanzamtFindByPrimaryKey(konto.getFinanzamtIId(), theClientDto.getMandant(), theClientDto);
		} else {
			return findFinanzamtAbweichendesUstland(kundeDto.getPartnerDto().getLandIIdAbweichendesustland(),
					theClientDto);
		}
	}

	@Override
	public FinanzamtDto finanzamtFindByLieferant(LieferantDto lieferantDto, TheClientDto theClientDto)
			throws RemoteException {
		if (getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			Integer kontoId = lieferantDto.getKontoIIdKreditorenkonto();
			Konto konto = em.find(Konto.class, kontoId);
			return finanzamtFindByPrimaryKey(konto.getFinanzamtIId(), theClientDto.getMandant(), theClientDto);
		} else {
			return findFinanzamtAbweichendesUstland(lieferantDto.getPartnerDto().getLandIIdAbweichendesustland(),
					theClientDto);
		}
	}

	private FinanzamtDto findFinanzamtAbweichendesUstland(Integer abweichendesUstLandId, TheClientDto theClientDto)
			throws RemoteException {
		if (abweichendesUstLandId == null) {
			return getMandantFinanzamt(theClientDto);
		}

		LandDto partnerLandDto = getSystemFac().landFindByPrimaryKey(abweichendesUstLandId);
		FinanzamtDto[] faDtos = finanzamtFindAllByMandantCNr(theClientDto);
		for (FinanzamtDto finanzamtDto : faDtos) {
			LandplzortDto lpoDto = finanzamtDto.getPartnerDto().getLandplzortDto();
			if (lpoDto != null && lpoDto.getLandDto() != null) {
				if (partnerLandDto.getCLkz().equals(lpoDto.getLandDto().getCLkz())) {
					return finanzamtDto;
				}
			}
		}

		return getMandantFinanzamt(theClientDto);
	}

	private FinanzamtDto getMandantFinanzamt(TheClientDto theClientDto) throws RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		Integer finanzamtId = mandantDto.getPartnerIIdFinanzamt();
		return finanzamtFindByPrimaryKey(finanzamtId, theClientDto.getMandant(), theClientDto);
	}

	private KontoDto kontoErstellen(String sKontonummer, String kontotypCNr, PartnerDto partnerDto,
			boolean kontoAnlegen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// das Konto gibts noch nicht -> Dto Anlegen
		KontoDto kontoDto = new KontoDto();
		kontoDto.setCNr(sKontonummer);
		kontoDto.setCBez(partnerDto.getCName1nachnamefirmazeile1());
		// Pflichtfelder befuellen
		GeschaeftsjahrMandantDto gjDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(
				getParameterFac().getGeschaeftsjahr(theClientDto.getMandant()), theClientDto.getMandant());
		kontoDto.setDGueltigvon(new Date(gjDto.getDBeginndatum().getTime()));
		kontoDto.setBAllgemeinsichtbar(Helper.getShortTrue());
		kontoDto.setBAutomeroeffnungsbuchung(Helper.getShortTrue());
		kontoDto.setBManuellbebuchbar(Helper.getShortTrue());
		kontoDto.setMandantCNr(theClientDto.getMandant());
		kontoDto.setKontotypCNr(kontotypCNr);
		kontoDto.setFinanzamtIId(findDefaultFinanzamtIdForPartner(partnerDto, theClientDto));
		kontoDto.setBOhneUst(Helper.getShortFalse());

		// nur wenn Fibumodul
		if (getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			String laenderart = getFinanzServiceFac().getLaenderartZuPartner(partnerDto.getIId(), getTimestamp(),
					theClientDto);
			if (laenderart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEIN_LAND_IM_KUNDEN, "laenderart = null");
			}

			String stkCnr = mapLaenderartZuSteuerkategorie(laenderart);

//			SteuerkategorieDto steuerkategorieDto = getSteuerkategorieZuLaenderart(
//					kontoDto.getFinanzamtIId(), laenderart, theClientDto);
			if (stkCnr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
						"Steuerkategorie fehlt f\u00FCr Finanzamt IId:" + kontoDto.getFinanzamtIId()
								+ ", L\u00E4nderart: " + laenderart);
			}
			kontoDto.setSteuerkategorieCnr(stkCnr);
// PJ19207 es gibt keine Steuerkategorie fuer Reverse mehr
//			kontoDto.setSteuerkategorieIId(steuerkategorieDto.getIId());
//			SteuerkategorieDto steuerkategorieReverseDto = getSteuerkategorieReverseZuLaenderart(
//					kontoDto.getFinanzamtIId(), laenderart, theClientDto);
//			if (steuerkategorieReverseDto == null)
//				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
//						"Steuerkategorie Reverse Charge fehlt f\u00FCr Finanzamt IId:" + kontoDto.getFinanzamtIId() + ", L\u00E4nderart: " + laenderart);
//			kontoDto.setSteuerkategorieIIdReverse(steuerkategorieReverseDto.getIId());
		}
		// und speichern
		if (kontoAnlegen) {
			kontoDto = createKonto(kontoDto, theClientDto);
		}
		return kontoDto;
	}

	public SteuerkategorieDto getSteuerkategorieReverseZuLaenderart(Integer finanzamtIId, String laenderart,
			TheClientDto theClientDto) throws RemoteException {
		return getSteuerkategorieZuLaenderart(finanzamtIId, laenderart, theClientDto, true);
	}

	public SteuerkategorieDto getSteuerkategorieZuLaenderart(Integer finanzamtIId, String laenderart,
			TheClientDto theClientDto) throws RemoteException {
		return getSteuerkategorieZuLaenderart(finanzamtIId, laenderart, theClientDto, false);
	}

	private String mapLaenderartZuSteuerkategorie(String laenderartCnr) {
		if (LAENDERART_INLAND.equals(laenderartCnr))
			return FinanzServiceFac.STEUERKATEGORIE_INLAND;
		if (LAENDERART_EU_AUSLAND_MIT_UID.equals(laenderartCnr))
			return FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID;
		if (LAENDERART_EU_AUSLAND_OHNE_UID.equals(laenderartCnr))
			return FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU;
		if (LAENDERART_DRITTLAND.equals(laenderartCnr))
			return FinanzServiceFac.STEUERKATEGORIE_AUSLAND;
		return null;
	}

	private SteuerkategorieDto getSteuerkategorieZuLaenderart(Integer finanzamtIId, String laenderart,
			TheClientDto theClientDto, boolean reverse) throws RemoteException {
		ReversechargeartDto rcArtDto = reverse ? getFinanzServiceFac().reversechargeartFindLeistung(theClientDto)
				: getFinanzServiceFac().reversechargeartFindOhne(theClientDto);

		String cnr = mapLaenderartZuSteuerkategorie(laenderart);
//		
//		if (laenderart != null) {
//			// Jetzt nach Laenderart waehlen
//			if (laenderart.equals(FinanzFac.LAENDERART_INLAND))
//				cnr = reverse ? FinanzServiceFac.STEUERKATEGORIE_INLAND_REVERSE : FinanzServiceFac.STEUERKATEGORIE_INLAND;
//			else if (reverse)
//				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLAND_REVERSE;
//			else if (laenderart.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID))
//				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID;
//			else if (laenderart
//					.equals(FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID))
//				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU;
//			else if (laenderart.equals(FinanzFac.LAENDERART_DRITTLAND))
//				cnr = FinanzServiceFac.STEUERKATEGORIE_AUSLAND;
//		}

		if (cnr == null)
			return null;
		SteuerkategorieDto steuerkategorieDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(cnr,
				rcArtDto.getIId(), finanzamtIId, theClientDto);
		return steuerkategorieDto;
	}

	private Integer getNextErgebnisgruppeReihung(String mandantCNr, Short bBilanzgruppe) throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("mandantCNr == null"));
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
	 * @param iIdEG1I      PK der ersten Ergebnisgruppe
	 * @param iIdEG2I      PK der zweiten Ergebnisgruppe
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void vertauscheErgebnisgruppen(Integer iIdEG1I, Integer iIdEG2I, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
						new Exception(
								"es wurde versucht, ergebnisgruppen von verschiedenen mandanten zu vertauschen: i_id="
										+ iIdEG1I + " bzw. " + iIdEG2I));
			}
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public void createKontoland(KontolandDto kontolandDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(kontolandDto, "kontolandDto");

		try {
			kontolandDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			kontolandDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Timestamp now = getTimestamp();
			kontolandDto.setTAnlegen(now);
			kontolandDto.setTAendern(now);
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KONTOLAND);

			Kontoland kontoland = new Kontoland(kontolandDto.getKontoIId(), kontolandDto.getLandIId(),
					kontolandDto.getKontoIIdUebersetzt(), kontolandDto.getPersonalIIdAnlegen(),
					kontolandDto.getPersonalIIdAendern(), kontolandDto.getTGueltigAb());
			kontoland.setIId(pk);
			em.persist(kontoland);
			em.flush();
			setKontolandFromKontolandDto(kontoland, kontolandDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKontoland(KontolandDto kontolandDto) throws EJBExceptionLP {
		Validator.dtoNotNull(kontolandDto, "kontolandDto");
		Validator.pkFieldNotNull(kontolandDto.getIId(), "iid");

		flushRemove(em, Kontoland.class, kontolandDto.getIId());
	}

	public void updateKontoland(KontolandDto kontolandDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(kontolandDto, "kontolandDto");

		Validator.notNull(kontolandDto.getLandIId(), "landIId");
		Validator.notNull(kontolandDto.getKontoIId(), "kontoIId");
		Validator.notNull(kontolandDto.getKontoIIdUebersetzt(), "kontoIIdUebersetzt");
		Validator.notNull(kontolandDto.getTGueltigAb(), "gueltigAb");

		if (kontolandDto.getIId() == null) {
			createKontoland(kontolandDto, theClientDto);
			return;
		}

		if (kontolandDto.getKontoIId().equals(kontolandDto.getKontoIIdUebersetzt())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KONTOLAND_ZEIGT_AUF_SICH_SELBST,
					"kontoiid = " + kontolandDto.getKontoIId());
		}

		throwIfDuplicate(kontolandDto);

		Kontoland kontoland = em.find(Kontoland.class, kontolandDto.getIId());
		if (kontoland == null) {
			createKontoland(kontolandDto, theClientDto);
		} else {
			kontolandDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			kontolandDto.setTAendern(getTimestamp());
			setKontolandFromKontolandDto(kontoland, kontolandDto);
		}
	}

	public void updateKontolands(KontolandDto[] kontolandDtos, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontolandDtos != null) {
			for (int i = 0; i < kontolandDtos.length; i++) {
				updateKontoland(kontolandDtos[i], theClientDto);
			}
		}
	}

	@Override
	public KontolandDto kontolandFindByPrimaryKey(Integer kontolandId) {
		Validator.pkFieldNotNull(kontolandId, "kontolandId");
		Kontoland entity = em.find(Kontoland.class, kontolandId);
		Validator.entityFound(entity, kontolandId);

		return assembleKontolandDto(entity);
	}

	@Override
	public KontolandDto kontolandFindByPrimaryKey(Integer kontoId, Integer landId, Timestamp gueltigAb)
			throws EJBExceptionLP {
		HvOptional<KontolandDto> dto = kontolandFindByPrimaryKeyOhneExc(kontoId, landId, gueltigAb);
		if (!dto.isPresent()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return dto.get();
	}

	@Override
	public HvOptional<KontolandDto> kontolandFindByPrimaryKeyOhneExc(Integer kontoId, Integer landId,
			Timestamp gueltigAb) throws EJBExceptionLP {
		Validator.pkFieldNotNull(kontoId, "kontoId");
		Validator.pkFieldNotNull(landId, "landId");
		Validator.notNull(gueltigAb, "gueltigAb");

		KontolandQuery q = new KontolandQuery(em);
		HvOptional<Kontoland> entity = q.find(kontoId, landId, gueltigAb);
		if (!entity.isPresent()) {
			return HvOptional.empty();
		}

		return HvOptional.of(assembleKontolandDto(entity.get()));
	}

	@Override
	public HvOptional<KontolandDto> kontolandZuDatum(Integer kontoId, Integer landId, Timestamp gueltigZum)
			throws EJBExceptionLP {
		Validator.pkFieldNotNull(kontoId, "kontoId");
		Validator.pkFieldNotNull(landId, "landId");
		Validator.notNull(gueltigZum, "gueltigZum");

		KontolandQuery q = new KontolandQuery(em);
		HvOptional<Kontoland> entity = q.findZuDatum(kontoId, landId, gueltigZum);
		if (!entity.isPresent()) {
			return HvOptional.empty();
		}

		return HvOptional.of(assembleKontolandDto(entity.get()));
	}

	private void setKontolandFromKontolandDto(Kontoland kontoland, KontolandDto kontolandDto) {
		kontoland.setKontoIIdUebersetzt(kontolandDto.getKontoIIdUebersetzt());
		kontoland.setTAnlegen(kontolandDto.getTAnlegen());
		kontoland.setPersonalIIdAnlegen(kontolandDto.getPersonalIIdAnlegen());
		kontoland.setTAendern(kontolandDto.getTAendern());
		kontoland.setPersonalIIdAendern(kontolandDto.getPersonalIIdAendern());
		kontoland.setTGueltigAb(kontolandDto.getTGueltigAb());
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

	public KontoImporterResult importCsv(List<String[]> allLines, boolean checkOnly, TheClientDto theClientDto) {

		FinanzFac lFinanzFac = getFinanzFac();
		FinanzServiceFac lFinanzServiceFac = getFinanzServiceFac();
		SystemFac lSystemFac = getSystemFac();

		IKontoImporterBeanServices beanServices = new KontoImporterBeanService(theClientDto, lFinanzFac,
				lFinanzServiceFac, lSystemFac);
		KontoImporter importer = new KontoImporter(beanServices);

		if (allLines.size() < 2)
			return new KontoImporterResult(new ArrayList<EJBLineNumberExceptionLP>());

		importer.getTransformer().buildFieldIndexer(allLines.get(0));
		List<String[]> linesWithData = allLines.subList(1, allLines.size());

		return checkOnly ? importer.checkImportCsvKontos(linesWithData) : importer.importCsvKontos(linesWithData);
	}

	@Override
	public boolean isMitlaufendesKonto(Integer kontoIId) throws RemoteException {
		Session session = FLRSessionFactory.getFactory().openSession();
		kontoFindByPrimaryKey(kontoIId);
		org.hibernate.Query q = session.createQuery("FROM FLRFinanzKonto konto WHERE konto.i_id = " + kontoIId + " AND"
				+ KontoQueryBuilder.buildOhneMitlaufendeKonten("konto"));
		return q.list().size() == 0;
	}

	public KontoRequest[] kontoExist(String mandant, KontoRequest... kontoRequest) throws RemoteException {

		for (KontoRequest request : kontoRequest) {

			String kontocNr = request.getKontoCnr();
			if (kontocNr == null) {
				myLogger.warn("cNrI == null");
				return null;
			}

			String kontotypCnr = request.getKontoTyp();
			if (kontotypCnr == null) {
				myLogger.warn("kontotypCnr == null");
				return null;
			}
			if (mandant == null) {
				myLogger.warn("sMandantI == null");
				return null;
			}

			try {
				Query query = em.createNamedQuery("KontofindByCNrKontotypMandant");
				query.setParameter(1, kontocNr);
				query.setParameter(2, kontotypCnr);
				query.setParameter(3, mandant);
				if (query.getResultList().isEmpty()) {
					request.setExist(false);
				} else {
					request.setExist(true);
				}
			} catch (Exception e) {
			}
		}
		return kontoRequest;
	}

	@Override
	public List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc(String mandantCNr) throws EJBExceptionLP {

		List<Bankverbindung> bvList = BankverbindungQuery.listByMandantCNr(em, mandantCNr);
		List<BankverbindungDto> bvDtoList = new ArrayList<BankverbindungDto>();

		for (Bankverbindung bv : bvList) {
			bvDtoList.add(assembleBankverbindungDto(bv));
		}

		return bvDtoList;
	}

	@Override
	public KontoDtoSmall[] kontosFindByPrimaryKeySmall(Integer[] ids) throws EJBExceptionLP {
		Validator.notNull(ids, "ids");
		KontoDtoSmall[] dtos = new KontoDtoSmall[ids.length];

		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null) {
				Konto konto = em.find(Konto.class, ids[i]);
				if (konto == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "" + ids[i]);
				}
				dtos[i] = assembleKontoDtoSmall(konto);
			}
		}

		return dtos;
	}

	@Override
	public List<KontoDto> kontoFindAllByUvaartMandantFinanzamt(Integer uvaartId, String mandantCnr,
			Integer finanzamtId) {
		Validator.notNull(uvaartId, "uvaartId");

		Query query = em.createNamedQuery(Konto.QUERY_ALL_UVAART_MANDANT_FINANZAMT);
		query.setParameter(1, uvaartId);
		query.setParameter(2, mandantCnr);
		query.setParameter(3, finanzamtId);
		return assembleKontoDtoList(query.getResultList());
	}

	public String getLaenderartFuerSteuerkategorie(String steuerkategorieCnr) {
		if (FinanzServiceFac.STEUERKATEGORIE_INLAND.equals(steuerkategorieCnr))
			return LAENDERART_INLAND;
		if (FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID.equals(steuerkategorieCnr))
			return LAENDERART_EU_AUSLAND_MIT_UID;
		if (FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU.equals(steuerkategorieCnr))
			return LAENDERART_EU_AUSLAND_OHNE_UID;
		if (FinanzServiceFac.STEUERKATEGORIE_AUSLAND.equals(steuerkategorieCnr))
			return LAENDERART_DRITTLAND;
		return null;
	}

	public BankverbindungDto getBankverbindungFuerSepaLastschriftByMandantOhneExc(String mandantCnr) {
		Validator.notNull(mandantCnr, "mandantCnr");
		List<Bankverbindung> bankverbindungen = BankverbindungQuery.listByMandantCNrBFuerSepaLastschrift(em, mandantCnr,
				true);

		if (bankverbindungen.isEmpty())
			return null;

		if (bankverbindungen.size() > 1) {
			myLogger.warn("Es existiert im Mandant " + mandantCnr + " mehr als eine Bankverbindung,"
					+ " die bFuerSepaLastschrift gesetzt hat. Die erste wird verwendet.");
		}

		return assembleBankverbindungDto(bankverbindungen.get(0));
	}
}
