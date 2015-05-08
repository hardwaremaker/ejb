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
package com.lp.server.angebot.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotServiceFac {
	// Angebotart
	public static final String ANGEBOTART_FREI = "Freies Angebot";

	public static final String ANGEBOTTEXT_KOPFTEXT = MediaFac.MEDIAART_KOPFTEXT;
	public static final String ANGEBOTTEXT_FUSSTEXT = MediaFac.MEDIAART_FUSSTEXT;

	// Angebotstatus
	public static final String ANGEBOTSTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String ANGEBOTSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String ANGEBOTSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String ANGEBOTSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;

	// Angeboteinheit
	public static final String ANGEBOTEINHEIT_STUNDE = SystemFac.EINHEIT_STUNDE;
	public static final String ANGEBOTEINHEIT_TAG = SystemFac.EINHEIT_TAG;
	public static final String ANGEBOTEINHEIT_WOCHE = SystemFac.EINHEIT_WOCHE;

	// Angeboterledigungsgrund
	public static final String ANGEBOTERLEDIGUNGSGRUND_ZUTEUER = "Zu teuer";
	public static final String ANGEBOTERLEDIGUNGSGRUND_PROJEKTABGESAGT = "Projekt abgesagt";
	public static final String ANGEBOTERLEDIGUNGSGRUND_ANMITBEWERBERVERLOREN = "An Mitbewerber verloren";
	// wie Stati; nicht wartbar
	public static final String ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN = "Auftrag erhalten";

	// Angebotpositionart
	public static final String ANGEBOTPOSITIONART_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String ANGEBOTPOSITIONART_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String ANGEBOTPOSITIONART_POSITION = LocaleFac.POSITIONSART_POSITION;
	public static final String ANGEBOTPOSITIONART_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String ANGEBOTPOSITIONART_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String ANGEBOTPOSITIONART_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String ANGEBOTPOSITIONART_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;
	public static final String ANGEBOTPOSITIONART_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;
	public static final String ANGEBOTPOSITIONART_AGSTUECKLISTE = LocaleFac.POSITIONSART_AGSTUECKLISTE;
	public static final String ANGEBOTPOSITIONART_STUECKLISTENPOSITION = LocaleFac.POSITIONSART_STUECKLISTENPOSITION; // unterstkl
	// :
	// 1
	// diese
	// Positionsart
	// existiert
	// nicht
	// auf
	// der
	// DB
	// ,
	// sie
	// dient
	// zum
	// Drucken
	public static final String ANGEBOTPOSITIONART_ENDSUMME = LocaleFac.POSITIONSART_ENDSUMME; // endsumme
	public static final String ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME = LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME ;

	// :
	// 1
	// Angebotspositionsart
	// Endsumme

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_ANGEBOTTEXT_MEDIAART_C_NR = "mediaart_c_nr";
	public static final String FLR_ANGEBOTTEXT_X_TEXTINHALT = "x_textinhalt";

	public static final String FLR_ANGEBOTPOSITIONART_POSITIONSART_C_NR = "positionsart_c_nr";

	// Fix verdrahtet Angebotstext
	public static final String ANGEBOT_DEFAULT_KOPFTEXT = "Wir legen unser Angebot wie folgt:";
	public static final String ANGEBOT_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihr Interesse,";

	public Integer createAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAngebottext(AngebottextDto angebottextDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AngebottextDto angebottextFindByPrimaryKey(Integer iIdAngebottextI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AngebottextDto angebottextFindByMandantCNrLocaleCNrCNr(
			String cNrLocaleI, String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebottextDto createDefaultAngebottext(String cNrMediaartI,
			String cNrLocaleI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void createAngebotartspr(AngebotartsprDto angebotartsprDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAngebotartspr(AngebotartsprDto angebotartsprDtoI)
			throws EJBExceptionLP, RemoteException;

	public void updateAngebotartspr(AngebotartsprDto angebotartsprDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotartsprDto angebotartsprFindByPrimaryKey(String localeCNrI,
			String angebotartCNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public AngebotartsprDto angebotartsprFindByLocaleCNrAngebotartCNr(
			String cNrLocaleI, String cNrAngebotartI) throws EJBExceptionLP,
			RemoteException;

	public Map<?, ?> getAngebotarten(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public String createAngebotart(AngebotartDto angebotartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAngebotart(String cNrAngebotartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAngebotart(AngebotartDto angebotartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotartDto angebotartFindByPrimaryKey(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAngebotstatus(AngebotstatusDto angebotstatusDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotstatusDto angebotstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public void createAngeboteinheit(AngeboteinheitDto angeboteinheitDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAngeboteinheit(String einheitCNr) throws RemoteException,
			EJBExceptionLP;

	public void updateAngeboteinheit(AngeboteinheitDto angeboteinheitDto)
			throws EJBExceptionLP, RemoteException;

	public AngeboteinheitDto angeboteinheitFindByPrimaryKey(String einheitCNr)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAngeboteinheiten(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public String createAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeAngeboterledigungsgrund(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public AngeboterledigungsgrundDto angeboterledigungsgrundFindByPrimaryKey(
			String cNrI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void createAngeboterledigungsgrundspr(
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto)
			throws RemoteException, EJBExceptionLP;

	public void removeAngeboterledigungsgrundspr(String localeCNr,
			String angbeoterledigungsgrundCNr) throws RemoteException,
			EJBExceptionLP;

	public void updateAngeboterledigungsgrundspr(
			AngeboterledigungsgrundsprDto angeboterledigungsgrundsprDto)
			throws RemoteException, EJBExceptionLP;

	public AngeboterledigungsgrundsprDto angeboterledigungsgrundsprFindByPrimaryKey(
			String localeCNr, String angbeoterledigungsgrundCNr)
			throws RemoteException, EJBExceptionLP;

	public String createAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAngebotpositionart(String cNrAngebotpositionartI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotpositionartDto angebotpositionartFindByPrimaryKey(
			String cNrAngebotpositionartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void checkAngebotIId(Integer iIdAngebotI) throws EJBExceptionLP,
			RemoteException;

	public Integer getLieferzeitInAngeboteinheit(Integer iIdAngebotI,
			String cNrAngeboteinheitI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Map<String, String> getAngebotpositionart(Locale locale1I,
			Locale locale2I, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;
}
