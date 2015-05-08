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

@NamedQueries( {
	@NamedQuery(name = LpUserCount.QueryAnzahlMandant, query = "SELECT OBJECT(o) FROM LpUserCount o WHERE o.tZeitpunkt>=?1 AND (SELECT rolle.iMaxUsers FROM Systemrolle rolle WHERE rolle.iId=o.systemrolleIId) IS NULL ORDER BY o.tZeitpunkt DESC"),
	@NamedQuery(name = LpUserCount.QueryAnzahlSystemrolle, query = "SELECT OBJECT(o) FROM LpUserCount o WHERE o.tZeitpunkt>=?1 AND o.systemrolleIId=?2 ORDER BY o.tZeitpunkt DESC")
	})
@Entity
@Table(name = "LP_USERCOUNT")
public class LpUserCount implements Serializable {
	
	private static final long serialVersionUID = -3761335275922030845L;
	public static final String QueryAnzahlMandant = "QueryAnzahlMandant";
	public static final String QueryAnzahlSystemrolle = "QueryAnzahlSystemrolle";
	
	@Id
	@Column(name = "T_ZEITPUNKT")
	private Timestamp tZeitpunkt;

	@Column(name = "I_ANZAHL")
	private Integer iAnzahl;

	@Column(name = "SYSTEMROLLE_I_ID")
	private Integer systemrolleIId;
	
	public Timestamp getTZeitpunkt() {
		return tZeitpunkt;
	}
	
	public Integer getIAnzahl() {
		return iAnzahl;
	}
	
	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}
	
	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}
	
	public void setIAnzahl(Integer iAnzahl) {
		this.iAnzahl = iAnzahl;
	}
	
	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}
}
