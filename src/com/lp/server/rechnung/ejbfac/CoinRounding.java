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
package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.lp.server.finanz.service.FinanzFac;

public class CoinRounding {

	private BigDecimal roundingValue = new BigDecimal("0.01") ;
	private BigDecimal roundingFactor ;
	
	private boolean roundUp = true ;
	private int moneyScale = FinanzFac.NACHKOMMASTELLEN ;
	
	public CoinRounding() {
		this(new BigDecimal("0.01")) ;
	}
	
	public CoinRounding(BigDecimal minimalCoin) {
		setRoundingValue(minimalCoin) ;
	}
		
	public BigDecimal getRoundingValue() {
		return roundingValue ;
	}

	public void setRoundingValue(BigDecimal newRoundingValue) {
		roundingValue = newRoundingValue ;
		roundingFactor = new BigDecimal("1").divide(newRoundingValue).setScale(moneyScale) ;
	}

	public boolean getRoundUp() {
		return roundUp ;
	}

	/**
	 * "Kaufmaennisch" auf- bzw. abrunden?
	 * @param roundUp true wenn aufgerundet werden soll, ansonsten false
	 */
	public void setRoundUp(boolean roundUp) {
		this.roundUp = roundUp ;
	}
	
	public BigDecimal round(BigDecimal value) {
		return round(value, roundingValue) ;
	}
	
	public BigDecimal round(BigDecimal value, BigDecimal minimalCoin) {
		BigDecimal d = value.multiply(roundingFactor) ;
		d = d.setScale(0, roundUp ? RoundingMode.HALF_UP : RoundingMode.DOWN) ;
		d = d.divide(roundingFactor).setScale(moneyScale) ;
		return d ;
	}
}
