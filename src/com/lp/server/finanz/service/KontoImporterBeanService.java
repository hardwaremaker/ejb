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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.rmi.RemoteException;

import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;

public class KontoImporterBeanService implements IKontoImporterBeanServices, Serializable {
	private static final long serialVersionUID = 1L;

	private FinanzFac finanzFac ;
	private FinanzServiceFac finanzServiceFac ;
	private SystemFac systemFac ;
	
	private TheClientDto theClientDto ;
	
	public KontoImporterBeanService(TheClientDto clientDto, FinanzFac finanzFac, 
			FinanzServiceFac finanzServiceFac, SystemFac systemFac) {
		this.theClientDto = clientDto ;
		this.finanzFac = finanzFac ;
		this.systemFac = systemFac ;
		this.finanzServiceFac = finanzServiceFac ;		
	}

	public void setTheClientDto(TheClientDto clientDto) {
		theClientDto = clientDto ;
	}
	
	@Override 
	public TheClientDto getTheClientDto() {
		return theClientDto ;
	}
	
	@Override
	public KontoDto updateKonto(KontoDto kontoDto) throws RemoteException {
		kontoDto.setMandantCNr(getTheClientDto().getMandant()) ;
		return finanzFac.createKonto(kontoDto,getTheClientDto()) ;
	}

	@Override
	public KostenstelleDto kostenstelleFindByNummerMandant(String cnr) throws RemoteException  {
		return systemFac.kostenstelleFindByNummerMandantOhneExc(cnr, getTheClientDto().getMandant()) ;
	}

	@Override
	public UvaartDto uvaartFindByCnrMandant(String cNr) throws RemoteException {
		return finanzServiceFac.uvaartFindByCnrMandantOhneExc(cNr, getTheClientDto()) ;
	}
	
	@Override
	public KontoDto kontoFindByCnrMandant(String kontoCnr) throws RemoteException {
		return finanzFac.kontoFindByCnrKontotypMandantOhneExc(
			kontoCnr, finanzServiceFac.KONTOTYP_SACHKONTO, getTheClientDto().getMandant(), getTheClientDto()) ;
	}

	@Override
	public FinanzamtDto[] finanzamtFindAllByMandantCNr() throws RemoteException {
		return finanzFac.finanzamtFindAllByMandantCNr(getTheClientDto());
	}
}
