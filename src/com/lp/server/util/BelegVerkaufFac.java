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
package com.lp.server.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Local;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;


@Local
public interface BelegVerkaufFac {

	public BigDecimal getNettoGesamtwertinBelegwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto);

	public BigDecimal getNettoGesamtwertinBelegwaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto,TheClientDto theClientDto); 
	
	public BigDecimal getGesamtwertinBelegwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto);
	
	public BigDecimal getGesamtwertinMandantwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,	BelegVerkaufDto belegVerkaufDto);
	
	public BigDecimal getGesamtwertInBelegswaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto,TheClientDto theClientDto);
	
	public BigDecimal getGesamtwertInMandantwaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto,TheClientDto theClientDto);
	
	public BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto);
	
	public BelegpositionVerkaufDto getBelegpositionVerkaufReport(
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto,Integer iNachkommastellenPreis);

	
	public BigDecimal getWertPoisitionsartPosition(Integer iIdPositionI, String belegartCNr,TheClientDto theClientDto);
	    
	public BelegpositionVerkaufDto berechnePauschalposition(
			  BelegpositionVerkaufDto belegpositionVerkaufDto,
			  BelegVerkaufDto belegVerkaufDto,
			  BigDecimal neuWert,
			  BigDecimal altWert
	  );
	 
	 public Object[] getMwstTabelle(
			 LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			 BelegVerkaufDto belegVerkaufDto
			 ,Locale locDruck,String waehrungCNrZusaetzlich,
			 TheClientDto theClientDto);
	 
	 /**
	  * Aus der Liste der eineindeutigen Seriennummern einige dieser Position zuordnen
	  * 
	  * @param rePosDto ist die Belegposition, der Seriennummern zugeordnet werden sollen, sofern es 
	  * sich um eine seriennummerntragende Position handelt
	  * @param notyetUsedIdentities die Liste der noch verf&uuml;gbaren Seriennummern (mit deren Menge). 
	  * Achtung: Diese Liste wird modifiziert, da diejenigen Seriennummern "entnommen" werden die 
	  * zugeordnet werden konnten
	  * @param theClientDto
	  */
	 public void setupPositionWithIdentities(BelegpositionDto rePosDto,  
			 List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto) ;
	 
	 public void setupPositionWithIdentities(boolean zubuchen, BelegpositionDto rePosDto,  
				List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto)  ;	 
	 

	/**
	 * Sofern vorhanden die Zwischensummenpositionsdaten entsprechend der zu loeschenden Position anpasssen
	 * 
	 * @param belegDto Der Beleg(Auftrag/Rechnung/..) in dem die Position vorhanden ist
	 * @param belegPositionDto Die zu loeschende Position
	 * @param belegPositionDtos Alle zum Beleg gehoerenden Positionen
	 * @throws EJBExceptionLP
	 */
	public void processIntelligenteZwischensummeRemove(BelegVerkaufDto belegDto, 
			BelegpositionVerkaufDto belegPositionDto, BelegpositionVerkaufDto[] belegPositionDtos) throws EJBExceptionLP ;
	
	/**
	 * Auf gleichen Mehrwertsteuersatz f&uuml;r die Positionen (inklusive) der angegeben Positionsnummern pr&uuml;fen
	 * 
	 * @param belegPositionDtos die (alle) Belegpositionen 
	 * @param vonPositionNumber die Positionsnummer (der nummerierten Positionen) ab der der MwstSatz zu pruefen ist
	 * @param bisPositionNumber die Positionsnummer (der nummerierten Positionen) bis zu der der MwstSatz zu pruefen ist
	 * @return true wenn fuer alle Belegpositionen zwischen von und bis der gleiche Satz definiert ist
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(
			BelegpositionVerkaufDto[] belegPositionDtos, Integer vonPositionNumber, Integer bisPositionNumber) throws EJBExceptionLP ;
		 
	/**
	 * Tr&auml;gt in die Zwischensummenpositionen den Rabattbetrag aus der Zwischensummenposition ein.</br>
	 * <p>Achtung: Irgendwo vorher wird ein R&uuml;cksetzen der Betr&auml;ge notwendig!</p>
	 * 
	 * @param positionDtos
	 */
	Set<Integer> adaptIntZwsPositions(BelegpositionVerkaufDto[] positionDtos) ;
	

	/**
	 * Die Zwischensummenpositionen f&uuml;r eine erneute Berechnung vorbereiten.
	 * 
	 * @param positionDtos
	 */
	Set<Integer> prepareIntZwsPositions(BelegpositionVerkaufDto[] positionDtos, BelegVerkaufDto belegVerkaufDto) ;	
	
	BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BigDecimal zwsRabatt, 
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto) ;

	BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, BelegpositionVerkaufDto[] allePositionenDto, Set<Integer> modifiedPositions) ;

	<T extends IBelegVerkaufEntity> void saveIntZwsPositions(BelegpositionVerkaufDto[] dtos, Set<Integer> modifiedPosIndex, Class<T> entityClass);

