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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.xml.sax.SAXException;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebotstkl.ejb.Webpartner;
import com.lp.server.angebotstkl.ejb.WebpartnerQuery;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.WebPartnerDtoAssembler;
import com.lp.server.angebotstkl.service.WebpartnerDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.Artikellieferantstaffel;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantImportDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDtoAssembler;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.ejb.Bestellvorschlag;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.inserat.ejb.Inserat;
import com.lp.server.partner.bl.VendidataSuppliersExportTransformer;
import com.lp.server.partner.bl.VendidataSuppliersMarshaller;
import com.lp.server.partner.bl.VendidataXmlSupplierTransformResult;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lflfliefergruppe;
import com.lp.server.partner.ejb.LflfliefergruppePK;
import com.lp.server.partner.ejb.Lfliefergruppespr;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.ejb.LieferantQuery;
import com.lp.server.partner.ejb.Lieferantbeurteilung;
import com.lp.server.partner.ejb.WeblieferantFarnell;
import com.lp.server.partner.fastlanereader.generated.FLRLFLiefergruppe;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRPartnerbank;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.IVendidataSuppliersExportBeanServices;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LflfliefergruppeDtoAssembler;
import com.lp.server.partner.service.LfliefergruppesprDto;
import com.lp.server.partner.service.LfliefergruppesprDtoAssembler;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantDtoAssembler;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.partner.service.LieferantbeurteilungDtoAssembler;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.partner.service.WeblieferantFarnellDto;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LieferantFacBean extends Facade implements LieferantFac {

	@PersistenceContext
	private EntityManager em;

	public Integer createLieferantFuerEingangsrechnungAusQRCode(String name, String land, String plz, String ort,
			String strasse, String waehrung, String iban, TheClientDto theClientDto) throws RemoteException {

		String name1 = null;
		String name2 = null;
		if (name.length() > 40) {
			name1 = name.substring(0, 40);
			name = name.substring(40);

			if (name.length() > 40) {
				name2 = name.substring(0, 40);
			} else {
				name2 = name;
			}

		} else {
			name1 = name;
		}

		String kbez = name1;

		String sN1 = " ";
		if (name != null) {
			sN1 = (name1 == null) ? " " : name1 + " ";
		}

		int iE = sN1.indexOf(" ");
		if (iE > PartnerFac.MAX_KBEZ / 2) {
			iE = PartnerFac.MAX_KBEZ / 2;
		}
		kbez = sN1.substring(0, iE);

		PartnerDto pDto = new PartnerDto();
		pDto.setCKbez(kbez);
		pDto.setCName1nachnamefirmazeile1(name1);
		pDto.setCName2vornamefirmazeile2(name2);
		pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
		pDto.setBVersteckt(false);
		pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

		LandDto landDto = getSystemFac().landFindByLkz(land);

		LandplzortDto landplzortDto = new LandplzortDto();
		landplzortDto.setLandDto(landDto);
		landplzortDto.setIlandID(landDto.getIID());
		landplzortDto.setCPlz(plz);
		
		
		
		
		// ORT

		Query queryOrt = em.createNamedQuery("OrtfindByCName");
		queryOrt.setParameter(1, ort);

		List results = queryOrt.getResultList();

		Integer ortIId = null;

		if (results.size() > 0) {

			Ort ortBean = (Ort) results.iterator().next();
			// bereits vorhanden, dann ID holen
			ortIId = ortBean.getIId();
		} else {
			OrtDto ortDto = new OrtDto();
			ortDto.setCName(ort);
			try {
				ortIId = getSystemFac().createOrt(ortDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		landplzortDto.setOrtIId(ortIId);
		landplzortDto.setOrtDto(getSystemFac().ortFindByPrimaryKey(ortIId));
		
		
		
		
		
		
		LandplzortDto lpoDtoVorhanden = getSystemFac().landplzortFindByLandOrtPlzOhneExc(
				landDto.getIID(), ortIId, plz) ; 
		
		
		Integer landplzortIId =null;
		
		if(lpoDtoVorhanden==null) {
			 landplzortIId = getSystemFac().createLandplzort(landplzortDto, theClientDto);
		}else {
			landplzortIId=lpoDtoVorhanden.getIId();
		}
		
		pDto.setLandplzortIId(landplzortIId);

		Integer mwstsatzbezIId = getPartnerFac().getDefaultMWSTSatzIIdAnhandLand(landDto, theClientDto);
		//

		Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

		pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

		LieferantDto lfDtoNeu = new LieferantDto();
		lfDtoNeu.setPartnerIId(pDto.getIId());
		lfDtoNeu.setPartnerDto(pDto);
		lfDtoNeu.setMandantCNr(theClientDto.getMandant());
		lfDtoNeu.setWaehrungCNr(waehrung);
		lfDtoNeu.setMwstsatzbezIId(mwstsatzbezIId);

		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		lfDtoNeu.setIdSpediteur(mDto.getSpediteurIIdLF());
		lfDtoNeu.setLieferartIId(mDto.getLieferartIIdLF());
		lfDtoNeu.setZahlungszielIId(mDto.getZahlungszielIIdLF());
		lfDtoNeu.setBIgErwerb(Helper.boolean2Short(false));

		Integer lieferantIId = getLieferantFac().createLieferant(lfDtoNeu, theClientDto);

		PartnerDto partnerDto = new PartnerDto();
		partnerDto.setCKbez("AutoAngelegt");
		partnerDto.setCName1nachnamefirmazeile1("AutomatischAngelegt");
		partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_SONSTIGES);
		partnerDto.setLocaleCNrKommunikation(theClientDto.getLocMandantAsString());

		LandplzortDto lpoDto = mDto.getPartnerDto().getLandplzortDto();
		partnerDto.setLandplzortDto(lpoDto);
		partnerDto.setLandplzortIId(lpoDto != null ? lpoDto.getIId() : null);
		partnerDto.setBVersteckt(false);

		BankDto bankDto = new BankDto();

		bankDto.setPartnerDto(partnerDto);
		Integer bankPartnerIId = getBankFac().createBank(bankDto, theClientDto);

		PartnerbankDto partnerBankDto = new PartnerbankDto();
		partnerBankDto.setBankPartnerIId(bankPartnerIId);
		partnerBankDto.setPartnerIId(partnerIId);
		partnerBankDto.setCIban(iban);
		partnerBankDto.setISort(getBankFac().getMaxISort(partnerIId) + 1);
		getBankFac().createPartnerbank(partnerBankDto, theClientDto);

		return lieferantIId;

	}

	public Integer createLieferant(LieferantDto lieferantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (lieferantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("lieferantDtoI == null"));
		}
		if (lieferantDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("lieferantDtoI.getPartnerDto() == null"));
		}

		try {
			// 1. Partner
			Integer iIdPartner = lieferantDtoI.getPartnerDto().getIId();
			if (iIdPartner != null) {
				getPartnerFac().updatePartner(lieferantDtoI.getPartnerDto(), theClientDto);
			} else {
				iIdPartner = getPartnerFac().createPartner(lieferantDtoI.getPartnerDto(), theClientDto);
			}

			// verbinde Partner mit Lieferanten
			lieferantDtoI.setPartnerIId(iIdPartner);

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANT_DEFAULT_GESPERRT);
			boolean bDefaultGesperrt = ((Boolean) parameter.getCWertAsObject()).booleanValue();
			if (bDefaultGesperrt) {
				lieferantDtoI.setTBestellsperream(getTimestamp());
			}

			// Partner lesen wegen generierter Daten.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartner, theClientDto);
			lieferantDtoI.setPartnerDto(partnerDto);

			if (partnerDto.getLandplzortIId() != null) {
				LandplzortDto plzortDto = getSystemFac().landplzortFindByPrimaryKey(partnerDto.getLandplzortIId());
				if (plzortDto.getLandDto().getWaehrungCNr() != null) {
					lieferantDtoI.setWaehrungCNr(plzortDto.getLandDto().getWaehrungCNr());
				}
			}

			// 2. Lieferant selbst
			// befuelle Felder am Server.
			lieferantDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lieferantDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

			if (lieferantDtoI.getBBeurteilen() == null) {
				lieferantDtoI.setBBeurteilen(Helper.boolean2Short(true));
			}
			// if (lieferantDtoI.getBReversecharge() == null) {
			// lieferantDtoI.setBReversecharge(Helper.boolean2Short(false));
			// }
			if (lieferantDtoI.getReversechargeartId() == null) {
				ReversechargeartDto rcartDto = getFinanzServiceFac()
						.reversechargeartFindOhne(theClientDto.getMandant());
				lieferantDtoI.setReversechargeartId(rcartDto.getIId());
			}
			// lieferantDtoI.setBReversecharge(Helper.getShortFalse());

			if (lieferantDtoI.getBMoeglicherLieferant() == null) {
				lieferantDtoI.setBMoeglicherLieferant(Helper.boolean2Short(false));
			}
			if (lieferantDtoI.getBZollimportpapier() == null) {
				lieferantDtoI.setBZollimportpapier(Helper.boolean2Short(false));
			}
			if (lieferantDtoI.getBVersteckterkunde() == null) {
				lieferantDtoI.setBVersteckterkunde(Helper.boolean2Short(false));
			}

			if (lieferantDtoI.getBZuschlagInklusive() == null) {
				lieferantDtoI.setBZuschlagInklusive(Helper.boolean2Short(false));
			}

			// Default abbuchungslager ist Hauptlager
			if (lieferantDtoI.getLagerIIdZubuchungslager() == null) {
				lieferantDtoI
						.setLagerIIdZubuchungslager(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
			}

			LieferantDto lieferantDto1 = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
					partnerDto.getIId(), theClientDto.getMandant(), theClientDto);
			Lieferant lieferant = null;

			if (lieferantDto1 == null) {
				// Generieren eines Lieferantenprimarykeys.
				PKGeneratorObj pkGen = new PKGeneratorObj();
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LIEFERANT);
				lieferantDtoI.setIId(pk);

				lieferant = new Lieferant(lieferantDtoI.getIId(), lieferantDtoI.getPartnerIId(),
						lieferantDtoI.getMandantCNr(), lieferantDtoI.getPersonalIIdAnlegen(),
						lieferantDtoI.getPersonalIIdAendern(), lieferantDtoI.getWaehrungCNr(),
						lieferantDtoI.getBMoeglicherLieferant(), lieferantDtoI.getBBeurteilen(),
						lieferantDtoI.getIdSpediteur(), lieferantDtoI.getLieferartIId(),
						lieferantDtoI.getZahlungszielIId(),
						// lieferantDtoI.getBReversecharge(),
						lieferantDtoI.getBIgErwerb(), lieferantDtoI.getLagerIIdZubuchungslager(),
						lieferantDtoI.getBZollimportpapier(), lieferantDtoI.getBVersteckterkunde(),
						lieferantDtoI.getReversechargeartId());
				em.persist(lieferant);
				em.flush();
			} else {
				lieferant = em.find(Lieferant.class, lieferantDto1.getIId());
				lieferantDtoI.setIId(lieferant.getIId());
			}

			setLieferantFromLieferantDto(lieferant, lieferantDtoI);
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		HvDtoLogger<LieferantDto> logger = new HvDtoLogger<LieferantDto>(em, theClientDto);
		logger.logInsert(lieferantDtoI);

		return lieferantDtoI.getIId();
	}

	public void removeLieferant(LieferantDto lieferantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (lieferantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("lieferantDto == null"));
		}

		// Erste den Lieferanten loeschen.
		Lieferant toRemove = em.find(Lieferant.class, lieferantDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {

			HvDtoLogger<LieferantDto> partnerLogger = new HvDtoLogger<LieferantDto>(em, lieferantDtoI.getIId(),
					theClientDto);
			partnerLogger.logDelete(lieferantDtoI);

			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateLieferant(LieferantDto lieferantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (lieferantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("lieferantDtoI == null"));
		}
		if (lieferantDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("lieferantDtoI.getPartnerDto() == null"));
		}

		try {
			// Lieferantenkto.
			if (lieferantDtoI.getIKreditorenkontoAsIntegerNotiId() != null) {
				workoutKreditoren(lieferantDtoI, theClientDto);
			} else {
				lieferantDtoI.setKontoIIdKreditorenkonto(null);
			}

			// Partnerteil des Lieferanten updaten.
			getPartnerFac().updatePartner(lieferantDtoI.getPartnerDto(), theClientDto);

			// Verstektekunde updaten.
			KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(lieferantDtoI.getPartnerIId(),
					lieferantDtoI.getMandantCNr(), theClientDto);
			if (kundeDto != null) {
				if (Helper.short2boolean(kundeDto.getBVersteckterlieferant())) {
					Kunde kunde = em.find(Kunde.class, kundeDto.getIId());
					kunde.setLieferartIId(lieferantDtoI.getLieferartIId());
					kunde.setSpediteurIId(lieferantDtoI.getIdSpediteur());
					kunde.setZahlungszielIId(lieferantDtoI.getZahlungszielIId());
					em.merge(kunde);
					em.flush();
				}
			}
			// Lieferant updaten.

			// Wer hat wann geaendert?
			lieferantDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lieferantDtoI.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

			Integer iId = lieferantDtoI.getIId();
			Lieferant lieferant = em.find(Lieferant.class, iId);

			LieferantDto dtoVorher = assembleLieferantDto(lieferant);

			// PJ 16965
			if (lieferant.getTFreigabe() == null && lieferantDtoI.getTFreigabe() != null) {
				lieferantDtoI.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
				lieferantDtoI.setTPersonalFreigabe(new Timestamp(System.currentTimeMillis()));
			}

			if (lieferantDtoI.getTFreigabe() == null && lieferant.getTFreigabe() != null) {
				lieferantDtoI.setPersonalIIdFreigabe(null);
				lieferantDtoI.setTPersonalFreigabe(null);
			}
			setLieferantFromLieferantDto(lieferant, lieferantDtoI);

			HvDtoLogger<LieferantDto> artikelLogger = new HvDtoLogger<LieferantDto>(em, lieferant.getIId(),
					theClientDto);
			artikelLogger.log(dtoVorher, lieferantDtoI);

		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * workoutKreditoren
	 * 
	 * @param lieferantDtoI LieferantDto
	 * @param theClientDto  String
	 * @throws EJBExceptionLP
	 */
	private void workoutKreditoren(LieferantDto lieferantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (lieferantDtoI
					.getUpdateModeKreditorenkonto() == LieferantDto.I_UPD_KREDITORENKONTO_PRUEFE_AUF_DOPPELTE) {

				long t = System.currentTimeMillis();
				LieferantDto[] lieferantDtos = findAllLieferantenByKreditorennr(lieferantDtoI, theClientDto);
				myLogger.info(">" + (System.currentTimeMillis() - t));

				verifyGleicheFibuBedingungen(lieferantDtoI, lieferantDtos, theClientDto);

				if (lieferantDtos.length > 0) {
					List<Object> entries = buildPartnerExc(Arrays.asList(lieferantDtos));
					throw new EJBExceptionLP(EJBExceptionLP.WARNUNG_KTO_BESETZT, entries, null);
				}
			}

			KontoDto lieferantDtoIch = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
					lieferantDtoI.getIKreditorenkontoAsIntegerNotiId().toString(), FinanzServiceFac.KONTOTYP_KREDITOR,
					lieferantDtoI.getMandantCNr(), theClientDto);

			if (lieferantDtoIch == null) {
				// --Kto nicht gefunden: create

				// AD: gleiche Methode verwenden wie fuer Nummer generieren!
				KontoDto k = createKreditorenkontoZuLieferantenAutomatisch(lieferantDtoI.getIId(), true,
						lieferantDtoI.getIKreditorenkontoAsIntegerNotiId().toString(), theClientDto);
				/*
				 * KontoDto k = new KontoDto(); k.setIId(null); k.setCNr(lieferantDtoI
				 * .getIKreditorenkontoAsIntegerNotiId().toString()); k.setCBez(lieferantDtoI
				 * .getPartnerDto().getCName1nachnamefirmazeile1()); UvaartDto uvaartDto =
				 * getFinanzServiceFac().uvaartFindByCnrMandant(FinanzServiceFac
				 * .UVAART_NICHT_DEFINIERT, theClientDto); k.setUvaartIId(uvaartDto.getIId());
				 * k.setKontoartCNr(FinanzServiceFac.KONTOART_NICHT_STEUERBAR);
				 * k.setBAutomeroeffnungsbuchung(Helper.boolean2Short(true));
				 * k.setBAllgemeinsichtbar(Helper.boolean2Short(true));
				 * k.setBManuellbebuchbar(Helper.boolean2Short(true)); /** @todo JO->WH,MB
				 * Innergemeinschaftlich, ... PJ 4388
				 */
				/*
				 * k.setKontotypCNr(FinanzServiceFac.KONTOTYP_KREDITOR);
				 * k.setMandantCNr(lieferantDtoI.getMandantCNr());
				 * 
				 * k = getFinanzFac().createKonto(k, theClientDto);
				 */
				lieferantDtoI.setKontoIIdKreditorenkonto(k.getIId());
			} else {
				PartnerDto partnerDto = lieferantDtoI.getPartnerDto();
				if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
					Integer finanzamtId = getFinanzFac().findDefaultFinanzamtIdForPartner(partnerDto, theClientDto);
					lieferantDtoIch.setFinanzamtIId(finanzamtId);
				}

				// --Kto gefunden: update
				lieferantDtoIch.setCBez(partnerDto.getCName1nachnamefirmazeile1());
				lieferantDtoI.setKontoIIdKreditorenkonto(lieferantDtoIch.getIId());
				getFinanzFac().updateKonto(lieferantDtoIch, theClientDto);
			}
			lieferantDtoI.setUpdateModeKreditorenkonto(LieferantDto.I_UPD_KREDITORENKONTO_KEIN_UPDATE);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Wenn es mehr als einen Lieferanten (mit der gleichen Kreditorennr) gibt, muss
	 * gelten: a) Ohne Fibu: Abweichendes UST-Land muss das gleiche sein b) Mit
	 * Fibu: Finanzamt muss das gleiche sein
	 * 
	 * @param lieferantDtos die zu pruefenden Lieferanten (mit der gleichen
	 *                      Kreditorennummer)
	 */
	private void verifyGleicheFibuBedingungen(LieferantDto updateLieferantDto, LieferantDto[] lieferantDtos,
			TheClientDto theClientDto) throws RemoteException {
		List<LieferantDto> all = new ArrayList<LieferantDto>(Arrays.asList(lieferantDtos));
		all.add(0, updateLieferantDto);
		if (all.size() < 2)
			return;

		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			verifyGleichesUSTLand(all, theClientDto);
		}
	}

	private void verifyGleichesUSTLand(List<LieferantDto> lieferantDtos, TheClientDto theClientDto)
			throws RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		Integer mandantFinanzamtId = mandantDto.getPartnerIIdFinanzamt();
		if (mandantFinanzamtId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_FINANZAMT_IM_MANDANT_NITCH_DEFINIERT,
					"Finanzamt im Mandant " + mandantDto.getCNr() + "' nicht definiert");
		}

		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPrimaryKey(mandantFinanzamtId,
				theClientDto.getMandant(), theClientDto);
		Integer ustlandId = null;
		for (LieferantDto lieferantDto : lieferantDtos) {
			Integer landId = lieferantDto.getPartnerDto().getLandIIdAbweichendesustland();
			if (landId == null) {
				landId = finanzamtDto.getPartnerDto().getLandplzortDto().getIlandID();
			}
			if (ustlandId == null) {
				ustlandId = landId;
			}
			if (!ustlandId.equals(landId)) {
				List<Object> entries = buildPartnerExc(lieferantDtos);
				entries.add(0, lieferantDto.getIId());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERANT_ABWEICHENDESUSTLAND_UNTERSCHIEDLICH, entries,
						null);
			}
		}
	}

	private List<Object> buildPartnerExc(List<LieferantDto> lieferantDtos) {
		List<Object> entries = new ArrayList<Object>();
		for (LieferantDto lieferantDto : lieferantDtos) {
			entries.add(lieferantDto.getPartnerDto().formatFixTitelName1Name2());
			entries.add(lieferantDto.getPartnerDto().formatAdresse());
		}
		return entries;
	}

	public ArrayList<Integer> lieferantFindByIBAN(String iban, TheClientDto theClientDto) {

		ArrayList<Integer> al = new ArrayList<Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT pb FROM FLRPartnerbank pb WHERE pb.c_iban='" + iban + "'";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		int i = 0;
		while (resultListIterator.hasNext()) {

			FLRPartnerbank pb = (FLRPartnerbank) resultListIterator.next();

			try {
				LieferantDto lfDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(pb.getPartner_i_id(),
						theClientDto.getMandant(), theClientDto);
				if (lfDto != null) {
					al.add(lfDto.getIId());
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		return al;
	}

	public LieferantDto lieferantFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrUserI == null"));
		}

		LieferantDto lieferantDto = new LieferantDto();
		try {
			// Lieferant.
			Lieferant lieferant = em.find(Lieferant.class, iIdI);
			lieferantDto = assembleLieferantDto(lieferant);

			// Partner.
			lieferantDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto));

			KontoDtoSmall k = null;
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				k = getFinanzFac().kontoFindByPrimaryKeySmall(lieferantDto.getKontoIIdKreditorenkonto());
			}
			Integer ik = null;
			if (k != null && k.getCNr() != null) {
				ik = new Integer(Integer.parseInt(k.getCNr()));
			}
			lieferantDto.setIKreditorenkontoAsIntegerNotiId(ik);

			// Partnerrechnungsadresse.
			Integer iIdPartnerRE = lieferantDto.getPartnerIIdRechnungsadresse();
			if (iIdPartnerRE != null) {
				lieferantDto.setPartnerRechnungsadresseDto(
						getPartnerFac().partnerFindByPrimaryKey(iIdPartnerRE, theClientDto));
			} else {
				lieferantDto.setPartnerRechnungsadresseDto(null);
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return lieferantDto;
	}

	public LieferantDto lieferantFindByPrimaryKeySmall(Integer iIdI) {
		Lieferant lieferant = em.find(Lieferant.class, iIdI);
		return assembleLieferantDto(lieferant);
	}

	public LieferantDto lieferantFindByPrimaryKeySmallWithNull(Integer iIdI) {
		Lieferant lieferant = em.find(Lieferant.class, iIdI);

		return lieferant == null ? null : assembleLieferantDto(lieferant);
	}

	public LieferantDto lieferantFindByPrimaryKeyOhneExc(Integer iIdI, TheClientDto theClientDto) {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		LieferantDto lieferantDto = new LieferantDto();
		try {
			// Lieferant.
			Lieferant lieferant = em.find(Lieferant.class, iIdI);
			if (lieferant == null) {
				return null;
			}
			lieferantDto = assembleLieferantDto(lieferant);

			// Partner.
			lieferantDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto));

			KontoDto k = null;
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				k = getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto());
			}
			Integer ik = null;
			if (k != null && k.getCNr() != null) {
				ik = new Integer(Integer.parseInt(k.getCNr()));
			}
			lieferantDto.setIKreditorenkontoAsIntegerNotiId(ik);

			// Partnerrechnungsadresse.
			Integer iIdPartnerRE = lieferantDto.getPartnerIIdRechnungsadresse();
			if (iIdPartnerRE != null) {
				lieferantDto.setPartnerRechnungsadresseDto(
						getPartnerFac().partnerFindByPrimaryKey(iIdPartnerRE, theClientDto));
			} else {
				lieferantDto.setPartnerRechnungsadresseDto(null);
			}
		} catch (Throwable th) {
			return null;
		}
		return lieferantDto;
	}

	private void setLieferantFromLieferantDto(Lieferant lieferant, LieferantDto lieferantDto) {
		lieferant.setPartnerIId(lieferantDto.getPartnerIId());
		lieferant.setMandantCNr(lieferantDto.getMandantCNr());
		lieferant.setMwstsatzIId(lieferantDto.getMwstsatzbezIId());
		lieferant.setWaehrungCNr(lieferantDto.getWaehrungCNr());
		lieferant.setLieferartIId(lieferantDto.getLieferartIId());
		lieferant.setZahlungszielIId(lieferantDto.getZahlungszielIId());
		lieferant.setNMindestbestellwert(lieferantDto.getNMindestbestellwert());
		lieferant.setNKredit(lieferantDto.getNKredit());
		lieferant.setNJahrbonus(lieferantDto.getNJahrbonus());
		lieferant.setNAbumsatz(lieferantDto.getNAbumsatz());
		lieferant.setNRabatt(lieferantDto.getNRabatt());
		lieferant.setNMindermengenzuschlag(lieferantDto.getNMindermengenzuschlag());
		lieferant.setNTransportkostenprolieferung(lieferantDto.getNTransportkostenprolieferung());
		lieferant.setKontoIIdKreditorenkonto(lieferantDto.getKontoIIdKreditorenkonto());
		lieferant.setKontoIIdWarenkonto(lieferantDto.getKontoIIdWarenkonto());
		lieferant.setCKundennr(lieferantDto.getCKundennr());
		lieferant.setCHinweisintern(lieferantDto.getCHinweisintern());
		lieferant.setCHinweisextern(lieferantDto.getCHinweisextern());
		lieferant.setXKommentar(lieferantDto.getXKommentar());
		lieferant.setBBeurteilen(lieferantDto.getBBeurteilen());
		lieferant.setBMoeglicherlieferant(lieferantDto.getBMoeglicherLieferant());
		lieferant.setIBeurteilung(lieferantDto.getIBeurteilung());
		lieferant.setPartnerIIdRechnungsadresse(lieferantDto.getPartnerIIdRechnungsadresse());
		lieferant.setPersonalIIdAnlegen(lieferantDto.getPersonalIIdAnlegen());
		lieferant.setPersonalIIdAendern(lieferantDto.getPersonalIIdAendern());
		lieferant.setKostenstelleIId(lieferantDto.getIIdKostenstelle());
		lieferant.setSpediteurIId(lieferantDto.getIdSpediteur());
		lieferant.setTBestellsperream(lieferantDto.getTBestellsperream());
		lieferant.setNKupferzahl(lieferantDto.getNKupferzahl());

		lieferant.setTFreigabe(lieferantDto.getTFreigabe());
		lieferant.setTPersonalFreigabe(lieferantDto.getTPersonalFreigabe());

		lieferant.setPersonalIIdFreigabe(lieferantDto.getPersonalIIdFreigabe());
		lieferant.setCFreigabe(lieferantDto.getCFreigabe());
		lieferant.setBIgErwerb(lieferantDto.getBIgErwerb());
		// lieferant.setBReversecharge(lieferantDto.getBReversecharge());
		lieferant.setBReversecharge(Helper.getShortFalse());
		lieferant.setReversechargeartId(lieferantDto.getReversechargeartId());
		lieferant.setLagerIIdZubuchungslager(lieferantDto.getLagerIIdZubuchungslager());
		lieferant.setBZollimportpapier(lieferantDto.getBZollimportpapier());
		lieferant.setBVersteckterkunde(lieferantDto.getBVersteckterkunde());
		lieferant.setCFremdsystemnr(lieferantDto.getCFremdsystemnr());
		lieferant.setBZuschlagInklusive(lieferantDto.getBZuschlagInklusive());
		lieferant.setILiefertag(lieferantDto.getILiefertag());
		lieferant.setPartnerIIdLieferadresse(lieferantDto.getPartnerIIdLieferadresse());

		em.merge(lieferant);
		em.flush();
	}

	private LieferantDto assembleLieferantDto(Lieferant lieferant) {
		return LieferantDtoAssembler.createDto(lieferant);
	}

	private LieferantDto[] assembleLieferantDtos(Collection<?> lieferanten) {
		List<LieferantDto> list = new ArrayList<LieferantDto>();
		if (lieferanten != null) {
			Iterator<?> iterator = lieferanten.iterator();
			while (iterator.hasNext()) {
				Lieferant lieferant = (Lieferant) iterator.next();
				list.add(assembleLieferantDto(lieferant));
			}
		}
		LieferantDto[] returnArray = new LieferantDto[list.size()];
		return (LieferantDto[]) list.toArray(returnArray);
	}

	public BigDecimal[] getUmsaetzeVomLieferantenImZeitraum(TheClientDto theClientDto, Integer lieferantIId, Date dVon,
			Date dBis) {
		Session session = null;

		BigDecimal[] bd = new BigDecimal[3];

		// Bestellungsumsatz

		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRBestellung.class);
		c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));

		// Ohne Rahmen, da ja sonst doppelt

		c.add(Restrictions.not(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
				BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)));

		if (lieferantIId != null) {
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE, lieferantIId));
		}

		if (dVon != null) {
			c.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, dVon));
		}
		if (dBis != null) {
			c.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, dBis));
		}
		// Filter nach Status
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
		cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
		cStati.add(BestellungFac.BESTELLSTATUS_GELIEFERT);
		cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
		cStati.add(BestellungFac.BESTELLSTATUS_TEILERLEDIGT);
		cStati.add(BestellungFac.BESTELLSTATUS_ABGERUFEN);

		c.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati));
		List<?> list = c.list();
		BigDecimal bdUmsatz = new BigDecimal(0);
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRBestellung flrbestellung = (FLRBestellung) iter.next();
			if (flrbestellung.getN_bestellwert() != null) {
				BigDecimal bdWertinmandantenwaehrung = flrbestellung.getN_bestellwert();
				if (!flrbestellung.getWaehrung_c_nr_bestellwaehrung().equals(theClientDto.getSMandantenwaehrung())) {
					BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
							flrbestellung.getF_wechselkursmandantwaehrungbestellungswaehrung().doubleValue());
					bdWertinmandantenwaehrung = getBetragMalWechselkurs(bdWertinmandantenwaehrung,
							Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));

					System.out.println(" BS:" + flrbestellung.getC_nr() + " W:" + flrbestellung.getN_bestellwert());
				}

				bdUmsatz = bdUmsatz.add(bdWertinmandantenwaehrung);

			}
		}

		bd[UMSAETZE_BESTELLUNGSUMSATZ] = bdUmsatz;

		session.close();

		// ERUmsatz
		GregorianCalendar gcVon = new GregorianCalendar();
		gcVon.setTimeInMillis(dVon.getTime());
		GregorianCalendar gcBis = new GregorianCalendar();
		gcBis.setTimeInMillis(dBis.getTime());

		try {

			BigDecimal bdER = getEingangsrechnungFac().berechneSummeUmsatzNetto(theClientDto, lieferantIId,
					EingangsrechnungFac.KRIT_DATUM_BELEGDATUM, theClientDto.getSMandantenwaehrung(), gcVon, gcBis,
					false);

			BigDecimal bdZK = getEingangsrechnungFac().berechneSummeUmsatzNetto(theClientDto, lieferantIId,
					EingangsrechnungFac.KRIT_DATUM_BELEGDATUM, theClientDto.getSMandantenwaehrung(), gcVon, gcBis,
					true);

			bd[UMSAETZE_EINGANGSRECHNUNGSUMSATZ] = bdER.add(bdZK);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// WE-Umsatz nach WE-Datum

		String sQuery = "SELECT we FROM FLRWareneingang we WHERE we.t_wareneingansdatum>='"
				+ Helper.formatDateWithSlashes(dVon) + "' AND we.t_wareneingansdatum<='"
				+ Helper.formatDateWithSlashes(dBis) + "'";
		sQuery += " AND we.flrbestellung.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND we.flrbestellung.bestellungstatus_c_nr <>'" + BestellungFac.BESTELLSTATUS_STORNIERT + "' ";
		sQuery += " AND we.flrbestellung.lieferant_i_id_bestelladresse=" + lieferantIId
				+ " AND we.flrbestellung.bestellungstatus_c_nr <>'" + BestellungFac.BESTELLSTATUS_STORNIERT + "' ";
		session = factory.openSession();

		org.hibernate.Query query = session.createQuery(sQuery);

		list = query.list();
		BigDecimal bdWEUmsatz = new BigDecimal(0);
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRWareneingang we = (FLRWareneingang) iter.next();
			try {
				BigDecimal bdWESumme = getWareneingangFac().getWareneingangWertsumme(
						getWareneingangFac().wareneingangFindByPrimaryKey(we.getI_id()), theClientDto);

				if (bdWESumme != null) {
					BigDecimal bdWertinmandantenwaehrung = we.getFlrbestellung().getN_bestellwert();
					if (!we.getFlrbestellung().getWaehrung_c_nr_bestellwaehrung()
							.equals(theClientDto.getSMandantenwaehrung())) {
						BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(we.getFlrbestellung()
								.getF_wechselkursmandantwaehrungbestellungswaehrung().doubleValue());
						bdWertinmandantenwaehrung = getBetragMalWechselkurs(bdWertinmandantenwaehrung,
								Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));

						System.out.println(" BS:" + we.getFlrbestellung().getC_nr() + " W:"
								+ we.getFlrbestellung().getN_bestellwert());
					}

					bdWEUmsatz = bdWEUmsatz.add(bdWESumme);

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		bd[UMSAETZE_WARENEINGANGSUMSATZ] = bdWEUmsatz;

		return bd;

	}

	public void removeLieferantPartnerRechnungsadresse(LieferantDto lieferantDtoI, TheClientDto theClientDto) {

		// precondition
		if (lieferantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("lieferantDtoI == null"));
		}

		// 1 Partnerrechnungsadresse des Lieferanten = null.
		// Wer hat wann geloescht?
		Integer iIdPartnerRE = lieferantDtoI.getPartnerRechnungsadresseDto().getIId();
		if (iIdPartnerRE != null) {
			// Update.
			lieferantDtoI.setPartnerIIdRechnungsadresse(null);
			lieferantDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lieferantDtoI.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			// 3 Lieferant update.
			updateLieferant(lieferantDtoI, theClientDto);
		}
	}

	/**
	 * &Uuml;berpr&uuml;ft, ob Ziel- und Quelllieferant vollst&auml;ndige
	 * Lieferanten sind
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @throws EJBExceptionLP
	 */
	private void checkInputParamsZielQuellLieferantDtos(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto)
			throws EJBExceptionLP {

		if (lieferantZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferantZielDto == null (Ziel)"));
		}

		if (lieferantZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantZielDto.getIId() == null (Ziel)"));
		}

		if (lieferantQuellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferantQuellDto == null (Quell)"));
		}

		if (lieferantQuellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantQuellDto.getIId() == null (Quell)"));
		}
	}

	/**
	 * H&auml;ngt die Liefergruppe von dem Quell- auf den Ziellieferanten um
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignLiefergruppeBeimZusammenfuehren(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		/*
		 * PART_LFLFLIEFERGRUPPE - lieferant_i_id
		 */
		LflfliefergruppeDto[] aLiefergruppeDtos = null;
		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		try {
			aLiefergruppeDtos = getLieferantFac().lflfliefergruppeFindByLieferantIIdOhneExc(lieferantQuellDto.getIId(),
					theClientDto);
			if (aLiefergruppeDtos != null) {
				for (int j = 0; j < aLiefergruppeDtos.length; j++) {
					aLiefergruppeDtos[j].setLieferantIId(lieferantZielDto.getIId());
					LflfliefergruppeDto[] aLiefergruppeZielDtos = null;
					// pruefen, ob dieser eintrag beim ziellieferanten nicht
					// schon existiert
					aLiefergruppeZielDtos = getLieferantFac().lflfliefergruppeFindByLieferantIIdLiefergruppeIIdOhneExc(
							lieferantZielDto.getIId(), aLiefergruppeDtos[j].getLfliefergruppeIId(), theClientDto);
					getLieferantFac().removeLflfliefergruppe(lieferantQuellDto.getIId(),
							aLiefergruppeDtos[j].getLfliefergruppeIId(), theClientDto);
					if (aLiefergruppeZielDtos.length < 1) {
						getLieferantFac().createLflfliefergruppe(aLiefergruppeDtos[j], theClientDto);
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt den Artikellieferanten vom Quelllieferanten auf den
	 * Ziellieferanten um Existiert beim Ziellieferant bereits ein
	 * Artikellieferanteintrag zu einem Artikel/Lieferanten, so wird dieser Eintrag
	 * beim Quelllieferanten gel&ouml;scht inkl Mengenstaffel
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignArtikellieferantBeimZusammenfuehren(LieferantDto lieferantZielDto,
			LieferantDto lieferantQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {
		/*
		 * WW_ARTIKELLIEFERANT - lieferant_i_id (ein Eintrag pro ArtikelId und
		 * LieferantId) -> verknuepft: artikellieferantstaffel
		 */
		ArtikellieferantDto[] aArtikellieferantDtos = null;
		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		try {
			aArtikellieferantDtos = getArtikelFac().artikellieferantFindByLieferantIId(lieferantQuellDto.getIId(),
					theClientDto);

			if (aArtikellieferantDtos != null) {
				for (int j = 0; j < aArtikellieferantDtos.length; j++) {
					// pruefen, ob es zum aktuellen artikel bereits einen
					// Eintrag beim Ziellieferanten gibt
					ArtikellieferantDto artikellieferantZielDto = getArtikelFac().getArtikelEinkaufspreis(
							aArtikellieferantDtos[j].getArtikelIId(), lieferantZielDto.getIId(), BigDecimal.ONE,
							lieferantQuellDto.getWaehrungCNr(), new java.sql.Date(System.currentTimeMillis()),
							theClientDto);

					ArtikellieferantDto alDto = getArtikelFac()
							.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
									aArtikellieferantDtos[j].getArtikelIId(), lieferantZielDto.getIId(),
									aArtikellieferantDtos[j].getTPreisgueltigab(), theClientDto);

					if (artikellieferantZielDto == null && alDto == null) {

						// lieferantiid umhaengen
						LieferantDto lieferantDto = getLieferantFac()
								.lieferantFindByPrimaryKey(lieferantZielDto.getIId(), theClientDto);
						aArtikellieferantDtos[j].setLieferantDto(lieferantDto);
						aArtikellieferantDtos[j].setLieferantIId(lieferantZielDto.getIId());
						getArtikelFac().updateArtikellieferant(aArtikellieferantDtos[j], theClientDto);

					} else {
						// artikellieferant inkl. staffel loeschen
						ArtikellieferantstaffelDto[] aArtikellieferantstaffelDtos = getArtikelFac()
								.artikellieferantstaffelFindByArtikellieferantIId(aArtikellieferantDtos[j].getIId());
						if (aArtikellieferantstaffelDtos != null && aArtikellieferantstaffelDtos.length > 0) {
							// zugehoerige Staffeln entfernen
							for (int k = 0; k < aArtikellieferantstaffelDtos.length; k++) {
								getArtikelFac().removeArtikellieferantstaffel(aArtikellieferantstaffelDtos[k]);
							}
						}
						getArtikelFac().removeArtikellieferant(aArtikellieferantDtos[j]);
					}
				}
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei der Anfrage (wenn vorhanden) die Anfrageadresse vom
	 * Quelllieferanten auf den Ziellieferanten um Ist bei der Anfrage auch ein
	 * Ansprechpartner von dem Quellieferanten eingetragen, so wird diese
	 * Ansprechpartnerzuordnung zu diesem Angebot gel&ouml;scht, da dieser
	 * Ansprechpartner nicht zum Ziellieferanten geh&ouml;rt
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param mandantCNr        String
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignAnfrageBeimZusammenfuehren(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		// ANF_ANFRAGE - lieferant_i_id_anfrageadresse (mandantcnr)

		try {
			AnfrageDto[] aAnfrageDtos = null;
			aAnfrageDtos = getAnfrageFac().anfrageFindByLieferantIIdAnfrageadresseMandantCNr(lieferantQuellDto.getIId(),
					mandantCNr, theClientDto);
			for (int j = 0; j < aAnfrageDtos.length; j++) {
				if (aAnfrageDtos[j] != null) {
					// ansprechpartner dazu loeschen, moeglich waere auch,
					// einfach einen der zielansprechpartner einzusetzen, falls
					// welche vorhanden sind
					Anfrage zeile = em.find(Anfrage.class, aAnfrageDtos[j].getIId());
					zeile.setLieferantIIdAnfrageadresse(lieferantZielDto.getIId());
					zeile.setAnsprechpartnerIIdLieferant(null);
					em.merge(zeile);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public void reassignLieferantbeurteilungBeimZusammenfuehren(LieferantDto lieferantZielDto,
			LieferantDto lieferantQuellDto, TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("LieferantbeurteilungfindByLieferantIId");
		query.setParameter(1, lieferantQuellDto.getIId());

		Collection<Lieferantbeurteilung> c = query.getResultList();

		Iterator it = c.iterator();
		while (it.hasNext()) {

			// ansprechpartner dazu loeschen, moeglich waere auch,
			// einfach einen der zielansprechpartner einzusetzen, falls
			// welche vorhanden sind
			Lieferantbeurteilung zeile = (Lieferantbeurteilung) it.next();

			try {
				Query queryVorhanden = em.createNamedQuery("LieferantbeurteilungfindByLieferantIIdTDatum");
				queryVorhanden.setParameter(1, lieferantZielDto.getIId());
				queryVorhanden.setParameter(2, zeile.getTDatum());

				Lieferantbeurteilung lieferantbeurteilungVorhanden = (Lieferantbeurteilung) queryVorhanden
						.getSingleResult();

				// Vorhandenen Aendern
				lieferantbeurteilungVorhanden.setLieferantIId(lieferantZielDto.getIId());

				em.merge(lieferantbeurteilungVorhanden);
				em.flush();

				// Und Quelle loeschen
				em.remove(zeile);

			} catch (NoResultException ex) {

				zeile.setLieferantIId(lieferantZielDto.getIId());
				em.merge(zeile);
				em.flush();

			}

		}

	}

	/**
	 * H&auml;ngt bei der Bestellung (wenn vorhanden) die Bestell- und
	 * Rechnungsadresse vom Quelllieferanten auf den Ziellieferanten um Ist bei der
	 * bestellung auch ein Ansprechpartner von dem Quellieferanten eingetragen, so
	 * wird diese Ansprechpartnerzuordnung zu dieser Bestellung gel&ouml;scht, da
	 * dieser Ansprechpartner nicht zum Ziellieferanten geh&ouml;rt
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param mandantCNr        String
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignBestellungBeimZusammenfuehren(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		// BES_BESTELLUNG - lieferant_i_id_bestelladresse (mandantcnr)
		try {
			BestellungDto[] aBestellungDtos = null;
			aBestellungDtos = getBestellungFac().bestellungFindByLieferantIIdBestelladresseMandantCNrOhneExc(
					lieferantQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aBestellungDtos.length; j++) {
				if (aBestellungDtos[j] != null) {

					Bestellung bestellung = em.find(Bestellung.class, aBestellungDtos[j].getIId());
					bestellung.setLieferantIIdBestelladresse(lieferantZielDto.getIId());
					bestellung.setAnsprechpartnerIId(null);
					em.merge(bestellung);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		// BES_BESTELLUNG - lieferant_i_id_rechnungsadresse (mandantcnr)
		try {
			BestellungDto[] aBestellungDtos = null;
			aBestellungDtos = getBestellungFac().bestellungFindByLieferantIIdRechnungsadresseMandantCNrOhneExc(
					lieferantQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aBestellungDtos.length; j++) {
				if (aBestellungDtos[j] != null) {

					Bestellung bestellung = em.find(Bestellung.class, aBestellungDtos[j].getIId());
					bestellung.setLieferantIIdRechnungsadresse(lieferantZielDto.getIId());
					// ansprechpartner dazu loeschen; moeglich waere auch,
					// einfach einen der zielansprechpartner einzusetzen, falls
					// welche vorhanden sind
					bestellung.setAnsprechpartnerIId(null);
					em.merge(bestellung);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt bei Bestellvorschl&auml;gen den Lieferanten vom Quelllieferanten
	 * auf den Ziellieferanten um
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param mandantCNr        String
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignBestellvorschlagBeimZusammenfuehren(LieferantDto lieferantZielDto,
			LieferantDto lieferantQuellDto, String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		// BES_BESTELLVORSCHLAG - lieferant_i_id (mandant_cnr)
		try {
			BestellvorschlagDto[] aBestellvorschlagDtos = null;
			aBestellvorschlagDtos = getBestellvorschlagFac()
					.bestellvorschlagFindByLieferantIIdMandantCNrOhneExc(lieferantQuellDto.getIId(), mandantCNr);
			for (int j = 0; j < aBestellvorschlagDtos.length; j++) {
				if (aBestellvorschlagDtos[j] != null) {
					Bestellvorschlag zeile = em.find(Bestellvorschlag.class, aBestellvorschlagDtos[j].getIId());
					zeile.setLieferantIId(lieferantZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei Eingangsrechnungen den Lieferanten vom Quelllieferanten auf
	 * den Ziellieferanten um
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param mandantCNr        String
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignEingangsrechnungBeimZusammenfuehren(LieferantDto lieferantZielDto,
			LieferantDto lieferantQuellDto, String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		// ER_EINGANGSRECHNUNG - lieferant_i_id (mandant_cnr)
		try {
			EingangsrechnungDto[] aEingangsrechnungDtos = null;
			aEingangsrechnungDtos = getEingangsrechnungFac().eingangsrechnungFindByMandantLieferantIId(mandantCNr,
					lieferantQuellDto.getIId());
			for (int j = 0; j < aEingangsrechnungDtos.length; j++) {
				if (aEingangsrechnungDtos[j] != null) {
					Eingangsrechnung zeile = em.find(Eingangsrechnung.class, aEingangsrechnungDtos[j].getIId());
					zeile.setLieferantIId(lieferantZielDto.getIId());
					em.merge(zeile);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei Reklamationen vom Typ Lieferant den Lieferanten vom
	 * Quelllieferanten auf den Ziellieferanten um Ist zu einem Quelllieferanten ein
	 * Ansprechpartner eingetragen, so wird dieser beim Umh&auml;ngen auf null
	 * gesetzt
	 * 
	 * @param lieferantZielDto  LieferantDto
	 * @param lieferantQuellDto LieferantDto
	 * @param mandantCNr        String
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void reassignReklamationBeimZusammenfuehren(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		checkInputParamsZielQuellLieferantDtos(lieferantZielDto, lieferantQuellDto);

		// REKLA_REKLAMATION lieferant_i_id (mandant_cnr)

		ReklamationDto[] aReklamationDtos = null;
		aReklamationDtos = getReklamationFac()
				.reklamationFindByLieferantIIdMandantCNrOhneExc(lieferantQuellDto.getIId(), mandantCNr);
		for (int j = 0; j < aReklamationDtos.length; j++) {
			if (aReklamationDtos[j] != null) {

				Reklamation zeile = em.find(Reklamation.class, aReklamationDtos[j].getIId());
				zeile.setLieferantIId(lieferantZielDto.getIId());
				zeile.setAnsprechpartnerIIdLieferant(null);
				em.merge(zeile);
				em.flush();

			}
		}

	}

	public void reassignInseratBeimZusammenfuehren(LieferantDto lieferantZielDto, LieferantDto lieferantQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("InseratfindByLieferantIId");
		query.setParameter(1, lieferantQuellDto.getIId());
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Inserat b = (Inserat) it.next();
			b.setLieferantIId(lieferantZielDto.getIId());
			b.setAnsprechpartnerIIdLieferant(null);
			em.merge(b);
			em.flush();
		}

	}

	/**
	 * Fuehrt zwei Lieferanten zusammen
	 * 
	 * @param lieferantZielDto     LieferantDto
	 * @param lieferantQuellDtoIid int
	 * @param iLieferantPartnerIId - falls der Partner vom Quelllieferanten
	 *                             verwendet werden soll
	 * @param theClientDto         String
	 * @throws EJBExceptionLP
	 */
	public void zusammenfuehrenLieferant(LieferantDto lieferantZielDto, int lieferantQuellDtoIid,
			Integer iLieferantPartnerIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		LieferantDto lieferantQuellDto = null;
		MandantDto[] aMandantDtos = null;

		if (lieferantZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferantZielDto == null (Ziel oder Quell)"));
		}

		if (lieferantZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantZielDto.getIId() == null (Ziel)"));
		}

		Lieferant lieferantQuelle = em.find(Lieferant.class, lieferantQuellDtoIid);

		Lieferant lieferantZiel = em.find(Lieferant.class, lieferantZielDto.getIId());

		if (lieferantQuelle.getKontoIIdKreditorenkonto() != null && lieferantZiel.getKontoIIdKreditorenkonto() != null
				&& !lieferantQuelle.getKontoIIdKreditorenkonto().equals(lieferantZiel.getKontoIIdKreditorenkonto())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN,
					new Exception("FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN"));
		}

		if (lieferantQuelle.getKontoIIdKreditorenkonto() != null
				&& lieferantZiel.getKontoIIdKreditorenkonto() == null) {

			// Quelle besetzt, Ziel jedoch nicht -> Ziel bekommt Quelle
			lieferantZielDto.setKontoIIdKreditorenkonto(lieferantQuelle.getKontoIIdKreditorenkonto());

		}

		if (lieferantQuelle.getMwstsatzIId() != null && lieferantZiel.getMwstsatzIId() != null
				&& !lieferantQuelle.getMwstsatzIId().equals(lieferantZiel.getMwstsatzIId())) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		if ((lieferantQuelle.getMwstsatzIId() != null && lieferantZiel.getMwstsatzIId() == null)
				|| (lieferantZiel.getMwstsatzIId() != null && lieferantQuelle.getMwstsatzIId() == null)) {
			// Beide besetzt, jedoch ungleich -> nicht moeglich
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST"));
		}

		lieferantQuellDto = lieferantFindByPrimaryKey(lieferantQuellDtoIid, theClientDto);

		myLogger.info("Ziellieferant: " + lieferantZielDto.toString());
		myLogger.info("Quelllieferant: " + lieferantQuellDto.toString());
		myLogger.info("iLieferantPartnerIId: " + iLieferantPartnerIId);

		// neue Ziel-Dto-Daten in die DB zurueckschreiben
		if ((lieferantZielDto.getPartnerDto() == null && lieferantZielDto.getPartnerIId() != null)
				|| (lieferantZielDto.getPartnerDto() != null && lieferantZielDto.getPartnerIId() != null
						&& lieferantZielDto.getPartnerDto().getIId() == null)) {

			lieferantZielDto.setPartnerDto(
					getPartnerFac().partnerFindByPrimaryKey(lieferantZielDto.getPartnerIId(), theClientDto));
		}

		lieferantZielDto.setUpdateModeKreditorenkonto(LieferantDto.I_UPD_KREDITORENKONTO_UPDATE);

		if (lieferantZielDto.getKontoIIdKreditorenkonto() != null) {
			lieferantZielDto.setIKreditorenkontoAsIntegerNotiId(new Integer(
					getFinanzFac().kontoFindByPrimaryKey(lieferantZielDto.getKontoIIdKreditorenkonto()).getCNr()));
		}

		updateLieferant(lieferantZielDto, theClientDto);

		try {

			// mandantenunabhaengig
			getDokumenteFac().vertauscheBelegartIdBeiBelegartdokumenten(LocaleFac.BELEGART_LIEFERANT,
					lieferantQuellDto.getIId(), lieferantZielDto.getIId(), theClientDto);
			getJCRDocFac().fuehreDokumenteZusammen(lieferantZielDto, lieferantQuellDto);
			reassignLiefergruppeBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, theClientDto);
			reassignArtikellieferantBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, theClientDto);

			getPartnerFac().reassignPartnerkommentarBeimZusammenfuehren(lieferantZielDto.getPartnerIId(),
					lieferantQuellDto.getPartnerIId(), false, theClientDto);

			// mandantenabhaengig
			aMandantDtos = getMandantFac().mandantFindAll(theClientDto);
			int i = 0;
			while (i < aMandantDtos.length) {
				reassignAnfrageBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignBestellungBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignBestellvorschlagBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto,
						aMandantDtos[i].getCNr(), theClientDto);
				reassignEingangsrechnungBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto,
						aMandantDtos[i].getCNr(), theClientDto);
				reassignReklamationBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				reassignInseratBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, aMandantDtos[i].getCNr(),
						theClientDto);
				i++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// PJ977
		reassignLieferantbeurteilungBeimZusammenfuehren(lieferantZielDto, lieferantQuellDto, theClientDto);

		// Quelllieferant loeschen
		if (lieferantQuellDto != null) {

			try {
				getLieferantFac().removeLieferant(lieferantQuellDto, theClientDto);
				// Den Partner aendern, falls gewuenscht (ist erst hier moeglich
				// wegen uk in part_kunde)
				if (iLieferantPartnerIId != null) {
					lieferantZielDto.setPartnerIId(iLieferantPartnerIId);
					lieferantZielDto
							.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(iLieferantPartnerIId, theClientDto));
					updateLieferant(lieferantZielDto, theClientDto);
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

		}
	}

	public void updateLieferantRechnungsadresse(LieferantDto lieferantDtoI, TheClientDto theClientDto) {
		// precondition
		if (lieferantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("lieferantDtoI == null"));
		}
		if (lieferantDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("lieferantDtoI.getPartnerDto() == null"));
		}

		try {
			// Partnerrechnungsadresse des Lieferanten CU.
			Integer iIdPartnerRE = lieferantDtoI.getPartnerRechnungsadresseDto().getIId();
			if (iIdPartnerRE != null) {
				// Update.
				getPartnerFac().updatePartner(lieferantDtoI.getPartnerRechnungsadresseDto(), theClientDto);
			} else {
				// Create; Rechnungsadr. ist erfasst worden.
				iIdPartnerRE = getPartnerFac().createPartner(lieferantDtoI.getPartnerRechnungsadresseDto(),
						theClientDto);
			}

			lieferantDtoI.setPartnerIIdRechnungsadresse(iIdPartnerRE);

			// Lieferanten updaten.
			updateLieferant(lieferantDtoI, theClientDto);

			// Der ausgewaehlte Partner muss jetzt noch als Lieferant angelegt
			// werden (wenn noch nicht vorhanden)
			LieferantDto lieferrantDtoREAdr = lieferantFindByiIdPartnercNrMandantOhneExc(iIdPartnerRE,
					lieferantDtoI.getMandantCNr(), theClientDto);
			if (lieferrantDtoREAdr == null) {
				// er ist noch nicht angelegt -> jetzt anlegen
				LieferantDto kundeDtoNew = new LieferantDto();
				kundeDtoNew.setMandantCNr(lieferantDtoI.getMandantCNr());
				kundeDtoNew.setPartnerIId(lieferantDtoI.getPartnerIIdRechnungsadresse());
				kundeDtoNew.setPartnerDto(lieferantDtoI.getPartnerRechnungsadresseDto());
				kundeDtoNew.setIIdKostenstelle(lieferantDtoI.getIIdKostenstelle());
				kundeDtoNew.setMwstsatzbezIId(lieferantDtoI.getMwstsatzbezIId());
				kundeDtoNew.setLieferartIId(lieferantDtoI.getLieferartIId());
				kundeDtoNew.setIdSpediteur(lieferantDtoI.getIdSpediteur());

				kundeDtoNew.setZahlungszielIId(lieferantDtoI.getZahlungszielIId());
				kundeDtoNew.setWaehrungCNr(lieferantDtoI.getWaehrungCNr());

				kundeDtoNew.setBBeurteilen(lieferantDtoI.getBBeurteilen());
				kundeDtoNew.setBIgErwerb(lieferantDtoI.getBIgErwerb());
				kundeDtoNew.setBMoeglicherLieferant(lieferantDtoI.getBMoeglicherLieferant());
				kundeDtoNew.setBZollimportpapier(lieferantDtoI.getBZollimportpapier());

				kundeDtoNew.setBVersteckterkunde(lieferantDtoI.getBVersteckterkunde());
				kundeDtoNew.setReversechargeartId(lieferantDtoI.getReversechargeartId());
				createLieferant(kundeDtoNew, theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public ArrayList getWareneingangspositionen(StatistikParamDto statistikParamDtoI, String sWaehrungI,
			boolean bVerdichtetNachArtikel, boolean bEingeschraenkt, TheClientDto theClientDto) throws EJBExceptionLP {

		Session sessionWEPOS = null;
		ArrayList<WareneingangspositionenDto> alData = new ArrayList<WareneingangspositionenDto>(10);
		try {
			// in die gewuenschte Waehrung umrechnen
			SessionFactory factory = FLRSessionFactory.getFactory();

			sessionWEPOS = factory.openSession();
			Criteria cWEPOS = sessionWEPOS.createCriteria(FLRWareneingangspositionen.class);
			cWEPOS.createAlias(WareneingangFac.FLR_WEPOS_FLRWARENEINGANG, "we");

			if (statistikParamDtoI.getId() != null) {
				cWEPOS.createAlias("we." + WareneingangFac.FLR_WE_FLRBESTELLUNG, "b");
				cWEPOS.add(Restrictions.eq("b." + BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
						statistikParamDtoI.getId()));
			}

			if (statistikParamDtoI.getDDatumVon() != null) {
				cWEPOS.add(Restrictions.ge("we." + WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM,
						Helper.cutDate(statistikParamDtoI.getDDatumVon())));
			}
			if (statistikParamDtoI.getDDatumBis() != null) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(statistikParamDtoI.getDDatumBis().getTime());
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);

				java.sql.Date dBis = new java.sql.Date(c.getTimeInMillis());

				cWEPOS.add(Restrictions.le("we." + WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM, dBis));
			}

			cWEPOS.addOrder(Order.desc("we." + WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM));

			WareneingangspositionenDto ks = null;

			if (bEingeschraenkt) {
				cWEPOS.setMaxResults(50);
			}

			List<?> lWEPOS = cWEPOS.list();

			// BSPOS
			for (int k = 0; k < lWEPOS.size(); k++) {
				FLRWareneingangspositionen wepos = ((FLRWareneingangspositionen) lWEPOS.get(k));
				ks = new WareneingangspositionenDto();

				ks.setSWas("WEPOS");
				ks.setBdMenge(wepos.getN_geliefertemenge());
				ks.setSIdent(wepos.getFlrbestellposition().getFlrartikel().getC_nr());

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						wepos.getFlrbestellposition().getFlrartikel().getI_id(), theClientDto);
				String sHelp = wepos.getFlrbestellposition().getFlrbestellung().getC_nr();
				ks.setSNr(wepos.getFlrbestellposition().getFlrbestellung().getC_nr());
				ks.setDBelegdatum(wepos.getFlrwareneingang().getT_wareneingansdatum());
				ks.setSBezeichnung(artikelDto.formatBezeichnung());

				String sWaehrungBS = wepos.getFlrbestellposition().getFlrbestellung()
						.getWaehrung_c_nr_bestellwaehrung();

				BigDecimal bDW = new BigDecimal(0);
				if (wepos.getN_gelieferterpreis() != null) {
					bDW = getLocaleFac().rechneUmInAndereWaehrungZuDatum(wepos.getN_gelieferterpreis(), sWaehrungBS,
							sWaehrungI, new Date(System.currentTimeMillis()), theClientDto);
				}

				ks.setBdPreis(bDW);
				ks.setBdWert(wepos.getN_geliefertemenge().multiply(bDW));

				ks.setSEinheit(wepos.getFlrbestellposition().getEinheit_c_nr());
				if (bVerdichtetNachArtikel) {
					boolean bGefunden = false;
					for (int j = 0; j < alData.size(); j++) {
						WareneingangspositionenDto temp = (WareneingangspositionenDto) alData.get(j);
						if (temp.getSIdent().equals(ks.getSIdent())) {

							BigDecimal wertNeu = ks.getBdWert().add(temp.getBdWert());
							BigDecimal mengeNeu = ks.getBdMenge().add(temp.getBdMenge());
							BigDecimal preisNeu = new BigDecimal(0);
							if (mengeNeu.doubleValue() != 0) {
								preisNeu = wertNeu.divide(mengeNeu, 4, BigDecimal.ROUND_HALF_EVEN);
							}

							temp.setBdWert(wertNeu);
							temp.setBdMenge(mengeNeu);
							temp.setBdPreis(preisNeu);
							temp.setDBelegdatum(null);
							temp.setSNr("");
							temp.setSWas("");

							alData.set(j, temp);
							bGefunden = true;
							break;
						}
					}
					if (bGefunden == false) {
						alData.add(ks);
					}
				} else {
					alData.add(ks);
				}
			}
			sessionWEPOS.close();
			sessionWEPOS = null;

		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		} finally {

			if (sessionWEPOS != null) {
				sessionWEPOS.close();
			}
			sessionWEPOS = null;
		}
		return alData;
	}

	public Integer createVerstecktenLieferantAusKunden(Integer kundeIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Integer iIdLieferant = null;

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
				kundeDto.getPartnerIId(), kundeDto.getMandantCNr(), theClientDto);
		// Neuen versteckten Kunde anlegen wenn Lieferant kein Kunde ist
		if (lieferantDto == null) {
			lieferantDto = new LieferantDto();
			lieferantDto.setPartnerDto(kundeDto.getPartnerDto());
			lieferantDto.setPartnerIId(kundeDto.getPartnerIId());
			lieferantDto.setBVersteckterkunde(Helper.boolean2Short(true));
			// Vorbelegungen werden vom Mandanten geholt
			MandantDto mandant = getMandantFac().mandantFindByPrimaryKey(kundeDto.getMandantCNr(), theClientDto);
			lieferantDto.setMandantCNr(mandant.getCNr());
			lieferantDto.setIIdKostenstelle(mandant.getIIdKostenstelle());
			lieferantDto.setMwstsatzbezIId(mandant.getMwstsatzbezIIdStandardinlandmwstsatz());
			lieferantDto.setLieferartIId(mandant.getLieferartIIdLF());
			lieferantDto.setIdSpediteur(mandant.getSpediteurIIdLF());
			lieferantDto.setZahlungszielIId(mandant.getZahlungszielIIdLF());

			lieferantDto.setWaehrungCNr(kundeDto.getCWaehrung());
			lieferantDto.setBBeurteilen(Helper.boolean2Short(false));

			// lieferantDto.setBReversecharge(Helper.getShortFalse());
			lieferantDto.setReversechargeartId(kundeDto.getReversechargeartId());
			lieferantDto.setBIgErwerb(Helper.getShortFalse());

			lieferantDto.setKontoIIdKreditorenkonto(kundeDto.getIidDebitorenkonto());

			iIdLieferant = createLieferant(lieferantDto, theClientDto);

		}

		else {
			iIdLieferant = lieferantDto.getIId();
		}

		return iIdLieferant;
	}

	public LflfliefergruppePK createLflfliefergruppe(LflfliefergruppeDto lflfliefergruppeDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (lflfliefergruppeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lflfliefergruppeDtoI == null"));
		}
		if (lflfliefergruppeDtoI.getLieferantIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("getLieferantIId.getCNr() == null"));
		}

		
		try {
			Query query = em.createNamedQuery("LflfliefergruppefindByLieferantIIdLiefergruppeIId");
			query.setParameter(1, lflfliefergruppeDtoI.getLieferantIId());
			query.setParameter(2, lflfliefergruppeDtoI.getLfliefergruppeIId());
			Lflfliefergruppe doppelt = (Lflfliefergruppe) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_LFLFLIEFERGRUPPE.UK"));
			}
		} catch (NoResultException ex) {
			//
		} 
		
		
		Lflfliefergruppe lflfliefergruppe = null;
		try {
			lflfliefergruppe = new Lflfliefergruppe(lflfliefergruppeDtoI.getLieferantIId(),
					lflfliefergruppeDtoI.getLfliefergruppeIId());
			em.persist(lflfliefergruppe);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new LflfliefergruppePK(lflfliefergruppe.getLieferantIId(), lflfliefergruppe.getLfliefergruppeIId());
	}

	public void removeLflfliefergruppe(Integer lieferantIId, Integer lfliefergruppeIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("lieferantIId == null"));
		}
		if (lfliefergruppeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception("lfliefergruppeIIdI == null"));
		}

		LflfliefergruppePK lflfliefergruppePK = new LflfliefergruppePK();
		lflfliefergruppePK.setLieferantIId(lieferantIId);
		lflfliefergruppePK.setLfliefergruppeIId(lfliefergruppeIIdI);
		Lflfliefergruppe toRemove = em.find(Lflfliefergruppe.class, lflfliefergruppePK);
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

	public LflfliefergruppeDto lflfliefergruppeFindByPrimaryKey(Integer lieferantIId, Integer lfliefergruppeIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lieferantIId == null"));
		}
		if (lfliefergruppeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lfliefergruppeIIdI == null"));
		}

		// try {
		LflfliefergruppePK lflfliefergruppePK = new LflfliefergruppePK();
		lflfliefergruppePK.setLieferantIId(lieferantIId);
		lflfliefergruppePK.setLfliefergruppeIId(lfliefergruppeIIdI);
		Lflfliefergruppe lflfliefergruppe = em.find(Lflfliefergruppe.class, lflfliefergruppePK);
		if (lflfliefergruppe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLflfliefergruppeDto(lflfliefergruppe);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIId(Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lieferantIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("LflfliefergruppefindByLieferantIId");
		query.setParameter(1, lieferantIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleLflfliefergruppeDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdOhneExc(Integer lieferantIId,
			TheClientDto theClientDto) {
		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lieferantIId == null"));
		}
		Query query = em.createNamedQuery("LflfliefergruppefindByLieferantIId");
		query.setParameter(1, lieferantIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleLflfliefergruppeDtos(cl);
	}

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdLiefergruppeIId(Integer lieferantIId,
			Integer liefergruppeIId, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lieferantIId == null"));
		}
		if (liefergruppeIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("liefergruppeIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("LflfliefergruppefindByLieferantIIdLiefergruppeIId");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, liefergruppeIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleLflfliefergruppeDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LflfliefergruppeDto[] lflfliefergruppeFindByLieferantIIdLiefergruppeIIdOhneExc(Integer lieferantIId,
			Integer liefergruppeIId, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lieferantIId == null"));
		}
		if (liefergruppeIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("liefergruppeIId == null"));
		}
		Query query = em.createNamedQuery("LflfliefergruppefindByLieferantIIdLiefergruppeIId");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, liefergruppeIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// return null;
		// }
		return assembleLflfliefergruppeDtos(cl);
	}

	private LflfliefergruppeDto assembleLflfliefergruppeDto(Lflfliefergruppe lflfliefergruppe) {
		return LflfliefergruppeDtoAssembler.createDto(lflfliefergruppe);
	}

	private LflfliefergruppeDto[] assembleLflfliefergruppeDtos(Collection<?> liefergruppen) {
		List<LflfliefergruppeDto> list = new ArrayList<LflfliefergruppeDto>();
		if (liefergruppen != null) {
			Iterator<?> iterator = liefergruppen.iterator();
			while (iterator.hasNext()) {
				Lflfliefergruppe liefergruppe = (Lflfliefergruppe) iterator.next();
				list.add(assembleLflfliefergruppeDto(liefergruppe));
			}
		}
		LflfliefergruppeDto[] returnArray = new LflfliefergruppeDto[list.size()];
		return (LflfliefergruppeDto[]) list.toArray(returnArray);
	}

	private void setLfliefergruppesprFromLfliefergruppesprDto(Lfliefergruppespr lfliefergruppespr,
			LfliefergruppesprDto lfliefergruppesprDto) {
		lfliefergruppespr.setCBez(lfliefergruppesprDto.getCBez());
		em.merge(lfliefergruppespr);
		em.flush();
	}

	private LfliefergruppesprDto assembleLfliefergruppesprDto(Lfliefergruppespr lfliefergruppespr) {
		return LfliefergruppesprDtoAssembler.createDto(lfliefergruppespr);
	}

	private LfliefergruppesprDto[] assembleLfliefergruppesprDtos(Collection<?> lfliefergruppesprs) {
		List<LfliefergruppesprDto> list = new ArrayList<LfliefergruppesprDto>();
		if (lfliefergruppesprs != null) {
			Iterator<?> iterator = lfliefergruppesprs.iterator();
			while (iterator.hasNext()) {
				Lfliefergruppespr lfliefergruppespr = (Lfliefergruppespr) iterator.next();
				list.add(assembleLfliefergruppesprDto(lfliefergruppespr));
			}
		}
		LfliefergruppesprDto[] returnArray = new LfliefergruppesprDto[list.size()];
		return (LfliefergruppesprDto[]) list.toArray(returnArray);
	}

	/**
	 * Hole alle Liefergruppenbezeichnungen (C_NR) mit mindestens einen LF.
	 * 
	 * @return String[]
	 * @throws EJBExceptionLP
	 */
	public ArrayList getAllLFGroupsWithMinOneLF() throws EJBExceptionLP {

		Session sessionLFG = null;
		ArrayList<String> resultList = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			sessionLFG = factory.openSession();
			Criteria c = sessionLFG.createCriteria(FLRLFLiefergruppe.class);
			resultList = new ArrayList<String>();
			List<?> l = c.list();
			for (int i = 0; i < l.size(); i++) {
				FLRLFLiefergruppe lfg = (FLRLFLiefergruppe) l.get(i);
				if (!resultList.contains(lfg.getFlrliefergruppe().getC_nr())) {
					resultList.add(lfg.getFlrliefergruppe().getC_nr());
				}
			}
		} catch (Exception e) {

		} finally {
			if (null != sessionLFG) {
				sessionLFG.close();
			}
		}
		return resultList;
	}

	public boolean pruefeObPreiseBeiWaehrungsaenderungVorhanden(Integer lieferantIId, String waehrungCNr,
			TheClientDto theClientDto) {

		Lieferant lieferant = em.find(Lieferant.class, lieferantIId);

		if (!waehrungCNr.equals(lieferant.getWaehrungCNr())) {

			String sQuery = "SELECT count(al) FROM FLRArtikellieferant al WHERE al.lieferant_i_id=" + lieferantIId;

			sQuery += " AND al.n_einzelpreis IS NOT NULL AND al.n_einzelpreis <> 0";

			org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList alDaten = new ArrayList();
			while (resultListIterator.hasNext()) {
				Long l = (Long) resultListIterator.next();

				if (l != null && l > 0) {
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * Alle Lieferanten in einer bestimmten Liefergruppe holen.
	 * 
	 * @param iIdLiefergruppeI PK der Liefergruppe
	 * @param theClientDto     der aktuelle Benutzer
	 * @return LieferantDto[] alle Lieferanten in der Liefergruppe
	 * @throws EJBExceptionLP Ausnahme
	 */
	public LieferantDto[] lieferantFindByLiefergruppeIId(Integer iIdLiefergruppeI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdLiefergruppeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdLiefergruppeI == null"));
		}

		LieferantDto[] aLieferantDto = null;
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT l.id_comp.lieferant_i_id FROM FLRLFLiefergruppe l WHERE l.id_comp.lfliefergruppe_i_id ="
				+ iIdLiefergruppeI + " ORDER BY l.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		aLieferantDto = new LieferantDto[results.size()];
		Iterator<?> resultListIterator = results.iterator();
		int i = 0;
		while (resultListIterator.hasNext()) {

			Integer lieferantIId = (Integer) resultListIterator.next();
			aLieferantDto[i] = lieferantFindByPrimaryKey(lieferantIId, theClientDto);
			i++;

		}

		return aLieferantDto;
	}

	private LieferantDto[] findAllLieferantenByKreditorennr(LieferantDto lieferantDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<LieferantDto> list = new ArrayList<LieferantDto>();
		if (lieferantDtoI.getIKreditorenkontoAsIntegerNotiId() != null) {
			Session session = null;
			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				// Lieferanten finden
				Criteria c = session.createCriteria(FLRLieferant.class);
				// alle mit diesem Kreditorenkonto
				c.createCriteria(LieferantFac.FLR_KONTO).add(Restrictions.eq(FinanzFac.FLR_KONTO_C_NR,
						lieferantDtoI.getIKreditorenkontoAsIntegerNotiId().toString()));
				// aber nicht denselben
				c.add(Restrictions.not(Restrictions.eq(LieferantFac.FLR_LIEFERANT_I_ID, lieferantDtoI.getIId())));
				c.add(Restrictions.eq(LieferantFac.FLR_LIEFERANT_MANDANT_C_NR, theClientDto.getMandant()));
				// query ausfuehren
				List<?> resultList = c.list();
				for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
					FLRLieferant item = (FLRLieferant) iter.next();
					list.add(lieferantFindByPrimaryKey(item.getI_id(), theClientDto));
				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
		LieferantDto[] returnArray = new LieferantDto[list.size()];
		return (LieferantDto[]) list.toArray(returnArray);
	}

	public LieferantDto lieferantFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI, String cNrMandantI,
			TheClientDto theClientDto) {

		// precondition
		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNrMandantI == null"));
		}
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		LieferantDto lieferantDto = null;
		try {
			// Lieferant.
			Query query = em.createNamedQuery("LieferantfindByiIdPartnercNrMandant");
			query.setParameter(1, iIdPartnerI);
			query.setParameter(2, cNrMandantI);
			Lieferant lieferant = (Lieferant) query.getSingleResult();
			lieferantDto = assembleLieferantDto(lieferant);
		}
		// //exccatch: hier ist die Catchkaskade
		catch (NoResultException ex) {
			// nothing here
		}
		return lieferantDto;
	}

	public LieferantDto[] lieferantFindByRechnungsadresseiIdPartnercNrMandantOhneExc(
			Integer iIdRechnungsadressePartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		// precondition
		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNrMandantI == null"));
		}
		if (iIdRechnungsadressePartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdRechnungsadressePartnerI == null"));
		}

		LieferantDto[] lieferantDtos = null;

		// try {
		// Lieferant.
		Query query = em.createNamedQuery("LieferantfindByRechnungsadresseiIdPartnercNrMandant");
		query.setParameter(1, iIdRechnungsadressePartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		lieferantDtos = assembleLieferantDtos(cl);
		// }
		// //exccatch: hier ist die Catchkaskade
		// catch (FinderException ex) {
		// nothing here
		// }

		return lieferantDtos;
	}

	public LieferantDto[] lieferantFindByRechnungsadresseiIdPartnercNrMandant(Integer iIdRechnungsadressePartnerI,
			String cNrMandantI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNrMandantI == null"));
		}
		if (iIdRechnungsadressePartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdRechnungsadressePartnerI == null"));
		}

		LieferantDto[] lieferantDtos = null;

		// try {
		// Lieferant.
		Query query = em.createNamedQuery("LieferantfindByRechnungsadresseiIdPartnercNrMandant");
		query.setParameter(1, iIdRechnungsadressePartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		lieferantDtos = assembleLieferantDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return lieferantDtos;
	}

	public LieferantDto lieferantFindByCKundennrcNrMandant(String cKundennr, String cNrMandantI,
			TheClientDto theClientDto) {

		LieferantDto[] lieferantDtos = null;

		Query query = em.createNamedQuery("LieferantfindBycKundennrcNrMandant");
		query.setParameter(1, cKundennr);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		lieferantDtos = assembleLieferantDtos(cl);

		if (lieferantDtos.length > 0) {
			return lieferantDtos[0];
		} else {
			return null;
		}

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(10000)
	public void pflegeEKpreise(Integer lieferantIId, Integer artikelgruppeIId, Date tGueltigab, BigDecimal nProzent,
			TheClientDto theClientDto) {
		if (tGueltigab == null || nProzent == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("tGueltigab == null || nProzent == null"));
		}
		tGueltigab = Helper.cutDate(tGueltigab);
		int nachkommastellen = getUINachkommastellenPreisEK(theClientDto.getMandant());

		Date dGestern = Helper.addiereTageZuDatum(tGueltigab, -1);

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT distinct al.artikel_i_id, al.gebinde_i_id FROM FLRArtikellieferant al WHERE al.t_preisgueltigab<='"
				+ Helper.formatDateWithSlashes(tGueltigab)
				+ "' AND (al.t_preisgueltigbis IS NULL OR al.t_preisgueltigbis >= '"
				+ Helper.formatDateWithSlashes(tGueltigab) + "') AND al.lieferant_i_id=" + lieferantIId;

		if (artikelgruppeIId != null) {
			queryString += " AND al.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId;
		}

		queryString += " ORDER  BY al.artikel_i_id, al.gebinde_i_id";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		int x = 0;
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			Integer artikelIId = (Integer) o[0];
			Integer gebindeIId = (Integer) o[1];

			x++;
			System.out.println(x + " von " + results.size());

			ArtikellieferantDto dto = getArtikelFac()
					.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(artikelIId, lieferantIId,
							tGueltigab, gebindeIId, theClientDto);

			if (dto != null) {

				Integer artikellieferantIIdAlt = dto.getIId();

				// Wenns fuer den gleichen Tag schon einen Preis gibt, dann auslassen!!!

				if (Helper.cutDate(dto.getTPreisgueltigab()).before(tGueltigab)) {
					//

					dto.setIId(null);
					dto.setTPreisgueltigab(new Timestamp(tGueltigab.getTime()));
					dto.setTPreisgueltigbis(null);

					if (dto.getNEinzelpreis() != null && dto.getNNettopreis() != null) {
						// Einzelpreis und Nettopreis um x-Prozent erhoehen
						BigDecimal erhoehungEinzelpreis = Helper.getProzentWert(dto.getNEinzelpreis(), nProzent, 4);

						BigDecimal erhoehungNettopreis = Helper.getProzentWert(dto.getNNettopreis(), nProzent, 4);

						dto.setNEinzelpreis(dto.getNEinzelpreis().add(erhoehungEinzelpreis));
						dto.setNNettopreis(dto.getNNettopreis().add(erhoehungNettopreis));

					}

					try {
						Integer artikellieferantIIdNeu = getArtikelFac().createArtikellieferant(dto, theClientDto);

						Session sessionStaffel = FLRSessionFactory.getFactory().openSession();
						String queryStringStaffel = "SELECT distinct st.n_menge FROM FLRArtikellieferantstaffel st WHERE st.t_preisgueltigab<='"
								+ Helper.formatDateWithSlashes(tGueltigab)
								+ "' AND (st.t_preisgueltigbis IS NULL OR st.t_preisgueltigbis >= '"
								+ Helper.formatDateWithSlashes(tGueltigab) + "') AND st.artikellieferant_i_id="
								+ artikellieferantIIdAlt;

						org.hibernate.Query queryStaffel = sessionStaffel.createQuery(queryStringStaffel);

						List<?> resultsStaffel = queryStaffel.list();
						Iterator<?> resultListIteratorStaffel = resultsStaffel.iterator();

						while (resultListIteratorStaffel.hasNext()) {

							BigDecimal nMenge = (BigDecimal) resultListIteratorStaffel.next();

							ArtikellieferantstaffelDto[] staffeln = getArtikelFac()
									.artikellieferantstaffelFindByArtikellieferantIIdFMenge(artikellieferantIIdAlt, nMenge,
											tGueltigab);

							if (staffeln.length > 0) {

								ArtikellieferantstaffelDto staffel = staffeln[0];
								staffel.setIId(null);
								staffel.setArtikellieferantIId(artikellieferantIIdNeu);

								staffel.setTPreisgueltigab(new Timestamp(tGueltigab.getTime()));
								staffel.setTPreisgueltigbis(null);

								BigDecimal erhoehungNettopreis = Helper.getProzentWert(staffel.getNNettopreis(), nProzent,
										4);
								staffel.setNNettopreis(staffel.getNNettopreis().add(erhoehungNettopreis));
								getArtikelFac().createArtikellieferantstaffel(staffel, theClientDto);
							}

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					// und auch Staffeln eintragen
				}

			}

		}

	}

	/**
	 * Ein Kreditorenkonto fuer einen Lieferanten automatisch erstellen. Anhand der
	 * definierten Nummernkreise bzw. der in der FiBu hinterlegten Regeln.
	 * 
	 * @param lieferantIId       Integer
	 * @param nichtErstellen
	 * @param kontonummerVorgabe String
	 * @param theClientDto
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	public KontoDto createKreditorenkontoZuLieferantenAutomatisch(Integer lieferantIId, boolean nichtErstellen,
			String kontonummerVorgabe, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// Kunde holen
			LieferantDto lieferantDto = lieferantFindByPrimaryKey(lieferantIId, theClientDto);
			// Konto anlegen
			KontoDto kontoDto = getFinanzFac().createKontoFuerPartnerAutomatisch(lieferantDto.getPartnerDto(),
					FinanzServiceFac.KONTOTYP_KREDITOR, nichtErstellen, kontonummerVorgabe, theClientDto);
			// Konto beim Kunden anhaengen
			lieferantDto.setKontoIIdKreditorenkonto(kontoDto.getIId());
			lieferantDto.setUpdateModeKreditorenkonto(LieferantDto.I_UPD_KREDITORENKONTO_KEIN_UPDATE);
			updateLieferant(lieferantDto, theClientDto);
			return kontoDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public LieferantDto[] lieferantFindByPartnerIId(Integer iIdPartnerI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		LieferantDto[] lieferantDtos = null;

		// try {
		Query query = em.createNamedQuery("LieferantfindByPartnerIId");
		query.setParameter(1, iIdPartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		lieferantDtos = assembleLieferantDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return lieferantDtos;
	}

	public LieferantDto[] lieferantfindByKontoIIdKreditorenkonto(Integer kontoIId) {
		Query query = em.createNamedQuery("LieferantfindByKontoIIdKreditorenkonto");
		query.setParameter(1, kontoIId);
		Collection<?> cl = query.getResultList();

		LieferantDto[] lieferantDto = assembleLieferantDtos(cl);
		return lieferantDto;
	}

	public LieferantDto[] lieferantFindByPartnerIIdOhneExc(Integer iIdPartnerI, TheClientDto theClientDto) {

		// precondition
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI == null"));
		}

		LieferantDto[] lieferantDtos = null;

		Query query = em.createNamedQuery("LieferantfindByPartnerIId");
		query.setParameter(1, iIdPartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		lieferantDtos = assembleLieferantDtos(cl);

		return lieferantDtos;
	}

	public Integer createLieferantbeurteilung(LieferantbeurteilungDto beurteilungDto, TheClientDto theClientDto) {
		if (beurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("beurteilungDto == null"));
		}
		if (beurteilungDto.getBGesperrt() == null || beurteilungDto.getLieferantIId() == null
				|| beurteilungDto.getIPunkte() == null || beurteilungDto.getTDatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"beurteilungDto.getBGesperrt() == null || beurteilungDto.getLieferantIId() == null || beurteilungDto.getIPunkte() == null || beurteilungDto.getTDatum() == null"));
		}

		try {
			Query query = em.createNamedQuery("LieferantbeurteilungfindByLieferantIIdTDatum");
			query.setParameter(1, beurteilungDto.getLieferantIId());
			query.setParameter(2, beurteilungDto.getTDatum());
			Lieferantbeurteilung doppelt = (Lieferantbeurteilung) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_LIEFERANTBEURTEILUNG.CNR"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LIEFERANTBEURTEILUNG);
			beurteilungDto.setIId(pk);

			beurteilungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			beurteilungDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

			Lieferantbeurteilung beurteilung = new Lieferantbeurteilung(beurteilungDto.getIId(),
					beurteilungDto.getLieferantIId(), beurteilungDto.getTDatum(), beurteilungDto.getBGesperrt(),
					beurteilungDto.getIPunkte(), beurteilungDto.getPersonalIIdAendern(), beurteilungDto.getTAendern(),
					beurteilungDto.getBManuellgeaendert(), beurteilungDto.getCKlasse());
			em.persist(beurteilung);
			em.flush();
			setLieferantbeurteilungFromLieferantbeurteilungDto(beurteilung, beurteilungDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return beurteilungDto.getIId();

	}

	public void removeLieferantbeurteilung(LieferantbeurteilungDto lieferantbeurteilungDto) {
		if (lieferantbeurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferantbeurteilungDto == null"));
		}
		if (lieferantbeurteilungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantbeurteilungDto.getIId() == null"));
		}
		Integer iId = lieferantbeurteilungDto.getIId();
		Lieferantbeurteilung toRemove = em.find(Lieferantbeurteilung.class, iId);
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

	public Integer updateLieferantbeurteilung(Integer lieferantIId, Integer iPunkte, java.util.Date tDatum,
			String klasse, TheClientDto theClientDto) {

		// PJ20889

		try {
			Query query = em.createNamedQuery("LieferantbeurteilungfindByLieferantIIdTDatum");
			query.setParameter(1, lieferantIId);
			query.setParameter(2, new Timestamp(Helper.cutDate(tDatum).getTime()));
			Lieferantbeurteilung lbu = ((Lieferantbeurteilung) query.getSingleResult());

			lbu.setIPunkte(iPunkte);
			if (klasse != null) {
				lbu.setCKlasse(klasse);
			}
			return lbu.getIId();
		} catch (NoResultException ex) {
			// Dann Neu anlegen
			LieferantbeurteilungDto lbuDto = new LieferantbeurteilungDto();

			lbuDto.setLieferantIId(lieferantIId);
			lbuDto.setIPunkte(iPunkte);
			lbuDto.setBGesperrt(Helper.boolean2Short(false));
			lbuDto.setBManuellgeaendert(Helper.boolean2Short(false));
			lbuDto.setTDatum(new Timestamp(Helper.cutDate(tDatum).getTime()));
			if (klasse != null) {
				lbuDto.setCKlasse(klasse);
			}

			return createLieferantbeurteilung(lbuDto, theClientDto);

		}

	}

	public void updateLieferantbeurteilung(LieferantbeurteilungDto beurteilungDto, TheClientDto theClientDto) {
		if (beurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("beurteilungDto == null"));
		}
		if (beurteilungDto.getIId() == null || beurteilungDto.getBGesperrt() == null
				|| beurteilungDto.getLieferantIId() == null || beurteilungDto.getIPunkte() == null
				|| beurteilungDto.getTDatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"beurteilungDto.getIId() == null || beurteilungDto.getBGesperrt() == null || beurteilungDto.getLieferantIId() == null || beurteilungDto.getIPunkte() == null || beurteilungDto.getTDatum() == null"));
		}

		Integer iId = beurteilungDto.getIId();
		// try {
		Lieferantbeurteilung beurteilung = em.find(Lieferantbeurteilung.class, iId);
		if (beurteilung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("LieferantbeurteilungfindByLieferantIIdTDatum");
			query.setParameter(1, beurteilungDto.getLieferantIId());
			query.setParameter(2, beurteilungDto.getTDatum());
			Integer iIdVorhanden = ((Lieferantbeurteilung) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_LIEFERANTBEURTEILUNG.CNR"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		beurteilungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		beurteilungDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		setLieferantbeurteilungFromLieferantbeurteilungDto(beurteilung, beurteilungDto);

	}

	public LieferantbeurteilungDto lieferantbeurteilungfindByLieferantIIdTDatum(Integer lieferantIId,
			Timestamp tDatum) {
		Query query = em.createNamedQuery("LieferantbeurteilungfindByLieferantIIdTDatum");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, tDatum);
		return assembleLieferantbeurteilungDto(((Lieferantbeurteilung) query.getSingleResult()));
	}

	public LieferantbeurteilungDto lieferantbeurteilungFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Lieferantbeurteilung beurteilung = em.find(Lieferantbeurteilung.class, iId);
		if (beurteilung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferantbeurteilungDto(beurteilung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public LieferantbeurteilungDto[] lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(Integer lieferantIId,
			Timestamp tBis) {

		Query query = em.createNamedQuery("LieferantbeurteilungfindByLetzteBeurteilungByLieferantIId");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, tBis);
		return assembleLieferantbeurteilungDtos(query.getResultList());

	}

	private void setLieferantbeurteilungFromLieferantbeurteilungDto(Lieferantbeurteilung beurteilung,
			LieferantbeurteilungDto beurteilungDto) {
		beurteilung.setBGesperrt(beurteilungDto.getBGesperrt());
		beurteilung.setIPunkte(beurteilungDto.getIPunkte());
		beurteilung.setLieferantIId(beurteilungDto.getLieferantIId());
		beurteilung.setTDatum(beurteilungDto.getTDatum());
		beurteilung.setTAendern(beurteilungDto.getTAendern());
		beurteilung.setPersonalIIdAendern(beurteilungDto.getPersonalIIdAendern());
		beurteilung.setCKommentar(beurteilungDto.getCKommentar());
		beurteilung.setCKlasse(beurteilungDto.getCKlasse());
		beurteilung.setBManuellgeaendert(beurteilungDto.getBManuellgeaendert());
		em.merge(beurteilung);
		em.flush();
	}

	private LieferantbeurteilungDto assembleLieferantbeurteilungDto(Lieferantbeurteilung lieferantbeurteilung) {
		return LieferantbeurteilungDtoAssembler.createDto(lieferantbeurteilung);
	}

	private LieferantbeurteilungDto[] assembleLieferantbeurteilungDtos(Collection<?> lieferantbeurteilungs) {
		List<LieferantbeurteilungDto> list = new ArrayList<LieferantbeurteilungDto>();
		if (lieferantbeurteilungs != null) {
			Iterator<?> iterator = lieferantbeurteilungs.iterator();
			while (iterator.hasNext()) {
				Lieferantbeurteilung beurteilung = (Lieferantbeurteilung) iterator.next();
				list.add(assembleLieferantbeurteilungDto(beurteilung));
			}
		}
		LieferantbeurteilungDto[] returnArray = new LieferantbeurteilungDto[list.size()];
		return (LieferantbeurteilungDto[]) list.toArray(returnArray);
	}

	@Override
	public String importiereArtikellieferant(Integer lieferantIId, List<String[]> daten, boolean bImportieren,
			TheClientDto theClientDto) {

		final int MAX_ERROR = 30;

		String mandantCNr = theClientDto.getMandant();
		Integer personalId = theClientDto.getIDPersonal();

		StringBuffer err = new StringBuffer();
		ArrayList<Integer> artids = new ArrayList<Integer>(daten.size() + 1); // cache
																				// fuer
																				// artikel
																				// ids
		artids.add(null); // header
		int errcnt = 0;

		for (int i = 1; i < daten.size(); i++) { // bei 1 beginnen, da erste
													// Zeile Header
			artids.add(null);
			if (daten.get(i)[ArtikellieferantImportDto._CSV_IDENT] == null
					|| daten.get(i)[ArtikellieferantImportDto._CSV_IDENT].trim().length() == 0) {
				Query query = em.createNamedQuery("ArtikellieferantfindByCArtikelnrlieferant");
				query.setParameter(1, daten.get(i)[ArtikellieferantImportDto._CSV_LIEFERANTENARTIKELNUMMER]);

				Collection c = query.getResultList();

				Iterator it = c.iterator();

				Integer artikelIId = null;
				while (it.hasNext()) {
					Artikellieferant al = (Artikellieferant) c.iterator().next();

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(al.getArtikelIId(), theClientDto);

					if (aDto.getMandantCNr().equals(mandantCNr)) {
						artikelIId = al.getArtikelIId();
						break;
					}
				}

				if (artikelIId != null) {
					artids.set(i, artikelIId);
					ArtikellieferantImportDto aiDto = new ArtikellieferantImportDto(artikelIId, lieferantIId,
							mandantCNr, personalId, daten.get(i), i);
					if (aiDto.isError()) {
						err.append(aiDto.getErrors());
						errcnt++;
					}
				} else {
					err.append("Zeile:" + i + " Fuer Lieferantenartikelnummer "
							+ daten.get(i)[ArtikellieferantImportDto._CSV_LIEFERANTENARTIKELNUMMER]
							+ " konnte kein Artikel gefunden werden\r\n");
					errcnt++;
				}

			} else {
				Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
				query.setParameter(1, daten.get(i)[ArtikellieferantImportDto._CSV_IDENT]);

				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
					query.setParameter(2, getSystemFac().getHauptmandant());
				} else {
					query.setParameter(2, mandantCNr);
				}

				Artikel artikel;

				try {
					artikel = (Artikel) query.getSingleResult();
					artids.set(i, artikel.getIId());
					ArtikellieferantImportDto aiDto = new ArtikellieferantImportDto(artikel.getIId(), lieferantIId,
							mandantCNr, personalId, daten.get(i), i);
					if (aiDto.isError()) {
						err.append(aiDto.getErrors());
						errcnt++;
					}
				} catch (NoResultException e) {
					err.append("Zeile:" + i + " Artikel nicht vorhanden\r\n");
					errcnt++;
				}
			}

			if (errcnt >= MAX_ERROR) {
				err.append("...\r\n");
				break;
			}
		}
		if (err.length() == 0 && bImportieren == true) {
			for (int i = 1; i < daten.size(); i++) {
				if (artids.get(i) != null) {
					ArtikellieferantImportDto aiDto = new ArtikellieferantImportDto(artids.get(i), lieferantIId,
							mandantCNr, personalId, daten.get(i), i);
					ArtikellieferantDto ax = null;
					Integer alId = null;
					try {
						ax = getArtikelFac().getArtikelEinkaufspreis(aiDto.getArtikelIId(), aiDto.getLieferantIId(),
								BigDecimal.ONE, theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(System.currentTimeMillis()), theClientDto);
					} catch (EJBExceptionLP e1) {
						//
					}

					// PJ 17365

					if (aiDto.vkPreisbasis != null && aiDto.vkPreisbasisGuelitgab != null) {

						Query query = em.createNamedQuery(
								"VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
						query.setParameter(1, theClientDto.getMandant());
						query.setParameter(2, artids.get(i));
						query.setParameter(3, aiDto.vkPreisbasisGuelitgab);

						try {
							VkPreisfindungEinzelverkaufspreis preis = (VkPreisfindungEinzelverkaufspreis) query
									.getSingleResult();
							preis.setNVerkaufspreisbasis(aiDto.vkPreisbasis);
						} catch (NoResultException ex) {
							VkPreisfindungEinzelverkaufspreisDto preisNew = new VkPreisfindungEinzelverkaufspreisDto();
							preisNew.setArtikelIId(artids.get(i));
							preisNew.setTVerkaufspreisbasisgueltigab(new Date(aiDto.vkPreisbasisGuelitgab.getTime()));
							preisNew.setMandantCNr(theClientDto.getMandant());
							preisNew.setNVerkaufspreisbasis(aiDto.vkPreisbasis);
							try {
								getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(preisNew, theClientDto);
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}
						}

					}

					try {
						if (ax != null) {
							aiDto.setISort(ax.getISort());
							// getArtikelFac().removeArtikellieferant(ax);
							aiDto.setIId(ax.getIId());
							aiDto.setIWiederbeschaffungszeit(ax.getIWiederbeschaffungszeit());
							// SP7990
							aiDto.setEinheitCNrVpe(ax.getEinheitCNrVpe());
							getArtikelFac().updateArtikellieferant(aiDto, theClientDto);
							alId = aiDto.getIId();
						} else {
							Query q = em.createNamedQuery("ArtikellieferantejbSelectNextReihung");
							q.setParameter(1, aiDto.getArtikelIId());
							Integer isort = (Integer) q.getSingleResult();
							if (isort == null)
								isort = new Integer(1);
							else
								isort++;
							aiDto.setISort(isort);
							alId = getArtikelFac().createArtikellieferant(aiDto, theClientDto);
						}

						if (aiDto.getArtikellieferantstaffelDto() != null) {
							ArtikellieferantstaffelDto alStaffelDto = aiDto.getArtikellieferantstaffelDto();

							Query q = em.createNamedQuery(
									"ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
							q.setParameter(1, alId);
							q.setParameter(2, alStaffelDto.getNMenge());
							q.setParameter(3, alStaffelDto.getTPreisgueltigab());
							Artikellieferantstaffel axStaffel = null;
							try {
								axStaffel = (Artikellieferantstaffel) q.getSingleResult();
							} catch (NoResultException e) {
								//
							}
							if (axStaffel != null) {
								alStaffelDto.setIWiederbeschaffungszeit(axStaffel.getIWiederbeschaffungszeit());
								getArtikelFac().removeArtikellieferantstaffel(
										ArtikellieferantstaffelDtoAssembler.createDto(axStaffel));
							}
							alStaffelDto.setArtikellieferantIId(alId);
							alStaffelDto.setBRabattbehalten((short) 0);
							getArtikelFac().createArtikellieferantstaffel(alStaffelDto, theClientDto);
						}
					} catch (EJBExceptionLP e) {
						err.append("Zeile:" + i + " " + e.getMessage());
					} catch (RemoteException e) {
						err.append("Zeile:" + i + " " + e.getMessage());
					}
				}
			}
		}
		return err.toString();
	}

	@Override
	public List<LieferantDto> lieferantFindByMandantCnr(TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "mandantCnr");

		List<Lieferant> lieferanten = LieferantQuery.listByMandantCnr(em, theClientDto.getMandant());
		List<LieferantDto> lieferantDtos = new ArrayList<LieferantDto>(lieferanten.size());
		for (Lieferant lieferant : lieferanten) {
			lieferantDtos.add(lieferantFindByPrimaryKey(lieferant.getIId(), theClientDto));
		}

		return lieferantDtos;
	}

	@Override
	public VendidataPartnerExportResult exportiere4VendingLieferanten(boolean checkOnly, TheClientDto theClientDto)
			throws RemoteException {
		IVendidataSuppliersExportBeanServices suppliersBeanServices = new VendidataSupplierExportBeanServices(
				getLieferantFac(), getSystemFac(), theClientDto);
		VendidataSuppliersExportTransformer exporter = new VendidataSuppliersExportTransformer(suppliersBeanServices);

		if (checkOnly) {
			VendidataXmlSupplierTransformResult exportXmlResult = exporter.checkExportSuppliers();
			return new VendidataPartnerExportResult(exportXmlResult.getExportErrors(),
					exportXmlResult.getExportStats());
		}

		try {
			VendidataXmlSupplierTransformResult exportXmlResult = exporter.exportSuppliers();
			VendidataSuppliersMarshaller marshaller = new VendidataSuppliersMarshaller();
			String xmlString = marshaller.marshal(exportXmlResult.getXmlSuppliers());
			return new VendidataPartnerExportResult(xmlString, exportXmlResult.getExportErrors(),
					exportXmlResult.getExportStats());
		} catch (JAXBException e) {
			throw new EJBExceptionLP(e);
		} catch (SAXException e) {
			throw new EJBExceptionLP(e);
		}
	}

	@Override
	public Integer generiere4VendingSupplierId(Integer lieferantIId, TheClientDto theClientDto) throws RemoteException {
		Validator.pkFieldNotNull(lieferantIId, "lieferantIId");
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer highestSupplierId = pkGen.getLastPrimaryKey(PKConst.PK_4VENDINGSUPPLIERID);
		if (new Integer(0).equals(highestSupplierId)) {
			highestSupplierId = LieferantQuery.resultMaxFremdsystemnr(em);
			pkGen.createSequenceIfNotExists(PKConst.PK_4VENDINGSUPPLIERID, highestSupplierId);
		}
		Integer nextPk = pkGen.getNextPrimaryKey(PKConst.PK_4VENDINGSUPPLIERID);
		Lieferant liefNextPk = LieferantQuery.resultByMandantCNrFremdsystemnr(em, theClientDto.getMandant(),
				nextPk.toString());
		if (liefNextPk != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "Generierte 4Vending-Supplier-Id bereits vorhanden!");
		}

		Lieferant lieferant = em.find(Lieferant.class, lieferantIId);
		lieferant.setCFremdsystemnr(nextPk.toString());
		em.merge(lieferant);
		em.flush();

		return nextPk;
	}

	private static final List<Integer> LieferantWebabfrageTypen = Arrays.asList(AngebotstklFac.WebabfrageTyp.FARNELL);

	@Override
	public Integer createWeblieferant(IWebpartnerDto dto, TheClientDto theClientDto) {
		List<Webpartner> entities = WebpartnerQuery.listByLieferantIdWebabfrageTypIIds(em, dto.getLieferantIId(),
				LieferantWebabfrageTypen);
		for (Webpartner webpartner : entities) {
			removeWeblieferant(webpartner.getIId());
		}
		return getAngebotstklFac().createWebpartner(dto, theClientDto);
	}

	@Override
	public void updateWeblieferant(IWebpartnerDto dto) {
		getAngebotstklFac().updateWebpartner(dto);
	}

	@Override
	public void removeWeblieferant(Integer iId) {
		getAngebotstklFac().removeWebpartner(iId);
	}

	@Override
	public WeblieferantFarnellDto weblieferantFarnellFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(iId, "iId");

		WeblieferantFarnell entity = em.find(WeblieferantFarnell.class, iId);
		Validator.entityFound(entity, iId);

		WeblieferantFarnellDto dto = WebPartnerDtoAssembler.createDto(entity);
		return dto;
	}

	@Override
	public IWebpartnerDto weblieferantFindByLieferantIIdOhneExc(Integer lieferantIId, TheClientDto theClientDto) {
		List<Webpartner> entities = WebpartnerQuery.listByLieferantIdWebabfrageTypIIds(em, lieferantIId,
				LieferantWebabfrageTypen);
		if (entities.isEmpty())
			return null;

		Webpartner entity = em.find(Webpartner.class, entities.get(0).getIId());
		WebpartnerDto dto = WebPartnerDtoAssembler.createDto(entity);

		return dto;
	}

	@Override
	public List<WebpartnerDto> weblieferantFindByMandant(String mandantCnr) {
		List<Webpartner> entities = WebpartnerQuery.listByMandantWebabfrageTypIIds(em, mandantCnr,
				LieferantWebabfrageTypen);
		return WebPartnerDtoAssembler.createDtos(entities);
	}
}
