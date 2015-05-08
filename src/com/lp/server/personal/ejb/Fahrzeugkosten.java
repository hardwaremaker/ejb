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
package com.lp.server.personal.ejb;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "FahrzeugkostenfindAktuelleKosten", query = "SELECT OBJECT(o) FROM Fahrzeugkosten o WHERE o.fahrzeugIId = ?1 AND o.tGueltigab <= ?2 ORDER BY o.tGueltigab DESC"),
		@NamedQuery(name = "FahrzeugkostenfindByFahrzeugIIdTGueltigab", query = "SELECT OBJECT(o) FROM Fahrzeugkosten o WHERE o.fahrzeugIId = ?1 AND o.tGueltigab = ?2") })
@Entity
@Table(name = "PERS_FAHRZEUGKOSTEN")
public class Fahrzeugkosten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	@Column(name = "N_KMKOSTEN")
	private BigDecimal nKmkosten;

	@Column(name = "FAHRZEUG_I_ID")
	private Integer fahrzeugIId;

	private static final long serialVersionUID = 1L;

	public Fahrzeugkosten() {
		super();
	}

	public Fahrzeugkosten(Integer fahrzeugIId,
			Timestamp gueltigab, 
			Integer id) {
		setFahrzeugIId(fahrzeugIId);
		
		setTGueltigab(gueltigab);
		setIId(id);
		setNKmkosten(new BigDecimal(0));
	}
	
	public Fahrzeugkosten(Integer id,
			Integer fahrzeugIId,
			Timestamp gueltigab, 
			BigDecimal kmkosten
			) {
		setFahrzeugIId(fahrzeugIId);
		
		setTGueltigab(gueltigab);
		setIId(id);
		setNKmkosten(kmkosten);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public BigDecimal getNKmkosten() {
		return this.nKmkosten;
	}

	public void setNKmkosten(BigDecimal nKmkosten) {
		this.nKmkosten = nKmkosten;
	}



	public Integer getFahrzeugIId() {
		return this.fahrzeugIId;
	}

	public void setFahrzeugIId(Integer fahrzeugIId) {
		this.fahrzeugIId = fahrzeugIId;
	}

}
