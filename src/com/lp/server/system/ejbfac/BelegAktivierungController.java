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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;

public class BelegAktivierungController implements IAktivierbarControlled {
	
	private IAktivierbar facbean;
	
	public BelegAktivierungController(IAktivierbar facbean) {
		this.facbean = facbean;
	}

	@Override
	public void aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notNull(t, "t");
		
		facbean.pruefeAktivierbar(iid, theClientDto);
		if(facbean.hatAenderungenNach(iid, t)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT,
					"Berechnungszeitpunkt: " + t.toString());
		}
		facbean.aktiviereBeleg(iid, theClientDto);
	}

	@Override
	public Timestamp berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		facbean.pruefeAktivierbar(iid, theClientDto);
		facbean.berechneBeleg(iid, theClientDto);
		return new Timestamp(System.currentTimeMillis());
	}

}
