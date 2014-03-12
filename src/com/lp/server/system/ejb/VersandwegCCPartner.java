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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.IVersandwegPartner;

@NamedQueries({
	@NamedQuery(name=VersandwegCCPartnerQuery.ByVersandwegIIdPartnerIId, 
			query = "SELECT OBJECT(C) FROM VersandwegCCPartner c WHERE c.partnerId = :partnerId AND c.versandwegId = :versandwegId")
})
@Entity
@Table(name="LP_VERSANDWEGCCPARTNER")
//@PrimaryKeyJoinColumns({
//	@PrimaryKeyJoinColumn(name="I_ID"),
//	@PrimaryKeyJoinColumn(name="PARTNER_I_ID")
//})
public class VersandwegCCPartner implements Serializable, IVersandwegPartner {
	private static final long serialVersionUID = 5390354347904059127L;

	@Id
	@TableGenerator(name="versandwegcc_id", table="lp_primarykey",
	pkColumnName = "c_name", pkColumnValue="versandwegccpartner", valueColumnName="i_index", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="versandwegcc_id")
	@Column(name="I_ID") 
	private Integer iId ;

	@Column(name="VERSANDWEG_I_ID")
	private Integer versandwegId ;
	
	@Column(name="PARTNER_I_ID")
	private Integer partnerId ;
	
	@Column(name="C_KUNDENNUMMER")
	private String cKundennummer ;

	@Column(name="C_KENNWORT")
	private String cKennwort ;

	@Column(name="C_KEYSTORE") 
	private String cKeystore ;

	@Column(name="C_KEYSTOREKENNWORT") 
	private String cKeystoreKennwort ;
	
	@Column(name="I_SOKO_ADRESSTYP")
	private Integer sokoAdresstyp ;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public Integer getPartnerIId() {
		return partnerId;
	}
	public void setPartnerIId(Integer partnerId) {
		this.partnerId = partnerId;
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
	
	/**
	 * Welche Variante der Adresse soll f&uuml;r die Soko-Suche verwendet werden.
	 *  
	 * @return 0 ... Auftragsadresse, 1 ... Lieferadresse, 2 ... Rechnungsadresse
	 */
	public Integer getISokoAdresstyp() {
		return sokoAdresstyp;
	}
	
	public void setISokoAdresstyp(Integer sokoAdresstyp) {
		this.sokoAdresstyp = sokoAdresstyp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cKennwort == null) ? 0 : cKennwort.hashCode());
		result = prime * result
				+ ((cKeystore == null) ? 0 : cKeystore.hashCode());
		result = prime
				* result
				+ ((cKeystoreKennwort == null) ? 0 : cKeystoreKennwort
						.hashCode());
		result = prime * result
				+ ((cKundennummer == null) ? 0 : cKundennummer.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((partnerId == null) ? 0 : partnerId.hashCode());
		result = prime * result
				+ ((sokoAdresstyp == null) ? 0 : sokoAdresstyp.hashCode());
		result = prime * result
				+ ((versandwegId == null) ? 0 : versandwegId.hashCode());
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
		VersandwegCCPartner other = (VersandwegCCPartner) obj;
		if (cKennwort == null) {
			if (other.cKennwort != null)
				return false;
		} else if (!cKennwort.equals(other.cKennwort))
			return false;
		if (cKeystore == null) {
			if (other.cKeystore != null)
				return false;
		} else if (!cKeystore.equals(other.cKeystore))
			return false;
		if (cKeystoreKennwort == null) {
			if (other.cKeystoreKennwort != null)
				return false;
		} else if (!cKeystoreKennwort.equals(other.cKeystoreKennwort))
			return false;
		if (cKundennummer == null) {
			if (other.cKundennummer != null)
				return false;
		} else if (!cKundennummer.equals(other.cKundennummer))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (partnerId == null) {
			if (other.partnerId != null)
				return false;
		} else if (!partnerId.equals(other.partnerId))
			return false;
		if (sokoAdresstyp == null) {
			if (other.sokoAdresstyp != null)
				return false;
		} else if (!sokoAdresstyp.equals(other.sokoAdresstyp))
			return false;
		if (versandwegId == null) {
			if (other.versandwegId != null)
				return false;
		} else if (!versandwegId.equals(other.versandwegId))
			return false;
		return true;
	}
}
