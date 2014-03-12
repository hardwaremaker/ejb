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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;


public class LosgutschlechtDto implements Serializable {
	private Integer iId;
	private Integer zeitdatenIId;
	private Integer lossollarbeitsplanIId;
	private BigDecimal nGut;
	private BigDecimal nSchlecht;
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer id) {
		iId = id;
	}
	public Integer getZeitdatenIId() {
		return zeitdatenIId;
	}
	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}
	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}
	public void setZeitdatenIId(Integer zeitdatenIId) {
		this.zeitdatenIId = zeitdatenIId;
	}
	private Integer maschinenzeitdatenIId;
	

	public Integer getMaschinenzeitdatenIId() {
		return maschinenzeitdatenIId;
	}

	public void setMaschinenzeitdatenIId(Integer maschinenzeitdatenIId) {
		this.maschinenzeitdatenIId = maschinenzeitdatenIId;
	}

	
	public BigDecimal getNGut() {
		return nGut;
	}
	public void setNGut(BigDecimal gut) {
		nGut = gut;
	}
	public BigDecimal getNSchlecht() {
		return nSchlecht;
	}
	public void setNSchlecht(BigDecimal schlecht) {
		nSchlecht = schlecht;
	}
	public BigDecimal getNInarbeit() {
		return nInarbeit;
	}
	public void setNInarbeit(BigDecimal inarbeit) {
		nInarbeit = inarbeit;
	}
	private BigDecimal nInarbeit;
	
	private Integer fehlerIId;
	
	private String cKommentar;


	public Integer getFehlerIId() {
		return fehlerIId;
	}

	public void setFehlerIId(Integer fehlerIId) {
		this.fehlerIId = fehlerIId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}
	
}
