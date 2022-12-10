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
import java.util.Map;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface DruckerFac {

	public final static String REPORTKONF_KOMPONENTENTYP_SELECTFIELD = "SELECTFIELD";
	public final static String REPORTKONF_KOMPONENTENTYP_CHECKBOX = "CHECKBOX";
	public final static String REPORTKONF_KOMPONENTENTYP_DATEFIELD = "DATEFIELD";
	public final static String REPORTKONF_KOMPONENTENTYP_TEXTFIELD = "TEXTFIELD";
	public final static String REPORTKONF_KOMPONENTENTYP_RADIOBUTTON = "RADIOBUTTON";
	public final static String REPORTKONF_KOMPONENTENTYP_JTOGGLEBUTTON = "JTOGGLEBUTTON";
	public final static String REPORTKONF_KOMPONENTENTYP_COMBOBOX = "COMBOBOX";
	public final static String REPORTKONF_KOMPONENTENTYP_COMBOBOX_PERIODE = "COMBOBOX_PERIODE";
	public final static String REPORTKONF_KOMPONENTENTYP_NUMBERFIELD = "NUMBERFIELD";
	public final static String REPORTKONF_KOMPONENTENTYP_SPINNER = "SPINNER";

	public Integer createStandarddrucker(StandarddruckerDto standarddruckerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer updateStandarddrucker(StandarddruckerDto standarddruckerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ReportvarianteDto reportvarianteFindByPrimaryKey(Integer iId);

	public StandarddruckerDto standarddruckerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public StandarddruckerDto standarddruckerFindByPcReportname(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneExc(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeStandarddrucker(StandarddruckerDto standarddruckerDto,
			String idUser) throws EJBExceptionLP, RemoteException;

	public void saveReportKonf(Integer standarddruckerIId,
			ReportkonfDto[] dtos, TheClientDto theClientDto)
			throws RemoteException;

	public void deleteReportKonf(Integer standarddruckerIId,
			TheClientDto theClientDto) throws RemoteException;

	public ReportkonfDto[] reportkonfFindByStandarddruckerIId(
			Integer standarddruckerIId) throws RemoteException;

	public void removeReportkonf(Integer reportkonfIId) throws RemoteException;

	public Map holeAlleVarianten(String reportname,
			TheClientDto theClientDto);

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneVariante(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto);

	public void removeReportvariante(ReportvarianteDto dto,
			TheClientDto theClientDto);
	public void updateReportvariante(ReportvarianteDto reportvarianteDto,
			TheClientDto theClientDto);
	public Integer createReportvariante(ReportvarianteDto reportvarianteDto,
			TheClientDto theClientDto);

	ReportvarianteDto reportvarianteFindByCReportnameCReportnameVariante(String cReportname, String cReportnameVariante);
}
