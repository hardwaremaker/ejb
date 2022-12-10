package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="LP_VERSANDWEGPARTNERCC")
@PrimaryKeyJoinColumn(name="I_ID")
public class VersandwegPartnerCC extends VersandwegPartner {
	private static final long serialVersionUID = 1544416896909638001L;
	
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
	
	
	public String getcKundennummer() {
		return cKundennummer;
	}

	public void setcKundennummer(String cKundennummer) {
		this.cKundennummer = cKundennummer;
	}

	public String getcKennwort() {
		return cKennwort;
	}

	public void setcKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}

	public String getcKeystore() {
		return cKeystore;
	}

	public void setcKeystore(String cKeystore) {
		this.cKeystore = cKeystore;
	}

	public String getcKeystoreKennwort() {
		return cKeystoreKennwort;
	}

	public void setcKeystoreKennwort(String cKeystoreKennwort) {
		this.cKeystoreKennwort = cKeystoreKennwort;
	}

	/**
	 * Welche Variante der Adresse soll f&uuml;r die Soko-Suche verwendet werden.
	 *  
	 * @return 0 ... Auftragsadresse, 1 ... Lieferadresse, 2 ... Rechnungsadresse
	 */
	public Integer getSokoAdresstyp() {
		return sokoAdresstyp;
	}

	public void setSokoAdresstyp(Integer sokoAdresstyp) {
		this.sokoAdresstyp = sokoAdresstyp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		result = prime * result
				+ ((sokoAdresstyp == null) ? 0 : sokoAdresstyp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersandwegPartnerCC other = (VersandwegPartnerCC) obj;
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
		if (sokoAdresstyp == null) {
			if (other.sokoAdresstyp != null)
				return false;
		} else if (!sokoAdresstyp.equals(other.sokoAdresstyp))
			return false;
		return true;
	}	
}
