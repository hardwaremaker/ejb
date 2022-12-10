
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

import java.math.BigDecimal;
import java.util.HashMap;

public class ReportMaterialbedarfsvorschauDto {
	private HashMap<Integer,ReportMaterialbedarfsvorschauDto> kinder = new HashMap<Integer,ReportMaterialbedarfsvorschauDto>();
	private BigDecimal mengeVergangenheit = new BigDecimal(0);
	private BigDecimal mengeAngelegteAuftraege = new BigDecimal(0);
	private BigDecimal mengeOffeneRahmen = new BigDecimal(0);
	private BigDecimal mengeOffeneAngebote = new BigDecimal(0);
	
	private HashMap<Integer,BigDecimal> artikelmengenVergangenheit=new HashMap();
	public HashMap<Integer, BigDecimal> getArtikelmengenVergangenheit() {
		return artikelmengenVergangenheit;
	}
	private HashMap<Integer,BigDecimal> artikelmengenAngelegteAuftraege=new HashMap();
	public HashMap<Integer, BigDecimal> getArtikelmengenAngelegteAuftraege() {
		return artikelmengenAngelegteAuftraege;
	}
	public HashMap<Integer, BigDecimal> getArtikelmengenOffeneRahmen() {
		return artikelmengenOffeneRahmen;
	}
	public HashMap<Integer, BigDecimal> getArtikelmengenOffeneAngebote() {
		return artikelmengenOffeneAngebote;
	}
	private HashMap<Integer,BigDecimal> artikelmengenOffeneRahmen=new HashMap();
	private HashMap<Integer,BigDecimal> artikelmengenOffeneAngebote=new HashMap();
	
	public BigDecimal getMengeAngelegteAuftraege() {
		return mengeAngelegteAuftraege;
	}
	public BigDecimal getMengeOffeneRahmen() {
		return mengeOffeneRahmen;
	}
	public BigDecimal getMengeOffeneAngebote() {
		return mengeOffeneAngebote;
	}
	public HashMap<Integer,ReportMaterialbedarfsvorschauDto> getSubGruppen() {
		return kinder;
	}
	public void setSubGruppen(HashMap<Integer,ReportMaterialbedarfsvorschauDto> vatergruppe) {
		this.kinder = vatergruppe;
	}
	
	
	public void add2MengeVergangenheit(Integer artikelIId,BigDecimal menge) {
		this.mengeVergangenheit = this.mengeVergangenheit.add(menge);
		
		if(artikelmengenVergangenheit.containsKey(artikelIId)){
			BigDecimal bdMengeVorhanden=artikelmengenVergangenheit.get(artikelIId);
			bdMengeVorhanden=bdMengeVorhanden.add(menge);
			artikelmengenVergangenheit.put(artikelIId, bdMengeVorhanden);
			
		} else {
			artikelmengenVergangenheit.put(artikelIId, menge);
		}
		
		
	}
	
	public void add2MengeAngelegteAuftraege(Integer artikelIId,BigDecimal menge) {
		this.mengeAngelegteAuftraege = this.mengeAngelegteAuftraege.add(menge);
		
		if(artikelmengenAngelegteAuftraege.containsKey(artikelIId)){
			BigDecimal bdMengeVorhanden=artikelmengenAngelegteAuftraege.get(artikelIId);
			bdMengeVorhanden=bdMengeVorhanden.add(menge);
			artikelmengenAngelegteAuftraege.put(artikelIId, bdMengeVorhanden);
			
		} else {
			artikelmengenAngelegteAuftraege.put(artikelIId, menge);
		}
		
	}
	
	public void add2MengeOffeneRahmen(Integer artikelIId,BigDecimal menge) {
		this.mengeOffeneRahmen = this.mengeOffeneRahmen.add(menge);
		
		if(artikelmengenOffeneRahmen.containsKey(artikelIId)){
			BigDecimal bdMengeVorhanden=artikelmengenOffeneRahmen.get(artikelIId);
			bdMengeVorhanden=bdMengeVorhanden.add(menge);
			artikelmengenOffeneRahmen.put(artikelIId, bdMengeVorhanden);
			
		} else {
			artikelmengenOffeneRahmen.put(artikelIId, menge);
		}
		
		
	}
	
	public void add2MengeOffeneAngebote(Integer artikelIId,BigDecimal menge) {
		this.mengeOffeneAngebote = this.mengeOffeneAngebote.add(menge);
		
		if(artikelmengenOffeneAngebote.containsKey(artikelIId)){
			BigDecimal bdMengeVorhanden=artikelmengenOffeneAngebote.get(artikelIId);
			bdMengeVorhanden=bdMengeVorhanden.add(menge);
			artikelmengenOffeneAngebote.put(artikelIId, bdMengeVorhanden);
			
		} else {
			artikelmengenOffeneAngebote.put(artikelIId, menge);
		}
		
	}
	public BigDecimal getMengeVergangenheit() {
		return mengeVergangenheit;
	}
	
	
	
}
