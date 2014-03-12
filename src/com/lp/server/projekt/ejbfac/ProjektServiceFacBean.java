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
package com.lp.server.projekt.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.ejb.Farbcode;
import com.lp.server.artikel.ejb.Vorschlagstext;
import com.lp.server.artikel.service.FarbcodeDto;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.artikel.service.VorschlagstextDtoAssembler;
import com.lp.server.partner.ejb.Kontaktart;
import com.lp.server.projekt.ejb.Projekterledigungsgrund;
import com.lp.server.projekt.service.ProjekterledigungsgrundDto;
import com.lp.server.projekt.service.ProjekterledigungsgrundDtoAssembler;
import com.lp.server.projekt.ejb.Bereich;
import com.lp.server.projekt.ejb.Historyart;
import com.lp.server.projekt.ejb.Kategorie;
import com.lp.server.projekt.ejb.KategoriePK;
import com.lp.server.projekt.ejb.Kategoriespr;
import com.lp.server.projekt.ejb.KategoriesprPK;
import com.lp.server.projekt.ejb.Projektstatus;
import com.lp.server.projekt.ejb.ProjektstatusPK;
import com.lp.server.projekt.ejb.Projekttyp;
import com.lp.server.projekt.ejb.ProjekttypPK;
import com.lp.server.projekt.ejb.Projekttypspr;
import com.lp.server.projekt.ejb.ProjekttypsprPK;
import com.lp.server.projekt.fastlanereader.generated.FLRKategorie;
import com.lp.server.projekt.fastlanereader.generated.FLRKategoriespr;
import com.lp.server.projekt.fastlanereader.generated.FLRProjektstatus;
import com.lp.server.projekt.fastlanereader.generated.FLRTyp;
import com.lp.server.projekt.fastlanereader.generated.FLRTypspr;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.BereichDtoAssembler;
import com.lp.server.projekt.service.HistoryartDto;
import com.lp.server.projekt.service.HistoryartDtoAssembler;
import com.lp.server.projekt.service.KategorieDto;
import com.lp.server.projekt.service.KategorieDtoAssembler;
import com.lp.server.projekt.service.KategoriesprDto;
import com.lp.server.projekt.service.KategoriesprDtoAssembler;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjektStatusDto;
import com.lp.server.projekt.service.ProjektStatusDtoAssembler;
import com.lp.server.projekt.service.ProjekttypDto;
import com.lp.server.projekt.service.ProjekttypDtoAssembler;
import com.lp.server.projekt.service.ProjekttypsprDto;
import com.lp.server.projekt.service.ProjekttypsprDtoAssembler;
import com.lp.server.stueckliste.ejb.Stuecklisteart;
import com.lp.server.stueckliste.ejb.Stuecklisteeigenschaftart;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.ejb.Status;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ProjektServiceFacBean extends Facade implements ProjektServiceFac {
	@PersistenceContext
	private EntityManager em;

	public String createKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		checkKategorieDto(kategorieDto);
		String cNrWieKey = null;
		try {
			Kategorie kategorie = new Kategorie(kategorieDto.getCNr(),
					kategorieDto.getISort(), kategorieDto.getMandantCNr());
			em.persist(kategorie);
			em.flush();
			setKategorieFromKategorieDto(kategorie, kategorieDto);
			KategoriePK pk = new KategoriePK(kategorieDto.getCNr(),
					kategorieDto.getMandantCNr());
			Kategorie tmpkategorie = em.find(Kategorie.class, pk);
			if (tmpkategorie == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			cNrWieKey = tmpkategorie.getPk().getCNr();

			if (kategorieDto.getKategoriesprDto() != null) {
				kategorieDto.getKategoriesprDto().setKategorieCNr(
						kategorieDto.getCNr());
				kategorieDto.getKategoriesprDto().setMandantCNr(
						kategorieDto.getMandantCNr());
				createKategoriespr(kategorieDto.getKategoriesprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return cNrWieKey;

	}

	public boolean sindErledigugnsgruendeVorhanden(TheClientDto theclientDto) {
		Query query = em
				.createNamedQuery("ProjekterledigungsgrundfindByMandantCnr");
		query.setParameter(1, theclientDto.getMandant());
		int i = query.getResultList().size();
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Integer createHistoryart(HistoryartDto historyartDto) {
		try {

			try {
				Query query = em.createNamedQuery("HistoryartFindByCBez");
				query.setParameter(1, historyartDto.getCBez());
				// @todo getSingleResult oder getResultList ?
				Historyart doppelt = (Historyart) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PROJ_HISTORYART.UK"));
			} catch (NoResultException ex1) {
				// nothing here
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HISTORYART);
			historyartDto.setIId(pk);

			Historyart historyart = new Historyart(historyartDto.getIId(),
					historyartDto.getCBez(), historyartDto.getIRot(),
					historyartDto.getIGruen(), historyartDto.getIBlau(),
					historyartDto.getBAktualisierezieltermin());
			em.persist(historyart);
			em.flush();
			setHistoryartFromHistoryartDto(historyart, historyartDto);
			return historyartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public Integer createBereich(BereichDto bereichDto,
			TheClientDto theClientDto) {
		try {

			try {
				Query query = em
						.createNamedQuery("BereichFindByMandantCNrCBez");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, bereichDto.getCBez());
				Bereich doppelt = (Bereich) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PROJ_BEREICH.UK"));
			} catch (NoResultException ex1) {
				// nothing here
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEREICH);
			bereichDto.setIId(pk);

			Integer i = null;
			try {
				Query querynext = em
						.createNamedQuery("BereichejbSelectNextReihung");
				querynext.setParameter(1, theClientDto.getMandant());
				i = (Integer) querynext.getSingleResult();

				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);

			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
			bereichDto.setISort(i);

			Bereich bereich = new Bereich(bereichDto.getIId(),
					theClientDto.getMandant(), bereichDto.getCBez(),
					bereichDto.getISort());
			em.persist(bereich);
			em.flush();
			setBereichFromBereichDto(bereich, bereichDto);
			return bereichDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void updateHistoryart(HistoryartDto historyartDto) {

		Integer iId = historyartDto.getIId();
		try {
			Historyart historyart = em.find(Historyart.class, iId);

			try {
				Query query = em.createNamedQuery("HistoryartFindByCBez");
				query.setParameter(1, historyartDto.getCBez());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Historyart) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PROJ_HISTORYART.UK"));
				}
			} catch (NoResultException ex) {

			}

			setHistoryartFromHistoryartDto(historyart, historyartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public void updateBereich(BereichDto bereichDto, TheClientDto theClientDto) {

		Integer iId = bereichDto.getIId();
		try {
			Bereich bereich = em.find(Bereich.class, iId);

			try {
				Query query = em
						.createNamedQuery("BereichFindByMandantCNrCBez");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, bereichDto.getCBez());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Bereich) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PROJ_BEREICH.UK"));
				}
			} catch (NoResultException ex) {

			}

			setBereichFromBereichDto(bereich, bereichDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	private void setHistoryartFromHistoryartDto(Historyart historyart,
			HistoryartDto historyartDto) {
		historyart.setCBez(historyartDto.getCBez());
		historyart.setIBlau(historyartDto.getIBlau());
		historyart.setIRot(historyartDto.getIRot());
		historyart.setIGruen(historyartDto.getIGruen());
		historyart.setBAktualisierezieltermin(historyartDto
				.getBAktualisierezieltermin());
		em.merge(historyart);
		em.flush();
	}

	private void setBereichFromBereichDto(Bereich bereich, BereichDto bereichDto) {
		bereich.setCBez(bereichDto.getCBez());
		bereich.setISort(bereichDto.getISort());
		bereich.setMandantCNr(bereichDto.getMandantCNr());
		em.merge(bereich);
		em.flush();
	}

	public void removeKategorie(String cNr) throws EJBExceptionLP {
		Kategorie toRemove = em.find(Kategorie.class, cNr);
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

	public void removeKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkKategorieDto(kategorieDto);
		try {
			KategoriePK pk = new KategoriePK(kategorieDto.getCNr(),
					kategorieDto.getMandantCNr());
			Query query = em
					.createNamedQuery("KategoriesprfindByKategorieCNrAndMandantCNr");
			query.setParameter(1, kategorieDto.getCNr());
			query.setParameter(2, kategorieDto.getMandantCNr());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Kategoriespr item = (Kategoriespr) iter.next();
				em.remove(item);
			}
			Kategorie kategorie = em.find(Kategorie.class, pk);
			if (kategorie == null) { // @ToDo null Pruefung?
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(kategorie);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateKategorie(KategorieDto kategorieDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkKategorieDto(kategorieDto);
		myLogger.logData(kategorieDto);
		if (kategorieDto != null) {
			try {
				KategoriePK pk = new KategoriePK(kategorieDto.getCNr(),
						kategorieDto.getMandantCNr());
				Kategorie kategorie = em.find(Kategorie.class, pk);
				setKategorieFromKategorieDto(kategorie, kategorieDto);
				// sprache
				if (kategorieDto.getKategoriesprDto() != null) {
					kategorieDto.getKategoriesprDto().setKategorieCNr(
							kategorieDto.getCNr());
					kategorieDto.getKategoriesprDto().setLocaleCNr(
							theClientDto.getLocUiAsString());
					kategorieDto.getKategoriesprDto().setMandantCNr(
							kategorieDto.getMandantCNr());

					updateKategoriespr(kategorieDto.getKategoriesprDto());
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public KategorieDto kategorieFindByPrimaryKey(String cNrI,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}

		KategorieDto kategorieDto = null;

		// try {
		KategoriePK pk = new KategoriePK(cNrI, mandantCNr);
		Kategorie kategorie = em.find(Kategorie.class, pk);
		if (kategorie == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		kategorieDto = assembleKategorieDto(kategorie);

		// jetzt die Spr
		KategoriesprDto kategoriesprDto = null;

		try {
			KategoriesprPK kategoriesprPK = new KategoriesprPK(
					kategorieDto.getCNr(), theClientDto.getLocUiAsString(),
					kategorieDto.getMandantCNr());
			Kategoriespr kategoriespr = em.find(Kategoriespr.class,
					kategoriesprPK);
			if (kategoriespr != null) {
				kategoriesprDto = assembleKategoriesprDto(kategoriespr);
			}
		} catch (Throwable t) {
			// ignore
		}

		kategorieDto.setKategoriesprDto(kategoriesprDto);
		return kategorieDto;
	}

	public void removeHistoryart(HistoryartDto dto) {

		Historyart toRemove = em.find(Historyart.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeBereich(BereichDto dto) {

		Bereich toRemove = em.find(Bereich.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public HistoryartDto historyartFindByPrimaryKey(Integer iId) {
		Historyart historyart = em.find(Historyart.class, iId);
		return HistoryartDtoAssembler.createDto(historyart);
	}

	public BereichDto bereichFindByPrimaryKey(Integer iId) {
		Bereich bereich = em.find(Bereich.class, iId);
		return BereichDtoAssembler.createDto(bereich);
	}

	private void setKategorieFromKategorieDto(Kategorie kategorie,
			KategorieDto kategorieDto) {
		kategorie.setISort(kategorieDto.getISort());

		em.merge(kategorie);
		em.flush();
	}

	private KategorieDto assembleKategorieDto(Kategorie kategorie) {
		return KategorieDtoAssembler.createDto(kategorie);
	}

	/**
	 * Alle Kategorie in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllBereich(TheClientDto theClientDto) {

		TreeMap<Integer, String> tmArten = new TreeMap<Integer, String>();

		Query query = em.createNamedQuery("BereichFindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Bereich stuecklisteart = (Bereich) itArten.next();

			tmArten.put(stuecklisteart.getIId(), stuecklisteart.getCBez());
		}

		return tmArten;
	}

	public Map getKategorie(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getKategorie";
		myLogger.entry();
		Map<String, String> map = new LinkedHashMap<String, String>();
		String sLocale = Helper.locale2String(locale);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cKategorie = session.createCriteria(FLRKategorie.class);

			cKategorie.add(Restrictions.eq("PK."
					+ ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));
			cKategorie.addOrder(Order.asc("i_sort"));
			List<?> lKategorie = cKategorie.list();
			for (Iterator<?> iter = lKategorie.iterator(); iter.hasNext();) {
				FLRKategorie kategorie = (FLRKategorie) iter.next();

				Iterator<?> sprsetIterator = kategorie
						.getKategorie_kategorie_set().iterator();
				if (sprsetIterator.hasNext()) {
					// gibt es eine uebersetzung
					while (sprsetIterator.hasNext()) {
						FLRKategoriespr kategoriespr = (FLRKategoriespr) sprsetIterator
								.next();
						if (kategoriespr.getLocale_c_nr().trim()
								.equals(sLocale.trim())) {
							map.put(kategorie.getPK().getC_nr(),
									kategoriespr.getC_bez());

							break;
						}
					}

				} else {
					// wenn nicht c_nr verwenden
					map.put(kategorie.getPK().getC_nr(), kategorie.getPK()
							.getC_nr());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return map;
	}

	public Map getProjektStatus(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getProjektStatus";
		myLogger.entry();
		Map<String, String> map = new LinkedHashMap<String, String>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cStatus = session.createCriteria(FLRProjektstatus.class);
			// session.enableFilter("filterLocale").setParameter("paramLocale",
			// sLocale);
			cStatus.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));
			cStatus.addOrder(Order.asc("i_sort"));
			List<?> lStatus = cStatus.list();
			for (Iterator<?> iter = lStatus.iterator(); iter.hasNext();) {
				FLRProjektstatus status = (FLRProjektstatus) iter.next();
				// wenn nicht c_nr verwenden
				map.put(status.getStatus_c_nr(), status.getStatus_c_nr());
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return map;
	}

	public Map getTyp(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getTyp";
		myLogger.entry();
		Map<String, String> map = new LinkedHashMap<String, String>();
		String sLocale = Helper.locale2String(locale);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cTyp = session.createCriteria(FLRTyp.class);
			// session.enableFilter("filterLocale").setParameter("paramLocale",
			// sLocale);
			cTyp.add(Restrictions.eq("PK."
					+ ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));
			cTyp.addOrder(Order.asc("i_sort"));
			List<?> lTyp = cTyp.list();
			for (Iterator<?> iter = lTyp.iterator(); iter.hasNext();) {
				FLRTyp typ = (FLRTyp) iter.next();
				Iterator<?> sprsetIterator = typ.getTyp_typ_set().iterator();
				if (sprsetIterator.hasNext()) {
					// gibt es eine uebersetzung
					boolean bSpracheGefunden = false;
					while (sprsetIterator.hasNext()) {
						FLRTypspr spr = (FLRTypspr) sprsetIterator.next();
						if (spr.getLocale_c_nr().trim().equals(sLocale.trim())) {
							map.put(typ.getPK().getC_nr(), spr.getC_bez());
							bSpracheGefunden = true;
							break;
						}
					}
					if (bSpracheGefunden == false) {
						map.put(typ.getPK().getC_nr(), typ.getPK().getC_nr());
					}
				} else {
					// wenn nicht c_nr verwenden
					map.put(typ.getPK().getC_nr(), typ.getPK().getC_nr());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return map;
	}

	public void createKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP {
		checkKategoriesprDto(kategoriesprDto);
		try {
			Kategoriespr kategoriespr = new Kategoriespr(
					kategoriesprDto.getKategorieCNr(),
					kategoriesprDto.getCBez(), kategoriesprDto.getLocaleCNr(),
					kategoriesprDto.getMandantCNr());
			em.persist(kategoriespr);
			em.flush();
			setKategoriesprFromKategoriesprDto(kategoriespr, kategoriesprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeKategoriespr(String kategorieCNr, String localeCNr)
			throws EJBExceptionLP {
		KategoriesprPK kategoriesprPK = new KategoriesprPK();
		kategoriesprPK.setKategorieCNr(kategorieCNr);
		kategoriesprPK.setLocaleCNr(localeCNr);
		Kategoriespr toRemove = em.find(Kategoriespr.class, kategoriesprPK);
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

	public void removeKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP {
		if (kategoriesprDto != null) {
			String projektkategorieCNr = kategoriesprDto.getKategorieCNr();
			String localeCNr = kategoriesprDto.getLocaleCNr();
			removeKategoriespr(projektkategorieCNr, localeCNr);
		}
	}

	public void updateKategoriespr(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP {
		checkKategoriesprDto(kategoriesprDto);
		myLogger.logData(kategoriesprDto);
		if (kategoriesprDto != null) {
			String cNr = kategoriesprDto.getKategorieCNr();
			String localeCNr = kategoriesprDto.getLocaleCNr();
			String mandantCNr = kategoriesprDto.getMandantCNr();
			try {
				KategoriesprPK kategoriesprPK = new KategoriesprPK(cNr,
						localeCNr, mandantCNr);
				Kategoriespr kategoriespr = em.find(Kategoriespr.class,
						kategoriesprPK);
				if (kategoriespr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createKategoriespr(kategoriesprDto);
				} else {
					setKategoriesprFromKategoriesprDto(kategoriespr,
							kategoriesprDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public Integer createProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto,
			TheClientDto theClientDto) {

		if (projekterledigungsgrundDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("projekterledigungsgrundDto == null"));
		}
		if (projekterledigungsgrundDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"projekterledigungsgrundDto.getCBez() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ProjekterledigungsgrundfindByMandantCnrCBez");
			query.setParameter(1, projekterledigungsgrundDto.getMandantCNr());
			query.setParameter(2, projekterledigungsgrundDto.getCBez());
			Projekterledigungsgrund doppelt = (Projekterledigungsgrund) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PROJ_PROJEKTERLEDIGUNGSGRUND.C_BEZ"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_PROJEKTERLEDIGUNGSGRUND);
		projekterledigungsgrundDto.setIId(pk);

		try {
			Projekterledigungsgrund projekterledigungsgrund = new Projekterledigungsgrund(
					projekterledigungsgrundDto.getIId(),
					projekterledigungsgrundDto.getMandantCNr(),
					projekterledigungsgrundDto.getCBez());
			em.persist(projekterledigungsgrund);
			em.flush();
			setProjekterledigungsgrundFromProjekterledigungsgrundDto(
					projekterledigungsgrund, projekterledigungsgrundDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return projekterledigungsgrundDto.getIId();
	}

	/**
	 * Entfernt eine vorhandene Projekterledigungsgrund
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeProjekterledigungsgrund(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Projekterledigungsgrund projekterledigungsgrund = em.find(
					Projekterledigungsgrund.class, iId);
			if (projekterledigungsgrund == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(projekterledigungsgrund);
			em.flush();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Speichert &auml;nderungen einer Projekterledigungsgrund
	 * 
	 * @param projekterledigungsgrundDto
	 *            ProjekterledigungsgrundDto
	 * @throws EJBExceptionLP
	 */
	public void updateProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto) {
		if (projekterledigungsgrundDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("projekterledigungsgrundDto == null"));
		}
		if (projekterledigungsgrundDto.getIId() == null
				|| projekterledigungsgrundDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"projekterledigungsgrundDto.getIId() == null || projekterledigungsgrundDto.getCBez() == null"));
		}

		Integer iId = projekterledigungsgrundDto.getIId();
		// try {
		Projekterledigungsgrund projekterledigungsgrund = em.find(
				Projekterledigungsgrund.class, iId);
		if (projekterledigungsgrund == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("ProjekterledigungsgrundfindByMandantCnrCBez");
			query.setParameter(1, projekterledigungsgrundDto.getMandantCNr());
			query.setParameter(2, projekterledigungsgrundDto.getCBez());
			Integer iIdVorhanden = ((Projekterledigungsgrund) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PROJ_PROJEKTERLEDIGUNGSGRUND.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setProjekterledigungsgrundFromProjekterledigungsgrundDto(
				projekterledigungsgrund, projekterledigungsgrundDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	/**
	 * Holt eine bestimmte Projekterledigungsgrund
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return ProjekterledigungsgrundDto
	 */
	public ProjekterledigungsgrundDto projekterledigungsgrundFindByPrimaryKey(
			Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		try {
			Projekterledigungsgrund projekterledigungsgrund = em.find(
					Projekterledigungsgrund.class, iId);
			if (projekterledigungsgrund == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleProjekterledigungsgrundDto(projekterledigungsgrund);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setProjekterledigungsgrundFromProjekterledigungsgrundDto(
			Projekterledigungsgrund projekterledigungsgrund,
			ProjekterledigungsgrundDto projekterledigungsgrundDto) {
		projekterledigungsgrund.setCBez(projekterledigungsgrundDto.getCBez());
		em.merge(projekterledigungsgrund);
		em.flush();
	}

	private ProjekterledigungsgrundDto assembleProjekterledigungsgrundDto(
			Projekterledigungsgrund projekterledigungsgrund) {
		return ProjekterledigungsgrundDtoAssembler
				.createDto(projekterledigungsgrund);
	}

	public void updateKategoriesprs(KategoriesprDto[] kategoriesprDtos)
			throws EJBExceptionLP {
		if (kategoriesprDtos != null) {
			for (int i = 0; i < kategoriesprDtos.length; i++) {
				updateKategoriespr(kategoriesprDtos[i]);
			}
		}
	}

	public KategoriesprDto kategoriesprFindByPrimaryKey(
			String projektkategorieCNr, String localeCNr) throws EJBExceptionLP {
		KategoriesprPK kategoriesprPK = new KategoriesprPK();
		kategoriesprPK.setKategorieCNr(projektkategorieCNr);
		kategoriesprPK.setLocaleCNr(localeCNr);
		Kategoriespr kategoriespr = em.find(Kategoriespr.class, kategoriesprPK);
		if (kategoriespr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKategoriesprDto(kategoriespr);
	}

	private void setKategoriesprFromKategoriesprDto(Kategoriespr kategoriespr,
			KategoriesprDto kategoriesprDto) {
		kategoriespr.setCBez(kategoriesprDto.getCBez());
		em.merge(kategoriespr);
		em.flush();
	}

	private KategoriesprDto assembleKategoriesprDto(Kategoriespr kategoriespr) {
		return KategoriesprDtoAssembler.createDto(kategoriespr);
	}

	private void checkKategorieDto(KategorieDto kategorieDto)
			throws EJBExceptionLP {
		if (kategorieDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kategorieDto == null"));
		}
		if (kategorieDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getKategorieCNr() == null"));
		}
		if (kategorieDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategorieDto.getMandantCNr() == null"));
		}
		myLogger.info("KategorieDto: " + kategorieDto.toString());
	}

	private void checkKategoriesprDto(KategoriesprDto kategoriesprDto)
			throws EJBExceptionLP {
		if (kategoriesprDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kategoriesprDto == null"));
		}
		if (kategoriesprDto.getKategorieCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getKategorieCNr() == null"));
		}
		if (kategoriesprDto.getLocaleCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getLocaleCNr() == null"));
		}
		if (kategoriesprDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getMandantCNr() == null"));
		}
		myLogger.info("KategorieDto: " + kategoriesprDto.toString());
	}

	private void checkProjekttypDto(ProjekttypDto projekttypDto)
			throws EJBExceptionLP {
		if (projekttypDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("projekttypDto == null"));
		}
		if (projekttypDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("projekttypDto.getCNr() == null"));
		}
		if (projekttypDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("projekttypDto.getMandantCNr() == null"));
		}
		myLogger.info("ProjekttypDto: " + projekttypDto.toString());
	}

	private void checkProjekttypsprDto(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP {
		if (projekttypsprDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kategoriesprDto == null"));
		}
		if (projekttypsprDto.getTypCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getKategorieCNr() == null"));
		}
		if (projekttypsprDto.getLocaleCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getLocaleCNr() == null"));
		}
		if (projekttypsprDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kategoriesprDto.getMandantCNr() == null"));
		}
		myLogger.info("KategorieDto: " + projekttypsprDto.toString());
	}

	public String createProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		// precondition.
		if (projekttypDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("projekttypDto == null"));
		}

		if (projekttypDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("projekttypDto.getCNr() == null"));
		}
		String cNrWieKey = null;

		try {
			Projekttyp projekttyp = new Projekttyp(projekttypDto.getCNr(),
					projekttypDto.getISort(), projekttypDto.getMandantCNr());
			em.persist(projekttyp);
			em.flush();
			setProjekttypFromProjekttypDto(projekttyp, projekttypDto);
			ProjekttypPK pk = new ProjekttypPK(projekttypDto.getCNr(),
					projekttypDto.getMandantCNr());
			Projekttyp tmpprojekttyp = em.find(Projekttyp.class, pk);
			if (tmpprojekttyp == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			cNrWieKey = tmpprojekttyp.getPk().getCNr();

			if (projekttypDto.getProjekttypsprDto() != null) {
				projekttypDto.getProjekttypsprDto().setTypCNr(
						projekttypDto.getCNr());
				projekttypDto.getProjekttypsprDto().setMandantCNr(
						projekttypDto.getMandantCNr());
				createProjekttypspr(projekttypDto.getProjekttypsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return cNrWieKey;
	}

	public void removeProjekttyp(String cNr) throws EJBExceptionLP {
		Projekttyp toRemove = em.find(Projekttyp.class, cNr);
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

	public void removeProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkProjekttypDto(projekttypDto);
		try {
			ProjekttypPK pk = new ProjekttypPK(projekttypDto.getCNr(),
					projekttypDto.getMandantCNr());
			Query query = em
					.createNamedQuery("ProjekttypsprfindByProjekttypCNrAndMandantCNr");
			query.setParameter(1, projekttypDto.getCNr());
			query.setParameter(2, projekttypDto.getMandantCNr());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Projekttypspr item = (Projekttypspr) iter.next();
				em.remove(item);
			}
			Projekttyp projekttyp = em.find(Projekttyp.class, pk);
			if (projekttyp == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(projekttyp);
			em.flush();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateProjekttyp(ProjekttypDto projekttypDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkProjekttypDto(projekttypDto);
		myLogger.logData(projekttypDto);
		if (projekttypDto != null) {
			try {
				ProjekttypPK pk = new ProjekttypPK(projekttypDto.getCNr(),
						projekttypDto.getMandantCNr());
				Projekttyp projekttyp = em.find(Projekttyp.class, pk);
				setProjekttypFromProjekttypDto(projekttyp, projekttypDto);
				// sprache
				if (projekttypDto.getProjekttypsprDto() != null) {
					projekttypDto.getProjekttypsprDto().setTypCNr(
							projekttypDto.getCNr());
					projekttypDto.getProjekttypsprDto().setLocaleCNr(
							theClientDto.getLocUiAsString());
					projekttypDto.getProjekttypsprDto().setMandantCNr(
							projekttypDto.getMandantCNr());

					updateProjekttypspr(projekttypDto.getProjekttypsprDto());
				}

			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}

	}

	public ProjekttypDto projekttypFindByPrimaryKey(String cNrI,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		// check2(cNrUserI);

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}

		ProjekttypDto projekttypDto = null;

		// try {
		ProjekttypPK pk = new ProjekttypPK(cNrI, mandantCNr);
		Projekttyp projekttyp = em.find(Projekttyp.class, pk);
		if (projekttyp == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		projekttypDto = assembleProjekttypDto(projekttyp);

		// jetzt die Spr
		ProjekttypsprDto projekttypsprDto = null;

		try {
			ProjekttypsprPK projekttypsprPK = new ProjekttypsprPK(
					projekttypDto.getCNr(), theClientDto.getLocUiAsString(),
					projekttypDto.getMandantCNr());
			Projekttypspr projekttypspr = em.find(Projekttypspr.class,
					projekttypsprPK);
			if (projekttypspr != null) {
				projekttypsprDto = assembleProjekttypsprDto(projekttypspr);
			}
		} catch (Throwable t) {
			// ignore
		}

		projekttypDto.setProjekttypsprDto(projekttypsprDto);
		return projekttypDto;
	}

	private void setProjekttypFromProjekttypDto(Projekttyp projekttyp,
			ProjekttypDto projekttypDto) {
		projekttyp.setISort(projekttypDto.getISort());
		em.merge(projekttyp);
		em.flush();
	}

	private ProjekttypDto assembleProjekttypDto(Projekttyp projekttyp) {
		return ProjekttypDtoAssembler.createDto(projekttyp);
	}

	public void createProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP {
		checkProjekttypsprDto(projekttypsprDto);
		try {
			Projekttypspr projekttypspr = new Projekttypspr(
					projekttypsprDto.getTypCNr(),
					projekttypsprDto.getLocaleCNr(),
					projekttypsprDto.getCBez(),
					projekttypsprDto.getMandantCNr());
			em.persist(projekttypspr);
			em.flush();
			setProjekttypsprFromProjekttypsprDto(projekttypspr,
					projekttypsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeProjekttypspr(String typCNr, String localeCNr)
			throws EJBExceptionLP {
		ProjekttypsprPK projekttypsprPK = new ProjekttypsprPK();
		projekttypsprPK.setProjekttypCNr(typCNr);
		projekttypsprPK.setLocaleCNr(localeCNr);
		Projekttypspr toRemove = em.find(Projekttypspr.class, projekttypsprPK);
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

	public void removeProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP {
		if (projekttypsprDto != null) {
			String typCNr = projekttypsprDto.getTypCNr();
			String localeCNr = projekttypsprDto.getLocaleCNr();
			removeProjekttypspr(typCNr, localeCNr);
		}
	}

	public void vertauscheBereich(Integer iId1I, Integer iId2I) {

		Bereich o1 = em.find(Bereich.class, iId1I);
		if (o1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Bereich o2 = em.find(Bereich.class, iId2I);
		if (o2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));

		o1.setISort(iSort2);
		o2.setISort(iSort1);

	}

	public void updateProjekttypspr(ProjekttypsprDto projekttypsprDto)
			throws EJBExceptionLP {
		checkProjekttypsprDto(projekttypsprDto);
		if (projekttypsprDto != null) {
			String cNr = projekttypsprDto.getTypCNr();
			String localeCNr = projekttypsprDto.getLocaleCNr();
			String mandantCNr = projekttypsprDto.getMandantCNr();
			ProjekttypsprPK projekttypsprPK = new ProjekttypsprPK(cNr,
					localeCNr, mandantCNr);
			Projekttypspr projekttypspr = em.find(Projekttypspr.class,
					projekttypsprPK);

			if (projekttypspr == null) {
				// diese Uebersetzung gibt es nocht nicht
				createProjekttypspr(projekttypsprDto);
			} else {
				setProjekttypsprFromProjekttypsprDto(projekttypspr,
						projekttypsprDto);
			}
		}
	}

	public ProjekttypsprDto projekttypsprFindByPrimaryKey(String typCNr,
			String localeCNr, String mandantCNr) throws EJBExceptionLP {
		ProjekttypsprPK projekttypsprPK = new ProjekttypsprPK();
		projekttypsprPK.setProjekttypCNr(typCNr);
		projekttypsprPK.setLocaleCNr(localeCNr);
		projekttypsprPK.setMandantCNr(mandantCNr);
		Projekttypspr projekttypspr = em.find(Projekttypspr.class,
				projekttypsprPK);
		if (projekttypspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleProjekttypsprDto(projekttypspr);
	}

	public ProjekttypsprDto projekttypsprFindByPrimaryKeyohneEx(String typCNr,
			String localeCNr, String mandantCNr) throws EJBExceptionLP {
		ProjekttypsprPK projekttypsprPK = new ProjekttypsprPK();
		projekttypsprPK.setProjekttypCNr(typCNr);
		projekttypsprPK.setLocaleCNr(localeCNr);
		projekttypsprPK.setMandantCNr(mandantCNr);
		Projekttypspr projekttypspr = em.find(Projekttypspr.class,
				projekttypsprPK);
		if (projekttypspr == null) {
			return null;
		}
		return assembleProjekttypsprDto(projekttypspr);
	}

	private void setProjekttypsprFromProjekttypsprDto(
			Projekttypspr projekttypspr, ProjekttypsprDto projekttypsprDto) {
		projekttypspr.setCBez(projekttypsprDto.getCBez());
		em.merge(projekttypspr);
		em.flush();
	}

	private ProjekttypsprDto assembleProjekttypsprDto(
			Projekttypspr projekttypspr) {
		return ProjekttypsprDtoAssembler.createDto(projekttypspr);
	}

	public void removeKontaktart(String cNr) throws EJBExceptionLP {
		Kontaktart toRemove = em.find(Kontaktart.class, cNr);
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

	public String createProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Status status = em.find(Status.class, projektStatusDto.getCNr());
		if (status == null) {
			status = new Status(projektStatusDto.getCNr());
			try {
				em.persist(status);
			} catch (EntityExistsException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS, e);
			}
		}
		String cNrWieKey = null;
		Projektstatus projektStatus = new Projektstatus(
				projektStatusDto.getISort(), projektStatusDto.getMandantCNr(),
				projektStatusDto.getCNr(),
				projektStatusDto.getBAenderungprotokollieren());
		em.persist(projektStatus);
		em.flush();
		setProjektStatusFromProjektStatusDto(projektStatus, projektStatusDto);
		ProjektstatusPK pk = new ProjektstatusPK(projektStatusDto.getCNr(),
				projektStatusDto.getMandantCNr());
		Projektstatus projektstatus = em.find(Projektstatus.class, pk);
		if (projektstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		cNrWieKey = projektstatus.getPk().getCNr();

		return cNrWieKey;
	}

	public void removeProjektStatus(String statusCNr, String mandantCNr)
			throws EJBExceptionLP {
		ProjektstatusPK pk = new ProjektstatusPK(statusCNr, mandantCNr);
		Projektstatus toRemove = em.find(Projektstatus.class, pk);
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

	public void removeProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (projektStatusDto != null) {
			removeProjektStatus(projektStatusDto.getCNr(),
					projektStatusDto.getMandantCNr());
		}
	}

	public void updateProjektStatus(ProjektStatusDto projektStatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (projektStatusDto != null) {
			ProjektstatusPK pk = new ProjektstatusPK(projektStatusDto.getCNr(),
					projektStatusDto.getMandantCNr());
			Projektstatus projektStatus = em.find(Projektstatus.class, pk);
			if (projektStatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setProjektStatusFromProjektStatusDto(projektStatus,
					projektStatusDto);
		}
	}

	public ProjektStatusDto projektStatusFindByPrimaryKey(String statusCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ProjektstatusPK pk = new ProjektstatusPK(statusCNr,
				theClientDto.getMandant());
		if (statusCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}

		Projektstatus projektstatus = em.find(Projektstatus.class, pk);
		if (projektstatus == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleProjektStatusDto(projektstatus);

	}

	public ProjektStatusDto[] projektStatusFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("ProjektStatusfindAll");
		Collection<?> cl = query.getResultList();
		return assembleProjektStatusDtos(cl);

	}

	private void setProjektStatusFromProjektStatusDto(
			Projektstatus projektStatus, ProjektStatusDto projektStatusDto) {
		projektStatus.setISort(projektStatusDto.getISort());
		projektStatus.setBAenderungprotokollieren(projektStatusDto
				.getBAenderungprotokollieren());
		em.merge(projektStatus);
		em.flush();
	}

	private ProjektStatusDto assembleProjektStatusDto(
			Projektstatus projektStatus) {
		return ProjektStatusDtoAssembler.createDto(projektStatus);
	}

	private ProjektStatusDto[] assembleProjektStatusDtos(
			Collection<?> projektStatuss) {
		List<ProjektStatusDto> list = new ArrayList<ProjektStatusDto>();
		if (projektStatuss != null) {
			Iterator<?> iterator = projektStatuss.iterator();
			while (iterator.hasNext()) {
				Projektstatus projektStatus = (Projektstatus) iterator.next();
				list.add(assembleProjektStatusDto(projektStatus));
			}
		}
		ProjektStatusDto[] returnArray = new ProjektStatusDto[list.size()];
		return (ProjektStatusDto[]) list.toArray(returnArray);
	}

}
