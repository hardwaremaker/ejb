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
package com.lp.server.instandhaltung.ejbfac;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.instandhaltung.ejb.Anlage;
import com.lp.server.instandhaltung.ejb.Geraet;
import com.lp.server.instandhaltung.ejb.Geraetehistorie;
import com.lp.server.instandhaltung.ejb.Geraetetyp;
import com.lp.server.instandhaltung.ejb.Gewerk;
import com.lp.server.instandhaltung.ejb.Halle;
import com.lp.server.instandhaltung.ejb.Instandhaltung;
import com.lp.server.instandhaltung.ejb.Iskategorie;
import com.lp.server.instandhaltung.ejb.Ismaschine;
import com.lp.server.instandhaltung.ejb.Standort;
import com.lp.server.instandhaltung.ejb.Standorttechniker;
import com.lp.server.instandhaltung.ejb.Wartungsliste;
import com.lp.server.instandhaltung.ejb.Wartungsschritte;
import com.lp.server.instandhaltung.fastlanereader.generated.FLRIskategorie;
import com.lp.server.instandhaltung.service.AnlageDto;
import com.lp.server.instandhaltung.service.AnlageDtoAssembler;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.GeraetDtoAssembler;
import com.lp.server.instandhaltung.service.GeraetehistorieDto;
import com.lp.server.instandhaltung.service.GeraetehistorieDtoAssembler;
import com.lp.server.instandhaltung.service.GeraetetypDto;
import com.lp.server.instandhaltung.service.GeraetetypDtoAssembler;
import com.lp.server.instandhaltung.service.GewerkDto;
import com.lp.server.instandhaltung.service.GewerkDtoAssembler;
import com.lp.server.instandhaltung.service.HalleDto;
import com.lp.server.instandhaltung.service.HalleDtoAssembler;
import com.lp.server.instandhaltung.service.InstandhaltungDto;
import com.lp.server.instandhaltung.service.InstandhaltungDtoAssembler;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.instandhaltung.service.IskategorieDto;
import com.lp.server.instandhaltung.service.IskategorieDtoAssembler;
import com.lp.server.instandhaltung.service.IsmaschineDto;
import com.lp.server.instandhaltung.service.IsmaschineDtoAssembler;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.instandhaltung.service.StandortDtoAssembler;
import com.lp.server.instandhaltung.service.StandorttechnikerDto;
import com.lp.server.instandhaltung.service.StandorttechnikerDtoAssembler;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungslisteDtoAssembler;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class InstandhaltungFacBean extends Facade implements InstandhaltungFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createInstandhaltung(InstandhaltungDto dto) {

		try {
			Query query = em.createNamedQuery("InstandhaltungFindyByKundeIId");
			query.setParameter(1, dto.getKundeIId());
			// @todo getSingleResult oder getResultList ?
			Instandhaltung doppelt = (Instandhaltung) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_INSTANDHALTUNG.KUNDE_I_ID"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSTANDHALTUNG);
			dto.setIId(pk);

			Instandhaltung bean = new Instandhaltung(dto.getIId(),
					dto.getKundeIId(), dto.getBVersteckt(),
					dto.getKategorieIId());
			em.persist(bean);
			em.flush();
			setInstandhaltungFromInstandhaltungDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateInstandhaltung(InstandhaltungDto dto) {
		Instandhaltung instandhaltung = em.find(Instandhaltung.class,
				dto.getIId());

		try {
			Query query = em.createNamedQuery("InstandhaltungFindyByKundeIId");
			query.setParameter(1, dto.getKundeIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Instandhaltung) query.getSingleResult())
					.getIId();
			if (instandhaltung.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_INSTANDHALTUNG.KUNDE_I_ID"));
			}
		} catch (NoResultException ex) {

		}

		setInstandhaltungFromInstandhaltungDto(instandhaltung, dto);
	}

	public void updateStandorttechniker(StandorttechnikerDto dto) {
		Standorttechniker bean = em.find(Standorttechniker.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("StandorttechnikerFindByStandortIIdPersonalIId");
			query.setParameter(1, dto.getStandortIId());
			query.setParameter(2, dto.getPersonalIId());
			// @todo getSingleResult oder getResultList ?
			Standorttechniker doppelt = (Standorttechniker) query
					.getSingleResult();

			Integer iIdVorhanden = ((Standorttechniker) query.getSingleResult())
					.getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_STANDORTTECHNIKER.UK"));
			}
		} catch (NoResultException ex) {

		}

		if (Helper.short2Boolean(dto.getBVerantwortlicher())) {
			// Alle anderen auf 0 setzten
			Query query = em
					.createNamedQuery("StandorttechnikerFindByStandortIId");
			query.setParameter(1, dto.getStandortIId());
			Collection c = query.getResultList();

			Iterator it = c.iterator();
			while (it.hasNext()) {
				Standorttechniker st = (Standorttechniker) it.next();
				if (!bean.getIId().equals(st.getIId())) {
					st.setBVerantwortlicher(Helper.boolean2Short(false));
					em.merge(st);
					em.flush();
				}
			}
		}

		setStandorttechnikerFromStandorttechnikerDto(bean, dto);
	}

	public InstandhaltungDto instandhaltungFindByPrimaryKey(Integer iId) {
		Instandhaltung instandhaltung = em.find(Instandhaltung.class, iId);
		return InstandhaltungDtoAssembler.createDto(instandhaltung);
	}

	public void removeInstandhaltung(InstandhaltungDto dto) {
		Instandhaltung toRemove = em.find(Instandhaltung.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setInstandhaltungFromInstandhaltungDto(Instandhaltung bean,
			InstandhaltungDto dto) {
		bean.setKundeIId(dto.getKundeIId());
		bean.setBVersteckt(dto.getBVersteckt());
		bean.setKategorieIId(dto.getKategorieIId());
		em.merge(bean);
		em.flush();
	}

	public Integer createHalle(HalleDto dto) {

		try {
			Query query = em.createNamedQuery("HalleFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Halle doppelt = (Halle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_HALLE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HALLE);
			dto.setIId(pk);

			Halle bean = new Halle(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez(), dto.getStandortIId());
			em.persist(bean);
			em.flush();
			setHalleFromHalleDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGeraetehistorie(GeraetehistorieDto dto,
			TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GERAETEHISTORIE);
			dto.setIId(pk);
			dto.setTAendern(new Timestamp(System.currentTimeMillis()));
			dto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Geraetehistorie bean = new Geraetehistorie(dto.getIId(),
					dto.getGeraetIId(), dto.getPersonalIIdTechniker(),
					dto.getPersonalIIdAendern(), dto.getTAendern(),
					dto.getTWartung(), dto.getBNichtmoeglich());
			em.persist(bean);
			em.flush();
			setGeraetehistorieFromGeraetehistorieDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createStandorttechniker(StandorttechnikerDto dto,
			TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STANDORTTECHNIKER);
			dto.setIId(pk);

			try {
				Query query = em
						.createNamedQuery("StandorttechnikerFindByStandortIIdPersonalIId");
				query.setParameter(1, dto.getStandortIId());
				query.setParameter(2, dto.getPersonalIId());
				// @todo getSingleResult oder getResultList ?
				Standorttechniker doppelt = (Standorttechniker) query
						.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_STANDORTTECHNIKER.UK"));
			} catch (NoResultException ex1) {
				// nothing here
			}

			if (Helper.short2Boolean(dto.getBVerantwortlicher())) {
				// Alle anderen auf 0 setzten
				Query query = em
						.createNamedQuery("StandorttechnikerFindByStandortIId");
				query.setParameter(1, dto.getStandortIId());
				Collection c = query.getResultList();

				Iterator it = c.iterator();
				while (it.hasNext()) {
					Standorttechniker st = (Standorttechniker) it.next();
					st.setBVerantwortlicher(Helper.boolean2Short(false));
					em.merge(st);
					em.flush();
				}
			}

			Standorttechniker bean = new Standorttechniker(dto.getIId(),
					dto.getStandortIId(), dto.getPersonalIId(),
					dto.getBVerantwortlicher());
			em.persist(bean);
			em.flush();
			setStandorttechnikerFromStandorttechnikerDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createIsmaschine(IsmaschineDto dto) {

		try {
			Query query = em.createNamedQuery("IsmaschineFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Ismaschine doppelt = (Ismaschine) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_ISMASCHINE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ISMASCHINE);
			dto.setIId(pk);

			Ismaschine bean = new Ismaschine(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez(), dto.getAnlageIId());
			em.persist(bean);
			em.flush();
			setIsmaschineFromIsmaschineDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGeraetetyp(GeraetetypDto dto) {

		try {
			Query query = em.createNamedQuery("GeraetetypFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Geraetetyp doppelt = (Geraetetyp) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_GERAETETYP.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GERAETETYP);
			dto.setIId(pk);

			Geraetetyp bean = new Geraetetyp(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setGeraetetypFromGeraetetypDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createIskategorie(IskategorieDto dto) {

		try {
			Query query = em
					.createNamedQuery("IskategorieFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Iskategorie doppelt = (Iskategorie) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_KATEGORIE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ISKATEGORIE);
			dto.setIId(pk);

			Iskategorie bean = new Iskategorie(dto.getIId(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setIskategorieFromIskategorieDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGewerk(GewerkDto dto) {

		try {
			Query query = em.createNamedQuery("GewerkFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Gewerk doppelt = (Gewerk) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_GEWERK.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GEWERK);
			dto.setIId(pk);

			Gewerk bean = new Gewerk(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setGewerkFromIsGewerkDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createWartungsliste(WartungslisteDto dto,
			TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WARTUNGSLISTE);
			dto.setIId(pk);

			if (dto.getISort() == null) {
				Integer i = null;
				try {
					Query querynext = em
							.createNamedQuery("WartungslisteejbSelectNextReihung");
					querynext.setParameter(1, dto.getGeraetIId());
					i = (Integer) querynext.getSingleResult();
				} catch (NoResultException ex) {
				}
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				dto.setISort(i);

			}

			if (dto.getTVeraltet() != null) {
				dto.setPersonalIIdVeraltet(theClientDto.getIDPersonal());
				dto.setTPersonalVeraltet(new Timestamp(System
						.currentTimeMillis()));
			} else {
				dto.setPersonalIIdVeraltet(null);
				dto.setTPersonalVeraltet(null);
			}

			Wartungsliste bean = new Wartungsliste(dto.getIId(),
					dto.getGeraetIId(), dto.getNMenge(),
					dto.getBWartungsmaterial(), dto.getBVerrechenbar(),
					dto.getISort());
			em.persist(bean);
			em.flush();
			setWartungslisteFromWartungslisteDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createWartungsschritte(WartungsschritteDto dto,
			TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WARTUNGSSCHRITTE);
			dto.setIId(pk);

			if (dto.getISort() == null) {
				Integer i = null;
				try {
					Query querynext = em
							.createNamedQuery("WartungsschritteejbSelectNextReihung");
					querynext.setParameter(1, dto.getGeraetIId());
					i = (Integer) querynext.getSingleResult();
				} catch (NoResultException ex) {
				}
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				dto.setISort(i);

			}

			Wartungsschritte bean = new Wartungsschritte(dto.getIId(),
					dto.getGeraetIId(), dto.getArtikelIId(),
					dto.getTAbdurchfuehren(), dto.getLDauer(),
					dto.getAuftragwiederholungsintervallCNr(), dto.getISort(),
					dto.getPersonalgruppeIId());
			em.persist(bean);
			em.flush();
			setWartungsschritteFromWartungsschritteDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateHalle(HalleDto dto) {
		Halle ialle = em.find(Halle.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("HalleFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Halle) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_HALLE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setHalleFromHalleDto(ialle, dto);
	}

	public void updateIsmaschine(IsmaschineDto dto) {
		Ismaschine ialle = em.find(Ismaschine.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("IsmaschineFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Ismaschine) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_ISMASCHINE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setIsmaschineFromIsmaschineDto(ialle, dto);
	}

	public void updateGeraetetyp(GeraetetypDto dto) {
		Geraetetyp ialle = em.find(Geraetetyp.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("GeraetetypFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Geraetetyp) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_GERAETETYP.UK"));
			}
		} catch (NoResultException ex) {

		}

		setGeraetetypFromGeraetetypDto(ialle, dto);
	}

	public void updateIskategorie(IskategorieDto dto) {
		Iskategorie ialle = em.find(Iskategorie.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("IskategorieFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Iskategorie) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_KATEGORIE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setIskategorieFromIskategorieDto(ialle, dto);
	}

	public void updateGewerk(GewerkDto dto) {
		Gewerk ialle = em.find(Gewerk.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("GewerkFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Gewerk) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_GEWERK.UK"));
			}
		} catch (NoResultException ex) {

		}

		setGewerkFromIsGewerkDto(ialle, dto);
	}

	public void updateWartungsliste(WartungslisteDto dto,
			TheClientDto theClientDto) {
		Wartungsliste wartungsliste = em
				.find(Wartungsliste.class, dto.getIId());
		if (wartungsliste.getTVeraltet() == null && dto.getTVeraltet() != null) {
			dto.setPersonalIIdVeraltet(theClientDto.getIDPersonal());
			dto.setTPersonalVeraltet(new Timestamp(System.currentTimeMillis()));
		}

		if (dto.getTVeraltet() == null && wartungsliste.getTVeraltet() != null) {
			dto.setPersonalIIdVeraltet(null);
			dto.setTPersonalVeraltet(null);
		}

		setWartungslisteFromWartungslisteDto(wartungsliste, dto);
	}

	public void updateWartungsschritte(WartungsschritteDto dto,
			TheClientDto theClientDto) {
		Wartungsschritte wartungsliste = em.find(Wartungsschritte.class,
				dto.getIId());

		setWartungsschritteFromWartungsschritteDto(wartungsliste, dto);
	}

	public void updateGeraet(GeraetDto dto) {
		Geraet ialle = em.find(Geraet.class, dto.getIId());

		/*try {
			Query query = em.createNamedQuery("GeraetFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Geraet) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_GERAET.UK"));
			}
		} catch (NoResultException ex) {

		}*/

		setGeraetFromGeraetDto(ialle, dto);
	}

	public void updateGeraetehistorie(GeraetehistorieDto dto,
			TheClientDto theClientDto) {
		Geraetehistorie ialle = em.find(Geraetehistorie.class, dto.getIId());
		dto.setTAendern(new Timestamp(System.currentTimeMillis()));
		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		setGeraetehistorieFromGeraetehistorieDto(ialle, dto);
	}

	public void updateAnlage(AnlageDto dto) {
		Anlage anlage = em.find(Anlage.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("AnlageFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Anlage) query.getSingleResult()).getIId();
			if (anlage.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_ANLAGE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setAnlageFromAnlageDto(anlage, dto);
	}

	public HalleDto halleFindByPrimaryKey(Integer iId) {
		Halle ialle = em.find(Halle.class, iId);
		return HalleDtoAssembler.createDto(ialle);
	}

	public GeraetDto geraetFindByPrimaryKey(Integer iId) {
		Geraet ialle = em.find(Geraet.class, iId);
		if (ialle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return GeraetDtoAssembler.createDto(ialle);
	}

	public GeraetehistorieDto geraetehistorieFindByPrimaryKey(Integer iId) {
		Geraetehistorie ialle = em.find(Geraetehistorie.class, iId);
		return GeraetehistorieDtoAssembler.createDto(ialle);
	}

	public GeraetehistorieDto[] geraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung(
			Integer geraetIId, Integer personalIIdTechniker, Timestamp tWartung) {

		Query query = em
				.createNamedQuery("GeraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung");
		query.setParameter(1, geraetIId);
		query.setParameter(2, personalIIdTechniker);
		query.setParameter(3, Helper.cutTimestamp(tWartung));
		Collection c = query.getResultList();

		return GeraetehistorieDtoAssembler.createDtos(c);
	}

	public IskategorieDto geraeteloseFindByPrimaryKey(Integer iId) {
		Iskategorie ialle = em.find(Iskategorie.class, iId);
		return IskategorieDtoAssembler.createDto(ialle);
	}

	public IsmaschineDto ismaschineFindByPrimaryKey(Integer iId) {
		Ismaschine ialle = em.find(Ismaschine.class, iId);
		return IsmaschineDtoAssembler.createDto(ialle);
	}

	public IskategorieDto iskategorieFindByPrimaryKey(Integer iId) {
		Iskategorie ialle = em.find(Iskategorie.class, iId);
		return IskategorieDtoAssembler.createDto(ialle);
	}

	public GewerkDto gewerkFindByPrimaryKey(Integer iId) {
		Gewerk ialle = em.find(Gewerk.class, iId);
		return GewerkDtoAssembler.createDto(ialle);
	}

	public GeraetetypDto geraetetypFindByPrimaryKey(Integer iId) {
		Geraetetyp ialle = em.find(Geraetetyp.class, iId);
		return GeraetetypDtoAssembler.createDto(ialle);
	}

	public WartungslisteDto wartungslisteFindByPrimaryKey(Integer iId) {
		Wartungsliste ialle = em.find(Wartungsliste.class, iId);
		return WartungslisteDtoAssembler.createDto(ialle);
	}

	public WartungslisteDto[] wartungslisteFindByGeraetIId(Integer geratIId) {
		Query query = em.createNamedQuery("WartungslistefindByGeraetIId");
		query.setParameter(1, geratIId);
		return WartungslisteDtoAssembler.createDtos(query.getResultList());
	}

	public WartungsschritteDto wartungsschritteFindByPrimaryKey(Integer iId) {
		Wartungsschritte ialle = em.find(Wartungsschritte.class, iId);
		return WartungsschritteDtoAssembler.createDto(ialle);
	}

	public AnlageDto anlageFindByPrimaryKey(Integer iId) {
		Anlage anlage = em.find(Anlage.class, iId);
		return AnlageDtoAssembler.createDto(anlage);
	}

	public void removeHalle(HalleDto dto) {
		Halle toRemove = em.find(Halle.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Map getAllKategorieren(TheClientDto theClientDto) {

		TreeMap<Integer, String> m = new TreeMap<Integer, String>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT kategorie FROM FLRIskategorie AS kategorie WHERE kategorie.mandant_c_nr='"
				+ theClientDto.getMandant() + "'  ORDER BY kategorie.c_bez ASC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRIskategorie p = (FLRIskategorie) resultListIterator.next();

			m.put(p.getI_id(), p.getC_bez());

		}

		session.close();

		// Nun noch ALLE hinzufuegen

		m.put(new Integer(-1),
				getTextRespectUISpr("lp.alle", theClientDto.getMandant(),
						theClientDto.getLocUi()));

		return m;
	}

	public void removeGeraet(GeraetDto dto) {
		Geraet toRemove = em.find(Geraet.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeGeraetehistorie(GeraetehistorieDto dto) {
		Geraetehistorie toRemove = em.find(Geraetehistorie.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeIskategorie(IskategorieDto dto) {
		Iskategorie toRemove = em.find(Iskategorie.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeGewerk(GewerkDto dto) {
		Gewerk toRemove = em.find(Gewerk.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeIsmaschine(IsmaschineDto dto) {
		Ismaschine toRemove = em.find(Ismaschine.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeGeraetetyp(GeraetetypDto dto) {
		Geraetetyp toRemove = em.find(Geraetetyp.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeWartungsliste(WartungslisteDto dto) {
		Wartungsliste toRemove = em.find(Wartungsliste.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeWartungsschritte(WartungsschritteDto dto) {
		Wartungsschritte toRemove = em.find(Wartungsschritte.class,
				dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAnlage(AnlageDto dto) {
		Anlage toRemove = em.find(Anlage.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setHalleFromHalleDto(Halle bean, HalleDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setStandortIId(dto.getStandortIId());
		em.merge(bean);
		em.flush();
	}

	private void setGeraetehistorieFromGeraetehistorieDto(Geraetehistorie bean,
			GeraetehistorieDto dto) {
		bean.setTWartung(dto.getTWartung());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setBNichtmoeglich(dto.getBNichtmoeglich());
		bean.setPersonalIIdTechniker(dto.getPersonalIIdTechniker());
		bean.setTAendern(dto.getTAendern());
		bean.setGeraetIId(dto.getGeraetIId());
		em.merge(bean);
		em.flush();
	}

	private void setStandorttechnikerFromStandorttechnikerDto(
			Standorttechniker bean, StandorttechnikerDto dto) {
		bean.setPersonalIId(dto.getPersonalIId());
		bean.setStandortIId(dto.getStandortIId());
		bean.setBVerantwortlicher(dto.getBVerantwortlicher());
		em.merge(bean);
		em.flush();
	}

	private void setIsmaschineFromIsmaschineDto(Ismaschine bean,
			IsmaschineDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setAnlageIId(dto.getAnlageIId());
		em.merge(bean);
		em.flush();
	}

	private void setGeraetetypFromGeraetetypDto(Geraetetyp bean,
			GeraetetypDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setMandantCNr(dto.getMandantCNr());
		em.merge(bean);
		em.flush();
	}

	private void setIskategorieFromIskategorieDto(Iskategorie bean,
			IskategorieDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setGewerkFromIsGewerkDto(Gewerk bean, GewerkDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setWartungslisteFromWartungslisteDto(Wartungsliste bean,
			WartungslisteDto dto) {
		bean.setGeraetIId(dto.getGeraetIId());
		bean.setBVerrechenbar(dto.getBVerrechenbar());
		bean.setBWartungsmaterial(dto.getBWartungsmaterial());
		bean.setTPersonalVeraltet(dto.getTVeraltet());
		bean.settVeraltet(dto.getTVeraltet());
		bean.setNMenge(dto.getNMenge());
		bean.setCVeraltet(dto.getCVeraltet());
		bean.setXBemerkung(dto.getXBemerkung());
		bean.setPersonalIIdVeraltet(dto.getPersonalIIdVeraltet());
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setCBez(dto.getCBez());
		bean.setISort(dto.getISort());
		em.merge(bean);
		em.flush();
	}

	private void setWartungsschritteFromWartungsschritteDto(
			Wartungsschritte bean, WartungsschritteDto dto) {
		bean.setGeraetIId(dto.getGeraetIId());
		bean.setAuftragwiederholungsintervallCNr(dto
				.getAuftragwiederholungsintervallCNr());
		bean.setLDauer(dto.getLDauer());
		bean.setTAbdurchfuehren(dto.getTAbdurchfuehren());
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setTagesartIId(dto.getTagesartIId());
		bean.setISort(dto.getISort());
		bean.setPersonalgruppeIId(dto.getPersonalgruppeIId());
		bean.setCBemerkung(dto.getCBemerkung());
		bean.setLieferantIId(dto.getLieferantIId());
		bean.setCBemerkunglieferant(dto.getCBemerkunglieferant());
		em.merge(bean);
		em.flush();
	}

	public Integer createStandort(StandortDto dto) {

		try {
			Query query = em
					.createNamedQuery("StandortFindByInstandhaltungIIdPartnerIId");
			query.setParameter(1, dto.getInstandhaltungIId());
			query.setParameter(2, dto.getPartnerIId());
			// @todo getSingleResult oder getResultList ?
			Standort doppelt = (Standort) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_STANDORT.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STANDORT);
			dto.setIId(pk);

			Standort bean = new Standort(dto.getIId(),
					dto.getInstandhaltungIId(), dto.getPartnerIId(),
					dto.getBVersteckt(), dto.getAuftragIId());
			em.persist(bean);
			em.flush();
			setStandortFromStandortDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createAnlage(AnlageDto dto) {

		try {
			Query query = em.createNamedQuery("AnlageFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Anlage doppelt = (Anlage) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_HALLE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ANLAGE);
			dto.setIId(pk);

			Anlage bean = new Anlage(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez(), dto.getHalleIId());
			em.persist(bean);
			em.flush();
			setAnlageFromAnlageDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGeraet(GeraetDto dto) {

	/*	try {
			Query query = em.createNamedQuery("GeraetFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Geraet doppelt = (Geraet) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IS_GERAET.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}*/

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GERAET);
			dto.setIId(pk);

			Geraet bean = new Geraet(dto.getIId(), dto.getMandantCNr(),
					 dto.getStandortIId(),
					dto.getGeraetetypIId(), dto.getBAufwand(),
					dto.getBMesswertabsolut(), dto.getBVersteckt());
			em.persist(bean);
			em.flush();
			setGeraetFromGeraetDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer geraetIId, int iSortierungNeuePositionI) {
		Query query = em.createNamedQuery("WartungslistefindByGeraetIId");
		query.setParameter(1, geraetIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Wartungsliste oPosition = (Wartungsliste) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}

	}

	public void sortierungWartungsschritteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer geraetIId, int iSortierungNeuePositionI) {
		Query query = em.createNamedQuery("WartungsschrittefindByGeraetIId");
		query.setParameter(1, geraetIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Wartungsschritte oPosition = (Wartungsschritte) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}

	}

	public void vertauscheWartungsliste(Integer iIdPosition1I,
			Integer iIdPosition2I) {
		Wartungsliste oPosition1 = em.find(Wartungsliste.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Wartungsliste oPosition2 = em.find(Wartungsliste.class, iIdPosition2I);
		if (oPosition2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);

	}

	public void vertauscheWartungsschritte(Integer iIdPosition1I,
			Integer iIdPosition2I) {
		Wartungsschritte oPosition1 = em.find(Wartungsschritte.class,
				iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Wartungsschritte oPosition2 = em.find(Wartungsschritte.class,
				iIdPosition2I);
		if (oPosition2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);

	}

	public void updateStandort(StandortDto dto) {
		Standort ialle = em.find(Standort.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("StandortFindByInstandhaltungIIdPartnerIId");
			query.setParameter(1, dto.getInstandhaltungIId());
			query.setParameter(2, dto.getPartnerIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Standort) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IS_STANDORT.UK"));
			}
		} catch (NoResultException ex) {

		}

		setStandortFromStandortDto(ialle, dto);
	}

	public StandortDto standortFindByPrimaryKey(Integer iId) {
		Standort ialle = em.find(Standort.class, iId);
		return StandortDtoAssembler.createDto(ialle);
	}
	public StandorttechnikerDto standorttechnikerFindByPrimaryKey(Integer iId) {
		Standorttechniker ialle = em.find(Standorttechniker.class, iId);
		return StandorttechnikerDtoAssembler.createDto(ialle);
	}

	public void removeStandort(StandortDto dto) {
		Standort toRemove = em.find(Standort.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeStandorttechniker(StandorttechnikerDto dto) {
		Standorttechniker toRemove = em.find(Standorttechniker.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setStandortFromStandortDto(Standort bean, StandortDto dto) {
		bean.setInstandhaltungIId(dto.getInstandhaltungIId());
		bean.setPartnerIId(dto.getPartnerIId());
		bean.setBVersteckt(dto.getBVersteckt());
		bean.setAnsprechpartnerIId(dto.getAnsprechpartnerIId());
		bean.setXBemerkung(dto.getXBemerkung());
		bean.setAuftragIId(dto.getAuftragIId());
		bean.setCDokumentenlink(dto.getCDokumentenlink());
		em.merge(bean);
		em.flush();
	}

	private void setAnlageFromAnlageDto(Anlage bean, AnlageDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setHalleIId(dto.getHalleIId());
		em.merge(bean);
		em.flush();
	}

	private void setGeraetFromGeraetDto(Geraet bean, GeraetDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setHalleIId(dto.getHalleIId());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setAnlageIId(dto.getAnlageIId());
		bean.setBAufwand(dto.getBAufwand());
		bean.setBMesswertabsolut(dto.getBMesswertabsolut());
		bean.setCLeistung(dto.getCLeistung());
		bean.setIAnzahl(dto.getIAnzahl());
		bean.setCGeraetesnr(dto.getCGeraetesnr());
		bean.setCStandort(dto.getCStandort());
		bean.setCVersorgungskreis(dto.getCVersorgungskreis());
		bean.setGeraetetypIId(dto.getGeraetetypIId());
		bean.setIsmaschineIId(dto.getIsmaschineIId());
		bean.setNGrenzwert(dto.getNGrenzwert());
		bean.setNGrenzwertmax(dto.getNGrenzwertmax());
		bean.setNGrenzwertmin(dto.getNGrenzwertmin());
		bean.setStandortIId(dto.getStandortIId());
		bean.setBVersteckt(dto.getBVersteckt());
		bean.setXBemerkung(dto.getXBemerkung());
		bean.setCFabrikat(dto.getCFabrikat());
		bean.setHerstellerIId(dto.getHerstellerIId());
		bean.setGewerkIId(dto.getGewerkIId());
		em.merge(bean);
		em.flush();
	}

}
