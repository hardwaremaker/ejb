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
package com.lp.server.partner.service;

import java.io.Serializable;

/**
 * <p>
 * Diese Klasse kuemmert sich um Kundeselektionskriterien.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 18.11.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 14:03:08 $
 */
public class KundeSelectCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean bKunden = false;
	private boolean bInteressenten = false;
	private boolean bVersteckt = false;
	private boolean bAnsprechpartnerfktAuchOhne = false;
	private Integer iIdAnsprechpartnerfkt = null;
	private String sPLZ = null;
	private String cNrMandant = null;
	private Integer iIdSerienbrief = null;
	private String sAbsenderEMail = null;
	private String sLKZ = null;

	public KundeSelectCriteriaDto() {
	}

	public void setSPLZ(String sPLZ) {
		this.sPLZ = sPLZ;
	}

	public void setBKunden(boolean bKunden) {
		this.bKunden = bKunden;
	}

	public void setBInteressenten(boolean bInteressenten) {
		this.bInteressenten = bInteressenten;
	}

	public void setIIdAnsprechpartnerfkt(Integer iIdAnsprechpartnerfkt) {
		this.iIdAnsprechpartnerfkt = iIdAnsprechpartnerfkt;
	}

	public void setBVersteckt(boolean bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public void setBAnsprechpartnerfktAuchOhne(
			boolean bAnsprechpartnerfktAuchOhne) {
		this.bAnsprechpartnerfktAuchOhne = bAnsprechpartnerfktAuchOhne;
	}

	public void setCNrMandant(String cNrMandant) {
		this.cNrMandant = cNrMandant;
	}

	public void setIIdSerienbrief(Integer iIdSerienbrief) {
		this.iIdSerienbrief = iIdSerienbrief;
	}

	public void setSAbsenderEMail(String sAbsenderEMail) {
		this.sAbsenderEMail = sAbsenderEMail;
	}

	public void setSLKZ(String sLKZ) {
		this.sLKZ = sLKZ;
	}

	public String getSPLZ() {
		return sPLZ;
	}

	public boolean isBKunden() {
		return bKunden;
	}

	public boolean isBInteressenten() {
		return bInteressenten;
	}

	public Integer getIIdAnsprechpartnerfkt() {
		return iIdAnsprechpartnerfkt;
	}

	public boolean isBVersteckt() {
		return bVersteckt;
	}

	public boolean isBAnsprechpartnerfktAuchOhne() {
		return bAnsprechpartnerfktAuchOhne;
	}

	public String getCNrMandant() {
		return cNrMandant;
	}

	public Integer getIIdSerienbrief() {
		return iIdSerienbrief;
	}

	public String getSAbsenderEMail() {
		return sAbsenderEMail;
	}

	public String getSLKZ() {
		return sLKZ;
	}
}
