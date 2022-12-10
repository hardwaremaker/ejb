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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import com.lp.server.util.report.*;

public class MonatsabrechnungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JasperPrintLP jasperPrint = null;
//	private String sEinheitVerfuegbarerUrlaub = "";
//	private BigDecimal nVerfuegbarerurlaub = new BigDecimal(0);
//	private BigDecimal nSaldo = new BigDecimal(0);
	private Object[][] data;
    private HashMap parameter = new HashMap();
    private ZeitsaldoDto zeitsaldoDto;
    
    private BigDecimal gleitzeitsaldoAbrechnungsmonatOhneVormonat;

	public BigDecimal getGleitzeitsaldoAbrechnungsmonatOhneVormonat() {
		return gleitzeitsaldoAbrechnungsmonatOhneVormonat;
	}

	public void setGleitzeitsaldoAbrechnungsmonatOhneVormonat(BigDecimal gleitzeitsaldoAbrechnungsmonatOhneVormonat) {
		this.gleitzeitsaldoAbrechnungsmonatOhneVormonat = gleitzeitsaldoAbrechnungsmonatOhneVormonat;
	}

	public MonatsabrechnungDto() {
		setZeitsaldoDto(new ZeitsaldoDto());
		setNVerfuegbarerurlaub(new BigDecimal(0));
		setnSaldo(new BigDecimal(0));
		setsEinheitVerfuegbarerUrlaub("");
	}

	public String getsEinheitVerfuegbarerUrlaub() {
		return getZeitsaldoDto().getEinheitVerfuegbarerUrlaub();
	}

	public void setsEinheitVerfuegbarerUrlaub(String sEinheitVerfuegbarerUrlaub) {
		getZeitsaldoDto().setEinheitVerfuegbarerUrlaub(sEinheitVerfuegbarerUrlaub);
	}

	public BigDecimal getnSaldo() {
		return getZeitsaldoDto().getNSaldo();
	}

	public void setnSaldo(BigDecimal nSaldo) {
		getZeitsaldoDto().setNSaldo(nSaldo);
	}
    
	public HashMap getParameter() {
		return parameter;
	}

	public void setParameter(HashMap parameter) {
		this.parameter = parameter;
	}

	public void setJasperPrint(JasperPrintLP jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public void setNVerfuegbarerurlaub(BigDecimal nVerfuegbarerurlaub) {
		getZeitsaldoDto().setNVerfuegbarerurlaub(nVerfuegbarerurlaub);
	}

	public JasperPrintLP getJasperPrint() {
		return jasperPrint;
	}

	public BigDecimal getNVerfuegbarerurlaub() {
//		if(nVerfuegbarerurlaub != null){
//			return nVerfuegbarerurlaub;
//		} else {
//			return new BigDecimal(0);
//		}
		return getZeitsaldoDto().getNVerfuegbarerurlaub() != null ? 
				getZeitsaldoDto().getNVerfuegbarerurlaub() : new BigDecimal(0);
	}
	
	  public void setData(Object[][] data) {
		    this.data = data;
		  }
	  public Object[][] getData() {
		    return data;
		  }

	public ZeitsaldoDto getZeitsaldoDto() {
		return zeitsaldoDto;
	}

	public void setZeitsaldoDto(ZeitsaldoDto zeitsaldoDto) {
		this.zeitsaldoDto = zeitsaldoDto;
	}

	
}
