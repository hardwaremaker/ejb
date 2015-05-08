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
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( 
		{
			@NamedQuery(name = Wechselkurs.QueryFindByVonZu, query = "SELECT OBJECT(o) FROM Wechselkurs o WHERE o.pk.waehrungCNrVon=?1 AND o.pk.waehrungCNrZu=?2 ORDER BY o.pk.tDatum ASC"),
			@NamedQuery(name = Wechselkurs.QueryFindByMandantenwaehrung, query = "SELECT OBJECT(o) FROM Wechselkurs o WHERE o.pk.waehrungCNrVon=?1 ORDER BY o.pk.tDatum ASC")
		})
@Entity
@Table(name = "LP_WECHSELKURS")
public class Wechselkurs implements Serializable {

	public final static String QueryFindByVonZu = "WechselkursfindByVonZu" ;
	public final static String QueryFindByMandantenwaehrung = "WechselkursfindByMandantenwaehrung" ;
	
	@EmbeddedId
	private WechselkursPK pk;

	@Column(name = "N_KURS")
	private BigDecimal nKurs;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Wechselkurs() {
		super();
	}

	public Wechselkurs(String waehrungCNrVon2,
			String waehrungCNrZu2,
			Date datum,
			BigDecimal kurs, 
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2) {
		WechselkursPK pk = new WechselkursPK(waehrungCNrVon2, waehrungCNrZu2,
				datum);
		setPk(pk);
		setNKurs(kurs);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}
	
	public Wechselkurs(String waehrungCNrVon2,
			String waehrungCNrZu2,
			Timestamp datum,
			BigDecimal kurs, 
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2) {
		this(waehrungCNrVon2,waehrungCNrZu2,new Date(datum.getTime()),kurs,personalIIdAnlegen2,personalIIdAendern2);
	}

	public WechselkursPK getPk() {
		return this.pk;
	}

	public void setPk(WechselkursPK pk) {
		this.pk = pk;
	}

	public BigDecimal getNKurs() {
		return this.nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
