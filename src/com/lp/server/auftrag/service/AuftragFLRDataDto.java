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
package com.lp.server.auftrag.service;

import java.io.Serializable;

import com.lp.server.partner.service.IAddressContact;

public class AuftragFLRDataDto implements IAuftragFLRData, Serializable {
	private static final long serialVersionUID = -7248146995274081239L;

	private Boolean internerKommentar ;
	private Boolean externerKommentar ;
	private IAddressContact addressContact ;
	private String externerKommentarText;
	private String internerKommentarText;
	private Integer kundeIIdAuftragsadresse;
	private Integer kundeIIdRechnungsadresse;
	private Integer kundeIIdLieferadresse;
	private String vertreterKurzzeichen;
	private String statusCnr;
	
	@Override
	public Boolean hasInternerKommentar() {
		return internerKommentar ;
	}

	@Override
	public void setInternerKommentar(Boolean value) {
		internerKommentar = value ;
	}

	@Override
	public Boolean hasExternerKommentar() {
		return externerKommentar ;
	}

	@Override
	public void setExternerKommentar(Boolean value) {
		externerKommentar = value ;
	}

	@Override
	public IAddressContact getAddressContact() {
		return addressContact ;
	}

	@Override
	public void setAddressContact(IAddressContact addressContact) {
		this.addressContact = addressContact ;
	}

	public String getExternerKommentarText() {
		return externerKommentarText;
	}

	public void setExternerKommentarText(String externerKommentarText) {
		this.externerKommentarText = externerKommentarText;
	}

	public String getInternerKommentarText() {
		return internerKommentarText;
	}

	public void setInternerKommentarText(String internerKommentarText) {
		this.internerKommentarText = internerKommentarText;
	}

	@Override
	public Integer getKundeIIdAuftragsadresse() {
		return kundeIIdAuftragsadresse;
	}

	@Override
	public void setKundeIIdAuftragsadresse(Integer kundeIId) {
		this.kundeIIdAuftragsadresse = kundeIId;
	}

	@Override
	public Integer getKundeIIdRechnungsadresse() {
		return kundeIIdRechnungsadresse;
	}

	@Override
	public void setKundeIIdRechnungsadresse(Integer kundeIId) {
		this.kundeIIdRechnungsadresse = kundeIId;
	}

	@Override
	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	@Override
	public void setKundeIIdLieferadresse(Integer kundeIId) {
		this.kundeIIdLieferadresse = kundeIId;
	}
	
	@Override
	public String getVertreterKurzzeichen() {
		return vertreterKurzzeichen;
	}
	
	@Override
	public void setVertreterKurzzeichen(String vertreterKurzzeichen) {
		this.vertreterKurzzeichen = vertreterKurzzeichen;
	}

	@Override
	public String getStatusCnr() {
		return statusCnr;
	}

	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}
}
