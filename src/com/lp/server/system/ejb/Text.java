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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "TextfindByMandantCNrLocaleCNr", query = "SELECT OBJECT (o) FROM Text o WHERE o.pk.mandantCNr = ?1 AND  o.pk.localeCNr = ?2"),
		@NamedQuery(name = "TextfindAll", query = "SELECT OBJECT (o) FROM Text o") })
@Entity
@Table(name = "LP_TEXT")
public class Text implements Serializable {
	@EmbeddedId
	private TextPK pk;

	@Column(name = "C_TEXT")
	private String cText;

	private static final long serialVersionUID = 1L;

	public Text() {
		super();
	}

	public Text(String lowerCase, String mandantCNr2, String localeCNr2,
			String text) {
		setPk(new TextPK(lowerCase, mandantCNr2, localeCNr2));
		setCText(text);
	}

	public TextPK getPk() {
		return this.pk;
	}

	public void setPk(TextPK pk) {
		this.pk = pk;
	}

	public String getCText() {
		return this.cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

}
