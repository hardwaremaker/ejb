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
package com.lp.server.system.ejbfac;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Anwender;
import com.lp.server.system.ejb.Einheit;
import com.lp.server.system.ejb.Einheitkonvertierung;
import com.lp.server.system.ejb.Einheitspr;
import com.lp.server.system.ejb.EinheitsprPK;
import com.lp.server.system.ejb.Extraliste;
import com.lp.server.system.ejb.Funktion;
import com.lp.server.system.ejb.Funktionspr;
import com.lp.server.system.ejb.Geschaeftsjahr;
import com.lp.server.system.ejb.GeschaeftsjahrMandant;
import com.lp.server.system.ejb.GeschaeftsjahrMandantQuery;
import com.lp.server.system.ejb.GeschaeftsjahrQuery;
import com.lp.server.system.ejb.Kostenstelle;
import com.lp.server.system.ejb.Land;
import com.lp.server.system.ejb.Landkfzkennzeichen;
import com.lp.server.system.ejb.Landplzort;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.ejb.OrtQuery;
import com.lp.server.system.ejb.Protokoll;
import com.lp.server.system.ejb.Versandweg;
import com.lp.server.system.ejb.VersandwegCC;
import com.lp.server.system.ejb.VersandwegCCPartner;
import com.lp.server.system.ejb.VersandwegCCPartnerQuery;
import com.lp.server.system.fastlanereader.generated.FLREinheit;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.AnwenderDtoAssembler;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.EinheitDtoAssembler;
import com.lp.server.system.service.EinheitKonvertierungDto;
import com.lp.server.system.service.EinheitKonvertierungDtoAssembler;
import com.lp.server.system.service.EinheitsprDto;
import com.lp.server.system.service.EinheitsprDtoAssembler;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.server.system.service.ExtralisteDtoAssembler;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.FunktionDtoAssembler;
import com.lp.server.system.service.FunktionsprDto;
import com.lp.server.system.service.FunktionsprDtoAssembler;
import com.lp.server.system.service.GeschaeftsjahrDto;
import com.lp.server.system.service.GeschaeftsjahrDtoAssembler;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDtoAssembler;
import com.lp.server.system.service.IVersandwegDto;
import com.lp.server.system.service.IVersandwegPartnerDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.KostenstelleDtoAssembler;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandDtoAssembler;
import com.lp.server.system.service.LandkfzkennzeichenDto;
import com.lp.server.system.service.LandkfzkennzeichenDtoAssembler;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LandplzortDtoAssembler;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.OrtDtoAssembler;
import com.lp.server.system.service.PingPacket;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.ServerJavaAndOSInfo;
import com.lp.server.system.service.ServerLocaleInfo;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandwegCCDto;
import com.lp.server.system.service.VersandwegCCPartnerDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class SystemFacBean extends Facade implements SystemFac {
	@PersistenceContext
	private EntityManager em;

	private ModelMapper mapper = null;

	protected ModelMapper getMapper() {
		if (mapper == null) {
			mapper = new ModelMapper();
		}
		return mapper;
	}

	public void erstelleProtokolleintrag(ProtokollDto protokollDto,
			TheClientDto theClientDto) {
		if (protokollDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("protokollDto == null"));
		}
		if (protokollDto.getCArt() == null || protokollDto.getCTyp() == null
				|| protokollDto.getCText() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"protokollDto.getCArt() == null || protokollDto.getCTyp() == null	|| protokollDto.getCText() == null"));
		}

		protokollDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		protokollDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));

		if (protokollDto.getCText() != null
				&& protokollDto.getCText().length() > 3000) {
			protokollDto.setCText(protokollDto.getCText().substring(0, 2999));
		}
		if (protokollDto.getCLangtext() != null
				&& protokollDto.getCLangtext().length() > 3000) {
			protokollDto.setCLangtext(protokollDto.getCLangtext().substring(0,
					2999));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PROTOKOLL);

		protokollDto.setIId(pk);

		Protokoll protokoll = new Protokoll(protokollDto.getIId(),
				protokollDto.getCArt(), protokollDto.getCTyp(),
				protokollDto.getCText(), protokollDto.getPersonalIIdAnlegen(),
				protokollDto.getTAnlegen());
		em.persist(protokoll);
		em.flush();
		setProtokollFromProtokollDto(protokoll, protokollDto);
	}

	/**
	 * Lege ein neues Land in der Datenbank an
	 * 
	 * @param landDto
	 *            LandDto
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createLand(LandDto landDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (landDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("landDto == null"));
		}
		if (landDto.getCLkz() == null || landDto.getCName() == null
				|| landDto.getILaengeuidnummer() == null
				|| landDto.getBSepa() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"landDto.getCLkz() == null || landDto.getCName() == null || landDto.getILaengeuidnummer() == null || landDto.getBSepa() == null"));
		}

		try {
			Query query = em.createNamedQuery("LandfindByLkzLandName");
			query.setParameter(1, landDto.getCLkz());
			query.setParameter(2, landDto.getCName());
			Land land = (Land) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_LAND.C_NAME"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LAND);
			landDto.setIID(pk);

			Land land = new Land(landDto.getIID(), landDto.getCLkz(),
					landDto.getCName(), landDto.getILaengeuidnummer(),
					landDto.getBSepa());
			em.persist(land);
			em.flush();

			setLandFromLandDto(land, landDto);
		} catch (EntityExistsException t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, t);
		}
		return landDto.getIID();
	}

	/**
	 * Loescht ein in der Datenbank vorhandenes Land
	 * 
	 * @param landDto
	 *            LandDto
	 * @throws EJBExceptionLP
	 *             landDto == null oder landDto.getCLkz() == null
	 */
	public void removeLand(LandDto landDto) throws EJBExceptionLP {

		myLogger.entry();
		if (landDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landDto == null"));
		}
		if (landDto.getIID() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landDto.getCLkz() == null"));
		}
		Land toRemove = em.find(Land.class, landDto.getIID());
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

	public String getHauptmandant() {
		Anwender anwender = em.find(Anwender.class,
				SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);
		return anwender.getMandantCNrHauptmandant();
	}

	/**
	 * Aktualisiert ein in der Datenbank vorhandenes Land
	 * 
	 * @param landDto
	 *            LandDto
	 * @throws EJBExceptionLP
	 */
	public void updateLand(LandDto landDto) throws EJBExceptionLP {

		myLogger.entry();

		if (landDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("landDto == null"));
		}
		if (landDto.getIID() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landDto.getIID() == null"));
		}
		if (landDto.getCLkz() == null || landDto.getCName() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"landDto.getCLkz() == null || landDto.getCName() == null"));
		}

		Land oLand = null;

		try {
			Query query = em.createNamedQuery("LandfindByLkzLandName");
			query.setParameter(1, landDto.getCLkz());
			query.setParameter(2, landDto.getCName());
			// @todo getSingleResult oder getResultList ?
			oLand = (Land) query.getSingleResult();

			// loeschen von Date
			if (oLand.getTEumitglied() != null
					&& landDto.getEUMitglied() == null) {
				// do nothing
			}

			// anlegen von Date
			else if (oLand.getTEumitglied() == null
					&& landDto.getEUMitglied() != null) {
				// do nothing
			}

			/*
			 * else { throw new EJBExceptionLP(
			 * EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new
			 * Exception("LP_LAND.C_NAME")); }
			 */
		} catch (NoResultException ex1) {
			// nothing here
		}

		oLand = em.find(Land.class, landDto.getIID());
		if (oLand == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLandFromLandDto(oLand, landDto);
	}

	public LandDto landFindByPrimaryKey(Integer iIdI) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdI == null"));
		}
		Land land = em.find(Land.class, iIdI);
		if (land == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLandDto(land);
	}

	public Integer getPartnerLandIId(Integer partnerIId) {
		Partner partner = em.find(Partner.class, partnerIId);
		if (partner != null) {
			Landplzort lplzo = em.find(Landplzort.class,
					partner.getLandplzortIId());
			if (lplzo != null)
				return lplzo.getLandIId();
		}
		return null;
	}

	public LandDto landFindByLkz(String cLkz) throws EJBExceptionLP {

		myLogger.entry();

		if (cLkz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cLkz == null"));
		}
		try {
			Query query = em.createNamedQuery("LandfindByLkz");
			query.setParameter(1, cLkz);
			Land land = (Land) query.getSingleResult();
			return assembleLandDto(land);

		} catch (NoResultException ex) {
			return null;
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	private void setLandFromLandDto(Land land, LandDto landDto) {
		land.setCLkz(landDto.getCLkz());
		land.setCName(landDto.getCName());
		land.setCTelvorwahl(landDto.getCTelvorwahl());
		land.setWaehrungCNr(landDto.getWaehrungCNr());
		land.setILaengeuidnummer(landDto.getILaengeuidnummer());
		land.setTEumitglied(landDto.getEUMitglied());
		land.setNUidnummerpruefenabbetrag(landDto
				.getNUidnummerpruefenabbetrag());
		land.setBSepa(landDto.getBSepa());
		land.setFGmtversatz(landDto.getFGmtversatz());
		land.setNMuenzRundung(landDto.getNMuenzRundung());
		land.setLandIIdGemeinsamespostland(landDto
				.getLandIIdGemeinsamespostland());
		em.merge(land);
		em.flush();
	}

	private LandDto assembleLandDto(Land land) {
		return LandDtoAssembler.createDto(land);
	}

	private LandDto[] assembleLandDtos(Collection<?> lands) {
		List<LandDto> list = new ArrayList<LandDto>();
		if (lands != null) {
			Iterator<?> iterator = lands.iterator();
			while (iterator.hasNext()) {
				Land land = (Land) iterator.next();
				list.add(assembleLandDto(land));
			}
		}
		LandDto[] returnArray = new LandDto[list.size()];
		return (LandDto[]) list.toArray(returnArray);
	}

	public LandplzortDto landplzortFindByLandOrtPlzOhneExc(String lkz,
			String ort, String plz) throws RemoteException {
		Validator.notEmpty(lkz, "lkz == null");
		Validator.notEmpty(ort, "ort == null");
		Validator.notNull(plz, "plz");

		try {
			LandDto landDto = landFindByLkz(lkz);
			if (null == landDto)
				return null;

			OrtDto ortDto = ortFindByNameOhneExc(ort);
			if (null == ortDto)
				return null;

			Query query = em
					.createNamedQuery("LandplzortfindByLandIIdByCPlzByOrtIId");
			query.setParameter(1, landDto.getIID());
			query.setParameter(2, plz);
			query.setParameter(3, ortDto.getIId());

			Landplzort landplzort = (Landplzort) query.getSingleResult();
			return assembleLandplzortDto(landplzort);
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex);
		}

		return null;
	}

	public LandplzortDto landplzortFindByLandOrtPlzOhneExc(Integer landIId,
			Integer ortIId, String plz) throws RemoteException {
		Validator.pkFieldNotNull(landIId, "landIId == null");
		Validator.pkFieldNotNull(ortIId, "ortIId == null");
		Validator.notNull(plz, "plz");

		try {
			Query query = em
					.createNamedQuery("LandplzortfindByLandIIdByCPlzByOrtIId");
			query.setParameter(1, landIId);
			query.setParameter(2, plz);
			query.setParameter(3, ortIId);

			Landplzort landplzort = (Landplzort) query.getSingleResult();
			return assembleLandplzortDto(landplzort);
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex);
		}

		return null;
	}

	public Integer createLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		myLogger.logData(landplzortDto, theClientDto.getIDUser());

		if (landplzortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("landplzortDto == null"));
		}
		if (landplzortDto.getCPlz() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("landplzortDto.getCPlz() == null"));
		}
		if (landplzortDto.getLandDto() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("landplzortDto.getLandDto() == null"));
		}
		// Auf doppelten Eintrag pruefen
		try {
			Query query = em
					.createNamedQuery("LandplzortfindByLandIIdByCPlzByOrtIId");
			query.setParameter(1, landplzortDto.getIlandID());
			query.setParameter(2, landplzortDto.getCPlz());
			query.setParameter(3, landplzortDto.getOrtIId());
			Landplzort landplzort = (Landplzort) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_LANDPLZORT.UK"));
		} catch (NoResultException e1) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LANDPLZORT);
			landplzortDto.setIId(pk);

			// Pruefen, ob der Ort schon angelegt wurde (kann am client direkt
			// eingegeben werden)
			try {
				Query query = em.createNamedQuery("OrtfindByCName");
				query.setParameter(1, landplzortDto.getOrtDto().getCName());
				Ort ort = (Ort) query.getSingleResult();
				landplzortDto.setOrtIId(ort.getIId());
			} catch (NoResultException e) {
				// wenn es den noch nicht gibt -> neu anlegen
				OrtDto ortDto = landplzortDto.getOrtDto();
				ortDto.setIId(null);
				Integer ortIId = createOrt(ortDto, theClientDto);
				landplzortDto.setOrtIId(ortIId);
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
			// Land PLZ Ort anlegen
			Landplzort landplzort = new Landplzort(landplzortDto.getIId(),
					landplzortDto.getCPlz(), landplzortDto.getOrtIId(),
					landplzortDto.getIlandID());
			em.persist(landplzort);
			em.flush();
			setLandplzortFromLandplzortDto(landplzort, landplzortDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return landplzortDto.getIId();
	}

	public void removeLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();

		if (landplzortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landplzortDto == null"));
		}
		if (landplzortDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landplzortDto.getIId() == null"));
		}
		Landplzort toRemove = em.find(Landplzort.class, landplzortDto.getIId());
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

	public void updateLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(landplzortDto, theClientDto.getIDUser());
		if (landplzortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("landplzortDto == null"));
		}
		if (landplzortDto.getCPlz() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("landplzortDto.getCPlz() == null"));
		}
		if (landplzortDto.getIlandID() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("landplzortDto.getIlandID() == null"));
		}
		if (landplzortDto.getOrtIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("landplzortDto.getOrtIId() == null"));
		}
		try {
			// Pruefen, ob der Ort schon angelegt wurde (kann am client direkt
			// eingegeben werden)
			Query query = em.createNamedQuery("OrtfindByCName");
			query.setParameter(1, landplzortDto.getOrtDto().getCName());
			Ort ort = (Ort) query.getSingleResult();
		} catch (NoResultException e) {
			// wenn es den noch nicht gibt -> neu anlegen
			OrtDto ortDto = landplzortDto.getOrtDto();
			ortDto.setIId(null);
			Integer ortIId = createOrt(ortDto, theClientDto);
			landplzortDto.setOrtIId(ortIId);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		Integer iId = landplzortDto.getIId();
		Landplzort landplzort = em.find(Landplzort.class, iId);
		if (landplzort == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLandplzortFromLandplzortDto(landplzort, landplzortDto);
	}

	public LandplzortDto landplzortFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Landplzort landplzort = em.find(Landplzort.class, iId);
		if (landplzort == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, "");
		}
		LandplzortDto landplzortDto = assembleLandplzortDto(landplzort);
		landplzortDto.setOrtDto(ortFindByPrimaryKey(landplzortDto.getOrtIId()));
		landplzortDto.setLandDto(landFindByPrimaryKey(landplzortDto
				.getIlandID()));
		return landplzortDto;
	}

	private void setLandplzortFromLandplzortDto(Landplzort landplzort,
			LandplzortDto landplzortDto) {
		landplzort.setCPlz(landplzortDto.getCPlz());
		landplzort.setOrtIId(landplzortDto.getOrtIId());
		landplzort.setLandIId(landplzortDto.getLandDto().getIID());
		em.merge(landplzort);
		em.flush();
	}

	private LandplzortDto assembleLandplzortDto(Landplzort landplzort) {
		return LandplzortDtoAssembler.createDto(landplzort);
	}

	private LandplzortDto[] assembleLandplzortDtos(Collection<?> landplzorts) {
		List<LandplzortDto> list = new ArrayList<LandplzortDto>();
		if (landplzorts != null) {
			Iterator<?> iterator = landplzorts.iterator();
			while (iterator.hasNext()) {
				Landplzort landplzort = (Landplzort) iterator.next();
				list.add(assembleLandplzortDto(landplzort));
			}
		}
		LandplzortDto[] returnArray = new LandplzortDto[list.size()];
		return (LandplzortDto[]) list.toArray(returnArray);
	}

	public Integer createOrt(OrtDto ortDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		myLogger.entry();

		if (ortDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ortDto == null"));
		}
		if (ortDtoI.getCName() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("ortDto.getCName() == null"));
		}

		try {
			Query query = em.createNamedQuery("OrtfindByCName");
			query.setParameter(1, ortDtoI.getCName());
			Ort doppelt = (Ort) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_ORT.C_NAME"));
		} catch (NoResultException e1) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ORT);
			ortDtoI.setIId(pk);

			ortDtoI.setIAendern(theClientDto.getIDPersonal());

			Ort ort = new Ort(ortDtoI.getIId(), ortDtoI.getCName(),
					ortDtoI.getIAendern());
			em.persist(ort);
			em.flush();

			setOrtFromOrtDto(ort, ortDtoI);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return ortDtoI.getIId();
	}

	public ExtralisteRueckgabeTabelleDto generiereExtraliste(
			Integer extralisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ExtralisteRueckgabeTabelleDto dto = new ExtralisteRueckgabeTabelleDto();
		// try {
		Extraliste extraliste = em.find(Extraliste.class, extralisteIId);
		if (extraliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		// Sortierung nach der Uebersetzung der Einheit
		String sQuery = extraliste.getXQuery();

		List<?> list = null;
		Class[] c = null;
		try {
			org.hibernate.Query query = session.createQuery(sQuery);

			list = query.list();
			dto.setColumnNames(query.getReturnAliases());
			c = new Class[query.getReturnTypes().length];

			for (int u = 0; u < query.getReturnTypes().length; u++) {
				c[u] = query.getReturnTypes()[u].getReturnedClass();
			}
			dto.setColumnClasses(c);

		} catch (HibernateException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
					new Exception(ex1.getMessage()));
		}

		Iterator<?> resultListIterator = list.iterator();

		int iZeile = 0;
		Object[][] o = null;
		while (resultListIterator.hasNext()) {
			Object[] oZeile = (Object[]) resultListIterator.next();
			if (iZeile == 0) {
				o = new Object[list.size()][oZeile.length];
			}

			for (int i = 0; i < oZeile.length; i++) {
				o[iZeile][i] = oZeile[i];
			}
			iZeile++;

		}
		session.close();
		dto.setData(o);
		// }
		// catch (FinderException ex) {

		// }

		return dto;
	}

	public void removeOrt(OrtDto ortDto) throws EJBExceptionLP {

		myLogger.entry();

		if (ortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("ortDto == null"));
		}
		if (ortDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("ortDto.getIId() == null"));
		}
		Ort toRemove = em.find(Ort.class, ortDto.getIId());
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

	public void updateOrt(OrtDto ortDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition.
		if (ortDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("ortDto == null"));
		}
		if (ortDtoI.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("ortDtoI.getIId()"));
		}
		ortDtoI.setIAendern(theClientDto.getIDPersonal());
		ortDtoI.setTAendern(getTimestamp());

		// suchen des Ortes
		Ort ort = em.find(Ort.class, ortDtoI.getIId());
		if (ort == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, "");
		}
		// setzen des Ortes von ortDtoI nach ort (Speicherung ausgefuehrt)
		setOrtFromOrtDto(ort, ortDtoI);
	}

	public OrtDto ortFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Ort ort = em.find(Ort.class, iId);
		if (ort == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, "");
		}
		return assembleOrtDto(ort);
	}

	private void setOrtFromOrtDto(Ort ort, OrtDto ortDto) {
		ort.setCName(ortDto.getCName());
		ort.setTAendern(new Timestamp(System.currentTimeMillis()));
		ort.setPersonalIIdAendern(ortDto.getIAendern());
		em.merge(ort);
		em.flush();
	}

	private OrtDto assembleOrtDto(Ort ort) {
		return OrtDtoAssembler.createDto(ort);
	}

	private OrtDto[] assembleOrtDtos(Collection<?> orts) {
		List<OrtDto> list = new ArrayList<OrtDto>();
		if (orts != null) {
			Iterator<?> iterator = orts.iterator();
			while (iterator.hasNext()) {
				Ort ort = (Ort) iterator.next();
				list.add(assembleOrtDto(ort));
			}
		}
		OrtDto[] returnArray = new OrtDto[list.size()];
		return (OrtDto[]) list.toArray(returnArray);
	}

	public String createEinheit(EinheitDto einheitDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (einheitDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("EinheitDtoI == null"));
		}

		// Wer legt an setzen.
		einheitDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		einheitDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		try {

			try {
				Query query = em.createNamedQuery("EinheitFindByCNr");
				query.setParameter(1, einheitDtoI.getCNr());
				Einheit einheit = (Einheit) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_EINHEIT.C_NR"));
			} catch (NoResultException ex) {
				//
			}

			Einheit einheit = new Einheit(einheitDtoI.getCNr(),
					einheitDtoI.getPersonalIIdAnlegen(),
					einheitDtoI.getPersonalIIdAendern(),
					einheitDtoI.getIDimension());
			em.persist(einheit);
			em.flush();

			if (einheitDtoI.getEinheitsprDto() != null) {
				einheitDtoI.getEinheitsprDto().setEinheitCNr(
						einheitDtoI.getCNr());
				createEinheitspr(einheitDtoI.getEinheitsprDto(), theClientDto);
			}

			// tAendern und tAnlegen werden im Bean generiert;
			// jetzt holen und setzen wegen update.
			einheitDtoI.setTAendern(einheit.getTAendern());
			einheitDtoI.setTAnlegen(einheit.getTAnlegen());

			setEinheitFromEinheitDto(einheit, einheitDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return einheitDtoI.getCNr();
	}

	public void removeEinheit(String cNr) throws EJBExceptionLP {

		Einheit toRemove = em.find(Einheit.class, cNr);
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

	public void removeEinheit(EinheitDto einheitDto) throws EJBExceptionLP {

		if (einheitDto != null) {
			if (einheitDto.getCNr().equals(SystemFac.EINHEIT_STUECK)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SYSTEM_STUECK_KANN_NICHT_GELOESCHT_WERDEN,
						new Exception("Basiseinheit STUECK nicht loeschbar"));
			}
			if (einheitDto.getCNr().equals(SystemFac.EINHEIT_MINUTE)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SYSTEM_MINUTE_KANN_NICHT_GELOESCHT_WERDEN,
						new Exception("Basiseinheit MINUTE nicht loeschbar"));
			}

			if (einheitDto.getCNr().equals(SystemFac.EINHEIT_SEKUNDE)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SYSTEM_SEKUNDE_KANN_NICHT_GELOESCHT_WERDEN,
						new Exception("Basiseinheit SEKUNDE nicht loeschbar"));
			}

			if (einheitDto.getCNr().equals(SystemFac.EINHEIT_STUNDE)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SYSTEM_STUNE_KANN_NICHT_GELOESCHT_WERDEN,
						new Exception("Basiseinheit STUNDE nicht loeschbar"));
			}

			String cNr = einheitDto.getCNr();
			removeEinheit(cNr);
		}
	}

	public void updateEinheit(EinheitDto einheitDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (einheitDto != null) {
			String cNr = einheitDto.getCNr();
			Einheit einheit = em.find(Einheit.class, cNr);
			if (einheit == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			try {
				Query query = em.createNamedQuery("EinheitFindByCNr");
				query.setParameter(1, einheitDto.getCNr());
				// @todo getSingleResult oder getResultList ?
				String cNrVorhanden = ((Einheit) query.getSingleResult())
						.getCNr();
				if (einheit.getCNr().equals(cNrVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("LP_EINHEIT"));
				}

			} catch (NoResultException ex) {

			}

			setEinheitFromEinheitDto(einheit, einheitDto);

			if (einheitDto.getEinheitsprDto() != null) {
				// -- upd oder create
				if (einheitDto.getEinheitsprDto().getEinheitCNr() == null) {
					// create
					// Key(teil) setzen.
					einheitDto.getEinheitsprDto().setEinheitCNr(
							einheitDto.getCNr());
					createEinheitspr(einheitDto.getEinheitsprDto(),
							theClientDto);
				} else {
					// upd
					updateEinheitspr(einheitDto.getEinheitsprDto(),
							theClientDto);
				}
			}
		}
	}

	public EinheitDto einheitFindByPrimaryKey(String cNr,
			TheClientDto theClientDtoI) throws EJBExceptionLP {

		EinheitDto einheitDto = einheitFindByPrimaryKeyOhneExc(cNr,
				theClientDtoI);
		if (null == einheitDto) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return einheitDto;
		// EinheitDto einheitDto = null;
		// Einheit einheit = em.find(Einheit.class, cNr);
		// if (einheit == null) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		// }
		// einheitDto = assembleEinheitDto(einheit);
		// Einheitspr einheitspr = em.find(Einheitspr.class, new EinheitsprPK(
		// einheitDto.getCNr(), theClientDtoI.getLocUiAsString()));
		// if (einheitspr != null)
		// einheitDto.setEinheitsprDto(assembleEinheitsprDto(einheitspr));
		// return einheitDto;
	}

	public EinheitDto einheitFindByPrimaryKeyOhneExc(String cNr,
			TheClientDto theClientDtoI) throws EJBExceptionLP {

		EinheitDto einheitDto = null;
		Einheit einheit = em.find(Einheit.class, cNr);
		if (einheit == null)
			return null;

		einheitDto = assembleEinheitDto(einheit);
		Einheitspr einheitspr = em.find(Einheitspr.class, new EinheitsprPK(
				einheitDto.getCNr(), theClientDtoI.getLocUiAsString()));
		if (einheitspr != null)
			einheitDto.setEinheitsprDto(assembleEinheitsprDto(einheitspr));
		return einheitDto;
	}

	public EinheitDto[] einheitFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("EinheitfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleEinheitDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
	}

	public JasperReport getDreispalter(TheClientDto theClientDto)
			throws EJBExceptionLP {

		String reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
				SystemFac.REPORT_DREISPALTER, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);
		try {
			// Report laden
			return (JasperReport) JRLoader.loadObjectFromFile(reportdir);
		} catch (JRException ex) {
			/**
			 * @todo Fehleraufschluesselung zentral, nicht hier.
			 */
			Throwable eCause = ex.getCause();
			if (eCause instanceof FileNotFoundException) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN, ex);
			} else if (eCause instanceof InvalidClassException) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION, ex);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
			}
		}
	}

	/**
	 * Lesen aller in der DB vorhandenen Einheiten (Mengenart)
	 * 
	 * @return Map
	 * @throws EJBExceptionLP
	 * @param theClientDto
	 *            String
	 */
	public Map getAllEinheit(TheClientDto theClientDto) throws EJBExceptionLP {
		LinkedHashMap<String, String> mEinheiten = new LinkedHashMap<String, String>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Sortierung nach der Uebersetzung der Einheit
			String sQuery = "SELECT einheit,(SELECT  spr.c_bez FROM FLREinheitspr AS spr WHERE spr.einheit.c_nr=einheit.c_nr AND spr.locale.c_nr='"
					+ theClientDto.getLocUiAsString()
					+ "' ) FROM FLREinheit AS einheit ORDER BY einheit.c_nr ASC ";
			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> list = query.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {

				Object[] o = (Object[]) iter.next();
				FLREinheit eht = (FLREinheit) o[0];

				String bez = (String) o[1];

				// Wenn keine gefunden wurde
				if (bez == null) {
					mEinheiten.put(eht.getC_nr(), eht.getC_nr());
				} else {
					mEinheiten.put(eht.getC_nr(), bez);
				}

			}
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mEinheiten;
	}

	/**
	 * Lesen aller in der DB vorhandenen Geschaeftsjahre
	 * 
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map getAllGeschaeftsjahr(String mandantCnr) throws EJBExceptionLP {
		List<GeschaeftsjahrMandant> c = GeschaeftsjahrMandantQuery
				.listByMandant(em, mandantCnr);
		// Query query = em.createNamedQuery("GeschaeftsjahrfindAll");
		// c = query.getResultList();

		if (c == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, "");
		}

		GeschaeftsjahrMandantDto[] allGeschaeftsjahr = GeschaeftsjahrMandantDtoAssembler
				.createDtos(c);
		Map<Integer, Integer> content = null;
		content = new TreeMap<Integer, Integer>();
		for (int i = 0; i < allGeschaeftsjahr.length; i++) {
			GeschaeftsjahrMandantDto geschaeftsjahr = allGeschaeftsjahr[i];
			Integer key = geschaeftsjahr.getIGeschaeftsjahr();
			Integer value = geschaeftsjahr.getIGeschaeftsjahr();
			content.put(key, value);
		}
		return content;
	}

	private void setEinheitFromEinheitDto(Einheit einheit, EinheitDto einheitDto) {
		einheit.setTAnlegen(einheitDto.getTAnlegen());
		einheit.setPersonalIIdAnlegen(einheitDto.getPersonalIIdAnlegen());
		einheit.setTAendern(einheitDto.getTAendern());
		einheit.setPersonalIIdAendern(einheitDto.getPersonalIIdAendern());
		einheit.setIDimension(einheitDto.getIDimension());
		em.merge(einheit);
		em.flush();
	}

	private void setEinheitKonvertierungFromEinheitKonvertierungDto(
			Einheitkonvertierung einheitkonvertierung,
			EinheitKonvertierungDto einheitKonvertierungDto) {
		einheitkonvertierung.setNFaktor(einheitKonvertierungDto.getNFaktor());
		einheitkonvertierung.setTAnlegen(einheitKonvertierungDto.getTAnlegen());
		einheitkonvertierung.setPersonalIIdAnlegen(einheitKonvertierungDto
				.getPersonalIIdAnlegen());
		einheitkonvertierung.setTAendern(einheitKonvertierungDto.getTAendern());
		einheitkonvertierung.setPersonalIIdAendern(einheitKonvertierungDto
				.getPersonalIIdAendern());
		einheitkonvertierung.setEinheitCNrVon(einheitKonvertierungDto
				.getEinheitCNrVon());
		einheitkonvertierung.setEinheitCNrZu(einheitKonvertierungDto
				.getEinheitCNrZu());
		em.merge(einheitkonvertierung);
		em.flush();
	}

	private EinheitDto assembleEinheitDto(Einheit einheit) {
		return EinheitDtoAssembler.createDto(einheit);
	}

	private EinheitDto[] assembleEinheitDtos(Collection<?> einheits) {
		List<EinheitDto> list = new ArrayList<EinheitDto>();
		if (einheits != null) {
			Iterator<?> iterator = einheits.iterator();
			while (iterator.hasNext()) {
				Einheit einheit = (Einheit) iterator.next();
				list.add(assembleEinheitDto(einheit));
			}
		}
		EinheitDto[] returnArray = new EinheitDto[list.size()];
		return (EinheitDto[]) list.toArray(returnArray);
	}

	public Integer createKostenstelle(KostenstelleDto kostenstelleDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Preconditions.
		if (kostenstelleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kostenstelleDto == null"));
		}
		if (kostenstelleDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("kostenstelleDto.getBVersteckt() == null"));
		}

		try {
			Query query = em
					.createNamedQuery("KostenstellefindByNummerMandant");
			query.setParameter(1, kostenstelleDto.getCNr());
			query.setParameter(2, kostenstelleDto.getMandantCNr());
			Kostenstelle kostenstelle = (Kostenstelle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_KOSTENSTELLE"));
		} catch (NoResultException ex1) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		// Wer legt an setzen.
		// kostenstelleDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// kostenstelleDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// Wegen zirkulaere Abh. beim Testaufbau-DB.
		kostenstelleDto.setPersonalIIdAendern(kostenstelleDto
				.getPersonalIIdAendern());
		kostenstelleDto.setPersonalIIdAnlegen(kostenstelleDto
				.getPersonalIIdAnlegen());

		Integer iIdNew = null;
		try {
			// PK fuer Partner generieren.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iIdNew = pkGen.getNextPrimaryKey(PKConst.PK_KOSTENSTELLE);
			kostenstelleDto.setIId(iIdNew);

			Kostenstelle kostenstelle = new Kostenstelle(
					kostenstelleDto.getIId(), kostenstelleDto.getMandantCNr(),
					kostenstelleDto.getCNr(),
					kostenstelleDto.getBProfitcenter(),
					kostenstelleDto.getBVersteckt());
			em.persist(kostenstelle);
			em.flush();
			setKostenstelleFromKostenstelleDto(kostenstelle, kostenstelleDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iIdNew;
	}

	public void removeKostenstelle(Integer iId) throws EJBExceptionLP {
		Kostenstelle toRemove = em.find(Kostenstelle.class, iId);
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

	public void removeKostenstelle(KostenstelleDto kostenstelleDto)
			throws EJBExceptionLP {
		if (kostenstelleDto != null) {
			Integer iId = kostenstelleDto.getIId();
			removeKostenstelle(iId);
		}
	}

	public void updateKostenstelle(KostenstelleDto kostenstelleDto)
			throws EJBExceptionLP {
		if (kostenstelleDto != null) {
			Integer iId = kostenstelleDto.getIId();
			// try {
			Kostenstelle kostenstelle = em.find(Kostenstelle.class, iId);
			if (kostenstelle == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setKostenstelleFromKostenstelleDto(kostenstelle, kostenstelleDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public KostenstelleDto kostenstelleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Kostenstelle kostenstelle = em.find(Kostenstelle.class, iId);
		if (kostenstelle == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKostenstelleDto(kostenstelle);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public KostenstelleDto[] kostenstelleFindByMandant(String pMandant)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KostenstellefindByMandant");
		query.setParameter(1, pMandant);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		// }
		return assembleKostenstelleDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KostenstelleDto kostenstelleFindByNummerMandant(String cNr,
			String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("KostenstellefindByNummerMandant");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Kostenstelle kostenstelle = (Kostenstelle) query.getSingleResult();
			return assembleKostenstelleDto(kostenstelle);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	public KostenstelleDto kostenstelleFindByNummerMandantOhneExc(String cNr,
			String mandantCNr) throws EJBExceptionLP {
		KostenstelleDto kostenstelleDto = null;
		try {
			Query query = em
					.createNamedQuery("KostenstellefindByNummerMandant");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Kostenstelle kostenstelle = (Kostenstelle) query.getSingleResult();
			kostenstelleDto = assembleKostenstelleDto(kostenstelle);
		} catch (NoResultException ex) {
			// nothing here.
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		return kostenstelleDto;
	}

	private void setProtokollFromProtokollDto(Protokoll protokoll,
			ProtokollDto protokollDto) {
		protokoll.setIId(protokollDto.getIId());
		protokoll.setCArt(protokollDto.getCArt());
		protokoll.setCTyp(protokollDto.getCTyp());
		protokoll.setCText(protokollDto.getCText());
		protokoll.setCLangtext(protokollDto.getCLangtext());
		protokoll.setTQuelle(protokollDto.getTQuelle());
		protokoll.setPersonalIIdAnlegen(protokollDto.getPersonalIIdAnlegen());
		protokoll.setTAnlegen(protokollDto.getTAnlegen());

		em.merge(protokoll);
		em.flush();
	}

	private void setKostenstelleFromKostenstelleDto(Kostenstelle kostenstelle,
			KostenstelleDto kostenstelleDto) {

		kostenstelle.setMandantCNr(kostenstelleDto.getMandantCNr());
		kostenstelle.setCNr(kostenstelleDto.getCNr());
		kostenstelle.setCBez(kostenstelleDto.getCBez());
		kostenstelle.setBProfitcenter(kostenstelleDto.getBProfitcenter());
		kostenstelle.setKontoIId(kostenstelleDto.getKontoIId());
		kostenstelle.setXBemerkung(kostenstelleDto.getxBemerkung());
		kostenstelle.setPersonalIIdAnlegen(kostenstelleDto
				.getPersonalIIdAnlegen());
		kostenstelle.setPersonalIIdAendern(kostenstelleDto
				.getPersonalIIdAendern());
		kostenstelle.setCSubdirectory(kostenstelleDto.getCSubdirectory());
		kostenstelle.setBVersteckt(kostenstelleDto.getBVersteckt());
		kostenstelle.setLagerIIdOhneabbuchung(kostenstelleDto
				.getLagerIIdOhneabbuchung());
		em.merge(kostenstelle);
		em.flush();
	}

	private KostenstelleDto assembleKostenstelleDto(Kostenstelle kostenstelle) {
		return KostenstelleDtoAssembler.createDto(kostenstelle);
	}

	private KostenstelleDto[] assembleKostenstelleDtos(
			Collection<?> kostenstelles) {
		List<KostenstelleDto> list = new ArrayList<KostenstelleDto>();
		if (kostenstelles != null) {
			Iterator<?> iterator = kostenstelles.iterator();
			while (iterator.hasNext()) {
				Kostenstelle kostenstelle = (Kostenstelle) iterator.next();
				list.add(assembleKostenstelleDto(kostenstelle));
			}
		}
		KostenstelleDto[] returnArray = new KostenstelleDto[list.size()];
		return (KostenstelleDto[]) list.toArray(returnArray);
	}

	private void setFunktionFromFunktionDto(Funktion funktion,
			FunktionDto funktionDto) {
		funktion.setCNr(funktionDto.getCNr());
		em.merge(funktion);
		em.flush();
	}

	private FunktionDto assembleFunktionDto(Funktion funktion) {
		return FunktionDtoAssembler.createDto(funktion);
	}

	private void setFunktionsprFromFunktionsprDto(Funktionspr funktionspr,
			FunktionsprDto funktionsprDto) {
		funktionspr.setCBezeichnung(funktionsprDto.getCBezeichnung());
		em.merge(funktionspr);
		em.flush();
	}

	private FunktionsprDto assembleFunktionsprDto(Funktionspr funktionspr) {
		return FunktionsprDtoAssembler.createDto(funktionspr);
	}

	private FunktionsprDto[] assembleFunktionsprDtos(Collection<?> funktionsprs) {
		List<FunktionsprDto> list = new ArrayList<FunktionsprDto>();
		if (funktionsprs != null) {
			Iterator<?> iterator = funktionsprs.iterator();
			while (iterator.hasNext()) {
				Funktionspr funktionspr = (Funktionspr) iterator.next();
				list.add(assembleFunktionsprDto(funktionspr));
			}
		}
		FunktionsprDto[] returnArray = new FunktionsprDto[list.size()];
		return (FunktionsprDto[]) list.toArray(returnArray);
	}

	public void createGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP {
		if (geschaeftsjahrDto == null)
			return;

		try {
			Geschaeftsjahr gj = GeschaeftsjahrQuery.singleByYear(em,
					geschaeftsjahrDto.getIGeschaeftsjahr());
			if (gj == null) {
				gj = new Geschaeftsjahr();
				gj.setIGeschaeftsjahr(geschaeftsjahrDto.getIGeschaeftsjahr());
				gj.setTBeginndatum(geschaeftsjahrDto.getDBeginndatum());
				gj.setTAnlegen(new Timestamp(System.currentTimeMillis()));
				gj.setPersonalIIdAnlegen(geschaeftsjahrDto
						.getPersonalIIdAnlegen());
				em.persist(gj);
				em.flush();
			}

			PKGeneratorObj pkGen = getPKGeneratorObj();
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_GESCHAEFTSJAHRMANDANT);
			geschaeftsjahrDto.setIId(pk);
			geschaeftsjahrDto.setTAnlegen(new Timestamp(System
					.currentTimeMillis()));
			GeschaeftsjahrMandant geschaeftsjahr = new GeschaeftsjahrMandant(
					geschaeftsjahrDto.getIId(),
					geschaeftsjahrDto.getIGeschaeftsjahr(),
					geschaeftsjahrDto.getMandantCnr(),
					geschaeftsjahrDto.getDBeginndatum(),
					geschaeftsjahrDto.getPersonalIIdAnlegen());

			em.persist(geschaeftsjahr);
			em.flush();
			setGeschaeftsjahrMandantFromGeschaeftsjahrMandantDto(
					geschaeftsjahr, geschaeftsjahrDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeGeschaeftsjahr(Integer iGeschaeftsjahr, String mandantCnr)
			throws EJBExceptionLP {
		GeschaeftsjahrMandant toRemove = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, iGeschaeftsjahr, mandantCnr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			em.remove(toRemove);
			em.flush();

			List<GeschaeftsjahrMandant> allEntries = GeschaeftsjahrMandantQuery
					.listByYear(em, iGeschaeftsjahr);
			if (allEntries.size() == 0) {
				Geschaeftsjahr toRemoveToo = GeschaeftsjahrQuery.singleByYear(
						em, iGeschaeftsjahr);
				em.remove(toRemoveToo);
				em.flush();
			}
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP {
		if (geschaeftsjahrDto != null) {
			Integer iGeschaeftsjahr = geschaeftsjahrDto.getIGeschaeftsjahr();
			removeGeschaeftsjahr(iGeschaeftsjahr,
					geschaeftsjahrDto.getMandantCnr());
		}
	}

	public void updateGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP {
		if (geschaeftsjahrDto != null) {
			Integer iGeschaeftsjahr = geschaeftsjahrDto.getIGeschaeftsjahr();
			GeschaeftsjahrMandant geschaeftsjahr = GeschaeftsjahrMandantQuery
					.singleByYearMandant(em, iGeschaeftsjahr,
							geschaeftsjahrDto.getMandantCnr());

			if (geschaeftsjahr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setGeschaeftsjahrMandantFromGeschaeftsjahrMandantDto(
					geschaeftsjahr, geschaeftsjahrDto);
		}
	}

	public void updateGeschaeftsjahrs(
			GeschaeftsjahrMandantDto[] geschaeftsjahrDtos)
			throws EJBExceptionLP {
		if (geschaeftsjahrDtos != null) {
			for (int i = 0; i < geschaeftsjahrDtos.length; i++) {
				updateGeschaeftsjahr(geschaeftsjahrDtos[i]);
			}
		}
	}

	public void sperreGeschaeftsjahr(Integer iGeschaeftsjahr,
			TheClientDto theClientDto) {
		// Geschaeftsjahr geschaeftsjahr = em.find(Geschaeftsjahr.class,
		// iGeschaeftsjahr);
		GeschaeftsjahrMandant geschaeftsjahr = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, iGeschaeftsjahr,
						theClientDto.getMandant());
		if (geschaeftsjahr == null)
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Gesch\u00E4ftsjahr " + iGeschaeftsjahr
							+ " nicht vorhanden!");
		else {
			geschaeftsjahr
					.setTSperre(new Timestamp(System.currentTimeMillis()));
			geschaeftsjahr.setPersonalIIdSperre(theClientDto.getIDPersonal());
		}
	}

	public GeschaeftsjahrMandantDto geschaeftsjahrFindByPrimaryKey(
			Integer iGeschaeftsjahr, String mandantCnr) throws EJBExceptionLP {
		// Geschaeftsjahr geschaeftsjahr = em.find(Geschaeftsjahr.class,
		// iGeschaeftsjahr);
		GeschaeftsjahrMandant geschaeftsjahr = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, iGeschaeftsjahr, mandantCnr);
		if (geschaeftsjahr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleGeschaeftsjahrMandantDto(geschaeftsjahr);
	}

	public byte[][] konvertierePDFFileInEinzelneBilder(String pdfFile,
			int resolution) {
		PDDocument document = null;
		byte[][] oBilder = null;
		try {
			document = PDDocument.load(pdfFile);
			List pages = document.getDocumentCatalog().getAllPages();

			oBilder = new byte[pages.size()][];

			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				BufferedImage image = page.convertToImage(
						BufferedImage.TYPE_INT_RGB, resolution);
				oBilder[i] = Helper.imageToByteArray(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

		} finally {
			if (document != null) {

				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
							e.getMessage());

				}
			}

		}
		return oBilder;
	}

	public GeschaeftsjahrMandantDto geschaeftsjahrFindByPrimaryKeyOhneExc(
			Integer iGeschaeftsjahr, String mandantCnr) throws EJBExceptionLP {
		GeschaeftsjahrMandant geschaeftsjahr = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, iGeschaeftsjahr, mandantCnr);
		// Geschaeftsjahr geschaeftsjahr = em.find(Geschaeftsjahr.class,
		// iGeschaeftsjahr);
		return geschaeftsjahr == null ? null
				: assembleGeschaeftsjahrMandantDto(geschaeftsjahr);
	}

	private void setGeschaeftsjahrMandantFromGeschaeftsjahrMandantDto(
			GeschaeftsjahrMandant geschaeftsjahr,
			GeschaeftsjahrMandantDto geschaeftsjahrDto) {
		geschaeftsjahr.setMandantCNr(geschaeftsjahrDto.getMandantCnr());
		geschaeftsjahr.setTBeginndatum(geschaeftsjahrDto.getDBeginndatum());
		geschaeftsjahr.setPersonalIIdAnlegen(geschaeftsjahrDto
				.getPersonalIIdAnlegen());
		geschaeftsjahr.setTSperre(geschaeftsjahrDto.getTSperre());
		geschaeftsjahr.setPersonalIIdSperre(geschaeftsjahr
				.getPersonalIIdSperre());
		em.merge(geschaeftsjahr);
		em.flush();
	}

	private GeschaeftsjahrMandantDto assembleGeschaeftsjahrMandantDto(
			GeschaeftsjahrMandant geschaeftsjahr) {
		return GeschaeftsjahrMandantDtoAssembler.createDto(geschaeftsjahr);
	}

	private GeschaeftsjahrMandantDto[] assembleGeschaeftsjahrMandantDtos(
			Collection<GeschaeftsjahrMandant> geschaeftsjahrs) {
		return GeschaeftsjahrMandantDtoAssembler.createDtos(geschaeftsjahrs);
	}

	private GeschaeftsjahrDto assembleGeschaeftsjahrDto(
			Geschaeftsjahr geschaeftsjahr) {
		return GeschaeftsjahrDtoAssembler.createDto(geschaeftsjahr);
	}

	private GeschaeftsjahrDto[] assembleGeschaeftsjahrDtos(
			Collection<?> geschaeftsjahrs) {
		return GeschaeftsjahrDtoAssembler.createDtos(geschaeftsjahrs);
		// List<GeschaeftsjahrDto> list = new ArrayList<GeschaeftsjahrDto>();
		// if (geschaeftsjahrs != null) {
		// Iterator<?> iterator = geschaeftsjahrs.iterator();
		// while (iterator.hasNext()) {
		// Geschaeftsjahr geschaeftsjahr = (Geschaeftsjahr) iterator
		// .next();
		// list.add(assembleGeschaeftsjahrDto(geschaeftsjahr));
		// }
		// }
		// GeschaeftsjahrDto[] returnArray = new GeschaeftsjahrDto[list.size()];
		// return (GeschaeftsjahrDto[]) list.toArray(returnArray);
	}

	public Integer createAnwender(AnwenderDto anwenderDto)
			throws EJBExceptionLP {

		if (anwenderDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("anwenderDto == null"));
		}

		if (anwenderDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("anwenderDto.getIId() == null"));
		}

		try {
			Anwender anwender = new Anwender(anwenderDto.getIId(),
					anwenderDto.getIBuildnummerDB(),
					anwenderDto.getCVersionDB(),
					anwenderDto.getIBuildnummerClienVon(),
					anwenderDto.getIBuildnummerClientBis(),
					anwenderDto.getIBuildnummerServerVon(),
					anwenderDto.getIBuildnummerServerBis());
			em.persist(anwender);
			em.flush();
			setAnwenderFromAnwenderDto(anwender, anwenderDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return anwenderDto.getIId();
	}

	public void removeAnwender() throws EJBExceptionLP {
		Object object = new Object();
		Anwender toRemove = em.find(Anwender.class, object);
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

	public void removeAnwender(AnwenderDto anwenderDto) throws EJBExceptionLP {
		if (anwenderDto != null) {
			removeAnwender();
		}
	}

	public void updateAnwender(AnwenderDto anwenderDto) throws EJBExceptionLP {
		if (anwenderDto != null) {
			// try {
			Anwender anwender = em.find(Anwender.class, anwenderDto.getIId());
			if (anwender == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAnwenderFromAnwenderDto(anwender, anwenderDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateAnwenders(AnwenderDto[] anwenderDtos)
			throws EJBExceptionLP {
		if (anwenderDtos != null) {
			for (int i = 0; i < anwenderDtos.length; i++) {
				updateAnwender(anwenderDtos[i]);
			}
		}
	}

	public AnwenderDto anwenderFindByPrimaryKey(Integer iIdI)
			throws EJBExceptionLP {

		AnwenderDto anwenderDto = null;
		// try {
		Anwender anwender = em.find(Anwender.class, iIdI);
		if (anwender == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		anwenderDto = assembleAnwenderDto(anwender);

		// Extra
		anwenderDto.setCVersionServer(HelperServer.getLPResourceBundle()
				.getString("lp.version.server"));
		anwenderDto.setIBuildnummerServer(new Integer(Integer
				.parseInt(HelperServer.getLPResourceBundle().getString(
						"lp.version.server.build"))));
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return anwenderDto;
	}

	private void setAnwenderFromAnwenderDto(Anwender anwender,
			AnwenderDto anwenderDto) {
		anwender.setMandantCNrHauptmandant(anwenderDto
				.getMandantCNrHauptmandant());
		em.merge(anwender);
		em.flush();
	}

	private AnwenderDto assembleAnwenderDto(Anwender anwender) {
		return AnwenderDtoAssembler.createDto(anwender);
	}

	public OrtDto ortFindByName(String cName) throws EJBExceptionLP {
		Validator.notNull(cName, "cName");

		try {
			Query query = em.createNamedQuery("OrtfindByCName");
			query.setParameter(1, cName);
			Ort ort = (Ort) query.getSingleResult();
			return assembleOrtDto(ort);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	public OrtDto ortFindByNameOhneExc(String cName) throws RemoteException {
		Validator.notNull(cName, "cName");

		try {
			HvTypedQuery<Ort> query = OrtQuery.byCNameLower(em, cName);
			Ort ort = query.getSingleResult();
			return assembleOrtDto(ort);
		} catch (NoResultException ex) {
			return null;
		} catch (NonUniqueResultException ex1) {
			return null;
		}
	}

	public Integer createEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (einheitKonvertierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("EinheitKonvertierungDto == null"));
		}

		if (pruefeEinheitKonvertierungViceVersa(
				einheitKonvertierungDto.getEinheitCNrVon(),
				einheitKonvertierungDto.getEinheitCNrZu(), theClientDto) != null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SYSTEM_EINHEITKONVERTIERUNG_SCHON_VORHANDEN,
					new Exception("Konvertierung schon angelegt"));

		}

		// Wer legt an setzen.
		einheitKonvertierungDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		einheitKonvertierungDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());

		Integer iIdNew = null;
		try {

			// PK fuer Einheitkonvertierung generieren.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iIdNew = pkGen.getNextPrimaryKey(PKConst.PK_EINHEITKONVERTIERUNG);
			einheitKonvertierungDto.setIId(iIdNew);

			Einheitkonvertierung einheitKonvertierung = new Einheitkonvertierung(
					einheitKonvertierungDto.getIId(),
					einheitKonvertierungDto.getEinheitCNrVon(),
					einheitKonvertierungDto.getEinheitCNrZu(),
					einheitKonvertierungDto.getNFaktor(),

					einheitKonvertierungDto.getPersonalIIdAnlegen(),
					einheitKonvertierungDto.getPersonalIIdAendern());
			em.persist(einheitKonvertierung);
			em.flush();

			// tAendern und tAnlegen werden im Bean generiert;
			// jetzt holen und setzen wegen update.
			einheitKonvertierungDto.setTAendern(new Timestamp(
					new GregorianCalendar().getTimeInMillis()));
			einheitKonvertierungDto.setTAnlegen(new Timestamp(
					new GregorianCalendar().getTimeInMillis()));

			setEinheitKonvertierungFromEinheitKonvertierungDto(
					einheitKonvertierung, einheitKonvertierungDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iIdNew;
	}

	public void updateEinheitkonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (einheitKonvertierungDto != null) {
			Integer iId = einheitKonvertierungDto.getIId();
			// try {
			Einheitkonvertierung einheitKonvertierung = em.find(
					Einheitkonvertierung.class, iId);
			if (einheitKonvertierung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setEinheitKonvertierungFromEinheitKonvertierungDto(
					einheitKonvertierung, einheitKonvertierungDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void removeEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (einheitKonvertierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("EinheitKonvertierungDto == null"));
		}

		Integer iId = einheitKonvertierungDto.getIId();
		Einheitkonvertierung toRemove = em
				.find(Einheitkonvertierung.class, iId);
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

	public byte[] getHintergrundbild() {
		Anwender anwender = em.find(Anwender.class, new Integer(
				SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

		return anwender.getOHintergrund();

	}

	private EinheitKonvertierungDto[] assembleEinheitKonvertierungDtos(
			Collection<?> einheitKonvertierungen) {
		List<EinheitKonvertierungDto> list = new ArrayList<EinheitKonvertierungDto>();
		if (einheitKonvertierungen != null) {
			Iterator<?> iterator = einheitKonvertierungen.iterator();
			while (iterator.hasNext()) {
				Einheitkonvertierung einheitKonvertierung = (Einheitkonvertierung) iterator
						.next();
				list.add(assembleEinheitKonvertierungDto(einheitKonvertierung));
			}
		}
		EinheitKonvertierungDto[] returnArray = new EinheitKonvertierungDto[list
				.size()];
		return (EinheitKonvertierungDto[]) list.toArray(returnArray);
	}

	private EinheitKonvertierungDto assembleEinheitKonvertierungDto(
			Einheitkonvertierung einheitKonvertierung) {
		return EinheitKonvertierungDtoAssembler.createDto(einheitKonvertierung);
	}

	public EinheitKonvertierungDto einheitKonvertierungFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Einheitkonvertierung einheitkonvertierung = em.find(
				Einheitkonvertierung.class, iId);
		if (einheitkonvertierung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleEinheitKonvertierungDto(einheitkonvertierung);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * diese Methode gibt den Faktor der Konvertierung zurueck wenn vorhanden
	 * 
	 * @param cNrVon
	 *            EinheitDto
	 * @param cNrZu
	 *            String
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal pruefeEinheitKonvertierungViceVersa(String cNrVon,
			String cNrZu, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.info("Von: " + cNrVon + " nach: " + cNrZu);

		BigDecimal bdFaktor = null;
		if (cNrVon.equals(cNrZu)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SYSTEM_EINHEITKONVERTIERUNG_GLEICHE_EINHEITEN,
					new Exception("einheitVon = einheitZU"));
		} else {
			try {
				// jetzt von -> zu suchen
				Einheitkonvertierung einheitKonvertierung = null;
				Query query = em
						.createNamedQuery("EinheitKonvertierungfindByVonZu");
				query.setParameter(1, cNrVon);
				query.setParameter(2, cNrZu);
				// @todo getSingleResult oder getResultList ?
				einheitKonvertierung = (Einheitkonvertierung) query
						.getSingleResult();
				if (einheitKonvertierung != null) {
					bdFaktor = einheitKonvertierung.getNFaktor();
				}
			}
			/**
			 * @todo saubere fehlerbehandlung
			 */
			catch (Throwable t) {
				try {
					// jetzt zu -> von suchen
					Einheitkonvertierung einheitKonvertierung = null;
					Query query = em
							.createNamedQuery("EinheitKonvertierungfindByVonZu");
					query.setParameter(1, cNrZu);
					query.setParameter(2, cNrVon);
					// @todo getSingleResult oder getResultList ?
					einheitKonvertierung = (Einheitkonvertierung) query
							.getSingleResult();
					if (einheitKonvertierung != null) {
						bdFaktor = Helper.rundeKaufmaennisch(new BigDecimal(1)
								.divide(einheitKonvertierung.getNFaktor(), 4,
										BigDecimal.ROUND_HALF_EVEN), 4);
					}

				}
				/**
				 * @todo saubere fehlerbehandlung
				 */
				catch (Throwable t2) {
					// do nothing
				}
			}
		}

		return bdFaktor;
	}

	/**
	 * 
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public EinheitKonvertierungDto[] getAllEinheitKonvertierungen()
			throws EJBExceptionLP {

		myLogger.entry();

		Collection<?> c = new LinkedList<Object>();
		// try {
		Query query = em.createNamedQuery("EinheitKonvertierungfindAll");
		c = query.getResultList();
		// }
		// catch (FinderException ex1) {
		// if(c.isEmpty()){ // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,"");
		// }
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex1);
		// }
		EinheitKonvertierungDto[] allEinheitenKonvertierungen = EinheitKonvertierungDtoAssembler
				.createDtos(c);

		return allEinheitenKonvertierungen;
	}

	/**
	 * 
	 * @param einheitVon
	 *            String
	 * @param einheitZu
	 *            String
	 * @return Map
	 */
	public Map getAllEinheitKonvertierungMitEinheitVonUndEinheitZu(
			String einheitVon, String einheitZu) {
		return null;
	}

	public void createEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (einheitsprDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einheitsprDto == null"));
		}
		try {
			Einheitspr einheitspr = new Einheitspr(
					einheitsprDto.getEinheitCNr(), einheitsprDto.getLocaleCNr());
			em.persist(einheitspr);
			em.flush();
			setEinheitsprFromEinheitsprDto(einheitspr, einheitsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (einheitsprDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"einheitsprDto == null"));
		}

		Einheitspr toRemove = em.find(Einheitspr.class, new EinheitsprPK(
				einheitsprDto.getEinheitCNr(), einheitsprDto.getLocaleCNr()));
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

	public void updateEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition

		if (einheitsprDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"einheitsprDto == null"));
		}

		// try {
		Einheitspr einheitspr = em.find(Einheitspr.class, new EinheitsprPK(
				einheitsprDto.getEinheitCNr(), einheitsprDto.getLocaleCNr()));
		if (einheitspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setEinheitsprFromEinheitsprDto(einheitspr, einheitsprDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public EinheitsprDto einheitsprFindByPrimaryKey(String einheitCNr,
			String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (einheitCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"einheitCNr == null"));
		}
		if (localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"localeCNr == null"));
		}

		// try {
		Einheitspr einheitspr = em.find(Einheitspr.class, new EinheitsprPK(
				einheitCNr, localeCNr));
		if (einheitspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleEinheitsprDto(einheitspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public EinheitsprDto[] einheitsprFindByEinheitCNr(String einheitCNr)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("EinheitsprfindByEinheitCNr");
		query.setParameter(1, einheitCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		// }
		return assembleEinheitsprDtos(cl);
	}

	private void setEinheitsprFromEinheitsprDto(Einheitspr einheitspr,
			EinheitsprDto einheitsprDto) {
		einheitspr.setCBez(einheitsprDto.getCBez());
		em.merge(einheitspr);
		em.flush();
	}

	private EinheitsprDto assembleEinheitsprDto(Einheitspr einheitspr) {
		return EinheitsprDtoAssembler.createDto(einheitspr);
	}

	private EinheitsprDto[] assembleEinheitsprDtos(Collection<?> einheitsprs) {
		List<EinheitsprDto> list = new ArrayList<EinheitsprDto>();
		if (einheitsprs != null) {
			Iterator<?> iterator = einheitsprs.iterator();
			while (iterator.hasNext()) {
				Einheitspr einheitspr = (Einheitspr) iterator.next();
				list.add(assembleEinheitsprDto(einheitspr));
			}
		}
		EinheitsprDto[] returnArray = new EinheitsprDto[list.size()];
		return (EinheitsprDto[]) list.toArray(returnArray);
	}

	public Font[] getFontlist(TheClientDto theClientDto) throws EJBExceptionLP {
		Font[] font = new Font[3];
		font[0] = new Font("Arial", Font.PLAIN, 10);
		font[1] = new Font("Verdana", Font.PLAIN, 10);
		font[2] = new Font("Courier", Font.PLAIN, 10);
		return font;
	}

	public Timestamp getServerTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public void executeUpdateHibernateQuery(String[] sQuery) {
		if (sQuery != null) {
			Session s = FLRSessionFactory.getFactory().openSession();
			for (int i = 0; i < sQuery.length; i++) {
				s.createQuery(sQuery[i]).executeUpdate();
			}
			s.close();
		}
	}

	/**
	 * Einen Betrag von einer Waehrung in die andere umrechnen.
	 * 
	 * @param bdMengeI
	 *            der Betrag in der Ausgangswaehrung
	 * @param cEinheitVonI
	 *            Ausgangswaehrung
	 * @param cEinheitNachI
	 *            Zielwaehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Betrag in der Zielwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal rechneUmInAndereEinheit(BigDecimal bdMengeI,
			String cEinheitVonI, String cEinheitNachI,
			Integer stuecklsitepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (bdMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdMengeI == null"));
		}
		if (cEinheitVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cEinheitVonI == null"));
		}
		if (cEinheitNachI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cEinheitNachI == null"));
		}
		// Den Umrechnungsfaktor bestimmen
		BigDecimal bdFaktor = null;
		// wenn gleich ist faktor 1.00
		if (cEinheitVonI.equals(cEinheitNachI)) {
			bdFaktor = new BigDecimal(1);
		} else {
			try {
				// jetzt von -> zu suchen
				Einheitkonvertierung einheitKonvertierung = null;
				Query query = em
						.createNamedQuery("EinheitKonvertierungfindByVonZu");
				query.setParameter(1, cEinheitVonI);
				query.setParameter(2, cEinheitNachI);
				einheitKonvertierung = (Einheitkonvertierung) query
						.getSingleResult();
				if (einheitKonvertierung != null) {
					bdFaktor = einheitKonvertierung.getNFaktor();
				}
			} catch (NoResultException ex) {
				try {
					// jetzt zu -> von suchen
					Einheitkonvertierung einheitKonvertierung = null;
					Query query = em
							.createNamedQuery("EinheitKonvertierungfindByVonZu");
					query.setParameter(1, cEinheitNachI);
					query.setParameter(2, cEinheitVonI);
					einheitKonvertierung = (Einheitkonvertierung) query
							.getSingleResult();
					// Faktor darf wg. Stuecklistenumrechnung mm2 in m3 nicht
					// gerundet werden
					if (einheitKonvertierung != null) {
						bdFaktor = new BigDecimal(1).divide(
								einheitKonvertierung.getNFaktor(), 8,
								BigDecimal.ROUND_HALF_EVEN);
					}
				} catch (NoResultException ex2) {
					// nothing here
				}
			}
		}
		if (bdFaktor == null) {
			ArrayList al = new ArrayList();
			al.add(cEinheitVonI.trim() + " -> " + cEinheitNachI.trim());
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
					al, new Exception(cEinheitVonI.trim() + " "
							+ cEinheitNachI.trim()));
			ArrayList<Object> a = new ArrayList<Object>();

			try {

				if (stuecklsitepositionIId != null) {
					StuecklistepositionDto stposDto = getStuecklisteFac()
							.stuecklistepositionFindByPrimaryKey(
									stuecklsitepositionIId, theClientDto);

					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									stposDto.getStuecklisteIId(), theClientDto);
					ArtikelDto artikelDtoPos = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									stposDto.getArtikelIId(), theClientDto);
					ArtikelDto artikelDtoStkl = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									stuecklisteDto.getArtikelIId(),
									theClientDto);

					// Projekte 3509/10048/9918
					String position = "";
					if (stposDto.getISort() != null) {
						position = stposDto.getISort() + "";
					}
					// Nachricht zusammenbauen
					String meldung = cEinheitVonI.trim() + " -> "
							+ cEinheitNachI.trim() + "\r\n";
					meldung += "Zu finden in St\u00FCckliste: "
							+ artikelDtoStkl.getCNr() + "\r\n";
					meldung += "Position " + position + "-> Artikelnummer: "
							+ artikelDtoPos.getCNr();
					a.add(meldung);

					ex.setAlInfoForTheClient(a);
				} else {
					String s = cEinheitVonI.trim() + " -> "
							+ cEinheitNachI.trim();
					a.add(s);
				}
				throw ex;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		// umrechnen
		BigDecimal bdMengeUmgerechnet = bdMengeI.multiply(bdFaktor);
		return bdMengeUmgerechnet;
	}

	public void createLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP {
		try {
			Landkfzkennzeichen landkfzkennzeichen = new Landkfzkennzeichen(
					landkfzkennzeichenDto.getCLkz(),
					landkfzkennzeichenDto.getCKfzkennzeichen());
			em.persist(landkfzkennzeichen);
			em.flush();
			setLandkfzkennzeichenFromLandkfzkennzeichenDto(landkfzkennzeichen,
					landkfzkennzeichenDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(""));
		}
	}

	public void removeLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP {
		if (landkfzkennzeichenDto != null) {
			String cLkz = landkfzkennzeichenDto.getCLkz();
			Landkfzkennzeichen toRemove = em.find(Landkfzkennzeichen.class,
					cLkz);
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

	public void updateLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP {
		if (landkfzkennzeichenDto != null) {
			String cLkz = landkfzkennzeichenDto.getCLkz();
			Landkfzkennzeichen landkfzkennzeichen = em.find(
					Landkfzkennzeichen.class, cLkz);
			if (landkfzkennzeichen == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			setLandkfzkennzeichenFromLandkfzkennzeichenDto(landkfzkennzeichen,
					landkfzkennzeichenDto);
		}
	}

	public void updateLandkfzkennzeichens(
			LandkfzkennzeichenDto[] landkfzkennzeichenDtos)
			throws EJBExceptionLP {
		if (landkfzkennzeichenDtos != null) {
			for (int i = 0; i < landkfzkennzeichenDtos.length; i++) {
				updateLandkfzkennzeichen(landkfzkennzeichenDtos[i]);
			}
		}
	}

	public LandkfzkennzeichenDto landkfzkennzeichenFindByPrimaryKey(String cLkz)
			throws EJBExceptionLP {
		// try {
		Landkfzkennzeichen landkfzkennzeichen = em.find(
				Landkfzkennzeichen.class, cLkz);
		if (landkfzkennzeichen == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLandkfzkennzeichenDto(landkfzkennzeichen);

		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// new Exception(""));
		// }
	}

	public LandkfzkennzeichenDto landkfzkennzeichenFindByPrimaryKeyOhneExc(
			String cLkz) throws EJBExceptionLP {
		Landkfzkennzeichen landkfzkennzeichen = em.find(
				Landkfzkennzeichen.class, cLkz);
		if (landkfzkennzeichen == null) {
			return null;
		}
		return assembleLandkfzkennzeichenDto(landkfzkennzeichen);
	}

	private void setLandkfzkennzeichenFromLandkfzkennzeichenDto(
			Landkfzkennzeichen landkfzkennzeichen,
			LandkfzkennzeichenDto landkfzkennzeichenDto) {
		landkfzkennzeichen.setCKfzkennzeichen(landkfzkennzeichenDto
				.getCKfzkennzeichen());
		em.merge(landkfzkennzeichen);
		em.flush();
	}

	private LandkfzkennzeichenDto assembleLandkfzkennzeichenDto(
			Landkfzkennzeichen landkfzkennzeichen) {
		return LandkfzkennzeichenDtoAssembler.createDto(landkfzkennzeichen);
	}

	private LandkfzkennzeichenDto[] assembleLandkfzkennzeichenDtos(
			Collection<?> landkfzkennzeichens) {
		List<LandkfzkennzeichenDto> list = new ArrayList<LandkfzkennzeichenDto>();
		if (landkfzkennzeichens != null) {
			Iterator<?> iterator = landkfzkennzeichens.iterator();
			while (iterator.hasNext()) {
				Landkfzkennzeichen landkfzkennzeichen = (Landkfzkennzeichen) iterator
						.next();
				list.add(assembleLandkfzkennzeichenDto(landkfzkennzeichen));
			}
		}
		LandkfzkennzeichenDto[] returnArray = new LandkfzkennzeichenDto[list
				.size()];
		return (LandkfzkennzeichenDto[]) list.toArray(returnArray);
	}

	public Integer createExtraliste(ExtralisteDto extralisteDto)
			throws EJBExceptionLP {
		if (extralisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("extralisteDto == null"));
		}
		if (extralisteDto.getCBez() == null
				|| extralisteDto.getBelegartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"extralisteDto.getCBez() == null || extralisteDto.getBelegartCNr() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ExtralistefindByCBezBelegartCNr");
			query.setParameter(1, extralisteDto.getCBez());
			query.setParameter(2, extralisteDto.getBelegartCNr());
			Extraliste extraliste = (Extraliste) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_EXTRALISTE.CNR"));
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EXTRALISTE);
			extralisteDto.setIId(pk);

			Extraliste extraliste = new Extraliste(extralisteDto.getIId(),
					extralisteDto.getCBez(), extralisteDto.getBelegartCNr());
			em.persist(extraliste);
			em.flush();
			setExtralisteFromExtralisteDto(extraliste, extralisteDto);
			return extralisteDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeExtraliste(ExtralisteDto extralisteDto)
			throws EJBExceptionLP {
		if (extralisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("extralisteDto == null"));
		}
		if (extralisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("extralisteDto.getIId() == null"));
		}
		Extraliste toRemove = em.find(Extraliste.class, extralisteDto.getIId());
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

	public void updateExtraliste(ExtralisteDto extralisteDto)
			throws EJBExceptionLP {
		if (extralisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("extralisteDto == null"));
		}
		if (extralisteDto.getIId() == null || extralisteDto.getCBez() == null
				|| extralisteDto.getBelegartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"extralisteDto.getIId() ==null || extralisteDto.getCBez() == null || extralisteDto.getBelegartCNr() == null"));
		}

		Integer iId = extralisteDto.getIId();
		try {
			try {
				Query query = em
						.createNamedQuery("ExtralistefindByCBezBelegartCNr");
				query.setParameter(1, extralisteDto.getCBez());
				query.setParameter(2, extralisteDto.getBelegartCNr());
				Integer iIdVorhanden = ((Extraliste) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("LP_EXTRALISTE.UK"));
				}

			} catch (NoResultException ex) {
				//
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

			Extraliste extraliste = em.find(Extraliste.class, iId);
			setExtralisteFromExtralisteDto(extraliste, extralisteDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public ExtralisteDto extralisteFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Extraliste extraliste = em.find(Extraliste.class, iId);
		if (extraliste == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleExtralisteDto(extraliste);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ExtralisteDto[] extralisteFindByBelegartCNr(String belegartCNr)
			throws EJBExceptionLP {
		if (belegartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartCNr == null"));
		}

		try {
			Query query = em.createNamedQuery("ExtralistefindByBelegartCNr");
			query.setParameter(1, belegartCNr);
			Collection<?> cl = query.getResultList();
			return assembleExtralisteDtos(cl);
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setExtralisteFromExtralisteDto(Extraliste extraliste,
			ExtralisteDto extralisteDto) {
		extraliste.setCBez(extralisteDto.getCBez());
		extraliste.setBelegartCNr(extralisteDto.getBelegartCNr());
		extraliste.setXQuery(extralisteDto.getXQuery());
		extraliste.setIDialogbreite(extralisteDto.getIDialogbreite());
		em.merge(extraliste);
		em.flush();
	}

	private ExtralisteDto assembleExtralisteDto(Extraliste extraliste) {
		return ExtralisteDtoAssembler.createDto(extraliste);
	}

	private ExtralisteDto[] assembleExtralisteDtos(Collection<?> extralistes) {
		List<ExtralisteDto> list = new ArrayList<ExtralisteDto>();
		if (extralistes != null) {
			Iterator<?> iterator = extralistes.iterator();
			while (iterator.hasNext()) {
				Extraliste extraliste = (Extraliste) iterator.next();
				list.add(assembleExtralisteDto(extraliste));
			}
		}
		ExtralisteDto[] returnArray = new ExtralisteDto[list.size()];
		return (ExtralisteDto[]) list.toArray(returnArray);
	}

	public String formatEinheit(String einheitCNr, Locale loc,
			TheClientDto theClientDto) throws EJBExceptionLP {
		String ret = "";
		if (einheitCNr != null) {
			EinheitDto einheitDto = null;
			EinheitsprDto einheitsprDto = null;
			if (loc != null) {
				try {
					einheitsprDto = einheitsprFindByPrimaryKey(einheitCNr,
							Helper.locale2String(loc), theClientDto);
				} catch (EJBExceptionLP ex) {
					// Uebersetzung nicht vorhanden
				}
				if (einheitsprDto != null) {
					if (einheitsprDto.getCBez() != null) {
						ret += einheitsprDto.getCBez().trim();
					} else {
						// Uebersetzung ist leer -> EinheitCNr angeben.
						ret += einheitCNr.trim();
					}

				}
			}
			// Keine Uebersetzung definiert -> EinheitCNr angeben.
			if (einheitsprDto == null) {
				ret += einheitCNr.trim();
			}
		}
		return ret;
	}

	public String getServerPathSeperator() {
		return File.separator;
	}

	public void pruefeGeschaeftsjahrSperre(BelegbuchungDto belegBuchungDto,
			String mandantCnr) {
		Buchung buchung = em.find(Buchung.class,
				belegBuchungDto.getBuchungIId());
		pruefeGeschaeftsjahrSperre(buchung.getGeschaeftsjahr(), mandantCnr);
	}

	public void pruefeGeschaeftsjahrSperre(Integer geschaeftsjahr,
			String mandantCnr) {
		GeschaeftsjahrMandant gj = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, geschaeftsjahr, mandantCnr);
		if (gj != null) {
			if (gj.getTSperre() != null) {
				String gjValue = geschaeftsjahr.toString();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_GESCHAEFTSJAHR_GESPERRT,
						"Gesch\u00E4ftsjahr " + gjValue,
						gjValue, new String[]{gjValue});
			}
		}
	}

	public PingPacket ping(PingPacket pingPacket) {
		if (null == pingPacket) {
			pingPacket = new PingPacket();
			pingPacket.setRequestNumber(1);
			pingPacket.setPingTimeSender(System.currentTimeMillis());
		}

		pingPacket.setPingTimeReceiver(System.currentTimeMillis());
		return pingPacket;
	}

	public ServerLocaleInfo getLocaleInfo() {
		ServerLocaleInfo info = new ServerLocaleInfo();
		Locale l = Locale.getDefault();
		Calendar c = Calendar.getInstance();
		info.setTimezone(c.getTimeZone().getDisplayName());
		info.setCountry(l.getCountry());
		info.setLanguage(l.getLanguage());
		info.setTimezoneID(c.getTimeZone().getID());
		info.setDSTSavings(c.getTimeZone().getDSTSavings());

		return info;
	}

	public String getServerVersion() {
		return HelperServer.getLPResourceBundle()
				.getString("lp.version.server");
	}

	public Integer getServerBuildNumber() {
		return Integer.parseInt(HelperServer.getLPResourceBundle().getString(
				"lp.version.server.build"));
	}

	public IVersandwegDto versandwegFindByPrimaryKey(Integer versandwegId) {
		Validator.pkFieldNotNull(versandwegId, "versandwegId");

		Versandweg versandweg = em.find(Versandweg.class, versandwegId);
		if (versandweg == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					versandwegId.toString());
		}

		IVersandwegDto dto = null;

		if (VersandwegType.CleverCureVerkauf.equals(versandweg.getCnr())) {
			VersandwegCC versandwegCC = em.find(VersandwegCC.class,
					versandweg.getIId());
			dto = getMapper().map(versandwegCC, VersandwegCCDto.class);
		}

		return dto;
	}

	public IVersandwegPartnerDto versandwegPartnerFindByPrimaryKey(
			Integer versandwegId, Integer partnerId) {
		Validator.pkFieldNotNull(versandwegId, "versandwegId");
		Validator.pkFieldNotNull(partnerId, "partnerId");

		Versandweg versandweg = em.find(Versandweg.class, versandwegId);
		if (versandweg == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					versandwegId.toString());
		}

		IVersandwegPartnerDto dto = null;
		if (VersandwegType.CleverCureVerkauf.equals(versandweg.getCnr().trim())) {
			try {
				VersandwegCCPartner ccpartner = VersandwegCCPartnerQuery
						.findByVersandwegIIdPartnerIId(em, versandweg.getIId(),
								partnerId);
				dto = getMapper().map(ccpartner, VersandwegCCPartnerDto.class);
			} catch (NoResultException e) {
			} catch (NonUniqueResultException e) {
			}
		}

		return dto;
	}

	public boolean enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			Integer kundeIId,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		// SP1881
		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);
		MwstsatzDto mwstSatzDto = null;
		try {
			mwstSatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kdDto.getMwstsatzbezIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (mwstSatzDto != null && mwstSatzDto.getFMwstsatz() == 0) {
			BigDecimal bdUSt = getBelegVerkaufFac()
					.getGesamtwertInBelegswaehrungUST(belegpositionVerkaufDtos,
							belegVerkaufDto, theClientDto);

			if (bdUSt != null && bdUSt.doubleValue() != 0) {
				return true;
			}

		}

		return false;

	}

	public boolean enthaeltEineVKPositionKeineMwstObwohlKundeSteuerpflichtig(
			Integer kundeIId,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		// SP2010
		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);
		MwstsatzDto mwstSatzDto = null;
		try {
			mwstSatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kdDto.getMwstsatzbezIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (mwstSatzDto != null && mwstSatzDto.getFMwstsatz() != 0) {

			for (int i = 0; i < belegpositionVerkaufDtos.length; i++) {
				if (belegpositionVerkaufDtos[i].getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_IDENT)
						|| belegpositionVerkaufDtos[i].getPositionsartCNr()
								.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

					BigDecimal bdmwst = belegpositionVerkaufDtos[i]
							.getNMwstbetrag();
					if (bdmwst == null) {
						bdmwst = belegpositionVerkaufDtos[i]
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
								.subtract(
										belegpositionVerkaufDtos[i]
												.getNBruttoeinzelpreis());
					}

					if (belegpositionVerkaufDtos[i]
							.getNNettoeinzelpreisplusversteckteraufschlag()
							.doubleValue() != 0
							&& bdmwst.doubleValue() == 0) {
						return true;
					}
				}

			}
		}

		return false;

	}

	@Override
	public ServerJavaAndOSInfo getJavaAndOSInfo() {
		ServerJavaAndOSInfo info = new ServerJavaAndOSInfo();
		info.initProperties();
		return info;
	}

}
