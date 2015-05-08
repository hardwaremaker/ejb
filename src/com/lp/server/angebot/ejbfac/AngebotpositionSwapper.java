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
package com.lp.server.angebot.ejbfac;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.isort.ASwapper;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.isort.ISwapValidator;
import com.lp.util.EJBExceptionLP;

public class AngebotpositionSwapper extends ASwapper<Angebotposition> {

	protected AngebotpositionSwapper(IPrimitiveSwapper swapper, EntityManager em) {
		super(swapper, em);
	}

	@Override
	public boolean sameHeadIId(Angebotposition position1,
			Angebotposition position2) {
		return position1.getAngebotIId().equals(position2.getAngebotIId());
	}

	@Override
	public Class<Angebotposition> getPositionEntityClass() {
		return Angebotposition.class;
	}

	@Override
	public String getArtikelSetQuery() {
		return "AngebotpositionpositionfindByPositionIIdArtikelset";
	}

	@Override
	public ISwapValidator<Angebotposition> getConcreteValidator() {
		return new ISwapValidator<Angebotposition>() {
			
			@Override
			public boolean withArtikelsetPositionen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void isMoveablePlus(Angebotposition basePosition,
					List<Angebotposition> possiblePos) throws EJBExceptionLP {
			}

			@Override
			public void isMoveable(Angebotposition pos1, List<Angebotposition> possiblePos) {
				Angebot dto = getEntityManager().find(Angebot.class, pos1.getAngebotIId());
				if(!dto.getAngebotstatusCNr().equals(
						LocaleFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
							new Exception(
									"Positionen k\u00F6nnen nur im Status 'Angelegt' verschoben werden."));
				}
				Angebotposition pos2 = possiblePos.get(0);
				Integer iSort1 = pos1.getISort();
				Integer iSort2 = possiblePos.get(0).getISort();
				if (pos1.getAngebotpositionartCNr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)
						&& iSort1 > iSort2
						&& pos2.getNNettoeinzelpreis() != null
						||
					pos2.getAngebotpositionartCNr().equals(
								AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)
								&& iSort1 < iSort2
								&& pos1.getNNettoeinzelpreis() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION,
							new Exception(
									"Die Endsumme darf nicht vor eine preisbehaftete Position verschoben werden."));
				}
			}
				
			@Override
			public void isMoveableMinus(Angebotposition basePosition,
					List<Angebotposition> possiblePos) throws EJBExceptionLP {
			}
		};
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return Angebotposition.QueryFindIZwsByVonBisIId;
	}

}
