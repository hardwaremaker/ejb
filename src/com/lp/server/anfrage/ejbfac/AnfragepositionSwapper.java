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
package com.lp.server.anfrage.ejbfac;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.isort.ASwapper;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.isort.ISwapValidator;
import com.lp.util.EJBExceptionLP;

public class AnfragepositionSwapper extends ASwapper<Anfrageposition> {

	protected AnfragepositionSwapper(IPrimitiveSwapper swapper, EntityManager em) {
		super(swapper, em);
	}

	@Override
	public boolean sameHeadIId(Anfrageposition position1,
			Anfrageposition position2) {
		return position1.getAnfrageIId().equals(position2.getAnfrageIId());
	}

	@Override
	public Class<Anfrageposition> getPositionEntityClass() {
		return Anfrageposition.class;
	}

	@Override
	public String getArtikelSetQuery() {
		return "AnfragepositionFindByAnfragepositionIdZugehoerig";
	}

	@Override
	public ISwapValidator<Anfrageposition> getConcreteValidator() {
		return new ISwapValidator<Anfrageposition>() {
			
			@Override
			public boolean withArtikelsetPositionen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void isMoveablePlus(Anfrageposition basePosition,
					List<Anfrageposition> possiblePos) throws EJBExceptionLP {
			}

			@Override
			public void isMoveable(Anfrageposition pos1, List<Anfrageposition> possiblePos) {
				Anfrage dto = getEntityManager().find(Anfrage.class, pos1.getAnfrageIId());
				if(!dto.getAnfragestatusCNr().equals(
						LocaleFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
							new Exception(
									"Positionen k\u00F6nnen nur im Status 'Angelegt' verschoben werden."));
				}
				Anfrageposition pos2 = possiblePos.get(0);
				Integer iSort1 = pos1.getISort();
				Integer iSort2 = possiblePos.get(0).getISort();
				
			}
				
			@Override
			public void isMoveableMinus(Anfrageposition basePosition,
					List<Anfrageposition> possiblePos) throws EJBExceptionLP {
			}
		};
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return null;
	}

}
