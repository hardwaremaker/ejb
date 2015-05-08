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

import java.util.List;

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.server.util.IZwsPosition;
import com.lp.util.EJBExceptionLP;

public class ZwischensummeValidator<T extends IPositionIIdArtikelset & IISort>
		implements ISwapValidator<T> {

	private ASwapper<T> swapper;

	public ZwischensummeValidator(ASwapper<T> swapper) {
		this.swapper = swapper;
	}

	@Override
	public void isMoveableMinus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		if (possiblePos.size() == 0)
			return;
		IZwsPosition zws = swapper.findZwsByVonBisIId(basePosition);
		if (zws != null) {
			if (possiblePos.get(0).getIId().equals(zws.getZwsVonPosition()))
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS,
						"Von-Position der Zwischensumme kann nicht hinter Bis-Position sein.");
		}

		if (!(basePosition instanceof IZwsPosition))
			return;
		if (basePosition.getIId() == null)
			return;
		if (possiblePos.get(0).getIId() == null)
			return;
		IZwsPosition zwsPos = (IZwsPosition) basePosition;
		if (zwsPos.getZwsBisPosition() == null) {
			return;
		}
		if (zwsPos.getZwsBisPosition().equals(possiblePos.get(0).getIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST,
					"Die Zwischensumme kann sich nicht selbst beinhalten.");
		}
	}

	@Override
	public void isMoveablePlus(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
		if (possiblePos.size() == 0)
			return;
		IZwsPosition zws = swapper.findZwsByVonBisIId(basePosition);
		if (zws != null) {
			if (possiblePos.get(0).getIId().equals(zws.getZwsBisPosition()))
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS,
						"Von-Position der Zwischensumme kann nicht hinter Bis-Position sein.");
		}
		if (!(possiblePos.get(0) instanceof IZwsPosition))
			return;
		if (basePosition.getIId() == null)
			return;
		if (possiblePos.get(0).getIId() == null)
			return;
		IZwsPosition zwsPos = (IZwsPosition) possiblePos.get(0);
		if (zwsPos.getZwsBisPosition() == null) {
			return;
		}
		if (zwsPos.getZwsBisPosition().equals(basePosition.getIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST,
					"Die Zwischensumme kann sich nicht selbst beinhalten.");
		}
	}

	@Override
	public void isMoveable(T basePosition, List<T> possiblePos)
			throws EJBExceptionLP {
	}

	@Override
	public boolean withArtikelsetPositionen() {
		return false;
	}

}
