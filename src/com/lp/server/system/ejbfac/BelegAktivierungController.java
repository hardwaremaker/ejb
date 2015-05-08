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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import com.lp.server.system.service.BelegPruefungDto;
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
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notNull(t, "t");
		
		facbean.pruefeAktivierbar(iid, theClientDto);
		List<Timestamp> timestamps = facbean.getAenderungsZeitpunkte(iid);
		timestamps.removeAll(Collections.singleton(null));
		Timestamp tAendern = Collections.max(timestamps);
		
		if(tAendern.after(t)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT,
					"Berechnungszeitpunkt: " + t.toString());
		}
		facbean.aktiviereBeleg(iid, theClientDto);
		return null ;
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		facbean.pruefeAktivierbar(iid, theClientDto);
		Timestamp t = facbean.berechneBeleg(iid, theClientDto);
		t = roundUpLikeDB(t);
		
		BelegPruefungDto pruefungDto = new BelegPruefungDto(iid) ;
		pruefungDto.setBerechnungsZeitpunkt(t) ;
		return pruefungDto ;
	}
	
	
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = berechneBelegControlled(iid, theClientDto) ;
		aktiviereBelegControlled(iid, pruefungDto.getBerechnungsZeitpunkt(), theClientDto);
		return pruefungDto ;
	}
	
	/**
	 * Rundet die Millisekunden des <code>Timestamp t</code> <b>AUF</b>,
	 * wie es der Typ <code>datetime</code> der MSSQL DB macht.<br>
	 * <b>Es wird immer nur aufgerundet, nicht ab!</b><br>
	 * 2 wird aufgerundet auf 3<br>
	 * 5 -> 7<br>
	 * 6 -> 7<br>
	 * 9 ->10<br>
	 * 
	 * Alle anderen Werte werden nicht ver&auml;ndert.
	 * @param t
	 * @return den aufgerundeten Timestamp
	 */
	protected Timestamp roundUpLikeDB(Timestamp t) {
		int ms = (int) (t.getTime() % 10);
		int msRounded = 0;
		
		if(ms == 2) msRounded = 3;
		else if(ms == 5 || ms == 6) msRounded = 7;
		else if(ms == 9) msRounded = 10;
		else msRounded = ms;
		
		return new Timestamp(t.getTime()-ms+msRounded);
	}
}
