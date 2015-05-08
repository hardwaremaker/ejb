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
package com.lp.server.lieferschein.ejbfac;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.util.isort.ASwapper;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.isort.ISwapValidator;
import com.lp.util.EJBExceptionLP;

public class LieferscheinpositionSwapper extends ASwapper<Lieferscheinposition> {

	protected LieferscheinpositionSwapper(IPrimitiveSwapper swapper,
			EntityManager em) {
		super(swapper, em);
	}

	@Override
	public String getArtikelSetQuery() {
		return "LieferscheinpositionfindByPositionIIdArtikelset";
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return Lieferscheinposition.QueryFindIZwsByVonBisIId;
	}

	@Override
	public boolean sameHeadIId(Lieferscheinposition position1,
			Lieferscheinposition position2) {
		return position1.getLieferscheinIId().equals(
				position2.getLieferscheinIId());
	}

	@Override
	public ISwapValidator<Lieferscheinposition> getConcreteValidator() {
		return new ISwapValidator<Lieferscheinposition>() {
			
			@Override
			public boolean withArtikelsetPositionen() {
				return false;
			}
			
			@Override
			public void isMoveablePlus(Lieferscheinposition basePosition,
					List<Lieferscheinposition> possiblePos) throws EJBExceptionLP {
			}
			
			@Override
			public void isMoveableMinus(Lieferscheinposition basePosition,
					List<Lieferscheinposition> possiblePos) throws EJBExceptionLP {
			}
			
			@Override
			public void isMoveable(Lieferscheinposition basePosition,
					List<Lieferscheinposition> possiblePos) throws EJBExceptionLP {
				Lieferschein lieferschein = getEntityManager().find(Lieferschein.class, basePosition.getLieferscheinIId());
				if (!lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
							new Exception(
									"Positionen k\u00F6nnen nur im Status 'Angelegt' verschoben werden."));
				}
				if (basePosition
						.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)
						&& basePosition.getISort() > possiblePos.get(0).getISort()
						&& possiblePos.get(0).getNNettoeinzelpreis() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION,
							new Exception(
									"Die Endsumme darf nicht vor eine preisbehaftete Position verschoben werden."));
				}
				if (possiblePos.get(0)
						.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)
						&& possiblePos.get(0).getISort() > basePosition.getISort()
						&& basePosition.getNNettoeinzelpreis() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION,
							new Exception(
									"Die Endsumme darf nicht vor eine preisbehaftete Position verschoben werden."));
				}
			}
		};
	}

	@Override
	public Class<Lieferscheinposition> getPositionEntityClass() {
		return Lieferscheinposition.class;
	}

}
