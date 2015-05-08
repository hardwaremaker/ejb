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
package com.lp.server.anfrage.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AnfrageServiceFac {
	public static final String ANFRAGETEXT_KOPFTEXT = MediaFac.MEDIAART_KOPFTEXT;
	public static final String ANFRAGETEXT_FUSSTEXT = MediaFac.MEDIAART_FUSSTEXT;

	public static final String ANFRAGESTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String ANFRAGESTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String ANFRAGESTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String ANFRAGESTATUS_ERFASST = LocaleFac.STATUS_ERFASST;
	public static final String ANFRAGESTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;

	public static final String ANFRAGEART_LIEFERANT = "Lieferant      ";
	public static final String ANFRAGEART_LIEFERGRUPPE = "Liefergruppe   ";

	public static final String ANFRAGEART_LIEFERGRUPPE_SHORT = "G";

	public static final String ANFRAGEPOSITIONART_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String ANFRAGEPOSITIONART_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String ANFRAGEPOSITIONART_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String ANFRAGEPOSITIONART_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String ANFRAGEPOSITIONART_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String ANFRAGEPOSITIONART_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;
	public static final String ANFRAGEPOSITIONART_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;

	
	public static final String ANFRAGEERLEDIGUNGSGRUND_BESTELLT = "Bestellt";
	
	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_ANFRAGETEXT_MEDIAART_C_NR = "mediaart_c_nr";
	public static final String FLR_ANFRAGETEXT_X_TEXTINHALT = "x_textinhalt";

	public static final String FLR_ANFRAGEPOSITIONART_POSITIONSART_C_NR = "positionsart_c_nr";

	// Fix verdrahtet Anfragetexte
	public static final String ANFRAGE_DEFAULT_KOPFTEXT = "Wir stellen unsere Anfrage wie folgt:";
	public static final String ANFRAGE_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihr Angebot,";

	public void createAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnfragestatus(AnfragestatusDto anfragestatusDto)
			throws EJBExceptionLP, RemoteException;

	public AnfragestatusDto anfragestatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public String createAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAnfragepositionart(String cNrAnfragepositionartI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AnfragepositionartDto anfragepositionartFindByPrimaryKey(
			String cNrAnfragepositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAnfragepositionart(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnfragetext(AnfragetextDto anfragetextDto)
			throws EJBExceptionLP, RemoteException;

	public AnfragetextDto anfragetextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public AnfragetextDto anfragetextFindByMandantLocaleCNr(String cNrLocaleI,
			String cNrMediaartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnfragetextDto createDefaultAnfragetext(String cNrMediaartI,
			String cNrLocaleI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String createAnfrageart(AnfrageartDto anfrageartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeAnfrageart(String cNrAnfrageartI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateAnfrageart(AnfrageartDto anfrageartDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public AnfrageartDto anfrageartFindByPrimaryKey(String cNrAnfrageartI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void createAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAnfrageartspr(String localeCNr, String anfrageartCNr)
			throws RemoteException, EJBExceptionLP;

	public void removeAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws RemoteException, EJBExceptionLP;

	public void updateAnfrageartspr(AnfrageartsprDto anfrageartsprDto)
			throws RemoteException, EJBExceptionLP;

	public AnfrageartsprDto anfrageartsprFindByPrimaryKey(String localeCNr,
			String anfrageartCNr) throws RemoteException, EJBExceptionLP;

	public Map<?, ?> getAnfragearten(Locale locale1, Locale locale2,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public Integer createZertifikatart(ZertifikatartDto anfragetextDto);

	public void updateZertifikatart(ZertifikatartDto zertifikatartDto);

	public ZertifikatartDto zertifikatartFindByPrimaryKey(Integer iId);

	public void removeZertifikatart(ZertifikatartDto dto);
	
	public Integer createAnfrageerledigungsgrund(
			AnfrageerledigungsgrundDto anfrageerledigungsgrundDto,
			TheClientDto theClientDto);
	public void updateAnfrageerledigungsgrund(
			AnfrageerledigungsgrundDto anfrageerledigungsgrundDto);
	public AnfrageerledigungsgrundDto anfrageerledigungsgrundFindByPrimaryKey(
			Integer iId);
	public void removeAnfrageerledigungsgrund(Integer iId);
	public boolean sindErledigugnsgruendeVorhanden(TheClientDto theclientDto);

}
