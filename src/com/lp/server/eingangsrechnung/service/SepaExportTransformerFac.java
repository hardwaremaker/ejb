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
package com.lp.server.eingangsrechnung.service;

import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Local
public interface SepaExportTransformerFac {

	public static final String MSGID_PREFIX = "M";
	public static final String MSGID_DATE_FORMAT = "ddMMyy-HHmmss";
	
	public static final String PMTINFID_PREFIX = "P";
	public static final String PMTINFID_DATE_FORMAT = "ddMMyy-HHmm";
	public static final String SERVICE_LEVEL_CODE_SEPA = "SEPA";
	public static final String CURRENCY_SEPA = "EUR";
	
	public static final String CREATIONDATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String REQUESTEDEXECUTIONDATE_FORMAT = "yyyy-MM-dd";
	public static final String DATEOFSIGNATURE_FORMAT = "yyyy-MM-dd";
	public static final String TIMEZONE_SEPA = "CST";
	
	public static final String DELIMITER_RMTINF_USTRD = ", ";
	
	public static class XMLLength {
		public static final Integer Max140Text = 140;
	}
	
	public Object transform(ZahlungsvorschlaglaufDto zvlaufDto,
			ZahlungsvorschlagDto[] zvDtos, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
}
