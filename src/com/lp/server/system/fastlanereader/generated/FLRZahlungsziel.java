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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRZahlungsziel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer id;

	/** persistent field */
	private String mandant_c_nr;

	/** persistent field */
	private String c_bez;

	/** persistent field */
	private Integer anzahlzieltagefuernetto;

	/** persistent field */
	private Integer skontoprozentsatz1;

	/** persistent field */
	private Integer anzahltage1;

	/** persistent field */
	private Integer skontoprozentsatz2;

	/** persistent field */
	private Integer anzahltage2;

	/** nullable persistent field */
	private Short b_versteckt;

	/** persistent field */
	private Set<?> zahlungsziel_zahlungsziel_set;

	/** full constructor */
	public FLRZahlungsziel(String mandant_c_nr, String c_bez,
			Integer anzahlzieltagefuernetto, Integer skontoprozentsatz1,
			Integer anzahltage1, Integer skontoprozentsatz2,
			Integer anzahltage2, Short b_versteckt,
			Set<?> zahlungsziel_zahlungsziel_set) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_bez = c_bez;
		this.anzahlzieltagefuernetto = anzahlzieltagefuernetto;
		this.skontoprozentsatz1 = skontoprozentsatz1;
		this.anzahltage1 = anzahltage1;
		this.skontoprozentsatz2 = skontoprozentsatz2;
		this.anzahltage2 = anzahltage2;
		this.b_versteckt = b_versteckt;
		this.zahlungsziel_zahlungsziel_set = zahlungsziel_zahlungsziel_set;
	}

	/** default constructor */
	public FLRZahlungsziel() {
	}

	/** minimal constructor */
	public FLRZahlungsziel(String mandant_c_nr, String c_bez,
			Integer anzahlzieltagefuernetto, Integer skontoprozentsatz1,
			Integer anzahltage1, Integer skontoprozentsatz2,
			Integer anzahltage2, Set<?> zahlungsziel_zahlungsziel_set) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_bez = c_bez;
		this.anzahlzieltagefuernetto = anzahlzieltagefuernetto;
		this.skontoprozentsatz1 = skontoprozentsatz1;
		this.anzahltage1 = anzahltage1;
		this.skontoprozentsatz2 = skontoprozentsatz2;
		this.anzahltage2 = anzahltage2;
		this.zahlungsziel_zahlungsziel_set = zahlungsziel_zahlungsziel_set;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public Integer getAnzahlzieltagefuernetto() {
		return this.anzahlzieltagefuernetto;
	}

	public void setAnzahlzieltagefuernetto(Integer anzahlzieltagefuernetto) {
		this.anzahlzieltagefuernetto = anzahlzieltagefuernetto;
	}

	public Integer getSkontoprozentsatz1() {
		return this.skontoprozentsatz1;
	}

	public void setSkontoprozentsatz1(Integer skontoprozentsatz1) {
		this.skontoprozentsatz1 = skontoprozentsatz1;
	}

	public Integer getAnzahltage1() {
		return this.anzahltage1;
	}

	public void setAnzahltage1(Integer anzahltage1) {
		this.anzahltage1 = anzahltage1;
	}

	public Integer getSkontoprozentsatz2() {
		return this.skontoprozentsatz2;
	}

	public void setSkontoprozentsatz2(Integer skontoprozentsatz2) {
		this.skontoprozentsatz2 = skontoprozentsatz2;
	}

	public Integer getAnzahltage2() {
		return this.anzahltage2;
	}

	public void setAnzahltage2(Integer anzahltage2) {
		this.anzahltage2 = anzahltage2;
	}

	public Short getB_versteckt() {
		return this.b_versteckt;
	}

	public void setB_versteckt(Short b_versteckt) {
		this.b_versteckt = b_versteckt;
	}

	public Set<?> getZahlungsziel_zahlungsziel_set() {
		return this.zahlungsziel_zahlungsziel_set;
	}

	public void setZahlungsziel_zahlungsziel_set(
			Set<?> zahlungsziel_zahlungsziel_set) {
		this.zahlungsziel_zahlungsziel_set = zahlungsziel_zahlungsziel_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
