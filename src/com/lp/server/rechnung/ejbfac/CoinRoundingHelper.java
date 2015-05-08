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
package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class CoinRoundingHelper {
	private RechnungFac rechnungFac ;
	private KundeFac kundeFac ;
	private ParameterFac parameterFac ;
	private ArtikelFac artikelFac ;
	private MandantFac mandantFac ;
		
	public CoinRoundingHelper(RechnungFac rechnungFac, KundeFac kundeFac, ArtikelFac artikelFac, ParameterFac parameterFac, MandantFac mandantFac) {
		this.rechnungFac = rechnungFac ;
		this.kundeFac = kundeFac ;
		this.artikelFac = artikelFac ;
		this.parameterFac = parameterFac ;
		this.mandantFac = mandantFac ;
	}
	
	/**
	 * Stellt fest, ob das Runden des Endbetrags notwendig ist.
	 * 
	 * @param rechnungDto
	 * @param theClientDto
	 * @return true wenn die Rundungsroutine durchgefuehrt werden soll, ansonsten false 
	 */
	public boolean isRoundingNeeded(RechnungDto rechnungDto, TheClientDto theClientDto) {
		return getMuenzRundung(rechnungDto, theClientDto) != null ;
	}
	
	/**
	 * Liefert den Betrag (in Landesw&auml;hrung) auf den gerundet werden soll
	 * 
	 * @param rechnungDto
	 * @param theClientDto
	 * @return null oder Rundungsbetrag in Landesw&auml;hrung. Beispiel 0.05
	 */
	public BigDecimal getRoundingValue(RechnungDto rechnungDto, TheClientDto theClientDto) {
		return getMuenzRundung(rechnungDto, theClientDto);		
	}

	/**
	 * Den Rundungsbetrag ermitteln.</br>
	 * Es wird dabei die Rechnungswaehrung beruecksichtigt, die nicht zwangslaeufig
	 * die Landeswaehrung des Kunden sein muss. -> keine Rundung notwendig
	 * @param rechnungDto
	 * @param theClientDto
	 * @return null wenn es keine Rundung gibt, bzw. kein Betrag hinterlegt ist,
	 *  ansonsten der Betrag
	 */
	protected BigDecimal getMuenzRundung(RechnungDto rechnungDto,
			TheClientDto theClientDto) {
		Integer kundeIId = rechnungDto.getKundeIId() ;
		KundeDto kundeDto = kundeFac.kundeFindByPrimaryKey(kundeIId, theClientDto) ;
//		LandDto landDto = kundeDto.getPartnerDto().getLandplzortDto().getLandDto() ;
		LandplzortDto landplzOrtDto = kundeDto.getPartnerDto().getLandplzortDto() ;
		if(null == landplzOrtDto) return null ;

		LandDto landDto = landplzOrtDto.getLandDto() ;
		if(!rechnungDto.getWaehrungCNr().equals(landDto.getWaehrungCNr())) return null ;
		
		return landDto.getNMuenzRundung() ;
	}
	
	
	public ArtikelDto getItemIIdForRounding(TheClientDto theClientDto) throws RemoteException {
		try {
			ParametermandantDto parameterDto = parameterFac.getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_RUNDUNGSAUSGLEICH_ARTIKEL) ;
			String artikelCnr = parameterDto.getCWert() ;
			return artikelFac.artikelFindByCNrOhneExc(artikelCnr, theClientDto) ;
		} catch(Exception e) {
			return null ;
		}
	}
	
	
	protected RechnungPositionDto findPositionWithArtikelIId(RechnungPositionDto[] dtos, Integer artikelIId) {
		for (RechnungPositionDto rechnungPositionDto : dtos) {
			if(artikelIId.equals(rechnungPositionDto.getArtikelIId())) return rechnungPositionDto ;
		}
		return null ;
	}

	/**
	 * Eine etwaige vorhande Rundungsposition aus der Rechnung entfernen.
	 * @param rePosDto
	 * @param artikelIId
	 * @param theClientDto
	 * @return true wenn eine Rundungsposition gefunden werden konnte
	 * @throws RemoteException
	 */
	public boolean removeRoundingPosition(
			RechnungPositionDto[] rePosDto, Integer artikelIId, TheClientDto theClientDto) throws RemoteException {
		RechnungPositionDto posDto = findPositionWithArtikelIId(rePosDto, artikelIId) ;
		if(posDto != null) {
			rechnungFac.removeRechnungPosition(posDto, theClientDto) ;			
		}
		return posDto != null ;
	}
	
	
	public boolean removeRoundingPosition(RechnungPositionDto[] dtos, TheClientDto theClientDto) throws RemoteException {
		try {
			ArtikelDto artikelDto = getItemIIdForRounding(theClientDto) ;
			if(null == artikelDto) return false ;
			return removeRoundingPosition(dtos, artikelDto.getIId(), theClientDto) ;
		} catch(EJBExceptionLP e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT, "Rundungsartikel nicht definiert");			
		}
	}
	

	protected CoinRounding roundingFactory(BigDecimal roundToValue) {
		CoinRounding rounder = new CoinRounding(roundToValue) ;
		rounder.setRoundUp(false) ;
		return rounder ;
	}

	/**
	 * 
	 * @param rechnungDto
	 * @param positionenDto
	 * @param theClientDto
	 * @throws RemoteException 
	 * @throws EJBExceptionLP FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT sofern eine Rundung notwendig ist,
	 * aber der dafuer vorgesehene Artikel nicht im System definiert ist.
	 */
	public void round(RechnungDto rechnungDto,
			RechnungPositionDto[] positionenDto, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		if(!isRoundingNeeded(rechnungDto, theClientDto)) return ;
		
		CoinRounding coinRounding = roundingFactory(getRoundingValue(rechnungDto, theClientDto)) ;
		BigDecimal brutto = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw()) ;
		BigDecimal bruttoRounded = coinRounding.round(brutto) ;
		BigDecimal diff = bruttoRounded.subtract(brutto) ;
		if(diff.signum() == 0) return ;

		ArtikelDto artikelDto = getItemIIdForRounding(theClientDto) ;
		if(null == artikelDto) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT, "Rundungsartikel nicht definiert");			
		}
		RechnungPositionDto rundungsPositionDto = findPositionWithArtikelIId(positionenDto, artikelDto.getIId()) ;
		if(null == rundungsPositionDto) {
			BelegpositionVerkaufDto belegDto = new BelegpositionVerkaufDto() ;
			// belegDto.setTypCNr(LocaleFac.POSITIONTYP_ALLES) ;
			belegDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT) ;
			belegDto.setArtikelIId(artikelDto.getIId()) ;
			belegDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
			belegDto.setBMwstsatzuebersteuert(Helper.getShortFalse()) ;
			belegDto.setBRabattsatzuebersteuert(Helper.getShortFalse()) ;
			belegDto.setBelegIId(rechnungDto.getIId()) ;
			belegDto.setEinheitCNr(artikelDto.getEinheitCNr()) ;
			MwstsatzDto mwstsatzDto = mandantFac.mwstsatzFindZuDatum(artikelDto.getMwstsatzbezIId(), rechnungDto.getTBelegdatum()) ;
			belegDto.setMwstsatzIId(mwstsatzDto.getIId()) ;
			
			// belegDto.setKostentraegerIId(rechnungDto.getK) ; // vorerst mal nicht setzen, 23.2.2012
			rundungsPositionDto = new RechnungPositionDto(belegDto) ;		
		} else {
			rechnungFac.removeRechnungPosition(rundungsPositionDto, theClientDto) ;
		}
		
		/*
		 * Wir gehen hier vorerst mal davon aus, dass es keine MwSt fuer den Artikel gibt.
		 * Daher ist brutto == netto
		 */
		rundungsPositionDto.setNMenge(new BigDecimal("1")) ;
		rundungsPositionDto.setBNettopreisuebersteuert(Helper.getShortTrue()) ;
		rundungsPositionDto.setNEinzelpreis(diff) ;
		rundungsPositionDto.setNEinzelpreisplusversteckteraufschlag(diff) ;
		rundungsPositionDto.setNNettoeinzelpreis(diff) ;
		rundungsPositionDto.setNBruttoeinzelpreis(diff) ;
		rundungsPositionDto.setFRabattsatz(new Double(0)) ;
		rundungsPositionDto.setFZusatzrabattsatz(new Double(0)) ;
		rechnungFac.createRechnungPosition(rundungsPositionDto, rechnungDto.getLagerIId(), theClientDto) ;

		rechnungDto.setNWertfw(rechnungDto.getNWertfw().add(diff)) ;

		BigDecimal bdWert = rechnungDto.getNWertfw().divide(
				rechnungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
				BigDecimal.ROUND_HALF_EVEN);

		rechnungDto.setNWert(bdWert);
	}
}
