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
 *******************************************************************************/
package com.lp.server.media.ejb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@NamedQueries({
	@NamedQuery(name=MediaStoreBelegQuery.ByMandantBelegIdPositionId, 
			query = "SELECT OBJECT(C) FROM MediaStoreBeleg c WHERE c.mandantCNr = :mandant AND c.belegIId = :belegId AND c.belegpositionIId = :belegpositionId")
})

@Entity
@Table(name="MEDIA_STOREBELEG")
public class MediaStoreBeleg {

	@Id
	@Column(name="I_ID")
	@TableGenerator(name="mediabeleg_id", table="LP_PRIMARYKEY",
			pkColumnName = "C_NAME", pkColumnValue="media_beleg", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="mediabeleg_id")
	private Integer iId ;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
		
	@Column(name="MEDIA_I_ID") 
	private Integer mediaIId ;
	
	@Column(name="C_BELEGARTNR")
	private String cBelegart ;
	
	@Column(name="BELEG_I_ID")
	private Integer belegIId ;
	
	@Column(name="BELEGPOSITION_I_ID")
	private Integer belegpositionIId ;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen ;
	
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_GELESEN")
	private Timestamp tGelesen ;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getMediaIId() {
		return mediaIId;
	}

	public void setMediaIId(Integer mediaIId) {
		this.mediaIId = mediaIId;
	}

	public String getCBelegart() {
		return cBelegart;
	}

	public void setCBelegart(String cBelegart) {
		this.cBelegart = cBelegart;
	}

	public Integer getBelegIId() {
		return belegIId;
	}

	public void setBelegIId(Integer belegIId) {
		this.belegIId = belegIId;
	}

	public Integer getBelegpositionIId() {
		return belegpositionIId;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTGelesen() {
		return tGelesen;
	}

	public void setTGelesen(Timestamp tGelesen) {
		this.tGelesen = tGelesen;
	}	
}
