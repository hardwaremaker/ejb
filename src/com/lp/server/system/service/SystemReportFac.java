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
import java.util.List;
import java.util.Locale;

import javax.ejb.Remote;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperPrint;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.report.PositionRpt;

@Remote
public interface SystemReportFac {

	public final static String REPORT_MODUL = "system";
	public final static String REPORT_VERSANDAUFTRAG = "syst_versandauftrag.jasper";
	public final static String REPORT_DASHBOARD = "syst_dashboard.jasper";
	public final static String REPORT_ENTITYLOG = "syst_entitylog.jasper";
	public final static String REPORT_MODULBERECHTIGUNG = "syst_modulberechtigungen.jasper";
	public final static String REPORT_STATISTIK = "syst_statistik.jasper";
	public final static int REPORT_FLR_SPALTENBREITE_MIN = 10;

	public final static int STATISTIK_OPTION_TAEGLICH=0;
	public final static int STATISTIK_OPTION_WOECHENTLICH=1;
	public final static int STATISTIK_OPTION_MONATLICH=2;
	public final static int STATISTIK_OPTION_QUARTAL=3;
	public final static int STATISTIK_OPTION_JAEHRLICH=4;
	
	
	public JasperPrintLP printUseCaseHandler(String uuid, QueryParameters q,
			int iAnzahlZeilen, String ueberschrift, int[] columnHeaderWidthsFromClientPerspective, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP[] printVersandAuftrag(
			VersandauftragDto versandauftragDto, Integer iAnzahlKopien,
			TheClientDto theClientDto) throws RemoteException;

	public PositionRpt getPositionForReport(String sBelegart,
			Integer iBelegpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printExtraliste(
			ExtralisteRueckgabeTabelleDto extralisteDto, Integer extralisteIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printEntitylog(String filterKey, String filterId,
			String cDatensatz, TheClientDto theClientDto);
	public PositionRpt getPositionForReport(String sBelegart,
			Integer iBelegpositionIId,boolean bLieferscheinDruckAusRechnung, boolean bAuftragAnzahlungsDruckAusRechnung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printModulberechtigungen(TheClientDto theClientDto);
	
	/**
	 * Das Positionsobjekt f&uuml;r den Druck einer Lieferscheinposition ermitteln
	 * 
	 * @param lsposId die Id der Lieferscheinposition (required)
	 * @param aLsposDto das LieferscheinpositionDto, ist es null, wird aus aus der lsposId ermittelt
	 * @param aLieferscheinDto der LieferscheinDto. Ist es null, wird es aus der lsposId ermittelt
	 * @param aKundeDto der Kunde des Lieferscheins. Ist er null, wird er aus der lsposId ermittelt
	 * @param aArtikelDto der Artikel der Position. Ist er null (und wird ben&ouml;tigt) wird er aus der lsposid ermittelt
	 * @param druckAusRechnung true wenn der Lieferschein eine Rechnungsposition ist
	 * @param theClientDto
	 * @return das Positionsobjekt
	 * @throws RemoteException
	 */
	public PositionRpt getLsPosReport(Integer lsposId, 
			LieferscheinpositionDto aLsposDto, LieferscheinDto aLieferscheinDto, 
			KundeDto aKundeDto, ArtikelDto aArtikelDto, boolean druckAusRechnung, TheClientDto theClientDto)  throws RemoteException ;
	
	List<JRPrintElement> getReportCopy(JasperPrint jrPrint, String modul, TheClientDto theClientDto);

	JasperPrint mergeWithPrintTypePrint(MergePrintTypeParams model, TheClientDto theClientDto);
	
	public JasperPrintLP printDashboard(TheClientDto theClientDto);
	
	public JasperPrintLP getABGReport(String belegartCNr, Locale locDruck, TheClientDto theClientDto);
	
	public JasperPrintLP printStatistik(DatumsfilterVonBis vonBis,Integer iOption, String sOption,TheClientDto theClientDto);
	
}