//	LinkedHashMap<Integer, MwstsatzReportDto> getMwstSumme(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
//			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto);
	
	/**
	 * Ermittlung des Steuerbetrags<br>
	 * <p>Es werden der Allgemeine Rabatt und der Projektierungsrabatt
	 * ber&uuml;cksichtigt.</p>
	 * 
	 * @param belegpositionVerkaufDtos
	 * @param belegVerkaufDto
	 * @param iNachkommastellenPreis
	 * @param theClientDto
	 * @return Die jeweils kummulierten Steuerbetr&auml;ge eines Satzes
	 */
	BelegSteuerDto getNettoGesamtWertUSTInfo(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis,
			TheClientDto theClientDto);

//	void preiseEinesArtikelsetsUpdaten(Collection<IBelegVerkaufEntity> setPositions, BigDecimal kopfMenge,
//			BigDecimal kopfNetto, KundeDto kundeDto, MwstsatzbezId bezId, String waehrungCnr, TheClientDto theClientDto)
//			throws RemoteException;

	void preiseEinesArtikelsetsUpdaten(Collection<IBelegVerkaufEntity> setPositions, BigDecimal kopfMenge,
			BigDecimal kopfNetto, KundeDto kundeDto, MwstsatzbezId bezId, String waehrungCnr,
			IArtikelsetPreisUpdate setPreisUpdate, TheClientDto theClientDto) throws RemoteException;

	BigDecimal getBdBruttoeinzelpreis(BelegpositionVerkaufDto belegpositionVerkaufDto, Integer iNachkommastellenPreis,
			TheClientDto theClientDto);

	void setupChangePositionWithIdentities(boolean zubuchen, BelegpositionDto posDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto);


	/**
	 * Sind alle mengenbehafteten Positionen Teil einer Zwischensumme?</br>
	 * 
	 * <p>Beim Angebotsdruck mit aktivierter Zusammenfassung geht der Druck davon
	 * aus, dass alle mengenbehafteten Positionen in Zwischensummen auf 
	 * der ersten Ebene enthalten sind. Dies wird hier &uuml;berpr&uuml;ft.</p>
	 * 
	 * <p>Implementierungsdetail: Es gibt keine Ueberpruefung der Ebene, weil 
	 * eine Belegposition einer inneren Zwischensumme automatisch in der 
	 * aeusseren Zwischensumme enthalten sein muss. Das koennte nur sein, 
	 * wenn die Zwischensumme unvollstaendig definiert ist. Dies wird erkannt,
	 * weil dann die Belegpositionsnummer des von/bis Index nicht ermittelt
	 * werden kann.</p>
	 * 
	 * @param positionDtos
	 * @return Ein (leeres) Set von Positions-IId die nicht in einer 
	 *   Zwischensumme enthalten sind.
	 */
	Set<Integer> calculatePositionsNotInZws(BelegpositionVerkaufDto[] positionDtos);
	
	ArrayList<BelegpositionVerkaufDto> getIntZwsPositions(BelegpositionVerkaufDto belegpositionVerkaufDtoZWS,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos);

	/**
	 * Die Belegposition neu berechnen</br>
	 * <p>Es wird auch der in der position vorhandene Mwstsatz auf Basis
	 * des Belegdatums neu bestimmt</p>
	 * 
	 * @param posDto
	 * @param kundeId
	 * @param belegDatum
	 * @param theClientDto
	 * @return
	 */
	BelegpositionVerkaufDto berechneNeu(BelegpositionVerkaufDto posDto, KundeId kundeId, Timestamp belegDatum,
			TheClientDto theClientDto);
	
	BelegpositionVerkaufDto erstelleVerpackungskostenpositionAnhandNettowert(Integer kundeIIdRechnungsadresse,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto,BelegpositionVerkaufDto positionFuerRueckgabe, TheClientDto theClientDto);


	/**
	 * Pr&uuml;ft und wirft im Falle von ungleichen Mwsts&auml;tzen eine EJBException</br>
	 * Alle Mengen/Artikelpositionen auf gleichen MwstsatzId in Zwischensumme pr&uuml;fen</br>
	 * 
	 * <p>Gibt es in der Zwischensumme die angegebene Positionsnummer nicht, wird eine
	 * Exception geliefert.</p>
	 * 
	 * <p>Fuer alle Zwischensummenpositionen die sich in positionDtos befinden,
	 *  wird ueberpr&uuml;ft, ob die Positionen die sich innerhald der von der Zwsposition
	 *  angegebenen "VonPosId" und "BisPosId" befinden die gleichen MwstsatzId haben 
	 *  und die identisch mit der MwstsatzId ist, die in der Zwsposition enthalten ist.
	 *  Falls nicht, wird die erste abweichende Zwspos-Id (die Id der Zwsposition) 
	 *  zur&uuml;ckgeliefert.
	 *  </p> 
	 * 
	 * @param dtos  positionDtos die (und nur die) Positionen die eine Menge und/oder eine Artikelposition
	 *   enthalten. In ISort aufsteigender Reihenfolge
	 */
	void validiereZwsAufGleichenMwstSatzThrow(BelegpositionVerkaufDto[] dtos);
}
