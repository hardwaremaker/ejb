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
package com.lp.server.util.isort;

import java.util.List;

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.util.EJBExceptionLP;

public class ArtikelsetSwapper<T extends IPositionIIdArtikelset & IISort> extends
		ASwapper<T> {

	private ASwapper<T> swapper;

	protected ArtikelsetSwapper(ASwapper<T> swapper) {
		super(swapper.getPrimitiveSwapper(), swapper.getEntityManager());
		this.swapper = swapper;
	}

	@Override
	public Class<T> getPositionEntityClass() {
		return swapper.getPositionEntityClass();
	}

	@Override
	public boolean sameHeadIId(T position1, T position2) {
		return swapper.sameHeadIId(position1, position2);
	}

	@Override
	public ISwapValidator<T> getConcreteValidator() {
		return swapper.getConcreteValidator();
	}

	@Override
	public String getArtikelSetQuery() {
		return swapper.getArtikelSetQuery();
	}

	private void vertauschePositionen(T pos1, T pos2) throws EJBExceptionLP {
		List<T> artikelset1 = findArtikelsetByKopf(pos1);
		List<T> artikelset2 = findArtikelsetByKopf(pos2);

		Integer startISort = pos2.getISort();

		pos2.setISort(-2);
		pos1.setISort(startISort);

		startISort++;
		if (artikelset1.size() > 0) {
			startISort = tauschePositionen(startISort, artikelset1);
		}

		pos2.setISort(startISort);
		startISort++;
		if (artikelset2.size() > 0) {
			startISort = tauschePositionen(startISort, artikelset2);
		}
		for(T pos : artikelset1)
			getEntityManager().merge(pos);
		for(T pos : artikelset2)
			getEntityManager().merge(pos);
		getEntityManager().flush();
	}

	@Override
	public void vertauschePositionenMinus(T pos1, List<T> possiblePos)
			throws EJBExceptionLP {
		T pos2 = CompositeISort.findFirstNoneArtikelsetPos(possiblePos);
		vertauschePositionen(pos1, pos2);
	}

	@Override
	public void vertauschePositionenPlus(T pos1, List<T> possiblePos)
			throws EJBExceptionLP {
		T pos2 = CompositeISort.findFirstNoneArtikelsetPos(possiblePos);
		vertauschePositionen(pos2, pos1);
	}

	@Override
	public List<T> findArtikelsetByKopf(T kopf) {
		return swapper.findArtikelsetByKopf(kopf);
	}

	private Integer tauschePositionen(Integer startISort, List<T> artikelset) {
		for (T pos : artikelset) {
			pos.setISort(startISort);
			++startISort;
		}
		return startISort;
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return swapper.getZwischensummeFindByVonBisQuery();
	}
}
