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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprPK;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ArtikelkommentarFac {

	public static final String FLR_ARTIKELKOMMENTARART_ARTIKELKOMMENTARARTSPRSET = "artikelkommentarartsprset";
	public static final String FLR_ARTIKELKOMMENTAR_B_DEFAULTBILD = "b_defaultbild";
	public static final String FLR_ARTIKELKOMMENTAR_DATENFORMAT_C_NR = "datenformat_c_nr";
	public static final String FLR_ARTIKELKOMMENTAR_FLRARTIKELKOMMENTARART = "flrartikelkommentarart";
	public static final String FLR_ARTIKELKOMMENTARSPR_X_KOMMENTAR = "x_kommentar";

	

	public static final int ARTIKELKOMMENTARART_MITDRUCKEN = 0;
	public static final int ARTIKELKOMMENTARART_HINWEIS = 1;
	public static final int ARTIKELKOMMENTARART_ANHANG = 2;
	
	public Integer createArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelkommentarartDto artikelkommentarartFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ArrayList<KeyvalueDto> getArtikelhinweise(Integer artikelIId, String belegartCNr,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createArtikelkommentar(
			ArtikelkommentarDto artikelkommentarDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikelkommentar(ArtikelkommentarDto artikelkommentarDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelkommentar(ArtikelkommentarDto artikelkommentarDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public byte[] getArtikeldefaultBild(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean gibtEsKommentareInAnderenSprachen(
			Integer artikelkommentarIId, TheClientDto theClientDto)
			throws RemoteException;

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelkommentarDto[] artikelkommentarFindByArtikelIId(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKey(
			Integer artikelkommentarIId, String localeCNr)
			throws EJBExceptionLP, RemoteException;

	public Integer createArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP, RemoteException;

	public boolean kopiereArtikelkommentar(Integer artikelIId_alt,
			Integer artikelIId_neu, boolean bNurClientLocale, TheClientDto theClientDto)
			throws RemoteException;

	public void removeArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelkommentardrucks(
			ArtikelkommentardruckDto[] artikelkommentardruckDtos)
			throws EJBExceptionLP, RemoteException;

	public ArtikelkommentardruckDto artikelkommentardruckFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNr(
			Integer artikelIId, String belegartCNr, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKeyUndLocale(
			Integer iId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public ArrayList<byte[]> getArtikelhinweiseBild(Integer artikelIId, String belegartCNr,
			TheClientDto theClientDto);
	public ArrayList<byte[]> getArtikelBilder(Integer artikelIId, TheClientDto theClientDto);
	
	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
			Integer artikelIId, String belegartCNr, String localeCNr,
			TheClientDto theClientDto);
	public void vertauscheArtikelkommentar(Integer iiD1, Integer iId2);
	
	public ArrayList<byte[]> getArtikelbilderFindByArtikelIIdBelegartCNr(
			Integer artikelIId, String belegartCNr, String localeCNr,
			TheClientDto theClientDto);
	public boolean sindTexteOderPDFsVorhanden(Integer artikelIId,
			TheClientDto theClientDto);
	
	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKeyOhneExc(
			Integer artikelkommentarIId, String localeCNr,
			TheClientDto theClientDto);
	
	
	public byte[] getPDFArtikelkommentar(String artikelCNr,
			String artikelkommentarartCNr, String localeCNr, String mandantCNr);
	
	public byte[] getArtikelkommentarBild(String artikelCNr,
			String artikelkommentarartCNr, String localeCNr, String mandantCNr);

	/**
	 * Liefert alle Artikelkommentare eines Artikels mit allen Dtos (sofern vorhanden)</br>
	 * Auch z.B. die ArtikelkommentarDruckDtos
	 * 
	 * @param artikelIId IId des Artikels
	 * @param theClientDto
	 * @return eine (leere) Liste aller Artikelkommentare eines Artikels
	 */
	List<ArtikelkommentarDto> artikelkommentarFindByArtikelIIdFull(
			Integer artikelIId, TheClientDto theClientDto);
	
	public String getArtikelkommentarFuerDetail(Integer artikelIId, TheClientDto theClientDto);
	
	public void textAusPdfInXKommentarAktualisieren(Integer artikelkommentarIId,String locUI);

	public String dateiverweisLaufwerkDurchUNCErsetzen(String dateiname, TheClientDto theClientDto);
}
