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
package com.lp.server.util.isort;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.server.util.IZwsPosition;
import com.lp.util.EJBExceptionLP;

public abstract class ASwapper<T extends IPositionIIdArtikelset & IISort> {

	private IPrimitiveSwapper swapper;
	private EntityManager em;

	protected ASwapper(IPrimitiveSwapper swapper, EntityManager em) {
		this.swapper = swapper;
		this.em = em;
	}

	/**
	 * Override and return QueryString for Artikelset-Handling
	 */
	public abstract String getArtikelSetQuery();
	
	/**
	 * Override and return QueryString for Zwischensumme Von-Bis validation
	 */
	public abstract String getZwischensummeFindByVonBisQuery();

	public abstract boolean sameHeadIId(T position1, T position2);

	protected IPrimitiveSwapper getPrimitiveSwapper() {
		return swapper;
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	public void vertauschePositionenMinus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		getPrimitiveSwapper().vertauschePositionen(basePosition.getIId(),
				possiblePos.get(0).getIId());
	}

	public void vertauschePositionenPlus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		getPrimitiveSwapper().vertauschePositionen(basePosition.getIId(),
				possiblePos.get(0).getIId());
	}

	public abstract ISwapValidator<T> getConcreteValidator();

	/**
	 * @return the position entity class
	 */
	public abstract Class<T> getPositionEntityClass();

	public List<T> findArtikelsetByKopf(T kopf) {
		if(getArtikelSetQuery() == null)
			return new ArrayList<T>();
		HvTypedQuery<T> q = new HvTypedQuery<T>(getEntityManager()
				.createNamedQuery(getArtikelSetQuery()));
		q.setParameter(1, ((IPositionIIdArtikelset) kopf).getIId());

		return q.getResultList();
	}

	public IZwsPosition findZwsByVonBisIId(T pos) {
		if(getZwischensummeFindByVonBisQuery() == null)
			return null;
		HvTypedQuery<IZwsPosition> query = new HvTypedQuery<IZwsPosition>(
				getEntityManager().createNamedQuery(getZwischensummeFindByVonBisQuery()));
		query.setParameter("iid", pos.getIId());
		List<IZwsPosition> list = query.getResultList();
		return list.size() == 0 ? null : list.get(0);
	}
}
