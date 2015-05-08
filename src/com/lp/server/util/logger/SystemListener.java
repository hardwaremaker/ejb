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
package com.lp.server.util.logger;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.pkgenerator.ejb.Sequence;
import com.lp.server.system.pkgenerator.ejb.SequenceBelegnr;

public class SystemListener {

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			this.getClass());

	@PrePersist
	public void logPre(Object object) {
		dolog("PrePersist",object);
	}

	@PostPersist
	public void logPost(Object object) {
		dolog("   PostPersist",object);
	}

	@PreUpdate 
	public void logUpdate(Object object) {
		dolog("PreUpdate",object);
	}

	@PostUpdate
	public void logPUpdate(Object object) {
		dolog("   PostUpdate",object);
	}

	@PostLoad
	public void logLoad(Object object) {
		//dolog("PostLoad",object);
	}

//	@PreRemove
//	@PostRemove
	
	private void dolog(String Msg, Object object) {
		String id = "";
		if(object instanceof Bestellung ) {
			id = "   IID: " +((Bestellung)object).getIId();
		}
		if(object instanceof Sequence ) {
			id = "   IID: " + ((Sequence)object).getCName() + " " +((Sequence)object).getIIndex();
		}
		if(object instanceof Bestellposition ) {
			id = "   IID: " +((Bestellposition)object).getIId();
		}
		if(object instanceof Mwstsatz ) {
			id = "   IID: " +((Mwstsatz)object).getIId();
		}			
		if(object instanceof SequenceBelegnr ) {
			id = "   IID: " + ((SequenceBelegnr)object).getPk().getCNamebeleg()
				+ " " + ((SequenceBelegnr)object).getPk().getMandantCNr() 
				+ " " + ((SequenceBelegnr)object).getPk().getIGeschaeftsjahr()
				+ " " + ((SequenceBelegnr)object).getIIndex();
		}
		
		
		myLogger.logKritisch(Msg + ": "+ object.toString()+id);
		System.out.println(Msg + ": " + object.toString()+id);

	}
}
