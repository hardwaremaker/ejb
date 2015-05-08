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
package com.lp.server.bestellung.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BestellungServiceFac {
	public static final String BESTELLUNGTEXT_KOPFTEXT = MediaFac.MEDIAART_KOPFTEXT;
	public static final String BESTELLUNGTEXT_FUSSTEXT = MediaFac.MEDIAART_FUSSTEXT;

	// Bestellungsstatus
	public static final String BESTELLUNGSSTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_BESTELLUNGTEXT_I_ID = "i_id";
	public static final String FLR_BESTELLUNGTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BESTELLUNGTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_BESTELLUNGTEXT_C_NR = "c_nr";
	public static final String FLR_BESTELLUNGTEXT_X_TEXTINHALT = "x_textinhalt";

	public static final String FLR_BESTELLPOSITIONART_POSITIONSART_C_NR = "positionsart_c_nr";

	public static final String FLR_MAHNTEXT_I_ID = "i_id";
	public static final String FLR_MAHNTEXT_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MAHNTEXT_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_MAHNTEXT_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_MAHNTEXT_X_TEXTINHALT = "c_textinhalt";

	// Bestellungtext
	public static final String BESTELLUNGTEXT_KOPF = "Kopftext";
	public static final String BESTELLUNGTEXT_FUSS = "Fusstext";

	// Fix verdrahtet Bestellungetext
	public static final String BESTELLUNG_DEFAULT_KOPFTEXT = "Wir \u00FCbermitteln Ihnen unsere Bestellung wie folgt:";
	public static final String BESTELLUNG_DEFAULT_FUSSTEXT = "Wir danken f\u00FCr Ihre Lieferung,";

	public Integer createBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP, RemoteException;

	public BestellungtextDto bestellungtextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BestellungtextDto bestellungtextFindByMandantLocaleCNr(
			String cNrMandantI, String cNrLocaleI, String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BestellungtextDto createDefaultBestellungtext(String sMediaartI,
			String cNrLocaleI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String createBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBestellpositionart(String positionsartCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BestellpositionartDto bestellpositionartFindByPrimaryKey(
			String positionsartCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<String, String> getBestellpositionart(Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public void createBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellpositionstatus(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellpositionstatuss(
			BestellpositionstatusDto[] bestellpositionstatusDtos)
			throws EJBExceptionLP, RemoteException;

	public BestellpositionstatusDto bestellpositionstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP, RemoteException;

	public String createBestellungart(BestellungsartDto bestellungartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String createBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void removeBestellungart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellungart(BestellungsartDto bestellungartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public void updateBestellungartspr(BestellungsartsprDto bestellungartsprDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellungart(BestellungsartDto bestellungartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto)
			throws RemoteException;

	public Map<?, ?> getBestellungart(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public BestellungsartDto bestellungartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String createBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBestellungstatus(String statusCNr) throws EJBExceptionLP,
			RemoteException;

	public void removeBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellungstatuss(
			BestellungstatusDto[] bestellungstatusDtos) throws EJBExceptionLP,
			RemoteException;

	public BestellungstatusDto bestellungstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP, RemoteException;

	public BestellungsartDto[] getAllBestellungsArt();

	public MahngruppeDto mahngruppeFindByPrimaryKey(Integer artgruIId);

	public void removeMahngruppe(MahngruppeDto mahngruppeDto);

	public void createMahngruppe(MahngruppeDto mahngruppeDto);

}
