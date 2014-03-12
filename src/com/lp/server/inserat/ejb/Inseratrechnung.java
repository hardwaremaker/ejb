/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.inserat.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "InseratrechnungfindInseratIIdRechnungpositionIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.inseratIId=?1 AND o.rechnungpositionIId=?2"),
		@NamedQuery(name = "InseratrechnungfindInseratIIdKundeIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.inseratIId=?1 AND o.kundeIId=?2"),
		@NamedQuery(name = "InseratrechnungfindByKundeIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.kundeIId=?1"),
		@NamedQuery(name = "InseratrechnungfindByAnsprechpartnerIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.ansprechpartnerIId=?1"),
		@NamedQuery(name = "InseratrechnungfindByRechnungpositionIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.rechnungpositionIId=?1"),
		@NamedQuery(name = "InseratrechnungfindDenErstenEintragByInseratIId", query = "SELECT OBJECT (o) FROM Inseratrechnung o WHERE o.inseratIId=?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "InseratrechnungejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Inseratrechnung o WHERE o.inseratIId = ?1") })
@Entity
@Table(name = "IV_INSERATRECHNUNG")
public class Inseratrechnung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "INSERAT_I_ID")
	private Integer inseratIId;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "RECHNUNGPOSITION_I_ID")
	private Integer rechnungpositionIId;

	public Integer getInseratIId() {
		return inseratIId;
	}

	public void setInseratIId(Integer inseratIId) {
		this.inseratIId = inseratIId;
	}

	public Integer getRechnungpositionIId() {
		return rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
	}

	public Integer getiSort() {
		return iSort;
	}

	public void setiSort(Integer iSort) {
		this.iSort = iSort;
	}

	@Column(name = "I_SORT")
	private Integer iSort;

	private static final long serialVersionUID = 1L;

	public Inseratrechnung(Integer id, Integer inseratIId, Integer kundeIId,
			Integer iSort) {
		setIId(id);
		setInseratIId(inseratIId);
		setKundeIId(kundeIId);
		setiSort(iSort);

	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Inseratrechnung() {

	}

}
