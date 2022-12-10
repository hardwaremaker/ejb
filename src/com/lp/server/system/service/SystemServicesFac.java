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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface SystemServicesFac {
	public final static String KEYVALUE_MECSTERMINAL = "MECSTERMINAL";
	public final static String KEYVALUE_ARTIKEL_KOPIEREN = "ARTIKEL_KOPIEREN";
	public final static String KEYVALUE_STKL_IMPORT_SPEZ = "STKL_IMPORT_SPEZ";
	public final static String KEYVALUE_AGSTKL_IMPORT_SPEZ = "AGSTKL_IMPORT_SPEZ";
	public final static String KEYVALUE_EINKAUFSAGSTKL_IMPORT_SPEZ = "EINKAUFSAGSTKL_IMPORT_SPEZ";
	public final static String KEYVALUE_BESTELLUNGSTKL_IMPORT_SPEZ = "BESTELLUNGSTKL_IMPORT_SPEZ";
	public final static String KEYVALUE_ZEITDATEN_QUELLE = "ZEITDATEN_QUELLE";

	public final static String KEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG = "EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG";
	public final static String KEYVALUE_EINSTELLUNGEN_LETZTE_INTERNE_BESTELLUNG = "EINSTELLUNGEN_LETZTE_INTERNE_BESTELLUNG";
	public final static String KEYVALUE_EINSTELLUNGEN_LETZTER_AUSLIEFERVORSCHLAG = "EINSTELLUNGEN_LETZTER_AUSLIEFERVORSCHLAG";
	public final static String KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG = "EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG";

	public final static String KEYVALUE_WEP_EXPORT_PRO_FIRST_LETZTER_ZEITPUNKT = "WEP_EXPORT_PRO_FIRST_LETZTER_ZEITPUNKT";
	
	public final static String KEYVALUE_GTIN_ARTIKELNUMMER = "GTIN_ARTIKELNUMMER";
	
	public void createKeyvalue(KeyvalueDto keyvalueDto) throws RemoteException,
			EJBExceptionLP;

	public void removeKeyvalue(String cGruppe, String cKey)
			throws RemoteException, EJBExceptionLP;

	public KeyvalueDto[] keyvalueFindyByCGruppe(String cGruppe)
			throws RemoteException;

	public void removeKeyvalue(KeyvalueDto keyvalueDto) throws RemoteException,
			EJBExceptionLP;

	public void updateKeyvalue(KeyvalueDto keyvalueDto) throws RemoteException,
			EJBExceptionLP;

	public KeyvalueDto keyvalueFindByPrimaryKey(String cGruppe, String cKey)
			throws RemoteException, EJBExceptionLP;

	public void speichereKeyValueDtos(KeyvalueDto[] keyvalueDto)
			throws RemoteException;

	public String getPrinterNameForReport(String modulI, String filenameI,
			Locale spracheI, String cSubdirectory, TheClientDto theClientDto);
	public void replaceKeyvaluesEinerGruppe(String cGruppe, ArrayList<KeyvalueDto> alDtos);
	
	public LPDatenSubreport getSubreportLetzteEinstellungen(String cGruppe);
	
}
