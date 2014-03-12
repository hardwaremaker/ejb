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
package com.lp.server.bestellung.ejbfac;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.util.isort.ASwapper;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.isort.ISwapValidator;
import com.lp.util.EJBExceptionLP;

public class BestellpositionSwapper extends ASwapper<Bestellposition> {

	protected BestellpositionSwapper(IPrimitiveSwapper swapper,
			EntityManager em) {
		super(swapper, em);
	}

	@Override
	public String getArtikelSetQuery() {
		return "BestellpositionfindByPositionIIdArtikelset";
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return null;
	}

	@Override
	public boolean sameHeadIId(Bestellposition position1,
			Bestellposition position2) {
		return position1.getBestellungIId() .equals(position2.getBestellungIId());
	}

	@Override
	public ISwapValidator<Bestellposition> getConcreteValidator() {
		return new ISwapValidator<Bestellposition>() {
			
			@Override
			public boolean withArtikelsetPositionen() {
				return false;
			}
			
			@Override
			public void isMoveablePlus(Bestellposition basePosition,
					List<Bestellposition> possiblePos) throws EJBExceptionLP {
				
			}
			
			@Override
			public void isMoveableMinus(Bestellposition basePosition,
					List<Bestellposition> possiblePos) throws EJBExceptionLP {
			}
			@Override
			public void isMoveable(Bestellposition basePosition,
					List<Bestellposition> possiblePos) throws EJBExceptionLP {
				Bestellung bestellung = getEntityManager().find(Bestellung.class, basePosition.getBestellungIId());
				if(!bestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
							new Exception(
									"Positionen k\u00F6nnen nur im Status 'Angelegt' verschoben werden."));
				}
			}
		};
	}

	@Override
	public Class<Bestellposition> getPositionEntityClass() {
		return Bestellposition.class;
	}

}
