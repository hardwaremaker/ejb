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

public class DefaultValidator<T extends IPositionIIdArtikelset & IISort> implements
		ISwapValidator<T> {

	private ASwapper<T> swapper;

	public DefaultValidator(ASwapper<T> swapper) {
		this.swapper = swapper;
	}

	@Override
	public boolean withArtikelsetPositionen() {
		return true;
	}

	@Override
	public void isMoveableMinus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
	}

	@Override
	public void isMoveablePlus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
	}

	@Override
	public void isMoveable(T basePosition, List<T> possiblePos) {
		if (possiblePos.size() == 0)
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_KEINE_POS_VORHANDEN,
					new Exception("Keine Positionen zum Tauschen vorhanden!"));

		if (!swapper.sameHeadIId(basePosition, possiblePos.get(0)))
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER,
					new Exception(
							"Es wurde versucht, Positionen aus verschiedenen Rechnungen zu vertauschen: positionen i_id="
									+ basePosition.getIId()
									+ " bzw. "
									+ possiblePos.get(0).getIId()));

	}

}
