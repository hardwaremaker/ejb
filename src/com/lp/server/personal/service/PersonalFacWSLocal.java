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
package com.lp.server.personal.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Local;
import javax.jws.WebParam;

import com.lp.util.EJBExceptionLP;

@Local
public interface PersonalFacWSLocal {

	// ANMERKUNG: WebserviceCallInterceptor wird nur aufgerufen wenn zusaetzlich zu @Interceptors ein Local Interface existiert
	
	public int bucheGeht(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station);

	public int bucheKommt(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station);

	public int bucheLosAblieferung(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr,
			@WebParam(name = "iMenge") Integer menge,
			@WebParam(name = "sAusweis") String cAusweis);

	public int bucheLosAblieferungSeriennummer(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr,
			@WebParam(name = "sArtikelnummer") String artikelCNr,
			@WebParam(name = "sSeriennummer") String cSeriennummer,
			@WebParam(name = "sVersion") String cVersion);

	public int bucheVersionsAenderungSeriennummer(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr,
			@WebParam(name = "sSeriennummer") String cSeriennummer,
			@WebParam(name = "sVersion") String cVersion);

	public int bucheLosTaetigkeitFertig(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr,
			@WebParam(name = "sTaetigkeitnummer") String taetigkeitCNr);

	public int buchePause(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station);

	public int bucheTaetigkeitFertig(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "iLossollarbeitsplanIId") Integer lossollarbeitsplanIId);

	public int bucheZeit(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sBuchungart") String buchungsart,
			@WebParam(name = "sStation") String station);

	public int bucheZeitAuftrag(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "iAuftragID") Integer auftragIId,
			@WebParam(name = "iArtikelID") Integer artikelIId) 
	throws EJBExceptionLP, RemoteException;

	public int bucheZeitBarcode(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sBarcodeListe") String sBarcodeListe);
	
	public int bucheZeitMitStueckmeldung(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sBarcodeListe") String sBarcodeListe,
			@WebParam(name = "sStueckmeldungListe") String sStueckmeldungListe);
	
	public int bucheZeitRelativ(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sBuchungart") String buchungsart,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "nAuftragId") Integer auftragIId,
			@WebParam(name = "nArtikelId") Integer artikelIId,
			@WebParam(name = "iDauerStunden") int dauerStunden,
			@WebParam(name = "iDauerMinuten") int dauerMinuten);
	
	public int bucheZeitRelativAuftrag(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sAuftragNr") String auftragCNr,
			@WebParam(name = "iDauerStunden") int dauerStunden,
			@WebParam(name = "iDauerMinuten") int dauerMinuten);

	public int bucheZeitRelativAuftragB(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sAuftragNr") String auftragCNr,
			@WebParam(name = "iDauerStunden") int dauerStunden,
			@WebParam(name = "iDauerMinuten") int dauerMinuten,
			@WebParam(name = "sBemerkung") String bemerkung,
			@WebParam(name = "sKommentar") String kommentar);

	public String findByCAusweis(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser);

	public String findByIPersonalnummer(
			@WebParam(name = "iPersonalnummer") Integer iPersonalnummer,
			@WebParam(name = "sIdUser") String idUser);
	
	public String getAnwesend(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser);
	
	public String getAuftragTaetigkeiten(
			@WebParam(name = "idUser") String idUserI,
			@WebParam(name="sMandant") String sMandantI,
			@WebParam(name="nAuftragId") Integer nAuftragIdI,
			@WebParam(name="nPersonalId") Integer nPersonalIdI);

	public String getAuftragTaetigkeitenDetail(
			@WebParam(name = "idUser") String idUserI,
			@WebParam(name = "sMandant") String sMandantI,
			@WebParam(name = "nAuftragId") Integer nAuftragIdI,
			@WebParam(name = "nPersonalId") Integer nPersonalIdI);

	public String getAuftragTaetigkeitenPersonal(
			@WebParam(name = "idUser") String idUserI,
			@WebParam(name="sMandant") String sMandantI,
			@WebParam(name="nAuftragId") Integer nAuftragIdI,
			@WebParam(name="nPersonalId") Integer nPersonalIdI,
			@WebParam(name="sAusweis") String cAusweis);

	public String getBuchungListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "nBuchungId") Integer nBuchungId,
			@WebParam(name = "sDatumVon") String sDatumVon,
			@WebParam(name = "sDatumBis") String sDatumBis) 
	throws EJBExceptionLP, RemoteException;
	
	public String getKundenAuftragListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String sMandant,
			@WebParam(name = "nPartneID") Integer nPartnerId);
	
	public String getKundenAuftragListePersonal(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String sMandant,
			@WebParam(name = "nPartneID") Integer nPartnerId,
			@WebParam(name = "sAusweis") String cAusweis);

	public String getLetztesKommt(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser);

	public String getListe(
			@WebParam(name = "idUser") String idUser);

	public String getListeMandant(
			@WebParam(name = "sMandant") String sMandant,
			@WebParam(name = "idUser") String idUser);

	public String getLoseInArbeit(
			@WebParam(name = "sAusweisnummer") String cAusweis,
			@WebParam(name = "sIdUser") String idUser);

	public String getLoseOffenMaschine(
			@WebParam(name = "sMaschine") String cMaschine,
			@WebParam(name = "sIdUser") String idUser);

	public BigDecimal getLosOffeneMenge(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr);

	public int getLosUeberlieferbar(
			@WebParam(name = "sSeriennummerLeser") String cSeriennummerLeser,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr);

	public String getLosZeit(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station,
			@WebParam(name = "sLosnummer") String losCNr,
			@WebParam(name = "sTaetigkeitnummer") String taetigkeitCNr);
	
	public String getMonatsabrechnung(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "iJahr") Integer iJahr,
			@WebParam(name = "iMonat") Integer iMonat,
			@WebParam(name = "sIdUser") String idUser);

	public String getZeitSaldo( 
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station);
	
	public String holeLetztesEnde(
			@WebParam(name = "sAusweis") String cAusweis,
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sStation") String station);

	public boolean istBuchungRuestenUmspannen(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "sBarcodeListe") String sBarcodeListe);

	public int loescheZeitbuchungId(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "iId") int iId);

	public int MessageForAll(
			@WebParam(name = "sMessage") String message);

	public int updateBuchung(
			@WebParam(name = "sIdUser") String idUser,
			@WebParam(name = "nBuchungID") Integer iBuchungID,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag,
			@WebParam(name = "iStunde") int stunde,
			@WebParam(name = "iMinute") int minute,
			@WebParam(name = "iSekunde") int sekunde,
			@WebParam(name = "sTaetigkeit") String sTaetigkeit,
			@WebParam(name = "sBelegart") String belegartnr,
			@WebParam(name = "nBelegartID") Integer iBelegartid,
			@WebParam(name = "nBelegartPositionID") Integer iBelegartpositionid,
			@WebParam(name = "sStation") String station);

}
