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
package com.lp.server.projekt.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "HistoryartFindByCBez", query = "SELECT OBJECT(o) FROM Historyart o WHERE o.cBez = ?1"),
	 @NamedQuery(name = "HistoryartFindAllBInAuswahllisteAnzeigen", query = "SELECT OBJECT(o) FROM Historyart o WHERE o.bInAuswahllisteAnzeigen = 1") })
@Entity
@Table(name = "PROJ_HISTORYART")
public class Historyart implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "I_ROT")
	private Integer iRot;

	@Column(name = "I_GRUEN")
	private Integer iGruen;

	@Column(name = "I_BLAU")
	private Integer iBlau;
	
	@Column(name = "B_IN_AUSWAHLLISTE_ANZEIGEN")
	private Short bInAuswahllisteAnzeigen;
	
	public Short getBInAuswahllisteAnzeigen() {
		return bInAuswahllisteAnzeigen;
	}

	public void setBInAuswahllisteAnzeigen(Short bInAuswahllisteAnzeigen) {
		this.bInAuswahllisteAnzeigen = bInAuswahllisteAnzeigen;
	}

	@Column(name = "B_AKTUALISIEREZIELTERMIN")
	private Short bAktualisierezieltermin;

	public Short getBAktualisierezieltermin() {
		return bAktualisierezieltermin;
	}

	public void setBAktualisierezieltermin(Short bAktualisierezieltermin) {
		this.bAktualisierezieltermin = bAktualisierezieltermin;
	}

	public Integer getIRot() {
		return iRot;
	}

	public void setIRot(Integer iRot) {
		this.iRot = iRot;
	}

	public Integer getIGruen() {
		return iGruen;
	}

	public void setIGruen(Integer iGruen) {
		this.iGruen = iGruen;
	}

	public Integer getIBlau() {
		return iBlau;
	}

	public void setIBlau(Integer iBlau) {
		this.iBlau = iBlau;
	}

	private static final long serialVersionUID = 1L;

	public Historyart() {
		super();
	}

	public Historyart(Integer id, String cBez, Integer iRot, Integer iGruen,
			Integer iBlau,Short bAktualisierezieltermin, Short bInAuswahllisteAnzeigen) {
		setIId(id);
		setCBez(cBez);
		setIRot(iRot);
		setIGruen(iGruen);
		setIBlau(iBlau);
		setBAktualisierezieltermin(bAktualisierezieltermin);
		setBInAuswahllisteAnzeigen(bInAuswahllisteAnzeigen);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

}
