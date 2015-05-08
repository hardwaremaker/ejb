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
import javax.persistence.Table;

import com.lp.server.util.IIId;

@Entity
@Table(name = "LP_ENTITYLOG")
public class EntityLog implements Serializable, IIId {
	private static final long serialVersionUID = -8721840806236226683L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	/**
	 * Um welche Daten handelt es sich?</br>
	 * <p>Stueckliste, Stuecklisteposition, ...</p>
	 */
	@Column(name = "C_ENTITY_KEY")
	private String cEntityKey;

	/**
	 * Was wurde gemacht?</br>
	 * <p>CREATE, UPDATE, DELETE</p>
	 */
	@Column(name = "C_OPERATION")
	private String cOperation;

	/**
	 * Die Iid(Integer) der Entity, bzw. die Schluesselwerte bei Mehrfach-Keys</br>
	 */
	@Column(name = "ENTITY_I_ID")
	private String entityIId;

	/**
	 * Die IId des "Kopf"satzes</br>
	 * <p>Auftragposition haben als Head_Id den Auftragskopf, usw.</p>
	 */
	@Column(name = "FILTER_I_ID")
	private String filterIId;

	/**
	 * Um welche Daten handelt es sich bei den Kopfdaten?</br>
	 * <p>Stueckliste, Zeitdaten, ...</p>
	 */
	@Column(name = "C_FILTER_KEY")
	private String cFilterKey;
	
	/**
	 * Welches "Feld" wurde ver&auml;ndert
	 */
	@Column(name = "C_KEY")
	private String cKey;

	/**
	 * Der Ausgangswert</br>
	 * <p>Bei INSERT ist das der Wert der eingef&uuml;gt wurde
	 */
	@Column(name = "C_VON")
	private String cVon;

	/**
	 * Der Neue Wert
	 */
	@Column(name = "C_NACH")
	private String cNach;

	@Column(name = "LOCALE_C_NR")
	private String localeCNr;

	/**
	 * Wann wurde geaendert
	 */
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	/**
	 * Wer hat geaendert
	 */
	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	public EntityLog() {
	}
	
	public EntityLog(Integer iId, String cEntityKey, String cOperation,
			String entityIId, String cKey, String cVon, String cNach,
			String localeCNr, Timestamp tAendern, Integer personalIId) {
		super();
		this.iId = iId;
		this.cEntityKey = cEntityKey;
		this.cOperation = cOperation;
		this.entityIId = entityIId;
		this.cKey = cKey;
		this.cVon = cVon;
		this.cNach = cNach;
		this.localeCNr = localeCNr;
		this.tAendern = tAendern;
		this.personalIId = personalIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCEntityKey() {
		return cEntityKey;
	}

	public void setCEntityKey(String cEntityKey) {
		this.cEntityKey = cEntityKey;
	}

	public String getCOperation() {
		return cOperation;
	}

	public void setCOperation(String cOperation) {
		this.cOperation = cOperation;
	}

	public String getEntityIId() {
		return entityIId;
	}

	public void setEntityIId(String entityIId) {
		this.entityIId = entityIId;
	}

	public String getCKey() {
		return cKey;
	}

	public void setCKey(String cKey) {
		this.cKey = cKey;
	}

	public String getCVon() {
		return cVon;
	}

	public void setCVon(String cVon) {
		this.cVon = cVon;
	}

	public String getCNach() {
		return cNach;
	}

	public void setCNach(String cNach) {
		this.cNach = cNach;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}


	public String getFilterIId() {
		return filterIId;
	}

	public void setFilterIId(String filterIId) {
		this.filterIId = filterIId;
	}


	public String getCFilterKey() {
		return cFilterKey;
	}

	public void setCFilterKey(String cFilterKey) {
		this.cFilterKey = cFilterKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cEntityKey == null) ? 0 : cEntityKey.hashCode());
		result = prime * result
				+ ((cFilterKey == null) ? 0 : cFilterKey.hashCode());
		result = prime * result + ((cKey == null) ? 0 : cKey.hashCode());
		result = prime * result + ((cNach == null) ? 0 : cNach.hashCode());
		result = prime * result
				+ ((cOperation == null) ? 0 : cOperation.hashCode());
		result = prime * result + ((cVon == null) ? 0 : cVon.hashCode());
		result = prime * result
				+ ((entityIId == null) ? 0 : entityIId.hashCode());
		result = prime * result
				+ ((filterIId == null) ? 0 : filterIId.hashCode());
		result = prime * result
				+ ((localeCNr == null) ? 0 : localeCNr.hashCode());
		result = prime * result
				+ ((personalIId == null) ? 0 : personalIId.hashCode());
		result = prime * result
				+ ((tAendern == null) ? 0 : tAendern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityLog other = (EntityLog) obj;
		if (cEntityKey == null) {
			if (other.cEntityKey != null)
				return false;
		} else if (!cEntityKey.equals(other.cEntityKey))
			return false;
		if (cFilterKey == null) {
			if (other.cFilterKey != null)
				return false;
		} else if (!cFilterKey.equals(other.cFilterKey))
			return false;
		if (cKey == null) {
			if (other.cKey != null)
				return false;
		} else if (!cKey.equals(other.cKey))
			return false;
		if (cNach == null) {
			if (other.cNach != null)
				return false;
		} else if (!cNach.equals(other.cNach))
			return false;
		if (cOperation == null) {
			if (other.cOperation != null)
				return false;
		} else if (!cOperation.equals(other.cOperation))
			return false;
		if (cVon == null) {
			if (other.cVon != null)
				return false;
		} else if (!cVon.equals(other.cVon))
			return false;
		if (entityIId == null) {
			if (other.entityIId != null)
				return false;
		} else if (!entityIId.equals(other.entityIId))
			return false;
		if (filterIId == null) {
			if (other.filterIId != null)
				return false;
		} else if (!filterIId.equals(other.filterIId))
			return false;
		if (localeCNr == null) {
			if (other.localeCNr != null)
				return false;
		} else if (!localeCNr.equals(other.localeCNr))
			return false;
		if (personalIId == null) {
			if (other.personalIId != null)
				return false;
		} else if (!personalIId.equals(other.personalIId))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		return true;
	}
}
