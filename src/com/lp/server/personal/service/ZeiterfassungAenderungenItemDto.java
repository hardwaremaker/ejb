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
import java.sql.Timestamp;

import com.lp.server.partner.service.IdValueDto;

public class ZeiterfassungAenderungenItemDto implements Serializable {

	private static final long serialVersionUID = 6962648740958124770L;

	private Integer entityId;
	private String operation;
	private IdValueDto personAendern;
	private Timestamp zeit;
	private Timestamp zeitAendern;
	private String woWurdeGebucht;
	private IdValueDto taetigkeit;
	private String belegart;
	private IdValueDto beleg;
	private IdValueDto belegPosition;
	private String artikelCnr;
	private String artikelCBez;
	
	private Boolean istBeleg;
	
	public ZeiterfassungAenderungenItemDto() {
	}
	
	public ZeiterfassungAenderungenItemDto(ZeiterfassungAenderungenItemDto copy) {
		this.entityId = copy.entityId;
		this.operation = copy.operation;
		this.personAendern = copy.personAendern;
		this.zeit = copy.zeit;
		this.zeitAendern = copy.zeitAendern;
		this.woWurdeGebucht = copy.woWurdeGebucht;
		this.taetigkeit = copy.taetigkeit;
		this.belegart = copy.belegart;
		this.beleg = copy.beleg;
		this.belegPosition = copy.belegPosition;
		this.artikelCnr = copy.artikelCnr;
		this.artikelCBez = copy.artikelCBez;
		this.istBeleg = copy.istBeleg;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public IdValueDto getPersonAendern() {
		return personAendern;
	}

	public void setPersonAendern(IdValueDto personAendern) {
		this.personAendern = personAendern;
	}

	public Timestamp getZeit() {
		return zeit;
	}

	public void setZeit(Timestamp zeit) {
		this.zeit = zeit;
	}

	public Timestamp getZeitAendern() {
		return zeitAendern;
	}

	public void setZeitAendern(Timestamp zeitAendern) {
		this.zeitAendern = zeitAendern;
	}

	public String getWoWurdeGebucht() {
		return woWurdeGebucht;
	}

	public void setWoWurdeGebucht(String woWurdeGebucht) {
		this.woWurdeGebucht = woWurdeGebucht;
	}

	public IdValueDto getTaetigkeit() {
		return taetigkeit;
	}

	public void setTaetigkeit(IdValueDto taetigkeit) {
		this.taetigkeit = taetigkeit;
	}

	public String getBelegart() {
		return belegart;
	}

	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}

	public IdValueDto getBeleg() {
		return beleg;
	}

	public void setBeleg(IdValueDto beleg) {
		this.beleg = beleg;
	}

	public IdValueDto getBelegPosition() {
		return belegPosition;
	}

	public void setBelegPosition(IdValueDto belegPosition) {
		this.belegPosition = belegPosition;
	}

	public String getArtikelCnr() {
		return artikelCnr;
	}

	public void setArtikelCnr(String artikelCnr) {
		this.artikelCnr = artikelCnr;
	}
	
	public String getArtikelBez() {
		return this.artikelCBez;
	}
	
	public void setArtikelBez(String artikelBez) {
		this.artikelCBez = artikelBez;
	}

	public Boolean istBeleg() {
		return istBeleg;
	}
	
	public void setIstBeleg(Boolean istBeleg) {
		this.istBeleg = istBeleg;
	}
	
	/**
	 * Pr&uuml;ft, ob alle Pflichtfelder dieses Items gesetzt sind. Bei Belegen
	 * m&uuml;ssen zus&auml;tzlich die belegspezifischen Parameter gesetzt sein.
	 * 
	 * @return true, wenn alle Pflichtfelder gesetzt sind
	 */
	public Boolean sindPflichtfelderGesetzt() {
		if(istBeleg != null) {
			if(zeit == null || woWurdeGebucht == null)
				return false;
			
			if(istBeleg) {
				return beleg != null && belegart != null && 
						belegPosition != null && artikelCnr != null ? true : false;
			}
			return taetigkeit != null ? true : false;
		}
		
		return false;
	}
}
