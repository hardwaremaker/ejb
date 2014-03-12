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

import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.util.EJBExceptionLP;

public class CompositeISort<T extends IPositionIIdArtikelset & IISort> {

	private List<ISwapValidator<T>> validators = new ArrayList<ISwapValidator<T>>();
	private ASwapper<T> swapper;

	public CompositeISort(ASwapper<T> swapper) {
		validators.add(new DefaultValidator<T>(swapper));
		validators.add(swapper.getConcreteValidator());
		validators.add(new ZwischensummeValidator<T>(swapper));
		validators.add(new ArtikelsetValidator<T>());
		this.swapper = swapper;
	}

	public void vertauschePositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds) throws EJBExceptionLP {
		List<T> possiblePos = getPositionen(possibleIIds);
		T basePosition = getPosition(iIdBasePosition);
		validateMinus(basePosition, possiblePos);
		swapper = SwapperFactory.createSwapper(basePosition, possiblePos,
				swapper);
		swapper.vertauschePositionenMinus(basePosition, possiblePos);
	}

	public void vertauschePositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds) throws EJBExceptionLP {
		List<T> possiblePos = getPositionen(possibleIIds);
		T basePosition = getPosition(iIdBasePosition);
		validatePlus(basePosition, possiblePos);
		swapper = SwapperFactory.createSwapper(basePosition, possiblePos,
				swapper);
		swapper.vertauschePositionenPlus(basePosition, possiblePos);
	}

	private void validateMinus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		List<T> filtered = filterArtikelsetPos(possiblePos);
		for (ISwapValidator<T> validator : validators) {
			validator.isMoveable(basePosition, validator
					.withArtikelsetPositionen() ? possiblePos : filtered);
			validator.isMoveableMinus(basePosition, validator
					.withArtikelsetPositionen() ? possiblePos : filtered);
		}
	}

	private void validatePlus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		List<T> filtered = filterArtikelsetPos(possiblePos);
		for (ISwapValidator<T> validator : validators) {
			validator.isMoveable(basePosition, validator
					.withArtikelsetPositionen() ? possiblePos : filtered);
			validator.isMoveablePlus(basePosition, validator
					.withArtikelsetPositionen() ? possiblePos : filtered);
		}
	}

	private List<T> getPositionen(List<Integer> possibleIIds) {
		List<T> positionen = new ArrayList<T>();
		for (Integer iid : possibleIIds) {
			positionen.add(getPosition(iid));
		}
		return positionen;
	}

	private T getPosition(Integer iid) {
		T pos = swapper.getEntityManager().find(
				swapper.getPositionEntityClass(), iid);
		if (pos == null)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"Die Position mit id " + iid + " wurde nicht gefunden");
		return pos;
	}

	public static <T extends IPositionIIdArtikelset> List<T> filterArtikelsetPos(List<T> positionen) {
		List<T> list = new ArrayList<T>();
		for (T pos : positionen) {
			if (pos.getPositionIIdArtikelset() == null)
				list.add(pos);
		}
		return list;
	}
	
	public static <T extends IPositionIIdArtikelset> T findFirstNoneArtikelsetPos(List<T> positionen) {
		for (T pos : positionen) {
			if (pos.getPositionIIdArtikelset() == null)
				return pos;
		}
		return null;
	}
}
