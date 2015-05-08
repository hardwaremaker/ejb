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
package com.lp.server.system.ejb;

import java.io.Serializable;
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

/**
 * Entity implementation class for Entity: LpDirekthilfe
 *
 */
@NamedQueries( {
	@NamedQuery ( name = LpDirekthilfeQuery.FindAllOfficial, query = "SELECT OBJECT(o) FROM LpDirekthilfe o WHERE o.bAnwender=0"), 
	@NamedQuery ( name = LpDirekthilfeQuery.FindByMandantAndLocale, query = "SELECT OBJECT(o) FROM LpDirekthilfe o WHERE o.mandantCNr=:mandant AND o.localeCNr=:locale AND o.bAnwender=:anwender"),
	@NamedQuery ( name = LpDirekthilfeQuery.FindByTokenMandantAndLocale, query = "SELECT OBJECT(o) FROM LpDirekthilfe o WHERE o.cToken=:token AND o.mandantCNr=:mandant AND o.localeCNr=:locale AND o.bAnwender=:anwender")
})
@Entity
@Table( name = "LP_DIREKTHILFE")
public class LpDirekthilfe implements Serializable {

	private static final long serialVersionUID = -467869231883387875L;

	public LpDirekthilfe() {
	}
	
	public LpDirekthilfe(String token, String text, String mandant,
			String locale, Integer personalIIdAendern, Short bAnwender) {
		this.cToken = token;
		this.cText = text;
		this.mandantCNr = mandant;
		this.localeCNr = locale;
		this.personalIIdAendern = personalIIdAendern;
		this.tAendern = new Timestamp(System.currentTimeMillis());
		this.bAnwender = bAnwender;
	}
	
	@Id
    @Column(name="I_ID")
    @TableGenerator(name="LP_DIREKTHILFE_ID", table="LP_PRIMARYKEY",
            pkColumnName = "C_NAME", pkColumnValue="LP_DIREKTHILFE", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.TABLE, generator="LP_DIREKTHILFE_ID")
    private Integer iId ;
	
	@Column(name="C_TOKEN")
	private String cToken;
	
	@Column(name="C_TEXT")
	private String cText;
	
	@Column(name="LOCALE_C_NR")
	private String localeCNr;
	
	@Column(name="MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name="T_AENDERN")
	private Timestamp tAendern;
	
	@Column(name="PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name="B_ANWENDER")
	private Short bAnwender;
	
	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public String getcToken() {
		return cToken;
	}

	public void setcToken(String cToken) {
		this.cToken = cToken;
	}

	public String getcText() {
		return cText;
	}

	public void setcText(String cText) {
		this.cText = cText;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Timestamp gettAendern() {
		return tAendern;
	}

	public void settAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	
	public Short getbAnwender() {
		return bAnwender;
	}
	
	public void setbAnwender(Short bAnwender) {
		this.bAnwender = bAnwender;
	}
   
}
