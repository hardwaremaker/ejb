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
package com.lp.server.artikel.ejbfac;

import java.sql.Timestamp;
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

import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentarart;
import com.lp.server.artikel.ejb.Artikelkommentarartspr;
import com.lp.server.artikel.ejb.ArtikelkommentarartsprPK;
import com.lp.server.artikel.ejb.Artikelkommentardruck;
import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprPK;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarartDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarartsprDto;
import com.lp.server.artikel.service.ArtikelkommentarartsprDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentardruckDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ArtikelkommentarFacBean extends Facade implements
		ArtikelkommentarFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarartDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
			query.setParameter(1, artikelkommentarartDto.getCNr());
			Artikelkommentarart doppelt = (Artikelkommentarart) query
					.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTAR.CNR"));
		} catch (NoResultException ex) {
			//
		}

		try {

			if (Helper.short2boolean(artikelkommentarartDto.getBWebshop())) {
				Query query1 = em
						.createNamedQuery("ArtikelkommentarartfindByBWebshop");

				Artikelkommentarart doppelt = (Artikelkommentarart) query1
						.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELKOMMENTAR.B_WEBSHOP"));
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		if (artikelkommentarartDto.getBTooltip() == null) {
			artikelkommentarartDto.setBTooltip(Helper.boolean2Short(false));
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTARART);
			artikelkommentarartDto.setIId(pk);

			Artikelkommentarart artikelkommentarart = new Artikelkommentarart(
					artikelkommentarartDto.getIId(),
					artikelkommentarartDto.getCNr(),
					artikelkommentarartDto.getBWebshop(),
					artikelkommentarartDto.getBTooltip());
			em.persist(artikelkommentarart);
			em.flush();
			setArtikelkommentarartFromArtikelkommentarartDto(
					artikelkommentarart, artikelkommentarartDto);
			if (artikelkommentarartDto.getArtikelkommentartartsprDto() != null) {
				Artikelkommentarartspr artikelkommentarartspr = new Artikelkommentarartspr(
						artikelkommentarartDto.getIId(),
						theClientDto.getLocUiAsString());
				em.persist(artikelkommentarartspr);
				em.flush();
				setArtikelkommentarartsprFromArtikelkommentarartsprDto(
						artikelkommentarartspr,
						artikelkommentarartDto.getArtikelkommentartartsprDto());
			}
			return artikelkommentarartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarartDto.getIId() == null"));
		}
		// try {
		try {
			Query query = em
					.createNamedQuery("ArtikelkommentarartsprfindByArtikelkommentarartIId");
			query.setParameter(1, artikelkommentarartDto.getIId());
			Collection<?> allArtklaspr = query.getResultList();
			Iterator<?> iter = allArtklaspr.iterator();
			while (iter.hasNext()) {
				Artikelkommentarartspr artklasprTemp = (Artikelkommentarartspr) iter
						.next();
				em.remove(artklasprTemp);
			}
			Artikelkommentarart artikelkommentarart = em.find(
					Artikelkommentarart.class, artikelkommentarartDto.getIId());
			if (artikelkommentarart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"FEhler bei removeArtikelkommentarart. Es gibt keine iid "
								+ artikelkommentarartDto.getIId()
								+ "\ndto.toString(): "
								+ artikelkommentarartDto.toString());
			}
			em.remove(artikelkommentarart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public void updateArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getIId() == null
				|| artikelkommentarartDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelkommentarartDto.getIId() == null || artikelkommentarartDto.getCNr() == null"));
		}
		Integer iId = artikelkommentarartDto.getIId();

		Artikelkommentarart artikelkommentarart = null;
		// try {
		artikelkommentarart = em.find(Artikelkommentarart.class, iId);
		if (artikelkommentarart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentarart. Es gibt keine Kommentarart mit der iid "
							+ iId + "\ndto.toString: "
							+ artikelkommentarartDto.toString());

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
			query.setParameter(1, artikelkommentarartDto.getCNr());
			Integer iIdVorhanden = ((Artikelkommentarart) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELKOMMENTARART.UC"));
			}
		} catch (NoResultException ex) {
			//
		}

		try {

			if (Helper.short2boolean(artikelkommentarartDto.getBWebshop())) {
				Query query1 = em
						.createNamedQuery("ArtikelkommentarartfindByBWebshop");

				Artikelkommentarart doppelt = (Artikelkommentarart) query1
						.getSingleResult();
				if (iId.equals(doppelt.getIId()) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_ARTIKELKOMMENTAR.B_WEBSHOP"));
				}
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		try {
			setArtikelkommentarartFromArtikelkommentarartDto(
					artikelkommentarart, artikelkommentarartDto);

			if (artikelkommentarartDto.getArtikelkommentartartsprDto() != null) {
				// try {
				Artikelkommentarartspr artklaspr = em.find(
						Artikelkommentarartspr.class,
						new ArtikelkommentarartsprPK(iId, theClientDto
								.getLocUiAsString()));
				if (artklaspr == null) {
					artklaspr = new Artikelkommentarartspr(iId,
							theClientDto.getLocUiAsString());
					em.persist(artklaspr);
					em.flush();
					setArtikelkommentarartsprFromArtikelkommentarartsprDto(
							artklaspr,
							artikelkommentarartDto
									.getArtikelkommentartartsprDto());
				}
				setArtikelkommentarartsprFromArtikelkommentarartsprDto(
						artklaspr,
						artikelkommentarartDto.getArtikelkommentartartsprDto());
				// }
				// catch (FinderException ex) {
				// Artikelkommentarartspr artklaspr = new
				// Artikelkommentarartspr(
				// iId,getTheClient(cNrUserI).getLocUiAsString());
				// em.persist(artikelkommentarartspr);
				// setArtikelkommentarartsprFromArtikelkommentarartsprDto(
				// artklaspr,
				// artikelkommentarartDto.getArtikelkommentartartsprDto());
				// }
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public ArtikelkommentarartDto artikelkommentarartFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikelkommentarart artikelkommentarart = em.find(
				Artikelkommentarart.class, iId);
		if (artikelkommentarart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarartFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}

		ArtikelkommentarartDto artikelkommentarartDto = assembleArtikelkommentarartDto(artikelkommentarart);
		ArtikelkommentarartsprDto artikelkommentarartsprDto = null;
		// try {
		Artikelkommentarartspr artikelkommentarartspr = em.find(
				Artikelkommentarartspr.class, new ArtikelkommentarartsprPK(iId,
						theClientDto.getLocUiAsString()));
		if (artikelkommentarartspr == null) {
			// nothing here
		} else {
			artikelkommentarartsprDto = assembleArtikelkommentarartsprDto(artikelkommentarartspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artikelkommentarartsprDto == null) {
			// try {
			Artikelkommentarartspr temp = em.find(
					Artikelkommentarartspr.class,
					new ArtikelkommentarartsprPK(iId, theClientDto
							.getLocKonzernAsString()));
			if (temp == null) {
				// nothing here
			} else {
				artikelkommentarartsprDto = assembleArtikelkommentarartsprDto(temp);
			}
			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artikelkommentarartDto
				.setArtikelkommentartartsprDto(artikelkommentarartsprDto);
		return artikelkommentarartDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtikelkommentarartFromArtikelkommentarartDto(
			Artikelkommentarart artikelkommentarart,
			ArtikelkommentarartDto artikelkommentarartDto) {
		artikelkommentarart.setCNr(artikelkommentarartDto.getCNr());
		artikelkommentarart.setBrancheId(artikelkommentarartDto.getBrancheId());
		artikelkommentarart.setBWebshop(artikelkommentarartDto.getBWebshop());
		artikelkommentarart.setBTooltip(artikelkommentarartDto.getBTooltip());
		em.merge(artikelkommentarart);
		em.flush();
	}

	private ArtikelkommentarartDto assembleArtikelkommentarartDto(
			Artikelkommentarart artikelkommentarart) {
		return ArtikelkommentarartDtoAssembler.createDto(artikelkommentarart);
	}

	private ArtikelkommentarartDto[] assembleArtikelkommentarartDtos(
			Collection<?> artikelkommentararts) {
		List<ArtikelkommentarartDto> list = new ArrayList<ArtikelkommentarartDto>();
		if (artikelkommentararts != null) {
			Iterator<?> iterator = artikelkommentararts.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarart artikelkommentarart = (Artikelkommentarart) iterator
						.next();
				list.add(assembleArtikelkommentarartDto(artikelkommentarart));
			}
		}
		ArtikelkommentarartDto[] returnArray = new ArtikelkommentarartDto[list
				.size()];
		return (ArtikelkommentarartDto[]) list.toArray(returnArray);
	}

	private void setArtikelkommentarartsprFromArtikelkommentarartsprDto(
			Artikelkommentarartspr artikelkommentarartspr,
			ArtikelkommentarartsprDto artikelkommentarartsprDto) {
		artikelkommentarartspr.setCBez(artikelkommentarartsprDto.getCBez());
		em.merge(artikelkommentarartspr);
		em.flush();
	}

	private ArtikelkommentarartsprDto assembleArtikelkommentarartsprDto(
			Artikelkommentarartspr artikelkommentarartspr) {
		return ArtikelkommentarartsprDtoAssembler
				.createDto(artikelkommentarartspr);
	}

	private ArtikelkommentarartsprDto[] assembleArtikelkommentarartsprDtos(
			Collection<?> artikelkommentarartsprs) {
		List<ArtikelkommentarartsprDto> list = new ArrayList<ArtikelkommentarartsprDto>();
		if (artikelkommentarartsprs != null) {
			Iterator<?> iterator = artikelkommentarartsprs.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarartspr artikelkommentarartspr = (Artikelkommentarartspr) iterator
						.next();
				list.add(assembleArtikelkommentarartsprDto(artikelkommentarartspr));
			}
		}
		ArtikelkommentarartsprDto[] returnArray = new ArtikelkommentarartsprDto[list
				.size()];
		return (ArtikelkommentarartsprDto[]) list.toArray(returnArray);
	}

	public Integer createArtikelkommentar(
			ArtikelkommentarDto artikelkommentarDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getArtikelIId() == null
				|| artikelkommentarDto.getArtikelkommentarartIId() == null
				|| artikelkommentarDto.getBDefaultbild() == null
				|| artikelkommentarDto.getDatenformatCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelkommentarDto.getArtikelIId() == null || artikelkommentarDto.getArtikelkommentarartIId() == null || artikelkommentarDto.getBDefaultbild() == null || artikelkommentarDto.getDatenformatCNr() == null"));
		}

		if (artikelkommentarDto
				.getDatenformatCNr()
				.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			artikelkommentarDto.setBDefaultbild(Helper.boolean2Short(false));
		}

		if (Helper.short2boolean(artikelkommentarDto.getBDefaultbild()) == true) {
			// try {
			Query query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			Collection<?> cl = query.getResultList();

			ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);
			if (dtos != null && dtos.length > 0) {
				for (int i = 0; i < dtos.length; i++) {
					Artikelkommentar artikelkommentar = em.find(
							Artikelkommentar.class, dtos[0].getIId());
					if (artikelkommentar != null) {
						artikelkommentar.setBDefaultbild(new Short((short) 0));
					}
				}
			}
			// }
			// catch (FinderException ex1) {
			//
			// }
		}

		try {
			Query query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2,
					artikelkommentarDto.getArtikelkommentarartIId());
			query.setParameter(3, artikelkommentarDto.getDatenformatCNr());
			Artikelkommentar doppelt = (Artikelkommentar) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTAR.UC"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTAR);
			artikelkommentarDto.setIId(pk);

			Query queryNext = em
					.createNamedQuery("ArtikelkommentarejbSelectNextReihung");
			queryNext.setParameter(1, artikelkommentarDto.getArtikelIId());

			Integer iSort = (Integer) queryNext.getSingleResult();
			if (iSort == null) {
				iSort = 0;
			}

			iSort = new Integer(iSort + 1);

			artikelkommentarDto.setISort(iSort);

			Artikelkommentar artikelkommentar = new Artikelkommentar(
					artikelkommentarDto.getIId(),
					artikelkommentarDto.getArtikelIId(),
					artikelkommentarDto.getArtikelkommentarartIId(),
					artikelkommentarDto.getDatenformatCNr(),
					artikelkommentarDto.getBDefaultbild(),
					artikelkommentarDto.getIArt(),
					artikelkommentarDto.getISort());
			em.persist(artikelkommentar);
			em.flush();
			setArtikelkommentarFromArtikelkommentarDto(artikelkommentar,
					artikelkommentarDto);
			if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {

				artikelkommentarDto.getArtikelkommentarsprDto()
						.setPersonalIIdAendern(theClientDto.getIDPersonal());
				artikelkommentarDto.getArtikelkommentarsprDto().setTAendern(
						new Timestamp(System.currentTimeMillis()));

				Artikelkommentarspr artikelkommentarspr = new Artikelkommentarspr(
						artikelkommentarDto.getIId(),
						theClientDto.getLocUiAsString(), artikelkommentarDto
								.getArtikelkommentarsprDto()
								.getPersonalIIdAendern(), artikelkommentarDto
								.getArtikelkommentarsprDto().getTAendern());
				em.persist(artikelkommentarspr);
				em.flush();
				setArtikelkommentarsprFromArtikelkommentarsprDto(
						artikelkommentarspr,
						artikelkommentarDto.getArtikelkommentarsprDto());
			}

			if (artikelkommentarDto.getArtikelkommentardruckDto() != null) {
				for (int i = 0; i < artikelkommentarDto
						.getArtikelkommentardruckDto().length; i++) {
					ArtikelkommentardruckDto dto = artikelkommentarDto
							.getArtikelkommentardruckDto()[i];
					dto.setArtikelkommentarIId(artikelkommentarDto.getIId());
					dto.setArtikelIId(artikelkommentarDto.getArtikelIId());
					createArtikelkommentardruck(dto);
				}
			}

			return artikelkommentarDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void vertauscheArtikelkommentar(Integer iiD1, Integer iId2) {
		Artikelkommentar iArtikelkommentar1 = em.find(Artikelkommentar.class,
				iiD1);

		Artikelkommentar iArtikelkommentar2 = em.find(Artikelkommentar.class,
				iId2);

		Integer iSort1 = iArtikelkommentar1.getISort();
		Integer iSort2 = iArtikelkommentar2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		iArtikelkommentar2.setISort(new Integer(-1));
		iArtikelkommentar1.setISort(iSort2);
		iArtikelkommentar2.setISort(iSort1);

	}

	public void removeArtikelkommentar(ArtikelkommentarDto artikelkommentarDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarDto.getIId() == null"));
		}
		// try {
		try {
			Query query = em
					.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
			query.setParameter(1, artikelkommentarDto.getIId());
			Collection<?> cl = query.getResultList();
			Iterator<?> iter = cl.iterator();
			while (iter.hasNext()) {
				Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) iter
						.next();
				em.remove(artikelkommentarspr);
			}

			query = em
					.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2, artikelkommentarDto.getIId());
			Collection<?> allDruck = query.getResultList();
			Iterator<?> iterAllDruck = allDruck.iterator();
			while (iterAllDruck.hasNext()) {
				Artikelkommentardruck artklasprTemp = (Artikelkommentardruck) iterAllDruck
						.next();
				em.remove(artklasprTemp);
			}

			Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class,
					artikelkommentarDto.getIId());
			if (artikelkommentar == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtikelkommentar. Es gibt keine iid "
								+ artikelkommentarDto.getIId()
								+ "\ndto.toString(): "
								+ artikelkommentarDto.toString());
			}
			em.remove(artikelkommentar);
			em.flush();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }

	}

	public void updateArtikelkommentar(ArtikelkommentarDto artikelkommentarDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getIId() == null
				|| artikelkommentarDto.getArtikelIId() == null
				|| artikelkommentarDto.getArtikelkommentarartIId() == null
				|| artikelkommentarDto.getBDefaultbild() == null
				|| artikelkommentarDto.getDatenformatCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelkommentarDto.getIId() == null || artikelkommentarDto.getArtikelIId() == null || artikelkommentarDto.getArtikelkommentarartIId() == null || artikelkommentarDto.getBDefaultbild() == null"));
		}
		if (artikelkommentarDto
				.getDatenformatCNr()
				.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			artikelkommentarDto.setBDefaultbild(Helper.boolean2Short(false));
		}

		if (Helper.short2boolean(artikelkommentarDto.getBDefaultbild()) == true) {
			// try {
			Query query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(query
					.getResultList());
			if (dtos != null && dtos.length > 0) {
				for (int i = 0; i < dtos.length; i++) {
					Artikelkommentar artikelkommentar = em.find(
							Artikelkommentar.class, dtos[i].getIId());
					if (artikelkommentar != null) {
						artikelkommentar.setBDefaultbild(new Short((short) 0));
					}
				}
			}
			// }
			// catch (FinderException ex1) {
			//
			// }
		}

		Integer iId = artikelkommentarDto.getIId();

		Artikelkommentar artikelkommentar = null;
		// try {
		artikelkommentar = em.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentar. Es gibt keine iid + "
							+ iId + "\ndto.toString(): "
							+ artikelkommentarDto.toString());

		}

		Query query = em
				.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
		query.setParameter(1, artikelkommentarDto.getArtikelIId());
		query.setParameter(2, artikelkommentarDto.getIId());
		Collection<?> allDruck = query.getResultList();
		Iterator<?> iterAllDruck = allDruck.iterator();
		try {
			while (iterAllDruck.hasNext()) {
				Artikelkommentardruck artklasprTemp = (Artikelkommentardruck) iterAllDruck
						.next();
				em.remove(artklasprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex2);
		}
		if (artikelkommentarDto.getArtikelkommentardruckDto() != null) {
			for (int i = 0; i < artikelkommentarDto
					.getArtikelkommentardruckDto().length; i++) {
				ArtikelkommentardruckDto dto = artikelkommentarDto
						.getArtikelkommentardruckDto()[i];
				dto.setArtikelkommentarIId(artikelkommentarDto.getIId());
				dto.setArtikelIId(artikelkommentarDto.getArtikelIId());
				createArtikelkommentardruck(dto);
			}
		}

		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2,
					artikelkommentarDto.getArtikelkommentarartIId());
			query.setParameter(3, artikelkommentarDto.getDatenformatCNr());
			Integer iIdVorhanden = ((Artikelkommentar) query.getSingleResult())
					.getIId();

			if (iId.equals(iIdVorhanden) == false) {

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELKOMMENTAR.UC"));
			}
		} catch (NoResultException ex) {
			//
		}
		try {
			setArtikelkommentarFromArtikelkommentarDto(artikelkommentar,
					artikelkommentarDto);

			if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
				artikelkommentarDto.getArtikelkommentarsprDto()
						.setPersonalIIdAendern(theClientDto.getIDPersonal());
				artikelkommentarDto.getArtikelkommentarsprDto().setTAendern(
						new Timestamp(System.currentTimeMillis()));

				Artikelkommentarspr artklaspr = em.find(
						Artikelkommentarspr.class, new ArtikelkommentarsprPK(
								iId, theClientDto.getLocUiAsString()));
				if (artklaspr == null) {
					artklaspr = new Artikelkommentarspr(iId,
							theClientDto.getLocUiAsString(),
							artikelkommentarDto.getArtikelkommentarsprDto()
									.getPersonalIIdAendern(),
							artikelkommentarDto.getArtikelkommentarsprDto()
									.getTAendern());
					em.persist(artklaspr);
					em.flush();
					setArtikelkommentarsprFromArtikelkommentarsprDto(artklaspr,
							artikelkommentarDto.getArtikelkommentarsprDto());
				}
				setArtikelkommentarsprFromArtikelkommentarsprDto(artklaspr,
						artikelkommentarDto.getArtikelkommentarsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public byte[] getArtikeldefaultBild(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 1));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(
					dtos[0].getIId(), theClientDto);

			if (dto.getArtikelkommentarsprDto() != null) {
				return dto.getArtikelkommentarsprDto().getOMedia();
			} else {
				return null;
			}
		} else {
			return null;
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArrayList<byte[]> getArtikelBilder(Integer artikelIId,
			TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}

		ArrayList<byte[]> al = new ArrayList<byte[]>();

		// try {
		Query query = em
				.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 1));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(
						dtos[i].getIId(), theClientDto);

				if (dto.getDatenformatCNr().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
					if (dto.getArtikelkommentarsprDto() != null
							&& dto.getArtikelkommentarsprDto().getOMedia() != null) {
						al.add(dto.getArtikelkommentarsprDto().getOMedia());
					}
				}
			}

		}
		query = em
				.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 0));
		cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(
						dtos[i].getIId(), theClientDto);

				if (dto.getDatenformatCNr().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
						|| dto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
					if (dto.getArtikelkommentarsprDto() != null
							&& dto.getArtikelkommentarsprDto().getOMedia() != null) {
						al.add(dto.getArtikelkommentarsprDto().getOMedia());
					}
				}
			}

		}
		return al;
	}

	public boolean gibtEsKommentareInAnderenSprachen(
			Integer artikelkommentarIId, TheClientDto theClientDto) {

		Query query2 = em
				.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
		query2.setParameter(1, artikelkommentarIId);
		Collection<?> cl2 = query2.getResultList();
		ArtikelkommentarsprDto[] artikelkommentarsprDtos = assembleArtikelkommentarsprDtos(cl2);

		for (int i = 0; i < artikelkommentarsprDtos.length; i++) {
			ArtikelkommentarsprDto artikelkommentarsprDto = artikelkommentarsprDtos[i];
			if (!artikelkommentarsprDto.getLocaleCNr().equals(
					theClientDto.getLocUiAsString())) {
				return true;
			}
		}
		return false;
	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikelkommentar artikelkommentar = em
				.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		ArtikelkommentarDto artikelkommentarDto = assembleArtikelkommentarDto(artikelkommentar);

		Query query = em
				.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
		query.setParameter(1, artikelkommentar.getArtikelIId());
		query.setParameter(2, artikelkommentar.getIId());
		Collection<?> cl = query.getResultList();
		artikelkommentarDto
				.setArtikelkommentardruckDto(assembleArtikelkommentardruckDtos(cl));

		ArtikelkommentarsprDto artikelkommentarsprDto = null;

		// try {
		Artikelkommentarspr artikelkommentarspr = em.find(
				Artikelkommentarspr.class, new ArtikelkommentarsprPK(iId,
						theClientDto.getLocUiAsString()));
		if (artikelkommentarspr == null) {
			// nothing here
		} else {
			artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artikelkommentarsprDto == null) {
			// try {
			Artikelkommentarspr temp = em.find(
					Artikelkommentarspr.class,
					new ArtikelkommentarsprPK(iId, theClientDto
							.getLocKonzernAsString()));
			if (temp == null) {
				// nothing here
			} else {
				artikelkommentarsprDto = assembleArtikelkommentarsprDto(temp);
			}

			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artikelkommentarDto.setArtikelkommentarsprDto(artikelkommentarsprDto);
		return artikelkommentarDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public boolean kopiereArtikelkommentar(Integer artikelIId_alt,
			Integer artikelIId_neu, TheClientDto theClientDto) {

		boolean bEsWurdeMehrereSprachenKopiert = false;

		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
		query.setParameter(1, artikelIId_alt);
		Collection<?> cl = query.getResultList();

		ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(cl);

		for (int i = 0; i < artikelkommentarDtos.length; i++) {
			Integer artikelkommentarIId_Alt = artikelkommentarDtos[i].getIId();

			ArtikelkommentarDto dto = artikelkommentarDtos[i];
			dto.setIId(null);
			dto.setArtikelIId(artikelIId_neu);

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTAR);

			Artikelkommentar artikelkommentar = new Artikelkommentar(pk,
					artikelIId_neu, dto.getArtikelkommentarartIId(),
					dto.getDatenformatCNr(), dto.getBDefaultbild(),
					dto.getIArt(), dto.getISort());
			em.persist(artikelkommentar);
			em.flush();

			Query query2 = em
					.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
			query2.setParameter(1, artikelkommentarIId_Alt);
			Collection<?> cl2 = query2.getResultList();
			ArtikelkommentarsprDto[] artikelkommentarsprDtos = assembleArtikelkommentarsprDtos(cl2);

			for (int z = 0; z < artikelkommentarsprDtos.length; z++) {
				Artikelkommentarspr artikelkommentarspr = new Artikelkommentarspr(
						pk, artikelkommentarsprDtos[z].getLocaleCNr(),
						artikelkommentarsprDtos[z].getPersonalIIdAendern(),
						artikelkommentarsprDtos[z].getTAendern());

				em.persist(artikelkommentarspr);
				em.flush();
				setArtikelkommentarsprFromArtikelkommentarsprDto(
						artikelkommentarspr, artikelkommentarsprDtos[z]);

				if (!artikelkommentarsprDtos[z].getLocaleCNr().equals(
						theClientDto.getLocUiAsString())) {
					bEsWurdeMehrereSprachenKopiert = true;
				}

			}

			// druck

			Query querydruck = em
					.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
			querydruck.setParameter(1, artikelIId_alt);
			querydruck.setParameter(2, artikelkommentarIId_Alt);
			Collection<?> cldruck = querydruck.getResultList();
			ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cldruck);

			for (int z = 0; z < artikelkommentardruckDtos.length; z++) {
				ArtikelkommentardruckDto artikelkommentardruckDto = artikelkommentardruckDtos[z];
				artikelkommentardruckDto.setIId(null);
				artikelkommentardruckDto.setArtikelIId(artikelIId_neu);
				artikelkommentardruckDto.setArtikelkommentarIId(pk);
				createArtikelkommentardruck(artikelkommentardruckDto);
			}

		}

		return bEsWurdeMehrereSprachenKopiert;

	}

	public ArtikelkommentarDto[] artikelkommentarFindByArtikelIId(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }

		ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(cl);

		for (int i = 0; i < artikelkommentarDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarDtos[i];
			// @ToDo Ungueltiger Prozeduraufruf oder ungueltiges Argument
			// artikelkommentarDto
			// .setArtikelkommentardruckDto(assembleArtikelkommentardruckDtos
			// (artikelkommentardruckHome
			// .findByArtikelIIdArtikelkommentarIId(artikelkommentarDto
			// .getArtikelIId(), artikelkommentarDto.getIId())));
			artikelkommentarDto
					.setArtikelkommentarsprDto(artikelkommentarsprFindByPrimaryKey(
							artikelkommentarDto.getIId(),
							theClientDto.getLocUiAsString()));

		}
		return artikelkommentarDtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKey(
			Integer artikelkommentarIId, String localeCNr)
			throws EJBExceptionLP {
		if (artikelkommentarIId == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelkommentarIId == null || localeCNr == null"));
		}

		// try {
		Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) em
				.find(Artikelkommentarspr.class, new ArtikelkommentarsprPK(
						artikelkommentarIId, localeCNr));
		if (artikelkommentarspr == null) {
			return null;
		}
		return assembleArtikelkommentarsprDto(artikelkommentarspr);
		// }
		// catch (FinderException ex) {
		// return null;
		// }

	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKeyUndLocale(
			Integer iId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Artikelkommentar artikelkommentar = em
				.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarFindByPrimaryKeyUndLocale. Es gibt keine iid "
							+ iId);
		}
		ArtikelkommentarDto artikelkommentarDto = assembleArtikelkommentarDto(artikelkommentar);

		ArtikelkommentarsprDto artikelkommentarsprDto = null;
		Artikelkommentarspr artikelkommentarspr = em.find(
				Artikelkommentarspr.class, new ArtikelkommentarsprPK(iId,
						localeCNr));
		if (artikelkommentarspr != null) {
			artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
		}

		if (artikelkommentarsprDto == null) {
			Artikelkommentarspr temp = em.find(
					Artikelkommentarspr.class,
					new ArtikelkommentarsprPK(iId, theClientDto
							.getLocKonzernAsString()));
			if (temp != null) {
				artikelkommentarsprDto = assembleArtikelkommentarsprDto(temp);
			} else {
				artikelkommentarsprDto = new ArtikelkommentarsprDto();
				artikelkommentarsprDto.setArtikelkommentarIId(iId);
				artikelkommentarsprDto.setXKommentar(
						getTextRespectUISpr("artikel.keinkommentarinsprvorhanden",
								theClientDto.getMandant(), theClientDto.getLocUi(), localeCNr.trim()));
			}
		}
		artikelkommentarDto.setArtikelkommentarsprDto(artikelkommentarsprDto);
		return artikelkommentarDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtikelkommentarFromArtikelkommentarDto(
			Artikelkommentar artikelkommentar,
			ArtikelkommentarDto artikelkommentarDto) {
		artikelkommentar.setArtikelIId(artikelkommentarDto.getArtikelIId());
		artikelkommentar.setArtikelkommentarartIId(artikelkommentarDto
				.getArtikelkommentarartIId());
		artikelkommentar.setBDefaultbild(artikelkommentarDto.getBDefaultbild());
		artikelkommentar.setIArt(artikelkommentarDto.getIArt());
		artikelkommentar.setDatenformatCNr(artikelkommentarDto
				.getDatenformatCNr());
		artikelkommentar.setISort(artikelkommentarDto.getISort());
		em.merge(artikelkommentar);
		em.flush();
	}

	private ArtikelkommentarDto assembleArtikelkommentarDto(
			Artikelkommentar artikelkommentar) {
		return ArtikelkommentarDtoAssembler.createDto(artikelkommentar);
	}

	private ArtikelkommentarDto[] assembleArtikelkommentarDtos(
			Collection<?> artikelkommentars) {
		List<ArtikelkommentarDto> list = new ArrayList<ArtikelkommentarDto>();
		if (artikelkommentars != null) {
			Iterator<?> iterator = artikelkommentars.iterator();
			while (iterator.hasNext()) {
				Artikelkommentar artikelkommentar = (Artikelkommentar) iterator
						.next();
				list.add(assembleArtikelkommentarDto(artikelkommentar));
			}
		}
		ArtikelkommentarDto[] returnArray = new ArtikelkommentarDto[list.size()];
		return (ArtikelkommentarDto[]) list.toArray(returnArray);
	}

	private void setArtikelkommentarsprFromArtikelkommentarsprDto(
			Artikelkommentarspr artikelkommentarspr,
			ArtikelkommentarsprDto artikelkommentarsprDto) {
		artikelkommentarspr.setXKommentar(artikelkommentarsprDto
				.getXKommentar());
		artikelkommentarspr.setOMedia(artikelkommentarsprDto.getOMedia());
		artikelkommentarspr.setCDateiname(artikelkommentarsprDto
				.getCDateiname());
		artikelkommentarspr.setTFiledatum(artikelkommentarsprDto
				.getTFiledatum());
		artikelkommentarspr.setTAendern(artikelkommentarsprDto.getTAendern());
		artikelkommentarspr.setPersonalIIdAendern(artikelkommentarsprDto
				.getPersonalIIdAendern());
		em.merge(artikelkommentarspr);
		em.flush();
	}

	private ArtikelkommentarsprDto assembleArtikelkommentarsprDto(
			Artikelkommentarspr artikelkommentarspr) {
		return ArtikelkommentarsprDtoAssembler.createDto(artikelkommentarspr);
	}

	private ArtikelkommentarsprDto[] assembleArtikelkommentarsprDtos(
			Collection<?> artikelkommentarsprs) {
		List<ArtikelkommentarsprDto> list = new ArrayList<ArtikelkommentarsprDto>();
		if (artikelkommentarsprs != null) {
			Iterator<?> iterator = artikelkommentarsprs.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) iterator
						.next();
				list.add(assembleArtikelkommentarsprDto(artikelkommentarspr));
			}
		}
		ArtikelkommentarsprDto[] returnArray = new ArtikelkommentarsprDto[list
				.size()];
		return (ArtikelkommentarsprDto[]) list.toArray(returnArray);
	}

	public Integer createArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP {

		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		if (artikelkommentardruckDto.getArtikelIId() == null
				|| artikelkommentardruckDto.getArtikelkommentarIId() == null
				|| artikelkommentardruckDto.getBelegartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelkommentardruckDto.getArtikelIId() == null || artikelkommentardruckDto.getArtikelkommentarIId() == null || artikelkommentardruckDto.getBelegartCNr() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
			query.setParameter(1, artikelkommentardruckDto.getArtikelIId());
			query.setParameter(2,
					artikelkommentardruckDto.getArtikelkommentarIId());
			query.setParameter(3, artikelkommentardruckDto.getBelegartCNr());
			// @todo getSingleResult oder getResultList ?
			Artikelkommentardruck doppelt = (Artikelkommentardruck) query
					.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTARDRUCK.UC"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTARDRUCK);
			artikelkommentardruckDto.setIId(pk);
			Artikelkommentardruck artikelkommentardruck = new Artikelkommentardruck(
					artikelkommentardruckDto.getIId(),
					artikelkommentardruckDto.getArtikelIId(),
					artikelkommentardruckDto.getArtikelkommentarIId(),
					artikelkommentardruckDto.getBelegartCNr());

			em.persist(artikelkommentardruck);
			em.flush();
			setArtikelkommentardruckFromArtikelkommentardruckDto(
					artikelkommentardruck, artikelkommentardruckDto);

			return artikelkommentardruckDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		if (artikelkommentardruckDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentardruckDto.getIId() == null"));
		}
		// try {
		Artikelkommentardruck artikelkommentardruck = em.find(
				Artikelkommentardruck.class, artikelkommentardruckDto.getIId());
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelkommentardruck. Es gibt keine iid "
							+ artikelkommentardruckDto.getIId()
							+ "\ndto.toString: "
							+ artikelkommentardruckDto.toString());
		}
		try {
			em.remove(artikelkommentardruck);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }

	}

	public void updateArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		Integer iId = artikelkommentardruckDto.getIId();
		Artikelkommentardruck artikelkommentardruck = null;
		// try {
		artikelkommentardruck = em.find(Artikelkommentardruck.class, iId);
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentardruck. Es gibt keine iid "
							+ iId + "\ndto.toString(): "
							+ artikelkommentardruckDto.toString());
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// try {
		Query query = em
				.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
		query.setParameter(1, artikelkommentardruckDto.getArtikelIId());
		query.setParameter(2, artikelkommentardruckDto.getArtikelkommentarIId());
		query.setParameter(3, artikelkommentardruckDto.getBelegartCNr());
		Integer iIdVorhanden = ((Artikelkommentardruck) query.getSingleResult())
				.getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTARDRUCK.UC"));
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		setArtikelkommentardruckFromArtikelkommentardruckDto(
				artikelkommentardruck, artikelkommentardruckDto);

	}

	public void updateArtikelkommentardrucks(
			ArtikelkommentardruckDto[] artikelkommentardruckDtos)
			throws EJBExceptionLP {
		if (artikelkommentardruckDtos != null) {
			for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
				updateArtikelkommentardruck(artikelkommentardruckDtos[i]);
			}
		}
	}

	public ArtikelkommentardruckDto artikelkommentardruckFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		ArtikelkommentardruckDto artikelkommentardruckDto = new ArtikelkommentardruckDto();
		Artikelkommentardruck artikelkommentardruck = em.find(
				Artikelkommentardruck.class, iId);
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentardruckFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		artikelkommentardruckDto = assembleArtikelkommentardruckDto(artikelkommentardruck);
		return artikelkommentardruckDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public String[] getArtikelhinweise(Integer artikelIId, String belegartCNr,
			TheClientDto theClientDto) {
		ArrayList<String> al = new ArrayList<String>();
		try {
			Query query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelIId);
			ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(query
					.getResultList());

			for (int i = 0; i < artikelkommentarDtos.length; i++) {
				if (artikelkommentarDtos[i].getIArt() == ARTIKELKOMMENTARART_HINWEIS) {

					if (artikelkommentarDtos[i].getDatenformatCNr().equals(
							MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {

						Query query1 = em
								.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
						query1.setParameter(1, artikelIId);
						query1.setParameter(2, artikelkommentarDtos[i].getIId());
						query1.setParameter(3, belegartCNr);
						// @todo getSingleResult oder getResultList ?

						try {

							Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) query1
									.getSingleResult();
							if (artikelkommentardruck != null) {
								// try {
								Artikelkommentarspr artikelkommentarspr = em
										.find(Artikelkommentarspr.class,
												new ArtikelkommentarsprPK(
														artikelkommentarDtos[i]
																.getIId(),
														theClientDto
																.getLocUiAsString()));
								if (artikelkommentarspr == null) {
									// nothing here
								} else {
									ArtikelkommentarsprDto artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
									if (artikelkommentarsprDto.getXKommentar() != null) {
										al.add(artikelkommentarsprDto
												.getXKommentar());
									}
								}
								// }
								// catch (FinderException ex) {
								// nothing here
								// }
							}
						} catch (NoResultException e) {
							// nothing here
						}

					}

				}
			}
		} catch (NoResultException e) {
			// nothing here
		}
		String[] s = new String[al.size()];
		return (String[]) al.toArray(s);
	}

	public ArrayList<byte[]> getArtikelhinweiseBild(Integer artikelIId,
			String belegartCNr, TheClientDto theClientDto) {
		ArrayList<byte[]> al = new ArrayList<byte[]>();
		try {
			Query query = em
					.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelIId);
			ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(query
					.getResultList());

			for (int i = 0; i < artikelkommentarDtos.length; i++) {
				if (artikelkommentarDtos[i].getIArt() == ARTIKELKOMMENTARART_HINWEIS) {

					if (artikelkommentarDtos[i].getDatenformatCNr().equals(
							MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
							|| artikelkommentarDtos[i]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| artikelkommentarDtos[i]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| artikelkommentarDtos[i]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

						Query query1 = em
								.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
						query1.setParameter(1, artikelIId);
						query1.setParameter(2, artikelkommentarDtos[i].getIId());
						query1.setParameter(3, belegartCNr);
						// @todo getSingleResult oder getResultList ?

						try {

							Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) query1
									.getSingleResult();
							if (artikelkommentardruck != null) {
								// try {
								Artikelkommentarspr artikelkommentarspr = em
										.find(Artikelkommentarspr.class,
												new ArtikelkommentarsprPK(
														artikelkommentarDtos[i]
																.getIId(),
														theClientDto
																.getLocUiAsString()));
								if (artikelkommentarspr == null) {
									// nothing here
								} else {
									ArtikelkommentarsprDto artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
									if (artikelkommentarsprDto.getOMedia() != null) {
										al.add(artikelkommentarsprDto
												.getOMedia());
									}
								}
								// }
								// catch (FinderException ex) {
								// nothing here
								// }
							}
						} catch (NoResultException e) {
							// nothing here
						}

					}

				}
			}
		} catch (NoResultException e) {
			// nothing here
		}
		return al;
	}

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNr(
			Integer artikelIId, String belegartCNr, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null || belegartCNr == null || localeCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || belegartCNr == null || localeCNr == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdBelegartCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, belegartCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }

		ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cl);
		ArrayList<ArtikelkommentarDto> al = new ArrayList<ArtikelkommentarDto>();

		for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarFindByPrimaryKeyUndLocale(
					artikelkommentardruckDtos[i].getArtikelkommentarIId(),
					localeCNr, theClientDto);
			if (artikelkommentarDto.getIArt() == ARTIKELKOMMENTARART_MITDRUCKEN) {
				al.add(artikelkommentarDto);
			}
		}

		// Nun noch nach I_SORT sortieren
		for (int i = al.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				ArtikelkommentarDto o = al.get(j);
				ArtikelkommentarDto o1 = al.get(j + 1);

				int iSort = o.getISort();
				int iSort1 = o1.getISort();

				if (iSort > iSort1) {
					al.set(j, o1);
					al.set(j + 1, o);
				}
			}
		}

		ArtikelkommentarDto[] dtos = new ArtikelkommentarDto[al.size()];
		return (ArtikelkommentarDto[]) al.toArray(dtos);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
			Integer artikelIId, String belegartCNr, String localeCNr,
			TheClientDto theClientDto) {
		if (artikelIId == null || belegartCNr == null || localeCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || belegartCNr == null || localeCNr == null"));
		}
		Query query = em
				.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdBelegartCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, belegartCNr);
		Collection<?> cl = query.getResultList();

		ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cl);
		ArrayList<ArtikelkommentarDto> al = new ArrayList<ArtikelkommentarDto>();

		for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarFindByPrimaryKeyUndLocale(
					artikelkommentardruckDtos[i].getArtikelkommentarIId(),
					localeCNr, theClientDto);
			if (artikelkommentarDto.getIArt() == ARTIKELKOMMENTARART_ANHANG) {
				al.add(artikelkommentarDto);
			}
		}

		ArtikelkommentarDto[] dtos = new ArtikelkommentarDto[al.size()];
		return (ArtikelkommentarDto[]) al.toArray(dtos);

	}

	private void setArtikelkommentardruckFromArtikelkommentardruckDto(
			Artikelkommentardruck artikelkommentardruck,
			ArtikelkommentardruckDto artikelkommentardruckDto) {
		artikelkommentardruck.setArtikelIId(artikelkommentardruckDto
				.getArtikelIId());
		artikelkommentardruck.setArtikelkommentarIId(artikelkommentardruckDto
				.getArtikelkommentarIId());
		artikelkommentardruck.setBelegartCNr(artikelkommentardruckDto
				.getBelegartCNr());
		em.merge(artikelkommentardruck);
		em.flush();
	}

	private ArtikelkommentardruckDto assembleArtikelkommentardruckDto(
			Artikelkommentardruck artikelkommentardruck) {
		return ArtikelkommentardruckDtoAssembler
				.createDto(artikelkommentardruck);
	}

	private ArtikelkommentardruckDto[] assembleArtikelkommentardruckDtos(
			Collection<?> artikelkommentardrucks) {
		List<ArtikelkommentardruckDto> list = new ArrayList<ArtikelkommentardruckDto>();
		if (artikelkommentardrucks != null) {
			Iterator<?> iterator = artikelkommentardrucks.iterator();
			while (iterator.hasNext()) {
				Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) iterator
						.next();
				list.add(assembleArtikelkommentardruckDto(artikelkommentardruck));
			}
		}
		ArtikelkommentardruckDto[] returnArray = new ArtikelkommentardruckDto[list
				.size()];
		return (ArtikelkommentardruckDto[]) list.toArray(returnArray);
	}
}
