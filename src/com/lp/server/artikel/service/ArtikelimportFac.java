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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Remote;

import jxl.Cell;

import com.lp.server.system.service.TheClientDto;

@Remote
public interface ArtikelimportFac {
	public void importiereArtikel(ArtikelImportDto[] daten,
			boolean bBestehendeArtikelUeberschreiben, TheClientDto theClientDto);

	public String pruefeUndImportiereArtikelXLS(byte[] xlsDatei,
			java.sql.Timestamp tDefaultEK,
			java.sql.Timestamp tDefaultVK, boolean bImportierenWennKeinFehler, TheClientDto theClientDto);
	
	
	public String importiereAllergeneXLS(byte[] xlsDatei, Integer lieferantIId, TheClientDto theClientDto);
	public String gestpreisImportieren(TheClientDto theClientDto,
			byte[] CRLFAscii, String rueckgabe, String artikelnummer,
			ArtikelDto artikelDto, BigDecimal preiseinheit, Integer artikelIId,
			BigDecimal gestehungspreis) throws RemoteException;
	public Integer artikelgruppeSuchenUndAnlegen(TheClientDto theClientDto,
			String artikelgruppe) throws RemoteException;
	public Integer artikelklasseSuchenUndAnlegen(TheClientDto theClientDto,
			 String artikelklasse) throws RemoteException;
	public String  einheitSuchenUndAnlegen(TheClientDto theClientDto,String einheitCNr) throws RemoteException;
	public Integer materialSuchenUndAnlegen(TheClientDto theClientDto,
			 String material) throws RemoteException;
	public Integer herstellerSuchenUndAnlegen(TheClientDto theClientDto,
			String hersteller) throws RemoteException ;
	public void artikelkommentartAnlegen(TheClientDto theClientDto,
			String kommentarart, String kommentar, ArrayList alBelege,
			Integer artikelIId) throws RemoteException;
	public BigDecimal vkPreisAnlegen(java.sql.Timestamp tDefaultEK,
			TheClientDto theClientDto, BigDecimal vkPreisbasis,
			BigDecimal preiseinheit, Integer artikelIId);
	public void lagerplatzAnlegen(TheClientDto theClientDto,
			Integer lagerIId_Hauptlager, String lagerplatz, Integer artikelIId)
			throws RemoteException;
	
	public Integer farbcodeSuchenUndAnlegen(TheClientDto theClientDto,
			String farbcode) throws RemoteException;
	public Integer reachSuchenUndAnlegen(TheClientDto theClientDto,
			String reach) throws RemoteException;
	public Integer automotiveSuchenUndAnlegen(TheClientDto theClientDto,
			String automotive) throws RemoteException;
	public Integer rohsSuchenUndAnlegen(TheClientDto theClientDto,
			String rohs) throws RemoteException;
	public Integer medicalSuchenUndAnlegen(TheClientDto theClientDto,
			String medical) throws RemoteException;
	
	

}
