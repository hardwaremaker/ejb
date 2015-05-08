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
		@NamedQuery(name = "DiaetentagessatzfindByDiatenIIdTGueltigab", query = "SELECT OBJECT(o) FROM Diaetentagessatz o WHERE o.diaetenIId = ?1 AND o.tGueltigab = ?2"),
		@NamedQuery(name = "DiaetentagessatzfindGueltigenTagessatzZuDatum", query = "SELECT OBJECT(o) FROM Diaetentagessatz o WHERE o.diaetenIId = ?1 AND o.tGueltigab<= ?2 ORDER BY o.tGueltigab DESC") })
@Entity
@Table(name = "PERS_DIAETENTAGESSATZ")
public class Diaetentagessatz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	@Column(name = "I_ABSTUNDEN")
	private Integer iAbstunden;

	@Column(name = "B_STUNDENWEISE")
	private Short bStundenweise;

	@Column(name = "N_STUNDENSATZ")
	private BigDecimal nStundensatz;

	@Column(name = "N_TAGESSATZ")
	private BigDecimal nTagessatz;

	@Column(name = "N_MINDESTSATZ")
	private BigDecimal nMindestsatz;

	@Column(name = "DIAETEN_I_ID")
	private Integer diaetenIId;


	@Column(name = "C_FILENAME_SCRIPT")
	private String cFilenameScript;

	public String getCFilenameScript() {
		return cFilenameScript;
	}

	public void setCFilenameScript(String cFilenameScript) {
		this.cFilenameScript = cFilenameScript;
	}
	private static final long serialVersionUID = 1L;

	public Diaetentagessatz() {
		super();
	}

	public Diaetentagessatz(Integer id, Integer diaetenIId,
			Timestamp gueltigab, Integer abstunden, Short stundenweise,
			BigDecimal stundensatz, BigDecimal tagessatz, BigDecimal mindestsatz) {
		setIId(id);
		setDiaetenIId(diaetenIId);
		setTGueltigab(gueltigab);
		setIAbstunden(abstunden);
		setBStundenweise(stundenweise);
		setNStundensatz(stundensatz);
		setNTagessatz(tagessatz);
		setNMindestsatz(mindestsatz);
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

	public Integer getIAbstunden() {
		return this.iAbstunden;
	}

	public void setIAbstunden(Integer iAbstunden) {
		this.iAbstunden = iAbstunden;
	}

	public Short getBStundenweise() {
		return this.bStundenweise;
	}

	public void setBStundenweise(Short bStundenweise) {
		this.bStundenweise = bStundenweise;
	}

	public BigDecimal getNStundensatz() {
		return this.nStundensatz;
	}

	public void setNStundensatz(BigDecimal nStundensatz) {
		this.nStundensatz = nStundensatz;
	}

	public BigDecimal getNTagessatz() {
		return this.nTagessatz;
	}

	public void setNTagessatz(BigDecimal nTagessatz) {
		this.nTagessatz = nTagessatz;
	}

	public BigDecimal getNMindestsatz() {
		return this.nMindestsatz;
	}

	public void setNMindestsatz(BigDecimal nMindestsatz) {
		this.nMindestsatz = nMindestsatz;
	}

	public Integer getDiaetenIId() {
		return this.diaetenIId;
	}

	public void setDiaetenIId(Integer diaetenIId) {
		this.diaetenIId = diaetenIId;
	}

}
