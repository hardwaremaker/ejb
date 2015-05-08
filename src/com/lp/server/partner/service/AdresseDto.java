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
package com.lp.server.partner.service;

import java.io.Serializable;

public class AdresseDto implements IAdresse, Serializable {
	private static final long serialVersionUID = 7107526801316372367L;

	private Integer partnerId ;
	private String[] lines ;
	private String salutation ;
	private String title ;
	private String titelSuffix ;
	private String name1Lastname ;
	private String name2Firstname ;
	private String name3Department ;
	private String postofficeBox ;
	private String street ;
	private String countryCode ;
	private String zipcode ;
	private String city ;
	private String email ;
	private String phone ;
	
	@Override
	public Integer getPartnerId() {
		return partnerId ;
	}

	@Override
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId ;
	}

	@Override
	public String[] getLines() {
		return lines ;
	}

	@Override
	public void setLines(String[] lines) {
		this.lines = lines ;
	}

	@Override
	public String getSalutation() {
		return salutation ;
	}

	@Override
	public void setSalutation(String salutation) {
		this.salutation = salutation ;
	}

	@Override
	public String getTitel() {
		return title ;
	}

	@Override
	public void setTitel(String titel) {
		this.title = titel ;
	}

	@Override
	public String getTitelSuffix() {
		return titelSuffix ;
	}

	@Override
	public void setTitelSuffix(String titelSuffix) {
		this.titelSuffix = titelSuffix ;

	}

	@Override
	public String getName1Lastname() {
		return name1Lastname ;
	}

	@Override
	public void setName1Lastname(String name1Lastname) {
		this.name1Lastname = name1Lastname ;
	}

	@Override
	public String getName2Firstname() {
		return name2Firstname ;
	}

	@Override
	public void setName2Firstname(String name2Firstname) {
		this.name2Firstname = name2Firstname ;
	}

	@Override
	public String getName3Department() {
		return name3Department ;
	}

	@Override
	public void setName3Department(String name3Department) {
		this.name3Department = name3Department ;
	}

	@Override
	public String getPostofficeBox() {
		return postofficeBox ;
	}

	@Override
	public void setPostofficeBox(String postofficeBox) {
		this.postofficeBox = postofficeBox ;
	}

	@Override
	public String getStreet() {
		return street ;
	}

	@Override
	public void setStreet(String street) {
		this.street = street ;
	}

	@Override
	public String getCountryCode() {
		return countryCode ;
	}

	@Override
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode ;

	}

	@Override
	public String getZipcode() {
		return zipcode ;
	}

	@Override
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode ;
	}

	@Override
	public String getCity() {
		return city ;
	}

	@Override
	public void setCity(String city) {
		this.city = city ;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
