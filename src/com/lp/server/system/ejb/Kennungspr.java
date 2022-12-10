package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@Entity
@Table(name = ITablenames.LP_KENNUNGSPR)
public class Kennungspr implements Serializable {
	private static final long serialVersionUID = 1088849709041498885L;

	@EmbeddedId
	private KennungsprPK pk;
	
	@Column(name = "C_BEZ")
	private String cBez;

	public Kennungspr() {
	}
	
	public Kennungspr(Integer kennungId, String localeCnr, String bezeichnung) {
		pk = new KennungsprPK(kennungId, localeCnr);
		setCBez(bezeichnung);
	}
	
	public KennungsprPK getPk() {
		return pk;
	}
	
	public void setPk(KennungsprPK pk) {
		this.pk = pk;
	}
	
	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cBez == null) ? 0 : cBez.hashCode());
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
		Kennungspr other = (Kennungspr) obj;
		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}
}
