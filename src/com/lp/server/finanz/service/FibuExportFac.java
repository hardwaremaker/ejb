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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FibuExportFac {
	public final static String ZIEL_RZL = "RZL";
	public final static String ZIEL_BMD = "BMD";
	public final static String ZIEL_ABACUS = "ABACUS";
	public final static String ZIEL_SCHLEUPEN = "SCHLEUPEN";
	public final static String ZIEL_DATEV = "DATEV-CSV";

	public final static String VARIANTE_KOSTENSTELLEN = "Kostenstellen";
	public final static String VARIANTE_ARTIKELGRUPPEN = "Artikelgruppen";

	public final static String MWSTSATZBEZ_DRUCKNAME_USTRED = "UstRedSteuersatz";
	public final static String MWSTSATZBEZ_DRUCKNAME_USTNORMAL = "UstNormalsteuersatz";
	public final static String MWSTSATZBEZ_DRUCKNAME_USTLUXUS ="UstLuxussteuer";
	public final static String MWSTSATZBEZ_DRUCKNAME_USTOHNE = "UstSteuerfrei";

	public final static String DATEV = "DATEV";
	public final static String HV_RAW = "HV RAW";
	public static final String RZL_CSV = "RZL-CSV";
	
	public final static String RZL_CSV_FILENAME_PREFIX = "EXTF_";
	
	public String exportiereBelege(
			FibuExportKriterienDto fibuExportKriterienDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	// public String
	// getNochNichtVerbuchteEingangsrechnungenAsString(java.sql.Date dStichtag,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException;
	//
	//
	// public String getNochNichtVerbuchteRechnungenAsString(java.sql.Date
	// dStichtag,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException;
	//
	//
	// public String getNochNichtVerbuchteGutschriftenAsString(java.sql.Date
	// dStichtag,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException;
	//
	//
	public ExportdatenDto exportdatenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ExportdatenDto exportdatenFindByBelegartCNrBelegiid(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP,
			RemoteException;

	public ExportlaufDto exportlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public void nehmeExportlaufZurueckUndLoescheIhn(Integer exportlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String exportierePersonenkonten(String kontotypCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer exportlaufFindLetztenExportlauf(String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public void removeExportdaten(Integer exportdatenIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ExportlaufDto createExportlauf(ExportlaufDto exportlaufDto)
	throws EJBExceptionLP;
	
	public ArrayList<IntrastatDto> exportiereIntrastatmeldung(
			String sVerfahren, java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<IntrastatDto> getIntrastatDatenWareneingang(
			java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<IntrastatDto> getIntrastatDatenVersand(java.sql.Date dVon,
			java.sql.Date dBis, BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String importiereOffenePosten(ArrayList<String[]> daten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public ExportdatenDto createExportdaten(ExportdatenDto exportdatenDto)
	throws EJBExceptionLP;

	public ExportdatenDto[] exportdatenFindByExportlaufIIdBelegartCNr(Integer exportlaufIId, String belegartCNr)
		throws EJBExceptionLP;
	
	public List<String> exportiereBuchungsjournal(String format, Date von, Date bis,
			boolean mitAutoEB, boolean mitManEB, boolean mitAutoB, boolean mitStornierte, String bezeichnung,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
}
