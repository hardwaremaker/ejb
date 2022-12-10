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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@Entity
@Table(name = ITablenames.PART_BEAUSKUNFTUNG)
public class Beauskunftung implements Serializable {

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "B_KOSTENPFLICHTIG")
	private Short bKostenpflichtig;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "IDENTIFIKATION_I_ID")
	private Integer identifikationIId;

	public Integer getIdentifikationIId() {
		return identifikationIId;
	}

	public void setIdentifikationIId(Integer identifikationIId) {
		this.identifikationIId = identifikationIId;
	}

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Beauskunftung() {
		super();
	}

	public Beauskunftung(Integer iId, Integer partnerIId,
			Integer identifikationIId, Short bKostenpflichtig,
			Integer personalIIdAnlegen, Timestamp tAnlegen) {
		setIId(iId);

		setPartnerIId(partnerIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(tAnlegen);
		setBKostenpflichtig(bKostenpflichtig);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Short getBKostenpflichtig() {
		return this.bKostenpflichtig;
	}

	public void setBKostenpflichtig(Short bKostenpflichtig) {
		this.bKostenpflichtig = bKostenpflichtig;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
