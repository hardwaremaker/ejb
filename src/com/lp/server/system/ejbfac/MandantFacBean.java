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
package com.lp.server.system.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;

import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.finanz.ejb.ReversechargeartQuery;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FinanzServiceFac.ReversechargeArt;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.ReversechargeartsprDto;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejb.Dokumentenlink;
import com.lp.server.system.ejb.Dokumentenlinkbeleg;
import com.lp.server.system.ejb.Kostentraeger;
import com.lp.server.system.ejb.Mandant;
import com.lp.server.system.ejb.Mandantagbspr;
import com.lp.server.system.ejb.Modulberechtigung;
import com.lp.server.system.ejb.ModulberechtigungPK;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.ejb.MwstsatzCode;
import com.lp.server.system.ejb.MwstsatzCodeQuery;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejb.Spediteur;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.ejb.Zahlungszielspr;
import com.lp.server.system.ejb.ZahlungszielsprPK;
import com.lp.server.system.ejb.Zusatzfunktion;
import com.lp.server.system.ejb.Zusatzfunktionberechtigung;
import com.lp.server.system.ejb.ZusatzfunktionberechtigungPK;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.DokumentenlinkDtoAssembler;
import com.lp.server.system.service.DokumentenlinkbelegDto;
import com.lp.server.system.service.DokumentenlinkbelegDtoAssembler;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.KostentraegerDto;
import com.lp.server.system.service.KostentraegerDtoAssembler;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.ModulberechtigungDtoAssembler;
import com.lp.server.system.service.MwstsatzCodeDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzDtoAssembler;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.MwstsatzbezDtoAssembler;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportMandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZahlungszielDtoAssembler;
import com.lp.server.system.service.ZahlungszielsprDto;
import com.lp.server.system.service.ZahlungszielsprDtoAssembler;
import com.lp.server.system.service.ZusatzfunktionDto;
import com.lp.server.system.service.ZusatzfunktionDtoAssembler;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDtoAssembler;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.MwstsatzCodeId;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.ReversechargeartId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class MandantFacBean extends Facade implements MandantFac {

	@PersistenceContext
	private EntityManager em;

	@Transient
	private transient ModelMapper mapper = null;

	protected ModelMapper getMapper() {
		if (mapper == null) {
			mapper = new ModelMapper();
		}
		return mapper;
	}

	public Integer createMwstsatz(MwstsatzDto mwstsatzDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("theClientDto == null"));
		}
		if (mwstsatzDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("mwstsatzDtoI == null"));
		}

		// checkDoppelter(mwstsatzDtoI, null);

		Integer iIdMWSNew = null;
		Integer iIdMwstbez = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iIdMWSNew = pkGen.getNextPrimaryKey(PKConst.PK_MWSSATZ);

			mwstsatzDtoI.setIId(iIdMWSNew);
			if (mwstsatzDtoI.getMwstsatzbezDto() != null) {

				iIdMwstbez = createMwstsatzbez(mwstsatzDtoI.getMwstsatzbezDto(), theClientDto);

				mwstsatzDtoI.setIIMwstsatzbezId(iIdMwstbez);
			}
			Mwstsatz mwstsatz = new Mwstsatz(mwstsatzDtoI.getIId(), mwstsatzDtoI.getFMwstsatz(),
					mwstsatzDtoI.getDGueltigab(), mwstsatzDtoI.getIIMwstsatzbezId());
			em.persist(mwstsatz);
			em.flush();
			setMwstsatzFromMwstsatzDto(mwstsatz, mwstsatzDtoI);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iIdMWSNew;
	}

	public Integer createKostentraeger(KostentraegerDto dto) {

		try {
			Query query = em.createNamedQuery("KostentraegerFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Kostentraeger doppelt = (Kostentraeger) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_KOSTENTRAEGER.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOSTENTRAEGER);
			dto.setIId(pk);

			Kostentraeger bean = new Kostentraeger(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setKostentraegerFromKostentraegerDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKostentraeger(KostentraegerDto dto) {
		Kostentraeger toRemove = em.find(Kostentraeger.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public KostentraegerDto kostentraegerFindByPrimaryKey(Integer iId) {
		Kostentraeger ialle = em.find(Kostentraeger.class, iId);
		return KostentraegerDtoAssembler.createDto(ialle);
	}

	public void updateKostentraeger(KostentraegerDto dto) {
		Kostentraeger ialle = em.find(Kostentraeger.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("KostentraegerFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Kostentraeger) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_KOSTENTRAEGER.UK"));
			}
		} catch (NoResultException ex) {

		}

		setKostentraegerFromKostentraegerDto(ialle, dto);
	}

	private void setKostentraegerFromKostentraegerDto(Kostentraeger bean, KostentraegerDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	/**
	 * Pruefe auf "doppelte" Mwsteintraege.
	 * 
	 * @param mwstsatzDtoI MwstsatzDto
	 * @param theClientDto MwstsatzDto[]
	 * @throws EJBExceptionLP / /* private void checkDoppelter(MwstsatzDto
	 *                        mwstsatzDtoI, Integer mwstIIdI) throws EJBExceptionLP
	 *                        { MwstsatzDto[] mwstDto = null; MwstsatzbezDto[]
	 *                        mwstbezDto = null; try { /// mwstDto =
	 *                        assembleMwstsatzDtos(mwstsatzHome.findAllByMandant(mwstsatzDtoI
	 *                        .getMandantCNr())); mwstbezDto =
	 *                        assembleMwstsatzbezDtos(mwstsatzbezHome
	 *                        .findAllByMandant(mwstsatzDtoI. getMandantCNr())); for
	 *                        (int i = 0; i < mwstbezDto.length; i++) {try {
	 *                        mwstDto[i] =* assembleMwstsatzDto
	 *                        (em.find(Mwstsatz.class, mwstbezDto[i].* getIId())); }
	 *                        catch (FinderException ex) { throw new*
	 *                        EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	 *                        ex); } } } catch (FinderException ex) { // nothing
	 *                        here } for (int i = 0; i < mwstDto.length; i++) {if
	 *                        (mwstIIdI != null) { if
	 *                        (mwstIIdI.compareTo(mwstDto[i].getIId()) != 0) { if (
	 *                        (mwstDto
	 *                        [i].getFMwstsatz().equals(mwstsatzDtoI.getFMwstsatz())
	 *                        || (mwstDto
	 *                        [i].getCBezeichnung().equals(mwstsatzDtoI.getCBezeichnung
	 *                        ())))) { throw new
	 *                        EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
	 *                        null); } } } else { if (
	 *                        (mwstDto[i].getFMwstsatz().equals(mwstsatzDtoI
	 *                        .getFMwstsatz()) ||
	 *                        (mwstDto[i].getCBezeichnung().equals(mwstsatzDtoI
	 *                        .getCBezeichnung())))) { throw new
	 *                        EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
	 *                        null); } } } }
	 */
	public void removeMwstsatz(MwstsatzDto mwstsatzDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrUserI == null"));
		}
		if (mwstsatzDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("mwstsatzDtoI == null"));
		}

		Mwstsatz toRemove = em.find(Mwstsatz.class, mwstsatzDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		removeMwstsatzCode(new MwstsatzId(toRemove.getIId()));

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public boolean darfAnwenderAufZusatzfunktionZugreifen(String zusatzfunktionCNr, TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(zusatzfunktionCNr, theClientDto.getMandant());
	}

	public boolean darfAnwenderAufZusatzfunktionZugreifen(String zusatzfunktionCNr, String mandantCNr) {

		Zusatzfunktionberechtigung zusatzfunktionberechtigung = em.find(Zusatzfunktionberechtigung.class,
				new ZusatzfunktionberechtigungPK(zusatzfunktionCNr, mandantCNr));

		if (zusatzfunktionberechtigung == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * mwstsatz update nur erlaubt wenn noch nicht in verwendung sonst kann man nur
	 * neu anlegen
	 * 
	 * @param mwstsatzDtoI MwstsatzDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void updateMwstsatz(MwstsatzDto mwstsatzDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrUserI == null"));
		}
		if (mwstsatzDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("mwstsatzDtoI == null"));
		}

		// Integer iId = mwstsatzDtoI.getIId();
		try {
			Mwstsatz mwstsatz = em.find(Mwstsatz.class, mwstsatzDtoI.getIId());
			if (mwstsatz == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// em.remove(mwstsatz);
			// em.flush();
			// Integer id = createMwstsatz(mwstsatzDtoI, cNrUserI);
			// mwstsatz = em.find(Mwstsatz.class, id);
			setMwstsatzFromMwstsatzDto(mwstsatz, mwstsatzDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public MwstsatzDto mwstsatzFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(theClientDto, "theClientDto");
		Validator.pkFieldNotNull(iId, "iId");

		// TheClientDto theClientDto = check(cNrUserI);

		Mwstsatz mwstsatz = em.find(Mwstsatz.class, iId);
		if (mwstsatz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		MwstsatzDto mwstsatzDto = assembleMwstsatzDto(mwstsatz);
		mwstsatzDto.setMwstsatzbezDto(mwstsatzbezFindByPrimaryKey(mwstsatz.getMwstsatzbezIId(), theClientDto));

		return mwstsatzDto;
	}

	private void setMwstsatzFromMwstsatzDto(Mwstsatz mwstsatz, MwstsatzDto mwstsatzDto) {

		mwstsatz.setFMwstsatz(mwstsatzDto.getFMwstsatz());
		mwstsatz.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
		mwstsatz.setDGueltigab(mwstsatzDto.getDGueltigab());
		em.merge(mwstsatz);
		em.flush();
	}

	private MwstsatzDto assembleMwstsatzDto(Mwstsatz mwstsatz) {
		return MwstsatzDtoAssembler.createDto(mwstsatz);
	}

	private MwstsatzDto[] assembleMwstsatzDtos(Collection<?> cl) {
		List<MwstsatzDto> list = new ArrayList<MwstsatzDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Mwstsatz mwstsatz = (Mwstsatz) iterator.next();
				list.add(assembleMwstsatzDto(mwstsatz));
			}
		}
		MwstsatzDto[] returnArray = new MwstsatzDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * Finde alle MWST-Satzbezeichnungen eines Mandanten.
	 * 
	 * @param ssMandantI   String
	 * @param theClientDto String
	 * @return Map: Key = I_ID der Bezeichnung, Value = Bezeichnung
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, String> mwstsatzbezFindAllByMandant(String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		MwstsatzbezDto[] mwstbezDto = null;
		// TheClientDto theClientDto = super.check(cNrUserI);
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		// try {
		Query query = em.createNamedQuery("MwstsatzbezfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		// }
		mwstbezDto = assembleMwstsatzbezDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		// Key, Bezeichnung in die Map schreiben.
		for (int i = 0; i < mwstbezDto.length; i++) {
			map.put(mwstbezDto[i].getIId(), mwstbezDto[i].getCBezeichnung());
			System.out.println("MWST" + mwstbezDto[i].toString());
		}
		return map;
	}

	public MwstsatzbezDto[] mwstsatzbezFindAllByMandantAsDto(String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		MwstsatzbezDto[] mwstbezDtos = null;
		Query query = em.createNamedQuery("MwstsatzbezfindAllByMandantInklHandeingabe"); // "MwstsatzbezfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<?> cl = query.getResultList();
		mwstbezDtos = assembleMwstsatzbezDtos(cl);
		return mwstbezDtos;
	}

	public Map<Integer, String> mwstsatzFindAllByMandant(String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LinkedHashMap<Integer, String> t = new LinkedHashMap<Integer, String>();
		Collection<MwstsatzDto> c = mwstsatzfindAllByMandant(ssMandantI);
		for (Iterator<MwstsatzDto> iter = c.iterator(); iter.hasNext();) {
			MwstsatzDto mwst = iter.next();
			t.put(mwst.getIId(), mwst.formatMwstsatz(theClientDto));
		}
		return t;
	}

	/**
	 * Finde die I_ID's aller MWST-Saetze eines Mandanten.
	 * 
	 * @param ssMandantI   String
	 * @param theClientDto String
	 * @return Collection
	 * @throws EJBExceptionLP
	 */
	public Set<Integer> mwstsatzIIdFindAllByMandant(String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// TheClientDto theClientDto = super.check(cNrUserI);
		Set<Integer> cIDs = new LinkedHashSet<Integer>();
		Collection<MwstsatzDto> c = mwstsatzfindAllByMandant(ssMandantI);
		for (Iterator<MwstsatzDto> iter = c.iterator(); iter.hasNext();) {
			MwstsatzDto mwst = iter.next();
			cIDs.add(mwst.getIId());
		}
		return cIDs;
	}

	/**
	 * Alle Aktuellen MWST-Saetze eines Mandanten finden.
	 * 
	 * @param ssMandantI   String
	 * @param tBelegdatum  Timestamp: optional
	 * @param theClientDto String
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, String> mwstsatzFindAllByMandant(String ssMandantI, Timestamp tBelegdatum,
			boolean bInklHandeingabe, TheClientDto theClientDto) throws EJBExceptionLP {
		LinkedHashMap<Integer, String> mapResult = new LinkedHashMap<Integer, String>();
		MwstsatzDto[] mwstSaetze = mwstsatzfindAllByMandant(ssMandantI, tBelegdatum, bInklHandeingabe);
		// zur richtigen Darstellung am Client
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(theClientDto.getLocUi());
		for (int i = 0; i < mwstSaetze.length; i++) {
			MwstsatzDto mwst = mwstSaetze[i];
			String mwstValue = formatMwstValue(mwst, dfs.getPercent(), theClientDto.getLocUi());
			mapResult.put(mwst.getIId(), mwstValue);
		}
		return mapResult;
	}

	/**
	 * Alle MWST-Saetze finden. inklusive MWST-Satz-Bezeichnung.
	 * 
	 * @param theClientDto String
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, MwstsatzDto> mwstsatzFindAll(TheClientDto theClientDto) throws EJBExceptionLP {
		// TheClientDto theClientDto = super.check(cNrUserI);
		HashMap<Integer, MwstsatzDto> t = new HashMap<Integer, MwstsatzDto>();
		// try {
		Query query = em.createNamedQuery("MwstsatzfindAll");
		Collection<?> c = query.getResultList();
		// if(c.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		// }
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			MwstsatzDto mwstDto = assembleMwstsatzDto((Mwstsatz) iter.next());
			// Bezeichnung dazu holen
			Mwstsatzbez mwstsatzbez = em.find(Mwstsatzbez.class, mwstDto.getIIMwstsatzbezId());
			if (mwstsatzbez == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			MwstsatzbezDto mwstsatzbezDto = assembleMwstsatzbezDto(mwstsatzbez);

			mwstDto.setMwstsatzbezDto(mwstsatzbezDto);
			// key ist die I_ID, Value ist das Dto
			t.put(mwstDto.getIId(), mwstDto);
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return t;
	}

	/**
	 * Alle MWST-Saetze eines Mandanten finden. inkl. MWST-Satz-Bezeichnung.
	 * 
	 * @param ssMandantI String
	 * @return Collection
	 * @throws EJBExceptionLP
	 */
	private Collection<MwstsatzDto> mwstsatzfindAllByMandant(String ssMandantI) throws EJBExceptionLP {
		ArrayList<MwstsatzDto> aMwst = new ArrayList<MwstsatzDto>();
		MwstsatzbezDto[] mwstbezDto = null;
		MwstsatzDto[] mwstDtos = null;
		// try {
		Query query = em.createNamedQuery("MwstsatzbezfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		// }
		mwstbezDto = assembleMwstsatzbezDtos(cl);
		for (int i = 0; i < mwstbezDto.length; i++) {
			query = em.createNamedQuery("MwstsatzfindByMwstbez");
			query.setParameter(1, mwstbezDto[i].getIId());
			cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
			// }
			mwstDtos = assembleMwstsatzDtos(cl);

			for (int j = 0; j < mwstDtos.length; j++) {
				mwstDtos[j].setMwstsatzbezDto(mwstbezDto[i]);
				aMwst.add(mwstDtos[j]);
			}
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return aMwst;
	}

	/**
	 * MWST-Satz zur MwstBezeichnung und Datum finden.
	 * 
	 * @param mwstsatzbezIId die IId der Mehrwertsteuersatzbezeichnung String
	 * @param tDatum         zu dem der Mwstsatz ermittelt werden soll
	 * @return der passende Mwstsatz oder null
	 */
	public MwstsatzDto mwstsatzFindZuDatum(Integer mwstsatzbezIId, Timestamp tDatum) {

		Query query = em.createNamedQuery("MwstsatzfindByTGueltigabMwstsatzbezIId");
		query.setParameter(1, tDatum);
		query.setParameter(2, mwstsatzbezIId);
		query.setMaxResults(1); // ist absteigend sortiert!

		Collection<?> cl = query.getResultList();

		MwstsatzDto[] mwstsatzDtos = assembleMwstsatzDtos(cl);
		if (mwstsatzDtos.length > 0) {
			return mwstsatzDtos[0];
		} else {
			return null;
		}
	}

	@Override
	public MwstsatzDto mwstsatzZuDatumClient(Integer mwstsatzbezId, Timestamp datum, TheClientDto theClientDto) {
		MwstsatzDto mwstsatzDto = mwstsatzFindZuDatum(mwstsatzbezId, datum);
		if (mwstsatzDto == null)
			return null;

		if (theClientDto != null) {
			mwstsatzDto.setMwstsatzbezDto(mwstsatzbezFindByPrimaryKey(mwstsatzbezId, theClientDto));
		}

		return mwstsatzDto;
	}

	@Override
	public MwstsatzDto mwstsatzZuDatumValidate(Integer mwstsatzbezId, Timestamp datum, TheClientDto theClientDto) {
		MwstsatzDto mwstsatzDto = mwstsatzFindZuDatum(mwstsatzbezId, datum);
		if (mwstsatzDto == null) {
			throw EJBExcFactory.mwstsatzFehlt(mwstsatzbezId, datum);
		}

		if (theClientDto != null) {
			mwstsatzDto.setMwstsatzbezDto(mwstsatzbezFindByPrimaryKey(mwstsatzbezId, theClientDto));
		}

		return mwstsatzDto;
	}

	@Override
	public MwstsatzDto mwstsatzZuDatumEvaluate(MwstsatzId satzId, Timestamp datum, TheClientDto theClientDto) {
		MwstsatzDto dto = mwstsatzFindByPrimaryKey(satzId.id(), theClientDto);
		return mwstsatzZuDatumValidate(dto.getIIMwstsatzbezId(), datum, theClientDto);
	}

	public MwstsatzDto getMwstSatzVonBruttoBetragUndUst(String mandant, Timestamp tBelegDatum, BigDecimal bruttoBetrag,
			BigDecimal mwstBetrag) {
		if (null == mwstBetrag || mwstBetrag.signum() == 0)
			mwstBetrag = BigDecimal.ZERO;

		MwstsatzDto[] mwstdtos = getMandantFac().mwstsatzfindAllByMandant(mandant, tBelegDatum, false);

		BigDecimal minDiff = null;
		MwstsatzDto selectedMwstSatz = null;
		for (MwstsatzDto mwstsatzDto : mwstdtos) {
			BigDecimal satz = new BigDecimal(mwstsatzDto.getFMwstsatz());
			BigDecimal mwst = Helper.getMehrwertsteuerBetrag(bruttoBetrag, satz);
			BigDecimal diff = mwstBetrag.subtract(mwst).abs();
			if (minDiff == null || diff.compareTo(minDiff) < 0) {
				minDiff = diff;
				selectedMwstSatz = mwstsatzDto;
			}
		}

		return selectedMwstSatz;
	}

	public MwstsatzDto getMwstSatzVonNettoBetragUndUst(String mandant, Timestamp tBelegDatum, BigDecimal nettoBetrag,
			BigDecimal mwstBetrag) {
		if (null == mwstBetrag || mwstBetrag.signum() == 0) {
			mwstBetrag = BigDecimal.ZERO;
		}

		MwstsatzDto[] mwstdtos = getMandantFac().mwstsatzfindAllByMandant(mandant, tBelegDatum, false);

		BigDecimal minDiff = null;
		MwstsatzDto selectedMwstSatz = null;
		for (MwstsatzDto mwstsatzDto : mwstdtos) {
			BigDecimal satz = new BigDecimal(mwstsatzDto.getFMwstsatz());
			BigDecimal mwst = Helper.getMehrwertsteuerBetragFuerNetto(nettoBetrag, satz);
			BigDecimal diff = mwstBetrag.subtract(mwst).abs();
			if (minDiff == null || diff.compareTo(minDiff) < 0) {
				minDiff = diff;
				selectedMwstSatz = mwstsatzDto;
			}
		}

		return selectedMwstSatz;
	}

	public MwstsatzDto[] mwstsatzfindAllByMandant(String ssMandantI, Timestamp tBelegdatum, boolean bInklHandeingabe)
			throws EJBExceptionLP {
		// Alle Bezeichnungen des Mandanten finden.
		Query query = em.createNamedQuery(
				bInklHandeingabe ? "MwstsatzbezfindAllByMandantInklHandeingabe" : "MwstsatzbezfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<?> cl = query.getResultList();

		// SP8303 Wir wollen den Standard-Inlands-Satz als ersten haben
		Mandant mandant = em.find(Mandant.class, ssMandantI);
		Integer defaultMwstsatzBezId = mandant.getMwstsatzIIdStandardinlandmwstsatz();

		List<MwstsatzDto> aMwst = new ArrayList<MwstsatzDto>();
		MwstsatzbezDto[] mwstbezDtos = assembleMwstsatzbezDtos(cl);
		for (int i = 0; i < mwstbezDtos.length; i++) {
			// Die aktuellsten dazugehoerigen MWST-Saetze. optional ab
			// Gueltigkeitsdatum.
			MwstsatzDto[] mwstDtosZuBez;
			if (tBelegdatum != null) {
				query = em.createNamedQuery("MwstsatzfindByTGueltigabMwstsatzbezIId");
				query.setParameter(1, tBelegdatum);
				query.setParameter(2, mwstbezDtos[i].getIId());
				query.setMaxResults(1); // ist absteigend sortiert!
				cl = query.getResultList();
				mwstDtosZuBez = assembleMwstsatzDtos(cl);
			} else {
				// TODO: SP8308 Macht es nicht mehr Sinn das aktuelle Datum zu nehmen?
				query = em.createNamedQuery("MwstsatzfindByMwstbez");
				query.setParameter(1, mwstbezDtos[i].getIId());
				cl = query.getResultList();
				mwstDtosZuBez = assembleMwstsatzDtos(cl);
			}

			// Gibt es einen gueltigen?
			if (mwstDtosZuBez.length != 0) {
				// den letzten nehmen, das ist der aktuellste.
				MwstsatzDto mwstsatz = mwstDtosZuBez[mwstDtosZuBez.length - 1];
				mwstsatz.setMwstsatzbezDto(mwstbezDtos[i]);

				if (mwstbezDtos[i].getIId().equals(defaultMwstsatzBezId)) {
					aMwst.add(0, mwstsatz);
				} else {
					aMwst.add(mwstsatz);
				}
			}
		}

		return aMwst.toArray(new MwstsatzDto[0]);
	}

	/**
	 * Alle MWST-Saetze eines Mandanten als Dtos in einer Map zurueckgeben. Hier
	 * sind auch die nicht mehr gueltigen enthalten.
	 * 
	 * @param ssMandantI   String
	 * @param theClientDto String
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, MwstsatzDto> mwstsatzFindAllByMandantAsDto(String ssMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LinkedHashMap<Integer, MwstsatzDto> map = new LinkedHashMap<Integer, MwstsatzDto>();
		// try {
		// Alle Bezeichnungen des Mandanten
		Query query = em.createNamedQuery("MwstsatzbezfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<Mwstsatzbez> collection = query.getResultList();
		for (Iterator<?> iter = collection.iterator(); iter.hasNext();) {
			Mwstsatzbez mwstbez = (Mwstsatzbez) iter.next();
			// Saetze dazu finden (auch die nicht mehr gueltigen).
			query = em.createNamedQuery("MwstsatzfindByMwstbez");
			query.setParameter(1, mwstbez.getIId());
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
			// }
			MwstsatzDto[] mwstsaetze = assembleMwstsatzDtos(cl);

			for (int i = 0; i < mwstsaetze.length; i++) {
				map.put(mwstsaetze[i].getIId(), mwstsaetze[i]);
			}
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return map;
	}

	/**
	 * Den Aktuellen MWST-Satz zu einer MWST-Bezeichnung finden.
	 * 
	 * @param mwstsatzbezIId String
	 * @param theClientDto   Timestamp: optional
	 * @return MwstsatzDto[]
	 * @throws EJBExceptionLP
	 */
	public MwstsatzDto mwstsatzFindByMwstsatzbezIIdAktuellster(Integer mwstsatzbezIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.warn("mwstsatzAktuellster for bezId " + mwstsatzbezIId + " called");

		Query query = em.createNamedQuery("MwstsatzfindByMwstbez");
		query.setParameter(1, mwstsatzbezIId);
		Collection<?> cl = query.getResultList();

		MwstsatzDto[] mwstsatzAlle = assembleMwstsatzDtos(cl);
		MwstsatzDto mwstsatzAktuell = null;

		// Gibt es einen Gueltigen?
		if (mwstsatzAlle.length != 0) {
			// den letzten nehmen, das ist der aktuellste.
			mwstsatzAktuell = mwstsatzAlle[mwstsatzAlle.length - 1];
			// Bezeichnung dazunehmen
			mwstsatzAktuell.setMwstsatzbezDto(mwstsatzbezFindByPrimaryKey(mwstsatzbezIId, theClientDto));
		}

		return mwstsatzAktuell;
	}

	/**
	 * 
	 * @param mwstI          der Mwstsatz
	 * @param cPercentSymbol char
	 * @param locale         Locale
	 * @return String der formatierte Mwstsatz
	 * @see com.lp.server.system.service#formatMwstValue()
	 */
	private String formatMwstValue(MwstsatzDto mwstI, char cPercentSymbol, Locale locale) {
		StringBuffer s = new StringBuffer();
		s.append(Helper.formatZahl(mwstI.getFMwstsatz(), 1, locale));
		s.append(cPercentSymbol);
		s.append(" " + mwstI.getMwstsatzbezDto().getCBezeichnung());
		return s.toString();
	}

	public void updateAGBs_PDF(byte[] oPdf, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("MandantagbsprFindByMandantCNrLocaleCnr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, theClientDto.getLocMandantAsString());

		try {
			Mandantagbspr mandantagbspr = (Mandantagbspr) query.getSingleResult();

			if (oPdf != null) {
				mandantagbspr.setOPDF(oPdf);
				em.merge(mandantagbspr);
				em.flush();
			} else {
				em.remove(mandantagbspr);
			}
		} catch (NoResultException e) {

			if (oPdf != null) {
				Integer i_id = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_MANDANTAGBSPR);

				Mandantagbspr mandantagbspr = new Mandantagbspr(i_id, theClientDto.getMandant(),
						theClientDto.getLocUiAsString(), oPdf);

				em.merge(mandantagbspr);
				em.flush();
			}
		}

	}

	public byte[] getAGBs_PDF(Locale loc, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("MandantagbsprFindByMandantCNrLocaleCnr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, Helper.locale2String(loc));

		try {
			Mandantagbspr mandantagbspr = (Mandantagbspr) query.getSingleResult();
			return mandantagbspr.getOPDF();
		} catch (NoResultException e) {

			try {
				query = em.createNamedQuery("MandantagbsprFindByMandantCNrLocaleCnr");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, theClientDto.getLocMandantAsString());
			} catch (NoResultException e1) {
				return null;
			}
		}

		return null;

	}

	public Locale getLocaleDesHauptmandanten() throws EJBExceptionLP {

		try {
			AnwenderDto anwenderDto = getSystemFac()
					.anwenderFindByPrimaryKey(new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

			Mandant mandant = em.find(Mandant.class, anwenderDto.getMandantCNrHauptmandant());

			Partner partner = em.find(Partner.class, mandant.getPartnerIId());

			return new Locale(partner.getLocaleCNrKommunikation().substring(0, 2),
					partner.getLocaleCNrKommunikation().substring(2, 4));
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	// Spediteur
	// -----------------------------------------------------------------

	public Integer createSpediteur(SpediteurDto oSpediteurDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		myLogger.entry();
		checkSpediteurDto(oSpediteurDtoI);

		Integer iIdSpediteurO = null;
		iIdSpediteurO = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_SPEDITEUR);

		if (oSpediteurDtoI.getBVersteckt() == null) {
			oSpediteurDtoI.setBVersteckt(Helper.boolean2Short(false));
		}

		try {
			oSpediteurDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Spediteur spediteur = new Spediteur(iIdSpediteurO, oSpediteurDtoI.getMandantCNr(),
					oSpediteurDtoI.getCNamedesspediteurs(), theClientDto.getIDPersonal(),
					oSpediteurDtoI.getBVersteckt());
			em.persist(spediteur);
			em.flush();

			setSpediteurFromSpediteurDto(spediteur, oSpediteurDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iIdSpediteurO;
	}

	public Integer createDokumentenlink(DokumentenlinkDto dokumentenlinkDto, TheClientDto theClientDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_DOKUMENTENLINK);
		dokumentenlinkDto.setIId(iId);
		try {

			if (dokumentenlinkDto.getBPfadAusArbeitsplatzparameter() == null) {
				dokumentenlinkDto.setBPfadAusArbeitsplatzparameter(Helper.boolean2Short(false));
			}
			if (dokumentenlinkDto.getBTitel() == null) {
				dokumentenlinkDto.setBTitel(Helper.boolean2Short(false));
			}

			Dokumentenlink dokumentenlink = new Dokumentenlink(dokumentenlinkDto.getIId(),
					dokumentenlinkDto.getBelegartCNr(), dokumentenlinkDto.getCBasispfad(),
					dokumentenlinkDto.getMandantCNr(), dokumentenlinkDto.getCMenuetext(),
					dokumentenlinkDto.getBPfadabsolut(), dokumentenlinkDto.getBUrl(),
					dokumentenlinkDto.getBPfadAusArbeitsplatzparameter(), dokumentenlinkDto.getBTitel());
			em.persist(dokumentenlink);
			em.flush();

			setDokumentenlinkFromDokumentenlinkDto(dokumentenlink, dokumentenlinkDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return dokumentenlinkDto.getIId();
	}

	public DokumentenlinkbelegDto[] getDokumentenlinkbelegs(String belegartCNr, Integer belegartIId,
			TheClientDto theClientDto) {

		DokumentenlinkDto[] dtos = dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(belegartCNr,
				theClientDto.getMandant(), true);

		DokumentenlinkbelegDto[] dtosBeleg = new DokumentenlinkbelegDto[dtos.length];

		for (int i = 0; i < dtos.length; i++) {

			Query query = em.createNamedQuery("DokumentenlinkbelegFindByDokumentenlinkIIdIIdBelegart");
			query.setParameter(1, dtos[i].getIId());
			query.setParameter(2, belegartIId);

			try {
				Dokumentenlinkbeleg dokumentenlinkbeleg = (Dokumentenlinkbeleg) query.getSingleResult();

				dtosBeleg[i] = DokumentenlinkbelegDtoAssembler.createDto(dokumentenlinkbeleg);

			} catch (NoResultException e) {
				dtosBeleg[i] = new DokumentenlinkbelegDto();
				dtosBeleg[i].setDokumentenlinkIId(dtos[i].getIId());
			}
			dtosBeleg[i].setDokumentenlinkDto(dtos[i]);

		}

		return dtosBeleg;
	}

	public void updateDokumentenlinkbeleg(String belegartCNr, Integer iBelegartId,
			DokumentenlinkbelegDto[] dokumentenlinkbelegDtos, TheClientDto theClientDto) {

		for (int i = 0; i < dokumentenlinkbelegDtos.length; i++) {
			try {
				Query query = em.createNamedQuery("DokumentenlinkbelegFindByDokumentenlinkIIdIIdBelegart");
				query.setParameter(1, dokumentenlinkbelegDtos[i].getDokumentenlinkIId());
				query.setParameter(2, iBelegartId);
				Dokumentenlinkbeleg dokumentenlinkbeleg = (Dokumentenlinkbeleg) query.getSingleResult();

				dokumentenlinkbeleg.setCPfad(dokumentenlinkbelegDtos[i].getCPfad());
				em.merge(dokumentenlinkbeleg);
				em.flush();
			} catch (NoResultException e) {
				Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_DOKUMENTENLINKBELEG);

				Dokumentenlinkbeleg d = new Dokumentenlinkbeleg(iId, dokumentenlinkbelegDtos[i].getDokumentenlinkIId(),
						iBelegartId);
				d.setCPfad(dokumentenlinkbelegDtos[i].getCPfad());
				em.merge(d);
				em.flush();
			}

		}

	}

	public void removeSpediteur(SpediteurDto spediteurDto) throws EJBExceptionLP {

		Integer iId = spediteurDto.getIId();
		Spediteur toRemove = em.find(Spediteur.class, iId);
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

	public void removeDokumentenlink(DokumentenlinkDto dokumentenlinkDto) {

		Integer iId = dokumentenlinkDto.getIId();
		Dokumentenlink toRemove = em.find(Dokumentenlink.class, iId);
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

	public void updateSpediteur(SpediteurDto spediteurDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (spediteurDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("spediteurDto == null"));
		}

		Integer iId = spediteurDto.getIId();
		// try {
		Spediteur spediteur = em.find(Spediteur.class, iId);
		if (spediteur == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSpediteurFromSpediteurDto(spediteur, spediteurDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void updateDokumentenlink(DokumentenlinkDto dokumentenlinkDto) {
		if (dokumentenlinkDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("dokumentenlinkDto == null"));
		}
		Integer iId = dokumentenlinkDto.getIId();
		Dokumentenlink dokumentenlink = em.find(Dokumentenlink.class, iId);
		if (dokumentenlink == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setDokumentenlinkFromDokumentenlinkDto(dokumentenlink, dokumentenlinkDto);
	}

	public SpediteurDto spediteurFindByMandantCNrCNamedesspediteursOhneExc(String name, String mandant) {
		try {
			return spediteurFindByMandantCNrCNamedesspediteurs(name, mandant);
		} catch (Throwable e) {
			return null;
		}
	}

	public SpediteurDto spediteurFindByMandantCNrCNamedesspediteurs(String name, String mandant) {
		Query query = em.createNamedQuery("SpediteurfindByMandantSpediteurname");
		query.setParameter(1, mandant);
		query.setParameter(2, name);
		Spediteur s = (Spediteur) query.getSingleResult();
		return assembleSpediteurDto(s);
	}

	public SpediteurDto spediteurFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		// precondition
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iId == null"));
		}

		SpediteurDto spediteurDto = null;
		// try {
		Spediteur spediteur = em.find(Spediteur.class, iId);
		if (spediteur == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		spediteurDto = assembleSpediteurDto(spediteur);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return spediteurDto;
	}

	public DokumentenlinkDto dokumentenlinkFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iId == null"));
		}
		DokumentenlinkDto dokumentenlinkDto = null;
		Dokumentenlink dokumentenlink = em.find(Dokumentenlink.class, iId);
		if (dokumentenlink == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		dokumentenlinkDto = assembleDokumentenlinkDto(dokumentenlink);

		return dokumentenlinkDto;
	}

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNr(String belegartCNr, String mandantCNr) {
		Query query = em.createNamedQuery("DokumentenlinkfindByBelegartCNrMandantCNr");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> c = query.getResultList();
		return assembleDokumentenlinkDtos(c);
	}

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(String belegartCNr,
			String mandantCNr, boolean bPfadAbsolut) {
		Query query = em.createNamedQuery("DokumentenlinkfindByBelegartCNrMandantCNrBPfadabsolut");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, mandantCNr);
		query.setParameter(3, Helper.boolean2Short(bPfadAbsolut));
		Collection<?> c = query.getResultList();
		return assembleDokumentenlinkDtos(c);
	}

	/**
	 * Eine Liste aller Spediteure fuer einen bestimmten Mandanten holen.
	 * 
	 * @param ssMandantI String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map<Integer, String> spediteurFindAllByMandant(String ssMandantI) throws EJBExceptionLP {
		TreeMap<Integer, String> t = new TreeMap<Integer, String>();
		Query query = em.createNamedQuery("SpediteurfindAllByMandant");
		query.setParameter(1, ssMandantI);
		Collection<?> c = query.getResultList();
		// if(c.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Spediteur spediteur = ((Spediteur) iter.next());
			Integer spediteurKey = spediteur.getIId();
			String spediteurValue = spediteur.getCNamedesspediteurs();
			t.put(spediteurKey, spediteurValue);
		}
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
		return t;
	}

	private void setSpediteurFromSpediteurDto(Spediteur spediteur, SpediteurDto spediteurDto) {
		spediteur.setMandantCNr(spediteurDto.getMandantCNr());
		spediteur.setCNamedesspediteurs(spediteurDto.getCNamedesspediteurs());
		spediteur.setPersonalIIdAendern(spediteurDto.getPersonalIIdAendern());
		spediteur.setBVersteckt(spediteurDto.getBVersteckt());
		spediteur.setTAendern(new Timestamp(System.currentTimeMillis()));
		spediteur.setCEmail(spediteurDto.getCEmail());
		spediteur.setPartnerIId(spediteurDto.getPartnerIId());
		spediteur.setAnsprechpartnerIId(spediteurDto.getAnsprechpartnerIId());
		spediteur.setCVerkehrszweig(spediteurDto.getCVerkehrszweig());
		em.merge(spediteur);
		em.flush();
	}

	private void setDokumentenlinkFromDokumentenlinkDto(Dokumentenlink dokumentenlink,
			DokumentenlinkDto dokumentenlinkDto) {
		dokumentenlink.setMandantCNr(dokumentenlinkDto.getMandantCNr());
		dokumentenlink.setCOrdner(dokumentenlinkDto.getCOrdner());
		dokumentenlink.setCBasispfad(dokumentenlinkDto.getCBasispfad());
		dokumentenlink.setBelegartCNr(dokumentenlinkDto.getBelegartCNr());
		dokumentenlink.setCMenuetext(dokumentenlinkDto.getCMenuetext());
		dokumentenlink.setBPfadabsolut(dokumentenlinkDto.getBPfadabsolut());
		dokumentenlink.setBUrl(dokumentenlinkDto.getBUrl());
		dokumentenlink.setBPfadAusArbeitsplatzparameter(dokumentenlinkDto.getBPfadAusArbeitsplatzparameter());
		dokumentenlink.setBTitel(dokumentenlinkDto.getBTitel());
		dokumentenlink.setRechtCNr(dokumentenlinkDto.getRechtCNr());
		em.merge(dokumentenlink);
		em.flush();
	}

	private SpediteurDto assembleSpediteurDto(Spediteur spediteur) {
		return SpediteurDtoAssembler.createDto(spediteur);
	}

	private DokumentenlinkDto assembleDokumentenlinkDto(Dokumentenlink spediteur) {
		return DokumentenlinkDtoAssembler.createDto(spediteur);
	}

	// private SpediteurDto[] assembleSpediteurDtos(Collection<?> spediteurs) {
	// List<SpediteurDto> list = new ArrayList<SpediteurDto>();
	// if (spediteurs != null) {
	// Iterator<?> iterator = spediteurs.iterator();
	// while (iterator.hasNext()) {
	// Spediteur spediteur = (Spediteur) iterator.next();
	// list.add(assembleSpediteurDto(spediteur));
	// }
	// }
	// SpediteurDto[] returnArray = new SpediteurDto[list.size()];
	// return list.toArray(returnArray);
	// }

	private DokumentenlinkDto[] assembleDokumentenlinkDtos(Collection<?> dokumentenlinks) {
		List<DokumentenlinkDto> list = new ArrayList<DokumentenlinkDto>();
		if (dokumentenlinks != null) {
			Iterator<?> iterator = dokumentenlinks.iterator();
			while (iterator.hasNext()) {
				Dokumentenlink spediteur = (Dokumentenlink) iterator.next();
				list.add(assembleDokumentenlinkDto(spediteur));
			}
		}
		DokumentenlinkDto[] returnArray = new DokumentenlinkDto[list.size()];
		return list.toArray(returnArray);
	}

	private void checkSpediteurDto(SpediteurDto spediteurDto) throws EJBExceptionLP {
		if (spediteurDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("spediteurDto == null"));
		}
	}

	public int istMandatsreferenzAbgelaufen(Integer zahlungszielIId, Integer kundeIId, Timestamp tBelegdatum,
			TheClientDto theClientDto) {
		int iReturn = MANDATSREFERENZ_GUELTIG;

		ZahlungszielDto zzDto = zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);

		if (Helper.short2boolean(zzDto.getBLastschrift())) {

			String cGlaeubiger = mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getCGlauebiger();

			if (cGlaeubiger == null || cGlaeubiger.length() == 0) {
				iReturn = MANDATSREFERENZ_KEINE_GLAEUBIGERNUMMER;
			} else {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

				try {
					PartnerbankDto[] bankverbindungDto = getBankFac()
							.partnerbankFindByPartnerIIdOhneExc(kundeDto.getPartnerIId(), theClientDto);

					if (bankverbindungDto != null && bankverbindungDto.length > 0) {

						if (bankverbindungDto[0].getCSepamandatsnummer() == null
								|| bankverbindungDto[0].getCSepamandatsnummer().length() == 0) {
							iReturn = MANDATSREFERENZ_KEINE_MANDATSREFERENZNUMMER;
						} else {

							if (bankverbindungDto[0].getTSepaerteilt() == null) {
								iReturn = MANDATSREFERENZ_KEIN_GUELTIGKEITSDATUM;
							} else {

								ParametermandantDto parameter = null;
								parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_GUELTIGKEIT_MANDATSREFERENZ);
								Integer iGueltigkeitMonate = (Integer) parameter.getCWertAsObject();

								Calendar c = Calendar.getInstance();

								c.setTimeInMillis(tBelegdatum.getTime());

								c.add(Calendar.MONTH, -iGueltigkeitMonate.intValue());

								java.sql.Date dAnfang = new java.sql.Date(c.getTimeInMillis());

								// Nachsehen ob dazwischen eine Rechnung gelegt
								// wurde

								String sQuery = "select re FROM FLRRechnung re WHERE re.status_c_nr<>'"
										+ LocaleFac.STATUS_STORNIERT + "' AND re.d_belegdatum>='"
										+ Helper.formatDateWithSlashes(dAnfang) + "' AND re.d_belegdatum<'"
										+ Helper.formatDateWithSlashes(new java.sql.Date(tBelegdatum.getTime()))
										+ "' AND re.flrkunde=" + kundeIId;

								Session session = FLRSessionFactory.getFactory().openSession();

								org.hibernate.Query q = session.createQuery(sQuery);
								q.setMaxResults(1);

								List<?> resultList = q.list();

								Iterator<?> resultListIterator = resultList.iterator();

								if (resultListIterator.hasNext()) {
									// OK
								} else {

									if (bankverbindungDto[0].getTSepaerteilt().before(dAnfang)) {
										iReturn = MANDATSREFERENZ_ABGELAUFEN;
									}
								}

								session.close();

							}

						}

					} else {
						iReturn = MANDATSREFERENZ_KEIN_BANKVERBINDUNG;
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		return iReturn;
	}

	// *** Zahlungsziel
	// ***********************************************************

	/**
	 * stcrud: 2 create ...
	 * 
	 * @param oZahlungszielDtoI ZahlungszielDto
	 * @param theClientDto      String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer createZahlungsziel(ZahlungszielDto oZahlungszielDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		myLogger.entry();

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrUserI == null"));
		}
		if (oZahlungszielDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("oZahlungszielDtoI == null"));
		}

		if (oZahlungszielDtoI.getBVersteckt() == null) {
			oZahlungszielDtoI.setBVersteckt(Helper.boolean2Short(false));
		}
		if (oZahlungszielDtoI.getBInzahlungsvorschlagberuecksichtigen() == null) {
			oZahlungszielDtoI.setBInzahlungsvorschlagberuecksichtigen(Helper.boolean2Short(true));
		}
		if (oZahlungszielDtoI.getBStichtag() == null) {
			oZahlungszielDtoI.setBStichtag(Helper.boolean2Short(false));
		}
		if (oZahlungszielDtoI.getBLastschrift() == null) {
			oZahlungszielDtoI.setBLastschrift(Helper.boolean2Short(false));
		}
		if (oZahlungszielDtoI.getBStichtagMonatsletzter() == null) {
			oZahlungszielDtoI.setBStichtagMonatsletzter(Helper.boolean2Short(false));
		}
		if (oZahlungszielDtoI.getBInzahlungsvorschlagberuecksichtigen() == null) {
			oZahlungszielDtoI.setBInzahlungsvorschlagberuecksichtigen(Helper.boolean2Short(true));
		}
		if (oZahlungszielDtoI.getAnzahlZieltageFuerNetto() == null) {
			oZahlungszielDtoI.setAnzahlZieltageFuerNetto(0);
		}

		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em.createNamedQuery("ZahlungszielfindByCBezMandantCNr");
			query.setParameter(1, oZahlungszielDtoI.getMandantCNr());
			query.setParameter(2, oZahlungszielDtoI.getCBez());
			// Zahlungsziel zahlungsziel = (Zahlungsziel)
			// query.getSingleResult();
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_ZAHLUNGSZIEL.UK"));
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		Integer iIdZahlungszielO = null;
		try {
			iIdZahlungszielO = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ZAHLUNGSZIEL);

			Zahlungsziel zahlungsziel = new Zahlungsziel(iIdZahlungszielO, oZahlungszielDtoI.getMandantCNr(),
					oZahlungszielDtoI.getCBez(), oZahlungszielDtoI.getBVersteckt(),
					oZahlungszielDtoI.getAnzahlZieltageFuerNetto(),
					oZahlungszielDtoI.getBInzahlungsvorschlagberuecksichtigen(), oZahlungszielDtoI.getBStichtag(),
					oZahlungszielDtoI.getBStichtagMonatsletzter(), oZahlungszielDtoI.getBLastschrift());
			em.persist(zahlungsziel);
			em.flush();

			setZahlungszielFromZahlungszielDto(zahlungsziel, oZahlungszielDtoI);

			if (oZahlungszielDtoI.getZahlungszielsprDto() != null) {
				oZahlungszielDtoI.getZahlungszielsprDto().setZahlungszielIId(iIdZahlungszielO);
				createZahlungszielspr(oZahlungszielDtoI.getZahlungszielsprDto());
			}

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iIdZahlungszielO;
	}

	/**
	 * stcrud: 3 remove ...
	 * 
	 * @param zahlungszielDtoI ZahlungszielDto
	 * @param theClientDto     String
	 * @throws EJBExceptionLP
	 */
	public void removeZahlungsziel(ZahlungszielDto zahlungszielDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (zahlungszielDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("zahlungszielDtoI == null"));
		}

		try {
			Query query = em.createNamedQuery("ZahlungszielsprfindByZahlungszielIId");
			query.setParameter(1, zahlungszielDtoI.getIId());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Zahlungszielspr item = (Zahlungszielspr) iter.next();
				em.remove(item);
			}
			Zahlungsziel zahlungsziel = em.find(Zahlungsziel.class, zahlungszielDtoI.getIId());
			if (zahlungsziel == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(zahlungsziel);
			em.flush();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	/**
	 * stcrud: 5 update ...
	 * 
	 * @param zahlungszielDtoI ZahlungszielDto
	 * @param theClientDto     String
	 * @throws EJBExceptionLP
	 */
	public void updateZahlungsziel(ZahlungszielDto zahlungszielDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (zahlungszielDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("zahlungszielDtoI == null"));
		}

		Integer iId = zahlungszielDtoI.getIId();

		// try {
		Zahlungsziel zahlungsziel = em.find(Zahlungsziel.class, iId);
		if (zahlungsziel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("ZahlungszielfindByCBezMandantCNr");
			query.setParameter(1, zahlungszielDtoI.getMandantCNr());
			query.setParameter(2, zahlungszielDtoI.getCBez());
			Zahlungsziel duplicateHelper = (Zahlungsziel) query.getSingleResult();
			Integer iIdVorhanden = duplicateHelper.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_ZAHLUNGSZIEL.UK"));
			}
		} catch (NoResultException ex) {
			// Nothing to do here
		}

		setZahlungszielFromZahlungszielDto(zahlungsziel, zahlungszielDtoI);
		if (zahlungszielDtoI.getZahlungszielsprDto() != null) {
			// -- upd oder create
			if (zahlungszielDtoI.getZahlungszielsprDto().getZahlungszielIId() == null) {
				// create
				// Key(teil) setzen.
				zahlungszielDtoI.getZahlungszielsprDto().setZahlungszielIId(zahlungszielDtoI.getIId());
				createZahlungszielspr(zahlungszielDtoI.getZahlungszielsprDto());
			} else {
				// upd
				updateZahlungszielspr(zahlungszielDtoI.getZahlungszielsprDto());
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * stcrud: 6 findByPrimary ...
	 * 
	 * @param iIdI         Integer
	 * @param theClientDto String
	 * @return ZahlungszielDto
	 * @throws EJBExceptionLP
	 */
	public ZahlungszielDto zahlungszielFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrUserI == null"));
		}
		ZahlungszielDto zahlungszielDto = null;

		Zahlungsziel zahlungsziel = em.find(Zahlungsziel.class, iIdI);
		if (zahlungsziel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		zahlungszielDto = assembleZahlungszielDto(zahlungsziel);

		try {
			Zahlungszielspr zahlungszielspr = em.find(Zahlungszielspr.class,
					new ZahlungszielsprPK(zahlungszielDto.getIId(), theClientDto.getLocUiAsString()));
			if (zahlungszielspr != null) {
				zahlungszielDto.setZahlungszielsprDto(assembleZahlungszielsprDto(zahlungszielspr));
			}

		} catch (Throwable t) {
			zahlungszielDto.setZahlungszielsprDto(null);
		}
		return zahlungszielDto;
	}

	@Override
	public ZahlungszielDto zahlungszielFindByCBezMandantNull(String cbez, String mandantCnr,
			TheClientDto theClientDto) {
		Validator.notEmpty(cbez, "cbez");
		Validator.notEmpty(mandantCnr, "mandantCnr");
		Validator.notNull(theClientDto, "theClientDto");

		try {
			Query query = em.createNamedQuery("ZahlungszielfindByCBezMandantCNr");
			query.setParameter(1, mandantCnr);
			query.setParameter(2, cbez);
			Zahlungsziel z = (Zahlungsziel) query.getSingleResult();
			return zahlungszielFindByPrimaryKey(z.getIId(), theClientDto);
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		return null;
	}

	/**
	 * Eine Liste aller Zahlungsziele fuer einen bestimmten Mandanten holen.
	 * 
	 * @param sMandantI String
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, String> zahlungszielFindAllByMandant(String sMandantI) throws EJBExceptionLP {
		TreeMap<Integer, String> t = new TreeMap<Integer, String>();
		// try {
		Query query = em.createNamedQuery("ZahlungszielfindAllByMandant");
		query.setParameter(1, sMandantI);
		Collection<?> c = query.getResultList();
		// if(c.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Zahlungsziel zahlungsziel = ((Zahlungsziel) iter.next());
			Integer key = zahlungsziel.getIId();
			String value = zahlungsziel.getCBez();
			t.put(key, value);
		}
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return t;
	}

	/**
	 * Alle Zahlungsziel-Dtos fuer einen bestimmten Mandanten holen.
	 * 
	 * @param sMandantI    String
	 * @param theClientDto String
	 * @return Map
	 * @throws EJBExceptionLP
	 */
	public Map<Integer, ZahlungszielDto> zahlungszielFindAllByMandantAsDto(String sMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LinkedHashMap<Integer, ZahlungszielDto> t = new LinkedHashMap<Integer, ZahlungszielDto>();
		Query query = em.createNamedQuery("ZahlungszielfindAllByMandant");
		query.setParameter(1, sMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		ZahlungszielDto[] c = assembleZahlungszielDtos(cl);
		for (int i = 0; i < c.length; i++) {
			t.put(c[i].getIId(), c[i]);
		}
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return t;
	}

	private void setZahlungszielFromZahlungszielDto(Zahlungsziel zahlungsziel, ZahlungszielDto zahlungszielDto) {
		zahlungsziel.setMandantCNr(zahlungszielDto.getMandantCNr());
		zahlungsziel.setCBez(zahlungszielDto.getCBez());
		zahlungsziel.setIAnzahlzieltagefuernetto(zahlungszielDto.getAnzahlZieltageFuerNetto());
		zahlungsziel.setNSkontoprozentsatz1(zahlungszielDto.getSkontoProzentsatz1());
		zahlungsziel.setISkontoanzahltage1(zahlungszielDto.getSkontoAnzahlTage1());
		zahlungsziel.setNSkontoprozentsatz2(zahlungszielDto.getSkontoProzentsatz2());
		zahlungsziel.setISkontoanzahltage2(zahlungszielDto.getSkontoAnzahlTage2());
		zahlungsziel.setBVersteckt(zahlungszielDto.getBVersteckt());
		zahlungsziel.setBInzahlungsvorschlagberuecksichtigen(zahlungszielDto.getBInzahlungsvorschlagberuecksichtigen());

		zahlungsziel.setBStichtag(zahlungszielDto.getBStichtag());
		zahlungsziel.setBStichtagMonatsletzter(zahlungszielDto.getBStichtagMonatsletzter());
		zahlungsziel.setIFolgemonat(zahlungszielDto.getIFolgemonat());
		zahlungsziel.setIStichtag(zahlungszielDto.getIStichtag());

		zahlungsziel.setBLastschrift(zahlungszielDto.getBLastschrift());

		zahlungsziel.setIFolgemonatSkontotage1(zahlungszielDto.getIFolgemonatSkontotage1());
		zahlungsziel.setIFolgemonatSkontotage2(zahlungszielDto.getIFolgemonatSkontotage2());
		zahlungsziel.setNAnzahlungProzent(zahlungszielDto.getNAnzahlungProzent());

		em.merge(zahlungsziel);
		em.flush();
	}

	private ZahlungszielDto assembleZahlungszielDto(Zahlungsziel zahlungsziel) {
		return ZahlungszielDtoAssembler.createDto(zahlungsziel);
	}

	private ZahlungszielDto[] assembleZahlungszielDtos(Collection<?> zahlungsziels) {
		List<ZahlungszielDto> list = new ArrayList<ZahlungszielDto>();
		if (zahlungsziels != null) {
			Iterator<?> iterator = zahlungsziels.iterator();
			while (iterator.hasNext()) {
				Zahlungsziel zahlungsziel = (Zahlungsziel) iterator.next();
				list.add(assembleZahlungszielDto(zahlungsziel));
			}
		}
		ZahlungszielDto[] returnArray = new ZahlungszielDto[list.size()];
		return list.toArray(returnArray);
	}

	// *** Mandant
	// ****************************************************************
	public String createMandant(MandantDto mandantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (mandantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("mandantDtoI == null"));
		}
		if (mandantDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("mandantDtoI.getCNr() == null"));
		}
		if (mandantDtoI.getAnwenderDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("mandantDtoI.getAnwenderDto() == null"));
		}
		if (mandantDtoI.getAnwenderDto().getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("mandantDtoI.getAnwenderDto().getIId() == null"));
		}

		Integer iIdPartner = null;
		try {
			if (mandantDtoI.getPartnerDto().getIId() == null) {
				// Partner.
				iIdPartner = getPartnerFac().createPartner(mandantDtoI.getPartnerDto(), theClientDto);
			} else {
				iIdPartner = mandantDtoI.getPartnerDto().getIId();
			}

			if (mandantDtoI.getPartnerIIdLieferadresse() == null) {
				mandantDtoI.setPartnerIIdLieferadresse(iIdPartner);
			}

			if (mandantDtoI.getIMaxpersonen() == null) {
				mandantDtoI.setIMaxpersonen(100);
			}

			// verbinde Partner mit Mandanten.
			mandantDtoI.setPartnerIId(iIdPartner);

			// Partner lesen wegen generierter Daten.
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartner, theClientDto);
			mandantDtoI.setPartnerDto(partnerDto);

			// Mandant.
			// befuelle Felder am Server.
			// JO: beim Testaufbau ist das Personal noch nicht angelegt!
			// mandantDtoI.setIAendern(theClientDto.getIDPersonal());
			// mandantDtoI.setIAnlegen(theClientDto.getIDPersonal());
			mandantDtoI.setIAendern(mandantDtoI.getIAendern());
			mandantDtoI.setIAnlegen(mandantDtoI.getIAnlegen());

			Mandant mandant = new Mandant(mandantDtoI.getCNr(), mandantDtoI.getCKbez(), mandantDtoI.getPartnerIId(),
					mandantDtoI.getWaehrungCNr(), mandantDtoI.getIAnlegen(), mandantDtoI.getIAendern(),
					mandantDtoI.getSpediteurIIdKunde(), mandantDtoI.getLieferartIIdKunde(),
					mandantDtoI.getZahlungszielIIdKunde(), mandantDtoI.getVkpfArtikelpreislisteIId(),
					mandantDtoI.getMwstsatzbezIIdStandardinlandmwstsatz(),
					mandantDtoI.getMwstsatzbezIIdStandardauslandmwstsatz(), mandantDtoI.getLieferartIIdLF(),
					mandantDtoI.getSpediteurIIdLF(), mandantDtoI.getZahlungszielIIdLF(),
					mandantDtoI.getPartnerIIdLieferadresse(), mandantDtoI.getIMaxpersonen());
			em.persist(mandant);
			em.flush();

			mandantDtoI.setBAgbAnfrage(mandant.getBAgbAnfrage());
			mandantDtoI.setBAgbAngebot(mandant.getBAgbAngebot());
			mandantDtoI.setBAgbAnhang(mandant.getBAgbAnhang());
			mandantDtoI.setBAgbAuftrag(mandant.getBAgbAuftrag());
			mandantDtoI.setBAgbBestellung(mandant.getBAgbBestellung());
			mandantDtoI.setBAgbLieferschein(mandant.getBAgbLieferschein());
			mandantDtoI.setBAgbRechnung(mandant.getBAgbRechnung());

			mandantDtoI.setBPreislisteFuerNeukunde(mandant.getBPreislisteFuerNeukunde());

			mandantDtoI.setTAendern(mandant.getTAendern());
			mandantDtoI.setTAnlegen(mandant.getTAnlegen());

			if (!mandantDtoI.getCNr().equals(MandantFac.URMANDANT_C_NR)) {
				setMussFelder(mandantDtoI, theClientDto);
			}

			// Mandant update.
			setMandantFromMandantDto(mandant, mandantDtoI);

			// Anwedner update.
			if (mandantDtoI.getAnwenderDto().getMandantCNrHauptmandant() != null) {
				getSystemFac().updateAnwender(mandantDtoI.getAnwenderDto());
			}

			// CK: Lager KEIN_LAGER anlegen, weil bei jedem Mandant ein Lager
			// KEIN_LAGER vorhanden sein muss
			LagerDto lagerDto = new LagerDto();
			lagerDto.setCNr(LagerFac.LAGER_KEINLAGER);
			lagerDto.setLagerartCNr(LagerFac.LAGERART_NORMAL);
			lagerDto.setBBestellvorschlag(Helper.boolean2Short(false));
			lagerDto.setBInternebestellung(Helper.boolean2Short(false));
			lagerDto.setBVersteckt(Helper.boolean2Short(false));
			lagerDto.setBKonsignationslager(Helper.boolean2Short(false));
			lagerDto.setMandantCNr(mandantDtoI.getCNr());

			getLagerFac().createLager(lagerDto);
			// CK: Lager "Hauptlager" anlegen, weil bei jedem Mandant ein
			// Hauptlager vorhanden sein muss (WH)
			lagerDto = new LagerDto();
			lagerDto.setCNr(
					getTextRespectUISpr("artikel.hauptlager", theClientDto.getMandant(), theClientDto.getLocUi()));
			lagerDto.setLagerartCNr(LagerFac.LAGERART_HAUPTLAGER);
			lagerDto.setBBestellvorschlag(Helper.boolean2Short(true));
			lagerDto.setBInternebestellung(Helper.boolean2Short(true));
			lagerDto.setBVersteckt(Helper.boolean2Short(false));
			lagerDto.setBKonsignationslager(Helper.boolean2Short(false));
			lagerDto.setMandantCNr(mandantDtoI.getCNr());
			getLagerFac().createLager(lagerDto);
			// CK: PJ10809 Es muss ein Lager Wertgutschrift geben
			lagerDto = new LagerDto();
			lagerDto.setCNr(LagerFac.LAGER_WERTGUTSCHRIFT);
			lagerDto.setLagerartCNr(LagerFac.LAGERART_WERTGUTSCHRIFT);
			lagerDto.setBBestellvorschlag(Helper.boolean2Short(true));
			lagerDto.setBInternebestellung(Helper.boolean2Short(true));
			lagerDto.setBVersteckt(Helper.boolean2Short(false));
			lagerDto.setBKonsignationslager(Helper.boolean2Short(false));
			lagerDto.setMandantCNr(mandantDtoI.getCNr());
			getLagerFac().createLager(lagerDto);

			// SP1011
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			personalDto.setIId(null);
			personalDto.setCAusweis(null);
			personalDto.setMandantCNr(mandantDtoI.getCNr());
			getPersonalFac().createPersonal(personalDto, theClientDto);

			createReversechargeartenFuerNeuenMandanten(mandantDtoI.getCNr(), theClientDto);

			// PJ22120 Automatikjobs fuer neuen Mandanten eintragen
			createAutomatikjobsFuerNeuenMandanten(mandantDtoI.getCNr(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, ex);
		}
		return mandantDtoI.getCNr();
	}

	private void createReversechargeartenFuerNeuenMandanten(String mandantCnr, TheClientDto theClientDto) {
		ReversechargeartDto rcartOhneDto = new ReversechargeartDto();
		rcartOhneDto.setCNr(ReversechargeArt.OHNE);
		rcartOhneDto.setMandantCNr(mandantCnr);
		rcartOhneDto.setISort(0);
		rcartOhneDto.setSprDto(new ReversechargeartsprDto());
		rcartOhneDto.getSprDto().setcBez("Ohne Reverse Charge");
		rcartOhneDto.getSprDto().setLocaleCNr(theClientDto.getLocUiAsString());
		getFinanzServiceFac().createReversechargeart(rcartOhneDto, theClientDto);

		ReversechargeartDto rcartIgDto = new ReversechargeartDto();
		rcartIgDto.setCNr(ReversechargeArt.IG);
		rcartIgDto.setMandantCNr(mandantCnr);
		rcartIgDto.setISort(1);
		rcartIgDto.setBVersteckt(Boolean.TRUE);
		rcartIgDto.setSprDto(new ReversechargeartsprDto());
		rcartIgDto.getSprDto().setcBez("Innergemeinschaftlich");
		rcartIgDto.getSprDto().setLocaleCNr(theClientDto.getLocUiAsString());
		getFinanzServiceFac().createReversechargeart(rcartIgDto, theClientDto);
	}

	private void createAutomatikjobsFuerNeuenMandanten(String mandantCnr, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		String oldMandantCnr = theClientDto.getMandant();
		AutomatikjobDto[] jobs = getAutomatikjobFac().automatikjobFindAllByCMandantCNr(oldMandantCnr);
		for (AutomatikjobDto job : jobs) {
			// Neuen Mandant setzen und auf nicht aktiviert stellen
			job.setCMandantCNr(mandantCnr);
			job.setBActive(0);
			// IId auf null setzen, dadurch wird neuer Automatikjob angelegt
			job.setIId(null);
			getAutomatikjobFac().createAutomatikjob(job);
		}
	}

	private void setMussFelder(MandantDto mandantDtoI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {

		if (mandantDtoI.getCNr().equals(MandantFac.URMANDANT_C_NR)) {
			throw new IllegalArgumentException("Mandant = Urmandant");
		}

		// Urmandat lesen und neue Mandantmussfelder vorbelegen.
		MandantDto urmandant = mandantFindByPrimaryKey(MandantFac.URMANDANT_C_NR, theClientDto);

		// Die Kostenstelle des Urmandanten wird kopiert und dem neuen Mandanten
		// zugewiesen
		KostenstelleDto k = getSystemFac().kostenstelleFindByPrimaryKey(urmandant.getIIdKostenstelle());
		k.setMandantCNr(mandantDtoI.getCNr());
		// SP3713 Sachkonto nicht mitkopieren, da Konto-IId mandantenabhaengig
		k.setKontoIId(null);
		Integer ik = getSystemFac().createKostenstelle(k, theClientDto);
		mandantDtoI.setIIdKostenstelle(ik);

		// Die Lieferart des Urmandanten wird kopiert und dem neuen Mandanten
		// zugewiesen
		if (urmandant.getLieferartIIdKunde() != null) {
			LieferartDto lieferartDtoKunde = getLocaleFac().lieferartFindByPrimaryKey(urmandant.getLieferartIIdKunde(),
					theClientDto);
			lieferartDtoKunde.setIId(null);
			lieferartDtoKunde.setMandantCNr(mandantDtoI.getCNr());
			// Datensaetze duplizieren
			Integer lieferartIIdKunde = getLocaleFac().createLieferart(lieferartDtoKunde, theClientDto);
			mandantDtoI.setLieferartIIdKunde(lieferartIIdKunde);
			mandantDtoI.setLieferartIIdLF(lieferartIIdKunde);
		}

		// Spediteur Kunde neuer Mandant bekommt die vom Urmandanten vorbelegt
		SpediteurDto sk = getMandantFac().spediteurFindByPrimaryKey(urmandant.getSpediteurIIdKunde());
		sk.setMandantCNr(mandantDtoI.getCNr());
		Integer isk = getMandantFac().createSpediteur(sk, theClientDto);
		mandantDtoI.setSpediteurIIdKunde(isk);

		// Spediteur LF neuer Mandant bekommt die vom Urmandanten vorbelegt
		mandantDtoI.setSpediteurIIdLF(isk);

		// Zahlungsziel Kunde neuer Mandant bekommt die vom Urmandanten
		// vorbelegt
		ZahlungszielDto zk = zahlungszielFindByPrimaryKey(urmandant.getZahlungszielIIdKunde(), theClientDto);
		zk.setMandantCNr(mandantDtoI.getCNr());
		Integer izk = getMandantFac().createZahlungsziel(zk, theClientDto);
		mandantDtoI.setZahlungszielIIdKunde(izk);

		// Zahlungsziel LF neuer Mandant bekommt die vom Urmandanten vorbelegt
		mandantDtoI.setZahlungszielIIdLF(izk);

		// VKPFPreisliste neuer Mandant bekommt die vom Urmandanten vorbelegt
		VkpfartikelpreislisteDto vkpl = getVkPreisfindungFac()
				.vkpfartikelpreislisteFindByPrimaryKey(urmandant.getVkpfArtikelpreislisteIId());
		vkpl.setMandantCNr(mandantDtoI.getCNr());
		vkpl.setWaehrungCNr(mandantDtoI.getWaehrungCNr());
		Integer iVKPL = getVkPreisfindungFac().createVkpfartikelpreisliste(vkpl, theClientDto);
		mandantDtoI.setVkpfArtikelpreislisteIId(iVKPL);

		// Standard MWST-Satz: Werte vom Urmandanten werden kopiert und
		// vorbelegt.
		try {
			MwstsatzbezDto mwstbezDtoUrmandant = mwstsatzbezFindByPrimaryKey(
					urmandant.getMwstsatzbezIIdStandardinlandmwstsatz(), theClientDto);
			// modifizieren und fuer den neuen Mandanten anlegen.
			mwstbezDtoUrmandant.setIId(null);

			// SP3212
			mwstbezDtoUrmandant.setFinanzamtIId(null);

			mwstbezDtoUrmandant.setMandantCNr(mandantDtoI.getCNr());
			Integer iIdMwstbezNeuerMandant = createMwstsatzbez(mwstbezDtoUrmandant, theClientDto);
			// Es muss auch der Steuersatz ein 2. mal angelegt werden :-(
			Map<Integer, MwstsatzDto> mwst = mwstsatzFindAllByMandantAsDto(urmandant.getCNr(), theClientDto);
			// die sind nach Gueltigkeitsdatum sortiert, wir brauchen den
			// letzten = den aktuellsten.
			if (mwst.size() > 0) {
				for (Iterator<Integer> iter = mwst.keySet().iterator(); iter.hasNext();) {
					Integer key = iter.next();
					MwstsatzDto item = mwst.get(key);
					// wenn ich den letzten hab
					if (!iter.hasNext()) {
						// Dto anpassen.
						item.setIId(null);
						item.setIIMwstsatzbezId(iIdMwstbezNeuerMandant);
						// MR: item.setMwstsatzbezDto nicht setzen, sonst wird
						// die Mwstsatbez nochmals neu angelegt.
						// und als neuen Datensatz speichern.
						createMwstsatz(item, theClientDto);
					}
				}
			}
			mandantDtoI.setMwstsatzbezIIdStandardinlandmwstsatz(iIdMwstbezNeuerMandant);
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SYSTEM_MWSTSATZ_IST_NULL, new Exception("MWSTSATZ == null"));
		}
	}

	public void removeMandant(String cNr) throws EJBExceptionLP {
		Mandant toRemove = em.find(Mandant.class, cNr);
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

	public void removeMandant(MandantDto mandantDtoI) throws EJBExceptionLP {
		if (mandantDtoI != null) {
			String cNr = mandantDtoI.getCNr();
			removeMandant(cNr);
		}
	}

	// alt
	// public void removeMandant(String cNr)
	// throws EJBExceptionLP {
	// try {
	// mandantHome.remove(cNr);
	// }
	// catch (Throwable t) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new
	// Exception(t));
	// }
	// }
	//
	// alt
	// public void removeMandant(MandantDto mandantDtoI)
	// throws EJBExceptionLP {
	// if (mandantDtoI != null) {
	// String cNr = mandantDtoI.getCNr();
	// removeMandant(cNr);
	// }
	// }

	public void updateMandant(MandantDto mandantDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (mandantDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("mandantDtoI == null"));
		}
		if (mandantDtoI.getPartnerDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mandantDtoI.getPartnerDto() == null"));
		}
		if (mandantDtoI.getAnwenderDto() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mandantDtoI.getAnwenderDto() == null"));
		}

		String cNr = mandantDtoI.getCNr();
		try {
			// Mandant update.
			Mandant mandant = em.find(Mandant.class, cNr);
			if (mandant == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// Aenderer wird nur hier gesetzt.
			mandant.setPersonalIIdAendern(theClientDto.getIDPersonal());
			// Aenderertimestamp wird nur hier gesetzt.
			mandant.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			setMandantFromMandantDto(mandant, mandantDtoI);

			// Partner update.
			getPartnerFac().updatePartner(mandantDtoI.getPartnerDto(), theClientDto);

			// Anwendner update.
			// getSystemFac().updateAnwender(mandantDtoI.getAnwenderDto());
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public MandantDto mandantFindByPrimaryKey(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		MandantDto mandantDto = null;

		// precondition
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception("cNrI == null"));
		}

		try {
			Mandant mandant = em.find(Mandant.class, cNrI);
			if (mandant == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			mandantDto = assembleMandantDto(mandant);

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(mandantDto.getPartnerIId(), theClientDto);
			mandantDto.setPartnerDto(partnerDto);

			AnwenderDto anwenderDto = getSystemFac()
					.anwenderFindByPrimaryKey(new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));
			mandantDto.setAnwenderDto(anwenderDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return mandantDto;
	}

	private void setMandantFromMandantDto(Mandant mandant, MandantDto mandantDto) {
		mandant.setCKbez(mandantDto.getCKbez());
		mandant.setPartnerIId(mandantDto.getPartnerIId());
		mandant.setWaehrungCNr(mandantDto.getWaehrungCNr());
		mandant.setSpediteurIIdKunde(mandantDto.getSpediteurIIdKunde());
		mandant.setLieferartIIdKunde(mandantDto.getLieferartIIdKunde());
		mandant.setZahlungszielIIdKunde(mandantDto.getZahlungszielIIdKunde());
		mandant.setSpediteurIIdLieferant(mandantDto.getSpediteurIIdLF());
		mandant.setLieferartIIdLieferant(mandantDto.getLieferartIIdLF());
		mandant.setZahlungszielIIdLieferant(mandantDto.getZahlungszielIIdLF());
		mandant.setVkpfartikelpreislisteIId(mandantDto.getVkpfArtikelpreislisteIId());
		mandant.setKostenstelleIId(mandantDto.getIIdKostenstelle());
		mandant.setBankverbindungIIdMandant(mandantDto.getIBankverbindung());
		mandant.setMwstsatzIIdStandardinlandmwstsatz(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());
		mandant.setMwstsatzIIdStandardauslandmwstsatz(mandantDto.getMwstsatzbezIIdStandardauslandmwstsatz());
		mandant.setMwstsatzIIdStandarddrittlandmwstsatz(mandantDto.getMwstsatzbezIIdStandarddrittlandmwstsatz());
		mandant.setPartnerIIdLieferadresse(mandantDto.getPartnerIIdLieferadresse());
		mandant.setIBenutzerMax(mandantDto.getIBenutzermax());
		mandant.setKundeIIdStueckliste(mandantDto.getKundeIIdStueckliste());
		mandant.setPartnerIIdFinanzamt(mandantDto.getPartnerIIdFinanzamt());
		mandant.setJahreRueckdatierbar(
				mandantDto.getJahreRueckdatierbar() == null ? 1 : mandantDto.getJahreRueckdatierbar());
		mandant.setKostenstelleIIdFibu(mandantDto.getKostenstelleIIdFibu());
		mandant.setLagerIIdZiellager(mandantDto.getLagerIIdZiellager());
		mandant.setCGlauebiger(mandantDto.getCGlauebiger());
		mandant.setWaehrungCNrZusaetzlich(mandantDto.getWaehrungCNrZusaetzlich());
		mandant.setVerrechnungsmodellIId(mandantDto.getVerrechnungsmodellIId());
		mandant.setZahlungszielIIdAnzahlung(mandantDto.getZahlungszielIIdAnzahlung());

		mandant.setBAgbAnfrage(mandantDto.getBAgbAnfrage());
		mandant.setBAgbAngebot(mandantDto.getBAgbAngebot());
		mandant.setBAgbAnhang(mandantDto.getBAgbAnhang());
		mandant.setBAgbAuftrag(mandantDto.getBAgbAuftrag());
		mandant.setBAgbBestellung(mandantDto.getBAgbBestellung());
		mandant.setBAgbLieferschein(mandantDto.getBAgbLieferschein());
		mandant.setBAgbRechnung(mandantDto.getBAgbRechnung());

		mandant.setBPreislisteFuerNeukunde(mandantDto.getBPreislisteFuerNeukunde());
		mandant.setIntrastatRegion(mandantDto.getCIntrastatRegion());
		em.merge(mandant);
		em.flush();
	}

	private MandantDto assembleMandantDto(Mandant mandant) {
		return MandantDtoAssembler.createDto(mandant);
	}

	private MandantDto[] assembleMandantDtos(Collection<?> mandants) {
		List<MandantDto> list = new ArrayList<MandantDto>();
		if (mandants != null) {
			Iterator<?> iterator = mandants.iterator();
			while (iterator.hasNext()) {
				Mandant mandant = (Mandant) iterator.next();
				list.add(assembleMandantDto(mandant));
			}
		}
		MandantDto[] returnArray = new MandantDto[list.size()];
		return list.toArray(returnArray);
	}

	public void createZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto) throws EJBExceptionLP {
		if (zahlungszielsprDto != null) {
			if (zahlungszielsprDto.getCBezeichnung() != null && !zahlungszielsprDto.getCBezeichnung().startsWith(" ")) {
				try {
					Zahlungszielspr zahlungszielspr = new Zahlungszielspr(zahlungszielsprDto.getZahlungszielIId(),
							zahlungszielsprDto.getLocaleCNr());
					em.persist(zahlungszielspr);
					em.flush();
					setZahlungszielsprFromZahlungszielsprDto(zahlungszielspr, zahlungszielsprDto);
				} catch (EntityExistsException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
				}
			}
		}
	}

	public void removeZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto) throws EJBExceptionLP {

		if (zahlungszielsprDto != null) {
			ZahlungszielsprPK zahlungszielsprPK = getZahlungsZielsprPK(zahlungszielsprDto);
			Zahlungszielspr toRemove = em.find(Zahlungszielspr.class, zahlungszielsprPK);
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		}
	}

	public void updateZahlungszielspr(ZahlungszielsprDto zahlungszielsprDto) throws EJBExceptionLP {
		if (zahlungszielsprDto != null) {

			if (zahlungszielsprDto.getCBezeichnung() == null || zahlungszielsprDto.getCBezeichnung().startsWith(" ")) {
				removeZahlungszielspr(zahlungszielsprDto);
			} else {
				ZahlungszielsprPK zahlungszielsprPK = getZahlungsZielsprPK(zahlungszielsprDto);

				Zahlungszielspr zahlungszielspr = em.find(Zahlungszielspr.class, zahlungszielsprPK);
				if (zahlungszielspr == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				setZahlungszielsprFromZahlungszielsprDto(zahlungszielspr, zahlungszielsprDto);
			}
		}
	}

	private ZahlungszielsprPK getZahlungsZielsprPK(ZahlungszielsprDto zahlungszielsprDto) {
		ZahlungszielsprPK zahlungszielsprPK = new ZahlungszielsprPK();
		zahlungszielsprPK.setZahlungszielIId(zahlungszielsprDto.getZahlungszielIId());
		zahlungszielsprPK.setLocaleCNr(zahlungszielsprDto.getLocaleCNr());
		return zahlungszielsprPK;
	}

	public ZahlungszielsprDto zahlungszielsprFindByPrimaryKey(Integer zahlungszielIId, String localeCNr)
			throws EJBExceptionLP {
		ZahlungszielsprDto zahlungszielsprDto = null;

		// try {
		ZahlungszielsprPK zahlungszielsprPK = new ZahlungszielsprPK();
		zahlungszielsprPK.setZahlungszielIId(zahlungszielIId);
		zahlungszielsprPK.setLocaleCNr(localeCNr);
		Zahlungszielspr zahlungszielspr = em.find(Zahlungszielspr.class, zahlungszielsprPK);
		if (zahlungszielspr != null) {
			zahlungszielsprDto = assembleZahlungszielsprDto(zahlungszielspr);
		}
		if (zahlungszielsprDto == null) {
			// nothing here, das braucht zuviel Zeit und bringt bei OhneExc
			// nichts
		}
		// }
		// catch (FinderException ex) {
		// nothing here, das braucht zuviel Zeit und bringt bei OhneExc nichts
		// }

		return zahlungszielsprDto;
	}

	public ZahlungszielsprDto zahlungszielsprFindByPrimaryKeyOhneExc(Integer iIdZahlungszielI, String sLocaleCNrI) {
		ZahlungszielsprDto oSprDtoO = null;

		try {
			oSprDtoO = zahlungszielsprFindByPrimaryKey(iIdZahlungszielI, sLocaleCNrI);
		} catch (Throwable t) {
			// nothing here, das braucht zuviel Zeit und bringt bei OhneExc
			// nichts
		}

		return oSprDtoO;
	}

	private void setZahlungszielsprFromZahlungszielsprDto(Zahlungszielspr zahlungszielspr,
			ZahlungszielsprDto zahlungszielsprDto) {
		zahlungszielspr.setCBezeichnung(zahlungszielsprDto.getCBezeichnung());
		em.merge(zahlungszielspr);
		em.flush();
	}

	private ZahlungszielsprDto assembleZahlungszielsprDto(Zahlungszielspr zahlungszielspr) {
		return ZahlungszielsprDtoAssembler.createDto(zahlungszielspr);
	}

	// private ZahlungszielsprDto[] assembleZahlungszielsprDtos(
	// Collection<?> zahlungszielsprs) {
	// List<ZahlungszielsprDto> list = new ArrayList<ZahlungszielsprDto>();
	// if (zahlungszielsprs != null) {
	// Iterator<?> iterator = zahlungszielsprs.iterator();
	// while (iterator.hasNext()) {
	// Zahlungszielspr zahlungszielspr = (Zahlungszielspr) iterator
	// .next();
	// list.add(assembleZahlungszielsprDto(zahlungszielspr));
	// }
	// }
	// ZahlungszielsprDto[] returnArray = new ZahlungszielsprDto[list.size()];
	// return list.toArray(returnArray);
	// }

	public MandantDto mandantFindByPrimaryKeyOhneExc(String cNrMandantI, TheClientDto theClientDto) {
		MandantDto mandantDto = null;

		// precondition
		if (cNrMandantI == null) {
			return null;
		}

		try {
			Mandant mandant = em.find(Mandant.class, cNrMandantI);
			if (mandant == null) {
				myLogger.warn("Not Found: cNrMandantI=" + cNrMandantI);
				return null;
			}
			mandantDto = assembleMandantDto(mandant);

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(mandantDto.getPartnerIId(), theClientDto);
			mandantDto.setPartnerDto(partnerDto);

			AnwenderDto anwenderDto = getSystemFac()
					.anwenderFindByPrimaryKey(new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));
			mandantDto.setAnwenderDto(anwenderDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			myLogger.warn("cNrMandantI=" + cNrMandantI, ex);
			return null;
		}
		return mandantDto;
	}

	public MandantDto[] mandantFindAll(TheClientDto theClientDto) {
		MandantDto[] mandantDtos = null;
		try {
			Query query = em.createNamedQuery("MandantfindAlleMandanten");
			mandantDtos = assembleMandantDtos(query.getResultList());
		} catch (Throwable t) {
			if (theClientDto == null) {
				myLogger.warn("mandantFindAll");
			} else {
				myLogger.warn(theClientDto.getIDUser(), "mandantFindAll");
			}
		}
		return mandantDtos;
	}

	public MandantDto[] mandantFindByPartnerIIdOhneExc(Integer partnerIId, TheClientDto theClientDto) {
		MandantDto[] aMandantDtos = null;
		try {
			Query query = em.createNamedQuery("MandantfindByPartnerIId");
			query.setParameter(1, partnerIId);
			aMandantDtos = assembleMandantDtos(query.getResultList());
		} catch (Throwable t) {
			myLogger.warn(theClientDto.getIDUser(), "" + partnerIId);
		}
		return aMandantDtos;
	}

	public MandantDto[] mandantFindByPartnerIId(Integer partnerIId, TheClientDto theClientDto) {
		MandantDto[] aMandantDtos = null;
		// try {
		Query query = em.createNamedQuery("MandantfindByPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		aMandantDtos = assembleMandantDtos(c);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return aMandantDtos;
	}

	/**
	 * MB: Zieldatum eines Belegs berechnen.
	 * 
	 * @param dBelegdatum     Date
	 * @param zahlungszielIId Integer
	 * @param theClientDto    String
	 * @return Date
	 * @throws EJBExceptionLP
	 */
	public java.sql.Date berechneZielDatumFuerBelegdatum(java.util.Date dBelegdatum, Integer zahlungszielIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zahlungszielIId != null) {
			ZahlungszielDto zahlungszielDto = zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);
			return berechneZielDatumFuerBelegdatum(dBelegdatum, zahlungszielDto, theClientDto);
		} else {
			return new java.sql.Date(dBelegdatum.getTime());
		}
	}

	/**
	 * MB: Zieldatum eines Belegs berechnen.
	 * 
	 * @param dBelegdatum     Date
	 * @param zahlungszielDto Integer
	 * @param theClientDto    String
	 * @return Date
	 * @throws EJBExceptionLP
	 */
	public java.sql.Date berechneZielDatumFuerBelegdatum(java.util.Date dBelegdatum, ZahlungszielDto zahlungszielDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (Helper.short2boolean(zahlungszielDto.getBStichtag()) == true) {
			return berechneFaelligkeitAnhandStichtag(dBelegdatum, zahlungszielDto, theClientDto);
		} else if (zahlungszielDto != null && zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
			return Helper.addiereTageZuDatum(dBelegdatum, zahlungszielDto.getAnzahlZieltageFuerNetto().intValue());
		} else {
			return new java.sql.Date(dBelegdatum.getTime());
		}
	}

	public java.sql.Date berechneFaelligkeitAnhandStichtag(java.util.Date dBelegdatum, ZahlungszielDto zahlungszielDto,
			TheClientDto theClientDto) {

		java.sql.Date dFaellig = new java.sql.Date(dBelegdatum.getTime());
		if (Helper.short2boolean(zahlungszielDto.getBStichtag()) == true) {

			if (zahlungszielDto.getIFolgemonat() != null) {

				Calendar cFaellig = Calendar.getInstance();
				cFaellig.setTime(dBelegdatum);
				int iTagBelegdatum = cFaellig.get(Calendar.DAY_OF_MONTH);

				if (Helper.short2boolean(zahlungszielDto.getBStichtagMonatsletzter())) {
					cFaellig.set(Calendar.DAY_OF_MONTH, 1);

					cFaellig.add(Calendar.MONTH, zahlungszielDto.getIFolgemonat());
					cFaellig.set(Calendar.DAY_OF_MONTH, cFaellig.getActualMaximum(Calendar.DAY_OF_MONTH));
				} else {
					cFaellig.set(Calendar.DAY_OF_MONTH, 1);

					cFaellig.add(Calendar.MONTH, zahlungszielDto.getIFolgemonat());
					cFaellig.set(Calendar.DAY_OF_MONTH, zahlungszielDto.getIStichtag());

					if (iTagBelegdatum > zahlungszielDto.getIStichtag()) {
						cFaellig.add(Calendar.MONTH, 1);
					}

				}

				return new java.sql.Date(cFaellig.getTime().getTime());

			}

		}
		return dFaellig;
	}

	public boolean hatZusatzfunktionberechtigung(String zusatzfunktionCNr, TheClientDto theClientDto) {
		// try {
		Zusatzfunktionberechtigung zusatzfunktionberechtigung = em.find(Zusatzfunktionberechtigung.class,
				new ZusatzfunktionberechtigungPK(zusatzfunktionCNr, theClientDto.getMandant()));
		if (zusatzfunktionberechtigung == null) { // @ToDo null Pruefung?
			return false;
		}
		return true;
		// }
		// catch (FinderException ex) {
		// return false;
		// }
	}

	/**
	 * MB: Berechne das Datum, bis zu dem der Skonto 1 abziehbar ist.
	 * 
	 * @param dBelegdatum     Date
	 * @param zahlungszielIId Integer
	 * @param theClientDto    String
	 * @return Date
	 * @throws EJBExceptionLP
	 */
	public java.sql.Date berechneSkontoTage1FuerBelegdatum(java.sql.Date dBelegdatum, Integer zahlungszielIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ZahlungszielDto zahlungszielDto = zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);

		if (Helper.short2boolean(zahlungszielDto.getBStichtag()) == true) {
			java.sql.Date dFaellig = berechneFaelligkeitAnhandStichtag(dBelegdatum, zahlungszielDto, theClientDto);
			return Helper.addiereTageZuDatum(dFaellig, -zahlungszielDto.getSkontoAnzahlTage1().intValue());
		} else {

			if (zahlungszielDto.getIFolgemonatSkontotage1() != null) {
				Calendar cFaellig = Calendar.getInstance();
				cFaellig.setTime(dBelegdatum);
				int iTagBelegdatum = cFaellig.get(Calendar.DAY_OF_MONTH);

				cFaellig.set(Calendar.DAY_OF_MONTH, 1);

				cFaellig.add(Calendar.MONTH, zahlungszielDto.getIFolgemonatSkontotage1());
				cFaellig.set(Calendar.DAY_OF_MONTH, zahlungszielDto.getSkontoAnzahlTage1());

				if (iTagBelegdatum > zahlungszielDto.getSkontoAnzahlTage1()) {
					cFaellig.add(Calendar.MONTH, 1);
				}

				return new java.sql.Date(cFaellig.getTime().getTime());
			} else {
				return Helper.addiereTageZuDatum(dBelegdatum, zahlungszielDto.getSkontoAnzahlTage1().intValue());
			}

		}

	}

	/**
	 * MB: Berechne das Datum, bis zu dem der Skonto 2 abziehbar ist.
	 * 
	 * @param dBelegdatum     Date
	 * @param zahlungszielIId Integer
	 * @param theClientDto    String
	 * @return Date
	 * @throws EJBExceptionLP
	 */
	public java.sql.Date berechneSkontoTage2Belegdatum(java.sql.Date dBelegdatum, Integer zahlungszielIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ZahlungszielDto zahlungszielDto = zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);

		if (Helper.short2boolean(zahlungszielDto.getBStichtag()) == true) {
			java.sql.Date dFaellig = berechneFaelligkeitAnhandStichtag(dBelegdatum, zahlungszielDto, theClientDto);
			return Helper.addiereTageZuDatum(dFaellig, -zahlungszielDto.getSkontoAnzahlTage2().intValue());
		} else {

			if (zahlungszielDto.getIFolgemonatSkontotage2() != null) {
				Calendar cFaellig = Calendar.getInstance();
				cFaellig.setTime(dBelegdatum);
				int iTagBelegdatum = cFaellig.get(Calendar.DAY_OF_MONTH);

				cFaellig.set(Calendar.DAY_OF_MONTH, 1);

				cFaellig.add(Calendar.MONTH, zahlungszielDto.getIFolgemonatSkontotage2());
				cFaellig.set(Calendar.DAY_OF_MONTH, zahlungszielDto.getSkontoAnzahlTage2());

				if (iTagBelegdatum > zahlungszielDto.getSkontoAnzahlTage2()) {
					cFaellig.add(Calendar.MONTH, 1);
				}

				return new java.sql.Date(cFaellig.getTime().getTime());
			} else {
				return Helper.addiereTageZuDatum(dBelegdatum, zahlungszielDto.getSkontoAnzahlTage2().intValue());
			}
		}

	}

	public void createModulberechtigung(ModulberechtigungDto modulberechtigungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (modulberechtigungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("modulberechtigungDto == null"));
		}
		if (modulberechtigungDto.getBelegartCNr() == null || modulberechtigungDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"modulberechtigungDto.getBelegartCNr() == null || modulberechtigungDto.getMandantCNr() == null"));
		}
		// try {
		Modulberechtigung modulberechtigung = em.find(Modulberechtigung.class,
				new ModulberechtigungPK(modulberechtigungDto.getBelegartCNr(), modulberechtigungDto.getMandantCNr()));
		if (modulberechtigung != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_MODULBERECHTIGUNG.PK"));
		}
		// catch (FinderException ex1) {
		// nothing here
		// }

		try {
			modulberechtigung = new Modulberechtigung(modulberechtigungDto.getBelegartCNr(),
					modulberechtigungDto.getMandantCNr());
			em.persist(modulberechtigung);
			em.flush();
			setModulberechtigungFromModulberechtigungDto(modulberechtigung, modulberechtigungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeModulberechtigung(ModulberechtigungDto modulberechtigungDto) throws EJBExceptionLP {
		myLogger.entry();
		if (modulberechtigungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("modulberechtigungDto == null"));
		}
		if (modulberechtigungDto.getBelegartCNr() == null || modulberechtigungDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"modulberechtigungDto.getBelegartCNr() == null || modulberechtigungDto.getMandantCNr() == null"));
		}

		if (modulberechtigungDto != null) {
			try {
				Modulberechtigung modulberechtigung = em.find(Modulberechtigung.class, new ModulberechtigungPK(
						modulberechtigungDto.getBelegartCNr(), modulberechtigungDto.getMandantCNr()));
				if (modulberechtigung == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				em.remove(modulberechtigung);
				em.flush();
			} catch (EntityExistsException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
			}
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public ModulberechtigungDto modulberechtigungFindByPrimaryKey(String belegartCNr, String mandantCNr)
			throws EJBExceptionLP {
		if (belegartCNr == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartCNr == null || mandantCNr == null"));
		}
		// try {
		ModulberechtigungPK modulberechtigungPK = new ModulberechtigungPK();
		modulberechtigungPK.setBelegartCNr(belegartCNr);
		modulberechtigungPK.setMandantCNr(mandantCNr);
		Modulberechtigung modulberechtigung = em.find(Modulberechtigung.class, modulberechtigungPK);
		if (modulberechtigung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleModulberechtigungDto(modulberechtigung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public boolean darfAnwenderAufModulZugreifen(String belegart, TheClientDto theClientDto) {
		ModulberechtigungPK modulberechtigungPK = new ModulberechtigungPK();
		modulberechtigungPK.setBelegartCNr(belegart);
		modulberechtigungPK.setMandantCNr(theClientDto.getMandant());
		Modulberechtigung modulberechtigung = em.find(Modulberechtigung.class, modulberechtigungPK);

		return modulberechtigung != null;
	}

	public ModulberechtigungDto modulberechtigungFindByPrimaryKeyOhneExc(String belegartCNr, String mandantCNr)
			throws EJBExceptionLP {
		if (belegartCNr == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartCNr == null || mandantCNr == null"));
		}
		// try {
		ModulberechtigungPK modulberechtigungPK = new ModulberechtigungPK();
		modulberechtigungPK.setBelegartCNr(belegartCNr);
		modulberechtigungPK.setMandantCNr(mandantCNr);
		Modulberechtigung modulberechtigung = em.find(Modulberechtigung.class, modulberechtigungPK);
		if (modulberechtigung == null) {
			return null;
		}
		return assembleModulberechtigungDto(modulberechtigung);
	}

	public ModulberechtigungDto[] modulberechtigungFindByMandantCNr(String mandantCNr) throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("mandantCNr == null"));
		}

		// try {
		Query query = em.createNamedQuery("ModulberechtigungfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleModulberechtigungDtos(cl);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setModulberechtigungFromModulberechtigungDto(Modulberechtigung modulberechtigung,
			ModulberechtigungDto modulberechtigungDto) {
		em.merge(modulberechtigung);
		em.flush();
	}

	private ModulberechtigungDto assembleModulberechtigungDto(Modulberechtigung modulberechtigung) {
		return ModulberechtigungDtoAssembler.createDto(modulberechtigung);
	}

	private ModulberechtigungDto[] assembleModulberechtigungDtos(Collection<?> modulberechtigungs) {
		List<ModulberechtigungDto> list = new ArrayList<ModulberechtigungDto>();
		if (modulberechtigungs != null) {
			Iterator<?> iterator = modulberechtigungs.iterator();
			while (iterator.hasNext()) {
				Modulberechtigung modulberechtigung = (Modulberechtigung) iterator.next();
				list.add(assembleModulberechtigungDto(modulberechtigung));
			}
		}
		ModulberechtigungDto[] returnArray = new ModulberechtigungDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * Im UI und auf Drucken soll das Zahlungsziel in einem bestimmten Locale bzw.
	 * der bestmoeglichen Uebersetzung angezeigt werden.
	 * 
	 * @param iIdZahlungszielI PK des Zahlungsziels
	 * @param localeI          das gewuenschte Locale
	 * @param theClientDto     der aktuelle Benutzer
	 * @return String die bestmoegliche Uebersetzung, null ist moeglich
	 */
	public String zahlungszielFindByIIdLocaleOhneExc(Integer iIdZahlungszielI, Locale localeI,
			TheClientDto theClientDto) {

		String cZahlungsziel = null;

		if (iIdZahlungszielI != null) {
			// Schritt 1: Uebersetzung in gewuenschtes Locale
			ZahlungszielsprDto zahlungszielsprDto = zahlungszielsprFindByPrimaryKeyOhneExc(iIdZahlungszielI,
					Helper.locale2String(localeI));

			if (zahlungszielsprDto != null && zahlungszielsprDto.getCBezeichnung() != null) {
				cZahlungsziel = zahlungszielsprDto.getCBezeichnung();
			} else {
				// Schritt 2: Uebersetzung in die UI Sprache des Benutzers
				zahlungszielsprDto = zahlungszielsprFindByPrimaryKeyOhneExc(iIdZahlungszielI,
						theClientDto.getLocUiAsString());

				if (zahlungszielsprDto != null && zahlungszielsprDto.getCBezeichnung() != null) {
					cZahlungsziel = zahlungszielsprDto.getCBezeichnung();
				} else {
					// Schritt 3: Uebersetzung in Konzerndatensprache
					zahlungszielsprDto = zahlungszielsprFindByPrimaryKeyOhneExc(iIdZahlungszielI,
							theClientDto.getLocKonzernAsString());

					if (zahlungszielsprDto != null && zahlungszielsprDto.getCBezeichnung() != null) {
						cZahlungsziel = zahlungszielsprDto.getCBezeichnung();
					} else {
						// Schritt 4: Die cNr der Lieferart
						ZahlungszielDto zahlungszielDto = zahlungszielFindByPrimaryKey(iIdZahlungszielI, theClientDto);

						cZahlungsziel = zahlungszielDto.getCBez();
					}
				}
			}
		}

		return cZahlungsziel;
	}

	public Integer getNachkommastellenPreisAllgemein(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN);
		Integer iPreisRabatte = (Integer) parameter.getCWertAsObject();
		return iPreisRabatte;
	}

	public Integer getNachkommastellenPreisEK(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);
		Integer iPreisRabatte = (Integer) parameter.getCWertAsObject();
		return iPreisRabatte;
	}

	public Integer getNachkommastellenPreisWE(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_WE);
		Integer iPreisRabatte = (Integer) parameter.getCWertAsObject();
		return iPreisRabatte;
	}

	public Integer getNachkommastellenPreisVK(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
		Integer iPreisRabatte = (Integer) parameter.getCWertAsObject();
		return iPreisRabatte;
	}

	public Integer getNachkommastellenMenge(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN);
		Integer iMenge = (Integer) parameter.getCWertAsObject();
		return iMenge;
	}

	public Integer getNachkommastellenLosgroesse(String mandantCNr) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameter = null;
		parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_NACHKOMMASTELLEN_LOSGROESSE);
		Integer iMenge = (Integer) parameter.getCWertAsObject();
		return iMenge;
	}

	public void createZusatzfunktion(ZusatzfunktionDto zusatzfunktionDto) throws EJBExceptionLP {
		if (zusatzfunktionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("zusatzfunktionDto == null"));
		}
		try {
			Zusatzfunktion zusatzfunktion = new Zusatzfunktion(zusatzfunktionDto.getCNr());
			em.persist(zusatzfunktion);
			em.flush();
			setZusatzfunktionFromZusatzfunktionDto(zusatzfunktion, zusatzfunktionDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ZusatzfunktionDto[] zusatzfunktionFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ZusatzfunktionfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZusatzfunktionDtos(cl);
		// }
		// catch (FinderException fe) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }

	}

	private void setZusatzfunktionFromZusatzfunktionDto(Zusatzfunktion zusatzfunktion,
			ZusatzfunktionDto zusatzfunktionDto) {
		em.merge(zusatzfunktion);
		em.flush();
	}

	private ZusatzfunktionDto assembleZusatzfunktionDto(Zusatzfunktion zusatzfunktion) {
		return ZusatzfunktionDtoAssembler.createDto(zusatzfunktion);
	}

	private ZusatzfunktionDto[] assembleZusatzfunktionDtos(Collection<?> zusatzfunktions) {
		List<ZusatzfunktionDto> list = new ArrayList<ZusatzfunktionDto>();
		if (zusatzfunktions != null) {
			Iterator<?> iterator = zusatzfunktions.iterator();
			while (iterator.hasNext()) {
				Zusatzfunktion zusatzfunktion = (Zusatzfunktion) iterator.next();
				list.add(assembleZusatzfunktionDto(zusatzfunktion));
			}
		}
		ZusatzfunktionDto[] returnArray = new ZusatzfunktionDto[list.size()];
		return list.toArray(returnArray);
	}

	public void createZusatzfunktionberechtigung(ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto)
			throws EJBExceptionLP {
		if (zusatzfunktionberechtigungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zusatzfunktionberechtigungDto == null"));
		}
		try {
			Zusatzfunktionberechtigung zusatzfunktionberechtigung = new Zusatzfunktionberechtigung(
					zusatzfunktionberechtigungDto.getZusatzfunktionCNr(),
					zusatzfunktionberechtigungDto.getMandantCNr());
			em.persist(zusatzfunktionberechtigung);
			em.flush();
			setZusatzfunktionberechtigungFromZusatzfunktionberechtigungDto(zusatzfunktionberechtigung,
					zusatzfunktionberechtigungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ZusatzfunktionberechtigungfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZusatzfunktionberechtigungDtos(cl);

		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
	}

	public ZusatzfunktionberechtigungDto zusatzfunktionberechtigungFindByPrimaryKey(String zusatzfunktionCNr,
			String mandantCNr) throws EJBExceptionLP {
		// try {
		Zusatzfunktionberechtigung zusatzfunktionberechtigung = em.find(Zusatzfunktionberechtigung.class,
				new ZusatzfunktionberechtigungPK(zusatzfunktionCNr, mandantCNr));
		if (zusatzfunktionberechtigung == null) {
			return null;
		}
		return assembleZusatzfunktionberechtigungDto(zusatzfunktionberechtigung);
		// }
		// catch (FinderException fe) {
		// return null;
		// }
	}

	private void setZusatzfunktionberechtigungFromZusatzfunktionberechtigungDto(
			Zusatzfunktionberechtigung zusatzfunktionberechtigung,
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto) {
		em.merge(zusatzfunktionberechtigung);
		em.flush();
	}

	private ZusatzfunktionberechtigungDto assembleZusatzfunktionberechtigungDto(
			Zusatzfunktionberechtigung zusatzfunktionberechtigung) {
		return ZusatzfunktionberechtigungDtoAssembler.createDto(zusatzfunktionberechtigung);
	}

	private ZusatzfunktionberechtigungDto[] assembleZusatzfunktionberechtigungDtos(
			Collection<?> zusatzfunktionberechtigungs) {
		List<ZusatzfunktionberechtigungDto> list = new ArrayList<ZusatzfunktionberechtigungDto>();
		if (zusatzfunktionberechtigungs != null) {
			Iterator<?> iterator = zusatzfunktionberechtigungs.iterator();
			while (iterator.hasNext()) {
				Zusatzfunktionberechtigung zusatzfunktionberechtigung = (Zusatzfunktionberechtigung) iterator.next();
				list.add(assembleZusatzfunktionberechtigungDto(zusatzfunktionberechtigung));
			}
		}
		ZusatzfunktionberechtigungDto[] returnArray = new ZusatzfunktionberechtigungDto[list.size()];
		return list.toArray(returnArray);
	}

	public Integer createMwstsatzbez(MwstsatzbezDto mwstsatzbezDto, TheClientDto theClientDto) throws EJBExceptionLP {

		Integer iId = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iId = pkGen.getNextPrimaryKey(PKConst.PK_MWSSATZBEZ);
			mwstsatzbezDto.setIId(iId);

			Mwstsatzbez mwstsatzbez = new Mwstsatzbez(mwstsatzbezDto.getIId(), mwstsatzbezDto.getCBezeichnung(),
					mwstsatzbezDto.getMandantCNr(), null, "");
			em.persist(mwstsatzbez);
			em.flush();
			setMwstsatzbezFromMwstsatzbezDto(mwstsatzbez, mwstsatzbezDto);
		} catch (EntityExistsException ex) {
			EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS, ex);
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add("Existierender Eintrag: " + mwstsatzbezDto.getCBezeichnung());
			alInfo.add("Anzulegende ID: " + mwstsatzbezDto.getIId());
			e.setAlInfoForTheClient(alInfo);
			throw e;
		}
		return iId;

	}

	public void removeMwstsatzbez(Integer iId) throws EJBExceptionLP {
		try {
			Mwstsatzbez toRemove = em.find(Mwstsatzbez.class, iId);
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

	public void removeMwstsatzbez(MwstsatzbezDto mwstsatzbezDto) throws EJBExceptionLP {
		if (mwstsatzbezDto != null) {
			Integer iId = mwstsatzbezDto.getIId();
			removeMwstsatzbez(iId);
		}
	}

	/**
	 * MWST-Satz-Bezeichnung updaten.
	 * 
	 * @param mwstsatzbezDto MwstsatzbezDto
	 * @param theClientDto   String
	 * @throws EJBException
	 */
	public void updateMwstsatzbez(MwstsatzbezDto mwstsatzbezDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (mwstsatzbezDto != null) {
			Integer iId = mwstsatzbezDto.getIId();
			// try {
			Mwstsatzbez mwstsatzbez = em.find(Mwstsatzbez.class, iId);
			if (mwstsatzbez == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			setMwstsatzbezFromMwstsatzbezDto(mwstsatzbez, mwstsatzbezDto);
		}
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		// }
	}

	public HvOptional<MwstsatzbezDto> mwstsatzbezFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto) {
		if (iId == null)
			return HvOptional.empty();
		Mwstsatzbez mwstsatzbez = em.find(Mwstsatzbez.class, iId);
		if (mwstsatzbez == null)
			return HvOptional.empty();
		return HvOptional.of(assembleMwstsatzbezDto(mwstsatzbez));
	}

	public MwstsatzbezDto mwstsatzbezFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		HvOptional<MwstsatzbezDto> result = mwstsatzbezFindByPrimaryKeyOhneExc(iId, theClientDto);
		if (!result.isPresent())
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		return result.get();
	}

	private void setMwstsatzbezFromMwstsatzbezDto(Mwstsatzbez mwstsatzbez, MwstsatzbezDto mwstsatzbezDto) {
		mwstsatzbez.setMandantCNr(mwstsatzbezDto.getMandantCNr());
		mwstsatzbez.setCBezeichnung(mwstsatzbezDto.getCBezeichnung());
		mwstsatzbez.setFinanzamtIId(mwstsatzbezDto.getFinanzamtIId());
		mwstsatzbez.setCDruckname(mwstsatzbezDto.getCDruckname());
		em.merge(mwstsatzbez);
		em.flush();
	}

	private MwstsatzbezDto assembleMwstsatzbezDto(Mwstsatzbez mwstsatzbez) {
		return MwstsatzbezDtoAssembler.createDto(mwstsatzbez);
	}

	private MwstsatzbezDto[] assembleMwstsatzbezDtos(Collection<?> mwstsatzbezs) {
		List<MwstsatzbezDto> list = new ArrayList<MwstsatzbezDto>();
		if (mwstsatzbezs != null) {
			Iterator<?> iterator = mwstsatzbezs.iterator();
			while (iterator.hasNext()) {
				Mwstsatzbez mwstsatzbez = (Mwstsatzbez) iterator.next();
				list.add(assembleMwstsatzbezDto(mwstsatzbez));
			}
		}
		MwstsatzbezDto[] returnArray = new MwstsatzbezDto[list.size()];
		return list.toArray(returnArray);
	}

	public MwstsatzbezDto getMwstsatzbezSteuerfrei(TheClientDto theClient) {

		Query query = em.createNamedQuery("MwstsatzbezfindByMandantCDruckname");
		query.setParameter(1, theClient.getMandant());
		query.setParameter(2, FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTOHNE);
		query.setMaxResults(1);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0)
			return assembleMwstsatzbezDto((Mwstsatzbez) cl.toArray()[0]);
		else
			return null;
	}

	@Override
	public ReportMandantDto createReportMandantDto(TheClientDto theClient, Locale localeDruck) {
		MandantDto mandant = mandantFindByPrimaryKey(theClient.getMandant(), theClient);
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClient.getIDPersonal(), theClient);

		String signatur = getPersonalFac().getSignatur(theClient.getIDPersonal(), theClient.getLocUiAsString());
		String sZessionstext = null;
		String hautpmandant = null;

		boolean bParametermandantArtikelgewichtGrammStattKilo = false;
		try {
			sZessionstext = getParameterFac().getMandantparameter(theClient.getMandant(),

					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZESSIONSTEXT).getCWert();

			bParametermandantArtikelgewichtGrammStattKilo = (Boolean) getParameterFac()
					.getMandantparameter(theClient.getMandant(),

							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGEWICHT_GRAMM_STATT_KILO)
					.getCWertAsObject();

			hautpmandant = getSystemFac().getHauptmandant();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String personalAnrede = null;
		if (localeDruck != null) {
			personalAnrede = getPersonalFac().formatAnrede(personalDto.getPartnerDto(), localeDruck, theClient);
		}

		return new ReportMandantDto(theClient, mandant, personalDto, signatur, sZessionstext,
				bParametermandantArtikelgewichtGrammStattKilo, hautpmandant, personalAnrede);
	}

	public boolean hatModulFinanzbuchhaltung(TheClientDto theClientDto) {
		return darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktion4Vending(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionSepaLastschrift(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_SEPA_LASTSCHRIFT, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionHvmaZeiterfassung(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_HVMA_ZEITERFASSUNG, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionStuecklisteMitFormeln(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_STUECKLISTE_MIT_FORMELN, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionDebugmodus(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_DEBUGMODUS, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionEinkaufsEan(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_EINKAUFS_EAN, theClientDto);
	}

	@Override
	public List<MwstsatzDto> mwstsatzFindJuengsteZuDatum(MwstsatzId mwstsatzId, Timestamp passendZuTimestamp,
			TheClientDto theClientDto) {
		Mwstsatz findSatz = em.find(Mwstsatz.class, mwstsatzId.id());
		return mwstsatzFindJuengsteZuDatumByBez(new MwstsatzbezId(findSatz.getMwstsatzbezIId()), passendZuTimestamp,
				theClientDto);
	}

	@Override
	public List<MwstsatzDto> mwstsatzFindJuengsteZuDatumByBez(MwstsatzbezId mwstsatzbezId, Timestamp passendZuTimestamp,
			TheClientDto theClientDto) {
		MwstsatzbezDto bezDto = mwstsatzbezFindByPrimaryKey(mwstsatzbezId.id(), theClientDto);
		Query query = em.createNamedQuery("MwstsatzfindByMwstbez");
		query.setParameter(1, bezDto.getIId());
		List<Mwstsatz> cl = query.getResultList();

		// Die Liste ist aufsteigend sortiert. Jetzt die raussuchen die
		// neuer(nach) sind, oder das letzte Datum davor.
		List<MwstsatzDto> dtos = new ArrayList<MwstsatzDto>();
		for (int i = cl.size() - 1; i >= 0; i--) {
			MwstsatzDto dto = assembleMwstsatzDto(cl.get(i));
			dto.setMwstsatzbezDto(bezDto);
			dtos.add(0, dto);

			if (dto.getDGueltigab().compareTo(passendZuTimestamp) < 0) {
				break;
			}
		}

		return dtos;
	}

	@Override
	public MwstsatzbezDto mwstsatzbezFindByBezeichnung(String bezeichnung, String mandantCnr) throws EJBExceptionLP {
		Validator.notEmpty(bezeichnung, "bezeichnung");
		Validator.notEmpty(mandantCnr, "mandantCnr");

		Query query = em.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
		query.setParameter(1, mandantCnr);
		query.setParameter(2, bezeichnung);

		Collection<Mwstsatzbez> cl = query.getResultList();
		if (cl.size() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, bezeichnung);
		}
		if (cl.size() > 1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_MWSTSATZBEZ.UK"));
		}
		return assembleMwstsatzbezDto(cl.iterator().next());
	}

	@Override
	public boolean hatZusatzfunktionZugferd(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_ZUGFERD, theClientDto);
	}

	@Override
	public List<DokumentenlinkDto> getSichtbareDokumentenlinks(String belegartCnr, boolean bPfadAbsolut,
			TheClientDto theClientDto) {
		DokumentenlinkDto[] dokumentenlinks = dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(belegartCnr,
				theClientDto.getMandant(), bPfadAbsolut);
		if (dokumentenlinks.length < 1) {
			return new ArrayList<DokumentenlinkDto>();
		}

		List<DokumentenlinkDto> visibleDoklinks = new ArrayList<DokumentenlinkDto>();
		for (DokumentenlinkDto doklink : dokumentenlinks) {
			if (doklink.getRechtCNr() == null || getTheJudgeFac().hatRecht(doklink.getRechtCNr(), theClientDto)) {
				visibleDoklinks.add(doklink);
			}
		}

		return visibleDoklinks;
	}

	@Override
	public boolean hatZusatzfunktionForecastAuftragVerteilen(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_FORECAST_AUFTRAG_VERTEILUNG, theClientDto);
	}

	@Override
	public boolean hatModulNachrichten(TheClientDto theClientDto) {
		return darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_NACHRICHTEN, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionPostPLCVersand(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_POST_PLC_VERSAND, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionLiquiditaetsvorschau(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_LIQUIDITAETSVORSCHAU, theClientDto);
	}

	@Override
	public boolean hatZusatzfunktionZentralerArtikelstamm(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);
	}

	@Override
	public Integer createMwstsatzCode(MwstsatzCodeDto codeDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(codeDto, "mwstsatzCodeDto");
		Validator.notNull(codeDto.getMwstsatzId(), "mwstsatzCodeDto.getMwstsatzId()");
		Validator.notNull(codeDto.getReversechargeartId(), "mwstsatzCodeDto.getReversechargeartId()");

		HvOptional<MwstsatzCode> duplicate = MwstsatzCodeQuery.findByMwstsatzIIdReversechargeartIId(em,
				codeDto.getMwstsatzId(), codeDto.getReversechargeartId());
		if (duplicate.isPresent())
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("UK_LP_MWSTSATZCODE"));

		try {
			Integer pk = new PKGeneratorObj().getNextPrimaryKey(PKConst.PK_MWSTSATZCODE);
			codeDto.setIId(pk);

			MwstsatzCode entity = getMapper().map(codeDto, MwstsatzCode.class);
			em.persist(entity);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public void removeMwstsatzCode(MwstsatzCodeId mwstsatzCodeId) {
		Validator.pkFieldValid(mwstsatzCodeId, "mwstsatzCodeId");

		removeMwstsatzCodeImpl(Collections.singleton(mwstsatzCodeId.id()));
	}

	public void removeMwstsatzCode(MwstsatzId mwstsatzId) {
		Validator.notNull(mwstsatzId, "mwstsatzId");

		Collection<Integer> iids = MwstsatzCodeQuery.findIIdByMwstsatzIId(em, mwstsatzId);
		removeMwstsatzCodeImpl(iids);
	}

	private void removeMwstsatzCodeImpl(Collection<Integer> mwstsatzCodeIIds) {
		try {
			for (Integer iId : mwstsatzCodeIIds) {
				MwstsatzCode entity = em.find(MwstsatzCode.class, iId);
				Validator.entityFound(entity, iId);
				em.remove(entity);
				em.flush();
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	@Override
	public void updateMwstsatzCode(MwstsatzCodeDto codeDto) {
		Validator.dtoNotNull(codeDto, "mwstsatzCodeDto");
		Validator.notNull(codeDto.getMwstsatzId(), "mwstsatzCodeDto.getMwstsatzId()");
		Validator.notNull(codeDto.getReversechargeartId(), "mwstsatzCodeDto.getReversechargeartId()");

		MwstsatzCode entity = em.find(MwstsatzCode.class, codeDto.getIId());
		Validator.entityFound(entity, codeDto.getIId());

		HvOptional<MwstsatzCode> duplicate = MwstsatzCodeQuery.findByMwstsatzIIdReversechargeartIId(em,
				codeDto.getMwstsatzId(), codeDto.getReversechargeartId());
		if (duplicate.isPresent() && !duplicate.get().getIId().equals(codeDto.getIId()))
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("UK_LP_MWSTSATZCODE"));

		entity = getMapper().map(codeDto, MwstsatzCode.class);
		em.merge(entity);
		em.flush();
	}

	@Override
	public HvOptional<MwstsatzCodeDto> mwstsatzCodeFindByPrimaryKeyOhneExc(MwstsatzCodeId mwstsatzCodeId) {
		if (mwstsatzCodeId == null || !mwstsatzCodeId.isValid())
			return HvOptional.empty();

		MwstsatzCode entity = em.find(MwstsatzCode.class, mwstsatzCodeId.id());
		if (entity == null)
			return HvOptional.empty();

		return HvOptional.of(getMapper().map(entity, MwstsatzCodeDto.class));
	}

	@Override
	public MwstsatzCodeDto mwstsatzCodeFindByPrimaryKey(MwstsatzCodeId mwstsatzCodeId) {
		Validator.pkFieldValid(mwstsatzCodeId, "mwstsatzCodeId");

		HvOptional<MwstsatzCodeDto> codeOpt = mwstsatzCodeFindByPrimaryKeyOhneExc(mwstsatzCodeId);
		if (codeOpt.isPresent())
			return codeOpt.get();

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				"mwstsatzCodeId=" + mwstsatzCodeId.id().toString());
	}

	@Override
	public HvOptional<MwstsatzCodeDto> mwstsatzCodeFindByMwstsatzReversechargeart(MwstsatzId mwstsatzId,
			ReversechargeartId reversechargeartId) {
		Validator.notNull(mwstsatzId, "mwstsatzId");
		Validator.notNull(reversechargeartId, "reversechargeartId");

		HvOptional<MwstsatzCode> codeOpt = MwstsatzCodeQuery.findByMwstsatzIIdReversechargeartIId(em, mwstsatzId,
				reversechargeartId);
		if (codeOpt.isEmpty()) {
			return HvOptional.empty();
		}

		return mwstsatzCodeFindByPrimaryKeyOhneExc(new MwstsatzCodeId(codeOpt.get().getIId()));
	}

	@Override
	public HvOptional<SteuercodeInfo> getSteuercodeArDefault(MwstsatzId mwstsatzId)
			throws RemoteException, EJBExceptionLP {
		Validator.notNull(mwstsatzId, "mwstsatzId");

		HvOptional<MwstsatzCodeDto> codeOpt = mwstsatzCodeReverschargeartOhneFindByMwstsatz(mwstsatzId);
		if (codeOpt.isEmpty()) {
			return HvOptional.empty();
		}

		MwstsatzCodeDto mwstsatzCode = codeOpt.get();
		if (StringUtils.isEmpty(mwstsatzCode.getCSteuercodeAr())) {
			return HvOptional.empty();
		}

		SteuercodeInfo codeInfo = getMapper().map(mwstsatzCode, SteuercodeInfo.class);
		codeInfo.setCode(mwstsatzCode.getCSteuercodeAr());

		return HvOptional.of(codeInfo);
	}

	public HvOptional<SteuercodeInfo> getSteuercodeErDefault(MwstsatzId mwstsatzId)
			throws RemoteException, EJBExceptionLP {
		Validator.notNull(mwstsatzId, "mwstsatzId");

		HvOptional<MwstsatzCodeDto> codeOpt = mwstsatzCodeReverschargeartOhneFindByMwstsatz(mwstsatzId);
		if (codeOpt.isEmpty()) {
			return HvOptional.empty();
		}
		MwstsatzCodeDto mwstsatzCode = codeOpt.get();
		if (StringUtils.isEmpty(mwstsatzCode.getCSteuercodeEr())) {
			return HvOptional.empty();
		}

		SteuercodeInfo codeInfo = getMapper().map(mwstsatzCode, SteuercodeInfo.class);
		codeInfo.setCode(mwstsatzCode.getCSteuercodeEr());

		return HvOptional.of(codeInfo);
	}

	private HvOptional<MwstsatzCodeDto> mwstsatzCodeReverschargeartOhneFindByMwstsatz(MwstsatzId mwstsatzId)
			throws RemoteException, EJBExceptionLP {
		Mwstsatz entitySatz = em.find(Mwstsatz.class, mwstsatzId.id());
		Mwstsatzbez entityBez = em.find(Mwstsatzbez.class, entitySatz.getMwstsatzbezIId());
		ReversechargeartDto rcOhneDto = getFinanzServiceFac().reversechargeartFindOhne(entityBez.getMandantCNr());
		return mwstsatzCodeFindByMwstsatzReversechargeart(mwstsatzId, new ReversechargeartId(rcOhneDto.getIId()));
	}

	@Override
	public List<MwstsatzCodeDto> getAllReversechargeartMwstsatzCodeByMwstsatzId(MwstsatzId mwstsatzId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.notNull(mwstsatzId, "mwstsatzId");

		return getAllReversechargeartMwstsatzCodeImpl(mwstsatzId, theClientDto);
	}

	private List<MwstsatzCodeDto> getAllReversechargeartMwstsatzCodeImpl(MwstsatzId mwstsatzId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		List<Integer> rcIIds = ReversechargeartQuery.listIIdsByMandant(em, theClientDto.getMandant(), Boolean.TRUE);

		List<MwstsatzCodeDto> resultCodes = new ArrayList<MwstsatzCodeDto>();
		for (Integer rcIId : rcIIds) {
			HvOptional<MwstsatzCodeDto> codeOpt = mwstsatzId != null
					? mwstsatzCodeFindByMwstsatzReversechargeart(mwstsatzId, new ReversechargeartId(rcIId))
					: HvOptional.empty();
			if (codeOpt.isPresent()) {
				resultCodes.add(codeOpt.get());
			} else {
				MwstsatzCodeDto codeDto = new MwstsatzCodeDto();
				codeDto.setReversechargeartIId(rcIId);
				codeDto.setMwstsatzId(mwstsatzId);
				resultCodes.add(codeDto);
			}
		}

		return resultCodes;
	}

	@Override
	public void updateOrCreateMwstsatzCodes(List<MwstsatzCodeDto> mwstsatzCodeDtos, TheClientDto theClientDto) {
		for (MwstsatzCodeDto codeDto : mwstsatzCodeDtos) {
			if (codeDto.getIId() == null) {
				createMwstsatzCode(codeDto, theClientDto);
			} else {
				updateMwstsatzCode(codeDto);
			}
		}
	}
	
	@Override
	public boolean hatZusatzfunktionIntrastat(TheClientDto theClientDto) {
		return darfAnwenderAufZusatzfunktionZugreifen(ZUSATZFUNKTION_INTRASTAT, theClientDto);		
	}
}
