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

import javax.persistence.Transient;

public class VersandwegCCPartnerDto implements IVersandwegPartnerDto {

	public enum SokoAdresstyp {
		AUFTRAGS_ADRESSE,
		LIEFER_ADRESSE,
		RECHNUNGS_ADRESSE
	} ;
	
	private String cKundennummer ;
	private String cKennwort ;
	private Integer iid ;
	private Integer versandwegId ;
	private Integer partnerIId ;
	private String cKeystore ;
	private String cKeystoreKennwort ;
	private Integer sokoAdresstyp ;
	
	@Override
	public Integer getIId() {
		return iid ;
	}

	@Override
	public void setIId(Integer iId) {
		this.iid = iId ;
	}

	@Override
	public Integer getPartnerIId() {
		return partnerIId ;
	}

	@Override
	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId ;
	}

	public Integer getVersandwegId() {
		return versandwegId;
	}

	public void setVersandwegId(Integer versandwegId) {
		this.versandwegId = versandwegId;
	}

	public String getCKundennummer() {
		return cKundennummer;
	}

	public void setCKundennummer(String cKundennummer) {
		this.cKundennummer = cKundennummer;
	}

	public String getCKennwort() {
		return cKennwort;
	}

	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}	
	
	public String getCKeystore() {
		return cKeystore;
	}
	public void setCKeystore(String cKeystore) {
		this.cKeystore = cKeystore;
	}

	public String getCKeystoreKennwort() {
		return cKeystoreKennwort;
	}

	public void setCKeystoreKennwort(String cKeystoreKennwort) {
		this.cKeystoreKennwort = cKeystoreKennwort;
	}

	public Integer getISokoAdresstyp() {
		return sokoAdresstyp;
	}

	public void setISokoAdresstyp(Integer sokoAdresstyp) {
		this.sokoAdresstyp = sokoAdresstyp;
	}
	
	@Transient
	public SokoAdresstyp getSokoAdresstyp() {
		return SokoAdresstyp.values()[sokoAdresstyp] ;
	}
}
