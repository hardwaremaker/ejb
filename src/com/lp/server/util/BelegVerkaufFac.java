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
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Local;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
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


	public BigDecimal berechneGesamtwertBelegProKundeProZeitintervall(
			Integer	iIdKundeI, 
			Date datVonI, 
			Date datBisI, 
			String belegartCNr);
	
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
			 ,Locale locDruck,
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
	Set<Integer> prepareIntZwsPositions(BelegpositionVerkaufDto[] positionDtos) ;	
	
	BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BigDecimal zwsRabatt, 
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto) ;

	BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, BelegpositionVerkaufDto[] allePositionenDto, Set<Integer> modifiedPositions) ; 
}
