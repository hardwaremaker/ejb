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
package com.lp.server.util.report;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.util.Helper;

/**
 * <p>
 * In diesem Dto werden alle Informationen transportiert, die benoetigt werden,
 * um die Summen einer Mwstsatzzeile anzudrucken.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 22.09.05
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */
public class MwstsatzReportDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Die Summe des Betraege der Positionen, die den Mwstsatz haben. */
	private BigDecimal nSummePositionsbetrag = null;

	/** Die Summe der Mwstbetraege der Positionen, die den Mwstsatz haben. */
	private BigDecimal nSummeMwstbetrag = null;

	public MwstsatzReportDto() {
		initialize();
	}

	public void initialize() {
		nSummePositionsbetrag = new BigDecimal(0);
		nSummeMwstbetrag = new BigDecimal(0);
	}

	public BigDecimal getNSummePositionsbetrag() {
		return this.nSummePositionsbetrag;
	}

	public void setNSummePositionsbetrag(BigDecimal nSummePositionsbetrag) {
		this.nSummePositionsbetrag = nSummePositionsbetrag;
	}

	public BigDecimal getNSummeMwstbetrag() {
		return this.nSummeMwstbetrag;
	}

	public void setNSummeMwstbetrag(BigDecimal nSummeMwstbetrag) {
		this.nSummeMwstbetrag = nSummeMwstbetrag;
	}
	
	public BigDecimal getNSummePositionsbetragMinusRabatte(Double dAllgemeinerRabattSatz, Double dProjektRabattSatz){
		BigDecimal bdAllgemeinerRabatt = new BigDecimal(0);
		nSummePositionsbetrag = Helper.rundeKaufmaennisch(nSummePositionsbetrag, 2);
		if(dAllgemeinerRabattSatz!=null){
			bdAllgemeinerRabatt = nSummePositionsbetrag.multiply(new BigDecimal(dAllgemeinerRabattSatz.doubleValue())).movePointLeft(2);
		}
        BigDecimal toReturn =nSummePositionsbetrag.subtract(bdAllgemeinerRabatt);
        BigDecimal bdProjektRabatt = new BigDecimal(0);
        if(dProjektRabattSatz!=null){
        	bdProjektRabatt = toReturn.multiply(new BigDecimal(dProjektRabattSatz.doubleValue())).movePointLeft(2);
        }
        toReturn = toReturn.subtract(bdProjektRabatt);
        return toReturn;
	}
	
	public BigDecimal getNSummeMWSTbetragMinusRabatte(Double dAllgemeinerRabattSatz, Double dProjektRabattSatz, Double fMwstsatz){
		BigDecimal bdSummePosition = getNSummePositionsbetragMinusRabatte(dAllgemeinerRabattSatz, dProjektRabattSatz);
		BigDecimal toReturn = new BigDecimal(0);
		if(fMwstsatz!=null){
			BigDecimal bdMwstSatz = new BigDecimal(fMwstsatz.doubleValue()).movePointLeft(2);
			toReturn = Helper.rundeGeldbetrag(bdSummePosition).multiply(bdMwstSatz); 
			toReturn = Helper.rundeGeldbetrag(toReturn);
		}
		return toReturn;
		
	}
}
