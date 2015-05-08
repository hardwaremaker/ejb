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
package com.lp.server.auftrag.ejbfac;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.isort.ASwapper;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.isort.ISwapValidator;
import com.lp.util.EJBExceptionLP;

public class AuftragpositionSwapper extends ASwapper<Auftragposition> {

	protected AuftragpositionSwapper(IPrimitiveSwapper swapper, EntityManager em) {
		super(swapper, em);
	}

	@Override
	public boolean sameHeadIId(Auftragposition position1,
			Auftragposition position2) {
		return position1.getAuftragIId().equals(position2.getAuftragIId());
	}

	@Override
	public Class<Auftragposition> getPositionEntityClass() {
		return Auftragposition.class;
	}

	@Override
	public String getArtikelSetQuery() {
		return "AuftragpositionfindByPositionIIdArtikelset";
	}

	@Override
	public ISwapValidator<Auftragposition> getConcreteValidator() {
		return new ISwapValidator<Auftragposition>() {
			
			@Override
			public boolean withArtikelsetPositionen() {
				return false;
			}
			
			@Override
			public void isMoveablePlus(Auftragposition basePosition,
					List<Auftragposition> possiblePos) throws EJBExceptionLP {
			}

			@Override
			public void isMoveable(Auftragposition pos1, List<Auftragposition> possiblePos) {
				Auftrag dto = getEntityManager().find(Auftrag.class, pos1.getAuftragIId());
				if(!dto.getAuftragstatusCNr().equals(
						LocaleFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
							new Exception(
									"Positionen k\u00F6nnen nur im Status 'Angelegt' verschoben werden."));
				}
				Auftragposition pos2 = possiblePos.get(0);
				Integer iSort1 = pos1.getISort();
				Integer iSort2 = possiblePos.get(0).getISort();
				if (pos1.getAuftragpositionartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME)
						&& iSort1 > iSort2
						&& pos2.getNNettoeinzelpreis() != null
						||
					pos2.getAuftragpositionartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME)
								&& iSort1 < iSort2
								&& pos1.getNNettoeinzelpreis() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION,
							new Exception(
									"Die Endsumme darf nicht vor eine preisbehaftete Position verschoben werden."));
				}
			}
				
			@Override
			public void isMoveableMinus(Auftragposition basePosition,
					List<Auftragposition> possiblePos) throws EJBExceptionLP {
			}
		};
	}

	@Override
	public String getZwischensummeFindByVonBisQuery() {
		return Auftragposition.QueryFindIZwsByVonBisIId;
	}

}
