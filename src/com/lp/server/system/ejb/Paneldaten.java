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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey", query = "SELECT OBJECT (o) FROM Paneldaten o WHERE o.panelCNr=?1 AND o.panelbeschreibungIId=?2 AND o.cKey=?3"),
		@NamedQuery(name = "PaneldatenfindByPanelCNrCKey", query = "SELECT OBJECT (o) FROM Paneldaten o WHERE o.panelCNr=?1 AND o.cKey=?2") })
@Entity
@Table(name = "LP_PANELDATEN")
public class Paneldaten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_KEY")
	private String cKey;

	@Column(name = "C_DATENTYPKEY")
	private String cDatentypkey;

	@Column(name = "X_INHALT")
	private String xInhalt;

	@Column(name = "O_INHALT")
	private byte[] oInhalt;

	@Column(name = "PANEL_C_NR")
	private String panelCNr;

	@Column(name = "PANELBESCHREIBUNG_I_ID")
	private Integer panelbeschreibungIId;

	private static final long serialVersionUID = 1L;

	public Paneldaten() {
		super();
	}

	public Paneldaten(Integer id, String panelCNr,
			Integer panelbeschreibungIId, String key, String datentypkey) {
		setIId(id);
		setPanelCNr(panelCNr);
		setPanelbeschreibungIId(panelbeschreibungIId);
		setCKey(key);
	    setCDatentypkey(datentypkey);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKey() {
		return this.cKey;
	}

	public void setCKey(String cKey) {
		this.cKey = cKey;
	}

	public String getCDatentypkey() {
		return this.cDatentypkey;
	}

	public void setCDatentypkey(String cDatentypkey) {
		this.cDatentypkey = cDatentypkey;
	}

	public String getXInhalt() {
		return this.xInhalt;
	}

	public void setXInhalt(String xInhalt) {
		this.xInhalt = xInhalt;
	}

	public byte[] getOInhalt() {
		return this.oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public String getPanelCNr() {
		return this.panelCNr;
	}

	public void setPanelCNr(String panel) {
		this.panelCNr = panel;
	}

	public Integer getPanelbeschreibungIId() {
		return this.panelbeschreibungIId;
	}

	public void setPanelbeschreibungIId(Integer panelbeschreibung) {
		this.panelbeschreibungIId = panelbeschreibung;
	}

}
