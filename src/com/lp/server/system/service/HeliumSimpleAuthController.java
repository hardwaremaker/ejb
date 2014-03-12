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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class HeliumSimpleAuthController {

	private TheClientDto webClientDto = null ;
	private BenutzerFac theBenutzerFac = null ;
	private MandantFac theMandantFac = null ;
	
	public HeliumSimpleAuthController(BenutzerFac benutzerFac, MandantFac mandantFac) {
		theBenutzerFac = benutzerFac ;
		theMandantFac = mandantFac ;
	}

	private boolean isEmptyString(String string) {
		return null == string || 0 == string.trim().length() ;
	}
	
	public void setupSessionParams(HeliumAuthHeader header) throws HeliumSimpleAuthException {
		webClientDto = null ;
		
		if(null == header) {
			throw new HeliumSimpleAuthException(
				EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;
		}

		if(isEmptyString(header.getUser())) {
			throw new HeliumSimpleAuthException(
				EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;
		}
		
		if(isEmptyString(header.getPassword())) {
			throw new HeliumSimpleAuthException(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;			
		}
				
		if(isEmptyString(header.getIsoCountry())) {
			header.setIsoCountry("AT") ;
		}
		if(isEmptyString(header.getIsoLanguage())) {
			header.setIsoLanguage("de") ;
		}
		
		try {
			String password = new String(Helper.getMD5Hash((header.getUser() + header.getPassword())
					.toCharArray())) ;
			BenutzerDto benutzerDto = theBenutzerFac.benutzerFindByCBenutzerkennungOhneEx(header.getUser(), password) ;
			if(null == benutzerDto) {
				throw new HeliumSimpleAuthException(EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;
			}
			
			if(benutzerDto.getTGueltigbis() != null) {
				Timestamp t = new Timestamp(new Date().getTime()) ;
				if(benutzerDto.getTGueltigbis().before(t)) {
					throw new HeliumSimpleAuthException(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG) ;
				}
			}
			
			if(benutzerDto.getBGesperrt() != null && benutzerDto.getBGesperrt() > 0) {
				throw new HeliumSimpleAuthException(EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT) ;				
			}
			
			MandantDto mandantDto = theMandantFac.mandantFindByPrimaryKey(benutzerDto.getMandantCNrDefault(), null) ;
			
			webClientDto = new TheClientDto() ;
			webClientDto.setBenutzername(benutzerDto.getCBenutzerkennung()) ;
			webClientDto.setMandant(mandantDto.getCNr()) ;
			webClientDto.setSMandantenwaehrung(mandantDto.getWaehrungCNr()) ;

			BenutzermandantsystemrolleDto bmsDto = 
					theBenutzerFac.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzerDto.getIId(), benutzerDto.getMandantCNrDefault()) ;			
			webClientDto.setIDPersonal(bmsDto.getPersonalIIdZugeordnet()) ;

			Locale l = new Locale(header.getIsoLanguage(), header.getIsoCountry()) ;
			webClientDto.setLocMandant(l) ;
			webClientDto.setUiLoc(l) ;
			webClientDto.setLocKonzern(l) ;
			
		} catch(EJBExceptionLP e) {
			throw new HeliumSimpleAuthException(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, e) ;			
		} catch(RemoteException e) {
			throw new HeliumSimpleAuthException(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, e) ;
		}  		
	}
	
	public TheClientDto getWebClientDto() {
		return webClientDto ;
	}
}
