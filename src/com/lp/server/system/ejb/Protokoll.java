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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = Protokoll.QUERY_ProtokollfindByCTyp, query = "SELECT OBJECT (O) FROM Protokoll o WHERE o.cTyp=?1"),
	@NamedQuery(name = Protokoll.QUERY_ProtokollfindByCTypCArt, query = "SELECT OBJECT (O) FROM Protokoll o WHERE o.cTyp=?1 AND o.cArt=?2"),
	@NamedQuery(name = Protokoll.QUERY_ProtokollfindAll, query = "SELECT OBJECT (O) FROM Protokoll o") })
@Entity
@Table(name = "LP_PROTOKOLL")
public class Protokoll implements Serializable {
	
	public static final String QUERY_ProtokollfindAll = "ProtokollfindAll" ;
	public static final String QUERY_ProtokollfindByCTyp = "ProtokollfindByCTyp" ;
	public static final String QUERY_ProtokollfindByCTypCArt = "ProtokollfindByCTypCArt" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_ART")
	private String cArt;

	@Column(name = "C_TYP")
	private String cTyp;

	@Column(name = "C_TEXT")
	private String cText;
	@Column(name = "C_LANGTEXT")
	private String cLangtext;

	@Column(name = "T_QUELLE")
	private Timestamp tQuelle;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	private static final long serialVersionUID = 1L;

	public Protokoll() {
		super();
	}

	public Protokoll(Integer id, String cArt,String cTyp, String cText,Integer personalIIdAnlegen, Timestamp tAnlegen) {

		setIId(id);
		setCArt(cArt);
		setCTyp(cTyp);
		setCText(cText);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(tAnlegen);
		

	}

	public String getCArt() {
		return cArt;
	}

	public void setCArt(String art) {
		cArt = art;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String typ) {
		cTyp = typ;
	}

	public String getCText() {
		return cText;
	}

	public void setCText(String text) {
		cText = text;
	}

	public String getCLangtext() {
		return cLangtext;
	}

	public void setCLangtext(String langtext) {
		cLangtext = langtext;
	}

	public Timestamp getTQuelle() {
		return tQuelle;
	}

	public void setTQuelle(Timestamp quelle) {
		tQuelle = quelle;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp anlegen) {
		tAnlegen = anlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	

	

}
