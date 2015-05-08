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
package com.lp.server.system.service;

import java.io.Serializable;
import java.util.Date;

/**
 *  Dieses Dto enth&auml;lt nur die n&ouml;tigen Felder, welche
 *  f&uuml;r die <b>offiziellen</b> Texte ben&ouml;tigt werden:
 *  <li>iId</li>
 *  <li>cToken</li>
 *  <li>cText</li>
 *  <li>tAendern</li>
 *  <li>cLocale</li><br>
 *  Da die Personal-ID immer die vom LPAdmin ist
 *  und der Mandant immer der Hauptmandant, sind diese
 *  Felder &uuml;berfl&uuml;ssig.
 * 	<br><b>ACHTUNG! DIESE KLASSE GIBT ES 1:1 IM UPDATEMANAGER UND
 * 	MUSS SYNCHRON GEHALTEN WERDEN!
 * @author robert
 *
 */
public class LpHvDirekthilfeDto implements Serializable {

	private static final long serialVersionUID = -1708496333197152359L;
	private Integer iId;
	private String cToken;
	private String cText;
	private Date tAendern;
	private String cLocale;
	
	public LpHvDirekthilfeDto() {
	}
	
	public LpHvDirekthilfeDto(Integer iId, String cToken, String cText, Date tAendern,
			String cLocale) {
		this.iId = iId;
		this.cToken = cToken;
		this.cText = cText;
		this.tAendern = tAendern;
		this.cLocale = cLocale;
	}
	
	public String getcToken() {
		return cToken;
	}
	public void setcToken(String cToken) {
		this.cToken = cToken;
	}
	public String getcText() {
		return cText;
	}
	public void setcText(String cText) {
		this.cText = cText;
	}
	public Date gettAendern() {
		return tAendern;
	}
	public void settAendern(Date tAendern) {
		this.tAendern = tAendern;
	}
	public String getcLocale() {
		return cLocale;
	}
	public void setcLocale(String cLocale) {
		this.cLocale = cLocale;
	}
	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cLocale == null) ? 0 : cLocale.hashCode());
		result = prime * result + ((cText == null) ? 0 : cText.hashCode());
		result = prime * result + ((cToken == null) ? 0 : cToken.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
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
		LpHvDirekthilfeDto other = (LpHvDirekthilfeDto) obj;
		if (cLocale == null) {
			if (other.cLocale != null)
				return false;
		} else if (!cLocale.equals(other.cLocale))
			return false;
		if (cText == null) {
			if (other.cText != null)
				return false;
		} else if (!cText.equals(other.cText))
			return false;
		if (cToken == null) {
			if (other.cToken != null)
				return false;
		} else if (!cToken.equals(other.cToken))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		return true;
	}
	
	
}
