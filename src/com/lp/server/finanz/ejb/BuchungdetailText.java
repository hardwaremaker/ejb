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
package com.lp.server.finanz.ejb;

import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung;
import com.lp.server.system.service.TheClientDto;

public class BuchungdetailText {

	private String saldoVortragBez = null ;
	private BenutzerServicesFacLocal benutzerServiceFac ;
	private TheClientDto theClientDto ;
	
	public BuchungdetailText(BenutzerServicesFacLocal benutzerServiceFac, TheClientDto clientDto) {		
		this.benutzerServiceFac = benutzerServiceFac ;
		theClientDto = clientDto ;
	}

	private String getTextRespectUISpr(String token) {
		return benutzerServiceFac.getTextRespectUISpr(token,
				theClientDto.getMandant(), theClientDto.getLocUi());		
	}
	
	private String getSaldoVortragKurzBez() {
		if(null == saldoVortragBez) {
			saldoVortragBez = getTextRespectUISpr("lp.buchungtypeb") ;
//			try {
//				BuchungsartDto baDto = finanzserviceFac.buchungsartFindByPrimaryKey(FinanzFac.BUCHUNGSART_SALDOVORTRAG) ;
//				saldoVortragBez = baDto.getCKbez() ;
//			} catch(RemoteException e) {			
//			}
		}
		return saldoVortragBez ;
	}
	
	protected String getText(String buchungsArt, String belegArt, boolean isAutomaticBuchung) {
		String art = buchungsArt == null ? "__" : buchungsArt ;
		String belegart = belegArt != null ? belegArt : "" ;

//		if(isAutomaticBuchung) {
//			art = getSaldoVortragKurzBez() ;
//		}

		return art + belegart ;		
	}
	
	public String getTextFuerBuchungsart(FLRFinanzBuchung buchung) {
		String sbuchungsart = null ;
		if(null != buchung.getFlrbuchungsart()) {
			sbuchungsart = buchung.getFlrbuchungsart().getC_kbez() ;
		}
		String sbelegart = null ;
		if(null != buchung.getFlrfbbelegart()) {
			sbelegart = buchung.getFlrfbbelegart().getC_kbez() ;
		}
		
//		Short s = buchung.getB_autombuchung() ;
		return getText(
			sbuchungsart, 
			sbelegart, false) ;		
	}	
	
	/**
	 * Liefert den Text ("EB") falls es sich um eine Automatische Eroeffnungsbuchung handelt</br>
	 * ansonsten "".
	 * 
	 * @param buchung
	 * @return Text("EB") bzw. ""
	 */
	public String getTextFuerAutomatischeEBBuchung(FLRFinanzBuchung buchung) {
		boolean b = null == buchung.getB_autombuchungeb() ? false : (short) 1 == buchung.getB_autombuchungeb() ;
		return b ? getSaldoVortragKurzBez() : "" ;
	}
}
