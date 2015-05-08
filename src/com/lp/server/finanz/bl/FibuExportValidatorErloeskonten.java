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
 *******************************************************************************/
package com.lp.server.finanz.bl;

import java.rmi.RemoteException;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.TheClientDto;

/**
 * Validator f&uuml;r die Erl&ouml;skontenermittlung</br>
 * <p>In diesem speziellen Fall wird die explizite Pr&uuml;fung des 
 * Rechnungsstatus, ob der Exportzeitraum g&uuml;ltig ist und ob die
 * Rechnung bereits vollst&auml;ndig kontiert ist nicht ben&ouml;tigt.
 * Es w&auml;re teilweise sogar hinderlich, denn &uuml;ber den Druck
 * wird erst der Status auf AKTIVIERT gesetzt</p>
 * <p>Au&szlig;erdem sollen keine Exceptions geworfen werden wenn eine
 * der Pr&uuml;fungen fehlschl&auml;gt. Sie sind mittels {@link #getValidations()} ersichtlich</p>
 * @author Gerold
 *
 */
public class FibuExportValidatorErloeskonten extends FibuExportValidator {
	public FibuExportValidatorErloeskonten(TheClientDto theClientDto) {
		super(theClientDto) ;
		setValidationThrowsException(false);
	}
	
	@Override
	public boolean isRechnungVollstaendigKontiert(RechnungDto rechnungDto,
			KundeDto kundeDto) throws RemoteException {
		return true ;
	}
	
	@Override
	public boolean isValidExportZeitraumFuerBeleg(
			FibuExportKriterienDto exportKriterienDto, Object belegDto,
			PartnerDto partnerDto) {
		return true ;
	}
	
	@Override
	public boolean isValidRechnungStatus(RechnungDto rechnungDto) {
		return true ;
	}
}
