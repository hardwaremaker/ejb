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
 *******************************************************************************/
package com.lp.server.personal.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.util.EJBExceptionLP;

@Remote
public interface NachrichtenFac {

	public class Art {
		public static final String EMAIL_VERSAND_FEHLGESCHLAGEN = "EMAIL_VERSAND_FEHLGESCHLAGEN";
		public static final String EINZELNACHRICHT = "EINZELNACHRICHT";
		public static final String ZEITDATENPRUEFEN = "ZEITDATENPRUEFEN";		
		public static final String ABLIEFERPREISEPRUEFEN = "ABLIEFERPREISEPRUEFEN";
		public static final String PROJEKT_ZUGEORDNET = "PROJEKT_ZUGEORDNET";
		public static final String PROJEKTZEITEN_UEBERSCHRITTEN_ERZEUGER = "PROJEKTZEITEN_UEBERSCHRITTEN_ERZEUGER";
		public static final String PROJEKTZEITEN_UEBERSCHRITTEN_ALLE = "PROJEKTZEITEN_UEBERSCHRITTEN_ALLE";
		public static final String MASTERDATA_NOTFOUND = "MASTERDATA_NOTFOUND";
		public static final String PRODORDER_NOTFOUND = "PRODORDER_NOTFOUND";
	}
	
	public void updateNachrichtenart(NachrichtenartDto nachrichtenartDto,
			TheClientDto theClientDto);

	public NachrichtenartDto nachrichtenartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public NachrichtengruppeDto nachrichtengruppeFindByPrimaryKey(Integer iId);

	public Integer createNachrichtengruppe(NachrichtengruppeDto dto);

	public void updateNachrichtengruppe(NachrichtengruppeDto dto);

	public void removeNachrichtengruppe(NachrichtengruppeDto dto);
	
	public Integer createNachrichtenabo(NachrichtenaboDto dto);
	public void updateNachrichtenabo(NachrichtenaboDto dto) ;
	public NachrichtenaboDto nachrichtenaboFindByPrimaryKey(Integer iId);
	public void removeNachrichtenabo(NachrichtenaboDto dto);
	
	public NachrichtengruppeteilnehmerDto nachrichtengrupperteilnehmerFindByPrimaryKey(Integer iId);
	public void removeNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto);
	public void updateNachrichtengruppeteilnehmer(
			NachrichtengruppeteilnehmerDto dto);
	public Integer createNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto);

	public void nachrichtAnEinzelpersonErstellen(String cBetreff, String xText,
			Integer personalIId, TheClientDto theClientDto);
	public void nachrichtErstellen(String nachrichtenartCNr, String cBetreff,
			String xText,String belegartCNr, Integer belegartIId,  TheClientDto theClientDto);
	public  void nachrichtErstellen(String nachrichtenartCNr,
			String cBetreff, String xText,
			Integer personalIIdNurEinzelnePerson,Integer personalIIdAusgenommen,String belegartCNr, Integer belegartIId, TheClientDto theClientDto);
	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(
			Integer personalIId, Integer personalIIdAbsender,String suche,boolean bNurEmpfangene,TheClientDto theClientDto);
	public Map getMoeglicheNachrichtenempfaenger(TheClientDto theClientDto);
	
	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(
			Integer personalIId, int iAnzahl,boolean nurNugelesene, Integer personalIIdAbsender,String suche, boolean bNurEmpfangene,TheClientDto theClientDto);
	public void empfangerBenachrichtigen(TheClientDto theClientDto, Object[] oReturnData);
	
	public void benachrichtigeClientsDasNeueNachrichtenVorhandenSind(
			Integer personalIId, TheClientDto theClientDto);
	
	public void nachrichtenempfaengerAlsGelesenMarkieren(Integer nachrichtenempaengerIId, boolean bGelesen, TheClientDto theClientDto);

	void nachrichtZeitdatenpruefen(Integer personalId, TheClientDto theClientDto);

	void nachrichtAblieferpreisepruefenTerminal(Integer losIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;
	
	public void nachrichtProjektZugeordnet(Integer personalId, Integer personalIIdZugewiesener,
			TheClientDto theClientDto);
	
	public void nachrichtProjektzeitenUeberschritten(ProjektDto projektDto, Double dIstZeiten,
			TheClientDto theClientDto);
	
	public String getBetreff(Integer nachrichtenempaengerIId, TheClientDto theClientDto);
	public NachrichtenempfaengerDto nachrichtenempfaengerFindByPrimaryKey(Integer iId);
	
	public Timestamp getAnlageDatumDerNachricht(Integer nachrichtenempfaengeriId);
	
	
	public NachrichtenDto nachrichtenFindByPrimaryKey(Integer iId);
	
	void nachrichtMasterDataNotFound(ArtikelId artikelId, String artikelCnr, TheClientDto theClientDto);
	
	void nachrichtProdOrderNotFound(String losCnr, String artikelCnr, Integer iSortLossollmaterial, TheClientDto theClientDto);
}