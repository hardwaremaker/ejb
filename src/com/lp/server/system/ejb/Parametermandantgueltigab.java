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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "ParametermandantgueltigabFindByPK", query = "SELECT OBJECT (o) FROM Parametermandantgueltigab o WHERE o.pk.mandantCNr=?1 AND o.pk.cNr=?2 AND o.pk.cKategorie=?3 AND o.pk.tGueltigab=?4"),
		@NamedQuery(name = "ParametermandantgueltigabFindByMandantCNrCNrCKategorie", query = "SELECT OBJECT (o) FROM Parametermandantgueltigab o WHERE o.pk.mandantCNr=?1 AND o.pk.cNr=?2 AND o.pk.cKategorie=?3") })
@Entity
@Table(name = "LP_PARAMETERMANDANTGUELTIGAB")
public class Parametermandantgueltigab implements Serializable {
	@EmbeddedId
	private ParametermandantgueltigabPK pk;

	@Column(name = "C_WERT")
	private String cWert;

	private static final long serialVersionUID = 1L;

	public Parametermandantgueltigab() {
		super();
	}

	public Parametermandantgueltigab(String nr, String mandantCMandant,
			String kategorie, String wert, Timestamp tGueltigab) {
		setPk(new ParametermandantgueltigabPK(nr, mandantCMandant, kategorie,
				tGueltigab));

		setCWert(wert);

	}

	public ParametermandantgueltigabPK getPk() {
		return this.pk;
	}

	public void setPk(ParametermandantgueltigabPK pk) {
		this.pk = pk;
	}

	public String getCWert() {
		return cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

}
