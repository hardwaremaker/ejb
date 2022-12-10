/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2021 HELIUM V IT-Solutions GmbH
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
package com.lp.server.system.service;

import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.util.EJBExceptionLP;

public class HeliumTokenAuthController extends HeliumSimpleAuthController {

	private PersonalFac personalFac ;
	
	public HeliumTokenAuthController(BenutzerFac benutzerFac, MandantFac mandantFac, PersonalFac personalFac) {
		super(benutzerFac, mandantFac) ;
		this.personalFac = personalFac ;
	}

	public void setupSessionParams(HeliumAuthHeader header) throws HeliumSimpleAuthException {
		super.setupSessionParams(header) ;
			
		if(null != getWebClientDto().getIDPersonal()) {
			PersonalDto personalDto = personalFac.personalFindByPrimaryKeySmall(getWebClientDto().getIDPersonal()) ;
			if(null == personalDto.getCAusweis() || 0 == personalDto.getCAusweis().trim().length()) {
				throw new HeliumSimpleAuthException(
						EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;										
			}
			
			if(!personalDto.getCAusweis().trim().equals(header.getToken())) {
				throw new HeliumSimpleAuthException(
						EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;					
			}
		}		
	}
}
