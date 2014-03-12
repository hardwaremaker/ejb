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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface RahmenbedarfeFac {

	public final static String REPORT_MODUL = "artikel";
	public final static String REPORT_RAHMENBEDARFE = "ww_rahmenbedarfe.jasper";
	public final static String REPORT_ALLERAHMENBEDARFE = "ww_allerahmenbedarfe.jasper";
	public static final String FLR_RAHMENBEDARFE_FLRARTIKEL = "flrartikel";
	public static final String FLR_RAHMENBEDARFE_FLRAUFTRAG = "flrauftrag";
	public static final String FLR_RAHMENBEDARFE_N_GESAMTMENGE = "n_gesamtmenge";

	public void removeRahmenbedarfe(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removeRahmenbedarfe(RahmenbedarfeDto rahmenbedarfeDto)
			throws RemoteException, EJBExceptionLP;

	public void updateRahmenbedarfe(RahmenbedarfeDto rahmenbedarfeDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public RahmenbedarfeDto rahmenbedarfeFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public RahmenbedarfeDto rahmenbedarfeFindByAuftragIIdArtikelIId(
			Integer auftragIId, Integer artikelIId) throws RemoteException,
			EJBExceptionLP;

	public void createRahmenbedarfe(Integer auftragIId,Integer losIId,
			Integer artikelIId, BigDecimal bdMengeRelativ,TheClientDto theClientDto)
			throws EJBExceptionLP,RemoteException;
	
	public void aktualisiereRahmenbedarfe(DetailbedarfDto b,TheClientDto theClientDto)
			throws RemoteException;

	public void aktualisiereAlleRahmenauftaegeEinesMandanten(TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printAlleOffenenRahmenbedarfe(
			boolean bSortiertNachArtikelnummer, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printRahmenbedarfe(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getSummeAllerRahmenbedarfeEinesArtikels(Integer artikelIId)
			throws RemoteException;
}
