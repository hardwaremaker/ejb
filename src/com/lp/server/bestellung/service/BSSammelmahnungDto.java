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
package com.lp.server.bestellung.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Erlinger; 10.11.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/06/27 10:22:10 $
 */
public class BSSammelmahnungDto {
	private String sBestellnummer = null;
	private String sAngebotsnummer = null;
	public String getSAngebotsnummer() {
		return sAngebotsnummer;
	}

	public void setSAngebotsnummer(String sAngebotsnummer) {
		this.sAngebotsnummer = sAngebotsnummer;
	}

	private String sBestellpositionbezeichung = null;
	private Date dBelegdatum = null;
	private Date dFaelligkeitsdatum = null;
	private Date dABTermin = null;
	private BigDecimal bdOffeneMenge = null;
	private BigDecimal bdWert = null;
	private Integer iMahnstufe = null;
	private String sIdentnummer = null;
	private String sHerstellerIdentnummer = null;
	private String sHerstellbezeichnung = null;
	private String sArtikelhersteller = null;
	private String sReferenznummer = null;
	private String sArtikelbez = null;
	private String sBestellnummern = null;
	private Integer ansprechpartnerIId = null;
	private Integer personalIIdAnforderer = null;
	
	
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPersonalIIdAnforderer() {
		return personalIIdAnforderer;
	}

	public void setPersonalIIdAnforderer(Integer personalIIdAnforderer) {
		this.personalIIdAnforderer = personalIIdAnforderer;
	}

	public String getsBestellnummern() {
		return sBestellnummern;
	}

	public void setsBestellnummern(String sBestellnummern) {
		this.sBestellnummern = sBestellnummern;
	}

	public String getSArtikelbez(){
		return this.sArtikelbez;
	}	
	
	public void setSArtikelbez(String sArtikelbez){
		this.sArtikelbez = sArtikelbez;
	}
	
	public String getSReferenznummer(){
		return this.sReferenznummer;
	}
	
	public void setSReferenznummer(String sReferenznummer){
		this.sReferenznummer = sReferenznummer;
	}
	
	public String getSArtikelhersteller(){
		return this.sArtikelhersteller;
	}
	
	public void setSArtikelhersteller(String sArtikelhersteller){
		this.sArtikelhersteller = sArtikelhersteller;
	}
		
	public String getSHerstellerbezeichnung(){
		return this.sHerstellbezeichnung;
	}
	
	public void setSHerstellerbezeichnung(String sHerstellerbezeichnung){
		this.sHerstellbezeichnung = sHerstellerbezeichnung;
	}
	
	public String getSHerstellerIdentnummer(){
	    return this.sHerstellerIdentnummer;
	}
	
	public void setSHerstellerIdentnummer(String sHerstellerIdentnummer){
		this.sHerstellerIdentnummer = sHerstellerIdentnummer;
	}
	
	public String getSIdentnummer(){
		return this.sIdentnummer;
	}
	
	public void setSIdentnummer(String sIdentnummer){
		this.sIdentnummer = sIdentnummer;
	}
	
	public String getSBestellpositionbezeichung() {
		return sBestellpositionbezeichung;
	}

	public void setSBestellpositionbezeichung(String sBestellpositionbezeichung) {
		this.sBestellpositionbezeichung = sBestellpositionbezeichung;
	}

	public BigDecimal getBdOffen() {
		return bdOffeneMenge;
	}

	public BigDecimal getBdWert() {
		return bdWert;
	}

	public Date getDBelegdatum() {
		return dBelegdatum;
	}

	public Date getDFaelligkeitsdatum() {
		return dFaelligkeitsdatum;
	}
	
	public Date getDABTermin() {
		return dABTermin;
	}

	public Integer getIMahnstufe() {
		return iMahnstufe;
	}

	public String getSRechnungsnummer() {
		return sBestellnummer;
	}

	public void setBdOffen(BigDecimal bdOffen) {
		this.bdOffeneMenge = bdOffen;
	}

	public void setBdWert(BigDecimal bdWert) {
		this.bdWert = bdWert;
	}

	public void setDBelegdatum(Date dBelegdatum) {
		this.dBelegdatum = dBelegdatum;
	}

	public void setIMahnstufe(Integer iMahnstufe) {
		this.iMahnstufe = iMahnstufe;
	}

	public void setDFaelligkeitsdatum(Date dFaelligkeitsdatum) {
		this.dFaelligkeitsdatum = dFaelligkeitsdatum;
	}
	
	public void setDABTermin(Date dABTermin) {
		this.dABTermin = dABTermin;
	}

	public void setSRechnungsnummer(String sRechnungsnummer) {
		this.sBestellnummer = sRechnungsnummer;
	}
}
