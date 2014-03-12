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

import javax.ejb.Remote;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BelegpositionkonvertierungFac {

	public AgstklpositionDto[] konvertiereNachAgstklpositionDto(
			BelegpositionDto[] belegpositionDto, Integer agstklIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StuecklistepositionDto[] konvertiereNachStklpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	public StuecklistearbeitsplanDto[] konvertiereNachStklarbeitsplanDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto);
	public BestellpositionDto[] konvertiereNachBestellpositionDto(BestellungDto bestellungDto,
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungPositionDto[] konvertiereNachRechnungpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto[] konvertiereNachLieferscheinpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnfragepositionDto[] konvertiereNachAnfragepositionDto(AnfrageDto anfrageDto, 
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotpositionDto[] konvertiereNachAngebotpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AuftragpositionDto[] konvertiereNachAuftragpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LossollarbeitsplanDto[] konvertiereNachLossollarbeitsplanDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	public EinkaufsangebotpositionDto[] konvertiereNachEinkaufsangebotpositionDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
			throws RemoteException;
	
	public BestellvorschlagDto[] konvertiereNachBestellvorschlagDto(BelegpositionDto[] belegpositionDto, TheClientDto theClientDto)
	 throws EJBExceptionLP, RemoteException;
	
	
	public WartungsschritteDto[] konvertiereNachWartungsschritteDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto);
	public WartungslisteDto[] konvertiereNachWartungslisteDto(
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto);
	
	public BelegpositionVerkaufDto cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(
			BelegpositionVerkaufDto target, BelegpositionVerkaufDto source,
			TheClientDto theClientDto);
	
	
}
