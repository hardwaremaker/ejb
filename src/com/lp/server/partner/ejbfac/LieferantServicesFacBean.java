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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.service.ArtikelFilterComboBoxEntry;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.partner.ejb.Lfliefergruppespr;
import com.lp.server.partner.ejb.LfliefergruppesprPK;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LfliefergruppeDtoAssembler;
import com.lp.server.partner.service.LfliefergruppesprDto;
import com.lp.server.partner.service.LfliefergruppesprDtoAssembler;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class LieferantServicesFacBean extends Facade implements LieferantServicesFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createLfliefergruppe(LfliefergruppeDto lfliefergruppeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (lfliefergruppeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lfliefergruppeDtoI == null"));
		}
		if (lfliefergruppeDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lfliefergruppeDtoI.getCNr() == null"));
		}

		Lfliefergruppe lfliefergruppe = null;
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_FUNKTION);
		lfliefergruppeDtoI.setIId(iId);
		lfliefergruppe = new Lfliefergruppe(lfliefergruppeDtoI.getIId(), lfliefergruppeDtoI.getCNr(),
				lfliefergruppeDtoI.getMandantCNr());
		em.persist(lfliefergruppe);
		em.flush();
		setLfliefergruppeFromLfliefergruppeDto(lfliefergruppe, lfliefergruppeDtoI);

		if (lfliefergruppeDtoI.getLfliefergruppesprDto() != null) {
			Lfliefergruppespr lfliefergruppespr = new Lfliefergruppespr(lfliefergruppeDtoI.getIId(),
					theClientDto.getLocUiAsString());
			em.persist(lfliefergruppespr);
			em.flush();
			setLfliefergruppesprFromLfliefergruppesprDto(lfliefergruppespr,
					lfliefergruppeDtoI.getLfliefergruppesprDto());
		}
		return lfliefergruppe.getIId();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Map getAllLiefergruppen(TheClientDto theClientDto) {

		Map m = new LinkedHashMap();
		Query query = em.createNamedQuery("LfliefergruppeFindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> clGruppen = query.getResultList();

		Iterator<?> resultListIterator = clGruppen.iterator();
		while (resultListIterator.hasNext()) {
			Lfliefergruppe lfliefergruppe = (Lfliefergruppe) resultListIterator.next();

			String spr = null;

			LfliefergruppesprPK lfliefergruppesprPK = new LfliefergruppesprPK();
			lfliefergruppesprPK.setLfliefergruppeIId(lfliefergruppe.getIId());
			lfliefergruppesprPK.setLocaleCNr(theClientDto.getLocUiAsString());
			Lfliefergruppespr lfliefergruppespr = em.find(Lfliefergruppespr.class, lfliefergruppesprPK);
			if (lfliefergruppespr != null && lfliefergruppespr.getCBez() != null) {
				spr = lfliefergruppespr.getCBez();
			} else {
				spr = lfliefergruppe.getCNr();
			}

			m.put(lfliefergruppe.getIId(), spr);

		}

		return m;

	}

	public void removeLfliefergruppe(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		Lfliefergruppe toRemove = em.find(Lfliefergruppe.class, iIdI);
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

	public void removeLfliefergruppe(LfliefergruppeDto lfliefergruppeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (lfliefergruppeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lfliefergruppeDtoI == null"));
		}
		Collection<?> c = null;
		Query query = em.createNamedQuery("LfliefergruppesprfindByLiefergruppenIId");
		query.setParameter(1, lfliefergruppeDtoI.getIId());
		c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		// Erst alle SPRs dazu loeschen.
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lfliefergruppespr item = (Lfliefergruppespr) iter.next();
			em.remove(item);
		}
		Integer iId = lfliefergruppeDtoI.getIId();
		removeLfliefergruppe(iId, theClientDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
	}

	public void updateLfliefergruppe(LfliefergruppeDto lfliefergruppeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (lfliefergruppeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("statusDtoI == null"));
		}

		Integer iId = lfliefergruppeDtoI.getIId();
		try {
			Lfliefergruppe lfliefergruppe = em.find(Lfliefergruppe.class, iId);
			if (lfliefergruppe == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setLfliefergruppeFromLfliefergruppeDto(lfliefergruppe, lfliefergruppeDtoI);
			if (lfliefergruppeDtoI.getLfliefergruppesprDto() != null) {
				// -- upd oder create
				if (lfliefergruppeDtoI.getLfliefergruppesprDto().getLfliefergruppeIId() == null) {
					// create
					// Key(teil) setzen.
					lfliefergruppeDtoI.getLfliefergruppesprDto().setLfliefergruppeIId(lfliefergruppeDtoI.getIId());
					lfliefergruppeDtoI.getLfliefergruppesprDto().setLocaleCNr(theClientDto.getLocUiAsString());

					createLfliefergruppespr(lfliefergruppeDtoI.getLfliefergruppesprDto(), theClientDto);
				} else {
					// upd
					updateLfliefergruppespr(lfliefergruppeDtoI.getLfliefergruppesprDto(), theClientDto);
				}
			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public LfliefergruppeDto lfliefergruppeFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iIdI == null"));
		}

		LfliefergruppeDto lfliefergruppeDto = null;
		// try {
		Lfliefergruppe lfliefergruppe = em.find(Lfliefergruppe.class, iIdI);
		if (lfliefergruppe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lfliefergruppeDto = assembleLfliefergruppeDto(lfliefergruppe);

		try {
			Lfliefergruppespr lfliefergruppespr = em.find(Lfliefergruppespr.class,
					new LfliefergruppesprPK(lfliefergruppeDto.getIId(), theClientDto.getLocUiAsString()));
			lfliefergruppeDto.setLfliefergruppesprDto(assembleLfliefergruppesprDto(lfliefergruppespr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return lfliefergruppeDto;
	}

	private void setLfliefergruppeFromLfliefergruppeDto(Lfliefergruppe lfliefergruppe,
			LfliefergruppeDto lfliefergruppeDtoI) {
		lfliefergruppe.setCNr(lfliefergruppeDtoI.getCNr());
		em.merge(lfliefergruppe);
		em.flush();
	}

	private LfliefergruppeDto assembleLfliefergruppeDto(Lfliefergruppe lfliefergruppe) {
		return LfliefergruppeDtoAssembler.createDto(lfliefergruppe);
	}

	private LfliefergruppeDto[] assembleLfliefergruppeDtos(Collection<?> lfliefergruppes) {
		List<LfliefergruppeDto> list = new ArrayList<LfliefergruppeDto>();
		if (lfliefergruppes != null) {
			Iterator<?> iterator = lfliefergruppes.iterator();
			while (iterator.hasNext()) {
				Lfliefergruppe lfliefergruppe = (Lfliefergruppe) iterator.next();
				list.add(assembleLfliefergruppeDto(lfliefergruppe));
			}
		}
		LfliefergruppeDto[] returnArray = new LfliefergruppeDto[list.size()];
		return (LfliefergruppeDto[]) list.toArray(returnArray);
	}

	public Integer createLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (lfliefergruppesprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("statusspr2DtoI == null"));
		}

		Lfliefergruppespr lfliefergruppespr = null;

		lfliefergruppespr = new Lfliefergruppespr(lfliefergruppesprDtoI.getLfliefergruppeIId(),
				lfliefergruppesprDtoI.getLocaleCNr());
		em.persist(lfliefergruppespr);
		em.flush();
		setLfliefergruppesprFromLfliefergruppesprDto(lfliefergruppespr, lfliefergruppesprDtoI);
		return lfliefergruppespr.getPk().getLfliefergruppeIId();
	}

	public void removeLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (lfliefergruppesprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lfliefergruppesprDtoI == null"));
		}

		Lfliefergruppespr toRemove = em.find(Lfliefergruppespr.class, new LfliefergruppesprPK(
				lfliefergruppesprDtoI.getLfliefergruppeIId(), lfliefergruppesprDtoI.getLocaleCNr()));
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

	public void updateLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (lfliefergruppesprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lfliefergruppesprDtoI == null"));
		}

		LfliefergruppesprPK lfliefergruppesprPK = new LfliefergruppesprPK();
		lfliefergruppesprPK.setLfliefergruppeIId(lfliefergruppesprDtoI.getLfliefergruppeIId());
		lfliefergruppesprPK.setLocaleCNr(lfliefergruppesprDtoI.getLocaleCNr());
		// try {
		Lfliefergruppespr lfliefergruppespr = em.find(Lfliefergruppespr.class, lfliefergruppesprPK);
		if (lfliefergruppespr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLfliefergruppesprFromLfliefergruppesprDto(lfliefergruppespr, lfliefergruppesprDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LfliefergruppesprDto lfliefergruppesprFindByPrimaryKey(Integer lfliefergruppeIIdI, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		// precondition
		if (lfliefergruppeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("lfliefergruppeIIdI == null"));
		}
		if (localeCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("localeCNrI == null"));
		}

		LfliefergruppesprPK lfliefergruppesprPK = new LfliefergruppesprPK();
		lfliefergruppesprPK.setLfliefergruppeIId(lfliefergruppeIIdI);
		lfliefergruppesprPK.setLocaleCNr(localeCNrI);
		Lfliefergruppespr lfliefergruppespr = em.find(Lfliefergruppespr.class, lfliefergruppesprPK);
		if (lfliefergruppespr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLfliefergruppesprDto(lfliefergruppespr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setLfliefergruppesprFromLfliefergruppesprDto(Lfliefergruppespr lfliefergruppespr,
			LfliefergruppesprDto lfliefergruppeDtoI) {
		lfliefergruppespr.setCBez(lfliefergruppeDtoI.getCBez());
		lfliefergruppespr.setXText(lfliefergruppeDtoI.getXText());
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
}
