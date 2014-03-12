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
package com.lp.server.rechnung.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;

@Remote
public interface RechnungServiceFac {

	// Fix verdrahtet Rechnungtext
	public static final String RECHNUNG_DEFAULT_KOPFTEXT = "Wir verrechnen wie folgt:";
	public static final String RECHNUNG_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihren Auftrag!\n\nMit freundlichen Gr\u00FC\u00DFen";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_RECHNUNGTEXT_I_ID = "i_id";
	public static final String FLR_RECHNUNGTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_RECHNUNGTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_RECHNUNGTEXT_C_NR = "c_nr";
	public static final String FLR_RECHNUNGTEXT_X_TEXTINHALT = "c_textinhalt";
	
	
	// Fix verdrahtet Rechnungtext
	public static final String GUTSCHRIFT_DEFAULT_KOPFTEXT = "Wir schreiben wie folgt gut";
	public static final String GUTSCHRIFT_DEFAULT_FUSSTEXT = "Mit freundlichen Gr\u00FC\u00DFen";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_GUTSCHRIFTTEXT_I_ID = "i_id";
	public static final String FLR_GUTSCHRIFTTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_GUTSCHRIFTTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_GUTSCHRIFTTEXT_C_NR = "c_nr";
	public static final String FLR_GUTSCHRIFTTEXT_X_TEXTINHALT = "c_textinhalt";


	public Integer createRechnungtext(RechnungtextDto rechnungtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public Integer createGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto,
			TheClientDto theClientDto) throws EJBExceptionLP , RemoteException;		
	
	public void removeRechnungtext(RechnungtextDto rechnungtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateRechnungtext(RechnungtextDto rechnungtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeGutschrifttext(GutschrifttextDto gutschrifttextDto,
			TheClientDto theClientDto);
	
	public void updateGutschrifttext(GutschrifttextDto gutschrifttextDto,
			TheClientDto theClientDto);
	
	public RechnungtextDto rechnungtextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;
	
	public GutschrifttextDto gutschrifttextFindByPrimaryKey(Integer iId)
		throws EJBExceptionLP , RemoteException;
	
	public GutschriftsgrundDto gutschriftsgrundFindByPrimaryKey(Integer iId)
		throws EJBExceptionLP , RemoteException;

	public RechnungtextDto rechnungtextFindByMandantLocaleCNr(String pMandant,
			String pSprache, String pText) throws EJBExceptionLP,
			RemoteException;
	
	public GutschrifttextDto gutschrifttextFindByMandantLocaleCNr(String pMandant,
			String pSprache, String pText) throws EJBExceptionLP ,
			RemoteException;

	public RechnungtextDto createDefaultRechnungtext(String sMediaartI,
			String sTextinhaltI, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public GutschrifttextDto createDefaultGutschrifttext(String sMediaartI,
			String sTextinhaltI, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP,RemoteException;

	public Map<String, String> getAllRechnungpositionsart(Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAllGutschriftpositionsart(Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAllProformarechnungpositionsart(
			Locale locale1, Locale locale2) throws EJBExceptionLP,
			RemoteException;

	public RechnungartsprDto rechnungartsprFindByPrimaryKey(
			String rechnungartCNr, Locale locale) throws EJBExceptionLP,
			RemoteException;

	public Map<String, String> uebersetzeRechnungartOptimal(
			DatenspracheIf[] pArray, Locale locale1, Locale locale2)
			throws RemoteException;

	public String uebersetzeRechnungartOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public Map<String, String> uebersetzeZahlungsartOptimal(
			DatenspracheIf[] pArray, Locale locale1, Locale locale2)
			throws RemoteException;

	public String uebersetzeZahlungsartOptimal(String cNr, Locale locale1,
			Locale locale2) throws RemoteException;

	public ZahlungsartsprDto zahlungsartsprFindByPrimaryKey(
			String zahlungsartCNr, Locale locale) throws EJBExceptionLP,
			RemoteException;

	public void createGutschriftpositionsart(
			GutschriftpositionsartDto gutschriftpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateGutschriftpositionsart(
			GutschriftpositionsartDto gutschriftpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void updateGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto, TheClientDto theClientDto)
		throws EJBExceptionLP, RemoteException;

	public GutschriftpositionsartDto gutschriftpositionsartFindByPrimaryKey(
			String positionsartCNr) throws EJBExceptionLP, RemoteException;

	public GutschriftpositionsartDto[] gutschriftpositionsartFindAll()
			throws EJBExceptionLP, RemoteException;

	public void createRechnungpositionsart(
			RechnungpositionsartDto rechnungpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateRechnungpositionsart(
			RechnungpositionsartDto rechnungpositionsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungpositionsartDto rechnungpositionsartFindByPrimaryKey(
			String positionsartCNr) throws EJBExceptionLP, RemoteException;

	public RechnungpositionsartDto[] rechnungpositionsartFindAll()
			throws EJBExceptionLP, RemoteException;

	public void createProformarechnungpositionsart(
			ProformarechnungpositionsartDto proformarechnungpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateProformarechnungpositionsart(
			ProformarechnungpositionsartDto proformarechnungpositionsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ProformarechnungpositionsartDto proformarechnungpositionsartFindByPrimaryKey(
			String positionsartCNr) throws EJBExceptionLP, RemoteException;

	public ProformarechnungpositionsartDto[] proformarechnungpositionsartFindAll()
			throws EJBExceptionLP, RemoteException;

	public void updateRechnungart(RechnungartDto rechnungartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZahlungsart(ZahlungsartDto zahlungsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createRechnungtyp(RechnungtypDto rechnungtypDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungtypDto rechnungtypFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public RechnungtypDto[] rechnungtypFindAll() throws EJBExceptionLP,
			RemoteException;

	public Map<?, ?> getAllZahlungsarten(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAllRechnungartRechnung(Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAllRechnungartGutschrift(Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public Map<String, String> getAllRechnungartProformarechnung(
			Locale locale1, Locale locale2) throws EJBExceptionLP,
			RemoteException;

	public void createRechnungart(RechnungartDto rechnungartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungartDto rechnungartFindByPrimaryKey(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public RechnungartDto[] rechnungartFindAll() throws EJBExceptionLP,
			RemoteException;

	public RechnungartDto[] rechnungartFindByRechnungtyp(String rechnungtypCNr)
			throws EJBExceptionLP, RemoteException;

	public void createZahlungsart(ZahlungsartDto zahlungsartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZahlungsartDto[] zahlungsartFindAll() throws EJBExceptionLP,
			RemoteException;

	public void createRechnungstatus(RechnungstatusDto rechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeRechnungstatus(RechnungstatusDto rechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public void removeGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto,
			TheClientDto theClientDto) throws EJBExceptionLP , RemoteException;

	public void updateRechnungstatus(RechnungstatusDto rechnungstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public RechnungstatusDto rechnungstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public ZahlungsartDto zahlungsartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
}
