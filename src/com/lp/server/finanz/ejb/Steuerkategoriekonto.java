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
package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

	@NamedQueries({ @NamedQuery(name = "SteuerkategoriekontoBySteuerkategorieIId", 
			query = "SELECT OBJECT(o) FROM Steuerkategoriekonto o WHERE o.pk.steuerkategorieiid = ?1"),
	@NamedQuery(name = "SteuerkategoriekontoBySteuerkategorieIIdandMwStSatzBeziid", 
			query = "SELECT OBJECT(o) FROM Steuerkategoriekonto o WHERE o.pk.steuerkategorieiid = ?1 AND o.pk.mwstsatzbeziid = ?2"),
	@NamedQuery(name = "SteuerkategoriekontoByKontoIIdandMwStSatzBeziid", 
			query = "SELECT OBJECT(o) FROM Steuerkategoriekonto o WHERE (o.kontoIIdVk = ?1 OR o.kontoIIdEk = ?1 OR o.kontoIIdEinfuhrUst = ?1) AND o.pk.mwstsatzbeziid = ?2")
})
@Entity
@Table(name = "FB_STEUERKATEGORIEKONTO")
public class Steuerkategoriekonto  implements Serializable {
	@EmbeddedId
	private SteuerkategoriekontoPK pk;

	@Column(name = "STEUERKATEGORIE_I_ID", insertable = false, updatable = false)
	private Integer steuerkategorieIId;

	@Column(name = "MWSTSATZBEZ_I_ID", insertable = false, updatable = false)
	private Integer mwstsatzbezIId;

	@Column(name = "KONTO_I_ID_VK")
	private Integer kontoIIdVk;

	@Column(name = "KONTO_I_ID_EK")
	private Integer kontoIIdEk;

	@Column(name = "KONTO_I_ID_SKONTOVK")
	private Integer kontoIIdSkontoVk;

	@Column(name = "KONTO_I_ID_SKONTOEK")
	private Integer kontoIIdSkontoEk;

	@Column(name = "KONTO_I_ID_EINFUHRUST")
	private Integer kontoIIdEinfuhrUst;
	
	private static final long serialVersionUID = 1L;
	
	public Steuerkategoriekonto() {
		super();
	}

	public Steuerkategoriekonto(Integer steuerkategorieIId, Integer mwstsatzbezIId) {
		setPk(new SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezIId));
		setSteuerkategorieIId(steuerkategorieIId);
		setMwstsatzbezIId(mwstsatzbezIId);
	}

	public SteuerkategoriekontoPK getPk() {
		return this.pk;
	}

	public void setPk(SteuerkategoriekontoPK pk) {
		this.pk = pk;
	}
	
	public Steuerkategoriekonto(SteuerkategoriekontoPK pkI) {
		this.pk = new SteuerkategoriekontoPK();
		setSteuerkategorieIId(pkI.getSteuerkategorieiid());
		setMwstsatzbezIId(pkI.getMwstsatzbeziid());
	}
	
	public Integer getSteuerkategorieIId() {
		return steuerkategorieIId;
	}

	public void setSteuerkategorieIId(Integer steuerkategorieIId) {
		this.steuerkategorieIId = steuerkategorieIId;
	}

	public Integer getMwstsatzbezIId() {
		return this.mwstsatzbezIId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbezIId) {
		this.mwstsatzbezIId = mwstsatzbezIId;
	}

	public Integer getKontoIIdVk() {
		return kontoIIdVk;
	}

	public void setKontoIIdVk(Integer kontoIIdVk) {
		this.kontoIIdVk = kontoIIdVk;
	}

	public Integer getKontoIIdEk() {
		return kontoIIdEk;
	}

	public void setKontoIIdEk(Integer kontoIIdEk) {
		this.kontoIIdEk = kontoIIdEk;
	}

	public void setKontoIIdSkontoVk(Integer kontoIIdSkontoVk) {
		this.kontoIIdSkontoVk = kontoIIdSkontoVk;
	}

	public Integer getKontoIIdSkontoVk() {
		return kontoIIdSkontoVk;
	}

	public void setKontoIIdSkontoEk(Integer kontoIIdSkontoEk) {
		this.kontoIIdSkontoEk = kontoIIdSkontoEk;
	}

	public Integer getKontoIIdSkontoEk() {
		return kontoIIdSkontoEk;
	}

	public void setKontoIIdEinfuhrUst(Integer kontoIIdEinfuhrUst) {
		this.kontoIIdEinfuhrUst = kontoIIdEinfuhrUst;
	}

	public Integer getKontoIIdEinfuhrUst() {
		return kontoIIdEinfuhrUst;
	}
	
}
