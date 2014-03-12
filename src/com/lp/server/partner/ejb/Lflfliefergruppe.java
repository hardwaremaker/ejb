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
package com.lp.server.partner.ejb;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQueries( {
	@NamedQuery(name = "LflfliefergruppefindByLiefergruppeIId", query = "SELECT OBJECT (o) FROM Lflfliefergruppe o WHERE o.lflfliefergruppePK.lfliefergruppeIId=?1"),
	@NamedQuery(name = "LflfliefergruppefindByLieferantIId", query = "SELECT OBJECT (O) FROM Lflfliefergruppe o WHERE o.lflfliefergruppePK.lieferantIId=?1"),
	@NamedQuery(name = "LflfliefergruppefindByLieferantIIdLiefergruppeIId", query = "SELECT OBJECT (O) FROM Lflfliefergruppe o WHERE o.lflfliefergruppePK.lieferantIId=?1 AND o.lflfliefergruppePK.lfliefergruppeIId=?2") })

@Entity
@Table(name = "PART_LFLFLIEFERGRUPPE")
public class Lflfliefergruppe implements Serializable {
	public Lflfliefergruppe() {
		// TODO Auto-generated constructor stub
	}

	@EmbeddedId
	private LflfliefergruppePK lflfliefergruppePK;

	private static final long serialVersionUID = 1L;

	public Lflfliefergruppe(Integer lieferantIId2, Integer lfliefergruppeIId) {
		this.lflfliefergruppePK=new LflfliefergruppePK(lieferantIId2,lfliefergruppeIId);
		setLieferantIId(lieferantIId2);
		setLfliefergruppeIId(lfliefergruppeIId);
	}

	public Integer getLieferantIId() {
		return this.lflfliefergruppePK.getLieferantIId();
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lflfliefergruppePK.setLieferantIId(lieferantIId);
	}

	public Integer getLfliefergruppeIId() {
		return this.lflfliefergruppePK.getLfliefergruppeIId();
	}

	public void setLfliefergruppeIId(Integer lfliefergruppeIId) {
		this.lflfliefergruppePK.setLfliefergruppeIId(lfliefergruppeIId);
	}

}
