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
package com.lp.server.partner.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface PartnerReportFac {

	// modul
	public final static String REPORT_MODUL = "partner";

	// reports
	public final static String REPORT_PART_SERIENBRIEF = "part_serienbrief.jasper";
	public final static String REPORT_PART_KURZBRIEF = "part_kurzbrief.jasper";
	public final static String REPORT_PART_ADRESSETIKETT = "part_adressetikett.jasper";
	public final static String REPORT_PART_PARTNERSTAMMBLATT = "part_partnerstammblatt.jasper";
	public final static String REPORT_PART_EMPFAENGERLISTE = "part_empfaengerliste.jasper";
	public final static String REPORT_PART_GEBURTSTAGSLISTE = "part_geburtstagsliste.jasper";

	public final static String REPORT_KUNDE_LIEFERSTATISTIK = "part_kunde_lieferstatistik.jasper";
	public final static String REPORT_KUNDE_MONATSSTATISTIK = "part_kunde_monatsstatistik.jasper";
	public final static String REPORT_KUNDE_UMSATZSTATISTIK = "part_kunde_umsatzstatistik";

	public JasperPrintLP printAdressetikett(Integer partnerIId,
			Integer ansprechpartnerIId, TheClientDto theClientDto) throws RemoteException;

	public int faxeSerienbrief(Integer serienbriefIId, String sAbsender,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int maileSerienbrief(Integer serienbriefIId, String sAbsenderEmail,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public JasperPrintLP druckeSerienbrief(Integer serienbriefIId,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public SerienbriefEmpfaengerDto[] getSerienbriefEmpfaenger(
			Integer serienbriefIId, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printEmpfaengerliste(Integer serienbriefIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printKurzbrief(KurzbriefDto kurzbriefDtoI,
			TheClientDto theClientDto, Integer iIdPartnerI, boolean bMitLogo)
			throws EJBExceptionLP, RemoteException;
	public JasperPrintLP printSerienbrief(SerienbriefDto kurzbriefDtoI,Integer ansprechpartnerIId,
			TheClientDto theClientDto, Integer iIdPartnerI, boolean bMitLogo)
			throws EJBExceptionLP, RemoteException;
	public JasperPrintLP printPartnerstammblatt(Integer partnerIId,
			TheClientDto theClientDto);
	public JasperPrintLP printGeburtstagsliste(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);
	
}
