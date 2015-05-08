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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class WarenzugangsreferenzDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String belegart = null;
	private String belegnummer = null;
	private String zusatz = null;
	private java.sql.Timestamp tBelegdatum = null;
	private DocPath docPath = null;
	private ArrayList<JCRDocDto> jcrdocs=null;
	private Integer position1 = null;
	private Integer position2 = null;
	private Integer kostenstelleIId = null;
	private Integer i_id_buchung = null;
	private Integer belegartIId = null;
	private Integer belegartpositionIId = null;
	
	public Integer getBelegartpositionIId() {
		return belegartpositionIId;
	}
	public void setBelegartpositionIId(Integer belegartpositionIId) {
		this.belegartpositionIId = belegartpositionIId;
	}
	public Integer getBelegartIId() {
		return belegartIId;
	}
	public void setBelegartIId(Integer belegartIId) {
		this.belegartIId = belegartIId;
	}
	private String person_buchender = null;
	private String kurzzeichen_buchender = null;
	
	
	
	public String getPerson_buchender() {
		return person_buchender;
	}
	public void setPerson_buchender(String person_buchender) {
		this.person_buchender = person_buchender;
	}
	public String getKurzzeichen_buchender() {
		return kurzzeichen_buchender;
	}
	public void setKurzzeichen_buchender(String kurzzeichen_buchender) {
		this.kurzzeichen_buchender = kurzzeichen_buchender;
	}
	public Integer getI_id_buchung() {
		return i_id_buchung;
	}
	public void setI_id_buchung(Integer i_id_buchung) {
		this.i_id_buchung = i_id_buchung;
	}
	private BigDecimal losAZAnteil = null;
	private BigDecimal losMaterialanteil = null;
	
	public BigDecimal getLosAZAnteil() {
		return losAZAnteil;
	}
	public void setLosAZAnteil(BigDecimal losAZAnteil) {
		this.losAZAnteil = losAZAnteil;
	}
	public BigDecimal getLosMaterialanteil() {
		return losMaterialanteil;
	}
	public void setLosMaterialanteil(BigDecimal losMaterialanteil) {
		this.losMaterialanteil = losMaterialanteil;
	}
	private BigDecimal nEinstandspreis = null;
	public BigDecimal getnEinstandspreis() {
		return nEinstandspreis;
	}
	public void setnEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}
	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}
	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}
	public BigDecimal getMenge() {
		return menge;
	}
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	private String land = null;
	private String hersteller = null;
	private BigDecimal menge = null;
	
	
	
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	public String getHersteller() {
		return hersteller;
	}
	public void setHersteller(String hersteller) {
		this.hersteller = hersteller;
	}
	public Integer getPosition1() {
		return position1;
	}
	public void setPosition1(Integer position1) {
		this.position1 = position1;
	}
	public Integer getPosition2() {
		return position2;
	}
	public void setPosition2(Integer position2) {
		this.position2 = position2;
	}
	public ArrayList<JCRDocDto> getJcrdocs() {
		return jcrdocs;
	}
	public void setJcrdocs(ArrayList<JCRDocDto> jcrdocs) {
		this.jcrdocs = jcrdocs;
	}
	public String getBelegart() {
		return belegart;
	}
	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}
	public String getBelegnummer() {
		return belegnummer;
	}
	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}
	public String getZusatz() {
		return zusatz;
	}
	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}
	public java.sql.Timestamp getTBelegdatum() {
		return tBelegdatum;
	}
	public void setTBelegdatum(java.sql.Timestamp belegdatum) {
		tBelegdatum = belegdatum;
	}
	public DocPath getDocPath() {
		return docPath;
	}
	public void setDocPath(DocPath docPath) {
		this.docPath = docPath;
	}
}
